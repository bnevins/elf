// A101_ch9_light_sensor_voltage 9/28/14 Joe Pardue

#define INT0 0 // INT0 is on pin 2 in the UNO

// You must use volatile when using variables in interrupts
volatile int state = 0;

unsigned long time = 0;
int count = 0;

void setup() {
  Serial.begin(57600);
  Serial.println("Measure light sensor voltage rev 1.0");
  attachInterrupt(INT0,lightInterrupt,RISING);
}

void loop() {
  // The time statements are used to prevent multiple
  // interrupts caused by a bouncing signal on the pin
  if(state)
  {    
    if((time+5) < millis())  
    {      
      Serial.print("Light interrupt #:");
      Serial.println(count++);
      time = millis();
    }    
    state = 0;    // reset state to 0    
  } 
}

// This function is called when an external interrupt
// occurs on the pin indicated by attachInterrupt
void lightInterrupt()
{
  state = 1;//!state; 
}


