var AccessKey = "d04e11abad23ade9c73170178f47ebee";
var sess_val = "";
var AccessKeyTag = "<AccessKey>d04e11abad23ade9c73170178f47ebee</AccessKey>";
var Const_TheIdUsedByOtherTable = "11";
var Const_TheNameIsExist = "66";
var Const_EditToExistName = "13";

// Ajax 儲存 Privilege
var PrivilegeLevel = [];
var PrivilegeCompany = [];
var PrivilegeDepartment = [];
var PrivilegeTitle = [];

// Ajax 儲存 Disk
var DiskList = [];

// Ajax 儲存 User
var UserList = [];

// Ajax 儲存 User
var NewUserAccount = "";
var NewUserQuota = "";
var NewUserPassword = "";
var NewUserEmail = "";
var NewUserLastName = "";
var NewUserFirstName = "";
var NewUserCompany = "";
var NewUserDepartment = "";
var NewUserTitle = "";
var NewUserLevel = "";

var SMTP_Info = [];



var CanBuildDiskNum = 0;
var CanExpandOrHotspareDiskNum = 0;
var CanHotspareDiskNum = 0;
var RaidID = 0;
var SelectedUserID = 0;
var ChkIfDataReadFinish_Counter = 0;
var ChkIfDataReadFinish_TimeOut = 0;

var OverInSys = 1;
var OverInAccount = 2;

var oldIpv4;
var oldIpv4Mask;
var oldIpv4Gateway;
var oldIpv6;
var oldIpv6Prefix;
var oldIpv6Gateway;
var oldIpv4Dns1;
var oldIpv4Dns2;
var oldStorageName;
var oldTimeZone;
var oldNtp;

var DebugMsg = false;
var StartUp_DataRead = true;

var IsRaidCreatedFlag = false;
var IsUserCreatMode = false;
var IsRaidDoing = false;

var oUISystem = {};
var oUIPerformance = {};
var oRelayAjax = {};
var oUIUpgrade = {};
var oUISystemInfo = {};
var oUISystemHostName = {};
var oUILop = {};
var oUIVM = {};
var oiSCSI = {};
var oUIError = {};
var oUIClusterInfo = {};
var oUIException = {};

var CurrentVMWorkType;
var CurrentVMWorkRebootCount;

var isAnotherAdminLogin;

var CountTotalVM;
var CountPoweronVM;
var CountOnlineVM;
var CountSuspendVM;
var VMListTable = [];
var VMListName = [];
var SeedListTable = [];
var SeedListName = [];
var BornedListTable = [];
var BornedListName = [];
var VMCheckName = [];
var VMListCloneTable = [];
var VMListCloneDestName = [];

var DiskListTable = [];
var DiskNoSortListTable = [];

var IPList;
var iSCSIPIndexList;
var GatewayList;
var DNSList;
var vSwitchList; // vSwitch UI介面功能實作 2017.01.23
var SnapshotList; // Snapshot UI介面功能實作 2017.01.25
var ScheduleList; // Schedule UI介面功能實作 2017.02.09
var JobsList; // Schedule UI介面功能實作 2017.02.10
var VDStandbyJobsList; // Schedule Jobs介面功能實作 2017.02.15
var VDForSnapshotJobsList; // Schedule Jobs介面功能實作 2017.02.15
var VDForBackupJobsList; // 備份 Jobs介面功能實作 2017.02.15
// var OrgSourceVDList; // 備份管理介面功能實作 2017.03.02
// var OrgRestoreVDList; // 備份管理介面功能實作 2017.03.02
var OrgMultiBackupMgtListBoth; // 備份管理介面功能實作 2017.03.02
var OrgMultiBackupMgtListSource; // 備份管理介面功能實作 2017.03.02
var OrgMultiBackupMgtListBk; // 備份管理介面功能實作 2017.03.02
var ScheduleJobsList; // Schedule Jobs介面功能實作 2017.02.15
var TempJobsList; // Schedule Jobs介面功能實作 2017.02.15
var BackupList = []; // Backup介面功能實作 2017.02.21
var BackupScheduleList; // Backup Schedule UI介面功能實作 2017.02.23
var VMListSnapshotCloneTable = []; // Snapshot Task List 功能實作 2017.02.07
var VMListSnapshotCloneDestName = []; // Snapshot Task List 功能實作 2017.02.07
var VMListBackupCloneTable = []; // backup Task List 功能實作 2017.02.07
var VMListBackupCloneDestName = []; // backup Task List 功能實作 2017.02.07

var now_page_id = '';
var cpu_mhz;


var SortDirection = {
    ASC: 0,
    Desc: 1
};
var SortType = {
    Num: 0,
    String: 1
};

var RAIDCP_Notify_Interval;
var RAIDCP_Status;
var Alarm_Notify_Interval;
var Alarm_Status;
var VDIIP;

var raid_notify_usage;
var raid_notify_toal;
var raid_notify_used;
var checkLogin;

var CephRaidList;
var CephRaidListForPerformance;
var GetCephRaidUsage;

var SysCfg_lang_ver; // 2018.05.24 william 列表標題及內容的文字位置調整

function enable_custom_button(btn) {
    btn.removeClass('disable_log_btn');
    btn.addClass('log_btn');
}

function disable_custom_button(btn) {
    btn.removeClass('log_btn');
    btn.addClass('disable_log_btn');
}

function add_two_fixed_float(val) {
    var rtn = new Number(val);
    return rtn.toFixed(2)
}

