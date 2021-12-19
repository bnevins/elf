/**
Temperature meaurement with a TMP37
20 mV/ degree C
TODO: hacked C++ can be greatly improved!
TODO:  use array of temperatureRange objects...
*/

const int temperatureDataPin = A0;  // A5 == 19, A0 == 14
const int redPin = 3;
const int greenPin = 5;
const int bluePin = 6;
const int locationSwitchPin = 7;
const int seasonSwitchPin = 8;
const boolean debug = true;
//enum Season { indoor_winter, outdoor_winter, indoor_summer, outdoor_summer };

// change this for indoor vs. oudoor, seasons, etc.
//const Season seasonAndLocation = indoor_winter;

int lowestTemp = 20; 
int highestTemp = 70;
const int brightness = 255; // 255 is maximum brightness
const boolean invert = false; // common cathode:false, common anode:true

//int degF = 25;
int R, G, B;


void setup() {
  Serial.begin(9600);
  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);
  pinMode(seasonSwitchPin, INPUT_PULLUP);
  pinMode(locationSwitchPin, INPUT_PULLUP);
  setSeasonAndLocation();
}


void loop() {
  setSeasonAndLocation();
  int degF = getTemperature();
  setRGBColor(degF);
  lightRGB();
  delay(5000);
 }

 void setSeasonAndLocation(){
    bool indoor = digitalRead(locationSwitchPin); // switch OPEN (HIGH) == indoor
    bool winter = digitalRead(seasonSwitchPin);   // switch OPEN (HIGH) == winter

    if(indoor && winter) {
      lowestTemp = 60; 
      highestTemp = 75;
    } else if(!indoor && winter) {
      lowestTemp = 20; 
      highestTemp = 70;
    } else if(indoor && !winter) {
      lowestTemp = 65; 
      highestTemp = 80;
    } else { // !indoor && !winter
      lowestTemp = 60; 
      highestTemp = 95; 
    }
 }
int getTemperature() {
  int degF = 0;

  // if someone types in a temperature -- use it!
  if(Serial.available()) {
    degF = Serial.parseInt();
  }
  else {
    int sensorValue = analogRead(temperatureDataPin);
    int mV = map(sensorValue, 0, 1023, 0, 5000);
    int degC = mV / 20;
    degF = degC * 1.8 + 32;
  }

  return degF;
}

void setRGBColor(int tempF) {
  int hue = tempToHue(tempF);
  hueToRGB(hue, brightness);
   if(debug) {
    Serial.print("Temp: ");
    Serial.print(tempF);
    Serial.print("   Hue: ");
    Serial.print(hue);
    Serial.print("   RGB:  ");
    Serial.print(R);
    Serial.print(":::");
    Serial.print(G);
    Serial.print(":::");
    Serial.println(B);
  }
}

int tempToHue(int tempF) {
  tempF = constrain(tempF, lowestTemp, highestTemp);
  return map(tempF, lowestTemp, highestTemp, 240, 0);  
}

 void lightRGB() {
  analogWrite(greenPin, G);
  analogWrite(redPin, R);
  analogWrite(bluePin, B);
}

// borrowed code
void hueToRGB( int hue, int brightness)
{
    unsigned int scaledHue = (hue * 6);
    // segment 0 to 5 around the color wheel
    unsigned int segment = scaledHue / 256;
    // position within the segment 
    unsigned int segmentOffset = scaledHue - (segment * 256); 

    unsigned int complement = 0;
    unsigned int prev = (brightness * ( 255 -  segmentOffset)) / 256;
    unsigned int next = (brightness *  segmentOffset) / 256;
    if(invert)
    {
      brightness = 255-brightness;
      complement = 255;
      prev = 255-prev;
      next = 255-next;
    }

    switch(segment ) {
    case 0:      // red
      R = brightness;
      G = next;
      B = complement;
    break;
    case 1:     // yellow
      R = prev;
      G = brightness;
      B = complement;
    break;
    case 2:     // green
      R = complement;
      G = brightness;
      B = next;
    break;
    case 3:    // cyan
      R = complement;
      G = prev;
      B = brightness;
    break;
    case 4:    // blue
      R = next;
      G = complement;
      B = brightness;
    break;
   case 5:      // magenta
    default:
      R = brightness;
      G = complement;
      B = prev;
    break;
    }
}













////  JUNKYARD  ////////
void loopx() {
 
  int sensorValue = analogRead(temperatureDataPin);
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
