package net.planetgeeks.mcstarter;

import java.io.File;

import net.planetgeeks.mcstarter.app.version.VersionsContainer;
import net.planetgeeks.mcstarter.minecraft.Minecraft;
import net.planetgeeks.mcstarter.minecraft.MinecraftProfile;
import net.planetgeeks.mcstarter.minecraft.mods.ModpackProfile;

public abstract class Test
{
	
	public static void main(String[] args) throws Exception
	{
		Minecraft minecraft = new Minecraft();

		VersionsContainer versions = minecraft.getVersions();

		System.out.println(versions.getVersion("1.8.1").getTime());

		ModpackProfile profile = new ModpackProfile();
		
		profile.setMinecraftVersion("1.8.1");
		
		MinecraftProfile.getMapper().writeValue(new File("map.json"), profile);
		
		ModpackProfile prof = (ModpackProfile) MinecraftProfile.getMapper().readValue(new File("map.json"), MinecraftProfile.class);

		System.out.println(prof.getType());
		
		/**
		 * Minecraft minecraft = new Minecraft();
		 * 
		 * MinecraftVersions versions = minecraft.getVersions();
		 * 
		 * minecraft.setVersion(versions.getLatestRelease());
		 * 
		 * M
		 * 
		 * minecraft.setSession(new OfflineSession("Flood2d"));
		 * 
		 * //TODO : set
		 * 
		 * System.out.println(minecraft.getVersion().getId());
		 **/
	}

	public abstract void test() throws Exception;
}
