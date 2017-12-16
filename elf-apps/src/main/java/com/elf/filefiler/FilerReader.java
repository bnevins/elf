/*
 * FilerReader.java
 *
 * Created on December 25, 2006, 12:42 AM
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
public class FilerReader
{
    public FilerReader(File bigExeFile)
    {
        this.bigExeFile = bigExeFile;
    }
    
    public void read() throws IOException
    {
        initialize();
        // skip over the exe part of the file.
        is.skip(BOOTSTRAPPERLENGTH);

        FilerInfo fi = new FilerInfo(is);
//        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(new File(fi.filename)));
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(new File("C:/temp/copiedfile.xyz")));
        FilerUtils.writeBytes(is, os, fi.length);
        os.close();
        is.close();
    }

    private void initialize() throws IOException
    {
        is = new BufferedInputStream(new FileInputStream(bigExeFile));
    }
    private File bigExeFile;
    private BufferedInputStream is;
    private static final int BOOTSTRAPPERLENGTH = 151552;
    
    public static void main(String[] args)
    {
        try
        {
            //File exe = new File("c:/temp/spit.exe");
            //File toAppend = new File("c:/temp/gfi.jar");
            File inFile = new File("C:/temp/fromspit.exe");
            FilerReader fr = new FilerReader(inFile);
            fr.read();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
}
