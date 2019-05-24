<?php
$server = $_SERVER['SERVER_ADDR'];
    if (strpos($_SERVER['SERVER_ADDR'], ':') !== FALSE) {
        $server = '['.$server.']';
    }  
$server='https://'.$server.'/';
echo '<script>window.location.href ="'.$server.'"</script>';
?>
