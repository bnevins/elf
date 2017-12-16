// A01_ch9_hello_world.ino Joe Pardue 7/11/14

void setup() 
{
  Serial.begin(57600);
}
void loop()
{
  Serial.println("Hello, world!"); // send Hello, world!
  delay(250); // pause for 1/4 second
}

