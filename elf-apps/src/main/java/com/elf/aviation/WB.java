/*
 * WB.java
 *
 * Created on May 27, 2001, 10:01 AM
 */

package com.elf.aviation;
import com.elf.util.Console;

/**
 *
 * @author  bnevins
 * @version 
 */
public class WB 
{
	public WB()
	{
		readData();
		calcMoments();
		calcCG();
		check();
	}
	
	
	private void check()
	{
		pr("Total Weight: " + totalWt + " lbs\n");
		pr("CG: " + cg + " inches aft of datum");

		if(totalWt > maxWt)
		{
			pr("*** " + (totalWt - maxWt) + "lbs. overweight ****");
			return;
		}
		
		checkCG();
	}
	
	
	private void checkCG()
	{
		if(cg > 47.25)
		{
			pr("*** CG too far aft! ***");
			return;
		}
		if(cg < 35)
		{
			pr("*** CG too far forward! ***");
			return;
		}
		
		if(cg > 39.5)
		{
			pr("*** CG OK ***");
			return;
		}
		
		// for 35 < CG < 39.5 -- we need to check weight...
		
		if(totalWt < 1960.0 + ( 97.777 * (cg - 35)))
		{
			pr("*** CG OK ***");
		}
		else
		{
			pr("Airplane is too heavy for a CG that far forward");
		}
	}
	
	
	private void readData()
	{
		fp			= Console.readInt("Front Passengers: ");
		rp			= Console.readInt("Rear Passengers: ");
		fuel		= Console.readInt("Fuel (gal): ") * 6;
		bagFront	= Console.readInt("Front Baggage: ");
		bagRear		= Console.readInt("Rear Baggage: ");
	}
	
	
	private void calcMoments()
	{
		fpMoment		= fp		* fp_arm;
		rpMoment		= rp		* rp_arm;
		fuelMoment		= fuel		* fuel_arm;
		bagFrontMoment	= bagFront	* bagFront_arm;
		bagRearMoment	= bagRear	* bagRear_arm;
		airplaneMoment	= airplane	* airplane_arm;
	}

	
	private void calcCG()
	{
		totalWt		= fp + rp + fuel + bagFront + bagRear + airplane;
		totalMoment	= fpMoment + rpMoment + fuelMoment + bagFrontMoment + bagRearMoment + airplaneMoment;
		cg			= totalMoment / totalWt;
		
	}
	
	
	public static void main (String args[]) 
	{
		new WB();
	}

	public static void pr(String s)
	{
		System.out.println(s);
	}
	
		
	private double cg;

	private double fp = 340;
	private double rp = 340;
	private double fuel = 240;
	private double bagFront = 10;
	private double bagRear = 0;
	private double totalWt;

	private double fpMoment;
	private double rpMoment;
	private double fuelMoment;
	private double bagFrontMoment;
	private double bagRearMoment;
	private double airplaneMoment;
	private double totalMoment;
	
	private static final double	fp_arm			= 37;	// inches
	private static final double	rp_arm			= 73;	// inches
	private static final double	bagFront_arm	= 95;	// inches
	private static final double	bagRear_arm		= 123;	// inches
	private static final double	fuel_arm		= 48;	// inches
	private static final double	airplane_arm	= 39.06;// inches

	private static final double	airplane		= 1467;	// lbs
	private static final double	maxWt			= 2400;	// lbs
}
