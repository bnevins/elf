package com.elf.process;

import com.elf.io.*;
import com.elf.util.OS;
import com.elf.util.StringUtils;
import java.io.*;
import java.util.*;

/**
 * Run a native process with jps
 * -- get the pid for a running JVM
 * note:  dropping in an implementation for jps is not hard.
 * @author bnevins
 */
public class Jps {

    public static void main(String[] args) {
        Set<Map.Entry<String, Integer>> set = getProcessTable().entrySet();
        System.out.println("** Got " + set.size() + " process entries");
        for (Map.Entry<String, Integer> e : set) {
            System.out.printf("%d %s\n", e.getValue(), e.getKey());
        }
    }

    final static public Map<String, Integer> getProcessTable() {
        return new Jps().pidMap;
    }

    /**
     * return the platform-specific process-id of a JVM
     * @param mainClassName The main class - this is how we identify the right JVM
     * @return the process id if possible otherwise 0
     */
    final static public int getPid(String mainClassName) {
        Jps jps = new Jps();
        Integer integer = jps.pidMap.get(mainClassName);

        if (integer == null) {
            return 0;
        }

        return integer;
    }

    /**
     * Is this pid owned by a process?
     * @param apid the pid of interest
     * @return whether there is a process running with that id
     */
    final static public boolean isPid(int apid) {
        return new Jps().pidMap.containsValue(apid);
    }

    private Jps() {
        try {
            if (jpsExe == null) {
                return;
            }
            ProcessManager pm = new ProcessManager(jpsExe.getPath(), "-l");
            pm.execute();
            String jpsOutput = pm.getStdout();

            // get each line
            String[] ss = jpsOutput.split("[\n\r]");

            for (String line : ss) {
                if (line == null || line.length() <= 0) {
                    continue;
                }

                String[] sublines = line.split(" ");
                if (sublines == null || sublines.length != 2) {
                    continue;
                }

                int aPid = 0;
                try {
                    aPid = Integer.parseInt(sublines[0]);
                } catch (Exception e) {
                    continue;
                }
                // todo -- handle duplicate names??
                // todo -- easy --> make the pid the key not the value!
                if (!isJps(sublines[1])) {
                    pidMap.put(sublines[1], aPid);
                }
            }
        } catch (Exception e) {
        }
    }

    private boolean isJps(String id) {
        if (!StringUtils.ok(id)) {
            return false;
        }

        if (id.equals(getClass().getName())) {
            return true;
        }

        if (id.equals("sun.tools.jps.Jps")) {
            return true;
        }

        return false;
    }
    private Map<String, Integer> pidMap = new HashMap<String, Integer>();
    private static final File jpsExe;
    private static final String jpsName;

    static {
        if (OS.isWindows()) {
            jpsName = "jps.exe";
        } else {
            jpsName = "jps";
        }

        final String javaroot = System.getProperty("java.home");
        final String relpath = "/bin/" + jpsName;
        final File fhere = new File(javaroot + relpath);
        File fthere = new File(javaroot + "/.." + relpath);

        if (fhere.isFile()) {
            jpsExe = SmartFile.sanitize(fhere);
        } else if (fthere.isFile()) {
            jpsExe = SmartFile.sanitize(fthere);
        } else {
            jpsExe = null;
        }
    }
}

