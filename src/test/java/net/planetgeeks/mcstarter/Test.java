package net.planetgeeks.mcstarter;

import net.planetgeeks.mcstarter.minecraft.Minecraft;
import net.planetgeeks.mcstarter.minecraft.VanillaProfile;
import net.planetgeeks.mcstarter.minecraft.session.OfflineSession;
import net.planetgeeks.mcstarter.util.Platform;

public abstract class Test
{
	public static void main(String[] args) throws Exception
	{
		//CREATE THE APPLICATION
		Minecraft minecraft = new Minecraft();

		//SET THE APPLICATION DIR
		minecraft.setApplicationDir(Platform.getPlatform().getAppDirectory("appProva"));
		
		//SET A SESSION
		minecraft.setSession(new OfflineSession());

		//CREATE A MINECRAFT APPLICATION PROFILE
		VanillaProfile profile = new VanillaProfile("vanilla 1.7.2");
		
		//SET MINECRAFT VERSION TO 1.7.2
		profile.setMinecraftVersion("1.7.2");

		//SET THE PROFILE INTO THE APPLICATION.
		minecraft.setProfile(profile);
		
		//INSTALL THE APPLICATION. IT WILL VERIFY ALL THE APPLICATION FILES AND WILL ALSO ATTEMPT TO DOWNLOAD MISSING FILES.
		minecraft.install();
	}

	public abstract void test() throws Exception;
}
