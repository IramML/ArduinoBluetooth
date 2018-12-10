#include "pitches.h"

#define buzzerPin 13
#define ledPin1 12
#define ledPin2 11
#define ledPin3 10
#define ledPin4 9
#define ledPin5 8
#define ledPin6 7



char statusBT = ' ';
 
void setup(void){
  Serial.begin(9600); //speed for serial communication to 9600 baud
  pinMode(buzzerPin, OUTPUT);
  pinMode(ledPin1, OUTPUT);
  pinMode(ledPin2, OUTPUT);
  pinMode(ledPin3, OUTPUT);
  pinMode(ledPin4, OUTPUT);
  pinMode(ledPin5, OUTPUT);
  pinMode(ledPin6, OUTPUT);
}
void loop(){
  refreshBT();
  switch(statusBT){
    //play Mario Bros Theme
    case 'a':             
      //source https://gist.github.com/micdenny/9b2ad8a6fac1f4a7d679087194761ce0  
      playMarioBrosTheme();
      break;
    //play Star Wars Theme
    case 'b':                
      // source https://gist.github.com/nicksort/4736535
      playStarWarsTheme();
      break;
    //play Star Wars Theme
    case 'c': 
      //source https://github.com/AraanBranco/arduino/blob/master/music/Game-of-thrones-song.txt                
      GameOfThrones();
      break;
    //play Zelda Theme
    case 'd':   
      playZeldaTheme();
      break;
    //play Doctor Who Theme
    case 'e':      
      //source https://codebender.cc/sketch:109527#Doctor%20Who%20Theme%20Song.ino           
      playDoctorWhoTheme();
      break;
    default :
      break;
  }
}
void refreshBT(){
  if(Serial.available()>0){ 
    statusBT = Serial.read();
  }
}
int song = 0;

//begin mario theme

//Mario main theme melody
int melody[] = {
  NOTE_E7, NOTE_E7, 0, NOTE_E7,
  0, NOTE_C7, NOTE_E7, 0,
  NOTE_G7, 0, 0,  0,
  NOTE_G6, 0, 0, 0,
 
  NOTE_C7, 0, 0, NOTE_G6,
  0, 0, NOTE_E6, 0,
  0, NOTE_A6, 0, NOTE_B6,
  0, NOTE_AS6, NOTE_A6, 0,
 
  NOTE_G6, NOTE_E7, NOTE_G7,
  NOTE_A7, 0, NOTE_F7, NOTE_G7,
  0, NOTE_E7, 0, NOTE_C7,
  NOTE_D7, NOTE_B6, 0, 0,
 
  NOTE_C7, 0, 0, NOTE_G6,
  0, 0, NOTE_E6, 0,
  0, NOTE_A6, 0, NOTE_B6,
  0, NOTE_AS6, NOTE_A6, 0,
 
  NOTE_G6, NOTE_E7, NOTE_G7,
  NOTE_A7, 0, NOTE_F7, NOTE_G7,
  0, NOTE_E7, 0, NOTE_C7,
  NOTE_D7, NOTE_B6, 0, 0
};
//Mario main them tempo
int tempo[] = {
  12, 12, 12, 12,
  12, 12, 12, 12,
  12, 12, 12, 12,
  12, 12, 12, 12,
 
  12, 12, 12, 12,
  12, 12, 12, 12,
  12, 12, 12, 12,
  12, 12, 12, 12,
 
  9, 9, 9,
  12, 12, 12, 12,
  12, 12, 12, 12,
  12, 12, 12, 12,
 
  12, 12, 12, 12,
  12, 12, 12, 12,
  12, 12, 12, 12,
  12, 12, 12, 12,
 
  9, 9, 9,
  12, 12, 12, 12,
  12, 12, 12, 12,
  12, 12, 12, 12,
};

