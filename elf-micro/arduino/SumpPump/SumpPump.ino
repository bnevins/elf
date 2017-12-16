#include "all.h"

/**
  1. the float goes HIGH.
  2. that gives power to this Arduino
  3. Which waits X minutes, and then gives power to the pump
  4. Wait to get powered off
  5. GOTO 1 !!!

  We need no inputs!  The float switch will simply turn our power on.
  We act like a fancy one-shot timer
*/

static int pumpPin = 8;
static unsigned long delayInSeconds = 300;
static unsigned long delayEndTime = 0;
static bool pumpState = false;
#define debug Serial.print

// change for active low!
#define RELAY_OFF LOW
#define RELAY_ON HIGH


void setup() {
  Serial.begin(57600);  //Begin serial communcation
  Serial.print("Serial Communications at 57600\n");
  pinMode(pumpPin, OUTPUT);
  turnPumpOff();
  startDelay();
}

void loop() {
  if (isPumpOn() || isInDelay()) {
    delay(1000);
    return;
  }

  if (!isPumpOn())
    turnPumpOn();
}

bool isPumpOn() {
  return pumpState;
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

