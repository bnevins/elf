/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.network;

import com.elf.net.NetUtils;
import java.lang.Runnable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bnevins
 */
public class PortChecker {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 1) {
            boolean free = NetUtils.isPortFree(Integer.parseInt(args[0]));
            System.out.println("Port " + args[0] + " is " + (free ? "free" : "in use"));
        } else if (args.length == 2) {
            // don't hang around forever.  5 seconds should be plenty...
            RemotePortChecker checker = new RemotePortChecker(args[0], Integer.parseInt(args[1]));
            Thread t = new Thread(checker);
            t.setDaemon(true);
            t.start();
            try {
                t.join(5000);
            }
            catch (InterruptedException ex) {
            }
            if(!checker.done)
                System.out.println("Port " + args[1] + " on host " + args[0] + " is " + "free since it timed-out after 5 seconds");
        } else
            System.out.println("USAGE:  PortChecker port == or == PortChecker host port");
    }

    private static class RemotePortChecker implements Runnable {

        private final String host;
        private final int port;
        private boolean done = false;

        RemotePortChecker(String host, int port) {
            this.host = host;
            this.port = port;
        }

        @Override
        public void run() {
            boolean free = NetUtils.isPortFree(host, port);
            System.out.println("Port " + port + " on host " + host + " is " + (free ? "free" : "in use"));
            done = true;
        }
    }
}
