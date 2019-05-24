<?php
include_once("libraries/ShareProcess.php");
include_once("libraries/GlobalFunction.php");
include_once("webapi/session_handler.php");
function ErrorCodeShowDescript($code)
{
    die("Can't Find Share (Result : $code).");
}
$rtnjson = array();        
$rtnjson['result'] = 0;
if(!isset($_GET['idR']))    
{
    $rtnjson['result'] = -3;  
    ErrorCodeShowDescript($rtnjson['result']);
    return;
}
else
    $idR = $_GET['idR'];
if(!isset($_GET['idO']))    
{
    $rtnjson['result'] = -4;  
    ErrorCodeShowDescript($rtnjson['result']);
    return;
}
else
    $idO = $_GET['idO'];
if(!isset($_GET['idU']))    
{
    $rtnjson['result'] = -5;  
    ErrorCodeShowDescript($rtnjson['result']); 
    return;
}
else
    $idU = $_GET['idU'];       
if(!isset($_GET['sharecase']))    
{
    $rtnjson['result'] = -6;  
    ErrorCodeShowDescript($rtnjson['result']);
    return;
}
else
    $sharecase = $_GET['sharecase'];  
if(!isset($_GET['code']))    
{
    $rtnjson['result'] = -7;  
    ErrorCodeShowDescript($rtnjson['result']);
    return;
}
else
    $code = $_GET['code'];
$session = new Session();
if(isset($_SESSION['AccessKey']) && isset($_SESSION['uid']))
{
    if($_GET['sid'] != $_SESSION['AccessKey'])
    {
        $rtnjson['result'] = -101;                        
    }                        
    $sid = $_SESSION['uid'];
}
else                
    $rtnjson['result'] = -100; 

if($rtnjson['result'] == 0)
{
    if(isset($_SESSION['san']) && $_SESSION['san'] == 1)
    {
        include_once('webapi/san_share_handler.php');
        $globalfunct = new GlobalFunction();
        $globalfunct->GetSANURL($san_url);
        $san_share_process = new SAN_Share();
        $stgid = $san_share_process->get_storage_id();        
        $san_share_process->san_handle_check_share($san_url,$stgid,$_SESSION['sanuid'],$sharecase,$idU,$idR,$rtnjson);
        if($rtnjson['result'] == 0)
        {
            $connectdb = new ConnectDB();
            $dbh = $connectdb->Connect('NewWDB');
            $shareProcess = new ShareProcess();                     
            $shareProcess->GetSANSharePath($idU,$idR,$idO,$sharecase,$code,$rtnjson,$dbh);       
        }
    }
    else
    {
        $connectdb = new ConnectDB();
        $dbh = $connectdb->Connect('NewWDB');
        $shareProcess = new ShareProcess();
        $shareProcess->GetSharePath($sid,$idU,$idR,$idO,$sharecase,$code,$rtnjson,$dbh);      
    }
    if($rtnjson['result'] == 0)
    {
        $FilePathArr = explode("/", $rtnjson['playpath']);
        $EncodeFilePath = '';
        foreach($FilePathArr as $PathItem)
        {
            $EncodeFilePath .= '/' .rawurlencode($PathItem);
        }        
    }
    else
        ErrorCodeShowDescript($rtnjson['result']);
}                        
else
    ErrorCodeShowDescript($rtnjson['result']);
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
       vlc1 = new VlcObjectManager ("vlc1", "<?php echo $EncodeFilePath ;?>");      
       vlc1.init();     
       
   }
    </script>
</head>
<body marginwidth="0" marginheight="0">
   <div id="vlc1"></div>   
</body>
</html>
