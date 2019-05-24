<?php
class ScheduleAction extends BaseAPI
{
	function  __construct()
    {            
    } 

    function logScheduleAppendErr($code)
    {
        $appendErr = null;
        switch ($code) {
            case '08300201':          
            case '08300602':         
                $appendErr = '(Failed to list domain)';
                break;      
            case '08300202':
                $appendErr = '(Failed to insert schedule base)';
                break;           
            case '08300203':
                $appendErr = '(Failed to insert domain schedule)';
                break;
            case '08300204':
            case '08300403':
                $appendErr = '(Name is duplicated)';
                break;            
            case '08300401':
            case '08300601':
                $appendErr = '(Failed to list scdedule)';
                break;
            case '08300603':
            case '08300402':            
                $appendErr = '';
                break;
        }
        return $appendErr;
    }

    function listSchedule($input,&$output){
    	if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }   
        $this->deleteNotNeedSchedule();
       	$output = null;        
       	$this->sql_list_schedule_and_jobs_by_idDomain($input['DomainID'],$output);
       	if(is_null($output)){
       		return CPAPIStatus::ListScheduleFail;
       	}
       	return CPAPIStatus::ListScheduleSuccess;
    }    

    function deleteNotNeedSchedule()
    {
        try
        {                        
            $sqlSelect = <<<SQL
            select t1.schedule_id from tbschedulebaseset as t1 left join tbjobbaseset as t2 on t1.schedule_id=t2.schedule_id WHERE t2.schedule_id is NULL 
SQL;
            $sqlDelete = <<<SQL
            DELETE from tbschedulebaseset WHERE schedule_id = :schedule_id;
SQL;
            $sth = connectDB::$dbh->prepare($sqlSelect);                                   
            if($sth->execute())
            {         
                while ($row = $sth->fetch() ) {     
                    // var_dump($row);                                
                    $sth1 = connectDB::$dbh->prepare($sqlDelete); 
                    $sth1->bindValue(':schedule_id', $row['schedule_id'], PDO::PARAM_INT);            
                    $sth1->execute();
                }    
                // $result = true;                                            
            }                           
        }
        catch (Exception $e){                             
        }     
    }

    function createSchedule($input,&$output)
    {        
    	// var_dump($input);
    	if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }           
        $domainC = new DomainAction();        
        $domainC->sqlListDomainByID($input['DomainID'], $outputDomain);
        if(is_null($outputDomain)){
        	$logCode = '08300201';
            goto fail; 
        }
        $output_schedule = null;
        $this->sql_select_schedule_by_idDomain_name((int)$input['DomainID'],$input['Name'],$output_schedule);
        // var_dump($output_schedule);
        if(!is_null($output_schedule)){
        	$logCode = '08300204';
            goto fail; 
        }
        $output_id = null;
        connectDB::$dbh->beginTransaction(); 
        if(!$this->sql_insert_schedule_base($input,$output_id)){
        	$logCode = '08300202';
            goto fail_rollback;  
        }
        if(is_null($output_id)){
            $logCode = '08300202';
            goto fail_rollback;              	
        }
        $input['ScheduleID']=$output_id;
        if(!$this->sql_insert_tbdomainschedule($input)){
            $logCode = '08300203';
            goto fail_rollback;        	        	
        }        
        connectDB::$dbh->commit();        
        $this->sql_select_schedule_by_id($input['ScheduleID'],$output);
        if(is_null($output)){
            $logCode = '08300203';
            goto fail;        	
        }
        $this->insert_schedule_plan((int)$input['FreqType'],$input['ScheduleID']);
        $logCode = '08100101';        
        $output['Jobs']=array();
        $rtn = CPAPIStatus::CreateScheduleSuccess;
        goto end;
        fail:
            $rtn = CPAPIStatus::CreateScheduleFail;
            goto end;    
        fail_rollback:                
            connectDB::$dbh->rollBack();           
            $rtn = CPAPIStatus::CreateScheduleFail;
            goto end;    
        end:
            $appendErr = $this->logScheduleAppendErr($logCode);
            $this->insertCreateScheduleLog($logCode,$input['Name'],$outputDomain['Name'],$input['DomainID'],$appendErr);
            return $rtn;    
    }    

    function insertCreateScheduleLog($code,$Name,$domainName,$domainID,$appendErr)
    {
        $logType = 8;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $appendVDDomain = $this->logappendVDDomain($Name,$domainName);   
        if(is_null($appendErr)){
            $message = "Create Schedule$appendVDDomain Success";
        }
        else{
            $message = "Failed to create Schedule$appendVDDomain$appendErr";
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function insert_schedule_plan($type_schedule,$idSchedule)
    {       
        $output_plan = null;
        switch ($type_schedule) {
            case 1:
                $this->sql_list_once_schedule_by_idSchedule($idSchedule,$output_plan);
                break;
            case 4:                   
                $this->sql_list_daily_schedule_by_idSchedule($idSchedule,$output_plan);
                break;    
            case 8: 
                $this->sql_list_weekly_schedule_by_idSchedule($idSchedule,$output_plan);
                break;
            case 16:
                $this->sql_list_monthly_day_schedule_by_idSchedule($idSchedule,$output_plan); 
                break;
            case 32:
                $this->sql_list_monthly_week_schedule_by_idSchedule($idSchedule,$output_plan); 
                break;        
            default:                
                break;
        }

        if(!is_null($output_plan)){              
            if($output_plan['NowDate'] >= $output_plan['StartDate'])
            {                
                if($output_plan['StartTime'] >= $output_plan['NowTime']){
                    $this->sql_insert_schedule_plan($idSchedule,$output_plan['NowDate'],$output_plan['StartTime']);
                }                    
            //     if($output_plan['NowDate'] == $output_plan['StartDate'] || $output_plan['NowDate'] == $output_plan['CreateDate'] || $output_plan['NowDate'] == $output_plan['ModifyDate']){   
            //         if($output_plan['NowDate'] != $output_plan['CreateDate']){
            //             $this->sql_insert_schedule_plan($dbh,$output_plan['ScheduleID'],$output_plan['NowDate'],$output_plan['StartTime']);
            //         }                 
            //         else if($output_plan['NowTime'] <= $output_plan['StartTime'] && $output_plan['StartTime']!=0){                        
            //             $this->sql_insert_schedule_plan($idSchedule,$output_plan['NowDate'],$output_plan['StartTime']);
            //         }
            //     }
            //     else{                    
            //         $this->sql_insert_schedule_plan($idSchedule,$output_plan['NowDate'],$output_plan['StartTime']);
            //     }
            }            
        }
    }

    function modifySchedule($input,&$output)
    {
        $logCode = '';
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }       
        $output_schedule = null;
        $this->sql_select_schedule_by_idDomain_idSchedule($input['ScheduleID'],$input['DomainID'],$output_schedule);
        if(is_null($output_schedule)){
            $logCode = '08300401';
            goto fail;
        }
        $domainC = new DomainAction();
        $outputDomain = null;
        $domainC->sqlListDomainByID($input['DomainID'], $outputDomain);
        // var_dump($output_schedule);
        // var_dump($input);
        if($output_schedule['Name'] != $input['Name'] || $output_schedule['FreqType'] != $input['FreqType'] || $output_schedule['FreqInterval'] != $input['FreqInterval'] || $output_schedule['FreqRelativeInterval'] != $input['FreqRelativeInterval'] || $output_schedule['FreqRecurrenceFactor'] != $input['FreqRecurrenceFactor'] || $output_schedule['StartDate'] != $input['StartDate'] || $output_schedule['StartTime'] != $input['StartTime'] || $output_schedule['isEnable'] != $input['isEnable']){         
            if($output_schedule['Name'] != $input['Name']){
                $output_same_schedule = null;
                $this->sql_select_schedule_by_idDomain_name((int)$input['DomainID'],$input['Name'],$output_same_schedule);               
                if(!is_null($output_same_schedule)){
                    $logCode = '08300403';
                    goto fail;
                }
            }
            if(!$this->sql_modify_schedule_base($input)){
                $logCode = '08300402';
                goto fail;
            }
            else{
                $this->sql_select_schedule_by_id($input['ScheduleID'],$output);
                if(is_null($output)){
                    $logCode = '08300402';
                    goto fail;
                }                
                if($output_schedule['FreqType'] != $input['FreqType'] || $output_schedule['FreqInterval'] != $input['FreqInterval'] || $output_schedule['FreqRelativeInterval'] != $input['FreqRelativeInterval'] || $output_schedule['FreqRecurrenceFactor'] != $input['FreqRecurrenceFactor'] || $output_schedule['StartDate'] != $input['StartDate'] || $output_schedule['StartTime'] != $input['StartTime']){ 
                    $this->sql_delete_schedule_plan($input['ScheduleID']);
                    $this->insert_schedule_plan($input['FreqType'],$input['ScheduleID']);
                }
                $logCode = '08100301';
                $rtn = CPAPIStatus::ModfiyScheduleSuccess;  
                goto end;
            }
        }
        else{
            // echo 'same';
            $this->sql_select_schedule_by_id($input['ScheduleID'],$output);
            if(is_null($output)){
                $logCode = '08300402';
                goto fail;                
            }
            $logCode = '08100301';            
            $rtn = CPAPIStatus::ModfiyScheduleSuccess;  
            goto end;
        }
        fail:
            $rtn = CPAPIStatus::ModfiyScheduleFail;
            goto end;          
        end:
            $appendErr = $this->logScheduleAppendErr($logCode);
            $this->insertModifyScheduleLog($logCode,$input['Name'],$outputDomain['Name'],$input['DomainID'],$appendErr);
            return $rtn;    
    }    

    function insertModifyScheduleLog($code,$Name,$domainName,$domainID,$appendErr)
    {
        $logType = 8;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $appendVDDomain = $this->logappendVDDomain($Name,$domainName);   
        if(is_null($appendErr)){
            $message = "Modify Schedule$appendVDDomain Success";
        }
        else{
            $message = "Failed to modify Schedule$appendVDDomain$appendErr";
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function deleteSchedule($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }        
        $output_schedule = null;
        $this->sql_select_schedule_by_idDomain_idSchedule($input['ScheduleID'],$input['DomainID'],$output_schedule);
        if(is_null($output_schedule)){
            $logCode = '08300601';
            goto fail;
        }
        $domainC = new DomainAction();
        $outputDomain = null;
        $domainC->sqlListDomainByID($input['DomainID'], $outputDomain);
        if(is_null($outputDomain)){
            $logCode = '08300602';
            goto fail;
        }
        $this->sql_delete_schedule_by_id($input['ScheduleID']);
        $output_schedule = null;
        $this->sql_select_schedule_by_idDomain_idSchedule($input['ScheduleID'],$input['DomainID'],$output_schedule);
        if(!is_null($output_schedule)){
            $logCode = '08300603';
            goto fail;
        }
        $logCode = '08100501';        
        $rtn = CPAPIStatus::DeleteScheduleSuccess;
        goto end;
        fail:
            $rtn = CPAPIStatus::DeleteScheduleFail;
            goto end;          
        end:
            $appendErr = $this->logScheduleAppendErr($logCode);
            $this->insertDeleteScheduleLog($logCode,$input['Name'],$outputDomain['Name'],$input['DomainID'],$appendErr);
            return $rtn;   
    }

    function insertDeleteScheduleLog($code,$Name,$domainName,$domainID,$appendErr)
    {
        $logType = 8;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }
        $appendVDDomain = $this->logappendVDDomain($Name,$domainName);   
        if(is_null($appendErr)){
            $message = "Delete Schedule$appendVDDomain Success";
        }
        else{
            $message = "Failed to delete Schedule$appendVDDomain$appendErr";
        }
        if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message),$lastID)){
            LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
        }       
    }

    function sql_delete_schedule_plan($idSchedule)
    {
        $result = false;
        try
        {                        
            $SQLDelete = <<<SQL
            DELETE FROM tbplainscheduleset WHERE schedule_id=:schedule_id AND state=0 
SQL;
            $sth = connectDB::$dbh->prepare($SQLDelete);                       
            $sth->bindValue(':schedule_id', $idSchedule, PDO::PARAM_INT);            
            if($sth->execute())
            {           
                $result = true;                                            
            }                           
        }
        catch (Exception $e){                             
        }     
        return $result;
    }

    function sql_insert_schedule_plan($idSchedule,$date,$time)
    {
        $result = false;
        try
        {                        
            $SQLInsert = <<<SQL
            CALL insert_schedule_plan(:schedule_id,:date,:time)   
SQL;
            $sth = connectDB::$dbh->prepare($SQLInsert);                       
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

    function sql_list_once_schedule_by_idSchedule($idSchedule,&$output)
    {
        try
        {                        
            $SQLSelect = <<<SQL
            CALL list_meet_once(:schedule_id,@Output_Date,@Output_Time)   
SQL;
            $sth = connectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':schedule_id', $idSchedule, PDO::PARAM_INT);            
            if($sth->execute())
            {                        
                $rows = $sth->fetchAll();                 
                $sqlquery = "SELECT @Output_Date,@Output_Time";         
                $sth = connectDB::$dbh->prepare($sqlquery);    
                $sth->execute();         
                while ($row = $sth->fetch() ) {                                     
                    $now_datetime = $row;
                }                       
                foreach ($rows as $row1) {   
                    $create_date = date("Ymd", strtotime($row1['date_created'])); 
                    $modify_date = date("Ymd", strtotime($row1['date_modified'])); 
                    $output= array("ScheduleID"=>$row1['schedule_id'],"StartDate"=>$row1['active_start_date'],"StartTime"=>$row1['active_start_time'],"NowDate"=>$now_datetime["@Output_Date"],"NowTime"=>$now_datetime["@Output_Time"],"CreateDate"=>$create_date,"ModifyDate"=>$modify_date);
                }                                   
            }                           
        }
        catch (Exception $e){                             
        }     
    }

    function sql_list_daily_schedule_by_idSchedule($idSchedule,&$output)
    {
        try
        {                        
            $SQLSelect = <<<SQL
            CALL list_meet_daily(:schedule_id,@Output_Date,@Output_Time)   
SQL;
            $sth = connectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':schedule_id', $idSchedule, PDO::PARAM_INT);            
            if($sth->execute())
            {                                
                $rows = $sth->fetchAll();                 
                $sqlquery = "SELECT @Output_Date,@Output_Time";         
                $sth = connectDB::$dbh->prepare($sqlquery);    
                $sth->execute();         
                while ($row = $sth->fetch() ) {                                     
                    $now_datetime = $row;
                }                       
                foreach ($rows as $row1) {      
                    $create_date = date("Ymd", strtotime($row1['date_created'])); 
                    $modify_date = date("Ymd", strtotime($row1['date_modified']));
                    $output= array("ScheduleID"=>$row1['schedule_id'],"StartDate"=>$row1['active_start_date'],"StartTime"=>$row1['active_start_time'],"NowDate"=>$now_datetime["@Output_Date"],"NowTime"=>$now_datetime["@Output_Time"],"CreateDate"=>$create_date,"ModifyDate"=>$modify_date);
                }                          
            }                           
        }
        catch (Exception $e){                             
        }     
    }

    function sql_list_weekly_schedule_by_idSchedule($idSchedule,&$output)
    {
        try
        {                        
            $SQLSelect = <<<SQL
            CALL list_meet_weekly(:schedule_id,@Output_Date,@Output_Time)   
SQL;
            $sth = connectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':schedule_id', $idSchedule, PDO::PARAM_INT);            
            if($sth->execute())
            {                                
                $rows = $sth->fetchAll();                 
                $sqlquery = "SELECT @Output_Date,@Output_Time";         
                $sth = connectDB::$dbh->prepare($sqlquery);    
                $sth->execute();         
                while ($row = $sth->fetch() ) {                                     
                    $now_datetime = $row;
                }                       
                foreach ($rows as $row1) {   
                    $create_date = date("Ymd", strtotime($row1['date_created'])); 
                    $modify_date = date("Ymd", strtotime($row1['date_modified']));
                    $output= array("ScheduleID"=>$row1['schedule_id'],"StartDate"=>$row1['active_start_date'],"StartTime"=>$row1['active_start_time'],"NowDate"=>$now_datetime["@Output_Date"],"NowTime"=>$now_datetime["@Output_Time"],"CreateDate"=>$create_date,"ModifyDate"=>$modify_date);
                }               
            }                           
        }
        catch (Exception $e){                             
        }     
    }

    function sql_list_monthly_day_schedule_by_idSchedule($idSchedule,&$output)
    {
        try
        {                        
            $SQLSelect = <<<SQL
            CALL list_meet_monthly_day(:schedule_id,@Output_Date,@Output_Time)   
SQL;
            $sth = connectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':schedule_id', $idSchedule, PDO::PARAM_INT);            
            if($sth->execute())
            {                                
                $rows = $sth->fetchAll();                 
                $sqlquery = "SELECT @Output_Date,@Output_Time";         
                $sth = connectDB::$dbh->prepare($sqlquery);    
                $sth->execute();         
                while ($row = $sth->fetch() ) {                                     
                    $now_datetime = $row;
                }                       
                foreach ($rows as $row1) {                             
                    $create_date = date("Ymd", strtotime($row1['date_created'])); 
                    $modify_date = date("Ymd", strtotime($row1['date_modified']));
                    $output= array("ScheduleID"=>$row1['schedule_id'],"StartDate"=>$row1['active_start_date'],"StartTime"=>$row1['active_start_time'],"NowDate"=>$now_datetime["@Output_Date"],"NowTime"=>$now_datetime["@Output_Time"],"CreateDate"=>$create_date,"ModifyDate"=>$modify_date);
                }                              
            }                           
        }
        catch (Exception $e){                             
        }     
    }

    function sql_list_monthly_week_schedule_by_idSchedule($idSchedule,&$output)
    {
        try
        {                        
            $SQLSelect = <<<SQL
            CALL list_meet_monthly_week(:schedule_id,@Output_Date,@Output_Time)   
SQL;
            $sth = connectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':schedule_id', $idSchedule, PDO::PARAM_INT);            
            if($sth->execute())
            {                                
                $rows = $sth->fetchAll();                 
                $sqlquery = "SELECT @Output_Date,@Output_Time";         
                $sth = connectDB::$dbh->prepare($sqlquery);    
                $sth->execute();         
                while ($row = $sth->fetch() ) {                                     
                    $now_datetime = $row;
                }                       
                foreach ($rows as $row1) {                             
                    $create_date = date("Ymd", strtotime($row1['date_created'])); 
                    $modify_date = date("Ymd", strtotime($row1['date_modified']));
                    $output= array("ScheduleID"=>$row1['schedule_id'],"StartDate"=>$row1['active_start_date'],"StartTime"=>$row1['active_start_time'],"NowDate"=>$now_datetime["@Output_Date"],"NowTime"=>$now_datetime["@Output_Time"],"CreateDate"=>$create_date,"ModifyDate"=>$modify_date);
                }                           
            }                           
        }
        catch (Exception $e){                             
        }     
    }

    function sql_list_schedule_and_jobs_by_idDomain($idDomain,&$output)
    {        
        try
        {                        
            $SQLSelect = <<<SQL
            SELECT t1.*,t3.job_id,t3.name as job_name,t3.description as job_desc
            ,t3.category_id
            from tbschedulebaseset as t1
            INNER JOIN tbdomainscheduleset as t2
            ON t1.schedule_id=t2.schedule_id
            LEFT JOIN tbjobbaseset as t3
            ON t2.schedule_id = t3.schedule_id
            WHERE t2.idDomain = :idDomain                        
SQL;
            $sth = connectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT);            
            if($sth->execute())
            {                                
                $output_schedule = array();
                while( $row = $sth->fetch() ) 
                {                                       
                    if(array_key_exists($row['schedule_id'], $output_schedule))
                    {
                        if(!is_null($row['job_id'])){
                            $output_schedule[$row['schedule_id']]['Jobs'][] = array("JobID"=>$row['job_id'],
                    "Name"=>$row['job_name'],"Desc"=>$row['job_desc'],"JobType"=>$row['category_id']);
                        }
                    }
                    else{
                        $output_schedule[$row['schedule_id']] = array('ScheduleID'=>(int)$row['schedule_id'],'Name'=>$row['name'],'Desc'=>$row['description'],'FreqType'=>(int)$row['freq_type'],'FreqInterval'=>(int)$row['freq_interval'],'FreqRelativeInterval'=>(int)$row['freq_relative_interval'],'FreqRecurrenceFactor'=>(int)$row['freq_recurrence_factor'],'StartDate'=>(int)$row['active_start_date'],'StartTime'=>(int)$row['active_start_time'],'Jobs'=>array(),'CreateTime'=>$row['date_created']);
                        if(!is_null($row['job_id'])){
                            $output_schedule[$row['schedule_id']]['Jobs'][] = array("JobID"=>$row['job_id'],
                    "Name"=>$row['job_name'],"Desc"=>$row['job_desc'],"JobType"=>(int)$row['category_id']);
                        }
                    }
                    // var_dump($output_schedule);
                }
                $output = array_values($output_schedule);
            }                           
        }
        catch (Exception $e){                             
        }     
    }

    function sql_insert_schedule_base($input,&$output_id){
    	$result = false;        
        $SQLInsert = "INSERT tbScheduleBaseSet (name,enable,freq_type,freq_interval,freq_relative_interval,freq_recurrence_factor,active_start_date,active_start_time,active_end_date,active_end_time,description,date_created,date_modified) Values (:name,:enable,:freq_type,:freq_interval,:freq_relative_interval,:freq_recurrence_factor,:active_start_date,:active_start_time,99991231,235900,:description,now(),now())";
        $SQLSelectInsertID = "SELECT LAST_INSERT_ID() as ScheduleID";
        try {
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':name', $input['Name'], PDO::PARAM_STR);    
            $sth->bindValue(':enable', $input['isEnable'], PDO::PARAM_BOOL);                     
            $sth->bindValue(':freq_type', $input['FreqType'], PDO::PARAM_INT);                       
            $sth->bindValue(':freq_interval', $input['FreqInterval'], PDO::PARAM_INT);
            $sth->bindValue(':freq_relative_interval', $input['FreqRelativeInterval'], PDO::PARAM_INT);
            $sth->bindValue(':freq_recurrence_factor', $input['FreqRecurrenceFactor'], PDO::PARAM_INT);
            $sth->bindValue(':active_start_date', $input['StartDate'], PDO::PARAM_INT);
            $sth->bindValue(':active_start_time', $input['StartTime'], PDO::PARAM_INT);
            $sth->bindValue(':description', $input['Desc'], PDO::PARAM_STR);
            if($sth->execute()){ 
            	if($sth->rowCount()== 1){
            		$sth = connectDB::$dbh->prepare($SQLSelectInsertID); 
            		if($sth->execute()){  
		            	while( $row = $sth->fetch()) 
		                {
		                    $output_id = $row['ScheduleID'];
		                }
	                	$result = true;          
	                }
            	}
            }
        }
        catch (Exception $e){                
        }        
        return $result;
    }

    function sql_insert_tbdomainschedule($input){
    	$result = false;        
        $SQLInsert = "INSERT tbdomainscheduleset (idDomain,schedule_id) Values (:idDomain,:schedule_id)";
        try {
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':idDomain', $input['DomainID'], PDO::PARAM_INT);
            $sth->bindValue(':schedule_id', $input['ScheduleID'], PDO::PARAM_INT);            
            if($sth->execute()){ 
            	if($sth->rowCount()== 1)
                	$result = true;          
            }             
        }
        catch (Exception $e){                
        }        
        return $result;
    }

    function sql_select_schedule_by_id($schedule_id,&$output)
    {
        try
        {                        
        	$SQLSelect = "SELECT * FROM tbScheduleBaseSet WHERE schedule_id=:schedule_id";
            $sth = connectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':schedule_id', $schedule_id, PDO::PARAM_INT);            
            if($sth->execute())
            {                                
                while( $row = $sth->fetch() ) 
                {                    
                    $output = array('ScheduleID'=>(int)$row['schedule_id'],'Name'=>$row['name'],'Desc'=>$row['description'],'FreqType'=>(int)$row['freq_type'],'FreqInterval'=>(int)$row['freq_interval'],'FreqRelativeInterval'=>(int)$row['freq_relative_interval'],'FreqRecurrenceFactor'=>(int)$row['freq_recurrence_factor'],'StartDate'=>(int)$row['active_start_date'],'StartTime'=>(int)$row['active_start_time']);
                }
            }                           
        }
        catch (Exception $e){                             
        }     
    }

    function sql_select_schedule_by_idDomain_idSchedule($schedule_id,$idDomain,&$output)
    {
    	try
        {                        
        	$SQLSelect = "SELECT t2.* FROM tbdomainscheduleset as t1 INNER JOIN tbScheduleBaseSet as t2 ON t1.schedule_id=t2.schedule_id WHERE t1.schedule_id=:schedule_id";
            $sth = connectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':schedule_id', $schedule_id, PDO::PARAM_INT);            
            if($sth->execute())
            {                                
                while( $row = $sth->fetch() ) 
                {                    
                    $output = array('ScheduleID'=>(int)$row['schedule_id'],'Name'=>$row['name'],'Desc'=>$row['description'],'FreqType'=>(int)$row['freq_type'],'FreqInterval'=>(int)$row['freq_interval'],'FreqRelativeInterval'=>(int)$row['freq_relative_interval'],'FreqRecurrenceFactor'=>(int)$row['freq_recurrence_factor'],'StartDate'=>(int)$row['active_start_date'],'StartTime'=>(int)$row['active_start_time'],'Desc'=>$row['description']);
                }
            }                           
        }
        catch (Exception $e){                             
        }     
    }

    function sql_select_schedule_by_idDomain_name($idDomain,$name,&$output)
    {
    	// var_dump($idDomain.' '.$name);
    	try
        {                        
        	$SQLSelect = "SELECT t2.* FROM tbdomainscheduleset as t1 INNER JOIN tbScheduleBaseSet as t2 ON t1.schedule_id=t2.schedule_id WHERE t1.idDomain=:idDomain AND t2.name=:name";
            $sth = connectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT);            
            $sth->bindValue(':name', $name, PDO::PARAM_STR);            
            if($sth->execute())
            {                                
                while( $row = $sth->fetch() ) 
                {                    
                    $output = array('ScheduleID'=>(int)$row['schedule_id'],'Name'=>$row['name'],'Desc'=>$row['description'],'FreqType'=>(int)$row['freq_type'],'FreqInterval'=>(int)$row['freq_interval'],'FreqRelativeInterval'=>(int)$row['freq_relative_interval'],'FreqRecurrenceFactor'=>(int)$row['freq_recurrence_factor'],'StartDate'=>(int)$row['active_start_date'],'StartTime'=>(int)$row['active_start_time']);
                }
                // var_dump($output);
            }                           
        }
        catch (Exception $e){                             
        }     
    }

    function sql_modify_schedule_base($input)
    {
    	// var_dump($input);
    	$result = false;
        $SQLUpdate = <<<SQL
            UPDATE tbScheduleBaseSet SET name=:name,enable=:enable,freq_type=:freq_type,freq_interval=:freq_interval,freq_relative_interval=:freq_relative_interval,freq_recurrence_factor=:freq_recurrence_factor,active_start_date=:active_start_date,active_start_time=:active_start_time,description=:description,date_modified=now() WHERE schedule_id=:schedule_id
SQL;
        try {            
           	$sth = connectDB::$dbh->prepare($SQLUpdate);            
           	$sth->bindValue(':schedule_id', $input['ScheduleID'], PDO::PARAM_INT);    
            $sth->bindValue(':name', $input['Name'], PDO::PARAM_STR);    
            $sth->bindValue(':enable', $input['isEnable'], PDO::PARAM_BOOL);                     
            $sth->bindValue(':freq_type', $input['FreqType'], PDO::PARAM_INT);                       
            $sth->bindValue(':freq_interval', $input['FreqInterval'], PDO::PARAM_INT);
            $sth->bindValue(':freq_relative_interval', $input['FreqRelativeInterval'], PDO::PARAM_INT);
            $sth->bindValue(':freq_recurrence_factor', $input['FreqRecurrenceFactor'], PDO::PARAM_INT);
            $sth->bindValue(':active_start_date', $input['StartDate'], PDO::PARAM_INT);
            $sth->bindValue(':active_start_time', $input['StartTime'], PDO::PARAM_INT);
            $sth->bindValue(':description', $input['Desc'], PDO::PARAM_STR);
            if($sth->execute()){ 
            	$result = true;
            }
        } catch (Exception $e) {
        }
        return $result;
    }

    function sql_delete_schedule_by_id($idSchedule)
    {
        $result = false;
        $SQLDelete = <<<SQL
           DELETE FROM tbschedulebaseset WHERE schedule_id=:schedule_id
SQL;
        try {
            $sth = connectDB::$dbh->prepare($SQLDelete);            
            $sth->bindValue(':schedule_id', $idSchedule, PDO::PARAM_INT);            
            if($sth->execute()){                            
                $result = true;                         
            }
        }
        catch (Exception $e){                
        }        
        return $result; 
    }

    function sql_delete_schedule_by_idDomain($idDomain)
    {
        $result = false;
        $SQLDelete = <<<SQL
           DELETE FROM tbschedulebaseset WHERE schedule_id IN ( select schedule_id from tbdomainscheduleset where idDomain=:idDomain)
SQL;
        try {
            $sth = connectDB::$dbh->prepare($SQLDelete);            
            $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT);            
            if($sth->execute()){                            
                $result = true;                         
            }
        }
        catch (Exception $e){                
        }        
        return $result; 
    }

    function processScheduleErrorCode($code)
    {  
        $this->baseOutputResponse($code);
        switch ($code){            
            case CPAPIStatus::CreateScheduleFail:                
            case CPAPIStatus::ModfiyScheduleFail:
            case CPAPIStatus::ListScheduleFail:
            case CPAPIStatus::DeleteScheduleFail:
                http_response_code(400);
                break;               
        }
    }
}