package net.planetgeeks.mcstarter.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import lombok.Getter;
import lombok.NonNull;

public class Extractor implements Closeable
{
	@Getter
	private final InputStream inputStream;

	public Extractor(@NonNull InputStream inputStream)
	{
		this.inputStream = inputStream;
	}

	public Extractor(@NonNull File file) throws FileNotFoundException
	{
		this(new FileInputStream(file));
	}

	public synchronized void extractEntry(@NonNull String entryName, @NonNull File destination) throws IOException
	{
		IOUtils.prepare(destination);
		
		ZipInputStream zis = new ZipInputStream(inputStream);

		try
		{
			ZipEntry entry;

			while ((entry = zis.getNextEntry()) != null)
				if (entry.getName().equals(entryName))
				{
					IOUtils.writeTo(zis, destination);
					break;
				}

		}
		finally
		{
			if (zis != null)
				zis.close();
		}
	}

	@Override
	public void close() throws IOException
	{
		if (inputStream != null)
			inputStream.close();
	}
}
