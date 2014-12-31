package net.planetgeeks.mcstarter.minecraft.mods;

import lombok.Getter;
import lombok.NonNull;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public abstract class Loader
{
	@Getter
	private final String id;
	
	@JsonCreator
	public Loader(@JsonProperty("id") @NonNull String id)
	{
        this.id = id;
	}
	
	public abstract LoaderType getType();
	
}