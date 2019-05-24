var isViewjob = 0;
var idSchedule_ssarr_temp = [];

/* ----------------------------- Dialog畫面 ----------------------------- */
function CreateDialog_Autoss() {
    $("#dialog_autoss").dialog({
        title: LanguageStr_Service.Service_SS_Schedule_title2,
        modal: true,
        resizable: false,
        draggable: true,
        closeOnEscape: false,
        autoOpen: false,
        dialogClass: "no-close",
        height: 710,
        width: 800,
        // 關閉預設的視窗關閉鈕
        // open: function (event, ui) {
        //     $(".ui-dialog-titlebar-close", ui.dialog | ui).hide();
        // },
        // 預設起始的視窗顯示位置
        // position: { my: "center", at: "left+910px top+525px ", of: window  } ,                 
    });
    $('#btn_close_autoss').click(function (event) {
        event.preventDefault();
        isViewjob = 0;
        $("#dialog_autoss").dialog('close');
    });
    $('#autoss_Cancel').click(function (event) {
        event.preventDefault();
        /*
        idSchedule_ssarr_temp.push(idSchedule);
        DeleteScheduleSnapshot(idSchedule_ssarr_temp);
        $('#autoss_ListJobBottom').empty();
        */
        $("#dialog_autoss").dialog('close');
    });
    $('#autoss_Confirm').click(function (event) {
        event.preventDefault();
        var name = $('#cs_name_input').val();
        var desc = $('#cs_desc_input').val();
        var isenable = true;
        var type;
        var interval;
        var relativeinterval = 0;
        var recurrencefactor = 0;
        var startdate = GetDateValue($('#datepicker_schedule').val());
        var starttime = GetTimeValue($('#timepicker_schedule').val());
        var ChoiceWeekValue = 0;
        var ChoiceWeekValue2 = 0;
        var ChoiceMonthValue = 0;
        var ChoiceMonthValue2 = 0;
        var ChoiceMonthRValue = 0;
        var ChoiceMonthRValue2 = 0;
        var ChoiceWeekRValue = 0;
        var ChoiceWeekRValue2 = 0;       
        desc = desc.replace(/"/g, '\\"');
        type = $('input[name=radMethod]:checked', '#st_table').val();
        // $('#st_table input').on('change', function() {
        //     alert($('input[name=radMethod]:checked', '#st_table').val()); 
        // });
        if (type == 4) {
            interval = 1;
        }
        else if (type == 8) {
            $('input[name="user_active_Week"]:checked').each(function () {
                ChoiceWeekValue = this.value;
                ChoiceWeekValue = parseInt(ChoiceWeekValue);
                ChoiceWeekValue2 += ChoiceWeekValue;
            });
            interval = ChoiceWeekValue2;
            recurrencefactor = 1;
        }
        else if (type == 16) {
            $('input[name="user_active_Month"]:checked').each(function () {
                ChoiceMonthValue = this.value;
                ChoiceMonthValue = parseInt(ChoiceMonthValue);
                ChoiceMonthValue2 += ChoiceMonthValue;
            });
            interval = ChoiceMonthValue2;
            recurrencefactor = 1;
        }
        else if (type == 32) {
            $('input[name="user_active_MR"]:checked').each(function () {
                ChoiceMonthRValue = this.value;
                ChoiceMonthRValue = parseInt(ChoiceMonthRValue);
                ChoiceMonthRValue2 += ChoiceMonthRValue;
            });
            relativeinterval = ChoiceMonthRValue2;
            $('input[name="user_active_WR"]:checked').each(function () {
                ChoiceWeekRValue = this.value;
                ChoiceWeekRValue = parseInt(ChoiceWeekRValue);
                ChoiceWeekRValue2 += ChoiceWeekRValue;
            });
            interval = ChoiceWeekRValue2;
            recurrencefactor = 1;
        }         
        CreateScheduleSnapshot(name, desc, isenable, type, interval, relativeinterval, recurrencefactor, startdate, starttime);
        // InitCreateScheduleJobButton(idSchedule, pname, pdesc);
    });
    $('#autoss_Previous').click(function (event) {
        event.preventDefault();
        $("#dialog_autoss").dialog('close');  
        $("#dialog_create_schedule").dialog('open');        
    });    
    $('#autoss_Modify').click(function (event) {
        event.preventDefault();
        ModifySnapshotScheduleJob();
    });    
    

}
/**************************************************************************/
/*                                                                        */
/*                               產生排程相關畫面                           */
/*                                                                        */
/**************************************************************************/
/*=================== 排程Jobs (1)建立清單 2017.02.10 Start ===================*/ 
function GetVDsForCreateTableList(isViewjob, jobVds) {
    var request = Ajax_ListVDs_SendCommand();
    request.done(function (AjaxData, statustext, jqXHR) {
        ListVDsToUI(AjaxData, isViewjob, jobVds);
    });
    request.fail(function (jqXHR, textStatus) {

    });
}
// 定義class承接json資料
function ListVDsToUI(JsonData, isViewjob, jobVds) {
    var HtmlStr = "";
    var vdjob;
    VDStandbyJobsList = [];
    VDForSnapshotJobsList = [];
    // $.each( JsonData['Org'], function(index, element){                 
    //     vdjob = new VDStandbyJobsVMClass(element['VDID'],element['UserID'],element['VDName'],element['RAM'],element['Suspend'],element['Desc'],element["Online"],element['State'],element['CreateTime'],element['ModfiyTime']);                   
    //     VDStandbyJobsList[element['VDID']]= vdjob;            
    // });                      
    if (isViewjob == 1) {      
        var tmpArr = [];
        var Flag = false;
        $.each(JsonData['Org'], function (index, element) {
            vdjob = new VDStandbyJobsVMClass(element['VDID'], element['UserID'], element['VDName'], element['RAM'], element['Suspend'], element['Desc'], element["Online"], element['State'], element['CreateTime'], element['ModfiyTime'], element['Username']);
            tmpArr[element['VDID']] = vdjob;
                
            for (var key in jobVds) {
                if (jobVds[key] === element['VDID']) {
                    Flag = true;
                }
            }   

            if(!Flag) {
                VDStandbyJobsList[element['VDID']] = tmpArr[element['VDID']];     
                Flag = false;       
            } else {
                Flag = false;       
            }                
        });
        $.each(jobVds, function (index, element) {
            if (typeof (tmpArr[element]) !== 'undefined') 
                VDForSnapshotJobsList[element] = tmpArr[element];
        });
        InitAddToJobBtn();
        InitAddToStandbyBtn();        
        CreateListVDStandbyJobsHtml();
        CreateListScheduleJobsHtml();
        // $('#autoss_Cancel').button("disable");
        // $('#autoss_Confirm').button("disable");
        $('#btn_close_autoss').button("enable");
        $('.iCheckBoxForwardbackSelect').iCheck({
            checkboxClass: 'icheckbox_minimal-blue'
        });
    }
    else {
        $.each(JsonData['Org'], function (index, element) {
            vdjob = new VDStandbyJobsVMClass(element['VDID'], element['UserID'], element['VDName'], element['RAM'], element['Suspend'], element['Desc'], element["Online"], element['State'], element['CreateTime'], element['ModfiyTime'], element['Username']);
            VDStandbyJobsList[element['VDID']] = vdjob;
        });   
        InitAddToJobBtn();
        InitAddToStandbyBtn();
        CreateListVDStandbyJobsHtml();
        $('#autoss_Cancel').button("enable");
        $('#autoss_Confirm').button("enable");
        $('#btn_close_autoss').button("disable");
        $('.iCheckBoxStandbySelect').iCheck({
            checkboxClass: 'icheckbox_minimal-blue'
        });
        $('.iCheckBoxForwardbackSelect').iCheck({
            checkboxClass: 'icheckbox_minimal-blue'
        });
    }
}
// 將VD相關資料List出來
function CreateListVDStandbyJobsHtml() {
    var HtmlStr = "";
    var no = 1;
    for (var key in VDStandbyJobsList) {
        HtmlStr += '<tr class="border_bottom">';
        HtmlStr += '<td id="ss_wrapper_top_bottom_Item" class="autoss_bottom0">';
        HtmlStr += '<label>' + no + '</label>';
        HtmlStr += '</td>';
        HtmlStr += '<td id="ss_top_bottom_chk" class="autoss_top_bottomchk">';
        // HtmlStr += '<input type="checkbox" value="" name="top_select" id="'+ element['userid'] +'" class="css-checkbox">';
        // HtmlStr += '<label for="top_select" class="css-label" style="margin: 0.5px;"></label>';
        HtmlStr += '<input type="checkbox" id="' + VDStandbyJobsList[key].vdid + '" class="iCheckBoxStandbySelect">';
        HtmlStr += '</td>';
        HtmlStr += '<td id="ss_wrapper_top_bottom_Owner" class="autoss_bottom2" style="text-align: left;">';
        HtmlStr += ' <label style=" width: 105px; display: block; text-overflow: ellipsis; overflow:hidden; white-space:nowrap; ">' + VDStandbyJobsList[key].username + '</label>';
        HtmlStr += '</td>';
        HtmlStr += '<td id="ss_wrapper_top_bottom_VDName" class="autoss_bottom3" style="text-align: left;">';
        HtmlStr += ' <label style=" width: 105px; display: block; text-overflow: ellipsis; overflow:hidden; white-space:nowrap; ">' + VDStandbyJobsList[key].vdname + '</label>';
        HtmlStr += '</td>';
        HtmlStr += '<td id="ss_wrapper_top_bottom_VDStatus" class="autoss_bottom4" style="text-align: left;">';
        // HtmlStr += ' <label>' + element['state'] + '</label>';
        HtmlStr += GetVMStatus(VDStandbyJobsList[key].state, VDStandbyJobsList[key].online);
        HtmlStr += '</td>';
        HtmlStr += '<td id="ss_wrapper_top_bottom_Remark" class="last_column autoss_bottom5" style="text-align: left;">';
        HtmlStr += ' <label>' + VDStandbyJobsList[key].desc + '</label>';
        HtmlStr += '</td>';
        HtmlStr += '</tr>';

        no++;
    }

    $('#autoss_ListBottom').empty();
    $('#autoss_ListBottom').append(HtmlStr);
    $('.iCheckBoxStandbySelect').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });
    $('.iCheckBoxAllStandbyJobsSelect').iCheck('uncheck');
    $('.iCheckBoxStandbySelect').iCheck('uncheck');
};

