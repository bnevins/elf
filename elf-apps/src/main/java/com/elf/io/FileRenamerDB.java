/*
 * FileRenamerDB.java
 *
 * Created on December 14, 2007, 1:30 AM
 *
 */

package com.elf.io;

import com.elf.db.DerbyException;
import com.elf.util.StringUtils;
import java.io.*;
import java.sql.*;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * @author bnevins
 */
public class FileRenamerDB
{
    public static void main(String[] args)
    {
        if(args.length < 1)
        {
            System.out.println("Usage FileRenamerDB root-dir [script-file]");
            System.exit(1);
        }
        try
        {
            FileRenamerDB fr = new FileRenamerDB(args[0]);
            if(args.length >= 2)
                fr.rename(args[1]);
            else
                fr.rename();
        } 
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public FileRenamerDB(String rootString) throws IOException, SQLException, DerbyException
    {
        root = FileUtils.safeGetCanonicalPath(new File(rootString));
        db = new FileDB();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    public void rename() throws IOException, SQLException, DerbyException 
    {
        rename(null);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    public void rename(String script) throws IOException, SQLException, DerbyException 
    {
        List<FileInfo> infos = getRenameInfo();
        PrintStream out;
        
        checkDB(infos);
        
        
        if(script != null)
            out = new PrintStream(new File(script));
        else
            out = System.out;
        
        for(FileInfo info : infos)
        {
            if(info.nef != null)
            {
                if(!info.nef.equals(info.newNef))
                {
                    out.println("mv " + info.nef + "   " + info.newNef);

                    if(script == null)
                        info.nef.renameTo(info.newNef);
                    
                    updateDB(info.nef, info.newNef);
                }
            }
            
            if(info.jpg != null)
            {
                if(!info.jpg.equals(info.newJpg))
                {
                    out.println("mv " + info.jpg + "   " + info.newJpg);

                    if(script == null)
                        info.jpg.renameTo(info.newJpg);

                    updateDB(info.jpg, info.newJpg);
                }
            }
        }
        
        if(script != null)
            out.close();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    private List<FileInfo> getRenameInfo() throws IOException 
    {
        List<File> nefs = getNefs();
        List<File> jpgs = getJpgs();
        System.out.println("#JPEGS BEFORE: " + jpgs.size());
        if(nefs.isEmpty() && jpgs.isEmpty())
            return new ArrayList<FileInfo>();
        
        // sort by time...
        Collections.sort(nefs, new TimeComparator());
        Collections.sort(jpgs, new TimeComparator());

        // associate the jpg, if any, with the nef and figure out the new names
        List<FileInfo> infos = getFileInfos(nefs, jpgs);

        return infos;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    private List<FileInfo> getFileInfos(List<File> nefs, List<File> jpgs)
    {
        List<FileInfo> infos = new ArrayList<FileInfo>(nefs.size());
        
        for(File nef : nefs)
        {
            File nefParent = nef.getParentFile();
            File jpg = findJpg(nef, jpgs); // might be null
            infos.add(new FileInfo(nef, jpg, null, null));
        }

        // note that all of the jpgs that have a matched nef file -- were removed 
        // from the jpg list.  These are all jpgs with no associated nef
        for(File jpg : jpgs)
        {
            infos.add(new FileInfo(null, jpg, null, null));
        }

        System.out.println("#JPEGS AFTER: " + jpgs.size());

        Collections.sort(infos, new TimeComparator2());
        
        // setup the rename-to filenames
        int fileCounter = 1;
        for(FileInfo info : infos)
        {
            String newNameRoot = PREFIX + StringUtils.padLeft(Integer.toString(fileCounter++), NUMBER_LENGTH, '0'); 
            
            if(info.nef != null)
            {
                File nefParent = info.nef.getParentFile();
                info.newNef = new File(nefParent, newNameRoot + NEF);
            }
            
            if(info.jpg != null)
            {
                File jpgParent = info.jpg.getParentFile();
                info.newJpg = new File(jpgParent, newNameRoot + JPG);
            }
        }
        
        return infos;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    private List<File> getNefs() throws IOException 
    {
        FileFinder ff = new FileFinder(root, new FileExtFilter(NEF));
        return ff.getFiles();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    private List<File> getJpgs() throws IOException 
    {
        FileFinder ff = new FileFinder(root, new FileExtFilter(JPG));
        return ff.getFiles();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    private File findJpg(File nef, List<File> jpgs)
    {
        // TODO very inefficient -- but I'm not in a hurry!!!
        // the jpg file matches if:
        // 1. the jpg parent starts with the entire parent of the nef
        // 2. the jpg filename matches the nef filename (w/o the extension of course)

        String nefParent = nef.getParent();
        String nefName = getNameNoExtension(nef);
        
        for(Iterator<File> it = jpgs.iterator(); it.hasNext(); )
        {
            File jpg = it.next();
            String jpgParent = jpg.getParent();
            String jpgName = getNameNoExtension(jpg);
            
            if(jpgParent.startsWith(nefParent) && jpgName.equals(nefName))
            {
                // match!  Remove it from the list to save time on the next search
                it.remove();
                return jpg;
            }
        }
        return null;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    private String getNameNoExtension(File f)
    {
        String name = f.getName();
        int dot = name.lastIndexOf(".");
        
        if(dot < 0)
            return name;
        
        return name.substring(0, dot);
    }

    private void checkDB(List<FileInfo> infos) throws SQLException, DerbyException
    {
        for(FileInfo info : infos)
        {
            if(info.nef != null)
            {
                if(!info.nef.equals(info.newNef))
                {
                    if(!alreadyInDB(info.nef))
                        System.out.println("ERROR -- not in DB: " + info.nef);
                }
            }
            
            if(info.jpg != null)
            {
                if(!info.jpg.equals(info.newJpg))
                {
                    if(!alreadyInDB(info.jpg))
                        System.out.println("ERROR -- not in DB: " + info.jpg);
                }
            }
        }
    }

    private boolean alreadyInDB(File f) throws SQLException, DerbyException
    {
        PreparedStatement ps = null;
        try
        {
            Connection conn = db.getConnection();
            ps = conn.prepareStatement(CHECK_FILE_QUERY);
            String path = getStandardPath(f);
            System.out.println("Looking for: " + path);
            ps.setString(1, path);
            ResultSet rs = ps.executeQuery();
            boolean ret = rs.next();
            return ret;
        }
        finally
        {
            if(ps != null)
                ps.close();
        }
    }
    private void updateDB(File old, File neww) throws SQLException, DerbyException
    {
        PreparedStatement ps = null;
        try
        {
            Connection conn = db.getConnection();
            ps = conn.prepareStatement(UPDATE_FILE_QUERY);

            ps.setString(1, getStandardPath(neww));
            ps.setString(2, neww.getName());
            ps.setString(3, old.getName());
            ps.setString(4, getStandardPath(old));
            
            ps.executeUpdate();
        }
        finally
        {
            if(ps != null)
                ps.close();
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////

    private static String getStandardPath(File f)
    {
        return FileUtils.safeGetCanonicalPath(f).replace('\\', '/');
    }

    
    private final String root;
    private FileDB db;
    private static final String NEF = ".nef";
    private static final String JPG = ".jpg";
    private static final String PREFIX = "N";
    //private static final String NIKON_STRING = "dsc";
    private static final String PREFIX_LC = PREFIX.toLowerCase();
    private static final int NUMBER_LENGTH = 6; // e.g. pix #2187 --> 002187
    private static final String CHECK_FILE_QUERY = 
        "select path from " + FileDB.FILEINFO_TABLE + " where path = ?";
    private static final String UPDATE_FILE_QUERY = 
        "update fileinfo set path=?,name=?,original_name=? where path=?";

    
    ////////////////////////////////////////////////////////////////////////////
    
    //
    // CLASSES
    //
    
    ////////////////////////////////////////////////////////////////////////////
    
    private static class TimeComparator implements Comparator<File>
    {
        public int compare(File f1, File f2)
        {
            long timediff = f1.lastModified() - f2.lastModified();

            if(timediff > 0)
                return 1;
            if(timediff < 0)
                return -1;
            return 0;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    private static class TimeComparator2 implements Comparator<FileInfo>
    {
        public int compare(FileInfo fi1, FileInfo fi2)
        {
            long timediff = fi1.lastModified() - fi2.lastModified();

            if(timediff > 0)
                return 1;
            if(timediff < 0)
                return -1;
            return 0;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    private static class FileInfo
    {
        FileInfo(File nef, File jpg, File newNef, File newJpg)
        {
            this.nef    = nef;
            this.newNef = newNef;
            this.jpg    = jpg;
            this.newJpg = newJpg;
        }
        
        public long lastModified()
        {
            if(nef != null)
                return nef.lastModified();
            
            return jpg.lastModified();
        }
        
        private File nef;
        private File newNef;
        private File jpg;
        private File newJpg;
    }
}

