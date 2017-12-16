/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.elf.io;

import java.io.*;
import java.util.*;
import java.util.ArrayList;


/**
 *
 * @author bnevins
 */
public class Unix2Dos 
{
    private boolean unix;
    private boolean dos;
    public Unix2Dos(String... args) throws FileNotFoundException, IOException 
    {
        files = new ArrayList<File>();
        
        for(String arg : args)
        {
            if(arg.equals("-unix"))
                unix = true;
            else if(arg.equals("-dos"))
                dos = true;
            else files.add(new File(arg));
        }
        
        for(File f : files)
        {
            boolean isDos = false;
            boolean isUnix = false;
            String s = read(f);
            if(s.indexOf("\r\n") >= 0)
            {
                isDos = true;
                if(!dos && !unix)
                    System.out.println(f + ": " + "DOS");
            }
            else if(s.indexOf("\n") >= 0)
            {
                isUnix = true;
                if(!dos && !unix)
                    System.out.println(f + ": " + "UNIX");
            }
            else
                if(!dos && !unix)
                    System.out.println(f + ": " + "????");
            
            if(unix || dos)
            {
                if(unix && isDos)
                {
                    toUnix(f, s);
                }
                else if(dos && isUnix)
                {
                    toDos(f, s);
                }
                else
                    System.out.println(f + ": No changes");
            }
        }
    }

    private void toDos(File f, String s) throws IOException 
    {
        String[] ss = s.split("\n");
        write(f, ss);
        //System.out.println(f + ": converted to DOS " );
    }

    private void toUnix(File f, String s) throws IOException 
    {
        String[] ss = s.split("\r\n");
        write(f, ss);
        //System.out.println(f + ": converted to UNIX");
    }

    private void write(File f, String[] ss) throws IOException
    {
        String path = f.getAbsolutePath();
        File before = new File(path + ".before");
        if(!f.renameTo(before))
        {
            System.out.println("Can't rename " + f);
            return;
        }
        
        FileWriter writer = new FileWriter(f);
        
        for(String s : ss)
        {
            String line = s + (dos ? "\r\n" : "\n");
            char[] buf = line.toCharArray();
            writer.write(buf, 0, buf.length);
        }
        writer.close();
        System.out.println(f + ": Converted to " + (dos ? "DOS" : "UNIX") +
                " before: " + before.length() + ", after: " + f.length());
    }
    
    private String read(File f) throws FileNotFoundException, IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(f));
        char[] buf = new char[10000];
        StringBuilder sb = new StringBuilder();
        int num;
        int offset = 0;
        
        while((num = br.read(buf, 0, buf.length)) >= 0)
        {
            sb.append(buf, 0, num);
        }
        br.close();
        String s = sb.toString();
        return s;
    }
    
    private List<String> readLines(File f) throws FileNotFoundException, IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(f));
        List<String> ss = new ArrayList<String>();
        String line;
        
        while((line = br.readLine()) != null)
            ss.add(line);
        
        br.close();
        return ss;
    }
    
    private void write(List<String> ss)
    {
    }
    
    public static void main(String[] args) {
        try
        {
            new Unix2Dos(args);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private ArrayList<File> files;
}
