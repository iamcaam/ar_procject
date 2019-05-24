var ActionStatus = {
    GetRelay: 0,
    CreateRelay: 1,
    DeleteRealy: 2,
    ResumeRelay:3,
    AddSource :4,
    ModifyChannel : 5,
    StopRelay : 6,
    StartRelay : 7,
    GetIP:24,
    SetIP:25,
    GetExternal:26,
    SetExternal:27,
    SystemChangePWD:41,
    SystemListVersion:42,
    SystemCheckNewVersion:43,
    SystemUpdate:44,
    SystemCheckUpdate:45,
    SystemListAlarm:46,
    SystemSetAlarm:47,   
    ListLog:51,
    ListTime : 61,
    SetTime : 62,
    UpdateNtp:63,
    ListCache : 71,
    SetCache : 72,
    ListSMB : 81,
    SetSMB : 82,
    SetSSH : 83,
    ListSSH : 84,
    VMInstall : 91,
    VMBackup : 92,
    VMRecovery : 93,
    VMStatus : 94,
    VMList : 95,
    VMShutdown: 96,
    VMClose : 97,
    VMStart:98,
    VMDelete:99,
    VMListImg:100,
    VMCreate:101,
    VMInfo:102,
    VMDeleteNIC:103,
    VMDeleteDisk:104,
    VMModifyInfo:105,
    VMResetPWD:106,
    VMEnable:107,
    VMDisable:108,
    VMTask : 109,
    VMClone:110,
    VMDeleteTask:111,
    VMSuspend:112,
    VMExport:113,
    VMListImport:114,
    VMImport:115,
    ChangeVNCPWD:121,
    iSCSIDiscovery:131,
    iSCSILogin:132,
    iSCSIListSession:133,
    iSCSILogout:134,
    ListDisk : 141,
    ResetDisk : 142,
    ListRAID : 151,
    DeleteRAID : 152,
    CreateRAID : 153,
    RecoveryRAID : 154,
    ListHostName : 171,
    SetHostName : 172,
    ListAD : 173,
    SetAD : 174,
    ListUPS : 175,
    SetUPS : 176    
};
var ErrorHandle = {
    createNew: function() {                        
        var oError = {};
        oError.CheckAuth = function(InputHttpCode,InputAction) {
            var bAuth = false;
            var sError = '';
            switch(InputHttpCode)
            {
                case 503:
                    alert(LanguageStr_Error.Error_DB);
//                    window.location = '/ui/Logout.html';
                    Ajax_Logout();
                    break;
                case 401:
                    Logout_Another_Admin_Login();
                    break;
                case 200:
                    bAuth = true;
                    break;
                case 400:
                    switch(InputAction)
                    {
                        case ActionStatus.SystemChangePWD:
                            alert(LanguageStr_Error.Error_PWD_Modify);
                            break;
                        case ActionStatus.SetTime:
                            alert(LanguageStr_Error.Error_Set_Time);
                            break;
                        case ActionStatus.UpdateNtp:
                            alert(LanguageStr_Error.Error_Time_Update);
                            break;
                        case ActionStatus.ListUPS:
                            alert(LanguageStr_Error.Error_List_UPS);
                            break;
                        case ActionStatus.SetUPS:
                            alert(LanguageStr_Error.Error_Set_UPS);
                            break;                              
                    }
                    break;
                default:
                    sError = oError.AlertErrorByAction(InputAction);
                    alert(sError);                    
//                    window.location = '/ui/Logout.html';
                    Ajax_Logout();
            }
            return bAuth;
        };        
        oError.AlertErrorByAction = function(InputAction){
            var sError = '';
            switch(InputAction){                               
                case ActionStatus.GetIP:
                    sError = LanguageStr_Error.Error_Get_IP;
                    break;
                case ActionStatus.SetIP:
                    sError = LanguageStr_Error.Error_Set_IP;
                    break;
                case ActionStatus.SystemListVersion:
                    sError = LanguageStr_Error.Error_List_Version;
                    break;
                case ActionStatus.SystemCheckNewVersion:
                    sError = LanguageStr_Error.Error_Check_Version;
                    break;
                case ActionStatus.SystemCheckUpdate:
                    sError = LanguageStr_Error.Error_Check_Update;
                    break;
                case ActionStatus.ListLog:
                    sError = LanguageStr_Error.Error_List_log;
                    break;
                case ActionStatus.ListTime:
                    sError = LanguageStr_Error.Error_List_Time;
                    break;
                case ActionStatus.SetTime:
                    sError = LanguageStr_Error.Error_Set_Time;
                    break;  
                case ActionStatus.ListSMB:
                    sError = LanguageStr_Error.Error_List_SMB;
                    break;
                case ActionStatus.SetSMB:
                    sError = LanguageStr_Error.Error_Set_SMB;
                    break;
                case ActionStatus.ListSSH:
                    sError = LanguageStr_Error.Error_List_SSH;
                    break;
                case ActionStatus.SetSSH:
                    sError = LanguageStr_Error.Error_Set_SSH;
                    break;
                case ActionStatus.VMInstall:
                    sError = "Error : Failed to Install " + VMorVDI + " since conneciton timeout.";
                    break;
                case ActionStatus.VMBackup:
                    sError = "Error : Failed to Backup " + VMorVDI + " since conneciton timeout.";
                    break;
                case ActionStatus.VMRecovery:
                    sError = "Error : Failed to Recovery " + VMorVDI + " since conneciton timeout.";
                    break;
                case ActionStatus.VMStatus:
                    sError = LanguageStr_Error.Error_VM_Status.replace("@",VMorVDI);
                    break;
                case ActionStatus.VMList:
                    sError = LanguageStr_Error.Error_List_VM.replace("@",VMorVDI);
                    break;
                case ActionStatus.VMShutdown:
                    sError = LanguageStr_Error.Error_VM_Shutdown.replace("@",VMorVDI);
                    break;
                case ActionStatus.VMClose:
                    sError = LanguageStr_Error.Error_VM_Poweroff.replace("@",VMorVDI);
                    break;
                case ActionStatus.VMStart:
                    sError = LanguageStr_Error.Error_VM_Poweron.replace("@",VMorVDI);
                    break;
                case ActionStatus.VMDelete:
                    sError = LanguageStr_Error.Error_VM_Delete.replace("@",VMorVDI);
                    break;
                case ActionStatus.VMListImg:
                    sError = LanguageStr_Error.Error_VM_List_Img;
                    break;
                case ActionStatus.VMCreate:
                    sError = LanguageStr_Error.Error_VM_Create.replace("@",VMorVDI);
                    break;
                case ActionStatus.VMInfo:
                    sError = LanguageStr_Error.Error_VM_List_Info.replace("@",VMorVDI);
                    break;
                case ActionStatus.VMDeleteDisk:
                    sError = LanguageStr_Error.Error_VM_Delete_Disk.replace("@",VMorVDI);
                    break;      
                case ActionStatus.VMModifyInfo:
                    sError = LanguageStr_Error.Error_VM_Modify_Properties.replace("@",VMorVDI);
                    break;
                case ActionStatus.VMResetPWD:
                    sError = LanguageStr_Error.Error_VM_ResetPWD.replace("@",VMorVDI);
                    break;
                case ActionStatus.VMEnable:
                    sError = LanguageStr_Error.Error_VM_Enable.replace("@",VMorVDI);
                    break;
                case ActionStatus.VMDisable:
                    sError = LanguageStr_Error.Error_VM_Disable.replace("@",VMorVDI);
                    break;
                case ActionStatus.VMTask:
                    sError = LanguageStr_Error.Error_VM_List_Task.replace("@",VMorVDI);
                    break;
                case ActionStatus.VMSuspend:
                    sError = LanguageStr_Error.Error_VM_Suspend.replace("@",VMorVDI);
                    break;
                case ActionStatus.VMClone:
                    sError = LanguageStr_Error.Error_VM_Clone.replace("@",VMorVDI);
                    break;
                case ActionStatus.VMDeleteTask:
                    sError = LanguageStr_Error.Error_VM_Delete_Task.replace("@",VMorVDI);
                    break;
                case ActionStatus.ChangeVNCPWD:
                    sError = LanguageStr_Error.Error_SMB_Change_PWD;
                    break;
                case ActionStatus.iSCSIDiscovery:
                    sError = LanguageStr_Error.Error_iSCSI_Discovery;
                    break;
                case ActionStatus.iSCSILogin:
                    sError = LanguageStr_Error.Error_iSCSI_Login;
                    break;
                case ActionStatus.iSCSIListSession:
                    sError = LanguageStr_Error.Error_iSCSI_List;
                    break;
                case ActionStatus.iSCSILogout:
                    sError = LanguageStr_Error.Error_iSCSI_Logout;
                    break;
                case ActionStatus.ListDisk:
                    sError = LanguageStr_Error.Error_List_Disk;                    
                    break;
                case ActionStatus.ResetDisk:
                    sError = LanguageStr_Error.Error_Reset_Disk;
                    break;
                case ActionStatus.DeleteRAID:
                    sError = LanguageStr_Error.Error_Delete_RAID;
                    break;
                case ActionStatus.CreateRAID:
                    sError = LanguageStr_Error.Error_Create_RAID;
                    break;
                case ActionStatus.RecoveryRAID:
                    sError = LanguageStr_Error.Error_Recovery_RAID;
                    break;
                case ActionStatus.ListHostName:
                    sError = LanguageStr_Error.Error_List_Hostname;
                    break;
                case ActionStatus.SetHostName:
                    sError = LanguageStr_Error.Error_Set_Hostname;
                    break;
                case ActionStatus.GetExternal:
                    sError = LanguageStr_Error.Error_List_ExternalIP;
                    break;
                case ActionStatus.SetExternal:
                    sError = LanguageStr_Error.Error_Set_ExternalIP;
                    break;
                case ActionStatus.SystemListAlarm:
                    sError = LanguageStr_Error.Error_List_Alarm;
                    break;
                case ActionStatus.SystemSetAlarm:
                    sError = LanguageStr_Error.Error_Set_Alarm;
                    break;
                case ActionStatus.VMExport:
                    sError = LanguageStr_Error.Error_VM_Export.replace("@",VMorVDI);
                    break;
                case ActionStatus.VMImport:
                    sError = LanguageStr_Error.Error_VM_Import.replace("@",VMorVDI);
                    break
                case ActionStatus.VMListImport:
                    sError = LanguageStr_Error.Error_VM_List_Import.replace("@",VMorVDI);
                    break;
                case ActionStatus.SetAD:
                    sError = LanguageStr_Error.Error_Set_AD;
                    break;
                case ActionStatus.ListAD:
                    sError = LanguageStr_Error.Error_List_AD;
                    break;                                      
            }
            return sError;
        };        
        return oError;
    }
};


