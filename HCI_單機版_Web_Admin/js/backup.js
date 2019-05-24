var PollingBackupListTaskFlag;
var PollingBackupListFlag;
var isRunningPollingBackupTask;   // Backup task list 實作 2017.03.01 william
var isRunningPollingBackupList;   // Backup list 實作 2017.03.01 william
var BackupTaskStart;
var BackupListStart; // Backup list 實作 2017.02.09 william

var taskpollingcount;

var NowSortBackupTaskType;
var NowSortBackupTaskDirection;
var NowSortBackupTaskDOMID;

var BackupMgtEnableFlag;

var BackupMgtTotalCapacity;
var BackupMgtAvailCapacity;
var BackupMgUsageCapacity;

var GetBackupID = [];
var RAIDRestore = [];
var BackupIDArray = [];
var directRestoreArray = [];
var waitRestoreArray = [];
var SelectRAIDHtmlStr;    
var locationhrefFlag = false;
var tempbkid = '';
var tempraidid = '';
var WaitRAIDRestore = [];
var rename_bkid;
var Get_BkType = 0;

function InitBackupSetting() {
    $('.iCheckRadioStorage').iCheck({  
        radioClass: 'iradio_flat-blue'    
    });
                    
    $('.iCheckRadioStorage').on('ifChecked', function(event){
        ChangeStorageDivPage($(this).val());
    });       
    taskpollingcount = 0;
    PollingBackupListTaskFlag = true;
    PollingBackupListFlag = true;
    BackupTaskStart = false;
    BackupListStart = false;
    isRunningPollingBackupTask = false;
    isRunningPollingBackupList = false;
    NowSortBackupTaskType = null;

    // GetBackupForCreateTableList();

    InitBackupMgtSelectAllCheckBox();
    EnableDisableSelectBackupMgtActionButton();
    Dialog_RestoreSelectRAID();
    $('#bkrename__input').on("input", function () {        
        if($('#bkrename__input').val().length ==0 || $('#conflictSource').text() === $('#bkrename__input').val())                      
            $('#BKRename_Bnt_Confirm').button( "disable" );
        else
            $('#BKRename_Bnt_Confirm').button( "enable" );        
    });  
    
    var tra,val
    val="//IP/Folder Path";
    // $("#backup_nasurl").css("color","#000");
    $("#backup_nasurl").val(val);
    if($("#backup_nasurl").length==0) {
         $("#backup_nasurl").val(val);
    }

    $("#backup_nasurl").focus(function() {
        $("#backup_nasurl").val("");	
        $("#backup_nasurl").removeAttr("style");
        $("#backup_nasurl").val(tra);
    });
    $("#backup_nasurl").blur(function() {
        tra=$("#backup_nasurl").val();
        if($("#backup_nasurl").val()==""){
            $("#backup_nasurl").val(val);
            $("#backup_nasurl").css("color","#737373");
        }
    });         
};

function ChangeStorageDivPage(check_val) {                       
    switch(check_val) {
        case 'iSCSI':
            $('#Div_iSCSI_page').show();
            $('#Div_Nas_page').hide();
            break;
        case 'nas':
            $('#Div_Nas_page').show();
            $('#Div_iSCSI_page').hide();
            break;
    }
}  

function BackupListEnable() {
    BackupTaskStart = true; // 快照任務清單 新增內容 2017.01.25 
    BackupListStart = true; // SnapShot list 實作 2017.02.09 william
    VMListBackupTask(true);
};

/**************************************************************************/
/*                                                                        */
/*                               Dialog畫面                               */
/*                                                                        */
/**************************************************************************/
/*=========================== LUN連線 Start ===========================*/
function CreateLUNConnection() {
    $("#dialog_bkConnectSetting").dialog('open');
}

function CreateDialog_CreateLUNConnection() {
    $("#dialog_bkConnectSetting").dialog({
        title: LanguageStr_Service.Service_LUN_Connection,
        modal: true,
        resizable: false,
        draggable: true,
        closeOnEscape: false,
        autoOpen: false,
        height: 250,
        width: 800,
    });
    $('#Backup_Format_btn').click(function (event) {
        event.preventDefault();
        $("#dialog_bkConnectSetting").dialog('close');
    });
    $('#Backup_Use_btn').click(function (event) {
        event.preventDefault();
        $("#dialog_bkConnectSetting").dialog('close');
    });
}
/*=========================== LUN連線 End ===========================*/
/*=========================== 重新命名 Start ===========================*/
function CreateBackupVDRename(bkid) {
    $("#dialog_bkvdrename").dialog('open');
    rename_bkid = bkid;
    $("#bkrename__input").val('');       
    $("#conflictSource").text(RAIDRestore[rename_bkid].vdname);
    $('#BKRename_Bnt_Confirm').button( "disable" );
}

function CreateDialog_BackupVDRename() {
    $("#dialog_bkvdrename").dialog({
        title: LanguageStr_Service.BackupVD_Rename,
        modal: true,
        resizable: false,
        draggable: true,
        closeOnEscape: false,
        autoOpen: false,
        height: 433,
        width: 800,
    });
    $('#BKRename_Bnt_Confirm').click(function (event) {
        event.preventDefault();
        $("#dialog_bkvdrename").dialog('close');
        var name = $("#bkrename__input").val();
        // var res = idarr[0].substr(0, 1);
        // var index = idarr[0].substr(1);

        // var sourcevdid = OrgMultiBackupMgtListBk[index].vdid;
        // var backupvdid = OrgMultiBackupMgtListBk[index].bkvdid;
        // var backupvdname = OrgMultiBackupMgtListBk[index].bkvdname;
        var sourcevdid = RAIDRestore[rename_bkid].sourcevdid;
        var backupvdid = RAIDRestore[rename_bkid].backupvdid;
        var backupvdname = RAIDRestore[rename_bkid].backupvdname;
        var raidid = WaitRAIDRestore[rename_bkid].raidid;   
        var request = Ajax_VDRestore(name, name, sourcevdid, backupvdid, backupvdname, raidid);
        CallBackRestoreVDarr(request);
        $("#bkrename__input").val('');
        WaitRestoreVDarr();
    });
}
/*=========================== 重新命名 End ===========================*/
/*=========================== LUN尚未連線 Start ===========================*/
function CreateStorageConnCheck(BackupMgtEnableFlag) {
    if (!BackupMgtEnableFlag) {
        $("#dialog_bkStorageConnCheck").dialog('open');
    }
}

function CreateDialog_StorageConnCheck() {
    $("#dialog_bkStorageConnCheck").dialog({
        title: LanguageStr_Service.BackupLUN_OfflineMessage_Warning,
        modal: true,
        resizable: false,
        draggable: true,
        closeOnEscape: false,
        autoOpen: false,
        height: 180,
        width: 500,
        dialogClass: "no-close",
    });
    $('#BKredirect_Bnt_Confirm').click(function (event) {
        event.preventDefault();
        $("a[href$='#tabs1-Backup_Setting']").click();
        $("#dialog_bkStorageConnCheck").dialog('close');
    });
}
/*=========================== LUN尚未連線 End ===========================*/

/**************************************************************************/
/*                                                                        */
/*                               產生排程相關畫面                           */
/*                                                                        */
/**************************************************************************/
/*=================== 備份 (1)檢查LUN  2017.02.21 Start ===================*/
function InitCheckLUNButton() {
    $('#btn_checkLUN_Connect').click(function (event) {
        event.preventDefault();
        var ip = $('#backup_iscsiip').val();
        var username = $('#backup_usename').val();
        var password = $('#backup_password').val();
        var targetname = $('#backup_targetname').val();
        Get_BkType = 1;
        CheckBackupLUN(Get_BkType, ip, username, password, targetname);
    });

    $('#btn_nas_Connect').click(function (event) {
        event.preventDefault();
        var nasurl = $('#backup_nasurl').val();
        var username = $('#backup_nas_usename').val();
        var password = $('#backup_nas_password').val();
        var port = $('#nas_port_input').val();
        Get_BkType = 2;
        CheckBackupLUN(Get_BkType, nasurl, username, password, port);
    });    
}

function CheckBackupLUN(type, ip_or_url, username, password, targetname_or_port) {
    blockPage();
    var request = Ajax_CheckBackupLUN(type, ip_or_url, username, password, targetname_or_port);
    CallBackCheckBackupLUN(request, type, ip_or_url, username, password, targetname_or_port);
};

function CallBackCheckBackupLUN(request, type, ip_or_url, username, password, targetname_or_port) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        stopPage();
        var checkLUNstate = msg['State'];
        var checkLUNdesc = msg['Desc'];
        CheckBackupState(checkLUNstate, checkLUNdesc, ip_or_url, username, password, targetname_or_port, type);
    });
    request.fail(function (jqxhr, textStatus) {
        stopPage();
        alert(LanguageStr_Service.CheckBackupLUN_failure);
    });
}
/*=================== 備份 (1)檢查LUN  2017.02.21 End ===================*/
/*=================== 備份 (2)List  2017.02.21 Start ===================*/
function GetBackupForCreateTableList() {
    var request = Ajax_ListBackup();
    request.done(function (AjaxData, statustext, jqXHR) {
        ListBackupToUI(AjaxData);
        GetBackupMgtVDs();
    });
    request.fail(function (jqXHR, textStatus) {
    });
}
// 定義class承接json資料
function ListBackupToUI(JsonData) {
    var HtmlStr = "";
    var bk;
    // var tmpRestoreVDs;
    BackupList = [];
    // OrgRestoreVDList = [];

    //  $.each( JsonData, function(index, element){   
    bk = new ListBackupClass(JsonData['CHAP'], JsonData['TargetName'], JsonData['Username'], JsonData['Password'], JsonData['Desc'], JsonData['BkUUID'], JsonData['IP'], JsonData['ID'], JsonData['TotalCapacity'], JsonData['AvialCapacity'], JsonData['Online']);
    // $.each(JsonData['RestoreVDs'].Original, function (rvdindex, rvdelement) {
    //     tmpRestoreVDs = new RestoreVDsClass(rvdelement['SourceVDName'], rvdelement['SourceVDID'], rvdelement['BackupVDName'], rvdelement['BackupVDID'], rvdelement['BackupTime']);
    //     OrgRestoreVDList[rvdelement['BackupVDID']] = tmpRestoreVDs;
    // });
    BackupList.push(bk);
    //    });    
    if(JsonData['Type']==1)
        CreateLoginBackupHtml(JsonData['Type'], JsonData['CHAP'], JsonData['IP'], JsonData['TargetName'], JsonData['Username'], JsonData['Password'], JsonData['Desc'], JsonData['Online']);
    else if(JsonData['Type']==2)
        CreateLoginBackupHtml(JsonData['Type'], '', JsonData['MountPath'], JsonData['Port'], JsonData['Username'], JsonData['Password'], JsonData['Desc'], JsonData['Online']);
    else if(JsonData['Type']==0)
        CreateLoginBackupHtml(JsonData['Type'], '', '', '', '', '', '', JsonData['Online']);        
    // CreateListBackupHtml();
    // BindDropDownBtn();
    // EnableDisableSelectScheduleActionButton();
    BackupMgtTotalCapacity = JsonData['TotalCapacity'];
    BackupMgtAvailCapacity = JsonData['AvialCapacity'];
}