//Underworld melody
int underworld_melody[] = {
  NOTE_C4, NOTE_C5, NOTE_A3, NOTE_A4,
  NOTE_AS3, NOTE_AS4, 0,
  0,
  NOTE_C4, NOTE_C5, NOTE_A3, NOTE_A4,
  NOTE_AS3, NOTE_AS4, 0,
  0,
  NOTE_F3, NOTE_F4, NOTE_D3, NOTE_D4,
  NOTE_DS3, NOTE_DS4, 0,
  0,
  NOTE_F3, NOTE_F4, NOTE_D3, NOTE_D4,
  NOTE_DS3, NOTE_DS4, 0,
  0, NOTE_DS4, NOTE_CS4, NOTE_D4,
  NOTE_CS4, NOTE_DS4,
  NOTE_DS4, NOTE_GS3,
  NOTE_G3, NOTE_CS4,
  NOTE_C4, NOTE_FS4, NOTE_F4, NOTE_E3, NOTE_AS4, NOTE_A4,
  NOTE_GS4, NOTE_DS4, NOTE_B3,
  NOTE_AS3, NOTE_A3, NOTE_GS3,
  0, 0, 0
};

//Underwolrd tempo
int underworld_tempo[] = {
  12, 12, 12, 12,
  12, 12, 6,
  3,
  12, 12, 12, 12,
  12, 12, 6,
  3,
  12, 12, 12, 12,
  12, 12, 6,
  3,
  12, 12, 12, 12,
  12, 12, 6,
  6, 18, 18, 18,
  6, 6,
  6, 6,
  6, 6,
  18, 18, 18, 18, 18, 18,
  10, 10, 10,
  10, 10, 10,
  3, 3, 3
};
void playMarioBrosTheme(){
  //sing the tunes
  sing(1);
  sing(1);
  sing(2);
}
void sing(int s) {
  // iterate over the notes of the melody:
  song = s;
  if (song == 2) {
    Serial.println(" 'Underworld Theme'");
    int size = sizeof(underworld_melody) / sizeof(int);
    for (int thisNote = 0; thisNote < size; thisNote++) {
      refreshBT();
      if(statusBT!='a')return;
      // to calculate the note duration, take one second
      // divided by the note type.
      //e.g. quarter note = 1000 / 4, eighth note = 1000/8, etc.
      int noteDuration = 1000 / underworld_tempo[thisNote];
 
      buzz(buzzerPin, underworld_melody[thisNote], noteDuration);
 
      // to distinguish the notes, set a minimum time between them.
      // the note's duration + 30% seems to work well:
      int pauseBetweenNotes = noteDuration * 1.30;
      delay(pauseBetweenNotes);
 
      // stop the tone playing:
      buzz(buzzerPin, 0, noteDuration);
 
    }
 
  } else {
 
    Serial.println(" 'Mario Theme'");
    int size = sizeof(melody) / sizeof(int);
    for (int thisNote = 0; thisNote < size; thisNote++) {
      refreshBT();
      if(statusBT!='a')return;
      // to calculate the note duration, take one second
      // divided by the note type.
      //e.g. quarter note = 1000 / 4, eighth note = 1000/8, etc.
      int noteDuration = 1000 / tempo[thisNote];
 
      buzz(buzzerPin, melody[thisNote], noteDuration);
 
      // to distinguish the notes, set a minimum time between them.
      // the note's duration + 30% seems to work well:
      int pauseBetweenNotes = noteDuration * 1.30;
      delay(pauseBetweenNotes);
 
      // stop the tone playing:
      buzz(buzzerPin, 0, noteDuration);
 
    }
  }
}
 
void buzz(int targetPin, long frequency, long length) {
  digitalWrite(13, HIGH);
  long delayValue = 1000000 / frequency / 2; // calculate the delay value between transitions
  //// 1 second's worth of microseconds, divided by the frequency, then split in half since
  //// there are two phases to each cycle
  long numCycles = frequency * length / 1000; // calculate the number of cycles for proper timing
  //// multiply frequency, which is really cycles per second, by the number of seconds to
  //// get the total number of cycles to produce
  for (long i = 0; i < numCycles; i++) { // for the calculated length of time...
    digitalWrite(targetPin, HIGH); // write the buzzer pin high to push out the diaphram
    delayMicroseconds(delayValue); // wait for the calculated delay value
    digitalWrite(targetPin, LOW); // write the buzzer pin low to pull back the diaphram
    delayMicroseconds(delayValue); // wait again or the calculated delay value
  }
  digitalWrite(13, LOW);
 
}
//end mario theme

