<?php
include_once("libraries/BaseAPI.php");
include_once("libraries/HandleLogin.php");
function writeDebug($data)
{
    $fp = fopen('/mnt/tmpfs/cephcmd/cmd.txt', 'a');
    // $gmt = $this->getTZ($timezone);        
    // date_default_timezone_set($timezone);     
    // fwrite($fp, date("Y-m-d H:i:s").' '.$api.' '.$_SERVER['REMOTE_ADDR'].PHP_EOL);   
    // $data = file_get_contents("php://input");
    fwrite($fp, $data.PHP_EOL);   
    fclose($fp);
}

function varDumpToString ($var){
          ob_start();
          var_dump($var);
          $result = ob_get_clean();
          return $result;
}
//ini_set("memory_limit","1M");
header("Content-type: text/html; charset=utf-8");
$result = 0;

if(isset($_REQUEST['progress'])) //getting the progress on an upload
{    
    session_start();
   //get the key of this particular upload - based on the passed parameter
    $key = ini_get("session.upload_progress.prefix").$_REQUEST["progress"];
    // writeDebug("=====key:$key=====");
    // writeDebug("=====Session key:".varDumpToString($_SESSION[$key])."=====");    
    if(isset($_SESSION[$key]))
        echo json_encode($_SESSION[$key]); //make it easy for our JavaScript to handle the progress data
    else //the session doesn't exist, which means the upload has already finished or has not yet started
        echo json_encode($key);
    session_write_close();
    return;
}
// $session_path="/LocalDB/admin/session";
// session_save_path($session_path);
// session_start();
// if(!isset($_POST['AccessKey']))
// {
//     $result = -98;
// }
// else
//     $sid = $_POST['AccessKey'];
// if(isset($_SESSION['AccessKey']))
// {
//     if($sid != $_SESSION['AccessKey'])
//     {
//         $result = -101;
//     }
// }
// else
// {
//     $result = -100;
// }
if($result == 0)
{
    $limit_size=1073741824;
    if ($_FILES["updatefile"]["error"] == UPLOAD_ERR_FORM_SIZE)
    {
        $result = 1;
    }
    $file_bytes=$_FILES['updatefile']['size'];
    if($result == 0)
    {
        if($file_bytes < $limit_size)
        {
            // writeDebug("=====$file_bytes=====");
            $srcarg = str_replace("$", "\\$", $_FILES['updatefile']['tmp_name']);
            $srcarg = '"'.str_replace("`", "\\`", $srcarg).'"';
            $desarg = '"'.'/var/www/html/updateUpload/update.bin'.'"';
            $cmd_clean_upload_folder = "rm -rf /var/www/html/updateUpload/update.bin";
            $cmd = 'mv '.$srcarg." ".$desarg;
            // $cmd_delete_session = 'rm -f /tmp/sess_*';
            //system($cmd_clean_upload_folder,$rtn);
            // sleep(20);
            // writeDebug($cmd);            
            system($cmd_clean_upload_folder,$rtn);
            system($cmd,$rtn);            
            // system($cmd_delete_session,$rtn);
            if(file_exists('/var/www/html/updateUpload/update.bin'))
                $result = 0;
            else
                $result = 2;
        }
        else
        {
            $result = 1;
        }
    }
}

?>
<script language="javascript" type="text/javascript">window.parent.window.finishUpload(<?php echo $result; ?>);</script>

