package net.planetgeeks.mcstarter.util;

/**
 * Implemented by objects that need to notify other objects about his state.
 * 
 * @author Flood2d
 */
public interface Listenable<T>
{
	/**
	 * Add an object listener.
	 * 
	 * @param listener
	 */
    void addListener(Listener<T> listener);
    
    /**
	 * Remove an object listener.
	 * 
	 * @param listener
	 */
    void removeListener(Listener<T> listener);
    
    /**
	 * Call end event.
	 * 
	 * @param listened - object
	 * @param e - exception if thrown, or null.
	 */
    void callEnd(T listened, Exception e);
}
