<?php
class vSwitchAction extends BaseAPI
{  
    private $cmdNICInfo = CmdRoot."ip nic_info ";
    private $cmdNICBrReset = CmdRoot."ip nic_br_reset ";
    private $cmdNICBrSet = CmdRoot."ip nic_br_set ";
    private $cmdNICClearAll = CmdRoot."ip nic_clear_all";
    private $cmdNICDNSSet = CmdRoot."ip nic_dns_set ";
    private $cmdClusterNICClearAll = CmdRoot."ip cluster_nic_clear_all ";
    private $cmdClusterNICBrSet = CmdRoot."ip cluster_nic_br_set ";
    private $cmdNICVLANInfo = CmdRoot."ip nic_vlan_info";
    private $cmdNICVLANBeginConfig = CmdRoot.'ip nic_vlan_begin_config';
    private $cmdNICVLANCreateLocal = CmdRoot.'ip nic_vlan_create_local ';
    private $cmdNICVLANEndConfig = CmdRoot.'ip nic_vlan_end_config';
    private $cmdNICVLANAbortConfig = CmdRoot.'ip nic_vlan_abort_config';

    function  __construct()
    {    
        set_time_limit(0);
    }
    
    function initInsertVswitch($input,$isUpdateVDServerAddress=false)
    {
        $this->connectDB();        
        if(connectDB::$dbh == null){
            return false;
        }
        $this->listVswitchShell($input,$outputvSwitch);   
        // var_dump($input);
        // var_dump($outputvSwitch);            
        if(isset($outputvSwitch['vdct'])){
            $nicCount = (int)$outputvSwitch['vdct'];
            // var_dump(1);
            for($i=0;$i<$nicCount;$i++)
            {
                if(!$this->sqlInsertVDServerNIC($input['NodeID'],$i)){               
                    goto failRollback;
                }                    
            }
            $noneIPVswitchCount = $nicCount;
            $index = 1;
            foreach ($outputvSwitch['vdnic'] as $value) {
                if($index == 1 && $isUpdateVDServerAddress){
                    // var_dump($input);
                    // var_dump($value);
                    $this->sqlUpdateVDServerAddress($input['NodeID'],$value['ipv4']);
                }
                $ismodify = true;
                if($value['ipv4'] == $input['CommunicateIP'])
                    $ismodify = false;
                if(!$this->sqlInsertVswitch($input['NodeID'],$value['ipv4'],$value['mask'],$value['gateway'],$index,$ismodify,$value['mac'])){
                    goto failRollback;
                }
                foreach ($value['nic'] as $nic) {
                    $nicIndex = (int)$nic;                    
                    if(!$this->sqlUpdateidVSwitch($input['NodeID'],$index,$nicIndex)){
                        goto failRollback;
                    }
                }
                $index++;
                $noneIPVswitchCount--;
            }
            // var_dump(2);
            for ($i=0;$i<$noneIPVswitchCount;$i++) {                
                if(!$this->sqlInsertVswitch($input['NodeID'],'','','',$index,true,NULL)){
                    goto failRollback;
                }
                $index++;
            }
            // var_dump(3);
            // connectDB::$dbh->commit();
            return true;
        }    
        else{
            return false;
        }
failRollback:                
            // connectDB::$dbh->rollBack();
            return false;      
    }

    function initInsertVswitchNew($input,$isUpdateVDServerAddress=false,$vswitchNameWithKey=NULL)
    {
        $this->connectDB();        
        if(connectDB::$dbh == null){
            return false;
        }
        $this->listVlanShell($input,$outputvSwitch);   
        // var_dump($input);
        // var_dump($outputvSwitch);            
        if(isset($outputvSwitch['nic'])){
            $nicCount = count($outputvSwitch['nic']);
            // var_dump(1);
            $bondWithIDArr = array();
            $nicIndexWithBondIDArr = array();
            foreach ($outputvSwitch['bond'] as $value) {
                if (preg_match('/^bond[0-9]+$/', $value['dev'])) {  
                    if(!$this->sqlInsertVDServerBond(array('NodeID'=>$input['NodeID'],'BondName'=>$value['dev']))){               
                        goto failRollback;
                    }
                    $bondID = connectDB::$dbh->lastInsertId();
                    $bondWithIDArr[$value['dev']] = $bondID;
                    foreach ($value['nic'] as $nic) {
                        $nicIndexWithBondIDArr[(int)$nic] = $bondID;
                    }                    
                }                
            }
            $i=0;
            foreach($outputvSwitch['nic'] as $value)
            {
                $bondID = NULL;
                if(array_key_exists($i, $nicIndexWithBondIDArr))
                    $bondID = $nicIndexWithBondIDArr[$i];
                if(!$this->sqlInsertVDServerNICNew(array('NodeID'=>$input['NodeID'],'NICName'=>"eth$i",'mac'=>$value['mac'],'BondID'=>$bondID))){
                    goto failRollback;
                }               
                $i++;     
            }         
            $this->sqlListAllVswitch($outputVswitchs);
            if(is_null($outputVswitchs))
                goto failRollback;           
            foreach ($outputvSwitch['bridge'] as $value) {
                $tmpBondNameArr = explode('.', $value['bond']);
                $tmpBondName = $tmpBondNameArr[0];
                $tmpBridgeArr = explode('.', $value['dev']);
                $tmpBridgeIndex = (int)substr($tmpBridgeArr[0], 2);
                $isModify = true;
                $IsCommunicate = $value['ipv4'] == $input['CommunicateIP'] ? true : false;
                if($value['ipv4'] == $input['CommunicateIP'] || (count($tmpBridgeArr) == 1 && $tmpBridgeIndex == 0)){
                    $isModify = false;                
                }
                // var_dump($tmpBondName);
                // var_dump($bondWithIDArr);
                if(array_key_exists($tmpBondName, $bondWithIDArr)){
                    $bondID = $bondWithIDArr[$tmpBondName];
                    $nameBridgeIndex = $tmpBridgeIndex+1;
                    $nameKey="br$tmpBridgeIndex".$value['vlanId'];
                    if(isset($vswitchNameWithKey) && array_key_exists($nameKey, $vswitchNameWithKey)){
                        $tmpName = $vswitchNameWithKey[$nameKey];
                    }
                    else{
                        if($value['vlanId'] == 0){
                            $tmpName = "vSwitch$nameBridgeIndex";
                        }
                        else
                            $tmpName = "vSwitch$nameBridgeIndex"."_VLAN".$value['vlanId'];
                    }
                    // if($tmpBridgeIndex != 0)
                    //     $tmpName .= '_'.$tmpBridgeIndex;
                    $isInsertNewVswitch = true;
                    foreach ($outputVswitchs as $vswitch) {
                        if($tmpBridgeIndex == $vswitch['BridgeID'] && (int)$value['vlanId'] == $vswitch['VLANID']){
                            $tmpName = $vswitch['Name'];
                            $vswitchID = $vswitch['VswitchID'];
                            $isInsertNewVswitch = false;                            
                            break;
                        }                            
                    }
                    // var_dump($isInsertNewVswitch);
                    // var_dump($tmpBridgeIndex.' '.$value['vlanId'].' '.$tmpName);
                    if($isInsertNewVswitch){                        
                        if(!$this->sqlInsertVswitchNew(array('BridgeID'=>$tmpBridgeIndex,'VLANID'=>(int)$value['vlanId'],'Name'=>$tmpName)))
                            goto failRollback;
                        $vswitchID = connectDB::$dbh->lastInsertId();
                    }
                    if(!$this->sqlInserttbvswitchbondmappingset(array('BondID'=>$bondID,'VswitchID'=>$vswitchID,'VLANID'=>(int)$value['vlanId'],'CanModify'=>$isModify,'IsCommunicate'=>$IsCommunicate,'IP'=>$value['ipv4'],'Mask'=>$value['mask'],'Gateway'=>$value['gateway'])))
                        goto failRollback;
                }
            }

            return true;
        }    
        else{
            return false;
        }
failRollback:                
            // connectDB::$dbh->rollBack();
            return false;      
    }

