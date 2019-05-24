<?php
// define("DEBUG", true);
define("DES_Key","ar3527678ar3527678ar3527");
define("CmdRoot", "sudo /bin/php /var/www/html/shell/CephCmd.php ");
define("CmdJavaRoot", "sudo /bin/java -jar Ceph_Mgt.jar  ");
class BaseAPI
{    
     private $TimeZoneArrMapping=array("Pacific/Midway"=>"-11:00",
    "Pacific/Honolulu"=>"-10:00",
    "US/Alaska"=>"-09:00",
    "US/Pacific"=>"-08:00",
    "US/Arizona"=>"-07:00",
    "US/Mountain"=>"-07:00",
    "America/Chihuahua"=>"-07:00",
    "America/Guatemala"=>"-06:00)",
    "US/Central"=>"-06:00",
    "America/Mexico_City"=>"-06:00",
    "Canada/Saskatchewan"=>"-06:00",
    "America/Bogota"=>"-05:00",
    "US/Eastern"=>"-05:00",
    "US/East-Indiana"=>"-05:00",
    "America/Caracas"=>"-04:30",
    "Canada/Atlantic"=>"-04:00",
    "America/La_Paz"=>"-04:00",
    "America/Manaus"=>"-04:00",
    "America/New_York"=>"-04:00",
    "Canada/Newfoundland"=>"-03:30",
    "America/Santiago"=>"-03:00",
    "America/Fortaleza"=>"-03:00",
    "America/Buenos_Aires"=>"-03:00",
    "America/Godthab"=>"-03:00",
    "America/Montevideo"=>"-02:00",
    "Atlantic/South_Georgia"=>"-02:00",
    "Atlantic/Azores"=>"-01:00",
    "Atlantic/Cape_Verde"=>"-01:00",
    "Africa/Casablanca"=>"+00:00",
    "Europe/London"=>"+00:00",
    "Africa/Monrovia"=>"+00:00",
    "Europe/Amsterdam"=>"+01:00",
    "Europe/Belgrade"=>"+01:00",
    "Europe/Brussels"=>"+01:00",
    "Europe/Sarajevo"=>"+01:00",
    "Africa/Kinshasa"=>"+01:00",
    "Africa/Windhoek"=>"+02:00",
    "Asia/Amman"=>"+02:00",
    "Europe/Athens"=>"+02:00",
    "Asia/Beirut"=>"+02:00",
    "Egypt"=>"+02:00",
    "Africa/Harare"=>"+02:00",
    "Europe/Helsinki"=>"+02:00",
    "Israel"=>"+02:00",
    "Africa/Gaborone"=>"+02:00",
    "Europe/Sofia"=>"+02:00",
    "Europe/Minsk"=>"+03:00",
    "Asia/Baghdad"=>"+03:00",
    "Asia/Kuwait"=>"+03:00",
    "Africa/Nairobi"=>"+03:00",
    "Europe/Moscow"=>"+03:00",
    "Iran"=>"+03:30",
    "Asia/Muscat"=>"+04:00",
    "Asia/Baku"=>"+04:00",
    "Asia/Tbilisi"=>"+04:00",
    "Asia/Yerevan"=>"+04:00",
    "Asia/Kabul"=>"+04:30",
    "Asia/Karachi"=>"+05:00",
    "Asia/Yekaterinburg"=>"+05:00",
    "Asia/Calcutta"=>"+05:30",
    "Asia/Kathmandu"=>"+05:45",
    "Asia/Almaty"=>"+06:00",
    "Asia/Dhaka"=>"+06:00",
    "Asia/Novosibirsk"=>"+06:00",
    "Asia/Rangoon"=>"+06:30",
    "Asia/Bangkok"=>"+07:00",
    "Asia/Krasnoyarsk"=>"+07:00",
    "Asia/Taipei"=>"+08:00",
    "Asia/Hong_Kong"=>"+08:00",
    "Asia/Ulaanbaatar"=>"+08:00",
    "Asia/Kuala_Lumpur"=>"+08:00",
    "Australia/Perth"=>"+08:00",
    "Asia/Irkutsk"=>"+08:00",
    "Asia/Tokyo"=>"+09:00",
    "Asia/Seoul"=>"+09:00",
    "Asia/Yakutsk"=>"+09:00",
    "Australia/Darwin"=>"+09:30",
    "Australia/Brisbane"=>"+10:00",
    "Pacifi/Guam"=>"+10:00",
    "Asia/Vladivostok"=>"+10:00",
    "Asia/Magadan"=>"+10:00",
    "Australia/Adelaide"=>"+10:30",
    "Australia/Tasmania"=>"+11:00",
    "Australia/Melbourne"=>"+11:00",
    "Pacific/Noumea"=>"+11:00",
    "Pacific/Auckland"=>"+12:00",
    "Pacific/Fiji"=>"+12:00");
    private $ha_c;    
    public $snap_action_enum = array ("take" => 101, "delete" => 102, "rollback" => 103, "view" => 104, "viewStop" => 105,"scheduleTake" => 106,"takeUP"=>107,"deleteUP"=>108,"rollbackUP"=>109,"viewUP"=>110,"viewStopUP"=>111,"scheduleTakeUP"=>112); 
    public $backupActionEnum = array ("backup" => 201,"restoreNew" => 202,"restoreSame" =>203,"scheduleBackup"=>204,'seedBackup'=>205,"restoreNewSeed"=>206,"restoreSameSeed"=>207,"upBackup"=>208,"restoreNewUP"=>209,"restoreSameUP"=>210,"scheduleUPBackup"=>211); 
    public $backupResultEnum = array ("ok" => 0,'fail'=>-1,'backup'=>1); 
    public $snap_state_enum = array ("wait"=>0,"doing"=>1,"ok"=>10,"fail"=>-1,'cancel'=>3); 
    public $taskStateEnum = array ("wait"=>1,"doing"=>2,"ok"=>0,"fail"=>-1,'cancel'=>3); 
    public $taskTypeEnum = array ("clone"=>0,"seed"=>1,"born"=>2,"create"=>3,"addDisk"=>10,"deleteDisk"=>11,"export"=>12,"import"=>13,"reseed"=>14,"distribute"=>15,"recovery"=>16,"usertoorg"=>17,"expandDisk"=>18,"moveDisk"=>19,"moveUserProfile"=>20,"addglustergroup"=>21,'renew'=>22,'addglustervolume'=>23,"seedtoorg"=>24,"createAG"=>25); 
    public $glusterStateEnum = array ("boot"=>0,"notReady"=>0,"split"=>1,"split.wait.splitNode.poweron"=>2,"split.wait.discard.nodeName"=>3,"split.recovery"=>4,"no.controlNode"=>5,"wait.controlNode.poweron"=>6,"ready"=>7,"split.wait.mainGroup.poweron"=>8,"waitGroupReady"=>9,"waitSplitBrainGroup"=>10,"GlusterRestoring"=>997,"noGlusterHaveData"=>998,"noGluster"=>999,"noCluster"=>999,"restore"=>997,"waitRemoteMaster"=>11); 
    public $taskActionEnum = array(""=>-1,"orgCreate"=>0,"orgClone"=>1,"seedCreate"=>2,"seedOverwrite"=>3,"seedReborn"=>4,"seedRecovery"=>5,"userCreate"=>6,"userClone"=>7,"userExport"=>8,"usrpfMove"=>9,"vdExport"=>10,"vdImport"=>11,"diskAdd"=>12,"diskDel"=>13,"diskMove"=>14,"vdDel"=>15,"ssTake"=>16,"ssDel"=>17,"ssRol"=>18,"ssView"=>19,"ssUnview"=>20,"bkBackup"=>21,"bkResSame"=>22,"bkResNew"=>23,"glusterGroupAdd"=>24,"glusterVolumeAdd"=>25);
    public $volumeStateEnum = array("ready"=>0,"degrage"=>1,"offline"=>2);
    public $vdServiceTypeEnum = array("AcroGateway"=>1);
    public $viewerProtocolEnum = array("Spice"=>1,"RDP"=>2,"All"=>3);
    protected $getTZCmd = 'sudo /var/www/html/date.sh get tz';
    private $cmdRaidusageList = CmdRoot."ip node_raidusage_list";
    public $isCeph;
    public $diskNumber;
    public $upsBatteryStausEnum = array ("none"=>-1,"unknown"=>0,"normal"=>1,"low"=>2,'depleted'=>3,"discharging"=>4,"failure"=>5);
    public $ssLayerDefault = 3;
    function binary_to_hex2_str($bin_data)
    {
        $hex = '';
        foreach (str_split($bin_data) as $bin) {
            $hex .= sprintf("%02x", ord($bin));
        }
        return trim($hex);
    }

