<?php
class CephPoolAction extends BaseAPI
{
	function  __construct()
    {           
    }

    function list_default_one_ceph_and_server(&$output)
    {
        $output = null;
	   	if($this->sql_list_cephs($output_cephs)){                      
	   		$output['CephID'] = $output_cephs[0]['CephID'];
	   		if($this->sql_list_ceph_with_server_by_idCephPool($output['CephID'],$output_ceph_server)){   
	   			$output['NodeAddress']=$output_ceph_server['Nodes'][0]['NodeAddress'];
	   		}
	   		else
	   			$output = null;
	   	}	   		
	   	if(is_null($output))
	   		return false;
	   	return true;
    }

    function sql_insert_tbCephPool($input)
    {
    	$result = false;
        $SQLInsert = <<<SQL
               INSERT tbCephPoolSet (sizePool) Values (:sizePool)
SQL;
        try
        {                                
            $sth = connectDB::$dbh->prepare($SQLInsert);                        
            $sth->bindValue(':sizePool', $input['Size'], PDO::PARAM_INT); 
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

    function sql_insert_tbVDServer_with_idCephPool($input)
    {
    	$result = false;
        $SQLInsert = <<<SQL
               INSERT tbVDServerSet (nameNode,address,isOnline,idCephPool) Values (:nameNode,:address,:isOnline,:idCephPool)
SQL;
        try
        {                                
            $sth = connectDB::$dbh->prepare($SQLInsert);                        
            $sth->bindValue(':nameNode', $input['Name'], PDO::PARAM_STR); 
            $sth->bindValue(':address', $input['Address'], PDO::PARAM_STR); 
            $sth->bindValue(':isOnline', true, PDO::PARAM_Bool); 
            $sth->bindValue(':idCephPool', $input['CephID'], PDO::PARAM_INT); 
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

    function sql_list_cephs(&$output)
    {
    	$output = null;
        $SQLList = <<<SQL
            SELECT * FROM tbCephPoolSet;
SQL;
		try
        {                            
            $sth = connectDB::$dbh->prepare($SQLList);            
            if($sth->execute())
            {                
            	$output = array();
                while( $row = $sth->fetch() ) 
                {                	
                	$output[] = array('CephID'=>(int)$row['idCephPool'],'PoolSize'=>(int)$row['sizePool']);                	
                }
            }                  
        }
        catch (Exception $e){                
        }                 
        if(is_null($output))
            return false;
        return true;
    }

    function sql_list_ceph_with_server_by_idCephPool($id,&$output)
    {
    	$output = null;
        $SQLList = <<<SQL
            SELECT t1.*,t2.* FROM tbCephPoolSet t1
                INNER JOIN tbVDServerSet t2
                ON t1.idCephPool=t2.idCephPool
                WHERE t1.idCephPool =:idCephPool
SQL;
       
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLList);
            $sth->bindValue(':idCephPool', $id, PDO::PARAM_INT);            
            if($sth->execute())
            {                
                while( $row = $sth->fetch() ) 
                {                    
                    // var_dump($row);
                	if(is_null($output['CephID'])){
                		$output['CephID'] = (int)$row['idCephPool'];
                		$output['PoolSize'] = (int)$row['sizePool'];
                	}                	
                    $output['Nodes'][] = array('NodeID'=>$row['idVDServer'],'NodeName'=>$row['nameNode'],'NodeAddress'=>$row['address']);
                }
            }                  
        }
        catch (Exception $e){                
        }                
        if(is_null($output))
            return false;
        return true;
    }
}