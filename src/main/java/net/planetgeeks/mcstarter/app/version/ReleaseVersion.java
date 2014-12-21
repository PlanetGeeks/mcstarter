package net.planetgeeks.mcstarter.app.version;

import lombok.NonNull;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Represents a stable/release version.
 * 
 * @author Flood2d
 */
public class ReleaseVersion extends Version
{
	@JsonCreator
    public ReleaseVersion(@JsonProperty("id") @NonNull String id)
    {
    	super(id);
    }

	@Override
	@JsonIgnore
	public String getType()
	{
		return "release";
	}
}
