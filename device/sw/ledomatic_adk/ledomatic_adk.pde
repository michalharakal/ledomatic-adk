#include <Wire.h>
#include <Servo.h>

#include <Max3421e.h>
#include <Usb.h>
#include <AndroidAccessory.h>

#include <CapSense.h>

#define  LED3_RED       2
#define  LED3_GREEN     4
#define  LED3_BLUE      3

#define  LED2_RED       5
#define  LED2_GREEN     7
#define  LED2_BLUE      6

#define  LED1_RED       8
#define  LED1_GREEN     10
#define  LED1_BLUE      9

#define  SERVO1         11
#define  SERVO2         12
#define  SERVO3         13

#define  TOUCH_RECV     14
#define  TOUCH_SEND     15

#define  RELAY1         A0
#define  RELAY2         A1

#define  LIGHT_SENSOR   A2
#define  TEMP_SENSOR    A3

#define  BUTTON1        A6
#define  BUTTON2        A7
#define  BUTTON3        A8

#define  JOY_SWITCH     A9      // pulls line down when pressed
#define  JOY_nINT       A10     // active low interrupt input
#define  JOY_nRESET     A11     // active low reset output

AndroidAccessory acc("Google, Inc.",
		     "DemoKit",
		     "DemoKit Arduino Board",
		     "1.0",
		     "http://www.android.com",
		     "0000000012345678");
Servo servos[3];

// 10M ohm resistor on demo shield
CapSense   touch_robot = CapSense(TOUCH_SEND, TOUCH_RECV);

void setup();
void loop();

void init_leds()
{
	digitalWrite(LED1_RED, 1);
	digitalWrite(LED1_GREEN, 1);
	digitalWrite(LED1_BLUE, 1);

	pinMode(LED1_RED, OUTPUT);
	pinMode(LED1_GREEN, OUTPUT);
	pinMode(LED1_BLUE, OUTPUT);

	digitalWrite(LED2_RED, 1);
	digitalWrite(LED2_GREEN, 1);
	digitalWrite(LED2_BLUE, 1);

	pinMode(LED2_RED, OUTPUT);
	pinMode(LED2_GREEN, OUTPUT);
	pinMode(LED2_BLUE, OUTPUT);

	digitalWrite(LED3_RED, 1);
	digitalWrite(LED3_GREEN, 1);
	digitalWrite(LED3_BLUE, 1);

	pinMode(LED3_RED, OUTPUT);
	pinMode(LED3_GREEN, OUTPUT);
	pinMode(LED3_BLUE, OUTPUT);
}



byte b1, b2, b3, b4, c;
void setup()
{
	Serial.begin(115200);
	Serial.print("\r\nStart");

	init_leds();
	acc.powerOn();
}

void loop()
{
	byte err;
	byte idle;
	static byte count = 0;
	byte msg[3];
	long touchcount;

	if (acc.isConnected()) {
		int len = acc.read(msg, sizeof(msg), 1);
		int i;
		byte b;
		uint16_t val;
		int x, y;
		char c0;

		if (len > 0) {
			// assumes only one command per packet
			if (msg[0] == 0x2) {
				if (msg[1] == 0x0)
					analogWrite(LED1_RED, 255 - msg[2]);
				else if (msg[1] == 0x1)
					analogWrite(LED1_GREEN, 255 - msg[2]);
				else if (msg[1] == 0x2)
					analogWrite(LED1_BLUE, 255 - msg[2]);
				else if (msg[1] == 0x3)
					analogWrite(LED2_RED, 255 - msg[2]);
				else if (msg[1] == 0x4)
					analogWrite(LED2_GREEN, 255 - msg[2]);
				else if (msg[1] == 0x5)
					analogWrite(LED2_BLUE, 255 - msg[2]);
				else if (msg[1] == 0x6)
					analogWrite(LED3_RED, 255 - msg[2]);
				else if (msg[1] == 0x7)
					analogWrite(LED3_GREEN, 255 - msg[2]);
				else if (msg[1] == 0x8)
					analogWrite(LED3_BLUE, 255 - msg[2]);
				else if (msg[1] == 0x10)
					servos[0].write(map(msg[2], 0, 255, 0, 180));
				else if (msg[1] == 0x11)
					servos[1].write(map(msg[2], 0, 255, 0, 180));
				else if (msg[1] == 0x12)
					servos[2].write(map(msg[2], 0, 255, 0, 180));
			} else if (msg[0] == 0x3) {
				if (msg[1] == 0x0)
					digitalWrite(RELAY1, msg[2] ? HIGH : LOW);
				else if (msg[1] == 0x1)
					digitalWrite(RELAY2, msg[2] ? HIGH : LOW);
			}
		}

		msg[0] = 0x1;

		
	} else {
		// reset outputs to default values on disconnect
		analogWrite(LED1_RED, 240);
		analogWrite(LED1_GREEN, 255);
		analogWrite(LED1_BLUE, 255);
		analogWrite(LED2_RED, 255);
		analogWrite(LED2_GREEN, 240);
		analogWrite(LED2_BLUE, 255);
		analogWrite(LED3_RED, 255);
		analogWrite(LED3_GREEN, 255);
		analogWrite(LED3_BLUE, 240);
		
	}

	delay(10);
}


