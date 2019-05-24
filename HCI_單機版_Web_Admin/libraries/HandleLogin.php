<?php
define("COOKIE_RUNTIME", 1209600);
define("COOKIE_DOMAIN", ".acrored.com");
define("COOKIE_SECRET_KEY", "acrored@TMPS{+$78sfpMJFe-92s");
define("SECURE", FALSE);
define("Manage_Session_Path","/LocalDB/manager/session");
include_once '/var/www/html/libraries/ErrorEnum.php';
//salt :     a7589e77a8a2a21c37b168e6d7ff817b93f2d19a5154f3ac1df51b9efbca535bb1858a75aaaca5ee255408bc1f785619799f2c8b7c5ca32202b68deee71c46d9
//password : 708a0d020a9962ebd63d6667f5eb4f3c8d315ad473748f0774c01114ee6bf5357afc72e5eef1bda7945b6357c447a94e0c843020b3462235cd956b1f0c923639
//$oLogin = new Login();
//$oLogin->Sec_Session_Start();
//$oLogin->DB_Connection();
////var_dump($oLogin->Login_Check());
//echo $oLogin->Login_By_Data('admin', hash('sha512', '000000'));
//var_dump($_SESSION);
class Login  extends BaseAPI{
    
    public $PDODB;
    private $pwd_path = '/LocalDB/admin/pwd';
    private $slat_path = '/LocalDB/admin/slat';
    
    public function Sec_Session_Start($session_path="/LocalDB/admin/session") {        
        session_save_path($session_path);
        ini_set('session.gc_probability', 1);
        $session_name = 'SessionAcrored';   // Set a custom session name 
        $secure = SECURE;

        // This stops JavaScript being able to access the session id.
        $httponly = true;

        // Forces sessions to only use cookies.
        if (ini_set('session.use_only_cookies', 1) === FALSE) {
            echo "Not Support Only Cookies";
            exit();
        }

        // Gets current cookies params.
        $cookieParams = session_get_cookie_params();
        session_set_cookie_params($cookieParams["lifetime"], $cookieParams["path"], $cookieParams["domain"], $secure, $httponly);

        // Sets the session name to the one set above.
        session_name($session_name);

        session_start();            // Start the PHP session         
        session_regenerate_id();    // regenerated the session, delete the old one.        
    }
    
    function Login_By_DB($sInputUserName,$sDomainName,$sInputPassword){        
        $this->connectDB();        
        if($this->dbh == null){
            return CPAPIStatus::DBConnectFail;
        }
        if(is_null($this->log_c))
            $this->log_c = new LogAction($this->dbh);
        $output_domain = null;
        $SQLListDomain = <<<SQL
            SELECT * FROM tbDomainSet WHERE nameDomain = :nameDomain
SQL;
         try        
        {  
            $sth = $this->dbh->prepare($SQLListDomain);
            $sth->bindValue(':nameDomain', $sDomainName, PDO::PARAM_STR);                                
            if($sth->execute())
            {                                              
                while( $row = $sth->fetch() ) 
                {                         
                    $output_domain=array("ID"=>(int)$row['idDomain']);
                }                                 
            }               
        }
        catch (Exception $e){                
            return APIStatus::LoginFail;
        }                 
//        var_dump($output_domain);
        if(is_null($output_domain))
            return APIStatus::LoginFail;        
        $output_user = null;
        $SQLListUser = "SELECT t1.idUser,DES_DECRYPT(FROM_BASE64(t1.password),'".DES_Key."') as password FROM tbUserBaseSet t1 INNER JOIN tbadminset t2 ON t1.idUser=t2.idUser WHERE idDomain=:idDomain AND nameUser=:nameUser";        
        try        
        {  
            $sth = $this->dbh->prepare($SQLListUser);
            $sth->bindValue(':idDomain', $output_domain['ID'], PDO::PARAM_INT);                                
            $sth->bindValue(':nameUser', $sInputUserName, PDO::PARAM_INT);
            if($sth->execute())
            {                                              
                while( $row = $sth->fetch() ) 
                {                         
                    $output_user=array('ID'=>$row['idUser'],'Password'=>$row['password']);
                }                                 
            }               
        }
        catch (Exception $e){                
            return APIStatus::LoginFail;
        }         
//        var_dump($output_user);
        if(is_null($output_user))
            return APIStatus::LoginFail;
//        echo 1;
        $UserSalt = trim(file_get_contents($this->slat_path));
        $sPassword = hash('sha512', $sInputPassword . $UserSalt);  
        $Password = hash('sha512', $output_user['Password']);                  
        $Password = hash('sha512', $Password.$UserSalt);             
        if ($Password === $sPassword) {  
            $session_path="/mnt/tmpfs/session/".$output_domain['ID'].'/'.$output_user['ID'];            
            if(!file_exists($session_path))
                if(!mkdir($session_path,0777,true))
                    return APIStatus::LoginFail;
            $sUserBrowser = $_SERVER['HTTP_USER_AGENT'];            
            $sUsername = preg_replace("/[^a-zA-Z0-9_\-]+/", "", $sInputUserName);
            shell_exec("rm -f $session_path/*");
            $this->Sec_Session_Start($session_path);
            $_SESSION['Username'] = $sUsername;
            $_SESSION['LoginString'] = hash('sha512', $sPassword . $sUserBrowser);
            $_SESSION['UserBrowser'] = $sUserBrowser;             
            session_write_close();
            // Login successful.             
            setcookie('Dom','',time()-60*60*24*365,'/');            
            setcookie('Dom',$this->data_crypt('ar3527678',$output_domain['ID'].'@'.$output_user['ID'],'encrypt'),time()+60*60*24*365,'/');
            setcookie("CheckKey", $sInputPassword);  
            if($this->log_c->createLog(array('Type'=>4,'Level'=>1,'Code'=>'04101201','Riser'=>$sInputUserName,'Message'=>"Admin(Name:".$sInputUserName.",Domain:".$sDomainName.") Login AcroVMS Manager Success"),$last_id)){
                $this->log_c->sqlInsertDomainLogRelation($last_id,$output_domain['ID']);
            }
            return APIStatus::LoginSuccess;
        } else {           
            if($this->log_c->createLog(array('Type'=>4,'Level'=>3,'Code'=>'04301301','Riser'=>$sInputUserName,'Message'=>"Admin(Name:".$sInputUserName.",Domain:".$sDomainName.") Login AcroVMS Manager Fail"),$last_id)){
                $this->log_c->sqlInsertDomainLogRelation($last_id,$output_domain['ID']);
            }
            return APIStatus::LoginFail;
        }           
    }
    

