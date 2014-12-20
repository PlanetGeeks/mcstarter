package net.planetgeeks.mcstarter.util.task.recover;

import net.planetgeeks.mcstarter.util.task.ProgressView;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * An extended {@link #ProgressTask} that can attempt a recovery if a checked exception occurs.
 * 
 * @author Flood2d
 */
@Getter
public abstract class RecoverableProgressTask<T> extends RecoverableTask<T> implements ProgressView
{
	@Setter(AccessLevel.PROTECTED)
	private double progress;

	@Override
    public double getProgress(int scale)
    {
    	return getProgress() * scale;
    }
}
