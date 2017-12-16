/*
 * FileExtFilter.java
 *
 * Created on December 16, 2007, 11:00 AM
 *
 */

package com.elf.io;

import java.io.*;

/**
 *
 * @author bnevins
 */
public class FileExtFilter implements FileFilter
{
    public FileExtFilter(String ex)
    {
        // make sure it starts with "."
        if(ex.startsWith("."))
            ext = ex;
        else
            ext = "." + ex;
    }

    public boolean accept(File f)
    {
        if(f.isDirectory())
            return true;

        String name = f.getName().toLowerCase();

        if(name.endsWith(ext))
            return true;

        return false;
    }
    
    private final String ext;
}
