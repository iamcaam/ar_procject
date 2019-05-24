
var UIUpgradeControl = {
    createNew:function(oInputHtml,oInputRelayAjax){
        var eCheckStatus = {
            NoCheck: -1,
            CanUpdate:0,
            NoNewVersion:1
        };
        var eUpdateStatus = {
          UpdateSuccess:0,
          NoUpdate : 1,
          Updating : 2,
          WgetFail : -1,
          WrongVersion : -2,
          UpdateFail : -3
        };
        var CheckStatus;
        var oHtml = oInputHtml;
        var oRelayAjax = oInputRelayAjax;   
        var TimerCheckUpdate;
        var oUIUpgradeContorl={};
        var oError = {};
        var bSetInterval;
        var NowFWVersion;
        var UpdateFlag = false;
        var UIProgressUploadFinish = false; 

        oUIUpgradeContorl.bCheckUpdateFlag;                
        oUIUpgradeContorl.Init = function(){               
            oError = ErrorHandle.createNew();     
            oUIUpgradeContorl.bCheckUpdateFlag = false;
            $('#tab-system-update').easytabs();
            bSetInterval = false;
//            $('#H5_SubTitle_SysMgr_FwUpdate').click(function() {  
//                var btn_element = $(this);
//                MenuButtonClickChangeColor(btn_element,true);
//                oUIPerformance.ClearCPURAMInteval();
//                CheckStatus = eCheckStatus.NoCheck;
//                $('#txt_new_firmware').text('');          
//                ChangeDivToLayer( $('#Div_SysCfg_FwUpdate') );
//                oUIUpgradeContorl.ListVersion();
//            });           
            $('#btn_check_firmware').click(function(){
                oUIUpgradeContorl.CheckNewVersion();
            });
            $('#btn_update_firmware').click(function(){
                var confirmable = true;
                if($('#txt_now_firmware').text() === $('#txt_new_firmware').text())
                    confirmable = confirm(LanguageStr_SysCfgUpdateFW.Update_SameVer_Warning);
                if(confirmable){
                    confirmable = confirm(LanguageStr_SysCfgUpdateFW.Update_Before_Warning);
                    if(confirmable)
                        oUIUpgradeContorl.Update();
                }
            });
            oHtml.blockPageMsg(LanguageStr_SysCfgUpdateFW.Update_Wait_Checking);            
            oUIUpgradeContorl.CheckUpdate();            
            TimerCheckUpdate = setInterval(function(){oUIUpgradeContorl.CheckUpdate();}, 8000);

            $('.iCheckUpDate').iCheck({  
                radioClass: 'iradio_flat-blue'    
            });

            $('.iCheckUpDate').on('ifChecked', function(event){
                ChangeUpDateDivPage($(this).val());
            }); 

            $("#upfile1").click(function () {
                $("#FwUploadSelector").trigger('click');
            });            

            // When your file input changes, update the text for your button
            $("#FwUploadSelector").change(function(){
                $this = $(this);
                // If the selection is empty, reset it
                if($this.val().length == 0) {
                    $('#UploadFileName').text(LanguageStr_SysCfgUpdateFW.No_file_chosen_Str);
                } else {
                    $('#UploadFileName').text($this.val().replace("C:\\fakepath\\", ""));
                }
            })

        };  

        function ChangeUpDateDivPage(check_val) {                       
            switch(check_val) {
                case 'online':
                    $('#Div_online_page').show();
                    $('#Div_local_page').hide();
                    break;
                case 'local':
                    $('#Div_local_page').show();
                    $('#Div_online_page').hide();
                    break;
            }
        }  
        

        oUIUpgradeContorl.Root = function(){
            CheckStatus = eCheckStatus.NoCheck;
            $('#txt_new_firmware').text('');                          
            oUIUpgradeContorl.ListVersion();
        };
        oUIUpgradeContorl.Update = function(){
            switch(CheckStatus)
            {
                case eCheckStatus.NoCheck:
                    alert(LanguageStr_SysCfgUpdateFW.Update_Check_New_Firmware);
                    break
                case eCheckStatus.NoNewVersion:
                    alert(LanguageStr_SysCfgUpdateFW.Update_No_New_Firmware);
                    break;
                case eCheckStatus.CanUpdate:
                    var request = oRelayAjax.updatenewversion();
                    oUIUpgradeContorl.CallBackUpdate(request);
                    break;
            }
        };
        oUIUpgradeContorl.CallBackUpdate = function(request){     
            oHtml.blockPageMsg(LanguageStr_SysCfgUpdateFW.Update_Updating);
//            oUIUpgradeContorl.CheckUpdate();
            TimerCheckUpdate = setInterval(function(){oUIUpgradeContorl.CheckUpdate();}, 5000);
        };

        oUIUpgradeContorl.LocalUpdate = function(){
            var request = oRelayAjax.localupdatenewversion();
            oUIUpgradeContorl.CallBackLoaclUpdate(request);
        };
        oUIUpgradeContorl.CallBackLoaclUpdate = function(request){     
            oHtml.blockPageMsg(LanguageStr_SysCfgUpdateFW.Update_Updating);
//            oUIUpgradeContorl.CheckUpdate();
            TimerCheckUpdate = setInterval(function(){oUIUpgradeContorl.CheckUpdate();}, 5000);
        };
        
        oUIUpgradeContorl.CheckUpdate = function(){
            var request = oRelayAjax.checkupdate();
            oUIUpgradeContorl.CallBackCheckUpdate(request);
        };
        oUIUpgradeContorl.CallBackCheckUpdate = function(request){            
            request.done(function(msg, statustext, jqxhr) {  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                switch(msg['Result'])
                {
                    case eUpdateStatus.UpdateSuccess:
                        window.clearInterval(TimerCheckUpdate);   
                        updateReboot();
                        // alert(LanguageStr_SysCfgUpdateFW.Update_Success);
                        // Ajax_Logout();
                        break;
                    case eUpdateStatus.NoUpdate:
                        window.clearInterval(TimerCheckUpdate);   
                        oUIUpgradeContorl.bCheckUpdateFlag = true;
                        oHtml.stopBlockPage();
                        break;
                    case eUpdateStatus.Updating:                        
                        oHtml.ChangeblockMsg(LanguageStr_SysCfgUpdateFW.Update_Updating);                        
                        break;
                    case eUpdateStatus.WgetFail:
                        window.clearInterval(TimerCheckUpdate);   
                        alert(LanguageStr_SysCfgUpdateFW.Update_Fail_Network);
                        oHtml.stopBlockPage();
                        oUIUpgradeContorl.bCheckUpdateFlag = true;
                        break;                       
                    case eUpdateStatus.WrongVersion:
                        window.clearInterval(TimerCheckUpdate);   
                        alert(LanguageStr_SysCfgUpdateFW.Update_Fail_WrongVersion);
                        oHtml.stopBlockPage();
                        oUIUpgradeContorl.bCheckUpdateFlag = true;
                        break;
                    case eUpdateStatus.UpdateFail:
                        window.clearInterval(TimerCheckUpdate);   
                        alert(LanguageStr_SysCfgUpdateFW.Update_Fail);
                        oHtml.stopBlockPage();
                        oUIUpgradeContorl.bCheckUpdateFlag = true;
                        break;
                    default:
                        window.clearInterval(TimerCheckUpdate);   
                        alert(LanguageStr_SysCfgUpdateFW.Update_Fail_Unknow);
                        oHtml.stopBlockPage();
                        oUIUpgradeContorl.bCheckUpdateFlag = true;
                        break;
                }
            });
            request.fail(function(jqxhr, textStatus) {   
                window.clearInterval(TimerCheckUpdate);
                oError.CheckAuth(jqxhr.status,ActionStatus.SystemCheckUpdate);
            });
        };
        function updateReboot() {
            // oHtmlCreate.blockPage();
            var request = oRelayAjax.Reboot();
            CallBackUpdateReboot(request);
        }

        function CallBackUpdateReboot(request) {
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
                oHtmlCreate.blockPageMsg(LanguageStr_SysCfgUpdateFW.Update_Success);
            });
            request.fail(function (jqxhr, textStatus) {
                checkLogin = false;
                StopPageInterval();
                oHtmlCreate.stopPage();
                oHtmlCreate.blockPageMsg(LanguageStr_SysCfgUpdateFW.Update_Success);
            });
        }

        oUIUpgradeContorl.ListVersion = function(){
            var request = oRelayAjax.listversion();
            oUIUpgradeContorl.CallBackListVersion(request);
        };
        oUIUpgradeContorl.CallBackListVersion = function(request){
            oHtml.blockPage();
            request.done(function(msg, statustext, jqxhr) {
                oHtml.stopPage();
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                if(msg['Result'] == 0)
                {                    
                    $('.txt_now_firmware').text(msg['Version']);
                    $('.txt_last').text(msg['LastUpdateTime']);     /* 新式樣修改(藍黃)--增加韌體更新時間 2016.12.13 william */               
                }
                else{
                    alert(LanguageStr_SysCfgUpdateFW.Update_Fail_ListVersion);
                }
            });
            request.fail(function(jqxhr, textStatus) {    
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.SystemListVersion);
            });
        };
        oUIUpgradeContorl.CheckNewVersion = function(){
            var request = oRelayAjax.checknewversion();
            oUIUpgradeContorl.CallBackCheckNewVersion(request);
        };
        oUIUpgradeContorl.CallBackCheckNewVersion = function(request){
            oHtml.blockPage();
            request.done(function(msg, statustext, jqxhr) {
                oHtml.stopPage();      
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                if(msg['Result'] == 0)
                {
//                    if(msg['Version'] !== $('#txt_now_firmware').text())
//                    {
                        CheckStatus = eCheckStatus.CanUpdate;
                        $('#txt_new_firmware').text(msg['Version']);                    
//                    }
//                    else
//                    {
//                        CheckStatus = eCheckStatus.NoNewVersion;
//                        $('#txt_new_firmware').text(LanguageStr_SysCfgUpdateFW.Update_No_New_Firmware_Txt);                    
//                    }
                }
                else{
                    alert(LanguageStr_SysCfgUpdateFW.Update_Fail_Check_Network);
                }
            });
            request.fail(function(jqxhr, textStatus) {           
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.SystemCheckNewVersion);
            });
        };
        oUIUpgradeContorl.UpdateNewVersion = function(){
            var request = oRelayAjax.updatenewversion()();
            oUIUpgradeContorl.CallBackUpdateNewVersion(request);
        };
        oUIUpgradeContorl.CallBackUpdateNewVersion = function(request){
            request.done(function(msg, statustext, jqxhr) {
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
            });
            request.fail(function(jqxhr, textStatus) {           
            });
        };
        return oUIUpgradeContorl;
    }
};
