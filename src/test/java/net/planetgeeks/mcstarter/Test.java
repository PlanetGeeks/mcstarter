package net.planetgeeks.mcstarter;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import net.planetgeeks.mcstarter.minecraft.Minecraft;
import net.planetgeeks.mcstarter.minecraft.Minecraft.MinecraftVersions;

public abstract class Test
{
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException
	{
		Minecraft minecraft = new Minecraft();
		
        MinecraftVersions versions = minecraft.getVersions();
        
		System.out.println(versions.getVersion("1.8.1").getTime());
		/**
		 * HttpDownloader downloader = new HttpDownloader();
		 * 
		 * File dir = new File("test");
		 * 
		 * downloader.submit(new URL(
		 * "https://dl.dropboxusercontent.com/u/88221856/EpicRealm/game/bin/minecraft.jar"
		 * ), new File(dir, "minecraft.jar")); downloader.submit(new URL(
		 * "https://dl.dropboxusercontent.com/u/88221856/EpicRealm/game/bin/lwjgl.jar"
		 * ), new File(dir, "lwjgl.jar")); downloader.submit(new URL(
		 * "https://dl.dropboxusercontent.com/u/88221856/EpicRealm/game/bin/lwjgl_util.jar"
		 * ), new File(dir, "lwjgl_util.jar")); downloader.submit(new URL(
		 * "https://dl.dropboxusercontent.com/u/88221856/EpicRealm/game/bin/lwjgl_util.jar"
		 * ), new File(dir, "lwjgl_jinput.jar"));
		 * downloader.setExecutorService(Executors.newFixedThreadPool(5));
		 * 
		 * downloader.call();
		 * 
		 * List<HttpFile> actives = null; List<HttpFile> terminated = null;
		 * List<HttpFile> tasks = null;
		 * 
		 * while(!downloader.isTerminated()) { actives =
		 * downloader.getRunningTasks(); terminated =
		 * downloader.getTerminatedTasks(); tasks = downloader.getTasks();
		 * 
		 * System.out.println(String.format(
		 * "Actives : %d , Terminated : %d, Total : %d", actives.size(),
		 * terminated.size(), tasks.size()));
		 * 
		 * Thread.sleep(1000L); }
		 **/
		/**
		 * Minecraft minecraft = new Minecraft();
		 * 
		 * MinecraftVersions versions = minecraft.getVersions();
		 * 
		 * minecraft.setVersion(versions.getLatestRelease());
		 * 
		 * M
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