function add_comma_of_number(o_input) {
    return o_input.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function formatBytes(bytes, decimals) {
    if (bytes == 0)
        return '0 Bytes';
    var k = 1024, // 由1000修改1024 2017.03.14 william
        dm = decimals + 1 || 3,
        sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
        i = Math.floor(Math.log(bytes) / Math.log(k));
    return add_comma_of_number(parseFloat((bytes / Math.pow(k, i)).toFixed(dm))) + ' ' + sizes[i];
}

function formatGB(GB, decimals) {
    var k = 1024, // 由1000修改1024 2017.03.14 william
        dm = decimals + 1 || 3,
        // sizes = ['GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
        i = Math.floor(Math.log(GB) / Math.log(k));
    if (GB == 0)
        return 0;
    if(GB < 1)    
         return GB.toFixed(2);
    // if (GB < 1000) {
    //     return add_comma_of_number(parseFloat((GB / Math.pow(k, i)).toFixed(dm))) + ' GB';
    // }      
    return parseFloat((GB / Math.pow(k, i)).toFixed(dm));
}

function nic_throughput_change_format(input) {
    var throughput = input * 8;
    if (throughput > 1024) {
        throughput = throughput / 1024;
        throughput = add_comma_of_number(throughput.toFixed(2));
        throughput = throughput + ' Mb/s';
    }
    else if (throughput > 1024 * 1024) {
        throughput = throughput / (1024 * 1024);
        throughput = add_comma_of_number(throughput.toFixed(2));
        throughput = throughput + ' Gb/s';
    }
    else {
        throughput = add_comma_of_number(throughput.toFixed(2));
        throughput = throughput + ' Kb/s';
    }
    return throughput;
}

// 網頁載入後的準備動作
function InitialSettingAndEvent() {
    checkLogin = true;
    tabControl();
    pwdHideShow();
    oUIError = ErrorHandle.createNew();
    isAnotherAdminLogin = false;
    $('#H5_Btn_Logout').show();
    $('.tab-container').easytabs();
    $('.jquery_suspend_spinner').spinner({
        max: 60,
        min: 1,
        numberFormat: "d2"
    }).on('input', function () {
        if ($(this).data('onInputPrevented')) return;
        var val = this.value,
            $this = $(this),
            max = $this.spinner('option', 'max'),
            min = $this.spinner('option', 'min');
        // We want only number, no alpha. 
        // We set it to previous default value.         
        if (!val.match(/^[+-]?[\d]{0,}$/)) val = 5;
        this.value = val > max ? max : val < min ? min : val;
    }).on('keydown', function (e) {
        // we set default value for spinner.
        if (!$(this).data('defaultValue')) $(this).data('defaultValue', this.value);
        // To handle backspace
        $(this).data('onInputPrevented', e.which === 8 ? true : false);
    });

    $('.btn-jquery').button();
    oRelayAjax = Relay.createNew();
    RegUpdateFw();
    // william Backup和Snapshot功能新增
    // 1. backup.js
    ChapChkBoxImgClick();
    InitCheckLUNButton();
    InitBackupSettingButton();
    InitVMBKTaskButtons(); //Task List按鈕初始化

    LoginBackupBtnEnable(); // 新增儲存連線設定時的按鈕狀態判定 2017.03.21 william
    GetBackupForCreateTableList();
    InitBackupRestoreButton();
    CreateDialog_CreateLUNConnection();
    CreateDialog_BackupVDRename();
    CreateDialog_StorageConnCheck();
    BackupTaskHederClickSort();
    $('#BackupMgt_Content_body_source').tooltip({
        position: { my: "left top+0", at: "left bottom", collision: "flipfit" }
    });
    // 2. SysCfg_Schedule.js
    /*--------------------------快照排程處理--------------------------*/
    CreateDialog_CreateSchedule(); // 建立排程Dialog畫面
    InitCreateScheduleButton(); // 建立排程
    InitScheduleDeleteButton();
    InitScheduleRuleButton();
    InitScheduleJobsButton();
    /*--------------------------備份排程處理--------------------------*/
    CreateDialog_CreateBackupSchedule(); // 建立排程Dialog畫面
    InitCreateBackupScheduleButton(); // 建立排程
    InitBackupScheduleDeleteButton();
    InitBackupScheduleRuleButton();
    InitBackupScheduleJobsButton();

    $('.btn_vm_operation').click(function (event) { // 綁定清單的下拉式按鈕
        var btn_element = $(this);
        event.preventDefault();
        RebindScheduleActionButtonEvent(btn_element);
        RebindBKScheduleActionButtonEvent(btn_element);
    });
    ButtonForCreateSchedule(); // 開啟排程Dialog畫面
    ButtonForCreateBKSchedule();    // 開啟排程Dialog畫面(BK)
    CreateDatepicker(); // 建立datapicker
    elementlocation() // datapicker 的起始位置


    AutossClickSort();
    AutobkClickSort();
    // 3. SysCfg_Autoss.js
    CreateDialog_Autoss();
    InitJobSelectAllCheckBox()
    // 4. SysCfg_Autobk.js
    CreateDialog_autobk();
    InitBackupJobSelectAllCheckBox();

    // SAN連線-功能禁用警告視窗 2017.05.08 william
    Dialog_SANFunctionWarning();
    $('.San_Warning').click(function (event) { // 綁定清單的下拉式按鈕
        event.preventDefault();
        $("#dialog_SANFunctionWarning").dialog('open');
    });

    // 2018.05.24 william 列表標題及內容的文字位置調整
    ChecklangVer();

    // 將所有的 DIV 加入中間 Container
    AddDivToLayer();

    // 產生對話框



    CreateDialog_Waiting();
    CreateDialogs_Message();
    CreateDialog_CreateRAID();
    CreateDialog_VM();
    CreateDialog_VMImport();
    CreateDialog_VMCreate();
    CreateDialog_VMModify();
    CreateDialog_VMInfo();
    CreateDialog_VMClone();
    CreateDialog_iSCSI();

    CreateDialog_CreateSnapshot(); /* 手動建立SnapShot(Dialog視窗) 2017.01.11 william */
    MenuButtonMouseLeave(); /* 上方大型選單按鈕變色 2017.01.18 william */
    AccessKeyTag = "<AccessKey>" + AccessKey + "</AccessKey>";
    PrivilegeLevel[0] = LanguageStr_AdminMain.PrivilegeLevel0;
    PrivilegeLevel[1] = LanguageStr_AdminMain.PrivilegeLevel1;
    PrivilegeLevel[2] = LanguageStr_AdminMain.PrivilegeLevel2;
    PrivilegeLevel[3] = LanguageStr_AdminMain.PrivilegeLevel3;
    PrivilegeLevel[4] = LanguageStr_AdminMain.PrivilegeLevel4;


    $('#Img_Network0').hide();
    $('#Img_Network1').hide();
    $('#Img_Network2').hide();
    $('#Img_Network3').hide();

    

    // 刪除所有設計時用到的 DIV
    RemoveDesignLayoutZone();

    // 註冊所有頁面的事件，寫在這邊，是為了只註冊一次事件



    RegAllPageEvent();

    // 首頁面按鈕的事件
    RegHomePageBtnEvent();

    MenuButtonClickChangeColor($('#H5_SubTitle_SysMgr_SysInfo'), false); /* SnapShot&Schedule&Backup  按鈕變色  2017.01.18 william */
    ChangeDivToLayer($('#Div_SysCfg_SysInfo'));
    $('#tab-performance-container').easytabs('select', '#tabs1-info');
    oUIPerformance.ClearCPURAMInteval();
    oUIPerformance.ClickGETCPURAM();
    CheckAdminLogin();
    $('#img_RAID_Notify').tooltip({
        position: { my: "right+45% top+0", at: "right bottom", collision: "flipfit" }
    });
    $('#img_Alarm_Notify').tooltip({
        position: { my: "right+45% top+0", at: "right bottom", collision: "flipfit" }
    });
    // 排版
    reLocateLayout();
}

function tabControl() {
    var tabs = $('.tab-container');
    tabs.easytabs({ animate: false });
    tabs.bind("easytabs:before", function (e, clicked) {
        if (clicked.parent().hasClass('tabdisabled')) {
            return false;
        }
    });
}

function pwdHideShow() {
    $('#CurPWDpic').click(function () {
        if ($('#CurPWDpic').attr("src") == "img/HideTextWord.png") {
            $('#CurPWDpic').attr("src", "img/ShowTextWord.png");
            $('#CurPWD').attr("type", "text");
        }
        else {
            $('#CurPWDpic').attr("src", "img/HideTextWord.png");
            $('#CurPWD').attr("type", "password");
        }
    });
    $('#NewPWDpic').click(function () {
        if ($('#NewPWDpic').attr("src") == "img/HideTextWord.png") {
            $('#NewPWDpic').attr("src", "img/ShowTextWord.png");
            $('#NewPWD').attr("type", "text");
        }
        else {
            $('#NewPWDpic').attr("src", "img/HideTextWord.png");
            $('#NewPWD').attr("type", "password");
        }
    });
    $('#ConfNewPWDpic').click(function () {
        if ($('#ConfNewPWDpic').attr("src") == "img/HideTextWord.png") {
            $('#ConfNewPWDpic').attr("src", "img/ShowTextWord.png");
            $('#ConfNewPWD').attr("type", "text");
        }
        else {
            $('#ConfNewPWDpic').attr("src", "img/HideTextWord.png");
            $('#ConfNewPWD').attr("type", "password");
        }
    });
    $('#ChangeSMBPWDpic').click(function () {
        if ($('#ChangeSMBPWDpic').attr("src") == "img/HideTextWord.png") {
            $('#ChangeSMBPWDpic').attr("src", "img/ShowTextWord.png");
            $('#ChangeSMBPWD').attr("type", "text");
        }
        else {
            $('#ChangeSMBPWDpic').attr("src", "img/HideTextWord.png");
            $('#ChangeSMBPWD').attr("type", "password");
        }
    });
    $('#ConfirmSMBPWDpic').click(function () {
        if ($('#ConfirmSMBPWDpic').attr("src") == "img/HideTextWord.png") {
            $('#ConfirmSMBPWDpic').attr("src", "img/ShowTextWord.png");
            $('#ConfirmSMBPWD').attr("type", "text");
        }
        else {
            $('#ConfirmSMBPWDpic').attr("src", "img/HideTextWord.png");
            $('#ConfirmSMBPWD').attr("type", "password");
        }
    });
}

// 註冊所有頁面的事件，寫在這邊，是為了只註冊一次事件



function RegAllPageEvent() {
    oHtmlCreate = CreateHtml.createNew();
    oHtmlCreate.InitWaitDialog();

    oUILog = UILogControl.createNew(oHtmlCreate, oRelayAjax);
    oUILog.Init();
    //Log
    $('#H5_SubTitle_SysMgr_Log').click(function (event) {
        var btn_element = $(this);
        MenuButtonClickChangeColor(btn_element, true);   /* 上方大型選單按鈕變色 2017.01.18 william */
        ChangeDivToLayer($('#Div_SysCfg_Log'));
        oUILog.PageInit();
    });

    //Performance
    oUIPerformance = UIPerformanceControl.createNew(oHtmlCreate, oRelayAjax);
    oUIPerformance.Init();
    $('#H5_SubTitle_SysMgr_SysInfo').click(function (event) {
        var btn_element = $(this);
        MenuButtonClickChangeColor(btn_element, true);   /* 上方大型選單按鈕變色 2017.01.18 william */
        event.preventDefault();
        ChangeDivToLayer($('#Div_SysCfg_SysInfo'));
        $('#tab-performance-container').easytabs('select', '#tabs1-info');
        StopPageInterval();
        oUIPerformance.ClickGETCPURAM();
    });
    //System Info
    oUISystemInfo = UISysInfoControl.createNew(oHtmlCreate, oRelayAjax);
    oUISystemInfo.Init();

    $('#H5_SubTitle_SysMgr_Service').click(function (event) {
        var btn_element = $(this);
        MenuButtonClickChangeColor(btn_element, true);   /* 上方大型選單按鈕變色 2017.01.18 william */
        event.preventDefault();
        ChangeDivToLayer($('#Div_SysCfg_Service'));
        StopPageInterval();
        // 初始化排程 2017.02.20 william
        InitSchedule();
        InitBackupSetting();
        BackupListEnable(); // 備份List
        $('#tabs1-Backup_Manager').css('display', 'none');
        $('#tabs1-Backup_schedule').css('display', 'none');
        $('#tabs1-bktask-list').css('display', 'none');
        $('#tab-Backup-container').easytabs('select', '#tabs1-Backup_Setting');
        $('#tab-service-container').easytabs('select', '#tabs1-service_ScheduleSnapshot');
        //        $('#tab-service-container').easytabs('select', '#tabs1-vnc');
        /*   oUISystemInfo.ClearPWDInput();
           oUISystemInfo.GetIPList();
           oUISystemInfo.ListSMB();
           oUISystemHostName.ListHostName();*/
    });

    //VM 
    oUIVM = UIVMControl.createNew(oHtmlCreate, oRelayAjax);
    oUIVM.Init();
    $('#H5_SubTitle_SysMgr_VMSetting').click(function (event) {
        var btn_element = $(this);
        MenuButtonClickChangeColor(btn_element, true);   /* 上方大型選單按鈕變色 2017.01.18 william */
        event.preventDefault();
        ChangeDivToLayer($('#Div_SysCfg_VM'));
        StopPageInterval();
        // oUISystemInfo.GetIPList();
        oUIVM.List();
        // oUIVM.ListTest();
        $('#tab-vm-container').easytabs('select', '#tabs1-vm');
    });

    //Upgrade
    oUIUpgrade = UIUpgradeControl.createNew(oHtmlCreate, oRelayAjax);
    oUIUpgrade.Init();

    // System        
    oUISystem = UISystemControl.createNew(oHtmlCreate, oRelayAjax);;
    oUIIP = UIIPControl.createNew(oHtmlCreate, oRelayAjax);
    oUISystemHostName = UISysHostName.createNew(oHtmlCreate, oRelayAjax);
    oUISystem.Init();
    oUIIP.Init();
    oUISystemHostName.Init();
    oUIClusterInfo = UIClusterInfo.createNew(oHtmlCreate, oRelayAjax);    
    oUIClusterInfo.Init();    
    $('#H5_SubTitle_SysMgr_Network').click(function (event) {
        var btn_element = $(this);
        MenuButtonClickChangeColor(btn_element, true);   /* 上方大型選單按鈕變色 2017.01.18 william */
        event.preventDefault();
        StopPageInterval();
        ChangeDivToLayer($('#Div_SysCfg_Network'));
        oUISystem.ClearPassword();
        oUIUpgrade.Root();
        // SAN版服務啟動時，載入不同的初始頁面tab 2017.05.08 william
        var $SysMgr_Network_Selected = $('#Div_SysCfg_Network ul > li:first');
        if( $SysMgr_Network_Selected.hasClass('tabdisabled') ) { // .hasClass() returns BOOLEAN true/false
            $('#tab-container').easytabs('select', '#tabs1-time');
        }
        else {
            $('#tab-container').easytabs('select', '#tabs1-ip');
        }
        $('#tab-container').easytabs('select', '#tabs1-ip');
        oUISystem.ListTime(true);
        oUISystemInfo.ListSMB();
        oUISystemInfo.ListSSH();
        oUISystem.ListAlarm();
        oUIIP.GetIPList(); // vSwitch UI介面功能實作 2017.01.23   
        // oUIIP.GetExternalList();
        oUISystemHostName.ListHostName();
        oUIClusterInfo.ListGlusterState();
    });

    oUIException = UIExceptionControl.createNew(oHtmlCreate, oRelayAjax);
    oUIException.Init();
    $('#H5_SubTitle_Sys_ExceptionHandling').click(function (event) {
        var btn_element = $(this);
        MenuButtonClickChangeColor(btn_element, true);   /* 上方大型選單按鈕變色 2017.01.18 william */
        event.preventDefault();
        StopPageInterval();
        ChangeDivToLayer($('#Div_SysCfg_Exception'));
        oUIException.GetDNSList();
    });    

    //VMS setting /* 新增VMS IP 設定 2017.04.26 william */ 
    // $('#H5_SubTitle_SysMgr_VMSSetting').click(function (event) {
    //     var btn_element = $(this);
    //     MenuButtonClickChangeColor(btn_element, true);   /* 上方大型選單按鈕變色 2017.01.18 william */
    //     event.preventDefault();
    //     ChangeDivToLayer($('#Div_SysCfg_VMSSetting'));
    //     $('#tab-vms-container').easytabs('select', '#tabs1-vmsip');
    // });

    // Raid
    RegRaidPageEvent();

    //Disk
    InitDiskPage();
}

// 6個頁面按鈕的事件
function RegHomePageBtnEvent() {
    $('#tab-storage-container').easytabs();
    oiSCSI = UIiSCSIControl.createNew(oHtmlCreate, oRelayAjax);
    oiSCSI.Init();
    $('#H5_SubTitle_SysMgr_Storage').click(function (e) {
        $('#tab-storage-container').easytabs('select', '#tabs1-disk');
        var btn_element = $(this);
        MenuButtonClickChangeColor(btn_element, true);   /* 上方大型選單按鈕變色 2017.01.18 william */
        StopPageInterval();
        StartUp_DataRead = false;
        InitialElementForStorage();
        $('.Div_WaitAjax').dialog('open');
        StartorStopPollingDisk(true);
        Ajax_ListDisk(true);
        SendPollingListDisk();
        StartorStopPollingRAID(true);
        Ajax_GetInfo_Raid(false);
        SendPollingListRAID();
        if (iSCSI === 1) {
            oiSCSI.StartorStopPollingiSCSI(true);
            oiSCSI.ListSession();
            oiSCSI.PollingListSession();
        }
    });

    // 登出
    $('#H5_Btn_Logout').click(function (e) {
        Ajax_Logout();
    });

    $('.drop-shutdown').click(function (e) {
        var confirmable = confirm(LanguageStr_AdminMain.AskShutdown);
        if (confirmable === true) {
            Shutdown();
        }
    });

    $('.drop-reboot').click(function (e) {
        var confirmable = confirm(LanguageStr_AdminMain.AskReboot);
        if (confirmable === true) {
            Reboot();
        }
    });
}


function CheckAdminLogin() {
    var request = oRelayAjax.CheckAnotherAdminLogin();
    CallBackCheckAdminLogin(request);
}

function CallBackCheckAdminLogin(request) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        if (typeof (msg['Reboot']) !== "undefined" && msg['Reboot']) {
            $('#dialog_vm_msg_reboot').html('<h3>' + LanguageStr_AdminMain.RAID_After_Delete_Msg + '</h3>');
            $("#dialog_vm_message_reboot").dialog('open');
            return false;
        }

        GetRAIDStatus(msg["Ceph"]);
        // var RaidUsage = msg["CephUsage"]; /* 新式樣修改(藍黃)--RAID容量顯示 2016.12.13 william */
        var RaidUsage = GetCephRaidUsage;

        var RaidAlarm = parseInt(msg["RAIDAlarm"]);
        var RaidUsageMax = parseInt(msg["RAIDUsageMax"]);
        if (RaidUsage < RaidAlarm)
            Set_RAIDCP_Notify(0);
        else if (RaidUsage >= RaidAlarm && RaidUsage < RaidUsageMax)
            Set_RAIDCP_Notify(-1);
        else if (RaidUsage >= RaidUsageMax)
            Set_RAIDCP_Notify(-2);
        if (msg["Alarm"] == 1)
            Set_Alarm_Notify(1);
        else
            Set_Alarm_Notify(0);
        var tmp_raid_usage = new Number(RaidUsage);
        tmp_raid_usage = tmp_raid_usage.toFixed(2);

        raid_notify_toal = new Number(msg["CephTotal"]);
        raid_notify_toal = raid_notify_toal.toFixed(2);
        raid_notify_used = new Number(msg['CephUsed']);
        raid_notify_used = raid_notify_used.toFixed(2);

        //if (raid_notify_usage != tmp_raid_usage) {
          //  raid_notify_usage = tmp_raid_usage; /* 新式樣修改(藍黃)--RAID容量顯示 2016.12.13 william */
            // var notify_msg = LanguageStr_AdminMain.RAIDCPNotify + raid_notify_usage + '%';
            var notify_msg = '';
            for(var i = 0; i < CephRaidList.length; i++) {
                notify_msg += 'RAID '+ CephRaidList[i].raidid + ' ' + LanguageStr_AdminMain.RAIDCPNotify + CephRaidList[i].usage + '%';
                notify_msg += '<br>';
            }
            $('#img_RAID_Notify').tooltip("option", "content", notify_msg);/* 新式樣修改(藍黃)--RAID容量顯示 2016.12.13 william */
        //}
        setTimeout(function () { CheckAdminLogin(); }, 8000); // 避免頻繁的Request,將5秒改成8秒 2017.03.10 william
    });
    request.fail(function (jqxhr, textStatus) {
        if (jqxhr.status == 401) {
            if (checkLogin)
                Logout_Another_Admin_Login();
            else
                window.location.href = 'index.php';
        }
        else
            setTimeout(function () { CheckAdminLogin(); }, 8000); // 避免頻繁的Request,將5秒改成8秒 2017.03.10 william
    });
}

