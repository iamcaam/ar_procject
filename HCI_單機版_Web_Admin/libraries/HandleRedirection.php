<?php
//ini_set('display_errors', 'On');

class RedirectionAction extends BaseAPI
{ 
    function listUserInfo($input)
    {                  
        // var_dump($input);                 
        if(!$this->connectDB()){
            http_response_code (500);
            exit();
        }                   
        $userC = new UserAction();
        $vdC = new VDAction();                              
        $outputUser = null;          
        $userC->listUserInfoByNameDomainName($input['AccountName'], $input['DomainName'], $outputUser);        
        if(is_null($outputUser)){
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
        if($adAuth){
            $adC = new ADAction();
            $domainName = $ad['DomainName'];
            $accountName = $input['AccountName']."@".$domainName;        
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
            // var_dump($input['Password']);
            // var_dump($outputUser['Password']);
            if(strcmp($input['Password'],$outputUser['Password'])!==0){
                if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300D01','Riser'=>$$input['AccountName'],'Message'=>"User(Name:".$input['AccountName'].") Login fail"),$last_id)){
                    LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                }
                http_response_code (401);
                exit();
            }
        }
        if($outputUser['State'] == 1){
            http_response_code(403);
            exit();
        }        
        $input['UserID']=$outputUser['UserID'];      
        if($vdC->listVDofUser($input,$output) != CPAPIStatus::ListVDofUserSuccess){
            http_response_code (401);
            exit();
        }
        $vd_arr = array();					
        $userC->sqlListUserDefaultVD($outputUser['UserID'], $outputDefault);
        // var_dump($outputDefault);
        if(is_null($outputDefault)){
            http_response_code (404);
            exit();
        }
        $vd_servers = array();
        $vd_servers[]=array('Address'=>$_SERVER['SERVER_NAME'],'Port'=>$_SERVER['SERVER_PORT']);
        $haveDefault = false;
        foreach ($output['Org'] as $vd){              
            $isOnline = false;            
            if($vd['Online'] && $input['ClientID']!=$vd['ClientID'])
                $isOnline = true;
            $isDefault = false;
            if($vd['VDID'] == $outputDefault['DefaultVD']){
                $haveDefault = true;
                $isDefault =  true;
            }
            $vd_arr[] = array('VdId'=>$vd['VDID'],'VdName'=>$vd['Name']
                    ,'VdStatus'=>$vd['State'],'IsDefault'=>$isDefault
                    ,"IsVdOnline"=>$isOnline,'CreateTime'=>$vd['CreateTime']
                    ,'Desc'=>$vd['Desc'],'Suspend'=>$vd['Suspend']==0?false:true
                    ,'Monitor'=>$vd['Monitor'],'VdServers'=>$vd_servers
                    ,'IsSnapshotView'=>$vd['IsSnapshotView'],'IsVdOrg'=>$vd['IsVdOrg']
                    ,'VGACount'=>1,'QXL'=>true,'Protocol'=>1,'RDP'=>array(),'RDPFirst'=>0);
        }       
        
        foreach ($output['User'] as $vd){
            $isOnline = false;           
            if($vd['Online'] && $input['ClientID']!=$vd['ClientID'])
                $isOnline = true;
            $isDefault = false;
            if($vd['VDID'] == $outputDefault['DefaultVD']){
                $haveDefault = true;
                $isDefault =  true;
            }
            $vd_arr[] = array('VdId'=>$vd['VDID'],'VdName'=>$vd['Name']
                    ,'VdStatus'=>$vd['State'],'IsDefault'=>$isDefault
                    ,"IsVdOnline"=>$isOnline,'CreateTime'=>$vd['CreateTime']
                    ,'Desc'=>$vd['Desc'],'Suspend'=>$vd['Suspend']==0?false:true
                    ,'Monitor'=>$vd['Monitor'],'VdServers'=>$vd_servers
                    ,'IsSnapshotView'=>$vd['IsSnapshotView'],'IsVdOrg'=>$vd['IsVdOrg']
                    ,'VGACount'=>1,'QXL'=>true,'Protocol'=>1,'RDP'=>array(),'RDPFirst'=>0);
        }  
        if(!$haveDefault&&count($vd_arr) > 0){
            $this->sqlUpdateUserDefaultVD($outputUser['UserID'], $vd_arr[0]['VdId']);
            if(count($vd_arr[0]) > 0)
                $vd_arr[0]['isDefault'] = true;
        }
        // if(count($vd_arr) > 0){
        //     if(LogAction::createLog(array('Type'=>4,'Level'=>1,'Code'=>'04100E01','Riser'=>$input['AccountName'],'Message'=>"User(Name:".$input['AccountName'].") Login Success"),$last_id)){
        //         LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
        //     }        
        // }
        // else{
        //     if(LogAction::createLog(array('Type'=>4,'Level'=>2,'Code'=>'04200E02','Riser'=>$input['AccountName'],'Message'=>"User(Name:".$input['AccountName'].") Login Success(No VDs)"),$last_id)){
        //         LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
        //     } 
        // }        
        $this->output_json(array('UserId'=>$outputUser['UserID'],'UserVdImages'=>$vd_arr));
    } 
    
    
    
