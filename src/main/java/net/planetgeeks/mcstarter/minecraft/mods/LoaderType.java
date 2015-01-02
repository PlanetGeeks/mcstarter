package net.planetgeeks.mcstarter.minecraft.mods;

import lombok.Getter;

import org.codehaus.jackson.annotate.JsonValue;

public enum LoaderType
{
	FORGE("Forge"),
	LITELOADER("LiteLoader");

	@Getter
	private String filename;
	
	LoaderType(String filename)
	{
	    this.filename = filename;	
	}
	
	@JsonValue
	public String getName()
	{
		return name().toLowerCase();
	}
}
