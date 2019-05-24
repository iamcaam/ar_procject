<?php
define("DEBUG", false);
// ini_set('display_errors', 'On');
include_once("/var/www/html/libraries/BaseAPI.php");
include_once("/var/www/html/libraries/ConnectDB.php");
include_once("/var/www/html/libraries/ErrorEnum.php");
include_once("/var/www/html/libraries/HandleLogin.php");
include_once("/var/www/html/libraries/HandleTask.php");
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
include_once("/var/www/html/libraries/HandleAD.php");

class API extends BaseAPI
{
    function __construct()
    {
        // $this->getStorageType();
        $api = $_GET['api'];
        if (DEBUG) {
            $data = file_get_contents("php://input");
            if ($_SERVER['REQUEST_METHOD'] != "GET") {
                $fp = fopen('/mnt/tmpfs/cephcmd/cmd.txt', 'a');
                $gmt = $this->getTZ($timezone);
                date_default_timezone_set($timezone);
                fwrite($fp, date("Y-m-d H:i:s").' '.$api.' '.$_SERVER['REMOTE_ADDR'].PHP_EOL);
                $curlExec="curl -o /dev/null -s -w \"%{http_code}\\n}\" -k --request ".$_SERVER['REQUEST_METHOD']." https://".$_SERVER['SERVER_ADDR']."/manager/$api ";
                if (strlen($data) > 0) {
                    $curlExec .= "--data '$data'";
                }
                fwrite($fp, $curlExec.PHP_EOL);
                // fwrite($fp, $data.PHP_EOL);
                fclose($fp);
            }
            // $fp = fopen('/mnt/tmpfs/cephcmd/cmd.txt', 'a');
            // $gmt = $this->getTZ($timezone);
            // date_default_timezone_set($timezone);
            // fwrite($fp, date("Y-m-d H:i:s").' '.$api.' '.$_SERVER['REMOTE_ADDR'].PHP_EOL);
            // $data = file_get_contents("php://input");
            // fwrite($fp, $data.PHP_EOL);
            // fclose($fp);
        }
        $apiArr = explode('/', $api);
        $count = count($apiArr);
        switch ($count) {
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
            default:
                http_response_code(404);
        }
    }
    
    function binaryToHex2Str($bin_data)
    {
        $hex = '';
        foreach (str_split($bin_data) as $bin) {
            $hex .= sprintf("%02x", ord($bin));
        }
        return trim($hex);
    }
    
    function handleOneArgc($api)
    {
        $data = file_get_contents("php://input");        
        switch ($api[0]) {
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
                $this->loginCheck($defaultValues);
                $this->timeActionOneArgc($data);
                break;
            case 'user':
                $this->loginCheck($defaultValues);
                $this->userActionOneArgc($defaultValues, $data);
                break;
            case 'vd':
            case 'vd_size':
                $this->loginCheck($defaultValues);
                $this->vdActionOneArgc($defaultValues, $data, $api[0]);
                break;
            case 'schedule':
                $this->loginCheck($defaultValues);
                $this->scheduleActionOneArgc($defaultValues, $data);
                break;
            case 'snapshot':
                $this->loginCheck($defaultValues);
                $this->snapshotActionOneArgc($defaultValues, $data);
                break;
            case 'task':
                $this->loginCheck($defaultValues);
                $this->taskActionOneArgc($defaultValues, $data);
                break;
            case 'domain':
                $this->loginCheck();
                $this->domain_action_one_argc($data);
                break;
            case 'log':
                $this->loginCheck($defaultValues);
                $this->logActionOneArgc($data);
                break;
            case 'log_by_id':
                $this->loginCheck($defaultValues);
                $this->logByIDActionOneArgc($data);
                break;
            case 'system':
                $this->loginCheck($defaultValues);
                $this->systemActionOneArgc($defaultValues, $data, $api[0]);
                break;
            case 'ups':
                $this->loginCheck($defaultValues);
                $this->systemActionOneArgc($defaultValues, $data, $api[0]);
                break;
            case 'mail':
                $this->loginCheck($defaultValues);
                $this->systemActionOneArgc($defaultValues, $data, $api[0]);
                break;
            case 'system_util':
                $this->loginCheck($defaultValues);
                $this->systemActionOneArgc($defaultValues, $data, $api[0]);
                break;
            case 'alarm':
                $this->loginCheck($defaultValues);
                $this->systemActionOneArgc($defaultValues, $data, $api[0]);
                break;
            case 'smb':
                $this->loginCheck($defaultValues);
                $this->systemActionOneArgc($defaultValues, $data, $api[0]);
                break;
            case 'smb_pwd':
                $this->loginCheck($defaultValues);
                $this->systemActionOneArgc($defaultValues, $data, $api[0]);
                break;
            case 'hostname':
                $this->loginCheck($defaultValues);
                $this->systemActionOneArgc($defaultValues, $data, $api[0]);
                break;
            case 'dropcache':
                $this->loginCheck($defaultValues);
                $this->systemActionOneArgc($defaultValues, $data, $api[0]);
                break;
            case 'ssh':
                $this->loginCheck($defaultValues);
                $this->systemActionOneArgc($defaultValues, $data, $api[0]);
                break;
            case 'vswitch':
                $this->loginCheck($defaultValues);
                $this->vSwitchActionOneArgc($defaultValues, $data);
                break;
            case 'external':
                $this->loginCheck($defaultValues);
                $this->externalIPActionOneArgc($defaultValues, $data);
                break;
            case 'disk':
                $this->loginCheck($defaultValues);
                $this->diskCephActionOneArgc($defaultValues, $data, $api[0]);
                break;
            case 'ceph':
                $this->loginCheck($defaultValues);
                $this->diskCephActionOneArgc($defaultValues, $data, $api[0]);
                break;
            case 'backup':
                $this->loginCheck($defaultValues);
                $this->backupActionOneArgc($defaultValues, $data);
                break;
            case 'ad':
                $this->loginCheck($defaultValues);
                $this->adActionOneArgc($defaultValues, $data);
                break;
            case 'check':
                if (isset($_GET['token'])) {
                    $defaultValues['token'] = $_GET['token'];
                }
                $this->loginCheck($defaultValues);
                $this->diskCephActionOneArgc($defaultValues, $data, $api[0]);
                break;
            case 'reboot':
                if ($_SERVER['REQUEST_METHOD'] == "PUT") {
                    $this->loginCheck($defaultValues);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $systemC = new SystemProcess();
                    $systemC->reboot($input);
                }
                break;
            case 'shutdown':
                if ($_SERVER['REQUEST_METHOD'] == "PUT") {
                    $this->loginCheck($defaultValues);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $systemC = new SystemProcess();
                    $systemC->shutdown($input);
                }
                break;
            default:
                http_response_code(404);
        }
    }
    
    function handleTwoArgc($api)
    {
        $data = file_get_contents("php://input");
        switch ($api[0]) {
            case 'iscsi':
                $this->loginCheck();
                $this->iscsi_action_two_argc($api[1], $data);
                break;
            case 'cluster':
                $this->loginCheck();
                $this->cluster_action_two_argc($api, $data);
                break;
            case 'target':
                $this->loginCheck();
                $this->target_action_two_argc($api[1], $data);
                break;
            case 'vd':
                $this->loginCheck($defaultValues);
                $this->vdActionTwoArgc($defaultValues, $api, $data);
                break;
            case 'seed':
                $this->loginCheck($defaultValues);
                $this->vdActionTwoArgc($defaultValues, $api, $data);
                break;
            case 'schedule':
                $this->loginCheck($defaultValues);
                $this->scheduleActionTwoArgc($defaultValues, $api, $data);
                break;
            case 'job':
                $this->loginCheck($defaultValues);
                $this->jobActionTwoArgc($defaultValues, $api, $data);
                break;
            case 'snapshot':
                $this->loginCheck($defaultValues);
                $this->snapshotActionTwoArgc($defaultValues, $api, $data);
                break;
            case 'user':
                $this->loginCheck($defaultValues);
                $this->userActionTwoArgc($defaultValues, $api, $data);
                break;
            case 'domain':
                $this->loginCheck();
                $this->domain_action_two_argc($api, $data);
                break;
            case 'task':
                $this->loginCheck($defaultValues);
                $this->taskActionTwoArgc($defaultValues, $api, $data);
                break;
            case 'update':
                $this->loginCheck($default_values);
                $this->update_action_two_argc($api, $data);
                break;
            case 'backup':
                $this->loginCheck($default_values);
                $this->backupActionTwoArgc($default_values, $api, $data);
                break;
            case 'disk':
            case 'ceph':
                $this->loginCheck($default_values);
                $this->diskActionTwoArgc($default_values, $api, $data);
                break;
            case 'mail':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->mailActionTwoArgc($default_values,$api,$data);
                break;                
            default:
                http_response_code(404);
        }
    }
    
