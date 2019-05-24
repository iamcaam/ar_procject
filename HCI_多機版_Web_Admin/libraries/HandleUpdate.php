<?php
class UpdateAction extends BaseAPI
{
    private $cmdClusterFWUpdateByUpload = CmdRoot.'ip cluster_fw_update_by_upload ';
    private $cmdClusterFWUpdateProgress = CmdRoot.'ip cluster_fw_update_progress ';
    private $cmdThinUncompress = CmdRoot.'127.0.0.1 thin_uncompress ';

	function UploadFile($input,$update_path)
    {                     
        if(isset($_COOKIE['Ext'])){
            $ext = $this->data_crypt('ar3527678',$_COOKIE['Ext'],'decrypt');
        }
        $UpdatePath = $update_path;
        $FileFullPath = $UpdatePath.$input['FileName'].'.'.$ext;
        // $CompletePath = $UpdatePath.$input['FileName'];
        $CompletePath = $UpdatePath.'update.bin';
        // var_dump($input);
        $SN = $input['SN'];
        $END = $input['END'];
        //$filecontents = binary_to_hex2_str($input['Data']);        
        $filecontents = base64_decode($input['Data']);                  
        $size = $input['Size'];
        if($SN == 1)
        {      
            // if(file_exists($FileFullPath))
            // {
            //     unlink($FileFullPath);
            // }   
            $cmd = 'rm -rf  '.$UpdatePath."*";   
            shell_exec($cmd);       
        }         
        if($fh2 = fopen($FileFullPath, 'ab+'))
        { 
            if($SN > 1 && !file_exists($FileFullPath))
            {
                $this->uploadresult = 1;
                return;
            }
            $image = $filecontents;
            if($image != "")
            {
                if(fwrite($fh2, $filecontents))	  
                {
                    fflush($fh2);
                    fclose($fh2);
                    if($SN == $END)
                    {
                        $file_bytes = filesize($FileFullPath);              
                        if($size != $file_bytes)
                        {                           
                            unlink($FileFullPath);
                            $this->uploadresult = 2;
                            return;
                        }                        
                                                                                                                                       
                        $srcarg = str_replace("$", "\\$", $FileFullPath);
                        $desarg = str_replace("$", "\\$", $CompletePath); 
                        $srcarg = '"'.str_replace("`", "\\`", $srcarg).'"';
                        $desarg = '"'.str_replace("`", "\\`", $desarg).'"';
                        $cmd = 'mv '.$srcarg." ".$desarg;   
                        shell_exec($cmd);
                        if(!file_exists($CompletePath))
                        {                                                             
                            unlink($FileFullPath);	                                    
                            $this->uploadresult = 2;
                            return;
                        }
                        $this->uploadresult = 0;                                   
                    }
                    else
                        $this->uploadresult = 0;                        
                }
                else
                {                                
                    fclose($fh2);
                    unlink($FileFullPath);	                   
                    $this->uploadresult = 2;  
                }
            }
            else
            {
                $this->uploadresult = 3;
            }
        }
        else
            $this->uploadresult = 99;    
    }   

    function UploadThinClientFile($input,$update_path)
    {                     
        if(isset($_COOKIE['Ext'])){
            $ext = $this->data_crypt('ar3527678',$_COOKIE['Ext'],'decrypt');
        }
        $UpdatePath = $update_path;
        $FileFullPath = $UpdatePath.$input['FileName'].'.'.$ext;
        // $CompletePath = $UpdatePath.$input['FileName'];
        $CompletePath = $UpdatePath.'update.bin';
        // $destPath = $UpdatePath.'temp/update.bin';
        // var_dump($input);
        $SN = $input['SN'];
        $END = $input['END'];
        //$filecontents = binary_to_hex2_str($input['Data']);        
        $filecontents = base64_decode($input['Data']);                  
        $size = $input['Size'];
        if($SN == 1)
        {      
            // if(file_exists($FileFullPath))
            // {
            //     unlink($FileFullPath);
            // }               
            $cmd = 'rm -rf '.$UpdatePath."*";               
            shell_exec($cmd);       
        }         
        if($fh2 = fopen($FileFullPath, 'ab+'))
        { 
            if($SN > 1 && !file_exists($FileFullPath))
            {
                $this->uploadresult = 1;
                return;
            }
            $image = $filecontents;
            if($image != "")
            {
                if(fwrite($fh2, $filecontents))   
                {
                    fflush($fh2);
                    fclose($fh2);
                    if($SN == $END)
                    {                       
                        $file_bytes = filesize($FileFullPath);              
                        if($size != $file_bytes)
                        {                           
                            unlink($FileFullPath);
                            $this->uploadresult = 2;
                            return;
                        }                        
                                                                                                                                       
                        $srcarg = str_replace("$", "\\$", $FileFullPath);
                        $desarg = str_replace("$", "\\$", $CompletePath); 
                        $srcarg = '"'.str_replace("`", "\\`", $srcarg).'"';
                        $desarg = '"'.str_replace("`", "\\`", $desarg).'"';
                        $cmd = 'mv '.$srcarg." ".$desarg;   
                        shell_exec($cmd);
                        if(!file_exists($CompletePath))
                        {                                                             
                            unlink($FileFullPath);                                      
                            $this->uploadresult = 2;
                            return;
                        }                               
                        exec($this->cmdThinUncompress,$outputArr,$rtn);
                        $fp = fopen('/mnt/tmpfs/cephcmd/cmd.txt', 'a');                        
                        if($rtn == 0)
                            $this->uploadresult = 0;
                        else
                            $this->uploadresult = 4;
                    }
                    else{                        
                        $this->uploadresult = 0;                        
                    }
                }
                else
                {                                
                    fclose($fh2);
                    unlink($FileFullPath);                     
                    $this->uploadresult = 2;  
                }
            }
            else
            {
                $this->uploadresult = 3;
            }
        }
        else
            $this->uploadresult = 99;    
    }    

