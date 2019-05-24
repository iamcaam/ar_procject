var UISystemControl = {
    createNew:function(oInputHtml,oInputRelayAjax){ 
        var oHtml = oInputHtml;
        var oRelayAjax = oInputRelayAjax;                        
        var oUISystemContorl={};
        var oError = {};
        var GetTimeInterval;        
        var Senconds;                
        oUISystemContorl.Init = function(){   
            VMIntervalStart = false;
            CurrentVMWorkType = -1;
            CurrentVMWorkRebootCount = 0;
            oError = ErrorHandle.createNew();
            InitSystemTab();
            InitDatePicker();
            InitTimeCombo();            
            InitPwdButton();  
            InitTimeButton();   
            InitAlarmButton();
            $('.iCheckRadio').iCheck({  
                radioClass: 'iradio_flat-blue'    
            });
            
            $('.iCheckRadio').on('ifChecked', function(event){
                TimeModeChangeEnableDisableElement($(this).val());
            });                        
        };                
        
        
        function InitPwdButton(){
            $('#btn_clear_pwd').click(function(event){
                event.preventDefault();
                oUISystemContorl.ClearPassword();
            });
            $('#btn_chg_pwd').click(function(event) {
                event.preventDefault();
                oUISystemContorl.ChangePasswordAction();
            });
        }
        
        function InitTimeButton(){
            $('#btn_set_time').click(function(event){
                event.preventDefault();
                SetTime();
            });
            $('#btn_reset_time').click(function(event) {
                event.preventDefault();
                oUISystemContorl.ListTime(true);
            });
            $('#btnupdate').click(function(event){
                event.preventDefault();
                UpdateTime();
            });
        }                               
        
        function InitTimeCombo(){
            $('#combotimezone').scombobox({
                editAble:false,
                wrap:false,
                maxHeight:'200px'
            });
            $('#combohour').scombobox({
                editAble:false,
                wrap:false,
                maxHeight:'130px'
            });
            $('#combomin').scombobox({
                editAble:false,
                wrap:false,
                maxHeight:'130px'
            });
            $('#combosecond').scombobox({
                editAble:false,
                wrap:false,
                maxHeight:'130px'
            });
            $('#combontpserver').scombobox({
                invalidAsValue: true,
                customEdit : true,
                wrap:false
            });
        };     
        
        function InitSystemTab(){
            $('#tab-container').easytabs();
        };
        
        function InitDatePicker(){
            $( "#datepicker" ).datepicker({dateFormat: "yy-mm-dd"});            
        };                
        
        function InitAlarmButton(){
            $('#btn_enable_alarm').click(function(event){
                event.preventDefault();  
                SetAlarm(1);
            });
             $('#btn_disable_alarm').click(function(event){
                event.preventDefault();                
                SetAlarm(0);
            });
        }                
        
        oUISystemContorl.ClearPassword = function(){
            $('#CurPWD').val('');        
            $('#NewPWD').val('');
            $('#ConfNewPWD').val('');
        };               
        
        oUISystemContorl.ChangePasswordAction = function(){            
            var sCurPWD = $('#CurPWD').val();        
            var sNewPWD = $('#NewPWD').val();
            var sConfirmNewPWD = $('#ConfNewPWD').val();
            if(sCurPWD.length === 0)
            {
                alert(LanguageStr_System.System_CurPWD_Empty);
                return false;
            }
            if(sNewPWD.length === 0)
            {
                alert(LanguageStr_System.System_NewPWD_Empty);
                return false;
            }
            if(sConfirmNewPWD.length === 0)
            {
                alert(LanguageStr_System.System_ConfirmNewPWD_Empty);
                return false;
            }
            // if(hex_md5(sCurPWD)!== oUISystemContorl.GetCookie('CheckKey'))
            // {
            //     alert(LanguageStr_System.System_CurPWD_Error);
            //     return false;
            // }
            if (sNewPWD.indexOf(' ') !== -1)
            {
                alert(LanguageStr_System.System_NewPWD_Whitespace);
                return false;
            }
            if (sNewPWD.length < 6 || sNewPWD.length > 12)
            {
                alert(LanguageStr_System.System_NewPWD_Length);
                return false;
            }
            if(sNewPWD !== sConfirmNewPWD)
            {
                alert(LanguageStr_System.System_PWD_NewPWD_NotSame);
                return false;
            }            
            oUISystemContorl.ChangePassword(sCurPWD,sNewPWD);
        };    
        
        oUISystemContorl.ChangePassword=function(sCurPWD,sNewPWD)
        {
            var request = oRelayAjax.changepwd(hex_sha512(sCurPWD),hex_sha512(sNewPWD));
            oUISystemContorl.CallBackChangePassword(request); 
        };
        
        oUISystemContorl.CallBackChangePassword=function(request){
            oHtml.blockPage();            
            request.done(function(msg, statustext, jqxhr) {                  
                oHtml.stopPage();
                if(typeof(msg['status']) !== "undefined"){
                    if(msg['status'] === 2){
                        Logout_Another_Admin_Login();
                        return false;                
                    }
                    else if (msg['status'] === 80){
                        alert(LanguageStr_System.System_CurPWD_Error);
                        return false;
                    }
                }                 
                alert(LanguageStr_System.System_PWD_Modify_Success);
                Ajax_Logout();
            });            
            request.fail(function(jqxhr, textStatus) {  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.SystemChangePWD);
            });
        };     
        
        oUISystemContorl.GetCookie=function(cname) {
            var name = cname + "=";
            var ca = document.cookie.split(';');
            for(var i=0; i<ca.length; i++) {
                var c = ca[i];
                while (c.charAt(0)===' ') c = c.substring(1);
                if (c.indexOf(name) !== -1) {
                    return c.substring(name.length, c.length);
                }
            }
            return "";
        };
        
        function VerifyNtpServer(Input_Data)
        {
            if (Input_Data.length === 0) {
                alert(LanguageStr_System.System_NTP_NoAddress_Warning);
                return false;
            };  
            var whitespace = " ";
            var sDot = ",";            
            if (Input_Data.indexOf(whitespace) !== -1 || Input_Data.indexOf(sDot) !== -1){
                lert(LanguageStr_System.System_NTP_Address_Warning);
                return false;
            }
            else
                return true;
        };
        
        function SetTime(){
            var mode = 0;
            var time = '';            
            var timezone = $('#combotimezone').scombobox('val');
            var ntpserver = '';
            var check_val = $( ".iCheckRadio:checked" ).val();            
            if(check_val === 'sync'){
                mode =1;
                ntpserver = $('#combontpserver').scombobox('val');
                if(!VerifyNtpServer(ntpserver)){                    
                    return;
                }
            }
            else{               
                var date = $('#datepicker').datepicker({ dateFormat: 'yy-mm-dd' }).val();
                var hour = $('#combohour').scombobox('val'); 
                var min = $('#combomin').scombobox('val'); 
                var second = $('#combosecond').scombobox('val'); 
                time = date + " " + hour + ":" + min + ":" + second;                
            }
            oHtml.blockPage();
            var request = oRelayAjax.SetTime(mode,timezone,time,ntpserver);
            CallBackSetTime(request);       
        };
                        
        function CallBackSetTime(request){
            request.done(function(msg, statustext, jqxhr) {          
                oHtml.stopPage();    
                oUISystemContorl.ListTime(true);
                
            });            
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();                
                if(jqxhr.status === 400){
                    alert(LanguageStr_System.System_NTP_Warning);
                    oUISystemContorl.ListTime(true);
                }
                else
                    oError.CheckAuth(jqxhr.status,ActionStatus.SetTime);
            });
        }
        
        function UpdateTime(){
            var ntpserver = $('#combontpserver').scombobox('val');  
            if(!VerifyNtpServer(ntpserver)){                
                return;
            }
            oHtml.blockPage();
            var request = oRelayAjax.UpdateTime(ntpserver);
            CallBackUpdateTime(request);       
        };
                        
        function CallBackUpdateTime(request){
            request.done(function(msg, statustext, jqxhr) {          
                oHtml.stopPage();    
                oUISystemContorl.ListTime(false);                
            });            
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();                
                if(jqxhr.status === 400){
                    alert(LanguageStr_System.System_NTP_Warning);
                    oUISystemContorl.ListTime(false);
                }
                else{
                    oError.CheckAuth(jqxhr.status,ActionStatus.UpdateNtp);
                }
            });
        }                
        
        oUISystemContorl.ListTime = function(bChangeDate){
            oHtml.blockPage();    
            var request = oRelayAjax.ListTime();
            CallBackListTime(request,bChangeDate);             
        };
        
        function CallBackListTime(request,bChangeDate){
            request.done(function(msg, statustext, jqxhr) {          
                oHtml.stopPage();    
                var d=new Date(msg['Time']);
                Senconds= d.getTime();                
                if(typeof(GetTimeInterval) !== 'undefined')
                    clearInterval(GetTimeInterval);
                if(bChangeDate){
                    $('#combotimezone').scombobox('val', msg['TimeZone']);
                }
                if(bChangeDate){
                    if(msg['Mode'] === 1){
                        $('#radio-sync').iCheck('check');                    
                        $('#combontpserver').scombobox('val',msg['NTP']);
                        $('#btn_set_time').button( "option", "label", LanguageStr_System.Btn_Confirm + ' / ' + LanguageStr_System.Btn_System_Update_Now );   
                    }
                    else {
                        $('#radio-manually').iCheck('check');
                        $('#btn_set_time').button( "option", "label", LanguageStr_System.Btn_Confirm  );   
                    }
                        
                }                    
                SetTimeStrBySenconds(bChangeDate);
                GetTimeInterval = setInterval(function(){SetTimeStrBySenconds(false);}, 1000);
            });            
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.ListTime);
            });
        };
        
        oUISystemContorl.ListAlarm = function(){
            oHtml.blockPage();    
            var request = oRelayAjax.ListAlarm();
            CallBackListAlarm(request);       
        };
        
        function CallBackListAlarm(request){
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
                var status_txt=LanguageStr_System.System_Enable;
                if(msg['Alarm'] == 0){                   
                    status_txt=LanguageStr_System.System_Disable;
                    Set_Alarm_Notify(0);      
                }                    
                else
                    Set_Alarm_Notify(1);      
                $('#label_alarm_status').text(status_txt);
            });            
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.SystemListAlarm);
            });
        }
        
        function SetAlarm(bEnable){
            oHtml.blockPage();    
            var request = oRelayAjax.SetAlarm(bEnable);
            CallBackSetAlarm(request);
        }
        
        function CallBackSetAlarm(request){
            request.done(function(msg, statustext, jqxhr) {          
                //oHtml.stopPage();            
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }   
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                oUISystemContorl.ListAlarm();
            });            
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.SystemSetAlarm);
            });
        }
        
        function SetTimeStrBySenconds(bChangeDate) {            
            var d1=new Date(Senconds);

            var curr_year = d1.getFullYear();

            var curr_month = d1.getMonth() + 1; //Months are zero based
            if (curr_month < 10)
                curr_month = "0" + curr_month;

            var curr_date = d1.getDate();
            if (curr_date < 10)
                curr_date = "0" + curr_date;

            var curr_hour = d1.getHours();
            if (curr_hour < 10)
                curr_hour = "0" + curr_hour;

            var curr_min = d1.getMinutes();
            if (curr_min < 10)
                curr_min = "0" + curr_min;

            var curr_sec = d1.getSeconds();     
            if (curr_sec < 10)
                curr_sec = "0" + curr_sec;

            var newtimestamp = curr_year + "-" + curr_month + "-" + curr_date + " " + curr_hour + ":" + curr_min + ":" + curr_sec;
            Senconds += 1000;
            $('#curTime').text(newtimestamp);
            if(bChangeDate)
            {
                $( "#datepicker" ).datepicker( "setDate", curr_year + "-" + curr_month + "-" + curr_date );
                $('#combohour').scombobox('val', curr_hour); 
                $('#combomin').scombobox('val', curr_min); 
                $('#combosecond').scombobox('val', curr_sec); 
                TimeModeChangeEnableDisableElement();
            }
        }
        
        function TimeModeChangeEnableDisableElement(check_val){           
//            var check_val = $( ".radiotime:checked" ).val();            
            switch(check_val){
                case 'manually':
                    $( "#btnupdate" ).button( "option", "disabled", true );
                    $( "#datepicker" ).datepicker("option", "disabled", false );
                    var disabled = false;
                    $('#combohour').scombobox('disabled', disabled); 
                    $('#combomin').scombobox('disabled', disabled); 
                    $('#combosecond').scombobox('disabled', disabled);                    
                    disabled = true;
                    $('#combontpserver').scombobox('disabled', disabled); 
                    $('#btn_set_time').button( "option", "label", LanguageStr_System.Btn_Confirm  );
                    break;
                case 'sync':
                    $( "#btnupdate" ).button( "option", "disabled", false );
                    $( "#datepicker" ).datepicker("option", "disabled", true );                    
                    var disabled = true;
                    $('#combohour').scombobox('disabled', disabled);    
                    $('#combomin').scombobox('disabled', disabled); 
                    $('#combosecond').scombobox('disabled', disabled);                     
                    disabled = false;
                    $('#combontpserver').scombobox('disabled', disabled); 
                    $('#btn_set_time').button( "option", "label", LanguageStr_System.Btn_Confirm + ' / ' + LanguageStr_System.Btn_System_Update_Now );
                    break;
            }
        }                
                                
        return oUISystemContorl;
    }
};
