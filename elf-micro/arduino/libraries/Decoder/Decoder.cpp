#include "Decoder.h"
/*
  Decoder.h - Library for turning on one of 16 outputs.
  TTL 54154 4:16 decoder.
  Normally all the outputs are HIGH.  There are 2 enable pins.  Tie one to LOW.  The other will act as ennable/disable.  
  A LOW means enable, HIGH means disable.  
  Could double the number of outputs each time a new address bit is added.  E.g. for 64 outputs use 4 4:16 chips.  Then use the
  2 extra bits to pick the one 4:16 chip. 

  Created by Byron Nevins, January 18, 2016
  Released into the public domain.
*/

const char WARN1[] = "Which is not in the range 0-15 inclusive";

// You  have to tell me which 4 pins on the Arduino you have pinned to the 4:16 chip.  And the enable pin.

Decoder::Decoder(int _enablePin, int _pinA1, int _pinB2, int _pinC4, int _pinD8) :  
enablePin(_enablePin), 
pinA1(_pinA1), 
pinB2(_pinB2), 
pinC4(_pinC4), 
pinD8(_pinD8),
current(0){
  // Important:  No writing to Serial from constructor
  pinMode(enablePin, OUTPUT);

  // disable...
  digitalWrite(enablePin, HIGH);

  pinMode(pinA1, OUTPUT);
  digitalWrite(pinA1, LOW);

  pinMode(pinB2, OUTPUT);
  digitalWrite(pinB2, LOW);

  pinMode(pinC4, OUTPUT);
  digitalWrite(pinC4, LOW);

  pinMode(pinD8, OUTPUT);
  digitalWrite(pinD8, LOW);
}

int Decoder::enable() {
  digitalWrite(enablePin, LOW);
}

int Decoder::disable() {
  digitalWrite(enablePin, HIGH);

}

int Decoder::getCurrent() {
  return current;
}

// setCurrent(-1) disables the chip.

void  Decoder::setCurrent(int which, char* buf) {

  if(which < 0) {
  	disable();
	return;
  }

  if(which > 15)
    return ;

  current = which;

  int a1 = (current & 1) ? HIGH : LOW;
  int b2 = (current & 2) ? HIGH : LOW;
  int c4 = (current & 4) ? HIGH : LOW;
  int d8 = (current & 8) ? HIGH : LOW;

  disable();
  digitalWrite(pinA1, a1);
  digitalWrite(pinB2, b2);
  digitalWrite(pinC4, c4);
  digitalWrite(pinD8, d8);
  enable();

  if(buf)
	  sprintf(buf, "Setting the current relay to %2d, %d%d%d%d\n", current, d8, c4, b2, a1);
}

