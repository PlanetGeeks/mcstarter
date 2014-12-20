package net.planetgeeks.mcstarter.util.http;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import lombok.NonNull;
import net.planetgeeks.mcstarter.util.task.RecoverableProgressTask;

/**
 * Manages several download requests and runs them concurrently. The whole task
 * can be monitored.
 * 
 * A default <code>ExecutorService</code> will be created if you don't set a
 * custom one.
 * 
 * @author Flood2d
 */
public class HttpDownloader extends RecoverableProgressTask<List<Future<HttpFile>>>
{
	private ExecutorService executor;

	private Set<HttpFile> downloads = new LinkedHashSet<>();
	private Set<HttpFile> terminated = new HashSet<>();
	
	/**
	 * Add a {@link #DownloadableFile} to the download list.
	 * 
	 * This method must be called during idle times. If this object is currently
	 * downloading, the adding Thread will wait the end of the task.
	 * 
	 * @param file downloadable to add.
	 */
	protected synchronized void add(@NonNull HttpFile file)
	{
		synchronized (this.downloads)
		{
			downloads.add(file);
		}
	}

	/**
	 * Add a file to the download list.
	 * 
	 * This method must be called during idle times. If this object is currently
	 * downloading, the adder Thread will wait the end of the task.
	 * 
	 * @param url - the remote location of the file.
	 * @param outFile - the destination file.
	 */
	public synchronized void add(@NonNull URL url, @NonNull File outFile)
	{
		add(new HttpFile(url, outFile, this));
	}

	/**
	 * Set the executor of the individual download tasks.
	 *
	 * This method must be called during idle times. If this object is currently
	 * downloading, the setter Thread will wait the end of the task.
	 * 
	 * @param executor - The ExecutorService
	 */
	public synchronized void setExecutorService(@NonNull ExecutorService executor)
	{
		this.executor = executor;
	}

	/**
	 * Get the executor of the individual download tasks.
	 * 
	 * If there isn't an executor set, a default one will be set and returned.
	 * 
	 * @return the task {@link #ExecutorService}.
	 */
	public ExecutorService getExecutorService()
	{
		return executor != null ? executor : (executor = Executors.newFixedThreadPool(5));
	}

	@Override
	public synchronized List<Future<HttpFile>> call() throws InterruptedException, ExecutionException
	{
		ExecutorService executor = getExecutorService();

		try
		{
			List<Future<HttpFile>> futures = new ArrayList<>();

			synchronized (downloads)
			{
				Iterator<HttpFile> it = downloads.iterator();
				
				while (it.hasNext())
				{
					HttpFile next = it.next();
					
					next.setRecoverable(isRecoverable());
					next.setAttemptLimit(getAttemptLimit());
					next.setAttemptDelay(getAttemptDelay());
					
					futures.add(executor.submit(next));
				}
			}

			executor.shutdown();

			while (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS))
				;

			for (Future<HttpFile> future : futures)
				future.get();

			return futures;
		}
		finally
		{
			executor.shutdownNow();
		}
	}

	/**
	 * Add an <code>HttpFile</code> task to the terminated list.
	 * 
	 * @param task - {@link #HttpFile} to add.
	 */
	protected void addTerminated(@NonNull HttpFile task)
	{
		synchronized (this.terminated)
		{
			this.terminated.add(task);
		}
	}
	
	/**
	 * 
	 * @return true if all requests have been processed, with or without errors.
	 */
	public boolean isTerminated()
	{
		return this.terminated.size() >= this.downloads.size();
	}
	
	/**
	 * Check if there is at least one file to download.
	 * 
	 * @return true if there is at least one file to download.
	 */
	public boolean hasElements()
	{
		return !downloads.isEmpty();
	}
	
	@Override
	public void updateProgress()
	{
		synchronized (this.downloads)
		{
			if(isTerminated())
			{
				setProgress(1.0D);
				return;
			}
			
			double progressTotal = 0.0D;
			double progress = 0.0D;

			for (HttpFile file : this.downloads)
			{
				int length = file.getLength();

				if(length == -1)
				{
					progressTotal = length;
					break;
				}
				
				progressTotal += Double.valueOf(length);
				
				progress += length * file.getProgress();
			}

			if (progressTotal == 0.0D || progressTotal == -1)
			{
				setProgress(-1);
				return;
			}

			setProgress(progress / progressTotal);
		}
	}
}
