<?php
class DomainAction extends BaseAPI
{
    function  __construct()
    {            
    }
    
    function listDefaultDomain(&$output)
    {   
        $output = null;       
        if($this->sql_list_domain($outputDomains)){
            $output['DomainID'] = $outputDomains[0]['DomainID'];            
            $output['DomainName'] = $outputDomains[0]['DomainName'];
        }           
        if(is_null($output))
            return false;
        return true;
    }

    function list_cluster_of_domain($input,&$output)
    {
        $this->connectDB();        
        if($this->dbh == null){
            return CPAPIStatus::DBConnectFail;
        }
        else
        {
            $output = null;
        }
    }
            
    function list_domain(&$output)
    {
        $this->connectDB();        
        if($this->dbh == null){
            return CPAPIStatus::DBConnectFail;
        }
        else
        {
            $output = null;
            $this->sql_list_domain($output);
            if(is_null($output))
                return CPAPIStatus::ListDomainFail;
            else
                return CPAPIStatus::ListDomainSuccess;
        }
    }
    
    function sql_list_domain(&$output)
    {
        $output=null;
        $SQLList = <<<SQL
            SELECT * FROM tbDomainSet
SQL;
         try        
        {  
            $sth = connectDB::$dbh->prepare($SQLList);                           
            if($sth->execute())
            {                              
                $output = array();
                while( $row = $sth->fetch() ) 
                {                                             
                    $output[]=array("DomainID"=>(int)$row['idDomain'],'DomainName'=>$row['nameDomain']);
                }                                 
            }               
        }
        catch (Exception $e){                
            $output=null;
        }         
        if(is_null($output))
            return false;
        return true;
    }
    
