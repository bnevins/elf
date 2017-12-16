/*
 * FileDB.java
 *
 * Created on December 20, 2007, 11:09 AM
 *
 */

package com.elf.io;

import com.elf.db.*;
import java.io.*;
import java.sql.*;

/**
 *
 * @author bnevins
 */
public class FileDB
{
    public FileDB() throws IOException, SQLException, DerbyException
    {
       derby = new Derby(USER, PW);
       derby.setNetworked();
       derby.connect(DB_NAME);
    }
    
    public static void main(String[] args)
    {
        try
        {
            FileDB fdb = new FileDB();
            fdb.createSchema();
        } 
        catch (Exception ex)
        {
            ex.printStackTrace();
        } 
    }

    public Connection getConnection() throws DerbyException
    {
        return derby.getConnection();
    }
    
    public synchronized void createSchema() throws IOException, SQLException, DerbyException
    {
        Statement statement = null;
        Connection connection = null;
        
        try
        {
            derby.createDB(DB_NAME);
            connection = derby.getConnection();
            String[] schema = readCreateSchemaScript();
            statement = connection.createStatement();
            
            // drop the tables.  We don't care if we get an Exception because of
            // the table not existing already.

            for(String table : TABLES)
            {
                try 
                { 
                    statement.execute("drop table " + table); 
                    System.out.println("Dropped " + table);
                }
                catch(Exception e)
                { 
                    System.out.println("Error dropping " + table + ": " + e);
                }
            }
            for(String sqlCommand : schema)
                statement.execute(sqlCommand);
            
            connection.close();
        }
        finally
        {
           try { connection.close(); } catch(Exception e){} 
           try { statement.close(); } catch(Exception e){} 
        }
    }

    private String[] readCreateSchemaScript() throws IOException
    {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(getClass().getResourceAsStream("createschema.sql")));
        char[] buf = new char[5000];
        String schema = "";
        int numRead = 0; 

        while((numRead = reader.read(buf, 0, 5000)) > 0)
        {
            schema += new String(buf, 0, numRead);
        }

        System.out.println("HERE IS THE SCHEMA!!!" + schema);

        //String[] statements = schema.split(";")
        return schema.split(";");
    }

    private final static String DB_NAME = "FILES";
    public final static String FILEINFO_TABLE = "FILEINFO";
    public final static String DIGESTS_TABLE = "DIGESTS";
    private final static String[] TABLES = { DIGESTS_TABLE, FILEINFO_TABLE }; 
    private final static String USER = "bnevins";
    private final static String PW = "APP";
    private final Derby derby;
}
