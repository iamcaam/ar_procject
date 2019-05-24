<?php
define("UpdateRootPath", "/var/www/html/check_update/");
define("RootPath","/var/www/html/");
class SystemProcess extends BaseAPI
{
    private $CmdProcess;
    private $cmdReboot = CmdRoot.'ip node_reboot '; 
    private $cmdShutdown = CmdRoot.'ip node_shutdown ';        
    private $cmdSystemUtil =  CmdRoot.'ip node_list_util_local ';
    private $cmdSystemInfo = CmdRoot.'ip node_list_info_local ';   
    private $cmdSMBPasswdSet = CmdRoot.'ip node_samba_passwd_set ';  
    private $cmdSMBList = CmdRoot.'ip node_samba_status ';    
    private $cmdSMBStart = CmdRoot.'ip node_samba_start ';    
    private $cmdSMBStop = CmdRoot.'ip node_samba_stop ';    
    private $cmdAlarmSet = CmdRoot.'ip node_alarm_set ';
    private $cmdAlarmList = CmdRoot.'ip node_alarm_list ';
    private $cmdHostnameGet = CmdRoot.'ip node_hostname_get';
    private $cmdHostnameSet = CmdRoot.'ip node_hostname_add -L ';
    private $cmdDropCache = CmdRoot.'ip node_drop_cache';
    private $cmd_change_hostname = 'sudo /var/www/html/vdi_center set_hostname ';
    private $cmd_get_tz = 'sudo /var/www/html/date.sh get tz ';
    private $cmd_get_time_mode = 'sudo /var/www/html/date.sh get mode ';
    private $cmd_set_tz = 'sudo /var/www/html/date.sh set tz ';
    private $cmd_set_time = 'sudo /var/www/html/date.sh set time ';
    private $cmd_set_ntp = 'sudo /var/www/html/date.sh set ntp ';
    private $cmdSetNTPNow = 'sudo /var/www/html/date.sh set ntpnow ';

    function __construct() {        
    }
    
    function echo_error_result($err)
    {
        $result['status'] = $err;
        echo json_encode($result,JSON_PRETTY_PRINT);
    }
    
    function API_System_Change_HostName($input)
    {        
        $this->Cmd_Send($this->cmd_change_hostname.' '.$input['HostName']);
        echo '{}';        
    }
    
    function API_System_HostName()
    {
        $output = gethostname();
        echo json_encode(array("HostName"=>$output),JSON_PRETTY_PRINT);
    }
    
