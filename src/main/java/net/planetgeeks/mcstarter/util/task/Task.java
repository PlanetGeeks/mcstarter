package net.planetgeeks.mcstarter.util.task;

import java.util.concurrent.Callable;

/**
 * Represents a task that can be executed and monitored.
 * 
 * @author Flood2d
 */
public abstract class Task<T> implements Callable<T> 
{
    public void checkInterrupt() throws InterruptedException
    {
    	if(Thread.interrupted())
    		throw new InterruptedException();
    }
}
