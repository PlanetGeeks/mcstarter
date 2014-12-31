package net.planetgeeks.mcstarter.minecraft.mods;

import static net.planetgeeks.mcstarter.minecraft.mods.LoaderType.LITELOADER;
import lombok.NonNull;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class LiteLoader extends Loader
{
	@JsonCreator
	public LiteLoader(@JsonProperty("id") @NonNull String id)
	{
        super(id);
	}
	
	@Override
	public LoaderType getType()
	{
		return LITELOADER;
	}
}
