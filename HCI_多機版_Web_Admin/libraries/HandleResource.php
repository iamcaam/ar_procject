<?php
class ResourceAction extends BaseAPI
{
    function  __construct()
    {        
    }                                                         
    
    function listResource($input,&$output)
    {        
        $this->connectDB();        
        if(connectDB::$dbh == null){
            return CPAPIStatus::DBConnectFail;        
        }
        if(!$this->sqlListServerInfoSet($output))
            return CPAPIStatus::ListResourceFail;
        if(!$this->sqlListAllvSwitch($outputvSwitch))
            return CPAPIStatus::ListResourceFail;        
        $output['vSwitch'] = $outputvSwitch;        
        $vdC = new VDAction();
        $vdC->listAllISO($input,$outputISO);
        $output['CDRom']=$outputISO['CDRom'];
        $output['Storage'] = 0;
        $glusterC = new GlusterAction();
        if($glusterC->listClusterVolumes($input,$outputVolumes) == CPAPIStatus::ListClusterVolumesSuccess){            
            $output['Volumes'] = $outputVolumes;
        }

        return CPAPIStatus::ListResourceSuccess;
    }

    function sqlListServerInfoSet(&$output)
    {
        $result = false;
        $output['CPU'] = 0;
        $output['MinMem'] = 0;
        $output['MaxMem'] = 0;
        $sqlList = <<<SQL
                SELECT * FROM tbvdserverinfoset;
SQL;
        try        
        {              
            $sth = connectDB::$dbh->prepare($sqlList);                             
            if($sth->execute())
            {                    
                $output = array();
                $i = 0;
                while( $row = $sth->fetch() ) 
                {      
                    if($i == 0){
                        $output['MinMem'] =  (int)$row['sizeRam'];
                        $output['MaxMem'] =  (int)$row['sizeRam'];
                    }
                    else{
                        $ram = (int)$row['sizeRam'];                        
                        if($ram > $output['MaxMem'])
                            $output['MaxMem'] = $ram;
                        if($ram < $output['MinMem'])
                            $output['MinMem'] = $ram;
                    }
                    $cpuhz = (int)$row['hzCpu'];
                    if($cpuhz > $output['CPU'])
                        $output['CPU'] = $cpuhz;
                    $i++;
                }                     
                $result = true;
            }                     
        }
        catch (Exception $e){                           
        }     
        return $result;
    }

    function sqlListAllvSwitch(&$output)
    {
        $result = false;
        $sqlList = <<<SQL
                SELECT nameAlias,idVSwitch FROM tbvswitchset;
SQL;
        try        
        {              
            $sth = connectDB::$dbh->prepare($sqlList);                             
            if($sth->execute())
            {                     
                $output = array();               
                while( $row = $sth->fetch() ) 
                {                                      
                    $output[] = array('Name'=>$row['nameAlias'],'VswitchID'=>$row['idVSwitch']);
                }                     
                $result = true;
            }                     
        }
        catch (Exception $e){                           
        }     
        return $result;
    }

    function sqlListMinvSwitchCountFromServers(&$output)
    {
        $result = false;
        $sqlList = <<<SQL
                SELECT MIN(mycount) mincount FROM (SELECT COUNT(idVSwitch) mycount FROM (SELECT idVDServer,idVSwitch FROM tbvdservernicset temp1 GROUP BY idVSwitch,idVDServer)temp2 GROUP BY idVDServer)temp3;
SQL;
        try        
        {              
            $sth = connectDB::$dbh->prepare($sqlList);                             
            if($sth->execute())
            {                                    
                while( $row = $sth->fetch() ) 
                {                                      
                    $output = $row['mincount'];                    
                }                     
                if(isset($output))
                    $result = true;
            }                     
        }
        catch (Exception $e){                           
        }     
        return $result;
    }

    function processResourceErrorCode($code){  
        $this->baseOutputResponse($code);
        switch ($code){            
            case CPAPIStatus::CreateResourceFail:   
            case CPAPIStatus::ListResourceFail:
            case CPAPIStatus::DeleteResourceFail:
            case CPAPIStatus::ListClusterResourceFail:  
            case CPAPIStatus::ModifyResourceFail:
            case CPAPIStatus::ListAllocatedofResourceFail:
                http_response_code(400);
                break;
            case CPAPIStatus::ListClusterPollingFail:
                http_response_code(417);
                break;                       
        }        
    }
}