    function listThinClientVersion($input,&$output)
    {
        $pathClusterFirmware='/artcluster/ThinClient/firmware/Version.json';
        $pathLocalFirmware='/var/www/html/Version.json';
        if(file_exists($pathClusterFirmware))
            $path=$pathClusterFirmware;
        else
            $path=$pathLocalFirmware;
        if(file_exists($path)){
            $content=file_get_contents($path);            
            $content=json_decode($content,true);
            $output=array("Version"=>$content['Version']);
            return CPAPIStatus::ListViewerVersionSuccess;
        }
        else
            return CPAPIStatus::ListViewerVersionFail;
    }

    function getUpdateFileVersion($input,&$output)
    {
        $output = array();        
        $output['NewVersion'] = '';
        $pathUpdateFile = '/artgluster/upload/update.bin';
        if(file_exists($pathUpdateFile))
        {            
            $versionContent = file_get_contents($pathUpdateFile, NULL, NULL, 0, 1024);  
            $versionArr = explode(':', $versionContent);
            if(count($versionArr) > 1)
            {
                $versionContent = $versionArr[1];
                for($i=0;$i<1024;$i++)
                {
                    $chr = substr($versionContent, $i, 1);
                    if($chr != "\0")
                    {
                        $output['NewVersion'] .= $chr;
                    }
                    else
                        break;
                }           
            }
            else{
                $output['NewVersion'] = '';                
            }
        }
        else{
            $output['NewVersion'] = '';            
        }    
    }

    function updateFW($input)
    {
        // $this->updateFWShell($input);
         if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }   
        $nodeC = new NodeAction();
        if(!$nodeC->sqlListServerByidVDServer($input['NodeID'], $node)){            
            return CPAPIStatus::UpdateNodeFail;
        }        
        $input['CommunicateIP'] = $node['CommunicateIP'];
        $this->updateFWShell($input);        
        return CPAPIStatus::UpdateNodeSuccess;
    }

    function updateFWShell($input)
    {
        $cmd = $this->cmdClusterFWUpdateByUpload;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);   
        $cmd .= $input['CommunicateIP'].' raid>/dev/null 2>&1 &';        
        $pipe = popen($cmd,"r");    
        pclose($pipe); 
        $rtn = 0;
        return $rtn;
    }

    function clusterUpdateFWProgress($input,&$output)
    {
        if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        }   
        $nodeC = new NodeAction();
        if(!$nodeC->sqlListServerByidVDServer($input['NodeID'], $node)){            
            return CPAPIStatus::ListUpdateNodeProgressFail;
        }        
        $input['CommunicateIP'] = $node['CommunicateIP'];
        if($this->clusterUpdateFWProgressShell($input,$output)!=0)
            return CPAPIStatus::ListUpdateNodeProgressFail;
        return CPAPIStatus::ListUpdateNodeProgressSuccess;
    }

    function clusterUpdateFWProgressShell($input,&$outputProgress)
    {
        $cmd = $this->cmdClusterFWUpdateProgress;         
        $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);   
        $cmd .= $input['CommunicateIP'];     
        // var_dump($cmd);   
        exec($cmd,$output,$rtn);       
        // var_dump($output);
        if($rtn == 0){
            $outputProgress = json_decode($output[0],true);
        }
        return $rtn;
    }

    function processUpdateErrorCode($code){  
        $this->baseOutputResponse($code);
        switch ($code){           
            case CPAPIStatus::UpdateNodeFail: 
            case CPAPIStatus::ListUpdateNodeProgressFail:  
            case CPAPIStatus::ListViewerVersionFail:             
                http_response_code(400);                
                break; 
        }
    }
}