function GetRAIDStatus(CephRaid) {
    CephRaidList = [];
    GetCephRaidUsage = 0;
    $.each(CephRaid, function(index, element) { 
        CephRaidList[index] = new CephClass(element['RAIDID'],element['Total'],element['Used'],element['Usage']);
        if(index == 0) {
            GetCephRaidUsage = element['Usage'];
        } else {
            if(element['Usage'] > GetCephRaidUsage)
                GetCephRaidUsage = element['Usage'];
        }

    });
}

function Set_RAIDCP_Notify(status) {
    if (status !== RAIDCP_Status) {
        if (typeof (RAIDCP_Notify_Interval) !== 'undefined')
            clearInterval(RAIDCP_Notify_Interval);
        $('#div_RAID_Notify').stop(true, true).show();
        switch (status) {
            case -2:
                $('#div_RAID_Notify').css("background", "red");
                RAIDCP_Notify_Interval = setInterval("Sparkle('#div_RAID_Notify')", 1000);
                break;
            case -1:
                $('#div_RAID_Notify').css("background", "#fdda2d"); /* SnapShot&Schedule&Backup  2017.01.11 william */
                break;
            case 0:
                $('#div_RAID_Notify').css("background", "#04a804"); /* SnapShot&Schedule&Backup  2017.01.11 william */
                break;
        }
        RAIDCP_Status = status;
    }
}

