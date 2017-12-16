// Arduino_101_ch6_servo_velocity
// 3/26/14 Joe Pardue

#include <Servo.h>

// create servo object to control a servo 
Servo myServo;   
 
// Declare global variables
int startAngle,endAngle, velocity;
String readString;
 
void setup() 
{ 
  myServo.attach(9);  // attaches the servo on pin 9 to the servo object 
  
  // initialize the serial communication:
  Serial.begin(57600);
  //Serial.flush();
  Serial.println("Servo Angle Velocity 1.0");
  Serial.println("Use:");
  Serial.println("Send vxxx where xxx is 0 to 100 to set velocity.");
  Serial.println("Send sxxx where xxx is 0 to 180 for start angle");
  Serial.println("Send exxx where xxx is 0 to 180 for end angle");
  Serial.println("Send g to move from start to end atvelocity");
} 
 
void loop() 
{ 
  char command;
  String temp; 

  while (Serial.available()) {
    char c = Serial.read();  //gets one byte from serial buffer
    readString += c; //makes the string readString
    delay(2);  //slow to allow buffer to fill with next character
  }
  
  if(readString.length() > 0) 
  {
    command = readString.charAt(0);
    
    if(command == 'v')// set the velocity
    {
      Serial.print("Set velocity to: ");
      temp = readString.substring(1);
      velocity = temp.toInt();
      Serial.println(velocity);
    }
    else if(command == 's')// set the start angle
    {
      Serial.print("Set start angle: ");
      temp = readString.substring(1);
      startAngle = temp.toInt();
      if(startAngle < 0) startAngle = 0;
      if(startAngle > 180) startAngle = 180;
      Serial.println(startAngle);      
    }
    else if(command == 'e')// set the end angle
    {
      Serial.print("Set end angle: ");
      temp = readString.substring(1);
      endAngle = temp.toInt();
      if(endAngle < 0) endAngle = 0;
      if(endAngle > 180) endAngle = 180;
      Serial.println(endAngle);      
    }
    else if(command == 'g')// move the pointer
    {   
      if(startAngle < endAngle)
      {
        for(int i = startAngle ; i < endAngle; i++)
        {
          // 180 - i because the compass is reversed
          myServo.write(180-i); 
          delay(velocity);
        } 
      }
      else
      {
         for(int i = startAngle ; i > endAngle; i--)
        {
          // 180 - i because the compass is reversed
          myServo.write(180-i);
          delay(velocity);
        }   
      }     
    }
    else
    {
      Serial.println("Incorrect command character");
    } 
   readString = ""; // clear out the string
  }
}



