package net.planetgeeks.mcstarter.app;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.planetgeeks.mcstarter.app.version.Version;

import org.codehaus.jackson.annotate.JsonIgnore;

public abstract class AppProfile<T extends App<?>>
{
	@Getter @Setter(AccessLevel.PROTECTED)
	@JsonIgnore		
    private String versionId;
	
	@JsonIgnore
	public abstract Version getVersion(T application) throws Exception;
}
