<?php
include_once("libraries/FileStructureProcess.php");
include_once("webapi/session_handler.php");
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
if(isset($_GET['sid']))
    $sid=$_GET['sid'];
else
{
    die("Error -98");
    return;
}
if(isset($_GET['cid']))
    $cid=$_GET['cid'];
else
{
    die("Error -98");
    return;
}
$session = new Session();
if(isset($_SESSION['AccessKey']) && isset($_SESSION['uid']))
{
    if($sid != $_SESSION['AccessKey'])
    {
        die("Error -101");
        return;
    }        
    $sid = $_SESSION['uid'];
}
else
{
    die("Error -100");
    return;
}
$lang = new Language(true);
$lang->load("langAudio");   
$connectdb = new ConnectDB();
$dbh = $connectdb->Connect('NewWDB');
$structureProcess = new StructureProcess(false,"getpath");
$path = $structureProcess->GetFilePhysicalPath($sid,$cid,$dbh,true);
if($path == '')
{
    die("Error -4");
    return;
}
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