function Set_Alarm_Notify(status) {
    if (status !== Alarm_Status) {
        var alarm_txt = status === 1 ? LanguageStr_AdminMain.Enable : LanguageStr_AdminMain.Disable;
        $('#img_Alarm_Notify').tooltip("option", "content", LanguageStr_AdminMain.AlarmNotify + alarm_txt);
        if (typeof (Alarm_Notify_Interval) !== 'undefined') {
            clearInterval(Alarm_Notify_Interval);
        }
        $('#div_Alarm_Notify').stop(true, true).show();
        switch (status) {
            case 1:
                $('#div_Alarm_Notify').css("background", "#04a804"); /* SnapShot&Schedule&Backup  2017.01.11 william */
                // 判斷Alar的狀態 SnapShot&Schedule&Backup  2017.01.11 william
                $('#btn_disable_alarm').css({ "border": "1px solid #add", "background": "#3ec6e5", "font-weight": "normal", "color": "#ffffff" });
                $('#btn_enable_alarm').css({ "border": "1px solid #91979c", "background": "#b3c5c5", "font-weight": "normal", "color": "#ffffff" }); // 修改 "border": "1px solid #add", "background": "#378de5", "font-weight": "normal", "color": "#026890" 2017.03.31 william
                $("#btn_disable_alarm")
                    .mouseover(function () {
                        $(this).css("background", "-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #378de5), color-stop(1, #79bbff) )");
                        $(this).css("background", "-moz-linear-gradient( center top, #378de5 5%, #79bbff 100% )");
                        $(this).css("filter", "progid:DXImageTransform.Microsoft.gradient(startColorstr='#378de5', endColorstr='#79bbff')");
                        $(this).css("background-color", "#378de5");
                        $(this).css("color", "#ffffff");
                        //$( this ).css({"background":"-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #378de5), color-stop(1, #79bbff) )","background":"-moz-linear-gradient( center top, #378de5 5%, #79bbff 100% )","filter":"progid:DXImageTransform.Microsoft.gradient(startColorstr='#378de5', endColorstr='#79bbff')","background-color":"#378de5"});
                    })
                    .mouseout(function () {
                        $(this).css({ "border": "1px solid #add", "background": "#3ec6e5", "font-weight": "normal", "color": "#ffffff" });
                    });
                $("#btn_enable_alarm")
                    .mouseover(function () {
                        $(this).css({ "border": "1px solid #91979c", "background": "#b3c5c5", "font-weight": "normal", "color": "#ffffff" });   // 修改 2017.03.31 william
                    })
                    .mouseout(function () {
                        $(this).css({ "border": "1px solid #91979c", "background": "#b3c5c5", "font-weight": "normal", "color": "#ffffff" }); // 修改 2017.03.31 william
                    });
                break;
            case 0:
                $('#div_Alarm_Notify').css("background", "#fdda2d"); /* SnapShot&Schedule&Backup  2017.01.11 william */
                // 判斷Alar的狀態 SnapShot&Schedule&Backup  2017.01.11 william
                $('#btn_enable_alarm').css({ "border": "1px solid #add", "background": "#3ec6e5", "font-weight": "normal", "color": "#ffffff" });
                $('#btn_disable_alarm').css({ "border": "1px solid #91979c", "background": "#b3c5c5", "font-weight": "normal", "color": "#ffffff" }); // 修改 2017.03.31 william
                $("#btn_enable_alarm")
                    .mouseover(function () {
                        $(this).css("background", "-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #378de5), color-stop(1, #79bbff) )");
                        $(this).css("background", "-moz-linear-gradient( center top, #378de5 5%, #79bbff 100% )");
                        $(this).css("filter", "progid:DXImageTransform.Microsoft.gradient(startColorstr='#378de5', endColorstr='#79bbff')");
                        $(this).css("background-color", "#378de5");
                        $(this).css("color", "#ffffff");
                        //$( this ).css({"background":"-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #378de5), color-stop(1, #79bbff) )","background":"-moz-linear-gradient( center top, #378de5 5%, #79bbff 100% )","filter":"progid:DXImageTransform.Microsoft.gradient(startColorstr='#378de5', endColorstr='#79bbff')","background-color":"#378de5"});
                    })
                    .mouseout(function () {
                        $(this).css({ "border": "1px solid #add", "background": "#3ec6e5", "font-weight": "normal", "color": "#ffffff" });
                    });
                $("#btn_disable_alarm")
                    .mouseover(function () {
                        $(this).css({ "border": "1px solid #91979c", "background": "#b3c5c5", "font-weight": "normal", "color": "#ffffff" }); // 修改 2017.03.31 william
                    })
                    .mouseout(function () {
                        $(this).css({ "border": "1px solid #91979c", "background": "#b3c5c5", "font-weight": "normal", "color": "#ffffff" }); // 修改 2017.03.31 william
                    });
                // Alarm_Notify_Interval = setInterval("Sparkle('#div_Alarm_Notify')",1000);
                //clearInterval(Alarm_Notify_Interval);
                break;
        }
        Alarm_Status = status;
    }
}

function Sparkle(element) {
    $(element).fadeOut(1000).fadeIn(1000);
}

function Reboot() {
    oHtmlCreate.blockPage();
    var request = oRelayAjax.Reboot();
    CallBackReboot(request);
}

function CallBackReboot(request) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        checkLogin = false;
        StopPageInterval();
        oHtmlCreate.stopPage();
        oHtmlCreate.blockPageMsg(LanguageStr_AdminMain.RebootMsg);
    });
    request.fail(function (jqxhr, textStatus) {
        checkLogin = false;
        StopPageInterval();
        oHtmlCreate.stopPage();
        oHtmlCreate.blockPageMsg(LanguageStr_AdminMain.RebootMsg);
    });
}

function Shutdown() {
    oHtmlCreate.blockPage();
    var request = oRelayAjax.Shutdown();
    CallBackShutdown(request);
}

function CallBackShutdown(request) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        checkLogin = false;
        StopPageInterval();
        oHtmlCreate.stopPage();
        oHtmlCreate.blockPageMsg(LanguageStr_AdminMain.ShutdownMsg);
    });
    request.fail(function (jqxhr, textStatus) {
        checkLogin = false;
        StopPageInterval();
        oHtmlCreate.stopPage();
        oHtmlCreate.blockPageMsg(LanguageStr_AdminMain.ShutdownMsg);
    });
}

function MenuButtonMouseLeave() /* 上方大型選單按鈕變色 2017.01.18 william */ {
    $('.MenuButton').on("mouseenter", function () {
        $('.MenuButton').removeClass('MenuButtonClickHover');
    });
}

function MenuButtonClickChangeColor(btn_element, isClick) /* 上方大型選單按鈕變色 2017.01.18 william */ {
    $.each($('.MenuButton'), function (index, element) {
        $(element).removeClass('MenuButtonClick');
    });
    if (isClick)
        btn_element.addClass('MenuButtonClickHover');
    btn_element.addClass('MenuButtonClick');
}



