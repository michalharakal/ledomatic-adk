//https://gitorious.org/av500/bacon-lunch/blobs/master/main.c

#include "WProgram.h"
#include "led.h"

Led::Led(int r_pin, int g_pin, int b_pin, int color)
{
  _color = color;
  _step  = 0;
  _level = 5;
  _r_pin = r_pin;
  _g_pin = g_pin;
  _b_pin  = b_pin;

}

#define RED 100
#define GRN 100
#define BLU 255

static unsigned int colors[][3] = {
	{ RED, 000, 000 },
	{ RED, GRN, 000 },
	{ 000, GRN, 000 },
	{ 000, GRN, BLU },
	{ 000, 000, BLU },
	{ RED, 000, BLU },
};

#define MAX_COLOR (sizeof( colors ) / sizeof ( int[3] ) )
#define MAX_STEP  128
#define MAX_LEVEL 10

void Led::rgb_set( int r, int g, int b )
{
  analogWrite(_r_pin, 255- r);
  analogWrite(_g_pin, 255-g);
  analogWrite(_b_pin, 255-b);
}


void Led::change_color( void )
{
	int next = _color + 1;
	if( next == MAX_COLOR )
		next = 0;
		
	unsigned int r1 = colors[_color][0];
	unsigned int g1 = colors[_color][1];
	unsigned int b1 = colors[_color][2];
	unsigned int r2 = colors[next][0];
	unsigned int g2 = colors[next][1];
	unsigned int b2 = colors[next][2];

       // TODO check map function from arduino
       // long map(long x, long in_min, long in_max, long out_min, long out_max)
       // http://www.arduino.cc/en/Reference/Map
	
	unsigned int r = ( r1 * (MAX_STEP - _step) / MAX_STEP + r2 * _step / MAX_STEP ) * _level / MAX_LEVEL;
	unsigned int g = ( g1 * (MAX_STEP - _step) / MAX_STEP + g2 * _step / MAX_STEP ) * _level / MAX_LEVEL;
	unsigned int b = ( b1 * (MAX_STEP - _step) / MAX_STEP + b2 * _step / MAX_STEP ) * _level / MAX_LEVEL;
	
	rgb_set( r, b, g );
	
	if( ++_step == MAX_STEP ) {
		_step = 0;
		_color = next;
	}
}




