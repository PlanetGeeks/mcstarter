package net.planetgeeks.mcstarter.http;

import static net.planetgeeks.mcstarter.util.LogUtils.log;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.planetgeeks.mcstarter.http.HttpRequest.HttpGetRequest;
import net.planetgeeks.mcstarter.task.Task;
import net.planetgeeks.mcstarter.util.Defaults;
import net.planetgeeks.mcstarter.util.Listenable;
import net.planetgeeks.mcstarter.util.Listener;
import net.planetgeeks.mcstarter.util.ProgressView;
import net.planetgeeks.mcstarter.util.Recoverable;

/**
 * Represents a task that manages a file that can be retrieved online. Downloads
 * the file from the given remote location and saves it has a temporary file. At
 * the end renames the temporary file to the final destination.
 * <p>
 * This object must be executed by an HttpDownloader. Never use {@link #call()}
 * method! For download a single file you can use the static
 * {@link #download(URL url, File file)} method.
 * 
 * @author Flood2d
 */

public class HttpFile extends ProgressView implements Task<HttpFile>, Recoverable, Observer, Listenable<HttpFile>
{
	@Getter
	private File destination;
	@Getter
	private HttpGetRequest request;
	@Getter
	private URL remoteLocation;
	@Getter
	private HttpDownloader downloader;

	@Getter
	@Setter
	private int attemptLimit = 2;
	@Getter
	@Setter
	private long attemptDelay = 10 * 1000L;
	@Getter
	@Setter
	private boolean recoverable = true;
	@Getter(AccessLevel.PRIVATE)
	private int attemptCounter = 0;

	private Set<Listener<HttpFile>> listeners = new LinkedHashSet<>();

	private int cachedLength = -1;

	protected HttpFile(@NonNull URL remoteLocation, @NonNull File destination,
			@NonNull HttpDownloader downloader)
	{
		this.destination = destination;
		this.remoteLocation = remoteLocation;
		this.downloader = downloader;
	}

	/**
	 * Download a file from the given {@link #getRemoteLocation()} and save it
	 * to {@link #getOutFile()}.
	 * 
	 * If an I/O Exception occurs, this method will automatically attempt to
	 * recover it.
	 * 
	 * @throws IOException if an I/O exception occurs.
	 * @throws InterruptedException if the <code>Thread</code> running this task
	 *             has been interrupted.
	 */
	@Override
	public synchronized HttpFile call() throws IOException, InterruptedException
	{
		log.info(Defaults.getString("http.file.download.start", remoteLocation.toString()));

		File temp = getTempLocation();

		try
		{
			checkStatus();

			request = new HttpGetRequest(remoteLocation);

			request.addObserver(this);

			request.call();

			if (!request.successful(HttpURLConnection.HTTP_OK))
				throw new IOException(Defaults.getString("http.file.download.noresource", remoteLocation.toString()));

			request.saveToFile(temp);

			if (destination.exists())
				destination.delete();

			temp.renameTo(destination);

			callEnd(this, null);

			return this;
		}
		catch (InterruptedException e)
		{
			callEnd(this, e);

			throw e;
		}
		catch (Exception e)
		{
			if (isRecoverable() && getAttemptCounter() < getAttemptLimit())
			{
				log.warning(Defaults.getString("http.file.download.reattempt", remoteLocation.toString(), getAttemptDelay() / 1000L));

				attemptCounter++;

				clearResources();

				try
				{
					Thread.sleep(getAttemptDelay());
				}
				catch (InterruptedException d)
				{
					callEnd(this, d);

					throw d;
				}

				return call();
			}

			log.severe(Defaults.getString("http.file.download.ioexception", remoteLocation.toString()));

			callEnd(this, e);

			throw e;
		}
		finally
		{
			clearResources();

			attemptCounter = 0;
		}
	}

	private void clearResources()
	{
		if (request != null)
			request.close();

		if (getTempLocation().exists())
			getTempLocation().delete();
	}

	private File getTempLocation()
	{
		return destination.getParentFile() != null ? new File(destination.getParentFile(), destination.getName() + ".temp") : new File(destination.getName() + ".temp");
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
	 * Download a file using a single <code>Thread</code>
	 * {@link #HttpDownloader}
	 * 
	 * If an I/O Exception occurs, this method will attempt to recover the
	 * download for 2 times with a delay of 10 seconds. If you want to customize
	 * attempt features you can look at
	 * {@link #download(URL, File, boolean, int, long)}.
	 * 
	 * @param remoteLocation - The remote location.
	 * @param destination - The destination file.
	 * @return a Future object that gives you information about the executed
	 *         task.
	 * @throws InterruptedException if the running <code>Thread</code> has been
	 *             interrupted.
	 * @throws ExecutionException if the task has thrown an exception.
	 */
	public static HttpDownloader download(@NonNull URL remoteLocation, @NonNull File destination) throws InterruptedException, ExecutionException
	{
		return download(remoteLocation, destination, true, 3, 15 * 1000L);
	}

	/**
	 * Download a file using a single <code>Thread</code>
	 * {@link #HttpDownloader}
	 * 
	 * This is an extended version of {@link #download(URL, File)}
	 * 
	 * @param remoteLocation - The remote location.
	 * @param destination - The destination file.
	 * @param recoverable - True if you want this method to try a recovery if an
	 *            I/O Exception occurs.
	 * @param attemptLimit - The limit of attempts.
	 * @param attemptDelay - The interval between attempts in milliseconds.
	 * 
	 * @return a Future object that gives you information about the executed
	 *         task.
	 * @throws InterruptedException if the running <code>Thread</code> has been
	 *             interrupted.
	 * @throws ExecutionException if the task has thrown an exception.
	 */
	public static HttpDownloader download(@NonNull URL remoteLocation, @NonNull File destination, boolean recoverable, int attemptLimit, long attemptDelay) throws InterruptedException, ExecutionException
	{
		HttpDownloader downloader = new HttpDownloader();

		downloader.setExecutor(Executors.newSingleThreadExecutor());
		downloader.setRecoverable(recoverable);
		downloader.setAttemptDelay(attemptDelay);
		downloader.setAttemptLimit(attemptLimit);

		HttpFile download = new HttpFile(remoteLocation, destination, downloader);

		downloader.submit(download);

		downloader.call();

		return downloader;
	}

	@Override
	public void checkStatus() throws InterruptedException
	{
		if (Thread.interrupted())
			throw new InterruptedException("Download interrupted");
	}

	@Override
	public void addListener(Listener<HttpFile> listener)
	{
		synchronized (listeners)
		{
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(Listener<HttpFile> listener)
	{
		synchronized (listeners)
		{
			listeners.remove(listener);
		}
	}

	@Override
	public void callEnd(HttpFile object, Exception e)
	{
		synchronized (listeners)
		{
			for (Listener<HttpFile> listener : listeners)
				listener.onEnd(object, e);
		}
	}

	@Override
	public void update(Observable observable, Object arg)
	{
		if (observable instanceof HttpRequest)
			setProgress(((HttpRequest) observable).getProgress());
	}
}
