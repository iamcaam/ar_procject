<?php
include_once("libraries/ShareProcess.php");
include_once("libraries/Language.php");
function media_type($path,$sid)
{
    $extension=substr(strrchr($path,'.'),1);
    $playpath = "web/play/".rand(100,999).$sid.rand(100,999)."/$path";
    switch(strtolower($extension))
    {
        case "ogg":
        case "oga":    
                echo '   <source src="'.$playpath.'"'." type=".'"'.'audio/ogg'.'"'."/>";
                break;
        case "mp3":
                echo '   <source src="'.$playpath.'"'." type="."'".'audio/mpeg'."'"."/>";
                break;	
        default :
    }
}
if(!isset($_GET['sid']))                 
    die("Can't Find Share (Result : -1).");                                         
else
    $sid = $_GET['sid'];
if(!isset($_GET['id']))                
    die("Can't Find Share (Result : -2).");                
else
    $rootid = $_GET['id'];
if(!isset($_GET['key']))
    die("Can't Find Share (Result : -3).");
else
    $key = $_GET['key'];
if(!isset($_GET['obj']))
    die("Can't Find Share (Result : -4).");
else
    $obj = $_GET['obj'];

$connectdb = new ConnectDB();
$dbh = $connectdb->Connect('NewWDB');
$shareProcess = new ShareProcess();
$result = $shareProcess->CheckPublicShare($sid, $rootid, $key, $dbh);                   
if(count($result) > 0 && $result[0] >= 0)
{
    include_once("libraries/FileStructureProcess.php");
    $structureProcess = new StructureProcess(false,"getpath");
    $dirpath = $structureProcess->GetPhysicalPath($sid, $rootid, $dbh, true);
    $filepath = $structureProcess->GetFilePhysicalPath($sid,$obj,$dbh,true);    
    if(strlen($filepath) > 0 && strlen($dirpath) > 0 )
    {
        $pos = strpos($filepath, $dirpath);
        if ($pos === false) {
            die("Can't Find Share (Result : -6).");
        } else {
            $path = $filepath;
        }
    }
    else
    {
        die("Can't Find Share (Result : -7).");
    }
}
else
    die("Can't Find Share (Result : -5).");
$lang = new Language(true);
$lang->load("langAudio");   
?>

    <!DOCTYPE html>
    <html><head>    
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="initial-scale = 1.0,width=device-width,maximum-scale = 2.0" />
    <link rel="stylesheet" type="text/css" href="style/audio-style.css" />
    <title>Music</title>
    <script type="text/javascript" src="js/stillwork.js"></script></head>
    <body>

    <div style="text-align:center">

    <audio id="audio1" controls="controls" autoplay="autoplay" loop="loop">
    <?php media_type($path,$sid); ?>
    Your browser does not support HTML5 audio.
    </audio>
    </div>   
    </body>
    </html>


