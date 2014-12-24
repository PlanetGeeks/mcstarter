package net.planetgeeks.mcstarter.minecraft;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import lombok.NonNull;
import net.planetgeeks.mcstarter.app.Library;
import net.planetgeeks.mcstarter.app.install.AppFile;
import net.planetgeeks.mcstarter.app.install.AppVerifier;
import net.planetgeeks.mcstarter.app.version.Version;
import net.planetgeeks.mcstarter.minecraft.MinecraftInstaller.OnlineRequiredException;
import net.planetgeeks.mcstarter.minecraft.MinecraftVersions.MinecraftVersion;
import net.planetgeeks.mcstarter.util.ProgressUpdater;
import net.planetgeeks.mcstarter.util.http.HttpDownloader;

public class MinecraftVerifier extends AppVerifier<Minecraft>
{
	public MinecraftVerifier(Minecraft app)
	{
		super(app);
	}

	@Override
	public List<AppFile> call() throws Exception
	{
		Version version = getApp().getSelectedVersion();

		checkVersion(version);
		// CONTROLLA SE C'E IL DOWNLOAD DELLA VERSIONE SCARICATA.

		return new ArrayList<AppFile>();
	}

	private List<AppFile> checkVersion(@NonNull Version version) throws IOException, OnlineRequiredException, InterruptedException, ExecutionException
	{
		List<AppFile> list = new ArrayList<>();
		
		AppFile json = new AppFile(Minecraft.getVersionJsonPath(getApp(), version));

		if (!json.verify())
		{
			if (!getApp().isOnline())
				throw new OnlineRequiredException("You must be logged with a premium Minecraft account.");

			json.setRemoteLocation(Minecraft.getVersionJsonURL(version));

			HttpDownloader downloader = json.download();

			ProgressUpdater updater = new ProgressUpdater();
			addSubTask(updater);
			updater.add(this);
			updater.call();

			downloader.awaitTermination();

			updater.interrupt();
			removeSubTask(updater);
		}
		
		AppFile jar = new AppFile(Minecraft.getVersionPath(getApp(), version));
		
		if(!jar.verify())
		{
			jar.setRemoteLocation(Minecraft.getVersionURL(version));
			list.add(jar);
		}
		
		MinecraftVersion mcversion = MinecraftVersion.readFrom(json.getFile());
		
		list.addAll(checkLibraries(mcversion.getLibraries()));
		
		return list;
	}
	
	public List<AppFile> checkLibraries(@NonNull List<Library> libraries)
	{
		List<AppFile> list = new ArrayList<>();
		
		for(Library library : libraries)
		{
			//if(library)
		}
		
		return list;
	}
	
	@Override
	public void updateProgress()
	{
		//SE STA SCARICANDO IL JSON, PRENDE L'HTTPDOWNLOADER, PRENDE IL PROGRESSO e fa scale(0.25D) dove 0.25D e' la percentuale che occupa nella task il controllo del json.
	}

	@Override
	public MinecraftInstaller getInstaller()
	{
		return getApp().getInstaller();
	}
}
