package net.planetgeeks.mcstarter.util.http;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Allows to manage requests through the http protocol. Based on
 * {@link java.net.HttpURLConnection}.
 * 
 * @author Flood2d
 */
public abstract class HttpRequest implements Closeable
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

	protected HttpRequest(@NonNull URL url)
	{
		this.url = url;
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
	 * Performs the request and setup the stream returned by
	 * {@link #getInputStream()}.
	 * 
	 * @throws IOException if an exception occurs during the request.
	 */
	public synchronized void perform() throws IOException
	{
		if (connection != null)
			throw new IllegalArgumentException("Connection already established!");

		boolean success = false;

		try
		{
			connection = (HttpURLConnection) url.openConnection();

			setupConnection(connection);

			connection.connect();

			execute(connection);

			success = true;
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
	 * {@link #HttpURLConnection} isn't 200 this method will automatically close
	 * the connection and will throw an {@link #IOException}. This method must
	 * be called after {@link #perform()}.
	 * 
	 * @return this object.
	 * @throws IOException if the request wasn't successful.
	 */
	public synchronized HttpRequest successful() throws IOException
	{
		int code = getResponseCode();

		if (code == 200)
			return this;

		close();
		throw new IOException("The request wasn't successful! Returned response code : " + code);
	}

	public synchronized int getResponseCode() throws IOException
	{
		if (connection == null)
			throw new IllegalArgumentException("This request wasn't performed!");

		return connection.getResponseCode();
	}

	@Override
	public void close() throws IOException
	{
		if (connection != null)
			connection.disconnect();
	}

	/**
	 * @throws InterruptedException if this thread has been interrupted.
	 */
	protected void checkInterrupted() throws InterruptedException
	{
		if (Thread.interrupted())
			throw new InterruptedException();
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
				throw new IllegalArgumentException("There's no content to read! InputStream is null.");

			if (latestResponse != null)
				return latestResponse;

			try
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				int r;
				while ((r = getInputStream().read()) != -1)
				{
					checkInterrupted();
					baos.write(r);
				}

				this.latestResponse = new HttpResponse(baos.toByteArray());
				return this.latestResponse;
			}
			finally
			{
				close();
			}
		}
		
		/**
		 * @return true if there's content to read!
		 */
		public boolean hasResponseContent()
		{
			return getInputStream() != null || latestResponse != null;
		}
	}

	/**
	 * Allows to manage get requests. Based on {@link #HttpRequest}.
	 * 
	 * @author Flood2d
	 */
	public static class HttpGetRequest extends HttpRequest
	{
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
	}
}
