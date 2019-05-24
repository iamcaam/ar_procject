<?php
class BackupAction extends BaseAPI
{
	private $cmdBkLunBrowse = CmdRoot."ip bk_lun_browse ";
	private $cmdBkLunLogin = CmdRoot."ip bk_lun_login ";
	private $cmdBkLunLogout = CmdRoot."ip bk_lun_logout ";
	private $cmdBkLunFormat = CmdRoot."ip bk_lun_format ";
	private $cmdBkLunModifyUUID = CmdRoot."ip bk_lun_modify_uuid ";
	private $cmdBkLunList = CmdRoot."ip bk_lun_list ";
    private $cmdBkVDListDiskSizeAll = CmdRoot."ip bk_vd_list_diskSize_all ";
    private $cmdBkTaskVDBackup = "sudo /var/www/html/script/cp_util.sh bk_task_vd_backup ";
    private $cmdBkTaskVDRestoreSame = CmdRoot."ip bk_task_vd_restore_same ";
    private $cmdBkTaskVDRestoreNew = CmdRoot."ip bk_task_vd_restore_new ";
    private $cmdBkLUNVDDel = CmdRoot."ip bk_lun_vd_del ";
    private $cmdBkVDDiskList = CmdRoot."ip bk_vd_disk_list ";
    private $cmdBkLUNBrowseSamba = CmdRoot."ip bk_lun_browse_samba ";
    private $cmdBkLUNLoginSamba = CmdRoot."ip bk_lun_login_samba ";
    private $cmdBkLUNLogoutSamba = CmdRoot."ip bk_lun_logout_samba ";
    private $cmdBkLUNClearSamba = CmdRoot."ip bk_lun_clear_samba ";
    private $cmdBkLUNModifyUUIDSamba = CmdRoot."ip bk_lun_modify_uuid_samba ";

    function  __construct()
    {    
        set_time_limit(0);         
    }

    function logBackupAppendErr($code)
    {
        $appendErr = null;
        switch ($code) {           
            case '0C300401':
            case '0C300402':
                $appendErr = '';
                break;
        }
        return $appendErr;
    }  

    function logRestoreAppendErr($code)
    {
        $appendErr = null;
        switch ($code) {           
            case '0C300201':
                $appendErr = '(Failed to create user)';
                break;
            case '0C300202':
                $appendErr = '(VD\'s name is duplicate)';
                break;
            case '0C300203':
                $appendErr = '(Failed to insert vd base db)';
                break;
            case '0C300204':
                $appendErr = '(Failed to insert original vd db)';
                break;
            case '0C300205':
                $appendErr = '(Failed to update default vd)';
                break;
            case '0C300206':
                $appendErr = '(Failed to insert vd info db)';
                break;
            case '0C300208':
                $appendErr = '(Failed to update vd status)';
                break;
        }
        return $appendErr;
    }  

	function lunBrowse($input,&$output)
	{		
        set_time_limit(65);	
        if(!isset($input['Type']))
            $input['Type'] = 1;	
        if(isset($input['CHAP']) && $input['CHAP'] == false){
            $input['Username'] = '';
            $input['Password'] = '';
        }            
        switch($input['Type']){
            case 1:
		        if($this->lunBrowseiSCSIShell($input,$output))
		  	        return CPAPIStatus::LUNBrowseSuccess;
                break;
            case 2:
                if($this->lunBrowseSMBShell($input,$output))
                    return CPAPIStatus::LUNBrowseSuccess;
                break;
		}
		return CPAPIStatus::LUNBrowseFail;
	}

	function lunBrowseiSCSIShell($input,&$output)
    {
    	$result = true;
        if(!is_null($input['ConnectIP'])){      
            $cmd = $this->cmdBkLunBrowse;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.$input['Username'].'" ';  
            $cmd .= '"'.$input['Password'].'" ';
            $cmd .= '"'.$input['IP'].'" ';  
            $cmd .= '3260 ';
            $cmd .= '"'.$input['TargetName'].'" ';                    
            $output = array();
            $output['Desc'] = '';    
            // var_dump($cmd);                        
            exec($cmd,$outputArr,$rtn);
            // var_dump($rtn);
            if($rtn == 0){                    
                $outputInfo = json_decode($outputArr[0],true);               
                if(count($outputInfo['bk']) > 0)
                {
                	if($outputInfo['curBkVer'] != $outputInfo['bk'][0]['lunBkVer']){
                		$output = array('State'=>2,'BkUUID'=>$outputInfo['bk'][0]['lunBkUuid']);	                		
                	}
                	else if($outputInfo['cephUuid'] != $outputInfo['bk'][0]['lunBkUuid']){
                		$output = array('State'=>3,'BkUUID'=>$outputInfo['bk'][0]['lunBkUuid']);
                		$output['Desc'] = base64_decode($outputInfo['description']);
                	}
                	else if($outputInfo['cephUuid'] == $outputInfo['bk'][0]['lunBkUuid']){
                		$output = array('State'=>4,'BkUUID'=>$outputInfo['bk'][0]['lunBkUuid']);
                        $output['Desc'] = base64_decode($outputInfo['description']);
                	}
                    else{
                        $output = array('State'=>5);
                    }
                }
                else
                	$output = array('State'=>1);
            }           
            elseif ($rtn == 30) {
            	$output = array('State'=>0);
            }          
            else {
            	$result = false;
            }  
        }                  
        return $result;       
    }

