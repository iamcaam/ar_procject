<?php
class GlusterAction extends BaseAPI
{	
	private $cmdClusterCreate = CmdRoot."ip cluster_create ";
    private $cmdClusterCreateCheck = CmdRoot."ip cluster_create_check ";
    private $cmdClusterRestore = CmdRoot."ip cluster_restore ";
	private $cmdClusterDel = CmdRoot."ip cluster_del ";
	private $cmdClusterCheckClusterCreated = CmdRoot."ip cluster_check_cluster_created ";
    private $cmdClusterVMSIPList = CmdRoot."ip cluster_vms_ip_list";
    private $cmdClusterShutdown = CmdRoot."ip node_shutdown ";
    private $cmdClusterShutdownAll = CmdRoot."ip cluster_shutdown ";
    private $cmdClusterReboot = CmdRoot."ip node_reboot ";
    private $cmdClusterAlarmList = CmdRoot."ip cluster_alarm_list";
    private $cmdClusterAlarmSet = CmdRoot."ip cluster_alarm_set ";
    private $cmdClusterAutosuspendList = CmdRoot."ip cluster_autosuspend_list";
    private $cmdClusterAutosuspendSet = CmdRoot."ip cluster_autosuspend_set ";
    private $cmdClusterSambaStatus = CmdRoot.'ip cluster_samba_status';
    private $cmdClusterSambaStart = CmdRoot.'ip cluster_samba_start';
    private $cmdClusterSambaStop = CmdRoot.'ip cluster_samba_stop';
    private $cmdClusterSambaPasswdSet = CmdRoot.'ip cluster_samba_passwd_set';
    private $cmdClusterCapacity = CmdRoot.'ip cluster_capacity';    
    private $cmdClusterDedup = CmdRoot.'ip cluster_dedup ';
    private $cmdClusterDedupList = CmdRoot.'ip cluster_dedup_list';
    private $cmdClusterDedupSet = CmdRoot.'ip cluster_dedup_set ';
    private $cmdClusterVolumeList = CmdRoot.'ip cluster_volume_list';
    private $cmdClusterVolumeAdd = CmdRoot.'ip cluster_volume_add ';
    private $cmdClusterGroupAddNew = CmdRoot.'ip cluster_group_add ';
    private $cmdVDTaskClusterGroupAddNew = CmdRoot.'ip vd_task_gluster_group_add ';
    private $cmdClusterGroupAddCheck = CmdRoot.'ip cluster_group_add_check ';
    private $cmdClusterGroupDel = CmdRoot.'ip cluster_group_del ';
    private $cmdClusterGroupMasterSet = CmdRoot.'ip cluster_group_master_set ';
    private $cmdClusterReplaceNode = CmdRoot.'ip cluster_replace_node ';
    private $cmdClusterReplaceNodeNew = CmdRoot.'ip cluster_replace_node_new ';
    private $cmdClusterReplaceNodeNewCheck = CmdRoot.'ip cluster_replace_node_new_check ';
    private $cmdClusterClearNode = CmdRoot.'ip cluster_clear_node';
    private $cmdClusterListState = CmdRoot.'ip cluster_list_state_for_web';
    private $cmdClusterRestoreProgress = CmdRoot.'ip cluster_restore_progress';
    private $cmdClusterNodeSetMaintainWeb = CmdRoot.'ip cluster_node_set_maintain_web ';
    private $cmdUPSSet = CmdRoot.'ip ups_set ';
    private $cmdUPSList = CmdRoot.'ip ups_info ';
    private $cmdClusterCheckRestoreAll = CmdRoot.'ip cluster_restore_all_check';
    private $cmdClusterRestoreAll = CmdRoot.'ip cluster_restore_all ';
    private $cmdClusterLocateGroup = CmdRoot.'ip cluster_locate_group ';
    private $cmdClusterLocateNode = CmdRoot.'ip cluster_locate_node ';
    private $cmdClusterVMSListReadyGroup = CmdRoot.'ip cluster_vms_list_ready_group';
    private $cmdClusterVMSSkipWaitGroupReady = CmdRoot.'ip1 cluster_vms_skip_wait_group_ready';
    private $cmdVDTaskGlusterVolumeAdd = CmdRoot.'ip vd_task_gluster_volume_add ';
    private $cmdClusterMailSSMTPSet = CmdRoot.'ip cluster_mail_ssmtp_set ';
    private $cmdClusterMailSSMTPList = CmdRoot.'ip cluster_mail_ssmtp_list';
    private $cmdClusterMailTest = CmdRoot.'ip cluster_mail_test';
    private $cmdClusterGroupMasterSetLocal = CmdRoot.'ip cluster_group_master_set_local';
    private $cmdVDTaskErrTask = CmdRoot."ip vd_task_err_task ";
    private $cmdClusterVMSIPListFromStorage = CmdRoot."ip1 cluster_vms_ip_list_from_storage";
    private $cmdClusterDNSList = CmdRoot."ip cluster_dns_list";
    private $cmdClusterDNSSet = CmdRoot."ip cluster_dns_set ";
    private $cmdClusterVMSIPSet = CmdRoot."ip cluster_vms_ip_set ";

    function processReportAddGlusterGroupData($input,&$output)
    {
        $metadata = base64_decode($input[0]);
        $errCode = $input[1];
        $creaeTime = date('Y-m-d H:i:s',$input[2]);                
        $metadataArr = explode('$*', $metadata);               
        $type = $this->taskTypeEnum[$metadataArr[0]];        
        // var_dump($metadataArr);    
        $output = array();     
        $output['Type'] = $type;
        $output['CephID'] = $metadataArr[1];
        $output['Node1ID'] = $metadataArr[2];
        $output['Node2ID'] = $metadataArr[3];
        $output['Node1Name'] = $metadataArr[4];
        $output['Node2Name'] = $metadataArr[5];
        $output['ConnectIP'] = $this->getConnectIP();
        $output['CreateTime'] = $creaeTime;
        $output['ErrCode'] = $errCode;
        // var_dump($output);
    }

    function processReportAddGlusterVolumeData($input,&$output)
    {
        $metadata = base64_decode($input[0]);
        $errCode = $input[1];
        $creaeTime = date('Y-m-d H:i:s',$input[2]);                
        $metadataArr = explode('$*', $metadata);               
        $type = $this->taskTypeEnum[$metadataArr[0]];        
        // var_dump($metadataArr);    
        $output = array();     
        $output['Type'] = $type;
        $output['CephID'] = $metadataArr[1];
        $output['PairID'] = $metadataArr[2];
        $output['RAIDID'] = $metadataArr[3];
        $output['Alias'] = $metadataArr[4];        
        $output['ConnectIP'] = $this->getConnectIP();
        $output['CreateTime'] = $creaeTime;
        $output['ErrCode'] = $errCode;
        // var_dump($output);
    }

    function logGlusterAppendErr($code)
    {
        $appendErr = null;
        switch ($code) {
            case '01301101':            
                $appendErr = '(Failed to list Hostname)';
                break;     
            case '01301102':          
                $appendErr = '(Failed to insert Node)';
                break;
            case '01301103':
                $appendErr = '(Failed to insert Vswitch)';
                break;
            case '01301104':
                $appendEr1 = '(Failed to insert Volume)';
                break;
            case '01301105':
                $appendErr = '(Failed to Insert Volume and Node mapping)';
                break;
            case '01301106':
            case '01301106-1':
            case '01301108':
            case '01301601':
                $appendErr = '';
                break;
        }
        return $appendErr;
    }  

  	function createGluster($input,&$rtn,&$errMsg)
	{
        $errMsg = '';
		// var_dump($input);
		set_time_limit(0);		
        $systemC = new SystemProcess();
        $nodeC = new NodeAction();
        $vswitchC = new vSwitchAction();
        $backupC = new BackupAction();
        $cmd = '';    
        if(!is_bool($input['ClearData'])){            
        	return CPAPIStatus::AddClusterFail;           
        }
       	if (filter_var($input['VMSIP'], FILTER_VALIDATE_IP) === false){
            $rtn = 1;
       		echo -1;
       		return CPAPIStatus::AddClusterFail;
       	}
       	if(filter_var($input['VMSMask'], FILTER_VALIDATE_IP) === false){
            $rtn = 2;
       		echo 0;
       		return CPAPIStatus::AddClusterFail;
       	}
       	// $cmd  .= $input['ClearData'] == true ? 1 : 0;
       	$cmd  .= $input['VMSIP'];
       	$cmd  .= ' '.$input['VMSMask'];        
        foreach ($input['Nodes'] as &$value) {
        	$input['ConnectIP'] = $value['CommunicateIP'];
        	$outputHostname = $systemC->listHostname($input,$rtn);	            
            // var_dump($rtn);        	
        	if($rtn != 0){
                $outputMsg = 'Failed to Get '.$input['ConnectIP'].' Hostname';
                $errMsg = base64_encode($outputMsg);
        		return CPAPIStatus::AddClusterFail;
        	}
        	$value['NodeName'] = $outputHostname['HostName'];        	
        	$cmd .= ' '.$value['NodeName'].' '.$value['CommunicateIP'];
        }       
        // var_dump($cmd);
        if(!$this->createGlusterCheckShell($cmd,$rtn,$outputMsg)){
            if(strlen($outputMsg) > 0)
                $errMsg = base64_encode($outputMsg);            
            return CPAPIStatus::AddClusterFail;                
        }
        if(!$this->createGlusterShell($cmd,$rtn,$outputMsg)){
            if(strlen($outputMsg) > 0)
                $errMsg = base64_encode($outputMsg);
            $this->deleteGlusterShell($input);
        	return CPAPIStatus::AddClusterFail;                
        }
        sleep(5);
        if(!$this->connectDB()){
        	// echo 0;
            $rtn = 3; 
        	$this->deleteGlusterShell();
            return CPAPIStatus::AddClusterFail;
        }       
        if(!$this->sqlGetUUID($outputUUID))
        {
            $rtn = 10; 
            $this->deleteGlusterShell();
            return CPAPIStatus::AddClusterFail;
        }
        connectDB::$dbh->beginTransaction();     
        if(!$this->sqlClearNode()){
        	// echo 1;
            $rtn = 4; 
        	goto failRollback;
        }          
        $nodeIDs = array();
      	// goto failRollback;
        $input['ConnectIP'] = $input['Nodes'][0]['IP'];         
        foreach ($input['Nodes'] as &$value) {
        	// var_dump($value);
            $value['ConnectIP'] = $input['ConnectIP'];         	
            $value['Role'] = 1;
            $value['PairID'] = 1;        
        	if(!$nodeC->initInsertNode($value,$tmpNodeID)){
        		// echo 2;
                $rtn = 5;
        		goto failRollback;
        	}        	                 
            $nodeIDs[] = $tmpNodeID;
        	// $nodeID = connectDB::$dbh->lastInsertId();
        	if(!isset($value['NodeID'])){
        		// echo 3;
                $rtn = 6; 
        		goto failRollback;
        	}
        	// $value['NodeID'] = $nodeID;
        	// echo 'insert vswitch';
            $value['ConnectIP'] = $value['CommunicateIP'];          
        	if(!$vswitchC->initInsertVswitchNew($value)){
        		// echo 4;
                $rtn = 7; 
        		goto failRollback;
        	}            
        }
        $backupC->sqlDeleteBackupSpace();        
        $this->listClusterVolumesShell($input,$outputVolumes);        
        $totalSize = $outputVolumes[0]['total']*1024*1024;
        $input['Size'] = $totalSize;
        $input['Name'] = 1;
        $input['Alias'] = 'Primary';
        $input['VolumeID'] = $outputUUID['UUID'];
        if(!$this->sqlInsertVolume($input)){
            $rtn = 8;
            goto failRollback;
        }
        foreach ($nodeIDs as $value) {            
            if(!$this->sqlInsertVolumeNodeMapping($outputUUID['UUID'],$value)){
                $rtn = 9;
                goto failRollback;      
            }  
        }  
        connectDB::$dbh->commit();
        return CPAPIStatus::AddClusterSuccess;
failRollback:                
			// echo 'rollback';			
            connectDB::$dbh->rollBack();
            $input['ConnectIP'] = "127.0.0.1";
            $this->deleteGlusterShell($input);
            return CPAPIStatus::AddClusterFail;      
	}		

    function createGlusterCheckShell($inputCmd,&$rtn,&$outputMsg)
    {
        $rtn = -99;
        $cmd = $this->cmdClusterCreateCheck;         
        $cmd = str_replace ( 'ip' , '127.0.0.1', $cmd);        
        $cmd .= $inputCmd;        
        exec($cmd,$outputArr,$rtn);        
        foreach ($outputArr as $value) {
            // var_dump($value);
            $outputMsg .= $value.PHP_EOL;
        }          
        // var_dump($cmd);
        // var_dump($rtn);
        if($rtn == 0){                         
            return true;      
        }            
        return false;
    }

	function createGlusterShell($inputCmd,&$rtn,&$outputMsg)
	{
        $rtn = -99;
		$cmd = $this->cmdClusterCreate;         
        $cmd = str_replace ( 'ip' , '127.0.0.1', $cmd);        
        $cmd .= $inputCmd;        
        exec($cmd,$outputArr,$rtn);        
        foreach ($outputArr as $value) {
            // var_dump($value);
            $outputMsg .= $value.PHP_EOL;
        }          
        // var_dump($cmd);
        // var_dump($rtn);
        if($rtn == 0){                         
            return true;      
        }            
        return false;
	}
    
