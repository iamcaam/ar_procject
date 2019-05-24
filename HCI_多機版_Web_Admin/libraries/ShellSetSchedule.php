<?php
include_once("/var/www/html/libraries/ConnectDB.php");
include_once("/var/www/html/libraries/BaseAPI.php");
include_once("/var/www/html/libraries/ErrorEnum.php");
include_once("/var/www/html/libraries/HandleLog.php");
function listAllSchedule(&$output)
{
	listOnceSchedule($output);
	listDailySchedule($output);
	listWeeklySchedule($output);
	listMonthlyDaySchedule($output);
	listMonthlyWeekSchedule($output);
}

function listOnceSchedule(&$output)
{
	try
    {                        
        $SQLSelect = <<<SQL
            CALL list_meet_once(NULL,@Output_Date,@Output_Time)
SQL;
		$sth = ConnectDB::$dbh->prepare($SQLSelect);        
        if($sth->execute())
        {                               
        	$rows = $sth->fetchAll();      
        	$sqlquery = "SELECT @Output_Date,@Output_Time";         
	        $sth = ConnectDB::$dbh->prepare($sqlquery);    
	        $sth->execute();         
	        while ($row = $sth->fetch() ) {     	        		        	
	        	$nowDatetime = $row;
	        }           	        
            foreach ($rows as $row1) {   
                $createDate = date("Ymd", strtotime($row1['date_created'])); 
                $modifyDate = date("Ymd", strtotime($row1['date_modified']));
                $output[] = array("Name"=>$row1['name'],"ScheduleID"=>$row1['schedule_id'],"StartDate"=>$row1['active_start_date'],"StartTime"=>$row1['active_start_time'],"NowDate"=>$nowDatetime["@Output_Date"],"NowTime"=>$nowDatetime["@Output_Time"],"CreateDate"=>$createDate,"ModifyDate"=>$modifyDate);
            } 
        }          
        else{
             $output = null;
        }                 
    }
    catch (Exception $e){          
        $output = null;                   
    }     
}

function listDailySchedule(&$output)
{
	try
    {                        
        $SQLSelect = <<<SQL
        	CALL list_meet_daily(NULL,@Output_Date,@Output_Time)   
SQL;
        $sth = ConnectDB::$dbh->prepare($SQLSelect);                               
        if($sth->execute())
        {           	    
        	$rows = $sth->fetchAll();      
        	$sqlquery = "SELECT @Output_Date,@Output_Time";         
	        $sth = ConnectDB::$dbh->prepare($sqlquery);    
	        $sth->execute();         
	        while ($row = $sth->fetch() ) {     	        	
	        	// var_dump($row);
	        	$nowDateTime = $row;
	        }           
	        // var_dump($nowDateTime);
            foreach ($rows as $row1) {             	
                $createDate = date("Ymd", strtotime($row1['date_created'])); 
                $modifyDate = date("Ymd", strtotime($row1['date_modified']));
                $output[] = array("Name"=>$row1['name'],"ScheduleID"=>$row1['schedule_id'],"StartDate"=>$row1['active_start_date'],"StartTime"=>$row1['active_start_time'],"NowDate"=>$nowDateTime["@Output_Date"],"NowTime"=>$nowDateTime["@Output_Time"],"CreateDate"=>$createDate,"ModifyDate"=>$modifyDate);
            }                  
        }     
        else{
            $output = null;
        }                      

        
        // $nowDatetime = $dbh->query($sqlquery)->fetch( PDO::FETCH_ASSOC);    
    }
    catch (Exception $e){       
        $output = null;                      
    }     
}

function listWeeklySchedule(&$output)
{
	 try
    {                        
        $SQLSelect = <<<SQL
            CALL list_meet_weekly(NULL,@Output_Date,@Output_Time)   
SQL;
        $sth = ConnectDB::$dbh->prepare($SQLSelect);                                    
        if($sth->execute())
        {                  
        	$rows = $sth->fetchAll();      
        	$sqlquery = "SELECT @Output_Date,@Output_Time";         
	        $sth = ConnectDB::$dbh->prepare($sqlquery);    
	        $sth->execute();         
	        while ($row = $sth->fetch() ) {     	        		        	
	        	$nowDateTime = $row;
	        }           	        
            foreach ($rows as $row1) {             	
                $createDate = date("Ymd", strtotime($row1['date_created'])); 
                $modifyDate = date("Ymd", strtotime($row1['date_modified']));
                $output[] = array("Name"=>$row1['name'],"ScheduleID"=>$row1['schedule_id'],"StartDate"=>$row1['active_start_date'],"StartTime"=>$row1['active_start_time'],"NowDate"=>$nowDateTime["@Output_Date"],"NowTime"=>$nowDateTime["@Output_Time"],"CreateDate"=>$createDate,"ModifyDate"=>$modifyDate);
            }             
        }         
        else{
            $output = null;
        }                        
    }
    catch (Exception $e){     
        $output = null;                        
    }     
}

