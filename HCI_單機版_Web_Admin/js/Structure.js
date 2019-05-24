var relaySourceClass = function(id,flag,url,prior)
{
    this.id = id;
    this.flag = flag;
    this.url = url;
    this.prior = prior;
};
var relayClass = function(id, source, port, destname,channelnumber,name,description,fulldest,status) {
    this.id = id;
    this.source = source;
    this.port = port;
    this.destname = destname;
    this.name = name;
    this.channelnumber = channelnumber;
    this.description = description;
    this.fulldest = fulldest;
    this.status = status;
};
var LogClass = function(type,operator,channelnumber,source,fulldest,name,time,description){
    this.type = type;
    this.operator = operator;
    this.channelnumber = channelnumber;
    this.source = source;
    this.fulldest = fulldest;
    this.name = name;
    this.time = time;
    this.description = description;
};
var IPClass = function(id, displayname, name, ip, mask,bridge,gateway) {
    this.id = id;
    this.displayname = displayname;
    this.name = name;
    this.ip = ip;
    this.mask = mask;
    this.bridge = bridge;
    this.gateway = gateway;
};
var iSCSIIPClass = function(id, displayname) {
    this.id = id;
    this.displayname = displayname;  
};
var GatewayClass = function(id, ip, bindport) {
    this.id = id;
    this.ip = ip;
    this.bindport = bindport;
};
var DNSClass = function(id, ip) {
    this.id = id;
    this.ip = ip;
};
var VMClass = function(username,name,cpu_util,mem_util,state,seed,seed_src,desc,type,create_time,online,state_display,suspend,ram,vdid,allsize){
    this.username = username;
    this.name = name;
    this.cpu_util = cpu_util;
    this.mem_util = mem_util;
    this.state = state;
    this.seed = seed;
    this.seed_src = seed_src;
    this.desc = desc;
    this.type = type;
    this.create_time = create_time;
    this.online = online;
    this.state_display = state_display;
    this.suspend = suspend;
    this.ram = ram;
    this.vdid = vdid;
    this.allsize = allsize; // 桌面設定-新增VD檔案大小實作 2017.05.03       
};
var VMSeedClass = function(name,vdi_count,desc,state,create_time,ram,vdid,allsize){
    this.name = name;
    this.vdi_count = vdi_count;
    this.desc = desc;
    this.state = state;
    this.create_time = create_time;
    this.ram = ram;
    this.vdid = vdid;
    this.allsize = allsize; // 桌面設定-新增VD檔案大小實作 2017.05.03           
};
var VMCloneClass = function(id,type,srcDomName,destDomName,process,status,start_time,end_time){
    this.id = id;
    this.type = type;
    this.srcDomName = srcDomName;
    this.destDomName = destDomName;
    this.process = process;
    this.status = status;
    // if(start_time == '')
    //     this.start_time = '9999/12/31 23:59:59'
    // else
        this.start_time = start_time;
    // this.start_time_display = start_time;
    this.end_time = end_time;
};
var DiskClass = function(postion,status,capacity,vendor,if_type,model,rev,dev_id){
    this.postion = postion;
    this.status = status;
    this.capacity = capacity;
    this.vendor = vendor;
    this.if_type = if_type;
    this.model = model;
    this.rev = rev;
    this.dev_id = dev_id;
};

