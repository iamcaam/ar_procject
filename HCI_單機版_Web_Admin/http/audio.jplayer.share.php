<?php
include_once("libraries/ShareProcess.php");
include_once("libraries/Language.php");

function media_type_js($path,$sid)
{            
    $extension=substr(strrchr($path,'.'),1);
    $playpath = "web/play/".rand(100,999).$sid.rand(100,999)."/$path";
    switch(strtolower($extension))
    {
        case "ogg":
        case "oga":
            echo '   oga:'.'"'.$playpath.'"';
            echo '   ,oga:'.'"'.$playpath.'"';
            break;
        case "mp3":
            echo '   m4a:'.'"'.$playpath.'"';
            echo '   ,m4a:'.'"'.$playpath.'"';
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
    <link rel="stylesheet" type="text/css" href="skin/jplayer.blue.monday.css" />
    <title>Music</title>
    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="js/jquery.jplayer.min.js"></script>

    <script type="text/javascript" src="js/stillwork.js"></script>
    <script type="text/javascript">
    $(document).ready(function(){
        $("#jquery_jplayer_1").jPlayer({
            ready: function () {
            $(this).jPlayer("setMedia", {
            <?php media_type_js($path,$sid); ?>
            }).jPlayer("play");
            },

        swfPath: "js",
        solution: "html, flash",
        supplied: "m4a, oga, mp3",
        wmode: "windows"
        });
        });
            </script>
    </head><body>
    <div id="jquery_jplayer_1" class="jp-jplayer"></div>
    <div id="jp_container_1" class="jp-audio">
    <div class="jp-type-single">
    <div class="jp-gui jp-interface">
    <ul class="jp-controls">
    <li><a href="javascript:;" class="jp-play" tabindex="1">play</a></li>
    <li><a href="javascript:;" class="jp-pause" tabindex="1">pause</a></li>
    <li><a href="javascript:;" class="jp-stop" tabindex="1">stop</a></li>
    <li><a href="javascript:;" class="jp-mute" tabindex="1" title="mute">mute</a></li>
    <li><a href="javascript:;" class="jp-unmute" tabindex="1" title="unmute">unmute</a></li>
    <li><a href="javascript:;" class="jp-volume-max" tabindex="1" title="max volume">max volume</a></li>
    </ul>
        <div class="jp-progress">
        <div class="jp-seek-bar">
            <div class="jp-play-bar"></div>
        </div>
        </div>
        <div class="jp-volume-bar">
        <div class="jp-volume-bar-value"></div>
        </div>
        <div class="jp-time-holder">
            <div class="jp-current-time"></div>
            <div class="jp-duration"></div>
            <ul class="jp-toggles">
                <li><a href="javascript:;" class="jp-repeat" tabindex="1" title="repeat">repeat</a></li>
                <li><a href="javascript:;" class="jp-repeat-off" tabindex="1" title="repeat off">repeat off</a></li>
            </ul>
        </div>
        </div>
        <div class="jp-title">
            <ul>     
                <li><?php echo substr(strrchr($path,'/'),1); ?></li> 
            </ul>
        </div>
        <div class="jp-no-solution">
        <span>Update Required</span>
           To play the media you will need to either update your browser to a recent version.
        </div>
        </div>
        </div>    
    </body>
    </html>

