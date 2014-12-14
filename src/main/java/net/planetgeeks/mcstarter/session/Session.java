package net.planetgeeks.mcstarter.session;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import net.planetgeeks.mcstarter.util.http.HttpRequest.HttpPostRequest;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Contains Yggdrasil session informations and provides different methods to manage them.
 * <ul>
 * <li> {@link #Session.getId()} return the id (email/username) linked to this session. It will be used during authentication or similiar requests.</li>
 * <li> {@link #Session.getAccessToken()} return the accessToken generated by Yggdrasil or resumed from a saved session.</li>
 * <li> {@link #Session.getClientToken()} return the clientToken.</li>
 * <li> {@link #Session.getAvailableProfiles()} return the list of profiles linked to this session.</li>
 * <li> {@link #Session.getSelectedProfile()} return the selected profile linked to this session.</li>
 * <li> {@link #Session.isValid()} return true if session informations are ready to be retrieved..</li>
 * </ul>
 * 
 * Usage example:
 * <pre>
 * Session session = new Session("hello@example.it").setPassword("password");
 * session.authenticate(); //this can thrown different exceptions.
 * 
 * if(session.isValid())
 *    System.out.println(session.getAccessToken()); //prints the generated accessToken.
 * 
 * </pre>
 * @author Flood2d
 */
@Getter
public class Session
{
	private final String id;

	@Setter
	private String accessToken;

	@Setter
	private String clientToken;

	private String password;

	@Setter
	private List<Profile> availableProfiles;

	@Setter
	private Profile selectedProfile;

	/**
	 * Construct an Yggdrasil Session with the given id.
	 * 
	 * @param id - Email or username.
	 */
	public Session(@NonNull String id)
	{
		this.id = id;
	}

	/**
	 * Attemp to authenticate on Yggdrasil server using {@link #getId()} and {@link #getPassword()} as credentials.
	 * 
	 * If successful, this method will generate a new access token that will invalidate the latest session.
	 * 
	 * @throws IOException if some exception occurs performing the request.
	 * @throws InterruptedException if the thread executing this method has been interrupted.
	 * @throws AuthException if something gone wrong during authentication.
	 */
	public synchronized void authenticate() throws IOException, InterruptedException, AuthException
	{
		HttpPostRequest request = null;

		try
		{
			request = new HttpPostRequest(getEndpoint(YggdrasilEndPoint.AUTHENTICATE));
			request.setBodyJson(new AuthPayload(getId(), getPassword()));
			request.perform();
			
			if(request.getResponseCode() != 200)
			{
				ErrorResponse response = request.getResponseContent().asJSONObject(ErrorResponse.class);
				throw new AuthException(response);
			}
			
			AuthResponse response = request.getResponseContent().asJSONObject(AuthResponse.class);
			
			setAccessToken(response.getAccessToken());
			setClientToken(response.getClientToken());
			setAvailableProfiles(response.getAvailableProfiles());
			setSelectedProfile(response.getSelectedProfile());
			
			for(Profile profile : getAvailableProfiles())
				profile.setSession(this);	
		}
		finally
		{
			if(request != null)
				request.close();
		}
	}
	
	public boolean isValid()
	{
		return true;
	}

	public synchronized void refresh()
	{

	}

	public synchronized boolean validate()
	{
		return false;
	}

	public synchronized boolean signout()
	{
		return false;
	}

	public synchronized boolean invalidate()
	{
		return false;
	}

	public Session setPassword(String password)
	{
		this.password = password;
		return this;
	}
	
	private static URL getEndpoint(YggdrasilEndPoint endpoint) throws MalformedURLException
	{
		return new URL("https://authserver.mojang.com/" + endpoint.getEndpoint());
	}

	@Data
	private static class ErrorResponse
	{
		private String error;
		private String errorMessage;
		private String cause;
	}

	@Data
	private static class MinecraftAgent
	{
		private final String name = "Minecraft";
		private final int version = 1;
	}

	@Data
	private static class AuthPayload
	{
		private final MinecraftAgent agent = new MinecraftAgent();
		private final String username;
		private final String password;
	}

	@Data
	private static class AuthResponse
	{
		private String accessToken;
		private String clientToken;
		private List<Profile> availableProfiles;
		private Profile selectedProfile;
	}

	@Data
	private static class RefreshPayload
	{
		private final String accessToken;
		private final String clientToken;
		private Profile selectedProfile;
	}

	@Data
	private static class RefreshResponse
	{
		private String accessToken;
		private String clientToken;
		private Profile selectedProfile;
	}

	@Data
	private static class ValidatePayload
	{
		private final String accessToken;
	}

	@Data
	private static class SignoutPayload
	{
		private final String username;
		private final String password;
	}

	@Data
	private static class InvalidatePayload
	{
		private final String accessToken;
		private final String clientToken;
	}
	
	protected static enum YggdrasilEndPoint
	{
		AUTHENTICATE("authenticate"),
		REFRESH("refresh"),
		VALIDATE("validate"),
		SIGNOUT("signout"),
		INVALIDATE("invalidate");
	
		@Getter
		String endpoint;
		
		YggdrasilEndPoint(String endpoint)
		{
			this.endpoint = endpoint;
		}
	}

	/**
	 * Signals that an exception of some sort has occurred during authentication.
	 * @author Flood2d
	 */
	public static class AuthException extends Exception
	{
		private static final long serialVersionUID = 1L;
		
		public AuthException(ErrorResponse response)
		{
		    super(String.format("%s: %s", response.getError(), response.getErrorMessage()));
		}
	}
}