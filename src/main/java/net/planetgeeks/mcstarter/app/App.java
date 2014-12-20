package net.planetgeeks.mcstarter.app;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.Getter;
import lombok.Setter;
import net.planetgeeks.mcstarter.app.install.Installer;
import net.planetgeeks.mcstarter.util.task.Task;

/**
 * Represents an application that can be installed, updated and launched.
 * 
 * @author Flood2d
 */
public abstract class App
{
	@Getter @Setter
    private Version version;

	/**
	 * Get the latest application release, including snapshot releases.
	 * 
	 * @return the latest application {@link #Version} available.
	 * 
	 * @throws IOException if an exeception occurs retriving the version.
	 */
	public abstract Version getLatestVersion() throws IOException;
	
	/**
	 * Get the latest application stable release.
	 * 
	 * @return the latest application stable {@link #ReleaseVersion} available.
	 * 
	 * @throws IOException if an exeception occurs retriving the version.
	 */
	public abstract ReleaseVersion getLatestRelease() throws IOException;
	
	/**
	 * Get the application installer.
	 * 
	 * @return the application {@link #Installer}.
	 */
	public abstract Installer<?> getInstaller();
	
	/**
	 * Validate application files by using its {@link #Installer} and then launch the application on a separated Thread.
	 * 
	 * @return a {@link #Future} object which gives information on the task.
	 * @throws Exception if an exception occurs launching the application.
	 * @throws InterruptedException if the launch task has been interrupted.
	 */
	public Future<LaunchTask> launch() throws InterruptedException, Exception
	{
		ExecutorService executor = Executors.newSingleThreadExecutor();
		
		Future<LaunchTask> task = executor.submit(new LaunchTask());
		
		executor.shutdown();
		
		return task;
	}
	
	public class LaunchTask extends Task<LaunchTask>
	{
		@Override
		public LaunchTask call() throws Exception, InterruptedException
		{
			getInstaller().call();
			
			checkInterrupt();
			
			//TODO : launch the application.
			
			return this;
		}
	}
}
