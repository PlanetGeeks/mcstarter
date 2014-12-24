package net.planetgeeks.mcstarter.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;
import net.planetgeeks.mcstarter.util.task.Task;

public class ProgressUpdater extends Task<Object>
{
	private List<ProgressView> views = new ArrayList<>();
	@Getter
	@Setter
	private long delay = 500L;
	@Getter
	private boolean started = false;
	private ScheduledExecutorService executor;
	private Updater updater;

	public synchronized void add(ProgressView view)
	{
		synchronized (views)
		{
			views.add(view);
		}
	}

	public synchronized void remove(ProgressView view)
	{
		synchronized (views)
		{
			if (views.contains(view))
				views.remove(view);
		}
	}

	@Override
	public Object call()
	{
		if (isStarted())
			throw new IllegalStateException("Progress updater already started!");

		started = true;

		addSubTask(updater = new Updater());

		executor = Executors.newSingleThreadScheduledExecutor();

		updater.schedule();

		return null;
	}

	private void updateAll()
	{
		synchronized (views)
		{
			for (ProgressView view : views)
				view.updateProgress();
		}
	}

	private class Updater extends Task<Object>
	{
		@Override
		public Object call() throws Exception
		{
			try
			{
				checkStatus();

				updateAll();
				
				schedule();
			}
			catch (InterruptedException e)
			{
				executor.shutdown();
			}
			return null;
		}
		
		private void schedule()
		{
			executor.schedule(updater, delay, TimeUnit.MILLISECONDS);
		}
	}
}