    function lunBrowseSMBShell($input,&$output)
    {
        $result = true;
        if(!is_null($input['ConnectIP'])){                  
            $cmd = $this->cmdBkLUNBrowseSamba;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.$input['Username'].'" ';  
            $cmd .= '"'.$input['Password'].'" ';                   
            $cmd .= '"'.$input['MountPath'].'" ';
            $cmd .= '"'.$input['Port'].'" ';                                 
            $output = array();
            $output['Desc'] = '';    
            // var_dump($cmd);                        
            exec($cmd,$outputArr,$rtn); 
            // var_dump($rtn);           
            if($rtn == 0){                    
                $outputInfo = json_decode($outputArr[0],true);     
                // var_dump($outputInfo);                          
                if(count($outputInfo['bk']) > 0)
                {
                    $haveSameUUID = false;
                    foreach ($outputInfo['bk'] as $value) {  
                        if($outputInfo['cephUuid'] == $value['lunBkUuid']){
                            $haveSameUUID = true;
                            if($outputInfo['curBkVer'] != $value['lunBkVer']){
                                $output = array('State'=>2,'BkUUID'=>$value['lunBkUuid']);                                   
                            }
                            else{                
                                // var_dump($value);                
                                $output = array('State'=>4,'BkUUID'=>$value['lunBkUuid']);
                                $output['Desc'] = base64_decode($outputInfo['description']);                                
                            }
                        }
                    }           
                    // var_dump($haveSameUUID);         
                    if(!$haveSameUUID)                        
                        $output = array('State'=>0);
                }
                else
                    $output = array('State'=>0);
            }           
            elseif ($rtn == 30) {
                $output = array('State'=>0);
            }          
            else {
                $result = false;
            }  
        }                  
        return $result;       
    }

