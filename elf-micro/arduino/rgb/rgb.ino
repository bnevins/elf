/**
RGB -- LED
*/

const int red = 3;
const int green = 5;
const int blue = 6;
int brightness = 0;

void setup() {
  pinMode(red, OUTPUT);
  pinMode(green, OUTPUT);
  pinMode(blue, OUTPUT);
  Serial.begin(9600);
}

void loopx() {
    if(brightness > 255)
      brightness = 0;

    rgbWrite(200, 100, 0);
    Serial.println(brightness);
    delay(500);
  
 }
void loop() {
  rgbWrite(255, 0, 0); // Red
  delay(1000);
  rgbWrite(0, 255, 0); // Green
  delay(1000);
  rgbWrite(0, 0, 255); // Blue
  delay(1000);
  rgbWrite(255, 255, 125); // Raspberry
  delay(1000);
  rgbWrite(0, 255, 255); // Cyan
  delay(1000);
  rgbWrite(255, 0, 255); // Magenta
  delay(1000);
  rgbWrite(255, 255, 0); // Yellow
  delay(1000);
  rgbWrite(255, 255, 255); // White
  delay(1000);
}
  void rgbWrite(int r, int g, int b) {
    analogWrite(green, g);
    analogWrite(red, r);
    analogWrite(blue, b);
  }