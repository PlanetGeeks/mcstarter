package net.planetgeeks.mcstarter.util;

/**
 * Implemented by objects that need to be notified on specific events.
 * 
 * @author Flood2d
 */
public interface Listener<T>
{
	/**
	 * Called on end event.
	 * 
	 * @param listened - the listened object
	 * @param e - an exception if thrown, or null
	 */
    void onEnd(T listened, Exception e);
}
