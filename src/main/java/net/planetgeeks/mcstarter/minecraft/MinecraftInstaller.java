package net.planetgeeks.mcstarter.minecraft;

import net.planetgeeks.mcstarter.app.install.Installer;

public class MinecraftInstaller extends Installer<Minecraft>
{
	public MinecraftInstaller(Minecraft application)
	{
		super(application);
	}

	@Override
	public void updateProgress()
	{
		
	}

	@Override
	protected void verify() throws Exception
	{
		//TODO : Minecraft files needs a premium account for the download.
	}
}
