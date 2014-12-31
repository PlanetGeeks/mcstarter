package net.planetgeeks.mcstarter.minecraft.mods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.planetgeeks.mcstarter.app.install.OnlineRequiredException;
import net.planetgeeks.mcstarter.minecraft.MinecraftManifest;
import net.planetgeeks.mcstarter.minecraft.MinecraftProfile;
import net.planetgeeks.mcstarter.minecraft.MinecraftVerifier;
import net.planetgeeks.mcstarter.minecraft.mods.forge.Forge;

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

@Getter
@Setter
@JsonFilter("ModpackProfile")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ModpackProfile extends MinecraftProfile
{
	static
	{
		getMapper().setFilters(addFilters(new SimpleFilterProvider()));
	}

	private String url;
	private Forge forgeLoader;
	private LiteLoader liteLoader;
	private List<Mod> mods = new ArrayList<>();

	@JsonCreator
	public ModpackProfile(@JsonProperty("id") @NonNull String id)
	{
		super(id);
	}

	@JsonIgnore
	public String getType()
	{
		return "modded";
	}

	@Override
	protected MinecraftManifest retriveVersion(MinecraftVerifier verifier) throws InterruptedException, IOException, OnlineRequiredException, Exception
	{
		return null; //TODO
	}

	public static FilterProvider addFilters(@NonNull SimpleFilterProvider provider)
	{
		provider.addFilter("ModpackProfile", new PropertyFilter());

		return Mod.addFilters(provider);
	}

	public static class PropertyFilter extends SimpleBeanPropertyFilter
	{
		@Override
		public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, BeanPropertyWriter writer) throws Exception
		{
			ModpackProfile modpack = (ModpackProfile) pojo;

			boolean serialize = false;

			switch (writer.getName())
			{
				case "mods":
					serialize = !modpack.getMods().isEmpty();
					break;
				default:
					serialize = true;
			}

			if (serialize)
				writer.serializeAsField(pojo, jgen, provider);
		}
	}
}