function CreateListScheduleJobsHtml() {
    var HtmlStr = "";
    var no = 1;
    for (var key in VDForSnapshotJobsList) {
        HtmlStr += '<tr class="border_bottom">';
        HtmlStr += '<td id="ss_wrapper_bottom_bottom_Item" class="autoss_bottom0">';
        HtmlStr += '<label>' + no + '</label>';
        HtmlStr += '</td>';
        HtmlStr += '<td id="ss_bottom_chk" class="ss_bottom_chk">';
        // HtmlStr += '<input type="checkbox" value="" name="bottom_select" id="bottom_select" class="css-checkbox">';
        // HtmlStr += '<label for="bottom_select" class="css-label" style="margin: 0.6px;"></label>';
        HtmlStr += '<input type="checkbox" id="' + VDForSnapshotJobsList[key].vdid + '" class="iCheckBoxForwardbackSelect">';
        HtmlStr += '</td>';
        HtmlStr += '<td id="ss_wrapper_bottom_bottom_Owner" class="autoss_bottom2" style="text-align: left;">';
        HtmlStr += ' <label style=" width: 105px; display: block; text-overflow: ellipsis; overflow:hidden; white-space:nowrap; ">' + VDForSnapshotJobsList[key].username + '</label>';
        HtmlStr += '</td>';
        HtmlStr += '<td id="ss_wrapper_bottom_bottom_VDNam" class="autoss_bottom3" style="text-align: left;">';
        HtmlStr += ' <label style=" width: 105px; display: block; text-overflow: ellipsis; overflow:hidden; white-space:nowrap; ">' + VDForSnapshotJobsList[key].vdname + '</label>';
        HtmlStr += '</td>';
        HtmlStr += '<td id="ss_wrapper_bottom_bottom_VDStatus" class="autoss_bottom4" style="text-align: left;">';
        HtmlStr += GetVMStatus(VDForSnapshotJobsList[key].state, VDForSnapshotJobsList[key].online);
        HtmlStr += '</td>';
        HtmlStr += '<td id="ss_wrapper_bottom_bottom_Remark" class="last_column autoss_bottom5" style="text-align: left;">';
        HtmlStr += ' <label>' + VDForSnapshotJobsList[key].desc + '</label>';
        HtmlStr += '</td>';
        HtmlStr += '</tr>';

        no++;

    }

    $('#autoss_ListJobBottom').empty();
    $('#autoss_ListJobBottom').append(HtmlStr);
    $('.iCheckBoxForwardbackSelect').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });
    $('.iCheckBoxAllForwardbackJobsSelect').iCheck('uncheck');
    $('.iCheckBoxForwardbackSelect').iCheck('uncheck');

};

