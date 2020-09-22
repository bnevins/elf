package com.elf.image;

import com.elf.args.Arg;
import com.elf.args.ArgProcessor;
import com.elf.args.BoolArg;
import com.elf.io.FileFinder;
import com.elf.io.FileUtils;
import com.elf.io.JpegFileFilter;
import com.elf.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * TODO: DONE!! 1. add cmdline option for prefix
 */
public class JpegFileRenamer {

    boolean verbose = false;
    boolean dryRun = false;
    private static int renameCount = 0;
    private final String prefix;
    private final static Arg[] argDescriptions = new Arg[]{
        new BoolArg("verbose", "v", false, "Verbose Output"),
        new BoolArg("dryRun", "n", false, "Dry Run.  Do NOT do the actual renaming"),
        new Arg("prefix", "p", "", "Prefix to prepend to each filename"),};

    private static void usage() {
        System.out.println("JpegFileRenamer directory-name");
        System.out.println("");
        System.out.println(Arg.toHelp(argDescriptions));
        System.exit(1);
    }

    private JpegFileRenamer(Map<String, String> params) {
        verbose = Boolean.parseBoolean(params.get("verbose"));
        dryRun = Boolean.parseBoolean(params.get("dryRun"));
        prefix = params.get("prefix");

        if (verbose) {
            System.out.println("verbose: " + Boolean.parseBoolean(params.get("verbose")));
            System.out.println("dryRun: " + Boolean.parseBoolean(params.get("dryRun")));
            System.out.println("prefix: " + params.get("prefix"));
        }
    }

    public void renameFile(File f) {
        f = FileUtils.safeGetCanonicalFile(f);
        JpegReader.setDebug(verbose);
        JpegReader reader = new JpegReader(f);
        String newName = reader.getFilenameFromTimestamp();
        if (newName == null) {
            throw new RuntimeException("Bad filename (" + newName + ") generated from: " + f);
        }
        if (f == null) {
            throw new RuntimeException("null file argument");
        }

        if (StringUtils.ok(prefix)) {
            newName = prefix + newName;
        }

        File dir = FileUtils.safeGetCanonicalFile(f.getParentFile());
        File newFile = new File(dir, newName);

        if (!dryRun) {
            if (f.equals(newFile)) {
                if (verbose) {
                    System.out.println("No need to rename itself:" + newFile);
                }
                return;
            }
            if (!f.renameTo(newFile)) {
                System.out.println("Could NOT Rename: " + f + " TO: " + newFile);
            } else // it went OK
            {
                renameCount++;
            }
        } else {
            // no renaming -- so they want output!
            verbose = true;
        }

        if (verbose) {
            System.out.println("Rename: " + f + " TO: " + newFile);
        }
    }

    public static void main(String[] args) {
        try {
            ArgProcessor proc = new ArgProcessor(argDescriptions, args);
            Map<String, String> params = proc.getParams();
            List<String> operands = proc.getOperands();

            if (operands.size() < 1) {
                usage();
            }
            String dirName = operands.get(0);
            dirName = FileUtils.safeGetCanonicalPath(new File(dirName));

            if (!StringUtils.ok(dirName)) {
                usage();
            }

            FileFinder ff = new FileFinder(dirName, new JpegFileFilter());
            List<File> files = ff.getFiles();

            JpegFileRenamer renamer = new JpegFileRenamer(params);

            for (File f : files) {
                renamer.renameFile(f);
            }
            System.out.printf("%d files renamed in directory %s\n", renameCount, dirName);

        } catch (IOException ex) {
            Logger.getLogger(JpegFileRenamer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
