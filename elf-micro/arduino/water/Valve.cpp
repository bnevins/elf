#include "all.h"
//#include "Utils.h"

// the relay board is active low.
const int RELAY_ON = LOW;
const int RELAY_OFF = HIGH;

//static void test() {
//  int led =13;
//  pinMode(led, OUTPUT);
//
//  for(int i = 0; i < 25; i++) {
//    digitalWrite(led, HIGH);
//    delay(500);
//    digitalWrite(led, LOW);
//    delay(1500);
//  }
//}



Valve::Valve(int pinNumber, int durationMinutes, int frequency) {
  /** CAREFUL !!!!
   * we do not yet have a serial connection.  If you log in here, 
   * it will break all serial!
   */
  pin =  pinNumber;  
  frequencyDays = frequency;
  durationMillis = durationMinutes * 60000L;
  state = RELAY_OFF;
  pinMode(pin, OUTPUT);
  digitalWrite(pin, RELAY_OFF);
  startTime = 0L;

  if(DEBUG)  
    durationMillis = durationMinutes * 1000L / 3L;
}

Valve::~Valve() {
  turnOff(); 
}

/**
 * returning true means that we were on -- and we are now off.  
 * Anything else means false;
 */
bool Valve::update() {
  if(isStopped()  || millis() <= ( durationMillis + startTime) )
    return false; 

  // i.e. the valve IS running -- and it has timed out...
  turnOff();
  return true;
} 

bool Valve::turnOn() {
  // run today?
  if(!isTodayOk()) 
    return false;

  // No chattering if zero duration.  
  if(durationMillis < 500)
    return false;
    
  sprintf(buf, "Turning on pin #%d for %ld seconds\n", pin, (durationMillis / 1000));
  log(buf);
  state = RELAY_ON;  
  digitalWrite(pin, RELAY_ON);
  startTime = millis();
  return true;
}

void Valve::turnOff() {
  log("Turning Off pin# ", pin);
  state = RELAY_OFF;  
  digitalWrite(pin, RELAY_OFF);
  startTime = 0L;
}

bool Valve::isTodayOk() {
  if( (getTotalDays() % frequencyDays ) == 0L)
    return true;

  if(getSkipMode()) {
    log("Skip Mode Enabled.  Running this valve even though this is not the right day");
    return true;
  }

  sprintf(buf, "Not the right day.  Current Day = %ld, Frequency Days = %ld, Pin #%d\n", getTotalDays(), frequencyDays, pin);
  log(buf);
  return false;     
}

bool Valve::isRunning() {
  return state == RELAY_ON;
}  

bool Valve::isStopped() {
  return state == RELAY_OFF;
}  






