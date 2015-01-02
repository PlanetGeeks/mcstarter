package net.planetgeeks.mcstarter;

import java.io.File;
import java.util.ArrayList;

import net.planetgeeks.mcstarter.minecraft.Minecraft;
import net.planetgeeks.mcstarter.minecraft.VanillaProfile;
import net.planetgeeks.mcstarter.minecraft.session.OfflineSession;
import net.planetgeeks.mcstarter.minecraft.version.Version;
import net.planetgeeks.mcstarter.util.Zip;

public abstract class Test
{
	public static void main(String[] args) throws Exception
	{	
        Zip zip = new Zip(new File("temp\\1.5.2.jar"));
        
        ArrayList<String> entries = new ArrayList<String>();
        entries.add("META_INF");
        
        zip.copyTo(new File("temp\\1.5.2-copied.jar"), entries);
        zip.close();
		
		//CREATE THE APPLICATION
		Minecraft minecraft = new Minecraft();

		//SET THE APPLICATION DIR
		minecraft.setApplicationDir(/**Platform.getPlatform().getAppDirectory("appProva")**/ new File("temp" + File.separator + "appProva"));
		
		//SET A SESSION
		minecraft.setSession(new OfflineSession());

		//CREATE A MINECRAFT APPLICATION PROFILE
		VanillaProfile profile = new VanillaProfile("vanilla 1.7.2");
		
		//SET MINECRAFT VERSION TO 1.7.2
		profile.setVersion(new Version("1.7.2"));

		//SET THE PROFILE INTO THE APPLICATION.
		minecraft.setProfile(profile);
		
		//INSTALL THE APPLICATION. IT WILL VERIFY ALL THE APPLICATION FILES AND WILL ALSO ATTEMPT TO DOWNLOAD MISSING FILES.
		minecraft.install();
	}

	public abstract void test() throws Exception;
}
