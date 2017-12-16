/*
 * Derby.java
 *
 * Created on June 10, 2007, 11:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.db;
import java.io.*;
import java.sql.*;
import java.util.*;

/**
 *
 * @author bnevins
 */
public class Derby
{
    public Derby() throws DerbyException
    {
        user = null;
        password = null;
        try
        {
            Class.forName(driver).newInstance();
        } 
        catch(Exception ex)
        {
            ex.printStackTrace();
            throw new DerbyException();
        } 
    }
    public Derby(String u, String pw) throws DerbyException
    {
        user = u;
        password = pw;
        
        try
        {
            Class.forName(driver).newInstance();
        } 
        catch(Exception ex)
        {
            ex.printStackTrace();
            throw new DerbyException();
        } 
    }
    
    ///////////////////////////////////////////////////////////////////////////

    public void setNetworked()
    {
        // use the defaults
        setNetworked("localhost", 1527);
    }
    
    ///////////////////////////////////////////////////////////////////////////

    public void setNetworked(String host, int port)
    {
        embedded = false;
        networkString = "//" + host + ":" + port + "/";
    }
    
    ///////////////////////////////////////////////////////////////////////////

    public void setAutoCommit(boolean b)
    {
        autoCommit = b;
    }

    ///////////////////////////////////////////////////////////////////////////
    
    public Connection getConnection() throws DerbyException
    {
        if(connection == null)
            throw new DerbyException("No connection object.");
        
        return connection;
    }

    ///////////////////////////////////////////////////////////////////////////
    
    public void connect(String name) throws SQLException, DerbyException
    {
        internalConnect(name, false);
    }
    
    ///////////////////////////////////////////////////////////////////////////
    
    public void createDB(String name) throws SQLException, DerbyException
    {
        internalConnect(name, true);
    }

    ///////////////////////////////////////////////////////////////////////////
    
    public void disconnect() throws SQLException
    {
        if(connection != null)
            connection.close();
        
        connection = null;
    }

    ///////////////////////////////////////////////////////////////////////////
    
    public void stop() throws DerbyException
    {
        try
        {
            disconnect();
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
            throw new DerbyException("Database did not shut down normally.");
        }
        catch (SQLException se)
        {
            // We are SUPPOSED to get one!
        }
    }

    private void internalConnect(String name, boolean create) throws SQLException, DerbyException
    {
        if(connection != null)
            return;
        
        String connString = getConnectString(name);
        
        if(create)
            connString += ";create=true";
        
        if(user != null)
            connString += ";user=" + user + ";password=" + password;
        
        connection = DriverManager.getConnection(connString);
        connection.setAutoCommit(autoCommit);
    }

    ///////////////////////////////////////////////////////////////////////////
    
    private String getConnectString(String name)
    {
        if(isEmbedded())
        {
            return embeddedProtocol + name;
        }
        else
        {
            return embeddedProtocol + networkString + name;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    
    private boolean isDir(String dir)
    {
        return dir != null && new File(dir).isDirectory();
    }

    ///////////////////////////////////////////////////////////////////////////

    private boolean isEmbedded()
    {
        return embedded;
    }

    ///////////////////////////////////////////////////////////////////////////
    
    private                 Connection  connection;
    private                 boolean     embedded            = true;
    private                 String      networkString;
    private        final    String      user;
    private        final    String      password;
    private                 boolean     autoCommit          = true;
    private static final    String      driver              = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final    String      embeddedProtocol    = "jdbc:derby:";
}
