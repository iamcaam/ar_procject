<?php
class NodePerformanceAction extends BaseAPI
{
	private $nodeHeadPath="/artdb/db/node/";
	private $cpuRealFileName="cpuRealTime.db";	
	private $memRealFileName="memRealTime.db";
	private $nicRealFileName='nicRealTime.db';
	private $diskRealFileName='diskRealTime.db';
	private $raidRealFileName='raidRealTime.db';
	private $cpuHistoryFileName="nodeCPUHistory.db";
	private $memHistoryFileName="nodeMemHistory.db";
	private $nicHistoryFileName="nodeNICHistory.db";
	private $diskHistoryFileName="nodeDiskHistory.db";
	private $raidHistoryFileName="nodeRAIDHistory.db";

	function getModeDBPath(&$input)
	{
		switch ($input['Mode']) {
			case 'cpu':	
				$input['SQLPath']=$this->nodeHeadPath.$input['CommunicateIP'].'/'.$this->cpuHistoryFileName;		
				break;
			case 'mem':
				$input['SQLPath']=$this->nodeHeadPath.$input['CommunicateIP'].'/'.$this->memHistoryFileName;
				break;		
			case 'nic':	
				$input['SQLPath']=$this->nodeHeadPath.$input['CommunicateIP'].'/'.$this->nicHistoryFileName;
				$input['TableBase']='NICBase';
				$input['IDRef']='nic_id';
				break;
			case 'disk':	
				$input['SQLPath']=$this->nodeHeadPath.$input['CommunicateIP'].'/'.$this->diskHistoryFileName;
				$input['TableBase']='DiskBase';
				$input['IDRef']='disk_id';
				break;
			case 'raid':
				$input['SQLPath']=$this->nodeHeadPath.$input['CommunicateIP'].'/'.$this->raidHistoryFileName;
				$input['TableBase']='RAIDBase';
				$input['IDRef']='raid_id';
				break;
		}
	}

