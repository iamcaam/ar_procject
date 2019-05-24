<?php
define("SANDEBUG", false);
// define("DEBUG", false);
// ini_set('display_errors', 'On');
include_once("/var/www/html/webapi/api.php");
class SANAPI extends BaseAPI
{
	function __construct()
    {          
        // var_dump($_SERVER);    
        $this->getSessionPath();  
    	$api = $_GET['api'];      
        if(SANDEBUG){
            $data = file_get_contents("php://input");
            if($_SERVER['REQUEST_METHOD'] != "GET"){
                $fp = fopen('/mnt/tmpfs/cephcmd/cmd.txt', 'a');
                $gmt = $this->getTZ($timezone);        
                date_default_timezone_set($timezone);     
                fwrite($fp, date("Y-m-d H:i:s").' '.$api.' '.$_SERVER['REMOTE_ADDR'].PHP_EOL);            
                $curlExec="curl -o /dev/null -s -w \"%{http_code}\\n}\" -k --request ".$_SERVER['REQUEST_METHOD']." https://".$_SERVER['SERVER_ADDR']."/san/$api ";
                if(strlen($data) > 0)
                    $curlExec .= "--data '$data'";
                fwrite($fp, $curlExec.PHP_EOL);
                // fwrite($fp, $data.PHP_EOL);
                fclose($fp);
            }
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
        	default:        		
        		break;
        }       
        // echo 1;        
        $apiC = new API($this->sessionPath);
    }    

    function getSessionPath()
    {
        if(!file_exists("/artgluster/LocalDB/manager/session"))
            $this->sessionPath = "/LocalDB/manager/session";
        else
            $this->sessionPath = "/artgluster/LocalDB/manager/session";
    }

    function handleOneArgc($api)
    {
        $data = file_get_contents("php://input");          
        switch ($api[0]){
            case 'check':
                $glusterC = new GlusterAction();
                $data = array();
                $data['ConnectIP'] = '127.0.0.1';
                $glusterC->checkGlusterForLogin($data,$outputInfo);    
                // var_dump($outputInfo);
                if($outputInfo['HaveGluster'] && $outputInfo['VMSIP'] != $_SERVER['SERVER_ADDR']){
                    http_response_code(500);
                    exit();
                }
                http_response_code(200);
                exit();
                break;
            case 'rdp':
                $this->rpdActionOneArgc(NULL,$data);
                break;
            case 'gluster':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->glusterActionOneArgc($defaultValues,$data);
                break;
            case 'gluster_summary':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->glusterSummary($defaultValues,$data);
                break;
            case 'login':
                $this->loginActionOneArgc($data);                     
                break;
            case 'pwd':
                $this->changeAdminPWD($data);                        
                break;
            case 'autosuspend':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->autosuspendActionOneArgc($defaultValues,$data);
                break;
            case 'node':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->nodeActionOneArgc($defaultValues,$data);
                break;
            case 'vms':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->vmsActionOneArgc($defaultValues,$data);
                break;
            case 'raid':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->raidActionOneArgc($defaultValues,$data);
                break;
            case 'volume':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->volumeActionOneArgc($defaultValues,$data);
                break;
            case 'mail':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->mailActionOneArgc($defaultValues,$data);
                break;
            case 'test':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $input = array();
                $this->setDefaultValueToInput($defaultValues,$input);
                $input['NodeID'] = 2;
                $input['ConnectIP'] = "192.168.92.61";
                $this->test($input);
                exit();
                break;
        }
    }    

    function test($input)
    {
        $vSwitchC = new vSwitchAction();
        $vSwitchC->initInsertVswitch($input);
    }

    function handleTwoArgc($api)
    {
    	$data = file_get_contents("php://input");          
        switch ($api[0]){
            case 'login':         
            	$this->loginActionTwoArgc($api);            
            	break;          
            case 'rdp':
                $this->rdpActionTwoArgc($api,NULL,$data);
                break;  
            case 'update':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->updateActionTwoArgc($api,$defaultValues,$data);
                break; 
            case 'node':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->nodeActionTwoArgc($api,$defaultValues,$data);
                break;
            case 'gluster':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->glusterActionTwoArgc($api,$defaultValues,$data);
                break;
            case 'split':                
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->splitActionTwoArgc($api,$defaultValues,$data);
                break;
            case 'mail':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->mailActionTwoArgc($api,$defaultValues,$data);
                break;
            case 'volume':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->volumeActionTwoArgc($api,$defaultValues,$data);
                break;
            case 'vswitch':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->vswitchActionTwoArgc($api,$defaultValues,$data);
                break;
        }
    }    

    function handleThreeArgc($api)
    {
        $data = file_get_contents("php://input");          
        switch ($api[0]){
            case 'node':              
                $this->nodeActionThreeArgc($data,$api);
                break;
            case 'rdp':
                $this->rdpActionThreeArgc($api,$defaultValues,$data);
                break;
            case 'vd':
                $this->vdActionThreeArgc($data,$api);
                break;
            case 'volume':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->volumeActionThreeArgc($api,$defaultValues,$data);
                break;            
            case 'gluster':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->glusterActionThreeArgc($api,$defaultValues,$data);
                break;
            case 'pair':
                $this->loginCheck($defaultValues,$this->sessionPath);
                $this->pairActionThreeArgc($api,$defaultValues,$data);
                break;

        }
    }    

