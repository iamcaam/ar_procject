var isViewjob = 0;
var idSchedule_bkarr_temp = [];

/* ----------------------------- Dialog畫面 ----------------------------- */
function CreateDialog_autobk() {
    $("#dialog_autobk").dialog({
        title: LanguageStr_Service.Service_BK_Schedule_title2,
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
    $('#btn_close_autobk').click(function (event) {
        event.preventDefault();
        isViewjob = 0;
        $("#dialog_autobk").dialog('close');
    });
    $('#autobk_Cancel').click(function (event) {
        event.preventDefault();
        /*
        idSchedule_bkarr_temp.push(idSchedule);
        DeleteScheduleBackup(idSchedule_bkarr_temp);
        $('#autobk_ListJobBottom').empty();
        */
        $("#dialog_autobk").dialog('close');
    });
    $('#autobk_Confirm').click(function (event) {
        event.preventDefault();
        var name = $('#csbk_name_input').val();
        var desc = $('#csbk_desc_input').val();
        var isenable = true;
        var type;
        var interval;
        var relativeinterval = 0;
        var recurrencefactor = 0;
        var startdate = GetDateValue($('#datepicker_schedulebk').val());
        var starttime = GetTimeValue($('#timepicker_schedulebk').val());
        var ChoiceWeekValue = 0;
        var ChoiceWeekValue2 = 0;
        var ChoiceMonthValue = 0;
        var ChoiceMonthValue2 = 0;
        var ChoiceMonthRValue = 0;
        var ChoiceMonthRValue2 = 0;
        var ChoiceWeekRValue = 0;
        var ChoiceWeekRValue2 = 0;  

        desc = desc.replace(/"/g, '\\"');
        type = $('input[name=radMethodbk]:checked', '#st_bktable').val();

        if (type == 4) {
            interval = 1;
        }
        else if (type == 8) {
            $('input[name="user_active_Weekbk"]:checked').each(function () {
                ChoiceWeekValue = this.value;
                ChoiceWeekValue = parseInt(ChoiceWeekValue);
                ChoiceWeekValue2 += ChoiceWeekValue;
            });
            interval = ChoiceWeekValue2;
            recurrencefactor = 1;
        }
        else if (type == 16) {
            $('input[name="user_active_Monthbk"]:checked').each(function () {
                ChoiceMonthValue = this.value;
                ChoiceMonthValue = parseInt(ChoiceMonthValue);
                ChoiceMonthValue2 += ChoiceMonthValue;
            });
            interval = ChoiceMonthValue2;
            recurrencefactor = 1;
        }
        else if (type == 32) {
            $('input[name="user_active_MRbk"]:checked').each(function () {
                ChoiceMonthRValue = this.value;
                ChoiceMonthRValue = parseInt(ChoiceMonthRValue);
                ChoiceMonthRValue2 += ChoiceMonthRValue;
            });
            relativeinterval = ChoiceMonthRValue2;
            $('input[name="user_active_WRbk"]:checked').each(function () {
                ChoiceWeekRValue = this.value;
                ChoiceWeekRValue = parseInt(ChoiceWeekRValue);
                ChoiceWeekRValue2 += ChoiceWeekRValue;
            });
            interval = ChoiceWeekRValue2;
            recurrencefactor = 1;
        }        
        CreateScheduleBackup(name, desc, isenable, type, interval, relativeinterval, recurrencefactor, startdate, starttime);
        // InitCreateScheduleJobBackupButton(idSchedule, pname, pdesc);
    });
    $('#autobk_Previous').click(function (event) {
        event.preventDefault();
        $("#dialog_autobk").dialog('close');  
        $("#dialog_create_bkschedule").dialog('open');        
    });       
    $('#autobk_Modify').click(function (event) {
        event.preventDefault();
        ModifyBackupScheduleJob();
    });       

}
/**************************************************************************/
/*                                                                        */
/*                               產生排程相關畫面                           */
/*                                                                        */
/**************************************************************************/
/*=================== 排程Jobs (1)建立清單 2017.02.10 Start ===================*/
function GetVDsForCreateBackupTableList(isViewjob, jobVds) {
    var request = Ajax_ListVDs_SendCommand();
    request.done(function (AjaxData, statustext, jqXHR) {
        ListVDsToJobBackupUI(AjaxData, isViewjob, jobVds);
    });
    request.fail(function (jqXHR, textStatus) {

    });
}
// 定義class承接json資料
function ListVDsToJobBackupUI(JsonData, isViewjob, jobVds) {
    var HtmlStr = "";
    var vdjob;
    VDStandbyJobsList = [];
    VDForBackupJobsList = [];               
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
                VDForBackupJobsList[element] = tmpArr[element];
        });
        InitAddToJobBackupBtn();
        InitAddToStandbyBackupBtn();
        CreateListVDStandbyJobsBackupHtml();        
        CreateListScheduleJobsBackupHtml();
        // $('#autobk_Cancel').button("disable");
        // $('#autobk_Confirm').button("disable");            
        $('#btn_close_autobk').button("enable");
        $('.iCheckBoxBackupForwardbackSelect1').iCheck({
            checkboxClass: 'icheckbox_minimal-blue'
        });
    }
    else {
        $.each(JsonData['Org'], function (index, element) {
            vdjob = new VDStandbyJobsVMClass(element['VDID'], element['UserID'], element['VDName'], element['RAM'], element['Suspend'], element['Desc'], element["Online"], element['State'], element['CreateTime'], element['ModfiyTime'], element['Username']);
            VDStandbyJobsList[element['VDID']] = vdjob;
        });
        InitAddToJobBackupBtn();
        InitAddToStandbyBackupBtn();
        CreateListVDStandbyJobsBackupHtml();
        $('#autobk_Cancel').button("enable");
        $('#autobk_Confirm').button("enable");
        $('#btn_close_autobk').button("disable");
        $('.iCheckBoxStandbySelect1').iCheck({
            checkboxClass: 'icheckbox_minimal-blue'
        });
        $('.iCheckBoxBackupForwardbackSelect1').iCheck({
            checkboxClass: 'icheckbox_minimal-blue'
        });
    }
}
// 將VD相關資料List出來
function CreateListVDStandbyJobsBackupHtml() {
    var HtmlStr = "";
    var no = 1;
    for (var key in VDStandbyJobsList) {
        HtmlStr += '<tr class="border_bottom">';
        HtmlStr += '<td id="ss_wrapper_top_bottom_Item" class="autobk_bottom0">';
        HtmlStr += '<label>' + no + '</label>';
        HtmlStr += '</td>';
        HtmlStr += '<td id="ss_top_bottom_chk" class="autobk_top_bottomchk">';
        // HtmlStr += '<input type="checkbox" value="" name="top_select" id="'+ element['userid'] +'" class="css-checkbox">';
        // HtmlStr += '<label for="top_select" class="css-label" style="margin: 0.5px;"></label>';
        HtmlStr += '<input type="checkbox" id="' + VDStandbyJobsList[key].vdid + '" class="iCheckBoxStandbySelect1">';
        HtmlStr += '</td>';
        HtmlStr += '<td id="ss_wrapper_top_bottom_Owner" class="autobk_bottom2" style="text-align: left;">';
        HtmlStr += ' <label style=" width: 105px; display: block; text-overflow: ellipsis; overflow:hidden; white-space:nowrap; ">' + VDStandbyJobsList[key].username + '</label>';
        HtmlStr += '</td>';
        HtmlStr += '<td id="ss_wrapper_top_bottom_VDName" class="autobk_bottom3" style="text-align: left;">';
        HtmlStr += ' <label style=" width: 105px; display: block; text-overflow: ellipsis; overflow:hidden; white-space:nowrap; ">' + VDStandbyJobsList[key].vdname + '</label>';
        HtmlStr += '</td>';
        HtmlStr += '<td id="ss_wrapper_top_bottom_VDStatus" class="autobk_bottom4" style="text-align: left;">';
        // HtmlStr += ' <label>' + element['state'] + '</label>';
        HtmlStr += GetVMStatus(VDStandbyJobsList[key].state, VDStandbyJobsList[key].online);
        HtmlStr += '</td>';
        HtmlStr += '<td id="ss_wrapper_top_bottom_Remark" class="last_column autobk_bottom5" style="text-align: left;">';
        HtmlStr += ' <label>' + VDStandbyJobsList[key].desc + '</label>';
        HtmlStr += '</td>';
        HtmlStr += '</tr>';

        no++;
    }

    $('#autobk_ListBottom').empty();
    $('#autobk_ListBottom').append(HtmlStr);
    $('.iCheckBoxStandbySelect1').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });
    $('.iCheckBoxAllStandbyBackupJobsSelect').iCheck('uncheck');
    $('.iCheckBoxStandbySelect1').iCheck('uncheck');
};

