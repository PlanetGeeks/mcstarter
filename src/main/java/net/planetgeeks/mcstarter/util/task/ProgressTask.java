package net.planetgeeks.mcstarter.util.task;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * An extended {@link #Task} whose progress can be monitored.
 
 * @author Flood2d
 */
@Getter
public abstract class ProgressTask<T> extends Task<T> implements ProgressView
{
	@Setter(AccessLevel.PROTECTED)
	private double progress;
	
	@Override
    public double getProgress(int scale)
    {
    	return getProgress() * scale;
    }
}