// 將所有的 DIV 加入中間 Container
function AddDivToLayer() {
    $('#Div_Main_LayoutContain').prepend($('#Div_SysCfg_Network'));
    $('#Div_Main_LayoutContain').prepend($('#Div_SysCfg_AccountMgr'));
    $('#Div_Main_LayoutContain').prepend($('#Div_SysCfg_PrivilegeMgr'));
    $('#Div_Main_LayoutContain').prepend($('#Div_SysCfg_SmtpMgr'));
    //	$('#Div_Main_LayoutContain').prepend( $('#Div_SysCfg_FwUpdate') );
    $('#Div_Main_LayoutContain').prepend($('#Div_SysCfg_SysInfo'));
    $('#Div_Main_LayoutContain').prepend($('#Div_SysCfg_Service'));
    $('#Div_Main_LayoutContain').prepend($('#Div_SysCfg_VM'));
    $('#Div_Main_LayoutContain').prepend($('#Div_SysCfg_Storage'));
    $('#Div_Main_LayoutContain').prepend($('#Div_SysCfg_Storage'));
    $('#Div_Main_LayoutContain').prepend($('#Div_SysCfg_Log'));
    $('#Div_Main_LayoutContain').prepend($('#Div_SysCfg_Exception'));
    // $('#Div_Main_LayoutContain').prepend($('#Div_SysCfg_VMSSetting')); /* 新增VMS IP 設定 2017.04.26 william */
    ChangeDivToLayer($('#Div_SysCfg_Network'));
}



// 將目前點選的畫面浮到最上層
function ChangeDivToLayer(obj) {
    $('#Div_MainContainBg').css("z-index", "0");
    $('#Div_SysCfg_Network').css("z-index", "0");
    $('#Div_SysCfg_Storage').css("z-index", "0");
    // $('#Div_SysCfg_AccountMgr').css( "z-index", "0" );
    // $('#Div_SysCfg_PrivilegeMgr').css( "z-index", "0" );
    // $('#Div_SysCfg_SmtpMgr').css( "z-index", "0" );
    //	$('#Div_SysCfg_FwUpdate').css( "z-index", "0" );
    $('#Div_SysCfg_SysInfo').css("z-index", "0");
    $('#Div_SysCfg_Service').css("z-index", "0");
    $('#Div_SysCfg_VM').css("z-index", "0");
    $('#Div_SysCfg_Log').css("z-index", "0");
    $('#Div_SysCfg_Exception').css("z-index", "0");
    // $('#Div_SysCfg_VMSSetting').css("z-index", "0"); /* 新增VMS IP 設定 2017.04.26 william */
    now_page_id = obj.attr('id');
    reLocateByDiv(now_page_id);
    obj.css("z-index", "10");
}




// 刪除所有設計時用到的 DIV
function RemoveDesignLayoutZone(obj) {
    $('.Div_DialogBase').hide();
    $('#Div_LayoutZone_SysCfg_Network').remove();
    // $('#Div_LayoutZone_SysCfg_SMTP').remove();
    // $('#Div_LayoutZone_SysCfg_AccountMgr').remove();
    // $('#Div_LayoutZone_SysCfg_PrivilegeMgr').remove();
    $('#Div_LayoutZone_SysCfg_FwUpdate').remove();
    $('#Div_LayoutZone_MakingElement').remove();
    $('#Div_LayoutZone_SysCfg_SysInfo').remove();
    $('#Div_LayoutZone_SysCfg_Service').remove();
    $('#Div_LayoutZone_SysCfg_VM').remove();
    $('#Div_LayoutZone_SysCfg_Storage').remove();
    $('#Div_LayoutZone_SysCfg_Log').remove();
    // $('#Div_LayoutZone_SysCfg_VMSSetting').remove(); /* 新增VMS IP 設定 2017.04.26 william */
    $('#Div_LayoutZone_SysCfg_Exception').remove();
}


function StopPageInterval() {
    if (iSCSI === 1) {
        if (typeof (oiSCSI) !== 'undefinded') {
            oiSCSI.StartorStopPollingiSCSI(false);
        }
    }
    if (typeof (oUIVM) !== 'undefinded')
        oUIVM.ClearAllVMInteval();
    if (typeof (oUIPerformance) !== 'undefinded')
        oUIPerformance.ClearCPURAMInteval();
    StartorStopPollingDisk(false);
    StartorStopPollingRAID(false);
    ClearBackupTaskInterval();
}




// 視窗變更大小就會觸發，利用 Timer 來延後



$(window).resize(function () {
    setTimeout("reLocateLayout()", 500);
});

function reLocateByDiv(id) {
    switch (id) {
        case 'Div_SysCfg_Network':
            relocateSystemsettingLayout();
            break;
        case 'Div_SysCfg_VM':
            relocateVDISettingLayout();
            break;
        case 'Div_SysCfg_Storage':
            relocateStorageSettingLayout();
            break;
        case 'Div_SysCfg_Log':
            relocateLogSettingLayout();
            break;
        case 'Div_SysCfg_Service':              // 新增Service layout resize的功能 2017.02.08 william
            relocateServiceSettingLayout();
            break;
        case 'Div_SysCfg_SysInfo':              // 新增System Infomation layout resize的功能 2017.03.03 william
            relocateSysInfoSettingLayout();
            break;
        case 'Div_SysCfg_Exception':              // 新增System Infomation layout resize的功能 2017.03.03 william
            relocateExceptionsettingLayout();
            break;            
        // case 'Div_SysCfg_VMSSetting': /* 新增VMS IP 設定 2017.04.26 william */              
        //     relocateVMSsettingLayout();
        //     break;                
    }
}

function reLocateLayout() {
    var NewTop;

    //var BrowserHeight = self.innerHeight;	
    var BrowserHeight = $(window).height();

    $('#Div_Main_LayoutContain').height(BrowserHeight - 130);
    $('.Div_Design_Layout').height(BrowserHeight - 130);

    reLocateByDiv(now_page_id);


    NewTop = BrowserHeight - 30;
    $('#Div_Main_LayoutBottom').css("top", NewTop);

}

function relocateLogSettingLayout() {
    var BrowserHeight = $(window).height();
    $('#div_log_table').height(BrowserHeight - 270);  /* 修改 260 2017.03.03 william */
    $('#Div_VM_Log_List').height(BrowserHeight - 308);   /* 修改 320px -> 298 -> 293 2017.03.03 william */
    var div = document.getElementById('div_log_scroll');
    var hasHorizontalScrollbar = div.scrollWidth > div.clientWidth;
    if (hasHorizontalScrollbar) {
        $('#div_log_select').css({ "width": "auto", "margin-left": "10px", "margin-right": "10px" });
        $('#div_log_page').css({ "width": "auto", "margin-left": "10px", "margin-right": "10px" });
    }
    else {
        $('#div_log_select').css({ "width": "98%", "margin-left": "auto", "margin-right": "auto" }); /* 修改 2017.03.02 william 原值：width": "1150px"*/
        $('#div_log_page').css({ "width": "98%", "margin-left": "auto", "margin-right": "auto" }); /* 修改 2017.03.02 william 原值：width": "1150px"*/
    }

}


function relocateSystemsettingLayout() {
    var BrowserHeight = $(window).height();
    var DivMainContainHeight = $('.Div_MainContain').height();
    // $('#tabs1-ip').height(BrowserHeight - 260); // vSwitch william 2017.01.19 修改 原值：300
    $('#tabs1-ip').height(DivMainContainHeight - 90);
    if (iSCSI === 1)
        $('#tabs1-iscsi-ip').height(BrowserHeight - 300);
    // $('#tabs1-external-ip').height(BrowserHeight - 260); // vSwitch william 2017.01.19 修改 原值：300
    $('#tabs1-external-ip').height(DivMainContainHeight - 90);
    // $('#tabs1-password').height(BrowserHeight - 260); // vSwitch william 2017.01.19 修改 原值：300
    $('#tabs1-password').height(DivMainContainHeight - 90);
    // $('#tabs1-system-update').height(BrowserHeight - 260); // vSwitch william 2017.01.19 修改 原值：300
    $('#tabs1-system-update').height(DivMainContainHeight - 90);
    // $('#tabs1-host-name').height(BrowserHeight - 260); // vSwitch william 2017.01.19 修改 原值：300
    $('#tabs1-host-name').height(DivMainContainHeight - 90);
    // $('#tabs1-alarm').height(BrowserHeight - 260); // vSwitch william 2017.01.19 修改 原值：300
    $('#tabs1-alarm').height(DivMainContainHeight - 90);
    // $('#tabs1-time').height(BrowserHeight - 260); // vSwitch william 2017.01.19 修改 原值：300
    $('#tabs1-time').height(DivMainContainHeight - 90);
    var tabs1timeHeight = $('#tabs1-time').height();
    $('#div_time').height(tabs1timeHeight - 10); // vSwitch william 2017.01.19 修改 原值：320
    //$('#div_ip_content').height(BrowserHeight-290);   // vSwitch william 2017.01.19 修改 原值：350
    var tabs1ipHeight = $('#tabs1-ip').height();
    $('#div_ip_field').height(tabs1ipHeight - 35);   // vSwitch william 2017.02.03 
    // $('#tabs1-smb').height(BrowserHeight - 260);  // vSwitch william 2017.01.19 修改 原值：300
    $('#tabs1-smb').height(DivMainContainHeight - 90);
    var tabs1smbHeight = $('#tabs1-smb').height();
    $('#div_smb').height(tabs1smbHeight - 5);
    $('#tabs1-ssh').height(DivMainContainHeight - 90);
    $('#tabs1-cluster').height(DivMainContainHeight - 90);
}

