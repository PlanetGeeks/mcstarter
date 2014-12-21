package net.planetgeeks.mcstarter.minecraft.mods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.planetgeeks.mcstarter.app.version.Version;
import net.planetgeeks.mcstarter.minecraft.MinecraftProfile;

import org.codehaus.jackson.annotate.JsonIgnore;

@Getter @Setter
public class ModpackProfile extends MinecraftProfile
{
	private List<Mod> mods = new ArrayList<>();
	
	@JsonIgnore
	public String getType()
	{
		return "modded";
	}

	@JsonIgnore
	public Version getForgeVersion() throws IOException
	{
		return null;
	}
}
