<?php
class TaskAction extends BaseAPI
{    
    private $cmdVDTaskList = CmdRoot."ip vd_task_list ";   
    private $cmdVDTaskClear = CmdRoot."ip vd_task_clear ";
    private $cmdVDSSTaskList = CmdRoot."ip vdss_task_list ";        
    private $cmdVDSSTaskClear = CmdRoot."ip vdss_task_clear ";
    private $cmdBkTaskList = CmdRoot."ip bk_task_list ";
    private $cmdBkTaskClear = CmdRoot."ip bk_task_clear ";
    private $cmdVDTaskWaitDel = CmdRoot."ip vd_task_wait_del ";
    
    function  __construct($dbh =null)
    {    
        set_time_limit(0);
        if(!is_null($dbh))
            $this->dbh=$dbh;
    }
    
    function listTask($input,&$output)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }        
        $gmt = $this->getTZ($timezone);
        date_default_timezone_set($timezone);        
        $output = array();             
        $rtn = $this->listTaskShell($input,$outputTask);                        
        foreach ($outputTask as $task){     
            $metaData =  base64_decode($task['reportMeta']);
            $metaDataArr = explode('$*', $metaData);
            $type = $this->taskTypeEnum[$metaDataArr[0]];
            $outputDestName = '';
            if(isset($type)){
                if($task['reportState'] == 'yes'){
                    $task['endTime'] = '';               
                }
                  
                switch ($type) {
                    case $this->taskTypeEnum['moveDisk']:                        
                        $outputSrcName = $metaDataArr[3].'(Disk Name:'.$metaDataArr[7].')';
                        $outputDestName = $metaDataArr[9];
                        break;    
                    case $this->taskTypeEnum['moveUserProfile']:
                        $outputSrcName = $metaDataArr[3];
                        $outputDestName = $metaDataArr[9];
                        break;                
                    default:
                        if(isset($metaDataArr[7])){  
                            $outputSrcName = $metaDataArr[7];                  
                            $outputDestName = $metaDataArr[3];
                        }
                        else{
                            $outputSrcName = $metaDataArr[3];
                        }
                        break;
                }
                $startTime = $task['startTime'] == "" ? "" : date('Y-m-d H:i:s',$task['startTime']);            
                $endTime = $task['endTime'] == "" ? "" :  date('Y-m-d H:i:s',$task['endTime']);
                if($task['reportState'] == 'no')
                    $endTime = '';
                $state = $this->taskStateEnum[$task['taskState']];
                $state = $this->changeTaskState($state,$task['reportState']);
                $output[]=array('ID'=>$task['taskName'],'State'=>$state,'Type'=>$type
                        ,'Source'=>$outputSrcName,'Dest'=>$outputDestName
                        ,'StartTime'=>$startTime,'EndTime'=>$endTime
                        ,'Process'=>floatval($task['progress']));
            }
        }

        if(!is_null($output))
            return CPAPIStatus::ListTaskSuccess;
        else
            return CPAPIStatus::ListTaskFail;
    }

    function listTaskShell($input,&$output)
    {
        $result=false;    
        $output = array();
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDTaskList;            
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            exec($cmd,$output,$rtn);               
            if($rtn == 0){           
                $output = json_decode($output[0],true);
                $result = true;
            }       
            // var_dump($output);     
        }      
        return $result;
    }

    function changeTaskState($state,$reportState)
    {        
        if($reportState == 'no' || $reportState == 'reporting'){
            if($state == $this->taskStateEnum['ok'] || $state == $this->taskStateEnum['fail'])
                $state = $this->taskStateEnum['doing'];
        }        
        return $state;
    }
    
    function clearTask($input)
    {
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDTaskClear;            
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);                        
            exec($cmd,$output,$rtn);               
            if($rtn == 0){           
                $output = json_decode($output[0],true);
                $result = true;
            }                   
        }      
    }

    function sql_list_vdname_by_vd_number($number,&$output){
         $SQLList = <<<SQL
            SELECT * FROM tbVDImageBaseSet WHERE idVdNumber=:idVdNumber
SQL;
        try
        {                                           
            $sth = $this->dbh->prepare($SQLList);                                             
            $sth->bindValue(':idVdNumber', $number, PDO::PARAM_INT);           
            if($sth->execute())
            {                         
                while( $row = $sth->fetch() ) 
                {     
                    $output = array('Name'=>$row['nameVD']);
                }
            }                   
        }
        catch (Exception $e){ 
            $output = null;
        }
    }
   
    function listSSTask($input,&$output)
    {        
        // var_dump($input);
        $gmt = $this->getTZ($timezone);
        date_default_timezone_set($timezone);       
        if(!$this->connectDB())
        {
            return CPAPIStatus::DBConnectFail;
        }
        $rtn = $this->listSSTaskShell($input,$output_task);
        if( $rtn != 0){
            return CPAPIStatus::ListTaskFail;
        }       
        $output = array();
        foreach ($output_task as $task){                                   
            $metadataArr = base64_decode($task['reportMeta']);            
            $metadataArr = explode('$*', $metadataArr);                  
            $type =  $this->snap_action_enum[$metadataArr[0]];            
            $metadata = $this->get_ss_task_data($type,$metadataArr);            
            $state = $this->taskStateEnum[$task['taskState']];               
            if($state == -1 && $task['errorCode'] == 101)
                $state = -2;                 
            $state = $this->changeTaskState($state,$task['reportState']);            
            $start_time = $task['startTime'] == "" ? "" : date('Y-m-d H:i:s',$task['startTime']);
            $end_time = $task['endTime'] == "" ? "" : date('Y-m-d H:i:s',$task['endTime']);
            if(($type == $this->snap_action_enum['take'] || $type == $this->snap_action_enum['scheduleTake']) && $state == $this->taskStateEnum['ok']){
                $metadata['LayerDate'] = date('Y-m-d H:i:s',$task['ssName']);
            }
            $output[]=array('ID'=>$task['taskName'],'State'=>$state,'Type'=>$type
                    ,'VDName'=>$metadata['VDName'],'LayerDate'=>$metadata['LayerDate']
                    ,'LayerDesc'=>$metadata['LayerDesc'],'StartTime'=>$start_time,'EndTime'=>$end_time
                    ,'Process'=>floatval($task['progress']),'ErrorCode'=>(int)$task['errorCode']);
            // var_dump($output);
        }         
        if(!is_null($output))
            return CPAPIStatus::ListTaskSuccess;
        else
            return CPAPIStatus::ListTaskFail;

    }

    function clearSSTask($input)
    {
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDSSTaskClear;            
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            exec($cmd,$output,$rtn);               
            if($rtn == 0){           
                $output = json_decode($output[0],true);
                $result = true;
            }                   
        }      
    }

    function getBackupTaskData($type,$meta_arr)
    {
        $rtn_data = null;
        // var_dump($in_task_meta_data);
        $dstVDName = '';
        $vd_name = $meta_arr[3];        
        if(isset($meta_arr[7])){
            $vd_name = $meta_arr[7];        
            $dstVDName = $meta_arr[3];
        }
        if($type == $this->backupActionEnum['backup'] || $type == $this->backupActionEnum['scheduleBackup']){
            $dstVDName = $vd_name;
        }
        // switch ($type){
        //     case $this->backupActionEnum['backup']: 
        //         $vd_name = $meta_arr[3];                
        //         break;            
        // }
        $rtn_data = array('VDName'=>$vd_name,'DestVDName'=>$dstVDName);
        return $rtn_data;
    }

    function listBackupTask($input,&$output)
    {        
        // var_dump($input);
        $gmt = $this->getTZ($timezone);
        date_default_timezone_set($timezone);       
        if(!$this->connectDB())
        {
            return CPAPIStatus::DBConnectFail;
        }
        $rtn = $this->listBkTaskShell($input,$output_task);
        if( $rtn != 0){
            return CPAPIStatus::ListTaskFail;
        }
        // var_dump($output_task);
        $output = array();
        foreach ($output_task as $task){                                   
            $metadataArr = base64_decode($task['reportMeta']);            
            $metadataArr = explode('$*', $metadataArr);
            $type =  $this->backupActionEnum[$metadataArr[0]];            
            $metadata = $this->getBackupTaskData($type,$metadataArr);            
            $state = $this->taskStateEnum[$task['taskState']];                  
            $state = $this->changeTaskState($state,$task['reportState']);            
            $start_time = $task['startTime'] == "" ? "" : date('Y-m-d H:i:s',$task['startTime']);
            $end_time = $task['endTime'] == "" ? "" : date('Y-m-d H:i:s',$task['endTime']);
            $output[]=array('ID'=>$task['taskName'],'State'=>$state,'Type'=>$type
                    ,'Source'=>$metadata['VDName'],'Dest'=>$metadata['DestVDName'],'StartTime'=>$start_time,'EndTime'=>$end_time
                    ,'Process'=>floatval($task['progress']));
        }         
             // $output[]=array('ID'=>'111','State'=>2,'Type'=>201
             //        ,'Source'=>'src','Dest'=>'dest','StartTime'=>'','EndTime'=>''
             //        ,'Process'=>10);
        if(!is_null($output))
            return CPAPIStatus::ListTaskSuccess;
        else
            return CPAPIStatus::ListTaskFail;
    }

    function clearBackupTask($input)
    {
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdBkTaskClear;            
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            exec($cmd,$output,$rtn);               
            if($rtn == 0){           
                $output = json_decode($output[0],true);
                $result = true;
            }                   
        }     
    }

    function listBkTaskShell($input,&$output)
    {
        $output = array();
        $rtn=-2;            
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBkTaskList;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            // var_dump($cmd);                 
            exec($cmd,$output,$rtn);                 
            if( $rtn ==0){           
                $output = json_decode($output[0],true);                
            }
        }              
        return $rtn;
    }

    function get_ss_task_data($type,$meta_arr)
    {
        $rtn_data = null;
        // var_dump($in_task_meta_data);
        switch ($type){
            case $this->snap_action_enum['take']: 
            case $this->snap_action_enum['scheduleTake']: 
                $vd_name = $meta_arr[3];
                $layerDesc = base64_decode($meta_arr[6]);               
                $layerDate = '';
                break;
            case $this->snap_action_enum['delete']:                            ;
                $vd_name = $meta_arr[3];  
                $layerDate = $meta_arr[7];   
                $layerDesc = base64_decode($meta_arr[8]);                               
                break;   
            case $this->snap_action_enum['view']:            
                $vd_name = $meta_arr[3];  
                $layerDate = $meta_arr[7];  
                $layerDesc = base64_decode($meta_arr[8]);  
                break;  
            case $this->snap_action_enum['viewStop']:            
                $vd_name = $meta_arr[3];                              
                $layerDate = '';
                $layerDesc = '';
                break;    
            case $this->snap_action_enum['rollback']:            
                $vd_name = $meta_arr[3];  
                $layerDate = $meta_arr[7];   
                $layerDesc = base64_decode($meta_arr[8]); 
                break;    
        }
        $rtn_data = array('VDName'=>$vd_name,'LayerDate'=>$layerDate,'LayerDesc'=>$layerDesc);
        return $rtn_data;
    }

    function listSSTaskShell($input,&$output)
    {
        $output = array();
        $rtn=-2;            
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdVDSSTaskList;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);  
            exec($cmd,$output,$rtn);                 
            if( $rtn ==0){           
                $output = json_decode($output[0],true);                
            }
        }              
        return $rtn;
    }

    function clear_task($input,$task_type='normal'){
        $this->connectDB();
        if($this->dbh == null){
            return CPAPIStatus::DBConnectFail;
        }
        $cluster_c = new Cluster_Action($this->dbh);            
        $output_clusters = null;  
        $cluster_c->sql_list_cluster_of_domain($input['DomainID'], $output_clusters);        
        if(is_null($output_clusters))
            return CPAPIStatus::ClearTaskFail;        
        foreach ($output_clusters as $cluster){            
            $rtn = $cluster_c->list_cluster_info_no_state($cluster,$output_cluster);            
            if($rtn != CPAPIStatus::ListClusterInfoSuccess){     
                return CPAPIStatus::ClearTaskFail;
            }                                      
            $rtn = $this->clear_task_shell($input['DomainID'],$output_cluster,$task_type);
            if(!$rtn){
                return CPAPIStatus::ClearTaskFail;
            }     
        }   
        return CPAPIStatus::ClearTaskSuccess;
    }
    
    function clear_task_shell($idDomain,$input_cluster,$task_type='normal'){        
        $rtn=false;            
        if(!is_null($input_cluster['ConnectIP'])){
            switch ($task_type) {
                case 'normal':
                    $cmd = $this->cmd_vd_task_clear;                
                    break;
                case 'snapshot':
                    $cmd = $this->cmd_vd_ss_task_clear;
                    break;
            }
            $cmd .= $input_cluster['ConnectIP'].' ';
            $cmd .= $idDomain;            
            // var_dump($cmd);
            $output=shell_exec($cmd);                 
            if($output==0){
                $rtn = true;                              
                return $rtn;
            }
        }             
        return $rtn;
    }

    function cancelTask($input)
    {
        // if(!$this->connectDB()){
        //     return CPAPIStatus::DBConnectFail;
        // }            
        // $this->domainC = new DomainAction();   
        // $this->domainC->sqlListDomainByID($input['DomainID'], $outputDomain);  
        $rtn = $this->cancelTaskShell($input);
        if($rtn != 0){
            return CPAPIStatus::CancelTaskFail;
        }                 
        //if($this->log_c->createLog(array('Type'=>11,'Level'=>1,'Code'=>'0B100101','Riser'=>'admin','Message'=>'Cancel Task(ID:'. $task_id.',Domain:'.$output_domain['Name'].')'),$last_id)){
        //     $this->log_c->sqlInsertDomainLogRelation($last_id,$input['DomainID']);
        //}              
        return CPAPIStatus::CancelTaskSuccess;
    }

    function cancelTaskShell($input)
    {
        $rtn=-99;            
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDTaskWaitDel;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);           
            $cmd .= $input['TaskID'];                  
            exec($cmd,$output,$rtn);   
        }             
        return $rtn;
    }        
    
    function processTaskErrorCode($code){  
        $this->baseOutputResponse($code);
        switch ($code){           
            case CPAPIStatus::CancelTaskFail:                         
            case CPAPIStatus::ListTaskFail:
            case CPAPIStatus::ClearTaskFail:
                http_response_code(400);
                break; 
        }
    }
}