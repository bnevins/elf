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

public class MakeList
{  
	public static void main(String[] args)
	{
		MakeList ml = new MakeList(args);
	}
	
	////////////////////////////////////////////////////////////////////////////

	MakeList(String[] args)
	{  
		Assertion.check(args.length == 2);
		Assertion.check(args[0]);
		Assertion.check(args[1]);
		
		try
		{  
			Connection con = ArtDBMaker.getConnection();
			Statement stmt = con.createStatement();
			
			String query = "Select drive, path, filename from ArtAll where " + args[0];
			System.out.println(query);
			ResultSet rs = stmt.executeQuery(query);
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(args[1])));
 
			while(rs.next())
			{
				final String sep = File.separator;
;
				String s = rs.getString(1) + ":" + sep + rs.getString(2) + sep + rs.getString(3);
				out.println(s);
			}
			out.close();
			stmt.close();
			con.close();
		}
		catch (SQLException ex)
		{  
			System.out.println(StringUtils.formatSQLException(ex));
		}
		catch (IOException ex)
		{  
			System.out.println("Exception: " + ex);
			ex.printStackTrace ();
		}
	}
		
	////////////////////////////////////////////////////////////////////////////
}

