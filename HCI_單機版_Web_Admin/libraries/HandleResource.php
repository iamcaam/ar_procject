<?php
class Resource_Action extends BaseAPI
{
    function  __construct($dbh =null,$log_c=null)
    {    
        if(!is_null($dbh))
            $this->dbh=$dbh;
        if(!is_null($log_c))
            $this->log_c = $log_c;      
    }
    
    function add_resource_of_domain($input,&$output){
        $this->connectDB();        
        if($this->dbh == null){
            return CPAPIStatus::DBConnectFail;
        }
        else
        {
            if(is_null($this->log_c))
                $this->log_c = new LogAction($this->dbh);
            $output = null;
            $this->sql_list_resource_by_gfsid_name($input['GFSID'], $input['Name'], $output);
            if(!is_null($output)){
                $this->log_c->createLog(array('Type'=>3,'Level'=>3,'Code'=>'03300201','Riser'=>'admin'
                    ,'Message'=>"Failed to create resource(".$input['Name'].")(Resource's name is duplicated)"));
                return CPAPIStatus::Conflict;          
            }
            if(!isset($input['Vswitch']))
                $input['Vswitch'] = 0;
//            var_dump($input);
            if(!$this->sql_insert_resource($input)){
                $this->log_c->createLog(array('Type'=>3,'Level'=>3,'Code'=>'03300202','Riser'=>'admin'
                    ,'Message'=>"Failed to create resource(".$input['Name'].")"));
                return CPAPIStatus::CreateResourceFail;
            }
            $output = null;
            $this->sql_list_resource_by_gfsid_name($input['GFSID'], $input['Name'], $output);    
            if(is_null($output)){
                $this->log_c->createLog(array('Type'=>3,'Level'=>3,'Code'=>'03300202','Riser'=>'admin'
                    ,'Message'=>"Failed to create resource(".$input['Name'].")"));
                return CPAPIStatus::CreateResourceFail;
            }
            $this->log_c->createLog(array('Type'=>3,'Level'=>1,'Code'=>'03100101','Riser'=>'admin'
                    ,'Message'=>"Create resource(Name:".$input['Name'].",CPU:".$input['CPU'].",RAM:".$input['RAM'].",Quota:".$input['Quota'].",Virtual Switch:Virtual Switch ".$input['Vswitch'].") success"));
            return CPAPIStatus::CreateResourceSuccess;
        }
    }        
    
    function modify_resource_of_domain($input,&$output){
        $this->connectDB();        
        if($this->dbh == null){
            return CPAPIStatus::DBConnectFail;
        }
        else
        {
            if(is_null($this->log_c))
                $this->log_c = new LogAction($this->dbh);
            $output_org = null;
            $this->sql_list_resource_by_domainid_gfsid( $input['DomainID'],$input['GFSID'],$output_org);
            if(is_null($output_org)){
                $this->log_c->createLog(array('Type'=>3,'Level'=>3,'Code'=>'03300601','Riser'=>'admin'
                    ,'Message'=>"Failed to modify resource(Failed to list resource)"));
                return CPAPIStatus::ModifyResourceFail;          
            }            
            $output_disk_size = null;
            $this->sql_list_disksize_by_idDomain_idGfs($input['DomainID'],$input['GFSID'], $output_disk_size);
            if(is_null($output_disk_size)){
                $this->log_c->createLog(array('Type'=>3,'Level'=>3,'Code'=>'03300601','Riser'=>'admin'
                    ,'Message'=>"Failed to modify resource(Failed to list resource)"));
                return CPAPIStatus::ModifyResourceFail;          
            }            
//            var_dump($input);
//            var_dump($output_org);
            if($input['Quota'] < $output_disk_size){
                $this->log_c->createLog(array('Type'=>3,'Level'=>3,'Code'=>'03300601','Riser'=>'admin'
                    ,'Message'=>"Failed to modify resource(Setting Capacity is over )"));
                return CPAPIStatus::ModifyResourceFail;          
            }
            if($output_org['Name'] != $input['Name']){
                $output = null;
                $this->sql_list_resource_by_gfsid_name($input['GFSID'], $input['Name'], $output);
                if(!is_null($output)){
                    $this->log_c->createLog(array('Type'=>3,'Level'=>3,'Code'=>'03300602','Riser'=>'admin'
                        ,'Message'=>"Failed to modify resource(".$input['Name'].")(Resource's name is duplicated)"));
                    return CPAPIStatus::Conflict;          
                }
            }
//            echo 1;
            if(!$this->sql_update_resource($input)){
                $this->log_c->createLog(array('Type'=>3,'Level'=>3,'Code'=>'03300603','Riser'=>'admin'
                    ,'Message'=>"Failed to modify resource(".$input['Name'].")"));
                return CPAPIStatus::ModifyResourceFail;
            }
            $output = null;
            $this->sql_list_resource_by_gfsid_name($input['GFSID'], $input['Name'], $output);    
            if(is_null($output)){
                $this->log_c->createLog(array('Type'=>3,'Level'=>3,'Code'=>'03300603','Riser'=>'admin'
                    ,'Message'=>"Failed to modify resource(".$input['Name'].")"));
                return CPAPIStatus::ModifyResourceFail;
            }
            $this->log_c->createLog(array('Type'=>3,'Level'=>1,'Code'=>'03100501','Riser'=>'admin'
                    ,'Message'=>"Modify resource(Name:".$input['Name'].",CPU:".$input['CPU'].",RAM:".$input['RAM'].",Quota:".$input['Quota'].",Virtual Switch:Virtual Switch ".$input['Vswitch'].") success"));
            return CPAPIStatus::ModifyResourceSuccess;
        }
    }  
    
