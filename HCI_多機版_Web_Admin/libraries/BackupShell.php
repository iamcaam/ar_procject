<?php
class BackupShell implements IBackupShell
{
	private $cmdBkTaskSeedBackup = CmdRoot."ip bk_task_seed_backup ";
	private $cmdBkTaskVDRestoreSame = CmdRoot."ip bk_task_vd_restore_same ";
	private $cmdBkTaskSeedRestoreSame = CmdRoot."ip bk_task_seed_restore_same ";
	private $cmdBkTaskVDRestoreNew = CmdRoot."ip bk_task_vd_restore_new ";
	private $cmdBkTaskSeedRestoreNew = CmdRoot."ip bk_task_seed_restore_new ";

	private $cmdBKTaskUPBackup = "sudo /var/www/html/script/cp_util.sh bk_task_up_backup ";
	private $cmdBKTaskUPRestoreSame = CmdRoot."ip bk_task_up_restore_same ";
	private $cmdBKTaskUPRestoreNew = CmdRoot."ip bk_task_up_restore_new ";

	public function backupSeedTaskShell($input)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBkTaskSeedBackup;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.base64_encode('seedBackup$*'.$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.base64_encode($input['Desc'])).'" ';                        
            $cmd .= '"'.$input['VDID'].'" ';                     
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);
            // var_dump($rtn);            
            if($rtn == 0){                                    
                return true;      
            }            
        }                     
        return false;       
    }

    public function restoreSameShell($input,$isSeed)
    {        
        if(!is_null($input['ConnectIP'])){      
            if(!$isSeed){    
                $taskType='restoreSame';
                $cmd = $this->cmdBkTaskVDRestoreSame;   
            }
            else{
                $taskType='restoreSameSeed';
                $cmd = $this->cmdBkTaskSeedRestoreSame;
            }
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.base64_encode("$taskType$*".$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['BackupVDID'].'$*'.$input['BackupVDName']).'" ';                    
            $cmd .= '"'.$input['BkUUID'].'" ';    
            $cmd .= '"'.$input['VDID'].'" '; 
            $cmd .= '"'.$input['VDName'].'" '; 
            if(isset($input['NewVolumeID']) && strlen($input['NewVolumeID']) > 0){
                $cmd .= '"'.$input['VolumeName'].'" '; 
                $cmd .= '"'.$input['VolumeName'].'" '; 
                $cmd .= '"'.$input['VolumeName'].'" '; 
                $cmd .= '"'.$input['VolumeName'].'" '; 
                $cmd .= '"'.$input['VolumeName'].'" '; 
                $cmd .= '"'.$input['VolumeName'].'" '; 
                $cmd .= '"'.$input['VolumeName'].'" '; 
            }
            else{                
                $diskArr = ["hda","hdc","hdd","hde","hdf","hdg","hdh"];                
                foreach ($diskArr as $value) {
                    // var_dump($value);
                    $mapping = false;
                    foreach ($input['DiskMapping'] as $value1) {
                        // var_dump($value);
                        // var_dump($value1);
                        if($value1['diskName'] == $value){
                            $cmd .= '"'.$value1['diskVolumeId'].'" '; 
                            $mapping = true;
                            break;
                        }
                    }
                    if(!$mapping){
                        $cmd .= '"" '; 
                    }
                }
            }
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);
            // var_dump($rtn);
            if($rtn == 0){                                    
                return true;      
            }            
        }                           
        return false;
    }

    public function restoreNewShell($input,$isSeed)
    {
        if(!is_null($input['ConnectIP'])){       
            if(!$isSeed){
                $cmd = $this->cmdBkTaskVDRestoreNew;
                $taskType='restoreNew';
            }
            else{
                $taskType='restoreNewSeed';
                $cmd = $this->cmdBkTaskSeedRestoreNew;
            }
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.base64_encode("$taskType$*".$input['CephID'].'$*'.$input['VDID'].'$*'.$input['VDName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['BackupVDID'].'$*'.$input['BackupVDName'].'$*'.$input['VolumeName']).'" ';
            $cmd .= '"'.$input['BkUUID'].'" ';
            $cmd .= '"'.$input['VDID'].'" ';
            $cmd .= '"'.$input['VDName'].'" ';
            $cmd .= '"'.$input['VDID'].'" ';
            $cmd .= '"'.$input['VolumeName'].'" ';
            $cmd .= '"'.$input['VolumeName'].'" ';
            $cmd .= '"'.$input['VolumeName'].'" '; 
            $cmd .= '"'.$input['VolumeName'].'" '; 
            $cmd .= '"'.$input['VolumeName'].'" '; 
            $cmd .= '"'.$input['VolumeName'].'" '; 
            $cmd .= '"'.$input['VolumeName'].'" ';                
            exec($cmd,$outputArr,$rtn);
            if($rtn == 0){            
                return true;
            }            
        }                     
        return false;
    }    

    public function backupUserDiskTaskShell($input,$type,&$rtn)
    {
        $rtn = -99;
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBKTaskUPBackup;
            // $cmd = str_replace ( 'ip1' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.base64_encode($type.'$*'.$input['CephID'].'$*'.$input['DiskID'].'$*'.$input['DiskName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.base64_encode($input['Desc']).'$*'.$input['Username'].'$*'.$input['DiskSize'].'$*'.$input['DiskType'].'$*'.$input['DiskCache'].'$*'.$input['SSLayerMax']).'" ';
            $cmd .= '"'.$input['DiskName'].'" ';
            if(file_exists('/var/www/html/up.ss')){
                $input['SSLayerMax'] = trim(file_get_contents('/var/www/html/up.ss'));
            }
            $cmd .= '"'.$input['SSLayerMax'].'" ';
            $cmd .= '"1" ';            
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);
            // var_dump($rtn);            
            if($rtn == 0){                                    
                return true;      
            }            
        }                     
        return false;       
    }

    public function restoreSameUPShell($input)
    {
        if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdBKTaskUPRestoreSame;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.base64_encode('restoreSameUP'.'$*'.$input['DiskID'].'$*'.$input['BackupDiskName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['SourceUsername'].'$*'.$input['BackupUsername']).'" ';                    
            $cmd .= '"'.$input['BkUUID'].'" ';    
            $cmd .= '"'.$input['BackupDiskName'].'" '; 
            $cmd .= '"'.$input['VolumeName'].'" '; 
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);
            // var_dump($rtn);
            if($rtn == 0){                                    
                return true;      
            }            
        }                           
        return false;
    }

    public function restoreNewUPShell($input,&$rtn)
    {
    	$rtn = -99;
        if(!is_null($input['ConnectIP'])){                        
            $cmd = $this->cmdBKTaskUPRestoreNew;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);
            $cmd .= '"'.base64_encode('restoreNewUP'.'$*'.$input['DiskID'].'$*'.$input['BackupDiskName'].'$*'.$input['DomainID'].'$*'.$input['DomainName'].'$*'.$input['SourceUsername'].'$*'.$input['BackupUsername']).'" ';                    
            $cmd .= '"'.$input['BkUUID'].'" ';    
            $cmd .= '"'.$input['BackupDiskName'].'" '; 
            $cmd .= '"'.$input['VolumeName'].'" '; 
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);
            // var_dump($rtn);
            if($rtn == 0){                                    
                return true;      
            }
        }                     
        return false;
    }

}