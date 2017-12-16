/*
 * DerbyException.java
 *
 * Created on June 10, 2007, 11:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.db;

/**
 *
 * @author bnevins
 */
public class DerbyException extends java.lang.Exception
{
    public DerbyException()
    {
    }
    public DerbyException(String msg)
    {
        super(msg);
    }
    public DerbyException(String msg, Throwable t)
    {
        super(msg, t);
    }
    public DerbyException(Throwable t1, Throwable t2)
    {
        super(t1.toString(), t2);
    }
}
