/*
 * MathUtils.java
 *
 * Created on March 11, 2007, 1:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.math;

/**
 *
 * @author bnevins
 */
public class MathUtils
{
	public static double[] quadratic(double a, double b, double c)
	{
		double[] ret = new double[2];
		//  (-b +- sq-rt(b^2 - 4ac)) /2a
		// = (f +- d) / e
		double d = Math.sqrt((b*b) - (4.0 * a * c));
		double e = 2.0 * a;
		double f = -1 * b;
		ret[0] = (f + d) / e;
		ret[1] = (f - d) / e;
		
		return ret; 
	}
	public static void main(String[] args)
	{
		double[] roots = quadratic(
				Double.parseDouble(args[0]),
				Double.parseDouble(args[1]),
				Double.parseDouble(args[2]));
		
		System.out.println("Root1: " + roots[0] + ", Root2: " + roots[1]);
	}
}
