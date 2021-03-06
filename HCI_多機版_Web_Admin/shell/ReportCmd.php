<?php
include_once("/var/www/html/libraries/BaseAPI.php");
include_once("/var/www/html/libraries/ConnectDB.php");
include_once("/var/www/html/libraries/ErrorEnum.php");
include_once("/var/www/html/libraries/HandleLogin.php");
include_once("/var/www/html/libraries/HandleTask.php");
include_once("/var/www/html/libraries/HandleUser.php");
include_once("/var/www/html/libraries/HandleVD.php");
include_once("/var/www/html/libraries/HandleDomain.php");
include_once("/var/www/html/libraries/HandleResource.php");
include_once("/var/www/html/libraries/HandleSystem.php");
include_once("/var/www/html/libraries/HandleLog.php");
include_once("/var/www/html/libraries/HandleVswitch.php");
include_once("/var/www/html/libraries/HandleSchedule.php");
include_once("/var/www/html/libraries/HandleJob.php");
include_once("/var/www/html/libraries/HandleSnapshot.php");
include_once("/var/www/html/libraries/HandleCephPool.php");
include_once("/var/www/html/libraries/HandleIP.php");
include_once("/var/www/html/libraries/HandleBackup.php");
include_once("/var/www/html/libraries/HandleGluster.php");
include_once("/var/www/html/libraries/HandleNode.php");
include_once("/var/www/html/libraries/ISnapshotShell.php");
include_once("/var/www/html/libraries/IBackupShell.php");
class ReportCmd extends BaseAPI
{	
	function __construct($argv)
    {    	
    	$gmt = $this->getTZ($timezone);
        date_default_timezone_set($timezone);        
    	$this->processArgv1($argv);
    }

