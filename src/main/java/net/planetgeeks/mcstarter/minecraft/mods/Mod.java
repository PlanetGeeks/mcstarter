package net.planetgeeks.mcstarter.minecraft.mods;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NonNull;
import net.planetgeeks.mcstarter.app.install.FileChecksum;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFilter("Mod")
public class Mod
{
	private @NonNull String id;
	private @NonNull String name;
	private @NonNull String url;
	private boolean active = true;
	private boolean core = false;
	private @NonNull List<String> dependencies = new ArrayList<>();
	private FileChecksum checksum = new FileChecksum();

	@JsonCreator
	public Mod(@JsonProperty("id") @NonNull String id,
			@JsonProperty("name") @NonNull String name,
			@JsonProperty("url") @NonNull String url)
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

	public static FilterProvider addFilters(@NonNull SimpleFilterProvider provider)
	{
        provider.addFilter("Mod", new PropertyFilter());

		return FileChecksum.addFilters(provider);
	}

	public static class PropertyFilter extends SimpleBeanPropertyFilter
	{
		@Override
		public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, BeanPropertyWriter writer) throws Exception
		{
			if (!writer.getName().equals("active") && !writer.getName().equals("core") && !writer.getName().equals("dependencies") && !writer.getName().equals("checksum"))
			{
				writer.serializeAsField(pojo, jgen, provider);
				return;
			}

			Mod mod = (Mod) pojo;

			boolean serialize = false;

			switch (writer.getName())
			{
				case "active":
					serialize = !mod.isActive();
					break;
				case "core":
					serialize = mod.isCore();
					break;
				case "dependencies":
					serialize = mod.getDependencies() != null && !mod.getDependencies().isEmpty();
					break;
				case "checksum":
					serialize = mod.getChecksum() != null && mod.getChecksum().isValid();
			}
			
			if(serialize)
				writer.serializeAsField(pojo, jgen, provider);
		}
	}
}
