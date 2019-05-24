<?php
class LogAction extends BaseAPI
{
   
    protected $perPageCount = 1000;
   
    function  __construct()
    {            
    }
    
    public static function createLog($input,&$lastID=null){     
        if(is_null($input['SourceIP'])){
            if (!empty($_SERVER['HTTP_CLIENT_IP'])) {
                $input['SourceIP'] = $_SERVER['HTTP_CLIENT_IP'];
            } elseif (!empty($_SERVER['HTTP_X_FORWARDED_FOR'])) {
                $input['SourceIP'] = $_SERVER['HTTP_X_FORWARDED_FOR'];
            } else {
                $input['SourceIP'] = $_SERVER['REMOTE_ADDR'];
            } 
        }        
        if(is_null($input['SourceIP']))
            $input['SourceIP'] = 'HCI Host';        
        $result = self::sqlInsertLog($input,$lastID);
        return $result;
    }
    
    public static function sqlInsertDomainLogRelation($idLog,$idDomain){
        $result = false;
        $sqlInsertLog = "INSERT tbdomainlogset (idDomain,idLog) "
                . "Values (:idDomain,:idLog)";
        try
        {                                        
            $sth = connectDB::$dbh->prepare($sqlInsertLog);            
            $sth->bindValue(':idDomain', (int)$idDomain, PDO::PARAM_INT);            
            $sth->bindValue(':idLog', (int)$idLog, PDO::PARAM_INT);
            $result = $sth->execute();               
        }
        catch (Exception $e){                
        }        
        return $result;
    }
    
    static function sqlInsertLog($input,&$lastID=null){  
        $maxCount = 1000000;
        $deleteCount = 10000;      
        $result = false;
        $sqlSelectLogCount = "SELECT count(idLog) as count from tbLogBaseSet;";
        $sqlDeleteLog = "DELETE from tbLogBaseSet LIMIT ".$deleteCount;
        $sqlInsertLog = "INSERT tbLogBaseSet (typeLog,levelLog,sourceIP,hostIP,codeLog,message,riser,timeCreate) "
                . "Values (:typeLog,:levelLog,:sourceIP,:hostIP,:codeLog,:message,:riser,";
        if(isset($input['Time']))
            $sqlInsertLog .= ':time';
        else
            $sqlInsertLog .= 'NOW()';
        $sqlInsertLog .= ');';
        try
        {               
            $sth = connectDB::$dbh->prepare($sqlSelectLogCount);       
            if($sth->execute()){                                        
                while( $row = $sth->fetch() ) 
                {       
                    // var_dump($row);
                    if($row['count'] >= $maxCount){
                        $sth = connectDB::$dbh->prepare($sqlDeleteLog);  
                        $sth->execute();
                    }
                }
            }
            // var_dump($input);
            $sth = connectDB::$dbh->prepare($sqlInsertLog);            
            $sth->bindValue(':typeLog', (int)$input['Type'], PDO::PARAM_INT);            
            $sth->bindValue(':levelLog', (int)$input['Level'], PDO::PARAM_INT);
            $sth->bindValue(':sourceIP', $input['SourceIP'], PDO::PARAM_STR);
            $sth->bindValue(':hostIP', '', PDO::PARAM_STR);
            $sth->bindValue(':codeLog', $input['Code'], PDO::PARAM_STR);
            $sth->bindValue(':message', $input['Message'], PDO::PARAM_STR);
            $sth->bindValue(':riser', $input['Riser'], PDO::PARAM_STR);
            if(isset($input['Time']))
                $sth->bindValue(':time', $input['Time'], PDO::PARAM_STR);
            $sth->execute();    
            if($sth->rowCount() == 1){
                $result = true;
                $lastID = connectDB::$dbh->lastInsertId();                
            }
        }
        catch (Exception $e){                
        }        
        return $result;
    }
        
    function getSortField($inputField)
    {
        $field = 'timeCreate';      
        //0:levelLog,1:typeLog,2:timeCreate,3:sourceIP,4:riser,5:codeLog
        switch($inputField){
            case 0:
                $field = 'levelLog';
                break;
            case 1:
                $field = 'typeLog';
                break;
            case 2:
                $field = 'timeCreate';
                break;
            case 3:
                $field = 'sourceIP';
                break;
            case 4:
                $field = 'riser';
                break;
            case 5:
                $field = 'codeLog';
        }         
        return $field;
    }

