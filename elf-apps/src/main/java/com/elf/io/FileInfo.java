package com.elf.io;

import com.elf.io.FileInfo;
import com.elf.util.StringUtils;
import java.io.File;
import java.io.PrintStream;


////////////////////////////////////////////////////////////////////////////

class FileInfo
{
    FileInfo(File nef, File jpg)
    {
        this.nef    = nef;
        this.jpg    = jpg;

    }

    public static void setPrefix(String newPrefix)
    {
        if(StringUtils.ok(newPrefix))
            prefix = newPrefix;
    }
    
    public long lastModified()
    {
        if(nef != null)
            return nef.lastModified();
        
        return jpg.lastModified();
    }

    @Override
    public String toString()
    {
        return("nef: " + nef + "\n"
            + "newNef: " + newNef + "\n" 
            + "tempNewNef: " + tempNewNef + "\n" 
            + "jpg: " + jpg + "\n"
            + "newJpg: " + newJpg + "\n"
            + "tempNewJpg: " + tempNewJpg + "\n"
            + "changed: " + changed);
    }
    
    void setNewNum(int num)
    {
        newNum = num;
        setOldNum();
    
        if(oldNum == newNum)
            changed = false;
        else
            changed = true;
        
        String numPart = StringUtils.padLeft(Integer.toString(newNum), NUMBER_LENGTH, '0'); 
        String newNameRoot = prefix + numPart;
        String tempNewNameRoot = TEMP_PREFIX + numPart;
        
        if (nef != null)
        {
            File nefParent = nef.getParentFile();
            newNef = new File(nefParent, newNameRoot + NEF);
            tempNewNef = new File(nefParent, tempNewNameRoot + NEF);
        }
        
        if (jpg != null)
        {
            File jpgParent = jpg.getParentFile();
            newJpg = new File(jpgParent, newNameRoot + JPG);
            tempNewJpg = new File(jpgParent, tempNewNameRoot + JPG);
        }
    }

    boolean hasChanged()
    {
        if(oldNum != newNum)
            return true;

        if(nef != null && !nef.equals(newNef))
        {
            return true;
        }
        if(jpg != null && !jpg.equals(newJpg))
        {
            return true;
        }
        
        return false;
    }
    void renameToTemp(PrintStream out)
    {
        if(nef != null)
        {
            if(out != null)
                out.println("mv \"" + nef + "\"   \"" + tempNewNef + "\"");
            else
                nef.renameTo(tempNewNef);
        }
        if(jpg != null)
        {
            if(out != null)
                out.println("mv \"" + jpg + "\"   \"" + tempNewJpg + "\"");
            else
                jpg.renameTo(tempNewJpg);
        }
    }
    
    void renameToFinal(PrintStream out)
    {
        if(nef != null)
        {
            if(out != null)
                out.println("mv \"" + tempNewNef + "\"   \"" + newNef + "\"");
            else
                tempNewNef.renameTo(newNef);
        }
        if(jpg != null)
        {
            if(out != null)
                out.println("mv \"" + tempNewJpg + "\"   \"" + newJpg + "\"");
            else
                tempNewJpg.renameTo(newJpg);
        }
    }
    private void setOldNum()
    {
        String name;
        
        if(nef != null)
            name = nef.getName();
        else
            name = jpg.getName();
        
        // if name doesn't match our pattern then oldnum = -1

        if(!name.startsWith(prefix))
            return;
        
        name = getRootNoPrefix(name);
        
        try
        {
            oldNum = Integer.parseInt(name);
        }
        catch(Exception e)
        {
            // ignore -- oldnum is already -1
        }
    }

    private String getRootNoPrefix(String name)
    {
        int index = name.indexOf('.');
        
        if(index < 0)
            return null;
        
        return name.substring(prefix.length(), index);
    }
    
    private File nef;
    private File tempNewNef;
    private File newNef;
    private File jpg;
    private File tempNewJpg;
    private File newJpg;
    private int oldNum = -1;
    private int newNum;
    private boolean changed;
    public static final String NEF = ".nef";
    public static final String JPG = ".jpg";
    private static final String DEFAULT_PREFIX = "N";
    private static final String TEMP_PREFIX = "TEMP_";
    private static final int NUMBER_LENGTH = 6; // e.g. pix #2187 --> 002187
    private static String  prefix = DEFAULT_PREFIX;

    public static void main(String[] args)
    {
        File f = new File("D:/data/Pix/Nikon/0710/NN001479.nef");
        
        FileInfo fii = new FileInfo(f, null);
        fii.setNewNum(1479);
        System.out.println(fii);
    }
}

