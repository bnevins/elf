/*
 * Toucher.java
 *
 * Created on December 25, 2007, 2:01 PM
 *
 */

package com.elf.io;

import java.io.*;

/**
 *
 * @author bnevins
 */
public class Toucher
{
    public static void main(String[] args)
    {
        if(args.length == 2)
        {
            try
            {
                new Toucher(args[0], args[1]);
            } 
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
            System.out.println("Usage: file-with-filenames date\n" +
                "Date e.g. ==> 19560123");
    }

    public Toucher(String fileoffiles, String date) throws FileNotFoundException, IOException
    {
        BufferedReader r = new BufferedReader(new FileReader(fileoffiles));
        String fn;
        ProcessBuilder pb = new ProcessBuilder();
        int minutes = 0;
        int hours = 1;
        
        while((fn = r.readLine()) != null)
        {
            // start at 12:01 AM, add 1 minute per file. this will make it obvious
            // we used Toucher...
            if(fn.length() == 0)
                continue;
            
            if(!new File(fn).exists())
            {
                System.out.println(fn + " does not exist.");
                continue;
            }
            
            ++minutes;
            if(minutes == 60)
            {
                minutes = 0;
                ++hours;
            }
            String time = getTime(hours, minutes);
            
            pb.command(TOUCH, "-t", date + time, fn);
            System.out.println(pb.command());
            pb.start();
        }
    }
    

    private String getTime(int h, int m)
    {
        String hh, mm;
        if(h < 10)
            hh = "0" + h;
        else
            hh = "" + h;
        
        if(m < 10)
            mm = "0" + m;
        else
            mm = "" + m;
        
        return hh + mm;
    }
    
    private static final String TOUCH = "C:/DEV/MKS/mksnt/touch.exe";
}
