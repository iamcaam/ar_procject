<?php
class JobAction extends BaseAPI
{
	function __construct()
	{	    
	}

	function logJobAppendErr($code)
    {
        $appendErr = null;
        switch ($code) {
            case '09300201':    
            case '09300601':   
           	case '09300402':   
                $appendErr = '(Failed to list domain)';
                break;     
            case '09300202':      
           	case '09300602':     
                $appendErr = '(Failed to list schedule)';
                break;     
            case '09300203':
            case '09300603':
                $appendErr = '(Name is duplicated)';
                break;
            case '09300204':
            case '09300604':
                $appendErr = '(Failed to insert job base)';
                break;
            case '09300605':
            	$appendErr = '(Failed to insert backup space)';
                break;
            case '09300606':
            	$appendErr = '(Failed to insert backup job)';
            	break;
            case '09300205':
                $appendErr = '(Failed to insert snapshot job)';
                break;
            case '09300206':
                $appendErr = '(Failed to insert snapshot job and vd mapping)';
                break;          
            case '09300607':
            	$appendErr = '(Failed to insert backup job and vd mapping)';
            	break;  
            case '09300401':
            	$appendErr = '(Failed to list job)';
            	break;
            case '09300207':
            case '09300403':
                $appendErr = '';
                break;
        }
        return $appendErr;
    }

