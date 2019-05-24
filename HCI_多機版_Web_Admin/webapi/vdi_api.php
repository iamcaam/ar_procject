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
include_once("/var/www/html/libraries/HandleRedirection.php");
include_once("/var/www/html/libraries/HandleGluster.php");
include_once("/var/www/html/libraries/HandleAD.php");
include_once("/var/www/html/libraries/ISnapshotShell.php");
include_once("/var/www/html/libraries/IBackupShell.php");

class VDIAPI extends BaseAPI
{
    function __construct()
    {        
        // $fp = fopen('a.txt', 'a');
        // fwrite($fp, $_GET['api'].PHP_EOL);
        $this->handleVDI(); 
    }
    
    function handleVDI()
    {
        $input = file_get_contents("php://input");    
        $cmd = $_GET['api'];
        if(DEBUG){
            $fp = fopen('/mnt/tmpfs/cephcmd/cmd.txt', 'a');
            $gmt = $this->getTZ($timezone);        
            date_default_timezone_set($timezone);     
            fwrite($fp, date("Y-m-d H:i:s").' '.$cmd.' '.$_SERVER['REMOTE_ADDR'].PHP_EOL);   
            $data = file_get_contents("php://input");
            fwrite($fp, $data.PHP_EOL);   
            fclose($fp);
        }  
        // var_dump($api);
        $cmd_arr = explode('/', $cmd); 
        // var_dump($cmd_arr[0]);                           
        $this->getDefaultValues($defaultValues);
        if(is_null($defaultValues)){
            http_response_code(404);
            exit();
        }        
        switch ($cmd_arr[0]){
            case 'check':
                http_response_code(200);
                break;
            case "user":            
                $input = json_decode($input,true);
                $this->setDefaultValueToInput($defaultValues,$input);                      
                $this->handleVDIUser($cmd_arr, $input);
                break;       
            case "password":
                $input = json_decode($input,true);
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->handleVDIChangePWD($input);
                break;
            case "version":
                $input = array();
                $this->setDefaultValueToInput($defaultValues,$input);
                if(isset($_GET['type'])){
                    switch((int)$_GET['type']){
                        case 1:
                            $this->handleJavaVDIVersion();
                            break;
                        default:
                            http_response_code(404);
                            exit();
                    }
                }
                else
                    $this->handleVDIVersion();
                break;
            case "port":
                http_response_code(200);
                break;
            case 'vd':
            case 'userdisk':
                $this->handleVDIVD($cmd_arr,$defaultValues,$input);
                break;
            case 'snapshot':
                $this->handleVDISnapshot($cmd_arr,$defaultValues,$input);
                break;
            case 'snapshot_userdisk':
                $this->handleUserDiskSnapshot($cmd_arr,$defaultValues,$input);
                break;
            default:
                http_response_code(404);
                exit();
        }
    }

    function getDefaultValues(&$output)
    {    
        $output = NULL;
        if($this->connectDB()){
            $domainC = new DomainAction();                
            if($domainC->listDefaultDomain($output_default_domain)){
                $ceph_c = new CephPoolAction();
                if($ceph_c->list_default_one_ceph_and_server($output_default_ceph)){
                    $output['DomainName'] = $output_default_domain['DomainName'];
                    $output['DomainID'] = $output_default_domain['DomainID'];
                    $output['CephID'] = $output_default_ceph['CephID'];
                    $output['ConnectIP'] = "127.0.0.1";
                }                                            
            }
        }
    }

    function handleJavaVDIVersion(){
        $redirect_c = new RedirectionAction();
        $redirect_c->listThinClientJavaFirmwareVersion();
    }

    function handleVDIVersion(){
        $redirect_c = new RedirectionAction();
        $redirect_c->listThinClientFirmwareVersion();
    }

    function handleVDI_port($input)
    {   
        $redirect_c = new RedirectionAction();
        $redirect_c->listVDIPort($input);
    }

    function handleVDIChangePWD($input)
    {           
        $user_c = new UserAction();    
        $user_c->modifyUserPasswordForRedirect($input);
    }

