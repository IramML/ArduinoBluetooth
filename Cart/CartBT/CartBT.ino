#define leftA 5 //forward            
#define leftB 6 //reverse              
#define rightA 9 //forward              
#define rightB 10 //reverse                                  
#define buzzer 3             
#define leds 4

char statusBT = "";
 
void setup() {
  Serial.begin(9600); //speed for serial communication to 9600 baud
  
  pinMode(rightA, OUTPUT);
  pinMode(rightB, OUTPUT);
  pinMode(leftA, OUTPUT);
  pinMode(leftB, OUTPUT);
  pinMode(buzzer, OUTPUT);
  pinMode(leds, OUTPUT);
}

void loop() {
  commands();
}
void commands(){
  if(Serial.available()>0){ 
    statusBT = Serial.read();
  }
  switch(statusBT){
    //generate sound
    case 'p':             
      digitalWrite(buzzer,HIGH);
      break;
    //turn off sound
    case 'z':
      digitalWrite(buzzer,LOW);
      break;
    //turn on leds
    case 'q':   
      digitalWrite(leds,HIGH);
      break;
    //turn off leds
    case 'w':
      digitalWrite(leds,LOW);
      break;
    //Forward
    case 'a':                 
      forwardRight();
      forwardLeft();
      break;
    //turn to the right
    case 'b':                
      forwardRight();
      stopLeft();
      break;
    //stop
    case 'c':                 
      stopRight();
      stopLeft();
      break;
    //turn to the left
    case 'd':                
      stopRight();
      forwardLeft();
      break;
    //reverse
    case 'e':                
      reverseRight();
      reverseLeft();
      break;
  }
}
void forwardRight(){
  digitalWrite(rightA, HIGH);
  digitalWrite(rightB, LOW);
}
void forwardLeft(){
  digitalWrite(leftA, HIGH);
  digitalWrite(leftB, LOW);
}
void reverseRight(){
  digitalWrite(rightA, LOW);
  digitalWrite(rightB, HIGH);
}
void reverseLeft(){
  digitalWrite(leftA, LOW);
  digitalWrite(leftB, HIGH);
}
void stopRight(){
  digitalWrite(rightA, LOW);
  digitalWrite(rightB, LOW);
}
void stopLeft(){
  digitalWrite(leftA, LOW);
  digitalWrite(leftB, LOW);
}