function CreateListScheduleJobsBackupHtml() {
    var HtmlStr = "";
    var no = 1;
    for (var key in VDForBackupJobsList) {
        HtmlStr += '<tr class="border_bottom">';
        HtmlStr += '<td id="ss_wrapper_bottom_bottom_Item" class="autobk_bottom0">';
        HtmlStr += '<label>' + no + '</label>';
        HtmlStr += '</td>';
        HtmlStr += '<td id="ss_bottom_chk" class="ss_bottom_chk">';
        // HtmlStr += '<input type="checkbox" value="" name="bottom_select" id="bottom_select" class="css-checkbox">';
        // HtmlStr += '<label for="bottom_select" class="css-label" style="margin: 0.6px;"></label>';
        HtmlStr += '<input type="checkbox" id="' + VDForBackupJobsList[key].vdid + '" class="iCheckBoxBackupForwardbackSelect1">';
        HtmlStr += '</td>';
        HtmlStr += '<td id="ss_wrapper_bottom_bottom_Owner" class="autobk_bottom2" style="text-align: left;">';
        HtmlStr += ' <label style=" width: 105px; display: block; text-overflow: ellipsis; overflow:hidden; white-space:nowrap; ">' + VDForBackupJobsList[key].username + '</label>';
        HtmlStr += '</td>';
        HtmlStr += '<td id="ss_wrapper_bottom_bottom_VDNam" class="autobk_bottom3" style="text-align: left;">';
        HtmlStr += ' <label style=" width: 105px; display: block; text-overflow: ellipsis; overflow:hidden; white-space:nowrap; ">' + VDForBackupJobsList[key].vdname + '</label>';
        HtmlStr += '</td>';
        HtmlStr += '<td id="ss_wrapper_bottom_bottom_VDStatus" class="autobk_bottom4" style="text-align: left;">';
        HtmlStr += GetVMStatus(VDForBackupJobsList[key].state, VDForBackupJobsList[key].online);
        HtmlStr += '</td>';
        HtmlStr += '<td id="ss_wrapper_bottom_bottom_Remark" class="last_column autobk_bottom5" style="text-align: left;">';
        HtmlStr += ' <label>' + VDForBackupJobsList[key].desc + '</label>';
        HtmlStr += '</td>';
        HtmlStr += '</tr>';

        no++;

    }

    $('#autobk_ListJobBottom').empty();
    $('#autobk_ListJobBottom').append(HtmlStr);
    $('.iCheckBoxBackupForwardbackSelect1').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });
    $('.iCheckBoxAllBackupForwardbackJobsSelect').iCheck('uncheck');
    $('.iCheckBoxBackupForwardbackSelect1').iCheck('uncheck');

};

