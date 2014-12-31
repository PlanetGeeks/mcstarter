package net.planetgeeks.mcstarter.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import lombok.Getter;

public class Handler implements Comparable<Handler>
{
	@Getter
	private Priority priority;
	@Getter
	private final Method method;
	@Getter
	private final Object object;

	public Handler(Priority priority, Object object, Method method)
	{
		this.priority = priority;
		this.method = method;
		this.object = object;
	}

	public void invoke(Object target) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		method.invoke(getObject(), target);
	}

	@Override
	public int compareTo(Handler handler)
	{
		if (method == handler.method && object == handler.object)
			return 0;

		int result = handler.getPriority().compareTo(getPriority());

		return result == 0 ? Integer.compare(hashCode(), handler.hashCode()) : result;
	}

	public static void invoke(Set<Handler> handlers, Object target) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		for (Handler handler : handlers)
			handler.invoke(target);
	}
}
