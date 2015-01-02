package net.planetgeeks.mcstarter.minecraft.mods;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NonNull;
import net.planetgeeks.mcstarter.util.Checksum;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = Mod.Serializer.class)
public class Mod
{
	private final String id;
	private final String name;
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
	
	public static class Serializer extends JsonSerializer<Mod>
	{
		@Override
		public void serialize(Mod value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException
		{
			jgen.writeStartObject();
			jgen.writeStringField("id", value.getId());
			jgen.writeStringField("name", value.getName());
			if(value.getUrl() != null) jgen.writeStringField("url", value.getUrl());
			if(!value.isActive()) jgen.writeBooleanField("active", value.isActive());
			if(value.isCore()) jgen.writeBooleanField("core", value.isCore());
			if(!value.getDependencies().isEmpty()) jgen.writeObjectField("dependencies", value.getDependencies());
			if(value.getChecksum() != null && value.getChecksum().isValid()) jgen.writeObjectField("checksum", value.getChecksum());
			if(value.getLoader() != LoaderType.FORGE) jgen.writeObjectField("loader", value.getLoader());
			jgen.writeEndObject();
		}	
	}
}
