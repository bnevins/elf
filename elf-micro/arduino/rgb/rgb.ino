/**
RGB -- LED
*/

const int redPin = 3;
const int greenPin = 5;
const int bluePin = 6;
int redValue, greenValue, blueValue;

int brightness = 0;

void setup() {
  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);
  Serial.begin(9600);

  Serial.println(readOneHex('0'));
  Serial.println(readOneHex('3'));
  Serial.println(readOneHex('A'));
  Serial.println(readOneHex('a'));
  Serial.println(readOneHex('F'));
  Serial.println(readOneHex('f'));
  Serial.println(readOneHex('G'));
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
    
    if(num < 3)
      return false;
      
    int r = readOneHex(Serial.read());
    int g = readOneHex(Serial.read());
    int b = readOneHex(Serial.read());

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
 
///////////////  JUNK //////////
 bool readColors1() {
    int num = Serial.available();
    
    if(num < 9)
      return false;
      
    Serial.print("Num chars available: ");
    Serial.println(num);
    String s = Serial.readString();
    Serial.print("String length == ");
    Serial.println(s.length());
    String redS = s.substring(0, 3);
    String greenS = s.substring(3, 6);
    String blueS = s.substring(6, 9);
    redValue = redS.toInt();
    greenValue = greenS.toInt();
    blueValue = blueS.toInt();
    Serial.println(redValue);
    Serial.println(greenValue);
    Serial.println(blueValue);
    return true;
  }

 
