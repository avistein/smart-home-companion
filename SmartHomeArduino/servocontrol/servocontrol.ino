#include<Servo.h>
#define RING_SERVO_PIN 9
#define LIGHT1_SERVO_PIN 6 
//#define FAN1_SERVO_PIN 10
//#define FAN2_SERVO_PIN 11

Servo ringServo;
Servo light1Servo;
//Servo fan1Servo;
//Servo fan2Servo;

int c = -1;
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  light1Servo.attach(LIGHT1_SERVO_PIN);
//  fan1Servo.attach(FAN1_SERVO_PIN);
//  fan2Servo.attach(FAN2_SERVO_PIN);
  light1Servo.write(138);
  delay(100);
//  fan1Servo.attach(135);
//  delay(100);
//  ringServo.write(65);
//  delay(100);
}

void loop() {
  // put your main code here, to run repeatedly:
  c = Serial.read();
  if (c == 1) {
    while (c == 1) {
      ringServo.attach(RING_SERVO_PIN);
      Serial.println(c);
      if (Serial.read() == 0) {
        ringServo.write(65);
        delay(200);
        ringServo.detach();
        break;
      }
      ringServo.write(55);
      delay(750);
      ringServo.write(100);
      delay(750);
    }
  }
  else if(c == 011){
//    light1Servo.attach(LIGHT1_SERVO_PIN);
    light1Servo.write(110);
    delay(200);
    light1Servo.write(138);
    delay(200);
//    light1Servo.detach();
  }
  else if(c == 010){
//    light1Servo.attach(LIGHT1_SERVO_PIN);
    light1Servo.write(180);
    delay(200);
    light1Servo.write(138);
    delay(200);
//    light1Servo.detach();
  }
  else if(c == 111){
//    fan1Servo.write(180);
//    delay(50);
  }
  else if(c == 110){
//    fan1Servo.write(120);
//    delay(50);
  }
  else if(c == 121){
    
  }
  else{
    
  }
}