function InitAddToJobBtn() {
    $('#autoss_AddToJob').click(function (event) {
        event.preventDefault();
        $.each($('.iCheckBoxStandbySelect:checked'), function (index, element) {
            var id = $(element).attr('id');
            var tmpVD = VDStandbyJobsList[id];
            VDForSnapshotJobsList[tmpVD['vdid']] = tmpVD;
            delete VDStandbyJobsList[id];
        });
        CreateListVDStandbyJobsHtml(); // 新增 by william
        CreateListScheduleJobsHtml(); // 新增 by william  
    });
}

function InitAddToStandbyBtn() {
    $('#autoss_AddToStandby').click(function (event) {
        event.preventDefault();
        $.each($('.iCheckBoxForwardbackSelect:checked'), function (index, element) {
            var id = $(element).attr('id');
            var tmpVD = VDForSnapshotJobsList[id];
            VDStandbyJobsList[tmpVD['vdid']] = tmpVD;
            delete VDForSnapshotJobsList[id];
        });
        CreateListVDStandbyJobsHtml(); // 新增 by william
        CreateListScheduleJobsHtml(); // 新增 by william  
    });
}

/*=================== 排程Jobs (1)建立清單 2017.02.10 End ===================*/
/*=================== 排程Jobs (2)建立 2017.02.10 Start ===================*/
// 建立排程按鈕實作 2017.02.13 william         
function InitCreateScheduleJobButton(idSchedule, pname, pdesc) {
    var jobtype = 1;    // Job Type類型: 1. 快照, 2. 備份
    var name = pname;
    var desc = pdesc;
    var vds = '';
    var i = 0;
    $('#autoss_ListJobBottom').empty();
    for (var key in VDForSnapshotJobsList) {
        if (i != 0)
            vds += ',';
        vds += '"' + VDForSnapshotJobsList[key].vdid + '"';
        i++
    }

    CreateScheduleJobSnapshot(idSchedule, name, jobtype, desc, vds);
}

