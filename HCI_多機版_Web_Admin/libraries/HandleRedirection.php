<?php
//ini_set('display_errors', 'On');

class RedirectionAction extends BaseAPI
{ 
    private $cmdListExternal = CmdRoot."ip remote_list_external ";  
    private $cmVDMigrateStatus = CmdRoot."ip vd_migrate_status ";

    function listUserInfo($input)
    {                          
        // $gmt = $this->getTZ($timezone);        
        // date_default_timezone_set($timezone);
        // $t = microtime(true);
        // $micro = sprintf("%06d",($t - floor($t)) * 1000000);
        // $d = new DateTime( date('Y-m-d H:i:s.'.$micro, $t) );

        // print $d->format("Y-m-d H:i:s.u"); // note at point on "u"

        // var_dump(date("Y-m-d H:i:s"));
        // var_dump($input); 
        if(!$this->connectDB()){
            http_response_code(500);
            exit();
        }                   
        $userC = new UserAction();
        $vdC = new VDAction();                              
        $outputUser = null;                  
        $userC->listUserInfoByNameDomainName($input['AccountName'], $input['DomainName'], $outputUser);        
        if(is_null($outputUser) || strcmp($input['AccountName'], $outputUser['Name']) != 0 || strlen($input['Password']) <=0){
            http_response_code (401);
            exit();
        }                       
        $outputDomain = null;
        $domainC = new DomainAction();
        $domainC->sqlListDomainByID($outputUser['DomainID'], $outputDomain);    
        $adAuth = false;
        if(!is_null($outputDomain['AD'])){
            $ad = base64_decode($outputDomain['AD']);
            $ad = json_decode($ad,true);
            if($ad['Enable'])
                $adAuth = true;
        }
        if($adAuth && $input['AccountName'] != 'acrosystem'){
            $adC = new ADAction();
            $domainName = $ad['DomainName'];
            $accountName = $input['AccountName']."@".$domainName; 
            // var_dump($input);       
            $result = $adC->adAuth($ad['IP'],$accountName,$input['Password']);
            if(!$result){
                if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300D01','Riser'=>$account_arr[0],'Message'=>"User(Name:".$input['AccountName'].") Login fail(AD Auth Fail)"),$lastID)){
                    LogAction::sqlInsertDomainLogRelation($lastID,$outputDomain['DomainID']);
                }
                http_response_code(401);
                exit();
            }            
            unlink("/mnt/tmpfs/ad/".$outputUser['UserID']);            
            file_put_contents("/mnt/tmpfs/ad/".$outputUser['UserID'],$input['Password']);
        }
        else{      
            if(strcmp($input['Password'],$outputUser['Password'])!==0){
                if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300D01','Riser'=>$$input['AccountName'],'Message'=>"User(Name:".$input['AccountName'].") Login fail"),$lastID)){
                    LogAction::sqlInsertDomainLogRelation($lastID,$outputDomain['DomainID']);
                }
                http_response_code(401);
                exit();
            }
        }
        // var_dump(date("Y-m-d H:i:s"));
        if($outputUser['State'] == -1){
            http_response_code(403);
            exit();
        }        
        $input['UserID']=$outputUser['UserID'];   
        $vd_servers[]=array('Address'=>$_SERVER['SERVER_NAME'],'Port'=>$_SERVER['SERVER_PORT']);
        // var_dump($vd_servers);
        $userC->sqlListUserDefaultVD($outputUser['UserID'], $outputDefault);
        if($vdC->listVDofUserForViewer($input,$output,$outputDefault, $vd_servers) != CPAPIStatus::ListVDofUserSuccess){
            http_response_code (401);
            exit();
        }
        // $t = microtime(true);
        // $micro = sprintf("%06d",($t - floor($t)) * 1000000);
        // $d = new DateTime( date('Y-m-d H:i:s.'.$micro, $t) );

        // print $d->format("Y-m-d H:i:s.u"); // note at point on "u"

        // var_dump(date("Y-m-d H:i:s"));
        $adDomain = '';
        if(isset($ad['DomainName']) && $adAuth)
            $adDomain = $ad['DomainName'];
        $this->output_json(array('UserId'=>$outputUser['UserID'],'UserVdImages'=>$output['UserVdImages'],'DiskName'=>$output['DiskName'],'ADAuth'=>$adAuth,'ADDomain'=>$adDomain));            
    } 
       
    function sqlUpdateUserDefaultVD($idUser,$idVD)
    {
        $result = false;
        $SQLUpdate = <<<SQL
            Update tbuserset SET idDefaultVD=:idDefaultVD WHERE idUser = :idUser
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($SQLUpdate);                        
            $sth->bindValue(':idDefaultVD', $idVD, PDO::PARAM_STR);           
            $sth->bindValue(':idUser', $idUser, PDO::PARAM_INT);  
            if($sth->execute())
            {                                
                $result = true;
            }                        
        }
        catch (Exception $e){                             
        }     
        return $result;
    }
    
    function listVDState($input,&$output)
    {
        if(!$this->connectDB()){
            http_response_code(400);
            exit();
        }       
        $this->listVDMigrateStateShell($input,$outputShell);
        // var_dump($outputShell);        
        $vd['VdStatus']=99;
        if(isset($outputShell['migrate'])){
            $vd['VdStatus'] = $outputShell['migrate'] == 'yes' ? 7 : 99;
        }
        $output = $vd;
    }

    function listVDMigrateStateShell($input,&$output)
    {
        $output = null;
        $cmd = $this->cmVDMigrateStatus;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);    
        $cmd .= '"'.$input['VDID'].'"';       
        exec($cmd,$outputVD,$rtn);            
        if($rtn == 0){
            // var_dump($outputVD);
            if(!is_null($outputVD))
                $output=json_decode($outputVD[0],true);            
        }
    }

    function setVDOnline($input)
    {        
        // $gmt = $this->getTZ($timezone);
        // date_default_timezone_set($timezone);       
        if(!$this->connectDB()){
            http_response_code(400);
            exit();
        }           
        $vdC = new VDAction();        
        $outputVD = null;                        
        $vdC->sqlListVDInfoByID($input['VDID'], $outputVD);
        if($outputVD == NULL){
            http_response_code (404);
            exit();
        }     
        $input['ConnectProtocol'] = 1;
        $vdC->sqlUpdateVDProtocol($input);
        if(!$outputVD['QXL']){
            http_response_code (451);
            exit();
        }            

        $outputVD = null; 
        if(!$vdC->listVDByidVD($input['VDID'], $outputVD)){
            http_response_code (404);
            exit();
        }                
        if(is_null($outputVD['OrgUserID']) && is_null($outputVD['UserUserID'])){
            http_response_code (404);
            exit();
        }            
        else{
            if(!is_null($outputVD['OrgUserID']))
                $userid = $outputVD['OrgUserID'];
            if(!is_null($outputVD['UserUserID']))
                $userid = $outputVD['UserUserID'];
        }
        $outputDomain = null;
        $domainC = new DomainAction();
        $domainC->sqlListDomainByID($outputVD['DomainID'], $outputDomain);           
        $userC = new UserAction();
        $outputUser = null;
        // var_dump($userid);
        // var_dump($outputVD);
        $userC->sqlListUserByIDAndidDomain($userid, $outputVD['DomainID'], $outputUser);
        if(is_null($outputUser)){
            http_response_code (401);
            exit();
        }
        if($outputUser['State'] == 1){
            http_response_code (403);
            exit();
        }
        $data['CephID'] = $input['CephID'];
        $data['DomainID'] = $outputVD['DomainID'];
        $data['VDID'] = $input['VDID'];
        $data['VDs'] = array();
        $data['VDs'][] = $input['VDID'];
        $data['ConnectIP'] = $input['ConnectIP'];
        $data['VDNumber']= $outputVD['VDNumber'];   
        $data['Password']=$outputUser['Password'];                 
        // var_dump(date("Y-m-d H:i:s"));
        if(!is_null($outputDomain['AD'])){
            $ad = base64_decode($outputDomain['AD']);                  
            $ad = json_decode($ad,true);               
            if($ad['Enable']){                
                $data['Password'] = file_get_contents("/mnt/tmpfs/ad/$userid");
            }
        }
        // var_dump($data);
        $rtnModifyPWD = $vdC->modifyVDPasswdShell($data);  
        // var_dump($rtnModifyPWD);              
        if( $rtnModifyPWD !=0 ){
            switch ($rtnModifyPWD) {
                case 95:     
                    http_response_code(412);//out of memory
                    exit();                          
                case '94':
                    http_response_code(403);//Disable
                    exit();                
                case '93':
                case '224':
                    http_response_code(417);//Disable
                    exit();                   
                case '92':
                    http_response_code(406);//Over Maximum Connections
                    exit();                                     
                case '102':
                    http_response_code(428);//VD Preparing Boot
                    exit();  
                case '103':
                    http_response_code(429);//VD Preparing Boot
                    exit(); 
                default:
                    http_response_code (401);
                    exit();
            }            
        }               
        // var_dump(date("Y-m-d H:i:s"));
        $vdC->poweronVD($data,$rtn,$outputUser['Name'],false,true);          
        if($rtn != 0 && $rtn != 91){
            switch ($rtn) {
                case 95:     
                    http_response_code(412);//out of memory
                    exit();                          
                case '94':
                    http_response_code(403);//Disable
                    exit();                
                case '93':
                    http_response_code(417);//Disable
                    exit();                   
                case '92':
                    http_response_code(406);//Over Maximum Connections
                    exit();     
                case '102':
                    http_response_code(428);//VD Preparing Boot
                    exit();  
                case '103':
                    http_response_code(429);//VD Preparing Boot
                    exit();                               
                default:
                    http_response_code(503);
                    exit();
            }               
        }                             
        $rtn = $vdC->poweronInfoShell($data,$output);
        if($rtn == 0 && isset($output['port'])){
            if($output['port']==''){
                http_response_code(503);
                exit();
            }
            $vdC->updateVDClientID($input['VDID'], $input['ClientID']);
            $server_c = array();
            // var_dump($output);            
            $outputID = NULL;
            if(is_null($outputID))
                $this->sqlListServerIDByIP($output['owner'],$outputID);
            if(!is_null($outputID)){
                $this->sqlAddtbIpPortMappingSetTable();
                $this->sqlListExternalByPort($outputID,$output['port'],$outputExternal);
            }
            foreach ($output['ownerIP'] as $ip)
            {                                                
                $server_c[]=array('Address'=>$ip,'Port'=>(int)$output['port']);                
            }                                          
            foreach ($outputExternal as $ip)
            {                                                
                $server_c[]=array('Address'=>$ip['IP'],'Port'=>(int)$ip['Port']);                
            }   
            if(LogAction::createLog(array('Type'=>4,'Level'=>1,'Code'=>'04100F01','Riser'=>$outputUser['Name'],'Message'=>"User(Name:".$outputUser['Name'].") Login VD(".$outputVD['Name'].") Success"),$last_id)){
                LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
            }
            $this->output_json(array('SpiceServers'=>$server_c));
        }
        else{
            http_response_code(404);
            exit();
        }
    }

    function setVDOnlineRDP($input)
    {               
        if(!$this->connectDB()){
            http_response_code(400);
            exit();
        }                        
        $vdC = new VDAction();     
        $userC = new UserAction();   
        $outputVD = null;      
        // var_dump($input);                  
        if(!$vdC->listVDByidVD($input['VDID'], $outputVD)){
            http_response_code(404);
            exit();
        }                   
        if(is_null($outputVD['OrgUserID']) && is_null($outputVD['UserUserID'])){
            http_response_code(404);
            exit();
        }            
        else{
            if(!is_null($outputVD['OrgUserID']))
                $userid = $outputVD['OrgUserID'];
            if(!is_null($outputVD['UserUserID']))
                $userid = $outputVD['UserUserID'];
        }        
        $vdC->sqlListVDInfoByID($input['VDID'],$outputVDInfo);
        // var_dump($outputVDInfo);
        if(is_null($outputVDInfo)){
            http_response_code(404);
            exit();
        }     
        $outputUser = null;
        $userC->sqlListUserByIDAndidDomain($userid, $outputVD['DomainID'], $outputUser);
        if(is_null($outputUser)){
            http_response_code(401);
            exit();
        }
        if($outputUser['State'] == 1){
            http_response_code(403);
            exit();
        }
        // if(isset($outputVDInfo['RDPPWDTime']) && $outputVDInfo['RDPPWDTime'] == 0){
        //     if(!$vdC->sqlUpdateVDRDPPWD(array('VDID'=>$input['VDID'],'Password'=>$outputUser['Password']))){
        //         http_response_code(400);
        //         exit();
        //     }
        // }
        $input['ConnectProtocol'] = 2;
        $vdC->sqlUpdateVDProtocol($input);
        $outputDomain = null;
        $domainC = new DomainAction();
        $domainC->sqlListDomainByID($outputVD['DomainID'], $outputDomain);           
        // $userC = new UserAction();        
        // var_dump($userid);
        // var_dump($outputVD);
        
        $data['CephID'] = $input['CephID'];
        $data['DomainID'] = $outputVD['DomainID'];
        $data['VDID'] = $input['VDID'];
        $data['VDs'] = array();
        $data['VDs'][] = $input['VDID'];
        $data['ConnectIP'] = $input['ConnectIP'];
        $data['VDNumber']= $outputVD['VDNumber'];   
        $data['Password']=$outputUser['Password'];            
        // var_dump(date("Y-m-d H:i:s"));
        $vdC->poweronVDRDP($data,$rtn,$outputUser['Name'],true);    
        // var_dump($rtn);      
        if($rtn != 0 && $rtn != 91){
            switch ($rtn) {
                case 95:     
                    http_response_code(412);//out of memory
                    exit();                          
                case '94':
                    http_response_code(403);//Disable
                    exit();                
                case '93':
                    http_response_code(417);//Disable
                    exit();                   
                case '92':
                    http_response_code(406);//Over Maximum Connections
                    exit();     
                case '102':
                    http_response_code(428);//VD Preparing Boot
                    exit();  
                case '103':
                    http_response_code(429);//VD Preparing Boot
                    exit();                      
                case '232':
                    http_response_code(431);//No VGA
                    exit();         
                default:
                    http_response_code(503);
                    exit();
            }               
        }       

        if(LogAction::createLog(array('Type'=>4,'Level'=>1,'Code'=>'04100F01','Riser'=>$outputUser['Name'],'Message'=>"User(Name:".$outputUser['Name'].") Login VD(".$outputVD['Name'].") Success"),$last_id)){
                LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
        }                              
    }

    function listVDRDPInfo($input,&$output)
    {        
        $gmt = $this->getTZ($timezone);        
        date_default_timezone_set($timezone);
        if(!$this->connectDB()){
            http_response_code(400);
            exit();
        }   
        $this->sqlListAllGatewayJsonConfig($outputJsonConfig);
        $input['AcroGatewayConfig'] = $outputJsonConfig;
        $vdC=new VDAction();
        $output = array();
        $output['Address'] = array(); 
        $output['RDPFirst'] = 1;       
        $sqlList = <<<SQL
            SELECT rdp,isPWDChange,isRDPFirst,rdpStage,rdpTimeStamp from tbVDImageBaseInfoSet WHERE idVD = :idVD
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($sqlList);              
            $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_STR);  
            if($sth->execute())
            {                                               
                while( $row = $sth->fetch() ) 
                {        
                    $output['Stage']=(int)$row['rdpStage'];
                    $output['RDPTimeStamp']=(int)$row['rdpTimeStamp'];
                    $output['RDPFirst'] = (int)$row['isRDPFirst'];
                    if(!$row['isPWDChange']){       
                        if(!is_null($row['rdp'])){
                            $content = json_decode($row['rdp'],true);  
                            foreach ($content['Address'] as $value) {                            
                                $output['Address'][] = array('IP'=>$value['IP'],'Port'=>$value['Port']);
                                $input['RDPIP'] = $value['IP'];                                
                                $this->changeRDPAddress($input,$outputAddress);                                
                                if(isset($outputAddress)){
                                    foreach ($outputAddress as $address) {
                                        $output['Address'][] = $address;
                                    }
                                    
                                }
                            }
                        }
                    }
                }                           
                $nowTimeStamp=time();
                if($output['Stage']!=0 && $output['Stage']!=5){
                    $diffTimeStamp = $nowTimeStamp - $output['RDPTimeStamp'];
                    if($diffTimeStamp > 60){
                        $stage = (int)$output['Stage'];
                        $output['Stage'] = 0-$stage;
                    }
                }            
                $result = true;
            }                        
        }
        catch (Exception $e){    
            $result = false;
        }     
        $result = false;
        $vds = array();
        $vds[] = array('VdId'=>$input['VDID']);
        $vdC->listVDSpecificShell($input,$vds,$outputVDShell); 
        $state = $this->changeVDState($outputVDShell[0]['vdState'],$outputVDShell[0]['volumeIntegrity']);        
        $output['State'] = $state;
        // $output['State'] = 7;
        $output['IsAssignedUserDisk'] = NULL;
        if($outputVDShell[0]['vdState'] == 'poweroff'){            
            $result = true;
        }                  
        else{
            $vdC->sqlListVDUserIDByVDID($input['VDID'],$outputID);                
            if(is_null($outputID['OrgUserID']) && is_null($outputID['UserUserID'])){
                http_response_code(400);
                exit();
            }               
            if(!is_null($outputID['OrgUserID']))
                $idUser = $outputID['OrgUserID'];
            if(!is_null($outputID['UserUserID']))
                $idUser = $outputID['UserUserID'];
            $vdC->sqlListVDIDOfUserDiskbyiduser($idUser,$outputVDIDofDisk);            
            // var_dump($outputVDIDofDisk);
            $output['IsAssignedUserDisk']=false;
            if(isset($outputVDIDofDisk) && $outputVDIDofDisk == $input['VDID'])
                $output['IsAssignedUserDisk']=true;
        }        
        return $result;
    }    

    function setVDDefault($input)
    {
        if(!$this->connectDB()){
            http_response_code(400);
            exit();
        }           
        $vdC = new VDAction();        
        $outputVD = null;                        
        if(!$vdC->listVDByidVD($input['VDID'], $outputVD)){
            http_response_code (404);
            exit();
        }                   
        if(is_null($outputVD['OrgUserID']) && is_null($outputVD['UserUserID'])){
            http_response_code (404);
            exit();
        }            
        else{
            if(!is_null($outputVD['OrgUserID']))
                $userid = $outputVD['OrgUserID'];
            if(!is_null($outputVD['UserUserID']))
                $userid = $outputVD['UserUserID'];
        }
        $this->sqlUpdateUserDefaultVD($userid, $input['VDID']);
    }

    function listExternalShell($ip,&$output)
    {
        $rtn = -2;
        if(!is_null($ip)){
            $cmd = $this->cmdListExternal;
            $cmd = str_replace ( 'ip' , $ip, $cmd);  
            // var_dump($cmd);
            exec($cmd,$output,$rtn);                                           
            if($rtn == 0){
                $output = json_decode($output[0],true);
                // var_dump($output);                        
            }
        }       
        return $rtn;
    }
    
    function vdAction($input)
    {
        if(!$this->connectDB()){
            http_response_code (400);
            exit();
        }     
        switch ($input['Action'])
        {
            case 0:                              
                $vd_c = new VDAction();
                $outputVD = null;                        
                if(!$vd_c->listVDByidVD($input['VDID'], $outputVD)){
                    http_response_code (404);
                    exit();
                }                
                $input['VDNumber'] = $outputVD['VDNumber'];              
                $vd_c->poweroffVDShell($input);
                break;
            default :
                http_response_code (404);
        }
    }
    
    function vdModify($input)
    {
        if(!$this->connectDB()){
            http_response_code (400);
            exit();
        }     
        $vd_c = new VDAction();
        if(!$vd_c->listVDByidVD($input['VDID'], $outputVD)){
            http_response_code (404);
            exit();
        }      
        if($outputVD['VGACount'] == 0){
            // var_dump($outputVD['VGACount']);
            if(!is_int($input['Monitor'])){         
                http_response_code(400);
                exit();
            }        
            if($input['Monitor'] != 1 && $input['Monitor'] != 2){            
                http_response_code(400);
                exit();
            }          
            $rtn = $vd_c->modifyVDVideoShell($input);
            if($rtn != 0)            
                http_response_code(400);
        }
    }

    function listVDIPort($input)
    {            
        if(!$this->connectDB()){
            http_response_code (400);
            exit();
        }    
        $this->sqlListAddressofUser($input, $output);       
        if(count($output) != 0)
        {        
            $IP = $output[0]['address'];           
            $curl_request = new CurlRequest("https://$IP/vdi/port","POST",  json_encode($input));
            $curl_request->execute();
            $curl_request->getResponse($response_code, $response_content);    
            if($response_code == 200)
                echo $response_content;
            else
            {
                http_response_code($response_code);
            }
        }
        else
            http_response_code(404);
    }       
    
    function sqlListAddressofUser($input,&$output)
    {
        $SQLQuery = "SELECT t3.address FROM tbUserSet t1 inner join tbUsertbVDServer t2 inner join tbVDServerBaseSet t3 ON t1.idUser=t2.tbUser_idUser AND t2.tbVDServer_idVDServer=t3.idVDServer WHERE t1.nameUser=:user;";                                    
        $sth = ConnectDB::$dbh->prepare($SQLQuery);
        $sth->bindValue(':user', $input['AccountName'], PDO::PARAM_STR);
        $sth->execute(); 
        $output= $sth->fetchall();               
    }        

    function listThinClientFirmwareVersion()
    {        
        $version_file = '/var/www/html/TestGtk.exe.config';       
        $version = '';
        $outputarr = array();
        $vSwitchC = new vSwitchAction();
        $vSwitchC->listVswitch(array('ConnectIP' =>'127.0.0.1'),$outputVswitch);
        $rtnip = $outputVswitch['Vswitchs'][0]['IP'];
        if(file_exists($version_file)){            
            $contents = file_get_contents($version_file);
            $dom = new SimpleXMLElement($contents);
            $adddom = $dom->xpath('//add');
            $tmpkey = '';
            $isFirmwareVer = false;            
            foreach ( $adddom as $key => $val ) { 
                $attr = $val->attributes();
                if((string)($attr['key'][0]) == 'FirmwareVer')
                    $version = (string)($attr['value'][0]);
            }    
        }
        else
            http_response_code(404);
        if($version == '')
            http_response_code(404);
        echo json_encode(array('Model'=>'3310','Version'=>$version,"UriNewFirmware"=>'https://'.$rtnip.'/TestGtk.tar.gz'));
    }

    function listThinClientJavaFirmwareVersion()
    {        
        $version_file = '/var/www/html/Version.json';       
        $version = '';
        $outputarr = array();
        $vSwitchC = new vSwitchAction();
        $vSwitchC->listVswitch(array('ConnectIP' =>'127.0.0.1'),$outputVswitch);
        if(isset($_SERVER['HTTP_HOST'])){
            $rtnip = $_SERVER['HTTP_HOST'];
        }
        else if(isset($_SERVER['SERVER_ADDR'])){
            $rtnip = $_SERVER['SERVER_ADDR'];
        }
        else
            $rtnip = $outputVswitch['Vswitchs'][0]['IP'];
        if(file_exists($version_file)){            
            $contents = file_get_contents($version_file);
            $versionJson = json_decode($contents,true);
            $version = $versionJson['Version'];
        }
        else
            http_response_code(404);
        if($version == '')
            http_response_code(404);
        echo json_encode(array('Model'=>'3310','Version'=>$version,"UriNewFirmware"=>'https://'.$rtnip.'/JavaViewer.tar.gz'));
    }
}
