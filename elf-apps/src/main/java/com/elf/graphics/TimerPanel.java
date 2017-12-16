/*
 * TimerPanel.java
 *
 * Created on August 14, 2007, 11:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.graphics;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 *
 * @author bnevins
 */
public class TimerPanel extends JPanel
{
    public TimerPanel(TimerPanelInfo tpi)
    {
        info = tpi;
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        
        Dimension d = getSize();
        
        // todo
        // make it 90% so we don't have ultra-complex calculations for
        // the clock points at the edges -- e.g. 15min, 45 min.
        
        diameter = Math.min(d.width, d.height) * 0.9;
        radius = diameter / 2.0;
        System.out.println("Diameter: " + diameter);
        System.out.println("Size: " + d.width + ", " + d.height);
        circleRect = new Rectangle2D.Double(0, 0, diameter, diameter);
        Ellipse2D circle = new Ellipse2D.Double();
        circle.setFrame(circleRect);
        //g2.draw(circle);
        
        drawClockPoints(g2);
        drawTime(g2);
        drawMinuteHand(g2);
        drawSecondHand(g2);
    }
    private void drawClockPoints(Graphics2D g2)
    {
        drawClockCenter(g2);
        for(double i = 1.0; i <= 12.0; i += 1.0)
        {
            Point2D.Double raw = new Point2D.Double();
            raw.y = Math.cos((i / 12.0) * PI_2);
            raw.x = Math.sin((i / 12.0) * PI_2);
            drawClockPoint(g2, translate(raw));
        }
    }

    private void drawClockCenter(Graphics2D g2)
    {
        Point2D.Double center = new Point2D.Double(radius, radius);
        drawClockPoint(g2, center, 1.4);
    }
    
    private void drawClockPoint(Graphics2D g2, Point2D.Double pt)
    {
        drawClockPoint(g2, pt, 1.0);
    }
    
    private void drawClockPoint(Graphics2D g2, Point2D.Double pt, double scale)
    {

        double x = pt.x;
        double y = pt.y;
        // todo
        double pointDiameter = CLOCK_POINT_SIZE * scale;
        double offset = pointDiameter / 2.0;
        
        System.out.println("POINT: " + x + ", " + y);
        
        Rectangle2D rect = new Rectangle2D.Double(x - offset, y - offset, pointDiameter, pointDiameter);
        //g2.fill(rect);
        Ellipse2D circle = new Ellipse2D.Double();
        circle.setFrame(rect);
        g2.fill(circle);
    }

    private void drawTime(Graphics2D g2)
    {
        Stroke originalStroke = g2.getStroke();
        drawMinuteHand(g2);
        drawSecondHand(g2);
        g2.setStroke(originalStroke);
    }

    private void drawMinuteHand(Graphics2D g2)
    {
        Stroke stroke = new BasicStroke(5);
        g2.setStroke(stroke);
        Point2D.Double origin = new Point2D.Double(radius, radius);
        Point2D.Double end = new Point2D.Double(
            Math.sin((time / 60.0) * PI_2),
            Math.cos((time / 60.0) * PI_2));

        
        //Point2D p1 = translate(end, 0.75);
        //Point2D p2 = translate(end, 0.5);
        
        end = translate(end, info.getMinuteHandLengthScale());
        
        //System.out.println("100%: " + end);
        //System.out.println("50%: " + p2);
        //System.out.println("75%: " + p1);
        
        
        
        Line2D.Double line = new Line2D.Double(origin, end);
        g2.draw(line);
    }

    private void drawSecondHand(Graphics2D g2)
    {
        Stroke stroke = new BasicStroke(2);
        g2.setStroke(stroke);
        Point2D.Double origin = new Point2D.Double(radius, radius);
        Point2D.Double end = new Point2D.Double(
            Math.sin((seconds / 60.0) * PI_2),
            Math.cos((seconds / 60.0) * PI_2));
        end = translate(end, info.getSecondHandLengthScale());
        Line2D.Double line = new Line2D.Double(origin, end);
        g2.draw(line);
    }

    private Point2D.Double translate(Point2D.Double raw)
    {
        return translate(raw, 1.0);
    }
    private Point2D.Double translate(Point2D.Double raw, double lengthFactor)
    {
        Point2D.Double pt = new Point2D.Double();
        
        pt.x = radius + (raw.x * radius * lengthFactor);
        pt.y = radius - (raw.y * radius * lengthFactor);
        
        
        //pt.x = radius * lengthFactor * (raw.x + 1);
        //pt.y = radius * lengthFactor * (1 - raw.y);
        return pt;
    }

    public static void main(String args[])
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                JFrame frame = new JFrame("Analog Timer");
                TimerPanelInfo tpi = new TimerPanelInfo();
                TimerPanel panel = new TimerPanel(tpi);
                frame.setSize(200,250);
                frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                frame.getContentPane().add(panel);
                frame.setVisible(true);
            }
        });
    }


    private Rectangle2D circleRect;
    private double diameter;
    private double radius;
    private static double CLOCK_POINT_SIZE = 6;
    private final static double PI_2 = Math.PI * 2;
    
    // temporary time indicator
    
    private final double time = 25.0;
    private final double seconds = 40.0;
    private TimerPanelInfo info;
}