function CreateScheduleJobSnapshot(idSchedule, name, jobtype, desc, vds) {
    var request = Ajax_CreateJob(idSchedule, name, jobtype, desc, vds);
    CallBackCreateScheduleJobSnapshot(request);
};

function CallBackCreateScheduleJobSnapshot(request) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        GetScheduleForCreateTableList();
        $("#dialog_autoss").dialog('close');
    });
    request.fail(function (jqxhr, textStatus) {

    });
}
/*=================== 排程Jobs (2)建立 2017.02.10 End ===================*/      
// ----------- 排程Jobs (3)Job清單 2017.02.10 Start -----------  

function GetJobsForCreateTableList(idJob) {
    var request = Ajax_ListJobs_SendCommand(idJob);
    var jobVds = '';
    request.done(function (AjaxData, statustext, jqXHR) {
        isViewjob = 1;
        jobVds = AjaxData.VDs;
        GetVDsForCreateTableList(isViewjob, jobVds);
    });
    request.fail(function (jqXHR, textStatus) {

    });
}
// ----------- 排程Jobs (3)Job清單 2017.02.10 End -----------       

function ModifySnapshotScheduleJob() {
    var jobtype = 1;    // Job Type類型: 1. 快照, 2. 備份
    var vds = '';
    var i = 0;
    var id = JobsID;

    for (var key in VDForSnapshotJobsList) {
        if (i != 0)
            vds += ',';
        vds += '"' + VDForSnapshotJobsList[key].vdid + '"';
        i++
    }
    
    var request = Ajax_ModifyJobs(id, vds);
    CallBackModifySnapshotScheduleJob(request);
}