function InitAddToJobBackupBtn() {
    $('#autobk_AddToJob').click(function (event) {
        event.preventDefault();
        $.each($('.iCheckBoxStandbySelect1:checked'), function (index, element) {
            var id = $(element).attr('id');
            var tmpVD = VDStandbyJobsList[id];
            VDForBackupJobsList[tmpVD['vdid']] = tmpVD;
            delete VDStandbyJobsList[id];
        });
        CreateListVDStandbyJobsBackupHtml(); // 新增 by william
        CreateListScheduleJobsBackupHtml(); // 新增 by william  
    });
}

function InitAddToStandbyBackupBtn() {
    $('#autobk_AddToStandby').click(function (event) {
        event.preventDefault();
        $.each($('.iCheckBoxBackupForwardbackSelect1:checked'), function (index, element) {
            var id = $(element).attr('id');
            var tmpVD = VDForBackupJobsList[id];
            VDStandbyJobsList[tmpVD['vdid']] = tmpVD;
            delete VDForBackupJobsList[id];
        });
        CreateListVDStandbyJobsBackupHtml(); // 新增 by william
        CreateListScheduleJobsBackupHtml(); // 新增 by william  
    });
}
/*=================== 排程Jobs (1)建立清單 2017.02.10 End ===================*/
/*=================== 排程Jobs (2)建立 2017.02.10 Start ===================*/
// 建立排程按鈕實作 2017.02.13 william         
function InitCreateScheduleJobBackupButton(idSchedule, pname, pdesc) {
    var jobtype = 2;    // Job Type類型: 1. 快照, 2. 備份
    var name = pname;
    var desc = pdesc;
    var vds = '';
    var i = 0;
    $('#autobk_ListJobBottom').empty();
    for (var key in VDForBackupJobsList) {
        if (i != 0)
            vds += ',';
        vds += '"' + VDForBackupJobsList[key].vdid + '"';
        i++
    }

    CreateScheduleJobBackup(idSchedule, name, jobtype, desc, vds);
}

