/**
RGB -- LED
*/

const int redPin = 3;
const int greenPin = 5;
const int bluePin = 6;
int redValue = 0;
int greenValue = 0;
int blueValue = 0;

void setup() {
  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);
  Serial.begin(9600);

  Serial.println("****** Color Picker *******");
  Serial.println("Enter RGB values as Hex digits, 1 hex digit per color.");
  Serial.println("For example bright red is F00.  Bright Yellow is FF0.  Medium blue is 008");
 }


void loop() {
  if(readColors()) 
    rgbWrite();
  delay(2000);
}
  void rgbWrite() {
    analogWrite(greenPin, greenValue);
    analogWrite(redPin, redValue);
    analogWrite(bluePin, blueValue);
  }

  // make just 16 gradations for each color (0-->F)
  bool readColors() {
    int num = Serial.available();
    
    if(num < 3) {
      // for instance 1 or 2 chars sent...
      while(Serial.read() != -1)
        ;
      return false;
    }
      
    int r = readOneHex(Serial.read());
    int g = readOneHex(Serial.read());
    int b = readOneHex(Serial.read());

    while(Serial.read() != -1)
      ;

    if(r < 0 || g < 0 || b < 0)
      return false;
 
    redValue = r * 17;
    greenValue = g * 17;
    blueValue = b * 17;
    
    
    Serial.println(redValue);
    Serial.println(greenValue);
    Serial.println(blueValue);
   
    return true;
  }
 
  int readOneHex(byte b) {
    if(b >= '0' && b <= '9')
      return b - '0';

    // TODO: toUppercase ???
    
    if(b >= 'A' && b <= 'F')
      return b - 'A' + 10;
    if(b >= 'a' && b <= 'f')
      return b - 'a' + 10;

    return -1;
 }
 
///////////////  JUNK BELOW //////////
 
 
