package net.planetgeeks.mcstarter.app.install;

import lombok.Getter;
import net.planetgeeks.mcstarter.app.App;
import net.planetgeeks.mcstarter.util.http.HttpDownloader;
import net.planetgeeks.mcstarter.util.task.Task;
import net.planetgeeks.mcstarter.util.task.TaskExecutor;

public abstract class AppInstaller<T extends App<?>> extends TaskExecutor<Task<?>>
{
	@Getter
	private T application;
	
	@Getter
	private AppVerifier<T> verifier;
	
	@Getter
	private HttpDownloader downloader;
	
    public AppInstaller(T application)	
    {
    	submit(verifier = new AppVerifier<T>(application));
    	
    	submit(downloader = new HttpDownloader());
    }
}
