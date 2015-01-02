package net.planetgeeks.mcstarter.minecraft.mods;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.planetgeeks.mcstarter.minecraft.Minecraft;
import net.planetgeeks.mcstarter.minecraft.MinecraftManifest;
import net.planetgeeks.mcstarter.minecraft.MinecraftProfile;
import net.planetgeeks.mcstarter.minecraft.MinecraftVerifier;
import net.planetgeeks.mcstarter.minecraft.OnlineRequiredException;
import net.planetgeeks.mcstarter.minecraft.mods.forge.Forge;
import net.planetgeeks.mcstarter.minecraft.version.Version;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class ModpackProfile extends MinecraftProfile
{
	private String url;
	private Forge forgeLoader;
	private LiteLoader liteLoader;
	private List<Mod> mods = new ArrayList<>();

	@JsonCreator
	public ModpackProfile(@JsonProperty("id") @NonNull String id)
	{
		super(id);
	}

	@JsonIgnore
	public String getType()
	{
		return "modded";
	}

	@Override
	protected MinecraftManifest retriveVersion(MinecraftVerifier verifier) throws InterruptedException, IOException, OnlineRequiredException, Exception
	{
		Minecraft minecraft = verifier.getApp();

		File manifestFile = MinecraftManifest.getManifestFile(minecraft, getVersionName());
		MinecraftManifest manifest = null;
		
		if (!manifestFile.exists())
		{
			// TODO SET DOWNLOADING MANIFEST FILE

			/**
			 * TODO FOR DEV TESTING if(!minecraft.isOnline()) throw new
			 * OnlineRequiredException(
			 * "Version manifest must be downloaded with a valid online session!"
			 * );
			 **/

			// TODO APPLY LITELOADER PATCH
			manifest = forgeLoader.retrieveManifest(minecraft);
		}

		// TODO SET VERIFING VERSION FILE
		File versionFile = null; //TODO forgeLoader.

		if (!versionFile.exists())
		{
			// TODO SET SUBMITTED VERSION FILE DOWNLOAD
			/**
			 * COMMENTED FOR DEV TESTING. if(!minecraft.isOnline()) throw new
			 * OnlineRequiredException
			 * ("Version file must be downloaded with a valid online session!");
			 **/

			// APPLY PATCH IF IT IS A LEGACY VERSION.
			verifier.getDownloader().submit(MinecraftManifest.getVersionURL(getVersion().getId()), versionFile);
		}

		return manifest; // TODO
	}

	@Override
	protected String getVersionName()
	{
		String versionName = forgeLoader != null ? versionName = forgeLoader.getVersionName(getVersion()) : getVersion().getId();

		if (liteLoader != null)
			versionName = liteLoader.getVersionName(getVersion(), new Version(versionName));

		return versionName;
	}
}
