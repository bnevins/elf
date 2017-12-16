
package com.elf.args;

import com.elf.util.StringUtils;

/**
 *
 * @author bnevins
 */
public class Arg {
    public Arg(String ln, String sn, String defaultValue, String desc) {
        this(ln, sn, defaultValue, true, desc);
    }
    
    public Arg(String ln, String sn, boolean required, String desc) {
        this(ln, sn, null, required, desc);
    }
    
    private Arg(String ln, String sn, String defaultValue, boolean required, String desc) {
        longName = ln;
        shortName = sn;
        this.defaultValue = defaultValue;
        this.required = required;
        description = desc;
    }
    
    public static String toHelp(Arg[] args) {
        String[] longs = new String[args.length];
        String[] shorts = new String[args.length];
        String[] descs = new String[args.length];
        String[] defs = new String[args.length];
        String[] reqs = new String[args.length];
        
        for(int i = 0; i < args.length; i++) {
            longs[i] = args[i].longName;
            shorts[i] = args[i].shortName;
            descs[i] = args[i].description;
            defs[i] = args[i].defaultValue;
            reqs[i] = Boolean.toString(args[i].required);
        }
        
        int longMax = Math.max(LONG.length(), StringUtils.maxWidth(longs)) + 4;
        int shortMax = Math.max(SHORT.length(), StringUtils.maxWidth(shorts)) + 3;
        int descMax = Math.max(DESC.length(), StringUtils.maxWidth(descs)) + 2;
        int defMax = Math.max(DEF.length(), StringUtils.maxWidth(defs)) + 2;
        int reqMax = Math.max(REQ.length(), StringUtils.maxWidth(reqs)) + 2;
        
        StringBuilder sb = new StringBuilder();
        
        sb.append(pr(DESC, descMax));
        sb.append(pr(LONG, longMax));
        sb.append(pr(SHORT, shortMax));
        sb.append(pr(DEF, defMax));
        sb.append(pr(REQ, reqMax));
        sb.append(StringUtils.EOL);
        
        for(Arg arg : args) {
            sb.append(StringUtils.EOL);
            sb.append(pr(arg.description, descMax));
            sb.append(pr("--" + arg.longName, longMax));
            sb.append(pr(arg.shortName == null ? "" : "-" + arg.shortName, shortMax));
            sb.append(pr(arg.defaultValue == null ? "" : arg.defaultValue, defMax));
            sb.append(pr(Boolean.toString(arg.required), reqMax));
        }
        
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Name: " + longName + ", value: " + 
                value + ", required: " + required + ", default: " + defaultValue;
    }
    
    boolean isRequired() {
        return required;
    }

    boolean isThisYou(String s) {
        if(ok(shortName) && s.equals("-" + shortName))
            return true;
    
        if(s.equals("--" + longName))
            return true;

        // --no-
        if(s.startsWith(NOT) && s.length() > NOT.length())  {
            s = s.substring(NOT.length());
            
            // s is at least one character
            if(s.equals(longName) || s.equals(shortName))
                return true;
        }
        return false;
    }

    public String getValue() {
        return value;
    }
    
    boolean requiresParameter() {
        return true;
    }
    
    private boolean ok(String s) {
        return s != null && s.length() > 0;
    }

    private static String pr(String s, int num) {
        return StringUtils.padRight(s, num);
    }
    
    public String longName;
    String shortName;
    String value;
    String defaultValue;
    boolean required;
    private String description;
    private static final String DESC = "Description";
    private static final String LONG = "Long Name";
    private static final String SHORT = "Short Name";
    private static final String REQ = "Required";
    private static final String DEF = "Default";
    static final String NOT = "--no-";
}
