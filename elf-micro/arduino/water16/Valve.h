#ifndef VALVE_H
#define VALVE_H
typedef unsigned long UL;
extern const int RELAY_ON;
extern const int RELAY_OFF;


class Valve {
  public:
  Valve(int id, int durationMinutes, int frequency);
  ~Valve();
  bool turnOn();
  void turnOff();
  bool isRunning();
  bool isStopped();
  bool update();
  static void setDecoder(Decoder* _pDecoder) { pDecoder = _pDecoder; }
  
  private:
  bool isTodayOk();
  int state; // RELAY_ON or RELAY_OFF
  int id;
  UL durationMillis;
  UL frequencyDays; // 1 == everyday, 2 == every other day 3 == every third day, etc.  
  UL startTime;
  static Decoder* pDecoder;
};

#endif
