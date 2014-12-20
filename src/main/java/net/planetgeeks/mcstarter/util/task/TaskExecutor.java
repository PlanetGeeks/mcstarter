package net.planetgeeks.mcstarter.util.task;

import static net.planetgeeks.mcstarter.util.task.TaskExecutor.Status.IDLE;
import static net.planetgeeks.mcstarter.util.task.TaskExecutor.Status.STARTED;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.NonNull;

/**
 * Manages different tasks and runs them concurrently. The whole task can be
 * monitored.
 * <p>
 * A default <code>ExecutorService</code> will be created if you don't set a
 * custom one.
 * 
 * @author Flood2d
 *
 * @param <T> a {@link #Task} <code>Type</code>
 * @param <E> the <code>Type</code> returned by the <code>call()</code> method
 *            of {@link #T}.
 */
public class TaskExecutor<T extends Task<E>, E> extends
		ProgressTask<List<Future<E>>>
{
	private ExecutorService executor;
	private List<T> tasks = new ArrayList<>();
	private List<Future<E>> futures = new ArrayList<>();

	/**
	 * Set the {@link #ExecutorService} that will be used by this
	 * {@link #TaskExecutor}.
	 * <p>
	 * Must be called before {@link #call()}.
	 * 
	 * @param executor - the {@link #ExecutorService} to set.
	 */
	public synchronized ExecutorService setExecutorService(@NonNull ExecutorService executor)
	{
		if (executor != null)
			expectStatus(IDLE);

		return (this.executor = executor);
	}

	/**
	 * Get the {@link #ExecutorService} used by this {@link #TaskExecutor}.
	 * 
	 * @return an {@link #ExecutorService},
	 */
	public synchronized ExecutorService getExecutorService()
	{
		return executor != null ? executor : setExecutorService(Executors.newSingleThreadExecutor());
	}

	/**
	 * Add a {@link #Task} object to the task list.
	 * <p>
	 * Must be called before {@link #call()}
	 * 
	 * @param task - a {@link #Task} to submit to submit.
	 */
	public synchronized void submit(@NonNull T task)
	{
		expectStatus(IDLE);

		tasks.add(task);
		
		addSubTask(task);
	}

	/**
	 * Start all tasks previously submitted and return a list of relative
	 * {@link #Future}.
	 * <p>
	 * Must be called once!
	 * 
	 * @return a list of {@link #Future}.
	 */
	@Override
	public synchronized List<Future<E>> call()
	{
		expectStatus(IDLE);

		ExecutorService executor = getExecutorService();

		synchronized (futures)
		{
			for (T task : tasks)
				futures.add(executor.submit(task));
		}

		executor.shutdown();

		return getFutures();
	}

	@Override
	public synchronized void interrupt()
	{
		expectStatus(STARTED);

		super.interrupt();
	}

	@Override
	public synchronized void pause()
	{
		expectStatus(STARTED);

		super.pause();
	}

	@Override
	public synchronized void resume()
	{
		expectStatus(STARTED);
		
		super.resume();

		setStatus(Task.Status.RUNNING);
	}

	/**
	 * Get a copy of the list containing all tasks.
	 * <p>
	 * Must be called after {@link #call()}.
	 * 
	 * @return a list of {@link #Task}
	 */
	public synchronized List<T> getTasks()
	{
		List<T> tasks = new ArrayList<>();

		for(T task : this.tasks)
			tasks.add(task);

		return tasks;
	}

	/**
	 * Get a copy of the list containing all futures.
	 * <p>
	 * Must be called after {@link #call()}.
	 * 
	 * @return a list of {@link #Future}
	 */
	public synchronized List<Future<E>> getFutures()
	{
		expectStatus(STARTED);

		List<Future<E>> futures = new ArrayList<>();

		for(Future<E> future : this.futures)
			futures.add(future);

		return futures;
	}

	private void expectStatus(Status status)
	{
		switch (status)
		{
			case STARTED:
				if (executor == null || !isStarted())
					throw new IllegalStateException("The executor hasn't been started!");
				break;
			case IDLE:
				if (executor != null && isStarted())
					throw new IllegalStateException("The executor has been already started!");
				break;
		}
	}

	/**
	 * Check if this executor has been started.
	 * 
	 * @return true if {@link #call()} has been called.
	 */
	public synchronized boolean isStarted()
	{
		return getExecutorService().isShutdown();
	}

	/**
	 * Checks if all tasks has been executed.
	 * 
	 * @return true if all tasks has been executed.
	 */
	public synchronized boolean isTerminated()
	{
		return getExecutorService().isTerminated();
	}

	/**
	 * Checks if at least one task has been submitted.
	 * 
	 * @return true if at least one task has been submitted.
	 */
	public synchronized boolean hasElements()
	{
		return !tasks.isEmpty();
	}

	@Override
	public void updateProgress()
	{
		synchronized (futures)
		{
			if (futures.isEmpty())
			{
				setProgress(-1D);
				return;
			}

			double progressTotal = futures.size();
			double progress = 0.0D;

			for (Future<E> future : futures)
				progress += future.isDone() ? 1.0D : 0.0D;

			if (progressTotal <= 0.0D)
			{
				setProgress(-1D);
				return;
			}

			setProgress(progress / progressTotal);
		}
	}

	protected static enum Status
	{
		STARTED,
		IDLE;
	}
}
