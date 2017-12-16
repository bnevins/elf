/*
 * FilerInfo.java
 *
 * Created on December 24, 2006, 10:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.filefiler;

import java.io.*;

/**
 *
 * @author bnevins
 */
public class FilerInfo
{
    
    /** Creates a new instance of FilerInfo */
    public FilerInfo(File file)
    {
        try
        {
            length = file.length();
            byte[] lengthBytes = Long.toString(length).getBytes();
            byte[] fn = file.getName().getBytes();
            System.arraycopy(fn, 0, filename, 0, fn.length);
            System.arraycopy(lengthBytes, 0, lengthString, 0, lengthBytes.length);
            in = new BufferedInputStream(new FileInputStream(file));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public FilerInfo(InputStream is) throws IOException
    {
        is.read(filenameBytes, 0, filenameBytes.length);
        is.read(lengthString, 0, lengthString.length);
        length = Long.valueOf(new String(lengthString).trim());
        //length = Long.valueOf(new String(lengthString, 0, indexFirstZero));
        filename = new String(filenameBytes).trim();
        
        System.out.println("Filename: " + filename + ", Length: " + length);
    }
    
    byte[] filenameBytes = new byte[1000];
    long    length;
    byte[] lengthString = new byte[20];
    BufferedInputStream in;
    String filename;
}
