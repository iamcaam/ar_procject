<?php
include_once("libraries/FileStructureProcess.php");
include_once("webapi/session_handler.php");
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

