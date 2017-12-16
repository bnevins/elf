// A101_ch10_light_sensor_LED_control 7/2/14 Joe Pardue

#include <EEPROM.h>

// Set the EEPROM memory locations for the calibration values
#define LOW_ADDRESS 0
#define HIGH_ADDRESS 1

int sensorPin = A0;  // analog input pin
unsigned int sensorValue = 0;  // store the analog input value
int ledPin = 9; // the LED anode is attached to pin 9
unsigned int lowLight = 0; // ADC reading for least light
unsigned int highLight = 0; // ADC reading for most light

void setup() {
  Serial.begin(57600);
  Serial.println("Light sensor LED control rev 1.0");
  
  // set the ledPin mode to OUTPUT
  pinMode(ledPin, OUTPUT); 

  // get lowlight from EEPROM
  lowLight = EEPROM.read(LOW_ADDRESS); 
  // get highlight from EEPROM
  highLight = EEPROM.read(HIGH_ADDRESS);
  
  Serial.print("In setup, lowLight = ");
  Serial.println(lowLight);
  Serial.print("In setup, highLight = ");
  Serial.println(highLight);
  
  
}

void loop() {
  unsigned int brightness;
  
  // check for calibration values
  if(Serial.available())
  {
    char c = Serial.read();
 
    if(c == 'l') // get the lowLight calibration value
    {  
      // read the value from the sensor: 
      lowLight = analogRead(sensorPin); 
      EEPROM.write(LOW_ADDRESS, lowLight);
      Serial.print("You set lowLight to: "); 
      Serial.println(lowLight);      
    }      
    if(c == 'h') // get the highLight calibration value 
    { 
      // read the value from the sensor: 
      highLight = analogRead(sensorPin);
      EEPROM.write(HIGH_ADDRESS, highLight);
       
      Serial.print("You set highLight to: ");
      Serial.println(highLight);      
    }
  } 
    
  // read the value from the sensor:
  sensorValue = analogRead(sensorPin);
  
  Serial.print("sensorValue: "); 
  Serial.println(sensorValue);
      
  // map the LED brighness from the ADC readings to PWM values
  //brightness = (unsigned int)map(sensorValue, lowLight, highLight, 0, 255);
  //brightness = map(sensorValue, 0, 1023, 0, 255);
  
  float temp = (highLight - lowLight)/255;
  brightness = (unsigned int)((float)sensorValue * temp);
  
  
  Serial.print("brightness = ");
  Serial.println(brightness);  
/*  Serial.print("sensorValue = ");
  Serial.println(sensorValue);
  Serial.print("lowLight = ");
  Serial.println(lowLight);  
  Serial.print("highLight = ");
  Serial.println(highLight);  
*/  
  // the map function is not perfect, so...
  if(brightness > 255)brightness = 255;
  if(brightness < 0) brightness = 0;
  
  analogWrite(ledPin, brightness); // set PWM for LED brightness

/*  Serial.print("brightness = ");
  Serial.println(brightness);
*/  
  delay(1000);
}