    function setBackupMapping($input,&$output)
    {        
    	if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }       	
        switch ($input['Type']) {
        	case 1:
        		return $this->setiSCSIMapping1($input,$output);
        		break;  
            case 2:
                return $this->setSMBMapping($input,$output);
                break;      	
        	default:
        		return CPAPIStatus::SetBackupMappingFail;
        		break;
        }
    }

    function setiSCSIMapping1($input,&$output)
    {
        set_time_limit(0);
        $this->iscsiLogoutShell($input);
        if($this->iscsiLoginShell($input,$outputInfo)){         
            connectDB::$dbh->beginTransaction();
            $chap = true;
            if(strlen($input['Username']) == 0){
                $chap = false;          
                $input['Password'] = '';
            }     
            switch ($input['Action']) {
                case 0:         
                    if(!$this->lunFormatShell($input)){
                        goto failRollback;
                    }       
                    break;
                case 1:        
                    $outputLun = array();
                    if(!$this->lunListShell($input,$outputLun))
                        goto failRollback;            
                    if(!(count($outputLun['bk']) > 0 && isset($outputLun['bk'][0]['lunBkUuid']) && $outputLun['curBkVer'] == $outputLun['bk'][0]['lunBkVer']))
                        goto failRollback;
                    $input['BkUUID'] = $outputLun['bk'][0]['lunBkUuid'];
                    if(!$this->lunModifyUUUIDShell($input)){
                        goto failRollback;
                    }                       
                    break;
                default:
                    goto failRollback;
                    break;
            }      
            $outputLun = array();
            if(!$this->lunList($input,$outputLun))
                goto failRollback;
            $input['ConfigSpace'] = array('CHAP'=>$chap,'TargetName'=>$input['TargetName'],'Username'=>$input['Username'],'Password'=>$input['Password'],'Desc'=>$input['Desc'],'BkUUID'=>$outputLun['BkUUID'],'IP'=>$input['IP']);     
            if (!$this->sqlDeleteBackupSpace()) {    
                goto failRollback;        
            }                   
            if (!$this->sqlInserttbbackupspaceset($input)) {    
                goto failRollback;        
            }            
            $backupID = connectDB::$dbh->lastInsertId();
            $output=$outputLun;
            $output['ID'] = $backupID;
            $output['Username'] = $input['Username'];
            $output['Password'] = $input['Password'];
            $output['TargetName'] = $input['TargetName'];
            $output['IP'] = $input['IP'];
            $output['Desc'] = $input['Desc'];
            connectDB::$dbh->commit();            
            return CPAPIStatus::SetBackupMappingSuccess;  
        }
        else{
            goto fail;        
        }
        failRollback:
            connectDB::$dbh->rollBack();
            goto fail;            
        fail:            
            $this->iscsiLogoutShell($input);
            return CPAPIStatus::SetBackupMappingFail;
    }    

    function setSMBMapping($input,&$output)
    {
        $this->smbLogoutShell($input);
        if($this->smbLoginShell($input,$outputInfo)){         
            connectDB::$dbh->beginTransaction();
            $chap = true;
            if(strlen($input['Username']) == 0){
                $chap = false;          
                $input['Password'] = '';
            }     
            switch ($input['Action']) {
                case 0:         
                    if(!$this->lunClearSambaShell($input)){
                        goto failRollback;
                    }       
                    break;
                case 1:        
                    $outputLun = array();
                    if(!$this->lunListShell($input,$outputLun))
                        goto failRollback;                    
                    if(count($outputLun['bk']) == 0){
                        goto failRollback;
                    }            
                    else{
                        $haveSameVersion = false;
                        foreach ($outputLun['bk'] as $value) {                            
                            if($outputLun['cephUuid'] == $value['lunBkUuid']){
                                $input['BkUUID'] = $value['lunBkUuid'];
                                if($outputLun['curBkVer'] == $value['lunBkVer']){
                                    $haveSameVersion = true;   
                                }                                
                            }
                        }                                  
                        if(!$haveSameVersion)
                            goto failRollback;
                    }                   
                    if(!$this->lunModifySambaUUUIDShell($input)){
                        goto failRollback;
                    }                       
                    break;
                default:
                    goto failRollback;
                    break;
            }      
            $outputLun = array();
            if(!$this->lunList($input,$outputLun))
                goto failRollback;
            // var_dump($input);
            $input['ConfigSpace'] = array('MountPath'=>$input['MountPath'],'Username'=>$input['Username'],'Password'=>$input['Password'],'Desc'=>$input['Desc'],'BkUUID'=>$outputLun['cephUuid'],'Port'=>$input['Port']);     
            if (!$this->sqlDeleteBackupSpace()) {    
                goto failRollback;        
            }                   
            if (!$this->sqlInserttbbackupspaceset($input)) {    
                goto failRollback;        
            }            
            $backupID = connectDB::$dbh->lastInsertId();
            $output=$outputLun;
            $output['ID'] = $backupID;
            $output['Username'] = $input['Username'];
            $output['Password'] = $input['Password'];
            $output['MountPath'] = $input['MountPath'];            
            $output['Desc'] = $input['Desc'];
            connectDB::$dbh->commit();            
            return CPAPIStatus::SetBackupMappingSuccess;  
        }
        else{
            goto fail;        
        }
        failRollback:
            connectDB::$dbh->rollBack();
            goto fail;            
        fail:            
            $this->smbLogoutShell($input);
            return CPAPIStatus::SetBackupMappingFail;
    }

    function lunFormat($input)
    {
        $this->sqlListBackupJobConfig($output);
        if($output['ID'] == 0)
            return CPAPIStatus::FormatBackupFail;
        $input['Desc'] = $output['Desc'];
        switch ($output['Type']) {
            case 1:                
                if(!$this->lunFormatShell($input)){
                    return CPAPIStatus::FormatBackupFail;
                }       
                break;
            case 2:
                if(!$this->lunClearSambaShell($input)){
                    return CPAPIStatus::FormatBackupFail;
                } 
                break;
            default:                
                break;
        }        
        return CPAPIStatus::FormatBackupSuccess;
    }

    function backupList($input,&$output)
    {
        $gmt = $this->getTZ($timezone);
        date_default_timezone_set($timezone);           
        $output = array('ID'=>0,'IP'=>'','Username'=>'','Password'=>'','TargetName'=>'','MountPath'=>'');
        $output['TotalCapacity'] = 0;
        $output['AvialCapacity'] = 0;
        $output['Online'] = false;
        $output['RestoreVDs']['Original'] = array();
        $output['Type'] = 0;
        if($this->connectDB()){           
            $this->sqlListBackupJobConfig($output);               
            if($output['ID'] != 0){
                if(!$this->lunList($input,$output)){                   
                    $output['TotalCapacity'] = 0;
                    $output['AvialCapacity'] = 0;
                    $output['Online'] = false;
                    // $output['RestoreVDs']['Original'] = array();
                }                
            }        
        }
    }

    function lunList($input,&$output)
    {
        if(!$this->lunListShell($input,$outputLun))
            return false;                
        // $this->sqlListAllVDImageBase($outputVD);
        // if(is_null($outputVD)){
        //     return false;
        // }        
        if(count($outputLun['bk']) == 0)
            return false;        
        $output['TotalCapacity'] = (int)$outputLun['capacity'];
        $output['AvialCapacity'] = (int)$outputLun['availCapacity'];        
        $output['Online'] = true;
        $output['BkUUID'] = $outputLun['cephUuid'];
        // $output['RestoreVDs']['Original'] = array();
        // foreach ($outputLun['bk'][0]['vd'] as $vd) {
        //     $backupVDID = $vd['vdUuid'];
        //     $backupVDName = $vd['vdAlias'];
        //     $sourceVDName = '';
        //     $sourceVDID = '';
        //     if(array_key_exists($backupVDID, $outputVD)){
        //         $sourceVDID = $outputVD[$backupVDID]['VDID'];
        //         $sourceVDName = $outputVD[$backupVDID]['VDName'];
        //     }
        //     $output['RestoreVDs']['Original'][] = array('SourceVDName'=>$sourceVDName,'SourceVDID'=>$sourceVDID,'BackupVDName'=>$backupVDName,'BackupVDID'=>$backupVDID,'BackupTime'=>date('Y-m-d H:i:s',$vd['bkTime']));
        // }
        // var_dump($output);
        return true;
    }    

    function listBackupVDWithSize($input,&$output)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }     
        $this->sqlListAllVDImageBase($outputVD);
        if(is_null($outputVD)){
            return CPAPIStatus::ListBackupVDWithSizeFail;  
        }            
        // var_dump($outputVD);
        // exit();            
        $this->backupList($input,$outputBackup);                
        if($outputBackup['ID'] == 0)
            return CPAPIStatus::ListBackupVDWithSizeFail;          
        $input['BkUUID'] = $outputBackup['BkUUID'];
        switch ($outputBackup['Type']) {
            case 1:
                if(!$this->listBackupVDWithSizeShell($input,$outputBackupVDs))
                return CPAPIStatus::ListBackupVDWithSizeFail;    
                break;
            case 2:
                if(!$this->listSMBBackupVDWithSizeShell($input,$outputBackupVDs))
                return CPAPIStatus::ListBackupVDWithSizeFail;   
                break; 
            default:
                return CPAPIStatus::ListBackupVDWithSizeFail;   
                break;
        }
         
        $output = array();
        $output['Original'] = array(); 
        foreach ($outputBackupVDs as $vd) {
            $backupVDID = $vd['vdUuid'];
            $backupVDName = $vd['vdAlias'];
            $sourceVDName = '';
            $sourceVDID = '';
            if(array_key_exists($backupVDID, $outputVD)){
                $sourceVDID = $outputVD[$backupVDID]['VDID'];
                $sourceVDName = $outputVD[$backupVDID]['VDName'];
            }        
            $state = $this->backupResultEnum[$vd['bkResult']];           
            $output['Original'][] = array('SourceVDName'=>$sourceVDName,'SourceVDID'=>$sourceVDID,'BackupVDName'=>$backupVDName,'BackupVDID'=>$backupVDID,'BackupTime'=>date('Y-m-d H:i:s',$vd['bkTime']),'TotalSize'=>(int)$vd['diskSize'],'State'=>$state);
        }   
        return CPAPIStatus::ListBackupVDWithSizeSuccess;
    }

    function listSMBBackupVDWithSizeShell($input,&$output)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBkVDListDiskSizeAll;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= $input['BkUUID'];                    
            exec($cmd,$outputArr,$rtn);                     
            if($rtn == 0){               
                $output = json_decode($outputArr[0],true);                               
                return true;      
            }            
        }                
        return false;
    }

    function listBackupVDWithSizeShell($input,&$output)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBkVDListDiskSizeAll;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= $input['BkUUID'];    
            // var_dump($cmd);         
            exec($cmd,$outputArr,$rtn);     
            // var_dump($rtn);                    
            if($rtn == 0){               
                $output = json_decode($outputArr[0],true);                               
                return true;      
            }            
        }                     
        return false;
    }

    function backupDelete($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }      
        $this->sqlListBackupJobConfig($outputBackup);        
        connectDB::$dbh->beginTransaction();
        switch ($outputBackup['Type']) {
            case 1:
                if(!$this->sqlDeleteBackupSpace())
                    goto failRollback;
                if(!$this->iscsiLogoutShell($input))
                    goto failRollback;
                break;
            case 2:
                if(!$this->sqlDeleteBackupSpace())
                    goto failRollback;
                if(!$this->smbLogoutShell($input))
                    goto failRollback;
                break;         
            default:
                goto failRollback;
                break;
        }
        
        connectDB::$dbh->commit();
        $rtn = CPAPIStatus::DeleteBackupMappingSuccess;
        goto end;
        failRollback:
            connectDB::$dbh->rollBack();
            $rtn = CPAPIStatus::DeleteBackupMappingFail;
            goto end;
        end:
            return $rtn;
    }

    function newClass()
    {
        $this->domainC = new DomainAction();
        $this->vdC = new VDAction();
    }

    function backup($input,$type = 'backup')
    {   
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }        
        $this->backupList($input,$outputBackup);
        if($outputBackup['ID'] == 0)
            return CPAPIStatus::BackupFail;       
        $this->newClass();
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);
        $input['DomainName'] = $outputDomain['Name'];
        if(!$this->vdC->sqlListVDByID($input['VDID'],$outputVD))
            return CPAPIStatus::BackupFail;
        if(!$this->vdC->sqlListVDInfoByID($input['VDID'],$outputVDInfo)){        
            return CPAPIStatus::BackupFail;
        }
        $input['VDName'] = $outputVD['Name'];
        $input['SSLayerMax'] = $outputVDInfo['SSLayerMax'];
        if(!isset($input['Desc']))
            $input['Desc'] = 'Auto : Backup';
        if(!$this->backupTaskShell($input,$type)){
            $logCode = '0C300401';
            $this->insertBackupLog($logCode,$input['VDName'],$input['DomainName'],$input['DomainID']);
            return CPAPIStatus::BackupFail;
        }
        $logCode = '0C100301';
        $this->insertBackupLog($logCode,$input['VDName'],$input['DomainName'],$input['DomainID']);
        return CPAPIStatus::BackupSuccess;
    }

    function backupTaskShell($input,$type)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBkTaskVDBackup;            
            $cmd .= '"'.base64_encode($type.'$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.base64_encode($input['Desc'])).'" ';
            $cmd .= '"'.$input['VDID'].'" ';           
            $cmd .= $input['SSLayerMax'].' ';
            $cmd .= '1 ';                         
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);
            // var_dump($rtn);            
            if($rtn == 0){                                    
                return true;      
            }            
        }                     
        return false;       
    }

    function processReporBackupData($input,&$output)
    {
        $metadata = base64_decode($input[0]);
        $errCode = $input[1];
        $creaeTime = date('Y-m-d H:i:s',$input[2]);                        
        $metadataArr = explode('$*', $metadata);          
        $type = $this->backupActionEnum[$metadataArr[0]];
        // var_dump($metadataArr);    
        $output = array();     
        $output['Type'] = $type;
        $output['CephID'] = $metadataArr[1];
        $output['VDID'] = $metadataArr[2];
        $output['VDName'] = $metadataArr[3];
        $output['DomainID'] = $metadataArr[4];
        $output['DomainName'] = $metadataArr[5];  
        $output['State'] = 5;
        $output['CreateTime'] = $creaeTime;    
        $output['LogicalLayer'] = $input[3];
        $output['LogicalParentLayer'] = $input[4];
        $output['isRootLayer'] = $input[4] == 'base.img' ? true : false;
        $output['RootLayer'] = $input[5]; 
        $output['Size'] = $input[6];     
        $output['LayerDesc'] = '';
        $output['LayerDate'] = $output['CreateTime'];
        $output['ConnectIP'] = $this->getConnectIP();
        $output['ErrCode'] = $errCode;    
         if(isset($metadataArr[6])){
            $output['Desc'] = base64_decode($metadataArr[6]);  
            $output['LayerDesc'] = base64_decode($metadataArr[6]);                
        }         
    }

    function reportBkTaskVDBackup($input)
    {
        $haveTask = false;
        $cancelTask = false;
        $rtnCode = 99;        
        $logCode = '0C300402';
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFinal;
        }      
        $this->processReporBackupData($input,$data);
        $snapC = new SnapshotAction();            
        if(!$snapC->sqlCheckVDExit($data['VDID'])){
            $rtnCode = 0;
            goto rtnFinal;
        }
        if($data['ErrCode'] == 0)
        {
                // var_dump($output);
            if(!$this->sqlGetUUID($outputUUID)){
                $rtnCode = 98;
                goto rtnFinal;
            }           
            $rootSame = true;
            if($data['isRootLayer']){                     
                $data['Layer'] = $outputUUID['UUID'];
                $data['UpperLayer'] = $data['Layer'];
            }
            else
            {                               
                if(!$snapC->sqlCheckVDExit($data['VDID'])){
                    $rtnCode = 0;
                    goto rtnFinal;
                }                
                $outputLayer = $snapC->sqlListSnapshotByLogicalLayerVDID($data['VDID'],$data['LogicalParentLayer']);                
                if(is_null($outputLayer)){
                    $vdC = new VDAction();
                    connectDB::$dbh->beginTransaction();
                    $rnt = $vdC->recreateVDSnapshot($data['VDID']);
                    if($rtn != 0){
                        connectDB::$dbh->rollBack();
                    $rtnCode = 1;
                    }
                    else{
                        $logCode = '0C100302';
                        connectDB::$dbh->commit();
                        $rtnCode = 0;
                    }                                    
                    goto rtnFinal;
                }                             
                $data['Layer'] = $outputUUID['UUID'];  
                $data['UpperLayer'] = $outputLayer['Layer'];  
                $outputRoot = $snapC->sqlListRootByidVD($data['VDID']);
                // var_dump($outputRoot);
                // var_dump($output);
                if(is_null($outputRoot)){
                    $rtnCode = 2;
                    goto rtnFinal;
                }                                  
                if($outputRoot['LogicalLayer'] != $data['RootLayer'])
                    $rootSame = false;             
            }
            connectDB::$dbh->beginTransaction();
            if(!$snapC->sqlInsertSanpshotLayer($data)){               
                $rtnCode = 3;              
                goto failRollback;
            }
            if(!$rootSame){
                if(!$snapC->deleteSnapshotToRoot($data['VDID'],$data['RootLayer'])){
                    $rtnCode = 4;
                    goto failRollback;
                }
            }           
            $rtnCode = 0;        
            connectDB::$dbh->commit();
            $logCode = '0C100302';
            goto rtnFinal;
        }
        else{
            if($data['ErrCode'] == 80 || $data['ErrCode'] == 81){
                $outputRoot = $snapC->sqlListRootByidVD($data['VDID']);               
                if(is_null($outputRoot)){
                    $rtnCode = 2;
                    goto rtnFinal;
                }                                  
                if($outputRoot['LogicalLayer'] != $data['RootLayer'])
                    $rootSame = false;                                
                if(!$rootSame){
                    connectDB::$dbh->beginTransaction();           
                    if(!$snapC->deleteSnapshotToRoot($data['VDID'],$data['RootLayer'])){
                        $rtnCode = 4;
                        goto failRollback;
                    }
                    connectDB::$dbh->commit();    
                }                       
            }
            else if($data['ErrCode'] == 210){
                $haveTask = true;
                $cancelTask = true;
                $logCode = '0C100303';
            }
            $rtnCode = 0;
            goto rtnFinal;
        }        
        failRollback:
            connectDB::$dbh->rollBack();
            goto rtnFinal;
        rtnFinal:            
            if($data['ErrCode'] == 0)
                $data['ErrCode'] = NULL;
            $this->insertBackupLog($logCode,$data['VDName'],$data['DomainName'],$data['DomainID'],$haveTask,$cancelTask,$data['ErrCode']);
            exit($rtnCode);
    }



    function insertBackupLog($code,$vdName,$domainName,$domainID,$haveTask=true,$cancelTask=false,$errCode=NULL)
    {
        $logType = 12;
        $logLevel = 3;
        $appendErr = $this->logBackupAppendErr($code);
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $taskStr = "";
        if($haveTask)
            $taskStr = " task";
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName); 
        $appendErrCode = '';
        if(isset($errCode)) 
            $appendErrCode = "($errCode)";
        if(is_null($appendErr)){
            if($cancelTask)
                $message = "Cancel Backup VD$appendVDDomain$taskStr";
            else
            $message = "Backup VD$appendVDDomain$taskStr Success";
        }
        else{
            $message = "Failed to backup VD$appendVDDomain$taskStr$appendErr$appendErrCode";
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function insertRestoreLog($code,$vdName,$domainName,$domainID,$haveTask=true,$errCode=NULL)
    {
        $logType = 12;
        $logLevel = 3;
        $appendErr = $this->logRestoreAppendErr($code);
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $taskStr = "";
        if($haveTask)
            $taskStr = " task";
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName); 
        $appendErrCode = '';
        if(isset($errCode)) 
            $appendErrCode = "($errCode)";
        if(is_null($appendErr)){
            $message = "Restore VD$appendVDDomain$taskStr Success";
        }
        else{
            $message = "Failed to restore VD$appendVDDomain$taskStr$appendErr$appendErrCode";
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function checkRestore($input,&$output)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        $this->backupList($input,$outputBackup);
        if($outputBackup['ID'] == 0)
            return CPAPIStatus::RestoreFail;  
        // var_dump($outputBackup);
        $input['BkUUID'] = $outputBackup['BkUUID'];
        $rtn = $this->listRestoreVDDiskShell($input,$outputBackupVDDisk);
        // var_dump($outputBackupVDDisk);
        if($rtn != 0)
            return CPAPIStatus::RestoreFail;
        $vdC = new VDAction();
        $input['VDID'] = $input['BackupVDID'];
        $rtn = $vdC->listVDShell($input,$outputSrcVD);
        // var_dump($outputSrcVD);
        if($rtn != 0)
            return CPAPIStatus::RestoreFail;
        if(!isset($outputSrcVD['disk']))
            $outputSrcVD['disk'] = array();
        $mapping = false;
        $totalSize = 0;        
        $diskMapping = array();
        foreach ($outputBackupVDDisk as $value) {
            $mapping = false;
            $totalSize += $value['allocSize'];
            foreach ($outputSrcVD['disk'] as $value1) {                
                if($value['diskName'] == $value1['diskName']){
                    $diskMapping[] = array('diskName'=>$value['diskName'],'diskVolumeId'=>$value1['diskVolumeId']);
                    $mapping = true;
                    break;
                }
            }
            if(!$mapping)
                break;
        }                
        $output = array("Mapping"=>$mapping,"TotalSize"=>$totalSize,"DiskMapping"=>$diskMapping);        
        return CPAPIStatus::RestoreSuccess;
    }

    function listRestoreVDDiskShell($input,&$output)
    {
        $rtn = -99;
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBkVDDiskList;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);        
            $cmd .= '"'.$input['BkUUID'].'" ';    
            $cmd .= '"'.$input['BackupVDID'].'" ';                        
            exec($cmd,$outputArr,$rtn);
            if($rtn == 0){                                    
                $output = json_decode($outputArr[0],true);
            }            
        }          
        return $rtn;   
    }

    function restore($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        $this->backupList($input,$outputBackup);
        if($outputBackup['ID'] == 0)
            return CPAPIStatus::RestoreFail;       
        $this->newClass();              
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);
        $input['DomainName'] = $outputDomain['Name'];
        if(isset($input['SourceVDID']) && strlen($input['SourceVDID']) != 0){
            if($input['SourceVDID'] != $input['BackupVDID'])
                return CPAPIStatus::RestoreFail;           
        }
        $input['BkUUID'] = $outputBackup['BkUUID'];        
        if($input['SourceVDID'] == $input['BackupVDID']){
            if(!$this->vdC->sqlListVDByID($input['SourceVDID'],$outputVD))
                return CPAPIStatus::RestoreFail;
            $input['VDID'] = $input['SourceVDID'];
            $input['VDName'] = $outputVD['Name'];
            if(!isset($input['RAIDID']))            
            {
                $this->checkRestore($input,$outputMapping);
                if(!$outputMapping['Mapping'])
                    return CPAPIStatus::RestoreFail;
                $input['DiskMapping'] = $outputMapping['DiskMapping'];
            }
            return $this->restoreSame($input);
        }
        else{            
            return $this->restoreNewVD($input);
        }
    }

    function restoreNewVD($input)
    {
        if(!isset($input['RAIDID']))
            $input['RAIDID'] = 1;
        $logCode = '';    
        $responsecode = $this->vdC->createUser($input,$outputUser);
        if($responsecode == CPAPIStatus::CreateUserSuccess || $responsecode == CPAPIStatus::Conflict){                
            $input['UserID'] = $outputUser['ID'];                
        }
        else{
            $logCode = '0C300201';
            goto fail;
        }
        if($this->vdC->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$output_vd)){
            $logCode = '0C300202';
            $rtn = CPAPIStatus::Conflict;
            goto end;
        }    
        connectDB::$dbh->beginTransaction();          
        $input['UUID'] = $input['BackupVDID'];
        $input['VDID'] = $input['BackupVDID'];
        $input['State'] = 30;        
        if(!$this->vdC->sqlInsertVDBase($input)){
            $logCode = '0C300203';  
            goto failRollback;     
        }
        $output_vd = null;
        $this->vdC->sqlListVDByID($input['VDID'], $output_vd);            
        if(is_null($output_vd)){
            $logCode = '0C300203'; 
            goto failRollback;      
        }        
        if(!$this->vdC->sqlInsertVDOrg($input)){
            $logCode = '0C300204';       
            goto failRollback;          
        }   
        if($input['Username'] != '' && !$this->vdC->sqlUpdateUserDefaultVD($input)){
            $logCode = '0C300205';  
            goto failRollback;     
        }
        if(!$this->vdC->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$output_vd)){
            $logCode = '0C300204';   
            goto failRollback;
        }
        $input['CPU']=-1;
        $input['CpuCt']=2;
        $input['RAM']=2048;
        $input['NIC_Info'] = '';
        $suspend = false;
        $input['Suspend']=$suspend;    
        $input['USBType']=3;    
        $input['USBRedirCt']=0;
        $cdrom = '';        
        $input['CDRom']=$cdrom;
        $sound = 1;
        $input['Sound']=$sound;
        $input['Desc']='';
        $input['SSLayerMax']=250;
        if(!$this->vdC->sqlInsertVDInfo($input)){
            $logCode = '0C300206'; 
            goto failRollback;
        }
        if(!$this->restoreNewShell($input)){
            $logCode = '0C300207'; 
            goto failRollback;
        }          
        $logCode = '0C100101';
        connectDB::$dbh->commit();  
        $rtn = CPAPIStatus::RestoreSuccess;
        goto end;
        fail:
            $rtn = CPAPIStatus::RestoreFail;
            goto end;
        failRollback:
            $rtn = CPAPIStatus::RestoreFail;
            connectDB::$dbh->rollBack();
            goto end;
        end:
            $this->insertRestoreLog($logCode,$input['VDName'],$input['DomainName'],$input['DomainID']);
            // var_dump($logCode);
            return $rtn;
    }


    function restoreNewShell($input)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBkTaskVDRestoreNew;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.base64_encode('restoreNew$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['BackupVDID'].'$*'.$input['BackupVDName']).'" ';                      
            $cmd .= '"'.$input['BkUUID'].'" ';    
            $cmd .= '"'.$input['VDID'].'" ';
            $cmd .= '"'.$input['VDName'].'" ';
            $cmd .= '"'.$input['VDID'].'" ';
            $cmd .= '"'.$input['RAIDID'].'" ';
            $cmd .= '"'.$input['RAIDID'].'" ';
            $cmd .= '"'.$input['RAIDID'].'" ';
            $cmd .= '"'.$input['RAIDID'].'" ';
            $cmd .= '"'.$input['RAIDID'].'" ';
            $cmd .= '"'.$input['RAIDID'].'" ';
            $cmd .= '"'.$input['RAIDID'].'" ';
            exec($cmd,$outputArr,$rtn);
            if($rtn == 0){                                    
                return true;      
            }            
        }                     
        return false;
    }

    function restoreSame($input)
    {
        $logCode = '';
        connectDB::$dbh->beginTransaction();
        if(!$this->vdC->sqlUpdateVDtoRestoreStatus($input['SourceVDID'],41)){
            $logCode = '0C300208'; 
            goto failRollback;
        }
        if(!$this->restoreSameShell($input)){
            $logCode = '0C300207'; 
            goto failRollback;
        }
        $logCode = '0C100101';
        connectDB::$dbh->commit();  
        $rtn = CPAPIStatus::RestoreSuccess;
        goto end;
        fail:
            $rtn = CPAPIStatus::RestoreFail;
            goto end;
        failRollback:
            $rtn = CPAPIStatus::RestoreFail;
            connectDB::$dbh->rollBack();
            goto end;
        end:        
            $this->insertRestoreLog($logCode,$input['VDName'],$input['DomainName'],$input['DomainID']);
            return $rtn;
    }

    function restoreSameShell($input)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBkTaskVDRestoreSame;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.base64_encode('restoreSame$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['BackupVDID'].'$*'.$input['BackupVDName']).'" ';
            $cmd .= '"'.$input['BkUUID'].'" ';    
            $cmd .= '"'.$input['VDID'].'" '; 
            $cmd .= '"'.$input['VDName'].'" '; 
            if(isset($input['RAIDID'])){
                $cmd .= '"'.$input['RAIDID'].'" '; 
                $cmd .= '"'.$input['RAIDID'].'" '; 
                $cmd .= '"'.$input['RAIDID'].'" '; 
                $cmd .= '"'.$input['RAIDID'].'" '; 
                $cmd .= '"'.$input['RAIDID'].'" '; 
                $cmd .= '"'.$input['RAIDID'].'" '; 
                $cmd .= '"'.$input['RAIDID'].'" '; 
            }
            else{
                // var_dump($input);
                $diskArr = ["hda","hdc","hdd","hde","hdf","hdg","hdh"];                
                foreach ($diskArr as $value) {
                    $mapping = false;
                    foreach ($input['DiskMapping'] as $value1) {
                        // var_dump($value);
                        // var_dump($value1);
                        if($value1['diskName'] == $value){
                            $cmd .= '"'.$value1['diskVolumeId'].'" '; 
                            $mapping = true;
                            break;
                        }
                    }
                    if(!$mapping){
                        $cmd .= '"" '; 
                    }
                }
            }
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);
            // var_dump($rtn);
            if($rtn == 0){                                    
                return true;      
            }            
        }                           
        return false;
    }

    function iscsiLoginShell($input)
    {
    	if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBkLunLogin;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.$input['Username'].'" ';  
            $cmd .= '"'.$input['Password'].'" ';
            $cmd .= '"'.$input['IP'].'" ';
            $cmd .= '3260 ';
            $cmd .= '"'.$input['TargetName'].'" ';                  
            exec($cmd,$outputArr,$rtn);
            if($rtn == 0){                                    
                return true;      
            }            
        }                     
        return false;       
    }

    function smbLoginShell($input)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBkLUNLoginSamba;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.$input['Username'].'" ';  
            $cmd .= '"'.$input['Password'].'" ';            
            $cmd .= '"'.$input['MountPath'].'" ';                  
            $cmd .= '"'.$input['Port'].'" ';
            exec($cmd,$outputArr,$rtn);
            if($rtn == 0){                                    
                return true;      
            }            
        }                     
        return false;       
    }    

    function iscsiLogoutShell($input)
    {
    	if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBkLunLogout;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            // var_dump($cmd);                
            exec($cmd,$outputArr,$rtn);
            if($rtn == 0){                                    
                return true;      
            }            
        }                     
        return false;       
    }

    function smbLogoutShell($input)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBkLUNLogoutSamba;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            // var_dump($cmd);                
            exec($cmd,$outputArr,$rtn);
            if($rtn == 0){                                    
                return true;      
            }            
        }                     
        return false;       
    }

    function lunFormatShell($input)
    {
    	if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBkLunFormat;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= base64_encode($input['Desc']).' ';                
            exec($cmd,$outputArr,$rtn);            
            if($rtn == 0){              
                return true;      
            }            
        }                     
        return false;
    }

    function lunClearSambaShell($input)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBkLUNClearSamba;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= base64_encode($input['Desc']).' ';                
            exec($cmd,$outputArr,$rtn);            
            if($rtn == 0){              
                return true;      
            }            
        }                     
        return false;
    }

    function lunModifyUUUIDShell($input)
    {
    	if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBkLunModifyUUID;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= $input['BkUUID'].' ';  
            $cmd .= base64_encode($input['Desc']).' ';               
            exec($cmd,$outputArr,$rtn);
            if($rtn == 0){                                    
                return true;      
            }            
        }                     
        return false;
    }

    function lunModifySambaUUUIDShell($input)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBkLUNModifyUUIDSamba;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            // $cmd .= $input['BkUUID'].' '; 
            $cmd .= base64_encode($input['Desc']).' ';   
            // var_dump($cmd);            
            exec($cmd,$outputArr,$rtn);
            // var_dump($rtn);
            if($rtn == 0){                                    
                return true;      
            }            
        }                     
        return false;
    }

    function lunListShell($input,&$output)
    {
    	if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBkLunList;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            exec($cmd,$outputArr,$rtn);           
            if($rtn == 0){               
            	$output = json_decode($outputArr[0],true);                               
                return true;      
            }            
        }                     
        return false;
    }

    function backupManagerDelete($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        $this->backupList($input,$outputBackup);
        if($outputBackup['ID'] == 0)
            return CPAPIStatus::DeleteBackupVDFail;  
        $input['BkUUID'] = $outputBackup['BkUUID'];
        if(!$this->bakupVDDeleteShell($input))
            return CPAPIStatus::DeleteBackupVDFail;  
        return CPAPIStatus::DeleteBackupVDSuccess;
    }

    function bakupVDDeleteShell($input)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBkLUNVDDel;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.$input['BkUUID'].'" ';    
            $cmd .= '"'.$input['BkVDID'].'" ';    
            // var_dump($cmd);        
            exec($cmd,$outputArr,$rtn);
            if($rtn == 0){                                    
                return true;      
            }            
        }          
        return false;        
    }

    function sqlInserttbbackupspaceset($input)
    {
    	$result = false;
        $SQLInsert = "INSERT tbbackupspaceset (nameSpace,typeSpace,configSpace,date_created,date_modified) Values (:nameSpace,:typeSpace,TO_BASE64(DES_ENCRYPT(:configSpace,'".DES_Key."')),now(),now())";
        try {        	            
            $configSpace = json_encode($input['ConfigSpace']);            
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':nameSpace', $input['Desc'], PDO::PARAM_STR);
            $sth->bindValue(':typeSpace', $input['Type'], PDO::PARAM_STR);
            $sth->bindValue(':configSpace',$configSpace,PDO::PARAM_STR);            
            if($sth->execute()){ 
            	if($sth->rowCount()== 1){            		
                	$result = true;          
            	}
            }
        }
        catch (Exception $e){                
        }        
        return $result;
    }

    function sqlListAllVDImageBase(&$output)
    {
        $output = null;
    	try{
	    	$SQLList = <<<SQL
	            SELECT idVD,nameVD FROM tbvdimagebaseset
SQL;
	        $sth = connectDB::$dbh->prepare($SQLList);	                 
	        if($sth->execute()){             
	        	$output = array();                      
	            while( $row = $sth->fetch() ) 
	            {                                 
	                $output[$row['idVD']] = array('VDName'=>$row['nameVD'],'VDID'=>$row['idVD']);
	            }                            
	        }
	    }
	    catch (Exception $e){   
	    	$output = null;             
        }  
    }    

    function sqlListBackupJobConfig(&$output)
    {        
        $sqlSelect = "SELECT idBackupSpace,typeSpace,DES_DECRYPT(FROM_BASE64(configSpace),'".DES_Key.
                "') as config from tbbackupspaceset LIMIT 1";
        try{
            $sth = connectDB::$dbh->prepare($sqlSelect);          
            if($sth->execute())
            {                                      
                while( $row = $sth->fetch() ) 
                {                                
                    // $output[] = array("VDID"=>$row['idVD'],"VDName"=>$row['nameVD']);      
                    $output = json_decode($row['config'],true);  
                    $output['ID'] = $row['idBackupSpace'];
                    $output['Type'] = (int)$row['typeSpace'];
                }
            }                           
        }
        catch (Exception $e){                             
        }     
    }

    function sqlDeleteBackupSpace()
    {
        $sqlDeleteAllScheduleWithBackupJob = "DELETE FROM tbschedulebaseset WHERE schedule_id IN (SELECT * FROM (SELECT t1.schedule_id FROM tbschedulebaseset as t1 inner JOIN tbjobbaseset as t2 on t1.schedule_id = t2.schedule_id INNER JOIN tbbackupjobset as t3 on t2.job_id = t3.job_id) AS p);";
        $sqlDelete = "DELETE FROM tbbackupspaceset WHERE idBackupSpace>0";
        try{
            $sth = connectDB::$dbh->prepare($sqlDeleteAllScheduleWithBackupJob); 
            if($sth->execute()){
                $sth = connectDB::$dbh->prepare($sqlDelete);          
                if($sth->execute())
                {                                      
                    return true;
                }               
            }            
        }
        catch (Exception $e){                             
        }     
        return false;
    }    

    function processBackupErrorCode($code){  
        $this->baseOutputResponse($code);
        switch ($code){            
            case CPAPIStatus::LUNBrowseFail:    
            case CPAPIStatus::SetBackupMappingFail:  
            case CPAPIStatus::DeleteBackupMappingFail:  
            case CPAPIStatus::BackupFail:    
            case CPAPIStatus::RestoreFail:   
            case CPAPIStatus::DeleteBackupVDFail:    
            case CPAPIStatus::ListBackupVDWithSizeFail:    
                http_response_code(400);
                break;               
        }        
    }
}