package net.planetgeeks.mcstarter.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import lombok.Getter;
import lombok.NonNull;

/**
 * Provides different methods for the managing of zip files.
 * <p>
 * Actions on this object can be performed one at a time.
 * 
 * @author Flood2d
 */
public class Zip implements Closeable
{
	@Getter
	private final InputStream inputStream;

	/**
	 * Construct a Zip object with the given input stream.
	 * <p>
	 * {@link #getInputStream()} will return the given input stream.
	 * 
	 * @param inputStream - The input stream
	 */
	public Zip(@NonNull InputStream inputStream)
	{
		this.inputStream = inputStream;
	}

	/**
	 * Construct a Zip object with the given file.
	 * <p>
	 * {@link #getInputStream()} will return an instance of
	 * {@link #FileInputStream}.
	 * 
	 * @param file - The zip file
	 * @throws FileNotFoundException
	 */
	public Zip(@NonNull File file) throws FileNotFoundException
	{
		this(new FileInputStream(file));
	}

	/**
	 * Extract an entry from this Zip file to the given destination.
	 * 
	 * @param destination - File destination.
	 * @param entryName - the name of the entry (complete path).
	 * @throws IOException - if an I/O Exception occurs extracting the entry.
	 */
	public synchronized void extractEntry(@NonNull File destination, @NonNull String entryName) throws IOException
	{
		IOUtils.prepare(destination);

		ZipInputStream zis = null;

		try
		{
			zis = new ZipInputStream(inputStream);

			ZipEntry entry;

			while ((entry = zis.getNextEntry()) != null)
				if (entry.getName().equals(entryName))
				{
					IOUtils.copy(zis, destination);
					break;
				}

		}
		finally
		{
			IOUtils.closeQuietly(zis);
		}
	}

	/**
	 * Copy this Zip file to the given destination.
	 * <p>
	 * This method will ignore all entries with a name that starts with one of
	 * those specified in the given <code>ignored</code> parameter.
	 * 
	 * @param destination - File destination.
	 * @param ignored - A list of entry names that will be ignored during copy.
	 * @throws IOException - if an I/O Exception occurs copying this Zip file.
	 */
	public synchronized void copyTo(@NonNull File destination, @NonNull List<String> ignored) throws IOException
	{
		IOUtils.prepare(destination);

		ZipInputStream zis = null;
		ZipOutputStream zos = null;

		try
		{
			zis = new ZipInputStream(new BufferedInputStream(inputStream));
			zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destination)));

			ZipEntry entry;
			entries: while ((entry = zis.getNextEntry()) != null)
			{
				if (ignored != null)
					for (String ignore : ignored)
						if (entry.getName().startsWith(ignore))
							continue entries;	
			
				if(entry.isDirectory())
				{
					zos.putNextEntry(entry);
					continue;
				}
				
				ZipEntry newEntry = new ZipEntry(entry.getName());
				newEntry.setTime(entry.getTime());		
				zos.putNextEntry(newEntry);
			    IOUtils.copy(zis, zos);
			}
		}
		finally
		{
			IOUtils.closeQuietly(zis, zos);
		}
	}

	@Override
	public void close() throws IOException
	{
		if (inputStream != null)
			inputStream.close();
	}
}
