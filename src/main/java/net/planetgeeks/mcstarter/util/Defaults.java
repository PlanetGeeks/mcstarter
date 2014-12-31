package net.planetgeeks.mcstarter.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import lombok.NonNull;

/**
 * Contains library default values.
 * <p>
 * Values are loaded from 'resources/properties/defaults.properties'.
 * 
 * @author Flood2d
 */
public class Defaults
{	
    private static ResourceBundle bundle;
    
    static
    {
    	bundle = ResourceBundle.getBundle("properties.defaults");
    }
    
    /**
     * Get a default value by its key.
     * 
     * @param key - value key.
     * @return a String 
     */
    public static String getString(@NonNull String key)
    {
         return getString(key, new Object[0]);
    }
    
    /**
     * Get a default value by its key, and format it with the given arguments.
     * 
     * @param key - value key
     * @param args - format arguments.
     * 
     * @return a formatted String.
     */
    public static String getString(@NonNull String key, @NonNull Object ... args)
    {
    	if(bundle != null)
    		return MessageFormat.format(bundle.getString(key), args);
    	
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("${%s}",  key));    
        for(int i = 0; i < args.length; i++)
        	builder.append(String.format("%s%s", i == 0 ? ":" : ",", args[i]));
        
        return builder.toString();
    }
    
    /**
     * Get a default value by its key and convert it to an {@link #URL} object.
     * 
     * @param key - url value key
     * @return an {@link #URL} object.
     * @throws MalformedURLException
     */
    public static URL getUrl(@NonNull String key) throws MalformedURLException
    {
        return new URL(getString(key));	
    }
}