//begin star wars theme
const int c = 261;
const int d = 294;
const int e = 329;
const int f = 349;
const int g = 391;
const int gS = 415;
const int a = 440;
const int aS = 455;
const int b = 466;
const int cH = 523;
const int cSH = 554;
const int dH = 587;
const int dSH = 622;
const int eH = 659;
const int fH = 698;
const int fSH = 740;
const int gH = 784;
const int gSH = 830;
const int aH = 880;
 
int counter = 0;

void playStarWarsTheme(){
  //Play first section
  firstSection();
 
  //Play second section
  secondSection();
 
  //Variant 1
  beep(f, 250);  
  beep(gS, 500);  
  beep(f, 350);  
  beep(a, 125);
  beep(cH, 500);
  beep(a, 375);  
  beep(cH, 125);
  beep(eH, 650);
 
  delay(500);
 
  //Repeat second section
  secondSection();
 
  //Variant 2
  beep(f, 250);  
  beep(gS, 500);  
  beep(f, 375);  
  beep(cH, 125);
  beep(a, 500);  
  beep(f, 375);  
  beep(cH, 125);
  beep(a, 650);  
 
  delay(650);
}

void beep(int note, int duration){
  refreshBT();
  if(statusBT!='b')return;
  //Play tone on buzzerPin
  tone(buzzerPin, note, duration);
 
  //Play different LED depending on value of 'counter'
  if(counter % 2 == 0){
    digitalWrite(ledPin1, HIGH);
    delay(duration);
    digitalWrite(ledPin1, LOW);
  }else
  {
    digitalWrite(ledPin2, HIGH);
    delay(duration);
    digitalWrite(ledPin2, LOW);
  }
 
  //Stop tone on buzzerPin
  noTone(buzzerPin);
 
  delay(50);
 
  //Increment counter
  counter++;
}
 
void firstSection(){
  beep(a, 500);
  beep(a, 500);    
  beep(a, 500);
  beep(f, 350);
  beep(cH, 150);  
  beep(a, 500);
  beep(f, 350);
  beep(cH, 150);
  beep(a, 650);
 
  delay(500);
 
  beep(eH, 500);
  beep(eH, 500);
  beep(eH, 500);  
  beep(fH, 350);
  beep(cH, 150);
  beep(gS, 500);
  beep(f, 350);
  beep(cH, 150);
  beep(a, 650);
 
  delay(500);
}
 
void secondSection()
{
  beep(aH, 500);
  beep(a, 300);
  beep(a, 150);
  beep(aH, 500);
  beep(gSH, 325);
  beep(gH, 175);
  beep(fSH, 125);
  beep(fH, 125);    
  beep(fSH, 250);
 
  delay(325);
 
  beep(aS, 250);
  beep(dSH, 500);
  beep(dH, 325);  
  beep(cSH, 175);  
  beep(cH, 125);  
  beep(b, 125);  
  beep(cH, 250);  
 
  delay(350);
}
//end star wars theme

//begin Game Of Thrones Theme 
void ilumina(int note){
  if(note == NOTE_G4 || note == NOTE_G3 || note == NOTE_GS3) {
   digitalWrite(ledPin1, HIGH);
  } else if (note == NOTE_C4) {
    digitalWrite(ledPin2, HIGH);
  } else if(note == NOTE_AS3) {
    digitalWrite(ledPin3, HIGH);
  } else if (note == NOTE_DS4) {
    digitalWrite(ledPin4, HIGH);
  }else if (note == NOTE_F3) {
    digitalWrite(ledPin5, HIGH);
  } else if(note == NOTE_E4) {
    digitalWrite(ledPin6, HIGH);
  }
}

void apaga(int note){
  if(note == NOTE_G4 || note == NOTE_G3 || note == NOTE_GS3) {
   digitalWrite(ledPin1, LOW);
  } else if (note == NOTE_C4) {
    digitalWrite(ledPin2, LOW);
  } else if(note == NOTE_AS3) {
    digitalWrite(ledPin3, LOW);
  } else if (note == NOTE_DS4) {
    digitalWrite(ledPin4, LOW);
  }  else if (note == NOTE_F3) {
    digitalWrite(ledPin5, LOW);
  } else if(note == NOTE_E4) {
    digitalWrite(ledPin6, LOW);
  }
}

