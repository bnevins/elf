/*
 * RGB_LEDs sketch
 * RGB LEDs driven from analog output ports
*/

const int redPin   = 9;        // choose the pin for each of the LEDs
const int greenPin = 10;
const int bluePin  = 11;
const boolean invert = false; // set true if common anode, false if common cathode
const int brightness = 255; // 255 is maximum brightness
const boolean debug = true;

//const boolean noMagentaToRed = true;

int color = 0; // a value from 0 to 255 representing the hue
int R, G, B;  // the Red Green and Blue color components

void setup()
{
  // pins driven by analogWrite do not need to be declared as outputs
  Serial.begin(9600);
  analogWrite(redPin, 0);
  analogWrite(greenPin, 0);
  analogWrite(bluePin, 0);
}

void loop() {
  if(Serial.available()) {
    
    int tempF = Serial.parseInt();
    int hue = tempToHue(tempF);
    hueToRGB(hue, brightness);
    lightRGB();
    if(debug) {
      Serial.print("TEMP: ");
      Serial.print(tempF);
      Serial.print("  HUE: ");
      Serial.print(hue);
      Serial.print("  Red:   ");
      Serial.print(R);
      Serial.print("  Green: ");
      Serial.print(G);
      Serial.print("  Blue:  ");
      Serial.println(B);
      Serial.println("\n ****  Enter Temperature in Fahrenheit ******\n");
    }
  }
}
void loopx()
{
 
  hueToRGB( color, brightness);  // call function to convert hue to RGB
  // write the RGB values to the pins
  lightRGB();
  Serial.print(color);
  Serial.print("  ");
  Serial.print(R);
  Serial.print("  ");
  Serial.print(G);
  Serial.print("  ");
  Serial.print(B);
  Serial.println("");
  
  color += 1;           // increment the color  stop at 240.  Going to 255 will return to RED!
  if(color > 240) {
     color = 0;
    analogWrite(redPin, 0);
    analogWrite(greenPin, 0);
    analogWrite(bluePin, 0 );     
    delay(2000);
  }
  delay(50);
}

void lightRGB() {
  // write the RGB values to the pins
  analogWrite(redPin, R);
  analogWrite(greenPin, G);
  analogWrite(bluePin, B );

}
int tempToHue(int tempF) {
  // WINTER ==> 20F to 70F
  if(tempF < 20)
    tempF = 20;
  if(tempF > 70)
    tempF = 70;

   // map it to the 240 element hue but blue->red not red->blue!
   return map(tempF, 20, 70, 240, 0);
}
// function to convert a color to its Red, Green, and Blue components.

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
