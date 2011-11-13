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

AndroidAccessory acc("Fiwio.com",
		     "ledomatic",
		     "Ledomatic ADK",
		     "1.0",
		     "http://www.ledomatic.com",
		     "0000000012345678");
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
	byte msg[9];
	
	if (acc.isConnected()) {
		int len = acc.read(msg, sizeof(msg), 1);

                


		if (len > 0) {
  /*
                       Serial.print("\r\n Data length: ");
                       Serial.print(msg[0], DEC);
                       Serial.print(msg[1], DEC);
                       Serial.print(msg[2], DEC);
                       Serial.print(msg[3], DEC);
                       Serial.print(msg[4], DEC);
                       Serial.print(msg[5], DEC);
                       Serial.print(msg[6], DEC);

                       Serial.print(msg[7], DEC);
                       Serial.print(msg[8], DEC);
                       */

			// assumes only one command per packet
			  analogWrite(LED1_RED, 255 - msg[0]);
  			  analogWrite(LED1_GREEN, 255 - msg[1]);
					analogWrite(LED1_BLUE, 255 - msg[2]);
					analogWrite(LED2_RED, 255 - msg[3]);
					analogWrite(LED2_GREEN, 255 - msg[4]);
					analogWrite(LED2_BLUE, 255 - msg[5]);
					analogWrite(LED3_RED, 255 - msg[6]);
					analogWrite(LED3_GREEN, 255 - msg[7]);
					analogWrite(LED3_BLUE, 255 - msg[8]);

		}

		
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


