/* A101_ch11_resonant_frequency 9/30/14 Joe Pardue */

#define tonePin 6


void setup() {
 Serial.begin(57600);
 Serial.println("A101_ch11_resonant_frequency rev 1.0");
}

void loop() {
  for(int i = 4600; i < 4700; i+=10)
  {
    tone(tonePin,i);
    delay(2000);
    Serial.println("freq: ");
    Serial.println(i);
    //tone(tonePin,0);
  } 
  
}