    function checkRestore($input,&$output)
    {
        $rtn = -99;
        $this->ClusterVMSIPListFromStorageShell($input,$outputVMSInfo);        
        $cmd = $this->cmdClusterCheckRestoreAll;         
        $cmd = str_replace ( 'ip' , '127.0.0.1', $cmd);        
        exec($cmd,$outputArr,$rtn);        
        $appendStr = '';
        $output = array('CanRestore'=>false,'RestoreMsg'=>'','ErrCode'=>$rtn,'VMSIP'=>'',"VMSMask"=>'');
        if(isset($outputVMSInfo['ip'])){
            $output['VMSIP'] = $outputVMSInfo['ip'];
            $output['VMSMask'] = $outputVMSInfo['mask'];
        }
        foreach ($outputArr as $value) {
            // var_dump($value);
            $appendStr .= $value.PHP_EOL;
        }        
        if($rtn == 0)
            $output['CanRestore']= true;
        $output['RestoreMsg'] = base64_encode($appendStr);
    }

    function ClusterVMSIPListFromStorageShell($input,&$output)
    {
        $output = null;
        $cmd = $this->cmdClusterVMSIPListFromStorage;
        $cmd = str_replace ( 'ip1' , '127.0.0.1', $cmd);
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($rtn);
        // var_dump($outputArr);
        if($rtn == 0){
            $output = json_decode($outputArr[0],true);
        }    
    }

    function restoreGlusterAll($input)
    {
        if(isset($input['VMSIP'])){
            if (filter_var($input['VMSIP'], FILTER_VALIDATE_IP) === false){
                return CPAPIStatus::RestoreGlusterFail;
            }
            if(filter_var($input['VMSMask'], FILTER_VALIDATE_IP) === false){
                return CPAPIStatus::RestoreGlusterFail;
            }
        }
        $this->restoreGlusterAllShell($input);
        return CPAPIStatus::RestoreGlusterSuccess;
    }

    function restoreGlusterAllShell($input)
    {
        $rtn = -99;
        $cmd = $this->cmdClusterRestoreAll;         
        $cmd = str_replace ( 'ip' , '127.0.0.1', $cmd);
        if(isset($input['VMSIP']) && isset($input['VMSMask'])){
            $cmd .= '"'.$input['VMSIP'].'" ';
            $cmd .= '"'.$input['VMSMask'].'" ';
        }        
        $cmd .= ' >/dev/null 2>&1 &';
        $pipe = popen($cmd,"r");    
        pclose($pipe);        
        return true;        
    }  

    function restoreGluster($input,&$rtn)
    {
        $systemC = new SystemProcess();
        $nodeC = new NodeAction();
        $vswitchC = new vSwitchAction();
        $backupC = new BackupAction();
        // var_dump($input);
        set_time_limit(0);              
        $cmd = '';    
        if(!is_bool($input['ClearData'])){
            return CPAPIStatus::AddClusterFail;           
        }
        if (filter_var($input['VMSIP'], FILTER_VALIDATE_IP) === false){
            // echo -1;
            $rtn = 1;
            return CPAPIStatus::AddClusterFail;
        }
        if(filter_var($input['VMSMask'], FILTER_VALIDATE_IP) === false){
            // echo 0;
            $rtn = 2;
            return CPAPIStatus::AddClusterFail;
        }
        // $cmd  .= $input['ClearData'] == true ? 1 : 0;
        $cmd  .= $input['VMSIP'];
        $cmd  .= ' '.$input['VMSMask'];
        $orgConnectIP = $input['ConnectIP'];
        foreach ($input['Nodes'] as &$value) {
            $input['ConnectIP'] = $value['CommunicateIP'];
            $outputHostname = $systemC->listHostname($input,$rtn);              
            if($rtn != 0){
                $rtn = 3;
                return CPAPIStatus::AddClusterFail;
            }
            $value['NodeName'] = $outputHostname['HostName'];           
            $cmd .= ' '.$value['NodeName'].' '.$value['CommunicateIP'];
        }           
        $input['ConnectIP'] = $orgConnectIP;
        $inputJson = json_encode($input);       
        $cmd.=' '.base64_encode($inputJson);
        // var_dump($cmd);
        if(!$this->restoreGlusterShell($cmd,$rtn))
            return CPAPIStatus::AddClusterFail;   
        $rtn = $this->listGlusterStateShell($input,$outputGLuster);
        if($rtn!=0)
            return CPAPIStatus::AddClusterFail;       
        return CPAPIStatus::AddClusterSuccess;
    }           

    function restoreGlusterShell($inputCmd,&$rtn)
    {
        $rtn = -99;
        $cmd = $this->cmdClusterRestore;         
        $cmd = str_replace ( 'ip' , '127.0.0.1', $cmd);        
        $cmd .= $inputCmd;    
        $cmd .= ' >/dev/null 2>&1 &';
        $pipe = popen($cmd,"r");    
        pclose($pipe);        
        return true;
        // var_dump($cmd);
        // var_dump($rtn);
        // if($rtn == 0){                         
        //     return true;      
        // }            
        // return false;
    }    

    function reportClusterRestore($inputReport)
    {
        $systemC = new SystemProcess();
        $nodeC = new NodeAction();
        $vswitchC = new vSwitchAction();
        $backupC = new BackupAction();
        $inputMeta = $inputReport[0];
        $input = json_decode(base64_decode($inputMeta),true);
        if(!$this->connectDB()){
            // echo 0;
            $rtn = 4;
            $this->deleteGlusterShell();
            return CPAPIStatus::AddClusterFail;
        }       
        connectDB::$dbh->beginTransaction();     
        if(!$this->sqlClearNode()){
            // echo 1;
            $rtn = 5;
            goto failRollback;
        }          
        $nodeIDs = array();
        // goto failRollback;
        $input['ConnectIP'] = $input['Nodes'][0]['IP'];
        foreach ($input['Nodes'] as &$value) {
            // var_dump($value);
            $value['ConnectIP'] = $input['ConnectIP'];
            $value['Role'] = 1;
            $value['PairID'] = 1;
            if(!$nodeC->initInsertNode($value)){
                // echo 2;
                $rtn = 6;
                goto failRollback;
            }           
            // $nodeID = connectDB::$dbh->lastInsertId();
            if(!isset($value['NodeID'])){
                // echo 3;
                $rtn = 7;
                goto failRollback;
            }
            // $value['NodeID'] = $nodeID;
            // echo 'insert vswitch';
            $value['ConnectIP'] = $value['CommunicateIP'];
            if(!$vswitchC->initInsertVswitchNew($value)){
                // echo 4;
                $rtn = 8;
                goto failRollback;
            }      
            $nodeIDs[] = $value['NodeID'];      
        }
        $backupC->sqlDeleteBackupSpace();        
        $this->listClusterVolumesShell($input,$outputShellVolumes);
        $this->sqlListVolumes($outputVolumes);
        // var_dump($outputShellVolumes);
        // var_dump($outputVolumes);
        if(sizeof($outputVolumes) == 0)
        {
            $totalSize = $outputVolumes[0]['total']*1024*1024;
            $input['Size'] = $totalSize;
            $input['Name'] = 1;
            $input['Alias'] = 'Primary';
            if(!$this->sqlGetUUID($outputUUID))
            {
                $rtn = 10; 
                goto failRollback;
            }
            $input['VolumeID'] = $outputUUID['UUID'];
            if(!$this->sqlInsertVolume($input)){
                $rtn = 9;
                goto failRollback;
            }
            foreach ($nodeIDs as $value) {        
                if(!$this->sqlInsertVolumeNodeMapping($outputUUID['UUID'],$value)){
                    $rtn = 11;
                    goto failRollback;      
                }  
            }     
        }else{                               
            foreach ($outputShellVolumes as $value) {
                $haveSameVolume = false;
                if(strlen($value['state']) > 0){
                    // var_dump($value);
                    foreach ($outputVolumes as $value1) {
                        // var_dump($value1);
                        // var_dump($value);
                        if($value1['VolumeName'] == $value['raidId']){
                            // var_dump('Have Same');
                            $haveSameVolume = true;
                            foreach ($nodeIDs as $value3) {                                
                                if(!$this->sqlInsertVolumeNodeMapping($value1['VolumeID'],$value3)){
                                    $rtn = 12;
                                    goto failRollback;      
                                }  
                            }  
                            break;
                        }                        
                    }
                    if(!$haveSameVolume){
                        // var_dump('Not Have Same');
                        if(!$this->sqlGetUUID($outputUUID))
                        {
                            $rtn = 13; 
                            goto failRollback;
                        }
                        $totalSize = $value['total']*1024*1024;
                        $inputVolume['Size'] = $totalSize;
                        $inputVolume['Name'] = $value['raidId'];
                        $inputVolume['VolumeID'] = $outputUUID['UUID'];
                        if($value['raidId'] == 1)
                            $inputVolume['Alias'] = 'Primary';
                        else
                            $inputVolume['Alias'] = 'Additional Volume '.$value['raidId'];
                        if(!$this->sqlInsertVolume($inputVolume)){
                            $rtn = 14;
                            goto failRollback;
                        }
                        foreach ($nodeIDs as $value) {        
                            if(!$this->sqlInsertVolumeNodeMapping($outputUUID['UUID'],$value)){
                                $rtn = 15;
                                goto failRollback;      
                            }  
                        }           
                    }                                
                }
            }
            
        }
        
        connectDB::$dbh->commit();
        exit(0);
failRollback:                       
            connectDB::$dbh->rollBack();
            $this->deleteGlusterShell($input);
            exit($rtn);
    }

    function reportClusterRestoreAll($inputReport)
    {       
        $backupC = new BackupAction();     
        $nodeC = new NodeAction(); 
        if(!$this->connectDB()){            
            exit(4);            
        }   
        $nodeC->listStateAllShell(array('ConnectIP'=>'127.0.0.1'),$output);
        // var_dump($output);
        $nodeC->sqlListServers($outputSQL,true);
        // var_dump($outputSQL);
        foreach ($output['info'] as $value) {
            foreach ($outputSQL as $value1) {
                if($value['nodeName'] == $value1['CommunicateIP']){

                    unset($outputSQL[$value1['CommunicateIP']]);
                }
            }
        }
        // var_dump($outputSQL);
        foreach ($outputSQL as $delValue) {
            $this->sqlDeleteVDServerByidVDServer($delValue['NodeID']);
        }
        $backupC->sqlDeleteBackupSpace();        
        exit(0);
    }

    function reportClusterRestoreAllNew($inputReport)
    {       
        $backupC = new BackupAction();     
        $nodeC = new NodeAction(); 
        $systemC = new SystemProcess();
        $vswitchC = new vSwitchAction();
        if(!$this->connectDB()){            
            exit(4);            
        }   
        $nodeC->listStateAllShell(array('ConnectIP'=>'127.0.0.1'),$output);
        // var_dump($output);
        $nodeC->sqlListServers($outputSQL,true);
        // var_dump($outputSQL);
        foreach ($output['info'] as $value) {
            foreach ($outputSQL as $value1) {
                if($value['nodeName'] == $value1['CommunicateIP']){                    
                    $nodeHostName=$systemC->listHostname(array('ConnectIP'=>$value1['CommunicateIP']));
                    $nodeC->initUpdateNode(array('NodeID'=>$value1['NodeID'],'ConnectIP'=>'127.0.0.1','CommunicateIP'=>$value1['CommunicateIP']));
                    $nodeC->sqlUpdateNodeName(array('NodeID'=>$value1['NodeID'],'NodeName'=>$nodeHostName['HostName']));
                    connectDB::$dbh->beginTransaction();               
                    $this->sqlListAllVswitchsNameWithBrNameKey($outputVswitchs);
                    $this->sqlClearNodevSwitch($value1['NodeID']);                                     
                    if(!$vswitchC->initInsertVswitchNew(array('NodeID'=>$value1['NodeID'],'ConnectIP'=>$value1['CommunicateIP'],'CommunicateIP'=>$value1['CommunicateIP']),true,$outputVswitchs)){                        
                        connectDB::$dbh->rollback();
                        // var_dump('fail');
                    }      
                    else{
                        // var_dump('success');
                        connectDB::$dbh->commit();
                    }
                    unset($outputSQL[$value1['CommunicateIP']]);
                }
            }
        }
        // var_dump($outputSQL);
        foreach ($outputSQL as $delValue) {
            $this->sqlDeleteVDServerByidVDServer($delValue['NodeID']);
        }
        $backupC->sqlDeleteBackupSpace();        
        exit(0);
    }

    function clusterRestoreProgress($input,&$output)
    {
        $this->clusterRestoreProgressShell($input,$outputProgress);
        $output['TotalPair'] = 0;
        $output['CurrentPair'] = 0;
        $output['State'] = 0;
        $output['VolumeProgress'] = array();
        if(isset($outputProgress)){   
            $output['TotalPair'] = (int)substr($outputProgress['groups'], strlen($outputProgress['groups'])-1);
            $output['CurrentPair'] = (int)$outputProgress['currentGroup'];
            if($outputProgress['status'] == 'preparing'){
                $output['State'] = 0;
            }
            else if(preg_match('/^[0-9]$/',$outputProgress['status']) || $outputProgress['status'] == 'doneOk'){
                if($outputProgress['status'] == 'doneOk')
                    $output['State'] = 2;
                else
                    $output['State'] = 1;
                foreach ($outputProgress['raid'] as $value) {
                    if(strlen($value['totalSize']) > 0 && strlen($value['currentSize']) > 0){                    
                        $volumeState = 0;
                        if($value['currentSize'] > 0){                        
                            if($value['totalSize'] == $value['currentSize'])
                                $volumeState = 2;
                            else
                                $volumeState = 1;
                        }                    
                        $output['VolumeProgress'][] = array('ID'=>(int)$value['raidId'],'TotalByte'=>$value['totalSize'],'CurrentByte'=>$value['currentSize'],'State'=>$volumeState);              
                    }                
                }
            }
            else if($outputProgress['status'] == 'doneError'){
                $output['State'] = -1;
            }
        }
    }

