package net.planetgeeks.mcstarter.util.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.planetgeeks.mcstarter.util.task.ProgressTask;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Allows to manage requests through the http protocol. Based on
 * {@link java.net.HttpURLConnection}.
 * 
 * @author Flood2d
 */
public abstract class HttpRequest extends ProgressTask<HttpRequest> implements
		Closeable
{
	/**
	 * Read timeout in milliseconds.
	 */
	private static final int READ_TIMEOUT = 1000 * 60;

	@Getter
	private URL url;
	@Getter
	private HttpURLConnection connection;
	@Getter
	private InputStream inputStream;
	private HashMap<String, String> headers = new HashMap<>();

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private int bytesReaded;
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private int bytesTotal = -1;

	protected HttpRequest(@NonNull URL url)
	{
		this.url = reformat(url);
	}

	public HttpRequest setHeader(@NonNull String key, @NonNull String value)
	{
		synchronized (headers)
		{
			headers.put(key, value);
		}

		return this;
	}

	/**
	 * Perform the request and setup the stream returned by
	 * {@link #getInputStream()}.
	 * 
	 * @throws IOException if an exception occurs during the request.
	 */
	@Override
	public synchronized HttpRequest call() throws IOException
	{
		if (connection != null)
			throw new IllegalStateException("Connection already established!");

		boolean success = false;

		try
		{
			connection = (HttpURLConnection) url.openConnection();

			setupConnection(connection);

			connection.connect();

			execute(connection);

			success = true;
			
			return this;
		}
		finally
		{
			if (!success)
				close();
		}
	}

	protected void setupConnection(HttpURLConnection connection) throws ProtocolException
	{
		connection.setRequestMethod(getMethod());
		connection.setUseCaches(false);
		connection.setDoOutput(true);
		connection.setReadTimeout(READ_TIMEOUT);

		synchronized (headers)
		{
			for (Map.Entry<String, String> entry : headers.entrySet())
			{
				connection.addRequestProperty(entry.getKey(), entry.getValue());
			}
		}
	}

	protected void execute(HttpURLConnection connection) throws IOException
	{
		inputStream = connection.getResponseCode() == HttpURLConnection.HTTP_OK ? connection.getInputStream() : connection.getErrorStream();
	}

	/**
	 * Check if the request was successful. If the response code of the relative
	 * {@link #HttpURLConnection} isn't equals to the one given this method will
	 * automatically close the connection and will throw an {@link #IOException}
	 * . This method must be called after {@link #perform()}.
	 * 
	 * @param code - the expected code
	 * @return this object.
	 * @throws IOException if the request wasn't successful.
	 */
	public synchronized HttpRequest successfulOrClose(int code) throws IOException
	{
		try
		{
			if (successful(code))
				return this;
			else
				throw new IOException();
		}
		catch (IOException e)
		{
			close();
			throw new IOException("The request wasn't successful!");
		}
	}

	/**
	 * Check if the request was successful. This method must be called after
	 * {@link #perform()}.
	 * 
	 * @param code - the expected code.
	 * @return true if the response code equals the one given.
	 * @throws IOException if the request hasn't provided a response code.
	 */
	public synchronized boolean successful(int code) throws IOException
	{
		return code == getResponseCode();
	}

	public synchronized int getResponseCode() throws IOException
	{
		if (connection == null)
			throw new IllegalStateException("This request wasn't performed!");

		return connection.getResponseCode();
	}

	@Override
	public void close()
	{
		if (connection != null)
			connection.disconnect();
	}

	/**
	 * Reformat an {#link #URL} that may contains blank spaces or other syntax
	 * errors.
	 * 
	 * @param url - The {@link #URL} object to reformat.
	 * @return a reformat {@link #URL} or the given url if reformat isn't
	 *         possible.
	 */
	public static URL reformat(@NonNull URL url)
	{
		try
		{
			url = new URL(url.toString());
			URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
			return uri.toURL();
		}
		catch (Exception e)
		{
			return url;
		}
	}

	/**
	 * Increment and update the progress.
	 * 
	 * @param bytes to add.
	 */
	protected void addBytesReaded(int bytes)
	{
		setBytesReaded(getBytesReaded() + bytes);

		updateProgress();
	}

	@Override
	public void updateProgress()
	{
		setProgress(getBytesTotal() <= 0 ? -1D : getBytesReaded() / (double) getBytesTotal());
	}

	/**
	 * @return true if there's content to read!
	 */
	public boolean hasResponseContent()
	{
		return getInputStream() != null;
	}

	/**
	 * @return the http request method that will be used during
	 *         {@link #perform()}.
	 */
	protected abstract String getMethod();

	/**
	 * Allows to manage post requests. Based on {@link #HttpRequest}.
	 * 
	 * @author Flood2d
	 */
	public static class HttpPostRequest extends HttpRequest
	{
		@Getter
		@Setter(AccessLevel.PROTECTED)
		private String contentType;
		@Getter(AccessLevel.PROTECTED)
		private byte[] body;
		private ObjectMapper mapper = new ObjectMapper();
		private HttpResponse latestResponse;

		/**
		 * @param url - The location of the server, receiver of post request.
		 */
		public HttpPostRequest(URL url)
		{
			super(url);
		}

		@Override
		protected String getMethod()
		{
			return "POST";
		}

		@Override
		protected void setupConnection(HttpURLConnection connection) throws ProtocolException
		{
			super.setupConnection(connection);

			if (body == null)
				return;

			connection.setRequestProperty("Content-Type", getContentType());
			connection.setRequestProperty("Content-Length", String.valueOf(body.length));
			connection.setDoInput(true);
		}

		@Override
		protected void execute(HttpURLConnection connection) throws IOException
		{
			latestResponse = null;

			if (body != null)
			{
				DataOutputStream out = new DataOutputStream(connection.getOutputStream());
				out.write(body);
				out.flush();
				out.close();
			}

			super.execute(connection);
		}

		/**
		 * Set the body of this post request to a JSON serialized object. The
		 * contentType of the request will be set to 'application/json'.
		 * 
		 * @param object - Object that will be serialized in JSON.
		 * @throws IOException if an exception occurs during serialization.
		 * @return This object.
		 */
		public HttpPostRequest setBodyJson(Object object) throws IOException
		{
			setContentType("application/json");
			setBody(mapper.writeValueAsBytes(object));

			return this;
		}

		protected synchronized void setBody(byte... body)
		{
			this.body = body;
		}

		/**
		 * Return the response content of this request and close the connection.
		 * This method will be called after {@link #perform()}. If the
		 * connection has been already closed, this method will return the
		 * latest returned {@link #HttpResponse}.
		 *
		 * @return an {@link #HttpResponse} object.
		 * @throws IOException if there's no content available to read.
		 * @throws InterruptedException if the Thread running this method has
		 *             been interrupted.
		 */
		public synchronized HttpResponse getResponseContent() throws IOException, InterruptedException
		{
			if (!hasResponseContent())
				throw new IllegalStateException("There's no content to read! InputStream is null.");

			if (latestResponse != null)
				return latestResponse;

			setBytesTotal(getConnection().getContentLength());

			try
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				int bytes;
				while ((bytes = getInputStream().read()) != -1)
				{
					baos.write(bytes);
					addBytesReaded(bytes);
					checkStatus();
				}

				this.latestResponse = new HttpResponse(baos.toByteArray());
				return this.latestResponse;
			}
			finally
			{
				close();
			}
		}

		@Override
		public boolean hasResponseContent()
		{
			return super.hasResponseContent() || latestResponse != null;
		}
	}

	/**
	 * Allows to manage get requests. Based on {@link #HttpRequest}.
	 * 
	 * @author Flood2d
	 */
	public static class HttpGetRequest extends HttpRequest
	{
		/** Read buffer size **/
		private static final int READ_BUFFER_LENGTH = 1024 * 8;

		/**
		 * @param url - The location of the resource.
		 */
		public HttpGetRequest(URL url)
		{
			super(url);
		}

		@Override
		protected String getMethod()
		{
			return "GET";
		}

		/**
		 * Save the response content of this request to a file and then close
		 * the request.
		 * 
		 * @param file - {@link #File} to save. Must be not null!
		 * @throws IOException if an I/O exception occurs.
		 * @throws InterruptedException if the executing <code>Thread</code> has
		 *             been interrupted.
		 */
		public synchronized void saveToFile(@NonNull File file) throws IOException, InterruptedException
		{
			File parent = file.getParentFile();
			
			if(parent != null && !parent.exists())
				parent.mkdirs();
			else if(file.exists())
				file.delete();

			FileOutputStream fos = null;
			BufferedOutputStream bos = null;

			try
			{
				bos = new BufferedOutputStream((fos = new FileOutputStream(file)));

				saveToStream(bos);
			}
			finally
			{
				if (fos != null)
					fos.close();
				if (bos != null)
					bos.close();
			}
		}

		/**
		 * Save the response content to an <code>OutputStream</code> and then
		 * close the request.
		 * 
		 * @param file - {@link #OutputStream} to write on. Must be not null!
		 * @throws IOException if an I/O exception occurs.
		 * @throws InterruptedException if the executing <code>Thread</code> has
		 *             been interrupted.
		 */
		public synchronized void saveToStream(@NonNull OutputStream out) throws IOException, InterruptedException
		{
			if (!hasResponseContent())
				throw new IllegalStateException("There's no content to read! InputStream is null.");

			setBytesTotal(getConnection().getContentLength());

			BufferedInputStream bis = null;

			try
			{
				bis = new BufferedInputStream(getInputStream());

				byte[] data = new byte[READ_BUFFER_LENGTH];

				int bytes;
				while ((bytes = bis.read(data, 0, data.length)) != -1)
				{
					out.write(data, 0, data.length);
					addBytesReaded(bytes);
					checkStatus();
				}
			}
			finally
			{
				if (bis != null)
					bis.close();
				close();
			}
		}
	}
}