    function isBase64($s)
    {
        return (bool) preg_match('/^[a-zA-Z0-9\/\r\n+]*={0,2}$/', $s);
    }

    function loginCheck(&$defaultValues,$sessionPath="/LocalDB/admin/session",$regSession=false)
    {        
        $oLogin = new Login(); 
        // var_dump($sessionPath);       
        // $oLogin->Sec_Session_Start($sessionPath);
        // $this->getDefaultCephDomain($defaultValues);
        // var_dump($defaultValues);        
        if($sessionPath == "/LocalDB/admin/session"){
            $oLogin->Sec_Session_Start($sessionPath);
            $this->getDefaultCephDomain($defaultValues);
            // var_dump($defaultValues);
            if($oLogin->Login_Check($defaultValues) != APIStatus::LoginSuccess)
            {
                http_response_code(401);
                exit();
            }
        }
        else{                    
            if (count(glob("/mnt/tmpfs/LocalDB/manager/session/*")) !== 0 ) { 
                $sessionPath = "/mnt/tmpfs/LocalDB/manager/session";
            }
            session_save_path($sessionPath);
            session_start();            
            $this->getDefaultCephDomain($defaultValues);
            if(file_exists('/mnt/tmpfs/ssh.enable')){
                //Test
                session_write_close();
                $oLogin->sanLoginCheck($defaultValues);
            }
            else{
                if($oLogin->sanLoginCheck($defaultValues) != APIStatus::LoginSuccess)
                {                
                    http_response_code(401);
                    exit();
                }
            }                                  
        }         
        return APIStatus::LoginSuccess;
    }          

    function getDefaultCephDomain(&$defaultValues)
    {               
        // var_dump($_SESSION);
        if(!$this->connectDB()){                         
            unset($_SESSION['DomainID']);
            unset($_SESSION['CephID']);
            unset($_SESSION['ConnectIP']);
            $defaultValues['DomainID'] = null;
            $defaultValues['CephID'] = null;        
            if(isset($_SESSION['VMSIP'])){
                $defaultValues['ConnectIP'] = $_SESSION['VMSIP'];
            }
            else
                $defaultValues['ConnectIP'] = '127.0.0.1';       
        }          
        else{                                   
            if(!isset($_SESSION['DomainID'])){
                // echo 1;
                $domainC = new DomainAction();                
                if($domainC->listDefaultDomain($output_default_domain)){                    
                    $ceph_c = new CephPoolAction();
                    if($ceph_c->list_default_one_ceph_and_server($output_default_ceph)){                        
                        $_SESSION['DomainID'] = $output_default_domain['DomainID'];
                        $_SESSION['CephID'] = $output_default_ceph['CephID'];
                        // $_SESSION['ConnectIP'] = $output_default_ceph['NodeAddress'];
                        if(isset($_SESSION['VMSIP'])){
                            $_SESSION['ConnectIP'] = $_SESSION['VMSIP'];
                        }
                        else
                            $_SESSION['ConnectIP'] = '127.0.0.1';    
                    }
                    else{
                        $_SESSION['DomainID'] = $output_default_domain['DomainID'];
                        $_SESSION['CephID'] = 1;
                        $_SESSION['ConnectIP'] = '127.0.0.1';    
                    }                                            
                }
            }
            $defaultValues['DomainID'] = $_SESSION['DomainID'];
            $defaultValues['CephID'] = $_SESSION['CephID'];
            $defaultValues['ConnectIP'] = $_SESSION['ConnectIP'];
        }
    }     

