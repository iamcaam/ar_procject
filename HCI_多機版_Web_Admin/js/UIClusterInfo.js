var UIClusterInfo = {
    createNew:function(oInputHtml,oInputRelayAjax){ 
        var oHtml = oInputHtml;
        var oRelayAjax = oInputRelayAjax;                        
        var oUIClusterInfo={};
        var oError = {};      
        oUIClusterInfo.Init = function(){               
            oError = ErrorHandle.createNew();    
            $('#btn_clear_gluster').click(function() {
                var confirmable = confirm(LanguageStr_Cluster.AskReboot);
                if (confirmable === true) {                    
                    oUIClusterInfo.ClearGluster();
                    Reboot();
                }                
                
            });

            $('#btn_refresh_gluster_Info').click(function() {
                oUIClusterInfo.ListGlusterState();
            }); 

            $('#btn_set_Force_Master').click(function() {
                // var confirmable = confirm(LanguageStr_Cluster.Normal_mode);
                // if (confirmable === true) {                    
                    ForceSetMaster();
                // }  
            }); 

            $('#btn_set_Normal_Mode').click(function() {
                // var confirmable = confirm(LanguageStr_Cluster.Normal_mode);
                // if (confirmable === true) {                    
                    oUIClusterInfo.SetGlusterNormal();
                // }  
            });             

        };                                           
        
        oUIClusterInfo.ListGlusterState = function(){
            oHtml.blockPage();    
            var request = oRelayAjax.ListGlusterState();
            CallBackListGlusterState(request);            
        };
        
        function CallBackListGlusterState(request){
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
                $('#label_cluster_state').text(CreateClusterTypeText(msg));

                if(msg['MaintainState']) {
                    $('#label_state_Maintain').text('(' + LanguageStr_Cluster.Maintain_mode + ')');
                    $('#btn_set_Normal_Mode').button("enable");
                } else {
                    $('#label_state_Maintain').text('');
                    $('#btn_set_Normal_Mode').button("disable");
                }
                if(msg['GlusterState']==11) {                    
                    $('#btn_set_Force_Master').button("enable");
                } else {                    
                    $('#btn_set_Force_Master').button("disable");
                }
            });
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();
                //oError.CheckAuth(jqxhr.status,ActionStatus.ListHostName);
            });
        };

        function CreateClusterTypeText(data){
            var input_type = data['GlusterState'];
            var type = '';
            switch(input_type){
                case 0:
                    type = LanguageStr_Cluster.State00;
                    break;
                case 1:
                    type = LanguageStr_Cluster.State01;
                    break;
                case 2:
                    type = LanguageStr_Cluster.State02;
                    break;
                case 3:
                    type = LanguageStr_Cluster.State03;
                    break;
                case 4:
                    type = LanguageStr_Cluster.State04;
                    break;
                case 5:
                    type = LanguageStr_Cluster.State05;
                    break;
                case 6:
                    type = LanguageStr_Cluster.State06;
                    break;
                case 7:
                    type = LanguageStr_Cluster.State07;
                    break;
                case 8:
                    type = LanguageStr_Cluster.State08;
                    break;
                case 9:
                    type = LanguageStr_Cluster.State09;
                    break;
                case 11:
                    type = LanguageStr_Cluster.State11;
                    break;
                case 997:
                    type = LanguageStr_Cluster.State997;
                    break;
                case 998:
                    type = LanguageStr_Cluster.State998;
                    break;
                case 999:
                    type = LanguageStr_Cluster.State999;
                    break;                                        
                default : 
                    type = input_type;
            };
            var vms = 'No';
            var db = 'No'
            var vmsIP = '';
            var dnsPing = '';
            if(typeof(data['VMS']) !== "undefined"){
                vms = data['VMS'] == true ? 'Yes' : 'No';
            }
            if(typeof(data['DataBase']) !== "undefined"){
                db = data['DataBase'] == true ? 'Yes' : 'No';
            }
            if(typeof(data['VMSIP']) !== "undefined"){
                vmsIP = data['VMSIP'];
            }
            if(typeof(data['PingDNS']) !== "undefined"){
                dnsPing = data['PingDNS'] == true ? 'OK' : 'Fail';
            }
            type = type + ' (AcroVMS : ' + vms + ', Database : ' + db + ', VMS IP : ' + vmsIP +  ', Ping DNS : ' + dnsPing +')';
            return type;
        }        


        oUIClusterInfo.ClearGluster = function(){
            oHtml.blockPage();    
            var request = oRelayAjax.ClearGluster();
            CallBackClearGluster(request);            
        };

        function CallBackClearGluster(request){
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
                
            });
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();
                //oError.CheckAuth(jqxhr.status,ActionStatus.ListHostName);
            });
        };

        oUIClusterInfo.SetGlusterNormal = function(){
            oHtml.blockPage();    
            var request = oRelayAjax.SetGlusterNormal();
            CallBackSetGlusterNormal(request);            
        };

        function CallBackSetGlusterNormal(request){
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

                oUIClusterInfo.ListGlusterState();
                
            });
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();
                //oError.CheckAuth(jqxhr.status,ActionStatus.ListHostName);
            });
        };        

        function ForceSetMaster(){
            oHtml.blockPage();    
            var request = oRelayAjax.SetGlusterForceMaster();
            CallBackForceSetMaster(request);            
        };

        function CallBackForceSetMaster(request){
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

                oUIClusterInfo.ListGlusterState();
                
            });
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();
                //oError.CheckAuth(jqxhr.status,ActionStatus.ListHostName);
            });
        };      

        return oUIClusterInfo;
    }
};