function CreateLoginBackupHtml(type,chap, ip_or_url, targetname_or_port, username, password, desc, online) {
    $('#Backup_Use_btn').button("disable");
    $('#Backup_Format_btn').button("disable");
    
    if (type == 1) {
        $('#radio-iSCSI').iCheck('check'); 
        $('.iCheckRadioStorage').iCheck('disable');
        Get_BkType = 1;
        if (chap == true)
            $('#chk_bkchap').attr("src", "img/checkbox.png");
        else
            $('#chk_bkchap').attr("src", "img/checkbox_empty.png");

        $('#backup_iscsiip').val(ip_or_url);
        $('#backup_targetname').val(targetname_or_port);
        $('#backup_usename').val(username);
        $('#backup_password').val(password);
        $('#checkLUNdesc_text').text(desc);
        $('input[id="backup_iscsiip"]').prop("disabled", true);
        $('input[id="backup_targetname"]').prop("disabled", true);
        $('input[id="backup_usename"]').prop("disabled", true);
        $('input[id="backup_password"]').prop("disabled", true);        
        $('#btn_checkLUN_Connect').hide();
        $('#Backup_Disconnect_btn').show();
        
        if (online == true) {
            $('#LUNOnlineCheck').text(LanguageStr_Service.BackupLUN_OnlineCheck);
            $('#clearallbk').button("enable");
            BackupMgtEnableFlag = true;            
        } else {
            $('#LUNOnlineCheck').text(LanguageStr_Service.BackupLUN_OfflineCheck); 
            $('#clearallbk').button("disable"); 
            BackupMgtEnableFlag = false;
            /*
            if ($('#backup_iscsiip').val().length !== 0 && $('#backup_targetname').val().length !== 0 && $('#backup_usename').val().length !== 0 && $('#backup_password').val().length !== 0) {
                var request = Ajax_DeleteBackup();
                CallBackDeleteBackup(request);
            }               
            */
        // $('#tab-Backup-container li:nth-child(n+2):nth-child(-n+4)').addClass('tabdisabled');
        // $('#tab-Backup-container li:nth-child(n+2):nth-child(-n+4) a').addClass('disable_etabs_underline');
        }

    // if ($('#backup_iscsiip').val().length !== 0 || $('#backup_targetname').val().length !== 0 || $('#backup_usename').val().length !== 0 || $('#backup_password').val().length !== 0 || $('#Backup_Desc').val().length !== 0) {}
    // else {}        
    } else if (type == 2) {
        $('#radio-nas').iCheck('check'); 
        $('.iCheckRadioStorage').iCheck('disable');        
        Get_BkType = 2;
        $('#backup_nasurl').val(ip_or_url);
        $('#nas_port_input').val(targetname_or_port);
        $('#backup_nas_usename').val(username);
        $('#backup_nas_password').val(password);
        $('#checkLUNdesc_nas_text').text(desc);             
        $('input[id="backup_nasurl"]').prop("disabled", true);
        $('input[id="nas_port_input"]').prop("disabled", true);
        $('input[id="backup_nas_usename"]').prop("disabled", true);
        $('input[id="backup_nas_password"]').prop("disabled", true);   
        $('#btn_nas_Connect').hide();
        $('#btn_nas_Disconnect').show();      
        $("#backup_nasurl").css("color","#000");
        if (online == true) {                  
            $('#nas_status').text(LanguageStr_Service.BackupLUN_OnlineCheck);   
            $('#clearallbkfornas').button("enable");       
            BackupMgtEnableFlag = true;    
        } else {
            $('#nas_status').text(LanguageStr_Service.BackupLUN_OfflineCheck);
            $('#clearallbkfornas').button("disable");      
            BackupMgtEnableFlag = false;                   
        }       
        
    } else if (type == 0) {
        BackupMgtEnableFlag = false;
        $('#radio-iSCSI').iCheck('check');
        $('.iCheckRadioStorage').iCheck('enable');       
        iSCSIOfflineNA();
        NASOfflineNA();
        $("#backup_nasurl").css("color","#737373");
    }
}

function iSCSIOfflineNA() {
    $('#LUNOnlineCheck').text('N/A');
    $('#Backup_Use_btn').button("option", "disabled", false);
    $('#Backup_Format_btn').button("option", "disabled", false);    
    $('input[id="backup_iscsiip"]').prop("disabled", false);
    $('input[id="backup_targetname"]').prop("disabled", false);  
    $('input[id="backup_usename"]').prop("disabled", true);
    $('input[id="backup_password"]').prop("disabled", true);               
    $('#Backup_Disconnect_btn').hide();
    $('#btn_checkLUN_Connect').button("disable").show();
    $('#clearallbk').button("disable");     
}

function NASOfflineNA() {
    $('#nas_status').text('N/A');
    $('#Backup_Use_btn').button("option", "disabled", false);
    $('#Backup_Format_btn').button("option", "disabled", false);  
    $('input[id="backup_nasurl"]').prop("disabled", false);
    $('input[id="nas_port_input"]').prop("disabled", false);
    $('input[id="backup_nas_usename"]').prop("disabled", false);
    $('input[id="backup_nas_password"]').prop("disabled", false);               
    $('#btn_nas_Disconnect').hide();
    $('#btn_nas_Connect').button("disable").show();
    $('#clearallbkfornas').button("disable");      
}

/*=================== 備份 (2)List  2017.02.21 End ===================*/
// ----------- 備份 (3)Seting Mapping  2017.02.22 Start ----------- 
function InitBackupSettingButton() {
    $('#Backup_Format_btn').click(function (event) {
        event.preventDefault();
        var type = Get_BkType; // iscsi = 1, nas = 2
        
        if(type==1) {
            var ip = $('#backup_iscsiip').val();
            var username = $('#backup_usename').val();
            var password = $('#backup_password').val();
            var targetname = $('#backup_targetname').val();        
        } else if(type==2) {
            var nasurl = $('#backup_nasurl').val();
            var username = $('#backup_nas_usename').val();
            var password = $('#backup_nas_password').val();
            var port = $('#nas_port_input').val();
        }
        var desc = $('#Backup_Desc').val();
        var action = 0;
        if (desc.length === 0) {
            alert(LanguageStr_Service.Service_BackupDesc_Empty_Warning);
            return false;
        }
        var format_ask = LanguageStr_Service.Backup_FormatAsk;
        var confirmable = confirm(format_ask);
        if (confirmable === true) {
            if(type==1) {               
                BackupFormat(type, ip, username, password, targetname, desc, action);
            } else if(type==2) {
                BackupFormat(type, nasurl, username, password, port, desc, action);
            }                 
        }

    });
    $('#Backup_Use_btn').click(function (event) {
        event.preventDefault();
        var type = Get_BkType; // iscsi = 1, nas = 2
        if(type==1) {
            var ip = $('#backup_iscsiip').val();
            var username = $('#backup_usename').val();
            var password = $('#backup_password').val();
            var targetname = $('#backup_targetname').val();                    
        } else if(type==2) {
            var nasurl = $('#backup_nasurl').val();
            var username = $('#backup_nas_usename').val();
            var password = $('#backup_nas_password').val();
            var port = $('#nas_port_input').val();
        }
        var desc = $('#Backup_Desc').val();
        var action = 1;
        if (desc.length === 0) {
            alert(LanguageStr_Service.Service_BackupDesc_Empty_Warning);
            return false;
        }
        if(type==1) {       
            BackupUse(type, ip, username, password, targetname, desc, action);
        } else if(type==2) {
            BackupUse(type, nasurl, username, password, port, desc, action);
        } 
    });
    $('#Backup_Disconnect_btn,#btn_nas_Disconnect').click(function (event) {
        event.preventDefault();
        var request = Ajax_DeleteBackup();
        CallBackDeleteBackup(request);
    });
    $('#clearallbk,#clearallbkfornas').click(function (event) {
        event.preventDefault();
        var clear_ask = LanguageStr_Service.Backup_ClearAllAsk;
        var confirmable = confirm(clear_ask);
        if (confirmable === true) {
            blockPage();
            var request = Ajax_FormatBackup();
            CallBackClearAllBackup(request);
        }
    });
    $("a[href$='#tabs1-Backup_Manager'], a[href$='#tabs1-Backup_schedule']").click(function () {
        CreateStorageConnCheck(BackupMgtEnableFlag);
    });
}

function BackupFormat(type, ip_or_url, username, password, targetname_or_port, desc, action) {
    blockPage();
    var request = Ajax_SetBackupMapping(type, ip_or_url, username, password, targetname_or_port, desc, action);
    CallBackBackupFormat(request, type);
};

function CallBackBackupFormat(request, type) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        stopPage();
        BackupMgtEnableFlag = true;        
        if(type==1) {        
            iSCSi_Use_Format_Setting();
        } else if(type==2) {
            nas_Use_Format_Setting();
        }        
    });
    request.fail(function (jqxhr, textStatus) {
        stopPage();
        // alert(LanguageStr_langSysCfgUserMgt.CreateRAID_alert);
    });
}

function BackupUse(type, ip_or_url, username, password, targetname_or_port, desc, action) {
    blockPage();
    var request = Ajax_SetBackupMapping(type, ip_or_url, username, password, targetname_or_port, desc, action);
    CallBackBackupUse(request, type);
};

function CallBackBackupUse(request, type) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        stopPage();
        BackupMgtEnableFlag = true;        
        if(type==1) {
            iSCSi_Use_Format_Setting();
        } else if(type==2) {
            nas_Use_Format_Setting();
        }

    });
    request.fail(function (jqxhr, textStatus) {
        stopPage();
        // alert(LanguageStr_langSysCfgUserMgt.CreateRAID_alert);
    });
}

function iSCSi_Use_Format_Setting() {  
    GetBackupForCreateTableList();
    $('#Backup_Desc').val('');
    $('#checkLUNstate_text').text('');
    $('#LUNOnlineCheck').text(LanguageStr_Service.BackupLUN_OnlineCheck);
    $('#btn_checkLUN_Connect').hide();
    $('#Backup_Disconnect_btn').show();
    $('#clearallbk').button("enable");
    // $('#tab-Backup-container li:nth-child(n+2):nth-child(-n+4)').removeClass('tabdisabled');
    // $('#tab-Backup-container li:nth-child(n+2):nth-child(-n+4) a').removeClass('disable_etabs_underline');
}

function nas_Use_Format_Setting() {  
    GetBackupForCreateTableList();
    $('#checkLUNdesc_nas_text').val('');
    $('#checkLUNstate_text').text('');
    $('#nas_status').text(LanguageStr_Service.BackupLUN_OnlineCheck);
    $('#btn_nas_Connect').hide();
    $('#btn_nas_Disconnect').show();
    $('#clearallbkfornas').button("enable");
}