    function processArgv1($argv)
    {    
    	$methodName = '';
    	// var_dump($argv);
		switch ($argv[1]) {			
			case 'report_vd_task_org_create':	
				$methodName = 'reportVDTaskOrgCreate';
				$this->cmdProcess($argv,7,$methodName);
				break;	
			case 'report_vd_task_gateway_create':	
				$methodName = 'reportVDTaskGatewayCreate';
				$this->cmdProcess($argv,7,$methodName);
				break;	
			case 'report_vd_task_seed_create':
				$methodName = 'reportVDTaskSeedCreate';
				$this->cmdProcess($argv,7,$methodName);
				break;	
			case 'report_vd_task_seed_overwrite':
				$methodName = 'reportVDTaskSeedOverwrite';
				$this->cmdProcess($argv,7,$methodName);
				break;	
			case 'report_vd_task_seed_reborn':
				$methodName = 'reportVDTaskSeedReborn';
				$this->cmdProcess($argv,5,$methodName);
				break;
			case 'report_vd_task_seed_recovery':
				$methodName = 'reportVDTaskSeedRecovery';
				$this->cmdProcess($argv,5,$methodName);
				break;
			case 'report_vd_task_user_create':
				$methodName = 'reportVDTaskUserCreate';
				$this->cmdProcess($argv,7,$methodName);
				break;		
			case 'report_vd_task_org_clone':		
				$methodName = 'reportVDTaskOrgClone';
				$this->cmdProcess($argv,7,$methodName);
				break;	
			case 'report_vd_task_user_clone':
				$methodName = 'reportUserToOriginalVD';
				$this->cmdProcess($argv,7,$methodName);
				break;	
			case 'report_vd_task_disk_add':
				$methodName = 'reportVDTaskDiskAdd';
				$this->cmdProcess($argv,6,$methodName);
				break;		
			case 'report_vd_task_disk_del':
				// var_dump($argv);
				$methodName = 'reportVDTaskDiskDel';
				$this->cmdProcess($argv,6,$methodName);
				break;	
			case 'report_vdss_task_take':
				$methodName = 'reportVDSSTaskTake';
				$this->cmdSSProcess($argv,9,$methodName);
				break;
			case 'report_vdss_task_up_take':
				$methodName = 'reportVDSSTaskUPTake';
				$this->cmdSSProcess($argv,9,$methodName);
				break;
			case 'report_vdss_task_del':
				$methodName = 'reportVDSSTaskDel';
				$this->cmdSSProcess($argv,5,$methodName);
				break;
			case 'report_vdss_task_up_del':
				$methodName = 'reportVDSSTaskUPDel';
				$this->cmdSSProcess($argv,5,$methodName);
				break;
			case 'report_vdss_task_rollback':
				$methodName = 'reportVDSSTaskRollback';
				$this->cmdSSProcess($argv,5,$methodName);
				break;
			case 'report_vdss_task_up_rollback':
				$methodName = 'reportVDSSTaskUPRollback';
				$this->cmdSSProcess($argv,5,$methodName);
				break;
			case 'report_vdss_task_view':
				$methodName = 'reportVDSSTaskView';
				$this->cmdSSProcess($argv,5,$methodName);
				break;
			case 'report_vdss_task_unview':
				$methodName = 'reportVDSSTaskUnview';
				$this->cmdSSProcess($argv,5,$methodName);
				break;
			case 'report_vd_task_export':
				$methodName = 'reportVDTaskExport';
				$this->cmdProcess($argv,5,$methodName);
				break;	
			case 'report_vd_task_import':
				$methodName = 'reportVDTaskImport';
				$this->cmdProcess($argv,5,$methodName);
				break;		
			case 'report_bk_task_vd_backup':
				$methodName = 'reportBkTaskVDBackup';
				$this->cmdBackupProcess($argv,9,$methodName);
				break;
			case 'report_bk_task_seed_backup':
				$methodName = 'reportBkTaskSeedBackup';
				$this->cmdBackupProcess($argv,9,$methodName);
				break;	
			case 'report_bk_task_up_backup':
				$methodName = 'reportBkTaskUPBackup';
				$this->cmdBackupProcess($argv,9,$methodName);
				break;	
			case 'report_bk_task_up_restore_same':
				$methodName = 'reportBkTaskUPRestoreSame';
				$this->cmdProcess($argv,6,$methodName);
				break;
			case 'report_bk_task_up_restore_new':
				$methodName = 'reportBkTaskUPRestoreNew';
				$this->cmdProcess($argv,7,$methodName);
				break;
			case 'report_bk_task_seed_restore_new':
				$methodName = 'reportBkTaskSeedRestoreNew';
				$this->cmdProcess($argv,8,$methodName);
				break;
			case 'report_bk_task_seed_restore_same':
				$methodName = 'reportBkTaskSeedRestoreSame';
				$this->cmdProcess($argv,7,$methodName);
				break;	
			case 'report_bk_task_vd_restore_new':
				$methodName = 'reportBkTaskVDRestoreNew';
				$this->cmdProcess($argv,7,$methodName);
				break;
			case 'report_bk_task_vd_restore_same':
				$methodName = 'reportBkTaskVDRestoreSame';
				$this->cmdProcess($argv,6,$methodName);
				break;	
			case 'report_vd_task_disk_move':
				$methodName = 'reportVDTaskDiskMove';
				$this->cmdProcess($argv,6,$methodName);
				break;	
			case 'report_vd_task_userprofile_move':
				$methodName = 'reportVDTaskUserprofileMove';
				$this->cmdProcess($argv,5,$methodName);
				break;	
			case 'report_vd_task_seed_clone_back':
				$methodName = 'reportSeedToOriginalVD';
				$this->cmdProcess($argv,7,$methodName);
				break;	
			case 'report_vd_task_gluster_group_add':
				$methodName = 'reportVDTaskGlusterGroupAdd';
				$this->cmdGlsuterProcess($argv,5,$methodName);
				break;
			case 'report_vd_task_gluster_volume_add':
				$methodName = 'reportVDTaskGlusterVolumeAdd';
				$this->cmdGlsuterProcess($argv,5,$methodName);
				break;
			case 'report_cluster_restore':
				$methodName = 'reportClusterRestore';
				$this->cmdGlsuterProcess($argv,3,$methodName);
				break;
			case 'report_cluster_restore_all':				
				$methodName = 'reportClusterRestoreAllNew';
				$this->cmdGlsuterProcess($argv,2,$methodName);
				break;
			case 'test':
				$methodName = 'test';
				$this->cmdProcess($argv,3,$methodName);
				break;
			case 'log':
				$this->reportLog($argv);
				break;						
			default:
				$this->printHelp();
				break;
		}
	}

