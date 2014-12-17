package net.planetgeeks.mcstarter;

import java.io.IOException;

import net.planetgeeks.mcstarter.minecraft.MinecraftVersions;

public abstract class Test
{
    public static void main(String[]args) throws IOException
    {
    //	new TestSerialization().test();
    	
    	
    	MinecraftVersions versions = MinecraftVersions.retrive();
    	
    	System.out.println(versions.getVersion("1.8.1").getReleaseTime());
    	
    	System.out.println(versions.getLatestRelease().getId());
    }
      
    public abstract void test();
}