    function handleThreeArgc($api)
    {
        // var_dump($api);
        $data = file_get_contents("php://input");
        switch ($api[0]) {
            case 'vd':
                $this->loginCheck($defaultValues);
                $this->vdActionThreeArgc($defaultValues, $api, $data);
                break;
            case 'backup':
                $this->loginCheck($defaultValues);
                $this->backupActionThreeArgc($defaultValues, $api, $data);
                break;
            case 'schedule':
                $this->loginCheck($defaultValues);
                $this->jobActionThreeArgc($defaultValues, $api, $data);
                break;
            case 'snapshot':
                $this->loginCheck($defaultValues);
                $this->snapshotActionThreeArgc($defaultValues, $api, $data);
                break;
            case 'domain':
                $this->loginCheck();
                $this->domain_action_three_argc($api, $data);
                break;
            case 'resource':
                $this->resource_action_three_argc($api, $data);
                break;
            case 'user':
                $this->loginCheck($defaultValues);
                $this->userActionThreeArgc($defaultValues, $api, $data);
                break;
            case 'update':
                $this->loginCheck();
                $this->update_action_three_argc($api, $data);
                break;
            case 'seed':
                $this->loginCheck($defaultValues);
                $this->seedActionThreeArgc($defaultValues, $api, $data);
                break;
            case 'uservd':
                $this->loginCheck($defaultValues);
                $this->userVDActionThreeArgc($defaultValues, $api, $data);
                break;
            case 'ceph':
                $this->loginCheck($defaultValues);
                $this->cephActionThreeArgc($defaultValues, $api, $data);
                break;
            default:
                http_response_code(404);
        }
    }
    
    function handleFourArgc($api)
    {
        $data = file_get_contents("php://input");
        switch ($api[0]) {
            case 'vd':
                $this->loginCheck($defaultValues);
                $this->vdActionFourArgc($defaultValues, $api, $data);
                break;
            case 'cluster':
                $this->loginCheck();
                $this->cluster_action_four_argc($api, $data);
                break;
            case 'resource':
                $this->loginCheck();
                $this->resource_action_four_argc($api, $data);
                break;
            case 'user':
                $this->loginCheck($defaultValues);
                $this->userActionFourArgc($defaultValues, $api, $data);
                break;
            default:
                http_response_code(404);
        }
    }
    
    function login($data)
    {
        $oLogin = new Login();
        $data = json_decode($data, true);
        $estatus = APIStatus::LoginFail;
        $pwd =  base64_decode($data['Password']);
        $pwd = $this->binaryToHex2Str($pwd);
        if (isset($data['Username'])) {
            if ($data['Username'] == 'admin') {
                $status = $oLogin->Login_By_Data_Web($data['Username'], $pwd);
            }
        }
        if ($status != APIStatus::LoginSuccess) {
            http_response_code(401);
        }
    }
    
