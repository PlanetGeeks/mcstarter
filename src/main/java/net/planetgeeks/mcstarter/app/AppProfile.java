package net.planetgeeks.mcstarter.app;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.planetgeeks.mcstarter.app.version.Version;

import org.codehaus.jackson.annotate.JsonIgnore;

public abstract class AppProfile<T extends App<T>>
{
	@Getter @Setter(AccessLevel.PROTECTED)
	@JsonIgnore		
    private String versionId;

	@JsonIgnore
	public Version getVersion(T application) throws Exception
	{
		if(versionId == null)
			throw new IllegalStateException("No version set for this profile!");
		
		return application.getVersion(getVersionId());
	}
}
