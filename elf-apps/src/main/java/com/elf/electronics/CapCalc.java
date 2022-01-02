package com.elf.electronics;

import com.elf.args.Arg;
import com.elf.args.ArgProcessor;
import com.elf.args.BoolArg;
import com.elf.io.TreeGrep;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.elf.util.StringUtils;

/**
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
            cc.calculate();
            cc.report();
        } catch (Exception ex) {
            Logger.getLogger(TreeGrep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    CapCalc(Map<String, String> params, List<String> operands) {
        this.params = params;
    }

    private void processArgs() {
        processCap(params.get("cap"));
        processResistance(params.get("res"));
        processFrequency(params.get("freq"));
    }

    private void processFrequency(String s) {
        freq = processKandM(s);
    }

    private void processResistance(String s) {
        res = processKandM(s);
    }

    private double processKandM(String s) {
        if (!StringUtils.ok(s)) {
            return -1;
        }
        int multiplier = 1;
        char unit = s.charAt(s.length() - 1);
        if (!isNum(unit)) {
            switch (unit) {
                case 'k':
                case 'K':
                    multiplier = 1000;
                    break;
                case 'm':
                case 'M':
                    multiplier = 1000000;
                    break;
                default:
                    throw new RuntimeException("Unknown resistor/frequency format");
            }
            s = s.substring(0, s.length() - 1);
        }
        double ret = Double.parseDouble(s);
        ret *= multiplier;
        return ret;
    }

    private void processCap(String s) {
        if (!StringUtils.ok(s)) {
            cap = -1;
            return;
        }
        int len = s.length();
        if (len < 2) {
            throw new RuntimeException("Expected at least 2 characters for capacitance!");
        }
        // must end in unit since no such thing as Farad capacitors!

        char unit = s.charAt(len - 1);
        String num = s.substring(0, len - 1); //strip unit off
        //System.out.println("CAP: " + num + unit);
        cap = Double.parseDouble(num);
        switch (unit) {
            case 'p':
            case 'P':
                cap *= 1E-12;
                break;
            case 'n':
            case 'N':
                cap *= 1E-9;
                break;
            case 'u':
            case 'U':
                cap *= 1E-6;
                break;
            default:
                throw new RuntimeException("unknown capacitor unit");
        }
    }

    private boolean isNum(char c) {
        return (c >= '0' && c <= '9');
    }

    private static void usage() {
        System.out.println("USAGE: ");
        System.out.println(Arg.toHelp(argDescriptions));
    }
    private final static Arg[] argDescriptions = new Arg[]{
        //new BoolArg("regexp", "r", false, "Regular Expression"),
        new Arg("cap", "c", false, "Capacitor"),
        new Arg("freq", "f", false, "Frequency"),
        new Arg("res", "r", false, "Resistor"),};
    //new Arg("exact", "e", false, "Exact Filename"),
    //new BoolArg("ic", null, true, "Case Insensitive"),
    //new BoolArg("filenameonly", "f", false, "Return Filenames Only"),
    //new Arg("prevline", "p", false, "Find on previous line"),};  

    private double cap;
    private double freq;
    private double res;
    private final Map<String, String> params;

    private void calculate() {
        // makes no sense to specify all 3!
        if(res > 0 && freq > 0 && cap > 0) {
            throw new RuntimeException("\n\n **** Cap, Res and Freq were all specified.  I have no idea what to do!");
        }
        if(res > 0 && freq > 0) { // Whats the -3dB capacitance?
            // calculate cap...
            cap = 1 / (2 * Math.PI * freq * res);
        }
        else if(cap > 0 && freq > 0) {  // calculate impedance of capacitor
            res = 1 / (2 * Math.PI * freq * cap);
        }
        else if(res > 0 && cap > 0) {  // what to do?
         throw new RuntimeException("Cap and Res -- what should I do?!?");   
        }
        else if(cap > 0) {
            // cap only -- print a table
            //long frequency = 1;
            System.out.printf("%10s%14s\n", "Frequency", "Impedance");
            for(double frequency = 1.0; frequency < 1.0E9; frequency *= 10.0) {
                double impedance = 1 / (2 * Math.PI * frequency * cap);
                System.out.printf("%-10.0f    %-8.0f\n", frequency, impedance);
            }
        }
        else {
            throw new RuntimeException("You must specify 2 values");
        }
    }

    private void report() {
        // cap stored as FARADS
        System.out.printf("Capacitance: %e pF\n", cap * 1e12);
        System.out.printf("Impedance: %e ohms\n", res);
        System.out.printf("Frequency: %e Hz\n", freq);

    }
}
