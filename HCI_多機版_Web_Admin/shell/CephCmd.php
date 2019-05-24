<?php
// define("DEBUG", true);
define("DEBUG", false);
// ini_set('display_errors', 'On');
include_once("/var/www/html/libraries/ErrorEnum.php");
include_once("/var/www/html/libraries/BaseAPI.php");
include_once("/var/www/html/libraries/HandleIP.php");
class CephCmd extends BaseAPI
{
	private $sshCmd = 'timeout 60 ssh -o ConnectTimeout=10 -o ServerAliveInterval=10 -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no root@';
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
				$this->cmdProcess($argv,26);
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
			case 'vd_import_list_disk':
				$this->cmdProcess($argv,4,true);
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
				$this->cmdOverArgcProcess($argv,6,true);
				break;
			case 'vd_nic_del':
				$this->cmdProcess($argv,5,true);
				break;
			case 'vd_iso_list_all':
				$this->cmdProcess($argv,3,true);
				break;
			case 'vd_modify':
				$this->cmdProcess($argv,17);
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
			case 'vd_test':
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
			case 'vdss_task_rollback':
				$this->cmdProcess($argv,6);
				break;			
			case 'vd_ss_view':
				$this->cmdProcess($argv,7);
				break;			
			case 'vd_ss_unview':
				$this->cmdProcess($argv,4);
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
				$this->cmdOverArgcProcess($argv,2);
				break;
			case 'node_shutdown':
				$this->cmdOverArgcProcess($argv,2);
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
			case 'bk_lun_clear_samba':
				$this->cmdProcessWithTimeout($argv,4,600);
				break;
			case 'bk_lun_modify_uuid_samba':
				$this->cmdProcess($argv,4);
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
				$this->cmdProcess($argv,3,3);
				break;
			case 'bk_lun_logout_clear':
				$this->cmdProcess($argv,3,3);
				break;
			case 'bk_lun_format':
				$this->cmdProcessWithTimeout($argv,4,600);
				break;
			case 'bk_lun_modify_uuid':
				$this->cmdProcess($argv,5);
				break;
			case 'bk_task_vd_backup':
				$this->cmdProcess($argv,7);
				break;
			case 'bk_task_vd_restore_same':
				$this->cmdProcess($argv,14);
				break;
			case 'bk_task_seed_restore_same':
				$this->cmdProcess($argv,14);
				break;
			case 'bk_task_vd_restore_new':
				$this->cmdProcess($argv,15);
				break;
			case 'bk_task_seed_restore_new':
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
			case 'cluster_node_list_info_all':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_node_list_info':
				$this->cmdProcess($argv,4,true);
				break;
			case 'cluster_node_list_util_all':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_node_list_util':
				$this->cmdProcess($argv,4,true);
				break;
			case 'cluster_alarm_set':
				$this->cmdProcess($argv,4);
				break;
			case 'cluster_alarm_list':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_autosuspend_list':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_autosuspend_set':
				$this->cmdProcess($argv,4,true);
				break;
			case 'cluster_node_drop_cache':
				$this->cmdProcess($argv,4);
				break;
			case 'cluster_raidusage_list':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_raidusage_set':
				$this->cmdProcess($argv,5);
				break;
			case 'cluster_node_shutdown':
				$this->cmdProcess($argv,4);
				break;
			case 'cluster_node_reboot':
				$this->cmdOverArgcProcess($argv,4);
				break;
			case 'cluster_node_maxPowerOn_list':
				$this->cmdProcess($argv,4,true);
				break;
			case 'cluster_maxPowerOn_list':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_controlNode_ip_list':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_nic_info_all':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_nic_br_set':
				$this->cmdOverArgcProcess($argv,10);
				break;
			case 'cluster_nic_dns_set':
				$this->cmdOverArgcProcess($argv,4);
				break;
			case 'cluster_nic_br_reset':
				$this->cmdProcess($argv,4);
				break;
			case 'cluster_nic_clear_all':
				$this->cmdProcess($argv,4);
				break;
			case 'cluster_node_list_state_all':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_create':
				$this->cmdOverArgcProcessWithTimeout($argv,9,600,true,true);
				break;
			case 'cluster_create_check':
				$this->cmdOverArgcProcessWithTimeout($argv,9,600,true,true);
				break;
			case 'cluster_restore':
				$this->cmdOverArgcProcessWithTimeout($argv,9,300);
				break;
			case 'cluster_restore_all':
				$this->cmdOverArgcProcessWithTimeout($argv,3,300,true,true);
				break;
			case 'cluster_del':
				sleep(5);
				$this->cmdProcess($argv,3);
				break;
			case 'cluster_check_cluster_created':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_vms_ip_list':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_list_info':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_list_state_for_web':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_node_set_maintain':
				if(isset($argv[4]) && $argv[4] == 'yes'){
					sleep(2);			
				}
				$this->cmdProcess($argv,5);
				break;
			case 'cluster_node_set_maintain_check':				
				$this->cmdProcess($argv,5);
				break;
			case 'cluster_shutdown':
				// sleep(2);
				$this->cmdProcess($argv,3);
				break;
			case 'cluster_reboot':
				// sleep(2);
				$this->cmdProcess($argv,4);
				break;		
			case 'cluster_node_list_poweron_vd':
				$this->cmdProcess($argv,4,true);
				break;
			case 'cluster_samba_status':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_samba_start':
				$this->cmdProcess($argv,3);
				break;
			case 'cluster_samba_stop':
				$this->cmdProcess($argv,3);
				break;
			case 'cluster_samba_passwd_set':
				$this->cmdProcess($argv,4);
				break;
			case 'cluster_list_poweron_vd':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_capacity':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_split_recovery':
				$this->cmdProcess($argv,4);
				break;
			case 'cluster_vms_set':
				$this->cmdProcess($argv,3);
				break;
			case 'cluster_dedup_set':
				$this->cmdProcess($argv,10);
				break;
			case 'cluster_dedup':
				$this->cmdProcess($argv,4);
				break;
			case 'cluster_dedup_list':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_volume_list':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_volume_add':
				$this->cmdOverArgcProcessWithTimeout($argv,6,600);
				break;
			case 'cluster_group_add':
			 	$this->cmdOverArgcProcessWithTimeout($argv,10,300);
				break;		
			case 'cluster_locate_group':
				$this->cmdProcess($argv,4);
				break;
			case 'cluster_locate_node':
				$this->cmdProcess($argv,4);
				break;		
			case 'vd_task_gluster_group_add':
				$this->cmdProcess($argv,12,true,true);
				break;
			case 'vd_task_gluster_volume_add':
				$this->cmdProcess($argv,8);
				break;
			case 'cluster_group_del':
				$this->cmdProcessWithTimeout($argv,4,300);
				break;
			case 'cluster_group_master_set':
				$this->cmdProcess($argv,5);
				break;
			case 'cluster_replace_node':
				$this->cmdProcessWithTimeout($argv,4,300);
				break;
			case 'cluster_replace_node_new':
				$this->cmdProcessWithTimeout($argv,6,300,true,true);
				break;
			case 'cluster_replace_node_new_check':
				$this->cmdProcessWithTimeout($argv,6,300,true,true);
				break;
			case 'cluster_node_mode_set':
				$this->cmdProcess($argv,5);
				break;
			case 'vd_live_migrate':
				$this->cmdProcess($argv,5);
				break;
			case 'cluster_fw_update_by_upload':
				$this->cmdProcess($argv,5);
				break;
			case 'cluster_fw_update_progress':
				$this->cmdProcess($argv,4,true);
				break;
			case 'cluster_split_list_info':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_clear_node':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_restore_progress':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_node_set_maintain_web':
				$this->cmdProcess($argv,4);
				break;
			case 'cluster_restore_all_check':
				$this->cmdProcessWithTimeout($argv,3,600,true,true);
				break;
			case 'cluster_vms_list_ready_group':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_vms_skip_wait_group_ready':
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
			case 'cluster_group_master_set_local':
				$this->cmdProcess($argv,3);
				break;
			case 'ups_set':
				$this->cmdProcess($argv,5,true);
				break;
			case 'ups_info':
				$this->cmdProcess($argv,3,true);
				break;
			case 'vd_task_seed_clone_back':
				$this->cmdProcess($argv,9);
				break;
			case 'bk_task_seed_backup':
				$this->cmdProcess($argv,5);
				break;
			case 'vd_task_err_task':
				$this->cmdProcess($argv,5);
				break;
			case 'vdss_task_err_task':
				$this->cmdProcess($argv,5);
				break;
			case 'bk_task_err_task':
				$this->cmdProcess($argv,5);
				break;
			case 'thin_uncompress':
				$this->cmdProcess($argv,3);
				break;
			case 'vd_list_specific':
				$this->cmdOverArgcProcess($argv,3,true);
				break;
			case 'cluster_node_spice_port_set':
				$this->cmdProcess($argv,6);
				break;
			case 'mysql_back':
				$this->mysql_back("VDICeph_back.sql");
				break;
			case 'mysql_bk_for_del':
				$this->mysql_back("VDICeph_del.sql");
				break;
			case 'remote_list_external':
				$argv[2] = 'list_external';
				$this->execSSHCmdForRemotePHP($argv,3,true);
				break;
			case 'list_external':
				$ipC = new IPProcess();
            	$ipC->listExternal($outputExternal);
		        echo json_encode($outputExternal);
				break;
			case 'bk_task_up_backup':
				$this->cmdProcess($argv,7);
				break;
			case 'vd_userprofile_list_diskSize_all':
				$this->cmdProcess($argv,3,true);
				break;
			case 'bk_task_up_restore_same':
				$this->cmdProcess($argv,7);
				break;
			case 'bk_task_up_restore_new':
				$this->cmdProcess($argv,7);
				break;
			case 'vd_userprofile_ss_view':
				$this->cmdProcess($argv,5);
				break;
			case 'vd_userprofile_ss_unview':
				$this->cmdProcess($argv,4);
				break;
			case 'vd_userprofile_list_with_ss':
				$this->cmdProcess($argv,4,true);
				break;
			case 'bk_up_list_diskSize':
				$this->cmdProcess($argv,4,true);
				break;
			case 'bk_lun_up_del':
				$this->cmdProcess($argv,5,true);
				break;
			case 'vdss_task_up_take':
				$this->cmdProcess($argv,7);
				break;
			case 'vdss_task_up_del':
				$this->cmdProcess($argv,6);
				break;
			case 'vdss_task_up_rollback':
				$this->cmdProcess($argv,6);
				break;
			case 'vd_task_gateway_create':
				$this->cmdProcess($argv,9);
				break;
			case 'vd_nic_replace':
				$this->cmdProcess($argv,6,true);
				break;
			case 'gw_qemu_ga':
				$this->cmdQemuGAProcess($argv,4,true);
				break;
			case 'nic_vlan_info':
				$this->cmdProcess($argv,3,true);
				break;
                                        case 'vd_poweron_prefer_node_set':
				$this->cmdProcess($argv,5);
			case 'nic_vlan_begin_config':
				$this->cmdProcess($argv,3);
				break;
			case 'nic_vlan_end_config':
				$this->cmdProcess($argv,3);
				break;
			case 'nic_vlan_create_local':
				$this->cmdOverArgcProcess($argv,10);
				break;
			case 'nic_vlan_abort_config':
				$this->cmdProcess($argv,3);
				break;
			case 'cluster_node_vlan_list_util':
				$this->cmdProcess($argv,4,true);
				break;
			case 'vd_migrate_status':
				$this->cmdProcess($argv,4,true);
				break;
			case 'cluster_vms_ip_list_from_storage':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_dns_list':
				$this->cmdProcess($argv,3,true);
				break;
			case 'cluster_dns_set':
				$this->cmdProcess($argv,4);
				break;
			case 'cluster_vms_ip_set':
				$this->cmdProcess($argv,5);
				break;
			default:
				$this->printHelp();
				break;
		}
	}

	function mysql_back($name)
	{
		$cmd = 'mysql VDICeph -e "show tables"';
		exec($cmd,$output,$rtn);
		// var_dump($rtn);
		if($rtn == 0){
			$cmd = "mysqldump VDICeph > /$name";
			exec($cmd,$output,$rtn);
			// var_dump($rtn);
			if($rtn != 0){
				unlink("/$name");
			}
			else{
				$cmd = "mysqldump VDICeph > /artgluster/$name";
				exec($cmd,$output,$rtn);
			}
		}
	}
	
	function printHelp()
	{
		echo 'cephIP vd_list_all'.PHP_EOL;
		echo 'cephIP vd_list_diskSize_all'.PHP_EOL;
		echo 'cephIP vd_list vdName'.PHP_EOL;
		echo 'cephIP vd_task_org_create reportMeta vdName cpuCt ramSize(K) diskSize(G) volumeID isoName vdAlias usbRedirCt usbType(3|2) [ich6|ac97] br [1|10](nicType) cpuLimit(MHz) [0|1](vdSuspend) UUID cpuType(fv|hv) hdBus(ide|scsi|virtio) hdCache(writethrough|writeback) DomainID bios(legacy|uefi) vgaCt(0|1...) preferNode'.PHP_EOL;
		echo 'cephIP vd_task_disk_add reportMeta vdName diskSize(G) diskVolumeId hdBus(ide|scsi|virtio) hdCache(writethrough|writeback) domainID'.PHP_EOL;
		echo 'cephIP vd_task_disk_del reportMeta vdName devName domainID'.PHP_EOL;
		echo 'cephIP vd_task_disk_move reportMeta vdName devName diskVolumeId domainId';
		echo 'cephIP vd_task_userprofile_move reportMeta imgName diskVolumeId domainId';
		echo 'cephIP vd_task_vd_del reportMeta vdName lockTime'.PHP_EOL;
		echo 'cephIP vd_task_org_clone reportMeta srcVdName dstVdName dstVdAlias uuid DomainID'.PHP_EOL;
		echo 'cephIP vd_task_user_clone reportMeta srcVdName dstVdName dstVdAlias uuid DomainID'.PHP_EOL;
		echo 'cephIP vd_task_list'.PHP_EOL;
		echo 'cephIP vd_task_wait_del taskName'.PHP_EOL;
		echo 'cephIP vd_nic_add vdName (br0|br1) nicSpeed(1|10) [mac]'.PHP_EOL;
		echo 'cephIP vd_nic_del vdName mac'.PHP_EOL;
		echo 'cephIP vd_task_seed_create reportMeta srcVdName dstVdName dstVdAlias DomainID'.PHP_EOL;
		echo 'cephIP vd_task_seed_overwrite reportMeta srcVdName dstVdName dstVdAlias DomainID'.PHP_EOL;
		echo 'cephIP vd_task_user_create reportMeta srcVdName dstVdName dstVdAlias uuid DomainID'.PHP_EOL;
		echo 'cephIP vd_task_seed_reborn reportMeta srcVdName DomainId'.PHP_EOL;
		echo 'cephIP vd_task_seed_recovery reportMeta srcVdName dstVdName domainId'.PHP_EOL;
		echo 'cephIP vd_iso_list_all'.PHP_EOL;
		echo 'cephIP vd_modify vdName cpuCt ramSize(K) isoFullName vdAlias usbRedirCt usbType(3|2) [ich6|ac97] cpuLimit(MHz) vdSuspend(0|1) cpuType(fv|hv|sandyBridge) bios(legacy|uefi) vgaCt(0|1...) qxlCt(1|0)'.PHP_EOL;
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
		echo 'cephIP vd_userprofile_create imgName diskSize(G) volumeID'.PHP_EOL;
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
		echo 'cephIP vdss_task_rollback reportMeta vdName ssName'.PHP_EOL;
		echo 'cephIP vd_ss_view vdName ssName viewVdName keepNic|noNic';
		echo 'cephIP vd_ss_unview vdName(src)';
		echo 'cephIP vd_task_export reportMeta vdName domainID'.PHP_EOL;
		echo 'cephIP vd_task_import reportMeta importName dstVdName dstVdAlias uuid hdaVolId hdcVolId hddVolId hdeVolId hdfVolId hdgVolId hdhVolId DomainID'.PHP_EOL;
		echo 'cephIP vd_import_list'.PHP_EOL;
		echo 'cephIP vd_import_list_disk importName'.PHP_EOL;
		echo 'cephIP vd_list_with_ss vdName'.PHP_EOL;
		echo 'cephIP node_list_info_local'.PHP_EOL;
		echo 'cephIP node_list_util_local'.PHP_EOL;
		echo 'cephIP node_alarm_set (0|1)flag'.PHP_EOL;
		echo 'cephIP node_alarm_list'.PHP_EOL;
		echo 'cephIP node_samba_passwd_set passwd'.PHP_EOL;
		echo 'cephIP node_samba_status'.PHP_EOL;
		echo 'cephIP node_samba_start'.PHP_EOL;		
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
		echo 'cephIP vdss_task_clear'.PHP_EOL;
		echo 'cephIP bk_lun_browse userName pwd ip port targetName'.PHP_EOL;
		echo 'cephIP bk_lun_login_samba userName pwd mountPoint port'.PHP_EOL;
		echo 'cephIP bk_lun_logout_samba'.PHP_EOL;
		echo 'cephIP bk_lun_browse_samba userName pwd mountPoint port'.PHP_EOL;
		echo 'cephIP bk_lun_modify_uuid_samba description(base64)'.PHP_EOL;
		echo 'cephIP bk_lun_clear_samba description(base64)'.PHP_EOL;
		echo 'cephIP bk_lun_list'.PHP_EOL;
		echo 'cephIP bk_vd_list_diskSize_all bkCephUuid'.PHP_EOL;
		echo 'cephIP bk_vd_disk_list bkCephUuid vdName'.PHP_EOL;
		echo 'cephIP bk_lun_login userName pwd ip port targetName'.PHP_EOL;
		echo 'cephIP bk_lun_logout'.PHP_EOL;
		echo 'cephIP bk_lun_logout_clear'.PHP_EOL;
		echo 'cephIP bk_lun_format description(base64)'.PHP_EOL;
		echo 'cephIP bk_lun_modify_uuid oldCephUuid description(base64)'.PHP_EOL;
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
		echo 'cephIP node_storage_type_list'.PHP_EOL;
		echo 'cephIP node_maxPowerOn_list'.PHP_EOL;
		echo 'cephIP fw_list_version'.PHP_EOL;
		echo 'cephIP fw_check_version_by_wget [raid|local]'.PHP_EOL;
		echo 'cephIP fw_update_by_wget [raid|local]'.PHP_EOL;
		echo 'cephIP fw_update'.PHP_EOL;
		echo 'cephIP fw_update_progress'.PHP_EOL;
		echo 'cephIP vd_video_set vdName [1|2]'.PHP_EOL;
		echo 'cephIP cluster_node_list_info_all'.PHP_EOL;
		echo 'cephIP cluster_node_list_info nodeName'.PHP_EOL;
		echo 'cephIP cluster_node_list_util_all'.PHP_EOL;
		echo 'cephIP cluster_node_list_util nodeName'.PHP_EOL;
		echo 'cephIP cluster_alarm_set (0|1)flag'.PHP_EOL;
		echo 'cephIP cluster_alarm_list'.PHP_EOL;
		echo 'cephIP cluster_autosuspend_list'.PHP_EOL;
		echo 'cephIP cluster_autosuspend_set time'.PHP_EOL;
		echo 'cephIP cluster_node_drop_cache nodeName'.PHP_EOL;
		echo 'cephIP cluster_raidusage_list'.PHP_EOL;
		echo 'cephIP cluster_raidusage_set raidAlarm raidUsageMax'.PHP_EOL;
		echo 'cephIP cluster_node_shutdown nodeName'.PHP_EOL;
		echo 'cephIP cluster_node_reboot nodeName'.PHP_EOL;
		echo 'cephIP cluster_node_maxPowerOn_list nodeName'.PHP_EOL;
		echo 'cephIP cluster_maxPowerOn_list'.PHP_EOL;
		echo 'cephIP cluster_controlNode_ip_list'.PHP_EOL;
		echo 'cephIP cluster_nic_info_all'.PHP_EOL;
		echo 'cephIP cluster_nic_br_reset nodeName'.PHP_EOL;
		echo 'cephIP cluster_nic_br_set nodeName br_idx static|dhcp ipv4 mask gateway eth_num [eth_idx1 ...] [br_idx ...] ...'.PHP_EOL;
		echo 'cephIP cluster_nic_dns_set nodeName [dns] [ ... ]'.PHP_EOL;
		echo 'cephIP cluster_nic_clear_all nodeName'.PHP_EOL;
		echo 'cephIP cluster_node_list_state_all'.PHP_EOL;
		echo 'cephIP cluster_create vmsIp vmsMask nodeAlias1 ip1 nodeAlias2 ip2'.PHP_EOL;
		echo 'cephIP cluster_create_check vmsIp vmsMask nodeAlias1 ip1 nodeAlias2 ip2'.PHP_EOL;
		echo 'cephIP cluster_restore vmsIp vmsMask nodeAlias1 ip1 nodeAlias2 ip2'.PHP_EOL;
		echo 'cephIP cluster_restore_all vmsIp'.PHP_EOL;
		echo 'cephIP cluster_check_cluster_created'.PHP_EOL;
		echo 'cephIP cluster_del'.PHP_EOL;
		echo 'cephIP cluster_vms_ip_list'.PHP_EOL;
		echo 'cephIP cluster_list_info'.PHP_EOL;
		echo 'cephIP cluster_list_state_for_web'.PHP_EOL;
		echo 'cephIP cluster_node_set_maintain nodeName yes|no'.PHP_EOL;
		echo 'cephIP cluster_node_set_maintain_check nodeName yes|no'.PHP_EOL;
		echo 'cephIP cluster_shutdown'.PHP_EOL;
		echo 'cephIP cluster_reboot 1|0'.PHP_EOL;
		echo 'cephIP cluster_node_list_poweron_vd nodeName'.PHP_EOL;
		echo 'cephIP cluster_samba_status'.PHP_EOL;
		echo 'cephIP cluster_samba_start'.PHP_EOL;
		echo 'cephIP cluster_samba_stop'.PHP_EOL;
		echo 'cephIP cluster_samba_passwd_set passwd'.PHP_EOL;
		echo 'cephIP cluster_list_poweron_vd'.PHP_EOL;
		echo 'cephIP cluster_capacity'.PHP_EOL;
		echo 'cephIP cluster_split_recovery nodeName'.PHP_EOL;
		echo 'cephIP cluster_vms_set [vmsName]'.PHP_EOL;
		echo 'cephIP vd_live_migrate vdName nodeTo'.PHP_EOL;
		echo 'cephIP cluster_fw_update_by_upload nodeName [raid|local]'.PHP_EOL;
		echo 'cephIP cluster_fw_update_progress nodeName'.PHP_EOL;
		echo 'cephIP cluster_split_list_info'.PHP_EOL;	
		echo 'cephIP cluster_dedup [on|off]'.PHP_EOL;
		echo 'cephIP cluster_dedup_set vol1[on|off] vol2[on|off] ... vol7[on|off]'.PHP_EOL;
		echo 'cephIP cluster_dedup_list'.PHP_EOL;
		echo 'cephIP cluster_volume_list'.PHP_EOL;
		echo 'cephIP cluster_volume_add clearFlag[0|1] groupId raidId'.PHP_EOL;
		echo 'cephIP cluster_group_add clearFlag[0|1] groupId nodeAlias1 ip1 nodeAlias2 ip2 raidId [raidId...]'.PHP_EOL;		
		echo 'cephIP cluster_group_del groupId'.PHP_EOL;
		echo 'cephIP cluster_group_master_set groupId nodeName'.PHP_EOL;
		echo 'cephIP cluster_replace_node nodeName'.PHP_EOL;
		echo 'cephIP cluster_replace_node_new nodeNameOld aliasNameNew nodeNameNew'.PHP_EOL;
		echo 'cephIP cluster_replace_node_new_check nodeNameOld aliasNameNew nodeNameNew'.PHP_EOL;
		echo 'cephIP cluster_node_mode_set nodeName nodeMode(hybrid|storage)'.PHP_EOL;
		echo 'cephIP cluster_clear_node'.PHP_EOL;
		echo 'cephIP cluster_restore_progress'.PHP_EOL;
		echo 'cephIP cluster_node_set_maintain_web  yes|no'.PHP_EOL;
		echo 'cephIP cluster_restore_all_check'.PHP_EOL;
		echo 'cephIP vd_task_gluster_group_add reportMeta clearFlag(0|1) groupId nodeAlias1 ip1 nodeAlias2 ip2 raidId domainId'.PHP_EOL;
		echo 'cephIP cluster_group_add_check nodeAlias1 ip1 nodeAlias2 ip2 groupId'.PHP_EOL;
		echo 'cephIP vd_task_gluster_volume_add reportMeta clearFlag(0|1) groupId raidId(0,1,...) domainId'.PHP_EOL;
		echo 'cephIP ups_set IP DelayTime'.PHP_EOL;
		echo 'cephIP ups_info'.PHP_EOL;
		echo 'cephIP cluster_locate_group groupId'.PHP_EOL;
		echo 'cephIP cluster_locate_node nodeName'.PHP_EOL;
		echo 'cephIP cluster_vms_list_ready_group'.PHP_EOL;
		echo 'cephIP cluster_vms_skip_wait_group_ready'.PHP_EOL;
		echo 'cephIP cluster_mail_ssmtp_set ssmtp port user passwd tls sender [enable|disable] reciver1 reciver2 reciver3 reciver4 reciver5'.PHP_EOL;
		echo 'cephIP cluster_mail_ssmtp_list'.PHP_EOL;
		echo 'cephIP cluster_mail_test'.PHP_EOL;
		echo 'cephIP cluster_group_master_set_local'.PHP_EOL;
		echo 'cephIP vd_task_seed_clone_back reportMeta srcVdName dstVdName dstVdAlias uuid domainId'.PHP_EOL;
		echo 'cephIP bk_task_seed_backup reportMeta vdName'.PHP_EOL;
		echo 'cephIP bk_task_seed_restore_same reportMeta bkCephUuid vdName vdAlias hdaVolId hdcVolId hddVolId hdeVolId hdfVolId hdgVolId hdhVolId'.PHP_EOL;
		echo 'cephIP bk_task_seed_restore_new reportMeta bkCephUuid vdName vdAlias uuid hdaVolId hdcVolId hddVolId hdeVolId hdfVolId hdgVolId hdhVolId'.PHP_EOL;
		echo 'cephIP vd_task_err_task reportMeta taskType(orgCreate|diskAdd|diskDel|diskMove|usrpfMove|orgClone|seedCloneBk|userClone|userCreate|seedCreate|seedOverwrite|seedReborn|seedRecovery|glusterGroupAdd|glusterVolumeAdd|glusterGroupModeSet|nodeModeSet|vdExport|userExport|vdImport'.PHP_EOL;
		echo 'cephIP vdss_task_err_task reportMeta taskType(ssTake|ssDel|ssRol|ssUpTake|ssUpDel|ssUpRol'.PHP_EOL;
		echo 'cephIP bk_task_err_task reportMeta taskType(bkBackup|bkResSame|bkResNew|bkSeedBackup|bkSeedResSame|bkSeedResNew'.PHP_EOL;
		echo 'cephIP cluster_node_spice_port_set nodeName portMin portMax'.PHP_EOL;
		echo 'cephIP thin_uncompress'.PHP_EOL;
		echo 'cephIP vd_list_specific'.PHP_EOL;
		echo 'cephIP bk_task_up_backup reportMeta imgName maxLayer overFlag(0|1)'.PHP_EOL;		
		echo 'cephIP vdss_task_up_take reportMeta imgName maxLayer overFlag(0|1)'.PHP_EOL;
		echo 'cephIP vdss_task_up_del reportMeta imgName ssName'.PHP_EOL;
		echo 'cephIP vdss_task_up_rollback reportMeta imgName ssName'.PHP_EOL;
		echo 'cephIP vd_userprofile_list_with_ss imgName'.PHP_EOL;
		echo 'cephIP vd_userprofile_list_diskSize_all'.PHP_EOL;	
		echo 'cephIP bk_task_up_restore_same reportMeta bkCephUuid imgName hdaVolId'.PHP_EOL;
		echo 'cephIP bk_task_up_restore_new reportMeta bkCephUuid imgName hdaVolId'.PHP_EOL;
		echo 'cephIP vd_userprofile_ss_view imgName ssName'.PHP_EOL;
		echo 'cephIP vd_userprofile_ss_unview imgName(src)'.PHP_EOL;
		echo 'cephIP bk_up_list_diskSize bkCephUuid'.PHP_EOL;
		echo 'cephIP bk_lun_up_del bkCephUuid imgName'.PHP_EOL;
		echo 'cephIP vd_task_gateway_create reportMeta vdName vdAlias [br0|br1..] [br0|br1..] domainId'.PHP_EOL;
		echo 'cephIP vd_nic_replace vdName mac (br0|br1...)'.PHP_EOL;
		echo 'NodeIP nic_vlan_info'.PHP_EOL;
		echo 'NodeIP gw_qemu_ga gaCmd vdName cmd argv1 argv2 ...'.PHP_EOL;
                          echo 'cephIP vd_poweron_prefer_node_set vdName [nodeName|""]'.PHP_EOL;
		echo 'NodeIP nic_vlan_begin_config'.PHP_EOL;
		echo 'NodeIP nic_vlan_create_local vlanId bondIdx static|dhcp ipv4 mask gateway ethCt [0(ethIdx) 1 ...]'.PHP_EOL;
		echo 'NodeIP nic_vlan_end_config'.PHP_EOL;
		echo 'NodeIP nic_vlan_abort_config'.PHP_EOL;
		echo 'cephIP cluster_node_vlan_list_util nodeName'.PHP_EOL;
		echo 'cephIP cluster_node_spice_port_del nodeName portMin portMax'.PHP_EOL;
		echo 'cephIP vd_migrate_status vdName'.PHP_EOL;
		echo 'cephIP cluster_vms_ip_list_from_storage'.PHP_EOL;
		echo 'cephIP cluster_dns_list'.PHP_EOL;
		echo 'cephIP cluster_dns_set dns'.PHP_EOL;
		echo 'cephIP cluster_vms_ip_set vmsIp vmsMask'.PHP_EOL;

		exit(100);
	}
	
	function cmdCephProcess($argv,$isBackground=false,$printOutput=true,$withNewLine=false)
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
	
	function cmdOverArgcProcess($argv,$argc,$printOutput=false,$withNewLine=false)
	{					
		// var_dump(sizeof($argv));
		// var_dump($argc);
		if(sizeof($argv) < $argc){
			exit(99);
		}	
		$this->execSSHCmd($argv,count($argv),$printOutput,NULL,$withNewLine);
	}	

	function cmdOverArgcProcessWithTimeout($argv,$argc,$timeout,$printOutput=false,$withNewLine=false)
	{					
		if(sizeof($argv) < $argc){
			exit(99);
		}	
		$this->execSSHCmd($argv,count($argv),$printOutput,$timeout,$withNewLine);
	}	

	function cmdProcess($argv,$argc,$printOutput=false,$withNewLine=false)
	{			
		// var_dump(sizeof($argv));
		if(sizeof($argv) != $argc){
			exit(99);
		}	
		$this->execSSHCmd($argv,$argc,$printOutput,NULL,$withNewLine);
	}		

	function cmdProcessWithTimeout($argv,$argc,$timeout,$printOutput=false,$withNewLine=false)
	{			
		if(sizeof($argv) != $argc){
			exit(99);
		}	
		$this->execSSHCmd($argv,$argc,$printOutput,$timeout,$withNewLine);
	}

	function cmdQemuGAProcess($argv,$argc,$printOutput=false,$withNewLine=false)
	{			
		// var_dump(sizeof($argv));
		if(sizeof($argv) < $argc){
			exit(99);
		}	
		$this->execQemuGACmd($argv,count($argv),$printOutput,NULL,$withNewLine);
	}	

	function execSSHCmd($argv,$argc,$printOutput,$timeout,$withNewLine)
	{		
		if(isset($timeout))
			$cmd =  str_replace("@1", $timeout, $this->sshCmdCustomTimeout);	
		else
			$cmd = $this->sshCmd;
		$cmd .= $argv[1].' "sudo /var/www/html/script/cp_util.sh ';
		for($i=2;$i<$argc;$i++){
			$cmd .= '\"'.$argv[$i].'\" ';
		}		
		$cmd .= '"';				
		if(DEBUG){
			$fp = fopen('/root/art/cmd_dump.txt', 'a');
	        fwrite($fp, 'PHP '.$cmd.PHP_EOL);	
	        fclose($fp);
	    }
		// var_dump($cmd);
		exec($cmd,$output,$rtn);
		// var_dump($output);
		// var_dump($rtn);
		if($printOutput){
			$outputStr = '';
			foreach ($output as $value) {
				$outputStr.=$value;
				if($withNewLine)
					$outputStr.=PHP_EOL;
			}
			echo $outputStr;
		}
		exit($rtn);
	}

	function execQemuGACmd($argv,$argc,$printOutput,$timeout,$withNewLine)
	{		
		if(isset($timeout))
			$cmd =  str_replace("@1", $timeout, $this->sshCmdCustomTimeout);	
		else
			$cmd = $this->sshCmd;
		$cmd .= $argv[1].' "sudo /var/www/html/script_karl/rdpGW.sh ';
		$jsonArg='';
		for($i=3;$i<$argc;$i++){
			$cmd .= '\"'.$argv[$i].'\" ';
		}			
		$cmd .= '"';				
		if(DEBUG){
			$fp = fopen('/root/art/cmd_dump.txt', 'a');
	        fwrite($fp, 'PHP '.$cmd.PHP_EOL);	
	        fclose($fp);
	    }		
		exec($cmd,$output,$rtn);		
		if($printOutput){
			$outputStr = '';
			foreach ($output as $value) {
				$outputStr.=$value;
				if($withNewLine)
					$outputStr.=PHP_EOL;
			}
			echo $outputStr;
		}
		exit($rtn);
	}

	function execSSHCmdForRemotePHP($argv,$argc,$printOutput,$timeout)
	{		
		if(isset($timeout))
			$cmd =  str_replace("@1", $timeout, $this->sshCmdCustomTimeout);	
		else
			$cmd = $this->sshCmd;
		$cmd .= $argv[1].' "sudo php /var/www/html/shell/CephCmd.php 127.0.0.1 ';
		for($i=2;$i<$argc;$i++){
			$cmd .= '\"'.$argv[$i].'\" ';
		}		
		$cmd .= '"';				
		if(DEBUG){
			$fp = fopen('/root/art/cmd_dump.txt', 'a');
	        fwrite($fp, 'PHP '.$cmd.PHP_EOL);	
	        fclose($fp);
	    }
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