/*
 * HTTPDownloader.java
 *
 * Created on May 15, 2007, 8:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.net;

import java.io.*;
import java.net.*;

/**
 *
 * @author bnevins
 */
public class HTTPDownloader
{
    
    public HTTPDownloader(String urlString, String filename) throws IOException
    {
        target = new File(filename);
        File parent = target.getParentFile();
        
        if(parent != null)
            parent.mkdirs();
        
        URL url = new URL(urlString);
        urlc = url.openConnection();
        download();
    }
    
    private void download() throws IOException
    {
        BufferedInputStream		bis = null;
        BufferedOutputStream	bos = null;
        try
        {
            bis = new BufferedInputStream(urlc.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(target));
            final byte[] bytes = new byte[BUF_SIZE];
            int rb = 0;
            int cb = 0; //cumulative
            while((rb = bis.read(bytes)) != -1)
            {
                bos.write(bytes, 0, rb);
                cb += rb;
            }
            System.out.println("Wrote " + cb + " bytes to " + target);
        }
        finally
        {
            try
            {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }
            catch(Exception e)
            {
                // ignore
            }
        }
    }
    
    public static void main(String[] args)
    {
        String url = "http://abcrad.vo.llnwd.net/o1/kgo/kgo-13.mp3";
        String target = "C:/temp/edell/de.mp3";
        
        try
        {
            new HTTPDownloader(url, target);
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
    
    private final           File			target;
    private                 URLConnection	urlc;
    private final static    int             BUF_SIZE    = 1024 * 1024;
    
}