function CallBackDeleteBackup(request) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        // GetBackupForCreateTableList();
        $('#btn_checkLUN_Connect').button("option", "disabled", false);
        $('#Backup_Use_btn').button("option", "disabled", false);
        $('#Backup_Format_btn').button("option", "disabled", false);
        $('#Backup_Desc').val('');
        $('#checkLUNstate_text').text('');        
        // $('#Backup_Disconnect_btn').button("option", "disabled", true);
        BackupMgtEnableFlag = false;
        DeleteBackupForiSCSIStatus();
        DeleteBackupForNASStatus();
        if(Get_BkType==2) {
            $('input[id="backup_usename"]').prop("disabled", true);
            $('input[id="backup_password"]').prop("disabled", true);              
        }
        // $('#tab-Backup-container li:nth-child(n+2):nth-child(-n+4)').addClass('tabdisabled');
        // $('#tab-Backup-container li:nth-child(n+2):nth-child(-n+4) a').addClass('disable_etabs_underline');
        $('#BackupMgt_Content_body_backup').empty();
        $('.iCheckRadioStorage').iCheck('enable');
        alert(LanguageStr_Service.BackupLUN_disconnect);
    });
    request.fail(function (jqxhr, textStatus) {

    });
}

function DeleteBackupForiSCSIStatus() {
    $('input[id="backup_iscsiip"]').prop("disabled", false);
    $('input[id="backup_targetname"]').prop("disabled", false);
    $('input[id="backup_usename"]').prop("disabled", false);
    $('input[id="backup_password"]').prop("disabled", false);   
    $('#checkLUNdesc_text').text('');
    $('#backup_password').val('');     
    $('#LUNOnlineCheck').text('N/A');
    $('#Backup_Disconnect_btn').hide();
    $('#btn_checkLUN_Connect').button("disable").show();
    $('#clearallbk').button("disable");    
}

function DeleteBackupForNASStatus() {
    $('input[id="backup_nasurl"]').prop("disabled", false);
    $('input[id="nas_port_input"]').prop("disabled", false);
    $('input[id="backup_nas_usename"]').prop("disabled", false);
    $('input[id="backup_nas_password"]').prop("disabled", false);       
    $('#checkLUNdesc_nas_text').text('');
    $('#backup_nas_password').val('');    
    $('#nas_status').text('N/A');      
    $('#btn_nas_Disconnect').hide();
    $('#btn_nas_Connect').button("disable").show();
    $('#clearallbkfornas').button("disable");      
}

function CallBackClearAllBackup(request) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        stopPage();
        GetBackupForCreateTableList();
        alert(LanguageStr_Service.Backup_ClearAllComplete);
        GetBackupForCreateTableList();
    });
    request.fail(function (jqxhr, textStatus) {
        stopPage();
    });
}
// ----------- 備份 (3)Seting Mapping  2017.02.22 End ----------- 
/*=================== 備份 (5)建立 backup 2017.02.22 Start ===================*/
function CreateVDBackup(id_vd) {
    if (id_vd.length > 0) {
        for (var i = 0; i < id_vd.length; i++) {
            var request = Ajax_DeleteSchedule(id_vd[i]);
            CallBackCreateVDBackup(request);
        }
    }
};

function CallBackCreateVDBackup(request) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }

    });
    request.fail(function (jqxhr, textStatus) {

    });
}
/*=================== 備份 (5)建立 backup 2017.02.22 End ===================*/
/*=================== 備份 (8)管理畫面 2017.02.22 Start ===================*/
function GetBackupMgtVDs() {
    var request = Ajax_ListBackupVD();
    var OrgRestoreVDList;
    request.done(function (AjaxData, statustext, jqXHR) {
        OrgRestoreVDList = ListBackupVDs(AjaxData);
        GetVDsForCreateBackupMgtList(OrgRestoreVDList);
    });
    request.fail(function (jqXHR, textStatus) {
    });
}
// 定義class承接json資料
function ListBackupVDs(JsonData) {
    var HtmlStr = "";
    var tmpRestoreVDs;
    var OrgRestoreVDList = [];
    BackupMgUsageCapacity = 0;
    $.each(JsonData['Original'], function (rvdindex, rvdelement) {
        tmpRestoreVDs = new RestoreVDsClass(rvdelement['SourceVDName'], rvdelement['SourceVDID'], rvdelement['BackupVDName'], rvdelement['BackupVDID'], rvdelement['BackupTime'], rvdelement['TotalSize'], rvdelement['State']);
        OrgRestoreVDList[rvdelement['BackupVDID']] = tmpRestoreVDs;
        BackupMgUsageCapacity += rvdelement['TotalSize'];
    });
    return OrgRestoreVDList;
}

function GetVDsForCreateBackupMgtList(OrgRestoreVDList) {
    var request = Ajax_BackupListVDs_SendCommand();
    request.done(function (AjaxData, statustext, jqXHR) {
        BackupMgtListVDsToUI(AjaxData, OrgRestoreVDList);
    });
    request.fail(function (jqXHR, textStatus) {

    });
}
// 定義class承接json資料
function BackupMgtListVDsToUI(JsonData, OrgRestoreVDList) {
    var HtmlStr = "";
    var OrgVdid;
    var bklist;
    var OrgSourceVDList = [];
    OrgMultiBackupMgtListBoth = [];
    OrgMultiBackupMgtListSource = [];
    OrgMultiBackupMgtListBk = [];

    $.each(JsonData['Org'], function (index, element) {
        OrgVdid = new VDBackupMgtClass(element['VDID'], element['UserID'], element['VDName'], element['RAM'], element['Suspend'], element['Desc'], element["DomainID"], element['AllSize'], element['CreateTime'], element['ModfiyTime'], element['RecoveryTime'], element['State']);
        OrgSourceVDList[element['VDID']] = OrgVdid;
    });

    for (var key in OrgSourceVDList) {
        if (typeof (OrgRestoreVDList[key]) !== 'undefined') {
            bklist = new VDBackupMgtBothListClass(OrgSourceVDList[key].vdid, OrgSourceVDList[key].vdname, OrgSourceVDList[key].desc, OrgSourceVDList[key].recovery_time, OrgSourceVDList[key].create_time, OrgSourceVDList[key].allsize, OrgSourceVDList[key].state, OrgRestoreVDList[key].BackupVDID, OrgRestoreVDList[key].BackupVDName, OrgRestoreVDList[key].BackupTime, OrgRestoreVDList[key].TotalSize, OrgRestoreVDList[key].State);
            OrgMultiBackupMgtListBoth.push(bklist);
            delete OrgRestoreVDList[key];
        }
        else {
            bklist = new VDBackupMgtSRListClass(OrgSourceVDList[key].vdid, OrgSourceVDList[key].vdname, OrgSourceVDList[key].desc, OrgSourceVDList[key].recovery_time, OrgSourceVDList[key].create_time, OrgSourceVDList[key].allsize, OrgSourceVDList[key].state, "", "", "", "", "");
            OrgMultiBackupMgtListSource.push(bklist);
        }
    }

    for (var key2 in OrgRestoreVDList) {
        bklist = new VDBackupMgtBKListClass("", "", "", "", "", "", "", OrgRestoreVDList[key2].BackupVDID, OrgRestoreVDList[key2].BackupVDName, OrgRestoreVDList[key2].BackupTime, OrgRestoreVDList[key2].TotalSize, OrgRestoreVDList[key2].State);
        OrgMultiBackupMgtListBk.push(bklist);
    }

    CreateBKMgtSourceVDNameListListHtml();
    CreateStorageCapacityHtml(BackupMgtTotalCapacity, BackupMgtAvailCapacity);
}

