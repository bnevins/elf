/**
  Temperature meaurement with a TMP37
  20 mV/ degree C
  This is an electronic galileo thermometer.
  Very simple with 8 colors and 8 temperature ranges
*/

// FIXME off by 1 when enter F --> C  --> F

const int temperatureDataPin = A0;  // A5 == 19, A0 == 14
const int redPin = 3;
const int greenPin = 5;
const int bluePin = 6;
const boolean debug = true;
const boolean invert = false; // common cathode:false, common anode:true
int R = 0, G = 0, B = 255; // very non-OOP but WTF!  Simple.  Use these variables at runtime to set the color values so I don't have to mess with passing an object around...
// Fahrenheit temp ranges - each number is the bottom temp for the range
int tempRanges[] = { -3000, 30, 40, 50, 60, 70, 80, 90};
int colorTable[][3] = { {255, 0, 255}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}, {13, 14, 15}, {0, 255, 0}, {19, 20, 21}, {255, 0, 0} };
int numRanges;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

void setup() {
  numRanges = sizeof(tempRanges) / sizeof(int);
  Serial.begin(9600);
  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);
  printRanges();
  Serial.println("Enter a temperature in degrees Fahrenheit to see the corresponding color");
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

void loop() {
  int degF = getTemperatureF();
  setRGBColor(degF);
  lightRGB();
  delay(5000);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

void setRGBColor(int tempF) {
  // n temperature ranges.  Each element is the lowest temp for the range
  // figure out what range we're in and set R,G,B to the values set for that range
  // start with checking against hottest temp range and work down to coldest

  for (int i = numRanges - 1; i >= 0; i--) {
    if (tempF >= tempRanges[i]) {
      R = colorTable[i][0];
      G = colorTable[i][1];
      B = colorTable[i][2];

      if (debug) {
        Serial.print("Temp: ");
        Serial.print(tempF);
        Serial.print("F");
        Serial.print("   RGB:  ");
        Serial.print(R);
        Serial.print(":::");
        Serial.print(G);
        Serial.print(":::");
        Serial.println(B);
      }
      break;
    }
  }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

void lightRGB() {
  analogWrite(redPin, R);
  analogWrite(greenPin, G);
  analogWrite(bluePin, B);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

void printRanges() {
  Serial.println("Here is the low temperature for each range along with its RGB color");

  for (int i = 0; i < sizeof(tempRanges) / sizeof(int); i++) {
    //Serial.print(i);
    //Serial.print("  ");
    Serial.print(tempRanges[i]);
    Serial.print("F     RGB= ");
    Serial.print(colorTable[i][0]);
    Serial.print(":::");
    Serial.print(colorTable[i][1]);
    Serial.print(":::");
    Serial.println(colorTable[i][2]);
  }
  Serial.print("\n\n\n");
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

int getTemperatureF() {
  double deg;

  // fixme -- input fahrenheit
  // if someone types in a temperature -- use it!
  if (Serial.available()) {
    return Serial.parseInt();
  }
  else {
    int sensorValue = analogRead(temperatureDataPin);
    double mV = map(sensorValue, 0, 1023, 0, 5000);
    deg = mV / 20.0;
    deg = (deg * 9.0 / 5.0) + 32.0;
  }
  return round(deg);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

int cToF(int degC) {
  double d = degC;
  return int((d * 1.8) + 32.0);
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////

int fToC(int degF) {
  double d = degF;
  return int((d - 32.0) * 5.0/9.0);
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////

void debugPrint(String s) {
  if (debug) {
    Serial.print(" DEBUG ******   ");
    Serial.println(s);
  }
}
