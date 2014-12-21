package net.planetgeeks.mcstarter.minecraft;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import lombok.Getter;
import lombok.Setter;
import net.planetgeeks.mcstarter.app.App;
import net.planetgeeks.mcstarter.app.version.ReleaseVersion;
import net.planetgeeks.mcstarter.app.version.Version;
import net.planetgeeks.mcstarter.app.version.VersionsContainer;
import net.planetgeeks.mcstarter.minecraft.Minecraft.MinecraftVersions;
import net.planetgeeks.mcstarter.minecraft.session.Session;

/**
 * Represents Minecraft Application. 
 * 
 * @author Flood2d
 */
public class Minecraft extends App<MinecraftVersions>
{
	private static final String DOWNLOAD_URL = "http://s3.amazonaws.com/Minecraft.Download/";
	
	@Getter
	private MinecraftInstaller installer = new MinecraftInstaller(this);
	
	@Getter @Setter
	private Session session;
	
	@Override
	public Version getLatestVersion() throws IOException
	{
		return getVersions().getLatestSnapshot();
	}

	@Override
	public ReleaseVersion getLatestRelease() throws IOException
	{
		return getVersions().getLatestRelease();
	}

	/**
	 * Get Minecraft downloads base dir url.
	 * 
	 * @return Minecraft downloads base dir {@link #URL}.
	 */
	public static URL getDownloadUrl() throws MalformedURLException
	{
		return new URL(DOWNLOAD_URL);
	}

	@Override
	public synchronized MinecraftVersions updateVersions() throws IOException
	{
		setVersions(VersionsContainer.getUpdated(MinecraftVersions.class, new URL(getDownloadUrl(), MinecraftVersions.VERSIONS_URL)));
		
		return getVersions();
	}
	
	/**
	 * Contains the list of all minecraft versions that can be installed.
	 * 
	 * @author Flood2d
	 */
	public static class MinecraftVersions extends VersionsContainer
	{
		private static final String VERSIONS_URL = "versions/versions.json";
	}
}
