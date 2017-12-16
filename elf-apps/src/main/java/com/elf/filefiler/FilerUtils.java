/*
 * FilerUtils.java
 *
 * Created on December 25, 2006, 12:05 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.filefiler;

import java.io.*;

/**
 * Note: Multi-threaded hostile!
 * @author bnevins
 */
public class FilerUtils
{
    static void writeBytes(InputStream is, OutputStream os) throws IOException
    {
        int numRead = 0;

        while((numRead = is.read(buffer)) >= 0)
        {
            System.out.println("numRead: " + numRead);
            os.write(buffer, 0, numRead);
        }
    }

    static void writeBytes(InputStream is, OutputStream os, final long numBytes) throws IOException
    {
        final int bufsize = buffer.length;
        int totalNumRead = 0;
        int numRead = 0;
        long numToRead = numBytes > bufsize ? bufsize : numBytes;

        while((numRead = is.read(buffer, 0, (int)numToRead)) > 0)
        {
            totalNumRead += numRead;
            System.out.println("numRead: " + numRead);
            os.write(buffer, 0, numRead);
            numToRead = numBytes - totalNumRead;

            if(numToRead > bufsize)
                numToRead = bufsize;
        }
    }
    
    private static byte[] buffer = new byte[1000000];
}