    public function getUIConfig(&$output)
    {   
        $pathConfig = '/var/www/html/ui.config';
        $output = array();
        $config = file_get_contents($pathConfig);
        if(!is_null($config))
            $output = json_decode($config,true);       
    }

    public function getStorageType()
    {
        $isCeph = null;
        // exec(CmdRoot.'127.0.0.1 node_storage_type_list',$outputArr,$rtn);
        // if($rtn == 0 && !is_null($outputArr[0])){
        //    switch ($outputArr[0]) {
        //         case 'ceph':
        //             $this->getDiskNumber();
        //             $this->isCeph = true;
        //             break;
        //         case 'zfs':
                    $this->getDiskNumber();
                    $this->isCeph = false;
        //             break;
        //         default:                
        //            break;
        //    }
        // }            
    }

    public function getDiskNumber(){
        $this->diskNumber = 2;
        exec(CmdRoot.'127.0.0.1 dk_disk_list',$outputArr,$rtn);
        if($rtn == 0 && !is_null($outputArr[0]))
        {
            $outputDisk = json_decode($outputArr[0],true);
            if(!is_null($outputDisk))
                $this->diskNumber = count($outputDisk);
        }
    }

    public function getPoweonLimit()
    {
        $poweronLimit = 5;
        exec(CmdRoot.'127.0.0.1 node_maxPowerOn_list',$outputArr,$rtn);
        if($rtn == 0 && !is_null($outputArr[0])){
            $poweronLimit = $outputArr[0];
        }            
        return $poweronLimit;
    }

