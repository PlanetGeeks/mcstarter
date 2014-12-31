package net.planetgeeks.mcstarter.task;

import java.util.concurrent.Callable;

public interface Task<T> extends Callable<T>
{
	void checkStatus() throws InterruptedException;
}