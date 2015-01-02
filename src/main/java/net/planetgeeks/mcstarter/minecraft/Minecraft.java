package net.planetgeeks.mcstarter.minecraft;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.planetgeeks.mcstarter.minecraft.session.OnlineSession;
import net.planetgeeks.mcstarter.minecraft.session.Session;
import net.planetgeeks.mcstarter.minecraft.version.ReleaseVersion;
import net.planetgeeks.mcstarter.minecraft.version.Version;
import net.planetgeeks.mcstarter.util.CollectionsUtils;
import net.planetgeeks.mcstarter.util.Defaults;
import net.planetgeeks.mcstarter.util.Platform;

/**
 * Represents Minecraft Application.
 * 
 * @author Flood2d
 */
public class Minecraft
{
	@Getter
	@Setter
	private Logger logger = Logger.getLogger(getClass().getName());
	@Getter
	@Setter
	private Session session;
	@Setter
	private MinecraftProfile profile;
	@Getter
	private MinecraftInstaller installer = new MinecraftInstaller(this);
	@Setter
	private File applicationDir;
    private List<Version> installedVersions;
	private MinecraftVersions versions;
	private Platform platform;

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
	public MinecraftVersions getVersions() throws IOException
	{
		return versions != null ? versions : updateVersions();
	}

	/**
	 * Update cached application versions container.
	 * <p>
	 * This method will try to retrieve the container online.
	 * 
	 * @return a {@link #VersionContainer}.
	 * @throws IOException if an I/O Exception occurs retrieving the container
	 *             online.
	 */
	public MinecraftVersions updateVersions() throws IOException
	{
		return versions = MinecraftVersions.getUpdated();
	}
	
	/**
	 * Get a list of {@link #Version} installed into
	 * {@link #getApplicationDir()}.
	 * 
	 * @return a list of {@link #Version}.
	 * @throws Exception if an exception occurs during search process.
	 */
	public List<Version> getInstalledVersions() throws InterruptedException
	{
		if(installedVersions == null)
			 installedVersions = new MinecraftVersionFinder(this).call();
			
		return CollectionsUtils.cloneList(installedVersions);
	}

	/**
	 * @return Minecraft downloads base directory {@link #URL}.
	 */
	public static URL getDownloadURL() throws MalformedURLException
	{
		return new URL(Defaults.getString("minecraft.baseurl.downloads"));
	}

	/**
	 * @return minecraft assets directory.
	 */
	public File getAssetsDir()
	{
		return getSubDir("assets");
	}

	/**
	 * @return minecraft versions directory.
	 */
	public File getVersionsDir()
	{
		return getSubDir("versions");
	}

	/**
	 * @return minecraft libraries directory.
	 */
	public File getLibrariesDir()
	{
		return getSubDir("libraries");
	}

	/**
	 * @return true if a valid {@link #OnlineSession} is set.
	 */
	public boolean isOnline()
	{
		if (getSession() == null)
			throw new IllegalStateException("Session is null!");

		return getSession() instanceof OnlineSession && getSession().isValid();
	}

	/**
	 * Verify application files and retrieve them online if they are missing.
	 * 
	 * @throws Exception if an exception occurs installing the application.
	 */
	public void install() throws Exception
	{
		getInstaller().call();
	}

	/**
	 * Call the {@link #install()} method and then launch the application.
	 * 
	 * @throws Exception if an exception occurs launching the application.
	 */
	public void launch() throws Exception
	{
		install();
		
		//TODO LAUNCH
	}
	
	/**
	 * Get the application profile.
	 * <p>
	 * An application profile is necessary to run an application.
	 * 
	 * @return the application profile.
	 */
	public MinecraftProfile getProfile()
	{
		return profile;
	}
	
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
