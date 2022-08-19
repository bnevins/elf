
//Morse "bug" keyer

//buttons
#define button_1 2
#define button_2 3

//speed selector pins
//take one to ground to select speed
//default is 5 words per minute
#define selector_1 A0
#define selector_2 A1
#define selector_3 A2

//the buzzer
const int sounder_pin = 5; // the pin we attach our sounder to
const int duty_cycle = 128; //50% duty cycle for analogWrite()


//required to debounce switches
#define MAX_CHECKS 10
volatile uint8_t Debounced_State = 0; //accessed by isr and main loop code
uint8_t State[MAX_CHECKS] = {0};
uint8_t Index = 0;


void setup() {
  // put your setup code here, to run once:

  pinMode(button_1, INPUT_PULLUP);
  pinMode(button_2, INPUT_PULLUP);

  pinMode(selector_1, INPUT_PULLUP);
  pinMode(selector_2, INPUT_PULLUP);
  pinMode(selector_3, INPUT_PULLUP);

  pinMode(sounder_pin, OUTPUT);

  // initialize timer1
  noInterrupts();           // disable all interrupts
  TCCR1A = 0;
  TCCR1B = 0;
  TCNT1  = 0;

  OCR1A = 625;              // compare match register  16MHX/256/100HZ
  TCCR1B |= (1 << WGM12);   // CTC mode
  TCCR1B |= (1 << CS12);    // 256 prescaler
  TIMSK1 |= (1 << OCIE1A);  // enable timer compare interrupt
  interrupts();             // enable all interrupts

}

void loop() {
  // put your main code here, to run repeatedly:

  static int transferred_value = 0;
  static bool do_character = false;
  static bool do_dot = false;
  static bool wait_up = false;

  noInterrupts();           // disable all interrupts
  transferred_value = Debounced_State;
  interrupts();             // enable all interrupts


  if (!transferred_value) {
    wait_up = false; //if all buttons released
  }
  if (transferred_value) {

    if (transferred_value == 2) {
      do_dot = true;  //dot
      do_character = true;
      wait_up = true;
    }
    if (transferred_value == 1) {
      if (!wait_up) {
        do_dot = false;  //dash
        do_character = true;
        wait_up = true;
      }
    }

  }


  //morse speed
  //all timing is based around the length of the dot
  //source for timing -> http://www.kent-engineers.com/codespeed.htm

  static unsigned long unit_length = 240; //default 5 words per minute
  if (!digitalRead(A0)) {
    unit_length = 240; //dot time calculated for 5 words per minute
  }
  if (!digitalRead(A1)) {
    unit_length = 92; //dot time calculated for 13 words per minute
  }
  if (!digitalRead(A2)) {
    unit_length = 60; //dot time calculated for 20 words per minute
  }


  const unsigned long dot = unit_length;
  const unsigned long dash = unit_length * 3;
  const unsigned long intra_letter_space = unit_length; //space between dot's and dashes within letter


  if (do_character)
  {
    do_character = false;


    if (!do_dot) { //dash
      analogWrite(sounder_pin, duty_cycle);
      delay(dash);                       // wait for a while
      analogWrite(sounder_pin, 0);
      delay(intra_letter_space);         // space between letter parts
    }
    else { //dot
      analogWrite(sounder_pin, duty_cycle);
      delay(dot);                    // wait for a while
      analogWrite(sounder_pin, 0);
      delay(intra_letter_space);     // space between letter parts
    }

  }


}
/*-----------------*/
//my_functions
ISR(TIMER1_COMPA_vect)          // timer compare interrupt service routine
{
  //code source - A Guide to Debouncing by Jack G. Ganssle
  //read buttons
  uint8_t temp = 0x00;

  temp |= !digitalRead(button_1) << 1;
  temp |= !digitalRead(button_2) << 0;

  //debounce
  uint8_t i, j;

  State[Index] = temp;
  ++Index;
  j = 0xFF;
  for (i = 0; i < MAX_CHECKS; i++) {
    j = j & State[i];
  }
  Debounced_State = j;
  if (Index >= MAX_CHECKS) {
    Index = 0;
  }

}
//-----------end------------
