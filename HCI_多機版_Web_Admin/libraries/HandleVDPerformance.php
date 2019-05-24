<?php
class VDPerformanceAction extends BaseAPI
{
	private $vdHeadPath="/artdb/db/vd/";
	private $cpuRealFileName="cpuRealTime.db";	
	private $memRealFileName="memRealTime.db";
	private $nicRealFileName="nicRealTime.db";
	private $diskRealFileName="diskRealTime.db";
	private $cpuHistoryFileName="vdCPUHistory.db";
	private $memHistoryFileName="vdMemHistory.db";
	private $nicHistoryFileName="vdNICHistory.db";
	private $diskHistoryFileName="vdDiskHistory.db";

	function getModeDBPath(&$input)
	{
		switch ($input['Mode']) {
			case 'cpu':	
				$input['SQLPath']=$this->vdHeadPath.$input['VDID'].'/'.$this->cpuHistoryFileName;		
				break;
			case 'mem':
				$input['SQLPath']=$this->vdHeadPath.$input['VDID'].'/'.$this->memHistoryFileName;
				break;		
			case 'nic':	
				$input['SQLPath']=$this->vdHeadPath.$input['VDID'].'/'.$this->nicHistoryFileName;
				$input['TableBase']='NICBase';
				$input['IDRef']='nic_id';
				break;
			case 'disk':	
				$input['SQLPath']=$this->vdHeadPath.$input['VDID'].'/'.$this->diskHistoryFileName;
				$input['TableBase']='DiskBase';
				$input['IDRef']='disk_id';
				break;
		}
	}