    function changeAdminPWD($data)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case 'PUT':
                $data = json_decode($data,true);
                $oLogin = new Login();
                $eLoginStatus = $oLogin->Change_Password(hash('sha512', $data['OldPWD']), hash('sha512', $data['NewPWD']),'/artgluster/LocalDB/manager/pwd','/artgluster/LocalDB/manager/slat');
                if($eLoginStatus == APIStatus::ChangePWDOldPWDFail){
                    http_response_code(400);
                }
                exit();
                break;
        }
    }

    function loginActionOneArgc($data)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case 'POST':
                $this->login($data);
                exit();
                break;
            default:
                http_response_code(404);
                break;
        }               
    }

    function login($data)
    {
        $oLogin = new Login();
        $data = json_decode($data, true);
        // var_dump($data);
        $estatus = APIStatus::LoginFail;
        $pwd =  base64_decode($data['Password']);
        $pwd = $this->binary_to_hex2_str($pwd);
        $data['ConnectIP'] = '127.0.0.1';
        $glusterC = new GlusterAction();
        $glusterC->checkGlusterForLogin($data,$outputInfo);    
        // var_dump($outputInfo);
        if($outputInfo['HaveGluster'] && $outputInfo['VMSIP'] != $_SERVER['SERVER_ADDR']){
            http_response_code(500);
            exit();
        }
        if(isset($data['Username']))
        {            
            if($data['Username'] == 'admin'){
                $vmsIP = NULL;
                $vmsMask = NULL;
                if(strlen($outputInfo['VMSIP']) > 0){
                    $vmsIP = $outputInfo['VMSIP'];
                    $vmsMask = $outputInfo['VMSMask'];
                }
                $status = $oLogin->Login_By_Data($data['Username'], $pwd, $this->sessionPath,$vmsIP,$vmsMask);
            }
        }
        if($status != APIStatus::LoginSuccess)
            http_response_code(401);
    }

    function rpdActionOneArgc($defaultValues,$input)
    {
        $glusterC = new GlusterAction();
        $data = array();
        $data['ConnectIP'] = '127.0.0.1';
        $glusterC->checkGlusterForLogin($data,$outputInfo);    
        // var_dump($outputInfo);
        if($outputInfo['HaveGluster'] && $outputInfo['VMSIP'] != $_SERVER['SERVER_ADDR']){
            http_response_code(500);
            exit();
        }
        switch ($_SERVER['REQUEST_METHOD']) {             
            case 'POST':
                $input = json_decode($input,true);                
                $this->setRDPInfo($input);
                exit();
                break;           
            default:
                http_response_code(404);
                break;
        }        
    }

    function setRDPInfo($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->insertRDPInfo($input,$output);
        if($responsecode != CPAPIStatus::InsertRDPSuccess)
            $vdC->processVDErrorCode($responsecode);
        else
            $this->output_json($output);
    }

    function mailActionOneArgc($defaultValues,$input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {    
            case 'GET':
                $input = array();
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->listMail($input);
                exit();                
                break;       
            case 'POST':
                $input = json_decode($input,true);
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->setMail($input);
                exit();
                break;           
            default:
                http_response_code(404);
                break;
        }        
    }

    function listMail($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->listMail($input,$output);
        if($responsecode != CPAPIStatus::ListMailSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
        else
            $this->output_json($output);
    }

    function setMail($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->setMail($input);
        if($responsecode != CPAPIStatus::SetMailSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
    }

    function vmsActionOneArgc($defaultValues,$input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {           
            case 'PUT':
                $input = array();
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->setVMS($input);
                exit();
                break;           
            default:
                http_response_code(404);
                break;
        }               
    }

    function setVMS($input)
    {
        $nodeC = new NodeAction();
        $responsecode = $nodeC->nodeSetVMS($input);
        if($responsecode != CPAPIStatus::SetNodeVMSSuccess)
            $nodc->processNodeErrorCode($responsecode);
    }

    function glusterSummary($defaultValues,$input)
    {        
        $input = array();
        $this->setDefaultValueToInput($defaultValues,$input);
        // var_dump($input);
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->listGlusterSummary($input,$output);
        // var_dump($responsecode);
        if($responsecode != CPAPIStatus::ListGlusterSummarySuccess)
            $glusterC->processGlusterErrorCode($responsecode);
        $this->output_json($output);
        exit();
    }

    function glusterActionOneArgc($defaultValues,$input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case 'POST':
                $input = json_decode($input,true);                
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->createGluster($input);
                exit();
                break;                       
            case 'DELETE':
                $input = array();
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->deleteGluster($input);
                exit();
                break;
            case 'GET':
                $input = array();
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->listGluster($input);
                exit();
                break;
        }               
    }

    function createGluster($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->createGluster($input,$rtn,$errMsg);
        if($responsecode != CPAPIStatus::AddClusterSuccess){            
            $glusterC->processGlusterErrorCode($responsecode);
            $responseBody = array('ErrorCode' => $rtn,'ErrorMsg'=>$errMsg);
            $this->output_json($responseBody);
        }
    }

    function deleteGluster($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->deleteGluster($input);
        $glusterC->processGlusterErrorCode($responsecode);
    }

    function listGluster($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->listGluster($input,$output);
        $this->output_json($output);
    }

    function autosuspendActionOneArgc($defaultValues,$input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case 'GET':
                $input = array();                
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->listAutosuspend($input);
                exit();
                break;
            case 'PUT':
                $input = json_decode($input,true);                
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->setAutosuspend($input);
                exit();
                break;           
        }               
    }

    function listAutosuspend($input)
    {
        $vdC = new VDAction();
        $vdC->listClusterAutosuspend($input,$output);
        $this->output_json($output);        
    }

    function setAutosuspend($input)
    {
        $vdC = new VDAction();
        $vdC->setClusterAutosuspend($input);
    }

    function nodeActionOneArgc($defaultValues,$input)
    {
        // $fp = fopen('/mnt/tmpfs/cephcmd/cmd.txt', 'a');
        // fwrite($fp, date("Y-m-d H:i:s")." list node ".$_SERVER['REMOTE_ADDR'].PHP_EOL);   
        //         $data = file_get_contents("php://input");
        //         fwrite($fp, $data.PHP_EOL);   
        //         fclose($fp);
        switch ($_SERVER['REQUEST_METHOD']) {
            case 'GET':
                $input = array();                
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->listNode($input);
                exit();
                break;        
        }             
    }

    function listNode($input)
    {        
        $nodeC = new NodeAction();
        $responsecode = $nodeC->listNodes($input,$outputNodes);
        if($responsecode == CPAPIStatus :: ListNodesSuccess)
            $this->output_json($outputNodes);
        else
            $nodeC->processNodeErrorCode($responsecode);
    }

    function raidActionOneArgc($defaultValues,$input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case 'GET':
                $input = array();                
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->listRAID($input);
                exit();
                break;        
        }
    }

    function listRAID($input)
    {
        $glusterC = new GlusterAction();
        $glusterC->listClusterRAIDsForCreate($input,$output);   
        $this->output_json($output);
    }

    function volumeActionOneArgc($defaultValues,$input)
    {
        switch ($_SERVER['REQUEST_METHOD']) {
            case 'GET':
                $input = array();
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->listVolume($input);
                exit();
                break;
            case 'POST':
                $input = json_decode($input,true);                
                $this->setDefaultValueToInput($defaultValues,$input);
                $this->addVolume($input);
                exit();
                break;        
            case 'GET':
                break;
        }
    }

    function listVolume($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->listClusterVolumes($input,$output);
        if($responsecode != CPAPIStatus::ListClusterVolumesSuccess)       
            $glusterC->processGlusterErrorCode($responsecode);
        else
            $this->output_json($output);
    }

    function addVolume($input)
    {
        $glusterC = new GlusterAction();
        $input['PairID'] = 1;
        $responsecode = $glusterC->addVolume($input);
        if($responsecode != CPAPIStatus::AddClusterVolumesSuccess)       
            $glusterC->processGlusterErrorCode($responsecode);
    }

    function splitActionTwoArgc($api,$defaultValues,$input)
    {
        // var_dump($api);
        switch ($api[1]) {
            case 'node':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'GET': 
                        $input = array();                    
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listSplitNode($input);
                        exit();
                        break;
                    default:
                        http_response_code(404);
                        break;
                }                               
                break;
            case 'reserve':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'POST': 
                        $input = json_decode($input,true);                        
                        $this->setDefaultValueToInput($defaultValues,$input);
                        // var_dump($input);
                        $this->reserveSplitNode($input);
                        exit();
                        break;
                    default:
                        http_response_code(404);
                        break;
                }                               
                break;
        }
    }

    function listSplitNode($input)
    {        
        $nodeC = new NodeAction();
        $responsecode = $nodeC->listSplitNodes($input,$outputNodes);
        if($responsecode == CPAPIStatus::ListNodesSuccess)
            $this->output_json($outputNodes);
        else
            $nodeC->processNodeErrorCode($responsecode);
    }

    function reserveSplitNode($input)
    {
        $nodeC = new NodeAction();
        $responsecode = $nodeC->reserveSplitNode($input);
        if($responsecode != CPAPIStatus::ReserveNodeSuccess)
            $nodeC->processNodeErrorCode($responsecode);
    }

    function vswitchActionTwoArgc($api,$defaultValues,$input)
    {        
        if(preg_match('/^[0-9]+$/',$api[1]))
        {
            switch ($_SERVER['REQUEST_METHOD']) {
                case 'PUT': 
                    $input = json_decode($input,true);
                    $input['VswitchID'] = $api[1];                        
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->modifyVswitch($input);
                    exit();
                    break;
                default:
                    http_response_code(404);
                    break;
            }                                              
        }
    }
    
    function modifyVswitch($input)
    {
        $vSwitchC = new vSwitchAction();
        $responsecode = $vSwitchC->modifyVSwitchName($input);        
        if($responsecode != CPAPIStatus::ModifyVSwitchSuccess){            
            $vSwitchC->process_vswitch_error_code($responsecode);                       
        }     
    }

    function volumeActionTwoArgc($api,$defaultValues,$input)
    {
        // var_dump($api);
        switch ($api[1]) {
            case 'dedupe':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'POST': 
                        $data = array();
                        $data['Volume'] = json_decode($input,true);                    
                        $this->setDefaultValueToInput($defaultValues,$data);
                        $this->setVolumeDedupe($data);
                        exit();
                        break;
                    default:
                        http_response_code(404);
                        break;
                }                               
                break;            
        }
    }
    
    function setVolumeDedupe($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->setVolumeDedupe($input);
        if($responsecode != CPAPIStatus::SetClusterDedupSuccess){            
            $glusterC->processGlusterErrorCode($responsecode);                       
        }     
    }

    function glusterActionTwoArgc($api,$defaultValues,$input)
    {
        switch ($api[1]) {
            case 'restore':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'POST':
                        $input = json_decode($input,true);                
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->restoreGluster($input);
                        exit();
                    default:
                        http_response_code(404);
                        break;
                }               
                break;
            case 'skip_wait':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'PUT':
                        $input = json_decode($input,true);                
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->glusterSkipWaitGroup($input);
                        exit();
                    default:
                        http_response_code(404);
                        break;
                }               
                break;
            case 'wait_node':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'GET':
                        $input = json_decode($input,true);                
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->glusterWaitNode($input);
                        exit();
                    default:
                        http_response_code(404);
                        break;
                }               
                break;
            case 'restore_all':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'POST':
                        $input = json_decode($input,true);                
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->restoreGlusterAll($input);
                        exit();
                    default:
                        http_response_code(404);
                        break;
                }               
                break;
            case 'restore_check':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'GET':
                        $input = json_decode($input,true);                
                        $this->setDefaultValueToInput($defaultValues,$input); 
                        $this->glusterRestoreCheck($input);                       
                        exit();
                    default:
                        http_response_code(404);
                        break;
                }               
                break;
            case 'restore_progress':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'GET':
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->glusterRestoreProgress($input);
                        exit();
                    default:
                        http_response_code(404);
                        break;
                }               
                break;
            case 'check':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'GET':
                        $input = array();                    
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->glusterCheck($input);
                        exit();
                        break;
                    default:
                        http_response_code(404);
                        break;
                }               
                break;
            case 'shutdown':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'PUT':
                        $input = array();                    
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->shutdownGluster($input);
                        exit();
                        break;
                    default:
                        http_response_code(404);
                        break;
                }               
                break;   
            case 'shutdown_all':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'PUT':
                        $input = array();                    
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->shutdownGlusterAll($input);
                        exit();
                        break;
                    default:
                        http_response_code(404);
                        break;
                }               
                break;  
            case 'reboot':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'PUT':
                        $input = array();                    
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->rebootGluster($input);
                        exit();
                        break;
                    default:
                        http_response_code(404);
                        break;
                }               
                break;      
            case 'alarm':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'GET':
                        $input = array();                    
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listGlusterAlarm($input);
                        exit();
                        break;
                    case 'PUT':
                        $input = json_decode($input,true);                        
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->setGlusterAlarm($input);
                        exit();
                        break;
                    default:
                        http_response_code(404);
                        break;
                }               
                break; 
            case 'autosuspend':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'GET':
                        $input = array();                    
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listGlusterAutosuspend($input);
                        exit();
                        break;
                    case 'PUT':
                        $input = json_decode($input,true);                        
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->setGlusterAutosuspend($input);
                        exit();
                        break;
                    default:
                        http_response_code(404);
                        break;
                }               
                break; 
            case 'samba':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'GET':
                        $input = array();                    
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listGlusterSamba($input);
                        exit();
                        break;
                    case 'PUT':
                        $input = json_decode($input,true);                        
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->setGlusterSamba($input);
                        exit();
                        break;
                    default:
                        http_response_code(404);
                        break;
                }               
                break;
            case 'dedupe':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'GET':
                        $input = array();                    
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listClusterDedup($input);
                        exit();
                        break;
                    case 'PUT':
                        $input = json_decode($input,true);                        
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->setClusterDedup($input);
                        exit();
                        break;
                    default:
                        http_response_code(404);
                        break;
                }               
                break;
            case 'ups':
                switch ($_SERVER['REQUEST_METHOD']) {                    
                    case 'POST':
                        $input = json_decode($input,true);                        
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->setUPS($input);
                        exit();
                        break;
                    default:
                        http_response_code(404);
                        break;
                }               
                break;
            case 'pair':
                switch ($_SERVER['REQUEST_METHOD']) {                    
                    case 'POST':
                        $input = json_decode($input,true);                        
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->glusterAddPair($input);
                        exit();
                        break;
                    default:
                        http_response_code(404);
                        break;
                }               
                break;
            case 'split':
                switch ($_SERVER['REQUEST_METHOD']) {                    
                    case 'POST':
                        $input = json_decode($input,true);                        
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->glusterSplitProcess($input);
                        exit();
                        break;
                    default:
                        http_response_code(404);
                        break;
                }     
            case 'miscellaneous':
                switch ($_SERVER['REQUEST_METHOD']) {                    
                    case 'GET':
                        $input = json_decode($input,true);                        
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->glusterListMiscellaneous($input);
                        exit();
                        break;
                    default:
                        http_response_code(404);
                        break;
                }         
                break;
            case 'dns':
                switch ($_SERVER['REQUEST_METHOD']) {                    
                    case 'GET':
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->glusterListDNS($input);
                        exit();
                        break;
                    case 'PUT':
                        $input = json_decode($input,true);
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->glusterSetDNS($input);
                        exit();          
                    default:
                        http_response_code(404);
                        break;
                }         
                break;
            case 'vmsip':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'GET':
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->glusterListVMSIP($input);
                        exit();
                        break;
                    case 'PUT':
                        $input = json_decode($input,true);
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->glusterSetVMSIP($input);
                        exit();          
                    default:
                        http_response_code(404);
                        break;
                }         
                break;
        }
    }

    function glusterSetVMSIP($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->setVMSIP($input);
        if($responsecode != CPAPIStatus::SetVMSIPSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
    }

    function glusterListVMSIP($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->listVMSIP($input,$output);
        if($responsecode != CPAPIStatus::ListVMSIPSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
        else
            $this->output_json($output);
    }

    function glusterSetDNS($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->setDNS($input);
        if($responsecode != CPAPIStatus::SetDNSSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
    }

    function glusterListDNS($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->listDNS($input,$output);
        if($responsecode != CPAPIStatus::ListDNSSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
        else
            $this->output_json($output);
    }

    function glusterListMiscellaneous($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->listGlusterMiscellaneous($input,$output);
        if($responsecode != CPAPIStatus::ListMiscellaneousSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
        else
            $this->output_json($output);
    }

    function glusterWaitNode($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->listGlusterNotReadyNode($input,$output);
        if($responsecode != CPAPIStatus::ListGlusterNotReadyNodeSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
        else
            $this->output_json($output);
    }

    function glusterSkipWaitGroup($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->glusterSkipWatingGroupReady($input);
        if($responsecode != CPAPIStatus::SkipGlusterWaitGroupSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
    }

    function restoreGlusterAll($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->restoreGlusterAll($input);
        if($responsecode != CPAPIStatus::RestoreGlusterSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
    }

    function glusterRestoreCheck($input)
    {
        $glusterC = new GlusterAction();
        $glusterC->checkRestore($input,$output);
        $this->output_json($output);
    }

    function glusterSplitProcess($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->splitAction($input);
        if($responsecode != CPAPIStatus::splitProcessSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
    }

    function glusterRestoreProgress($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->clusterRestoreProgress($input,$output);
        $this->output_json($output);
    }

    function glusterAddPair($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->addGlusterNewGroup($input,$errMsg);
        if($responsecode != CPAPIStatus::AddClusterGroupSuccess){            
            $glusterC->processGlusterErrorCode($responsecode);
            $responseBody = array('ErrorMsg'=>$errMsg);
            $this->output_json($responseBody);                       
        }        
    }

    function setUPS($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->setUPS($input,$output);
        if($responsecode != CPAPIStatus::SetUPSSuccess){            
            $glusterC->processGlusterErrorCode($responsecode);                       
        }
        else
            $this->output_json($output);
    }

    function restoreGluster($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->restoreGluster($input,$rtn);
        if($responsecode != CPAPIStatus::AddClusterSuccess){            
            $glusterC->processGlusterErrorCode($responsecode);
            $responseBody = array('ErrorCode' => $rtn);
            $this->output_json($responseBody);
        }
    }

    function listClusterDedup($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->listClusterDedup($input,$output);
        if($responsecode != CPAPIStatus::ListClusterDedupSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
        else
            $this->output_json($output);
    }

    function setClusterDedup($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->setClusterDedup($input,$output);
        if($responsecode != CPAPIStatus::SetClusterDedupSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
        else
            $this->output_json($output);
    }


    function glusterCheck($input)
    {
        $glusterC = new GlusterAction();
        if($glusterC->checkGluster($input,$output))
            $this->output_json($output);
        else
            http_response_code(400);
    }    

    function shutdownGluster($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->shutdownGluster($input);
        if($responsecode != CPAPIStatus::ShutdownClusterSuccess)
            http_response_code(400);       
    }

    function shutdownGlusterAll($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->shutdownGlusterAll($input);
    }

    function rebootGluster($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->rebootGluster($input);   
        if($responsecode != CPAPIStatus::RebootClusterSuccess)
            http_response_code(400);
    }

    function listGlusterAlarm($input)
    {
        $glusterC = new GlusterAction();
        $glusterC->listGlusterAlarm($input,$output);
        $this->output_json($output);
    }

    function setGlusterAlarm($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->setGlusterAlarm($input);
        if($responsecode != CPAPIStatus::SetClusterAlarmSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
    }

    function listGlusterAutosuspend($input)
    {
        $glusterC = new GlusterAction();
        $glusterC->listGlusterAutosuspend($input,$output);
        $this->output_json($output);   
    }

    function setGlusterAutosuspend($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->setGlusterAutosuspend($input);
        if($responsecode != CPAPIStatus::SetClusterAutosuspendSuccess)
            $glusterC->processGlusterErrorCode($responsecode);       
    }

    function listGlusterSamba($input)
    {
        $glusterC = new GlusterAction();
        $glusterC->listGlusterSambaStatus($input,$output);
        $this->output_json($output);
    }

    function setGlusterSamba($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->setGlusterSambaStatus($input);
        if($responsecode != CPAPIStatus::SetClusterSambaSuccess)
            $glusterC->processGlusterErrorCode($responsecode);       
    }

    function loginActionTwoArgc($api)
    {      
    	switch ($api[1]) {
    		case 'controller':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'GET':
                        $this->loginCheckServer();
                        exit();
                        break;
                    default:
                        http_response_code(404);
                        break;
                }    			
    			break;    	    		
    	}
    }

    function loginCheckServer()
    {        
    	$nodeC = new NodeAction();
        $responsecode = $nodeC->listNodesForLoginCheck($output);        
        if($responsecode == CPAPIStatus::ListNodesForLoginCheckSuccess){
            $this->output_json($output);
        }
        else {
            $nodeC->processNodeErrorCode($responsecode);            
        }
    }

    function rdpActionTwoArgc($api,$defaultValues,$input)
    {
        $glusterC = new GlusterAction();
        $data = array();
        $data['ConnectIP'] = '127.0.0.1';
        $glusterC->checkGlusterForLogin($data,$outputInfo);    
        // var_dump($outputInfo);
        if($outputInfo['HaveGluster'] && $outputInfo['VMSIP'] != $_SERVER['SERVER_ADDR']){
            http_response_code(500);
            exit();
        }
        if(preg_match('/^info$/',$api[1])){
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "POST":   
                    $input = json_decode($input,true);
                    $this->listInfoWithMAC($input);
                    exit();
                    break;
            }
        }
        else if(preg_match('/^[a-z,0-9,-]+$/',$api[1]))
        {
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case 'POST':
                    $input = json_decode($input,true);
                    $input['VDID']=$api[1];                    
                    $this->insertRDPInfoWithVDID($input);
                    exit();
                    break;
                case "PUT":   
                    $input = json_decode($input,true);
                    $input['VDID']=$api[1];                    
                    $this->setVDRDPStatus($input);
                    exit();
                    break;
                case "GET":   
                    $input = json_decode($input,true);
                    $input['VDID']=$api[1];
                    $this->listVDPWDUsername($input);
                    exit();
                    break;
            }
        }   
    }

    function listVDPWDUsername($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->listUserPWDofVD($input,$output);        
        if($responsecode != CPAPIStatus::ListRDPInfoSuccess)
            $vdC->processVDErrorCode($responsecode);
        else
            $this->output_json($output);
    }

    function listInfoWithMAC($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->rdpListInfoWithMAC($input,$output);
        if($responsecode != CPAPIStatus::ListRDPInfoSuccess)
            $vdC->processVDErrorCode($responsecode);
        else
            $this->output_json($output);
    }

    function insertRDPInfoWithVDID($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->insertRDPInfoWithVDID($input,$output);
        if($responsecode != CPAPIStatus::InsertRDPSuccess)
            $vdC->processVDErrorCode($responsecode);       
    }

    function setVDRDPStatus($input)
    {
        
    }

    function updateActionTwoArgc($api,$defaultValues,$input)
    {
        switch ($api[1]) {
            case 'upload':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'POST':
                        $input = json_decode($input,true);
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->updateUpload($input);
                        exit();
                        break;                    
                }               
                break;   
            case 'newversion':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'GET':
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->getUpdateNewVersion($input);
                        exit();
                        break;                    
                }     
                break;               
            case 'viewerversion':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'GET':
                        $input = array();
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listThinClientVersion($input);
                        exit();
                        break;                    
                }
                break;
            case 'viewerupload':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'POST':
                        $input = json_decode($input,true);
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->uploadThinClient($input);
                        exit();
                        break;                    
                }               
                break;   
        }
    }

    function updateUpload($input)
    {
        $updateC = new UpdateAction();
        $updateC->UploadFile($input,'/artgluster/upload/');
        // var_dump($updateC->uploadresult);
        if($updateC->uploadresult != 0)
            http_response_code(400);
    }

    function getUpdateNewVersion($input)
    {
        $updateC = new UpdateAction();
        $updateC->getUpdateFileVersion($input,$output);
        $this->output_json($output);
    }

    function listThinClientVersion($input)
    {
        $updateC = new UpdateAction();
        $responsecode = $updateC->listThinClientVersion($input,$output);
        if($responsecode == CPAPIStatus::ListViewerVersionSuccess)
            $this->output_json($output);
        else 
            $updateC->processUpdateErrorCode($responsecode);
    }

    function uploadThinClient($input)
    {
        $updateC = new UpdateAction();
        $updateC->UploadThinClientFile($input,'/artgluster/ThinClient/upload/');
        // var_dump($updateC->uploadresult);
        if($updateC->uploadresult != 0)
            http_response_code(400);
    }

    function nodeActionTwoArgc($api,$defaultValues,$input)
    {        
        if(preg_match('/^[0-9]+$/',$api[1]))
        {   
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":   
                    $input = array();
                    $input['NodeID']=(int)$api[1];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->listNodeInfo($input);
                    exit();
                    break;
            }
        }
        else if(preg_match('/^poweronvd$/',$api[1]))
        {   
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":   
                    $input = array();                   
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->listNodePoweronVD($input);
                    exit();
                    break;
            }
        }
    }

    function listNodeInfo($input)
    {
        $nodeC = new NodeAction();
        $responsecode = $nodeC->listNodeInfo($input,$outputNodes);
        if($responsecode == CPAPIStatus::ListNodesSuccess)
            $this->output_json($outputNodes);
        else
            $nodeC->processNodeErrorCode($responsecode);
    }

    function listNodePoweronVD($input)
    {
        $nodeC = new NodeAction();
        $responsecode = $nodeC->listNodesPoweronVD($input,$output);
        if($responsecode == CPAPIStatus::ListNodesPoweronVDSuccess)
            $this->output_json($output);
        else
            $nodeC->processNodeErrorCode($responsecode);
    }   

    function mailActionTwoArgc($api,$defaultValues,$input)
    {
        // var_dump($api);
        switch ($api[1]) {
            case 'test':
                switch ($_SERVER['REQUEST_METHOD']) {
                    case 'PUT': 
                        $input = array();                    
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->testMail($input);
                        exit();
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
        $glusterC=new GlusterAction();
        $responsecode = $glusterC->sendTestMail($input);
        if($responsecode != CPAPIStatus::SendTestMailSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
    }

    function glusterActionThreeArgc($api,$defaultValues,$input)
    {        
        if(preg_match('/^pair\/[0-9]+$/',$api[1].'/'.$api[2]))
        {   
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "DELETE":   
                    // var_dump($api); 
                    $input = array();
                    $input['PairID']=$api[2];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->deleteGlusterPair($input);
                    exit();
                    break;
            }
        }
    }

    function deleteGlusterPair($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->deleteGlusterGroup($input);
        if($responsecode != CPAPIStatus::DeleteClusterGroupSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
    }

    function volumeActionThreeArgc($api,$defaultValues,$input)
    {        
        if(preg_match('/^[a-z,0-9,-]+\/disk$/',$api[1].'/'.$api[2]))
        {   
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":   
                    // var_dump($api); 
                    $input = array();
                    $input['VolumeID']=$api[1];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->listVolumeVDsProfile($input);
                    exit();
                    break;
            }
        }
    }

    function listVolumeVDsProfile($input)
    {
        $vdC = new VDAction($input);
        $vdC->listVolumeVDsUserProfile($input,$output);
        $this->output_json($output);
    }

    function vdActionThreeArgc($input,$api)
    {
        if(preg_match('/^[a-z,0-9,-]+\/migrate$/',$api[1].'/'.$api[2]))
        {            
            $this->loginCheck($defaultValues,$this->sessionPath);
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "PUT":                    
                    $input = json_decode($input,true);
                    $input['VDID']=$api[1];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->migrateVD($input);
                    exit();
                    break;                
            }
        }       
    }

    function migrateVD($input)
    {
        $vdC = new VDAction();
        $responsecode = $vdC->migrateVD($input);
        if($responsecode != CPAPIStatus::MigrateVDSuccess)
            $vdC->processVDErrorCode($responsecode);
    }

    function nodeActionThreeArgc($input,$api)
    {
        if(preg_match('/^[0-9]+\/vswitch$/',$api[1].'/'.$api[2]))
        {            
            $this->loginCheck($defaultValues,$this->sessionPath);
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":                    
                    $input = array();
                    $input['NodeID']=(int)$api[1];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->listNodevSwitchNew($input);
                    exit();
                    break; 
                case "PUT":                    
                    $vSwitchs = json_decode($input,true);
                    $data = array('Bonds' => $vSwitchs);
                    $this->setDefaultValueToInput($defaultValues,$data);
                    $data['NodeID']=(int)$api[1];
                    $this->setNodevSwitchNew($data);
                    exit();
                    break;                              
            }
        }        
        else if(preg_match('/^[0-9]+\/vswitch_new$/',$api[1].'/'.$api[2]))
        {            
            $this->loginCheck($defaultValues,$this->sessionPath);
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":                    
                    $input = array();
                    $input['NodeID']=(int)$api[1];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->listNodevSwitchNew($input);
                    exit();
                    break; 
                case "PUT":                    
                    $vSwitchs = json_decode($input,true);
                    $data = array('Bonds' => $vSwitchs);
                    $this->setDefaultValueToInput($defaultValues,$data);
                    $data['NodeID']=(int)$api[1];
                    $this->setNodevSwitchNew($data);
                    exit();
                    break;                              
            }
        }
        else if(preg_match('/^[0-9]+\/cpu_real$/',$api[1].'/'.$api[2]))
        {            
            $this->loginCheck($defaultValues,$this->sessionPath);
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":                    
                    $input = array();
                    $input['NodeID']=(int)$api[1];
                    $input['Type']=$_GET['type'];
                    if(!isset($input['Type']) || $input['Type'] == 0){
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listNodeCPUReal($input);
                    }else{
                        $input['Mode']='cpu';
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listNodeCPUHistory($input);
                    }                    
                    exit();
                    break;                                         
            }
        }
        else if(preg_match('/^[0-9]+\/cpu_history$/',$api[1].'/'.$api[2]))
        {            
            $this->loginCheck($defaultValues,$this->sessionPath);
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":                    
                    $input = array();
                    $input['NodeID']=(int)$api[1];
                    $input['Type']=$_GET['type'];
                    $input['Mode']='cpu';
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->listNodeCPUHistory($input);
                    exit();
                    break;                                         
            }
        }
        else if(preg_match('/^[0-9]+\/mem_real$/',$api[1].'/'.$api[2]))
        {            
            $this->loginCheck($defaultValues,$this->sessionPath);
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":                    
                    $input = array();
                    $input['NodeID']=(int)$api[1];
                    $input['Type']=$_GET['type'];
                    if(!isset($input['Type']) || $input['Type'] == 0){
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listNodeMemReal($input);
                    }else{
                        $input['Mode']='mem';
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listNodeMemHistory($input);
                    }
                    exit();
                    break;                                         
            }
        }      
        else if(preg_match('/^[0-9]+\/mem_history$/',$api[1].'/'.$api[2]))
        {            
            $this->loginCheck($defaultValues,$this->sessionPath);
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":                    
                    $input = array();
                    $input['NodeID']=(int)$api[1];
                    $input['Type']=$_GET['type'];
                    $input['Mode']='mem';
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->listNodeMemHistory($input);
                    exit();
                    break;                                         
            }
        }  
        else if(preg_match('/^[0-9]+\/nic_real$/',$api[1].'/'.$api[2]))
        {            
            $this->loginCheck($defaultValues,$this->sessionPath);
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":                    
                    $input = array();
                    $input['NodeID']=(int)$api[1];
                    $input['Type']=$_GET['type'];
                    if(!isset($input['Type']) || $input['Type'] == 0){
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listNodeNICReal($input);
                    }else{
                        $input['Mode']='nic';
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listNodeNICHistory($input);
                    }
                    exit();
                    break;                                         
            }
        }
        else if(preg_match('/^[0-9]+\/nic_history$/',$api[1].'/'.$api[2]))
        {            
            $this->loginCheck($defaultValues,$this->sessionPath);
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":                    
                    $input = array();
                    $input['NodeID']=(int)$api[1];
                    $input['Type']=$_GET['type'];
                    $input['Mode']='nic';
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->listNodeNICHistory($input);
                    exit();
                    break;                                         
            }
        }
        else if(preg_match('/^[0-9]+\/disk_real$/',$api[1].'/'.$api[2]))
        {            
            $this->loginCheck($defaultValues,$this->sessionPath);
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":                    
                    $input = array();
                    $input['NodeID']=(int)$api[1];
                    $input['Type']=$_GET['type'];
                    if(!isset($input['Type']) || $input['Type'] == 0){
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listNodeDiskReal($input);
                    }else{
                        $input['Mode']='disk';
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listNodeDiskHistory($input);
                    }
                    exit();
                    break;                                         
            }
        }
        else if(preg_match('/^[0-9]+\/disk_history$/',$api[1].'/'.$api[2]))
        {            
            $this->loginCheck($defaultValues,$this->sessionPath);
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":                    
                    $input = array();
                    $input['NodeID']=(int)$api[1];
                    $input['Type']=$_GET['type'];
                    $input['Mode']='disk';
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->listNodeDiskHistory($input);
                    exit();
                    break;                                         
            }
        }    
        else if(preg_match('/^[0-9]+\/raid_real$/',$api[1].'/'.$api[2]))
        {            
            $this->loginCheck($defaultValues,$this->sessionPath);
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":                    
                    $input = array();
                    $input['NodeID']=(int)$api[1];
                    $input['Type']=$_GET['type'];
                    if(!isset($input['Type']) || $input['Type'] == 0){
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listNodeRAIDReal($input);
                    }else{
                        $input['Mode']='raid';
                        $this->setDefaultValueToInput($defaultValues,$input);
                        $this->listNodeRAIDHistory($input);                        
                    }
                    exit();
                    break;                                         
            }
        }
        else if(preg_match('/^[0-9]+\/role$/',$api[1].'/'.$api[2]))
        {            
            $this->loginCheck($defaultValues,$this->sessionPath);
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "PUT":                    
                    $input = array();
                    $input['NodeID']=(int)$api[1];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->changeNodeRole($input);
                    exit();
                    break;                                
            }
        }
        else if(preg_match('/^[0-9]+\/replace$/',$api[1].'/'.$api[2]))
        {            
            $this->loginCheck($defaultValues,$this->sessionPath);
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "PUT":                    
                    $input = json_decode($input,true);
                    $input['NodeID']=(int)$api[1];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->replaceNode($input);
                    exit();
                    break;                                
            }
        }
        else if(preg_match('/^[0-9]+\/locate$/',$api[1].'/'.$api[2]))
        {            
            $this->loginCheck($defaultValues,$this->sessionPath);
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "PUT":                    
                    $input = array();
                    $input['NodeID']=(int)$api[1];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->locateNode($input);
                    exit();
                    break;                                
            }
        }
        else if(preg_match('/^[0-9]+\/polling$/',$api[1].'/'.$api[2]))
        {            
            $this->loginCheck($defaultValues,$this->sessionPath);
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":                    
                    $input = array();
                    $input['NodeID']=(int)$api[1];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->listNodePolling($input);
                    exit();
                    break;                                
            }
        }
        else if(preg_match('/^[0-9]+\/reboot$/',$api[1].'/'.$api[2]))
        { 
            $this->loginCheck($defaultValues,$this->sessionPath,true);
            switch ($_SERVER['REQUEST_METHOD'])
            {             
                case "PUT":                    
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $input['NodeID']=(int)$api[1];
                    $this->rebootNode($input);
                    exit();
                    break;                              
            }
        }
        else if(preg_match('/^[0-9]+\/shutdown$/',$api[1].'/'.$api[2]))
        { 
            $this->loginCheck($defaultValues,$this->sessionPath,true);
            switch ($_SERVER['REQUEST_METHOD'])
            {             
                case "PUT":                    
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $input['NodeID']=(int)$api[1];
                    $this->shutdownNode($input);
                    exit();
                    break;                              
            }
        }
        else if(preg_match('/^[0-9]+\/maintainstate$/',$api[1].'/'.$api[2]))
        { 
            $this->loginCheck($defaultValues,$this->sessionPath);
            switch ($_SERVER['REQUEST_METHOD'])
            {             
                case "PUT":                    
                    $input = json_decode($input,true);
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $input['NodeID']=(int)$api[1];
                    $this->setNodeMaintenance($input);
                    exit();
                    break;                              
            }
        }
        else if(preg_match('/^[0-9]+\/update$/',$api[1].'/'.$api[2]))
        { 
            $this->loginCheck($defaultValues,$this->sessionPath,true);
            switch ($_SERVER['REQUEST_METHOD'])
            {             
                case "PUT":                    
                    $input = json_decode($input,true);
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $input['NodeID']=(int)$api[1];
                    $this->updateNode($input);
                    exit();
                    break;                              
            }
        }
        else if(preg_match('/^[0-9]+\/update_progress$/',$api[1].'/'.$api[2]))
        { 
            $this->loginCheck($defaultValues,$this->sessionPath,true);
            switch ($_SERVER['REQUEST_METHOD'])
            {             
                case "GET":                    
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $input['NodeID']=(int)$api[1];
                    $this->updateNodeProgress($input);
                    exit();
                    break;                              
            }
        }
        else if(preg_match('/^[0-9]+\/external$/',$api[1].'/'.$api[2]))
        { 
            // echo 1;
            $this->loginCheck($defaultValues,$this->sessionPath,true);
            switch ($_SERVER['REQUEST_METHOD'])
            {             
                case "GET":                    
                    $input = array();
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $input['NodeID']=(int)$api[1];
                    $this->listNodeExternal($input);
                    exit();
                    break;     
                case 'POST':
                    $data = array();
                    $data['Externals'] = json_decode($input,true);                         
                    $this->setDefaultValueToInput($defaultValues,$data);
                    $data['NodeID']=(int)$api[1];
                    $this->setNodeExternal($data);
                    exit();
                    break;
            }
        }
        else if(preg_match('/^.+\/check$/',$api[1].'/'.$api[2]))
        {            
            // echo 1;
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":                    
                    $input = array();                   
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $ip = base64_decode($api[1]);
                    if (filter_var($ip, FILTER_VALIDATE_IP) === false) {
                        http_response_code(400);
                    }
                    $input['ConnectIP'] = $ip;
                    $this->checkNode($input);
                    exit();
                    break;                                     
            }
        }        
    }    

    function listNodeDiskReal($input)
    {
        $nodePerformanceC = new NodePerformanceAction();
        $nodePerformanceC->listNodeDiskRealPerformance($input);
    }

    function listNodeDiskHistory($input)
    {
        $nodePerformanceC = new NodePerformanceAction();
        $nodePerformanceC->listNodeNICDiskHistoryPerformance($input);
    }

    function listNodeRAIDHistory($input)
    {
        $nodePerformanceC = new NodePerformanceAction();
        $nodePerformanceC->listNodeNICDiskHistoryPerformance($input);   
    }

    function listNodeRAIDReal($input)
    {
        $nodePerformanceC = new NodePerformanceAction();
        $nodePerformanceC->listNodeRAIDRealPerformance($input);
    }

    function listNodeNICReal($input)
    {
        $nodePerformanceC = new NodePerformanceAction();
        $nodePerformanceC->listNodeNICRealPerformance($input);
    }

    function listNodeNICHistory($input)
    {
        $nodePerformanceC = new NodePerformanceAction();
        $nodePerformanceC->listNodeNICDiskHistoryPerformance($input);
    }

    function listNodeMemReal($input)
    {
        $nodePerformanceC = new NodePerformanceAction();
        $nodePerformanceC->listNodeMemRealPerformance($input);
    }

    function listNodeMemHistory($input)
    {
        if(!isset($input['Type']))
            http_response_code(400);
        $nodePerformanceC = new NodePerformanceAction();
        $nodePerformanceC->listNodeCPUMemHistoryPerformance($input);
    }

    function listNodeCPUReal($input)
    {
        $nodePerformanceC = new NodePerformanceAction();
        $nodePerformanceC->listNodeCPURealPerformance($input);
    }

    function listNodeCPUHistory($input)
    {
        if(!isset($input['Type']))
            http_response_code(400);
        $nodePerformanceC = new NodePerformanceAction();
        $nodePerformanceC->listNodeCPUMemHistoryPerformance($input);
    }

    function locateNode($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->locateNode($input);
        if($responsecode != CPAPIStatus::LocateNodeSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
    }

    function changeNodeRole($input)
    {
        $nodeC = new NodeAction();
        $responsecode = $nodeC->changeNodeRole($input);
        if($responsecode != CPAPIStatus::ChangeNodeRoleSuccess)
            $nodeC->processNodeErrorCode($responsecode);        
    }

    function replaceNode($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->replaceGlusterNodeWithNewIP($input,$errMsg);
        if($responsecode != CPAPIStatus::ReplaceClusterNodeSuccess){
            $glusterC->processGlusterErrorCode($responsecode);
            $responseBody = array('ErrorMsg'=>$errMsg);
            $this->output_json($responseBody);       
        }
    }

    function setNodeExternal($input)
    {
        $ipC = new IPProcess();
        $responsecode = $ipC->setNodeExternal($input,$output);      
        if($responsecode != CPAPIStatus::SetNodeExternalSuccess)
            $ipC->processNetworkErrorCode($responsecode);
        else
            $this->output_json($output);
    }

    function listNodeExternal($input)
    {
        $ipC = new IPProcess();
        $ipC->listNodeExternal($input,$output);
        $this->output_json($output);
    }

    function listNodePolling($input)
    {
        $nodeC = new NodeAction();
        $nodeC->listNodePolling($input,$output);
        $this->output_json($output);
    }

    function checkNode($input)
    {
        $nodeC = new NodeAction();
        $responsecode = $nodeC->checkNode($input,$output);
        // var_dump($output);
        if($responsecode == CPAPIStatus::CheckNodeSuccess)
            $this->output_json($output);
        else
            $nodeC->processNodeErrorCode($responsecode);
    }

    function listNodevSwitch($input)
    {
        $vSwitchC = new vSwitchAction();
        $responsecode = $vSwitchC->listNodevSwitch($input,$output);
        if($responsecode == CPAPIStatus::ListVSwitchSuccess)
            $this->output_json($output);
        else
            $vSwitchC->process_vswitch_error_code($responsecode);
    }

    function listNodevSwitchNew($input)
    {
        $vSwitchC = new vSwitchAction();
        $responsecode = $vSwitchC->listNodevSwitchNew($input,$output);
        if($responsecode == CPAPIStatus::ListVSwitchSuccess)
            $this->output_json($output);
        else
            $vSwitchC->process_vswitch_error_code($responsecode);
    }

    function setNodevSwitch($input)
    {
        $vSwitchC = new vSwitchAction();
        $responsecode = $vSwitchC->setNodevSwitch($input);
        if($responsecode != CPAPIStatus::SetVSwitchSuccess)        
            $vSwitchC->process_vswitch_error_code($responsecode);
    }

    function setNodevSwitchNew($input)
    {
        $vSwitchC = new vSwitchAction();
        $responsecode = $vSwitchC->setNodevSwitchNew($input,$output);
        if($responsecode != CPAPIStatus::SetVSwitchSuccess)        
            $vSwitchC->process_vswitch_error_code($responsecode);
        else{
            $this->output_json($output);
        }
    }

    function rebootNode($input)
    {
        $nodeC = new NodeAction();
        $nodeC->rebootNode($input);
    }

    function shutdownNode($input)
    {
        $nodeC = new NodeAction();
        $nodeC->shutdownNode($input);
    }

    function setNodeMaintenance($input)
    {
        $nodeC = new NodeAction();
        $responsecode = $nodeC->setNodeMaintenance($input);
        if($responsecode != CPAPIStatus::SetNodeMaintainanceSuccess)
            $nodeC->processNodeErrorCode($responsecode);
    }

    function updateNode($input)
    {
        $updateC = new UpdateAction();
        $responsecode = $updateC->updateFW($input);
        if($responsecode != CPAPIStatus::UpdateNodeSuccess)
            $nodeC->processUpdateErrorCode($responsecode);
    }

    function updateNodeProgress($input)
    {        
        $updateC = new UpdateAction();
        $responsecode = $updateC->clusterUpdateFWProgress($input,$output);
        if($responsecode != CPAPIStatus::ListUpdateNodeProgressSuccess)
            $nodeC->processUpdateErrorCode($responsecode);
        else
            $this->output_json($output);
    }

    function pairActionThreeArgc($api,$defaultValues,$input)
    {
        // var_dump($api);
        if(preg_match('/^[0-9]+\/raid$/',$api[1].'/'.$api[2]))
        {   
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "GET":   
                    // var_dump($api); 
                    $input = array();
                    $input['PairID']=$api[1];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->listPairRAIDForAddVolume($input);
                    exit();
                    break;
            }
        }
        else if(preg_match('/^[0-9]+\/volume$/',$api[1].'/'.$api[2]))
        {   
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "POST":   
                    // var_dump($api); 
                    $input = json_decode($input,true);
                    $input['PairID']=$api[1];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->pairAddVolume($input);
                    exit();
                    break;
            }
        }
        else if(preg_match('/^[0-9]+\/locate$/',$api[1].'/'.$api[2]))
        {   
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "PUT":   
                    // var_dump($api); 
                    $input = array();
                    $input['PairID']=$api[1];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->locatePair($input);
                    exit();
                    break;
            }
        }
        else if(preg_match('/^[0-9]+\/split$/',$api[1].'/'.$api[2]))
        {   
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "POST":   
                    // var_dump($api); 
                    $input = json_decode($input,true);
                    $input['PairID']=$api[1];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->reservePairSplitNode($input);
                    exit();
                    break;
            }
        }
        else if(preg_match('/^[0-9]+\/master$/',$api[1].'/'.$api[2]))
        {   
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "PUT":   
                    // var_dump($api); 
                    $input = json_decode($input,true);
                    $input['PairID']=$api[1];
                    $this->setDefaultValueToInput($defaultValues,$input);
                    $this->setNodeMaster($input);
                    exit();
                    break;
            }
        }
    }

    function locatePair($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->locatePair($input);
        if($responsecode != CPAPIStatus::LocatePairSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
    }

    function setNodeMaster($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->setClusterGroupMaster($input);
        if($responsecode != CPAPIStatus::SetClusterGroupMasterSuccess)
            $glusterC->processGlusterErrorCode($responsecode);   
    }

    function reservePairSplitNode($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->reservePairSplit($input);
        if($responsecode != CPAPIStatus::ReserveNodeSuccess)
            $glusterC->processGlusterErrorCode($responsecode);
    }

    function listPairRAIDForAddVolume($input)
    {
        // var_dump($input);
        $glusterC = new GlusterAction();
        $glusterC->listClusterPairRAIDsForCreate($input,$output);   
        $this->output_json($output);
    }

    function pairAddVolume($input)
    {
        $glusterC = new GlusterAction();
        $responsecode = $glusterC->addVolume($input);
        if($responsecode != CPAPIStatus::AddClusterVolumesSuccess)       
            $glusterC->processGlusterErrorCode($responsecode);
    }

    function rdpActionThreeArgc($api,$defaultValues,$input)
    {
        $glusterC = new GlusterAction();
        $data = array();
        $data['ConnectIP'] = '127.0.0.1';
        $glusterC->checkGlusterForLogin($data,$outputInfo);    
        // var_dump($outputInfo);
        if($outputInfo['HaveGluster'] && $outputInfo['VMSIP'] != $_SERVER['SERVER_ADDR']){
            http_response_code(500);
            exit();
        }
        if(preg_match('/^[a-z,0-9,-]+\/pwd$/',$api[1].'/'.$api[2])){
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "PUT":
                    $input = array();
                    $input['VDID'] = $api[1];
                    $this->vdChangePWD($input);
                    exit();
                    break;
            }
        }
        else if(preg_match('/^[a-z,0-9,-]+\/stage$/',$api[1].'/'.$api[2])){
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "PUT":
                    $input = json_decode($input,true);
                    $input['VDID'] = $api[1];
                    $this->vdChangerRDPStage($input);
                    exit();
                    break;
            }
        }
        else if(preg_match('/^[a-z,0-9,-]+\/online$/',$api[1].'/'.$api[2])){
            switch ($_SERVER['REQUEST_METHOD'])
            {
                case "PUT":
                    $input = json_decode($input,true);
                    $input['VDID'] = $api[1];
                    $this->vdChangerRDPOnline($input);
                    exit();
                    break;
            }
        }
    }

    function vdChangerRDPStage($input)
    {
        $vdC = new VDAction();
        if(!isset($input['VDID']))
            http_response_code(400);
        $vdC->updateRDPStage($input);
    }

    function vdChangerRDPOnline($input)
    {
        $vdC = new VDAction();
        if(!isset($input['VDID']))
            http_response_code(400);
        $vdC->updateRDPOnline($input);
    }

    function vdChangePWD($input)
    {
        $vdC = new VDAction();
        if(!isset($input['VDID']))
            http_response_code(400);
        $responsecode = $vdC->changeVDPWD($input,$output);        
        if($responsecode != CPAPIStatus::InsertRDPSuccess)
            $vdC->processVDErrorCode($responsecode);       
    }
}
// var_dump($_GET['api']);
$api = new SANAPI();