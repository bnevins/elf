/**
Temperature meaurement with a TMP37
20 mV/ degree C
*/

const int dataPin = A0;  // A5 == 19, A0 == 14

void setup() {

  Serial.begin(9600);
}

void loop() {
 
  int sensorValue = analogRead(dataPin);
   Serial.println(sensorValue);
  // print out the value you read:
  int mV = map(sensorValue, 0, 1023, 0, 5000);
  Serial.print(mV);
  Serial.print(" mV    ");
  int degC = mV / 20;
  int degF = degC * 1.8 + 32;
  Serial.print(degC);
  Serial.print("C  ");
  Serial.print(degF);
  Serial.print("F  \n");
  delay(5000); 
}
