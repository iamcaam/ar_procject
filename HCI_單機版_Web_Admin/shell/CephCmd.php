<?php
include_once("/var/www/html/libraries/ErrorEnum.php");
include_once("/var/www/html/libraries/BaseAPI.php");
include_once("/var/www/html/libraries/HandleIP.php");
class CephCmd extends BaseAPI
{
	private $sshCmd = 'timeout 30 ssh -o ConnectTimeout=10 -o ServerAliveInterval=10 -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no root@';
	private $sshCmdCustomTimeout = 'timeout @1 ssh -o ConnectTimeout=10 -o ServerAliveInterval=10 -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no root@';
	private $sshCephCmd = 'ssh -o ConnectTimeout=10 -o ServerAliveInterval=10 -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no root@';
	function __construct($argv)
    {
    	// var_dump($argv);
    	$this->processArgv1($argv);
    }

    function processArgv1($argv)
    {    
		switch ($argv[2]) {
			case 'vd_list_all':
				$this->cmdProcess($argv,3,true);
				break;	
			case 'vd_list_diskSize_all':
				$this->cmdProcess($argv,3,true);
				break;
			case 'vd_list':
				$this->cmdProcess($argv,4,true);
				break;
			case 'vd_list_with_ss':
				$this->cmdProcess($argv,4,true);
				break;
			case 'vd_task_list':
				$this->cmdProcess($argv,3,true);
				break;	
			case 'vd_task_clear':
				$this->cmdProcess($argv,3,true);
				break;
			case 'vd_task_wait_del':
				$this->cmdProcess($argv,4);
				break;
			case 'vdss_task_list':
				$this->cmdProcess($argv,3,true);
				break;
			case 'vdss_task_clear':
				$this->cmdProcess($argv,3,true);
				break;
			case 'vd_task_org_create':				
				$this->cmdProcess($argv,23);
				break;		
			case 'vd_task_disk_add':
				$this->cmdProcess($argv,10);
				break;		
			case 'vd_task_disk_del':
				$this->cmdProcess($argv,7);
				break;		
			case 'vd_task_vd_del':
				$this->cmdProcess($argv,5);
				break;
			case 'vd_task_org_clone':
				$this->cmdProcess($argv,9);
				break;
			case 'vd_task_user_clone':
				$this->cmdProcess($argv,9);
				break;
			case 'vd_task_seed_create':
				$this->cmdProcess($argv,8);
				break;
			case 'vd_task_seed_overwrite':
				$this->cmdProcess($argv,8);
				break;
			case 'vd_task_seed_reborn':
				$this->cmdProcess($argv,6);
				break;
			case 'vd_task_seed_recovery':
				$this->cmdProcess($argv,7);
				break;
			case 'vd_task_user_create':
				$this->cmdProcess($argv,9);
				break;
			case 'vd_task_export':
				$this->cmdProcess($argv,6);
				break;
			case 'vd_import_list':
				$this->cmdProcess($argv,3,true);
				break;
			case 'vd_task_import':			
				$this->cmdProcess($argv,16);
				break;
			case 'vd_task_disk_move':
				$this->cmdProcess($argv,8);
				break;
			case 'vd_task_userprofile_move':
				$this->cmdProcess($argv,7);
				break;
			case 'vd_nic_add':
				$this->cmdProcess($argv,6,true);
				break;
			case 'vd_nic_del':
				$this->cmdProcess($argv,5,true);
				break;
			case 'vd_iso_list_all':
				$this->cmdProcess($argv,3,true);
				break;
			case 'vd_modify':
				$this->cmdProcess($argv,14);
				break;
			case 'vd_disk_modify':
				$this->cmdProcess($argv,8);
				break;
			case 'vd_poweron':
				$this->cmdProcess($argv,4);
				break;
			case 'vd_poweron_viewer':
				$this->cmdProcess($argv,4);
				break;
			case 'vd_poweron_info':
				$this->cmdProcess($argv,4,true);
				break;
			case 'vd_shutdown':
				$this->cmdProcess($argv,4);
				break;
			case 'vd_poweroff':
				$this->cmdProcess($argv,4);
				break;
			case 'vd_poweron_before':
				$this->cmdProcess($argv,4);
				break;
			case 'vd_del':
				$this->cmdProcess($argv,4);
				break;
			case 'vd_iso_attach':
				$this->cmdProcess($argv,5);
				break;
			case 'vd_iso_detach':
				$this->cmdProcess($argv,4);
				break;
			case 'vd_disable_set':
				$this->cmdProcess($argv,5);
				break;
			case 'vd_suspend_set':
				$this->cmdProcess($argv,5);
				break;
			case 'vd_passwd_set':
				$this->cmdProcess($argv,5);
				break;
			case 'vd_connect_passwd_set':
				$this->cmdProcess($argv,5);
				break;
			case 'vd_userprofile_create':
				$this->cmdProcess($argv,6);
				break;
			case 'vd_userprofile_resize':
				$this->cmdProcess($argv,5);
				break;
			case 'vd_userprofile_del':
				$this->cmdProcess($argv,4);
				break;
			case 'vd_userprofile_attach':
				$this->cmdProcess($argv,7);
				break;
			case 'vd_userprofile_detach':
				$this->cmdProcess($argv,4);
				break;
			case 'vdss_get_view_layer':
				$this->cmdProcess($argv,4,true);
				break;
			case 'vdss_task_take':
				$this->cmdProcess($argv,7);
				break;
			case 'vdss_task_del':
				$this->cmdProcess($argv,6);
				break;
			case 'vdss_task_view':
				$this->cmdProcess($argv,6);
				break;
			case 'vdss_task_unview':
				$this->cmdProcess($argv,5);
				break;
			case 'vd_ss_view':
				$this->cmdProcess($argv,7);
				break;			
			case 'vd_ss_unview':
				$this->cmdProcess($argv,4);
				break;
			case 'vdss_task_rollback':
				$this->cmdProcess($argv,6);
				break;			
			case 'node_list_info_local':
				$this->cmdProcess($argv,3,true);
				break;					
			case 'node_list_util_local':
				$this->cmdProcess($argv,3,true);
				break;		
			case 'node_alarm_set':
				$this->cmdProcess($argv,4,true);
				break;
			case 'node_alarm_list':
				$this->cmdProcess($argv,3,true);
				break;		
			case 'node_samba_passwd_set':
				$this->cmdProcess($argv,4,true);
				break;		
			case 'node_samba_status':
				$this->cmdProcess($argv,3,true);
				break;	
			case 'node_samba_start':
				$this->cmdProcess($argv,3,true);
				break;	
			case 'node_samba_stop':
				$this->cmdProcess($argv,3,true);
				break;
			case 'node_hostname_get':
				$this->cmdProcess($argv,3,true);
				break;
			case 'node_drop_cache':
				$this->cmdProcess($argv,3);
				break;
			case 'node_firewall_enable_ssh':
				$this->cmdProcess($argv,3);
				break;
			case 'node_firewall_disable_ssh':
				$this->cmdProcess($argv,3);
				break;
			case 'node_firewall_ssh_status':
				$this->cmdProcess($argv,3,true);
				break;
			case 'node_raidusage_list':
				$this->cmdProcess($argv,3,true);
				break;			
			case 'node_reboot':
				$this->cmdProcess($argv,3);
				break;
			case 'node_shutdown':
				$this->cmdProcess($argv,3);
				break;
			case 'node_autosuspend_list':
				$this->cmdProcess($argv,3,true);
				break;
			case 'node_autosuspend_set':
				$this->cmdProcess($argv,4,true);
				break;
			case 'nic_info':
				$this->cmdProcess($argv,3,true);
				break;
			case 'nic_br_set':
				$this->cmdOverArgcProcess($argv,9);
				break;
			case 'nic_dns_set':
				$this->cmdOverArgcProcess($argv,3);
				break;
			case 'nic_br_reset':
				$this->cmdProcess($argv,3,true);
				break;
			case 'nic_clear_all':
				$this->cmdProcess($argv,3,true);
				break;
			case 'node_hostname_add':
				$this->cmdOverArgcProcess($argv,5);
				break;
			case 'bk_lun_browse':
				$this->cmdProcessWithTimeout($argv,8,60,true);
				break;
			case 'bk_lun_browse_samba':
				$this->cmdProcessWithTimeout($argv,7,60,true);
				break;
			case 'bk_lun_login_samba':
				$this->cmdProcessWithTimeout($argv,7,60,true);
				break;
			case 'bk_lun_logout_samba':
				$this->cmdProcess($argv,3);
				break;
			case 'bk_lun_list':
				$this->cmdProcess($argv,3,true);
				break;
			case 'bk_vd_list_diskSize_all':
				$this->cmdProcess($argv,4,true);
				break;
			case 'bk_vd_disk_list':
				$this->cmdOverArgcProcess($argv,5,true);				
				break;
			case 'bk_lun_login':
				$this->cmdProcessWithTimeout($argv,8,60);
				break;
			case 'bk_lun_logout':
				$this->cmdProcess($argv,3);
				break;
			case 'bk_lun_logout_clear':
				$this->cmdProcess($argv,3);
				break;
			case 'bk_lun_format':
				$this->cmdProcessWithTimeout($argv,4,600);
				break;
			case 'bk_lun_clear_samba':
				$this->cmdProcessWithTimeout($argv,4,600);
				break;
			case 'bk_lun_modify_uuid':
				$this->cmdProcess($argv,5);
				break;
			case 'bk_lun_modify_uuid_samba':
				$this->cmdProcess($argv,4);
				break;
			case 'bk_task_vd_backup':
				$this->cmdProcess($argv,7);
				break;
			case 'bk_task_vd_restore_same':
				$this->cmdProcess($argv,14);
				break;
			case 'bk_task_vd_restore_new':
				$this->cmdProcess($argv,15);
				break;
			case 'bk_task_list':
				$this->cmdProcess($argv,3,true);
				break;
			case 'bk_task_clear':
				$this->cmdProcess($argv,3);
				break;
			case 'bk_lun_vd_del':
				$this->cmdProcess($argv,5,true);
				break;
			case 'dk_raid_list':
				$this->cmdProcess($argv,3,true);
				break;
			case 'dk_unimportable_raid_list_disk':
				$this->cmdProcess($argv,4,true);
				break;
			case 'dk_raid_list_disk':
				$this->cmdProcess($argv,4,true);
				break;
			case 'dk_ui_raid_destroy':
				$this->cmdProcess($argv,4,true);
				break;
			case 'dk_ui_raid_create':
				$this->cmdOverArgcProcess($argv,5,true);
				break;
			case 'dk_ui_raid_add':
				$this->cmdOverArgcProcess($argv,4,true);
				break;
			case 'dk_disk_list':
				$this->cmdProcess($argv,3,true);
				break;
			case 'dk_clear_black_list':
				$this->cmdProcess($argv,4,true);
				break;
			case 'dk_disk_locate':
				$this->cmdProcess($argv,4);
				break;
			case 'dk_del_disk_partition':
				$this->cmdOverArgcProcess($argv,4);
				break;
			case 'dk_raid_locate':
				$this->cmdProcess($argv,4);
				break;
			case 'dk_raid_dedup_set':
				$this->cmdProcess($argv,5);
				break;
			case 'node_storage_type_list':
				$this->cmdProcess($argv,3,true);
				break;
			case 'node_maxPowerOn_list':
				$this->cmdProcess($argv,3,true);
				break;
			case 'fw_list_version':
				$this->cmdProcess($argv,3,true);
				break;
			case 'fw_check_version_by_wget':
				$this->cmdProcess($argv,4,true);
				break;
			case 'fw_update_by_wget':
				$this->cmdProcess($argv,4,true);
				break;
			case 'fw_update_by_upload':
				$this->cmdProcess($argv,4,true);
				break;
			case 'fw_update':
				$this->cmdProcess($argv,3,true);
				break;
			case 'fw_update_progress':
				$this->cmdProcess($argv,3,true);
				break;
			case 'fw_sys_list_version':
				$this->cmdProcess($argv,3,true);
				break;
			case 'fw_sys_set_mode':
				$this->cmdProcess($argv,4,true);
				break;
			case 'vd_video_set':
				$this->cmdProcess($argv,5,true);
				break;
			case 'cluster_dedup':
				$this->cmdProcess($argv,4);
				break;
			case 'cluster_dedup_list':
				$this->cmdProcess($argv,3,true);
				break;		
			case 'ups_set':
				$this->cmdProcess($argv,5,true);
				break;
			case 'ups_info':
				$this->cmdProcess($argv,3,true);
				break;	
			case 'cluster_mail_ssmtp_set':
				$this->cmdProcess($argv,15);
				break;
			case 'cluster_mail_ssmtp_list':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_mail_test':
				$this->cmdProcess($argv,3,true);
				break;
			default:
				$this->printHelp();
				break;
		}
	}