    function data_crypt($key,$data,$mode='encrypt') {
        $key=substr(trim($key),0,24);
        $iv = rand(99999999,10000000);
        $cipher = mcrypt_module_open(MCRYPT_TripleDES,'','cbc','');
        mcrypt_generic_init($cipher, $key, $iv);
        if(strtoupper($mode) == 'ENCRYPT') {
            $data='12345678'.$data;
            $cbc = mcrypt_generic($cipher,$data);
        } else {
            $data = pack('H*',$data);
            $cbc = mdecrypt_generic($cipher,$data);        
        }
        return (strtoupper($mode) == 'ENCRYPT') ? bin2hex($cbc) : trim(substr($cbc,8));
    }
    
    function Login_By_Data($sInputUserName, $sInputPassword,$session_path="LocalDB/admin/session") {        
        $this->connectDB();        
        if($this->dbh == null){
            return CPAPIStatus::DBConnectFail;
        }
        if(is_null($this->log_c))
            $this->log_c = new LogAction($this->dbh);
        $UserPassword = trim(file_get_contents($this->pwd_path));   
        $UserSalt = trim(file_get_contents($this->slat_path));
        // hash the password with the unique salt.        
        $sPassword = hash('sha512', $sInputPassword . $UserSalt);  
        //var_dump($UserPassword);
        //var_dump($sPassword);
        if ($UserPassword === $sPassword) {            
            // Password is correct!
            // Get the user-agent string of the user.
            $sUserBrowser = $_SERVER['HTTP_USER_AGENT'];            
            // XSS protection as we might print this value
            $sUsername = preg_replace("/[^a-zA-Z0-9_\-]+/", "", $sInputUserName);
            shell_exec("rm -f $session_path/*");
            $this->Sec_Session_Start($session_path);
            $_SESSION['Username'] = $sUsername;
            $_SESSION['LoginString'] = hash('sha512', $sPassword . $sUserBrowser);
            $_SESSION['UserBrowser'] = $sUserBrowser;
            session_write_close();
            // Login successful. 
            setcookie("CheckKey", $sInputPassword);
            setcookie('Ext','',time()-60*60*24*365,'/');            
            setcookie('Ext',$this->data_crypt('ar3527678',uniqid(),'encrypt'),time()+60*60*24*365,'/');
            $this->log_c->createLog(array('Type'=>4,'Level'=>1,'Code'=>'04100F01','Riser'=>'admin','Message'=>"Admin Login AcroVMS Administrator Success"),$last_id);            
            return APIStatus::LoginSuccess;
        } else {                    
            $this->log_c->createLog(array('Type'=>4,'Level'=>3,'Code'=>'04301001','Riser'=>'admin','Message'=>"Admin Login AcroVMS Administrator Fail"),$last_id);            
            return APIStatus::LoginFail;
        }           
    }
    