    function clusterRestoreProgressShell($input,&$output)
    {
        $rtn = -99;
        $cmd = $this->cmdClusterRestoreProgress;         
        $cmd = str_replace ( 'ip' , '127.0.0.1', $cmd);    
        exec($cmd,$outputArr,$rtn);
        // var_dump($rtn);
        // var_dump($outputArr);
        if($rtn == 0 )
            $output=json_decode($outputArr[0],true);
        return $rtn;
    }

    function addGlusterNewGroup($input,&$errMsg)
    {
        $errMsg = '';
        $logCode = "";
        // var_dump($input);
        set_time_limit(0);      
        $systemC = new SystemProcess();
        $nodeC = new NodeAction();
        $vswitchC = new vSwitchAction();
        $backupC = new BackupAction();
        $sqlLock = 'LOCK TABLES tbvdserverset WRITE,tbvdservernicset WRITE,tbbondset WRITE,tbvswitchbondmappingset WRITE,tbvswitchset WRITE,tbvdserverinfoset WRITE,tbVolumeSet WRITE,tbVolumeVDServerMappingSet WRITE';
        $sqlUnLock = 'UNLOCK TABLES ';
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        // var_dump(1);        
        $sth=connectDB::$dbh->prepare($sqlLock);
        $sth->execute();
        if(!$nodeC->sqlListMaxPairID($maxPairID))
            return CPAPIStatus::DBConnectFail;
        $cmd = '';
        $cmdCheck = '';
        $cmd .= $maxPairID + 1;
        $cmd .= ' ';
        // var_dump('1-1');
        connectDB::$dbh->beginTransaction();
        foreach ($input['Nodes'] as &$value) {
            // var_dump($value);
            $input['ConnectIP'] = $value['CommunicateIP'];
            $outputHostname = $systemC->listHostname($input,$rtn);  
            // var_dump($rtn);            
            if($rtn != 0){
                $logCode = '01301101';
                $appendErr = $this->logGlusterAppendErr($logCode);
                $appendErr .= '(Failed to get from '.$value['CommunicateIP'].')';
                goto failRollback;
            }
            // var_dump($outputHostname);
            $value['ConnectIP'] = $value['CommunicateIP'];
            $value['NodeName'] = $outputHostname['HostName'];           
            $cmd .= $value['NodeName'].' '.$value['CommunicateIP'].' ';
            $cmdCheck .= $value['NodeName'].' '.$value['CommunicateIP'].' ';
            $value['Role'] = 1;
            $value['PairID'] = $maxPairID + 1;
            $value['State'] = 99;
            if(!$nodeC->initInsertNode($value,$tmpNodeID,true)){
                // echo 2;
                $rtn = 1;
                $logCode = '01301102';
                goto failRollback;
            }                            
            $nodeIDs[] = $tmpNodeID;            
            $nodeNames[] = $value['NodeName'];
            if(!isset($value['NodeID'])){                
                $rtn = 2; 
                $logCode = '01301102';
                goto failRollback;
            }            
            if(!$vswitchC->initInsertVswitchNew($value)){                
                $rtn = 3; 
                $logCode = '01301103';
                goto failRollback;
            }                        
        }
        $input['NodeIDs'] = $nodeIDs;
        $input['NodeNames'] = $nodeNames;
        // var_dump(2);
        $this->sqlListVolumes($outputVolumes);
        
        foreach ($input['RAIDs'] as $raid) {
            if($raid['RAIDID'] <= 0 || $raid['RAIDID'] > 7){
                $rtn = 4; 
                $logCode = '01301104';
                goto failRollback;
            }
            $haveVolume = false;            
            foreach ($outputVolumes as $value) {                
                if($value['VolumeName'] == $raid['RAIDID']){
                    $volumeID = $value['VolumeID'];
                    $haveVolume = true;
                    break;
                }                
            }
            if(!$haveVolume){
                if(!$this->sqlGetUUID($outputUUID))
                {            
                    $rtn = 4; 
                    $logCode = '01301104';
                    goto failRollback;
                }
                $volumeID = $outputUUID['UUID'];
                $addVolume = array();
                $addVolume['VolumeID'] = $volumeID;
                $addVolume['Name'] = $raid['RAIDID'];
                $addVolume['Size'] = 0;
                $addVolume['Alias'] = $raid['Alias'];
                // var_dump($addVolume);
                if(!$this->sqlInsertVolume($addVolume)){
                    $rtn = 5; 
                    $logCode = '01301104';
                    goto failRollback;
                }
            }
            foreach ($nodeIDs as $node) {      
                // var_dump($volumeID);      
                // var_dump($node);
                if(!$this->sqlInsertVolumeNodeMapping($volumeID,$node)){
                    $rtn = 6;
                    $logCode = '01301105';
                    goto failRollback;      
                }  
            }  
            $cmd .= $raid['RAIDID'].' ';
        }         
        // var_dump(3);              
        $input['ConnectIP'] = '127.0.0.1';
        $cmdCheck .= $maxPairID + 1;
        $rtn = $this->clusterGroupAddCheckShell($input,$cmdCheck,$outputMsg);
        if($rtn != 0){
            if(strlen($outputMsg) > 0)
                $errMsg = base64_encode($outputMsg);
            $errCode = $rtn;            
            $logCode = '01301106-1';
            goto failRollback;
        }
        $rtn = $this->addGlusterNewGroupShell($input,$cmd,$outputMsg);
        if($rtn != 0){
            if(strlen($outputMsg) > 0)
                $errMsg = base64_encode($outputMsg);
            $errCode = $rtn;            
            $logCode = '01301106';
            goto failRollback;
        }
        $this->listClusterVolumesShell($input,$outputVolumes);
        // var_dump($outputVolumes);
        foreach ($input['RAIDs'] as $raid) {
            $volumeIndex = (int)$raid['RAIDID'];
            $totalSize = $outputVolumes[$volumeIndex]['total']*1024*1024;
            $updateVolume['Size'] = $totalSize; 
            $updateVolume['Name'] = $raid['RAIDID'];       
            $this->sqlUpdateVolumeSize($updateVolume);
        }
        connectDB::$dbh->commit();
        $sth=connectDB::$dbh->prepare(sqlUnLock);
        $sth->execute();
        // var_dump('end');
        $logCode = '01101001';
        $appendErr = $this->logGlusterAppendErr($logCode);
        $this->insertAddGlusterNewGroupLog($logCode,$input,$appendErr,$errCode);
        return CPAPIStatus::AddClusterGroupSuccess;
        failRollback:                      
            // var_dump($rtn);      
            connectDB::$dbh->rollBack();            
            $sth=connectDB::$dbh->prepare(sqlUnLock);
            $sth->execute();
            if(!isset($appendErr))
                $appendErr = $this->logGlusterAppendErr($logCode);
            $this->insertAddGlusterNewGroupLog($logCode,$input,$appendErr,$errCode);
            return CPAPIStatus::AddClusterGroupFail;  
    }

    function insertAddGlusterNewGroupFailTaskShell($input)
    {
        $cmd = $this->cmdVDTaskErrTask;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= '"'.base64_encode('addglustergroup$*'.$input['CephID'].'$*'.$input['NodeIDs'][0].'$*'.$input['NodeIDs'][1].'$*'.$input['NodeNames'][0].'$*'.$input['NodeNames'][1]).'" ';
        $cmd .= 'glusterGroupAdd';
        exec($cmd,$output,$rtn);
    }