    public function listRAIDUsage($input,&$output)
    {
        $raidAlarm = 80;
        $raidUsageMax = 98;
        $hostname = NULL;
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdRaidusageList;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);   
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);
            if($rtn == 0){                   
                if(isset($outputArr[0])){
                    $outputCmd = json_decode($outputArr[0],true);
                    $raidAlarm = $outputCmd['raidAlarm'];
                    $raidUsageMax = $outputCmd['raidUsageMax'];
                }
            }            
        }             
        $output = array('RAIDUsageMax' =>$raidUsageMax,'RAIDAlarm'=>$raidAlarm);  
    }   

    public function Cmd_Send($cmd)
    {     
        $output = shell_exec($cmd);
        return $output;
    }         

    function checkJson($input)
    {
        if(is_null($input)){
            http_response_code(400);
            exit();
        }
    }
    
    function sqlSetTimeZone($gmt)
    {
        $sqlSet = <<<SQL
                    set time_zone=:timezone;
SQL;
        $sth = connectDB::$dbh->prepare($sqlSet);
        $sth->bindValue(':timezone', $gmt, PDO::PARAM_STR);
        $sth->execute();      
    }


    function setDefaultValueToInput($defaultValues,&$input)
    {
        foreach ($defaultValues as $key => $value) {
            $input[$key] = $value;
        }
    }

    public function getConnectIP()
    {
        return "127.0.0.1";
    }

    public function getTZ(&$timezone)
    {        
        $timezone = exec($this->getTZCmd,$outputarr);         
        $gmt = $this->TimeZoneArrMapping[$timezone] == null ? "+08:00" : $this->TimeZoneArrMapping[$timezone];     
        return $gmt;       
    }

    public function getPHPTZ(&$timezone)
    {        
        $timezone = exec($this->getTZCmd,$outputarr);         
        return $timezone;       
    }


    public function logappendVDDomain($vdName,$domainName)
    {
        $appendVDDomain = '';
        if(!is_null($vdName)){
            $appendVDDomain .= '(';
            if(!is_null($vdName)){
                $appendVDDomain .= "Name:$vdName";
            }            
            $appendVDDomain .= ')';
        }        
        return $appendVDDomain;
    }

    public function logSnapshotAppendVDDomain($vdName,$domainName,$desc=NULL,$date=NULL)
    {
        $appendVDDomain = '';
        if(!is_null($vdName)){
            $appendVDDomain .= '(';
            if(!is_null($vdName)){
                $appendVDDomain .= "Name:$vdName";
            }     
            if(!is_null($date)){
                $appendVDDomain .= ",Layer Date:$date";
            }      
            if(!is_null($desc)){
                $appendVDDomain .= ",Layer Description:$desc";
            }      
            $appendVDDomain .= ')';
        }        
        return $appendVDDomain;
    }

    function sqlGetUUID(&$output){
        $output = null;
        $SQLList = <<<SQL
            SELECT UUID() as UUID
SQL;
        try
        {                                             
            $sth = ConnectDB::$dbh->prepare($SQLList);
            if($sth->execute())
            {                                
                while( $row = $sth->fetch() ) 
                {     
                    $output = array('UUID'=>$row['UUID']);
                }
            }                   
        }
        catch (Exception $e){ 
            $output = null;
        }   
        if(is_null($output))     
            return false;
        return true;     
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
    
    function get_service_ip()
    {
        if($this->ha_c == NULL)
            $this->ha_c = new HAProcess();
        $ha_content = json_decode($this->ha_c->list_ha(),true);        
        return $ha_content['service_ip'];       
    }
    
    function connectDB()
    {         
        if(ConnectDB::$dbh == null){                       
            ConnectDB::connect();            
        }         
        if(ConnectDB::$dbh == null)
            return false;        
        return true;
    }
    
    function baseOutputResponse($code)
    {        
        switch ($code)
        {
            case APIStatus::VDSC_CMD_NOT_FOUND:
            case APIStatus::VDSC_CMD_ERROR:
                http_response_code(400);
                break;
            case APIStatus::Conflict:                
                http_response_code(409);
                break;
            case APIStatus::NotFound:
            case APIStatus::VDSC_CMD_CONNECT_FAIL:
                http_response_code(404);
                break;
            case APIStatus::DBConnectFail:
                http_response_code(500);
                break;
        }
    }
    
    function output_json($data)
    {
        header('Content-Type: application/json; charset=utf-8');         
        echo json_encode($data);
    }
    
    function output_raw_json($data)
    {
        header('Content-Type: application/json; charset=utf-8');         
        echo $data;
    }
    
    function encrypt($toencrypt){  
        $key = 'ar3527678ar3527678ar3527';  

        //使用3DES方法加密   
        $encryptMethod = MCRYPT_TRIPLEDES;                
        
       
        //初始化向量來增加安全性
        $iv = mcrypt_create_iv(mcrypt_get_iv_size($encryptMethod,MCRYPT_MODE_ECB), MCRYPT_RAND);  
        
        //使用mcrypt_encrypt函數加密，MCRYPT_MODE_ECB表示使用ECB模式
        $encrypted_toencrypt = mcrypt_encrypt($encryptMethod, $key, $toencrypt, MCRYPT_MODE_ECB,$iv);   
                
        //回傳解密後字串
        return base64_encode($encrypted_toencrypt);
    }
    
    function decrypt($todecrypt) {  
        //解密用的key，必須跟加密用的key一樣   
        $key = 'ar3527678ar3527678ar3527';  

        //解密前先解開base64碼
        $todecrypt = base64_decode($todecrypt);

        //使用3DES方法解密
        $encryptMethod = MCRYPT_TRIPLEDES;  

        //初始化向量來增加安全性 
        $iv = mcrypt_create_iv(mcrypt_get_iv_size($encryptMethod,MCRYPT_MODE_ECB), MCRYPT_RAND);  

        //使用mcrypt_decrypt函數解密，MCRYPT_MODE_ECB表示使用ECB模式  
        $decrypted_todecrypt = mcrypt_decrypt($encryptMethod, $key, $todecrypt, MCRYPT_MODE_ECB,$iv);

        //回傳解密後字串
        return $decrypted_todecrypt;  
    }  

    function getAllocatedSize()
    {
        $allocate = 0;
        if($this->connectDB()){
            $allocate = $this->sqlListAllDiskSize();
        }     
        // var_dump($allocate);
        return $allocate;
    }

    function sqlListAllDiskSize()
    {
        $allocate = 0;
        $SQLList = <<<SQL
            select SUM(sizeVDisk) as Allocate from tbvdiskset;
SQL;
        try
        {                                           
            $sth = connectDB::$dbh->prepare($SQLList);                   
            if($sth->execute())
            {         
                $output = array();
                while( $row = $sth->fetch() ) 
                {             
                    if(!is_null($row['Allocate']))          
                        $allocate = $row['Allocate'];
                }
            }                   
        }
        catch (Exception $e){ 
            $allocate = 0;
        }        
        return $allocate;
    }  

    function sqlAddtbIpPortMappingSetTable()
    {      
        $result = false;

        $sqlAddField = <<<SQL
            create table `tbIpPortMappingSet` (`idPortMapping` int not null  auto_increment ,`idVDServer` bigint not null ,`ipExternal` longtext not null ,`portExternal` int not null ,`portInternal` int not null ,`date_created` datetime not null ,`date_modified` datetime not null ,primary key ( `idPortMapping`) ) engine=InnoDb auto_increment=0;
            CREATE index  `IX_idVDServer` on `tbIpPortMappingSet` (`idVDServer` DESC) using HASH;
            alter table `tbIpPortMappingSet` add constraint `FK_tbIpPortMappingSet_tbVDServerSet_idVDServer`  foreign key (`idVDServer`) references `tbVDServerSet` ( `idVDServer`)  on update cascade on delete cascade;
SQL;
        try
        {             
            if (count(connectDB::$dbh->query("SHOW TABLES LIKE 'tbIpPortMappingSet'")->fetchAll())){
                 $result = true;
            }   
            else{                           
                $sth = connectDB::$dbh->prepare($sqlAddField);                                          
                if($sth->execute())
                {         
                    $result = true;
                }   
            }                
        }
        catch (Exception $e){             
        }    
        return $result;
    }

    function sqlAddVDSnapshotMaxCountField()
    {
        $result = false;
        $sqlAddField = <<<SQL
            alter table `tbVDImageBaseInfoSet` add column `limitSSLyer` smallint not null DEFAULT 30;
SQL;
        try
        {             
            if (count(connectDB::$dbh->query("SHOW fields from tbVDImageBaseInfoSet LIKE 'limitSSLyer'")->fetchAll())){
                $result = true;
            }   
            else{                           
                $sth = connectDB::$dbh->prepare($sqlAddField);                                          
                if($sth->execute())
                {         
                    $result = true;
                }   
            }                
        }
        catch (Exception $e){             
        }    
        return $result;
    }

    function sqlListServerIDByIP($ip,&$output)
    {
        $output = NULL;
        // $SQLQuery = "SELECT idVDServer FROM tbvswitchset WHERE ip=:ip;";
        $SQLQuery="SELECT t2.idVDServer FROM tbvswitchbondmappingset as t1 inner join tbbondset as t2 on t1.idBond=t2.idBond WHERE t1.ip=:ip";
        $sth = ConnectDB::$dbh->prepare($SQLQuery);
        $sth->bindValue(':ip', $ip, PDO::PARAM_STR);
        if($sth->execute())
        {                                
            while( $row = $sth->fetch() ) 
            {
                $output = $row['idVDServer'];
            }
        }
    }

    function sqlListExternalByPort($idVDServer,$portInternal,&$output)
    {
        $output = array();
        $sqlQuery = "SELECT ipExternal,portExternal FROM tbipportmappingset WHERE idVDServer=:idVDServer AND portInternal = :portInternal;";
        $sth = ConnectDB::$dbh->prepare($sqlQuery);
        $sth->bindValue(':idVDServer', $idVDServer, PDO::PARAM_INT);
        $sth->bindValue(':portInternal', $portInternal, PDO::PARAM_STR);
        if($sth->execute())
        {                                            
            while( $row = $sth->fetch() ) 
            {
                $output[] = array('IP'=>$row['ipExternal'],'Port'=>$row['portExternal']);
            }
        }
    }

    function transformDiskType($diskType)
    {
        $rtnDiskType = 'ide';
        switch ($diskType) {
            case 0:
                $rtnDiskType = 'ide';
                break;
            case 1:
                $rtnDiskType = 'scsi';
                break;
            case 2:
                $rtnDiskType = 'virtio';
                break;
            default:
                $rtnDiskType = 'ide';
                break;
        }
        return $rtnDiskType;
    }
    
    function sqlListVolumeByVolumeID($idVolume,&$outputVolume)
    {
        $outputVolume = NULL;
        $sqlList = "SELECT * FROM tbVolumeSet WHERE idVolume = :idVolume";
        try
        {                            
            $sth = connectDB::$dbh->prepare($sqlList); 
            $sth->bindValue(':idVolume', $idVolume, PDO::PARAM_STR);           
            if($sth->execute())
            {                                      
                while($row = $sth->fetch()) 
                {                                             
                    $outputVolume=array("VolumeID"=>$row['idVolume'],'VolumeName'=>$row['name'],'VolumeAlias'=>$row['nameAlias'],"Size"=>(int)$row['sizeVolume']);
                }          
            }                                       
        }
        catch (Exception $e){
        }                                
    }

    function sqlListVolumeByVolumeName($nameVolume,&$outputVolume)
    {
        $outputVolume = NULL;
        $sqlList = "SELECT * FROM tbVolumeSet WHERE name = :name";
        try
        {                            
            $sth = connectDB::$dbh->prepare($sqlList); 
            $sth->bindValue(':name', $nameVolume, PDO::PARAM_STR);           
            if($sth->execute())
            {                                      
                while($row = $sth->fetch()) 
                {                                             
                    $outputVolume=array("VolumeID"=>$row['idVolume'],'VolumeName'=>$row['name'],'VolumeAlias'=>$row['nameAlias'],"Size"=>(int)$row['sizeVolume']);
                }          
            }                                       
        }
        catch (Exception $e){
        }                                
    }

    function sqlListUserDisk($idVDisk,&$outputDisk)
    {
        $outputDisk = null;
        $SQLList = <<<SQL
            SELECT t1.idVDisk,t2.nameVDisk,t2.sizeVDisk,t2.configVDisk,t2.limitSSLyer,t3.idUser,t3.nameUser,t3.idDomain,t4.idVolume,t4.nameAlias,t4.name
            FROM tbuserdiskset as t1 
            inner join tbvdiskset as t2 on t1.idVDisk=t2.idVDisk 
            inner join tbVolumeSet as t4 on t2.idVolume=t4.idVolume
            inner join tbuserbaseset as t3 on t1.idUser=t3.idUser 
            WHERE t1.idVDisk=:idVDisk
SQL;
        try        
        {              
            $sth = connectDB::$dbh->prepare($SQLList);     
            $sth->bindValue(':idVDisk', $idVDisk, PDO::PARAM_INT);                         
            if($sth->execute())
            {       
                $outputDisk = array();                                 
                while( $row = $sth->fetch() ) 
                {                                
                    $outputDisk['DiskID'] = $row['idVDisk'];
                    $outputDisk['DiskName'] = $row['nameVDisk'];
                    $outputDisk['DiskSize'] = $row['sizeVDisk'];
                    $configDisk = $row['configVDisk'];
                    $configDisk = json_decode($configDisk,true);
                    $outputDisk['DiskType'] = $configDisk['DiskType'];
                    $outputDisk['DiskCache'] = $configDisk['DiskCache'];
                    $outputDisk['UserID'] = $row['idUser'];
                    $outputDisk['Username'] = $row['nameUser'];                    
                    $outputDisk['DomainID'] = $row['idDomain'];
                    $outputDisk['SSLayerMax'] = $row['limitSSLyer'];
                    $outputDisk['VolumeID'] = $row['idVolume'];
                    $outputDisk['VolumeName'] = $row['name'];
                    $outputDisk['VolumeAlias'] = $row['nameAlias'];
                }                     
            }                     
        }
        catch (Exception $e){                
            $outputDisk=null;
        }              
    }

    function sqlListUserDiskByName($nameVDisk,&$outputDisk)
    {
        $outputDisk = null;
        $SQLList = <<<SQL
            SELECT t1.idVDisk,t2.nameVDisk,t2.sizeVDisk,t2.configVDisk,t2.limitSSLyer,t3.idUser,t3.nameUser,t3.idDomain
            FROM tbuserdiskset as t1 
            inner join tbvdiskset as t2 on t1.idVDisk=t2.idVDisk 
            inner join tbuserbaseset as t3 on t1.idUser=t3.idUser 
            WHERE t2.nameVDisk=:nameVDisk
SQL;
        try        
        {              
            $sth = connectDB::$dbh->prepare($SQLList);     
            $sth->bindValue(':nameVDisk', $nameVDisk, PDO::PARAM_STR);                         
            if($sth->execute())
            {       
                $outputDisk = array();                                 
                while( $row = $sth->fetch() ) 
                {                                
                    $outputDisk['DiskID'] = $row['idVDisk'];
                    $outputDisk['DiskName'] = $row['nameVDisk'];
                    $outputDisk['DiskSize'] = $row['sizeVDisk'];
                    $configDisk = $row['configVDisk'];
                    $configDisk = json_decode($configDisk,true);
                    $outputDisk['DiskType'] = $configDisk['DiskType'];
                    $outputDisk['DiskCache'] = $configDisk['DiskCache'];
                    $outputDisk['UserID'] = $row['idUser'];
                    $outputDisk['Username'] = $row['nameUser'];
                    $outputDisk['DomainID'] = $row['idDomain'];
                    $outputDisk['SSLayerMax'] = $row['limitSSLyer'];
                }                     
            }                     
        }
        catch (Exception $e){                
            $outputDisk=null;
        }              
    }

    function sqlListAllUserDisk(&$output,$withKey=false)
    {
        $output = null;
        $sqlSelect = <<<SQL
                select t2.idVDisk,t2.nameVDisk,t3.idUser,t3.nameUser,t2.date_recovery 
                from tbuserdiskset as t1 
                inner join tbvdiskset as t2 
                on t1.idVDisk=t2.idVDisk 
                inner join tbuserbaseset as t3 
                on t1.idUser=t3.idUser; 
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($sqlSelect); 
            if($sth->execute())
            {                
                $output=array();               
                while( $row = $sth->fetch() ) 
                {                                                        
                    $recoveryTime = $row['date_recovery'] == '0000-00-00 00:00:00'  ? '' : $row['date_recovery'];  
                    if($withKey)                                      
                        $output[$row['nameVDisk']] = array('DiskID'=>(int)$row['idVDisk'],'UserID'=>(int)$row['idUser']
                                    ,'DiskName'=>$row['nameVDisk'],'Username'=>$row['nameUser'],'RecoveryTime'=>$recoveryTime);
                    else
                        $output[] = array('DiskID'=>(int)$row['idVDisk'],'UserID'=>(int)$row['idUser']
                                    ,'DiskName'=>$row['nameVDisk'],'Username'=>$row['nameUser'],'RecoveryTime'=>$recoveryTime);
                }
            }                           
        }
        catch (Exception $e){      
            $output = null;                       
        }       
    }

    function sqlCheckVDiskExit($idVDisk)
    {
        $exit = true;
        $sqlListVDisk = <<<SQL
            SELECT * FROM tbuserdiskset WHERE idVDisk =:idVDisk
SQL;
        try
        {                            
            $sth = connectDB::$dbh->prepare($sqlListVDisk);
            $sth->bindValue(':idVDisk', $idVDisk, PDO::PARAM_INT); 
            if($sth->execute())
            {        
                $exit = false;
                while( $row = $sth->fetch() ) 
                {
                    $exit = true;
                }                
            }                            
        }
        catch (Exception $e){                
        }   
        return $exit;            
    }


    function sqlCheckVDiskExitByName($nameVDisk,&$output)
    {
        $exit = true;
        $sqlListVDisk = <<<SQL
            SELECT t1.idVolume,t4.name,t1.idVDisk,t3.nameUser 
            FROM tbvdiskset as t1
            inner join tbuserdiskset as t2 
            on t1.idVDisk = t2.idVDisk
            inner join tbuserbaseset as t3
            on t2.idUser = t3.idUser
            inner join tbVolumeSet as t4
            on t1.idVolume=t4.idVolume
            WHERE t1.nameVDisk =:nameVDisk
SQL;
        try
        {                            
            $sth = connectDB::$dbh->prepare($sqlListVDisk);
            $sth->bindValue(':nameVDisk', $nameVDisk, PDO::PARAM_INT); 
            if($sth->execute())
            {        
                $exit = false;
                while( $row = $sth->fetch() ) 
                {
                    $exit = true;
                    $output['DiskID'] = $row['idVDisk'];
                    $output['Username'] = $row['nameUser'];
                    $output['VolumeID'] = $row['idVolume'];
                    $output['VolumeName'] = $row['name'];
                }                
            }                            
        }
        catch (Exception $e){                
        }               
        return $exit;
    }

    function sqlUpdateUserDisktoRestoreStatus($idVDisk,$status)
    {
        $result = false;
        $sqlUpdate = <<<SQL
            Update tbvdiskset SET stateVDisk=:stateVDisk WHERE idVDisk = :idVDisk
SQL;
        try
        {            
            $sth = connectDB::$dbh->prepare($sqlUpdate);
            $sth->bindValue(':idVDisk', $idVDisk, PDO::PARAM_INT); 
            $sth->bindValue(':stateVDisk', $status, PDO::PARAM_INT);           
            if($sth->execute()){                
                $result = true;            
            }
        }
        catch (Exception $e){    
            $result = false;
        }     
        // var_dump($result);
        return $result;
    }

    function sqlUpdateUserDiskSize($input,$status)
    {
        $result = false;
        $sqlUpdate = <<<SQL
            Update tbvdiskset SET sizeVDisk=:sizeVDisk,configVDisk=:configVDisk WHERE idVDisk = :idVDisk
SQL;
        try
        {            
            $sth = connectDB::$dbh->prepare($sqlUpdate);
            $sth->bindValue(':idVDisk', $input['DiskID'], PDO::PARAM_INT); 
            $sth->bindValue(':sizeVDisk', $input['DiskSize'], PDO::PARAM_INT);     
            $configVDisk = json_encode(array('DiskType'=>$input['DiskType'],'DiskCache'=>$input['DiskCache']));      
            $sth->bindValue(':configVDisk', $configVDisk, PDO::PARAM_STR);     
            if($sth->execute()){                
                $result = true;            
            }
        }
        catch (Exception $e){    
            $result = false;
        }     
        // var_dump($result);
        return $result;
    }

    function sqlUpdateUserDiskLimitSSLayer($input)
    {
        $result = false;
        $sqlUpdate = <<<SQL
            Update tbvdiskset SET limitSSLyer=:limitSSLyer WHERE idVDisk = :idVDisk
SQL;
        try
        {            
            $sth = connectDB::$dbh->prepare($sqlUpdate);
            $sth->bindValue(':idVDisk', $input['DiskID'], PDO::PARAM_INT); 
            $sth->bindValue(':limitSSLyer', $input['SSLayerMax'], PDO::PARAM_INT);             
            if($sth->execute()){                
                $result = true;            
            }
        }
        catch (Exception $e){    
            $result = false;
        }     
        // var_dump($result);
        return $result;
    }

    function sqlCheckUserHaveUserDisk($idUser,&$output)
    {
        $exit = true;
        $sqlCheckUserisk = <<<SQL
            SELECT t1.idVDisk,t2.limitSSLyer 
            FROM tbuserdiskset as t1 
            INNER JOIN tbvdiskset as t2
            on t1.idVDisk=t2.idVDisk
            WHERE idUser=:idUser
SQL;
        try
        {                            
            $sth = connectDB::$dbh->prepare($sqlCheckUserisk);
            $sth->bindValue(':idUser', $idUser, PDO::PARAM_INT); 
            if($sth->execute())
            {        
                $exit = false;
                while( $row = $sth->fetch() ) 
                {
                    $output['DiskID'] = $row['idVDisk'];
                    $output['SSLayerMax'] = $row['limitSSLyer'];
                    $exit = true;                    
                }                
            }                            
        }
        catch (Exception $e){                
        }               
        return $exit;
    }

    function changeModifyReturnCode($rtnCode)
    {
        switch ($rtnCode) {
            case '91':
                $rtnMsg = 'VD poweron';
                break;
            case '93':
                $rtnMsg = 'VD is in task';
                break;
            case '100':
                $rtnMsg = 'VD is not ready';
                break;
            case '102':
                $rtnMsg = 'VD is booting';
                break;
            case '103':
                $rtnMsg = 'VD is booting';
                break;
            case '122':
                $rtnMsg = 'Lock timeout';
                break;
            default:
                $rtnMsg = $rtnCode;
                break;
        }
        return $rtnMsg;
    }

     //轉換狀態值，相容小黑
    function changeVDState($oldState,$volumeIntegrity='online')
    {        
        $newState = 5;
        if($volumeIntegrity == 'offline'){
            if($oldState == 'poweron')
                $newState = 11;
            else
                $newState = 12;
        }
        else{
            switch ($oldState){           
                case 'poweroff':
                    $newState = 5;
                    break;           
                case 'poweron':
                    $newState = 1;
                    break;
                case 'suspend':
                    $newState = 3;
                    break;        
                case 'disable':            
                    $newState = -1;
                    break;
                case 'notReady':
                    $newState = -3;
                    break;
                case 'ssCrash':
                    $newState = -5;
                    break;
                case 'migrate':
                    $newState = 7;
                    break;
                case 'boot':
                    $newState = 10;
                    break;
                default :
                    $newState = 5;
                    break;
            }
        }
        return $newState;
    } 

    function getStartLastDate(&$startDate,&$lastDate)
    {
        $timezone = $this->getPHPTZ();
        date_default_timezone_set($timezone);
        $timeNow = date("Y-m-d H:i:s");
        $date = new DateTime($timeNow);
        $second=$date->format('s');        
        if($second>=0 && $second<20){
            $second='00';
        }elseif($second>=20 && $second<40){
            $second='20';
        }
        elseif($second>=40){
            $second='40';
        }        
        $lastTime=$date->format("Ymd H:i:$second");                
        $lastDate = new DateTime($lastTime);
        $startDate = new DateTime($lastTime);
        $startDate = $startDate->modify('-1 hour');        
        $startDate=$startDate->add(new DateInterval('PT20S'));        
    }

    function getDayStartLastDate(&$startDate,&$lastDate,$diffTime='')
    {
        $timezone = $this->getPHPTZ();
        date_default_timezone_set($timezone);
        $timeNow = date("Y-m-d H:i:s");
        $timeNow = new DateTime($timeNow);
        if(strlen($diffTime) > 0)
            $timeNow->modify($diffTime);
        $minute=(int)$timeNow->format('i');     
        $a=$minute/5;
        $b=$minute%5; 
        $a=5*(int)$a;
        $endTime=$timeNow->format("Y-m-d H:$a:00"); 
        $lastDate=new DateTime($endTime);
        $startDate=new DateTime($endTime);
        $startDate->modify('-1 day');
        $startDate->modify('+5 minute');  
    }
    
    function getWeekStartLastDate(&$startDate,&$lastDate,$diffTime='')
    {
        $timezone = $this->getPHPTZ();
        date_default_timezone_set($timezone);
        $timeNow = date("Y-m-d H:i:s");
        $timeNow = new DateTime($timeNow);
        if(strlen($diffTime) > 0)
            $timeNow->modify($diffTime);
        $minute=(int)$timeNow->format('i');     
        $a=$minute/30;
        $b=$minute%30;    
        $a=30*(int)$a;                
        $endTime=$timeNow->format("Y-m-d H:$a:00"); 
        $lastDate=new DateTime($endTime);
        $startDate=new DateTime($endTime);
        $startDate->modify('-7 day');
        $startDate->modify('+30 minute');  
    }

    function getMonthStartLastDate(&$startDate,&$lastDate,$diffTime='')
    {
        $timezone = $this->getPHPTZ();
        date_default_timezone_set($timezone);
        $timeNow = date("Y-m-d H:i:s");
        $timeNow = new DateTime($timeNow);  
        if(strlen($diffTime) > 0)
            $timeNow->modify($diffTime);      
        $hour=(int)$timeNow->format('H');            
        $a=$hour/2;
        $b=$hour%2;          
        $a=2*(int)$a;                
        $endTime=$timeNow->format("Y-m-d $a:00:00");
        $lastDate=new DateTime($endTime);
        $startDate=new DateTime($endTime);
        $startDate->modify('-30 day');
        $startDate->modify('+120 minute');  
    }

    function getYearStartLastDate(&$startDate,&$lastDate,$diffTime='')
    {
        $timezone = $this->getPHPTZ();
        date_default_timezone_set($timezone);
        $timeNow = date("Y-m-d H:i:s");
        $timeNow = new DateTime($timeNow);   
        if(strlen($diffTime) > 0)
            $timeNow->modify($diffTime);      
        $timeNow->modify("-1 day");                  
        $endTime=$timeNow->format("Y-m-d 23:59:59");
        $lastDate=new DateTime($endTime);
        $startDate=new DateTime($endTime);
        $startDate->modify('-364 day');        
    }

    function getPeriod(&$input)
    {
        switch ($input['Type']) {
            case 1:
                $this->getDayStartLastDate($startDate,$lastDate,'-20 minute');
                $input['PeriodCount']=288;          
                $input['Period']='PT5M';
                $input['TableName']=$input['Mode'].'Day';
                break;
            case 2:
                $this->getWeekStartLastDate($startDate,$lastDate,'-45 minute');
                $input['PeriodCount']=336;          
                $input['Period']='PT30M';
                $input['TableName']=$input['Mode'].'Week';
                break;
            case 3:
                $this->getMonthStartLastDate($startDate,$lastDate,'-135 minute');
                $input['PeriodCount']=360;          
                $input['Period']='PT120M';
                $input['TableName']=$input['Mode'].'Month';
                break;
            case 4:
                $this->getYearStartLastDate($startDate,$lastDate,'-15 minute');
                $input['PeriodCount']=365;
                $input['Period']='PT1440M';
                $input['TableName']=$input['Mode'].'Year';
                break;

        }
        $input['StartDate']=$startDate;
        $input['LastDate']=$lastDate;
    }

    function validateDate($date, $format = 'Y-m-d')
    {
        $d = DateTime::createFromFormat($format, $date);
        // The Y ( 4 digits year ) returns TRUE for any integer with any number of digits so changing the comparison from == to === fixes the issue.
        return $d && $d->format($format) === $date;
    }

    function changeRDPAddress($input,&$address)
    {
        $address = array();        
        foreach ($input['AcroGatewayConfig'] as $value) {
            if(is_array($value) && isset($value['ExternalIPSetting']) && isset($value['ForwardPortSetting'])){                
                if(strlen($value['ForwardPortSetting']['IPStart']) > 0 && strlen($value['ForwardPortSetting']['IPStartPort']) > 0){
                    $ipArr = explode('.', $value['ForwardPortSetting']['IPStart']);
                    $regIP = '/^'.$ipArr[0].'.'.$ipArr[1].'.'.$ipArr[2].'.[0-9]+$/';                    
                    if (preg_match($regIP, $input['RDPIP'])) {
                        $rdpIPArr = explode('.', $input['RDPIP']);
                        $rdpIPLast = (int)$rdpIPArr[3];
                        $startIPLast = (int)$ipArr[3];
                        $endIPLast=$startIPLast + $value['ForwardPortSetting']['NumPort'] - 1;                          
                        if($rdpIPLast >= $startIPLast &&  $rdpIPLast <= $endIPLast){
                            $diffNum = $rdpIPLast - $startIPLast;
                            $rdpPort = $value['ForwardPortSetting']['IPStartPort'] + $diffNum;
                            if(isset($value['ExternalIPSetting']['IP'])){                                
                                $address[] = array('IP'=>$value['ExternalIPSetting']['IP'],'Port'=>$rdpPort);
                                if(strlen($value['ForwardPortSetting']['ExternalIP']) > 0 && strlen($value['ForwardPortSetting']['ExternalStartPort']) > 0){
                                    $extRDPPort = $value['ForwardPortSetting']['ExternalStartPort'] + $diffNum;
                                    $address[] = array('IP'=>$value['ForwardPortSetting']['ExternalIP'],'Port'=>$extRDPPort);
                                }
                            }
                        }                    
                    }
                }
            }
        }
    }

    function sqlListAllGatewayJsonConfig(&$output)
    {
        $sqlList = <<<SQL
            SELECT jsonConfigService from tbvdimageserviceset WHERE typeService = 1
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($sqlList);                          
            if($sth->execute())
            {                    
                $output=array();                           
                while( $row = $sth->fetch() ) 
                {        
                    $output[] = json_decode($row['jsonConfigService'],true);
                }                                       
                $result = true;
            }                        
        }
        catch (Exception $e){    
            $result = false;
        } 
    }

    function sqlListAllVswitchsWithBrNameKey(&$output,&$outputIDWithName)
    {
        $output = null;
        $sqlList = <<<SQL
            SELECT * FROM tbvswitchset;
SQL;
       
        try
        {                                    
            $sth = connectDB::$dbh->prepare($sqlList);
            $sth->bindValue(':idVD', $id, PDO::PARAM_STR); 
            if($sth->execute())
            {    
                $output = array();
                $outputIDWithName = array();            
                while( $row = $sth->fetch() ) 
                {           
                    $key = 'br'.$row['idBridge'];
                    if($row['idVlan'] != 0)
                        $key .= '.'.$row['idVlan'];      
                    $vswitchID = (int)$row['idVSwitch'];
                    $output[$key] = array('VswitchID'=>$vswitchID,'Name'=>$row['nameAlias']);   
                    $outputIDWithName[$vswitchID] = $row['nameAlias'];
                }
            }                  
        }
        catch (Exception $e){                
        }         
        if(is_null($output))
            return false;
        return true;
    }

    function sqlListAllVswitchsNameWithBrNameKey(&$output)
    {
        $output = null;
        $sqlList = <<<SQL
            SELECT * FROM tbvswitchset;
SQL;
       
        try
        {                                    
            $sth = connectDB::$dbh->prepare($sqlList);
            $sth->bindValue(':idVD', $id, PDO::PARAM_STR); 
            if($sth->execute())
            {    
                $output = array();            
                while( $row = $sth->fetch() ) 
                {           
                    $key = 'br'.$row['idBridge'];
                    if($row['idVlan'] != 0)
                        $key .= '.'.$row['idVlan'];      
                    $output[$key] = $row['nameAlias'];   
                }
            }                  
        }
        catch (Exception $e){                
        }         
        if(is_null($output))
            return false;
        return true;
    }

    function debug($msg)
    {
        // var_dump(DEBUG);
        if(SANDEBUG){
            $fp = fopen('/mnt/tmpfs/cephcmd/cmd.txt', 'a');
            $gmt = $this->getTZ($timezone);        
            date_default_timezone_set($timezone);     
            $result = print_r($msg,true);
            // var_dump($result);
            fwrite($fp, date("Y-m-d H:i:s").' '.$result.' '.$_SERVER['REMOTE_ADDR'].PHP_EOL);
            fclose($fp);
        }  
    }
}

