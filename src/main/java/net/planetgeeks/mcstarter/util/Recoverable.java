package net.planetgeeks.mcstarter.util;

public interface Recoverable
{
	/**
	 * Get the limit of attempts.
	 * 
	 * @return the attempts limit.
	 */
	int getAttemptLimit();
	
	/**
	 * Get the delay between the attempts in milliseconds.
	 * 
	 * @return the delay in milliseconds.
	 */
    long getAttemptDelay();
	
    /**
     * Check if the task can be recovered.
     * 
     * @return the task can be recovered.
     */
	boolean isRecoverable();
	
	/**
	 * Set the attempts limit.
	 * 
	 * @param the attempts limit.
	 */
	void setAttemptLimit(int limit);
	
	/**
	 * Set the delay between the attempts in milliseconds.
	 * 
	 * @param the delay in milliseconds.
	 */
	void setAttemptDelay(long delay);
	
	/**
	 * Enable/Disable recover functionality.
	 * 
	 * @param true to enable, false to disable.
	 */
	void setRecoverable(boolean recoverable);
	
}
