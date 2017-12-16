// A101_ch7_potentiometer_voltage 5/7/14 Joe Pardue

int sensorPin = A0;  // analog input pin
int sensorValue = 0;  // store the analog input value

void setup() {
  Serial.begin(57600);
  Serial.println("Measure potentiometer voltage rev 1.0");
}

void loop() {
  
  if(Serial.available())
  {
    char c = Serial.read();
    if(c == 'r')
    { 
      // read the value from the sensor:
      sensorValue = analogRead(sensorPin);
      
      Serial.print("Potentiometer voltage: ");
      Serial.print(sensorValue);      
      Serial.print("  Voltage: ");
      Serial.println(((5.0*(float)sensorValue)/1024.0), 3); 
    } 
  }                
}

