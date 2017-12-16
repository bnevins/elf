// How many hours until it starts after booting up?
unsigned long nextStartHours = 12;

// How long to run the heater?
unsigned long heaterTimeMinutes = 5;

// How often to run the heater?
unsigned long frequencyHours = 24;

//The Arduino output for the heater
const int HEATER_PIN = 7;

bool debug = true;

// No user-stuff below
unsigned long heaterEnd = 0L;
unsigned long nextStart = hoursToMillis(nextStartHours);
unsigned long heaterTime = minutesToMillis(heaterTimeMinutes);
unsigned long frequency = hoursToMillis(frequencyHours);

static unsigned long hoursToMillis(unsigned long hrs) { return hrs*60*60*1000; }
static unsigned long minutesToMillis(unsigned long hrs) { return hrs*60*1000; }

void setup() {
  if(debug) {
    nextStart=10000;
    heaterTime = 6000;
    frequency = 30000;
  }
  pinMode(HEATER_PIN, OUTPUT);
  digitalWrite(HEATER_PIN, HIGH);  
}

void loop() {
  unsigned long now = millis();

  if(now > nextStart) {
    nextStart += frequency;
    heaterEnd = now + heaterTime;
    digitalWrite(HEATER_PIN, LOW);  
  } 
  

  if(heaterEnd > 0 && now > heaterEnd) {
    digitalWrite(HEATER_PIN, HIGH);  
    heaterEnd = 0;
  }
}


