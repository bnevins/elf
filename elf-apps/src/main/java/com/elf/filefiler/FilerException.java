/*
 * FilerException.java
 *
 * Created on December 24, 2006, 10:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.filefiler;

/**
 *
 * @author bnevins
 */
public class FilerException extends java.lang.Exception
{
    
    /**
     * Creates a new instance of <code>FilerException</code> without detail message.
     */
    public FilerException()
    {
    }
    
    
    /**
     * Constructs an instance of <code>FilerException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public FilerException(String msg)
    {
        super(msg);
    }

    public FilerException(Throwable t)
    {
        super(t);
    }
}
