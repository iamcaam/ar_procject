var Relay = {
    createNew: function() {
        var relayobj = {};     
        // 快照介面功能實作 2017.01.24   
        relayobj.TakeSnapshot = function(vd_id, desc) {
            var jsonrequest = '{"VDID":"' + vd_id + '","Desc":"' + desc + '"}';
            var request = relayobj.CallAjax("/manager/snapshot", "POST", jsonrequest, "");
            return request;            
        };               
        relayobj.DeleteSnapshot = function(id_Layer){
            var request = relayobj.CallAjax("/manager/snapshot/"+id_Layer, "DELETE", "", "");
            return request;
        };
        relayobj.listSnapshot = function(vd_id){                 
            var request = relayobj.CallAjax("/manager/vd/"+vd_id+"/snapshot", "GET", '', "json");
            return request;
        };
        relayobj.ViewSnapshot = function(id_Layer){
            var request = relayobj.CallAjax("/manager/snapshot/view/"+id_Layer, "PUT", "", "");
            return request;
        };
        relayobj.StopViewSnapshot = function(vd_id){
            var request = relayobj.CallAjax("/manager/snapshot/stop_view/"+vd_id, "PUT", "", "");
            return request;
        };
        relayobj.RollbackSnapshot = function(id_Layer){
            var request = relayobj.CallAjax("/manager/snapshot/rollback/"+id_Layer, "PUT", "", "");
            return request;
        };     
        // Snapshot task list 功能實作 2017.02.02             
        relayobj.VMClearSnapShotTask = function(){
            var request = relayobj.CallAjax("/manager/task/snapshot", "DELETE", "", "");
            return request;             
        };    
        relayobj.VMListSnapshotTask = function(){     
            var request = relayobj.CallAjax("/manager/task/snapshot", "GET", "", "json");
            return request;
        };                                         
        // vSwitch UI介面功能實作 2017.01.23                
        relayobj.listvswitch = function() { 
            var request = relayobj.CallAjax("/manager/vswitch", "GET", '', "json");
            return request;            
        };
        relayobj.listexternal = function() {            
            var request = relayobj.CallAjax("/manager/externalip", "GET", '', "json");
            return request;            
        };
        relayobj.setexternal = function(sJsonIP) {
            var jsonrequest='{"ip":[' + sJsonIP + ']}';    
            var request = relayobj.CallAjax("/manager/externalip", "POST", jsonrequest, "");
            return request;
        };
        relayobj.setip = function(sJsonIP, sJsonDNS) {
            var sJsonRequest = '{"Vswitchs":[' + sJsonIP + '],"DNS":[' + sJsonDNS + ']}';
            // alert(sJsonRequest);
            var request = relayobj.CallAjax("/manager/vswitch", "POST", sJsonRequest, "");
            return request;            
        };
        relayobj.setdns = function(sJsonDNS) {
            var sJsonRequest = '{"DNS":[' + sJsonDNS + ']}';
            var request = relayobj.CallAjax("/manager/dns", "POST", sJsonRequest, "");
            return request;            
        };          
        relayobj.changepwd = function(sOldPWD,sNewPWD) {
            var sJsonRequest = '{"OldPWD":"' + sOldPWD + '","PWD":"' + sNewPWD + '"}';            
            var request = relayobj.CallAjax("/manager/changepwd", "POST", sJsonRequest, "json");
            return request;            
        };     
        relayobj.listversion = function(){           
            var request = relayobj.CallAjax("/manager/update/now", "GET", '', 'json');
            return request;            
        };
        relayobj.listuploadnewversion = function(){           
            var request = relayobj.CallAjax("/manager/update/uploadnew", "GET", '', 'json');
            return request;            
        };
        relayobj.checknewversion = function(){
            var request = relayobj.CallAjax("/manager/update/new", "GET", '', 'json');
            return request;            
        };
        relayobj.updatenewversion = function(){           
            var request = relayobj.CallAjax("/manager/update/updatenew", "PUT", '', 'json');
            return request;            
        };
        relayobj.localupdatenewversion = function(){           
            var request = relayobj.CallAjax("/manager/update/updatelocalnew", "PUT", '', 'json');
            return request;            
        };
        relayobj.checkupdate = function(){            
            var request = relayobj.CallAjax("/manager/update/check", "GET", '', 'json');
            return request;            
        };
        relayobj.listlog = function(page,level,type,field,asc) {
            var url_query='';            
            url_query += 'Page=' + page;
            url_query += '&Level=' + level;
            url_query += '&Type=' + type;
            url_query += '&Field=' + field;
            url_query += '&ASC=' + asc;                      
            var request = relayobj.CallAjax("/manager/log?"+url_query, "GET", '','json');
            return request;            
        };   
        relayobj.SetAlarm = function(bEnable){
            var jsonrequest='{"Alarm":' + bEnable + '}';            
            var request = relayobj.CallAjax("/manager/alarm", "PUT", jsonrequest, "");
            return request;  
        };
        relayobj.ListAlarm = function(){        
            var request = relayobj.CallAjax("/manager/alarm", "GET", "", "json");
            return request;  
        };
        relayobj.ListTime = function(){                
            var request = relayobj.CallAjax("/manager/time", "GET", "", "");
            return request;  
        };
        relayobj.SetTime = function(mode,timezone,time,ntpserver){
            var jsonrequest = '{"TimeZone":"'+ timezone 
                    +'","Time":"' + time 
                    + '","Mode":' + mode  
                    + ',"NTP":"' + ntpserver + '"}';              
            var request = relayobj.CallAjaxCustomTimeout("/manager/time" , "POST", jsonrequest, "json",60000);
            return request;
        };
        relayobj.UpdateTime = function(ntpserver){
            var jsonrequest = '{"NTP":"' + ntpserver + '"}';              
            var request = relayobj.CallAjax("/manager/time" , "PUT", jsonrequest, "json");
            return request;
        };    
        relayobj.ListSMB = function(){                    
            var request = relayobj.CallAjax("/manager/smb", "GET", "", "json");
            return request;  
        };
        relayobj.ListSSH = function(){                    
            var request = relayobj.CallAjax("/manager/ssh", "GET", "", "json");
            return request;  
        };
        relayobj.SetSSH = function(SSHFlag){                    
            var jsonrequest='{"EnableSSH":' + SSHFlag + '}';    
            // alert(jsonrequest);  
            var request = relayobj.CallAjax("/manager/ssh", "PUT", jsonrequest, "");
            return request;  
        };
        relayobj.SetSMB = function(SMBFlag){
            var jsonrequest='{"SMB":' + SMBFlag + '}';      
            // alert(jsonrequest);      
            var request = relayobj.CallAjax("/manager/smb", "PUT", jsonrequest, "");
            return request;
        };
        relayobj.InstallVM = function(InstallAddr){
            var jsonrequest='{"AccessKey":"' + AccessKey + '","Address":"' + InstallAddr + '"}';            
            var request = relayobj.CallAjax("/system/vm/install", "POST", jsonrequest, "json");
            return request;  
        };
        relayobj.RecoveryVM = function(){
            var jsonrequest='{"AccessKey":"' + AccessKey + '"}';            
            var request = relayobj.CallAjax("/system/vm/recovery", "POST", jsonrequest, "json");
            return request;  
        };
        relayobj.BackupVM = function(){
            var jsonrequest='{"AccessKey":"' + AccessKey + '"}';            
            var request = relayobj.CallAjax("/system/vm/backup", "POST", jsonrequest, "json");
            return request;  
        };
        relayobj.VMStatus = function(){
            var jsonrequest='{"AccessKey":"' + AccessKey + '"}';            
            var request = relayobj.CallAjax("/system/vm/status", "POST", jsonrequest, "json");
            return request;  
        };        
        relayobj.CPURAMInfo = function(){          
            var request = relayobj.CallAjax("/manager/system_util", "GET", "", "json");
            return request; 
        };  
        relayobj.CleanRAM = function(){            
            var request = relayobj.CallAjax("/manager/dropcache", "DELETE",  "", "");
            return request; 
        };
        relayobj.SystemInfo = function(){             
            var request = relayobj.CallAjax("/manager/system", "GET", "", "json");
            return request; 
        };   
        relayobj.ChangeVNCPWD = function(pwd){
            var jsonrequest='{"PWD":"' + pwd + '"}';            
            var request = relayobj.CallAjax("/manager/smb_pwd", "PUT", jsonrequest, "");
            return request; 
        };   
        relayobj.VMList = function(){
            var request = relayobj.CallAjaxCustomTimeout("/manager/vd", "GET", '', '', 90000);
            return request;
        };
        // 桌面設定-新增VD檔案大小實作 2017.05.03       
        relayobj.VMSizeList = function(){
            var request = relayobj.CallAjax("/manager/vd_size", "GET", "", "json");
            return request;
        };        
        relayobj.VMListImport = function(){                
            var request = relayobj.CallAjax("/manager/vd/import", "GET", "", "json");
            return request;
        };        
        relayobj.VMInfo = function(id){                 
            var request = relayobj.CallAjax("/manager/vd/"+id, "GET", '', "");
            return request;
        };
        relayobj.VMListDiskImgs = function(){
            var jsonrequest='{"AccessKey":"' + AccessKey + '"}';            
            var request = relayobj.CallAjax("/vm/listdiskimgs", "POST", jsonrequest, "json");
            return request;
        };
        relayobj.VMListIsos = function(){
            var request = relayobj.CallAjax("/manager/vd/iso", "GET", "", "json");
            return request;
        };
        relayobj.VMCreate = function(name,cpu_cores,ram_size,disk_size,disk_path,disk_type,iso_path,description,init_ram_size,usb_redirect_count,audio,nic_speed,cpu_clock,auto_suspend,vswitch){
            var jsonrequest='{'; 
            jsonrequest += '"Username":"' + name + '",';
            jsonrequest += '"VDName":"' + name + '",';
            jsonrequest += '"CpuCt":"' + cpu_cores + '",';
            jsonrequest += '"RAM":' + ram_size + ',';        
            jsonrequest += '"Disk":' + disk_size + ',';
            // jsonrequest += '"BootDiskPath":"' + disk_path + '",';
            // jsonrequest += '"BootDiskType":' + disk_type + ',';
            jsonrequest += '"CDRom":"' + iso_path + '",';
            jsonrequest += '"Desc":"' + description + '",';
            jsonrequest += '"USBRedirCt":' + usb_redirect_count + ',';
            jsonrequest += '"NIC":' + nic_speed + ',';
            jsonrequest += '"Vswitch":' + vswitch + ',';
            jsonrequest += '"CPU":"' + cpu_clock +  '",';
            jsonrequest += '"Sound":"' + audio +  '",';
            jsonrequest += '"Suspend":' + auto_suspend 
            jsonrequest += '}';      
            // alert(jsonrequest);
            var request = relayobj.CallAjax("/manager/vd", "POST", jsonrequest, "");
            return request;
        };
        relayobj.StartVM = function(namearr){
            var jsonrequest='[';
            $.each(namearr,function(index,element){
                if(index !== 0)
                    jsonrequest += ',';
                jsonrequest += '"' + element + '"';
            });
            jsonrequest += ']';             
            var request = relayobj.CallAjax("/manager/vd/poweron", "POST", jsonrequest, "");
            return request;
        };
        relayobj.ShutdownVM = function(namearr){
            var jsonrequest='[';   
            $.each(namearr,function(index,element){
                if(index !== 0)
                    jsonrequest += ',';
                jsonrequest += '"' + element + '"';
            });
            jsonrequest += ']'; 
            var request = relayobj.CallAjax("/manager/vd/shutdown", "POST", jsonrequest, "");
            return request;
        };
        relayobj.CloseVM = function(namearr){
            var jsonrequest='[';   
            $.each(namearr,function(index,element){
                if(index !== 0)
                    jsonrequest += ',';
                jsonrequest += '"' + element + '"';
            });
            jsonrequest += ']'; 
            var request = relayobj.CallAjax("/manager/vd/poweroff", "POST", jsonrequest, "");
            return request;
        };
        relayobj.SuspendVM = function(sets){
            var jsonrequest='[';   
            for(var i=0;i<sets.length;i++){
                if(i !== 0)
                    jsonrequest += ',';
                jsonrequest += '{"VDID":"' + sets[i].VDID + '"';
                jsonrequest += ',"Suspend":' + sets[i].Suspend + '}';
            }
            jsonrequest += ']'; 
            // alert(jsonrequest);
            var request = relayobj.CallAjax("/manager/vd/suspend", "POST", jsonrequest, "");
            return request;
        };
        relayobj.ListSuspendSetting = function(){
            var request = relayobj.CallAjax("/manager/vd/suspend", "GET", "", "json");
            return request;
        };
        relayobj.SetSuspendSetting = function(mins){
            var jsonrequest='{"Suspend":' + mins + '}';                
            var request = relayobj.CallAjax("/manager/vd/suspend", "PUT", jsonrequest, "");
            return request;
        };        
        relayobj.DeleteVM = function(id){
            // var jsonrequest='{"AccessKey":"' + AccessKey + '","Name":[';   
            // $.each(namearr,function(index,element){
            //     if(index !== 0)
            //         jsonrequest += ',';
            //     jsonrequest += '"' + element + '"';
            // });
            // jsonrequest += ']}';
            var request = relayobj.CallAjax("/manager/vd/"+id, "DELETE", "", "");
            return request;
        };
        relayobj.DeleteVMNIC = function(id,mac){            
            var request = relayobj.CallAjax("/manager/vd/"+id+"/nic/"+mac, "DELETE", "", "");
            return request;
        };
        relayobj.AddVMNIC = function(id,speed,vswitch){
            var jsonrequest='{"Speed":' + speed + ',"Vswitch":'+vswitch+'}';                        
            // alert(id);
            // alert(speed);
            var request = relayobj.CallAjax("/manager/vd/"+id+'/nic', "POST", jsonrequest, "json");
            return request;
        };
        relayobj.DeleteVMDisk = function(id,target){
            var request = relayobj.CallAjax("/manager/vd/" + id + "/disk/" + target, "DELETE", "", "");
            return request;
        };
        relayobj.AddVMDisk = function(id,size){
            var jsonrequest='{"Disk":' + size + '}';             
            var request = relayobj.CallAjax("/manager/vd/" + id + "/disk", "POST", jsonrequest, "");            
            return request;
        };        
        relayobj.ModifyVMInfo = function(vd_id,org_name,name,cpu,mem,cdrom,desc,usb_redirect_count,audio,cpu_clock,auto_suspend){
            var jsonrequest='{';           
            jsonrequest += '"VDName":"' + name;
            jsonrequest += '","CpuCt":' + cpu;
            jsonrequest += ',"RAM":' + mem;            
            jsonrequest += ',"CDRom":"' + cdrom;
            jsonrequest += '","Desc":"' + desc;
            jsonrequest += '","USBRedirCt":' + usb_redirect_count;
            jsonrequest += ',"Sound":' + audio;
            // jsonrequest += ',"ChangeName":' + bol_change_name;
            jsonrequest += ',"CPU":' + cpu_clock;
            jsonrequest += ',"Suspend":' + auto_suspend;
            jsonrequest += '}';                  
            var request = relayobj.CallAjax("/manager/vd/"+vd_id, "PUT", jsonrequest, "");
            return request;
        };
        relayobj.ModifyVMISO = function(vd_id,path){
            var jsonrequest='{"Path":"' + path + '"}';                 
            var request = relayobj.CallAjax("/manager/vd/"+vd_id+"/iso", "PUT", jsonrequest, "");
            return request;
        };
        relayobj.ModifyVMSeed = function(vd_id,name,desc){
            var jsonrequest='{';
            jsonrequest += '"Name":"' + name;     
            jsonrequest += '","Desc":"' + desc;
            jsonrequest += '"}';                 
            var request = relayobj.CallAjax("/manager/seed/"+vd_id, "PUT", jsonrequest, "");
            return request;            
        };
        relayobj.ResetVMPWD = function(id){
            // var jsonrequest='{"AccessKey":"' + AccessKey;
            // jsonrequest += '","Name":"' + name;         
            // jsonrequest += '"}';           
            var request = relayobj.CallAjax("/manager/vd/"+id+"/resetpwd", "PUT", '', '');
            return request;            
        };
        relayobj.EnableVM = function(namearr){
            var jsonrequest='[';
            $.each(namearr,function(index,element){
                if(index !== 0)
                    jsonrequest += ',';
                jsonrequest += '"' + element + '"';
            });
            jsonrequest += ']';            
            var request = relayobj.CallAjax("/manager/vd/enable", "PUT", jsonrequest, "");
            return request;            
        };
        relayobj.DisableVM = function(namearr){
            var jsonrequest='[';
            $.each(namearr,function(index,element){
                if(index !== 0)
                    jsonrequest += ',';
                jsonrequest += '"' + element + '"';
            });
            jsonrequest += ']';
            var request = relayobj.CallAjax("/manager/vd/disable", "PUT", jsonrequest, "");
            return request;            
        };
        relayobj.CloneVM = function(type,srcid,destnamearr,description){
            var type = parseInt(type);
            switch (type){
                case 1:
                    return MakeSeed(srcid,destnamearr,description);
                    break;
                case 2:
                    return BornUserVD(srcid,destnamearr,description);
                    break;
                default:
                    return NormalCloneVD(srcid,destnamearr,description);
                    break;
            }            
        };
        function MakeSeed(srcid,destname,desc){
            var jsonrequest='{';
                jsonrequest += '"VDName":"';
                jsonrequest += destname;
                jsonrequest += '","Desc":"';
                jsonrequest += desc;
                jsonrequest += '"}';            
            var request = relayobj.CallAjax("/manager/vd/"+srcid+"/seed", "POST", jsonrequest, "json");
            return request;
        }
        function BornUserVD(srcid,destnamearr,desc){
            var jsonrequest='{"VDs":[';
            $.each(destnamearr,function(index,element){  
                if(index !== 0)
                       jsonrequest += ',';              
                jsonrequest += '{"VDName":"' + element + '",';
                jsonrequest += '"Username":"' + element + '"}';
            });                        
            jsonrequest += '],"Desc":"';
            jsonrequest += desc;
            jsonrequest += '"}';            
            var request = relayobj.CallAjax("/manager/vd/"+srcid+"/uservd", "POST", jsonrequest, "json");
            return request;
        }
        function NormalCloneVD(srcid,destnamearr,desc){
            var jsonrequest='{"VDs":[';
            $.each(destnamearr,function(index,element){  
                if(index !== 0)
                       jsonrequest += ',';              
                jsonrequest += '{"VDName":"' + element + '",';
                jsonrequest += '"Username":"' + element + '"}';
            });                        
            jsonrequest += '],"Desc":"';
            jsonrequest += desc;
            jsonrequest += '"}';            
            var request = relayobj.CallAjax("/manager/vd/"+srcid+"/clone", "POST", jsonrequest, "json");
            return request;
        }
        relayobj.ImportVM = function(name,newname){
            var jsonrequest='{';
            jsonrequest += '"OrgName":"' + name + '",';
            jsonrequest += '"Username":"' + newname + '",';
            jsonrequest += '"NewName":"' + newname + '"';
            jsonrequest += '}';            
            var request = relayobj.CallAjax("/manager/vd/import", "POST", jsonrequest, "json");
            return request;            
        };
        relayobj.ExportVM = function(vmid){         
            var request = relayobj.CallAjax("/manager/vd/"+vmid+"/export", "PUT", "", "");
            return request;            
        };
        relayobj.VMListTask = function(){
//            console.log("Send Polling List Task"); 
            // var jsonrequest='{"AccessKey":"' + AccessKey + '"}';            
            var request = relayobj.CallAjax("/manager/task", "GET", "", "json");
            return request;
        };
        relayobj.VMClearTask = function(idarr){
            // var jsonrequest='{"AccessKey":"' + AccessKey + '","ID":[';
            // $.each(idarr,function(index,element){
            //     if(index !== 0)
            //         jsonrequest += ',';
            //     jsonrequest += '"' + element + '"';
            // });
            // jsonrequest += ']}';            
            var request = relayobj.CallAjax("/manager/task", "DELETE", "", "");
            return request;             
        };
        relayobj.iSCSIDiscovery = function(bol_chap,ip,username,passwd){
            var jsonrequest='{"AccessKey":"' + AccessKey + '"';
            jsonrequest += ',"CHAP":' + bol_chap;     
            jsonrequest += ',"IP":"' + ip + '"';
            jsonrequest += ',"Username":"' + username + '"';
            jsonrequest += ',"Password":"' + passwd + '"';
            jsonrequest += '}';     
            var request = relayobj.CallAjax("/iscsi/discovery", "POST", jsonrequest, "json");
            return request;
        };
        relayobj.iSCSILogin = function(targetname,arr_ip){
           var jsonrequest='{"AccessKey":"' + AccessKey + '","IP":[';
            $.each(arr_ip,function(index,element){
                if(index !== 0)
                    jsonrequest += ',';
                jsonrequest += '"' + element + '"';
            });
            jsonrequest += ']';
            jsonrequest += ',"TargetName":"' + targetname + '"';            
            jsonrequest += '}';                 
            var request = relayobj.CallAjaxCustomTimeout("/iscsi/login", "POST", jsonrequest, "json",60000);
            return request;
        };
        relayobj.iSCSIListSession = function(){
            var jsonrequest='{"AccessKey":"' + AccessKey + '"}';            
            var request = relayobj.CallAjax("/iscsi/session", "POST", jsonrequest, "json");
            return request;
        };
        relayobj.iSCSILogoutAll = function(){
            var jsonrequest='{"AccessKey":"' + AccessKey + '"}';            
            var request = relayobj.CallAjax("/iscsi/logout_all", "POST", jsonrequest, "json");
            return request;
        };        
        relayobj.Reboot = function(){           
            var request = relayobj.CallAjax("/manager/reboot", "PUT", "", "");
            return request;
        };
        relayobj.Shutdown = function(){
            var request = relayobj.CallAjax("/manager/shutdown", "PUT", "", "");
            return request;
        };
        relayobj.ListHostName = function(){        
            var request = relayobj.CallAjax("/manager/hostname", "GET", "", "json");
            return request;
        };
        relayobj.SetHostName = function(hostname){
            var jsonrequest='{"HostName":"' + hostname + '"';            
            jsonrequest += '}';                 
            var request = relayobj.CallAjax("/manager/hostname", "PUT", jsonrequest, "");
            return request;
        };
        relayobj.ListiSCSIIP = function(){
            var jsonrequest='{"AccessKey":"' + AccessKey + '"';            
            jsonrequest += '}';     
            var request = relayobj.CallAjax("/ip/listiscsi", "POST", jsonrequest, "json");
            return request;
        };
        relayobj.SetiSCSIIP = function(sjsonbond,sjsonip,irebootflag){
            var jsonrequest='{"AccessKey":"' + AccessKey + '",';     
            jsonrequest += '"MnagerDev":"' + IPList[0]['name'] + '",'; 
            jsonrequest += sjsonbond + ','; 
            jsonrequest += '"NIC":[' + sjsonip + '],';
            jsonrequest += '"Reboot":' +  irebootflag;
            jsonrequest += '}';                        
            var request = relayobj.CallAjax("/ip/setiscsi", "POST", jsonrequest, "json");
            return request;
        };
        relayobj.ListGlusterState = function(){        
            var request = relayobj.CallAjax("/manager/gluster", "GET", "", "json");
            return request;
        };        
        relayobj.ClearGluster = function(){        
            var request = relayobj.CallAjax("/manager/gluster", "DELETE", "", "");
            return request;
        };         
        relayobj.SetGlusterNormal = function(){        
            var request = relayobj.CallAjax("/manager/gluster/normal", "PUT", "", "");
            return request;
        };      
        relayobj.SetGlusterForceMaster = function(){        
            var request = relayobj.CallAjax("/manager/gluster/master", "PUT", "", "");
            return request;
        };     
        relayobj.CheckAnotherAdminLogin = function(){     
            var urlToken = '';
            if (typeof(Storage) !== "undefined") {
                 if (sessionStorage.token) {
                    urlToken = '?token=' + sessionStorage.token;
                 }
            }                           
            var request = relayobj.CallAjax("/manager/check" + urlToken, "GET", "", "json");
            return request;
        };              
        relayobj.CallAjaxNoAsync = function(url, method, data, datatype) {
            var request = $.ajax({
                type: method,
                url: url,
                data: data,
                dataType: datatype,
                timeout:30000,
                async: false
            });
            return request;
        };       
        relayobj.CallAjaxCustomTimeout = function(url, method, data, datatype,timeout) {
            var request = $.ajax({
                type: method,
                url: url,
                data: data,
                timeout:timeout,
                dataType: datatype
            });
            return request;
        };        
        relayobj.CallAjax = function(url, method, data, datatype) {
            var request = $.ajax({
                type: method,
                url: url,
                data: data,
                timeout:30000,
                dataType: datatype
            });
            return request;
        };
        return relayobj;
    }
};