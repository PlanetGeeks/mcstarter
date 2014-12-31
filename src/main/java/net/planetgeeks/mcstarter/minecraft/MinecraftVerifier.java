package net.planetgeeks.mcstarter.minecraft;

import static net.planetgeeks.mcstarter.util.LogUtils.log;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import lombok.NonNull;
import net.planetgeeks.mcstarter.app.install.AppVerifier;
import net.planetgeeks.mcstarter.app.install.OnlineRequiredException;
import net.planetgeeks.mcstarter.http.HttpDownloader;
import net.planetgeeks.mcstarter.http.HttpFile;
import net.planetgeeks.mcstarter.minecraft.Assets.AssetObject;
import net.planetgeeks.mcstarter.minecraft.Assets.AssetsIndex;
import net.planetgeeks.mcstarter.util.Checksum;
import net.planetgeeks.mcstarter.util.Defaults;
import net.planetgeeks.mcstarter.util.Platform;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

public class MinecraftVerifier extends AppVerifier<MinecraftInstaller>
{
	public MinecraftVerifier(@NonNull MinecraftInstaller installer)
	{
		super(installer);
	}

	@Override
	public MinecraftVerifier call() throws InterruptedException, IOException, Exception, OnlineRequiredException
	{
		Platform platform = getApp().getPlatform();

		MinecraftManifest manifest = verifyVersion();

		verifyLibraries(manifest, platform);
		
		verifyAssets(manifest);

		return this;
	}

	private MinecraftManifest verifyVersion() throws InterruptedException, IOException, Exception, OnlineRequiredException
	{
		log.info(Defaults.getString("minecraft.verifier.start.version"));

		Minecraft minecraft = getApp();

		MinecraftProfile profile = minecraft.getProfile();

		if (profile == null)
			throw new IllegalStateException(Defaults.getString("minecraft.installer.noprofile"));

		return profile.retriveVersion(this);
	}

	private void verifyLibraries(@NonNull MinecraftManifest manifest, @NonNull Platform platform) throws MalformedURLException
	{
		log.info(Defaults.getString("minecraft.verifier.start.libraries"));

		File libsDir = getApp().getLibrariesDir();
		
		for (Library library : manifest.getLibraries())
		{
			if(!library.isAllowed(platform))
				continue;
			
			File libFile = new File(libsDir, library.getPath(platform)), shaFile = new File(libsDir, library.getPathSha1(platform));
			
			try
			{
				if(libFile.exists() && Checksum.from(shaFile).compare(libFile))
					continue;
			}
			catch (Exception e)
			{	
			}
				
			log.info(Defaults.getString("minecraft.verifier.add.library"));
			
			getDownloader().submit(library.getURL(platform), libFile);
			getDownloader().submit(library.getURLSha1(platform), shaFile);
		}
	}
	
	private void verifyAssets(@NonNull MinecraftManifest manifest) throws InterruptedException, ExecutionException, JsonParseException, JsonMappingException, IOException
	{
		log.info(Defaults.getString("minecraft.verifier.start.assets"));
		
		URL baseURL = Defaults.getUrl("minecraft.baseurl.downloads");
		
		Assets assets = new Assets(manifest);
		
		File assetsDir = getApp().getAssetsDir();
		
		File indexFile = new File(assetsDir, assets.getIndexPath());
		
		if(!indexFile.exists())
			HttpFile.download(assets.getIndexURL(baseURL), indexFile);
		
		AssetsIndex index = AssetsIndex.readFrom(indexFile);
		
		baseURL = Defaults.getUrl("minecraft.baseurl.resources");
		
		for(Map.Entry<String, AssetObject> entry : index.getObjects().entrySet())
		{
			AssetObject object = entry.getValue();
			
			File objectFile = new File(assetsDir, assets.getObjectPath(object));
			
			if(objectFile.exists() && new Checksum(object.getHash(), object.getSize()).compare(objectFile))
				continue;
			
			log.info(Defaults.getString("minecraft.verifier.add.asset"));
			
			getDownloader().submit(assets.getObjectURL(baseURL, object), objectFile);
		}
	}

	public Minecraft getApp()
	{
		return getInstaller().getApp();
	}

	@Override
	public MinecraftInstaller getInstaller()
	{
		return super.getInstaller();
	}

	public HttpDownloader getDownloader()
	{
		return getInstaller().getDownloader();
	}
}