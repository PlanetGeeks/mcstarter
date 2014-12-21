package net.planetgeeks.mcstarter.app.install;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.planetgeeks.mcstarter.app.App;
import net.planetgeeks.mcstarter.util.http.HttpFile;
import net.planetgeeks.mcstarter.util.task.ProgressTask;

public class AppVerifier<Z extends App<?>> extends ProgressTask<List<HttpFile>>
{
	@Getter
	private Z application;
	
	public AppVerifier(Z app)
	{
		this.application = app;
	}
	
	@Override
	public void updateProgress()
	{
		
	}

	@Override
	public List<HttpFile> call() throws Exception
	{
		return new ArrayList<HttpFile>();
	}
}
