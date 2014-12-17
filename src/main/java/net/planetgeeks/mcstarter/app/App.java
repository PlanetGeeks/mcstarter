package net.planetgeeks.mcstarter.app;

import java.io.IOException;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an application that can be installed, updated and launched.
 * 
 * @author Flood2d
 */
public abstract class App
{
	@Getter @Setter
    private Version version;

	/**
	 * Get the latest application release, including snapshot releases.
	 * 
	 * @return the latest application {@link #Version} available.
	 * 
	 * @throws IOException if an exeception occurs retriving the version.
	 */
	public abstract Version getLatestVersion() throws IOException;
	
	/**
	 * Get the latest application stable release.
	 * 
	 * @return the latest application stable {@link #ReleaseVersion} available.
	 * 
	 * @throws IOException if an exeception occurs retriving the version.
	 */
	public abstract ReleaseVersion getLatestRelease() throws IOException;
	
	/**
	 * Launch the application
	 */
	public abstract void launch();
}