	function printHelp()
	{
		echo 'cephIP vd_list_all'.PHP_EOL;
		echo 'cephIP vd_list_diskSize_all'.PHP_EOL;
		echo 'cephIP vd_list vdName'.PHP_EOL;
		echo 'cephIP vd_task_org_create reportMeta vdName cpuCt ramSize(K) diskSize(G) RAIDID isoName vdAlias usbType(3|2) usbRedirCt [ich6|ac97] br [1|10](nicType) cpuLimit(MHz) [0|1](vdSuspend) UUID DomainID [taskfn]'.PHP_EOL;
		echo 'cephIP vd_task_disk_add reportMeta vdName diskSize(G) diskVolumeId hdBus(ide|scsi|virtio) hdCache(writethrough|writeback) domainID'.PHP_EOL;
		echo 'cephIP vd_task_disk_del reportMeta vdName devName [taskfn]'.PHP_EOL;
		echo 'cephIP vd_task_disk_move reportMeta vdName devName diskVolumeId domainId';
		echo 'cephIP vd_task_userprofile_move reportMeta imgName diskVolumeId domainId';
		echo 'cephIP vd_task_vd_del reportMeta vdName [taskfn]'.PHP_EOL;
		echo 'cephIP vd_task_org_clone reportMeta srcVdName dstVdName dstVdAlias uuid DomainID [taskfn]'.PHP_EOL;
		echo 'cephIP vd_task_user_clone reportMeta srcVdName dstVdName dstVdAlias uuid DomainID [taskfn]'.PHP_EOL;
		echo 'cephIP vd_task_list'.PHP_EOL;
		echo 'cephIP vd_nic_add vdName (br0|br1) nicSpeed(1|10) [mac]'.PHP_EOL;
		echo 'cephIP vd_nic_del vdName mac'.PHP_EOL;
		echo 'cephIP vd_task_seed_create reportMeta srcVdName dstVdName dstVdAlias DomainID [taskfn]'.PHP_EOL;
		echo 'cephIP vd_task_seed_overwrite reportMeta srcVdName dstVdName dstVdAlias DomainID'.PHP_EOL;
		echo 'cephIP vd_task_user_create reportMeta srcVdName dstVdName dstVdAlias uuid DomainID [taskfn]'.PHP_EOL;
		echo 'cephIP vd_task_seed_reborn reportMeta srcVdName DomainId'.PHP_EOL;
		echo 'cephIP vd_task_seed_recovery reportMeta srcVdName dstVdName domainId'.PHP_EOL;
		echo 'cephIP vd_iso_list_all'.PHP_EOL;
		echo 'cephIP vd_modify vdName cpuCt ramSize(K) isoFullName vdAlias usbType(3|2) usbRedirCt [ich6|ac97] cpuLimit(MHz) vdSuspend(0|1)'.PHP_EOL;
		echo 'cephIP vd_disk_modify vdName devName(hda|hdc...) hdBus(ide|scsi|virtio) hdCache(writethrough|writeback) diskSize(G)'.PHP_EOL;
		echo 'cephIP vd_poweron_before vdName'.PHP_EOL;
		echo 'cephIP vd_poweron vdName'.PHP_EOL;
		echo 'cephIP vd_poweron_viewer vdName'.PHP_EOL;
		echo 'cephIP vd_poweroff vdName'.PHP_EOL;
		echo 'cephIP vd_shutdown vdName'.PHP_EOL;
		echo 'cephIP vd_del vdName'.PHP_EOL;
		echo 'cephIP vd_iso_attach vdName isoName'.PHP_EOL;
		echo 'cephIP vd_iso_detach vdName'.PHP_EOL;
		echo 'cephIP vd_disable_set vdName [0|1]'.PHP_EOL;
		echo 'cephIP vd_suspend_set vdName [0|1]'.PHP_EOL;
		echo 'cephIP vd_poweron_info vdName'.PHP_EOL;
		echo 'cephIP vd_passwd_set vdName passwd'.PHP_EOL;
		echo 'cephIP vd_userprofile_create imgName diskSize(G) RAIDID'.PHP_EOL;
		echo 'cephID vd_userprofile_resize imgName diskSize(G)'.PHP_EOL;
		echo 'cephIP vd_userprofile_del imgName'.PHP_EOL;
		echo 'cephIP vd_userprofile_attach vdName imgName hdBus(ide|scsi|virtio) hdCache(writethrough|writeback)'.PHP_EOL;
		echo 'cephIP vd_userprofile_detach vdName'.PHP_EOL;
		echo 'cephIP vd_connect_passwd_set vdName passwd'.PHP_EOL;
		echo 'cephIP vdss_get_view_layer vdName'.PHP_EOL;
		echo 'cephIP vdss_task_list'.PHP_EOL;
		echo 'cephIP vdss_task_take reportMeta vdName maxLayer overFlag(0|1)'.PHP_EOL;
		echo 'cephIP vdss_task_del reportMeta vdName ssName'.PHP_EOL;
		echo 'cephIP vdss_task_view reportMeta vdName ssName'.PHP_EOL;
		echo 'cephIP vdss_task_unview reportMeta vdName'.PHP_EOL;
		echo 'cephIP vd_ss_view vdName ssName viewVdName keepNic|noNic';
		echo 'cephIP vd_ss_unview vdName(src)';
		echo 'cephIP vdss_task_rollback reportMeta vdName ssName'.PHP_EOL;
		echo 'cephIP vd_task_export reportMeta vdName DomainID'.PHP_EOL;
		echo 'cephIP vd_task_import reportMeta importName dstVdName dstVdAlias uuid hdaVolId hdcVolId hddVolId hdeVolId hdfVolId hdgVolId hdhVolId  DomainID'.PHP_EOL;
		echo 'cephIP vd_import_list'.PHP_EOL;
		echo 'cephIP vd_list_with_ss vdName'.PHP_EOL;
		echo 'cephIP node_list_info_local'.PHP_EOL;
		echo 'cephIP node_list_util_local'.PHP_EOL;
		echo 'cephIP node_alarm_set (0|1)flag'.PHP_EOL;
		echo 'cephIP node_alarm_list'.PHP_EOL;
		echo 'cephIP node_samba_passwd_set passwd'.PHP_EOL;
		echo 'cephIP node_samba_status'.PHP_EOL;
		echo 'cephIP node_samba_start'.PHP_EOL;
		echo 'cephIP node_samba_stop'.PHP_EOL;
		echo 'cephIP node_samba_stop'.PHP_EOL;
		echo 'cephIP node_hostname_get'.PHP_EOL;
		echo 'cephIP node_drop_cache'.PHP_EOL;
		echo 'cephIP node_raidusage_list'.PHP_EOL;
		echo 'cephIP node_reboot'.PHP_EOL;
		echo 'cephIP node_shutdown'.PHP_EOL;
		echo 'cephIP node_autosuspend_list'.PHP_EOL;		
		echo 'cephIP node_autosuspend_set timeout'.PHP_EOL;
		echo 'cephIP node_firewall_enable_ssh'.PHP_EOL;
		echo 'cephIP node_firewall_disable_ssh'.PHP_EOL;
		echo 'cephIP node_firewall_ssh_status'.PHP_EOL;
		echo 'cephIP nic_info'.PHP_EOL;
		echo 'cephIP nic_br_set br_idx ipv4 mask gateway eth_num eth_idx1 [eth_idx2, ...] [br_idx ...] ...'.PHP_EOL;
		echo 'cephIP nic_dns_set [dns] [ ... ]'.PHP_EOL;
		echo 'cephIP nic_br_reset'.PHP_EOL;
		echo 'cephIP nic_clear_all'.PHP_EOL;
		echo 'cephIP node_hostname_add [-L] hostname IP1 [IP2 ...](-L:set local hostname)'.PHP_EOL;
		echo 'cephIP vd_task_clear'.PHP_EOL;
		echo 'cephIP vdss_task_clear cephID'.PHP_EOL;
		echo 'cephIP bk_lun_browse userName pwd ip port targetName'.PHP_EOL;
		echo 'cephIP bk_lun_list'.PHP_EOL;
		echo 'cephIP bk_vd_list_diskSize_all bkCephUuid'.PHP_EOL;
		echo 'cephIP bk_vd_disk_list bkCephUuid vdName'.PHP_EOL;
		echo 'cephIP bk_lun_login userName pwd ip port targetName'.PHP_EOL;
		echo 'cephIP bk_lun_logout'.PHP_EOL;
		echo 'cephIP bk_lun_logout_clear'.PHP_EOL;
		echo 'cephIP bk_lun_login_samba userName pwd mountPoint port'.PHP_EOL;
		echo 'cephIP bk_lun_logout_samba'.PHP_EOL;
		echo 'cephIP bk_lun_browse_samba userName pwd mountPoint port'.PHP_EOL;
		echo 'cephIP bk_lun_format description(base64)'.PHP_EOL;
		echo 'cephIP bk_lun_modify_uuid oldCephUuid description(base64)'.PHP_EOL;
		echo 'cephIP bk_lun_modify_uuid_samba description(base64)'.PHP_EOL;
		echo 'cephIP bk_lun_clear_samba description(base64)'.PHP_EOL;
		echo 'cephIP bk_task_vd_backup reportMeta vdName maxLayer overFlag(0|1)'.PHP_EOL;
		echo 'cephIP bk_task_vd_restore_same reportMeta bkCephUuid vdName vdAlias hdaVolId hdcVolId hddVolId hdeVolId hdfVolId hdgVolId hdhVolId'.PHP_EOL;
		echo 'cephIP bk_task_vd_restore_new reportMeta bkCephUuid vdName vdAlias uuid hdaVolId hdcVolId hddVolId hdeVolId hdfVolId hdgVolId hdhVolId'.PHP_EOL;
		echo 'cephIP bk_task_list'.PHP_EOL;
		echo 'cephIP bk_task_clear'.PHP_EOL;
		echo 'cephIP bk_lun_vd_del bkCephUuid vdName'.PHP_EOL;
		echo 'cephIP dk_raid_list'.PHP_EOL;
		echo 'cephIP dk_ui_raid_destroy force(1|0)'.PHP_EOL;
		echo 'cephIP dk_ui_raid_create RAIDID level diskID...'.PHP_EOL;
		echo 'cephIP dk_ui_raid_add RAIDID diskID...'.PHP_EOL;
		echo 'cephIP dk_disk_list'.PHP_EOL;
		echo 'cephIP dk_clear_black_list diskID'.PHP_EOL;
		echo 'cephIP dk_disk_locate [slot(1,2...)]'.PHP_EOL;
		echo 'cephIP dk_raid_locate [raidID(1,2...)]'.PHP_EOL;
		echo 'cephIP dk_unimportable_raid_list_disk [raidID(1,2...)]'.PHP_EOL;
		echo 'cephIP dk_del_disk_partition diskID ...'.PHP_EOL;
		echo 'cephIP dk_raid_list_disk'.PHP_EOL;
		echo 'cephIP dk_raid_dedup_set raidId(1|2...) dedupValue(on|off)'.PHP_EOL;
		echo 'cephIP node_storage_type_list'.PHP_EOL;
		echo 'cephIP node_maxPowerOn_list'.PHP_EOL;
		echo 'cephIP fw_list_version'.PHP_EOL;
		echo 'cephIP fw_check_version_by_wget [raid|local]'.PHP_EOL;
		echo 'cephIP fw_update_by_wget [raid|local]'.PHP_EOL;
		echo 'cephIP fw_update'.PHP_EOL;
		echo 'cephIP fw_update_progress'.PHP_EOL;
		echo 'cephIP vd_video_set vdName [1|2]'.PHP_EOL;
		echo 'cephIP cluster_dedup [on|off]'.PHP_EOL;
		echo 'cephIP cluster_dedup_list'.PHP_EOL;	
		echo 'cephIP ups_set IP DelayTime'.PHP_EOL;
		echo 'cephIP ups_info'.PHP_EOL;	
		echo 'cephIP cluster_mail_ssmtp_set ssmtp port user passwd tls sender [enable|disable] reciver1 reciver2 reciver3 reciver4 reciver5'.PHP_EOL;
		echo 'cephIP cluster_mail_ssmtp_list'.PHP_EOL;
		echo 'cephIP cluster_mail_test'.PHP_EOL;
		exit(100);
	}
	
