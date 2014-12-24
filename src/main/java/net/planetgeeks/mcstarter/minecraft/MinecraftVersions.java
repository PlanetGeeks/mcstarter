package net.planetgeeks.mcstarter.minecraft;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import net.planetgeeks.mcstarter.app.Library;
import net.planetgeeks.mcstarter.app.version.VersionsContainer;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Contains the list of all minecraft versions that can be installed.
 * 
 * @author Flood2d
 */
public class MinecraftVersions extends VersionsContainer
{
	@Getter
	private static final String VERSIONS_URL = "versions/versions.json";

	public static URL getRemoteLocation() throws MalformedURLException
	{
		return new URL(Minecraft.getDownloadURL(), VERSIONS_URL);
	}

	public static MinecraftVersions getUpdated() throws JsonParseException, JsonMappingException, MalformedURLException, IOException
	{
		return getUpdated(MinecraftVersions.class, getRemoteLocation());
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Data
	public static class MinecraftVersion
	{
		private String id;
		private Date time;
		private Date releaseTime;
		private String type;
		private String minecraftArguments;
		private List<Library> libraries = new ArrayList<>();
		private String mainClass;
		private String minimumLauncherVersion;
		private String assets;
		private String inheritsFrom;
		private String jar;

		public static MinecraftVersion readFrom(File file) throws JsonParseException, JsonMappingException, IOException
		{
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(file, MinecraftVersion.class);
		}
	}
}