    function listVswitchNew($input,&$outputAll)
    {               
        $output = null;        
        $outputFree = null;                      
        $this->listVlanShell($input, $outputIP);        
        if(is_null($outputIP)){
            return CPAPIStatus::ListVSwitchFail;
        }                       
        if(!isset($outputIP['nic'])){
            return CPAPIStatus::ListVSwitchFail;
        }            
        $nicCount = count($outputIP['nic']);        
        $outputFree = array();
        $removeFromFree = array();
        $output = array();
        for($i=0;$i<$nicCount;$i++){            
            $outputFree[] =  "eth$i";   
            $output[] = array('Devs'=>array(),'Vswitchs' => array());
        }   
        // var_dump($output);
        $outputBond = array();     
        foreach ($outputIP['bond'] as $value) {
            if(strpos($value['dev'], '.') === FALSE){
                $outputBond[$value['dev']] = array();
                foreach ($value['nic'] as $nic) {
                    $outputBond[$value['dev']][] = "eth$nic";
                    $indexFree = 0;                    
                    foreach ($outputFree as $free) {
                        if($free == "eth$nic")
                            unset($outputFree[$indexFree]);
                        $indexFree++;
                    }
                }                
            }
        }        
        $tmpVswitchID = 9999;
        foreach ($outputIP['bridge'] as $value){      
            $tmpBondNameArr = explode('.', $value['bond']);
            $tmpBondName = $tmpBondNameArr[0];
            $bondIndex = (int)substr($tmpBondName, 4);   
            $output[$bondIndex]['Devs'] = $outputBond[$tmpBondName];
            $output[$bondIndex]['Vswitchs'][] =array("VswitchID"=>$tmpVswitchID,"Name"=>"vSwitch$tmpVswitchID","IP"=>$value['ipv4'],"Mask"=>$value['mask'],"Gateway"=>$value['gateway'],"VLANID"=>(int)$value['vlanId']);
            $tmpVswitchID++;
        }         
        foreach ($removeFromFree as $value) {
            unset($outputFree[$value]);
        }
        $dnsIP = array();
        foreach ($outputIP['dns'] as $value) {
            $dnsIP[] = $value;
            break;
        }
        $outputFree = array_values($outputFree);
        // var_dump($output);
        // var_dump($outputFree);
        $outputAll = array("Bonds"=>$output,"FreeDevs"=>$outputFree,"DNS"=>$dnsIP);
        return CPAPIStatus::ListVSwitchSuccess;
    }