	function listNodeCPURealPerformance($input)
	{
		$nodeC = new NodeAction();
		// $timezone = $this->getPHPTZ();
		if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        } 
        $nodeC->sqlListServerByidVDServer($input['NodeID'],$outputNode);
        if(is_null($outputNode))
        	return CPAPIStatus::DBConnectFail;	       
        $this->getStartLastDate($startDate,$lastDate);
        $startTime=$startDate->format("Y-m-d_H:i:s");
        $lastTime=$lastDate->format("Y-m-d_H:i:s"); 
        $sqlPath=$this->nodeHeadPath.$outputNode['CommunicateIP'].'/'.$this->cpuRealFileName;
        // var_dump($sqlPath);
        // var_dump($startTime);
        // var_dump($lastTime);
        $queryPerformance = array();
        if(file_exists($sqlPath))
        {
	        try
	        {
		        $db = new PDO('sqlite:'.$sqlPath);
		        $sqlList =<<<EOF
	      		select * from cpuRealTime where (time>=:start AND time<=:end);
EOF;
				$sth = $db->prepare($sqlList);    
		        $sth->bindValue(':start', $startTime, PDO::PARAM_STR);  
		        $sth->bindValue(':end', $lastTime, PDO::PARAM_STR);
		        if($sth->execute())
		        {                
		        	// var_dump(1);
		            while( $row = $sth->fetch() ) 
		            {           
		            	// var_dump(2);	
		            	if(!isset($queryPerformance[$row['time']]))
		            		$queryPerformance[$row['time']]['CPU']=array();  
		            	$key=(int)$row['id']; 
		            	if(!isset($queryPerformance[$row['time']]['CPU'][$key]))
		            		$queryPerformance[$row['time']]['CPU'][$key] = array();
		            	$queryPerformance[$row['time']]['CPU'][$key][]=array('Type'=>(int)$row['type'],'Value'=>floatval($row['value']));    
		            }            
		        }            
		    }
		    catch (Exception $e){             
	        }    
			// var_dump($queryPerformance);	        
		}
		$output = array();
        for($i=0;$i<180;$i++)
        {			
			$tmpTime=$startDate->format("Y-m-d_H:i:s");
			// $tmpTime = $startDate;
			$tmpCPU=NULL;
			if(array_key_exists($tmpTime, $queryPerformance)){
				foreach ($queryPerformance[$tmpTime]['CPU'] as $key => $value) {					
					$id=$key;
					// var_dump($id);
					$tmpValue=array();					
					foreach ($value as $value1) {											
						$tmpValue[]=array('Type'=>(int)$value1['Type'],'Value'=>floatval($value1['Value']));
					}										
					$tmpCPU[]=array('ID'=>$id,'Values'=>$tmpValue);					
				}					
			}
			if($i==177 || $i==178 || $i==179){				
				if(!is_null($tmpCPU)){			
					$output[] = array('Time'=>$startDate->format("Y-m-d H:i:s"),'Data'=>$tmpCPU);
				}
			}
			else
				$output[] = array('Time'=>$startDate->format("Y-m-d H:i:s"),'Data'=>$tmpCPU);
			$startDate->add(new DateInterval('PT20S'));
			// var_dump($startDate->format("Ymd H:i:s"));
		}
		$this->output_json($output);
	}

	function listNodeCPUMemHistoryPerformance($input)
	{
		// var_dump($input);
		$nodeC = new NodeAction();
		// $timezone = $this->getPHPTZ();
		if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        } 
        $nodeC->sqlListServerByidVDServer($input['NodeID'],$outputNode);
        if(is_null($outputNode))
        	return CPAPIStatus::DBConnectFail;
        $input['CommunicateIP']=$outputNode['CommunicateIP'];
        $this->getModeDBPath($input);
        $this->getPeriod($input);
        $this->listNodeCPUMemHistory($input);
	}

	function listNodeCPUMemHistory($input)
	{		
		// var_dump($input);
		$startDate=$input['StartDate'];
		$lastDate=$input['LastDate'];
		$startTime=$startDate->format("Y-m-d_H:i:s");
        $lastTime=$lastDate->format("Y-m-d_H:i:s"); 
        $sqlPath=$input['SQLPath'];
        $tableName=$input['TableName'];
        $periodCount=$input['PeriodCount'];
        $period=$input['Period'];    
        $queryPerformance = array();
        if(file_exists($sqlPath))
        {
	        try
	        {
		        $db = new PDO('sqlite:'.$sqlPath);
		        $sqlList =<<<EOF
	      		select * from $tableName where (time>=:start AND time<=:end);
EOF;
				$sth = $db->prepare($sqlList);    
		        $sth->bindValue(':start', $startTime, PDO::PARAM_STR);  
		        $sth->bindValue(':end', $lastTime, PDO::PARAM_STR);
		        
		        if($sth->execute())
		        {                
		            while( $row = $sth->fetch() ) 
		            {                 	      
		            	// var_dump($row);      	
		            	if(!isset($queryPerformance[$row['time']]))
		            		$queryPerformance[$row['time']]['Data']=array();  
		            	$key=(int)$row['id']; 
		            	if(!isset($queryPerformance[$row['time']]['Data'][$key]))
		            		$queryPerformance[$row['time']]['Data'][$key] = array();
		            	$queryPerformance[$row['time']]['Data'][$key][]=array('Type'=>(int)$row['type'],'Value'=>floatval($row['value']));    
		            }            
		        }            
		    }
		    catch (Exception $e){             
	        }    
			// var_dump($queryPerformance);	        
		}
		$output = array();
        for($i=0;$i<$periodCount;$i++)
        {			
			$tmpTime=$startDate->format("Y-m-d_H:i:s");
			// $tmpTime = $startDate;
			$tmpData=NULL;
			if(array_key_exists($tmpTime, $queryPerformance)){
				foreach ($queryPerformance[$tmpTime]['Data'] as $key => $value) {					
					$id=$key;
					// var_dump($id);
					$tmpValue=array();					
					foreach ($value as $value1) {											
						$tmpValue[]=array('Type'=>(int)$value1['Type'],'Value'=>floatval($value1['Value']));
					}														
					$tmpData[]=array('ID'=>$id,'Values'=>$tmpValue);					
				}					
			}			
			if($i==($periodCount-3) || $i==($periodCount-2) || $i==($periodCount-1)){				
				if(!is_null($tmpData)){			
					$output[] = array('Time'=>$startDate->format("Y-m-d H:i:s"),'Data'=>$tmpData);
				}
			}
			else
				$output[] = array('Time'=>$startDate->format("Y-m-d H:i:s"),'Data'=>$tmpData);			
			// var_dump($output);
			$startDate->add(new DateInterval($period));
			// var_dump($startDate->format("Ymd H:i:s"));
		}
		// var_dump($output);
		$this->output_json($output);
	}

	function listNodeNICDiskHistoryPerformance($input)
	{
		// var_dump($input);
		$nodeC = new NodeAction();
		// $timezone = $this->getPHPTZ();
		if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        } 
        $nodeC->sqlListServerByidVDServer($input['NodeID'],$outputNode);
        if(is_null($outputNode))
        	return CPAPIStatus::DBConnectFail;
        $input['CommunicateIP']=$outputNode['CommunicateIP'];
        $this->getModeDBPath($input);
        $this->getPeriod($input);
        $this->listNodeNICDiskHistory($input);
	}

	function listNodeNICDiskHistory($input)
	{		     
		$startDate=$input['StartDate'];
		$lastDate=$input['LastDate'];
		$startTime=$startDate->format("Y-m-d_H:i:s");
        $lastTime=$lastDate->format("Y-m-d_H:i:s"); 
        $sqlPath=$input['SQLPath'];
        $tableName=$input['TableName'];
        $tableBase=$input['TableBase'];
        $periodCount=$input['PeriodCount'];
        $period=$input['Period'];
		$idRef=$input['IDRef'];      
		$mode=$input['Mode'];
        $startTime=$startDate->format("Y-m-d_H:i:s");
        $lastTime=$lastDate->format("Y-m-d_H:i:s");         
        $queryPerformance = array();
        $bases=array();        
        if(file_exists($sqlPath))
        {
	        try
	        {
		        $db = new PDO('sqlite:'.$sqlPath);
		        $sqlListNIC =<<<EOF
		      		select * from $tableBase;
EOF;
				$sth = $db->prepare($sqlListNIC);
				if($sth->execute())
		        { 
		        	while( $row = $sth->fetch() ) 
			        {
			        	switch ($mode) {
			        		case 'nic':
			        			$this->sqlListAllVswitchsWithBrNameKey($outputVswitchs,$outputIDWithName);
			        			if(array_key_exists($row['name'], $outputVswitchs)){
					        		$bases[$row['id']]=array('ID'=>$outputVswitchs[$row['name']]['VswitchID'],'Name'=>$outputVswitchs[$row['name']]['Name']);
					        	}
					        	else{
					        		$bases[$row['id']]=NULL;
					        	}			        			
			        			break;
			        		case 'disk':
			        		case 'raid':
			        			$bases[$row['id']]=$row['id'];
			        			break;			        		
			        	}			        	
			        }			        
		        	$sqlList =<<<EOF
		      			select * from $tableName where (time>=:start AND time<=:end);
EOF;
					$sth = $db->prepare($sqlList);    
			        $sth->bindValue(':start', $startTime, PDO::PARAM_STR);  
			        $sth->bindValue(':end', $lastTime, PDO::PARAM_STR);		        
			        if($sth->execute())
			        {                
			            while( $row = $sth->fetch() ) 
			            {                 	            	
			            	if(!isset($queryPerformance[$row['time']]))
			            		$queryPerformance[$row['time']]['Data']=array();  
			            	switch ($mode) {
			            		case 'nic':			            			
			            			$key=$bases[$row['nic_id']]['ID'];			            			
			            			break;
			            		default:			            			
			            			$key=$bases[$row[$idRef]];
			            			break;
			            	}			  					            	
			            	if(isset($key)){          				            	
			            		if(!isset($queryPerformance[$row['time']]['Data'][$key]))
			            			$queryPerformance[$row['time']]['Data'][$key] = array();
			            		$queryPerformance[$row['time']]['Data'][$key][]=array('Type'=>(int)$row['type'],'Value'=>floatval($row['value']));
			            	}
			            }            
			        }            
		    	}
		    }
		    catch (Exception $e){             
	        }    
	    }
		// var_dump($queryPerformance);
        $output = array();
        for($i=0;$i<$periodCount;$i++)
        {			
			$tmpTime=$startDate->format("Y-m-d_H:i:s");						
			$tmpData=NULL;
			if(array_key_exists($tmpTime, $queryPerformance))
			{				
				$tmpData=array();			
				foreach ($queryPerformance[$tmpTime]['Data'] as $key => $value) {
					$id=$key;
					$tmpValue=array();					
					foreach ($value as $value1) {						
						$tmpValue[]=array('Type'=>(int)$value1['Type'],'Value'=>floatval($value1['Value']));
					}					

					if($input['Mode'] == 'disk')
						$tmpData[]=array('ID'=>$id,'Name'=>strval($id),'Values'=>$tmpValue);
					else
						$tmpData[]=array('ID'=>$id,'Values'=>$tmpValue);	
				}					
			}
			if($i==($periodCount-3) || $i==($periodCount-2) || $i==($periodCount-1)){				
				if(!is_null($tmpData)){			
					$output[] = array('Time'=>$startDate->format("Y-m-d H:i:s"),'Data'=>$tmpData);
				}
			}
			else
				$output[] = array('Time'=>$startDate->format("Y-m-d H:i:s"),'Data'=>$tmpData);				
			
			$startDate->add(new DateInterval($period));
			// var_dump($startDate->format("Ymd H:i:s"));
		}
		$this->output_json($output);
	}

	function listNodeMemRealPerformance($input)
	{
		$nodeC = new NodeAction();
		// $timezone = $this->getPHPTZ();
		if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        } 
        $nodeC->sqlListServerByidVDServer($input['NodeID'],$outputNode);
        if(is_null($outputNode))
        	return CPAPIStatus::DBConnectFail;	        
        $this->getStartLastDate($startDate,$lastDate);
        $startTime=$startDate->format("Y-m-d_H:i:s");
        $lastTime=$lastDate->format("Y-m-d_H:i:s"); 
        $sqlPath=$this->nodeHeadPath.$outputNode['CommunicateIP'].'/'.$this->memRealFileName;
        // var_dump($startDate);
        // var_dump($lastTime);
        $queryPerformance = array();
        if(file_exists($sqlPath))
        {
	        try
	        {
		        $db = new PDO('sqlite:'.$sqlPath);
		        $sqlList =<<<EOF
		      		select * from memRealTime where (time>=:start AND time<=:end);
EOF;
				$sth = $db->prepare($sqlList);    
		        $sth->bindValue(':start', $startTime, PDO::PARAM_STR);  
		        $sth->bindValue(':end', $lastTime, PDO::PARAM_STR);
		        
		        if($sth->execute())
		        {                
		            while( $row = $sth->fetch() ) 
		            {                 	            	
		            	if(!isset($queryPerformance[$row['time']]))
		            		$queryPerformance[$row['time']]['Memory']=array();   
		            	$key=(int)$row['id']; 
		            	if(!isset($queryPerformance[$row['time']]['Memory'][$key]))
		            		$queryPerformance[$row['time']]['Memory'][$key] = array();
		            	$queryPerformance[$row['time']]['Memory'][$key][]=array('Type'=>(int)$row['type'],'Value'=>floatval($row['value']));		             
		            }            
		        }            
		    }
		    catch (Exception $e){             
	        }    
	    }
		// var_dump($queryPerformance);
        $output = array();
        for($i=0;$i<180;$i++)
        {			
			$tmpTime=$startDate->format("Y-m-d_H:i:s");
			// $tmpTime = $startDate;
			$tmpMemory=NULL;
			if(array_key_exists($tmpTime, $queryPerformance)){
				foreach ($queryPerformance[$tmpTime]['Memory'] as $key => $value) {					
					$id=$key;
					// var_dump($id);
					$tmpValue=array();					
					foreach ($value as $value1) {											
						$tmpValue[]=array('Type'=>(int)$value1['Type'],'Value'=>floatval($value1['Value']));
					}										
					$tmpMemory[]=array('ID'=>$id,'Values'=>$tmpValue);					
				}		
			}
			if($i==177 || $i==178 || $i==179){				
				if(!is_null($tmpMemory)){
					$output[] = array('Time'=>$startDate->format("Y-m-d H:i:s"),'Data'=>$tmpMemory);
				}
			}
			else
				$output[] = array('Time'=>$startDate->format("Y-m-d H:i:s"),'Data'=>$tmpMemory);			
			$startDate->add(new DateInterval('PT20S'));
			// var_dump($startDate->format("Ymd H:i:s"));
		}
		$this->output_json($output);
	}

	function listNodeNICRealPerformance($input)
	{
		$nodeC = new NodeAction();
		// $timezone = $this->getPHPTZ();
		if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        } 
        $nodeC->sqlListServerByidVDServer($input['NodeID'],$outputNode);
        if(is_null($outputNode))
        	return CPAPIStatus::DBConnectFail;
        $this->sqlListAllVswitchsWithBrNameKey($outputVswitchs,$outputIDWithName);
        if(is_null($outputVswitchs))
        	return CPAPIStatus::DBConnectFail;
        $this->getStartLastDate($startDate,$lastDate);
        $startTime=$startDate->format("Y-m-d_H:i:s");
        $lastTime=$lastDate->format("Y-m-d_H:i:s"); 
        $sqlPath=$this->nodeHeadPath.$outputNode['CommunicateIP'].'/'.$this->nicRealFileName;
        // var_dump($startDate);
        // var_dump($lastTime);
        $queryPerformance = array();
        $nics=array();        
        // var_dump($sqlPath);
        if(file_exists($sqlPath))
        {
	        try
	        {
		        $db = new PDO('sqlite:'.$sqlPath);
		        $sqlListNIC =<<<EOF
		      		select * from NICBase;
EOF;
				$sth = $db->prepare($sqlListNIC);
				if($sth->execute())
		        { 
		        	while( $row = $sth->fetch() ) 
			        {
			        	if(array_key_exists($row['name'], $outputVswitchs)){
			        		$nics[$row['id']]=array('ID'=>$outputVswitchs[$row['name']]['VswitchID'],'Name'=>$outputVswitchs[$row['name']]['Name']);
			        	}
			        	else{
			        		$nics[$row['id']]=NULL;
			        	}
			        	// $index=str_replace('br', '', $row['name']);
			        	// $nics[$row['id']]=$index+1;
			        }   			               
		        	$sqlList =<<<EOF
		      			select * from nicRealTime where (time>=:start AND time<=:end);
EOF;
					$sth = $db->prepare($sqlList);    
			        $sth->bindValue(':start', $startTime, PDO::PARAM_STR);  
			        $sth->bindValue(':end', $lastTime, PDO::PARAM_STR);		        
			        if($sth->execute())
			        {                
			            while( $row = $sth->fetch() ) 
			            {                 	            	
			            	if(!isset($queryPerformance[$row['time']]))
			            		$queryPerformance[$row['time']]['NIC']=array();   
			            	$key=$nics[$row['nic_id']]['ID'];			            	
			            	if(isset($key)){
				            	if(!isset($queryPerformance[$row['time']]['NIC'][$key]))
				            		$queryPerformance[$row['time']]['NIC'][$key] = array();
				            	$queryPerformance[$row['time']]['NIC'][$key][]=array('Type'=>(int)$row['type'],'Value'=>floatval($row['value']));
				            }
			            }            
			        }            
		    	}
		    }
		    catch (Exception $e){             
	        }    
	    }
		// var_dump($queryPerformance);
        $output = array();
        for($i=0;$i<180;$i++)
        {			
			$tmpTime=$startDate->format("Y-m-d_H:i:s");						
			$tmpNIC=NULL;
			if(array_key_exists($tmpTime, $queryPerformance))
			{				
				$tmpNIC=array();			
				foreach ($queryPerformance[$tmpTime]['NIC'] as $key => $value) {
					$id=$key;
					$tmpValue=array();					
					foreach ($value as $value1) {						
						$tmpValue[]=array('Type'=>(int)$value1['Type'],'Value'=>floatval($value1['Value']));
					}					
					$tmpNIC[]=array('ID'=>$id,'Name'=>$outputIDWithName[$id],'Values'=>$tmpValue);
				}
				
				// $tmpNIC=$queryPerformance[$tmpTime]['NIC'];
			}
			if($i==177 || $i==178 || $i==179){				
				if(!is_null($tmpNIC)){
					$output[] = array('Time'=>$startDate->format("Y-m-d H:i:s"),'Data'=>$tmpNIC);
				}
			}
			else
				$output[] = array('Time'=>$startDate->format("Y-m-d H:i:s"),'Data'=>$tmpNIC);			
			$startDate->add(new DateInterval('PT20S'));
			// var_dump($startDate->format("Ymd H:i:s"));
		}
		$this->output_json($output);
	}

	function listNodeDiskRealPerformance($input)
	{
		$nodeC = new NodeAction();
		// $timezone = $this->getPHPTZ();
		if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        } 
        $nodeC->sqlListServerByidVDServer($input['NodeID'],$outputNode);
        if(is_null($outputNode))
        	return CPAPIStatus::DBConnectFail;	        
        $this->getStartLastDate($startDate,$lastDate);
        $startTime=$startDate->format("Y-m-d_H:i:s");
        $lastTime=$lastDate->format("Y-m-d_H:i:s"); 
        $sqlPath=$this->nodeHeadPath.$outputNode['CommunicateIP'].'/'.$this->diskRealFileName;
        // var_dump($startDate);
        // var_dump($lastTime);
        $queryPerformance = array();
        $disks=array();
        // var_dump($sqlPath);
        if(file_exists($sqlPath))
        {
	        try
	        {
		        $db = new PDO('sqlite:'.$sqlPath);
		        $sqlListDisk =<<<EOF
		      		select * from DiskBase;
EOF;
				$sth = $db->prepare($sqlListDisk);
				if($sth->execute())
		        { 
		        	while( $row = $sth->fetch() ) 
			        {			        	
			        	$disks[$row['id']]=$row['id'];
			        }   			               
		        	$sqlList =<<<EOF
		      			select * from diskRealTime where (time>=:start AND time<=:end);
EOF;
					$sth = $db->prepare($sqlList);    
			        $sth->bindValue(':start', $startTime, PDO::PARAM_STR);  
			        $sth->bindValue(':end', $lastTime, PDO::PARAM_STR);		        
			        if($sth->execute())
			        {                
			            while( $row = $sth->fetch() ) 
			            {                 	            	
			            	if(!isset($queryPerformance[$row['time']]))
			            		$queryPerformance[$row['time']]['Disk']=array();   
			            	$key=$disks[$row['disk_id']];
			            	if(!isset($queryPerformance[$row['time']]['Disk'][$key]))
			            		$queryPerformance[$row['time']]['Disk'][$key] = array();
			            	$queryPerformance[$row['time']]['Disk'][$key][]=array('Type'=>(int)$row['type'],'Value'=>floatval($row['value']));    
			            }            
			        }            
		    	}
		    }
		    catch (Exception $e){             
	        }    
	    }
		// var_dump($queryPerformance);
        $output = array();
        for($i=0;$i<180;$i++)
        {			
			$tmpTime=$startDate->format("Y-m-d_H:i:s");						
			$tmpDisk=NULL;
			if(array_key_exists($tmpTime, $queryPerformance))
			{				
				$tmpDisk=array();			
				foreach ($queryPerformance[$tmpTime]['Disk'] as $key => $value) {
					$id=$key;
					$tmpValue=array();					
					foreach ($value as $value1) {						
						$tmpValue[]=array('Type'=>(int)$value1['Type'],'Value'=>floatval($value1['Value']));
					}					
					$tmpDisk[]=array('ID'=>$id,'Name'=>strval($id),'Values'=>$tmpValue);
				}
				
				// $tmpNIC=$queryPerformance[$tmpTime]['NIC'];
			}
			if($i==177 || $i==178 || $i==179){				
				if(!is_null($tmpDisk)){
					$output[] = array('Time'=>$startDate->format("Y-m-d H:i:s"),'Data'=>$tmpDisk);
				}			
			}
			else
				$output[] = array('Time'=>$startDate->format("Y-m-d H:i:s"),'Data'=>$tmpDisk);			
			$startDate->add(new DateInterval('PT20S'));
			// var_dump($startDate->format("Ymd H:i:s"));
		}
		$this->output_json($output);
	}

	function listNodeRAIDRealPerformance($input)
	{
		$nodeC = new NodeAction();
		// $timezone = $this->getPHPTZ();
		if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        } 
        $nodeC->sqlListServerByidVDServer($input['NodeID'],$outputNode);
        if(is_null($outputNode))
        	return CPAPIStatus::DBConnectFail;	        
        $this->getStartLastDate($startDate,$lastDate);
        $startTime=$startDate->format("Y-m-d_H:i:s");
        $lastTime=$lastDate->format("Y-m-d_H:i:s"); 
        $sqlPath=$this->nodeHeadPath.$outputNode['CommunicateIP'].'/'.$this->raidRealFileName;
        // var_dump($startDate);
        // var_dump($lastTime);
        $queryPerformance = array();
        $raids=array();
        // var_dump($sqlPath);
        if(file_exists($sqlPath))
        {
	        try
	        {
		        $db = new PDO('sqlite:'.$sqlPath);
		        $sqlListDisk =<<<EOF
		      		select * from raidBase;
EOF;
				$sth = $db->prepare($sqlListDisk);
				if($sth->execute())
		        { 
		        	while( $row = $sth->fetch() ) 
			        {			        	
			        	$raids[$row['id']]=$row['id'];
			        }   			               
		        	$sqlList =<<<EOF
		      			select * from raidRealTime where (time>=:start AND time<=:end);
EOF;
					$sth = $db->prepare($sqlList);    
			        $sth->bindValue(':start', $startTime, PDO::PARAM_STR);  
			        $sth->bindValue(':end', $lastTime, PDO::PARAM_STR);		        
			        if($sth->execute())
			        {                
			            while( $row = $sth->fetch() ) 
			            {                 	            	
			            	if(!isset($queryPerformance[$row['time']]))
			            		$queryPerformance[$row['time']]['RAID']=array();   
			            	$key=$raids[$row['raid_id']];
			            	if(!isset($queryPerformance[$row['time']]['RAID'][$key]))
			            		$queryPerformance[$row['time']]['RAID'][$key] = array();
			            	$queryPerformance[$row['time']]['RAID'][$key][]=array('Type'=>(int)$row['type'],'Value'=>floatval($row['value']));    
			            }            
			        }            
		    	}
		    }
		    catch (Exception $e){             
	        }    
	    }
		// var_dump($queryPerformance);
        $output = array();
        for($i=0;$i<180;$i++)
        {			
			$tmpTime=$startDate->format("Y-m-d_H:i:s");						
			$tmpRAID=NULL;
			if(array_key_exists($tmpTime, $queryPerformance))
			{				
				$tmpRAID=array();			
				foreach ($queryPerformance[$tmpTime]['RAID'] as $key => $value) {
					$id=$key;
					$tmpValue=array();					
					foreach ($value as $value1) {						
						$tmpValue[]=array('Type'=>(int)$value1['Type'],'Value'=>floatval($value1['Value']));
					}					
					$tmpRAID[]=array('ID'=>$id,'Name'=>strval($id),'Values'=>$tmpValue);
				}
				// $tmpNIC=$queryPerformance[$tmpTime]['NIC'];
			}
			if($i==177 || $i==178 || $i==179){				
				if(!is_null($tmpRAID)){
					$output[] = array('Time'=>$startDate->format("Y-m-d H:i:s"),'Data'=>$tmpRAID);
				}
			}
			else
				$output[] = array('Time'=>$startDate->format("Y-m-d H:i:s"),'Data'=>$tmpRAID);			
			$startDate->add(new DateInterval('PT20S'));
			// var_dump($startDate->format("Ymd H:i:s"));
		}
		$this->output_json($output);
	}
}