    function handleVDIUser($cmd_arr,$input)
    {               
        // var_dump($cmd_arr);
        switch (count($cmd_arr))
        {
            case "3":            
                if(preg_match('/^user\/vd\/.+/',$cmd_arr[0].'/'.$cmd_arr[1].'/'.$cmd_arr[2]))
                { 
                    switch ($_SERVER['REQUEST_METHOD'])
                    {
                        case 'POST':   
                            $this->checkGluster();                                                    
                            $input['ClientID'] = $cmd_arr[2];
                            $redirect_c = new RedirectionAction();                        
                            $redirect_c->listUserInfo($input);    
                            break;
                        case 'PUT':               
                            $this->checkGluster();         
                            $redirect_c = new RedirectionAction();
                            $input['VDID'] = $cmd_arr[2];             
                            $redirect_c->setVDOnline($input);
                            break;
                        case 'GET':
                            $this->checkGluster();                              
                            $input['VDID'] = $cmd_arr[2]; 
                            $this->listVDState($input);    
                            break;
                        default :
                            http_response_code(404);
                    }
                }
                else if(preg_match('/^user\/vd_rdp\/.+/',$cmd_arr[0].'/'.$cmd_arr[1].'/'.$cmd_arr[2]))
                { 
                    switch ($_SERVER['REQUEST_METHOD'])
                    {                        
                        case 'PUT':               
                            $this->checkGluster();         
                            $redirect_c = new RedirectionAction();
                            $input['VDID'] = $cmd_arr[2];             
                            $redirect_c->setVDOnlineRDP($input);
                            break;
                        case 'GET':
                            $this->checkGluster();         
                            $redirect_c = new RedirectionAction();
                            $input['VDID'] = $cmd_arr[2];                                     
                            $redirect_c->listVDRDPInfo($input,$output);
                            $this->output_json($output);
                            break;
                        default :
                            http_response_code(404);
                    }
                }
                else
                    http_response_code(404);
                break;           
            case "4":
                if(preg_match('/^user\/vd\/.+\/action$/',$cmd_arr[0].'/'.$cmd_arr[1].'/'.$cmd_arr[2].'/'.$cmd_arr[3]))
                { 
                    switch ($_SERVER['REQUEST_METHOD'])
                    {
                        case 'PUT':       
                            $this->checkGluster();                                 
                            $redirect_c = new RedirectionAction();
                            $input['VDID'] = $cmd_arr[2];
                            $redirect_c -> vdAction($input);
                            break;
                        default :
                            http_response_code(404);
                    }
                }
                else if(preg_match('/^user\/vd\/.+\/info$/',$cmd_arr[0].'/'.$cmd_arr[1].'/'.$cmd_arr[2].'/'.$cmd_arr[3]))
                { 
                    switch ($_SERVER['REQUEST_METHOD'])
                    {
                        case 'PUT':      
                            $this->checkGluster();                                  
                            $redirect_c = new RedirectionAction();
                            $input['VDID'] = $cmd_arr[2];
                            $redirect_c ->vdModify($input);
                            break;
                        default :
                            http_response_code(404);
                    }
                }
                else if(preg_match('/^user\/vd\/.+\/default$/',$cmd_arr[0].'/'.$cmd_arr[1].'/'.$cmd_arr[2].'/'.$cmd_arr[3]))
                { 
                    switch ($_SERVER['REQUEST_METHOD'])
                    {
                        case 'PUT':      
                            $this->checkGluster();                                  
                            $redirect_c = new RedirectionAction();
                            $input['VDID'] = $cmd_arr[2];
                            $redirect_c ->setVDDefault($input);
                            break;
                        default :
                            http_response_code(404);
                    }
                }
                else
                    http_response_code(404);
                break;
            default :
                http_response_code(404);
        }
    }

    function listVDState($input)
    {
        $redirect_c = new RedirectionAction();
        $redirect_c->listVDState($input,$output);
        $this->output_json($output);
    }