    function listVlanShell($input,&$output)
    {        
        // var_dump($input);
        if (!is_null($input['ConnectIP'])){    
            $cmd = $this->cmdNICVLANInfo ;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);     
            // var_dump($cmd);         
            exec($cmd,$outputArr,$rtn);
            // var_dump($outputArr);                
            if($rtn == 0){             
                $output = json_decode($outputArr[0],true);
                return;
            }  
        }              
    }

    function listVswitch($input,&$outputAll)
    {               
        $output = null;        
        $outputFree = null;              
        $this->listVswitchShell($input, $outputIP);
        // var_dump($outputIP);
        if(is_null($outputIP)){
            return CPAPIStatus::ListVSwitchFail;
        }                       
        if(!isset($outputIP['vdct'])){
            return CPAPIStatus::ListVSwitchFail;
        }            
        $nicCount = (int)$outputIP['vdct'];
        $outputFree = array();
        $removeFromFree = array();
        for($i=0;$i<$nicCount;$i++){
            $outputFree[] =  "eth$i";   
            $output[] = array('DHCP'=>false,'IP' => '', 'Mask'=>'','Gateway'=>'','Devs'=>array());
        }        
        $i=0;
        foreach ($outputIP['vdnic'] as $key=>$vdnic){      
            if(strpos($vdnic['dev'], '.') === FALSE){
                $dhcp = false;
                $devs = array();
                foreach ($vdnic['nic'] as $value) {
                    $index = (int)$value;
                    $devs[] = $outputFree[$index];
                    $removeFromFree[] = $index;                
                }            
                if($vdnic['protocol'] == 'dhcp')
                    $dhcp = true;
                $output[$i] = array('DHCP'=>$dhcp,'IP' => $vdnic['ipv4'], 'Mask'=>$vdnic['mask'],'Gateway'=>$vdnic['gateway'],'Devs'=>$devs);
                $i++;
            }
        }         
        foreach ($removeFromFree as $value) {
            unset($outputFree[$value]);
        }
        $dnsIP = array();
        foreach ($outputIP['dns'] as  $value) {
            $dnsIP[] = $value;
            break;
        }
        $outputFree = array_values($outputFree);
        // var_dump($output);
        // var_dump($outputFree);
        $outputAll = array("Vswitchs"=>$output,"FreeDevs"=>$outputFree,"DNS"=>$dnsIP);
        return CPAPIStatus::ListVSwitchSuccess;
    }        
        
    function listVswitchShell($input,&$output)
    {        
        // var_dump($input);
        if (!is_null($input['ConnectIP'])){    
            $cmd = $this->cmdNICInfo ;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);     
            // var_dump($cmd);         
            exec($cmd,$outputArr,$rtn);
            // var_dump($outputArr);                
            if($rtn == 0){             
                $output = json_decode($outputArr[0],true);
                return;
            }  
        }              
    }
    
    function listNodevSwitch($input,&$output)
    {
        $this->connectDB();        
        if(connectDB::$dbh == null){
            return CPAPIStatus::DBConnectFail;        
        }
        $this->sqlListvSwitchByidVDserver($input['NodeID'],$outputvSwitchs,$outputFree);            
        $output = array('Vswitchs'=>$outputvSwitchs,'FreeDevs'=>$outputFree);
        return CPAPIStatus::ListVSwitchSuccess;
    }    

    function sqlListvSwitchByidVDserver($idServer,&$output,&$output_free)
    {               
        $SQLSelect = <<<SQL
                SELECT t1.*,t2.name
                    FROM tbVSwitchSet as t1 
                    LEFT JOIN tbVDServerNicSet as t2                    
                    ON t1.idVSwitch=t2.idVSwitch AND t1.idVDServer=t2.idVDServer                    
                    WHERE t1.idVDServer=:idVDServer                    
SQL;
        $SQLSelectFree = <<<SQL
                SELECT name FROM tbVDServerNicSet                                                       
                WHERE idVDServer=:idVDServer AND idVSwitch is null
SQL;
        try        
        {                   
            $sth = connectDB::$dbh->prepare($SQLSelect);
            $sth->bindValue(':idVDServer', $idServer, PDO::PARAM_INT);                    
            if($sth->execute())
            {                  
                $i=0;        
                while( $row = $sth->fetch() ) 
                {              
//                        if($vswitchs[$row['idVSwitch']]["Alias"] == ''){
                    $vswitchs[$row['idVSwitch']]["ID"] = (int)$row['idVSwitch'];              
                    $vswitchs[$row['idVSwitch']]["IP"] =$row['ip'];
                    $vswitchs[$row['idVSwitch']]["Mask"] =$row['mask'];
                    $vswitchs[$row['idVSwitch']]["Gateway"] =$row['gateway'];
                    if($i==0)
                        $vswitchs[$row['idVSwitch']]["CanModify"] = false;
                    else
                        $vswitchs[$row['idVSwitch']]["CanModify"] =(bool)$row['isEnable'];
                    if(isset($row['name'])){
                        $vswitchs[$row['idVSwitch']]["Devs"][] =$row['name'];
                    }
                    else
                        $vswitchs[$row['idVSwitch']]["Devs"] = array();
                    $i++;
                }   
                if(count($vswitchs) > 0)
                    $output = array_values($vswitchs);
                else
                {
                    $output = array();
                }                    
                $sth = connectDB::$dbh->prepare($SQLSelectFree);
                $sth->bindValue(':idVDServer', $idServer, PDO::PARAM_INT);                    
                if($sth->execute())
                {                      
                    $output_free = array();
                    while( $row = $sth->fetch() ) 
                    {              
                        $output_free[]=$row['name'];
                    }                       
                }                                                                     
            }
        }
        catch (Exception $e){                
            $output=null;
        }                   
    }

    function listNodevSwitchNew($input,&$output)
    {
        $this->connectDB();        
        if(connectDB::$dbh == null){
            return CPAPIStatus::DBConnectFail;        
        }
        $this->sqlListvSwitchByidVDserverNew($input['NodeID'],$outputvBonds,$outputFree);            
        $output = array('Bonds'=>$outputvBonds,'FreeDevs'=>$outputFree);
        return CPAPIStatus::ListVSwitchSuccess;
    }

    function sqlListvSwitchByidVDserverNew($idServer,&$output,&$output_free,&$outputManager,&$outputCommunicate)
    {               
        $sqlSelect = <<<SQL
                select t1.name,t1.mac,t2.idBond,t2.nameBond,t3.ip,t3.mask,t3.gateway,t3.canModify,t3.isCommunicationPort,t4.nameAlias,t4.idVlan,t4.idBridge,t4.idVSwitch from tbvdservernicset as t1 inner join tbbondset as t2 on t1.idBond=t2.idBond inner join tbvswitchbondmappingset as t3 on t2.idBond=t3.idBond inner join tbvswitchset as t4 on t3.idVswitch=t4.idVswitch where t1.idVDServer=:idVDServer ORDER BY t4.idBridge;                   
SQL;
        $sqlSelectNicCount = <<<SQL
            SELECT count(name) as count FROM tbVDServerNICSet WHERE idVDServer=:idVDServer
SQL;
        $sqlSelectFree = <<<SQL
                SELECT name FROM tbVDServerNicSet                                                       
                WHERE idVDServer=:idVDServer AND idBond is null
SQL;
        try        
        {                   
            $sth = connectDB::$dbh->prepare($sqlSelect);
            $sth->bindValue(':idVDServer', $idServer, PDO::PARAM_INT);                    
            if($sth->execute())
            {                  
                $bondWithKey = array();
                while( $row = $sth->fetch() ) 
                {              
                    $key=$row['idBond'];
                    if($row['idBridge'] == 0 && $row['idVlan'] == 0){
                        $outputManager = array('BridgeID'=>0,'VLANID'=>0,'IP'=>$row['ip'],'Mask'=>$row['mask'],'Gateway'=>$row['gateway']);
                    }
                    if($row['isCommunicationPort']){
                        $outputCommunicate = array('BridgeID'=>(int)$row['idBridge'],'VLANID'=>(int)$row['idVlan'],'IP'=>$row['ip'],'Mask'=>$row['mask'],'Gateway'=>$row['gateway']);
                    }
                    if(array_key_exists($key, $bondWithKey)){
                        $haveDev = false;
                        foreach ($bondWithKey[$key]['Devs'] as $value) {
                            if($row['name'] == $value){
                                $haveDev = true;
                                break;
                            }
                        }
                        if(!$haveDev)
                            $bondWithKey[$key]['Devs'][] = $row['name'];
                        $haveVswitch = false;
                        foreach ($bondWithKey[$key]['Vswitchs'] as $value) {
                            if($row['nameAlias'] == $value['Name']){
                                $haveVswitch = true;
                                break;
                            }
                        }
                        if(!$haveVswitch){
                            $bondWithKey[$key]['Vswitchs'][] = array('VswitchID'=>(int)$row['idVSwitch'],'Name'=>$row['nameAlias'],'IP'=>$row['ip'],'Mask'=>$row['mask'],'Gateway'=>$row['gateway'],'VLANID'=>(int)$row['idVlan'],'CanModify'=>(bool)$row['canModify'],'IsCommunicate'=>(bool)$row['isCommunicationPort']); 
                        }
                    }else{
                        $bondWithKey[$key] = array();
                        $bondWithKey[$key]['BondID'] = (int)$row['idBond'];
                        $bondWithKey[$key]['BridgeID'] = (int)$row['idBridge'];
                        $bondWithKey[$key]['Devs'] = array();
                        $bondWithKey[$key]['Devs'][] = $row['name'];
                        $bondWithKey[$key]['Vswitchs'][] = array('VswitchID'=>(int)$row['idVSwitch'],'Name'=>$row['nameAlias'],'IP'=>$row['ip'],'Mask'=>$row['mask'],'Gateway'=>$row['gateway'],'VLANID'=>(int)$row['idVlan'],'CanModify'=>(bool)$row['canModify'],'IsCommunicate'=>(bool)$row['isCommunicationPort']); 
                    }
                }   
                if(count($bondWithKey) > 0)
                    $output = array_values($bondWithKey);
                else
                {
                    $output = array();
                }                           
                $sth = connectDB::$dbh->prepare($sqlSelectFree);
                $sth->bindValue(':idVDServer', $idServer, PDO::PARAM_INT);                    
                if($sth->execute())
                {                      
                    $output_free = array();
                    while( $row = $sth->fetch() ) 
                    {              
                        $output_free[]=$row['name'];
                    }                       
                }           
                $sth = connectDB::$dbh->prepare($sqlSelectNicCount);
                $sth->bindValue(':idVDServer', $idServer, PDO::PARAM_INT);                    
                if($sth->execute())
                {
                    while( $row = $sth->fetch() ) 
                    {
                        $count = $row['count'];
                    }
                }         
                if(isset($count)){
                    if(count($output) < $count){
                        $nonVswithBondCount = $count - count($output);
                        for ($i=0; $i < $nonVswithBondCount; $i++) { 
                            $output[]= array('Devs'=>array(),'Vswitchs'=>array());
                        }
                    }
                }                                                          
            }
        }
        catch (Exception $e){                
            $output=null;
        }                   
    }

    function sqlListVswitchDistinctCountNameAlias(&$output)
    {
        $output=NULL;
        $sqlSelect = <<<SQL
            select count(nameAlias)as count,count(distinct nameAlias) as diffCount from tbvswitchset;
SQL;
        try        
        {                   
            $sth = connectDB::$dbh->prepare($sqlSelect);                
            if($sth->execute())
            {                       
                while( $row = $sth->fetch() ){            
                    $output = array('Count'=>(int)$row['count'],'DiffCount'=>(int)$row['diffCount']);
                }
            }
        }
        catch (Exception $e){                
            $output=NULL;
        }        
        
    }

    function sqlListAllVswitch(&$output)
    {
        $output=NULL;
        $sqlSelect = <<<SQL
            select * from tbVSwitchSet;
SQL;
        try        
        {                   
            $sth = connectDB::$dbh->prepare($sqlSelect);                
            if($sth->execute())
            {       
                $output = array();                           
                while( $row = $sth->fetch() ){            
                    $output[] = array('Name'=>$row['nameAlias'],'VswitchID'=>$row['idVSwitch'],'VLANID'=>$row['idVlan'],'BridgeID'=>$row['idBridge']);
                }
            }
        }
        catch (Exception $e){                
            $output=NULL;
        }        
    }
    
    function modifyVSwitchName($input)
    {
        $this->connectDB();        
        if(connectDB::$dbh == null){
            return CPAPIStatus::DBConnectFail;        
        }
        $this->sqlListAllVswitch($outputVswitchs);
        foreach ($outputVswitchs as $value) {
            if($value['Name'] == $input['Name'] && $input['VswitchID'] != $value['VswitchID'])
                return CPAPIStatus::Conflict;
        }
        if(!$this->sqlUpdatevSwitchNew($input))
            return CPAPIStatus::ModifyVSwitchFail;
        return CPAPIStatus::ModifyVSwitchSuccess;
    }

    function sqlInsertVDServerNIC($idServer,$index)
    {
        $result = false;
        $SQLInsert = "INSERT tbVDServerNicSet (idVDServer,name) Values (:idVDServer,:name)";
        try
        {              
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':idVDServer', $idServer, PDO::PARAM_INT);            
            $sth->bindValue(':name', "eth$index", PDO::PARAM_STR);            
            $result = $sth->execute();               
        }
        catch (Exception $e){                
        }        
        return $result;
    }

    function sqlInsertVDServerNICNew($input)
    {
        $result = false;
        $SQLInsert = "INSERT tbVDServerNicSet (idVDServer,name,mac,idBond) Values (:idVDServer,:name,:mac,:idBond)";
        try
        {              
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':idVDServer', $input['NodeID'], PDO::PARAM_INT);            
            $sth->bindValue(':name', $input['NICName'], PDO::PARAM_STR);
            $sth->bindValue(':mac', $input['mac'], PDO::PARAM_STR);
            $sth->bindValue(':idBond', $input['BondID'], PDO::PARAM_INT);
            $result = $sth->execute();               
        }
        catch (Exception $e){                
        }        
        return $result;
    }

    function sqlInsertVDServerBond($input)
    {
        // var_dump($input);
        $result = false;
        $SQLInsert = "INSERT tbbondset (idVDServer,nameBond) Values (:idVDServer,:nameBond)";
        try
        {              
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':idVDServer', $input['NodeID'], PDO::PARAM_INT);            
            $sth->bindValue(':nameBond', $input['BondName'], PDO::PARAM_STR);            
            $result = $sth->execute();               
        }
        catch (Exception $e){                
        }        
        return $result;
    }

    function sqlDeleteVDServerBond($input)
    {
        // var_dump($input);
        $result = false;
        $sqlDelete = "DELETE FROM tbbondset WHERE idBond=:idBond AND idVDServer=:idVDServer";
        try
        {              
            $sth = connectDB::$dbh->prepare($sqlDelete);            
            $sth->bindValue(':idBond', $input['BondID'], PDO::PARAM_INT);        
            $sth->bindValue(':idVDServer', $input['NodeID'], PDO::PARAM_INT);
            $result = $sth->execute();               
        }
        catch (Exception $e){                
        }        
        return $result;
    }

    function sqlClearNonMappingtbvswitchset()
    {
        $result = false;
        $sqlClear = 'delete from tbvswitchset where idVSwitch IN (select idVswitch from(select t1.idVSwitch as idVSwitch from tbvswitchset as t1 left join tbvswitchbondmappingset as t2 on t1.idVSwitch=t2.idVSwitch where t2.idVSwitch IS NULL) dummyname)';
        try
        {              
            $sth = connectDB::$dbh->prepare($sqlClear);             
            $result = $sth->execute();               
        }
        catch (Exception $e){                
        }        
        return $result;   
    }

    function sqlInsertVswitchNew($input){
        $result = false;
        $SQLInsert = "INSERT tbVSwitchSet (idVlan,idBridge,nameAlias,description) Values (:idVlan,:idBridge,:nameAlias,'')";
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':idVlan', $input['VLANID'], PDO::PARAM_INT);            
            $sth->bindValue(':idBridge', $input['BridgeID'], PDO::PARAM_INT);            
            $sth->bindValue(':nameAlias', $input['Name'], PDO::PARAM_STR);            
            $result = $sth->execute();               
        }
        catch (Exception $e){                
        }        
        return $result;
    }

    function sqlListvSwitchIDByidVlanidBridge($input,&$outputID){
        $result = false;
        $outputID=NULL;
        $sqlList = "SELECT idVSwitch FROM tbVSwitchSet WHERE idVlan=:idVlan AND idBridge=:idBridge";
        try
        {                            
            // var_dump($input);
            $sth = connectDB::$dbh->prepare($sqlList);                              
            $sth->bindValue(':idVlan', $input['VLANID'], PDO::PARAM_INT);
            $sth->bindValue(':idBridge', $input['BridgeID'], PDO::PARAM_INT);
            if($sth->execute())
            {            
                $result = true;                   
                while( $row = $sth->fetch() ){
                    $outputID = $row['idVSwitch'];                     
                }
            }                      
        }
        catch (Exception $e){                
        }        
        // var_dump($result);
        return $result;
    }

    function sqlCheckDuplicatevSwitchName($name,$notEqulID=NULL){
        $result = false;
        $sqlUpdate = "SELECT idVSwitch FROM tbVSwitchSet WHERE nameAlias=:nameAlias";
        if(isset($notEqulID)){
            $sqlUpdate .= " AND idVSwitch <> :idVSwitch";
        }
        try
        {                            
            // var_dump($input);
            $sth = connectDB::$dbh->prepare($sqlUpdate);                              
            $sth->bindValue(':nameAlias', $name, PDO::PARAM_STR);
            if(isset($notEqulID)){
                $sth->bindValue(':idVSwitch', $notEqulID, PDO::PARAM_INT);
            }
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

    function sqlUpdatevSwitchNew($input){
        $result = false;
        $sqlUpdate = "UPDATE tbVSwitchSet SET nameAlias=:nameAlias,description=:description WHERE idVSwitch=:idVSwitch";
        try
        {                            
            $sth = connectDB::$dbh->prepare($sqlUpdate);                            
            $sth->bindValue(':idVSwitch', $input['VswitchID'], PDO::PARAM_INT);                        
            $sth->bindValue(':nameAlias', $input['Name'], PDO::PARAM_STR);
            $sth->bindValue(':description', $input['Desc'], PDO::PARAM_STR);
            $result = $sth->execute();               
        }
        catch (Exception $e){                
        }        
        return $result;
    }

    function sqlDeletetbvswitchbondmappingset($input)
    {
        $result = false;
        $sqlUpdate = "DELETE FROM tbvswitchbondmappingset WHERE idBond=:idBond AND idVSwitch=:idVSwitch";
        try
        {                            
            // var_dump($input);
            $sth = connectDB::$dbh->prepare($sqlUpdate);                              
            $sth->bindValue(':idBond', $input['BondID'], PDO::PARAM_INT);
            $sth->bindValue(':idVSwitch', $input['VswitchID'], PDO::PARAM_INT);
            if($sth->execute())
            {               
                // if($sth->rowCount() == 1)
                $result = true;
            }                      
        }
        catch (Exception $e){                
        }        
        return $result;
    }

    function sqlUpdatetbvswitchbondmappingset($input){
        $result = false;
        $sqlUpdate = "Update tbvswitchbondmappingset SET ip=:ip,mask=:mask,gateway=:gateway WHERE idBond=:idBond AND idVSwitch=:idVSwitch";
        try
        {                            
            // var_dump($input);
            $sth = connectDB::$dbh->prepare($sqlUpdate);                  
            $sth->bindValue(':ip', $input['IP'], PDO::PARAM_STR);
            $sth->bindValue(':mask', $input['Mask'], PDO::PARAM_STR);
            $sth->bindValue(':gateway', $input['Gateway'], PDO::PARAM_STR);
            $sth->bindValue(':idBond', $input['BondID'], PDO::PARAM_INT);
            $sth->bindValue(':idVSwitch', $input['VswitchID'], PDO::PARAM_INT);
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

    function sqlCleartbvdservernicsetMappingBond($input){
        $result = false;
        $sqlClear = "Update tbvdservernicset set idBond=NULL WHERE idVDServer=:idVDServer";
        try
        {                            
            // var_dump($input);
            $sth = connectDB::$dbh->prepare($sqlClear);             
            $sth->bindValue(':idVDServer', $input['NodeID'], PDO::PARAM_INT);
            if($sth->execute())
            {               
                // if($sth->rowCount() == 1)
                $result = true;
            }                      
        }
        catch (Exception $e){                
        }        
        return $result;
    }

    function sqlUpdatetbvdservernicsetMappingBond($input){
        $result = false;
        $sqlUpdate = "Update tbvdservernicset set idBond=:idBond WHERE name=:name AND idVDServer=:idVDServer";
        try
        {                            
            // var_dump($input);
            $sth = connectDB::$dbh->prepare($sqlUpdate);                              
            $sth->bindValue(':idBond', $input['BondID'], PDO::PARAM_INT);
            $sth->bindValue(':name', $input['Dev'], PDO::PARAM_STR);
            $sth->bindValue(':idVDServer', $input['NodeID'], PDO::PARAM_INT);
            if($sth->execute())
            {               
                // if($sth->rowCount() == 1)
                $result = true;
            }                      
        }
        catch (Exception $e){                
        }        
        return $result;
    }

    function sqlInserttbvswitchbondmappingset($input){
        $result = false;
        $sqlInsert = "INSERT tbvswitchbondmappingset (idBond,idVSwitch,canModify,isCommunicationPort,ip,mask,gateway) Values (:idBond,:idVSwitch,:canModify,:isCommunicationPort,:ip,:mask,:gateway)";
        try
        {                            
            // var_dump($input);
            $sth = connectDB::$dbh->prepare($sqlInsert);      
            $sth->bindValue(':idBond', $input['BondID'], PDO::PARAM_INT);                         
            $sth->bindValue(':idVSwitch', $input['VswitchID'], PDO::PARAM_INT);            
            $sth->bindValue(':canModify', $input['CanModify'], PDO::PARAM_BOOL);
            $sth->bindValue(':isCommunicationPort', $input['IsCommunicate'], PDO::PARAM_BOOL);
            $sth->bindValue(':ip', $input['IP'], PDO::PARAM_STR);
            $sth->bindValue(':mask', $input['Mask'], PDO::PARAM_STR);
            $sth->bindValue(':gateway', $input['Gateway'], PDO::PARAM_STR);
            $result = $sth->execute();               
        }
        catch (Exception $e){                
        }        
        return $result;
    }
    
    function sqlInsertVswitch($idServer,$ip,$mask,$gateway,$index,$enable,$mac){
        $result = false;
        $SQLInsert = "INSERT tbVSwitchSet (idVDServer,idVSwitch,isEnable,nameAlias,ip,mask,gateway,mac) "
                . "Values (:idVDServer,:idVSwitch,:isEnable,'',:ip,:mask,:gateway,:mac)";
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':idVDServer', $idServer, PDO::PARAM_INT);            
            $sth->bindValue(':idVSwitch', $index, PDO::PARAM_INT);            
            $sth->bindValue(':isEnable', $enable, PDO::PARAM_BOOL);
            // $sth->bindValue(':nameAlias', $alias, PDO::PARAM_BOOL);
            $sth->bindValue(':ip', $ip, PDO::PARAM_STR);
            $sth->bindValue(':mask', $mask, PDO::PARAM_STR);
            $sth->bindValue(':gateway', $gateway, PDO::PARAM_STR);
            $sth->bindValue(':mac', $mac, PDO::PARAM_STR);
            $result = $sth->execute();               
        }
        catch (Exception $e){                
        }        
        return $result;
    }
    
    function sqlUpdatevSwitch($idServer,$idVSwitch,$alias,$ip,$mask,$gateway,$enable){
        $result = false;
        $SQLUpdate = "UPDATE tbVSwitchSet SET isEnable=:isEnable,nameAlias=:nameAlias,ip=:ip"
                . ",mask=:mask,gateway=:gateway WHERE idVDServer=:idVDServer AND idVSwitch=:idVSwitch";                
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLUpdate);            
            $sth->bindValue(':idVDServer', $idServer, PDO::PARAM_INT);            
            $sth->bindValue(':idVSwitch', $idVSwitch, PDO::PARAM_INT);            
            $sth->bindValue(':isEnable', $enable, PDO::PARAM_BOOL);
            $sth->bindValue(':nameAlias', $alias, PDO::PARAM_BOOL);
            $sth->bindValue(':ip', $ip, PDO::PARAM_STR);
            $sth->bindValue(':mask', $mask, PDO::PARAM_STR);
            $sth->bindValue(':gateway', $gateway, PDO::PARAM_STR);
            $result = $sth->execute();               
        }
        catch (Exception $e){                
        }        
        return $result;
    }

    function sqlUpdateVDServerAddress($idServer,$ip){
        $result = false;
        $SQLUpdate = "UPDATE tbvdserverset SET address=:address WHERE idVDServer=:idVDServer";
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLUpdate);            
            $sth->bindValue(':idVDServer', $idServer, PDO::PARAM_INT);                     
            $sth->bindValue(':address', $ip, PDO::PARAM_STR);
            $result = $sth->execute();               
        }
        catch (Exception $e){                
        }        
        return $result;
    }
    
    function sqlUpdateidVSwitch($idServer,$idVSwitch,$ethindex){
        $result = false;
        $SQLUpdate = "UPDATE tbVDServerNicSet SET idVSwitch = :idVSwitch "
                . "WHERE idVDServer=:idVDServer AND name=:name";
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLUpdate);      
            $sth->bindValue(':idVSwitch', $idVSwitch, PDO::PARAM_INT); 
            $sth->bindValue(':idVDServer', $idServer, PDO::PARAM_INT);                             
            $sth->bindValue(':name', "eth$ethindex", PDO::PARAM_STR);            
            $result = $sth->execute();               
        }
        catch (Exception $e){                
        }        
        return $result;
    }
    
    function sql_list_count_idVSwitch_by_idGgs($idGfs,$idVSwitch,&$count){
        $SQLSelect = <<<SQL
                SELECT count(t3.idVSwitch) as count
                    FROM tbGfsLunSet as t1 
                    INNER JOIN tbVDServerSet as t2                    
                    ON t1.idCluster=t2.idCluster
                    INNER JOIN tbVSwitchSet as t3  
                    ON t2.idVDServer=t3.idVDServer
                    WHERE t1.idGfs=:idGfs AND t3.idVSwitch=:idVSwitch AND t3.isEnable=true
SQL;
        try
        {             
            $count = 0;
            $sth = connectDB::$dbh->prepare($SQLSelect);            
            $sth->bindValue(':idGfs', $idGfs, PDO::PARAM_INT);            
            $sth->bindValue(':idVSwitch', $idVSwitch, PDO::PARAM_INT);  
            if($sth->execute()){
                while( $row = $sth->fetch() ) 
                {                                  
                    $count = (int)$row['count'];
                }      
            }
        }
        catch (Exception $e){                
        }        
        return $result;
    }
    
    function sql_update_territroy_idVSwitch($idGfs,$orgidVSwitch,$newidVSwitch){
        $result = false;
        $SQLUpdate = "UPDATE tbTerritorySet SET idVSwitch = :newidVSwitch "
                . "WHERE idGfs=:idGfs AND idVSwitch=:orgidVSwitch";
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLUpdate);      
            $sth->bindValue(':newidVSwitch', $newidVSwitch, PDO::PARAM_INT); 
            $sth->bindValue(':orgidVSwitch', $orgidVSwitch, PDO::PARAM_INT);                             
            $sth->bindValue(':idGfs', $idGfs, PDO::PARAM_INT);            
            $result = $sth->execute();               
        }
        catch (Exception $e){                
        }        
        return $result;
    }
    
    function setVswitch($input)
    {
        // var_dump($input);       
        $index = 0;
        $setVswitchCmd = '';        
        $setDNSCmd = '';
        foreach ($input['Vswitchs'] as $vswitch){ 
            if(count($vswitch['Devs']) > 0){                       
                $setVswitchCmd .= $index.' ';     
                if($vswitch['DHCP'])
                    $setVswitchCmd .= 'dhcp ';
                else   
                    $setVswitchCmd .= 'static ';
                if($vswitch['IP'] == '' || $vswitch['Mask'] == ''){
                    $setVswitchCmd .= 'na ';
                    $setVswitchCmd .= 'na ';
                    $setVswitchCmd .= 'na ';
                }
                else{
                    $setVswitchCmd .= $vswitch['IP'].' ';
                    $setVswitchCmd .= $vswitch['Mask'].' ';
                    $setVswitchCmd .= $vswitch['Gateway']==''?'na ':$vswitch['Gateway'].' ';
                }
                $setVswitchCmd .= count($vswitch['Devs']).' ';
                foreach ($vswitch['Devs'] as $dev)
                {
                    $dev_index = substr($dev,3);
                    $setVswitchCmd .=$dev_index.' ';
                }
            }            
            $index++;
        }        
        if($this->clearVswitchShell($input) != 0){            
            return CPAPIStatus::SetVSwitchFail;
        }        
        if($this->setVswitchShell($input,$setVswitchCmd) != 0){           
            return CPAPIStatus::SetVSwitchFail;
        }
        foreach ($input['DNS'] as $value) {
            if(strlen($value) > 0)
                $setDNSCmd .= '"'.$value.'"';
        }
        $this->setDNSShell($input,$setDNSCmd);
        // $systemC = new SystemProcess();
        // $systemC->reboot($input);
        return CPAPIStatus::SetVSwitchSuccess;
    }
    
    function setDNS($input)
    {
        foreach ($input['DNS'] as $value) {
            if(strlen($value) > 0)
                $setDNSCmd .= '"'.$value.'"';
        }
        $this->setDNSShell($input,$setDNSCmd);
    }

    function setNodevSwitch($input)
    {        
        $set_cmd_arg = '';
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }   
        $node = null;
        $nodeC = new NodeAction();                  
        if(!$nodeC->sqlListServerByidVDServer($input['NodeID'], $node)){
            // echo 1;
            // $this->log_c->create_log(array('Type'=>7,'Level'=>3,'Code'=>'07300201','Riser'=>'admin'
            //         ,'Message'=>"Failed to set vswitch(Failed to list node)"));  
            return CPAPIStatus::SetVSwitchFail;
        }        
        $input['CommunicateIP']=$node['CommunicateIP'];
        $set_cmd_arg .= $input['CommunicateIP'].' ';
        connectDB::$dbh->beginTransaction();
        if(!$this->sqlClearNICidVSwitch($input['NodeID'])){
            // echo 2;
            connectDB::$dbh->rollBack();  
            // $this->log_c->create_log(array('Type'=>7,'Level'=>3,'Code'=>'07300202','Riser'=>'admin'
            //         ,'Message'=>"Failed to set vswitch(node:".$node['Name'].")(Failed to clear nic db)"));  
            return CPAPIStatus::SetVSwitchFail;
        }             
        $index = 1;
        foreach ($input['Vswitchs'] as $vswitch){        
            if(count($vswitch['Devs']) > 0){       
                $enable = true;
                if($input['CommunicateIP'] == $vswitch['IP'])
                    $enable = false;
                if(!$this->sqlUpdatevSwitch($input['NodeID'], $index, '', $vswitch['IP'], $vswitch['Mask'], $vswitch['Gateway'],$enable)){
                    // echo 5;
                    connectDB::$dbh->rollBack();  
                    // $this->log_c->create_log(array('Type'=>7,'Level'=>3,'Code'=>'07300204','Riser'=>'admin'
                    // ,'Message'=>"Failed to set vswitch(node:".$node['Name'].")(Failed to insert vswitch db)"));  
                    return CPAPIStatus::SetVSwitchFail;
                }
                $brIndex = $index-1;
                $set_cmd_arg .= $brIndex.' static ';                
                if($vswitch['IP'] == '' || $vswitch['Mask'] == ''){
                    $set_cmd_arg .= 'na ';
                    $set_cmd_arg .= 'na ';
                    $set_cmd_arg .= 'na ';
                }
                else{
                    $set_cmd_arg .= $vswitch['IP'].' ';
                    $set_cmd_arg .= $vswitch['Mask'].' ';
                    $set_cmd_arg .= $vswitch['Gateway']==''?'na ':$vswitch['Gateway'].' ';
                }
                $set_cmd_arg .= count($vswitch['Devs']).' ';
                foreach ($vswitch['Devs'] as $dev)
                {
                    $dev_index = substr($dev,3);
                    if(!$this->sqlUpdateidVSwitch($input['NodeID'], $index,$dev_index)){
                    echo 3;
                        connectDB::$dbh->rollBack();  
                    //     $this->log_c->create_log(array('Type'=>7,'Level'=>3,'Code'=>'07300205','Riser'=>'admin'
                    // ,'Message'=>"Failed to set vswitch(node:".$node['Name'].")(Failed to update nic db)"));  
                        return CPAPIStatus::SetVSwitchFail;
                    }   
                    $set_cmd_arg .=$dev_index.' ';
                }
            }
            else{
                if(!$this->sqlUpdatevSwitch($input['NodeID'], $index, '', $vswitch['IP'], $vswitch['Mask'], $vswitch['Gateway'],true)){
                    // echo 4;
                    connectDB::$dbh->rollBack();  
                    // $this->log_c->create_log(array('Type'=>7,'Level'=>3,'Code'=>'07300204','Riser'=>'admin'
                    // ,'Message'=>"Failed to set vswitch(node:".$node['Name'].")(Failed to update vswitch db)"));  
                    return CPAPIStatus::SetVSwitchFail;
                }
            }
            $index++;
        }        
        if($this->clearNodeVswitchShell($input) != 0){
            // echo 6;
            connectDB::$dbh->rollBack();  
            // echo 1;
            // $this->log_c->create_log(array('Type'=>7,'Level'=>3,'Code'=>'07300206','Riser'=>'admin'
            //         ,'Message'=>"Failed to set vswitch(node:".$node['Name'].")(Failed to clear vswitch)"));  
            return CPAPIStatus::SetVSwitchFail;
        }
        if($this->setNodeVswitchShell($input,$set_cmd_arg) != 0){
            connectDB::$dbh->rollBack();  
            // echo 2;
            // $this->log_c->create_log(array('Type'=>7,'Level'=>3,'Code'=>'07300207','Riser'=>'admin'
            //         ,'Message'=>"Failed to set vswitch(node:".$node['Name'].")(Failed to set vswitch)"));  
            return CPAPIStatus::SetVSwitchFail;
        }
        // if($nodeC->rebootNodeShell($input) != 0){
        //     connectDB::$dbh->rollBack();  
        //     // $this->log_c->create_log(array('Type'=>7,'Level'=>3,'Code'=>'07300208','Riser'=>'admin'
        //     //         ,'Message'=>"Failed to set vswitch(node:".$node['Name'].")(Failed to reboot node)"));  
        //     return CPAPIStatus::SetVSwitchFail;
        // }
        connectDB::$dbh->commit();
        // $this->log_c->create_log(array('Type'=>7,'Level'=>1,'Code'=>'07100101','Riser'=>'admin'
        //             ,'Message'=>"Set vswitch(node:".$node['Name'].") Success"));  
        return CPAPIStatus::SetVSwitchSuccess;
    }    

    function setNodevSwitchNew($input,&$output)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        $node = null;
        $nodeC = new NodeAction();                  
        if(!$nodeC->sqlListServerByidVDServer($input['NodeID'], $node)){            
            return CPAPIStatus::SetVSwitchFail;
        }
        $input['NodeName'] = $node['NodeName'];
        $this->sqlListvSwitchByidVDserverNew($input['NodeID'],$outputBonds,$outputFree,$outputManager,$outputCommunicate);
        if(is_null($outputBonds) || is_null($outputManager) || is_null($outputCommunicate))
            return CPAPIStatus::SetVSwitchFail;
        $indexBond=0;
        $haveManager = false;
        $haveCommunicate = false;
        $bondDevs = array();
        $gateways = array();
        connectDB::$dbh->beginTransaction();
        $responsecode = CPAPIStatus::SetVSwitchFail;
        $appendErr = '';
        $logCode = '';
        $setArr = array();
        foreach ($outputBonds as &$bond) {
            foreach ($bond['Vswitchs'] as &$bondVswitch) {
                $bondVswitch['haveSameVswitch'] = false;
            }
        }        
        // var_dump($input);
        foreach ($input['Bonds'] as $value) {      
            if(count($value['Devs']) > 0 && count($value['Vswitchs']) > 0){      
                if(isset($outputBonds[$indexBond]) && count($outputBonds[$indexBond]['Devs'])){                
                    $idBond = $outputBonds[$indexBond]['BondID'];                
                }
                else{                
                    if(!$this->sqlInsertVDServerBond(array('NodeID'=>$input['NodeID'],'BondName'=>"bond$indexBond"))){
                        $logCode = '0730020A';
                        $appendErr="(Failed to insert bond($indexBond) db)";
                        goto failRollback;
                    }
                    $idBond = connectDB::$dbh->lastInsertId();
                }            
                $vlanArr = array();
                foreach ($value['Devs'] as $dev) {
                    foreach ($bondDevs as $bondDev) {
                        if($dev == $bondDev['Dev']){
                            $logCode = '0730020B';
                            $appendErr="(Duplicate Bond Device($indexBond:".$bondDev['Dev']."))";
                            goto failRollback;
                        }
                    }
                    $bondDevs[] = array('Dev'=>$dev,'BondID'=>$idBond);
                }            
                foreach ($value['Vswitchs'] as $vswitch) {
                    if(strlen($vswitch['Gateway']) > 0){
                        if(array_key_exists($vswitch['Gateway'], $gateways)){
                            $logCode='0730021C';
                            $appendErr="(Duplicate Gateway(".$vswitch['Gateway'].")";
                            goto failRollback;
                        }
                        $gateways[$vswitch['Gateway']] = $vswitch['Gateway'];
                    }
                    // var_dump($outputManager);
                    // var_dump($outputCommunicate);                
                    if($indexBond == $outputManager['BridgeID'] && $vswitch['IP'] == $outputManager['IP'] && $vswitch['Mask'] == $outputManager['Mask'] && $vswitch['Gateway'] == $outputManager['Gateway'])
                        $haveManager = true;
                    if($indexBond == $outputCommunicate['BridgeID'] && $vswitch['IP'] == $outputCommunicate['IP'] && $vswitch['Mask'] == $outputCommunicate['Mask'] && $vswitch['Gateway'] == $outputCommunicate['Gateway'])
                        $haveCommunicate = true;
                    $haveSameVswitch = false;
                    foreach ($outputBonds as &$bond) {
                        // var_dump($indexBond);
                        // var_dump($bond);
                        if($indexBond == $bond['BridgeID']){
                            foreach ($bond['Vswitchs'] as &$bondVswitch) {                                                 
                                if($vswitch['VLANID'] == $bondVswitch['VLANID']){
                                    // var_dump($bondVswitch);
                                    // var_dump($vswitch);     
                                    $haveSameVswitch = true;
                                    $bondVswitch['haveSameVswitch'] = true;
                                    if($vswitch['Name'] != $bondVswitch['Name']){
                                        // if($this->sqlCheckDuplicatevSwitchName($vswitch['Name'])){
                                        //     $logCode='0730020C';
                                        //     $appendErr="(Duplicate Vswitch Name(".$vswitch['Name'].")";
                                        //     goto failRollback;
                                        // }
                                        if(!$this->sqlUpdatevSwitchNew(array('VswitchID'=>$bondVswitch['VswitchID'],'Name'=>$vswitch['Name'],'Desc'=>''))){
                                            $logCode='0730020D';
                                            $appendErr="(Failed to update Vswitch Name(".$vswitch['Name'].")";
                                            goto failRollback;
                                        }                                        
                                    }
                                    if(!($vswitch['IP'] == $bondVswitch['IP'] && $vswitch['Mask'] == $bondVswitch['Mask'] && $vswitch['Gateway'] == $bondVswitch['Gateway'])){
                                        if(!$this->sqlUpdatetbvswitchbondmappingset(array('VswitchID'=>$bondVswitch['VswitchID'],'BondID'=>$idBond,'IP'=>$vswitch['IP'],'Mask'=>$vswitch['Mask'],'Gateway'=>$vswitch['Gateway']))){
                                            $logCode='0730020E';
                                            $appendErr="(Failed to update Vswitch and Bond mapping(".$vswitch['Name'].":$indexBond)";
                                            goto failRollback;
                                        }     
                                    }
                                    break;
                                }
                               
                            }
                        }
                        if($haveSameVswitch)
                            break;
                    }
                    // var_dump($haveSameVswitch);                
                    if(!$haveSameVswitch){
                        // var_dump('Insert Vswitch New');
                        if(!$this->sqlListvSwitchIDByidVlanidBridge(array('BridgeID'=>$indexBond,'VLANID'=>(int)$vswitch['VLANID']),$vswitchID)){
                            $logCode='0730020F';
                            $appendErr="(Failed to list Vswitch)";
                            goto failRollback;
                        }                                                                                 
                        // var_dump($vswitchID);
                        if(is_null($vswitchID)){
                            // if($this->sqlCheckDuplicatevSwitchName($vswitch['Name'])){
                            //     $logCode='0730020C';
                            //     $appendErr="(Duplicate Vswitch Name(".$vswitch['Name'].")";
                            //     goto failRollback;
                            // }
                            if(!$this->sqlInsertVswitchNew(array('BridgeID'=>$indexBond,'VLANID'=>(int)$vswitch['VLANID'],'Name'=>$vswitch['Name']))){
                                $logCode='07300210';
                                $appendErr="(Failed to insert Vswitch(".$vswitch['Name'].")";
                                    // var_dump(5);
                                goto failRollback;
                            }
                            $vswitchID = connectDB::$dbh->lastInsertId();
                        }else{
                            // if($this->sqlCheckDuplicatevSwitchName($vswitch['Name'],$vswitchID)){
                            //     // var_dump('same name');
                            //     $logCode='0730020C';
                            //     $appendErr="(Duplicate Vswitch Name(".$vswitch['Name'].")";                            
                            //     goto failRollback;
                            // }
                            if(!$this->sqlUpdatevSwitchNew(array('VswitchID'=>$vswitchID,'Name'=>$vswitch['Name'],'Desc'=>''))){
                                // var_dump(3);
                                $logCode='0730020D';
                                $appendErr="(Failed to update Vswitch Name(".$vswitch['Name'].")";
                                goto failRollback;
                            }   
                        }                    
                        if(is_null($vswitchID)){
                            $logCode='0730020F-1';
                            $appendErr="(Failed to list Vswitch)";
                            goto failRollback;
                        }
                        if(!$this->sqlInserttbvswitchbondmappingset(array('BondID'=>$idBond,'VswitchID'=>$vswitchID,'CanModify'=>true,'IsCommunicate'=>false,'IP'=>$vswitch['IP'],'Mask'=>$vswitch['Mask'],'Gateway'=>$vswitch['Gateway']))){
                            // var_dump(6)                        
                            $logCode='07300211';
                            $appendErr="(Failed to insert Vswitch and Bond mapping(".$vswitch['Name'].":$indexBond)";
                            goto failRollback;
                        }
                    }         
                    $setArr[] = array('ConnectIP'=>$node['CommunicateIP'],'BondIndex'=>$indexBond,'VLANID'=>$vswitch['VLANID'],'IP'=>$vswitch['IP'],'Mask'=>$vswitch['Mask'],'Gateway'=>$vswitch['Gateway'],'Devs'=>$value['Devs']);       
                    // $this->nicVLANCreateLocalShell();
                }                            
                $indexBond++;
            }
        }
        $this->sqlListVswitchDistinctCountNameAlias($outputCount);        
        if(is_null($outputCount)){
            $logCode='0730020F-1';
            $appendErr="(Failed to list Vswitch Count)";
            goto failRollback;
        }           
        if($outputCount['Count'] != $outputCount['DiffCount']){
            // var_dump('Duplicate');
            $logCode='0730020C';
            $appendErr="(Duplicate Vswitch Name)";
            goto failRollback;
        }                            
        // var_dump($haveManager);
        // var_dump($haveCommunicate);
        if(!$haveManager || !$haveCommunicate){
            $logCode='07300212';
            $appendErr="(Can't change manager or communicate IP)";
            goto failRollback;
        }        
        // $outputBonds = array_values($outputBonds);
        // var_dump($outputBonds);
        foreach ($outputBonds as &$bond) {  
            // var_dump($bond);          
            foreach ($bond['Vswitchs'] as &$bondVswitch) {   
                // var_dump($bondVswitch);             
                if(!$bondVswitch['haveSameVswitch']){
                    // var_dump($bondVswitch);
                    if(!$this->sqlDeletetbvswitchbondmappingset(array('BondID'=>$bond['BondID'],'VswitchID'=>$bondVswitch['VswitchID']))){
                        $logCode='07300213';
                        $appendErr="(Failed to delete Vswitch and Bond mapping(".$bond['BondID'].':'.$bondVswitch['VswitchID']."))";
                        goto failRollback;
                    }
                }
            }
        }
        if(!$this->sqlCleartbvdservernicsetMappingBond($input)){
            $logCode='07300214';
            $appendErr="(Failed to clear NIC and Bond mapping)";
            goto failRollback;
        }
        // var_dump($bondDevs);
        foreach ($bondDevs as $value) {
            // var_dump($value);
            if(!$this->sqlUpdatetbvdservernicsetMappingBond(array('Dev'=>$value['Dev'],'BondID'=>$value['BondID'],'NodeID'=>$input['NodeID']))){
                $logCode='07300215';
                $appendErr="(Failed to update NIC and Bond mapping)";
                goto failRollback;
            }
        }
        $indexBond=$indexBond-1;        
        for($i=($indexBond+1);$i<count($outputBonds);$i++){            
            // var_dump($outputBonds[$i]['BondID']);          
            $idBond=$outputBonds[$i]['BondID'];
            if(!$this->sqlDeleteVDServerBond(array('BondID'=>$idBond,'NodeID'=>$input['NodeID']))){
                $logCode='07300216';
                $appendErr="(Failed to delete Bond($idBond))";
                goto failRollback;
            }
        }        
        if(!$this->sqlClearNonMappingtbvswitchset()){
            $logCode='07300217';
            $appendErr="(Failed to clear non mapping Vswitch)";
            goto failRollback;
        }       

        if($this->nicVLANBeginConfigShell(array('ConnectIP'=>$node['CommunicateIP']))!=0){
            $logCode='07300218';
            $appendErr="(Failed to begin config)";
            goto failRollback;
        }
        foreach ($setArr as $set) {
            if($this->nicVLANCreateLocalShell($set)!=0){
                $logCode='07300219';
                $appendErr="(Failed to set config)";
                if($this->nicVLANAbortConfigShell(array('ConnectIP'=>$node['CommunicateIP']))!=0){
                    $logCode='0730021A';
                    $appendErr="(Failed to abort config)";
                }
                goto failRollback;       
            }
        }
        if($this->nicVLANEndConfigShell(array('ConnectIP'=>$node['CommunicateIP']))!=0){
            $logCode='0730021B';
            $appendErr="(Failed to end config)";
            goto failRollback;
        }
        $nodeC->rebootNode(array('ConnectIP'=>$input['ConnectIP'],'CommunicateIP'=>$node['CommunicateIP'],'NodeID'=>$input['NodeID']));
        $appendErr=NULL;
        $logCode='07100101';       
        connectDB::$dbh->commit();            
        $this->listNodevSwitchNew($input,$output);        
        $responsecode = CPAPIStatus::SetVSwitchSuccess;
        goto end;
