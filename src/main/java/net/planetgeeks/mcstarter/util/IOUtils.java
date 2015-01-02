package net.planetgeeks.mcstarter.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
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
			while((line = br.readLine()) != null)
			{
				builder.append(!first ? "\n" : "");
				builder.append(line);
				
				if(first)
					first = false;
			}
			
			return builder.toString();
		}
		finally
		{
			if(br != null)
				br.close();
			if(reader != null)
				reader.close();
		}
	}
	
	public static void writeTo(@NonNull InputStream is, @NonNull File file) throws IOException
	{
		writeTo(is, new FileOutputStream(file));
	}
	
	public static void writeTo(@NonNull InputStream is, @NonNull OutputStream os) throws IOException
	{
		BufferedOutputStream bos = new BufferedOutputStream(os);
		BufferedInputStream bis = new BufferedInputStream(is);
		
		try
		{
			byte[] data = new byte[1024];

			int len = 0;
			while ((len = bis.read(data, 0, data.length)) >= 0)
				bos.write(data, 0, len);	
		}
		finally
		{
			if(bos != null)
				bos.close();
			if(bis != null)
				bis.close();
		}
	}
	
	public static void prepare(@NonNull File file)
	{
		if(file.getParentFile() != null && !file.getParentFile().exists())
			file.mkdirs();
	}
}
