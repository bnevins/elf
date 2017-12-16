#ifndef DATA_INCLUDED
#define DATA_INCLUDED
// Kootek 5V relay board is looking for ground sink to turn on

static const int DEBUG = 1;
static const bool SERIAL_OK = true;

// momentary NO switch.  Listen as a ISR
static const int INTERRUPT_PIN = 2;
static const int INTERRUPT_NUM = 0;
static const int VALVE_120V_PIN = 13;
static const int DECODER_ENABLE_PIN = 12;


#endif

///////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////

//
// JUNKYARD BELOW !!!!
//


/*
  //setRelay(4);
 //updateRelays();
 
 //digitalWrite(10, LOW);
 setRelay(0);
 updateRelays();
 delay(1000);
 setRelay(1);
 updateRelays();
 delay(1000);
 setRelay(2);
 updateRelays();
 delay(1000);
 
 static int i = 0;
 
 if(i >= NUM_VALVES)
 i = -1;
 if(stepPressed) {
 //noInterrupts();
 //stepPressed = 0;
 //interrupts();
 log(i);
 setRelay(i++);
 updateRelays();
 delay(150);  
 stepPressed = 0;
 }
 static unsigned long counter = 0;
 if((++counter % 100000) == 0)
 logloglog(counter);
// clock spills over every 49 days or so (32 bit in msec)
// we NEVER let that happen
void handleClock() {
  unsigned long now = millis(); 

  if(millis() > timeToResetClock) {
    resetMillis();
    nextStartTime -= now;
  }
  if(DEBUG) {
    Serial.print("CLOCK RESET ");
    Serial.print("nextStartTime: ");
    Serial.println(nextStartTime);
  }
  for(int i = 0; i < NUM_VALVES; i++)  
    valves[i].end -= now;  

}
// internal clock!
extern volatile unsigned long timer0_millis;

void resetMillis(){
  uint8_t oldSREG = SREG;
  cli();
  timer0_millis = (unsigned long)0;
  SREG = oldSREG;
}



 */





