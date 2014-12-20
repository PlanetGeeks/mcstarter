package net.planetgeeks.mcstarter.minecraft;

import net.planetgeeks.mcstarter.app.install.Installer;

public class MinecraftInstaller extends Installer<Minecraft>
{
	public MinecraftInstaller(Minecraft application)
	{
		super(application);
		
		getDownloader().setAttemptLimit(2);
		getDownloader().setAttemptDelay(10 * 1000L);
	}

	@Override
	public void updateProgress()
	{
		
	}

	@Override
	protected void verify() throws Exception
	{
		//TODO : Minecraft files needs a premium account for the download.
		/**
		 * First check if there's a local version available for the version specified.
		 * Then control libraries by using 'version'.json.. Each library has a sha1 related file for checksum.
		 * Then control assets by using 'version'.json in assets index folder or legacy.json for previous versions.
		 * 
		 * If an online session has been provided, the installer is allowed to download the files.
		 * If there is on offline version, the installer will log all the missing files and will attempt a launch.
		 * 
		 * Attempt to launch minecraft if there were all strong dependency files. But notify the user.
		 */
	}
}
