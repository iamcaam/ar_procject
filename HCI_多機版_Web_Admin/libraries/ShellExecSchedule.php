<?php
// error_reporting(0); 
include_once("/var/www/html/libraries/ConnectDB.php");
include_once("/var/www/html/libraries/ErrorEnum.php");
include_once("/var/www/html/libraries/BaseAPI.php");
include_once("/var/www/html/libraries/HandleVswitch.php");
include_once("/var/www/html/libraries/HandleCephPool.php");
include_once("/var/www/html/libraries/HandleSnapshot.php");
include_once("/var/www/html/libraries/HandleVD.php");
include_once("/var/www/html/libraries/HandleDomain.php");
include_once("/var/www/html/libraries/HandleLog.php");
include_once("/var/www/html/libraries/HandleBackup.php");
include_once("/var/www/html/libraries/ISnapshotShell.php");
include_once("/var/www/html/libraries/IBackupShell.php");

function sqlListOneSchedulePlan(&$output)
{
	try
    {                        
        $SQLSelect = <<<SQL
            select t1.idEvent,t1.schedule_id,t2.enable 
            from tbplainscheduleset as t1 
            inner join tbschedulebaseset as t2 
            on t1.schedule_id=t2.schedule_id 
            where state=0 
            AND (DATE_FORMAT(now(),'%Y%m%d') > t1.active_start_date
            OR( DATE_FORMAT(now(),'%Y%m%d') = t1.active_start_date AND DATE_FORMAT(now(),'%H%i%s') >= t1.active_start_time))
            order by t1.active_start_date,t1.active_start_time limit 1   
SQL;
        $sth = ConnectDB::$dbh->prepare($SQLSelect);                            
        if($sth->execute())
        {                                	        
	        while ($row = $sth->fetch() ) {     	        		        	
	        	$output = array('isEnable'=>$row['enable'],'EventID'=>$row['idEvent'],'ScheduleID'=>$row['schedule_id']);
	        }           	                    
        }                           
    }
    catch (Exception $e){                             
    }
}

function sqlUpdateSchedulePlanState($idEvent,$state)
{
	$result = false;
	try
    {                        
        $SQLUpdate = <<<SQL
            UPDATE tbplainscheduleset SET state=:state where idEvent=:idEvent
SQL;
        $sth = ConnectDB::$dbh->prepare($SQLUpdate);   
        $sth->bindValue(':idEvent', $idEvent, PDO::PARAM_INT);
        $sth->bindValue(':state', $state, PDO::PARAM_INT);                                
        if($sth->execute())
        {                                	        
	    	$result = true;                    
        }                           
    }
    catch (Exception $e){                             
    }
    return $result;
}

function sqlDeleteSchedulePlan($idEvent)
{
    $result = false;
    try
    {                        
        $SQLDelete = <<<SQL
            DELETE FROM tbplainscheduleset where idEvent=:idEvent
SQL;
        $sth = ConnectDB::$dbh->prepare($SQLDelete);   
        $sth->bindValue(':idEvent', $idEvent, PDO::PARAM_INT);        
        if($sth->execute())
        {                                           
            $result = true;                    
        }                           
    }
    catch (Exception $e){                             
    }
    return $result;
}

function sqlListJobsByidSchedule($idSchedule,&$output)
{
	try
    {                        
        $SQLSelect = <<<SQL
            select t1.job_id,t1.category_id,t2.name from tbjobbaseset as t1 
            inner join tbschedulebaseset as t2
            on t1.schedule_id = t2.schedule_id
            where t1.schedule_id=:schedule_id
SQL;
        $sth = ConnectDB::$dbh->prepare($SQLSelect);   
        $sth->bindValue(':schedule_id', $idSchedule, PDO::PARAM_INT);                 
        if($sth->execute())
        {                                	        
	        while ($row = $sth->fetch() ) {     	            		        
	        	$output[] = array('ScheduleName'=>$row['name'],'JobID'=>$row['job_id'],"Type"=>(int)$row['category_id']);
	        }           	                    
        }                           
    }
    catch (Exception $e){                             
    }	
}

