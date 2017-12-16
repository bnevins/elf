/* 
 * IMPORTANT -- don't use pins 0, 1 for digital I/O -- they are needed for serial IO
 * IMPORTANT -- convention -- valves start at pin   6 and go sequentially.  Currently limited to
 * 8.  To upgrade add a 4bit decoder chip.  Then only 4 pins needed for 16 valves.
 * IMPORTANT:  Kootek valve board uses active **low**
 */

#include "all.h"

//boolean skipMode = false;
ValveManager valveManager;

volatile int stepPressed = 0;
void stepSwitchISR() {
  // DO NOT SERIAL PRINT HERE!!!!
  // important!  Actively check the pin!!!
  stepPressed = !digitalRead(INTERRUPT_PIN);
}

void inputs_setup() {
  //pinMode(INTERRUPT_PIN, INPUT_PULLUP);
  //pinMode(2, OUTPUT);   
  //attachInterrupt(INERRUPT_NUM, stepSwitchISR, RELAY_OFF);
  //attachInterrupt(0, stepSwitchISR, FALLING);
}

int handleSkip() {

  if(stepPressed == 0) 
    return 0;

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

  logln("Serial Communications at 9600");
  inputs_setup();
}

void loop()
{
  handleSkip(); 
  handleValves();
  delay();
 }



