    function sql_list_resource_by_gfsid_name($id,$name,&$output){
         $SQLList = <<<SQL
            SELECT t1.*,t3.nameCluster FROM tbTerritorySet as t1 
                INNER JOIN tbGfsLunSet as t2
                INNER JOIN tbClusterSet as t3
                ON t1.idGfs = t2.idGfs AND t2.idCluster = t3.idCluster
                WHERE t1.nameTerritory=:nameTerritory AND t1.idGfs = :idGfs
SQL;
        try        
        {  
            $sth = $this->dbh->prepare($SQLList);
            $sth->bindValue(':nameTerritory', $name, PDO::PARAM_STR);                    
            $sth->bindValue(':idGfs', $id, PDO::PARAM_INT);                    
            if($sth->execute())
            {                              
                while( $row = $sth->fetch() ) 
                {                         
                    $output = array("Name"=>$row['nameTerritory'],'Quota'=>(int)$row['quotaTerritory']
                            ,'GFSID'=>(int)$row['idGfs'],'DomainID'=>(int)$row['idDomain']
                            ,'CPU'=>(int)$row['hzCpu'],'RAM'=>(int)$row['sizeRam']
                            ,'ClusterName'=>$row['nameCluster'],'Vswitch'=>(int)$row['idVSwitch']);
                }                     
            }               
        }
        catch (Exception $e){                
            $output=null;
        }   
    }
    
    function sql_list_resource_by_domainid_gfsid($iddomain,$idgfs,&$output){    
         $SQLList = <<<SQL
            SELECT t1.*,t3.nameCluster FROM tbTerritorySet as t1 
                INNER JOIN tbGfsLunSet as t2                 
                INNER JOIN tbClusterSet as t3
                ON t1.idGfs = t2.idGfs AND t2.idCluster = t3.idCluster
                WHERE t1.idDomain=:idDomain AND t1.idGfs = :idGfs
SQL;
        try        
        {  
            $sth = $this->dbh->prepare($SQLList);
            $sth->bindValue(':idDomain', $iddomain, PDO::PARAM_INT);                    
            $sth->bindValue(':idGfs', $idgfs, PDO::PARAM_INT);                    
            if($sth->execute())
            {                              
                while( $row = $sth->fetch() ) 
                {                         
                    $output = array("Name"=>$row['nameTerritory'],'Quota'=>(int)$row['quotaTerritory']
                            ,'GFSID'=>(int)$row['idGfs'],'DomainID'=>(int)$row['idDomain']
                            ,'CPU'=>(int)$row['hzCpu'],'RAM'=>(int)$row['sizeRam']
                            ,'ClusterName'=>$row['nameCluster'],'Vswitch'=>(int)$row['idVSwitch']);
                }                     
            }               
        }
        catch (Exception $e){                
            $output=null;
        }   
    }        
    