function sqlListSnapshotJobUserDisksbyidJob($idJob,&$output)
{
    $SQLSelect = <<<SQL
        SELECT t2.idVDisk,t4.nameVDisk,t3.nameUser,t3.idUser,t3.idDomain
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
                $output[] = array("DomainID"=>$row['idDomain'],"DiskID"=>$row['idVDisk'],"DiskName"=>$row['nameVDisk'],'UserID'=>$row['idUser'],"Username"=>$row['nameUser']);
            }
        }                      
    }
    catch (Exception $e){                       
    }     
}

function sqlListBackupJobVDisksByidJob($idJob,&$output)
{
    $sqlSelect = <<<SQL
        SELECT t2.idVDisk,t4.nameVDisk,t3.nameUser,t3.idUser,t3.idDomain
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
                $output[] = array("DomainID"=>$row['idDomain'],"DiskID"=>$row['idVDisk'],"DiskName"=>$row['nameVDisk'],'UserID'=>$row['idUser'],"Username"=>$row['nameUser']);      
            }
        }                      
    }
    catch (Exception $e){           
    }             
}

function sqlSelectSnapshotVDsByidJob($idJob,&$output)
{
	try
    {                        
        $SQLSelect = <<<SQL
            select t1.idVD,t2.idDomain from tbssjobvdmappingset as t1
            INNER JOIN tbvdimagebaseset as t2
            ON t1.idVD = t2.idVD
            WHERE job_id=:job_id;   
SQL;
        $sth = ConnectDB::$dbh->prepare($SQLSelect);   
        $sth->bindValue(':job_id', $idJob, PDO::PARAM_STR);                                
        if($sth->execute())
        {                                	        
	        while ($row = $sth->fetch() ) {     	        		        	
	        	$output[] = array('VDID'=>$row['idVD'],'DomainID'=>$row['idDomain']);
	        }           	                    
        }                           
    }
    catch (Exception $e){                             
    }		
}

function sqlSelectBackupVDsByidJob($idJob,&$output)
{
    try
    {                        
        $SQLSelect = <<<SQL
            select t1.idVD,t2.idDomain from tbbackupjobvdmappingset as t1
            INNER JOIN tbvdimagebaseset as t2
            ON t1.idVD = t2.idVD
            WHERE job_id=:job_id;   
SQL;
        $sth = ConnectDB::$dbh->prepare($SQLSelect);   
        $sth->bindValue(':job_id', $idJob, PDO::PARAM_STR);                                
        if($sth->execute())
        {                                           
            while ($row = $sth->fetch() ) {                                     
                $output[] = array('VDID'=>$row['idVD'],'DomainID'=>$row['idDomain']);
            }                                   
        }                           
    }
    catch (Exception $e){                             
    }       
}

// function sql_select_fence_ip_by_idVD($dbh,$idVD,&$output)
// {
// 	try
//     {                        
//         $SQLSelect = <<<SQL
//             select t1.idDomain,t1.idVdNumber,t1.nameVD,t4.address,t5.nameDomain from tbvdimagebaseset as t1 inner join tbgfslunset as t2 on t1.idGfs=t2.idGfs inner join tbclusterset as t3 on t2.idCluster=t3.idCluster inner join tbvdserverset as t4 on t3.idCluster=t4.idCluster inner join tbdomainset as t5 on t1.idDomain=t5.idDomain WHERE t1.idVD=:idVD AND t4.isFence=true;   
// SQL;
//         $sth = $dbh->prepare($SQLSelect);   
//         $sth->bindValue(':idVD', $idVD, PDO::PARAM_STR);                                
//         if($sth->execute())
//         {                                	        
// 	        while ($row = $sth->fetch() ) {     	        		        	
// 	        	$output = array('VDID'=>$idVD,'VDName'=>$row['nameVD'],'ServerVDName'=>$row['idDomain'].'_'.$row['idVdNumber'].'_vd','IP'=>$row['address'],'DomainID'=>$row['idDomain'],'DomainName'=>$row['nameDomain']);
// 	        }           	                    
//         }                           
//     }
//     catch (Exception $e){                             
//     }
// }

