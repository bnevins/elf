/*
 * Grep.java
 *
 * Created on Oct 20, 2007, 9:33:12 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.elf.io;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.regex.*;

/**
 *
 * @author bnevins
 */
public class Grep
{
    // Compile the pattern
    //
    public void setPattern(String pat) throws PatternSyntaxException
    {
        pattern = Pattern.compile(pat);
    }


    // Search for occurrences of the input pattern in the given file
    //
    public void grep(File f) throws IOException
    {

        // Open the file and then get a channel from the stream
        FileInputStream fis = new FileInputStream(f);
        FileChannel fc = fis.getChannel();

        // Get the file's size and then map it into memory
        int sz = (int) fc.size();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);

        // Decode the file into a char buffer
        CharBuffer cb = decoder.decode(bb);

        // Perform the search
        grep(f, cb);

        // Close the channel and the stream
        fc.close();
    }

    // Use the linePattern to break the given CharBuffer into lines, applying
    // the input pattern to each line to see if we have a match
    //
    private void grep(File f, CharBuffer cb)
    {
        Matcher lm = linePattern.matcher(cb); // Line matcher
        Matcher pm = null; // Pattern matcher
        int lines = 0;
        while (lm.find())
        {
            lines++;
            CharSequence cs = lm.group(); // The current line

            if (pm == null)
                pm = pattern.matcher(cs);
            else
                pm.reset(cs);
            
            if (pm.find())
            {
                System.out.print(f + ":" + lines + ":" + cs);
            }
            if (lm.end() == cb.limit())
            {
                break;
            }
        }
    }

    public static void main(String[] args)
    {
        if (args.length < 2)
        {
            System.err.println("Usage: java Grep pattern file...");
            return;
        }
        
        Grep grepper = new Grep();
        
        grepper.setPattern(args[0]);
        for (int i = 1; i < args.length; i++)
        {
            File f = new File(args[i]);
            try
            {
                grepper.grep(f);
            } 
            catch (IOException x)
            {
                System.err.println(f + ": " + x);
            }
        }
    }
    private static Charset charset = Charset.forName("ISO-8859-15");
    private static CharsetDecoder decoder = charset.newDecoder();
    private static Pattern linePattern = Pattern.compile(".*\r?\n");
    private Pattern pattern;

}