/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.elf.process;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  sample "jps -l" output:
2112 com.sun.enterprise.glassfish.bootstrap.ASMain
1140 sun.tools.jps.Jps
944
3320 com.sun.enterprise.admin.cli.optional.DerbyControl
 * @author bnevins
 */
public class GFKiller {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GFKiller gfk = new GFKiller();
        gfk.kill();

    }

    public GFKiller() {
        setKillList();
        setKiller();
    }

    public void kill() {
        if(killIds.length <= 0)
            System.out.println("Nothing to kill.");
        for(int i : killIds) {
            ProcessManager pm = new ProcessManager(killerExe, "/f", "/pid", "" + i);
            pm.setEcho(false);

            try {
                System.out.println("Killing PID " + i);
                pm.execute();
            }
            catch (ProcessManagerException ex) {
                Logger.getLogger(GFKiller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

////////////////////////////  private below /////////////////////////////////

    private void setKiller() {
        try {
            killerExe = ProcessUtils.getExe("taskkill.exe").getPath();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void setKillList() {
        List<Integer> pids = new LinkedList<Integer>();
        Map<String, Integer> procs = Jps.getProcessTable();

        for(String s : PROCS) {
            if(procs.containsKey(s))
                pids.add(procs.get(s));
        }
        killIds = new int[pids.size()];
        int index = 0;

        for(int pid : pids) {
            killIds[index++] =  pid;
        }
    }


    private final static String[] PROCS = new String[]
    {
        "com.sun.enterprise.glassfish.bootstrap.ASMain",
        "com.sun.enterprise.admin.cli.optional.DerbyControl",
    };
    private int[]  killIds;
    private String killerExe;
}
