package net.planetgeeks.mcstarter.session;

import java.util.List;

/**
 * This interface represents a user login instance.
 * 
 * @author Flood2d
 */
public interface Session
{
	/**
	 * Get session access token that will be used for user identification.
	 *
	 * @return access token or <code>null</code>
	 */
    public String getAccessToken();
    
    /**
	 * Get session client token.
	 * 
	 * @return client token or <code>null</code>
	 */
    public String getClientToken();
    
    /**
     * Get the selectedProfile linked to this session.
     * 
     * @return a {@link #Profile} object.
     */
    public Profile getSelectedProfile();
    
    /**
     * Get a list of available profiles linked to this session.
     * 
     * @return a {@link #List} of {@link #Profile} objects.
     */
    public List<Profile> getAvailableProfiles();
    
    /**
     * Check session status.
     * 
     * @return true if this session is ready to be used.
     */
    public boolean isValid();
}
