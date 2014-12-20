package net.planetgeeks.mcstarter.util.http;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.java.Log;
import net.planetgeeks.mcstarter.util.http.HttpRequest.HttpGetRequest;
import net.planetgeeks.mcstarter.util.task.RecoverableProgressTask;

/**
 * Represents a task object that manages a downloadable file.
 * Downloads the file from the given remote location and saves it has a temporary file.
 * At the end renames the temporary file to the final destination.
 * <p>
 * This object must be executed by an HttpDownloader. Never use {@link #call()} method! 
 * For download a single file you can use the static {@link #download(URL url, File file)} method.
 * 
 * @author Flood2d
 */

@Log
public class HttpFile extends RecoverableProgressTask<HttpFile>
{
	@Getter
	private File destination;
	@Getter
	private HttpGetRequest request;
	@Getter
	private URL remoteLocation;
	@Getter
	private HttpDownloader downloader;
	
	private int cachedLength = -1;
	
	protected HttpFile(@NonNull URL remoteLocation, @NonNull File destination, @NonNull HttpDownloader downloader)
	{
		this.destination = destination;
		this.remoteLocation = remoteLocation;
		this.downloader = downloader;
	}
	
	/**
	 * Download a file from the given {@link #getRemoteLocation()} and save it
	 * to {@link #getOutFile()}.
	 * 
	 * If an I/O Exception occurs, this method will automatically attempt to recover it.
	 * 
	 * @throws IOException if an I/O exception occurs.
	 * @throws InterruptedException if the <code>Thread</code> running this task
	 *             has been interrupted.
	 */
	@Override
	public synchronized HttpFile call() throws InterruptedException, IOException
	{
		File temp = getTempLocation();

		try
		{
			checkInterrupt();
			
			request = new HttpGetRequest(remoteLocation);

			request.call();

			if(!request.successful(HttpURLConnection.HTTP_OK))
				throw new IOException(String.format("There's no resource available at '%s'!", remoteLocation.toString()));
			
			request.saveToFile(temp);

			if (destination.exists())
				destination.delete();

			temp.renameTo(destination);
			
			return this;
		}
		catch(IOException e)
		{
			if(isRecoverable() && getAttemptCounter() < getAttemptLimit())
			{
				log.warning(String.format("Unable to download the file from %s! Another attempt will be performed after %d seconds.", remoteLocation.toString(), getAttemptDelay() / 1000L));
				
				incrementAttemptCounter();
				
				clearResources();
				
				Thread.sleep(getAttemptDelay());
				
				return call();
			}

			log.severe(String.format("Unable to download the file from %s! An I/O Exception has been thrown.", remoteLocation.toString()));
			
			throw e;
		}
		finally
		{
			clearResources();
			
			downloader.addTerminated(this);
			
			resetAttemptCounter();
		}
	}
	
	private void clearResources()
	{
		if(request != null)
			request.close();
		
		if(getTempLocation().exists())
			getTempLocation().delete();
	}

	private File getTempLocation()
	{
		return destination.getParentFile() != null ? new File(destination.getParentFile(), destination.getName() + ".temp") : new File(destination.getName() + ".temp");
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
	
	/**
	 * Get file length.
	 * 
	 * @return file length in bytes, or -1 the length can't be retrieved.
	 */
	public int getLength()
	{
		return request != null ? (cachedLength = request.getBytesTotal()) : cachedLength;
	}
	
	/**
	 * Download a file using a single <code>Thread</code> {@link #HttpDownloader}
	 * 
	 * If an I/O Exception occurs, this method will attempt to recover the download for 2 times with a delay of 10 seconds.
	 * If you want to customize attempt features you can look at {@link #download(URL, File, boolean, int, long)}.
	 * 
	 * @param remoteLocation - The remote location.
	 * @param destination - The destination file.
	 * @return a Future object that gives you information about the executed task.
	 * @throws InterruptedException if the running <code>Thread</code> has been interrupted.
	 * @throws ExecutionException if the task has thrown an exception.
	 */
	public static Future<HttpFile> download(@NonNull URL remoteLocation, @NonNull File destination) throws InterruptedException, ExecutionException
	{
		return download(remoteLocation, destination, true, 3, 15 * 1000L);
	}
	
	/**
	 * Download a file using a single <code>Thread</code> {@link #HttpDownloader}
	 * 
	 * This is an extended version of {@link #download(URL, File)}
	 * @param remoteLocation - The remote location.
	 * @param destination - The destination file.
	 * @param recoverable - True if you want this method to try a recovery if an I/O Exception occurs.
	 * @param attemptLimit - The limit of attempts.
	 * @param attemptDelay - The interval between attempts in milliseconds.
	 * 
	 * @return a Future object that gives you information about the executed task.
	 * @throws InterruptedException if the running <code>Thread</code> has been interrupted.
	 * @throws ExecutionException if the task has thrown an exception.
	 */
	public static Future<HttpFile> download(@NonNull URL remoteLocation, @NonNull File destination, boolean recoverable, int attemptLimit, long attemptDelay) throws InterruptedException, ExecutionException
	{
		HttpDownloader downloader = new HttpDownloader();
		
		downloader.setExecutorService(Executors.newSingleThreadExecutor());
		downloader.setRecoverable(recoverable);
		downloader.setAttemptDelay(attemptDelay);
		downloader.setAttemptLimit(attemptLimit);
		
		HttpFile download = new HttpFile(remoteLocation, destination, downloader);
		
		downloader.add(download);
		
		List<Future<HttpFile>> futures = downloader.call();
		
		return futures != null && futures.size() > 0 ? futures.get(0) : null;
	}
}
