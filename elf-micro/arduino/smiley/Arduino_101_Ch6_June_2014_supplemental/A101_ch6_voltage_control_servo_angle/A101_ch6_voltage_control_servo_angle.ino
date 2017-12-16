// A101_ch6_voltage_control_servo_angle 
// 3/31/14 Joe Pardue

#include <Servo.h> 
 
int sensorPin = A0;  // analog input pin
int sensorValue = 0;  // store the analog input value 
 
Servo myservo;  // create servo object to control a servo 
 
int pos = 0;    // variable to store the servo position 
String readString;

void setup() 
{ 
  // attaches the servo on pin 9 to the servo
  myservo.attach(9); 
  
  // initialize the serial communication:
  Serial.begin(57600);
  Serial.flush();
  Serial.println("Voltage control servo Angle 1.0");
} 
 
void loop() 
{ 
  if(Serial.available())
  {
    char c = Serial.read();
    if(c == 'r')
    { 
      // read the value from the sensor:
      sensorValue = analogRead(sensorPin);
      
      Serial.print("AnalogRead: ");
      Serial.print(sensorValue);      
      Serial.print("  Voltage: ");
      Serial.print(  ((5.0*(float)sensorValue)/1024.0), 3); 
	
      //float val = (float)sensorValue;
      //val = (float)map(val,0.0,1023.0,0.0,180.0);
      int val = (float)sensorValue;
      val = (float)map(val,0,1023,0.0,180);
       
      Serial.print("  Angle = ");
      Serial.println(val);
       
      myservo.write(val); // use converted angle     
      
    } 
  }  
}

