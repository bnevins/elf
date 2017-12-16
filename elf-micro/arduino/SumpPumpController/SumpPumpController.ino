#include "all.h"

static bool firstTime = true;
static int detectorPin = 6;
static int pumpPin = 8;
static bool pumpState = false;
static bool prevPumpState = false;

// TODO -- I added these variables so we can be smart about setting the delay time.  E.g. small duty cycle should make the delay longer.
static unsigned long pumpOnTimeInSeconds = 0;
static unsigned long pumpOffTimeInSeconds = 0;

// Likely that I'll adjust this
static unsigned long delayInSeconds = 300;
static unsigned long delayEndTime = 0;
static unsigned long currentPumpStart = 0;

#define debug Serial.print

// change for active high!
#define RELAY_OFF HIGH
#define RELAY_ON LOW

//
// Note:  We are using an input with a ~~ 20-50K pullup resistor.  Perfect.  Makes things simpler.  We don't need a resistor!
// This means, though, that the logical sense is inverted.  Just remember this:
// FLOOD == LOW INPUT  (circuit completes the circuit, input goes low)
// one wire goes to ground.  The other goes to "detectorPin"


void setup() {
  pinMode(detectorPin, INPUT_PULLUP);
  pinMode(pumpPin, OUTPUT);
  digitalWrite(pumpPin, RELAY_OFF);
  pumpState = false;
  prevPumpState = false;
  debug("firstTime!!!\n");
  Serial.begin(57600);  //Begin serial communcation
  Serial.print("Serial Communications at 57600\n");
}

void loop() {
  bool flooded = isFlooded();
  bool pumpOn = isPumpOn();
  bool inDelay = isInDelay();
  delay(1000);

  if(pumpOn)
    ++pumpOnTimeInSeconds;
  else
    ++pumpOffTimeInSeconds;

  debug4("flooded, pumpOn, inDelay, prevPumpState:  : ", flooded, pumpOn, inDelay, prevPumpState);

  if (isInDelay())
    return;

  // The water was just completely pumped dry...
  else if (flooded == false && prevPumpState == true) {
    debug("#########  TRUE -> FALSE\n");
    turnPumpOff();
    startDelay();
    prevPumpState = false;
  }
  // the pump was off -- we've flooded!!  But wait 5 minutes to avoid the constant on/off
  else if (flooded == true && prevPumpState == false) {
    debug("#########  FALSE -> TRUE\n");
    turnPumpOn();
    prevPumpState = true;
  }
}

bool isPumpOn() {
  return pumpState;
}

bool isFlooded() {
  return digitalRead(detectorPin) == LOW;
}

bool isInDelay() {
  unsigned long now = millis();
  debug("now, delayendtime\n");
  debug(now);
  debug(" --- ");
  debug(delayEndTime);
  debug("\n");

  if (delayEndTime == 0)
    return false;

  if (now < delayEndTime)
    return true;

  delayEndTime = 0;
  return false;
}

void startDelay() {
  unsigned long now = millis();
  unsigned long secondsPumpRan = (now-currentPumpStart) / 1000;
  
  if( secondsPumpRan < 30 )
    delayInSeconds *= 2;

  else if (secondsPumpRan > 300)
    delayInSeconds /= 2;
    
  if(delayInSeconds < 60)
     delayInSeconds = 60;

  if(delayInSeconds > 4800)
     delayInSeconds = 4800;

  delayEndTime = millis() + (delayInSeconds * 1000);
}

void turnPumpOn() {
  debug("Turn Pump On!!!!!!");
  currentPumpStart = millis();

  pumpState = true;
  digitalWrite(pumpPin, RELAY_ON);
}

void turnPumpOff() {
  debug("Turn Pump Off!!!!!!");
  pumpState = false;
  digitalWrite(pumpPin, RELAY_OFF);

}

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


