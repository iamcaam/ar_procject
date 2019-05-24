var UISysUPS = {
    createNew:function(oInputHtml,oInputRelayAjax) {
        var oHtml = oInputHtml;
        var oRelayAjax = oInputRelayAjax;     
        var oUISysUPS = {};
        var oError = {};     
        var PollingFlag;
        var GetUpsInterval;

        oUISysUPS.Init = function() {   
            oError = ErrorHandle.createNew();
            PollingFlag = true;

            $('.iCheckRadioUps').iCheck({  
                radioClass: 'iradio_flat-blue'    
            });
            
            $('#combo_ups_delaytime').scombobox({
                editAble:false,
                wrap:false       
            });            

            $('#btn_set_ups').click(function() {
                var upsip = $('#input_UPSIP').val();        
                var upstime = $('#combo_ups_delaytime').scombobox('val');     
                if(upsip.length > 0) {
                    var regexIP = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
                    if (regexIP.test(upsip) === false) {
                        var error_msg = (LanguageStr_System.Ups_IpFormat_Error);
                        alert(error_msg);
                        return false;
                    }
                }      
                PollingFlag = false;
                oUISysUPS.SetUPS(upsip, upstime);
            });

            $('#btn_reset_ups').click(function() {
                oUISysUPS.ListUPS();
            });                       
        };         

        oUISysUPS.ListUPS = function(){
            oHtml.blockPage();    
            var request = oRelayAjax.ListUPS();
            CallBackListUPS(request);            
        };

        function CallBackListUPS(request){
            request.done(function(msg, statustext, jqxhr) {          
                oHtml.stopPage();                
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2) {
                    Logout_Another_Admin_Login();
                    return false;
                }        
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3) {
                    window.location.href = 'index.php';
                    return false;
                }

                $('#input_UPSIP').val(msg['IP']);
                $('#combo_ups_delaytime').scombobox('val',msg['DelayTime']);
                $('#UPSBatteryStatus').text(UpsTypeStatus(msg['BatteryStatus']));
                $('#UPSBatteryTime').text(msg['BatteryTime']);
                $('#UPSChargeCondition').text(msg['BatteryChargeCondition'] + '%');                    
            
                GetUpsInterval = setInterval(function(){oUISysUPS.CheckBatteryPolling();},5000);
            });
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.ListUPS);
            });
        };

        oUISysUPS.CheckBatteryPolling = function(){
            var request = oRelayAjax.ListUPS();
            CallBackCheckBatteryPolling(request);            
        };        

        function CallBackCheckBatteryPolling(request){
            request.done(function(msg, statustext, jqxhr) {                       
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2) {
                    Logout_Another_Admin_Login();
                    return false;
                }        
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3) {
                    window.location.href = 'index.php';
                    return false;
                }
                if(PollingFlag) {                    
                    $('#UPSBatteryStatus').text(UpsTypeStatus(msg['BatteryStatus']));
                    $('#UPSBatteryTime').text(msg['BatteryTime']);
                    $('#UPSChargeCondition').text(msg['BatteryChargeCondition'] + '%');                    
                }                
            });
            request.fail(function(jqxhr, textStatus) {});
        };


        oUISysUPS.SetUPS = function(ip, delaytime){
            oHtml.blockPage();    
            var request = oRelayAjax.SetUPS(ip, delaytime);
            CallBackSetUPS(request);            
        };
        
        function CallBackSetUPS(request){
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
                $('#input_UPSIP').val(msg['IP']);
                $('#combo_ups_delaytime').scombobox('val',msg['DelayTime']);
                $('#UPSBatteryStatus').text(UpsTypeStatus(msg['BatteryStatus']));
                $('#UPSBatteryTime').text(msg['BatteryTime']);
                $('#UPSChargeCondition').text(msg['BatteryChargeCondition'] + '%');

                PollingFlag = true;

            });
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.SetUPS);
                $('#input_UPSIP').val('');
                PollingFlag = true;
            });
        };

        function UpsTypeStatus(type){
            switch(type){
                case -1 :
                    return 'Disconnect';
                case 0 :
                    return 'Unknown';
                case 1 :
                    return 'Battery Normal';
                case 2 :
                    return 'Battery Low';
                case 3 :
                    return 'Battery Depleted';
                case 4 :
                    return 'Battery Discharging';      
                case 5 :
                    return 'Battery Failure';  
                default:             
                    return 'N/A';                                                                                                 
            }
        }

        oUISysUPS.ClearUpsInteval=function()
        {
            if(typeof(GetUpsInterval) !== 'undefined')
                clearInterval(GetUpsInterval);            
        }  


        return oUISysUPS;
    }
};