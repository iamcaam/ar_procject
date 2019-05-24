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
    private $cmd_vd_task_cancel = "sudo /var/www/html/vdi_util.sh vd_task_cancel ";
    private $cmd_vd_ss_task_cancel = "sudo /var/www/html/vdi_util.sh vd_ss_task_cancel ";
    private $cmd_cluster_task_list ="sudo /var/www/html/vdi_util.sh cluster_task_list ";
    private $cmd_cluster_task_clear ="sudo /var/www/html/vdi_util.sh cluster_task_clear ";
    private $cmd_cluster_task_cancel ="sudo /var/www/html/vdi_util.sh cluster_task_cancel ";
    
    function  __construct()
    {    
        set_time_limit(0);
    }
    
    function listTask($input,&$output)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }        
        $gmt = $this->getTZ($timezone);
        // var_dump($timezone);
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
                    case $this->taskTypeEnum['addglustergroup']:
                        $outputSrcName = '';
                        $outputDestName = '';
                        break;   
                    case $this->taskTypeEnum['addglustervolume']:
                        $outputSrcName = 'Pair ID : '.$metaDataArr[1].'(Volume : '.$metaDataArr[4].')';
                        $outputDestName = '';
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
            // switch ($metaDataArr[0]){
            //     case 'create':
            //         $taskType = 1;
            //         // $task_meta = base64_decode($task['taskMetadata']);
            //         $outputSrcName = $metaDataArr[3];
            //         $outputDestName = '';
            //         break;
            //     case 2:
            //         $task_meta = base64_decode($task['taskMetadata']);
            //         $meta_array = explode('$*', $task_meta);
            //         $outputSrcName = $meta_array[0];
            //         $outputDestName = $meta_array[1];
            //         break;
            //     case 3:                                                        
            //         $task_meta = base64_decode($task['taskMetadata']);
            //         $meta_array = explode('$*', $task_meta);
            //         $outputSrcName = $meta_array[1];
            //         $outputDestName = $meta_array[2];
            //         $output_type = (int)$meta_array[0];
            //         switch ($output_type){
            //             case 2:
            //                 $task['type'] = 23;
            //                 break;
            //             default :
            //                 break;
            //         }
            //         break;
            //     case 4:
            //         $task_meta = base64_decode($task['taskMetadata']);                            
            //         $meta_array = explode('$*', $task_meta);
            //         $output_type = (int)$meta_array[0];
            //         switch ($output_type){
            //             case 1:
            //                 $task['type'] = 21;
            //                 break;
            //             default :
            //                 break;
            //         }
            //         $outputSrcName = $meta_array[1];     
            //         $outputDestName = "";
            //         break;
            //     case 6:                                                        
            //         $task_meta = base64_decode($task['taskMetadata']);
            //         $meta_array = explode('$*', $task_meta);
            //         $outputSrcName = $meta_array[0];
            //         $outputDestName = $meta_array[1];
            //         break;
            //     case 7:                                                        
            //         $task_meta = base64_decode($task['taskMetadata']);
            //         $meta_array = explode('$*', $task_meta);
            //         $outputSrcName = $meta_array[1];
            //         $outputDestName = $meta_array[2];
            //         $output_type = (int)$meta_array[0];
            //         switch ($output_type){
            //             case 2:
            //                 $task['type'] = 22;
            //                 break;
            //             default :
            //                 break;
            //         }
            //         break;
            // }
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
            if(($type == $this->snap_action_enum['take'] || $type == $this->snap_action_enum['scheduleTake']|| $type == $this->snap_action_enum['takeUP'] || $type == $this->snap_action_enum['scheduleTakeUP']) && $state == $this->taskStateEnum['ok']){
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
            $vd_name = $meta_arr[3];
            $dstVDName = $vd_name;
        }else if($type == $this->backupActionEnum['upBackup'] || $type==$this->backupActionEnum['scheduleUPBackup']){
            $dstVDName = $vd_name;
        }
        else if($type == $this->backupActionEnum['restoreNewUP'] || $type == $this->backupActionEnum['restoreSameUP']){
            // var_dump($meta_arr);
            $vd_name = $meta_arr[5];   
            $dstVDName = $meta_arr[6];
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
            case $this->snap_action_enum['takeUP']: 
            case $this->snap_action_enum['scheduleTakeUP']:  
                $vd_name = $meta_arr[7];
                $layerDesc = base64_decode($meta_arr[6]);               
                $layerDate = '';
                break;
            case $this->snap_action_enum['delete']:
                $vd_name = $meta_arr[3];  
                $layerDate = $meta_arr[7];   
                $layerDesc = base64_decode($meta_arr[8]);                               
                break;   
            case $this->snap_action_enum['deleteUP']:                
                $vd_name = $meta_arr[9];  
                $layerDate = $meta_arr[7];   
                $layerDesc = base64_decode($meta_arr[8]);
                break;
            case $this->snap_action_enum['view']:  
            case $this->snap_action_enum['viewUP']:          
                $vd_name = $meta_arr[3];  
                $layerDate = $meta_arr[7];  
                $layerDesc = base64_decode($meta_arr[8]);  
                break;  
            case $this->snap_action_enum['viewStop']:
            case $this->snap_action_enum['viewStopUP']:        
                $vd_name = $meta_arr[3];                              
                $layerDate = '';
                $layerDesc = '';
                break;    
            case $this->snap_action_enum['rollback']:            
                $vd_name = $meta_arr[3];  
                $layerDate = $meta_arr[7];   
                $layerDesc = base64_decode($meta_arr[8]); 
                break;
            case $this->snap_action_enum['rollbackUP']:                
                $vd_name = $meta_arr[9];  
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
            // var_dump($cmd);                 
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
    
    function list_cluster_task(&$output){
        $this->connectDB();
        if($this->dbh == null){
            return CPAPIStatus::DBConnectFail;
        }
        $output_fences = null;
        $this->sql_list_all_cluster_fence($output_fences);        
        if(is_null($output_fences))
            return CPAPIStatus::ListTaskFail;
        $output = array();
        foreach ($output_fences as $fence){
            $rtn = $this->list_cluster_task_shell($fence['IP'], $output_task);
            if($rtn != 0){
                continue;
            }          
//            var_dump($output_task);
            $output_task = json_decode($output_task,true);                     
            foreach ($output_task as $task_arr){    
                foreach ($task_arr as $task){                    
                    switch ((int)$task['type']){
                        case 51:
                            $task_meta = base64_decode($task['taskMetadata']);                            
                            $meta_array = explode('$*', $task_meta);
                            $output_src_name = $meta_array[1]."(Cluster:".$fence['ClusterName'].")";
                            $output_dest_name = '';
                            break;
                    }
                    $state = $this->change_task_state((int)$task['state'],(int)$task['errorCode']);
                    $start_time = $task['startTime'] == "" ? null : $task['startTime'];
                    $end_time = $task['endTime'] == "" ? null : $task['endTime'];                          
                    $output[]=array('ID'=>$task['task'].':'.$fence['ClusterID'],'State'=>$state,'Type'=>(int)$task['type']
                            ,'Source'=>$output_src_name,'Dest'=>$output_dest_name
                            ,'StartTime'=>$start_time,'EndTime'=>$end_time
                            ,'Process'=>floatval($task['process']),'ErrorCode'=>(int)$task['errorCode']);
//                        var_dump($output);                    
                }
            }
            return CPAPIStatus::ListTaskSuccess;
        }
    }    
    
    function list_cluster_task_shell($ip,&$output){
        $cmd = $this->cmd_cluster_task_list;
        $cmd .= $ip;                    
        $output=shell_exec($cmd);                 
        if(!(is_int($output) && ($output==-2 || $output==-1))){           
            $rtn = 0;
            return $rtn;
        }
    }
    
    function clear_cluster_task(){
         $this->connectDB();
        if($this->dbh == null){
            return CPAPIStatus::DBConnectFail;
        }
        $output_fences = null;
        $this->sql_list_all_cluster_fence($output_fences);        
        if(is_null($output_fences))
            return CPAPIStatus::ClearTaskFail;
        foreach ($output_fences as $fence){
            $this->clear_cluster_task_shell($fence['IP']);           
        }
        return CPAPIStatus::ClearTaskSuccess;
    }
    
    function clear_cluster_task_shell($ip){
        $rtn=-2;                    
        $cmd = $this->cmd_cluster_task_clear;
        $cmd .= $ip.' ';             
        $rtn=shell_exec($cmd);                        
    }
    
    function cancel_cluster_task($input)
    {        
        $this->connectDB();
        if($this->dbh == null){
            return CPAPIStatus::DBConnectFail;
        }
        if(is_null($this->log_c))
            $this->log_c = new LogAction($this->dbh);              
        $task_arr = explode(':', $input['TaskID']);          
        $cluster_id = $task_arr[1];
        $task_id =$task_arr[0];
        if(is_null($cluster_id))
            return CPAPIStatus::CancelTaskFail;
        $input['TaskID'] = $task_id;
        $input['ClusterID'] = $cluster_id;
        $cluster_c = new Cluster_Action($this->dbh);            
        $output_cluster = null;  
        $cluster_c->list_cluster_info_no_state($input, $output_cluster);       
        // var_dump($output_cluster); 
        if(is_null($output_cluster))
            return CPAPIStatus::CancelTaskFail;             
        $rtn = $this->cancel_cluster_task_shell($input['TaskID'],$output_cluster);
        if(!$rtn){
            return CPAPIStatus::CancelTaskFail;
        }                         
        return CPAPIStatus::CancelTaskSuccess;
    }

    function cancel_cluster_task_shell($idTask,$input_cluster)
    {
        $rtn=false;            
        if(!is_null($input_cluster['ConnectIP'])){          
            $cmd = $this->cmd_cluster_task_cancel;                   
            $cmd .= $input_cluster['ConnectIP'].' ';
            $cmd .= $idDomain.' ';            
            $cmd .= $idTask;            
            $output=shell_exec($cmd);   
            // var_dump($cmd);              
            if($output==0){
                $rtn = true;                
            }
        }             
        return $rtn;
    }

    function sql_list_all_cluster_fence(&$output){
        $SQLList = <<<SQL
                SELECT t1.nameCluster,t1.idCluster,t2.* FROM tbClusterSet as t1 
                    INNER JOIN tbVDServerSet as t2 
                    ON t1.idCluster=t2.idCluster 
                    AND t2.isFence=true;
SQL;
          try        
        {              
            $sth = $this->dbh->prepare($SQLList);                             
            if($sth->execute())
            {                    
                $output = array();
                while( $row = $sth->fetch() ) 
                {
                  
                    $output[] = array("ClusterID"=>$row['idCluster'],"ClusterName"=>$row['nameCluster'],"Name"=>$row['nameNode'],"IP"=>$row['address']);
                }                     
            }                     
        }
        catch (Exception $e){                
            $output=null;
        }         
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