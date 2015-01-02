package net.planetgeeks.mcstarter.minecraft;

/**
 * Thrown by tasks that need an online session.
 * 
 * @author Flood2d
 */
public class OnlineRequiredException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public OnlineRequiredException(String message)
	{
		super(message);
	}
}