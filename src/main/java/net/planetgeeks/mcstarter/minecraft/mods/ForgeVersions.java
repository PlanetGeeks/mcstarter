package net.planetgeeks.mcstarter.minecraft.mods;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import net.planetgeeks.mcstarter.app.version.VersionsContainer;

public class ForgeVersions extends VersionsContainer
{
	private static ForgeVersions cached;

	/**
	 * Retrieve versions information online or from a static cached container if
	 * this is available.
	 * 
	 * @return a <code>ForgeVersions</code> object containing versions
	 *         information.
	 * @throws IOException
	 */
	public static ForgeVersions retrieve() throws IOException
	{
		if (cached != null)
			return cached;

		return update();
	}

	/**
	 * Updated versions information online and update the static cached
	 * container.
	 * 
	 * @return a new <code>ForgeVersions</code> object containing versions
	 *         information.
	 * @throws IOException
	 */
	public static ForgeVersions update() throws IOException
	{
		return cached = getUpdated(ForgeVersions.class, getRemoteLocation());
	}
	
	public static URL getRemoteLocation() throws MalformedURLException
	{
		return null;
	}
}
