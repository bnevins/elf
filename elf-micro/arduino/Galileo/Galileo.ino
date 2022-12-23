/**
  Temperature meaurement with a TMP37
  20 mV/ degre

  This is an electronic galileo thermometer.
  Very simple with 8 colors and 8 temperature ranges

   Temp Sensor: ????
   3 color LED is common cathode.  Long lead is cathode.  R-CATHODE-G-B
   6 connections to Arduino:
   1. +5V to pin 1 of temp sensor (flat side UP, pins coming out at you, left=1, right = 3)
   2. A0 goes to pin 2 of temp sensor
   3. GND goes to Pin 2 of LED and pin 3 of temp sensor
   4. D3 to 220R to RED pin of LED (pin 1)
   5. D5 to 220R to green pin of LED (pin 3)
   6. D6 to to 220R blue pin of LED (pin 4)

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
const int numRanges = sizeof(tempRanges) / sizeof(int);;
int colorTable[][3] = {
  {255, 0, 255}, // Under 30
  {136, 0, 255},    // 30-39
  {0, 255, 127},    // 40-49
  {0, 255, 68}, // 50-59
  {0, 255, 32}, // 60-69
  {0, 255, 0},  // 70-79
  {255, 32, 0}, // 80-89
  {255, 0, 0}   // 90+
};

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

void setup() {
  Serial.begin(9600);
  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);
  printRanges();
  Serial.println("**********************************************************************************");
  Serial.println("***  Enter a temperature in degrees Fahrenheit to see the corresponding color ****");
  Serial.println("**********************************************************************************\n");
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
        Serial.println("");
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
  Serial.print("\n");
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
    debugPrint("sensor value: ");
    debugPrint(String(sensorValue));
    double mV = map(sensorValue, 0, 1023, 0, 5000);
    debugPrint(",  ");
    debugPrint(String(mV));
    debugPrint("mV");
    debugPrint(", ");

    deg = mV / 20.0;

    debugPrint(String(deg));
    debugPrint("C");
    debugPrint(",  ");

    deg = (deg * 1.8) + 32.0;
    int ret = round(deg);

    debugPrint(String(deg));
    debugPrint("F");
    debugPrint(",  rounded off: ");
    debugPrint(String(ret));
    debugPrint("F\n");
    return ret;
  }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

int cToF(int degC) {
  double d = degC;
  return int((d * 1.8) + 32.0);
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////

int fToC(int degF) {
  double d = degF;
  return int((d - 32.0) * 5.0 / 9.0);
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////

void debugPrint(String s) {
  if (debug) {
    //Serial.print(" DEBUG ******   ");
    Serial.print(s);
  }
}