function listMonthlyDaySchedule(&$output)
{
	try
    {                        
        $SQLSelect = <<<SQL
            CALL list_meet_monthly_day(NULL,@Output_Date,@Output_Time)   
SQL;
        $sth = ConnectDB::$dbh->prepare($SQLSelect);                              
        if($sth->execute())
        {                                
            $rows = $sth->fetchAll();      
        	$sqlquery = "SELECT @Output_Date,@Output_Time";         
	        $sth = ConnectDB::$dbh->prepare($sqlquery);    
	        $sth->execute();         
	        while ($row = $sth->fetch() ) {     	        		        	
	        	$nowDateTime = $row;
	        }           	        
            foreach ($rows as $row1) {             	
                $createDate = date("Ymd", strtotime($row1['date_created'])); 
                $modifyDate = date("Ymd", strtotime($row1['date_modified']));
                $output[] = array("Name"=>$row1['name'],"ScheduleID"=>$row1['schedule_id'],"StartDate"=>$row1['active_start_date'],"StartTime"=>$row1['active_start_time'],"NowDate"=>$nowDateTime["@Output_Date"],"NowTime"=>$nowDateTime["@Output_Time"],"CreateDate"=>$createDate,"ModifyDate"=>$modifyDate);
            }                     
        }     
        else{
            $output = null;
        }                            
    }
    catch (Exception $e){     
        $output = null;                        
    }     
}

function listMonthlyWeekSchedule(&$output)
{
	try
    {                        
        $SQLSelect = <<<SQL
            CALL list_meet_monthly_week(NULL,@Output_Date,@Output_Time)   
SQL;
        $sth = ConnectDB::$dbh->prepare($SQLSelect);                               
        if($sth->execute())
        {                                
	        $rows = $sth->fetchAll();                 
        	$sqlquery = "SELECT @Output_Date,@Output_Time";         
	        $sth = ConnectDB::$dbh->prepare($sqlquery);    
	        $sth->execute();         
	        while ($row = $sth->fetch() ) {     	        		        	
	        	$nowDatetime = $row;
	        }           	        
            foreach ($rows as $row1) {                             
                $createDate = date("Ymd", strtotime($row1['date_created'])); 
                $modifyDate = date("Ymd", strtotime($row1['date_modified']));
                $output[] = array("Name"=>$row1['name'],"ScheduleID"=>$row1['schedule_id'],"StartDate"=>$row1['active_start_date'],"StartTime"=>$row1['active_start_time'],"NowDate"=>$nowDatetime["@Output_Date"],"NowTime"=>$nowDatetime["@Output_Time"],"CreateDate"=>$createDate,"ModifyDate"=>$modifyDate);
            }           
        }       
        else{
            $output = null;
        }                          
    }
    catch (Exception $e){        
        $output = null;                     
    }     
}

function minDiff($startTime, $endTime) {
    $start = strtotime($startTime);
    $end = strtotime($endTime);
    $timeDiff = $end - $start;
    return floor($timeDiff / 60);
}

