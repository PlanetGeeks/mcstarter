package net.planetgeeks.mcstarter.minecraft.session;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.planetgeeks.mcstarter.util.http.HttpRequest.HttpPostRequest;

/**
 * Contains Yggdrasil session information and provides different methods to
 * manage them.
 * <p>
 * Usage example:
 * 
 * <blockquote>
 * 
 * <pre>
 * OnlineSession session = new OnlineSession(&quot;hello@example.it&quot;).setPassword(&quot;password&quot;);
 * session.verify();
 * 
 * if (session.isValid())
 * 	System.out.println(session.getAccessToken());
 * </pre>
 * 
 * </blockquote>
 * 
 * @author Flood2d
 */
@Getter
public class OnlineSession implements Session
{
	private final String id;
	private String password;

	@Setter
	private String accessToken;
	@Setter
	private String clientToken;
	@Setter
	private List<Profile> availableProfiles;
	@Setter
	private Profile selectedProfile;

	/**
	 * Construct an Yggdrasil Session with the given id.
	 * 
	 * @param id - Email or username.
	 */
	public OnlineSession(@NonNull String id)
	{
		this.id = id;
	}

	/**
	 * Attempt to authenticate using {@link #authenticate()} if a password has
	 * been set or attempt to refresh current session using
	 * {@link #refresh()}.
	 * 
	 * If successful, this method will generate a new access token that will
	 * invalidate the latest session.
	 * 
	 * @throws IOException if some exception occurs performing the request.
	 * @throws InterruptedException if the thread executing this method has been
	 *             interrupted.
	 * @throws AuthException if something gone wrong during authentication.
	 */
	public synchronized void verify() throws IOException, InterruptedException, AuthException
	{
		if (password != null && !password.isEmpty())
			authenticate();
		else if (accessToken != null && clientToken != null)
			refresh();
		else
			throw new AuthException("Missing credentials/tokens! You must set a valid password or use a valid session.");
	}

	/**
	 * Attempt to authenticate on Yggdrasil server using {@link #getId()} and
	 * {@link #getPassword()} as credentials.
	 * 
	 * If successful, this method will generate a new access token that will
	 * invalidate the latest session.
	 * 
	 * @throws IOException if some exception occurs performing the request.
	 * @throws InterruptedException if the thread executing this method has been
	 *             interrupted.
	 * @throws AuthException if something gone wrong during authentication.
	 */
	public synchronized void authenticate() throws IOException, InterruptedException, AuthException
	{
		if (getPassword() == null || getPassword().isEmpty())
			throw new AuthException("Incomplete credentials! You must set a valid password before calling this method!");

		HttpPostRequest request = null;

		try
		{
			request = new HttpPostRequest(getEndpoint(YggdrasilEndPoint.AUTHENTICATE));
			request.setBodyJson(new AuthPayload(getId(), getPassword()));
			request.call();
			
			checkResponse(request, HttpURLConnection.HTTP_OK);

			System.out.println(request.getResponseContent().asUTF8String());
			AuthResponse response = request.getResponseContent().asJSONObject(AuthResponse.class);

			setAccessToken(response.getAccessToken());
			setClientToken(response.getClientToken());
			setAvailableProfiles(response.getAvailableProfiles());
			setSelectedProfile(response.getSelectedProfile());

			for (Profile profile : getAvailableProfiles())
				profile.setSession(this);
		}
		finally
		{
			if (request != null)
				request.close();
		}
	}

	@Override
	public boolean isValid()
	{
		return accessToken != null && clientToken != null && getSelectedProfile() != null;
	}

	/**
	 * Attempt to refresh an Yggdrasil session using {@link #getAccessToken()}
	 * and {@link #getClientToken()}.
	 * 
	 * If successful, this method will generate a new access token that will
	 * invalidate the latest session.
	 * 
	 * @throws IOException if some exception occurs performing the request.
	 * @throws InterruptedException if the thread executing this method has been
	 *             interrupted.
	 * @throws AuthException if something gone wrong during refresh.
	 */
	public synchronized void refresh() throws IOException, InterruptedException, AuthException
	{
		if (getAccessToken() == null || getClientToken() == null)
			throw new AuthException("Nothing to refresh! This session doesn't contain tokens to refresh.");

		HttpPostRequest request = null;

		try
		{
			request = new HttpPostRequest(getEndpoint(YggdrasilEndPoint.REFRESH));
			request.setBodyJson(new RefreshPayload(getAccessToken(), getClientToken()));
			request.call();

			checkResponse(request, HttpURLConnection.HTTP_OK);

			RefreshResponse response = request.getResponseContent().asJSONObject(RefreshResponse.class);

			setAccessToken(response.getAccessToken());
			setClientToken(response.getClientToken());

			Profile profile = response.getSelectedProfile();
			profile.setSession(this);

			setSelectedProfile(profile);
		}
		finally
		{
			if (request != null)
				request.close();
		}
	}

