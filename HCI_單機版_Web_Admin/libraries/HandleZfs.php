<?php
class ZfsAction extends BaseAPI
{
	private $cmdListZfs = CmdRoot.'127.0.0.1 dk_raid_list';
	private $cmdCreateZfs = CmdRoot.'127.0.0.1 dk_ui_raid_create ';
	private $cmdDeleteZfs = CmdRoot.'127.0.0.1 dk_ui_raid_destroy ';
	private $cmdExpansionZfs = CmdRoot.'127.0.0.1 dk_ui_raid_add ';
	private $raidLevelEnum = array ("1"=>"RAID 1","5"=>"RAID 5","6"=>"RAID 6",'7'=>'RAID 7'); 
	private $cmdClusterDedup = CmdRoot.'127.0.0.1 cluster_dedup ';
    private $cmdClusterDedupList = CmdRoot.'127.0.0.1 cluster_dedup_list';
    private $cmdDKRaidLocate = CmdRoot.'127.0.0.1 dk_raid_locate ';	
    private $cmdDKRaidDedupSet = CmdRoot.'127.0.0.1 dk_raid_dedup_set ';
    private $cmdDKUnimportableRAIDListDisk = CmdRoot.'127.0.0.1 dk_unimportable_raid_list_disk ';
	private $cmdDKRAIDListDisk = CmdRoot.'127.0.01 dk_raid_list_disk ';

	public function createZfs($input)
	{				
		$cmd = $this->cmdCreateZfs;		
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
		// $this->debug("=======Delete========");
		if($input['RAIDID'] != 1){
			if(!$this->connectDB()){
            	return CPAPIStatus::DBConnectFail;
        	}
        }  
        if($input['RAIDID'] != 1)
        	$this->sqlListVDOSDiskWithRAIDID($input['RAIDID'],$outputVDs);
		$cmd = $this->cmdDeleteZfs;
		$cmd .= $input['RAIDID'];
		// var_dump($cmd);
		// $this->debug($cmd)
		exec($cmd,$outputArr,$rtn);
		// $this->debug($rtn)
		// var_dump($rtn);
		if($rtn == 0){
			if($input['RAIDID'] != 1){
				$vdC = new VDAction();
				foreach ($outputVDs as $value) {
					$input['VDID'] = $value;
					$vdC->deleteVD($input);				
				}
				$this->deleteVDiskWithRAIDID($input['RAIDID']);
			}			
		}		
		// $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd); 	
		// var_dump($cmd);				
		// exec($cmd,$output,$rtn);
		sleep(3);
			
	}

	function deleteVDiskWithRAIDID($idRAID)
	{
		$SQLSelect = <<<SQL
            DELETE FROM tbVDiskSet WHERE idRAID=:idRAID
SQL;
//         AND (t1.stateVD != 30 AND t1.stateVD != 31)
        try
        {                                             
            $sth = connectDB::$dbh->prepare($SQLSelect);                        
            $sth->bindValue(':idRAID', $idRAID, PDO::PARAM_INT);                       
            $sth->execute();
        }
        catch (Exception $e){                             
        }           
	}

	function sqlListVDOSDiskWithRAIDID($idRAID,&$output)
	{
		$output = array();
        $SQLSelect = <<<SQL
                SELECT idVD FROM tbVDiskSet WHERE nameVDisk='hda' AND idRAID=:idRAID
SQL;
//         AND (t1.stateVD != 30 AND t1.stateVD != 31)
        try
        {                                             
            $sth = connectDB::$dbh->prepare($SQLSelect);                        
            $sth->bindValue(':idRAID', $idRAID, PDO::PARAM_INT);                       
            if($sth->execute())
            {                
                $output=array();
                while( $row = $sth->fetch() ) 
                {                    
                    $output[] = $row['idVD'];
                }
            }                           
        }
        catch (Exception $e){                             
        }           
	}
	