    function getCondition($data,&$bintType,&$bindLevel,&$condition)
    {
        $condition = '';         
        $bintType = false;
        $bindLevel = false;
        if($data['Type'] != 0 || $data['Level'] != 0){
            $condition .= 'WHERE';
            if($data['Type'] != 0 && $data['Level'] != 0 ){
                $bintType = true;
                $bindLevel = true;
                $condition .= ' typeLog = :type AND levelLog = :level';
            }
            else{
                if($data['Type'] != 0){
                    $bintType = true;
                    $condition .= ' typeLog = :type';
                }
                else{
                    $bindLevel = true;
                    $condition .= ' levelLog = :level';
                }
            }
        }        
    }

    function getPageAndTotalPage($count,&$page,&$totalPage)
    {
        if($count > 0){
            $totalPage = (int)($count /$this->perPageCount);
            if($count%$this->perPageCount != 0)
                $totalPage +=1;
        }            
        if($page != 1 && $count < (($jsonData['Page']-1)*$this->perPageCount+1)){
            $page = 1;
        }            
    }

    function listLogWithSortAndType($jsonData,&$output){  
        if($this->connectDB()){        
            $gmt = $this->getTZ();
            $this->getCondition($jsonData,$bintType,$bindLevel,$condition);
            $field = $this->getSortField($jsonData['Field']);
            $ascordesc = 'ASC';
            //0:DESC,1:ASC
            if($jsonData['ASC'] == 0)
                $ascordesc = 'DESC';    
            $SQLQueryLogCount = "SELECT COUNT(idLog) FROM tblogbaseset $condition";
            $SQLQueryLogContent = "SELECT * FROM tblogbaseset $condition ORDER BY $field $ascordesc LIMIT :pageCount OFFSET :offset;";                
            try
            {
                $count = 0;
                $this->sqlSetTimeZone($gmt);  
                // var_dump($SQLQueryLogCount);
                $sth = connectDB::$dbh->prepare($SQLQueryLogCount);        
                if($bintType){
                    $sth->bindValue(':type', $jsonData['Type'],PDO::PARAM_INT);
                }
                if($bindLevel){
                    $sth->bindValue(':level', $jsonData['Level'],PDO::PARAM_INT);
                }            
                $sth->execute();                  
                while($row = $sth->fetch()){
                    $count = (int)$row['COUNT(idLog)'];
                }
                $page = $jsonData['Page'];
                $totalPage =1;
                $this->getPageAndTotalPage($count,$page,$totalPage);
                $sth = connectDB::$dbh->prepare($SQLQueryLogContent);            
                $pageCount = $this->perPageCount;
                $sth->bindValue(':pageCount', $pageCount,PDO::PARAM_INT);
                $sth->bindValue(':offset', ($jsonData['Page']-1)*$pageCount,PDO::PARAM_INT); 
                if($bintType){
                    $sth->bindValue(':type', $jsonData['Type'],PDO::PARAM_INT);
                }
                if($bindLevel){
                    $sth->bindValue(':level', $jsonData['Level'],PDO::PARAM_INT);
                }         
                $sth->execute(); 
                $displayCount = 0;
                $logs = array();
                while($row = $sth->fetch()){
                    $logs[] = array("Level"=>(int)$row['levelLog'],"Type"=>(int)$row['typeLog']
                            ,"Riser"=>$row['riser'],"Time"=>$row['timeCreate']
                            ,"SourceIP"=>$row['sourceIP'],"Message"=>$row['message']
                            ,"Code"=>$row['codeLog']);
                    $displayCount++;                
                }
                $output = array("TotalPage"=>$totalPage,"NowPage"=>$page,"TotalCount"=>$count,"DisplayCount"=>$displayCount,"Logs"=>$logs,"PageCount"=>$this->perPageCount);
            }
            catch (Exception $e){

            }
                    }
        else{
           $output = array("TotalPage"=>1,"NowPage"=>1,"TotalCount"=>0,"DisplayCount"=>0,"Logs"=>array(),"PageCount"=>$this->perPageCount);
        }
        $this->dbh = null;
        return CPAPIStatus::ListLogSuccess;
   }

    function list_log($input,&$output_log){
        $this->connectDB();        
        if(connectDB::$dbh == null)                    
            return CPAPIStatus::DBConnectFail;    
        $output_log = null;
        $this->sql_list_log($input, $output_log);
        if(is_null($output_log)){
            return CPAPIStatus::ListLogFail;
        }   
        else{
            return CPAPIStatus::ListLogSuccess;
        }
    }
    
