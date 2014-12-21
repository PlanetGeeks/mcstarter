package net.planetgeeks.mcstarter;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import net.planetgeeks.mcstarter.util.http.HttpDownloader;
import net.planetgeeks.mcstarter.util.http.HttpFile;

public class TestDownload extends Test
{

	@Override
	public void test() throws Exception
	{
		HttpDownloader downloader = new HttpDownloader();

		File dir = new File("test");

		downloader.submit(new URL("https://dl.dropboxusercontent.com/u/88221856/EpicRealm/game/bin/minecraft.jar"), new File(dir, "minecraft.jar"));
		downloader.submit(new URL("https://dl.dropboxusercontent.com/u/88221856/EpicRealm/game/bin/lwjgl.jar"), new File(dir, "lwjgl.jar"));
		downloader.submit(new URL("https://dl.dropboxusercontent.com/u/88221856/EpicRealm/game/bin/lwjgl_util.jar"), new File(dir, "lwjgl_util.jar"));
		downloader.submit(new URL("https://dl.dropboxusercontent.com/u/88221856/EpicRealm/game/bin/lwjgl_util.jar"), new File(dir, "lwjgl_jinput.jar"));
		downloader.setExecutorService(Executors.newFixedThreadPool(5));

		downloader.call();

		List<HttpFile> actives = null;
		List<HttpFile> terminated = null;
		List<HttpFile> tasks = null;

		int z = 0;
		while (!downloader.isTerminated())
		{
			if(z == 10)
				downloader.interrupt();
			
			
			
			actives = downloader.getRunningTasks();
			terminated = downloader.getTerminatedTasks();
			tasks = downloader.getTasks();

			System.out.println(String.format("Actives : %d , Terminated : %d, Total : %d", actives.size(), terminated.size(), tasks.size()));

			Thread.sleep(1000L);
			
			
			
			z++;
		}
		
		try
		{
			downloader.awaitTermination();
			List<ExecutionException> exceptions = downloader.checkTasks();
			
			System.out.println("EXECUTION EXCEPTIONS : " + exceptions.size());
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
	}
    
}
