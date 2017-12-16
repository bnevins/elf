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

public class ArtDB
{  
	ArtDB()
	{  
	}

	////////////////////////////////////////////////////////////////////////////
	
	public Connection getConnection() throws ArtDBException
	{	
		if(connection == null)
			setConnection();

		return connection;
	}

	////////////////////////////////////////////////////////////////////////////
	
	public Statement getStatement() throws ArtDBException
	{	
		try
		{
			return connection.createStatement();
		}
		catch(SQLException sex)
		{
			throw new ArtDBException(sex);
		}
	}

	////////////////////////////////////////////////////////////////////////////
	
	private void setConnection() throws ArtDBException//, IOException
	{	
		Assertion.check(connection == null);

		System.setProperty("jdbc.drivers", "sun.jdbc.odbc.JdbcOdbcDriver");
		String url		= "jdbc:odbc:Pix";
		String username = null;
		String password = null;

		try
		{
			connection = DriverManager.getConnection(url, username, password);
		}
		catch(SQLException sex)
		{
			throw new ArtDBException(sex);
		}
	}	

	////////////////////////////////////////////////////////////////////////////

	private Connection	connection	= null;
}