// 將VD相關資料List出來
function CreateBKMgtSourceVDNameListListHtml() {
    var HtmlStr_Source_both = "";
    var HtmlStr_Backup_both = "";
    var HtmlStr_Source_sr = "";
    var HtmlStr_Backup_sr = "";
    var HtmlStr_Source_bk = "";
    var HtmlStr_Backup_bk = "";
    // var HtmlStr_both = "";
    // var HtmlStr_sr = "";
    // var HtmlStr_bk = "";
    // var HtmlStr_btn = "";

    var no = 1;


    // HtmlStr_btn += '<div style="position: relative;z-index: 10;left: 836px;width: 0;">';
    // HtmlStr_btn += '<div style="position: absolute;top: -100px;">';
    // HtmlStr_btn += '<div style="position: relative;top: 125px;vertical-align: middle;text-align: center;"></div>';
    // HtmlStr_btn += '<div id="BackupVDBtn" class="Backup_arrow" style="top: 130px;">⇨</div>';
    // HtmlStr_btn += '<div id="RestoreVDBtn" class="Backup_arrow" style="top: 150px;">⇦</div>';
    // HtmlStr_btn += '<div style="position: relative;top: 160px;vertical-align: middle;text-align: center;"></div>';
    // HtmlStr_btn += '</div>';
    // HtmlStr_btn += '</div>';

    $.each(OrgMultiBackupMgtListBoth, function (index, element) {
        if (index === OrgMultiBackupMgtListBoth.length - 1)
            HtmlStr_Source_both += '<div id="BackupMgtRow' + element['vdname'] + '" class = "Div_VMRow LastVMRow">';
        else
            HtmlStr_Source_both += '<div id="BackupMgtRow' + element['vdname'] + '" class = "Div_VMRow">';
        if (element['vdstate'] == -5) {
            HtmlStr_Source_both += '<H4 class = "BackupMgt_stablelist_row1 AbsoluteLayout RowField" >' + no + '</H4>';
            HtmlStr_Source_both += '<div class = "BackupMgt_stablelist_row2 AbsoluteLayout RowField" >';
            HtmlStr_Source_both += '<div style="position:relative">';
            HtmlStr_Source_both += '</div>';
            HtmlStr_Source_both += '</div>';
            HtmlStr_Source_both += '<H4 class = "BackupMgt_stablelist_row3 AbsoluteLayout RowField" >' + element['vdname'] + '</H4>';
            HtmlStr_Source_both += '<H4 class = "BackupMgt_stablelist_row7 AbsoluteLayout RowField" >' + element['create_time'] + '</H4>';
            HtmlStr_Source_both += '<H4 class = "BackupMgt_stablelist_row4 AbsoluteLayout RowField" >' + element['recovery_time'] + '</H4>';
            HtmlStr_Source_both += '<H4 class = "BackupMgt_stablelist_row5 AbsoluteLayout RowField" >' + formatBytes(element['vdallsize'], 1) + '</H4>';
            HtmlStr_Source_both += '<H4 class = "BackupMgt_stablelist_row6 AbsoluteLayout RowField" title="(' + LanguageStr_Service.Backup_SourceVD_Crash + ')' + element['desc'] + '">(' + LanguageStr_Service.Backup_SourceVD_Crash + ')' + element['desc'] + '</H4>';
        }
        else {
            HtmlStr_Source_both += '<H4 class = "BackupMgt_stablelist_row1 AbsoluteLayout RowField" >' + no + '</H4>';
            HtmlStr_Source_both += '<div class = "BackupMgt_stablelist_row2 AbsoluteLayout RowField" >';
            HtmlStr_Source_both += '<div style="position:relative">';
            HtmlStr_Source_both += '<input type="checkbox" id="' + element['vdid'] + '" class="iCheckBoxSourceVDSelect">';
            HtmlStr_Source_both += '</div>';
            HtmlStr_Source_both += '</div>';
            HtmlStr_Source_both += '<H4 class = "BackupMgt_stablelist_row3 AbsoluteLayout RowField" >' + element['vdname'] + '</H4>';
            HtmlStr_Source_both += '<H4 class = "BackupMgt_stablelist_row7 AbsoluteLayout RowField" >' + element['create_time'] + '</H4>';
            HtmlStr_Source_both += '<H4 class = "BackupMgt_stablelist_row4 AbsoluteLayout RowField" >' + element['recovery_time'] + '</H4>';
            HtmlStr_Source_both += '<H4 class = "BackupMgt_stablelist_row5 AbsoluteLayout RowField" >' + formatBytes(element['vdallsize'], 1) + '</H4>';
            HtmlStr_Source_both += '<H4 class = "BackupMgt_stablelist_row6 AbsoluteLayout RowField" title="' + element['desc'] + '">' + element['desc'] + '</H4>';
        }
        HtmlStr_Source_both += '</div> ';

        if (index === OrgMultiBackupMgtListBoth.length - 1)
            HtmlStr_Backup_both += '<div id="BackupMgtRow' + element['bkvdname'] + '" class = "Div_VMRow LastVMRow">';
        else
            HtmlStr_Backup_both += '<div id="BackupMgtRow' + element['bkvdname'] + '" class = "Div_VMRow">';
        if (element['bkstate'] == -1) {
            HtmlStr_Backup_both += '<H4 class = "BackupMgt_btablelist_row1 AbsoluteLayout RowField" >' + no + '</H4>';
            HtmlStr_Backup_both += '<div class = "BackupMgt_btablelist_row2 AbsoluteLayout RowField" style="width: 448px;font-size: 14px;font-weight: 900;">';
            HtmlStr_Backup_both += '<div style="position:relative">';
            HtmlStr_Backup_both += '<label>' + LanguageStr_Service.Backup_BKVD_Fail + '</label>';
            HtmlStr_Backup_both += '</div>';
            HtmlStr_Backup_both += '</div>';
        }
        else {
            HtmlStr_Backup_both += '<H4 class = "BackupMgt_btablelist_row1 AbsoluteLayout RowField" >' + no + '</H4>';
            HtmlStr_Backup_both += '<div class = "BackupMgt_btablelist_row2 AbsoluteLayout RowField" >';
            HtmlStr_Backup_both += '<div style="position:relative">';
            HtmlStr_Backup_both += '<input type="checkbox" id="a' + index + '" class="iCheckBoxBackupVDSelect">';
            HtmlStr_Backup_both += '</div>';
            HtmlStr_Backup_both += '</div>';
            HtmlStr_Backup_both += '<H4 class = "BackupMgt_btablelist_row3 AbsoluteLayout RowField" >' + element['bkvdname'] + '</H4>';
            HtmlStr_Backup_both += '<H4 class = "BackupMgt_btablelist_row4 AbsoluteLayout RowField" >' + element['bktime'] + '</H4>';
            HtmlStr_Backup_both += '<H4 class = "BackupMgt_btablelist_row5 AbsoluteLayout RowField" >' + formatBytes(element['bksize'], 1) + '</H4>';
        }
        HtmlStr_Backup_both += '</div> ';

        no++;
    });

    //--------------------------------------------------------------------------------------------------------------------------------------------
    $.each(OrgMultiBackupMgtListSource, function (index, element) {
        if (index === OrgMultiBackupMgtListSource.length - 1)
            HtmlStr_Source_sr += '<div id="BackupMgtRow' + element['vdname'] + '" class = "Div_VMRow LastVMRow">';
        else
            HtmlStr_Source_sr += '<div id="BackupMgtRow' + element['vdname'] + '" class = "Div_VMRow">';
        if (element['vdstate'] == -5) {
            HtmlStr_Source_sr += '<H4 class = "BackupMgt_stablelist_row1 AbsoluteLayout RowField" >' + no + '</H4>';
            HtmlStr_Source_sr += '<div class = "BackupMgt_stablelist_row2 AbsoluteLayout RowField" >';
            HtmlStr_Source_sr += '<div style="position:relative">';
            HtmlStr_Source_sr += '</div>';
            HtmlStr_Source_sr += '</div>';
            HtmlStr_Source_sr += '<H4 class = "BackupMgt_stablelist_row3 AbsoluteLayout RowField" >' + element['vdname'] + '</H4>';
            HtmlStr_Source_sr += '<H4 class = "BackupMgt_stablelist_row7 AbsoluteLayout RowField" >' + element['create_time'] + '</H4>';
            HtmlStr_Source_sr += '<H4 class = "BackupMgt_stablelist_row4 AbsoluteLayout RowField" >' + element['recovery_time'] + '</H4>';
            HtmlStr_Source_sr += '<H4 class = "BackupMgt_stablelist_row5 AbsoluteLayout RowField" >' + formatBytes(element['vdallsize'], 1) + '</H4>';
            HtmlStr_Source_sr += '<H4 class = "BackupMgt_stablelist_row6 AbsoluteLayout RowField" title="(' + LanguageStr_Service.Backup_SourceVD_Crash + ')' + element['desc'] + '">(' + LanguageStr_Service.Backup_SourceVD_Crash + ')' + element['desc'] + '</H4>';
        }
        else {
            HtmlStr_Source_sr += '<H4 class = "BackupMgt_stablelist_row1 AbsoluteLayout RowField" >' + no + '</H4>';
            HtmlStr_Source_sr += '<div class = "BackupMgt_stablelist_row2 AbsoluteLayout RowField" >';
            HtmlStr_Source_sr += '<div style="position:relative">';
            HtmlStr_Source_sr += '<input type="checkbox" id="' + element['vdid'] + '" class="iCheckBoxSourceVDSelect">';
            HtmlStr_Source_sr += '</div>';
            HtmlStr_Source_sr += '</div>';
            HtmlStr_Source_sr += '<H4 class = "BackupMgt_stablelist_row3 AbsoluteLayout RowField" >' + element['vdname'] + '</H4>';
            HtmlStr_Source_sr += '<H4 class = "BackupMgt_stablelist_row7 AbsoluteLayout RowField" >' + element['create_time'] + '</H4>';
            HtmlStr_Source_sr += '<H4 class = "BackupMgt_stablelist_row4 AbsoluteLayout RowField" >' + element['recovery_time'] + '</H4>';
            HtmlStr_Source_sr += '<H4 class = "BackupMgt_stablelist_row5 AbsoluteLayout RowField" >' + formatBytes(element['vdallsize'], 1) + '</H4>';
            HtmlStr_Source_sr += '<H4 class = "BackupMgt_stablelist_row6 AbsoluteLayout RowField" title="' + element['desc'] + '">' + element['desc'] + '</H4>';
        }
        HtmlStr_Source_sr += '</div> ';

        if (index === OrgMultiBackupMgtListSource.length - 1)
            HtmlStr_Backup_sr += '<div id="BackupMgtRow' + element['bkvdname'] + '" class = "Div_VMRow LastVMRow">';
        else
            HtmlStr_Backup_sr += '<div id="BackupMgtRow' + element['bkvdname'] + '" class = "Div_VMRow">';
        HtmlStr_Backup_sr += '<H4 class = "BackupMgt_btablelist_row1 AbsoluteLayout RowField" >' + no + '</H4>';
        HtmlStr_Backup_sr += '<div class = "BackupMgt_btablelist_row2 AbsoluteLayout RowField" style="width: 700px;font-size: 14px;font-weight: 900;">';
        HtmlStr_Backup_sr += '<div style="position:relative">';
        HtmlStr_Backup_sr += '<label>' + LanguageStr_Service.BackupMgt_Message1 + '</label>';
        HtmlStr_Backup_sr += '</div>';
        HtmlStr_Backup_sr += '</div>';
        HtmlStr_Backup_sr += '</div> ';

        no++;
    });

    //--------------------------------------------------------------------------------------------------------------------------------------------
    $.each(OrgMultiBackupMgtListBk, function (index, element) {
        if (element['bkstate'] != -1) {
            if (index === OrgMultiBackupMgtListBk.length - 1)
                HtmlStr_Source_bk += '<div id="BackupMgtRow' + element['vdname'] + '" class = "Div_VMRow LastVMRow">';
            else
                HtmlStr_Source_bk += '<div id="BackupMgtRow' + element['vdname'] + '" class = "Div_VMRow">';
            HtmlStr_Source_bk += '<H4 class = "BackupMgt_stablelist_row1 AbsoluteLayout RowField" >' + no + '</H4>';
            HtmlStr_Source_bk += '<div class = "BackupMgt_stablelist_row2 AbsoluteLayout RowField" style="width: 700px;font-size: 14px;font-weight: 900;">';
            HtmlStr_Source_bk += '<div style="position:relative">';
            HtmlStr_Source_bk += '<label>' + LanguageStr_Service.BackupMgt_Message2 + '</label>';
            HtmlStr_Source_bk += '</div>';
            HtmlStr_Source_bk += '</div>';
            HtmlStr_Source_bk += '</div> ';

            if (index === OrgMultiBackupMgtListBk.length - 1)
                HtmlStr_Backup_bk += '<div id="BackupMgtRow' + element['bkvdname'] + '" class = "Div_VMRow LastVMRow">';
            else
                HtmlStr_Backup_bk += '<div id="BackupMgtRow' + element['bkvdname'] + '" class = "Div_VMRow">';

            HtmlStr_Backup_bk += '<H4 class = "BackupMgt_btablelist_row1 AbsoluteLayout RowField" >' + no + '</H4>';
            HtmlStr_Backup_bk += '<div class = "BackupMgt_btablelist_row2 AbsoluteLayout RowField" >';
            HtmlStr_Backup_bk += '<div style="position:relative">';
            HtmlStr_Backup_bk += '<input type="checkbox" id="b' + index + '" class="iCheckBoxBackupVDSelect">';
            HtmlStr_Backup_bk += '</div>';
            HtmlStr_Backup_bk += '</div>';
            HtmlStr_Backup_bk += '<H4 class = "BackupMgt_btablelist_row3 AbsoluteLayout RowField" >' + element['bkvdname'] + '</H4>';
            HtmlStr_Backup_bk += '<H4 class = "BackupMgt_btablelist_row4 AbsoluteLayout RowField" >' + element['bktime'] + '</H4>';
            HtmlStr_Backup_bk += '<H4 class = "BackupMgt_btablelist_row5 AbsoluteLayout RowField" >' + formatBytes(element['bksize'], 1) + '</H4>';

            HtmlStr_Backup_bk += '</div> ';

            no++;
        }
    });
    //--------------------------------------------------------------------------------------------------------------------------------------------    
    $('#BackupMgt_Content_body_source').empty();
    $('#BackupMgt_Content_body_source').append(HtmlStr_Source_both);
    $('#BackupMgt_Content_body_source').append(HtmlStr_Source_sr);
    $('#BackupMgt_Content_body_source').append(HtmlStr_Source_bk);
    $('#BackupMgt_Content_body_backup').empty();
    $('#BackupMgt_Content_body_backup').append(HtmlStr_Backup_both);
    $('#BackupMgt_Content_body_backup').append(HtmlStr_Backup_sr);
    $('#BackupMgt_Content_body_backup').append(HtmlStr_Backup_bk);
    // $('#Div_BackupMgtList').empty();
    // $('#Div_BackupMgtList').append(HtmlStr_btn);
    // $('#Div_BackupMgtList').append(HtmlStr_both);
    // $('#Div_BackupMgtList').append(HtmlStr_sr);
    // $('#Div_BackupMgtList').append(HtmlStr_bk);

    $('.iCheckBoxSourceVDSelect').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });
    $('.iCheckBoxBackupVDSelect').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });

    $('.iCheckBoxAllSourceVDSelect').iCheck('uncheck');
    $('.iCheckBoxSourceVDSelect').iCheck('uncheck');
    $('.iCheckBoxAllBackUpVDSelect').iCheck('uncheck');
    $('.iCheckBoxBackupVDSelect').iCheck('uncheck');
    $('.iCheckBoxBackupVDSelect').on('ifChanged', function (event) {
        EnableDisableSelectBackupMgtActionButton();
    });
};
//<H4 class = "BackupMgt_stablelist_row6 AbsoluteLayout RowField" title="(' + LanguageStr_Service.Backup_SourceVD_Crash + ')' + element['desc'] + '">(' + LanguageStr_Service.Backup_SourceVD_Crash + ')' + element['desc'] + '</H4>';
function CreateStorageCapacityHtml(Total_Capacity, Avail_Capacity) {
    var Convert_Total_Capacity;
    var Convert_Avail_Capacity;
    var Convert_Used_Capacity;
    var Convert_bk_Capacity;
    var Convert_fs_Capacity
    var C, A, B, b1, b2;
    var CapacityValue = (Avail_Capacity / Total_Capacity) * 100;
    // 計算使用容量 2017.03.31 william
    b1 = BackupMgUsageCapacity/(1024*1024*1024);
    Convert_Total_Capacity = formatGB(Total_Capacity / 1024, 1);
    Convert_Avail_Capacity = formatGB(Avail_Capacity / 1024, 1);
    Convert_Used_Capacity = formatGB((Total_Capacity - Avail_Capacity) / 1024, 1);
    Convert_bk_Capacity = formatGB(b1, 1);    
    Convert_fs_Capacity = formatGB((((Total_Capacity - Avail_Capacity) / 1024) - b1), 1);

    var HtmlStr_Capacity_Div = "";
    HtmlStr_Capacity_Div += '<label  style="font-weight: 700;font-size: 16px;">' + LanguageStr_Service.BackupMgt_AllBK_Capacity + '</label>';
    HtmlStr_Capacity_Div += '&nbsp;';
    HtmlStr_Capacity_Div += '<label>' + LanguageStr_Service.BackupMgt_Total_Capacity + '</label>';
    HtmlStr_Capacity_Div += '<label>' + Convert_Total_Capacity + '</label>';
    HtmlStr_Capacity_Div += ',&nbsp;&nbsp;&nbsp;';
    HtmlStr_Capacity_Div += '<label>' + LanguageStr_Service.BackupMgt_Avail_Capacity + '</label>';
    if (CapacityValue <= 10)
        HtmlStr_Capacity_Div += '<label style="color:#ff0000;">' + Convert_Avail_Capacity + '</label>';
    else
        HtmlStr_Capacity_Div += '<label style="color:#044de1;">' + Convert_Avail_Capacity + '</label>';
    HtmlStr_Capacity_Div += ',&nbsp;&nbsp;&nbsp;';
    HtmlStr_Capacity_Div += '<label>' + LanguageStr_Service.BackupMgt_Used_Capacity + '</label>';
    HtmlStr_Capacity_Div += '<label>' + Convert_Used_Capacity + '</label>';
    HtmlStr_Capacity_Div += '&nbsp;';
    HtmlStr_Capacity_Div += '(';
    HtmlStr_Capacity_Div += '<label>' + LanguageStr_Service.BackupMgt_BK_Capacity + '</label>';
    HtmlStr_Capacity_Div += '<label>' + Convert_bk_Capacity + '</label>';
    HtmlStr_Capacity_Div += ',&nbsp;&nbsp;';
    HtmlStr_Capacity_Div += '<label>' + LanguageStr_Service.BackupMgt_FS_Capacity + '</label>';
    HtmlStr_Capacity_Div += '<label>' + Convert_fs_Capacity + '</label>';
    HtmlStr_Capacity_Div += ')';
    HtmlStr_Capacity_Div += '<label>' + LanguageStr_Service.BackupMgt_text_Dot + '</label>';


    $('#BackupMgt_Storage_Capacity').empty();
    $('#BackupMgt_Storage_Capacity').append(HtmlStr_Capacity_Div);
}

