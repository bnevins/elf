#include "all.h"

// *******************************************************************************************************
// *******************************************************************************************************
// ******************   Data goes here    ****************************************************************
// *******************************************************************************************************
// *******************************************************************************************************

static UL FIRST_TIME_DELAY_HOURS = 12;

Valve ValveManager::valves[] = { 
  // relay#, duration in minutes, frequency in days
  Valve(0, 60, 3),
  Valve(1, 0, 2),
  Valve(2, 21, 1),
  Valve(3, 0, 3),
  Valve(4, 0, 2),
  Valve(5, 28, 2),
  Valve(6, 30, 2),
  Valve(7, 7, 3),
};

// *******************************************************************************************************
// *******************************************************************************************************

ValveManager::ValveManager(Decoder* pDecoder) {
  if(DEBUG)
    FIRST_TIME_DELAY_HOURS = 0;
  
  Valve::setDecoder(pDecoder);
  
  numValves = sizeof(valves) / sizeof(Valve);
  nextStartTime = millis() + hoursToMillis(FIRST_TIME_DELAY_HOURS);
  isRunning = false;

  // power supply relay ACTIVE HIGH
  pinMode(VALVE_120V_PIN, OUTPUT);
  digitalWrite(VALVE_120V_PIN, LOW);
}

ValveManager::~ValveManager() {
}

void ValveManager::update() {
  // THE BUG IS IN HERE!!!!
  
  if(millis() > nextStartTime) {
    if(DEBUG) {
      nextStartTime = millis() + minutesToMillis(2) + 100;
      //sprintf(buf, "Next Start Time: %ld, Current Time= %ld\n", nextStartTime, millis());  
      //log(buf);
    }
    else
      nextStartTime = millis() + hoursToMillis(24);
      
    turnOn120();
    increment(); 
  }
  for(int i = 0; i < numValves; i++) {
    // update() returns true when the current valve time has finished.
    if(valves[i].update()) {
      if(!increment(i)) {
        isRunning = false;
        turnOff120();
        }
      else
        isRunning = true;
    }
  }
}

void ValveManager::turnOn120() {
  digitalWrite(VALVE_120V_PIN, HIGH);
}

void ValveManager::turnOff120() {
  digitalWrite(VALVE_120V_PIN, LOW);
}

int ValveManager::getRunningValve() {
  for(int i = 0; i < numValves; i++) 
    if(valves[i].isRunning())
      return i;

  return -1;
}

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

