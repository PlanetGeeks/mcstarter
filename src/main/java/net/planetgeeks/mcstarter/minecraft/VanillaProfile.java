package net.planetgeeks.mcstarter.minecraft;

import org.codehaus.jackson.annotate.JsonIgnore;

public class VanillaProfile extends MinecraftProfile
{
	@JsonIgnore
	public String getType()
	{
		return "vanilla";
	} 
}
