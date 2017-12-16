
#define SEG_A 8
#define SEG_B 9
#define SEG_C 2
#define SEG_D 3
#define SEG_E 4
#define SEG_F 5
#define SEG_G 6

#define DELAY 500

// each row has output needed for A, B,..., G

int segmap[10][7] = { 
  { LOW, LOW, LOW, LOW, LOW, LOW, HIGH },
  { HIGH, HIGH, HIGH, HIGH, LOW, LOW, HIGH } ,
  { LOW, LOW, HIGH, LOW, LOW, HIGH, LOW }, //2
  { LOW, LOW, LOW, LOW, HIGH, HIGH, LOW }, 
  { HIGH, LOW, LOW, HIGH, HIGH, LOW, LOW },
  { LOW, HIGH, LOW, LOW, HIGH, LOW, LOW }, // 5
  { HIGH, HIGH, LOW, LOW, LOW, LOW, LOW },
  { LOW, LOW, LOW, HIGH, HIGH, HIGH, HIGH },
  { LOW, LOW, LOW, LOW, LOW, LOW, LOW },
  { LOW, LOW, LOW, HIGH, HIGH, LOW, LOW }
};

int segPin[7] = { 8, 9, 2, 3, 4, 5, 6 }; // e.g "B" segment from pin #9

// could make this a little more efficient/compact by putting the pin numbers in an array...
void light(int num) {
  Serial.print("Lighting up: ");
  Serial.println(num);
  
  digitalWrite(SEG_A, segmap[num][0]);
  digitalWrite(SEG_B, segmap[num][1]);
  digitalWrite(SEG_C, segmap[num][2]);
  digitalWrite(SEG_D, segmap[num][3]);
  digitalWrite(SEG_E, segmap[num][4]);
  digitalWrite(SEG_F, segmap[num][5]);
  digitalWrite(SEG_G, segmap[num][6]);
}


void LoopDisplay()
{
  //Loop through all Chars and Numbers
  int i = -1;
  
  while(++i < 10) {
    delay(DELAY);
    light(i);
  }
 }

void setup()
{
  //Setup our pins
  pinMode(SEG_A, OUTPUT);
  pinMode(SEG_B, OUTPUT);
  pinMode(SEG_C, OUTPUT);
  pinMode(SEG_D, OUTPUT);
  pinMode(SEG_E, OUTPUT);
  pinMode(SEG_F, OUTPUT);
  pinMode(SEG_G, OUTPUT);
  Serial.begin(9600);  //Begin serial communcation

}

void loop()
{
  LoopDisplay();
}













