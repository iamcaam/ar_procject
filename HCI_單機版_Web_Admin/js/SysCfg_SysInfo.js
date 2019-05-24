var UISysInfoControl = {
    createNew:function(oInputHtml,oInputRelayAjax){ 
        var oHtml = oInputHtml;
        var oRelayAjax = oInputRelayAjax;                        
        var oUISystemInfoContorl={};
        var oError = {};      
        var isBindiCheckRadioSMBCheckEvent;
        oUISystemInfoContorl.Init = function(){               
            oError = ErrorHandle.createNew(); 
            InitServiceTab();  
            InitBackupTab();    // 新增備份tabs選單 2017.02.21 william   
            isBindiCheckRadioSMBCheckEvent = false;
            isBindiCheckRadioSSHCheckEvent = false;
            $('.iCheckRadioSMB').iCheck({  
                radioClass: 'iradio_flat-blue'    
            });           
            $('.iCheckRadioSSH').iCheck({  
                radioClass: 'iradio_flat-blue'    
            });           
            $('.btn_change_vnc_pwd').click(function(event){
                var btnelement = $(this);
                var sNewVNCPWD = ''; 
                var sConfirmNewVNCPWD = ''; 
                if(btnelement.attr('id') === 'btn_for_vnc'){
                    sNewVNCPWD = $('#ChangeVNCPWD').val(); 
                    sConfirmNewVNCPWD = $('#ConfirmVNCPWD').val(); 
                }
                else{
                    sNewVNCPWD = $('#ChangeSMBPWD').val(); 
                    sConfirmNewVNCPWD = $('#ConfirmSMBPWD').val(); 
                }
                event.preventDefault();
                if(sNewVNCPWD.length === 0)
                {
                    alert(LanguageStr_Service.Service_New_Password_Empty);
                    return false;
                }
                if(sConfirmNewVNCPWD.length === 0)
                {
                    alert(LanguageStr_Service.Service_Confirm_Password_Empty);
                    return false;
                }            
                if (sNewVNCPWD.indexOf(' ') !== -1)
                {
                    alert(LanguageStr_Service.Service_Password_Whitespace);
                    return false;
                }
                if(sNewVNCPWD !== sConfirmNewVNCPWD)
                {
                    alert(LanguageStr_Service.Service_Password_Not_Same);
                    return false;
                }   
                
                ChangeVNCPW(sNewVNCPWD);
            });            
            $('#btn_for_smb_cancel').click(function(event){
                $('#ChangeSMBPWD').val(''); 
                $('#ConfirmSMBPWD').val('');                 
            }); 
            $('#btn_enable_ssh').click(function(event){
                ClickSetSSH(1);
            });  
            $('#btn_disable_ssh').click(function(event){
                ClickSetSSH(0);
            });             
            $('#btn_enable_smb').click(function(event){
                ClickSetSMB(1);
            });  
            $('#btn_disable_smb').click(function(event){
                ClickSetSMB(0);
            });                                                
        };                  
                 
        oUISystemInfoContorl.ClearPWDInput = function(){            
            $('#ChangeVNCPWD').val(''); 
            $('#ConfirmVNCPWD').val('');                 
            $('#ChangeSMBPWD').val(''); 
            $('#ConfirmSMBPWD').val('');             
        };
        
        oUISystemInfoContorl.ListSMB = function(){
            oHtml.blockPage();    
            var request = oRelayAjax.ListSMB();
            CallBackListSMB(request);            
        };
        
        function CallBackListSMB(request){
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
                if(msg['SMB'] === 1){
                    $('#radio-enable-smb').iCheck('check'); 
                    $('#label_smd_status').text(LanguageStr_System.System_Enable);         
                    EnableBtnStyle($('#btn_enable_smb'),$('#btn_disable_smb'),1);                                
                }
                else{
                    $('#radio-disable-smb').iCheck('check'); 
                    $('#label_smd_status').text(LanguageStr_System.System_Disable); 
                    EnableBtnStyle($('#btn_enable_smb'),$('#btn_disable_smb'),0);                    
                }
                if(!isBindiCheckRadioSMBCheckEvent){
                    isBindiCheckRadioSMBCheckEvent =true;
                    $('.iCheckRadioSMB').on('ifChecked', function(event){
                        SetSMB();
                    });  
                }   
            });
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.ListSMB);
            });
        };
        
        function SetSMB()
        {
            var check_val = $( ".iCheckRadioSMB:checked" ).val();            
            oHtml.blockPage(); 
            var request = oRelayAjax.SetSMB(check_val);
            CallBackSetSMB(request); 
        }
        
        function ClickSetSMB(check_val)
        {    
            oHtml.blockPage(); 
            var request = oRelayAjax.SetSMB(check_val);
            CallBackSetSMB(request); 
        }        

        function CallBackSetSMB(request){
            request.done(function(msg, statustext, jqxhr) {          
//                oHtml.stopPage();
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                oUISystemInfoContorl.ListSMB();
            });
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.SetSMB);
            });
        };        
        
        oUISystemInfoContorl.ListSSH = function(){
            oHtml.blockPage();    
            var request = oRelayAjax.ListSSH();
            CallBackListSSH(request);            
        };
        
        function CallBackListSSH(request){
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
                if(msg['EnableSSH'] === true){
                    $('#radio-enable-ssh').iCheck('check'); 
                    $('#label_ssh_status').text(LanguageStr_System.System_Enable); 
                    EnableBtnStyle($('#btn_enable_ssh'),$('#btn_disable_ssh'),1);
                }
                else{
                    $('#radio-disable-ssh').iCheck('check'); 
                    $('#label_ssh_status').text(LanguageStr_System.System_Disable); 
                    EnableBtnStyle($('#btn_enable_ssh'),$('#btn_disable_ssh'),0);
                }
                if(!isBindiCheckRadioSSHCheckEvent){
                    isBindiCheckRadioSSHCheckEvent =true;
                    $('.iCheckRadioSSH').on('ifChecked', function(event){
                        SetSSH();
                    });  
                }   
            });
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.ListSSH);
            });
        };
        
        function SetSSH()
        {
            var check_val = $( ".iCheckRadioSSH:checked" ).val();            
            oHtml.blockPage(); 
            check_val = check_val == 1 ? true : false                
            var request = oRelayAjax.SetSSH(check_val);
            CallBackSetSSH(request); 
        }

        function ClickSetSSH(check_val)
        {            
            oHtml.blockPage();                 
            var request = oRelayAjax.SetSSH(check_val);
            CallBackSetSSH(request); 
        }        
        
        function CallBackSetSSH(request){
            request.done(function(msg, statustext, jqxhr) {          
//                oHtml.stopPage();
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                oUISystemInfoContorl.ListSSH();
            });
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.SetSSH);
            });
        };
        
        function InitServiceTab(){
            $('#tab-service-container').easytabs();
        }; 
        
        function InitBackupTab(){ // 新增備份tabs選單 2017.02.21 william   
            $('#tab-Backup-container').easytabs();
        };         
        
        function ChangeVNCPW(newpwd){
            oHtml.blockPage();
            var request = oRelayAjax.ChangeVNCPWD(newpwd);
            CallBackChangeVNCPW(request);
        };
        
        function CallBackChangeVNCPW(request){            
            request.done(function(msg, statustext, jqxhr) {                                
                oHtml.stopPage();
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return;
                }   
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                $('#ChangeSMBPWD').val('');
                $('#ConfirmSMBPWD').val('');
            });
            request.fail(function(jqxhr, textStatus) {
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.ChangeVNCPWD);
                $('#ChangeSMBPWD').val('');
                $('#ConfirmSMBPWD').val('');
            });
        };
        
        oUISystemInfoContorl.GetIPList = function(){
            //oHtml.blockPage();
            var request = oRelayAjax.listip();
            oUISystemInfoContorl.CallBackGetIP(request);
        };
        
        oUISystemInfoContorl.CallBackGetIP = function(request){            
            request.done(function(msg, statustext, jqxhr) {                                
            //oHtml.stopPage();
            if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                Logout_Another_Admin_Login();
                return;
            }
            else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                window.location.href = 'index.php';
                return false;
            }
                $.each(msg['managernic'], function(index, element) {
                   if(element['ipv4'].length !== 0){
//                       $('#label_vncip').text(element['ip'] + ':5901');
                       $('#label_nasip').text(element['ipv4']);
                       VDIIP = element['ipv4'];
                       return false;
                   }                       
                });
            });
            request.fail(function(jqxhr, textStatus) {
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.GetIP);
            });
        };                                      
        
        return oUISystemInfoContorl;
    }
};