// A101_ch11_light_sensor_theremin 10/5/14 Joe Pardue

int sensorPin = A0;  // analog input pin
unsigned int sensorValue = 0;  // store the analog input value

int speaker = 6;
unsigned int freq = 0;;

void setup() {
  Serial.begin(57600);
  Serial.println("Light sensor LED Theremin rev 1.0");
}

void loop() {
  // read the value from the sensor:
  freq = analogRead(sensorPin);
  
  // map the light level to the frequency
  freq = map(freq, 1024, 0, 0, 5000); 
  // play the tone
  tone(speaker,freq);
}




