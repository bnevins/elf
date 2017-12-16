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

public class ArtDBException extends Exception
{  
	public ArtDBException(SQLException se)
	{ 
		super("SQL Exception -- " + StringUtils.formatSQLException(se));
	}
	public ArtDBException(String what)
	{ 
		super(what);
	}
	public ArtDBException()
	{ 
		super();
	}
	
}
