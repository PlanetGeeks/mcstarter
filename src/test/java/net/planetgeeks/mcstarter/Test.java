package net.planetgeeks.mcstarter;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public abstract class Test
{
	private static String name = "net.java.jinput:jinput-platform:2.0.5";

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException
	{

		/**
		 * Minecraft minecraft = new Minecraft();
		 * 
		 * MinecraftVersions versions = minecraft.getVersions();
		 * 
		 * minecraft.setVersion(versions.getLatestRelease());
		 * 
		 * minecraft.setSession(new OfflineSession("Flood2d"));
		 * 
		 * //TODO : set
		 * 
		 * System.out.println(minecraft.getVersion().getId());
		 **/
	}

	public abstract void test();
}
