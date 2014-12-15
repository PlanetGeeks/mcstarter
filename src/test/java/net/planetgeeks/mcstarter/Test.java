package net.planetgeeks.mcstarter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.planetgeeks.mcstarter.session.Session;
import net.planetgeeks.mcstarter.session.Session.AuthException;

public class Test
{
    public static void main(String[]args) throws IOException
    {
    	String id;
    	String password;
    	
    	try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in)))
    	{
    		System.out.print("Insert email/username: ");
    		id = br.readLine();
    		System.out.println();
    		System.out.print("Insert password: ");
    		password = br.readLine();
    	}
    	
    	Session session = new Session(id);
    	session.setPassword(password);
    	
        try
		{
        	//verify the session with the provided credentials
			session.verify();
			
			System.out.println("FIRST ACCESSTOKEN : " + session.getAccessToken());
			
			session.refresh();
			
			System.out.println("REFRESHED ACCESSTOKEN : " + session.getAccessToken());
			
			session.signout();
			
			System.out.println("SIGNOUT CORRECTLY");
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
