
package com.elf.args;

/**
 *
 * @author bnevins
 */
public class BoolArg extends Arg{

    public BoolArg(String ln, String sn, boolean defaultValue, String desc) {
        super(ln, sn, Boolean.toString(defaultValue), desc);
    }
    @Override
    boolean requiresParameter() {
        return false;
    }

    @Override
    boolean isThisYou(String s) {
        if(super.isThisYou(s))
            return true;

        // handle --xxx=true --xxx=false
        // We only get here if it wasn't --xxx or --no-xxx

        if(ok(shortName) && s.startsWith("-" + shortName + "="))
            return true;

        if(s.startsWith("--" + longName + "="))
            return true;

        return false;
    }

    boolean parse(String s) {
        // 1. if "--xyz", then xyz set to true
        // 2. if "--no-xyz" then xyz set to false
        // 3. if "--xyz=true"
        // 4. "--xyz=flase"

        // case 2
        if(s.startsWith(Arg.NOT))
            return false;

        int index = s.indexOf('=');

        // case 1
        if(index < 0)
            return true;

        // =true is the minimum.  THis will catch =t or whatever
        if(s.length() - index < 5)
            return true;

        return Boolean.parseBoolean(s.substring(index + 1));
    }

    private boolean ok(String s) {
        return s != null && s.length() > 0;
    }
}
