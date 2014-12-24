package net.planetgeeks.mcstarter.app.install;

import lombok.Getter;
import net.planetgeeks.mcstarter.app.App;
import net.planetgeeks.mcstarter.util.http.HttpDownloader;
import net.planetgeeks.mcstarter.util.task.Task;
import net.planetgeeks.mcstarter.util.task.TaskExecutor;

public abstract class AppInstaller<T extends App> extends TaskExecutor<Task<?>>
{
	@Getter
	private T app;
	
	@Getter
	private HttpDownloader downloader;
	
    public AppInstaller(T application)	
    {
    	submit(getVerifier());
    	
    	submit(downloader = new HttpDownloader());
    	
    	downloader.setAwaitTermination(true);
    }
    
    public abstract AppVerifier<T> getVerifier();
}
