package net.planetgeeks.mcstarter.minecraft.mods.forge;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import lombok.Getter;
import lombok.NonNull;
import net.planetgeeks.mcstarter.http.HttpDownloader;
import net.planetgeeks.mcstarter.minecraft.MinecraftVerifier;
import net.planetgeeks.mcstarter.task.Task;
import net.planetgeeks.mcstarter.util.ProgressView;

public class ForgeManifestInstaller extends ProgressView implements Task<ForgeManifestInstaller>, Observer
{
	private static final String UNIVERSAL_PATH = "http://files.minecraftforge.net/maven/net/minecraftforge/forge/%1$s-%2$s/forge-%1$s-%2$s-universal.jar";
	
	@Getter
	private final Forge forge;
	@Getter
	private final MinecraftVerifier verifier;
	
	public ForgeManifestInstaller(@NonNull MinecraftVerifier verifier, @NonNull Forge forge)
	{
		this.forge = forge;
		this.verifier = verifier;
	}
	 
	@Override
	public ForgeManifestInstaller call() throws Exception
	{
		String mcVersion = getVerifier().getApp().getProfile().getMinecraftVersion();
		String forgeId = getForge().getId();
		
		HttpDownloader downloader = new HttpDownloader();
		
		downloader.addObserver(this);
		
	//	downloader.submit(new URL(String.format(UNIVERSAL_PATH, mcVersion, forgeId), );
		
		return null;
	}

	@Override
	public void checkStatus() throws InterruptedException
	{
		if(Thread.interrupted())
			throw new InterruptedException("Task interrupted");
	}

	@Override
	public void update(Observable o, Object arg)
	{
		if(o instanceof HttpDownloader)
		{
			 //UPDATE OBSERVER	
		}
	}
}
