<?php
include_once("libraries/ShareProcess.php");
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


