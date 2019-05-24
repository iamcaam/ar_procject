<?php
include_once("libraries/FileStructureProcess.php");
include_once("webapi/session_handler.php");
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
$connectdb = new ConnectDB();
$dbh = $connectdb->Connect('NewWDB');
$structureProcess = new StructureProcess(false,"getpath");
$path = $structureProcess->GetFilePhysicalPath($sid,$cid,$dbh,true);
if($path == '')
{
    die("Error -4");
    return;
}
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
                var vlcob = document.getElementById('vlc');
                if(typeof  vlcob.playlist == 'undefined' )
                {  
                    wrapper.innerHTML = '<object name="vlc" id="vlc" type="application/x-vlc-plugin" width="0px" height="0px" events="True" classid="clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921"><param name="MRL" value="'+url+'"/><param name="volume" value="50" /></object>';
                    wrapper.firstChild.style.width = "100%";
                    wrapper.firstChild.style.height = "100%";      
                }
                else
                {
                    wrapper.firstChild.style.width = "100%";
                    wrapper.firstChild.style.height = "100%";         
                    vlcob.playlist.add(url);
                    setTimeout(function(){vlcob.playlist.play()},500);    
                }
            }
            else {
                wrapper.innerHTML = '<embed name="vlc" id="vlc" type="application/x-vlc-plugin" pluginspage="http://www.videolan.org" version="VideoLAN.VLCPlugin.2" width="720px" height="540px" target="'+ url +'"></embed>';                           
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
