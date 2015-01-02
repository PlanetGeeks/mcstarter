package net.planetgeeks.mcstarter.minecraft.version;

import java.util.Date;
import java.util.regex.Pattern;

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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type", defaultImpl = Version.class)
@JsonSubTypes({ @Type(value = ReleaseVersion.class, name = "release"), @Type(value = SnapshotVersion.class, name = "snapshot"), @Type(value = Version.class, name = "version") })
@JsonIgnoreProperties(ignoreUnknown = true)
public class Version implements Comparable<Version>
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

	@Override
	public int compareTo(Version version)
	{
		String[] versionPeriods = version.getId().split(Pattern.quote(".")), currentPeriods = getId().split(Pattern.quote("."));

		boolean current = versionPeriods.length < currentPeriods.length;

		for (int i = 0; i < (current ? currentPeriods.length : versionPeriods.length); i++)
		{
			int versionPeriod = 0, currentPeriod = 0;

			try
			{
				versionPeriod = i < versionPeriods.length ? Integer.valueOf(versionPeriods[i]) : 0;

				currentPeriod = i < currentPeriods.length ? Integer.valueOf(currentPeriods[i]) : 0;
			}
			catch (NumberFormatException e)
			{
				break;
			}

			if (currentPeriod > versionPeriod)
				return 1;

			if (currentPeriod < versionPeriod)
				return -1;
		}

		return 0;
	}
	
	@Override
	public String toString()
	{
		return getId();
	}
}
