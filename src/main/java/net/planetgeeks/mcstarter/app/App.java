package net.planetgeeks.mcstarter.app;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.planetgeeks.mcstarter.app.install.AppInstaller;
import net.planetgeeks.mcstarter.app.version.ReleaseVersion;
import net.planetgeeks.mcstarter.app.version.Version;
import net.planetgeeks.mcstarter.app.version.VersionsContainer;
import net.planetgeeks.mcstarter.util.Platform;

public abstract class App
{
	@Setter
	private File applicationDir;

	@Getter
	@Setter
	private Logger logger = Logger.getLogger(getClass().getName());

	private Platform platform;

	/**
	 * @return the application installer task.
	 */
	protected abstract AppInstaller<?> getInstaller();

	/**
	 * Verify application files and retrieve them online if they are missing.
	 * 
	 * @throws Exception if an exception occurs installing the application.
	 */
	public abstract void install() throws Exception;

	/**
	 * Call the {@link #install()} method and then launch the application.
	 * 
	 * @throws Exception if an exception occurs launching the application.
	 */
	public abstract void launch() throws Exception;

	/**
	 * Get the cached application versions container.
	 * <p>
	 * If a cached container isn't set, this method will try to retrieve it
	 * online by calling {@link #updateVersions()}.
	 * 
	 * @return a {@link #VersionContainer}
	 * @throws IOException if an I/O Exception occurs retrieving the container
	 *             online.
	 */
	public abstract VersionsContainer getVersions() throws IOException;

	/**
	 * Update cached application versions container.
	 * <p>
	 * This method will try to retrieve the container online.
	 * 
	 * @return a {@link #VersionContainer}.
	 * @throws IOException if an I/O Exception occurs retrieving the container
	 *             online.
	 */
	public abstract VersionsContainer updateVersions() throws IOException;

	/**
	 * Get a list of {@link #Version} installed into
	 * {@link #getApplicationDir()}.
	 * 
	 * @return a list of {@link #Version}.
	 * @throws Exception if an exception occurs during search process.
	 */
	public abstract List<Version> getInstalledVersions() throws Exception;

	/**
	 * Get the application profile.
	 * <p>
	 * An application profile is necessary to run an application.
	 * 
	 * @return the application profile.
	 */
	public abstract <T extends AppProfile> T getProfile();

	/**
	 * Retrieve application latest version, snapshot or release.
	 * 
	 * @return the {@link #Version} retrieved.
	 * @throws IOException if an I/O Exception occurs retrieving the version.
	 */
	public Version getLatestVersion() throws IOException
	{
		return getVersions().getLatestSnapshot();
	}

	/**
	 * Retrieve application latest stable version.
	 * 
	 * @return the {@link #ReleaseVersion} retrieved.
	 * @throws IOException if an I/O Exception occurs retrieving the version.
	 */
	public ReleaseVersion getLatestRelease() throws IOException
	{
		return getVersions().getLatestRelease();
	}

	/**
	 * Retrieve application version from an online container.
	 * <p>
	 * A {@link #NullPointerException} will be thrown if the container doesn't
	 * contain a version with the given id.
	 * 
	 * @param versionId - the versionId.
	 * @return the Version retrieved.
	 * @throws IOException if an I/O Exception occurs retrieving the version.
	 */
	public Version getVersion(@NonNull String versionId) throws IOException
	{
		return getVersions().getVersion(versionId);
	}

	/**
	 * Get application version set into the current {@link #AppProfile}.
	 * 
	 * @return the selected version.
	 */
	public Version getSelectedVersion()
	{
		if (getProfile() == null)
			throw new IllegalStateException("No application profile set!");

		return getProfile().getVersion();
	}

	/**
	 * @return {@link #AppProfile}s sub directory of
	 *         {@link #getApplicationDir()}.
	 */
	public File getProfilesDir()
	{
		return getSubDir("profiles");
	}

	/**
	 * Get a sub directory of {@link #getApplicationDir()}.
	 * 
	 * @param name - sub directory name.
	 * @return sub directory of {@link #getApplicationDir()}.
	 */
	protected File getSubDir(@NonNull String name)
	{
		return new File(getApplicationDir(), name);
	}

	/**
	 * @return the application directory.
	 */
	public File getApplicationDir()
	{
		if (applicationDir == null)
			throw new IllegalStateException("No directory set for this application");

		return applicationDir;
	}

	/**
	 * @return the platform managing this application.
	 */
	public Platform getPlatform()
	{
		return platform == null ? (platform = Platform.getPlatform()) : platform;
	}
}
