package net.planetgeeks.mcstarter.minecraft.mods.forge;

import static net.planetgeeks.mcstarter.minecraft.mods.LoaderType.FORGE;
import static net.planetgeeks.mcstarter.util.LogUtils.log;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import lombok.NonNull;
import net.planetgeeks.mcstarter.http.HttpFile;
import net.planetgeeks.mcstarter.minecraft.Minecraft;
import net.planetgeeks.mcstarter.minecraft.MinecraftManifest;
import net.planetgeeks.mcstarter.minecraft.mods.Loader;
import net.planetgeeks.mcstarter.minecraft.mods.LoaderType;
import net.planetgeeks.mcstarter.minecraft.version.Version;
import net.planetgeeks.mcstarter.util.Defaults;
import net.planetgeeks.mcstarter.util.Extractor;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class Forge extends Loader
{
	private static final Version MINOR_INSTALLER_VERSION = new Version("1.5.2");
	
	@JsonCreator
	public Forge(@JsonProperty("version") @NonNull String version)
	{
        super(version);
	}

	@Override
	public LoaderType getType()
	{
		return FORGE;
	}
	
	public MinecraftManifest retrieveManifest(@NonNull Minecraft minecraft) throws InterruptedException, ExecutionException, IOException
	{
		Version mcVersion = minecraft.getSelectedVersion();
		
		if(hasInstaller(mcVersion))
		{
			log.info(Defaults.getString("forge.manifest.installer.download"));
			
			URL installerURL = getInstallerURL(mcVersion);

			File parentDir = new File(minecraft.getVersionsDir(), getVersionName(mcVersion)), installerFile = new File(parentDir, getInstallerFilename(mcVersion));

			HttpFile.download(installerURL, installerFile);
			
			File installProfile = new File(parentDir, "install_profile.json");
			
			try(Extractor extractor = new Extractor(installerFile))
			{	
				log.info(Defaults.getString("forge.manifest.installer.extract", installProfile.getName(), installerFile.getAbsolutePath()));
				
				extractor.extractEntry(installProfile.getName(), installProfile);
				
				//EXTRACT TO LIBS minecraftforge-universal-{#getPathVersion()}.jar
			}
			
			MinecraftManifest manifest = ForgeInstallProfile.getManifestFrom(installProfile);
			
			log.info(Defaults.getString("forge.manifest.save"));
			
			manifest.saveTo(new File(parentDir, parentDir.getName() + ".json"));
			
			installProfile.delete();
			
			installerFile.delete();
			
			return manifest;
		}
		
		return null;
	}
	
	private URL getInstallerURL(@NonNull Version mcVersion) throws MalformedURLException
	{
		URL baseURL = Defaults.getUrl("minecraft.baseurl.forge");
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(getPathVersion(mcVersion));
		builder.append("/");
		builder.append(getInstallerFilename(mcVersion));
		
		return new URL(baseURL, builder.toString());
	}
	
	private String getInstallerFilename(@NonNull Version mcVersion)
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("forge-");
		builder.append(getPathVersion(mcVersion));
		builder.append("-installer.jar");
		
		return builder.toString();
	}
	
	private String getPathVersion(@NonNull Version mcVersion)
	{
		return String.format("%s-%s", mcVersion, getVersion());
	}
	
	private static boolean hasInstaller(@NonNull Version mcVersion)
	{
		return mcVersion.compareTo(MINOR_INSTALLER_VERSION) >= 0;
	}
}
