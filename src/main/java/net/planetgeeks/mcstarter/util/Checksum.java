package net.planetgeeks.mcstarter.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFilter("FileChecksum")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Checksum
{
	private String sha1 = null;
	private long size = -1;

	public Checksum()
	{

	}

	public Checksum(@NonNull String sha1)
	{
		this(sha1, -1);
	}

	public Checksum(long size)
	{
		this(null, size);
	}

	public Checksum(@NonNull String sha1, long size)
	{
		setSha1(sha1);
		setSize(size);
	}

	/**
	 * @return true if checksum sha1 isn't null or its size isn't equals to -1.
	 */
	@JsonIgnore
	public boolean isValid()
	{
		return sha1 != null || size != -1;
	}

	/**
	 * @param file - the file to compare
	 * @return return true if this is an invalid checksum, or
	 *         {@link #compareSha1(File)} return true, or
	 *         {@link #compareSize(File)} return true.
	 */
	@JsonIgnore
	public boolean compare(@NonNull File file) throws IOException
	{
		return !isValid() || compareSha1(file) || compareSize(file);
	}

	/**
	 * @param file - the file to compare
	 * @return return true if the file sha1 equals to the checksum sha1.
	 */
	@JsonIgnore
	public boolean compareSha1(@NonNull File file) throws IOException
	{
		return this.sha1 != null && compareSha1(EncryptUtils.getSha1(file));
	}

	/**
	 * @param file - the file to compare
	 * @return return true if the file length equals to the checksum size.
	 */
	@JsonIgnore
	public boolean compareSize(@NonNull File file)
	{
		return compareSize(file.length());
	}

	/**
	 * @param sha1 - the sha1 to compare
	 * @return true if the given sha1 equals to the checksum sha1.
	 */
	@JsonIgnore
	public boolean compareSha1(@NonNull String sha1)
	{
		return this.sha1 != null && this.sha1.equals(sha1);
	}

	/**
	 * @param size - the size to compare
	 * @return true if the given size equals to the checksum size.
	 */
	@JsonIgnore
	public boolean compareSize(long size)
	{
		return this.size != -1 && this.size == size;
	}

	/**
	 * @return an empty Checksum, with a null sha1 and a size equals to -1.
	 */
	public static Checksum getEmpty()
	{
		return new Checksum(null, -1);
	}

	/**
	 * Get a Checksum object from a .sha1 file.
	 * 
	 * @param file - the .sha1 file.
	 * @return a Checksum object.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InvalidChecksumException if the .sha1 file isn't valid.
	 */
	public static Checksum from(@NonNull File file) throws FileNotFoundException, IOException, InvalidChecksumException
	{
		String content = IOUtils.readContent(file).trim();

		if (content.trim().length() != 40)
			throw new InvalidChecksumException();

		return new Checksum(content);
	}

	public static class InvalidChecksumException extends Exception
	{
		private static final long serialVersionUID = 1L;
	}

	public static FilterProvider addFilters(@NonNull SimpleFilterProvider provider)
	{
		provider.addFilter("FileChecksum", new PropertyFilter());

		return provider;
	}

	public static class PropertyFilter extends SimpleBeanPropertyFilter
	{
		@Override
		public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, BeanPropertyWriter writer) throws Exception
		{
			Checksum checksum = (Checksum) pojo;

			if (!writer.getName().equals("size") || checksum.getSize() != -1)
				writer.serializeAsField(pojo, jgen, provider);
		}
	}
}
