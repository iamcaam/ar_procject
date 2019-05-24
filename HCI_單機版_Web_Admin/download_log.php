<?php
include_once('/var/www/html/libraries/BaseAPI.php');
include_once('/var/www/html/libraries/ConnectDB.php');
include_once('/var/www/html/libraries/log_handler.php');
$proc_api = new LogProcess();
$proc_api->OutputDownloadFile();