    function systemInfo($input,&$output)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdSystemInfo;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            exec($cmd,$outputArr,$rtn);
            if($rtn == 0){                   
                $outputSystem = json_decode($outputArr[0],true);
                // var_dump($output);
                $output = array();
                $output['CPU'] = array();
                $output['CPU']['Count'] = $outputSystem['cpuCount'];
                $output['CPU']['Clock'] = $outputSystem['cpuHz'].' '.$outputSystem['cpuHzUnit'];
                $output['CPU']['Model'] = $outputSystem['cpuModel'];
                $output['CPU']['Cores'] = $outputSystem['cpuCores'];
                $output['CPU']['Threads'] = $outputSystem['cpuThreads'];
                $output['Mem'] = array();
                $output['Mem']['Total'] = $outputSystem['memTotal'];
                $output['Mem']['Clocks'] = array();
                foreach ($outputSystem['memClock'] as $value) {
                    if($value['memClock'] != 'NA'){
                        $output['Mem']['Clocks'][] = array('Bank' =>$value['Bank'],'Clock'=>$value[memClock]);
                    }
                }
                $output['NIC'] = array();
                foreach ($outputSystem['NIC'] as $value) {
                    $online = $value['Online'] == 'yes' ? true : false;                
                    $output['NIC'][] = array('Name'=>$value['Name'],'Online'=>$online,'Speed'=>$value['Speed'],'Mode'=>$value['maxmode']);
                }
                return true;      
            }            
        }           
        return false;             
    }        
    
    function getCeph($input)
    {
        $total = 0;
        $used = 0;   
        $usage = 0;     
        // if($this->isCeph){
        //     $cephC = new CephAction();
        //     $cephC->listCeph($input,$output);
        // }
        // else{
            $cephC = new ZfsAction();
            $cephC->listZfs($input,$output);
        // }
        $rtn=array();

        if(isset($output)){
            if(count($output) > 0){
                foreach ($output as $value) {                    
                    if(strlen($value['Level']) > 0){
                        $total = $value['Total'];
                        $used = $value['Used'];
                        $usage = bcdiv($value['Used'], $value['Total'],4)*100;
                        $rtn[] = array('RAIDID'=>$value['RAIDID'],'Total' => $total,'Used'=>$used, 'Usage'=>$usage);
                    }
                }
            }
        }    
        // var_dump($rtn);
        return $rtn;
    }

    function systemUtil($input,&$output)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdSystemUtil;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);   
            exec($cmd,$outputArr,$rtn);
            if($rtn == 0){                   
                $outputUtil = json_decode($outputArr[0],true);
                $output['CPU'] = array();
                $totalCPU = 0;                
                foreach ($outputUtil['cpu'] as $value) {
                    $output['CPU'][] = $value['util'];
                    $totalCPU = bcadd($totalCPU, $value['util'], 2);
                }                
                $totalCPU = bcdiv($totalCPU,count($outputUtil['cpu']),2);    
                array_unshift ($output['CPU'],$totalCPU);
                $output['Mem'] = array();
                $output['Mem']['Total'] = $outputUtil['memTotal'];
                $output['Mem']['Used'] = $outputUtil['memUsed'];
                $output['Mem']['Allocated'] = $outputUtil['memAllocated'];
                $output['NIC'] = array();
                $totalRx = 0;
                $totalTx = 0;
                foreach ($outputUtil['nic'] as $value) {                    
                    $output['NIC'][] = array('Rx'=>$value['rx'],'Tx'=>$value['tx'],'Total'=>bcadd($value['rx'], $value['tx'], 2));
                    $totalRx = bcadd($totalRx, $value['rx'], 2);
                    $totalTx = bcadd($totalTx, $value['tx'], 2);
                }
                $totalRx = bcdiv($totalRx,count($outputUtil['nic']),2);    
                $totalTx = bcdiv($totalTx,count($outputUtil['nic']),2);  
                array_unshift($output['NIC'],array('Rx'=>$totalRx,'Tx'=>$totalTx,'Total'=> bcadd($totalTx, $totalRx, 2)));
                $output['IOWait'] = $outputUtil['iowait'];
                $output['Ceph'] = $this->getCeph($input);                
                // $output['Ceph']['Total'] = $outputCeph['Total'];
                // $output['Ceph']['Used'] = $outputCeph['Used'];
                // $output['Ceph']['Usage'] = $outputCeph['Usage'];
                return true;      
            }            
        }       
        return false;  
    }
    
    function listAlarm($input,&$output)
    {
        $alarm = 1;
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdAlarmList;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);   
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);
            if($rtn == 0){                   
                $alarm = (int)$outputArr[0];     
            }            
        }       
        $output = array('Alarm' => $alarm);
        return true;  
    }

    function setAlarm($input)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdAlarmSet;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);   
            $cmd .= $input['Alarm'];
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);            
        }       
        if($rtn == 0)
            return true;
        else
            return false;  
    }

    function listSMB($input,&$output)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdSMBList;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);               
            exec($cmd,$outputArr,$rtn);
            if($rtn == 0){                   
                $output = array("SMB"=>(int)$outputArr[0]);
                return true;      
            }            
        }       
        return false;  
    }

    function setSMB($input)
    {        
        if(!is_null($input['ConnectIP'])){      
            if($input['SMB'] == 0)
                $cmd = $this->cmdSMBStop;
            else      
                $cmd = $this->cmdSMBStart;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);   
            // var_dump($cmd);         
            exec($cmd,$outputArr,$rtn);            
        }       
        if($rtn == 0)
            return true;
        else
            return false;  
    }

    function setSMBPassword($input)
    {
        if(!is_null($input['ConnectIP'])){      
            $cmd = $this->cmdSMBPasswdSet;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);  
            $cmd .= $input['PWD']; 
            // var_dump($cmd);         
            exec($cmd,$outputArr,$rtn);            
        }       
        if($rtn == 0)
            return true;
        else
            return false;  
    }

    function listHostname($input,&$rtn)
    {
        $rtn = -99;
        $hostname = 'HCI Host';        
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdHostnameGet;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);                           
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);            
            if($rtn == 0){                   
                $hostname = $outputArr[0];                
            }            
        }               
        return array('HostName' => $hostname);    
    }

    function setHostname($input)
    {
        $hostIP = '127.0.0.1';
        $vSwitchC = new vSwitchAction();
        $vSwitchC->listVswitch($input,$outputVswitch);
        if(count($outputVswitch['Vswitchs']) > 0){
            $hostIP = $outputVswitch['Vswitchs'][0]['IP'];
        }
        $hostname = 'HCI Host';        
        if(isset($input['HostName']))
            $hostname = $input['HostName'];
        else
            return;
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdHostnameSet;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);   
            $cmd .= $hostname.' ';    
            // var_dump($cmd);                    
            exec($cmd,$outputArr,$rtn);                          
        }                       
    }

    function dropCashe($input)
    {       
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdDropCache;            
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);                           
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);                      
        }                       
    }

    function reboot($input)
    {                   
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdReboot;            
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);                           
            exec($cmd,$outputArr,$rtn);                      
        }        
    }  
    
    function shutdown($input)
    {                   
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdShutdown;            
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);                           
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);                      
        }
    }  

    function ListVersion()
    {                
        $cmd = CmdRoot.'127.0.0.1 fw_list_version';
        // $exu_str = "sudo ".UpdateRootPath.'list_version.sh';   
        // var_dump($cmd);
        // $output = exec($exu_str,$outputarr); 
        exec($cmd,$outputArr,$rtn);            
        if(isset($outputArr[0]))
            $json_arr = json_decode($outputArr[0]);
        if($json_arr == null)
        {
            $json_arr['Result'] = '-99';
        }
        return $json_arr;
    }
    
    function ListNewVersion()
    {
        $result = array();
        $result['Result'] = 0;
        $result['NewVersion'] = '';
        $path_update_file = '/var/www/html/check_update/upload/update.bin';
        if(file_exists($path_update_file))
        {            
            $version_content = file_get_contents($path_update_file, NULL, NULL, 0, 1024);  
            $versionarr = explode(':', $version_content);
            if(count($versionarr) > 1)
            {
                $version_content = $versionarr[1];
                for($i=0;$i<1024;$i++)
                {
                    $chr = substr($version_content, $i, 1);
                    if($chr != "\0")
                    {
                        $result['NewVersion'] .= $chr;
                    }
                    else
                        break;
                }           
            }
            else
                $result['Result'] = -99;
        }
        else
            $result['Result'] = -99;
        header('Content-Type: application/json; charset=utf-8');
        echo json_encode($result,JSON_PRETTY_PRINT);
    }
    
    function CheckNewVersion()
    {
        $cmd = CmdRoot.'127.0.0.1 fw_check_version_by_wget local';
        // $exu_str = "sudo ".UpdateRootPath."check_button_action.sh";    
        // var_dump($cmd);        
        exec($cmd,$outputArr,$rtn);            
        if(isset($outputArr[0]))      
            $json_arr = json_decode($outputArr[0]);        
        if($json_arr == null)
        {
            $json_arr['Result'] = '-99';
        }
        return $json_arr;
    }
    
    function Update()
    {        
        $cmd = CmdRoot.'127.0.0.1 fw_update_by_wget local';
        // $exu_str = "sudo ".UpdateRootPath."update_button_action.sh".' >/dev/null 2>&1 &';   
//        var_dump($exu_str);
        $pipe = popen($cmd,"r");
        pclose($pipe);
        $json_arr['Result'] = '0';                
        return $json_arr;
    }

    function uploadUpdate()
    {        
        $cmd = CmdRoot.'127.0.0.1 fw_update_by_upload local';
        // $exu_str = "sudo ".UpdateRootPath."update_button_action.sh".' >/dev/null 2>&1 &';   
//        var_dump($exu_str);
        $pipe = popen($cmd,"r");
        pclose($pipe);
        $json_arr['Result'] = '0';                
        return $json_arr;
    }
    
    function CheckUpdate()
    {
        $cmd = CmdRoot.'127.0.0.1 fw_update_progress';
        // var_dump($cmd);
        // $exu_str = "sudo ".UpdateRootPath."update_progress.sh";
        exec($cmd,$outputArr,$rtn);       
        if(isset($outputArr[0])){            
            $json_arr = json_decode($outputArr[0],true);
        }        
        if($json_arr == null)
        {
            $json_arr['Result'] = '-99';
        }
        return $json_arr;
    }

    function getUploadSoftwareVersion()
    {
        $result = array();
        $result['status'] = 0;
        $result['NewVersion'] = '';
        $path_update_file = '/var/www/html/updateUpload/update.bin';
        if(file_exists($path_update_file))
        {            
            $version_content = file_get_contents($path_update_file, NULL, NULL, 0, 1024);  
            $versionarr = explode(':', $version_content);            
            if(count($versionarr) > 1)
            {                
                $version_content = $versionarr[1];
                $develop = file_get_contents('/var/www/html/fw_develop.txt');
                if($develop == 0){
                    $versionarr = explode('-build', $version_content);                
                    $version_content = $versionarr[0];
                }
                for($i=0;$i<1024;$i++)
                {
                    $chr = substr($version_content, $i, 1);
                    if($chr != "\0")
                    {
                        $result['NewVersion'] .= $chr;
                    }
                    else
                        break;
                }           
            }
            else
                $result['status'] = -1;
        }
        else
            $result['status'] = -1;
        header('Content-Type: application/json; charset=utf-8');
        echo json_encode($result,JSON_PRETTY_PRINT);
    }
    
    function GetTime()
    {        
        $timezone = exec($this->cmd_get_tz,$outputarr);           
        $output = exec($this->cmd_get_time_mode,$outputarr);
        $outputarr = explode(',', $output);
        $mode = $outputarr[0];
        $ntp = $outputarr[1];        
        date_default_timezone_set($timezone);        
        $TimeData = array("Time" => date("Y/m/d H:i:s"),"TimeZone"=>$timezone
                ,"Mode"=>(int)$mode,"NTP"=>$ntp);
        return $TimeData;
    }
    
    function SetTime($InputJson)
    {                
        set_time_limit(60);
        $Result = false;
        // $InputJson = json_decode($JsonData,true);                
        if(strlen($InputJson['Mode']) > 0 && strlen($InputJson['TimeZone']) > 0)
        {
            switch ($InputJson['Mode'])
            {
                case 0:
                    if(strlen($InputJson['Time']) > 0)
                    {
                        $exu_str = $this->cmd_set_tz.$InputJson['TimeZone'];
                        $output = exec($exu_str,$outputarr);                       
                        $exu_str = $this->cmd_set_time.$InputJson['Time'];
                        $output = exec($exu_str,$outputarr);     
                        if($output == 0)
                            $Result = true;
                    }
                    break;
                case 1:
                    if(strlen($InputJson['NTP']) > 0)
                    {
                        $exu_str = $this->cmd_set_tz.$InputJson['TimeZone'];
                        $output = exec($exu_str,$outputarr);                       
                        $exu_str = $this->cmd_set_ntp.$InputJson['NTP'];
                        $output = exec($exu_str,$outputarr);     
                        if($output == 0)
                            $Result = true;
                    }
                    break;
            }
        }
        return $Result;
    }
    
    function updateTime($inputJson)
    {
        $result = false;            
        if(strlen($inputJson['NTP']) > 0)
        {
            $exuStr = $this->cmdSetNTPNow.$inputJson['NTP'];            
            $output = exec($exuStr,$outputArr);     
            if($output == 0)
                $result = true;
        }
        return $result;
    }        
}
?>
