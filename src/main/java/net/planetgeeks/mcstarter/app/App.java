package net.planetgeeks.mcstarter.app;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.planetgeeks.mcstarter.app.install.AppInstaller;
import net.planetgeeks.mcstarter.app.version.ReleaseVersion;
import net.planetgeeks.mcstarter.app.version.Version;
import net.planetgeeks.mcstarter.app.version.VersionsContainer;
import net.planetgeeks.mcstarter.util.task.Task;

/**
 * Represents an application that can be installed, updated and launched.
 * 
 * @author Flood2d
 */
public abstract class App<T extends VersionsContainer>
{
	@Getter @Setter
    private AppProfile profile;
	
	@Setter(AccessLevel.PROTECTED)
	private T versions;
	
	@Getter @Setter 
	private File applicationDir;

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
	public abstract AppInstaller<?> getInstaller();
	
	/**
	 * Get versions information online or from a cached container if this is available.
	 * 
	 * @return a <code>VersionsContainer</code> object containing versions information.
	 * @throws IOException
	 */
	public T getVersions() throws IOException
	{
		if (versions != null)
			return versions;

		return updateVersions();
	}
	
	/**
	 * Retrieve versions information online and update the cached container.
	 * 
	 * @return a new <code>VersionsContainer</code> object containing versions information.
	 * @throws IOException
	 */
	public abstract T updateVersions() throws IOException;
	
	public File getProfilesDir()
	{
		return getSubDir("profiles");
	}

	/**
	 * Get a sub directory of {@link #getApplicationDir()} with the given name.
	 * 
	 * @param name of the sub directory.
	 * @return a sub directory of {@link #getApplicationDir()}.
	 */
	protected File getSubDir(@NonNull String name)
	{
		return new File(getApplicationDir(), name);
	}
	
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
			
			checkStatus();
			
			//TODO : launch the application.
			
			return this;
		}
	}
}
