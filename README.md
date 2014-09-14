HOME AUTOMAT - Android and Arduino
==================================

This repository it is smartfone application for controlling our home devices.
It is home automation ANDROID OS panel for ARDUINO open hardware controlling four relays (i.e. lamps at living room).

Screenshot
-----------
![Application Home Automat screenshot](https://raw.github.com/bieli/home_automat__android_and_arduino/master/assets/home-automat.v0.5.png)

Use case description
--------------------

 * Send HTTP POST with data for Arduino open hardware.
 * App. sending request type (1 = SET_OUTPUT_DIGITAL) and mask (0 | 1 | 2 | 4) and state value (1 = ON | 0 = OFF) in POST parameters.
 * App. sending special token inputed in app. GUI (from textEdit) - in feature will be unique/generated individual for Your smartfone
 * Arduino has network interface with hardcoded tcp/ip location.
 * Arduino waiting for HTTP POST dataset for parsing and controlling four relays.
 * Relays are connected with lamps in room and we can controlling those lamps from Android phone.
 * We are happy with new toy and before sleeping we can turn off the lights by clicks on smartfone app. ;-)

![application screenshot](https://raw.github.com/bieli/home_automat__android_and_arduino/master/assets/home-automat.2.png)

APK Android application for install (tested only on system Android 2.1 and 4.0)
-----------
![HOME AUTOMAT APK Icon](https://raw.github.com/bieli/home_automat__android_and_arduino/master/res/drawable-xhdpi/ic_launcher.png)
[![Install by click - HomeAutomation.apk](https://github.com/bieli/home_automat__android_and_arduino/raw/master/bin/HA.apk)]


HOW TO DEBUG WITH PHP WEB SERVER EMULATOR SCRIPT
------------------------------------------------
> php -S 192.168.1.5 ha.php 
> 
> array (
>   'type' => '1',
>   'mask' => '2',
>   'value' => '1',
> )
> 'HA-TOKEN: _token_'
> array (
>   'type' => '1',
>   'mask' => '2',
>   'value' => '0',
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
  
