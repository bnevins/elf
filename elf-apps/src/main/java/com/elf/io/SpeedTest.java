/*
 * Copyright (c) 2012-2013 Elf Industries
 */
package com.elf.io;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bnevins
 */
public class SpeedTest {
    public static void main(String[] args) {
        if (args.length < 2) {
            usage();
        }

        File staticSourceFile = new File(args[0]);
        new SpeedTest(staticSourceFile, null);
        for (int i = 1; i < args.length; i++) {
            new SpeedTest(staticSourceFile, new File(args[i]));
        }
    }

    private static void usage() {
        System.out.printf("java com.elf.io.SpeedTest sourcefile path1 path2 ... pathn\n");
        System.exit(1);
    }
    private File outdir;
    private File hugeReceiver;
    private File sourceFile;

    SpeedTest(File sourceFile, File outdir) {
        try {
            this.outdir = outdir;
            this.sourceFile = sourceFile;

            if (outdir == null) {
                hugeReceiver = null;
            }
            else  {
                hugeReceiver = File.createTempFile("speedtest_", null, outdir);
                hugeReceiver.deleteOnExit();
            }
            
            long nsec = timer();
            long usec = nsec / 1000;
            int msec = (int) (usec / 1000);
            //System.out.println("XXXXX " + outdir + " -- Time in Nanoseconds = " + nsec);
            long len = sourceFile.length();
            double megaBps = len / usec;  // bytes per usec == MBytes/sec!
            double megabps = megaBps * 8;
            System.out.printf("%s copied to %s, total bytes = %d, msec = %d, %f mbps, %f mBps\n", 
                    sourceFile, hugeReceiver, len, msec, megabps, megaBps);
            //System.out.println("XXXXX " + outdir + " -- Time in milliseconds = " + msec);
        }
        catch (IOException ex) {
            Logger.getLogger(SpeedTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private long timer() throws IOException {
        long nsec = System.nanoTime();
        FileUtils.copyFile(sourceFile, hugeReceiver);
        nsec = System.nanoTime() - nsec;
        return nsec;
        //return nsec /(1000 * 1000); // milliseconds now
    }
    private String format() {
        return "";
    }
}
