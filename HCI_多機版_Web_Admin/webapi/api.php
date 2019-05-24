<?php
// ini_set('display_errors', 'On');
define("DEBUG", false);
define("Test",false);
include_once("/var/www/html/libraries/BaseAPI.php");
include_once("/var/www/html/libraries/ConnectDB.php");
include_once("/var/www/html/libraries/ConnectSQLLite.php");
include_once("/var/www/html/libraries/ErrorEnum.php");
include_once("/var/www/html/libraries/HandleLogin.php");
include_once("/var/www/html/libraries/HandleTask.php");
include_once("/var/www/html/libraries/HandleGluster.php");
include_once("/var/www/html/libraries/HandleUser.php");
include_once("/var/www/html/libraries/HandleVD.php");
include_once("/var/www/html/libraries/HandleDomain.php");
include_once("/var/www/html/libraries/HandleResource.php");
include_once("/var/www/html/libraries/HandleSystem.php");
include_once("/var/www/html/libraries/HandleLog.php");
include_once("/var/www/html/libraries/HandleVswitch.php");
include_once("/var/www/html/libraries/HandleSchedule.php");
include_once("/var/www/html/libraries/HandleJob.php");
include_once("/var/www/html/libraries/HandleSnapshot.php");
include_once("/var/www/html/libraries/HandleCephPool.php");
include_once("/var/www/html/libraries/HandleIP.php");
include_once("/var/www/html/libraries/HandleDisk.php");
include_once("/var/www/html/libraries/HandleCeph.php");
include_once("/var/www/html/libraries/HandleVswitch.php");
include_once("/var/www/html/libraries/HandleBackup.php");
include_once("/var/www/html/libraries/HandleZfs.php");
include_once("/var/www/html/libraries/HandleNode.php");
include_once("/var/www/html/libraries/HandleNodePerformance.php");
include_once("/var/www/html/libraries/HandleVDPerformance.php");
include_once("/var/www/html/libraries/HandleUpdate.php");
include_once("/var/www/html/libraries/HandleAD.php");
include_once("/var/www/html/libraries/ISnapshotShell.php");
include_once("/var/www/html/libraries/IBackupShell.php");

class API extends BaseAPI
{
    function __construct($sessionPath="/LocalDB/admin/session")
    {    
        $this->sessionPath=$sessionPath;
        // var_dump($sessionPath);
        // $this->getStorageType();
        $api = $_GET['api'];
        if(DEBUG){
            $fp = fopen('/mnt/tmpfs/cephcmd/cmd.txt', 'a');
            $gmt = $this->getTZ($timezone);
            date_default_timezone_set($timezone);     
            fwrite($fp, date("Y-m-d H:i:s").' '.$api.' '.$_SERVER['REQUEST_METHOD'].' '.$_SERVER['REMOTE_ADDR'].PHP_EOL);   
            $data = file_get_contents("php://input");
            fwrite($fp, $data.PHP_EOL);   
            fclose($fp);
        }
        // var_dump($api);
        $apiArr = explode('/', $api);        
        $count = count($apiArr);          
        switch ($count){
            case 1:
                $this->handleOneArgc($apiArr);
                break;
            case 2:
                $this->handleTwoArgc($apiArr);
                break;
            case 3:
                $this->handleThreeArgc($apiArr);
                break;         
            case 4:
                $this->handleFourArgc($apiArr);
                break;
            default :
                http_response_code(404);
        }
    }       
    
    function binaryToHex2Str($bin_data)
    {    
        $hex = '';
        foreach(str_split($bin_data) as $bin)
            $hex .= sprintf("%02x", ord($bin));        
        return trim($hex);
    }
    
    function handleOneArgc($api)
    {                
        // var_dump($api);
        $data = file_get_contents("php://input");          
        switch ($api[0]){
            case 'login':                                   
                // $this->login($data);
                http_response_code(200);
                break;
            case 'logout':
                $oLogin = new Login();
                $oLogin->doLogout();
                break;
            case 'changepwd':
                $this->handleAdminChangePWD($data);
                break;                
            case 'time':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->timeActionOneArgc($data);
                break;                
            case 'user':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->userActionOneArgc($defaultValues,$data);
                break;                
            case 'vd':
            case 'acrogateway':
            case 'vd_size':
            case 'userdisk':
                // var_dump($this->sessionPath);
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->vdActionOneArgc($defaultValues,$data,$api[0]);                
                break;                
            case 'schedule':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->scheduleActionOneArgc($defaultValues,$data);
                break;       
            case 'snapshot':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->snapshotActionOneArgc($defaultValues,$data);
                break;
            case 'task':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->taskActionOneArgc($defaultValues,$data);
                break;                            
            case 'log':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->logActionOneArgc($data);
                break;
            case 'log_by_id':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->logByIDActionOneArgc($data);
                break;
            case 'system':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->systemActionOneArgc($defaultValues,$data,$api[0]);
                break;
            case 'system_util':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->systemActionOneArgc($defaultValues,$data,$api[0]);
                break;
            case 'alarm':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->systemActionOneArgc($defaultValues,$data,$api[0]);
                break;
            case 'smb':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->systemActionOneArgc($defaultValues,$data,$api[0]);
                break;
            case 'smb_pwd':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->systemActionOneArgc($defaultValues,$data,$api[0]);
                break;
            case 'hostname':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->systemActionOneArgc($defaultValues,$data,$api[0]);
                break;
            case 'dropcache':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->systemActionOneArgc($defaultValues,$data,$api[0]);
                break;
            case 'ssh':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->systemActionOneArgc($defaultValues,$data,$api[0]);
                break;
            case 'vswitch':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->vSwitchActionOneArgc($defaultValues,$data);
                break;
            case 'dns':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->dnsActionOneArgc($defaultValues,$data);
                break;
            case 'externalip':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->externalIPActionOneArgc($defaultValues,$data);
                break;      
            case 'disk':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->diskCephActionOneArgc($defaultValues,$data,$api[0]);
                break;  
            case 'ceph':
                $this->loginCheck($defaultValues,$this->sessionPath);                
                $this->diskCephActionOneArgc($defaultValues,$data,$api[0]);        
                break;
            case 'backup':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->backupActionOneArgc($defaultValues,$data);
                break;
            case 'check':
                if(isset($_GET['token']))
                    $defaultValues['token'] = $_GET['token'];                     
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->diskCephActionOneArgc($defaultValues,$data,$api[0]);
                break;
            case 'reboot':
                if($_SERVER['REQUEST_METHOD'] == "PUT"){
                    $this->loginCheck($defaultValues,$this->sessionPath);
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $systemC = new SystemProcess();
                    $systemC->reboot($input);
                }
                break;
            case 'shutdown':
                if($_SERVER['REQUEST_METHOD'] == "PUT"){
                    $this->loginCheck($defaultValues,$this->sessionPath);
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $systemC = new SystemProcess();
                    $systemC->shutdown($input);
                }
                break;          
            case 'resource':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->resourceActionOneArgc($defaultValues);
                break;
            case 'ad':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->adActionOneArgc($defaultValues,$data);
                break;
            case 'gluster':                
                $this->loginCheck($defaultValues,$this->sessionPath);                
                $this->glusterActionOneArgc($defaultValues,$data);
                break;
            case 'snapshot_userdisk':                
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->snapshotDiskActionOneArgc($defaultValues,$data);
                break;   
            default :
                http_response_code(404);
        }
    }       
    
    function handleTwoArgc($api)
    {        
        $data = file_get_contents("php://input");
        switch ($api[0]){            
            // case 'iscsi':
            //     $this->loginCheck();
            //     $this->iscsi_action_two_argc($api[1], $data);
            //     break;        
            // case 'cluster':
            //     $this->loginCheck();
            //     $this->cluster_action_two_argc($api, $data);
            //     break;            
            // case 'target':
            //     $this->loginCheck();
            //     $this->targetActionTwoArgc($api[1], $data);
            //     break;
            case 'onlinevd':
            case 'vd':
                // var_dump($api);
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->vdActionTwoArgc($defaultValues,$api, $data);
                break;
            case 'userdisk':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->vdActionTwoArgc($defaultValues,$api, $data);
                break;
            case 'seed':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->vdActionTwoArgc($defaultValues,$api, $data);
                break;
            case 'schedule':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->scheduleActionTwoArgc($defaultValues,$api, $data);
                break;
            case 'job':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->jobActionTwoArgc($defaultValues,$api, $data);
                break;
            case 'snapshot':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->snapshotActionTwoArgc($defaultValues,$api, $data);
                break;
            case 'snapshot_userdisk':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->snapshotDiskActionTwoArgc($defaultValues,$api, $data);
                break;
            case 'user':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->userActionTwoArgc($defaultValues,$api, $data);
                break;           
            case 'task':                
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->taskActionTwoArgc($defaultValues,$api, $data);
                break;
            case 'update':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->update_action_two_argc($api, $data);
                break;
            case 'backup':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->backupActionTwoArgc($defaultValues,$api, $data);
                break;
            case 'disk':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->diskActionTwoArgc($defaultValues,$api, $data);
                break;
            case 'ceph':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->cephActionTwoArgc($defaultValues,$api, $data);
                break;         
            case 'gluster':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->glusterActionTwoArgc($defaultValues,$api, $data);
                break;
            case 'ad':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->adActionTwoArgc($defaultValues,$api, $data);
                break;
            default :
                http_response_code(404);
        }
    }       
    
    function handleThreeArgc($api)
    {                
        $data = file_get_contents("php://input");        
        switch ($api[0]){           
            case 'vd':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->vdActionThreeArgc($defaultValues,$api, $data);
                break;
            case 'acrogateway':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->acroGatewayActionThreeArgc($defaultValues,$api, $data);
                break;
            case 'backup':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->backupActionThreeArgc($defaultValues,$api, $data); 
                break;
            case 'schedule':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->jobActionThreeArgc($defaultValues,$api,$data);
                break;
            case 'snapshot':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->snapshotActionThreeArgc($defaultValues,$api,$data);
                break;
            case 'snapshot_userdisk':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->snapshotDiskActionThreeArgc($defaultValues,$api,$data);
                break;
            // case 'domain':
            //     $this->loginCheck();
            //     $this->domain_action_three_argc($api,$data);
            //     break;            
            case 'user':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->userActionThreeArgc($defaultValues,$api, $data);
                break;     
            case 'seed':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->seedActionThreeArgc($defaultValues,$api, $data);
                break;
            case 'uservd':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->userVDActionThreeArgc($defaultValues,$api, $data);
                break;
            case 'ceph':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->cephActionThreeArgc($defaultValues,$api, $data);
                break;
            default :
                http_response_code(404);
        }
    }
    
    function handleFourArgc($api)
    {
        $data = file_get_contents("php://input");
        switch ($api[0]){   
            case 'vd':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->vdActionFourArgc($defaultValues,$api, $data);    
                break; 
            case 'user':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->userActionFourArgc($defaultValues,$api, $data);    
                break;                        
            // case 'cluster':
            //     $this->loginCheck();
            //     $this->cluster_action_four_argc($api, $data);                
            //     break;              
            default :
                http_response_code(404);
        }
    }
    
    function login($data)
    {
        $oLogin = new Login();
        $data = json_decode($data,true);
        $estatus = APIStatus::LoginFail;
        $pwd =  base64_decode($data['Password']);  
        $pwd = $this->binaryToHex2Str($pwd);                                        
        if(isset($data['Username']))
        {
            if($data['Username'] == 'admin')
                $status = $oLogin->Login_By_Data_Web($data['Username'], $pwd);                               
        }
        if($status != APIStatus::LoginSuccess)
            http_response_code(401);
    }
    
