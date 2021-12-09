/**
Temperature meaurement with a TMP37
20 mV/ degree C
*/

void setup() {

  Serial.begin(9600);
}

void loop() {
int sensorValue = analogRead(A0);
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
