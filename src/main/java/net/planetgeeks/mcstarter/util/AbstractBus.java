package net.planetgeeks.mcstarter.util;

import static net.planetgeeks.mcstarter.util.LogUtils.log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import lombok.Getter;
import lombok.NonNull;

public abstract class AbstractBus<T extends Annotation>
{
	@Getter
	private Class<T> annotation;

	protected AbstractBus(Class<T> annotation)
	{
		this.annotation = annotation;
	}

	protected static void register(AbstractBus<?> bus, Object... objs)
	{
		for (Object obj : objs)
			bus.verify(obj);
	}

	protected abstract void verify(Object obj, Method method, Class<?> param, T annotation);

	public void verify(@NonNull Object object)
	{
		Method[] methods = object.getClass().getMethods();

		for (Method method : methods)
		{
			T[] handlers = method.getAnnotationsByType(annotation);

			if (handlers.length < 1)
				continue;

			if (!method.getReturnType().equals(Void.TYPE))
			{
				log.severe(Defaults.getString("bus.invalid.method", method.getName(), Defaults.getString("bus.invalid.method.return")));
				continue;
			}

			Class<?>[] params = method.getParameterTypes();

			if (params.length != 1)
			{
				log.severe(Defaults.getString("bus.invalid.method", method.getName(), Defaults.getString("bus.invalid.method.param")));
				continue;
			}

			verify(object, method, params[0], handlers[0]);
		}
	}
}
