var LanguageStr_VM =
{
    VM_Delete : "Do you want to execute Delete@ now?",    
    VM_Export : "Files of& will be exported, and put in \"Samba's EXPORT Directory\"。\r\n\r\nDo you want to Export@ now?",
    VM_Import_Ask : "Do you want to Import@ now?",
    VM_Str_Import : "Import",
    VM_Delete_VD_First : 'Please delete all User\'s@ of & and then delete.',
    VM_Delete_Shutdown_First : 'Please shutdown/poweroff & and then delete.',
    VM_Disable_Shutdown_First : 'Please shutdown/poweroff & and then disable.',
    VM_Poweron_ask : 'Do you want to execute Poweron@ now?',
    VM_Shutdown_ask : 'Do you want to execute Shutdown@ now?',
    VM_Poweroff_ask : 'Do you want to execute Poweroff@ now?',
    VM_Enable_ask : 'Do you want to execute Enable@ now?',
    VM_Disable_ask : 'Do you want to execute Disable@ now?',
    VM_ResetPWD_ask : "Do you want to reset@'s" + ' password to "000000" now?',
    VM_Destination_Warning : "Please input value in Destination Name.",
    VM_Clone_Name_Warning : 'You can only input "A-Z 0-9 a-z _" in VD Name(&).',
    VM_Clone_Name_Warning_1 : "You can't input integer in VD Name(&).",
    VM_Clone_Name_Warning_2 : "VD Name(&) is duplicated, please input again.",
    VM_Name_Duplicate : "Fail to Create(VD Name is duplicated).",
    VM_Name_Empty_Warning : "Please input value in VD Name.",
    VM_Memory_Integer_Warning : 'Please input integer or integer and two decimal in Memory.',
    VM_NewDisk_Integer_Warning : 'Please input integer in Create New Disk.',
    VM_Create_Fail : 'Fail to Create@.',
    VM_Import_Fail : 'Fail to Import@',
    VM_Export_Fail : 'Fail to Import@(&)',
    VM_Export_Success : "Files of@ are Exported Successful！\r\n\r\nPlease use PC connect to VDI Server(IP:&)'s samba， and use \"manger\" to login， copy files from \"manager/EXPORT Directory\".",
    VM_Import_No_VD : 'No Importable@',
    VM_Clone : 'Clone@',
    VM_Clone_UserVD : 'Clone@',        
    VM_Clone_UserVD_Failure : 'Fail to Clone@.',
    VM_Make_Seed : 'Make Seed',
    VM_Delete_Borned_Warning : "Please delete &'s all User's@ at first.",
    VM_Delete_Seed_ask : "Do you want to delete Seed now?",
    VM_Delete_NIC_ask : "Do you want to delete this nic now?",
    VM_Delete_Txt : "Delete",
    VM_Boot_Disk : "Boot Disk :",
    VM_Data_Disk : "Data Disk :",
    VM_Setting : "Setting",
    VM_Task_Clone : "Clone",
    VM_Task_Make_Seed : "Make Seed",
    VM_Task_Create_Original : 'Create Virtual Host',
    VM_Born_Users : "Born User",
    VM_Task_Add_Disk : "Add Disk",
    VM_Task_Del_Disk : "Delete Disk",
    VM_Task_Export_VD : "Export VD",
    VM_Task_Import_VD : "Import VD",
    VM_Task_Overwrite_Seed : "Overwrite Seed",
    VM_Task_Reborn_User : "Reborn User VD",
    VM_Task_Renew_User : "Renew User VD",
    VM_Task_Recovery_User : "Recovery User VD",
    VM_Task_Clone_to_Org : "Clone to Virtual Host",
    VM_Task_Failed : "Failed",
    VM_Task_Completed : "Completed",
    VM_Task_Waiting : "Waiting",
    VM_Task_Cloning : "Processing",
    VM_Enable : "Enable",
    VM_Disable : "Disable",
    VM_Stop : "Stop",
    VM_Waiting_Poweron : "Waiting Poweron",
    VM_Poweron : "Poweron",
    VM_Blocked : "Blocked",
    VM_Pause : "Suspend",
    VM_Shutdowning : "Shutdowning",
    VM_Shutdown : "Shutdown",
    VM_Crash : "Crash",
    VM_Suspend : "Suspend",
    VM_Disconnected : "Disconnected",
    VM_Connected : "Connected",
    VM_Modify_Name_Fail : "Fail to modify VD",
    VM_Modify : "Modify",
    VM_Seed : "Seed",
    VM_Information : "Information",
    VM_Create_Original : 'Create "Original"',
    VM_Import_Original : 'Import "Original"',
    VM_Limited : 'Limited',
    VM_UnLimited : 'Unlimited',
    /* SnapShot 2017.02.06 william */ 
    VM_Snapshot : 'Snapshot',
    VM_SSIsview_ON : 'Yes', 
    VM_SSIsview_OFF : 'No',
    VM_SnapshotType_take : 'Take Snapshot',
    VM_SnapshotType_delete : 'Delete Snapshot',
    VM_SnapshotType_rollback : 'Rollback Snapshot',
    VM_SnapshotType_view : 'View Snapshot',
    VM_SnapshotType_view_stop : 'View_stop Snapshot',
    VM_SnapshotType_Schedule_Take: 'Schedule Snapshot',

    VM_SnapshotState_Normal : 'Ready',
    VM_SnapshotState_Preparing_view : 'View preparing',
    VM_SnapshotState_View : 'Viewing',
    VM_SnapshotState_Preparing_delete : 'Delete preparing',
    VM_SnapshotState_Preparing_rollback : 'Rollback preparing',
    VM_SnapshotState_Preparing_unview : 'Unview preparing',
    // 新增CPU類型  2017.09.01 william
    VM_CPUType_fullvirtual : 'Virtual CPU',
    VM_CPUType_Halfvirtual : 'Physical CPU',
    VM_SnapShotLayer_Empty_Warning : "Please input value in SnapShot Layer.",
    VM_SnapShotLayer_Error1:"The number of SnapShot Layer can't below 30.",
    VM_SnapShotLayer_Error2:"The number of SnapShot Layer can't above 1000.",
    VM_SnapShotLayer_Recommendation:"The maximum number of Suggested snapshot layers : 500",   
    /* 2018.01.08 william 新增磁碟擴充功能，建立擴充視窗 */ 
    VM_Expend_Disk_Txt : "Expend",
    VM_ExpendDisk_title : "Expend the disk",    
    VM_Disktype_warning : "The same disk type can not exceed 2, Please select a different disk type.",
    VM_UserList_title : "User List",
    VM_SeedList_title : "Seed List",
    Expand_Disk : "Expand Disk",
    Move_Disk : "Move Disk",
    Move_User_Profile : "Move User Profile",
    Move : "Move",        
    Moving : "Moving",
    User_Name_Warning : 'You can only input "A-Z 0-9 a-z _" in Username(&).',
    User_Name_Warning_1 : "You can't input integer in Username(&).",
    User_Name_Warning_2 : "Username(&) is duplicated, please input again.",
    User_Name_Duplicate : "Fail to Create(Username is duplicated).",
    User_Name_Empty_Warning : "Please input value in Username.",    
    Virtual_Host_Name_Str : "Virtual Host Name :"
}