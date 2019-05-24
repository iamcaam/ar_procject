var UIExceptionControl = {
    createNew:function(oInputHtml,oInputRelayAjax){
        var oHtml = oInputHtml;
        var oRelayAjax = oInputRelayAjax;        
        var oUIExceptionContorl={};
        var oError = {};
        oUIExceptionContorl.Init = function(){   
            oError = ErrorHandle.createNew();              

            $('#btn_dns_confirm').click(function() {
                oUIExceptionContorl.SetDNSAction();
            });
            $('#btn_dns_cancel').click(function() {
                oUIExceptionContorl.GetDNSList();
            });         
        };                        
        
             
        
        oUIExceptionContorl.GetDNSList = function(){
            var request = oRelayAjax.listvswitch();
            oUIExceptionContorl.CallBackGetDNS(request);
        };
        
        oUIExceptionContorl.CallBackGetDNS = function(request){
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
                var rtnHtml = oHtml.processDNSData(msg['DNS']);                 
                $('#div_dns_field').empty();              
                $('#div_dns_field').html(rtnHtml);

            });
            request.fail(function(jqxhr, textStatus) {
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.GetIP);
            });
        };
        
        
        function validateNetMask(mask)
        {
        //m[0] can be 128, 192, 224, 240, 248, 252, 254, 255
	//m[1] can be 128, 192, 224, 240, 248, 252, 254, 255 if m[0] is 255, else m[1] must be 0
	//m[2] can be 128, 192, 224, 240, 248, 252, 254, 255 if m[1] is 255, else m[2] must be 0
	//m[3] can be 128, 192, 224, 240, 248, 252, 254, 255 if m[2] is 255, else m[3] must be 0

            var flag = true;
            var correct_range = {128:1,192:1,224:1,240:1,248:1,252:1,254:1,255:1,0:1};
            var m = mask.split('.');

            for (var i = 0; i <= 3; i ++) {
                    if (!(m[i] in correct_range)) {
                            flag = false;
                            break;
                    }
            }
	
            if ((m[0] == 0) || (m[0] != 255 && m[1] != 0) || (m[1] != 255 && m[2] != 0) || (m[2] != 255 && m[3] != 0)) {
                    flag = false;
	}
	
            return flag;
        }

        
        oUIExceptionContorl.SetDNSAction = function(){
            var regexIP = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
            var sJsonDNS = '';

            if ($('#DNS0').val().length > 0 && !regexIP.test($('#DNS0').val())) {
                alert(LanguageStr_Network.IP_DNS_Format_Error);
                return false;
            }
            else{
                sJsonDNS += '"' + $('#DNS0').val() + '"'     
            }

            oUIExceptionContorl.SetDNS(sJsonDNS);           
        };    
        

        
        oUIExceptionContorl.SetDNS=function(sJsonDNS)
        {
            var confirmable = confirm(LanguageStr_Network.Exception_DNS_Setting_Ask);
            if(confirmable){
                var request = oRelayAjax.setdns(sJsonDNS);
                oUIExceptionContorl.CallBackSetDNS(request); 
            }
        };
        
        oUIExceptionContorl.CallBackSetDNS=function(request){
            // $('#dialog_vm_msg_reboot').html('<h3>' + LanguageStr_Network.IP_After_Chanage_Msg + '</h3>');
            // $( "#dialog_vm_message_reboot" ).dialog('open');            
            request.done(function(msg, statustext, jqxhr) {
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                oUIExceptionContorl.GetDNSList();

            });
            request.fail(function(jqxhr, textStatus) {
                if (jqxhr.status !== 400) {} else {
                    oHtml.stopPage();
                    alert(LanguageStr_Network.IP_Set_Fail);
                }

            });
        };          


        return oUIExceptionContorl;
    }
};

