/*
 * FileRenamer.java
 *
 * Created on December 14, 2007, 1:30 AM
 *
 */

package com.elf.io;

import com.elf.util.StringUtils;
import java.io.*;
import java.util.*;

/**
 *  DOES NOT USE DB
 * Say you have files named: 1,2,3
 * Now you add a new file that is  2<NEW<3
 * the commands would be rename NEW to 3, rename 3 to 4.  Oops!  3 is already gone!
 * The only safe way to do this is to change the names twice.  E.g. N000001.nef --> Z000001.nef
 * then after ALL files are renamed, rename ALL of them again.
 * @author bnevins
 */

public class FileRenamer
{
    public static void main(String[] args)
    {
        if(args.length < 1)
        {
            //TODO Arg Processor
            System.out.println("Usage FileRenamer [--prefix xyz] root-dir [script-file]");
            System.exit(1);
        }
        try
        {
            int index = 0;

            if(args[0].equals("--prefix")) {
                FileInfo.setPrefix(args[1]);
                index += 2;
            }


            FileRenamer fr = new FileRenamer(args[index]);
            
            
            if( (args.length - index ) >= 2)
                fr.rename(args[1]);
            else
                fr.rename();
        } 
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public FileRenamer(String rootString)
    {
        root = FileUtils.safeGetCanonicalPath(new File(rootString));
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    public void rename() throws IOException
    {
        rename(null);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    public void rename(String script) throws IOException
    {
        List<FileInfo> infos = getRenameInfo();
        PrintStream out;
        
        boolean changed = false;

        for(FileInfo info : infos)
        {
            if(info.hasChanged())
            {
                changed = true; 
                break;
            }
        }
        
        if(!changed)
        {
            System.out.println("NO CHANGES NEEDED!");
            return;
        }
        
        if(script != null)
            out = new PrintStream(new File(script));
        else
            out = null;
        
        
        

        for(FileInfo info : infos)
        {
            info.renameToTemp(out);
        }

        for(FileInfo info : infos)
        {
            info.renameToFinal(out);
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
            File jpg = findJpg(nef, jpgs); // might be null
            infos.add(new FileInfo(nef, jpg));
        }

        // note that all of the jpgs that have a matched nef file -- were removed 
        // from the jpg list.  These are all jpgs with no associated nef
        for(File jpg : jpgs)
        {
            infos.add(new FileInfo(null, jpg));
        }

        System.out.println("#JPEGS AFTER: " + jpgs.size());

        Collections.sort(infos, new TimeComparator2());
        
        // setup the rename-to filenames
        int fileCounter = 1;
        for(FileInfo info : infos)
        {
            info.setNewNum(fileCounter++);
        }
        
        return infos;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    private List<File> getNefs() throws IOException 
    {
        FileFinder ff = new FileFinder(root, new FileExtFilter(FileInfo.NEF));
        return ff.getFiles();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    private List<File> getJpgs() throws IOException 
    {
        FileFinder ff = new FileFinder(root, new FileExtFilter(FileInfo.JPG));
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

    ////////////////////////////////////////////////////////////////////////////

    private static String getStandardPath(File f)
    {
        return FileUtils.safeGetCanonicalPath(f).replace('\\', '/');
    }

    
    private final String root;
    private FileDB db;
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

}

