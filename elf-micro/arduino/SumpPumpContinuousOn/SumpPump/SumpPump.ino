#include "all.h"

static bool firstTime = true;
static int floatPin = 7;
static int pumpPin = 13;
static bool pumpState = false;
static bool prev_float= false;

// Likely that I'll adjust this
static unsigned long delayInSeconds = 10;
static unsigned long delayEndTime = 0;

#define debug Serial.print

// change for active low!
#define RELAY_OFF LOW
#define RELAY_ON HIGH

void debug4(char* msg, bool b1, bool b2, bool b3, bool b4) {
  Serial.print(msg);
  Serial.print(b1);
  Serial.print(", ");
  Serial.print(b2);
  Serial.print(", ");
  Serial.print(b3);
  Serial.print(", ");
  Serial.print(b4);
  Serial.print("\n");
}

void setup() {
  pinMode(floatPin, INPUT);
  pinMode(pumpPin, OUTPUT);
  pumpState = false;  
  turnPumpOff();
  debug("firstTime!!!\n");
  Serial.begin(57600);  //Begin serial communcation
  Serial.print("Serial Communications at 57600\n");
}

void loop() {
  bool floatOn = isFloatOn();
  bool pumpOn = isPumpOn();
  bool inDelay = isInDelay();
  delay(1000);
  debug4("floatOn, pumpOn, inDelay, prev_float:  : ", floatOn, pumpOn, inDelay, prev_float);

  if(floatOn == false && prev_float == true) {
    debug("#########  TRUE -> FALSE\n");
    startDelay();
    prev_float = false;
    turnPumpOff();
  }

  else if(floatOn == true && prev_float == false) {
    debug("#########  FALSE -> TRUE\n");

    if(!isInDelay()) {
      turnPumpOn();
      prev_float = true;      
    }
  }
}

bool isPumpOn() {
  return pumpState;
}

bool isFloatOn() {
  return digitalRead(floatPin) == HIGH;
}

bool isInDelay() {
  unsigned long now = millis();
  debug("now, delayendtime\n");
  debug(now);
  debug(" --- ");
  debug(delayEndTime);
  debug("\n");
  if(delayEndTime == 0)
    return false;


  if(now < delayEndTime)
    return true;

  delayEndTime = 0;
  return false;
}

void startDelay(){
  delayEndTime = millis() + (delayInSeconds * 1000);
}

void turnPumpOn() {
  debug("Turn Pump On!!!!!!");
  pumpState = true;
  digitalWrite(pumpPin, RELAY_ON); 
}

void turnPumpOff() {
  debug("Turn Pump Off!!!!!!");  
  pumpState = false;
  digitalWrite(pumpPin, RELAY_OFF); 
}


























