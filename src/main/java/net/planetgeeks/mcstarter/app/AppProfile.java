package net.planetgeeks.mcstarter.app;

import java.io.IOException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.planetgeeks.mcstarter.app.version.Version;

import org.codehaus.jackson.annotate.JsonIgnore;

public abstract class AppProfile
{
	@Getter @Setter(AccessLevel.PROTECTED)
	@JsonIgnore		
    private String versionId;

	@JsonIgnore
	public Version getVersion(App application) throws IOException
	{
		if(versionId == null)
			throw new IllegalStateException("No version set for this profile!");
		
		return application.getVersion(getVersionId());
	}
}