function relocateVDISettingLayout() {
    var BrowserHeight = $(window).height();
    var DivMainContainHeight = $('.Div_MainContain').height();
    //var BrowserWidth = $(window).width();
    //$('#div_vm_scroll').width(BrowserWidth -150);    
    //$('#div_seed_scroll').width(BrowserWidth -150);    
    //$('#div_task_scroll').width(BrowserWidth -150);

    /* 由300修改270 2016.12.22 william */
    // $('#tabs1-vm').height(BrowserHeight - 290);
    $('#tabs1-vm').height(DivMainContainHeight - 115);
    // $('#tabs1-task-seed').height(BrowserHeight - 290);
    $('#tabs1-task-seed').height(DivMainContainHeight - 115);
    // $('#tabs1-borned').height(BrowserHeight - 290);
    $('#tabs1-borned').height(DivMainContainHeight - 115);
    // $('#tabs1-vm-setting').height(BrowserHeight - 290);
    $('#tabs1-vm-setting').height(DivMainContainHeight - 115);
    // $('#tabs1-task-list').height(BrowserHeight - 290);
    $('#tabs1-task-list').height(DivMainContainHeight - 115);
    // $('#tabs1-sstask-list').height(BrowserHeight - 290); // 新增 2017.03.03 william  
    $('#tabs1-sstask-list').height(DivMainContainHeight - 115);

    /* 由390修改360 2016.12.22 william */
    $('#Div_VMList').height(BrowserHeight - 368);
    $('#Div_SeedList').height(BrowserHeight - 368);
    $('#Div_VM_Clone_List').height(BrowserHeight - 368);
    $('#Div_Borned_List').height(BrowserHeight - 368);
    $('#Div_VMSnapshot_Clone_List').height(BrowserHeight - 368); // 快照任務清單功能實作 2017.02.02   
}

function relocateStorageSettingLayout() {
    var BrowserHeight = $(window).height();
    var BrowserWidth = $(window).width();    
    var DivMainContainHeight = $('.Div_MainContain').height();
    // $('#div_disk_scroll').width(BrowserWidth - 145);  // 修改磁碟管理顯示 2017.02.24 william
    // $('#div_disk_table').width(BrowserWidth -125);  // 修改磁碟管理顯示 2017.02.24 william

    // $('#div_raid_scroll').width(BrowserWidth - 150);
    $('#div_iscsi_scroll').width(BrowserWidth - 150);
    // $('#tabs1-disk').height(BrowserHeight - 260);   // 修改 300 2017.03.03 william
    $('#tabs1-disk').height(DivMainContainHeight - 90);
    // $('#tabs1-raid').height(BrowserHeight - 260);   // 修改 300 2017.03.03 william
    $('#tabs1-raid').height(DivMainContainHeight - 90);
    $('#tabs1-iscsi').height(BrowserHeight - 300);
    $('#Div_DiskList').height(BrowserHeight - 356);  // 修改 390 2017.03.03 william
    $('#Div_RAIDList').height(BrowserHeight - 356);  // 修改 390 2017.03.03 william
    $('#Div_iSCISList').height(BrowserHeight - 390);
}

function relocateServiceSettingLayout() { // 新增Service layout resize的功能 2017.02.08 william
    var BrowserHeight = $(window).height();
    var BrowserWidth = $(window).width();
    var DivMainContainHeight = $('.Div_MainContain').height();
    $('#tabs1-service_ScheduleSnapshot').height(BrowserHeight - 260);
    $('#Div_Service_Snapshot_List').height(BrowserHeight - 356);

    // $('#tabs1-service_Backup').height(BrowserHeight - 260);
    $('#tabs1-service_Backup').height(DivMainContainHeight - 90);
    // $('#tabs1-Backup_Setting').height(BrowserHeight - 320);
    $('#tabs1-Backup_Setting').height(DivMainContainHeight - 140);    
    // $('#tabs1-Backup_Manager').height(BrowserHeight - 320);
    $('#tabs1-Backup_Manager').height(DivMainContainHeight - 140);    
    // $('#tabs1-Backup_schedule').height(BrowserHeight - 320);
    $('#tabs1-Backup_schedule').height(DivMainContainHeight - 140);    
    // $('#tabs1-bktask-list').height(BrowserHeight - 320);
    $('#tabs1-Backup_schedule').height(DivMainContainHeight - 140);    
    // 針對內部再有tab，需要作2層的高度調整 2017.02.21 william
    $('#Div_VMBackup_Clone_List').height(BrowserHeight - 406); // 備份任務清單功能實作 2017.02.02   
    $('#Div_Service_BackupSchedule_List').height(BrowserHeight - 406);

    // $('.CheckBackupLUN_wrapper').height(BrowserHeight - 320);
    $('.CheckBackupLUN_wrapper').height(DivMainContainHeight - 135);

    // $('#Div_BackupMgtList').height(BrowserHeight - 400);
    $('.BackupMgt_div_body').height(BrowserHeight - 380);
    
    // $('.BackupMgt_div_source').width((BrowserWidth / 2) * 0.85);
    // $('.BackupMgt_div_backup').width((BrowserWidth / 2) * 0.85);
    // var w = $('#div_BackupMgt_scroll').width();
    // var w1 = ($('#div_BackupMgt_scroll').width()-105) / 2;
    // var w2 = (w1 / w) * 100
    // $('.BackupMgt_div_source').width( w2 + '%');
    // $('.BackupMgt_div_backup').width( w2 + '%');

    var DivBackupManagerContainHeight = $('#tabs1-Backup_Manager').height();
    // var DivBackupManagerContainWidth = $('#tabs1-Backup_Manager').width();
    $('#BackupMgt_Content_body_source').height(DivBackupManagerContainHeight - 100);
    $('#BackupMgt_Content_body_backup').height(DivBackupManagerContainHeight - 100);
    $('#div_BackupMgt_scroll').height(DivBackupManagerContainHeight - 60);

}

function relocateSysInfoSettingLayout() { // 新增System Infomation layout resize的功能 2017.03.03 william
    var BrowserHeight = $(window).height();
    var DivMainContainHeight = $('.Div_MainContain').height();
    // $('#tabs1-info').height(BrowserHeight - 260);
    $('#tabs1-info').height(DivMainContainHeight - 90);
    // $('#tabs1-cpu').height(BrowserHeight - 260);
    $('#tabs1-cpu').height(DivMainContainHeight - 90);
}

function relocateExceptionsettingLayout() {
    var BrowserHeight = $(window).height();
    var DivMainContainHeight = $('.Div_MainContain').height();
    $('#tabs1-exception_DNS').height(DivMainContainHeight - 90);
}

// function relocateVMSsettingLayout() { /* 新增VMS IP 設定 2017.04.26 william */              
//     var BrowserHeight = $(window).height();
//     var DivMainContainHeight = $('.Div_MainContain').height();
//     $('#tabs1-vmsip').height(DivMainContainHeight - 90);
//     var tabs1ipHeight = $('#tabs1-vmsip').height();
//     $('#div_vmsip_field').height(tabs1ipHeight - 35);   // vSwitch william 2017.02.03 
// }

// 切換到 網路內頁 之後要做的元件設定 
function InitialElementForNetwork() {
    // 換內頁



    ChangeDivToLayer($('#Div_SysCfg_Network'));
}

// 切換到 SMTP 之後要做的元件設定 
function InitialElementSmtp() {
    // 換內頁



    ChangeDivToLayer($('#Div_SysCfg_SmtpMgr'));
}

// 切換到 FW Update 之後要做的元件設定 
function InitialElementFwUpdate() {
    // 換內頁



    ChangeDivToLayer($('#Div_SysCfg_FwUpdate'));
}

// 切換到 Storage內頁 之後要做的元件設定 
function InitialElementForStorage() {
    // 換內頁



    ChangeDivToLayer($('#Div_SysCfg_Storage'));
}

