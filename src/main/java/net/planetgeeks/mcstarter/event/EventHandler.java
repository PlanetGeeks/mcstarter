package net.planetgeeks.mcstarter.event;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static net.planetgeeks.mcstarter.util.Priority.NORMAL;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.planetgeeks.mcstarter.util.Priority;

@Retention(value = RUNTIME)
@Target(value = METHOD)
public @interface EventHandler
{
	Priority priority() default NORMAL;
}
