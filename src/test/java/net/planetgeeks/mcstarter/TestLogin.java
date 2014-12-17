package net.planetgeeks.mcstarter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.planetgeeks.mcstarter.minecraft.session.OnlineSession;
import net.planetgeeks.mcstarter.minecraft.session.OnlineSession.AuthException;

public class TestLogin extends Test
{
    public void test()
    {
    	try
		{
			testLogin();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
    }
    
    private static void testLogin() throws IOException
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
    	
    	OnlineSession session = new OnlineSession(id);
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