function CreateScheduleJobBackup(idSchedule, name, jobtype, desc, vds) {
    var request = Ajax_CreateJob(idSchedule, name, jobtype, desc, vds);
    CallBackCreateScheduleJobBackup(request);
};

function CallBackCreateScheduleJobBackup(request) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        // GetBKScheduleForCreateTableList();
        GetScheduleForCreateTableList();
        $("#dialog_autobk").dialog('close');
    });
    request.fail(function (jqxhr, textStatus) {

    });
}
/*=================== 排程Jobs (2)建立 2017.02.10 End ===================*/    
/*=================== 排程Jobs (3)Job清單 2017.02.10 Start ===================*/     
function GetBKJobsForCreateTableList(idJob) {
    var request = Ajax_ListJobs_SendCommand(idJob);
    var jobVds = '';
    request.done(function (AjaxData, statustext, jqXHR) {
        isViewjob = 1;
        jobVds = AjaxData.VDs;
        GetVDsForCreateBackupTableList(isViewjob, jobVds);
    });
    request.fail(function (jqXHR, textStatus) {

    });
}

function ModifyBackupScheduleJob() {
    var jobtype = 2;    // Job Type類型: 1. 快照, 2. 備份
    var vds = '';
    var i = 0;
    var id = JobsID;

    for (var key in VDForBackupJobsList) {
        if (i != 0)
            vds += ',';
        vds += '"' + VDForBackupJobsList[key].vdid + '"';
        i++
    }
    
    var request = Ajax_ModifyJobs(id, vds);
    CallBackModifyBackupScheduleJob(request);
}

function CallBackModifyBackupScheduleJob(request) {
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
        $("#dialog_autobk").dialog('close');
    });
    request.fail(function (jqxhr, textStatus) {

    });
}

/*=================== 排程Jobs (3)Job清單 2017.02.10 End ===================*/      
/* ----------------------------- 功能 ----------------------------- */

function InitBackupJobSelectAllCheckBox() {
    $('.iCheckBoxAllStandbyBackupJobsSelect').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });
    $('.iCheckBoxAllBackupForwardbackJobsSelect').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });

    $('.iCheckBoxAllStandbyBackupJobsSelect').on('ifChanged', function (event) {
        var element = $(this);
        if (element.prop('checked')) {
            $('.iCheckBoxStandbySelect1').iCheck('check');
        }
        else {
            $('.iCheckBoxStandbySelect1').iCheck('uncheck');
        }
    });
    $('.iCheckBoxAllBackupForwardbackJobsSelect').on('ifChanged', function (event) {
        var element = $(this);
        if (element.prop('checked')) {
            $('.iCheckBoxBackupForwardbackSelect1').iCheck('check');
        }
        else {
            $('.iCheckBoxBackupForwardbackSelect1').iCheck('uncheck');
        }
    });
}