function InitBackupRestoreButton() {
    var backup_ask = LanguageStr_Service.Service_BackupAsk;
    $('#BackupVDBtn').click(function (event) {
        event.preventDefault();
        var confirmable = confirm(backup_ask);
        if (confirmable === true) {
            BackupSelectVD('iCheckBoxSourceVDSelect');
        }
    });

    var restore_ask = LanguageStr_Service.Service_RestoreAsk;
    $('#RestoreVDBtn').click(function (event) {
        event.preventDefault();
        // var confirmable = confirm(restore_ask);
        // if (confirmable === true) {
        //     RestoreSelectVD('iCheckBoxBackupVDSelect');
        // }
        RAIDRestore = [];
        BackupIDArray = [];
        directRestoreArray = [];
        waitRestoreArray = [];
        SelectRAIDHtmlStr = '';
        locationhrefFlag = false;
        WaitRAIDRestore = [];
        $('.Div_WaitAjax').dialog( 'open' );
        CheckMappingVolume('iCheckBoxBackupVDSelect');
    });

    var delete_ask = LanguageStr_Service.Service_BackupVDDeleteAsk;
    $('#btn_delete_all_BackupVD').click(function (event) {
        event.preventDefault();
        var confirmable = confirm(delete_ask);
        if (confirmable === true) {
            DeleteSelectBackupVD('iCheckBoxBackupVDSelect');
        }
    });

    $('#btn_reflash_all_BackupVD').click(function (event) {
        event.preventDefault();
        reflashBackupMgtList();
    });
}

function reflashBackupMgtList(bolStartPolling) {
    blockPage();
    CallBackreflashBackupMgtList();
}

function CallBackreflashBackupMgtList() {
    // request.done(function (msg, statustext, jqxhr) {
    //     if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
    //         Logout_Another_Admin_Login();
    //         return false;
    //     }
    //     else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
    //         window.location.href = 'index.php';
    //         return false;
    //     }
    stopPage();
    GetBackupForCreateTableList();
    // });
    // request.fail(function (jqxhr, textStatus) {

    // });
}


// Restore Check  Mapping Volume
function CheckMappingVolume(checkboxclass) {
    GetBackupID = [];
    $.each($('.' + checkboxclass + ':checked'), function (index, element) {
        GetBackupID.push($(element).attr('id'));
    });    
    if (GetBackupID.length > 0)
        GetMappingResult();   
}

function GetMappingResult() {
    var tempData;
    var res;
    var index;
    var username = '';
    var vdname = '';
    var sourcevdid = '';
    var backupvdid = '';
    var backupvdname = '';
    var IsSourceEmpty = false;

    if (GetBackupID.length > 0) {

        res = GetBackupID[0].substr(0, 1);
        index = GetBackupID[0].substr(1);
        switch (res) {
            case 'a':
                sourcevdid = OrgMultiBackupMgtListBoth[index].vdid;
                backupvdid = OrgMultiBackupMgtListBoth[index].bkvdid;
                backupvdname = OrgMultiBackupMgtListBoth[index].bkvdname;
                tempData = new RAIDRestoreClass(username, vdname, sourcevdid, backupvdid, backupvdname);
                RAIDRestore[backupvdid] = tempData;                
                break;
            case 'b':
                username = OrgMultiBackupMgtListBk[index].bkvdname;
                vdname = OrgMultiBackupMgtListBk[index].bkvdname;
                sourcevdid = OrgMultiBackupMgtListBk[index].vdid;
                backupvdid = OrgMultiBackupMgtListBk[index].bkvdid;
                backupvdname = OrgMultiBackupMgtListBk[index].bkvdname;
                IsSourceEmpty = true;
                tempData = new RAIDRestoreClass(username, vdname, sourcevdid, backupvdid, backupvdname);
                RAIDRestore[backupvdid] = tempData;                    
                break;
        } 
        
        var request = Ajax_CheckMappingVolume(backupvdid);
        CallBackGetMappingResult(request, backupvdid, IsSourceEmpty);

    } else if (GetBackupID.length == 0) {        
        $('.Div_WaitAjax').dialog('close'); 
        if(SelectRAIDHtmlStr.length != 0) {
            locationhrefFlag = false;
            RestoreVDarr();
            $('#SelectRaidContent').html(SelectRAIDHtmlStr);
            RebindSelectRAIDCombobox(); 
            $( "#dialog_RestoreSelectRaid" ).dialog('open');
        } else {
            locationhrefFlag = true;
            RestoreVDarr();
        }

    }
};

function CallBackGetMappingResult(request, backupvdid, IsSourceEmpty) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }

        if(msg['Mapping'] == false || IsSourceEmpty) {
            BackupIDArray.push(backupvdid);
            waitRestoreArray.push(backupvdid);
            CreateSelectRAIDHtml(backupvdid);
        } else {
            directRestoreArray.push(backupvdid);
        }
        GetBackupID.splice(0, 1);
        GetMappingResult();
    });

    request.fail(function (jqxhr, textStatus) {
        if (jqxhr.status == 400) {
            GetBackupID.splice(0, 1);
            GetMappingResult();
        }
        else if (jqxhr.status == 409) {

        }
    });
}

function CreateSelectRAIDHtml(id) {
    SelectRAIDHtmlStr += '<div>';
    SelectRAIDHtmlStr += '<label style="position:relative;top:10px;font-size: 16px; font-weight: 900;">' + RAIDRestore[id].backupvdname + '</label>';
    SelectRAIDHtmlStr += '<div style="padding: 15px 0px 0px 0px;">';                
    SelectRAIDHtmlStr += '<label style="position:relative;top:10px;">RAID ID :</label>';
    SelectRAIDHtmlStr += '<div id="SelectRAID_' + id + '" class="SelectRAIDID" style="position:relative;width: 200px;bottom: 14px; left: 55px;">';
    SelectRAIDHtmlStr += '</div>';                
    SelectRAIDHtmlStr += '</div>';                
    SelectRAIDHtmlStr += '</div>';                                                           
}

