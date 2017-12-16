#include "all.h"

// *******************************************************************************************************
// *******************************************************************************************************
// ******************   Data goes here    ****************************************************************
// *******************************************************************************************************
// *******************************************************************************************************

static UL FIRST_TIME_DELAY_HOURS = 12;

Valve ValveManager::valves[] = { 
  // pin, duration in minutes, frequency in days
  Valve(4, 60, 3),
  Valve(5, 0, 2),
  Valve(6, 21, 1),
  Valve(7, 0, 3),
  Valve(8, 0, 2),
  Valve(9, 28, 2),
  Valve(10, 30, 2),
  Valve(11, 7, 3),
};

// *******************************************************************************************************
// *******************************************************************************************************

ValveManager::ValveManager() {
  if(DEBUG)
    FIRST_TIME_DELAY_HOURS = 0;

  numValves = sizeof(valves) / sizeof(Valve);
  nextStartTime = millis() + hoursToMillis(FIRST_TIME_DELAY_HOURS);
  isRunning = false;

  // power supply relay ACTIVE HIGH
  pinMode(VALVE_120V_PIN, OUTPUT);

  // DECODER TTL 54154 4:16 ==> active LOW
  pinMode(DECODER_ENABLE_PIN, OUTPUT);

  disableSystem();
}

ValveManager::~ValveManager() {
}

void ValveManager::update() {

  if(millis() > nextStartTime) {
    if(DEBUG) {
      nextStartTime = millis() + minutesToMillis(2) + 100;
      sprintf(buf, "Next Start Time: %ld, Current Time= %ld\n", nextStartTime, millis());  
      log(buf);
    }
    else
      nextStartTime = millis() + hoursToMillis(24);

    enableSystem();
    increment(); 
  }
  for(int i = 0; i < numValves; i++) {
    // update() returns true when the current valve time has finished.
    if(valves[i].update()) {
      if(!increment(i)) {
        isRunning = false;
        disableSystem();
      }
      else
        isRunning = true;
    }
  }
}

void ValveManager::enableSystem() {
  digitalWrite(VALVE_120V_PIN, HIGH);
  digitalWrite(DECODER_ENABLE_PIN, LOW);  
}

void ValveManager::disableSystem() {
  digitalWrite(VALVE_120V_PIN, LOW);
  digitalWrite(DECODER_ENABLE_PIN, HIGH);  
}

int ValveManager::getRunningValve() {
  for(int i = 0; i < numValves; i++) 
    if(valves[i].isRunning())
      return i;

  return -1;
}

//int ValveManager::getNextPin() {
//  return currentPin++;  
//}

bool ValveManager::increment() {

  return increment(getRunningValve());
}

// actually simple.  Just go to the next valve and start it.
bool ValveManager::increment(int retiredValve) {

  if(++retiredValve >= numValves)
    return false; // all done!

  // some are normally skipped...
  // turnOn return true if it really started...

  while( ! valves[retiredValve++].turnOn()) {
    if(retiredValve >= numValves)
      return false;
  }

  return true;
}




