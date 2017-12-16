#include "utils.h"

void setup() {

  Serial.begin(57600);
}

void loop() {
  //delay(1000);

  char* s = waitString();

  if(strlen(s) <= 0) {
    Serial.println(".");
    delay(1000);
    return;
    
  }

  String string  = s;

    

  char* s2 = waitString();
  String string2 = s2;
  Serial.println(string);
  Serial.println(string2);
  
}

void loop2() {
  // using a constant String:
  String stringOne = "Hello String";     
  Serial.println(stringOne);      // prints "Hello String"

  // converting a constant char into a String:
  stringOne =  String('a');        
  Serial.println(stringOne);       // prints "a"

  // converting a constant string into a String object:
  String stringTwo =  String("This is a string");  
  Serial.println(stringTwo);      // prints "This is a string"

  // concatenating two strings:
  stringOne =  String(stringTwo + " with more"); 
  // prints "This is a string with more":
  Serial.println(stringOne);      

  // using a constant integer:
  stringOne =  String(13);        
  Serial.println(stringOne);      // prints "13"

  // using an int and a base:
  stringOne =  String(analogRead(A0), DEC);  
  // prints "453" or whatever the value of analogRead(A0) is
  Serial.println(stringOne);      

  // using an int and a base (hexadecimal):
  stringOne =  String(45, HEX);   
  // prints "2d", which is the hexadecimal version of decimal 45:
  Serial.println(stringOne);      

  // using an int and a base (binary)
  stringOne =  String(255, BIN);  
  // prints "11111111" which is the binary value of 255
  Serial.println(stringOne);      

  // using a long and a base:
  stringOne =  String(millis(), DEC);
  // prints "123456" or whatever the value of millis() is: 
  Serial.println(stringOne);      

  // do nothing while true:
  while(true);

}




