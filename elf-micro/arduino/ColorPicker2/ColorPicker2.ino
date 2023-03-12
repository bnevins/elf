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
  Serial.println("Enter RGB values as Hex digits, 2 hex digits per color.");
  Serial.println("For example bright red is FF0000.  Bright Yellow is FFFF00.  Medium blue is 000080");
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

  bool readColors() {
    int num = Serial.available();
    
    if(num < 6) {
      while(Serial.read() != -1)
        ;
      return false;
    }
      
    int r1 = readOneHex(Serial.read());
    int r2 = readOneHex(Serial.read());
    int g1 = readOneHex(Serial.read());
    int g2 = readOneHex(Serial.read());
    int b1 = readOneHex(Serial.read());
    int b2 = readOneHex(Serial.read());

    while(Serial.read() != -1)
      ;

    if(r1 < 0 || g1 < 0 || b1 < 0 || r2 < 0 || g2 < 0 || b2 < 0)
      return false;
 
    redValue = r1 * 16 + r2;
    greenValue = g1 * 16 + g2;
    blueValue = b1 * 16 + b2;
    
    
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
 
 
