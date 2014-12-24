package net.planetgeeks.mcstarter.minecraft;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.planetgeeks.mcstarter.app.App;
import net.planetgeeks.mcstarter.app.version.Version;
import net.planetgeeks.mcstarter.minecraft.session.OnlineSession;
import net.planetgeeks.mcstarter.minecraft.session.Session;

/**
 * Represents Minecraft Application.
 * 
 * @author Flood2d
 */
public class Minecraft extends App
{
	private static final String DOWNLOAD_URL = "http://s3.amazonaws.com/Minecraft.Download/";
	private static final String VERSION_URL = "versions/%0s/%0s.jar";
	private static final String VERSION_JSON_URL = "versions/%0s/%0s.json";
	private static final String VERSION_PATH = "%0s%1s%0s.jar";
	private static final String VERSION_JSON_PATH = "%0s%1s%0s.json";

	@Getter
	@Setter
	private Session session;
	@Getter
	@Setter
	private MinecraftProfile profile;
	@Getter
	private MinecraftInstaller installer = new MinecraftInstaller(this);
	private MinecraftVersions versions;

	@Override
	public MinecraftVersions getVersions() throws IOException
	{
		return versions != null ? versions : updateVersions();
	}

	@Override
	public MinecraftVersions updateVersions() throws IOException
	{
		return versions = MinecraftVersions.getUpdated();
	}

	/**
	 * @return Minecraft downloads base directory {@link #URL}.
	 */
	public static URL getDownloadURL() throws MalformedURLException
	{
		return new URL(DOWNLOAD_URL);
	}

	public static URL getVersionURL(@NonNull Version version) throws MalformedURLException
	{
		 return getFormattedURL(version, VERSION_URL, version.getId());
	}
	
	public static File getVersionPath(@NonNull Minecraft minecraft, @NonNull Version version)
	{
		return new File(minecraft.getVersionsDir(), String.format(VERSION_PATH, version.getId(), File.separator));
	}
	
	public static URL getVersionJsonURL(@NonNull Version version) throws MalformedURLException
	{
		return getFormattedURL(version, VERSION_JSON_URL, version.getId());
	}
	
	public static File getVersionJsonPath(@NonNull Minecraft minecraft, @NonNull Version version)
	{
		return new File(minecraft.getVersionsDir(), String.format(VERSION_JSON_PATH, version.getId(), File.separator));
	}
	
	private static URL getFormattedURL(@NonNull Version version, @NonNull String pattern, Object ... params) throws MalformedURLException
	{
		return new URL(getDownloadURL(), String.format(pattern, params));
	}

	public File getAssetsDir()
	{
		return getSubDir("assets");
	}

	public File getVersionsDir()
	{
		return getSubDir("versions");
	}

	public File getLibrariesDir()
	{
		return getSubDir("libraries");
	}
	
	public boolean isOnline()
	{
		if(getSession() == null)
			throw new IllegalStateException("Session is null!");
		
	    return getSession() instanceof OnlineSession;
	}
}
