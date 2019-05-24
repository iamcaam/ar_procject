<?php
include_once("libraries/ShareProcess.php");
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
                                
$FilePathArr = explode("/", $path);
$EncodeFilePath = '';
foreach($FilePathArr as $PathItem)
{
    $EncodeFilePath .= '/' .rawurlencode($PathItem);
}
?>
<html xmlns="http://www.w3.org/1999/xhtml" >
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">    
    <script type="text/javascript">
    var vlc1;                    
   function VlcObjectManager (elemId, url)
   {
      var wrapperId = elemId;
      var url = url;
      var wrapper;
      this.count = 0;
      
      this.init = function(){
         wrapper = document.getElementById(wrapperId);         
         if (wrapper) {            
            if (window.attachEvent) { //is ie
                //wrapper.innerHTML = '<object name="vlc" id="vlc" type="application/x-vlc-plugin" width="0px" height="0px" events="True" classid="clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921"><param name="MRL" value="'+url+'"/><param name="volume" value="50" /></object>';
                wrapper.innerHTML = '<object name="vlc" id="vlc" type="application/x-vlc-plugin" width="0px" height="0px" events="True" classid="clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921"><param name="volume" value="50" /><param name="autoplay" value="false" /><param id="toolbar" name="toolbar" value="true" /></object>';
                vlc1 = wrapper.firstChild;            
                wrapper.firstChild.style.width = "100%";
                wrapper.firstChild.style.height = "100%";
                var vlcob = document.getElementById('vlc');                 
                vlcob.playlist.add(url);
                setTimeout(function(){vlcob.playlist.play()},500);    
            }
            else {
                wrapper.innerHTML = '<embed name="vlc" id="vlc" type="application/x-vlc-plugin" pluginspage="http://www.videolan.org" version="VideoLAN.VLCPlugin.2" width="720px" height="540px" target="'+ url +'"></embed>';
                vlc1 = wrapper.firstChild;            
                wrapper.firstChild.style.width = "100%";
                wrapper.firstChild.style.height = "100%";
                
            }                  
         }
      };
      this.end = function(){
        
         wrapper.removeChild(vlc);
         delete vlc1;
         vlc1 = null;
      };
   }

   window.onload = function () {        
       vlc1 = new VlcObjectManager ("vlc1", "<?php echo 'web/play/'.rand(100,999).$sid.rand(100,999).$EncodeFilePath ;?>");      
       vlc1.init();     
       
   }
    </script>
</head>
<body marginwidth="0" marginheight="0">
   <div id="vlc1"></div>   
</body>
</html>