    function clusterGroupAddCheckShell($input,$cmdAppend,&$outputMsg)
    {
        $rtn = -99;        
        $cmd = $this->cmdClusterGroupAddCheck;        
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);      
    }

    function addGlusterNewGroupShell($input,$cmdAppend,&$outputMsg)
    {
        $rtn = -99;        
        $cmd = $this->cmdVDTaskClusterGroupAddNew;        
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);   
        $cmd .= '"'.base64_encode('addglustergroup$*'.$input['CephID'].'$*'.$input['NodeIDs'][0].'$*'.$input['NodeIDs'][1].'$*'.$input['NodeNames'][0].'$*'.$input['NodeNames'][1]).'" ';  
        if($input['ClearData'])
            $cmd .= "1 ";
        else
            $cmd .= "1 ";
        $cmd .= $cmdAppend;  
        $cmd .= ' '.$input['DomainID'];
        // var_dump($cmd);     
        exec($cmd,$outputArr,$rtn);              
        foreach ($outputArr as $value) {
            // var_dump($value);
            $outputMsg .= $value.PHP_EOL;
        }  
        // var_dump($outputMsg);
        // var_dump($rtn);      
        return $rtn;  
    }

    function reportVDTaskGlusterGroupAdd($input)
    {
        $this->processReportAddGlusterGroupData($input,$data);
        $rtnCode = 99;        
        $cancelTask = false;
        if(!$this->connectDB()){            
            $rtnCode = 98;
            goto rtnFilnal;
        }   
        if($data['ErrCode'] == 0){  
            $nodeC = new NodeAction();
            if(!$nodeC->sqlUpdateServerState(array("NodeID"=>$data['Node1ID'],"State"=>0))){
                $logCode = '01301007';            
                $rtnCode = 1;
                goto rtnFilnal;
            }
            if(!$nodeC->sqlUpdateServerState(array("NodeID"=>$data['Node2ID'],"State"=>0))){
                $logCode = '01301007';
                $rtnCode = 2;
                goto rtnFilnal;
            }
            $rtnCode = 0;           
            $logCode = '01101003';
            goto rtnFilnal;
        }  
        else{
            if($data['ErrCode'] == 210){                
                $cancelTask = true;
                $logCode = '01101002';
                goto rtnFilnal;             
            }
            $this->sqlDeleteVDServerByidVDServer($data['Node1ID']);
            $this->sqlDeleteVDServerByidVDServer($data['Node2ID']);
            $rtnCode = 0;
            $logCode = '01301108';
            goto rtnFilnal;             
        }   
        rtnRollback:            
            goto rtnFilnal;
        rtnFilnal:            
            $appendErr = $this->logGlusterAppendErr($logCode);
            $this->insertAddGlusterNewGroupLog($logCode,$input,$appendErr,$data['ErrCode'],false,$cancelTask);
            exit($rtnCode);
    }

    function insertAddGlusterNewGroupLog($code,$input,$appendErr,$errCode=NULL,$haveTask=true,$cancelTask=false)
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
                $message = "Cancel Add Hybrid Nodes of Gluster$taskStr";
            else
                $message = "Add Hybrid Nodes of Gluster$taskStr Success";
        }
        else{
            $message = "Failed to Add Hybrid Nodes of Gluster$taskStr$appendErr";
            if(isset($errCode)){
                $rtnMsg = $this->changeModifyReturnCode($errCode);
                $message .= "($rtnMsg)";
            }
        }
        // var_dump($message);
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
        }       
    }

    function deleteGlusterGroup($input)
    {
        $logCode = '';
        $sqlLock = 'LOCK TABLES `tbvolumevdservermappingset` AS t1 WRITE';
        $sqlUnLock = 'UNLOCK TABLES ';
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        $sth=connectDB::$dbh->prepare(sqlLock);
        $sth->execute();
        connectDB::$dbh->beginTransaction();
        // var_dump(0);
        $this->sqlDeleteVDServerByidStoragePair($input['PairID']);
        // var_dump(1);
        $rtn = $this->deleteGlusterGroupShell($input);
        if($rtn != 0){
            $logCode = '01301601';
            goto failRollback;
        }        
        connectDB::$dbh->commit();
        $logCode = '01101501';
        $responsercode = CPAPIStatus::DeleteClusterGroupSuccess;
        goto rtnFilnal;
failRollback:                                  
            connectDB::$dbh->rollBack();            
            $sth=connectDB::$dbh->prepare(sqlUnLock);
            $sth->execute();
            $responsercode =  CPAPIStatus::DeleteClusterGroupFail;  
            goto rtnFilnal;
rtnFilnal:            
            $appendErr = $this->logGlusterAppendErr($logCode);
            $this->insertDeleteGlusterNewGroupLog($logCode,$input,$appendErr,$rtn,false);
            return $responsercode;
    }

    function deleteGlusterGroupShell($input)
    {
        $rtn = -99;
        $cmd = $this->cmdClusterGroupDel;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);     
        $cmd .= $input['PairID'];          
        exec($cmd,$outputArr,$rtn);          
        return $rtn;
    }

    function insertDeleteGlusterNewGroupLog($code,$input,$appendErr,$errCode=NULL,$haveTask=true,$cancelTask=false)
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
                $message = "Cancel Delete Hybrid Nodes of Gluster$taskStr";
            else
                $message = "Delete Hybrid Nodes of Gluster$taskStr Success";
        }
        else{
            $message = "Failed to Delete Hybrid Nodes of Gluster$taskStr$appendErr";
            if(isset($errCode)){
                $rtnMsg = $this->changeModifyReturnCode($errCode);
                $message .= "($rtnMsg)";
            }
        }
        // var_dump($message);
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
        }       
    }

    function setClusterGroupMaster($input)
    {
        $nodeC = new NodeAction();
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        $nodeC->sqlListServerByidVDServer($input['NodeID'],$outputNode);
        if(is_null($outputNode))
            return CPAPIStatus::SetClusterGroupMasterFail;
        $input['CommunicateIP'] = $outputNode['CommunicateIP'];
        $rtn = $this->setClusterGroupMasterSheel($input);
        if($rtn != 0){
            if(LogAction::createLog(array('Type'=>1,'Level'=>3,'Code'=>'01301301','Riser'=>'admin','Message'=>'Failed to Set Master Node('.$outputNode['NodeName'].") of Pair($rtn)."),$lastID)){
                LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            } 
            return CPAPIStatus::SetClusterGroupMasterFail;
        }
        if(LogAction::createLog(array('Type'=>1,'Level'=>1,'Code'=>'01101201','Riser'=>'admin','Message'=>'Set Master Node('.$outputNode['NodeName'].') of Pair Success.'),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
        }
        return CPAPIStatus::SetClusterGroupMasterSuccess;
    }

    function setClusterGroupMasterSheel($input)
    {
        $rtn = -99;
        $cmd = $this->cmdClusterGroupMasterSet;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);     
        $cmd .= $input['PairID'];
        $cmd .= ' ';
        $cmd .= $input['CommunicateIP'];  
        // var_dump($cmd);     
        exec($cmd,$outputArr,$rtn);  
        // var_dump($rtn);      
        return $rtn;
    }

    function replaceGlusterNode($input)
    {
        $errMsg = '';
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        $nodeC = new NodeAction();
        $vswitchC = new vSwitchAction();
        // var_dump($input);
        $nodeC->sqlListServerByidVDServer($input['NodeID'],$outputNode);
        connectDB::$dbh->beginTransaction();
        if(!$this->sqlClearNodeInfovSwitch($input['NodeID'])){
            $rtn = 1;
            goto failRollback;
        }
        if(!$this->sqlClearVDPreferNode($input)){
            $rtn = 5;
            goto failRollback;
        }
        // var_dump(1);
        $input['CommunicateIP'] = $outputNode['CommunicateIP'];        
        $outputNode['ConnectIP'] = $outputNode['CommunicateIP'];        
        if(!$nodeC->reinitInsertNode($outputNode,true)){
            // echo 2;
            $rtn = 2;
            goto failRollback;
        }         
        // var_dump(2);                                   
        if(!$vswitchC->initInsertVswitchNew($outputNode)){                
            $rtn = 4; 
            goto failRollback;
        }
        // var_dump(4);
        $rtn = $this->replaceGlusterNodeShell($input);        
        if( $rtn != 0){
            goto failRollback;
        }           
        connectDB::$dbh->commit();
        if(LogAction::createLog(array('Type'=>1,'Level'=>1,'Code'=>01101101,'Riser'=>'admin','Message'=>"Replace the Cluster Node(".$input['IP'].') successfully'),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
        }
        return CPAPIStatus::ReplaceClusterNodeSuccess;
failRollback:            
            // var_dump($rtn);
            connectDB::$dbh->rollBack(); 
            switch ($rtn) {
                case '217':
                    $rtnErr = "The RAID ID doesn't match the other node";
                    break;
                case '223':
                    $rtnErr = "The Old Node hasn't disconnected,and you will retry again after some time";
                    break;           
                default:
                    $rtnErr = $rtn;
                    break;
            }                                       
            if(LogAction::createLog(array('Type'=>1,'Level'=>3,'Code'=>01301205,'Riser'=>'admin','Message'=>"Failed Replace the Cluster Node(".$input['IP'].")($rtnErr)"),$lastID)){
                LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            }
            return CPAPIStatus::ReplaceClusterNodeFail;
    }
    function sqlClearVDPreferNode($input)
    {
        $result = false;
        $sqlUpdate = "UPDATE tbvdimagebaseset set idVDServer=NULL WHERE idVDServer=:idVDServer";
        try
        {                            
            // var_dump($input);
            $sth = connectDB::$dbh->prepare($sqlUpdate);   
            $sth->bindValue(':idVDServer', $input['NodeID'], PDO::PARAM_INT);
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

    function replaceGlusterNodeShell($input,&$outputMsg)
    {
        $rtn = -99;
        $cmd = $this->cmdClusterReplaceNode;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);     
        $cmd .= $input['CommunicateIP'];  
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);  
        // var_dump($rtn);              
        return $rtn;
    }

    function replaceGlusterNodeWithNewIP($input,&$errMsg)
    {
        $errMsg = '';
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        $nodeC = new NodeAction();
        $vswitchC = new vSwitchAction();
        $systemC = new SystemProcess();
        // var_dump($input);
        $nodeC->sqlListServerByidVDServer($input['NodeID'],$outputNode);
        $input['OrgCommunicateIP'] = $outputNode['CommunicateIP'];
        $input['OrgIP'] = $outputNode['IP'];
        $orgConnectIP = $input['ConnectIP'];
        $input['ConnectIP'] = $input['CommunicateIP'];
        $outputHostname = $systemC->listHostname($input,$rtn);  
        // var_dump($rtn);        
        if($rtn != 0){
            return CPAPIStatus::ReplaceClusterNodeFail;
        }
        $input['NodeName'] = $outputHostname['HostName'];           
        // $input['ConnectIP'] = $orgConnectIP;
        connectDB::$dbh->beginTransaction();
        if(!$this->sqlClearNodeInfovSwitch($input['NodeID'])){
            $logCode = '01301201';
            $appendErr = 'Failed to clear node';
            goto failRollback;
        }
        // $input['CommunicateIP'] = $outputNode['CommunicateIP'];        
        // $outputNode['ConnectIP'] = $outputNode['CommunicateIP'];        
        if(!$this->sqlUpdateNodeAddress($input)){
            $logCode = '01300202';
            $appendErr = 'Failed to update node address';
            goto failRollback;
        }        
        if(!$nodeC->reinitInsertNode($input,true)){            
            $logCode = '01300203';
            $appendErr = 'Failed to insert node';
            goto failRollback;
        }                                                 
        if(!$vswitchC->initInsertVswitchNew($input)){                
            $logCode = '01300204';
            $appendErr = 'Failed to insert vswitch';
            goto failRollback;
        }
        $input['ConnectIP'] = $orgConnectIP;
        $errCode = $this->replaceGlusterNodeNewCheckShell($input,$outputMsg);
        if( $errCode != 0){
            $appendErr = '';
            $logCode = '01300205-1';
            if(strlen($outputMsg) > 0)
                $errMsg = base64_encode($outputMsg);
            goto failRollback;
        }     
        $errCode = $this->replaceGlusterNodeNewShell($input,$outputMsg);
        if( $errCode != 0){
            $appendErr = '';
            $logCode = '01300205';
            if(strlen($outputMsg) > 0)
                $errMsg = base64_encode($outputMsg);
            goto failRollback;
        }                   
        connectDB::$dbh->commit();
        $appendErr = NULL;
        $rtn=CPAPIStatus::ReplaceClusterNodeSuccess;
        $logCode = '01101101';
        // $this->insertReplaceLog($logCode,$input,$appendErr,$errCode);
        goto end;
failRollback:                
            connectDB::$dbh->rollBack();                        
            $rtn=CPAPIStatus::ReplaceClusterNodeFail;  
            goto end;
end:
        $this->insertReplaceLog($logCode,$input,$appendErr,$errCode);
        return $rtn;
    }

    function insertReplaceLog($code,$input,$appendErr,$errCode=NULL)
    {
        $logType = 1;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $appendNodeStr = 'From '.$input['OrgCommunicateIP'].' to '.$input['CommunicateIP'];
        if(is_null($appendErr)){            
            $message = "Replace Node($appendNodeStr) Success.";
        }
        else{            
            $message = "Failed to replace Node($appendNodeStr)";
            if(strlen($appendErr) > 0)
                 $message .= "($appendErr)";
            if(isset($errCode)){
                if($errCode == 217)
                    $message .= "(The RAID ID doesn't match the other node)";
                else
                    $message .= "($errCode)";
            }
            $message .= '.';
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
        }
    }

    function sqlUpdateNodeAddress($input)
    {
        $result = false;
        $outputVolumes = NULL;
        $sqlUpdate = "UPDATE tbvdserverset set address=:address,nameNode=:nameNode WHERE idVDServer=:idVDServer";
        try
        {                            
            // var_dump($input);
            $sth = connectDB::$dbh->prepare($sqlUpdate);   
            $sth->bindValue(':idVDServer', $input['NodeID'], PDO::PARAM_INT);
            $sth->bindValue(':address', $input['IP'], PDO::PARAM_STR);  
            $sth->bindValue(':nameNode', $input['NodeName'], PDO::PARAM_STR);  
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

    function replaceGlusterNodeNewCheckShell($input,&$outputMsg)
    {
        $rtn = -99;
        $cmd = $this->cmdClusterReplaceNodeNewCheck;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);     
        $cmd .= $input['OrgCommunicateIP'].' ';  
        $cmd .= $input['NodeName'].' ';
        $cmd .= $input['CommunicateIP'].' ';
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        foreach ($outputArr as $value) {           
            $outputMsg .= $value.PHP_EOL;
        }          
        // var_dump($rtn);
        return $rtn;
    }

    function replaceGlusterNodeNewShell($input,&$outputMsg)
    {
        $rtn = -99;
        $cmd = $this->cmdClusterReplaceNodeNew;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);     
        $cmd .= $input['OrgCommunicateIP'].' ';  
        $cmd .= $input['NodeName'].' ';
        $cmd .= $input['CommunicateIP'].' ';
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        foreach ($outputArr as $value) {           
            $outputMsg .= $value.PHP_EOL;
        }          
        // var_dump($rtn);
        return $rtn;
    }

    function reservePairSplit($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        $nodeC = new NodeAction();
        $nodeC->sqlListServerByidVDServer($input['NodeID'],$outputNode);
        $input['CommunicateIP'] =$outputNode['CommunicateIP'];
        $rtn = $nodeC->reserveSplitNodeShell($input);
        if( $rtn == 0){
            if(LogAction::createLog(array('Type'=>1,'Level'=>1,'Code'=>'01101601','Riser'=>'admin','Message'=>'Reserver Node('.$outputNode['NodeName'].") of Pair."),$lastID)){
                LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            } 
            return CPAPIStatus::ReserveNodeSuccess;            
        }
        else{
            if(LogAction::createLog(array('Type'=>1,'Level'=>3,'Code'=>'01301701','Riser'=>'admin','Message'=>'Failed to Reserver Node('.$outputNode['NodeName'].") of Pair($rtn)."),$lastID)){
                LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            } 
            return CPAPIStatus::ReserveNodeFail;
        }
    }

    function sqlDeleteVDServerByidStoragePair($idStoragePair)
    {
        $result = false;       
        $sqlClear = "DELETE FROM tbvdserverset WHERE idStoragePair=:idStoragePair";
        try
        {                            
            $sth = connectDB::$dbh->prepare($sqlClear);
            $sth->bindValue(':idStoragePair', $idStoragePair, PDO::PARAM_INT);
            if($sth->execute())
            {                
                $result = true;
            }                                       
        }
        catch (Exception $e){   
            
        }                        
        return $result;
    }

    function sqlDeleteVDServerByidVDServer($idVDServer)
    {
        $result = false;       
        $sqlDeleteServerNIC = "DELETE FROM tbvdservernicset WHERE idVDServer=:idVDServer";
        $sqlClear = "DELETE FROM tbvdserverset WHERE idVDServer=:idVDServer";
        try
        {                            
            $sth = connectDB::$dbh->prepare($sqlDeleteServerNIC);
            $sth->bindValue(':idVDServer', $idVDServer, PDO::PARAM_INT);
            if($sth->execute())
            { 
                $sth = connectDB::$dbh->prepare($sqlClear);
                $sth->bindValue(':idVDServer', $idVDServer, PDO::PARAM_INT);
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

    function sqlInsertVolume($input)
    {
        $result = false;
        $outputVolumes = NULL;
        $sqlInsert = "INSERT tbVolumeSet (idVolume,idCephpool,name,nameAlias,sizeVolume) VALUES (:idVolume,1,:name,:nameAlias,:sizeVolume);";
        try
        {                            
            // var_dump($input);
            $sth = connectDB::$dbh->prepare($sqlInsert);   
            $sth->bindValue(':idVolume', $input['VolumeID'], PDO::PARAM_STR);
            $sth->bindValue(':name', $input['Name'], PDO::PARAM_STR);
            $sth->bindValue(':nameAlias', $input['Alias'], PDO::PARAM_STR);
            $sth->bindValue(':sizeVolume', $input['Size'], PDO::PARAM_INT);         
            if($sth->execute())
            {                       
                if ($sth->rowCount()== 1) {
                    $result = true;                 
                }
            }                                       
        }
        catch (Exception $e){   
            $result = false;            
        }      
        // var_dump($result);
        return $result;           
    }

    function sqlInsertVolumeNodeMapping($idVolume,$idVDServer)
    {
        $result = false;
        $outputVolumes = NULL;
        $sqlInsert = "INSERT tbVolumeVDServerMappingSet (idVolume,idVDServer) VALUES (:idVolume,:idVDServer);";
        try
        {                            
            // var_dump($input);
            $sth = connectDB::$dbh->prepare($sqlInsert);               
            $sth->bindValue(':idVolume', $idVolume, PDO::PARAM_STR);
            $sth->bindValue(':idVDServer', $idVDServer, PDO::PARAM_INT);         
            if($sth->execute())
            {                       
                if ($sth->rowCount()== 1) {
                    $result = true;                 
                }
            }                                       
        }
        catch (Exception $e){   
            $result = false;            
        }      
        // var_dump($result);
        return $result;           
    }

	function deleteGluster($input)
	{
        $this->backupMysql();
		$rtn = $this->deleteGlusterShell($input);
		if($rtn != 0)
			return CPAPIStatus::DeleteClusterFail;
		else
			return CPAPIStatus::DeleteClusterSuccess;
	}

    function backupMysql()
    {
        $cmd = CmdRoot.'127.0.0.1 mysql_bk_for_del';
        exec($cmd,$outputArr,$rtn);
    }

	function deleteGlusterShell($input)
	{
		$cmd = $this->cmdClusterDel;
		$cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
		$cmd .= '>/dev/null 2>&1 &';
        // var_dump($cmd);
		$pipe = popen($cmd,"r");    
        pclose($pipe); 
        $rtn = 0;
		return $rtn;
		// var_dump($cmd);
		// var_dump($rtn);
	}

    function shutdownGluster($input)
    {
        $rtn = $this->shutdownGlusterShell($input);        
        if($rtn == 0)
            return CPAPIStatus::ShutdownClusterSuccess;
        else
            return CPAPIStatus::ShutdownClusterFail;
    }

    function shutdownGlusterShell($input)
    {        
        $cmd = $this->cmdClusterShutdown;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);     
        $cmd .= 'force >/dev/null 2>&1 &';
        $pipe = popen($cmd,"r");    
        pclose($pipe); 
        // var_dump($cmd);
        // exec($cmd,$outputArr,$rtn);       
        return 0;
    }

    function shutdownGlusterAll($input)
    {
        $cmd = $this->cmdClusterShutdownAll;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);    
        $cmd .= '>/dev/null 2>&1 &'; 
        exec($cmd,$outputArr,$rtn);   
        $pipe = popen($cmd,"r");    
        pclose($pipe);  
    }

    function rebootGluster($input)
    {
        $rtn = $this->rebootGlusterShell($input);
        if($rtn == 0)
            return CPAPIStatus::RebootClusterSuccess;
        else
            return CPAPIStatus::RebootClusterFail;
    }

    function rebootGlusterShell($input)
    {
        $cmd = $this->cmdClusterReboot;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= 'force';     
        exec($cmd,$outputArr,$rtn);       
        return $rtn;
    }

    function listGlusterAlarm($input,&$output)
    {
        $output = array();
        $output['Alarm'] = true;
        if($this->listGlusterAlarmShell($input,$outputAlarm)){
            $output['Alarm'] = $outputAlarm == 0 ? false : true;
        }
    }

    function listGlusterAlarmShell($input,&$output)
    {
        $cmd = $this->cmdClusterAlarmList;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);        
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($outputArr);
        if($rtn == 0){              
            $output = (int)$outputArr[0];
            return true;      
        }            
        return false;
    }

    function setGlusterAlarm($input)
    {
        if($this->setGlusterAlarmShell($input) != 0 )
            return CPAPIStatus::SetClusterAlarmFail;
        return CPAPIStatus::SetClusterAlarmSuccess;
    }

    function setGlusterAlarmShell($input)
    {
        $rtn = -99;
        $cmd = $this->cmdClusterAlarmSet;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);         
        $cmd .= $input['Alarm'] == true ? 1 : 0;       
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        return $rtn;
    }

    function listGlusterAutosuspend($input,&$output)
    {
        $output['Autosuspend'] = 60;
        if($this->listGlusterAutosuspendShell($input,$outputSuspend)){
            // var_dump($outputSuspend);
            $output['Autosuspend'] = $outputSuspend;
        }
    }

    function listGlusterAutosuspendShell($input,&$output)
    {
        $cmd = $this->cmdClusterAutosuspendList;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);        
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($outputArr);
        if($rtn == 0 && is_int((int)$outputArr[0])){               
            $output = (int)$outputArr[0];
            return true;      
        }            
        return false;
    }

    function setGlusterAutosuspend($input)
    {
        if($this->setGlusterAutosuspendShell($input) != 0)
            return CPAPIStatus::SetClusterAutosuspendFail;
        return CPAPIStatus::SetClusterAutosuspendSuccess;
    }

    function setGlusterAutosuspendShell($input)
    {
        $rtn = -99;
        $cmd = $this->cmdClusterAutosuspendSet;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);  
        $cmd .= $input['Autosuspend'];      
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($outputArr);      
        return $rtn;
    }

    function listGlusterSambaStatus($input,&$output)
    {
        $output['Start'] = true;        
        if($this->listGlusterSambaStatusShell($input,$outputStatus)){
            // var_dump($outputStatus);
            $output['Start'] = $outputStatus == 1 ? true :false;
        }
    }

    function listGlusterSambaStatusShell($input,&$output)
    {
        $rtn = -99;
        $cmd = $this->cmdClusterSambaStatus;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);          
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($outputArr);      
       if($rtn == 0 && is_int((int)$outputArr[0])){               
            $output = (int)$outputArr[0];
            return true;      
        }            
        return false;         
    }

    function setGlusterSambaStatus($input)
    {
        if($this->setGlusterSambaStatusShell($input) != 0){
            return CPAPIStatus::SetClusterSambaFail;
        }
        return CPAPIStatus::SetClusterSambaSuccess;
    }

    function setGlusterSambaStatusShell($input)
    {
        $rtn = -99;
        if($input['Start'])
            $cmd = $this->cmdClusterSambaStart;
        else
            $cmd = $this->cmdClusterSambaStop;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);          
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($outputArr);      
        return $rtn;
    }

    function setClusterSambaPasswd($input)
    {

    }

    function setClusterSambaPasswdShell($input)
    {
        $rtn = -99;
        $cmd = $this->cmdClusterSambaPasswdSet;        
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);          
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($outputArr);      
        return $rtn;   
    }

    function listClusterDedup($input,&$output)
    {        
        if($this->listClusterDedupShell($input,$outputStatus)){
            $output = array();
            // var_dump($outputStatus);
            $output['Dedupe'] = $outputStatus == 'on' ? true : false;
            return CPAPIStatus::ListClusterDedupSuccess;
        }            
        else
            return CPAPIStatus::ListClusterDedupFail;
    }

    function listClusterDedupShell($input,&$output)
    {
        $rtn = -99;
        $cmd = $this->cmdClusterDedupList;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);          
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($outputArr);      
       if($rtn == 0){
            $output = $outputArr[0];
            return true;      
        }            
        return false;
    }

    function setClusterDedup($input,&$output)
    {
        $this->setClusterDedupShell($input);
        if($this->listClusterDedup($input,$output) != CPAPIStatus::ListClusterDedupSuccess){            
            return CPAPIStatus::SetClusterDedupFail;
        }        
        return CPAPIStatus::SetClusterDedupSuccess;
    }

    function setClusterDedupShell($input)
    {
        $cmd = $this->cmdClusterDedup;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);     
        $cmd .= $input['Dedupe'] == true ? 'on' : 'off';   
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($outputArr);
        if($rtn == 0){               
            return true;      
        }            
        return false;
    }

    function listClusterRAIDsForCreate($input,&$output)
    {
        $zfsC = new ZfsAction();
        $nodeC = new NodeAction();
        $nodeC->sqlListServers($outputNodes);
        if(is_null($outputNodes))
            return CPAPIStatus::ListClusterVolumesFail;
        $outputRAIDs = array();
        foreach ($outputNodes as $value) {
            $input['CommunicateIP'] = $value['CommunicateIP'];
            $zfsC->listZfsForCreate($input,$outputRAID);       
            // var_dump($outputRAID);     
            if(sizeof($outputRAIDs) == 0){
                foreach ($outputRAID as $value1) {
                    $outputRAIDs[] = $value1;
                }
            }
            else{
                foreach ($outputRAID as $value2) {
                    foreach ($outputRAIDs as &$value3) {
                        if($value2['RAIDID'] == $value3['RAIDID']){
                            if(strlen($value2['Level']) == 0){
                                $value3['Level'] = "";
                                $value3['Status'] = "";
                                $value3['Total'] = 0;
                                $value3['Used'] = 0;
                            }                                
                        }                        
                        // var_dump($value3);
                    }
                }
            }
        }        
        if(isset($outputRAIDs)){     
            $this->sqlListVolumes($outputSQLVolumes);
            // var_dump($outputSQLVolumes);
            if(is_null($outputSQLVolumes))
                return CPAPIStatus::ListClusterVolumesFail;
            foreach ($outputRAIDs as &$value) {
                $value['isClusterUsed'] = false;
                foreach ($outputSQLVolumes as $sqlValue) {
                    if($value['RAIDID'] == $sqlValue['VolumeName'])
                        $value['isClusterUsed'] = true;
                }
                $value['Total'] = $value['Total']*1024*1024*1024;
                $value['Used'] = $value['Used']*1024*1024*1024;
            }
            $output = $outputRAIDs;
            return CPAPIStatus::ListClusterVolumesSuccess;
        }
        else{
            return CPAPIStatus::ListClusterVolumesFail;
        }
    }

    function listClusterPairRAIDsForCreate($input,&$output)
    {
        // var_dump($input);
        $zfsC = new ZfsAction();
        $nodeC = new NodeAction();
        $nodeC->sqlListServersByPairID($input['PairID'],$outputNodes);
        // var_dump($outputNodes);
        if(is_null($outputNodes))
            return CPAPIStatus::ListClusterVolumesFail;
        $outputRAIDs = array();
        foreach ($outputNodes as $value) {
            if($value['State'] == -1)
                continue;
            $input['CommunicateIP'] = $value['CommunicateIP'];
            $zfsC->listZfsForCreate($input,$outputRAID);       
            // var_dump($outputRAID);     
            if(sizeof($outputRAIDs) == 0){
                foreach ($outputRAID as $value1) {
                    $outputRAIDs[] = $value1;
                }
            }
            else{
                foreach ($outputRAID as $value2) {
                    foreach ($outputRAIDs as &$value3) {
                        if($value2['RAIDID'] == $value3['RAIDID']){
                            if(strlen($value2['Level']) == 0){
                                $value3['Level'] = "";
                                $value3['Status'] = "";
                                $value3['Total'] = 0;
                                $value3['Used'] = 0;
                            }                                
                        }                        
                    }
                }
            }
        }        
        if(isset($outputRAIDs)){     
            $this->sqlListVolumesByPairID($input['PairID'],$outputSQLVolumes);
            // var_dump($outputSQLVolumes);
            if(is_null($outputSQLVolumes))
                return CPAPIStatus::ListClusterVolumesFail;
            foreach ($outputRAIDs as &$value) {
                $value['isClusterUsed'] = false;
                foreach ($outputSQLVolumes as $sqlValue) {
                    if($value['RAIDID'] == $sqlValue['VolumeName'])
                        $value['isClusterUsed'] = true;
                }
                $value['Total'] = $value['Total']*1024*1024*1024;
                $value['Used'] = $value['Used']*1024*1024*1024;
            }
            $output = $outputRAIDs;
            return CPAPIStatus::ListClusterVolumesSuccess;
        }
        else{
            return CPAPIStatus::ListClusterVolumesFail;
        }
    }

    function listClusterVolumes($input,&$output)
    {
        if($this->listClusterVolumesShell($input,$outputVolumes)==0){     
            $this->sqlListVolumes($outputSQLVolumes);
            if(is_null($outputSQLVolumes))
                return CPAPIStatus::ListClusterVolumesFail;
            foreach ($outputSQLVolumes as &$sqlValue) {
                $sqlValue['VolumeName']=(int)$sqlValue['VolumeName'];
                $sqlValue['Pair'] = array();            
                foreach ($outputVolumes as $value) {                   
                    if($sqlValue['VolumeName'] == $value['raidId']){
                        // var_dump($value);
                        foreach ($value['ownerGroupId'] as $value1) {
                            $sqlValue['Pair'][] = (int)$value1;
                        }
                        $sqlValue['State'] = $this->volumeStateEnum[$value['state']];
                        if($value['state'] == 'offline'){
                            // var_dump('Offline');
                            $sqlValue['Size'] = NULL;
                            $sqlValue['Used'] = NULL;
                            $sqlValue['Usage'] = NULL;
                            $sqlValue['Dedupe'] = $value['dedup'] == 'on' ? true : false;
                        }
                        else{                            
                            if(strlen($value['usage']) > 0){
                                if($sqlValue['Size'] != $value['total']*1024*1024){
                                    $sqlValue['Size'] = $value['total']*1024*1024;
                                    $sqlValue['Name'] = $sqlValue['VolumeName'];
                                    $this->sqlUpdateVolumeSize($sqlValue);
                                }                            
                                $sqlValue['Used'] = $value['used']*1024*1024;
                                $sqlValue['Usage'] = $value['usage'];                                
                            }
                            else{
                                $sqlValue['Size'] = NULL;
                                $sqlValue['Used'] = NULL;
                                $sqlValue['Usage'] = NULL;
                            }
                            $sqlValue['Dedupe'] = $value['dedup'] == 'on' ? true : false;
                        }                        
                    }
                }           
                // $sqlValue['Size'] = NULL;
                // $sqlValue['Used'] = NULL;
                // $sqlValue['Usage'] = NULL;     
            }
            $output = $outputSQLVolumes;
            return CPAPIStatus::ListClusterVolumesSuccess;
        }
        else{
            return CPAPIStatus::ListClusterVolumesFail;
        }
    }

    function listClusterVolumesShell($input,&$output)
    {
        $rtn = -99;
        $cmd = $this->cmdClusterVolumeList;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        exec($cmd,$outputArr,$rtn);
        if($rtn == 0){ 
            $output = json_decode($outputArr[0],true);
        }
        return $rtn;
    }

    function sqlListVolumes(&$outputVolumes)
    {
        $outputVolumes = NULL;
        $sqlList = "SELECT * FROM tbVolumeSet ORDER BY name";
        try
        {                            
            $sth = connectDB::$dbh->prepare($sqlList);            
            if($sth->execute())
            {                       
                $outputVolumes = array();
                while( $row = $sth->fetch() ) 
                {                                             
                    $outputVolumes[]=array("VolumeID"=>$row['idVolume'],'VolumeName'=>$row['name'],'VolumeAlias'=>$row['nameAlias'],"Size"=>(int)$row['sizeVolume']);
                }          
            }                                       
        }
        catch (Exception $e){
        }                                
    }    

    function sqlListVolumesByPairID($idStoragePair,&$outputVolumes)
    {
        $outputVolumes = NULL;
        $sqlList = "SELECT DISTINCT(t2.idVolume),t2.name,t2.nameAlias,t2.sizeVolume FROM tbvolumevdservermappingset as t1 inner join tbvolumeset as t2 on t1.idVolume=t2.idVolume inner join tbvdserverset as t3 on t1.idVDServer=t3.idVDServer WHERE t3.idStoragePair=:idStoragePair ORDER BY t2.name";
        try
        {                            
            $sth = connectDB::$dbh->prepare($sqlList);
            $sth->bindValue(':idStoragePair', $idStoragePair, PDO::PARAM_INT);            
            if($sth->execute())
            {                       
                $outputVolumes = array();
                while( $row = $sth->fetch() ) 
                {                                             
                    $outputVolumes[]=array("VolumeID"=>$row['idVolume'],'VolumeName'=>$row['name'],'VolumeAlias'=>$row['nameAlias'],"Size"=>(int)$row['sizeVolume']);
                }          
            }                                       
        }
        catch (Exception $e){
        }                                
    }

    function sqlClearNotFirstPairNode()
    {
        $result = false;       
        $sqlClear = "DELETE FROM tbvdserverset WHERE idStoragePair > 1";
        try
        {                            
            $sth = connectDB::$dbh->prepare($sqlClear);            
            if($sth->execute())
            {                
                $result = true;
            }                                       
        }
        catch (Exception $e){   
            
        }                        
        return $result;
    }

	function sqlClearNode()
	{
		$result = false;       
        $sqlClear = "DELETE FROM tbvdserverset WHERE idVDServer > 0";
        try
        {                            
            $sth = connectDB::$dbh->prepare($sqlClear);            
            if($sth->execute())
            {                
                $result = true;
            }                                       
        }
        catch (Exception $e){   
            
        }                        
        return $result;
	}	

    function sqlClearNodeInfovSwitch($idVDServer)
    {
        $result = false;       
        $sqlClearNodeInfo = "DELETE FROM tbvdserverinfoset WHERE idVDServer=:idVDServer";
        $sqlClearNodeNIC = "DELETE FROM tbvdservernicset WHERE idVDServer=:idVDServer";
        $sqlClearBond = "DELETE FROM tbbondset WHERE idVDServer=:idVDServer";
        $sqlClearNodevSwitch = "DELETE FROM tbvswitchset where idVSwitch IN (select idVswitch from(select t1.idVSwitch as idVSwitch from tbvswitchset as t1 left join tbvswitchbondmappingset as t2 on t1.idVSwitch=t2.idVSwitch where t2.idVSwitch IS NULL) dummyname)";
        try
        {                            
            $sth = connectDB::$dbh->prepare($sqlClearNodeInfo);
            $sth->bindValue(':idVDServer', $idVDServer, PDO::PARAM_INT);        
            if($sth->execute())
            {                
                $sth = connectDB::$dbh->prepare($sqlClearNodeNIC);
                $sth->bindValue(':idVDServer', $idVDServer, PDO::PARAM_INT);   
                if($sth->execute())
                {
                    $sth = connectDB::$dbh->prepare($sqlClearBond);
                    $sth->bindValue(':idVDServer', $idVDServer, PDO::PARAM_INT);   
                    if($sth->execute())
                    {
                        $sth = connectDB::$dbh->prepare($sqlClearNodevSwitch);                        
                        if($sth->execute())
                        {
                            $result = true;
                        }
                    }
                } 
            }                                       
        }
        catch (Exception $e){            
        }    
        return $result;
    }

    function sqlClearNodevSwitch($idVDServer)
    {
        $result = false;               
        $sqlClearNodeNIC = "DELETE FROM tbvdservernicset WHERE idVDServer=:idVDServer";
        $sqlClearBond = "DELETE FROM tbbondset WHERE idVDServer=:idVDServer";
        $sqlClearNodevSwitch = "DELETE FROM tbvswitchset where idVSwitch IN (select idVswitch from(select t1.idVSwitch as idVSwitch from tbvswitchset as t1 left join tbvswitchbondmappingset as t2 on t1.idVSwitch=t2.idVSwitch where t2.idVSwitch IS NULL) dummyname)";
        try
        {                            
            $sth = connectDB::$dbh->prepare($sqlClearNodeNIC);
            $sth->bindValue(':idVDServer', $idVDServer, PDO::PARAM_INT);        
            if($sth->execute())
            {        
                $sth = connectDB::$dbh->prepare($sqlClearBond);
                $sth->bindValue(':idVDServer', $idVDServer, PDO::PARAM_INT);        
                if($sth->execute())
                {                        
                    $sth = connectDB::$dbh->prepare($sqlClearNodevSwitch);                
                    if($sth->execute())
                    {
                        $result = true;
                    }
                }
            }                                       
        }
        catch (Exception $e){            
        }    
        return $result;
    }

	function checkGluster($input,&$output)
	{	
		$nodeC = new NodeAction();
		$responsercode = $nodeC->checkNode($input,$output);
		// var_dump($input);
		if($responsercode != CPAPIStatus::CheckNodeSuccess)
			return false;
        // var_dump($output);
    //       $rtn = $this->checkGlusteShell($input);
		// if( $rtn === false)
		// 	return false;
        // var_dump($rtn);
		// $output['HaveGluster'] = $rtn == 1 ? true : false;
        if($output['HaveGluster']){
            if($nodeC->listStateAllShell($input,$outputGluster)){                
                foreach ($outputGluster['info'] as $value) {
                    if($value['splitState'] == 'recovery')
                        $outputGluster['clusterState'] = 'split.recovery';
                }                
                $output['GlusterState'] = $this->glusterStateEnum[$outputGluster['clusterState']];
            }
            else    
                $output['GlusterState'] = $this->glusterStateEnum['boot'];
        }
        else{
            
            // var_dump($output);
            if($output['Booting'])
                $output['GlusterState'] = $this->glusterStateEnum['boot'];   
            else if($output['Restoring'])
                $output['GlusterState'] = $this->glusterStateEnum['GlusterRestoring'];   
            else if($output['CanRestore'])
                $output['GlusterState'] = $this->glusterStateEnum['noGlusterHaveData'];   
            else if($output['WaitGroup'])
                $output['GlusterState'] = $this->glusterStateEnum['waitGroupReady'];   
            else if($output['WaitSplitBrainGroup'])
                $output['GlusterState'] = $this->glusterStateEnum['waitSplitBrainGroup']; 
            else{                
                if($output['CheckResult'] == 2)
                    $output['GlusterState'] = $this->glusterStateEnum['noGluster'];                   
                else
                    $output['GlusterState'] = $this->glusterStateEnum['boot'];
            }
        }

        /* For Test */
        // $output['GlusterState'] = $this->glusterStateEnum['split.wait.discard.nodeName'];
        
		return true;
	}

    function listGlusterState($input,&$output)
    {                
        if($this->listGlusterStateShell($input,$outputGluster)){               
            $output['GlusterState'] = $this->glusterStateEnum[$outputGluster['clusterState']];
            $output['MaintainState'] = $outputGluster['maintainState'] == 'yes' ? true : false;
            $output['VMS'] = $outputGluster['vms@this'] == 'yes' ? true : false;
            $output['DataBase'] = $outputGluster['mysql@this'] == 'yes' ? true : false;
            $output['VMSIP'] = $outputGluster['vmsIp'];
            $output['PingDNS'] = $outputGluster['dnsPing'] == 'ok' ? true : false; 
        }
        else    
            $output['GlusterState'] = $this->glusterStateEnum['boot'];        
    }

    function listGlusterStateShell($input,&$output)
    {
        $cmd = $this->cmdClusterListState;               
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);  
        // var_dump($cmd);          
        exec($cmd,$outputArr,$rtn);
        // var_dump($rtn);       
        // var_dump($outputArr);
        if($rtn == 0){              
            $output = json_decode($outputArr[0],true);
            // var_dump($output);
            return true;      
        }            
        return false;
    }

    function setClusterNodeNomarlState($input)
    {
        $cmd = $this->cmdClusterNodeSetMaintainWeb;
        $cmd = str_replace ( 'ip' , '127.0.0.1', $cmd);
        $cmd .= 'no';
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($rtn);
        if($rtn == 0)
            return CPAPIStatus::SetNodeMaintainanceSuccess;
        else  
            return CPAPIStatus::SetNodeMaintainanceFail;
    }

    function setClusterGroupMasterSetLocal($input)
    {
        $cmd=$this->cmdClusterGroupMasterSetLocal;
        $cmd = str_replace ( 'ip' , '127.0.0.1', $cmd);
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($rtn);
        if($rtn == 0)
            return CPAPIStatus::SetLocalMasterSuccess;
        else  
            return CPAPIStatus::SetLocalMasterFail;   
    }

    function splitAction($input)
    {
        switch ($input['Action']) {
            case 1:
                $rtn = $this->splitClearGluster($input);
                break;
            case 2:
                $rtn = $this->splitReboot($input);
                break;
            default:
                return CPAPIStatus::splitProcessFail;
                break;
        }
        return $rtn;
    }

    function splitClearGluster($input)
    {
        $nodeC = new NodeAction();
        $nodeC->sqlListServerByidVDServer($input['NodeID'],$outputNode);
        $input['CommunicateIP'] = $outputNode['CommunicateIP'];
        if($this->clearGluster(array('ConnectIP'=>$outputNode['CommunicateIP'])) == CPAPIStatus::clearGlusterSuccess){
            $nodeC->rebootNodeShell($input,true);
            return CPAPIStatus::splitProcessSuccess;
        }
        else{
            return CPAPIStatus::splitProcessFail;
        }
    }

    function splitReboot($input)
    {
        $nodeC = new NodeAction();
        $nodeC->sqlListServerByidVDServer($input['NodeID'],$outputNode);
        $nodeC->sqlListServersByPairID($outputNode['PairID'],$outputNodes);
        foreach ($outputNodes as $value) {
            if($input['NodeID'] != $value['NodeID'])
                $input['CommunicateIP'] = $value['CommunicateIP'];
        }        
        $nodeC->rebootNodeShell($input,true);
        return CPAPIStatus::splitProcessSuccess;
    }

    function clearGluster($input)
    {   
        // $input['ConnectIP'] = '127.0.0.1';
        $cmd = $this->cmdClusterClearNode;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);   
        // var_dump($cmd);                        
        exec($cmd,$outputArr,$rtn);
        if($rtn == 0)
            return CPAPIStatus::clearGlusterSuccess;
        else
            return CPAPIStatus::clearGlusterFail;
        // var_dump($rtn);
    }

    function checkGlusterForLogin($input,&$output)
    {
        $rtn = $this->checkGlusterShell($input,$outputGluster);        
        // var_dump($rtn);
        $output['HaveGluster'] = true;
        $output['VMSIP'] = '';
        $output['VMSMask'] = '';
        if($rtn !== false){
            if($rtn == 0){                
                switch ($outputGluster) {                    
                    case '5':
                    case '6':                    
                        // $output['HaveGluster'] = true;
                        $rtn = $this->listVMSIPListShell($outputVMSIP);
                        if($rtn == 0 && isset($outputVMSIP['ip'])){
                            $output['VMSIP'] = $outputVMSIP['ip'];
                            $output['VMSMask'] = $outputVMSIP['mask'];
                        }     
                        break;                                      
                    default:                        
                        $output['HaveGluster'] = false;
                        break;
                }
                // $output['Restoring'] = $outputGluster == 3 ? true : false;
                // $output['Booting'] = $outputGluster == 4 ? true : false;
                // $output['WaitGroup'] = $outputGluster == 5 ? true : false;  
                // $output['WaitSplitBrainGroup'] = $outputGluster == 6 ? true : false;              
            }
            if($rtn == 1){
                $rtn = $this->listVMSIPListShell($outputVMSIP);
                if($rtn == 0 && isset($outputVMSIP['ip'])){
                    $output['VMSIP'] = $outputVMSIP['ip'];
                    $output['VMSMask'] = $outputVMSIP['mask'];
                }                
            }
        }
    }

	function checkGlusterShell($input,&$output)
	{
		$cmd = $this->cmdClusterCheckClusterCreated;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);   
        // var_dump($cmd);                        
        exec($cmd,$outputArr,$rtn);        
        // var_dump($outputArr);
        if(isset($outputArr[0])){     
            $output = $outputArr[0];
            if($outputArr[0] == 0)
            	return 1;
           	else{
           		return 0; 
            }
        }            
        return false;
	}

    function listVMSIPListShell(&$output,$input=null)
    {        
        $output=null;
        $cmd = $this->cmdClusterVMSIPList;         
        if(isset($input['ConnectIP']))
            $cmd = str_replace('ip ',$input['ConnectIP'].' ', $cmd);
        else
        $cmd = str_replace ('ip ','127.0.0.1 ', $cmd);        
        exec($cmd,$outputArr,$rtn);               
        if($rtn == 0){                         
            $output = json_decode($outputArr[0],true);
        }            
        return $rtn;
    }

    function listGluster($input,&$output)
    {
        $output = array();
        $output['CapacityTotal'] = 0;
        $output['CapacityUsed'] = 0;
        $output['CapacityUsage'] = 0;
        if($this->listClusterCapacityShell($input,$outputCapacity) == 0){
            $output['CapacityTotal'] = (int)($outputCapacity['total']*1024*1024);
            $output['CapacityUsed'] = (int)($outputCapacity['used']*1024*1024);
            $output['CapacityUsage'] = floatval($outputCapacity['usage']);
        }
        $this->listUPSShell($input,$outputUPS);
        // var_dump($outputUPS);
        $output['UPS'] = $outputUPS;
    }

    function listGlusterMiscellaneous($input,&$output)
    {
        $nodeC=new NodeAction();
        $output=array();
        $output['MaxPoweronVD'] = 60;
        if($nodeC->listStateAllShell($input,$outputState)){
            if(isset($outputState['maxPoweron'])){
                $output['MaxPoweronVD'] = (int)$outputState['maxUserVdPoweronCt'];
            }
        }
        $cmd = $this->cmdClusterDNSList;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);        
        exec($cmd,$outputArr,$rtn);        
        if($rtn != 0)
            return CPAPIStatus::ListMiscellaneousFail;
        else{
            $outputDNS = json_decode($outputArr[0],true);
            if(!isset($outputDNS['dns'])){
                return CPAPIStatus::ListDNSFail;   
            }
            $output['DNS'] = $outputDNS['dns'];            
        }        
        $this->listVMSIPListShell($outputIPInfo,$input);        
        if(is_null($outputIPInfo))
            return CPAPIStatus::ListMiscellaneousFail;
        else{
            if(!isset($outputIPInfo['ip']) && !isset($outputIPInfo['mask'])){
                return CPAPIStatus::ListMiscellaneousFail;
            }
            else{                
                $output['VMSIP'] = $outputIPInfo['ip'];
                $output['VMSMask'] = $outputIPInfo['mask'];                
            }
        }
        return CPAPIStatus::ListMiscellaneousSuccess;
    }

    function listGlusterSummary($input,&$output)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }        
        $this->sqlListSumCPU($outputSumCPU);
        $output = $outputSumCPU;        
        $this->sqlListAllVolume($outputVolumes);        
        $output['Volumes'] = $outputVolumes;
        $this->sqlListAllServerDistinctvSwitch($outputVswitchs);
        $output['Vswitchs'] = $outputVswitchs;        
        return CPAPIStatus::ListGlusterSummarySuccess;
    }

    function sqlListSumCPU(&$output)
    {
        $output = NULL;
        $sqlList = <<<SQL
            select COUNT(idVDServer) as sumNodes,SUM(numSocket) as sumSocket,SUM(numCore) as sumCore,SUM(numThread) as sumThread,SUM(hzCpu) as sumhzCpu from tbvdserverinfoset;
SQL;
        try        
        {  
            $sth = connectDB::$dbh->prepare($sqlList);                       
            $sth->bindParam(':idVD', $idVD);                    
            if($sth->execute())
            {                                              
                while( $row = $sth->fetch() ) 
                {                         
                    $output = array("SumNodes"=>(int)$row['sumNodes'],'SumCPU'=>array('SumSocket'=>(int)$row['sumSocket'],'SumCore'=>(int)$row['sumCore'],'SumThread'=>(int)$row['sumThread'],'SumCPUhz'=>(int)$row['sumhzCpu']));
                }
            }
        }
        catch (Exception $e){                            
        }        
    }

    function sqlListAllVolume(&$output)
    {
        $output = NULL;
        $sqlList = <<<SQL
            SELECT * FROM tbvolumeset ORDER BY name;
SQL;
        try        
        {  
            $sth = connectDB::$dbh->prepare($sqlList);                            
            if($sth->execute())
            {                       
                $output = array();                       
                while( $row = $sth->fetch() ) 
                {                         
                    $output[] = array("VolumeID"=>$row['idVolume'],'VolumeSize'=>(int)$row['sizeVolume']);
                }                     
            }               
        }
        catch (Exception $e){                            
        }
    }

    function sqlListAllServerDistinctvSwitch(&$output)
    {
        $output = NULL;
        $sqlList = <<<SQL
            SELECT DISTINCT(idVSwitch) FROM tbvdservernicset;
SQL;
        try        
        {  
            $sth = connectDB::$dbh->prepare($sqlList);                       
            $sth->bindParam(':idVD', $idVD);                    
            if($sth->execute())
            {                        
                $output = array();                      
                while( $row = $sth->fetch() ) 
                {                         
                    $output[] = (int)$row['idVSwitch'];
                }                     
            }               
        }
        catch (Exception $e){                            
        }
    }

    function listClusterCapacityShell($input,&$output)
    {
        $cmd = $this->cmdClusterCapacity;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);       
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);      
        // var_dump($outputArr);
        if($rtn == 0){                         
            $output = json_decode($outputArr[0],true);
        }                  
        return $rtn;     
    }

    function listUPSShell($input,&$output)
    {
        $output = array('IP'=>'','DelayTime'=>60,"BatteryStatus"=>-1,"BatteryTime"=>0,"BatteryChargeCondition"=>0);
        $cmd = $this->cmdUPSList;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);               
        exec($cmd,$outputArr,$rtn);       
        // var_dump($rtn);       
        // var_dump($outputArr);
        if($rtn == 0){                         
            $output = json_decode($outputArr[0],true);
            $output = $this->changeUPSJyson($output);
        }            
        return $output;
    }

    function addVolume($input)
    {
        if(!$this->connectDB()){           
            return CPAPIStatus::DBConnectFail;
        }
        // if(!$this->sqlGetUUID($outputUUID))
        // {
        //     return CPAPIStatus::DBConnectFail;
        // }
        // $nodeC = new NodeAction();
        // $input['Name'] = $input['RAIDID'];
        // $input['Size'] = 0;        
        // if(!$nodeC->sqlListServersByPairID($input['PairID'],$outputNodes)){
        //     return CPAPIStatus::AddClusterVolumesFail;
        // }            
        // $this->sqlListVolumes($outputVolumes);
        // connectDB::$dbh->beginTransaction();               
        // $haveVolume = false;            
        // foreach ($outputVolumes as $value) {                
        //     if($value['VolumeName'] == $input['RAIDID']){
        //         $volumeID = $value['VolumeID'];
        //         $haveVolume = true;
        //         break;
        //     }                
        // }
        // // var_dump($haveVolume);
        // if(!$haveVolume){            
        //     $volumeID = $outputUUID['UUID'];
        //     $addVolume = array();
        //     $addVolume['VolumeID'] = $volumeID;
        //     $addVolume['Name'] = $input['RAIDID'];
        //     $addVolume['Size'] = 0;
        //     $addVolume['Alias'] = $input['Alias'];
        //     // var_dump($addVolume);
        //     if(!$this->sqlInsertVolume($addVolume)){
        //         $rtn = 5; 
        //         connectDB::$dbh->rollBack();
        //         return CPAPIStatus::AddClusterVolumesFail;
        //     }
        // }                               
        // $input['VolumeID'] = $volumeID;
        // foreach ($outputNodes as $value) {
        //     if(!$this->sqlInsertVolumeNodeMapping($input['VolumeID'],$value['NodeID']))
        //         return CPAPIStatus::AddClusterVolumesFail;
        // }
        $rtn = $this->addVolumeShell($input);
        if($rtn!=0){            
            connectDB::$dbh->rollBack();
            if(LogAction::createLog(array('Type'=>1,'Level'=>3,'Code'=>'01301501','Riser'=>'admin','Message'=>'Failed to Add Volume('.$input['Alias'].') of Pair ID '.$input['PairID']." Task($rtn)."),$lastID)){
                LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            } 
            $this->insertAddVolumeFailTaskShell($input);
            return CPAPIStatus::AddClusterVolumesFail;
        }        
        // $this->listClusterVolumesShell($input,$outputVolumes);
        // $volumeIndex = (int)$input['RAIDID'];
        // $totalSize = $outputVolumes[$volumeIndex]['total']*1024*1024;
        // $input['Size'] = $totalSize;        
        // $this->sqlUpdateVolumeSize($input);
        // connectDB::$dbh->commit();
        if(LogAction::createLog(array('Type'=>1,'Level'=>1,'Code'=>'01101401','Riser'=>'admin','Message'=>'Add Volume('.$input['Alias'].') of Pair ID '.$input['PairID']." Task Success."),$lastID)){
                LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
        } 
        return CPAPIStatus::AddClusterVolumesSuccess;
    }

    function insertAddVolumeFailTaskShell($input)
    {
        $cmd = $this->cmdVDTaskErrTask;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= '"'.base64_encode('addglustervolume$*'.$input['CephID'].'$*'.$input['PairID'].'$*'.$input['RAIDID'].'$*'.$input['Alias']).'" ';
        $cmd .= 'glusterVolumeAdd';
        exec($cmd,$output,$rtn);   
    }

    function addVolumeShell($input)
    {
        $cmd = $this->cmdVDTaskGlusterVolumeAdd;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);  
        $cmd .= '"'.base64_encode('addglustervolume$*'.$input['CephID'].'$*'.$input['PairID'].'$*'.$input['RAIDID'].'$*'.$input['Alias']).'" '; 
        // if($input['ClearData'])
        //     $cmd .= '1 ';
        // else
        //     $cmd .= '0 '; 
        $cmd .= '1 ';     
        $cmd .= $input['PairID'].' ';
        $cmd .= $input['RAIDID'].' ';
        $cmd .= $input['DomainID'].' ';
        $this->debug($cmd);
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($rtn);
        return $rtn;
    }

    function reportVDTaskGlusterVolumeAdd($input)
    {
        $this->processReportAddGlusterVolumeData($input,$data);
        $rtn=99;
        $cancelTask=false;
        if(!$this->connectDB()){           
            $rtn=98;
            goto rtnFilnal;
        }
        if($data['ErrCode'] == 0){  
            if(!$this->sqlGetUUID($outputUUID))
            {
                $rtn=98;
                goto rtnFilnal;
            }
            $nodeC = new NodeAction();
            $data['Name'] = $input['RAIDID'];
            $data['Size'] = 0;        
            if(!$nodeC->sqlListServersByPairID($data['PairID'],$outputNodes)){
                $rtn=1;
                goto rtnFilnal;
            }            
            $this->sqlListVolumes($outputVolumes);
            connectDB::$dbh->beginTransaction();               
            $haveVolume = false;            
            foreach ($outputVolumes as $value) {                
                if($value['VolumeName'] == $data['RAIDID']){
                    $volumeID = $value['VolumeID'];
                    $haveVolume = true;
                    break;
                }                
            }
            // var_dump($haveVolume);
            if(!$haveVolume){            
                $volumeID = $outputUUID['UUID'];
                $addVolume = array();
                $addVolume['VolumeID'] = $volumeID;
                $addVolume['Name'] = $data['RAIDID'];
                $addVolume['Size'] = 0;
                $addVolume['Alias'] = $data['Alias'];
                // var_dump($addVolume);
                if(!$this->sqlInsertVolume($addVolume)){
                    $rtn = 2; 
                    goto rtnRollback;
                }
            }                               
            $data['VolumeID'] = $volumeID;
            foreach ($outputNodes as $value) {
                if(!$this->sqlInsertVolumeNodeMapping($data['VolumeID'],$value['NodeID'])){
                    $rtn = 3; 
                    goto rtnRollback;
                }
            }
            $this->listClusterVolumesShell($data,$outputVolumes);
            $volumeIndex = (int)$data['RAIDID'];
            $totalSize = $outputVolumes[$volumeIndex]['total']*1024*1024;
            $data['Size'] = $totalSize;        
            $this->sqlUpdateVolumeSize($data);
            connectDB::$dbh->commit();
            if(LogAction::createLog(array('Type'=>1,'Level'=>1,'Code'=>'01101403','Riser'=>'admin','Message'=>'Add Volume('.$data['Alias'].') of Pair ID '.$data['PairID']." Success."),$lastID)){
                    LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            } 
            $rtn = 0;                
            goto rtnFilnal;             
        }
        else{
            if($data['ErrCode'] == 210){                
                $cancelTask = true;
                if(LogAction::createLog(array('Type'=>1,'Level'=>1,'Code'=>'01101402','Riser'=>'admin','Message'=>'Cancel Add Volume ('.$data['Alias'].') of Pair ID '.$data['PairID']." Success."),$lastID)){
                    LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
                }                
            }
            if(LogAction::createLog(array('Type'=>1,'Level'=>1,'Code'=>'01301502','Riser'=>'admin','Message'=>'Failed to Add Volume ('.$data['Alias'].') of Pair ID '.$data['PairID'].'('.$data['ErrCode'].")."),$lastID)){
                    LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            } 
            $rtn = 0;                
            goto rtnFilnal;             
        }
        rtnRollback:       
            connectDB::$dbh->rollBack();     
            goto rtnFilnal;
        rtnFilnal:
            exit($rtn);
    }

    function setUPS($input,&$output)
    {
        $cmd = $this->cmdUPSSet;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);        
        $cmd .= '"'.$input['IP'].'"';
        $cmd .= ' ';        
        $cmd .= '"'.$input['DelayTime'].'"';        
        exec($cmd,$outputArr,$rtn);        
        if($rtn != 0)
            return CPAPIStatus::SetUPSFail;
        else{                        
            $output = $this->listUPSShell($input);
            return CPAPIStatus::SetUPSSuccess;
        }
    }

    function changeUPSJyson($input)
    {
        $output = array();
        $output['IP'] = $input['ip'];        
        if($input['delay_time'] == "")
            $input['delay_time'] = 60;
        else
            $input['delay_time'] = (int)$input['delay_time'];
        $output['DelayTime'] = $input['delay_time'];
        $output['BatteryStatus'] = $this->upsBatteryStausEnum[$input['battery_status']];
        $output['BatteryTime'] = (int)$input['battery_time'];        
        $output['BatteryChargeCondition'] = (int)$input['battery_charge_condition'];
        return $output;
    }

    function setVolumeDedupe($input)
    {
        if(!$this->connectDB()){           
            return CPAPIStatus::DBConnectFail;
        }
        $this->sqlListVolumes($outputVolumes);   
        // var_dump($outputVolumes);
        // var_dump($input['Volume']);
        if(count($input['Volume']) != count($outputVolumes))
            return CPAPIStatus::SetClusterDedupFail;     
        $setDedupe['Dedupe'] = array(false,false,false,false,false,false,false);
        $logAppendMsg = '';
        foreach ($input['Volume'] as $value) {
            foreach ($outputVolumes as $volume) {
                if($value['VolumeID'] == $volume['VolumeID']){
                    $index = (int)$volume['VolumeName'] - 1;
                    $setDedupe['Dedupe'][$index] = $value['Dedupe'];
                    $logDedupe = $value['Dedupe'] == true ? 'on' : 'off';
                    if($volume['VolumeName'] == 1)
                        $logAppendMsg .= 'Primary:'.$logDedupe.' ';
                    else
                        $logAppendMsg .= 'Additional Volume '.$volume['VolumeName'].':'.$logDedupe.' ';
                }
            }
        }
        $logAppendMsg = trim($logAppendMsg);
        $setDedupe['ConnectIP'] = $input['ConnectIP'];
        if($this->setVolumeDedupeShell($setDedupe) != 0 ){
            if(LogAction::createLog(array('Type'=>1,'Level'=>3,'Code'=>'01301401','Riser'=>'admin','Message'=>"Failed to set Dedupe($logAppendMsg)"),$lastID)){
                LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            }  
            return CPAPIStatus::SetClusterDedupFail;
        }
        if(LogAction::createLog(array('Type'=>1,'Level'=>1,'Code'=>'01101301','Riser'=>'admin','Message'=>"Set Dedupe($logAppendMsg) Success"),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
        }  
        return CPAPIStatus::SetClusterDedupSuccess;
    }   

    function setVolumeDedupeShell($input)
    {
        $cmd = $this->cmdClusterDedupSet;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd); 
        foreach ($input['Dedupe'] as $value) {
            $cmd .=  $value == true ? 'on ' : 'off ';
        }
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($rtn);
        return $rtn;
    }

    function sqlUpdateVolumeSize($input)
    {
        $result = false;       
        $sqlClear = "UPDATE tbVolumeSet SET sizeVolume=:sizeVolume WHERE name=:name";
        try
        {                            
            $sth = connectDB::$dbh->prepare($sqlClear);       
            $sth->bindValue(':sizeVolume', $input['Size'], PDO::PARAM_INT);
            $sth->bindValue(':name', $input['Name'], PDO::PARAM_STR);     
            if($sth->execute())
            {                
                $result = true;
            }                                       
        }
        catch (Exception $e){

        }                        
        return $result;
    }

    function locatePair($input)
    {
        $cmd = $this->cmdClusterLocateGroup;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= $input['PairID'];
        exec($cmd,$outputArr,$rtn);
        if($rtn != 0)
            return CPAPIStatus::LocatePairFail;
        return CPAPIStatus::LocatePairSuccess;
    }

    function locateNode($input)
    {
        $nodeC = new NodeAction();
        if(!$this->connectDB()){           
            return CPAPIStatus::DBConnectFail;
        }
        $nodeC->sqlListServerByidVDServer($input['NodeID'],$output);
        if(is_null($output))
            return CPAPIStatus::LocateNodeFail;
        $cmd = $this->cmdClusterLocateNode;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= $output['CommunicateIP'];        
        exec($cmd,$outputArr,$rtn);
        if($rtn != 0)
            return CPAPIStatus::LocateNodeFail;
        return CPAPIStatus::LocateNodeSuccess;
    }

    function setMail($input)
    {
        $cmd = $this->cmdClusterMailSSMTPSet;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= '"'.$input['SMTP'].'" ';         
        $cmd .= '"'.$input['Port'].'" ';
        $cmd .= '"'.$input['User'].'" ';
        $cmd .= '"'.$input['Password'].'" ';
        $tls = $input['TLS'] == true ? 'yes ' : 'no ';
        $cmd .= $tls;
        $cmd .= '"'.$input['Sender'].'" ';
        $enable = $input['Enable'] == true ? 'enable' : 'disable';
        $cmd .=$enable.' ';
        $cmd .= '"'.$input['Receiver1'].'" ';
        $cmd .= '"'.$input['Receiver2'].'" ';
        $cmd .= '"'.$input['Receiver3'].'" ';
        $cmd .= '"'.$input['Receiver4'].'" ';
        $cmd .= '"'.$input['Receiver5'].'" ';                
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($rtn);
        if($rtn != 0)
            return CPAPIStatus::SetMailFail;
        return CPAPIStatus::SetMailSuccess;
    }

    function listMail($input,&$output)
    {
        $cmd = $this->cmdClusterMailSSMTPList;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        exec($cmd,$outputArr,$rtn);
        if($rtn != 0)
            return CPAPIStatus::ListMailFail;
        $outputJson = json_decode($outputArr[0],true);
        $output = array();
        $output['Enable'] = $outputJson['enable'] == 'enable' ? true : false;
        $output['SMTP'] = $outputJson['ssmtp'];
        $output['Port'] = $outputJson['port'];
        $output['User'] = $outputJson['user'];
        $output['Password'] = '';
        $output['TLS'] = $outputJson['tls'] == 'yes' ? true : false;
        $output['Sender'] = $outputJson['sender'];
        $output['Receiver1'] = $outputJson['reciver1'];
        $output['Receiver2'] = $outputJson['reciver2'];
        $output['Receiver3'] = $outputJson['reciver3'];
        $output['Receiver4'] = $outputJson['reciver4'];
        $output['Receiver5'] = $outputJson['reciver5'];
        return CPAPIStatus::ListMailSuccess;
    }

    function sendTestMail($input)
    {
        $cmd = $this->cmdClusterMailTest;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        exec($cmd,$outputArr,$rtn);
        if($rtn != 0)
            return CPAPIStatus::SendTestMailFail;
        return CPAPIStatus::SendTestMailSuccess;
    }

    function glusterSkipWatingGroupReady($input,&$output)
    {
        $cmd = $this->cmdClusterVMSSkipWaitGroupReady;         
        $cmd = str_replace ( 'ip1' , $input['ConnectIP'], $cmd);
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($rtn);
        if($rtn != 0)
            return CPAPIStatus::SkipGlusterWaitGroupFail;
        return CPAPIStatus::SkipGlusterWaitGroupSuccess;
    }

    function listGlusterNotReadyNode($input,&$output)
    {        
        if(!$this->connectDB()){           
            return CPAPIStatus::ListGlusterNotReadyNodeFail;
        }
        $cmd = $this->cmdClusterVMSListReadyGroup;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        exec($cmd,$outputArr,$rtn);
        if($rtn != 0)
            return CPAPIStatus::ListGlusterNotReadyNodeFail;
        $nodeC=new NodeAction();
        $outputPair = json_decode($outputArr[0],true);
        $output=array();
        foreach ($outputPair as $value) {
            if($value['status']=='notReady'){
                $nodeC->sqlListServersByPairID($value['groupId'],$outputNodes);
                foreach ($outputNodes as $value1) {
                    $output[]=array('NodeName'=>$value1['NodeName'],'CommunicateIP'=>$value1['CommunicateIP'],'PairID'=>(int)$value['groupId']);
                }
            }
        }
        return CPAPIStatus::ListGlusterNotReadyNodeSuccess;
    }    

    function listDNS($input,&$output)
    {
        $cmd = $this->cmdClusterDNSList;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);        
        exec($cmd,$outputArr,$rtn);        
        if($rtn != 0)
            return CPAPIStatus::ListDNSFail;
        else{
            $outputDNS = json_decode($outputArr[0],true);
            if(!isset($outputDNS['dns'])){
                return CPAPIStatus::ListDNSFail;   
            }
            $output = array('DNS'=>$outputDNS['dns']);
            return CPAPIStatus::ListDNSSuccess;
        }
        return CPAPIStatus::ListDNSSuccess;
    }

    function setDNS($input,&$output)
    {
        $cmd = $this->cmdClusterDNSSet;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);        
        $cmd .= '"'.$input['DNS'].'"';
        exec($cmd,$outputArr,$rtn);        
        if($rtn != 0){
            if($rtn == 2){
                $logAppendMsg = 'Failed to ping DNS';
            }else{
                $logAppendMsg = $rtn;
            }
            if(LogAction::createLog(array('Type'=>1,'Level'=>3,'Code'=>'01301802','Riser'=>'admin','Message'=>"Failed to Set DNS(".$input['DNS'].")($logAppendMsg)"),$lastID)){
                LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            }  
            return CPAPIStatus::SetDNSFail;
        }else{
            if(LogAction::createLog(array('Type'=>1,'Level'=>1,'Code'=>'01101801','Riser'=>'admin','Message'=>"Set DNS(".$input['DNS'].") Success."),$lastID)){
                LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            }  
            return CPAPIStatus::SetDNSSuccess;
        }    
    }

    function listVMSIP($input,&$output)
    {
        $this->listVMSIPListShell($outputIPInfo,$input);        
        if(is_null($outputIPInfo))
            return CPAPIStatus::ListVMSIPFail;
        else{
            if(!isset($outputIPInfo['ip']) && !isset($outputIPInfo['mask'])){
                return CPAPIStatus::ListVMSIPFail;
            }
            else{                
                $output['VMSIP'] = $outputIPInfo['ip'];
                $output['VMSMask'] = $outputIPInfo['mask'];
                return CPAPIStatus::ListVMSIPSuccess;
            }
        }        
    }

    function setVMSIP($input)
    {
        if (filter_var($input['VMSIP'], FILTER_VALIDATE_IP) === false){
            return CPAPIStatus::SetVMSIPFail;
        }
        if(filter_var($input['VMSMask'], FILTER_VALIDATE_IP) === false){            
            return CPAPIStatus::SetVMSIPFail;
        }
        $cmd = $this->cmdClusterVMSIPSet;
        $cmd = str_replace ( 'ip ' , $input['ConnectIP'].' ', $cmd);        
        $cmd .= '"'.$input['VMSIP'].'" ';
        $cmd .= '"'.$input['VMSMask'].'" ';
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);        
        if($rtn != 0){
            $logAppendMsg = $rtn;            
            if(LogAction::createLog(array('Type'=>1,'Level'=>3,'Code'=>'01301902','Riser'=>'admin','Message'=>"Failed to Set Gluster VMS IP(".$input['VMSIP'].")($logAppendMsg)"),$lastID)){
                LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            }  
            return CPAPIStatus::SetVMSIPFail;
        }else{
            // if(LogAction::createLog(array('Type'=>1,'Level'=>1,'Code'=>'01101901','Riser'=>'admin','Message'=>"Set Gluster VMS IP(".$input['VMSIP'].") Success."),$lastID)){
            //     LogAction::sqlInsertDomainLogRelation($lastID,$input['DomainID']);
            // }  
            return CPAPIStatus::SetVMSIPSuccess;
        }
    }

	function processGlusterErrorCode($code){  
        $this->baseOutputResponse($code);
        switch ($code){           
            case CPAPIStatus::AddClusterFail:    	
            case CPAPIStatus::DeleteClusterFail:
            case CPAPIStatus::SetClusterAlarmFail:
            case CPAPIStatus::SetClusterAutosuspendFail;
            case CPAPIStatus::SetClusterSambaFail:
            case CPAPIStatus::ListClusterDedupFail:
            case CPAPIStatus::SetClusterDedupFail:
            case CPAPIStatus::AddClusterVolumesFail:
            case CPAPIStatus::ListClusterVolumesFail:
            case CPAPIStatus::SetUPSFail:
            case CPAPIStatus::AddClusterGroupFail:
            case CPAPIStatus::DeleteClusterGroupFail:
            case CPAPIStatus::ReplaceClusterNodeFail:
            case CPAPIStatus::SetClusterGroupMasterFail:
            case CPAPIStatus::ReserveNodeFail:
            case CPAPIStatus::ListGlusterSummaryFail:
            case CPAPIStatus::SetNodeMaintainanceFail:
            case CPAPIStatus::splitProcessFail:
            case CPAPIStatus::LocateNodeFail:
            case CPAPIStatus::LocatePairFail:
            case CPAPIStatus::SkipGlusterWaitGroupFail:
            case CPAPIStatus::ListGlusterNotReadyNodeFail:
            case CPAPIStatus::ListMailFail:
            case CPAPIStatus::SetMailFail:
            case CPAPIStatus::SendTestMailFail:
            case CPAPIStatus::SetLocalMasterFail:
            case CPAPIStatus::RestoreGlusterFail:
            case CPAPIStatus::ListDNSFail:
            case CPAPIStatus::SetDNSFail:
            case CPAPIStatus::ListVMSIPFail:
            case CPAPIStatus::SetVMSIPFail:
            case CPAPIStatus::ListMiscellaneousFail:
                http_response_code(400);
                break; 
        }
    }
}