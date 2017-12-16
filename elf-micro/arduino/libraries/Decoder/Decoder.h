#ifndef DECODER_H
#define DECODER_H

#include "Arduino.h"

class Decoder {
public:
  Decoder(int _enablePin, int _pinA1, int _pinB2, int _pinC4, int _pinD8);
  int enable();
  int disable();
  void setCurrent(int whichOne, char* buf = 0);
  int getCurrent();

private:
  int current;
  const int enablePin; 
  const int pinA1;
  const int pinB2; 
  const int pinC4; 
  const int pinD8;
};



#endif







