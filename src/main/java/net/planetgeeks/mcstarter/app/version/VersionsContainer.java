package net.planetgeeks.mcstarter.app.version;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.planetgeeks.mcstarter.http.HttpRequest.HttpGetRequest;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
	
public abstract class VersionsContainer
{
	@Getter
	private static ObjectMapper mapper = new ObjectMapper();
	@Setter(AccessLevel.PROTECTED)
	private LatestVersions latest;
	@Setter(AccessLevel.PROTECTED)
	private List<Version> versions;
	
	public static <T extends VersionsContainer> T getUpdated(@NonNull Class<T> containerType, @NonNull URL remoteLocation) throws JsonParseException, JsonMappingException, IOException
	{
		try (HttpGetRequest request = new HttpGetRequest(remoteLocation))
		{
			request.call();

			if (!request.successful(HttpURLConnection.HTTP_OK))
				throw new IOException("Invalid GET response code. Expected 200!");

			return getMapper().readValue(request.getInputStream(), containerType);
		}
	}
	
	private LatestVersions getLatestVersions()
	{
		if (latest == null)
			throw new IllegalStateException("This instance contains invalid information!");

		return latest;
	}

	/**
	 * Get the version by using its id.
	 * 
	 * @param id - the id of the requested version.
	 * @return a {@link #Version} object.
	 * @throws {@link #NullPointerException} if there's no version with the given id.
	 */
	public Version getVersion(@NonNull String id)
	{
		for (Version version : getVersions())
			if(version.getId().equals(id))
				return version;
		
		throw new NullPointerException("There's no version with the given id!");
	}

	/**
	 * Get the latest snapshot version.
	 * 
	 * @return a {@link #SnapshotVersion} object.
	 */
	public SnapshotVersion getLatestSnapshot()
	{
        return (SnapshotVersion) getVersion(getLatestVersions().getSnapshot());
	}
	
	/**
	 * Get the latest release version.
	 * 
	 * @return a {@link #ReleaseVersion} object.
	 */
	public ReleaseVersion getLatestRelease()
	{
        return (ReleaseVersion) getVersion(getLatestVersions().getRelease());
	}
	
	/**
	 * Get the list that contains all versions.
	 * 
	 * @return a {@link #Version} list.
	 */
	public List<Version> getVersions()
	{
		if (versions == null)
			throw new IllegalStateException("This instance contains invalid information!");

		return versions;
	}
	
	/**
	 * Get a list that contains all stable versions.
	 * 
	 * @return a {@link #ReleaseVersion} list.
	 */
	public List<ReleaseVersion> getReleases()
	{
		List<ReleaseVersion> releases = new ArrayList<>();
		
	    for(Version version : getVersions())
	    	if(version instanceof ReleaseVersion)
	    		releases.add((ReleaseVersion) version);
	    
	    return releases;
	}
	
	/**
	 * Get a list that contains all snapshot versions.
	 * 
	 * @return a {@link #SnapshotVersion} list.
	 */
	public List<ReleaseVersion> getSnapshots()
	{
		List<ReleaseVersion> releases = new ArrayList<>();
		
	    for(Version version : getVersions())
	    	if(version instanceof ReleaseVersion)
	    		releases.add((ReleaseVersion) version);
	    
	    return releases;
	}


	@Data
	public static class LatestVersions
	{
		private String snapshot;
		private String release;
	}
}
