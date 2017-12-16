// A101_ch7_pot_angle 5/7/14 Joe Pardue

#include <Servo.h> 

Servo myservo;  // create servo object to control a servo

int sensorPin = A0;  // analog input pin
int sensorValue = 0;  // store the analog input value
int zero = 0; // calibration reading for 0 degree
int oneeighty = 0; // calibrartion reading for 180 degree

void setup() {
  // attaches the servo on pin 9 to the servo
  myservo.attach(9);   
  
  Serial.begin(57600);
  Serial.println("Measure potentiometer angle rev 1.0");
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
    
  }  
  delay(1000);
  int val = sensorValue;
  val = map(val,zero,oneeighty,0,180);
      
  // the dial is reversed for what we want so we
  // reverse the value to get the angle
  //val = 180 - val;
  myservo.write(val); // use converted angle   
  Serial.println(val);  
}

