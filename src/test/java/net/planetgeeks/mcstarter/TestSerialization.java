package net.planetgeeks.mcstarter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import net.planetgeeks.mcstarter.app.version.ReleaseVersion;
import net.planetgeeks.mcstarter.app.version.Version;

import org.codehaus.jackson.map.ObjectMapper;

public class TestSerialization extends Test
{
	@Override
	public void test()
	{
		Version rel = new ReleaseVersion("hello");

		ObjectMapper mapper = new ObjectMapper();
		
		File file = new File("test.json");
		
		try(FileOutputStream out = new FileOutputStream(file))
		{
			mapper.writeValue(out, rel);	
			
			ReleaseVersion version = (ReleaseVersion) mapper.readValue(file, Version.class);
			
			System.out.println(version.getType());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
