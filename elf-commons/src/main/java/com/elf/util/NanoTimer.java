/*
 * NanoTimer.java
 *
 * Created on December 24, 2007, 9:50 PM
 *
 */

package com.elf.util;

/**
 *
 * @author bnevins
 */
public class NanoTimer
{
    public void start()
    {
        start = System.nanoTime();
    }
    public void end()
    {
        end = System.nanoTime();
    }

    public double msec()
    {
        double nsec = end - start;
        return nsec / 1000000;
    }
    
    @Override
    public String toString()
    {
        return "" + msec() + " msec";
    }
    public static void main(String[] args)
    {
        NanoTimer nt = new NanoTimer();
        nt.start();
        try
        {
            Thread.sleep(2500);
        } 
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
        nt.end();
        System.out.println("Time: " + nt.msec() + " msec");
    }

    private long start;
    private long end;
}
