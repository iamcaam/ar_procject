<?php
class DiskAction extends BaseAPI
{
	private $cmdDKDiskLocate = CmdRoot."ip dk_disk_locate ";
	public $diskStateEnum = array ("Empty"=>0,"Unused"=>1,"Used"=>2); 
	public $diskZfsStateEnum = array ("noStatus"=>-11,"error"=>-1,"empty"=>0,"new"=>1,"online"=>2,'recovery'=>3,'recoveryFail'=>9,'Hotspare'=>10,'Hotspare In Use'=>11); 
	private $cmdDKDelDiskPartition = CmdRoot.'127.0.0.1 dk_del_disk_partition ';

	public function listDisk(&$output)
	{		
		// var_dump(CmdJavaRoot.'ScanDisk');
		exec(CmdJavaRoot.'ScanDisk',$outputArr,$rtn);
		// var_dump($outputArr);
		$outputCmd = json_decode($outputArr[0],true);
		// var_dump($outputCmd);		
		$output = array();
		if(isset($outputCmd) && $outputCmd['Result'] == 'Success'){			
			foreach ($outputCmd['Disks'] as $value) {
				$speed = '';
				if(strlen($value['CurrentSpeed']) > 0)
					$speed = 'SATA '.$value['CurrentSpeed'];		
				$output[] = array("Slot"=>$value['Slot'],"Status"=>$this->diskStateEnum[$value['Slot_Status']],"Vendor"=>$value['Vendor'],"DiskName"=>$value['DiskName'],"Capacity"=>$value['Capacity'],"Model"=>$value['Model'],"Firmware"=>$value['Firmware'],"Speed"=>$speed,"DevID"=>$value['WWN']);
			}
		}	
	}

	public function listZfsDisk(&$output)
	{
		// var_dump(CmdRoot.'127.0.0.1 dk_disk_list');
		exec(CmdRoot.'127.0.0.1 dk_disk_list',$outputArr,$rtn);
		// var_dump($outputArr);
		$outputCmd = json_decode($outputArr[0],true);
		// var_dump($outputCmd);		
		$output = array();
		if(rtn == 0){			
			foreach ($outputCmd as $value) {
				$speed = '';
				if(strlen($value['speed']) > 0)
					$speed = $value['diskType'].' '.$value['speed'].' Gb/s';		
				$output[] = array("Slot"=>$value['position'],"Status"=>$this->diskZfsStateEnum[$value['status']],"Vendor"=>$value['vendor'],"DiskName"=>$value['devId'],"Capacity"=>$value['capacity'],"Model"=>$value['model'],"Firmware"=>$value['rev'],"Speed"=>$speed,"DevID"=>$value['devId']);
			}
		}	
	}

	function resetZfsDisk($input)
    {    	    	
        exec(CmdRoot.'127.0.0.1 dk_clear_black_list '.$input['DevID'],$outputArr,$rtn);        
    } 

    function locateDisk($input)
    {
    	$cmd = $this->cmdDKDiskLocate;
    	$cmd = str_replace ( 'ip' , '127.0.0.1', $cmd);
    	$cmd .= $input['Slot'];
    	exec($cmd,$outputArr,$rtn);
    }

    function initialDisk($input)
    {
    	$cmd = $this->cmdDKDelDiskPartition;
    	$cmd = str_replace ( 'ip' , '127.0.0.1', $cmd);
    	foreach ( $input['Disks'] as $value) {
			$cmd .= '"'.$value.'" ';
		}
		// var_dump($cmd);
		exec($cmd,$outputArr,$rtn);	
		if($rtn != 0)
			return CPAPIStatus::InitialDiskFail;
		return CPAPIStatus::InitialDiskSuccess;
    	// var_dump($rtn);
    }
}