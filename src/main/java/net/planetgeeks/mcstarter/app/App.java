 package net.planetgeeks.mcstarter.app;

import java.io.File;
import java.io.IOException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.planetgeeks.mcstarter.app.install.AppInstaller;
import net.planetgeeks.mcstarter.app.version.ReleaseVersion;
import net.planetgeeks.mcstarter.app.version.Version;
import net.planetgeeks.mcstarter.app.version.VersionsContainer;

/**
 * Represents an application that can be installed, updated and launched.
 * 
 * @author Flood2d
 */
public abstract class App<Z extends App<Z>>
{
	@Getter @Setter
    private AppProfile<Z> profile;
	
	@Setter(AccessLevel.PROTECTED)
	private VersionsContainer versions;
	
	@Getter @Setter 
	private File applicationDir;
	
	/**
	 * Get the latest application release, including snapshot releases.
	 * 
	 * @return the latest application {@link #Version} available.
	 * 
	 * @throws IOException if an exeception occurs retriving the version.
	 */
	public Version getLatestVersion() throws IOException
	{
		return getVersions().getLatestSnapshot();
	}
	
	/**
	 * Get the latest application stable release.
	 * 
	 * @return the latest application stable {@link #ReleaseVersion} available.
	 * 
	 * @throws IOException if an exeception occurs retriving the version.
	 */
	public ReleaseVersion getLatestRelease() throws IOException
	{
		return getVersions().getLatestRelease();
	}
	
	public Version getSelectedVersion() throws IOException
	{
		if(profile == null)
			throw new IllegalStateException("No application profile set!");
		
		return getVersions().getVersion(profile.getVersionId()); //TODO : profile.getVersion(this);
	}
	
	public Version getVersion(@NonNull String versionId) throws IOException
	{
		return getVersions().getVersion(versionId);
	}
	
	/**
	 * Get the application installer.
	 * 
	 * @return the application {@link #Installer}.
	 */
	public abstract AppInstaller<Z> getInstaller();
	
	/**
	 * Get versions information online or from a cached container if this is available.
	 * 
	 * @return a <code>VersionsContainer</code> object containing versions information.
	 * @throws IOException
	 */
	public VersionsContainer getVersions() throws IOException
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
	public abstract VersionsContainer updateVersions() throws IOException;
	
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
}
