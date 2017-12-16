
// a101_ch9_time_display.pde Joe Pardue 7/13/14
// Modification of TimeSerial.ped
 
#include <Time.h> 

// time sync to PC is HEADER 
// followed by unix time_t as ten ascii digits
#define TIME_MSG_LEN  11   
// Header tag for serial time sync message
#define TIME_HEADER  'T'   
// ASCII bell character requests a time sync message 
#define TIME_REQUEST  7    

byte integer[] = {0x3F, 0x06, 0x5B, 0x4F, 0x66, 
                  0x6D, 0x7D, 0x07, 0x7F, 0x6F,
                  0x08};
byte character[] = {0x77, 0x7C, 0x39, 0x5E, 0x79, 0x71,
                    0x6F, 0x76, 0x30, 0x1E, 0x70, 0x38,
                    0x15, 0x54, 0x3F, 0x73, 0x67, 0x50, 
                    0x6D, 0x78, 0x3E, 0x1C, 0x2A, 0x46, 
                    0x6E, 0x52};

#define DPMASK 0x80
#define GMASK 0x40
#define FMASK 0x20
#define EMASK 0x10
#define DMASK 0x08
#define CMASK 0x04
#define BMASK 0x02
#define AMASK 0x01

#define DPPIN 9
#define GPIN 13
#define FPIN 12
#define EPIN 6
#define DPIN 7
#define CPIN 8
#define BPIN 10
#define APIN 11

#define PAUSE 500

// lowest segment starts with LED 6
// hightest segment ends with LED 12
int low = 6;
int high = 13;

void setup()  {
  
   // Set LED pin modes to output
  for(int i = low; i <=high; i++){
    pinMode(i, OUTPUT);
  }  
  
  Serial.begin(57600);
  Serial.println("a101_ch8_time_display rev 1.0");  
  //set function to call when sync required
  setSyncProvider( requestSync);  
  Serial.println("Waiting for sync message");
}

void loop(){    
  if(Serial.available() ) 
  {
    processSyncMessage();
  }
  if(timeStatus()!= timeNotSet)   
  {
    // on if synced, off if needs refresh  
    digitalWrite(13,timeStatus() == timeSet); 
    sevenSegDisplay();  
  }
  delay(1000);
}

//********************************************//
// Functions to display the time on the 7-seg LED
//********************************************//
void sevenSegDisplay(){ 
  // show the hour
  setLEDs('H');
  delay(PAUSE);
  showDigits(hour());
  delay(PAUSE*2);
  
  // show the minute
  setLEDs('M');
  delay(PAUSE);
  showDigits(minute());
  delay(PAUSE);     
 
   // show the second
  setLEDs('S');
  delay(PAUSE);
  showDigits(second());
  delay(PAUSE);  
}

// Display all time values as two digits
// use leading 0 if value less than 10
void showDigits(int digits){
  int high, low;
  
  if(digits > 59) high = 6;
  else if(digits > 49) high = 5; 
  else if(digits > 39) high = 4; 
  else if(digits > 29) high = 3; 
  else if(digits > 19) high = 2; 
  else if(digits > 9) high = 1; 
  else high = 0;
  
  low = digits - (high*10);
  
  setLEDs(high+48);
  delay(PAUSE);
  setLEDs(low+48);
  delay(PAUSE);
}

void setLEDs(int input){
  // verify input is integer or character
  if( (input > 47) & (input < 58) )
  { 
    input = integer[input - 48];
  } 
  else if ( (input > 64) & (input < 91) ) 
  { 
   input = character[input - 65]; 
  } 
  else // it isn't an integer or character
  {
   return; // do nothing if invalid input 
  }
  
  // Turn the segments on or off
  digitalWrite(DPPIN,!(DPMASK&input));
  digitalWrite(GPIN,!(GMASK&input));
  digitalWrite(FPIN,!(FMASK&input));
  digitalWrite(EPIN,!(EMASK&input));
  digitalWrite(DPIN,!(DMASK&input));
  digitalWrite(CPIN,!(CMASK&input));
  digitalWrite(BPIN,!(BMASK&input));
  digitalWrite(APIN,!(AMASK&input));
}

//********************************************//
// Functions to synchronize the time
//********************************************//
void processSyncMessage() {
  // if time sync available from serial port, update time
  // time message consists of a header and ten ascii digits
  while(Serial.available() >=  TIME_MSG_LEN ){  
    char c = Serial.read() ; 
    Serial.print(c);  
    if( c == TIME_HEADER ) {       
      time_t pctime = 0;
      for(int i=0; i < TIME_MSG_LEN -1; i++){   
        c = Serial.read();          
        if( c >= '0' && c <= '9'){
          // convert digits to a number
          pctime = (10 * pctime) + (c - '0') ;     
        }
      } 
      // Sync Arduino clock to time from the serial port
      setTime(pctime);   
    }  
  }
}

time_t requestSync()
{
  Serial.print(TIME_REQUEST); 
  // the time will be sent later in response to serial mesg
  return 0; 
}



