package net.planetgeeks.mcstarter.event;

import static net.planetgeeks.mcstarter.util.LogUtils.log;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import lombok.NonNull;
import net.planetgeeks.mcstarter.util.Defaults;
import net.planetgeeks.mcstarter.util.Handler;

public abstract class Event
{
	private static Map<Class<? extends Event>, Set<Handler>> handlers = new HashMap<>();

	protected static void register(@NonNull Class<? extends Event> eventType, @NonNull Handler handler)
	{
		if (!handlers.containsKey(eventType))
		{
			synchronized (handlers)
			{
				handlers.put(eventType, new TreeSet<Handler>());
			}
		}

		Set<Handler> handlers = Event.handlers.get(eventType);

		synchronized (handlers)
		{
			if (!handlers.add(handler))
				log.warning(Defaults.getString("event.register.exists", handler.toString()));
		}
	}

	protected static void unregister(@NonNull Object obj)
	{
		synchronized (handlers)
		{
			Iterator<Class<? extends Event>> events = handlers.keySet().iterator();

			while (events.hasNext())
			{
				Class<? extends Event> eventType = events.next();

				Set<Handler> handlers = unregister(Event.handlers.get(eventType), obj);
				
				synchronized(handlers)
				{
					if(handlers.isEmpty())
						events.remove();
				}
			}
		}
	}
	
	protected static Set<Handler> unregister(@NonNull Set<Handler> handlers, @NonNull Object obj)
	{ 
		synchronized(handlers)
		{
			Iterator<Handler> it = handlers.iterator();

			while (it.hasNext())
			{
				Handler handler = it.next();

				if (handler.getObject() == obj)
					it.remove();
			}
		}	
		
		return handlers;
	}

	protected static void invokeAll(@NonNull Event evt) throws InterruptedException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Set<Handler> handlers = getHandlers(evt.getClass());

		synchronized (handlers)
		{
			for (Handler handler : handlers)
				handler.invoke(evt);
		}
	}

	private static Set<Handler> getHandlers(@NonNull Class<? extends Event> eventType)
	{
		synchronized (handlers)
		{
			return handlers.containsKey(eventType) ? handlers.get(eventType) : new HashSet<Handler>();
		}
	}
}
