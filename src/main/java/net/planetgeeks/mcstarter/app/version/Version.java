package net.planetgeeks.mcstarter.app.version;

import java.util.Date;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Contains essentials version informations.
 * 
 * @author Flood2d
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type", defaultImpl= Version.class)
@JsonSubTypes({ @Type(value = ReleaseVersion.class, name = "release"), @Type(value = SnapshotVersion.class, name = "snapshot"), @Type(value = Version.class, name = "version") })
@JsonIgnoreProperties(ignoreUnknown = true)
public class Version
{
	@Getter
	private final String id;
	@Getter
	@Setter
	private Date time;
	@Getter
	@Setter
	private Date releaseTime;

	@JsonCreator
	public Version(@JsonProperty("id") @NonNull String id)
	{
		this.id = id;
	}

	/**
	 * Get version type.
	 * 
	 * @return a {@link #String} that identifies the version type.
	 */
	@JsonIgnore
	public String getType()
	{
		return "version";
	}
}
