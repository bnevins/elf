/*
 * EmailerException.java
 *
 * Created on October 6, 2007, 7:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.enterprise.email;

/**
 *
 * @author bnevins
 */
public class EmailerException extends java.lang.Exception
{
    public EmailerException()
    {
    }
    
    public EmailerException(String msg)
    {
        super(msg);
    }
    
    public EmailerException(String msg, Throwable t)
    {
        super(msg, t);
    }
}
