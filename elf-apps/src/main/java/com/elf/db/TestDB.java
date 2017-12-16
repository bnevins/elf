/**
 * @version 1.20 1999-08-16
 * @author Cay Horstmann
 */
package com.elf.db;

import java.net.*;
import java.sql.*;
import java.io.*;
import java.util.*;
import com.elf.util.*;
//import com.elf.db.art.*;

public class TestDB
{
	public static void main(String[] args)
	{
		if(args.length < 2)
			return;

		try
		{
			db = new DBHelper("Flicks");
			conn = db.getConnection();
			st = db.getStatement();
		}
		catch(SQLException ex)
		{
			System.err.println("" + ex);
			return;
		}

		if(args[0].equalsIgnoreCase("query"))
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

				System.out.println();
				
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
				//System.err.println("" + new ArtDBException(ex));
				System.err.println(ex);
			}
		}

	}
	
	//////////////////////////////////////////////////////////////////
	
	
	private static Connection conn = null;
	private static Statement st = null;
	private static DBHelper  db	= null;
}
		