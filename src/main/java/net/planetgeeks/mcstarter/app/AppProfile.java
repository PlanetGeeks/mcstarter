package net.planetgeeks.mcstarter.app;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.planetgeeks.mcstarter.app.version.Version;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public abstract class AppProfile
{
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@JsonIgnore
	private String versionId;
	@Getter
	private final String id;
	@Setter
	private Version version;

	@JsonCreator
	public AppProfile(@JsonProperty("id") @NonNull String id)
	{
		this.id = id;
	}

	@JsonIgnore
	public Version getVersion()
	{
		if (version == null)
			if (versionId == null)
				throw new IllegalStateException("No version set for this profile!");
			else
				version = new Version(versionId);

		return version;
	}
}
