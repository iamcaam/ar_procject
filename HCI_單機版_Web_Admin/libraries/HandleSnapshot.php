<?php
class SnapshotAction extends BaseAPI
{
	private $ssLayerMax = 30;
	private $ssLayerIsOverwrite = true;
	private $cmdVDSSTaskTake = "sudo /var/www/html/script/cp_util.sh vdss_task_take ";	
	private $cmdVDSSTaskDelete = CmdRoot."ip vdss_task_del ";
	private $cmdVDSSTaskView = CmdRoot."ip vdss_task_view ";
	private $cmdVDSSListViewLayer = CmdRoot."ip vdss_get_view_layer ";
    private $cmdVDSSTaskViewStop = CmdRoot."ip vdss_task_unview ";
    private $cmdVDSSTaskRollback = CmdRoot."ip vdss_task_rollback ";

    private $cmdVDSSView = CmdRoot."ip vd_ss_view ";
    private $cmdVDSSUnView = CmdRoot."ip vd_ss_unview ";

    function __construct()
    {
        
    }

    function processTakeReportData($input,&$output)
    {
        $output = NULL;
        $metadata = base64_decode($input[0]);
        $metadataArr = explode('$*', $metadata);        
        $output['Type'] = $metadataArr[0];
        $output['ErrCode'] = $input[1];
        $output['CreateTime'] = date('Y-m-d H:i:s',$input[2]);
        $output['LogicalLayer'] = $input[3];
        $output['LogicalParentLayer'] = $input[4];
        $output['isRootLayer'] = $input[4] == 'base.img' ? true : false;
        $output['RootLayer'] = $input[5];       
        $output['Size'] = $input[6];    
        $output['CephID'] = $metadataArr[1];
        $output['VDID'] = $metadataArr[2];
        $output['VDName'] = $metadataArr[3];
        $output['DomainID'] = $metadataArr[4];
        $output['DomainName'] = $metadataArr[5];
        $output['ConnectIP'] = $this->getConnectIP();  
        $output['LayerDesc'] = '';
        $output['LayerDate'] = $output['CreateTime'];
        // var_dump($output);
        if(isset($metadataArr[6])){
            // var_dump($metadataArr[6]);
            $output['LayerDesc'] = base64_decode($metadataArr[6]);           
        }      
    }

     function processSSReportData($input,&$output)
    {
        $output = NULL;
        $metadata = base64_decode($input[0]);
        $metadataArr = explode('$*', $metadata);
        // var_dump($metadataArr);
        $output['Type'] = $metadataArr[0];
        $output['ErrCode'] = $input[1];
        $output['CreateTime'] = date('Y-m-d H:i:s',$input[2]); 
        $output['CephID'] = $metadataArr[1];
        $output['VDID'] = $metadataArr[2];
        $output['VDName'] = $metadataArr[3];
        $output['DomainID'] = $metadataArr[4];
        $output['DomainName'] = $metadataArr[5];
        $output['ConnectIP'] = $this->getConnectIP();  
        $output['Layer'] = $metadataArr[6];
        $output['LayerDesc'] = base64_decode($metadataArr[8]);
        $output['LayerDate'] = $metadataArr[7];
    }

