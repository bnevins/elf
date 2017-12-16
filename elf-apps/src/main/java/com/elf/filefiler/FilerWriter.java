/*
 * FilerWriter.java
 *
 * Created on December 24, 2006, 11:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.filefiler;

import java.io.*;
import java.util.*;

/**
 *
 * @author bnevins
 */
public class FilerWriter
{
    
    /**
     * Creates a new instance of FilerWriter
     */
    public FilerWriter(BootstrapperInfo bootstrapperInfo, FilerInfo... infos)
    {
        this.infos = infos;
        this.bootstrapperInfo = bootstrapperInfo;
    }
    
    public void write() throws FilerException
    {
        try
        {
            initialize();
            copyBootstrap();
            
            for(FilerInfo fi : infos)
                append(fi);
            
        }
        catch(Exception e)
        {
            throw new FilerException(e);
        }
        finally
        {
            try { exeIn.close(); } catch(Exception e) { }
            try { bigExeOut.close(); } catch(Exception e) { }
            
            for(FilerInfo fi : infos)
            {
                try { fi.in.close(); } catch(Exception e) { }
            }
        }
    }
    
    
    private void initialize() throws IOException
    {
        exeIn = new BufferedInputStream(new FileInputStream(bootstrapperInfo.bootstrapperFile));
        bigExeOut = new BufferedOutputStream(new FileOutputStream(bootstrapperInfo.filed));
    }
    
    private void copyBootstrap() throws IOException
    {
        FilerUtils.writeBytes(exeIn, bigExeOut);
    }
    
    private void append(FilerInfo fi) throws IOException
    {
        bigExeOut.write(fi.filenameBytes, 0, fi.filenameBytes.length);
        bigExeOut.write(fi.lengthString, 0, fi.lengthString.length);
        FilerUtils.writeBytes(fi.in, bigExeOut);
    }
    
    private FilerInfo[] infos;
    private BootstrapperInfo bootstrapperInfo;
    private BufferedInputStream exeIn;
    private BufferedOutputStream bigExeOut;
    
    public static void main(String[] args)
    {
        try
        {
            File exe = new File("c:/temp/spit.exe");
            File toAppend = new File("c:/temp/gfi.jar");
            File out = new File("C:/temp/fromspit.exe");
            
            BootstrapperInfo bsi = new BootstrapperInfo(exe, out);
            FilerInfo fi = new FilerInfo(toAppend);
            FilerWriter fw = new FilerWriter(bsi, fi);
            fw.write();
        
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
}

}
