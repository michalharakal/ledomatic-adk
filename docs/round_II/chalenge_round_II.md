#led-o-matic ADK
##author
Michal Harakal, July 2011
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

![simplified block](img/device_blok.png)

##History
**led-o-matic adk** is an extension to the already existing and running device [led-o-matic http://ledomatic.com](http://ledomatic.com).
The device was built in autumn 2010 on barcamp on Google Developer Days in Munich.


## Hardware
### Simplified schema of the *light unit*

![simplified schema](img/schema.png)

## Software
### Android
Android device takes care of sound recording, audio signal analysis, on device visualisation and 
communication with the *light unit* over an open accessory protocol. 

#### sound recording + analysis
<pre>
 // src: http://stackoverflow.com/questions/5774104/android-audio-fft-to-retrieve-specific-frequency-magnitude-using-audiorecord
 int channel_config = AudioFormat.CHANNEL_CONFIGURATION_MONO;
 int format = AudioFormat.ENCODING_PCM_16BIT;
 int sampleSize = 8000;
 int bufferSize = AudioRecord.getMinBufferSize(sampleSize, channel_config, format);
 AudioRecord audioInput = new AudioRecord(AudioSource.MIC, sampleSize, channel_config, format, bufferSize);
 // read audio    
 double[] audioBuffer = new short[bufferSize];
 audioInput.startRecording();
 audioInput.read(audioBuffer, 0, bufferSize);
</pre>

Projects as inspiration:

  * [javaFFT](http://introcs.cs.princeton.edu/97data/FFT.java)
  * [Audalyzer](http://code.google.com/p/moonblink/wiki/Audalyzer)
  * [firmata](http://firmata.org/wiki/Main_Page) for porting to *Android Open Accessory*
  
Despite of many project I can learn and profit from, there is still enough place for innovation 
(colours alignment to frequencies, visualisation, UI for tablet, etc.)  

### *light unit* with arduino
The heart of the *light unit* is Arduino MEGA + USB Host shield (aka *Android Open Accessory Development Kit* ).
The most beautiful thing on this project is the generic firmata protocol. A firmata client sees 
the *light unit* as the generic "firmata" device with 9 analog outputs. Arduino IDE from the version 0022
delivers the code for firmata as an example. Just minor modifications are required to get it working with 
Android Open Accessory protocol.


##Steps to get it done

  * Replace Arduino with Google's Android Development Kit. 
  * build in the next 2 LED Amplifiers to control 3 LED RGB stripes. 
  * take care of a proper power supply (every LED RGB stripe can take 35W power)
  * proper electrical schematics
  * software for android device:
    * audio recording
    * Fast Fourier Transformation
    * firmata over *Android Open Accessory*
    * UI visualisation, should work also without HW connected
  * software on arduino
    * port firmata over serial port to firmata over *Android Open Accessory*
  
### List of material
  * Android Open Accessories capable device
  * pin headers for wiring Arduino and amplifiers
  * Android Development Kit - Arduino Mega+USBHost 
  * 3xLED Amplifier-Repeater 12V for RGB SMD Strip
  * LED stripe
  * 12V 3x35W Power supply (PC power supply)
  * wires, connecters
  * Box for *light unit*
  
## Motivation
I like to build stuff, software and hardware kind. The main motivation for the project is to test and
learn Android Open Accessory and the challenge accelerated my interests. Someone who already worked 
on Apple's "Made For iPod" program can understand the meaning of openness. To build a nice device 
is the best way for "learning by doing".
