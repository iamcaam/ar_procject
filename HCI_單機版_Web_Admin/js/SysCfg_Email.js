var UISysEmail = {
    createNew:function(oInputHtml,oInputRelayAjax) {
        var oHtml = oInputHtml;
        var oRelayAjax = oInputRelayAjax;     
        var oUISysEmail = {};
        var oError = {};     

        oUISysEmail.Init = function() {   
            oError = ErrorHandle.createNew();    

            $('.field input').keyup(function() {

                var empty = false;
                $('.field input').each(function() {
                    if ($(this).val().length == 0) {
                        empty = true;
                    }
                });

                if (empty) {
                    // $('.actions input').attr('disabled', 'disabled');
                    $('#btn_set_email').button( "disable" );
                    $('#btn_test_email').button( "disable" );   
                } else {
                    // $('.actions input').removeAttr('disabled');
                    $('#btn_set_email').button( "enable" );
                    $('#btn_test_email').button( "enable" );                    
                }
            });
            $('#btn_set_email').click(function() {
                var enable;
                var _TLS;
                var smtp = $('#input_SMTPIP').val();    
                var port = $('#input_SMTPPort').val();
                var user = $('#input_EmailUser').val();
                var password = $('#input_EmailPwd').val();
                var sender = $('#input_EmailSender').val();
                var receiver1 = $('#input_EmailReceiver1').val();
                var receiver2 = $('#input_EmailReceiver2').val();
                var receiver3 = $('#input_EmailReceiver3').val();
                var receiver4 = $('#input_EmailReceiver4').val();
                var receiver5 = $('#input_EmailReceiver5').val();

                if($("#Email_Enable_checkBox").prop('checked') == true) {
                    enable = true;
                } else {
                    enable = false;
                }

                if($("#Email_TLS_checkBox").prop('checked') == true) {
                    _TLS = true;
                } else {
                    _TLS = false;
                }                

                oUISysEmail.SetEmail(enable, smtp, port, user, password, _TLS, sender, receiver1, receiver2, receiver3, receiver4, receiver5);
            });
            $('#btn_reset_email').click(function() {
                oUISysEmail.ListEmail();
            });            
            $('#btn_test_email').click(function() {
                oUISysEmail.TestEmail();
            });                          
        };         

        oUISysEmail.ListEmail = function(){
            oHtml.blockPage();    
            var request = oRelayAjax.ListEmail();
            CallBackListEmail(request);            
        };

        function CallBackListEmail(request){
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
                $( "#Email_Enable_checkBox" ).prop( "checked", msg['Enable'] );
                $( "#Email_TLS_checkBox" ).prop( "checked", msg['TLS'] );
                $('#input_SMTPIP').val(msg['SMTP']);
                $('#input_SMTPPort').val(msg['Port']);
                $('#input_EmailUser').val(msg['User']);
                $('#input_EmailPwd').val(msg['Password']);
                $('#input_EmailSender').val(msg['Sender']);
                $('#input_EmailReceiver1').val(msg['Receiver1']);
                $('#input_EmailReceiver2').val(msg['Receiver2']);
                $('#input_EmailReceiver3').val(msg['Receiver3']);
                $('#input_EmailReceiver4').val(msg['Receiver4']);
                $('#input_EmailReceiver5').val(msg['Receiver5']);      

                if($('#input_SMTPIP').val().length == 0 || $('#input_SMTPPort').val().length == 0 || $('#input_EmailUser').val().length == 0 || $('#input_EmailPwd').val().length == 0 || $('#input_EmailReceiver1').val().length == 0) {
                    // $(':input[type="submit"]').prop('disabled', true);
                    // $('.btn_vm_select_operation').button( "enable" );
                    $('#btn_set_email').button( "disable" );         
                }

                if($('#input_SMTPIP').val().length == 0 && $('#input_SMTPPort').val().length == 0 && $('#input_EmailUser').val().length == 0 && $('#input_EmailReceiver1').val().length == 0) {
                    $('#btn_test_email').button( "disable" );            
                }

            });
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();
                alert(LanguageStr_System.Error_List_Email);
                // oError.CheckAuth(jqxhr.status,ActionStatus.ListEmail);
            });
        };

        oUISysEmail.TestEmail = function(){
            oHtml.blockPage();    
            var request = oRelayAjax.TestEmail();
            CallBackTestEmail(request);            
        };      

        function CallBackTestEmail(request){
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
                alert(LanguageStr_System.Email_Test_success);        
            });
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();
                alert(LanguageStr_System.Email_Test_fail);
                // oError.CheckAuth(jqxhr.status,ActionStatus.ListEmail);
            });
        };   

        oUISysEmail.SetEmail = function(enable, smtp, port, user, password, _TLS, sender, receiver1, receiver2, receiver3, receiver4, receiver5){
            oHtml.blockPage();    
            var request = oRelayAjax.SetEmail(enable, smtp, port, user, password, _TLS, sender, receiver1, receiver2, receiver3, receiver4, receiver5);
            CallBackSetEmail(request);            
        };
        
        function CallBackSetEmail(request){
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

                oUISysEmail.ListEmail();
                $('#btn_test_email').button( "enable" );
            });
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();
                alert(LanguageStr_System.Error_Set_Email);
                // oError.CheckAuth(jqxhr.status,ActionStatus.SetEmail);
            });
        };

       

        return oUISysEmail;
    }
};