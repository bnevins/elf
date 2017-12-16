const float MV_PER_STEP = (5000.0 / 1024.0);
const float VOLTS_PER_STEP = (5.0 / 1024.0);
int lightSensorPin = A0;
int lightSensorValue = 0;
int tempSensorPin = A1;
int tempSensorValue = 0;

char buf[100];
char buf2[10];
char buf3[10];

void setup() {
  Serial.begin(57600);
  Serial.println("Measure Light Sensor Voltage");
}

void loop() {
  if(Serial.available()) {
    char c = Serial.read();

    if(c== 'r') {
      // read it
      lightSensorValue = analogRead(lightSensorPin);  
      tempSensorValue = analogRead(tempSensorPin);  
      int mvLight = int(float(lightSensorValue) * MV_PER_STEP);
      int mvTemp = int(float(tempSensorValue) * MV_PER_STEP);
      sprintf(buf, "Light Sensor Value:  %04d Voltage: %d mV\n",
      lightSensorValue, mvLight);
      Serial.print(buf);  

      sprintf(buf, "Temp Sensor Value:  %04d Voltage: %d mV  Temp (C): %s  Temp(F): %s\n",
      tempSensorValue, mvTemp, 
      floatToString(getCTemp()),
      floatToString(getFTemp())
      );
      Serial.print(buf);  
    }
  }
}

float getCTemp() {
  float mv = float(analogRead(tempSensorPin)) * MV_PER_STEP;    

  return(mv - 500.0 ) / 10.0;
}

float getFTemp() {
  return getCTemp() * 1.8 + 32;
}

char* floatToString(float f) {
  dtostrf(f, 6, 1, buf2);
  return buf2;
}