void GameOfThrones() {
  for(int i=0; i<4; i++) {
    refreshBT();
    if(statusBT!='c')return;
    tone(buzzerPin, NOTE_G4);
    ilumina(NOTE_G4);
    delay(500);
    noTone(buzzerPin);
    apaga(NOTE_G4);

    tone(buzzerPin, NOTE_C4);
    ilumina(NOTE_C4);
    delay(500);
    noTone(buzzerPin);
    apaga(NOTE_C4);

    tone(buzzerPin, NOTE_DS4);
    ilumina(NOTE_DS4);
    delay(250);
    noTone(buzzerPin);
    apaga(NOTE_DS4);

    tone(buzzerPin, NOTE_F4);
    ilumina(NOTE_F4);
    delay(250);
    noTone(buzzerPin);
    apaga(NOTE_F4);
  }

  for(int i=0; i<4; i++){
    refreshBT();
    if(statusBT!='c')return;
    tone(buzzerPin, NOTE_G4);
    ilumina(NOTE_G4);
    delay(500);
    noTone(buzzerPin);
    apaga(NOTE_G4);

    tone(buzzerPin, NOTE_C4);
    ilumina(NOTE_C4);
    delay(500);
    noTone(buzzerPin);
    apaga(NOTE_C4);

    tone(buzzerPin, NOTE_E4);
    ilumina(NOTE_E4);
    delay(250);
    noTone(buzzerPin);
    apaga(NOTE_E4);

    tone(buzzerPin, NOTE_F4);
    ilumina(NOTE_F4);
    delay(250);
    noTone(buzzerPin);
    apaga(NOTE_F4);
  }

    tone(buzzerPin, NOTE_G4);
    ilumina(NOTE_G4);
    delay(1500);
    noTone(buzzerPin);
    apaga(NOTE_G4);

    tone(buzzerPin, NOTE_C4);
    ilumina(NOTE_C4);
    delay(1500);
    apaga(NOTE_C4);

//
    tone(buzzerPin, NOTE_E4);
    ilumina(NOTE_E4);
    delay(250);
    apaga(NOTE_C4);

    tone(buzzerPin, NOTE_F4);
    ilumina(NOTE_F4);
    delay(250);
    apaga(NOTE_C4);
//
    tone(buzzerPin, NOTE_G4);
    ilumina(NOTE_G4);
    delay(1000);
    noTone(buzzerPin);
    apaga(NOTE_G4);

    tone(buzzerPin, NOTE_C4);
    ilumina(NOTE_C4);
    delay(1000);
    apaga(NOTE_C4);

    tone(buzzerPin, NOTE_DS4);
    ilumina(NOTE_DS4);
    delay(250);
    noTone(buzzerPin);
    apaga(NOTE_DS4);

    tone(buzzerPin, NOTE_F4);
    ilumina(NOTE_F4);
    delay(250);
    noTone(buzzerPin);
    apaga(NOTE_F4);
    
  for(int i=0; i<3; i++) {
    refreshBT();
    if(statusBT!='c')return;
    tone(buzzerPin, NOTE_D4);
    ilumina(NOTE_D4);
    delay(500);
    noTone(buzzerPin);
    apaga(NOTE_D4);
    
    tone(buzzerPin, NOTE_G3);
    ilumina(NOTE_G3);
    delay(500);
    noTone(buzzerPin);
    apaga(NOTE_G3);

    tone(buzzerPin, NOTE_AS3);
    ilumina(NOTE_AS3);
    delay(250);
    noTone(buzzerPin);
    apaga(NOTE_AS3);

    tone(buzzerPin, NOTE_C4);
    ilumina(NOTE_C4);
    delay(250);
    noTone(buzzerPin);
    apaga(NOTE_C4);
    
    
  }
  


      tone(buzzerPin, NOTE_D4);
      ilumina(NOTE_D4);
      delay(1500);
      noTone(buzzerPin);
      apaga(NOTE_D4);
      
      tone(buzzerPin, NOTE_F4);
      ilumina(NOTE_F4);
      delay(1500);
      noTone(buzzerPin);
      apaga(NOTE_F4);

      tone(buzzerPin, NOTE_AS3);
      ilumina(NOTE_AS3);
      delay(1000);
      noTone(buzzerPin);
      apaga(NOTE_AS3);

      tone(buzzerPin, NOTE_DS4);
      ilumina(NOTE_DS4);
      delay(250);
      noTone(buzzerPin);
      apaga(NOTE_DS4);

      tone(buzzerPin, NOTE_D4);
      ilumina(NOTE_D4);
      delay(250);
      noTone(buzzerPin);
      apaga(NOTE_D4);

      tone(buzzerPin, NOTE_F4);
      ilumina(NOTE_F4);
      delay(1000);
      noTone(buzzerPin);
      apaga(NOTE_F4);

      tone(buzzerPin, NOTE_AS3);
      ilumina(NOTE_AS3);
      delay(1000);
      noTone(buzzerPin);
      apaga(NOTE_AS3);

      tone(buzzerPin, NOTE_DS4);
      ilumina(NOTE_DS4);
      delay(250);
      noTone(buzzerPin);
      apaga(NOTE_DS4);

      tone(buzzerPin, NOTE_D4);
      ilumina(NOTE_D4);
      delay(250);
      noTone(buzzerPin);
      apaga(NOTE_D4);

      tone(buzzerPin, NOTE_C4);
      ilumina(NOTE_C4);
      delay(500);
      noTone(buzzerPin);
      apaga(NOTE_C4);

  for(int i=0; i<3; i++) {
    refreshBT();
    if(statusBT!='c')return;
      tone(buzzerPin, NOTE_GS3);
      ilumina(NOTE_GS3);
      delay(250);
      noTone(buzzerPin);
      apaga(NOTE_GS3);

      tone(buzzerPin, NOTE_AS3);
      ilumina(NOTE_AS3);
      delay(250);
      noTone(buzzerPin);
      apaga(NOTE_AS3);

      tone(buzzerPin, NOTE_C4);
      ilumina(NOTE_C4);
      delay(500);
      noTone(buzzerPin);
      apaga(NOTE_C4);

      tone(buzzerPin, NOTE_F3);
      ilumina(NOTE_F3);
      delay(500);
      noTone(buzzerPin);
      apaga(NOTE_F3);
  }

    tone(buzzerPin, NOTE_G4);
    ilumina(NOTE_G4);
    delay(1000);
    noTone(buzzerPin);
    apaga(NOTE_G4);

    tone(buzzerPin, NOTE_C4);
    ilumina(NOTE_C4);
    delay(1000);
    noTone(buzzerPin);
    apaga(NOTE_C4);

    tone(buzzerPin, NOTE_DS4);
    ilumina(NOTE_DS4);
    delay(250);
    noTone(buzzerPin);
    apaga(NOTE_DS4);

    tone(buzzerPin, NOTE_F4);
    ilumina(NOTE_F4);
    delay(250);
    noTone(buzzerPin);
    apaga(NOTE_F4);

    tone(buzzerPin, NOTE_G4);
    ilumina(NOTE_G4);
    delay(1000);
    noTone(buzzerPin);
    apaga(NOTE_G4);

    tone(buzzerPin, NOTE_C4);
    ilumina(NOTE_C4);
    delay(1000);
    noTone(buzzerPin);
    apaga(NOTE_C4);

    tone(buzzerPin, NOTE_DS4);
    ilumina(NOTE_DS4);
    delay(250);
    noTone(buzzerPin);
    apaga(NOTE_DS4);

    tone(buzzerPin, NOTE_F4);
    ilumina(NOTE_F4);
    delay(250);
    noTone(buzzerPin);
    apaga(NOTE_F4);

    tone(buzzerPin, NOTE_D4);
    ilumina(NOTE_D4);
    delay(500);
    noTone(buzzerPin);
    apaga(NOTE_D4);

  for(int i=0; i<4; i++) {
    refreshBT();
    if(statusBT!='c')return;
    tone(buzzerPin, NOTE_G3);
    ilumina(NOTE_G3);
    delay(500);
    noTone(buzzerPin);
    apaga(NOTE_G3);

    tone(buzzerPin, NOTE_AS3);
    ilumina(NOTE_AS3);
    delay(250);
    noTone(buzzerPin);
    apaga(NOTE_AS3);

    tone(buzzerPin, NOTE_C4);
    ilumina(NOTE_C4);
    delay(250);
    noTone(buzzerPin);
    apaga(NOTE_C4);

    tone(buzzerPin, NOTE_D4);
    ilumina(NOTE_D4);
    delay(500);
    noTone(buzzerPin);
    apaga(NOTE_D4);
  }
}
// end Game Of Thrones Theme
//begin Zelda Theme
void playZeldaTheme(){
  tone(13,277,200);  delay(200);
  tone(13,349,200);  delay(200);
  tone(13,370,200);  delay(400);
  
  tone(13,277,200);  delay(200);
  tone(13,349,200);  delay(200);
  tone(13,370,200);  delay(400);
  refreshBT();
  if(statusBT!='d')return;
  tone(13,277,200);  delay(200);
  tone(13,349,200);  delay(200);
  tone(13,370,200);  delay(200);
  tone(13,523,200);  delay(200);
  tone(13,440,200);  delay(400);
  refreshBT();
  if(statusBT!='d')return;
  tone(13,370,200);  delay(200);
  tone(13,330,200);  delay(200);
  tone(13,370,200);  delay(200);
  tone(13,311,200);  delay(200);
  tone(13,261,200);  delay(600);
  refreshBT();
  if(statusBT!='d')return;
  tone(13,440,200);  delay(200);
  tone(13,261,200);  delay(200);
  tone(13,311,200);  delay(200);
  tone(13,261,200);  delay(800);  
int tones[] = {261, 277, 294, 311, 330, 349, 370, 392, 415, 440 , 493, 523};
//            mid C  C#   D    D#   E    F    F#   G    G#   A  ,  B ,  C    */
}
//end Zelda Theme
//begin Doctor Who Theme
// notes in the melody:
int melodyDoctorWho[] = {
  NOTE_E2, 0, NOTE_E2, NOTE_E2, 
  NOTE_E2, NOTE_E2, 0, NOTE_E2, 
  NOTE_E2, NOTE_E2, NOTE_E2, 0, 
  NOTE_E2, NOTE_E2, NOTE_E2, NOTE_C2, 
  0, NOTE_E2, 0, NOTE_E2, 
  0, NOTE_E2, NOTE_E2, NOTE_E2, 
  NOTE_E2, 0, NOTE_E2, NOTE_E2, 
  NOTE_E2, NOTE_E2, 0, NOTE_E2, 
  NOTE_E2, NOTE_E2, NOTE_C2, 0,
  NOTE_B3, NOTE_C5, NOTE_B4, 0,
  NOTE_D5, NOTE_A3, NOTE_B2, 0,
  NOTE_D5, NOTE_C5, NOTE_B4, 0,
  NOTE_D5, NOTE_C5, NOTE_B4, 0,
  NOTE_B3, NOTE_C5, NOTE_B4, 0  
};

