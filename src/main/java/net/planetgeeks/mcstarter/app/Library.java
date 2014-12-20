package net.planetgeeks.mcstarter.app;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class Library
{
	@Getter
	@Setter
	private String name;
	@Getter
	@Setter
	private LibraryNatives natives;
	@Getter
	@Setter
	private List<LibraryRule> rules;
	@Getter
	@Setter
	private LibraryExtract extract;

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
