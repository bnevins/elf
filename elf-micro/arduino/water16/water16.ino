#include <Decoder.h>

#include "all.h"

Decoder decoder(12, 7,6,5,4);
//Decoder decoder(enablepin, A_PIN, B_PIN, C_PIN, D_PIN);

/* 
 * IMPORTANT -- don't use pins 0, 1 for digital I/O -- they are needed for serial IO
 * IMPORTANT:  Kootek valve board uses active **low**
 */


//boolean skipMode = false;
ValveManager valveManager(&decoder);

volatile int stepPressed = 0;
void stepSwitchISR() {
  // DO NOT SERIAL PRINT HERE!!!!
  // important!  Actively check the pin!!!
  stepPressed = !digitalRead(INTERRUPT_PIN);
}

int handleSkip() {

  if(stepPressed == 0) { 
    //logln("stepPressed == FALSE");
    return 0;
  }
  //else
    //logln("stepPressed == TRUE");

  //skipMode = true;
  log("XXXXX from handle skip -- stepPressed is set to: ", stepPressed);
  valveManager.increment();
  delay(500);  
  stepPressed = 0;
  return 1;
}

void handleValves() {
  valveManager.update();
}

void delay() {
  if(DEBUG)
    delay(100);
  else
    delay(1000);
}
////////////////////////////////////////////////////////////
/////////   magic functions below
///////////////////////////////////////////////////////////
void setup()
{
  //if(SERIAL_OK)
  Serial.begin(57600);  //Begin serial communcation

  logln("Serial Communications at 57600");
}

void loop()
{
  //logln("ZZZZZ");
  handleSkip(); 
  handleValves();
  //logln("XXXXXXX");
  delay();
}


