package net.planetgeeks.mcstarter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import net.planetgeeks.mcstarter.util.http.HttpDownloader;
import net.planetgeeks.mcstarter.util.http.HttpFile;

public abstract class Test
{
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException
	{
		File dir = new File("temp");
		URL dir_url = new URL("https://dl.dropboxusercontent.com/u/88221856/EpicRealm/game/bin/");
		
		HttpFile.download(new URL(dir_url, "jinput.jar"), new File("jinput.jar"), true, 3, 3000L).get();
		
		final HttpDownloader downloader = new HttpDownloader();
	    
	    downloader.add(new URL(dir_url, "jinput.jar"), new File(dir, "jinput.jar"));
	    downloader.add(new URL(dir_url, "lwjgl.jar"), new File(dir, "lwjgl.jar"));
	    downloader.add(new URL(dir_url, "lwjgl_util.jar"), new File(dir, "lwjgl_util.jar"));
	    downloader.add(new URL(dir_url, "minecraft.jar"), new File(dir, "minecraft.jar"));
	    
	    new Thread()
	    {
	    	public void run()
	    	{
	    		try
				{
					downloader.call();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				catch (ExecutionException e)
				{
					e.printStackTrace();
				}
	    	}
	    }.start();
	    
		
        new Thread()
        {
        	public void run()
        	{
        		do
        		{
        			downloader.updateProgress();
        			
        			try
    				{
    					Thread.sleep(1000L);
    				}
    				catch (InterruptedException e)
    				{
    					e.printStackTrace();
    				}
            		
            		System.out.println("STATUS : " + downloader.getProgress());
        		}
        		while(!downloader.isTerminated());
        		
        		System.out.println("STATUS : " + downloader.getProgress());
        	}
        }.start();
        
	}
	
	public abstract void test();
}