function RebindSelectRAIDCombobox() {
    if (BackupIDArray.length > 0) {
        Get_Raid_ID($('#SelectRAID_' + BackupIDArray[0]), -1, false);
        BackupIDArray.splice(0, 1);
        RebindSelectRAIDCombobox();
    }        
}


// var idarr;
// function RestoreSelectVD(checkboxclass) {
//     idarr = [];
//     $.each($('.' + checkboxclass + ':checked'), function (index, element) {
//         idarr.push($(element).attr('id'));
//     });
//     if (idarr.length > 0)
//         RestoreVDarr();
// }

function RestoreVDarr() {
    // var res;
    // var index;
    var username = '';
    var vdname = '';
    var sourcevdid = '';
    var backupvdid = '';
    var backupvdname = '';
    var tempid = '';
    var raidid = '';
    
    tempid = directRestoreArray[0];
    if(typeof(tempid) !== "undefined") {
        username = RAIDRestore[tempid].username;
        vdname = RAIDRestore[tempid].vdname;
        sourcevdid = RAIDRestore[tempid].sourcevdid;
        backupvdid = RAIDRestore[tempid].backupvdid;
        backupvdname = RAIDRestore[tempid].backupvdname;
        var request = Ajax_VDRestore(username, vdname, sourcevdid, backupvdid, backupvdname, raidid);
        CallBackRestoreVDarr(request);        
    }    
};

function CallBackRestoreVDarr(request) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        // idarr.splice(0, 1);
        directRestoreArray.splice(0, 1);
        RestoreVDarr();
        if(locationhrefFlag)
            $('#tab-Backup-container').easytabs('select', '#tabs1-bktask-list');
        //CreateBKMgtSourceVDNameListListHtml();
    });
    request.fail(function (jqxhr, textStatus) {
        if (jqxhr.status == 400) {
            // idarr.splice(0, 1);
            directRestoreArray.splice(0, 1);
            RestoreVDarr();
        }
        else if (jqxhr.status == 409) {
            // CreateBackupVDRename();
        }
    });
}

function WaitRestoreVDarr() {
    var username = '';
    var vdname = '';
    var sourcevdid = '';
    var backupvdid = '';
    var backupvdname = '';
    var raidid = '';
    var tempid = '';
    
    tempid = waitRestoreArray[0];
    if(typeof(tempid) !== "undefined") {
        username = RAIDRestore[tempid].username;
        vdname = RAIDRestore[tempid].vdname;
        sourcevdid = RAIDRestore[tempid].sourcevdid;
        backupvdid = RAIDRestore[tempid].backupvdid;
        backupvdname = RAIDRestore[tempid].backupvdname;
        raidid = WaitRAIDRestore[tempid].raidid;
        var request = Ajax_VDRestore(username, vdname, sourcevdid, backupvdid, backupvdname, raidid);
        CallBackWaitRestoreVDarr(request, tempid);        
    } else {
        $('#tab-Backup-container').easytabs('select', '#tabs1-bktask-list');
    }   
};

function CallBackWaitRestoreVDarr(request , bkid) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        waitRestoreArray.splice(0, 1);
        WaitRestoreVDarr();    
    });
    request.fail(function (jqxhr, textStatus) {
        if (jqxhr.status == 400) {
            waitRestoreArray.splice(0, 1);
            WaitRestoreVDarr();    
        }
        else if (jqxhr.status == 409) {
            waitRestoreArray.splice(0, 1);                            
            CreateBackupVDRename(bkid);
        }
    });
}

function BackupSelectVD(checkboxclass) {
    var idarr = [];
    $.each($('.' + checkboxclass + ':checked'), function (index, element) {
        idarr.push($(element).attr('id'));
    });
    if (idarr.length > 0)
        BackupVDarr(idarr);
}

function BackupVDarr(vd_id) {
    if (vd_id.length > 0) {
        var request = Ajax_VDBackup(vd_id[0]);
        CallBackBackupVDarr(request, vd_id);
    }
};

function CallBackBackupVDarr(request, vd_id) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        vd_id.splice(0, 1);
        BackupVDarr(vd_id);
        $('#tab-Backup-container').easytabs('select', '#tabs1-bktask-list');
        // CreateBKMgtSourceVDNameListListHtml();
    });
    request.fail(function (jqxhr, textStatus) {

    });
}

function DeleteSelectBackupVD(checkboxclass) {
    var idarr = [];
    $.each($('.' + checkboxclass + ':checked'), function (index, element) {
        idarr.push($(element).attr('id'));
    });
    if (idarr.length > 0)
        DeleteBackupVD(idarr);
}

function DeleteBackupVD(id_BackupVD) {
    var res;
    var index;
    var backupvdid = '';

    if (id_BackupVD.length > 0) {

        res = id_BackupVD[0].substr(0, 1);
        index = id_BackupVD[0].substr(1);
        switch (res) {
            case 'a':

                backupvdid = OrgMultiBackupMgtListBoth[index].bkvdid;

                break;
            case 'b':

                backupvdid = OrgMultiBackupMgtListBk[index].bkvdid;

                break;
        }
        blockPage();
        var request = Ajax_DeleteBackupVD(backupvdid);
        CallBackDeleteBackupVD(request, id_BackupVD);
    }
    else
        GetBackupForCreateTableList();

};

function CallBackDeleteBackupVD(request, id_BackupVD) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        id_BackupVD.splice(0, 1);
        DeleteBackupVD(id_BackupVD);
        stopPage();
        // CreateBKMgtSourceVDNameListListHtml();
    });
    request.fail(function (jqxhr, textStatus) {
        stopPage();
    });
}

/*=================== 備份 (8)管理畫面 2017.02.22 End ===================*/
/*=================== (9)Task 2017.03.01 Start ===================*/
function StopBackupPolling() {
    PollingBackupListFlag = false;
    PollingBackupListTaskFlag = false;
}

function StartBackupPolling(isPollingBackupList, isPollingBackupTask) {
    if (isPollingBackupList)
        PollingBackupListFlag = true;
    if (isPollingBackupTask)
        PollingBackupListTaskFlag = true;
}

function InitVMBKTaskButtons() {
    $('#btn_clear_bktask').click(function (event) {
        event.preventDefault();
        VMClearBackupTask();
    });
    $('#btn_refresh_bktask').click(function (event) {
        event.preventDefault();
        StopBackupPolling();
        VMListBackupTask(true);
        StartBackupPolling(true, false);
    });
}

function VMClearBackupTask() {
    StopBackupPolling();
    blockPage();
    var request = Ajax_ClearBackupTask();
    taskpollingcount = 0;
    CallBackVMClearBackupTask(request);
}

function CallBackVMClearBackupTask(request) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        stopPage();
        VMListBackupTask(true);
        StartBackupPolling(true, true);
    });
    request.fail(function (jqxhr, textStatus) {
        stopPage();
        // oError.CheckAuth(jqxhr.status, ActionStatus.VMDeleteSnapShotTask);
        StartBackupPolling(true, true);
    });
}

function ClearBackupTaskInterval() {
    BackupTaskStart = false;
}

function VMListBackupTask(bolStartPolling) {
    blockPage();
    var request = Ajax_VMListBackupTask();
    CallBackVMListBackupTask(request, bolStartPolling);
}

function CallBackVMListBackupTask(request, bolStartPolling) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        stopPage();
        CreateVMBackupTaskTable(msg);
        if (typeof (NowSortBackupTaskType) === 'undefined' || NowSortBackupTaskType === null) {
            $('.label_bktask_arrow').html('');
            $('#bktaskBKListStartTime1').html('&#9660;');
            var th = $('.VMBackupList_Task_StartTime');
            th.removeClass('asc');
            th.addClass('desc');
            NowSortBackupTaskDirection = SortDirection.Desc;
            NowSortBackupTaskType = SortType.String;
            NowSortBackupTaskDOMID = 'start_time';
        }
        SortList(NowSortBackupTaskDirection, NowSortBackupTaskType, NowSortBackupTaskDOMID, VMListBackupCloneTable);
        $('#Div_VMBackup_Clone_List').html(CreateVMBackupTaskHtmlBTable());
        BackupTaskprogress();
        $('#Div_VMBackup_Clone_List').tooltip({
            position: { my: "left top+0", at: "left bottom", collision: "flipfit" }
        });

        // $.each(VMListSnapshotCloneTable,function(index,element){
        //     if(element['status'] >= 0 && element['status'] !== 3){
        //         $('#progress' + element['id']).progressbar({
        //             value: Math.floor( element['process'] )
        //         });                               
        //         $('#progressText' + element['id']).text(element['process'] + '%');                            
        //     }
        // }); 

        if (bolStartPolling) {
            PollingBackupListTaskFlag = true;
            PollingVMListBackupTask(true);
        }
    });
    request.fail(function (jqxhr, textStatus) {
        if (bolStartPolling) {
            PollingBackupListTaskFlag = true;
            PollingVMListBackupTask(true);
        }
    });
}

function CheckForPollingListBackupTask() {
    if (!BackupTaskStart) {
        isRunningPollingBackupTask = false;
        return false;
    }
    if (PollingBackupListTaskFlag) {
        setTimeout(function () { PollingVMListBackupTask(false); }, 8000); // 避免頻繁的Request,將5秒改成8秒 2017.03.10 william
    }
    else {
        setTimeout(function () { CheckForPollingListBackupTask(); }, 1000);
    }
}

function PollingVMListBackupTask(checkRunning) {
    if (checkRunning && isRunningPollingBackupTask)
        return false;
    isRunningPollingBackupTask = true;
    var request = Ajax_VMListBackupTask();
    CallBackPollingVMListBackupTask(request);
}

function CallBackPollingVMListBackupTask(request) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        try {
            CreateVMBackupTaskTable(msg);
            var sort_id = getSortID();
            SortList(NowSortBackupTaskDirection, NowSortBackupTaskType, sort_id, VMListBackupCloneTable);
            $('#Div_VMBackup_Clone_List').html(CreateVMBackupTaskHtmlBTable());
            BackupTaskprogress();
            $('#Div_VMBackup_Clone_List').tooltip({
                position: { my: "left top+0", at: "left bottom", collision: "flipfit" }
            });
            // $.each(VMListBackupCloneTable,function(index,element){
            //     if(element['status'] >= 0 && element['status'] !== 3){
            //         $('#progress' + element['id']).progressbar({
            //             value: Math.floor( element['process'] )
            //         });                               
            //         $('#progressText' + element['id']).text(element['process'] + '%');                            
            //     }
            // });
            CheckForPollingListBackupTask();
        }
        catch (e) {
            CheckForPollingListBackupTask();
        }
    });
    request.fail(function (jqxhr, textStatus) {
        CheckForPollingListBackupTask();
    });
}