failRollback:
        connectDB::$dbh->rollBack();
        goto end;
end:
        $this->insertSetvSwitchlog($logCode,$input,$appendErr);
        return $responsecode;
    }

    function insertSetvSwitchlog($code,$input,$appendErr,$errCode=NULL)
    {
        $logType = 7;
        $logLevel = 3;
        if(is_null($appendErr)){
            $logLevel = 1;
        }   
        if(is_null($appendErr)){          
            $message = "Set Node(".$input['NodeName'].") Vswitch Success";
        }
        else{
            $message = "Failed to Set Node(".$input['NodeName'].") Vswitch$appendErr";
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

    function nicVLANBeginConfigShell($input)
    {       
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){    
            $cmd = $this->cmdNICVLANBeginConfig;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd); 
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);
            // var_dump($rtn);
        }   
        return $rtn;
    }

    function nicVLANCreateLocalShell($input)
    {
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){    
            $cmd = $this->cmdNICVLANCreateLocal;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);  
            $cmd .= '"'.$input['VLANID'].'" ';
            $cmd .= '"'.$input['BondIndex'].'" ';
            $cmd .= '"static" ';
            $cmd .= '"'.$input['IP'].'" ';
            $cmd .= '"'.$input['Mask'].'" ';
            $cmd .= '"'.$input['Gateway'].'" ';
            $cmd .= '"'.count($input['Devs']).'" ';
            foreach ($input['Devs'] as $value) {
                $cmd .= '"'.substr($value, 3).'" ';
            }
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);
            // var_dump($rtn);
        }   
        return $rtn;   
    }

    function nicVLANAbortConfigShell($input)
    {
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){    
            $cmd = $this->cmdNICVLANAbortConfig;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);
            // var_dump($rtn);
        }   
        return $rtn;
    }

    function nicVLANEndConfigShell($input)
    {
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){    
            $cmd = $this->cmdNICVLANEndConfig;            
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);
            // var_dump($rtn);
        }   
        return $rtn;
    }
    
    function listVMSIP($input)
    {

    }

    function setVMSIP($input)
    {
        
    }

    function clearNodeVswitchShell($input){
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){    
            $cmd = $this->cmdClusterNICClearAll;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);   
            $cmd .= $input['CommunicateIP'];       
            // var_dump($cmd);     
            exec($cmd,$outputArr,$rtn);   
        }   
        return $rtn;
    }

    function clearVswitchShell($input){
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){    
            $cmd = $this->cmdNICClearAll ;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);   
            // var_dump($cmd); 
            exec($cmd,$outputArr,$rtn);   
        }   
        return $rtn;
    }
    
    function setVswitchShell($input,$arg){
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){    
            $cmd = $this->cmdNICBrSet ;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);    
            $cmd .= $arg;      
            // var_dump($cmd);                
            exec($cmd,$outputArr,$rtn);
        }          
        return $rtn;     
    }

     function setNodeVswitchShell($input,$arg){
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){    
            $cmd = $this->cmdClusterNICBrSet ;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);    
            $cmd .= $arg;      
            // var_dump($cmd);                
            exec($cmd,$outputArr,$rtn);
        }          
        return $rtn;     
    }
    
    function setDNSShell($input,$arg){
        $rtn = -2;
        if(!is_null($input['ConnectIP'])){    
            $cmd = $this->cmdNICDNSSet ;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);    
            $cmd .= $arg;         
            // var_dump($cmd);     
            exec($cmd,$outputArr,$rtn);
        }          
        return $rtn;     
    }

    function sqlClearNICidVSwitch($idServer){
        $result = false;
        $SQLUpdate = "Update tbVDServerNICSet SET idVSwitch=null WHERE idVDServer=:idVDServer";
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLUpdate);            
            $sth->bindValue(':idVDServer', $idServer, PDO::PARAM_INT);                        
            $result = $sth->execute();               
        }
        catch (Exception $e){                
        }        
        return $result;
    }
    
    function sql_clear_vswitch($idServer){
        $result = false;
        $SQLDelete = "DELETE FROM tbVSwitchSet WHERE idVDServer=:idVDServer";
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLDelete);            
            $sth->bindValue(':idVDServer', $idServer, PDO::PARAM_INT);                        
            $result = $sth->execute();               
        }
        catch (Exception $e){                
        }        
        return $result;
    }
    
    function process_vswitch_error_code($code){          
        $this->baseOutputResponse($code);
        switch ($code){            
            case CPAPIStatus::ListVSwitchFail:                 
            case CPAPIStatus::SetVSwitchFail:   
            case CPAPIStatus::ModifyVSwitchFail:
                http_response_code(400);
                break;                                         
        }
        
    }
}

