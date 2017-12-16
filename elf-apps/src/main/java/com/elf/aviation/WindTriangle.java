/*
 * WindTriangle.java
 *
 * Created on November 30, 2001, 5:59 PM
 */

package com.elf.aviation;
import com.elf.util.Console;

/**
 *
 * @author  bnevins
 * @version 
 */
public class WindTriangle 
{
	public WindTriangle() 
	{
		readConstantData();

		while(true)
		{
			readTrueCourse();
			crunch();
			display();
		}
	}

	///////////////////////////////////////////////////////////////////////////
	
	private void readTrueCourse()
	{
		int tc = -1;
		
		if(!reuseTrueCourse)
			tc = Console.readInt("True Course " + ((gotTrueCourse) ? "(-1 to keep using previous): " : ""));
		
		if(tc < 0 && !reuseTrueCourse)
		{
			reuseTrueCourse = true;
		}
			
		gotTrueCourse = true;
		
		double d = Console.readDouble("Distance for leg: ");
		
		if(!reuseTrueCourse)
			info.setTrueCourse(tc);
		
		info.setDistance(d);
	}

	///////////////////////////////////////////////////////////////////////////
	
	private void crunch()
	{
		double wca = info.getWindSpeed() / info.getTrueAirSpeed();
		wca *= Math.sin(info.getWindDirection() - info.getTrueCourse());
		info.setWindCorrectionAngle(wca);
	
		info.setTrueHeading(info.getTrueCourse() + Math.asin(wca));
		info.setGroundSpeed(info.getTrueAirSpeed() * Math.sqrt(1 - (wca * wca)) - info.getWindSpeed() * Math.cos(info.getWindDirection() - info.getTrueCourse()));
		info.setMagneticHeading(info.getTrueHeading() + info.getVariation());
		int timeTimes10 = (int)(info.getDistance() / info.getGroundSpeed() * 600.0);
		info.setTime(((double)(timeTimes10)) / 10);
	}

	///////////////////////////////////////////////////////////////////////////
	
	private void display()
	{
		StringBuffer sb = new StringBuffer("\n");
		sb.append("Wind Correction Angle: " + toDeg(info.getWindCorrectionAngle()) + "\n");
		sb.append("True Course:           " + toDeg(info.getTrueCourse()) + "\n");
		sb.append("True Heading:          " + toDeg(info.getTrueHeading()) + "\n");
		sb.append("Magnetic Heading:      " + toDeg(info.getMagneticHeading()) + "\n");
		sb.append("Ground Speed:          " + (int)info.getGroundSpeed() + " knots\n");
		sb.append("Time:                  " + info.getTime() + " minutes\n\n");
		
		System.out.println("" + sb);
	}
		
	///////////////////////////////////////////////////////////////////////////
	
	private void readConstantData()
	{
		info.setWindSpeed(		Console.readDouble("Wind Speed: "));
		info.setWindDirection(	Console.readInt("Wind Direction: "));
		info.setTrueAirSpeed(	Console.readDouble("True Air Speed:"));
		info.setVariation(		Console.readInt("Magnetic Variation:"));
		
		
	/*private double		tc	= 0;
	private double		gs	= 0;
	private double		th	= 0;
	private double		mh	= 0;
	 **/
	}

	//////////////////////////////////////////////////////////////////////////

	private int toDeg(double r)
	{
		return (int)AviationUtils.radianToDegree(r);
	}

	//////////////////////////////////////////////////////////////////////////

	
	
	/**
	* @param args the command line arguments
	*/
	public static void main (String args[]) 
	{
		WindTriangle tri = new WindTriangle();
		System.out.println(tri.info.toString());
	}

	///////////////////////////////////////////////////////////////////////////
	
	private	WindTriangleInfo	info			= new WindTriangleInfo();
	private	boolean				reuseTrueCourse	= false;
	private	boolean				gotTrueCourse	= false;
}

/*
Wind Triangles
In all formulae, all angles are in radians. Convert back and forth as in the Great Circle section. [This is unnecessary on calculators which have a "degree mode" for trig functions. Most programming languages provide only "radian mode".]

      angle_radians=(pi/180)*angle_degrees
      angle_degrees=(180/pi)*angle_radians 
A further conversion is required if using degrees/minutes/seconds:

 angle_degrees=degrees+(minutes/60.)+(seconds/3600.)

 degrees=int(angle_degrees)
 minutes=int(60*(angle_degrees-degrees))
 seconds=60*(60*(angle_degrees-degrees)-minutes)) 
[ You may have a built-in HH <-> HH:MM:SS conversion to do this efficiently]
Let CRS=course, HD=heading, WD=wind direction (from), TAS=True airpeed, GS=groundspeed, WS=windspeed. 

Units of the speeds do not matter as long as they are all the same.

  (1) Unknown Wind:

 WS=sqrt( (TAS-GS)^2+ 4*TAS*GS*(sin((HD-CRS)/2))^2 )
 WD=CRS + atan2(TAS*sin(HD-CRS), TAS*cos(HD-CRS)-GS)  (**)
 IF (WD<0) THEN WD=WD+2*pi
 IF (WD>2*pi) THEN WD=WD-2*pi
   ( (**) assumes atan2(y,x), reverse arguments if your implementation 
 has atan2(x,y) )

  (2) Find HD, GS

 SWC=(WS/TAS)*sin(WD-CRS)
 IF (abs(SWC)>1)
      "course cannot be flown-- wind too strong"
 ELSE 
      HD=CRS+asin(SWC)
      if (HD<0) HD=HD+2*pi
      if (HD>2*pi) HD=HD-2*pi
      GS=TAS*sqrt(1-SWC^2)-WS*cos(WD-CRS)
 ENDIF
Note: 
The purpose of the "if (HD<0) HD=HD+2*pi; if (HD>2*pi) HD=HD-2*pi" is to ensure the final heading ends up in the range (0, 2*pi). Another way to do this, with the MOD function available is: 
   HD=MOD(HD,2*pi)
(3) Find CRS, GS 

  GS=sqrt(WS^2 + TAS^2 - 2*WS*TAS*cos(HD-WD))
  WCA=atan2(WS*sin(HD-WD),TAS-WS*cos(HD-WD))  (*)
  CRS=MOD(HD+WCA,2*pi)
(*) WCA=asin((WS/GS)*sin(HD-WD)) works if the wind correction angle is less than 90 degrees, which will always be the case if WS < TAS. The listed formula works in the general case 

Head- and cross-wind components.
   HW= WS*cos(WD-RD)     (tailwind negative)
   XW= WS*sin(WD-RD)     (positive=  wind from right)

where HW, XW, WS are the headwind, crosswind and wind speed. WD and RD are the wind direction (from) and runway direction. 
As usual, unless you have a version of sin and cos available that takes degree arguments, you'll need to convert to radians. 

Example: Wind 060 @ 20 departing Runway 3. 
  WS=20 knots
  WD=60 degrees = 60*pi/180 radians
  RD=30 degrees = 30*pi/180 radians
  Plugging in:
  Headwind=17.32 knots
  Crosswind = 10 knots (from right)


*/



