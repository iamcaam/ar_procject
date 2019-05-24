<?php
header("Content-type: text/html; charset=utf-8");
include_once("session_auth.php");
include_once("restful.php");
include_once("MountLUN.php");
include_once("ShareDB.php");
include_once("libraries/Language.php");
function get_basename($filename)
{
    return preg_replace('/^.+[\\\\\\/]/', '', $filename);	
}
function output_file($file, $name, $mime_type='')
{
    /*
    This function takes a path to a file to output ($file), 
    the filename that the browser will see ($name) and 
    the MIME type of the file ($mime_type, optional).

    If you want to do something on download abort/finish,
    register_shutdown_function('function_name');
    */
    global $lang;
    if(!is_readable($file)) die($lang->line("langShareFile.ErrNotFoundFile"));

    $size = filesize($file);
    $name = rawurldecode($name);

    /* Figure out the MIME type (if not specified) */
    $known_mime_types=array(
        "pdf" => "application/pdf",
        "txt" => "text/plain",
        "html" => "text/html",
        "htm" => "text/html",
        "exe" => "application/octet-stream",
        "zip" => "application/zip",
        "doc" => "application/msword",
        "xls" => "application/vnd.ms-excel",
        "ppt" => "application/vnd.ms-powerpoint",
        "gif" => "image/gif",
        "png" => "image/png",
        "jpeg"=> "image/jpg",
        "jpg" =>  "image/jpg",
        "php" => "text/plain"
    );

    if($mime_type==''){
        $file_extension = strtolower(substr(strrchr($file,"."),1));
        if(array_key_exists($file_extension, $known_mime_types)){
            $mime_type=$known_mime_types[$file_extension];
        } else {
            $mime_type="application/force-download";
        };
    };

    @ob_end_clean(); //turn off output buffering to decrease cpu usage

    // required for IE, otherwise Content-Disposition may be ignored
    if(ini_get('zlib.output_compression'))
    ini_set('zlib.output_compression', 'Off');

    header('Content-Type: ' . $mime_type);
    header("Content-Type: application/octet-stream; text/html; charset=utf-8");
    header("Content-Type: application/download; text/html; charset=utf-8");
    //header('Content-Disposition: attachment; filename="'.$name.'"');
    if(preg_match('/MSIE/i',  $_SERVER['HTTP_USER_AGENT']))    
            header('Content-Disposition: attachment; filename='.urlencode($name));    
    else
            header('Content-Disposition: attachment; filename='.$name);
    header("Content-Transfer-Encoding: binary");
    header('Accept-Ranges: bytes');

    /* The three lines below basically make the 
        download non-cacheable */
    header("Cache-control: private");
    header('Pragma: private');
    header("Expires: Mon, 26 Jul 1997 05:00:00 GMT");

    // multipart-download and download resuming support
    if(isset($_SERVER['HTTP_RANGE']))
    {
        list($a, $range) = explode("=",$_SERVER['HTTP_RANGE'],2);
        list($range) = explode(",",$range,2);
        list($range, $range_end) = explode("-", $range);
        $range=intval($range);
        if(!$range_end) {
            $range_end=$size-1;
        } else {
            $range_end=intval($range_end);
        }

        $new_length = $range_end-$range+1;
        header("HTTP/1.1 206 Partial Content");
        header("Content-Length: $new_length");
        header("Content-Range: bytes $range-$range_end/$size");
    } else {
        $new_length=$size;
        header("Content-Length: ".$size);
    }

    /* output the file itself */
    $chunksize = 1*(512*1024); //you may want to change this
    $bytes_send = 0;
    if ($file = fopen($file, 'r'))
    {
        if(isset($_SERVER['HTTP_RANGE']))
        fseek($file, $range);
        @ob_end_flush(); 
        while(!feof($file) && 
            (!connection_aborted()) && 
            ($bytes_send<$new_length)
            )
        {
            $buffer = fread($file, $chunksize);
            print($buffer); //echo($buffer); // is also possible
            flush();
            $bytes_send += strlen($buffer);
        }
        fclose($file);
    } else die($lang->line("langShareFile.ErrOpenFile"));

    die();
}
$lang = new Language(false);
$lang->load("langShareFile");
if($_SERVER['HTTPS'] != on && !is_mobile())
{
    echo $lang->line("langShareFile.WebNotAllow");
    return;
}
if(isset($_POST['SHID']))
    $SHID=$_POST['SHID'];
else if (isset($_GET['SHID']))
    $SHID=$_GET['SHID'];
if (($SHID == ""))
    return;
$DB_URL=GetDBIP();
$processdb = new ProcessDBForUI();
if(!isset($DB_URL)||$DB_URL == "")
    $rvls = $processdb->LocalDBProcessForGetPath($SHID);
else
    $rvls = $processdb->RemoteDBProcessForGetPath ($SHID, $DB_URL);

if(is_null($rvls))
{
    echo $lang->line("langShareFile.ErrNotFoundFile");	
    return;
}
//$RESTFUL=new __RESTful__;
//$url="http://$DB_URL/WARestService/$SHID";
//$rvls=$RESTFUL->GetFromDB($url);


//if($rvls['Result'] == true)
//{
    $mountresult = mountlun($rvls["UserName"],"0", 0, $rvls["LUNName"], 1);
    if($mountresult['status'] == "ok"){                 
        $file=$rvls['Path'];
        if (file_exists($file)) {
            set_time_limit(0);  
            output_file($file, get_basename($file), '');
//            if(preg_match('/MSIE/i',  $_SERVER['HTTP_USER_AGENT']))
//            {
//                    header('Content-Disposition: attachment; filename='.urlencode(get_basename($file)));
//            }
//            else
//            {
//                    header('Content-Disposition: attachment; filename='.get_basename($file));
//            }
//            header('Content-Description: File Transfer');
//            header('Content-Type: application/x-download; text/html; charset=utf-8');
//            header('Content-Transfer-Encoding: binary');
//            header('Expires: 0');
//            header('Cache-Control: must-revalidate, post-check=0, pre-check=0');
//            header('Pragma: public');
//            header('Content-Length: ' . filesize($file));
//            ob_clean();
//            flush();
//            $chunksize = 1 * (1 * 512);
//            $handle = fopen($file, 'rb'); 
//            while (!feof($handle)) 
//            { 
//                $buffer = fread($handle, $chunksize); 
//                echo $buffer; 
//                ob_flush(); 
//                flush(); 
//            } 
            exit;
        }
        else         
             echo $lang->line("langShareFile.ErrNotFoundFile");	      
    }
    else
    {
        echo "Error 1 : ".$lang->line("langShareFile.ErrNotFoundFile")."(".$mountresult['status'].")";        
    }
//}
//else
//    echo "Error 2 : 找不到分享";
?>