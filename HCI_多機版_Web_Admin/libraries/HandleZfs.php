<?php
class ZfsAction extends BaseAPI
{
	private $cmdListZfs = CmdRoot.'127.0.0.1 dk_raid_list';
	private $cmdListZfsForSAN = CmdRoot.'ip dk_raid_list';
	private $cmdCreateZfs = CmdRoot.'127.0.0.1 dk_ui_raid_create ';
	private $cmdDeleteZfs = CmdRoot.'127.0.0.1 dk_ui_raid_destroy ';
	private $cmdExpansionZfs = CmdRoot.'127.0.0.1 dk_ui_raid_add ';
	private $raidLevelEnum = array ("1"=>"RAID 1","5"=>"RAID 5","6"=>"RAID 6","7"=>"RAID 7");
	private $cmdDKRaidLocate = CmdRoot.'127.0.0.1 dk_raid_locate ';	
	private $cmdDKUnimportableRAIDListDisk = CmdRoot.'127.0.0.1 dk_unimportable_raid_list_disk ';
	private $cmdDKRAIDListDisk = CmdRoot.'127.0.01 dk_raid_list_disk ';
 
	public function createZfs($input)
	{				
		$cmd = $this->cmdCreateZfs;		
		// $cmd .= 'ceph ';
		$cmd .= $input['RAIDID'].' ';
		$cmd .= $input['Level'].' ';
		foreach ( $input['Disks'] as $value) {
			$cmd .= '"'.$value.'" ';
		}			
		exec($cmd,$outputArr,$rtn);		
		sleep(3);
        return CPAPIStatus::CreateCephSuccess;        		
	}

	public function deleteZfs($input)
	{				
		$cmd = $this->cmdDeleteZfs;		
		$cmd .= $input['RAIDID'];
		// $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd); 	
		// var_dump($cmd);				
		// exec($cmd,$output,$rtn);
		sleep(3);
		exec($cmd,$outputArr,$rtn);				
	}

	public function listZfsForSAN($input,&$output)
	{		
		$cmd = $this->cmdListZfsForSAN;		
		$cmd = str_replace ( 'ip' , $input['CommunicateIP'], $cmd);	
		// var_dump($cmd);
		exec($cmd,$outputArr,$rtn);		
		// var_dump($outputArr);
		// var_dump($outputArr);
		$outputCmd = $outputArr[0];		
		$outputCmd = json_decode($outputCmd,true);			
		$output = NULL;		
		if(isset($outputCmd[0]['used'])){
			$output = array();
			// $allocate = $this->getAllocatedSize();		
			// $output = array();							
			foreach ($outputCmd as $value) {
				if(strlen($value['raidLevel']) > 0){
					$raidStatus = strtoupper( substr( $value['state'], 0, 1 ) ).strtolower(substr($value['state'], 1 ));
					switch ($value['raidLevel']) {
						case '1':
							$raidLevel=$this->raidLevelEnum[$value['raidLevel']];
							break;
						case '5':
							$raidLevel=$this->raidLevelEnum[$value['raidLevel']];
							break;
						case '6':
							$raidLevel=$this->raidLevelEnum[$value['raidLevel']];
							break;
						case '7':
							$raidLevel=$this->raidLevelEnum[$value['raidLevel']];
							break;
						case 'NA':
							$raidLevel = 'NA';
							break;
						case '':
							$raidLevel = '';
							break;
						default:
							$raidLevel='Single Disk/JBOD';
							break;
					}
					$output[] = array("RAIDID"=>$value['raidId'],"Level"=>$raidLevel,"Status"=>$raidStatus,"Total"=>$value['capacity']*1024*1024,"Used"=>$value['used']*1024*1024);	
				}
			}
			
		}		
	}

