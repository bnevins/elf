int led =13;

void setup() {
  pinMode(led, OUTPUT);
}

void loop() {
  digitalWrite(led, HIGH);
  delay(1000);
  digitalWrite(led, LOW);
  delay(100);
  Serial.begin(9600);
  Serial.println("xxxxx");
}