// 切換到 磁碟內頁 之後要做的元件設定 
function InitialElementForDisk() {
    // 換內頁



    ChangeDivToLayer($('#Div_SysCfg_DiskMgr'));
}


// 切換到 RAID 之後要做的元件設定 
function InitialElementForRaid() {
    // 換內頁



    ChangeDivToLayer($('#Div_SysCfg_RaidMgr'));
}

// 切換到 使用者內頁 之後要做的元件設定 
function InitialElementForUser() {
    // 換內頁



    ChangeDivToLayer($('#Div_SysCfg_AccountMgr'));
}

// 切換到 Privilege 內頁 之後要做的元件設定 
function InitialElementForPrivilege() {
    // 換內頁



    ChangeDivToLayer($('#Div_SysCfg_PrivilegeMgr'));


    // 一共有三層，先顯示 Company 這層
    $('#Div_CompanyMgr').show();
    $('#Div_DepartmentMgr').hide();
    $('#Div_TitleMgr').hide();

    $('#H4_BtnCompany').show(500);
    $('#H4_BtnDepartment').show(500);
    $('#H4_BtnTitle').show(500);

    // Privilege 三個按鈕置中



    NewLeft = ($('.Div_MainContain').width() * 0.5 - $('#H4_BtnDepartment').width() * 0.5);
    $('#H4_BtnDepartment').css("left", NewLeft);

    NewLeft = $('#H4_BtnDepartment').position().left - 200;
    $('#H4_BtnCompany').css("left", NewLeft);

    NewLeft = $('#H4_BtnDepartment').position().left + 200;
    $('#H4_BtnTitle').css("left", NewLeft);

    $('#H4_BtnCompany').click();
}





function JSON_ChkStatus(JsonData, okMsg, errMsg) {
    if (JsonData['status'] == 0) {
        if (okMsg !== "") {
            OpenDialog_Message(okMsg);
        }

        return true;
    }
    else {
        if (errMsg !== "") {
            OpenDialog_Message(errMsg);
        }

        return false;
    }
}

function Logout_Another_Admin_Login() {
    if (!isAnotherAdminLogin) {
        isAnotherAdminLogin = true;
        removeSessionStorage();
        alert(LanguageStr_AdminMain.LogoutWarning);
        window.location.href = 'index.php';
    }
}

function removeSessionStorage() {
    if (typeof (Storage) !== "undefined") {
        sessionStorage.removeItem('token');
    }
}

// 登出
function Ajax_Logout() {

    var request = CallAjax("manager/logout", "", "");



    // 管他成功失敗全都導回首頁XD
    request.done(function (msg, statustext, jqxhr) {
        removeSessionStorage();
        window.location.href = 'index.php';
    });

    request.fail(function (jqXHR, textStatus) {
        removeSessionStorage();
        window.location.href = 'index.php';
    });
}


function CallAjax(url, data, datatype, method) {
    if (typeof (method) == 'undefined')
        method = 'POST';
    if (true == DebugMsg) {
        alert("url:" + url + ", data" + data);
    }
    var request = $.ajax
        ({
            type: method,
            url: url,
            data: data,
            dataType: datatype
        });

    return request;
}

// 開啟詢問YES或NO的對話框
function OpenDialog_QuestionYesNo(ShowTxt) {
    $('.Div_QuestionYesNo').dialog('open');
    $('.Lbl_Dialog_Message').text(ShowTxt);
}

// 開啟顯示訊息對話框



function OpenDialog_Message(ShowTxt) {
    $('.Div_Message').dialog('open');
    $('.Lbl_Dialog_Message').text(ShowTxt);
}


// 開啟顯示訊息對話框



function OpenDialog_UpdateFwYesNo(OldVer, NewVer) {
    $('.Div_UpdateFwYesNo').dialog('open');
    $('.Lbl_Dialog_UpdateFwYesNo_OldVer_Value').text(OldVer);
    $('.Lbl_Dialog_UpdateFwYesNo_NewVer_Value').text(NewVer);
}



// 建立等待對話框



function CreateDialog_Waiting() {
    $(".Div_WaitAjax").dialog(
        {
            closeOnEscape: false,
            autoOpen: false,
            resizable: false,
            height: 180,
            modal: true,
            dialogClass: "no-close",
            show:
            {
                effect: "fade",
                duration: 500
            },
            hide:
            {
                effect: "fade",
                duration: 500
            }
        });
}


// 建立等待對話框



function CreateUpdateFw_Waiting() {
    $(".Div_UpdateFwYesNo").dialog(
        {
            closeOnEscape: false,
            autoOpen: false,
            resizable: false,
            height: 350,
            width: 600,
            modal: true,
            dialogClass: 'no-close',
            open: function () {
                $(this).parent().children().children("button.ui-dialog-titlebar-close").hide();
            },
            show:
            {
                effect: "fade",
                duration: 500
            },
            hide:
            {
                effect: "fade",
                duration: 500
            },

            // 對話框有幾個按鈕，以及按了有什麼事件觸發



            buttons:
            {
                "Yes": function () {
                    Ajax_UpdateFw();
                    $(this).dialog('close');
                },
                "No": function () {
                    CleanUpdateFlag();
                    $(this).dialog('close');
                }
            }
        });
    $('.no-close .ui-button-text:contains(Yes)').text(LanguageStr_AdminMain.ButtonYes);
    $('.no-close .ui-button-text:contains(No)').text(LanguageStr_AdminMain.ButtonCancel);
}


function CreateUpdateProgressBar() {
    var progressbar = $("#progressbar"),
        progressLabel = $(".progress-label");

    progressbar.progressbar({
        value: 0
        //      change: function() {          
        //            progressLabel.text( progressbar.progressbar( "value" ) + "%" );
        //      },
        //      complete: function() {
        //        progressLabel.text( "Complete!" );
        //      }
    });

    //    progressbar.progressbar( "value", 0);
    progressLabel.text('');
}



// 建立訊息對話框



function CreateDialogs_Message() {
    $('.Div_Message').dialog(
        {
            autoOpen: false,
            resizable: false,
            width: 500,
            height: 300,
            modal: true,
            dialogClass: 'DialogMessage',
            show:
            {
                effect: 'fade',
                duration: 500
            },
            hide:
            {
                effect: 'fade',
                duration: 500
            },

            buttons:
            {
                Exit: function () {
                    $(this).dialog('close');
                }
            }
        });
    $('.DialogMessage .ui-button-text:contains(Yes)').text(LanguageStr_AdminMain.ButtonYes1);

    $('.Div_QuestionYesNo').dialog(
        {
            autoOpen: false,
            resizable: false,
            width: 500,
            height: 300,
            modal: true,
            dialogClass: 'DialogQuestion',
            show:
            {
                effect: 'fade',
                duration: 500
            },
            hide:
            {
                effect: 'fade',
                duration: 500
            },

            buttons:
            {
                "Yes": function () {
                    Ajax_DeleteRaid();
                },

                "No": function () {
                    $(this).dialog('close');
                }
            }
        });
    $('.DialogQuestion .ui-button-text:contains(Yes)').text(LanguageStr_AdminMain.ButtonYes);
    $('.DialogQuestion .ui-button-text:contains(No)').text(LanguageStr_AdminMain.ButtonCancel);

    $('.Div_CreateUser').dialog(
        {
            autoOpen: false,
            resizable: false,
            width: 900,
            height: 500,
            modal: true,
            dialogClass: 'DialogCreateUser',
            show:
            {
                effect: 'fade',
                duration: 500
            },
            hide:
            {
                effect: 'fade',
                duration: 500
            },
            buttons:
            {
                "Yes": function () {
                    if (true == GetUserInfoAndCheckInput()) {
                        if (true == IsUserCreatMode) {
                            Ajax_CreateUser();
                        }
                        else {
                            Ajax_ModifyUser();
                        }
                    }
                },

                "No": function () {
                    $(this).dialog('close');
                    //$('.H4_BtnCreateUser').fadeIn(500);
                }
            }
        });
    $('.DialogCreateUser .ui-button-text:contains(Yes)').text(LanguageStr_AdminMain.ButtonYes1);
    $('.DialogCreateUser .ui-button-text:contains(No)').text(LanguageStr_AdminMain.ButtonCancel);
}

function CreateDialog_CreateRAID() {
    $('.btn-create').button();
    $('#combolevel').scombobox({
        editAble: false,
        wrap: false
    });
    $('#iCheckTP').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });
    $('.iCheckBox_RAID_All_Disk').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });
    $('.iCheckBox_RAID_All_Disk').on('ifChanged', function (event) {
        var element = $(this);
        if (element.prop('checked')) {
            $('.iCheckBox').iCheck('check');
        }
        else {
            $('.iCheckBox').iCheck('uncheck');
        }
    });
    $("#dialog_create_raid").dialog({
        title: LanguageStr_Raid.CreateRAID_Title,
        modal: true,
        resizable: false,
        draggable: true,
        closeOnEscape: false,
        autoOpen: false,
        dialogClass: "no-close",
        height: 550,
        width: 800
    });
    $('#btn_close_create_raid').click(function (event) {
        event.preventDefault();
        $("#dialog_create_raid").dialog('close');
    });
}

