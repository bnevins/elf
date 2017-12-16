/*
 * ProcessManagerException.java
 * Any errors in ProcessManager will cause this to be thrown
 * @since JDK 1.4
 * @author bnevins
 * Created on October 28, 2005, 10:08 PM
 */
//NOTE: Tabs are used instead of spaces for indentation. 
//  Make sure that your editor does not replace tabs with spaces. 
//  Set the tab length using your favourite editor to your 
//  visual preference.
package com.elf.process;

public class ProcessManagerTimeoutException extends ProcessManagerException {

    public ProcessManagerTimeoutException(Throwable cause) {
        super(cause);
    }
}