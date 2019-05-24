<?php
class NodeAction extends BaseAPI
{
	private $cmdClusterControlNodeIPList = CmdRoot."127.0.0.1 cluster_controlNode_ip_list ";
	private $cmdClusterNodeListInfoAll = CmdRoot."ip cluster_node_list_info_all ";
    private $cmdClusterNodeListStateAll = CmdRoot."ip cluster_node_list_state_all ";
    private $cmdClusterNodeListInfo = CmdRoot."ip cluster_node_list_info ";
    private $cmdNodeListInfoLocal = CmdRoot."ip node_list_info_local ";
    private $cmdClusterNodeListUtil = CmdRoot."ip cluster_node_list_util ";
    private $cmdClusterNodeReboot = CmdRoot."ip cluster_node_reboot ";
    private $cmdClusterNodeShutdown = CmdRoot."ip cluster_node_shutdown ";
    private $cmdClusterListInfo = CmdRoot."ip cluster_list_info";
    private $cmdClusterNodeSetMaintain = CmdRoot."ip cluster_node_set_maintain ";
    private $cmdClusterNodeSetMaintainCheck = CmdRoot."ip cluster_node_set_maintain_check ";
    // private $cmdClusterNodeListPoweronVD = CmdRoot."ip cluster_node_list_poweron_vd ";
    private $cmdClusterListPoweronVD = CmdRoot."ip cluster_list_poweron_vd ";
    private $cmdClusterrSplitVMS = CmdRoot.'ip cluster_split_recovery ';
    private $cmdClusterVMSSet = CmdRoot.'ip cluster_vms_set';
    private $cmdClusterSplitListInfo = CmdRoot.'ip cluster_split_list_info';
    private $cmdNodeFirewallEnableSSH = CmdRoot.'ip node_firewall_enable_ssh';
    private $cmdNodeFirewallDisableSSH = CmdRoot.'ip node_firewall_disable_ssh';
    private $cmdNodeFirewallSSHStatus = CmdRoot.'ip node_firewall_ssh_status';
    private $cmdClusterNodeModeSet = CmdRoot.'ip cluster_node_mode_set ';
    private $cmdClusterNodeVLANListUtil = CmdRoot.'ip cluster_node_vlan_list_util ';
    private $maintainStateEnum = array ("splitDiscard"=>4,"split"=>3,"update"=>2,"yes" => 1,'no'=>0,'notReady'=>-1);     
    function  __construct()
    {    
        set_time_limit(0);         
    }
        
    function initInsertNode(&$input,&$outputNodeID,$localCmd=false)
    {
        $this->connectDB();        
        if(connectDB::$dbh == null){
            return false;
        }        
        // echo 0;
        // var_dump($input);
        if(!$this->sqlInsertNode($input))
            return false;
        // echo 4;
        // $input['NodeName'] = $input['CommunicateIP'];
        $nodeID = connectDB::$dbh->lastInsertId();
        $outputNodeID = $nodeID;
        $input['NodeID'] = $nodeID;
        if(!$this->listNodeInfoShell($input,$outputInfo,$localCmd))
            return false;
        // echo 1;
        $outputInfo['NodeID'] = $nodeID;
        // var_dump($outputInfo);
        if(!$this->sqlInsertNodeInfo($outputInfo))
            return false;
        // echo 'fin 2';
        // var_dump($outputInfo);
        return true;        
    }

    function initUpdateNode($input)
    {
        $this->connectDB();        
        if(connectDB::$dbh == null){
            return false;
        }                       
        if(!$this->listNodeInfoShell($input,$outputInfo))
            return false;
        // echo 1;
        $outputInfo['NodeID'] = $input['NodeID'];
        // var_dump($outputInfo);
        if(!$this->sqlUpdateNodeInfo($outputInfo))
            return false;
        // echo 'fin 2';
        // var_dump($outputInfo);
        return true;   
    }

    function reinitInsertNode($input,$localCmd=false)
    {
        $this->connectDB();        
        if(connectDB::$dbh == null){
            return false;
        }                        
        if(!$this->listNodeInfoShell($input,$outputInfo,$localCmd))
            return false;
        // var_dump($outputInfo);
        // echo 1;
        $outputInfo['NodeID'] = $input['NodeID'];
        // var_dump($outputInfo);            
        if(!$this->sqlInsertNodeInfo($outputInfo)){            
            return false;
        }
        // echo 'fin 2';
        // var_dump($outputInfo);
        return true;        
    }

