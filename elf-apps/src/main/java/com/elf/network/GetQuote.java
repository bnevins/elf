/**
 * @version 1.00 1999-08-28
 * @author Cay Horstmann
 */
package com.elf.network;

import java.io.*;
import java.net.*;
import java.util.*;

public class GetQuote
{  
	@SuppressWarnings("deprecation")
	private static double doPost(String stock) throws IOException
	{  
		URL url = new URL("http://finance.yahoo.com/q?");
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);

		PrintWriter out	= new PrintWriter(connection.getOutputStream());

		out.print("d=" + URLEncoder.encode("t") + '&');
		out.print("s=" + URLEncoder.encode(stock) + '\n');
		out.close();

		BufferedReader in;

		try
		{  
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		}
		catch (FileNotFoundException exception)
		{  
			InputStream err	= ((HttpURLConnection)connection).getErrorStream();
		
			if (err == null) 
				throw exception;
			
			in = new BufferedReader(new InputStreamReader(err));
		}

		String line;
		double ret = 0.0;

		while ((line = in.readLine()) != null)
		{
			int index = line.indexOf("Last Trade");
			if(index >= 0)
			{
				line = in.readLine();
				System.out.println(line);
				ret = parseQuote(line);
				break;
			}
		}

		in.close();
		return ret;
	}

	///////////////////////////////////////////////////////////////////////////
	
	private static double parseQuote(String line)
	{
		// <td class="yfnc_tabledata1"><big><b>5.10</b></big></td>
		
		int begin = line.indexOf("<b>");
		
		if(begin < 0)
			return 0.0;
		begin += 3;
		
		int end = line.indexOf("<", begin);
		
		if(end < 0)
			return 0.0;
		
		try
		{
			return Double.parseDouble(line.substring(begin, end));
		}
		catch(NumberFormatException e)
		{
			return 0.0;
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private static double oldParseQuote(String line)
	{
		StringTokenizer st	= new StringTokenizer(line);
		double price		= 0.0;
		double fraction		= 0.0;
     
		while (st.hasMoreTokens()) 
		{
			String s = st.nextToken();

			if(s.startsWith("<b>"))
			{
				price = parseMainPrice(s.substring(3));
				fraction = parseFraction(st.nextToken());
				//System.out.println("Price= " + price + " + " + fraction);
				break;
			}
		}
		return price + fraction;
     }

	///////////////////////////////////////////////////////////////////////////
	
	private static double parseMainPrice(String line)
	{
		int index = line.indexOf("<");

		if(index >= 0)
			line = line.substring(0, index);

		return (double)(Integer.parseInt(line));
	}

	///////////////////////////////////////////////////////////////////////////
	
	private static double parseFraction(String line)
	{
		final String startSup	= "<sup>";
		final String endSup		= "</sup>/<sub>";
		final String endSub		= "</sub>";

		int sup, sub;
		double ret = 0.0;
		//System.out.println("parseFraction: " + line);

		if(!line.startsWith(startSup)) 		
			return ret;

		line = line.substring(startSup.length());


		int index = line.indexOf(endSup);

		if(index < 0)
			return ret;

		sup = Integer.parseInt(line.substring(0, index));

		line = line.substring(index + endSup.length());

		index = line.indexOf(endSub);

		if(index < 0)
			return ret;

		sub = Integer.parseInt(line.substring(0, index));
		
		return ((double)sup) / ((double)sub);
	}

	///////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args)
	{  
		if(args.length < 1)
			args = new String[] {"SUNW"};
			//return;
		try
		{  
			double price = doPost(args[0]);
			System.out.println("Price: " + price);
		}
		catch (IOException exception)
		{  
			System.out.println("Error: " + exception);
		}
	}
}

/* Output from Yahoo:
yfnc_tablehead1" width="48%">Last Trade:</td>
<td class="yfnc_tabledata1"><big><b>5.10</b></big></td>

	*/