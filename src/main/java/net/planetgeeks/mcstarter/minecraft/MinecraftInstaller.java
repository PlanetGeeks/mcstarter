package net.planetgeeks.mcstarter.minecraft;

import static net.planetgeeks.mcstarter.util.LogUtils.log;

import java.io.IOException;
import java.util.concurrent.Executors;

import lombok.Getter;
import net.planetgeeks.mcstarter.app.install.AppInstaller;
import net.planetgeeks.mcstarter.app.install.OnlineRequiredException;
import net.planetgeeks.mcstarter.http.HttpDownloader;
import net.planetgeeks.mcstarter.util.Defaults;

public class MinecraftInstaller extends AppInstaller<Minecraft>
{
	@Getter
	private final HttpDownloader downloader;

	protected MinecraftInstaller(Minecraft app)
	{
		super(app);

		this.downloader = new HttpDownloader();
		this.downloader.setExecutor(Executors.newFixedThreadPool(5));
	}

	@Override
	public MinecraftInstaller call() throws InterruptedException, IOException, OnlineRequiredException, Exception
	{
		MinecraftProfile profile = getApp().getProfile();

		if (profile == null)
			throw new IllegalStateException(Defaults.getString("minecraft.installer.noprofile"));

		MinecraftVerifier verifier = new MinecraftVerifier(this);

		verifier.call();

		log.info(Defaults.getString("minecraft.installer.start.download"));

		getDownloader().call();

		return this;
	}
}
