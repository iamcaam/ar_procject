<?php
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
    public $snap_action_enum = array ("take" => 101, "delete" => 102, "rollback" => 103, "view" => 104, "viewStop" => 105,"scheduleTake" => 106); 
    public $backupActionEnum = array ("backup" => 201,"restoreNew" => 202,"restoreSame" =>203,"scheduleBackup"=>204); 
    public $backupResultEnum = array ("ok" => 0,'fail'=>-1); 
    public $snap_state_enum = array ("wait"=>0,"doing"=>1,"ok"=>10,"fail"=>-1); 
    public $taskStateEnum = array ("wait"=>1,"doing"=>2,"ok"=>0,"fail"=>-1); 
    public $taskTypeEnum = array ("clone"=>0,"seed"=>1,"born"=>2,"create"=>3,"addDisk"=>10,"deleteDisk"=>11,"export"=>12,"import"=>13,"reseed"=>14,"distribute"=>15,"recovery"=>16,"usertoorg"=>17,"expandDisk"=>18,"moveDisk"=>19,"moveUserProfile"=>20,'renew'=>22); 
    protected $getTZCmd = 'sudo /var/www/html/date.sh get tz';
    public $upsBatteryStausEnum = array ("none"=>-1,"unknown"=>0,"normal"=>1,"low"=>2,'depleted'=>3,"discharging"=>4,"failure"=>5);
    private $cmdRaidusageList = CmdRoot."ip node_raidusage_list";
    public $isCeph;
    public $diskNumber;

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
        // $isCeph = false;
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
        var_dump($cbc);
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

    function getAllocatedSize($raidID)
    {
        $allocate = 0;
        if($this->connectDB()){
            $allocate = $this->sqlListAllDiskSize($raidID);
        }     
        // var_dump($allocate);
        return $allocate;
    }

    function sqlListAllDiskSize($raidID)
    {
        $allocate = 0;
        $SQLList = <<<SQL
            select SUM(sizeVDisk) as Allocate from tbvdiskset WHERE idRAID=:idRAID;
SQL;
        try
        {                                           
            $sth = connectDB::$dbh->prepare($SQLList);  
            $sth->bindValue(':idRAID', $raidID, PDO::PARAM_INT);                  
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
        $sqlAddField1 = <<<SQL
            alter table `tbvdimagebaseinfoset` add column `typeUsb` tinyint(3) default 2 not NULL;
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
            $result = false;
            if (count(connectDB::$dbh->query("SHOW fields from tbVDImageBaseInfoSet LIKE 'typeUSB'")->fetchAll())){
                $result = true;
            }   
            else{                           
                $sth = connectDB::$dbh->prepare($sqlAddField1);   
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

    function sqlListAddressofUser($input,&$output)
    {
        $SQLQuery = "SELECT t3.address FROM tbUserSet t1 inner join tbUsertbVDServer t2 inner join tbVDServerBaseSet t3 ON t1.idUser=t2.tbUser_idUser AND t2.tbVDServer_idVDServer=t3.idVDServer WHERE t1.nameUser=:user;";                                    
        $sth = ConnectDB::$dbh->prepare($SQLQuery);
        $sth->bindValue(':user', $input['AccountName'], PDO::PARAM_STR);
        $sth->execute(); 
        $output= $sth->fetchall();               
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
    
    function debug($msg)
    {
        // var_dump(DEBUG);
        if(DEBUG){
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

