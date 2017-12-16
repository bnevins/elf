/*
 * FileDigester.java
 *
 * Created on September 16, 2007, 4:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.elf.io;

import java.io.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author bnevins
 */
public class FileDigester {

    public FileDigester(String filename) throws NoSuchAlgorithmException {
        this.filename = filename;
    }

    public byte[] getDigest() {
        byte[] bytes = loadBytes();
        return computeDigest(bytes);
    }

    public String getDigestString() {
        return getHumanReadable(getDigest(), true);
    }

    public String getDigestStringCompact() {
        return getHumanReadable(getDigest(), false);
    }

    /**
     * *
     * This uses too much memory!
     *
     * private byte[] loadBytes() { BufferedInputStream in = null;
     *
     * try { in = new BufferedInputStream(new FileInputStream(filename),
     * BUFFER_SIZE); ByteArrayOutputStream out = new ByteArrayOutputStream();
     * byte[] buf = new byte[BUFFER_SIZE]; int len;
     *
     * while((len = in.read(buf, 0, buf.length)) >= 0) { out.write(buf, 0, len);
     * }
     *
     * return out.toByteArray(); } catch (IOException e) { return null; }
     * finally { if (in != null) { try { in.close(); } catch (IOException e2) {
     * } } } } ***
     */
    private byte[] loadBytes() {
        InputStream in = null;

        try {
            in = new FileInputStream(filename);
            int total = (int) new File(filename).length();

            // If it's too big the JVM may run out of memory.
            // Just use the first 50 MB of the file in that case...
            if (total > MAX_FILE_SIZE) {
                total = MAX_FILE_SIZE;
            }

            byte[] buf = new byte[total];

            int numRead = 0;
            int numToRead = total;
            int howmany;

            while ((howmany = in.read(buf, numRead, numToRead)) > 0) {
                numRead += howmany;
                numToRead = total - numRead;
            }
            return buf;
        } catch (Throwable t) {
            System.out.println("Error: " + t);
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e2) {
                }
            }
        }
    }

    private byte[] computeDigest(byte[] b) {
        if (b == null) {
            return null;
        }

        digester.reset();
        digester.update(b);
        return digester.digest();
    }

    private String getHumanReadable(byte[] hash, boolean useSpaces) {
        if (hash == null) {
            return "FFFFFFFFFF";
        }

        String d = "";
        for (int i = 0; i < hash.length; i++) {
            int v = hash[i] & 0xFF;
            if (v < 16) {
                d += "0";
            }
            d += Integer.toString(v, 16).toUpperCase();

            if (useSpaces) {
                d += " ";
            }
        }

        return d;
    }

    private static void usage() {
        System.out.println("USAGE: FileDigester file1 file2 ....");
        System.out.println("USAGE: FileDigester --compare MD5-string filepath");
    }

    public static void main(String[] args) {

        try {
            if (args.length <= 0) {
                usage();
                return;
            }
            if (args[0].equals("--compare")) {
                // --compare string filename == 3
                if (args.length != 3) {
                    usage();
                    return;
                }
                FileDigester fd = new FileDigester(args[2]);
                String calculated = fd.getDigestStringCompact();

                if (calculated.equalsIgnoreCase(args[1].trim())) 
                    System.out.println("MATCH");
                
                else
                    System.out.printf("Does not match:  %s\n", calculated);
                
                return;
            }

            for (String arg : args) {
                FileDigester fd;
                fd = new FileDigester(arg);
                System.out.println("File: " + arg + ", DIGEST: " + fd.getDigestStringCompact());
                //System.out.println(fd.getDigestStringCompact());
            }
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
    }

    private static final String SHA = "SHA-1";
    private static final String MD5 = "MD5";
    private static final int BUFFER_SIZE = 1048576;
    private MessageDigest digester = MessageDigest.getInstance(MD5);
    private String filename;

    private final static int MAX_FILE_SIZE = 50 * 1000 * 1000;
}
