package net.planetgeeks.mcstarter.http;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.planetgeeks.mcstarter.task.TaskExecutor;
import net.planetgeeks.mcstarter.util.Recoverable;

/**
 * Manages several download requests and runs them concurrently. The whole task
 * can be monitored.
 * 
 * @author Flood2d
 */
public class HttpDownloader extends TaskExecutor<HttpFile> implements Recoverable, Observer
{

	@Getter @Setter
	private int attemptLimit = 2;
	@Getter @Setter
	private long attemptDelay = 10 * 1000L;
	@Getter @Setter
	private boolean recoverable = true;
	
	/**
	 * Add a file to the download list.
	 * 
	 * This method must be called during idle times. If this object is currently
	 * downloading, the adder Thread will wait the end of the task.
	 * 
	 * @param url - the remote location of the file.
	 * @param outFile - the destination file.
	 */
	public synchronized void submit(@NonNull URL url, @NonNull File outFile)
	{
		submit(new HttpFile(url, outFile, this));
	}
	
	@Override
	public synchronized void submit(@NonNull HttpFile file)
	{
		file.addObserver(this);
		super.submit(file);
	}
	
	@Override
	public synchronized List<Future<HttpFile>> call() throws ExecutionException, InterruptedException
	{
		Set<HttpFile> files = getTasks();
		
		for(HttpFile file : files)
		{
			file.setRecoverable(isRecoverable());
			file.setAttemptLimit(getAttemptLimit());
			file.setAttemptDelay(getAttemptDelay());
		}
		
		return super.call();
	}

	@Override
	public void update(Observable o, Object arg)
	{
		if(o instanceof HttpFile)
		{
			//notify progress
		}
	}
}