    function Login_By_Data_Web($sInputUserName, $sInputPassword, $token, $session_path="/LocalDB/admin/session") {              
        // var_dump($token);
        $UserPassword = trim(file_get_contents($this->pwd_path));   
        $UserSalt = trim(file_get_contents($this->slat_path));
        // hash the password with the unique salt.             
        $sPassword = hash('sha512', $sInputPassword . $UserSalt);  
        var_dump($UserPassword);
        var_dump($sPassword);
        if ($UserPassword === $sPassword) {            
            // Password is correct!
            // Get the user-agent string of the user.
            $sUserBrowser = $_SERVER['HTTP_USER_AGENT'];            
            // XSS protection as we might print this value
            $sUsername = preg_replace("/[^a-zA-Z0-9_\-]+/", "", $sInputUserName);
            shell_exec("rm -f $session_path/*");
            $this->Sec_Session_Start($session_path);
            $_SESSION['Username'] = $sUsername;
            $_SESSION['LoginString'] = hash('sha512', $sPassword . $sUserBrowser);
            $_SESSION['UserBrowser'] = $sUserBrowser;
            if(isset($token))
                $_SESSION['token'] = $token;
            session_write_close();
            // Login successful. 
            setcookie("CheckKey", $sInputPassword);
            setcookie('Ext','',time()-60*60*24*365,'/');            
            setcookie('Ext',$this->data_crypt('ar3527678',uniqid(),'encrypt'),time()+60*60*24*365,'/');            
            return APIStatus::LoginSuccess;
        } else {                                
            return APIStatus::LoginFail;
        }           
    }
    
    public function Change_Password($sOldPWD,$sNewPWD){                
        $UserPassword = trim(file_get_contents($this->pwd_path));   
        $UserSalt = trim(file_get_contents($this->slat_path));
        $sPassword = hash('sha512', $sOldPWD . $UserSalt);
        if($UserPassword != $sPassword){
            return APIStatus::ChangePWDOldPWDFail;
        }            
        $sNewPWD = hash('sha512', $sNewPWD . $UserSalt);                    
        if (file_put_contents($this->pwd_path, $sNewPWD)) {                 
            return APIStatus::ChangePWDSuccess;                    
        } else {                    
            return APIStatus::ChangePWDFail;
        }
    }
    
    public function Login_Check_By_DB($domainid,$idUser){
        $this->connectDB();        
        if($this->dbh == null){
            return CPAPIStatus::DBConnectFail;
        }
        $output_pwd = null;
        $SQLListUser = "SELECT DES_DECRYPT(FROM_BASE64(password),'".DES_Key."') as password FROM tbUserBaseSet WHERE idDomain=:idDomain AND idUser=:idUser";        
        try        
        {  
            $sth = $this->dbh->prepare($SQLListUser);
            $sth->bindValue(':idDomain', $domainid, PDO::PARAM_INT);                                
            $sth->bindValue(':idUser', $idUser, PDO::PARAM_INT);
            if($sth->execute())
            {                                              
                while( $row = $sth->fetch() ) 
                {                         
                    $output_pwd=$row['password'];
                }                                 
            }               
        }
        catch (Exception $e){                
            return APIStatus::LoginFail;
        }                             
        if(is_null($output_pwd))
            return APIStatus::LoginFail;
        
        $UserSalt = trim(file_get_contents($this->slat_path));         
        
        $sLoginString = $_SESSION['LoginString'];    
        if (isset($_SESSION['Username'], $_SESSION['LoginString'])) {    
            // Get the user-agent string of the user.
            $sUserBrowser = $_SERVER['HTTP_USER_AGENT'];   
            $Password = hash('sha512', $output_pwd);                  
            $Password = hash('sha512', $Password.$UserSalt);
            $sLoginCheck = hash('sha512', $Password . $sUserBrowser);           
            session_write_close();
            if ($sLoginCheck == $sLoginString) {
                // Logged In!!!! 
                return APIStatus::LoginSuccess;
            } else {
                // Not logged in                 
                return APIStatus::LoginFail;
            }        
        } else {
            // Not logged in             
            return APIStatus::LoginFail;
        }
    }
    
    public function Login_Check($input) {    
        // var_dump($input);
        // var_dump($_SESSION);         
        if (isset($_SESSION['Username'], $_SESSION['LoginString'])) {                 
            $sLoginString = $_SESSION['LoginString'];            
            // Get the user-agent string of the user.
            $sUserBrowser = $_SERVER['HTTP_USER_AGENT'];
            $token =  $_SESSION['token'];                                                   
            $UserPassword = trim(file_get_contents($this->pwd_path));
          
            $sLoginCheck = hash('sha512', $UserPassword . $sUserBrowser);
            session_write_close();
            if(isset($input['token'])){
                if($input['token'] != $token)
                    return APIStatus::LoginFail;
            }
            if ($sLoginCheck == $sLoginString) {
                // Logged In!!!! 
                return APIStatus::LoginSuccess;
            } else {
                // Not logged in                 
                return APIStatus::LoginFail;
            }        
        } else {
            // Not logged in             
            return APIStatus::LoginFail;
        }
    }        

