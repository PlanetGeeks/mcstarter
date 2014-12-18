package net.planetgeeks.mcstarter.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import lombok.Getter;
import lombok.NonNull;
import net.planetgeeks.mcstarter.util.http.HttpRequest.HttpGetRequest;
import net.planetgeeks.mcstarter.util.task.ProgressTask;

/**
 * Represents a task object that manages a downloadable file.
 * 
 * @author Flood2d
 */
public class DownloadableFile extends ProgressTask<Boolean>
{
	@Getter
	private File outFile;

	@Getter
	private HttpGetRequest request;
	
	@Getter
	private URL remoteLocation;

	public DownloadableFile(@NonNull URL url, @NonNull File outFile)
	{
		this.outFile = outFile;
	}

	/**
	 * Download a file from the given {@link #getRemoteLocation()} and save it to {@link #getOutFile()}
	 * 
	 * @throws IOException if an I/O exception occurs.
	 * @throws InterruptedException if the <code>Thread</code> running this task has been interrupted.
	 */
	@Override
	public synchronized Boolean call() throws IOException, InterruptedException
	{
		try
		{
			request = new HttpGetRequest(remoteLocation);
			
			request.call();
			
			request.saveToFile(outFile);
			
			return true;
		}
		finally
		{
			if (request != null)
				request.close();
		}
	}

	@Override
	public void updateProgress()
	{
	}
	
	@Override
	public double getProgress()
	{
		return request != null ? request.getProgress() : 0.0D;
	}
}
