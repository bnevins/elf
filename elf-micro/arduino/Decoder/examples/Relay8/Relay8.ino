#include "Decoder.h"

const int enablepin = 12;
const int A_PIN = 7;
const int B_PIN = 6;
const int C_PIN = 5;
const int D_PIN = 4;
const int DELAY=2000;
const int NUM_RELAYS = 8;

int numLoops = 0;

Decoder decoder(enablepin, A_PIN, B_PIN, C_PIN, D_PIN);

void setup()
{
  Serial.begin(57600);
}

void loop()
{
  if(numLoops > 1) {
    decoder.disable();
    return;
  }
  else
    ++numLoops;

  for(int i = 0; i < NUM_RELAYS; i++) {
    Serial.print(decoder.setCurrent(i));
    delay(DELAY);
  }
}