// note durations: 4 = quarter note, 8 = eighth note, etc.:
int noteDurations[] = {
  4, 4, 8, 8, 
  8, 8, 4, 8, 
  8, 8, 8, 4, 
  8, 8, 8, 2, 
  4, 4, 4, 4, 
  8, 8, 8, 8, 
  4, 8, 8, 8, 
  8, 4, 8, 8, 
  8, 2, 4, 4, 
  4, 4, 1, 2,
  2, 4, 1, 1,
  4, 4, 2, 4,
  2, 4, 4, 2,
  4, 4, 1, 2,
};
void playDoctorWhoTheme(){
    // iterate over the notes of the melody:
  //integers arrays are two bytes so divide by 2
    for (int thisNote = 0; thisNote < sizeof(melodyDoctorWho)/2; thisNote++) {  
      refreshBT();
  if(statusBT!='e')return;
    // to calculate the note duration, take one second 
    // divided by the note type.
    //e.g. quarter note = 1000 / 4, eighth note = 1000/8, etc.
    int noteDuration = 1000/noteDurations[thisNote];
    tone(13, melodyDoctorWho[thisNote],noteDuration);

    // to distinguish the notes, set a minimum time between them.
    // the note's duration + 30% seems to work well:
    int pauseBetweenNotes = noteDuration * 1.30;
    delay(pauseBetweenNotes);
    // stop the tone playing:
    noTone(13);
  }
}
//end Doctor Who Theme