function Dialog_RestoreSelectRAID() {
    $("#dialog_RestoreSelectRaid").dialog({
        title: LanguageStr_Service.SelectRAID,
        modal: true,
        resizable: false,
        draggable: true,
        autoOpen: false,
        height: 350,
        width: 500,
        dialogClass: "no-close",
        closeOnEscape: false
    });
    
    $('#btn_close_Restore').click(function (event) {
        event.preventDefault();
        $( "#dialog_RestoreSelectRaid" ).dialog('close');                    
    });      

    $('#btn_Restore_confirm').click(function (event) {
        event.preventDefault();
        $.each($('.SelectRAIDID'), function (index, element) {            
            tempbkid = $(element).attr('id').slice(11);
            tempraidid = $('#combo_SelectRAID_'+tempbkid).scombobox('val');
            WaitRAIDRestore[tempbkid] = new WaitRAIDRestoreClass(tempraidid);
        }); 
        WaitRestoreVDarr();
        $( "#dialog_RestoreSelectRaid" ).dialog('close');
    });    

}

// --------------------------------------------- Backup Task List畫面顯示 Start---------------------------------------------
function CreateVMBackupTaskTable(JsonData) {
    VMListBackupCloneTable = [];
    VMListBackupCloneDestName = [];
    var tmpCompleteCount = 0;
    var tmpbkState = 'none';
    $.each(JsonData, function (index, element) {
        var start_time = '';
        var end_time = '';

        if (typeof (element['StartTime']) !== 'undefined') {
            start_time = element['StartTime'];
        }
        if (typeof (element['EndTime']) !== 'undefined') {
            end_time = element['EndTime'];
        }
        if (element['State'] === 0) {
            tmpCompleteCount++;
            // stopBlink(); // 啟動後，在VD進行傳輸時，按鈕閃滅 2017.03.10 william
        }
        // if (element['State'] === 2) { // 啟動後，在VD進行傳輸時，按鈕閃滅 2017.03.10 william
        //     switch (element['Type']) {
        //         case 201:
        //             tmpbkState = 'backup';
        //             func_Blink(tmpbkState);
        //             break;
        //         case 202:
        //         case 203:
        //             tmpbkState = 'restore';
        //             func_Blink(tmpbkState);
        //             break;
        //         default:
        //             tmpbkState = 'none';
        //     }
        // }
        var tmp_vmbackup_clone_class = new VMBackupCloneClass(element['ID'], element['State'], CreateBackupTaskTypeText(element['Type']), element['Source'], element['Dest'], start_time, end_time, element['Process']);
        VMListBackupCloneTable.push(tmp_vmbackup_clone_class);
        VMListBackupCloneDestName[element['ID']] = tmp_vmbackup_clone_class;
    });
    if (tmpCompleteCount != taskpollingcount) {
        taskpollingcount = tmpCompleteCount;
        GetBackupForCreateTableList();
    }

};

function func_Blink(tmpbkState) {
    switch (tmpbkState) {
        case 'none':
            break;
        case 'backup':
            divtoBlink(1);
            break;
        case 'restore':
            divtoBlink(2);
            break;
    }
}

var Backup_VDtransport;
function divtoBlink(type) {
    var elem_type;
    if (type == 1) {
        Backup_VDtransport = setInterval(function () {
            $('#BackupVDBtn').toggleClass("Backup_transBlink");
        }, 500);
    }
    else {
        Backup_VDtransport = setInterval(function () {
            //  el.style.backgroundColor = 'rgba(62,198,229,'+Math.abs(Math.sin(ofs))+')';
            //  ofs += 0.01;
            $('#RestoreVDBtn').toggleClass("Backup_transBlink");
        }, 500);
    }
}

function stopBlink() {
    clearInterval(Backup_VDtransport);
    $('#BackupVDBtn').removeClass("Backup_transBlink");
    $('#RestoreVDBtn').removeClass("Backup_transBlink");
}

function CreateBackupTaskTypeText(input_type) {
    var type = '';
    switch (input_type) {
        case 201:
            type = LanguageStr_Service.VM_BackupType_backup;
            break;
        case 202:
            type = LanguageStr_Service.VM_BackupType_restorenew;
            break;
        case 203:
            type = LanguageStr_Service.VM_BackupType_restoresame;
            break;
        case 204:
            type = LanguageStr_Service.VM_BackupType_schedulebackup;
            break;
        default:
            type = input_type;
    };
    return type;
}

function CreateVMBackupTaskHtmlBTable() {
    var HtmlStr = '';
    var no = 0;
    $.each(VMListBackupCloneTable, function (index, element) {
        no = index + 1;
        if (index === VMListBackupCloneTable.length - 1)
            HtmlStr += '<div class = "Div_VMRow LastVMRow">';
        else
            HtmlStr += '<div class = "Div_VMRow">';
        HtmlStr += '<H4 class = "TaskList_ItemNo_Row AbsoluteLayout RowField" >' + no + '</H4>';
        HtmlStr += '<H4 class = "VMBackupList_Task_Type_Row AbsoluteLayout RowField">' + element['type'] + '</H4>';
        HtmlStr += '<H4 class = "VMBackupList_Task_source_Row AbsoluteLayout RowField" title="' + element['source'] + '">' + element['source'] + '</H4>';
        HtmlStr += '<H4 class = "VMBackupList_Task_dest_Row AbsoluteLayout RowField" title="' + element['dest'] + '">' + element['dest'] + '</H4>';
        HtmlStr += '<H4 class = "VMBackupList_Task_process_Row AbsoluteLayout RowField">' + CreateBackupProgressHTML(element['state'], index) + '</H4>';
        HtmlStr += '<H4 id="bkclonestatus' + element['id'] + '" class = "VMBackupList_Task_State_Row AbsoluteLayout RowField">' + CreateVMStatusText(element['state']) + '</H4>';
        HtmlStr += '<H4 id="start_time' + element['id'] + '" class = "VMBackupList_Task_StartTime_Row AbsoluteLayout RowField">' + element['start_time'] + '</H4>';
        HtmlStr += '<H4 id="end_time' + element['id'] + '" class = "VMBackupList_Task_EndTime_Row AbsoluteLayout RowField">' + element['end_time'] + '</H4>';
        HtmlStr += '</div>';
    });
    return HtmlStr;
};

function CreateBackupProgressHTML(state, index) {
    var HtmlProgressStr = '';
    if (state === 2) {
        HtmlProgressStr += '<div>';
        HtmlProgressStr += '<div>';
        HtmlProgressStr += '<div style="position: relative;margin-top:5px">';
        HtmlProgressStr += '<div id="bkprogress' + index + '" class="progressbar_clone">';
        HtmlProgressStr += '<div id="bkprogressText' + index + '" class="progress-label" style="width:180px;line-height:20px"></div>';
        HtmlProgressStr += '</div>';
        HtmlProgressStr += '</div>';
        HtmlProgressStr += '</div>';
        HtmlProgressStr += '</div>';
    }
    return HtmlProgressStr;
}

function BackupTaskprogress() {
    $.each(VMListBackupCloneTable, function (index, element) {
        if (element['state'] === 2) {
            $('#bkprogress' + index).progressbar({
                value: Math.floor(element['process'])
            });
            $('#bkprogressText' + index).text(element['process'] + '%');
        }
    });
}

function CreateVMStatusText(status) {
    if (status < 0) {
        return LanguageStr_VM.VM_Task_Failed;
    }
    else if (status === 0) {
        return LanguageStr_VM.VM_Task_Completed;
    }
    else if (status === 1) {
        return LanguageStr_VM.VM_Task_Waiting;
    }
    else if (status === 2) {
        return LanguageStr_VM.VM_Task_Cloning;
    }

};
// --------------------------------------------- Backup Task List畫面顯示 End---------------------------------------------


function BackupTaskHederClickSort() {
    $('.BKTaskHeader').click(function (event) {
        StopBackupPolling();
        event.preventDefault();
        var th = $(this);
        var eSortType = SortType.String;
        var eSortDirection = SortDirection.ASC;
        if (th.attr('id') === "BKListState")
            eSortType = SortType.Num;
        if (th.hasClass('asc')) {
            $('.BKTaskHeader').removeClass('asc');
            $('.BKTaskHeader').removeClass('desc');
            $('.label_bktask_arrow').html('');
            $('#bktask' + th.attr('id') + '1').html('&#9660;');
            th.addClass('desc');
            eSortDirection = SortDirection.Desc;
        }
        else {
            $('.BKTaskHeader').removeClass('asc');
            $('.BKTaskHeader').removeClass('desc');
            $('.label_bktask_arrow').html('');
            $('#bktask' + th.attr('id') + '1').html('&#9650;');
            th.addClass('asc');
            eSortDirection = SortDirection.ASC;
        }
        NowSortBackupTaskType = eSortType;
        NowSortBackupTaskDirection = eSortDirection;
        NowSortBackupTaskDOMID = th.attr('id');
        var sort_id = getSortID();
        SortList(eSortDirection, eSortType, sort_id, VMListBackupCloneTable);
        $('#Div_VMBackup_Clone_List').html(CreateVMBackupTaskHtmlBTable());
        BackupTaskprogress();
        $('#Div_VMBackup_Clone_List').tooltip({
            position: { my: "left top+0", at: "left bottom", collision: "flipfit" }
        });
        StartBackupPolling(true, true);
    });
}