    function handleAdminChangePWD($data)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "POST":
                $data = json_decode($data, true);
                $oLogin = new Login();
                $eLoginStatus = $oLogin->Change_Password($data['OldPWD'], $data['PWD']);
                if ($eLoginStatus == APIStatus::ChangePWDOldPWDFail) {
                    echo json_encode(array('status' => 80));
                    exit();
                }
                echo '{}';
                break;
            default:
                http_response_code(404);
        }
    }
   
    function loginCheck(&$defaultValues)
    {
        $oLogin = new Login();
        $oLogin->Sec_Session_Start();
        $this->getDefaultCephDomain($defaultValues);
        if(file_exists('/mnt/tmpfs/ssh.enable')){                
            $oLogin->Login_Check($defaultValues);
        }
        else{
            if ($oLogin->Login_Check($defaultValues) != APIStatus::LoginSuccess) {
                http_response_code(401);
                exit();
            }
        }
        // $oLogin->Login_Check();
        return APIStatus::LoginSuccess;
    }
    
    function getDefaultCephDomain(&$defaultValues)
    {
        // unset($_SESSION['DomainID']);
        // unset($_SESSION['CephID']);
        // unset($_SESSION['ConnectIP']);
        if (!$this->connectDB()) {
            unset($_SESSION['DomainID']);
            unset($_SESSION['CephID']);
            unset($_SESSION['ConnectIP']);
            unset($_SESSION['NodeID']);
            $defaultValues['DomainID'] = null;
            $defaultValues['CephID'] = null;
            $defaultValues['ConnectIP'] = '127.0.0.1';
        } else {
            if (!isset($_SESSION['DomainID'])) {
                $domainC = new DomainAction();
                if ($domainC->listDefaultDomain($output_default_domain)) {
                    // var_dump($output_default_domain);
                    $ceph_c = new CephPoolAction();
                    if ($ceph_c->list_default_one_ceph_and_server($output_default_ceph)) {
                        // var_dump($output_default_ceph);
                        $_SESSION['DomainID'] = $output_default_domain['DomainID'];
                        $_SESSION['CephID'] = $output_default_ceph['CephID'];
                        $_SESSION['NodeID'] = $output_default_ceph['NodeID'];
                        // $_SESSION['ConnectIP'] = $output_default_ceph['NodeAddress'];
                        $_SESSION['ConnectIP'] =  '127.0.0.1';
                    }
                }
            }
            // var_dump($_SESSION);
            $defaultValues['DomainID'] = $_SESSION['DomainID'];
            $defaultValues['CephID'] = $_SESSION['CephID'];
            $defaultValues['ConnectIP'] = $_SESSION['ConnectIP'];
            $defaultValues['NodeID'] = $_SESSION['NodeID'];
        }
    }

    function adActionOneArgc($defaultValues, $input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $input = array();
                $this->setDefaultValueToInput($defaultValues, $input);
                $this->listAD($input);
                break;
            case "POST":
                $input = json_decode($input, true);
                // var_dump($input);
                $this->setDefaultValueToInput($defaultValues, $input);
                $this->setAD($input);
                break;
            default:
                http_response_code(404);
        }
    }

    function setAD($input)
    {
        $ad_c = new ADAction();
        $responsecode = $ad_c->addADConfigure($input);
        if ($responsecode != CPAPIStatus::AddDomainADAuthSuccess) {
            $ad_c->processADErrorCode($responsecode);
        }
    }

    function listAD($input)
    {
        $ad_c = new ADAction();
        $responsecode = $ad_c->listADConfigure($input['DomainID'], $output);
        if ($responsecode == CPAPIStatus::ListDomainADConfigSuccess) {
            $this->output_json($output);
        } else {
            $ad_c->processADErrorCode($responsecode);
        }
    }

    function userActionOneArgc($defaultValues, $input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $input = array();
                $this->setDefaultValueToInput($defaultValues, $input);
                $this->listUser($input);
                break;
            case "POST":
                $input = json_decode($input, true);
                $this->setDefaultValueToInput($defaultValues, $input);
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
        // var_dump($input);
        $responsecode = $userC->listUser($input['DomainID'], $output);
        if ($responsecode == CPAPIStatus::ListUserofDomainSuccess) {
            $this->output_json($output);
        } else {
            $userC->process_user_error_code($responsecode);
        }
    }

    function scheduleActionOneArgc($defaultValues, $input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $input = array();
                $this->setDefaultValueToInput($defaultValues, $input);
                $this->listSchedule($input);
                break;
            case "POST":
                $input = json_decode($input, true);
                $this->setDefaultValueToInput($defaultValues, $input);
                $this->createSchedule($input);
                break;
            default:
                http_response_code(404);
        }
    }

    function listSchedule($input)
    {
        $scheduleC = new ScheduleAction();
        $responsecode = $scheduleC->listSchedule($input, $output);
        if ($responsecode != CPAPIStatus::ListScheduleSuccess) {
            $scheduleC->processScheduleErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }

    function createSchedule($input)
    {
        $scheduleC = new ScheduleAction();
        $responsecode = $scheduleC->createSchedule($input, $output);
        if ($responsecode != CPAPIStatus::CreateScheduleSuccess) {
            $scheduleC->processScheduleErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }

    function backupActionOneArgc($defaultValues, $input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $input = array();
                $this->setDefaultValueToInput($defaultValues, $input);
                $this->listBackup($input);
                break;
            case "POST":
                $input = json_decode($input, true);
                $this->setDefaultValueToInput($defaultValues, $input);
                $this->setBackup($input);
                break;
            case "DELETE":
                $input = array();
                $this->setDefaultValueToInput($defaultValues, $input);
                $this->deleteBackup($input);
                break;
            default:
                http_response_code(404);
        }
    }

    function listBackup($input)
    {
        $backupC = new BackupAction();
        $backupC->backupList($input, $output);
        $this->output_json($output);
    }

    function setBackup($input)
    {
        $backupC = new BackupAction();
        $responsecode = $backupC->setBackupMapping($input, $output);
        if ($responsecode == CPAPIStatus::SetBackupMappingSuccess) {
            $this->output_json($output);
        } else {
            $backupC->processBackupErrorCode($responsecode);
        }
    }

    function deleteBackup($input)
    {
        $backupC = new BackupAction();
        $responsecode = $backupC->backupDelete($input);
        if ($responsecode != CPAPIStatus::DeleteBackupMappingSuccess) {
            $backupC->processBackupErrorCode($responsecode);
        }
    }

    function timeActionOneArgc($data)
    {
        // var_dump($_SERVER['REQUEST_METHOD']);
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $this->listTime();
                break;
            case "POST":
                $data = json_decode($data, true);
                $this->setTime($data);
                break;
            case "PUT":
                $data = json_decode($data, true);
                $this->updateTime($data);
                break;
            default:
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
    
    function vdActionOneArgc($defaultValues, $input, $api)
    {        
        switch ($_SERVER['REQUEST_METHOD']) {
            case "POST":                
                $input = json_decode($input, true);                
                $this->setDefaultValueToInput($defaultValues, $input);
                $this->createVD($input);
                break;
            case "GET":
                switch ($api) {
                    case 'vd':
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->listVD($input);
                        break;
                    case 'vd_size':
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->listVDWithSize($input);
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

    function listVDWithSize($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->listVDWithSize($input, $output);
        if ($responsecode != CPAPIStatus::ListVDofCephSuccess) {
            $vdC->processVDErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }

    function listVD($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->listVD($input, $output);
        if ($responsecode != CPAPIStatus::ListVDofCephSuccess) {
            $vdC->processVDErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }

    function createVD($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->createVD($input, $output);
        if ($responsecode != CPAPIStatus::CreateVDofCephSuccess) {
            $vdC->processVDErrorCode($responsecode);
        } else {
            echo '{}';
        }
    }

    function snapshotActionOneArgc($defaultValues, $input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "POST":
                $input = json_decode($input, true);
                $this->setDefaultValueToInput($defaultValues, $input);
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
        if ($responsecode != CPAPIStatus::TakeSnapshotSuccess) {
            $snap_c->processSnapshotErrorCode($responsecode);
        }
    }

    function taskActionOneArgc($defaultValues, $input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $input = array();
                $this->setDefaultValueToInput($defaultValues, $input);
                $this->listTask($input);
                break;
            case "DELETE":
                $input = array();
                $this->setDefaultValueToInput($defaultValues, $input);
                $this->clearTask($input);
                break;
            default:
                http_response_code(404);
        }
    }
    
    function listTask($input)
    {
        $taskC = new TaskAction();
        $responsecode = $taskC->listTask($input, $output);
        if ($responsecode != CPAPIStatus::ListTaskSuccess) {
            $taskC->processTaskErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }
    
    function clearTask($input)
    {
        $task_c = new TaskAction();
        $task_c->clearTask($input);
        // if($responsecode != CPAPIStatus::ClearTaskSuccess)
        //     $cluseter_c->process_task_error_code($responsecode);
    }
    
    function domain_action_one_argc($input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "POST":
                $domain_c = new DomainAction();
                $input = json_decode($input, true);
                $responsecode = $domain_c->create_domain($input, $output);
                if ($responsecode != CPAPIStatus::CreateDomainSuccess) {
                    $domain_c->process_domain_error_code($responsecode);
                } else {
                    $this->output_json($output);
                }
                break;
            case "GET":
                $domain_c = new DomainAction();
                $responsecode = $domain_c->list_domain($output);
                if ($responsecode != CPAPIStatus::ListDomainSuccess) {
                    $domain_c->process_domain_error_code($responsecode);
                } else {
                    $this->output_json($output);
                }
                break;
            default:
                http_response_code(404);
        }
    }
    
    function externalIPActionOneArgc($defaultValues, $input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $input = array();
                $this->setDefaultValueToInput($defaultValues, $input);
                $this->listExternal($input);
                break;
            case "POST":
                $data = array();
                $data['Externals'] = json_decode($input, true);
                $this->setDefaultValueToInput($defaultValues, $data);
                $this->setExternal($data);
                break;
            default:
                http_response_code(404);
        }
    }

    function listExternal($input)
    {
        $ipC = new IPProcess();
        $ipC->listNodeExternal($input, $output);
        $this->output_json($output);
    }

    function setExternal($input)
    {
        $ipC = new IPProcess();
        $responsecode = $ipC->setNodeExternal($input, $output);
        if ($responsecode != CPAPIStatus::SetNodeExternalSuccess) {
            $ipC->processNetworkErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }

    function diskCephActionOneArgc($defaultValues, $input, $api)
    {
        switch ($api) {
            case 'disk':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case "GET":
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->listDisk($input);
                        break;
                    default:
                        http_response_code(404);
                }
                break;
            case 'ceph':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case "GET":
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->listCeph($input);
                        break;
                    case "POST":
                        $input = json_decode($input, true);
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->createCeph($input);
                        break;
                    case "DELETE":
                        $input = json_decode($input, true);
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->deleteCeph($input);
                        break;
                    default:
                        http_response_code(404);
                }
                break;
            case 'check':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case "GET":
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues, $input);
                        // if($this->isCeph)
                        //     $this->check($input);
                        // else
                            $this->checkZfs($input);
                        break;
                    default:
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
            $zfsC->listZfs($input, $output);
        // }
        if (!is_array($output)) {
            echo '{}';
        } else {
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
        $cephC->listCeph($input, $outputCeph);
        $output['Reboot'] = false;
        if (isset($outputCeph)) {
            if (count($outputCeph > 0)) {
                if ($outputCeph[0]['Total'] > 0 && $outputCeph[0]['Used'] >0) {
                    $cephTotal = $outputCeph[0]['Total'];
                    $cephUsed = $outputCeph[0]['Used'];
                    $cephUsage = bcdiv($outputCeph[0]['Used'], $outputCeph[0]['Total'], 4)*100;
                }
            }
        }
        if (isset($outputCeph['Reboot'])) {
            $output['Reboot'] = $outputCeph['Reboot'];
        }
        $this->listRAIDUsage($input, $outputUsage);
        $systemC = new SystemProcess();
        $systemC->listAlarm($input, $output);
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
        // var_dump($outputCeph);
        $output['Reboot'] = false;
        if (isset($outputCeph)) {
            // if(count($outputCeph > 0)){
            //     if($outputCeph[0]['Total'] > 0 && $outputCeph[0]['Used'] >0){
            //         $cephTotal = $outputCeph[0]['Total'];
            //         $cephUsed = $outputCeph[0]['Used'];
            //         $cephUsage = bcdiv($outputCeph[0]['Used'], $outputCeph[0]['Total'],4)*100;
            //     }
            // }
            $output['Ceph'] = $outputCeph;
            // var_dump($output);
        }
        $this->listRAIDUsage($input, $outputUsage);
        $systemC = new SystemProcess();
        $systemC->listAlarm($input, $outputAlarm);
        $output['Alarm'] = $outputAlarm['Alarm'];
        // var_dump($output);
        // $output['CephTotal'] = $cephTotal;
        // $output['CephUsed'] = $cephUsed;
        // $output['CephUsage'] = $cephUsage;
        $output['RAIDUsageMax'] = $outputUsage['RAIDUsageMax'];
        $output['RAIDAlarm'] = $outputUsage['RAIDAlarm'];
        $this->output_json($output);
    }

    function logActionOneArgc($input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $input = array();
                $input['Page']=$_GET['Page'];
                $input['Field']=$_GET['Field'];
                $input['Type']=$_GET['Type'];
                $input['Level']=$_GET['Level'];
                $input['ASC']=$_GET['ASC'];
                $this->listLog($input);
                break;
            default:
                http_response_code(404);
        }
    }
    
    function listLog($input)
    {
        $logC = new LogAction();
        $responsecode = $logC->listLogWithSortAndType($input, $outputLog);
        if ($responsecode == CPAPIStatus::ListLogSuccess) {
            $this->output_json($outputLog);
        } else {
            $logC->processLogErrorCode($responsecode);
        }
    }
    
    function vSwitchActionOneArgc($defaultValues, $input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $input = array();
                $this->setDefaultValueToInput($defaultValues, $input);
                $this->listVswitch($input);
                break;
            case 'POST':
                // var_dump($input);
                $input = json_decode($input, true);
                // var_dump($input);
                $this->setDefaultValueToInput($defaultValues, $input);
                $this->setVswitch($input);
                break;
            default:
                http_response_code(404);
        }
    }

    function listVswitch($input)
    {
        $vSwitchC = new vSwitchAction();
        $responsecode = $vSwitchC->listVswitch($input, $output);
        if ($responsecode != CPAPIStatus::ListVSwitchSuccess) {
            $output = array("Vswitchs"=>array(),"FreeDevs"=>array());
        }
        $this->output_json($output);
    }

    function setVswitch($input)
    {
        // var_dump($input);
        $vSwitchC = new vSwitchAction();
        $responsecode = $vSwitchC->setVswitch($input);
        if ($responsecode != CPAPIStatus::SetVSwitchSuccess) {
            $vSwitchC->process_vswitch_error_code($responsecode);
        }
    }

    function systemActionOneArgc($defaultValues, $input, $api)
    {
        switch ($api) {
            case 'system':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case "GET":
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->listSystemInfo($input);
                        break;
                    default:
                        http_response_code(404);
                }
                break;
            case 'system_util':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case "GET":
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->listSystemUtil($input);
                        break;
                    default:
                        http_response_code(404);
                }
                break;
            case 'alarm':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case "GET":
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->listAlarm($input);
                        break;
                    case 'PUT':
                        $input = json_decode($input, true);
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->setAlarm($input);
                        break;
                    default:
                        http_response_code(404);
                }
                break;
            case 'smb':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case "GET":
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->listSMB($input);
                        break;
                    case 'PUT':
                        $input = json_decode($input, true);
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->setSMB($input);
                        break;
                    default:
                        http_response_code(404);
                }
                break;
            case 'smb_pwd':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'PUT':
                        $input = json_decode($input, true);
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->setSMBPWD($input);
                        break;
                    default:
                        http_response_code(404);
                }
                break;
            case 'hostname':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'GET':
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->listHostname($input);
                        break;
                    case 'PUT':
                        $input = json_decode($input, true);
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->setHostname($input);
                        break;
                    default:
                        http_response_code(404);
                }
                break;
            case 'dropcache':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'DELETE':
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->dropCashe($input);
                        break;
                    default:
                        http_response_code(404);
                }
                break;
            case 'ssh':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'GET':
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->getSSH($input);
                        break;
                    case 'PUT':
                        $input = json_decode($input, true);
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->setSSH($input);
                        break;
                    default:
                        http_response_code(404);
                }
                break;
            case 'ups':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'GET':
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->listUPS($input);
                        break;
                    case 'POST':
                        $input = json_decode($input, true);
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->setUPS($input);
                        break;
                    default:
                        http_response_code(404);
                }
                break;
            case 'mail':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'GET':
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->listMail($input);
                        break;
                    case 'POST':
                        $input = json_decode($input, true);
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $this->setMail($input);
                        break;
                    default:
                        http_response_code(404);
                }
                break;
            default:
                http_response_code(404);
                break;
        }
    }

    function listMail($input)
    {
        $systemC = new SystemProcess();
        $responsecode = $systemC->listMail($input, $output);        
        if($responsecode != CPAPIStatus::ListMailSuccess)
            http_response_code(400);
        else
            $this->output_json($output);
    }

    function setMail($input)
    {
        $systemC = new SystemProcess();
        $responsecode = $systemC->setMail($input);
        if($responsecode != CPAPIStatus::SetMailSuccess)
            http_response_code(400);      
    }

    function listUPS($input)
    {
        $systemC = new SystemProcess();
        $systemC->listUPS($input, $output);
        $this->output_json($output);
    }

    function setUPS($input)
    {
        $systemC = new SystemProcess();
        $responsecode = $systemC->setUPS($input, $output);
        if ($responsecode != CPAPIStatus::SetUPSSuccess) {
            http_response_code(400);
        } else {
            $this->output_json($output);
        }
    }

    function getSSH($input)
    {
        $nodeC = new SystemProcess();
        $responsecode = $nodeC->listNodeSSH($input, $output);
        if ($responsecode != CPAPIStatus::ListNodeSSHSuccess) {
            http_response_code(400);
        } else {
            $this->output_json($output);
        }
    }

    function setSSH($input)
    {
        $nodeC = new SystemProcess();
        $responsecode = $nodeC->setNodeSSH($input);
        // var_dump($responsecode);
        if ($responsecode != CPAPIStatus::SetNodeSSHSuccess) {
            http_response_code(400);
        }
    }
    
    function listSystemInfo($input)
    {
        $systemC = new SystemProcess();
        if ($systemC->systemInfo($input, $output)) {
            $this->output_json($output);
        } else {
            http_response_code(400);
        }
    }

    function listSystemUtil($input)
    {
        $systemC = new SystemProcess();
        if ($systemC->systemUtil($input, $output)) {
            $this->output_json($output);
        } else {
            http_response_code(400);
        }
    }

    function listAlarm($input)
    {
        $systemC = new SystemProcess();
        if ($systemC->listAlarm($input, $output)) {
            $this->output_json($output);
        } else {
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
        if ($systemC->listSMB($input, $output)) {
            $this->output_json($output);
        } else {
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
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $input = array();
                $input['count']=(int)$_GET['count'];
                $input['id']=(int)$_GET['id'];
                $input['readback']=(int)$_GET['readback'];
                $this->listLogByID($input);
                break;
            default:
                http_response_code(404);
        }
    }
    
    function listLogByID($input)
    {
        $log_c = new LogAction();
        $responsecode = $log_c->listLogByID($input, $output_log);
        if ($responsecode == CPAPIStatus::ListLogSuccess) {
            $this->output_json($output_log);
        } else {
            $log_c->processLogErrorCode($responsecode);
        }
    }
    
    function scheduleActionTwoArgc($defaultValues, $argc, $input)
    {
        if (preg_match('/^schedule\/[0-9]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "DELETE":
                    $input = array();
                    $input['ScheduleID']=$argc[1];
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->deleteSchedule($input);
                    break;
                case "PUT":
                    $input = json_decode($input, true);
                    $this->checkJson($input);
                    $input['ScheduleID']=$argc[1];
                    $this->setDefaultValueToInput($defaultValues, $input);
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
        if ($responsecode != CPAPIStatus::DeleteScheduleSuccess) {
            $scheduleC->processScheduleErrorCode($responsecode);
        } else {
            echo '{}';
        }
    }

    function modifySchedule($input)
    {
        $scheduleC = new ScheduleAction();
        $responsecode = $scheduleC->modifySchedule($input, $output);
        if ($responsecode != CPAPIStatus::ModfiyScheduleSuccess) {
            $scheduleC->processScheduleErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }

    function target_action_two_argc($argc, $input)
    {
        if (preg_match('/^[0-9]+$/', $argc)) {
            $input = array();
            $input['IDTarget'] = $argc;
            $this->list_target_id($input);
        } else {
            http_response_code(404);
        }
    }
    
    function list_target_id($input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $vdtarget_c = new Target_Action();
                $responsecode = $vdtarget_c->list_target_by_id($input, $output);
                if ($responsecode == CPAPIStatus::ListTargetbyIDSuccess) {
                    $this->output_json($output);
                } else {
                    $vdtarget_c->process_target_error_code($responsecode);
                }
                break;
            default:
                http_response_code(404);
        }
    }
    
    function iscsi_action_two_argc($argc, $input)
    {
        switch ($argc) {
            case 'discovery':
                $this->discovery_action($input);
                break;
            default:
                http_response_code(404);
                break;
        }
    }
    
    function discovery_action($input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "POST":
                $iscsi_c = new iSCSI_Action();
                $input = json_decode($input, true);
                $responsecode = $iscsi_c->API_Discovery_iSCSI($input, $output);
                if ($responsecode != CPAPIStatus::DiscoverySuccess) {
                    $iscsi_c->process_iscsi_error_code($responsecode);
                } else {
                    $this->output_json($output);
                }
                break;
            default:
                http_response_code(404);
        }
    }
    
    function cluster_action_two_argc($argc, $input)
    {
        if (preg_match('/^cluster\/free$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $this->cluster_list_free_resource_action();
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^cluster\/[0-9]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
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
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }
    
    function cluster_list_free_resource_action()
    {
        $cluster_c = new Cluster_Action();
        $responsecode = $cluster_c->list_cluster_free_resource($output);
        if ($responsecode == CPAPIStatus::ListClusterFreeSuccess) {
            $this->output_json($output);
        } else {
            $cluster_c->process_cluster_error_code($responsecode);
        }
    }
    
    function get_cluster_info_action($input)
    {
        set_time_limit(60);
        $cluseter_c = new Cluster_Action();
        $responsecode = $cluseter_c->list_cluster_info($input, $output);
        if ($responsecode != CPAPIStatus::ListClusterInfoSuccess) {
            $cluseter_c->process_cluster_error_code($responsecode);
        } else {
            $this->output_json($output);
        }
    }
    
    function delete_cluster_action($input)
    {
        $cluseter_c = new Cluster_Action();
        $responsecode = $cluseter_c->delete_cluster($input);
        if ($responsecode != CPAPIStatus::DeleteClusterSuccess) {
            $cluseter_c->process_cluster_error_code($responsecode);
        }
    }
       
    function taskActionTwoArgc($defaultValues, $argc, $input)
    {
        // var_dump($argc);
        if (preg_match('/^task\/snapshot$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->listSSTask($input);
                    break;
                case "DELETE":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->clearSSTask($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^task\/backup$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->listBackupTask($input);
                    break;
                case "DELETE":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->clearBackupTask($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^task\/[-,A-Z,a-z,0-9,\.,:]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "DELETE":
                    $input = array();
                    $input['TaskID']=$argc[1];
                    $this->setDefaultValueToInput($defaultValues, $input);
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
        if ($responsecode != CPAPIStatus::CancelTaskSuccess) {
            $taskC->processTaskErrorCode($responsecode);
        }
    }

    function clearBackupTask($input)
    {
        $taskC = new TaskAction();
        $taskC->clearBackupTask($input);
    }

    function listBackupTask($input)
    {
        $taskC = new TaskAction();
        $responsecode = $taskC->listBackupTask($input, $output);
        if ($responsecode == CPAPIStatus::ListTaskSuccess) {
            $this->output_json($output);
        } else {
            $task_c->processTaskErrorCode($responsecode);
        }
    }

    function clearSSTask($input)
    {
        $taskC = new TaskAction();
        $taskC->clearSSTask($input);
    }

    function listSSTask($input)
    {
        $task_c = new TaskAction();
        $responsecode = $task_c->listSSTask($input, $output);
        if ($responsecode == CPAPIStatus::ListTaskSuccess) {
            $this->output_json($output);
        } else {
            $task_c->processTaskErrorCode($responsecode);
        }
    }

    function vdActionTwoArgc($defaultValues, $argc, $input)
    {
        if (preg_match('/^vd\/iso+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->listISO($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^vd+\/suspend$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->listSuspend($input);
                    break;
                case 'PUT':
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->setSuspend($input);
                    break;
                case 'POST':
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->setVDSuspend($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^vd+\/import$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->listImportVD($input);
                    break;
                case 'POST':
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->importVD($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^vd\/poweron$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $vds = json_decode($input, true);
                    $data['VDs'] = $vds;
                    $this->setDefaultValueToInput($defaultValues, $data);
                    // var_dump($input);
                    $this->poweronVD($data);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^vd\/shutdown$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $vds = json_decode($input, true);
                    $data['VDs'] = $vds;
                    $this->setDefaultValueToInput($defaultValues, $data);
                    $this->shutdownVD($data);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^vd\/poweroff$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $vds = json_decode($input, true);
                    $data['VDs'] = $vds;
                    $this->setDefaultValueToInput($defaultValues, $data);
                    $this->poweroffVD($data);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^vd\/disable$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $vds = json_decode($input, true);
                    $data['VDs'] = $vds;
                    $this->setDefaultValueToInput($defaultValues, $data);
                    $data['Disable'] = true;
                    $this->setVDDisable($data);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^vd\/enable$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $vds = json_decode($input, true);
                    $data['VDs'] = $vds;
                    $this->setDefaultValueToInput($defaultValues, $data);
                    $data['Disable'] = false;
                    $this->setVDDisable($data);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^vd\/backup$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->backupVD($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^vd\/restore$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->restoreVD($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^seed\/[a-z,0-9,-]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case 'PUT':
                    $input = json_decode($input, true);
                    // var_dump($input);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['VDID'] = $argc[1];
                    $this->modifySeed($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^vd\/[a-z,0-9,-]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "DELETE":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['VDID']=$argc[1];
                    $this->deleteVD($input);
                    break;
                case 'GET':
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['VDID'] = $argc[1];
                    $this->listVDInfo($input);
                    break;
                case 'PUT':
                    $input = json_decode($input, true);
                    // var_dump($input);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['VDID'] = $argc[1];
                    $this->modifyVD($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }

    function restoreVD($input)
    {
        $backupC = new BackupAction();
        $responsecode = $backupC->restore($input);
        if ($responsecode != CPAPIStatus::RestoreSuccess) {
            $backupC->processBackupErrorCode($responsecode);
        }
    }

    function backupVD($input)
    {
        $backupC = new BackupAction();
        $responsecode = $backupC->backup($input);
        if ($responsecode != CPAPIStatus::BackupSuccess) {
            $backupC->processBackupErrorCode($responsecode);
        }
    }

    function listSuspend($input)
    {
        $vdC = new VDAction();
        $vdC->listAutosuspend($input, $output);
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
        if ($responsecode != CPAPIStatus::DeleteVDSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        }
    }
    
    function jobActionTwoArgc($defaultValues, $argc, $input)
    {
        if (preg_match('/^job\/[a-z,0-9,-]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $input['JobID']=$argc[1];
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->listJobDetail($input);
                    break;
                case "DELETE":
                    $input = array();
                    $input['JobID']=$argc[1];
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->deleteJob($input);
                    break;
                case 'PUT':
                    $input = json_decode($input, true);
                    $input['VDs'] = $input;
                    $input['JobID']=$argc[1];
                    $this->setDefaultValueToInput($defaultValues, $input);
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
        $responsecode = $jobC->modifyJob($input, $output);
        if ($responsecode != CPAPIStatus::ModifyJobSuccess) {
            $jobC->processJobErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }

    function listJobDetail($input)
    {
        $jobC = new JobAction();
        $responsecode = $jobC->listJobDetail($input, $output);
        if ($responsecode != CPAPIStatus::ListJobDetailSuccess) {
            $jobC->processJobErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }

    function deleteJob($input)
    {
        $jobC = new JobAction();
        $responsecode = $jobC->deleteJob($input);
        if ($responsecode != CPAPIStatus::DeleteJobSuccess) {
            $jobC->processJobErrorCode($responsecode);
        }
    }

    function snapshotActionTwoArgc($defaultValues, $argc, $input)
    {
        if (preg_match('/^snapshot\/[a-z,0-9,-]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "DELETE":
                    $input = array();
                    $input['LayerID']=$argc[1];
                    $this->setDefaultValueToInput($defaultValues, $input);
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
        if ($responsecode != CPAPIStatus::DeleteSnapshotSuccess) {
            $snapshot_c->processSnapshotErrorCode($responsecode);
        }
    }

    function backupActionTwoArgc($defaultValues, $argc, $input)
    {
        if (preg_match('/^backup\/check$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->backupBrowse($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^backup\/format$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->backupFormat($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^backup\/vd$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->listBackupVD($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }

    function backupBrowse($input)
    {
        $bkC = new BackupAction();
        $responsecode = $bkC->lunBrowse($input, $output);
        if ($responsecode == CPAPIStatus::LUNBrowseSuccess) {
            $this->output_json($output);
        } else {
            $bkC->processBackupErrorCode($responsecode);
        }
    }

    function backupFormat($input)
    {
        $bkC = new BackupAction();
        $responsecode = $bkC->lunFormat($input);
        if ($responsecode != CPAPIStatus::FormatBackupSuccess) {
            $bkC->processBackupErrorCode($responsecode);
        }
    }

    function listBackupVD($input)
    {
        $bkC = new BackupAction();
        $responsecode = $bkC->listBackupVDWithSize($input, $output);
        if ($responsecode == CPAPIStatus::ListBackupVDWithSizeSuccess) {
            $this->output_json($output);
        } else {
            $bkC->processBackupErrorCode($responsecode);
        }
    }

    function diskActionTwoArgc($defaultValues, $argc, $input)
    {
        if (preg_match('/^disk\/reset$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->resetDisk($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^disk\/initial$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $data = array();
                    $data['Disks'] = $input;
                    $this->setDefaultValueToInput($defaultValues, $data);
                    $this->initialDisk($data);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^disk\/locate$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->locateDisk($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^ceph\/expansion$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->expansionRAID($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^ceph\/locate$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    // $data = array();
                    // $data['Disks'] = $input;
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->locateRAID($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^ceph\/dedupe$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->setDedupe($input);
                    break;
                case "GET":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->listDedupe($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^ceph\/[0-9]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $input['RAIDID'] = (int)$argc[1];
                    // $data = array();
                    // $data['Disks'] = $input;
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->listRAIDDisk($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }

    function listRAIDDisk($input)
    {
        $zfsC = new ZFSAction();
        $zfsC->listNormalRaid($input, $output);
        $this->output_json($output);
    }

    function initialDisk($input)
    {
        $diskC = new DiskAction();
        $responsecode = $diskC->initialDisk($input);
        if ($responsecode != CPAPIStatus::InitialDiskSuccess) {
            http_response_code(400);
        }
    }

    function locateDisk($input)
    {
        $diskC = new DiskAction();
        $diskC->locateDisk($input);
    }

    function locateRAID($input)
    {
        $zfsC = new ZfsAction();
        $zfsC->locateZfs($input);
    }
    
    function expansionRAID($input)
    {
        $raidC = new ZfsAction();
        $raidC->expansionZfs($input);
    }

    function listDedupe($input)
    {
        $raidC = new ZfsAction();
        $responsecode = $raidC->listClusterDedup($input, $output);
        if ($responsecode != CPAPIStatus::ListDedupSuccess) {
            http_response_code(400);
        } else {
            $this->output_json($output);
        }
    }

    function setDedupe($input)
    {
        $raidC = new ZfsAction();
        $responsecode = $raidC->setZfsDedup($input, $output);
        if ($responsecode != CPAPIStatus::SetDedupSuccess) {
            http_response_code(400);
        }
        // else
        //     $this->output_json($output);
    }
    
    function resetDisk($input)
    {
        $diskC = new DiskAction();
        $diskC->resetZfsDisk($input);
    }

    function userActionTwoArgc($defaultValues, $argc, $input)
    {
        if (preg_match('/^user\/[0-9]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "DELETE":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['UserID']=$argc[1];
                    if (is_null($_GET['vd_delete'])) {
                        http_response_code(404);
                    }
                    $input['vd_delete']=$_GET['vd_delete'];
                    $this->deleteUserAction($input);
                    break;
                case "PUT":
                    if (!is_null($_GET['disable'])) {
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $input['UserID']=$argc[1];
                        $input['Disable']=  ((int)$_GET['disable']) == 1 ? true : false;
                        $this->disableUser($input);
                    } else {
                        http_response_code(404);
                    }
                    break;
                default:
                    http_response_code(404);
            }
        }
    }
    
    function disableUser($input)
    {
        $user_c = new UserAction();
        $responsecode = $user_c->disableUser($input);
        if ($responsecode != CPAPIStatus::DisableUserSuccess) {
            $user_c->process_user_error_code($responsecode);
        }
    }

    function deleteUserAction($input)
    {
        $user_c = new UserAction();
        $responsecode = $user_c->deleteUser($input);
        if ($responsecode != CPAPIStatus::DeleteUserSuccess) {
            $user_c->process_user_error_code($responsecode);
        }
    }

    function mailActionTwoArgc($defaultValues, $api, $input)
    {
        // var_dump($api);
        switch ($api[1]) {
            case 'test':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'PUT': 
                        $input = array();                    
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->testMail($input);                        
                        break;
                    default:
                        http_response_code(404);
                        break;
                }                               
                break;            
        }
    }

    function testMail($input)
    {
        $systemC=new SystemProcess();
        $responsecode = $systemC->sendTestMail($input);
        if($responsecode != CPAPIStatus::SendTestMailSuccess)
            http_response_code(400);
    }
    
    function domain_action_two_argc($argc, $input)
    {
        if (preg_match('/^domain\/[0-9]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = json_decode($input, true);
                    $input['DomainID']=$argc[1];
                    $this->modify_domain_action($input);
                    break;
                case "DELETE":
                    $input = array();
                    $input['DomainID']=$argc[1];
                    $this->delete_domain_action($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
    }
    
    function modify_domain_action($input)
    {
        $domain_c = new DomainAction();
        $responsecode = $domain_c->modify_domain($input);
        if ($responsecode != CPAPIStatus::ModifyDomainSuccess) {
            $domain_c->process_domain_error_code($responsecode);
        }
    }

    function delete_domain_action($input)
    {
        $domain_c = new DomainAction();
        $responsecode = $domain_c->delete_domain($input['DomainID']);
        if ($responsecode != CPAPIStatus::DeleteDomainSuccess) {
            $domain_c->process_domain_error_code($responsecode);
        }
    }
    
    function update_action_two_argc($argc, $input)
    {
        if (preg_match('/^update\/now$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $this->update_list_now_version();
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^update\/new$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $this->update_list_new_version();
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^update\/check$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $this->update_check();
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^update\/updatenew$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $this->update_new();
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^update\/updatelocalnew$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $this->update_local_new();
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^update\/uploadnew$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $this->update_list_upload_new_version();
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
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

    function update_local_new()
    {
        $oProcSystem = new SystemProcess();
        $VersionData = $oProcSystem->uploadUpdate();
        $this->output_json($VersionData);
    }

    function update_new()
    {
        $oProcSystem = new SystemProcess();
        $VersionData = $oProcSystem->Update();
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
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->listUnimportableRAID($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }

    function listUnimportableRAID($input)
    {
        $zfsC = new ZFSAction();
        $zfsC->listUnimportableRaid($input, $output);
        $this->output_json($output);
    }

    function vdActionThreeArgc($defaultValues, $argc, $input)
    {
        if (preg_match('/^[a-z,0-9,-]+\/snapshot$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['VDID'] = $argc[1];
                    $this->listVDSnapshot($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/sslayermax$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['VDID'] = $argc[1];
                    $this->listVDSSLayerMax($input);
                    break;
                case "PUT":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['VDID'] = $argc[1];
                    $this->updateVDSSLayerMax($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/disk$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['VDID'] = $argc[1];
                    $this->addVDDisk($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/nic$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['VDID'] = $argc[1];
                    $this->addVDNIC($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/resetpwd$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['VDID'] = $argc[1];
                    $this->resetVDPassword($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/iso$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['VDID'] = $argc[1];
                    $this->modifyVDISO($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/free$/', $argc[1].'/'.$argc[2])) {
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
                    $this->setDefaultValueToInput($defaultValues, $input);
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
                    $this->setDefaultValueToInput($defaultValues, $input);
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
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['VDID'] = $argc[1];
                    $this->cloneVD($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/orgvd$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['VDID'] = $argc[1];
                    $this->usertoOrgVD($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/uservd$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
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
                    $this->setDefaultValueToInput($defaultValues, $input);
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
    
    function listVDSSLayerMax($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->listVDSSLayer($input, $output);
        if ($responsecode != CPAPIStatus::ListVDInfoSuccess) {
            $vdC->processVDErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }

    function updateVDSSLayerMax($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->updateVDSSLayer($input, $output);
        if ($responsecode != CPAPIStatus::ModifyVDSuccess) {
            $vdC->processVDErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }

    function listVDSnapshot($input)
    {
        $snapshot_c = new SnapshotAction();
        $responsecode = $snapshot_c->listSnapshot($input, $output);
        if ($responsecode == CPAPIStatus::ListSnapshotSuccess) {
            $this->output_json($output);
        } else {
            $snapshot_c->processSnapshotErrorCode($responsecode);
        }
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
        if ($responsecode != CPAPIStatus::AddVDNICSuccess) {
            $vdC->processVDErrorCode($responsecode);
        } else {
            echo '{}';
        }
    }

    function addVDDisk($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->addVDDisk($input);
        if ($responsecode != CPAPIStatus::AddVDDiskSuccess) {
            $vdC->processVDErrorCode($responsecode);
        } else {
            echo '{}';
        }
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
        $responsecode = $vdC->userToOriginalVD($input);
        // var_dump($responsecode);
        if ($responsecode != CPAPIStatus::CloneOrgSuccess) {
            $vdC->processVDErrorCode($responsecode);
        } else {
            echo '{}';
        }
    }
    
    function seedCreate($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->createSeedVD($input);
        if ($responsecode != CPAPIStatus::CreateSeedSuccess) {
            $vdC->processVDErrorCode($responsecode);
        } else {
            echo '{}';
        }
    }
    
    function resetVDPassword($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->resetVDPassword($input);
        if ($responsecode != CPAPIStatus::ChangePWDSuccess) {
            $vdC->processVDErrorCode($responsecode);
        } else {
            echo '{}';
        }
    }

    function overwriteSeed($input)
    {
        // var_dump($input);
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
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['ScheduleID'] = $argc[1];
                    $this->createJob($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }

    function createJob($input)
    {
        $jobC = new JobAction();
        $responsecode = $jobC->createJob($input, $output);
        if ($responsecode != CPAPIStatus::CreateJobSuccess) {
            $jobC->processJobErrorCode($responsecode);
        } else {
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
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->rollbackSnapshot($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^view\/[a-z,0-9,-]+$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();
                    $input['LayerID'] = $argc[2];
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->viewSnapshot($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^stop_view\/[a-z,0-9,-]+$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();
                    $input['VDID'] = $argc[2];
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $this->stopViewSnapshot($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }

    function rollbackSnapshot($input)
    {
        $snapshot_c = new SnapshotAction();
        $responsecode = $snapshot_c->createRollbackSnapshotTask($input);
        if ($responsecode != CPAPIStatus::RollbackSnapshotSuccess) {
            $snapshot_c->processSnapshotErrorCode($responsecode);
        }
    }

    function viewSnapshot($input)
    {
        $snapshot_c = new SnapshotAction();
        $responsecode = $snapshot_c->viewSnapshot($input);
        if ($responsecode != CPAPIStatus::ViewSnapshotSuccess) {
            $snapshot_c->processSnapshotErrorCode($responsecode);
        }
    }

    function stopViewSnapshot($input)
    {
        $snapshot_c = new SnapshotAction();
        $responsecode = $snapshot_c->stopViewSnapshot($input);
        if ($responsecode != CPAPIStatus::StopViewSnapshotSuccess) {
            $snapshot_c->processSnapshotErrorCode($responsecode);
        }
    }

    function backupActionThreeArgc($defaultValues, $argc, $input)
    {
        if (preg_match('/^vd\/[a-z,0-9,-]+$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "DELETE":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['BkVDID'] = $argc[2];
                    $this->deleteBackupVD($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }

    function deleteBackupVD($input)
    {
        $backupC = new BackupAction();
        $responsecode = $backupC->backupManagerDelete($input);
        if ($responsecode != CPAPIStatus::DeleteBackupVDSuccess) {
            $backupC->processBackupErrorCode($responsecode);
        }
    }

    function domain_action_three_argc($argc, $input)
    {
        if (preg_match('/^[0-9]+\/user+$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $input['DomainID'] = (int)$argc[1];
                    $this->domain_create_user_action($input);
                    break;
                case "GET":
                    $input = array('DomainID'=>$argc[1]);
                    $this->domain_list_user_action($input);
                    break;
                default:
                    http_response_code(404);
                    break;
            }
        } else if (preg_match('/^[0-9]+\/resource+$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $input['DomainID'] = (int)$argc[1];
                    $this->domain_create_resource_action($input);
                    break;
                case "GET":
                    $input = array('DomainID'=>(int)$argc[1]);
                    $this->domain_list_resource_action($input);
                    break;
                default:
                    http_response_code(404);
                    break;
            }
        } else {
            http_response_code(404);
        }
    }
    
    function domain_list_resource_action($input)
    {
        $resource_c = new Resource_Action();
        $responsecode = $resource_c->list_resource_of_domain($input, $output);
        if ($responsecode == CPAPIStatus::ListResourceSuccess) {
            $this->output_json(array("Resources"=>$output));
        } else {
            $resource_c->process_resource_error_code($responsecode);
        }
    }
    
    function domain_create_resource_action($input)
    {
        $resource_c = new Resource_Action();
        $responsecode = $resource_c->add_resource_of_domain($input, $output);
        if ($responsecode == CPAPIStatus::CreateResourceSuccess) {
            $this->output_json($output);
        } else {
            $resource_c->process_resource_error_code($responsecode);
        }
    }
    
    function domain_list_user_action($input)
    {
        $user_c = new UserAction();
        $responsecode = $user_c->list_user_of_domain($input['DomainID'], $output);
        if ($responsecode == CPAPIStatus::ListUserofDomainSuccess) {
            $this->output_json($output);
        } else {
            $user_c->process_user_error_code($responsecode);
        }
    }
    
    function domain_create_user_action($input)
    {
        $user_c = new UserAction();
        $responsecode = $user_c->create_user($input, $output);
        if ($responsecode == CPAPIStatus::CreateUserSuccess) {
            http_response_code(200);
        } else {
            $user_c->process_user_error_code($responsecode);
        }
    }
        
    function resource_action_three_argc($argc, $input)
    {
        if (preg_match('/^[0-9]+\/[0-9]+$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "DELETE":
                    $input=array('DomainID'=>$argc[1],'IDGFS'=>$argc[2]);
                    $this->delete_resource($input);
                    break;
                case "PUT":
                    $input = json_decode($input, true);
                    if (is_null($input)) {
                        http_response_code(400);
                        exit();
                    }
                    $input['DomainID'] = (int)$argc[1];
                    $input['GFSID'] = (int)$argc[2];
                    $this->modify_resource($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }
    
    function modify_resource($input)
    {
        $resource_c = new Resource_Action();
        $responsecode = $resource_c->modify_resource_of_domain($input, $output);
        if ($responsecode == CPAPIStatus::ModifyResourceSuccess) {
            $this->output_json($output);
        } else {
            $resource_c->process_resource_error_code($responsecode);
        }
    }
    
    function delete_resource($input)
    {
        $resource_c = new Resource_Action();
        $responsecode = $resource_c->delete_resource($input['IDGFS'], $input['DomainID']);
        if ($responsecode == CPAPIStatus::DeleteResourceSuccess) {
            http_response_code(200);
        } else {
            $resource_c->process_resource_error_code($responsecode);
        }
    }
    
    function target_action_three_argc($argc, $input)
    {
        if (preg_match('/^[0-9]+\/server$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $input['IDTarget'] = (int)$argc[1];
                    $this->target_assign_server_aciton($input);
                    break;
                case "GET":
                    $input = json_decode($input, true);
                    $input['idCluster'] = (int)$argc[1];
                    $this->cluster_list_storage_action($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[0-9]+\/state/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = json_decode($input, true);
                    $input['IDTarget'] = (int)$argc[1];
                    $this->change_target_status($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[0-9]+\/vd/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $input['IDTarget'] = (int)$argc[1];
                    $this->list_vd_of_target($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }
    
    function list_vd_of_target($input)
    {
        $target_c = new Target_Action();
        $responsecode = $target_c->list_vd_of_target($input, $output);
        if ($responsecode != CPAPIStatus::ListVDofTargetSuccess) {
            $target_c->process_target_error_code($responsecode);
        } else {
            $this->output_json($output);
        }
    }
    
    function change_target_status($input)
    {
        $target_c = new Target_Action();
        $responsecode = $target_c->changer_target_status($input);
        if ($responsecode != CPAPIStatus::ChangeTargetStateSuceess) {
            $target_c->process_target_error_code($responsecode);
        }
    }
    
    function target_assign_server_aciton($input)
    {
        $target_c = new Target_Action();
        $responsecode = $target_c->assign_target_to_server($input, $output);
        if ($responsecode != CPAPIStatus::AddTaskAssignTargettoServerSuccess) {
            $target_c->process_target_error_code($responsecode);
        } else {
            $this->output_json($output);
        }
    }
    
    function userActionThreeArgc($defaultValues, $argc, $input)
    {
        // var_dump($argc);
        if (preg_match('/^[0-9]+\/resetpwd$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['UserID'] = (int)$argc[1];
                    $this->resetUserPassword($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[0-9]+\/info$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['UserID'] = (int)$argc[1];
                    $this->modifyUser($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[0-9]+\/profile$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['UserID'] = (int)$argc[1];
                    $this->createUserProfileAction($input);
                    break;
                case "DELETE":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['UserID'] = (int)$argc[1];
                    $this->deleteUserProfileAction($input);
                    break;
                case 'PUT':
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['UserID'] = (int)$argc[1];
                    $this->modifyUserProfileAction($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[0-9]+\/detail$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues, $input);
                        $input['UserID']=$argc[1];
                        $this->listUserDetail($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }

    function modifyUser($input)
    {
        $user_c = new UserAction();
        $responsecode = $user_c->modifyUser($input);
        if ($responsecode != CPAPIStatus::ModifyUserSuccess) {
            $user_c->process_user_error_code($responsecode);
        }
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
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['SeedID'] = $argc[1];
                    $input['Renew'] = $_GET['renew'] == 1 ? true : false;
                    $this->seedRebornAction($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
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
                    $this->setDefaultValueToInput($defaultValues, $input);
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
        
    function update_action_three_argc($argc, $input)
    {
        if (preg_match('/^[0-9]+\/upload$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $input['ClusterID'] = (int)$argc[1];
                    $this->update_upload_action($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }
    
    function update_upload_action($input)
    {
        $update_c = new Update_Action();
        $responsecode = $update_c->Redirect_UploadFile($input);
        if ($responsecode != CPAPIStatus::UpdateUploadSuccess) {
            $update_c->process_update_error_code($responsecode);
        }
    }

    function vdActionFourArgc($defaultValues, $argc, $input)
    {
        if (preg_match('/^[a-z,0-9,-]+\/restore\/check$/', $argc[1].'/'.$argc[2].'/'.$argc[3])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['BackupVDID'] = $argc[1];
                    $this->checkRestoreVD($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z0-9-]+\/disk\/[0-9]+$/', $argc[1].'/'.$argc[2].'/'.$argc[3])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "DELETE":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['VDID'] = $argc[1];
                    $input['DiskID'] = $argc[3];
                    $this->deleteVDDisk($input);
                    break;
                case 'PUT':
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['VDID'] = $argc[1];
                    $input['DiskID'] = $argc[3];
                    $this->modifyVDDisk($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z0-9-]+\/disk\/move+$/', $argc[1].'/'.$argc[2].'/'.$argc[3])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case 'PUT':
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['VDID'] = $argc[1];
                    $this->moveVDDisk($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/user\/[0-9]+$/', $argc[1].'/'.$argc[2].'/'.$argc[3])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['VDID'] = $argc[1];
                    $input['UserID'] = (int)$argc[3];
                    $this->vdAssignUser($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z0-9-]+\/nic\/[0-9a-z:]+$/', $argc[1].'/'.$argc[2].'/'.$argc[3])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "DELETE":
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['VDID'] = $argc[1];
                    $input['MAC'] = $argc[3];
                    $this->deleteVDNIC($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }

    function moveVDDisk($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->moveVDDisk($input);
        if ($responsecode != CPAPIStatus::MoveVDDiskSuccess) {
            $vdC->processVDErrorCode($responsecode);
        }
    }
    
    function checkRestoreVD($input)
    {
        $backupC = new BackupAction();
        $responsecode = $backupC->checkRestore($input, $output);
        if ($responsecode != CPAPIStatus::RestoreSuccess) {
            $backupC->processBackupErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }

    function modifyVDDisk($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->modifyVDDisk($input);
        if ($responsecode != CPAPIStatus::ModifyVDDiskSuccess) {
            $vdC->processVDErrorCode($responsecode);
        }
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
        if ($responsecode != CPAPIStatus::DeleteVDNICSuccess) {
            $vdC->processVDErrorCode($responsecode);
        } else {
            echo '{}';
        }
    }

    function deleteVDDisk($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->deleteVDDisk($input);
        if ($responsecode != CPAPIStatus::DeleteVDDiskSuccess) {
            $vdC->processVDErrorCode($responsecode);
        } else {
            echo '{}';
        }
    }

    function cluster_action_four_argc($argc, $input)
    {
        if (preg_match('/^[0-9]+\/node\/[0-9]+$/', $argc[1].'/'.$argc[2].'/'.$argc[3])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "DELETE":
                    $input['ClusterID'] = (int)$argc[1];
                    $input['ID'] = (int)$argc[3];
                    $this->remove_node($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }
    
    function remove_node($input)
    {
        $cluster_c = new Cluster_Action();
        $responsecode = $cluster_c->remove_node_from_cluster($input);
        $cluster_c->process_cluster_error_code($responsecode);
    }
    
    function resource_action_four_argc($argc, $input)
    {
        if (preg_match('/^[0-9]+\/[0-9]+\/allocated$/', $argc[1].'/'.$argc[2].'/'.$argc[3])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input['DomainID'] = (int)$argc[1];
                    $input['GFSID'] = (int)$argc[2];
                    $this->list_allocate_resource($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }
    
    function list_allocate_resource($input)
    {
        $resource_c = new Resource_Action();
        $responsecode = $resource_c->list_allocated_of_resource($input, $output);
        if ($responsecode == CPAPIStatus::ListAllocatedofResourceSuccess) {
            $this->output_json($output);
        } else {
            $resource_c->process_resource_error_code($responsecode);
        }
    }

    function userActionFourArgc($defaultValues, $argc, $input)
    {
        if (preg_match('/^[a-z0-9-]+\/disk\/move+$/', $argc[1].'/'.$argc[2].'/'.$argc[3])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case 'PUT':
                    $input = json_decode($input, true);
                    $this->setDefaultValueToInput($defaultValues, $input);
                    $input['UserID'] = $argc[1];
                    $this->moveUserProfile($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
    }

    function moveUserProfile($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->moveUserProfile($input);
        // var_dump($responsecode);
        if ($responsecode != CPAPIStatus::MoveVDDiskSuccess) {
            $vdC->processVDErrorCode($responsecode);
        }
    }
}
$api_c = new API();
