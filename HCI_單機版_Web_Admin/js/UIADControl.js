var UIADControl = {
    createNew:function(oInputHtml,oInputRelayAjax){
        var oHtml = oInputHtml;
        var oRelayAjax = oInputRelayAjax;        
        var oUIADContorl={};
        var oError = {};
        oUIADContorl.Init = function(){   
            oError = ErrorHandle.createNew();              
            $('#btn_ad_confirm').click(function() {
                oUIADContorl.SetADList();
            });
            $('#btn_ad_cancel').click(function() {
                oUIADContorl.GetADList();
            });  
            $("#AD_Enable_checkBox").change(function() {
                if(this.checked) {
                    $('#AD_Account').prop('disabled', false);
                    $('#AD_Password').prop('disabled', false);
                }
                else {
                    $('#AD_Account').prop('disabled', true).val('');
                    $('#AD_Password').prop('disabled', true).val('');        
                }
            });                                      
        };                        
        
        oUIADContorl.SetADList = function(){
            var ip =$('#AD_ServerIP').val();
            var domainname = $('#AD_DomainName').val(); 
            var account = $('#AD_Account').val(); 
            var password = $('#AD_Password').val(); 
            var chkbox;
            if ($('#AD_Enable_checkBox').is(":checked")) {
                chkbox = true;
                if(ip.length===0){
                    var error_msg = (LanguageStr_Network.AD_IP_Empty_Error);
                    alert(error_msg);
                    return false;
                } 
                if(domainname.length===0){
                    var error_msg = (LanguageStr_Network.AD_DomianName_Empty_Error);
                    alert(error_msg);
                    return false;
                }             
                if(account.length===0){
                    var error_msg = (LanguageStr_Network.AD_Account_Empty_Error);
                    alert(error_msg);
                    return false;
                } 
                if(password.length===0){
                    var error_msg = (LanguageStr_Network.AD_Password_Empty_Error);
                    alert(error_msg);
                    return false;
                }                 
            }
            else
                chkbox = false;
            if(ip.length > 0){
                var regexIP = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
                if (regexIP.test(ip) === false) {
                    var error_msg = ("IP's format is wrong.");
                    alert(error_msg);
                    return false;
                }
            }                 

            var request = oRelayAjax.setad(chkbox, ip, domainname, account, password);
            oUIADContorl.CallBacksetad(request);
        };

        
        oUIADContorl.CallBacksetad = function(request){
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
                oUIADContorl.GetADList();
            });
            request.fail(function(jqxhr, textStatus) { 
                oHtml.stopPage();
                var error_msg;
                if(jqxhr.status==400) {
                    oUIADContorl.GetADList();
                    error_msg = LanguageStr_Network.AD_Login_Failure_Error;
                    alert(error_msg);
                    return false;    
                }
                else if(jqxhr.status==500){
                    $('#AD_ServerIP').val('');
                    $('#AD_DomainName').val(''); 
                    $('#AD_Account').val('').prop('disabled', true);
                    $('#AD_Password').val('').prop('disabled', true);
                    $('#AD_Enable_checkBox').prop('checked', false); 
                    error_msg = LanguageStr_Network.ExternalIPandAD_RAID_Error;
                    alert(error_msg);
                }
                else{
                    oError.CheckAuth(jqxhr.status,ActionStatus.SetAD);
                }
            });
        };                
        
        oUIADContorl.GetADList = function(){
            var request = oRelayAjax.listAD();
            oUIADContorl.CallBackGetADList(request);
        };
        
        oUIADContorl.CallBackGetADList = function(request){
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
                // // 新增外部IP及轉址功能設定  2017.09.04 william
                $("#AD_Account").val('');
                $("#AD_Password").val('');
                $('#AD_ServerIP').val(msg['IP']);
                $('#AD_DomainName').val(msg['DomainName']);
                if(msg['Enable']) {
                    $('#AD_Account').prop('disabled', false);
                    $('#AD_Password').prop('disabled', false);
                    $('#AD_Enable_checkBox').prop('checked', true);
                }
                else {
                    $('#AD_Account').prop('disabled', true);
                    $('#AD_Password').prop('disabled', true); 
                    $('#AD_Enable_checkBox').prop('checked', false); 
                }    
            });
            request.fail(function(jqxhr, textStatus) {
                oHtml.stopPage();
                // oError.CheckAuth(jqxhr.status,ActionStatus.GetExternal);
            });
        };
    
        return oUIADContorl;
    }
};
