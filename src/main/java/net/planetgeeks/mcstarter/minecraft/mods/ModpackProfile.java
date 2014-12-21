package net.planetgeeks.mcstarter.minecraft.mods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.planetgeeks.mcstarter.app.version.Version;
import net.planetgeeks.mcstarter.minecraft.MinecraftProfile;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

@JsonFilter("ModpackProfile")
public class ModpackProfile extends MinecraftProfile
{
	static
	{
		getMapper().setFilters(addFilters(new SimpleFilterProvider()));
	}

	@Getter
	@Setter
	private List<Mod> mods = new ArrayList<>();

	@JsonIgnore
	public String getType()
	{
		return "modded";
	}

	@JsonIgnore
	public Version getForgeVersion() throws IOException
	{
		//TODO : retrieve from minecraft getVersion();
		return null;
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
			if (!writer.getName().equals("mods"))
			{
				writer.serializeAsField(pojo, jgen, provider);
				return;
			}

			ModpackProfile modpack = (ModpackProfile) pojo;

			boolean serialize = false;

			switch (writer.getName())
			{
				case "mods":
					serialize = modpack.getMods() != null && !modpack.getMods().isEmpty();
					break;
			}
			
			if(serialize)
				writer.serializeAsField(pojo, jgen, provider);
		}
	}
}
