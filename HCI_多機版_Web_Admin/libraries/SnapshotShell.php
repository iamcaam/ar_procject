<?php 
class SnapshotShell implements ISnapshotShell
{
	private $cmdVDSSTaskTake = "sudo /var/www/html/script/cp_util.sh vdss_task_take ";
	private $cmdVDSSTaskDelete = CmdRoot."ip vdss_task_del ";
	private $cmdVDSSTaskRollback = CmdRoot."ip vdss_task_rollback ";

	private $cmdVDSSTaskUPTake = "sudo /var/www/html/script/cp_util.sh vdss_task_up_take ";
	private $cmdVDSSTaskUPDelete = CmdRoot."ip vdss_task_up_del ";
	private $cmdVDSSTaskUPRollback = CmdRoot."ip vdss_task_up_rollback ";

    private $cmdVDUserProfileSSView = CmdRoot."ip vd_userprofile_ss_view ";
    private $cmdVDUserProfileSSUnview = CmdRoot."ip vd_userprofile_ss_unview ";  

	public function takeSnapshotShell($input,$type)
	{
		$rtn=-99;   
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDSSTaskTake;
            // $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.base64_encode($type.'$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.base64_encode($input['Desc'])).'" ';                            
            $cmd .= '"'.$input['VDID'].'" ';                              
            $cmd .= $input['SSLayerMax'].' ';
            $cmd .= $input['LayerOverwrite'] == true ? 1 : 0;  
            // var_dump($cmd);           
            exec($cmd,$output,$rtn);   
            // var_dump($rtn);                                   
        }             
        return $rtn;
	}

	function deleteSnapshotShell($input)
    {
        // var_dump($input);
    	$rtn = -99;
    	if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDSSTaskDelete;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.base64_encode('delete$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['LayerID'].'$*'.$input['LayerDate'].'$*'.base64_encode($input['LayerDesc'])).'" ';
            $cmd .= '"'.$input['VDID'].'" '; 
            $cmd .= $input['LogicalLayer'];                   
            // var_dump($cmd);
            exec($cmd,$output,$rtn);
        }     
        return $rtn;
    }

    function rollbackSnapshotShell($input)
    {
        $rtn = -99;
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDSSTaskRollback;
            // var_dump($input);
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd); 
            $cmd .= '"'.base64_encode('rollback$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['LayerID'].'$*'.$input['LayerDate'].'$*'.base64_encode($input['LayerDesc'])).'" ';
            $cmd .= '"'.$input['VDID'].'" ';  
            $cmd .= '"'.$input['LogicalLayer'].'" ';              
            // var_dump($cmd);        
            $rtn = shell_exec($cmd);                                    
        }     
        return $rtn;
    }

    function takeUPSnapshotShell($input,$type)
    {        
        // var_dump($type);
        $rtn=-99;   
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDSSTaskUPTake;
            // $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.base64_encode($type.'$*'.$input['CephID'].'$*'.$input['DiskID'].'$*'.$input['DiskName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.base64_encode($input['Desc']).'$*'.$input['Username']).'" ';      
            $cmd .= '"'.$input['DiskName'].'" ';  
            if(file_exists('/var/www/html/up.ss')){
                $input['SSLayerMax'] = trim(file_get_contents('/var/www/html/up.ss'));
            }                            
            $cmd .= $input['SSLayerMax'].' ';
            $cmd .= $input['LayerOverwrite'] == true ? 1 : 0;                            
            // var_dump($cmd);
            exec($cmd,$output,$rtn);      
            // var_dump($rtn);                                   
        }             
        return $rtn;
    }

    function deleteUPSnapshotShell($input)
    {
        // var_dump($input);
        $rtn = -99;
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDSSTaskUPDelete;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.base64_encode('deleteUP'.'$*'.$input['CephID'].'$*'.$input['DiskID'].'$*'.$input['DiskName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['LayerID'].'$*'.$input['LayerDate'].'$*'.base64_encode($input['LayerDesc']).'$*'.$input['Username']).'" ';
            $cmd .= '"'.$input['DiskName'].'" '; 
            $cmd .= $input['LogicalLayer'].' ';            
            // var_dump($cmd);
            exec($cmd,$output,$rtn);
            // var_dump($rtn);
        }     
        return $rtn;
    }

    function rollbackUPSnapshotShell($input)
    {
        $rtn = -99;
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDSSTaskUPRollback;
            // var_dump($input);
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd); 
            $cmd .= '"'.base64_encode('rollbackUP'.'$*'.$input['CephID'].'$*'.$input['DiskID'].'$*'.$input['DiskName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['LayerID'].'$*'.$input['LayerDate'].'$*'.base64_encode($input['LayerDesc']).'$*'.$input['Username']).'" ';;             
            $cmd .= '"'.$input['DiskName'].'" ';  
            $cmd .= '"'.$input['LogicalLayer'].'" ';                          
            // var_dump($cmd);        
            $rtn = shell_exec($cmd);                                    
        }     
        return $rtn;
    }

    function directViewSnapshotUPShell($input)
    {
        $rtn = -99;
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDUserProfileSSView;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.$input['DiskName'].'" ';
            $cmd .= '"'.$input['LogicalLayer'].'" ';                 
            exec($cmd,$output,$rtn);            
            // var_dump($rtn);
        }     
        return $rtn;
    }

    function directStopViewUPSnapshotShell($input)
    {
        $rtn = -99;
        if(!is_null($input['ConnectIP'])){
            $cmd = $this->cmdVDUserProfileSSUnview;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);            
            $cmd .= '"'.$input['DiskName'].'" ';                        
            // var_dump($cmd);        
            exec($cmd,$output,$rtn);           
            // var_dump($rtn);                            
        }     
        return $rtn;
    }
}