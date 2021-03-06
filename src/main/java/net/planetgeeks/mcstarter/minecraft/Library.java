package net.planetgeeks.mcstarter.minecraft;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.planetgeeks.mcstarter.util.Defaults;
import net.planetgeeks.mcstarter.util.Platform;
import net.planetgeeks.mcstarter.util.Platform.OS;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class Library
{
	@JsonIgnore
	private String pkg;
	@Getter
	@JsonIgnore
	private String artifact;
	@Getter
	@JsonIgnore
	private String version;
	@Getter
	@Setter
	private String url;
	@Getter
	@Setter
	private LibraryNatives natives;
	@Getter
	@Setter
	private List<LibraryRule> rules;
	@Getter
	@Setter
	private LibraryExtract extract;
	@Getter
	@Setter
	private boolean serverreq = false;
	@Getter
	@Setter
	private boolean clientreq = true;
	@Getter
	@Setter
	private List<String> checksums;

	public void setName(@NonNull String name) throws Exception
	{
		String[] periods = name.split(Pattern.quote(":"));
		if (periods.length != 3)
			throw new Exception("Invalid library name!");
		this.pkg = periods[0];
		this.artifact = periods[1];
		this.version = periods[2];
	}
	
	public String getName()
	{
		return String.format("%s:%s:%s", getPackage(), getArtifact(), getVersion());
	}

	@JsonIgnore
	public String getPackage()
	{
		return pkg;
	}

	/**
	 * @return true if this library is platform native.
	 */
	@JsonIgnore 
	public boolean isNative()
	{
		return natives != null;
	}

	/**
	 * Get the library file path.
	 * <p>
	 * Directory separator is always a backslash to the right '/'.
	 *
	 * @param platform - Destination platform
	 * @return the library file path.
	 */
	@JsonIgnore
	public String getPath(@NonNull Platform platform)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(getPackage().replace(".", "/") + "/");
		builder.append(getArtifact() + "/");
		builder.append(getVersion() + "/");
		builder.append(getFilename(platform));
		return builder.toString();
	}

	@JsonIgnore
	public String getPathSha1(@NonNull Platform platform)
	{
		return getPath(platform) + ".sha1";
	}

	/**
	 * Get the library file name.
	 *
	 * @param platform - Destination platform.
	 * @return the file name, including its extension.
	 */
	@JsonIgnore
	public String getFilename(@NonNull Platform platform)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(getArtifact() + "-" + getVersion());
		
		if (isNative())
			builder.append("-" + natives.getNative(platform));
		
		builder.append(".jar");
		return builder.toString();
	}

	/**
	 * Get remote location of the library file.
	 *
	 * @param platform - Destination platform.
	 * @return an {@link #URL} object.
	 *
	 * @throws MalformedURLException
	 */
	@JsonIgnore
	public URL getURL(@NonNull Platform platform) throws MalformedURLException
	{
		return new URL(getLibrariesURL(), getPath(platform));
	}

	/**
	 * Get remote location of the library checksum file.
	 *
	 * @param platform - Destination platform.
	 * @return an {@link #URL} object.
	 *
	 * @throws MalformedURLException
	 */
	@JsonIgnore
	public URL getURLSha1(@NonNull Platform platform) throws MalformedURLException
	{
		return new URL(getLibrariesURL(), getPathSha1(platform));
	}

	/**
	 * Check if this library is required by the application for the given
	 * platform.
	 *
	 * @param platform - The destination platform.
	 * @return true if this library must be retrieved by the given platform.
	 */
	@JsonIgnore
	public boolean isAllowed(@NonNull Platform platform)
	{
		if (rules == null || rules.isEmpty())
			return true;
		boolean allowed = true;
		for (LibraryRule rule : rules)
			allowed = rule.isAllowed(platform) ? allowed : false;
		return allowed;
	}

	/**
	 * @return libraries base url.
	 *
	 * @throws MalformedURLException
	 */
	@JsonIgnore
	public static URL getLibrariesURL() throws MalformedURLException
	{
		return Defaults.getUrl("minecraft.baseurl.libraries");
	}

	@Data
	private static class LibraryNatives
	{
		private String linux;
		private String windows;
		private String osx;

		public String getNative(@NonNull Platform platform)
		{
			String nativeString;
			switch (platform.getOs())
			{
				case WINDOWS:
					nativeString = windows;
					break;
				case MAC_OSX:
					nativeString = osx;
					break;
				default:
					nativeString = linux;
					break;
			}
			return nativeString.replace("${arch}", platform.getArch());
		}
	}

	@Data
	private static class LibraryRule
	{
		private String action;
		private LibraryOs os;

		@JsonIgnore
		public boolean isAllowed(@NonNull Platform platform)
		{
			boolean matchOs = true;
			if (os != null)
				matchOs = os.matches(platform);
			return (action.equals("allow") && matchOs) || (action.equals("disallow") && !matchOs);
		}
	}

	@Data
	private static class LibraryExtract
	{
		private List<String> exclude;
	}

	private static class LibraryOs
	{
		private OS os;
		@Getter
		@Setter
		private Pattern version;

		@JsonProperty("name")
		public OS getOs()
		{
			return os;
		}

		@JsonProperty("name")
		public void setOs(@NonNull String name)
		{
			switch (name.trim())
			{
				case "windows":
					os = OS.WINDOWS;
					break;
				case "osx":
					os = OS.MAC_OSX;
					break;
				default:
					os = OS.LINUX;
			}
		}

		@JsonIgnore
		public boolean matches(@NonNull Platform platform)
		{
			return matchesName(platform) && (getVersion() != null ? version.matcher(platform.getVersion()).matches() : true);
		}

		@JsonIgnore
		private boolean matchesName(@NonNull Platform platform)
		{
			OS os = platform.getOs() != OS.WINDOWS && platform.getOs() != OS.MAC_OSX ? OS.LINUX : platform.getOs();
			return getOs().equals(os);
		}
	}
}