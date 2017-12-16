1
// Processing program to receive strings from the Arduino

import processing.serial.*;

Serial mySerial;  // Create object from Serial class
String input;     // Data received from the serial port

void setup()
{
  // Find the COM port you are using by looking in the Arduino
  // Tools/SerialPort menu item.
  mySerial = new Serial(this, "COM5", 57600); 
}

void draw()
{
  if ( mySerial.available() > 0) 
  {  // If data is available,
    input = mySerial.readStringUntil('\n'); // put it in the input string
  } 
  println(input); //print it out in the console
}