	public function listZfs($input,&$output)
	{		
		$cmd = $this->cmdListZfs;			
		exec($cmd,$outputArr,$rtn);		
		// var_dump($outputArr);
		$outputCmd = $outputArr[0];		
		// var_dump($outputCmd);
		$outputCmd = json_decode($outputCmd,true);				
		$output = NULL;
		if(isset($outputCmd)){
			$output = array();
			foreach ($outputCmd as $value) {
				$allocate = $this->getAllocatedSize($value['raidId']);							
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
				if($value['dedup'] == 'on')
					$dedup = true;
				else
					$dedup = false;
				$output[] = array("RAIDID"=>$value['raidId'],"Level"=>$raidLevel,"Status"=>$value['state'],"Allocate"=>$allocate,"Total"=>$value['capacity']/1024,"Used"=>$value['used']/1024,"Dedupe"=>$dedup);
			}			
			// var_dump($output);
			// $this->debug($output);
		}
	}

	public function expansionZfs($input)
	{
		// var_dump($input);
		$cmd = $this->cmdExpansionZfs;
		$cmd .= $input['RAIDID'].' ';
		foreach ( $input['Disks'] as $value) {
			$cmd .= '"'.$value.'" ';
		}
		// var_dump($cmd);
		// $this->debug($cmd);
		exec($cmd,$outputArr,$rtn);
		sleep(3);
        return CPAPIStatus::ExpansionSuccess;
	}

	function listClusterDedup($input,&$output)
    {        
        if($this->listClusterDedupShell($input,$outputStatus)){
            $output = array();
            // var_dump($outputStatus);
            $output['Dedupe'] = $outputStatus == 'on' ? true : false;
            return CPAPIStatus::ListDedupSuccess;
        }            
        else
            return CPAPIStatus::ListDedupFail;
    }

    function listClusterDedupShell($input,&$output)
    {
        $rtn = -99;
        $cmd = $this->cmdClusterDedupList;
        // $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);          
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($outputArr);      
       if($rtn == 0){               
            $output = $outputArr[0];
            return true;      
        }            
        return false;
    }

    function setZfsDedup($input,&$output)
    {
        $rtn = $this->setZfsDedupShell($input);
        $logMsg = $input['RAIDID'].':';
        $logMsg .= $input['Dedupe'] == true ? 'on' : 'off';
        if($rtn){
        	if($this->connectDB()){            
        		if(LogAction::createLog(array('Type'=>14,'Level'=>1,'Code'=>'0E100501','Riser'=>'admin','Message'=>"Set Dedupe(RAID ID $logMsg)Success."),$lastID)){
                	LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
            	} 
        	}  
        	 
        }
        else{
        	if($this->connectDB()){     
        		if(LogAction::createLog(array('Type'=>14,'Level'=>3,'Code'=>'0E300502','Riser'=>'admin','Message'=>"Failed to set Dedupe(RAID ID $logMsg)"),$lastID)){
                	LogAction::sqlInsertDomainLogRelation($lastID,$domainID);
            	}
            }  
        }
        return CPAPIStatus::SetDedupSuccess;
    }

    function setZfsDedupShell($input)
    {
        $cmd = $this->cmdDKRaidDedupSet;
        // $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);  
        $cmd .= $input['RAIDID'].' ';   
        $cmd .= $input['Dedupe'] == true ? 'on' : 'off';
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($rtn);
        // var_dump($outputArr);
        if($rtn == 0){               
            return true;      
        }            
        return false;
    }

    function setClusterDedup($input,&$output)
    {
        $this->setClusterDedupShell($input);
        if($this->listClusterDedup($input,$output) != CPAPIStatus::ListDedupSuccess){            
            return CPAPIStatus::SetDedupFail;
        }        
        return CPAPIStatus::SetDedupSuccess;
    }

    function setClusterDedupShell($input)
    {
        $cmd = $this->cmdClusterDedup;
        // $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);     
        $cmd .= $input['Dedupe'] == true ? 'on' : 'off';
        // var_dump($cmd);
        exec($cmd,$outputArr,$rtn);
        // var_dump($outputArr);
        if($rtn == 0){               
            return true;      
        }            
        return false;
    }

    public function locateZfs($input)
	{
		$cmd = $this->cmdDKRaidLocate;
		$cmd .= $input['RAIDID'];		
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