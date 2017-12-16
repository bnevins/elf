/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.elf.network;

import java.net.*;

/**
 *
 * @author bnevins
 */
public class SocketBinder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Ready to wait forever on port " + port);

            while(true)
                ss.accept();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}