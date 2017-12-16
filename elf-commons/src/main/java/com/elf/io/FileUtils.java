/*
 * FileUtils.java
 *
 * Created on June 15, 2003, 12:26 AM
 */
package com.elf.io;

import com.elf.util.OS;
import com.elf.util.StringUtils;
import java.io.*;

/**
 *
 * @author bnevins
 */
public class FileUtils {
    private static int COPY_FILE_BUFFER_SIZE = 1048576;

    private FileUtils() {
    }

    public static boolean safeIsDirectory(String filename) {
        return safeIsDirectory(new File(filename));
    }

    public static boolean safeIsDirectory(File f) {
        if (f == null || !f.exists() || !f.isDirectory()) {
            return false;
        }

        return true;
    }

    public static File safeGetCanonicalFile(File f) {
        if (f == null) {
            return null;
        }

        try {
            return f.getCanonicalFile();
        }
        catch (IOException e) {
            return f.getAbsoluteFile();
        }
    }

    public static String safeGetCanonicalPath(File f) {
        if (f == null) {
            return null;
        }

        try {
            return f.getCanonicalPath();
        }
        catch (Exception e) {
            return f.getAbsolutePath();
        }
    }

    /////////////////////////////////////////////////////////
    public static boolean whack(File parent) {
        //Reporter.insist(parent);

        if (safeIsRealDirectory(parent)) {
            File[] kids = parent.listFiles();

            for (int i = 0; i < kids.length; i++) {
                File f = kids[i];

                if (f.isDirectory()) {
                    whack(f);
                }
                else {
                    f.delete();
                }

            }
        }
        return parent.delete();
    }

    ///////////////////////////////////////////////////////////////////////////
    public static boolean safeIsRealDirectory(String s) {
        return safeIsRealDirectory(new File(s));
    }

    ///////////////////////////////////////////////////////////////////////////
    public static boolean safeIsRealDirectory(File f) {
        if (safeIsDirectory(f) == false) {
            return false;
        }



        // these 2 values while be different for symbolic links
        String canonical = safeGetCanonicalPath(f);
        String absolute = f.getAbsolutePath();

        if (canonical.equals(absolute)) {
            return true;
        }

        /* Bug 4715043 -- WHOA -- Bug Obscura!!
         * In Windows, if you create the File object with, say, "d:/foo", then the 
         * absolute path will be "d:\foo" and the canonical path will be "D:\foo"
         * and they won't match!!!
         **/
        if (OS.isWindows() && canonical.equalsIgnoreCase(absolute)) {
            return true;
        }

        return false;
    }

    public static File removeBlankLines(File f) throws FileNotFoundException, IOException {
        File copy = new File(f.getParentFile(), f.getName() + "xx");
        BufferedReader reader = new BufferedReader(new FileReader(f));
        PrintStream writer = new PrintStream(copy);
        String s;
        while ((s = reader.readLine()) != null) {
            if (s.length() > 0 && !StringUtils.isAllWhite(s)) {
                writer.println(s);
            }
        }
        reader.close();
        writer.close();
        return copy;
    }

    /**
     * If fout is null then we copy "to nowhere" -- used as a control when
     * testing speed e.g.
     *
     * @param fin
     * @param fout
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void copyFile(File fin, File fout) throws FileNotFoundException, IOException {
        InputStream sin = null;
        OutputStream sout = null;
        try {
            sin = new BufferedInputStream(new FileInputStream(fin));
            if (fout != null) {
                sout = new BufferedOutputStream(new FileOutputStream(fout));
            }
            byte[] buf = new byte[COPY_FILE_BUFFER_SIZE];
            while (sin.read(buf) >= 0) {
                if (sout != null) {
                    sout.write(buf);
                }
            }
        }
        finally {
            try {
                if (sin != null) {
                    sin.close();
                }
            }
            catch (IOException ex) {
                // ignore
            }
            try {
                if (sout != null) {
                    sout.flush();
                    sout.close();
                }
            }
            catch (IOException ex) {
                // ignore
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Whack returned: " + whack(new File("C:/temp/link")));
    }
}
