package net.planetgeeks.mcstarter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import net.planetgeeks.mcstarter.util.http.HttpDownloader;
import net.planetgeeks.mcstarter.util.http.HttpFile;

public abstract class Test
{
	private static String name = "net.java.jinput:jinput-platform:2.0.5";

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException
	{
        HttpDownloader downloader = new HttpDownloader();
        
        File dir = new File("test");
        
        downloader.submit(new URL("https://dl.dropboxusercontent.com/u/88221856/EpicRealm/game/bin/minecraft.jar"), new File(dir, "minecraft.jar"));
        downloader.submit(new URL("https://dl.dropboxusercontent.com/u/88221856/EpicRealm/game/bin/lwjgl.jar"), new File(dir, "lwjgl.jar"));
        downloader.submit(new URL("https://dl.dropboxusercontent.com/u/88221856/EpicRealm/game/bin/lwjgl_util.jar"), new File(dir, "lwjgl_util.jar"));
        downloader.submit(new URL("https://dl.dropboxusercontent.com/u/88221856/EpicRealm/game/bin/lwjgl_util.jar"), new File(dir, "lwjgl_jinput.jar"));
        
        downloader.call();
        
        HttpFile file = downloader.getTasks().get(0);
        
        int z = 0;
        
        while(!downloader.isTerminated())
        {
        	if(z == 4)
        		downloader.pause();
        	
        	if(z == 8)
        		downloader.resume();
        	
        	Thread.sleep(1000);
        	
        	downloader.updateProgress();
        	       
        	System.out.println(String.format("count = %d Progress Total %s%% Progress first %s%%", z, downloader.getProgress(100), file.getProgress(100)));
        
        	z++;
        }
        
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