	function printHelp()
	{
		echo 'report_vd_task_org_create reportMetaData errorCode addingTime allDisk(base64) allNIC(base64)'.PHP_EOL;
		echo 'report_vd_task_seed_create  reportMetaData errorCode  addingTime  allDisk(base64) allNIC(base64)'.PHP_EOL;
		echo 'report_vd_task_user_create  reportMetaData errorCode  addingTime allDisk(base64) allNIC(base64)'.PHP_EOL;
		echo 'report_vd_task_org_clone reportMetaData errorCode addingTime  allDisk(base64) allNIC(base64)'.PHP_EOL;
		echo 'report_vd_task_disk_add reportMetaData errorCode addingTime allDisk(base64)
report_vd_task_disk_del reportMetaData errorCode addingTime allDisk(base64)'.PHP_EOL;
		echo 'report_vd_task_disk_del reportMetaData errorCode addingTime allDisk(base64)'.PHP_EOL;		
		echo 'report_vd_task_disk_move reportMetaData errorCode addingTime allDisk(base64)'.PHP_EOL;		
		echo 'report_vdss_task_take reportMetaData errorCode addingTime current parent root size'.PHP_EOL;
		echo 'report_vdss_task_del reportMetaData errorCode addingTime'.PHP_EOL;
		echo 'report_vdss_task_view reportMetaData errorCode addingTime'.PHP_EOL;
		echo 'report_vdss_task_unview reportMetaData errorCode addingTime'.PHP_EOL;
		echo 'report_vd_task_export reportMetaData errorCode addingTime'.PHP_EOL;
		echo 'report_vd_task_import reportMetaData errorCode addingTime'.PHP_EOL;
		echo 'report_bk_task_vd_backup reportMetaData errorCode addingTime'.PHP_EOL;
		echo 'report_bk_task_vd_restore_new reportMetaData errorCode addingTime volumeID lasteMeta'.PHP_EOL;
		echo 'report_bk_task_vd_restore_same reportMetaData errorCode addingTime volumeID'.PHP_EOL;
		echo 'report_cluster_restore reportMetaData'.PHP_EOL;
		echo 'report_bk_task_seed_restore_same "$reportMeta" "$errorCode" "$endTime" "$preBackupTime" volumeID'.PHP_EOL;
		echo 'report_bk_task_seed_restore_new "$reportMeta" "$errorCode" "$endTime" "$preBackupTime" volumeID "$lastBkReportMeta"'.PHP_EOL;
		echo 'report_bk_task_up_restore_same "$reportMeta" "$errorCode" "$endTime" volumeID'.PHP_EOL;
		echo 'report_bk_task_up_restore_new "$reportMeta" "$errorCode" "$endTime" volumeID "$lastBkReportMeta"'.PHP_EOL;
		echo 'log logType logLevel logCode logMessage logTime'.PHP_EOL;
		exit(100);
	}
	
	function cmdBackupProcess($argv,$argc,$methodName)
	{
		if(sizeof($argv) != $argc){
			exit(99);
		}
		$argv = array_slice($argv, 2); 
		$backupC = new BackupAction();		
		$backupC->{$methodName}($argv);
	}

	function cmdGlsuterProcess($argv,$argc,$methodName)
	{
		if(sizeof($argv) != $argc){
			exit(99);
		}
		$argv = array_slice($argv, 2); 
		$glusterC = new GlusterAction();		
		$glusterC->{$methodName}($argv);	
	}

	function cmdProcess($argv,$argc,$methodName)
	{				
		if(sizeof($argv) != $argc){
			exit(99);
		}		
		$argv = array_slice($argv, 2); 
		$vdC = new VDAction();		
		$vdC->{$methodName}($argv);
	}	

	function cmdOverArgcProcess($argv,$argc,$methodName)
	{				
		if(sizeof($argv) < $argc){
			exit(99);
		}		
		$argv = array_slice($argv, 2); 
		$vdC = new VDAction();		
		$vdC->{$methodName}($argv);
	}

	function cmdSSProcess($argv,$argc,$methodName)
	{
		if(sizeof($argv) != $argc){
			exit(99);
		}
		$argv = array_slice($argv, 2); 
		$vdC = new SnapshotAction();		
		$vdC->{$methodName}($argv);
	}

	function reportLog($argv)
	{
		$argv = array_slice($argv, 2); 
		if(!$this->connectDB()){
            exit(99);
        }       
        $_SERVER['SERVER_ADDR'] = 'HCI Host';
        $gmt = $this->getTZ($timezone);
        date_default_timezone_set($timezone);        
        $time = date('Y-m-d H:i:s',$argv[4]);
        LogAction::createLog(array('Type'=>$argv[0],'Level'=>$argv[1],'Code'=>$argv[2],'Riser'=>'admin','Message'=>$argv[3],'Time'=>$time),$lastID);
	}
}
$reportC = new ReportCmd($argv);