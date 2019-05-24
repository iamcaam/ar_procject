<?php
class CephAction extends BaseAPI
{
	// public $diskStateEnum = array ("Empty"=>0,"Unused"=>1,"Used"=>2); 
	private $cmdCreateSNCeph = CmdJavaRoot.'CreateSNCeph ';
	private $cmdSNCephStatus = CmdJavaRoot.'SNCephStatus ';
	private $cmdStorageStatus = CmdJavaRoot.'StorageStatus ';
	private $cmdListStoragePool = CmdJavaRoot.'ListStoragePool ';
	private $cmdGetCephDuplicate = CmdJavaRoot.'getcephduplicate ';
	private $cmdDestroySNCeph = CmdJavaRoot.'DestroySNCeph ';
	private $cmdHostnameList = CmdRoot."ip node_hostname_get ";	

	function getHostName($input)
	{
		$hostname = NULL;
		if(!is_null($input['ConnectIP'])){            
            $cmd = $this->cmdHostnameList;
            $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd);   
            // var_dump($cmd);
            exec($cmd,$outputArr,$rtn);
            if($rtn == 0){                   
                $hostname =$outputArr[0];                
            }            
        }       
        return $hostname;  
	}

	

	function getCephDuplicate()
	{
		$duplicate = NULL;
		$cmd = $this->cmdGetCephDuplicate;	
		exec($cmd,$outputArr,$rtn);		
		$outputCmd = $this->getOutputStr($outputArr);
		$outputCmd = json_decode($outputCmd,true);			
		if($outputCmd['Result'] == 'Success'){
			$duplicate = $outputCmd['Size'];
		}
		return $duplicate;
	}

	function getCephStorageStatus()
	{
		$output = NULL;
		$cmd = $this->cmdStorageStatus;	
		exec($cmd,$outputArr,$rtn);		
		$outputCmd = $this->getOutputStr($outputArr);
		$outputCmd = json_decode($outputCmd,true);				
		$output = array("Status"=>$outputCmd['Storage'],"Progress"=>$outputCmd['Percentage']);
		return $output;
	}

	function getOutputStr($input)
	{
		$output = '';
		foreach ($input as $value) {
			$output .= $value;
		}
		return $output;
	}

	public function createCeph($input)
	{		
		// java -jar Ceph_Mgt.jar CreateSNCeph ceph01 2 vdi /dev/sdx*		
		// $cmd = $this->cmdCreateSNCeph;	
		// $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd); 
		// $hostname = $this->getHostName($input);
		// $cmd .= '"'.$hostname.'" ';
		// $cmd .= $input['Duplicate'].' ';
		// $cmd .= 'ceph ';
		// foreach ( $input['Disks'] as $value) {
		// 	$cmd .= '"'.$value.'" ';
		// }	
		// $cmd .= ' >/dev/null 2>&1 &';			
		// exec($cmd,$output,$rtn);    
		$hostname = $this->getHostName($input);
		$input['Hostname'] = $hostname;
		$input['PoolName'] = 'ceph';
		$input['Method'] = 'Create';
		$rtn=file_put_contents('/mnt/tmpfs/cephcmd/cmd.json', json_encode($input));
		sleep(2);
        return CPAPIStatus::CreateCephSuccess;        		
	}

	public function deleteCeph($input)
	{				
		$cmd = $this->cmdDestroySNCeph;		
		// $cmd = str_replace ( 'ip' , $input['ConnectIP'], $cmd); 	
		// var_dump($cmd);				
		// exec($cmd,$output,$rtn);
		$rtn=file_put_contents('/mnt/tmpfs/cephcmd/cmd.json', json_encode(array('Method' =>'Destroy')));
		sleep(2);              	
	}

	public function listCeph($input,&$output)
	{		
		$cmd = $this->cmdSNCephStatus;			
		exec($cmd,$outputArr,$rtn);			
		$outputCmd = $this->getOutputStr($outputArr);		
		$outputCmd = json_decode($outputCmd,true);				
		if($outputCmd['Status'] == 'Proceeding'){			
			$duplicate = $this->getCephDuplicate();
			if(isset($duplicate)){
				$output = array();						
				$output[] = array("Duplicate"=>$duplicate,"Status"=>"Proceeding(".$outputCmd['Message'].")","Allocate"=>0,"Total"=>0,"Used"=>0);			
			}
			else 
				$output = NULL;		
		}
		else if($outputCmd['Status'] == 'Reboot'){
			$output['Reboot'] = true;
		}
		else{								
			$cmd = $this->cmdListStoragePool;	
			$outputArr = array();
			exec($cmd,$outputArr,$rtn);					
			$outputCmd = $this->getOutputStr($outputArr);								
			$outputCmd = json_decode($outputCmd,true);							
			if($outputCmd['Result'] == 'Success'){
				if(count($outputCmd['Pools']) > 0)
				{
					$duplicate = $this->getCephDuplicate();
					if(isset($duplicate)){						
						$status = $this->getCephStorageStatus();						
						$allocate = $this->getAllocatedSize();		
						$output = array();				
						$output[] = array("Duplicate"=>$duplicate,"Status"=>$status['Status'],"Allocate"=>$allocate,"Total"=>$outputCmd['Pools'][0]['TotalSize']/1024,"Used"=>$outputCmd['Pools'][0]['Used']/1024);	
						// var_dump($output);
					}
					else
						$output = NULL;	
				}
				else
					$output = NULL;	
			}
			else
				$output = NULL;	
		}
	}

	
}