     function sql_list_log($input,&$output){
        $exu_str = "sudo /var/www/html/date.sh get tz";
        $timezone = exec($exu_str,$outputarr);         
        $gmt = $this->TimeZoneArrMapping[$timezone] == null ? "+08:00" : $this->TimeZoneArrMapping[$timezone];            
        $sqlSetTimeZone = <<<SQL
                set time_zone=:timezone;
SQL;
        $condition = '';
        $bintType = false;
        $bindLevel = false;
        if($input['Type'] != -1 || $input['Level'] != -1){
            $condition .= 'WHERE';
            if($input['Type'] != -1 && $input['Level'] != -1 ){
                $bintType = true;
                $bindLevel = true;
                $condition .= ' typeLog = :type AND levelLog = :level';
            }
            else{
                if($input['Type'] != -1){
                    $bintType = true;
                    $condition .= ' typeLog = :type';
                }
                else{
                    $bindLevel = true;
                    $condition .= ' levelLog = :level';
                }
            }
        }
        $field = 'timeCreate';
        $ascordesc = 'DESC';
        $SQLQueryLogCount = "SELECT COUNT(idLog) FROM tbLogBaseSet $condition";
        $SQLQueryLogContent = "SELECT * FROM tbLogBaseSet $condition ORDER BY $field $ascordesc LIMIT :limit OFFSET :offset;";           
        try
        {
            $count = 0;
            $sth = connectDB::$dbh->prepare($sqlSetTimeZone);
            $sth->bindValue(':timezone', $gmt, PDO::PARAM_STR);
            $sth->execute();             
            $sth = connectDB::$dbh->prepare($SQLQueryLogCount);        
            if($bintType){
                $sth->bindValue(':type', $input['Type'],PDO::PARAM_INT);
            }
            if($bindLevel){
                $sth->bindValue(':level', $input['Level'],PDO::PARAM_INT);
            }            
            $sth->execute();           
            while($row = $sth->fetch()){
                $count = (int)$row['COUNT(idLog)'];
            }
            $page = $input['Page'];
            $pageCount = (int)$input['Count'];
            $totalPage =1;
            if($count > 0){
                $totalPage = (int)($count / $pageCount);
                if($count%$pageCount != 0)
                    $totalPage +=1;
            }           
            
            if($page != 1 && $count < (($input['Page']-1)*$pageCount+1)){
                $page = 1;
            }                               
            $offset = ($input['Page']-1)*$pageCount;                
            $sth = connectDB::$dbh->prepare($SQLQueryLogContent);                        
            $sth->bindValue(':limit', $pageCount,PDO::PARAM_INT);
            $sth->bindValue(':offset', $offset,PDO::PARAM_INT); 
            if($bintType){                
                $sth->bindValue(':type', $input['Type'],PDO::PARAM_INT);
            }
            if($bindLevel){                
                $sth->bindValue(':level', $input['Level'],PDO::PARAM_INT);
            }         
            $sth->execute(); 
            $display_count = 0;
            $logs = array();
            while($row = $sth->fetch()){           
                $logs[] = array("Level"=>(int)$row['levelLog'],"Type"=>(int)$row['typeLog']
                        ,"Riser"=>$row['riser'],"Time"=>$row['timeCreate']
                        ,"SourceIP"=>$row['sourceIP'],"Message"=>$row['message']
                        ,"Code"=>$row['codeLog']);
                $display_count++;                
            }          
            $output = array("TotalPage"=>$totalPage,"NowPage"=>$page,"TotalCount"=>$count,"DisplayCount"=>$display_count,"Logs"=>$logs,"PageCount"=>$pageCount);            
        }
        catch (Exception $e){
            $output = null;
        }
    }       
    
    function listLogByID($input,&$output_log){
        $this->connectDB();        
        if(connectDB::$dbh == null)                    
            return CPAPIStatus::DBConnectFail;    
        $output_log = null;
        $this->sqlListLogByID($input, $output_log);
        if(is_null($output_log)){
            return CPAPIStatus::ListLogFail;
        }   
        else{
            return CPAPIStatus::ListLogSuccess;
        }
    }
    