function insertSchedulePlan($meetPlans)
{
	foreach ($meetPlans as $outputPlan) {               
		if($outputPlan['NowDate'] >= $outputPlan['StartDate'])
	    {                        
            $nowDateTime = $outputPlan['NowDate'].sprintf('%06d',$outputPlan['NowTime']);
            $startDateTime = $outputPlan['StartDate'].sprintf('%06d',$outputPlan['StartTime']);        
            $timediff = minDiff($startDateTime,$nowDateTime);            
            if($outputPlan['StartTime'] >= $outputPlan['NowTime'] || $timediff <=10)
            {
                echo 'insert';
                sqlInsertSchedulePlan($outputPlan['ScheduleID'],$outputPlan['NowDate'],$outputPlan['StartTime']);
            }
            else{
                echo 'insert log';
                if(sqlCheckSchedulePlanExit($outputPlan['ScheduleID'],$outputPlan['NowDate'],$outputPlan['StartTime']) == NULL){
                    $domain_id = sqlListidDomainByScheduleID($outputPlan['ScheduleID']);
                    if(LogAction::createLog(array('Type'=>8,'Level'=>1,'Code'=>'08400101','Riser'=>'admin','Message'=>"Not Insert Schedule Plan(Name:".$outputPlan['Name'].")(Now Time is over Start Time 10 minutes)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$domain_id);
                    }       
                }
            }
         //    var_dump($outputPlan);
	        // if($outputPlan['NowDate'] == $outputPlan['StartDate'] || $outputPlan['NowDate'] == $outputPlan['CreateDate'] || $outputPlan['NowDate'] == $outputPlan['ModifyDate']){
         //        echo 1;
         //        if($outputPlan['NowDate'] != $outputPlan['CreateDate'] && $outputPlan['NowDate'] != $outputPlan['ModifyDate']){
         //            echo 'insert 1';
         //            sqlInsertSchedulePlan($dbh,$outputPlan['ScheduleID'],$outputPlan['NowDate'],$outputPlan['StartTime']);
         //        }
	        //     else if($outputPlan['NowTime'] <= $outputPlan['StartTime'] && $outputPlan['StartTime']!=0){
         //            echo 'insert 2';
         //            sqlInsertSchedulePlan($dbh,$outputPlan['ScheduleID'],$outputPlan['NowDate'],$outputPlan['StartTime']);
         //        }	            
	        // }
	        // else{
         //        echo 2;                
	        //        sqlInsertSchedulePlan($dbh,$outputPlan['ScheduleID'],$outputPlan['NowDate'],$outputPlan['StartTime']);
	        // }
	    }     
	}	
}

function sqlCheckSchedulePlanExit($idSchedule,$startDate,$startTime)
{
    try
    {      
        // var_dump($idSchedule.' '.$startDate.' '.$startTime);
        $id = NULL;
        $SQLSelect = <<<SQL
            SELECT * FROM tbplainscheduleset WHERE schedule_id=:schedule_id AND active_start_date=:active_start_date AND active_start_time=:active_start_time;
SQL;
        $sth = ConnectDB::$dbh->prepare($SQLSelect);                       
        $sth->bindValue(':schedule_id', $idSchedule, PDO::PARAM_INT);
        $sth->bindValue(':active_start_date', $startDate, PDO::PARAM_INT);
        $sth->bindValue(':active_start_time', $startTime, PDO::PARAM_INT);
        if($sth->execute())
        {           
             while ($row = $sth->fetch() ) { 
                // var_dump($row);
                $id = $row['schedule_id'];
            }                                                   
        }                
    }
    catch (Exception $e){                             
    }   
    // var_dump($id);
    return $id;
}

function sqlListidDomainByScheduleID($idSchedule)
{
    $idDomain = null;
    try
    {                        
        $SQLSelect = <<<SQL
            SELECT idDomain FROM tbdomainscheduleset WHERE schedule_id = :schedule_id
SQL;
        $sth = ConnectDB::$dbh->prepare($SQLSelect);                       
        $sth->bindValue(':schedule_id', $idSchedule, PDO::PARAM_INT);
        if($sth->execute())
        {           
             while ($row = $sth->fetch() ) {                                    
                $idDomain = $row['idDomain'];
            }                                                   
        }                           
    }
    catch (Exception $e){                             
    }     
    return $idDomain;
}

function sqlInsertSchedulePlan($idSchedule,$date,$time)
{
    $result = false;
    try
    {                        
        $SQLInsert = <<<SQL
            CALL insert_schedule_plan(:schedule_id,:date,:time)   
SQL;
        $sth = ConnectDB::$dbh->prepare($SQLInsert);                       
        $sth->bindValue(':schedule_id', $idSchedule, PDO::PARAM_INT);            
        $sth->bindValue(':date', $date, PDO::PARAM_INT);
        $sth->bindValue(':time', $time, PDO::PARAM_INT);
        if($sth->execute())
        {           
            $result = true;                                            
        }                           
    }
    catch (Exception $e){                             
    }     
    return $result;
}

function microtimeFloat()
{
    list($usec, $sec) = explode(" ", microtime());
    return ((float)$usec + (float)$sec);
}

$dateFile = '/mnt/tmpfs/monitor/plan/setdate';
$cmdGetTZ = 'sudo /var/www/html/date.sh get tz ';
$tz=trim(shell_exec($cmdGetTZ));
date_default_timezone_set($tz);
if(!is_null($argv[1]) && is_numeric($argv[1])){
	// var_dump('set date');
	file_put_contents($dateFile, $argv[1]);	
}
$setTime=0;
if(ConnectDB::$dbh == null){                       
    ConnectDB::connect();            
}   
$_SERVER['SERVER_ADDR'] = 'HCI Host';
While(1){
	if(ConnectDB::$dbh == null){
		ConnectDB::connect();   		
		if(ConnectDB::$dbh == null){			
			sleep(20);
			continue;
		}
	}		
    // $log_c->dbh = $dbh;
	$nowDate=(int)date('Ymd');
	$nowTime=(int)date('His');
	$setDate=(int)file_get_contents($dateFile);	    
	if($nowDate>=$setDate && $nowTime >= $setTime){        
		$output=array();
		listAllSchedule($output);
        // var_dump($output);
        if(is_null($output)){
            // echo 'resetdb';
            ConnectDB::$dbh = null;
            sleep(20);
            continue;
        }
        // var_dump($output);
		insertSchedulePlan($output);
		$setDate = (int)date('Ymd', strtotime($nowDate.' + 1 days'));
		// var_dump($setDate);
		file_put_contents($dateFile, $setDate);        
	}	
	sleep(20);			
}
?>