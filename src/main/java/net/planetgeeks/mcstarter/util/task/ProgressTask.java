package net.planetgeeks.mcstarter.util.task;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.planetgeeks.mcstarter.util.ProgressView;

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
    public double getProgress(double scale)
    {
    	return getProgress() * scale;
    }
}