/* Schedule類別實作  2017.01.11 william */
var ScheduleClass = function(index,ScheduleID,Name,Desc,isEnable,FreqType,FreqInterval,FreqRelativeInterval,FreqRecurrenceFactor,StartDate,StartTime,CreateTime) {
    this.index = index;
    this.ScheduleID = ScheduleID;
    this.Name = Name;
    this.Desc = Desc;
    this.isEnable = isEnable;
    this.FreqType = FreqType;
    this.FreqInterval = FreqInterval;
    this.FreqRelativeInterval = FreqRelativeInterval;
    this.FreqRecurrenceFactor = FreqRecurrenceFactor;
    this.StartDate = StartDate;
    this.StartTime = StartTime;    
    this.CreateTime = CreateTime;
    
    this.Jobs = new Array();    
};
/* Schedule的job類別實作  2017.01.11 william */
var Job_class = function (JobID,Name,Desc,JobType) {
    this.JobID = JobID;
    this.Name = Name;
    this.Desc = Desc;
    this.JobType = JobType;
}
/* 快照類別實作  2017.01.11 william */
var SnapShotClass = function(Layer,UpperLayer,CreateTime,Desc,State,Size) {
    this.Layer = Layer;
    this.UpperLayer = UpperLayer;
    this.CreateTime = CreateTime;
    this.Desc = Desc;
    this.State = State;
    this.Size = Size;
};
/* 快照任務類別實作  2017.01.11 william */
var VMSnapShotCloneClass = function(id,state,type,vdname,layerdesc,layerdate,start_time,end_time,process,errorcode){
    this.id = id;
    this.state = state;
    this.type = type;
    this.vdname = vdname;
    this.layerdesc = layerdesc;
    this.layerdate = layerdate;
    this.start_time = start_time;
    this.end_time = end_time;
    this.process = process;
    this.errorcode = errorcode;
};
// 新增排程VM(Jobs)的class 2017.02.15 william
var VDStandbyJobsVMClass = function(vdid,userid,vdname,ram,suspend,desc,online,state,create_time,modify_time, username) {
    this.vdid = vdid;
    this.userid = userid;
    this.vdname = vdname;
    this.ram = ram;
    this.suspend = suspend;
    this.desc = desc;
    this.online = online;
    this.state = state;
    this.create_time = create_time;
    this.modify_time = modify_time;
    this.username = username;
};
// 新增備份(List)的class 2017.02.21 william
var ListBackupClass = function(chap,targetname,username,password,desc,bkuuid,ip,id,totalcapacity,avialcapacity,online) {
    this.chap = chap;
    this.targetname = targetname;
    this.username = username;
    this.password = password;
    this.desc = desc;
    this.bkuuid = bkuuid;
    this.ip = ip;
    this.id = id;
    this.totalcapacity = totalcapacity;
    this.avialcapacity = avialcapacity;
    this.online = online;
};
// 新增備份(List->RestoreVDs)的class 2017.02.21 william
var RestoreVDsClass = function(SourceVDName,SourceVDID,BackupVDName,BackupVDID,BackupTime,TotalSize,State) {
    this.SourceVDName = SourceVDName;
    this.SourceVDID = SourceVDID;
    this.BackupVDName = BackupVDName;
    this.BackupVDID = BackupVDID;
    this.BackupTime = BackupTime;
    this.TotalSize = TotalSize;    
    this.State = State;        
};
// 新增備份VM(backup)的class 2017.02.23 william
var VDBackupMgtClass = function(vdid,userid,vdname,ram,suspend,desc,domainid,allsize,create_time,modify_time,recovery_time,state) {
    this.vdid = vdid;
    this.userid = userid;
    this.vdname = vdname;
    this.ram = ram;
    this.suspend = suspend;
    this.desc = desc;
    this.domainid = domainid;
    this.allsize = allsize;
    this.create_time = create_time;
    this.modify_time = modify_time;
    this.recovery_time = recovery_time;
    this.state = state;
};
var VDBackupMgtBothListClass = function(vdid,vdname,desc,recovery_time,create_time,vdallsize,vdstate,bkvdid,bkvdname,bktime,bksize,bkstate) {
    this.vdid = vdid;
    this.vdname = vdname;
    this.desc = desc;
    this.recovery_time = recovery_time;
    this.create_time = create_time;
    this.vdallsize = vdallsize;   
    this.vdstate = vdstate;   
    this.bkvdid = bkvdid;
    this.bkvdname = bkvdname;
    this.bktime = bktime;
    this.bksize = bksize;    
    this.bkstate = bkstate;       
};
var VDBackupMgtSRListClass = function(vdid,vdname,desc,recovery_time,create_time,vdallsize,vdstate,bkvdid,bkvdname,bktime,bksize,bkstate) {
    this.vdid = vdid;
    this.vdname = vdname;
    this.desc = desc;
    this.recovery_time = recovery_time;
    this.create_time = create_time;    
    this.vdallsize = vdallsize;   
    this.vdstate = vdstate;   
    this.bkvdid = bkvdid;
    this.bkvdname = bkvdname;
    this.bktime = bktime;
    this.bksize = bksize;    
    this.bkstate = bkstate;    
};
var VDBackupMgtBKListClass = function(vdid,vdname,desc,recovery_time,create_time,vdallsize,vdstate,bkvdid,bkvdname,bktime,bksize,bkstate) {
    this.vdid = vdid;
    this.vdname = vdname;
    this.desc = desc;
    this.recovery_time = recovery_time;
    this.create_time = create_time; 
    this.vdallsize = vdallsize;   
    this.vdstate = vdstate;   
    this.bkvdid = bkvdid;
    this.bkvdname = bkvdname;
    this.bktime = bktime;
    this.bksize = bksize;    
    this.bkstate = bkstate;      
};
/* 備份任務類別實作  2017.03.03 william */
var VMBackupCloneClass = function(id,state,type,source,dest,start_time,end_time,process){
    this.id = id;
    this.state = state;
    this.type = type;
    this.source = source;
    this.dest = dest;
    this.start_time = start_time;
    this.end_time = end_time;
    this.process = process;
};   
 // vSwitch UI介面功能實作 2017.01.23            
var NICClass = function(name,indexSetVswitch){
    this.name = name;
    this.indexSetVswitch = indexSetVswitch;
}

 // 新增外部IP及轉址功能設定  2017.09.05 william
var EXIPListClass = function(ip,port,portcount){
    this.ip = ip;
    this.port = port;
    this.portcount = portcount;
}

var UserClass = function(id,name,isadmin,state) {
    this.id = id;
    this.name = name;
    this.isadmin = isadmin;
    this.state = state;  
};

var UserDetailClass = function(id,disksize,vdname,vdid,disktype, raidid, state) {
    this.id = id;
    this.disksize = disksize;
    this.vdname = vdname;
    this.vdid = vdid;  
    this.disktype = disktype;  
    this.raidid = raidid;  
    this.state = state;
};

var GetTextareaUserNameClass = function(vdname,username) {
    this.vdname = vdname;
    this.username = username;    
};

var RAIDRestoreClass = function(username, vdname, sourcevdid, backupvdid, backupvdname) {
    this.username = username;
    this.vdname = vdname;    
    this.sourcevdid = sourcevdid;
    this.backupvdid = backupvdid;       
    this.backupvdname = backupvdname;   
};

var WaitRAIDRestoreClass = function(raidid) {   
    this.raidid = raidid;   
};

var CephClass = function(raidid, total, used, usage) {   
    this.raidid = raidid;   
    this.total = total;
    this.used = used;
    this.usage = usage;
};

var CephForPerformanceClass = function(raidid, total, used, usage) {   
    this.raidid = raidid;   
    this.total = total;
    this.used = used;
    this.usage = usage;
};