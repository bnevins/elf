/**
 * @version 1.20 1999-08-16
 * @author Cay Horstmann
 */
package com.elf.db.art;

import java.net.*;
import java.sql.*;
import java.io.*;
import java.util.*;
import com.elf.util.*;

public class TestArtDB
{
	public static void main(String[] args)
	{
		if(args.length < 2)
			return;

		try
		{

			db = new ArtDB();
			conn = db.getConnection();
			st = db.getStatement();
		}
		catch(ArtDBException ex)
		{
			System.err.println("" + ex);
			return;
		}

		if(args[0].equalsIgnoreCase("updateRating"))
			updateRating(args[1], Integer.parseInt(args[2]));
		
		else if(args[0].equalsIgnoreCase("makeList"))
			makeList(args[1], Integer.parseInt(args[2]));
		
		else if(args[0].equalsIgnoreCase("query"))
		{
			try
			{
				ResultSet rs = st.executeQuery(args[1]);
				ResultSetMetaData rsm = rs.getMetaData();
				int columnCount = rsm.getColumnCount();

				for (int i = 1; i <= columnCount; i++)
				{  
					if (i > 1) System.out.print(", ");
						System.out.print(rsm.getColumnName(i));
				}	
				
				while (rs.next())
				{  
					for (int i = 1; i <= columnCount; i++)
					{  
						if (i > 1) System.out.print(", ");
							System.out.print(rs.getString(i));
					}	
					
					System.out.println();
				}	
		
				rs.close();
			}
			catch(SQLException ex)
			{
				System.err.println("" + new ArtDBException(ex));
			}
		}

	}
	
	//////////////////////////////////////////////////////////////////
	
	private static void updateRating(String fname, int rating)
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(fname));

			String line;

			while((line = in.readLine()) != null)
			{
				Assertion.check(line.length() >= 10);	// R:\A\B.jpg
				int index = line.lastIndexOf('\\');
				Assertion.check(index > 0);

				String filename = line.substring(index + 1);
				String path = line.substring(3, index);

				String up = "UPDATE ARTALL SET RATING = " + rating +
							" WHERE FILENAME = '" + filename + "' AND PATH = '" +
							path + "'";

				System.err.println(up);

				st.executeUpdate(up);
			}
		}
		catch(Exception e)
		{
			System.out.println("" + e);
		}
	}

	//////////////////////////////////////////////////////////////////
	
	private static void makeList(String fname, int rating)
	{
		--rating;
		try
		{
			PrintWriter out = new PrintWriter(new FileWriter(fname));
			String query = "SELECT path, filename from ArtAll where rating > " + rating;

			ResultSet rs = st.executeQuery(query);
			ResultSetMetaData rsm = rs.getMetaData();
			int columnCount = rsm.getColumnCount();

			for (int i = 1; i <= columnCount; i++)
			{  
				if (i > 1) System.out.print(", ");
					System.out.print(rsm.getColumnName(i));
			}	
			
			while (rs.next())
			{  
				String s = "R:\\" + rs.getString(1) + "\\" + rs.getString(2);
				out.println(s);
				System.err.println(s);
			}	
			out.close();
			rs.close();
		}
		catch(Exception ex)
		{
			System.err.println("" + ex);
		}
	}




	
	private static Connection conn = null;
	private static Statement st = null;
	private static ArtDB db	= null;
}
		