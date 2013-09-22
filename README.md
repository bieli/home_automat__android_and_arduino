HOME AUTOMAT - Android and Arduino
==================================

This repository it is prototype of smartfone application for controlling our home devices.
It is home automation Android panel for Arduino open hardware for controlling in this prototype version four relays (lamps at living room).


Use case description
--------------------

 * Send HTTP POST with data for Arduino open hardware.
 * We sending bit( 0 | 1 | 2 | 3) and state (1 = ON | 0 = OFF) dataset.
 * Arduino has network interface with hardcoded tcp/ip location.
 * Arduino waiting for HTTP POST dataset for parsing and controlling four relays.
 * Relays are connected with lamps in room and we can controlling those lamps from Android phone.
 * We are happy with new toy and before sleeping we can turn off the lights by clicks on smartfone app. ;-)
 
 
Screenshot
-----------
![application screenshot](https://raw.github.com/bieli/home_automat__android_and_arduino/master/home-automat.1.png)

![application screenshot](https://raw.github.com/bieli/home_automat__android_and_arduino/master/home-automat.2.png)


APK Android application for install (currently supported only older system Android 2.1)
-----------
![HOME AUTOMAT APK Icon](https://raw.github.com/bieli/home_automat__android_and_arduino/master/res/drawable-xhdpi/ic_launcher.png)
[![Install by click - HomeAutomation.apk](https://github.com/bieli/home_automat__android_and_arduino/raw/master/bin/HomeAutomation.apk)]

TODO
----
 * move resources into res subdirectory
 * add some unit tests
 * code refactoring
  * AsyncTask for HAService call
  * add interfaces
  * add IoC for HAService (default injecting HttpHa, next Z-Wave, ZeeBee, etc...)
 * home automation network discovering
 * home automation network binding
  