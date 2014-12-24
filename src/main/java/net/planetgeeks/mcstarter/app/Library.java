package net.planetgeeks.mcstarter.app;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import lombok.Data;
import lombok.NonNull;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
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
	
	@JsonIgnore
	public File getFile(@NonNull File parent)
	{
		String[] periods = name.split(Pattern.quote(":"));
		
		if(periods.length != 3)
			throw new IllegalStateException("Invalid library name!");
		
		return new File(parent, String.format("%1s%0s%2s%0s%2s-%3s.jar", File.separator, periods[0], periods[1], periods[2]));
	}
	
	@JsonIgnore
	public File getFileSha1(@NonNull File parent)
	{
		File file = getFile(parent);

		return new File(file.getParent() + file.getName() + ".sha");
	}
}