$cmd_get_tz = 'sudo /var/www/html/date.sh get tz ';
$snapC = new SnapshotAction();
$backupC = new BackupAction();
ConnectDB::connect();
While(1){
	if(ConnectDB::$dbh == null){
        ConnectDB::connect();           
        if(ConnectDB::$dbh == null){            
            sleep(20);
            continue;
        }
    }       	   

    $ceph_c = new CephPoolAction();
    if($ceph_c->list_default_one_ceph_and_server($output_default_ceph)){             
        $cephID = $output_default_ceph['CephID'];        
    }
    else{
        echo 'set db null';
        ConnectDB::$dbh = null;
        sleep(20);   
        continue;
    }                                            
    // var_dump($cephID);
	$outputPlan =  null;
	sqlListOneSchedulePlan($outputPlan);
	if(!is_null($outputPlan)){       
        $_SERVER['SERVER_ADDR']= 'HCI Host';
        if(!$outputPlan['isEnable']){
            sqlDeleteSchedulePlan($outputPlan['EventID']);
            continue;
        }		
		$outputJobs=array();
		sqlListJobsByidSchedule($outputPlan['ScheduleID'],$outputJobs);           
		foreach ($outputJobs as $job) {
            // var_dump($job);
			switch($job['Type']){
				case 1:
					$outputVDs = array();
					sqlSelectSnapshotVDsByidJob($job['JobID'],$outputVDs);
					foreach ($outputVDs as $vd) {                        
                        $vd['Desc'] = 'Auto : Schedule Snapsht(Shedule Name:'.$job['ScheduleName'].')';
                        $vd['ConnectIP'] = '127.0.0.1';
                        $vd['CephID'] = $cephID ;                        
					    $snapC->createTakeSnapshotTask($vd,'scheduleTake');	
					}
                    sqlUpdateSchedulePlanState($outputPlan['EventID'],1);
                case 2:
                    $outputVDs = array();
                    sqlSelectBackupVDsByidJob($job['JobID'],$outputVDs);
                    foreach ($outputVDs as $vd) {                        
                        $vd['Desc'] = 'Auto : Schedule Backup(Shedule Name:'.$job['ScheduleName'].')';
                        $vd['ConnectIP'] = '127.0.0.1';
                        $vd['CephID'] = $cephID ;
                        // var_dump($vd);
                        $backupC->backup($vd,'scheduleBackup');    
                    }
                    sqlUpdateSchedulePlanState($outputPlan['EventID'],1);
                case 4:
                    $outputDisks = array();     
                    sqlListSnapshotJobUserDisksbyidJob($job['JobID'],$outputDisks);
                    // var_dump($outputDisks);
                    foreach ($outputDisks as $disk) {    
                        $disk['Desc'] = 'Auto : Schedule Snapsht User Disk(Shedule Name:'.$job['ScheduleName'].')';
                        $disk['ConnectIP'] = '127.0.0.1';
                        $disk['CephID'] = $cephID ;
                        $snapC->createTakeUPSnapshotTask($disk,'scheduleTakeUP',true);    
                    }         
                    sqlUpdateSchedulePlanState($outputPlan['EventID'],1);                  
                    break;
                case 8:
                    $outputDisks = array();     
                    sqlListBackupJobVDisksByidJob($job['JobID'],$outputDisks);
                    foreach ($outputDisks as $disk) {    
                        $disk['Desc'] = 'Auto : Schedule Snapsht User Disk(Shedule Name:'.$job['ScheduleName'].')';
                        $disk['ConnectIP'] = '127.0.0.1';
                        $disk['CephID'] = $cephID ;
                        $backupC->backupUserDisk($disk,'scheduleUPBackup');    
                    }                                 
                    sqlUpdateSchedulePlanState($outputPlan['EventID'],1);
                    break;
				break;					
			}				
		}
        sleep(10);
        // sqlDeleteSchedulePlan($dbh,$outputPlan['EventID']);
	}		
    else{
	    sleep(20);	
    }
}
?>