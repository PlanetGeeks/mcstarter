package net.planetgeeks.mcstarter.minecraft.version;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import lombok.NonNull;

/**
 * Represents a snapshot/development version.
 * 
 * @author Flood2d
 */
public class SnapshotVersion extends Version
{
	@JsonCreator
    public SnapshotVersion(@JsonProperty("id") @NonNull String id)
    {
    	super(id);
    }

	@Override
	@JsonIgnore
	public String getType()
	{
		return "snapshot";
	}
}
