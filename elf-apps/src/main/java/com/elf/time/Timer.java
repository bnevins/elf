package com.elf.time;

import com.elf.audio.Sounds;
import java.io.Console;

/**
 *
 * @author Byron Nevins
 */
public class Timer {
    public static void main(String[] args) {
        try {
            int num = Integer.parseInt(args[0]);
            int msec = num * 60 * 1000;

            Console con = System.console();
            System.out.println("console: " + con);
            String s;
            Thread.sleep(msec);
            while (System.in.available() == 0) {
                Sounds.beep();
                Thread.sleep(500);
            }
        }
        catch (Exception ex) {
            usage();
        }
    }
    private static void usage() {
        System.out.printf("java %s mseconds\n", Timer.class.getName());
    }
}
