package net.planetgeeks.mcstarter.util.http;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.NonNull;
import net.planetgeeks.mcstarter.util.DownloadableFile;
import net.planetgeeks.mcstarter.util.task.ProgressTask;

/**
 * Manages several download requests and runs them concurrently. The whole task can be monitored.
 * 
 * A default <code>ExecutorService</code> will be created if you don't set a custom one.
 * 
 * @author Flood2d
 */
public class HttpDownloader extends ProgressTask<Boolean>
{
	private ExecutorService executor;
	
	private List<DownloadableFile> downloads = new ArrayList<>();
	
	/**
	 * Add a {@link #DownloadableFile} to the download list.
	 * 
	 * This method must be called during idle times. If this object is currently downloading, 
	 * the adding Thread will wait the end of the task.
	 * 
	 * @param file downloadable to add.
	 */
	public synchronized void add(@NonNull DownloadableFile file)
	{
		downloads.add(file);
	}
	
	/**
	 * Add a file to the download list.
	 * 
	 * This method must be called during idle times. If this object is currently downloading, 
	 * the adder Thread will wait the end of the task.
	 * 
	 * @param url - the remote location of the file.
	 * @param outFile - the destination file.
	 */
	public synchronized void add(@NonNull URL url, @NonNull File outFile)
	{
		add(new DownloadableFile(url, outFile));
	}
	
	/**
	 * Set the executor of the individual download tasks.
	 *
	 * This method must be called during idle times. If this object is currently downloading, 
	 * the setter Thread will wait the end of the task.
	 * 
	 * @param executor - The ExecutorService
	 */
	public synchronized void setExecutorService(@NonNull ExecutorService executor)
	{
		this.executor = executor;
	}
	
	/**
	 * Get the executor of the individual download tasks.
	 * 
	 * If there isn't an executor set, a default one will be set and returned.
	 * 
	 * @return the task {@link #ExecutorService}.
	 */
	public synchronized ExecutorService getExecutorService()
	{
		return executor != null ? executor : Executors.newFixedThreadPool(10);
	}
	
	@Override
	public synchronized Boolean call()
	{
        ExecutorService executor = getExecutorService();
        
        //executes the downloadFile list.
        
        return true;
	}
	
	
	
	@Override
	public void updateProgress()
	{
		
	}
}
