package net.planetgeeks.mcstarter.app.install;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.planetgeeks.mcstarter.util.http.HttpDownloader;
import net.planetgeeks.mcstarter.util.http.HttpFile;

public class AppFile
{
	@Getter
	@NonNull
	public final File file;

	@Getter
	@Setter
	public URL remoteLocation;

	@Getter
	@Setter
	private FileChecksum checksum = new FileChecksum();

	public AppFile(@NonNull File file)
	{
		this.file = file;
	}

	/**
	 * @return false if the file doesn't exist or must be updated.
	 * @throws IOException
	 */
	public boolean verify() throws IOException
    {
        return file.exists() && checksum.compare(file);
    }
	
	/**
	 * Download the file from {@link getRemoteLocation()} and save it to {@link #getDestination()}.
	 * 
	 * @return the downloader.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public HttpDownloader download() throws InterruptedException, ExecutionException
	{
		if(getRemoteLocation() == null)
			throw new IllegalStateException("You must set a remote location!");
		
		return HttpFile.download(remoteLocation, file);
	}
}
