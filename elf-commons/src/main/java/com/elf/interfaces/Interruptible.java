package com.elf.interfaces;

/** 
 * An interface for classes that can be polled to see
 *  if they've been interrupted. 
 */

public interface Interruptible 
{
	boolean isInterrupted();
}
