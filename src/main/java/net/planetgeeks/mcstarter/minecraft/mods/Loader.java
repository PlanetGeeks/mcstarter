package net.planetgeeks.mcstarter.minecraft.mods;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import net.planetgeeks.mcstarter.minecraft.version.Version;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public abstract class Loader
{
	@JsonIgnore
	@Getter(AccessLevel.PROTECTED)
	private final Version version;

	@JsonCreator
	public Loader(@JsonProperty("id") @NonNull String id)
	{
		this.version = new Version(id);
	}
	
	public String getId()
	{
		return version.getId();
	}

	public abstract LoaderType getType();

	@JsonIgnore
	public String getVersionName(@NonNull Version mcVersion)
	{
		StringBuilder builder = new StringBuilder();

		builder.append(mcVersion);
		builder.append("-");
		builder.append(getType().getFilename());
		builder.append(getVersion());

		return builder.toString();
	}

	@JsonIgnore
	public String getVersionName(@NonNull Version mcVersion, @NonNull Version baseVersion)
	{
		StringBuilder builder = new StringBuilder();

		builder.append(getVersionName(mcVersion));
		builder.append("-");
		builder.append(baseVersion);

		return builder.toString();
	}
}