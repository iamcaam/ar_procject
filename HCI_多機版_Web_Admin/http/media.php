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
        case "mp4" :
            echo '   <source src='.'"'.$playpath.'"'." type="."'".'video/mp4'."'"."/>";
            break;
        case "m4v" :
            echo '   <source src='.'"'.$playpath.'"'." type="."'".'video/mp4'."'"."/>";
            break;
        case "ogv" :
            echo '   <source src='.'"'.$playpath.'"'." type="."'".'video/ogg'."'"."/>";
            break;
        case "webm" :
            echo '   <source src='.'"'.$playpath.'"'." type="."'".'video/webm'."'"."/>";
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
    <html>
        <head>    
            <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
            <link rel="stylesheet" type="text/css" href="style/media-style.css" />
            <title>Media</title>
            <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
            <script type="text/javascript" src="js/media.js"></script>
            <script type="text/javascript" src="js/stillwork.js"></script></head>
            <body>
            <div style="text-align:center">
                <video id="video1" controls="controls" poster autoplay="autoplay" loop="loop">
                    <?php media_type($path,$sid); ?>
                    Your browser does not support HTML5 video.
                </video>
            </div>           
        </body>
    </html>


