package net.planetgeeks.mcstarter.minecraft;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import lombok.Getter;
import lombok.Setter;
import net.planetgeeks.mcstarter.app.App;
import net.planetgeeks.mcstarter.app.ReleaseVersion;
import net.planetgeeks.mcstarter.app.Version;
import net.planetgeeks.mcstarter.minecraft.session.Session;

/**
 * Represents Minecraft Application. 
 * 
 * @author Flood2d
 */
public class Minecraft extends App
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

	public MinecraftVersions getVersions() throws IOException
	{
		return MinecraftVersions.retrive();
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
}
