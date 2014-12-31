package net.planetgeeks.mcstarter.minecraft.mods;

import org.codehaus.jackson.annotate.JsonValue;

public enum LoaderType
{
	FORGE,
	LITELOADER;

	@JsonValue
	public String getName()
	{
		return name().toLowerCase();
	}
}
