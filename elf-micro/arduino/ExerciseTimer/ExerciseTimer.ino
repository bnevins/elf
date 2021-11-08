/*
  Keyboard

  Plays a pitch for 1 second every 5 or 30 seconds
  circuit:
  - Switch on pin 2 -- connects to ground for 30 seconds.  Floating for 5 seconds
  - three 10 kilohm resistors from analog in 0 through 5 to ground
  - 8 ohm speaker on digital pin 8 through a 330 ohm resistor <8 --> resistor --> speaker hot.  Speaker ground to ground

  created November 8, 2021
 
  by Byron Nevins

  This example code is in the public domain.
*/

#include "pitches.h"

unsigned long interval = 5000;
int speakerPin = 8;
int switchPin = 2;
int speakerTime = 1000;

void setup() {
  //configure pin 2 as an input and enable the internal pull-up resistor
  pinMode(switchPin, INPUT_PULLUP);
}

void loop() {

   if (digitalRead(switchPin) == HIGH)
    interval = 5000;
  else
    interval = 30000;  
  
    tone(speakerPin, NOTE_C6, speakerTime);
    delay(interval + speakerTime);
}
