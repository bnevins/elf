/*
 * Filer.java
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
public class Filer
{
    
    /**
     * Creates a new instance of Filer
     */
    public Filer()
    {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        
    }
    
    public static void foo()
    {
        try
        {
            File exe = new File("c:/temp/spit.exe");
            String src = "c:/temp/gfi.jar";
            File out = new File("C:/temp/fromspit.exe");
            File copy = new File("C:/temp/gfi_copied.jar");
            long exeLength = exe.length();
            System.out.println("len: " + exeLength);
            
            BufferedInputStream exeIn = new BufferedInputStream(new FileInputStream(exe));
            BufferedOutputStream outs = new BufferedOutputStream(new FileOutputStream(out));
            byte[] bytes = new byte[1048576];
            int numRead = 0;
            while((numRead = exeIn.read(bytes)) >= 0)
            {
                System.out.println("numRead: " + numRead);
                outs.write(bytes, 0, numRead);
            }
            // 
            exeIn.close();
            
            BufferedInputStream bigIn = new BufferedInputStream(new FileInputStream(src));
            numRead = 0;
            while((numRead = bigIn.read(bytes)) >= 0)
            {
                System.out.println("numRead: " + numRead);
                outs.write(bytes, 0, numRead);
            }
            
            bigIn.close();
            outs.close();
            // now split them up...
            
            BufferedInputStream hugeIn = new BufferedInputStream(new FileInputStream(out));
            outs = new BufferedOutputStream(new FileOutputStream(copy));
            
            hugeIn.skip(exeLength);
            
            numRead = 0;
            while((numRead = hugeIn.read(bytes)) >= 0)
            {
                System.out.println("writing copy: " + numRead);
                outs.write(bytes, 0, numRead);
            }
            
            hugeIn.close();
            outs.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        
        //RandomAccessFile
    }
}
