/*
 * ProcessorException.java
 *
 * Created on June 10, 2006, 1:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.remodel;

/**
 *
 * @author bnevins
 */
public class ProcessorException extends java.lang.Exception
{
	
	/**
	 * Creates a new instance of <code>ProcessorException</code> without detail message.
	 */
	public ProcessorException()
	{
	}
	
	
	/**
	 * Constructs an instance of <code>ProcessorException</code> with the specified detail message.
	 * @param msg the detail message.
	 */
	public ProcessorException(String msg)
	{
		super(msg);
	}
}