    function handleAdminChangePWD($data)
    {
        switch($_SERVER['REQUEST_METHOD'])
        {
            case "POST":
                $data = json_decode($data,true);
                $oLogin = new Login();
                $eLoginStatus = $oLogin->Change_Password($data['OldPWD'], $data['PWD']);
                if($eLoginStatus == APIStatus::ChangePWDOldPWDFail){
                    echo json_encode(array('status' => 80));
                    exit();
                }
                echo '{}';
                break;
            default :
                http_response_code(404);
        }                    
    }      

    function adActionOneArgc($defaultValues,$input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $input = array();
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->listAD($input);
                break;
            case "POST":
                $input = json_decode($input, true);
                // var_dump($input);
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->setAD($input);
                break;
            default:
                http_response_code(404);
        }
    }

    function listAD($input)
    {
        $ad_c = new ADAction();
        $responsecode = $ad_c->listADConfigure($input['DomainID'],$output);
        if($responsecode == CPAPIStatus::ListDomainADConfigSuccess){
            $this->output_json($output);
        }
        else{
            $ad_c->processADErrorCode($responsecode);
        }
    }

    function setAD($input)
    {
        $ad_c = new ADAction();
        $responsecode = $ad_c->addADConfigure($input);
        if($responsecode != CPAPIStatus::AddDomainADAuthSuccess){
            $ad_c->processADErrorCode($responsecode);
        }
    }

    function glusterActionOneArgc($defaultValues,$input)
    {        
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $input = array();                
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->listGluster($input);
                break;
            case "DELETE":
                $input = array();                
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->clearGluster($input);
                break;
            default:
                http_response_code(404);
        }
    }

    function listGluster($input)
    {
        // var_dump($input);    
        $glusterC = new GlusterAction();
        $glusterC->listGlusterState($input,$output);
        $this->output_json($output);
    }

    function clearGluster($input)
    {
        $glusterC = new GlusterAction();
        $glusterC->clearGluster($input);
    }

    function resourceActionOneArgc($defaultValues)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $input = array();
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->listResource($input);
                break;          
            default:
                http_response_code(404);
        }
    }

    function listResource($input)
    {
        $resourceC = new ResourceAction();
        $responsecode = $resourceC->listResource($input,$output);
        if($responsecode != CPAPIStatus::ListResourceSuccess)
            $resourceC->processResourceErrorCode($responsecode);
        else
            $this->output_json($output);
    }

    function userActionOneArgc($defaultValues, $input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $input = array();
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->listUser($input);
                break;      
            case "POST":
                $input = json_decode($input, true);
                $this->setDefaultValueToInput($defaultValues,$input);                
                $this->createUserAction($input);
                break;                
            default:
                http_response_code(404);
        }
    }    
    
    function createUserAction($input)
    {
        $user_c = new UserAction();        
        $responsecode = $user_c->createUser($input, $output);
        if ($responsecode == CPAPIStatus::CreateUserSuccess) {
            $this->output_json($output);
        } else {
            $user_c->process_user_error_code($responsecode);
        }
    }

    function listUser($input)
    {        
        $userC = new UserAction();
        $responsecode = $userC->listUser($input['DomainID'], $output);
        if ($responsecode == CPAPIStatus::ListUserofDomainSuccess) {
            $this->output_json($output);
        } else {
            $userC->process_user_error_code($responsecode);
        }
    }

    function scheduleActionOneArgc($defaultValues,$input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $input = array();
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->listSchedule($input);
                break;            
            case "POST":
                $input = json_decode($input, true);
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->createSchedule($input);
                break;
            default:
                http_response_code(404);
        }
    }

    function listSchedule($input)
    {
        $scheduleC = new ScheduleAction();
        $responsecode = $scheduleC->listSchedule($input,$output);
        if($responsecode != CPAPIStatus::ListScheduleSuccess){
            $scheduleC->processScheduleErrorCode($responsecode);
        }
        else{
            $this->output_json($output);
        }
    }

    function createSchedule($input)
    {
        $scheduleC = new ScheduleAction();
        $responsecode = $scheduleC->createSchedule($input,$output);
        if($responsecode != CPAPIStatus::CreateScheduleSuccess){
            $scheduleC->processScheduleErrorCode($responsecode);
        }
        else{
            $this->output_json($output);
        }            
    }

    function backupActionOneArgc($defaultValues,$input)
    {
        switch($_SERVER['REQUEST_METHOD'])
        {
            case "GET":                
                $input = array();
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->listBackup($input);
                break;
            case "POST":            
                $input = json_decode($input,true);
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->setBackup($input);
                break;
            case "DELETE":
                $input = array();
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->deleteBackup($input);
                break;
            default:
                http_response_code(404);
        }
    }

    function listBackup($input)
    {
        $backupC = new BackupAction();
        $backupC->backupList($input,$output);
        $this->output_json($output);
    }

    function setBackup($input)
    {
        $backupC = new BackupAction();
        $responsecode = $backupC->setBackupMapping($input,$output);
        if($responsecode == CPAPIStatus::SetBackupMappingSuccess)
            $this->output_json($output);
        else
            $backupC->processBackupErrorCode($responsecode);
    }

    function deleteBackup($input)
    {
        $backupC = new BackupAction();
        $responsecode = $backupC->backupDelete($input);
        if($responsecode != CPAPIStatus::DeleteBackupMappingSuccess){
            $backupC->processBackupErrorCode($responsecode);
        }        
    }

    function timeActionOneArgc($data)
    {
        // var_dump($_SERVER['REQUEST_METHOD']);
        switch($_SERVER['REQUEST_METHOD'])
        {
            case "GET":                
                $this->listTime();
                break;
            case "POST":
                $data = json_decode($data,true);       
                $this->setTime($data);
                break;
            case "PUT":
                $data = json_decode($data,true);       
                $this->updateTime($data);
                break;
            default :
                http_response_code(404);
        }                    
    }

    function listTime()
    {
        $system = new SystemProcess();
        $time = $system->GetTime();
        $this->output_json($time);
    }

    function setTime($data)
    {        
        $system = new SystemProcess();
        $system->SetTime($data);    
        echo '{}';
    }

    function updateTime($data)
    {
        $system = new SystemProcess();
        $time = $system->updateTime($data);
        echo '{}';
    }
    
    function vdActionOneArgc($defaultValues, $input,$api)
    {      
        switch ($_SERVER['REQUEST_METHOD']) {
            case "POST":   
                switch ($api) {             
                    case 'vd':
                        $input = json_decode($input, true);                
                        $this->setDefaultValueToInput($defaultValues,$input);                
                        $this->createVD($input);   
                        break;
                    case 'acrogateway':
                        $input = json_decode($input, true);                
                        $this->setDefaultValueToInput($defaultValues,$input);                
                        $this->createAcroGateway($input);
                        break;
                    default:
                        http_response_code(404);
                        break;
                }
                break;  
            case "GET":
                switch ($api) {
                    case 'vd':
                        $this->setDefaultValueToInput($defaultValues,$input);                
                        $this->listVD($input);
                        break;
                    case 'vd_size':
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listVDWithSize($input);
                        break;
                    case 'userdisk':
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listUserDiskWithSize($input);
                        break;
                    case 'acrogateway':
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listAcroGateway($input);
                        break;
                    default:
                        http_response_code(404);
                        break;
                }                
                break;
            default:
                http_response_code(404);
        }
        
    }

    function createAcroGateway($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->createAcroGatewayNew($input, $output);        
        if ($responsecode != CPAPIStatus::CreateVDofCephSuccess) {
            $vdC->processVDErrorCode($responsecode);
        }
    }

    function listAcroGateway($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->listAcroGateway($input,$output);
        if ($responsecode != CPAPIStatus::ListVDofCephSuccess) {
            $vdC->processVDErrorCode($responsecode);
        }
        else{            
            $this->output_json($output);  
        }
    }

    function listUserDiskWithSize($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->listAllUserDiskWithSize($input, $output);    
         if ($responsecode != CPAPIStatus::ListVDofCephSuccess) {
            $vdC->processVDErrorCode($responsecode);
        }
        else{            
            $this->output_json($output);  
        }   
    }

    function listVDWithSize($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->listVDWithSize($input, $output);    
        if ($responsecode != CPAPIStatus::ListVDofCephSuccess) {
            $vdC->processVDErrorCode($responsecode);
        }
        else{            
            $this->output_json($output);  
        }
    }

    function listVD($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->listVD($input, $output);
        // var_dump($output);        
        if ($responsecode != CPAPIStatus::ListVDofCephSuccess) {
            $vdC->processVDErrorCode($responsecode);
        }
        else
            $this->output_json($output);
    }

    function createVD($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->createVD($input, $output);        
        if ($responsecode != CPAPIStatus::CreateVDofCephSuccess) {
            $vdC->processVDErrorCode($responsecode);
        }
        else
           echo '{}';
    }

    function snapshotActionOneArgc($defaultValues, $input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {            
            case "POST":
                $input = json_decode($input, true);
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->createTakeSnapshotTask($input);
                break;
            default:
                http_response_code(404);
        }
    }

    function createTakeSnapshotTask($input)
    {
        // echo 'take';
        $snap_c = new SnapshotAction();
        $input['Desc'] = 'Manual : '.$input['Desc'];        
        $responsecode = $snap_c->createTakeSnapshotTask($input);
        if($responsecode != CPAPIStatus::TakeSnapshotSuccess)
            $snap_c->processSnapshotErrorCode($responsecode);
    }

    function snapshotDiskActionOneArgc($defaultValues, $input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {            
            case "POST":
                $input = json_decode($input, true);   
                $this->setDefaultValueToInput($defaultValues,$input);             
                $this->createTakeVDiskSnapshotTask($input);
                break;
            default:
                http_response_code(404);
        }
    }

    function createTakeVDiskSnapshotTask($input)
    {
        if(Test){
            $shell = new SnapshotShellTest();
            $snap_c = new SnapshotAction($shell);
        }
        else
            $snap_c = new SnapshotAction();
        $input['Desc'] = 'Manual : '.$input['Desc'];        
        $responsecode = $snap_c->createTakeUPSnapshotTask($input);
        if($responsecode != CPAPIStatus::TakeSnapshotSuccess)
            $snap_c->processSnapshotErrorCode($responsecode);
    }

    function taskActionOneArgc($defaultValues, $input)
    {        
        switch($_SERVER['REQUEST_METHOD'])
        {           
            case "GET":          
                $input = array();                
                $this->setDefaultValueToInput($defaultValues,$input);          
                $this->listTask($input);
                break;
            case "DELETE":
                $input = array();                
                $this->setDefaultValueToInput($defaultValues,$input);   
                $this->clearTask($input);
                break;
            default :
                http_response_code(404);
        }                    
    }
    
    function listTask($input)
    {
        $taskC = new TaskAction();
        $responsecode = $taskC->listTask($input,$output);        
        if($responsecode != CPAPIStatus::ListTaskSuccess)
            $taskC->processTaskErrorCode($responsecode);
        else
            $this->output_json($output);
    }
    
    function clearTask($input)
    {
        $task_c = new TaskAction();
        $task_c->clearTask($input); 
        // if($responsecode != CPAPIStatus::ClearTaskSuccess)
        //     $cluseter_c->process_task_error_code($responsecode);
    }
        
    function externalIPActionOneArgc($defaultValues,$input)
    {
        switch($_SERVER['REQUEST_METHOD'])
        {            
            case "GET":                 
                $input = array();     
                $this->setDefaultValueToInput($defaultValues,$input);              
                $this->listExternal($input);
                break;
            case "POST":
                $input = json_decode($input,true);
                $this->setDefaultValueToInput($defaultValues,$input);              
                $this->setExternal($input);
                break;
            default :
                http_response_code(404);
        }      
    }  

    function listExternal($input)
    {
        $ipC = new IPProcess();
        $ipC->listExternal($output);
        $this->output_json($output);
    }

    function setExternal($input)
    {
        $ipC = new IPProcess();
        $ipC->setExternal($input);
    }

    function diskCephActionOneArgc($defaultValues,$input,$api)
    {        
        switch ($api) {
            case 'disk':
                switch($_SERVER['REQUEST_METHOD'])  
                {            
                    case "GET":                 
                        $input = array();             
                        $this->setDefaultValueToInput($defaultValues,$input);      
                        $this->listDisk($input);
                        break;
                    default :
                        http_response_code(404);
                }   
                break;       
            case 'ceph':
                switch($_SERVER['REQUEST_METHOD'])  
                {            
                    case "GET":
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues,$input);      
                        $this->listCeph($input);
                        break;
                    case "POST":                 
                        $input = json_decode($input,true);             
                        $this->setDefaultValueToInput($defaultValues,$input);      
                        $this->createCeph($input);
                        break;
                    case "DELETE":
                        $input = json_decode($input,true);
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->deleteCeph($input);
                        break;
                    default :
                        http_response_code(404);
                }     
                break;   
            case 'check':
                switch($_SERVER['REQUEST_METHOD'])  
                {            
                    case "GET":                    
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues,$input);
                        // if($this->isCeph)
                        //     $this->check($input);
                        // else
                            $this->checkZfs($input);
                        break;                   
                    default :
                        http_response_code(404);
                }
                break;
            default:
                http_response_code(404);
                break;
        }   
    }

    function listDisk($input)
    {
        $diskC = new DiskAction();
        // if($this->isCeph)
        //     $diskC->listDisk($output);
        // else
            $diskC->listZfsDisk($output);
        $this->output_json($output);
    }

    function listCeph($input)
    {                
        // if($this->isCeph){
        //     $cephC = new CephAction();
        //     $cephC->listCeph($input,$output);
        // }
        // else{
            $zfsC = new ZFSAction();
            $zfsC->listZfs($input,$output);   
        // }
        if(!is_array($output)){
            echo '{}';
        }
        else{
            $this->output_json($output);
        }
    }

    function createCeph($input)
    {
        // if($this->isCeph){
        //     $cephC = new CephAction();
        //     $cephC->createCeph($input);
        // }
        // else{
            $zfsC = new ZFSAction();
            $zfsC->createZfs($input);
        // }

    }

    function deleteCeph($input)
    {
        // if($this->isCeph){
        //     $cephC = new CephAction();
        //     $cephC->deleteCeph($input);
        // }
        // else{
            $zfsC = new ZFSAction();
            $zfsC->deleteZfs($input);   
        // }
    }

    function check($input)
    {
        $cephUsage = 0;
        $cephTotal = 0;
        $cephUsed = 0;
        $cephC = new CephAction();
        $cephC->listCeph($input,$outputCeph);      
        $output['Reboot'] = false;
        if(isset($outputCeph)){
            if(count($outputCeph > 0)){                
                if($outputCeph[0]['Total'] > 0 && $outputCeph[0]['Used'] >0){
                    $cephTotal = $outputCeph[0]['Total'];
                    $cephUsed = $outputCeph[0]['Used'];
                    $cephUsage = bcdiv($outputCeph[0]['Used'], $outputCeph[0]['Total'],4)*100;
                }
            }                

        }
        if(isset($outputCeph['Reboot']))
            $output['Reboot'] = $outputCeph['Reboot'];
        $this->listRAIDUsage($input,$outputUsage);
        $systemC = new SystemProcess();
        $systemC->listAlarm($input,$output);
        // var_dump($output);
        $output['CephTotal'] = $cephTotal;
        $output['CephUsed'] = $cephUsed;
        $output['CephUsage'] = $cephUsage;
        $output['RAIDUsageMax'] = $outputUsage['RAIDUsageMax'];
        $output['RAIDAlarm'] = $outputUsage['RAIDAlarm'];        
        $this->output_json($output);
    }

    function checkZfs($input)
    {
        $cephUsage = 0;
        $cephTotal = 0;
        $cephUsed = 0;
        $systemC = new SystemProcess();
        $outputCeph = $systemC->getCeph($input); 
        $output['Reboot'] = false;        
        if(isset($outputCeph)){             
            $output['Ceph'] = $outputCeph;                         
        }             
        $this->listRAIDUsage($input,$outputUsage);
        $systemC = new SystemProcess();
        $systemC->listAlarm($input,$outputAlarm);
        $output['Alarm'] = $outputAlarm['Alarm'];
        $output['RAIDUsageMax'] = $outputUsage['RAIDUsageMax'];
        $output['RAIDAlarm'] = $outputUsage['RAIDAlarm'];
        $this->output_json($output);
    }

    function logActionOneArgc($input)
    {
        switch($_SERVER['REQUEST_METHOD'])
        {            
            case "GET":                 
                $input = array();   
                $input['Page']=$_GET['Page'];
                $input['Field']=$_GET['Field'];
                $input['Type']=$_GET['Type'];
                $input['Level']=$_GET['Level'];
                $input['ASC']=$_GET['ASC'];
                $this->listLog($input);
                break;
            default :
                http_response_code(404);
        }      
    }   
    
    function listLog($input)
    {
        $logC = new LogAction();
        $responsecode = $logC->listLogWithSortAndType($input,$outputLog);
        if($responsecode == CPAPIStatus::ListLogSuccess){
            $this->output_json($outputLog);
        }
        else{
            $logC->processLogErrorCode($responsecode);
        }
    }
    
    function dnsActionOneArgc($defaultValues,$input)
    {        
        switch($_SERVER['REQUEST_METHOD'])  
        {                       
            case 'POST':                
                $input = json_decode($input,true);                
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->setDNS($input);                
                break;
            default :
                http_response_code(404);
        }                           
    }    

    function setDNS($input)
    {
        $vSwitchC = new vSwitchAction();
        $responsecode = $vSwitchC->setDNS($input);
    }

    function vSwitchActionOneArgc($defaultValues,$input)
    {        
        switch($_SERVER['REQUEST_METHOD'])  
        {            
            case "GET":                 
                $input = array();             
                $this->setDefaultValueToInput($defaultValues,$input);      
                $this->listVswitch($input);
                break;
            case 'POST':
                // var_dump($input);
                $input = json_decode($input,true);
                // var_dump($input);
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->setVswitch($input);
                break;
            default :
                http_response_code(404);
        }                           
    }    

    function listVswitch($input)
    {
        $vSwitchC = new vSwitchAction();
        $responsecode = $vSwitchC->listVswitch($input,$output);
        if($responsecode != CPAPIStatus::ListVSwitchSuccess){
            $output = array("Vswitchs"=>array(),"FreeDevs"=>array());
        }            
        $this->output_json($output);
    }

    function setVswitch($input)
    {
        // var_dump($input);
        $vSwitchC = new vSwitchAction();
        $responsecode = $vSwitchC->setVswitch($input);
        if($responsecode != CPAPIStatus::SetVSwitchSuccess){
            $vSwitchC->process_vswitch_error_code($responsecode);
        }
    }

    function systemActionOneArgc($defaultValues,$input,$api)
    {
        switch ($api) {
            case 'system':
                switch($_SERVER['REQUEST_METHOD'])  
                {            
                    case "GET":                 
                        $input = array();             
                        $this->setDefaultValueToInput($defaultValues,$input);      
                        $this->listSystemInfo($input);
                        break;
                    default :
                        http_response_code(404);
                }   
                break;
            case 'system_util':
                switch($_SERVER['REQUEST_METHOD'])  
                {            
                    case "GET":                 
                        $input = array();                           
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listSystemUtil($input);
                        break;
                    default :
                        http_response_code(404);
                }   
                break;
            case 'alarm':
                switch($_SERVER['REQUEST_METHOD'])  
                {            
                    case "GET":                 
                        $input = array();                           
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listAlarm($input);
                        break;
                    case 'PUT':
                        $input = json_decode($input,true);
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->setAlarm($input);
                        break;
                    default :
                        http_response_code(404);
                }   
                break;
            case 'smb':
                switch($_SERVER['REQUEST_METHOD'])  
                {            
                    case "GET":                 
                        $input = array();                           
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listSMB($input);
                        break;
                    case 'PUT':
                        $input = json_decode($input,true);
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->setSMB($input);
                        break;
                    default :
                        http_response_code(404);
                }   
                break;
            case 'smb_pwd':
                switch($_SERVER['REQUEST_METHOD'])  
                {                                
                    case 'PUT':
                        $input = json_decode($input,true);
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->setSMBPWD($input);
                        break;
                    default :
                        http_response_code(404);
                }   
                break;
            case 'hostname':
                switch($_SERVER['REQUEST_METHOD'])  
                {                                
                    case 'GET':
                        $input = array();                 
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listHostname($input);
                        break;
                    case 'PUT':
                        $input = json_decode($input,true);
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->setHostname($input);
                        break;   
                    default :
                        http_response_code(404);
                }   
                break;
            case 'dropcache':
                switch($_SERVER['REQUEST_METHOD'])  
                {                                
                    case 'DELETE':
                        $input = array();                 
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->dropCashe($input);
                        break;
                    default :
                        http_response_code(404);
                }   
                break;
            case 'ssh':
                switch($_SERVER['REQUEST_METHOD'])  
                {                   
                    case 'GET':
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->getSSH($input);
                        break;             
                    case 'PUT':
                        $input = json_decode($input,true);
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->setSSH($input);
                        break;
                    default :
                        http_response_code(404);
                }
                break;
            default:
                http_response_code(404);
                break;
        }        
    }   

    function getSSH($input)
    {
        $nodeC = new NodeAction();
        $responsecode = $nodeC->listNodeSSH($input,$output);
        if($responsecode != CPAPIStatus::ListNodeSSHSuccess)
            http_response_code(400);
        else
            $this->output_json($output);
    }

    function setSSH($input)
    {
        $nodeC = new NodeAction();
        $responsecode = $nodeC->setNodeSSH($input);
        // var_dump($responsecode);
        if($responsecode != CPAPIStatus::SetNodeSSHSuccess)
            http_response_code(400);
    }
    
    function listSystemInfo($input)
    {
        $systemC = new SystemProcess();        
        if($systemC->systemInfo($input,$output)){
            $this->output_json($output);
        }
        else{
            http_response_code(400);
        }
    }

    function listSystemUtil($input)
    {
        $systemC = new SystemProcess();        
        if($systemC->systemUtil($input,$output)){
            $this->output_json($output);
        }
        else{
            http_response_code(400);
        }
    }

    function listAlarm($input)
    {
        $systemC = new SystemProcess();        
        if($systemC->listAlarm($input,$output)){
            $this->output_json($output);
        }
        else{
            http_response_code(400);
        }
    }

    function setAlarm($input)
    {
        $systemC = new SystemProcess();        
        $systemC->setAlarm($input);
    }

    function listSMB($input)
    {
        $systemC = new SystemProcess();        
        if($systemC->listSMB($input,$output)){
            $this->output_json($output);
        }
        else{
            http_response_code(400);
        }
    }

    function setSMB($input)
    {
        $systemC = new SystemProcess();        
        $systemC->setSMB($input);
    }

    function setSMBPWD($input)
    {
        $systemC = new SystemProcess();        
        $systemC->setSMBPassword($input);   
    }

    function listHostname($input)
    {
        $systemC = new SystemProcess();
        $output = $systemC->listHostname($input);
        $this->output_json($output);
    }

    function setHostname($input)
    {
        $systemC = new SystemProcess();
        $systemC->setHostname($input);       
    }

    function dropCashe($input)
    {
        $systemC = new SystemProcess();        
        $systemC->dropCashe($input);
    }

    function logByIDActionOneArgc($input)
    {
        switch($_SERVER['REQUEST_METHOD'])
        {            
            case "GET":                 
                $input = array();                
                $input['count']=(int)$_GET['count'];
                $input['id']=(int)$_GET['id'];
                $input['readback']=(int)$_GET['readback'];
                $this->listLogByID($input);
                break;
            default :
                http_response_code(404);
        }      
    }    
    
    function listLogByID($input)
    {
        $log_c = new LogAction();
        $responsecode = $log_c->listLogByID($input,$output_log);
        if($responsecode == CPAPIStatus::ListLogSuccess){
            $this->output_json($output_log);
        }
        else{
            $log_c->processLogErrorCode($responsecode);
        }
    }
    
    function scheduleActionTwoArgc($defaultValues,$argc,$input)
    {
        if (preg_match('/^schedule\/[0-9]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "DELETE":
                    $input = array();
                    $input['ScheduleID']=$argc[1];
                    $this->setDefaultValueToInput($defaultValues,$input);                   
                    $this->deleteSchedule($input);
                    break;   
                case "PUT":
                    $input = json_decode($input,true);      
                    $this->checkJson($input);          
                    $input['ScheduleID']=$argc[1];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->modifySchedule($input);
                    break;            
                default:
                    http_response_code(404);
            }
        }
    }

    function deleteSchedule($input)
    {
        $scheduleC = new ScheduleAction();
        $responsecode = $scheduleC->deleteSchedule($input);
        if($responsecode != CPAPIStatus::DeleteScheduleSuccess){
            $scheduleC->processScheduleErrorCode($responsecode);
        }
        else
            echo '{}';
    }

    function modifySchedule($input)
    {
        $scheduleC = new ScheduleAction();
        $responsecode = $scheduleC->modifySchedule($input,$output);
        if($responsecode != CPAPIStatus::ModfiyScheduleSuccess){
            $scheduleC->processScheduleErrorCode($responsecode);
        }
        else{
            $this->output_json($output);
        }      
    }   

    function targetActionTwoArgc($argc,$input)
    {        
        if(preg_match('/^[0-9]+$/',$argc))
        {
            $input = array();
            $input['IDTarget'] = $argc;
            $this->list_target_id($input);
        }
        else
        {
            http_response_code(404);
        }
    }
    
    function list_target_id($input)
    {
        switch ($_SERVER['REQUEST_METHOD'])
        {
            case "GET":
                $vdtarget_c = new Target_Action();  
                $responsecode = $vdtarget_c->list_target_by_id($input, $output);
                if($responsecode == CPAPIStatus::ListTargetbyIDSuccess)
                    $this->output_json($output);
                else
                    $vdtarget_c->process_target_error_code($responsecode);                
                break;
            default :
                http_response_code(404);
        }
    }        
    
    function iscsi_action_two_argc($argc,$input)
    {
        switch ($argc)
        {
            case 'discovery':                
                $this->discovery_action($input);
                break;
            default :
                http_response_code(404);
                break;
        }
    }
    
    function discovery_action($input)
    {
        switch ($_SERVER['REQUEST_METHOD'])
        {
            case "POST":                
                $iscsi_c = new iSCSI_Action();                
                $input = json_decode($input,true);                
                $responsecode = $iscsi_c->API_Discovery_iSCSI($input,$output);                 
                if($responsecode != CPAPIStatus::DiscoverySuccess)
                    $iscsi_c->process_iscsi_error_code($responsecode);
                else
                    $this->output_json($output);
                break;            
            default :
                http_response_code(404);
        }
    }
    
    function cluster_action_two_argc($argc,$input)
    {      
        if(preg_match('/^cluster\/free$/',$argc[0].'/'.$argc[1]))
        {
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":                    
                    $this->cluster_list_free_resource_action();                           
                    break;                
                default :
                    http_response_code(404);
            }
        }
        else if(preg_match('/^cluster\/[0-9]+$/',$argc[0].'/'.$argc[1]))
        {            
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "DELETE":                    
                    $input = array();
                    $input['ClusterID']=(int)$argc[1];
                    $this->delete_cluster_action($input);
                    break;
                case "GET":
                    $input = array();
                    $input['ClusterID']=(int)$argc[1];
                    $this->get_cluster_info_action($input);
                    break;                    
                default :
                    http_response_code(404);
            }
        }
        else
            http_response_code(404);
    }
    
    function cluster_list_free_resource_action()
    {
        $cluster_c = new Cluster_Action();
        $responsecode = $cluster_c->list_cluster_free_resource($output);
        if($responsecode == CPAPIStatus::ListClusterFreeSuccess)
            $this->output_json($output);
        else
        {
            $cluster_c->process_cluster_error_code($responsecode);
        }
    }
    
    function get_cluster_info_action($input)
    {
        set_time_limit(60);
        $cluseter_c = new Cluster_Action();        
        $responsecode = $cluseter_c->list_cluster_info($input,$output);  
        if($responsecode != CPAPIStatus::ListClusterInfoSuccess)
            $cluseter_c->process_cluster_error_code($responsecode);  
        else
            $this->output_json($output);
    }
    
    function delete_cluster_action($input)
    {
        $cluseter_c = new Cluster_Action();        
        $responsecode = $cluseter_c->delete_cluster($input);     
        if($responsecode != CPAPIStatus::DeleteClusterSuccess)
            $cluseter_c->process_cluster_error_code($responsecode);       
    }    
       
    function glusterActionTwoArgc($defaultValues,$argc,$input)
    {      
        // var_dump($argc);
        if (preg_match('/^gluster\/normal$/', $argc[0].'/'.$argc[1])) {            
            switch ($_SERVER['REQUEST_METHOD']) {                
                case "PUT":
                    $input = array();  
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->setGlusterNormal($input);
                    break;                    
                default:
                    http_response_code(404);
            }
        }     
        else if (preg_match('/^gluster\/master$/', $argc[0].'/'.$argc[1])) {            
            switch ($_SERVER['REQUEST_METHOD']) {                
                case "PUT":
                    $input = array();  
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->setGlusterMaster($input);
                    break;                    
                default:
                    http_response_code(404);
            }
        }       
        else
            http_response_code(404);
    }

    function setGlusterNormal($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->setClusterNodeNomarlState($input);
        if($responsecode != CPAPIStatus::SetNodeMaintainanceSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
    }

    function setGlusterMaster($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->setClusterGroupMasterSetLocal($input);
        if($responsecode != CPAPIStatus::SetLocalMasterSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
    }

    function taskActionTwoArgc($defaultValues,$argc,$input)
    {      
        // var_dump($argc);
        if (preg_match('/^task\/snapshot$/', $argc[0].'/'.$argc[1])) {            
            switch ($_SERVER['REQUEST_METHOD']) {                
                case "GET":
                    $input = array();  
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->listSSTask($input);
                    break;    
                case "DELETE":
                    $input = array();                    
                    $this->setDefaultValueToInput($defaultValues,$input);                
                    $this->clearSSTask($input);
                    break;            
                default:
                    http_response_code(404);
            }
        }           
        else if (preg_match('/^task\/backup$/', $argc[0].'/'.$argc[1])) {            
            switch ($_SERVER['REQUEST_METHOD']) {                
                case "GET":
                    $input = array();  
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->listBackupTask($input);
                    break;    
                case "DELETE":
                    $input = array();                    
                    $this->setDefaultValueToInput($defaultValues,$input);                
                    $this->clearBackupTask($input);
                    break;            
                default:
                    http_response_code(404);
            }
        }  
        else if (preg_match('/^task\/[-,A-Z,a-z,0-9,\.,:]+$/', $argc[0].'/'.$argc[1])) {            
            switch ($_SERVER['REQUEST_METHOD']) {                
                case "DELETE":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input);  
                    $input['TaskID']=$argc[1];                                  
                    $this->cancelTask($input);
                    break;                
                default:
                    http_response_code(404);
            }
        }        
    }

    function cancelTask($input)
    {        
        $taskC = new TaskAction();
        $responsecode = $taskC->cancelTask($input);
        if($responsecode != CPAPIStatus::CancelTaskSuccess)
            $taskC->processTaskErrorCode($responsecode);
    }   

    function clearBackupTask($input)
    {
        $taskC = new TaskAction();        
        $taskC->clearBackupTask($input);      
    }

    function listBackupTask($input)
    {
        $taskC = new TaskAction();
        $responsecode = $taskC->listBackupTask($input,$output);
        if($responsecode == CPAPIStatus::ListTaskSuccess)
            $this->output_json($output);
        else
            $task_c->processTaskErrorCode($responsecode);        
    }

    function clearSSTask($input)
    {
        $taskC = new TaskAction();        
        $taskC->clearSSTask($input);      
    }

    function listSSTask($input)
    {
        $task_c = new TaskAction();
        $responsecode = $task_c->listSSTask($input,$output);
        if($responsecode == CPAPIStatus::ListTaskSuccess)
            $this->output_json($output);
        else
            $task_c->processTaskErrorCode($responsecode);
    }

    function vdActionTwoArgc($defaultValues,$argc,$input)
    {                              
        // var_dump($argc);  
        if(preg_match('/^vd\/iso+$/',$argc[0].'/'.$argc[1]))
        {            
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":                              
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input);                    
                    $this->listISO($input);
                    break;                                  
                default :
                    http_response_code(404);
            }
        }  
        else if (preg_match('/^vd+\/suspend$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input);                    
                    $this->listSuspend($input);
                    break;
                case 'PUT':
                    $input = json_decode($input,true);
                    $this->setDefaultValueToInput($defaultValues,$input);                    
                    $this->setSuspend($input);
                    break;
                case 'POST':
                    $input = json_decode($input,true);
                    $this->setDefaultValueToInput($defaultValues,$input);                    
                    $this->setVDSuspend($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
        else if (preg_match('/^vd+\/autosuspend$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {                
                case 'POST':
                    $input = json_decode($input,true);
                    $this->setDefaultValueToInput($defaultValues,$input);                    
                    $this->setVDSuspend($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
        else if (preg_match('/^vd+\/import$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input);                    
                    $this->listImportVD($input);
                    break;
                case 'POST':
                    $input = json_decode($input,true);
                    $this->setDefaultValueToInput($defaultValues,$input);                    
                    $this->importVD($input);
                    break;
                default:
                    http_response_code(404);
            }
        } 
        else if(preg_match('/^vd\/poweron$/',$argc[0].'/'.$argc[1]))
        {            
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "POST":                              
                    $vds = json_decode($input,true);  
                    $data['VDs'] = $vds;
                    $this->setDefaultValueToInput($defaultValues,$data);   
                    // var_dump($input);
                    $this->poweronVD($data);
                    break;                                  
                default :
                    http_response_code(404);
            }
        }    
        else if(preg_match('/^vd\/shutdown$/',$argc[0].'/'.$argc[1]))
        {
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "POST":                              
                    $vds = json_decode($input,true);  
                    $data['VDs'] = $vds;
                    $this->setDefaultValueToInput($defaultValues,$data);   
                    $this->shutdownVD($data);
                    break;                                  
                default :
                    http_response_code(404);
            }
        }
        else if(preg_match('/^vd\/poweroff$/',$argc[0].'/'.$argc[1]))
        {
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "POST":                              
                    $vds = json_decode($input,true);  
                    $data['VDs'] = $vds;
                    $this->setDefaultValueToInput($defaultValues,$data);   
                    $this->poweroffVD($data);
                    break;                                  
                default :
                    http_response_code(404);
            }
        }             
        else if(preg_match('/^vd\/disable$/',$argc[0].'/'.$argc[1]))
        {            
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "PUT":                              
                    $vds = json_decode($input,true);  
                    $data['VDs'] = $vds;                       
                    $this->setDefaultValueToInput($defaultValues,$data);
                    $data['Disable'] = true;
                    $this->setVDDisable($data);
                    break;                                  
                default :
                    http_response_code(404);
            }
        }    
        else if(preg_match('/^vd\/enable$/',$argc[0].'/'.$argc[1]))
        {            
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "PUT":                              
                    $vds = json_decode($input,true);  
                    $data['VDs'] = $vds;           
                    $this->setDefaultValueToInput($defaultValues,$data);   
                    $data['Disable'] = false;
                    $this->setVDDisable($data);
                    break;                                  
                default :
                    http_response_code(404);
            }
        }
        else if(preg_match('/^vd\/backup$/',$argc[0].'/'.$argc[1]))
        {            
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "POST":                              
                    $input = json_decode($input,true);                                
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->backupVD($input);
                    break;                                  
                default :
                    http_response_code(404);
            }
        }
        else if(preg_match('/^seed\/backup$/',$argc[0].'/'.$argc[1]))
        {            
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "POST":                              
                    $input = json_decode($input,true);                                
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->backupSeed($input);
                    break;                                  
                default :
                    http_response_code(404);
            }
        }
        else if(preg_match('/^seed\/restore$/',$argc[0].'/'.$argc[1]))
        {            
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "POST":                              
                    $input = json_decode($input,true);                                
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->restoreSeed($input);
                    break;                                  
                default :
                    http_response_code(404);
            }
        }
        else if(preg_match('/^vd\/restore$/',$argc[0].'/'.$argc[1]))
        {            
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "POST":                              
                    $input = json_decode($input,true);                                
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->restoreVD($input);
                    break;                                  
                default :
                    http_response_code(404);
            }
        }
        else if(preg_match('/^userdisk\/backup$/',$argc[0].'/'.$argc[1])){
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "POST":                              
                    $input = json_decode($input,true);
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->backupUserDisk($input);
                    break;                                  
                default :
                    http_response_code(404);
            }
        }else if(preg_match('/^userdisk\/restore$/',$argc[0].'/'.$argc[1])){
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "POST":                              
                    $input = json_decode($input,true);
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->restoreUserDisk($input);
                    break;                                  
                default :
                    http_response_code(404);
            }
        }else if(preg_match('/^seed\/[a-z,0-9,-]+$/',$argc[0].'/'.$argc[1]))
        {            
            switch ($_SERVER['REQUEST_METHOD'])
            {                
                case 'PUT':
                    $input = json_decode($input,true);  
                    // var_dump($input);   
                    $this->setDefaultValueToInput($defaultValues,$input);                 
                    $input['VDID'] = $argc[1];
                    $this->modifySeed($input);
                    break;                         
                default :
                    http_response_code(404);
            }
        }         
        else if(preg_match('/^vd\/[a-z,0-9,-]+$/',$argc[0].'/'.$argc[1]))
        {            
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "DELETE":                              
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input);   
                    $input['VDID']=$argc[1];
                    $this->deleteVD($input);
                    break;    
                case 'GET':                    
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input);                    
                    $input['VDID'] = $argc[1];
                    $this->listVDInfo($input);
                    break;        
                case 'PUT':
                    $input = json_decode($input,true);  
                    // var_dump($input);   
                    $this->setDefaultValueToInput($defaultValues,$input);                 
                    $input['VDID'] = $argc[1];
                    $this->modifyVD($input);
                    break;                         
                default :
                    http_response_code(404);
            }
        }
        else if(preg_match('/^onlinevd\/[a-z,0-9,-]+$/',$argc[0].'/'.$argc[1]))
        {            
            switch ($_SERVER['REQUEST_METHOD'])
            {                
                case 'PUT':
                    $input = json_decode($input,true);  
                    // var_dump($input);   
                    $this->setDefaultValueToInput($defaultValues,$input);                 
                    $input['VDID'] = $argc[1];
                    $this->modifyOnlineVD($input);
                    break;                         
                default :
                    http_response_code(404);
            }
        }             
        else
            http_response_code(404);
    }

    function modifyOnlineVD($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->modifyOnlineVD($input, $output);
        if ($responsecode != CPAPIStatus::ModifyVDSuccess) {
            $vdC->processVDErrorCode($responsecode);
        }
    }

    function restoreUserDisk($input)
    {
        if(Test){
            $shell = new BackupShellTest();
            $backupC = new BackupAction($shell);
        }
        else
            $backupC = new BackupAction();
        $responsecode = $backupC->restoreUP($input);
        if($responsecode != CPAPIStatus::RestoreSuccess)
            $backupC->processBackupErrorCode($responsecode);
    }

    function backupUserDisk($input)
    {                
        if(Test){
            $shell = new BackupShellTest();
            $backupC = new BackupAction($shell);
        }
        else
            $backupC = new BackupAction();
        $responsecode = $backupC->backupUserDisk($input);        
        if($responsecode != CPAPIStatus::BackupSuccess)
            $backupC->processBackupErrorCode($responsecode);
    }

    function restoreSeed($input)
    {        
        // var_dump($input);
        if(Test){
            $shell = new BackupShellTest();
            $backupC = new BackupAction($shell);
        }
        else
            $backupC = new BackupAction();
        $responsecode = $backupC->restore($input,true);
        if($responsecode != CPAPIStatus::RestoreSuccess)
            $backupC->processBackupErrorCode($responsecode);
    }

    function restoreVD($input)
    {        
        // var_dump($input);
        $backupC = new BackupAction();
        $responsecode = $backupC->restore($input);
        if($responsecode != CPAPIStatus::RestoreSuccess)
            $backupC->processBackupErrorCode($responsecode);
    }

    function backupSeed($input)
    {
        if(Test){
            $shell = new BackupShellTest();
            $backupC = new BackupAction($shell);
        }
        else
            $backupC = new BackupAction();        
        $responsecode = $backupC->backupSeed($input);
        if($responsecode != CPAPIStatus::BackupSuccess)
            $backupC->processBackupErrorCode($responsecode);
    }

    function backupVD($input)
    {
        $backupC = new BackupAction();
        $responsecode = $backupC->backup($input);
        if($responsecode != CPAPIStatus::BackupSuccess)
            $backupC->processBackupErrorCode($responsecode);
    }

    function listSuspend($input)
    {
        $vdC = new VDAction();
        $vdC->listAutosuspend($input,$output);
        $this->output_json($output);        
    }
    
    function setSuspend($input)
    {
        $vdC = new VDAction();
        $vdC->setAutosuspend($input);        
    }

    function setVDSuspend($input)
    {
        $vdC = new VDAction();
        $vdC->setVDAutosuspend($input);        
    }

    function importVD($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->importVD($input);
        if ($responsecode != CPAPIStatus::ListImportSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        }
    }

    function listImportVD($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->listImportVD($input, $output);
        if ($responsecode != CPAPIStatus::ListImportSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }
    

    function setVDDisable($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->disableVD($input);      
    }

    function listISO($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->listAllISO($input, $output);
        $this->output_json($output);
    }

    function modifySeed($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->modifySeedVD($input);
        if ($responsecode != CPAPIStatus::ModifyVDSuccess) {
            $vdC->processVDErrorCode($responsecode);
        }
    }

    function modifyVD($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->modifyVD($input, $output);
        if ($responsecode != CPAPIStatus::ModifyVDSuccess) {
            $vdC->processVDErrorCode($responsecode);
        }
    }

    function listVDInfo($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->listVDInfo($input, $output);
        if ($responsecode != CPAPIStatus::ListVDInfoSuccess) {
            $vdC->processVDErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }

    function poweronVD($input)
    {
        $vd_c = new VDAction();         
        $vd_c->poweronVD($input);           
    }
    
    function shutdownVD($input)
    {
        $vd_c = new VDAction();          
        $vd_c->shutdownVD($input);           
    }
    
    function poweroffVD($input)
    {
        $vd_c = new VDAction();           
        $vd_c->poweroffVD($input);           
    }
    
    function deleteVD($input)
    {
        $vd_c = new VDAction();          
        $responsecode = $vd_c->deleteVD($input);           
        if($responsecode != CPAPIStatus::DeleteVDSuccess)
            $vd_c->processVDErrorCode($responsecode);       
    }        
    
    function jobActionTwoArgc($defaultValues,$argc,$input)
    {
        if(preg_match('/^job\/[a-z,0-9,-]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $input['JobID']=$argc[1];
                    $this->setDefaultValueToInput($defaultValues,$input);               
                    $this->listJobDetail($input);
                    break;   
                case "DELETE":
                    $input = array();
                    $input['JobID']=$argc[1];
                    $this->setDefaultValueToInput($defaultValues,$input);               
                    $this->deleteJob($input);
                    break;
                case 'PUT':
                    $input = json_decode($input,true);
                    $input['VDs'] = $input;
                    $input['JobID']=$argc[1];
                    $this->setDefaultValueToInput($defaultValues,$input);               
                    $this->modifyJob($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
    }

    function modifyJob($input)
    {
        $jobC = new JobAction();
        $responsecode = $jobC->modifyJob($input,$output);        
        if ($responsecode != CPAPIStatus::ModifyJobSuccess) {
            $jobC->processJobErrorCode($responsecode);
        }
        else{
            $this->output_json($output);
        }
    }

    function listJobDetail($input)
    {        
        $jobC = new JobAction();
        $responsecode = $jobC->listJobDetail($input,$output);
        if($responsecode != CPAPIStatus::ListJobDetailSuccess){
            $jobC->processJobErrorCode($responsecode);
        }
        else{
            $this->output_json($output);
        }      
    }

    function deleteJob($input)
    {
        $jobC = new JobAction();
        $responsecode = $jobC->deleteJob($input);
        if($responsecode != CPAPIStatus::DeleteJobSuccess)
            $jobC->processJobErrorCode($responsecode);
    }

    function snapshotActionTwoArgc($defaultValues,$argc,$input)
    {        
        if (preg_match('/^snapshot\/[a-z,0-9,-]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {                
                case "DELETE":                    
                    $input = array();
                    $input['LayerID']=$argc[1];
                    $this->setDefaultValueToInput($defaultValues,$input);                   
                    $this->deleteSnapshot($input);
                    break;                
                default:
                    http_response_code(404);
            }
        }        
    }

    function deleteSnapshot($input)
    {                
        $snapshot_c = new SnapshotAction();
        $responsecode = $snapshot_c->createDeleteSnapshotTask($input);        
        if($responsecode != CPAPIStatus::DeleteSnapshotSuccess)
            $snapshot_c->processSnapshotErrorCode($responsecode);
    }

    function snapshotDiskActionTwoArgc($defaultValues,$argc,$input)
    {                        
        if (preg_match('/^snapshot_userdisk\/[a-z,0-9,-]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {                
                case "DELETE":                    
                    $input = array();
                    $input['LayerID']=$argc[1];
                    $this->setDefaultValueToInput($defaultValues,$input);                                     
                    $this->deleteSnapshotDisk($input);
                    break;                
                default:
                    http_response_code(404);
            }
        }        
    }

    function deleteSnapshotDisk($input)
    {                
        if(Test){
            $shell = new SnapshotShellTest();
            $snapshot_c = new SnapshotAction($shell);
        }
        else
            $snapshot_c = new SnapshotAction();
        // $snapshot_c = new SnapshotAction();
        $responsecode = $snapshot_c->createDeleteUPSnapshotTask($input);        
        if($responsecode != CPAPIStatus::DeleteSnapshotSuccess)
            $snapshot_c->processSnapshotErrorCode($responsecode);
    }

    function backupActionTwoArgc($defaultValues,$argc,$input)
    {
        if (preg_match('/^backup\/check$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {                
                case "POST":                    
                    $input = json_decode($input,true);                  
                    $this->setDefaultValueToInput($defaultValues,$input);                   
                    $this->backupBrowse($input);
                    break;                
                default:
                    http_response_code(404);
            }
        }
        else if (preg_match('/^backup\/format$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {                
                case "PUT":                    
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input);                   
                    $this->backupFormat($input);
                    break;                
                default:
                    http_response_code(404);
            }
        }
        else if (preg_match('/^backup\/vd$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {                
                case "GET":                    
                    $input = array();                  
                    $this->setDefaultValueToInput($defaultValues,$input);                   
                    $this->listBackupVD($input);
                    break;                
                default:
                    http_response_code(404);
            }
        }
        else
            http_response_code(404);
    }

    function backupBrowse($input)
    {
        $bkC = new BackupAction();
        $responsecode = $bkC->lunBrowse($input,$output);
        if($responsecode == CPAPIStatus::LUNBrowseSuccess)
            $this->output_json($output);
        else{
            $bkC->processBackupErrorCode($responsecode);
        }
    }

    function backupFormat($input)
    {
        $bkC = new BackupAction();
        $responsecode = $bkC->lunFormat($input);
        if($responsecode != CPAPIStatus::FormatBackupSuccess){       
            $bkC->processBackupErrorCode($responsecode);
        }
    }

    function listBackupVD($input)
    {
        $bkC = new BackupAction();
        $responsecode = $bkC->listBackupVDWithSize($input,$output);
        if($responsecode == CPAPIStatus::ListBackupVDWithSizeSuccess)
            $this->output_json($output);
        else{
            $bkC->processBackupErrorCode($responsecode);
        }
    }

    function diskActionTwoArgc($defaultValues, $argc, $input)
    {        
        if (preg_match('/^disk\/reset$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input,true);
                    $this->setDefaultValueToInput($defaultValues,$input);                    
                    $this->resetDisk($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
        else if (preg_match('/^disk\/initial$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input,true);
                    $data = array();
                    $data['Disks'] = $input;
                    $this->setDefaultValueToInput($defaultValues,$data);                    
                    $this->initialDisk($data);
                    break;
                default:
                    http_response_code(404);
            }
        } 
        else if (preg_match('/^disk\/locate$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input,true);
                    $this->setDefaultValueToInput($defaultValues,$input);                    
                    $this->locateDisk($input);
                    break;
                default:
                    http_response_code(404);
            }
        } 
        else {
            http_response_code(404);
        }
    }

    function initialDisk($input)
    {
        $diskC = new DiskAction();   
        $responsecode = $diskC->initialDisk($input);
        if($responsecode != CPAPIStatus::InitialDiskSuccess)
            http_response_code(400);
    }

    function locateDisk($input)
    {
        $diskC = new DiskAction();
        $diskC->locateDisk($input);   
    }

    function resetDisk($input)
    {
        $diskC = new DiskAction();
        $diskC->resetZfsDisk($input);
    }

    function userActionTwoArgc($defaultValues,$argc,$input)
    {
        if(preg_match('/^user\/[0-9]+$/',$argc[0].'/'.$argc[1]))
        {            
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "DELETE":                              
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $input['UserID']=$argc[1];
                    if(is_null($_GET['vd_delete']))
                        http_response_code(404);
                    $input['vd_delete']=$_GET['vd_delete'];
                    $this->deleteUserAction($input);
                    break;    
                case "PUT":
                    if (!is_null($_GET['disable'])) {
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $input['UserID']=$argc[1];                        
                        $input['Disable']=  ((int)$_GET['disable']) == 1 ? true : false;
                        $this->disableUser($input);
                    } else {
                        $input = json_decode($input,true);
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $input['UserID']=$argc[1];
                        $this->modifyUser($input);
                        // http_response_code(404);
                    }
                    break;                              
                default :
                    http_response_code(404);
            }
        }
        else
            http_response_code(404);
    }
    
    function modifyUser($input)
    {
        $user_c = new UserAction();
        $responsecode = $user_c->modifyUser($input);
        if($responsecode != CPAPIStatus::ModifyUserSuccess)
            $user_c->process_user_error_code($responsecode);
    }

    function disableUser($input)
    {
        $user_c = new UserAction();
        $responsecode = $user_c->disableUser($input);
        if($responsecode != CPAPIStatus::DisableUserSuccess)
            $user_c->process_user_error_code($responsecode);
    }

    function deleteUserAction($input)
    {            
        // var_dump($input);
        $user_c = new UserAction();
        $responsecode = $user_c->deleteUser($input);
        if($responsecode != CPAPIStatus::DeleteUserSuccess)
            $user_c->process_user_error_code($responsecode);
    }           
    
    function cephActionTwoArgc($defaultValues, $argc, $input)
    {
         if (preg_match('/^ceph\/expansion$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input,true);
                    // $data = array();
                    // $data['Disks'] = $input;
                    $this->setDefaultValueToInput($defaultValues,$input);                    
                    $this->expansionRAID($input);
                    break;
                default:
                    http_response_code(404);
            }
        }       
        else if (preg_match('/^ceph\/locate$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input,true);
                    // $data = array();
                    // $data['Disks'] = $input;
                    $this->setDefaultValueToInput($defaultValues,$input);                    
                    $this->locateRAID($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
        else if (preg_match('/^ceph\/[0-9]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $input['RAIDID'] = (int)$argc[1];
                    // $data = array();
                    // $data['Disks'] = $input;
                    $this->setDefaultValueToInput($defaultValues,$input);                    
                    $this->listRAIDDisk($input);
                    break;
                default:
                    http_response_code(404);
            }
        }  
        else {
            http_response_code(404);
        }
    }

    function listRAIDDisk($input)
    {
        $zfsC = new ZFSAction();
        $zfsC->listNormalRaid($input,$output);
        $this->output_json($output);   
    }    

    function locateRAID($input)
    {
        $zfsC = new ZFSAction();
        $zfsC->locateZfs($input);   
    }


    function expansionRAID($input)
    {
        $zfsC = new ZFSAction();
        $zfsC->expansionZfs($input);
    }

    function adActionTwoArgc($defaultValues,$argc,$input)
    {      
        // var_dump($argc);
        if (preg_match('/^ad\/ou$/', $argc[0].'/'.$argc[1])) {            
            switch ($_SERVER['REQUEST_METHOD']) {                
                case "GET":
                    $input = array();  
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->listADOU($input);
                    break;    
                case "POST":
                    $data = json_decode($input,true);  
                    $input = array();
                    $input['SyncOU'] = $data;  
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->setADOU($input);
                    break;                
                default:
                    http_response_code(404);
            }
        }else if (preg_match('/^ad\/import$/', $argc[0].'/'.$argc[1])) {            
            switch ($_SERVER['REQUEST_METHOD']) {                
                case "POST":
                    $data = json_decode($input,true);
                    $input = array();
                    $input['SyncOU'] = $data;  
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->importADUsers($input);
                    break;                    
                default:
                    http_response_code(404);
            }
        }        
        else
            http_response_code(404);
    }

    function setADOU($input)
    {
        $ad_c = new ADAction();
        $responsecode = $ad_c->setOU($input);
        if($responsecode != CPAPIStatus::SetADOUSuccess){
            $ad_c->processADErrorCode($responsecode);
        }
    }

    function importADUsers($input)
    {
        $ad_c = new ADAction();
        $responsecode = $ad_c->importUsers($input);
        if($responsecode != CPAPIStatus::ImportADUserSuccess){
            $ad_c->processADErrorCode($responsecode);
        }
    }

    function listADOU($input)
    {
        $ad_c = new ADAction();
        $responsecode = $ad_c->listOUs($input,$output);        
        if($responsecode == CPAPIStatus::ListADOUSuccess){
            $this->output_json($output);
        }
        else{
            $ad_c->processADErrorCode($responsecode);
        }   
    }

    function update_action_two_argc($argc,$input)
    {
        if(preg_match('/^update\/now$/',$argc[0].'/'.$argc[1]))
        {
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":   
                    $this->update_list_now_version();                              
                    break;                  
                default :
                    http_response_code(404);
            }
        }       
        else if(preg_match('/^update\/new$/',$argc[0].'/'.$argc[1]))
        {
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":                 
                    $this->update_list_new_version();              
                    break;                  
                default :
                    http_response_code(404);
            }
        }              
        else if(preg_match('/^update\/check$/',$argc[0].'/'.$argc[1]))
        {
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":      
                    $this->update_check();                            
                    break;                  
                default :
                    http_response_code(404);
            }
        }   
        else if(preg_match('/^update\/updatenew$/',$argc[0].'/'.$argc[1]))
        {
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "PUT":     
                    $this->update_new();                                         
                    break;                  
                default :
                    http_response_code(404);
            }
        }       
        else if(preg_match('/^update\/updatelocalnew$/',$argc[0].'/'.$argc[1]))
        {
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "PUT":     
                    $this->update_local_new();                                         
                    break;                  
                default :
                    http_response_code(404);
            }
        }            
        else if(preg_match('/^update\/uploadnew$/',$argc[0].'/'.$argc[1]))
        {
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":     
                    $this->update_list_upload_new_version();                                         
                    break;                  
                default :
                    http_response_code(404);
            }
        }    
        else
            http_response_code (404);
    }
    
    function update_list_upload_new_version()
    {
        $oProcSystem = new SystemProcess();
        $VersionData = $oProcSystem->getUploadSoftwareVersion();        
    }

    function update_list_now_version()
    {
        $oProcSystem = new SystemProcess();
        $outputarr = $oProcSystem ->ListVersion();  
        $this->output_json($outputarr);
    }

    function update_list_new_version()
    {
        $oProcSystem = new SystemProcess();
        $VersionData = $oProcSystem->CheckNewVersion();                    
        $this->output_json($VersionData);
    }
    
    function update_check()
    {
        $oProcSystem = new SystemProcess();
        $VersionData = $oProcSystem->CheckUpdate();                    
        $this->output_json($VersionData);
    }

    function update_new()
    {
        $oProcSystem = new SystemProcess();
        $VersionData = $oProcSystem->Update();                    
        $this->output_json($VersionData);
    }

    function update_local_new()
    {
        $oProcSystem = new SystemProcess();
        $VersionData = $oProcSystem->uploadUpdate();                    
        $this->output_json($VersionData);
    }

    function cephActionThreeArgc($defaultValues, $argc, $input)
    {
         if (preg_match('/^unimportable\/[0-9]+$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $input['RAIDID'] = $argc[2];
                    // $data = array();
                    // $data['Disks'] = $input;
                    $this->setDefaultValueToInput($defaultValues,$input);                    
                    $this->listUnimportableRAID($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
        else {
            http_response_code(404);
        }
    }

    function listUnimportableRAID($input)
    {
        $zfsC = new ZFSAction();
        $zfsC->listUnimportableRaid($input,$output);
        $this->output_json($output);
    }

    function acroGatewayActionThreeArgc($defaultValues, $argc, $input)
    {            
        if (preg_match('/^[a-z,0-9,-]+\/ip$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input,true);
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $this->setAcroGatewayIP($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/dhcp$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input,true);
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $this->setAcroGatewayDHCP($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/forwardport$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input,true);
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $this->setAcroGatewayForwardPort($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/vswitch$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $data = array();                
                    $input = json_decode($input,true);
                    $data['Vswitchs'] = $input;
                    $this->setDefaultValueToInput($defaultValues,$data); 
                    $data['VDID'] = $argc[1];
                    $this->modifyAcroGatewayVswitch($data);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }
    
    function modifyAcroGatewayVswitch($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->modifyAcroGatewayVswitchNew($input);
        if ($responsecode != CPAPIStatus::SetAcroGatewayVswitchSuccess) {
            $vdC->processVDErrorCode($responsecode);
        }
    }

    function setAcroGatewayForwardPort($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->setAcroGatewayForwardPort($input);
        if ($responsecode != CPAPIStatus::SetAcroGatewayForwardPortSuccess) {
            $vdC->processVDErrorCode($responsecode);
        }
    }

    function setAcroGatewayDHCP($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->setAcroGatewayDHCP($input);
        if ($responsecode != CPAPIStatus::SetAcroGatewayDHCPSuccess) {
            $vdC->processVDErrorCode($responsecode);
        }
    }

    function setAcroGatewayIP($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->setAcroGatewayIP($input);
        if ($responsecode != CPAPIStatus::SetAcroGatewayIPSuccess) {
            $vdC->processVDErrorCode($responsecode);
        }
    }

    function vdActionThreeArgc($defaultValues, $argc, $input)
    {            
        if (preg_match('/^import\/.+$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $input['Name'] = base64_decode($argc[2]);
                    $this->setDefaultValueToInput($defaultValues,$input);                     
                    $this->listImportVDSize($input);
                    break;
                default:
                    http_response_code(404);
            }
        }    
        else if (preg_match('/^[a-z,0-9,-]+\/node$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $this->setVDPreferNode($input);
                    break;
                default:
                    http_response_code(404);
            }
        } 
        else if (preg_match('/^[a-z,0-9,-]+\/cpu_real$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $this->listVDCPUReal($input);
                    break;
                default:
                    http_response_code(404);
            }
        }  
        else if (preg_match('/^[a-z,0-9,-]+\/cpu_history$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $input['Type']=$_GET['type'];
                    $input['Mode']='cpu';
                    $this->listVDCPUHistory($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
        else if (preg_match('/^[a-z,0-9,-]+\/mem_real$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $this->listVDMemReal($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
        else if (preg_match('/^[a-z,0-9,-]+\/mem_history$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $input['Type']=$_GET['type'];
                    $input['Mode']='mem';
                    $this->listVDMemHistory($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
        else if (preg_match('/^[a-z,0-9,-]+\/nic_real$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $this->listVDNICReal($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
        else if (preg_match('/^[a-z,0-9,-]+\/nic_history$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $input['Type']=$_GET['type'];
                    $input['Mode']='nic';
                    $this->listVDNICHistory($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
        else if (preg_match('/^[a-z,0-9,-]+\/disk_real$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $this->listVDDiskReal($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
        else if (preg_match('/^[a-z,0-9,-]+\/disk_history$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $input['Type']=$_GET['type'];
                    $input['Mode']='disk';
                    $this->listVDDiskHistory($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
        else if (preg_match('/^[a-z,0-9,-]+\/snapshot$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $this->listVDSnapshot($input);
                    break;
                default:
                    http_response_code(404);
            }
        }  
        else if (preg_match('/^[a-z,0-9,-]+\/quickcreate$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['SeedID'] = $argc[1];
                    $this->quickCreate($input);
                    break;
                default:
                    http_response_code(404);
            }
        }      
        else if (preg_match('/^[a-z,0-9,-]+\/disk$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);                  
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $this->addVDDisk($input);
                    break;
                default:
                    http_response_code(404);
            }
        } 
        else if (preg_match('/^[a-z,0-9,-]+\/nic$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);                  
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $this->addVDNIC($input);
                    break;
                default:
                    http_response_code(404);
            }
        }        
        else if (preg_match('/^[a-z,0-9,-]+\/resetpwd$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();                    
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $this->resetVDPassword($input);
                    break;
                default:
                    http_response_code(404);
            }
        } 
        else if (preg_match('/^[a-z,0-9,-]+\/iso$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues,$input);                    
                    $input['VDID'] = $argc[1];
                    $this->modifyVDISO($input);
                    break;
                default:
                    http_response_code(404);
            }
        }         
        else if (preg_match('/^[a-z,0-9,-]+\/free$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();
                    $input['DomainID'] = $idDomain;
                    $input['ID'] = $argc[1];
                    $this->vd_assign_free($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/seed$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $this->seedCreate($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/seed_redeploy$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $this->overwriteSeed($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/clone$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues,$input);         
                    $input['VDID'] = $argc[1];
                    $this->cloneVD($input);
                    break;
                default:
                    http_response_code(404);
            }
        }else if (preg_match('/^[a-z,0-9,-]+\/orgvd$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues,$input);         
                    $input['VDID'] = $argc[1];
                    $this->usertoOrgVD($input);
                    break;
                default:
                    http_response_code(404);
            }
        }else if (preg_match('/^[a-z,0-9,-]+\/uservd$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);    
                    $this->setDefaultValueToInput($defaultValues,$input);                 
                    $input['VDID'] = $argc[1];
                    $this->bornUserVD($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/export$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $input['VDID'] = $argc[1];
                    $this->exportVD($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }
    
    function setVDPreferNode($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->modifyVDPreferNode($input,$output);
        if($responsecode == CPAPIStatus::ModifyVDSuccess){
            $this->output_json($output);
        }else{
            $vdC->processVDErrorCode($responsecode);
        }
    }

    function listVDDiskReal($input)
    {
        $vdPerformanceC = new VDPerformanceAction();
        $vdPerformanceC->listVDDiskRealPerformance($input);
    }

    function listVDDiskHistory($input)
    {
        $vdPerformanceC = new VDPerformanceAction();
        $vdPerformanceC->listVDNICDiskHistoryPerformance($input);
    }

    function listVDNICReal($input)
    {
        $vdPerformanceC = new VDPerformanceAction();
        $vdPerformanceC->listVDNICRealPerformance($input);
    }

    function listVDNICHistory($input)
    {
        $vdPerformanceC = new VDPerformanceAction();
        $vdPerformanceC->listVDNICDiskHistoryPerformance($input);
    }

    function listVDCPUReal($input)
    {
        $vdPerformanceC = new VDPerformanceAction();
        $vdPerformanceC->listVDCPURealPerformance($input);
    }

    function listVDCPUHistory($input)
    {        
        $vdPerformanceC = new VDPerformanceAction();
        $vdPerformanceC->listVDCPUMemHistoryPerformance($input);
    }

    function listVDMemReal($input)
    {
        $vdPerformanceC = new VDPerformanceAction();
        $vdPerformanceC->listVDMemRealPerformance($input);
    }

    function listVDMemHistory($input)
    {        
        $vdPerformanceC = new VDPerformanceAction();
        $vdPerformanceC->listVDCPUMemHistoryPerformance($input);
    }

    function listImportVDSize($input)
    {
        $vdC = new VDAction();
        $vdC->listImportVDSize($input,$output);
        $this->output_json($output);
    }

    function quickCreate($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->quickCreateUserVDUserProfile($input);
        if ($responsecode != CPAPIStatus::BornVDSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        }
    }

    function listVDSnapshot($input)
    {        
        $snapshot_c = new SnapshotAction();        
        $responsecode = $snapshot_c->listSnapshot($input,$output);
        if($responsecode == CPAPIStatus::ListSnapshotSuccess)
            $this->output_json($output);
        else
            $snapshot_c->processSnapshotErrorCode($responsecode);        
    }
    
    function exportVD($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->exportVD($input);
        if ($responsecode != CPAPIStatus::ExportVDSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        }
    }

    function addVDNIC($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->addVDNIC($input);       
        if($responsecode != CPAPIStatus::AddVDNICSuccess)
            $vdC->processVDErrorCode($responsecode);
        else
            echo '{}';
    }

    function addVDDisk($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->addVDDisk($input);       
        if($responsecode != CPAPIStatus::AddVDDiskSuccess)
            $vdC->processVDErrorCode($responsecode);
        else
            echo '{}';
    }
    
    function bornUserVD($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->bornUserVD($input);
        // if ($responsecode != CPAPIStatus::BornVDSuccess) {
        //     $vdC->processVDErrorCode($responsecode);
        // }
        echo '{}';
    }
    
    function cloneVD($input)
    {
        $vdC = new VDAction();
        $vdC->cloneVD($input);
        echo '{}';
    }

    function usertoOrgVD($input)
    {
        $vdC = new VDAction();
        $vdC->userToOriginalVD($input);
    }
    
    function seedCreate($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->createSeedVD($input);
        if ($responsecode != CPAPIStatus::CreateSeedSuccess) {
            $vdC->processVDErrorCode($responsecode);
        }
        else{
            echo '{}';
        }
    }
    
    function resetVDPassword($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->resetVDPassword($input);
        if ($responsecode != CPAPIStatus::ChangePWDSuccess) {
            $vdC->processVDErrorCode($responsecode);
        }
        else
            echo '{}';
    }

    function overwriteSeed($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->overwriteSeedVD($input);
        if ($responsecode != CPAPIStatus::RecreateSeedSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        }
    }
    
    function vd_assign_free($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->vd_assign_to_free($input['ID']);
        if ($responsecode != CPAPIStatus::AssignFreeSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        }
    }

    function modifyVDISO($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->modifyVDISO($input);
        if ($responsecode != CPAPIStatus::ModifyISOSuccess) {
            $vdC->processVDErrorCode($responsecode);
        }
    }    
    
    function jobActionThreeArgc($defaultValues, $argc, $input)
    {
        if (preg_match('/^[0-9]+\/job$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input,true);
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $input['ScheduleID'] = $argc[1];
                    $this->createJob($input);
                    break;
                default:
                    http_response_code(404);
            }
        }         
        else {
            http_response_code(404);
        }
    }

    function createJob($input)
    {
        $jobC = new JobAction();
        $responsecode = $jobC->createJob($input,$output);        
        if ($responsecode != CPAPIStatus::CreateJobSuccess) {
            $jobC->processJobErrorCode($responsecode);
        }
        else{
            $this->output_json($output);
        }
    }

    function snapshotActionThreeArgc($defaultValues, $argc, $input)
    {
        if (preg_match('/^rollback\/[a-z,0-9,-]+$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();                    
                    $input['LayerID'] = $argc[2];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->rollbackSnapshot($input);
                    break;
                default:
                    http_response_code(404);
            }
        } 
        else if (preg_match('/^view\/[a-z,0-9,-]+$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();                    
                    $input['LayerID'] = $argc[2];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->viewSnapshot($input);
                    break;
                default:
                    http_response_code(404);
            }
        } 
        else if (preg_match('/^stop_view\/[a-z,0-9,-]+$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();                    
                    $input['VDID'] = $argc[2];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->stopViewSnapshot($input);
                    break;
                default:
                    http_response_code(404);
            }
        } 
        else {
            http_response_code(404);
        }
    }

    function rollbackSnapshot($input)
    {
        $snapshot_c = new SnapshotAction();
        $responsecode = $snapshot_c->createRollbackSnapshotTask($input);        
        if($responsecode != CPAPIStatus::RollbackSnapshotSuccess)
            $snapshot_c->processSnapshotErrorCode($responsecode);        
    }

    function viewSnapshot($input)
    {
        $snapshot_c = new SnapshotAction();
        $responsecode = $snapshot_c->viewSnapshot($input);        
        if($responsecode != CPAPIStatus::ViewSnapshotSuccess)
            $snapshot_c->processSnapshotErrorCode($responsecode);
    }

    function stopViewSnapshot($input)
    {
        $snapshot_c = new SnapshotAction();
        $responsecode = $snapshot_c->stopViewSnapshot($input);        
        if($responsecode != CPAPIStatus::StopViewSnapshotSuccess)
            $snapshot_c->processSnapshotErrorCode($responsecode);   
    }

    function snapshotDiskActionThreeArgc($defaultValues, $argc, $input)
    {
        if (preg_match('/^rollback\/[a-z,0-9,-]+$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();                    
                    $input['LayerID'] = $argc[2];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->rollbackDiskSnapshot($input);
                    break;
                default:
                    http_response_code(404);
            }
        } 
        else if (preg_match('/^view\/[a-z,0-9,-]+$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();                    
                    $input['LayerID'] = $argc[2];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->viewDiskSnapshot($input);
                    break;
                default:
                    http_response_code(404);
            }
        } 
        else if (preg_match('/^stop_view\/[0-9]+$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();                    
                    $input['UserID'] = $argc[2];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->stopViewDiskSnapshot($input);
                    break;
                default:
                    http_response_code(404);
            }
        } 
        else {
            http_response_code(404);
        }
    }

    function viewDiskSnapshot($input)
    {
        if(Test){
            $shell = new SnapshotShellTest();
            $snapshot_c = new SnapshotAction($shell);
        }
        else
            $snapshot_c = new SnapshotAction();
        $responsecode = $snapshot_c->viewSnapshotUP($input);        
        if($responsecode != CPAPIStatus::ViewSnapshotSuccess)
            $snapshot_c->processSnapshotErrorCode($responsecode);
    }

    function stopViewDiskSnapshot($input)
    {
        if(Test){
            $shell = new SnapshotShellTest();
            $snapshot_c = new SnapshotAction($shell);
        }
        else
            $snapshot_c = new SnapshotAction();
        $responsecode = $snapshot_c->stopViewUPSnapshot($input);        
        if($responsecode != CPAPIStatus::StopViewSnapshotSuccess)
            $snapshot_c->processSnapshotErrorCode($responsecode);   
    }

    function rollbackDiskSnapshot($input)
    {
        if(Test){
            $shell = new SnapshotShellTest();
            $snapshot_c = new SnapshotAction($shell);
        }
        else
            $snapshot_c = new SnapshotAction();        
        $responsecode = $snapshot_c->createRollbackUPSnapshotTask($input);        
        if($responsecode != CPAPIStatus::RollbackSnapshotSuccess)
            $snapshot_c->processSnapshotErrorCode($responsecode);
    }

    function backupActionThreeArgc($defaultValues, $argc, $input)
    {                
        if (preg_match('/^vd\/[a-z,0-9,-]+$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "DELETE":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['BkVDID'] = $argc[2];
                    $this->deleteBackupVD($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
        else if (preg_match('/^userdisk\/.+$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "DELETE":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['BackupDiskName'] = $argc[2];
                    $this->deleteBackupUP($input);
                    break;
                default:
                    http_response_code(404);
            }
        } 
        else {
            http_response_code(404);
        }
    }

    function deleteBackupVD($input)
    {
        $backupC = new BackupAction();
        $responsecode = $backupC->backupManagerDelete($input);
        if($responsecode != CPAPIStatus::DeleteBackupVDSuccess)
            $backupC->processBackupErrorCode($responsecode);
    }    

    function deleteBackupUP($input)
    {
        $backupC = new BackupAction();
        $responsecode = $backupC->backupUPDelete($input);
        if($responsecode != CPAPIStatus::DeleteBackupVDSuccess)
            $backupC->processBackupErrorCode($responsecode);
    }    
    
    function userActionThreeArgc($defaultValues, $argc,$input)
    {
        // var_dump($argc);
        if(preg_match('/^[0-9]+\/resetpwd$/',$argc[1].'/'.$argc[2]))
        {
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "PUT":                    
                    $input = json_decode($input,true);                  
                    $this->setDefaultValueToInput($defaultValues,$input);   
                    $input['UserID'] = (int)$argc[1];
                    $this->resetUserPassword($input);                                        
                    break;                
                default :
                    http_response_code(404);
            }
        }        
        else if (preg_match('/^[0-9]+\/profile$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $input['UserID'] = (int)$argc[1];
                    $this->createUserProfileAction($input);
                    break;
                case "DELETE":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input);                    
                    $input['UserID'] = (int)$argc[1];
                    $this->deleteUserProfileAction($input);
                    break;
                case 'PUT':
                    $input = json_decode($input,true);
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $input['UserID'] = (int)$argc[1];
                    $this->modifyUserProfileAction($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
        else if (preg_match('/^[0-9]+\/detail$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues,$input);         
                        $input['UserID']=$argc[1];                        
                        $this->listUserDetail($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
        else if (preg_match('/^[0-9]+\/snapshot$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['UserID'] = (int)$argc[1];
                    $this->listUserDiskSnapshot($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
        else
        {
            http_response_code(404);
        }
    }

    function listUserDiskSnapshot($input)
    {
        $snapshot_c = new SnapshotAction();        
        $responsecode = $snapshot_c->listUserDiskSnapshot($input,$output);
        if($responsecode == CPAPIStatus::ListSnapshotSuccess)
            $this->output_json($output);
        else
            $snapshot_c->processSnapshotErrorCode($responsecode);       
    }
    
    function listUserDetail($input)
    {
        $user_c = new UserAction();
        $responsecode = $user_c->listUserInfo($input, $output);
        if ($responsecode != CPAPIStatus::ListUserInfoSuccess) {
            $user_c->process_user_error_code($responsecode);
        } else {
            $this->output_json($output);
        }
    }

    function resetUserPassword($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->resetUserPassword($input);
        if ($responsecode != CPAPIStatus::ChangePWDSuccess) {
            $vdC->processVDErrorCode($responsecode);
        }   
    }

    function modifyUserProfileAction($input)
    {
        $user_c = new UserAction();
        $responsecode = $user_c->modifyUserProfileDisk($input);
        if ($responsecode != CPAPIStatus::ModifyUserProfileSuccess) {
            $user_c->process_user_error_code($responsecode);
        }
    }
    
    function deleteUserProfileAction($input)
    {
        $user_c = new UserAction();
        $responsecode = $user_c->deleteUserProfileDisk($input);
        if ($responsecode != CPAPIStatus::DeleteUserProfileSuccess) {
            $user_c->process_user_error_code($responsecode);
        }
    }
    
    function createUserProfileAction($input)
    {
        $user_c = new UserAction();
        $responsecode = $user_c->createUserProfileDisk($input);
        if ($responsecode != CPAPIStatus::CreateUserProfileSuccess) {
            $user_c->process_user_error_code($responsecode);
        }
    }

    function seedActionThreeArgc($defaultValues, $argc, $input)
    {        
        if (preg_match('/^[a-z,0-9,-]+\/distribute$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $input['SeedID'] = $argc[1];
                    $input['Renew'] = $_GET['renew'] == 1 ? true : false;
                    $this->seedRebornAction($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/orgvd$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues,$input);         
                    $input['VDID'] = $argc[1];
                    $this->seedtoOrgVD($input);
                    break;
                default:
                    http_response_code(404);
            }
        }else {
            http_response_code(404);
        }
    }
    
    function seedtoOrgVD($input)
    {
        $vdC = new VDAction();
        $vdC->seedToOriginalVD($input);   
    }

    function seedRebornAction($input)
    {
        // var_dump($input);
        $vd_c = new VDAction();
        $vd_c->seedRebornUserVD($input);
    }

    function userVDActionThreeArgc($defaultValues, $argc, $input)
    {
        if (preg_match('/^[a-z,0-9,-]+\/recovery$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $input['VDID'] = $argc[1];
                    $this->recoveryUserVD($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }
    
    function recoveryUserVD($input)
    {
        $vd_c = new VDAction();
        $vd_c->recoveryUserVD($input);
    }

    function vdActionFourArgc($defaultValues,$argc,$input)
    {              
        if (preg_match('/^[a-z,0-9,-]+\/restore\/check$/', $argc[1].'/'.$argc[2].'/'.$argc[3])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['BackupVDID'] = $argc[1];                                        
                    $this->checkRestoreVD($input);
                    break;
                default:
                    http_response_code(404);
            }
        }     
        else if (preg_match('/^[a-z,0-9,-]+\/user\/[0-9]+$/', $argc[1].'/'.$argc[2].'/'.$argc[3])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $input['UserID'] = (int)$argc[3];
                    $this->vdAssignUser($input);
                    break;
                default:
                    http_response_code(404);
            }
        }          
        else if(preg_match('/^[a-z0-9-]+\/disk\/[0-9]+$/',$argc[1].'/'.$argc[2].'/'.$argc[3]))
        {
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "DELETE":                                        
                    $input = array();                    
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $input['DiskID'] = $argc[3];
                    $this->deleteVDDisk($input);
                    break;  
                case 'PUT':
                    $input = json_decode($input,true);
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $input['DiskID'] = $argc[3];
                    $this->modifyVDDisk($input);
                    break;         
                default :
                    http_response_code(404);
            }
        }      
        else if(preg_match('/^[a-z0-9-]+\/disk\/move+$/',$argc[1].'/'.$argc[2].'/'.$argc[3]))
        {
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case 'PUT':
                    $input = json_decode($input,true);
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];                    
                    $this->moveVDDisk($input);
                    break;         
                default :
                    http_response_code(404);
            }
        } 
        else if(preg_match('/^[a-z0-9-]+\/nic\/[0-9a-z:]+$/',$argc[1].'/'.$argc[2].'/'.$argc[3]))
        {
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "DELETE":                                        
                    $input = array();                    
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['VDID'] = $argc[1];
                    $input['MAC'] = $argc[3];
                    $this->deleteVDNIC($input);
                    break;                
                default :
                    http_response_code(404);
            }
        }     
        else
            http_response_code(404);
    }

    function checkRestoreVD($input)
    {
        $backupC = new BackupAction();
        $responsecode = $backupC->checkRestore($input,$output);       
        if($responsecode != CPAPIStatus::RestoreSuccess)
            $backupC->processBackupErrorCode($responsecode);
        else
            $this->output_json($output);
    }

    function moveVDDisk($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->moveVDDisk($input);       
        if($responsecode != CPAPIStatus::MoveVDDiskSuccess)
            $vdC->processVDErrorCode($responsecode);
    }

    function modifyVDDisk($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->modifyVDDisk($input);       
        if($responsecode != CPAPIStatus::ModifyVDDiskSuccess)
            $vdC->processVDErrorCode($responsecode);
    }

    function vdAssignUser($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->vdAssigntoUser($input);
        if ($responsecode != CPAPIStatus::AssignUserSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        }
    }

    function deleteVDNIC($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->deleteVDNIC($input);       
        if($responsecode != CPAPIStatus::DeleteVDNICSuccess)
            $vdC->processVDErrorCode($responsecode);
        else
            echo '{}';   
    }

    function deleteVDDisk($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->deleteVDDisk($input);       
        if($responsecode != CPAPIStatus::DeleteVDDiskSuccess)
            $vdC->processVDErrorCode($responsecode);
        else
            echo '{}';   
    } 

    function userActionFourArgc($defaultValues,$argc,$input)
    {
        if(preg_match('/^[a-z0-9-]+\/disk\/move+$/',$argc[1].'/'.$argc[2].'/'.$argc[3]))
        {
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case 'PUT':
                    $input = json_decode($input,true);
                    $this->setDefaultValueToInput($defaultValues,$input); 
                    $input['UserID'] = $argc[1];                    
                    $this->moveUserProfile($input);
                    break;         
                default :
                    http_response_code(404);
            }
        } 
    }

    function moveUserProfile($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->moveUserProfile($input);
        if($responsecode != CPAPIStatus::MoveVDDiskSuccess)
            $vdC->processVDErrorCode($responsecode);
    }
}