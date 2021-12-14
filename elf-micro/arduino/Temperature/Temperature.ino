/**
Temperature meaurement with a TMP37
20 mV/ degree C
TODO: hacked C++ can be greatly improved!
TODO:  use array of temperatureRange objects...
*/

const int dataPin = A0;  // A5 == 19, A0 == 14
const int redPin = 3;
const int greenPin = 5;
const int bluePin = 6;
int degF = 25;
int red, green, blue;

class temperatureRange {
  public:
  temperatureRange(int l, int h, int r, int g, int b) : red(r), green(g), blue(b), low(l), high(h) {
  }
  bool isInRange(int temp);
  int red,green,blue;
  int low;
  int high;
};

bool temperatureRange::isInRange(int temp) {
  if(temp >= low && temp <= high)
    return true;
  return false;
}

temperatureRange range1(0, 32, 0, 0, 15);
temperatureRange range2(33, 40, 0, 15, 15);
temperatureRange range3(41, 48, 0, 15, 0);
temperatureRange range4(49, 56, 15, 15, 0);
temperatureRange range5(57, 100, 15, 0, 0);

void setup() {
  Serial.begin(9600);
  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);
}
/* testing loop!! */
void test_loop() {
  //printTemperature();
  if(degF > 70)
    degF = 25;
    
  Serial.print("Temperature = ");
  Serial.print(degF++);
  Serial.print("F  -->  ");
  showTemperatureLED();
  delay(1000);
 }

void loop() {
  printTemperature();
  showTemperatureLED();
  delay(5000);
 }
 
void printTemperature() {
 
  int sensorValue = analogRead(dataPin);
  int mV = map(sensorValue, 0, 1023, 0, 5000);
  int degC = mV / 20;
  degF = degC * 1.8 + 32;
  Serial.print("Temperature = ");
  Serial.print(degF);
  Serial.println("F");
}

void showTemperatureLED() {
  if(range1.isInRange(degF))
    setColors(range1);
  else if(range2.isInRange(degF))
    setColors(range2);
  else if(range3.isInRange(degF))
    setColors(range3);
  else if(range4.isInRange(degF))
    setColors(range4);
  else if(range5.isInRange(degF))
    setColors(range5);
  rgbWrite();
}

void setColors(temperatureRange range) {
  red = range.red;
  green = range.green;
  blue = range.blue;  
}

 void rgbWrite() {
    Serial.print(red);
    Serial.print(":::");
    Serial.print(green);
    Serial.print(":::");
    Serial.println(blue);
    analogWrite(greenPin, green);
    analogWrite(redPin, red);
    analogWrite(bluePin, blue);
}
////  JUNKYARD  ////////
void loopx() {
 
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
