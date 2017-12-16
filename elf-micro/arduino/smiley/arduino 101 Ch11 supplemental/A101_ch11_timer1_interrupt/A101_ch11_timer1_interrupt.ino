// A101_ch11_timer_interrupt 9/28/14 Joe Pardue

#include <TimerOne.h> // Timer 1 library

volatile int oneSecondFlag = 0;
int tick = 1;

void setup(){
  // Set up the serial port
  Serial.begin(57600);
  
  // identify yourself
  Serial.println(F("A101_ch11_timer_interrupt rev. 0.01"));  
  
  // initialize timer1 interrupt
  Timer1.initialize(1000000); // call it once per second
  Timer1.attachInterrupt(myTimer1);  
}

void loop(){
  // if one second has passed
  if(oneSecondFlag) 
  {
    oneSecondFlag = 0; // set the flag to 0        
    if(tick)
    { 
      Serial.println("tick");
      tick = 0;
    } 
    else
    { 
      Serial.println("tock");
      tick = 1;
    }  
  }
}

// once per second this is called and sets the flag to 1
void myTimer1()
{
  oneSecondFlag = 1; 
}
