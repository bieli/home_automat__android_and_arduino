<?php

var_dump($_POST);

error_log( var_export($_POST, true) . "\n", 3, "/tmp/android-app-test123.log");


