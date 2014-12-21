package net.planetgeeks.mcstarter.minecraft;

import java.io.IOException;

import lombok.Getter;
import lombok.NonNull;
import net.planetgeeks.mcstarter.app.AppProfile;
import net.planetgeeks.mcstarter.app.version.Version;
import net.planetgeeks.mcstarter.minecraft.mods.ModpackProfile;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.ObjectMapper;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type", defaultImpl= MinecraftProfile.class)
@JsonSubTypes({ @Type(value = VanillaProfile.class, name = "vanilla"), @Type(value = ModpackProfile.class, name = "modded"), @Type(value = MinecraftProfile.class, name = "unknown") })
@JsonIgnoreProperties(ignoreUnknown = true)
public class MinecraftProfile extends AppProfile<Minecraft>
{		
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
	public String getType()
	{
		return "unknown";
	} 
	
	@Override
	public Version getVersion(@NonNull Minecraft minecraft) throws IOException
	{
		return minecraft.getVersions().getVersion(getVersionId());
	}
	
    // private boolean useOptifine; This will be a future implementation.
	
	//public List<String> getLibraries();
}
