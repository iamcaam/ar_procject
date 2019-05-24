var UIIPControl = {
    createNew:function(oInputHtml,oInputRelayAjax){
        var oHtml = oInputHtml;
        var oRelayAjax = oInputRelayAjax;        
        var oUIIPContorl={};
        var oError = {};
        var biSCSIBond;
        var nicCount;
        oUIIPContorl.Init = function(){   
            oError = ErrorHandle.createNew();              
            $('#btn_iscsi_ip_confirm').click(function() {
                SetiSCSIIPAction();
            });
            $('#btn_iscsi_ip_cancel').click(function() {
                oUIIPContorl.GetIPList();
            }); 
            $('#btn_ip_confirm').click(function() {
                oUIIPContorl.SetIPAction();
            });
            $('#btn_ip_cancel').click(function() {
                oUIIPContorl.GetIPList();
            });     
            $('#btn_chg_external_ip').click(function() {
                oUIIPContorl.SetExternalList();
            });
            $('#btn_clear_external_ip').click(function() {
                oUIIPContorl.GetExternalList();
            });             
        };                        
        
        oUIIPContorl.SetExternalList = function(){
            var setip =$('#input_ExternalIP').val();
            var exip_portfrom = $('#input_PortFrom').val(); // 新增外部IP及轉址功能設定  2017.09.04 william
            var exip_portcount = $('#input_PortCount').val(); // 新增外部IP及轉址功能設定  2017.09.04 william
            var max_portcount = 65535; // 新增外部IP及轉址功能設定  2017.09.04 william
            var min_portcount = 0; // 新增外部IP及轉址功能設定  2017.09.04 william
            var allow_portcount = (max_portcount - exip_portfrom) + 1; // 新增外部IP及轉址功能設定  2017.09.04 william
            if(setip.length > 0){
                var regexIP = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
                if (regexIP.test(setip) === false) {
                    var error_msg = ("External IP's format is wrong.");
                    alert(error_msg);
                    return false;
                }
            }            
            if(!setipcheck(setip)){
                    var error_msg = (LanguageStr_Network.IP_repeated_Error);
                    alert(error_msg);
                    return false;
            } 
            if(exip_portfrom.length===0) {
                    var error_msg = (LanguageStr_Network.IP_portfrom_Empty_Error);
                    alert(error_msg);
                    return false;

            }            
            if(exip_portfrom<min_portcount||exip_portfrom>max_portcount) {
                    var error_msg = (LanguageStr_Network.IP_portfrom_Error);
                    alert(error_msg);
                    return false;

            }
            if(exip_portcount>allow_portcount) {
                    var error_msg = (LanguageStr_Network.IP_portcount_Error1).replace("@",allow_portcount);
                    alert(error_msg);
                    return false;

            }
            if(exip_portcount>100) {
                    var error_msg = (LanguageStr_Network.IP_portcount_Error2);
                    alert(error_msg);
                    return false;
            }
            if(exip_portcount<1) {
                    var error_msg = (LanguageStr_Network.IP_portcount_Error3);
                    alert(error_msg);
                    return false;
            }            

            var reqdata = '[';
            for (var key in RestoreEXIPList) {
                reqdata += '{"ExternalIP":"' + RestoreEXIPList[key].ip + '",';
                reqdata += '"ExternalPort":' + RestoreEXIPList[key].port + ',';
                reqdata += '"ExternalPortCount":' + RestoreEXIPList[key].portcount;
                reqdata += '},';
            }
            reqdata += '{"ExternalIP":"' + setip + '",';
            reqdata += '"ExternalPort":' + exip_portfrom + ',';
            reqdata += '"ExternalPortCount":' + exip_portcount;
            reqdata += '}]';
            // var request = oRelayAjax.setexternal('"' + setip + '"');
            var request = oRelayAjax.setexternal(reqdata);
            oUIIPContorl.CallBackSetExternal(request);
        };

        function setipcheck(ip) {  // 新增外部IP及轉址功能設定  2017.09.04 william
            for (var key in RestoreEXIPList) {
                if(RestoreEXIPList[key].ip == ip) {
                    return false;
                }
            }
            return true;                        
        }
        
        oUIIPContorl.CallBackSetExternal = function(request){
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
                oUIIPContorl.GetExternalList();
            });
            request.fail(function(jqxhr, textStatus) {
                oHtml.stopPage();
                 // 新增外部IP及轉址功能設定  2017.09.14 william
                if(jqxhr.status == 500){
                    $('#input_ExternalIP').val(''); 
                    $('#input_PortFrom').val(''); 
                    $('#input_PortCount').val(''); 
                    var error_msg = LanguageStr_Network.ExternalIPandAD_RAID_Error;
                    alert(error_msg);
                }
                else
                    oError.CheckAuth(jqxhr.status,ActionStatus.SetExternal);
            });
        };                
        
        oUIIPContorl.GetExternalList = function(){
            var request = oRelayAjax.listexternal();
            oUIIPContorl.CallBackGetExternal(request);
        };
        
        oUIIPContorl.CallBackGetExternal = function(request){
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
                // 新增外部IP及轉址功能設定  2017.09.04 william
                // $('#input_ExternalIP').val(msg['ip'][0]);
                $('#input_ExternalIP').val(''); 
                $('#input_PortFrom').val(''); 
                $('#input_PortCount').val(''); 
                var rtnHtml = oHtml.CreateEXIPListHtmlByTable(msg);                 
                $('#Div_exipList').empty()              
                $('#Div_exipList').html(rtnHtml);
                RebindEXIPActionButtonEvent();
            });
            request.fail(function(jqxhr, textStatus) {
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.GetExternal);
            });
        };

        function RebindEXIPActionButtonEvent() { // 新增外部IP及轉址功能設定  2017.09.04 william
            $('.EXIPActionBtn').click(function(event){                               
                event.preventDefault();

                var btn_element = $(this);
                var id = btn_element.attr('id');
                var tmpcount = 1;

                var reqdata = '[';
                if(!ipcountcheck(id)) {
                    for (var key in RestoreEXIPList) {
                        if(tmpcount==EXIPListCount) {
                            reqdata += '{"ExternalIP":"' + RestoreEXIPList[key].ip + '",';
                            reqdata += '"ExternalPort":' + RestoreEXIPList[key].port + ',';
                            reqdata += '"ExternalPortCount":' + RestoreEXIPList[key].portcount;
                            reqdata += '}]';
                        }
                        else {
                            if(RestoreEXIPList[key]!=RestoreEXIPList[id]) {
                                reqdata += '{"ExternalIP":"' + RestoreEXIPList[key].ip + '",';
                                reqdata += '"ExternalPort":' + RestoreEXIPList[key].port + ',';
                                reqdata += '"ExternalPortCount":' + RestoreEXIPList[key].portcount;
                                reqdata += '},';
                            }
                        }
                        tmpcount += 1;
                     }
                }
                else {
                    for (var key in RestoreEXIPList) {
                        if(tmpcount==(EXIPListCount-1)) {
                            reqdata += '{"ExternalIP":"' + RestoreEXIPList[key].ip + '",';
                            reqdata += '"ExternalPort":' + RestoreEXIPList[key].port + ',';
                            reqdata += '"ExternalPortCount":' + RestoreEXIPList[key].portcount;
                            reqdata += '}]';
                        }
                        else if(tmpcount<(EXIPListCount-1)){
                            if(RestoreEXIPList[key]!=RestoreEXIPList[id]) {
                                reqdata += '{"ExternalIP":"' + RestoreEXIPList[key].ip + '",';
                                reqdata += '"ExternalPort":' + RestoreEXIPList[key].port + ',';
                                reqdata += '"ExternalPortCount":' + RestoreEXIPList[key].portcount;
                                reqdata += '},';
                            }
                        }
                        tmpcount += 1;
                    }
                }

                var request = oRelayAjax.setexternal(reqdata);
                oUIIPContorl.CallBackSetExternal(request);
            });  
        }

        function ipcountcheck(id) {  // 新增外部IP及轉址功能設定  2017.09.04 william
            var tmpcount = 1;
            for (var key in RestoreEXIPList) {
                if(RestoreEXIPList[key] == RestoreEXIPList[id] && tmpcount == EXIPListCount) {
                    return true;
                }
                tmpcount += 1;
            }
            return false;                        
        }                                
        
        oUIIPContorl.GetIPList = function(){
            var request = oRelayAjax.listvswitch();
            oUIIPContorl.CallBackGetIP(request);
        };
        
        oUIIPContorl.CallBackGetIP = function(request){
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
                nicCount = msg['Vswitchs'].length;      
                var rtnHtml = oHtml.processvSwitchData(msg);                 
                $('#div_ip_field').empty()              
                $('#div_ip_field').html(rtnHtml);
                 $('.iCheckRadioManualDHCP').iCheck({  
                    radioClass: 'iradio_flat-blue'    
                });                 
                if($('#0radio-manual').prop('checked')){
                    $('.inputv0').prop('disabled', false);
                }
                else{
                    $('.inputv0').prop('disabled', true);
                }
                $('.iCheckRadioManualDHCP').on('ifChanged', function(event){
                    var element = $(this);                    
                    var id = element.attr('id').substring(0,1);  
                        if(element.attr('id').substring(1) === "radio-manual"){
                            $('.inputv'+id).prop('disabled', false);
                        }   
                        else{
                            $('.inputv'+id).prop('disabled', true);                           
                        }
                        $('.inputv'+id).val('');                        
                });
                initEthElemClick('eth_elem');
                initAssignFreeClick();
            });
            request.fail(function(jqxhr, textStatus) {
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.GetIP);
            });
        };
        

        function initEthElemClick(className)
        {
            $('.'+className).click(function(event) {   
                var btnElement = $(this);             
                var id = btnElement.attr('id');
                var action = id.substring(0,1);
                var index = id.substring(1,2);
                if(action == 'a'){
                    $('.eth_assigned'+index).removeClass('eth_elem_click');
                }
                else{
                    $('.eth_free'+index).removeClass('eth_elem_click');
                }
                btnElement.addClass('eth_elem_click');
            });            
        }

        function initAssignFreeClick()
        {
            $('.to_free').click(function(event) {   
                var btnElement = $(this);              
                if(btnElement.hasClass('arrow_disable'))
                    return false;
                var index = $('.to_free').index(btnElement);
                var selectEth;     
                $(".eth_assigned"+index).each(function() { 
                    var element = $(this);
                    if(element.hasClass('eth_elem_click'))
                        selectEth=element;
                });
                if(typeof(selectEth) !== 'undefined'){
                    var id = selectEth.attr('id');
                    var dev = id.substring(2);   
                    vSwitchList[dev].indexSetVswitch = -1;    
                    selectEth.remove();
                    oHtml.FlashvSwitchFree();
                    initEthElemClick('free');  
                    if(index == 0){
                        if($(".eth_assigned"+index).length <= 1){
                            $('.to_free:eq(0)').addClass('arrow_disable');
                        }
                    }                 
                    if($(".eth_assigned"+index).length == 0){
                        $('.inputv'+index).val('');
                        $('.inputv'+index).prop('disabled', true);
                    }
                }                                 
            });      
            $('.to_assign').click(function(event) {   
                var btnElement = $(this);                 
                var index = $('.to_assign').index(btnElement);
                var selectEth;     
                $(".eth_free"+index).each(function() { 
                    var element = $(this);
                    if(element.hasClass('eth_elem_click'))
                        selectEth=element;
                });
                if(typeof(selectEth) !== 'undefined'){
                    var id = selectEth.attr('id');
                    var dev = id.substring(2);   
                    vSwitchList[dev].indexSetVswitch = index;                    
                    selectEth.remove();
                    oHtml.FlashvSwitchFree();
                    oHtml.FlashvSwitchAssign(index);
                    initEthElemClick('free');                
                    initEthElemClick('eth_assigned'+index);  
                    if(index == 0){
                        if($(".eth_assigned"+index).length > 1){
                            $('.to_free:eq(0)').removeClass('arrow_disable');
                        }
                    }    
                    if($(".eth_assigned"+index).length > 0){                       
                        $('.inputv'+index).prop('disabled', false);
                    }
                }
            });          
        }

        function iSCSIProcessHtml(msg){
            oHtml.EmptyiSCSIIPDiv();
            oHtml.appendiSCSIIPDiv(msg); 
            $('.iCheckBoxiSCSIBond').iCheck({  
                checkboxClass: 'icheckbox_minimal-blue'    
            });
//            $('.iSCSIiCheckRadioManualDHCP').iCheck({  
//                radioClass: 'iradio_flat-blue'    
//            }); 
            $('.iCheckBoxiSCSIBond').on('ifChanged', function(event){
                var element = $(this);
                if(element.prop('checked')){
                    $('#iscsi_ip').hide(); 
                    $('#iscsi_ip_bond').show(); 
                }
                else{
                    $('#iscsi_ip').show(); 
                    $('#iscsi_ip_bond').hide(); 
                }
            });                
//            $('.iSCSIiCheckRadioManualDHCP').on('ifChanged', function(event){
//                var element = $(this);
//                var id = element.attr('id').substring(0,2);  
//                if(element.attr('id').substring(2) === "iscsi-radio-manual"){
//                    $('#iSCSIIP'+id).prop('disabled', false);
//                    $('#iSCSIIPMask'+id).prop('disabled', false);  
//                }   
//                else{
//                    $('#iSCSIIP'+id).prop('disabled', true);
//                    $('#iSCSIIPMask'+id).prop('disabled', true);  
//                }
//                $('#iSCSIIP'+id).val('');
//                $('#iSCSIIPMask'+id).val('');
//                $('#iSCSIIPGateway'+id).val('');                   
//            });
        }        
        
        function InitNICFieldByBridgeCheckBox()
        {
//            $('#GatewaySelect0').empty();
            $.each($(".iCheckBoxNICDHCP:checked"),function(){
                var element = $(this);
                var id = element.attr('id').substring(9); 
                if(this.checked) {                       
                    $('#IP'+id).prop('disabled', true);
                    $('#IPMask'+id).prop('disabled', true);                     
                }
            });
            $.each($(".iCheckBoxNICBridge:checked"),function(){
                var checkelement = $(this);
                var checkid = checkelement.attr('id').substring(11); 
                $('#DHCPCheck'+checkid).prop('disabled', true);
                $('#DHCPCheck'+checkid).prop('checked',false);
                $('#IP'+checkid).prop('disabled', true);
                $('#IPMask'+checkid).prop('disabled', true);                                    
            });
            $.each($(".iCheckBoxNICBridge:not(:checked)"),function(){                    
                var uncheckelement = $(this);
                var uncheckid = uncheckelement.attr('id').substring(11);
                if(($(".iCheckBoxNICBridge").length - $(".iCheckBoxNICBridge:checked").length) === 1)
                    uncheckelement.prop('disabled',true);
                else
                    uncheckelement.prop('disabled',false);
                $('#DHCPCheck'+uncheckid).prop('disabled', false);
//                $('#GatewaySelect0').append(oHtml.CreateGatewaySelectOption(uncheckid));
            });
//            $('#GatewaySelect0').prop('selectedIndex', 0);
        }
        
        function RebindNICBridgeCheckBox()
        {
            $(".iCheckBoxNICDHCP").change(function(){
                var element = $(this);
                var id = element.attr('id').substring(9); 
                if(this.checked) {   
                    $('#IP'+id).val('');
                    $('#IPMask'+id).val('');
                    $('#IP'+id).prop('disabled', true);
                    $('#IPMask'+id).prop('disabled', true);                     
                }
                else{
                    $('#IP'+id).val('');
                    $('#IPMask'+id).val('');
                    $('#IP'+id).prop('disabled', false);
                    $('#IPMask'+id).prop('disabled', false);
                }                    
            });
            $(".iCheckBoxNICBridge").change(function() {
                var element = $(this);
                var id = element.attr('id').substring(11);                
                if(this.checked) {                    
                    $('#IP'+id).val('');
                    $('#IPMask'+id).val('');
                    $('#IP'+id).prop('disabled', true);
                    $('#IPMask'+id).prop('disabled', true); 
                    $('#DHCPCheck'+id).prop('disabled', true);
                    $('#DHCPCheck'+id).prop('checked',false);
                }
                else{
                    $('#IP'+id).prop('disabled', false);
                    $('#IPMask'+id).prop('disabled', false);
                    $('#DHCPCheck'+id).prop('disabled', false);
                }
                $('#GatewaySelect0').empty();
//                alert($(".iCheckBoxNICBridge:checked").length);
//                alert($(".iCheckBoxNICBridge").length);
//                $.each($(".iCheckBoxNICBridge:checked"),function(){
//                    var checkelement = $(this);
//                    var checkid = checkelement.attr('id').substring(11);  
//                    if(checkelement.attr('id') !== element.attr('id')){
//                        $('#IP'+checkid).prop('disabled', false);
//                        $('#IPMask'+checkid).prop('disabled', false);
//                        checkelement.prop('checked',false);
//                    }
//                });
                $.each($(".iCheckBoxNICBridge:not(:checked)"),function(){                    
                    var uncheckelement = $(this);
                    var uncheckid = uncheckelement.attr('id').substring(11);
                    if(($(".iCheckBoxNICBridge").length - $(".iCheckBoxNICBridge:checked").length) === 1)
                        uncheckelement.prop('disabled',true);
                    else
                        uncheckelement.prop('disabled',false);
                    $('#DHCPCheck'+uncheckid).prop('disabled', false);
                    $('#GatewaySelect0').append(oHtml.CreateGatewaySelectOption(uncheckid));
                });
                $('#GatewaySelect0').prop('selectedIndex', 0);
            });
            
        }
        
        function SetiSCSIIPAction(){
            var regexIP = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
//            var mask = /^[1-2]{1}[2,4,5,9]{1}[0,2,4,5,8]{1}\.[0-2]{1}[0,2,4,5,9]{1}[0,2,4,5,8]{1}\.[0-2]{1}[0,2,4,5,9]{1}[0,2,4,5,8]{1}\.[0-9]{1,3}$/; 
            var sJsonIP = '';
            var sJsonBond = '"Bond":';
            var bCheckHaveInputIP;
            var aGateway = [];
            var aIP = [];
            var aMask = [];
            var tmpiSCSIIPIndexList;
            var bInputIP;
            var iSetBond = 0;
            var iReboot = 0;
            var sAskReboot = '';
            if($('.iCheckBoxiSCSIBond').prop('checked')){
                tmpiSCSIIPIndexList = iSCSIPIndexList[1];
                sJsonBond += '1';
                iSetBond = 1;
                sAskReboot = "After Enable Port Trunking, then system will reboot. Do you want to setting now?"
            }
            else{
                tmpiSCSIIPIndexList = iSCSIPIndexList[0];
                sJsonBond += '0';
                sAskReboot = "After Disable Port Trunking, then system will reboot. Do you want to setting now?"
            }
            if(iSetBond !== biSCSIBond)
                iReboot = 1;
            var setindex = 0;
            var tmp_ip_list = [];            
            $.each(tmpiSCSIIPIndexList, function(index, element) {      
                bInputIP = false;
                if($('#iSCSIIP' + element['id']).val().length > 0 || $('#iSCSIIPMask' + element['id']).val().length > 0){
                    if (regexIP.test($('#iSCSIIP' + element['id']).val()) === false) {
                        var error_msg = (LanguageStr_Network.IP_Format_Error).replace('&',element['displayname']);
                        alert(error_msg);
                        return false;
                    }
                    if (validateNetMask($('#iSCSIIPMask' + element['id']).val()) === false) {
                        var error_msg = (LanguageStr_Network.IP_Mask_Format_Error).replace('&',element['displayname']);
                        alert(error_msg);
                        return false;
                    }  
                    
                    bCheckHaveInputIP = true;
                }
                if ($('#iSCSIIPGateway' + element['id']).val().length > 0) {
                    if(!regexIP.test($('#iSCSIIPGateway' + element['id']).val())){
                        alert(LanguageStr_Network.IP_Gateway_Error);
                        return false;
                    }
                    aGateway = $('#iSCSIIPGateway' + element['id']).val().split('.');
                    aIP = $('#iSCSIIP' + element['id']).val().split('.');
                    aMask = $('#iSCSIIPMask' + element['id']).val().split('.');
                    var iIPAndMask1 = aIP[0] & aMask[0];
                    var iIPAndMask2 = aIP[1] & aMask[1];
                    var iIPAndMask3 = aIP[2] & aMask[2];
                    var iIPAndMask4 = aIP[3] & aMask[3];
                    var iGatewayAndMask1 = aGateway[0] & aMask[0];
                    var iGatewayAndMask2 = aGateway[1] & aMask[1];
                    var iGatewayAndMask3 = aGateway[2] & aMask[2];
                    var iGatewayAndMask4 = aGateway[3] & aMask[3];
                    if (iIPAndMask1 !== iGatewayAndMask1 || iIPAndMask2 !== iGatewayAndMask2 || iIPAndMask3 !== iGatewayAndMask3 || iIPAndMask4 !== iGatewayAndMask4) {
                        var error_msg = (LanguageStr_Network.IP_Not_Same_LAN_Error).replace('&',element['displayname']);
                        alert(error_msg);
                        return false;
                    }
                }  
                if($('#iSCSIIP' + element['id']).val().length > 0){                    
//                    $('#iSCSIIPGateway' + element['id']).val('');
                    if(!CheckSameLAN(element['displayname'],$('#iSCSIIP' + element['id']).val(),$('#iSCSIIPMask' + element['id']).val(),IPList))
                        return false;
                    if(!CheckSameLAN(element['displayname'],$('#iSCSIIP' + element['id']).val(),$('#iSCSIIPMask' + element['id']).val(),tmp_ip_list))
                        return false;
                }
                if($('#iSCSIIPGateway' + element['id']).val().length > 0 ){
                    if( CheckSameLANHaveGateway(element['displayname'],$('#iSCSIIP' + element['id']).val(),$('#iSCSIIPMask' + element['id']).val(),tmp_ip_list))
                        return false;
                }
                bInputIP = true;
                sJsonIP += '{';              
                sJsonIP += '"DHCP":0,';                
                sJsonIP += '"IP":"' + $('#iSCSIIP' + element['id']).val() + '",';
                sJsonIP += '"Mask":"' + $('#iSCSIIPMask' + element['id']).val() + '",';
                sJsonIP += '"Gateway":"' + $('#iSCSIIPGateway' + element['id']).val() + '"';
                sJsonIP += '}';
                tmp_ip_list[index] = new IPClass(element['id'], element['displayname'], element['dev'], $('#iSCSIIP' + element['id']).val(), $('#iSCSIIPMask' + element['id']).val(),0, $('#iSCSIIPGateway' + element['id']).val());
                if(setindex===0 && tmpiSCSIIPIndexList.length > 1)
                    sJsonIP += ',';                
                setindex++;
            });                        
            if(bInputIP){
                if(bCheckHaveInputIP){
                    var confirmable = true;
                    if(iReboot === 1){
                        confirmable = confirm(sAskReboot);
                    }
                    if(confirmable)
                        SetiSCSIIP(sJsonBond,sJsonIP,iReboot);
                }
                else{
                    alert(LanguageStr_Network.IP_No_IP_Error);
                }
            }           
        };
        
        function SetiSCSIIP(sJsonBond,sJsonIP,iReboot)
        {
            var request = oRelayAjax.SetiSCSIIP(sJsonBond,sJsonIP,iReboot);
            CallBackSetiSCSIIP(request,iReboot); 
        };
        
        function CallBackSetiSCSIIP(request,iReboot){
            oHtml.blockPage();
            request.done(function(msg, statustext, jqxhr) {
                if(iReboot === 1){
                    oHtml.stopPage();
                    oHtml.blockPageMsg(LanguageStr_AdminMain.RebootMsg);
                }
                else
                    oUIIPContorl.GetIPList();
//               alert(jqxhr.responseText); 
            });
            request.fail(function(jqxhr, textStatus) {
//                alert(jqxhr.responseText); 
                oHtml.stopPage();
                if(iReboot === 1){                    
                    oHtml.blockPageMsg(LanguageStr_AdminMain.RebootMsg);
                }
                else{                
                    alert(LanguageStr_Network.IP_Set_Fail);
                    oUIIPContorl.GetIPList();
                }
            });
        } 
        
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

        
        oUIIPContorl.SetIPAction = function(){
            var regexIP = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
            //var regmask = /^[1-2]{1}[2,4,5,9]{1}[0,2,4,5,8]{1}\.[0-2]{1}[0,2,4,5,9]{1}[0,2,4,5,8]{1}\.[0-2]{1}[0,2,4,5,9]{1}[0,2,4,5,8]{1}\.[0-9]{1,3}$/; 
//            var regmask = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(/([1-9]|[1-2]d|3[0-2]))$/;
            var bCheckHaveInputIP;
            var bInputIP = false;
//            var bInputGateway = false;
            var bInputDNS = false;
            var sJsonIP = '';
            var sJsonGateway = '';
            var sJsonDNS = '';
            var aGateway = [];
            var aIP = [];
            var aMask = [];
            var ipSet = [];
            var tmp_ip_list = [];

            for(var i=0;i<nicCount;i++){
                ipSet[i] = [];
            }
            var indexSet;
            for(var k in vSwitchList){
                indexSet = vSwitchList[k].indexSetVswitch;
                if( indexSet == 0)
                    bCheckHaveInputIP = true;
                if(indexSet != -1)
                    ipSet[indexSet].push(vSwitchList[k].name);
            }
            if(!bCheckHaveInputIP || ($('.vswitch_ip_0').val().length <= 0 && $('#0radio-manual').prop('checked'))){
                alert(LanguageStr_Network.IP_No_IP_Error);
                return false;
            }
            var ip = '';
            var mask = '';
            var gateway = '';
            var element;
            var dhcp = false;
            var tmpIndex;
            for(var index in ipSet){
                element = ipSet[index];
                if(index != 0){
                    sJsonIP += ',';
                }
                ip = '';
                mask = '';
                gateway = '';
                dhcp = false;
                if(typeof($('#' + index + 'radio-dhcp')) !== 'undefined'){
                    dhcp = $('#' + index + 'radio-dhcp').prop('checked') === true ? 1: 0;
                }
                tmpIndex = parseInt(index)+1;
                displayname = 'vSwitch ' + tmpIndex;
                if(element.length > 0){
                    ip = $('.vswitch_ip_' + index).val();
                    mask = $('.vswitch_mask_' + index).val();
                    gateway = $('.vswitch_gateway_' + index).val();
                    if(mask.length > 0){
                        if (validateNetMask(mask) === false) {
                        var error_msg = (LanguageStr_Network.IP_Mask_Format_Error).replace('&',displayname);
                        alert(error_msg);
                        return false;
                        }
                        if (regexIP.test(ip) === false) {
                            var error_msg = (LanguageStr_Network.IP_Format_Error).replace('&',displayname);
                            alert(error_msg);
                            return false;
                        }                  
                        if (gateway.length > 0 ) {
                            aGateway = gateway.split('.');
                            aIP = ip.split('.');
                            aMask = mask.split('.');
                            var iIPAndMask1 = aIP[0] & aMask[0];
                            var iIPAndMask2 = aIP[1] & aMask[1];
                            var iIPAndMask3 = aIP[2] & aMask[2];
                            var iIPAndMask4 = aIP[3] & aMask[3];
                            var iGatewayAndMask1 = aGateway[0] & aMask[0];
                            var iGatewayAndMask2 = aGateway[1] & aMask[1];
                            var iGatewayAndMask3 = aGateway[2] & aMask[2];
                            var iGatewayAndMask4 = aGateway[3] & aMask[3];
                            if (iIPAndMask1 !== iGatewayAndMask1 || iIPAndMask2 !== iGatewayAndMask2 || iIPAndMask3 !== iGatewayAndMask3 || iIPAndMask4 !== iGatewayAndMask4) {
                                var error_msg = (LanguageStr_Network.IP_Not_Same_LAN_Error).replace('&',displayname);
                                alert(error_msg);
                                return false;
                            }
                        }
                    }
                    else{
                        if (ip.length > 0) {
                            var error_msg = (LanguageStr_Network.IP_Mask_Empty_Error).replace('&',displayname);
                            alert(error_msg);
                            return false;
                          }             
                    }
                    sJsonIP += '{"DHCP":' + dhcp + ',"IP":"'+ip+'","Mask":"'+mask+'","Gateway":"'+gateway+'","Devs":[';
                    $.each(element,function(devIndex,devElement){
                        if(devIndex != 0)
                            sJsonIP += ','
                        sJsonIP += '"'+devElement+'"';
                    });
                    sJsonIP += ']}';
                }
                else{
                    sJsonIP += '{"DHCP":' + dhcp + ',"IP":"'+ip+'","Mask":"'+mask+'","Gateway":"'+gateway+'","Devs":[]}';
                }
                if(gateway.length > 0 ){
                    if( CheckSameLANHaveGateway(displayname,ip,mask,tmp_ip_list))
                        return false;
                }
                if(ip.length > 0){
                    if( CheckSameIP(displayname,ip,mask,tmp_ip_list))
                        return false;
                }
                tmp_ip_list[index] = new IPClass(index, displayname, '', ip, mask, 0, gateway);
            }

            // var setindex = 0;
            // $.each(IPList, function(index, element) {
            //     bInputIP = false;
            //     if (element['bridge'] === 0 && $('#IPMask' + element['id']).val().length > 0) {
            //         if (validateNetMask($('#IPMask' + element['id']).val()) === false) {
            //             var error_msg = (LanguageStr_Network.IP_Mask_Format_Error).replace('&',element['displayname']);
            //             alert(error_msg);
            //             return false;
            //         }
            //         if (regexIP.test($('#IP' + element['id']).val()) === false) {
            //             var error_msg = (LanguageStr_Network.IP_Format_Error).replace('&',element['displayname']);
            //             alert(error_msg);
            //             return false;
            //         }
            //         bCheckHaveInputIP = true;                
            //         if ($('#IPGateway' + element['id']).val().length > 0 ) {
            //             aGateway = $('#IPGateway' + element['id']).val().split('.');
            //             aIP = $('#IP' + element['id']).val().split('.');
            //             aMask = $('#IPMask' + element['id']).val().split('.');
            //             var iIPAndMask1 = aIP[0] & aMask[0];
            //             var iIPAndMask2 = aIP[1] & aMask[1];
            //             var iIPAndMask3 = aIP[2] & aMask[2];
            //             var iIPAndMask4 = aIP[3] & aMask[3];
            //             var iGatewayAndMask1 = aGateway[0] & aMask[0];
            //             var iGatewayAndMask2 = aGateway[1] & aMask[1];
            //             var iGatewayAndMask3 = aGateway[2] & aMask[2];
            //             var iGatewayAndMask4 = aGateway[3] & aMask[3];
            //             if (iIPAndMask1 !== iGatewayAndMask1 || iIPAndMask2 !== iGatewayAndMask2 || iIPAndMask3 !== iGatewayAndMask3 || iIPAndMask4 !== iGatewayAndMask4) {
            //                 var error_msg = (LanguageStr_Network.IP_Not_Same_LAN_Error).replace('&',element['displayname']);
            //                 alert(error_msg);
            //                 return false;
            //             }
            //         }
            //     } else {
            //         if (element['bridge'] === 0 && $('#IP' + element['id']).val().length > 0) {
            //             var error_msg = (LanguageStr_Network.IP_Mask_Empty_Error).replace('&',element['displayname']);
            //             alert(error_msg);
            //             return false;
            //         }                    
            //     }
            //     var isCheckDHCP = $('#' + element['id'] + 'radio-dhcp').prop('checked') === true ? 1: 0;
            //     var ip = isCheckDHCP === 0 ?$('#IP' + element['id']).val():'';
            //     var mask = isCheckDHCP === 0 ? $('#IPMask' + element['id']).val() : '';
            //     var gateway = isCheckDHCP === 0 ? $('#IPGateway' + element['id']).val() : '';
            //     if(!isCheckDHCP){
            //         if(iSCSI === 1){
            //             var tmpiSCSIList = biSCSIBond === 0 ? iSCSIPIndexList[0] : iSCSIPIndexList[1];                    
            //             if(!CheckSameLAN(element['displayname'],ip,mask,tmpiSCSIList))
            //                 return false;
            //         }
            //     }
            //     bInputIP = true;                                             
            //     if(element['bridge'] === 0){
            //         if (setindex !== 0)
            //             sJsonIP += ',';     
            //         sJsonIP += '{"id":' + element['id'] + ',"name":"' + element['name'] + '","ip":"' + ip + '","mask":"' + 
            //             mask  + '","bridge":' + 0 + ',"dhcp":' + isCheckDHCP + ',"gateway":"' + gateway +'"}';              
            //         setindex++;
            //     }
            // });
            // if($('.iCheckRadioDHCP:checked').length > 0)
            //     bCheckHaveInputIP = true;
            // if (!bCheckHaveInputIP) {
            //     alert(LanguageStr_Network.IP_No_IP_Error);
            //     return false;
            // }
            // if (!bInputIP)
            //     return false;
            if ($('#DNS0').val().length > 0 && !regexIP.test($('#DNS0').val())) {
                alert(LanguageStr_Network.IP_DNS_Format_Error);
                return false;
            }
            else{
                sJsonDNS += '"' + $('#DNS0').val() + '"'     
            }
            // $.each(DNSList, function(index, element) {
            //     if ($('#DNS' + element['id']).val().length > 0 && !regexIP.test($('#DNS' + element['id']).val())) {
            //         alert(LanguageStr_Network.IP_DNS_Format_Error);
            //         return false;
            //     }
            //     bInputDNS = true;
            //     sJsonDNS += '{"ip":"' + $('#DNS' + element['id']).val() + '"}';
            //     return false;
            // });
            // if (!bInputDNS)
            //     return false;
            // alert(sJsonIP);
//            alert(sJsonGateway);
            // alert(sJsonDNS);
            oUIIPContorl.SetIP(sJsonIP, sJsonDNS);           
        };    
        
        function CheckSameIP(displayname,ip,mask,iplist){
            var atmpIP;
            var atmpMask;
            var itmpIPAndMask1;
            var itmpIPAndMask2;
            var itmpIPAndMask3;
            var itmpIPAndMask4;            
            var bCheckResult = false;
            $.each(iplist,function(index,element){
                if(element['ip'] == ip){                    
                    var error_msg = (LanguageStr_Network.IP_Same_IP).replace('&',element['displayname']).replace('#',displayname);
                    alert(error_msg);
                    bCheckResult = true;
                    return false;                    
                }
            });
            return bCheckResult;
        }

        function CheckSameLANHaveGateway(displayname,ip,mask,iplist){
            var atmpIP;
            var atmpMask;
            var itmpIPAndMask1;
            var itmpIPAndMask2;
            var itmpIPAndMask3;
            var itmpIPAndMask4;
            var aIP = ip.split('.');
            var aMask = mask.split('.');            
            var iIPAndMask1 = aIP[0] & aMask[0];
            var iIPAndMask2 = aIP[1] & aMask[1];
            var iIPAndMask3 = aIP[2] & aMask[2];
            var iIPAndMask4 = aIP[3] & aMask[3];   
            var bCheckResult = false;
            $.each(iplist,function(index,element){
                if(element['gateway'].length > 0){
                    atmpIP = element['ip'].split('.');
                    atmpMask = element['mask'].split('.');
                    itmpIPAndMask1 = atmpIP[0] & atmpMask[0];
                    itmpIPAndMask2 = atmpIP[1] & atmpMask[1];
                    itmpIPAndMask3 = atmpIP[2] & atmpMask[2];
                    itmpIPAndMask4 = atmpIP[3] & atmpMask[3];                  
                    if (iIPAndMask1 === itmpIPAndMask1 && iIPAndMask2 === itmpIPAndMask2 && iIPAndMask3 === itmpIPAndMask3 && iIPAndMask4 === itmpIPAndMask4) {
                        var error_msg = (LanguageStr_Network.IP_Same_LAN_Not_Input_Gateway).replace('&',element['displayname']).replace('#',displayname);
                        alert(error_msg);
                        bCheckResult = true;
                        return false;
                    }
                }
            });
            return bCheckResult;
        }
        
        function CheckSameLAN(displayname,ip,mask,iplist){
            var atmpIP;
            var atmpMask;
            var itmpIPAndMask1;
            var itmpIPAndMask2;
            var itmpIPAndMask3;
            var itmpIPAndMask4;
            var aIP = ip.split('.');
            var aMask = mask.split('.');            
            var iIPAndMask1 = aIP[0] & aMask[0];
            var iIPAndMask2 = aIP[1] & aMask[1];
            var iIPAndMask3 = aIP[2] & aMask[2];
            var iIPAndMask4 = aIP[3] & aMask[3];   
            var bCheckResult = true;
            $.each(iplist,function(index,element){
                if(element['ip'].length > 0){
                    atmpIP = element['ip'].split('.');
                    atmpMask = element['mask'].split('.');
                    itmpIPAndMask1 = atmpIP[0] & atmpMask[0];
                    itmpIPAndMask2 = atmpIP[1] & atmpMask[1];
                    itmpIPAndMask3 = atmpIP[2] & atmpMask[2];
                    itmpIPAndMask4 = atmpIP[3] & atmpMask[3];                  
                    if (iIPAndMask1 === itmpIPAndMask1 && iIPAndMask2 === itmpIPAndMask2 && iIPAndMask3 === itmpIPAndMask3 && iIPAndMask4 === itmpIPAndMask4) {
                        var error_msg = (LanguageStr_Network.IP_iSCSI_Same_LAN_Error).replace('&',element['displayname']).replace('#',displayname);
                        alert(error_msg);
                        bCheckResult = false;
                        return false;
                    }
                }
            });
            return bCheckResult;
        }
        
        oUIIPContorl.SetIP=function(sJsonIP, sJsonDNS)
        {
            var confirmable = confirm(LanguageStr_Network.IP_After_Setting_Ask);
            if(confirmable){
                var request = oRelayAjax.setip(sJsonIP, sJsonDNS);
                oUIIPContorl.CallBackSetIP(request); 
            }
        };
        
        oUIIPContorl.CallBackSetIP=function(request){
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
                $('#dialog_vm_msg_reboot').html('<h3>' + LanguageStr_Network.IP_After_Chanage_Msg + '</h3>');
                $( "#dialog_vm_message_reboot" ).dialog('open'); 
                ipReboot();

            });
            request.fail(function(jqxhr, textStatus) {
                if (jqxhr.status !== 400) {} else {
                    oHtml.stopPage();
                    alert(LanguageStr_Network.IP_Set_Fail);
                }

            });
        };          

        function ipReboot(){
            var request = oRelayAjax.Reboot();  
            CallBackIPReboot(request);
        }       

        function CallBackIPReboot(request){
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
        return oUIIPContorl;
    }
};

