/*
 * NetUtils_old.java
 *
 * Created on October 21, 2004, 11:49 AM
 */

package com.elf.net;

import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author  bnevins
 */
public class NetUtils_old
{
	
	private NetUtils_old()
	{
        // all static!
	}

    public static boolean isServerReachableHTTP(final String host, final int port)
	{
		boolean reachable = false;
		try
		{
			final URL url = new URL("http", host, port, "/");
			final URLConnection urlc = url.openConnection();
			final HttpURLConnection http = (HttpURLConnection)urlc;
			http.setRequestMethod("GET");
			http.getContentLength();
			reachable = true;
		}
		catch(final Exception e)
		{
			reachable = false; //just in case
		}
		return ( reachable );
	}
	
	
	public static String getInfo()
	{
        try {
            return getInfo(InetAddress.getLocalHost());
        }
        catch(UnknownHostException e) {
            return null;
        }
	}
	
	public static String getInfo(String...  hostnames)
	{
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < hostnames.length; i++)
        {
            sb.append("Given Hostname: " + hostnames[i] + "   ");
            try
            {
                sb.append(getInfo(InetAddress.getByName(hostnames[i])));
            }
            catch (UnknownHostException ex)
            {
                sb.append(ex.toString());
            }
            sb.append('\n');
        }

        return sb.toString();
    }

	static String getInfo(InetAddress in)
	{
		try
		{
			StringBuffer sb = new StringBuffer();
			
			String		ip  = in.getHostAddress();
			String official	= in.getHostName();
			String canon	= in.getCanonicalHostName();
			InetAddress[] allip = InetAddress.getAllByName(official);
			String all = "";
			
			for(int j = 0; j < allip.length; j++)
				all += allip[j].getHostAddress() + ", ";
			
			sb.append("ip: " + ip);
			sb.append(",  ");
			sb.append("official: " + official);
			sb.append(",  ");
			sb.append("canon: " + canon);
			sb.append(",  ");
			sb.append("all: " + all);
			return sb.toString();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "ERROR";
		}
	}
	
	public static void main(String[] args)
	{
		System.out.println(getInfo());
		
		String[] hh = new String[]
		{
			//"iasengsol6.red.iplanet.com",
			//"wqa-leo.red.iplanet.com",
			//"stork1.sfbay.sun.com",
			//"eas119.india.sun.com",
			//"eas117.india.sun.com",
			//"www.yahoo.com",
			"LOCALHOST",
			"127.0.0.1",
			"localhost",
			"vaio"
		};
		
		if(args.length > 0)
			System.out.println(getInfo(args));
		else
			System.out.println(getInfo(hh));
	}
	
}