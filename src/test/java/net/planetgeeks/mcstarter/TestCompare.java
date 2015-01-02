package net.planetgeeks.mcstarter;

import java.io.File;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class TestCompare extends Test
{

	@Override
	public void test() throws Exception
	{
		ZipInputStream zis0 = new ZipInputStream(new FileInputStream(new File("temp\\1.5.2.jar")));
		
		ZipInputStream zis1 = new ZipInputStream(new FileInputStream(new File("temp\\1.5.2-Forge.jar")));
		
		ZipEntry entry0;
		ZipEntry entry1;
		while((entry0 = zis0.getNextEntry()) != null)
		{
			entry1 = zis1.getNextEntry();
			
			if(entry0.getSize() != entry1.getSize())
			{
				System.out.println(entry0.getSize() + " " + entry1.getSize());
			}
	
		}
		
		zis0.close();
		zis1.close();
	}

}
