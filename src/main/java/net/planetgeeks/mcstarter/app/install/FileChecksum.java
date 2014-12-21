package net.planetgeeks.mcstarter.app.install;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileChecksum
{
	private String sha1 = null;
	private long size = -1;

	@JsonIgnore
	public boolean compare(@NonNull File file) throws IOException, NoSuchAlgorithmException
	{	
		return compareSha1(file) || compareSize(file);
	}

	@JsonIgnore
	public boolean compareSha1(@NonNull File file) throws IOException, NoSuchAlgorithmException
	{
		return this.sha1 != null && compareSha1(getSha1(file));
	}
	
	@JsonIgnore
	public boolean compareSize(@NonNull File file)
	{
		return compareSize(file.length());
	}
	
	@JsonIgnore
	public boolean compareSha1(@NonNull String sha1)
	{
		return this.sha1 != null && this.sha1.equals(sha1);
	}

	@JsonIgnore
	public boolean compareSize(long size)
	{
		return this.size != -1 && this.size == size;
	}

	@JsonIgnore
	public static String getSha1(@NonNull File file) throws NoSuchAlgorithmException, IOException
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
	}
}
