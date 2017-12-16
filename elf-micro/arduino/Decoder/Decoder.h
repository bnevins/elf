#ifndef DECODER_H
#define DECODER_H

#include "Arduino.h"

class Decoder {
public:
  Decoder(int enablePin, int A1, int B2, int C4, int D8);
  int enable();
  int disable();
  const char* setCurrent(int whichOne);
  int getCurrent();

private:
  int current;
  const int enablePin; 
  const int A1;
  const int B2; 
  const int C4; 
  const int D8;
  char buf[200];
};



#endif







