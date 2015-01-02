package net.planetgeeks.mcstarter.minecraft;

import java.io.File;
import java.io.IOException;

import lombok.NonNull;
import net.planetgeeks.mcstarter.http.HttpFile;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class VanillaProfile extends MinecraftProfile
{
	@JsonCreator
	public VanillaProfile(@JsonProperty("id") @NonNull String id)
	{
		super(id);
	}

	@JsonIgnore
	public String getType()
	{
		return "vanilla";
	}

	// TODO : For modpack version, create a listeneable interface that must be
	// implemented by HttpFile. When download end, the listener of the HttpFile
	// will apply the forge/liteloader patch.

	@Override
	protected MinecraftManifest retriveVersion(MinecraftVerifier verifier) throws InterruptedException, IOException, Exception, OnlineRequiredException
	{
		// TODO SET VERIFING MANIFEST FILE
		Minecraft minecraft = verifier.getApp();

		File manifestFile = MinecraftManifest.getManifestFile(minecraft, getVersionName());

		if (!manifestFile.exists())
		{
			// TODO SET DOWNLOADING MANIFEST FILE

			/**
			 * TODO FOR DEV TESTING if(!minecraft.isOnline()) throw new
			 * OnlineRequiredException(
			 * "Version manifest must be downloaded with a valid online session!"
			 * );
			 **/
			HttpFile.download(MinecraftManifest.getManifestURL(getVersion().getId()), manifestFile);
		}

		// TODO SET VERIFING VERSION FILE
		File versionFile = MinecraftManifest.getVersionFile(minecraft, getVersionName());

		if (!versionFile.exists())
		{
			// TODO SET SUBMITTED VERSION FILE DOWNLOAD
			/**
			 * COMMENTED FOR DEV TESTING. if(!minecraft.isOnline()) throw new
			 * OnlineRequiredException
			 * ("Version file must be downloaded with a valid online session!");
			 **/
			verifier.getDownloader().submit(MinecraftManifest.getVersionURL(getVersion().getId()), versionFile);
		}

		return MinecraftManifest.readFrom(manifestFile);
	}

	@Override
	protected String getVersionName()
	{
		return getVersion().getId();
	}
}
