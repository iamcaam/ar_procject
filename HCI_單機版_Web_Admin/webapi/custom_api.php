<?php
// ini_set('display_errors', 'On');
include_once("/var/www/html/lib_manager/BaseAPI.php");
include_once("/var/www/html/api/error_code.php");
include_once("/var/www/html/api/get_input_api.php");
include_once("/var/www/html/include/HandleLogin.php");
include_once("/var/www/html/include/HandleSystem.php");
include_once("/var/www/html/include/HandleHA.php");
include_once("/var/www/html/include/GlobalClass.php");
include_once("/var/www/html/include/ConnectDB.php");
include_once("/var/www/html/include/ErrorEnum.php");
include_once("/var/www/html/include/VDErrorEnum.php");
include_once("/var/www/html/lib_manager/HandleCluster.php");
// include_once("/var/www/html/lib_manager/HandleVDServer.php");
include_once("/var/www/html/lib_manager/HandleiSCSI.php");
// include_once("/var/www/html/lib_manager/HandleStorage.php");
include_once("/var/www/html/lib_manager/HandleTarget.php");
include_once("/var/www/html/lib_manager/HandleTask.php");
include_once("/var/www/html/lib_manager/HandleUser.php");
include_once("/var/www/html/lib_manager/HandleVD.php");
include_once("/var/www/html/lib_manager/HandleSchedule.php");
include_once("/var/www/html/lib_manager/HandleDomain.php");
include_once("/var/www/html/lib_manager/HandleResource.php");
include_once("/var/www/html/lib_manager/HandleLog.php");
include_once("/var/www/html/lib_manager/HandleAD.php");
include_once("/var/www/html/lib_manager/HandleJob.php");
include_once("/var/www/html/lib_manager/HandleSnapshot.php");
// include_once("/var/www/html/lib_manager/UUID.php");

class CustomAPI extends BaseAPI
{
    function __construct()
    {
        $api = $_GET['api'];
        $api_arr = explode('/', $api);        
        $count = count($api_arr);
        switch ($count) {
            case 1:
                $this->handle_one_argc($api_arr);
                break;
            case 2:
                $this->handle_two_argc($api_arr);
                break;
            case 3:
                $this->handle_three_argc($api_arr);
                break;
            case 4:
                $this->handle_four_argc($api_arr);
                break;
            default:
                http_response_code(404);
        }
    }
    
    function handle_one_argc($api)
    {
        $data = file_get_contents("php://input");
        switch ($api[0]) {
            case 'login':
                $this->login($data);
                break;
            case 'task':
                $this->login_check($id_arr);
                $this->task_action_one_argc((int)$id_arr[0], $data);
                break;
            case 'user':
                $this->login_check($id_arr);
                $this->user_action_one_argc($id_arr[0], $data);
                break;
            case 'resource':
                $this->login_check($id_arr);
                $this->resource_action_one_argc($id_arr[0]);
                break;
            case 'ad':
                $this->login_check($id_arr);
                $this->ad_action_one_argc($id_arr[0], $data);
                break;
//            case 'logout':
//                $oLogin = new Login();
//                $oLogin->DoLogout(Manage_Session_Path);
//                break;           
            case 'log_by_id':
                $this->login_check($id_arr);
                $this->log_by_id_action_one_argc($id_arr[0]);
                break;
            case 'schedule':
                $this->login_check($id_arr);
                $this->schedule_action_one_argc($id_arr[0], $data);
                break;
            case 'snapshot':
                $this->login_check($id_arr);
                $this->snapshot_action_one_argc($id_arr[0], $data);
                break;
            default:
                http_response_code(404);
        }
    }
    
    function handle_two_argc($api)
    {
        $data = file_get_contents("php://input");
        switch ($api[0]) {
            case 'vd':
                $this->login_check($id_arr);
                $this->vd_action_two_argc($id_arr[0], $api, $data);
                break;
            case 'user':
                $this->login_check($id_arr);
                $this->user_action_two_argc($id_arr[0], $api, $data);
                break;
            case 'seed':
                $this->login_check($id_arr);
                $this->vd_action_two_argc($id_arr[0], $api, $data);
                break;
            case 'resource':
                $this->login_check($id_arr);
                $this->resource_action_two_argc($id_arr[0], $api, $data);
                break;
            case 'schedule':
                $this->login_check($id_arr);
                $this->schedule_action_two_argc($id_arr[0], $api, $data);
                break;
            case 'job':
                $this->login_check($id_arr);
                $this->job_action_two_argc($id_arr[0], $api, $data);  
                break;
            case 'task':
                $this->login_check($id_arr);
                $this->task_action_two_argc($id_arr[0], $api, $data);
                break;
            case 'snapshot':
                $this->login_check($id_arr);
                $this->snapshot_action_two_argc($id_arr[0], $api, $data);
                break;
            default:
                http_response_code(404);
        }
    }
    
