#ifndef led_h
#define led_h

#include "WProgram.h"

class Led
{
  private:
    int _r_pin;
    int _g_pin;
    int _b_pin;
    int _color;
    int _step;
    int _level;
    
    

  public:
    Led(int r_pin, int g_pin, int b_pin, int color);
    void change_color( void );
    void rgb_set( int r, int g, int b );


};

#endif
