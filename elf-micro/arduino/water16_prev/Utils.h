#ifndef UTILS_H
#define UTILS_H

extern char buf[1000];
extern unsigned long getTotalDays();
extern unsigned long daysToMillis(unsigned long days);
extern unsigned long hoursToMillis(unsigned long hours);
extern unsigned long minutesToMillis(unsigned long minutes);
extern unsigned long secondsToMillis(unsigned long seconds);
extern bool getSkipMode();
extern void setSkipMode(bool newVal);
extern void log(char* msg);
extern void logln(char* msg);
extern void log(unsigned long num);
extern void log(int num);
extern void log(char* msg, unsigned long num);

#endif