	public function listZfsForCreate($input,&$output)
	{
		$cmd = $this->cmdListZfsForSAN;		
		$cmd = str_replace ( 'ip' , $input['CommunicateIP'], $cmd);	
		// var_dump($cmd);
		exec($cmd,$outputArr,$rtn);		
		// var_dump($outputArr);
		// var_dump($outputArr);
		$outputCmd = $outputArr[0];		
		$outputCmd = json_decode($outputCmd,true);			
		$output = NULL;		
		if(isset($outputCmd[0]['used'])){
			$output = array();
			// $allocate = $this->getAllocatedSize();		
			// $output = array();							
			foreach ($outputCmd as $value) {				
				$raidStatus = strtoupper( substr( $value['state'], 0, 1 ) ).strtolower(substr($value['state'], 1 ));
				switch ($value['raidLevel']) {
					case '1':
						$raidLevel=$this->raidLevelEnum[$value['raidLevel']];
						break;
					case '5':
						$raidLevel=$this->raidLevelEnum[$value['raidLevel']];
						break;
					case '6':
						$raidLevel=$this->raidLevelEnum[$value['raidLevel']];
						break;
					case '7':
						$raidLevel=$this->raidLevelEnum[$value['raidLevel']];
						break;
					case 'NA':
						$raidLevel='NA';
						break;
					case '':
						$raidLevel = '';
						break;
					default:
						$raidLevel='Single Disk/JBOD';
						break;
				}
				$output[] = array("RAIDID"=>$value['raidId'],"Level"=>$raidLevel,"Status"=>$raidStatus,"Total"=>$value['capacity']/1024,"Used"=>$value['used']/1024);				
			}
		}		
	}

	public function listZfs($input,&$output)
	{		
		$cmd = $this->cmdListZfs;	
		// var_dump($cmd);		
		exec($cmd,$outputArr,$rtn);		
		// var_dump($outputArr);
		$outputCmd = $outputArr[0];		
		$outputCmd = json_decode($outputCmd,true);				
		$output = NULL;
		if(isset($outputCmd)){
			$output = array();
			foreach ($outputCmd as $value) {
				$allocate = 0;
				// $allocate = $this->getAllocatedSize();		
				// var_dump($outputCmd);
				switch ($value['raidLevel']) {
					case '1':
						$raidLevel=$this->raidLevelEnum[$value['raidLevel']];
						break;
					case '5':
						$raidLevel=$this->raidLevelEnum[$value['raidLevel']];
						break;
					case '6':
						$raidLevel=$this->raidLevelEnum[$value['raidLevel']];
						break;
					case '7':
						$raidLevel=$this->raidLevelEnum[$value['raidLevel']];
						break;	
					case 'NA':
						$raidLevel='NA';
						break;		
					case '':
						$raidLevel = '';
						break;		
					default:
						$raidLevel='Single Disk/JBOD';
						break;
				}
				$isINGluster = $value['gluster'] == 'yes' ? true:false;				
				$output[] = array("RAIDID"=>$value['raidId'],"Level"=>$raidLevel,"Status"=>$value['state'],"Allocate"=>$allocate,"Total"=>$value['capacity']/1024,"Used"=>$value['used']/1024,"InGluster"=>$isINGluster);
			}

		}
	}

	public function expansionZfs($input)
	{
		$cmd = $this->cmdExpansionZfs;
		$cmd .= $input['RAIDID'].' ';
		foreach ( $input['Disks'] as $value) {
			$cmd .= '"'.$value.'" ';
		}
		// var_dump($cmd);
		exec($cmd,$outputArr,$rtn);
		sleep(3);
        return CPAPIStatus::ExpansionSuccess;
	}

	public function locateZfs($input)
	{
		$cmd = $this->cmdDKRaidLocate;
		$cmd .= $input['RAIDID'].' ';		
		// var_dump($cmd);
		exec($cmd,$outputArr,$rtn);		
		// var_dump($rtn);
	}

	public function listUnimportableRaid($input,&$output)
	{
		$output = array();
		$cmd = $this->cmdDKUnimportableRAIDListDisk;
		$cmd .= $input['RAIDID'];				
		// var_dump($cmd);
		exec($cmd,$outputArr,$rtn);		
		// var_dump($rtn);		
		if($rtn == 0){
			$outputRAIDs = json_decode($outputArr[0],true);
			foreach ($outputRAIDs as $value) {
				$disks = array();
				foreach ($value['disk'] as $value1) {
					$disks[] = (int)$value1;
				}
				$output[] = array('ID' => $value['id'],'Disks'=>$disks);
			}
		}
	}

	public function listNormalRaid($input,&$output)
	{		
		$cmd = $this->cmdDKRAIDListDisk;
		$cmd .= $input['RAIDID'];				
		// var_dump($cmd);
		exec($cmd,$outputArr,$rtn);		
		// var_dump($rtn);		
		$disks = array();
		if($rtn == 0){
			$outputRAID = json_decode($outputArr[0],true);			
			foreach ($outputRAID['subRaid'] as $value) {				
				foreach ($value['disk'] as $value1) {
					$disks[] = (int)$value1['position'];
				}
			}			
		}
		$output = array('Disks'=>$disks);
	}
}
