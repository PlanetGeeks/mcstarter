package net.planetgeeks.mcstarter.minecraft;

import net.planetgeeks.mcstarter.app.install.AppInstaller;

public class MinecraftInstaller extends AppInstaller<Minecraft>
{
	private MinecraftVerifier verifier;
	
	public MinecraftInstaller(Minecraft app)
	{
		super(app);
		
		getDownloader().setAttemptLimit(2);
		getDownloader().setAttemptDelay(10 * 1000L);
	}

	@Override
	public void updateProgress()
	{
		
	}

	@Override
	public MinecraftVerifier getVerifier()
	{
		return verifier != null ? verifier : new MinecraftVerifier(getApp());
	}

	public static class OnlineRequiredException extends Exception
	{
		private static final long serialVersionUID = 1L;
		
		public OnlineRequiredException(String message)
		{
			super(message);
		}
	}
}