    function sqlListLogByID($input,&$output){
        $exu_str = "sudo /var/www/html/date.sh get tz";
        $timezone = exec($exu_str,$outputarr);         
        $gmt = $this->TimeZoneArrMapping[$timezone] == null ? "+08:00" : $this->TimeZoneArrMapping[$timezone];            
        $sqlSetTimeZone = <<<SQL
                set time_zone=:timezone;
SQL;
        if(!isset($input['count']) && !isset($input['readback'])){
            $output = null;
            return;
        }
        $condition = '';
        if(isset($input['id'])){
            if($input['id'] != -1){
                $condition = 'WHERE idLog<:id';
                if(isset($input['readback']) && $input['readback'] == 0){
                   $condition = 'WHERE idLog>:id';
                }
            }
        }
        $sqllist = "SELECT * FROM tbLogBaseSet $condition ORDER BY idLog DESC LIMIT :limit";
        try
        {          
            $sth = connectDB::$dbh->prepare($sqlSetTimeZone);
            $sth->bindValue(':timezone', $gmt, PDO::PARAM_STR);
            $sth->execute();                      
            $sth = connectDB::$dbh->prepare($sqllist);            
            $sth->bindValue(':limit', $input['count'], PDO::PARAM_INT);
            if(isset($input['id']) && $input['id'] != -1){                
                $sth->bindValue(':id', $input['id'], PDO::PARAM_INT);
            }                        
            if($sth->execute()){                
                $output = array();
                while($row = $sth->fetch()){   
                    $time=$row['timeCreate']=="0000-00-00 00:00:00"?"":$row['timeCreate'];            
                    $output[] = array("ID"=>(int)$row['idLog'],"Level"=>(int)$row['levelLog'],"Type"=>(int)$row['typeLog']
                        ,"Riser"=>$row['riser'],"Time"=>$time
                        ,"SourceIP"=>$row['sourceIP'],"Message"=>$row['message']
                        ,"Code"=>$row['codeLog']);               
                }
            }     
        }
        catch (Exception $e){
            $output = null;
        }
    }
    
    function list_domain_log_by_id($input,&$output_log){
        $this->connectDB();        
        if(connectDB::$dbh == null)                    
            return CPAPIStatus::DBConnectFail;    
        $output_log = null;
        $this->sql_list_domain_log_by_id($input, $output_log);
        if(is_null($output_log)){
            return CPAPIStatus::ListLogFail;
        }   
        else{
            return CPAPIStatus::ListLogSuccess;
        }
    }
    
    function sql_list_domain_log_by_id($input,&$output){
        $exu_str = "sudo /var/www/html/date.sh get tz";
        $timezone = exec($exu_str,$outputarr);         
        $gmt = $this->TimeZoneArrMapping[$timezone] == null ? "+08:00" : $this->TimeZoneArrMapping[$timezone];            
        $sqlSetTimeZone = <<<SQL
                set time_zone=:timezone;
SQL;
        if(!isset($input['count']) && !isset($input['readback'])){
            $output = null;
            return;
        }
        $condition = '';
        if(isset($input['id'])){
            if($input['id'] != -1){
                $condition = 'AND t1.idLog<:id';
                if(isset($input['readback']) && $input['readback'] == 0){
                   $condition = 'AND t1.idLog>:id';
                }
            }
        }        
        $sqllist = "SELECT t2.* FROM tbdomainlogset as t1 INNER JOIN tbLogBaseSet as t2 ON t1.idLog=t2.idLog "
                . "WHERE t1.idDomain=:idDomain $condition ORDER BY idLog DESC LIMIT :limit";
        try
        {          
            $sth = connectDB::$dbh->prepare($sqlSetTimeZone);
            $sth->bindValue(':timezone', $gmt, PDO::PARAM_STR);
            $sth->execute();     
            $sth = connectDB::$dbh->prepare($sqllist);            
            $sth->bindValue(':limit', $input['count'], PDO::PARAM_INT);
            $sth->bindValue(':idDomain', $input['DomainID'], PDO::PARAM_INT);
            if(isset($input['id']) && $input['id'] != -1){                
                $sth->bindValue(':id', $input['id'], PDO::PARAM_INT);
            }                        
            if($sth->execute()){                
                $output = array();
                while($row = $sth->fetch()){               
                    $output[] = array("ID"=>(int)$row['idLog'],"Level"=>(int)$row['levelLog'],"Type"=>(int)$row['typeLog']
                        ,"Riser"=>$row['riser'],"Time"=>$row['timeCreate']
                        ,"SourceIP"=>$row['sourceIP'],"Message"=>$row['message']
                        ,"Code"=>$row['codeLog']);               
                }
            }     
        }
        catch (Exception $e){
            $output = null;
        }
    }
    
    function processLogErrorCode($code){  
        $this->baseOutputResponse($code);
        switch ($code){                     
            case CPAPIStatus::ListLogFail:            
                http_response_code(400);
                break;                           
        }
        
    }
}