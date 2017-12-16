/*
 * TimerPanelInfo.java
 *
 * Created on August 15, 2007, 11:05 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.graphics;

/**
 *
 * @author bnevins
 */
public class TimerPanelInfo
{
    private double clockPointDiameter = 6;
    private double minuteHandLengthScale = 0.7;
    private double minuteHandThickness = 2;
    private double secondHandLengthScale = 0.95;
    private double secondHandThickness = 1;
    private double circlePercentageOfRealEstate = 0.9;

    public double getClockPointDiameter()
    {
        return clockPointDiameter;
    }

    public void setClockPointDiameter(double clockPointDiameter)
    {
        this.clockPointDiameter = clockPointDiameter;
    }

    public double getMinuteHandLengthScale()
    {
        return minuteHandLengthScale;
    }

    public void setMinuteHandLengthScale(double minuteHandLengthScale)
    {
        this.minuteHandLengthScale = minuteHandLengthScale;
    }

    public double getMinuteHandThickness()
    {
        return minuteHandThickness;
    }

    public void setMinuteHandThickness(double minuteHandThickness)
    {
        this.minuteHandThickness = minuteHandThickness;
    }

    public double getSecondHandLengthScale()
    {
        return secondHandLengthScale;
    }

    public void setSecondHandLengthScale(double secondHandLengthScale)
    {
        this.secondHandLengthScale = secondHandLengthScale;
    }

    public double getSecondHandThickness()
    {
        return secondHandThickness;
    }

    public void setSecondHandThickness(double secondHandThickness)
    {
        this.secondHandThickness = secondHandThickness;
    }

    public double getCirclePercentageOfRealEstate()
    {
        return circlePercentageOfRealEstate;
    }

    public void setCirclePercentageOfRealEstate(double circlePercentageOfRealEstate)
    {
        this.circlePercentageOfRealEstate = circlePercentageOfRealEstate;
    }
}
