/*
 * UsersManagerException.java
 *
 * Created on October 6, 2007, 3:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.enterprise.auth;

/**
 *
 * @author bnevins
 */
public class UsersManagerException extends java.lang.Exception
{
    public UsersManagerException()
    {
    }

    public UsersManagerException(String msg)
    {
        super(msg);
    }

    public UsersManagerException(String msg, Throwable t)
    {
        super(msg, t);
    }
}