function CallBackModifySnapshotScheduleJob(request) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        GetScheduleForCreateTableList();
        $("#dialog_autoss").dialog('close');
    });
    request.fail(function (jqxhr, textStatus) {

    });
}


/**************************************************************************/
/*                                                                        */
/*                           Relay Ajax Rule建立                           */
/*                                                                        */
/**************************************************************************/
// 1. 取得全部 VD 資訊
function Ajax_ListVDs_SendCommand() {
    var request = CallAjax("/manager/vd", "", "json", "GET");
    return request;
}
// 2. 建立排程的Job 
function Ajax_CreateJob(id_Schedule, name, jobtype, description, vds) {
    var jsonrequest = '{';
    jsonrequest += '"Name":"' + name + '",';
    jsonrequest += '"JobType":' + jobtype + ',';
    jsonrequest += '"Desc":"' + description + '",';
    jsonrequest += '"VDs":[' + vds + ']';
    jsonrequest += '}';
    var request = CallAjax("manager/schedule/" + id_Schedule + "/job", jsonrequest, "", "POST");
    return request;
};
// 3. 取得全部 Job 清單資訊
function Ajax_ListJobs_SendCommand(id_Job) {
    var request = CallAjax("manager/job/" + id_Job, "", "json", "GET");
    return request;
}
// 4. Modfiy Job
function Ajax_ModifyJobs(id_Job, vds) {
    var jsonrequest = '[';
    jsonrequest += vds;
    jsonrequest += ']';    
    var request = CallAjax("manager/job/" + id_Job, jsonrequest, "json", "PUT");
    return request;
}

/* ----------------------------- 功能 ----------------------------- */
// 1. 判斷state狀態並作文字轉換
function GetVMStatus(Status, Online) {
    switch (Status) {
        case -5:
            return '<label style="color:red">Crash</label>';
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
            return '<label style="color:green">' + LanguageStr_VM.VM_Poweron + '</label>';
            break;
        case 2:
            return LanguageStr_VM.VM_Blocked;
            break;
        case 3:
            return '<label style="color:black">' + LanguageStr_VM.VM_Pause + '</label>';
            break;
        case 4:
            return '<label style="color:#044de1">' + LanguageStr_VM.VM_Shutdowning + '</label>';
            break;
        case 5:
            return '<label style="color:#044de1">' + LanguageStr_VM.VM_Shutdown + '</label>';
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

function InitJobSelectAllCheckBox() {
    $('.iCheckBoxAllStandbyJobsSelect').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });
    $('.iCheckBoxAllForwardbackJobsSelect').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });

    $('.iCheckBoxAllStandbyJobsSelect').on('ifChanged', function (event) {
        var element = $(this);
        if (element.prop('checked')) {
            $('.iCheckBoxStandbySelect').iCheck('check');
        }
        else {
            $('.iCheckBoxStandbySelect').iCheck('uncheck');
        }
    });
    $('.iCheckBoxAllForwardbackJobsSelect').on('ifChanged', function (event) {
        var element = $(this);
        if (element.prop('checked')) {
            $('.iCheckBoxForwardbackSelect').iCheck('check');
        }
        else {
            $('.iCheckBoxForwardbackSelect').iCheck('uncheck');
        }
    });
}