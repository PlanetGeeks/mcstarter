package net.planetgeeks.mcstarter.minecraft.mods.forge;

import java.io.File;
import java.io.IOException;

import lombok.Data;
import lombok.NonNull;
import net.planetgeeks.mcstarter.minecraft.MinecraftManifest;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForgeInstallProfile
{
    private ForgeInstall install;
    private MinecraftManifest versionInfo;

    @JsonIgnore
    public MinecraftManifest getManifest()
    {
    	return versionInfo;
    }
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForgeInstall
    {
    	private String target;
        private String filePath;
        private String logo;
        private String welcome;
        private String version;
        private String path;
        private String profileName;
        private String minecraft;
        private String stripMeta;
    }
    
    public static MinecraftManifest getManifestFrom(@NonNull File file) throws JsonParseException, JsonMappingException, IOException
    {
    	ObjectMapper mapper = new ObjectMapper();
    	
    	return mapper.readValue(file, ForgeInstallProfile.class).getManifest();
    }
}
