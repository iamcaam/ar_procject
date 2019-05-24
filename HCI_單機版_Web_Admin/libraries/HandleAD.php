<?php
class ADAction extends BaseAPI
{

    function __construct()
    {
    }
    
    function mydapStart(&$mydap, $username, $password, $host, $port = 389)
    {
        // Connect to AD            
        $mydap = ldap_connect($host, $port);
        if (!$mydap) {
            return false;
        }
        ldap_set_option($mydap, LDAP_OPT_PROTOCOL_VERSION, 3);
        ldap_set_option($ldapconn, LDAP_OPT_REFERRALS, 0);        
        if (!@ldap_bind($mydap, $username, $password)) {            
            return false;
        }
        return true;
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
        $SQLUpdate = <<<SQL
            UPDATE tbDomainSet SET configAd=:configAd WHERE idDomain=:idDomain
SQL;
        try {
            // var_dump($input);
            $data = array("Enable"=>$input['Enable'],"IP"=>$input['IP'],"DomainName"=>$input['DomainName']);
            $data = json_encode($data);
            $data = base64_encode($data);
            // var_dump($data);
            $sth = connectDB::$dbh->prepare($SQLUpdate);
            $sth->bindValue(':configAd', $data, PDO::PARAM_STR);
            $sth->bindValue(':idDomain', $input['DomainID'], PDO::PARAM_INT);
            $result = $sth->execute();
        } catch (Exception $e) {
        }
        return $result;
    }

    function listADConfigure($idDomain, &$output)
    {
        if(!$this->connectDB()){
            // return CPAPIStatus::DBConnectFail;
            $output = array('Enable'=>false,'IP'=>'','DomainName'=>'');
            return CPAPIStatus::ListDomainADConfigSuccess;
        }        
        $domainC = new DomainAction();
        $outputDomain = null;
        $domainC->sqlListDomainByID($idDomain, $outputDomain);
        if (is_null($outputDomain)) {
            return CPAPIStatus::ListDomainADConfigFail;
        }
        if (is_null($outputDomain['AD'])) {
            $output = array('Enable'=>false,'IP'=>'','DomainName'=>'');
        } else {
            if($outputDomain['AD'] == ''){
                $output = array('Enable'=>false,'IP'=>'','DomainName'=>'');
            }
            else{
                $output = base64_decode($outputDomain['AD']);
                $output = json_decode($output, true);
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
                http_response_code(400);
                break;
        }
    }
}
