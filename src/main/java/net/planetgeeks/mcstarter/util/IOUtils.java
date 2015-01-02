package net.planetgeeks.mcstarter.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lombok.NonNull;

public class IOUtils
{
	/**
	 * Read a file content and get it as a String object.
	 * 
	 * @param file - File to read.
	 * @return a String containing the file content.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static String readContent(@NonNull File file) throws IOException, FileNotFoundException
	{
		StringBuilder builder = new StringBuilder();

		BufferedReader br = null;
		FileReader reader = null;

		try
		{
			br = new BufferedReader((reader = new FileReader(file)));

			String line;
			boolean first = true;
			while ((line = br.readLine()) != null)
			{
				builder.append(!first ? "\n" : "");
				builder.append(line);

				if (first)
					first = false;
			}

			return builder.toString();
		}
		finally
		{
			closeQuietly(br, reader);
		}
	}

	/**
	 * Write data from an input stream to a File Output Stream constructed with
	 * the given file.
	 * <p>
	 * This method will not close the given input stream.
	 * 
	 * @param is - The input stream.
	 * @param file - The destination file.
	 * @throws IOException
	 */
	public static void copy(@NonNull InputStream is, @NonNull File file) throws IOException
	{
		BufferedOutputStream bos = null;

		try
		{
			copy(is, bos = new BufferedOutputStream(new FileOutputStream(file)));
		}
		finally
		{
			closeQuietly(bos);
		}
	}

	/**
	 * Write data from an input stream to an output stream.
	 * <p>
	 * This method will not close the streams!
	 * 
	 * @param is - The input stream.
	 * @param os - The output stream.
	 * @throws IOException
	 */
	public static void copy(@NonNull InputStream is, @NonNull OutputStream os) throws IOException
	{
		byte[] data = new byte[1024];

		int len = 0;
		while ((len = is.read(data, 0, data.length)) >= 0)
			os.write(data, 0, len);
	}

	/**
	 * Get data contained in the given InputStream as a byte array.
	 * 
	 * @param is - Input stream.
	 * @return a byte array.
	 * @throws IOException if an I/O Exception writing the data.
	 */
	public static byte[] toByteArray(@NonNull InputStream is) throws IOException
	{
		byte[] data = new byte[4096];
		ByteArrayOutputStream entryBuffer = new ByteArrayOutputStream();
		
		int len;
		while((len = is.read(data)) != -1)
			entryBuffer.write(data, 0, len);

		return entryBuffer.toByteArray();
	}

	/**
	 * Creates file parent directory if it doesn't exist.
	 * 
	 * @param file - The file to prepare.
	 */
	public static void prepare(@NonNull File file)
	{
		if (file.getParentFile() != null && !file.getParentFile().exists())
			file.mkdirs();
	}

	/**
	 * Close all the given Closeable objects.
	 * <p>
	 * If an I/O Exception occurs closing one of them, the method will continue
	 * closing others. At the end it will throw the first IOException thrown.
	 * 
	 * @param closeables - Closeables to close.
	 * @throws IOException - The first I/O Exception thrown.
	 */
	public static void closeQuietly(Closeable... closeables) throws IOException
	{
		IOException exception = null;

		for (Closeable closeable : closeables)
			try
			{
				if (closeable != null)
					closeable.close();
			}
			catch (IOException e)
			{
				exception = exception != null ? exception : e;
			}

		if (exception != null)
			throw exception;
	}
	
	/**
	 * @return a temporary file of the given file.
	 */
	public static File getTemp(@NonNull File file)
	{
		return file.getParentFile() != null ? new File(file.getParentFile(), file.getName() + ".temp") : new File(file.getName() + ".temp");
	}
}
