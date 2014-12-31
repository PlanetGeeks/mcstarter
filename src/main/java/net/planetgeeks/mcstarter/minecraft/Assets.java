package net.planetgeeks.mcstarter.minecraft;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class Assets
{
	@Getter
	private String version;
	
    public Assets(@NonNull MinecraftManifest manifest)
    {
    	this.version = manifest.getAssets() != null ? manifest.getAssets() : "legacy";
    }
    
    public String getIndexPath()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("indexes/");
		builder.append(getVersion());
		builder.append(".json");
		return builder.toString();
	}
    
    public URL getIndexURL(@NonNull URL baseURL) throws MalformedURLException
    {
    	return new URL(baseURL, getIndexPath());
    }
    
    public String getObjectPath(@NonNull AssetObject asset)
    {
    	StringBuilder builder = new StringBuilder();
    	builder.append("objects/");
    	builder.append(asset.getPath());
    	return builder.toString();
    }
    
    public URL getObjectURL(@NonNull URL baseURL, @NonNull AssetObject asset) throws MalformedURLException
    {
    	return new URL(baseURL, asset.getPath());
    }
    
    public boolean isLegacy()
    {
    	return version.equals("legacy");
    }
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AssetsIndex
    {
    	private boolean virtual = false;
    	private Map<String, AssetObject> objects;
    	
    	public static AssetsIndex readFrom(@NonNull File file) throws JsonParseException, JsonMappingException, IOException
    	{
    		return new ObjectMapper().readValue(file, AssetsIndex.class);
    	}
    }
    
    @Data
    public static class AssetObject
    {
    	private String hash;
    	private int size;
    	
    	@JsonIgnore
    	public String getPath()
    	{
    		StringBuilder builder = new StringBuilder();
        	builder.append(getHash().substring(0, 2) + "/");
        	builder.append(getHash());
        	return builder.toString();
    	}
    }
}
