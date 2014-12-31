package net.planetgeeks.mcstarter.event;

import static net.planetgeeks.mcstarter.util.LogUtils.log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.Getter;
import lombok.NonNull;
import net.planetgeeks.mcstarter.util.AbstractBus;
import net.planetgeeks.mcstarter.util.Defaults;
import net.planetgeeks.mcstarter.util.Handler;

public class EventBus extends AbstractBus<EventHandler>
{
	
	@Getter
	private static EventBus instance = new EventBus();

	private EventBus()
	{
		super(EventHandler.class);
	}

	public static void call(Event event)
	{
		try
		{
			Event.invokeAll(event);
		}
		catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | InterruptedException e)
		{
			e.printStackTrace();
			log.severe(Defaults.getString("eventbus.invoke.exception", event.getClass()));
		}
	}

	public static void register(Object ... objs)
	{
		register(instance, objs);
	}
	
	public static void unregister(@NonNull Object ... objs)
	{
		for(Object obj : objs)
			unregister(obj);
	}
	
	private static void unregister(@NonNull Object obj)
	{
		Event.unregister(obj);
	}

	@Override
	protected void verify(Object obj, Method method, Class<?> param, EventHandler annotation)
	{
		Class<? extends Event> eventType;

		try
		{
			eventType = param.asSubclass(Event.class);
		}
		catch (ClassCastException e)
		{
			log.severe(Defaults.getString("bus.invalid.method", method.getName(), Defaults.getString("eventbus.invalid.method.param")));
			return;
		}

		Event.register(eventType, new Handler(annotation.priority(), obj, method));
	}
}
