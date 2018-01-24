#define LedPin 13
char state = '0';
void setup(){
  pinMode(LedPin, OUTPUT);
  digitalWrite(LedPin, LOW);
  Serial.begin(9600); //defult communication rate ot BT model
}

void loop() {
  if(Serial.available()>0){  //check data
    state = Serial.read();   //and read it
  }
if(state == '0'){
  digitalWrite(LedPin,LOW);  //led off
  Serial.println("LED: OFF");  //send to phone "LED: OFF"
}
else if (state == '1'){
  digitalWrite(LedPin,HIGH);  // led on
  Serial.println("LED: ON");  //send "LED: ON"
}
}
