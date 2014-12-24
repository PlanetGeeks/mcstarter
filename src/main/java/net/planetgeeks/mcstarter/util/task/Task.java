package net.planetgeeks.mcstarter.util.task;

import static net.planetgeeks.mcstarter.util.task.Task.Status.INTERRUPTED;
import static net.planetgeeks.mcstarter.util.task.Task.Status.PAUSED;
import static net.planetgeeks.mcstarter.util.task.Task.Status.RUNNING;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Represents a task that can be executed, paused or interrupted.
 * <p>
 * A task can have several subordinate task whose status will be updated simultaneously with the master task.
 * <p>
 * 
 * @see #interrupt()
 * @see #pause()
 * @see #resume()
 * @see #addSubTask(Task)
 * @see #removeSubTask(Task)
 * 
 * @author Flood2d
 * @param <T> The return <code>Type</code> of the {@link #call} method.
 */
public abstract class Task<T> implements Callable<T>
{
	@Getter @Setter(AccessLevel.PROTECTED)
	private Status status = RUNNING;

	private List<Task<?>> subTasks = new ArrayList<>();

	protected void checkStatus() throws InterruptedException
	{
		synchronized (getStatus())
		{
			setStatus(Thread.interrupted() ? INTERRUPTED : getStatus());

			switch (getStatus())
			{
				case INTERRUPTED:
					throw new InterruptedException("The task has been interrupted");
				case PAUSED:

					getStatus().wait();

					setStatus(RUNNING);
					break;
				default:
					break;
			}
		}
	}

	/**
	 * Pause from a running status this task and its subordinated tasks.
	 */
	public void pause()
	{
		synchronized (getStatus())
		{
			if (getStatus() == RUNNING)
				setStatus(PAUSED);
			
			synchronized(subTasks)
			{
				for(Task<?> task : subTasks)
					task.pause();
			}
		}
	}

	/**
	 * Resume from a paused status this task and its subordinated tasks.
	 */
	public void resume()
	{
		synchronized (getStatus())
		{
			getStatus().notifyAll();
			
			synchronized(subTasks)
			{
				for(Task<?> task : subTasks)
					task.resume();
			}
		}
	}

	/**
	 * Interrupt the task and its subordinated tasks.
	 */
	public void interrupt()
	{
		synchronized (getStatus())
		{
			setStatus(INTERRUPTED);
			
			synchronized(subTasks)
			{
				for(Task<?> task : subTasks)
					task.interrupt();
			}
		}
	}
	
	/**
	 * Add a task to the subordinated task list of this task.
	 * <p>
	 * Changing the status of the master task will automatically apply the same status to all subordinated tasks.
	 * 
	 * @param task to subordinate.
	 */
	protected void addSubTask(@NonNull Task<?> task)
	{
		synchronized(subTasks)
		{
			subTasks.add(task);
		}
	}
	
	/**
	 * Remove a task from the subordinated task list of this task.
	 * <p>
	 * Changing the status of the master task will affect only status of subordinated tasks.
	 * 
	 * @param task to remove.
	 */
	protected void removeSubTask(@NonNull Task<?> task)
	{
		synchronized(subTasks)
		{
			if(subTasks.contains(task))
				subTasks.remove(task);
		}
	}
	
	/**
	 * Clear subordinated task list of this task.
	 */
	protected void clearSubTasks()
	{
		synchronized(subTasks)
		{
			subTasks.clear();
		}
	}
	
	/**
	 * Get a copy of the subordinated task list of this task.
	 * 
	 * @return a copy of the subordinated task list.
	 */
	public List<Task<?>> getSubTasks()
	{
		List<Task<?>> subTasks = new ArrayList<>();
		
		synchronized(this.subTasks)
		{
			for(Task<?> task : this.subTasks)
				subTasks.add(task);
		}
		
		return subTasks;
	}

	/**
	 * Represents a task status.
	 *
	 * @author Flood2d
	 */
	public static enum Status
	{
		PAUSED,
		INTERRUPTED,
		RUNNING;
	}
}