    function sqlListDomainByID($idDomain,&$output){
        $output = null;
        $SQLList = <<<SQL
            SELECT * FROM tbDomainSet WHERE idDomain=:idDomain
SQL;
         try        
        {  
            $sth = ConnectDB::$dbh->prepare($SQLList);   
            $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT);                 
            if($sth->execute())
            {                                            
                while( $row = $sth->fetch() ) 
                {                         
                    $output=array('Name'=>$row['nameDomain'],'AD'=>$row['configAd']);
                }                                 
            }               
        }
        catch (Exception $e){                
            $output=null;
        }
        if($output == null)
            return false;
        return true;
    }
    
    function create_domain($input,&$output)
    {
        $this->connectDB();    
        $log_code;
        if($this->dbh == null){
            return CPAPIStatus::DBConnectFail;
        }
        else{
            if(is_null($this->log_c))
                $this->log_c = new LogAction($this->dbh);
            $ouput_domain=null;
            $this->sql_list_domain_by_name($input['Name'], $ouput_domain);
            if(!is_null($ouput_domain)){
                $this->log_c->createLog(array('Type'=>2,'Level'=>3,'Code'=>'02300201','Riser'=>'admin'
                    ,'Message'=>"Failed to create domain(".$input['Name'].")(Domain's name is duplicated)"));
                return CPAPIStatus::Conflict;
            }
            $result = CPAPIStatus::CreateDomainFail;
            $this->dbh->beginTransaction();
            if($this->sql_insert_domain($input))
            {
                $this->sql_list_domain_by_name($input['Name'], $output);
                if(!is_null($output)){      
                    $user_c = new UserAction($this->dbh);
                    if(!$user_c->sql_insert_user_base(array('Name'=>'admin','DomainID'=>$output['ID'])))
                        goto fail;
                    $ouput_user = null;
                    $user_c->sql_select_user_by_name_domainid('admin',$output['ID'],$output_user);
                    if(is_null($output_user))
                        goto fail;                    
                    if(!$user_c->sql_insert_tbAdmin($output_user['UserID']))
                        goto fail;   
//                    if(!mkdir('/mnt/tmpfs/session/'.$output['ID'].'/admin',0777,true))
//                        goto fail;
                    $this->log_c->createLog(array('Type'=>2,'Level'=>1,'Code'=>'02100101','Riser'=>'admin'
                    ,'Message'=>"Create domain(".$input['Name'].") success"));
                    $this->dbh->commit();                   
                    return CPAPIStatus::CreateDomainSuccess;
                }
                else
                {
                    $log_code = '02300202';                   
                    goto fail;
                }
            }
            else{
                $log_code = '02300202';                
                return CPAPIStatus::CreateDomainFail;
            }
        }
        fail:            
            $this->dbh->rollBack();
            switch ($log_code){
                case '02300202':
                    $this->log_c->createLog(array('Type'=>2,'Level'=>3,'Code'=>'02300202','Riser'=>'admin'
                    ,'Message'=>"Failed to create domain(".$input['Name'].")"));
                    break;
            }
            goto end;
        end: 
            return $result;
            
    }
    
    function sql_insert_domain($input){
        $result = false;
        $SQLInsert = <<<SQL
            INSERT tbDomainSet (nameDomain,quotaVD) Values (:nameDomain,0)
SQL;
        try        
        {  
            $sth = connectDB::$dbh->prepare($SQLInsert);
            $sth->bindValue(':nameDomain', $input['Name'], PDO::PARAM_STR);         
            $result = $sth->execute();                          
        }
        catch (Exception $e){                
            $output=null;
        }
        return $result;
    }       
    
    function sql_list_domain_by_name($name,&$output)
    {
        $SQLList = <<<SQL
            SELECT * FROM tbDomainSet WHERE nameDomain=:nameDomain
SQL;
        try        
        {  
            $sth = $this->dbh->prepare($SQLList);
            $sth->bindValue(':nameDomain', $name, PDO::PARAM_STR);                    
            if($sth->execute())
            {                              
                while( $row = $sth->fetch() ) 
                {                         
                    $output = array("ID"=>(int)$row['idDomain'],"Name"=>$row['nameDomain'],'AD'=>$row['configAd']);
                }                     
            }               
        }
        catch (Exception $e){                
            $output=null;
        }          
    }   
    
    function modify_domain($input){    
        // var_dump($input);    
        $this->connectDB();            
        if($this->dbh == null){
            return CPAPIStatus::DBConnectFail;
        }
        if(is_null($this->log_c)){
            $this->log_c = new LogAction($this->dbh);
        }
        $ouput_domain=null;
        $this->sqlListDomainByID($input['DomainID'], $ouput_domain);
        if(is_null($ouput_domain)){
            $this->log_c->createLog(array('Type'=>2,'Level'=>3,'Code'=>'02300801','Riser'=>'admin'
                ,'Message'=>"Failed to modify domain(Failed to list domain)"));
            return CPAPIStatus::ModifyDomainFail;
        }
        if(strcasecmp($output_domain['Name'], $input['Name']) != 0){
            $output_check = null;
            $this->sql_list_domain_by_name($input['Name'], $output_check);
            if(!is_null($output_check)){
                $this->log_c->createLog(array('Type'=>2,'Level'=>3,'Code'=>'02300802','Riser'=>'admin'
                    ,'Message'=>"Failed to modify domain(".$input['Name'].")(Domain's name is duplicated)"));
                return CPAPIStatus::ModifyDomainFail;
            }
        }
        if(!$this->sql_update_domain($input)){
            $this->log_c->createLog(array('Type'=>2,'Level'=>3,'Code'=>'02300803','Riser'=>'admin'
                    ,'Message'=>"Failed to modify domain(".$input['Name'].")"));
            return CPAPIStatus::ModifyDomainFail;
        }
        $this->log_c->createLog(array('Type'=>2,'Level'=>1,'Code'=>'02100701','Riser'=>'admin'
                    ,'Message'=>"Modify domain(".$input['Name'].") Success"));
        return CPAPIStatus::ModifyDomainSuccess;
    }

    function sql_update_domain($input){
        $result = false;
        $SQLUpdate = <<<SQL
            UPDATE tbDomainSet SET nameDomain=:nameDomain WHERE idDomain=:idDomain
SQL;
        try {            
            $sth = $this->dbh->prepare($SQLUpdate);
            $sth->bindValue(':nameDomain', $input['Name'], PDO::PARAM_STR);
            $sth->bindValue(':idDomain', $input['DomainID'], PDO::PARAM_INT);
            $result = $sth->execute();
        } catch (Exception $e) {
        }
        return $result;
    }

    function delete_domain($id){
        $this->connectDB();        
        if($this->dbh == null){
            return CPAPIStatus::DBConnectFail;
        }
        if(is_null($this->log_c))
            $this->log_c = new LogAction($this->dbh);
        $output_domain = null;
        $this->sqlListDomainByID($id, $output_domain);
        if(is_null($output_domain)){
            $this->log_c->createLog(array('Type'=>2,'Level'=>3,'Code'=>'02300401','Riser'=>'admin'
                    ,'Message'=>"Failed to delete domain(Failed to list domain's db)"));
            return CPAPIStatus::NotFound;
        }
        $schedule_c = new Schedule_Action($this->dbh,$this->log_c);
        $schedule_c->sql_delete_schedule_by_idDomain($id);
        $resource_c = new Resource_Action($this->dbh,$this->log_c);
        if($resource_c->list_resource_of_domain(array('DomainID'=>$id), $output) != CPAPIStatus::ListResourceSuccess){
            $this->log_c->createLog(array('Type'=>2,'Level'=>3,'Code'=>'02300402','Riser'=>'admin'
                    ,'Message'=>"Failed to delete domain(".$output_domain['Name'].")(Failed to list reource's db)"));
            return CPAPIStatus::NotFound;        
        }
        foreach ($output as $resource){
            if($resource_c->delete_resource($resource['GFSID'], $resource['DomainID']) != CPAPIStatus::DeleteResourceSuccess){
                $this->log_c->createLog(array('Type'=>2,'Level'=>3,'Code'=>'02300403','Riser'=>'admin'
                    ,'Message'=>"Failed to delete domain(".$output_domain['Name'].")(Failed to delete reource)"));
                return CPAPIStatus::DeleteDomainFail;
            }
        }
        $user_c = new UserAction($this->dbh,$this->log_c);
        if($user_c->list_user_of_domain($id,$output) != CPAPIStatus::ListUserofDomainSuccess){            
            $this->log_c->createLog(array('Type'=>2,'Level'=>3,'Code'=>'02300404','Riser'=>'admin'
                    ,'Message'=>"Failed to delete domain(".$output_domain['Name'].")(Failed to list user's db)"));
            return CPAPIStatus::DeleteDomainFail;        
        }
        foreach ($output as $user){
            if(!$user_c->delete_user(array('UserID'=>$user['ID']
                    ,'DomainID'=>$user['DomainID'],'vd_delete'=>0))){
                $this->log_c->createLog(array('Type'=>2,'Level'=>3,'Code'=>'02300405','Riser'=>'admin'
                    ,'Message'=>"Failed to delete domain(".$output_domain['Name'].")(Failed to delete user)"));
                return CPAPIStatus::DeleteDomainFail;
            }
        }
        if(!$this->sql_delete_domain_log_relation($id)){
            $this->log_c->createLog(array('Type'=>2,'Level'=>3,'Code'=>'02300406','Riser'=>'admin'
                    ,'Message'=>"Failed to delete domain(".$output_domain['Name'].")"));
            return CPAPIStatus::DeleteDomainFail;
        }
        if(!$this->sql_delete_domain($id)){
            $this->log_c->createLog(array('Type'=>2,'Level'=>3,'Code'=>'02300406','Riser'=>'admin'
                    ,'Message'=>"Failed to delete domain(".$output_domain['Name'].")"));
            return CPAPIStatus::DeleteDomainFail;
        }
        $this->log_c->createLog(array('Type'=>2,'Level'=>1,'Code'=>'02100301','Riser'=>'admin'
                    ,'Message'=>"Delete domain(".$output_domain['Name'].") success"));
        shell_exec("rm -rf ".'/mnt/tmpfs/session/'.$id);
        return CPAPIStatus::DeleteDomainSuccess;
    }
    
    function sql_delete_domain_log_relation($id){
        $result = false;
        $SQLDelete = <<<SQL
            DELETE FROM tbdomainlogset WHERE idDomain = :idDomain
SQL;
        try
        {                                                    
            $sth = $this->dbh->prepare($SQLDelete);                       
            $sth->bindValue(':idDomain', $id, PDO::PARAM_INT);
            if($sth->execute())
            {                
                $result = true;
            }
        }
        catch (Exception $e){                             
        }    
        return $result;
    }
    
    function sql_delete_domain($id){
        $result = false;
        $SQLDelete = <<<SQL
            DELETE FROM tbDomainSet WHERE idDomain=:idDomain
SQL;
        try
        {                                                    
            $sth = $this->dbh->prepare($SQLDelete);                       
            $sth->bindValue(':idDomain', $id, PDO::PARAM_INT);
            if($sth->execute())
            {                
                $result = true;
            }
        }
        catch (Exception $e){                             
        }    
        return $result;
    }
    
    function process_domain_error_code($code){  
        $this->baseOutputResponse($code);
        switch ($code){            
            case CPAPIStatus::CreateDomainFail:            
            case CPAPIStatus::ListDomainFail:
            case CPAPIStatus::DeleteDomainFail:
            case CPAPIStatus::ModifyDomainFail:
                http_response_code(400);
                break;                       
        }
        
    }
}

