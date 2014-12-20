package net.planetgeeks.mcstarter.util.task.recover;

import java.util.List;
import java.util.concurrent.Future;

import lombok.Getter;
import lombok.Setter;
import net.planetgeeks.mcstarter.util.task.TaskExecutor;

/**
 * An extended {@link #TaskExecutor} that deals with {@link #RecoverableTask} objects.
 * 
 * @author Flood2d
 *
 * @see #TaskExecutor
 */
@Getter
public class RecoverableTaskExecutor<T extends RecoverableTask<E>, E> extends TaskExecutor<T, E> implements Recoverable
{
    @Setter
	private int attemptLimit = 2;
	@Setter
	private long attemptDelay = 10 * 1000L;
	@Setter
	private boolean recoverable = true;
	
	@Override
	public synchronized List<Future<E>> call()
	{
		for(RecoverableTask<E> task : getTasks())
		{
			task.setRecoverable(isRecoverable());
			task.setAttemptLimit(getAttemptLimit());
			task.setAttemptDelay(getAttemptDelay());
		}
		
		return super.call();
	}
}
