const int keyer = 4;
const int speakerPin = 9;
const int ledPin = 13;
const int pitch = 698;


void setup()
{
  pinMode(keyer, INPUT_PULLUP);
  pinMode(ledPin, OUTPUT);
}

 void loop()
{
  boolean up = digitalRead(keyer); // +5V == key UP, Ground is key down

  if(up)
    digitalWrite(ledPin, LOW);
  else
    digitalWrite(ledPin, HIGH);
   
   delay(10);  
 }


/***
 int pin = 13;


void loop()
{
  dot(); dot(); dot();
  dash(); dash(); dash();
  dot(); dot(); dot();
  delay(3000);
}

void dot()
{
  digitalWrite(pin, HIGH);
  delay(250);
  digitalWrite(pin, LOW);
  delay(250);
}

void dash()
{
  digitalWrite(pin, HIGH);
  delay(1000);
  digitalWrite(pin, LOW);
  delay(250);
}
***/
