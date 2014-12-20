package net.planetgeeks.mcstarter.util.task;

/**
 * Implemented by tasks whose progress can be monitored.
 * 
 * @author Flood2d
 */
public interface ProgressView
{
	/**
	 * Get the progress.
	 * 
	 * @return a double value between 0.0D and 1.0D inclusive, or -1D if the progress can't be retrieved.
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