    /**
     * Logs in via the Cookie
     * @return bool success state of cookie login
     */
    private function Login_With_Cookie_Data()
    {
        if (isset($_COOKIE['rememberme'])) {
            // extract data from the cookie
            list ($user_id, $token, $hash) = explode(':', $_COOKIE['rememberme']);
            // check cookie hash validity
            if ($hash == hash('sha512', $user_id . ':' . $token . COOKIE_SECRET_KEY) && !empty($token)) {
                // cookie looks good, try to select corresponding user
                if ($this->databaseConnection()) {
                    // get real token from database (and all other data)
                    $sth = $this->db_connection->prepare("SELECT user_id, user_name, user_email FROM users WHERE user_id = :user_id
                                                      AND user_rememberme_token = :user_rememberme_token AND user_rememberme_token IS NOT NULL");
                    $sth->bindValue(':user_id', $user_id, PDO::PARAM_INT);
                    $sth->bindValue(':user_rememberme_token', $token, PDO::PARAM_STR);
                    $sth->execute();
                    // get result row (as an object)
                    $result_row = $sth->fetchObject();

                    if (isset($result_row->user_id)) {
                        // write user data into PHP SESSION [a file on your server]
                        $_SESSION['UserID'] = $result_row->user_id;
                        $_SESSION['UserName'] = $result_row->user_name;                        
                        $_SESSION['UserLoggedIn'] = 1;

                        // declare user id, set the login status to true
                        $this->user_id = $result_row->user_id;
                        $this->user_name = $result_row->user_name;
                        $this->user_email = $result_row->user_email;
                        $this->user_is_logged_in = true;

                        // Cookie token usable only once
                        $this->NewRememberMeCookie();
                        return true;
                    }
                }
            }
            // A cookie has been used but is not valid... we delete it
            $this->deleteRememberMeCookie();
            $this->errors[] = MESSAGE_COOKIE_INVALID;
        }
        return false;
    }
    
    /**
     * Create all data needed for remember me cookie connection on client and server side
     */
    private function NewRememberMeCookie()
    {
        // if database connection opened
        if ($this->DB_Connection()) {
            // generate 64 char random string and store it in current user data
            $random_token_string = hash('sha512', mt_rand());
            $sth = $this->db_connection->prepare("UPDATE users SET user_rememberme_token = :user_rememberme_token WHERE user_id = :user_id");
            $sth->execute(array(':user_rememberme_token' => $random_token_string, ':user_id' => $_SESSION['user_id']));

            // generate cookie string that consists of userid, randomstring and combined hash of both
            $cookie_string_first_part = $_SESSION['user_id'] . ':' . $random_token_string;
            $cookie_string_hash = hash('sha512', $cookie_string_first_part . COOKIE_SECRET_KEY);
            $cookie_string = $cookie_string_first_part . ':' . $cookie_string_hash;

            // set cookie
            setcookie('rememberme', $cookie_string, time() + COOKIE_RUNTIME, "/", COOKIE_DOMAIN);
        }
    }
    
    /**
     * Delete all data needed for remember me cookie connection on client and server side
     */
    private function DeleteRememberMeCookie()
    {
        // if database connection opened
        if ($this->databaseConnection()) {
            // Reset rememberme token
            $sth = $this->db_connection->prepare("UPDATE users SET user_rememberme_token = NULL WHERE user_id = :user_id");
            $sth->execute(array(':user_id' => $_SESSION['UserID']));
        }

        // set the rememberme-cookie to ten years ago (3600sec * 365 days * 10).
        // that's obivously the best practice to kill a cookie via php
        // @see http://stackoverflow.com/a/686166/1114320
        setcookie('rememberme', false, time() - (3600 * 3650), '/', COOKIE_DOMAIN);
    }

    /**
     * Perform the logout, resetting the session
     */
    public function doLogout($sessionPath="/LocalDB/admin/session")
    {
//        $this->DeleteRememberMeCookie();
        $this->sec_session_start();
        $this->SessionDestroy();
        shell_exec("rm -f $session_path/*");   
    }

    public function SessionDestroy()
    {
         // Unset all session values 
        $_SESSION = array();

        // get session parameters 
        $params = session_get_cookie_params();

        // Delete the actual cookie. 
        setcookie(session_name(),'', time() - 42000, $params["path"], $params["domain"], $params["secure"], $params["httponly"]);        
        // Destroy session 
        session_destroy();      
    }
    
}
?>