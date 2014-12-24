package net.planetgeeks.mcstarter.app.install;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import net.planetgeeks.mcstarter.app.App;
import net.planetgeeks.mcstarter.util.task.ProgressTask;

public abstract class AppVerifier<T extends App> extends ProgressTask<List<AppFile>>
{
	@Getter
	private final T app;
	
	public AppVerifier(@NonNull T app)
	{
		this.app = app;
	}
	
	public abstract AppInstaller<T> getInstaller();
	
	@Override
	public void updateProgress()
	{
		
	}
}
