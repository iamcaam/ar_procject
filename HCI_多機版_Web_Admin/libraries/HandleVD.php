<?php
class VDAction extends BaseAPI
{    
    protected $lockTime = 3;    
    private $cmdVDTaskOrgCreate = CmdRoot."ip vd_task_org_create ";
    private $cmdVDTaskOrgClone = CmdRoot."ip vd_task_org_clone ";
    private $cmdVDTaskUserClone = CmdRoot."ip vd_task_user_clone ";
    private $cmdVDTaskSeedCloneBack = CmdRoot."ip vd_task_seed_clone_back ";
    private $cmdVDTaskDiskAdd = CmdRoot."ip vd_task_disk_add ";
    private $cmdVDTaskDiskMove = CmdRoot."ip vd_task_disk_move ";
    private $cmdVDTaskUserprofileMove = CmdRoot."ip vd_task_userprofile_move ";
    private $cmdVDTaskDiskDelete = CmdRoot."ip vd_task_disk_del ";
    private $cmdVDNICAdd = CmdRoot."ip vd_nic_add ";
    private $cmdVDNICDelete = CmdRoot."ip vd_nic_del ";
    private $cmdVDModify = CmdRoot."ip vd_modify ";
    private $cmdVDDiskModify = CmdRoot."ip vd_disk_modify ";
    private $cmdVDVideoSet = CmdRoot."ip vd_video_set ";
    private $cmdVDListAll = CmdRoot."ip vd_list_all ";
    private $cmdVDListDiskSizeAll = CmdRoot."ip vd_list_diskSize_all ";
    private $cmdVDTaskSeedCreate = CmdRoot."ip vd_task_seed_create ";
    private $cmdVDTaskSeedOverwrite = CmdRoot."ip vd_task_seed_overwrite ";
    private $cmdVDTaskUserCreate = CmdRoot."ip vd_task_user_create ";
    private $cmdVDTaskSeedReborn = CmdRoot."ip vd_task_seed_reborn ";
    private $cmdVDTaskSeedRecovery = CmdRoot."ip vd_task_seed_recovery ";
    private $cmdISOListAll= CmdRoot."ip vd_iso_list_all ";
    private $cmdVDISOAttach = CmdRoot."ip vd_iso_attach ";
    private $cmdVDISODetach = CmdRoot."ip vd_iso_detach ";
    private $cmdVDPoweronBefore = CmdRoot."ip vd_poweron_before "; 
    // private $cmdVDPoweron = "sudo /var/www/html/script/cp_util.sh vd_test ";     
    private $cmdVDPoweron = CmdRoot."ip vd_poweron ";
    private $cmdVDPoweronViewer = CmdRoot."ip vd_poweron_viewer ";
    private $cmdVDShutdown = CmdRoot."ip vd_shutdown ";
    private $cmdVDPoweroff = CmdRoot."ip vd_poweroff ";
    private $cmdVDDel = CmdRoot."ip vd_del ";    
    private $cmdVDDisableSet = CmdRoot."ip vd_disable_set ";    
    private $cmdVDList = CmdRoot."ip vd_list ";    
    private $cmdVDListWithSS = CmdRoot."ip vd_list_with_ss ";
    private $cmdVDPoweronInfo = CmdRoot."ip vd_poweron_info ";  
    private $cmdVDPasswdSet = CmdRoot."ip vd_connect_passwd_set ";
    private $cmdVDTaskExport = CmdRoot."ip vd_task_export ";
    private $cmdVDTaskImport = CmdRoot."ip vd_task_import ";
    private $cmdVDImportList = CmdRoot."ip vd_import_list ";
    private $cmdVDImportListDisk = CmdRoot."ip vd_import_list_disk ";
    private $cmdAutosuspendList = CmdRoot.'ip node_autosuspend_list ';
    private $cmdClusterAutosuspendList = CmdRoot.'ip cluster_autosuspend_list ';
    private $cmdClusterAutosuspendSet = CmdRoot.'ip cluster_autosuspend_set ';
    private $cmdAutosuspednSet = CmdRoot.'ip node_autosuspend_set ';
    private $cmdVDSuspendSet = CmdRoot.'ip vd_suspend_set ';
    private $cmdVDLiveMigrate = CmdRoot.'ip vd_live_migrate ';
    private $cmdVDUserProfileAttach = CmdRoot."ip vd_userprofile_attach ";
    private $cmdVDUserProfileDetach = CmdRoot."ip vd_userprofile_detach ";
    private $cmdVDTaskErrTask = CmdRoot."ip vd_task_err_task ";
    private $cmdVDListSpecific = CmdRoot."ip vd_list_specific ";
    private $cmdVDUserprofileListWithSS = CmdRoot."ip vd_userprofile_list_with_ss ";    
    private $cmdVDUserprofileListDiskSizeAll = CmdRoot."ip vd_userprofile_list_diskSize_all";
    private $cmdVDTaskGatewayCreate = CmdRoot."ip vd_task_gateway_create ";
    private $cmdVDNICReplace = CmdRoot."ip vd_nic_replace ";

    private $cmdQemuGA = CmdRoot."ip gw_qemu_ga ";

    private $cmd_vd_task_seed_distribute = "sudo /var/www/html/vdi_util.sh vd_task_seed_distribute ";
    private $cmd_vd_import_disk_list = "sudo /var/www/html/vdi_util.sh vd_import_disk_list ";    
    private $cmd_vd_userprofile = "sudo /var/www/html/vdi_util.sh vd_userprofile ";            

    private $cmdVDPoweronPreferNodeSet = CmdRoot.'ip vd_poweron_prefer_node_set ';

    function  __construct()
    {    
        set_time_limit(0);         
    }    

    function test($argv)
    {
        var_dump($argv);
    }    

    function newDomainClass()
    {       
        if(is_null($this->domainC))
            $this->domainC = new DomainAction();
    }    

    function logVDAppendErr($code)
    {
        $appendErr = null;
        switch ($code) {
            case '05300201': 
            case '05300602':  
            case '05300802':
            case '05302A02':
            case '05300C02':
                $appendErr = '(Failed to create user)';
                break;     
            case '05300202':
            case '05300401':
            case '05300601':
            case '05300801':
            case '05302A01':
            case '05300E02':
            case '05300C04':
                $appendErr = '(VD\'s name is duplicated)';
                break;     
            case '05300205':
            case '05300403':
            case '05300604':
            case '05300804':
            case '05302A04':
            case '05301A02':
                $appendErr = '(Failed to list cluster)';
                break;
            case '05300405':
                $appendErr = '(Failed to list Virtual Host\'s disk)';
                break;
            case '05300606':
                $appendErr = '(Failed to list seed VD\'s disk)';
                break;
            case '05300206':
            case '05300407':
            case '05300608':
            case '05300808':
            case '05302A08':
            case '05300C08':
                $appendErr = '(Failed to insert VD base db)';
                break;
            case '05300207':
            case '05300408':
            case '05300609':
            case '05300809':
            case '05302A09':
            case '05300C09':
                $appendErr = '(Failed to insert VD info db)';
                break;
            case '05300209':
            case '0530040A':
            case '0530060B':
            case '0530020A':
            case '0530080B':
            case '05302A0B':           
            case '05302101': 
                $appendErr = '(Failed to insert VD disk db)';                
                break;
            case '05302C01':
            case '05302E01':
            case '04301701':
                $appendErr = '(Failed to update VD disk db)';                
                break;
            case '0530020B':
            case '0530060C':
            case '0530080C':
            case '05302A0C':
            case '05300C0C':
                $appendErr = '(Failed to update default VD)'; 
                break;
            case '05300409':
                $appendErr = '(Failed to insert VD seed db)';
                break;
            case '05300603':
                $appendErr = '(Failed to list seed VD)';
                break;
            case '0530060A':
                $appendErr = '(Failed to insert user VD db)';
                break;
            case '0530060D':
            case '0530080D':            
            case '05302A0D':
            case '05300E01':
            case '05300A01':
            case '05300C0D':
            case '05300C0F':
                $appendErr = '(Failed to list VD)';
                break;
            case '05300803':
            case '05302A03':
                $appendErr = '(Failed to list source VD)';
                break;
            case '05300208':
            case '0530080A':
            case '05302A0A':
            case '05300C0A':
                $appendErr = '(Failed to insert Virtual Host db)';
                break;
            case '05300A03':
            case '05300C0B':
                $appendErr = '(Failed to delete VD disk db)';
                break;
            case '05301A04':
                $appendErr = '(Failed to delete VD db)';
                break;
            case '05301A05':
                $appendErr = '(Failed to delete user deafault VD db)';
                break;
            case '05301A07':
                $appendErr = '(VD in task)';
                break;
            case '05302301':
                $appendErr = '(Failed to update VD disk db)';
                break;
            case '05301005':
                $appendErr = '(Out of memory)';
                break;
            case '05301006':
                $appendErr = '(VD is disabled)';
                break;
            case '05301007':
                $appendErr = '(VD is in task)';
                break;
            case '05301008':
                $appendErr = '(Too many users)';
                break;                
            case '05300E05':
            case '05300E05-1':
                $appendErr = '(Failed to update VD db)';
                break;
            case '05300A03-40':
                $appendErr = '(VD in view)';
                break;
            case '05300C06':
                $appendErr = '(Failed to list VD\'s disk)';
                break;
            case '0530020E':
                $appendErr = '(Failed to list Vswitch)';
                break;
            case '0530020C':
            case '0530040B':
            case '0530060E':
            case '0530080E':
            case '05302A0E':
            case '05301A06':
            case '05301F02':
            case '05302102':
            case '05302302':
            case '05301003':
            case '05301403':
            case '05301203':
            case '05300E06':
            case '05300E06-1':
            case '05300A03':
            case '05300C0E':
            case '05302501':
            case '05302701':     
            case '0530060F':
            case '0530020D':
            case '0530040C':
            case '05302303':
            case '05302103':
            case '0530080F':
            case '05302A0F':
            case '05300A04':
            case '05300C10':
            case '0C300209':
            case '05301606':
            case '05301C06':
            case '05302C02':
            case '05302E02':
            case '05302E03':
            case '04301702':
            case '04301703':
                $appendErr = '';
                break;
        }
        return $appendErr;
    }  

    function logGlusterAppendErr($code)
    {
        $appendErr = null;
        switch ($code) {            
            case '01301001':
                $appendErr = '';
                break;
        }
        return $appendErr;
    }  

    function newUserClass()
    {
        if($this->userC == null)
            $this->userC = new UserAction();
    }

    function createUser($input,&$outputUser)
    {
        $this->newUserClass();
        $responsecode = $this->userC->createUser(array('Name'=>$input['Username'],'DomainID'=>$input['DomainID'],'isAdmin'=>false),$outputUser,false);
        return $responsecode;
    }

    function listAllISO($input,&$output)
    {
        $isos = array();
        $this->isoListAllShell($input,$outputIsos);
        $isos[] = array("Name"=>'None',"Path"=>'none');
        foreach ($outputIsos as $value) {            
            $isos[] = array("Name"=>$value['name'],"Path"=>$value['name']);
        }
        $output = array("CDRom"=>$isos);
    }

    function isoListAllShell($input,&$output)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdISOListAll;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);              
            // var_dump($cmd);   
            exec($cmd,$outputArr,$rtn);
            if($rtn == 0){   
                $output = '';
                foreach ($outputArr as $value) {
                    $output.=$value;
                }          
                $output = json_decode($output,true);
                return true;      
            }            
        }             
        $output = json_decode(array(),true);      
        return false;       
    }

    function processReportRestoreUPData($input,&$output)
    {        
        $metadata = base64_decode($input[0]);
        $errCode = $input[1];
        $creaeTime = date('Y-m-d H:i:s',$input[2]);     
        $volumeName = $input[3];                      
        $metadataArr = explode('$*', $metadata);          
        $type = $this->backupActionEnum[$metadataArr[0]];
        // var_dump($metadataArr);    
        $output = array();     
        $output['Type'] = $type;
        $output['DiskID'] = $metadataArr[1];
        $output['DiskName'] = $metadataArr[2];
        $output['DomainID'] = $metadataArr[3];
        $output['DomainName'] = $metadataArr[4];  
        $output['Username'] = $metadataArr[5];       
        $output['State'] = 0;
        $output['CreateTime'] = $creaeTime;
        $output['VolumeName'] = $volumeName;   
        // $output['ConnectIP'] = $this->getConnectIP();  
        $output['ErrCode'] = $errCode;  
        // var_dump($type);
        // var_dump($input);
        if($type==$this->backupActionEnum['restoreNewUP']&&isset($input[4])){            
            if($this->isBase64($input[4])){
                $reportMeta = base64_decode($input[4]);
                $reportMetaArr = explode('$*', $reportMeta);                 
                if(isset($reportMetaArr[11]))
                    $output['SSLayerMax'] = $reportMetaArr[11];
            }
        }        
        // var_dump($output); 
    }

    function processReportData($input,&$output,$isImport = false)
    {
        $metadata = base64_decode($input[0]);
        $errCode = $input[1];
        $createTime = date('Y-m-d H:i:s',$input[2]);        
        if(isset($input[3])){
            $disks = base64_decode($input[3]);
            $disks = json_decode($disks,true);
        }
        if(isset($input[4]))
            $nics = base64_decode($input[4]);        
        $metadataArr = explode('$*', $metadata);               
        $type = $this->taskTypeEnum[$metadataArr[0]];        
        // var_dump($metadataArr);    
        $output = array();     
        $output['Type'] = $type;
        $output['CephID'] = $metadataArr[1];
        $output['VDID'] = $metadataArr[2];
        $output['VDName'] = $metadataArr[3];
        $output['DomainID'] = $metadataArr[4];
        $output['DomainName'] = $metadataArr[5];
        if(isset($metadataArr[6]))
        {
            if($isImport)
                $output['VDNumber'] = $metadataArr[6];
            else
                $output['OrgVDID'] = $metadataArr[6];
        }
        $output['ConnectIP'] = $this->getConnectIP();
        $output['State'] = 5;
        if(!isset($createTime) || $createTime == 0){
            $gmt = $this->getTZ($timezone);        
            date_default_timezone_set($timezone);  
            $createTime = date("Y-m-d H:i:s");    
        }            
        $output['CreateTime'] = $createTime;
        $output['NIC'] = $nics;    
        $output['Disks'] = $disks;
        $output['ErrCode'] = $errCode;
        // var_dump($output);
    }

    function processReportDiskData($input,&$output)
    {
        $metadata = base64_decode($input[0]);
        $errCode = $input[1];
        $creaeTime = date('Y-m-d H:i:s',$input[2]);        
        if(isset($input[3])){
            $disks = base64_decode($input[3]);
            $disks = json_decode($disks,true);
        }
        if(isset($input[4]))
            $nics = base64_decode($input[4]);        
        $metadataArr = explode('$*', $metadata);               
        $type = $this->taskTypeEnum[$metadataArr[0]];        
        // var_dump($metadataArr);    
        $output = array();     
        $output['Type'] = $type;
        $output['CephID'] = $metadataArr[1];
        $output['VDID'] = $metadataArr[2];
        $output['VDName'] = $metadataArr[3];
        $output['DomainID'] = $metadataArr[4];
        $output['DomainName'] = $metadataArr[5];
        $output['DiskID'] = $metadataArr[6];
        if(isset($metadataArr[7]))
        {
            $output['DiskSize'] = $metadataArr[7];
        }
        $output['ConnectIP'] = $this->getConnectIP();
        $output['State'] = 5;
        $output['CreateTime'] = $creaeTime;
        $output['ErrCode'] = $errCode;
        // var_dump($output);
    }    

    function processReportDiskMoveData($input,&$output)
    {
        $metadata = base64_decode($input[0]);
        $errCode = $input[1];
        $creaeTime = date('Y-m-d H:i:s',$input[2]);        
        if(isset($input[3])){
            $disks = base64_decode($input[3]);
            $disks = json_decode($disks,true);
        }
        if(isset($input[4]))
            $nics = base64_decode($input[4]);        
        $metadataArr = explode('$*', $metadata);               
        $type = $this->taskTypeEnum[$metadataArr[0]];        
        // var_dump($metadataArr);    
        $output = array();             
        $output['Type'] = $type;
        $output['CephID'] = $metadataArr[1];
        $output['VDID'] = $metadataArr[2];
        $output['VDName'] = $metadataArr[3];
        $output['DomainID'] = $metadataArr[4];
        $output['DomainName'] = $metadataArr[5];
        $output['DiskID'] = $metadataArr[6];
        $output['DiskName'] = $metadataArr[7];
        $output['VolumeID'] = $metadataArr[8];
        $output['VolumeAlias'] = $metadataArr[9];
        $output['SrcVolumeAlias'] = $metadataArr[10];
        $output['ConnectIP'] = $this->getConnectIP();
        $output['CreateTime'] = $creaeTime;
        $output['ErrCode'] = $errCode;
        // var_dump($output);
    }
    
    function processReportUserprofileMoveData($input,&$output)
    {
        $metadata = base64_decode($input[0]);
        $errCode = $input[1];
        $creaeTime = date('Y-m-d H:i:s',$input[2]);        
        if(isset($input[3])){
            $disks = base64_decode($input[3]);
            $disks = json_decode($disks,true);
        }
        if(isset($input[4]))
            $nics = base64_decode($input[4]);        
        $metadataArr = explode('$*', $metadata);               
        $type = $this->taskTypeEnum[$metadataArr[0]];        
        // var_dump($metadataArr);    
        $output = array();             
        $output['Type'] = $type;
        $output['CephID'] = $metadataArr[1];
        $output['UserID'] = $metadataArr[2];
        $output['Username'] = $metadataArr[3];
        $output['DomainID'] = $metadataArr[4];
        $output['DomainName'] = $metadataArr[5];
        $output['DiskID'] = $metadataArr[6];
        $output['DiskName'] = $metadataArr[7];
        $output['VolumeID'] = $metadataArr[8];
        $output['VolumeAlias'] = $metadataArr[9];
        $output['SrcVolumeAlias'] = $metadataArr[10];
        $output['ConnectIP'] = $this->getConnectIP();
        $output['CreateTime'] = $creaeTime;
        $output['ErrCode'] = $errCode;
        // var_dump($output);
    }

    function processReportRestoreData($input,&$output)
    {
        $metadata = base64_decode($input[0]);
        $errCode = $input[1];
        $creaeTime = date('Y-m-d H:i:s',$input[2]); 
        $volumeName = $input[3];                               
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
        $output['ConnectIP'] = $this->getConnectIP();  
        $output['ErrCode'] = $errCode;       
        $output['VolumeName'] = $volumeName;   
        if(($type==$this->backupActionEnum['restoreNewSeed'] || $type==$this->backupActionEnum['restoreSameSeed'])&&isset($input[3])){            
            $output['RecoveryTime'] = date('Y-m-d H:i:s',$input[4]);
        }    
        else if($type==$this->backupActionEnum['restoreNew']&&isset($input[4])){
            if($this->isBase64($input[4])){
                $reportMeta = base64_decode($input[4]);
                $reportMetaArr = explode('$*', $reportMeta); 
                if(isset($reportMetaArr[7]))
                    $output['SSLayerMax'] = $reportMetaArr[7];
            }
        }    
    }

    function createAcroGateway($input)
    {
        // var_dump($input);
        $logCode = "";
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }            
        if(!$this->sqlAddVDSnapshotMaxCountField()){
            return CPAPIStatus::DBConnectFail;
        }        
        if(!$this->sqlGetUUID($outputUUID))
            return CPAPIStatus::DBConnectFail;
        if(!$this->sqlAddColumnField()){
            return CPAPIStatus::DBConnectFail;
        }
        $this->newDomainClass();                
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);
        $input['DomainName'] = $outputDomain['Name']; 
        $input['Username'] = 'acrosystem';   
        if($input['Username'] != ""){               
            $responsecode = $this->createUser($input,$outputUser);           
            if($responsecode == CPAPIStatus::CreateUserSuccess || $responsecode == CPAPIStatus::Conflict){                
                $input['UserID'] = $outputUser['ID'];                
            }
            else{                
                $logCode = '05300201';                
                goto fail;                
            }
        }
        else
        {            
            $input['UserID'] = null;    
        }                                             
        if($this->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$outputVD)){            
            $logCode = '05300202'; 
            $this->insertCreateVDFailTaskShell($input);
            $rtn = CPAPIStatus::Conflict;          
            goto end; 
        }       
        if(isset($input['ConnectIP'])){                     
            connectDB::$dbh->beginTransaction();              
            $input['UUID'] = $outputUUID['UUID'];          
            //Set State For Task
            $input['State'] = 30;
            if(!$this->sqlInsertVDBase($input)){
                $logCode = '05300206';                
                goto fail_del_vd; 
            }
            $outputVD = null;
            $this->sqlListVDByID($input['UUID'], $outputVD);            
            if(is_null($outputVD)){
                $logCode = '05300206';                
                goto fail_del_vd;  
            }
            $input['VDNumber'] = $outputVD['VDNumber'];               
            $input['NIC_Info']="";    
            if(!isset($input['UEFI']))
                $input['UEFI'] = false;            
            $input['QXL'] = true;
            if(!isset($input['VGACount']))
                $input['VGACount'] = 0;  
            if(!isset($input['CPUType'])){
                $input['CPUType'] = 1;
                $input['DiskType'] = 0;
                $input['DiskCache'] = 0;
            }                
            $input['CpuCt']=2;
            $input['CPU'] = -1;
            $input['RAM']=2048;
            $input['Disk']=4;
            $input['CDRom']='';            
            $input['USBRedirCt']=0;
            $input['Sound']=1;
            $input['USBType'] = 2;
            $input['Suspend'] = false;
            if(!$this->sqlInsertVDInfo($input)){
                $logCode = '05300207';                
                goto fail_del_vd;                  
            }
            $input['Type'] = 1;
            $input['JsonConfig'] = '{}';
            if(!$this->sqlInsertServiceVD($input)){
                $logCode = '05300208';               
                goto fail_del_vd;                 
            }
            $this->sqlListVolumeByVolumeName('1',$outputVolume);
            $input['VolumeID'] = $outputVolume['VolumeID'];
            $input['VDID'] = $input['UUID'];
            if(!$this->sqlInsertVDDisk($input,30)){
                $logCode = '05300209';                
                goto fail_del_vd;                  
            }
            $lastID = connectDB::$dbh->lastInsertId();
            $input['DiskID'] = $lastID;
            // if($input['Username'] != '' && !$this->sqlUpdateUserDefaultVD($input)){
            //     $logCode = '0530020B';                    
            //     goto fail_del_vd;              
            // }
            if(!$this->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$outputVD)){
                $logCode = '0530020A';                
                goto fail_del_vd;   
            }                       
            $input['VDID'] = $outputVD['VDID'];            
            // $this->sqlListVolumeByVolumeID($input['VolumeID'],$outputVolume);
            // $input['VolumeName'] = $outputVolume['VolumeName'];            
            if($this->createAcroGatewayShell($input) != 0){
                $logCode = '0530020C';                
                goto fail_del_vd;       
            }            
            connectDB::$dbh->commit();
            $logCode = '05100101';           
            $rtn = CPAPIStatus::CreateVDofCephSuccess;
            goto end;
        }
        else{
            $logCode = '05300205';
            goto fail;
        } 
        fail:
            $rtn = CPAPIStatus::CreateVDofCephFail;
            $this->insertCreateVDFailTaskShell($input);
            goto end;
        fail_del_vd:                
            connectDB::$dbh->rollBack();
            $rtn = CPAPIStatus::CreateVDofCephFail;
            $this->insertCreateVDFailTaskShell($input);
            goto end;
        end:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertCreateVDLog($logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],$appendErr,true,false,NULL,true);
            return $rtn;
    }

    function createAcroGatewayShell($input)
    {
        $rtn=-99;                
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDTaskGatewayCreate;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.base64_encode('createAG$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['DiskID']).'" ';
            $cmd .= '"'.$input['UUID'].'" ';            
            $cmd .= '"'.$input['VDName'].'" ';  
            foreach ($input['vswitch'] as $value) {
                $cmd .= 'br'.$value.' ';
            }          
            $cmd .= '"'.$input['DomainID'].'" ';  
            // var_dump($cmd);            
            exec($cmd,$output,$rtn);
            // var_dump($rtn);
        }        
        return $rtn;
    }

    function createAcroGatewayNew($input)
    {
        // var_dump($input);
        $logCode = "";
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }            
        if(!$this->sqlAddVDSnapshotMaxCountField()){
            return CPAPIStatus::DBConnectFail;
        }        
        if(!$this->sqlGetUUID($outputUUID))
            return CPAPIStatus::DBConnectFail;
        if(!$this->sqlAddColumnField()){
            return CPAPIStatus::DBConnectFail;
        }
        $this->newDomainClass();                
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);
        $input['DomainName'] = $outputDomain['Name']; 
        $input['Username'] = 'acrosystem';   
        if($input['Username'] != ""){               
            $responsecode = $this->createUser($input,$outputUser);           
            if($responsecode == CPAPIStatus::CreateUserSuccess || $responsecode == CPAPIStatus::Conflict){                
                $input['UserID'] = $outputUser['ID'];                
            }
            else{                
                $logCode = '05300201';                
                goto fail;                
            }
        }
        else
        {            
            $input['UserID'] = null;    
        }                                             
        if($this->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$outputVD)){            
            $logCode = '05300202'; 
            $this->insertCreateVDFailTaskShell($input);
            $rtn = CPAPIStatus::Conflict;          
            goto end; 
        }       
        $newVswitchArr = array();        
        foreach ($input['vswitch'] as $vswitch) {
            $this->sqlListVswitchByid($vswitch,$outputVswitch);            
            if(is_null($outputVswitch)){
                $logCode = '0530020E'; 
                $this->insertCreateVDFailTaskShell($input);
                $rtn = CPAPIStatus::CreateVDofCephFail;          
                goto end; 
            }
            $newVswitchArr[] = $outputVswitch;
        }
        $input['Vswitch'] = $newVswitchArr;
        if(isset($input['ConnectIP'])){                     
            connectDB::$dbh->beginTransaction();              
            $input['UUID'] = $outputUUID['UUID'];          
            //Set State For Task
            $input['State'] = 30;
            if(!$this->sqlInsertVDBase($input)){
                $logCode = '05300206';                
                goto fail_del_vd; 
            }
            $outputVD = null;
            $this->sqlListVDByID($input['UUID'], $outputVD);            
            if(is_null($outputVD)){
                $logCode = '05300206';                
                goto fail_del_vd;  
            }
            $input['VDNumber'] = $outputVD['VDNumber'];               
            $input['NIC_Info']="";    
            if(!isset($input['UEFI']))
                $input['UEFI'] = false;            
            $input['QXL'] = true;
            if(!isset($input['VGACount']))
                $input['VGACount'] = 0;  
            if(!isset($input['CPUType'])){
                $input['CPUType'] = 1;
                $input['DiskType'] = 0;
                $input['DiskCache'] = 0;
            }                
            $input['CpuCt']=2;
            $input['CPU'] = -1;
            $input['RAM']=2048;
            $input['Disk']=4;
            $input['CDRom']='';            
            $input['USBRedirCt']=0;
            $input['Sound']=1;
            $input['USBType'] = 2;
            $input['Suspend'] = false;
            if(!$this->sqlInsertVDInfo($input)){
                $logCode = '05300207';                
                goto fail_del_vd;                  
            }
            $input['Type'] = 1;
            $input['JsonConfig'] = '{}';
            if(!$this->sqlInsertServiceVD($input)){
                $logCode = '05300208';               
                goto fail_del_vd;                 
            }
            $this->sqlListVolumeByVolumeName('1',$outputVolume);
            $input['VolumeID'] = $outputVolume['VolumeID'];
            $input['VDID'] = $input['UUID'];
            if(!$this->sqlInsertVDDisk($input,30)){
                $logCode = '05300209';                
                goto fail_del_vd;                  
            }
            $lastID = connectDB::$dbh->lastInsertId();
            $input['DiskID'] = $lastID;
            // if($input['Username'] != '' && !$this->sqlUpdateUserDefaultVD($input)){
            //     $logCode = '0530020B';                    
            //     goto fail_del_vd;              
            // }
            if(!$this->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$outputVD)){
                $logCode = '0530020A';                
                goto fail_del_vd;   
            }                       
            $input['VDID'] = $outputVD['VDID'];            
            // $this->sqlListVolumeByVolumeID($input['VolumeID'],$outputVolume);
            // $input['VolumeName'] = $outputVolume['VolumeName'];                      
            if($this->createAcroGatewayNewShell($input) != 0){
                $logCode = '0530020C';                
                goto fail_del_vd;       
            }            
            connectDB::$dbh->commit();
            $logCode = '05100101';           
            $rtn = CPAPIStatus::CreateVDofCephSuccess;
            goto end;
        }
        else{
            $logCode = '05300205';
            goto fail;
        } 
        fail:
            $rtn = CPAPIStatus::CreateVDofCephFail;
            $this->insertCreateVDFailTaskShell($input);
            goto end;
        fail_del_vd:                
            connectDB::$dbh->rollBack();
            $rtn = CPAPIStatus::CreateVDofCephFail;
            $this->insertCreateVDFailTaskShell($input);
            goto end;
        end:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertCreateVDLog($logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],$appendErr,true,false,NULL,true);
            return $rtn;
    }

    function createAcroGatewayNewShell($input)
    {
        // var_dump($input);
        $rtn=-99;                
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDTaskGatewayCreate;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.base64_encode('createAG$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['DiskID']).'" ';
            $cmd .= '"'.$input['UUID'].'" ';            
            $cmd .= '"'.$input['VDName'].'" ';  

            foreach ($input['Vswitch'] as $value) {
                $vswitchCmd = 'br';
                $vswitchCmd .= $value['BridgeID'];
                if($value['VLANID'] != 0){
                    $vswitchCmd .= '.'.$value['VLANID'];
                }
                $cmd .= $vswitchCmd.' ';
            }          
            $cmd .= '"'.$input['DomainID'].'" ';  
            // var_dump($cmd);            
            exec($cmd,$output,$rtn);
            // var_dump($rtn);
        }        
        return $rtn;
    }

    function reportVDTaskGatewayCreate($input)
    {      
        $haveTask = false;    
        $cancelTask = false;
        $logCode = '0530020D';
        $rtnCode = 99;
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFilnal;
        }                    
        $this->processReportData($input,$data);       
        // var_dump($data);
        if($data['ErrCode'] == 0){    
            $data['State'] = 0;
            $rtnCode = $this->vdUpdateAll($data);
            if($rtnCode==0)
                $logCode = '05100102';
            goto rtnFilnal;
        }
        else{
            if($data['ErrCode'] == 210){
                $haveTask = true;
                $cancelTask = true;
                $logCode = '05100103';
            }
            $responsecode = $this->deleteVD($data,false);
            if($responsecode == CPAPIStatus::NotFound || $responsecode == CPAPIStatus::DeleteVDSuccess)
                $rtnCode =0;            
            goto rtnFilnal;
        }
        rtnFilnal:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertCreateVDLog($logCode,$data['VDName'],$data['DomainName'],$data['DomainID'],$appendErr,$haveTask,$cancelTask,$data['ErrCode'],true);
            exit($rtnCode);
    }    

    function modifyAcroGatewayVswitch($input)
    {        
        $logCode = "";
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        $result = true;
        foreach ($input['Vswitchs'] as $value) {
            $input['vswitch'] = 'br'.$value['vswitch'];
            $input['mac'] = $value['mac'];
            if($this->modifyAcroGatewayVswitchShell($input,$output,$rtnCode)){                            
                if(!is_null($output))
                    $this->sqlUpdateVDNIC($input['VDID'],json_encode($output));
                $this->sqlUpdateVDModfiyTime($input['VDID']);   
            }                       
            else{
                $result = false;
            }          
        }
        if(!$result){
            return CPAPIStatus::SetAcroGatewayVswitchFail;
        }else{
            return CPAPIStatus::SetAcroGatewayVswitchSuccess;
        }
    }

    function modifyAcroGatewayVswitchShell($input,&$output,&$rtnCode)
    {
        $output=NULL;
        $result = false;
        $rtn=-99;                
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDNICReplace;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);                        
            $cmd .= '"'.$input['VDID'].'" ';
            $cmd .= '"'.$input['mac'].'" ';
            $cmd .= '"'.$input['vswitch'].'" ';            
            exec($cmd,$output,$rtn);           
            // var_dump($output); 
            $rtnCode = $rtn;
            if($rtn == 0){
                $output = json_decode($output[0],true);
                $result = true;
            }            
        }        
        return $result;
    }

    function modifyAcroGatewayVswitchNew($input)
    {        
        $logCode = "";
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        $result = true;
        foreach ($input['Vswitchs'] as $value) {
            $this->sqlListVswitchByid($value['vswitch'],$outputVswitch);
            if(is_null($outputVswitch)){
                $result = false;
                break;
            }
            $input['vswitch'] = 'br';
            $input['vswitch'] .= $outputVswitch['Vswitch']['BridgeID'];
            if($outputVswitch['Vswitch']['VLANID'] != 0){
                $$input['vswitch'] .= '.'.$outputVswitch['Vswitch']['VLANID'];
            }
            // $input['vswitch'] = 'br'.$value['vswitch'];
            $input['mac'] = $value['mac'];
            if($this->modifyAcroGatewayVswitchShell($input,$output,$rtnCode)){                            
                if(!is_null($output))
                    $this->sqlUpdateVDNIC($input['VDID'],json_encode($output));
                $this->sqlUpdateVDModfiyTime($input['VDID']);   
            }                       
            else{
                $result = false;
            }          
        }
        if(!$result){
            return CPAPIStatus::SetAcroGatewayVswitchFail;
        }else{
            return CPAPIStatus::SetAcroGatewayVswitchSuccess;
        }
    }

    function modifyAcroGatewayVswitchNewShell($input,&$output,&$rtnCode)
    {
        $output=NULL;
        $result = false;
        $rtn=-99;                
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDNICReplace;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);                        
            $cmd .= '"'.$input['VDID'].'" ';
            $cmd .= '"'.$input['mac'].'" ';
            $cmd .= '"'.$input['vswitch'].'" ';            
            exec($cmd,$output,$rtn);           
            // var_dump($output); 
            $rtnCode = $rtn;
            if($rtn == 0){
                $output = json_decode($output[0],true);
                $result = true;
            }            
        }        
        return $result;
    }


    function listAcroGateway($input,&$output)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        if(!$this->sqlListAllVswitchsWithBrNameKey($outputVswitchs)){
            return CPAPIStatus::DBConnectFail;
        }
        if(!$this->sqlListServiceVDByidCephidDomain($input['CephID'],$input['DomainID'],$this->vdServiceTypeEnum['AcroGateway'], $outputVD)){
            return CPAPIStatus::NotFound;
        }
        if(count($outputVD) > 0)
            $this->listVDSpecificShell($input,$outputVD,$outputShellVDs);
        foreach ($outputVD as &$vd){     
            $jsonConfig = json_decode($vd['JsonConfig'],true);
            $vd['ExternalIPSetting']=array('vswitch'=>0,'mac'=>'','speed'=>10,'IP'=>'','Mask'=>'','Gateway'=>'');
            $vd['InternalIPSetting']=array('vswitch'=>0,'mac'=>'','speed'=>10,'IP'=>'','Mask'=>'');      
            $vd['DHCPSetting']=array('Enable'=>false,'Subnet'=>'','Mask'=>'','Gateway'=>'','DNS'=>array(),'IPStartRange'=>'','IPEndRange'=>''); 
            $vd['ForwardPortSetting']=array('ExternalIP'=>"",'ExternalStartPort'=>'','IPStart'=>'','IPStartPort'=>'','NumPort'=>0); 
            if(strlen($vd['NIC']) > 0){
                $nic = json_decode($vd['NIC'],true);
                $i=0;
                foreach ($nic as $value) {
                    $value['IP']='';
                    $value['Mask']='';
                    $value['Gateway']='';
                    $value['DNS'] = array();
                    if(array_key_exists($value['br'], $outputVswitchs))
                        $value['vswitch'] = (int)$outputVswitchs[$value['br']]['VswitchID'];
                    else{
                        $value['vswitch'] = NULL;
                    }
                    // $value['vswitch'] = (int)substr($value['br'], 2);
                    if($i==0){
                        if(is_array($jsonConfig) && isset($jsonConfig['ExternalIPSetting'])){
                            foreach ($nic as $value) {                                
                                if($jsonConfig['ExternalIPSetting']['mac'] == $value['mac']){                                    
                                    $value['IP']=$jsonConfig['ExternalIPSetting']['IP'];
                                    $value['Mask']=$jsonConfig['ExternalIPSetting']['Mask'];
                                    $value['Gateway']=$jsonConfig['ExternalIPSetting']['Gateway'];
                                    foreach ($jsonConfig['ExternalIPSetting']['DNS'] as $dns) {
                                        $value['DNS'][] = $dns;
                                    }                                    
                                    break;
                                }                                
                            }
                        }                    
                        $vd['ExternalIPSetting'] = $value;
                    }
                    else if($i==1){     
                        if(is_array($jsonConfig) && isset($jsonConfig['InternalIPSetting'])){
                            foreach ($nic as $value) {
                                if($jsonConfig['InternalIPSetting']['mac'] == $value['mac']){
                                    $value['IP']=$jsonConfig['InternalIPSetting']['IP'];
                                    $value['Mask']=$jsonConfig['InternalIPSetting']['Mask'];                                    
                                    break;
                                }                                
                            }
                        }                    
                        $vd['InternalIPSetting'] = $value;
                    }
                    $i++;                      
                }
                // $nic=array_values($nic);
            }       
            if(is_array($jsonConfig) && isset($jsonConfig['DHCPSetting'])){
                $vd['DHCPSetting'] = $jsonConfig['DHCPSetting'];
            }     
            if(is_array($jsonConfig) && isset($jsonConfig['ForwardPortSetting'])){
                $vd['ForwardPortSetting'] = $jsonConfig['ForwardPortSetting'];
            }
            unset($vd['NIC']);
            unset($vd['JsonConfig']);
            // var_dump($nic);                                
            foreach ($outputShellVDs as $key=>$value) {                      
                $vd['OSState']=1;
                if($vd['VdId'] == $value['vdName']){ 
                    if(strlen($value['vdOwner']) > 0){
                        if($this->listAcroGatewayStateShell(array('ConnectIP'=>$value['vdOwner'],'VDID'=>$vd['VdId']),$outputState)){                            
                            if($outputState == 0)
                                $vd['OSState']=0;
                        }
                    }                                                           
                    $state = $this->changeVDState($value['vdState'],$value['volumeIntegrity']);                    
                    $vd['VDState']=$state;                   
                    unset($outputShellVDs[$key]);                 
                    break;
                }                                
                $state = $this->changeVDState('poweroff','online');
                $vd['VDState']=$state;                
            }                                 
        }
        $output=$outputVD;        
        return CPAPIStatus::ListVDofCephSuccess;
    }

    function listAcroGatewayStateShell($input,&$outputState)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdQemuGA.' gw_qemu_ga_exec ';
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.$input['VDID'].'" '; 
            $cmd .= 'gw_node_status';      
            // var_dump($cmd);      
            exec($cmd,$output,$rtn);       
            // var_dump($output);            
            if($rtn == 0){             
                $output = json_decode($output[0],true);        
                if(isset($output['return']['pid'])){
                    $input['PID'] = $output['return']['pid'];
                    if($this->listQemuGAStatusShell($input,$outputData)){                        
                        if(trim($outputData) == 'Ready'){
                            $outputState = 0;
                        }else{
                            $outputState = 1;
                        }                                 
                        return true;             
                    }
                    else{
                        return false;
                    }

                }else{
                    return false;
                }                
                return;
            }else{
                return false;
            }            
        }
        return false;
    }
    
    function setAcroGatewayIP($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        if(!$this->sqlListServiceVDByidVD($input['VDID'],$outputVD)){
            return CPAPIStatus::NotFound;
        }        
        $vds = array();
        $vds[] = array('VdId'=>$input['VDID']);
        $this->listVDSpecificShell($input,$vds,$outputShellVDs);        
        foreach ($outputShellVDs as $key => $value) {
            if($input['VDID'] == $value['vdName']){                 
                if(strlen($value['vdOwner']) > 0){
                    $jsonConfig = json_decode($outputVD['JsonConfig'],true);
                    connectDB::$dbh->beginTransaction();                    
                    $jsonConfig['ExternalIPSetting'] = $input['ExternalIPSetting'];
                    $jsonConfig['InternalIPSetting'] = $input['InternalIPSetting'];
                    $jsonConfigStr = json_encode($jsonConfig);                    
                    if(!$this->sqlUpdateServiceVDJsonConfig(array('VDID'=>$input['VDID'],'JsonConfig'=>$jsonConfigStr))){
                        connectDB::$dbh->rollBack();
                        return CPAPIStatus::SetAcroGatewayIPFail;
                    }
                    $input['ConnectIP'] = $value['vdOwner'];                    
                    if($this->setAcroGatewayIPShell($input)){
                        connectDB::$dbh->commit();
                        return CPAPIStatus::SetAcroGatewayIPSuccess;
                    }else{
                        connectDB::$dbh->rollBack();
                        return CPAPIStatus::SetAcroGatewayIPFail;
                    }
                }
                else{
                    return CPAPIStatus::SetAcroGatewayIPFail;
                }
            }
        }        
        return CPAPIStatus::SetAcroGatewayIPFail;
    }

    function setAcroGatewayIPShell($input,&$rtnCode)
    {
        $rtnCode=99;
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdQemuGA.' gw_qemu_ga_exec ';
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.$input['VDID'].'" ';
            $cmd .= 'gw_nic_set ';      
            $cmd .= '"'.strtoupper($input['ExternalIPSetting']['mac']).'" ';
            $cmd .= '"'.$input['ExternalIPSetting']['IP'].'" ';
            $cmd .= '"'.$input['ExternalIPSetting']['Mask'].'" ';
            $cmd .= '"'.$input['ExternalIPSetting']['Gateway'].'" ';
            $dns1='';
            $dns2='';
            $i=1;
            foreach ($input['ExternalIPSetting']['DNS'] as $value) {
                if(!is_null($value) && strlen($value) > 0){
                    switch ($i) {
                        case 1:
                            $dns1=$value;
                            break;
                        case 2:   
                            $dns2=$value;                 
                            break;
                    }
                    $i++;
                }
            }
            $cmd .= '"'.$dns1.'" ';
            $cmd .= '"'.$dns2.'" ';
            $cmd .= '"'.strtoupper($input['InternalIPSetting']['mac']).'" ';
            $cmd .= '"'.$input['InternalIPSetting']['IP'].'" ';
            $cmd .= '"'.$input['InternalIPSetting']['Mask'].'" ';
            // var_dump($cmd);      
            exec($cmd,$output,$rtn);    
            // var_dump($rtn);   
            // var_dump($output);            
            if($rtn == 0){             
                $output = json_decode($output[0],true);        
                if(isset($output['return']['pid'])){
                    $input['PID'] = $output['return']['pid'];
                    if($this->listQemuGAStatusShell($input,$outputData,15,$rtnCode)){
                        return true;             
                    }
                    else{
                        return false;
                    }

                }else{
                    return false;
                }                
                return;
            }else{
                return false;
            }            
        }
        return false;
    }

    function setAcroGatewayDHCP($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        if(!$this->sqlListServiceVDByidVD($input['VDID'],$outputVD)){
            return CPAPIStatus::NotFound;
        }        
        $vds = array();
        $vds[] = array('VdId'=>$input['VDID']);
        $this->listVDSpecificShell($input,$vds,$outputShellVDs);        
        foreach ($outputShellVDs as $key => $value) {
            if($input['VDID'] == $value['vdName']){                 
                if(strlen($value['vdOwner']) > 0){
                    $jsonConfig = json_decode($outputVD['JsonConfig'],true);
                    connectDB::$dbh->beginTransaction();                    
                    $jsonConfig['DHCPSetting'] = array('Enable'=>$input['Enable'],'Subnet'=>$input['Subnet'],'Mask'=>$input['Mask'],"Gateway"=>$input['Gateway'],'DNS'=>$input['DNS'],'IPStartRange'=>$input['IPStartRange'],'IPEndRange'=>$input['IPEndRange']);                   
                    $jsonConfigStr = json_encode($jsonConfig);                    
                    if(!$this->sqlUpdateServiceVDJsonConfig(array('VDID'=>$input['VDID'],'JsonConfig'=>$jsonConfigStr))){
                        connectDB::$dbh->rollBack();
                        return CPAPIStatus::SetAcroGatewayDHCPFail;
                    }
                    $input['ConnectIP'] = $value['vdOwner'];                    
                    if($this->setAcroGatewayDHCPShell($input)){
                        connectDB::$dbh->commit();
                        return CPAPIStatus::SetAcroGatewayDHCPSuccess;
                    }else{
                        connectDB::$dbh->rollBack();
                        return CPAPIStatus::SetAcroGatewayDHCPFail;
                    }
                }
                else{
                    return CPAPIStatus::SetAcroGatewayDHCPFail;
                }
            }
        }
        return CPAPIStatus::SetAcroGatewayDHCPFail;
    }

    function setAcroGatewayDHCPShell($input,&$rtnCode)
    {
        $rtnCode=99;
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdQemuGA.' gw_qemu_ga_exec ';
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.$input['VDID'].'" ';
            $cmd .= 'gw_dhcp_set ';      
            $cmd .= $input['Enable'] == true ? '1 ' : '0 ';
            $cmd .= '"'.$input['Subnet'].'" ';
            $cmd .= '"'.$input['Mask'].'" ';
            $cmd .= '"'.$input['Gateway'].'" ';
            $dns1='';
            $dns2='';
            $i=1;
            foreach ($input['DNS'] as $value) {
                switch ($i) {
                    case 1:
                        $dns1=$value;
                        break;
                    case 2:   
                        $dns2=$value;                 
                        break;
                }
                $i++;
            }
            $cmd .= '"'.$dns1.'" ';
            $cmd .= '"'.$dns2.'" ';            
            $cmd .= '"'.$input['IPStartRange'].'" ';
            $cmd .= '"'.$input['IPEndRange'].'" ';
            // var_dump($cmd);      
            exec($cmd,$output,$rtn);    
            // var_dump($rtn);   
            // var_dump($output);            
            if($rtn == 0){             
                $output = json_decode($output[0],true);        
                if(isset($output['return']['pid'])){
                    $input['PID'] = $output['return']['pid'];
                    if($this->listQemuGAStatusShell($input,$outputData,15,$rtnCode)){
                        return true;             
                    }
                    else{
                        return false;
                    }

                }else{
                    return false;
                }                
                return;
            }else{
                return false;
            }            
        }
        return false;
    }

    function setAcroGatewayForwardPort($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        if(!$this->sqlListServiceVDByidVD($input['VDID'],$outputVD)){
            return CPAPIStatus::NotFound;
        }        
        $vds = array();
        $vds[] = array('VdId'=>$input['VDID']);
        $this->listVDSpecificShell($input,$vds,$outputShellVDs);        
        foreach ($outputShellVDs as $key => $value) {
            if($input['VDID'] == $value['vdName']){                 
                if(strlen($value['vdOwner']) > 0){
                    $jsonConfig = json_decode($outputVD['JsonConfig'],true);
                    connectDB::$dbh->beginTransaction();                    
                    $jsonConfig['ForwardPortSetting'] = array('ExternalIP'=>$input['ExternalIP'],'ExternalStartPort'=>$input['ExternalStartPort'],'IPStart'=>$input['IPStart'],"IPStartPort"=>$input['IPStartPort'],'NumPort'=>$input['NumPort']);                   
                    $jsonConfigStr = json_encode($jsonConfig);                    
                    if(!$this->sqlUpdateServiceVDJsonConfig(array('VDID'=>$input['VDID'],'JsonConfig'=>$jsonConfigStr))){
                        connectDB::$dbh->rollBack();
                        return CPAPIStatus::SetAcroGatewayForwardPortFail;
                    }
                    $input['ConnectIP'] = $value['vdOwner'];                    
                    if($this->setAcroGatewayForwardPortShell($input)){
                        connectDB::$dbh->commit();
                        return CPAPIStatus::SetAcroGatewayForwardPortSuccess;
                    }else{
                        connectDB::$dbh->rollBack();
                        return CPAPIStatus::SetAcroGatewayForwardPortFail;
                    }
                }
                else{
                    return CPAPIStatus::SetAcroGatewayForwardPortFail;
                }
            }
        }
        return CPAPIStatus::SetAcroGatewayForwardPortFail;
    }

    function setAcroGatewayForwardPortShell($input,&$rtnCode)
    {
        $rtnCode=99;
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdQemuGA.' gw_qemu_ga_exec ';
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.$input['VDID'].'" ';
            $cmd .= 'gw_forward_port_set ';            
            $cmd .= '"'.$input['IPStart'].'" ';
            $cmd .= '"'.$input['IPStartPort'].'" ';
            $cmd .= '"'.$input['NumPort'].'" ';          
            // var_dump($cmd);      
            exec($cmd,$output,$rtn);    
            // var_dump($rtn);   
            // var_dump($output);            
            if($rtn == 0){             
                $output = json_decode($output[0],true);        
                if(isset($output['return']['pid'])){
                    $input['PID'] = $output['return']['pid'];
                    if($this->listQemuGAStatusShell($input,$outputData,300,$rtnCode)){
                        return true;             
                    }
                    else{
                        return false;
                    }

                }else{
                    return false;
                }                
                return;
            }else{
                return false;
            }            
        }
        return false;
    }

    function listQemuGAStatusShell($input,&$outputData,$retryCount=10,&$rtnCode)
    {
        for($i=0; $i<$retryCount; $i++){
            $cmd = $this->cmdQemuGA.' gw_qemu_ga_exec_status ';
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.$input['VDID'].'" ';
            $cmd .= $input['PID'];              
            exec($cmd,$output,$rtn);              
            if($rtn == 0){
                $output = json_decode($output[0],true);   
                if(isset($output['return']['exited'])){                    
                    if($output['return']['exited']){                        
                        $rtnCode=$output['return']['exitcode'];
                        if($output['return']['exitcode'] == 0){                                                        
                            $outputData = base64_decode($output['return']['out-data']);
                            return true;
                        }else{
                            return false;
                        }
                    }
                }
                else{
                    return false;
                }
            }else{
                return false;
            }
            sleep(1);
        }        
        return false;
    }

    function listVD($input,&$output)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }                  
        if(!$this->sqlListOrgVDByidCephidDomain($input['CephID'],$input['DomainID'], $outputVD))
            return CPAPIStatus::NotFound;     
        if(!$this->sqlListSeedVDByidCephidDomain($input['CephID'],$input['DomainID'], $outputSeedVD))
            return CPAPIStatus::NotFound;                 
        if(!$this->sqlListUserVDByidCephidDomain($input['CephID'],$input['DomainID'], $outputUserVD))
            return CPAPIStatus::NotFound;         
        $this->listVDAllShell($input, $outputVDAll);
        // var_dump($outputVDAll);
        foreach ($outputVD as &$vd){            
            $vdName = $vd['VDID'];
            $i = 0;
            $outputVDInfo= null;
            foreach ($outputVDAll as $key=>$vdInfo){               
                if( $vdName == $vdInfo['vdName']){
                    $i=$key;
                    $outputVDInfo = $vdInfo;                            
                    break;
                }
                $i++;
            }
                  
            if(is_null($outputVDInfo['vdState'])){
                $outputVDInfo['vdOnline'] = false;
                $outputVDInfo['vdState'] = 'notReady';
                $outputVDInfo['volumeIntegrity'] = 'online';
            }
            else{
                unset($outputVDAll[$i]);                
            }
            unset($vd['VDNumber']);            
            $vd['Online']=$outputVDInfo['vdOnline']==1?true:false;                
            if($outputVDInfo['vdDisable'] == 1)
                $outputVDInfo['vdState'] = 'disable';
            $state = $this->changeVDState($outputVDInfo['vdState'],$outputVDInfo['volumeIntegrity']);
            // var_dump($state);
            $vd['State']=$state;  
            $vd['Task'] = $this->taskActionEnum[$outputVDInfo['taskAction']];            
            // $vd['']
                           
        }      
        foreach ($outputUserVD as &$vd){
            $vdName = $vd['VDID'];
            $i = 0;
            $outputVDInfo= null;            
            foreach ($outputVDAll as $key=>$vdInfo){                         
                if( $vdName == $vdInfo['vdName']){
                    $i=$key;
                    $outputVDInfo = $vdInfo;                            
                    break;
                }
            }
            if(is_null($outputVDInfo['vdState'])){                
                $outputVDInfo['vdOnline'] = false;
                $outputVDInfo['vdState'] = 'notReady';
                $outputVDInfo['volumeIntegrity'] = 'online';
            }
            else{
                unset($outputVDAll[$i]);
            }
            unset($vd['VDNumber']);            
            $vd['Online']=$outputVDInfo['vdOnline']==1?true:false;
            if($outputVDInfo['vdDisable'] == 1)
                $outputVDInfo['vdState'] = 'disable';
            $state = $this->changeVDState($outputVDInfo['vdState'],$outputVDInfo['volumeIntegrity']);
            $vd['State']=$state;                         
            $vd['Task'] = $this->taskActionEnum[$outputVDInfo['taskAction']];               
        }       
        foreach ($outputSeedVD as &$vd){  
            $vdName = $vd['VDID'];
            $i = 0;
            foreach ($outputVDAll as $key=>$vdInfo){                         
                if( $vdName == $vdInfo['vdName']){
                    $i=$key;
                    $outputVDInfo = $vdInfo;                            
                    break;
                }
            }
            unset($outputVDAll[$i]);
            unset($vd['VDNumber']);                
            // var_dump($outputVDInfo);
            $vd['State']=$outputVDInfo['seedOverwrite'] == 'yes'? 91 : 90;
            $userVDCount = $this->sqlCheckSeedVDUserVDCount($vd['VDID']);
            if($userVDCount == 0)
                $vd['State'] = 90;
            $vd['Task'] = $this->taskActionEnum[$outputVDInfo['taskAction']];
        }
        $output = array('Org'=>$outputVD,'Seed'=>$outputSeedVD,'User'=>$outputUserVD);
        return CPAPIStatus::ListVDofCephSuccess;
    }    
            
    function sqlCheckSeedVDUserVDCount($idVD)
    {
        $result = 0;
        $sqlList = <<<SQL
            select count(idVD) as userVDCount from tbvdimageset WHERE idSeed=:idSeed;
SQL;
        try
        {             
            $sth = connectDB::$dbh->prepare($sqlList);    
            $sth->bindValue(':idSeed', $idVD, PDO::PARAM_STR);                      
            if($sth->execute())
            {                
                while( $row = $sth->fetch() ) 
                {     
                    $result=$row['userVDCount'];
                }
            }             
        }
        catch (Exception $e){             
        }    
        return $result;
    }

    function listVDAllShell($input,&$output)
    {               
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdVDListAll;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);              
            // var_dump($cmd);   
            exec($cmd,$outputArr,$rtn);
            if($rtn == 0){   
                $output = '';
                foreach ($outputArr as $value) {
                    $output.=$value;
                }          
                $output = json_decode($output,true);
                return true;      
            }            
        }             
        $output = json_decode(array(),true);      
        return false;       
    } 

    function listVDWithSize($input,&$output)
    {        
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }                  
        if(!$this->sqlListOrgVDByidCephidDomain($input['CephID'],$input['DomainID'], $outputVD))
            return CPAPIStatus::NotFound;     
        if(!$this->sqlListSeedVDByidCephidDomain($input['CephID'],$input['DomainID'], $outputSeedVD))
            return CPAPIStatus::NotFound;                 
        if(!$this->sqlListUserVDByidCephidDomain($input['CephID'],$input['DomainID'], $outputUserVD))
            return CPAPIStatus::NotFound;           
        $this->listVDWithSizeAllShell($input, $outputVDAll);        
        $this->setVDAllSize($outputVD,$outputVDAll);        
        $this->setVDAllSize($outputUserVD,$outputVDAll);        
        $this->setVDAllSize($outputSeedVD,$outputVDAll,true);                       
        $output = array('Org'=>$outputVD,'Seed'=>$outputSeedVD,'User'=>$outputUserVD);
        return CPAPIStatus::ListVDofCephSuccess;
    }    
    
    function setVDAllSize(&$listVDs,&$listShellVDs,$isSeed = false)
    {

        foreach ($listVDs as &$vd){   
            $vdName = $vd['VDID'];
            $i = 0;
            $outputVDInfo= null;
            foreach ($listShellVDs as $key=>$vdInfo){                           
                // var_dump($vdInfo);      
                if( $vdName == $vdInfo['vdName']){
                    $i=$key;
                    $outputVDInfo = $vdInfo;                            
                    break;
                }
                $i++;
            }
            
            if(is_null($outputVDInfo['diskSize'])){
                $outputVDInfo['diskSize'] = 0;
                $outputVDInfo['volumeIntegrity'] = 'online';
            }
            else{
                unset($listShellVDs[$i]);                
            }
            unset($vd['VDNumber']);        
            $vd['Online']=$outputVDInfo['vdOnline']==1?true:false;
            if($outputVDInfo['vdDisable'] == 1)
                $outputVDInfo['vdState'] = 'disable';    

            $vd['AllSize']=(int)$outputVDInfo['diskSize'];   
            if($isSeed){
                // var_dump($outputVDInfo);
                $vd['State']=$outputVDInfo['seedOverwrite'] == 'yes'? 91 : 90;
                $userVDCount = $this->sqlCheckSeedVDUserVDCount($vd['VDID']);
                if($userVDCount == 0)
                    $vd['State'] = 90;
            }
            else
                $vd['State']=$this->changeVDState($outputVDInfo['vdState'],$outputVDInfo['volumeIntegrity']);            
            $vd['Task'] = $this->taskActionEnum[$outputVDInfo['taskAction']];                      
        }      
    }

    function listVDWithSizeAllShell($input,&$output)
    {               
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdVDListDiskSizeAll;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);              
            // var_dump($cmd);   
            exec($cmd,$outputArr,$rtn);
            if($rtn == 0){   
                $output = '';
                foreach ($outputArr as $value) {
                    $output.=$value;
                }          
                // var_dump($output);
                $output = json_decode($output,true);
                return true;      
            }            
        }             
        $output = json_decode(array(),true);      
        return false;       
    } 

    function listAllUserDiskWithSize($input,&$output)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        $this->sqlListAllUserDisk($outputUserDisk);
        if(is_null($outputUserDisk))
            return CPAPIStatus::NotFound;
        if(!$this->listUserDiskWithSizeAllShell($input,$outputUserDiskShell)){
            return CPAPIStatus::NotFound;
        }
        foreach ($outputUserDisk as &$disk){   
            $diskName = $disk['DiskName'];
            $i = 0;
            $outputDiskInfo= null;
            foreach ($outputUserDiskShell as $key=>$diskInfo){               
                if( $diskName == $diskInfo['imgName']){
                    $i=$key;
                    $outputDiskInfo = $diskInfo;                            
                    break;
                }
                $i++;
            }
                  
            if(is_null($outputDiskInfo['imgName'])){                
                $outputDiskInfo['diskSize'] = 0;
            }
            else{
                unset($outputUserDiskShell[$i]);                
            }                           
            $disk['AllSize']=(int)$outputDiskInfo['diskSize'];                                       
        }     
        $output = $outputUserDisk;
        return CPAPIStatus::ListVDofCephSuccess;
    }

    function listUserDiskWithSizeAllShell($input,&$output)
    {               
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdVDUserprofileListDiskSizeAll;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);             
            exec($cmd,$output,$rtn);                        
            if($rtn == 0){   
                $output = json_decode($output[0],true);                
                return true;
            }else{
                return false;
            }            
        }
        return false;    
    }

    function listVDInfo($input,&$output)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }                 
        if(!$this->sqlAddColumnField()){
            return CPAPIStatus::DBConnectFail;
        }
        if(!$this->sqlListVDInfoByID($input['VDID'],$outputVD)){
            return CPAPIStatus::NotFound;
        }   
        if(is_null($outputVD['CPUType'])){
            if(!$this->reSyncVDCpuTypeDisk($input,$outputVD)){
                return CPAPIStatus::ListVDInfoFail;
            }
        }
        $this->listVDShell($input,$outputVDShell);        
        // sleep(8);
        $this->sqlListVDDisk($input['VDID'],$outputVDDisks);
        $newVDDisks=array();
        if(!is_null($outputVDShell)){
            foreach ($outputVDDisks as &$disk) {
                if(strpos($disk['Name'], 'ud') === false){
                    foreach ($outputVDShell['disk'] as $diskShell) {
                        if($disk['DiskName'] == $diskShell['diskName']){
                            $disk['UsedSize'] = (int)$diskShell['allocSize'];
                        }
                    }
                    $newVDDisks[] = $disk;
                }                                    
            }
        }
        $outputVD['Disks'] = $newVDDisks;
        if($outputVD['CDRom'] == '')
            $outputVD['CDRom'] = array("Name"=>'None',"Path"=>'none');
        else
            $outputVD['CDRom'] = array("Name"=>$outputVD['CDRom'],"Path"=>$outputVD['CDRom']);
        $output = $outputVD;
        unset($output['VDNumber']);
        return CPAPIStatus::ListVDInfoSuccess;
    }

    function sqlAddColumnField()
    {      
        $result = false;
        $sqlAddField = <<<SQL
            alter table `tbVDiskSet` add column `configVDisk` longtext not null;
            alter table `tbVDImageBaseInfoSet` add column `configCpu` longtext not null;
SQL;
        try
        {             
            if (count(connectDB::$dbh->query("SHOW COLUMNS FROM `tbVDImageBaseInfoSet` LIKE 'configCpu'")->fetchAll())){
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
    
    function reSyncVDCpuTypeDisk($input,&$outputVD)
    {
        $result = false;
        $this->listVDShell($input,$outputVDInfo);
        connectDB::$dbh->beginTransaction();
        $this->sqlDeleteVDDisk($input['VDID']);
        foreach ($outputVDInfo['disk'] as $value) {
            $input['DiskName'] = $value['diskName'];
            $input['Disk'] = $value['totalSize']/(1024*1024*1024);
            $input['DiskCache'] = $value['hdCache'] == 'writethrough' ? 0 : 1;
            switch ($value['hdBus']) {
                case 'ide':
                    $input['DiskType'] = 0;
                    break;
                case 'scsi':
                    $input['DiskType'] = 1;
                    break;
                case 'virtio':
                    $input['DiskType'] = 2;
                    break;
                default:
                    $input['DiskType'] = 0;
                    break;
            }
            if(!$this->sqlInsertVDDisk($input)){
                connectDB::$dbh->rollBack();
                return $result;
            }
        }         
        $input['CPUType'] = $outputVDInfo['cpuType'] == 'hv' ? 1 : 0;
        $outputVD['CPUType'] = $input['CPUType'];
        if(!$this->sqlUpdateVDCPUType($input)){
            connectDB::$dbh->rollBack();
            return $result;
        }
        $result = true;
        connectDB::$dbh->commit();
        // var_dump($result);
        return $result;
    }

    function listVDWithSSShell($input,&$output)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdVDListWithSS;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.$input['VDID'].'" '; 
            // var_dump($cmd);
            exec($cmd,$output,$rtn);    
            // var_dump($output);
            if($rtn == 0){                     
                $output = json_decode($output[0],true);                
                return;
            }            
        }           
    }

    function listVDShell($input,&$output)
    {                
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdVDList;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.$input['VDID'].'" ';             
            exec($cmd,$output,$rtn);       
            // var_dump($output);         
            if($rtn == 0){                     
                $output = json_decode($output[0],true);                
                return;
            }            
        }               
    } 

    function vdUpdateAll($input,$updateSnapshot=false,$updateVDTime=true)
    {        
        $rtnCode = 99;
        // var_dump($input);
        connectDB::$dbh->beginTransaction();
        if($updateVDTime){
            if(!$this->sqlUpdateVDStatus($input)){
                $rtnCode = 1;
                goto finalReturn;
            }
        }
        else{
            if(!$this->sqlUpdateVDStatusNoTime($input)){
                $rtnCode = 1;
                goto finalReturn;
            }
        }
        $rtnCode = $this->vdUpdateNIC($input);
        // var_dump("nic:".$rtnCode);
        if($rtnCode != 0){            
            goto finalReturn;
        }        
        $rtnCode = $this->vdUpdateDisk($input);
        if($rtnCode != 0){            
            goto finalReturn;
        }              
        if($updateSnapshot){
            $rtnCode = $this->vdUpdateSnapshot($input);
            if($rtnCode != 0)            
                goto finalReturn;        
        }
        // var_dump("disk:".$rtnCode);
        goto finalReturn;    
        finalReturn:
            if($rtnCode != 0)
                connectDB::$dbh->rollBack();
            else
                connectDB::$dbh->commit();
            return $rtnCode;
    }

    function vdUpdateNIC($input)
    {
        // var_dump($input);
        $rtnCode = 0;        
        if(!$this->sqlUpdateVDNIC($input['VDID'],$input['NIC'])){
            $rtnCode = 2;           
        }   
        return $rtnCode;
    }

    function vdUpdateDisk($input)
    {
        $rtnCode = 99;
        if(!$this->sqlListVDDisk($input['VDID'],$outputDisks)){
            $rtnCode = 3;
            goto finalReturn;   
        }
        // var_dump($input);
        // var_dump($outputDisks);
        if(sizeof($outputDisks) == sizeof($input['Disks'])){
            // var_dump($input['Disks']);
            foreach ($outputDisks as $key=>$value) {
                // var_dump($input['Disks'][$key]['diskName']);
                $diskSize = $value['Size']*1024*1024;
                if($value['DiskName'] != $input['Disks'][$key]['diskName'] || $diskSize != $input['Disks'][$key]['totalSize']){
                    $diskSize = $input['Disks'][$key]['totalSize']/(1024*1024*1024);
                    $data['DiskID'] = $value['DiskID'];
                    $data['Size'] = $diskSize;
                    $data['State'] = 0;
                    $data['DiskName'] = $input['Disks'][$key]['diskName'];
                    $data['DiskCache'] = $input['Disks'][$key]['hdCache'] == 'writethrough' ? 0 : 1;
                    switch ($input['Disks'][$key]['hdBus']) {
                        case 'ide':
                            $data['DiskType'] = 0;
                            break;
                        case 'scsi':
                            $data['DiskType'] = 1;
                            break;
                        case 'virtio':
                            $data['DiskType'] = 2;
                            break;
                        default:
                            $data['DiskType'] = 0;
                            break;
                    }                                        
                    if(!$this->sqlUpdateVDDisk($data)){
                        $rtnCode = 4;
                        goto finalReturn;
                    }                        
                }
            }
        }
        else{            
            // echo 'delete';
            if(!$this->sqlDeleteVDDisk($input['VDID'])){
                $rtnCode = 5;
                goto finalReturn;
            }            
            foreach ($input['Disks'] as $value) {                
                $diskSize = $value['totalSize']/(1024*1024*1024);
                $input['Disk'] = $diskSize;
                $input['DiskName'] = $value['diskName'];
                $input['DiskCache'] = $value['hdCache'] == 'writethrough' ? 0 : 1;
                $this->sqlListVolumeByVolumeName($value['diskVolumeId'],$outputVolume);
                $input['VolumeID'] = $outputVolume['VolumeID'];
                switch ($value['hdBus']) {
                    case 'ide':
                        $input['DiskType'] = 0;
                        break;
                    case 'scsi':
                        $input['DiskType'] = 1;
                        break;
                    case 'virtio':
                        $input['DiskType'] = 2;
                        break;
                    default:
                        $input['DiskType'] = 0;
                        break;
                }                       
                if(!$this->sqlInsertVDDisk($input)){
                    $rtnCode = 6;
                    goto finalReturn;
                }
            }    
        }
        $rtnCode = 0;
        goto finalReturn;
        finalReturn:            
            return $rtnCode;
    }

    function vdUpdateSnapshot($input)
    {
        $rtnCode = 99;
        $snapC = new SnapshotAction();
        $output = $snapC->sqlListSnapshotByid($input['OrgVDID']);
        // var_dump($output);
        if(is_null($output)){
            $rtnCode = 7;
                goto finalReturn;
        }            
        foreach ($output as $value) {
            if(!$this->sqlGetUUID($outputUUID)){
                $rtnCode = 8;
                goto finalReturn;
            }          
            if($value['Layer'] == $value['UpperLayer']){
                $data['Layer'] = $outputUUID['UUID'];
                $data['UpperLayer'] = $outputUUID['UUID'];                
            }else{
                $data['Layer'] = $outputUUID['UUID'];
                $data['UpperLayer'] = $upperLayer;
            }            
            $upperLayer = $data['Layer'];
            $data['VDID'] = $input['VDID'];
            $data['LayerDesc'] = $value['Desc'];
            $data['LogicalLayer'] = $value['LogicalLayer'];
            $data['CreateTime'] = $value['CreateTime'];
            $data['Size']=$value['Size'];
            if(!$snapC->sqlInsertSanpshotLayer($data)){
                $rtnCode = 9;
                goto finalReturn;
            }                
        }
        $rtnCode = 0;
        goto finalReturn;
        finalReturn:            
            return $rtnCode;
    }

    function addSnapshot($input,$isRestore,$isVDisk=false)
    {
        $rtnCode = 99;
        $snapC = new SnapshotAction();       
        foreach ($input['SS'] as $key=>$value) {
            if(!$this->sqlGetUUID($outputUUID)){
                $rtnCode = 8;
                goto finalReturn;
            }          
            if($key == 0){
                $data['Layer'] = $outputUUID['UUID'];
                $data['UpperLayer'] = $outputUUID['UUID'];                
            }else{
                $data['Layer'] = $outputUUID['UUID'];
                $data['UpperLayer'] = $upperLayer;
            }            
            $upperLayer = $data['Layer'];
            if($isVDisk)
                $data['DiskID'] = $input['DiskID'];
            else
                $data['VDID'] = $input['VDID'];            
            $data['LayerDesc'] = '';
            $data['LogicalLayer'] = $value['ssName'];
            $data['Size'] = $value['ssSize'];
            $data['CreateTime'] = date('Y-m-d H:i:s',$value['ssName']);
            if(isset($value['reportMeta'])){
                $metadata = base64_decode($value['reportMeta']);
                $metadataArr = explode('$*', $metadata);
                if(isset($metadataArr[6])){
                    $data['LayerDesc'] = base64_decode($metadataArr[6]);           
                }      
            }
            if(!$snapC->sqlInsertSanpshotLayer($data,$isVDisk)){
                $rtnCode = 9;
                goto finalReturn;
            }                
            $lastTime = $data['CreateTime'];
        }
        if($isRestore){   
            if(isset($input['RecoveryTime']))
                $lastTime=$input['RecoveryTime'];  
            // var_dump($input);   
            // var_dump($lastTime);
            if($isVDisk)
                $updateID = $input['DiskID'];
            else
                $updateID = $input['VDID'];
            $this->sqlUpdateVDRecoveryTime($updateID,$lastTime,$isVDisk);
        }
        $rtnCode = 0;
        goto finalReturn;
        finalReturn:            
            return $rtnCode;
    }

    function createVD($input,&$output)
    {        
        // var_dump($input);
        $logCode = "";
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }            
        if(!$this->sqlAddVDSnapshotMaxCountField()){
            return CPAPIStatus::DBConnectFail;
        }        
        if(!$this->sqlGetUUID($outputUUID))
            return CPAPIStatus::DBConnectFail;
        if(!$this->sqlAddColumnField()){
            return CPAPIStatus::DBConnectFail;
        }        
        if(isset($input['NodeID'])){
            $nodeC = new NodeAction();
            $nodeC->sqlListServerByidVDServer($input['NodeID'],$outputNode);
            if(is_null($outputNode)){
                return CPAPIStatus::CreateVDofCephFail;
            }
            $input['PreferNodeAddress'] = $outputNode['CommunicateIP'];
        }
        $this->newDomainClass();                
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);
        $input['DomainName'] = $outputDomain['Name'];    
        if($input['Username'] != ""){               
            $responsecode = $this->createUser($input,$outputUser);           
            if($responsecode == CPAPIStatus::CreateUserSuccess || $responsecode == CPAPIStatus::Conflict){                
                $input['UserID'] = $outputUser['ID'];                
            }
            else{                
                $logCode = '05300201';                
                goto fail;                
            }
        }
        else
        {            
            $input['UserID'] = null;    
        }                                             
        if($this->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$outputVD)){            
            $logCode = '05300202'; 
            $this->insertCreateVDFailTaskShell($input);
            $rtn = CPAPIStatus::Conflict;          
            goto end; 
        }       
        $this->sqlListVswitchByid($input['Vswitch'],$outputVswitch);
        if(is_null($outputVswitch)){
            $logCode = '0530020E'; 
            $this->insertCreateVDFailTaskShell($input);
            $rtn = CPAPIStatus::CreateVDofCephFail;          
            goto end; 
        }
        if(isset($input['ConnectIP'])){                     
            connectDB::$dbh->beginTransaction();              
            $input['UUID'] = $outputUUID['UUID'];          
            //Set State For Task
            $input['State'] = 30;
            if(!$this->sqlInsertVDBase($input)){
                $logCode = '05300206';                
                goto fail_del_vd; 
            }
            $outputVD = null;
            $this->sqlListVDByID($input['UUID'], $outputVD);            
            if(is_null($outputVD)){
                $logCode = '05300206';                
                goto fail_del_vd;  
            }
            $input['VDNumber'] = $outputVD['VDNumber'];               
            $input['NIC_Info']="";    
            if(!isset($input['UEFI']))
                $input['UEFI'] = false;            
            $input['QXL'] = true;
            if(!isset($input['VGACount']))
                $input['VGACount'] = 0;  
            if(!isset($input['CPUType'])){
                $input['CPUType'] = 1;
                $input['DiskType'] = 0;
                $input['DiskCache'] = 0;
            }                
            if(!$this->sqlInsertVDInfo($input)){
                $logCode = '05300207';                
                goto fail_del_vd;                  
            }
            if(!$this->sqlInsertVDOrg($input)){
                $logCode = '05300208';               
                goto fail_del_vd;                 
            }
            $input['VDID'] = $input['UUID'];
            if(!$this->sqlInsertVDDisk($input,30)){
                $logCode = '05300209';                
                goto fail_del_vd;                  
            }
            $lastID = connectDB::$dbh->lastInsertId();
            $input['DiskID'] = $lastID;
            if($input['Username'] != '' && !$this->sqlUpdateUserDefaultVD($input)){
                $logCode = '0530020B';                    
                goto fail_del_vd;              
            }
            if(!$this->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$outputVD)){
                $logCode = '0530020A';                
                goto fail_del_vd;   
            }                       
            $input['VDID'] = $outputVD['VDID'];            
            $this->sqlListVolumeByVolumeID($input['VolumeID'],$outputVolume);
            $input['VolumeName'] = $outputVolume['VolumeName'];    
            $input['Vswitch'] = $outputVswitch;        
            if($this->createVDShell($input) != 0){
                $logCode = '0530020C';                
                goto fail_del_vd;       
            }            
            connectDB::$dbh->commit();
            $logCode = '05100101';           
            $rtn = CPAPIStatus::CreateVDofCephSuccess;
            goto end;
        }
        else{
            $logCode = '05300205';
            goto fail;
        } 
        fail:
            $rtn = CPAPIStatus::CreateVDofCephFail;
            $this->insertCreateVDFailTaskShell($input);
            goto end;
        fail_del_vd:                
            connectDB::$dbh->rollBack();
            $rtn = CPAPIStatus::CreateVDofCephFail;
            $this->insertCreateVDFailTaskShell($input);
            goto end;
        end:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertCreateVDLog($logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],$appendErr);
            return $rtn;
    }            

    /*
        reportMetaData errorCode addingTime allDisk(base64) allNIC(base64)
        MetaData : create$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['DiskID']
     */
    function reportVDTaskOrgCreate($input)
    {      
        $haveTask = false;    
        $cancelTask = false;
        $logCode = '0530020D';
        $rtnCode = 99;
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFilnal;
        }                    
        $this->processReportData($input,$data);       
        // var_dump($data);
        if($data['ErrCode'] == 0){    
            $data['State'] = 0;
            $rtnCode = $this->vdUpdateAll($data);
            if($rtnCode==0)
                $logCode = '05100102';
            goto rtnFilnal;
        }
        else{
            if($data['ErrCode'] == 210){
                $haveTask = true;
                $cancelTask = true;
                $logCode = '05100103';
            }
            $responsecode = $this->deleteVD($data,false);
            if($responsecode == CPAPIStatus::NotFound || $responsecode == CPAPIStatus::DeleteVDSuccess)
                $rtnCode =0;            
            goto rtnFilnal;
        }
        rtnFilnal:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertCreateVDLog($logCode,$data['VDName'],$data['DomainName'],$data['DomainID'],$appendErr,$haveTask,$cancelTask,$data['ErrCode']);
            exit($rtnCode);
    }        

    function insertCreateVDLog($code,$vdName,$domainName,$domainID,$appendErr,$haveTask=true,$cancelTask=false,$errCode=NULL,$isAcroGateway=false)
    {
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $taskStr = "";
        if($haveTask)
            $taskStr = " task";
        if($isAcroGateway){
            $vdStr = 'AcroGateway';
        }else{
            $vdStr = 'Virtual Host';
        }
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);   
        if(is_null($appendErr)){
            if($cancelTask)
                $message = "Cancel Create $vdStr$appendVDDomain$taskStr";
            else
                $message = "Create $vdStr$appendVDDomain$taskStr Success";
        }
        else{
            $message = "Failed to Create $vdStr$appendVDDomain$taskStr$appendErr";
            if(isset($errCode)){
                $rtnMsg = $this->changeModifyReturnCode($errCode);
                $message .= "($rtnMsg)";
            }
        }
        // var_dump($message);
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }
    
    function insertCreateVDFailTaskShell($input)
    {
        $cmd = $this->cmdVDTaskErrTask;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= '"'.base64_encode('create$*'.$input['CephID'].'$*VDID$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*DiskID').'" ';
        $cmd .= 'orgCreate';
        exec($cmd,$output,$rtn);
    }

    function createVDShell($input)
    {
        $rtn=-99;                
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDTaskOrgCreate;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.base64_encode('create$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['DiskID']).'" ';
            $cmd .= '"'.$input['UUID'].'" ';
            $cmd .= $input['CpuCt'].' ';
            $cmd .= ($input['RAM']*1024).' ';
            $cmd .= $input['Disk'].' ';
            $cmd .= '"'.$input['VolumeName'].'" ';
            $cdrom = '';
            if(strlen($input['CDRom']) > 0 && strtolower($input['CDRom']) != 'none')
                $cdrom = $input['CDRom'];
            $cmd .= '"'.$cdrom.'" ';
            $cmd .= '"'.$input['VDName'].'" ';
            $cmd .= $input['USBType'].' ';
            $cmd .= $input['USBRedirCt'].' ';
            switch($input['Sound']){
                case 1:
                    $sound = 'ich6';
                    break;
                case 2:
                    $sound = 'ac97';
                    break;
            }
            $cmd .= '"'.$sound.'" ';       
            $vswitchCmd = 'br';
            $vswitchCmd .= $input['Vswitch']['BridgeID'];
            if($input['Vswitch']['VLANID'] != 0){
                $vswitchCmd .= '.'.$input['Vswitch']['VLANID'];
            }
            $cmd .= $vswitchCmd.' ';
            $cmd .= $input['NIC'].' ';            
            if($input['CPU'] == 0)
                $input['CPU'] = -1;
            $cmd .= $input['CPU'].' ';
            $suspend = $input['Suspend'] == false ? 0 : 1;
            $cmd .= $suspend.' ';                           
            $cmd .= '"'.$input['UUID'].'" ';
            $cpuType = $input['CPUType'] == 0 ? 'fv' : 'hv';
            $cmd .= $cpuType.' ';            
            switch ($input['DiskType']) {
                case 0:
                    $diskType = 'ide';
                    break;
                case 1:
                    $diskType = 'scsi';
                    break;
                case 2:
                    $diskType = 'virtio';
                    break;
                default:
                    $diskType = 'ide';
                    break;
            }
            $cmd .= $diskType.' ';
            $diskCache = $input['DiskCache'] == 0 ? 'writethrough' : 'writeback';
            $cmd .= $diskCache.' ';
            $cmd .= $input['DomainID'].' ';                    
            $uefi = $input['UEFI'] == true ? 'uefi':'legacy';
            $cmd .= $uefi.' ';
            $vgaCount = $input['VGACount'];            
            $cmd .= $vgaCount.' ';
            $cmd .= $input['PreferNodeAddress'] == NULL ? '""' : '"'.$input['PreferNodeAddress'].'"';
            // $cmd .= '>/dev/null';
            // var_dump($cmd);
            exec($cmd,$output,$rtn);
        }        
        return $rtn;
    }

    function createSeedVD($input)
    {
        $logCode = "";
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }     
        if(!$this->sqlGetUUID($outputUUID))
            return CPAPIStatus::DBConnectFail;                 
        $this->newDomainClass();        
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);                                    
        if(!$this->sqlListVDInfoByID($input['VDID'],$outputInfo)){            
            $logCode = '05300402';
            goto fail;                       
        }        
        if($this->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$outputVD)){
            $logCode = '05300401';  
            $rtn = CPAPIStatus::Conflict;  
            $this->insertCreateSeedVDFailTaskShell($outputInfo,$input);        
            goto end;    
        }     
        $outputInfo['VDID'] = $input['VDID'];
        $input['DomainName'] = $outputDomain['Name'];        
        $input['CPU']=$outputInfo['CPU'];
        $input['CpuCt']=$outputInfo['CpuCt'];
        $input['RAM']=$outputInfo['RAM'];
        $input['NIC_Info'] = '';
        $input['Suspend']=$outputInfo['Suspend'];      
        $input['USBType']=$outputInfo['USBType'];       
        $input['USBRedirCt']=$outputInfo['USBRedirCt'];
        $input['CDRom']=$outputInfo['CDRom'];
        $input['Sound']=$outputInfo['Sound'];   
        $input['CPUType']=$outputInfo['CPUType'];
        $input['SSLayerMax']=$outputInfo['SSLayerMax'];
        $input['UEFI']=$outputInfo['UEFI'];
        $input['QXL']=$outputInfo['QXL'];
        $input['VGACount']=$outputInfo['VGACount'];
        $input['ViewerProtocol'] = $outputInfo['ViewerProtocol'];
        if(!$this->sqlListVDDisk($input['VDID'], $outputDisk)){
            $logCode = '05300405';
            goto fail;
        }                          
        if(isset($input['ConnectIP'])){
            connectDB::$dbh->beginTransaction();                        
            $input['UUID'] = $outputUUID['UUID'];            
            $input['State'] = 30;
            if(!$this->sqlInsertVDBase($input)){
                $logCode = '05300407';
                goto fail_del_vd;             
            }                        
            if(!$this->sqlListVDByID($input['UUID'], $outputVD)){
                $logCode = '05300407';
                goto fail_del_vd;   
            }
            if(!$this->sqlInsertVDInfo($input)){
                $logCode = '05300408';
                goto fail_del_vd;
            }
            $diskInsert = array('VDID'=>$input['UUID'],'CephID'=>$input['CephID'],'DomainID'=>$input['DomainID']);
            foreach ($outputDisk as $disk) {
                $diskInsert['Disk'] = $disk['Size'];
                $diskInsert['VolumeID'] = $disk['VolumeID'];
                if(!$this->sqlInsertVDDisk($diskInsert,30)){
                    $logCode = '0530040A';
                    goto fail_del_vd; 
                }                             
            }            
            if(!$this->sqlInsertVDSeed($input)){
                $logCode = '05300409';
                goto fail_del_vd;                                  
            }                        
            if(!$this->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$outputSeedVD)){
                $logCode = '05300409';
                goto fail_del_vd;   
            }
            $input['VDNumber'] = $outputSeedVD['VDNumber'];     
            $input['VDID'] = $outputSeedVD['VDID'];               
            $rtn = $this->createSeedVDShell($outputInfo,$input);
            if($rtn != 0){
                $logCode = '0530040B';
                goto fail_del_vd;
            }
            unset($input['UUID']);
            unset($input['VDNumber']);
            unset($input['CreateIP']);
            unset($input['NIC_Info']);
            $output = $input;
            connectDB::$dbh->commit();            
            $rtn = CPAPIStatus::CreateSeedSuccess;
            $logCode = '05100301';
            goto end;
        }
        else{
            $logCode = '05300403';            
            goto fail;
        }
        fail:
            $rtn = CPAPIStatus::CreateSeedFail;
            $this->insertCreateSeedVDFailTaskShell($outputInfo,$input);
            goto end;
        fail_del_vd:                
            connectDB::$dbh->rollBack();           
            $rtn = CPAPIStatus::CreateSeedFail;
            $this->insertCreateSeedVDFailTaskShell($outputInfo,$input);
            goto end;
        end:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertCreateSeedVDLog($logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],$appendErr);
            return $rtn;    
    }        
    
    /*
        reportMetaData errorCode addingTime allDisk(base64) allNIC(base64)
        MetaData : seed$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$inputOrg['VDID'].'$*'.$inputOrg['VDName']
     */
    function reportVDTaskSeedCreate($input)
    {          
        $haveTask = false;    
        $cancelTask = false;
        $logCode = '0530040C';
        $rtnCode = 99;
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFilnal;
        }                    
        $this->processReportData($input,$data);        
        $newNICs = json_decode($data['NIC'],true);        
        foreach ($newNICs as &$value) {            
            $value['mac']='';            
        }
        $data['NIC'] = json_encode($newNICs);
        // var_dump($data['NIC']);
        if($data['ErrCode'] == 0){     
            $rtnCode = $this->vdUpdateAll($data);
            if($rtnCode == 0)
                $logCode = '05100302';
            goto rtnFilnal;
        }
        else{
            if($data['ErrCode'] == 210){
                $haveTask = true;
                $cancelTask = true;
                $logCode = '05100303';
            }
            $responsecode = $this->deleteVD($data,false);
            if($responsecode == CPAPIStatus::NotFound || $responsecode == CPAPIStatus::DeleteVDSuccess)
                $rtnCode =0;
            goto rtnFilnal;
        }
        rtnFilnal:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertCreateSeedVDLog($logCode,$data['VDName'],$data['DomainName'],$data['DomainID'],$appendErr,$haveTask,$cancelTask,$data['ErrCode']);
            exit($rtnCode);
    }


    function insertCreateSeedVDLog($code,$vdName,$domainName,$domainID,$appendErr,$haveTask=true,$cancelTask=false,$errCode=NULL)
    {
        $logType = 5;
        $logLevel = 3;        
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $taskStr = "";
        if($haveTask)
            $taskStr = " task";
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);   
        if(is_null($appendErr)){
            if($cancelTask)
                $message = "Cancel Make Seed$appendVDDomain$taskStr.";
            else
                $message = "Make Seed$appendVDDomain$taskStr Success";
        }
        else{
            $message = "Failed to make Seeed$appendVDDomain$taskStr$appendErr";
            if(isset($errCode)){
                $rtnMsg = $this->changeModifyReturnCode($errCode);
                $message .= "($rtnMsg)";
            }
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function insertOverwriteSeedVDLog($code,$vdName,$domainName,$domainID,$appendErr,$haveTask=true,$cancelTask=false,$errCode=NULL)
    {
        $logType = 5;
        $logLevel = 3;        
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $taskStr = "";
        if($haveTask)
            $taskStr = " task";
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);   
        if(is_null($appendErr)){
            if($cancelTask)
                $message = "Cancel Overwrite Seed$appendVDDomain$taskStr.";
            else
                $message = "Overwrite Seed$appendVDDomain$taskStr Success";
        }
        else{
            $message = "Failed to Overwrite Seeed$appendVDDomain$taskStr$appendErr";
            if(isset($errCode)){
                $rtnMsg = $this->changeModifyReturnCode($errCode);
                $message .= "($rtnMsg)";
            }
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function insertCreateSeedVDFailTaskShell($inputOrg,$input)
    {
        $cmd = $this->cmdVDTaskErrTask;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= '"'.base64_encode('seed$*'.$input['CephID'].'$*VDID$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*ORGVDID$*'.$inputOrg['VDName']).'" '; 
        $cmd .= 'seedCreate';
        exec($cmd,$output,$rtn);
    }

    function createSeedVDShell($inputOrg,$input)
    {        
        // var_dump($inputOrg);
        // var_dump($input);
        $rtn=-99;            
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDTaskSeedCreate;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd); 
            $cmd .= '"'.base64_encode('seed$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$inputOrg['VDID'].'$*'.$inputOrg['VDName']).'" ';            
            $cmd .= '"'.$inputOrg['VDID'].'" ';
            $cmd .= '"'.$input['VDID'].'" ';            
            // $desc = str_replace('"','\"',$inpu['Desc']);
            $vdName = '"'.$input['VDName'].'"';
            $cmd .= $vdName.' ';
            $cmd .= $input['DomainID'];             
            exec($cmd,$output,$rtn);                             
        }            
        return $rtn;
    }

    function insertOverwriteSeedVDFailTaskShell($inputOrg,$input)
    {
        $cmd = $this->cmdVDTaskErrTask;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= '"'.base64_encode('reseed$*'.$input['CephID'].'$*VDID$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*ORGVDID$*'.$inputOrg['VDName']).'" '; 
        $cmd .= 'seedOverwrite';
        exec($cmd,$output,$rtn);
    } 

    function overwriteSeedVDShell($inputOrg,$input)
    {        
        // var_dump($inputOrg);
        // var_dump($input);
        $rtn=-99;            
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDTaskSeedOverwrite;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd); 
            $cmd .= '"'.base64_encode('reseed$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$inputOrg['VDID'].'$*'.$inputOrg['VDName']).'" ';            
            $cmd .= '"'.$inputOrg['VDID'].'" ';
            $cmd .= '"'.$input['VDID'].'" ';            
            // $desc = str_replace('"','\"',$inpu['Desc']);
            $vdName = '"'.$input['VDName'].'"';
            $cmd .= $vdName.' ';
            $cmd .= $input['DomainID'];     
            // var_dump($cmd);        
            exec($cmd,$output,$rtn);  
            // var_dump($rtn);                           
        }            
        return $rtn;
    }

    function bornUserVD($input)
    {                   
        $logCode = '';
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }     
        $log = function ($logCode,$input,$isRollback=false,$outputInfo=NULL){            
            if($isRollback){
                connectDB::$dbh->rollBack();                
            }
            if($logCode != '05300603' && $logCode != '05100501')
                $this->insertBornUserVDFailTaskShell($outputInfo,$input);
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertBornUserVDLog($logCode,$input['VDName'],$input['DomainName'],$input['DomainID'],$appendErr);
        };        
        $this->newDomainClass();        
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);
        if(!$this->sqlListVDInfoByID($input['VDID'],$outputInfo)){
                $logCode = '05300603';
                $log($logCode,$input);
                return;
                // goto fail;         
        }
        $outputInfo['VDID'] = $input['VDID'];
        $input['DomainName']=$outputDomain['Name'];
        $input['VDNumber']=$outputInfo['VDNumber'];
        $input['CephID']=$outputInfo['CephID'];
        $input['CPU']=$outputInfo['CPU'];
        $input['CpuCt']=$outputInfo['CpuCt'];
        $input['RAM']=$outputInfo['RAM'];
        $input['NIC_Info'] = json_decode($outputInfo['NIC'],true);
        $input['Suspend']=$outputInfo['Suspend'];        
        $input['USBType']=$outputInfo['USBType'];
        $input['USBRedirCt']=$outputInfo['USBRedirCt'];
        $input['CDRom']=$outputInfo['CDRom'];
        $input['Sound']=$outputInfo['Sound'];      
        $input['CPUType']=$outputInfo['CPUType'];                 
        $input['SSLayerMax']=$outputInfo['SSLayerMax'];
        $input['VGACount']=$outputInfo['VGACount'];
        $input['UEFI']=$outputInfo['UEFI'];
        $input['QXL']=$outputInfo['QXL'];
        $input['ViewerProtocol'] = $outputInfo['ViewerProtocol'];
        if(!$this->sqlListVDDisk($input['VDID'], $outputDisk)){
            $logCode = '05300606';
            $log($logCode,$input,false,$outputInfo);
            return;                         
        }                                 
        foreach ($input['VDs'] as $value) {  
            if(trim($value['Username']) == '')
                continue;       
            if(!$this->sqlGetUUID($outputUUID))
                continue;   
            $input['VDName'] = $value['VDName']; 
            $input['Username'] = $value['Username'];
            // var_dump($input);
            if($this->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$output_vd)){
                $logCode = '05300601';
                $rtn = CPAPIStatus::Conflict;    
                $log($logCode,$input,false,$outputInfo);
                // echo 'duplicated';
                continue;
                // goto end;  
            }                    
            // var_dump($input['Username']);
            if($input['Username'] != ""){                
                $responsecode = $this->createUser($input,$outputUser);    
                // var_dump($outputUser);        
                if($responsecode == CPAPIStatus::CreateUserSuccess || $responsecode == CPAPIStatus::Conflict){                
                    $input['UserID'] = $outputUser['ID'];                
                }
                else{
                    $logCode = '05300602';
                    $log($logCode,$input,false,$outputVDInfo);
                    
                    continue;
                    // goto fail;  
                }
            }
            else
            {            
                $input['UserID'] = null;
            }                                              
            
            if(isset($input['ConnectIP'])){                     
                connectDB::$dbh->beginTransaction();              
                $input['UUID'] = $outputUUID['UUID'];
                $input['State'] = 30;
                if(!$this->sqlInsertVDBase($input)){
                    $logCode = '05300608';
                    $log($logCode,$input,true,$outputInfo);
                    continue;
                }                         
                if(!$this->sqlListVDByID($input['UUID'], $output_vd)){
                    $logCode = '05300608';
                    $log($logCode,$input,true,$outputInfo);
                    continue;
                }
                if(!$this->sqlInsertVDInfo($input)){
                    $logCode = '05300609';
                    $log($logCode,$input,true,$outputInfo);
                    continue;             
                }                       
                //modify to seed id
                $input['VDID'] = $outputInfo['VDID'];
                if(!$this->sqlInsertVDUser($input)){
                    $logCode = '0530060A';
                    $log($logCode,$input,true,$outputInfo);
                    continue;               
                }            
                $diskInsert = array('VDID'=>$input['UUID'],'CephID'=>$input['CephID'],'DomainID'=>$input['DomainID']);
                foreach ($outputDisk as $disk) {                    
                    $diskInsert['Disk'] = $disk['Size'];
                    $diskInsert['VolumeID'] = $disk['VolumeID'];
                    if(!$this->sqlInsertVDDisk($diskInsert,30)){
                        $logCode = '0530060B';
                        $log($logCode,$input,true,$outputInfo);
                        continue;
                    }                             
                }            
                if($input['Username'] != '' && !$this->sqlUpdateUserDefaultVD($input)){
                    $logCode = '0530060C';
                    $log($logCode,$input,true,$outputInfo);
                    continue;                                          
                }            
                if(!$this->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$output_vd)){
                    $logCode = '0530060D';
                    $log($logCode,$input,true,$outputInfo);
                    continue; 
                }               
                $input['VDID'] = $input['UUID'];
                $input['VDNumber'] = $output_vd['VDNumber'];           
                if($this->bornUserVDShell($outputInfo,$input) != 0){
                    $logCode = '0530060E';
                    $log($logCode,$input,true,$outputInfo);
                    continue;          
                }
                connectDB::$dbh->commit();
                $logCode = '05100501';            
                $rtn = CPAPIStatus::BornVDSuccess;
                $log($logCode,$input);                
            }
            else{
                $logCode = '05300604';            
                $log($logCode,$input);    
                continue;
            }
        }         
    }

     /*
        reportMetaData errorCode addingTime allDisk(base64) allNIC(base64)
        MetaData : born$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$inputOrg['VDID'].'$*'.$inputOrg['VDName']
     */
    function reportVDTaskUserCreate($input)
    {
        $haveTask = false;    
        $cancelTask = false;
        $logCode = '0530060F';
        $rtnCode = 99;
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFilnal;
        }                    
        $this->processReportData($input,$data);        
        if($data['ErrCode'] == 0){     
            $rtnCode = $this->vdUpdateAll($data);
            if($rtnCode == 0)
                $logCode = '05100502';
            goto rtnFilnal;
        }
        else{
            if($data['ErrCode'] == 210){
                $haveTask = true;
                $cancelTask = true;
                $logCode = '05100503';
            }
            $responsecode = $this->deleteVD($data,false);
            if($responsecode == CPAPIStatus::NotFound || $responsecode == CPAPIStatus::DeleteVDSuccess)
                $rtnCode =0;
            goto rtnFilnal;
        }
        rtnFilnal:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertBornUserVDLog($logCode,$data['VDName'],$data['DomainName'],$data['DomainID'],$appendErr,$haveTask,$cancelTask,$data['ErrCode']);
            exit($rtnCode);
    }


    function insertBornUserVDLog($code,$vdName,$domainName,$domainID,$appendErr,$haveTask=true,$cancelTask=false,$errCode=NULL)
    {
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $taskStr = "";
        if($haveTask)
            $taskStr = " task";
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);       
        if(is_null($appendErr)){
            if($cancelTask)
                $message = "Cancel Born user VD$appendVDDomain$taskStr.";
            else
                $message = "Born user VD$appendVDDomain$taskStr Success";
        }
        else{
            $message = "Failed to born user VD$appendVDDomain$taskStr$appendErr";
            if(isset($errCode)){
                $rtnMsg = $this->changeModifyReturnCode($errCode);
                $message .= "($rtnMsg)";
            }
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function insertBornUserVDFailTaskShell($inputOrg,$input)
    {
        $cmd = $this->cmdVDTaskErrTask;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= '"'.base64_encode('born$*'.$input['CephID'].'$*VDID$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$inputOrg['VDID'].'$*'.$inputOrg['VDName']).'" ';
        $cmd .= 'userCreate';
        exec($cmd,$output,$rtn);
    }

    function bornUserVDShell($inputOrg,$input)
    {
        // var_dump($inputOrg);
        // var_dump($input);        
        $rtn=-99;    
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDTaskUserCreate;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd); 
            $cmd .= '"'.base64_encode('born$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$inputOrg['VDID'].'$*'.$inputOrg['VDName']).'" ';            
            $cmd .= '"'.$inputOrg['VDID'].'" ';
            $cmd .= '"'.$input['VDID'].'" ';            
            $desc = '"'.$input['VDName'].'" ';
            $cmd .= $desc;
            $cmd .= '"'.$input['UUID'].'" '; 
            $cmd .= $input['DomainID'];           
            // var_dump($cmd);
            exec($cmd,$output,$rtn);                                
        }
        return $rtn;
    }

    function addVDDisk($input)
    {
        $logCode="";        
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }      
        $this->newDomainClass();             
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);  
        $input['DomainName'] = $outputDomain['Name'];
        if(!$this->sqlListVDByID($input['VDID'],$outputVD))
            return CPAPIStatus::AddVDDiskFail;
        if(!$this->sqlListVDDisk($input['VDID'],$outputDisks))            
            return CPAPIStatus::AddVDDiskFail;
        $countIDEType = 0;
        $countSCSIType = 0;
        $countVIRTIOType = 0;
        $outputDisks[] = $input;
        $countDisks = 0;       
        foreach ($outputDisks as $value) {
            $countDisks++;
            $pos = strpos($value['DiskName'], 'ud');
            if($pos === false){
                switch ($value['DiskType']) {
                    case 0:
                        $countIDEType++;
                        break;
                    case 1:
                        $countSCSIType++;
                        break;
                    case 2:
                        $countVIRTIOType++;
                        break;
                }
            }
        }
        $input['VDName']=$outputVD['Name'];        
        $input['VDNumber']=$outputVD['VDNumber'];  
        if(($countDisks-1) == 6){
            $this->insertAddVDDiskFailTaskShell($input);
            return CPAPIStatus::AddVDDiskFail;
        }       
        // var_dump($countIDEType);
        // var_dump($countSCSIType);
        // var_dump($countVIRTIOType);
        if($countIDEType > 2 || $countSCSIType > 2 || $countVIRTIOType > 2){
            $this->insertAddVDDiskFailTaskShell($input);
            return CPAPIStatus::AddVDDiskFail;        
        }
        connectDB::$dbh->beginTransaction();
        if(!isset($input['DiskType'])){
            $input['DiskType'] = 0;
            $input['DiskCache'] = 0;
        }
        if(!isset($input['DiskCache'])){
            $input['DiskCache'] = 0;
        }   
        if(!$this->sqlInsertVDDisk($input,30)){        
            $logCode = '05302101';
            goto failRollBack;
        }        
        $lastID = connectDB::$dbh->lastInsertId();
        if(is_null($lastID)){
            $logCode = '05302101';
            goto failRollBack;
        }
        $input['DiskID'] = $lastID;
        $this->sqlListVolumeByVolumeID($input['VolumeID'],$outputVolume);
        $input['VolumeName'] = $outputVolume['VolumeName'];
        if($this->addVDDiskShell($input) != 0){
            $logCode = '05302102';
            goto failRollBack;
        }            
        else{
            $logCode = '05102001';
            $this->sqlUpdateVDModfiyTime($input['VDID']);
            connectDB::$dbh->commit();
            $rtn = CPAPIStatus::AddVDDiskSuccess;
            goto end;
        }       
        failRollBack:                
            connectDB::$dbh->rollBack();           
            $rtn = CPAPIStatus::AddVDDiskFail;
            $this->insertAddVDDiskFailTaskShell($input);
            goto end;
        end:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertAddVDDiskLog($logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],$appendErr);
            return $rtn;    
    }

     /*
        reportMetaData errorCode addingTime allDisk(base64)
        MetaData : create$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['DiskID']
     */
    function reportVDTaskDiskAdd($input)
    {
        $haveTask = false;    
        $cancelTask = false;
        $logCode = '05302103';
        $rtnCode = 99;
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFilnal;
        }                    
        $this->processReportData($input,$data);
        // var_dump($data);
        if($data['ErrCode'] == 0){     
            connectDB::$dbh->beginTransaction();
            $rtnCode = $this->vdUpdateDisk($data);
            if($rtnCode != 0)
                connectDB::$dbh->rollBack();
            else{
                if(!$this->sqlDeleteSnapshot($data['VDID'])){
                    $rtnCode = 12;
                    connectDB::$dbh->rollBack();
                    goto rtnFilnal;
                }
                connectDB::$dbh->commit();
                $logCode = '05102002';
            }
            goto rtnFilnal;
        }       
        else{
            if($data['ErrCode'] == 210){
                $haveTask = true;
                $cancelTask = true;
                $logCode = '05102003';
            }
            connectDB::$dbh->beginTransaction();
            $rtnCode = $this->vdUpdateDisk($data);
            if($rtnCode != 0)
                connectDB::$dbh->rollBack();
            else
                connectDB::$dbh->commit();
            $rtnCode = 0;
        }
        rtnFilnal:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertAddVDDiskLog($logCode,$data['VDName'],$data['DomainName'],$data['DomainID'],$appendErr,$haveTask,$cancelTask,$data['ErrCode']);
            exit($rtnCode);
    }

    function insertAddVDDiskFailTaskShell($input)
    {
        $cmd = $this->cmdVDTaskErrTask;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= '"'.base64_encode('addDisk$*'.$input['CephID'].'$*VDID$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*DiskID').'" ';
        $cmd .= 'diskAdd';
        exec($cmd,$output,$rtn);   
    }

    function addVDDiskShell($input)
    {
        $rtn=-99;                
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDTaskDiskAdd;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.base64_encode('addDisk$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['DiskID']).'" ';
            $cmd .= '"'.$input['VDID'].'" ';     
            $cmd .= $input['Disk'].' ';
            $cmd .= $input['VolumeName'].' ';
            switch ($input['DiskType']) {
                case 0:
                    $input['DiskType'] = 'ide';
                    break;
                case 1:
                    $input['DiskType'] = 'scsi';
                    break;
                case 2:
                    $input['DiskType'] = 'virtio';
                    break;
                default:
                    $input['DiskType'] = 'ide';
                    break;
            }
            $cmd .= $input['DiskType'].' writethrough ';           
            $cmd .= $input['DomainID'].' ';
            // var_dump($cmd);
            exec($cmd,$output,$rtn);      
            // var_dump($rtn);                                
        }
        // var_dump($output);
        return $rtn;
    }

    function insertAddVDDiskLog($code,$vdName,$domainName,$domainID,$appendErr,$haveTask=true,$cancelTask=false,$errCode=NULL)
    {
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $taskStr = "";
        if($haveTask)
            $taskStr = " task";
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);       
        if(is_null($appendErr)){
            if($cancelTask)
                $message = "Cancel Add VD$appendVDDomain disk$taskStr";
            else
                $message = "Add VD$appendVDDomain disk$taskStr Success";
        }
        else{
            $message = "Failed to add VD$appendVDDomain disk$taskStr$appendErr";
            if(isset($errCode)){
                $rtnMsg = $this->changeModifyReturnCode($errCode);
                $message .= "($rtnMsg)";
            }
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function moveVDDisk($input)
    {
        $logCode="";        
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }      
        $this->newDomainClass();             
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);  
        $input['DomainName'] = $outputDomain['Name'];
        if(!$this->sqlListVDByID($input['VDID'],$outputVD))
            return CPAPIStatus::ModifyVDDiskFail;
        if(!$this->sqlListVDDiskByID($input['VDID'],$input['DiskID'],$outputDisk))            
            return CPAPIStatus::ModifyVDDiskFail;        
        $this->sqlListVolumeByVolumeID($outputDisk['VolumeID'],$outputVolume);
        $input['SrcVolumeAlias'] = $outputVolume['VolumeAlias'];
        $input['DiskName'] = $outputDisk['DiskName'];
        $input['VDName']=$outputVD['Name'];        
        $input['VDNumber']=$outputVD['VDNumber'];        
        connectDB::$dbh->beginTransaction();        
        $this->sqlListVolumeByVolumeID($input['VolumeID'],$outputVolume);
        $input['VolumeAlias'] = $outputVolume['VolumeAlias'];  
        $input['VolumeName'] = $outputVolume['VolumeName'];
        if(!$this->sqlUpdateVDDiskState($input['DiskID'],33)){        
            $logCode = '05302E01';
            goto failRollBack;
        }                
        if($this->moveVDDiskShell($input) != 0){
            $logCode = '05302E02';
            goto failRollBack;
        }            
        else{
            $logCode = '05102D01';           
            connectDB::$dbh->commit();
            $rtn = CPAPIStatus::MoveVDDiskSuccess;
            goto end;
        }       
        failRollBack:                
            connectDB::$dbh->rollBack();           
            $rtn = CPAPIStatus::MoveVDDiskFail;
            $this->insertMoveVDDiskFailTaskShell($input);
            goto end;
        end:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertMoveVDDiskLog($logCode,$input,$appendErr);
            return $rtn;    
    }

    function moveUserProfile($input)
    {
        $logCode="";        
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }      
        $this->newDomainClass();   
        $this->newUserClass();          
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);  
        $input['DomainName'] = $outputDomain['Name'];
        $this->userC->sqlListUserByIDAndidDomain($input['UserID'],$input['DomainID'], $outputUser);
        if(is_null($outputUser))
            return CPAPIStatus::ModifyVDDiskFail;
        $this->userC->sqlListUserProfileDiskDetail($input,$outputProfile);
        // var_dump($outputProfile);
        if(is_null($outputProfile))            
            return CPAPIStatus::ModifyVDDiskFail;        
        $this->sqlListVolumeByVolumeID($outputProfile['VolumeID'],$outputVolume);
        $input['SrcVolumeAlias'] = $outputVolume['VolumeAlias'];
        $input['DiskName'] = $outputProfile['DiskName'];
        $input['DiskID'] = $outputProfile['ID'];
        $input['Username'] = $outputUser['Name'];      
        connectDB::$dbh->beginTransaction();        
        $this->sqlListVolumeByVolumeID($input['VolumeID'],$outputVolume);
        $input['VolumeAlias'] = $outputVolume['VolumeAlias'];  
        $input['VolumeName'] = $outputVolume['VolumeName'];
        if(!$this->sqlUpdateVDDiskState($input['DiskID'],33)){        
            $logCode = '04301701';
            goto failRollBack;
        }        
        if($this->moveUserprofileShell($input) != 0){
            $logCode = '04301702';
            goto failRollBack;
        }            
        else{
            $logCode = '04101601';           
            connectDB::$dbh->commit();
            $rtn = CPAPIStatus::MoveVDDiskSuccess;
            goto end;
        }       
        failRollBack:                
            connectDB::$dbh->rollBack();           
            $rtn = CPAPIStatus::MoveVDDiskFail;
            $this->insertMoveUserProfileFailTaskShell($input);
            goto end;
        end:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertMoveUserDiskLog($logCode,$input,$appendErr);
            return $rtn;    
    }

    function insertMoveUserProfileFailTaskShell($input)
    {
        $cmd = $this->cmdVDTaskErrTask;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= '"'.base64_encode('moveUserProfile$*'.$input['CephID'].'$*'.$input['UserID'].'$*'.$input['Username'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['DiskID'].'$*'.$input['DiskName'].'$*'.$input['VolumeID'].'$*'.$input['VolumeAlias'].'$*'.$input['SrcVolumeAlias']).'" ';
        $cmd .= 'usrpfMove';
        exec($cmd,$output,$rtn);
    }

    function moveUserprofileShell($input)
    {
        $rtn=-99;                      
        // var_dump($input);
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDTaskUserprofileMove;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.base64_encode('moveUserProfile$*'.$input['CephID'].'$*'.$input['UserID'].'$*'.$input['Username'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['DiskID'].'$*'.$input['DiskName'].'$*'.$input['VolumeID'].'$*'.$input['VolumeAlias'].'$*'.$input['SrcVolumeAlias']).'" ';            
            $cmd .= $input['DiskName'].' ';
            $cmd .= $input['VolumeName'].' ';        
            $cmd .= $input['DomainID'].' ';
            // var_dump($cmd);
            exec($cmd,$output,$rtn);      
            // var_dump($rtn);                                
        }
        // var_dump($output);
        return $rtn;
    }

    function reportVDTaskUserprofileMove($input)
    {
        $haveTask = false;    
        $cancelTask = false;
        $logCode = '05302E03';
        $rtnCode = 99;
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFilnal;
        }                    
        $this->processReportUserprofileMoveData($input,$data);
        // var_dump($data);
        if($data['ErrCode'] == 0){     
            connectDB::$dbh->beginTransaction();
            $rtnCode = $this->sqlUpdateVDDiskStateAndVolumeID($data);
            if(!$rtnCode)
                connectDB::$dbh->rollBack();
            else{                
                connectDB::$dbh->commit();
                $logCode = '05102D02';
            }
            goto rtnFilnal;
        }       
        else{
            if($data['ErrCode'] == 210){
                $haveTask = true;
                $cancelTask = true;
                $logCode = '05102D03';
            }
            connectDB::$dbh->beginTransaction();            
            $rtnCode = $this->sqlUpdateVDDiskState($data['DiskID'],0);
            if(!$rtnCode){
                connectDB::$dbh->rollBack();
                $rtnCode = 1;
            }
            else{
                connectDB::$dbh->commit();
                $rtnCode = 0;
            }
        }
        rtnFilnal:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertMoveUserDiskLog($logCode,$data,$appendErr,$haveTask,$cancelTask,$data['ErrCode']);
            exit($rtnCode);
    }

    function insertMoveUserDiskLog($code,$input,$appendErr,$haveTask=true,$cancelTask=false,$errCode=NULL)
    {
        // var_dump($input);
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $taskStr = "";
        if($haveTask)
            $taskStr = " task";
        $message = "";        
        if(is_null($appendErr)){            
            if($cancelTask)
                $message = "Cancel Move User's(".$input['Username'].") disk(".$input['DiskName'].") to Volume(".$input['VolumeAlias'].")$taskStr";
            else
                $message = "Move User's(".$input['Username'].") disk to Volume(".$input['VolumeAlias'].")$taskStr Success";
        }
        else{            
            $message = "Failed to move User's(".$input['Username'].") disk to Volume(".$input['VolumeAlias'].")$taskStr$appendErr";
            if(isset($errCode)){
                $rtnMsg = $this->changeModifyReturnCode($errCode);
                $message .= "($rtnMsg)";            
            }
        }      
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
        }       
    }

     /*
        reportMetaData errorCode addingTime allDisk(base64)
        MetaData : create$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['DiskID']
     */
    function reportVDTaskDiskMove($input)
    {
        $haveTask = false;    
        $cancelTask = false;
        $logCode = '05302E03';
        $rtnCode = 99;
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFilnal;
        }                    
        $this->processReportDiskMoveData($input,$data);
        // var_dump($data);
        if($data['ErrCode'] == 0){     
            connectDB::$dbh->beginTransaction();
            $rtnCode = $this->sqlUpdateVDDiskStateAndVolumeID($data);
            if(!$rtnCode)
                connectDB::$dbh->rollBack();
            else{                
                connectDB::$dbh->commit();
                $logCode = '05102D02';
            }
            goto rtnFilnal;
        }       
        else{
            if($data['ErrCode'] == 210){
                $haveTask = true;
                $cancelTask = true;
                $logCode = '05102D03';
            }
            connectDB::$dbh->beginTransaction();            
            $rtnCode = $this->sqlUpdateVDDiskState($data['DiskID'],0);
            if(!$rtnCode){
                connectDB::$dbh->rollBack();
                $rtnCode = 1;
            }
            else{
                connectDB::$dbh->commit();
                $rtnCode = 0;
            }
        }
        rtnFilnal:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertMoveVDDiskLog($logCode,$data,$appendErr,$haveTask,$cancelTask,$data['ErrCode']);
            exit($rtnCode);
    }

    function insertMoveVDDiskFailTaskShell($input)
    {
        $cmd = $this->cmdVDTaskErrTask;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= '"'.base64_encode('moveDisk$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['DiskID'].'$*'.$input['DiskName'].'$*'.$input['VolumeID'].'$*'.$input['VolumeAlias'].'$*'.$input['SrcVolumeAlias']).'" ';
        $cmd .= 'diskMove';
        exec($cmd,$output,$rtn);   
    }

    function moveVDDiskShell($input)
    {
        $rtn=-99;                       
        // var_dump($input);
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDTaskDiskMove;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.base64_encode('moveDisk$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['DiskID'].'$*'.$input['DiskName'].'$*'.$input['VolumeID'].'$*'.$input['VolumeAlias'].'$*'.$input['SrcVolumeAlias']).'" ';
            $cmd .= '"'.$input['VDID'].'" ';     
            $cmd .= $input['DiskName'].' ';
            $cmd .= $input['VolumeName'].' ';        
            $cmd .= $input['DomainID'].' ';
            // var_dump($cmd);
            exec($cmd,$output,$rtn);      
            // var_dump($rtn);                                
        }
        // var_dump($output);
        return $rtn;
    }

    function insertMoveVDDiskLog($code,$input,$appendErr,$haveTask=true,$cancelTask=false,$errCode=NULL)
    {
        // var_dump($input);
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $taskStr = "";
        if($haveTask)
            $taskStr = " task";
        $message = "";
        $appendVDDomain = $this->logappendVDDomain($input['VDName'],$input['DomainName']);       
        if(is_null($appendErr)){            
            if($cancelTask)
                $message = "Cancel Move VD$appendVDDomain disk(".$input['DiskName'].") to Volume(".$input['VolumeAlias'].")$taskStr";
            else
                $message = "Move VD$appendVDDomain disk(".$input['DiskName'].") to Volume(".$input['VolumeAlias'].")$taskStr Success";
        }
        else{            
            $message = "Failed to move VD$appendVDDomain disk(".$input['DiskName'].") to Volume(".$input['VolumeAlias'].")$taskStr$appendErr";
            if(isset($errCode)){                
                $rtnMsg = $this->changeModifyReturnCode($errCode);
                $message .= "($rtnMsg)";
            }
        }      
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
        }       
    }

    function modifyVDDisk($input)
    {
        $logCode="";        
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }      
        $this->newDomainClass();             
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);  
        $input['DomainName'] = $outputDomain['Name'];
        if(!$this->sqlListVDByID($input['VDID'],$outputVD))
            return CPAPIStatus::ModifyVDDiskFail;
        if(!$this->sqlListVDDiskByID($input['VDID'],$input['DiskID'],$outputDisk))            
            return CPAPIStatus::ModifyVDDiskFail;  
        $input['DiskType'] = $outputDisk['DiskType']; 
        if($input['DiskType'] == $outputDisk['DiskType'] && $input['DiskSize'] == $outputDisk['DiskSize']){
            return CPAPIStatus::ModifyVDDiskSuccess;
        }
        $input['DiskName'] = $outputDisk['DiskName'];
        $input['VDName']=$outputVD['Name'];   
        $input['Size'] = $input['DiskSize'];             
        connectDB::$dbh->beginTransaction();
        $input['State'] = 0;
        $input['DiskCache'] = 0;
        if(!$this->sqlUpdateVDDisk($input)){
            $logCode = '05302C01';
            goto failRollBack;
        }
        $errCode = $this->modifyVDDiskShell($input);
        if( $errCode != 0){
            $logCode = '05302C02';
            goto failRollBack;
        }            
        else{
            $logCode = '05102B01';
            $this->sqlUpdateVDModfiyTime($input['VDID']);
            connectDB::$dbh->commit();
            $rtn = CPAPIStatus::ModifyVDDiskSuccess;
            goto end;
        }       
        failRollBack:                
            connectDB::$dbh->rollBack();           
            $rtn = CPAPIStatus::ModifyVDDiskFail;
            goto end;
        end:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertModifyVDDiskLog($logCode,$input,$appendErr,$errCode);
            return $rtn;    
    }

    function modifyVDDiskShell($input,&$rtnCode)
    {
        $rtn=-99;                
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDDiskModify;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.$input['VDID'].'" ';     
            $cmd .= $input['DiskName'].' ';
            $input['DiskType'] = $this->transformDiskType($input['DiskType']);            
            $cmd .= $input['DiskType'].' writethrough ';
            $cmd .= $input['DiskSize'];
            // var_dump($cmd);
            exec($cmd,$output,$rtn);              
            // var_dump($rtn);                                
        }
        // var_dump($output);
        return $rtn;
    }

    function insertModifyVDDiskLog($code,$input,$appendErr,$errCode=NULL)
    {
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }        
        $appendVDDomain = $this->logappendVDDomain($input['VDName'],$input['DomainName']);
        $diskType = $this->transformDiskType($input['DiskType']);
        $appendDisk = "(Disk Type:".$diskType.",Disk Size:".$input['DiskSize'].")";
        if(is_null($appendErr)){            
            $message = "Modify VD$appendVDDomain disk$appendDisk Success";
        }
        else{
            $message = "Failed to modify VD$appendVDDomain disk$appendDisk$appendErr";
            if(isset($errCode)){
                $errCode = $this->changeModifyReturnCode($errCode);
                $message .= "($errCode)";
            }
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function deleteVDDisk($input)
    {
        $logCode="";        
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }      
        $this->newDomainClass();             
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);  
        $input['DomainName'] = $outputDomain['Name'];
        if(!$this->sqlListVDByID($input['VDID'],$outputVD))
            return CPAPIStatus::DeleteVDDiskFail;
        if(!$this->sqlListVDDiskByID($input['VDID'],$input['DiskID'],$outputDisk))
            return CPAPIStatus::DeleteVDDiskFail;       
        if($outputDisk['State'] != 0)
            return CPAPIStatus::DeleteVDDiskFail;
        $input['VDName']=$outputVD['Name'];        
        $input['VDNumber']=$outputVD['VDNumber'];  
        $input['DiskName']=$outputDisk['DiskName'];
        connectDB::$dbh->beginTransaction();
        if(!$this->sqlUpdateVDDiskState($input['DiskID'],31)){        
            $logCode = '05302301';
            goto failRollBack;
        }                           
        if($this->deleteVDDiskShell($input) != 0){
            $logCode = '05302302';
            goto failRollBack;
        }            
        else{
            $logCode = '05102201';
            $this->sqlUpdateVDModfiyTime($input['VDID']);
            connectDB::$dbh->commit();            
            $rtn = CPAPIStatus::DeleteVDDiskSuccess;
            goto end;
        }       
        failRollBack:                
            connectDB::$dbh->rollBack();           
            $rtn = CPAPIStatus::DeleteVDDiskFail;
            $this->insertDeleteVDDiskFailTaskShell($input);
            goto end;
        end:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertDeleteVDDiskLog($logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],$appendErr);
            return $rtn; 
    }

     /*
        reportMetaData errorCode addingTime allDisk(base64)
        MetaData : create$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['DiskID']
     */
    function reportVDTaskDiskDel($input)
    {
        $haveTask = false;    
        $cancelTask = false;
        $logCode = '05302303';
        $rtnCode = 99;
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFilnal;
        }                    
        $this->processReportData($input,$data);
        if($data['ErrCode'] == 0){     
            connectDB::$dbh->beginTransaction();
            $rtnCode = $this->vdUpdateDisk($data);
            if($rtnCode != 0)
                connectDB::$dbh->rollBack();
            else{
                 if(!$this->sqlDeleteSnapshot($data['VDID'])){
                    $rtnCode = 12;
                    connectDB::$dbh->rollBack();
                    goto rtnFilnal;
                }
                connectDB::$dbh->commit();
                $this->checkANDDeleteVolume();
                $logCode = '05102202';
            }
            goto rtnFilnal;
        }       
        else{
            if($data['ErrCode'] == 210){
                $haveTask = true;
                $cancelTask = true;
                $logCode = '05102203';
            }
            connectDB::$dbh->beginTransaction();
            $rtnCode = $this->vdUpdateDisk($data);
            if($rtnCode != 0)
                connectDB::$dbh->rollBack();
            else
                connectDB::$dbh->commit();
            $rtnCode = 0;
        }
        rtnFilnal:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertDeleteVDDiskLog($logCode,$data['VDName'],$data['DomainName'],$data['DomainID'],$appendErr,$haveTask,$cancelTask,$data['ErrCode']);
            exit($rtnCode);
    }

    function insertDeleteVDDiskFailTaskShell($input)
    {
        $cmd = $this->cmdVDTaskErrTask;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= '"'.base64_encode('deleteDisk$*'.$input['CephID'].'$*VDID$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*DiskID').'" ';
        $cmd .= 'diskDel';
        exec($cmd,$output,$rtn);   
    }

    function deleteVDDiskShell($input)
    {
        $rtn=-99;                
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDTaskDiskDelete;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.base64_encode('deleteDisk$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['DiskID']).'" ';
            $cmd .= '"'.$input['VDID'].'" ';     
            $cmd .= $input['DiskName'].' '; 
            $cmd .= $input['DomainID'].' ';
            exec($cmd,$output,$rtn);                                      
        }
        // var_dump($output);
        return $rtn;
    }

    function insertDeleteVDDiskLog($code,$vdName,$domainName,$domainID,$appendErr,$haveTask=true,$cancelTask=false,$errCode=NULL)
    {
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $taskStr = "";
        if($haveTask)
            $taskStr = " task";
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);       
        if(is_null($appendErr)){
            if($cancelTask)
                $message = "Cancel Delete VD$appendVDDomain disk$taskStr";
            else
                $message = "Delete VD$appendVDDomain disk$taskStr Success";
        }
        else{
            $message = "Failed to delete VD$appendVDDomain disk$taskStr$appendErr";
            if(isset($errCode)){
                $rtnMsg = $this->changeModifyReturnCode($errCode);
                $message .= "($rtnMsg)";
            }
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function addVDNIC($input)
    {
        $logCode="";        
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }      
        $this->newDomainClass();             
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);  
        $input['DomainName'] = $outputDomain['Name'];
        if(!$this->sqlListVDByID($input['VDID'],$outputVD))
            return CPAPIStatus::AddVDNICFail;
        $this->sqlListVswitchByid($input['Vswitch'],$outputVswitch);
        if(is_null($outputVswitch)){
            return CPAPIStatus::AddVDNICFail;
        }
        $input['Vswitch'] = $outputVswitch;
        $input['VDName']=$outputVD['Name'];        
        $input['VDNumber']=$outputVD['VDNumber'];  
        $input['DiskName']=$outputDisk['DiskName'];
        // var_dump($input);
        if($this->addVDNICShell($input,$output,$rtnCode)){            
            if(!is_null($output))
                $this->sqlUpdateVDNIC($input['VDID'],json_encode($output));
            $this->sqlUpdateVDModfiyTime($input['VDID']);
            $logCode = '05102401';
            $rtn=CPAPIStatus::AddVDNICSuccess;
        }
        else{
            $logCode = '05302501';
            $rtn=CPAPIStatus::AddVDNICFail;
        }
        end:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertAddVDNICLog($logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],$appendErr,$rtnCode);
            return $rtn;
    }

    function insertAddVDNICLog($code,$vdName,$domainName,$domainID,$appendErr,$rtnCode)
    {
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);       
        if(is_null($appendErr)){
            $message = "Add VD$appendVDDomain NIC Success";
        }
        else{
            if(isset($rtnCode)){               
                $rtnMsg = $this->changeModifyReturnCode($rtnCode);
                $message = "Failed to add VD$appendVDDomain NIC$appendErr($rtnMsg)";            
            }
            else{
                $message = "Failed to add VD$appendVDDomain NIC$appendErr";
            }
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }    

    function addVDNICShell($input,&$output,&$rtnCode)
    {
        $result = false;        
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDNICAdd;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);             
            $cmd .= '"'.$input['VDID'].'" ';   
            // $cmd .= 'br'.$input['Vswitch'].' ';
            $vswitchCmd = 'br';
            $vswitchCmd .= $input['Vswitch']['BridgeID'];
            if($input['Vswitch']['VLANID'] != 0){
                $vswitchCmd .= '.'.$input['Vswitch']['VLANID'];
            }
            $cmd .= $vswitchCmd.' ';
            $cmd .= $input['Speed'].' ';   
            if(isset($input['MAC']) && strlen($input['MAC']) > 0)
                $cmd .= $input['MAC'];  
            // var_dump($cmd);           
            exec($cmd,$output,$rtn);    
            $rtnCode = $rtn;
            if($rtn == 0){
                $output = json_decode($output[0],true);
                $result = true;
            }
        }
        return $result;
    }

    function deleteVDNIC($input)
    {
        $logCode="";        
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }      
        $this->newDomainClass();             
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);  
        $input['DomainName'] = $outputDomain['Name'];
        if(!$this->sqlListVDByID($input['VDID'],$outputVD))
            return CPAPIStatus::DeleteVDNICFail;
        $input['VDName']=$outputVD['Name'];        
        $input['VDNumber']=$outputVD['VDNumber'];  
        $input['DiskName']=$outputDisk['DiskName'];
        // var_dump($input);
        if($this->deleteVDNICShell($input,$output,$rtnCode)){            
            if(!is_null($output))
                $this->sqlUpdateVDNIC($input['VDID'],json_encode($output));
            $this->sqlUpdateVDModfiyTime($input['VDID']);
            $logCode = '05102601';
            $rtn=CPAPIStatus::DeleteVDNICSuccess;
        }
        else{
            $logCode = '05302701';
            $rtn=CPAPIStatus::DeleteVDNICFail;
        }
        end:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertDeleteVDNICLog($logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],$appendErr,$rtnCode);
            return $rtn;
    }

    function insertDeleteVDNICLog($code,$vdName,$domainName,$domainID,$appendErr,$rtnCode=NULL)
    {
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);       
        if(is_null($appendErr)){
            $message = "Delete VD$appendVDDomain NIC Success";
        }
        else{
            if(isset($rtnCode)){
                $rtnMsg = $this->changeModifyReturnCode($rtnCode);
                $message = "Failed to delete VD$appendVDDomain NIC$appendErr($rtnMsg)";
            }
            else
                $message = "Failed to delete VD$appendVDDomain NIC$appendErr";
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function deleteVDNICShell($input,&$output,&$rtnCode)
    {
        $result = false;        
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDNICDelete;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);             
            $cmd .= '"'.$input['VDID'].'" ';              
            $cmd .= ' '.$input['MAC'];    
            // var_dump($cmd);            
            exec($cmd,$output,$rtn);       
            $rtnCode = $rtn;     
            if($rtn == 0){
                $output = json_decode($output[0],true);
                $result = true;
            }
        }
        return $result;
    }

    function cloneVD($input)
    {                
        $log = function ($logCode,$input,$isRollback=false,$outputVDInfo=NULL){            
            if($isRollback){                
                connectDB::$dbh->rollBack();                
            }
            if($logCode != '05300803' && $logCode != '05300806' && $logCode != '05100701')
                $this->insertCloneVDFailTaskShell($outputVDInfo,$input);
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertCloneVDLog($logCode,$input['VDName'],$input['DomainName'],$input['DomainID'],$appendErr);
        };            
        $logCode="";        
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }           
        $this->newDomainClass();             
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);             
        if(!$this->sqlListVDInfoByID($input['VDID'],$outputVDInfo)){   
            $logCode = '05300803';
            $log($logCode,$input);
            return;            
        }        
        $outputVDInfo['VDID'] = $input['VDID'];
        $input['DomainName'] = $outputDomain['Name'];
        $input['VDNumber']=$outputVDInfo['VDNumber'];
        $input['CephID']=$outputVDInfo['CephID'];
        $input['CPU']=$outputVDInfo['CPU'];
        $input['CpuCt']=$outputVDInfo['CpuCt'];
        $input['RAM']=$outputVDInfo['RAM'];
        $input['NIC_Info'] = json_decode($outputVDInfo['NIC'],true);
        $input['Suspend']=$outputVDInfo['Suspend'];      
        $input['USBType']=$outputVDInfo['USBType'];  
        $input['USBRedirCt']=$outputVDInfo['USBRedirCt'];
        $input['CDRom']=$outputVDInfo['CDRom'];
        $input['Sound']=$outputVDInfo['Sound'];  
        $input['CPUType']=$outputVDInfo['CPUType'];
        $input['SSLayerMax']=$outputVDInfo['SSLayerMax'];
        $input['VGACount'] = $outputVDInfo['VGACount'];      
        $input['UEFI'] = $outputVDInfo['UEFI'];
        $input['QXL'] = $outputVDInfo['QXL'];
        $input['ViewerProtocol'] = $outputVDInfo['ViewerProtocol'];
        if(!$this->sqlListVDDisk($input['VDID'], $outputDisk)){
            $logCode = '05300806';
            $log($logCode,$input);
            return;                         
        }      
        foreach ($input['VDs'] as $value) {    
            $input['VDName'] = $value['VDName']; 
            $input['Username'] = $value['Username'];     
            if(!$this->sqlGetUUID($outputUUID))
                continue;           
            if($this->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$outputVD)){
                $logCode = '05300801';
                $log($logCode,$input,false,$outputVDInfo);
                continue;                
            }
            if($input['Username'] != ""){                
                $responsecode = $this->createUser($input,$outputUser);            
                if($responsecode == CPAPIStatus::CreateUserSuccess || $responsecode == CPAPIStatus::Conflict){                
                    $input['UserID'] = $outputUser['ID'];                
                }
                else{
                    $logCode = '05300802';
                    $log($logCode,$input,false,$outputVDInfo);                    
                    continue;      
                }
            }
            else
            {            
                $input['UserID'] = null;
            }                    
            if(isset($input['ConnectIP'])){                     
                connectDB::$dbh->beginTransaction();              
                $input['UUID'] = $outputUUID['UUID'];
                $input['State'] = 30;
                if(!$this->sqlInsertVDBase($input)){
                    $logCode = '05300808';
                    $log($logCode,$input,true,$outputVDInfo);
                    continue;
                }
                $outputVD = null;
                $this->sqlListVDByID($input['UUID'], $outputVD);            
                if(is_null($outputVD)){
                    $logCode = '05300808';
                    $log($logCode,$input,true,$outputVDInfo);
                    continue;
                } 
                if(!$this->sqlInsertVDInfo($input)){
                    $logCode = '05300809';
                    $log($logCode,$input,true,$outputVDInfo);
                    continue;               
                }
                if(!$this->sqlInsertVDOrg($input)){
                    $logCode = '0530080A';
                    $log($logCode,$input,true,$outputVDInfo);
                    continue;                 
                }
                $diskInsert = array('VDID'=>$input['UUID'],'CephID'=>$input['CephID'],'DomainID'=>$input['DomainID']);
                foreach ($outputDisk as $disk) {
                    $diskInsert['VolumeID']=$disk['VolumeID'];
                    $diskInsert['Disk'] = $disk['Size'];
                    if(!$this->sqlInsertVDDisk($diskInsert,30)){
                        $logCode = '0530080B';
                        $log($logCode,$input,true,$outputVDInfo);
                        continue;
                    }                             
                }                
                if($input['Username'] != '' && !$this->sqlUpdateUserDefaultVD($input)){
                    $logCode = '0530080C';
                    $log($logCode,$input,true,$outputVDInfo);
                    continue;                                             
                }                
                if(!$this->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$outputVD)){
                    $logCode = '0530080D';
                    $log($logCode,$input,true,$outputVDInfo);
                    continue;   
                }                
                $input['VDID'] = $outputVD['VDID'];                    
                $input['VDNumber'] = $outputVD['VDNumber'];         
                if($this->cloneVDShell($outputVDInfo,$input) != 0){
                    $logCode = '0530080E';
                    $log($logCode,$input,true,$outputVDInfo);
                    continue;           
                }
                connectDB::$dbh->commit();
                $logCode = '05100701';            
                $rtn = CPAPIStatus::CloneOrgSuccess;
                $log($logCode,$input);           
            }
            else{
                $logCode = '05300804';
                $log($logCode,$input);  
            }
        }
        // fail:
        //     $rtn = CPAPIStatus::CloneOrgFail;
        //     goto end;
        // fail_del_vd:                
        //     connectDB::$dbh->rollBack();             
        //     $rtn = CPAPIStatus::CloneOrgFail;
        //     goto end;
        // end:
        //     $append_err = $this->logVDAppendErr($logCode);
        //     $this->insert_clone_vd_log($logCode,$input['VDName'],$output_domain['Name'],$input['DomainID'],$append_err);
        //     return $rtn;
    }

    /*
        reportMetaData errorCode addingTime allDisk(base64) allNIC(base64)
        MetaData : clone$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$inputOrg['VDID'].'$*'.$inputOrg['VDName']
     */
    function reportVDTaskOrgClone($input)
    {          
        $haveTask = false;    
        $cancelTask = false;
        $logCode = '0530080F';
        $rtnCode = 99;
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFilnal;
        }                    
        $this->processReportData($input,$data);       
        if($data['ErrCode'] == 0){    
            $rtnCode = $this->vdUpdateAll($data,true);
            if($rtnCode == 0)
                $logCode = '05100702';
            goto rtnFilnal;
        }
        else{
            if($data['ErrCode'] == 210){
                $haveTask = true;
                $cancelTask = true;
                $logCode = '05100703';
            }
            $responsecode = $this->deleteVD($data,false);
            if($responsecode == CPAPIStatus::NotFound || $responsecode == CPAPIStatus::DeleteVDSuccess)
                $rtnCode =0;
            goto rtnFilnal;
        }
        rtnFilnal:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertCloneVDLog($logCode,$data['VDName'],$data['DomainName'],$data['DomainID'],$appendErr,$haveTask,$cancelTask);
            exit($rtnCode);
    }        

    function insertCloneVDLog($code,$vdName,$domainName,$domainID,$appendErr,$haveTask=true,$cancelTask=false)
    {
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $taskStr = "";
        if($haveTask)
            $taskStr = " task";
        if(!is_null($vdName)){
            $appendVDDomain .= '(';
            $appendVDDomain .= "Name:$vdName";            
            $appendVDDomain .= ')';
        }           
        if(is_null($appendErr)){
            if($cancelTask)
                $message = "Cancel Clone VD$appendVDDomain$taskStr";
            else
                $message = "Clone VD$appendVDDomain$taskStr Success";
        }
        else{
            $message = "Failed to clone VD$appendVDDomain$taskStr$appendErr";
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function insertCloneVDFailTaskShell($inputOrg,$input)
    {
        $cmd = $this->cmdVDTaskErrTask;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= '"'.base64_encode('clone$*'.$input['CephID'].'$*VDID$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$inputOrg['VDID'].'$*'.$inputOrg['VDName']).'" ';
        $cmd .= 'orgClone';
        exec($cmd,$output,$rtn);
    }

    function cloneVDShell($inputOrg,$input)
    {
        $rtn=-99;           
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDTaskOrgClone;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.base64_encode('clone$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$inputOrg['VDID'].'$*'.$inputOrg['VDName']).'" ';            
            $cmd .= '"'.$inputOrg['VDID'].'" ';
            $cmd .= '"'.$input['VDID'].'" ';            
            $desc = '"'.$input['VDName'].'"';          
            $cmd .= $desc.' ';
            $cmd .= '"'.$input['UUID'].'" '; 
            $cmd .= $input['DomainID'];
            // var_dump($cmd);
            exec($cmd,$output,$rtn);
        }                
        return $rtn;
    }        
    
    function userToOriginalVD($input)
    {                
        $log = function ($logCode,$input,$isRollback=false,$outputVDInfo=NULL){            
            if($isRollback)
                connectDB::$dbh->rollBack();
            if($logCode != '05302A03' && $logCode != '05302A06' && $logCode != '05102901')
                $this->insertUserToOriginalVDFailTaskShell($outputVDInfo,$input);
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertUserToOriginalVDLog($logCode,$input['VDName'],$input['DomainName'],$input['DomainID'],$appendErr);
        };            
        $logCode="";        
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }           
        $this->newDomainClass();             
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);             
        if(!$this->sqlListVDInfoByID($input['VDID'],$outputVDInfo)){   
            $logCode = '05302A03';
            $log($logCode,$input);
            return;            
        }        
        $outputVDInfo['VDID'] = $input['VDID'];
        $input['DomainName'] = $outputDomain['Name'];
        $input['VDNumber']=$outputVDInfo['VDNumber'];
        $input['CephID']=$outputVDInfo['CephID'];
        $input['CPU']=$outputVDInfo['CPU'];
        $input['CpuCt']=$outputVDInfo['CpuCt'];
        $input['RAM']=$outputVDInfo['RAM'];
        $input['NIC_Info'] = json_decode($outputVDInfo['NIC'],true);
        $input['Suspend']=$outputVDInfo['Suspend'];        
        $input['USBType']=$outputVDInfo['USBType'];
        $input['USBRedirCt']=$outputVDInfo['USBRedirCt'];
        $input['CDRom']=$outputVDInfo['CDRom'];
        $input['Sound']=$outputVDInfo['Sound'];  
        $input['CPUType']=$outputVDInfo['CPUType'];
        $input['SSLayerMax']=$outputVDInfo['SSLayerMax'];
        $input['VGACount'] = $outputVDInfo['VGACount'];      
        $input['UEFI'] = $outputVDInfo['UEFI'];
        $input['QXL'] = $outputVDInfo['QXL'];
        $input['ViewerProtocol'] = $outputVDInfo['ViewerProtocol'];
         if(!$this->sqlListVDDisk($input['VDID'], $outputDisk)){
            $logCode = '05302A06';
            $log($logCode,$input);
            return;                         
        }      
        if(!$this->sqlGetUUID($outputUUID))
            return CPAPIStatus::DBConnectFail;           
        if($this->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$outputVD)){
            $logCode = '05302A01';
            $log($logCode,$input,false,$outputVDInfo); 
            return;            
        }
        if($input['Username'] != ""){                
            $responsecode = $this->createUser($input,$outputUser);            
            if($responsecode == CPAPIStatus::CreateUserSuccess || $responsecode == CPAPIStatus::Conflict){                
                $input['UserID'] = $outputUser['ID'];                
            }
            else{
                $logCode = '05302A02';
                $log($logCode,$input,false,$outputVDInfo);
                return;      
            }
        }
        else
        {            
            $input['UserID'] = null;
        }                    
        if(isset($input['ConnectIP'])){                     
            connectDB::$dbh->beginTransaction();              
            $input['UUID'] = $outputUUID['UUID'];
            $input['State'] = 30;
            if(!$this->sqlInsertVDBase($input)){
                $logCode = '05302A08';
                $log($logCode,$input,true);
                return;
            }
            $outputVD = null;
            $this->sqlListVDByID($input['UUID'], $outputVD);            
            if(is_null($outputVD)){
                $logCode = '05302A08';
                $log($logCode,$input,true,$outputVDInfo);
                return;
            } 
            if(!$this->sqlInsertVDInfo($input)){
                $logCode = '05302A09';
                $log($logCode,$input,true,$outputVDInfo);
                return;               
            }
            if(!$this->sqlInsertVDOrg($input)){
                $logCode = '05302A0A';
                $log($logCode,$input,true,$outputVDInfo);
                return;                 
            }
            $diskInsert = array('VDID'=>$input['UUID'],'CephID'=>$input['CephID'],'DomainID'=>$input['DomainID']);
            foreach ($outputDisk as $disk) {
                $diskInsert['VolumeID']=$disk['VolumeID'];
                $diskInsert['Disk'] = $disk['Size'];
                if(!$this->sqlInsertVDDisk($diskInsert,30)){
                    $logCode = '05302A0B';
                    $log($logCode,$input,true,$outputVDInfo);
                    continue;
                }                             
            }                
            if($input['Username'] != '' && !$this->sqlUpdateUserDefaultVD($input)){
                $logCode = '05302A0C';
                $log($logCode,$input,true,$outputVDInfo);
                return;                                             
            }                
            if(!$this->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$outputVD)){
                $logCode = '05302A0D';
                $log($logCode,$input,true,$outputVDInfo);
                return;   
            }                
            $input['VDID'] = $outputVD['VDID'];                    
            $input['VDNumber'] = $outputVD['VDNumber'];         
            if($this->userToOriginalVDShell($outputVDInfo,$input) != 0){
                $logCode = '05302A0E';
                $log($logCode,$input,true,$outputVDInfo);
                return;           
            }
            connectDB::$dbh->commit();
            $logCode = '05102901';            
            $rtn = CPAPIStatus::CloneOrgSuccess;
            $log($logCode,$input);           
        }
        else{
            $logCode = '05302A04';
            $log($logCode,$input);  
        }
    }

    /*
        reportMetaData errorCode addingTime allDisk(base64) allNIC(base64)
        MetaData : clone$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$inputOrg['VDID'].'$*'.$inputOrg['VDName']
     */
    function reportUserToOriginalVD($input)
    {          
        $haveTask = false;    
        $cancelTask = false;
        $logCode = '05302A0F';
        $rtnCode = 99;
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFilnal;
        }                    
        $this->processReportData($input,$data);       
        if($data['ErrCode'] == 0){    
            $rtnCode = $this->vdUpdateAll($data,true);
            if($rtnCode == 0)
                $logCode = '05102902';
            goto rtnFilnal;
        }
        else{
            if($data['ErrCode'] == 210){
                $haveTask = true;
                $cancelTask = true;
                $logCode = '05102903';
            }
            $responsecode = $this->deleteVD($data,false);
            if($responsecode == CPAPIStatus::NotFound || $responsecode == CPAPIStatus::DeleteVDSuccess)
                $rtnCode =0;
            goto rtnFilnal;
        }
        rtnFilnal:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertUserToOriginalVDLog($logCode,$data['VDName'],$data['DomainName'],$data['DomainID'],$appendErr,$haveTask,$cancelTask,$data['ErrCode']);
            exit($rtnCode);
    }        

    function insertUserToOriginalVDLog($code,$vdName,$domainName,$domainID,$appendErr,$haveTask=true,$cancelTask=false,$errCode=NULL)
    {
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $taskStr = "";
        if($haveTask)
            $taskStr = " task";
        if(!is_null($vdName)){
            $appendVDDomain .= '(';
            $appendVDDomain .= "Name:$vdName";            
            $appendVDDomain .= ')';
        }           
        if(is_null($appendErr)){
            if($cancelTask)
                $message = "Cancel User VD to Virtual Host$appendVDDomain$taskStr";
            else
                $message = "User VD to Virtual Host$appendVDDomain$taskStr Success";
        }
        else{
            $message = "Failed to User VD to Virtual Host$appendVDDomain$taskStr$appendErr";
            if(isset($errCode)){
                $rtnMsg = $this->changeModifyReturnCode($errCode);
                $message .= "($rtnMsg)";                
            }
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function insertUserToOriginalVDFailTaskShell($inputOrg,$input)
    {
        $cmd = $this->cmdVDTaskErrTask;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= '"'.base64_encode('usertoorg$*'.$input['CephID'].'$*VDID$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$inputOrg['VDID'].'$*'.$inputOrg['VDName']).'" ';
        $cmd .= 'userClone';
        exec($cmd,$output,$rtn);
    }

    function userToOriginalVDShell($inputOrg,$input)
    {
        $rtn=-99;           
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDTaskUserClone;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.base64_encode('usertoorg$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$inputOrg['VDID'].'$*'.$inputOrg['VDName']).'" ';            
            $cmd .= '"'.$inputOrg['VDID'].'" ';
            $cmd .= '"'.$input['VDID'].'" ';            
            $desc = '"'.$input['VDName'].'"';          
            $cmd .= $desc.' ';
            $cmd .= '"'.$input['UUID'].'" '; 
            $cmd .= $input['DomainID'];
            // var_dump($cmd);
            exec($cmd,$output,$rtn);
        }                
        return $rtn;
    }

    function seedToOriginalVD($input)
    {                
        $log = function ($logCode,$input,$isRollback=false,$outputVDInfo=NULL){            
            if($isRollback)
                connectDB::$dbh->rollBack();
            if($logCode != '05302A03' && $logCode != '05302A06' && $logCode != '05102901')
                $this->insertSeedToOriginalVDFailTaskShell($outputVDInfo,$input);
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertSeedToOriginalVDLog($logCode,$input['VDName'],$input['DomainName'],$input['DomainID'],$appendErr);
        };            
        $logCode="";        
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }           
        $this->newDomainClass();             
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);             
        if(!$this->sqlListVDInfoByID($input['VDID'],$outputVDInfo)){   
            $logCode = '05302A03';
            $log($logCode,$input);
            return;            
        }        
        $outputVDInfo['VDID'] = $input['VDID'];
        $input['DomainName'] = $outputDomain['Name'];
        $input['VDNumber']=$outputVDInfo['VDNumber'];
        $input['CephID']=$outputVDInfo['CephID'];
        $input['CPU']=$outputVDInfo['CPU'];
        $input['CpuCt']=$outputVDInfo['CpuCt'];
        $input['RAM']=$outputVDInfo['RAM'];
        $input['NIC_Info'] = json_decode($outputVDInfo['NIC'],true);
        $input['Suspend']=$outputVDInfo['Suspend'];        
        $input['USBType']=$outputVDInfo['USBType'];
        $input['USBRedirCt']=$outputVDInfo['USBRedirCt'];
        $input['CDRom']=$outputVDInfo['CDRom'];
        $input['Sound']=$outputVDInfo['Sound'];  
        $input['CPUType']=$outputVDInfo['CPUType'];
        $input['SSLayerMax']=$outputVDInfo['SSLayerMax'];
        $input['VGACount'] = $outputVDInfo['VGACount'];      
        $input['UEFI'] = $outputVDInfo['UEFI'];
        $input['QXL'] = $outputVDInfo['QXL'];
         if(!$this->sqlListVDDisk($input['VDID'], $outputDisk)){
            $logCode = '05302A06';
            $log($logCode,$input);
            return;                         
        }      
        if(!$this->sqlGetUUID($outputUUID))
            return CPAPIStatus::DBConnectFail;           
        if($this->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$outputVD)){
            $logCode = '05302A01';
            $log($logCode,$input,false,$outputVDInfo); 
            return;            
        }
        if($input['Username'] != ""){                
            $responsecode = $this->createUser($input,$outputUser);            
            if($responsecode == CPAPIStatus::CreateUserSuccess || $responsecode == CPAPIStatus::Conflict){                
                $input['UserID'] = $outputUser['ID'];                
            }
            else{
                $logCode = '05302A02';
                $log($logCode,$input,false,$outputVDInfo);
                return;      
            }
        }
        else
        {            
            $input['UserID'] = null;
        }                    
        if(isset($input['ConnectIP'])){                     
            connectDB::$dbh->beginTransaction();              
            $input['UUID'] = $outputUUID['UUID'];
            $input['State'] = 30;
            if(!$this->sqlInsertVDBase($input)){
                $logCode = '05302A08';
                $log($logCode,$input,true);
                return;
            }
            $outputVD = null;
            $this->sqlListVDByID($input['UUID'], $outputVD);            
            if(is_null($outputVD)){
                $logCode = '05302A08';
                $log($logCode,$input,true);
                return;
            } 
            if(!$this->sqlInsertVDInfo($input)){
                $logCode = '05302A09';
                $log($logCode,$input,true);
                return;               
            }
            if(!$this->sqlInsertVDOrg($input)){
                $logCode = '05302A0A';
                $log($logCode,$input,true);
                return;                 
            }
            $diskInsert = array('VDID'=>$input['UUID'],'CephID'=>$input['CephID'],'DomainID'=>$input['DomainID']);
            foreach ($outputDisk as $disk) {
                $diskInsert['VolumeID']=$disk['VolumeID'];
                $diskInsert['Disk'] = $disk['Size'];
                if(!$this->sqlInsertVDDisk($diskInsert,30)){
                    $logCode = '05302A0B';
                    $log($logCode,$input,true);
                    continue;
                }                             
            }                
            if($input['Username'] != '' && !$this->sqlUpdateUserDefaultVD($input)){
                $logCode = '05302A0C';
                $log($logCode,$input,true);
                return;                                             
            }                
            if(!$this->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$outputVD)){
                $logCode = '05302A0D';
                $log($logCode,$input,true);
                return;   
            }                
            $input['VDID'] = $outputVD['VDID'];                    
            $input['VDNumber'] = $outputVD['VDNumber'];         
            if($this->seedToOriginalVDShell($outputVDInfo,$input) != 0){
                $logCode = '05302A0E';
                $log($logCode,$input,true);
                return;           
            }
            connectDB::$dbh->commit();
            $logCode = '05102901';            
            $rtn = CPAPIStatus::CloneOrgSuccess;
            $log($logCode,$input);           
        }
        else{
            $logCode = '05302A04';
            $log($logCode,$input);  
        }
    }

    /*
        reportMetaData errorCode addingTime allDisk(base64) allNIC(base64)
        MetaData : clone$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$inputOrg['VDID'].'$*'.$inputOrg['VDName']
     */
    function reportSeedToOriginalVD($input)
    {          
        $haveTask = false;    
        $cancelTask = false;
        $logCode = '05302A0F';
        $rtnCode = 99;
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFilnal;
        }                    
        $this->processReportData($input,$data);       
        if($data['ErrCode'] == 0){    
            $rtnCode = $this->vdUpdateAll($data,true);
            if($rtnCode == 0)
                $logCode = '05102902';
            goto rtnFilnal;
        }
        else{
            if($data['ErrCode'] == 210){
                $haveTask = true;
                $cancelTask = true;
                $logCode = '05102903';
            }
            $responsecode = $this->deleteVD($data,false);
            if($responsecode == CPAPIStatus::NotFound || $responsecode == CPAPIStatus::DeleteVDSuccess)
                $rtnCode =0;
            goto rtnFilnal;
        }
        rtnFilnal:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertSeedToOriginalVDLog($logCode,$data['VDName'],$data['DomainName'],$data['DomainID'],$appendErr,$haveTask,$cancelTask,$data['ErrCode']);
            exit($rtnCode);
    }        

    function insertSeedToOriginalVDLog($code,$vdName,$domainName,$domainID,$appendErr,$haveTask=true,$cancelTask=false,$errCode=NULL)
    {
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $taskStr = "";
        if($haveTask)
            $taskStr = " task";
        if(!is_null($vdName)){
            $appendVDDomain .= '(';
            $appendVDDomain .= "Name:$vdName";            
            $appendVDDomain .= ')';
        }           
        if(is_null($appendErr)){
            if($cancelTask)
                $message = "Cancel Seed VD to Virtual Host$appendVDDomain$taskStr";
            else
                $message = "Seed VD to Virtual Host$appendVDDomain$taskStr Success";
        }
        else{
            $message = "Failed to Seed VD to Virtual Host$appendVDDomain$taskStr$appendErr";
            if(isset($errCode)){
                $rtnMsg = $this->changeModifyReturnCode($errCode);
                $message .= "($rtnMsg)";                
            }
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function insertSeedToOriginalVDFailTaskShell($inputOrg,$input)
    {
        $cmd = $this->cmdVDTaskErrTask;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= '"'.base64_encode('seedtoorg$*'.$input['CephID'].'$*VDID$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$inputOrg['VDID'].'$*'.$inputOrg['VDName']).'" ';
        $cmd .= 'seedCloneBk';
        exec($cmd,$output,$rtn);
    }

    function seedToOriginalVDShell($inputOrg,$input)
    {
        $rtn=-99;           
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDTaskSeedCloneBack;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.base64_encode('seedtoorg$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$inputOrg['VDID'].'$*'.$inputOrg['VDName']).'" ';            
            $cmd .= '"'.$inputOrg['VDID'].'" ';
            $cmd .= '"'.$input['VDID'].'" ';            
            $desc = '"'.$input['VDName'].'"';          
            $cmd .= $desc.' ';
            $cmd .= '"'.$input['UUID'].'" '; 
            $cmd .= $input['DomainID'];
            // var_dump($cmd);
            exec($cmd,$output,$rtn);
        }                
        return $rtn;
    }

    function resetVDPassword($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }             
        $this->newDomainClass();        
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);   
        if(!$this->sqlListVDInfoByID($input['VDID'],$outputVD)){
            return CPAPIStatus::NotFound;
        }        
        if(!$this->sqlListVDUserIDByVDID($input['VDID'],$outputUserID))
            return CPAPIStatus::ResetPasswordFail;
        if(is_null($outputUserID['OrgUserID']) && is_null($outputUserID['UserUserID']))
            return CPAPIStatus::ResetPasswordFail;        
        if(!is_null($outputUserID['OrgUserID']))
            $input['UserID'] = $outputUserID['OrgUserID'];
        if(!is_null($outputUserID['UserUserID']))
            $input['UserID'] = $outputUserID['UserUserID'];
        $this->newUserClass();       
        $input['Password'] = '000000';
        $responsecode = $this->userC->modifyUserPassword($input,$logCode,true);
        $appendErr = $this->logVDAppendErr($logCode);
        $this->insertResetVDPasswordLog($logCode,$outputVD['VDName'],$outputDomain['Name'],$input['DomainID'],$appendErr);
        return $responsecode;
    }

    function resetUserPassword($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }             
        $this->newDomainClass();        
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);           
        $this->newUserClass();       
        $this->userC->sqlListUserByIDAndidDomain($input['UserID'],$input['DomainID'],$outputUser);
        if(is_null($outputUser)){
            return CPAPIStatus::NotFound;
        }
        $input['Password'] = '000000';
        $responsecode = $this->userC->modifyUserPassword($input,$logCode,true);
        $appendErr = $this->logVDAppendErr($logCode);
        $this->insertResetVDPasswordLog($logCode,$outputUser['Name'],$outputDomain['Name'],$input['DomainID'],$appendErr);
        return $responsecode;
    }

    function insertResetVDPasswordLog($code,$vdName,$domainName,$domainID,$appendErr)
    {
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);       
        if(is_null($appendErr)){
            $message = "Reset VD's$appendVDDomain Password Success";
        }
        else{
            $message = "Failed to reset VD's$appendVDDomain Password";
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function deleteVD($input,$isLog = true)
    {
        $logCode = "";       
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }     
        if(!$this->sqlGetUUID($outputUUID))
            return CPAPIStatus::DBConnectFail;          
        $this->newDomainClass();       
        $this->newUserClass(); 
        if(!$this->sqlListVDByID($input['VDID'],$output_vd)){            
            return CPAPIStatus::NotFound;
        }
        $input['DomainID']=$output_vd['DomainID'];        
        $this->domainC->sqlListDomainByID($input['DomainID'], $output_domain);
        $input['VDName']=$output_vd['Name'];       
        $input['VDNumber']=$output_vd['VDNumber'];
        connectDB::$dbh->beginTransaction();           
        $this->sqlListVDUserIDByVDID($input['VDID'],$outputUserID);                        
        if(!isset($outputUserID)){            
            $count = $this->sqlCheckSeedVDUserVDCount($input['VDID']);               
            if($count > 0){
                connectDB::$dbh->rollBack();
                if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>'05301A06','Riser'=>'admin','Message'=>'Failde to Delete Seed VD('.$input['VDName'].')(User VDs is borned or reborned or renewed from seed)'),$lastID)){
                    LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
                }                   
                return CPAPIStatus::DeleteVDFail;
            }
        }
        if(!$this->sqlDeleteVDDisk($input['VDID'])){
            $logCode = '05301A03';
            goto fail_del_vd; 
        }
        if(!$this->sqlDeleteVD($input['VDID'])){
            $logCode = '05301A04';
            goto fail_del_vd;             
        }
        if(!$this->sqlDeleteUserDefaultVD($input['VDID'])){
            $logCode = '05301A05';
            goto fail_del_vd;             
        }        
        // if(!is_null($outputUserID)){
        //     $userID=$outputUserID['OrgUserID']==NULL?$outputUserID['UserUserID']:$outputUserID['OrgUserID'];            
        //     // var_dump($userID);
        //     $this->userC->sqlDeleteUser((int)$userID);
        // }
        if($this->deleteVDShell($input) != 0){
            switch ($rtn_delete){
                case 93:
                    $logCode = '05301A07';
                    break;
                default :
                    $logCode = '05301A06';
            }
            goto fail_del_vd;        
        }
        connectDB::$dbh->commit();        
        $this->checkANDDeleteVolume();
        $logCode = '05101901';        
        $rtn = CPAPIStatus::DeleteVDSuccess;    
        goto end;
        fail:
            $rtn = CPAPIStatus::DeleteVDFail;
            goto end;
        fail_del_vd:               
            connectDB::$dbh->rollBack();            
            $rtn = CPAPIStatus::DeleteVDFail;
            goto end;
        end:
            if($isLog){
                $append_err = $this->logVDAppendErr($logCode);
                $this->insertDeleteVDLog($logCode,$input['VDName'],$output_domain['Name'],$input['DomainID'],$append_err);
            }
            //var_dump($logCode);
            return $rtn;
    }
    
    function insertDeleteVDLog($code,$vdName,$domainName,$domainID,$appendErr)
    {
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);    
        if(is_null($appendErr)){
            $message = "Delete VD$appendVDDomain Success";
        }
        else{
            $message = "Failed to delete VD$appendVDDomain$appendErr";
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function deleteVDShell($input)
    {       
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDDel;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.$input['VDID'].'" ';   
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);
        }     
        // var_dump($rtn);
        return $rtn;
    }

    function poweronVD($input,&$rtn=99,$riser='admin',$isView=false,$isViewer=false)
    {        
        $rtn = 999;        
        $log = function ($logCode,$input)use($riser)
        {
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertPoweronVDLog($logCode,$input['VDName'],$input['DomainName'],$input['DomainID'],$appendErr,$riser);
        };       
        $logCode = "";       
        // $gmt = $this->getTZ($timezone);        
        // date_default_timezone_set($timezone);          
        // $fp = fopen('/mnt/tmpfs/cephcmd/cmd.txt', 'a');
        // fwrite($fp, date("Y-m-d H:i:s").' Start '.$_SERVER['REMOTE_ADDR'].PHP_EOL);
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }        
        $this->newDomainClass();   
        $this->domainC->sqlListDomainByID($input['DomainID'], $output_domain);                
        foreach ($input['VDs'] as $id){    
            $outputVD = null;      
            if(!$isView){ 
                $this->sqlListVDByIDPoweron($id,$outputVD);
                $input['DomainID']=$outputVD['DomainID'];
                $input['VDName']=$outputVD['Name'];            
                $input['VDNumber']=$outputVD['VDNumber'];  
                // var_dump($outputVD);
                if(is_null($outputVD)){                
                    continue;
                }                    
            }           
            $this->listVDShell(array('ConnectIP'=>$input['ConnectIP'],'VDID'=>$id),$outputVDShell);        
            if($outputVDShell['vdState'] == 'poweroff'){            
                $this->sqlClearVDRDPConfig(array('ConnectIP'=>$input['ConnectIP'],'VDID'=>$id));
            }    
            $input['VDID'] = $id;
            
            // fwrite($fp, date("Y-m-d H:i:s").$input['VDID'].' Before Poweon Shell'.$_SERVER['REMOTE_ADDR'].PHP_EOL);           
            if($outputVDShell['vdState'] != 'poweron'){
                $rtn=$this->poweronVDShell($input,$isView,$isViewer);   
                // var_dump($rtn);
                // sleep(10);                                
                // fwrite($fp, date("Y-m-d H:i:s").' '.$input['VDID'].' After Poweon Shell '.$_SERVER['REMOTE_ADDR'].PHP_EOL);
                if($rtn == 0 || $rtn == 91 || $rtn == 102){
                    if($outputVDShell['vdState'] == 'poweroff'){  
                        $input['Stage'] =0;
                        $this->sqlUpdateRDPStage($input);
                    }
                    if($rtn == 0 || $rtn == 102){                        
                        $logCode = '05100F01';               
                        $log($logCode,$input);
                    }else{
                        if($isViewer){
                            $logCode = '05100F02';
                            $log($logCode,$input);
                        }
                    }
                }
                else{
                    switch ($rtn)
                    {
                        case 95:
                            $logCode = '05301005';                     
                            break;
                        case 94:
                            $logCode = '05301006';                        
                            break;
                        case 93:
                            $logCode = '05301007';                        
                            break;
                        case 92:
                            $logCode = '05301008';
                            break;
                        default :
                            $logCode = '05301003';                        
                    }
                    $log($logCode,$input);
                }
            }            
            else{
                $rtn = 0;    
            }
        }       
        //fclose($fp);           
    }
    
    function poweronVDRDP($input,&$rtn=99,$riser='admin',$isViewer=true)
    {        
        $rtn = 999;        
        $log = function ($logCode,$input)use($riser)
        {
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertPoweronVDLog($logCode,$input['VDName'],$input['DomainName'],$input['DomainID'],$appendErr,$riser);
        };       
        $logCode = "";             
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }        
        $this->newDomainClass();   
        $this->domainC->sqlListDomainByID($input['DomainID'], $output_domain);             
        $outputVD = null;              
        $this->sqlListVDByIDPoweron($input['VDID'],$outputVD);
        $input['DomainID']=$outputVD['DomainID'];
        $input['VDName']=$outputVD['Name'];            
        $input['VDNumber']=$outputVD['VDNumber'];  
        // var_dump($outputVD);
        if(is_null($outputVD)){                
            return CPAPIStatus::NotFound;
        }                          
        $this->listVDShell($input,$outputVDShell);        
        if($outputVDShell['vdState'] == 'poweroff'){            
            $this->sqlClearVDRDPConfig($input);
        }      
        if($outputVDShell['vdState'] != 'poweron'){       
            $rtn=$this->poweronVDShell($input,$isView,$isViewer);
            if($rtn == 0 || $rtn == 91 || $rtn == 102){
                if($outputVDShell['vdState'] == 'poweroff'){  
                    $input['Stage'] =0;
                    $this->sqlUpdateRDPStage($input);
                }
                if($rtn == 0 || $rtn == 102){                        
                    $logCode = '05100F01';               
                    $log($logCode,$input);
                }else{
                    if($isViewer){
                        $logCode = '05100F02';
                        $log($logCode,$input);
                    }
                }
            }
            else{
                switch ($rtn)
                {
                    case 95:
                        $logCode = '05301005';                     
                        break;
                    case 94:
                        $logCode = '05301006';                        
                        break;
                    case 93:
                        $logCode = '05301007';                        
                        break;
                    case 92:
                        $logCode = '05301008';
                        break;
                    default :
                        $logCode = '05301003';                        
                }
                $log($logCode,$input);
            }    
        }     
        else{
            $rtn = 0;
        }            
    }    

    function rdpListInfoWithMAC($input,&$output)
    {          
        $output['VDID'] = NULL;
        $insertData = array();
        $insertData['Address'] = array();
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }     
        foreach ($input as $value) {            
            if(is_null($output['VDID'])){
                $this->sqlListVDByMAC($value,$outputVD);               
                $output['VDID'] = $outputVD['VDID'];
            }            
        }
        if(is_null($outputVD['VDID']))
            return CPAPIStatus::ListRDPInfoFail;
        // var_dump($outputVD);
        $domainC = new DomainAction();
        $domainC->sqlListDomainByID($outputVD['DomainID'], $outputDomain);    
        // var_dump($outputDomain);
        $adAuth = false;
        if(!is_null($outputDomain['AD'])){
            $ad = base64_decode($outputDomain['AD']);
            $ad = json_decode($ad,true);
            if($ad['Enable'])
                $adAuth = true;
        }
        $adDomain = '';
        if(isset($ad['DomainName']) && $adAuth)
            $adDomain = $ad['DomainName'];
        $output['IsAdUser'] = $adAuth;    
        $output['ADDomain'] = $adDomain;    
        $this->sqlListVDUserIDByVDID($output['VDID'],$outputID);        
        if(is_null($outputID['OrgUserID']) && is_null($outputID['UserUserID']))
            return CPAPIStatus::ListRDPInfoFail;
        if(!is_null($outputID['OrgUserID']))
            $idUser = $outputID['OrgUserID'];
        if(!is_null($outputID['UserUserID']))
            $idUser = $outputID['UserUserID'];
        $this->sqlListVDInfoByID($outputVD['VDID'],$outputVDInfo);
        if(is_null($outputVDInfo))
            return CPAPIStatus::ListRDPInfoFail;        
        $output['RDPPWDTime']=(int)$outputVDInfo['RDPPWDTime'];
        $userC = new UserAction();
        $outputUser = NULL;
        $userC->sqlListUserWithPWDByID($idUser,$outputUser);
        if(is_null($outputUser))
            return CPAPIStatus::ListRDPInfoFail;
        $this->sqlListVDIDOfUserDiskbyiduser($idUser,$outputVDIDofDisk);
        // var_dump($outputVDIDofDisk);
        $output['IsAssignedUserDisk']=false;
        if(isset($outputVDIDofDisk) && $outputVDIDofDisk == $outputVD['VDID'])
            $output['IsAssignedUserDisk']=true;
        $output['Username']=$outputUser['Name'];
        if($adAuth){
            if(file_exists("/mnt/tmpfs/ad/$idUser")){
                $output['Password'] = file_get_contents("/mnt/tmpfs/ad/$idUser");
            }else{
                $output['Password'] = '';
            }
        }
        else{
            $output['Password']=$outputUser['Password'];
        }
        $outputVD['Stage'] = 1;
        $this->sqlUpdateRDPStage($outputVD);
        return CPAPIStatus::ListRDPInfoSuccess;
    }

    function listUserPWDofVD($input,&$output)
    {
        $userC = new UserAction();
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }  
        // var_dump($input);
        $this->sqlListVDUserIDByVDID($input['VDID'],$outputID); 
        // var_dump($outputID);       
        if(is_null($outputID['OrgUserID']) && is_null($outputID['UserUserID']))
            return CPAPIStatus::ListRDPInfoFail;
        if(!is_null($outputID['OrgUserID']))
            $idUser = $outputID['OrgUserID'];
        if(!is_null($outputID['UserUserID']))
            $idUser = $outputID['UserUserID'];
        $userC->sqlListUserWithPWDByID($idUser,$outputUser);        
        if(is_null($outputUser))
            return CPAPIStatus::ListRDPInfoFail;        
        $output['Username']=$outputUser['Name'];
        $output['Password']=$outputUser['Password'];
        return CPAPIStatus::ListRDPInfoSuccess;
    }

    function insertRDPInfoWithVDID($input,&$output)
    {        
        $insertData = array();
        $insertData['Address'] = array();
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }     
        foreach ($input['Address'] as $value) {
            // if (preg_match('/^192.168.177.[0-9]+$/', $value['IP'])) {                 
            //     $lastIPRange = explode( '.', $value['IP'])[3];                
            //     if($lastIPRange >= 51 && $lastIPRange <= 100){
            //         $port = $lastIPRange + 16800;
            //         $insertData['Address'][] = array("IP"=>'220.130.225.17',"Port"=>$port);
            //     }
            //     else{
            //         $insertData['Address'][] = array("IP"=>$value['IP'],"Port"=>3389);
            //     }
            // }
            // else{
            $insertData['Address'][] = array("IP"=>$value['IP'],"Port"=>3389);
            // }                                                       
        }
        if(count($insertData['Address']) > 0){
            $insertData['VDID'] = $input['VDID'];
            if(!$this->sqlInsertRDPConfig($insertData)){
                return CPAPIStatus::InsertRDPFail;
            }
            $insertData['PWDChange'] = false;
            $insertData['RDPFirst'] = 0;
            if(!$this->sqlUpdateVDChangePWD($insertData)){
                return CPAPIStatus::InsertRDPFail;
            }
            if(!$this->sqlUpdateVDRDPFirst($insertData)){
                return CPAPIStatus::InsertRDPFail;
            }
            $insertData['Stage'] = 5;
            $this->sqlUpdateRDPStage($insertData);
            return CPAPIStatus::InsertRDPSuccess;
        }
        else{
            return CPAPIStatus::InsertRDPFail;
        }
    }

    function insertRDPInfo($input,&$output)
    {
        $output['VDID'] = NULL;
        $insertData = array();
        $insertData['Address'] = array();
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }     
        foreach ($input['Address'] as $value) {            
            if(is_null($output['VDID'])){
                $this->sqlListVDByMAC($value['MAC'],$outputVD);
                // var_dump($outputVD);
                $output['VDID'] = $outputVD['VDID'];
            }
            // if($value['IP'] == '192.168.77.93')
            //     $insertData['Address'][] = '220.130.225.17';
            // else
            $insertData['Address'][] = $value['IP'];
        }
        if(!is_null($output['VDID']) && count($insertData['Address']) > 0){
            $insertData['VDID'] = $output['VDID'];
            if(!$this->sqlInsertRDPConfig($insertData)){
                return CPAPIStatus::InsertRDPFail;
            }
            return CPAPIStatus::InsertRDPSuccess;
        }
        else{
            return CPAPIStatus::InsertRDPFail;
        }
    }

    function updateRDPStage($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        } 
        // var_dump($input);
        $this->sqlUpdateRDPStage($input);
    }

    function updateRDPOnline($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        } 
        // var_dump($input);
        if($input['RDPOnline'] == 3 || $input['RDPOnline'] == 4 || $input['RDPOnline'] == 6)
            $this->sqlUpdateRDPOnline($input);
    }

    function changeVDRDPStatus($input)
    {
        $this->listVDShell($input,$outputVDShell);    
        switch ($input['Status']) {
            case 1:
                if($outputVDShell['vdState'] == 'poweroff'){            
                    $this->sqlClearVDRDPConfig($input);
                }  
                break;                
            default:                
                break;
        }        
    }

    function changeVDPWD($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        $input['PWDChange'] = false;
        if(!$this->sqlUpdateVDChangePWD($input)){
            return CPAPIStatus::InsertRDPFail;
        }
        return CPAPIStatus::InsertRDPSuccess;
    }

    function sqlListVDByMAC($mac,&$output)
    {
        $output = NULL;
        $result = false;        
        $sqlList = <<<SQL
            SELECT t1.idVD,t2.idDomain FROM tbVDImageBaseInfoSet as t1 inner join tbVDImageBaseSet as t2 on t1.idVD=t2.idVD WHERE t1.typeNic Like :typeNic;
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($sqlList);              
            $sth->bindValue(':typeNic', "%$mac%", PDO::PARAM_STR);  
            if($sth->execute())
            {                                  
                while( $row = $sth->fetch() ) 
                {
                    $output['VDID'] = $row['idVD'];
                    $output['DomainID'] = $row['idDomain'];
                }          
                $result = true;
            }                        
        }
        catch (Exception $e){    
            $result = false;
        }     
        return $result;
    }

    function sqlInsertRDPConfig($input)
    {
        $result = false;
        $idVD = $input['VDID'];
        unset($input['VDID']);        
        $sqlClear = <<<SQL
            Update tbVDImageBaseInfoSet SET rdp=:rdp WHERE idVD = :idVD
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($sqlClear);              
            $sth->bindValue(':idVD', $idVD, PDO::PARAM_STR);  
            $jsonData=json_encode($input);
            $sth->bindValue(':rdp', $jsonData, PDO::PARAM_STR);  
            if($sth->execute())
            {                                                
                $result = true;
            }                        
        }
        catch (Exception $e){    
            $result = false;
        }     
        return $result;
    }

    function sqlClearVDRDPConfig($input)
    {
        $result = false;
        $sqlClear = <<<SQL
            Update tbVDImageBaseInfoSet SET rdp='',rdpStage=0,rdpTimeStamp=0 WHERE idVD = :idVD
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($sqlClear);              
            $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_STR);  
            if($sth->execute())
            {                                                
                $result = true;
            }                        
        }
        catch (Exception $e){    
            $result = false;
        }     
        return $result;
    }

    function sqlUpdateRDPStage($input)
    {
        $result = false;        
        $sqlUpdate = <<<SQL
            Update tbVDImageBaseInfoSet SET rdpStage=:rdpStage,rdpTimeStamp=UNIX_TIMESTAMP(NOW()) WHERE idVD = :idVD AND (rdpStage = 0 OR rdpStage <> :rdpStage);
SQL;
        try
        {                     
            // var_dump($input);       
            // var_dump($sqlUpdate);
            $sth = connectDB::$dbh->prepare($sqlUpdate);     
            $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_STR);           
            $sth->bindValue(':rdpStage', $input['Stage'], PDO::PARAM_INT);            
            if($sth->execute())
            {                                                
                $result = true;
            }                        
        }
        catch (Exception $e){    
            $result = false;
        }     
        return $result;
    }

    function sqlUpdateRDPOnline($input)
    {
        $result = false;        
        $sqlUpdate = <<<SQL
            Update tbVDImageBaseInfoSet SET isRDPOnline=:isRDPOnline WHERE idVD = :idVD;
SQL;
        try
        {                     
            // var_dump($input);       
            // var_dump($sqlUpdate);
            $sth = connectDB::$dbh->prepare($sqlUpdate);     
            $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_STR);           
            $sth->bindValue(':isRDPOnline', $input['RDPOnline'], PDO::PARAM_INT);            
            if($sth->execute())
            {                                                
                $result = true;
            }                        
        }
        catch (Exception $e){    
            $result = false;
        }     
        return $result;
    }

    function insertPoweronVDLog($code,$vdName,$domainName,$domainID,$appendErr,$riser)
    {
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        // var_dump($code);
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);    
        if(is_null($appendErr)){
            if($code != '05100F02')
                $message = "Poweron VD$appendVDDomain";
            else
                $message = "Connect VD$appendVDDomain";
        }
        else{
            $message = "Failed to poweron VD$appendVDDomain$appendErr";
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>$riser,'Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function modifyVDPasswdShell($input)
    {
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdVDPasswdSet;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);              
            $cmd .= '"'.$input['VDID'].'" ';   
            $passwd = htmlspecialchars($input['Password'],ENT_QUOTES);
            $cmd .= "'$passwd'";    
            // var_dump($cmd);
            exec($cmd,$output,$rtn);                              
        }               
        return $rtn;
    }     

    function poweronInfoShell($input,&$output)
    {
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDPoweronInfo;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);              
            $cmd .= '"'.$input['VDID'].'" ';   
            // var_dump($cmd);   
            exec($cmd,$output,$rtn);                                           
            // var_dump($rtn);
            if($rtn == 0){
                $output = json_decode($output[0],true);
                // var_dump($output);                        
            }
        }       
        return $rtn;
    }

    function poweronVDShell($input,$isView=false,$isViewer = false)
    {        
        $sqlLock = 'LOCK TABLES `tbdomainsettingset` AS t1 WRITE';
        $sqlUnLock = 'UNLOCK TABLES ';
        $rtn = -2;       
        if(!is_null($input['ConnectIP'])){     
            if(!$isView){                
                $this->attachUserDisk($input);           
            } 
            // $fp = fopen('/mnt/tmpfs/cephcmd/cmd.txt', 'a');
            if($isViewer)  
                $cmd = $this->cmdVDPoweronViewer;
            else
                $cmd = $this->cmdVDPoweron;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);              
            $cmd .= '"'.$input['VDID'].'" ';
            // fwrite($fp, date("Y-m-d H:i:s").' '.$cmd.PHP_EOL);
            $sth=connectDB::$dbh->prepare($sqlLock);
            $sth->execute();
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);            
            // var_dump($rtn);
            $sth=connectDB::$dbh->prepare($sqlUnLock);
            $sth->execute();
            // $outputArr=system($cmd,$rtn);
            // fwrite($fp, date("Y-m-d H:i:s").' '.$rtn.PHP_EOL);
            // fclose($fp);
        }            
        return $rtn;
    }    

    function attachUserDisk($input)
    {
        $outputDisk = null;        
        $this->sqlListUserDiskByVDID($input['VDID'],$outputDisk);                  
        $input['DiskType'] = $outputDisk['DiskType'];
        $input['DiskCache'] = $outputDisk['DiskCache'];           
        // var_dump($disk_name_arr);
        // var_dump($outputDisk['UserID']);
        // var_dump($outputDisk);
        if(is_null($outputDisk)){           
            $this->detachUserDiskShell($input);
        }
        else{                                  
            $outputDiskUserID = null;
            $this->sqlListVDUserIDByVDID($outputDisk['VDID'], $outputDiskUserID);
            // var_dump($outputDiskUserID);
            // var_dump($outputDisk);
            if(!is_null($outputDiskUserID)){
                if(isset($outputDiskUserID['OrgUserID'])){
                    if($outputDisk['UserID'] != $outputDiskUserID['OrgUserID'])
                        $outputDisk['VDID'] = null;       
                }
                if(isset($outputDiskUserID['UserUserID'])){
                    if($outputDisk['UserID'] != $outputDiskUserID['UserUserID']){
                        $outputDisk['VDID'] = null;                               
                    }
                }                    
            }
            // var_dump($outputDisk);
            $input['DiskName'] = $outputDisk['DiskName'];          
            if($outputDisk['VDID'] == null){   
                // var_dump('====Update====');  
                $this->sqlListVDUserIDByVDID($input['VDID'], $outputUserID);
                // var_dump($outputUserID);
                if(isset($outputUserID['UserUserID'])){
                    $this->sqlUpdateVDIDofUserDisk($outputDisk['ID'], $input['VDID']);
                    $this->attachUserDiskShell($input);
                }
            }
            else{
                if($outputDisk['VDID'] != $input['VDID']){
                    $this->detachUserDiskShell($input);
                }
                else{
                    // var_dump('====Attach====');
                    $this->attachUserDiskShell($input);
                }
            }
            
        }            
    }

    function sqlListUserDiskByVDID($id,&$output)
    {
        $SQLList = <<<SQL
            SELECT t1.idUser,t3.idVD,t3.nameVDisk,t3.idVDisk,t3.configVDisk 
                FROM tbvdimageset as t1
                INNER JOIN tbuserdiskset as t2
                ON t1.idUser=t2.idUser
                INNER JOIN tbVDiskSet as t3
                ON t2.idVDisk=t3.idVDisk
                WHERE t1.idVD=:idVD;
SQL;
        try
        { 
            $sth = connectDB::$dbh->prepare($SQLList);
            $sth->bindValue(':idVD', $id, PDO::PARAM_STR);              
            if($sth->execute()){                                        
                while( $row = $sth->fetch() ) 
                {                                    
                    $diskType = 0;
                    $diskCache = 0;
                    $configVDisk = json_decode($row['configVDisk'],true);
                    // var_dump($configVDisk);
                    if(isset($configVDisk['DiskType'])){
                        $diskType = $configVDisk['DiskType'];
                        $diskCache = $configVDisk['DiskCache'];
                    }          
                    $output =  array('DiskName'=>$row['nameVDisk'],
                        'UserID'=>$row['idUser'],
                        'VDID'=>$row['idVD'],
                        'ID'=>$row['idVDisk'],
                        'DiskType'=>$diskType,
                        'DiskCache'=>$diskCache,
                        );               
                }                            
            }
        }
        catch (Exception $e){
            
        }
    }
        
    function attachUserDiskShell($input)
    {
        // var_dump($input);
        $rtn = -2;
        $cmdAppend = '';
        $cmdAppend .= '"'.$input['VDID'].'" ';
        $cmdAppend .= '"'.$input['DiskName'].'" ';
        switch ($input['DiskType']) {
                case 0:
                    $diskType = 'ide';
                    break;
                case 1:
                    $diskType = 'scsi';
                    break;
                case 2:
                    $diskType = 'virtio';
                    break;
                default:
                    $diskType = 'ide';
                    break;
            }
        $cmdAppend .= $diskType.' ';
        $diskCache = $input['DiskCache'] == 0 ? 'writethrough' : 'writeback';
        $cmdAppend .= $diskCache.' ';               
        $cmd = $this->cmdVDUserProfileAttach;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= $cmdAppend;       
        // var_dump($cmd);
        exec($cmd,$output,$rtn);                      
        // var_dump($rtn);
        return $rtn;
    }
    
    function detachUserDiskShell($input)
    {
        $rtn = -2;
        $cmd_append = '';
        $cmd_append .= '"'.$input['VDID'].'" ';
        // $cmd_append .= '"'.$input['DiskName'].'"';
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDUserProfileDetach;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= $cmd_append;    
            // var_dump($cmd);
            exec($cmd,$output,$rtn);           
            // var_dump($rtn);
        }                 
        return $rtn;
    }

    function poweronVDBeforeShell($input)
    {
        // var_dump($input);
        $rtn = -2;       
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdVDPoweronBefore;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);              
            $cmd .= '"'.$input['VDID'].'" ';            
            exec($cmd,$outputArr,$rtn);            
        }        
        return $rtn;
    }

    function shutdownVD($input)
    {
        $logCode = "";       
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }        
        $this->newDomainClass();      
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);     
        foreach ($input['VDs'] as $id){         
            $output_vd = null;
            $this->sqlListVDByID($id,$output_vd);
            if(is_null($output_vd)){
               continue;
            }                       
            $input['VDID'] = $id;
            $input['DomainID']=$output_vd['DomainID'];
            $input['VDName']=$output_vd['Name'];         
            $input['VDNumber']=$output_vd['VDNumber'];           
            $rtn = $this->shutdownVDShell($input);            
            if($rtn == 0){
                $logCode = '05101301';                
            }
            else{
                $logCode = '05301403';                
            }
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertShutdownVDLog($logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],$appendErr);
        }   
    }
    
    function insertShutdownVDLog($code,$vdName,$domainName,$domainID,$appendErr)
    {
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);    
        if(is_null($appendErr)){
            $message = "Shutdown VD$appendVDDomain";
        }
        else{
            $message = "Failed to shutdown VD$appendVDDomain$appendErr";
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function shutdownVDShell($input)
    {
        $rtn = -2;       
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdVDShutdown;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);              
            $cmd .= '"'.$input['VDID'].'" >/dev/null 2>&1 &';
            $pipe = popen($cmd,"r");    
            pclose($pipe); 
            $rtn = 0;                       
        }            
        return $rtn;
    }

    function poweroffVD($input,$riser='admin')
    {      
        $logCode = "";       
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }               
        $this->newDomainClass();      
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);    
        foreach ($input['VDs'] as $id){                     
            if(!$this->sqlListVDByID($id,$output_vd)){
                continue;
            }                    
            $input['VDID']=$id;
            $input['DomainID']=$output_vd['DomainID'];
            $input['VDName']=$output_vd['Name'];            
            $input['VDNumber']=$output_vd['VDNumber']; 
            // sleep(10);                       
            $rtn = $this->poweroffVDShell($input);                
            if($rtn == 0){
                $logCode = '05101101';                
            }
            else{
                $logCode = '05301203';                
            }
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertPoweroffVDLog($logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],$appendErr,$riser);
        }
    }
    
    function insertPoweroffVDLog($code,$vdName,$domainName,$domainID,$appendErr,$riser)
    {
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);    
        if(is_null($appendErr)){
            $message = "Poweroff VD$appendVDDomain";
        }
        else{
            $message = "Failed to poweroff VD$appendVDDomain$appendErr";
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>$riser,'Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function poweroffVDShell($input)
    {
        $rtn = -2;       
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdVDPoweroff;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);              
            $cmd .= '"'.$input['VDID'].'" >/dev/null 2>&1 &';
            // var_dump($cmd);
            $pipe = popen($cmd,"r");    
            pclose($pipe);                        
            $rtn = 0;
        }            
        return $rtn;
    }

    function modifyVD($input)
    {
        $logCode = "";
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }        
        $this->newDomainClass();   
        $this->newUserClass();             
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);        
        if(isset($input['NodeID'])){
            $nodeC = new NodeAction();
            $nodeC->sqlListServerByidVDServer($input['NodeID'],$outputNode);
            if(is_null($outputNode)){
                return CPAPIStatus::ModifyVDFail;
            }     
            $input['PreferNodeAddress'] = $outputNode['CommunicateIP'];
        }
        if(!$this->sqlListVDInfoByID($input['VDID'],$output)){  
            $logCode = '05300E01';
            goto fail;
        }
        $input['VDNumber']=$output['VDNumber'];                
        $this->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$outputVD);
        if(!is_null($outputVD) && $input['VDID'] != $outputVD['VDID']){            
            $logCode = '05300E02';
            $rtn = CPAPIStatus::Conflict;
            goto fail;
        }
        $this->sqlListVDUserIDByVDID($input['VDID'],$outputUserID);   
        if(!isset($input['UEFI']))
           $input['UEFI'] = false;
       if(!isset($input['QXL']))
           $input['QXL'] = true;
        if(!isset($input['VGACount']))
           $input['VGACount'] = 0;     
        connectDB::$dbh->beginTransaction();                
        if(!$this->sqlUpdateVDPreferNode($input)){
            $logCode='05300E05-1';
            goto failRollBack;
        }
        if($this->modifyVDPreferNodeShell($input,$rtnCode) != 0){
            $logCode='05300E06-1';
            goto failRollBack;
        }
        connectDB::$dbh->commit();
        connectDB::$dbh->beginTransaction();                
        if(!$this->sqlUpdateVD($input)){
            $logCode='05300E05';
            goto failRollBack;
        }        
        if(isset($input['ViewerProtocol'])){
            if(!$this->sqlUpdateVDViewerProtocol($input)){
                $logCode='05300E05';
                goto failRollBack;        
            }
        }
        // if(!is_null($outputUserID)){
        //     $userID=$outputUserID['OrgUserID']==NULL?$outputUserID['UserUserID']:$outputUserID['OrgUserID'];            
        //     $this->userC->sqlUpdateUserName((int)$userID,$input['VDName']);
        // }
        if($this->modifyVDShell($input,$rtnCode) != 0){
            $logCode='05300E06';
            goto failRollBack;
        }
        connectDB::$dbh->commit();
        $logCode = '05100D01';       
        $rtn = CPAPIStatus::ModifyVDSuccess;
        goto end;
        fail:
            $rtn = CPAPIStatus::ModifyVDFail;
            goto end;
        failRollBack:
            connectDB::$dbh->rollBack();                 
            $rtn = CPAPIStatus::ModifyVDFail;
            goto end;
        end:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertModifyVDLog($logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],$appendErr,$rtnCode);
            return $rtn;        
    }

    function insertModifyVDLog($code,$vdName,$domainName,$domainID,$appendErr,$errCode=NULL)
    {
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);    
        if(is_null($appendErr)){
            $message = "Modify VD$appendVDDomain Success";
        }
        else{
            $message = "Failed to modify VD$appendVDDomain$appendErr";
            if(isset($errCode)){
                $rtnMsg = $this->changeModifyReturnCode($errCode);
                $message .= "($rtnMsg)";
            }           
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function modifyVDShell($input,&$rtnCode)
    {
        $rtn=-99;     
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDModify;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.$input['VDID'].'" ';
            $cmd .= $input['CpuCt'].' ';
            $cmd .= ($input['RAM']*1024).' ';  
            $cdrom = '';
            if(strlen($input['CDRom']) > 0 && strtolower($input['CDRom']) != 'none')
                $cdrom = $input['CDRom'];
            $cmd .= '"'.$cdrom.'" ';      
            $desc = '"'.$input['VDName'].'"';
            $cmd .= $desc.' ';
            $cmd .= $input['USBType'].' ';
            $cmd .= $input['USBRedirCt'].' ';
            switch($input['Sound']){
                case 1:
                    $sound = 'ich6';
                    break;
                case 2:
                    $sound = 'ac97';
                    break;
            }
            $cmd .= '"'.$sound.'" ';
            if($input['CPU'] == 0)
                $input['CPU'] = -1;
            $cmd .= $input['CPU'].' ';
            $suspend = $input['Suspend'] == false ? 0 : 1;
            $cmd .= $suspend.' ';    
            $cpuType = $input['CPUType'] == 0 ? 'fv' : 'hv';
            $cmd .= $cpuType.' ';       
            $uefi = $input['UEFI'] == true ? 'uefi':'legacy';
            $cmd .= $uefi.' ';
            $vgaCount = $input['VGACount'];            
            $cmd .= $vgaCount.' ';
            $qxl = $input['QXL'] == false ? '0' : '1'; 
            $cmd .= $qxl.' ';
            // var_dump($cmd);                
            exec($cmd,$output,$rtn);  
            $rtnCode = $rtn;
            // var_dump($rtn);                                               
        }                    
        return $rtn;
    }

    function modifyVDPreferNodeShell($input)
    {
        $rtn=-99;     
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDPoweronPreferNodeSet;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.$input['VDID'].'" ';
            $cmd .= $input['PreferNodeAddress'] == null ? '""' : '"'.$input['PreferNodeAddress'].'"';
            // var_dump($cmd);
            exec($cmd,$output,$rtn);
            // var_dump($rtn);                                               
        }                    
        return $rtn;
    }

    function modifyVDVideoShell($input)
    {
        $rtn=-99;     
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDVideoSet;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.$input['VDID'].'" ';
            $cmd .= $input['Monitor'].' ';    
            // var_dump($cmd);        
            exec($cmd,$output,$rtn);     
            // var_dump($rtn);                                            
        }                    
        return $rtn;
    }

    function modifyVDISO($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }                
        if(!$this->sqlListVDByID($input['VDID'],$output)){
            return CPAPIStatus::NotFound;
        }                      
        $input['VDNumber'] = $output['VDNumber'];
        if($this->modifyVDISOShell($input) == 0){
            $this->sqlUpdateVDISO($input['VDID'],$input['Path']);
            $this->sqlUpdateVDModfiyTime($input['VDID']);
            return CPAPIStatus::ModifyISOSuccess;
        }
        else
            return CPAPIStatus::ModifyISOFail;
    }
    
    function modifyVDISOShell($input)
    {
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){
             if(strlen($input['Path']) == 0 || strtolower($input['Path']) == 'none')
                $cmd = $this->cmdVDISODetach;
            else
                $cmd = $this->cmdVDISOAttach;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);                 
            $cmd .= '"'.$input['VDID'].'" ';
            if(strlen($input['Path']) > 0 && strtolower($input['Path']) != 'none')
                $cmd .= '"'.$input['Path'].'"';                            
            exec($cmd,$outputArr,$rtn);
        }    
        return $rtn;
    }    

    function modifyOnlineVD($input)
    {
        $result = true;
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        if(!$this->sqlListVDByID($input['VDID'],$output)){
            return CPAPIStatus::NotFound;
        }                      
        if(isset($input['NodeID'])){
            $nodeC = new NodeAction();
            $nodeC->sqlListServerByidVDServer($input['NodeID'],$outputNode);
            if(is_null($outputNode)){
                return CPAPIStatus::ModifyVDFail;
            }
            $input['PreferNodeAddress'] = $outputNode['CommunicateIP'];
        }         
        if($this->modifyVDPreferNodeShell($input) == 0){
            $this->sqlUpdateVDPreferNode($input);
        }
        else
            $result=false;          
        $input['VDNumber'] = $output['VDNumber'];
        if($this->modifyVDISOShell($input) == 0){
            $this->sqlUpdateVDISO($input['VDID'],$input['Path']);                        
        }
        else
            $result=false;        
        if($this->setVDAutosuspendShell($input) == 0){
            $this->sqlUpdateVDAutoSuspenDescription($input);
        }else{
            $result=false;
        }
        if(isset($input['ViewerProtocol'])){
            if(!$this->sqlUpdateVDViewerProtocol($input))
                $result = false;
        }
        if(!$result)
            return CPAPIStatus::ModifyVDFail;
        else
            return CPAPIStatus::ModifyVDSuccess;
    }

   function modifyVDPreferNode($input,&$output)
    {
        $result = true;
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        if(!$this->sqlListVDByID($input['VDID'],$output)){
            return CPAPIStatus::NotFound;
        }     
        if(isset($input['NodeID'])){
            $nodeC = new NodeAction();
            $nodeC->sqlListServerByidVDServer($input['NodeID'],$outputNode);
            if(is_null($outputNode)){
                return CPAPIStatus::ModifyVDFail;
            }
            $input['PreferNodeAddress'] = $outputNode['CommunicateIP'];
        }                 
        if($this->modifyVDPreferNodeShell($input) == 0){
            $this->sqlUpdateVDPreferNode($input);
            $output = array();
            $output['PreferNodeID'] = $input['NodeID'];
            $output['PreferNodeName'] = $outputNode['NodeName'];
            return CPAPIStatus::ModifyVDSuccess;
        }else{
            return CPAPIStatus::ModifyVDFail;
        }
    }
    function modifySeedVD($input)
    {
        // var_dump($input);
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }          
        $outputVD = null;
        $this->sqlListVDByNameidDomain($input['Name'],$input['DomainID'],$outputVD);
        if(!is_null($outputVD) && $input['VDID'] != $outputVD['VDID'])
            return CPAPIStatus::Conflict;    
        if(!$this->sqlUpdateSeedVD($input))
            return CPAPIStatus::ModifyVDFail;
        return CPAPIStatus::ModifyVDSuccess;
    }
    
    function sqlUpdateSeedVD($input)
    {
        $result = false;
        $SQLUpdateName = <<<SQL
            Update tbVDImageBaseSet SET nameVD=:nameVD,date_modified=now() WHERE idVD = :idVD
SQL;
        $SQLUpdateInfo = <<<SQL
            Update tbVDImageBaseInfoSet SET Description=:Description WHERE idVD = :idVD
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($SQLUpdateName);  
            $sth->bindValue(':nameVD', $input['Name'], PDO::PARAM_STR); 
            $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_STR);  
            if($sth->execute())
            {                                
                $sth = connectDB::$dbh->prepare($SQLUpdateInfo);
                $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_STR);                 
                $sth->bindValue(':Description', $input['Desc'], PDO::PARAM_STR);
                if($sth->execute())
                    $result = true;
            }                        
        }
        catch (Exception $e){    
            $result = false;
        }     
        return $result;
    }

    function disableVD($input)
    {                
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }        
        $this->newDomainClass();      
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);     
        foreach ($input['VDs'] as $id){         
            $output_vd = null;
            $this->sqlListVDByID($id,$output_vd);
            if(is_null($output_vd)){
               continue;
            }                       
            $input['VDID'] = $id;
            $input['DomainID']=$output_vd['DomainID'];
            $input['VDName']=$output_vd['Name'];         
            $input['VDNumber']=$output_vd['VDNumber'];           
            $rtn = $this->disableVDShell($input);                       
        }         
    }
    
    function disableVDShell($input)
    {
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdVDDisableSet;            
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);                 
            $cmd .= '"'.$input['VDID'].'" ';
            $cmd .= $input['Disable'] == true ? 1 : 0;
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);
        }    
        return $rtn;        
    }

    function exportVD($input)
    {    
        // var_dump($input);
        $logCode = '';
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }      
        $this->newDomainClass();
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);        
        if(!$this->sqlListOrgVDforExport($input['VDID'], $output_vd)){
            $logCode = '05300A01';
            goto fail;     
        }               
        $input['DomainName'] = $outputDomain['Name'];
        $input['VDNumber'] = $output_vd['VDNumber'];
        $input['VDName'] = $output_vd['Name'];
        $result = $this->exportVDShell($input);
        if( $result != 0){                        
            if($result == 40)
                $logCode = '05300A03-40';
            else
                $logCode = '05300A03';
            goto fail;
        }
        $logCode = '05100901';      
        $rtn = CPAPIStatus::ExportVDSuccess;
        goto end;
        fail:
            $rtn = CPAPIStatus::ExportVDFail;
            $this->insertExportVDFailTaskShell($input);
            goto end;
        end:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertExportVDLog($logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],$appendErr);
            return $rtn;
    }

    function reportVDTaskExport($input)
    {
        $haveTask = false;
        $cancelTask = false;
        $logCode = '05300A04';
        $rtnCode = 99;
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFilnal;
        }                    
        $this->processReportData($input,$data);       
        if($data['ErrCode'] == 0){      
            $rtnCode = 0;                  
            $logCode = '05100902';
            goto rtnFilnal;
        }       
        else if ($data['ErrCode'] == 210){
            $rtnCode = 0;  
            $haveTask = true;
            $cancelTask = true;                
            $logCode = '05100903';
            goto rtnFilnal;
        }
        else{
            $rtnCode = 0;
            goto rtnFilnal;
        }
        rtnFilnal:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertExportVDLog($logCode,$data['VDName'],$data['DomainName'],$data['DomainID'],$appendErr,$haveTask,$cancelTask,$data['ErrCode']);
            exit($rtnCode);   
    }

    function insertExportVDFailTaskShell($input)
    {
        $cmd = $this->cmdVDTaskErrTask;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= '"'.base64_encode('export$*'.$input['CephID'].'$*VDID$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName']).'" ';
        $cmd .= 'vdExport';
        exec($cmd,$output,$rtn);   
    }

    function exportVDShell($input)
    {
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDTaskExport;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);     
            $cmd .= '"'.base64_encode('export$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName']).'" ';            
            $cmd .= '"'.$input['VDID'].'" ';    
            $cmd .= '"'.$input['DomainID'].'" '; 
            // var_dump($cmd);        
            exec($cmd,$outputArr,$rtn);                                                    
        }     
        return $rtn;
    }

    function insertExportVDLog($code,$vdName,$domainName,$domainID,$appendErr,$haveTask=true,$cancelTask=false,$errCode=NULL)
    {
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $taskStr = "";
        if($haveTask)
            $taskStr = " task";
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);    
        if(is_null($appendErr)){
            if($cancelTask)
                $message = "Cancel Export VD$appendVDDomain$taskStr";
            else
                $message = "Export VD$appendVDDomain$taskStr Success";
        }
        else{
            $message = "Failed to export VD$appendVDDomain$taskStr$appendErr";
            if(isset($errCode)){
                $rtnMsg = $this->changeModifyReturnCode($errCode);
                $message .= "($rtnMsg)";
            }
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function importVD($input)
    {        
        $logCode = '';
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }  
        if(!$this->sqlAddVDSnapshotMaxCountField()){
            return CPAPIStatus::DBConnectFail;
        }        
        if(!$this->sqlGetUUID($outputUUID))
            return CPAPIStatus::DBConnectFail;
        if(!$this->sqlAddColumnField()){
            return CPAPIStatus::DBConnectFail;
        }        
        $this->newDomainClass();
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);
        $input['DomainName'] = $outputDomain['Name'];
        if($input['Username'] != ""){               
            $responsecode = $this->createUser($input,$outputUser);
            if($responsecode == CPAPIStatus::CreateUserSuccess || $responsecode == CPAPIStatus::Conflict){                
                $input['UserID'] = $outputUser['ID'];                
            }
            else{                
                $logCode = '05300C02';                
                goto fail;                
            }
        }
        else
        {            
            $input['UserID'] = null;    
        }                                     
        if($this->sqlListVDByNameidDomain($input['NewName'],$input['DomainID'],$output_vd)){
            $logCode = '05300C04';
            $rtn = CPAPIStatus::Conflict;           
            goto end;
        }             
        connectDB::$dbh->beginTransaction();          
        $input['UUID'] = $outputUUID['UUID'];
        $input['VDID'] = $outputUUID['UUID'];
        $input['State'] = 30;
        $input['VDName'] = $input['NewName'];        
        if(!$this->sqlInsertVDBase($input)){
            $logCode = '05300C08';
            goto fail_del_vd;         
        }
        $output_vd = null;
        $this->sqlListVDByID($input['UUID'], $output_vd);            
        if(is_null($output_vd)){
            $logCode = '05300C08';
            goto fail_del_vd;   
        }
        $input['VDNumber'] = $output_vd['VDNumber'];
        if(!$this->sqlInsertVDOrg($input)){
            $logCode = '05300C0A';
            goto fail_del_vd;                 
        }   
        if($input['Username'] != '' && !$this->sqlUpdateUserDefaultVD($input)){
            $logCode = '05300C0C';
            goto fail_del_vd;        
        }
        if(!$this->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$output_vd)){
            $logCode = '05300C0D';
            goto fail_del_vd;   
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
        $input['UEFI']=false;
        $input['QXL'] = true;
        $input['VGACount']=0;
        if(!$this->sqlInsertVDInfo($input)){
            $logCode = '05300C09';
            goto fail_del_vd;  
        }
        $this->sqlListVolumeByVolumeID($input['VolumeID'],$outputVolume);
        $input['VolumeName'] = $outputVolume['VolumeName'];
        $rtn = $this->importVDShell($input);         
        if($rtn != 0){
            $logCode = '05300C0E';
            goto fail_del_vd;
        }
        // $output_vd = null;
        // $this->listVDShell($input, $output_cluster, $output_vd);        
        // if(is_null($output_vd['cpuLimit'])){
        //     $log_code = '05300C0F';
        //     goto del_vd;        
        // }
        
        // $output = array('ID'=>$input['UUID'],'UserID'=>$input['UserID']
        //         ,'Name'=>$input['NewName'],'Online'=>false,'State'=>5
        //         ,'Desc'=>"",'Suspend'=>false);
        connectDB::$dbh->commit(); 
        $logCode = '05100B01';
        $rtn = CPAPIStatus::ImportVDSuccess;
        goto end;
        fail:
            $rtn = CPAPIStatus::ImportVDFail;
            $this->insertImportVDFailTaskShell($input);
            goto end;      
        fail_del_vd:                
            connectDB::$dbh->rollBack();           
            $this->insertImportVDFailTaskShell($input);
            $rtn = CPAPIStatus::ImportVDFail;
            goto end;
        end:            
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertImportVDLog($logCode,$input['NewName'],$outputDomain['Name'],$input['DomainID'],$appendErr);
            return $rtn;
    }

    function listImportVDSize($input,&$output)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }  
        $output['TotalSize'] = 0;
        $rtn = $this->listImportVDSizeShell($input,$outputInfo);
        if($rtn == 0){
            foreach ($outputInfo as $value) {
                if(sizeof($value['allocSize'])>0)
                    $output['TotalSize'] += $value['allocSize'];
            }
        }
    }

    function listImportVDSizeShell($input,&$output)
    {
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDImportListDisk;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);                
            $cmd .= '"'.$input['Name'].'" ';                       
            // var_dump($cmd);         
            exec($cmd,$outputArr,$rtn);    
            // var_dump($rtn);       
            if($rtn == 0){
                $output = json_decode($outputArr[0],true);
            }                
            // var_dump($rtn);   
        }              
        return $rtn;
    }

    function reportBkTaskVDRestoreSame($input)
    {
        if(!$this->connectDB()){
            exit(98);
        }               
        $this->processReportRestoreData($input,$data); 
        // var_dump($data);
        if(strlen($data['VolumeName']) > 0){
            $this->sqlListVolumeByVolumeName($data['VolumeName'],$outputVolume);
            $data['VolumeAlias'] = $outputVolume['VolumeAlias'];
        }         
        if($this->sqlListVDInfoByID($data['VDID'],$outputInfo)){
            // var_dump($outputInfo);
            $data['Desc'] = $outputInfo['Desc'];
            $data['SSLayerMax'] = $outputInfo['SSLayerMax'];
        }
        $this->reportVDTaskCommon($data,true,true,false);
    }

    function reportBkTaskVDRestoreNew($input)
    {
        if(!$this->connectDB()){
            exit(98);
        }    
        $this->processReportRestoreData($input,$data);           
        if(strlen($data['VolumeName']) > 0){
            $this->sqlListVolumeByVolumeName($data['VolumeName'],$outputVolume);
            $data['VolumeAlias'] = $outputVolume['VolumeAlias'];
        }              
        if(!isset($data['SSLayerMax']))
            $data['SSLayerMax']=$this->ssLayerDefault;        
        $this->reportVDTaskCommon($data);
    }

    function reportBkTaskSeedRestoreSame($input)
    {
        if(!$this->connectDB()){
            exit(98);
        }               
        $this->processReportRestoreData($input,$data);
        // var_dump($data);  
        if(strlen($data['VolumeName']) > 0){
            $this->sqlListVolumeByVolumeName($data['VolumeName'],$outputVolume);
            $data['VolumeAlias'] = $outputVolume['VolumeAlias'];
        }         
        if($this->sqlListVDInfoByID($data['VDID'],$outputInfo)){
            $data['Desc'] = $outputInfo['Desc'];
            $data['SSLayerMax'] = $outputInfo['SSLayerMax'];
        }
        var_dump('restoreSame');
        $this->reportVDTaskCommon($data,true,true,false);
    }

    function reportBkTaskSeedRestoreNew($input)
    {
        if(!$this->connectDB()){
            exit(98);
        }    
        $this->processReportRestoreData($input,$data);     
        if(strlen($data['VolumeName']) > 0){
            $this->sqlListVolumeByVolumeName($data['VolumeName'],$outputVolume);
            $data['VolumeAlias'] = $outputVolume['VolumeAlias'];
        }              
        if(!isset($data['SSLayerMax']))
            $data['SSLayerMax']=$this->ssLayerDefault;        
        $this->reportVDTaskCommon($data);
    }

    function reportVDTaskImport($input)
    {                           
        $this->processReportData($input,$data,true);   
        // var_dump($data);     
        if(!isset($data['SSLayerMax']))
            $data['SSLayerMax']=$this->ssLayerDefault;      
        $this->reportVDTaskCommon($data,false);
    }

    function reportVDTaskCommon($data,$isRestore=true,$cleanDiskSnapshot=false,$isUpdateViewerProtocol=true)
    {                  
        $haveTask = false;
        $cancelTask = false;   
        $logCode = $this->getVDTaskCommonErrorLogCode($data['Type']);
        $rtnCode = 99;        
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFilnal;
        }                       
        if($data['ErrCode'] == 0){ 
            $data['ConnectIP'] = $this->getConnectIP();            
            if($this->listVDWithSSShell($data,$output) != 0){
                $rtnCode = 1;
                goto rtnFilnal;
            }                    
            // var_dump($output);        
            $data['CPUType'] = $output['cpuType'] == 'fv' ? 0 : 1;
            $data['CPU']=$output['cpuLimit'];
            $data['CpuCt']=$output['cpuCt'];
            $data['RAM']=$output['maxMem']/1024;
            $data['NIC'] = json_encode($output['nic']);
            $suspend = ((int)$output['vdSuspend']) == 1 ? true : false;
            $data['Suspend']=$suspend;        
            $data['USBType']=$output['usbType'];
            $data['USBRedirCt']=$output['usbRedir'];
            $data['UEFI']=$output['bios'] == 'uefi' ? true : false;
            $data['VGACount']=(int)$output['vgaCt'];
            $data['QXL']=$output['qxlCt'] == '0' ? false : true;
            $cdrom = '';
            if(!is_null($output['cdrom']))
                $cdrom = $output['cdrom'];
            $data['CDRom']=$cdrom;
            $sound = $output['soundModel'] == 'ich6' ? 1 : 2;
            $data['Sound']=$sound;        
            if(!isset($data['Desc']))  
                $data['Desc'] = '';
            connectDB::$dbh->beginTransaction();
            if($cleanDiskSnapshot){
                if(!$this->sqlDeleteVDDisk($data['VDID'])){
                    $rtnCode = 11;
                    goto rtnRollback;            
                }
                if(!$this->sqlDeleteSnapshot($data['VDID'])){
                    $rtnCode = 12;
                    goto rtnRollback;            
                }
            }
            var_dump($isUpdateViewerProtocol);
            if($isUpdateViewerProtocol){
                if($data['QXL']){
                    $data['ViewerProtocol'] = $this->viewerProtocolEnum['Spice'];
                    $this->sqlUpdateVDViewerProtocol($data);
                }else{
                    $data['ViewerProtocol'] = $this->viewerProtocolEnum['RDP'];
                    $this->sqlUpdateVDViewerProtocol($data);
                }
            }
            if(!$this->sqlUpdateVDStatus($data)){
                $rtnCode = 2;
                goto rtnRollback;
            }
            if(!$this->sqlUpdateVD($data)){
                $rtnCode = 3;
                goto rtnRollback;
            }
            if($this->vdUpdateNIC($data)!=0){
                $rtnCode = 4;
                goto rtnRollback;
            }
            foreach ($output['disk'] as $value) {
                $data['DiskName'] = $value['diskName'];
                $data['Disk'] = $value['totalSize']/(1024*1024*1024);
                $data['DiskCache'] = $value['hdCache'] == 'writethrough' ? 0 : 1;
                switch ($value['hdBus']) {
                    case 'ide':
                        $data['DiskType'] = 0;
                        break;
                    case 'scsi':
                        $data['DiskType'] = 1;
                        break;
                    case 'virtio':
                        $data['DiskType'] = 2;
                        break;
                    default:
                        $data['DiskType'] = 0;
                        break;
                }
                $this->sqlListVolumeByVolumeName($value['diskVolumeId'],$outputVolume);
                // var_dump($outputVolume);
                $data['VolumeID'] = $outputVolume['VolumeID'];
                if(!$this->sqlInsertVDDisk($data)){
                    $rtnCode = 5;
                    goto rtnRollback;
                }
            }         
            $data['SS'] = $output['ss'];
            if($this->addSnapshot($data,$isRestore) != 0){
                $rtnCode = 5;
                goto rtnRollback;
            }                
            connectDB::$dbh->commit();
            $rtnCode = 0;
            $logCode = $this->getVDTaskCommonErrorLogCode($data['Type'],true);
            goto rtnFilnal;
        }  
        else{
            if($data['ErrCode'] == 210){
                $haveTask = true;
                $cancelTask = true;
            }
            switch ($data['Type']) {
                case $this->taskTypeEnum['import']:
                    if($data['ErrCode'] == 210)
                        $logCode = '05100B03';
                    if(!$this->deleteVD($data,false))
                        exit(6);                      
                    break;  
                case $this->backupActionEnum['restoreNew']:
                case $this->backupActionEnum['restoreNewSeed']:
                    // echo 'delete';
                    if($data['ErrCode'] == 210)
                        $logCode = '0C100103';
                    if(!$this->deleteVD($data,false))
                        exit(6);                      
                    break;  
                case $this->backupActionEnum['restoreSame']:
                case $this->backupActionEnum['restoreSameSeed']:
                    if($data['ErrCode'] == 210)
                        $logCode = '0C100103';
                    if(!$this->sqlUpdateVDtoRestoreStatus($data['VDID'],5))
                        exit(6);  
                    break;
            }
            $rtnCode = 0;
            goto rtnFilnal;
             
        }     
        rtnRollback:
            connectDB::$dbh->rollBack();
            goto rtnFilnal;
        rtnFilnal:
            // $appendErr = $this->logVDAppendErr($logCode);
            // $this->insertImportVDLog($logCode,$data['VDName'],$data['DomainName'],$data['DomainID'],$appendErr,false);
            $this->insertVDTaskCommonLog($logCode,$data,$haveTask,$cancelTask);
            exit($rtnCode);   
    }

    function reportSeedVDTaskCommon($data,$isRestore=true,$cleanDiskSnapshot=false)
    {                  
        $haveTask = false;
        $cancelTask = false;   
        $logCode = $this->getVDTaskCommonErrorLogCode($data['Type']);
        $rtnCode = 99;        
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFilnal;
        }                       
        if($data['ErrCode'] == 0){ 
            $data['ConnectIP'] = $this->getConnectIP();            
            if($this->listVDWithSSShell($data,$output) != 0){
                $rtnCode = 1;
                goto rtnFilnal;
            }                            
            $data['CPUType'] = $output['cpuType'] == 'fv' ? 0 : 1;
            $data['CPU']=$output['cpuLimit'];
            $data['CpuCt']=$output['cpuCt'];
            $data['RAM']=$output['maxMem']/1024;
            $data['NIC'] = json_encode($output['nic']);
            $suspend = ((int)$output['vdSuspend']) == 1 ? true : false;
            $data['Suspend']=$suspend;        
            $data['USBType']=$output['usbType'];
            $data['USBRedirCt']=$output['usbRedir'];
            $data['UEFI']=$output['bios'] == 'uefi' ? true : false;
            $data['QXL']=$output['qxlCt'] == '0' ? false : true;
            $data['VGACount']=(int)$output['vgaCt'];
            $cdrom = '';
            if(!is_null($output['cdrom']))
                $cdrom = $output['cdrom'];
            $data['CDRom']=$cdrom;
            $sound = $output['soundModel'] == 'ich6' ? 1 : 2;
            $data['Sound']=$sound;        
            if(!isset($data['Desc']))  
                $data['Desc'] = '';
            connectDB::$dbh->beginTransaction();
            if($cleanDiskSnapshot){
                if(!$this->sqlDeleteVDDisk($data['VDID'])){
                    $rtnCode = 11;
                    goto rtnRollback;            
                }               
            }            
            if(!$this->sqlUpdateVD($data)){
                $rtnCode = 3;
                goto rtnRollback;
            }
            if($this->vdUpdateNIC($data)!=0){
                $rtnCode = 4;
                goto rtnRollback;
            }
            foreach ($output['disk'] as $value) {
                $data['DiskName'] = $value['diskName'];
                $data['Disk'] = $value['totalSize']/(1024*1024*1024);
                $data['DiskCache'] = $value['hdCache'] == 'writethrough' ? 0 : 1;
                switch ($value['hdBus']) {
                    case 'ide':
                        $data['DiskType'] = 0;
                        break;
                    case 'scsi':
                        $data['DiskType'] = 1;
                        break;
                    case 'virtio':
                        $data['DiskType'] = 2;
                        break;
                    default:
                        $data['DiskType'] = 0;
                        break;
                }
                $this->sqlListVolumeByVolumeName($value['diskVolumeId'],$outputVolume);
                $data['VolumeID'] = $outputVolume['VolumeID'];
                if(!$this->sqlInsertVDDisk($data)){
                    $rtnCode = 5;
                    goto rtnRollback;
                }
            }         
            // $data['SS'] = $output['ss'];
            // if($this->addSnapshot($data,$isRestore) != 0){
            //     $rtnCode = 5;
            //     goto rtnRollback;
            // }                
            connectDB::$dbh->commit();
            $rtnCode = 0;
            $logCode = $this->getVDTaskCommonErrorLogCode($data['Type'],true);
            goto rtnFilnal;
        }  
        else{
            if($data['ErrCode'] == 210){
                $haveTask = true;
                $cancelTask = true;
            }
            switch ($data['Type']) {
                case $this->taskTypeEnum['import']:
                    if($data['ErrCode'] == 210)
                        $logCode = '05100B03';
                    if(!$this->deleteVD($data,false))
                        exit(6);                      
                    break;  
                case $this->backupActionEnum['restoreNew']:
                    // echo 'delete';
                    if($data['ErrCode'] == 210)
                        $logCode = '0C100103';
                    if(!$this->deleteVD($data,false))
                        exit(6);                      
                    break;  
                case $this->backupActionEnum['restoreSame']:
                    if($data['ErrCode'] == 210)
                        $logCode = '0C100103';
                    if(!$this->sqlUpdateVDtoRestoreStatus($data['VDID'],5))
                        exit(6);  
                    break;
            }
            $rtnCode = 0;
            goto rtnFilnal;
             
        }     
        rtnRollback:
            connectDB::$dbh->rollBack();
            goto rtnFilnal;
        rtnFilnal:
            // $appendErr = $this->logVDAppendErr($logCode);
            // $this->insertImportVDLog($logCode,$data['VDName'],$data['DomainName'],$data['DomainID'],$appendErr,false);
            $this->insertVDTaskCommonLog($logCode,$data,$haveTask,$cancelTask);
            exit($rtnCode);   
    }

     function reportBkTaskUPRestoreSame($input)
    {
        if(!$this->connectDB()){
            exit(98);
        }       
        $this->processReportRestoreUPData($input,$data);           
        $this->reportUPTaskCommon($data,true,true);
    }

    function reportBkTaskUPRestoreNew($input)
    {
        if(!$this->connectDB()){
            exit(98);
        }
        $this->processReportRestoreUPData($input,$data);           
        $this->reportUPTaskCommon($data,true,true);
    }

    function reportUPTaskCommon($data,$isRestore=true,$cleanDiskSnapshot=false)
    {                     
        $haveTask = false;
        $cancelTask = false;   
        $logCode = $this->getVDTaskCommonErrorLogCode($data['Type']);
        $rtnCode = 99;                
        if($data['ErrCode'] == 0){ 
            $data['ConnectIP'] = $this->getConnectIP();                
            if($this->listUserDiskWithSSShell($data,$output) != 0){
                $rtnCode = 1;
                goto rtnFilnal;
            }                            
            // var_dump($output);
            if(isset($output['totalSize']))
                $data['DiskSize'] = $output['totalSize']/1073741824;
            else
                $data['DiskSize'] = 0;
            if(isset($output['reportMeta'])){
                if($this->isBase64($output['reportMeta'])){
                    $reportMeta = base64_decode($output['reportMeta']);
                    $metadataArr = explode('$*', $reportMeta);
                    if(isset($metadataArr[9]))
                        $data['DiskType'] = (int)$metadataArr[9];
                    if(isset($metadataArr[10]))
                        $data['DiskCache'] = (int)$metadataArr[10];
                }
            }
            else{
                $data['DiskType'] = 2;
                $data['DiskCache'] = 0;
            }
            // var_dump($data);
            connectDB::$dbh->beginTransaction();
            if($cleanDiskSnapshot){                
                if(!$this->sqlDeleteSnapshot($data['DiskID'],true)){
                    $rtnCode = 12;
                    goto rtnRollback;            
                }
            }
            if(!$this->sqlUpdateUserDisktoRestoreStatus($data['DiskID'],$data['State'])){
                $rtnCode = 2;
                goto rtnRollback;
            }    
            if(!$this->sqlUpdateUserDiskSize($data,$data['DiskID'],$data['DiskSize'],$data['State'])){
                $rtnCode = 13;
                goto rtnRollback;
            }   
            // var_dump($data);
            if($data['Type'] == $this->backupActionEnum['restoreNewUP']){
                if(isset($data['SSLayerMax'])){                    
                    $this->sqlUpdateUserDiskLimitSSLayer($data);
                }
            }
            $data['SS'] = $output['ss'];
            // var_dump($data);
            if($this->addSnapshot($data,$isRestore,true) != 0){
                $rtnCode = 5;
                goto rtnRollback;
            }                
            connectDB::$dbh->commit();
            $rtnCode = 0;            
            $logCode = $this->getVDTaskCommonErrorLogCode($data['Type'],true);
            goto rtnFilnal;
        }  
        else{
            if($data['ErrCode'] == 210){
                $haveTask = true;
                $cancelTask = true;
            }
            // var_dump($data);
            switch ($data['Type']) {                                
                case $this->backupActionEnum['restoreNewUP']:
                    // echo 'delete';
                    if($data['ErrCode'] == 210)
                        $logCode = '0C100103';
                    $userC = new UserAction();
                    if(!$userC->sql_delete_user_profile_disk($data['DiskID']))
                        exit(6);                      
                    break;                  
                case $this->backupActionEnum['restoreSameUP']:
                    if($data['ErrCode'] == 210)
                        $logCode = '0C100103';
                    if(!$this->sqlUpdateUserDisktoRestoreStatus($data['DiskID'],0))
                        exit(6);  
                    break;
            }
            $rtnCode = 0;
            goto rtnFilnal;
             
        }     
        rtnRollback:
            connectDB::$dbh->rollBack();
            goto rtnFilnal;
        rtnFilnal:
            // $appendErr = $this->logVDAppendErr($logCode);
            // $this->insertImportVDLog($logCode,$data['VDName'],$data['DomainName'],$data['DomainID'],$appendErr,false);
            // var_dump($rtnCode);
            // var_dump($data);
            $this->insertVDTaskCommonLog($logCode,$data,$haveTask,$cancelTask);
            exit($rtnCode);   
    }

    function recreateVDSnapshot($idVD)
    {
        // $vdC = new VDAction();
        $this->sqlDeleteSnapshot($idVD);
        $data = array('ConnectIP'=>'127.0.0.1','VDID'=>$idVD);
        if($this->listVDWithSSShell($data,$output) != 0){
            $rtnCode = 1;            
            goto rtnFinal;
        }   
        $data['SS'] = $output['ss'];
        if($this->addSnapshot($data,false) != 0){
            $rtnCode = 2;
        }            
        $rtnCode = 0;
        rtnFinal:
            return $rtnCode; 
    }

    function recreateVDiskSnapshot($input)
    {       
        $this->sqlDeleteSnapshot($input['DiskID'],true);        
        $data = array('ConnectIP'=>'127.0.0.1','DiskID'=>$input['DiskID'],'DiskName'=>$input['DiskName']);
        if($this->listUserDiskWithSSShell($data,$output) != 0){
            $rtnCode = 1;            
            goto rtnFinal;
        }   
        $data['SS'] = $output['ss'];
        // var_dump($data);
        if($this->addSnapshot($data,false,true) != 0){
            $rtnCode = 2;
        }            
        $rtnCode = 0;
        rtnFinal:
            return $rtnCode; 
    }

    function listUserDiskWithSSShell($input,&$output)
    {
        // var_dump($input);
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdVDUserprofileListWithSS;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.$input['DiskName'].'" '; 
            // var_dump($cmd);
            exec($cmd,$output,$rtn);    
            // var_dump($rtn);
            if($rtn == 0){                     
                // var_dump($output);
                $output = json_decode($output[0],true);
                // var_dump($output);                
                return;
            }            
        }           
    }
    
    function getVDTaskCommonErrorLogCode($type,$success = false)
    {
        $rtnCode = '';
        switch ($type) {
            case $this->taskTypeEnum['import']:
                if(!$success)
                    $rtnCode = '05300C10';
                else
                    $rtnCode = '05100B02';
                break; 
            case $this->backupActionEnum['restoreNew']:
            case $this->backupActionEnum['restoreSame']:
            case $this->backupActionEnum['restoreNewSeed']:
            case $this->backupActionEnum['restoreSameSeed']:
            case $this->backupActionEnum['restoreNewUP']:
            case $this->backupActionEnum['restoreSameUP']:
                if(!$success)
                    $rtnCode = '0C300209';
                else
                    $rtnCode = '0C100102';
                break;               
        }
        return $rtnCode;
    }

    function insertVDTaskCommonLog($logCode,$data,$haveTask,$cancelTask)
    {
        $isSeed = false;
        if($data['Type'] == $this->backupActionEnum['restoreNewSeed'] || $data['Type'] == $this->backupActionEnum['restoreSameSeed'])
            $isSeed = true;
        switch ($data['Type']) {
            case $this->taskTypeEnum['import']:
                $appendErr = $this->logVDAppendErr($logCode);
                 $this->insertImportVDLog($logCode,$data['VDName'],$data['DomainName'],$data['DomainID'],$appendErr,$haveTask,$cancelTask,$data['ErrCode']);
                break;     
            case $this->backupActionEnum['restoreNew']:
            case $this->backupActionEnum['restoreNewSeed']:
                $appendErr = $this->logVDAppendErr($logCode);
                 $this->insertRestoreVDLog($logCode,$data['VDName'],$data['DomainName'],$data['DomainID'],$appendErr,$data['CreateTime'],$haveTask,$cancelTask,$data['ErrCode'],$data['VolumeAlias'],$isSeed);
                break; 
            case $this->backupActionEnum['restoreSame']:
            case $this->backupActionEnum['restoreSameSeed']:
                $appendErr = $this->logVDAppendErr($logCode);
                 $this->insertRestoreVDLog($logCode,$data['VDName'],$data['DomainName'],$data['DomainID'],$appendErr,$data['CreateTime'],$haveTask,$cancelTask,$data['ErrCode'],$data['VolumeAlias'],$isSeed);
                break;        
            case $this->backupActionEnum['restoreSameUP']:
            case $this->backupActionEnum['restoreNewUP']:
                $appendErr = $this->logVDAppendErr($logCode);
                $this->insertRestoreUPLog($logCode,$data['Username'],$data['DomainName'],$data['DomainID'],$appendErr,$data['CreateTime'],$haveTask,$cancelTask,$data['ErrCode']);
                break;          
        }
    }

    function insertImportVDLog($code,$vdName,$domainName,$domainID,$appendErr,$haveTask=true,$cancelTask=false,$errCode=NULL)
    {
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $taskStr = "";
        if($haveTask)
            $taskStr = " task";
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);    
        if(is_null($appendErr)){
            if($cancelTask)
                $message = "Cancel Import VD$appendVDDomain$taskStr";
            else
                $message = "Import VD$appendVDDomain$taskStr Success";
        }
        else{
            $message = "Failed to import VD$appendVDDomain$taskStr$appendErr";
            if(isset($errCode)){
                $rtnMsg = $this->changeModifyReturnCode($errCode);
                $message .= "($rtnMsg)";
            }
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function insertRestoreVDLog($code,$vdName,$domainName,$domainID,$appendErr,$logTime,$haveTask=true,$cancelTask=false,$errCode=NULL,$volumeAlias=NULL,$isSeed=false)
    {
        $logType = 12;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $taskStr = "";
        if($haveTask)
            $taskStr = " task";
        $vdStr = "VD";
        if($isSeed)
            $vdStr = "Seed";
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);    
        if(is_null($appendErr)){
            if($cancelTask)
                $message = "Cancel Restore $vdStr$appendVDDomain$taskStr";
            else
                $message = "Restore $vdStr$appendVDDomain$taskStr Success";
        }
        else{
            $message = "Failed to restore $vdStr$appendVDDomain$taskStr$appendErr";
            if(isset($errCode)){
                if($errCode == 82)
                    $message .= "($volumeAlias's space is not enough to restore)";                
                else{
                    $rtnMsg = $this->changeModifyReturnCode($errCode);
                    $message .= "($rtnMsg)";
                }
            }
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message,'Time'=>$logTime),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function insertRestoreUPLog($code,$username,$domainName,$domainID,$appendErr,$logTime,$haveTask=true,$cancelTask,$errCode=NULL)
    {
        $logType = 12;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $taskStr = "";
        if($haveTask)
            $taskStr = " task";       
        $appendVDDomain = $this->logappendVDDomain($username,$domainName);    
        if(is_null($appendErr)){
            if($cancelTask)
                $message = "Cancel Restore User Disk$appendVDDomain$taskStr";
            else
                $message = "Restore User Disk$appendVDDomain$taskStr Success";
        }
        else{
            $rtnCode = '';
            if(isset($errCode))
                $rtnCode=$errCode;
            $message = "Failed to restore User Disk$appendVDDomain$taskStr$appendErr($rtnCode)";
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message,'Time'=>$logTime),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function listImportVDDiskShell($input,$input_cluster,&$output)
    {
        $rtn = -2;
        if(!is_null($input_cluster['ConnectIP'])){
            $cmd = $this->cmd_vd_import_disk_list;
            $cmd .= $input_cluster['ConnectIP'].' ';
            $cmd .= '"'.$input['DomainName'].'" ';
            $cmd .= '"'.$input['OrgName'].'" ';            
            $output = shell_exec($cmd);      
            $output = json_decode($output,true);
            if(is_array($output)){  
                $rtn = 0;            
                return $rtn;
            }
        }                  
        return $rtn;
    }

    function insertImportVDFailTaskShell($input)
    {
        $cmd = $this->cmdVDTaskErrTask;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= '"'.base64_encode('import$*'.$input['CephID'].'$*VDID$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['VDNumber']).'" ';
        $cmd .= 'vdImport';
        exec($cmd,$output,$rtn);       
    }
        
    function importVDShell($input)
    {
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDTaskImport;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);     
            $cmd .= '"'.base64_encode('import$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['VDNumber']).'" ';
            $cmd .= '"'.$input['OrgName'].'" ';           
            $cmd .= '"'.$input['VDID'].'" ';
            $cmd .= '"'.$input['VDName'].'" ';            
            $cmd .= '"'.$input['UUID'].'" ';  
            $cmd .= '"'.$input['VolumeName'].'" ';
            $cmd .= '"'.$input['VolumeName'].'" ';
            $cmd .= '"'.$input['VolumeName'].'" ';
            $cmd .= '"'.$input['VolumeName'].'" ';
            $cmd .= '"'.$input['VolumeName'].'" ';
            $cmd .= '"'.$input['VolumeName'].'" ';
            $cmd .= '"'.$input['VolumeName'].'" ';
            $cmd .= '"'.$input['DomainID'].'"';  
            // var_dump($cmd);          
            exec($cmd,$outputArr,$rtn);                 
            // var_dump($rtn);   
        }              
        return $rtn;
    }    

    function listImportVD($input,&$output)
    {        
        $this->connectDB();        
        if(connectDB::$dbh == null){
            return CPAPIStatus::DBConnectFail;
        }                    
        if($this->listImportVDShell($input,$output) != 0){
            return CPAPIStatus::ListImportFail;
        }
        return CPAPIStatus::ListImportSuccess;
    }
    
    function listImportVDShell($input,&$output)
    {        
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDImportList;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);                             
            $output = array();            
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);   
            if($rtn == 0) {                
                $output = json_decode($outputArr[0],true);
            }
        }     
        $rtn = 0;
        return $rtn;
    }        
    
    function listAutosuspend($input,&$output)
    {
        $this->listAutosuspendShell($input,$suspendTime);
        $output = array('Suspend'=>(int)$suspendTime);
    }

    function listAutosuspendShell($input,&$output)
    {        
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdAutosuspendList;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);                 
            $output = array();            
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);   
            if($rtn == 0) {                
                $output = $outputArr[0];
            }
            else{
                $output = 60;
            }
        }                   
    }        

    function listClusterAutosuspend($input,&$output)
    {
        $this->listAutosuspendShell($input,$suspendTime);
        $output = array('Suspend'=>(int)$suspendTime);
    }

    function listClusterAutosuspendShell($input,&$output)
    {        
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdClusterAutosuspendList;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);                 
            $output = array();            
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);   
            if($rtn == 0) {                
                $output = $outputArr[0];
            }
            else{
                $output = 60;
            }
        }                   
    }        

    function setClusterAutosuspend($input)
    {
        $this->setClusterAutosuspendShell($input);
    }

    function setClusterAutosuspendShell($input)
    {        
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdClusterAutosuspendSet;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);                 
            $cmd .= '"'.$input['Suspend'].'" ';                                              
            exec($cmd,$outputArr,$rtn);   
        }   
        return $rtn;                
    }

    function setAutosuspend($input,&$output)
    {
        $this->setAutosuspendShell($input);       
    }

    function setAutosuspendShell($input)
    {        
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdAutosuspednSet;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);                 
            $cmd .= '"'.$input['Suspend'].'" ';                                              
            exec($cmd,$outputArr,$rtn);   
        }   
        return $rtn;                
    }

    function setVDAutosuspend($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }        
        $this->newDomainClass();      
        $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);     
        foreach ($input as $vd){         
            $output_vd = null;
            $this->sqlListVDByID($vd['VDID'],$output_vd);
            if(is_null($output_vd)){
               continue;
            }                       
            $input['VDID'] = $vd['VDID'];
            $input['DomainID']=$output_vd['DomainID'];
            $input['VDName']=$output_vd['Name'];         
            $input['VDNumber']=$output_vd['VDNumber'];           
            $input['Suspend']=$vd['Suspend'];
            $rtn = $this->setVDAutosuspendShell($input);    
            if($rtn == 0){
                $this->sqlUpdateVDAutosuspend($input);
            }                   
        }         
    }

    function setVDAutosuspendShell($input)
    {        
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDSuspendSet;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);                 
            $cmd .= '"'.$input['VDID'].'" ';
            $cmd .= $input['Suspend'] == true ? 1 : 0;    
            // var_dump($cmd);                                   
            exec($cmd,$outputArr,$rtn);   
        }   
        return $rtn;                
    }

    function migrateVD($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }        
        $nodeC = new NodeAction();
        if(!$nodeC->sqlListServerByidVDServer($input['NodeID'],$outputServer)){
            return CPAPIStatus::MigrateVDFail;            
        }
        $input['CommunicateIP'] = $outputServer['CommunicateIP'];
        if($this->migrateVDShell($input) != 0)
            return CPAPIStatus::MigrateVDFail;
        return CPAPIStatus::MigrateVDSuccess;
    }

    function migrateVDShell($input)
    {
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDLiveMigrate;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);                 
            $cmd .= '"'.$input['VDID'].'" ';
            $cmd .= '"'.$input['CommunicateIP'].'" >/dev/null 2>&1 &';            
            // var_dump($cmd);
            $pipe = popen($cmd,"r");    
            pclose($pipe); 
            $rtn = 0;
        }   
        return $rtn;         
    }

    function sql_get_idDomain_by_idUser($input_id,&$output)
    {        
        $SQLGetID = <<<SQL
                SELECT t1.idDomain FROM tbUserBaseSet t1 WHERE idUser=:idUser
SQL;
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLGetID);
            $sth->bindValue(':idUser', $input_id, PDO::PARAM_INT); 
            if($sth->execute())
            {                
                while( $row = $sth->fetch() ) 
                {
                    $output = array('DomainID'=>(int)$row['idDomain']);
                }
            }               
        }
        catch (Exception $e){                
        }         
    }       
    
    function sqlListVDByNameidDomain($name,$id,&$output)
    {
        $output = null;
        $SQLSelect = <<<SQL
                SELECT * FROM tbVDImageBaseSet                    
                    WHERE nameVD=:nameVD AND idDomain=:idDomain LIMIT 1
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($SQLSelect);        
            $sth->bindValue(':nameVD', $name, PDO::PARAM_STR);    
            $sth->bindValue(':idDomain', $id, PDO::PARAM_INT);           
            if($sth->execute())
            {                
                while( $row = $sth->fetch() ) 
                {
                    $output = array('VDID'=>$row['idVD'],'VDNumber'=>$row['idVdNumber']);
                }
            }                           
        }
        catch (Exception $e){                             
        }              
        if(is_null($output))
            return false;
        return true;                   
    }
    
    function sqlInsertVDBase($input)
    {        
        $result = false;
        $SQLInsert = <<<SQL
               INSERT tbVDImageBaseSet (idVD,idCephPool,idDomain,nameVD,stateVD,idVDServer) Values (:idVD,:idCephPool,:idDomain,:nameVD,:stateVD,:idVDServer)
SQL;
        try
        {                         
            // var_dump($input);       
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':idVD', $input['UUID'], PDO::PARAM_STR); 
            $sth->bindValue(':idCephPool', $input['CephID'], PDO::PARAM_INT); 
            $sth->bindValue(':idDomain', $input['DomainID'], PDO::PARAM_INT); 
            $sth->bindValue(':nameVD', $input['VDName'], PDO::PARAM_STR); 
            $sth->bindValue(':stateVD', $input['State'], PDO::PARAM_INT);
            $sth->bindValue(':idVDServer', $input['NodeID'], PDO::PARAM_INT);
            if($sth->execute())
            {               
                if($sth->rowCount() == 1)
                    $result = true;
            }               
        }
        catch (Exception $e){                
        }         
        return $result;
    }
    
    function sqlInsertVDInfo($input)
    {                
        // var_dump($input);
        $result = false;
        $SQLInsert = <<<SQL
            INSERT tbVDImageBaseInfoSet (idVD,ctCpuCore,rateCpuClock,sizeRam,CdRom,ctUsb,typeSound,typeNic,isAutoSuspend,Description,configCpu,limitSSLyer,typeUsb,ctGpu,isUefiEnable,isQXLEnable,viewerProtocol) 
                Values (:idVD,:ctCpuCore,:rateCpuClock,:sizeRam,:CdRom,:ctUsb,:typeSound,:typeNic,:isAutoSuspend,:Description,:configCpu,:limitSSLyer,:typeUsb,:ctGpu,:isUefiEnable,:isQXLEnable,:viewerProtocol)
SQL;
        try
        {                                       
            $sth = connectDB::$dbh->prepare($SQLInsert);
            $sth->bindValue(':idVD', $input['UUID'], PDO::PARAM_STR); 
            $sth->bindValue(':ctCpuCore', $input['CpuCt'], PDO::PARAM_INT); 
            $sth->bindValue(':rateCpuClock', $input['CPU'], PDO::PARAM_INT); 
            $sth->bindValue(':sizeRam', $input['RAM'], PDO::PARAM_INT); 
            $sth->bindValue(':CdRom', $input['CDRom'], PDO::PARAM_STR);
            $sth->bindValue(':typeUsb', $input['USBType'], PDO::PARAM_INT); 
            $sth->bindValue(':ctUsb', $input['USBRedirCt'], PDO::PARAM_INT); 
            $sth->bindValue(':typeSound', $input['Sound'], PDO::PARAM_INT);             
            $sth->bindValue(':typeNic', json_encode($input['NIC_Info']), PDO::PARAM_STR);
            $sth->bindValue(':isAutoSuspend', $input['Suspend'], PDO::PARAM_BOOL);
            $sth->bindValue(':Description', $input['Desc'], PDO::PARAM_STR);
            $cpuConfig = json_encode(array('CPUType'=>$input['CPUType']));            
            $sth->bindValue(':configCpu', $cpuConfig, PDO::PARAM_STR);
            $sth->bindValue(':ctGpu', $input['VGACount'], PDO::PARAM_INT);
            $sth->bindValue(':isUefiEnable', $input['UEFI'], PDO::PARAM_BOOL);
            $sth->bindValue(':isQXLEnable', $input['QXL'], PDO::PARAM_BOOL);
            $ssLayerMax = $this->ssLayerDefault;
            if(isset($input['SSLayerMax']))
                $ssLayerMax = $input['SSLayerMax'];
            $sth->bindValue(':limitSSLyer', $ssLayerMax, PDO::PARAM_INT);
            $viewerProtocol = $this->viewerProtocolEnum['Spice'];
            if(isset($input['ViewerProtocol']))
                $viewerProtocol = $input['ViewerProtocol'];
            $sth->bindValue(':viewerProtocol', $viewerProtocol, PDO::PARAM_INT);
            if($sth->execute())
            {                               
                if($sth->rowCount() == 1)
                    $result = true;
            }                          
        }
        catch (Exception $e){                
        }         
        return $result;
    }
    
    function sqlInsertVDDisk($input,$stateVDisk = 0)
    {                
        // var_dump($input);
        $result = false;
        $SQLInsert = <<<SQL
               INSERT tbVDiskSet (idVD,idCephPool,idDomain,nameVDisk,sizeVDisk,stateVDisk,configVDisk,idVolume) Values (:idVD,:idCephPool,:idDomain,:nameVDisk,:sizeVDisk,:stateVDisk,:configVDisk,:idVolume)
SQL;
        try
        {  
            $nameVDisk = '';
            if(isset($input['DiskName'])){
                $nameVDisk = $input['DiskName'];
            }                            
            $sth = connectDB::$dbh->prepare($SQLInsert);
            $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_STR); 
            $sth->bindValue(':idCephPool', $input['CephID'], PDO::PARAM_INT); 
            $sth->bindValue(':idDomain', $input['DomainID'], PDO::PARAM_INT); 
            $sth->bindValue(':nameVDisk', $nameVDisk, PDO::PARAM_STR); 
            $sth->bindValue(':sizeVDisk', $input['Disk'], PDO::PARAM_INT);
            $sth->bindValue(':stateVDisk', $stateVDisk, PDO::PARAM_INT);
            $sth->bindValue(':idVolume', $input['VolumeID'], PDO::PARAM_STR);
            $configVDisk = '';
            if(isset($input['DiskType']))
                $configVDisk = json_encode(array('DiskType'=>$input['DiskType'],'DiskCache'=>$input['DiskCache']));
            $sth->bindValue(':configVDisk', $configVDisk, PDO::PARAM_STR);
            if($sth->execute())
            {               
                if($sth->rowCount() == 1)
                    $result = true;
            }               
        }
        catch (Exception $e){                
        }         
        return $result;
    }
    
    function sqlUpdateVDDiskState($idVDisk,$stateVDisk = 0)
    {
        $result = false;
        $SQLUpdate = <<<SQL
            Update tbVDiskSet SET stateVDisk=:stateVDisk WHERE idVDisk = :idVDisk
SQL;
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLUpdate);
            $sth->bindValue(':idVDisk', $idVDisk, PDO::PARAM_INT); 
            $sth->bindValue(':stateVDisk', $stateVDisk, PDO::PARAM_INT); 
            if($sth->execute())
            {               
                if($sth->rowCount() == 1)                       
                    $result = true;                
            }               
        }
        catch (Exception $e){                
        }    
        return $result;
    }

    function sqlUpdateVDDiskStateAndVolumeID($input)
    {
        $result = false;
        $SQLUpdate = <<<SQL
            Update tbVDiskSet SET stateVDisk=:stateVDisk,idVolume=:idVolume WHERE idVDisk = :idVDisk
SQL;
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLUpdate);
            $sth->bindValue(':idVDisk', $input['DiskID'], PDO::PARAM_INT); 
            $sth->bindValue(':idVolume', $input['VolumeID'], PDO::PARAM_STR); 
            $sth->bindValue(':stateVDisk', $input['State'], PDO::PARAM_INT); 
            if($sth->execute())
            {               
                if($sth->rowCount() == 1)                       
                    $result = true;                
            }               
        }
        catch (Exception $e){                
        }    
        return $result;
    }

    function sqlInsertVDOrg($input)
    {
        $result = false;
        $SQLInsert = <<<SQL
               INSERT tbvdimageorgset (idVD,idUser,isOnline) Values (:idVD,:idUser,false)
SQL;
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLInsert);
            $sth->bindValue(':idVD', $input['UUID'], PDO::PARAM_STR); 
            $sth->bindValue(':idUser', $input['UserID'], PDO::PARAM_INT);            
            if($sth->execute())
            {                
                if($sth->rowCount() == 1)
                    $result = true;
            }               
        }
        catch (Exception $e){                
        }         
        return $result;
    }

    function sqlInsertServiceVD($input)
    {
        $result = false;
        $SQLInsert = <<<SQL
               INSERT tbvdimageserviceset (idVD,idUser,typeService,jsonConfigService) Values (:idVD,:idUser,:typeService,:jsonConfigService)
SQL;
        try
        {                                       
            $sth = connectDB::$dbh->prepare($SQLInsert);
            $sth->bindValue(':idVD', $input['UUID'], PDO::PARAM_STR); 
            $sth->bindValue(':idUser', $input['UserID'], PDO::PARAM_INT);
            $sth->bindValue(':typeService', $input['Type'], PDO::PARAM_INT);
            $sth->bindValue(':jsonConfigService', $input['JsonConfig'], PDO::PARAM_STR);
            if($sth->execute())
            {                
                if($sth->rowCount() == 1)
                    $result = true;
            }               
        }
        catch (Exception $e){                
        }         
        return $result;
    }
    
    function sqlInsertVDUser($input)
    {
        $result = false;
        $SQLInsert = <<<SQL
            INSERT tbvdimageset (idSeed,idVD,idUser,isOnline) Values (:idSeed,:idVD,:idUser,false)
SQL;
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLInsert);
            $sth->bindValue(':idSeed', $input['VDID'], PDO::PARAM_STR); 
            $sth->bindValue(':idVD', $input['UUID'], PDO::PARAM_STR); 
            $sth->bindValue(':idUser', $input['UserID'], PDO::PARAM_INT);            
            if($sth->execute())
            {                
                if($sth->rowCount() == 1)
                    $result = true;
            }               
        }
        catch (Exception $e){                
        }         
        return $result;
    }
    
    function sqlInsertVDSeed($input)
    {
        $result = false;
        $SQLInsert = <<<SQL
               INSERT tbvdseedset (idVD) Values (:idVD)
SQL;
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLInsert);
            $sth->bindValue(':idVD', $input['UUID'], PDO::PARAM_STR);                      
            if($sth->execute())
            {                
                if($sth->rowCount() == 1)
                    $result = true;
            }               
        }
        catch (Exception $e){                
        }         
        return $result;
    }
        
    function sql_update_vd_uuid($org_uuid,$new_uuid)
    {
        $result = false;
        $SQLUpdate = <<<SQL
                Update tbVDImageBaseSet SET idVD=:NewidVD WHERE idVD = :OrgidVD
SQL;
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLUpdate);
            $sth->bindValue(':NewidVD', $new_uuid, PDO::PARAM_STR); 
            $sth->bindValue(':OrgidVD', $org_uuid, PDO::PARAM_STR); 
            if($sth->execute())
            {                                      
                $result = true;                
            }               
        }
        catch (Exception $e){                
        }    
        return $result;
    }
   
    function sqlUpdateUserDefaultVDForViewer($idUser,$idVD)
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
    

    function sqlUpdateUserDefaultVD($input)
    { 
        $result = false;
        $SQLGetDefaultVDID = <<<SQL
                SELECT idDefaultVD FROM tbuserset WHERE idUser=:idUser
SQL;
        $SQLUpdateDefaultVD = <<<SQL
                Update tbuserset SET idDefaultVD=:idDefaultVD WHERE idUser = :idUser
SQL;
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLGetDefaultVDID);
            $sth->bindValue(':idUser', $input['UserID'], PDO::PARAM_STR); 
            if($sth->execute())
            {                       
                while( $row = $sth->fetch() ) 
                {
                    $idDefault=$row['idDefaultVD'];
                }                
                if(is_null($idDefault)){
                    $sth = connectDB::$dbh->prepare($SQLUpdateDefaultVD);
                    $sth->bindValue(':idDefaultVD', $input['UUID'], PDO::PARAM_STR); 
                    $sth->bindValue(':idUser', $input['UserID'], PDO::PARAM_STR); 
                    $sth->execute();
                    if($sth->rowCount() == 1)
                        $result = true;
                }                
                else{                    
                    $result = true;
                }
            }               
        }
        catch (Exception $e){                
        }    
        return $result;
    }
    
    function sqlListVDByID($id,&$output)
    {
        $output = null;
        $SQLGetVD = <<<SQL
            SELECT * FROM tbVDImageBaseSet WHERE idVD =:idVD
SQL;
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLGetVD);
            $sth->bindValue(':idVD', $id, PDO::PARAM_STR); 
            if($sth->execute())
            {                
                while( $row = $sth->fetch() ) 
                {
                    $output = array('VDNumber'=>$row['idVdNumber'],'DomainID'=>(int)$row['idDomain']
                            ,'Name'=>$row['nameVD'],'CephID'=>$row['idCephPool']);
                }                
            }                            
        }
        catch (Exception $e){                
        }         
        if(is_null($output))
            return false;
        return true;
    }
    
    function sqlListVDByIDPoweron($id,&$output)
    {
        $SQLGetVD = <<<SQL
            SELECT t1.*,t2.sizeRam FROM tbVDImageBaseSet as t1
                INNER JOIN tbvdimagebaseinfoset as t2
                ON t1.idVD = t2.idVD
            WHERE t1.idVD =:idVD
SQL;
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLGetVD);
            $sth->bindValue(':idVD', $id, PDO::PARAM_STR); 
            if($sth->execute())
            {                
                while( $row = $sth->fetch() ) 
                {
                    $output = array('VDNumber'=>$row['idVdNumber'],'DomainID'=>(int)$row['idDomain']
                            ,'Name'=>$row['nameVD'],'RAM'=>(int)$row['sizeRam']);
                }                
            }                            
        }
        catch (Exception $e){                
        }         
    }
    
    function sqlListVDUserIDByVDID($id,&$output)
    {
        $output = null;
        $SQLGetVD = <<<SQL
            SELECT t1.*,t2.sizeRam,t2.ctGpu FROM tbVDImageBaseSet as t1
                INNER JOIN tbvdimagebaseinfoset as t2
                ON t1.idVD = t2.idVD
                WHERE t1.idVD =:idVD
SQL;
        $sqlGetOrgVDUserID = "SELECT idUser as OrgidUser FROM tbvdimageorgset WHERE idVD =:idVD";
        $sqlGetServiceVDUserID = "SELECT idUser as OrgidUser FROM tbvdimageserviceset WHERE idVD =:idVD";
        $sqlGetUserVDUserID = "SELECT idUser as UseridUser FROM tbvdimageset WHERE idVD =:idVD";
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLGetVD);
            $sth->bindValue(':idVD', $id, PDO::PARAM_STR); 
            if($sth->execute())
            {                
                while( $row = $sth->fetch() ) 
                {
                    $output = array('VDNumber'=>$row['idVdNumber'],'DomainID'=>(int)$row['idDomain']
                            ,'Name'=>$row['nameVD'],'RAM'=>$row['sizeRam'],'VGACount'=>$row['ctGpu']);
                }                
            }                
            if(is_null($output))
                return false;
            $sth = connectDB::$dbh->prepare($sqlGetOrgVDUserID);
            $sth->bindValue(':idVD', $id, PDO::PARAM_STR);
            if($sth->execute())
            {                               
                while( $row = $sth->fetch() ) 
                {                    
                    $output['OrgUserID'] = $row['OrgidUser'];
                }                
                if($sth->rowCount() == 0)
                    $output['OrgUserID'] = null;
            }        
            else
                $output['OrgUserID'] = null;
            if(is_null($output['OrgUserID'])){
                $sth = connectDB::$dbh->prepare($sqlGetServiceVDUserID);
                $sth->bindValue(':idVD', $id, PDO::PARAM_STR);
                if($sth->execute())
                {                               
                    while( $row = $sth->fetch() ) 
                    {                    
                        $output['OrgUserID'] = $row['OrgidUser'];
                    }                
                    if($sth->rowCount() == 0)
                        $output['OrgUserID'] = null;
                }        
                else
                    $output['OrgUserID'] = null;
            }
            // var_dump($output);
            if(is_null($output['OrgUserID'])){
                $sth = connectDB::$dbh->prepare($sqlGetUserVDUserID);
                $sth->bindValue(':idVD', $id, PDO::PARAM_STR); 
                if($sth->execute())
                {                               
                    while( $row = $sth->fetch() ) 
                    {
                        // var_dump($row);
                        $output['UserUserID'] = $row['UseridUser'];
                    }                
                    if($sth->rowCount() == 0)
                        $output = null;
                }        
                else
                    $output = null;
            }
            // var_dump($output);
        }
        catch (Exception $e){                
        }         
        if(is_null($output))
            return false;
        return true;
    }
    
    function sqlListVDInfoByID($id,&$output)
    {        
        $output = null;
        if(!$this->sqlListAllVswitchsWithBrNameKey($outputVswitchs)){
            // var_dump($outputVswitchs);
            return false;
        }
        $SQLGetVD = <<<SQL
            SELECT t1.*,t1.date_created as creaeTime,t1.date_modified as modifyTime,t2.* FROM tbVDImageBaseSet t1
                INNER JOIN tbVDImageBaseInfoSet t2
                ON t1.idVD=t2.idVD
                WHERE t1.idVD =:idVD
SQL;
       
        try
        {            
            $this->sqlAddVDSnapshotMaxCountField();                
            $sth = connectDB::$dbh->prepare($SQLGetVD);
            $sth->bindValue(':idVD', $id, PDO::PARAM_STR); 
            if($sth->execute())
            {                
                while( $row = $sth->fetch() ) 
                {
                    $nic = array();
                    if(strlen($row['typeNic']) > 0)
                        $nic = json_decode($row['typeNic'],true);
                    foreach ($nic as &$value) {
                        if(array_key_exists($value['br'], $outputVswitchs))
                            $value['vswitch'] = (int)$outputVswitchs[$value['br']]['VswitchID'];
                        else{
                            $value['vswitch'] = NULL;
                        }
                    }
                    if($row['rateCpuClock'] == -1)
                        $row['rateCpuClock'] = 0;    
                    $configCpu = json_decode($row['configCpu'],true);
                    $CPUType = NULL;
                    if(isset($configCpu['CPUType']))
                        $CPUType = $configCpu['CPUType'];   
                    if($row['typeUsb'] == 0)
                        $row['typeUsb'] = 2;
                    $preferNodeID = $row['idVDServer'] == null ? $row['idVDServer'] : (int)$row['idVDServer'];
                    $output = array('VDNumber'=>(int)$row['idVdNumber'],'DomainID'=>(int)$row['idDomain']
                            ,'VDName'=>$row['nameVD'],'CephID'=>(int)$row['idCephPool']
                            ,'CPU'=>(int)$row['rateCpuClock'],'CPUType'=>$CPUType,'RAM'=>(int)$row['sizeRam']
                            ,'NICs'=>$nic,'Suspend'=>(bool)$row['isAutoSuspend']
                            ,'Desc'=>$row['Description'],'USBRedirCt'=>(int)$row['ctUsb']
                            ,'USBType'=>(int)$row['typeUsb']
                            ,'CDRom'=>$row['CdRom'],'CpuCt'=>(int)$row['ctCpuCore']
                            ,'Sound'=>(int)$row['typeSound']
                            ,'CreateTime'=>$row['creaeTime']
                            ,'ModifyTime'=>$row['modifyTime']
                            ,'SSLayerMax'=>(int)$row['limitSSLyer']
                            ,'UEFI'=>(bool)$row['isUefiEnable']
                            ,'VGACount'=>(int)$row['ctGpu']
                            ,'QXL'=>(bool)$row['isQXLEnable']
                            ,'Protocol'=>(int)$row['connectProtocol']
                            ,'RDPPWDTime'=>$row['pwdTimeStamp']
                            ,'PreferNodeID'=>$preferNodeID
                            ,'ViewerProtocol'=>(int)$row['viewerProtocol']
                            );
                }
            }                  
        }
        catch (Exception $e){                
        }         
        if(is_null($output))
            return false;
        return true;
    }
    
    function sqlDeleteVD($id)
    {
        $result = false;
        $SQLUpdateSnapshotUpperLayer = "UPDATE tbvdsnapshotset SET idUpperLayer=NULL WHERE idSourceVD = :idSourceVD";
        $SQLDeleteVD = "DELETE FROM tbVDImageBaseSet WHERE idVD = :idVD";
        try
        {          
            $sth = connectDB::$dbh->prepare($SQLUpdateSnapshotUpperLayer);
            $sth->bindValue(':idSourceVD', $id, PDO::PARAM_STR); 
            if($sth->execute())
            {                
                $sth = connectDB::$dbh->prepare($SQLDeleteVD);
                $sth->bindValue(':idVD', $id, PDO::PARAM_STR); 
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
    
    function sqlDeleteSnapshot($id,$isVDisk=false)
    {
        // var_dump($id);
        $result = false;
        if($isVDisk){
            $SQLUpdateSnapshotUpperLayer = "UPDATE tbvdisksnapshotset SET idUpperLayer=NULL WHERE idSourceDisk = :id";
            $SQLDeleteVDSanpshot = "DELETE FROM tbvdisksnapshotset WHERE idSourceDisk = :id";
        }
        else{
            $SQLUpdateSnapshotUpperLayer = "UPDATE tbvdsnapshotset SET idUpperLayer=NULL WHERE idSourceVD = :id";
            $SQLDeleteVDSanpshot = "DELETE FROM tbvdsnapshotset WHERE idSourceVD = :id";
        }
        // $SQLUpdateSnapshotUpperLayer = "UPDATE tbvdsnapshotset SET idUpperLayer=NULL WHERE idSourceVD = :idSourceVD";
        // $SQLDeleteVDSanpshot = "DELETE FROM tbvdsnapshotset WHERE idSourceVD = :idVD";
        try
        {          
            $sth = connectDB::$dbh->prepare($SQLUpdateSnapshotUpperLayer);
            $sth->bindValue(':id', $id, PDO::PARAM_STR); 
            if($sth->execute())
            {                
                $sth = connectDB::$dbh->prepare($SQLDeleteVDSanpshot);
                $sth->bindValue(':id', $id, PDO::PARAM_STR); 
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

    function sqlDeleteVDDisk($id)
    {
        $result = false;     
        $SQLGetUserProfile = "SELECT t1.idVDisk from tbuserdiskset as t1 INNER JOIN tbVDiskSet as t2 ON t1.idVDisk = t2.idVDisk WHERE t2.idVD = :idVD";
        $SQLUpdateProfileDisk = "UPDATE tbVDiskSet SET idVD = null WHERE idVDisk=:idVDisk";
        $SQLDeleteVD = "DELETE FROM tbVDiskSet WHERE idVD = :idVD";
        try
        {            
            $sth = connectDB::$dbh->prepare($SQLGetUserProfile);
            $sth->bindValue(':idVD', $id, PDO::PARAM_STR); 
            if($sth->execute())
            {     
                $output_id = null;
                while( $row = $sth->fetch() ) 
                {
                    $output_id = $row['idVDisk'];
                }    
            }  
            if(!is_null($output_id)){
                 $sth = connectDB::$dbh->prepare($SQLUpdateProfileDisk);
                $sth->bindValue(':idVDisk', $output_id, PDO::PARAM_STR); 
                if($sth->execute())
                {     
                    if($sth->rowCount() != 1)
                        return false;
                }                
                $SQLDeleteVD .= " AND idVDisk NOT IN ( $output_id )";
            }
            // var_dump($SQLDeleteVD);
            $sth = connectDB::$dbh->prepare($SQLDeleteVD);
            $sth->bindValue(':idVD', $id, PDO::PARAM_STR); 
            if($sth->execute())
            {                
                $result = true;
            }  
        }
        catch (Exception $e){   
            
        }                                
        return $result;
    }
    
    function checkANDDeleteVolume()
    {
        $sqlSelectNoMappingVolume = 'select DISTINCT(t1.idVolume) from tbvolumeset as t1 LEFT JOIN tbvolumevdservermappingset as t2 ON t1.idVolume = t2.idVolume WHERE t2.idVDServer IS NULL';
        $sqlSelectVDiskNoMappingVolume = 'select count(idVDisk) as count from tbvdiskset where idVolume=:idVolume';
        $sqlDeleteVolume = 'DELETE FROM tbvolumeset where idVolume=:idVolume';
        try
        {                            
            $sth = connectDB::$dbh->prepare($sqlSelectNoMappingVolume);
            if($sth->execute())
            {                       
                while( $row = $sth->fetch() ) 
                {
                    $tmpVolume = $row['idVolume'];
                    $sth1 = connectDB::$dbh->prepare($sqlSelectVDiskNoMappingVolume);
                    $sth1->bindValue(':idVolume', $tmpVolume, PDO::PARAM_STR); 
                    if($sth1->execute())
                    {
                        while( $row1 = $sth1->fetch() ) 
                        {
                            if($row1['count'] == 0){
                                $sth2 = connectDB::$dbh->prepare($sqlDeleteVolume);
                                $sth2->bindValue(':idVolume', $tmpVolume, PDO::PARAM_STR); 
                                $sth2->execute();
                            }
                        }
                    }                       
                }    
            }               
        }
        catch (Exception $e){                
        }           

    }

    function sqlDeleteUserDefaultVD($id)
    {
        $result = false;
        $SQLUpdateDefaultVD = <<<SQL
                Update tbuserset SET idDefaultVD='' WHERE idDefaultVD = :idDefaultVD
SQL;
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLUpdateDefaultVD);
            $sth->bindValue(':idDefaultVD', $id, PDO::PARAM_STR); 
            if($sth->execute())
            {                       
                $result = true; 
            }               
        }
        catch (Exception $e){                
        }    
        return $result;
    }        
        
    function sqlUpdateVDIDofUserDisk($idDisk,$idVD)
    {
        $result = false;
        $SQLUpdate = <<<SQL
            Update tbVDiskSet SET idVD =:idVD WHERE idVDisk = :idVDisk
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($SQLUpdate);                        
            $sth->bindValue(':idVD', $idVD, PDO::PARAM_STR);           
            $sth->bindValue(':idVDisk', $idDisk, PDO::PARAM_INT);  
            if($sth->execute())
            {                                
                $result = true;
            }                        
        }
        catch (Exception $e){                             
        }     
        return $result;
    }                
    
    function sqlListServiceVDByidCephidDomain($idCephPool,$idDomain,$type,&$output)
    {        
        $output = null;
        $sqlSelect = <<<SQL
                SELECT t1.*,t2.idUser,t3.isAutoSuspend,t3.Description,t3.sizeRam,t3.typeNic,t2.jsonConfigService
                    FROM tbVDImageBaseSet as t1 
                    INNER JOIN tbvdimageserviceset as t2                    
                    ON t1.idVD=t2.idVD
                    INNER JOIN tbvdimagebaseinfoset as t3  
                    ON t1.idVD=t3.idVD
                    WHERE t1.idCephPool=:idCephPool AND t1.idDomain=:idDomain AND t2.typeService=:typeService
                    AND t1.stateVD <> 30
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($sqlSelect);                        
            $sth->bindValue(':idCephPool', $idCephPool, PDO::PARAM_INT);           
            $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT); 
            $sth->bindValue(':typeService', $type, PDO::PARAM_INT); 
            if($sth->execute())
            {                
                $output=array();
                $userid=null;                
                while( $row = $sth->fetch() ) 
                {
                    if(!is_null($row['idUser']))
                        $userid = (int)$row['idUser']; 
                        $recoveryTime = $row['date_recovery'] == null ? "" : $row['date_recovery'];            
                    $output[] = array('VDID'=>$row['idVD'],'VdId'=>$row['idVD'],'UserID'=>$userid
                        ,'VDName'=>$row['nameVD'],'DomainID'=>(int)$row['idDomain']
                        ,'VDNumber'=>$row['idVdNumber']
                        ,'RAM'=>(int)$row['sizeRam']
                        ,'CreateTime'=>$row['date_created']
                        ,'ModfiyTime'=>$row['date_modified']
                        ,'RecoveryTime'=>$recoveryTime
                        ,'Suspend'=>(bool)$row['isAutoSuspend']
                        ,'Desc'=>$row['Description']
                        ,'NIC'=>$row['typeNic']
                        ,'JsonConfig'=>$row['jsonConfigService']);
                }
            }                           
        }
        catch (Exception $e){                             
        }               
        if(is_null($output))
            return false;
        return true;
    }

    function sqlListServiceVDByidVD($idVD,&$output)
    {
        $output = null;
        $sqlSelect = <<<SQL
                SELECT * FROM tbvdimageserviceset WHERE idVD=:idVD
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($sqlSelect);                        
            $sth->bindValue(':idVD', $idVD, PDO::PARAM_STR);                       
            if($sth->execute())
            {                                                            
                while( $row = $sth->fetch() ) 
                {                          
                    $output = array('VDID'=>$row['idVD'],'Type'=>$row['typeService'],'JsonConfig'=>$row['jsonConfigService']);
                }
            }                           
        }
        catch (Exception $e){                             
        }               
        if(is_null($output))
            return false;
        return true;
    }

    function sqlUpdateServiceVDJsonConfig($input)
    {
        $result = false;        
        $sqlUpdate = <<<SQL
            Update tbvdimageserviceset SET jsonConfigService=:jsonConfigService WHERE idVD = :idVD;
SQL;
        try
        {                     
            // var_dump($input);
            // var_dump($sqlUpdate);
            $sth = connectDB::$dbh->prepare($sqlUpdate);     
            $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_STR);           
            $sth->bindValue(':jsonConfigService', $input['JsonConfig'], PDO::PARAM_STR);            
            if($sth->execute())
            {                                                
                $result = true;
            }                        
        }
        catch (Exception $e){    
            $result = false;
        }     
        // var_dump($result);
        return $result;   
    }

    function sqlListOrgVDByidCephidDomain($idCephPool,$idDomain,&$output)
    {        
        $output = null;
        $sqlSelect = <<<SQL
                SELECT t1.*,t2.idUser,t3.isAutoSuspend,t3.Description,t3.sizeRam
                    FROM tbVDImageBaseSet as t1 
                    INNER JOIN tbvdimageorgset as t2                    
                    ON t1.idVD=t2.idVD
                    INNER JOIN tbvdimagebaseinfoset as t3  
                    ON t1.idVD=t3.idVD
                    WHERE t1.idCephPool=:idCephPool AND t1.idDomain=:idDomain
                    AND t1.stateVD <> 30
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($sqlSelect);                        
            $sth->bindValue(':idCephPool', $idCephPool, PDO::PARAM_INT);           
            $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT); 
            if($sth->execute())
            {                
                $output=array();
                $userid=null;                
                while( $row = $sth->fetch() ) 
                {
                    if(!is_null($row['idUser']))
                        $userid = (int)$row['idUser']; 
                        $recoveryTime = $row['date_recovery'] == null ? "" : $row['date_recovery'];            
                    $output[] = array('VDID'=>$row['idVD'],'UserID'=>$userid
                        ,'VDName'=>$row['nameVD'],'DomainID'=>(int)$row['idDomain']
                        ,'VDNumber'=>$row['idVdNumber']
                        ,'RAM'=>(int)$row['sizeRam']
                        ,'CreateTime'=>$row['date_created']
                        ,'ModfiyTime'=>$row['date_modified']
                        ,'RecoveryTime'=>$recoveryTime
                        ,'Suspend'=>(bool)$row['isAutoSuspend']
                        ,'Desc'=>$row['Description']);
                }
            }                           
        }
        catch (Exception $e){                             
        }               
        if(is_null($output))
            return false;
        return true;
    }
    
    function sqlListSeedVDByidCephidDomain($idCephPool,$idDomain,&$output)
    {
        $output = null;
        $SQLSelect = <<<SQL
                SELECT t1.*,t3.Description,t3.sizeRam FROM tbVDImageBaseSet as t1 
                    INNER JOIN tbvdseedset as t2     
                    INNER JOIN tbVDImageBaseInfoSet as t3
                    ON t1.idVD=t2.idVD AND t1.idVD=t3.idVD
                    WHERE t1.idCephPool=:idCephPool AND t1.idDomain=:idDomain
                    AND t1.stateVD != 30
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($SQLSelect);                        
            $sth->bindValue(':idCephPool', $idCephPool, PDO::PARAM_INT);           
            $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT);
            if($sth->execute())
            {                
                $output=array();
                while( $row = $sth->fetch() ) 
                {
                    $recoveryTime = $row['date_recovery'] == null ? "" : $row['date_recovery'];   
                    $output[] = array('VDID'=>$row['idVD']
                        ,'VDName'=>$row['nameVD'],'DomainID'=>(int)$row['idDomain'],'CreateTime'=>$row['date_created'],'ModfiyTime'=>$row['date_modified'],'RecoveryTime'=>$recoveryTime
                        ,'RAM'=>(int)$row['sizeRam'],'VDNumber'=>$row['idVdNumber'],'Desc'=>$row['Description']);
                }
            }                           
        }
        catch (Exception $e){                             
        }   
        if(is_null($output))
            return false;
        return true;    
    }
    
    function sqlListUserVDByidCephidDomain($idCephPool,$idDomain,&$output)
    {
        $output = null;
        $SQLSelect = <<<SQL
                SELECT t1.*,t2.idSeed,t4.nameVD as SeedName,t2.idUser,t3.isAutoSuspend,t3.Description,t3.sizeRam
                    FROM tbVDImageBaseSet as t1 
                    INNER JOIN tbvdimageset as t2                    
                    ON t1.idVD=t2.idVD
                    INNER JOIN tbVDImageBaseSet as t4                    
                    ON t2.idSeed=t4.idVD
                    INNER JOIN tbvdimagebaseinfoset as t3  
                    ON t1.idVD=t3.idVD
                    WHERE t1.idCephPool=:idCephPool AND t1.idDomain=:idDomain
                    AND t1.stateVD <> 30
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($SQLSelect);                        
            $sth->bindValue(':idCephPool', $idCephPool, PDO::PARAM_INT);           
            $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT);  
            if($sth->execute())
            {                
                $output=array();
                while( $row = $sth->fetch() ) 
                {
                    $recoveryTime = $row['date_recovery'] == null ? "" : $row['date_recovery'];
                    $output[] = array('VDID'=>$row['idVD'],'UserID'=>(int)$row['idUser']
                        ,'SeedID'=>$row['idSeed'],'VDName'=>$row['nameVD']
                        ,'SeedName'=>$row['SeedName']
                        ,'DomainID'=>(int)$row['idDomain']
                        ,'VDNumber'=>$row['idVdNumber']
                        ,'CreateTime'=>$row['date_created']
                        ,'ModfiyTime'=>$row['date_modified']
                        ,'RecoveryTime'=>$recoveryTime
                        ,'RAM'=>(int)$row['sizeRam']
                        ,'Suspend'=>(bool)$row['isAutoSuspend']
                        ,'Desc'=>$row['Description']);
                }
            }                           
        }
        catch (Exception $e){                             
        }
        if(is_null($output))
            return false;
        return true;
    }
    
    function listVDofUser($input,&$output)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        $output_vd = null;
        $this->sqlListOrgVDByidUseridDomain($input['UserID'],$input['DomainID'], $output_vd);
        if(is_null($output_vd))
            return CPAPIStatus::NotFound;                 
        $output_user_vd = null;
        $this->sqlListUserVDByidUseridDomain($input['UserID'],$input['DomainID'], $output_user_vd);            
        if(is_null($output_user_vd))
            return CPAPIStatus::NotFound;        
        foreach ($output_vd as &$vd){              
            $input['VDID'] = $vd['VDID'];
            // $input['VDNumber'] = $vd['VDNumber'];     
            // var_dump($input);                              
            $this->listVDShell($input, $output_vd_info);
            // var_dump($output_vd_info);
            $isView = $this->sqlCheckVDSnapshotHaveView($vd);
            if(is_null($output_vd_info))
                return CPAPIStatus::ListVDofUserFail;
            else{                
                if($output_vd_info != -2 && is_null($output_vd_info['vdState'])){
                    $output_vd_info['vdOnline'] = false;
                    $output_vd_info['volumeIntegrity'] = 'online';
                    $output_vd_info['vdState'] = -3;                    
                }
                $monitorCt = 1;
                if(isset($output_vd_info['videoCt']))
                    $monitorCt = (int)$output_vd_info['videoCt'];
                $online = (int)$output_vd_info['vdOnline']==1?true:false;                
                // var_dump($online);
                $vd['Online']=$online;
                $state = $this->changeVDState($output_vd_info['vdState'],$output_vd_info['volumeIntegrity']);                
                $vd['State']=$state;                
                $vd['Monitor'] = $monitorCt;
                $vd['IsSnapshotView'] = $isView;
                $vd['IsVdOrg'] = true;
                unset($vd['VDNumber']);
            }                  
            // $vd['Online']=false;
            // $vd['State']=-3;            
        }
        foreach ($output_user_vd as &$vd){                                  
            $output_vd_info = null;    
            $input['VDID'] = $vd['VDID'];      
            $this->listVDShell($input, $output_vd_info);
            if(is_null($output_vd_info))
                return CPAPIStatus::ListVDofUserFail;
            else{             
                if($output_vd_info != -2 && is_null($output_vd_info['vdState'])){
                    $output_vd_info['vdOnline'] = false;
                    $output_vd_info['volumeIntegrity'] = 'online';
                    $output_vd_info['vdState'] = -3;
                }
                $monitorCt = 1;
                if(isset($output_vd_info['videoCt']))
                    $monitorCt = (int)$output_vd_info['videoCt'];
                $vd['Online']=(int)$output_vd_info['vdOnline']==1?true:false;
                $state = $this->changeVDState($output_vd_info['vdState'],$output_vd_info['volumeIntegrity']);
                $vd['State']=$state;           
                $vd['Monitor'] = $monitorCt; 
                $vd['IsSnapshotView'] = false;
                $vd['IsVdOrg'] = false; 
                unset($vd['VDNumber']);
            }  
            // $vd['Online']=false;
            // $vd['State']=-3;                  
        }
        // var_dump($output_user_vd);
        $output = array("Org"=>$output_vd,"User"=>$output_user_vd);
        return CPAPIStatus::ListVDofUserSuccess;
    }

    function listVDofUserForViewer($input,&$output,$defaultVD,$vdServers)
    {                
        $output_vd = null;
        $haveDefault = false;
        $userC = new UserAction();
        $this->sqlListAllGatewayJsonConfig($outputJsonConfig);
        $input['AcroGatewayConfig'] = $outputJsonConfig;
        // var_dump($defaultVD);       
        $this->sqlListOrgVDByidUseridDomainForViewer($input['UserID'],$input['DomainID'], $output_vd,$defaultVD,$haveDefault,$vdServers);        
        // var_dump($haveDefault);
        // var_dump($output_vd);
        if(is_null($output_vd))
            return CPAPIStatus::NotFound;                 
        $this->sqlListServiceVDByidUseridDomainForViewer($input['UserID'],$input['DomainID'], $output_vd,$defaultVD,$haveDefault,$vdServers);
        // $output_user_vd = null;
        $this->sqlListUserVDByidUseridDomainForViewer($input['UserID'],$input['DomainID'], $output_vd,$defaultVD,$haveDefault,$vdServers);
        if(!$haveDefault&&count($output_vd) > 0){
            $this->sqlUpdateUserDefaultVDForViewer($input['UserID'], $output_vd[0]['VdId']);
            if(count($output_vd[0]) > 0)
                $output_vd[0]['IsDefault'] = true;
        }          
        $userC->sqlListUserProfileDiskDetail($input,$outputUserDisk);
        if(isset($outputUserDisk['DiskName']))
            $input['DiskName'] = $outputUserDisk['DiskName'];
        if(count($output_vd) > 0)
            $this->listVDSpecificShell($input,$output_vd,$outputShellVDs);
        // var_dump($outputShellVDs);
        $haveBind=false;
        foreach ($output_vd as &$vd){  
            $rdpArr= array();             
            foreach ($vd['RDP'] as $rdp) {    
                $rdpArress = array();
                $rdpArress[] = array('IP'=>$rdp['IP'],'Port'=>$rdp['Port']);
                $input['RDPIP'] = $rdp['IP'];
                $this->changeRDPAddress($input,$outputAddress);
                if(isset($outputAddress)){
                    foreach ($outputAddress as $address) {
                        $rdpArress[] = $address;                        
                    }
                }
                $rdpArr=$rdpArress;
            }
            $vd['RDP'] = $rdpArr;
            $vd['IsRDPOnline'] = false;  
            $vd['BindUserDisk'] = false;
            $isBind=false;   
            if(!vd['IsVdOrg']){
                if($outputUserDisk['VDID'] == $vd['VdId']){
                    $haveBind=true;
                    $vd['BindUserDisk']= true;
                }    
            }           
            foreach ($outputShellVDs as $key=>$value) {                      
                if($vd['VdId'] == $value['vdName']){                    
                    $isOnline = false;
                    $online = (int)$value['vdOnline']==1?true:false; 
                    if($online && $input['ClientID']!=$vd['ClientID'])
                        $isOnline = true;          
                    if($value['vdState'] == 'poweron'){                    
                        $vd['IsRDPOnline'] = $vd['RDPOnline'] == 3 ? true : false;
                    }
                    else{
                        $vd['IsRDPOnline'] = false;
                    }
                    $state = $this->changeVDState($value['vdState'],$value['volumeIntegrity']);                    
                    $vd['IsVdOnline']=$isOnline;
                    $vd['VdStatus']=$state;                   
                    unset($outputShellVDs[$key]);                 
                    break;
                }
                $vd['IsVdOnline'] = false;
                $state = $this->changeVDState('poweroff','online');
                $vd['VdStatus']=$state;
                // var_dump($vd);
            }    
            // if($vd['QXL']){
            //     if($vd['USBRedirCt'] > 0 || $vd['VGACount'] > 0){
            //         $vd['AvailableProtocol'] = $this->viewerProtocolEnum['Spice'];
            //     }else{
            //         $vd['AvailableProtocol'] = $this->viewerProtocolEnum['All'];
            //     }
            // }else{
            //     $va['AvailableProtocol'] = $this->viewerProtocolEnum['RDP'];
            // }                  
        }
        if(!$haveBind){
            if(count($output_vd) > 0){     
                foreach ($output_vd as &$vd) {                    
                    if(!$vd['IsVdOrg']){
                        $outputUserDisk['VDID'] = $vd['VdId'];
                        // var_dump($outputUserDisk);
                        $this->sqlUpdateVDIDofUserDisk($outputUserDisk['ID'], $outputUserDisk['VDID']);
                        $vd['BindUserDisk'] = true;
                        break;
                    }                               
                }                           
            }                       
        }
        $output = array('UserVdImages'=>$output_vd,'DiskName'=>$input['DiskName']);
        return CPAPIStatus::ListVDofUserSuccess;
    }
    
    function listVDSpecificShell($input,$VDs,&$output)
    {
        // var_dump($input);
        $output = array();
        $cmd = $this->cmdVDListSpecific;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        foreach ($VDs as $value) {
            $cmd .= '"'.$value['VdId'].'" ';
        }                   
        exec($cmd,$outputVD,$rtn);            
        if($rtn == 0){
            // var_dump($outputVD);
            if(!is_null($outputVD))
                $output=json_decode($outputVD[0],true);            
        }
    }

    function listVDofUserNoInfo($input,&$output)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        } 
        $output_vd = null;
        $this->sqlListOrgVDByidUseridDomain($input['UserID'],$input['DomainID'], $output_vd);
        if(is_null($output_vd))
            return CPAPIStatus::NotFound; 
        $output_user_vd = null;
        $this->sqlListUserVDByidUseridDomain($input['UserID'],$input['DomainID'], $output_user_vd);        
        if(is_null($output_user_vd))
            return CPAPIStatus::NotFound;  
        // var_dump($output_user_vd);
        $output = array_merge($output_vd,$output_user_vd);        
    }
    
    function vdAssigntoUser($input){
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }            
        $output_id = null;        
        $this->sqlListVDUserIDByVDID($input['VDID'], $output_id);     
        // var_dump($input);
        if(is_null($output_id))
            return CPAPIStatus::AssignUserFail;
        // if(!is_null($output_id['OrgUserID']) || !is_null($output_id['OrgUserID']))
        //    return CPAPIStatus::AssignUserFail;
        $userC = new UserAction();        
        $userC->sqlListUserByID($input['UserID'],$outputChangeUser);
        if(isset($output_id['OrgUserID'])){
            $userC->sqlListUserByID($output_id['OrgUserID'],$outputUser);
            if($input['UserID'] != $output_id['OrgUserID']){                
                if(!$this->sqlUpdateOrgVDUserID($input['VDID'],$input['UserID'])){
                    if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>'05300E06','Riser'=>'admin','Message'=>'Failed to Change VD\'s('.$output_id['Name'].') User(From "'.$outputUser['Username'].'" To "'.$outputChangeUser['Username'].'")'),$lastID)){
                        LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
                    }    
                    return CPAPIStatus::AssignUserFail;
                }
            }
        }
        if(isset($output_id['UserUserID'])){
            if($input['UserID'] != $output_id['UserUserID']){
                $this->sqlListVDDisk($input['VDID'],$outputDisks,true);               
                foreach ($outputDisks as $value) {                                      
                    if(!strpos($value['DiskName'], 'ud') === false){                        
                        $this->sqlUpdateVDIDofUserDisk($value['DiskID'],NULL);
                        break;
                    }
                }
                $userC->sqlListUserByID($output_id['UserUserID'],$outputUser);
                if(!$this->sqlUpdateUserVDUserID($input['VDID'],$input['UserID'])){
                    if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>'05300E06','Riser'=>'admin','Message'=>'Failed to Change VD\'s('.$output_id['Name'].') User(From "'.$outputUser['Username'].'" To "'.$outputChangeUser['Username'].'")'),$lastID)){
                        LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
                    }    
                    return CPAPIStatus::AssignUserFail;
                }
            }
        }
        if(LogAction::createLog(array('Type'=>5,'Level'=>1,'Code'=>'05100D01','Riser'=>'admin','Message'=>'Change VD\'s('.$output_id['Name'].') User(From "'.$outputUser['Username'].'" To "'.$outputChangeUser['Username'].'") Successful'),$lastID)){
                    LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
                }    
        return CPAPIStatus::AssignUserSuccess;
    }

    function sqlListOrgVDByidUseridDomain($idUser,$idDomain,&$output)
    {
        $SQLSelect = <<<SQL
                SELECT t1.*,t3.idUser,t4.isAutoSuspend,t4.Description,t4.isQXLEnable,t4.ctGpu,t4.rdp
                    FROM tbVDImageBaseSet as t1 
                    INNER JOIN tbvdimageorgset as t2
                    ON t1.idVD=t2.idVD 
                    INNER JOIN tbUserBaseSet t3 
                    ON t2.idUser=t3.idUser
                    INNER JOIN tbvdimagebaseinfoset as t4  
                    ON t1.idVD=t4.idVD
                    WHERE t3.idUser=:idUser AND t3.idDomain=:idDomain
                    AND t1.stateVD <> 30 ORDER BY t1.nameVD
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($SQLSelect);                        
            $sth->bindValue(':idUser', $idUser, PDO::PARAM_INT);           
            $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT);  
            if($sth->execute())
            {                
                $output=array();
                while( $row = $sth->fetch() ) 
                {                                
                    if(isset($row['rdp']) && strlen($row['rdp']) > 0){
                        $rdp=json_decode($row['rdp'],true);
                        $rdp=$rdp['Address'];
                    }
                    else{
                        $rdp=array();
                    }                        
                    $output[] = array('VDID'=>$row['idVD'],'VDNumber'=>$row['idVdNumber']
                        ,'UserID'=>(int)$row['idUser'],'Name'=>$row['nameVD']
                        ,'DomainID'=>(int)$row['idDomain']
                        ,'ClientID'=>$row['idClient']
                        ,'Suspend'=>(bool)$row['isAutoSuspend']
                        ,'Desc'=>$row['Description']
                        ,'VGACount'=>(int)$row['ctGpu']
                        ,'QXL'=>(bool)$row['isQXLEnable']
                        ,'CreateTime'=>$row['date_created']
                        ,'RDP'=>$rdp);
                }
            // var_dump($output);
            }                           
        }
        catch (Exception $e){                             
        }     
    }

    function sqlListOrgVDByidUseridDomainForViewer($idUser,$idDomain,&$output,$defaultVD,&$haveDefault,$vdServers)
    {
        $SQLSelect = <<<SQL
                SELECT t1.*,t3.idUser,t4.isAutoSuspend,t4.Description,t4.isQXLEnable,t4.ctGpu,t4.rdp,t4.connectProtocol,t4.isRDPFirst,t4.isRDPOnline,t4.ctUsb,t4.viewerProtocol
                    FROM tbVDImageBaseSet as t1 
                    INNER JOIN tbvdimageorgset as t2
                    ON t1.idVD=t2.idVD 
                    INNER JOIN tbUserBaseSet t3 
                    ON t2.idUser=t3.idUser
                    INNER JOIN tbvdimagebaseinfoset as t4  
                    ON t1.idVD=t4.idVD
                    WHERE t3.idUser=:idUser AND t3.idDomain=:idDomain
                    AND t1.stateVD <> 30 ORDER BY t1.nameVD
SQL;
        try
        {                    
            // var_dump($idUser);                         
            // var_dump($idDomain);
            $sth = connectDB::$dbh->prepare($SQLSelect);                        
            $sth->bindValue(':idUser', $idUser, PDO::PARAM_INT);           
            $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT);  
            if($sth->execute())
            {                
                $output=array();
                while( $row = $sth->fetch() ) 
                {            
                    if(isset($row['rdp']) && strlen($row['rdp']) > 0){
                        $rdp=json_decode($row['rdp'],true);
                        $rdp=$rdp['Address'];
                    }
                    else{
                        $rdp=array();
                    }                        
                    $isView = $this->sqlCheckVDSnapshotHaveView(array('VDID'=>$row['idVD']));                   
                    $isDefault = false;
                    if($row['idVD'] == $defaultVD['DefaultVD']){                        
                        $isDefault =  true;
                        $haveDefault = true;
                    }
                    $connectProtocol = 0;
                    if(isset($row['connectProtocol']))
                        $connectProtocol = (int)$row['connectProtocol'];
                    $output[] = array('VdId'=>$row['idVD']                        
                        ,'VdName'=>$row['nameVD']
                        ,'IsDefault'=>$isDefault              
                        ,'ClientID'=>$row['idClient']
                        ,'IsSnapshotView'=>$isView
                        ,'Suspend'=>(bool)$row['isAutoSuspend']
                        ,'Desc'=>$row['Description']
                        ,'VGACount'=>(int)$row['ctGpu']
                        ,'QXL'=>(bool)$row['isQXLEnable']
                        ,'CreateTime'=>$row['date_created']
                        ,'RDP'=>$rdp
                        ,'IsVdOrg'=>true
                        ,'VdServers'=>$vdServers
                        ,'Protocol'=>$connectProtocol
                        ,'RDPFirst'=>(int)$row['isRDPFirst']
                        ,'RDPOnline'=>(int)$row['isRDPOnline']
                        ,'USBRedirCt'=>(int)$row['ctUsb']
                        ,'IsVdOnline'=>false
                        ,'AvailableProtocol'=>(int)$row['viewerProtocol']);
                }
            // var_dump($output);
            }                           
        }
        catch (Exception $e){                             
        }     
    }

    function sqlListServiceVDByidUseridDomainForViewer($idUser,$idDomain,&$output,$defaultVD,&$haveDefault,$vdServers)
    {
        $SQLSelect = <<<SQL
                SELECT t1.*,t3.idUser,t4.isAutoSuspend,t4.Description,t4.isQXLEnable,t4.ctGpu,t4.rdp,t4.connectProtocol,t4.isRDPFirst,t4.isRDPOnline,t4.ctUsb,t4.viewerProtocol
                    FROM tbVDImageBaseSet as t1 
                    INNER JOIN tbvdimageserviceset as t2
                    ON t1.idVD=t2.idVD 
                    INNER JOIN tbUserBaseSet t3 
                    ON t2.idUser=t3.idUser
                    INNER JOIN tbvdimagebaseinfoset as t4  
                    ON t1.idVD=t4.idVD
                    WHERE t3.idUser=:idUser AND t3.idDomain=:idDomain
                    AND t1.stateVD <> 30 ORDER BY t1.nameVD
SQL;
        try
        {                    
            // var_dump($idUser);                         
            // var_dump($idDomain);
            $sth = connectDB::$dbh->prepare($SQLSelect);                        
            $sth->bindValue(':idUser', $idUser, PDO::PARAM_INT);           
            $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT);  
            if($sth->execute())
            {                
                // $output=array();
                while( $row = $sth->fetch() ) 
                {            
                    if(isset($row['rdp']) && strlen($row['rdp']) > 0){
                        $rdp=json_decode($row['rdp'],true);
                        $rdp=$rdp['Address'];
                    }
                    else{
                        $rdp=array();
                    }                        
                    $isView = $this->sqlCheckVDSnapshotHaveView(array('VDID'=>$row['idVD']));                   
                    $isDefault = false;
                    if($row['idVD'] == $defaultVD['DefaultVD']){                        
                        $isDefault =  true;
                        $haveDefault = true;
                    }
                    $connectProtocol = 0;
                    if(isset($row['connectProtocol']))
                        $connectProtocol = (int)$row['connectProtocol'];
                    $output[] = array('VdId'=>$row['idVD']                        
                        ,'VdName'=>$row['nameVD']
                        ,'IsDefault'=>$isDefault              
                        ,'ClientID'=>$row['idClient']
                        ,'IsSnapshotView'=>$isView
                        ,'Suspend'=>(bool)$row['isAutoSuspend']
                        ,'Desc'=>$row['Description']
                        ,'VGACount'=>(int)$row['ctGpu']
                        ,'QXL'=>(bool)$row['isQXLEnable']
                        ,'CreateTime'=>$row['date_created']
                        ,'RDP'=>$rdp
                        ,'IsVdOrg'=>true
                        ,'VdServers'=>$vdServers
                        ,'Protocol'=>$connectProtocol
                        ,'RDPFirst'=>(int)$row['isRDPFirst']
                        ,'RDPOnline'=>(int)$row['isRDPOnline']
                        ,'USBRedirCt'=>(int)$row['ctUsb']
                        ,'IsVdOnline'=>false
                        ,'AvailableProtocol'=>(int)$row['viewerProtocol']);
                }
            // var_dump($output);
            }                           
        }
        catch (Exception $e){                             
        }     
    }

    function sqlCheckVDSnapshotHaveView($input)
    {
        $haveView = false;
        $SQLSelect = <<<SQL
                SELECT idLayer FROM tbvdsnapshotset WHERE idSourceVD = :idSourceVD AND stateLayer = 2;
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($SQLSelect);                        
            $sth->bindValue(':idSourceVD', $input['VDID'], PDO::PARAM_STR);           
            if($sth->execute())
            {                                
                while( $row = $sth->fetch() ) 
                {
                    $haveView = true;                    
                }            
            }                           
        }
        catch (Exception $e){                             
        }    
        return $haveView;
    }
    
    function sqlListUserVDByidUseridDomain($idUser,$idDomain,&$output)
    {
        $SQLSelect = <<<SQL
                SELECT t1.*,t2.idSeed,t3.idUser,t4.isAutoSuspend,t4.Description,t4.isQXLEnable,t4.ctGpu,t4.rdp FROM tbVDImageBaseSet as t1 
                    INNER JOIN tbvdimageset as t2
                    ON t1.idVD=t2.idVD 
                    INNER JOIN tbUserBaseSet t3
                    ON t2.idUser=t3.idUser
                    INNER JOIN tbvdimagebaseinfoset as t4  
                    ON t1.idVD=t4.idVD                
                    WHERE t3.idUser=:idUser AND t3.idDomain=:idDomain
                AND t1.stateVD <> 30 ORDER BY t1.nameVD
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($SQLSelect);                        
            $sth->bindValue(':idUser', $idUser, PDO::PARAM_INT);           
            $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT);  
            if($sth->execute())
            {                
                $output=array();
                while( $row = $sth->fetch() ) 
                {
                    // $isDefault = false;                    
                    // if($row['idVD']==$row['idDefaultVD'])
                    //     $isDefault = true;  
                    if(isset($row['rdp']) && strlen($row['rdp']) > 0){
                        $rdp=json_decode($row['rdp'],true);
                        $rdp=$rdp['Address'];
                    }
                    else{
                        $rdp=array();
                    }                       
                    $output[] = array('VDID'=>$row['idVD'],'VDNumber'=>$row['idVdNumber']
                        ,'UserID'=>(int)$row['idUser']
                        ,'Name'=>$row['nameVD'],'DomainID'=>(int)$row['idDomain']
                        ,'ClientID'=>$row['idClient'],'SeedID'=>(int)$row['idSeed']
                        ,'Suspend'=>(bool)$row['isAutoSuspend']
                        ,'Desc'=>$row['Description']
                        ,'VGACount'=>(int)$row['ctGpu']
                        ,'QXL'=>(bool)$row['isQXLEnable']
                        ,'RDP'=>$rdp
                        ,'CreateTime'=>$row['date_created']);
                }
                // var_dump($output);
            }                           
        }
        catch (Exception $e){                             
        }     
    }          

    function sqlListUserVDByidUseridDomainForViewer($idUser,$idDomain,&$output,$defaultVD,&$haveDefault,$vdServers)
    {
        $SQLSelect = <<<SQL
                SELECT t1.*,t2.idSeed,t3.idUser,t4.isAutoSuspend,t4.Description,t4.isQXLEnable,t4.ctGpu,t4.rdp,t4.connectProtocol,t4.isRDPFirst,t4.isRDPOnline,t4.ctUsb,t4.viewerProtocol
                    FROM tbVDImageBaseSet as t1 
                    INNER JOIN tbvdimageset as t2
                    ON t1.idVD=t2.idVD 
                    INNER JOIN tbUserBaseSet t3
                    ON t2.idUser=t3.idUser
                    INNER JOIN tbvdimagebaseinfoset as t4  
                    ON t1.idVD=t4.idVD                
                    WHERE t3.idUser=:idUser AND t3.idDomain=:idDomain
                AND t1.stateVD <> 30 ORDER BY date_created;
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($SQLSelect);                        
            $sth->bindValue(':idUser', $idUser, PDO::PARAM_INT);           
            $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT);  
            if($sth->execute())
            {                
                // $output=array();
                while( $row = $sth->fetch() ) 
                {
                    // $isDefault = false;                    
                    // if($row['idVD']==$row['idDefaultVD'])
                    //     $isDefault = true;  
                    if(isset($row['rdp']) && strlen($row['rdp']) > 0){
                        $rdp=json_decode($row['rdp'],true);
                        $rdp=$rdp['Address'];
                    }
                    else{
                        $rdp=array();
                    }            
                    $isView = $this->sqlCheckVDSnapshotHaveView(array('VDID'=>$row['idVD']));                   
                    $isDefault = false;
                    if($row['idVD'] == $defaultVD['DefaultVD']){
                        $isDefault =  true;
                        $haveDefault = true;
                    }           
                    $connectProtocol = 0;
                    if(isset($row['connectProtocol']))
                        $connectProtocol = (int)$row['connectProtocol'];
                    $output[] = array('VdId'=>$row['idVD']                        
                        ,'VdName'=>$row['nameVD']
                        ,'IsDefault'=>$isDefault              
                        ,'ClientID'=>$row['idClient']
                        ,'IsSnapshotView'=>$isView
                        ,'Suspend'=>(bool)$row['isAutoSuspend']
                        ,'Desc'=>$row['Description']
                        ,'VGACount'=>(int)$row['ctGpu']
                        ,'QXL'=>(bool)$row['isQXLEnable']
                        ,'CreateTime'=>$row['date_created']
                        ,'RDP'=>$rdp
                        ,'IsVdOrg'=>false
                        ,'VdServers'=>$vdServers
                        ,'Protocol'=>$connectProtocol
                        ,'RDPFirst'=>(int)$row['isRDPFirst']
                        ,'RDPOnline'=>(int)$row['isRDPOnline']
                        ,'USBRedirCt'=>(int)$row['ctUsb']
                        ,'IsVdOnline'=>false
                        ,'AvailableProtocol'=>(int)$row['viewerProtocol']);
                }
                // var_dump($output);
            }                           
        }
        catch (Exception $e){                             
        }     
    }       
    
    function listVDByidVD($id,&$output)
    {                
        if(!$this->connectDB()){            
            return false;
        }        
        $output = null;      
        $this->sqlListVDUserIDByVDID($id,$output);               
        if(is_null($output)){
            $output = null;     
            return false;
        }               
        return true;
    }

    function quickCreateUserVDUserProfile($input)
    {
        $logCode = '';
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }         
        $outputUUID = null;
        $this->sqlGetUUID($outputUUID);
        if(is_null($outputUUID))
            return CPAPIStatus::DBConnectFail;        
        $output_domain = null;
        $domain_c = new DomainAction();
        $domain_c->sqlListDomainByID($input['DomainID'], $output_domain);
        $input['DomainName'] = $output_domain['Name'];
        // $input['DomainUUID'] = $output_domain['DomainUUID'];        
        $output_vd = null;        
        $this->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$output_vd);
        if(!is_null($output_vd)){
            if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>'05300601','Riser'=>'admin','Message'=>"Failed to born user VD(Name:".$input['VDName'].") task(VD's name is duplicated)"),$last_id)){
                LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
            }
            return CPAPIStatus::Conflict;    
        }
        if($input['Username'] != ""){                
            $user_c = new UserAction();
            $responsecode = $user_c->createUser(array('Name'=>$input['Username'],'DomainID'=>$input['DomainID'],'isAdmin'=>false),$output_user,false);     
            if($responsecode == CPAPIStatus::CreateUserSuccess || $responsecode == CPAPIStatus::Conflict){                
                $input['UserID'] = $output_user['ID'];                
            }
            else{
                if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>'05300602','Riser'=>'admin','Message'=>"Failed to born user VD(Name:".$input['VDName'].") task(Failed to create user)"),$last_id)){
                    LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                }
                return CPAPIStatus::BornVDFail;
            }
        }
        else
        {            
            $input['UserID'] = null;
            return CPAPIStatus::BornVDFail;
        }                    
        $output_info = null;
        $this->sqlListVDInfoByID($input['SeedID'],$output_info);         
        $output_info['VDID'] = $input['SeedID'];
        if(is_null($output_info)){
            if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>'05300603','Riser'=>'admin','Message'=>"Failed to born user VD(Name:".$input['VDName'].") task(Failed to list seed vd)"),$last_id)){
                LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
            }
            return CPAPIStatus::NotFound;        
        }        
        // var_dump($output_info);
        $input['CPU']=$output_info['CPU'];
        $input['CPUType']=$output_info['CPUType'];
        $input['CpuCt']=$output_info['CpuCt'];
        $input['RAM']=$output_info['RAM'];
        $input['NIC_Info'] = json_decode($output_info['NIC'],true);
        $input['Suspend']=$output_info['Suspend'];        
        $input['USBRedirCt']=$output_info['USBRedirCt'];
        $input['CDRom']=$output_info['CDRom'];
        $input['Sound']=$output_info['Sound'];
        $input['SSLayerMax']=$output_info['SSLayerMax'];  
        $input['USBType'] = $output_info['USBType']; 
        $input['VGACount'] = $output_info['VGACount'];
        $input['UEFI'] = $output_info['UEFI'];
        $input['QXL'] = $output_info['QXL'];
        $user_c->sqlListUserProfileDiskDetail($input,$outputProfile);
        if(is_null($outputProfile) && isset($input['DiskSize']) && $input['DiskSize'] != 0){        
            $responsecode = $user_c->createUserProfileDisk($input);   
            if($responsecode != CPAPIStatus::CreateUserProfileSuccess)
                goto fail;             
        }
        connectDB::$dbh->beginTransaction();  
        $input['UUID'] = $outputUUID['UUID'];
        $input['State'] = 30;
        if(!$this->sqlInsertVDBase($input)){
            $logCode = '05300608';
            goto fail_del_vd; 
        }
        $output_vd = null;
        $this->sqlListVDByID($input['UUID'], $output_vd);            
        if(is_null($output_vd)){
            $logCode = '05300608';
            goto fail_del_vd;  
        }        
        if(!$this->sqlInsertVDInfo($input)){
            $logCode = '05300609';
            goto fail_del_vd;               
        }
        $input['VDID'] = $input['SeedID'];
        if(!$this->sqlInsertVDUser($input)){
            $logCode = '0530060A';
            goto fail_del_vd;                  
        }
        if($input['Username'] != '' && !$this->sqlUpdateUserDefaultVD($input)){
            $logCode = '0530060C';
            goto fail_del_vd;                                             
        }
        $output_vd = null;
        $this->sqlListVDByNameidDomain($input['VDName'],$input['DomainID'],$output_vd);
        if(is_null($output_vd)){
            $logCode = '0530060D';
            goto fail_del_vd;   
        }
        $input['VDID'] = $output_vd['VDID'];               
        $rtn = $this->bornUserVDShell($output_info, $input);
        if($rtn != 0){
            $logCode = '0530060E';
            goto fail_del_vd;           
        }
        connectDB::$dbh->commit();
        if(LogAction::createLog(array('Type'=>5,'Level'=>1,'Code'=>'05100501','Riser'=>'admin','Message'=>"Born User VD(Name:".$input['VDName'].") task success"),$last_id)){
            LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
        }
        $rtn = CPAPIStatus::BornVDSuccess;
        goto end;            
        fail:
            $rtn = CPAPIStatus::BornVDFail;
            goto end;
        fail_del_vd:                
            connectDB::$dbh->rollBack();   
            switch ($logCode){
                case '05300608':
                    if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>$logCode,'Riser'=>'admin','Message'=>"Failed to born user VD(Name:".$input['VDName'].") task(Failed to insert VD base db)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
                case '05300609':
                    if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>$logCode,'Riser'=>'admin','Message'=>"Failed to born user VD(Name:".$input['VDName'].") task(Failed to insert VD info db)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
                case '0530060A':
                    if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>$logCode,'Riser'=>'admin','Message'=>"Failed to born user VD(Name:".$input['VDName'].") task(Failed to insert user VD db)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
                case '0530060B':
                    if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>$logCode,'Riser'=>'admin','Message'=>"Failed to born user VD(Name:".$input['VDName'].") task(Failed to insert VD disk db)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
                case '0530060C':
                    if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>$logCode,'Riser'=>'admin','Message'=>"Failed to born user VD(Name:".$input['VDName'].") task(Failed to update default VD)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
                case '0530060D':
                    if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>$logCode,'Riser'=>'admin','Message'=>"Failed to born user VD(Name:".$input['VDName'].") task(Failed to list VD)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
                case '0530060E':
                    if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>$logCode,'Riser'=>'admin','Message'=>"Failed to born user VD(Name:".$input['VDName'].") task"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
            }
            $rtn = CPAPIStatus::BornVDFail;
            goto end;
        end:
            // var_dump($rtn);
            return $rtn;
    }
    
    function sql_list_cpubase_of_gfs($idGfs,&$output)
    {
        $SQLList = <<<SQL
            SELECT MIN(t3.hzCpu) as MINCPU
                FROM tbGfsLunSet t1 
                INNER JOIN tbVDServerSet t2
                INNER JOIN tbVDServerInfoSet t3
                ON t1.idCluster=t2.idCluster AND t2.idVDServer=t3.idVDServer
                WHERE t1.idGfs=:idGfs
SQL;
        $sth = connectDB::$dbh->prepare($SQLList);
        $sth->bindValue(':idGfs', $idGfs, PDO::PARAM_INT);          
        if($sth->execute()){                                   
            while( $row = $sth->fetch() ) 
            {                                 
                $output = array('MINCPU'=>(int)$row['MINCPU']);
            }                            
        }
    }
    
    function sql_list_free_resource_of_gfs_and_domain($idDomain,$idGfs,&$output)
    {
        $SQLList = <<<SQL
            SELECT * FROM tbTerritorySet WHERE idGfs=:idGfs AND idDomain=:idDomain;
SQL;
        $sth = connectDB::$dbh->prepare($SQLList);
        $sth->bindValue(':idGfs', $idGfs, PDO::PARAM_INT); 
        $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT); 
        if($sth->execute()){                        
            $output = array('FreeSizeGB'=>0,'RAMFreeMB'=>0,'CPUFree'=>0);
            while( $row = $sth->fetch() ) 
            {                                    
                $output['FreeSizeGB'] += $row['quotaTerritory'];               
                $output['RAMFreeMB'] += $row['sizeRam'];                
                $output['CPUFree'] += $row['hzCpu'];                
            }                            
        }
    }
    
    function sql_list_assign_reource_of_gfs_and_domain($idDomain,$idGfs,&$output)
    {
        $SQLList = <<<SQL
            SELECT SUM(t2.rateCpuClock) as TotalCPU,
                SUM(t2.sizeRam) as TotalRAM       
                FROM tbVDImageBaseSet t1 
                INNER JOIN tbVDImageBaseInfoSet t2                
                ON t1.idVD=t2.idVD
                WHERE t1.idGfs=:idGfs AND t1.idDomain=:idDomain;
SQL;
        $SQLListDisk = <<<SQL
            SELECT SUM(sizeVDisk) as TotalSize FROM tbvdiskset WHERE idDomain = :idDomain AND idGfs = :idGfs
SQL;
        $sth = connectDB::$dbh->prepare($SQLList);
        $sth->bindValue(':idGfs', $idGfs, PDO::PARAM_INT); 
        $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT); 
        if($sth->execute()){                        
            $output = array('TotalSize'=>0,'TotalRAM'=>0,'TotalCPU'=>0);
            while( $row = $sth->fetch() ) 
            {                 
                if(!is_null($row)['TotalCPU'])
                    $output['TotalCPU'] += $row['TotalCPU'];               
                if(!is_null($row)['TotalRAM'])
                    $output['TotalRAM'] += $row['TotalRAM'];                
               // if(!is_null($row)['TotalSize'])
               //     $output['TotalSize'] += $row['TotalSize'];
            }                            
            $sth1 = connectDB::$dbh->prepare($SQLListDisk);
            $sth1->bindValue(':idGfs', $idGfs, PDO::PARAM_INT); 
            $sth1->bindValue(':idDomain', $idDomain, PDO::PARAM_INT); 
            if($sth1->execute()){     
                while( $row1 = $sth1->fetch() ) 
                {                                             
                    if(!is_null($row1)['TotalSize'])
                        $output['TotalSize'] += $row1['TotalSize'];
                }
            }
        }
    }    
    
    function sqlUpdateVDCPURAMUSBSound($id,$input)
    {
        $SQLUpdateInfo = <<<SQL
            Update tbVDImageBaseInfoSet SET ctCpuCore=:ctCpuCore,rateCpuClock=:rateCpuClock,
                sizeRam=:sizeRam,ctUsb=:ctUsb,typeSound=:typeSound                
                WHERE idVD = :idVD
SQL;
        try
        {  
            // var_dump($id);                                                                           
            // var_dump($input);           
            $sth = connectDB::$dbh->prepare($SQLUpdateInfo);
            $sth->bindValue(':idVD', $id, PDO::PARAM_INT); 
            $sth->bindValue(':ctCpuCore', $input['CpuCt'], PDO::PARAM_INT); 
            $sth->bindValue(':rateCpuClock', $input['CPU'], PDO::PARAM_INT); 
            $sth->bindValue(':sizeRam', $input['RAM'], PDO::PARAM_INT);             
            $sth->bindValue(':ctUsb', $input['USBRedirCt'], PDO::PARAM_INT); 
            $sth->bindValue(':typeSound', $input['Sound'], PDO::PARAM_INT);                    
            if($sth->execute())
                $result = true;            
        }
        catch (Exception $e){    
            $result = false;
        }     
        return $result;
    }

    function sqlUpdateVDDisk($input)
    {
        // var_dump($input);
        $result = false;
        $SQLUpdateInfo = <<<SQL
            Update tbVDiskSet SET stateVDisk=:stateVDisk,sizeVDisk=:sizeVDisk,nameVDisk=:nameVDisk,configVDisk=:configVDisk,limitSSLyer=:limitSSLyer WHERE idVDisk = :idVDisk
SQL;
        try
        {            
            $sth = connectDB::$dbh->prepare($SQLUpdateInfo);
            $sth->bindValue(':idVDisk', $input['DiskID'], PDO::PARAM_INT); 
            $sth->bindValue(':stateVDisk', $input['State'], PDO::PARAM_INT);    
            $sth->bindValue(':sizeVDisk', $input['Size'], PDO::PARAM_INT);              
            $sth->bindValue(':nameVDisk', $input['DiskName'], PDO::PARAM_STR);            
            $configVDisk = json_encode(array('DiskType'=>$input['DiskType'],'DiskCache'=>$input['DiskCache']));
            $sth->bindValue(':configVDisk', $configVDisk, PDO::PARAM_STR);
            if(!isset($input['SSLayerMax']))
                $input['SSLayerMax'] = $this->ssLayerDefault;            
            $sth->bindValue(':limitSSLyer', $input['SSLayerMax'], PDO::PARAM_INT);
            if($sth->execute()){
                // if($sth->rowCount() == 1)
                    $result = true;            
            }
        }
        catch (Exception $e){    
            $result = false;
        }     
        return $result;
    }

    function sqlUpdateVDAutosuspend($input)
    {
        // var_dump($input);
        $result = false;
        $SQLUpdateInfo = <<<SQL
            Update tbvdimagebaseinfoset SET isAutoSuspend=:isAutoSuspend WHERE idVD = :idVD
SQL;
        try
        {                        
            $sth = connectDB::$dbh->prepare($SQLUpdateInfo);
            $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_INT); 
            $sth->bindValue(':isAutoSuspend', $input['Suspend'], PDO::PARAM_BOOL);
            if($sth->execute()){
                // if($sth->rowCount() == 1)
                    $result = true;            
            }
        }
        catch (Exception $e){    
            $result = false;
        }     
        // var_dump($result);
        return $result;
    }

    function sqlUpdateVDtoRestoreStatus($idVD,$status)
    {
        $result = false;
        $sqlUpdate = <<<SQL
            Update tbVDImageBaseSet SET stateVD=:stateVD WHERE idVD = :idVD
SQL;
        try
        {            
            $sth = connectDB::$dbh->prepare($sqlUpdate);
            $sth->bindValue(':idVD', $idVD, PDO::PARAM_STR); 
            $sth->bindValue(':stateVD', $status, PDO::PARAM_INT);           
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

    function sqlUpdateVDStatus($input)
    {
        // var_dump($input);
        $result = false;
        $SQLUpdateInfo = <<<SQL
            Update tbVDImageBaseSet SET stateVD=:stateVD,date_created=:date_created,date_modified=:date_modified WHERE idVD = :idVD
SQL;
        try
        {            
            $sth = connectDB::$dbh->prepare($SQLUpdateInfo);
            $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_INT); 
            $sth->bindValue(':stateVD', $input['State'], PDO::PARAM_INT);             
            $sth->bindValue(':date_created', $input['CreateTime'], PDO::PARAM_STR);
            $sth->bindValue(':date_modified', $input['CreateTime'], PDO::PARAM_STR);
            if($sth->execute()){
                // if($sth->rowCount() == 1)
                    $result = true;            
            }
        }
        catch (Exception $e){    
            $result = false;
        }     
        // var_dump($result);
        return $result;
    }

    function sqlUpdateVDStatusNoTime($input)
    {
        // var_dump($input);
        $result = false;
        $SQLUpdateInfo = <<<SQL
            Update tbVDImageBaseSet SET stateVD=:stateVD WHERE idVD = :idVD
SQL;
        try
        {            
            $sth = connectDB::$dbh->prepare($SQLUpdateInfo);
            $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_INT); 
            $sth->bindValue(':stateVD', $input['State'], PDO::PARAM_INT);             
            if($sth->execute()){
                // if($sth->rowCount() == 1)
                    $result = true;            
            }
        }
        catch (Exception $e){    
            $result = false;
        }     
        // var_dump($result);
        return $result;
    }

    function sqlUpdateVDDescription($input)
    {
        // var_dump($input);
        $result = false;
        $SQLUpdateInfo = <<<SQL
            Update tbvdimagebaseinfoset SET Description=:Description WHERE idVD = :idVD
SQL;
        try
        {            
            $sth = connectDB::$dbh->prepare($SQLUpdateInfo);
            $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_INT); 
            $sth->bindValue(':Description', $input['Description'], PDO::PARAM_STR);             
            if($sth->execute()){
                // if($sth->rowCount() == 1)
                    $result = true;            
            }
        }
        catch (Exception $e){    
            $result = false;
        }     
        // var_dump($result);
        return $result;
    }

    function sqlUpdateVDNIC($id,$nic)
    {
        $result = false;
        $SQLUpdateInfo = <<<SQL
            Update tbVDImageBaseInfoSet SET typeNic=:typeNic WHERE idVD = :idVD
SQL;
        try
        {            
            $tmpNICs = json_decode($nic,true);            
            // var_dump($tmpNICs);
            $nics = array();
            foreach ($tmpNICs as $tmpNIC) {                
                $nics[] = array("br"=>$tmpNIC['br'],"speed"=>(int)$tmpNIC['speed'],"mac"=>$tmpNIC['mac'],"vswitch"=>(int)$tmpNIC['vswitch']);
            }
            // var_dump($nics);
            $nics=json_encode($nics);
            $sth = connectDB::$dbh->prepare($SQLUpdateInfo);
            $sth->bindValue(':idVD', $id, PDO::PARAM_INT); 
            $sth->bindValue(':typeNic', $nics, PDO::PARAM_STR);             
            if($sth->execute()){
                // if($sth->rowCount() == 1)
                    $result = true;            
            }
        }
        catch (Exception $e){    
            $result = false;
        }     
        // var_dump($result);
        return $result;
    }

    function sqlUpdateVDISO($id,$CDRom)
    {
        $result = false;
        $SQLUpdateInfo = <<<SQL
            Update tbVDImageBaseInfoSet SET CdRom=:CdRom WHERE idVD = :idVD
SQL;
        try
        {  
            // var_dump($id);                                                                           
            // var_dump($input);           
            $sth = connectDB::$dbh->prepare($SQLUpdateInfo);
            $sth->bindValue(':idVD', $id, PDO::PARAM_STR); 
            $sth->bindValue(':CdRom', $CDRom, PDO::PARAM_STR);             
            if($sth->execute()){
                // if($sth->rowCount() == 1)
                    $result = true;            
            }
        }
        catch (Exception $e){    
            $result = false;
        }     
        return $result;
    }

    function sqlUpdateVDAutoSuspenDescription($input)
    {
        // var_dump($input);
        $result = false;
        $sqlUpdate = <<<SQL
            Update tbVDImageBaseInfoSet SET isAutoSuspend=:isAutoSuspend,Description=:Description WHERE idVD = :idVD
SQL;
        try
        {  
            // var_dump($id);                                                                           
            // var_dump($input);           
            $sth = connectDB::$dbh->prepare($sqlUpdate);
            $sth->bindValue(':isAutoSuspend', $input['Suspend'], PDO::PARAM_BOOL); 
            $sth->bindValue(':Description', $input['Desc'], PDO::PARAM_STR);
            $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_STR);          
            if($sth->execute()){
                // if($sth->rowCount() == 1)
                $result = true;            
            }
        }
        catch (Exception $e){    
            $result = false;
        }     
        return $result;
    }

    function sqlUpdateVDRecoveryTime($id,$recoveryTime,$isVDisk=false)
    {
        $result = false;
        if($isVDisk)
            $SQLUpdate = <<<SQL
            Update tbVDiskSet SET date_recovery=:date_recovery WHERE idVDisk = :id
SQL;

        else
            $SQLUpdate = <<<SQL
            Update tbVDImageBaseSet SET date_recovery=:date_recovery WHERE idVD = :id
SQL;
        try
        {  
            // var_dump($id);                                                                                               
            $sth = connectDB::$dbh->prepare($SQLUpdate);
            $sth->bindValue(':id', $id, PDO::PARAM_STR);                     
            $sth->bindValue(':date_recovery', $recoveryTime, PDO::PARAM_STR);
            if($sth->execute()){
                // if($sth->rowCount() == 1)
                    $result = true;            
            }
        }
        catch (Exception $e){    
            $result = false;
        }     
        return $result;
    }

    function sqlUpdateVDModfiyTime($id)
    {
        $result = false;
        $SQLUpdate = <<<SQL
            Update tbVDImageBaseSet SET date_modified=now() WHERE idVD = :idVD
SQL;
        try
        {  
            // var_dump($id);                                                                           
            // var_dump($input);           
            $sth = connectDB::$dbh->prepare($SQLUpdate);
            $sth->bindValue(':idVD', $id, PDO::PARAM_STR);                     
            if($sth->execute()){
                // if($sth->rowCount() == 1)
                    $result = true;            
            }
        }
        catch (Exception $e){    
            $result = false;
        }     
        return $result;
    }

    function sqlUpdateVDPreferNode($input)
    {
        // var_dump($input);
        $result = false;
        $sqlUpdate = <<<SQL
                Update tbVDImageBaseSet SET idVDServer=:idVDServer WHERE idVD = :idVD
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($sqlUpdate);                      
            $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_STR);            
            $sth->bindValue(':idVDServer',$input['NodeID'], PDO::PARAM_INT);            
            if($sth->execute())
            {                                                
                $result = true;
                // var_dump($sth->rowCount());
            }                        
        }
        catch (Exception $e){    
            $result = false;
        }     
        return $result;
    }


    function sqlUpdateVD($input,$updateDescription=true)
    {
        // var_dump($input);
        $result = false;
        $SQLUpdateName = <<<SQL
            Update tbVDImageBaseSet SET nameVD=:nameVD,date_modified=now() WHERE idVD = :idVD
SQL;
        if($updateDescription){
            $SQLUpdateInfo = <<<SQL
            Update tbVDImageBaseInfoSet SET ctCpuCore=:ctCpuCore,rateCpuClock=:rateCpuClock,
                sizeRam=:sizeRam,CdRom=:CdRom,ctUsb=:ctUsb,typeSound=:typeSound,
                isAutoSuspend=:isAutoSuspend,Description=:Description,configCpu=:configCpu,
                limitSSLyer=:limitSSLyer,typeUsb=:typeUsb,ctGpu=:ctGpu,isUefiEnable=:isUefiEnable,
                isQXLEnable=:isQXLEnable
                WHERE idVD = :idVD
SQL;
        }
        else{
            $SQLUpdateInfo = <<<SQL
            Update tbVDImageBaseInfoSet SET ctCpuCore=:ctCpuCore,rateCpuClock=:rateCpuClock,
                sizeRam=:sizeRam,CdRom=:CdRom,ctUsb=:ctUsb,typeSound=:typeSound,
                isAutoSuspend=:isAutoSuspend,configCpu=:configCpu,
                limitSSLyer=:limitSSLyer,typeUsb=:typeUsb,ctGpu=:ctGpu,isUefiEnable=:isUefiEnable,
                isQXLEnable=:isQXLEnable
                WHERE idVD = :idVD
SQL;
        }
        try
        {                                             
            $sth = connectDB::$dbh->prepare($SQLUpdateName);  
            $sth->bindValue(':nameVD', $input['VDName'], PDO::PARAM_STR); 
            $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_STR);  
            if($sth->execute())
            {                                
                // var_dump($sth->rowCount());
                $sth = connectDB::$dbh->prepare($SQLUpdateInfo);
                $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_STR); 
                $sth->bindValue(':ctCpuCore', $input['CpuCt'], PDO::PARAM_INT); 
                $sth->bindValue(':rateCpuClock', $input['CPU'], PDO::PARAM_INT); 
                $sth->bindValue(':sizeRam', $input['RAM'], PDO::PARAM_INT); 
                $sth->bindValue(':CdRom', $input['CDRom'], PDO::PARAM_STR);
                $sth->bindValue(':typeUsb', $input['USBType'], PDO::PARAM_INT); 
                $sth->bindValue(':ctUsb', $input['USBRedirCt'], PDO::PARAM_INT); 
                $sth->bindValue(':typeSound', $input['Sound'], PDO::PARAM_INT);
                $sth->bindValue(':isAutoSuspend', $input['Suspend'], PDO::PARAM_BOOL);
                if($updateDescription)
                    $sth->bindValue(':Description', $input['Desc'], PDO::PARAM_STR);
                $cpuConfig = json_encode(array('CPUType'=>$input['CPUType']));            
                $sth->bindValue(':configCpu', $cpuConfig, PDO::PARAM_STR);
                $ssLayerMax = $this->ssLayerDefault;
                if(isset($input['SSLayerMax']))
                    $ssLayerMax = $input['SSLayerMax'];
                $sth->bindValue(':limitSSLyer', $ssLayerMax, PDO::PARAM_INT);
                $sth->bindValue(':ctGpu', $input['VGACount'], PDO::PARAM_INT);
                $sth->bindValue(':isUefiEnable', $input['UEFI'], PDO::PARAM_BOOL);
                $sth->bindValue(':isQXLEnable', $input['QXL'], PDO::PARAM_BOOL);
                if($sth->execute())
                    $result = true;
                // var_dump($sth->rowCount());
            }                        
        }
        catch (Exception $e){    
            $result = false;
        }     
        return $result;
    }

    function sqlUpdateVDCPUType($input)
    {
        // var_dump($input);
        $result = false;
        $SQLUpdateInfo = <<<SQL
            Update tbVDImageBaseInfoSet SET configCpu=:configCpu
                WHERE idVD = :idVD
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($SQLUpdateInfo);            
            $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_STR);
            $cpuConfig = json_encode(array('CPUType'=>$input['CPUType']));            
            $sth->bindValue(':configCpu', $cpuConfig, PDO::PARAM_STR);
            if($sth->execute())
                $result = true;                      
        }
        catch (Exception $e){    
            $result = false;
        }     
        return $result;
    }

    function sqlUpdateVDViewerProtocol($input)
    {
        // var_dump($input);
        $result = false;
        $sqlUpdate = <<<SQL
            Update tbVDImageBaseInfoSet SET viewerProtocol=:viewerProtocol WHERE idVD = :idVD
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($sqlUpdate);            
            $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_STR);            
            $sth->bindValue(':viewerProtocol', $input['ViewerProtocol'], PDO::PARAM_INT);
            if($sth->execute())
                $result = true;                      
        }
        catch (Exception $e){    
            $result = false;
        }     
        return $result;
    }

    function modify_seed_vd($input)
    {
        $this->connectDB();        
        if(connectDB::$dbh == null){
            $output = null;     
            return CPAPIStatus::DBConnectFail;
        }    
        $output_vd = null;
        $this->sqlListVDByNameidDomain($input['Name'],$input['DomainID'],$output_vd);        
        if(!is_null($output_vd) && $input['ID'] != $output_vd['ID'])
            return CPAPIStatus::Conflict;    
        if(!$this->sql_update_seed_vd($input))
            return CPAPIStatus::ModifyVDFail;
        return CPAPIStatus::ModifyVDSuccess;
    }
    
    function sql_update_seed_vd($input)
    {
        $result = false;
        $SQLUpdateName = <<<SQL
            Update tbVDImageBaseSet SET nameVD=:nameVD WHERE idVD = :idVD
SQL;
        $SQLUpdateInfo = <<<SQL
            Update tbVDImageBaseInfoSet SET Description=:Description WHERE idVD = :idVD
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($SQLUpdateName);  
            $sth->bindValue(':nameVD', $input['Name'], PDO::PARAM_STR); 
            $sth->bindValue(':idVD', $input['ID'], PDO::PARAM_STR);  
            if($sth->execute())
            {                                
                $sth = connectDB::$dbh->prepare($SQLUpdateInfo);
                $sth->bindValue(':idVD', $input['ID'], PDO::PARAM_STR);                 
                $sth->bindValue(':Description', $input['Desc'], PDO::PARAM_STR);
                if($sth->execute()){
                    $result = true;
                }
            }                        
        }
        catch (Exception $e){    
            $result = false;
        }     
        return $result;
    }       
        
    function vd_assign_to_free($id)
    {
        $this->connectDB();        
        if(connectDB::$dbh == null){
            return CPAPIStatus::DBConnectFail;        
        }
        $output_id = null;        
        $this->sql_list_vd_userid_by_id($id, $output_id);        
        if(is_null($output_id))
            return CPAPIStatus::AssignFreeFail;
        if(!is_null($output_id['OrgUserID'])){
            if(!$this->sqlUpdateOrgVDUserID($id))
                return CPAPIStatus::AssignFreeFail;
        }           
        if(!is_null($output_id['UseridUser'])){
            if(!$this->sqlUpdateUserVDUserID($id))
                return CPAPIStatus::AssignFreeFail;
        }
        return CPAPIStatus::AssignFreeSuccess;
    }        
    
    function sql_list_vd_userid_by_id($id,&$output)
    {
        $SQLListOrgUserID = <<<SQL
            SELECT idUser as OrgidUser FROM tbvdimageorgset WHERE idVD=:idVD1               
SQL;
        $SQLListUserUserID = <<<SQL
            SELECT idUser as UseridUser FROM tbvdimageset
 WHERE idVD=:idVD2
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($SQLListOrgUserID);                        
            $sth->bindValue(':idVD1', $id, PDO::PARAM_STR);            
            if($sth->execute())
            {                                
                while( $row = $sth->fetch() ) 
                {     
                    $output = array('OrgUserID'=>$row['OrgidUser']
                            ,'UserUserID'=>null,'type'=>0);
                }
            }       
            $sth = connectDB::$dbh->prepare($SQLListUserUserID);                        
            $sth->bindValue(':idVD2', $id, PDO::PARAM_STR);            
            if($sth->execute())
            {                                
                while( $row = $sth->fetch() ) 
                {     
                    $output = array('OrgUserID'=>null
                            ,'UserUserID'=>$row['UseridUser'],'type'=>1);
                }
            }   
        }
        catch (Exception $e){ 
            $output = null;
        }     
    }
    
    function sqlListVDIDOfUserDiskbyiduser($idUser,&$output)
    {
        $output = NULL;
        $SQLList = <<<SQL
            SELECT t1.idVD FROM tbVDiskSet as t1
                INNER JOIN tbuserdiskset as t2
                ON t1.idVDisk = t2.idVDisk
                WHERE t2.idUser=:idUser
SQL;
        try
        {                                           
            $sth = connectDB::$dbh->prepare($SQLList);                                             
            $sth->bindValue(':idUser', $idUser, PDO::PARAM_STR);           
            if($sth->execute())
            {         
                if($sth->rowCount() == 1){               
                    while( $row = $sth->fetch() ) 
                    {     
                        $output = $row['idVD'];
                    }
                }
                else
                    $output = "";
            }                   
        }
        catch (Exception $e){ 
            $output = null;
        }
    }
    
    function sqlUpdateOrgVDUserID($idVD,$idUser=null)
    {
        $result = false;
        $SQLUpdateOrg = <<<SQL
            UPDATE tbvdimageorgset SET idUser=:idUser WHERE idVD=:idVD
SQL;
        try
        {                                                    
            $sth = connectDB::$dbh->prepare($SQLUpdateOrg);                       
            $sth->bindValue(':idVD', $idVD, PDO::PARAM_STR);           
            $sth->bindValue(':idUser', $idUser);
            if($sth->execute())
            {                             
                if($sth->rowCount() == 1)
                    $result = true;                    
            }                           
        }
        catch (Exception $e){                             
        }    
        return $result;
    }
    
    function sqlUpdateUserVDUserID($idVD,$idUser=null)
    {
        $result = false;
        $sqlUpdateUserVDUserID = <<<SQL
            UPDATE tbvdimageset SET idUser=:idUser WHERE idVD=:idVD
SQL;
        try
        {                            
            // var_dump($idVD);                        
            // var_dump($idUser);
            $sth = connectDB::$dbh->prepare($sqlUpdateUserVDUserID);          
            $sth->bindValue(':idVD', $idVD, PDO::PARAM_STR);           
            $sth->bindValue(':idUser', $idUser, PDO::PARAM_INT);
            if($sth->execute())
            {                             
                if($sth->rowCount() == 1)
                    $result = true;                    
            }                           
        }
        catch (Exception $e){                             
        }    
        // var_dump($result);
        return $result;
    }

    function sqlListVDDisk($idVD,&$output,$containsUD=false)
    {
        $output = null;
        if($containsUD)
            $SQLList = <<<SQL
            SELECT * FROM tbVDiskSet WHERE idVD=:idVD
SQL;
        else{
            $SQLList = <<<SQL
            SELECT * FROM tbVDiskSet WHERE idVD=:idVD AND nameVDisk NOT LIKE '%ud%'
SQL;
        }

        try
        {                                           
            $sth = connectDB::$dbh->prepare($SQLList);                                             
            $sth->bindValue(':idVD', $idVD, PDO::PARAM_STR);           
            if($sth->execute())
            {         
                $output = array();
                while( $row = $sth->fetch() ) 
                {     
                    $configVDisk = $row['configVDisk'];
                    $DiskType = 0;
                    $DiskCache = 0;
                    $configVDisk = json_decode($configVDisk,true);
                    if(isset($configVDisk['DiskType'])){
                        $DiskType = $configVDisk['DiskType'];
                        $DiskCache = $configVDisk['DiskCache'];
                    }
                    $output[] = array('DiskID'=>(int)$row['idVDisk'],'DiskName'=>$row['nameVDisk'],'State'=>$row['stateVDisk'],'Size'=>(int)$row['sizeVDisk'],'UsedSize'=>0,'DiskType'=>$DiskType,'DiskCache'=>$DiskCache,"VolumeID"=>$row['idVolume']);
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
        
    function sqlListVDDiskByID($idVD,$idVDisk,&$output)
    {
        $output = null;
        $SQLList = <<<SQL
            SELECT * FROM tbVDiskSet WHERE idVD=:idVD AND idVDisk = :idVDisk
SQL;
        try
        {                                           
            $sth = connectDB::$dbh->prepare($SQLList);
            $sth->bindValue(':idVD', $idVD, PDO::PARAM_STR);           
            $sth->bindValue(':idVDisk', $idVDisk, PDO::PARAM_INT);           
            if($sth->execute())
            {                         
                while( $row = $sth->fetch() ) 
                {     
                    $configVDisk = json_decode($row['configVDisk'],true);                    
                    $output = array('DiskName'=>$row['nameVDisk'],'State'=>$row['stateVDisk'],'Size'=>(int)$row['sizeVDisk'],'DiskType'=>$configVDisk['DiskType'],'VolumeID'=>$row['idVolume']);
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
    
    function sqlListOrgVDforExport($idVD,&$output)
    {
        $output = NULL;
        $SQLList = <<<SQL
            SELECT t1.nameVD,t1.idVdNumber,t2.nameDomain
                FROM tbVDImageBaseSet as t1 
                INNER JOIN tbDomainSet as t2
                ON t1.idDomain=t2.idDomain
                WHERE t1.idVD = :idVD
SQL;
        try        
        {  
            $sth = connectDB::$dbh->prepare($SQLList);                       
            $sth->bindParam(':idVD', $idVD);                    
            if($sth->execute())
            {                                              
                while( $row = $sth->fetch() ) 
                {                         
                    $output = array("Name"=>$row['nameVD'],'DomainName'=>$row['nameDomain']
                            ,'VDNumber'=>$row['idVdNumber']);
                }                     
            }               
        }
        catch (Exception $e){                
            $output=NULL;
        }
        if(is_null($output))
            return false;
        return true;
    }
    
    
    function seedRebornUserVD($input)
    {
        $log_code = "";
        $uservd_name = "";
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }            
        $domainC = new DomainAction();
        $output_domain = null;
        $domainC->sqlListDomainByID($input['DomainID'], $output_domain);
        $output_seed = null;
        $this->sqlListSeedVD($input['SeedID'], $output_seed);  
        // var_dump($output_seed);
        $bornStr = $input['Renew'] == true ? 'Renew' : 'Reborn';
        if(is_null($output_seed)){
            if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>'05301601','Riser'=>'admin','Message'=>"Failed to $bornStr user vd from seed task(Failed to list seed vd)"),$lastID)){
                LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            }
            return CPAPIStatus::NotFound;
        }
        $output_seed['DomainID'] = $input['DomainID'];
        $output_seed['DomainName'] = $output_domain['Name'];
        $output_user_vds = null;
        $this->sqlListUserVDofSeed($input['SeedID'], $output_user_vds);        
        // var_dump($output_user_vds);
        if(is_null($output_user_vds)){
            if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>'05301602','Riser'=>'admin','Message'=>"Failed to $bornStr user vd from seed(Name:".$output_seed['VDName'].") task(Failed to list user vds)"),$lastID)){
                LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            }
            return CPAPIStatus::NotFound;        
        }
        if(count($output_user_vds) > 0){
            connectDB::$dbh->beginTransaction();
            foreach ($output_user_vds as $user_vd){
                $uservd_name = $user_vd['VDName'];
                if(!$this->sqlDeleteVDDisk($user_vd['VDID'])){
                    $log_code = '05301604';
                    goto fail;
                }
                $user_vd['State'] = 30;
                if(!$this->sqlUpdateVDStatusNoTime($user_vd)){                    
                    $log_code = '05301604';
                    goto fail;
                }            
                $user_vd['RDPFirst'] = 2;
                if(!$this->sqlUpdateVDRDPFirst($user_vd)){                    
                    $log_code = '05301604';
                    goto fail;
                }
            }    
            $output_seed['CephID'] = $input['CephID'];
            $output_seed['ConnectIP'] = $input['ConnectIP'];
            $output_seed['Renew'] = $input['Renew'];
            if($this->seedRebornUserVDShell($output_seed) == 0){      
                connectDB::$dbh->commit();
                if(LogAction::createLog(array('Type'=>5,'Level'=>1,'Code'=>'05101501','Riser'=>'admin','Message'=>
                    "$bornStr User VDs from Seed(Name:".$output_seed['VDName'].") task Success"),$lastID)){
                    LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
                }
            }                        
            else{
                    $log_code = '05301605';
                    goto fail;
            }  
        }
        goto end;
        fail:
            connectDB::$dbh->rollBack();
            switch ($log_code){
                case '05301604':
                    if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>'05301604','Riser'=>'admin','Message'=>"Failed to $bornStr user vd from seed(Name:".$output_seed['VDName'].") task(Failed to update vd status)"),$lastID)){
                        LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
                    }
                    break;
                case '05301605':
                    if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>'05301605','Riser'=>'admin','Message'=>"Failed to $bornStr user vd from seed(Name:".$output_seed['VDName'].") task"),$lastID)){
                        LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
                    }
                    break;
            }
            $this->insertSeedRebornUserVDFailTaskShell($output_seed);
            return CPAPIStatus::SeedRebornFail;
        end:
            return CPAPIStatus::SeedRebornSuccess;
    }
    
    function insertSeedRebornUserVDFailTaskShell($input_seed)
    {
        $cmd = $this->cmdVDTaskErrTask;
        $cmd = str_replace ( 'ip' , $input_seed['ConnectIP'], $cmd);
        if($input_seed['Renew'])
            $taskTypeStr = 'renew';
        else
            $taskTypeStr = 'distribute';
        $cmd .= '"'.base64_encode($taskTypeStr.'$*'.$input_seed['CephID'].'$*'.$input_seed['VDID'].'$*'.$input_seed['VDName'].'$*'.$input_seed['DomainID'].'$*'.$input_seed['DomainName']).'" ';        
        $cmd .= 'seedReborn';        
        exec($cmd,$output,$rtn);   
    }

    function seedRebornUserVDShell($input_seed)
    {
        $rtn = -2;
        if(!is_null($input_seed['ConnectIP'])){
            $cmd = $this->cmdVDTaskSeedReborn;
            $cmd = str_replace ( 'ip' , $input_seed['ConnectIP'], $cmd);    
            if($input_seed['Renew'])
                $taskTypeStr = 'renew';
            else
                $taskTypeStr = 'distribute';
            $cmd .= '"'.base64_encode($taskTypeStr.'$*'.$input_seed['CephID'].'$*'.$input_seed['VDID'].'$*'.$input_seed['VDName'].'$*'.$input_seed['DomainID'].'$*'.$input_seed['DomainName']).'" ';         
            // $cmd .= '"'.base64_encode("$type$*".$input_seed['Name'].'$*'.$input_user['Name']).'" ';
            $cmd .= '"'.$input_seed['VDID'].'" ';                             
            $cmd .= '"'.$input_seed['DomainID'].'" ';
            // var_dump($cmd);
            exec($cmd,$output,$rtn);                               
            // var_dump($rtn);
        }           
        return $rtn;
    }

    /*
        reportMetaData errorCode addingTime allDisk(base64) allNIC(base64)
        MetaData : born$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$inputOrg['VDID'].'$*'.$inputOrg['VDName']
     */
    function reportVDTaskSeedReborn($input)
    {        
        $haveTask = false;
        $cancelTask = false;
        $rtnCode = 99;
        $logCode = '05301606';
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }              
        $this->processReportData($input,$data);        
        // var_dump($data);
        if($data['ErrCode'] == 0){  
            
            // var_dump('======2======');        
            $this->sqlListVDInfoByID($data['VDID'],$outputInfo);
            $this->sqlListUserVDofSeedIgnoreState($data['VDID'],$outputUsers);
            // var_dump($outputUsers);
            if(is_null($outputUsers)){
                $rtnCode = 1;
                goto rtnFinal;
            }                      
            connectDB::$dbh->beginTransaction();
            $data['ConnectIP'] = $this->getConnectIP();
            $rtnCode = $this->refreshVDInfo($data,false);
            if($rtnCode != 0)
                goto rtnRollback;
            foreach ($outputUsers as $user) {
                $user['ConnectIP'] = $this->getConnectIP();
                $user['CephID'] = $data['CephID'];
                $user['DomainID'] = $data['DomainID'];
                if(isset($outputInfo['ViewerProtocol'])){
                    $user['ViewerProtocol'] = $outputInfo['ViewerProtocol'];
                    $this->sqlUpdateVDViewerProtocol($user);
                }
                $rtnCode = $this->refreshVDInfo($user);
                if($rtnCode != 0)
                    goto rtnRollback;                
            }   
            connectDB::$dbh->commit();         
            $rtnCode = 0;
            $logCode = '05101502';
            goto rtnFinal;
        }
        else{            
            if($data['ErrCode'] == 210){
                $haveTask = true;
                $cancelTask = true;
                $logCode = '05101503';
            }
            // var_dump($data);
            if(!$this->sqlUpdateUserVDsofSeedStatus($data['VDID'],0)){
                $rtnCode = 1;
                goto rtnFinal;    
            }
            $rtnCode =0;
            goto rtnFinal;
        }
        rtnRollback:
            connectDB::$dbh->rollBack();
            goto rtnFinal;
        rtnFinal:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertSeedRebornLog($logCode,$data['VDName'],$data['DomainName'],$data['DomainID'],$appendErr,$data['Type'],$haveTask,$cancelTask,$data['ErrCode']);
            exit($rtnCode);
    }

    function recoveryUserVD($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }             
        $domainC = new DomainAction();
        $output_domain = null;
        $domainC->sqlListDomainByID($input['DomainID'], $output_domain);
        $output_user = null;
        $this->sqlListUserVD($input['VDID'],$output_user);        
        if(is_null($output_user)){
            if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>'05301C01','Riser'=>'admin','Message'=>"Failed to recovery user vd task(Failed to list user vd)"),$lastID)){
                LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            }
            return CPAPIStatus::NotFound;
        }
        $output_seed = null;
        $this->sqlListSeedVD($output_user['SeedID'], $output_seed);        
        if(is_null($output_seed)){
            if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>'05301C02','Riser'=>'admin','Message'=>"Failed to recovery user vd(Name:".$output_user['VDName'].") task(Failed to list user vd)"),$lastID)){
                LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            }
            return CPAPIStatus::NotFound;
        }
        connectDB::$dbh->beginTransaction();        
        if(!$this->sqlDeleteVDDisk($output_user['VDID'])){
            connectDB::$dbh->rollBack();      
            if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>'05301C04','Riser'=>'admin','Message'=>"Failed to recovery user vd(Name:".$output_user['VDName'].") task(Failed to update vd status)"),$lastID)){
                LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            }
            $this->insertSeedRecoveryUserVDFailTaskShell($output_seed, $output_user);
        }
        $output_user['State'] = 30;
        $output_user['RDPFirst'] = 2;
        if(!$this->sqlUpdateVDRDPFirst($output_user)){
            connectDB::$dbh->rollBack();      
            if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>'05301C04','Riser'=>'admin','Message'=>"Failed to recovery user vd(Name:".$output_user['VDName'].") task(Failed to update vd rdp)"),$lastID)){
                LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            }
            $this->insertSeedRecoveryUserVDFailTaskShell($output_seed, $output_user);
        }
        else{
            if(!$this->sqlUpdateVDStatusNoTime($output_user)){
                connectDB::$dbh->rollBack();      
                if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>'05301C04','Riser'=>'admin','Message'=>"Failed to recovery user vd(Name:".$output_user['VDName'].") task(Failed to update vd status)"),$lastID)){
                    LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
                }
                $this->insertSeedRecoveryUserVDFailTaskShell($output_seed, $output_user);
            }
            else{              
                $output_user['CephID'] = $input['CephID'];
                $output_user['DomainID'] = $input['DomainID'];
                $output_user['DomainName'] = $output_domain['Name'];
                $output_user['ConnectIP'] = $input['ConnectIP'];
                if($this->seedRecoveryUserVDShell($output_seed, $output_user) == 0){
                    connectDB::$dbh->commit();
                    if(LogAction::createLog(array('Type'=>5,'Level'=>1,'Code'=>'05101B01','Riser'=>'admin','Message'=>"Recovery User VD(Name:".$output_user['VDName'].") task success"),$lastID)){
                        LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
                    }
                }
                else{
                    if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>'05301C05','Riser'=>'admin','Message'=>"Failed to recovery user vd(Name:".$output_user['VDName'].") task"),$lastID)){
                        LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
                    }
                    connectDB::$dbh->rollBack();
                    $this->insertSeedRecoveryUserVDFailTaskShell($output_seed, $output_user);
                }
            }       
        }       
    }
    
    function insertSeedRecoveryUserVDFailTaskShell($input_seed,$input_user)
    {
        $cmd = $this->cmdVDTaskErrTask;
        $cmd = str_replace ( 'ip' , $input_seed['ConnectIP'], $cmd);
        $cmd .= '"'.base64_encode('recovery$*'.$input_user['CephID'].'$*'.$input_user['VDID'].'$*'.$input_user['VDName'].'$*'.$input_user['DomainID'].'$*'.$input_user['DomainName'].'$*'.$input_seed['VDID'].'$*'.$input_seed['VDName']).'" ';        
        $cmd .= 'seedRecovery';        
        exec($cmd,$output,$rtn); 
    }

    function seedRecoveryUserVDShell($input_seed,$input_user)
    {
        $rtn = -2;
        if(!is_null($input_user['ConnectIP'])){
            $cmd = $this->cmdVDTaskSeedRecovery;
            $cmd = str_replace ( 'ip' , $input_user['ConnectIP'], $cmd);            
            $cmd .= '"'.base64_encode('recovery$*'.$input_user['CephID'].'$*'.$input_user['VDID'].'$*'.$input_user['VDName'].'$*'.$input_user['DomainID'].'$*'.$input_user['DomainName'].'$*'.$input_seed['VDID'].'$*'.$input_seed['VDName']).'" ';         
            // $cmd .= '"'.base64_encode("$type$*".$input_seed['Name'].'$*'.$input_user['Name']).'" ';
            $cmd .= '"'.$input_seed['VDID'].'" ';                 
            $cmd .= '"'.$input_user['VDID'].'" '; 
            $cmd .= '"'.$input_user['DomainID'].'" ';                        
            // var_dump($cmd);
            exec($cmd,$output,$rtn);                               
            // var_dump($rtn);
        }           
        return $rtn;
    }

    function reportVDTaskSeedRecovery($input)
    {
        $haveTask = false;
        $cancelTask = false;
        $rtnCode = 99;
        $logCode = '05301C06';
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }              
        $this->processReportData($input,$data);
        // var_dump('======2======');        
        if($data['ErrCode'] == 0){
            $data['ConnectIP'] = $this->getConnectIP();
            $this->sqlListVDInfoByID($data['OrgVDID'],$outputInfo);
            if(isset($outputInfo['ViewerProtocol'])){
                $data['ViewerProtocol'] = $outputInfo['ViewerProtocol'];
                $this->sqlUpdateVDViewerProtocol($data);
            }
            connectDB::$dbh->beginTransaction();
            $rtnCode = $this->refreshVDInfo($data);
            if($rtnCode != 0)
                goto rtnRollback;
            connectDB::$dbh->commit();
            $rtnCode = 0;
            $logCode = '05101B02';
            goto rtnFinal;
        }
        else{            
            if($data['ErrCode'] == 210){
                $haveTask = true;
                $cancelTask = true;
                $logCode = '05101B03';
            }            
            $data['State'] = 0;
            if(!$this->sqlUpdateVDStatusNoTime($data)){
                $rtnCode = 2;
                goto rtnFinal;
            }
            $rtnCode =0;
            goto rtnFinal;
        }
        rtnRollback:
            connectDB::$Dbh->rollBack();
            goto rtnFinal;
        rtnFinal:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertSeedRebornLog($logCode,$data['VDName'],$data['DomainName'],$data['DomainID'],$appendErr,$data['Type'],$haveTask,$cancelTask,$data['ErrCode']);
            exit($rtnCode);
    }

    function insertSeedRebornLog($code,$vdName,$domainName,$domainID,$appendErr,$type,$haveTask=true,$cancelTask = false,$errCode=NULL)
    {
        $logType = 5;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $taskStr = "";
        if($haveTask)
            $taskStr = " task";
        if($type == $this->taskTypeEnum['distribute'])
            $taskHead = 'Reborn';
        else if($type == $this->taskTypeEnum['renew'])
            $taskHead = 'Renew';
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);  
        if(is_null($appendErr)){
            // var_dump($type);
            if($type == $this->taskTypeEnum['distribute'] || $type == $this->taskTypeEnum['renew']){                
                if($cancelTask)
                    $head = "Cancel $taskHead User VDs From Seed";
                else
                    $head = "$taskHead User VDs From Seed";
            }
            else{
                if($cancelTask)
                    $head = 'Cancel Recovery User VD';
                else
                    $head = 'Recovery User VD';
            }
            if($cancelTask)
                $message = "$head$appendVDDomain$taskStr";
            else
                $message = "$head$appendVDDomain$taskStr Success";
        }
        else{
            if($type == $this->taskTypeEnum['distribute'] || $type == $this->taskTypeEnum['renew']){                
                $head = "Failed to $taskHead User VDs From Seed";
            }
            else
                $head = 'Failed to Recovery User VD';
            $message = "$head$appendVDDomain$taskStr$appendErr";
            if(isset($errCode)){
                $rtnMsg = $this->changeModifyReturnCode($errCode);
                $message .= "($rtnMsg)";
            }
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }
    
    function refreshVDInfo($data,$isUpdateNIC=true)
    {
        if($this->listVDWithSSShell($data,$output) != 0){
            $rtnCode = 1;
            goto rtnFinal;
        }   
        // var_dump($output);
        $data['State'] = 0;  
        $data['CPUType'] = $output['cpuType'] == 'fv' ? 0 : 1;
        $data['CPU']=$output['cpuLimit'];
        $data['CpuCt']=$output['cpuCt'];
        $data['RAM']=$output['maxMem']/1024;
        if(!$isUpdateNIC){            
            foreach ($output['nic'] as &$value) {
                $value['mac'] = '';
            }
        }
        $data['NIC'] = json_encode($output['nic']);
        $suspend = ((int)$output['vdSuspend']) == 1 ? true : false;
        $data['Suspend']=$suspend;        
        $data['USBRedirCt']=$output['usbRedir'];
        $data['UEFI']=$output['bios'] == 'uefi' ? true : false;
        $data['VGACount']=(int)$output['vgaCt'];
        $data['QXL']=$output['qxlCt'] == '0' ? false : true;
        $cdrom = '';
        if(!is_null($output['cdrom']))
            $cdrom = $output['cdrom'];
        $data['CDRom']=$cdrom;
        $data['USBType'] =$output['usbType'];
        $sound = $output['soundModel'] == 'ich6' ? 1 : 2;
        $data['Sound']=$sound;   
        if(!$this->sqlDeleteVDDisk($data['VDID'])){
            $rtnCode = 6;
            goto rtnFinal;
        }         
        if(!$this->sqlUpdateVDStatusNoTime($data)){
            $rtnCode = 2;
            goto rtnFinal;
        }
        if(!$this->sqlUpdateVD($data,false)){
            $rtnCode = 3;
            goto rtnFinal;
        }
        // if($isUpdateNIC){
            if($this->vdUpdateNIC($data)!=0){
                $rtnCode = 4;
                goto rtnFinal;
            }
        // }
        foreach ($output['disk'] as $value) {
            // var_dump($value);
            $data['DiskName'] = $value['diskName'];
            $data['Disk'] = $value['totalSize']/(1024*1024*1024);
            $data['DiskCache'] = $value['hdCache'] == 'writethrough' ? 0 : 1;
            switch ($value['hdBus']) {
                case 'ide':
                    $data['DiskType'] = 0;
                    break;
                case 'scsi':
                    $data['DiskType'] = 1;
                    break;
                case 'virtio':
                    $data['DiskType'] = 2;
                    break;
                default:
                    $data['DiskType'] = 0;
                    break;
            }
            $this->sqlListVolumeByVolumeName($value['diskVolumeId'],$outputVolume);
            $data['VolumeID'] = $outputVolume['VolumeID'];
            if(!$this->sqlInsertVDDisk($data)){
                $rtnCode = 5;
                goto rtnFinal;
            }
        } 
        $rtnCode = 0;
        goto rtnFinal;
        rtnFinal:
            return $rtnCode;
    }

    function sqlListSeedVD($id,&$output)
    {
        $SQLList = <<<SQL
            SELECT t2.nameVD,t2.idVD,t2.idVdNumber FROM tbvdseedset t1
                INNER JOIN tbVDImageBaseSet t2
                ON t1.idVD=t2.idVD
                WHERE t1.idVD =:idVD
SQL;
        $sth = connectDB::$dbh->prepare($SQLList);
        $sth->bindValue(':idVD', $id, PDO::PARAM_STR);          
        if($sth->execute()){                                   
            while( $row = $sth->fetch() ) 
            {                                 
                $output = array('VDID'=>$row['idVD'],'VDName'=>$row['nameVD'],'VDNumber'=>(int)$row['idVdNumber']);
            }                            
        }
    }
    
    function sqlListUserVDofSeed($id,&$output)
    {
        $SQLList = <<<SQL
            SELECT t2.nameVD,t2.idVD,t2.idVdNumber FROM tbvdimageset t1
                INNER JOIN tbVDImageBaseSet t2
                ON t1.idVD=t2.idVD
                WHERE t1.idSeed =:idSeed AND t2.stateVD <> 30
SQL;
        $sth = connectDB::$dbh->prepare($SQLList);
        $sth->bindValue(':idSeed', $id, PDO::PARAM_STR);          
        if($sth->execute()){      
            $output = array();
            while( $row = $sth->fetch() ) 
            {                                 
                $output[] = array('VDID'=>$row['idVD'],'VDName'=>$row['nameVD'],'VDNumber'=>(int)$row['idVdNumber']);
            }                            
        }
    }

    function sqlListUserVDofSeedIgnoreState($id,&$output)
    {
        $SQLList = <<<SQL
            SELECT t2.nameVD,t2.idVD,t2.idVdNumber,t3.viewerProtocol
            FROM tbvdimageset t1 
            INNER JOIN tbVDImageBaseInfoSet t3
            ON t1.idVD=t3.idVD
            INNER JOIN tbVDImageBaseSet t2 
            ON t1.idVD=t2.idVD 
            WHERE t1.idSeed =:idSeed
SQL;
        $sth = connectDB::$dbh->prepare($SQLList);
        $sth->bindValue(':idSeed', $id, PDO::PARAM_STR);          
        if($sth->execute()){      
            $output = array();
            while( $row = $sth->fetch() ) 
            {                                 
                $output[] = array('VDID'=>$row['idVD'],'VDName'=>$row['nameVD'],'VDNumber'=>(int)$row['idVdNumber'],'ViewerProtocol'=>$row['viewerProtocol']);
            }                            
        }
    }
    
    
    function sqlListUserVD($id,&$output)
    {
        $SQLList = <<<SQL
            SELECT t1.idSeed,t2.nameVD,t2.idVD,t2.idVdNumber FROM tbvdimageset t1
                INNER JOIN tbVDImageBaseSet t2
                ON t1.idVD=t2.idVD
                WHERE t1.idVD =:idVD
SQL;
        $sth = connectDB::$dbh->prepare($SQLList);
        $sth->bindValue(':idVD', $id, PDO::PARAM_STR);          
        if($sth->execute()){                                   
            while( $row = $sth->fetch() ) 
            {                                 
                $output = array('SeedID'=>$row['idSeed'],'VDID'=>$row['idVD'],'VDName'=>$row['nameVD'],'VDNumber'=>(int)$row['idVdNumber']);
            }                            
        }
    }
    
    
    function overwriteSeedVD($input)
    {
        $log_code = "";
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }    
        $domainC = new DomainAction();
        $output_domain = null;
        $domainC->sqlListDomainByID($input['DomainID'], $output_domain);
        $output_org = null;
        // var_dump($input);
        $this->sqlListOrgVD($input['VDID'], $output_org);        
        if(is_null($output_org)){
            if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>'05301801','Riser'=>'admin','Message'=>"Failed to overwrite seed vd from original task(Failed to list virtual host)"),$lastID)){
                LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            }
            return CPAPIStatus::NotFound;        
        }
        $output_seed = null;
        $this->sqlListSeedVD($input['SeedID'], $output_seed);
        if(is_null($output_seed)){
            if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>'05301803','Riser'=>'admin','Message'=>"Failed to overwrite seed vd from original(Name:".$output_org['VDName'].",Domain:".$output_domain['Name'].") task(Failed to list seed vd)"),$lastID)){
                LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            }
            return CPAPIStatus::NotFound;
        }                 

        $output_seed['VDID'] = $input['SeedID'];        
        $output_seed['DomainID']=$input['DomainID'];
        $output_org['DomainID']=$input['DomainID'];
        connectDB::$dbh->beginTransaction();
        $output_seed['State'] = 31;
        // var_dump($output_seed);
        if(!$this->sqlUpdateVDStatusNoTime($output_seed)){
            $log_code = "05301807";
            goto fail;                        
        }        
        // if(isset($input['Description'])){
        //     $output_seed['Description'] = $input['Description'];
        //     if(!$this->sqlUpdateVDDescription($output_seed)){
        //         $log_code = "05301807";
        //         goto fail;                        
        //     }
        // }        
        // if(!$this->sqlDeleteVDDisk($output_seed['VDID'])){
        //     $log_code = "05301807";
        //     goto fail;            
        // }
        // var_dump(3);
        $output_seed['ConnectIP'] = $input['ConnectIP'];
        $output_seed['CephID'] = $input['CephID'];
        if($this->overwriteSeedVDShell($output_org, $output_seed) == 0){
            connectDB::$dbh->commit();  
            if(LogAction::createLog(array('Type'=>5,'Level'=>1,'Code'=>'05101701','Riser'=>'admin','Message'=>"Virtual Host(Name:".$output_org['VDName'].") overwrite seed vd(Name:".$output_seed['VDName'].") task Success"),$lastID)){
                LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            }
            return CPAPIStatus::RecreateSeedSuccess;
        }
        else{
            $log_code = "05301808";
            goto fail;
        }
        fail:
            connectDB::$dbh->rollBack();
            switch ($log_code){
                case '05301807':
                    if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>$log_code,'Riser'=>'admin','Message'=>"Failed to overwrite seed vd(Name:".$output_seed['VDName'].") from original(Name:".$output_org['VDName'].") task(Failed to update vd status)"),$lastID)){
                        LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
                    }
                    break;
                case '05301808':
                    if(LogAction::createLog(array('Type'=>5,'Level'=>3,'Code'=>$log_code,'Riser'=>'admin','Message'=>"Failed to overwrite seed vd(Name:".$output_seed['VDName'].") from original(Name:".$output_org['VDName'].") task"),$lastID)){
                        LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
                    }
                    break;
            }
            $this->insertOverwriteSeedVDFailTaskShell($output_org, $output_seed);
            return CPAPIStatus::RecreateSeedFail;
    }
    
    /*
        reportMetaData errorCode addingTime allDisk(base64) allNIC(base64)
        MetaData : seed$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$inputOrg['VDID'].'$*'.$inputOrg['VDName']
     */
    function reportVDTaskSeedOverwrite($input)
    {          
        $haveTask = false;    
        $cancelTask = false;
        $logCode = '0530040C';
        $rtnCode = 99;
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFinal;
        }                    
        $this->processReportData($input,$data);              
        if($data['ErrCode'] == 0){     
            // var_dump($data);
            $data['State'] = 0;
            $data['ConnectIP'] = $this->getConnectIP();
            connectDB::$dbh->beginTransaction();
            $this->sqlListVDInfoByID($data['OrgVDID'],$outputInfo);
            if(isset($outputInfo['ViewerProtocol'])){
                $data['ViewerProtocol'] = $outputInfo['ViewerProtocol'];
                $this->sqlUpdateVDViewerProtocol($data);
            }
            $rtnCode = $this->refreshVDInfo($data,false);
            if($rtnCode != 0)
                goto rtnRollback;
            connectDB::$dbh->commit();
            $rtnCode = 0;
            $logCode = '05100302';
            goto rtnFinal;
        }
        else{
            if($data['ErrCode'] == 210){
                $haveTask = true;
                $cancelTask = true;
                $logCode = '05100303';
            }
            // $responsecode = $this->deleteVD($data,false);
            $data['State'] = 0;
            if($this->sqlUpdateVDStatusNoTime($data))
                $rtnCode = 0;
            goto rtnFinal;    
        }
        rtnRollback:
            connectDB::$dbh->rollBack();
            goto rtnFinal;
        rtnFinal:
            $appendErr = $this->logVDAppendErr($logCode);
            $this->insertOverwriteSeedVDLog($logCode,$data['VDName'],$data['DomainName'],$data['DomainID'],$appendErr,$haveTask,$cancelTask,$data['ErrCode']);
            // var_dump($rtnCode);
            exit($rtnCode);
    }

    
    function sqlListOrgVD($id,&$output)
    {
        $SQLList = <<<SQL
            SELECT t1.idVD,t1.nameVD,t1.idVdNumber,t3.viewerProtocol
            FROM tbVDImageBaseSet as t1
            INNER JOIN tbVDImageBaseInfoSet as t3
            on t1.idVD = t3.idVD
            INNER JOIN tbvdimageorgset as t2
            ON t1.idVD=t2.idVD
            WHERE t1.idVD =:idVD
SQL;
        $sth = connectDB::$dbh->prepare($SQLList);
        $sth->bindValue(':idVD', $id, PDO::PARAM_STR);          
        if($sth->execute()){                                   
            while( $row = $sth->fetch() ) 
            {                                 
                $output = array('VDID'=>$row['idVD'],'VDName'=>$row['nameVD'],'VDNumber'=>(int)$row['idVdNumber'],'ViewerProtocol'=>$row['ViewerProtocol']);
            }                            
        }
    }
    
    function sqlUpdateUserVDsofSeedStatus($idSeed,$status)
    {
        $result = false;
        $SQUpdate = <<<SQL
        update tbvdimagebaseset set stateVD=:stateVD WHERE idVD in (SELECT idVD FROM tbvdimageset WHERE idSeed=:idSeed);
SQL;
        $sth = connectDB::$dbh->prepare($SQUpdate);
        $sth->bindValue(':idSeed', $idSeed, PDO::PARAM_STR);     
        $sth->bindValue(':stateVD', $status, PDO::PARAM_INT);  
        if($sth->execute()){                                                                   
            $result = true;
        }
        return $result;
    }

    function sql_list_vdisk_by_vdname($name,&$output)
    {
         $SQLList = <<<SQL
            SELECT idVDisk,sizeVDisk FROM tbVDiskSet WHERE nameVDisk =:nameVDisk
SQL;
        $sth = connectDB::$dbh->prepare($SQLList);
        $sth->bindValue(':nameVDisk', $name, PDO::PARAM_STR);          
        if($sth->execute()){                                   
            while( $row = $sth->fetch() ) 
            {                                 
                $output = array('DiskID'=>(int)$row['idVDisk'],'Size'=>(int)$row['sizeVDisk'],'VDNumber'=>(int)$row['idVdNumber']);
            }                            
        }
    }
    
    function sql_check_seed($id)
    {
        $rtn = true;
        $SQLList = <<<SQL
            SELECT count(idVD) as count FROM tbvdseedset WHERE idVD =:idVD
SQL;
        $sth = connectDB::$dbh->prepare($SQLList);
        $sth->bindValue(':idVD', $id, PDO::PARAM_STR);          
        if($sth->execute()){                                   
            while( $row = $sth->fetch() ) 
            {                                 
                $output = (int)$row['count'];
                // var_dump($output);
                if($output == 0)
                    $rtn = false;
            }                            
        }        
        return $rtn;
    }
    
    function updateVDClientID($id,$client_id)
    {       
        if(!$this->connectDB()){
            return false;
        }     
        $this->sqlUpdateVDClientID($id, $client_id);
    }
    
    function sqlUpdateVDClientID($id,$client_id)
    {
        $result = false;
        $SQLUpdate = <<<SQL
                Update tbVDImageBaseSet SET idClient=:idClient WHERE idVD = :idVD
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($SQLUpdate);                        
            $sth->bindValue(':idClient', $client_id, PDO::PARAM_STR);           
            $sth->bindValue(':idVD', $id, PDO::PARAM_STR);  
            if($sth->execute())
            {                                
                $result = true;
            }                        
        }
        catch (Exception $e){                             
        }     
        return $result;
    }

    function listVolumeVDsUserProfile($input,&$output)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }  
        $this->sqlListVolumeVDs($input,$outputVDs);
        $this->sqlListVolumeUserporfiles($input,$outputProfiles);
        // var_dump($outputProfiles);
        $outputVDs['UserProfile'] = $outputProfiles;
        // var_dump($outputVDs);
        $output = $outputVDs;
    }

    function sqlListVolumeVDs($input,&$output)
    {
        $sqlListOrg = <<<SQL
            SELECT t1.*,t3.idVD,t3.nameVD,t4.idUser,t4.nameUser FROM tbVDiskSet as t1  
            INNER JOIN tbvdimageorgset t2 ON t1.idVD = t2.idVD
            INNER JOIN tbvdimagebaseset t3 ON t2.idVD = t3.idVD
            INNER JOIN tbuserbaseset t4 ON t2.idUser = t4.idUser
            WHERE t1.idVolume =:idVolume AND t1.stateVDisk = 0
SQL;
        $sqlListSeed = <<<SQL
            SELECT t1.*,t3.idVD,t3.nameVD FROM tbVDiskSet as t1  
            INNER JOIN tbvdseedset t2 ON t1.idVD = t2.idVD
            INNER JOIN tbvdimagebaseset t3 ON t2.idVD = t3.idVD            
            WHERE t1.idVolume =:idVolume AND t1.stateVDisk = 0
SQL;
        $sqlListUser = <<<SQL
            SELECT t1.*,t3.idVD,t3.nameVD,t4.idUser,t4.nameUser FROM tbVDiskSet as t1  
            INNER JOIN tbvdimageset t2 ON t1.idVD = t2.idVD
            INNER JOIN tbvdimagebaseset t3 ON t2.idVD = t3.idVD
            INNER JOIN tbuserbaseset t4 ON t2.idUser = t4.idUser
            WHERE t1.idVolume =:idVolume AND t1.stateVDisk = 0
SQL;
        $sth = connectDB::$dbh->prepare($sqlListOrg);
        $sth->bindValue(':idVolume', $input['VolumeID'], PDO::PARAM_STR);          
        $output = array();
        $output['Org'] = array();
        $output['Seed'] = array();
        $output['User'] = array();
        if($sth->execute()){                                         
            while( $row = $sth->fetch() ) 
            {                            
                $pos = strpos($row['nameVDisk'], 'ud');
                if ($pos === false) {     
                    $output['Org'][] = array('UserID'=>(int)$row['idUser'],"Username"=>$row['nameUser'],'DiskID'=>(int)$row['idVDisk'],'DiskName'=>$row['nameVDisk'],'Size'=>(int)$row['sizeVDisk'],'VDID'=>$row['idVD']);
                }
            }                            
        }
        $sth = connectDB::$dbh->prepare($sqlListSeed);
        $sth->bindValue(':idVolume', $input['VolumeID'], PDO::PARAM_STR);
        if($sth->execute()){                                         
            while( $row = $sth->fetch() ) 
            {                                 
                $output['Seed'][] = array('DiskID'=>(int)$row['idVDisk'],'DiskName'=>$row['nameVDisk'],'Size'=>(int)$row['sizeVDisk'],'VDID'=>$row['idVD']);
            }                            
        }
        $sth = connectDB::$dbh->prepare($sqlListUser);
        $sth->bindValue(':idVolume', $input['VolumeID'], PDO::PARAM_STR);
        if($sth->execute()){                                         
            while( $row = $sth->fetch() ) 
            {                    
                $pos = strpos($row['nameVDisk'], 'ud');
                if ($pos === false) {                
                    $output['User'][] = array('UserID'=>(int)$row['idUser'],"Username"=>$row['nameUser'],'DiskID'=>(int)$row['idVDisk'],'DiskName'=>$row['nameVDisk'],'Size'=>(int)$row['sizeVDisk'],'VDID'=>$row['idVD']);
                }
            }                            
        }
    }

    function sqlListVolumeUserporfiles($input,&$output)
    {
        $sqlListUserprofile = <<<SQL
            SELECT t1.*,t3.idUser,t3.nameUser FROM tbVDiskSet as t1  
            INNER JOIN tbuserdiskset t2 ON t1.idVDisk = t2.idVDisk
            INNER JOIN tbuserbaseset t3 ON t2.idUser = t3.idUser
            WHERE t1.idVolume =:idVolume AND t1.stateVDisk = 0
SQL;
        $sth = connectDB::$dbh->prepare($sqlListUserprofile);
        $sth->bindValue(':idVolume', $input['VolumeID'], PDO::PARAM_STR);          
        $output = array();       
        if($sth->execute()){                                         
            while( $row = $sth->fetch() ) 
            {                                           
                    $output[] = array('UserID'=>(int)$row['idUser'],"Username"=>$row['nameUser'],'DiskID'=>(int)$row['idVDisk'],'DiskName'=>$row['nameVDisk'],'Size'=>(int)$row['sizeVDisk']);                
            }                            
        }
    }

    function sqlUpdateVDProtocol($input)
    {
        $result = false;
        $sqlUpdate = <<<SQL
        update tbVDImageBaseInfoSet set connectProtocol=:connectProtocol WHERE idVD=:idVD;
SQL;
        $sth = connectDB::$dbh->prepare($sqlUpdate);
        $sth->bindValue(':connectProtocol', $input['ConnectProtocol'], PDO::PARAM_INT);     
        $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_STR);  
        if($sth->execute()){                                                                   
            $result = true;
        }
        return $result;
    }

    function sqlUpdateVDRDPPWDTimeStamp($input)
    {
        $result =  false;
        $sqlUpdate = "Update tbVDImageBaseInfoSet SET pwdTimeStamp=UNIX_TIMESTAMP(NOW()) WHERE idVD = :idVD";
        try
        {                                     
            $sth = connectDB::$dbh->prepare($sqlUpdate);            
            $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_STR); 
            if($sth->execute())
                $result = true;                   
        }
        catch (Exception $e){                
        }    
        return $result;
    }

    function sqlUpdateVDRDPFirst($input)
    {
        $result =  false;
        $sqlUpdate = "Update tbVDImageBaseInfoSet SET isRDPFirst=:isRDPFirst WHERE idVD = :idVD";
        try
        {                                     
            $sth = connectDB::$dbh->prepare($sqlUpdate);            
            $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_STR); 
            $sth->bindValue(':isRDPFirst', $input['RDPFirst'], PDO::PARAM_INT); 
            if($sth->execute())
                $result = true;                   
        }
        catch (Exception $e){                
        }    
        return $result;
    }

    function sqlUpdateVDChangePWD($input)
    {
        $result =  false;
        $sqlUpdate = "Update tbVDImageBaseInfoSet SET isPWDChange=:isPWDChange WHERE idVD = :idVD";
        try
        {                                     
            $sth = connectDB::$dbh->prepare($sqlUpdate);            
            $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_STR); 
            $sth->bindValue(':isPWDChange', $input['PWDChange'], PDO::PARAM_BOOL); 
            if($sth->execute())
                $result = true;                   
        }
        catch (Exception $e){                
        }    
        return $result;
    }

    function sqlUpdateVDRDPAddress($input)
    {
        $result =  false;
        $sqlUpdate = "Update tbVDImageBaseInfoSet SET rdp='' WHERE idVD = :idVD";
        try
        {                                     
            $sth = connectDB::$dbh->prepare($sqlUpdate);            
            $sth->bindValue(':idVD', $input['VDID'], PDO::PARAM_STR); 
            if($sth->execute())
                $result = true;                   
        }
        catch (Exception $e){                
        }    
        return $result;
    }

    function sqlListVswitchByid($idVSwitch,&$output)
    {
        $output=NULL;
        $sqlList = <<<SQL
            SELECT * FROM tbvswitchset where idVSwitch=:idVSwitch;
SQL;
        $sth = connectDB::$dbh->prepare($sqlList);
        $sth->bindValue(':idVSwitch', $idVSwitch, PDO::PARAM_INT);        
        if($sth->execute()){                                         
            while( $row = $sth->fetch() ) 
            {                                           
                $output = array('VswitchID'=>(int)$row['idVSwitch'],'BridgeID'=>$row['idBridge'],"VLANID"=>$row['idVlan'],'DiskID'=>(int)$row['idVDisk'],'DiskName'=>$row['nameVDisk'],'Size'=>(int)$row['sizeVDisk']);                
            }                            
        }
    }

    function processVDErrorCode($code)
    {  
        $this->baseOutputResponse($code);
        switch ($code){            
            case CPAPIStatus::CreateVDofUserFail: 
            case CPAPIStatus::CreateVDofCephFail:
            case CPAPIStatus::ListVDofCephFail:
            case CPAPIStatus::DeleteVDFail:
            case CPAPIStatus::ListISOFail:
            case CPAPIStatus::ModifyISOFail:
            case CPAPIStatus::ListVDInfoFail:
            case CPAPIStatus::ListGFSFreeFail:
            case CPAPIStatus::ModifyVDFail:
            case CPAPIStatus::AssignFreeFail:
            case CPAPIStatus::AssignUserFail:
            case CPAPIStatus::CreateSeedFail:
            case CPAPIStatus::DisableVDFail:
            case CPAPIStatus::BornVDFail:
            case CPAPIStatus::CloneOrgFail:
            case CPAPIStatus::ExportVDFail:
            case CPAPIStatus::ListImportFail:
            case CPAPIStatus::ImportVDFail:
            case CPAPIStatus::RecreateSeedFail:
            case CPAPIStatus::ChangePWDFail:
            case CPAPIStatus::AddVDDiskFail:
            case CPAPIStatus::DeleteVDDiskFail:
            case CPAPIStatus::AddVDNICFail:
            case CPAPIStatus::DeleteVDNICFail:
            case CPAPIStatus::MigrateVDFail:
            case CPAPIStatus::ModifyVDDiskFail:
            case CPAPIStatus::MoveVDDiskFail:
            case CPAPIStatus::InsertRDPFail:
            case CPAPIStatus::ListRDPInfoFail:
            case CPAPIStatus::SetAcroGatewayIPFail:
            case CPAPIStatus::SetAcroGatewayDHCPFail:
            case CPAPIStatus::SetAcroGatewayForwardPortFail:
            case CPAPIStatus::SetAcroGatewayVswitchFail:
                http_response_code(400);
                break;               
        }        
    }
}