function CreateDialog_VM() {
    $("#dialog_vm_message").dialog({
        title: "Attention",
        modal: true,
        resizable: false,
        draggable: true,
        autoOpen: false,
        height: 280,
        width: 570
    });

    $("#dialog_vm_message_reboot").dialog({
        title: "Attention",
        modal: true,
        resizable: false,
        draggable: true,
        autoOpen: false,
        height: 280,
        width: 570,
        dialogClass: "no-close",
        closeOnEscape: false
    });
}

function CreateDialog_VMModify() {
    $("#dialog_modify_vm_boot").dialog({
        title: LanguageStr_VM.VM_Modify + VMorVDI,
        modal: true,
        resizable: false,
        draggable: true,
        autoOpen: false,
        height: 250,
        width: 400
    });
    $("#dialog_modify_vm").dialog({
        title: LanguageStr_VM.VM_Modify + VMorVDI,
        modal: true,
        resizable: false,
        draggable: true,
        autoOpen: false,
        height: 530,
        width: 750
    });
    $('#dialog_modify_seed').dialog({
        title: LanguageStr_VM.VM_Modify + " " + LanguageStr_VM.VM_Seed,
        modal: true,
        resizable: false,
        draggable: true,
        autoOpen: false,
        height: 300,
        width: 600
    });
}

function CreateDialog_VMCreate() {
    $("#dialog_create_vm").dialog({
        title: LanguageStr_VM.VM_Create_Original + VMorVDI,
        modal: true,
        resizable: false,
        draggable: true,
        autoOpen: false,
        height: 580,
        width: 750,
        dialogClass: "no-close",
        closeOnEscape: false
    });
}

function CreateDialog_VMImport() {
    $("#dialog_import_vm").dialog({
        title: LanguageStr_VM.VM_Import_Original + VMorVDI,
        modal: true,
        resizable: false,
        draggable: true,
        autoOpen: false,
        height: 480,
        width: 550,
        dialogClass: "no-close",
        closeOnEscape: false
    });
    $('#btn_close_import').click(function (event) {
        event.preventDefault();
        $("#dialog_import_vm").dialog('close');
    });
}

function CreateDialog_VMInfo() {
    $('#tab-vm-info-container').easytabs();
    $("#dialog_vm_info").dialog({
        title: VMorVDIBack + LanguageStr_VM.VM_Information,
        modal: true,
        resizable: false,
        draggable: true,
        autoOpen: false,
        height: 500,
        width: 600
    });
}

function CreateDialog_VMClone() {
    $('.iCheckRadioClone').iCheck({
        radioClass: 'iradio_flat-blue'
    });
    $("#dialog_vm_clone").dialog({
        title: LanguageStr_VM.VM_Task_Clone + VMorVDI,
        modal: true,
        resizable: false,
        draggable: true,
        autoOpen: false,
        height: 400,
        width: 600,
        dialogClass: "no-close",
        closeOnEscape: false
    });
    $('#btn_close_clone_vm').click(function (event) {
        event.preventDefault();
        $("#dialog_vm_clone").dialog('close');
    });
    $("#dialog_vm_create_vdi").dialog({
        title: LanguageStr_VM.VM_Born_Users + VMorVDI,
        modal: true,
        resizable: false,
        draggable: true,
        autoOpen: false,
        height: 400,
        width: 600,
        dialogClass: "no-close",
        closeOnEscape: false
    });
    $('#btn_close_create_vdi').click(function (event) {
        event.preventDefault();
        $("#dialog_vm_create_vdi").dialog('close');
    });
}

function CreateDialog_iSCSI() {
    $('.iCheckBoxCHAP').iCheck({
        checkboxClass: 'icheckbox_flat-blue'
    });
    $("#dialog_create_iscsi").dialog({
        title: "Create iSCSI Connection",
        modal: true,
        resizable: false,
        draggable: true,
        autoOpen: false,
        height: 400,
        width: 600,
        dialogClass: "no-close",
        closeOnEscape: false
    });
    $('#btn_close_create_iscsi').click(function (event) {
        event.preventDefault();
        $("#dialog_create_iscsi").dialog('close');
    });
}
/* 手動建立SnapShot(Dialog視窗) 2017.01.11 william */
function CreateDialog_CreateSnapshot() {
    $("#dialog_vm_snapshot").dialog({
        title: LanguageStr_VM.VM_Snapshot,
        modal: true,
        resizable: false,
        draggable: true,
        closeOnEscape: false,
        autoOpen: false,
        height: 558, //433->576
        width: 800,
    });
    $('#btn_close_create_snapshot').click(function (event) {
        event.preventDefault();
        $("#dialog_vm_snapshot").dialog('close');
    });
}
/* SAN連線-功能禁用警告視窗 2017.05.08 william */
function Dialog_SANFunctionWarning() {
    $("#dialog_SANFunctionWarning").dialog({
        title: LanguageStr_AdminMain.Dialog_Title_Warning,
        modal: true,
        resizable: false,
        draggable: true,
        closeOnEscape: false,
        autoOpen: false,
        height: 180,
        width: 500,
        dialogClass: "no-close",
    });
    $('#SanWarning_Btn_Confirm').click(function (event) {
        event.preventDefault();
        $("#dialog_SANFunctionWarning").dialog('close');
    });
}

/* 2018.05.24 william 列表標題及內容的文字位置調整 */ 
function ChecklangVer() {  
	if($('.lang0').length){
        SysCfg_lang_ver = "zh-tw";
    }else if($('.lang1').length){
        SysCfg_lang_ver = "zh-cn";
	}else if($('.lang2').length){
        SysCfg_lang_ver = "en-us";
	} 
}

function EnableBtnStyle(elementid1,elementid2,status) {
        switch (status) {
            case 0: 
                elementid1.css({ "border": "1px solid #add", "background": "#3ec6e5", "font-weight": "normal", "color": "#ffffff" });
                elementid2.css({ "border": "1px solid #91979c", "background": "#b3c5c5", "font-weight": "normal", "color": "#ffffff" });
                elementid1
                    .mouseover(function () {
                        $(this).css("background", "-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #378de5), color-stop(1, #79bbff) )");
                        $(this).css("background", "-moz-linear-gradient( center top, #378de5 5%, #79bbff 100% )");
                        $(this).css("filter", "progid:DXImageTransform.Microsoft.gradient(startColorstr='#378de5', endColorstr='#79bbff')");
                        $(this).css("background-color", "#378de5");
                        $(this).css("color", "#ffffff");
                    })
                    .mouseout(function () {
                        $(this).css({ "border": "1px solid #add", "background": "#3ec6e5", "font-weight": "normal", "color": "#ffffff" });
                    });
                elementid2
                    .mouseover(function () {
                        $(this).css({ "border": "1px solid #91979c", "background": "#b3c5c5", "font-weight": "normal", "color": "#ffffff" });
                    })
                    .mouseout(function () {
                        $(this).css({ "border": "1px solid #91979c", "background": "#b3c5c5", "font-weight": "normal", "color": "#ffffff" });
                    });
                break;
            case 1:
                elementid2.css({ "border": "1px solid #add", "background": "#3ec6e5", "font-weight": "normal", "color": "#ffffff" });
                elementid1.css({ "border": "1px solid #91979c", "background": "#b3c5c5", "font-weight": "normal", "color": "#ffffff" });
                elementid2
                    .mouseover(function () {
                        $(this).css("background", "-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #378de5), color-stop(1, #79bbff) )");
                        $(this).css("background", "-moz-linear-gradient( center top, #378de5 5%, #79bbff 100% )");
                        $(this).css("filter", "progid:DXImageTransform.Microsoft.gradient(startColorstr='#378de5', endColorstr='#79bbff')");
                        $(this).css("background-color", "#378de5");
                        $(this).css("color", "#ffffff");
                    })
                    .mouseout(function () {
                        $(this).css({ "border": "1px solid #add", "background": "#3ec6e5", "font-weight": "normal", "color": "#ffffff" });
                    });
                elementid1
                    .mouseover(function () {
                        $(this).css({ "border": "1px solid #91979c", "background": "#b3c5c5", "font-weight": "normal", "color": "#ffffff" }); 
                    })
                    .mouseout(function () {
                        $(this).css({ "border": "1px solid #91979c", "background": "#b3c5c5", "font-weight": "normal", "color": "#ffffff" }); 
                    });
                break;
        }
}  
