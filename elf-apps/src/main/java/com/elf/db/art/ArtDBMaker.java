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

class ArtDBMaker
{  
	ArtDBMaker(ArtRecordMgr	artMgr)
	{  
		Assertion.check(artMgr);
		
		this.artMgr = artMgr;
		
		try
		{  
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			//BufferedReader in = new BufferedReader(new FileReader(tableName + ".dat"));
			populateTable(stmt);
			//showTable(tableName, stmt);
			//in.close();
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
	
	static Connection getConnection() throws SQLException, IOException
	{	
		Properties		props		= new Properties();
		String			fileName	= "ArtDB.properties";
		FileInputStream in			= new FileInputStream(fileName);
		
		props.load(in);

		String drivers = props.getProperty("jdbc.drivers");
		
		if (drivers != null)
			System.setProperty("jdbc.drivers", drivers);
		
		String url		= props.getProperty("jdbc.url");
		String username = props.getProperty("jdbc.username");
		String password = props.getProperty("jdbc.password");

		return DriverManager.getConnection(url, username, password);
	}	

	////////////////////////////////////////////////////////////////////////////

	private void showTable(String tableName, Statement stmt) throws SQLException
	{  
		String query = "SELECT * FROM " + tableName;
		ResultSet rs = stmt.executeQuery(query);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();

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

	////////////////////////////////////////////////////////////////////////////

	private  void populateTable(Statement stmt)
			throws SQLException, IOException
	{  
		// 1. do the categories table....
		
		String[] ss = artMgr.getUniqueFieldValues("cat");
		
		for(int i = 0; i < ss.length; i++)
			addRecord(stmt, "Categories", "catname", ss[i]);

		ss = artMgr.getUniqueFieldValues("subcat");
		
		for(int i = 0; i < ss.length; i++)
			addRecord(stmt, "SubCategories", "subcatname", ss[i]);

		for(artMgr.start(); artMgr.hasNext(); )
		{
			ArtRecord ar = artMgr.next();
			addRecord(stmt, "Locations", "drive", "path", ar.drive, ar.path);
		}
		for(artMgr.start(); artMgr.hasNext(); )
		{
			ArtRecord ar = artMgr.next();
			
			if(ar.firstName.length() > 0)
				addRecord(stmt, "Names", "first", "last", ar.firstName, ar.lastName);
		}
		for(artMgr.start(); artMgr.hasNext(); )
		{
			ArtRecord ar = artMgr.next();
			addArtRecord(stmt, ar);
		}
	}

	////////////////////////////////////////////////////////////////////////////

	private  void addArtRecord(Statement stmt, ArtRecord ar)
			throws SQLException, IOException
	{	
		// get all the ID field values...
		String query = "Select DirID from Locations where Drive = '" + ar.drive + "' AND Path = '" + ar.path + "'";
		ResultSet rs = stmt.executeQuery(query);
		Assertion.check(rs.next());
		int dirID = rs.getInt(1);
		//System.out.println("[" + query + "] returned " + dirID);

		query = "Select CatID from Categories where CatName = '" + ar.cat + "'";
		rs = stmt.executeQuery(query);
		Assertion.check(rs.next());
		int catID = rs.getInt(1);
		//System.out.println("[" + query + "] returned " + catID);
		
		int subcatID = -1;
		
		if(ar.subcat.length() > 0)
		{
			query = "Select SubCatID from SubCategories where SubCatName = '" + ar.subcat + "'";
			rs = stmt.executeQuery(query);
			Assertion.check(rs.next());
			subcatID = rs.getInt(1);
			//System.out.println("[" + query + "] returned " + subcatID);
		}
		
		int nameID = -1;
		
		if(ar.firstName.length() > 0)
		{
			query = "Select NameID from Names where First = '" + ar.firstName + "' AND Last = '" + ar.lastName + "'";
			
			rs = stmt.executeQuery(query);
			Assertion.check(rs.next());
			nameID = rs.getInt(1);
			//System.out.println("[" + query + "] returned " + nameID);
		}
		String command = "Insert INTO Art (Filename, DirID, CatID, Rating ";
		
		if(nameID >= 0)
			command += ", NameID";
			
		if(subcatID >= 0)
			command += ", SubCatID";
		
		command += ") VALUES ('" + ar.fileName + "', " + dirID + ", " + catID + ", " + ar.rating;

		if(nameID >= 0)
			command += ", " + nameID;
			
		if(subcatID >= 0)
			command += ", " + subcatID;
		
		command += ")";
		
		try
		{
			stmt.executeUpdate(command);
		System.out.println(command);
		}
		catch (SQLException ex)
		{  
			if(ex.getSQLState().equals("S1000"))	// record is already there...
			{
				System.out.println("Record already exists");
				return;
			}
				
			System.out.println(StringUtils.formatSQLException(ex));
			//throw ex;
		}
	}	

	////////////////////////////////////////////////////////////////////////////

	private  void addRecord(Statement stmt, String tableName, String field, String value)
			throws SQLException, IOException
	{	
		String command = "INSERT INTO " + tableName + " (" + field + ") VALUES ('" +
		value + "')";
		try
		{
			stmt.executeUpdate(command);
		}
		catch (SQLException ex)
		{  
			if(ex.getSQLState().equals("S1000"))	// record is already there...
				return;
				
			System.out.println(StringUtils.formatSQLException(ex));
			throw ex;
		}

    }		

	////////////////////////////////////////////////////////////////////////////

	private  void addRecord(Statement stmt, String tableName, String field1, String field2, String value1, String value2)
			throws SQLException, IOException
	{	
		String command = "INSERT INTO " + tableName + " (" + field1 + ", " + field2 + ") VALUES ('" +
		value1 + "', '" + value2 +  "')";
		try
		{
			//System.out.println(command);
			stmt.executeUpdate(command);
		}
		catch (SQLException ex)
		{  
			if(ex.getSQLState().equals("S1000"))	// record is already there...
				return;
				
			System.out.println(StringUtils.formatSQLException(ex));
			throw ex;
		}

    }		

	////////////////////////////////////////////////////////////////////////////

	private ArtRecordMgr	artMgr	= null;
}