    function sql_insert_resource($input){
        $result = false;
        $SQLInsert = <<<SQL
            INSERT tbTerritorySet (nameTerritory,idGfs,idDomain,quotaTerritory,hzCpu,sizeRam,idVSwitch) 
                Values (:nameTerritory,:idGfs,:idDomain,:quotaTerritory,:hzCpu,:sizeRam,:idVSwitch)
SQL;
        try        
        {  
            $sth = $this->dbh->prepare($SQLInsert);
            $sth->bindValue(':nameTerritory', $input['Name'], PDO::PARAM_STR);                    
            $sth->bindValue(':idGfs', $input['GFSID'], PDO::PARAM_INT);
            $sth->bindValue(':idDomain', $input['DomainID'], PDO::PARAM_INT);
            $sth->bindValue(':idVSwitch', $input['Vswitch'], PDO::PARAM_INT);
            $sth->bindValue(':quotaTerritory', $input['Quota'], PDO::PARAM_INT);
            $sth->bindValue(':hzCpu', $input['CPU'], PDO::PARAM_INT);
            $sth->bindValue(':sizeRam', $input['RAM'], PDO::PARAM_INT);
            $result = $sth->execute();                        
        }
        catch (Exception $e){                            
        }
        return $result;
    }
    
    function sql_update_resource($input){
        $result = false;
        $SQLUpdate = <<<SQL
            UPDATE tbTerritorySet SET nameTerritory=:nameTerritory,quotaTerritory=:quotaTerritory
                ,hzCpu=:hzCpu,sizeRam=:sizeRam,idVSwitch=:idVSwitch WHERE idGfs=:idGfs AND idDomain=:idDomain                 
SQL;
        try        
        {  
            $sth = $this->dbh->prepare($SQLUpdate);
            $sth->bindValue(':nameTerritory', $input['Name'], PDO::PARAM_STR);                    
            $sth->bindValue(':idGfs', $input['GFSID'], PDO::PARAM_INT);
            $sth->bindValue(':idDomain', $input['DomainID'], PDO::PARAM_INT);
            $sth->bindValue(':quotaTerritory', $input['Quota'], PDO::PARAM_INT);
            $sth->bindValue(':hzCpu', $input['CPU'], PDO::PARAM_INT);
            $sth->bindValue(':sizeRam', $input['RAM'], PDO::PARAM_INT);
            $sth->bindValue(':idVSwitch', $input['Vswitch'], PDO::PARAM_INT);
            $result = $sth->execute();                        
        }
        catch (Exception $e){                            
        }
        return $result;
    }
    
    function list_resource_of_domain($input,&$output){
        $this->connectDB();        
        if($this->dbh == null){
            return CPAPIStatus::DBConnectFail;
        }
        else
        {
            $output = null;           
            $this->sql_list_resource_of_domain($input['DomainID'], $output);           
            if(is_null($output))
                return CPAPIStatus::ListResourceFail;
            return CPAPIStatus::ListResourceSuccess;
        }
    }
    
    function sql_list_resource_of_domain($id,&$output){
         $SQLList = <<<SQL
            SELECT t1.*,t3.nameCluster FROM tbTerritorySet as t1
            INNER JOIN tbGfsLunSet as t2
            INNER JOIN tbClusterSet as t3
            ON t1.idGfs = t2.idGfs AND t2.idCluster = t3.idCluster
            WHERE idDomain = :idDomain
SQL;
        try        
        {  
            $sth = $this->dbh->prepare($SQLList);                       
            $sth->bindValue(':idDomain', $id, PDO::PARAM_INT);                    
            if($sth->execute())
            {                              
                $output=array();
                while( $row = $sth->fetch() ) 
                {                         
                    $output[] = array("Name"=>$row['nameTerritory'],'Quota'=>(int)$row['quotaTerritory']
                            ,'GFSID'=>(int)$row['idGfs'],'DomainID'=>(int)$row['idDomain']
                            ,'CPU'=>(int)$row['hzCpu'],'RAM'=>(int)$row['sizeRam']
                            ,'ClusterName'=>$row['nameCluster'],'Vswitch'=>(int)$row['idVSwitch']);
                }                     
            }               
        }
        catch (Exception $e){                
            $output=null;
        }   
    }
    
