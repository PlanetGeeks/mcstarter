package net.planetgeeks.mcstarter.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
}
