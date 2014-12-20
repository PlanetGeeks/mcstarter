package net.planetgeeks.mcstarter.minecraft;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.planetgeeks.mcstarter.app.AppLibrary;

public class MinecraftLibrary extends AppLibrary
{
	@Getter @Setter
    private String name;
	@Getter @Setter
    private LibraryNatives natives;
	@Getter @Setter
    private List<LibraryRules> rules;
	@Getter @Setter
    private LibraryExtract extract;

    @Data
    private static class LibraryNatives
    {
    	private String linux;
    	private String windows;
    	private String osx;
    }
    
    @Data
    private static class LibraryRules
    {
    	private String action;
    	private LibraryOs os;
    }
    
    @Data
    private static class LibraryExtract
    {
    	private List<String> exclude;
    }
    
    @Data
    private static class LibraryOs
    {
    	private String name;
    	private String version;
    }
}