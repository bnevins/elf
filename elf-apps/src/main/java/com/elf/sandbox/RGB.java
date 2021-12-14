/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.sandbox;

/**
 *
 * @author bnevins
 */
public class RGB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RGB rgb  = new RGB();
        for(int i = 0; i < 255; i++)
            rgb.loop();
    }


int redPin   = 3;        // choose the pin for each of the LEDs
int greenPin = 5;
int bluePin  = 6;
boolean invert = false; // set true if common anode, false if common cathode

int color = 0; // a value from 0 to 255 representing the hue
int R, G, B;  // the Red Green and Blue color components

void setup()
{
  // pins driven by analogWrite do not need to be declared as outputs
}

void loop()
{
  int brightness = 255; // 255 is maximum brightness
  hueToRGB( color, brightness);  // call function to convert hue to RGB
  // write the RGB values to the pins
  analogWrite(redPin, R);
  analogWrite(greenPin, G);
  analogWrite(bluePin, B );

  color++;           // increment the color
  if(color > 255)    //
     color = 0;
       delay(10);
  
}

// function to convert a color to its Red, Green, and Blue components.

void hueToRGB( int hue, int brightness)
{
    int scaledHue = (hue * 6);
    // segment 0 to 5 around the color wheel
    int segment = scaledHue / 256;
    // position within the segment 
    int segmentOffset = scaledHue - (segment * 256); 

    int complement = 0;
    int prev = (brightness * ( 255 -  segmentOffset)) / 256;
    int next = (brightness *  segmentOffset) / 256;
    if(invert)
    {
      brightness = 255-brightness;
      complement = 255;
      prev = 255-prev;
      next = 255-next;
    }

    switch(segment ) {
    case 0:      // red
     R = brightness;
     G = next;
     B = complement;
    break;
    case 1:     // yellow
    R = prev;
    G = brightness;
    B = complement;
    break;
    case 2:     // green
    R = complement;
    G = brightness;
    B = next;
    break;
    case 3:    // cyan
    R = complement;
    G = prev;
    B = brightness;
    break;
    case 4:    // blue
    R = next;
    G = complement;
    B = brightness;
    break;
   case 5:      // magenta
    default:
    R = brightness;
    G = complement;
    B = prev;
    break;
    }
      System.out.println("Segment: " + segment + "  Hue: " + color + "   R,G,B: " + R + ", " + G + ", " + B);

}    

    private void analogWrite(int redPin, int R) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void delay(int i) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
