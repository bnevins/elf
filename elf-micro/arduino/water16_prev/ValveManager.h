#ifndef VALVEMANAGER_H
#define VALVEMANAGER_H

#ifndef VALVE_H
#include "Valve.h"
#endif

#include <Arduino.h>
// Adjustable 
// Adjustable 

class ValveManager {
public:
  static Valve valves[]; 
  ValveManager();
  ~ValveManager();

  //void add(Valve* v);
  void update();
  //void add(UL _durationMinutes = 60, UL _frequency = 2);
  //  void add(Valve* pValve);
  bool increment();
  bool increment(int currentValve);
  int getRunningValve();
  //  int getNextPin();
private:  
  void enableSystem();
  void disableSystem();
  int numValves;
  UL nextStartTime;
  bool isRunning;  
};

#endif


