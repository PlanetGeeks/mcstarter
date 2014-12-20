package net.planetgeeks.mcstarter.app.install;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import lombok.Getter;
import lombok.extern.java.Log;
import net.planetgeeks.mcstarter.app.App;
import net.planetgeeks.mcstarter.util.http.HttpDownloader;
import net.planetgeeks.mcstarter.util.task.ProgressTask;

@Log
public abstract class Installer<T extends App> extends ProgressTask<Installer<T>>
{
	@Getter
	private HttpDownloader downloader = new HttpDownloader();

	@Getter
	private T application;
	
	public Installer(T application)
	{
		this.application = application;
	}
	
	@Override
	public Installer<T> call() throws Exception, InterruptedException
	{
		verify();

		checkInterrupt();

		if (getDownloader().hasElements())
			install();

		setProgress(1.0D);

		return this;
	}

	/**
	 * Check whether the application can be started and if it is updated,
	 * otherwise prepare the necessary for the installation.
	 * <p>
	 * All the files to  download or update must be inserted into the <code>Installer</code>'s {@link #HttpDownloader}. You can get it by using {@link #getDownloader()}.
	 * <p>
	 * Offline checks must be inserted here.
	 */
	protected abstract void verify() throws Exception;

	/**
	 * Download all required files. 
	 * 
	 * @throws IOException if an I/O exception occurs executing the task.
	 * @throws ExecutionException if an exception occurs during the download.
	 * @throws InterruptedException if the task has been interrupted.
	 */
	protected void install() throws IOException, InterruptedException, ExecutionException
	{
		try
		{
			downloader.call();
		}
		catch(ExecutionException e)
		{
			log.severe("Unable to download all files! The application hasn't been installed correctly.");
			
			throw e;
		} 
	}
}
