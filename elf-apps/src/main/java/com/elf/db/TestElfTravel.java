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
import com.elf.db.art.*;

public class TestElfTravel
{
	public static void main(String[] args)
	{
		try
		{
			db = new DBHelper("Elf_Travel");
			conn = db.getConnection();
			st = db.getStatement();
		}
		catch(SQLException ex)
		{
			System.err.println("" + ex);
			return;
		}

		try
		{
			ResultSet rs = st.executeQuery("SELECT * FROM CABIN");
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
			System.err.println("" + new ArtDBException(ex));
		}
	}
	
	//////////////////////////////////////////////////////////////////
	
	
	private static Connection conn = null;
	private static Statement st = null;
	private static DBHelper  db	= null;
}
		