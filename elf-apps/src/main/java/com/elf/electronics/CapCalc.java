/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.electronics;

import com.elf.args.Arg;
import com.elf.args.ArgProcessor;
import com.elf.args.BoolArg;
import com.elf.io.TreeGrep;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.elf.util.StringUtils;

/**
 *
 * @author bnevins
 */
public class CapCalc {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            usage();
        }
        try {
            ArgProcessor proc = new ArgProcessor(argDescriptions, args);
            Map<String, String> params = proc.getParams();
            List<String> operands = proc.getOperands();
            CapCalc cc = new CapCalc(params, operands);
            cc.processArgs();
        } catch (Exception ex) {
            Logger.getLogger(TreeGrep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    CapCalc(Map<String, String> params, List<String> operands) {
        this.params = params;
    }

    private void processArgs() {
        processCap(params.get("cap"));
    }
    private void processCap(String capS) {
        if(!StringUtils.ok(capS)) {
            cap = -1;
            return;
        }
        int len = capS.length();
        if(len < 2)
            throw new RuntimeException("Expected at least 2 characters for capacitance!");
        // must end in unit since no such thing as Farad capacitors!
        
        char unit = capS.charAt(len - 1);
        String num = capS.substring(0, len - 1); //strip unit off
        //System.out.println("CAP: " + num + unit);
        cap = (double)Integer.parseInt(num);
        switch(unit) {
            case 'p':
            case 'P': cap *= 1E-12; break;
            case 'n':
            case 'N': cap *= 1E-9; break;
            case 'u':
            case 'U': cap *= 1E-6; break;
            default:
            throw new RuntimeException("unknown capacitor unit");
        }
        System.out.printf("CAP: %e\n", cap);
    }
    private static void usage() {
        System.out.println("USAGE: ");
        System.out.println(Arg.toHelp(argDescriptions));
    }
    private final static Arg[] argDescriptions = new Arg[]{
        //new BoolArg("regexp", "r", false, "Regular Expression"),
        new Arg("cap", "c", false, "Capacitor"),
        new Arg("freq", "f", false, "Frequency"),
        new Arg("res", "r", false, "Frequency"),};
    //new Arg("exact", "e", false, "Exact Filename"),
    //new BoolArg("ic", null, true, "Case Insensitive"),
    //new BoolArg("filenameonly", "f", false, "Return Filenames Only"),
    //new Arg("prevline", "p", false, "Find on previous line"),};  

    private double cap;
    private double freq;
    private double res;
    private final Map<String, String> params;

}