	function listVDCPURealPerformance($input)
	{		
		$vdC = new VDAction();
		// $timezone = $this->getPHPTZ();
		if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        } 
        $vdC->sqlListVDByID($input['VDID'],$outputVD);
        if(is_null($outputVD))
        	return CPAPIStatus::DBConnectFail;	
        $this->getStartLastDate($startDate,$lastDate);
        $startTime=$startDate->format("Y-m-d_H:i:s");
        $lastTime=$lastDate->format("Y-m-d_H:i:s"); 
        $sqlPath=$this->vdHeadPath.$input['VDID'].'/'.$this->cpuRealFileName;
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
		        // var_dump($sqlList);
		        // var_dump($startTime);
		        // var_dump($lastTime);
		        if($sth->execute())
		        {                
		            while( $row = $sth->fetch() ) 
		            {                 	            	
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
	    }
		// var_dump($queryPerformance);
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
			else{				
				$output[] = array('Time'=>$startDate->format("Y-m-d H:i:s"),'Data'=>$tmpCPU);
			}			
			$startDate->add(new DateInterval('PT20S'));
			// var_dump($startDate->format("Ymd H:i:s"));
		}
		$this->output_json($output);
	}

	function listVDMemRealPerformance($input)
	{
		$vdC = new VDAction();
		// $timezone = $this->getPHPTZ();
		if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        } 
        $vdC->sqlListVDByID($input['VDID'],$outputVD);
        if(is_null($outputVD))
        	return CPAPIStatus::DBConnectFail;	
        $this->getStartLastDate($startDate,$lastDate);
        $startTime=$startDate->format("Y-m-d_H:i:s");
        $lastTime=$lastDate->format("Y-m-d_H:i:s"); 
        $sqlPath=$this->vdHeadPath.$input['VDID'].'/'.$this->memRealFileName;
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
		        // var_dump($sqlList);
		        // var_dump($startTime);
		        // var_dump($lastTime);
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
						if($value1['Type'] != 4 && $id != 2)	
							$tmpValue[]=array('Type'=>(int)$value1['Type'],'Value'=>floatval($value1['Value']));
						else if($id == 2)
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

	function listVDCPUMemHistoryPerformance($input)
	{
		// var_dump($input);
		$vdC = new VDAction();
		// $timezone = $this->getPHPTZ();
		if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        } 
        $vdC->sqlListVDByID($input['VDID'],$outputVD);
        if(is_null($outputVD))
        	return CPAPIStatus::DBConnectFail;
        $this->getModeDBPath($input);
        $this->getPeriod($input);
        $this->listVDCPUMemHistory($input);
	}

	function listVDCPUMemHistory($input)
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
		$startTime=$startDate->format("Y-m-d_H:i:s");
        $lastTime=$lastDate->format("Y-m-d_H:i:s");         
        // var_dump($startDate);
        // var_dump($lastDate);                    
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
						if($input['Mode'] == 'cpu'){							
							$tmpValue[]=array('Type'=>(int)$value1['Type'],'Value'=>floatval($value1['Value']));
						}
						else						{
							if($value1['Type'] != 4 && $id != 2)	
								$tmpValue[]=array('Type'=>(int)$value1['Type'],'Value'=>floatval($value1['Value']));
							else if($id==2)
								$tmpValue[]=array('Type'=>(int)$value1['Type'],'Value'=>floatval($value1['Value']));	
						}
					}														
					$tmpData[]=array('ID'=>$id,'Values'=>$tmpValue);					
				}					
			}			
			$output[] = array('Time'=>$startDate->format("Y-m-d H:i:s"),'Data'=>$tmpData);
			// var_dump($output);
			$startDate->add(new DateInterval($period));
			// var_dump($startDate->format("Ymd H:i:s"));
		}
		// var_dump($output);
		$this->output_json($output);
	}

	function listVDNICRealPerformance($input)
	{
		$vdC = new VDAction();
		// $timezone = $this->getPHPTZ();
		if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        } 
        $vdC->sqlListVDByID($input['VDID'],$outputVD);
        if(is_null($outputVD))
        	return CPAPIStatus::DBConnectFail;	
        $this->getStartLastDate($startDate,$lastDate);
        $startTime=$startDate->format("Y-m-d_H:i:s");
        $lastTime=$lastDate->format("Y-m-d_H:i:s"); 
        $sqlPath=$this->vdHeadPath.$input['VDID'].'/'.$this->nicRealFileName;
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
			        	$index=$row['id']+1;
			        	$nics[$row['id']]=$index;
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
			            	$key=$nics[$row['nic_id']];
			            	if(!isset($queryPerformance[$row['time']]['NIC'][$key]))
			            		$queryPerformance[$row['time']]['NIC'][$key] = array();
			            	$queryPerformance[$row['time']]['NIC'][$key][]=array('Type'=>(int)$row['type'],'Value'=>floatval($row['value']));    
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
					// var_dump($id);
					$tmpValue=array();					
					foreach ($value as $value1) {						
						$tmpValue[]=array('Type'=>(int)$value1['Type'],'Value'=>floatval($value1['Value']));
					}										
					$tmpNIC[]=array('ID'=>$id,'Values'=>$tmpValue);					
				}												
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

	function listVDDiskRealPerformance($input)
	{
		$vdC = new VDAction();
		// $timezone = $this->getPHPTZ();
		if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        } 
        $vdC->sqlListVDByID($input['VDID'],$outputVD);
        if(is_null($outputVD))
        	return CPAPIStatus::DBConnectFail;	
        $this->getStartLastDate($startDate,$lastDate);
        $startTime=$startDate->format("Y-m-d_H:i:s");
        $lastTime=$lastDate->format("Y-m-d_H:i:s"); 
        $sqlPath=$this->vdHeadPath.$input['VDID'].'/'.$this->diskRealFileName;
        // var_dump($startDate);
        // var_dump($lastTime);
        $queryPerformance = array();
        $disks=array();        
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
			        	$index=$row['id'];
			        	$disks[$row['id']]=$row['name'];
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
			            	$key=$row['disk_id'];
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
				$tmpNIC=array();			
				foreach ($queryPerformance[$tmpTime]['Disk'] as $key => $value) {					
					$id=$key;
					// var_dump($id);
					$tmpValue=array();					
					foreach ($value as $value1) {						
						$tmpValue[]=array('Type'=>(int)$value1['Type'],'Value'=>floatval($value1['Value']));
					}										
					$tmpDisk[]=array('ID'=>$id,'Name'=>$disks[$id],'Values'=>$tmpValue);					
				}												
			}
			if($i==177 || $i==178 || $i==179){				
				if(!is_null($tmpDisk)){	
					$output[] = array('Time'=>$startDate->format("Y-m-d H:i:s"),'Data'=>$tmpDisk);
				}
			}
			else{
				$output[] = array('Time'=>$startDate->format("Y-m-d H:i:s"),'Data'=>$tmpDisk);
			}
			$startDate->add(new DateInterval('PT20S'));
			// var_dump($startDate->format("Ymd H:i:s"));
		}
		$this->output_json($output);
	}

	function listVDNICDiskHistoryPerformance($input)
	{
		// var_dump($input);
		$vdC = new VDAction();
		// $timezone = $this->getPHPTZ();
		if(!$this->connectDB()){
            return CPAPIStatus::DBConnectFail;
        } 
        $vdC->sqlListVDByID($input['VDID'],$outputVD);
        if(is_null($outputVD))
        	return CPAPIStatus::DBConnectFail;        
        $this->getModeDBPath($input);
        $this->getPeriod($input);
        switch ($input['Mode']) {
        	case 'disk':
        		$this->listVDDiskHistory($input);
        		break;
        	case 'nic':
        		$this->listVDNICHistory($input);
        		break;        	
        }        
	}

	function listVDDiskHistory($input)
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
        $disks=array();        
        if(file_exists($sqlPath))
        {
	        try
	        {
		        $db = new PDO('sqlite:'.$sqlPath);
		        $sqlListDisk =<<<EOF
		      		select * from $tableBase;
EOF;
				$sth = $db->prepare($sqlListDisk);
				if($sth->execute())
		        { 
		        	while( $row = $sth->fetch() ) 
			        {
			        	$index=$row['id'];
			        	$disks[$row['id']]=$row['name'];
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
			            		$queryPerformance[$row['time']]['Disk']=array();   
			            	$key=$row[$idRef];
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
        for($i=0;$i<$periodCount;$i++)
        {			
			$tmpTime=$startDate->format("Y-m-d_H:i:s");						
			$tmpDisk=NULL;
			if(array_key_exists($tmpTime, $queryPerformance))
			{				
				$tmpNIC=array();			
				foreach ($queryPerformance[$tmpTime]['Disk'] as $key => $value) {					
					$id=$key;
					// var_dump($id);
					$tmpValue=array();					
					foreach ($value as $value1) {						
						$tmpValue[]=array('Type'=>(int)$value1['Type'],'Value'=>floatval($value1['Value']));
					}										
					$tmpDisk[]=array('ID'=>$id,'Name'=>$disks[$id],'Values'=>$tmpValue);					
				}												
			}
			$output[] = array('Time'=>$startDate->format("Y-m-d H:i:s"),'Data'=>$tmpDisk);
			$startDate->add(new DateInterval($period));			
		}
		$this->output_json($output);
	}

	function listVDNICHistory($input)
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
        $nics=array();
        // var_dump($sqlPath);
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
			        	$index=$row['id']+1;
			        	$nics[$row['id']]=$index;
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
			            		$queryPerformance[$row['time']]['NIC']=array();   
			            	$key=$nics[$row[$idRef]];
			            	if(!isset($queryPerformance[$row['time']]['NIC'][$key]))
			            		$queryPerformance[$row['time']]['NIC'][$key] = array();
			            	$queryPerformance[$row['time']]['NIC'][$key][]=array('Type'=>(int)$row['type'],'Value'=>floatval($row['value']));    
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
			$tmpNIC=NULL;
			if(array_key_exists($tmpTime, $queryPerformance))
			{				
				$tmpNIC=array();			
				foreach ($queryPerformance[$tmpTime]['NIC'] as $key => $value) {					
					$id=$key;
					// var_dump($id);
					$tmpValue=array();					
					foreach ($value as $value1) {						
						$tmpValue[]=array('Type'=>(int)$value1['Type'],'Value'=>floatval($value1['Value']));
					}										
					$tmpNIC[]=array('ID'=>$id,'Values'=>$tmpValue);					
				}												
			}
			$output[] = array('Time'=>$startDate->format("Y-m-d H:i:s"),'Data'=>$tmpNIC);
			$startDate->add(new DateInterval($period));
			// var_dump($startDate->format("Ymd H:i:s"));
		}
		$this->output_json($output);
	}
}