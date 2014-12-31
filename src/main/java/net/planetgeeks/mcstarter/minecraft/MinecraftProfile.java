package net.planetgeeks.mcstarter.minecraft;

import java.io.IOException;

import lombok.Getter;
import lombok.NonNull;
import net.planetgeeks.mcstarter.app.AppProfile;
import net.planetgeeks.mcstarter.app.install.OnlineRequiredException;
import net.planetgeeks.mcstarter.minecraft.mods.ModpackProfile;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.ObjectMapper;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type", defaultImpl= VanillaProfile.class)
@JsonSubTypes({ @Type(value = VanillaProfile.class, name = "vanilla"), @Type(value = ModpackProfile.class, name = "modded")})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class MinecraftProfile extends AppProfile
{		
	@JsonCreator
	public MinecraftProfile(@JsonProperty("id") @NonNull String id)
	{
		super(id);
	}

	@Getter
	private static ObjectMapper mapper = new ObjectMapper();
	
	public String getMinecraftVersion()
	{
		return getVersionId();
	}
	
	public void setMinecraftVersion(@NonNull String minecraftVersion)
	{
		setVersionId(minecraftVersion);
	}
	
	@JsonIgnore
	protected abstract MinecraftManifest retriveVersion(MinecraftVerifier verifier) throws InterruptedException, IOException,  OnlineRequiredException, Exception;
}
