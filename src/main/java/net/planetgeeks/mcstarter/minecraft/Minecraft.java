package net.planetgeeks.mcstarter.minecraft;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.planetgeeks.mcstarter.app.App;
import net.planetgeeks.mcstarter.app.version.Version;
import net.planetgeeks.mcstarter.minecraft.session.OnlineSession;
import net.planetgeeks.mcstarter.minecraft.session.Session;
import net.planetgeeks.mcstarter.util.CollectionsUtils;
import net.planetgeeks.mcstarter.util.Defaults;

/**
 * Represents Minecraft Application.
 * 
 * @author Flood2d
 */
public class Minecraft extends App
{
	@Getter
	@Setter
	private Session session;
	@Getter
	@Setter
	private MinecraftProfile profile;
	@Getter
	private MinecraftInstaller installer = new MinecraftInstaller(this);
    private List<Version> installedVersions;
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
	
	@Override
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

	@Override
	public void install() throws Exception
	{
		getInstaller().call();
	}

	@Override
	public void launch() throws Exception
	{
		install();
		
		//TODO LAUNCH
	}
}
