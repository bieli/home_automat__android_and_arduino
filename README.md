HOME AUTOMAT - Android and Arduino
==================================

This repository it is smartfone application for controlling our home devices.
It is home automation ANDROID OS panel for ARDUINO open hardware controlling four relays (i.e. lamps at living room).

Screenshot
-----------
![Application Home Automat screenshot](https://raw.github.com/bieli/home_automat__android_and_arduino/master/assets/home-automat.v0.5.png)

Technical description
--------------------

 * App. send HTTP POST with data for Arduino open hardware with request parameters: type (1 = SET_OUTPUT_DIGITAL), mask (0 | 1 | 2 | 4), value (1 = ON | 0 = OFF)
 * Additinally header HA-TOKEN is sending - inputed by app. layout named HA token - it's unique/generated individual hash for Your smartfone
 * On Arduino side it's network interface with configured tcp/ip location named in app. layout Arduino URL
 * Arduino waiting for HTTP POST request and parsing date for controlling four relays connected as outputs
 * Relays are connected with lamps in room and we can controlling those lamps from Android phone
 * We are happy with new toy and before sleeping we can turn off the lights by clicks on smartfone app. buttons ;-)

![application screenshot](https://raw.github.com/bieli/home_automat__android_and_arduino/master/assets/home-automat.2.png)

APK Android application for install (tested only on system Android 2.1 and 4.0)
-----------
![HOME AUTOMAT APK Icon](https://raw.github.com/bieli/home_automat__android_and_arduino/master/res/drawable-xhdpi/ic_launcher.png)
[![Install by click - HomeAutomation.apk](https://github.com/bieli/home_automat__android_and_arduino/raw/master/bin/HA.apk)]


HOW TO DEBUG WITH PHP WEB SERVER EMULATOR SCRIPT
------------------------------------------------
> php -S 192.168.1.5 ha.php 
> tail -f /tmp/HomeAutoma.response.log
> array (
>   'type' => '1',
>   'mask' => '2',
>   'value' => '1',
> )
> 'HA-TOKEN: _token_'


TODO
----
 * add some unit tests
 * code refactoring
  * AsyncTask for HAService call
  * add IoC for HAService (default injecting HttpHa, next Z-Wave, ZeeBee, etc...)
 * home automation network discovering
 * home automation network binding
  
