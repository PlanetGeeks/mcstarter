package net.planetgeeks.mcstarter.util;

import java.io.File;

import lombok.Getter;
import lombok.NonNull;

/**
 * Contains platform informations.
 * 
 * @author Flood2d
 */
public class Platform
{
	@Getter
	private final OS os;
	@Getter
	private final String arch;
	@Getter
	private final String version;

	private static File workDir;

	private Platform(@NonNull OS os)
	{
		this.os = os;
        this.arch = getArch(os);
        this.version = System.getProperty("os.version");
	}

	/**
	 * Get the application directory with the given name.
	 * <p>
	 * It will be:
	 * <ul>
	 * <li>Windows - a sub directory of %appdata%.</li>
	 * <li>MacOSX - a sub directory of Library/Application Support/.</li>
	 * </ul>Linux - a sub directory of %user_home%</li>
	 * 
	 * @param appName - the application directory name.
	 * @return a directory {@link #File}
	 */
	public File getAppDirectory(@NonNull String appName)
	{
		File workDir = getWorkingDirectory(this);

		switch (getOs())
		{
			case SOLARIS:
			case LINUX:
			case WINDOWS:
				return new File(workDir, "." + appName + File.separator);
			case MAC_OSX:
				return new File(workDir, appName);
			default:
				return new File(workDir, appName + File.separator);
		}
	}

	private static File getWorkingDirectory(@NonNull Platform platform)
	{
		return workDir == null ? (workDir = findWorkingDirectory(platform)) : workDir;
	}

	private static File findWorkingDirectory(@NonNull Platform platform)
	{
		String userHome = System.getProperty("user.home", ".");
		File workDir;

		switch (platform.getOs())
		{
			case SOLARIS:
			case LINUX:
				workDir = new File(userHome);
				break;
			case WINDOWS:
				String applicationData = System.getenv("APPDATA");
				workDir = new File(applicationData != null ? applicationData : userHome);
				break;
			case MAC_OSX:
				workDir = new File(userHome, "Library/Application Support/");
				break;
			default:
				workDir = new File(userHome);

		}

		return workDir;
	}

	/**
	 * Get current Platform.
	 * 
	 * @return a Platform
	 */
	public static Platform getPlatform()
	{
		return getPlatform(System.getProperty("os.name").toLowerCase());
	}

	/**
	 * Get a Platform using an OS name.
	 * 
	 * @param osName - OS name.
	 * @return a Platform.
	 */
	public static Platform getPlatform(@NonNull String osName)
	{
		return getPlatform(OS.getOs(osName));
	}

	/**
	 * Get a Platform using an {@link #OS} object.
	 * 
	 * @param os - OS
	 * @return a Platform
	 */
	public static Platform getPlatform(@NonNull OS os)
	{
		return new Platform(os);
	}
	
	/**
	 * Get current OS architecture.
	 * <p>
	 * OS must equals with the real current OS. 
	 * 
	 * @param os - OS
	 * @return a String containing 64 or 32.
	 */
	public static String getArch(OS os)
	{
		switch (os)
		{
			case WINDOWS:
				String arch = System.getenv("PROCESSOR_ARCHITECTURE");
				String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");
				return arch.endsWith("64") || (wow64Arch != null && wow64Arch.endsWith("64")) ? "64" : "32";
			default:
				return System.getProperty("os.arch").contains("64") ? "64" : "32";
		}
	}

	public static enum OS
	{
		WINDOWS,
		MAC_OSX,
		SOLARIS,
		SUN_OS,
		LINUX,
		UNKNOWN;

		/**
		 * Get OS by its name.
		 * @param osName
		 * @return an OS.
		 */
		public static OS getOs(@NonNull String osName)
		{
			osName = osName.toLowerCase();

			if (osName.contains("win"))
				return OS.WINDOWS;

			if (osName.contains("mac"))
				return OS.MAC_OSX;
			if (osName.contains("solaris"))
				return OS.SOLARIS;
			if (osName.contains("sunos"))
				return OS.SUN_OS;
			if (osName.contains("linux") || osName.contains("unix"))
				return OS.LINUX;

			return OS.UNKNOWN;
		}
	}
}
