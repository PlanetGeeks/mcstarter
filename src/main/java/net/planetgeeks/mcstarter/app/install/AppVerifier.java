package net.planetgeeks.mcstarter.app.install;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import net.planetgeeks.mcstarter.app.App;
import net.planetgeeks.mcstarter.util.task.ProgressTask;

public abstract class AppVerifier<T extends App<T>> extends ProgressTask<List<AppFile>>
{
	@Getter
	private final T application;
	
	public AppVerifier(@NonNull T app)
	{
		this.application = app;
	}
	
	public AppInstaller<?> getInstaller()
	{
		return getApplication().getInstaller();
	}
	
	@Override
	public void updateProgress()
	{
		
	}
}
