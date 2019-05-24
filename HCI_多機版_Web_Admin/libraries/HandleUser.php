<?php
class UserAction extends BaseAPI
{ 
    private $cmd_vd_task_disk_create ="sudo /var/www/html/vdi_util.sh vd_task_disk_create ";
    private $cmd_vd_disk_delete = "sudo /var/www/html/vdi_util.sh vd_disk_delete ";
    private $cmdVDUserProfileCreate = CmdRoot."ip vd_userprofile_create ";
    private $cmdVDUserProfileDel = CmdRoot."ip vd_userprofile_del ";
    private $cmdVDUserProfileModify = CmdRoot."ip vd_userprofile_resize ";

    function  __construct()
    {           
    }

    function createUser($input,&$output,$bol_insert_duplicate_user_fail = true)
    {
        $log_code = '';
        // var_dump($input);        
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }              
        $output_domain = null;
        $domain_c = new DomainAction();
        $domain_c->sqlListDomainByID($input['DomainID'], $output_domain);
        $input['Username']=$input['Name'];
        $this->sqlSelectUserByNameDomainID($input['Name'],$input['DomainID'],$outputUser);
        if(!is_null($outputUser)){            
            $output['ID']=$outputUser['UserID'];
            if($bol_insert_duplicate_user_fail){
                if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300201','Riser'=>'admin','Message'=>"Failed to create user(Name:".$input['Name'].")(User's name is duplicated)"),$last_id)){
                    LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                }
            }
            // unset($output['UserID']);            
            return CPAPIStatus::Conflict;
        }
        $rtn = CPAPIStatus::CreateUserFail;        
        connectDB::$dbh->beginTransaction();       
        if(!isset($input['Description']))
            $input['Description'] = ''; 
        $result=$this->sqlInsertUserBase($input);
        // var_dump($result);
        if(!$result){
            $log_code = '04300202';
            goto fail;
        }        
        $output_user = null;
        $this->sqlSelectUserByNameDomainID($input['Name'],$input['DomainID'],$output_user);
        $input['UserID'] = $output_user['UserID'];
        // var_dump($output_user);
        if(is_null($output_user)){
            $log_code = '04300202';
            goto fail;
        }
        // echo 1;
        if(!$input['isAdmin']){            
            if(!$this->sqlInsertUser($output_user['UserID'])){
                $log_code = '04300203-2';     
                goto fail;            
            }
        }
        else{
            if(!$this->sqlInserttbAdmin($output_user['UserID'])){
                $log_code = '04300203-1';  
                goto fail;
            }
            // echo 2;
            // if(!mkdir('/mnt/tmpfs/session/'.$input['DomainID'].'/'.$output_user['UserID'])){
            //     goto fail;
            // }
            // echo 3;
        }
        if(isset($input['DiskSize']) && $input['DiskSize'] > 0){
            $input['State']=0;
            if(!$this->sqlInsertVdisk($input)){
                $log_code = '04300A06';
                goto fail;                
            }
            $output_id = null;    
            $this->sqlGetidVDisk($input,$output_id);            
            if(is_null($output_id)){
                $log_code = '04300A07';
                goto fail;   
            }
            // var_dump($output_id);
            $input['DiskID'] = $output_id;
            $input['Name'] = $input['DomainID'].'_'.$input['UserID'].'_'.$input['DiskID'].'_ud';
            if(!$this->sqlUpdateVdiskName($input)){
                $log_code = '04300A08';
                goto fail;            
            }
            if(!$this->sqlInserttbUserDisk($input)){
                $log_code = '04300A09';
                goto fail;            
            }
            $this->sqlListVolumeByVolumeID($input['VolumeID'],$outputVolume);
            $input['VolumeName'] = $outputVolume['VolumeName'];
            if($this->createUserProfileShell($input) != 0){
                $log_code = '04300A0A';
                goto fail;                
            }
            if(LogAction::createLog(array('Type'=>4,'Level'=>1,'Code'=>'04100901','Riser'=>'admin','Message'=>"Create user's(Name:".$input['Username'].") profile disk success"),$last_id)){
                LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
            }
        }            
        $output=array('ID'=>$output_user['UserID'],'Name'=>$input['Username']
                ,'isAdmin'=>$input['isAdmin'],"State"=>0);        
        $rtn = CPAPIStatus::CreateUserSuccess;       
        connectDB::$dbh->commit();
        if(LogAction::createLog(array('Type'=>4,'Level'=>1,'Code'=>'04100101','Riser'=>'admin','Message'=>"Create user Success(Name:".$input['Username'].")"),$last_id)){
            LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
        }
        goto end;
        fail:
            connectDB::$dbh->rollBack();   
            switch ($log_code){
                case '04300202':
                    if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300202','Riser'=>'admin'
                    ,'Message'=>"Failed to create user(Name:".$input['Username'].")(Step 1:Failed to create user)"),$last_id))
                    {
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }                    
                    break;
                case '04300203-1':
                    if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300203','Riser'=>'admin'
                    ,'Message'=>"Failed to create user(Name:".$input['Username'].")(Step 2:Failed to create admin)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
                case '04300203-2':
                    if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300203','Riser'=>'admin'
                    ,'Message'=>"Failed to create user(Name:".$input['Username'].")(Step 2:Failed to create user)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
                case '04300204':
                    if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300204','Riser'=>'admin'
                    ,'Message'=>"Failed to create admin(Name:".$input['Username'].")(Step 3:Failed to create user)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
                case '04300A06':
                    if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300A06','Riser'=>'admin','Message'=>"Failed to create user's(Name:".$input['Name'].") profile disk(Failed to insert vdisk's db)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
                case '04300A07':
                    if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300A07','Riser'=>'admin','Message'=>"Failed to create user's(Name:".$input['Name'].") profile disk(Failed to get vdisk's db)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
                case '04300A08':
                    if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300A08','Riser'=>'admin','Message'=>"Failed to create user's(Name:".$input['Name'].") profile disk(Failed to update vdisk's db)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
                case '04300A09':
                    if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300A09','Riser'=>'admin','Message'=>"Failed to create user's(Name:".$input['Name'].") profile disk(Failed to insert userdisk's db)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
                case '04300A0A':
                    if(LogAction::createLog(array('Type'=>4,'Level'=>1,'Code'=>'04300A0A','Riser'=>'admin','Message'=>"Failed to create user's(Name:".$input['Name'].") profile disk"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
            }                 
            goto end;
        end:
            return $rtn;                                           
    }               

    function modifyUser($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        if($this->sqlUpdateUserDescription($input)){
            return CPAPIStatus::ModifyUserSuccess;
        }else{
            return CPAPIStatus::ModifyUserFail;
        }
    } 
    
    function sqlInsertUserBase($input)
    {        
        // var_dump($input);
        $result = false;        
        $SQLInsert = "INSERT tbUserBaseSet (nameUser,password,idDomain,Description) Values (:nameUser,TO_BASE64(DES_ENCRYPT(:password,'".DES_Key."')),:idDomain,:Description)";
        try
        {       
            $password = '000000';                     
            if($input['Name'] == 'acrosystem'){
                $password='acro000000';
            }
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':nameUser', $input['Name'], PDO::PARAM_STR);            
            $sth->bindValue(':password', $password, PDO::PARAM_STR);            
            $sth->bindValue(':idDomain', $input['DomainID'], PDO::PARAM_INT);
            $sth->bindValue(':Description', $input['Description'], PDO::PARAM_STR);
            $result = $sth->execute();
        }
        catch (Exception $e){                
        }        
        return $result;
    }
    
    function sqlUpdateUserDescription($input)
    {        
        // var_dump($input);
        $result = false;
        $sqlUpdate = "UPDATE tbuserbaseset SET Description=:Description WHERE idUser=:idUser";
        try {
            // var_dump($SQLUpdate);          
            $sth = connectDB::$dbh->prepare($sqlUpdate);
            $sth->bindValue(':Description', $input['Description'], PDO::PARAM_STR);
            $sth->bindValue(':idUser', $input['UserID'], PDO::PARAM_INT);
            $result = $sth->execute();
        } catch (Exception $e) {
        }
        // var_dump($result);
        return $result;   
    }


    function sqlInserttbAdmin($idUser){        
        $result = false;        
        $SQLInsert = "INSERT tbadminset (idUser) Values (:idUser)";
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLInsert);                     
            $sth->bindValue(':idUser', $idUser, PDO::PARAM_INT);
            $result = $sth->execute();               
        }
        catch (Exception $e){                
        }        
        return $result;
    }
    
    function sqlSelectUserByNameDomainID($name,$id,&$output){       
        $SQLSelect = <<<SQL
                SELECT idUser FROM tbUserBaseSet                    
                    WHERE nameUser=:nameUser AND idDomain=:idDomain LIMIT 1
SQL;
        try
        {                                                    
            $sth = connectDB::$dbh->prepare($SQLSelect);        
            $sth->bindValue(':nameUser', $name, PDO::PARAM_STR);    
            $sth->bindValue(':idDomain', $id, PDO::PARAM_INT);           
            if($sth->execute())
            {                
                while( $row = $sth->fetch()) 
                {
                    $output = array('UserID'=>(int)$row['idUser']);
                }
            }                           
        }
        catch (Exception $e){                             
        }                  
    }            
    
    function sqlInsertUser($id){
        $result = false;        
        $SQLInsert = "INSERT tbuserset (idUser) Values (:idUser)";
        try
        {                            
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':idUser', $id, PDO::PARAM_INT);                       
            $result = $sth->execute();               
        }
        catch (Exception $e){                
        }        
        return $result;
    }
    
    function listUser($id,&$output){
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }             
        $output = null;        
        $this->sqlListUserByidDomain($id,$output);
        if(is_null($output))
            return CPAPIStatus::ListUserofDomainFail;
        else
            return CPAPIStatus::ListUserofDomainSuccess;        
    }
    
    function sqlListUserByidDomain($id,&$output){
        $SQLSelect = <<<SQL
            SELECT t1.*,t2.idUser as IDAdmin FROM tbUserBaseSet 
                as t1 left join tbadminset as t2 
                ON t1.idUser=t2.idUser 
                WHERE idDomain=:idDomain AND nameUser!='acrosystem';
SQL;
        try
        {                                                    
            $sth = ConnectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':idDomain', $id, PDO::PARAM_INT);           
            if($sth->execute())
            {                
                $output = array();
                while( $row = $sth->fetch() ) 
                {                                        
                    $isAdmin = true;
                    if(is_null($row['IDAdmin']))
                        $isAdmin = false;        
                    $description = '';
                    if(isset($row['Description']))
                        $description = $row['Description'];                                
                    $output[] = array('ID'=>(int)$row['idUser'],'Name'=>$row['nameUser']
                            ,'State'=>(int)$row['stateUser'],'isAdmin'=>$isAdmin
                            ,'Description'=>$description);
                }
            }                           
        }
        catch (Exception $e){                             
        }       
    }
    
    function deleteUser($input)
    {        
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }                  
        $output_user = null;
        $this->sqlListUserByIDAndidDomain($input['UserID'], $input['DomainID'],$output_user);
        // var_dump($output_user);
        if(is_null($output_user)){
            if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300401','Riser'=>'admin','Message'=>"Failed to delete user(User is not found)"),$last_id)){
                LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
            }        
            return CPAPIStatus::NotFound;
        }
        // echo 'action 0';
        $output_domain = null;
        $domain_c = new DomainAction();
        $domain_c->sqlListDomainByID($input['DomainID'], $output_domain);            
        $responsecode = $this->deleteUserProfileDisk($input);
        if($responsecode != CPAPIStatus::DeleteUserProfileSuccess){
            if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300406','Riser'=>'admin','Message'=>"Failed to delete user(Name:".$output_user['Name'].")(Failed to delete user's profile disk)"),$last_id)){
               LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
            }  
            return CPAPIStatus::DeleteUserFail; 
        }
        $output_vd = null;        
        if($input['vd_delete'] == 1){
            $vd_c = new VDAction($this->dbh);            
            $vd_c->listVDofUserNoInfo($input,$output_vd);
            // var_dump($output_vd);
            if(is_null($output_vd)){
                if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300402','Riser'=>'admin','Message'=>"Failed to delete user(Name:".$output_user['Name'].")(User's vds are not found)"),$last_id)){
                    LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                }  
                return CPAPIStatus::NotFound;                  
            }
            // echo 'action 1';
            foreach ($output_vd as $vd){
                $vd['ConnectIP'] = $input['ConnectIP'];
                if($vd_c->deleteVD($vd) != CPAPIStatus::DeleteVDSuccess){
                    if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300403','Riser'=>'admin','Message'=>"Failed to delete user(Name:".$output_user['Name'].")(Failed to delete vd(".$vd['Name']."))"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    } 
                    return CPAPIStatus::DeleteUserFail;
                }
            }
            // echo 'action 2';
        }
        else{                        
            if(!$this->sqlUpdateUserVDRelation($input['UserID'])){
                if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300404','Riser'=>'admin','Message'=>"Failed to delete user(Name:".$output_user['Name'].")(Failed to update vd's relation)"),$last_id)){
                    LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                } 
                return CPAPIStatus::DeleteUserFail;
            }
        }        
        if(!$this->sqlDeleteUser($input['UserID'])){
            if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300405','Riser'=>'admin','Message'=>"Failed to delete user(Name:".$output_user['Name'].")"),$last_id)){
                LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
            } 
            return CPAPIStatus::DeleteUserFail;       
        }
        if(LogAction::createLog(array('Type'=>4,'Level'=>1,'Code'=>'04100301','Riser'=>'admin','Message'=>"Delete user(Name:".$output_user['Name'].") Success"),$last_id)){
            LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
        } 
        return CPAPIStatus::DeleteUserSuccess;
    }
    
    function sqlDeleteUser($id){
        $result = false;
        $SQLDelete = <<<SQL
            DELETE FROM tbUserBaseSet WHERE idUser=:idUser
SQL;
        try
        {                                                    
            $sth = connectDB::$dbh->prepare($SQLDelete);                       
            $sth->bindValue(':idUser', $id, PDO::PARAM_INT);           
            if($sth->execute())
            {                
                $result = true;
            }                           
        }
        catch (Exception $e){                             
        }    
        return $result;
    }
    
    function sqlUpdateUserVDRelation($id){
        $result = false;
        $SQLUpdateOrg = <<<SQL
            UPDATE tbvdimageset
Org SET idUser=null WHERE idUser=:idUser
SQL;
        $SQLUpdateUser = <<<SQL
            UPDATE tbvdimageset
 SET idUser=null WHERE idUser=:idUser
SQL;
        try
        {                                                    
            $sth = connectDB::$dbh->prepare($SQLUpdateOrg);                       
            $sth->bindValue(':idUser', $id, PDO::PARAM_INT);           
            if($sth->execute())
            {             
                $sth = connectDB::$dbh->prepare($SQLUpdateUser);                       
                $sth->bindValue(':idUser', $id, PDO::PARAM_INT);           
                if($sth->execute())
                    $result = true;
            }                           
        }
        catch (Exception $e){                             
        }    
        return $result;
    }
    
    function listUserInfoByNameDomainName($name,$domainname,&$output){
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }     
        $output = null;
        $this->sqlListUserInfoByNameDomainName($name,$domainname,$output);         
    }
    
    function sqlListUserInfoByNameDomainName($name,$domainname,&$output){
        $SQLSelect = 
            "SELECT t1.idUser,t1.nameUser,t1.stateUser,t1.idDomain,DES_DECRYPT(FROM_BASE64(t1.password),'".DES_Key.
                "') as Password,t2.idUser as IDAdmin FROM tbUserBaseSet as t1 
                left join tbadminset as t2 ON t1.idUser=t2.idUser INNER JOIN tbDomainSet as t3 
                ON t1.idDomain=t3.idDomain WHERE t1.nameUser=:nameUser 
                AND t3.nameDomain=:nameDomain";
        try
        {                                                    
            $sth = connectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':nameUser', $name, PDO::PARAM_STR);
            $sth->bindValue(':nameDomain', $domainname, PDO::PARAM_STR);
            if($sth->execute())
            {                
                
                while( $row = $sth->fetch() ) 
                {
                    $isAdmin = true;
                    if(is_null($row['IDAdmin']))
                        $isAdmin = false;
                    $output = array('UserID'=>(int)$row['idUser'],'Name'=>$row['nameUser']
                            ,'DomainID'=>(int)$row['idDomain'],'isAdmin'=>$isAdmin
                            ,'Password'=>$row['Password'],'State'=>(int)$row['stateUser']);
                }
            }                           
        }
        catch (Exception $e){                             
        }     
    }
    
    function check_user_by_id_domainid($id,$domainid){
        $result = false;        
        $output = null;
        $this->sqlListUserByIDAndidDomain($id,$domainid,$output);
        if(!is_null($output))
            $result = true;
        return $result;
    }        
    
    function sqlListUserWithPWDByID($idUser,&$output){
        $SQLSelect = 
            "SELECT nameUser,idUser,stateUser,DES_DECRYPT(FROM_BASE64(password),'".DES_Key.
                "') as Password FROM tbUserBaseSet WHERE idUser=:idUser";
        try
        {                                                    
            $sth = connectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':idUser', $idUser, PDO::PARAM_INT);            
            if($sth->execute())
            {                
                
                while( $row = $sth->fetch() ) 
                {                    
                    $output = array('Name'=>$row['nameUser'],'UserID'=>(int)$row['idUser'],'Password'=>$row['Password']
                            ,'State'=>(int)$row['stateUser']);
                }
            }                           
        }
        catch (Exception $e){                             
        }     
    }

    function sqlListUserByIDAndidDomain($idUser,$idDomain,&$output){
        $SQLSelect = 
            "SELECT nameUser,idUser,stateUser,DES_DECRYPT(FROM_BASE64(password),'".DES_Key.
                "') as Password FROM tbUserBaseSet WHERE idUser=:idUser AND idDomain=:idDomain";
        try
        {                                                    
            $sth = connectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':idUser', $idUser, PDO::PARAM_INT);
            $sth->bindValue(':idDomain', $idDomain, PDO::PARAM_INT);
            if($sth->execute())
            {                
                
                while( $row = $sth->fetch() ) 
                {                    
                    $output = array('Name'=>$row['nameUser'],'UserID'=>(int)$row['idUser'],'Password'=>$row['Password']
                            ,'State'=>(int)$row['stateUser']);
                }
            }                           
        }
        catch (Exception $e){                             
        }     
    }
    
    function modifyUserPasswordForRedirect($input){
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }  
        // $name_arr = explode('@',$input['AccountName']);
        $name = $input['AccountName'];
        // $domainname = $name_arr[1];        
        $output = null;    
        $domainC = new DomainAction();
        if(!$domainC->listDefaultDomain($outputDomain)){
            http_response_code (404);
            exit();
        }           
        $this->sqlListUserInfoByNameDomainName($name,$outputDomain['DomainName'],$output); 
        if(is_null($output)){
            http_response_code (404);
            exit();
        }
        if(strcmp($input['OldPassword'],$output['Password']) != 0){
            http_response_code (401);
            exit();
        }       
        if(strcmp($input['NewPassword'],$output['Password'])==0){
            return CPAPIStatus::ChangePWDSuccess;
        }
        connectDB::$dbh->beginTransaction();  
        if(!$this->sqlUpdateUserPassword($output['UserID'], $input['NewPassword'])){
            connectDB::rollBack();
            http_response_code (400);
            exit();
        }            
        $this->sqlGetUserVDs($output['UserID'],$outputVDs);
        $vdC = new VDAction();
        foreach ($outputVDs as $value) {
            // var_dump($value);
            // if(!$vdC->sqlUpdateVDRDPPWDTimeStamp(array('VDID'=>$value))){
            //     connectDB::$dbh->rollBack();
            //     http_response_code (400);
            //     exit();
            // }                
            if(!$vdC->sqlUpdateVDChangePWD(array('VDID'=>$value,'PWDChange'=>true))){
                connectDB::$dbh->rollBack();
                http_response_code (400);
                exit();
            }
            $vdC->modifyVDPasswdShell(array('ConnectIP'=>$input['ConnectIP'],'VDID'=>$value,'Password'=>$input['NewPassword']));
        }
        connectDB::$dbh->commit(); 
        return CPAPIStatus::ChangePWDSuccess;        
    }
       

    function modifyUserPassword($input,&$logCode,$isReset=false){        
        $logCode = "";
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }              
        $output_user = null;
        $this->sqlListUserByIDAndidDomain($input['UserID'], $input['DomainID'],$output_user);
        if(is_null($output_user)){
            return CPAPIStatus::NotFound;
        }               
        $domain_c = new DomainAction();
        $domain_c->sqlListDomainByID($input['DomainID'], $output_domain);
        if(!$this->sqlUpdateUserPassword($input['UserID'], $input['Password'])){
            if($isReset)
                $logCode = '05301F02';            
            return CPAPIStatus::ChangePWDFail;
        }         
        if(isReset)
            $logCode = '05101D01';
        $this->sqlGetUserVDs($input['UserID'],$outputVDs);
        $vdC = new VDAction();
        foreach ($outputVDs as $value) {
            // var_dump($value);           
            if(strcmp($input['Password'],$output_user['Password'])!=0){ 
                if(!$vdC->sqlUpdateVDChangePWD(array('VDID'=>$value,'PWDChange'=>true))){                
                    return CPAPIStatus::ChangePWDFail;  
                }
            }
            $vdC->modifyVDPasswdShell(array('ConnectIP'=>$input['ConnectIP'],'VDID'=>$value,'Password'=>$input['Password']));
        }
        return CPAPIStatus::ChangePWDSuccess;        
    }
    
    function sqlGetUserVDs($idUser,&$output)
    {
        $result =  false;
        $sqlListOrg = "SELECT idVD FROM tbvdimageorgset WHERE idUser = :idUser";
        $sqlListUserVD = "SELECT idVD FROM tbvdimageset WHERE idUser = :idUser";
        try
        {                                     
            $sth = connectDB::$dbh->prepare($sqlListOrg);            
            $sth->bindValue(':idUser', $idUser, PDO::PARAM_INT); 
            if($sth->execute()){
                $output = array();
                while( $row = $sth->fetch() ){
                    $output[] = $row['idVD'];
                }                            
                $sth = connectDB::$dbh->prepare($sqlListUserVD);            
                $sth->bindValue(':idUser', $idUser, PDO::PARAM_INT); 
                if($sth->execute()){  
                    while( $row = $sth->fetch() ){
                        $output[] = $row['idVD'];
                    }
                }
            }
        }
        catch (Exception $e){                
        }    
        return $result;
    }


    function sqlUpdateUserPassword($idUser,$password){
        $result =  false;
        $SQLUpdate = "Update tbUserBaseSet SET password=TO_BASE64(DES_ENCRYPT(:password,'".DES_Key."')) WHERE idUser = :idUser";
        try
        {                                     
            $sth = connectDB::$dbh->prepare($SQLUpdate);
            $sth->bindValue(':password', $password, PDO::PARAM_STR); 
            $sth->bindValue(':idUser', $idUser, PDO::PARAM_INT); 
            if($sth->execute())
                $result = true;                   
        }
        catch (Exception $e){                
        }    
        return $result;
    }

    function sqlUpdateUserName($idUser,$nameUser){
        $result =  false;
        $SQLUpdate = "Update tbUserBaseSet SET nameUser=:nameUser WHERE idUser = :idUser";
        try
        {                                     
            $sth = connectDB::$dbh->prepare($SQLUpdate);
            $sth->bindValue(':nameUser', $nameUser, PDO::PARAM_STR); 
            $sth->bindValue(':idUser', $idUser, PDO::PARAM_INT); 
            if($sth->execute())
                $result = true;                   
        }
        catch (Exception $e){                
        }    
        return $result;
    }
    
    function sqlListUserDefaultVD($idUser,&$output){
        $SQLSelect = 
            "SELECT idDefaultVD FROM tbuserset WHERE idUser=:idUser";
        try
        {                                                    
            $sth = connectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':idUser', $idUser, PDO::PARAM_INT);            
            if($sth->execute())
            {                
                
                while( $row = $sth->fetch() ) 
                {                    
                    // var_dump($row);
                    $output = array('DefaultVD'=>$row['idDefaultVD']);
                }
            }                           
        }
        catch (Exception $e){                             
        }     
    }
    
    function disableUser($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }          
        $str_action = 'Enable';
        if((int)$input['Disable'] == 1)
            $str_action = 'Disable';
        $output_user = null;
        $this->sqlListUserByIDAndidDomain($input['UserID'], $input['DomainID'],$output_user);        
        if(is_null($output_user)){
            if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300601','Riser'=>'admin','Message'=>"Failed to $str_action user(User is not found)"),$last_id)){
                LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
            } 
            return CPAPIStatus::NotFound;        
        }
        $output_domain = null;
        $domain_c = new DomainAction();
        $domain_c->sqlListDomainByID($input['DomainID'], $output_domain);
        $rtn = $this->sqlDisableUser($input['UserID'],$input['Disable']);
        if($rtn){            
            if($input['Disable']){
                $vd_c = new VDAction();
                $responsecode = $vd_c->listVDofUser($input, $output);                
                if($responsecode != CPAPIStatus::ListVDofUserSuccess)
                    return CPAPIStatus::DisableUserFail;
                $name_arr = array();
                foreach ($output['Org'] as $vd){
                    $name_arr[] = $vd['ID'];
                }            
                foreach ($output['User'] as $vd){
                    $name_arr[] = $vd['ID'];
                }                            
                $vd_c->poweroffVD(array('DomainID'=>$input['DomainID'],'VDs'=>$name_arr));
            }           
            if(LogAction::createLog(array('Type'=>4,'Level'=>1,'Code'=>'04100501','Riser'=>'admin','Message'=>"$str_action user(Name:".$output_user['Name'].") success"),$last_id)){
                LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
            } 
            return CPAPIStatus::DisableUserSuccess;
        }            
        else{
            if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300602','Riser'=>'admin','Message'=>"Failed to $str_action user(Name:".$output_user['Name'].")"),$last_id)){
                LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
            } 
            return CPAPIStatus::DisableUserFail;
        }
    }
    
    function sqlDisableUser($idUser,$isdisable){
        $result = false;
        $SQLUpdate = 
            "Update tbUserBaseSet SET stateUser=:stateUser WHERE idUser=:idUser";
        try
        {                             
            if($isdisable)
                $state = 1;
            else
                $state = 0;            
            $sth = connectDB::$dbh->prepare($SQLUpdate);                       
            $sth->bindValue(':idUser', $idUser, PDO::PARAM_INT);            
            $sth->bindValue(':stateUser', $state, PDO::PARAM_INT);            
            if($sth->execute())
            {                
                $result = true;
            }                           
        }
        catch (Exception $e){                             
        }
        return $result;
    }
    
    function createUserProfileDisk($input)
    {
        $log_code = '';
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }            
        $this->sqlGetUserProfileDisk($input,$outputUserDisk);
        if(isset($outputUserDisk)){
            return CPAPIStatus::CreateUserProfileSuccess;
        }
        $vd_c = new VDAction();
        $output_user = null;
        $this->sqlListUserByIDAndidDomain($input['UserID'],$input['DomainID'], $output_user);         
        if(is_null($output_user)){
            if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300A01','Riser'=>'admin','Message'=>"Failed to create user's profile disk task(User is not found)"),$last_id)){
                LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
            }
            return CPAPIStatus::NotFound;
        }
        $output_domain = null;
        $domain_c = new DomainAction();
        $domain_c->sqlListDomainByID($input['DomainID'], $output_domain);
        $input['DomainName']  = $output_domain['Name'];
        $input['Username'] = $output_user['Name'];
        connectDB::$dbh->beginTransaction(); 
        $input['State']=0;
           // echo 1;
        if(!$this->sqlInsertVdisk($input)){
            $log_code = '04300A06';
            goto fail_del_vd;                
        }
        $output_id = null;    
        $this->sqlGetidVDisk($input,$output_id);            
        if(is_null($output_id)){
            $log_code = '04300A07';
            goto fail_del_vd;   
        }
        $input['DiskID'] = $output_id;
        // $input['Name'] = $input['DomainID'].'_'.$input['UserID'].'_'.$input['DiskID'].'_ud';
        $input['Name'] = $input['Username'].'_ud';
        if(!$this->sqlUpdateVdiskName($input)){
            $log_code = '04300A08';
            goto fail_del_vd;            
        }
        if(!$this->sqlInserttbUserDisk($input)){
            $log_code = '04300A09';
            goto fail_del_vd;            
        }
        $this->sqlListVolumeByVolumeID($input['VolumeID'],$outputVolume);
        $input['VolumeName'] = $outputVolume['VolumeName'];
        if($this->createUserProfileShell($input) != 0){
            $log_code = '04300A0A';
            goto fail_del_vd;                
        }
        connectDB::$dbh->commit();
        if(LogAction::createLog(array('Type'=>4,'Level'=>1,'Code'=>'04100901','Riser'=>'admin','Message'=>"Create user's(Name:".$output_user['Name'].") profile disk success"),$last_id)){
            LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
        }
        $rtn = CPAPIStatus::CreateUserProfileSuccess;
        goto end;       
        fail:
            $rtn = CPAPIStatus::CreateUserProfileFail;
            goto end;
        fail_del_vd:                
            connectDB::$dbh->rollBack();   
            switch ($log_code){
                case '04300A06':
                    if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300A06','Riser'=>'admin','Message'=>"Failed to create user's(Name:".$output_user['Name'].") profile disk(Failed to insert vdisk's db)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
                case '04300A07':
                    if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300A07','Riser'=>'admin','Message'=>"Failed to create user's(Name:".$output_user['Name'].") profile disk(Failed to get vdisk's db)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
                case '04300A08':
                    if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300A08','Riser'=>'admin','Message'=>"Failed to create user's(Name:".$output_user['Name'].") profile disk(Failed to update vdisk's db)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
                case '04300A09':
                    if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300A09','Riser'=>'admin','Message'=>"Failed to create user's(Name:".$output_user['Name'].") profile disk(Failed to insert userdisk's db)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
                case '04300A0A':
                    if(LogAction::createLog(array('Type'=>4,'Level'=>1,'Code'=>'04300A0A','Riser'=>'admin','Message'=>"Failed to create user's(Name:".$output_user['Name'].") profile disk"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
            }
            $rtn = CPAPIStatus::CreateUserProfileFail;
            goto end;
        end:
            // var_dump($rtn);
            return $rtn;
    }        
    
    function sqlInsertVdisk(&$input,$isUP=false)
    {
        $result = false;
        // var_dump($input);
        if(is_null($input['State']))
            $state = 0;
        $SQLInsert = <<<SQL
            INSERT tbVDiskSet (idVD,idCephPool,idDomain,nameVDisk,sizeVDisk,stateVDisk,configVDisk,idVolume,limitSSLyer) 
                Values (:idVD,:idCephPool,:idDomain,:nameVDisk,:sizeVDisk,:stateVDisk,:configVDisk,:idVolume,:limitSSLyer)
SQL;
        try
        {                              
            $tmp_name = uniqid();
            if(!$isUP)
                $input['Name'] = $tmp_name;            
            $configVDisk = json_encode(array('DiskType'=>$input['DiskType'],'DiskCache'=>0));
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':idVD', $input['ID'], PDO::PARAM_STR);
            $sth->bindValue(':idCephPool', $input['CephID'], PDO::PARAM_INT); 
            $sth->bindValue(':idDomain', $input['DomainID'], PDO::PARAM_INT); 
            $sth->bindValue(':nameVDisk', $input['Name'], PDO::PARAM_STR); 
            $sth->bindValue(':sizeVDisk', $input['DiskSize'], PDO::PARAM_INT);
            $sth->bindValue(':stateVDisk', $input['State'], PDO::PARAM_INT);
            $sth->bindValue(':configVDisk', $configVDisk, PDO::PARAM_STR);
            $sth->bindValue(':idVolume', $input['VolumeID'], PDO::PARAM_STR);
            if(!isset($input['SSLayerMax']))
                $input['SSLayerMax'] = $this->ssLayerDefault;
            $sth->bindValue(':limitSSLyer', $input['SSLayerMax'], PDO::PARAM_INT);
            if($sth->execute())
            {                          
                if($sth->rowCount() == 1)
                    $result = true;
            }               
        }
        catch (Exception $e){                
        }         
        // var_dump($result);
        return $result;
    }
    
    function sqlGetidVDisk($input,&$output){
        $SQLSelect = 
            "SELECT idVDisk FROM tbVDiskSet WHERE idDomain=:idDomain AND nameVDisk=:nameVDisk";
        try
        {                                                    
            $sth = connectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':idDomain', $input['DomainID'], PDO::PARAM_INT);            
            $sth->bindValue(':nameVDisk', $input['Name'], PDO::PARAM_STR); 
            if($sth->execute())
            {                                
                while( $row = $sth->fetch() ) 
                {                    
                    $output = (int)$row['idVDisk'];
                }
            }                           
        }
        catch (Exception $e){                             
        }     
    }
    
    function sqlUpdateVdiskName($input){        
        $result = false;
        $SQLUpdate = 
            "Update tbVDiskSet SET nameVDisk=:nameVDisk WHERE idVDisk=:idVDisk";
        try
        {                                         
            $sth = connectDB::$dbh->prepare($SQLUpdate);                       
            $sth->bindValue(':nameVDisk', $input['Name'], PDO::PARAM_STR);            
            $sth->bindValue(':idVDisk', $input['DiskID'], PDO::PARAM_INT);            
            if($sth->execute())
            {                
                $result = true;
            }                           
        }
        catch (Exception $e){                             
        }
        return $result;
    }
    
    function sqlInserttbUserDisk($input){
        $result = false;
        $SQLInsert = <<<SQL
            INSERT tbuserdiskset (idUser,idVDisk) Values (:idUser,:idVDisk)
SQL;
        try
        {                                          
            $sth = connectDB::$dbh->prepare($SQLInsert);            
            $sth->bindValue(':idUser', $input['UserID'], PDO::PARAM_INT); 
            $sth->bindValue(':idVDisk', $input['DiskID'], PDO::PARAM_INT);             
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
    
    function createUserProfileShell($input){
        $rtn = -2;        
        $cmd = $this->cmdVDUserProfileCreate;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);        
        $cmd .= '"'.$input['Name'].'" ';
        $cmd .= $input['DiskSize'].' ';   
        $cmd .= $input['VolumeName'].' ';   
        // var_dump($cmd);     
        exec($cmd,$output,$rtn); 
        // var_dump($rtn);
        return $rtn;
    }
    
    function deleteUserProfileDisk($input){
        // var_dump($input);
        $log_code = '';
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        $output_user = null;       
        $this->sqlListUserByIDAndidDomain($input['UserID'],$input['DomainID'], $output_user);       
        if(is_null($output_user)){
            if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300C01','Riser'=>'admin','Message'=>"Failed to delete user's profile disk(User is not found)"),$last_id)){
                LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
            }
            return CPAPIStatus::NotFound;      
        }
        $output_domain = null;
        $domain_c = new DomainAction();
        $domain_c->sqlListDomainByID($input['DomainID'], $output_domain);
        $output_disk = null;
        $this->sqlGetUserProfileDisk($input, $output_disk); 
        // var_dump($output_disk); 
        if(is_null($output_disk)){            
            return CPAPIStatus::DeleteUserProfileSuccess;   
        }
        if($output_disk['State'] == 30){
            if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300C05','Riser'=>'admin','Message'=>"Failed to Delete user's(Name:".$output_user['Name'].") profile disk(Disk in task)"),$last_id)){
                LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
            }
            return CPAPIStatus::DeleteUserProfileFail;   
        }        
        $input['DiskName'] = $output_disk['DiskName'];
        $input['DiskID'] = $output_disk['DiskID'];
        $vd_c = new VDAction();                               
        connectDB::$dbh->beginTransaction(); 
        // var_dump($input);
        if(!$this->sqlDeleteUserProfileDisk($input['DiskID'])){
            $log_code = '04300C03';
            goto fail_del_vd;            
        }
        if($this->deleteUserProfileDiskShell($input) != 0){
            $log_code = '04300C04';
            goto fail_del_vd;            
        }
        connectDB::$dbh->commit();
        if(LogAction::createLog(array('Type'=>4,'Level'=>1,'Code'=>'04100B01','Riser'=>'admin','Message'=>"Delete user's(Name:".$output_user['Name'].") profile disk Success"),$last_id)){
            LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
        }
        $rtn = CPAPIStatus::DeleteUserProfileSuccess;
        goto end;       
        fail:
            $rtn = CPAPIStatus::DeleteUserProfileFail;
            goto end;
        fail_del_vd:                
            connectDB::$dbh->rollBack();  
            switch ($log_code){
                case '04300C03':
                    if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300C03','Riser'=>'admin','Message'=>"Failed to delete user's(Name:".$output_user['Name'].") profile disk(Failed to delete db)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
                case '04300C04':
                    if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04300C04','Riser'=>'admin','Message'=>"Failed to delete user's(Name:".$output_user['Name'].") profile disk"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
            }
            $rtn = CPAPIStatus::DeleteUserProfileFail;
            goto end;
        end:
            return $rtn;
//            var_dump($rtn);
    }
    
    function sqlDeleteUserProfileDisk($idVDisk){
        $result = false;
        $SQLUpdateSnapshotUpperLayer = "UPDATE tbvdisksnapshotset SET idUpperLayer=NULL WHERE idSourceDisk = :idSourceDisk";
        $SQLDeleteUserDiskRelation = <<<SQL
            DELETE FROM tbuserdiskset WHERE idVDisk=:idVDisk
SQL;
        $SQLDeleteVDisk = <<<SQL
            DELETE FROM tbVDiskSet WHERE idVDisk=:idVDisk
SQL;
        try
        {             
            $sth = connectDB::$dbh->prepare($SQLUpdateSnapshotUpperLayer);
            $sth->bindValue(':idSourceDisk', $idVDisk, PDO::PARAM_INT); 
            if($sth->execute())
            {                                             
                $sth = connectDB::$dbh->prepare($SQLDeleteUserDiskRelation);                       
                $sth->bindValue(':idVDisk', $idVDisk, PDO::PARAM_INT);           
                if($sth->execute())
                {                
                    if($sth->rowCount() == 1){
                        
                        $sth = connectDB::$dbh->prepare($SQLDeleteVDisk);                       
                        $sth->bindValue(':idVDisk', $idVDisk, PDO::PARAM_INT);           
                        if($sth->execute())
                        {            
                            if($sth->rowCount() == 1)
                                $result = true;                                
                        }                              
                    }
                }    
            }                       
        }
        catch (Exception $e){                             
        }    
        return $result;
    }
    
    function sqlGetUserProfileDisk($input,&$output){
        $SQLSelect = 
            "SELECT t1.idVDisk,t2.nameVDisk,t2.stateVDisk,t2.sizeVDisk,t2.configVDisk,t2.limitSSLyer FROM tbuserdiskset as t1 INNER JOIN tbVDiskSet as t2 "
                . "ON t1.idVDisk=t2.idVDisk WHERE t1.idUser=:idUser";
        try
        {                                                    
            $sth = connectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':idUser', $input['UserID'], PDO::PARAM_INT);                         
            if($sth->execute())
            {                                
                while( $row = $sth->fetch() ) 
                {                   
                    $configVDisk = $row['configVDisk'];
                    $DiskType = 0;
                    $DiskCache = 0;
                    $configVDisk = json_decode($configVDisk,true);
                    if(isset($configVDisk['DiskType'])){
                        $DiskType = $configVDisk['DiskType'];
                        $DiskCache = $configVDisk['DiskCache'];
                    }               
                    $output = array("DiskID"=>$row['idVDisk'],"DiskName"=>$row['nameVDisk'],'State'=>(int)$row['stateVDisk'],"DiskSize"=>$row['sizeVDisk'],'DiskType'=>$DiskType,'DiskCache'=>$DiskCache,'SSLayerMax'=>$row['limitSSLyer']);
                }
            }                           
        }
        catch (Exception $e){                             
        }     
    }
    
    function deleteUserProfileDiskShell($input){
        $rtn = -2;
        $cmd = $this->cmdVDUserProfileDel;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);     
        $cmd .= $input['DiskName'].' ';      
        // var_dump($cmd);
        exec($cmd,$output,$rtn);
        // var_dump($rtn);
        return $rtn;
    }

    function modifyUserProfileDisk($input)
    {
        $logCode = '';
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        $outputUser = null;
        $this->sqlListUserByIDAndidDomain($input['UserID'],$input['DomainID'], $outputUser);
        if(is_null($outputUser)){
            return CPAPIStatus::ModifyUserProfileSuccess;      
        }        
        $outputDisk = null;
        $this->sqlGetUserProfileDisk($input, $outputDisk); 
        // var_dump($input);
        // var_dump($outputDisk);
        $input['DiskType'] = $outputDisk['DiskType'];
        if($input['DiskType'] == $outputDisk['DiskType'] && $input['DiskSize'] == $outputDisk['DiskSize'] && $input['SSLayerMax'] == $outputDisk['SSLayerMax']){
            return CPAPIStatus::ModifyUserProfileSuccess;
        }
        $diskType = $this->transformDiskType($input['DiskType']);
        $diskStr = '(Disk Type:'.$diskType.',Disk Size:'.$input['DiskSize'].')';        
        if(is_null($outputDisk)){
            return CPAPIStatus::ModifyUserProfileSuccess;   
        }
        if($outputDisk['State'] != 0){
            if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04301501','Riser'=>'admin','Message'=>"Failed to modify user's(Name:".$outputUser['Name'].") profile disk(Disk in task)"),$last_id)){
                LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
            }
            return CPAPIStatus::ModifyUserProfileFail;   
        }
        $input['DiskName'] = $outputDisk['DiskName'];
        $input['DiskID'] = $outputDisk['DiskID'];
        $input['Size'] = $input['DiskSize'];
        $input['DiskCache'] = 0;
        $input['State'] = 0;
        $vdC = new VDAction();                               
        connectDB::$dbh->beginTransaction(); 
        if(!$vdC->sqlUpdateVDDisk($input)){
            $logCode = '04301502';
            goto failRollBack;
        }
        $errCode = $this->modifyUserProfileShell($input);
        if($errCode != 0){
            $logCode = '04301503';
            goto failRollBack;
        }           
        connectDB::$dbh->commit();        
        if(LogAction::createLog(array('Type'=>4,'Level'=>1,'Code'=>'04100B01','Riser'=>'admin','Message'=>"Modify user's(Name:".$outputUser['Name'].") profile disk$diskStr Success"),$last_id)){
            LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
        }
        $rtn = CPAPIStatus::ModifyUserProfileSuccess;
        goto end;       
        fail:
            $rtn = CPAPIStatus::ModifyUserProfileFail;
            goto end;
        failRollBack:                
            connectDB::$dbh->rollBack();  
            switch ($logCode){
                case '04301502':
                    if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04301502','Riser'=>'admin','Message'=>"Failed to modify user's(Name:".$outputUser['Name'].") profile disk(Failed to update db)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
                case '04301503':
                    $errCode = $this->changeModifyReturnCode($errCode);
                    if(LogAction::createLog(array('Type'=>4,'Level'=>3,'Code'=>'04301503','Riser'=>'admin','Message'=>"Failed to modify user's(Name:".$outputUser['Name'].") profile disk($errCode)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    break;
            }
            $rtn = CPAPIStatus::ModifyUserProfileFail;
            goto end;
        end:
            return $rtn;
    }

    function modifyUserProfileShell($input)
    {
        $rtn = -99;
        $cmd = $this->cmdVDUserProfileModify;
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);     
        $cmd .= $input['DiskName'].' ';    
        $cmd .= $input['DiskSize'].' ';  
        // var_dump($cmd);
        exec($cmd,$output,$rtn);
        // var_dump($rtn);
        return $rtn;
    }
    
    function listUserInfo($input,&$output){
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        $output_user = null;
        $this->sqlListUserByIDAndidDomain($input['UserID'],$input['DomainID'], $output_user);
        if(is_null($output_user))
            return CPAPIStatus::NotFound;     
        $output_user_disk = null;
        $this->sqlListUserProfileDiskDetail($input, $output_user_disk);
        $output = array("ProfileDisk"=>$output_user_disk);
        return CPAPIStatus::ListUserInfoSuccess;
    }
    
    function sqlListUserProfileDiskDetail($input,&$output)
    {
        $SQLList = <<<SQL
            SELECT t1.idVDisk,t2.nameVDisk,t2.stateVDisk,t2.sizeVDisk,t2.configVDisk,t2.idVolume,t2.limitSSLyer,t4.nameVD,t4.idVD 
                FROM tbuserdiskset as t1
                INNER JOIN tbVDiskSet as t2 
                ON t1.idVDisk=t2.idVDisk
                LEFT JOIN tbVDImageBaseSet as t4
                ON t2.idVD=t4.idVD
                WHERE t1.idUser=:idUser
SQL;
        try
        {                                                    
            $sth = connectDB::$dbh->prepare($SQLList);                       
            $sth->bindValue(':idUser', $input['UserID'], PDO::PARAM_INT);
            if($sth->execute())
            {                                
                while( $row = $sth->fetch() ) 
                {                    
                    $diskType = 0;
                    $diskCache = 0;
                    $configVDisk = json_decode($row['configVDisk'],true);
                    if(isset($configVDisk['DiskType'])){
                        $diskType = $configVDisk['DiskType'];
                        $diskCache = $configVDisk['DiskCache'];
                    }                 
                    $output = array("ID"=>(int)$row['idVDisk']
                            ,"VolumeID"=>$row['idVolume']
                            ,"DiskName"=>$row['nameVDisk']
                            ,"DiskSize"=>(int)$row['sizeVDisk']
                            ,"VDName"=>$row['nameVD'],"VDID"=>$row['idVD']
                            ,"State"=>(int)$row['stateVDisk'],'DiskType'=>$diskType,
                            'DiskCache'=>$diskCache,'SSLayerMax'=>$row['limitSSLyer']);
                }
            }                           
        }
        catch (Exception $e){                             
        }     
    }

    function sqlListUserByID($idUser,&$output)
    {
        $SQLSelect = 
            "SELECT * FROM tbuserbaseset WHERE idUser=:idUser";
        try
        {                                                    
            $sth = connectDB::$dbh->prepare($SQLSelect);                       
            $sth->bindValue(':idUser', $idUser, PDO::PARAM_INT);                         
            if($sth->execute())
            {                                
                while( $row = $sth->fetch() ) 
                {                                        
                    $output = array("Username"=>$row['nameUser']);
                }
            }                           
        }
        catch (Exception $e){                             
        }     
    }
    
    function process_user_error_code($code){  
        $this->baseOutputResponse($code);
        switch ($code){            
            case CPAPIStatus::CreateUserFail:    
            case CPAPIStatus::ListUserofDomainFail:
            case CPAPIStatus::DeleteUserFail:
            case CPAPIStatus::ChangePWDFail:
            case CPAPIStatus::DisableUserFail:
            case CPAPIStatus::CreateUserProfileFail:
            case CPAPIStatus::DeleteUserProfileFail:
            case CPAPIStatus::ListUserInfoFail:
            case CPAPIStatus::ModifyUserProfileFail:
            case CPAPIStatus::ModifyUserFail:
                http_response_code(400);
                break;               
        }
        
    }
}


