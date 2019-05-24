<?php
class ADAction extends BaseAPI
{

    function __construct()
    {
    }
    
    function mydapStart(&$mydap, $username, $password, $host, $port = 389)
    {
        // var_dump($password);
        // Connect to AD            
        $mydap = ldap_connect($host, $port);
        if (!$mydap) {
            return false;
        }
        ldap_set_option($mydap, LDAP_OPT_PROTOCOL_VERSION, 3);
        ldap_set_option($ldapconn, LDAP_OPT_REFERRALS, 0);     
        // var_dump(@ldap_bind($mydap, $username, $password));
        if (!@ldap_bind($mydap, $username, $password)) {            
            return false;
        }
        return true;
    }
    
    function listOUs($input,&$output)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        $domainC = new DomainAction();
        $outputDomain = null;
        $domainC->sqlListDomainByID($input['DomainID'], $outputDomain);
        if (is_null($outputDomain)) {
            return CPAPIStatus::ListADOUFail;
        }
        // var_dump($outputDomain);
        if (is_null($outputDomain['AD'])) {
            $output = array();
        } else {
            if($outputDomain['AD'] == ''){
                $output = array();
            }
            else{
                $output = base64_decode($outputDomain['AD']);                                
                $output = json_decode($output, true);
                // var_dump($output);
                if(isset($output['Enable']) && $output['Enable']){
                    $this->mydapListOUs($output,$outputOU);
                    // var_dump($outputOU);
                    if(isset($output['SyncOU']) && is_array($output['SyncOU'])){
                        foreach ($outputOU as &$value) {
                            foreach ($output['SyncOU'] as $syncvalue) {
                                if($value['DN'] == $syncvalue){
                                    $value['IsSync'] = true;
                                }
                            }
                        }
                    }
                    $output = $outputOU;
                }else{
                    $output = array();
                }                                
            }
        }
        return CPAPIStatus::ListADOUSuccess;
    }

    function mydapListOUs($input,&$outputOU)
    {
        $outputOU = array();
        $mydap = ldap_connect($input['IP'], 389);
        if (!$mydap) {
            return false;
        }
        ldap_set_option($mydap, LDAP_OPT_PROTOCOL_VERSION, 3);
        ldap_set_option($mydap, LDAP_OPT_REFERRALS, 0);
        $account = $input['Account'].'@'.$input['DomainName'];
        if (!@ldap_bind($mydap, $account, $input['Password'])) {            
            return false;
        }
        $domArr = explode('.', $input['DomainName']);        
        $baseDN = '';
        foreach ($domArr as $dom) {
            $baseDN .= "dc=$dom,";
        }
        if(strlen($baseDN) > 0)
            $baseDN = substr($baseDN, 0, count($baseDN)-2);
        // var_dump($baseDN);
        $justthese = array('cn','ou');
        $results=ldap_search($mydap, $baseDN, 'objectClass=OrganizationalUnit');        
        if($results == false)
            return false;
        $members = ldap_get_entries($mydap, $results);
        // var_dump($members['count']);
        if ($members["count"] > 0){
            array_shift($members);
            foreach ($members as $value) {
                $outputOU[] = array('IsSync'=>false,'OUName'=>$value['ou'][0],'DN'=>$value['dn']);
                // var_dump($value['cn'][0]);    
                // var_dump(bin2hex($value['objectguid'][0]));
                // var_dump(bin2hex($value['objectguid'][0]));
                // $output=$this->FormatGUID(bin2hex($value['objectguid'][0]));
                // var_dump($output);
            }
        }
    }

    function setOU($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        $domainC = new DomainAction();
        $outputDomain = null;
        $domainC->sqlListDomainByID($input['DomainID'], $outputDomain);
        if (is_null($outputDomain)) {
            return CPAPIStatus::SetADOUFail;
        }
        if (isset($outputDomain['AD'])) {
            $outputAD = base64_decode($outputDomain['AD']);                                
            $outputAD = json_decode($outputAD, true);
            // var_dump($outputAD);
            if(isset($outputAD['Enable']) && $outputAD['Enable']){
                $outputAD['SyncOU'] = $input['SyncOU'];
                $outputAD['DomainID'] = $input['DomainID'];
                // var_dump($outputAD);
                $this->sqlUpdateDomainAD($outputAD);               
            }
        }
        return CPAPIStatus::SetADOUSuccess;
    }

    function importUsers($input)
    {        
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }
        $domainC = new DomainAction();
        $outputDomain = null;
        $domainC->sqlListDomainByID($input['DomainID'], $outputDomain);
        if (is_null($outputDomain)) {
            return CPAPIStatus::ImportADUserFail;
        }
        if (isset($outputDomain['AD'])) {
            $outputAD = base64_decode($outputDomain['AD']);                                
            $outputAD = json_decode($outputAD, true);
            // var_dump($outputAD);
            if(isset($outputAD['Enable']) && $outputAD['Enable']){
                $outputAD['SyncOU'] = $input['SyncOU'];
                $outputAD['DomainID'] = $input['DomainID'];
                // var_dump($outputAD);
                $this->sqlUpdateDomainAD($outputAD);
                if(!$this->mydapListUsersOfOUs($outputAD,$outputUser,true)){
                    return CPAPIStatus::ImportADUserFail;
                }
            }
        }
        return CPAPIStatus::ImportADUserSuccess;
    }

    function mydapListUsersOfOUs($input,&$output,$isCreate=false)
    {        
        // var_dump($input);
        $output = array();
        $mydap = ldap_connect($input['IP'], 389);
        if (!$mydap) {
            return false;
        }
        ldap_set_option($mydap, LDAP_OPT_PROTOCOL_VERSION, 3);
        ldap_set_option($mydap, LDAP_OPT_REFERRALS, 0);
        $account = $input['Account'].'@'.$input['DomainName'];
        if (!@ldap_bind($mydap, $account, $input['Password'])) {            
            return false;
        }
        $justthese = array('cn',"description","samaccountname");
        $count = 0;
        $userC = new UserAction();
        foreach ($input['SyncOU'] as $value) {
            // var_dump($value);
            // $this->sqlInsertADOU($value,$outputID);
            // var_dump($outputID);
            // var_dump($value);            
            $results = ldap_search($mydap, $value, '(&(objectCategory=person)(objectClass=user))',$justthese);
            // $results = ldap_search($mydap, $value, '(&(objectCategory=person)(objectClass=user))');
            if($results == false)
                continue;
            $members = ldap_get_entries($mydap, $results);            
            // $count += $members['count'];
            if ($members["count"] > 0){
                array_shift($members);
                foreach ($members as $value1) {                           
                    $output[]=$value1['cn'][0];                              
                    // $desc = '';                    
                    if($value1['dn'] == 'CN='.$value1['cn'][0].",$value"){
                        // var_dump($value1['cn'][0]);
                        if($isCreate){
                            $userC->createUser(array('DomainID'=>$input['DomainID'],'Name'=>$value1['samaccountname'][0],'isAdmin'=>false,'Description'=>$value1['cn'][0]),$outputUser,false);
                            if(isset($outputUser)){                                
                                $userC->sqlUpdateUserDescription(array('UserID'=>$outputUser['ID'],'Description'=>$value1['cn'][0]));
                            //     if(isset($outputID)){
                            //         $this->sqlUpdateUserOU(array('OUID'=>$outputID,'UserID'=>$outputUser['ID']));
                            //     }
                            }
                        }
                    }
                }
            }
        }
        return true;
        // var_dump($count);
    }

    function sqlInsertADOU($OUDN,&$output)
    {
        $output = NULL;
        $exit = false;
        $sqlList = <<<SQL
            SELECT idSetting FROM tbAdOrganizationSettingSet WHERE nameDistinguished=:nameDistinguished
SQL;
        $sqlInsert = <<<SQL
            INSERT tbAdOrganizationSettingSet (nameOrganizationUnit,nameDistinguished) VALUES (:nameOrganizationUnit,:nameDistinguished)
SQL;
        try {
            $sth = connectDB::$dbh->prepare($sqlList);
            $sth->bindValue(':nameDistinguished', $OUDN, PDO::PARAM_STR);          
            if($sth->execute()){                                   
                while( $row = $sth->fetch() ) 
                {                              
                    $exit = true;   
                    $output = $row['idSetting'];
                }                        
                // var_dump($exit);    
                if(!$exit){
                    $sth = connectDB::$dbh->prepare($sqlInsert);
                    // var_dump($OUDN);
                    $sth->bindValue(':nameOrganizationUnit', $OUDN, PDO::PARAM_STR);
                    $sth->bindValue(':nameDistinguished', $OUDN, PDO::PARAM_STR);          
                    if($sth->execute()){  
                        if($sth->rowCount() == 1){
                            $output = connectDB::$dbh->lastInsertId();
                        }else{
                            $output = NULL;
                        }
                    }
                }
            }   
        }catch (Exception $e) {
            $output = NULL;
        }        
    }

    function sqlUpdateUserOU($input)
    {        
        // var_dump($input);
        $result = false;
        $sqlUpdate = "UPDATE tbuserbaseset SET settingOu=:settingOu WHERE idUser=:idUser";
        try {
            // var_dump($SQLUpdate);          
            $sth = connectDB::$dbh->prepare($sqlUpdate);
            $sth->bindValue(':settingOu', $input['OUID'], PDO::PARAM_INT);
            $sth->bindValue(':idUser', $input['UserID'], PDO::PARAM_INT);
            $result = $sth->execute();
        } catch (Exception $e) {
        }
        // var_dump($result);
        return $result;   
    }

    function mydapTest()
    {
        $mydap = ldap_connect('192.168.95.249', 389);
        if (!$mydap) {
            return false;
        }
        ldap_set_option($mydap, LDAP_OPT_PROTOCOL_VERSION, 3);
        ldap_set_option($mydap, LDAP_OPT_REFERRALS, 0);  
        if (!@ldap_bind($mydap, 'administrator@adtest.com', '!QAZ2wsx')) {            
            return false;
        }
        $output = array();
        $pagesize = 1000;
        $counter = "";
        // do {
                // Enable pagination
            // ldap_control_paged_result($mydap, $pagesize, true, $counter);
            // Determine class of object we are dealing with
            // $results = ldap_search($mydap, 'dc=adtest,dc=com', 'objectClass=OrganizationalUnit') or die('Error searching LDAP: '.ldap_error($mydap));
            $results = ldap_search($mydap, 'dc=adtest,dc=com', '(&(objectCategory=person)(objectClass=user)(cn=karl))') or die('Error searching LDAP: '.ldap_error($mydap));

            $justthese = array('objectguid', 'givenname', 'cn');
            // $results = ldap_search($mydap, 'OU=OU1分部1支部1,OU=OU1分部1,OU=OU1,DC=adtest,DC=com', '(&(objectCategory=person)(objectClass=user))') or die('Error searching LDAP: '.ldap_error($mydap));
            // $results = ldap_search($mydap, 'OU=Users,DC=adtest,DC=com', '(&(objectCategory=person)(objectClass=user))') or die('Error searching LDAP: '.ldap_error($mydap));
            $members = ldap_get_entries($mydap, $results);
            var_dump($members['count']);
            if ($members["count"] > 0){
                array_shift($members);
                foreach ($members as $value) {
                    var_dump($value['cn'][0]);    
                    var_dump($value['dn']);    
                    // var_dump($value);        
                    var_dump(bin2hex($value['objectguid'][0]));
                    // var_dump(bin2hex($value['objectguid'][0]));
                    // $output=$this->FormatGUID(bin2hex($value['objectguid'][0]));
                    // var_dump($output);
                }
            }
            // var_dump($members);
            
        //     ldap_control_paged_result_response($mydap, $results, $counter);
        // } while ($counter !== null && $counter != "");

        // Return alphabetized member list
        sort($output);
    }

    function FormatGUID($hexGUID){
 
        $hexGUID = str_replace("-", "", $hexGUID);
 
        for ($i = 0; $i <= strlen($hexGUID)-2; $i = $i+2){
            $output .=  "\\".substr($hexGUID, $i, 2);
        }
 
        return $output;
    }
    
    function mydapMembers($mydap, $object_dn, $object_class = 'g')
    {
        //global $mydap;
        if (!isset($mydap)) {
            die('Error, no LDAP connection established');
        }
        if (empty($object_dn)) {
            die('Error, no LDAP object specified');
        }

        // Pagination to overcome 1000 LDAP SizeLimit
        $output = array();
        $pagesize = 1000;
        $counter = "";
        do {
                // Enable pagination
                ldap_control_paged_result($mydap, $pagesize, true, $counter);

                // Determine class of object we are dealing with
            if ($object_class == 'g') {
                // Query Group members
                $results = ldap_search($mydap, $object_dn, 'cn=*', array('member')) or die('Error searching LDAP: '.ldap_error($mydap));
                $members = ldap_get_entries($mydap, $results);

                // No group members found
                if (!isset($members[0]['member'])) {
                    return false;
                }

                // Remove 'count' element from array
                array_shift($members[0]['member']);

                // Append to output
                $output = array_merge($output, $members[0]['member']);
            } elseif ($object_class == 'c' || $object_class == "o") {
                // Query Container or Organizational Unit members
                $results = ldap_search($mydap, $object_dn, 'objectClass=user', array('sn')) or die('Error searching LDAP: '.ldap_error($mydap));
                $members = ldap_get_entries($mydap, $results);
                // Remove 'count' element from array
                array_shift($members);
                // Pull the 'dn' from each result, append to output
                foreach ($members as $e) {
                    $output[] = $e['dn'];
                }
            } else {
                die("Invalid mydap_member object_class, must be c, g, or o");
            }
            // Retrieve pagination information/position
            ldap_control_paged_result_response($mydap, $results, $counter);
        } while ($counter !== null && $counter != "");

        // Return alphabetized member list
        sort($output);
        return $output;
    }

    function mydapAttributes($mydap, $user_dn, $keep = false, $filter = 'cn=*')
    {
        if (!isset($mydap)) {
            die('Error, no LDAP connection established');
        }
        if (empty($user_dn)) {
            die('Error, no LDAP user specified');
        }

        // Disable pagination setting, not needed for individual attribute queries
        ldap_control_paged_result($mydap, 1);

        // Query user attributes
        //$results = (($keep) ? ldap_search($mydap,$user_dn,'cn=*',$keep) : ldap_search($mydap,$user_dn,'cn=*'))
        $results = (($keep) ? ldap_search($mydap, $user_dn, $filter, $keep) : ldap_search($mydap, $user_dn, 'cn=*'))
        or die('Error searching LDAP: '.ldap_error($mydap));

        $attributes = ldap_get_entries($mydap, $results);
        // Return attributes list
        if (isset($attributes[0])) {
            return $attributes[0];
        } else {
            return array();
        }
    }

    function mydap_end($mydap)
    {
        if (!isset($mydap)) {
            die('Error, no LDAP connection established');
        }
     
        // Close existing LDAP connection
        @ldap_unbind($mydap);
    }

    function addADConfigure($input)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }          
        // $domainC = new DomainAction();
        // $output_domain = null;
        // $domainC->sql_list_domain_by_id($input['DomainID'], $output_domain);
        // if (is_null($output_domain)) {
        //     if($this->log_c->create_log(array('Type'=>2,'Level'=>3,'Code'=>'02300601','Riser'=>'admin','Message'=>"Failed to set AD(Failed to list domain)"),$last_id)){
        //         $this->log_c->sql_insert_domain_log_relation($last_id,$input['DomainID']);
        //     }
        //     return CPAPIStatus::AddDomainADAuthFail;
        // }
        connectDB::$dbh->beginTransaction();
        if (!$this->sqlUpdateDomainAD($input)) {
            connectDB::$dbh->rollBack();
            if(LogAction::createLog(array('Type'=>2,'Level'=>3,'Code'=>'02300602','Riser'=>'admin','Message'=>"Failed to set AD.(Failed to update domain's db)"),$last_id)){
                LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
            }
            return CPAPIStatus::AddDomainADAuthFail;
        }
        if ($input['Enable']) {
            if ($input['Account'] != '') {                
                $domainName = $input['DomainName'];
                if (!$this->mydapStart($mydap, $input['Account'].'@'.$domainName, $input['Password'], $input['IP'])) {
                    connectDB::$dbh->rollBack();
                    if(LogAction::createLog(array('Type'=>2,'Level'=>3,'Code'=>'02300603','Riser'=>'admin','Message'=>"Failed to set AD.(Failed to auth ad)"),$last_id)){
                        LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
                    }
                    return CPAPIStatus::AddDomainADAuthFail;
                }
                $this->mydap_end($mydap);
            }
        }
        connectDB::$dbh->commit();
        if(LogAction::createLog(array('Type'=>2,'Level'=>1,'Code'=>'02100501','Riser'=>'admin','Message'=>"Set AD Success."),$last_id)){
                LogAction::sqlInsertDomainLogRelation($last_id,$input['DomainID']);
            }
        return CPAPIStatus::AddDomainADAuthSuccess;
    }

    function sqlUpdateDomainAD($input)
    {
        $result = false;
        $SQLUpdate = "UPDATE tbDomainSet SET configAd=TO_BASE64(DES_ENCRYPT(:configAd,'".DES_Key."')) WHERE idDomain=:idDomain";
        try {
            // var_dump($SQLUpdate);
            $data = array("Enable"=>$input['Enable'],"IP"=>$input['IP'],"DomainName"=>$input['DomainName'],'Account'=>$input['Account'],'Password'=>$input['Password']);
            if(isset($input['SyncOU']))
                $data['SyncOU'] = $input['SyncOU'];
            $data = json_encode($data);
            $data = base64_encode($data);
            // var_dump($data);
            $sth = connectDB::$dbh->prepare($SQLUpdate);
            $sth->bindValue(':configAd', $data, PDO::PARAM_STR);
            $sth->bindValue(':idDomain', $input['DomainID'], PDO::PARAM_INT);
            $result = $sth->execute();
        } catch (Exception $e) {
        }
        // var_dump($result);
        return $result;
    }

    function listADConfigure($idDomain, &$output)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }        
        $domainC = new DomainAction();
        $outputDomain = null;
        $domainC->sqlListDomainByID($idDomain, $outputDomain);
        if (is_null($outputDomain)) {
            return CPAPIStatus::ListDomainADConfigFail;
        }
        // var_dump($outputDomain);
        if (is_null($outputDomain['AD'])) {
            $output = array('Enable'=>false,'IP'=>'','DomainName'=>'');
        } else {
            if($outputDomain['AD'] == ''){
                $output = array('Enable'=>false,'IP'=>'','DomainName'=>'','Account'=>'');
            }
            else{
                $output = base64_decode($outputDomain['AD']);                                
                $output = json_decode($output, true);
                if(isset($output['Password']))
                    unset($output['Password']);
            }
        }
        return CPAPIStatus::ListDomainADConfigSuccess;
    }

    function adAuth($adIP, $username, $password)
    {
        if (!$this->mydapStart($mydap, $username, $password, $adIP)) {            
            return false;
        }
        return true;
    }

    function processADErrorCode($code)
    {
        $this->baseOutputResponse($code);
        switch ($code) {
            case CPAPIStatus::AddDomainADAuthFail:
            case CPAPIStatus::ListDomainADConfigFail:
            case CPAPIStatus::ListADOUFail:
            case CPAPIStatus::ImportADUserFail:
                http_response_code(400);
                break;
        }
    }
}
