package net.planetgeeks.mcstarter.util.task;

import lombok.Getter;
import lombok.Setter;

public abstract class ProgressTask<T> extends Task<T> implements ProgressView
{
	@Getter @Setter
	private double progress;
	
	@Override
    public double getProgress(int scale)
    {
    	return getProgress() * scale;
    }
}

