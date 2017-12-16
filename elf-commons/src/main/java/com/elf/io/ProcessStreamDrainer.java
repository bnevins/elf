/*
 * ProcessStreamDrainer.java
 *
 * Created on October 26, 2006, 9:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.io;

import java.io.*;
import java.util.*;

/**
 * If you don't drain a process' stdout and stderr it will cause a deadlock after a few hundred bytes of output.  
 * At that point the Process is blocked because its stdout and/or stderr buffer is full and it is waiting for the Java caller
 * to drain it.  Meanwhile the Java program is blocked waiting on the external process.
 * This class makes this common, but messy and tricky, procedure easier.
 * It creates 2 threads that drain output on stdout and stderr of the external process.  THe output is automatically
 * collected into Strings that are available with a method call.  You can also attach any number of OutputStreams or Files
 * to receive the process output. The OutputStreams are especially handy for long-running external processes.
 * <p> Sample Code:
 *        
 * <pre>
 *      ProcessBuilder pb = new ProcessBuilder("ls",  "-R", "c:/as");
        try
        {
            Process p = pb.start();
            try
            {
                ProcessStreamDrainer psd = new ProcessStreamDrainer(p);
                //psd.addOutputAndErrorEavesdropper(new FileOutputStream(new File("C:/temp/ls-both.txt")));
                //psd.addOutputEavesdropper(new File("C:/temp/ls-out1.txt"), new File("C:/temp/ls-out2.txt"));
                psd.addOutputAndErrorEavesdropper(System.out);
                psd.drain();
                psd.waitFor();
                System.out.println(psd.getOut());
            } catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
        } 
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
 </pre>

 * @author bnevins
 */
public class ProcessStreamDrainer
{
    /**
     * Create an instance.
     * @param process Drain output from this Process
     */
    public ProcessStreamDrainer(Process process)
    {
        if(process == null)
            throw new NullPointerException("Internal Error: null Process object");
        
        this.process = process;
    }
    
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Attach one or more OutputStreams to the stdout of the external process
     * @param outstreams 
     */
    public void addOutputEavesdropper(OutputStream... outstreams)
    {
        for(OutputStream out : outstreams)
        {
            if(out instanceof PrintStream)
                outs.add((PrintStream)out);
            else
                outs.add(new PrintStream(out));
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Attach one or more OutputStreams to the stderr of the external process
     * @param errstreams 
     */
    public void addErrorEavesdropper(OutputStream... errstreams)
    {
        for(OutputStream err : errstreams)
        {
            if(err instanceof PrintStream)
                errs.add((PrintStream)err);
            else
                errs.add(new PrintStream(err));
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Attach one or more OutputStreams to a blending of stdout and stderr of the external process
     * @param bothstreams 
     */
    public void addOutputAndErrorEavesdropper(OutputStream... bothstreams)
    {
        for(OutputStream both : bothstreams)
        {
            if(both instanceof PrintStream)
                boths.add((PrintStream)both);
            else
                boths.add(new PrintStream(both));
        }
    }

    /**
     * Get the stdout of the external process as a String
     */
    public final String getOut()
    {
        return outSB.toString();
    }
    
    ///////////////////////////////////////////////////////////////////////////
    
    /**
     * Get the stderr of the external process as a String
     */
    public final String getErr()
    {
        return errSB.toString();
    }
    
    ///////////////////////////////////////////////////////////////////////////
    
    /**
     * Get the blended stdout and stderr of the external process as a String
     */
    public final String getOutAndErr()
    {
        return outSB.toString() + errSB.toString();
    }
    
    ///////////////////////////////////////////////////////////////////////////
    
    /**
     * Wait for the drain threads to die.  This is guaranteed to occur after the external process dies.  Note that this may,
     * of course, block indefinitely.
     */
    public final void waitFor() throws InterruptedException
    {
        if(errThread == null)
            drain();
        
        errThread.join();
        outThread.join();
    }
    
    ///////////////////////////////////////////////////////////////////////////
    
    /**
     * Start the draining.  Call this after constructing and setting up streams (if any)
     */
    public final void drain()
    {
        outs.addAll(boths);
        errs.addAll(boths);
        
        out = new ProcessStreamDrainerWorker(process.getInputStream(), outs, outSB);
        outThread = new Thread(out, OUT_DRAINER);
        outThread.setDaemon(true);
        outThread.start();

        err = new ProcessStreamDrainerWorker(process.getErrorStream(), errs, errSB);
        errThread = new Thread(err, ERROR_DRAINER);
        errThread.setDaemon(true);
        errThread.start();
    }
 
    ///////////////////////////////////////////////////////////////////////////
    
    private Process                     process;
    private ProcessStreamDrainerWorker  err;
    private ProcessStreamDrainerWorker  out;
    private Thread                      errThread;
    private Thread                      outThread;
    private ArrayList<PrintStream>      outs = new ArrayList<PrintStream>();
    private ArrayList<PrintStream>      errs = new ArrayList<PrintStream>();
    private ArrayList<PrintStream>      boths = new ArrayList<PrintStream>();
    private StringBuilder               outSB = new StringBuilder();
    private StringBuilder               errSB = new StringBuilder();
    private final static String         ERROR_DRAINER   = "StderrDrainer";
    private final static String         OUT_DRAINER     = "StdoutDrainer";
    
    ///////////////////////////////////////////////////////////////////////////
    
    private static class ProcessStreamDrainerWorker implements Runnable
    {
        ProcessStreamDrainerWorker(InputStream in, ArrayList<PrintStream> outs, StringBuilder theSB)
        {
            if(in == null)
                return;
            
            reader = new BufferedInputStream(in);
            sb = theSB;
            writers = outs;
        }

        public void run()
        {
            if(reader == null)
                return;

            try
            {
                int count=0;
                byte[] buffer = new byte[4096];

                while((count = reader.read(buffer)) != -1)
                {
                    sb.append(new String(buffer, 0, count));
                    
                    for(PrintStream pw : writers)
                        pw.write(buffer, 0, count);
                }
            }
            catch (IOException e)
            {
            }
            finally
            {
                try
                {
                    reader.close();
                }
                catch(Exception e)
                {
                    // nothing to do...
                }
            }
        }
        
        private BufferedInputStream     reader;
        private StringBuilder           sb;
        private ArrayList<PrintStream>  writers;
    }
 }

