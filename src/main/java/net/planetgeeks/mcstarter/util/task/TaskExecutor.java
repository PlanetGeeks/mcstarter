package net.planetgeeks.mcstarter.util.task;

import static net.planetgeeks.mcstarter.util.task.TaskExecutor.Status.IDLE;
import static net.planetgeeks.mcstarter.util.task.TaskExecutor.Status.STARTED;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.Getter;
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
 */
public class TaskExecutor<T extends Task<?>> extends ProgressTask<List<Future<?>>>
{
	private ExecutorService executor;
	private List<T> tasks = new ArrayList<>();
	private List<T> actives = new ArrayList<>();
	private List<T> terminated = new ArrayList<>();
	private List<Future<?>> futures = new ArrayList<>();

	/**
	 * Set the {@link #ExecutorService} that will be used by this
	 * {@link #RawTaskExecutor}.
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
	 * Get the {@link #ExecutorService} used by this {@link #RawTaskExecutor}.
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
	public synchronized List<Future<?>> call()
	{
		expectStatus(IDLE);

		ExecutorService executor = getExecutorService();

		synchronized (futures)
		{
			for (T task : tasks)
				futures.add(executor.submit(new Executable<T>(task, this)));
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

		for (T task : this.tasks)
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
	public synchronized List<Future<?>> getFutures()
	{
		expectStatus(STARTED);

		List<Future<?>> futures = new ArrayList<>();

		for (Future<?> future : this.futures)
			futures.add(future);

		return futures;
	}

	/**
	 * Get a copy of the list containing all running tasks.
	 * <p>
	 * Must be called after {@link #call()}.
	 * 
	 * @return a list of {@link #Task}
	 */
	public synchronized List<T> getRunningTasks()
	{
		expectStatus(STARTED);

		List<T> actives = new ArrayList<>();

		synchronized (this.actives)
		{
			for (T active : this.actives)
				actives.add(active);
		}

		return actives;
	}

	/**
	 * Get a copy of the list containing all terminated tasks.
	 * <p>
	 * Must be called after {@link #call()}.
	 * 
	 * @return a list of {@link #Task}
	 */
	public synchronized List<T> getTerminatedTasks()
	{
		expectStatus(STARTED);

		List<T> terminated = new ArrayList<>();

		synchronized (this.terminated)
		{
			for (T task : this.terminated)
				terminated.add(task);
		}

		return terminated;
	}

	/**
	 * Add an <code>HttpFile</code> task to the terminated list.
	 * 
	 * @param task - {@link #HttpFile} to add.
	 */
	private void addTerminated(@NonNull T task)
	{
		synchronized (this.terminated)
		{
			if (!this.terminated.contains(task))
				this.terminated.add(task);

			removeActive(task);
		}
	}

	/**
	 * Add a task to the active list.
	 * 
	 * @param task to add.
	 */
	private void addActive(@NonNull T task)
	{
		synchronized (this.actives)
		{
			if (!this.actives.contains(task))
				this.actives.add(task);
		}
	}

	/**
	 * Remove a task from the active list.
	 * 
	 * @param task to remove.
	 */
	private void removeActive(@NonNull T task)
	{
		synchronized (this.actives)
		{
			if (this.actives.contains(task))
				this.actives.remove(task);
		}
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

			for (Future<?> future : futures)
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

	/**
	 * Used by the executor to manage active and terminated tasks.
	 * 
	 * @author Flood2d
	 */
	private static class Executable<T extends Task<?>> implements Callable<Object>
	{
		@Getter
		private T task;
		@Getter
		private TaskExecutor<T> executor;

		public Executable(@NonNull T task, @NonNull TaskExecutor<T> executor)
		{
			this.task = task;
			this.executor = executor;
		}

		@Override
		public Object call() throws Exception
		{
			executor.addActive(task);

			try
			{
				return task.call();
			}
			finally
			{
				executor.addTerminated(task);
			}
		}
	}
}