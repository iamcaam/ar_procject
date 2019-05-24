<?php
// ini_set('display_errors', 'On');
include_once ("/var/www/html/libraries/BaseAPI.php");
include_once ("/var/www/html/libraries/ConnectDB.php");
include_once ("/var/www/html/libraries/HandleLogin.php");
include_once("/var/www/html/libraries/HandleLog.php");
include_once("/var/www/html/libraries/HandleDES.php");
$oLogin = new Login();
//$oLogin->Sec_Session_Start();
// var_dump($_POST);
if(isset($_POST['username']) && isset($_POST['p']))
{       
    $oLogin->connectDB();
    if($_POST['username'] == "admin" &&$oLogin->Login_By_Data_Web($_POST['username'],$_POST['p'],$_POST['token']) == APIStatus::LoginSuccess)
    {            
        if(ConnectDB::$dbh != null)
            LogAction::createLog(array('Type'=>4,'Level'=>1,'Code'=>'04100E01','Riser'=>
            'admin','Message'=>"User(Name:admin) Login Success"),$last_id);
        
        // var_dump($_COOKIE);
        if($_COOKIE['ARChkrm'] == 1){   
            $sUserBrowser = $_SERVER['HTTP_USER_AGENT'];            
            $desC = new STD3Des(base64_encode('ar3527678'));
            $password=$desC->encrypt($sUserBrowser.$_POST['password']);
            // var_dump($password);
            setcookie('arkey',$password);            
            // var_dump(2);
        }
        // var_dump($_COOKIE);
        header("Location: Admin_Main.php");
        exit();      
    }
     else
    {
        if(ConnectDB::$dbh != null)
            LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300D01','Riser'=>'admin','Message'=>"User(Name:admin) Login fail"),$last_id);
        header('Location: index.php?error=1');
        setcookie('arkey','');
        exit();
    }   
}
else
{    
    header('HTTP/1.0 404 Not Found');
    echo "<h1>Error 404 Not Found</h1>";
    echo "The page that you have requested could not be found.";
    exit();
}
?>
