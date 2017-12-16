#include "all.h"

static bool skip;
extern bool getSkipMode() { 
  return skip; 
}
extern void setSkipMode(bool newVal) {
  skip = newVal; 
}
char buf[1000];

void log(char* msg) {
  if(SERIAL_OK)
    Serial.print(msg);
}

void logln(char* msg) {
  if(SERIAL_OK)
    Serial.println(msg);
}

void log(unsigned long num) {
  if(SERIAL_OK)
    Serial.print(num);
}

void log(int num) {
  if(SERIAL_OK)
    Serial.print(num);
}
void log(char* msg, unsigned long num) {
  log(msg);
  log(num);
  log("\n");
}

unsigned long getTotalDays() {
  // debug -- use 2 minute days

    if(DEBUG)
    return millis() / minutesToMillis(2);
  else
    return ( millis() / daysToMillis(1L) );
}

unsigned long daysToMillis(unsigned long days) {
  return hoursToMillis(days * 24L); 
}

unsigned long hoursToMillis(unsigned long hours) {
  return minutesToMillis(hours * 60L); 
}

unsigned long minutesToMillis(unsigned long minutes) {
  return secondsToMillis(minutes * 60L); 
}

unsigned long secondsToMillis(unsigned long seconds) {
  return seconds * 1000L; 
}