    function sqlInsertNode($input)
    {
        $result = false;
        $SQLInsertNode = "INSERT tbVDServerSet (idCephPool,nameNode,address,isOnline,date_created,date_modified,roleVDServer,idStoragePair,stateVDServer) Values (1,:nameNode,:address,true,Now(),Now(),:roleVDServer,:idStoragePair,:stateVDServer)";
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLInsertNode);                      
            $sth->bindValue(':nameNode', $input['NodeName'], PDO::PARAM_STR);            
            $sth->bindValue(':address', $input['IP'], PDO::PARAM_STR);
            $sth->bindValue(':roleVDServer', $input['Role'], PDO::PARAM_INT);
            $sth->bindValue(':idStoragePair', $input['PairID'], PDO::PARAM_INT);
            if(!isset($input['State']))
                $stateVDServer = 0;
            else
                $stateVDServer = $input['State'];
            $sth->bindValue(':stateVDServer', $stateVDServer, PDO::PARAM_INT);
            $result = $sth->execute();   
        }
        catch (Exception $e){                
        }        
        return $result;
    }

    function sqlDeleteNodeInfo($input)
    {
        $result = false;
        $sqlDelete = <<<SQL
               DELETE FROM tbVDServerInfoSet WHERE idVDServer=:idVDServer
SQL;
        try
        {                                
            $sth = connectDB::$dbh->prepare($sqlDelete);            
            $sth->bindValue(':idVDServer', $input['NodeID'], PDO::PARAM_INT);             
            if($sth->execute())
            {                               
                $result = true;
            }               
        }
        catch (Exception $e){                
        }         
        return $result;
    }

    function sqlInsertNodeInfo($input)
    {
        $result = false;
        $SQLInsert = <<<SQL
               INSERT tbVDServerInfoSet (idVDServer,hzCpu,numSocket,numCore,numThread,sizeRam,nameFirmware) 
                   Values (:idVDServer,:hzCpu,:numSocket,:numCore,:numThread,:sizeRam,:verFirmware)
SQL;
        try
        {                 
            // var_dump($input);               
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':idVDServer', $input['NodeID'], PDO::PARAM_INT); 
            $sth->bindValue(':hzCpu', $input['cpuHz']*1000, PDO::PARAM_INT); 
            $sth->bindValue(':numSocket', $input['cpuCount'], PDO::PARAM_INT); 
            $sth->bindValue(':numCore', $input['cpuCores'], PDO::PARAM_INT); 
            $sth->bindValue(':numThread', $input['cpuThreads'], PDO::PARAM_INT);
            $ram = floor(($input['memTotal']/1024));
            $sth->bindValue(':sizeRam', $ram, PDO::PARAM_INT);
            if(is_null($input['swversion']))
                $swversion = '';
            else
            {
                $swversion = $input['swversion'];
            }
            $sth->bindValue(':verFirmware', $swversion, PDO::PARAM_STR);
            if($sth->execute())
            {               
                // var_dump($sth->rowCount());
                if($sth->rowCount() == 1)
                    $result = true;
            }               
        }
        catch (Exception $e){                
        }         
        return $result;
    }

    function sqlUpdateNodeInfo($input)
    {
        $result = false;
        $sqlUpdate = <<<SQL
            UPDATE tbVDServerInfoSet set hzCpu=:hzCpu,numSocket=:numSocket,numCore=:numCore,numThread=:numThread,sizeRam=:sizeRam,nameFirmware=:verFirmware WHERE idVDServer=:idVDServer;               
SQL;
        try
        {                 
            // var_dump($input);         
            $sth = connectDB::$dbh->prepare($sqlUpdate);            
            $sth->bindValue(':idVDServer', $input['NodeID'], PDO::PARAM_INT); 
            $sth->bindValue(':hzCpu', $input['cpuHz']*1000, PDO::PARAM_INT); 
            $sth->bindValue(':numSocket', $input['cpuCount'], PDO::PARAM_INT); 
            $sth->bindValue(':numCore', $input['cpuCores'], PDO::PARAM_INT); 
            $sth->bindValue(':numThread', $input['cpuThreads'], PDO::PARAM_INT);
            $ram = floor(($input['memTotal']/1024));
            $sth->bindValue(':sizeRam', $ram, PDO::PARAM_INT);
            if(is_null($input['swversion']))
                $swversion = '';
            else
            {
                $swversion = $input['swversion'];
            }
            $sth->bindValue(':verFirmware', $swversion, PDO::PARAM_STR);
            if($sth->execute())
            {                               
                $result = true;
            }               
        }
        catch (Exception $e){                
        }         
        return $result;
    }

    function sqlUpdateNodeName($input)
    {
        $result = false;
        $SQLInsert = <<<SQL
            UPDATE tbvdserverset set nameNode=:nameNode WHERE idVDServer=:idVDServer;               
SQL;
        try
        {                 
            // var_dump($input);               
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':idVDServer', $input['NodeID'], PDO::PARAM_INT);             
            $sth->bindValue(':nameNode', $input['NodeName'], PDO::PARAM_INT);            
            if($sth->execute())
            {                               
                $result = true;
            }               
        }
        catch (Exception $e){                
        }         
        return $result;
    }

	function listNodesForLoginCheck(&$output)
	{
		if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }   
        if(!$this->clusterControlNodeIPListShell($outputIP)){
        	return CPAPIStatus::ListNodesForLoginCheckFail;
        }     
        $output = array('IP' =>$outputIP);        
        return CPAPIStatus::ListNodesForLoginCheckSuccess;    
	}

    function clusterControlNodeIPListShell(&$outputIP)
    {        
        $cmd = $this->cmdClusterControlNodeIPList;    
        // var_dump($cmd);          
        exec($cmd,$outputArr,$rtn);
        if($rtn == 0){   
            $outputIP = $outputArr[0];               
            if (!filter_var($outputIP, FILTER_VALIDATE_IP) === false) {                 
                return true;                
            }
        }                                 
        return false;
    }

    function listNodes($input,&$output)
    {                
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }           
        if(!$this->sqlListServers($outputServers,true))
            return CPAPIStatus::ListNodesFail;       
        if(!$this->listStateAllShell($input,$outputInfos))
            return CPAPIStatus::ListNodesFail;
        // var_dump($outputInfos);
        $output = array();
        $changeSpitDiscard = array();
        foreach ($outputInfos['info'] as $value) {                       
            $ip = '';
            $id = NULL;
            $nodeName = '';
            if(array_key_exists($value['nodeName'], $outputServers)){
                $ip = $outputServers[$value['nodeName']]['IP'];
                $id = $outputServers[$value['nodeName']]['NodeID'];
                $role = $outputServers[$value['nodeName']]['Role'];
                $pairID = $outputServers[$value['nodeName']]['PairID'];
                $nodeName = $outputServers[$value['nodeName']]['NodeName'];
            }
            else{
                continue;
            }
            if($value['controlState'] == 'yes' || $value['masterState'] == 'yes')
                $controlNode = true;
            else
                $controlNode = false;
            if($value['updateState'] == 'yes')
                $maintainState = $this->maintainStateEnum['update'];    
            else
                $maintainState = $this->maintainStateEnum[$value['maintainState']];   
            if($value['splitState'] == 'split')
                $maintainState = $this->maintainStateEnum['split'];    
            else
                $maintainState = $this->maintainStateEnum[$value['maintainState']];
            switch($value['connectState']){
                case 'boot':
                    $onlineState = 1;
                    break;
                case 'connected':
                    $onlineState = 2;
                    break;                
                case 'waitRemoteMaster':
                    $onlineState = 3;
                    break;
                case 'noGluster':
                    $onlineState = 4;
                    break;
                case 'noRaid':
                    $onlineState = 5;
                    break;
                case 'raidCrash':
                    $onlineState = 6;
                    break;
                default:
                    $onlineState = 0;
                    break;
            }             
            if($pairID == $tmpPairID && $tmpSplitState=='split' && $value['splitState'] == 'split'){
                $changeSpitDiscard[$pairID] = $tmpSplitState;
            }
            $tmpPairID = $pairID;
            $tmpSplitState = $value['splitState'];
            $output[]=array('NodeID'=>$id,'IP'=>$ip,'NodeName'=>$nodeName,'ControlNode'=>$controlNode,'MaintainState'=>$maintainState,'OnlineState'=>$onlineState,'HealCount'=>(int)$value['healInfoCt'],'SplitCount'=>(int)$value['healSplitCt'],'Role'=>$role,'PairID'=>$pairID,'MaxPoweronVD'=>(int)$value['maxPoweron'],'CommunicateIP'=>$value['nodeName']);
        }
        // var_dump($changeSpitDiscard);
        foreach ($output as &$value) {
            if(array_key_exists($value['PairID'], $changeSpitDiscard)){
                // var_dump($value);
                $value['maintainState'] = $this->maintainStateEnum['splitDiscard'];
            }
        }
        return CPAPIStatus::ListNodesSuccess;
    }

    function listStateAllShell($input,&$output)
    {
        $cmd = $this->cmdClusterListInfo;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);  
        // var_dump($cmd);          
        exec($cmd,$outputArr,$rtn);       
        // var_dump($outputArr);
        if($rtn == 0){              
            $output = json_decode($outputArr[0],true);
            // var_dump($output);
            return true;      
        }            
        return false;
    }

    function listInfoAllShell($input,&$output)
    {
        $cmd = $this->cmdClusterNodeListInfoAll;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
        exec($cmd,$outputArr,$rtn);
        if($rtn == 0){              
            $output = json_decode($outputArr[0],true);
            return true;      
        }            
        return false;
    }

    function listSplitNodes($input,&$output)
    {
        // var_dump($input);
        if(!$this->listStateAllShell($input,$outputInfos))
            return CPAPIStatus::ListNodesFail;
        // var_dump($outputInfos);
        $output = array();
        foreach ($outputInfos['info'] as $value) {
            if($value['groupId'] == 1)
                $output[]=array('NodeName'=>$value['nodeAlias'],'CommunicateIP'=>$value['nodeName']);   
        }
        return CPAPIStatus::ListNodesSuccess;        
    }
    
    function listClusterSplitInfoShell($input,&$output)
    {
        $cmd = $this->cmdClusterSplitListInfo;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
        exec($cmd,$outputArr,$rtn);
        if($rtn == 0){              
            $output = json_decode($outputArr[0],true);
            return true;      
        }            
        return false;
    }

    function reserveSplitNode($input)
    {
        // var_dump($input);
        if($this->reserveSplitNodeShell($input) == 0){
            return CPAPIStatus::ReserveNodeSuccess;            
        }
        else{
            return CPAPIStatus::ReserveNodeFail;
        }
    }

    function reserveSplitNodeShell($input)
    {        
        $cmd = $this->cmdClusterrSplitVMS;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= $input['CommunicateIP'];
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($outputArr);       
        return $rtn;
    }

    function nodeSetVMS($input)
    {
        if($this->setVMSShell($input) == 0){
            return CPAPIStatus::SetNodeVMSSuccess;            
        }
        else{
            return CPAPIStatus::SetNodeVMSFail;
        }
    }

    function setVMSShell($input)
    {
        $cmd = $this->cmdClusterVMSSet;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        exec($cmd,$outputArr,$rtn);        
        return $rtn;
    }

    function changeNodeRole($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        if(!$this->sqlListServerByidVDServer($input['NodeID'],$outputServer))
            return CPAPIStatus::ChangeNodeRoleFail;
        $input['CommunicateIP'] = $outputServer['CommunicateIP'];
        connectDB::$dbh->beginTransaction();
        if(!$this->sqlUpdateNodeRole($input))
            goto fail;
        if($this->setNodeRoleShell($input) != 0)
            goto fail;
        connectDB::$dbh->commit();
        return CPAPIStatus::ChangeNodeRoleSuccess;
        fail:
            connectDB::$dbh->rollBack();
            return CPAPIStatus::ChangeNodeRoleFail;
    }

    function sqlUpdateNodeRole($input)
    {
        $result = false;
        $sqlUpdate = <<<SQL
            UPDATE tbVDServerSet SET roleVDServer=:roleVDServer WHERE idVDServer = :idVDServer
SQL;
        try
        {               
            // var_dump($input);                 
            $sth = connectDB::$dbh->prepare($SQLInsert);
            $sth->bindValue(':idVDServer', $input['NodeID'], PDO::PARAM_INT); 
            $sth->bindValue(':roleVDServer', $input['Role'], PDO::PARAM_INT);
            if($sth->execute())
            {                               
                $result = true;
            }                          
        }
        catch (Exception $e){                
        }         
        return $result;
    }

    function setNodeRoleShell($input)
    {
        $cmd = $this->cmdClusterNodeModeSet;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= $input['CommunicateIP'].' ';
        $cmd .= $input['Role'] == 1 ? 'hybrid' : 'storage';
    }

    function listNodeInfo($input,&$output)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }   
        $output = array();
        $output['FirmwareVer'] = NULL;
        $output['CPU'] = array('Count'=>NULL,"Clock"=>NULL,"Model"=>NULL,"Cores"=>NULL,"Threads"=>NULL); 
        $output['Mem'] = array('Total'=>NULL,"Clocks"=>array());
        $output['NIC'] = array();
        $output['VGACount']=0;
        if($this->sqlListServerByidVDServer($input['NodeID'],$outputServer))
        {
            // echo 1;
            $input['CommunicateIP'] = $outputServer['CommunicateIP'];
            if($this->listNodeInfoShell($input,$outputInfo))
            {
                // var_dump($outputInfo);
                // echo 2;
                if(isset($outputInfo['cpuCount'])){
                    $output['CPU']['Count'] = (int)$outputInfo['cpuCount'];
                    $output['CPU']['Clock'] = floatval($outputInfo['cpuHz']);
                    $output['CPU']['Model'] = $outputInfo['cpuModel'];
                    $output['CPU']['Cores'] = (int)$outputInfo['cpuCores'];
                    $output['CPU']['Threads'] = (int)$outputInfo['cpuThreads'];
                    $output['Mem']['Total'] = (int)$outputInfo['memTotal'];
                    foreach ($outputInfo['memClock'] as $value) {
                        $output['Mem']['Clocks'][] = array('Bank' => (int)$value['Bank'], 'Clock'=>$value['memClock']);
                    }
                    foreach ($outputInfo['NIC'] as $value) {
                        $online = $value['Online'] == 'yes' ? true : false;
                        $output['NIC'][] = array('Name' => $value['Name'], 'Online'=>$online,'Speed'=>$value['Speed']);
                    }
                    $output['FirmwareVer'] = $outputInfo['swversion'];                    
                }
            }
        }
        return CPAPIStatus::ListNodesSuccess;
    }

    function listNodeInfoShell($input,&$output,$localCmd = false)
    {        
        if($localCmd)
            $cmd = $this->cmdNodeListInfoLocal;
        else
            $cmd = $this->cmdClusterNodeListInfo;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        if(!$localCmd)
            $cmd .= $input['CommunicateIP'];
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($rtn);
        // var_dump($outputArr);
        if($rtn == 0){              
            $output = json_decode($outputArr[0],true);
            return true;      
        }            
        return false;
    }

    function listNodePolling($input,&$output)
    {
        // var_dump($input);
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }   
        $this->sqlListAllVswitchsWithBrNameKey($outputVswitch);
        if(is_null($outputVswitch)){
            return CPAPIStatus::DBConnectFail;
        }
        // var_dump($outputVswitch);
        $output = array();
        $output['CPU'] = array();  
        $output['Mem'] = array('Total'=>NULL,"Used"=>NULL);
        $output['NICUtil'] = array();
        $output['NIC'] = array();     
        $output['RAID'] = array();
        $output['VGA'] = array('Total'=>NULL,"Used"=>NULL);
        if($this->sqlListServerByidVDServer($input['NodeID'],$outputServer))
        {            
            $input['CommunicateIP'] = $outputServer['CommunicateIP'];
            if($this->listNodeVLANUtilShell($input,$outputUtil))
            {                
                // var_dump($outputUtil);
                if(isset($outputUtil['memTotal'])){
                    foreach ($outputUtil['cpu'] as $cpu) {
                        $output['CPU'][] = floatval($cpu['util']);
                    }
                    $tmpNICUtil=array();
                    foreach ($outputUtil['nic'] as $nicUtil) {
                        $key=NULL;
                        if (strpos($nicUtil['name'], 'br') !== FALSE){
                            $key=str_replace('br', '', $nicUtil['name']);
                        }
                        if (strpos($nicUtil['name'], 'bond') !== FALSE){
                            $key=str_replace('bond', '', $nicUtil['name']);
                        }                                    
                        $key="br$key";
                        if(isset($key)&&array_key_exists($key, $outputVswitch)){                        
                            // $idVswitch = $outputVswitch[$key];
                            $rx = floatval($nicUtil['rx']);
                            $tx = floatval($nicUtil['tx']);                            
                            if(array_key_exists($key, $tmpNICUtil)){
                                if($rx > $tmpNICUtil[$key]['Rx']){
                                    // var_dump($tmpNICUtil[$key]['Rx']);
                                    // var_dump($rx);
                                    $tmpNICUtil[$key]['Rx'] = $rx;
                                }
                                if($tx > $tmpNICUtil[$key]['Tx']){                                    
                                    $tmpNICUtil[$key]['Tx'] = $tx;
                                }
                            }else{
                                $tmpNICUtil[$key] = array('VswitchID'=>$outputVswitch[$key]['VswitchID'],'Name'=>$outputVswitch[$key]['Name'],'Rx'=>$rx,'Tx'=>$tx);
                            }
                            // var_dump($nicUtil);
                            // var_dump($tmpNICUtil);
                        }
                    }
                    $output['NICUtil']=array_values($tmpNICUtil);
                    $output['Mem']['Total']=(int)$outputUtil['memTotal'];
                    $output['Mem']['Used']=(int)$outputUtil['memUsed'];
                }                
                $zfsC = new ZfsAction();
                $zfsC->listZfsForSAN($input,$outputZfs);                  
                if(!is_null($outputZfs) && isset($outputZfs[0]['Used'])){
                    $output['RAID'] = $outputZfs;
                }
                else
                    $output['RAID'] = array();   
            }
            if($this->listNodeInfoShell($input,$outputInfo))
            {
                foreach ($outputInfo['NIC'] as $value) {
                    $online = $value['Online'] == 'yes' ? true : false;
                    $output['NIC'][] = array('Name' => $value['Name'], 'Online'=>$online,'Speed'=>$value['Speed']);
                }
                if(isset($outputInfo['VGA'])){
                    $output['VGA']['Total'] = count($outputInfo['VGA']);
                    $output['VGA']['Used'] = 0;
                    foreach ($outputInfo['VGA'] as $value) {
                        if(strlen($value['vdName']) > 0)
                            $output['VGA']['Used']++;
                    }
                }
                
            }          
        }
    }

    function listNodeVLANUtilShell($input,&$output)
    {
        $cmd=$this->cmdClusterNodeVLANListUtil;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= $input['CommunicateIP'];
         exec($cmd,$outputArr,$rtn);
        // var_dump($rtn);
        // var_dump($outputArr);
        if($rtn == 0){              
            $output = json_decode($outputArr[0],true);
            return true;      
        }            
        return false;
    }

    function listNodeUtilShell($input,&$output)
    {

        $cmd = $this->cmdClusterNodeListUtil;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
        $cmd .= $input['CommunicateIP'];
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($rtn);
        // var_dump($outputArr);
        if($rtn == 0){              
            $output = json_decode($outputArr[0],true);
            return true;      
        }            
        return false;
    }

    function listNodesPoweronVD($input,&$output)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }   
        if(!$this->sqlListServers($outputServers,true))
            return CPAPIStatus::ListNodesPoweronVDFail;
        // var_dump($outputServers);
        if(!$this->listNodesPoweronVDShell($input,$outputInfo))
            return CPAPIStatus::ListNodesPoweronVDFail;
        // var_dump($outputInfos);
        $output = array();
        foreach ($outputInfo as $value) {
            $ip = '';
            $id = NULL;
            if(array_key_exists($value['nodeName'], $outputServers)){                
                $id = $outputServers[$value['nodeName']]['NodeID'];
            }   
            $poweronVDs = array();             
            foreach ($value['poweronVd'] as $vd) {
                $state = $this->changeVDState($vd['state'],$vd['volumeIntegrity']);
                $this->sqlListVDByidVD($vd['vd'],$outputPreferNode);
                $poweronVDs[] = array('VDID'=>$vd['vd'],'SrcVDID'=>$vd['srcVdName'],'State'=>$state,'VGACount'=>count($vd['vgaId']),'PreferNodeID'=>$outputPreferNode);
            }         
            if(isset($id))
                $output[]=array('NodeID'=>$id,'PoweronVD'=>$poweronVDs);            
        }
        return CPAPIStatus::ListNodesPoweronVDSuccess;
    }

    function listNodesPoweronVDShell($input,&$output)
    {
        $cmd = $this->cmdClusterListPoweronVD;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);        
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump(expression)ump($rtn);
        if($rtn == 0){              
            $output = json_decode($outputArr[0],true);
            return true;      
        }            
        return false;
    }

    function sqlListVDByidVD($idVD,&$output){
        $sqlList = <<<SQL
            SELECT idVDServer FROM tbVDImageBaseSet WHERE idVD=:idVD
SQL;
        try
        {                            
            $sth = connectDB::$dbh->prepare($sqlList);
            $sth->bindValue(':idVD', $idVD, PDO::PARAM_STR); 
            if($sth->execute())
        {                            
                while ($row = $sth->fetch() ) {
                    $preferNodeID = $row['idVDServer'] == null ? $row['idVDServer'] : (int)$row['idVDServer'];
                    $output=$preferNodeID;
                }
            }               
        }
        catch (Exception $e){                
        }
    }

    function sqlListMaxPairID(&$output)
    {        
        $output = null;
        $sqlSelect = <<<SQL
                select Max(idStoragePair) as pairMax FROM tbvdserverset
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($sqlSelect);                                  
            if($sth->execute())
            {                                        
                while( $row = $sth->fetch() ) 
                {
                    $output = $row['pairMax'];
                    return true;                                      
                }                
            }                           
        }
        catch (Exception $e){                             
        }                       
        return false;
    }

	function sqlListServers(&$output,$withKey=false)
	{
		$output = null;
        $sqlSelect = <<<SQL
                SELECT t1.*,t3.ip  as communicateIP 
                FROM tbvdserverset t1
                inner join tbbondset as t2
                ON t1.idVDServer=t2.idVDServer 
                inner join tbvswitchbondmappingset as t3
                on t2.idBond=t3.idBond
                WHERE t3.isCommunicationPort = true AND t1.stateVDServer<>99 ORDER BY t1.idStoragePair;
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($sqlSelect);                                  
            if($sth->execute())
            {                
                $output=array();
                $userid=null;                
                while( $row = $sth->fetch() ) 
                {                  
                    if($withKey)
                        $output[$row['communicateIP']] = array('NodeID'=>(int)$row['idVDServer'],'','NodeName'=>$row['nameNode'],'IP'=>$row['address'],'CommunicateIP'=>$row['communicateIP'],'Role'=>(int)$row['roleVDServer'],'PairID'=>(int)$row['idStoragePair'],'State'=>$row['stateVDServer']);
                    else
                        $output[] = array('NodeID'=>(int)$row['idVDServer'],'NodeName'=>$row['nameNode'],'IP'=>$row['address'],'CommunicateIP'=>$row['communicateIP'],'Role'=>(int)$row['roleVDServer'],'PairID'=>(int)$row['idStoragePair'],'State'=>$row['stateVDServer']);
                }
                return true;
            }                           
        }
        catch (Exception $e){                             
        }                       
        return false;
	}		
    
    function sqlListServersByPairID($idStoragePair,&$output,$withKey=false)
    {
        $output = null;
        $sqlSelect = <<<SQL
                SELECT t1.*,t3.ip  as communicateIP 
                FROM tbvdserverset t1
                inner join tbbondset as t2
                ON t1.idVDServer=t2.idVDServer 
                inner join tbvswitchbondmappingset as t3
                ON t2.idBond=t3.idBond 
                WHERE t3.isCommunicationPort = true AND t1.idStoragePair=:idStoragePair;
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($sqlSelect);   
            $sth->bindValue(':idStoragePair', $idStoragePair, PDO::PARAM_INT);                               
            if($sth->execute())
            {                
                $output=array();
                $userid=null;                
                while( $row = $sth->fetch() ) 
                {                  
                    if($withKey)
                        $output[$row['nameNode']] = array('NodeID'=>(int)$row['idVDServer'],'NodeName'=>$row['nameNode'],'IP'=>$row['address'],'CommunicateIP'=>$row['communicateIP'],'Role'=>(int)$row['roleVDServer'],'PairID'=>(int)$row['idStoragePair'],'State'=>$row['stateVDServer']);
                    else
                        $output[] = array('NodeID'=>(int)$row['idVDServer'],'NodeName'=>$row['nameNode'],'IP'=>$row['address'],'CommunicateIP'=>$row['communicateIP'],'Role'=>(int)$row['roleVDServer'],'PairID'=>(int)$row['idStoragePair'],'State'=>$row['stateVDServer']);
                }
                if(count($output) > 0)
                    return true;
            }                           
        }
        catch (Exception $e){                             
        }                       
        return false;
    }   

    function rebootNode($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }   
        if(!$this->sqlListServerByidVDServer($input['NodeID'], $node)){            
            return CPAPIStatus::RebootNodeFail;
        }        
        $input['CommunicateIP'] = $node['CommunicateIP'];
        $rtn = $this->rebootNodeShell($input);
        // $rtn = 78;
        if($rtn == 0){            
            LogAction::createLog(array('Type'=>6,'Level'=>1,'Code'=>'06100201','Riser'=>'admin','Message'=>'Reboot '.$node['NodeName'],$lastID));
            return CPAPIStatus::RebootNodeSuccess;
        }
        else{
            switch ($rtn) {
                case '78':
                    LogAction::createLog(array('Type'=>6,'Level'=>3,'Code'=>'06300204','Riser'=>'admin','Message'=>'Faied to reboot '.$node['NodeName']."(The Node has Unsync. object)($rtn)"),$lastID);
                    break;                
                default:
                    LogAction::createLog(array('Type'=>6,'Level'=>3,'Code'=>'06300205','Riser'=>'admin','Message'=>'Faied to reboot '.$node['NodeName']."($rtn)"),$lastID);
                    break;
            }
            return CPAPIStatus::RebootNodeFail;
        }
    }

    function rebootNodeShell($input,$isForce = false)
    {
        $cmd = $this->cmdClusterNodeReboot;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);   
        $cmd .= $input['CommunicateIP'].' ';
        if($isForce)
            $cmd .= 'force';
        $cmd .= '>/dev/null 2>&1 &';    
        // var_dump($cmd);      
        exec($cmd,$outputArr,$rtn);
        // var_dump($rtn);
        return $rtn;
    }

    function shutdownNode($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }   
        if(!$this->sqlListServerByidVDServer($input['NodeID'], $node)){            
            return CPAPIStatus::ShutdownNodeFail;
        }        
        $input['CommunicateIP'] = $node['CommunicateIP'];
        $rtn = $this->shutdownNodeShell($input);
        $rtn=0;
        if( $rtn == 0){
            // LogAction::createLog(array('Type'=>6,'Level'=>1,'Code'=>'06100202','Riser'=>'admin','Message'=>'Shutdown '.$node['NodeName'],$lastID));
            return CPAPIStatus::ShutdownNodeSuccess;
        }
        else{
            switch ($rtn) {
                case '76':
                    LogAction::createLog(array('Type'=>6,'Level'=>3,'Code'=>'06300206','Riser'=>'admin','Message'=>'Faied to shutdown '.$node['NodeName']."(The Node is not maintainable)($rtn)",$lastID));
                    return CPAPIStatus::ShutdownNodeFail;
                    break;
                case '77':
                    LogAction::createLog(array('Type'=>6,'Level'=>3,'Code'=>'06300206','Riser'=>'admin','Message'=>'Faied to shutdown '.$node['NodeName']."(The Node is updating)($rtn)",$lastID));
                    return CPAPIStatus::ShutdownNodeFail;
                    break;
                case '78':
                    LogAction::createLog(array('Type'=>6,'Level'=>3,'Code'=>'06300206','Riser'=>'admin','Message'=>'Faied to shutdown '.$node['NodeName']."(The Node has Unsync. object)($rtn)",$lastID));
                    return CPAPIStatus::ShutdownNodeFail;
                    break;
                // default:
                //     LogAction::createLog(array('Type'=>6,'Level'=>3,'Code'=>'06300207','Riser'=>'admin','Message'=>'Faied to shutdown '.$node['NodeName']."($rtn)",$lastID));
                //     break;
            }
            return CPAPIStatus::ShutdownNodeSuccess;
        }
    }

    function shutdownNodeShell($input)
    {
        $cmd = $this->cmdClusterNodeShutdown;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);   
        $cmd .= $input['CommunicateIP'];     
        exec($cmd,$outputArr,$rtn);    
        return $rtn;
    }

    function sqlListServerByidVDServer($idVDServer,&$output)
    {
        $output = null;
        $sqlSelect = <<<SQL
                -- SELECT t1.*,t2.ip as communicateIP FROM tbvdserverset as t1 
                -- inner join tbvswitchset as t2
                -- ON t1.idVDServer=t2.idVDServer 
                -- WHERE t1.idVDServer = :idVDServer 
                -- AND t2.isEnable = false;
                SELECT t1.*,t3.ip as communicateIP FROM tbvdserverset as t1 inner join tbbondset as t2 ON t1.idVDServer=t2.idVDServer inner join tbvswitchbondmappingset as t3 on t2.idBond=t3.idBond WHERE t1.idVDServer = :idVDServer AND t3.isCommunicationPort = true;
SQL;
        try
        {                                             
            $sth = connectDB::$dbh->prepare($sqlSelect);     
            $sth->bindValue(':idVDServer', $idVDServer, PDO::PARAM_INT);
            if($sth->execute())
            {                                    
                while( $row = $sth->fetch() ) 
                {                  
                    $output = array('NodeID'=>$row['idVDServer'],'NodeName'=>$row['nameNode'],'IP'=>$row['address'],'CommunicateIP'=>$row['communicateIP'],'PairID'=>$row['idStoragePair']);
                    // var_dump($out)
                    return true;
                }                
            }                           
        }
        catch (Exception $e){                             
        }                       
        return false;
    }

    function sqlUpdateServerState($input)
    {
        $SQLUpdateInfo = <<<SQL
            Update tbvdserverset SET stateVDServer=:stateVDServer WHERE idVDServer = :idVDServer
SQL;
        try
        {  
            // var_dump($id);                                                                           
            // var_dump($input);           
            $sth = connectDB::$dbh->prepare($SQLUpdateInfo);
            $sth->bindValue(':idVDServer', $input['NodeID'], PDO::PARAM_INT); 
            $sth->bindValue(':stateVDServer', $input['State'], PDO::PARAM_INT);             
            if($sth->execute())
                $result = true;            
        }
        catch (Exception $e){    
            $result = false;
        }     
        return $result;
    }

    function checkNode($input,&$output)
    {
        // var_dump($input);
        $vswitchC = new vSwitchAction();
        $systemC = new SystemProcess();
        $glusterC = new GlusterAction();
        $responsecode = $vswitchC->listVswitchNew($input,$outputVswitch);
        // var_dump($outputVswitch);
        if($responsecode != CPAPIStatus::ListVSwitchSuccess)
            return CPAPIStatus::CheckNodeFail;                            
        $outputHostname = $systemC->listHostname($input,$rtn);
        // var_dump($rtn);
        if($rtn != 0)
            return CPAPIStatus::CheckNodeFail;
        $output = $outputVswitch;
        $raidC = new ZfsAction();
        $output['RAIDisReady'] = false;
        $output['VAS'] = false;
        $output['Seed'] = false;
        $output['Dedupe'] = false;
        $output['HostName'] = $outputHostname['HostName'];
        // var_dump($output);
        $this->getUIConfig($outputUIConfig);
        if($outputUIConfig['APPluse'] == 1)
            $output['VAS'] = true;
        if($outputUIConfig['Seed'] == 1)
            $output['Seed'] = true;
        if($outputUIConfig['Dedupe'] == 1)
            $output['Dedupe'] = true;
        $input['CommunicateIP'] = $input['ConnectIP'];
        $raidC->listZfsForCreate($input,$outputZfs);
        // var_dump($outputZfs);
        if(isset($outputZfs) && count($outputZfs) > 0 && $outputZfs[0]['Status'] == 'Ready'){
            $output['RAIDisReady'] = true;            
        }
        $output['HaveGluster'] = true;
        $rtn = $glusterC->checkGlusterShell($input,$outputResult);
        // var_dump($rtn);
        if( $rtn !== false)
            $output['HaveGluster'] = $rtn == 1 ? true : false;;    
        $output['CanRestore'] = $outputResult == 1 ? true : false;
        $output['Restoring'] = $outputResult == 3 ? true : false;
        $output['Booting'] = $outputResult == 4 ? true : false;
        $output['WaitGroup'] = $outputResult == 5 ? true : false;
        $output['WaitSplitBrainGroup'] = $outputResult == 6 ? true : false;
        $output['ConnectIP'] = $input['ConnectIP'];
        $output['CheckResult'] = $outputResult;
        return CPAPIStatus::CheckNodeSuccess;
    }

    function setNodeMaintenance($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }   
        if(!$this->sqlListServerByidVDServer($input['NodeID'], $node)){            
            return CPAPIStatus::SetNodeMaintainanceFail;
        }       
        $input['CommunicateIP'] = $node['CommunicateIP'];
        $rtn = $this->setNodeMaintenanceCheckShell($input);
        if( $rtn != 0 ){
            if($rtn == 2)
                return CPAPIStatus::SetNodeMaintainanReentry;
            else
                return CPAPIStatus::SetNodeMaintainanceFail;
        }
        $this->setNodeMaintenanceShell($input);       
        return CPAPIStatus::SetNodeMaintainanceSuccess;
    }

    function setNodeMaintenanceCheckShell($input)
    {
        $rtn = -99;
        $cmd = $this->cmdClusterNodeSetMaintainCheck;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);   
        $cmd .= $input['CommunicateIP'].' ';
        $cmd .= $input['Maintenance'] == true ? 'yes' : 'no';
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn); 
        // var_dump($rtn);
        return $rtn;
    }

    function setNodeMaintenanceShell($input)
    {
        $cmd = $this->cmdClusterNodeSetMaintain;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);   
        $cmd .= $input['CommunicateIP'].' ';
        $cmd .= $input['Maintenance'] == true ? 'yes' : 'no';
        if($input['Maintenance']){
            $cmd .= '>/dev/null 2>&1 &';
            $pipe = popen($cmd,"r");    
            pclose($pipe); 
            sleep(2);
            $rtn = 0;        
        }
        else{
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);
            sleep(2);
        }
        // var_dump($cmd);        
        return $rtn;
    }

    function setNodeSSH($input)
    {
        $rtn = -99;
        if($input['EnableSSH'])
            $cmd = $this->cmdNodeFirewallEnableSSH;
        else
            $cmd = $this->cmdNodeFirewallDisableSSH;
        $cmd = str_replace ( 'ip' , '127.0.0.1', $cmd);
        // var_dump($cmd);           
        exec($cmd,$outputArr,$rtn);    
        // var_dump($rtn);
        if($rtn == 0)
            return CPAPIStatus::SetNodeSSHSuccess;
        else
            return CPAPIStatus::SetNodeSSHFail;
    }

    function listNodeSSH($input,&$output)
    {
        $rtn = -99;        
        $cmd = $this->cmdNodeFirewallSSHStatus;                    
        $cmd = str_replace ( 'ip' , '127.0.0.1', $cmd);    
        // var_dump($cmd);           
        exec($cmd,$outputArr,$rtn);    
        // var_dump($outputArr);
        // var_dump($rtn);
        if($rtn == 0){
            $isSSHEnable = $outputArr[0] == 'enable' ? true : false;
            $output = array('EnableSSH'=>$isSSHEnable);
            return CPAPIStatus::ListNodeSSHSuccess;
        }
        else
            return CPAPIStatus::ListNodeSSHFail;
    }

	function processNodeErrorCode($code){  
        $this->baseOutputResponse($code);        
        switch ($code){           
            case CPAPIStatus::ListNodesForLoginCheckFail:    
            case CPAPIStatus::ListNodesFail:     
            case CPAPIStatus::CheckNodeFail:     	
            case CPAPIStatus::SetNodeMaintainanceFail:
            case CPAPIStatus::ListNodesPoweronVDFail:
            // case CPAPIStatus::DiscardNodeFail:
            case CPAPIStatus::SetNodeVMSFail:
            case CPAPIStatus::ChangeNodeRoleFail:
                http_response_code(400);
                break;
            case CPAPIStatus::SetNodeMaintainanReentry:
                http_response_code(406);
                break;
        }
    }

}