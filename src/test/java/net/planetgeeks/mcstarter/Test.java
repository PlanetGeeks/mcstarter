package net.planetgeeks.mcstarter;

import java.io.IOException;

import net.planetgeeks.mcstarter.session.Session;
import net.planetgeeks.mcstarter.session.Session.AuthException;

public class Test
{
    public static void main(String[]args)
    {
    	Session session = new Session("hello");
    	session.setPassword("world");
    	
        try
		{
        	//this will return an AuthException because of invalid credentials.
			session.authenticate();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		catch (AuthException e)
		{
			e.printStackTrace();
		}	
    }
}