    function handle_three_argc($api)
    {
        $data = file_get_contents("php://input");
        switch ($api[0]) {
            case 'vd':
                $this->login_check($id_arr);
                $this->vd_action_three_argc($id_arr[0], $api, $data);
                break;
            case 'user':
                $this->login_check($id_arr);
                $this->user_action_three_argc($id_arr[0], $api, $data);
                break;
            case 'gfs':
                $this->login_check($id_arr);
                $this->gfs_action_three_argc($id_arr[0], $api, $data);
                break;
            case 'seed':
                $this->login_check($id_arr);
                $this->seed_action_three_argc($id_arr[0], $api, $data);
                break;
            case 'uservd':
                $this->login_check($id_arr);
                $this->uservd_action_three_argc($id_arr[0], $api, $data);
                break;
            case 'schedule':
                $this->login_check($id_arr);
                $this->job_action_three_argc($id_arr[0], $api, $data);
                break;
            case 'snapshot':
                $this->login_check($id_arr);
                $this->snapshot_action_three_argc($id_arr[0], $api, $data);
                break;
            case 'task':
                $this->login_check($id_arr);
                $this->task_action_three_argc($id_arr[0], $api, $data);
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
        $pwd = $this->binary_to_hex2_str($pwd);
        if (isset($data['Username'])) {
            $name_arr = explode('@', $data['Username']);
            if (count($name_arr) == 2) {
                $status = $oLogin->Login_By_DB($name_arr[0], $name_arr[1], $pwd);
            }
        }
        if ($status != APIStatus::LoginSuccess) {
            http_response_code(401);
        }
    }
    
    function binary_to_hex2_str($bin_data)
    {
        $hex = '';
        foreach (str_split($bin_data) as $bin) {
            $hex .= sprintf("%02x", ord($bin));
        }
        return trim($hex);
    }
    
    function login_check(&$id_arr)
    {
        $oLogin = new Login();
//        var_dump($_COOKIE);
        if (isset($_COOKIE['Dom'])) {
            $data = $oLogin->data_crypt('ar3527678', $_COOKIE['Dom'], 'decrypt');
            $id_arr = explode('@', $data);
            if (count($id_arr) == 2) {
                $oLogin->Sec_Session_Start("/mnt/tmpfs/session/".$id_arr[0].'/'.$id_arr[1]);
            }
            if ($oLogin->Login_Check_By_DB($id_arr[0], $id_arr[1])!= APIStatus::LoginSuccess) {
                http_response_code(401);
                exit();
            }
        } else {
            http_response_code(401);
        }
    }
    
    function task_action_one_argc($domainid, $input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $input = array();
                $input['DomainID'] = $domainid;
                $this->list_task_action($input);
                break;
            case "DELETE":
                $input = array();
                $input['DomainID'] = $domainid;
                $this->clear_task_action($input);
                break;
            default:
                http_response_code(404);
        }
    }
    
    function clear_task_action($input)
    {
        $task_c = new Task_Action();
        $responsecode = $task_c->clear_task($input);
        if ($responsecode != CPAPIStatus::ClearTaskSuccess) {
            $task_c->process_task_error_code($responsecode);
        }
    }
    
    function list_task_action($input)
    {
        $task_c = new Task_Action();
        $responsecode = $task_c->list_task($input, $output);
        if ($responsecode == CPAPIStatus::ListTaskSuccess) {
            $this->output_json($output);
        } else {
            $task_c->process_task_error_code($responsecode);
        }
    }
    