    function handleVDIVD($cmd_arr,$defaultValues,$input)
    {               
        // var_dump($cmd_arr);
        switch (count($cmd_arr))
        {
            case "3":            
                if(preg_match('/^vd\/[a-z,0-9,-]+\/snapshot$/',$cmd_arr[0].'/'.$cmd_arr[1].'/'.$cmd_arr[2]))
                { 
                    switch ($_SERVER['REQUEST_METHOD'])
                    {
                        case 'GET':   
                            $this->checkGluster();                                                    
                            $input['VDID'] = $cmd_arr[1];
                            $this->setDefaultValueToInput($defaultValues,$input); 
                            // var_dump($input);
                            $this->listVDSnapshot($input);
                            break;                        
                        default :
                            http_response_code(404);
                    }
                }else if(preg_match('/^userdisk\/.+\/snapshot$/',$cmd_arr[0].'/'.$cmd_arr[1].'/'.$cmd_arr[2]))
                { 
                    switch ($_SERVER['REQUEST_METHOD'])
                    {
                        case 'GET':   
                            $this->checkGluster();                                                    
                            $input['DiskName'] = $cmd_arr[1];                   
                            $this->listUserDiskSnapshot($input);
                            break;                        
                        default :
                            http_response_code(404);
                    }
                }                
                else
                    http_response_code(404);
                break;            
            default :
                http_response_code(404);
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

    function listUserDiskSnapshot($input)
    {
        $snapshot_c = new SnapshotAction();        
        $responsecode = $snapshot_c->listUserDiskSnapshotByName($input,$output);
        if($responsecode == CPAPIStatus::ListSnapshotSuccess)
            $this->output_json($output);
        else
            $snapshot_c->processSnapshotErrorCode($responsecode); 
    }

    function handleVDISnapshot($cmd_arr,$defaultValues,$input)
    {               
        // var_dump($cmd_arr);
        switch (count($cmd_arr))
        {
            case "3":            
                if(preg_match('/^snapshot\/view\/[a-z,0-9,-]+/',$cmd_arr[0].'/'.$cmd_arr[1].'/'.$cmd_arr[2]))
                { 
                    switch ($_SERVER['REQUEST_METHOD'])
                    {
                        case 'PUT':   
                            $this->checkGluster();   
                            $input = json_decode($input,true);                                                 
                            $input['LayerID'] = $cmd_arr[2];
                            $this->setDefaultValueToInput($defaultValues,$input); 
                            $this->viewSnapshot($input);
                            break;                        
                        default :
                            http_response_code(404);
                    }
                }
                else if(preg_match('/^snapshot\/rollback\/[a-z,0-9,-]+/',$cmd_arr[0].'/'.$cmd_arr[1].'/'.$cmd_arr[2]))
                { 
                    switch ($_SERVER['REQUEST_METHOD'])
                    {
                        case 'PUT':   
                            $this->checkGluster();   
                            $input = array();                                                 
                            $input['LayerID'] = $cmd_arr[2];
                            $this->setDefaultValueToInput($defaultValues,$input); 
                            $this->rollbackSnapshot($input);
                            break;                        
                        default :
                            http_response_code(404);
                    }
                }
                else if(preg_match('/^snapshot\/login\/[a-z,0-9,-]+/',$cmd_arr[0].'/'.$cmd_arr[1].'/'.$cmd_arr[2]))
                { 
                    switch ($_SERVER['REQUEST_METHOD'])
                    {
                        case 'PUT':   
                            $this->checkGluster();                                                    
                            $input = array();                                                 
                            $input['LayerID'] = $cmd_arr[2];
                            $this->setDefaultValueToInput($defaultValues,$input); 
                            $this->loginSnapshot($input);
                            break;                        
                        default :
                            http_response_code(404);
                    }
                }
                else if(preg_match('/^snapshot\/stop_view\/[a-z,0-9,-]+/',$cmd_arr[0].'/'.$cmd_arr[1].'/'.$cmd_arr[2]))
                { 
                    switch ($_SERVER['REQUEST_METHOD'])
                    {
                        case 'PUT':      
                            $this->checkGluster();       
                            $input = array();                           
                            $input['VDID'] = $cmd_arr[2];
                            $this->setDefaultValueToInput($defaultValues,$input);
                            $this->unviewSnapshot($input);
                            break;
                        default :
                            http_response_code(404);
                    }
                }
                else
                    http_response_code(404);
                break;            
            default :
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

    function unviewSnapshot($input)
    {
        $snapshot_c = new SnapshotAction();
        $responsecode = $snapshot_c->stopViewSnapshot($input);        
        if($responsecode != CPAPIStatus::StopViewSnapshotSuccess)
            $snapshot_c->processSnapshotErrorCode($responsecode);
    }

    function loginSnapshot($input)
    {
        $snapshot_c = new SnapshotAction();
        $snapshot_c->loginSnapshot($input);                
    }

    function handleUserDiskSnapshot($cmd_arr,$defaultValues,$input)
    {               
        // var_dump($cmd_arr);
        switch (count($cmd_arr))
        {
            case "3":            
                if(preg_match('/^snapshot_userdisk\/view\/[a-z,0-9,-]+/',$cmd_arr[0].'/'.$cmd_arr[1].'/'.$cmd_arr[2]))
                { 
                    switch ($_SERVER['REQUEST_METHOD'])
                    {
                        case 'PUT':   
                            $this->checkGluster();   
                            $input = json_decode($input,true);                                              
                            $input['LayerID'] = $cmd_arr[2];      
                            $this->setDefaultValueToInput($defaultValues,$input);                      
                            $this->viewUserDiskSnapshot($input);
                            break;                        
                        default :
                            http_response_code(404);
                    }
                }
                else if(preg_match('/^snapshot_userdisk\/stop_view\/.+/',$cmd_arr[0].'/'.$cmd_arr[1].'/'.$cmd_arr[2]))
                { 
                    switch ($_SERVER['REQUEST_METHOD'])
                    {
                        case 'PUT':      
                            $this->checkGluster();       
                            $input = array();                           
                            $input['DiskName'] = $cmd_arr[2];    
                            $this->setDefaultValueToInput($defaultValues,$input);                        
                            $this->unviewUserDiskSnapshot($input);
                            break;
                        default :
                            http_response_code(404);
                    }
                }
                else if(preg_match('/^snapshot_userdisk\/rollback\/[a-z,0-9,-]+/',$cmd_arr[0].'/'.$cmd_arr[1].'/'.$cmd_arr[2]))
                { 
                    switch ($_SERVER['REQUEST_METHOD'])
                    {
                        case 'PUT':     
                            $this->checkGluster();                          
                            $input = array();                                                 
                            $input['LayerID'] = $cmd_arr[2];     
                            $this->setDefaultValueToInput($defaultValues,$input);                       
                            $this->rollbackUserDiskSnapshot($input);
                            break;                        
                        default :
                            http_response_code(404);
                    }
                }
                else
                    http_response_code(404);
                break;            
            default :
                http_response_code(404);
        }
    }

    function rollbackUserDiskSnapshot($input,$rtn)
    {
        $snapshot_c = new SnapshotAction();
        $responsecode = $snapshot_c->createRollbackUPSnapshotTask($input);        
        if($responsecode != CPAPIStatus::RollbackSnapshotSuccess){
            if($rtn != 0){
                if($rtn != 72)
                    http_response_code(400);
                else
                    http_response_code(403);
            }                
        }       
    }

    function viewUserDiskSnapshot($input)
    {
        $snapshot_c = new SnapshotAction();
        $responsecode = $snapshot_c->viewSnapshotUP($input,$rtn);        
        if($responsecode != CPAPIStatus::ViewSnapshotSuccess){
            if($rtn != 0){
                if($rtn != 72)
                    http_response_code(400);
                else
                    http_response_code(403);
            }                
        }
    }

    function unviewUserDiskSnapshot($input)
    {        
        $snapshot_c = new SnapshotAction();
        $responsecode = $snapshot_c->stopViewUPSnapshot($input,true,$rtn);      
        // var_dump($rtn);  
        if($responsecode != CPAPIStatus::StopViewSnapshotSuccess){
            if($rtn != 0){
                if($rtn != 72)
                    http_response_code(400);
                else
                    http_response_code(403);
            }                
        }
    }

    function checkGluster()
    {
        $glusterC = new GlusterAction();
        $glusterC->checkGlusterForLogin($data,$outputInfo);      
        // var_dump($outputInfo);      
        if($outputInfo['HaveGluster'] && $outputInfo['VMSIP'] != $_SERVER['SERVER_ADDR']){
            http_response_code(500);
            exit();
        }
    }
}
$vdAPI = new VDIAPI();