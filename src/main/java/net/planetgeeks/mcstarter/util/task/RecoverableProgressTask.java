package net.planetgeeks.mcstarter.util.task;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an extended {@link #ProgressTask} that can attempt a recovery if a checked exception occurs.
 * 
 * @author Flood2d
 */
@Getter
public abstract class RecoverableProgressTask<T> extends ProgressTask<T>
{
	@Setter(AccessLevel.PROTECTED)
	private int attemptCounter = 0;
    @Setter
	private int attemptLimit = 2;
	@Setter
	private long attemptDelay = 10 * 1000L;
	@Setter
	private boolean recoverable = true;
	
	protected void incrementAttemptCounter()
	{
		attemptCounter++;
	}
	
	protected void resetAttemptCounter()
	{
		attemptCounter = 0;
	}
	
	/**
	 * Check if the task is currently attempting a recovery.
	 * 
	 * @return true if the task is attempting a recovery.
	 */
	public boolean isAttempting()
	{
		return attemptCounter > 0;
	}
}
