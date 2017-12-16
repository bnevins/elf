/*
 * MakeFileDigestsAndDupeCheck.java
 *
 * Created on December 16, 2007, 12:26 PM
 *
 */

package com.elf.io;

import com.elf.db.DerbyException;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

/**
 *
 * @author bnevins
 */
public class FileTreeDigester
{
    public FileTreeDigester(String rootString, String outFileName) throws IOException, NoSuchAlgorithmException, SQLException, DerbyException
    {
        db = new FileDB();
        root = FileUtils.safeGetCanonicalPath(new File(rootString));
        doit(outFileName);
    }
    
    ////////////////////////////////////////////////////////////////////////////

    private void doit(String fn) throws IOException, NoSuchAlgorithmException, DerbyException, SQLException
    {
        PrintWriter pw = new PrintWriter(fn);
        FileFinder ff = new FileFinder(root);
        List<File> files = ff.getFiles();
        Collections.sort(files);
        
        for(File f : files)
        {
            f = renameIfNameTooLong(f);
    
            if(alreadyInDB(f))
                continue;
            
            FileDigester fd = new FileDigester(f.getPath());
            String digest = fd.getDigestStringCompact();
            pw.printf("%s:::%s\n", f.getPath(), digest);
            addToDB(f, digest);
            //System.out.printf("%s:::%s\n", f.getPath(), digest);
        }
        pw.close();
    }

    ///////////////////////////////////////////////////////////////////////////

    public static void main(String[] args)
    {
        if(args.length == 2)
        {
            try
            {
                new FileTreeDigester(args[0], args[1]);
            } 
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
            System.out.println("USAGE: MakeFileDigests root-dir out-file");
    }

    private void addToDB(File f, String digest) throws DerbyException, SQLException
    {
        Connection conn = db.getConnection();
        PreparedStatement ps = conn.prepareStatement(ADD_FILE_QUERY);
            
        ps.setString(1, f.getName());
        ps.setString(2, getStandardPath(f));
        ps.setLong(3, f.lastModified());
        ps.setString(4, digest);
        ps.executeUpdate();
        ps.close();
        
    }

    private boolean alreadyInDB(File f) throws SQLException, DerbyException
    {
        PreparedStatement ps = null;
        try
        {
            Connection conn = db.getConnection();
            ps = conn.prepareStatement(CHECK_FILE_QUERY);

            ps.setString(1, getStandardPath(f));
            ResultSet rs = ps.executeQuery();
            boolean ret = rs.next();
            
            if(ret)
                System.out.println("Already in DB: " + f);
            else
                System.out.println("Added " + f);
            return ret;
        }
        finally
        {
            if(ps != null)
                ps.close();
        }
    }

    private static String getStandardPath(File f)
    {
        return FileUtils.safeGetCanonicalPath(f).replace('\\', '/');
    }

    private File renameIfNameTooLong(File f)
    {
        String name = f.getName();
        
        if(name.length() >= LONGEST_NAME)
        {
            name = name.substring(0, LONGEST_NAME - 10);
            String random = "" + System.nanoTime();
            int ranlen = random.length();
            name += random.substring(ranlen - 9);
            File newFile = new File(f.getParentFile(), name);
            System.out.printf("*** RENAME:\nOLD: %s\nNEW: %s\n", f.getPath(), newFile.getPath());
            boolean b = f.renameTo(newFile);
            System.out.println("RENAME RESULT: " + b);
            return newFile;
        }
        return f;
    }
    
    private String root;
    private final FileDB db;
    private static final int LONGEST_NAME = 100;
    private static final String ADD_FILE_QUERY = 
        "insert into " + FileDB.FILEINFO_TABLE + "(NAME,PATH,DATE,DIGEST) " +
        "values (?, ?, ?,?)";
    private static final String CHECK_FILE_QUERY = 
        "select path from " + FileDB.FILEINFO_TABLE + " where path = ?";
}
