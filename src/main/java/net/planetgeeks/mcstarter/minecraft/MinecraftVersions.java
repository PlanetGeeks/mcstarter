package net.planetgeeks.mcstarter.minecraft;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import net.planetgeeks.mcstarter.app.ReleaseVersion;
import net.planetgeeks.mcstarter.app.SnapshotVersion;
import net.planetgeeks.mcstarter.app.Version;
import net.planetgeeks.mcstarter.util.http.HttpRequest.HttpGetRequest;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Contains the list of all minecraft versions that can be installed.
 * 
 * @author Flood2d
 */
public class MinecraftVersions
{
	private static final String VERSIONS_URL = "versions/versions.json";
	private static MinecraftVersions cached;
	private static ObjectMapper mapper = new ObjectMapper();

	@Setter
	private LatestVersions latest;
	@Setter
	private List<Version> versions;

	/**
	 * Retrive versions information from internet or from a cached container if this is available.
	 * 
	 * @return a new <code>MinecraftVersions</code> object containing versions information.
	 * @throws IOException
	 */
	public synchronized static MinecraftVersions retrive() throws IOException
	{
		if (cached != null)
			return cached;

		return update();
	}

	/**
	 * Retrive versions information from internet and update the cached container.
	 * 
	 * @return a new <code>MinecraftVersions</code> object containing versions information.
	 * @throws IOException
	 */
	protected synchronized static MinecraftVersions update() throws IOException
	{
		try (HttpGetRequest request = new HttpGetRequest(new URL(Minecraft.getDownloadUrl(), VERSIONS_URL)))
		{
			request.call();

			if (!request.successful(HttpURLConnection.HTTP_OK))
				throw new IOException("Invalid get response code. Expected 200!");

			cached = mapper.readValue(request.getInputStream(), MinecraftVersions.class);
		}

		return cached;
	}

	private List<Version> getVersionsList()
	{
		if (versions == null)
			throw new IllegalArgumentException("This instance contains invalid information!");

		return versions;
	}
	
	private LatestVersions getLatestVersions()
	{
		if (latest == null)
			throw new IllegalArgumentException("This instance contains invalid information!");

		return latest;
	}

	/**
	 * Get the version by using its id.
	 * 
	 * @param id - the id of the requested version.
	 * @return a {@link #Version} object.
	 * @throws {@link #NullPointerException} if there's no version with the given id.
	 */
	public synchronized Version getVersion(@NonNull String id)
	{
		for (Version version : getVersionsList())
			if(version.getId().equals(id))
				return version;
		
		throw new NullPointerException("There's no version with the given id!");
	}

	/**
	 * Get the latest snapshot version.
	 * 
	 * @return a {@link #SnapshotVersion} object.
	 */
	public synchronized SnapshotVersion getLatestSnapshot()
	{
        return (SnapshotVersion) getVersion(getLatestVersions().getSnapshot());
	}

	/**
	 * Get the latest release version.
	 * 
	 * @return a {@link #ReleaseVersion} object.
	 */
	public synchronized ReleaseVersion getLatestRelease()
	{
        return (ReleaseVersion) getVersion(getLatestVersions().getRelease());
	}

	@Data
	public static class LatestVersions
	{
		private String snapshot;
		private String release;
	}
}
