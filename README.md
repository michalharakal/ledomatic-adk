## Android ADK Challenge

**led-o-matic ADK** is my submision to the II. round for the *Android ADK Challenge* for Google Developer Day 
Berlin 2011. 

##Idea
**led-o-matic adk** is music-to-colour light converter. It consist of 3 basic parts:

  * android device for sound input, analysis and visualisation
  * *light unit* for electrical control of LED stripes
  * LED RGB stripes
  
An Android device (phone or tablet) with Android Open Accessory support records music over device's 
microphone. It analyses the sound's spectrum in a real time and calculates the desired colour for 3 bandwidth areas. 
The desired colour is sent with generic protocol ([firmata](http://firmata.org/wiki/Main_Page)) over a USB cable 
to the *light unit*, which physically controls LED RGB stripes. *Light unit* can control the intensity 
of the every colour on the every channel with 9 PWM outputs on *Android Open Accessory Development Kit*, 
so you can get 3 various colours on 3 LED RGB stripes.