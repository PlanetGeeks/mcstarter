package net.planetgeeks.mcstarter.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.NonNull;

public class EncryptUtils
{	 
	/**
	 * Calculate sha1 of the given file and get it as a String object.
	 * 
	 * @param file
	 * @return a String
	 * @throws IOException
	 */
	public static String getSha1(@NonNull File file) throws IOException
	{
		MessageDigest md = null;

		try (FileInputStream fis = new FileInputStream(file))
		{
			md = MessageDigest.getInstance("SHA1");

			byte[] data = new byte[1024];

			int bytes = 0;

			while ((bytes = fis.read(data)) != -1)
				md.update(data, 0, bytes);

			byte[] mdbytes = md.digest();

			StringBuffer sb = new StringBuffer("");

			for (int i = 0; i < mdbytes.length; i++)
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));

			return sb.toString();
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new IllegalStateException("Invalid algorithm! Internal code has been modified?");
		}
	}

}
