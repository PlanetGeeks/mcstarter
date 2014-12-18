package net.planetgeeks.mcstarter.minecraft.session;

import lombok.Data;
import lombok.NonNull;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Contains minecraft profile information.
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
    
    @JsonCreator
    public Profile(@JsonProperty("id") @NonNull String id, @JsonProperty("name") @NonNull String name)
	{
		this.id = id;
		this.name = name;
	}
}
