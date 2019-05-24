<?php
class vSwitchAction extends BaseAPI
{  
    private $cmdNICInfo = CmdRoot."ip nic_info ";
    private $cmdNICBrReset = CmdRoot."ip nic_br_reset ";
    private $cmdNICBrSet = CmdRoot."ip nic_br_set ";
    private $cmdNICClearAll = CmdRoot."ip nic_clear_all";
    private $cmdNICDNSSet = CmdRoot."ip nic_dns_set ";
    function  __construct()
    {    
        set_time_limit(0);
    }
    
    function listVswitch($input,&$outputAll){       
        
        $output = null;        
        $outputFree = null;              
        $this->listVswitchShell($input, $outputIP);
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
        foreach ($outputIP['vdnic'] as $key=>$vdnic){      
            $dhcp = false;
            $devs = array();
            foreach ($vdnic['nic'] as $value) {
                $index = (int)$value;
                $devs[] = $outputFree[$index];
                $removeFromFree[] = $index;                
            }            
            if($vdnic['protocol'] == 'dhcp')
                $dhcp = true;
            $output[$key] = array('DHCP'=>$dhcp,'IP' => $vdnic['ipv4'], 'Mask'=>$vdnic['mask'],'Gateway'=>$vdnic['gateway'],'Devs'=>$devs);
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
//        var_dump($output);
//        var_dump($outputFree);
        $outputAll = array("Vswitchs"=>$output,"FreeDevs"=>$outputFree,"DNS"=>$dnsIP);
        return CPAPIStatus::ListVSwitchSuccess;
    }        
    
    
    function listVswitchShell($input,&$output){        
        if (!is_null($input['ConnectIP'])){    
            $cmd = $this->cmdNICInfo ;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);              
            exec($cmd,$outputArr,$rtn);                
            if($rtn == 0){             
                $output = json_decode($outputArr[0],true);
                return;
            }  
        }              
    }
    
    function sql_list_vswitch_by_idserver($idServer,&$output,&$output_free){
//        $SQLSelectCount = <<<SQL
//            SELECT Count(name) as count FROM tbVDServerNicSet WHERE idVDServer=:idVDServer                    
SQL;
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
            $sth = $this->dbh->prepare($SQLSelect);
            $sth->bindValue(':idVDServer', $idServer, PDO::PARAM_INT);                    
            if($sth->execute())
            {                          
                while( $row = $sth->fetch() ) 
                {              
//                        if($vswitchs[$row['idVSwitch']]["Alias"] == ''){
                    $vswitchs[$row['idVSwitch']]["ID"] = (int)$row['idVSwitch'];                            
                    $vswitchs[$row['idVSwitch']]["Alias"] =$row['nameAlias'];                       
                    $vswitchs[$row['idVSwitch']]["IP"] =$row['ip'];
                    $vswitchs[$row['idVSwitch']]["Mask"] =$row['mask'];
                    $vswitchs[$row['idVSwitch']]["Gateway"] =$row['gateway'];
                    $vswitchs[$row['idVSwitch']]["Enable"] =(bool)$row['isEnable'];
//                        }
                    if(isset($row['name']))
                        $vswitchs[$row['idVSwitch']]["Devs"][] =$row['name'];
                    else
                        $vswitchs[$row['idVSwitch']]["Devs"] = array();
                }   
                if(count($vswitchs) > 0)
                    $output = array_values($vswitchs);
                else
                {
                    $output = array();
                }                    
                $sth = $this->dbh->prepare($SQLSelectFree);
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
    
    function sql_insert_vdserver_nic($idServer,$index){
        $result = false;
        $SQLInsert = "INSERT tbVDServerNicSet (idVDServer,name) Values (:idVDServer,:name)";
        try
        {              
            $sth = $this->dbh->prepare($SQLInsert);            
            $sth->bindValue(':idVDServer', $idServer, PDO::PARAM_INT);            
            $sth->bindValue(':name', "eth$index", PDO::PARAM_STR);            
            $result = $sth->execute();               
        }
        catch (Exception $e){                
        }        
        return $result;
    }
    
    function sql_insert_vswitch($idServer,$alias,$ip,$mask,$gateway,$index,$enable){
        $result = false;
        $SQLInsert = "INSERT tbVSwitchSet (idVDServer,idVSwitch,isEnable,nameAlias,ip,mask,gateway) "
                . "Values (:idVDServer,:idVSwitch,:isEnable,:nameAlias,:ip,:mask,:gateway)";
        try
        {                            
            $sth = $this->dbh->prepare($SQLInsert);            
            $sth->bindValue(':idVDServer', $idServer, PDO::PARAM_INT);            
            $sth->bindValue(':idVSwitch', $index, PDO::PARAM_INT);            
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
    
    function sql_update_vswitch($idServer,$idVSwitch,$alias,$ip,$mask,$gateway,$enable){
        $result = false;
        $SQLUpdate = "UPDATE tbVSwitchSet SET isEnable=:isEnable,nameAlias=:nameAlias,ip=:ip"
                . ",mask=:mask,gateway=:gateway WHERE idVDServer=:idVDServer AND idVSwitch=:idVSwitch";                
        try
        {                            
            $sth = $this->dbh->prepare($SQLUpdate);            
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
    
    function sql_update_idVSwitch($idServer,$idVSwitch,$ethindex){
        $result = false;
        $SQLUpdate = "UPDATE tbVDServerNicSet SET idVSwitch = :idVSwitch "
                . "WHERE idVDServer=:idVDServer AND name=:name";
        try
        {                            
            $sth = $this->dbh->prepare($SQLUpdate);      
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
            $sth = $this->dbh->prepare($SQLSelect);            
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
            $sth = $this->dbh->prepare($SQLUpdate);      
            $sth->bindValue(':newidVSwitch', $newidVSwitch, PDO::PARAM_INT); 
            $sth->bindValue(':orgidVSwitch', $orgidVSwitch, PDO::PARAM_INT);                             
            $sth->bindValue(':idGfs', $idGfs, PDO::PARAM_INT);            
            $result = $sth->execute();               
        }
        catch (Exception $e){                
        }        
        return $result;
    }
    
    function setVswitch($input){
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
        $systemC = new SystemProcess();
        // $systemC->reboot($input);
        return CPAPIStatus::SetVSwitchSuccess;
    }
    
    function sql_list_idgfs_of_cluster($idCluster,&$output)
    {
        $SQLListGFSID = "SELECT idGfs FROM tbGfsLunSet WHERE idCluster = :idCluster";
        try
        {                            
            $sth = $this->dbh->prepare($SQLListGFSID);
            $sth->bindValue(':idCluster', $idCluster, PDO::PARAM_INT); 
            if($sth->execute())
            {                
                while( $row = $sth->fetch() ) 
                {
                    $output = (int)$row['idGfs'];
                }
            }               
        }
        catch (Exception $e){                
        }                
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

    function sql_clear_nic_idVSwitch($idServer){
        $result = false;
        $SQLUpdate = "Update tbVDServerNICSet SET idVSwitch=null WHERE idVDServer=:idVDServer";
        try
        {                            
            $sth = $this->dbh->prepare($SQLUpdate);            
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
            $sth = $this->dbh->prepare($SQLDelete);            
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
                http_response_code(400);
                break;                                         
        }
        
    }
}

