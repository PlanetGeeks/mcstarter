package net.planetgeeks.mcstarter.app;

import java.io.File;
import java.io.IOException;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.planetgeeks.mcstarter.app.install.AppInstaller;
import net.planetgeeks.mcstarter.app.version.ReleaseVersion;
import net.planetgeeks.mcstarter.app.version.Version;
import net.planetgeeks.mcstarter.app.version.VersionsContainer;

public abstract class App
{
	@Getter
	@Setter
	private File applicationDir;
	
    public abstract VersionsContainer getVersions() throws IOException;
    
    public abstract AppProfile getProfile();
    
    public abstract AppInstaller<?> getInstaller();
    
    public abstract VersionsContainer updateVersions() throws IOException;
    
    public Version getLatestVersion() throws IOException
    {
    	return getVersions().getLatestSnapshot();
    }
    
    public ReleaseVersion getLatestRelease() throws IOException
    {
    	return getVersions().getLatestRelease();    
    }
    
    public Version getVersion(@NonNull String versionId) throws IOException
    {
    	return getVersions().getVersion(versionId);
    }
    
    public Version getSelectedVersion() throws IOException
    {
    	if (getProfile() == null)
			throw new IllegalStateException("No application profile set!");
    	
    	return getProfile().getVersion(this);
    }
    
    public File getProfilesDir()
	{
		return getSubDir("profiles");
	}
    
	protected File getSubDir(@NonNull String name)
	{
		return new File(getApplicationDir(), name);
	}
}
