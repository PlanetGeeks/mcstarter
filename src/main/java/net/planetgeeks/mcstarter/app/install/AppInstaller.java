package net.planetgeeks.mcstarter.app.install;

import lombok.Getter;
import lombok.NonNull;
import net.planetgeeks.mcstarter.app.App;
import net.planetgeeks.mcstarter.task.Task;

public abstract class AppInstaller<T extends App> implements Task<AppInstaller<T>>
{
	@Getter
	private T app;
	
    public AppInstaller(@NonNull T app)	
    {
        this.app = app;
    }
    
    @Override
    public void checkStatus() throws InterruptedException
    {
    	if(Thread.interrupted())
    		throw new InterruptedException("Installation interrupted!");
    }
}
