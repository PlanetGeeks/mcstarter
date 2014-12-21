package net.planetgeeks.mcstarter.minecraft.mods;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NonNull;
import net.planetgeeks.mcstarter.app.install.FileChecksum;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Mod
{
    private @NonNull String id;
    private @NonNull String name;
    private @NonNull String url;
    private @NonNull List<String> dependencies = new ArrayList<>();
    private boolean active = true;
    private boolean core = false;
    private FileChecksum checksum = new FileChecksum();
    
    @JsonCreator
    public Mod(@JsonProperty("id") @NonNull String id, @JsonProperty("name") @NonNull String name, @JsonProperty("url") @NonNull String url)
    {
    	this.id = id;
    	this.name = name;
    	this.url = url;
    }
    
    @JsonIgnore
    public File getSaveLocation(@NonNull File modsDir)
    {
    	return new File(modsDir, id + ".jar");
    }
}
