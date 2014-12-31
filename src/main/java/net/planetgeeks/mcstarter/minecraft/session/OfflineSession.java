package net.planetgeeks.mcstarter.minecraft.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Contains a random/custom offline session information.
 * 
 * <p>
 * Usage example:
 * 
 * <blockquote>
 * 
 * <pre>
 * Session session = new OfflineSession("Flood2d");
 * 
 * Profile profile = session.getSelectedProfile();
 * 
 * System.out.println(profile.getName());
 * </pre>
 * 
 * </blockquote>
 * 
 * @author Flood2d
 */
public class OfflineSession implements Session
{
	@Setter @Getter
    private List<Profile> availableProfiles;
	
	/**
	 * Construct an OfflineSession with the given name.
	 * @param name - profile name
	 */
	public OfflineSession(@NonNull String name)
	{
		List<Profile> profiles = new ArrayList<Profile>();
		Profile profile = new Profile("id", name);
		profile.setSession(this);
		profiles.add(profile);
		setAvailableProfiles(profiles);
	}
	
	/**
	 * Construct an OfflineSession with a random profile's name.
	 */
	public OfflineSession()
	{
		this("Player" + (new Random().nextInt(900) + 100));
	}
	
	@Override
	public String getAccessToken()
	{
		return null;
	}

	@Override
	public String getClientToken()
	{
		return null;
	}

	@Override
	public Profile getSelectedProfile()
	{
		return getAvailableProfiles() != null && getAvailableProfiles().size() > 0 ? getAvailableProfiles().get(0) : null;
	}

	@Override
	public boolean isValid()
	{
		return true;
	}
}
