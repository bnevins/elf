/*
 * AviationUtils.java
 *
 * Created on November 30, 2001, 6:26 PM
 */

package com.elf.aviation;

/**
 *
 * @author  bnevins
 * @version 
 */
public class AviationUtils 
{
	private AviationUtils() 
	{
	}

	//////////////////////////////////////////////////////////////////////////
	
	static double degreeToRadian(double deg)
	{
		// 2*pi radian = 360 degrees
		return fixRadian(PI * deg / 180.0);
	}

	//////////////////////////////////////////////////////////////////////////
	
	static double radianToDegree(double rad)
	{
		return fixDegrees(rad * 180.0 / PI);
	}

	//////////////////////////////////////////////////////////////////////////
	
	static double fixDegrees(double toFix)
	{
		if(toFix > -0.1 && toFix < 0.1)
			return 0;
		if(toFix < -360)
			return fixDegrees(toFix + 360);
		else if(toFix > 360)
			return fixDegrees(toFix - 360);
		else
			return toFix;
	}

	//////////////////////////////////////////////////////////////////////////
	
	static double fixRadian(double toFix)
	{
		if(toFix < 0)
			return fixRadian(toFix + TWO_PI);
		else if(toFix > TWO_PI)
			return fixRadian(toFix - TWO_PI);
		else
			return toFix;
	}

	//////////////////////////////////////////////////////////////////////////
	
	private final static double	PI		= Math.PI;
	private final static double	TWO_PI	= Math.PI * 2.0;
	
	/**
	* @param args the command line arguments
	*/
	public static void main (String args[]) 
	{
		double[] degs = { 360, 720, 721, -3600, 90, 180, 270, 0 };
		double[] rads = { PI/2, PI, PI * 1.5, TWO_PI, PI * 100, PI * (-100), 0};
		
		for(int i = 0; i < degs.length; i++)
			System.out.println("" + degs[i] + " degrees = " + degreeToRadian(degs[i]) + " radians");

		for(int i = 0; i < rads.length; i++)
			System.out.println("" + rads[i] + " radians = " + radianToDegree(rads[i]) + " degrees");
	}

}