    function sql_list_resource_of_domainid_and_gfsid($idDomain,$idGfs,&$output){
         $SQLList = <<<SQL
            SELECT * FROM tbTerritorySet
            WHERE idDomain = :idDomain AND idGfs = :idGfs
SQL;
        try        
        {  
            $sth = $this->dbh->prepare($SQLList);                       
            $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT);                    
            $sth->bindValue(':idGfs', $idGfs, PDO::PARAM_INT); 
            if($sth->execute())
            {                              
                while( $row = $sth->fetch() ) 
                {                         
                    $output= array('RAM'=>(int)$row['sizeRam']);
                }                     
            }               
        }
        catch (Exception $e){                
            $output=null;
        }   
    }
    
    function delete_resource($idGfs,$idDomain){
//        echo 1;
        $this->connectDB();        
        if($this->dbh == null){
            return CPAPIStatus::DBConnectFail;
        }
        else
        {            
            if(is_null($this->log_c))
                $this->log_c = new LogAction($this->dbh);
            $output_resource = null;
            $this->sql_list_resource_by_id($idGfs, $idDomain, $output_resource);
            if(is_null($output_resource)){
                $this->log_c->createLog(array('Type'=>3,'Level'=>3,'Code'=>'03300401','Riser'=>'admin'
                    ,'Message'=>"Failed to delete resource(Failed to list resource)"));                
                return CPAPIStatus::NotFound;
            }
            $output_vd = null;
            $this->sql_list_vd_by_idGfs_idDomain($idGfs,$idDomain,$output_vd);
            if(is_null($output_vd)){
                $this->log_c->createLog(array('Type'=>3,'Level'=>3,'Code'=>'03300402','Riser'=>'admin'
                    ,'Message'=>"Failed to delete resource(".$output_resource['Name'].")(Failed to list vd)"));
                return CPAPIStatus::NotFound;
            }
            $vd_c = new VDAction($this->dbh);            
            $seed_vds = array();
            foreach ($output_vd as $vd)
            {                
                if($vd_c->sql_check_seed($vd)){
                    $seed_vds[] = $vd;
                    continue;
                }
//                var_dump($seed_vds);
                if($vd_c->delete_vd(array('ID'=>$vd)) != CPAPIStatus::DeleteVDSuccess){
                    $this->log_c->createLog(array('Type'=>3,'Level'=>3,'Code'=>'03300403','Riser'=>'admin'
                    ,'Message'=>"Failed to delete resource(".$output_resource['Name'].")(Failed to delete vd)"));
                    return CPAPIStatus::DeleteResourceFail;
                }
            }       
            foreach ($seed_vds as $vd)
            {                
                if($vd_c->delete_vd(array('ID'=>$vd)) != CPAPIStatus::DeleteVDSuccess){
                    $this->log_c->createLog(array('Type'=>3,'Level'=>3,'Code'=>'03300403','Riser'=>'admin'
                    ,'Message'=>"Failed to delete resource(".$output_resource['Name'].")(Failed to delete vd)"));
                    return CPAPIStatus::DeleteResourceFail;
                }
            }    
            if($this->sql_delete_resource($idGfs, $idDomain)){
                $this->log_c->createLog(array('Type'=>3,'Level'=>1,'Code'=>'03100301','Riser'=>'admin'
                    ,'Message'=>"Delete resource(".$output_resource['Name'].") success"));
                return CPAPIStatus::DeleteResourceSuccess;
            }
            else{
                $this->log_c->createLog(array('Type'=>3,'Level'=>3,'Code'=>'03300404','Riser'=>'admin'
                    ,'Message'=>"Failed to delete resource(".$output_resource['Name'].")"));
                return CPAPIStatus::DeleteResourceFail;
            }
        }
    }
    
    function sql_list_resource_by_id($idGfs,$idDomain,&$output){
        $SQLList = <<<SQL
            SELECT * FROM tbTerritorySet WHERE idGfs=:idGfs AND idDomain = :idDomain
SQL;
        try        
        {  
            $sth = $this->dbh->prepare($SQLList);             
            $sth->bindValue(':idGfs', $idGfs, PDO::PARAM_INT);    
            $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT);                    
            if($sth->execute())
            {                              
                while( $row = $sth->fetch() ) 
                {                         
                    $output = array('Name'=>$row['nameTerritory']);
                }                     
            }               
        }
        catch (Exception $e){                
            $output=null;
        }   
    }
    
    function sql_delete_resource($idGfs,$idDomain){
        $result = false;
        $SQLDelete = <<<SQL
            DELETE FROM tbTerritorySet WHERE idDomain = :idDomain AND idGfs = :idGfs
SQL;
        try        
        {  
            $sth = $this->dbh->prepare($SQLDelete);                       
            $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT);                    
            $sth->bindValue(':idGfs', $idGfs, PDO::PARAM_INT);    
            if($sth->execute())
            {                                             
                $result = true;                  
            }               
        }
        catch (Exception $e){                            
        }   
        return $result;
    }
    
    function sql_list_vd_by_idGfs_idDomain($idGfs,$idDomain,&$output){
        $SQLList = <<<SQL
            SELECT idVD FROM tbVDImageBaseSet WHERE idGfs=:idGfs AND idDomain = :idDomain
SQL;
        try        
        {  
            $sth = $this->dbh->prepare($SQLList);             
            $sth->bindValue(':idGfs', $idGfs, PDO::PARAM_INT);    
            $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT);                    
            if($sth->execute())
            {                              
                $output=array();
                while( $row = $sth->fetch() ) 
                {                         
                    $output[] = $row['idVD'];
                }                     
            }               
        }
        catch (Exception $e){                
            $output=null;
        }   
    }
    
    function list_resource_of_cluster($input,&$output){
        $this->connectDB();        
        if($this->dbh == null){
            return CPAPIStatus::DBConnectFail;
        }
        else
        {
            $output = null;           
            $this->sql_list_resource_of_cluster($input['ClusterID'], $output);               
            if(is_null($output))
                return CPAPIStatus::ListClusterResourceFail;
            return CPAPIStatus::ListClusterResourceSuccess;
        }
    }
    
    function sql_list_resource_of_cluster($id,&$output){    
         $SQLList = <<<SQL
            SELECT t1.*,t3.nameDomain FROM tbTerritorySet as t1 
            INNER JOIN tbGfsLunSet as t2
            INNER JOIN tbDomainSet as t3
            ON t1.idGfs = t2.idGfs
            AND t1.idDomain = t3.idDomain
            WHERE t2.idCluster = :idCluster
SQL;
        try        
        {  
            $sth = $this->dbh->prepare($SQLList);                       
            $sth->bindValue(':idCluster', $id, PDO::PARAM_INT);                    
            if($sth->execute())
            {                              
                $output=array();
                while( $row = $sth->fetch() ) 
                {                         
                    $output[] = array("Name"=>$row['nameTerritory'],'DomainName'=>$row['nameDomain']
                        ,'Quota'=>(int)$row['quotaTerritory']
                            ,'GFSID'=>(int)$row['idGfs'],'DomainID'=>(int)$row['idDomain']
                        ,'CPU'=>(int)$row['hzCpu'],'RAM'=>(int)$row['sizeRam'],'Vswitch'=>(int)$row['idVSwitch']);
                }                     
            }               
        }
        catch (Exception $e){                
            $output=null;
        }   
    }
      
    function list_allocated_of_resource($input,&$output){
        $this->connectDB();        
        if($this->dbh == null){
            return CPAPIStatus::DBConnectFail;
        }
        $output_disk_size = null;
        $this->sql_list_disksize_by_idDomain_idGfs($input['DomainID'],$input['GFSID'], $output_disk_size);
        if(is_null($output_disk_size))
            return CPAPIStatus::NotFound;
        $output = array("AllocateDisk"=>$output_disk_size);
        return CPAPIStatus::ListAllocatedofResourceSuccess;
    }
    
    function list_resource_of_domain_polling($input,&$output,$input_cluster=null){    
        $this->connectDB();        
        if($this->dbh == null){
            return CPAPIStatus::DBConnectFail;
        }
        $output_gfs_id = null;
        $this->sql_list_gfsid_of_domain($input['DomainID'], $output_gfs_id);
        if(is_null($output_gfs_id))
            return CPAPIStatus::NotFound;          
        $cluster_c = new Cluster_Action($this->dbh);
        $output = array();
        foreach ($output_gfs_id as $key=>$value){
            $output_disk_size = null;
//            var_dump($value);
            $this->sql_list_disksize_by_idDomain_idGfs($input['DomainID'],$value, $output_disk_size);
            if(is_null($output_disk_size))
                return CPAPIStatus::NotFound;
//            var_dump($output_disk_size);
            if(is_null($input_cluster)){
                $output_id = null;
                $this->sql_get_idCluster_by_idGfs($value,$output_id);
                if(is_null($output_id))
                    return CPAPIStatus::NotFound; 
                $rtn = $cluster_c->list_cluster_info_no_state($output_id,$output_cluster);              
                if($rtn != CPAPIStatus::ListClusterInfoSuccess)
                    return CPAPIStatus::ListClusterResourcePollingFail; 
               
            }
            else{    
                $output_cluster = $input_cluster;
            }
            $output_info = null;
            $cluster_c->list_cluster_info_shell($output_cluster, $output_info);
            if(is_null($output_info))
                return CPAPIStatus::ListClusterResourcePollingFail; 
            $poweron_vds = array();
            foreach ($output_info as $info){
                foreach ($info['poweronVD'] as $poweron_vd){
                    $str_arr = explode('_', $poweron_vd);
                    if($str_arr[0] == $input['DomainID'])
                        $poweron_vds[] = $str_arr[1];
                }
            }            
            if(count($poweron_vds) != 0){
                $output_ram = null;
                $this->sql_list_ram_by_vds($poweron_vds, $output_ram);
                if(is_null($output_ram))
                    return CPAPIStatus::ListClusterResourcePollingFail; 
                $output[] = array('GFSID' => (int)$value
                        ,'AllocateDisk'=>(int)$output_disk_size
                        ,'UsedRAM'=>$output_ram,'PoweronVD'=>$poweron_vds);
            }
            else{
                $output[] = array('GFSID' => (int)$value
                        ,'AllocateDisk'=>(int)$output_disk_size,'UsedRAM'=>0
                    ,'PoweronVD'=>$poweron_vds);
            }            
        }    
        return CPAPIStatus::ListClusterResourcePollingSuccess;
//        var_dump($output);
    }
    
    function sql_list_gfsid_of_domain($id,&$output){
         $SQLList = <<<SQL
            SELECT idGfs FROM tbTerritorySet WHERE idDomain = :idDomain
SQL;
        try        
        {              
            $sth = $this->dbh->prepare($SQLList);                       
            $sth->bindValue(':idDomain', $id, PDO::PARAM_INT);                    
            if($sth->execute())
            {                              
                $output=array();
                while( $row = $sth->fetch() ) 
                {                         
                    $output[] = $row['idGfs'];
                }                     
            }               
        }
        catch (Exception $e){                
            $output=null;
        }   
    }
    
    function sql_list_ram_by_vds($vds,&$output){
        $in  = str_repeat('?,', count($vds) - 1) . '?';        
        $SQLList = "SELECT count(t1.idVD) as count,SUM(t2.sizeRam) as total_ram 
            FROM tbvdimagebaseset as t1
                INNER JOIN tbvdimagebaseinfoset as t2
                ON t1.idVD = t2.idVD
            WHERE t1.idVdNumber IN($in)";

        try        
        {  
            $sth = $this->dbh->prepare($SQLList);                         
            $sth->bindValue(':idDomain', $id, PDO::PARAM_INT);                    
            if($sth->execute($vds))
            {                            
                while( $row = $sth->fetch() ) 
                {                                 
                    $output = $row['total_ram'] + $row['count'] * 512;
                }                     
            }               
        }
        catch (Exception $e){                
            $output=null;
        }   
    }   
    
    function sql_list_disksize_by_idDomain_idGfs($idDomain,$idGfs,&$output){
        $SQLList = <<<SQL
            SELECT sizeVDisk FROM tbvdiskset WHERE idDomain = :idDomain AND idGfs = :idGfs
SQL;
        try        
        {  
            $sth = $this->dbh->prepare($SQLList);                         
            $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT);                    
            $sth->bindValue(':idGfs', $idGfs, PDO::PARAM_INT);
            if($sth->execute())
            {            
                $output = 0;
                while( $row = $sth->fetch() ) 
                {                         
//                    if(!array_key_exists($row['idGfs'], $output))
//                        $output[$row['idGfs']] = 0;
                    $output += $row['sizeVDisk'];
                }                     
            }               
        }
        catch (Exception $e){                
            $output=null;
        }   
    }
    
    function sql_get_idCluster_by_idGfs($id,&$output){
        $SQLGetVD = <<<SQL
            SELECT idCluster FROM tbGfsLunSet WHERE idGfs =:idGfs
SQL;
        try
        {                                        
            $sth = $this->dbh->prepare($SQLGetVD);
            $sth->bindValue(':idGfs', $id, PDO::PARAM_INT); 
            if($sth->execute())
            {                
                while( $row = $sth->fetch() ) 
                {
                    $output = array('ClusterID'=>(int)$row['idCluster']);                    
                }               
            }               
        }
        catch (Exception $e){                
        }         
    }
    
    function process_resource_error_code($code){  
        $this->baseOutputResponse($code);
        switch ($code){            
            case CPAPIStatus::CreateResourceFail:   
            case CPAPIStatus::ListResourceFail:
            case CPAPIStatus::DeleteResourceFail:
            case CPAPIStatus::ListClusterResourceFail:  
            case CPAPIStatus::ModifyResourceFail:
            case CPAPIStatus::ListAllocatedofResourceFail:
                http_response_code(400);
            case CPAPIStatus::ListClusterPollingFail:
                http_response_code(417);
                break;                       
        }        
    }
}

