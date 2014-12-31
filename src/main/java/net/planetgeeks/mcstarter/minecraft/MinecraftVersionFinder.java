package net.planetgeeks.mcstarter.minecraft;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import net.planetgeeks.mcstarter.app.version.Version;
import net.planetgeeks.mcstarter.task.Task;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class MinecraftVersionFinder implements Task<List<Version>>
{
	@Getter
	private Minecraft app;
	@Getter
	private ObjectMapper mapper = new ObjectMapper();

	public MinecraftVersionFinder(@NonNull Minecraft app)
	{
		this.app = app;
	}

	@Override
	public List<Version> call() throws InterruptedException
	{
		List<Version> installed = new ArrayList<>();

		File versionsDir = getApp().getVersionsDir();

		if (versionsDir.exists() && versionsDir.isDirectory())
			for (File dir : versionsDir.listFiles())
				installed.addAll(readDir(dir));

		return installed;
	}

	private List<Version> readDir(@NonNull File dir) throws InterruptedException
	{
		List<Version> versions = new ArrayList<>();

		checkStatus();

		if (dir.isDirectory())
			try
			{
				versions.add(readManifest(dir));
			}
			catch (IOException e)
			{
			}

		return versions;
	}

	private Version readManifest(@NonNull File manifestDir) throws JsonParseException, JsonMappingException, IOException
	{
		File manifest = new File(manifestDir, manifestDir.getName() + ".json");

		if (manifest.exists() && manifest.isFile())
			return getMapper().readValue(manifest, Version.class);

		throw new FileNotFoundException("Manifest file not found");
	}

	@Override
	public void checkStatus() throws InterruptedException
	{
		if (Thread.interrupted())
			throw new InterruptedException("Task interrupted");
	}

}
