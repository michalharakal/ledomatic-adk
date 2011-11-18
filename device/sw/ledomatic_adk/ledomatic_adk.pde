#include <Wire.h>
#include <Servo.h>

#include <Max3421e.h>
#include <Usb.h>
#include <AndroidAccessory.h>

#include "led.h"

#define  LED3_RED       2
#define  LED3_GREEN     4
#define  LED3_BLUE      3

#define  LED2_RED       5
#define  LED2_GREEN     7
#define  LED2_BLUE      6

#define  LED1_RED       8
#define  LED1_GREEN     10
#define  LED1_BLUE      9

int rpin = LED1_RED;
int gpin = LED1_GREEN;
int bpin = LED1_BLUE;

// RGB led instances
Led ch1(LED1_RED, LED1_GREEN, LED1_BLUE, 0);
Led ch2(LED2_RED, LED2_GREEN, LED2_BLUE, 1);
Led ch3(LED3_RED, LED3_GREEN, LED3_BLUE, 2);


AndroidAccessory acc("Fiwio.com",
     "ledomatic",
     "Ledomatic ADK",
     "1.0",
     "http://www.ledomatic.com",
     "0000000012345678");

void setup()
{
  Serial.begin(115200);
  Serial.print("\r\nStart");

  acc.powerOn();
}

void loop()
{
  byte err;
  byte idle;
  static byte count = 0;
  byte msg[9];

  if (acc.isConnected()) {
    int len = acc.read(msg, sizeof(msg), 1);
    if (len == 9) {
      // assumes only one command per packet
      ch1.rgb_set(msg[0], msg[1], msg[2]);
      ch2.rgb_set(msg[3], msg[4], msg[5]);
      ch3.rgb_set(msg[6], msg[7], msg[8]);
    }
  } 
  else {
    // reset outputs to default values on disconnect
    ch1.change_color();
    ch2.change_color();
    ch3.change_color();
  }
  delay(10);
}



