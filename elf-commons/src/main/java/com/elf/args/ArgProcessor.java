
package com.elf.args;

import java.util.*;

/**
 *
 * @author bnevins
 */
public class ArgProcessor {
    public ArgProcessor (Arg[] allArgs0, String[] cmdlineArgs0) {
        allArgs = allArgs0;
        cmdlineArgs = cmdlineArgs0;

        if(cmdlineArgs == null)
            cmdlineArgs = new String[0];
        
        process();
    }

    public Map<String,String> getParams() {
        return params;
    }
    
    public List<String> getOperands() {
        return operands;
    }
    
    private void process() {
        int numArgs = cmdlineArgs.length;
        
        for(int i = 0; i < numArgs; i++) {
            String arg = cmdlineArgs[i];
            
            if(!ok(arg))
                continue;
            
            if(isParam(arg))
                i += doParam(arg, i);
            else
                operands.add(arg);
        }
        processParams();
    }

    private int doParam(String s, int index) {
        for(Arg arg : allArgs) {
            if(arg.isThisYou(s)) {
                if(arg.requiresParameter()) {
                    if(index + 1 >= cmdlineArgs.length)
                        throw new IllegalArgumentException("no parameter for " + arg.longName);
                    arg.value = cmdlineArgs[index + 1];
                    return 1;
                }
                else {
                    // this is a boolean arg.
                    BoolArg ba = (BoolArg)arg;
                    arg.value = Boolean.toString(ba.parse(s));
                }
            }
        }
        return 0;
    }

    private boolean ok(String s) {
        return s != null && s.length() > 0;
    }

    private void processParams() {
        for(Arg arg : allArgs) {
            if(arg.value != null) {
                params.put(arg.longName, arg.value);
            }
            else if(arg.defaultValue != null) {
                arg.value = arg.defaultValue;
                params.put(arg.longName, arg.defaultValue);
            }
            else if(arg.isRequired())
                throw new IllegalArgumentException("Required parameter: " + arg.longName);
        }
    
    }

    private boolean isParam(String s) {
        return s.startsWith("-");
    }
    private Map<String,String> params = new HashMap<String,String>();
    private List<String> operands = new LinkedList<String>();
    private Arg[] allArgs;
    private String[] cmdlineArgs;
}
