package net.planetgeeks.mcstarter.minecraft.mods.forge;

import static net.planetgeeks.mcstarter.minecraft.mods.LoaderType.FORGE;
import lombok.NonNull;
import net.planetgeeks.mcstarter.minecraft.mods.Loader;
import net.planetgeeks.mcstarter.minecraft.mods.LoaderType;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class Forge extends Loader
{
	@JsonCreator
	public Forge(@JsonProperty("id") @NonNull String id)
	{
        super(id);
	}

	@Override
	public LoaderType getType()
	{
		return FORGE;
	}
	
	/**
	@JsonIgnore
	public ForgeInstallProfile getProfileFromInstaller() throws IOException
	{
		FileInputStream fis = null;
		ZipInputStream zis = null;
		try
		{
			fis = new FileInputStream(new File(String.format("%s-installer.jar", getId())));
			zis = new ZipInputStream(new BufferedInputStream(fis));

			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null)
			{
				if (!entry.getName().equals("install_profile.json"))
					continue;

				return ForgeInstallProfile.readFrom(zis);
			}
			
			throw new IllegalArgumentException("The installer doesn't contain a valid 'install_profile.json'!");
		}
		finally
		{
			if (fis != null)
				fis.close();
			if (zis != null)
				zis.close();
		}
	}
	**/
}