	/**
	 * Attempt to validate an Yggdrasil session using {@link #getAccessToken()}.
	 * 
	 * A successful execution of this method means that there's an
	 * active-session linked with the given accessToken.
	 * 
	 * @throws IOException if some exception occurs performing the request.
	 * @throws InterruptedException if the thread executing this method has been
	 *             interrupted.
	 * @throws AuthException if something gone wrong during validation.
	 */
	public synchronized void validate() throws AuthException, IOException, InterruptedException
	{
		if (getAccessToken() == null || getClientToken() == null)
			throw new AuthException("Nothing to validate! This session doens't contain tokens to validate.");

		HttpPostRequest request = null;

		try
		{
			request = new HttpPostRequest(getEndpoint(YggdrasilEndPoint.VALIDATE));
			request.setBodyJson(new ValidatePayload(getAccessToken()));
			request.call();

			checkResponse(request, HttpURLConnection.HTTP_NO_CONTENT);
		}
		finally
		{
			if (request != null)
				request.close();
		}
	}

	/**
	 * Attempt to signout an Yggdrasil session using {@link #getId()} and
	 * {@link #getPassword()} as credentials.
	 * 
	 * A successful execution of this method means that the accessToken linked
	 * to the given credentials has been invalidated.
	 * 
	 * @throws IOException if some exception occurs performing the request.
	 * @throws InterruptedException if the thread executing this method has been
	 *             interrupted.
	 * @throws AuthException if something gone wrong during signout.
	 */
	public synchronized void signout() throws AuthException, IOException, InterruptedException
	{
		if (getPassword() == null || getPassword().isEmpty())
			throw new AuthException("Incomplete credentials! You must set a valid password before calling this method!");

		HttpPostRequest request = null;

		try
		{
			request = new HttpPostRequest(getEndpoint(YggdrasilEndPoint.SIGNOUT));
			request.setBodyJson(new SignoutPayload(getId(), getPassword()));
			request.call();

			checkResponse(request, HttpURLConnection.HTTP_NO_CONTENT);
		}
		finally
		{
			if (request != null)
				request.close();
		}
	}

	/**
	 * Attempt to invalidate an Yggdrasil session using
	 * {@link #getAccessToken()} and {@link #getClientToken()} as credentials.
	 * 
	 * A successful execution of this method means that the given accessToken
	 * has been invalidated.
	 * 
	 * @throws IOException if some exception occurs performing the request.
	 * @throws InterruptedException if the thread executing this method has been
	 *             interrupted.
	 * @throws AuthException if something gone wrong during invalidation.
	 */
	public synchronized void invalidate() throws AuthException, IOException, InterruptedException
	{
		if (getAccessToken() == null || getClientToken() == null)
			throw new AuthException("Nothing to invalidate! This session doens't contain tokens to invalidate.");

		HttpPostRequest request = null;

		try
		{
			request = new HttpPostRequest(getEndpoint(YggdrasilEndPoint.INVALIDATE));
			request.setBodyJson(new InvalidatePayload(getAccessToken(), getClientToken()));
			request.call();

			checkResponse(request, HttpURLConnection.HTTP_NO_CONTENT);
		}
		finally
		{
			if (request != null)
				request.close();
		}
	}

	private void checkResponse(@NonNull HttpPostRequest request, @NonNull int... expectedCodes) throws IOException, InterruptedException, AuthException
	{
		for (int expected : expectedCodes)
			if(request.successful(expected))
				return;

		if (request.hasResponseContent())
			throw new AuthException(request.getResponseContent().asJSONObject(ErrorResponse.class));
		else
			throw new AuthException("No given details for this exception!");
	}

	/**
	 * Set session password. It mustn't be null or empty.
	 * 
	 * @param password - the password
	 * @return this object.
	 */
	public OnlineSession setPassword(@NonNull String password)
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
		private Profile selectedProfile;
		private List<Profile> availableProfiles;
		
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
	 * Signals that an exception of some sort has occurred during
	 * authentication.
	 * 
	 * @author Flood2d
	 */
	public static class AuthException extends Exception
	{
		private static final long serialVersionUID = 1L;

		public AuthException(ErrorResponse response)
		{
			super(String.format("%s: %s", response.getError(), response.getErrorMessage()));
		}

		public AuthException(String exception)
		{
			super(exception);
		}
	}
}
