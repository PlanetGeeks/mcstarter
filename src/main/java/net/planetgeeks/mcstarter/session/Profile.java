package net.planetgeeks.mcstarter.session;

import lombok.Data;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Contains minecraft profile informations.
 * <ul>
 * <li> {@link #Profile.getId()} return the id (email/username) linked to this profile.</li>
 * <li> {@link #Profile.getName()} return the name of this profile.</li>
 * <li> {@link #Profile.getLegacy()} return true if its a legacy profile.</li>
 * <li> {@link #Profile.getSession()} return the session linked to this profile.</li>
 * </ul>
 * 
 * @author Flood2d
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true, value = { "session" })
public class Profile
{
    private final String id;
    private final String name;
    private boolean legacy;
    private Session session;
}
