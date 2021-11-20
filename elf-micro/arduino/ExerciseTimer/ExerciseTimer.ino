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
*/

#include "pitches.h"

const int     speakerPin    = 8;
const int     switchPin     = 2;
const int     speakerTime   = 650;
const int     note1         = NOTE_C6;   
const int     note2         = NOTE_G6;   
int           getReadyTime;
unsigned long interval;


void setup() {
  pinMode(switchPin, INPUT_PULLUP);
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, LOW);
}

void loop() {
    setTimes();
    tone(speakerPin, note1, speakerTime);
    digitalWrite(LED_BUILTIN, HIGH); 
    delay(getReadyTime);
    tone(speakerPin, note2, speakerTime);
    digitalWrite(LED_BUILTIN, LOW); 
    delay(interval);
}
void setTimes() {
   if (digitalRead(switchPin) == HIGH) {
    interval = 5000;
    getReadyTime = 2500;
   }
   else {
    interval = 30000;
    getReadyTime = 7000;
   }  
}

void playNotes() {
  tone(speakerPin, NOTE_C1, speakerTime);
  delay(speakerTime * 2);
  tone(speakerPin, NOTE_C2, speakerTime);
  delay(speakerTime * 2);
  tone(speakerPin, NOTE_C3, speakerTime);
  delay(speakerTime * 2);
  tone(speakerPin, NOTE_C4, speakerTime);
  delay(speakerTime * 2);
  tone(speakerPin, NOTE_C6, speakerTime);
  delay(speakerTime * 2);
  tone(speakerPin, NOTE_G6, speakerTime);
  delay(speakerTime * 2);
  tone(speakerPin, NOTE_F6, speakerTime);
  delay(speakerTime * 2);
  tone(speakerPin, NOTE_C8, speakerTime);
  delay(speakerTime * 2);
}
