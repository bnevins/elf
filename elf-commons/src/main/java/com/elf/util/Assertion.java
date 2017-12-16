package com.elf.util;


/**
 * A class for assertion checking
 *
 * @version 1.00 1 May 1999
 * @version 1.10 5 June 2000
 * @version 2.0 Nov 12, 2004
 * @author Byron Nevins
 */

public class Assertion
{
	/**
	 * Check an assertion
	 * @param b the condition to check
	 * @throws Assertion.Failure if condition not true
	 */
	
	public static void check(boolean b)
	{
		assert b;

		if(!b)
			throw new IllegalArgumentException();
	}

	/**
	 * Check an assertion
	 * @param b the condition to check
	 * @param mesg a string describing the check
	 * @throws IllegalArgumentException or AssertionError if condition not true
	 */
	
	public static void check(boolean b, String mesg)
	{
		assert b : mesg;
		
		if(!b)
			throw new IllegalArgumentException(mesg);
	}
	/**
	 * Check an assertion
	 * @param s the String to check
	 * @throws Assertion.Failure if condition not true
	 */
	
	public static void check(String s)
	{
		assert StringUtils.ok(s);

		if(!StringUtils.ok(s))
			throw new IllegalArgumentException();
	}

	/**
	 * Check an assertion
	 * @param obj the Object to check
	 * @throws Assertion.Failure if obj is null
	 */
	
	public static void check(Object obj)
	{
		assert obj != null;

		if(obj == null)
			throw new IllegalArgumentException();
	}

	/**
	 * Check an assertion
	 * @param s the String to check
	 * @param mesg a string describing the check
	 * @throws IllegalArgumentException or AssertionError if condition not true
	 */
	
	public static void check(String s, String mesg)
	{
		assert StringUtils.ok(s) : mesg;

		if(!StringUtils.ok(s))
			throw new IllegalArgumentException(mesg);
	}

	public static void main(String[] args)
	{
		check(true);
		check(false, "foobar");
	}

}

