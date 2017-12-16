#include "Decoder.h"

const char WARN1[] = "Which is not in the range 0-15 inclusive";

Decoder::Decoder(int _enablePin, int _A1, int _B2, int _C4, int _D8) :  
enablePin(_enablePin), 
A1(_A1), 
B2(_B2), 
C4(_C4), 
D8(_D8),
current(0){
  // Important:  No writing to Serial from constructor
  pinMode(enablePin, OUTPUT);
  digitalWrite(enablePin, HIGH);

  pinMode(A1, OUTPUT);
  digitalWrite(A1, LOW);

  pinMode(B2, OUTPUT);
  digitalWrite(B2, LOW);

  pinMode(C4, OUTPUT);
  digitalWrite(C4, LOW);

  pinMode(D8, OUTPUT);
  digitalWrite(D8, LOW);
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

const char* Decoder::setCurrent(int which) {
  // todo print warning
  if(which < 0 || which > 15)
    return WARN1;

  current = which;

  int a1 = (current & 1) ? HIGH : LOW;
  int b2 = (current & 2) ? HIGH : LOW;
  int c4 = (current & 4) ? HIGH : LOW;
  int d8 = (current & 8) ? HIGH : LOW;

  disable();
  digitalWrite(A1, a1);
  digitalWrite(B2, b2);
  digitalWrite(C4, c4);
  digitalWrite(D8, d8);
  enable();

  sprintf(buf, "Setting the current relay to %2d, %d%d%d%d\n", current, d8, c4, b2, a1);
  return buf;
}





