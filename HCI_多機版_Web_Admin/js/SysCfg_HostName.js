var UISysHostName = {
    createNew:function(oInputHtml,oInputRelayAjax){ 
        var oHtml = oInputHtml;
        var oRelayAjax = oInputRelayAjax;                        
        var oUISystemHostName={};
        var oError = {};      
        var reg_name = /^[a-zA-Z0-9_.]*$/;
        oUISystemHostName.Init = function(){               
            oError = ErrorHandle.createNew();    
            $('#btn_chg_host_name').click(function() {
                var name = $('#input_HostName').val();           
                if(name.length === 0)
                {
                    alert(LanguageStr_VM.VM_Name_Empty_Warning);
                    return false;
                }         
                if(!reg_name.test(name))
                {
                    var name_warning = (LanguageStr_System.System_Host_Name_Warning);
                    alert(name_warning);
                    return false;
                }         
                var confirmable = confirm(LanguageStr_System.HostName_After_Setting_Ask); // 新增確定後執行功能 2017.03.23 william
                if(confirmable){
                    oUISystemHostName.SetHostName(name);
                }
            });
            $('#btn_clear_host_name').click(function() {
                oUISystemHostName.ListHostName();
            }); 
        };                                           
        
        oUISystemHostName.ListHostName = function(){
            oHtml.blockPage();    
            var request = oRelayAjax.ListHostName();
            CallBackListHostName(request);            
        };
        
        function CallBackListHostName(request){
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
                $('#input_HostName').val(msg['HostName']);
            });
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.ListHostName);
            });
        };
        
        oUISystemHostName.SetHostName = function(hostname){
            oHtml.blockPage();    
            var request = oRelayAjax.SetHostName(hostname);
            CallBackSetHostName(request);            
        };
        
        function CallBackSetHostName(request){
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
                oUISystemHostName.ListHostName();
                /* 新增更新Host Name後，重新開機 2017.03.23 william */
                $('#dialog_vm_msg_reboot').html('<h3>' + LanguageStr_System.HostName_After_Chanage_Msg + '</h3>');
                $( "#dialog_vm_message_reboot" ).dialog('open'); 
                HostNameReboot();

            });
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.SetHostName);
            });
        };
        /* 新增更新Host Name後，重新開機 2017.03.23 william */
        function HostNameReboot(){
            var request = oRelayAjax.Reboot();  
            CallBackHostNameReboot(request);
        }       
        /* 新增更新Host Name後，重新開機 2017.03.23 william */
        function CallBackHostNameReboot(request){
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
        } 

        return oUISystemHostName;
    }
};