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
  IMPORTANT:  tone() is asynchronous!

  Nov 18 change - 
  pin8 to base of 2N3904 transistor.  +5 to speaker, - speaker to Collector.  Emitter to ground

  Nov 23, 2021 -- added red and green leds to pins 6,4   
*/

#include "pitches.h"

const int     speakerPin    = 9;
const int     speakerTime   = 2000;
const int     note1         = NOTE_F5;   
const int     note2         = NOTE_G6;   
const int     note3         = NOTE_G4;   
int           liftTime = 60000;  //msec
unsigned long restTime = 180000; // msec
bool          longRestTime = true;


void setup() {
}

void loop() {
    myTone(note1, speakerTime * 2);
    
    if(longRestTime)      
      reminderDelay();
    else
      delay(restTime);
}

void reminderDelay() {
  for(int i = 0; i < 2; i++) {
    delay(restTime / 3);
    tone(speakerPin, note3, speakerTime / 2);   
  }
  delay(restTime / 3);
}

void myTone(int note, int time) {
  tone(speakerPin, note, time);
  delay(time);
}
