package net.planetgeeks.mcstarter.auth;

import java.util.List;

import lombok.Data;

public class Session
{

	public Session()
	{

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
	private static class InvalidatePayload
	{
		private final String accessToken;
		private final String clientToken;
	}
}
