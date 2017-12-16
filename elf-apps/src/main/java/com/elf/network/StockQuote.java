/**
 * @version 1.00 1999-08-28
 * @author Cay Horstmann
 */
package com.elf.network;

import java.io.*;
import java.net.*;
import java.util.*;

public class StockQuote
{  
	public StockQuote(String ticker)
	{  
		if(ticker == null || ticker.length() <= 0)
			return;

		this.ticker = ticker;

		try
		{  
			doPost();
		}
		catch (IOException exception)
		{  
			System.out.println("Error: " + exception);
		}
	}	

	///////////////////////////////////////////////////////////////////////////
	
	public double getPrice()
	{  
		return price;
	}

	///////////////////////////////////////////////////////////////////////////
	
	public String getTicker()
	{  
		return ticker;
	}

	///////////////////////////////////////////////////////////////////////////
	
	public String getDate()
	{  
		return date;
	}

	///////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("deprecation")
	private void doPost() throws IOException
	{  
		URL url = new URL("http://finance.yahoo.com/q?");
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);

		PrintWriter out	= new PrintWriter(connection.getOutputStream());

		if(debug)
		{
			String s = "d=" + URLEncoder.encode("t") + '&';
			s += "s=" + URLEncoder.encode(ticker) + '\n';
			out.print(s);
			System.out.println(s);
		}
		else
		{
			out.print("d=" + URLEncoder.encode("t") + '&');
			out.print("s=" + URLEncoder.encode(ticker) + '\n');
			out.close();
		}
		
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
		PrintWriter debugFile = null;
		
		if(debug)
		{
			debugFile = new PrintWriter(new FileOutputStream("F:/dev/src/java/com/elf/network/output.txt"));
		}
		
		while ((line = in.readLine()) != null)
		{
			if(debug)
				debugFile.println(line);
			
			int index = line.indexOf("Last Trade");

			if(index >= 0)
			{
				line = line.substring(index + 10);
				parseDate(line);
				parseQuote(line);
				if(!debug)
					break;
			}
		}

		in.close();
	}

	///////////////////////////////////////////////////////////////////////////
	
	private void parseDate(String line)
	{
		if(!line.startsWith("<br>"))
			return;

		line = line.substring(4);
		StringTokenizer st	= new StringTokenizer(line);

		if(!st.hasMoreTokens())
			return;

		String month = st.nextToken();

		if(!st.hasMoreTokens())
			return;

		String day = st.nextToken();

		//Calendar today = Calendar.getInstance();
		
		date = month + " " + day;
	}

	///////////////////////////////////////////////////////////////////////////
	
	private void parseQuote(String line)
	{
		StringTokenizer st	= new StringTokenizer(line);
		double whole		= 0.0;
		double fraction		= 0.0;
     
		while (st.hasMoreTokens()) 
		{
			String s = st.nextToken();

			if(s.startsWith("<b>"))
			{
				whole = parseMainPrice(s.substring(3));
				fraction = parseFraction(st.nextToken());
				//System.out.println("Price= " + price + " + " + fraction);
				break;
			}
		}
		
		price = whole + fraction;
     }

	///////////////////////////////////////////////////////////////////////////
	
	private double parseMainPrice(String line)
	{
		int index = line.indexOf("<");

		if(index >= 0)
			line = line.substring(0, index);

		return (double)(Integer.parseInt(line));
	}

	///////////////////////////////////////////////////////////////////////////
	
	private double parseFraction(String line)
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
			return;

		StockQuote sq = new StockQuote(args[0]);
		System.out.println(sq.getTicker() + "(" + sq.getDate() + "): " + sq.getPrice());
	}

	private String	ticker	= null;
	private String	date	= "";
	private double	price	= 0.0;
	
	private static final boolean	debug = true;
}

/* Output from Yahoo:

</td></tr><tr align=center valign=top><td nowrap>Last Trade<br>Jul 21 &#183; <b>138 <sup>3</sup>/<sub>16</sub></b></td><td nowrap colspan=2>Change<br>
<font color="#ff0020">-4 <sup>1</sup>/<sub>2</sub> (-3.15%)</font></td><td nowrap>Prev Cls<br>142 <sup>11</sup>/<sub>16</sub></td><td nowrap>Volume<br
>16,043,300</td><td nowrap>Div Date<br>Jul 30</td><td nowrap rowspan=3 valign=bottom width="1%" bgcolor=white><a href="/q?s=INTC&d=1y">

Output with a whole number for price:

  </td></tr><tr align=center valign=top><td nowrap>Last Trade<br>Jul 21 &#183; <b>81</b></td><td nowrap colspan=2>Change<br><font color="#ff0020">-<sup>
7</sup>/<sub>16</sub> (-0.54%)</font></td><td nowrap>Prev Cls<br>81 <sup>7</sup>/<sub>16</sub></td><td nowrap>Volume<br>816,100</td><td nowrap>Div Dat
e<br>Jul 3</td><td nowrap rowspan=3 valign=bottom width="1%" bgcolor=white><a href="/q?s=PEB&d=1y">
	
StringTokenizer:

<br>Jul
21
&#183;
<b>138
<sup>3</sup>/<sub>16</sub></b></td><td
nowrap

---------------

<br>Jul
21
&#183;
<b>104</b></td><td
nowrap





	*/


