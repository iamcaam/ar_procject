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
        $sqlList = "SELECT nameDomain,DES_DECRYPT(FROM_BASE64(configAd),'".DES_Key.
                "') as configAd FROM tbDomainSet WHERE idDomain=:idDomain";
        try        
        {  
            $sth = ConnectDB::$dbh->prepare($sqlList);   
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

