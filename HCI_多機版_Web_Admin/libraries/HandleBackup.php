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
    // private $cmdBkTaskVDBackup = CmdRoot."ip bk_task_vd_backup ";
    private $cmdBkTaskVDBackup = "sudo /var/www/html/script/cp_util.sh bk_task_vd_backup ";    
    
    private $cmdBkLUNVDDel = CmdRoot."ip bk_lun_vd_del ";
    private $cmdBkVDDiskList = CmdRoot."ip bk_vd_disk_list ";
    private $cmdBkLUNBrowseSamba = CmdRoot."ip bk_lun_browse_samba ";
    private $cmdBkLUNLoginSamba = CmdRoot."ip bk_lun_login_samba ";
    private $cmdBkLUNLogoutSamba = CmdRoot."ip bk_lun_logout_samba ";
    private $cmdBkLUNClearSamba = CmdRoot."ip bk_lun_clear_samba ";
    private $cmdBkLUNModifyUUIDSamba = CmdRoot."ip bk_lun_modify_uuid_samba ";
        
    private $cmdBkTaskErrTask = CmdRoot."ip bk_task_err_task ";
    
    private $cmdBKUPListDiskSize = CmdRoot.'ip bk_up_list_diskSize ';
    private $cmdBKLUNUPDel = CmdRoot.'ip bk_lun_up_del ';
    
    private $backupShell;
    function  __construct(IBackupShell $backupShell=NULL)
    {    
        set_time_limit(0);    
        if(is_null($backupShell)){
            $this->backupShell = new BackupShell();
        }
        else{
            $this->backupShell = $backupShell;   
        }     
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
            case '0C300209':
                $appendErr = '(User already has User Disk)';
                break;
            case '0C300207':
                $appendErr='';
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
            // var_dump($outputArr);
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
            // var_dump($output);
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
        if(isset($input['CHAP']) && $input['CHAP'] == false){
            $input['Username'] = '';
            $input['Password'] = '';
        }   
        if($this->iscsiLoginShell($input,$outputInfo)){         
            connectDB::$dbh->beginTransaction();                       
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
                    // var_dump($outputLun);        
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
            $input['ConfigSpace'] = array('CHAP'=>$input['CHAP'],'TargetName'=>$input['TargetName'],'Username'=>$input['Username'],'Password'=>$input['Password'],'Desc'=>$input['Desc'],'BkUUID'=>$outputLun['BkUUID'],'IP'=>$input['IP']);     
            if (!$this->sqlDeleteBackupSpace()) {    
                goto failRollback;        
            }                   
            if (!$this->sqlInserttbbackupspaceset($input)) {    
                goto failRollback;        
            }            
            $backupID = connectDB::$dbh->lastInsertId();
            $output=$outputLun;
            $output['ID'] = $backupID;
            $output['CHAP'] = $input['CHAP'];
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
            // var_dump($outputLun);
            $input['ConfigSpace'] = array('MountPath'=>$input['MountPath'],'Username'=>$input['Username'],'Password'=>$input['Password'],'Desc'=>$input['Desc'],'BkUUID'=>$outputLun['BkUUID'],'Port'=>$input['Port']);     
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
        $output = array('ID'=>0,'IP'=>'','Username'=>'','Password'=>'','TargetName'=>'');
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
        $output['TotalCapacity'] = (int)($outputLun['capacity']*1024*1024);
        $output['AvialCapacity'] = (int)($outputLun['availCapacity']*1024*1024);        
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
        $this->sqlListAllUserDisk($outputUserDisk,true);
        if(is_null($outputUserDisk)){
            return CPAPIStatus::ListBackupVDWithSizeFail;  
        }            
        // var_dump($outputVD);
        $this->backupList($input,$outputBackup);
        // var_dump($outputBackup);
        if($outputBackup['ID'] == 0)
            return CPAPIStatus::ListBackupVDWithSizeFail;          
        $input['BkUUID'] = $outputBackup['BkUUID'];
        if(!$this->listBackupVDWithSizeShell($input,$outputBackupVDs))
            return CPAPIStatus::ListBackupVDWithSizeFail;     
        if(!$this->listBackupUPWithSizeShell($input,$outputBackupUserDisks))
            return CPAPIStatus::ListBackupVDWithSizeFail;
        $output = array();
        $userC = new UserAction();
        $output['Original'] = array(); 
        $output['Seed'] = array();
        $output['UserDisk'] = array();
        $gmt = $this->getTZ($timezone);
        date_default_timezone_set($timezone);     
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
            if($vd['vdType'] == 'org'){                
                $output['Original'][] = array('SourceVDName'=>$sourceVDName,'SourceVDID'=>$sourceVDID,'BackupVDName'=>$backupVDName,'BackupVDID'=>$backupVDID,'BackupTime'=>date('Y-m-d H:i:s',$vd['bkTime']),'TotalSize'=>(int)$vd['diskSize'],'State'=>$state);
            }
            else if($vd['vdType'] == 'seed'){
                $output['Seed'][] = array('SourceVDName'=>$sourceVDName,'SourceVDID'=>$sourceVDID,'BackupVDName'=>$backupVDName,'BackupVDID'=>$backupVDID,'BackupTime'=>date('Y-m-d H:i:s',$vd['bkTime']),'TotalSize'=>(int)$vd['diskSize'],'State'=>$state);
            }
        }   
        foreach ($outputBackupUserDisks as $disk) {            
            $backupDiskName = $disk['imgName'];
            if($this->isBase64($disk['reportMeta']))
                $metadata = base64_decode($disk['reportMeta']);
            $metadataArr = explode('$*', $metadata);            
            if(isset($metadataArr[7]))
                $backupUsername=$metadataArr[7];
            $sourceDiskName = '';
            $sourceUsername = '';            
            $state = $this->backupResultEnum[$disk['bkResult']];
            if(array_key_exists($backupDiskName, $outputUserDisk)){
                // var_dump($outputUserDisk);
                $sourceUsername = $outputUserDisk[$backupDiskName]['Username'];
                $sourceDiskName = $outputUserDisk[$backupDiskName]['DiskName'];                
                if(!(isset($backupUsername) && strlen($backupUsername) > 0)){
                    $backupUsername=$sourceUsername;
            }
            }else{
                $userC->sqlSelectUserByNameDomainID($backupUsername,$input['DomainID'],$outputUser);
                if(isset($outputUser))
                    $sourceUsername = $backupUsername;
            }
            if(!(isset($backupUsername) && strlen($backupUsername) > 0)){
                $backupUsername=$backupDiskName;
            }
            $output['UserDisk'][] = array('SourceDiskName'=>$sourceDiskName,'SourceUsername'=>$sourceUsername,'BackupDiskName'=>$backupDiskName,'BackupUsername'=>$backupUsername,'BackupTime'=>date('Y-m-d H:i:s',$disk['bkTime']),'TotalSize'=>(int)$disk['diskSize'],'State'=>$state);     
        }
        return CPAPIStatus::ListBackupVDWithSizeSuccess;
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

    function listBackupUPWithSizeShell($input,&$output)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBKUPListDiskSize;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.$input['BkUUID'].'" ';               
            exec($cmd,$outputArr,$rtn);                                       
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
        if(!$this->sqlDeleteBackupSpace())
            goto failRollback;
        switch ($outputBackup['Type']) {
            case 1:
                if(!$this->iscsiLogoutShell($input))
                    goto failRollback;
                break;
            case 2:
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
            $this->insertBackupFailTaskShell($input,$type);
            $this->insertBackupLog($logCode,$input);
            return CPAPIStatus::BackupFail;
        }
        $logCode = '0C100301';
        $this->insertBackupLog($logCode,$input);
        return CPAPIStatus::BackupSuccess;
    }

    function insertBackupFailTaskShell($input,$type)
    {
        $cmd = $this->cmdBkTaskErrTask;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= '"'.base64_encode($type.'$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.base64_encode($input['Desc'])).'" ';
        $cmd .= 'bkBackup';
        exec($cmd,$output,$rtn);
    }

    function backupTaskShell($input,$type,&$rtn)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBkTaskVDBackup;
            // $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.base64_encode($type.'$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.base64_encode($input['Desc']).'$*'.$input['SSLayerMax']).'" ';                        
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

    function processReporBackupData($input,&$output,$isVDisk=false)
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
        if($isVDisk){
            $output['DiskID'] = $metadataArr[2];
            $output['DiskName'] = $metadataArr[3];
        }
        else{
            $output['VDID'] = $metadataArr[2];
            $output['VDName'] = $metadataArr[3];
        }
        $output['DomainID'] = $metadataArr[4];
        $output['DomainName'] = $metadataArr[5];  
        $output['State'] = 5;
        if(!isset($createTime) || $createTime == 0){
            $gmt = $this->getTZ($timezone);        
            date_default_timezone_set($timezone);  
            $createTime = date("Y-m-d H:i:s");    
        }   
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
        if(isset($metadataArr[7])){
            $output['Username'] = $metadataArr[7];
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
                $outputLayer = $snapC->sqlListSnapshotByLogicalLayerID($data['VDID'],$data['LogicalParentLayer']);                
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
                $outputRoot = $snapC->sqlListRootByid($data['VDID']);
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
                $outputRoot = $snapC->sqlListRootByid($data['VDID']);               
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
            if(isset($data['CreateTime']))
                $data['Time'] = $data['CreateTime'];
            $this->insertBackupLog($logCode,$data,$haveTask,$cancelTask,$data['ErrCode']);
            exit($rtnCode);
    }

    function insertBackupLog($code,$input,$haveTask=true,$cancelTask=false,$errCode=NULL)
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
        $appendVDDomain = $this->logappendVDDomain($input['VDName'],$input['DomainName']); 
        $appendErrCode = '';
        if(isset($errCode)) 
        {
            if($errCode == 82)
                $appendErrCode = "(iSCSI LUN's space is not enough to backup.)";
            else
                $appendErrCode = "($errCode)";
        }
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
            LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
        }       
    }

    function backupSeed($input)
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
        // if(!$this->vdC->sqlListVDInfoByID($input['VDID'],$outputVDInfo)){        
        //     return CPAPIStatus::BackupFail;
        // }
        $input['VDName'] = $outputVD['Name'];
        // $input['SSLayerMax'] = $outputVDInfo['SSLayerMax'];
        if(!isset($input['Desc']))
            $input['Desc'] = 'Auto : Backup';
        if(!$this->backupShell->backupSeedTaskShell($input)){
            $logCode = '0C300401';
            $this->insertBackupSeedLog($logCode,$input);
            return CPAPIStatus::BackupFail;
        }
        $logCode = '0C100301';
        $this->insertBackupSeedLog($logCode,$input);
        return CPAPIStatus::BackupSuccess;
    }

    // function backupSeedTaskShell($input)
    // {
    //     if(!is_null($input['ConnectIP'])){            
    //         $cmd = $this->cmdBkTaskSeedBackup;
    //         $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
    //         $cmd .= '"'.base64_encode('seedBackup$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.base64_encode($input['Desc'])).'" ';                        
    //         $cmd .= '"'.$input['VDID'].'" ';                     
    //         // var_dump($cmd);
    //         exec($cmd,$outputArr,$rtn);
    //         // var_dump($rtn);            
    //         if($rtn == 0){                                    
    //             return true;      
    //         }            
    //     }                     
    //     return false;       
    // }

    function processReporBackupSeedData($input,&$output)
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
        if(!isset($createTime) || $createTime == 0){
            $gmt = $this->getTZ($timezone);        
            date_default_timezone_set($timezone);  
            $createTime = date("Y-m-d H:i:s");    
        }   
        $output['CreateTime'] = $creaeTime;
        $output['ConnectIP'] = $this->getConnectIP();
        $output['ErrCode'] = $errCode;    
         if(isset($metadataArr[6])){
            $output['Desc'] = base64_decode($metadataArr[6]);  
            $output['LayerDesc'] = base64_decode($metadataArr[6]);                
        }         
    }

    function reportBkTaskSeedBackup($input)
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
            $rtnCode = 0;
            $logCode = '0C100302';
            goto rtnFinal;
        }
        else{
            if($data['ErrCode'] == 210){
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
            if(isset($data['CreateTime']))
                $data['Time'] = $data['CreateTime'];
            $this->insertBackupSeedLog($logCode,$data,$haveTask,$cancelTask,$data['ErrCode']);
            exit($rtnCode);
    }

    function insertBackupSeedLog($code,$input,$haveTask=true,$cancelTask=false,$errCode=NULL)
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
        $appendVDDomain = $this->logappendVDDomain($input['VDName'],$input['DomainName']); 
        $appendErrCode = '';
        if(isset($errCode)) 
        {
            if($errCode == 82)
                $appendErrCode = "(iSCSI LUN's space is not enough to backup.)";
            else
                $appendErrCode = "($errCode)";
        }
        if(is_null($appendErr)){
            if($cancelTask)
                $message = "Cancel Backup Seed VD$appendVDDomain$taskStr";
            else
                $message = "Backup Seed VD$appendVDDomain$taskStr Success";
        }
        else{
            $message = "Failed to backup Seed VD$appendVDDomain$taskStr$appendErr$appendErrCode";
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
        }       
    }

    function insertRestoreLog($code,$vdName,$domainName,$domainID,$haveTask=true,$errCode=NULL,$isSeed=false,$isVDisk=false)
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
        $restoreStr = "Restore VD";
        if($isSeed)
            $restoreStr = "Restore Seed VD";
        if($isVDisk)
            $restoreStr = "Restore User Disk";
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName); 
        $appendErrCode = '';
        if(isset($errCode)) 
            $appendErrCode = "($errCode)";
        if(is_null($appendErr)){
            $message = "$restoreStr$appendVDDomain$taskStr Success";
        }
        else{
            $message = "Failed to $restoreStr$appendVDDomain$taskStr$appendErr$appendErrCode";
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

    function restore($input,$isSeed=false)
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
            if(isset($input['NewVolumeID']) && strlen($input['NewVolumeID']) > 0)
            {   
                $this->sqlListVolumeByVolumeID($input['NewVolumeID'],$outputVolume);
                $input['VolumeName'] = $outputVolume['VolumeName'];    
            }
            else
            {
                $this->checkRestore($input,$outputMapping);   
                // var_dump($outputMapping);                 
                if(!$outputMapping['Mapping'])
                    return CPAPIStatus::RestoreFail;
                $input['DiskMapping'] = $outputMapping['DiskMapping'];
            }
            $input['VDID'] = $input['SourceVDID'];
            $input['VDName'] = $outputVD['Name'];
            return $this->restoreSame($input,$isSeed);
        }
        else{                        
            if(!isset($input['NewVolumeID']) || strlen($input['NewVolumeID']) == 0)
            {  
                return CPAPIStatus::RestoreFail;
            }
            $this->sqlListVolumeByVolumeID($input['NewVolumeID'],$outputVolume);
            // var_dump($outputVolume);
            $input['VolumeName'] = $outputVolume['VolumeName'];    
            return $this->restoreNewVD($input,$isSeed);
        }
    }

    function restoreNewVD($input,$isSeed)
    {
        $logCode = '';    
        if(!$isSeed){
            $responsecode = $this->vdC->createUser($input,$outputUser);
            if($responsecode == CPAPIStatus::CreateUserSuccess || $responsecode == CPAPIStatus::Conflict){
                $input['UserID'] = $outputUser['ID'];                
            }
            else{
                $logCode = '0C300201';                
                goto fail;
            }
        }
        if($this->vdC->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$output_vd)){
            $logCode = '0C300202';
            $rtn = CPAPIStatus::Conflict;
            $this->insertRestoreNewFailTaskShell($input,$isSeed);
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
        if(!$isSeed){
            if(!$this->vdC->sqlInsertVDOrg($input)){
                $logCode = '0C300204';       
                goto failRollback;          
            }   
            if($input['Username'] != '' && !$this->vdC->sqlUpdateUserDefaultVD($input)){
                $logCode = '0C300205';  
                goto failRollback;     
            }
        }
        else{
            if(!$this->vdC->sqlInsertVDSeed($input)){
                $logCode = '0C300204';       
                goto failRollback;          
            }   
        }
        if(!$this->vdC->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$output_vd)){
            $logCode = '0C300204';   
            goto failRollback;
        }
        $input['CPU']=-1;
        $input['CPUType']=0;
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
        $input['SSLayerMax']=$this->ssLayerDefault;
        $input['UEFI']=true;
        $input['VGACount']=0;
        $input['QXL']=true;
        if(!$this->vdC->sqlInsertVDInfo($input)){
            $logCode = '0C300206'; 
            goto failRollback;
        }
        if(!$this->backupShell->restoreNewShell($input,$isSeed)){
            $logCode = '0C300207'; 
            goto failRollback;
        }          
        $logCode = '0C100101';
        connectDB::$dbh->commit();  
        $rtn = CPAPIStatus::RestoreSuccess;
        goto end;
        fail:
            $rtn = CPAPIStatus::RestoreFail;
            $this->insertRestoreNewFailTaskShell($input,$isSeed);
            goto end;
        failRollback:
            $rtn = CPAPIStatus::RestoreFail;
            $this->insertRestoreNewFailTaskShell($input,$isSeed);
            connectDB::$dbh->rollBack();
            goto end;
        end:
            $this->insertRestoreLog($logCode,$input['VDName'],$input['DomainName'],$input['DomainID'],NULL,$isSeed);
            // var_dump($logCode);
            return $rtn;
    }

    function insertRestoreNewFailTaskShell($input,$isSeed)
    {
        if(!$isSeed){            
            $taskType='restoreNew';
        }
        else{
            $taskType='restoreSeedNew';            
        }
        $cmd = $this->cmdBkTaskErrTask;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= '"'.base64_encode("$taskType$*".$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['BackupVDID'].'$*'.$input['BackupVDName']).'" ';
        if(!$isSeed)
            $cmd .= 'bkSeedResNew';
        else
            $cmd .= 'bkResNew';
        exec($cmd,$output,$rtn);
    }        

    function restoreSame($input,$isSeed)
    {
        $logCode = '';
        connectDB::$dbh->beginTransaction();
        if(!$this->vdC->sqlUpdateVDtoRestoreStatus($input['SourceVDID'],41)){
            $logCode = '0C300208'; 
            goto failRollback;
        }        
        if(!$this->backupShell->restoreSameShell($input,$isSeed)){
            $logCode = '0C300207'; 
            goto failRollback;
        }
        $logCode = '0C100101';
        connectDB::$dbh->commit();  
        $rtn = CPAPIStatus::RestoreSuccess;
        goto end;       
        failRollback:
            $rtn = CPAPIStatus::RestoreFail;
            $this->insertRestoreSameFailTaskShell($input,$isSeed);
            connectDB::$dbh->rollBack();
            goto end;
        end:        
            $this->insertRestoreLog($logCode,$input['VDName'],$input['DomainName'],$input['DomainID'],true,NULL,$isSeed);
            return $rtn;
    }

    function insertRestoreSameFailTaskShell($input,$isSeed)
    {
        if(!$isSeed){            
            $taskType='restoreSame';
        }
        else{
            $taskType='restoreSeedSame';            
        }
        $cmd = $this->cmdBkTaskErrTask;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= '"'.base64_encode("$taskType$*".$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['BackupVDID'].'$*'.$input['BackupVDName']).'" ';
        if(!$isSeed)
            $cmd .= 'bkSeedResSame';
        else
            $cmd .= 'bkResSame';
        exec($cmd,$output,$rtn);
    }

    // function restoreSameShell($input,$isSeed)
    // {        
    //     if(!is_null($input['ConnectIP'])){      
    //         if(!$isSeed){    
    //             $taskType='restoreSame';
    //             $cmd = $this->cmdBkTaskVDRestoreSame;   
    //         }
    //         else{
    //             $taskType='restoreSeedSame';
    //             $cmd = $this->cmdBkTaskSeedRestoreSame;
    //         }
    //         $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
    //         $cmd .= '"'.base64_encode("$taskType$*".$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['BackupVDID'].'$*'.$input['BackupVDName']).'" ';                    
    //         $cmd .= '"'.$input['BkUUID'].'" ';    
    //         $cmd .= '"'.$input['VDID'].'" '; 
    //         $cmd .= '"'.$input['VDName'].'" '; 
    //         if(isset($input['NewVolumeID'])){
    //             $cmd .= '"'.$input['VolumeName'].'" '; 
    //             $cmd .= '"'.$input['VolumeName'].'" '; 
    //             $cmd .= '"'.$input['VolumeName'].'" '; 
    //             $cmd .= '"'.$input['VolumeName'].'" '; 
    //             $cmd .= '"'.$input['VolumeName'].'" '; 
    //             $cmd .= '"'.$input['VolumeName'].'" '; 
    //             $cmd .= '"'.$input['VolumeName'].'" '; 
    //         }
    //         else{                
    //             $diskArr = ["hda","hdc","hdd","hde","hdf","hdg","hdh"];                
    //             foreach ($diskArr as $value) {
    //                 $mapping = false;
    //                 foreach ($input['DiskMapping'] as $value1) {
    //                     // var_dump($value);
    //                     // var_dump($value1);
    //                     if($value1['diskName'] == $value){
    //                         $cmd .= '"'.$value1['diskVolumeId'].'" '; 
    //                         $mapping = true;
    //                         break;
    //                     }
    //                 }
    //                 if(!$mapping){
    //                     $cmd .= '"" '; 
    //                 }
    //             }
    //         }
    //         // var_dump($cmd);
    //         exec($cmd,$outputArr,$rtn);
    //         // var_dump($rtn);
    //         if($rtn == 0){                                    
    //             return true;      
    //         }            
    //     }                           
    //     return false;
    // }

    function backupUserDisk($input,$type = 'upBackup')
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
        // $this->backupList($input,$outputBackup);
        // if($outputBackup['ID'] == 0)
        //     return CPAPIStatus::BackupFail;               
        $outputDisk = NULL;        
        $this->sqlListUserDiskByName($input['DiskName'],$outputDisk);              
        if(is_null($outputDisk))
            return CPAPIStatus::BackupFail;     
        $input['DiskID'] = $outputDisk['DiskID'];
        $input['Username'] = $outputDisk['Username']; 
        $input['DiskSize'] = $outputDisk['DiskSize'];
        $input['DiskType'] = $outputDisk['DiskType'];
        $input['DiskCache'] = $outputDisk['DiskCache'];
        $input['SSLayerMax'] = $outputDisk['SSLayerMax'];              
        if(!$this->backupShell->backupUserDiskTaskShell($input,$type,$rtn)){
            $logCode = '0C300401';
            if($rtn != 0)
                $errCode = $rtn;
            $this->insertBackupUPLog($logCode,$input,true,false,$errCode);
            return CPAPIStatus::BackupFail;
        }            
        $logCode = '0C100301';
        $this->insertBackupUPLog($logCode,$input,true,false,NULL);
        return CPAPIStatus::BackupSuccess;
    }  

    function insertBackupUPLog($code,$input,$haveTask=true,$cancelTask=false,$errCode=NULL)
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
        $appendVDDomain = $this->logappendVDDomain($input['Username'],$input['DomainName']); 
        $appendErrCode = '';
        if(isset($errCode)) 
        {
            if($errCode == 82)
                $appendErrCode = "(iSCSI LUN's space is not enough to backup.)";
            else
                $appendErrCode = "($errCode)";
        }
        if(is_null($appendErr)){
            if($cancelTask)
                $message = "Cancel Backup User Disk$appendVDDomain$taskStr";
            else
                $message = "Backup User Disk$appendVDDomain$taskStr Success";
        }
        else{
            $message = "Failed to backup User Disk$appendVDDomain$taskStr$appendErr$appendErrCode";
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
        }       
    }  

    function reportBkTaskUPBackup($input)
    {
        $haveTask = false;
        $cancelTask = false;
        $rtnCode = 99;        
        $logCode = '0C300402';
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFinal;
        }                  
        $this->processReporBackupData($input,$data,true);
        $snapC = new SnapshotAction(); 
        // var_dump($data);            
        if(!$this->sqlCheckVDiskExit($data['DiskID'])){
            $rtnCode = 0;
            goto rtnFinal;
        }
        if($data['ErrCode'] == 0)
        {                
            $this->sqlGetUUID($outputUUID);
            if(is_null($outputUUID)){
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
                if(!$this->sqlCheckVDiskExit($data['DiskID'])){
                    $rtnCode = 0;
                    goto rtnFinal;
                }               
                $outputLayer = $snapC->sqlListSnapshotByLogicalLayerID($data['DiskID'],$data['LogicalParentLayer'],true);
                // $outputLayer = null;
                if(is_null($outputLayer)){
                    $vdC = new VDAction();
                    connectDB::$dbh->beginTransaction();
                    $rnt = $vdC->recreateVDiskSnapshot($data);
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
                $outputRoot = $snapC->sqlListRootByid($data['DiskID'],true);
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
            if(!$snapC->sqlInsertSanpshotLayer($data,true)){               
                $rtnCode = 3;              
                goto failRollback;
            }
            if(!$rootSame){
                if(!$snapC->deleteSnapshotToRoot($data['DiskID'],$data['RootLayer'],true)){
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
                $outputRoot = $snapC->sqlListRootByid($data['DiskID'],true);               
                if(is_null($outputRoot)){
                    $rtnCode = 2;
                    goto rtnFinal;
                }                                  
                if($outputRoot['LogicalLayer'] != $data['RootLayer'])
                    $rootSame = false;                                
                if(!$rootSame){
                    connectDB::$dbh->beginTransaction();           
                    if(!$snapC->deleteSnapshotToRoot($data['DiskID'],$data['RootLayer'],true)){
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
            $this->insertBackupUPLog($logCode,$data,$haveTask,$cancelTask,$data['ErrCode']);
            exit($rtnCode);
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
            // var_dump($cmd);              
            exec($cmd,$outputArr,$rtn);
            // var_dump($rtn);
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
            // var_dump($rtn);
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
            $cmd .= base64_encode($input['Desc']).' ';               
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
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);           
            // var_dump($outputArr);
            // var_dump($rtn);
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

    function backupUPDelete($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }                                  
        $this->backupList($input,$outputBackup);
        if($outputBackup['ID'] == 0)
            return CPAPIStatus::DeleteBackupVDFail;  
        $input['BkUUID'] = $outputBackup['BkUUID'];
        if(!$this->bakupUPDeleteShell($input))
            return CPAPIStatus::DeleteBackupVDFail;  
        return CPAPIStatus::DeleteBackupVDSuccess;
    }

    function bakupUPDeleteShell($input)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBKLUNUPDel;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);                        
            $cmd .= '"'.$input['BkUUID'].'" ';    
            $cmd .= '"'.$input['BackupDiskName'].'" ';                
            // var_dump($cmd);        
            exec($cmd,$outputArr,$rtn);
            // var_dump($rtn);
            if($rtn == 0){                                    
                return true;      
            }            
        }          
        return false;        
    }

    function restoreUP($input)
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
        if(isset($input['SourceDiskName']) && strlen($input['SourceDiskName']) > 0){
            if($input['SourceDiskName'] != $input['BackupDiskName'])
                return CPAPIStatus::RestoreFail;           
        }
        $input['BkUUID'] = $outputBackup['BkUUID'];       
        if($input['SourceDiskName'] == $input['BackupDiskName']){
            // var_dump('====1====');      
            if(!$this->sqlCheckVDiskExitByName($input['BackupDiskName'],$outputDisk))
                return CPAPIStatus::RestoreFail;
            $input['DiskID'] = $outputDisk['DiskID'];
            $input['Username'] = $outputDisk['Username'];
            $input['VolumeName'] = $outputDisk['VolumeName'];
            $input['VolumeID'] = $outputDisk['VolumeID'];
            // var_dump($outputVD);         
            // var_dump($input);             
            return $this->restoreSameUP($input);
        }
        else{            
            $this->sqlListVolumeByVolumeID($input['NewVolumeID'],$outputVolume);
            if(is_null($outputVolume)){
                return CPAPIStatus::RestoreFail;
            }
            $input['VolumeName'] = $outputVolume['VolumeName'];  
            return $this->restoreNewUP($input);
        }
    }

    function restoreSameUP($input)
    {
        $logCode = '';
        connectDB::$dbh->beginTransaction();        
        if(!$this->sqlUpdateUserDisktoRestoreStatus($input['DiskID'],41)){
            $logCode = '0C300208'; 
            goto failRollback;
        }
        if(!$this->backupShell->restoreSameUPShell($input)){
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
            $this->insertRestoreLog($logCode,$input['SourceUsername'],$input['DomainName'],$input['DomainID'],true,NULL,false,true);
            return $rtn;
    }    

    function restoreNewUP($input)
    {        
        // var_dump($input);
        if(!isset($input['SourceUsername']) || strlen($input['SourceUsername']) == 0){
            return CPAPIStatus::RestoreFail;
        }
        $logCode = '';              
        $input['Name'] = $input['SourceUsername'];  
        $userC = new UserAction();  
        $responsecode = $userC->createUser($input,$outputUser,false);
        if($responsecode == CPAPIStatus::CreateUserSuccess || $responsecode == CPAPIStatus::Conflict){
            $input['UserID'] = $outputUser['ID'];                
        }
        else{
            $logCode = '0C300201';                
            goto fail;
        }
        if($responsecode == CPAPIStatus::Conflict){
            if($this->sqlCheckUserHaveUserDisk($input['UserID'])){
                $logCode = '0C300209';
                goto fail;
            }
        }
        // goto fail;
        connectDB::$dbh->beginTransaction();     
        $input['ID'] = null;             
        $input['DiskName'] = $input['BackupDiskName'];
        $input['DiskType'] = 2;
        $input['Name'] = $input['BackupDiskName'];
        $input['DiskSize'] = 0;
        $input['State'] = 41;               
        $input['VolumeID'] = $input['NewVolumeID'];
        // var_dump($input);
        if(!$userC->sqlInsertVdisk($input,true)){
            $logCode = '0C300203';
            goto failRollback;                
        }
        $output_id = null;    
        $userC->sqlGetidVDisk($input,$output_id);            
        if(is_null($output_id)){
            $logCode = '0C300203';
            goto failRollback;   
        }
        $input['DiskID'] = $output_id;                       
        if(!$userC->sqlInserttbUserDisk($input)){
            $logCode = '0C300203';
            goto failRollback;            
        }                   
        if(!$this->backupShell->restoreNewUPShell($input,$rtn)){
            $errCode = $rtn;
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
            // var_dump($logCode);
            $this->insertRestoreLog($logCode,$input['SourceUsername'],$input['DomainName'],$input['DomainID'],true,$errCode,false,true);
            return $rtn;
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
                    $output['ID'] = (int)$row['idBackupSpace'];
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