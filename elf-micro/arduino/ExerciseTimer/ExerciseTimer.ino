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
const int     timeSwitchPin = 7;
const int     redLED        = 3;
const int     greenLED      = 5;
const int     speakerTime   = 650;
const int     note1         = NOTE_C6;   
const int     note2         = NOTE_G6;   
const int     note3         = NOTE_C7;   
int           getReadyTime;
unsigned long interval;
bool          longInterval = false;


void setup() {
  pinMode(timeSwitchPin, INPUT_PULLUP);
  pinMode(redLED, OUTPUT);
  pinMode(13, OUTPUT);
  digitalWrite(redLED, LOW);
  digitalWrite(13, HIGH);
  //playNotes();
}

void loop() {
    setTimes();
    tone(speakerPin, note1, speakerTime * 2);
    digitalWrite(greenLED, LOW); 
    digitalWrite(redLED, HIGH); 
      digitalWrite(13, LOW);

    delay(getReadyTime);
    tone(speakerPin, note2, speakerTime);
    digitalWrite(greenLED, HIGH); 
    digitalWrite(redLED, LOW);   
    digitalWrite(13, HIGH);

    if(longInterval)
      reminderDelay();
    else
      delay(interval);
}
void setTimes() {
   if (digitalRead(timeSwitchPin) == HIGH) {
    interval = 5000;
    getReadyTime = 2500;
    longInterval = false;
   }
   else {
    interval = 30000;
    getReadyTime = 5000;
    longInterval = true;
   }  
}

void reminderDelay() {
  for(int i = 0; i < 2; i++) {
    delay(interval / 3);
    tone(speakerPin, note3, speakerTime / 2);   
  }
  delay(interval / 3);
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
  tone(speakerPin, NOTE_C7, speakerTime);
  delay(speakerTime * 2);
  tone(speakerPin, NOTE_C8, speakerTime);
  delay(speakerTime * 2);
}
