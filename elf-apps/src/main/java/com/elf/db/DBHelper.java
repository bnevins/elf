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

public class DBHelper
{  
	public DBHelper(String ODBCName)
	{  
	    this.ODBCName = ODBCName;
	}

	////////////////////////////////////////////////////////////////////////////
	
	public DBHelper(String ODBCName, String u, String p)
	{  
	    this.ODBCName = ODBCName;
	    username = u;
	    password = p;
	}

	////////////////////////////////////////////////////////////////////////////
	
	public Connection getConnection() throws SQLException
	{	
		if(connection == null)
			setConnection();

		return connection;
	}


	////////////////////////////////////////////////////////////////////////////
	
	public Statement getStatement() throws SQLException
	{	
		return connection.createStatement();
	}

	////////////////////////////////////////////////////////////////////////////
	
	private void setConnection() throws SQLException//, IOException
	{	
		Assertion.check(connection == null);

		System.setProperty("jdbc.drivers", "sun.jdbc.odbc.JdbcOdbcDriver");
		String url		= "jdbc:odbc:" + ODBCName;

		connection = DriverManager.getConnection(url, username, password);
	}	

	////////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args)
	{	
		System.out.println("Before Change: " + System.getProperty("jdbc.drivers"));
		System.setProperty("jdbc.drivers", "sun.jdbc.odbc.JdbcOdbcDriver");
		System.out.println("After Change: " + System.getProperty("jdbc.drivers"));
	}

	////////////////////////////////////////////////////////////////////////////

	private Connection	connection	= null;
	private String      ODBCName    = null;
	private String      username    = null;
	private String      password    = null;
}

