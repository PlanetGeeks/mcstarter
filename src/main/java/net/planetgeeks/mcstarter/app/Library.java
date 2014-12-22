package net.planetgeeks.mcstarter.app;

import java.util.List;

import lombok.Data;
import lombok.NonNull;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Library
{
	private final String name;
	private LibraryNatives natives;
	private List<LibraryRule> rules;
	private LibraryExtract extract;

	@JsonCreator
	public Library(@JsonProperty("name") @NonNull String name)
	{
		this.name = name;
	}
	
	@Data
	private static class LibraryNatives
	{
		private String linux;
		private String windows;
		private String osx;
	}

	@Data
	private static class LibraryRule
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