//begin Sweet child o mine
//Sweet Child O Mine - Guns N Roses-------------------------------------------------------------------------------------------------------------------------------
// Notes
int mainRiffD[] = {NOTE_D4 , NOTE_D5 , NOTE_A4, NOTE_G4, NOTE_G5, NOTE_A4, NOTE_FS5, NOTE_A4};
int mainRiffE[] = {NOTE_E4 , NOTE_D5 , NOTE_A4, NOTE_G4, NOTE_G5, NOTE_A4, NOTE_FS5, NOTE_A4};
int mainRiffG[] = {NOTE_G4 , NOTE_D5 , NOTE_A4, NOTE_G4, NOTE_G5, NOTE_A4, NOTE_FS5, NOTE_A4};

#define led7 6
#define led8 5

int mainRiffDurations[] = {
//d4  d5  a4   g4  g5  g4  fs5  a4  
  6,  6,  6,   6,  6,  6,  6 ,  6};

int mainRiffLeds[] = {ledPin1, ledPin2, ledPin3, ledPin4, ledPin5, ledPin6, led7, led8};

//----------------------------------------------------------------------------------------------------------------------------------------------


void playSweetChildOMine () {
  for(int introTwoTimes = 0; introTwoTimes < 2; introTwoTimes++){
    for(int dTwice = 0; dTwice < 2; dTwice++){
      for (int thisNote = 0; thisNote < 8; thisNote++){
        int mainRiffDuration = 1000/mainRiffDurations[thisNote];
        tone(buzzerPin, mainRiffD[thisNote], mainRiffDuration);
        digitalWrite(mainRiffLeds[thisNote], HIGH);
        int pauseBetweenNotes = mainRiffDuration * 1.30;
        delay(pauseBetweenNotes);
        noTone(buzzerPin);
        digitalWrite(mainRiffLeds[thisNote], LOW);
      }
    }
    
    for(int eTwice = 0; eTwice < 2; eTwice++){
      for (int thisNote = 0; thisNote < 8; thisNote++){
        int mainRiffDuration = 1000/mainRiffDurations[thisNote];
        tone(buzzerPin, mainRiffE[thisNote], mainRiffDuration);
        digitalWrite(mainRiffLeds[thisNote], HIGH);
        int pauseBetweenNotes = mainRiffDuration * 1.30;
        delay(pauseBetweenNotes);
        noTone(buzzerPin);
        digitalWrite(mainRiffLeds[thisNote], LOW);
      }
    }
    
    for(int gTwice = 0; gTwice < 2; gTwice++){
      for (int thisNote = 0; thisNote < 8; thisNote++){
        int mainRiffDuration = 1000/mainRiffDurations[thisNote];
        tone(buzzerPin, mainRiffG[thisNote], mainRiffDuration);
        digitalWrite(mainRiffLeds[thisNote], HIGH);
        int pauseBetweenNotes = mainRiffDuration * 1.30;
        delay(pauseBetweenNotes);
        noTone(buzzerPin);
        digitalWrite(mainRiffLeds[thisNote], LOW);
      }
    }
    
    for(int dTwice = 0; dTwice < 2; dTwice++){
      for (int thisNote = 0; thisNote < 8; thisNote++){
        int mainRiffDuration = 1000/mainRiffDurations[thisNote];
        tone(buzzerPin, mainRiffD[thisNote], mainRiffDuration);
        digitalWrite(mainRiffLeds[thisNote], HIGH);
        int pauseBetweenNotes = mainRiffDuration * 1.30;
        delay(pauseBetweenNotes);
        noTone(buzzerPin);
        digitalWrite(mainRiffLeds[thisNote], LOW);
      }
    }
  }
}
//end Sweet child o mine