    function listSnapshot($input,&$output)
    {        
    	if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }       
        $this->sqlAddVDSnapshotMaxCountField();
        $this->vdC = new VDAction();       
        $outputVDInfo = $this->listVDInfoByidVD($input['VDID']);              
        if(is_null($outputVDInfo)){  	
        	return CPAPIStatus::ListSnapshotFail;	
        }                 
        $input['VDName'] = $outputVDInfo['VDName'];
        // $input['ServerVDName']=$input['DomainID'].'_'.$outputVDInfo['VDNumber'].'_vd';
        $output = null;
        $current_view = null;
        $rtn = $this->listVDViewLayer($input);        
        if(isset($rtn['viewLayer']) && strlen($rtn['viewLayer']) > 0)
        	$current_view = $rtn['viewLayer'];
        $output = $this->sqlListSnapshotByidVD($input['VDID'],$current_view);        
        if(is_null($output))
        	return CPAPIStatus::ListSnapshotFail;
        return CPAPIStatus::ListSnapshotSuccess;
    }

    function sqlListSnapshotByidVD($idVD,$view_layer=null)
    {
        $output = NULL;
    	$SQLList = <<<SQL
            SELECT * FROM tbvdsnapshotset WHERE idSourceVD=:idSourceVD;
SQL;
        try        
        {              
            $sth = connectDB::$dbh->prepare($SQLList);     
            $sth->bindValue(':idSourceVD', $idVD, PDO::PARAM_STR);                         
            if($sth->execute())
            {                
                $output = array();       
            	$outputLayers = array();                    
                while( $row = $sth->fetch() ) 
                {                       
                	$isView=false;           
                	if($row['idLayer'] == $view_layer)
                		$isView=true;
                    if($row['id'] == $row['idUpperLayer'])
                        $rootLayer = array("Layer"=>$row['id'],"LogicalLayer"=>$row['idLayer'],"UpperLayer"=>$row['idUpperLayer'],"State"=>(int)$row['stateLayer'],"CreateTime"=>$row['date_created'],"Desc"=>$row['description'],"isView"=>$isView,"Size"=>(int)$row['sizeLayer']);
                    else
                        $outputLayers[$row['idUpperLayer']] = array("Layer"=>$row['id'],"LogicalLayer"=>$row['idLayer'],"UpperLayer"=>$row['idUpperLayer'],"State"=>(int)$row['stateLayer'],"CreateTime"=>$row['date_created'],"Desc"=>$row['description'],"isView"=>$isView,"Size"=>(int)$row['sizeLayer']);
                }                     
            }           
            if(isset($outputLayers) && isset($rootLayer))
            {                    
                // var_dump($outputLayers);
                // var_dump($rootLayer);
                $output[] = $rootLayer;
                $tmpUpperLayer = $rootLayer;
                if(count($outputLayers) > 0)
                {
                    do{                    
                        $tmpCurrentLayer = $outputLayers[$tmpUpperLayer['Layer']];
                        $output[] = $tmpCurrentLayer;
                        unset($outputLayers[$tmpUpperLayer['Layer']]);
                        $tmpUpperLayer = $tmpCurrentLayer;                    
                    }while(count($outputLayers) != 0);
                }
            }            
        }
        catch (Exception $e){                
            $output=null;
        }      
        return $output;
    }

    function listVDViewLayer($input)
    {
    	$rtn = -99;
    	if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDSSListViewLayer;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.$input['VDID'].'" ';                  
            // var_dump($cmd);
            exec($cmd,$output,$rtn);         
        }     
        if($rtn == 0)
            $rtn=json_decode($output[0],true);
        return $rtn;
    }

    function createTakeSnapshotTask($input,$type='take')
    {
        // var_dump($input);
    	$actionType = $this->snap_action_enum[$type];
    	$logCode = '';    	
    	$input['LayerOverwrite'] = $this->ssLayerIsOverwrite;
    	if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }    
        // $output_uuid = null;
        // $this->sqlGetUUID($output_uuid);       
        $this->newRelateClass();
        $outputDomain = $this->listDomainByidDomain($input['DomainID']);
        if(is_null($outputDomain)){
        	$logCode = '0A300201';
        	goto fail;
        }        
        $input['DomainName'] = $outputDomain['Name'];
        $outputVDInfo = $this->listVDInfoByidVD($input['VDID']);        
        if(is_null($outputVDInfo)){  
			$logCode = '0A300202';
        	goto fail;
        }
        $input['VDName'] = $outputVDInfo['VDName'];
        $input['SSLayerMax'] = $outputVDInfo['SSLayerMax'];
        // $input['ServerVDName']=$input['DomainID'].'_'.$outputVDInfo['VDNumber'].'_vd';        
        // var_dump($input);
        $rtn = $this->takeSnapshotShell($input,$type);            
        if((int)$rtn != 0){        
        	$logCode = '0A300204';
        	goto fail;
        }          
        $logCode = '0A100101';
        goto success;
        fail:
        	$this->insertSnapshotLog($actionType,$logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID']);
        	return CPAPIStatus::TakeSnapshotFail;
        success:        
        	$this->insertSnapshotLog($actionType,$logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID']);
        	return CPAPIStatus::TakeSnapshotSuccess;
    }

    function reportVDSSTaskTake($argv)
    {                 
        // var_dump($argv);
        $haveTask = false;
        $cancelTask = false;
        $actionType = $this->snap_action_enum['take'];
        $logCode = '0A300205';
        $rtnCode = 99;
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFinal;
        }                 
        $this->processTakeReportData($argv,$output);        
        if(!$this->sqlCheckVDExit($output['VDID'])){
            $rtnCode = 0;
            goto rtnFinal;
        }
        if(!is_null($output)){
            if($output['ErrCode'] != 0){
                if($data['ErrCode'] == 80 ){
                    $outputRoot = $this->sqlListRootByidVD($data['VDID']);               
                    if(is_null($outputRoot)){
                        $rtnCode = 2;
                        goto rtnFinal;
                    }                                  
                    if($outputRoot['LogicalLayer'] != $data['RootLayer'])
                        $rootSame = false;                                
                    if(!$rootSame){
                        connectDB::$dbh->beginTransaction();           
                        if(!$this->deleteSnapshotToRoot($data['VDID'],$data['RootLayer'])){
                            $rtnCode = 4;
                            goto failRollback;
                        }
                        connectDB::$dbh->commit();    
                    }                       
            }
                else if($output['ErrCode'] == 210 ){
                    $haveTask = true;
                    $cancelTask = true;
                    // var_dump($cancelTask);
                    $logCode = '0A100103';
                }
                $rtnCode = 0;
                goto rtnFinal;           
            }                           
            if(!$this->sqlGetUUID($outputUUID)){
                $rtnCode = 98;
                goto rtnFinal;
            }           
            $rootSame = true;
            if($output['isRootLayer']){                     
                $output['Layer'] = $outputUUID['UUID'];
                $output['UpperLayer'] = $output['Layer'];
            }
            else
            {                                                               
                $outputLayer = $this->sqlListSnapshotByLogicalLayerVDID($output['VDID'],$output['LogicalParentLayer']);                
                if(is_null($outputLayer)){
                    // $rtnCode = 1;
                    connectDB::$dbh->beginTransaction();
                    $vdC = new VDAction();
                    $rnt = $vdC->recreateVDSnapshot($output['VDID']);
                    if($rtn != 0){
                        connectDB::$dbh->rollBack();
                        $rtnCode = 1;
                    }
                    else{
                        $logCode = '0A100102';
                        connectDB::$dbh->commit();
                        $rtnCode = 0;
                    }                
                    goto rtnFinal;
                }                             
                $output['Layer'] = $outputUUID['UUID'];  
                $output['UpperLayer'] = $outputLayer['Layer'];  
                $outputRoot = $this->sqlListRootByidVD($output['VDID']);
                // var_dump($outputRoot);
                // var_dump($output);
                if(is_null($outputRoot)){
                    $rtnCode = 2;
                    goto rtnFinal;
                }                                  
                if($outputRoot['LogicalLayer'] != $output['RootLayer'])
                    $rootSame = false;             
            }
            connectDB::$dbh->beginTransaction();
            if(!$this->sqlInsertSanpshotLayer($output)){               
                $rtnCode = 3;              
                goto failRollback;
            }
            if(!$rootSame){                
                if(!$this->deleteSnapshotToRoot($output['VDID'],$output['RootLayer'])){
                    $rtnCode = 4;
                    goto failRollback;
                }
            }           
            $rtnCode = 0;
            $logCode = '0A100102';
            connectDB::$dbh->commit();
            goto rtnFinal;
        }
        else{
            $rtnCode = 97;
            goto rtnFinal;
        }            
        failRollback:
            connectDB::$dbh->rollBack();
            goto rtnFinal;
        rtnFinal:
            if($output['ErrCode'] == 0)
                $output['ErrCode'] = NULL;
            // var_dump($cancelTask);
            $this->insertSnapshotLog($actionType,$logCode,$output['VDName'],$output['DomainName'],$output['DomainID'],$output['LayerDesc'],$output['LayerDate'],$haveTask,$cancelTask,$output['ErrCode']);
            exit($rtnCode);
    }

    function recreateVDSnapshot($idVD)
    {
        $vdC = new VDAction();
        $vdC->sqlDeleteVDSnapshot($idVD);
        $data = array('ConnectIP'=>'127.0.0.1','VDID'=>$idVD);
        if($vdC->listVDWithSSShell($data,$output) != 0){
            $rtnCode = 1;            
            goto rtnFinal;
        }   
        $data['SS'] = $output['ss'];
        if($vdC->vdAddSnapshot($data,false) != 0){
            $rtnCode = 2;
        }            
        $rtnCode = 0;
        rtnFinal:
            return $rtnCode; 
    }

    function deleteSnapshotToRoot($idVD,$rootLayer)
    {        
        $result = false;
        $output = $this->sqlListSnapshotByidVD($idVD);
        if(!is_null($output))
        {            
            foreach ($output as $value) {
                if($value['LogicalLayer'] == $rootLayer)
                    break;
                if(!$this->sqlDeleteSnapshotLayer(array('VDID' => $idVD, 'Layer'=>$value['Layer'])))
                    goto rtnFinal;
            }
            $result = true;
        }
        rtnFinal:
            return $result;
    }

    function createDeleteSnapshotTask($input)
    {    	
    	$actionType = $this->snap_action_enum['delete'];
    	$logCode = '';    	
    	if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }    
        $this->newRelateClass();
        $outputDomain = $this->listDomainByidDomain($input['DomainID']);
        if(is_null($outputDomain)){
        	$logCode = '0A300401';
        	goto fail;
        }        
        $input['DomainName'] = $outputDomain['Name'];             
        $outputLayerInfo = $this->listSnapshotByidLayer($input['LayerID'],$input['DomainID']);
        if(is_null($outputLayerInfo)){  
			$logCode = null;
        	goto fail;
        }                
        if($this->sqlCheckLayerHaveView($outputLayerInfo['VDID'])){
            $logCode = '0A300407';
            goto fail;
        }
       	$this->generateVDLayerDate($input,$outputLayerInfo); 
        connectDB::$dbh->beginTransaction();
        if(!$this->sqlUpdateSnapshotLayerState($input['LayerID'],3)){
            connectDB::$dbh->rollBack();
            $logCode = '0A300406';
            goto fail;
        }
        $rtn = $this->deleteSnapshotShell($input);
        if($rtn != 0){        
            connectDB::$dbh->rollBack();
        	$logCode = '0A300404';
        	goto fail;
        }          
        connectDB::$dbh->commit();
        $logCode = '0A100301';
        goto success;
        fail:        	            
            if(!is_null($logCode)){
        	   $this->insertSnapshotLog($actionType,$logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],$outputLayerInfo['Desc'],$outputLayerInfo['CreateTime']);
            }
        	return CPAPIStatus::DeleteSnapshotFail;
        success:
        	$this->insertSnapshotLog($actionType,$logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],$outputLayerInfo['Desc'],$outputLayerInfo['CreateTime']);
        	return CPAPIStatus::DeleteSnapshotSuccess;
    }

    function reportVDSSTaskDel($argv)
    {
        $haveTask = false;
        $cancelTask = false;
        $actionType = $this->snap_action_enum['delete'];
        $logCode = '0A300405';
        $rtnCode = 99;
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFinal;
        }         
        $this->processSSReportData($argv,$output);        
        if(!is_null($output)){
            if($output['ErrCode'] != 0){
                if($output['ErrCode'] == 210 ){
                    $haveTask = true;
                    $cancelTask = true;                    
                    $logCode = '0A100303';
                }
                if(!$this->sqlUpdateSnapshotLayerState($output['Layer'],0))
                    $rtnCode = 2;
                else
                    $rtnCode = 0;
                goto rtnFinal;           
            }               
            if(!$this->sqlCheckVDExit($output['VDID'])){
                $rtnCode = 0;
                goto rtnFinal;
            }
            connectDB::$dbh->beginTransaction();
            if(!$this->sqlDeleteSnapshotLayer($output)){               
                $rtnCode = 1;              
                goto failRollback;
            }           
            $rtnCode = 0;
            $logCode = '0A100302';
            connectDB::$dbh->commit();
            goto rtnFinal;
        }
        else{
            $rtnCode = 97;
            goto rtnFinal;
        }            
        failRollback:
            connectDB::$dbh->rollBack();
            goto rtnFinal;
        rtnFinal:
            $this->insertSnapshotLog($actionType,$logCode,$output['VDName'],$output['DomainName'],$output['DomainID'],$output['LayerDesc'],$output['LayerDate'],$haveTask,$cancelTask,$output['ErrCode']);
            exit($rtnCode);
    }

    function createViewSnapshotTask($input)
    {
    	$actionType = $this->snap_action_enum['view'];
    	$logCode = '';    	
    	if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }    
       	$this->newRelateClass();
        $outputDomain = $this->listDomainByidDomain($input['DomainID']);
        if(is_null($outputDomain)){
        	$logCode = '0A300601';
        	goto fail;
        }        
        $input['DomainName'] = $outputDomain['Name'];        
        $outputLayerInfo = $this->listSnapshotByidLayer($input['LayerID'],$input['DomainID']);
        if(is_null($outputLayerInfo)){  
			$logCode = '0A300602';
        	goto fail;
        }        
       	$this->generateVDLayerDate($input,$outputLayerInfo); 
        connectDB::$dbh->beginTransaction();
        if(!$this->sqlUpdateSnapshotLayerState($input['LayerID'],1)){
            $logCode = '0A300605';
            goto fail;
        }  
        $rtn = $this->snapshotViewShell($input);
        if($rtn != 0){   
            connectDB::$dbh->rollBack();     
        	$logCode = '0A300604';
        	goto fail;
        }
        connectDB::$dbh->commit();
        $logCode = '0A100501';          
        goto success;
        fail:        	
        	$this->insertSnapshotLog($actionType,$logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],$outputLayerInfo['Desc'],$outputLayerInfo['CreateTime']);
        	return CPAPIStatus::ViewSnapshotFail;
        success:
        	$this->insertSnapshotLog($actionType,$logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],$outputLayerInfo['Desc'],$outputLayerInfo['CreateTime']);
        	return CPAPIStatus::ViewSnapshotSuccess;
    }

    function reportVDSSTaskView($argv)
    {
        $haveTask = false;
        $cancelTask = false;
        $actionType = $this->snap_action_enum['view'];
        $logCode = '0A300605';
        $rtnCode = 99;
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFinal;
        }         
        $this->processSSReportData($argv,$output);               
        if(!is_null($output)){           
            if($output['ErrCode'] != 0){
                if($output['ErrCode'] == 210 ){
                    $haveTask = true;
                    $cancelTask = true;                    
                    $logCode = '0A100503';
                }
                if(!$this->sqlUpdateSnapshotLayerState($output['Layer'],0))
                    $rtnCode = 2;
                else
                    $rtnCode = 0;                
                goto rtnFinal;           
            }         
            if(!$this->sqlCheckVDExit($output['VDID'])){
                $rtnCode = 0;
                goto rtnFinal;
            }          
            if(!$this->sqlUpdateCleanSnapshotViewLayerState($output['VDID'],0)){
                $rtnCode = 2;   
                goto rtnFinal;
            }
            if(!$this->sqlUpdateSnapshotLayerState($output['Layer'],2))
                $rtnCode = 2;   
            else{     
                $rtnCode = 0;
                $logCode = '0A100501';            
            }
            goto rtnFinal;
        }
        else{
            $rtnCode = 97;
            goto rtnFinal;
        }                    
        rtnFinal:
            $this->insertSnapshotLog($actionType,$logCode,$output['VDName'],$output['DomainName'],$output['DomainID'],$output['LayerDesc'],$output['LayerDate'],$haveTask,$cancelTask);
            exit($rtnCode);
    }

    function viewSnapshot($input)
    {
        // var_dump($input);
        $actionType = $this->snap_action_enum['view'];
        $logCode = '';      
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }    
        $this->newRelateClass();
        $outputDomain = $this->listDomainByidDomain($input['DomainID']);
        if(is_null($outputDomain)){
            $logCode = '0A300601';
            goto fail;
        }        
        $input['DomainName'] = $outputDomain['Name'];        
        $outputLayerInfo = $this->listSnapshotByidLayer($input['LayerID'],$input['DomainID']);
        if(is_null($outputLayerInfo)){  
            $logCode = '0A300602';
            goto fail;
        }        
        $input['VDName'] = $outputLayerInfo['VDName'];
        $this->generateVDLayerDate($input,$outputLayerInfo); 
        connectDB::$dbh->beginTransaction();
        if(!$this->sqlUpdateCleanSnapshotViewLayerState($outputLayerInfo['VDID'],0)){
            $logCode = '0A300605';
            goto fail;
        }
        if(!$this->sqlUpdateSnapshotLayerState($input['LayerID'],2)){
            $logCode = '0A300605';
            goto fail;
        }  
        $rtn = $this->directViewSnapshotShell($input);
        // var_dump($rtn);
        if($rtn != 0){               
            connectDB::$dbh->rollBack();     
            $logCode = '0A300604';
            goto fail;
        }
        connectDB::$dbh->commit();
        $logCode = '0A100501';          
        goto success;
        fail:           
            $this->insertSnapshotLog($actionType,$logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],$outputLayerInfo['Desc'],$outputLayerInfo['CreateTime'],false);
            return CPAPIStatus::ViewSnapshotFail;
        success:
            $this->insertSnapshotLog($actionType,$logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],$outputLayerInfo['Desc'],$outputLayerInfo['CreateTime'],false);
            return CPAPIStatus::ViewSnapshotSuccess;
    }

    function directViewSnapshotShell($input)
    {
        $rtn = -99;
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDSSView;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);          
            $cmd .= '"'.$input['VDID'].'" ';
            $cmd .= '"'.$input['LogicalLayer'].'" ';    
            $cmd .= '"'.$input['LayerID'].'" ';
            $nicParam = $input['KeepNIC'] == true ? 'keepNic' : 'noNic';
            $cmd .= '"'.$nicParam.'"'; 
            exec($cmd,$output,$rtn);                              

        }     
        return $rtn;
    }

    function loginSnapshot($input)
    {        
        // var_dump($input);
        // $gmt = $this->getTZ($timezone);
        // date_default_timezone_set($timezone);       
        if(!$this->connectDB()){
            http_response_code(400);
            exit();
        }           
        $outputLayer = $this->sqlListSnapshotByidLayer($input['LayerID']);
        // var_dump($outputLayer);
        if(is_null($outputLayer)){
            http_response_code(400);
            exit();
        }
        $vdC = new VDAction();        
        $outputVD = null;                        
        if(!$vdC->listVDByidVD($outputLayer['VDID'], $outputVD)){
            http_response_code (404);
            exit();
        }                   
        if(is_null($outputVD['OrgUserID']) && is_null($outputVD['UserUserID'])){
            http_response_code (404);
            exit();
        }            
        else{
            if(!is_null($outputVD['OrgUserID']))
                $userid = $outputVD['OrgUserID'];
            if(!is_null($outputVD['UserUserID']))
                $userid = $outputVD['UserUserID'];
        }
        $outputDomain = null;
        $domainC = new DomainAction();
        $domainC->sqlListDomainByID($outputVD['DomainID'], $outputDomain);           
        $userC = new UserAction();
        $outputUser = null;
        // var_dump($userid);
        // var_dump($outputVD);
        $userC->sqlListUserByIDAndidDomain($userid, $outputVD['DomainID'], $outputUser);
        if(is_null($outputUser)){
            http_response_code (401);
            exit();
        }
        if($outputUser['State'] == -1){
            http_response_code (403);
            exit();
        }
        $data['CephID'] = $input['CephID'];
        $data['DomainID'] = $outputVD['DomainID'];
        $data['VDID'] = $input['LayerID'];
        $data['VDs'] = array();
        $data['VDs'][] = $input['LayerID'];
        $data['ConnectIP'] = $input['ConnectIP'];
        $data['VDNumber']= $outputVD['VDNumber'];   
        $data['VDName'] = $outputVD['Name']."'s Snapshot View";
        $data['Password']=$outputUser['Password'];                 
        // var_dump(date("Y-m-d H:i:s"));
        if(!is_null($outputDomain['AD'])){
            $ad = base64_decode($outputDomain['AD']);                  
            $ad = json_decode($ad,true);               
            if($ad['Enable']){                
                $data['Password'] = file_get_contents("/mnt/tmpfs/ad/$userid");
            }
        }
        // var_dump($data);
        if($vdC->modifyVDPasswdShell($data) !=0 ){
            http_response_code (401);
            exit();
        }             
        // var_dump(date("Y-m-d H:i:s"));
        // var_dump($data);
        $vdC->poweronVD($data,$rtn,$outputUser['Name'],true);            
        // var_dump($rtn);
        if($rtn != 0 && $rtn != 91){
            switch ($rtn) {
                case 95:     
                    http_response_code(412);//out of memory
                    exit();                          
                case '94':
                    http_response_code(403);//Disable
                    exit();                
                case '93':
                    http_response_code(417);//Disable
                    exit();                   
                case '92':
                    http_response_code(406);//Over Maximum Connections
                    exit();                                        
                default:
                    http_response_code(503);
                    exit();
            }               
        }                        
        // var_dump($data);     
        $rtn = $vdC->poweronInfoShell($data,$output);
        if($rtn == 0 && isset($output['port'])){
            if($output['port']==''){
                http_response_code(503);
                exit();
            }            
            $server_c = array();
            // var_dump($output);            
            $this->sqlAddtbIpPortMappingSetTable();            
            $this->sqlListExternalByPort($input['NodeID'],$output['port'],$outputExternal);
            foreach ($output['ownerIP'] as $ip)
            {                                                
                $server_c[]=array('Address'=>$ip,'Port'=>(int)$output['port']);                
            }                                          
            foreach ($outputExternal as $ip)
            {                                                
                $server_c[]=array('Address'=>$ip['IP'],'Port'=>(int)$ip['Port']);                
            }   
            $this->output_json(array('SpiceServers'=>$server_c));
        }
        else{
            http_response_code(404);
            exit();
        }
            
    }

    function createStopViewSnapshotTask($input)
    {
        $actionType = $this->snap_action_enum['viewStop'];
        $logCode = '';     
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }    
        $this->newRelateClass();
        $outputDomain = $this->listDomainByidDomain($input['DomainID']);
        if(is_null($outputDomain)){
            $logCode = '0A300801';
            goto fail;
        }        
        $input['DomainName'] = $outputDomain['Name'];   
        $outputVDInfo = $this->listVDInfoByidVD($input['VDID']);      
        if(is_null($outputVDInfo)){  
            $logCode = '0A300802';
            goto fail;
        }
        connectDB::$dbh->beginTransaction();
        if(!$this->sqlUpdateSnapshotLayerState(null,5,$input['VDID'])){
            $logCode = '0A300805';
            goto fail;
        }  
        $input['VDName'] = $outputVDInfo['VDName'];            
        $rtn = $this->snapshotStopViewShell($input); 
        // var_dump($rtn);       
        if((int)$rtn != 0){      
            connectDB::$dbh->rollBack();    
            $logCode = '0A300804';
            goto fail;
        }   
        connectDB::$dbh->commit();         
        $logCode = '0A100701';
        goto success;
        fail:          
            $this->insertSnapshotLog($actionType,$logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID']);
            return CPAPIStatus::StopViewSnapshotFail;
        success:
            $this->insertSnapshotLog($actionType,$logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID']);
            return CPAPIStatus::StopViewSnapshotSuccess;
    }

    function reportVDSSTaskUnview($argv)
    {
        $haveTask = false;
        $cancelTask = false;
        $actionType = $this->snap_action_enum['viewStop'];
        $logCode = '0A300805';
        $rtnCode = 99;
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFinal;
        }         
        $this->processSSReportData($argv,$output);        
        if(!is_null($output)){           
            if($output['ErrCode'] != 0){
                if($output['ErrCode'] == 210 ){
                    $haveTask = true;
                    $cancelTask = true;                    
                    $logCode = '0A100703';
                }
                if(!$this->sqlUpdateSnapshotLayerState(null,2,$output['VDID']))
                    $rtnCode = 2;   
                else
                $rtnCode = 0;
                goto rtnFinal;           
            }              
            if(!$this->sqlCheckVDExit($output['VDID'])){
                $rtnCode = 0;
                goto rtnFinal;
            }    
            if(!$this->sqlUpdateSnapshotLayerState(null,0,$output['VDID']))
                $rtnCode = 2;   
            else{           
                $rtnCode = 0;
                $logCode = '0A100701';            
            }
            goto rtnFinal;
        }
        else{
            $rtnCode = 97;
            goto rtnFinal;
        }                    
        rtnFinal:
            $this->insertSnapshotLog($actionType,$logCode,$output['VDName'],$output['DomainName'],$output['DomainID'],NULL,NULL,$haveTask,$cancelTask);
            exit($rtnCode);
    }

    function stopViewSnapshot($input)
    {
        $actionType = $this->snap_action_enum['viewStop'];
        $logCode = '';     
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }    
        $this->newRelateClass();
        $outputDomain = $this->listDomainByidDomain($input['DomainID']);
        if(is_null($outputDomain)){
            $logCode = '0A300801';
            goto fail;
        }        
        $input['DomainName'] = $outputDomain['Name'];           
        $outputVDInfo = $this->listVDInfoByidVD($input['VDID']);      
        if(is_null($outputVDInfo)){  
            $logCode = '0A300802';
            goto fail;
        }
        connectDB::$dbh->beginTransaction();
        if(!$this->sqlUpdateCleanSnapshotViewLayerState($input['VDID'],0)){
            $logCode = '0A300805';
            goto fail;
        }  
        $input['VDName'] = $outputVDInfo['VDName'];            
        $rtn = $this->directUnviewSnapshotShell($input); 
        // var_dump($rtn);       
        if((int)$rtn != 0){      
            connectDB::$dbh->rollBack();    
            $logCode = '0A300804';
            goto fail;
        }   
        connectDB::$dbh->commit();         
        $logCode = '0A100701';
        goto success;
        fail:          
            $this->insertSnapshotLog($actionType,$logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],NULL,NULL,false);
            return CPAPIStatus::StopViewSnapshotFail;
        success:
            $this->insertSnapshotLog($actionType,$logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],NULL,NULL,false);
            return CPAPIStatus::StopViewSnapshotSuccess;
    }

    function directUnviewSnapshotShell($input)
    {
        $rtn = -99;
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDSSUnView;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);          
            $cmd .= '"'.$input['VDID'].'" ';       
            // var_dump($cmd);    
            exec($cmd,$output,$rtn);                         
            // var_dump($rtn);
        }     
        return $rtn;
    }

    function createRollbackSnapshotTask($input)
    {
        $actionType = $this->snap_action_enum['rollback'];
        $logCode = '';              
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }    
        $this->newRelateClass();
        $outputDomain = $this->listDomainByidDomain($input['DomainID']);
        if(is_null($outputDomain)){
            $logCode = '0A300A01';
            goto fail;
        }        
        $input['DomainName'] = $outputDomain['Name'];        
        $outputLayerInfo = $this->listSnapshotByidLayer($input['LayerID'],$input['DomainID']);
        if(is_null($outputLayerInfo)){  
            $logCode = null;
            goto fail;
        }        
        if($this->sqlCheckLayerHaveView($outputLayerInfo['VDID'])){
            $logCode = '0A300A07';
            goto fail;
        }
        $this->generateVDLayerDate($input,$outputLayerInfo); 
        connectDB::$dbh->beginTransaction();
        if(!$this->sqlUpdateSnapshotLayerState($input['LayerID'],4)){
            $logCode = '0A300A06';
            goto fail;
        }        
        $rtn = $this->rollbackSnapshotShell($input);
        if($rtn != 0){    
            connectDB::$dbh->rollBack();    
            $logCode = '0A300A04';
            goto fail;
        }          
        connectDB::$dbh->commit();
        $logCode = '0A100901';
        goto success;
        fail:           
            if(!is_null($logCode)){
               $this->insertSnapshotLog($actionType,$logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],$outputLayerInfo['Desc'],$outputLayerInfo['CreateTime']);
            }
            return CPAPIStatus::RollbackSnapshotFail;
        success:
            $this->insertSnapshotLog($actionType,$logCode,$input['VDName'],$outputDomain['Name'],$input['DomainID'],$outputLayerInfo['Desc'],$outputLayerInfo['CreateTime']);
            return CPAPIStatus::RollbackSnapshotSuccess;
    }

    function reportVDSSTaskRollback($argv)
    {
        $haveTask = false;
        $cancelTask = false;
        $actionType = $this->snap_action_enum['rollback'];
        $logCode = '0A300A05';
        $rtnCode = 99;
        if(!$this->connectDB()){
            $rtnCode = 98;
            goto rtnFinal;
        }         
        $this->processSSReportData($argv,$output);        
        if(!is_null($output)){
            if(!$this->sqlUpdateSnapshotLayerState($output['Layer'],0))
                $rtnCode = 2;
            else{
                if($output['ErrCode'] != 0){
                    if($output['ErrCode'] == 210 ){
                        $haveTask = true;
                        $cancelTask = true;                    
                        $logCode = '0A100903';
                    }
                    $rtnCode = 0;
                    goto rtnFinal;           
                }              
                // var_dump($output);
                if(!$this->sqlCheckVDExit($output['VDID'])){
                    $rtnCode = 0;
                    goto rtnFinal;
                }
                connectDB::$dbh->beginTransaction();
                if(!$this->sqlRollbackSnapshotLayer($output)){               
                    $rtnCode = 1;              
                    goto failRollback;
                }           
                $rtnCode = 0;
                $logCode = '0A100902';
                connectDB::$dbh->commit();
            }
            goto rtnFinal;
        }
        else{
            $rtnCode = 97;
            goto rtnFinal;
        }            
        failRollback:
            connectDB::$dbh->rollBack();
            goto rtnFinal;
        rtnFinal:
            $this->insertSnapshotLog($actionType,$logCode,$output['VDName'],$output['DomainName'],$output['DomainID'],$output['LayerDesc'],$output['LayerDate'],$haveTask,$cancelTask);
            exit($rtnCode);
    }

    function newRelateClass()
    {
        $this->domain_c = new DomainAction();   
        $this->vdC = new VDAction();     
    }

    function generateVDLayerDate(&$input,$input_layer_info)
    {
        $input['LogicalLayer'] = $input_layer_info['LogicalLayer'];
    	$input['VDID'] = $input_layer_info['VDID'];
        $input['VDName'] = $input_layer_info['VDName'];
        //$input['ServerVDName']=$input['DomainID'].'_'.$input_layer_info['VDNumber'].'_vd'; 
        $input['LayerDate']=$input_layer_info['CreateTime'];
        $input['LayerDesc'].=$input_layer_info['Desc']==''?'':$input_layer_info['Desc'];
    }

    function listSnapshotByidLayer($idLayer,$nowidDomain)
    {
    	$outputLayerInfo = null;
        $outputLayerInfo = $this->sqlListSnapshotByidLayer($idLayer); 
        // var_dump($outputLayerInfo);
        if(!is_null($outputLayerInfo)){
        	if($outputLayerInfo['DomainID'] != $nowidDomain)
        		$outputLayerInfo = null;
        }        	
        return $outputLayerInfo;
    }

    function sqlCheckVDExit($idVD)
    {
        $exit = true;
        $SQLGetVD = <<<SQL
            SELECT * FROM tbVDImageBaseSet WHERE idVD =:idVD
SQL;
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLGetVD);
            $sth->bindValue(':idVD', $idVD, PDO::PARAM_STR); 
            if($sth->execute())
            {        
                $exit = false;
                while( $row = $sth->fetch() ) 
                {
                    $exit = true;
                }                
            }                            
        }
        catch (Exception $e){                
        }               
        return $exit;
    }

    function sqlListSnapshotByidLayer($idLayer)
    {    	
    	$SQLList = <<<SQL
            SELECT t1.*,t2.nameVD,t2.idDomain,t2.idVdNumber FROM tbvdsnapshotset as t1
            INNER JOIN tbvdimagebaseset as t2
            ON t1.idSourceVD = t2.idVD 
            WHERE t1.id=:id;
SQL;
        try        
        {              
            $sth = connectDB::$dbh->prepare($SQLList);     
            $sth->bindValue(':id', $idLayer, PDO::PARAM_STR);                         
            if($sth->execute())
            {                                    
                while( $row = $sth->fetch() ) 
                {                  
                    $output = array("VDName"=>$row['nameVD'],"VDNumber"=>$row['idVdNumber'],"DomainID"=>(int)$row['idDomain'],"VDID"=>$row['idSourceVD'],"Layer"=>$row['id'],"LogicalLayer"=>$row['idLayer'],"UpperLayer"=>$row['idUpperLayer'],"State"=>(int)$row['stateLayer'],"CreateTime"=>$row['date_created'],"Desc"=>$row['description']);
                }                     
            }                     
        }
        catch (Exception $e){                
            $output=null;
        }      
        return $output;   
    }

    function sqlListSnapshotByLogicalLayerVDID($vdID,$logicalLayer)
    {
        $output=null;
        $SQLList = <<<SQL
            SELECT * FROM tbvdsnapshotset WHERE idSourceVD=:idSourceVD AND idLayer = :idLayer;
SQL;
        try        
        {            
            // var_dump($vdID);
            // var_dump($logicalLayer);
            $sth = connectDB::$dbh->prepare($SQLList);
            $sth->bindValue(':idSourceVD', $vdID, PDO::PARAM_STR);     
            $sth->bindValue(':idLayer', $logicalLayer, PDO::PARAM_STR);
            if($sth->execute())
            {                                    
                while( $row = $sth->fetch() ) 
                {                  
                    $output = array("VDID"=>$row['idSourceVD'],"Layer"=>$row['id'],"LogicalLayer"=>$row['idLayer'],"UpperLayer"=>$row['idUpperLayer'],"State"=>(int)$row['stateLayer'],"CreateTime"=>$row['date_created'],"Desc"=>$row['description']);
                }                     
            }                     
        }
        catch (Exception $e){                
            $output=null;
        }      
        return $output;   
    }

    function sqlListRootByidVD($idVD)
    {
        $output = NULL;
        $SQLList = <<<SQL
            SELECT * FROM tbvdsnapshotset WHERE idSourceVD=:idSourceVD AND id=idUpperLayer;
SQL;
        try        
        {              
            $sth = connectDB::$dbh->prepare($SQLList);     
            $sth->bindValue(':idSourceVD', $idVD, PDO::PARAM_STR);                         
            if($sth->execute())
            {              
                $output = array();                      
                while( $row = $sth->fetch() ) 
                {                  
                    $output = array("Layer"=>$row['id'],"LogicalLayer"=>$row['idLayer'],"UpperLayer"=>$row['idUpperLayer']);
                }                     
            }                     
        }
        catch (Exception $e){                
            $output=NULL;
        }      
        return $output;  
    }

    function sqlCheckLayerHaveView($idVD)
    {
        $SQLList = <<<SQL
            SELECT count(id) as count FROM tbvdsnapshotset WHERE idSourceVD=:idSourceVD AND (stateLayer = 1 OR stateLayer = 2)
SQL;
        try        
        {              
            $sth = connectDB::$dbh->prepare($SQLList);     
            $sth->bindValue(':idSourceVD', $idVD, PDO::PARAM_STR);                         
            if($sth->execute())
            {              
                $output = array();                      
                while( $row = $sth->fetch() ) 
                {                  
                    if($row['count'] != 0)
                        return true;
                }                     
            }                     
        }
        catch (Exception $e){                
        }      
        return false;  
    }

    function sqlInsertSanpshotLayer($input)
    {        
        $result = false;
        //`insert_snapshot`(OUT `Result` INT,`input_vd_id` longtext,`input_desc` longtext,`input_layer` longtext,`input_previous_layer` longtext, `input_create_time` longtext,`input_id` longtext)  
        $sqlInsert = <<<SQL
            call insert_snapshot(@res,:vdid,:desc,:layer,:parentlayer,:time,:id,:size);
SQL;
        $sqlSelectResult = <<<SQL
            SELECT @res as result;
SQL;
        try
        {                         
            // var_dump($input);       
            $sth = connectDB::$dbh->prepare($sqlInsert);            
            $sth->bindValue(':vdid', $input['VDID'], PDO::PARAM_STR); 
            $sth->bindValue(':desc', $input['LayerDesc'], PDO::PARAM_STR); 
            $sth->bindValue(':id', $input['Layer'], PDO::PARAM_STR); 
            $sth->bindValue(':layer', $input['LogicalLayer'], PDO::PARAM_STR); 
            $sth->bindValue(':parentlayer', $input['UpperLayer'], PDO::PARAM_STR);
            $sth->bindValue(':time', $input['CreateTime'], PDO::PARAM_STR);
            $sth->bindValue(':size', $input['Size'],PDO::PARAM_INT);
            if($sth->execute())
            {                      
                $query = connectDB::$dbh->query($sqlSelectResult,PDO::FETCH_ASSOC);
                $res = -1;
                foreach ($query as $x)
                {                                                            
                    $res=$x['result'];
                }                  
                // var_dump($res);              
                if(isset($res) && $res == 0)
                    $result = true;
            }               
        }
        catch (Exception $e){                
        }         
        // var_dump($result);
        return $result;
    }

    function sqlDeleteSnapshotLayer($input)
    {
        $result = false;
        $sqlDelete = <<<SQL
            call delete_snapshot(@res,:vdid,:layer)
SQL;
        $sqlSelectResult = <<<SQL
            SELECT @res as result;
SQL;
        try
        {                         
            // var_dump($input);       
            $sth = connectDB::$dbh->prepare($sqlDelete);            
            $sth->bindValue(':vdid', $input['VDID'], PDO::PARAM_STR); 
            $sth->bindValue(':layer', $input['Layer'], PDO::PARAM_STR);             
            if($sth->execute())
            {                      
                $query = connectDB::$dbh->query($sqlSelectResult,PDO::FETCH_ASSOC);
                $res = -1;
                foreach ($query as $x)
                {                                                            
                    $res=$x['result'];
                }                  
                // var_dump($res);              
                if(isset($res) && $res == 0)
                    $result = true;
            }               
        }
        catch (Exception $e){                
        }         
        // var_dump($result);
        return $result;
    }    

    function sqlRollbackSnapshotLayer($input)
    {
        $result = false;
        $sqlRollback = <<<SQL
            call rollback_snapshot(@res, :vdid,:layer)
SQL;
         $sqlSelectResult = <<<SQL
            SELECT @res as result;
SQL;
        try
        {                                 
            $sth = connectDB::$dbh->prepare($sqlRollback);            
            $sth->bindValue(':vdid', $input['VDID'], PDO::PARAM_STR); 
            $sth->bindValue(':layer', $input['Layer'], PDO::PARAM_STR);             
            if($sth->execute())
            {                      
                $query = connectDB::$dbh->query($sqlSelectResult,PDO::FETCH_ASSOC);
                $res = -1;
                foreach ($query as $x)
                {                                                            
                    $res=$x['result'];
                }                  
                // var_dump($res);              
                if(isset($res) && $res == 0)
                    $result = true;
            }               
        }
        catch (Exception $e){                
        }         
        // var_dump($result);
        return $result;
    }

    function sqlUpdateSnapshotLayerState($idLayer,$state,$idVDForUnview = null)
    {
        if(isset($idVDForUnview)){
            if($state == 0){
                $sqlUpdate = "Update tbvdsnapshotset SET stateLayer=:stateLayer WHERE idSourceVD = :idSourceVD AND stateLayer=5 ";
            }
            elseif ($state == 2) {
                $sqlUpdate = "Update tbvdsnapshotset SET stateLayer=:stateLayer WHERE idSourceVD = :idSourceVD AND stateLayer=5 ";
            }
            else{
            $sqlUpdate = "Update tbvdsnapshotset SET stateLayer=:stateLayer WHERE idSourceVD = :idSourceVD AND stateLayer=2";
            }
        }
        else{
            $sqlUpdate = "Update tbvdsnapshotset SET stateLayer=:stateLayer WHERE id = :id";
        }
        try
        {                      
            $sth = connectDB::$dbh->prepare($sqlUpdate);
            if(!isset($idVDForUnview))
                $sth->bindValue(':id',$idLayer, PDO::PARAM_STR); 
            else
                $sth->bindValue(':idSourceVD',$idVDForUnview, PDO::PARAM_STR); 
            $sth->bindValue(':stateLayer',$state, PDO::PARAM_INT);
            if($sth->execute())
                $result = true;            
        }
        catch (Exception $e){    
            $result = false;
        }     
        return $result;
    }

    function sqlUpdateCleanSnapshotViewLayerState($idVD,$state)
    {        
        $sqlUpdate = "Update tbvdsnapshotset SET stateLayer=:stateLayer WHERE idSourceVD = :idSourceVD AND stateLayer=2";                
        try
        {                      
            $sth = connectDB::$dbh->prepare($sqlUpdate);        
            $sth->bindValue(':idSourceVD',$idVD, PDO::PARAM_STR); 
            $sth->bindValue(':stateLayer',$state, PDO::PARAM_INT);
            if($sth->execute())
                $result = true;            
        }
        catch (Exception $e){    
            $result = false;
        }     
        return $result;
    }

    function list_clusterinfo_by_idGfs($gfs_id){
    	$output_cluster = null;
    	$this->vdC->sql_get_idCluster_by_idGfs($gfs_id,$output_id);
        if(!is_null($output_id)){                            
	        $rtn = $this->cluster_c->list_cluster_info_no_state($output_id,$output_cluster);
	        if($rtn != CPAPIStatus::ListClusterInfoSuccess){
	        	$output_cluster = null;
	        }
	    }
        return $output_cluster;
    }

    function listDomainByidDomain($domainID){
    	$outputDomain = null;
    	$this->domain_c->sqlListDomainByID($domainID, $outputDomain);
    	return $outputDomain;
    }

    function listVDInfoByidVD($vdID){
    	$outputVDInfo = null;
        $this->vdC->sqlListVDInfoByID($vdID,$outputVDInfo);   
        return $outputVDInfo;
    }   

    function insertSnapshotLog($type,$code,$vdName,$domainName,$domainID,$layerDesc=NULL,$layerDate=NULL,$isTask = true,$cancelTask=false,$errCode = NULL){
    	$logType = 10;
    	$logLevel = 3;
    	$message = '';
    	$appendVDDomain = '';
    	$appendErr = '';
    	$appendVDDomain = $this->logSnapshotAppendVDDomain($vdName,$domainName,$layerDesc,$layerDate);  
    	$appendErr = $this->rtnAppendErr($code); 
        $appendErrCode = '';
        if(isset($errCode)) {
            if($errCode == 91)
            {
                $appendErrCode = "(VD is poweron)";
            }
            else
                $appendErrCode = "($errCode)";          
        }
    	$message = $this->rtnMessage($type,$appendErr,$appendVDDomain,$isTask,$cancelTask);    	        
    	if(is_null($appendErr))
    		$logLevel = 1;    	
		if(LogAction::createLog(array('Type'=>$logType,'Level'=>$logLevel,'Code'=>$code,'Riser'=>'admin','Message'=>$message.$appendErrCode),$last_id)){
            LogAction::sqlInsertDomainLogRelation($last_id,$domainID);
        }    	
    }

    function rtnAppendErr($code)
    {
    	$appendErr = '';
    	switch($code){
    		case '0A100101':    
            case '0A100103': 
    		case '0A100301':   			
            case '0A100303':		
    		case '0A100501':
            case '0A100503':
            case '0A100701':
            case '0A100703':
            case '0A100901':
            case '0A100903':
            case '0A100102':
            case '0A100302':
            case '0A100502':
            case '0A100702':
            case '0A100902':
    			$appendErr = null;    			    			
    			break;
    		case '0A300201':
    		case '0A300401':
    		case '0A300601':
            case '0A300801':
            case '0A300A01':
    			$appendErr .= '(Failed to list domain)';    			
    			break;
    		case '0A300202':
            case '0A300802':
    			$appendErr .= '(Failed to list vd)';    			
    			break;
    		case '0A300402':
    		case '0A300602':
            case '0A300A02':
    			$appendErr .= '(Failed to list layer)';    			
    			break;
    		case '0A300203':
    		case '0A300403':
    		case '0A300603':
            case '0A300803':
            case '0A300A03':
    			$appendErr .= '(Failed to list cluster)';    			
    			break;    	
            case '0A300406':
            case '0A300605':
            case '0A300805':
            case '0A300A06':
                $appendErr .= '(Failed to update layer state)';               
                break;      
            case '0A300407':
            case '0A300A07':
                $appendErr .= '(VD in view)';
                break;      
    	}    	
    	return $appendErr;
    }

    function rtnMessage($type,$appendErr,$appendVDDomain,$isTask,$cancelTask)
    {        
    	switch ($type) {
    		case $this->snap_action_enum['take']:
            case $this->snap_action_enum['scheduleTake']:
    			return $this->assembleTakeInsertSnapshotLogMessage($appendErr,$appendVDDomain,$isTask,$cancelTask);
    			break;    		
    		case $this->snap_action_enum['delete']:
    			return $this->assembleDeleteInsertSnapshotLogMessage($appendErr,$appendVDDomain,$isTask,$cancelTask);
    			break; 
    		case $this->snap_action_enum['view']:
    			return $this->assembleSnapshotViewLogMessage($appendErr,$appendVDDomain,$isTask,$cancelTask);
    			break; 
            case $this->snap_action_enum['viewStop']:
                return $this->assembleSnapshotViewStopLogMessage($appendErr,$appendVDDomain,$isTask,$cancelTask);
                break; 
            case $this->snap_action_enum['rollback']:
                return $this->assembleRollbackSnapshotMessage($appendErr,$appendVDDomain,$isTask,$cancelTask);
                break;
    	}
    }

    function assembleTakeInsertSnapshotLogMessage($appendErr,$appendVDDomain,$isTask=true,$cancelTask)
    {	      
        $task = '';       
    	if(is_null($appendErr)){  
            $head = 'Take snapshot';        
            if($isTask){                
                if($cancelTask)      
                    $head = 'Cancel take snapshot';
                else
                $head = 'Create take snapshot';
                $task = ' task';
            }           
            if($cancelTask)
                return "$head$appendVDDomain$task";
            else
    		return "$head$appendVDDomain$task Success";
    	}
    	else{
            $head = 'Failed to take snapshot';
            if($isTask){
                $head = 'Failed to create take snapshot';
                $task = ' task';
            }            
    		return "$head$appendVDDomain$task$appendErr";
    	}
    }

    function assembleDeleteInsertSnapshotLogMessage($appendErr,$appendVDDomain,$isTask,$cancelTask)
    {	
        $task = '';       
    	if(is_null($appendErr)){
            $head = 'Delete snapshot';        
            if($isTask){                
                if($cancelTask)      
                    $head = 'Cancel delete snapshot';
                else           
                $head = 'Create delete snapshot';
                $task = ' task';
            }           
            if($cancelTask)
                return "$head$appendVDDomain$task";
            else
    		return "$head$appendVDDomain$task Success";
    	}
    	else{
            $head = 'Failed to delete snapshot';
            if($isTask){
                $head = 'Failed to create delete snapshot';
                $task = ' task';
            }   
    		return "$head$appendVDDomain$task$appendErr";
    	}
    }

	function assembleSnapshotViewLogMessage($appendErr,$appendVDDomain,$isTask,$cancelTask)
    {	
        $task = '';
    	if(is_null($appendErr)){
            $head = 'View snapshot';        
            if($isTask){                
                if($cancelTask)      
                    $head = 'Cancel view snapshot';
                else            
                $head = 'Create view snapshot';
                $task = ' task';
            }      
            if($cancelTask)
                return "$head$appendVDDomain$task";
            else
    		return "$head$appendVDDomain$task Success";
    	}
    	else{
            $head = 'Failed to view snapshot';        
            if($isTask){                
                $head = 'Failed to create view snapshot';
                $task = ' task';
            }      
    		return "$head$appendVDDomain$task$appendErr";
    	}
    }

    function assembleSnapshotViewStopLogMessage($appendErr,$appendVDDomain,$isTask,$cancelTask)
    {   
        $task = '';
        if(is_null($appendErr)){
            $head = 'Stop view snapshot';        
            if($isTask){                
                if($cancelTask)      
                    $head = 'Cancel stop view snapshot';
                else         
                $head = 'Create stop view snapshot';
                $task = ' task';
            }      
            if($cancelTask)
                return "$head$appendVDDomain$task";
            else
            return "$head$appendVDDomain$task Success";
        }
        else{
            $head = 'Failed to stop view snapshot';        
            if($isTask){                
                $head = 'Failed to create stop view snapshot';
                $task = ' task';
            }  
            return "$head$appendVDDomain$task$appendErr";
        }
    }

    function assembleRollbackSnapshotMessage($appendErr,$appendVDDomain,$isTask,$cancelTask)
    {   
        $task='';
        if(is_null($appendErr)){
            $head = 'Rollback snapshot';        
            if($isTask){                
                if($cancelTask)      
                    $head = 'Cancel rollback snapshot';
                else            
                $head = 'Create rollback snapshot';
                $task = ' task';
            }           
            if($cancelTask)
                return "$head$appendVDDomain$task";
            else
            return "$head$appendVDDomain$task Success";
        }
        else{
            $head = 'Failed to rollback snapshot';
            if($isTask){
                $head = 'Failed to create rollback snapshot';
                $task = ' task';
            }   
            return "$head$appendVDDomain$task$appendErr";
        }
    }

    function snapshotViewShell($input)
    {
    	$rtn = -99;
    	if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDSSTaskView;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.base64_encode('view$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['LayerID'].'$*'.$input['LayerDate'].'$*'.base64_encode($input['LayerDesc'])).'" ';
            $cmd .= '"'.$input['VDID'].'" ';
            $cmd .= '"'.$input['LogicalLayer'].'" ';    
            // var_dump($cmd);        
            exec($cmd,$output,$rtn);                                      
        }     
        return $rtn;
    }

    function deleteSnapshotShell($input)
    {
        // var_dump($input);
    	$rtn = -99;
    	if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDSSTaskDelete;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.base64_encode('delete$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['LayerID'].'$*'.$input['LayerDate'].'$*'.base64_encode($input['LayerDesc'])).'" ';
            $cmd .= '"'.$input['VDID'].'" '; 
            $cmd .= $input['LogicalLayer'];                   
            // var_dump($cmd);
            exec($cmd,$output,$rtn);
        }     
        return $rtn;
    }

    function takeSnapshotShell($input,$type)
    {
    	$rtn=-99;   
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDSSTaskTake;            
            $cmd .= '"'.base64_encode($type.'$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.base64_encode($input['Desc'])).'" ';                              
            $cmd .= '"'.$input['VDID'].'" ';                              
            $cmd .= $input['SSLayerMax'].' ';
            $cmd .= $input['LayerOverwrite'] == true ? 1 : 0;   
            // var_dump($cmd);          
            exec($cmd,$output,$rtn);                                      
            // var_dump($rtn);
        }             
        return $rtn;
    }

    function snapshotStopViewShell($input)
    {
        $rtn = -99;
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDSSTaskViewStop;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.base64_encode('viewStop$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName']).'" ';
            $cmd .= '"'.$input['VDID'].'" ';        
            // var_dump($cmd);        
            exec($cmd,$output,$rtn);                                       
        }     
        return $rtn;
    }

    function rollbackSnapshotShell($input)
    {
        $rtn = -99;
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDSSTaskRollback;
            // var_dump($input);
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd); 
            $cmd .= '"'.base64_encode('rollback$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['LayerID'].'$*'.$input['LayerDate'].'$*'.base64_encode($input['LayerDesc'])).'" ';;            
            $cmd .= '"'.$input['VDID'].'" ';  
            $cmd .= '"'.$input['LogicalLayer'].'" ';              
            // var_dump($cmd);        
            $rtn = shell_exec($cmd);                                    
        }     
        return $rtn;
    }

    function processSnapshotErrorCode($code){  
        $this->baseOutputResponse($code);
        switch ($code){           
            case CPAPIStatus::DeleteSnapshotFail:  
            case CPAPIStatus::TakeSnapshotFail:  
            case CPAPIStatus::ListSnapshotFail:   
            case CPAPIStatus::ViewSnapshotFail:
            case CPAPIStatus::StopViewSnapshotFail:
            case CPAPIStatus::RollbackSnapshotFail:
                http_response_code(400);
                break; 
        }
    }
}
