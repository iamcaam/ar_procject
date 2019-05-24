var CreateHtml = {   
    createNew: function() {                
        var table_relay = $('#table_relay');
        var table_log = $('#table_log');
        var table_detail=$('#table_detail');
        var ip_field = $('#div_ip_field');
        var iscsi_ip_field = $('#div_iscsi_ip_field');
        var source_area = $('#UrlArea');
        var modify_source_area = $('#ModifySourceArea');
        var Table_Result_Mass_Entry = $('#MassEntryResulTable');
        var createobj = {};
        createobj.InitWaitDialog = function(){
            $( "#dialog_wait" ).dialog({
                title: "Attention",
                modal: true,
                resizable: false,
                draggable: false,
                closeOnEscape: false,
                autoOpen: false,
                dialogClass: "no-close",
                height:280,
                width:550               
            });           
        };                             
        createobj.EmptyIPDiv = function() {
            ip_field.empty();
        };
        createobj.appendIPDiv = function(ip_list) {
            IPList = [];
            GatewayList = [];
            DNSList = [];
            var tmp_ip_item;
            var tmp_dns_item;
            var tmp_gateway_item;
            var tmp_ip_displayidex;
            var tmp_ip;
            var tmp_mask;
            var tmp_ip_html;
            var tmp_gateway_html;
            var tmp_dns_html;
            var tmp_bol_marge;
            var tmp_bridge;
            var ip_html = '';
            var BrowserHeight = $(window).height();
            var div_height = BrowserHeight-290; // vSwitch william 2017.01.19 原值：350
            ip_html += '<div id="div_ip_content" style="width:685px;height:' + div_height + 'px;">'; // vSwitch william 2017.01.19 取消overflow:auto,原值：width:550px
            ip_html += '<div class="vSwitch_wrapper">';  // vSwitch william 2017.01.19 新增
            $.each(ip_list, function(index, element) {
                switch (index) {
                    case 'managernic':                       
                        $.each(element, function(ipindex, ipelement) {
                            tmp_bol_marge = true;
                        tmp_bridge = 0;
                        tmp_ip = ipelement['ipv4'];
                        tmp_mask = ipelement['mask'];
                        var display_name = LanguageStr_Network.IP_Viewer_Mgt;
                        tmp_ip_item = new IPClass(0, display_name, ipelement['dev'], tmp_ip, tmp_mask,0, ipelement['gateway']);
                        IPList[0] = tmp_ip_item;
                        // tmp_ip_html = createobj.CreateIPHtml1(0, display_name, tmp_ip, tmp_mask, false,tmp_bridge,ipelement['dhcp'],ipelement['gateway']);
                        // tmp_ip_html = createobj.CreatevSwitchHtml(0, display_name, tmp_ip, tmp_mask, false,tmp_bridge,ipelement['dhcp'],ipelement['gateway']); // vSwitch william 2017.01.19 新增
                        ip_html += tmp_ip_html;
                        tmp_bridge = 1;
                        tmp_ip = '';
                        tmp_mask = '';
                        var display_name = LanguageStr_Network.IP_Virtual_Desktop;                        
                        // tmp_ip_html = createobj.CreateIPHtml1(0, display_name, tmp_ip, tmp_mask, true,tmp_bridge,0,"");
                        //tmp_ip_html = createobj.CreatevSwitchHtml(0, display_name, tmp_ip, tmp_mask, true,tmp_bridge,0,""); // vSwitch william 2017.01.19 新增
                        ip_html += tmp_ip_html;
//                            tmp_ip_displayidex = ipindex + 1;                            
////                            switch(ipindex){
////                                case 0:
////                                    display_name = 'Viewer & Mgt. ' + LanguageStr_Network.IP_Network;
////                                    break;
////                                case 1:
////                                    display_name = 'Virtual Desktop ' + LanguageStr_Network.IP_Network;
////                                    break;
////                                case 2:
////                                    display_name = 'iSCSI ' + LanguageStr_Network.IP_Network;
////                                    break;
////                            }
//                            tmp_ip_item = new IPClass(ipindex, display_name, ipelement['dev'], tmp_ip, tmp_mask,0, ipelement['gateway']);
////                            tmp_ip_item = new IPClass(ipindex, 'NIC' + tmp_ip_displayidex, ipelement['name'], tmp_ip, tmp_mask,ipelement['bridge']);
//                            IPList[ipindex] = tmp_ip_item;
//                            if (ipindex === 0)
//                                tmp_bol_marge = false;
////                            if(ipelement['bridge'] === 0)
//                                tmp_bridge = 0;
////                            else
////                                tmp_bridge = 1;                                                                                    
//                            tmp_ip_html = createobj.CreateIPHtml(ipindex, display_name, tmp_ip, tmp_mask, tmp_bol_marge,tmp_bridge,ipelement['dhcp'],ipelement['gateway']);
//                            ip_html += tmp_ip_html;
                        });
//                        var display_name = 'Viewer & Mgt. ' + LanguageStr_Network.IP_Network;
                        ip_html += '</div>';  // vSwitch william 2017.01.19 新增
                        break;
//                    case 'Gateway':
//                        $.each(element, function(gatewayindex, gatewayelement) {
//                            tmp_ip = gatewayelement['ip'];
//                            tmp_gateway_item = new GatewayClass(gatewayindex, tmp_ip, gatewayelement['bindport']);
//                            GatewayList[gatewayindex] = tmp_gateway_item;
//                            tmp_gateway_html = createobj.CreateGatewayHtml(gatewayindex, tmp_ip, gatewayelement['bindport']);
//                            ip_html += tmp_gateway_html;
//                            return false;
//                        });
//                        break;
                    case 'dns':
                        $.each(element, function(dnsindex, dnselement) {
//                            tmp_ip = dnselement['ip'];
                            tmp_ip = dnselement;
                            tmp_dns_item = new DNSClass(dnsindex, tmp_ip);
                            DNSList[dnsindex] = tmp_dns_item;
                            tmp_dns_html = createobj.CreateDNSHtml(dnsindex, tmp_ip);
                            ip_html += tmp_dns_html;
                            return false;
                        });
                        break;
                }
            });
            ip_html += '<br></div>';            
            ip_field.html(ip_html);           
        };
        createobj.EmptyiSCSIIPDiv = function() {
            iscsi_ip_field.empty();
        };
        createobj.appendiSCSIIPDiv = function(ip_list) {                     
            var tmp_ip_displayidex;
            var tmp_ip;
            var tmp_mask;
            var tmp_gateway;
            var tmp_ip_html;            
            var tmp_bol_marge;            
            var ip_html = '';
            iSCSIPIndexList = [];
            var BrowserHeight = $(window).height();
            var div_height = BrowserHeight-350;
            ip_html += '<div id="div_ip_content" style="width:550px;height:' + div_height + 'px;overflow:auto">';            
            
            if(ip_list['iscsibond'] === 0){
                ip_html += '<input type="checkbox" class="iCheckBoxiSCSIBond" id="icheckiscsi">';
                ip_html += '<label style="cursor: pointer;padding-left: 5px;font-size: 14px;vertical-align: middle;" for="icheckiscsi">Enable Port Trunking</label>';
                ip_html += '<div id="iscsi_ip">';                
            }
            else{
                ip_html += '<input type="checkbox" class="iCheckBoxiSCSIBond" id="icheckiscsi" checked>';
                ip_html += '<label style="cursor: pointer;padding-left: 5px;font-size: 14px;vertical-align: middle;" for="icheckiscsi">Enable Port Trunking</label>';
                ip_html += '<div id="iscsi_ip_bond">';                
            }            
            ip_html += '<div style="height:10px"></div>';   
            iSCSIPIndexList[0] = [];
            iSCSIPIndexList[1] = [];
            $.each(ip_list['iscsinic'], function(ipindex, ipelement) {                
                tmp_bol_marge = true;
                tmp_ip = ipelement['ipv4'];
                tmp_mask = ipelement['mask'];
                tmp_gateway = ipelement['gateway'];
                tmp_ip_displayidex = ipindex + 1;                
                var display_name = '';                
                if(ip_list['iscsinic'].length > 1){                   
                    display_name = 'iSCSI ' + LanguageStr_Network.IP_Network + ' ' + tmp_ip_displayidex;                                                
                }
                else{
                    display_name = 'iSCSI ' + LanguageStr_Network.IP_Network;
                }                        
                var tmpindex = 0;
                if(ip_list['iscsibond'] === 0){
                    tmpindex = '0' + ipindex;
                    iSCSIPIndexList[0][ipindex] = new IPClass(tmpindex, display_name,'',ipelement['ipv4'],ipelement['mask'],0,ipelement['gateway']);
                }
                else{
                    tmpindex = '1' + ipindex;
                    iSCSIPIndexList[1][ipindex] = new IPClass(tmpindex, display_name,'',ipelement['ipv4'],ipelement['mask'],0,ipelement['gateway']);
                }
                if (ipindex === 0)
                    tmp_bol_marge = false;                                                                                                   
                tmp_ip_html = createobj.CreateiSCSIIPHtml(tmpindex, display_name, tmp_ip, tmp_mask, tmp_bol_marge,ipelement['dhcp'],tmp_gateway);
                ip_html += tmp_ip_html;                        
            });
            ip_html += '</div>';
            var hide_bond = ip_list['iscsibond']===0?1:0;            
            ip_html += CreateiSCSIHideIPHtml(hide_bond);
            ip_html += '<br></div>';            
            iscsi_ip_field.html(ip_html);           
        };
        
        function CreateiSCSIHideIPHtml(isBond){            
            var ip_html = '';
            var tmp_ip_html;
            var iplength = 1;
            var tmp_bol_marge; 
            var tmp_ip_displayidex;            
            ip_html += '<div style="height:10px"></div>';   
            if(isBond === 0){
                iplength = 2;
                ip_html += '<div id="iscsi_ip" style="display:none">';                
            }
            else{                
                ip_html += '<div id="iscsi_ip_bond" style="display:none">';                
            }                        
           
            for(var i = 0; i < iplength; i++) { 
                tmp_bol_marge = true;
                tmp_ip_displayidex = i + 1;                
                var display_name = '';
                if(iplength > 1){                   
                    display_name = 'iSCSI ' + LanguageStr_Network.IP_Network + ' ' + tmp_ip_displayidex;                                                
                }
                else{
                    display_name = 'iSCSI ' + LanguageStr_Network.IP_Network;
                }            
                 var tmpindex = 0;
                if(isBond === 0){
                    tmpindex = '0' + i;
                    iSCSIPIndexList[0][i] = new IPClass(tmpindex, display_name,'','','',0,'');
                }
                else{
                    tmpindex = '1' + i;
                    iSCSIPIndexList[1][i] = new IPClass(tmpindex, display_name,'','','',0,'');                
                }
                if (i === 0)
                    tmp_bol_marge = false;                                                                                                   
                tmp_ip_html = createobj.CreateiSCSIIPHtml(tmpindex, display_name, '', '', tmp_bol_marge,0,'');
                ip_html += tmp_ip_html;                        
            };
            ip_html += '</div>';
            return ip_html;
        }
        
        createobj.CreateiSCSIIPHtml = function(id, displayname, ip, mask, bmarge,dhcp,gateway) {
            var rtnhtml = '<fieldset ';
            if (bmarge)
                rtnhtml += 'style="margin-top:10px"';            
            rtnhtml += '><legend style="">' + displayname + '</legend>';                              
//            rtnhtml += '<div class="div_fieldcontent" style="height:25px">';                                        
//            rtnhtml += '<div style="position: relative">';
//            rtnhtml += '<label style="position:absolute;top:5px">' + LanguageStr_Network.IP_Type + '</label>'; 
//            rtnhtml += '<div style="position: absolute;left:130px;top:0px">';    
//            rtnhtml += '<div style="position: relative">';
//            if(dhcp===1)
//                rtnhtml += '<input type="radio" id="' + id + 'iscsi-radio-manual" name="' + id + 'radio-iscsi-ip-type"  class="iSCSIiCheckRadioManualDHCP" value="0">';
//            else
//                rtnhtml += '<input type="radio" id="' + id + 'iscsi-radio-manual" name="' + id + 'radio-iscsi-ip-type"  class="iSCSIiCheckRadioManualDHCP" value="0" checked>';
//            rtnhtml += '<label style="position: absolute;top:3px;left:25px;cursor:pointer" for="' + id + 'iscsi-radio-manual">' + LanguageStr_Network.IP_Manual + '</label>';
//            rtnhtml += '</div>';
//            rtnhtml += '</div>';
//            rtnhtml += '<div style="position: absolute;left:220px;top:0px">';
//            rtnhtml += '<div style="position: relative">';
//            if(dhcp===1)
//                rtnhtml += '<input type="radio" id="' + id + 'iscsi-radio-dhcp" name="' + id + 'radio-iscsi-ip-type"  class="iSCSIiCheckRadioManualDHCP iSCSIiCheckRadioDHCP" value="1" checked>';
//            else
//                rtnhtml += '<input type="radio" id="' + id + 'iscsi-radio-dhcp" name="' + id + 'radio-iscsi-ip-type"  class="iSCSIiCheckRadioManualDHCP iSCSIiCheckRadioDHCP" value="1">';
//            rtnhtml += '<label style="position: absolute;top:3px;left:25px;cursor:pointer" for="' + id + 'iscsi-radio-dhcp">DHCP</label>';
//            rtnhtml += '</div>';
//            rtnhtml += '</div>';
//            rtnhtml += '</div>';
//            rtnhtml += '</div>';

            rtnhtml += '<div class="div_fieldcontent" style="margin-top:10px">';            
            rtnhtml += '<label class="LabelIPHead">IP : </label>';
            rtnhtml += '<input id="iSCSIIP' + id + '" type="text" class="iptext" value="' + ip + '" ';
            if(dhcp === 1){
                rtnhtml += 'disabled';
            }
            rtnhtml += '>';
            rtnhtml += '<br><br>';
            rtnhtml += '<label class="LabelIPHead">' + LanguageStr_Network.IP_Mask + '</label>';
            rtnhtml += '<input class="iptext" id="iSCSIIPMask' + id + '" type="text" value="' + mask + '" ';
            if(dhcp === 1){
                rtnhtml += 'disabled';
            }
            rtnhtml += '>';
            rtnhtml += '<br><br>';
            rtnhtml += '<label class="LabelIPHead">' + LanguageStr_Network.IP_Gateway + '</label>';
            rtnhtml += '<input class="iptext" id="iSCSIIPGateway' + id + '" type="text" value="' + gateway + '" ';
            if(dhcp === 1){
                rtnhtml += 'disabled';
            }
            rtnhtml += '>';
            rtnhtml += '<br></div>';
            rtnhtml += '</fieldset>';
            return rtnhtml;
        };
        // vSwitch UI介面功能實作 2017.01.23   
        createobj.processvSwitchData = function(ajaxData) {
            var rtnhtml = '';
            vSwitchList=[];
            var BrowserHeight = $(window).height();
            var div_height = BrowserHeight-545; // vSwitch william 2017.01.19 原值：350
            rtnhtml += '<div id="div_ip_content" style="width:685px;height:' + div_height + 'px;">'; // vSwitch william 2017.01.19 取消overflow:auto,原值：width:550px
            rtnhtml += '<div class="vSwitch_wrapper">';
            $.each(ajaxData['Vswitchs'], function(index, vswitch) {
                rtnhtml += createobj.CreatevSwitchHtml(index,vswitch['IP'],vswitch['Mask'],vswitch['Gateway'],vswitch['Devs'],ajaxData['FreeDevs'],index==0?true:false,vswitch['DHCP']);                
            });
            rtnhtml += '</div>';           
            if(ajaxData['DNS'].length > 0){
                $.each(ajaxData['DNS'], function(index, dns) {
                    rtnhtml += createobj.CreateDNSHtml(index,dns);
                });  
            }else{
                 rtnhtml += createobj.CreateDNSHtml(0,'');
            }        
            rtnhtml += '<br></div>';
            return rtnhtml;
        };
        // vSwitch UI介面功能實作 2017.01.23  
        createobj.FlashvSwitchFree = function() {
            var index = 0;
            var tmpDevIndex;
            $('.vSwitch_FreeContent').each(function() {
                var element = $(this);
                var rtnhtml = '';
                element.empty();
                for (var k in vSwitchList){
                    if(vSwitchList[k].indexSetVswitch == -1) {
                        tmpDevIndex = vSwitchList[k].name.substring(3);
                        tmpDevIndex++;
                        rtnhtml += '<div id="f'+index+vSwitchList[k].name+'" class="eth_elem free eth_free'+index+'">eth'+tmpDevIndex+'</div>';
                    }             
                }               
                element.html(rtnhtml);
                index++;
            });
        }     
        // vSwitch UI介面功能實作 2017.01.23  
        createobj.FlashvSwitchAssign = function(index) {            
            var rtnhtml = '';
            var tmpDevIndex;
            $('.vSwitch_AssignedCcontent:eq('+index+')').empty();
            for (var k in vSwitchList){
                if(vSwitchList[k].indexSetVswitch == index){
                    tmpDevIndex = vSwitchList[k].name.substring(3);
                    tmpDevIndex++;
                    rtnhtml += '<div id="a'+index+vSwitchList[k].name+'" class="eth_elem eth_assigned'+index+'">eth'+tmpDevIndex+'</div>';
                }             
            }               
            $('.vSwitch_AssignedCcontent:eq('+index+')').html(rtnhtml); 
        }   
        // vSwitch UI介面功能實作 2017.01.23  
        createobj.CreatevSwitchHtml = function(id, ip, mask,gateway,devs,freeDevs,addFreeeDevs,dhcp) {   
            var tmpDevIndex;
            var rtnhtml = '<fieldset class="vSwitch_fieldset_external">';
            rtnhtml += '<legend>' + LanguageStr_Network.IP_vSwitch + ' ' + id + '</legend>'; /* 2017.10.03 william 名詞修改 vSwitch -> 虛擬交換器*/
            rtnhtml += '<div class="Div_vSwitch_inner" style="position: relative;bottom: 13px;height:112px;">'; // bottom: 19px;
            rtnhtml += '<div class="div_fieldcontent vSwitch_TypeRow">';
            /* type Start */
            if (id ===0){
                rtnhtml += '<div style="position: relative">';
                rtnhtml += '<label class="vSwitch_TypeLabel">' + LanguageStr_Network.IP_Type + '</label>';
                rtnhtml += '<div class="vSwitch_ManualContent">';
                rtnhtml += '<div style="position: relative;bottom: 3px;">';
                if(dhcp)
                    rtnhtml += '<input type="radio" id="' + id + 'radio-manual" name="' + id + 'radio-ip-type" class="iCheckRadioManualDHCP" value="0">';
                else
                    rtnhtml += '<input type="radio" id="' + id + 'radio-manual" name="' + id + 'radio-ip-type" class="iCheckRadioManualDHCP" value="0" checked>';
                rtnhtml += '<label class="vSwitch_ManualLabel" for="' + id + 'radio-manual">' + LanguageStr_Network.IP_Manual + '</label>';
                rtnhtml += '</div>';
                rtnhtml += '</div>';
                rtnhtml += '<div class="vSwitch_DHCPContent">';
                rtnhtml += '<div style="position: relative;bottom: 3px;">';
                if(dhcp)   
                    rtnhtml += '<input type="radio" id="' + id + 'radio-dhcp" name="' + id + 'radio-ip-type" class="iCheckRadioManualDHCP iCheckRadioDHCP" value="1" checked>';
                else
                    rtnhtml += '<input type="radio" id="' + id + 'radio-dhcp" name="' + id + 'radio-ip-type" class="iCheckRadioManualDHCP iCheckRadioDHCP" value="1">';                
                rtnhtml += '<label class="vSwitch_DHCPLabel" for="' + id + 'radio-dhcp">DHCP</label>';
                rtnhtml += '</div>';
                rtnhtml += '</div>';
                rtnhtml += '</div>';
            }
            /* type End */
            rtnhtml += '</div>';
            rtnhtml += '<div class="vSwitch_div_IP">';
            rtnhtml += '<label class="vSwitch_LabelHead" style="top:6px">IP :</label>';
            rtnhtml += '<input type="text" class="vSwitch_text1 inputv'+id+' vswitch_ip_'+id+'" style="top:5px;" value="'+ip+'"';
            if(devs.length == 0 )
                rtnhtml += ' disabled';
            rtnhtml += '><br>';
            rtnhtml += '<label class="vSwitch_LabelHead" style="top:34px">Mask :</label>';
            rtnhtml += '<input type="text" class="vSwitch_text1 inputv'+id+' vswitch_mask_'+id+'" style="top:33px;" value="'+mask+'"';
            if(devs.length == 0 )
                rtnhtml += ' disabled';
            rtnhtml += '><br>';
            rtnhtml += '<label class="vSwitch_LabelHead" style="top:62px">Gateway :</label>';
            rtnhtml += '<input type="text" class="vSwitch_text1 inputv'+id+' vswitch_gateway_'+id+'" style="top:60px;" value="'+gateway+'"';
            if(devs.length == 0 )
                rtnhtml += ' disabled';
            rtnhtml += '>';
            rtnhtml += '</div>';
           
            rtnhtml += '<div class="vSwitch_div_assigned">';
            rtnhtml += '<fieldset class="vSwitch_fieldset_internal">';
            rtnhtml += '<legend>Assigned</legend>';
            rtnhtml += '<div class="vSwitch_AssignedCcontent">';
            $.each(devs, function(index, dev) {    
                tmpDevIndex = dev.substring(3);
                tmpDevIndex++;            
                rtnhtml += '<div id="a'+id+dev+'" class="eth_elem eth_assigned'+id+'">eth'+tmpDevIndex+'</div>';
                vSwitchList[dev] = new NICClass(dev,id);
            });
            rtnhtml += '</div>';
            rtnhtml += '</fieldset>';
            rtnhtml += '</div>';
            rtnhtml += '<div class="vSwitch_div_Arrow">';
            if(id == 0 && devs.length <= 1)
                rtnhtml += '<div class="vSwitch_arrow to_free arrow_disable" style="top: 15px;">⇨</div>';
            else
                rtnhtml += '<div class="vSwitch_arrow to_free" style="top: 15px;">⇨</div>';
            rtnhtml += '<div class="vSwitch_arrow to_assign" style="top: 55px;">⇦</div>';
            rtnhtml += '</div>';
            rtnhtml += '<div class="vSwitch_div_Free">';
            rtnhtml += '<fieldset class="vSwitch_fieldset_internal">';
            rtnhtml += '<legend>Free</legend>';
            rtnhtml += '<div class="vSwitch_FreeContent">';            
            $.each(freeDevs, function(index, dev) {        
                tmpDevIndex = dev.substring(3);
                tmpDevIndex++;        
                rtnhtml += '<div id="f'+id+dev+'" class="eth_elem eth_free'+id+'">eth'+tmpDevIndex+'</div>';  
                if(addFreeeDevs)
                    vSwitchList[dev] = new NICClass(dev,-1);              
            });
            rtnhtml += '</div>';
            rtnhtml += '</fieldset>';
            rtnhtml += '</div>';
            rtnhtml += '</div>';
            rtnhtml += '</fieldset>';                 
            return rtnhtml;
        };     

        createobj.processDNSData = function(ajaxData) {
            var rtnhtml = '';
            vSwitchList=[];
            var BrowserHeight = $(window).height();
            var div_height = BrowserHeight-545; 
            if(ajaxData.length > 0){
                $.each(ajaxData, function(index, dns) {
                    rtnhtml += createobj.CreateDNSHtml(index,dns);
                });  
            }else{
                 rtnhtml += createobj.CreateDNSHtml(0,'');
            }        
            rtnhtml += '<br></div>';
            return rtnhtml;
        };


        createobj.CreateDNSHtml = function(id, ip) { 
            var rtnhtml = '<fieldset ';
            rtnhtml += 'class="vSwitch_fieldset_DNS"'; // vSwitch william 2017.01.19 修改           
            rtnhtml += '><legend>DNS</legend><div class="div_fieldcontent" style="position: relative"><label class="LabelIPHead vSwitch_LabelHead" style="left: -43px;top: -1px;">IP : </label><input id="DNS' + id + '" type="text" class="iptext vSwitch_text1" style="left: 42px;top: -3px;height: 15px;" value="' + ip + '"><br></div></fieldset>'; // vSwitch william 2017.01.19 修改           
            return rtnhtml;
        };         
        createobj.CreateRAMInfo = function(JsonData){
            var reg = /^\d+$/;
            var rtnhtml = '';
            var mem_index;
            rtnhtml += '<div style="position: relative;left: 30px">';
            rtnhtml += '<div>';
            rtnhtml += '<div><label class="LabelCPURAMHead2" style="font-size: 14px;color:#044de1;width:110px;">' + LanguageStr_SystemInfo.SystemInfo_Total_RAM + ' :</label>'; /* SnapShot&Schedule&Backup  2017.01.11 william */                                                                       
            rtnhtml += '<label id="label_ram" style="font-size: 14px;">' + add_comma_of_number(JsonData['Mem']['Total']) + ' KB</label>';
            rtnhtml += '</div>';
            rtnhtml += '</div>';
            rtnhtml += '</div>';
            rtnhtml += '<div style="height:10px"></div>';
            rtnhtml += '<div style="position: relative;left: 30px">';
            rtnhtml += '<div>';
            rtnhtml += '<label class="LabelCPURAMHead" style="font-size: 14px;color:#044de1;width:135px;">' + LanguageStr_SystemInfo.SystemInfo_Memory_Clock_Rate + ' :</label>'; /* SnapShot&Schedule&Backup  2017.01.11 william */                                           
            rtnhtml += '</div>';
            rtnhtml += '</div>';
            $.each(JsonData['Mem']['Clocks'],function(index,element){
                mem_index = index + 1;                
                rtnhtml += '<div style="height:10px"></div>';
                rtnhtml += '<div style="position: relative;left: 45px">';
                rtnhtml += '<div>';
                rtnhtml += '<label class="LabelCPURAMHead2" style="color:#044de1;">' + LanguageStr_SystemInfo.SystemInfo_Memory + ' ' + mem_index + ' :</label>';                
                if(reg.test( element['Clock']))
                    rtnhtml += '<label>' + add_comma_of_number(element['Clock']) + ' MHZ</label>';
                else
                    rtnhtml += '<label>None</label>';
                rtnhtml += '</div>';
                rtnhtml += '</div>';
            });
            return rtnhtml;
        };
        createobj.CreateCPURAMPerformanceHtml = function(JsonData){
            var rtnhtml = '';
            var cpuhtml = '';
            var cputotalhtml = '';
            var nichtml = '';
            var nictotalhtml = '';
            var index = 0;
            $.each(JsonData['CPU'],function(index,element){                
                if(index === 0){
                    cputotalhtml += '<div style="position: relative">';
                    cputotalhtml += '<div style="position: absolute;left: 30px">';
                    cputotalhtml += '<label class="LabelCPURAMHead2" style="font-size: 14px;color:#044de1;width: 100px;">Total :</label>';
                    var cpu_util = new Number(element);
                    cputotalhtml += '<label style="font-size: 14px;width:65px;text-align: right;display: inline-block;">' + cpu_util.toFixed(2) + ' %</label>';
                    cputotalhtml += '</div>';
                    cputotalhtml += '</div>';                    
                }
                else{
                    if(index !== (JsonData['CPU'].length - 1))
                        cpuhtml += '<div style="height: 20px">';
                    cpuhtml += '<div style="position: relative">';
                    cpuhtml += '<div style="position: absolute;left: 30px">';
                    if(index < 10)
                        cpuhtml += '<label class="LabelCPURAMHead2" style="font-size: 14px;color:#044de1;text-align: right;position: relative; right: 24px;width:100px;">' + index + '. CPU 0' + index + ' : </label>';
                    else
                        cpuhtml += '<label class="LabelCPURAMHead2" style="font-size: 14px;color:#044de1;text-align: right;position: relative; right: 24px;width:100px;">' + index + '. CPU ' + index + ' : </label>';                        
                    var cpu_util = new Number(element);
                    cpuhtml += '<label style="font-size: 14px;width:65px;text-align: right;display: inline-block;">' + cpu_util.toFixed(2) + ' %</label>';
                    cpuhtml += '</div>';
                    cpuhtml += '</div>';
                    if(index !== (JsonData['CPU'].length - 1))
                        cpuhtml += '</div>';
                }                    
                index++;       
            });
            cpuhtml += '<div style="height:15px"></div>';
            cpuhtml += '<div>';
            cpuhtml += '<div style="position:relative">';
            cpuhtml += '<div style="height:5px;position:absolute;left:30px;width:165px;border-bottom: dashed 1px rgba(0, 0, 0, .5)"></div>';
            cpuhtml += '</div>';
            cpuhtml += '</div>';
            cpuhtml += '<div style="height:15px"></div>';            
            cpuhtml += cputotalhtml;
            var nicindex = 0;
            $.each(JsonData['NIC'],function(index,element){                 
                if(nicindex === 0){
                    nictotalhtml += '<label class="LabelCPURAMHead" style="font-size: 14px;color:#044de1;">Total : </label>';
                    nictotalhtml += '<label style="position:absolute;;width: 120px;font-size: 14px;">' + nic_throughput_change_format(element['Rx']) + '</label>';
                    nictotalhtml += '<label style="position:absolute;;width: 120px;font-size: 14px;left:265px;">' + nic_throughput_change_format(element['Tx']) + '</label>';  // 修改 width: 100px 2017.02.03 william
                    nictotalhtml += '<label style="position:absolute;;width: 120px;font-size: 14px;left:400px;">' + nic_throughput_change_format(element['Total']) + '</label>';                    
                }
                else{
                    //nichtml += '<label class="LabelCPURAMHead">NIC ' + nicindex + ': </label>';                
                    nichtml += '<div>';
                    nichtml += '<div style="position:relative">';
                    nichtml += '<label class="LabelCPURAMHead" style="font-size: 14px;color:#044de1;">vSwitch ' + (index) + ' : </label>';
                    nichtml += '<label style="position:absolute;font-size: 14px;width: 120px;">' + nic_throughput_change_format(element['Rx']) + '</label>';
                    nichtml += '<label style="position:absolute;left:265px;font-size: 14px;width: 120px;">' + nic_throughput_change_format(element['Tx']) + '</label>';  // 修改 left:220px 2017.02.03 william
                    nichtml += '<label style="position:absolute;left:400px;font-size: 14px;width: 120px;">' + nic_throughput_change_format(element['Total']) + '</label>';
                    nichtml += '</div>';
                    nichtml += '</div>';
                    nichtml += '<br>';
                }
                
                nicindex++;       
            });
            if(JsonData['NIC'].length > 2){
                nichtml += '<div style="width:470px;height:5px;border-bottom: dashed 1px rgba(0, 0, 0, .5)"></div>';
                nichtml += '<div style="height:15px"></div>';            
                nichtml += nictotalhtml;
            }
            var mem_allocated_util = ((JsonData['Mem']['Allocated']/JsonData['Mem']['Total'])*100).toFixed(2);
            var mem_used_util = ((JsonData['Mem']['Used']/JsonData['Mem']['Total'])*100).toFixed(2);
            rtnhtml += '<div class="div_cpu_ram_title">';
            rtnhtml += '<label class="label_cpu_ram_title">' + LanguageStr_SystemInfo.SystemInfo_CPU_Util + '</label>';
            rtnhtml += '</div>';            
            rtnhtml += '<div style="width:230px;height:685px;overflow:auto">';
            rtnhtml += '<div style="height: 10px"></div>';
            rtnhtml += cpuhtml;                                              
            rtnhtml += '<div class="div_cpu_ram_performance_title">';            
            rtnhtml += '<label class="label_cpu_ram_title">' + LanguageStr_SystemInfo.SystemInfo_RAM_Util + '</label>';            
            rtnhtml += '</div>';           
            rtnhtml += '<div style="position: absolute;left: 230px;top: 25px;">';
            rtnhtml += '<div style="position: relative;left: 30px;width:450px">';
            rtnhtml += '<div style="height:30px">';
            rtnhtml += '<label class="LabelRAMHead1" style="font-size: 14px;color:#044de1;">' + LanguageStr_SystemInfo.SystemInfo_Total + '</label>';
            rtnhtml += '<label style="display:inline-block;width:140px;text-align:right;margin-left: -35px;font-size: 14px;">'+ add_comma_of_number(JsonData['Mem']['Total']) + ' KB</label>';            
            rtnhtml += '</div>';
            rtnhtml += '<div style="height:25px">';
            rtnhtml += '<label class="LabelRAMHead1" style="font-size: 14px;color:#044de1;">' + LanguageStr_SystemInfo.SystemInfo_Allocated + '</label>';
            rtnhtml += '<label style="display:inline-block;width:140px;text-align:right;margin-left: -35px;font-size: 14px;">'+ add_comma_of_number(JsonData['Mem']['Allocated']) + ' KB</label>'; 
            rtnhtml += '<label style="margin-left:45px;font-size: 14px;">'+ mem_allocated_util + ' %</label>'; // 按鈕樣式修改 原值：margin-left:50px 2017.02.03 william 
            rtnhtml += '</div>';
            rtnhtml += '<div>';
            rtnhtml += '<label class="LabelRAMHead1" style="font-size: 14px;color:#044de1;">' + LanguageStr_SystemInfo.SystemInfo_Used + '</label>';
            rtnhtml += '<label style="display:inline-block;width:140px;text-align:right;margin-left: -35px;font-size: 14px;">'+ add_comma_of_number(JsonData['Mem']['Used']) + ' KB</label>';
            rtnhtml += '<label style="margin-left:45px;font-size: 14px;">'+ mem_used_util + ' %</label>';  // 按鈕樣式修改 原值：margin-left:50px 2017.02.03 william 
            rtnhtml += '<a id="btn_clean_ram" href="#" class="log_btn" style="width:70px;margin-left:51px">' + LanguageStr_SystemInfo.SystemInfo_Clean + '</a>'; // 按鈕樣式修改 原值：class="log_btn" ,margin-left:45px 2017.02.03 william 
            rtnhtml += '</div>';
            rtnhtml += '</div>';
            rtnhtml += '</div>';

            // 新增RAID容量使用率 2016.12.08 william  
            /*
            var UsedCapacity = JsonData['Ceph']['Used'];
            var TotalCapacity = JsonData['Ceph']['Total'];
            if(typeof(raid_notify_used) === 'undefined'){
                UsedCapacity = new Number(UsedCapacity);
                UsedCapacity = UsedCapacity.toFixed(2);
            }
            else
                UsedCapacity = raid_notify_used;
            if(typeof(raid_notify_toal) === 'undefined'){
                TotalCapacity = new Number(TotalCapacity);
                TotalCapacity = TotalCapacity.toFixed(2);
            }   
            else
                TotalCapacity = raid_notify_toal;
            if(typeof(raid_notify_usage) === 'undefined'){
                var raid_usage = JsonData['Ceph']['Usage'];   
                raid_usage = new Number(raid_usage);
                raid_usage = raid_usage.toFixed(2);
            }   
            else{
                var raid_usage = raid_notify_usage;   
            }     
            */    
            var UsedCapacity;
            var TotalCapacity;
            var raid_usage;
            rtnhtml += '<div class="div_raid_capacity_usage_title">';            
            rtnhtml += '<label class="label_cpu_ram_title">' + LanguageStr_SystemInfo.SystemInfo_Raid_Util + '</label>';            
            rtnhtml += '</div>';           
            rtnhtml += '<div style="position: absolute;left: 760px;top: 25px;">';
            rtnhtml += '<div style="position: relative;left: 30px;width: 275px;">';
            rtnhtml += '<div style="width: 290px;height:685px;overflow-y:auto;overflow-x: hidden;">';            
            for(var i = 0; i < 7; i++) {
                if(CephRaidListForPerformance && CephRaidListForPerformance.length>0 && CephRaidListForPerformance[i+1] && CephRaidListForPerformance[i+1].raidid.length>0) {
                    TotalCapacity = CephRaidListForPerformance[i+1].total;
                    TotalCapacity = new Number(TotalCapacity);
                    TotalCapacity = TotalCapacity.toFixed(2);                
                    UsedCapacity = CephRaidListForPerformance[i+1].used;
                    UsedCapacity = new Number(UsedCapacity);
                    UsedCapacity = UsedCapacity.toFixed(2);                
                    raid_usage = CephRaidListForPerformance[i+1].usage;
                    raid_usage = new Number(raid_usage);
                    raid_usage = raid_usage.toFixed(2);
                    rtnhtml += '<label class="LabelCPURAMHead" style="color:#044de1;font-size: 14px;">RAID ' + (i+1) + ' :</label><label style="position: absolute; left: 60px;font-size: 14px;">' + LanguageStr_SystemInfo.Raid_Enable + '</label><br><br>';   
                    rtnhtml += '<div style="position: relative;left: 76px;width:350px">';
                    rtnhtml += '<div style="height:30px">';
                    rtnhtml += '<label class="LabelRAMHead1" style="width:105px;">' + LanguageStr_SystemInfo.SystemInfo_Total_Capacity + '</label>';
                    rtnhtml += '<label class="RAIDList_Capacity_info" style="display:inline-block;width:108px;text-align:right;margin-left: -15px;">'+add_comma_of_number(TotalCapacity)+' GB</label>';            
                    rtnhtml += '</div>';
                    rtnhtml += '<div style="height:30px">';
                    rtnhtml += '<label class="LabelRAMHead1" style="width:105px;">' + LanguageStr_SystemInfo.SystemInfo_Used_Capacity + '</label>';
                    rtnhtml += '<label class="RAIDList_FreeCapacity_info" style="display:inline-block;width:108px;text-align:right;margin-left: -15px;">'+add_comma_of_number(UsedCapacity)+' GB</label>'; 
                    rtnhtml += '</div>';
                    rtnhtml += '<div>';
                    rtnhtml += '<label class="LabelRAMHead1" style="width:105px;">' + LanguageStr_SystemInfo.SystemInfo_Raid_Capacity_Usage + '</label>';
                    rtnhtml += '<label class="Raid_Capacity_Usage_info" style="display:inline-block;width:108px;text-align:right;margin-left: -15px;">'+raid_usage+' %</label>';
                    rtnhtml += '</div>';
                    rtnhtml += '</div>';     
                } else {
                    rtnhtml += '<label class="LabelCPURAMHead" style="color:#044de1;font-size: 14px;">RAID ' + (i+1) + ' : </label><label style="position: absolute; left: 60px;font-size: 14px;">' + LanguageStr_SystemInfo.Raid_NA + '</label><br><br>';    
                }
                if(6 != i)
                    rtnhtml += '<div style="width: 275px;height:5px;border-bottom: dashed 1px rgba(0, 0, 0, .5);"></div><div style="height:10px"></div>';                                    

            }
            rtnhtml += '</div>';
            rtnhtml += '</div>';
            rtnhtml += '</div>';            

            //---------------------------------------------------------------------------------------------------------------------------
            rtnhtml += '<div class="div_cpu_ram_performance_title" style="top:150px;left: 230px;">';            
            rtnhtml += '<label class="label_cpu_ram_title">' + LanguageStr_SystemInfo.SystemInfo_IO_Wait + '</label>';
            rtnhtml += '</div>';           
            rtnhtml += '<div style="position: absolute;left: 230px;top: 180px;">';
            rtnhtml += '<div style="position: relative;left: 30px;width:290px">';
            rtnhtml += '<div>';
            rtnhtml += '<label class="LabelCPURAMHead1" style="width:190px;font-size: 14px;color:#044de1;">' + LanguageStr_SystemInfo.SystemInfo_IO_Wait + '</label>';
            var iowait =  new Number(JsonData['IOWait']); 
           rtnhtml += '<label style="margin-left:34px;position: relative;left: -36px;font-size: 14px;">'+ iowait.toFixed(2) + ' %</label>';
            rtnhtml += '</div>';
            rtnhtml += '</div>';
            rtnhtml += '</div>';
            
            rtnhtml += '<div style="height: 10px"></div>';
            rtnhtml += '<div class="div_nic_performance_title">';
            rtnhtml += '<label class="label_cpu_ram_title">' + LanguageStr_SystemInfo.SystemInfo_Network_Throughput + '</label>';
            rtnhtml += '</div>';                
            rtnhtml += '<div style="position: absolute;left: 230px;top: 280px;width:550px">';
            rtnhtml += '<div style="position: relative;left: 30px">';     
            rtnhtml += '<label class="LabelCPURAMHead"></label>';
            rtnhtml += '<label style="display: inline-block;width: 120px;position:absolute;left:130px;font-size: 14px;color:#044de1;">' + LanguageStr_SystemInfo.SystemInfo_Receive + '</label>'; // 修改 left:119px 2017.02.03 william
            rtnhtml += '<label style="display: inline-block;width: 120px;position:absolute;left:265px;font-size: 14px;color:#044de1;">' + LanguageStr_SystemInfo.SystemInfo_Send + '</label>'; // 修改 left:219px 2017.02.03 william
            rtnhtml += '<label style="display: inline-block;width: 120px;position:absolute;left:400px;font-size: 14px;color:#044de1;">' + LanguageStr_SystemInfo.SystemInfo_RaddS + '</label>'; // 修改 left:319px 2017.02.03 william
            rtnhtml += '<br>';
            rtnhtml += '<br>';
            rtnhtml += nichtml;
            rtnhtml += '</div>';
            rtnhtml += '</div>';
            rtnhtml += '</div>';
            return rtnhtml;
        };

        // GB&TB string 判斷 2016.12.12 william
        function stringdetect(string_Data)
        {
            var string_Data_1 = 0;
            if(string_Data.indexOf("TB")==-1){ //就代表string_Data中不含有TB這個substring.
                string_Data_1 = parseFloat(string_Data);
                return string_Data_1;
            }  
            else{
                string_Data_1 = parseFloat(string_Data)*1024;
                return string_Data_1;
            }
        }

        createobj.CreateVMListHtmlByTable = function(){            
            var HtmlStr = "";                        
            $.each( VMListTable, function(index, element){    
                var no = index + 1;
                var type = element["type"] === 0 ? "Original" : "Borned";
                if(index === VMListTable.length - 1)
                    HtmlStr += '<div id="VMRow' + element['name'] + '" class = "Div_VMLognRow LastVMRow" data-status="' + element['state'] + '">';
                else
                    HtmlStr += '<div id="VMRow' + element['name'] + '" class = "Div_VMLognRow" data-status="' + element['state'] + '">';
                HtmlStr += '<H4 class = "VMList_ItemNo_Row AbsoluteLayout RowField" >' + no  + '</H4>';                           
                HtmlStr += '<H4 class = "VMList_Username_Row AbsoluteLayout RowField" title="' + element['name'] + '">' + element['name'] +  '</H4>';   
                HtmlStr += '<H4 class = "VMList_Name_Row AbsoluteLayout RowField" title="' + element['name'] + '">' +  element['name'];                
//                HtmlStr += '<img src="/img/Info.png" style="margin-left:5px;cursor:pointer" height="20" width="20" align="center" class="DetailVM" id="' + element['name'] + '">';
                HtmlStr += '</H4>';
//                HtmlStr += '<H4 class = "VMListType_Row AbsoluteLayout RowField" >' +  type + '</H4>';
//                HtmlStr += '<a class = "VMListSeedSrc_Row AbsoluteLayout RowField" title="' + element['seed_src'] + '">' +  element['seed_src'] + '</a>';                           
//                HtmlStr += '<H4 class = "VMList_CPUUtil_Row AbsoluteLayout RowField" >' + element['cpu_util'] +  '</H4>';
//                HtmlStr += '<H4 class = "VMList_MemUtil_Row AbsoluteLayout RowField" >' + element['mem_util'] + '</H4>';        
                HtmlStr += '<H4 class = "VMList_Status_Row AbsoluteLayout RowField" >' +  createobj.GetVMStatus(element['state'],element['online']) + '</H4>';                           
                HtmlStr += '<H4 class = "VMList_Online_Row AbsoluteLayout RowField" >' +  createobj.GetVMOnlineStatus(element['online']) + '</H4>';                           
                HtmlStr += '<H4 class = "VMList_RAM_Row AbsoluteLayout RowField" >' +  element['ram'] + '</H4>';                           
                HtmlStr += '<H4 class = "VMList_CreateTime_Row AbsoluteLayout RowField" >' + element['create_time'] +  '</H4>'; 
                HtmlStr += '<H4 class = "VMList_AllSize_Row AbsoluteLayout RowField" >' + formatBytes(element['allsize'], 1) +  '</H4>'; // 桌面設定-新增VD檔案大小實作 2017.05.03 william                     
                HtmlStr += '<a class = "VMList_Desc_Row AbsoluteLayout RowField" title="' + element['desc'] + '">' +  element['desc'] + '</a>';                           
                HtmlStr += '<div id="VMActionRow' + element['name'] + '" class = "VMList_Action_Row AbsoluteLayout RowField" >'; 
                HtmlStr += createobj.CreateVMDropDownHtml(element['name'],element['state'],element['seed'],element['seed_src']);      
                HtmlStr += '</div>';   
                HtmlStr += '<div class = "VMList_Select_Row AbsoluteLayout RowField" >'; 
                HtmlStr += '<div style="position:relative">';
                if(element['state'] === -2)
                    HtmlStr += '<input type="checkbox" id="' + element['name'] + '" class="iCheckBoxVMSelect iCheckBoxDisable">';
                else
                    HtmlStr += '<input type="checkbox" id="' + element['name'] + '" class="iCheckBoxVMSelect">';
                HtmlStr += '</div>';
                HtmlStr += '</div>';
                HtmlStr += '<div class = "VMList_Auto_Suspend_Row AbsoluteLayout RowField" >'; 
                HtmlStr += '<div style="position:relative">';
                if(element['suspend'] === true){
                    HtmlStr += '<input type="checkbox" id="VMSuspend' + element['name'] + '" class="iCheckBoxVMSuspend iCheckBoxSuspend" checked>';                                    
                }
                else{
                    HtmlStr += '<input type="checkbox" id="VMSuspend' + element['name'] + '" class="iCheckBoxVMSuspend iCheckBoxSuspend">';                                      
                }
                HtmlStr += '<label style="margin-left:7px;position: absolute;top: 3px;">' + LanguageStr_VM.VM_Enable + '</label>';                
                HtmlStr += '</div>';
                HtmlStr += '</div>'; 
                HtmlStr += '</div>';       
            });                            
            return HtmlStr;
        };    
        createobj.CreateSeedListHtmlByTable = function(){            
            var HtmlStr = "";                        
            $.each( SeedListTable, function(index, element){   
                var no = index + 1;   
                if(index === SeedListTable.length - 1)
                    HtmlStr += '<div id="VMRow' + element['name'] + '" class = "Div_VMRow LastVMRow">';
                else
                    HtmlStr += '<div id="VMRow' + element['name'] + '" class = "Div_VMRow">';
                HtmlStr += '<H4 class = "VMList_ItemNo_Row AbsoluteLayout RowField" >' + no  + '</H4>';
                HtmlStr += '<div class = "SeedList_Select_Row AbsoluteLayout RowField" >';
                HtmlStr += '<div style="position:relative">'; 
               // HtmlStr += '<input type="checkbox" id="' + element['name'] + '" class="iCheckBoxSeedSelect">';
               // 2016.12.19 william新增
                if(element['state'] === -2)
                    HtmlStr += '<input type="checkbox" id="' + element['name'] + '" class="iCheckBoxSeedSelect iCheckBoxDisable">';
                else
                    HtmlStr += '<input type="checkbox" id="' + element['name'] + '" class="iCheckBoxSeedSelect">';
                HtmlStr += '</div>';
                HtmlStr += '</div>';
                HtmlStr += '<H4 class = "SeedList_Name_Row AbsoluteLayout RowField" title="' + element['name'] + '">' +  element['name'] + '</H4>';
                HtmlStr += '<a class = "SeedList_Description_Row AbsoluteLayout RowField" title="' + element['desc'] + '">' + element['desc'] +  '</a>';
                HtmlStr += '<H4 class = "SeedList_VDICount_Row AbsoluteLayout RowField" >' + element['vdi_count'] + '</H4>';                        
                HtmlStr += '<H4 class = "SeedList_RAM_Row AbsoluteLayout RowField" >' + element['ram'] +  '</H4>';                
                HtmlStr += '<H4 class = "SeedList_CreateTime_Row AbsoluteLayout RowField" >' + element['create_time'] +  '</H4>';   
                HtmlStr += '<H4 class = "SeedList_AllSize_Row AbsoluteLayout RowField" >' + formatBytes(element['allsize'], 1) +  '</H4>'; // 桌面設定-新增VD檔案大小實作 2017.05.03 william                             
                HtmlStr += '<div class = "SeedList_Action_Row AbsoluteLayout RowField" >'; 
                HtmlStr += createobj.CreateSeedDropDownHtml(element['name']);      
                HtmlStr += '</div>';                        
                HtmlStr += '</div> ';       
            });                            
            return HtmlStr;
        };
        createobj.CreateBornedListHtmlByTable = function(){            
            var HtmlStr = "";                        
            $.each( BornedListTable, function(index, element){  
                var no = index + 1;
//                HtmlStr += '<div style="height:3px"></div>';
//                var type = element["type"] === 0 ? "Original" : "Borned";
                if(index === BornedListTable.length - 1)
                    HtmlStr += '<div id="VMRow' + element['name'] + '" class = "Div_VMLognRow LastVMRow" data-status="' + element['state'] + '">';
                else
                    HtmlStr += '<div id="VMRow' + element['name'] + '" class = "Div_VMLognRow" data-status="' + element['state'] + '">';
                HtmlStr += '<H4 class = "VMList_ItemNo_Row AbsoluteLayout RowField" >' + no  + '</H4>';                           
                HtmlStr += '<H4 class = "BornedList_Username_Row AbsoluteLayout RowField" title="' + element['name'] + '">' +  element['name'] + '</H4>';                           
                HtmlStr += '<H4 class = "BornedList_Name_Row AbsoluteLayout RowField" title="' + element['name'] + '"><div style="white-space: nowrap;overflow: hidden;text-overflow: ellipsis;">' +  element['name'];                
//                HtmlStr += '<img src="/img/Info.png" style="margin-left:5px;cursor:pointer" height="20" width="20" align="center" class="DetailVM" id="' + element['name'] + '">';
                HtmlStr += '</div></H4>';
//                HtmlStr += '<H4 class = "VMListType_Row AbsoluteLayout RowField" >' +  type + '</H4>';
                HtmlStr += '<a class = "BornedList_SeedSrc_Row AbsoluteLayout RowField" title="' + element['seed_src'] + '">' +  element['seed_src'] + '</a>';                           
//                HtmlStr += '<H4 class = "VMList_CPUUtil_Row AbsoluteLayout RowField" >' + element['cpu_util'] +  '</H4>';
//                HtmlStr += '<H4 class = "VMList_MemUtil_Row AbsoluteLayout RowField" >' + element['mem_util'] + '</H4>';        
                HtmlStr += '<H4 class = "BornedList_Status_Row AbsoluteLayout RowField" >' +  createobj.GetVMStatus(element['state'],element['online']) + '</H4>';                           
                HtmlStr += '<H4 class = "BornedList_Online_Row AbsoluteLayout RowField" >' +  createobj.GetVMOnlineStatus(element['online']) + '</H4>';                           
                HtmlStr += '<H4 class = "BornedList_RAM_Row AbsoluteLayout RowField" >' + element['ram'] +  '</H4>';                
                HtmlStr += '<H4 class = "BornedList_CreateTime_Row AbsoluteLayout RowField" >' + element['create_time'] +  '</H4>';                
                HtmlStr += '<H4 class = "BornedList_AllSize_Row AbsoluteLayout RowField" >' + formatBytes(element['allsize'], 1) +  '</H4>'; // 桌面設定-新增VD檔案大小實作 2017.05.03 william
                HtmlStr += '<a class = "BornedList_Desc_Row AbsoluteLayout RowField" title="' + element['desc'] + '">' +  element['desc'] + '</a>';                           
                HtmlStr += '<div id="BornedActionRow' + element['name'] + '" class = "BornedList_Action_Row AbsoluteLayout RowField" >'; 
                HtmlStr += createobj.CreateVMDropDownHtml(element['name'],element['state'],element['seed'],element['seed_src']);      
                HtmlStr += '</div>';   
                HtmlStr += '<div class = "BornedList_Select_Row AbsoluteLayout RowField" >'; 
                HtmlStr += '<div style="position:relative">';
                if(element['state'] === -2)
                    HtmlStr += '<input type="checkbox" id="' + element['name'] + '" class="iCheckBoxBornedSelect iCheckBoxDisable">';
                else
                    HtmlStr += '<input type="checkbox" id="' + element['name'] + '" class="iCheckBoxBornedSelect">';
                HtmlStr += '</div>';
                HtmlStr += '</div>'; 
                HtmlStr += '<div class = "BornedList_Auto_Suspend_Row AbsoluteLayout RowField" >'; 
                HtmlStr += '<div style="position:relative">';
                if(element['suspend'] === true)
                    HtmlStr += '<input type="checkbox" id="VMSuspend' + element['name'] + '" class="iCheckBoxBornedSuspend iCheckBoxSuspend" checked>';                
                else
                    HtmlStr += '<input type="checkbox" id="VMSuspend' + element['name'] + '" class="iCheckBoxBornedSuspend iCheckBoxSuspend">';                
                HtmlStr += '<label style="margin-left:7px;position: absolute;top: 3px;">' + LanguageStr_VM.VM_Enable + '</label>';                
                HtmlStr += '</div>';
                HtmlStr += '</div>'; 
                HtmlStr += '</div> ';       
            });                            
            return HtmlStr;
        };
        createobj.CreateSeedDropDownHtml = function(name){
            var drop_down_content = '';           
            drop_down_content = '#dropdown-VM-Operation5';           
            return '<a href="#" class="btn-light btn-operation-vm" data-dropdown="' + drop_down_content + '" data-operation="' + name + '">' + LanguageStr_VM.VM_Setting + '&nbsp;▼</a>';
        };
        createobj.CreateVMListTable = function(JsonData){
            VMListTable = [];
            VMListName = [];
            VMCheckName = [];
            SeedListTable = [];
            SeedListName = [];
            BornedListTable = [];
            BornedListName = [];
            var CalcuVDICount = [];
            var memory;
            var tmp_vm_seed_class;
            var tmp_vm_class;
            var seedname = '';
            var vd_usage_size; // 桌面設定-新增VD檔案大小實作 2017.05.04 william
            CountTotalVM=0;
            CountPoweronVM=0;
            CountOnlineVM=0;
            CountSuspendVM=0;
            function calc_count(element){
                CountTotalVM++;
                if(element['Online'] === true)
                    CountOnlineVM++;
                if(element['State'] === 1)
                    CountPoweronVM++;
                if(element['State'] === 3)
                    CountSuspendVM++;
            }
            $.each( JsonData['Org'], function(index, element){                 
                calc_count(element);
                memory = ClacMemory(element['RAM']);   
                VMCheckName[index] = element['VDName'];                    
                vd_usage_size = element['AllSize']; // 桌面設定-新增VD檔案大小實作 2017.05.04 william                    
                tmp_vm_class = new VMClass(element['VDName'],0,0,element['State'],0,'',element['Desc'],0,element['CreateTime'],element["Online"],createobj.GetVMStatus(element['State'],element['Online']),element['Suspend'],memory.toFixed(2),element['VDID'],vd_usage_size);                   
                VMListTable.push(tmp_vm_class);
                VMListName[element['VDName']] = tmp_vm_class;                  
            });       
            $.each( JsonData['Seed'], function(index, element){  
                calc_count(element);
                memory = ClacMemory(element['RAM']);               
                VMCheckName[index] = element['VDName'];                     
                vd_usage_size = element['AllSize']; // 桌面設定-新增VD檔案大小實作 2017.05.04 william                 
                tmp_vm_seed_class = new VMSeedClass(element['VDName'],0,element['Desc'],element['State'],element['CreateTime'],memory.toFixed(2),element['VDID'],vd_usage_size);                  
                SeedListTable.push(tmp_vm_seed_class);
                SeedListName[element['VDName']] = tmp_vm_seed_class;  
            });  
            $.each( JsonData['User'], function(index, element){  
                calc_count(element);
                memory = ClacMemory(element['RAM']);                              
                VMCheckName[index] = element['VDName']; 
                seedname = element['SeedName'];                   
                vd_usage_size = element['AllSize']; // 桌面設定-新增VD檔案大小實作 2017.05.04 william           
                tmp_vm_class = new VMClass(element['VDName'],0,0,element['State'],0,seedname,element['Desc'],0,element['CreateTime'],element["Online"],createobj.GetVMStatus(element['State'],element['Online']),element['Suspend'],memory.toFixed(2),element['VDID'],vd_usage_size);
                BornedListTable.push(tmp_vm_class);
                BornedListName[element['VDName']] = tmp_vm_class;
                if(typeof(SeedListName[seedname]) !== 'undefined'){                    
                    SeedListName[seedname]['vdi_count']++;
                }
            });                     
        };          
        function ClacMemory(memory){            
            memory =  Math.round(memory*100/1024);
            memory = memory/100;
            return memory;
        }
        createobj.CreateVMStatusText = function(status){ // VM狀態值修改 2017.02.24 william
            if(status < 0){
                return LanguageStr_VM.VM_Task_Failed;
            }
            else if(status === 0){
                return LanguageStr_VM.VM_Task_Completed;
            }                
            else if(status === 1){
                return LanguageStr_VM.VM_Task_Waiting;
            }
            else if(status === 2){
                return LanguageStr_VM.VM_Task_Cloning;
            }
            
        };
        function CreateTaskTypeText(input_type){
            var type = '';
            switch(input_type){
                case 0:
                    type = LanguageStr_VM.VM_Task_Clone;
                    break;
                case 1:
                    type = LanguageStr_VM.VM_Task_Make_Seed;
                    break;
                case 2:
                    type = LanguageStr_VM.VM_Born_Users + VMorVDI;
                    break;
                case 3:
                    type = LanguageStr_VM.VM_Task_Create_Original;
                    break;
                case 10:
                    type = LanguageStr_VM.VM_Task_Add_Disk;
                    break;
                case 11:
                    type = LanguageStr_VM.VM_Task_Del_Disk;
                    break;
                case 12:
                    type = LanguageStr_VM.VM_Task_Export_VD;
                    break;
                case 13:
                    type = LanguageStr_VM.VM_Task_Import_VD;
                    break;
                default : 
                    type = input_type;
            };
            return type;
        }
        createobj.CreateVMTaskHtmlBTable = function(JsonData){
            var HtmlStr = '';   
            var no = 0;       
            $.each( VMListCloneTable, function(index, element){ 
                no = index + 1;                  
                if(index === VMListCloneTable.length - 1)
                    HtmlStr += '<div class = "Div_VMRow LastVMRow">';
                else
                    HtmlStr += '<div class = "Div_VMRow">';
                //HtmlStr += '<div class = "Div_VMRow">';    
                
                // 2016.12.19 william新增
                HtmlStr += '<H4 class = "TaskList_ItemNo_Row AbsoluteLayout RowField" >' + no  + '</H4>';

                /* 未來新增之項目 2016.12.23 william
                HtmlStr += '<div class = "TaskList_Select_Row AbsoluteLayout RowField" >';
                HtmlStr += '<div style="position:relative">'; 
                if(element['state'] === -2)
                    HtmlStr += '<input type="checkbox" id="' + element['id'] + '" class="iCheckBoxTaskSelect iCheckBoxDisable">';
                else
                    HtmlStr += '<input type="checkbox" id="' + element['id'] + '" class="iCheckBoxTaskSelect">';
                HtmlStr += '</div>';
                HtmlStr += '</div>'; 
                */            
                HtmlStr += '<H4 class = "VMList_Type_Row AbsoluteLayout RowField">' + element['type'] + '</H4>';
                HtmlStr += '<H4 class = "VMList_Source_Name_Row AbsoluteLayout RowField" title="' + element['srcDomName'] + '">' + element['srcDomName'] + '</H4>';
                HtmlStr += '<H4 class = "VMList_Dest_Name_Row AbsoluteLayout RowField" title="' + element['destDomName'] + '">' + element['destDomName'] +'</H4>';
                if(element['status'] ===2){
                    HtmlStr += '<div id="divprogress' + index + '" class = "VMList_Task_Progress_Row AbsoluteLayout RowField">';
                    HtmlStr += '<div>';
                    HtmlStr += '<div style="position: relative;top: 4px;"">';
                    HtmlStr += '<div id="progress' + index + '" class="progressbar_clone">';
                    HtmlStr += '<div id="progressText' + index +  '" class="progress-label" style="width:150px;top:-4px"></div>';
                    HtmlStr += '</div>';
                    HtmlStr += '</div>';
                    HtmlStr += '</div>';
                    HtmlStr += '</div>';       
                }                
                HtmlStr += '<H4 id="clonestatus' + element['id'] + '" class = "VMList_Task_Status_Row AbsoluteLayout RowField">' + createobj.CreateVMStatusText(element['status']) + '</H4>';                
                HtmlStr += '<H4 id="start_time' + element['id'] + '" class = "VMList_Start_Time_Row AbsoluteLayout RowField">' + element['start_time'] + '</H4>';                
                HtmlStr += '<H4 id="end_time' + element['id'] + '" class = "VMList_End_Time_Row AbsoluteLayout RowField">' + element['end_time'] + '</H4>';
                HtmlStr += '</div>';
            });
            return HtmlStr;
        };
        
        createobj.toDateTime = function(secs)
        {
            var t = new Date(1970,0,1);
            t.setSeconds(secs);
            return t.yyyymmddHHmmss();
        };
        
        Date.prototype.yyyymmddHHmmss = function() {                                         
//            var yyyy = this.getFullYear().toString();                                    
//            var mm = (this.getMonth()+1).toString(); // getMonth() is zero-based         
//            var dd  = this.getDate().toString();             
//            return yyyy + '-' + (mm[1]?mm:"0"+mm[0]) + '-' + (dd[1]?dd:"0"+dd[0]);
            return [this.getFullYear(),
                (this.getMonth()+1).padLeft(),
               this.getDate().padLeft()
               ].join('/') +' ' +
              [this.getHours().padLeft(),
               this.getMinutes().padLeft(),
               this.getSeconds().padLeft()].join(':');                
            
        };  
        
        Number.prototype.padLeft = function(base,chr){
            var  len = (String(base || 10).length - String(this).length)+1;
            return len > 0? new Array(len).join(chr || '0')+this : this;
        };
        
        createobj.CreateVMTaskTable = function(JsonData){
            VMListCloneTable = [];
            VMListCloneDestName = [];
//            var HtmlStr = '';            
            $.each( JsonData, function(index, element){ 
                var start_time = '';
                var end_time = '';
                //if(typeof(element['start_localtime']) !== 'undefined' && element['start_localtime'] !== -1){
                if(typeof(element['StartTime']) !== 'undefined'){
                    start_time = element['StartTime'];
                }
//                if(typeof(element['end_localtime']) !== 'undefined' && element['end_localtime'] !== -1){
                if(typeof(element['EndTime']) !== 'undefined'){
                    end_time = element['EndTime'];
                }
                var tmp_vm_clone_class = new VMCloneClass(element['ID'],CreateTaskTypeText(element['Type']),element['Source'],element['Dest'],element['Process'],element['State'],start_time,end_time);
                VMListCloneTable.push(tmp_vm_clone_class);
                VMListCloneDestName[element['ID']] = tmp_vm_clone_class;
            });            
        };
        createobj.CreateVMDropDownHtml = function(name,Status,isSeed,SeedSrc){
            var drop_down_content = '';
            if(isSeed === 1)
            {
                drop_down_content = '#dropdown-VM-Operation5';
            }            
            else
            {               
                switch(Status)
                {
                    case -3:
                    case -5:
                        drop_down_content = '#dropdown-VM-Operation12';
                        break;
                    case -2:
                        drop_down_content = '#dropdown-VM-Operation7';
                        break;
                    case -1:
                        drop_down_content = '#dropdown-VM-Operation4';
                        break;
                    case 1:
                        if(SeedSrc.length > 0)
                            drop_down_content = '#dropdown-VM-Operation11';
                        else
                            drop_down_content = '#dropdown-VM-Operation2';
                        break;
                    case 5 :
                        if(SeedSrc.length > 0)
                            drop_down_content = '#dropdown-VM-Operation6';
                        else
                            drop_down_content = '#dropdown-VM-Operation1';
                        break;
                    default:
                        drop_down_content = '#dropdown-VM-Operation3';
                        break;
                }                
            }
            return '<a href="#" class="btn-light btn-operation-vm" data-dropdown="' + drop_down_content + '" data-operation="' + name + '">' + LanguageStr_VM.VM_Setting + '&nbsp;▼</a>';
        };
                
        createobj.GetVMOnlineStatus = function(Online){
            switch(Online){
                case false:
                    return '<label style="color:#044de1">' + LanguageStr_VM.VM_Disconnected + '</label>'; /* SnapShot&Schedule&Backup  2017.01.11 william */
                    break;
                case true:
                    return '<label style="color:green">' + LanguageStr_VM.VM_Connected + '</label>';
                    break;                                
            }
        };
        createobj.GetVMStatus = function(Status,Online){
            switch(Status){
                case -5:
                    return '<label style="color:red">SS Crash</label>';
                    break;
                case -3:
                    return '<label style="color:red">Not Ready</label>';
                    break;
                case -2:
                    return '<label style="color:red">' + LanguageStr_VM.VM_Waiting_Poweron + '</label>';
                    break;
                case -1:
                    return '<label style="color:red">' + LanguageStr_VM.VM_Disable + '</label>';
                    break;
                case 0:
                    return LanguageStr_VM.VM_Stop;                    
                    break;
                case 1:
//                    var online_desc = Online === 1 ? "(Online)" : "(Offline)";
//                    return 'Running' + online_desc;
                    return '<label style="color:green">' + LanguageStr_VM.VM_Poweron + '</label>';
                    break;
                case 2:
                    return LanguageStr_VM.VM_Blocked;
                    break;
                case 3:
                    return '<label style="color:black">' + LanguageStr_VM.VM_Pause  + '</label>';
                    break;
                case 4:
                    return '<label style="color:#044de1">' + LanguageStr_VM.VM_Shutdowning + '</label>'; // SnapShot&Schedule&Backup  2017.01.11 william 
                    break;
                case 5:
                    return '<label style="color:#044de1">' + LanguageStr_VM.VM_Shutdown + '</label>'; // SnapShot&Schedule&Backup  2017.01.11 william 
                    break;
                case 6:
                    return LanguageStr_VM.VM_Crash;
                    break;
                case 7:
                    return '<label style="color:black">' + LanguageStr_VM.VM_Suspend + '</label>';                    
                    break;
                default:
                    return Status;
            }
        };       
        createobj.CreateIPStatusHtml = function(JsonData){
            var HtmlStr = "";  
            var index = 1;
            var onlinestatus = 'Offline';
            HtmlStr += '<div style="height:680px;overflow:auto">';
            $.each(JsonData['NIC'], function(nicindex, element){
                onlinestatus = element['Online']==true?'Online':'Offline';
                HtmlStr += '<div>';
                HtmlStr += '<label class="LabelCPURAMHead" style="font-size: 14px;color:#044de1">' + LanguageStr_Network.IP_NIC + index + ' :</label>'; // SnapShot&Schedule&Backup  2017.01.11 william 
                HtmlStr += '<br>';
                HtmlStr += '<br>';
                HtmlStr += '<label class="LabelCPURAMHead2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + LanguageStr_SystemInfo.SystemInfo_Status + '</label>';
                HtmlStr += '<label>' + onlinestatus + '</label>';
                HtmlStr += '<br>';
                HtmlStr += '<br>';
                HtmlStr += '<label class="LabelCPURAMHead2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + LanguageStr_SystemInfo.SystemInfo_Speed + '</label>';
                HtmlStr += '<label>' + add_comma_of_number(element['Speed']) + '</label>';                                
                HtmlStr += '<br>';
                HtmlStr += '<br>';
                HtmlStr += '<label class="LabelCPURAMHead2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + LanguageStr_SystemInfo.SystemInfo_Mode + '</label>';
                HtmlStr += '<label>' + add_comma_of_number(element['Mode']).replace('baseT','Base-T') + '</label>';
                HtmlStr += '</div>';
                if(index < JsonData['NIC'].length)
                {
                    HtmlStr += '<div style="width:220px;height:5px;border-bottom: dashed 1px rgba(0, 0, 0, .5)"></div>';
                    HtmlStr += '<div style="height:10px"></div>';
                }
                index++;
            });
            HtmlStr += '</div>';
            return HtmlStr;
            
        };
        createobj.CreateDiskImgCombo = function(JsonData,isModify){
            var HtmlStr = "";              
            if(isModify)
                HtmlStr += '<select id="combo_modify_img">';
            else
                HtmlStr += '<select id="combo_img">';
            $.each(JsonData['DiskImgs'], function(index, element){
                 HtmlStr += '<option value="' + element['Path'] + '">' + element['Name'] + '</option>';
            });
            HtmlStr += '</select>';
            return HtmlStr;
        };       
        createobj.CreateModifyIsoCombo = function(JsonData){
            var HtmlStr = "";       
            var OptionHtmlStr = "";       
            var source_split_arr;
            var name = "";
            var target = "";             
            var is_have_none = false;
            var tmp_source = '';
            $.each(JsonData['CDRom'], function(index, element){
                OptionHtmlStr += '<option value='+element['Path']+'>'+element['Name']+'</option>';
                // target = element['Target'];
                // if(element['Source'] === ''){
                //     OptionHtmlStr += '<option value="none">None</option>';            
                //     is_have_none = true;
                // }
                // else{                    
                //     source_split_arr = element['Source'].split("/");
                //     if(source_split_arr.length > 0)
                //         name = source_split_arr[source_split_arr.length - 1];
                //     OptionHtmlStr += '<option value="' + element['Source'] + '">' + name + '</option>';
                // }
            });
            $target = JsonData['CDRom'][0]['Path'];
            // if(!is_have_none)
            //     OptionHtmlStr += '<option value="none">None</option>'; 
            HtmlStr += '<select id="combo_modify_iso" data-target="' + target + '">';
            HtmlStr += OptionHtmlStr;
            HtmlStr += '</select>';
            return HtmlStr;
        };
        createobj.CreateModifyIsoComboBoot = function(JsonData){
            var HtmlStr = "";       
            var OptionHtmlStr = "";                   
            var target = "";                         
            $.each(JsonData['CDRom'], function(index, element){
                OptionHtmlStr += '<option value='+element['Path']+'>'+element['Name']+'</option>';               
            });
            $target = JsonData['CDRom'][0]['Path'];
            // if(!is_have_none)
            //     OptionHtmlStr += '<option value="none">None</option>';             
            HtmlStr += '<select id="combo_iso_boot" data-target="' + target + '">';
            HtmlStr += OptionHtmlStr;
            HtmlStr += '</select>';
            return HtmlStr;
        };
        createobj.CreateIsoCombo = function(JsonData){
            var HtmlStr = "";              
            HtmlStr += '<select id="combo_iso">';
            HtmlStr += '<option value="none">None</option>';
            $.each(JsonData['CDRom'], function(index, element){
                 HtmlStr += '<option value="' + element['Path'] + '">' + element['Name'] + '</option>';
            });
            HtmlStr += '</select>';
            return HtmlStr;
        };
        createobj.CreateVMNICListHtml = function(JsonData,isModify){
            var HtmlStr = "";         
            var speed = 1000;
            HtmlStr += '<div style="height:300px;overflow:auto">';
            $.each(JsonData['NICs'], function(index, element){
                HtmlStr += '<div style="height:40px">';
                HtmlStr += '<label class="LabelHead">NIC ' + (index + 1)  + LanguageStr_SystemInfo.SystemInfo_Speed + '</label>';
                speed = element['speed']*1000;
                HtmlStr += '<label style="width:130px;display:inline-block">' + add_comma_of_number(speed)  + ' Mb/s</label>';
                HtmlStr += '<label style="width:130px;display:inline-block">Virsual Switch ' + element['vswitch']  + '</label>';
                if(isModify)
                    HtmlStr += '<a id="' + element['mac']  + '"class="btn-vm-delete-nic btn-jquery large-btn" style="font-size: 14px;">' + LanguageStr_VM.VM_Delete_Txt + '</a>';
                HtmlStr += '</div>';
            });
            HtmlStr += '</div>';
            return HtmlStr;
        };
        createobj.CreateVMDiskListHtml = function(JsonData,isModify){
            var HtmlStr = "";         
            var Capacity = "";
            HtmlStr += '<div style="height:300px;overflow:auto">';            
            $.each(JsonData['Disks'], function(index, element){
                HtmlStr += '<div style="height:40px">';
                if(index === 0)
                    HtmlStr += '<label class="LabelHead" style="width:185px">' + LanguageStr_VM.VM_Boot_Disk + '</label>';
                else
                    HtmlStr += '<label class="LabelHead" style="width:185px">' + LanguageStr_VM.VM_Data_Disk + '</label>';
                Capacity = element['Size'];                
                HtmlStr += '<label style="width:80px;display:inline-block">' + add_comma_of_number(add_two_fixed_float(Capacity))  + ' GB</label>';
                 HtmlStr += '<label style="width:80px;display:inline-block">' + changeVMDiskStatus(element['State'])  + '</label>';
                if(index !== 0 && isModify){
                    if(element['State'] == 0)                    
                        HtmlStr += '<a id="' + element['DiskID']  + '" href="#" class="btn-vm-delete-disk" style="font-size: smaller">' + LanguageStr_VM.VM_Delete_Txt + '</a>';
                }
                HtmlStr += '</div>';
            });
            HtmlStr += '</div>';
            return HtmlStr;
        };
        function changeVMDiskStatus(status){
            switch(status){
                case '0' :
                    return 'Ready';
                case '30' :
                    return 'Adding';
                case '31' :
                    return 'Deleting';
                default :
                    return 'Not Ready';
            }
        }
        createobj.CreateiSCSIAfterDiscoveryHtml= function(JsonData){
            var rtnhtml = '';            
            rtnhtml += '<div style="height:40px;">';
            rtnhtml += '<div style="position:relative;">';
            rtnhtml += '<label style="position:absolute;top:7px;" class="LabelHead">Connect Target :</label>';
            rtnhtml += '<div style="position:absolute;width: 280px;top:0px;left:210px">';
            rtnhtml += '<select id="combo_target" class="combo_iscsi_target" style="width: 280px;">';
            $.each(JsonData['targets'], function(index, element) {
                rtnhtml += '<option value="' + element + '">' + element + '</option>';
            });
            rtnhtml += '</select></div></div></div>';
            rtnhtml += '<div style="height:200px;overflow:auto">';
            $.each(JsonData['ips'], function(index, element) {
                rtnhtml += '<div style="height:40px;">';
                rtnhtml += '<div style="position:relative;">';
                var priority_index = index + 1;
                rtnhtml += '<label style="position:absolute;top:7px;" class="LabelHead">Connect Priority ' + priority_index + ' : </label>';
                rtnhtml += '<div style="position:absolute;width: 280px;top:0px;left:210px">';
                rtnhtml += '<select id="MPIO' + priority_index + '" class="combo_iscsi_mpio_ip" style="width: 280px;">';
                $.each(JsonData['ips'], function(ipindex, ipelement) {
                    rtnhtml += '<option value="' + ipelement + '"';
                    if(index == ipindex)
                        rtnhtml += ' selected';
                    rtnhtml += '>';
                    rtnhtml += ipelement;
                    rtnhtml += '</option>';
                });
                rtnhtml += '<option value="none">none</option>';
                rtnhtml += '</select></div></div></div>';
            });
            rtnhtml += '</div>';
            return rtnhtml;
        };
        createobj.CreateiSCSILabelSessionHtml = function(JsonData){
            var HtmlStr = '';                
            var State = 'Unknow';
            $.each( JsonData['sessions'], function(index, element){     
                $.each(element['ip'],function(ipindex,ipelement){                    
                    switch(ipelement['state']){
                        case 1:
                            State="Active";
                            break;
                        case 2:
                            State="Standby";
                            break;
                        case 0:
                            State="Offline";
                            break;
                    }
                    var prio_index = ipindex + 1;
                    HtmlStr += '<label id="label_iscsi_session">Priority ' + prio_index + ' : ' + ipelement['ip'] + ' ( ' + State + ' )</label><br><br>';
                });
            });
            return HtmlStr;
        }
        createobj.CreateiSCSISessionHtml = function(JsonData){            
            var HtmlStr = '';            
            var State = 'Unknow';
            $.each( JsonData['sessions'], function(index, element){                                 
                HtmlStr += '<div class = "Div_iSCSIRow">';
                HtmlStr += '<H4 class = "iSCSIList_Name_Row AbsoluteLayout RowField">' + element['name'] + '</H4>';
                HtmlStr += '<div class = "iSCSIList_IP_Row AbsoluteLayout RowField">';
                $.each(element['ip'],function(ipindex,ipelement){                    
                    switch(ipelement['state']){
                        case 1:
                            State="Active";
                            break;
                        case 2:
                            State="Standby";
                            break;
                        case 0:
                            State="Offline";
                            break;
                    }
                    var prio_index = ipindex + 1;
                    HtmlStr += '<label>Priority ' + prio_index + ' : ' + ipelement['ip'] + ' ( ' + State + ' )</label><br><br>';
                });
                HtmlStr += '</div>';
                HtmlStr += '<div class = "iSCSIList_Action_Row AbsoluteLayout RowField" >'; 
                HtmlStr += '<a href="#" class="btn-light btn-operation-vm" data-dropdown="#dropdown-iSCSI" >' + LanguageStr_VM.VM_Setting + '&nbsp;▼</a>';
                HtmlStr += '</div>';
                HtmlStr += '</div>';
            });
            return HtmlStr;        
        };
        createobj.CreateLogHtml = function(JsonData,start_index){ // 新增log list項次 2016.12.21 william 
            var HtmlStr = '';                 
            var LevelHtmlStr='';
            var TypeHtmlStr='';
            var no ; 
            $.each( JsonData['Logs'], function(index, element){       
                no = start_index;           
                LevelHtmlStr='';
                TypeHtmlStr='';
                switch(element['Level']){
                    case 1:
                        LevelHtmlStr = '<img class="LogList_Level_Row" src="/img/msg_info.png" height="20" width="20" alt="Information">';
                        break;
                    case 2:
                        LevelHtmlStr = '<img class="LogList_Level_Row" src="/img/msg_warning.png" height="20" width="20" alt="Warning">';
                        break;
                    case 3:
                        LevelHtmlStr = '<img class="LogList_Level_Row" src="/img/msg_error.png" height="20" width="20" alt="Error">';
                        break;
                }
                switch(element['Type']){
                    // case 1:
                    //     TypeHtmlStr='System';
                    //     break;
                    // case 2:
                    //     TypeHtmlStr='Disk';
                    //     break;
                    // case 3:
                    //     TypeHtmlStr='RAID';
                    //     break;
                    case 4:
                        TypeHtmlStr='User';
                        break;
                    case 5:
                        TypeHtmlStr='VD';
                        break;    
                    case 6:
                        TypeHtmlStr='System';
                        break;
                    case 8:
                        TypeHtmlStr='Schedule';
                        break;
                    case 9:
                        TypeHtmlStr='Job';
                        break;
                    case 10:
                        TypeHtmlStr='Snapshot';
                        break;
                    case 12:
                        TypeHtmlStr='Backup';
                        break;
                    case 14:
                        TypeHtmlStr='RAID';
                        break;
                }
                if(index === JsonData['Logs'].length - 1)
                    HtmlStr += '<div class = "Div_VMRow LastVMRow">';
                else
                    HtmlStr += '<div class = "Div_VMRow">';
                //HtmlStr += '<div class = "LogList_Level_Row AbsoluteLayout RowField">' + LevelHtmlStr +'</div>';
                HtmlStr += LevelHtmlStr;

                // 新增log list項次 2016.12.21 william 
                HtmlStr += '<H4 class = "LogList_ItemNo_Row AbsoluteLayout RowField" >' + no  + '</H4>';           
                HtmlStr += '<H4 class = "LogList_Type_Row AbsoluteLayout RowField">' + TypeHtmlStr + '</H4>';
                HtmlStr += '<H4 class = "LogList_Time_Row AbsoluteLayout RowField">' + element['Time'] + '</H4>';
                HtmlStr += '<H4 class = "LogList_EventCode_Row AbsoluteLayout RowField" >' + element['Code'] + '</H4>';
                HtmlStr += '<label class = "LogList_Content_Row AbsoluteLayout RowField" title="' + element['Message'] + '">' + element['Message'] + '</label>';                
                HtmlStr += '<H4 class = "LogList_User_Row AbsoluteLayout RowField">' + element['Riser']  + '</H4>';
                HtmlStr += '<H4 class = "LogList_SrcIP_Row AbsoluteLayout RowField">' + element['SourceIP'] + '</H4>';
                HtmlStr += '</div>';   
                start_index++;         
            });            
            return HtmlStr;
        };
        createobj.CreateImportVDsHtml = function(JsonData){
            var HtmlStr = '';
            var i =1;
            $.each( JsonData, function(index, element){               
                HtmlStr += '<tr>';
                HtmlStr += '<td style="text-align:center">' + i + '</td>';              
                HtmlStr += '<td style="text-align:center"><input id="import' + element + '" style="width:300px;height:25px" type="text" value="' + element + '"></td>';
                HtmlStr += '<td style="text-align:center"><a id="' + element + '" href="#" class="btn-vd-import" style="font-size: smaller">'+LanguageStr_VM.VM_Str_Import+'</a></td>';
                HtmlStr += '</tr>';
                i++;
            });  
            return HtmlStr;
        };
        createobj.blockPage = function() {
            $('.Div_WaitAjax').dialog( 'open' );
//            $('#dialog_msg').text('Please Wait...');
//            $( "#dialog_wait" ).dialog('open');
//            $.blockUI();
        };
        createobj.blockPageMsg = function(msg) {
//            $('#dialog_msg').html('<h3>'+msg+'</h3>');
//            $("#dialog_wait" ).dialog('open');
            $('#dialog_vm_msg_reboot').html('<h3>'+msg+'</h3>');
            $( "#dialog_vm_message_reboot" ).dialog('open');
        };
        createobj.ChangeblockMsg = function(msg) {
//            $('#dialog_msg').html('<h3>'+msg+'</h3>');   
            $('#dialog_vm_msg_reboot').html('<h3>'+msg+'</h3>');
        };
        createobj.stopPage = function() {
            $('.Div_WaitAjax').dialog( 'close' );
        };
        createobj.stopBlockPage = function() {
//            $( "#dialog_wait" ).dialog('close');
            $( "#dialog_vm_message_reboot" ).dialog('close');
        };
        createobj.HideAllOption = function(){
            $.each(DivAllOption,function(index,element){
               $(element[0]).hide(); 
               $(element[1]).removeClass('OptionSelect');
               $(element[1]).css('background-image','');
               $(element[1]).addClass('OptionUnSelect');
            });
        };
        createobj.ShowOption = function(Option){
            $(Option[0]).show();            
            $(Option[1]).removeClass('OptionUnSelect');
            $(Option[1]).css('background-image','');
            $(Option[1]).addClass('OptionSelect');
        };
        // VD手動快照介面功能實作 2017.02.09   
        // ----------- VD快照 (1)查詢(List) 2017.02.09 Start ----------- 
        createobj.processSnapshotData = function(JsonData) {
            var rtnhtml = '';
            var State_tmp;
            SnapshotList=[];            
            var no = 1;  
            var isView = false;
            var isRollback = false;
            $.each(JsonData, function(index, element) {                
                if(element['State'] == 1 || element['State'] == 2)
                    isView = true;
                SnapshotList[element['Layer']] = new SnapShotClass(element['Layer'],element['UpperLayer'],element['CreateTime'],element['Desc'],element['State'],element['Size']);               
            });
            if(isView){
                $('.btn_Take_Snapshot').button( "option", "disabled", true );   
                $('#btn_StopView_snapshot').button( "option", "disabled", false );  
            }
            else{
               $('.btn_Take_Snapshot').button( "option", "disabled", false );
            $('#btn_StopView_snapshot').button( "option", "disabled", true );  
            }
            for(var key in SnapshotList){
                if(SnapshotList[key].State == 4)
                    isRollback = true;
                if(isView || isRollback)
                    State_tmp = 6;
                else
                    State_tmp = SnapshotList[key].State;
                rtnhtml += createobj.CreateSnapshotHtml(no,State_tmp,SnapshotList[key].Layer, SnapshotList[key].UpperLayer,SnapshotList[key].CreateTime,SnapshotList[key].Desc,SnapshotList[key].State,SnapshotList[key].Size);                
                no ++;
            }
            return rtnhtml;
        };
   
        createobj.CreateSnapshotHtml = function(no, State_tmp, layer, upperlayer, createtime, desc, state, size) {   
            var rtnhtml = '<tr>';
            rtnhtml += '<td id="Takess_bottom_Item" class="takess_bottom0"><label>' + no + '</label></td>';
            rtnhtml += '<td id="Takess_bottom_Schedule_Name" class="takess_bottom2"><label>' + createtime + '</label></td>';
            rtnhtml += '<td id="Takess_bottom_Description" class="takess_bottom4"><label>' + desc + '</label> </td>';                    
            rtnhtml += '<td id="Takess_bottom_Time" class="takess_bottom5"><label>' + formatBytes(size, 1) + '</label> </td>';               
            rtnhtml += '<td id="Takess_bottom_Isview" class="takess_bottom1"><label>' + SnapshotStateText(state) + '</label></td>';
            rtnhtml += '<td id="Takess_bottom_Action" class=" takess_bottom3 takess_LastColumn">';
            rtnhtml += createobj.CreateSnapshotDropDownHtml(no, layer, State_tmp);     
            rtnhtml += '</td>';           
            rtnhtml += '</tr>';

            return rtnhtml;
        };       

        createobj.CreateSnapshotDropDownHtml = function(no, layer, state){
            var drop_down_content = ''; 
            var SnapshotDropDownBtn = '';

            switch(state){
                case 0: // Normal
                    drop_down_content = '#dropdown-VM-Operation8';   
                    SnapshotDropDownBtn = '<a href="#" id="'+ layer +'" class="btn-light btn-operation-vm SnapShotDropBtn" data-dropdown="' + drop_down_content + '" data-operation="' + no + '">' + LanguageStr_VM.VM_Setting + '&nbsp;▼</a>';
                    break;
                case 1: // Preparing_view
                    drop_down_content = '#dropdown-VM-Operation8';   
                    SnapshotDropDownBtn = '<a href="#" id="'+ layer +'" class="btn-light btn-operation-vm SnapShotDropBtn dropdown-disabled" data-dropdown="' + drop_down_content + '" data-operation="' + no + '">' + LanguageStr_VM.VM_Setting + '&nbsp;▼</a>';
                    $('#'+layer+'').prop('disabled',true);
                    break;
                case 2: // View
                    drop_down_content = '#dropdown-VM-Operation8';   
                    SnapshotDropDownBtn = '<a href="#" id="'+ layer +'" class="btn-light btn-operation-vm SnapShotDropBtn dropdown-disabled" data-dropdown="' + drop_down_content + '" data-operation="' + no + '">' + LanguageStr_VM.VM_Setting + '&nbsp;▼</a>';
                    break;
                case 3: // Preparing_delete
                    drop_down_content = '#dropdown-VM-Operation8';   
                    SnapshotDropDownBtn = '<a href="#" id="'+ layer +'" class="btn-light btn-operation-vm SnapShotDropBtn dropdown-disabled" data-dropdown="' + drop_down_content + '" data-operation="' + no + '">' + LanguageStr_VM.VM_Setting + '&nbsp;▼</a>';
                    break;
                case 4: // Preparing_rollback                
                    drop_down_content = '#dropdown-VM-Operation8'; 
                    SnapshotDropDownBtn = '<a href="#" id="'+ layer +'" class="btn-light btn-operation-vm SnapShotDropBtn dropdown-disabled" data-dropdown="' + drop_down_content + '" data-operation="' + no + '">' + LanguageStr_VM.VM_Setting + '&nbsp;▼</a>';            
                    break;
                case 5: // Preparing_unview
                    drop_down_content = '#dropdown-VM-Operation8';   
                    SnapshotDropDownBtn = '<a href="#" id="'+ layer +'" class="btn-light btn-operation-vm SnapShotDropBtn" data-dropdown="' + drop_down_content + '" data-operation="' + no + '">' + LanguageStr_VM.VM_Setting + '&nbsp;▼</a>';
                    break;   
                case 6: 
                    drop_down_content = '#dropdown-VM-Operation8';   
                    SnapshotDropDownBtn = '<a href="#" id="'+ layer +'" class="btn-light btn-operation-vm SnapShotDropBtn dropdown-disabled" data-dropdown="' + drop_down_content + '" data-operation="' + no + '">' + LanguageStr_VM.VM_Setting + '&nbsp;▼</a>';
                    break;                                      
                default : // default
                    drop_down_content = '#dropdown-VM-Operation8';   
                    SnapshotDropDownBtn = '<a href="#" id="'+ layer +'" class="btn-light btn-operation-vm SnapShotDropBtn" data-dropdown="' + drop_down_content + '" data-operation="' + no + '">' + LanguageStr_VM.VM_Setting + '&nbsp;▼</a>';
            };
            return SnapshotDropDownBtn; 

            // drop_down_content = '#dropdown-VM-Operation8';           
            // return '<a href="#" id="'+ layer +'" class="btn-light btn-operation-vm SnapShotDropBtn" data-dropdown="' + drop_down_content + '" data-operation="' + no + '">' + LanguageStr_VM.VM_Setting + '&nbsp;▼</a>';
        };

         function SnapshotStateText (input_state){
            var state = '';

            switch(input_state){
                case 0:
                    state = LanguageStr_VM.VM_SnapshotState_Normal;
                    break;
                case 1:
                    state = LanguageStr_VM.VM_SnapshotState_Preparing_view;
                    break;
                case 2:
                    state = LanguageStr_VM.VM_SnapshotState_View;
                    break;
                case 3:
                    state = LanguageStr_VM.VM_SnapshotState_Preparing_delete;
                    break;
                case 4:
                    state = LanguageStr_VM.VM_SnapshotState_Preparing_rollback;
                    break;
                case 5:
                    state = LanguageStr_VM.VM_SnapshotState_Preparing_unview;
                    break;                    
                default : 
                    state = input_state;
            };
            return state;            
        };   
        // ----------- VD快照 (1)查詢(List) 2017.02.09 End -----------       
        // ----------- VD快照 (2)Task(List) 2017.02.09 Start ----------- 
        createobj.CreateVMSnapshotTaskTable = function(JsonData){
            VMListSnapshotCloneTable = [];
            VMListSnapshotCloneDestName = [];
//            var HtmlStr = '';            
            $.each( JsonData, function(index, element){ 
                var start_time = '';
                var end_time = '';

                if(typeof(element['StartTime']) !== 'undefined'){
                    start_time = element['StartTime'];
                }
                if(typeof(element['EndTime']) !== 'undefined'){
                    end_time = element['EndTime'];
                }

                var tmp_vmsnapshot_clone_class = new VMSnapShotCloneClass(element['ID'],element['State'],CreateSnapshotTaskTypeText(element['Type']),element['VDName'],element['LayerDesc'],element['LayerDate'],start_time,end_time,element['Process'],element['ErrorCode']);
                VMListSnapshotCloneTable.push(tmp_vmsnapshot_clone_class);
                VMListSnapshotCloneDestName[element['ID']] = tmp_vmsnapshot_clone_class;
            });            
        };

        function CreateSnapshotTaskTypeText(input_type){
            var type = '';
            switch(input_type){
                case 101:
                    type = LanguageStr_VM.VM_SnapshotType_take;
                    break;
                case 102:
                    type = LanguageStr_VM.VM_SnapshotType_delete;
                    break;
                case 103:
                    type = LanguageStr_VM.VM_SnapshotType_rollback;
                    break;
                case 104:
                    type = LanguageStr_VM.VM_SnapshotType_view;
                    break;
                case 105:
                    type = LanguageStr_VM.VM_SnapshotType_view_stop;
                    break;
                case 106:
                    type = LanguageStr_VM.VM_SnapshotType_Schedule_Take;
                    break;                    
                default : 
                    type = input_type;
            };
            return type;
        }

        // function CheckLayerDate(input_LayerDate, input_StartTime){
        //     var LayerDate = '';
        //     if(input_LayerDate === input_StartTime) {
        //         LayerDate = '';
        //         return LayerDate;
        //     }
        //     else {
        //         LayerDate = input_LayerDate;
        //         return LayerDate;                
        //     }
        // }        

        createobj.CreateVMSnapshotTaskHtmlBTable = function(JsonData){
            var HtmlStr = '';   
            var no = 0;       
            $.each( VMListSnapshotCloneTable, function(index, element){ 
                no = index + 1;                  
                if(index === VMListSnapshotCloneTable.length - 1)
                    HtmlStr += '<div class = "Div_VMRow LastVMRow">';
                else
                    HtmlStr += '<div class = "Div_VMRow">';
                HtmlStr += '<H4 class = "TaskList_ItemNo_Row AbsoluteLayout RowField" >' + no  + '</H4>';          
                HtmlStr += '<H4 class = "VMSnapshotList_Task_Type_Row AbsoluteLayout RowField">' + element['type'] + '</H4>';
                HtmlStr += '<H4 class = "VMSnapshotList_Task_VDName_Row AbsoluteLayout RowField" title="' + element['vdname'] + '">' + element['vdname'] + '</H4>';            
                HtmlStr += '<H4 id="layerdate' + element['id'] + '" class = "VMSnapshotList_Task_LayerDate_Row AbsoluteLayout RowField" title="' + element['layerdate'] + '">' + element['layerdate'] +'</H4>'; 
                HtmlStr += '<H4 class = "VMSnapshotList_Task_LayerDesc_Row AbsoluteLayout RowField" title="' + element['layerdesc'] + '">' + element['layerdesc'] +'</H4>'; 
                HtmlStr += '<H4 id="ssclonestatus' + element['id'] + '" class = "VMSnapshotList_Task_State_Row AbsoluteLayout RowField">' + createobj.CreateVMStatusText(element['state']) + '</H4>';                
                HtmlStr += '<H4 id="start_time' + element['id'] + '" class = "VMSnapshotList_Task_StartTime_Row AbsoluteLayout RowField">' + element['start_time'] + '</H4>';                
                HtmlStr += '<H4 id="end_time' + element['id'] + '" class = "VMSnapshotList_Task_EndTime_Row AbsoluteLayout RowField">' + element['end_time'] + '</H4>';
                HtmlStr += '</div>';
            });
            return HtmlStr;
        };           
        // ----------- VD快照 (2)Task(List) 2017.02.09 End -----------        
        return createobj;
    }
};
