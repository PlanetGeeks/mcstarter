package net.planetgeeks.mcstarter.util;

import java.util.Observable;

public abstract class ProgressView extends Observable
{
	private double progress;
	
	/**
	 * Get the progress.
	 * 
	 * @return a double value between 0.0D and 1.0D inclusive, or -1D if the progress can't be retrieved.
	 */
    public double getProgress()
    {
    	return progress;
    }
    
    /**
	 * Get the progress scaled.
	 * 
	 * @param scale - the scale.
	 * @return the result of the multiplication between {@link #getProgress()} and the given scale value.
	 */
    public double getProgress(double scale)
    {
    	return progress * scale;
    }

    /**
     * Set the progress.
     * 
     * @param progress - a double value between 0.0D and 1.0D, inclusive.
     */
    protected void setProgress(double progress)
    {
    	this.progress = progress;
    	
    	this.notifyObservers();
    }
}
