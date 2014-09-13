<?php

$h = getallheaders();
var_dump($_POST, $h = "HA-TOKEN: " . $h['HA-TOKEN']);

error_log( var_export($_POST, true) . "\n", 3, "/tmp/HomeAutoma.response.log");
error_log( var_export($h, true) . "\n", 3, "/tmp/HomeAutoma.response.log");


