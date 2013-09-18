home_automat__android_and_arduino
=================================

Home automation Android panel for Arduino open hardware for controlling four relays (lamps at living room).


Use case description
--------------------

 * Send HTTP POST with data for Arduino open hardware.
 * We sending bit( 0 | 1 | 2 | 3) and state (1 = ON | 0 = OFF) dataset.
 * Arduino has network interface with hardcoded tcp/ip location.
 * Arduino waiting for HTTP POST dataset for parsing and controlling four relays.
 * Relays are connected with lamps in room and we can controlling those lamps from Android phone.
 * We are happy with new toy and before sleeping we can turn off the lights by clicks on smartfone app. ;-)
