package net.planetgeeks.mcstarter.minecraft;

import java.util.ArrayList;
import java.util.List;

import net.planetgeeks.mcstarter.app.install.AppFile;
import net.planetgeeks.mcstarter.app.install.AppVerifier;
import net.planetgeeks.mcstarter.app.version.Version;

public class MinecraftVerifier extends AppVerifier<Minecraft>
{
	public MinecraftVerifier(Minecraft app)
	{
		super(app);
	}

	@Override
	public List<AppFile> call() throws Exception
	{
		 Version version = getApplication().getSelectedVersion();
		
		return new ArrayList<AppFile>();
	}
}
