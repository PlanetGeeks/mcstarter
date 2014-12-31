package net.planetgeeks.mcstarter.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CollectionsUtils
{	
	/**
	 * Copy a set to another one.
	 * 
	 * @param src - Source set
	 * @param dst - Destination set
	 * @return the destination set.
	 */
	public static <T, M extends Set<? extends T>, N extends Set<? super T>> N copySet(M src, N dst)
	{
		return copyCollection(src, dst);
	}

	/**
	 * Copy a list to another one.
	 * 
	 * @param src - Source list
	 * @param dst - Destination list
	 * @return the destination list.
	 */
	public static <T, M extends List<? extends T>, N extends List<? super T>> N copyList(M src, N dst)
	{
		return copyCollection(src, dst);
	}

	/**
	 * Clone a set.
	 * <p>
	 * It simply copies the source set to a new {@link #LinkedHashSet}.
	 * 
	 * @param src - Source set
	 * @return the LinkedHashSet with copied values.
	 */
	public static <T, M extends Set<? extends T>> LinkedHashSet<T> cloneSet(M src)
	{
		return copySet(src, new LinkedHashSet<T>());
	}

	/**
	 * Clone a list.
	 * <p>
	 * It simply copies the source list to a new {@link #ArrayList}.
	 * 
	 * @param src - Source list
	 * @return the ArrayList with copied values.
	 */
	public static <T, M extends List<? extends T>> ArrayList<T> cloneList(M src)
	{
		return copyList(src, new ArrayList<T>());
	}
	
	/**
	 * Copy a collection to another one.
	 * 
	 * @param src - Source collection
	 * @param dst - Destination collection
	 * @return the destination collection.
	 */
	public static <T, M extends Collection<? extends T>, N extends Collection<? super T>> N copyCollection(M src, N dst)
	{
		synchronized(src)
		{
			synchronized(dst)
			{
				dst.clear();
				
				for(T element : src)
					dst.add(element);
				
				return dst;
			}
		}
	}
}
 