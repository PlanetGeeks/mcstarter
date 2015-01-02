package net.planetgeeks.mcstarter.minecraft;

import java.io.IOException;

import lombok.Getter;
import lombok.NonNull;
import net.planetgeeks.mcstarter.minecraft.mods.ModpackProfile;
import net.planetgeeks.mcstarter.minecraft.version.Version;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.ObjectMapper;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type", defaultImpl = VanillaProfile.class)
@JsonSubTypes({ @Type(value = VanillaProfile.class, name = "vanilla"), @Type(value = ModpackProfile.class, name = "modded") })
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class MinecraftProfile
{
	@Getter
	private static ObjectMapper mapper = new ObjectMapper();

	@Getter
	private final String id;

	private Version version;

	@JsonCreator
	public MinecraftProfile(@JsonProperty("id") @NonNull String id)
	{
		this.id = id;
	}

	@JsonIgnore
	public Version getVersion()
	{
		return version;
	}

	@JsonIgnore
	public void setVersion(Version version)
	{
		this.version = version;
	}

	@JsonProperty("minecraftVersion")
	protected String _getVersion()
	{
		return this.version.getId();
	}

	@JsonProperty("minecraftVersion")
	protected void _setVersion(String versionId)
	{
		this.version = new Version(versionId);
	}

	@JsonIgnore
	protected abstract String getVersionName();

	@JsonIgnore
	protected abstract MinecraftManifest retriveVersion(MinecraftVerifier verifier) throws InterruptedException, IOException, OnlineRequiredException, Exception;
}
