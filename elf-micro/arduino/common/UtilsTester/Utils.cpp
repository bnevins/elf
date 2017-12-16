#include "utils.h"

char buf[1000];

char* readString() {
  *buf = 0;

  if(Serial && Serial.available()) {
    int numBytes = Serial.readBytesUntil('\n', buf, 999);
    buf[numBytes] = 0;
  }
  return buf;
}

char* waitString() {
  *buf = 0;

  while(true) {
    if(Serial && Serial.available()) {
      int numBytes = Serial.readBytesUntil('\n', buf, 999);
      buf[numBytes] = 0;

      if(numBytes > 0)
        return buf;
    }
  }


  *buf = 0;

  if(Serial && Serial.available()) {
    int numBytes = Serial.readBytesUntil('\n', buf, 999);
    buf[numBytes] = 0;
  }
  return buf;
}

Utils::Utils() {

}


