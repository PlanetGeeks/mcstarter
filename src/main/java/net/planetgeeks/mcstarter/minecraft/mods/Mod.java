package net.planetgeeks.mcstarter.minecraft.mods;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NonNull;
import net.planetgeeks.mcstarter.util.Checksum;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFilter("Mod")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Mod
{
	private @NonNull String id;
	private @NonNull String name;
	private String url;
	private boolean active = true;
	private boolean core = false;
	private List<String> dependencies = new ArrayList<>();
	private Checksum checksum;
	private LoaderType loader = LoaderType.FORGE;

	@JsonCreator
	public Mod(@JsonProperty("id") @NonNull String id,
			@JsonProperty("name") @NonNull String name)
	{
		this.id = id;
		this.name = name;
	}

	@JsonIgnore
	public File getFile(@NonNull File modsDir)
	{
		return new File(modsDir, id + ".jar");
	}

	public static FilterProvider addFilters(@NonNull SimpleFilterProvider provider)
	{
		provider.addFilter("Mod", new PropertyFilter());

		return Checksum.addFilters(provider);
	}

	public static class PropertyFilter extends SimpleBeanPropertyFilter
	{
		@Override
		public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, BeanPropertyWriter writer) throws Exception
		{
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
					serialize = !mod.getDependencies().isEmpty();
					break;
				case "checksum":
					serialize = mod.getChecksum().isValid();
					break;
				case "loader":
					serialize = mod.getLoader() != LoaderType.FORGE;
					break;
				default:
					serialize = true;
			}

			if (serialize)
				writer.serializeAsField(pojo, jgen, provider);
		}
	}
}