	function createJob($input,&$output)
	{
	    if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }   
	    switch ((int)$input['JobType']) {
	        case 1:
	            $responsecode = $this->createSnapshotJob($input,$output);
	            return $responsecode;
	            break;
	        case 2:
	        	$responsecode = $this->createBackupJob($input,$output);	        	
	            return $responsecode;
	            break;
	        case 4:
	        	$responsecode = $this->createSnapshotUPjob($input,$output);	        	
	            return $responsecode;
	        	break;
	        case 8:
	        	$responsecode = $this->createBackupUPJob($input,$output);	        	
	            return $responsecode;
	        	break;
	        default:
	            break;
	    }
	}

	function createSnapshotJob($input,&$output)
	{
		$logCode = '';		
        if(!$this->sqlGetUUID($outputUUID))
            return CPAPIStatus::DBConnectFail;	    
	    $scheduleC = new ScheduleAction();
	    $outputSchedule = null;	    
	    $domainC = new DomainAction();
	    $outputDomain = null;
	    $domainC->sqlListDomainByID($input['DomainID'], $outputDomain);
	    if (is_null($outputDomain)) {
	        $logCode = '09300201';
	    	goto fail;
	    }
	    $scheduleC->sql_select_schedule_by_idDomain_idSchedule($input['ScheduleID'], $input['DomainID'], $outputSchedule);
	    if (is_null($outputSchedule)) {
	        $logCode = '09300202';
	    	goto fail;
	    }
	    // $outputJob = null;
	    // $this->sqlListJobByidScheduleName($input['ScheduleID'], $input['Name'], $outputJob);
	    // if (!is_null($outputJob)) {
	    //     $logCode = '09300203';
	    // 	goto fail;
	    // }
	    $outputJob_id = null;
	    connectDB::$dbh->beginTransaction();	  
	    $input['JobID'] = $outputUUID['UUID'];
	    if (!$this->sqlInsertJobbase($input)) {
	        $logCode = '09300204';
	    	goto fail_rollback;
	    }
	    if (!$this->sqlInserttbsnapshotjobset($input['JobID'])) {
	        $logCode = '09300205';
	    	goto fail_rollback;	 
	    }
	    $vd_c = new VDAction();
	    foreach ($input['VDs'] as $value) {
	    	$output_check = null;
	    	$vd_c->sql_list_vd_userid_by_id($value,$output_check);	    	
	    	if(!is_null($output_check)){
	    		if(!$this->sqlInserttbssjobvdmappingset($input['JobID'],$value)){
	    			$logCode = '09300206';
	    			goto fail_rollback;	    		
	    		}
	    	}	    	
	    }
	    $output = null;
	    $this->sqlListJobbaseByidJob($input['JobID'],$output);
	    if(is_null($output)){
	    	$logCode = '09300207';
	    	goto fail_rollback;	 	    	
	    }
	    // var_dump($output);
	    $outputVDs = null;
	    $this->sqlListSnapshotJobVDsByidJob($input['JobID'],$outputVDs);
	    if(is_null($outputVDs)){
	    	$logCode = '09300207';
	    	goto fail_rollback;	    	
	    }
	    // var_dump($outputVDs);
	    $output['VDs'] = $outputVDs;
	    // var_dump($output);
	    connectDB::$dbh->commit();
	    $logCode = '09100101';	 
		$rtn = CPAPIStatus::CreateJobSuccess;
		goto end;
		fail:
            $rtn = CPAPIStatus::CreateJobFail;
            goto end;
        fail_rollback:                
            connectDB::$dbh->rollBack();           
            $rtn = CPAPIStatus::CreateJobFail;
            goto end;
        end:
            $appendErr = $this->logJobAppendErr($logCode);
            $this->insertCreateSnapshotJobLog($logCode,$input['Name'],$outputDomain['Name'],$input['DomainID'],$appendErr);
            return $rtn;    
	}	

	function createSnapshotUPjob($input,&$output)
	{
		// var_dump($input);
		$logCode = '';
		if(!$this->sqlGetUUID($outputUUID))
            return CPAPIStatus::DBConnectFail;       
	    $scheduleC = new ScheduleAction();
	    $outputSchedule = null;	    
	    $domainC = new DomainAction();
	    $outputDomain = null;
	    $domainC->sqlListDomainByID($input['DomainID'], $outputDomain);
	    if (is_null($outputDomain)) {
	        $logCode = '09300201';
	    	goto fail;
	    }
	    $scheduleC->sql_select_schedule_by_idDomain_idSchedule($input['ScheduleID'], $input['DomainID'], $output_schedule);
	    if (is_null($output_schedule)) {
	        $logCode = '09300202';
	    	goto fail;
	    }
	    $outputJob = null;
	    // $this->sqlListJobByidScheduleName($input['ScheduleID'], $input['Name'], $output_job);
	    // if (!is_null($outputJob)) {
	    //     $logCode = '09300203';
	    // 	goto fail;
	    // }
	    $outputJob_id = null;
	    connectDB::$dbh->beginTransaction();	  
	    $input['JobID'] = $outputUUID['UUID'];
	    if (!$this->sqlInsertJobbase($input)) {
	        $logCode = '09300204';
	    	goto fail_rollback;
	    }
	    if (!$this->sqlInserttbsnapshotjobset($input['JobID'])) {
	        $logCode = '09300205';
	    	goto fail_rollback;
	    }	    
	    foreach ($input['Users'] as $value) {
	    	$outputCheck = null;	    	
	    	$this->sqlCheckUserHaveUserDisk($value,$outputCheck);	    	
	    	if(!is_null($outputCheck)){	    					
	    		if(!$this->sqlInserttbSSJobUserDiskMappingSet($input['JobID'],$outputCheck['DiskID'])){
	    			$logCode = '09300206';
	    			goto fail_rollback;			
	    		}
	    	}	    	
	    }
	    $output = null;
	    $this->sqlListJobbaseByidJob($input['JobID'],$output);
	    if(is_null($output)){
	    	$logCode = '09300207';
	    	goto fail_rollback;		
	    }
	    // var_dump($output);
	    $outputUsers = null;
	    $this->sqlListSnapshotJobUserIDbyidJob($input['JobID'],$outputUsers);
	    if(is_null($outputUsers)){
	    	$logCode = '09300207';
	    	goto fail_rollback;	
	    }
	    // var_dump($output_vds);
	    $output['Users'] = $outputUsers;
	    // var_dump($output);
	    connectDB::$dbh->commit();
	    $logCode = '09100101';	 
		$rtn = CPAPIStatus::CreateJobSuccess;
		goto end;
		fail:
            $rtn = CPAPIStatus::CreateJobFail;
            goto end;
        fail_rollback:                
            connectDB::$dbh->rollBack();           
            $rtn = CPAPIStatus::CreateJobFail;
            goto end;
        end:
            $appendErr = $this->logJobAppendErr($logCode);
            $this->insertCreateSnapshotJobLog($logCode,$input['Name'],$outputDomain['Name'],$input['DomainID'],$appendErr,true);
            return $rtn;    		
	}

	function insertCreateSnapshotJobLog($code,$vdName,$domainName,$domainID,$appendErr,$isVDisk=false)
    {
        $logType = 9;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);   
        $userDiskStr="";
        if($isVDisk)
        	$userDiskStr=" User Disk";
        if(is_null($appendErr)){
            $message = "Create snapshot$userDiskStr job$appendVDDomain Success";
        }
        else{
            $message = "Failed to create snapshot$userDiskStr job$appendVDDomain$appendErr";
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function sqlInserttbSSJobUserDiskMappingSet($idJob,$idVDisk)
    {
    	$result = false;
        $sqlInsert = <<<SQL
           INSERT tbSSJobUserDiskMappingSet (job_id,idVDisk) Values (:job_id,:idVDisk)
SQL;
        try {
            $sth = connectDB::$dbh->prepare($sqlInsert);            
            $sth->bindValue(':job_id', $idJob, PDO::PARAM_STR);
            $sth->bindValue(':idVDisk', $idVDisk, PDO::PARAM_INT);
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

    function sqlListSnapshotJobUserIDbyidJob($idJob,&$output)
	{
		$SQLSelect = <<<SQL
            SELECT t2.idVDisk,t4.nameVDisk,t3.nameUser,t3.idUser
            from tbSSJobUserDiskMappingSet as t1
            INNER JOIN tbuserdiskset as t2
            ON t1.idVDisk=t2.idVDisk
            INNER JOIN tbvdiskset as t4
            ON t1.idVDisk=t4.idVDisk
            INNER JOIN tbuserbaseset as t3
            on t2.idUser=t3.idUser            
            WHERE t1.job_id = :job_id            
SQL;
		try{
			$sth = connectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':job_id', $idJob, PDO::PARAM_STR);            
            if($sth->execute())
            {                                
            	$output = array();
            	while( $row = $sth->fetch() ) 
                {
                	// $output[] = array("VDID"=>$row['idVD'],"Name"=>$row['nameVD']);      
                	$output[] = (int)$row['idUser'];
                }
            }                           
        }
        catch (Exception $e){                             
        }     
	}

    function createBackupJob($input,&$output)
    {
    	$logCode = '';		
        if(!$this->sqlGetUUID($outputUUID))
            return CPAPIStatus::DBConnectFail;
        $backupC = new BackupAction();
        $backupC->backupList($input,$outputBackup);
        if($outputBackup['ID'] == 0)
            return CPAPIStatus::CreateJobFail;     
        $lastBackupID = $outputBackup['ID'];    
        $scheduleC = new ScheduleAction();
	    $outputSchedule = null;	    
	    $domainC = new DomainAction();
	    $outputDomain = null;
	    $domainC->sqlListDomainByID($input['DomainID'], $outputDomain);
	    if (is_null($outputDomain)) {
	        $logCode = '09300601';
	    	goto fail;
	    }
	    $scheduleC->sql_select_schedule_by_idDomain_idSchedule($input['ScheduleID'], $input['DomainID'], $outputSchedule);
	    if (is_null($outputSchedule)) {
	        $logCode = '09300602';
	    	goto fail;
	    }
	    $outputJob = null;
	    // $this->sqlListJobByidScheduleName($input['ScheduleID'], $input['Name'], $outputJob);
	    // if (!is_null($outputJob)) {
	    //     $logCode = '09300603';
	    // 	goto fail;
	    // }
	    $outputJob_id = null;
	    connectDB::$dbh->beginTransaction();	  
	    $input['JobID'] = $outputUUID['UUID'];
	    if (!$this->sqlInsertJobbase($input)) {
	        $logCode = '09300604';
	    	goto fail_rollback;
	    }	    	                  
	    if (!$this->sqlInserttbbackupjobset($input['JobID'],$lastBackupID)) {
	        $logCode = '09300606';
	    	goto fail_rollback;	 
	    }
	    $vd_c = new VDAction();
	    foreach ($input['VDs'] as $value) {
	    	$output_check = null;
	    	$vd_c->sql_list_vd_userid_by_id($value,$output_check);	    	
	    	if(!is_null($output_check)){
	    		if(!$this->sqlInserttbbackupjobvdmappingset($input['JobID'],$value)){
	    			$logCode = '09300607';
	    			goto fail_rollback;	    		
	    		}
	    	}	    	
	    }	   	
	    $output = null;
	    $this->sqlListJobbaseByidJob($input['JobID'],$output);
	    if(is_null($output)){
	    	$logCode = '09300607';
	    	goto fail_rollback;	 	    	
	    }	    
	    $outputVDs = null;
	    $this->sqlListBackupJobVDsByidJob($input['JobID'],$outputVDs);
	    if(is_null($outputVDs)){
	    	$logCode = '09300607';
	    	goto fail_rollback;	    	
	    }	    
	    $output['VDs'] = $outputVDs;	   	
	    connectDB::$dbh->commit();
	    $logCode = '09100501';	 
		$rtn = CPAPIStatus::CreateJobSuccess;
		goto end;
		fail:
            $rtn = CPAPIStatus::CreateJobFail;
            goto end;
        fail_rollback:                
            connectDB::$dbh->rollBack();           
            $rtn = CPAPIStatus::CreateJobFail;
            goto end;
        end:
        	// var_dump($logCode);
            $appendErr = $this->logJobAppendErr($logCode);
            $this->insertCreateBackupJobLog($logCode,$input['Name'],$outputDomain['Name'],$input['DomainID'],$appendErr);
            return $rtn;
    }

    function createBackupUPJob($input,&$output)
    {
    	$logCode = '';		
        if(!$this->sqlGetUUID($outputUUID))
            return CPAPIStatus::DBConnectFail;
        $backupC = new BackupAction();
        $backupC->backupList($input,$outputBackup);
        $lastBackupID = $outputBackup['ID'];
        if($outputBackup['ID'] == 0)
            return CPAPIStatus::CreateJobFail;                     
        $scheduleC = new ScheduleAction();	    
	    $domainC = new DomainAction();
	    $outputSchedule = null;	    
	    $outputDomain = null;
	    $domainC->sqlListDomainByID($input['DomainID'], $outputDomain);
	    if (is_null($outputDomain)) {
	        $logCode = '09300601';
	    	goto fail;
	    }
	    $scheduleC->sql_select_schedule_by_idDomain_idSchedule($input['ScheduleID'], $input['DomainID'], $outputSchedule);
	    if (is_null($outputSchedule)) {
	        $logCode = '09300602';
	    	goto fail;
	    }
	    // $outputJob = null;
	    // $this->sqlListJobByidScheduleName($input['ScheduleID'], $input['Name'], $outputJob);
	    // if (!is_null($outputJob)) {
	    //     $logCode = '09300603';
	    // 	goto fail;
	    // }
	    $outputJob_id = null;
	    connectDB::$dbh->beginTransaction();	  
	    $input['JobID'] = $outputUUID['UUID'];
	    if (!$this->sqlInsertJobbase($input)) {
	        $logCode = '09300604';
	    	goto fail_rollback;
	    }	    	                  
	    if (!$this->sqlInserttbbackupjobset($input['JobID'],$lastBackupID)) {
	        $logCode = '09300606';
	    	goto fail_rollback;	 
	    }	    
	    foreach ($input['Users'] as $value) {
	    	$outputCheck = null;
	    	$this->sqlCheckUserHaveUserDisk($value,$outputCheck);	    	
	    	if(!is_null($outputCheck)){
	    		// var_dump($input);
	    		// var_dump($value);
	    		if(!$this->sqlInserttbBackupJobUserDiskMappingSet($input['JobID'],$outputCheck['DiskID'])){
	    			$logCode = '09300607';
	    			goto fail_rollback;	    		
	    		}
	    	}	    	
	    }	   	
	    $output = null;
	    $this->sqlListJobbaseByidJob($input['JobID'],$output);
	    if(is_null($output)){
	    	$logCode = '09300607';
	    	goto fail_rollback;	 	    	
	    }	    
	    $outputUsers = null;
	    $this->sqlListBackupJobUserIDByidJob($input['JobID'],$outputUsers);
	    if(is_null($outputUsers)){
	    	$logCode = '09300607';
	    	goto fail_rollback;	    	
	    }	    
	    $output['Users'] = $outputUsers;	   	
	    connectDB::$dbh->commit();
	    $logCode = '09100501';	 
		$rtn = CPAPIStatus::CreateJobSuccess;
		goto end;
		fail:
            $rtn = CPAPIStatus::CreateJobFail;
            goto end;
        fail_rollback:                
            connectDB::$dbh->rollBack();           
            $rtn = CPAPIStatus::CreateJobFail;
            goto end;
        end:
        	// var_dump($logCode);
            $appendErr = $this->logJobAppendErr($logCode,true);
            $this->insertCreateBackupJobLog($logCode,$input['Name'],$outputDomain['Name'],$input['DomainID'],$appendErr,true);
            return $rtn;
    }

    function sqlInserttbBackupJobUserDiskMappingSet($idJob,$idVDisk)
    {
    	$result = false;
        $SQLInsert = <<<SQL
           INSERT tbBackupJobUserDiskMappingSet (job_id,idVDisk) Values (:job_id,:idVDisk)
SQL;
        try {
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':job_id', $idJob, PDO::PARAM_STR);
            $sth->bindValue(':idVDisk', $idVDisk, PDO::PARAM_INT);
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

    function sqlListBackupJobUserIDByidJob($idJob,&$output)
	{
		$sqlSelect = <<<SQL
            SELECT t3.idUser
            from tbBackupJobUserDiskMappingSet as t1
            INNER JOIN tbuserdiskset as t2
            ON t1.idVDisk=t2.idVDisk
            INNER JOIN tbvdiskset as t4
            ON t1.idVDisk=t4.idVDisk
            INNER JOIN tbuserbaseset as t3
            on t2.idUser=t3.idUser            
            WHERE t1.job_id = :job_id            
SQL;
		try{
			$sth = connectDB::$dbh->prepare($sqlSelect);                       
            $sth->bindValue(':job_id', $idJob, PDO::PARAM_STR);            
            if($sth->execute())
            {                                
            	$output = array();
            	while( $row = $sth->fetch() ) 
                {
                	// $output[] = array("VDID"=>$row['idVD'],"VDName"=>$row['nameVD']);      
                	$output[] = (int)$row['idUser'];
                }
            }                           
        }
        catch (Exception $e){                             
        }             
	}

    function insertCreateBackupJobLog($code,$vdName,$domainName,$domainID,$appendErr,$isVDisk)
    {
    	$logType = 9;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $userDiskStr = '';
        if($isVDisk)
        	$userDiskStr = ' User Disk';
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);   
        if(is_null($appendErr)){
            $message = "Create backup$userDiskStr job$appendVDDomain Success";
        }
        else{
            $message = "Failed to create backup$userDiskStr job$appendVDDomain$appendErr";
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

	function listJobDetail($input,&$output)
	{		
		if(!$this->connectDB()){
        	return CPAPIStatus::DBConnectFail;
        }   
	    $output = null;
	    $this->sqlListJobbaseByidJob($input['JobID'],$output);	
	    if(is_null($output)){
	    	return CPAPIStatus::ListJobDetailFail;
	    }	  
	    $outputVDs = null;	    
	    switch ($output['JobType']) {
	    	case 1:	    
	    	 	$this->sqlListSnapshotJobVDsByidJob($input['JobID'],$outputVDs);
	    		if(is_null($outputVDs)){	    	
	        		return CPAPIStatus::ListJobDetailFail;		
	    		}		
	    		$output['VDs'] = $outputVDs;
	    		return CPAPIStatus::ListJobDetailSuccess;
	    		break;	  
	    	case 2:
	    	 	$this->sqlListBackupJobVDsByidJob($input['JobID'],$outputVDs);
	    		if(is_null($outputVDs)){	    	
	        		return CPAPIStatus::ListJobDetailFail;		
	    		}		    		
	    		$this->sqlListBackupJobConfigByidJob($input['JobID'],$outputConfig);
	    		if(is_null($outputConfig)){	    	
	        		return CPAPIStatus::ListJobDetailFail;		
	    		}	    		
	    		$output['VDs'] = $outputVDs;
	    		$output['Config'] = $outputConfig;
	    		return CPAPIStatus::ListJobDetailSuccess;
	    		break;  	
	    	case 4:
	    		$this->sqlListSnapshotJobUserIDbyidJob($input['JobID'],$outputUsers);	    		
	    		if(is_null($outputUsers)){
	        		return CPAPIStatus::ListJobDetailFail;	    		
	    		}
	    		$output['Users'] = $outputUsers;
	    		return CPAPIStatus::ListJobDetailSuccess;
	    		break;
	    	case 8:
	    		$this->sqlListBackupJobUserIDByidJob($input['JobID'],$outputUsers);
	    		if(is_null($outputUsers)){
	        		return CPAPIStatus::ListJobDetailFail;	    		
	    		}$this->sqlListBackupJobConfigByidJob($input['JobID'],$outputConfig);
	    		if(is_null($outputConfig)){	    	
	        		return CPAPIStatus::ListJobDetailFail;		
	    		}	    		
	    		$output['Users'] = $outputUsers;
	    		$output['Config'] = $outputConfig;
	    		return CPAPIStatus::ListJobDetailSuccess;
	    		break;
	    	default:
	    		return CPAPIStatus::ListJobDetailFail;	
	    		break;
	    }
	}

	function deleteJob($input)
	{
		if(!$this->connectDB()){
	        return CPAPIStatus::DBConnectFail;
	    }	  
	    $outputJob = null;
	    $this->sqlListJobbaseByidJob($input['JobID'],$outputJob);
	    if(is_null($outputJob)){
	    	$logCode = '09300401';
	       	goto fail;
	    }
	    $input['Name'] = $outputJob['Name'];
	    $domainC = new DomainAction();
	    $outputDomain = null;
	    $domainC->sqlListDomainByID($input['DomainID'], $outputDomain);
	    if (is_null($outputDomain)) {
	    	$logCode = '09300402';
	       	goto fail;
	    }
	    $this->sqlDeleteJob($input['JobID']);
	    $outputJob = null;
	    $this->sqlListJobbaseByidJob($input['JobID'],$outputJob);
	    if(!is_null($outputJob)){
	    	$logCode = '09300403';
	    	goto fail;	    	
	    }	    
      	$logCode = '09100301';
      	$rtn = CPAPIStatus::DeleteJobSuccess;
      	goto end;
      	fail:
            $rtn = CPAPIStatus::DeleteJobFail;
            goto end;      
        end:
            $appendErr = $this->logJobAppendErr($logCode);
            $this->insertDeleteJobLog($logCode,$input['Name'],$outputDomain['Name'],$input['DomainID'],$appendErr);
            return $rtn;    
	}

	function insertDeleteJobLog($code,$vdName,$domainName,$domainID,$appendErr)
    {
        $logType = 9;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $appendVDDomain = $this->logappendVDDomain($vdName,$domainName);   
        if(is_null($appendErr)){
            $message = "Delete job$appendVDDomain Success";
        }
        else{
            $message = "Failed to delete job$appendVDDomain$appendErr";
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function modifyJob($input,&$output)
    {
    	if(!$this->connectDB()){
        	return CPAPIStatus::DBConnectFail;
        }   
        $this->sqlListJobbaseByidJob($input['JobID'],$outputJob);
        if(is_null($outputJob))
        	return CPAPIStatus::ModifyJobFail;
        switch ($outputJob['JobType']) {
        	case 1:
        		$this->sqlCleartbssjobvdmappingset($input['JobID']);
        		break;
        	case 2:
        		$this->sqlCleartbbackupjobvdmappingset($input['JobID']);
        		break;
        	case 4:
        		$this->sqlCleartbssjobuserdiskmappingset($input['JobID']);
        		break;
        	case 8:
        		$this->sqlCleartbbackupjobuserdiskmappingset($input['JobID']);
        		break;
        	default:
        		return CPAPIStatus::ModifyJobFail;
        		break;
        } 
    	switch ($outputJob['JobType']) {
    		case 1:
    		case 2:
				$this->modifyJobForVDs($outputJob['JobType'],$input);
    			break;
    		case 4:
    		case 8:
    			$input['Users'] = $input['VDs'];
    			// unset($input['VDs']);
    			$this->modifyJobForUserDisks($outputJob['JobType'],$input);
    			break;
    		default:    			
    			break;
    	}
    	// $vd_c = new VDAction();
    	// foreach ($input['VDs'] as $value) {
	    // 	$output_check = null;
	    // 	$vd_c->sql_list_vd_userid_by_id($value,$output_check);	    	
	    // 	if(!is_null($output_check)){
	    // 		switch ($outputJob['JobType']) {
		   //      	case 1:
			  //   		if(!$this->sqlInserttbssjobvdmappingset($input['JobID'],$value)){
			  //   			continue;
			  //   		}
			  //   		break;
			  //   	case 2:
			  //   		if(!$this->sqlInserttbbackupjobvdmappingset($input['JobID'],$value)){
			  //   			continue;
			  //   		}
			  //   		break;
			  //   }
	    // 	}	    	
	    // }
	    $responsecode = $this->listJobDetail($input,$output);
	    if($responsecode != CPAPIStatus::ListJobDetailSuccess)
	    	return CPAPIStatus::ModifyJobFail;
	    return CPAPIStatus::ModifyJobSuccess;
    }

    function modifyJobForVDs($jobType,$input)
    {
    	$vd_c = new VDAction();
    	foreach ($input['VDs'] as $value) {
	    	$output_check = null;
	    	$vd_c->sql_list_vd_userid_by_id($value,$output_check);	    	
	    	if(!is_null($output_check)){
	    		switch ($jobType) {
		        	case 1:
			    		if(!$this->sqlInserttbssjobvdmappingset($input['JobID'],$value)){
			    			continue;
			    		}
			    		break;
			    	case 2:
			    		if(!$this->sqlInserttbbackupjobvdmappingset($input['JobID'],$value)){
			    			continue;
			    		}
			    		break;
			    }
	    	}	    	
	    }
    }

    function modifyJobForUserDisks($jobType,$input)
    {
    	foreach ($input['Users'] as $value) {
	    	$outputCheck = null;
	    	$this->sqlCheckUserHaveUserDisk($value,$outputCheck);	    	
	    	if(!is_null($outputCheck)){	
	    		switch ($jobType) {
	    			case 4:
	    				$this->sqlInserttbSSJobUserDiskMappingSet($input['JobID'],$outputCheck['DiskID']);
	    				break;
	    			case 8:
	    				$this->sqlInserttbBackupJobUserDiskMappingSet($input['JobID'],$outputCheck['DiskID']);
	    				break;
	    			default:	    				
	    				break;
	    		}	    		
	    		
	    	}	    	
	    }	  
    }

	function sqlListJobbaseByidJob($idJob,&$output)
	{
		try {
	        $SQLSelect = "SELECT * FROM tbjobbaseset WHERE job_id=:job_id";
	        $sth = connectDB::$dbh->prepare($SQLSelect);
	        $sth->bindValue(':job_id', $idJob, PDO::PARAM_STR);	        
	        if ($sth->execute()) {
	            while ($row = $sth->fetch()) {
	                $output = array('SceduleID'=>(int)$row['schedule_id'],'JobID'=>$row['job_id'],'Name'=>$row['name'],'JobType'=>(int)$row['category_id'],'Desc'=>$row['description']);
	            }
	        }
	    } catch (Exception $e) {
	    }
	}

	function sqlListSnapshotJobVDsByidJob($idJob,&$output)
	{
		$SQLSelect = <<<SQL
            SELECT t2.idVD,t2.nameVD
            from tbssjobvdmappingset as t1
            INNER JOIN tbvdimagebaseset as t2
            ON t1.idVD=t2.idVD           
            WHERE t1.job_id = :job_id            
SQL;
		try{
			$sth = connectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':job_id', $idJob, PDO::PARAM_STR);            
            if($sth->execute())
            {                                
            	$output = array();
            	while( $row = $sth->fetch() ) 
                {
                	// $output[] = array("VDID"=>$row['idVD'],"VDName"=>$row['nameVD']);      
                	$output[] = $row['idVD'];      
                }
            }                           
        }
        catch (Exception $e){                             
        }             
	}

	function sqlListBackupJobVDsByidJob($idJob,&$output)
	{
		$SQLSelect = <<<SQL
            SELECT t2.idVD,t2.nameVD
            from tbbackupjobvdmappingset as t1
            INNER JOIN tbvdimagebaseset as t2
            ON t1.idVD=t2.idVD           
            WHERE t1.job_id = :job_id            
SQL;
		try{
			$sth = connectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':job_id', $idJob, PDO::PARAM_STR);            
            if($sth->execute())
            {                                
            	$output = array();
            	while( $row = $sth->fetch() ) 
                {
                	// $output[] = array("VDID"=>$row['idVD'],"VDName"=>$row['nameVD']);      
                	$output[] = $row['idVD'];      
                }
            }                           
        }
        catch (Exception $e){                             
        }             
	}

	function sqlListBackupJobConfigByidJob($idJob,&$output)
	{
		$output = null;
		$SQLSelect = "SELECT DES_DECRYPT(FROM_BASE64(t2.configSpace),'".DES_Key.
                "') as config from tbbackupjobset as t1 INNER JOIN tbbackupspaceset as t2 ON t1.idBackupSpace=t2.idBackupSpace WHERE t1.job_id = :job_id";
		try{
			$sth = connectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':job_id', $idJob, PDO::PARAM_STR);            
            if($sth->execute())
            {                                            	
            	while( $row = $sth->fetch() ) 
                {
                	// $output[] = array("VDID"=>$row['idVD'],"VDName"=>$row['nameVD']);      
                	$output = json_decode($row['config'],true);      
                }
            }                           
        }
        catch (Exception $e){                             
        }     
        // var_dump($output);
	}

	function sqlListJobByidScheduleName($idSchedule, $name, &$output)
	{
	    try {
	        $SQLSelect = "SELECT * FROM tbjobbaseset WHERE schedule_id=:schedule_id AND name=:name";
	        $sth = connectDB::$dbh->prepare($SQLSelect);
	        $sth->bindValue(':schedule_id', $idSchedule, PDO::PARAM_INT);
	        $sth->bindValue(':name', $name, PDO::PARAM_STR);
	        if ($sth->execute()) {
	            while ($row = $sth->fetch()) {
	                $output = array('SceduleID'=>(int)$row['schedule_id'],'JobID'=>$row['job_id'],'Name'=>$row['name'],'JobType'=>(int)$row['category_id'],'Desc'=>$row['description']);
	            }
	        }
	    } 
	    catch (Exception $e) {
	    }
	}

	function sqlInsertJobbase($input)
	{
	    $result = false;
	    $SQLInsert= <<<SQL
	           INSERT tbjobbaseset (job_id,name,schedule_id,description,category_id,date_created,date_modified) Values (:job_id,:name,:schedule_id,:description,:category_id,now(),now())
SQL;
	    try {
	        $sth = connectDB::$dbh->prepare($SQLInsert);
	        $sth->bindValue(':job_id', $input['JobID'], PDO::PARAM_STR);
	        $sth->bindValue(':name', $input['Name'], PDO::PARAM_STR);
	        $sth->bindValue(':schedule_id', $input['ScheduleID'], PDO::PARAM_INT);
	        $sth->bindValue(':description', $input['Desc'], PDO::PARAM_STR);
	        $sth->bindValue(':category_id', $input['JobType'], PDO::PARAM_INT);
	        if ($sth->execute()) {
	            if ($sth->rowCount()== 1) {
	                $result = true;	                
	            }
	        }
	    } catch (Exception $e) {
	    }
	    return $result;
	}	

    function sqlInserttbsnapshotjobset($idJob)
    {
        $result = false;
        $SQLInsert = <<<SQL
           INSERT tbsnapshotjobset (job_id) Values (:job_id)
SQL;
        try {        	
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':job_id', $idJob, PDO::PARAM_STR);
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

    function sqlInserttbssjobvdmappingset($idJob,$idVD)
    {
    	$result = false;
        $SQLInsert = <<<SQL
           INSERT tbssjobvdmappingset (job_id,idVD) Values (:job_id,:idVD)
SQL;
        try {
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':job_id', $idJob, PDO::PARAM_STR);
            $sth->bindValue(':idVD', $idVD, PDO::PARAM_STR);
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

    function sqlCleartbssjobvdmappingset($idJob)
    {
    	$result = false;
        $SQLDelete = <<<SQL
           DELETE FROM tbssjobvdmappingset WHERE job_id=:job_id
SQL;
        try {
            $sth = connectDB::$dbh->prepare($SQLDelete);            
            $sth->bindValue(':job_id', $idJob, PDO::PARAM_STR);            
            if($sth->execute()){             	        	
               	$result = true;                      	
            }
        }
        catch (Exception $e){                
        }        
        return $result;	
    }

    function sqlCleartbbackupjobvdmappingset($idJob)
    {
    	$result = false;
        $SQLDelete = <<<SQL
           DELETE FROM tbbackupjobvdmappingset WHERE job_id=:job_id
SQL;
        try {
            $sth = connectDB::$dbh->prepare($SQLDelete);            
            $sth->bindValue(':job_id', $idJob, PDO::PARAM_STR);            
            if($sth->execute()){             	        	
               	$result = true;                      	
            }
        }
        catch (Exception $e){                
        }        
        return $result;	
    }

    function sqlCleartbssjobuserdiskmappingset($idJob)
    {
    	$result = false;
        $SQLDelete = <<<SQL
           DELETE FROM tbssjobuserdiskmappingset WHERE job_id=:job_id
SQL;
        try {
            $sth = connectDB::$dbh->prepare($SQLDelete);            
            $sth->bindValue(':job_id', $idJob, PDO::PARAM_STR);            
            if($sth->execute()){             	        	
               	$result = true;                      	
            }
        }
        catch (Exception $e){                
        }        
        return $result;	
    }

    function sqlCleartbbackupjobuserdiskmappingset($idJob)
    {
    	$result = false;
        $SQLDelete = <<<SQL
           DELETE FROM tbbackupjobuserdiskmappingset WHERE job_id=:job_id
SQL;
        try {
            $sth = connectDB::$dbh->prepare($SQLDelete);            
            $sth->bindValue(':job_id', $idJob, PDO::PARAM_STR);            
            if($sth->execute()){             	        	
               	$result = true;                      	
            }
        }
        catch (Exception $e){                
        }        
        return $result;
    }

    function sqlInserttbbackupspaceset($input)
    {
    	$result = false;
        $SQLInsert = <<<SQL
           INSERT tbbackupspaceset (nameSpace,typeSpace,configSpace,date_created,date_modified) Values (:nameSpace,:typeSpace,TO_BASE64(DES_ENCRYPT(:configSpace,'".DES_Key."')),now(),now())
SQL;
        try {        	
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':nameSpace', $input['Name'], PDO::PARAM_STR);
            $sth->bindValue(':typeSpace', $input['JobType'], PDO::PARAM_STR);
            $sth->bindValue(':configSpace', $input['ConfigSpace'], PDO::PARAM_STR);
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

    function sqlInserttbbackupjobset($idJob,$idBackupSpace)
    {
        $result = false;
        $SQLInsert = <<<SQL
           INSERT tbbackupjobset (job_id,idBackupSpace) Values (:job_id,:idBackupSpace)
SQL;
        try {        	
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':job_id', $idJob, PDO::PARAM_STR);
            $sth->bindValue(':idBackupSpace', $idBackupSpace, PDO::PARAM_INT);
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

    function sqlInserttbbackupjobvdmappingset($idJob,$idVD)
    {
    	$result = false;
        $SQLInsert = <<<SQL
           INSERT tbbackupjobvdmappingset (job_id,idVD,date_created,date_modified) Values (:job_id,:idVD,NOW(),NOW())
SQL;
        try {
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':job_id', $idJob, PDO::PARAM_STR);
            $sth->bindValue(':idVD', $idVD, PDO::PARAM_STR);
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

    function sqlDeleteJob($idJob)
    {
    	$result = false;
        $SQLDelete = <<<SQL
           DELETE FROM tbjobbaseset WHERE job_id=:job_id
SQL;
        try {
            $sth = connectDB::$dbh->prepare($SQLDelete);            
            $sth->bindValue(':job_id', $idJob, PDO::PARAM_STR);            
            if($sth->execute()){             	        	
               	$result = true;                      	
            }
        }
        catch (Exception $e){                
        }        
        return $result;	
    }

    function processJobErrorCode($code){  
        $this->baseOutputResponse($code);
        switch ($code){            
            case CPAPIStatus::CreateJobFail:      
            case CPAPIStatus::ListJobDetailFail:
            case CPAPIStatus::DeleteJobFail:                      
                http_response_code(400);
                break;               
        }
    }
}
