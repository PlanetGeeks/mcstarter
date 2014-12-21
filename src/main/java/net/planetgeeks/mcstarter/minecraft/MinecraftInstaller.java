package net.planetgeeks.mcstarter.minecraft;

import net.planetgeeks.mcstarter.app.install.AppInstaller;

public class MinecraftInstaller extends AppInstaller<Minecraft>
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
}
