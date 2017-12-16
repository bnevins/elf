// Servo Angle
// 3/7/14 Joe Pardue

#include <Servo.h> 
 
Servo myservo;  // create servo object to control a servo 
 
int pos = 0;    // variable to store the servo position 
String readString;

void setup() 
{ 
  myservo.attach(9);  // attaches the servo on pin 9 to the servo object 
  
  // initialize the serial communication:
  Serial.begin(57600);
  Serial.flush();
  Serial.println("Servo Angle 1.0");
} 
 
void loop() 
{ 
  byte angle;
  
  while (Serial.available()) {
    char c = Serial.read();  //gets one byte from serial buffer
    readString += c; //makes the string readString
    delay(2);  //slow looping to allow buffer to fill with next character
  }

  if (readString.length() >0) {
    Serial.println(readString);  //so you can see the captured string 
    int n = readString.toInt();  //convert readString into a number

    // The compass has the angles reversed from the servo angles 
    //     so we convert the input angle to fit the compass angle
    n = 180-n;
 
    Serial.print("writing Angle: ");
    Serial.println(180-n); // show angle sent
    myservo.write(n); // use converted angle

    readString=""; //empty for next input
  }   
} 
