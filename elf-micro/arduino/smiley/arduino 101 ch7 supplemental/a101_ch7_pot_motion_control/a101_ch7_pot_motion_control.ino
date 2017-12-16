// A101_ch7_pot_motion_control 5/7/14 Joe Pardue

#include <Servo.h> 

Servo myservo;  // create servo object to control a servo
int sensorPin = A0;  // analog input pin
int zero = 0; // calibration reading for 0 degree
int oneeighty = 0; // calibrartion reading for 180 degree
int val; // value for the angle

void setup() {
  // attaches the servo on pin 9 to the servo
  myservo.attach(9);   
  
  Serial.begin(57600);
  Serial.println("Pot motion control rev 1.0");
}

void loop() { 
  
  if(Serial.available())
  {
    char c = Serial.read();

    if(c == 'a') // get the zero degree calibration value
    { 
      // read the value from the sensor:
      zero = analogRead(sensorPin);     
      Serial.print("You set 0 degree to: ");
      Serial.println(zero);      
    }     
    if(c == 'b') // get the 180 degree calibration value
    { 
      // read the value from the sensor:
      oneeighty = analogRead(sensorPin);    
      Serial.print("You set 180 degree to: ");
      Serial.println(oneeighty);      
    }          
    if(c == 'r') // get the angle value
    {    
      Serial.print("val: ");
      Serial.println(val);      
    }        
  }   
  
  delay(100);
  val = analogRead(sensorPin);;
  val = map(val,zero,oneeighty,0,180);
      
  // the dial is reversed for what we want so we
  // reverse the value to get the angle
  val = 180 - val;
  myservo.write(val); // use converted angle     
}

