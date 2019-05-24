var UIPerformanceControl = {
    createNew:function(oInputHtml,oInputRelayAjax){ 
        var oHtml = oInputHtml;
        var oRelayAjax = oInputRelayAjax;                        
        var oUIPerformanceContorl={};
        var oError = {};
        var GetCPURAMInterval;             
        oUIPerformanceContorl.Init = function(){   
            VMIntervalStart = false;
            oError = ErrorHandle.createNew();
            InitPerformanceTab();                                                         
        };            
        
        function InitPerformanceTab(){
            $('#tab-performance-container').easytabs();
        };                                                                                            

        oUIPerformanceContorl.ClickGETCPURAM = function(){
            SytemInfo();
            CPURAMInfo();
            if(UtilPolling)
                GetCPURAMInterval = setInterval(function(){CPURAMInfo(); }, 5000); //新增RAID容量使用率 2016.12.08 william
        };

        $('#btn_refresh_performance').click(function(){
            event.preventDefault();
            oHtml.blockPage();
            CPURAMInfo();
            oHtml.stopPage(); 
        });

        function SytemInfo()
        {
            var request = oRelayAjax.SystemInfo(); 
            CallBackSytemInfo(request);
        };
        
        function CallBackSytemInfo(request){
            request.done(function(msg, statustext, jqxhr) {                 
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }    
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                $('#label_cpu').text(msg['CPU']['Model']);
                $('#label_cpu_count').html('&nbsp;&nbsp;&#215;' + msg['CPU']['Count']);
                var re = /^[0-9.]+/; 
                var m;
                if ((m = re.exec(msg['CPU']['Clock'])) !== null) {
                    // alert(m);
                    cpu_mhz = m[0]*1000;                    
                }
                $('#label_cpu_clock').text(msg['CPU']['Clock']);
                $('#label_cpu_cores').text(msg['CPU']['Cores'] + 'C/' + msg['CPU']['Threads'] + 'T');               
                $('#div_ram_info').html(oHtml.CreateRAMInfo(msg));
                $('#div_nic_state').html(oHtml.CreateIPStatusHtml(msg));                
            });
            request.fail(function(jqxhr, textStatus) {   
            });
        }     
           
        function CPURAMInfo()
        {
            var request = oRelayAjax.CPURAMInfo(); 
//            oHtml.blockPage();
            CallBackCPURAMInfo(request);
            
        };
        
        function CallBackCPURAMInfo(request){
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
                // SendListRAID(msg); //修改成SendListRAID() 2016.12.08 william
                GetRAIDStatusForPerformance(msg["Ceph"]);
                $('#div_cpu_ram').html(oHtml.CreateCPURAMPerformanceHtml(msg));
                $('#btn_clean_ram').click(function(event){
                    event.preventDefault();
                    CleanRAM();
                });
                
            });
            request.fail(function(jqxhr, textStatus) {   
//                oHtml.stopPage();
//                oError.CheckAuth(jqxhr.status,ActionStatus.VMStatus);
            });
        }           

    function GetRAIDStatusForPerformance(CephRaid) {
        CephRaidListForPerformance = [];
        $.each(CephRaid, function(index, element) { 
            CephRaidListForPerformance[element['RAIDID']] = new CephForPerformanceClass(element['RAIDID'],element['Total'],element['Used'],element['Usage']);
        });
    }                          
        
        function CleanRAM()
        {
            var request = oRelayAjax.CleanRAM(); 
            oHtml.blockPage();
            CallBackCleanRAM(request);
        };
        
        function CallBackCleanRAM(request){
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
                CPURAMInfo();           
            });
            request.fail(function(jqxhr, textStatus) {   
                oHtml.stopPage();
//                oError.CheckAuth(jqxhr.status,ActionStatus.VMStatus);
            });
        }
        
        oUIPerformanceContorl.ClearCPURAMInteval=function()
        {
            if(typeof(GetCPURAMInterval) !== 'undefined')
                clearInterval(GetCPURAMInterval);            
        }                
        
        return oUIPerformanceContorl;
    }
};