    function sqlUpdateUserDefaultVD($idUser,$idVD){
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
    
    function setVDOnline($input)
    {        
        // $gmt = $this->getTZ($timezone);
        // date_default_timezone_set($timezone);    
        // $fp = fopen('/mnt/tmpfs/a.txt', 'a');
        // fwrite($fp, 'Start'.date("Y-m-d H:i:s").PHP_EOL);         
        if(!$this->connectDB()){
            http_response_code (400);
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
        $outputDomain = null;
        $domainC = new DomainAction();
        $domainC->sqlListDomainByID($outputVD['DomainID'], $outputDomain);        
        $userC = new UserAction();
        $outputUser = null;
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
        // fwrite($fp, 'ModfiyPWD'.date("Y-m-d H:i:s").PHP_EOL);  
        $rtnModifyPWD = $vdC->modifyVDPasswdShell($data); 
        // var_dump($rtnModifyPWD);         
        if( $rtnModifyPWD !=0){
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
                    http_response_code(429);//VD Migration
                    exit(); 
                default:
                    http_response_code (401);
                    exit();
            }            
        }             
        // fwrite($fp, 'ModfiyPWD'.date("Y-m-d H:i:s").PHP_EOL);    
        // fclose($fp);            
        $vdC->poweronVD($data,$rtn,$outputUser['Name'],false,true);              
        // $fp = fopen('/mnt/tmpfs/a.txt', 'a');      
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
                    http_response_code(429);//VD Migration
                    exit(); 
                default:
                    http_response_code(503);
                    exit();
            }               
        }                             
        // fwrite($fp, 'poweronInfo'.date("Y-m-d H:i:s").PHP_EOL);        
        $rtn = $vdC->poweronInfoShell($data,$output);
        // fwrite($fp, 'poweronInfo'.date("Y-m-d H:i:s").PHP_EOL);     

        if($rtn == 0 && isset($output['port'])){
            if($output['port']==''){
                http_response_code(503);
                exit();
            }
            $vdC->updateVDClientID($input['VDID'], $input['ClientID']);
            $server_c = array();            
            $this->sqlAddtbIpPortMappingSetTable();            
            $this->sqlListExternalByPort($input['NodeID'],$output['port'],$outputExternal);
            foreach ($output['ownerIP'] as $ip)
            {
                $server_c[]=array('Address'=>$ip,'Port'=>(int)$output['port']);
            }                 
            foreach ($outputExternal as $ip)
            {                                                
                $server_c[]=array('Address'=>$ip['IP'],'Port'=>(int)$ip['Port']);                
            }    
            $this->output_json(array('SpiceServers'=>$server_c));
            // fwrite($fp, 'END'.date("Y-m-d H:i:s").PHP_EOL);        
            // fclose($fp);    
        }
        else{
            http_response_code(404);
            exit();
        }
            
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
