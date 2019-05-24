var create_iscsi_step ={
    input_chap_info:0,
    mpio_and_target :1
};
var UIiSCSIControl = {
    createNew:function(oInputHtml,oInputRelayAjax){
        var oHtml = oInputHtml;
        var oRelayAjax = oInputRelayAjax;        
        var oUIiSCSIContorl={};
        var oError = {};   
        var arr_mpio_name = [];
        var nowstep_create_iscsi;
        var PollingListiSCSISession;
        var bRunPollingSession;
        var tmp_iscsi_session_length;        
        oUIiSCSIContorl.Init = function(){   
            oError = ErrorHandle.createNew(); 
            PollingListiSCSISession = true;  
            bRunPollingSession = false;
            tmp_iscsi_session_length = 0;
            nowstep_create_iscsi = create_iscsi_step.input_chap_info;
            $('#btn_refresh_iscsi').click(function(event){                
                oUIiSCSIContorl.ListSession();
                oUIiSCSIContorl.PollingListSession();
            });
            InitCreateiSCSIDialogBtn();
            CHAPCheckboxChangeEvent();
            BindCreateiSCSINextBtnEvent();
            BindCreateiSCSIPreviousBtnEvent();
            BindLogoutBtnEvent();
        };                        
        
        function BindLogoutBtnEvent(){
            $('.drop-logout').click(function(event){
                event.preventDefault();
                var confirmable = confirm(LanguageStr_iSCSI.iSCSI_Logout_Ask);
                if (confirmable === true) {
                    Logout();
                }
            });
        }
        
        function CHAPCheckboxChangeEvent(){
            $('#check-chap').on('ifChanged', function(event){                
                var bol_disable = !$('#check-chap').prop('checked');
                EnableDisableCHAPInput(bol_disable);
            });
        };
        
        function EnableDisableCHAPInput(bol)
        {
            $('#input_iscsi_user_name').prop('disabled',bol);
            $('#input_iscsi_passwd').prop('disabled',bol);
        }    
            
        function InitCreateiSCSIDialogBtn(){
            $('#btn_create_iscsi_dialog').click(function(event){
                event.preventDefault();                
                $('#input_iscsi_ip').val('');
                $('#check-chap').iCheck('uncheck');
                $('#input_iscsi_user_name').val('');
                $('#input_iscsi_passwd').val('');
                EnableDisableCHAPInput(true);
                nowstep_create_iscsi = create_iscsi_step.input_chap_info;
                Process_Create_iSCSI_Step_UI(nowstep_create_iscsi);
                $("#dialog_create_iscsi").dialog('open');
            });
        };
        
        function BindCreateiSCSINextBtnEvent(){
            $('#btn_iscsi_next').click(function(event){                
                Process_Create_iSCSI_Step_Action(nowstep_create_iscsi);
            });
        };
        
        function BindCreateiSCSIPreviousBtnEvent(){
            $('#btn_iscsi_previous').click(function(event){
                nowstep_create_iscsi = create_iscsi_step.input_chap_info;
                Process_Create_iSCSI_Step_UI(nowstep_create_iscsi);
            });
        };
        
        function Process_Create_iSCSI_Step_UI(nowstep){
            switch(nowstep){
                case create_iscsi_step.input_chap_info:    
                    $('#btn_iscsi_previous').hide();
                    $('#btn_iscsi_next').button('option','label',LanguageStr_iSCSI.iSCSI_Next);
                    $('#connect_iscsi_input_info').show();
                    $('#connect_iscsi_select_target').hide();
                    break;
                case create_iscsi_step.mpio_and_target:                    
                    $('#btn_iscsi_next').button('option','label',LanguageStr_iSCSI.iSCSI_Confirm);
                    $('#btn_iscsi_previous').show();
                    $('#connect_iscsi_input_info').hide();
                    $('#connect_iscsi_select_target').show();
                    break;                    
            }
        }               
        
        function Process_Create_iSCSI_Step_Action(nowstep){
            switch(nowstep){
                case create_iscsi_step.input_chap_info:
                    if(VerifyCHAPInput()){
                        var chap = $('#check-chap').prop('checked') === true ? 1 : 0;
                        Discovery(chap,$('#input_iscsi_ip').val(),$('#input_iscsi_user_name').val(),$('#input_iscsi_passwd').val());
                    }
                    break;
                case create_iscsi_step.mpio_and_target:
                    var mpio_ip_arr = [];
                    var set = true;
                    $.each($('.combo_iscsi_mpio_ip'),function(index,element){
                        var element_index = index + 1;
                        if($('#MPIO' + element_index).val() !== 'none'){
                            if(mpio_ip_arr.indexOf($('#MPIO' + element_index).val()) === -1)
                                mpio_ip_arr.push($('#MPIO' + element_index).val());
                            else{
                                alert((LanguageStr_iSCSI.iSCSI_Reselect_Priority).replace("&", element_index));
                                set = false;
                                return false;
                            }                            
                        }
                    });
                    if(set){
                        var target_name = $('#combo_target').val();                        
                        Login(target_name,mpio_ip_arr);
                    }
                    break;
            }
        }
                
        function VerifyCHAPInput(){
            var ip = $('#input_iscsi_ip').val();
            if(ip.length === 0){
                alert(LanguageStr_iSCSI.iSCSI_IP_Warning);
                return false;
            }
            if($('#check-chap').prop('checked')){
                var uname = $('#input_iscsi_user_name').val();
                if(uname.length === 0){
                    alert(LanguageStr_iSCSI.iSCSI_Username_Warning);
                    return false;
                }
                var passwd = $('#input_iscsi_passwd').val();
                if(passwd.length === 0){
                    alert(LanguageStr_iSCSI.iSCSI_Password_Warning);
                    return false;
                }
            }
            return true;
        }                       
                
        oUIiSCSIContorl.StartorStopPollingiSCSI = function(bstart){
            if(bstart){
                PollingListiSCSISession = true;
            }
            else{                
                PollingListiSCSISession = false;
                tmp_iscsi_session_length = 0;
            }
        };                
                
        oUIiSCSIContorl.ListSession = function(){
            oHtml.blockPage();
            var request = oRelayAjax.iSCSIListSession();
            CallBackListSession(request); 
        };                
        
        function CallBackListSession(request){
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
                if(msg['sessions'].length > 0)
                    $("#btn_create_iscsi_dialog").button( "option", "disabled", true );                 
                else
                    $("#btn_create_iscsi_dialog").button( "option", "disabled", false );
                if(tmp_iscsi_session_length !== msg['sessions'].length){
                    $('#Div_iSCISList').html(oHtml.CreateiSCSISessionHtml(msg));
                }
                else{
                    $('.iSCSIList_IP_Row').html(oHtml.CreateiSCSILabelSessionHtml(msg));
                }
                tmp_iscsi_session_length = msg['sessions'].length;                             
            });
            request.fail(function(jqxhr, textStatus) {
//                alert(jqxhr.responseText);
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.iSCSIListSession);
            });
        }        
        
        oUIiSCSIContorl.PollingListSession = function(){   
            if(!bRunPollingSession){
//                console.log('Polling iSCSI');
                bRunPollingSession = true;
                var request = oRelayAjax.iSCSIListSession();
                CallBackPollingListSession(request); 
            }
        };
        
        function CallBackPollingListSession(request){
            request.done(function(msg, statustext, jqxhr) {                                                
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;                
                }       
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                if(msg['sessions'].length > 0)
                    $("#btn_create_iscsi_dialog").button( "option", "disabled", true );                 
                else
                    $("#btn_create_iscsi_dialog").button( "option", "disabled", false );
                if(tmp_iscsi_session_length !== msg['sessions'].length){
                    $('#Div_iSCISList').html(oHtml.CreateiSCSISessionHtml(msg));
                }
                else{
                    $('.iSCSIList_IP_Row').html(oHtml.CreateiSCSILabelSessionHtml(msg));
                }
                tmp_iscsi_session_length = msg['sessions'].length;                
                if(PollingListiSCSISession){                    
                    setTimeout(function(){bRunPollingSession = false;oUIiSCSIContorl.PollingListSession();},10000);
                }
                else
                    bRunPollingSession = false;
            });
            request.fail(function(jqxhr, textStatus) {
//                bRunPollingSession = false;                
//                oError.CheckAuth(jqxhr.status,ActionStatus.iSCSIListSession);
            });
        } 
        
        function Discovery(chap,ip,username,passwd){
            oHtml.blockPage();
            var request = oRelayAjax.iSCSIDiscovery(chap,ip,username,passwd);
            CallBackDiscovery(request); 
        }
        
        function CallBackDiscovery(request){
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
                if(msg['rc'] === 0){                                                          
                    $('#connect_iscsi_select_target').html(oHtml.CreateiSCSIAfterDiscoveryHtml(msg));
//                    $('#combo_target').scombobox({
//                        maxHeight:'150px',
//                        editAble:false,
//                        wrap:false       
//                    });
//                    $.each($('.combo_iscsi_mpio_ip'),function(index,element){
//                        var element_index = index + 1;
//                        $('#MPIO' + element_index).scombobox({
//                            maxHeight:'150px',
//                            editAble:false,
//                            wrap:false       
//                        });
//                            
//                    });
//                    $('.combo_iscsi_mpio_ip').scombobox({
//                        maxHeight:'150px',
//                        editAble:false,
//                        wrap:false       
//                    });
//                    $('.iCheckBoxMPIO').iCheck({  
//                        checkboxClass: 'icheckbox_flat-blue'    
//                    });  
                    
                    nowstep_create_iscsi = create_iscsi_step.mpio_and_target;
                    Process_Create_iSCSI_Step_UI(nowstep_create_iscsi);                    
//                    $.each(msg['ips'], function(ipindex, ipelement) {
//                        var index = ipindex + 1;
//                        arr_mpio_name.push('#MPIO' + index);
//                        $('#MPIO' + index).scombobox('disabled',true);
//                    });
//                    $('.iCheckBoxMPIO').on('ifChanged', function(event){     
//                        var element = $(this);
//                        var bol_disable = !element.prop('checked');
//                        $.each(arr_mpio_name, function(mpioindex, mpioelement) {
//                            $(mpioelement).scombobox('disabled',bol_disable);
//                        });
//                    });
                }
                else{                
                    if(msg['rc'] < 0){
                        alert(LanguageStr_iSCSI.iSCSI_Discovery_Fail);
                    }
                    else{
                        alert(LanguageStr_iSCSI.iSCSI_No_Targets);
                    }
                    
                }
            });
            request.fail(function(jqxhr, textStatus) {
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.iSCSIDiscovery);
            });
        }
        
        function Login(targetname,arr_ip){
            PollingListiSCSISession = false;
            oHtml.blockPage();
            var request = oRelayAjax.iSCSILogin(targetname,arr_ip);
            CallBackLogin(request);  
        }
        
        function CallBackLogin(request){
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
                if(msg['rc'] !== 0){                    
                    var str_fail_ip = '';
                    $.each(msg['failips'],function(index,element){
                        if(index !== 0)
                            str_fail_ip += ',';
                        else
                            str_fail_ip += element;
                    });
                    alert((LanguageStr_iSCSI.iSCSI_Connect_Fail).replace("&", str_fail_ip));
                }
                else
                    $("#dialog_create_iscsi").dialog('close');
                if(typeof(NowDiskSortType)=== 'undefined')
                {
                    var th = $('#poition .VMHeader');
                    th.removeClass('desc');
                    th.addClass('asc');
                    NowDiskSortDirection = SortDirection.ASC;
                    NowDiskSortType = SortType.String;
                    NowDiskSortDOMID = th.attr('id');
                }                                            
                setTimeout( function(){Ajax_ListDisk(false);}, 500 );
                setTimeout( function(){Ajax_GetInfo_Raid(false);}, 1000 );
                PollingListiSCSISession = true;                
                oUIiSCSIContorl.ListSession();
                oUIiSCSIContorl.PollingListSession();
                //setTimeout( oUIiSCSIContorl.ListSession, 500 );                 
            });
            request.fail(function(jqxhr, textStatus) {
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.iSCSIDiscovery);
            });
        }
        
        function Logout(){
            PollingListiSCSISession = false;
            oHtml.blockPage();
            var request = oRelayAjax.iSCSILogoutAll();
            CallBackLogout(request);  
        }
        
        function CallBackLogout(request){
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
                if(msg['rc'] !== 0){                    
                    alert(LanguageStr_iSCSI.iSCSI_Logout_Fail);
                }                
                if(typeof(NowDiskSortType)=== 'undefined')
                {
                    var th = $('#poition .VMHeader');
                    th.removeClass('desc');
                    th.addClass('asc');
                    NowDiskSortDirection = SortDirection.ASC;
                    NowDiskSortType = SortType.String;
                    NowDiskSortDOMID = th.attr('id');
                }                                            
                setTimeout( function(){Ajax_ListDisk(false);}, 500 );
                setTimeout( function(){Ajax_GetInfo_Raid(false);}, 500 );   
                PollingListiSCSISession = true;                
                oUIiSCSIContorl.ListSession();
                oUIiSCSIContorl.PollingListSession();
            });
            request.fail(function(jqxhr, textStatus) {
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.iSCSILogout);
            });
        }
        return oUIiSCSIContorl;
    }
};