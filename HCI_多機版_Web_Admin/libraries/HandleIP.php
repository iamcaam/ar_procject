<?php
class IPProcess extends BaseAPI
{   
    protected $externalPath = '/var/www/html/webapi/External.config';
    private $cmdClusterNodeSpicePortSet = CmdRoot.'ip cluster_node_spice_port_set ';

    function __construct() {        
    }

    function listNodeExternal($input,&$output)
    {        
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        if(!$this->sqlAddtbIpPortMappingSetTable()){
            return CPAPIStatus::DBConnectFail;   
        }
        $this->sqlListNodeExternal($input,$output);        
    }    

    function sqlListNodeExternal($input,&$output)
    {        
        $output = array();
        $sqlList = <<<SQL
            SELECT ipExternal,count(ipExternal) as count,MIN(portExternal) as minPort from tbipportmappingset WHERE idVDServer = :idVDServer GROUP BY ipExternal;
SQL;
        try
        {                     
            $sth = connectDB::$dbh->prepare($sqlList);
            $sth->bindValue(':idVDServer', $input['NodeID'], PDO::PARAM_STR); 
            if($sth->execute())
            {                                
                while( $row = $sth->fetch() ) 
                {
                    $output[] = array('ExternalIP'=>$row['ipExternal'],'ExternalPort'=>(int)$row['minPort']
                            ,'ExternalPortCount'=>(int)$row['count']);
                }                
            }                            
        }
        catch (Exception $e){             
        }    
    }

    function setNodeExternal($input,&$output)
    {
        // var_dump($input);
        $nodeC = new NodeAction();
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }        
        $nodeC->sqlListServerByidVDServer($input['NodeID'],$outputNode);
        if(!isset($outputNode['CommunicateIP'])){
            return CPAPIStatus::SetNodeExternalFail;
        }
        connectDB::$dbh->beginTransaction();
        if(!$this->sqlCleanNodeExternal($input)){
            connectDB::$dbh->rollBack();
            return CPAPIStatus::SetNodeExternalFail;
        }
        if(count($input['Externals']) > 0){
            if(!$this->sqlInsertNodeExternal($input)){
                connectDB::$dbh->rollBack();
                return CPAPIStatus::SetNodeExternalFail;
            }
            $this->sqlListNodeExternalMinMax($input,$outputPort);
            if(!isset($outputPort['Min']) && isset($outputPort['Max'])){
                connectDB::$dbh->rollBack();
                return CPAPIStatus::SetNodeExternalFail;
            }
            $shellData = array('ConnectIP'=>$input['ConnectIP'],'CommunicateIP'=>$outputNode['CommunicateIP'],'Min'=>$outputPort['Min'],'Max'=>$outputPort['Max']);
            if($this->clusterNodeSpicePortSetShell($shellData) != 0){
                connectDB::$dbh->rollBack();
                return CPAPIStatus::SetNodeExternalFail;
            }
        }
        else{
            $shellData = array('ConnectIP'=>$input['ConnectIP'],'CommunicateIP'=>$outputNode['CommunicateIP'],'Min'=>-1,'Max'=>-1);
            if($this->clusterNodeSpicePortSetShell($shellData) != 0){
                connectDB::$dbh->rollBack();
                return CPAPIStatus::SetNodeExternalFail;
            }
        }
        connectDB::$dbh->commit();
        $this->listNodeExternal($input,$output);
        return CPAPIStatus::SetNodeExternalSuccess;
    }

    function clusterNodeSpicePortSetShell($input)
    {
        $cmd = $this->cmdClusterNodeSpicePortSet;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);          
        $cmd .= '"'.$input['CommunicateIP'].'" ';
        $cmd .= '"'.$input['Min'].'" ';                   
        $cmd .= '"'.$input['Max'].'" ';
        // var_dump($cmd);
        exec($cmd,$output,$rtn);
        // var_dump($rtn);  
        return $rtn;
    }

    function sqlCleanNodeExternal($input)
    {
        $result = false;
        $output = array();
        $sqlDelete = <<<SQL
            DELETE FROM tbipportmappingset WHERE idVDServer = :idVDServer AND portInternal>0;
SQL;
        try
        {                     
            $sth = connectDB::$dbh->prepare($sqlDelete);
            $sth->bindValue(':idVDServer', $input['NodeID'], PDO::PARAM_STR); 
            if($sth->execute())
            {                                
                $result = true;
            }                            
        }
        catch (Exception $e){             
        }    
        return $result;
    }

    function sqlInsertNodeExternal($input)
    {
        $result = false;
        $sqlInsert = "INSERT INTO tbipportmappingset (idVDServer, ipExternal, portExternal, portInternal) VALUES ";
        $portTotalCount = 0;
        foreach ($input['Externals'] as $value) {
            $portTotalCount += $value['ExternalPortCount'];
        }
        // var_dump($portTotalCount);
        $qPart = array_fill(0, $portTotalCount, "(?, ?, ?, ?)");        
        $sqlInsert .=  implode(",",$qPart);
        try
        {                                     
            $sth = connectDB::$dbh->prepare($sqlInsert);
            $i = 1;
            foreach ($input['Externals'] as $value) {
                $portInternal = $value['ExternalPort'];
                for($j=0;$j<$value['ExternalPortCount'];$j++) { //bind the values one by one
                    if($j == 0)
                        $portExternal = $value['ExternalPort'];
                    $sth->bindValue($i++, $input['NodeID']);
                    $sth->bindValue($i++, $value['ExternalIP']);
                    $sth->bindValue($i++, $portExternal++);
                    $sth->bindValue($i++, $portInternal++);
                }
            }
            // var_dump($i);
            if($sth->execute())
            {
                $result = true;
            }
        }
        catch (Exception $e){
        }
        // var_dump($result);
        return $result;
    }

    function sqlListNodeExternalMinMax($input,&$output)
    {
        $output =null;
        $sqlList = "select Min(portExternal) as min,Max(portExternal) as max from tbipportmappingset where idVDServer = :idVDServer;";
        try
        {                                     
            $sth = connectDB::$dbh->prepare($sqlList);
            $sth->bindValue(':idVDServer', $input['NodeID'], PDO::PARAM_INT);
            if($sth->execute())
            {
                $output=array();
                while( $row = $sth->fetch() ) 
                {
                    $output['Min'] = $row['min'];
                    $output['Max'] = $row['max'];
                }
            }
        }
        catch (Exception $e){
        }
    }

    function processNetworkErrorCode($code){  
        $this->baseOutputResponse($code);
        switch ($code){                        
            case CPAPIStatus::SetNodeExternalFail:
                http_response_code(400);
                break;               
        }
    }
}