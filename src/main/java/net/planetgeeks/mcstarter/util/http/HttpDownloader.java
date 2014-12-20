package net.planetgeeks.mcstarter.util.http;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import lombok.NonNull;
import net.planetgeeks.mcstarter.util.task.recover.RecoverableTaskExecutor;

/**
 * Manages several download requests and runs them concurrently. The whole task
 * can be monitored.
 * 
 * @author Flood2d
 */
public class HttpDownloader extends RecoverableTaskExecutor<HttpFile, HttpFile>
{
	private Set<HttpFile> terminated = new HashSet<>();

	/**
	 * Add a file to the download list.
	 * 
	 * This method must be called during idle times. If this object is currently
	 * downloading, the adder Thread will wait the end of the task.
	 * 
	 * @param url - the remote location of the file.
	 * @param outFile - the destination file.
	 */
	public synchronized void submit(@NonNull URL url, @NonNull File outFile)
	{
		submit(new HttpFile(url, outFile, this));
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
}
