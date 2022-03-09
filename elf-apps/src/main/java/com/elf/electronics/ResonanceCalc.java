package com.elf.electronics;

import com.elf.args.Arg;
import com.elf.args.ArgProcessor;
import com.elf.util.StringUtils;
import java.util.List;
import java.util.Map;

/**
 *
 * @author bnevins
 */

public class ResonanceCalc {
    public static void main(String[] args) {
        if (args.length == 0) {
            usage();
        }
        try {
            ArgProcessor proc = new ArgProcessor(argDescriptions, args);
            Map<String, String> params = proc.getParams();
            List<String> operands = proc.getOperands();
            ResonanceCalc rc = new ResonanceCalc(params, operands);
            rc.processArgs();
            rc.calculate();
            rc.report();
        
        //} catch (RuntimeException ex) {
        }catch (Exception ex) {
            System.out.println(ex);
        }
    }

    ResonanceCalc(Map<String, String> params, List<String> operands) {
        this.params = params;
    }

    private void processArgs() {
        processCap(params.get("cap"));
        processInductance(params.get("ind"));
        processFrequency(params.get("freq"));
    }

    private void processFrequency(String s) {
        freq = processKandM(s);
    }

    private void processInductance(String s) {
        ind = process_u_and_m(s);
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

    // FIXME -- very lazy copy&paste&edit of processKandM
    private double process_u_and_m(String s) {
        if (!StringUtils.ok(s)) {
            return -1;
        }
        double divisor = 1;
        char unit = s.charAt(s.length() - 1);
        if (!isNum(unit)) {
            switch (unit) {
                case 'u':
                    divisor = 1000000;
                    break;
                case 'm':
                    divisor = 1000;
                    break;
                default:
                    throw new RuntimeException("Unknown resistor/frequency format");
            }
            s = s.substring(0, s.length() - 1);
        }
        double ret = Double.parseDouble(s);
        ret /= divisor;
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
        new Arg("ind", "i", false, "Inductor"),};
    //new Arg("exact", "e", false, "Exact Filename"),
    //new BoolArg("ic", null, true, "Case Insensitive"),
    //new BoolArg("filenameonly", "f", false, "Return Filenames Only"),
    //new Arg("prevline", "p", false, "Find on previous line"),};  

    private double cap;
    private double freq = 1;
    private double ind;
    private final Map<String, String> params;

    private void calculate() {
        // makes no sense to specify all 3!
        if (ind > 0 && freq > 0 && cap > 0) {
            throw new RuntimeException("\n\n **** Cap, Ind and Freq were all specified.  I have no idea what to do!");
        }
        if (ind > 0 && freq > 0) { 
            // calculate cap...
            cap = 1 / (Math.pow(2 * Math.PI * freq, 2) * ind);
        } else if (cap > 0 && freq > 0) {  // calculate inductor
            ind = 1 / (Math.pow(2 * Math.PI * freq, 2) * cap);
        } else if (ind > 0 && cap > 0) {  
            // Calculate the resonant frequency
            freq = 1 / ( (2 * Math.PI) * Math.sqrt(ind * cap) );
        } else if (cap > 0) {
            // cap only -- print a table
            //long frequency = 1;
            System.out.printf("%10s%14s\n", "Frequency", "Impedance");
            for (double frequency = 1.0; frequency < 1.0E9; frequency *= 10.0) {
                double impedance = 1 / (2 * Math.PI * frequency * cap);
                System.out.printf("%-10.0f    %-8.0f\n", frequency, impedance);
            }
        } else {
            throw new RuntimeException("You must specify 2 values");
        }
    }

    private void report() {
        // cap stored as FARADS
        System.out.printf("Capacitance: %s\n", convertCap(cap));
        System.out.printf("Inductance: %sHenrys\n", convert(ind, 1));
        System.out.printf("Frequency: %sHz\n", convert(freq, 4));

    }


    /**
     * caps are special. 2 sig figs. Anything >= 0.001 uF stay as uF. Anything
     * less is pF
     */
    public static String convertCap(double val) {
        if (val < 1.0e-9) {
            return String.format("%.0f pF", val * 1.0e12);
        } else {
            return String.format("%.3f uF", val * 1.0e6);
        }

    }

    public static String convert(double val) {
        return convert(val, 0);
    }

    public static String convert(double val, int dp) {
        // If the value is zero, then simply return 0 with the correct number of dp
        if (val == 0) {
            return String.format("%." + dp + "f", 0.0);
        }

        // If the value is negative, make it positive so the log10 works
        if (val < 0) {
            throw new RuntimeException("Can't handle negative values");
        }

        //double posVal = (val < 0) ? -val : val;
        double log10 = Math.log10(val);

        // Determine how many orders of 3 magnitudes the value is
        int count = (int) Math.floor(log10 / 3);

        // Calculate the index of the prefix symbol
        int index = count + PREFIX_OFFSET;

        // Scale the value into the range 1<=val<1000
        val /= Math.pow(10, count * 3);

        if (index >= 0 && index < PREFIX_ARRAY.length) {
            // If a prefix exists use it to create the correct string
            return String.format("%." + dp + "f %s", val, PREFIX_ARRAY[index]);
        } else {
            // If no prefix exists just make a string of the form 000e000
            return String.format("%." + dp + "fe%d", val, count * 3);
        }
    }
    private final static int PREFIX_OFFSET = 5;
    private final static String[] PREFIX_ARRAY = {"f", "p", "n", "µ", "m", "", "k", "M", "G", "T"};

}