    function user_action_one_argc($domainid, $input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $this->list_user($domainid);
                break;
            case "POST":
                $input = json_decode($input, true);
                $input['DomainID'] = $domainid;
                $this->create_user_action($input);
                break;
            default:
                http_response_code(404);
        }
    }
    
    function create_user_action($input)
    {
        $user_c = new UserAction();
        $responsecode = $user_c->create_user($input, $output);
        if ($responsecode == CPAPIStatus::CreateUserSuccess) {
            $this->output_json($output);
        } else {
            $user_c->process_user_error_code($responsecode);
        }
    }
    
    function list_user($domainid)
    {
        $user_c = new UserAction();
        $responsecode = $user_c->list_user_of_domain($domainid, $output);
        if ($responsecode == CPAPIStatus::ListUserofDomainSuccess) {
            $this->output_json($output);
        } else {
            $user_c->process_user_error_code($responsecode);
        }
    }
    
    function resource_action_one_argc($domainid)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $this->list_resource($domainid);
                break;
            default:
                http_response_code(404);
        }
    }
    
    function list_resource($domainid)
    {
        $resource_c = new Resource_Action();
        $responsecode = $resource_c->list_resource_of_domain(array('DomainID'=>$domainid), $output);
        if ($responsecode == CPAPIStatus::ListResourceSuccess) {
            $this->output_json(array("Resources"=>$output));
        } else {
            $resource_c->process_resource_error_code($responsecode);
        }
    }
    
    function ad_action_one_argc($domainid, $input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $this->list_ad($domainid);
                break;
            case "POST":
                $input = json_decode($input, true);
                $input['DomainID'] = $domainid;
                $this->set_ad($input);
                break;
            default:
                http_response_code(404);
        }
    }

    function list_ad($domainid)
    {
        $ad_c = new AD_Action();
        $responsecode = $ad_c->list_ad_configure($domainid,$output);
        if($responsecode == CPAPIStatus::ListDomainADConfigSuccess){
            $this->output_json($output);
        }
        else{
            $ad_c->process_ad_error_code($responsecode);
        }
    }

    function set_ad($input)
    {
        $ad_c = new AD_Action();
        $responsecode = $ad_c->add_ad_configure($input);
        if($responsecode != CPAPIStatus::AddDomainADAuthSuccess){
            $ad_c->process_ad_error_code($responsecode);
        }
    }

    function schedule_action_one_argc($domainid, $input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $input = array();
                $input['DomainID'] = $domainid;
                $this->list_schedule($input);
                break;            
            case "POST":
                $input = json_decode($input, true);
                $input['DomainID'] = $domainid;
                $this->create_schedule($input);
                break;
            default:
                http_response_code(404);
        }
    }

    function list_schedule($input)
    {
        $schedule_c = new Schedule_Action();
        $responsecode = $schedule_c->list_schedule($input,$output);
        if($responsecode != CPAPIStatus::ListScheduleSuccess){
            $schedule_c->process_schedule_error_code($responsecode);
        }
        else{
            $this->output_json($output);
        }
    }

    function create_schedule($input)
    {
        $schedule_c = new Schedule_Action();
        $responsecode = $schedule_c->create_schedule($input,$output);
        if($responsecode != CPAPIStatus::CreateScheduleSuccess){
            $schedule_c->process_schedule_error_code($responsecode);
        }
        else{
            $this->output_json($output);
        }            
    }

    function snapshot_action_one_argc($domainid, $input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {            
            case "POST":
                $input = json_decode($input, true);
                $input['DomainID'] = $domainid;
                $this->create_take_snapshot_task($input);
                break;
            default:
                http_response_code(404);
        }
    }

    function create_take_snapshot_task($input)
    {
        $snap_c = new Snapshot_Action();
        $responsecode = $snap_c->create_take_snapshot_task($input);
        if($responsecode != CPAPIStatus::TakeSnapshotSuccess)
            $snap_c->process_snapshot_error_code($responsecode);
    }

    function log_by_id_action_one_argc($domainid)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $input = array();
                $input['DomainID'] = (int)$domainid;
                $input['count']=(int)$_GET['count'];
                $input['id']=(int)$_GET['id'];
                $input['readback']=(int)$_GET['readback'];
                $this->list_log_by_id($input);
                break;
            default:
                http_response_code(404);
        }
    }
    
    function list_log_by_id($input)
    {
        $log_c = new LogAction();
        $responsecode = $log_c->list_domain_log_by_id($input, $output_log);
        if ($responsecode == CPAPIStatus::ListLogSuccess) {
            $this->output_json($output_log);
        } else {
            $log_c->processLogErrorCode($responsecode);
        }
    }
    
    function vd_action_two_argc($domain_id, $argc, $input)
    {
        if (preg_match('/^vd\/poweron+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->poweron_vd_action($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^vd\/shutdown+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->shutdown_vd_action($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^vd\/poweroff+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $this->poweroff_vd_action($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^vd\/import+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $input['DomainID']=$domain_id;
                    $this->import_vd_action($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^vd\/[a-z,0-9,-]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "DELETE":
                    $input = array();
                    $input['DomainID']=$domain_id;
                    $input['ID']=$argc[1];
                    $this->delete_vd_action($input);
                    break;
                case "GET":
                    $input = array();
                    $input['ID']=$argc[1];
                    $this->list_vd_info_action($input);
                    break;
                case "PUT":
                    if (is_null($_GET['disable'])) {
                        $input = json_decode($input, true);
                        $input['ID']=$argc[1];
                        $input['DomainID']=$domain_id;
                        $this->modify_vd_action($input);
                    } else {
                        $input['DomainID']=$domain_id;
                        $input['ID']=$argc[1];
                        $input['Disable']=  boolval($_GET['disable']);
                        $this->disable_vd_action($input);
                    }
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^seed\/[a-z,0-9,-]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = json_decode($input, true);
                    $input['ID']=$argc[1];
                    $input['DomainID']=$domain_id;
                    $this->modify_seed_action($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }
    
    function import_vd_action($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->import_vd($input, $output);
        if ($responsecode != CPAPIStatus::ImportVDSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }
    
    function disable_vd_action($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->disable_vd($input['ID'], $input['Disable']);
        if ($responsecode != CPAPIStatus::DisableVDSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        }
    }
    
    function poweron_vd_action($input)
    {
        $vd_c = new VDAction();
        $vd_c->poweron_vd($input);
    }
    
    function shutdown_vd_action($input)
    {
        $vd_c = new VDAction();
        $vd_c->shutdown_vd($input);
    }
    
    function poweroff_vd_action($input)
    {
        $vd_c = new VDAction();
        $vd_c->poweroff_vd($input);
    }
    
    function delete_vd_action($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->delete_vd($input);
        if ($responsecode != CPAPIStatus::DeleteVDSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        }
    }
    
    function list_vd_info_action($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->list_vd_info($input['ID'], $output);
        if ($responsecode != CPAPIStatus::ListVDInfoSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }
    
    function modify_vd_action($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->modify_vd($input);
        if ($responsecode != CPAPIStatus::ModifyVDSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        }
    }
    
    function modify_seed_action($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->modify_seed_vd($input);
        if ($responsecode != CPAPIStatus::ModifyVDSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        }
    }
    
    function user_action_two_argc($domain_id, $argc, $input)
    {
        if (preg_match('/^user\/[0-9]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "DELETE":
                    $input = array();
                    $input['UserID']=$argc[1];
                    $input['DomainID']=$domain_id;
                    if (is_null($_GET['vd_delete'])) {
                        http_response_code(404);
                    }
                    $input['vd_delete']=$_GET['vd_delete'];
                    $this->delete_user_action($input);
                    break;
                case "PUT":
                    if (!is_null($_GET['disable'])) {
                        $input = array();
                        $input['UserID']=$argc[1];
                        $input['DomainID']=$domain_id;
                        $input['Disable']=  ((int)$_GET['disable']) == 1 ? true : false;
                        $this->disable_user_action($input);
                    } else {
                        http_response_code(404);
                    }
                    break;
                default:
                    http_response_code(404);
            }
        }
    }
    
    function disable_user_action($input)
    {
        $user_c = new UserAction();
        $responsecode = $user_c->disable_user($input);
        if ($responsecode != CPAPIStatus::DisableUserSuccess) {
            $user_c->process_user_error_code($responsecode);
        }
    }
    
    function delete_user_action($input)
    {
        $user_c = new UserAction();
        $responsecode = $user_c->delete_user($input);
        if ($responsecode != CPAPIStatus::DeleteUserSuccess) {
            $user_c->process_user_error_code($responsecode);
        }
    }
    
    function schedule_action_two_argc($idDomain,$argc,$input)
    {
        if (preg_match('/^schedule\/[0-9]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "DELETE":
                    $input = array();
                    $input['ScheduleID']=$argc[1];
                    $input['DomainID']=$idDomain;                    
                    $this->delete_schedule($input);
                    break;   
                case "PUT":
                    $input = json_decode($input,true);
                    $input['ScheduleID']=$argc[1];
                    $input['DomainID']=$idDomain;                    
                    $this->modify_schedule($input);
                    break;            
                default:
                    http_response_code(404);
            }
        }
    }

    function delete_schedule($input)
    {
        $schedule_c = new Schedule_Action();
        $responsecode = $schedule_c->delete_schedule($input);
        if($responsecode != CPAPIStatus::ModfiyScheduleSuccess){
            $schedule_c->process_schedule_error_code($responsecode);
        }
    }

    function modify_schedule($input)
    {
        $schedule_c = new Schedule_Action();
        $responsecode = $schedule_c->modify_schedule($input,$output);
        if($responsecode != CPAPIStatus::ModfiyScheduleSuccess){
            $schedule_c->process_schedule_error_code($responsecode);
        }
        else{
            $this->output_json($output);
        }      
    }

    function job_action_two_argc($idDomain,$argc,$input)
    {
        if (preg_match('/^job\/[a-z,0-9,-]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $input['JobID']=$argc[1];
                    $input['DomainID']=$idDomain;                    
                    $this->list_job_detail($input);
                    break;   
                case "DELETE":
                    $input = array();
                    $input['JobID']=$argc[1];
                    $input['DomainID']=$idDomain;                    
                    $this->delete_job($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
    }

    function list_job_detail($input)
    {        
        $job_c = new Job_Action();
        $responsecode = $job_c->list_job_detail($input,$output);
        if($responsecode != CPAPIStatus::ListJobDetailSuccess){
            $job_c->process_job_error_code($responsecode);
        }
        else{
            $this->output_json($output);
        }      
    }

    function delete_job($input)
    {
        $job_c = new Job_Action();
        $responsecode = $job_c->delete_job($input);
        if($responsecode != CPAPIStatus::DeleteJobSuccess)
            $job_c->process_job_error_code($responsecode);
    }

    function task_action_two_argc($idDomain,$argc,$input)
    {      
        // var_dump($argc);
        if (preg_match('/^task\/snapshot$/', $argc[0].'/'.$argc[1])) {            
            switch ($_SERVER['REQUEST_METHOD']) {                
                case "GET":
                    $input = array();                    
                    $input['DomainID']=$idDomain;                    
                    $this->list_ss_task($input);
                    break;    
                case "DELETE":
                    $input = array();                    
                    $input['DomainID']=$idDomain;                    
                    $this->clear_ss_task($input);
                    break;            
                default:
                    http_response_code(404);
            }
        }             
        else if (preg_match('/^task\/[-,A-Z,a-z,0-9,\.,:]+$/', $argc[0].'/'.$argc[1])) {            
            switch ($_SERVER['REQUEST_METHOD']) {                
                case "DELETE":
                    $input = array();
                    $input['TaskID']=$argc[1];
                    $input['DomainID']=$idDomain;                    
                    $this->cancel_task($input);
                    break;                
                default:
                    http_response_code(404);
            }
        }        
    }

    function clear_ss_task($input)
    {
        $task_c = new Task_Action();        
        $responsecode = $task_c->clear_task($input,'snapshot');
        if($responsecode != CPAPIStatus::ClearTaskSuccess)        
            $task_c->process_task_error_code($responsecode);
    }

    function list_ss_task($input)
    {
        $task_c = new Task_Action();
        $responsecode = $task_c->list_ss_task($input,$output);
        if($responsecode == CPAPIStatus::ListTaskSuccess)
            $this->output_json($output);
        else
            $task_c->process_task_error_code($responsecode);
    }

    function cancel_task($input)
    {        
        $task_c = new Task_Action();        
        $responsecode = $task_c->cancel_task($input);
        if($responsecode != CPAPIStatus::CancelTaskSuccess)
            $task_c->process_task_error_code($responsecode);
    }

    function snapshot_action_two_argc($idDomain,$argc,$input)
    {        
        if (preg_match('/^snapshot\/[a-z,0-9,-]+$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {                
                case "DELETE":                    
                    $input = array();
                    $input['LayerID']=$argc[1];
                    $input['DomainID']=$idDomain;                    
                    $this->delete_snapshot($input);
                    break;                
                default:
                    http_response_code(404);
            }
        }        
    }

    function delete_snapshot($input)
    {                
        $snapshot_c = new Snapshot_Action();
        $responsecode = $snapshot_c->create_delete_snapshot_task($input);        
        if($responsecode != CPAPIStatus::DeleteSnapshotSuccess)
            $snapshot_c->process_snapshot_error_code($responsecode);
    }

    function task_action_three_argc($idDomain,$argc,$input)
    {        
        if (preg_match('/^snapshot\/[-,A-Z,a-z,0-9,\.,:]+$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {                
                case "DELETE":
                    $input = array();
                    $input['TaskID']=$argc[2];
                    $input['DomainID']=$idDomain;                    
                    $this->cancel_ss_task($input);
                    break;                
                default:
                    http_response_code(404);
            }
        }
    }

    function cancel_ss_task($input)
    {        
        $task_c = new Task_Action();
        $responsecode = $task_c->cancel_task($input,'snapshot');
        if($responsecode != CPAPIStatus::CancelTaskSuccess)
            $task_c->process_task_error_code($responsecode);
    }

    function vd_action_three_argc($idDomain, $argc, $input)
    {                
        if (preg_match('/^[a-z,0-9,-]+\/snapshot$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $input['DomainID'] = $idDomain;
                    $input['VDID'] = $argc[1];
                    $this->list_vd_snapshot($input);
                    break;
                default:
                    http_response_code(404);
            }
        } 
        else if (preg_match('/^[a-z,0-9,-]+\/iso$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = json_decode($input, true);
                    $input['DomainID'] = $idDomain;
                    $input['ID'] = $argc[1];
                    $this->modify_vd_iso($input);
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
                    $input['DomainID'] = $idDomain;
                    $input['ID'] = $argc[1];
                    $this->vd_create_seed($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/seed_redeploy$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $input['DomainID'] = $idDomain;
                    $input['ID'] = $argc[1];
                    $this->vd_redeploy_seed($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/clone$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $input['DomainID'] = $idDomain;
                    $input['ID'] = $argc[1];
                    $this->vd_clone($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/uservd$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $input['DomainID'] = $idDomain;
                    $input['ID'] = $argc[1];
                    $this->born_user_vd($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/uservd$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $input['DomainID'] = $idDomain;
                    $input['ID'] = $argc[1];
                    $this->born_user_vd($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[a-z,0-9,-]+\/export$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();
                    $input['DomainID'] = $idDomain;
                    $input['ID'] = $argc[1];
                    $this->export_vd($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[0-9]+\/import$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $input['DomainID'] = $idDomain;
                    $input['GFSID'] = $argc[1];
                    $this->list_import_vd($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }
    
    function list_vd_snapshot($input)
    {        
        $snapshot_c = new Snapshot_Action();        
        $responsecode = $snapshot_c->list_snapshot($input,$output);
        if($responsecode == CPAPIStatus::ListSnapshotSuccess)
            $this->output_json($output);
        else
            $snapshot_c->process_snapshot_error_code($responsecode);        
    }

    function list_import_vd($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->list_import_vd($input, $output);
        if ($responsecode != CPAPIStatus::ListImportSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }
    
    function export_vd($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->export_vd($input);
        if ($responsecode != CPAPIStatus::ExportVDSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        }
    }
    
    function born_user_vd($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->born_user_vd($input);
        // if ($responsecode != CPAPIStatus::BornVDSuccess) {
        //     $vd_c->processVDErrorCode($responsecode);
        // }
    }
    
    function vd_clone($input)
    {
        $vd_c = new VDAction();
        $vd_c->cloneVD($input);
        // if ($responsecode != CPAPIStatus::CloneOrgSuccess) {
        //     $vd_c->processVDErrorCode($responsecode);
        // }
    }
    
    function vd_create_seed($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->create_seed_vd($input);
        if ($responsecode != CPAPIStatus::CreateSeedSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        }
    }
    
    function vd_redeploy_seed($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->recreate_seed_vd($input);
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
    
    function modify_vd_iso($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->modify_vd_iso($input);
        if ($responsecode != CPAPIStatus::ModifyISOSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        }
    }
    
    function resource_action_two_argc($domain_id, $argc, $input)
    {
        if (preg_match('/^resource\/polling$/', $argc[0].'/'.$argc[1])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $input['DomainID']=$domain_id;
                    $this->resource_polling_action($input);
                    break;
                default:
                    http_response_code(404);
            }
        }
    }
    
    function resource_polling_action($input)
    {
        $resource_c = new Resource_Action();
        $responsecode=$resource_c->list_resource_of_domain_polling($input, $output);
        if ($responsecode == CPAPIStatus::ListClusterResourcePollingSuccess) {
            $this->output_json($output);
        } else {
            $resource_c->process_resource_error_code($responsecode);
        }
    }

    function user_action_three_argc($idDomain, $argc, $input)
    {
        if (preg_match('/^[0-9]+\/vd$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $input['DomainID'] = $idDomain;
                    $input['UserID'] = (int)$argc[1];
                    $this->create_vd_of_user_action($input);
                    break;
                case "GET":
                    $input = array();
                    $input['DomainID'] = $idDomain;
                    $input['UserID'] = (int)$argc[1];
                    $this->list_vd_of_user_action($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[0-9]+\/password$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = json_decode($input, true);
                    $input['DomainID'] = $idDomain;
                    $input['UserID'] = (int)$argc[1];
                    $this->modify_user_password_action($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[0-9]+\/profile$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $input['DomainID'] = $idDomain;
                    $input['UserID'] = (int)$argc[1];
                    $this->create_profile_of_user_action($input);
                    break;
                case "DELETE":
                    $input = array();
                    $input['DomainID'] = $idDomain;
                    $input['UserID'] = (int)$argc[1];
                    $this->delete_profile_of_user_action($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[0-9]+\/detail$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                        $input = array();
                        $input['UserID']=$argc[1];
                        $input['DomainID']=$idDomain;
                        $this->list_user_detail_action($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }
    
    function list_user_detail_action($input)
    {
        $user_c = new UserAction();
        $responsecode = $user_c->list_user_info($input, $output);
        if ($responsecode != CPAPIStatus::ListUserInfoSuccess) {
            $user_c->process_user_error_code($responsecode);
        } else {
            $this->output_json($output);
        }
    }
    
    function delete_profile_of_user_action($input)
    {
        $user_c = new UserAction();
        $responsecode = $user_c->delete_user_profile_disk($input);
        if ($responsecode != CPAPIStatus::DeleteUserProfileSuccess) {
            $user_c->process_user_error_code($responsecode);
        }
    }
    
    function create_profile_of_user_action($input)
    {
        $user_c = new UserAction();
        $responsecode = $user_c->create_user_profile_disk($input);
        if ($responsecode != CPAPIStatus::CreateUserProfileSuccess) {
            $user_c->process_user_error_code($responsecode);
        }
    }
    
    function list_vd_of_user_action($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->listVDofUser($input, $output);
        if ($responsecode != CPAPIStatus::ListVDofUserSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }
    
    function create_vd_of_user_action($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->create_vd_of_user($input, $output);
        if ($responsecode != CPAPIStatus::CreateVDofUserSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }
    
    function modify_user_password_action($input)
    {
        $user_c = new UserAction();
        $responsecode = $user_c->modify_user_password($input);
        if ($responsecode != CPAPIStatus::ChangePWDSuccess) {
            $user_c->process_user_error_code($responsecode);
        }
    }
    
    function gfs_action_three_argc($domainid, $argc, $input)
    {
        if (preg_match('/^[0-9]+\/vd$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input, true);
                    $input['DomainID'] = $domainid;
                    $input['GFSID'] = (int)$argc[1];
                    $this->gfs_add_vd_action($input);
                    break;
                case "GET":
                    $input['GFSID'] = (int)$argc[1];
                    $input['DomainID'] = $domainid;
                    $this->gfs_list_vd_action($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[0-9]+\/iso$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $input['DomainID'] = $domainid;
                    $input['GFSID'] = (int)$argc[1];
                    $this->gfs_list_iso_action($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else if (preg_match('/^[0-9]+\/free$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "GET":
                    $input = array();
                    $input['DomainID'] = $domainid;
                    $input['GFSID'] = (int)$argc[1];
                    $this->gfs_list_free_action($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }
    
    function gfs_list_free_action($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->list_free_resource_of_gfs_and_domain($input['DomainID'], $input['GFSID'], $output);
        if ($responsecode != CPAPIStatus::ListGFSFreeSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }
    
    function gfs_list_iso_action($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->list_iso_of_gfs_and_domain($input['DomainID'], $input['GFSID'], $output);
        if ($responsecode != CPAPIStatus::ListISOSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }
    
    function gfs_list_vd_action($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->list_vd_of_gfs_test($input, $output);
        if ($responsecode != CPAPIStatus::ListVDofGfsSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        } else {
            $this->output_json($output);
        }
    }
    
    function gfs_add_vd_action($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->create_vd_of_gfs($input, $output);
        if ($responsecode != CPAPIStatus::CreateVDofGfsSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        }
//        else
//            $this->output_json($output);
    }
    
    function seed_action_three_argc($domainid, $argc, $input)
    {
        if (preg_match('/^[a-z,0-9,-]+\/distribute$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();
                    $input['DomainID'] = $domainid;
                    $input['SeedID'] = $argc[1];
                    $this->seed_distribut_action($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }
    
    function seed_distribut_action($input)
    {
//    var_dump($input);
        $vd_c = new VDAction();
        $vd_c->seed_distribute_uservd($input);
    }
    
    function uservd_action_three_argc($domainid, $argc, $input)
    {
        if (preg_match('/^[a-z,0-9,-]+\/recovery$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();
                    $input['DomainID'] = $domainid;
                    $input['UserVDID'] = $argc[1];
                    $this->uservd_recovery_action($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }
    
    function uservd_recovery_action($input)
    {
        $vd_c = new VDAction();
        $vd_c->uservd_recovery($input);
    }
    
    function job_action_three_argc($domainid, $argc, $input)
    {
        if (preg_match('/^[0-9]+\/job$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "POST":
                    $input = json_decode($input,true);
                    $input['DomainID'] = $domainid;
                    $input['ScheduleID'] = $argc[1];
                    $this->create_job($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }

    function create_job($input)
    {
        $job_c = new Job_Action();
        $responsecode = $job_c->create_job($input,$output);
        // var_dump($output);
        if ($responsecode != CPAPIStatus::CreateJobSuccess) {
            $job_c->process_job_error_code($responsecode);
        }
        else{
            $this->output_json($output);
        }
    }

    function snapshot_action_three_argc($domainid, $argc, $input)
    {
        if (preg_match('/^rollback\/[a-z,0-9,-]+$/', $argc[1].'/'.$argc[2])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();                    
                    $input['LayerID'] = $argc[2];
                    $input['DomainID'] = $domainid;
                    $this->rollback_snapshot($input);
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
                    $input['DomainID'] = $domainid;
                    $this->view_snapshot($input);
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
                    $input['DomainID'] = $domainid;
                    $this->stop_view_snapshot($input);
                    break;
                default:
                    http_response_code(404);
            }
        } 
        else {
            http_response_code(404);
        }
    }

    function rollback_snapshot($input)
    {
        $snapshot_c = new Snapshot_Action();
        $responsecode = $snapshot_c->create_rollback_snapshot_task($input);        
        if($responsecode != CPAPIStatus::RollbackSnapshotSuccess)
            $snapshot_c->process_snapshot_error_code($responsecode);        
    }

    function view_snapshot($input)
    {
        $snapshot_c = new Snapshot_Action();
        $responsecode = $snapshot_c->create_view_snapshot_task($input);        
        if($responsecode != CPAPIStatus::ViewSnapshotSuccess)
            $snapshot_c->process_snapshot_error_code($responsecode);
    }

    function stop_view_snapshot($input)
    {
        $snapshot_c = new Snapshot_Action();
        $responsecode = $snapshot_c->create_stopview_snapshot_task($input);        
        if($responsecode != CPAPIStatus::StopViewSnapshotSuccess)
            $snapshot_c->process_snapshot_error_code($responsecode);   
    }

    function handle_four_argc($api)
    {
        $data = file_get_contents("php://input");
        switch ($api[0]) {
            case 'vd':
                $this->login_check($id_arr);
                $this->vd_action_four_argc($id_arr[0], $api, $data);
                break;
            default:
                http_response_code(404);
        }
    }
    
    function vd_action_four_argc($idDomain, $argc, $input)
    {
        if (preg_match('/^[a-z,0-9,-]+\/user\/[0-9]+$/', $argc[1].'/'.$argc[2].'/'.$argc[3])) {
            switch ($_SERVER['REQUEST_METHOD']) {
                case "PUT":
                    $input = array();
                    $input['DomainID'] = $idDomain;
                    $input['ID'] = $argc[1];
                    $input['UserID'] = (int)$argc[3];
                    $this->vd_assign_user($input);
                    break;
                default:
                    http_response_code(404);
            }
        } else {
            http_response_code(404);
        }
    }
    
    function vd_assign_user($input)
    {
        $vd_c = new VDAction();
        $responsecode = $vd_c->vd_assign_to_user($input);
        if ($responsecode != CPAPIStatus::AssignUserSuccess) {
            $vd_c->processVDErrorCode($responsecode);
        }
    }
}
$api_c = new CustomAPI();
