package net.planetgeeks.mcstarter.app.install;

import lombok.Getter;
import lombok.NonNull;
import net.planetgeeks.mcstarter.task.Task;

public abstract class AppVerifier<I extends AppInstaller<?>> implements Task<AppVerifier<I>>
{
	@Getter
	private I installer;

	public AppVerifier(@NonNull I installer)
	{
		this.installer = installer;
	}

	@Override
	public void checkStatus() throws InterruptedException
	{
		if (Thread.interrupted())
			throw new InterruptedException("App verification interrupted!");
	}
}
