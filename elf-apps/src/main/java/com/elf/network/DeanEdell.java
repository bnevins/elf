/*
 * DeanEdell.java
 *
 * Created on May 15, 2007, 8:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.elf.network;

import com.elf.args.Arg;
import com.elf.args.ArgProcessor;
import com.elf.args.BoolArg;
import com.elf.io.FileDigester;
import com.elf.net.HTTPDownloader;
import com.elf.util.StringUtils;
import java.io.*;
import java.security.*;
import java.util.*;
//import javazoom.jl.converter.jlc;
import static java.util.Calendar.*;

/**
 *
 * @author bnevins
 */
public class DeanEdell {

    public static void main(String[] args) {

        // set defaults
        String mp3Dir = DEFAULT_DIR_HTTP;
        String url = makeURL();

        ArgProcessor proc = new ArgProcessor(argDescriptions, args);
        Map<String, String> params = proc.getParams();
        //List<String> operands = proc.getOperands();

        if(Boolean.parseBoolean(params.get("help")))
            usage(); // does not return

        String mp3DirArg = params.get("mp3dir");
        String urlArg = params.get("url");

        String digestArg = params.get("digest");

        if(StringUtils.ok(urlArg))
            url = urlArg;

        if(StringUtils.ok(mp3DirArg))
            mp3Dir = mp3DirArg;

        try {
            new DeanEdell(url, mp3Dir, digestArg);
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public DeanEdell(String url, String mp3Dir, String fileToDigest) throws FileNotFoundException {
        // APPENDS to logfile  FNFE is only thrown if it doesn't exist and can not be created --
        // i.e. not likely and catastrophic -- so just die!
        log = new PrintWriter(new FileOutputStream(mp3Dir + "/DeanEdell.log", true));
        String mp3filename = makeMp3FileName();
        File dir = new File(mp3Dir);
        dir.mkdirs();

        log(new Date().toString());
        File mp3fullpath = new File(dir, mp3filename);


        try {
            if(fileToDigest != null)
                mp3fullpath = new File(fileToDigest);
            else {
                log("Downloading from " + url);
                log("Downloading to " + mp3fullpath);
                new HTTPDownloader(url, mp3fullpath.getPath());
            }
            
            writeDigest(mp3fullpath);
            //log("Converting mp3 to wav srote to: " + mp3fullpath);
            mp3ToWav(dir, mp3filename);

        } catch (Exception ioe) {
            ioe.printStackTrace();
        } finally {
            log("--------------------------------------------");
            log.close();
        }
    }
    private static void usage()
    {
        System.out.println("DeanEdell Usage");
        System.out.println("");
        System.out.println(Arg.toHelp(argDescriptions));
        System.exit(1);
    }

    private File makeDigestFile(File mp3) {
        String digestFileName = mp3.getAbsolutePath();
        digestFileName = digestFileName.substring(0, digestFileName.length() - 3);
        digestFileName += "dig";

        return new File(digestFileName);
    }

    private void writeDigest(File mp3) {
        FileDigester fd;
        File digestFile = makeDigestFile(mp3);
        log("Writing digest of " + mp3 +  " to " + digestFile);

        try {
            fd = new FileDigester(mp3.getAbsolutePath());
            String digestString = fd.getDigestString();
            log("DIGEST: " + digestString);
            PrintWriter out;
            try {
                out = new PrintWriter(digestFile);
                out.print(digestString);
                out.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        } catch (NoSuchAlgorithmException ex) {
            log("ERROR - can not digest!");
        }
    }

    private static int getDayOfWeekForToday() {
        // Monday == 1, Tuesday == 2, etc.
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(DAY_OF_WEEK);
        int firstDayOfWeek = cal.getFirstDayOfWeek();

        if (firstDayOfWeek == SUNDAY) {
            return dayOfWeek - firstDayOfWeek;
        } else {
            return dayOfWeek;
        }
    }

    private String makeMp3FileName() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;  // January == 0
        int dayOfMonth = cal.get(DAY_OF_MONTH);
        int dayOfWeek = cal.get(DAY_OF_WEEK);
        int year = cal.get(YEAR);

        if (dayOfWeek == SATURDAY || dayOfWeek == SUNDAY) {
            log("No Dean Edell on weekends!");
            System.exit(0);
        }

        // make the months and days:  01, ...09, 10, 11, etc.)
        // there are better ways of doing this but I'm lazy today...'
        String monthString = "0";
        if (month < 10) {
            monthString += "" + month;
        } else {
            monthString = "" + month;
        }

        String dayString = "0";
        if (dayOfMonth < 10) {
            dayString += "" + dayOfMonth;
        } else {
            dayString = "" + dayOfMonth;
        }

        String filename = "DE" + year + monthString + dayString + ".mp3";
        return filename;
    }

    private void log(String s) {
        log.println(s);
    }

    private static String makeURL() {
        return DEFAULT_URL_ROOT + getDayOfWeekForToday() + DEFAULT_URL_SUFFIX;
    }
    private PrintWriter log;
    //private final static String DEFAULT_URL_ROOT_FOR_APRIL_2010 = "http://lark.net/kgo_archives/";
    private final static String DEFAULT_URL_ROOT = "http://bayradio.com/kgo_archives/";
    private final static String DEFAULT_URL_SUFFIX = "1400.mp3";
    //private final static String OLD_DEFAULT_URL = "http://lark.net/kgo_archive/kgo-13.mp3";
    //private final static String OLDER_DEFAULT_URL = "http://abcrad.vo.llnwd.net/o1/kgo/kgo-13.mp3";
    private final static String DEFAULT_DIR_HTTP = "C:/as/domains/domain1/docroot/DeanEdellFiles";


        private final static Arg[] argDescriptions = new Arg[]
    {
        new Arg("mp3dir", "d", false, "Directory to copy the mp3 file to"),
        new Arg("url", "u", false, "URL of the file to fetch"),
        new Arg("digest", null, false, "Do a digest of the specified mp3 file only."),
        new BoolArg("help", "h", false, "Usage Help"),
        //new BoolArg("filenameonly", "f", false, "Return Filenames Only"),
    };



    private void mp3ToWav(File dir, String mp3filename) {
            return;
/***
        final int len = mp3filename.length();

        if(len < 5 || !mp3filename.endsWith(".mp3"))
            return;

        final String wavfilename = mp3filename.substring(0, len - 4) + ".wav";

        final File out = new File(dir, wavfilename);
        final File in = new File(dir, mp3filename);

        String[] args = new String[]
        {
            "-v",
            "-p",
            out.getPath(),
            in.getPath(),
        };

       jlc.main(args);

 */
    }





}
