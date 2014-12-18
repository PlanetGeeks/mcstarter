package net.planetgeeks.mcstarter.util.task;

/**
 * Represents an object that can be monitored.
 * 
 * @author Flood2d
 */
public interface ProgressView
{
	/**
	 * Get the progress.
	 * 
	 * @return a double value between 0.0D and 1.0D, inclusive.
	 */
    double getProgress();
    
    /**
	 * Get the progress scaled.
	 * 
	 * @param scale - the scale.
	 * @return the result of the multiplication between {@link #getProgress()} and the given scale value.
	 */
    double getProgress(int scale);
    
    /**
	 * Update the progress.
	 */
    void updateProgress();
}
