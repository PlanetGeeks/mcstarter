package net.planetgeeks.mcstarter.task;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import lombok.NonNull;
import net.planetgeeks.mcstarter.util.CollectionsUtils;
import net.planetgeeks.mcstarter.util.ProgressView;

public class TaskExecutor<N extends Task<N>> extends ProgressView implements Task<List<Future<N>>>
{
	private Set<N> tasks = new LinkedHashSet<>();
	private List<Future<N>> futures;
	private ExecutorService executor;

	public synchronized void submit(N task)
	{
		synchronized (tasks)
		{
			tasks.add(task);
		}
	}

	public synchronized void setExecutor(@NonNull ExecutorService executor)
	{
		this.executor = executor;
	}

	public ExecutorService getExecutor()
	{
		return executor == null ? (executor = Executors.newSingleThreadExecutor()) : executor;
	}

	public Set<N> getTasks()
	{
		return CollectionsUtils.cloneSet(tasks);
	}

	public List<Future<N>> getFutures()
	{
		if (futures == null)
			throw new IllegalStateException("Executor not started yet!");

		return CollectionsUtils.cloneList(futures);
	}

	@Override
	public synchronized List<Future<N>> call() throws InterruptedException, ExecutionException
	{
		ExecutorService executor = getExecutor();

		futures = new ArrayList<>();
		
		synchronized (futures)
		{
			for (N task : tasks)
				futures.add(executor.submit(task));
		}

		executor.shutdown();

		try
		{
			while (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS))
				;
		}
		catch (InterruptedException e)
		{
			executor.shutdownNow();

			throw e;
		}

		synchronized (futures)
		{
			for (Future<N> future : futures)
				future.get();
		}

		return getFutures();
	}

	@Override
	public void checkStatus() throws InterruptedException
	{
		if (Thread.interrupted())
			throw new InterruptedException("Task executor interrupted");
	}
}
