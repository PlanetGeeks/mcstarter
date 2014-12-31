package net.planetgeeks.mcstarter.minecraft;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NonNull;
import net.planetgeeks.mcstarter.util.Defaults;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MinecraftManifest
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
	
	public static MinecraftManifest readFrom(File file) throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(file, MinecraftManifest.class);
	}
	
	private static String getPath(@NonNull String versionId, @NonNull String extension)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(versionId);
		builder.append("/");
		builder.append(versionId);
		builder.append(extension);
		
		return builder.toString();
	}
	
	public URL getVersionURL() throws MalformedURLException
	{
		return getVersionURL(id);
	}
	
	public URL getManifestURL() throws MalformedURLException
	{
		return getManifestURL(id);
	}
	
	public File getVersionFile(@NonNull Minecraft minecraft)
	{
		return getVersionFile(minecraft, id);
	}
	
	public File getManifestFile(@NonNull Minecraft minecraft)
	{
		return getManifestFile(minecraft, id);
	}

	public static URL getVersionURL(@NonNull String versionId) throws MalformedURLException
	{
		return new URL(getVersionsURL(), getPath(versionId, ".jar"));
	}

	public static File getVersionFile(@NonNull Minecraft minecraft, @NonNull String versionId)
	{
		return new File(minecraft.getVersionsDir(), getPath(versionId, ".jar"));
	}

	public static URL getManifestURL(@NonNull String versionId) throws MalformedURLException
	{
		return new URL(getVersionsURL(), getPath(versionId, ".json"));
	}

	public static File getManifestFile(@NonNull Minecraft minecraft, @NonNull String versionId)
	{
		return new File(minecraft.getVersionsDir(), getPath(versionId, ".json"));
	}
	
	public static URL getVersionsURL() throws MalformedURLException
	{
		return new URL(Defaults.getString("minecraft.baseurl.versions"));
	}
}