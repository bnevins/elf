// A101_ch11_alarms 9/30/14 Joe Pardue
#include <TimerOne.h> // Timer 1 library

#define highTone 4530
#define lowTone 3500

int speaker = 6;   // the number of the speaker driver pin
int alarmArray[100]; // array to hold the arlarm tones
int arrayCount = 0; // initialize the array count

void setup(){
  // Set up the serial port
  Serial.begin(57600);
  
  // identify yourself
  Serial.println(F("a101_ch11_alarm rev. 0.01"));  
  
  // initialize timer1
  Timer1.initialize(125000); // interrupts 8 times a second
  Timer1.attachInterrupt(myTimer1); 
}

void loop(){
  threeBeep();
  delay(3000); 
  threeBeepWarbleUp();
  delay(3000);
  threeBeepWarbleDown();
  delay(3000);  
  threeBeepWarbleUpDown();
  delay(3000);
  threeBeepWarbleDownUp();
  delay(3000);
  sos();
  delay(5000); 
}

void sos(){
 int sos[] = {0, 0, highTone, 0, highTone, 0, highTone, 0, highTone, highTone, highTone, 0, highTone, highTone, highTone, 0, highTone, highTone, highTone, 0, highTone, 0, highTone, 0, highTone,0};  

  arrayCount = 25;
  for(int i = 0; i<=arrayCount; i++)
  {
    alarmArray[i] = sos[i];
  }
}

void threeBeep(){
 int threeBeep[] = {0, 0, highTone, 0, highTone, 0, highTone};  
  arrayCount = 6;
  for(int i = 0; i<=arrayCount; i++)
  {
    alarmArray[i] = threeBeep[i];
  }
}

void threeBeepWarbleUp(){
 int threeBeepWarble[] = {0, 0, highTone, lowTone, 0, highTone, lowTone, 0, highTone, lowTone};  
  arrayCount = 9;
  for(int i = 0; i<=arrayCount; i++)
  {
    alarmArray[i] = threeBeepWarble[i];
  }
}

void threeBeepWarbleUpDown(){
 int threeBeepWarble[] = {0, 0, highTone, lowTone, highTone, 0, highTone, lowTone, highTone, 0, highTone, lowTone, highTone};  
  arrayCount = 12;
  for(int i = 0; i<=arrayCount; i++)
  {
    alarmArray[i] = threeBeepWarble[i];
  }
}

void threeBeepWarbleDownUp(){
 int threeBeepWarble[] = {0, 0, lowTone, highTone, lowTone, 0, lowTone, highTone, lowTone, 0, lowTone, highTone, lowTone};  
  arrayCount = 12;
  for(int i = 0; i<=arrayCount; i++)
  {
    alarmArray[i] = threeBeepWarble[i];
  }
}

void threeBeepWarbleDown(){
 int threeBeepWarble[] = {0, 0, lowTone, highTone,  0, lowTone, highTone, 0, lowTone, highTone};  
  arrayCount = 9;
  for(int i = 0; i<=arrayCount; i++)
  {
    alarmArray[i] = threeBeepWarble[i];
  }
}

// Called 8 times per second
// Checks alarm array count is > 0
// if so it plays the tone in the alarm array 
// and decrements the count.
void myTimer1()
{  
  if((arrayCount > 0)){
   if(alarmArray[arrayCount] == 0) noTone(speaker);
   else tone(speaker,alarmArray[arrayCount]);
   arrayCount--;
  }  
}