	function cmdCephProcess($argv,$isBackground=false,$printOutput=true)
	{
		$cmd = $this->sshCephCmd.$argv[1].' sudo cd /var/www/html/webapi;sudo java -jar Ceph_Mgt.jar  ';
		for($i=2;$i<count($argv);$i++){
			$cmd .= $argv[$i].' ';
		}
		if(isBackground)
			$cmd .= ' >/dev/null 2>&1 &';						
		exec($cmd,$output,$rtn);
		// var_dump($output);
		// var_dump($rtn);
		if($printOutput){
			$outputStr = '';
			foreach ($output as $value) {
				$outputStr.=$value;
			}
			echo $outputStr;
		}
		exit($rtn);
	}
	
	function cmdOverArgcProcess($argv,$argc,$printOutput=false)
	{					
		if(sizeof($argv) < $argc){
			exit(99);
		}	
		$this->execSSHCmd($argv,count($argv),$printOutput);
	}	

	function cmdProcess($argv,$argc,$printOutput=false)
	{			
		if(sizeof($argv) != $argc){
			exit(99);
		}	
		$this->execSSHCmd($argv,$argc,$printOutput,NULL);
	}	

	function cmdProcessWithTimeout($argv,$argc,$timeout,$printOutput=false)
	{			
		if(sizeof($argv) != $argc){
			exit(99);
		}	
		$this->execSSHCmd($argv,$argc,$printOutput,$timeout);
	}

	function execSSHCmd($argv,$argc,$printOutput,$timeout)
	{
		// if(isset($argv[$positionCephID]))
		// 	$argv[$positionCephID] = 'ceph';	
		if(isset($timeout))
			$cmd =  str_replace("@1", $timeout, $this->sshCmdCustomTimeout);	
		else
			$cmd = $this->sshCmd;
		$cmd .= $argv[1].' "sudo /var/www/html/script/cp_util.sh ';
		for($i=2;$i<$argc;$i++){
			$cmd .= '\"'.$argv[$i].'\" ';
		}		
		$cmd .= '"';					
		// var_dump($cmd);
		exec($cmd,$output,$rtn);
		// var_dump($output);
		// var_dump($rtn);
		if($printOutput){
			$outputStr = '';
			foreach ($output as $value) {
				$outputStr.=$value;
			}
			echo $outputStr;
		}
		exit($rtn);
	}
}
$_SERVER['SERVER_ADDR'] = 'HCI Host';
$cmd = new CephCmd($argv);