function getSortID() {

    var sort_id = 'start_time';

    if (NowSortBackupTaskDOMID === 'BKListType')
        sort_id = 'type';
    if (NowSortBackupTaskDOMID === 'BKListSourcVD')
        sort_id = 'source';
    if (NowSortBackupTaskDOMID === 'BKListDestVD')
        sort_id = 'dest';
    if (NowSortBackupTaskDOMID === 'BKListState')
        sort_id = 'state';
    if (NowSortBackupTaskDOMID === 'BKListStartTime')
        sort_id = 'start_time';
    if (NowSortBackupTaskDOMID === 'BKListEndTime')
        sort_id = 'end_time';
    return sort_id;
}
/*=================== (9)Task 2017.03.01 End ===================*/
/**************************************************************************/
/*                                                                        */
/*                           Relay Ajax Rule建立                           */
/*                                                                        */
/**************************************************************************/
// 1. 檢查 Backup LUN 
function Ajax_CheckBackupLUN(type, ip_or_url, username, password, targetname_or_port) {
    var jsonrequest = '{';
    if(type==1) {
        jsonrequest += '"Type":' + type + ',';
        jsonrequest += '"IP":"' + ip_or_url + '",';
        jsonrequest += '"Username":"' + username + '",';
        jsonrequest += '"Password":"' + password + '",';
        jsonrequest += '"TargetName":"' + targetname_or_port + '"';
    } else if(type==2) {
        jsonrequest += '"Type":' + type + ',';
        jsonrequest += '"MountPath":"' + ip_or_url + '",';
        jsonrequest += '"Username":"' + username + '",';
        jsonrequest += '"Password":"' + password + '",';
        jsonrequest += '"Port":' + targetname_or_port;
    }
    jsonrequest += '}';
    var request = CallAjax("/manager/backup/check", jsonrequest, "", "POST");
    return request;
};
// 2. Set Backup Mapping 
function Ajax_SetBackupMapping(type, ip_or_url, username, password, targetname_or_port, desc, action) {
    var jsonrequest = '{';
    if(type==1) {    
        jsonrequest += '"Type":' + type + ',';
        jsonrequest += '"IP":"' + ip_or_url + '",';
        jsonrequest += '"Username":"' + username + '",';
        jsonrequest += '"Password":"' + password + '",';
        jsonrequest += '"TargetName":"' + targetname_or_port + '",';
        jsonrequest += '"Desc":"' + desc + '",';
        jsonrequest += '"Action":' + action + '';
    } else if(type==2) {
        jsonrequest += '"Type":' + type + ',';
        jsonrequest += '"Username":"' + username + '",';
        jsonrequest += '"Password":"' + password + '",';
        jsonrequest += '"MountPath":"' + ip_or_url + '",';        
        jsonrequest += '"Port":' + targetname_or_port + ',';        
        jsonrequest += '"Desc":"' + desc + '",';        
        jsonrequest += '"Action":' + action;        
    }        
    jsonrequest += '}';
    var request = CallAjax("/manager/backup", jsonrequest, "", "POST");
    return request;
};
// 3. 取得全部 Backup 資訊
function Ajax_ListBackup() {
    var request = CallAjax("/manager/backup", "", "json", "GET");
    return request;
}
// 4. 刪除 backup 
function Ajax_DeleteBackup() {
    var request = CallAjax("/manager/backup", "", "", "DELETE");
    return request;
};
// 5. 建立 VD backup 
function Ajax_VDBackup(vdid) {
    var jsonrequest = '{';
    jsonrequest += '"VDID":"' + vdid + '"';
    jsonrequest += '}';
    var request = CallAjax("/manager/vd/backup", jsonrequest, "", "POST");
    return request;
};
// 6. 復原 VD  
function Ajax_VDRestore(username, vdname, sourcevdid, backupvdid, backupvdname, raidid) {
    var jsonrequest = '{';
    // jsonrequest += '"Username": "' + username + '",';
    jsonrequest += '"Username": "Temporary_User",';
    jsonrequest += '"VDName": "' + vdname + '",';
    jsonrequest += '"SourceVDID": "' + sourcevdid + '",';
    jsonrequest += '"BackupVDID": "' + backupvdid + '",';
    jsonrequest += '"BackupVDName": "' + backupvdname + '"';
    if(raidid.length > 0)
        jsonrequest += ',"RAIDID": ' + raidid;    
    jsonrequest += '}';
    var request = CallAjax("/manager/vd/restore", jsonrequest, "", "POST");
    return request;
};
// 7. 刪除 backup'VD 
function Ajax_DeleteBackupVD(backupvdid) {
    var request = CallAjax("/manager/backup/vd/" + backupvdid, "", "", "DELETE");
    return request;
};

// 8. 清除備份任務
function Ajax_ClearBackupTask() {
    var request = CallAjax("/manager/task/backup", "", "", "DELETE");
    return request;
};
// 9. 備份任務List
function Ajax_VMListBackupTask() {
    var request = CallAjax("/manager/task/backup", "", "json", "GET");
    return request;
};
// 10. 取得全部 Backup VD資訊
function Ajax_ListBackupVD() {
    var request = CallAjax("/manager/backup/vd", "", "json", "GET");
    return request;
}
// 11.
function Ajax_BackupListVDs_SendCommand() {
    var request = CallAjax("/manager/vd_size", "", "json", "GET");
    return request;
}
// 12. 連線時可作格式化
function Ajax_FormatBackup() {
    var request = CallAjax("/manager/backup/format", "", "", "PUT");
    return request;
}
// Check VD Disk Mapping Volume For Restore 
function Ajax_CheckMappingVolume(BackupVDID) {
    var request = CallAjax("manager/vd/" + BackupVDID + "/restore/check", "", "json", "GET");
    return request;
}
/**************************************************************************/
/*                                                                        */
/*                                  功能                                  */
/*                                                                        */
/**************************************************************************/
function ChapChkBoxImgClick() {
    if ($('#chk_bkchap').attr("src") == "img/checkbox_empty.png") {
        $('input[id="backup_usename"]').prop("disabled", true);
        $('input[id="backup_password"]').prop("disabled", true);
        $('#backup_usename').val('');
        $('#backup_password').val('');
    }

    $('.bkchap').bind("change click", function () {
        if (!BackupMgtEnableFlag) {
            if ($('#chk_bkchap').attr("src") == "img/checkbox_empty.png") {
                $('#chk_bkchap').attr("src", "img/checkbox.png");
                $('input[id="backup_usename"]').prop("disabled", false);
                $('input[id="backup_password"]').prop("disabled", false);
                $('#btn_checkLUN_Connect').button("disable");
            }
            else if ($('#chk_bkchap').attr("src") == "img/checkbox.png") {
                $('#chk_bkchap').attr("src", "img/checkbox_empty.png");
                $('input[id="backup_usename"]').prop("disabled", true);
                $('input[id="backup_password"]').prop("disabled", true);
                $('#backup_usename').val('');
                $('#backup_password').val('');
                if ($('#backup_iscsiip').val().length !== 0 && $('#backup_targetname').val().length !== 0)
                    $('#btn_checkLUN_Connect').button("enable");
                else
                    $('#btn_checkLUN_Connect').button("disable");
            }
        }
    });


}

function CheckBackupState(input_state, input_desc, ip_or_url, username, password, targetname_or_port, type) {
    var state = '';

    switch (input_state) {
        case 0:
            state = LanguageStr_Service.Service_CheckBackupState0;
            $("#checkLUNstate_text").text(state);
            $('#Backup_Use_btn').button("option", "disabled", true);
            $('#Backup_Format_btn').button("option", "disabled", false);
            $("#checkLUNdesc_text").text(input_desc);
            $("#Backup_Desc").val(input_desc);
            CreateLUNConnection();
            break;
        case 1:
            state = LanguageStr_Service.Service_CheckBackupState1;
            $("#checkLUNstate_text").text(state);
            $('#Backup_Use_btn').button("option", "disabled", true);
            $('#Backup_Format_btn').button("option", "disabled", false);
            $("#checkLUNdesc_text").text(input_desc);
            $("#Backup_Desc").val(input_desc);
            CreateLUNConnection();
            break;
        case 2:
            state = LanguageStr_Service.Service_CheckBackupState2;
            $("#checkLUNstate_text").text(state);
            $('#Backup_Use_btn').button("option", "disabled", true);
            $('#Backup_Format_btn').button("option", "disabled", false);
            $("#checkLUNdesc_text").text(input_desc);
            $("#Backup_Desc").val(input_desc);
            CreateLUNConnection();
            break;
        case 3:
            state = LanguageStr_Service.Service_CheckBackupState3;
            $("#checkLUNstate_text").text(state);
            $('#Backup_Use_btn').button("option", "disabled", false);
            $('#Backup_Format_btn').button("option", "disabled", false);
            $("#checkLUNdesc_text").text(input_desc);
            $("#Backup_Desc").val(input_desc);
            CreateLUNConnection();
            break;
        case 4:
            state = LanguageStr_Service.Service_CheckBackupState4;
            BackupUse(type, ip_or_url, username, password, targetname_or_port, input_desc, 1);
            break;
        case 5:
            state = LanguageStr_Service.Service_CheckBackupState5;
            $("#checkLUNstate_text").text(state);
            $('#Backup_Use_btn').button("option", "disabled", true);
            $('#Backup_Format_btn').button("option", "disabled", false);
            $("#checkLUNdesc_text").text(input_desc);
            $("#Backup_Desc").val(input_desc);
            CreateLUNConnection();
            break;
        default:
            state = input_state;
    };
    return state;
};

function blockPage() {
    $('.Div_WaitAjax').dialog('open');
};

function stopPage() {
    $('.Div_WaitAjax').dialog('close');
};

function InitBackupMgtSelectAllCheckBox() {
    $('.iCheckBoxAllSourceVDSelect').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });
    $('.iCheckBoxAllSourceVDSelect').on('ifChanged', function (event) {
        var element = $(this);
        if (element.prop('checked')) {
            $('.iCheckBoxSourceVDSelect').iCheck('check');
        }
        else {
            $('.iCheckBoxSourceVDSelect').iCheck('uncheck');
        }
    });
    $('.iCheckBoxAllBackUpVDSelect').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });
    $('.iCheckBoxAllBackUpVDSelect').on('ifChanged', function (event) {
        var element = $(this);
        if (element.prop('checked')) {
            $('.iCheckBoxBackupVDSelect').iCheck('check');
        }
        else {
            $('.iCheckBoxBackupVDSelect').iCheck('uncheck');
        }
        EnableDisableSelectBackupMgtActionButton();
    });
}

function EnableDisableSelectBackupMgtActionButton() {
    if ($('.iCheckBoxBackupVDSelect:checked').length > 0) {
        $('.btn_BackupMgt_select_operation').button("enable");
    }
    else {
        $('.btn_BackupMgt_select_operation').button("disable");
    }
}

function LoginBackupBtnEnable() {
    $('#bkformlogin :input').bind("change keyup", function () {
        if ($('#chk_bkchap').attr("src") == "img/checkbox_empty.png") {
            if ($('#backup_iscsiip').val().length !== 0 &&
                $('#backup_targetname').val().length !== 0) {
                $('#btn_checkLUN_Connect').button("enable");
                $('#Backup_Disconnect_btn').hide();
            }
            else {
                $('#btn_checkLUN_Connect').button("disable");
                $('#Backup_Disconnect_btn').hide();
            }
        }
        else if ($('#chk_bkchap').attr("src") == "img/checkbox.png") {
            if ($('#backup_iscsiip').val().length !== 0 &&
                $('#backup_targetname').val().length !== 0 &&
                $('#backup_usename').val().length !== 0 &&
                $('#backup_password').val().length !== 0) {
                $('#btn_checkLUN_Connect').button("enable");
                $('#Backup_Disconnect_btn').hide();
            }
            else {
                $('#btn_checkLUN_Connect').button("disable");
                $('#Backup_Disconnect_btn').hide();
            }
        }

        if($('#backup_nasurl').val().length !== 0 &&
           $('#backup_nas_usename').val().length !== 0 &&
           $('#backup_nas_password').val().length !== 0) {
           $('#btn_nas_Connect').button("enable");
           $('#btn_nas_Disconnect').hide();           
        } else {
           $('#btn_nas_Connect').button("disable");
           $('#btn_nas_Disconnect').hide();    
        }
    });
}