package net.planetgeeks.mcstarter.minecraft;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import lombok.Getter;
import net.planetgeeks.mcstarter.app.version.VersionsContainer;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

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
}
