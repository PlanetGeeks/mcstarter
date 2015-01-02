package net.planetgeeks.mcstarter.minecraft.mods.forge;

import static net.planetgeeks.mcstarter.util.LogUtils.log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import net.planetgeeks.mcstarter.task.Task;
import net.planetgeeks.mcstarter.util.Defaults;
import net.planetgeeks.mcstarter.util.IOUtils;
import net.planetgeeks.mcstarter.util.ProgressView;
import net.planetgeeks.mcstarter.util.Zip;

public class ForgePatcher extends ProgressView implements Task<ForgePatcher>
{
	@Getter
	private final File originalFile;
	private boolean removeMeta = false;
	
	public ForgePatcher(@NonNull File originalFile)
	{
		this.originalFile = originalFile;
	}
	
	@Override
	public ForgePatcher call() throws IOException
	{
		log.info(Defaults.getString("forge.patcher.start"));
		
		if(originalFile.exists())
		{
		    File temp = IOUtils.getTemp(originalFile);
		   
		    if(removeMeta)
		    	removeMeta(temp);
		    
		    originalFile.delete();

		    temp.renameTo(originalFile);
		    
		    setProgress(1.0D);
		    
		    log.info(Defaults.getString("forge.patcher.success"));
		}
		else
			log.severe(Defaults.getString("forge.patcher.nosrc"));
		
		return this;
	}
	
	private void removeMeta(@NonNull File temp) throws IOException
	{
		setProgress(0.5D);
		
		log.info("forge.patcher.meta");
		
		Zip zip = null;
	    
	    try
	    {
	    	zip = new Zip(originalFile);
	    	
	    	List<String> ignored = new ArrayList<>();
	    	ignored.add("META-INF");
	    	zip.copyTo(temp, ignored);
	    }
	    finally
	    {
	    	IOUtils.closeQuietly(zip);
	    }
	}
	
	public void setRemoveMeta(boolean remove)
	{
		this.removeMeta = remove;
	}
	
	@Override
	public void checkStatus() throws InterruptedException
	{
		if(Thread.interrupted())
			throw new InterruptedException("Task interrupted");
	}
}
