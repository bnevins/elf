/**
Temperature meaurement with a TMP37
20 mV/ degree C
This is an electronic galileo thermometer. 
Very simple with 5 colors and 5 temperature ranges
*/

const int temperatureDataPin = A0;  // A5 == 19, A0 == 14
const int redPin = 3;
const int greenPin = 5;
const int bluePin = 6;
const boolean debug = true;
const boolean invert = false; // common cathode:false, common anode:true
int R, G, B;
// Centigrade temp ranges - each number is the top temp for the range
// "100" means fucking hot!!
int tempRanges[] = { -1, 7, 15, 24, 29, 100};
int colorTable[][3] = { {1,2,3}, {1,2,3}, {1,2,3}, {9,9,9}, {9,9,9}, {9,9,9} };

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

void setup() {
  Serial.begin(9600);
  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);
  printRanges();
  Serial.print("Enter a temperature in degrees Centigrade to see the corresponding color");
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

void loop() {
  int degC = getTemperatureC();
  setRGBColor(degC);
  lightRGB();
  delay(5000);
 }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

void printRanges() {
  Serial.println("Here is the top temperature for each range along with its RGB color");
  
  for(int i = 0; i < sizeof(tempRanges)/sizeof(int); i++) {
    Serial.print(i);
    Serial.print("  ");
    Serial.print(tempRanges[i]);
    Serial.print("C     RGB= ");
    Serial.print(colorTable[i][0]);
    Serial.print(":::");
    Serial.print(colorTable[i][1]);
    Serial.print(":::");
    Serial.println(colorTable[i][2]);
  }
  Serial.print("\n\n\n");
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

int getTemperatureC() {
  int degC = 0;

  // if someone types in a temperature -- use it!
  if(Serial.available()) {
    degC = Serial.parseInt();
  }
  else {
    int sensorValue = analogRead(temperatureDataPin);
    int mV = map(sensorValue, 0, 1023, 0, 5000);
    degC = mV / 20;
  }
  return degC;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

int cToF(int degC) {
    return degC * 1.8 + 32;
 }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

void setRGBColor(int tempC) {
   // 5 temperature ranges.  Each element is the highest temp for the range
   
   
   if(debug) {
    Serial.print("Temp: ");
    Serial.print(tempC);
    Serial.print("C");
    Serial.print("   RGB:  ");
    Serial.print(R);
    Serial.print(":::");
    Serial.print(G);
    Serial.print(":::");
    Serial.println(B);
  }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

 void lightRGB() {
  analogWrite(redPin, R);
  analogWrite(greenPin, G);
  analogWrite(bluePin, B);
}
