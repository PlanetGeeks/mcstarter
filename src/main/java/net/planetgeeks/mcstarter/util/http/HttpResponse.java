package net.planetgeeks.mcstarter.util.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import lombok.Getter;
import lombok.NonNull;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Manage an array of bytes and provide different conversion methods.
 * 
 * @author Flood2d
 */
public class HttpResponse
{
	@Getter
	private byte[] data;
	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * Construct a new {@link #HttpResponse} object with the given array of bytes.
	 * 
	 * The array of bytes mustn't be null.
	 * 
	 * @param data - the array of bytes
	 */
	public HttpResponse(@NonNull byte... data)
	{
		this.data = data;
	}

	/**
	 * Return the response as {@link #String} using UTF-8 encoding.
	 * 
	 * @return the string.
	 * @throws IOException if an exception occurs during data decoding.
	 */
	public String asUTF8String() throws IOException
	{
		return asString("UTF-8");
	}

	/**
	 * Return the response as {@link #String}.
	 * 
	 * @param encoding - the encoding that will be used to decode the array of
	 *            bytes.
	 * @return the string.
	 * @throws UnsupportedEncodingException if the given encoding in not
	 *             supported.
	 */
	public String asString(String encoding) throws UnsupportedEncodingException
	{
		return new String(data, encoding);
	}

	/**
	 * Return the response as a json object.
	 * 
	 * @param objClass - the class that will be used by the
	 *            {@link #ObjectMapper} to create and setup the json object.
	 * @return the object.
	 * @throws IOException will be throw if the given objClass is not suitable
	 *             for this response.
	 */
	public <T> T asJSONObject(Class<T> objClass) throws IOException
	{
		return mapper.readValue(data, objClass);
	}
}
