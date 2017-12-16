/*
 * WebServer.java
 *
 * Created on June 10, 2003, 10:56 PM
 */

package com.elf.network;
import java.net.*;
import java.io.*;

/**
 *
 * @author  bnevins
 */
public class WebServer {
	
	/** Creates a new instance of WebServer */
	public WebServer() 
	{
		try
		{
			init();
			Socket s = ss.accept();
			InputStreamReader stream = new InputStreamReader(s.getInputStream());
			int c;
			while((c = stream.read()) >= 0)
			{
				System.out.print((char)c);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	private void init() throws IOException
	{
		ss = new ServerSocket(80);
	}
	
	public static void main(String[] notUsed)
	{
		WebServer ws = new WebServer();
	}
	private ServerSocket ss;
}
