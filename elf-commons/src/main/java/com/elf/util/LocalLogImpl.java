/*
 * LocalLogImpl.java
 *
 * Created on December 8, 2005, 1:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.util;

import java.util.logging.Logger;

/**
 *
 * @author bnevins
 */
public final class LocalLogImpl {
    
    public LocalLogImpl() 
    {
        localstrings = new LocalStringsImpl();
        loggerName = localstrings.get("logger");
        setLogger();
    }
    public LocalLogImpl(String loggerName) 
    {
        this.loggerName = loggerName;
        localstrings = new LocalStringsImpl();
        setLogger();
    }

    public LocalLogImpl(String loggerName, Class clazz) 
    {
        this.loggerName = loggerName;
        localstrings = new LocalStringsImpl(clazz);
        setLogger();
    }
    
    public void severe(String index, Object... obj)
    {
    }
    private void setLogger()
    {
        logger = Logger.getLogger(loggerName);
    }
    private LocalStringsImpl localstrings;
    private Logger logger;
    private String loggerName;
}
