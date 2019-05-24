/*
* Schedule 排程功能
* Date: 2017.02.18
* Maker: william
*/
var dateToday = new Date();
var timeNow = new Date(0,0,0,0,0,0);
var idSchedule = '';
var idJob = '';
var valueSchedule = '';
var pname;
var pdesc;
var idSchedule_temparr = [];
var SCJobType;

var NowSortSnapshotScheduleType;
var NowSortSnapshotScheduleDirection;
var NowSortSnapshotScheduleDOMID;
var NowSortBackupScheduleType;
var NowSortBackupScheduleDirection;
var NowSortBackupScheduleDOMID;

var FirstTimeClickFlag = false;

function InitSchedule() {

    radChecked(); // 排程類型的選擇 
    $('input[name="radMethod"]').on("click", radChecked); // 排程類型的選擇 
    InitScheduleSelectAllCheckBox(); // 建立Checkbox功能 2017.02.18 william
    EnableDisableSelectScheduleActionButton();

    // GetBKScheduleForCreateTableList();  // 建立排程列表(BK)

    radCheckedbk(); // 排程類型的選擇 
    $('input[name="radMethodbk"]').on("click", radCheckedbk); // 排程類型的選擇     
    GetScheduleForCreateTableList(); // 建立排程列表
    InitBackupScheduleSelectAllCheckBox();
    EnableDisableSelectBackupScheduleActionButton();
};
/**************************************************************************/
/*                                                                        */
/*                               Dialog畫面                               */
/*                                                                        */
/**************************************************************************/
/*=========================== SnapShot快照 Start ===========================*/
function ButtonForCreateSchedule() {
    $('#btn_ss_create_schedule').click(function (event) {
        event.preventDefault();
        ResetAllSchedule();
        idSchedule = 0; 
        $("#btn_create_schedule_Modify").hide();  
        $("#btn_create_schedule_Confirm").show();
        $("#dialog_create_schedule").dialog('open');
        $("#datepicker_schedule").datepicker('setDate', dateToday);
        $("#timepicker_schedule").timepicker('setDate', timeNow);
        $("#btn_create_schedule_Cancel").removeClass("ui-widget ui-widget");
        $("#btn_create_schedule_Modify").removeClass("ui-widget ui-widget");
        $("#btn_create_schedule_Confirm").removeClass("ui-widget ui-widget");
        FirstTimeClickFlag = true;        
    });
}

function CreateDialog_CreateSchedule() {
    $("#dialog_create_schedule").dialog({
        title: LanguageStr_Service.Service_SS_Schedule_title,
        modal: true,
        resizable: false,
        draggable: true,
        closeOnEscape: false,
        autoOpen: false,
        dialogClass: "no-close",
        height: 433,
        width: 800,
        // 關閉預設的視窗關閉鈕
        // open: function (event, ui) {
        //     $(".ui-dialog-titlebar-close", ui.dialog | ui).hide();
        // },
        // 預設起始的視窗顯示位置
        // position: { my: "center", at: "left+910px top+525px ", of: window  } ,                 
    });
    $('#btn_close_ss_create_schedule').click(function (event) {
        event.preventDefault();
        $("#dialog_create_schedule").dialog('close');
        ResetAllSchedule();
    });
    $('#btn_create_schedule_Cancel').click(function (event) {
        event.preventDefault();
        // ResetAllSchedule();
        $("#dialog_create_schedule").dialog('close');
    });
    $('#btn_create_schedule_Modify').click(function (event) {
        event.preventDefault();
        InitModifyScheduleButton(idSchedule);
    });
}
/*=========================== SnapShot快照 End ===========================*/
/*=========================== Backup備份 Start ===========================*/
function ButtonForCreateBKSchedule() {
    $('#btn_bk_create_schedule').click(function (event) {
        event.preventDefault();
        ResetAllSchedule();
        idSchedule = 0;
        $("#btn_create_bkschedule_Modify").hide();
        $('#btn_create_bkschedule_Confirm').show();
        $("#dialog_create_bkschedule").dialog('open');
        $("#datepicker_schedulebk").datepicker('setDate', dateToday);
        $("#timepicker_schedulebk").timepicker('setDate', timeNow);
        $("#btn_create_bkschedule_Cancel").removeClass("ui-widget ui-widget");
        $("#btn_create_bkschedule_Modify").removeClass("ui-widget ui-widget");
        $("#btn_create_bkschedule_Confirm").removeClass("ui-widget ui-widget");        
        FirstTimeClickFlag = true;
    });
}

function CreateDialog_CreateBackupSchedule() {
    $("#dialog_create_bkschedule").dialog({
        title: LanguageStr_Service.Service_BK_Schedule_title,
        modal: true,
        resizable: false,
        draggable: true,
        closeOnEscape: false,
        autoOpen: false,
        dialogClass: "no-close",
        height: 433,
        width: 800,
    });
    $('#btn_close_bk_create_schedule').click(function (event) {
        event.preventDefault();
        $("#dialog_create_bkschedule").dialog('close');
        ResetAllSchedule();
    });
    $('#btn_create_bkschedule_Cancel').click(function (event) {
        event.preventDefault();
        // ResetAllSchedule();
        $("#dialog_create_bkschedule").dialog('close');
    });
    $('#btn_create_bkschedule_Modify').click(function (event) {
        event.preventDefault();
        InitModifyScheduleBackupButton(idSchedule);
    });
}
/*=========================== Backup備份 End ===========================*/

/**************************************************************************/
/*                                                                        */
/*                               產生排程相關畫面                           */
/*                                                                        */
/**************************************************************************/
/*=================== 排程快照 (1)清單 2017.02.10 Start ===================*/
function GetScheduleForCreateTableList() {
    var request = Ajax_ListSchedule_SendCommand();
    request.done(function (AjaxData, statustext, jqXHR) {
        $('.Div_WaitAjax').dialog('close');
        ListScheduleSnapshotToUI(AjaxData);
    });
    request.fail(function (jqXHR, textStatus) {
        //oUIError.CheckAuth(jqxhr.status, ActionStatus.ListSchedule);
        $('.Div_WaitAjax').dialog('close');
    });
}
// 定義class承接json資料
function ListScheduleSnapshotToUI(JsonData) {
    var HtmlStr = "";
    var sc;
    var tmpJob;
    var tmpJobType;
    ScheduleList = [];
    BackupScheduleList = [];
    // ScheduleWithIDList=[];
    JobsList = [];
    $.each(JsonData, function (index, element) {
        tmpJobType = 1;
        sc = new ScheduleClass(index, element['ScheduleID'], element['Name'], element['Desc'], element['isEnable'], element['FreqType'], element['FreqInterval'], element['FreqRelativeInterval'], element['FreqRecurrenceFactor'], element['StartDate'], element['StartTime'], element['CreateTime']);
        $.each(element['Jobs'], function (jobindex, jobelement) {
            tmpJob = new Job_class(jobelement['JobID'], jobelement['Name'], jobelement['Desc'], jobelement['JobType']);
            tmpJobType = jobelement['JobType'];
            JobsList[jobelement['JobID']] = tmpJob;
            sc.Jobs.push(tmpJob);
        });
        if (tmpJobType == 1) {
            ScheduleList.push(sc);
        }
        else {
            BackupScheduleList.push(sc);
        }
        // ScheduleWithIDList[element['ScheduleID']] = sc;
    });
    CreateListScheduleHtml();
    CreateListBackupScheduleHtml();
    BindDropDownBtn();
    EnableDisableSelectScheduleActionButton();
    EnableDisableSelectBackupScheduleActionButton();

    // SortList(NowSortSnapshotScheduleDirection,NowSortSnapshotScheduleType,NowSortSnapshotScheduleDOMID,ScheduleList);
    // CreateListScheduleHtml();

    // if(typeof(NowSortType)=== 'undefined' || NowSortSeedType === null)
    // {                            
    //     $('.label_vm_arrow').html('');
    //     $('#vmname1').html('&#9650;');
    //     var th = $('.VMList_Name');
    //     th.removeClass('desc');
    //     th.addClass('asc');
    //     NowSortDirection = SortDirection.ASC;
    //     NowSortType = SortType.String;
    //     NowSortDOMID = 'name';
    // }
}
// 將Schedule相關資料List出來
function CreateListScheduleHtml() {
    var HtmlStr = "";
    // var HtmlStrSnap = '';
    // var HtmlStrBk = '';
    var no = 1;
    // var noSnap = 1;
    // var noBk = 1;
    var temp_jobid;

    var jobType = 1;
    $.each(ScheduleList, function (index, element) {
        jobType = 1;
        $.each(ScheduleList[index].Jobs, function (jobindex, jobelement) {
            if (jobelement['JobID'] !== 'undefined') {
                temp_jobid = jobelement['JobID'];
                jobType = jobelement['JobType'];
            }
            else
                temp_jobid = "";
        });
        if (index === ScheduleList.length - 1)
            HtmlStr += '<div class = "Div_VMRow LastVMRow">';
        else
            HtmlStr += '<div class = "Div_VMRow">';
        // no = jobType == 1 ? noSnap : noBk;
        HtmlStr += '<H4 class = "TaskList_ItemNo_Row AbsoluteLayout RowField" >' + no + '</H4>';
        HtmlStr += '<div class = "Schedule_Snapshot_Select_Row AbsoluteLayout RowField" >';
        HtmlStr += '<div style="position:relative">';

        HtmlStr += '<input type="checkbox" id="' + element['ScheduleID'] + '" value="' + index + '" href="' + temp_jobid + '" class="iCheckBoxScheduleSelect">';
        HtmlStr += '</div>';
        HtmlStr += '</div>';
        HtmlStr += '<H4 class = "Schedule_Snapshot_Name_Row AbsoluteLayout RowField">' + element['Name'] + '</H4>';
        HtmlStr += '<H4 class = "Schedule_Snapshot_Desc_Row AbsoluteLayout RowField">' + element['Desc'] + '</H4>';
        HtmlStr += '<H4 class = "Schedule_Snapshot_CreateTime_Row AbsoluteLayout RowField">' + element['CreateTime'] + '</H4>';
        HtmlStr += '<H4 class = "Schedule_Snapshot_Action_Row AbsoluteLayout RowField">' + CreateScheduleDropDownHtml(no, element['index'], element['ScheduleID'], temp_jobid) + '</H4>';
        // $.each(ScheduleList[no-1].Jobs,function(jobindex,jobelement){  
        // HtmlStr += '<H4 class = "Schedule_Snapshot_Action_Row AbsoluteLayout RowField">' + jobelement['JobID'] + '</H4>'; 
        // });       
        HtmlStr += '</div>';
                no++;
    });
    $('#Div_Service_Snapshot_List').empty();
    $('#Div_Service_Snapshot_List').html(HtmlStr);
    $('.iCheckBoxScheduleSelect').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });
    $('.iCheckBoxAllScheduleSelect').iCheck('uncheck');
    $('.iCheckBoxScheduleSelect').iCheck('uncheck');
    $('.iCheckBoxScheduleSelect').on('ifChanged', function (event) {
        EnableDisableSelectScheduleActionButton();
    });

};
// Schedule List的按鈕實作
function CreateScheduleDropDownHtml(no, index, ScheduleID, temp_jobid) {
    var drop_down_content = '';
    drop_down_content = '#dropdown-VM-Operation9';
    return '<a href="' + temp_jobid + '" value="' + (no - 1) + '" id="' + ScheduleID + '" class="btn-light btn-operation-vm ScheduleDropBtn" data-dropdown="' + drop_down_content + '" data-operation="' + no + '">' + LanguageStr_VM.VM_Setting + '&nbsp;▼</a>';
};
// 按下按鈕時，來獲取List資料的Schedule ID，Job ID，index
function BindDropDownBtn() {
    $('.ScheduleDropBtn').click(function (event) {
        event.preventDefault();
        var btn_element = $(this);
        idSchedule = btn_element.attr('id');
        idJob = btn_element.attr('href');
        valueSchedule = btn_element.attr('value');
    });
}

function InitScheduleSelectAllCheckBox() {
    $('.iCheckBoxAllScheduleSelect').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });
    $('.iCheckBoxAllScheduleSelect').on('ifChanged', function (event) {
        var element = $(this);
        if (element.prop('checked')) {
            $('.iCheckBoxScheduleSelect').iCheck('check');
            // $('.iCheckBoxDisable').iCheck('uncheck');
        }
        else {
            $('.iCheckBoxScheduleSelect').iCheck('uncheck');
        }
        EnableDisableSelectScheduleActionButton();
    });
}

function EnableDisableSelectScheduleActionButton() {
    if ($('.iCheckBoxScheduleSelect:checked').length > 0) {
        $('.btn_sc_select_operation').button("enable");
    }
    else {
        $('.btn_sc_select_operation').button("disable");
    }
}
/*=================== 排程快照 (1)清單 2017.02.10 End ===================*/
/*=================== 排程快照 (2)刪除 2017.02.10 Start ===================*/
function DeleteScheduleSnapshot(id_Schedule) {
    if (id_Schedule.length > 0) {
        for (var i = 0; i < id_Schedule.length; i++) {
            var request = Ajax_DeleteSchedule(id_Schedule[i]);
            CallBackDeleteScheduleSnapshot(request);
        }
    }
};

function CallBackDeleteScheduleSnapshot(request) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        SortList(NowSortSnapshotScheduleDirection, NowSortSnapshotScheduleType, NowSortSnapshotScheduleDOMID, ScheduleList);
        GetScheduleForCreateTableList();
    });
    request.fail(function (jqxhr, textStatus) {

    });
}
/*=================== 排程快照 (2)刪除 2017.02.10 End ===================*/
/*=================== 排程快照 (3)建立 2017.02.10 Start ===================*/
// 建立排程按鈕實作 2017.02.13 william         
function InitCreateScheduleButton() {
    $('#btn_create_schedule_Confirm').click(function (event) {
        event.preventDefault();
        var name = $('#cs_name_input').val();
        var type;
        var startdate = GetDateValue($('#datepicker_schedule').val());
        var starttime = GetTimeValue($('#timepicker_schedule').val());        
        var fail_msg = LanguageStr_Service.Schedule_Create_Fail;

        type = $('input[name=radMethod]:checked', '#st_table').val();

        if(name.length==0) {
            alert(fail_msg);
            return false;
        } else if(typeof(type) == "undefined") {
            alert(fail_msg);
            return false;
        } else if($('#datepicker_schedule').val().length==0) {
            alert(fail_msg);
            return false;
        } else if($('#timepicker_schedule').val().length==0) {
            alert(fail_msg);      
            return false;
        }   
            
       OpenSnapshotTargetSetup();
        //CreateScheduleSnapshot(name, desc, isenable, type, interval, relativeinterval, recurrencefactor, startdate, starttime);
    });
}

function OpenSnapshotTargetSetup() {
    $("#dialog_create_schedule").dialog('close');
    if(FirstTimeClickFlag) {
        FirstTimeClickFlag = false;
        GetVDsForCreateTableList();   
        $('#autoss_ListBottom').empty();
        $('#autoss_ListJobBottom').empty();      
        $('#autoss_Previous').show();   
        $('#autoss_Confirm').show();              
        $('#autoss_Modify').hide(); 
    }        
    $("#dialog_autoss").dialog('open');    
}


function CreateScheduleSnapshot(name, description, isenable, type, interval, relativeinterval, recurrencefactor, start_date, start_time) {
    var request = Ajax_CreateSchedule(name, description, isenable, type, interval, relativeinterval, recurrencefactor, start_date, start_time);
    CallBackCreateScheduleSnapshot(request);
};

function CallBackCreateScheduleSnapshot(request) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        idSchedule = msg['ScheduleID'];
        pname = msg['Name'];
        pdesc = msg['Desc'];
        InitCreateScheduleJobButton(idSchedule, pname, pdesc);
        $("#dialog_autoss").dialog('close');  
    });
    request.fail(function (jqxhr, textStatus) {
        //oHtml.stopPage();
        // oError.CheckAuth(jqxhr.status,ActionStatus.VMSnapshotCreate);
        if(jqxhr.status == 400) {
            var fail_msg = LanguageStr_Service.Schedule_Create_Fail;
            alert(fail_msg);
        }
    });
}
/*=================== 排程快照 (3)建立 2017.02.10 End ===================*/
/*=================== 排程快照 (4)修改 2017.02.14 Start ===================*/
function ScheduleInfo(valueSchedule) {
    var Info_name = ScheduleList[valueSchedule].Name;
    var Info_desc = ScheduleList[valueSchedule].Desc;
    var Info_isenab = ScheduleList[valueSchedule].isEnable;
    var Info_type = ScheduleList[valueSchedule].FreqType;
    var Info_interval = ScheduleList[valueSchedule].FreqInterval;
    var Info_relativeinterval = ScheduleList[valueSchedule].FreqRelativeInterval;
    var Info_recurrencefactor = ScheduleList[valueSchedule].FreqRecurrenceFactor;
    var Info_startdate = ConvertDateValue(ScheduleList[valueSchedule].StartDate);
    var Info_starttime = ConvertTimeValue(ScheduleList[valueSchedule].StartTime);

    var x, y, z, w;

    $('#cs_name_input').val(Info_name);
    $('#cs_desc_input').val(Info_desc);
    $('#datepicker_schedule').val(Info_startdate);
    $('#timepicker_schedule').val(Info_starttime);

    if (Info_type == 4) {
        $('#div_' + Info_type).show();
    }
    else if (Info_type == 8) {
        $('#div_' + Info_type).show();
        x = Info_interval.toString(2);
        y = padLeft(x, 7);
        isWeekchecked(y);
    }
    else if (Info_type == 16) {
        $('#div_' + Info_type).show();
        x = Info_interval.toString(2);
        y = padLeft(x, 32);
        isMonthchecked(y);
    }
    else if (Info_type == 32) {
        $('#div_' + Info_type).show();
        x = Info_interval.toString(2);
        z = Info_relativeinterval.toString(2);
        y = padLeft(x, 7);
        w = padLeft(z, 5);
        isWeekRchecked(y);
        isMonthRchecked(w);
    }

    $('input[name="radMethod"][value="' + Info_type + '"]', '#st_table').prop('checked', true);
    // $("input[name=mygroup][value=" + value + "]").prop('checked', true);    
}

function InitModifyScheduleButton(idSchedule) {
    //     $('#btn_create_schedule_Modify').click(function(event){
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
    desc = desc.replace(/"/g, '\\"');

    ModifyScheduleSnapshot(idSchedule, name, desc, isenable, type, interval, relativeinterval, recurrencefactor, startdate, starttime);
    //    });
}

function ModifyScheduleSnapshot(id_Schedule, name, description, isenable, type, interval, relativeinterval, recurrencefactor, start_date, start_time) {
    var request = Ajax_ModifySchedule(id_Schedule, name, description, isenable, type, interval, relativeinterval, recurrencefactor, start_date, start_time);
    CallBackModifyScheduleSnapshot(request);
};

function CallBackModifyScheduleSnapshot(request) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        $("#dialog_create_schedule").dialog('close');
        SortList(NowSortSnapshotScheduleDirection, NowSortSnapshotScheduleType, NowSortSnapshotScheduleDOMID, ScheduleList);
        GetScheduleForCreateTableList();
        ResetAllSchedule();
    });
    request.fail(function (jqxhr, textStatus) {

    });
}

/*=================== 排程快照 (4)修改 2017.02.14 End ===================*/
/*=========================== Backup備份 Start ===========================*/
/**************************************************************************/
/*                                                                        */
/*                               產生排程相關畫面                           */
/*                                                                        */
/**************************************************************************/
/*=================== 排程備份 (1)清單 2017.02.10 Start ===================*/
// function GetBKScheduleForCreateTableList() {
//     var request = Ajax_ListSchedule_SendCommand();
//     request.done(function (AjaxData, statustext, jqXHR) {
//         ListScheduleBackupToUI(AjaxData);
//     });
//     request.fail(function (jqXHR, textStatus) {
//         oUIError.CheckAuth(jqxhr.status, ActionStatus.ListSchedule);
//     });
// }
// // 定義class承接json資料
// function ListScheduleBackupToUI(JsonData) {
//     var HtmlStr = "";
//     var sc;
//     var tmpJob;
//     BackupScheduleList = [];
//     JobsList = [];
//     $.each(JsonData, function (index, element) {
//         sc = new ScheduleClass(index, element['ScheduleID'], element['Name'], element['Desc'], element['isEnable'], element['FreqType'], element['FreqInterval'], element['FreqRelativeInterval'], element['FreqRecurrenceFactor'], element['StartDate'], element['StartTime'], element['CreateTime']);
//         $.each(element['Jobs'], function (jobindex, jobelement) {
//             tmpJob = new Job_class(jobelement['JobID'], jobelement['Name'], jobelement['Desc'], jobelement['JobType']);
//             JobsList[jobelement['JobID']] = tmpJob;
//             sc.Jobs.push(tmpJob);
//         });
//         BackupScheduleList.push(sc);
//     });
//     // CreateListBackupScheduleHtml();
//     BindDropDownBtn();
//     EnableDisableSelectBackupScheduleActionButton();
// }

// 將Schedule相關資料List出來
function CreateListBackupScheduleHtml() {
    var HtmlStr = "";
    var no = 1;
    var temp_jobid;

    var jobType = 2;
    $.each(BackupScheduleList, function (index, element) {
        jobType = 2;
        $.each(BackupScheduleList[index].Jobs, function (jobindex, jobelement) {
            if (jobelement['JobID'] !== 'undefined') {
                temp_jobid = jobelement['JobID'];
                jobType = jobelement['JobType'];
            }
            else
                temp_jobid = "";
        });
        if (index === BackupScheduleList.length - 1)
            HtmlStr += '<div class = "Div_VMRow LastVMRow">';
        else
            HtmlStr += '<div class = "Div_VMRow">';
        HtmlStr += '<H4 class = "TaskList_ItemNo_Row AbsoluteLayout RowField" >' + no + '</H4>';
        HtmlStr += '<div class = "Schedule_Backup_Select_Row AbsoluteLayout RowField" >';
        HtmlStr += '<div style="position:relative">';

        HtmlStr += '<input type="checkbox" id="' + element['ScheduleID'] + '" value="' + index + '" href="' + temp_jobid + '" class="iCheckBoxBackupScheduleSelect">';
        HtmlStr += '</div>';
        HtmlStr += '</div>';
        HtmlStr += '<H4 class = "Schedule_Backup_Name_Row AbsoluteLayout RowField">' + element['Name'] + '</H4>';
        HtmlStr += '<H4 class = "Schedule_Backup_Desc_Row AbsoluteLayout RowField">' + element['Desc'] + '</H4>';
        HtmlStr += '<H4 class = "Schedule_Backup_CreateTime_Row AbsoluteLayout RowField">' + element['CreateTime'] + '</H4>';
        HtmlStr += '<H4 class = "Schedule_Backup_Action_Row AbsoluteLayout RowField">' + CreateBKScheduleDropDownHtml(no, element['index'], element['ScheduleID'], temp_jobid) + '</H4>';
        HtmlStr += '</div>';
        no++;
    });
    $('#Div_Service_BackupSchedule_List').html(HtmlStr);
    $('.iCheckBoxBackupScheduleSelect').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });
    $('.iCheckBoxAllBackupScheduleSelect').iCheck('uncheck');
    $('.iCheckBoxBackupScheduleSelect').iCheck('uncheck');
    $('.iCheckBoxBackupScheduleSelect').on('ifChanged', function (event) {
        EnableDisableSelectBackupScheduleActionButton();
    });
};

// Schedule List的按鈕實作
function CreateBKScheduleDropDownHtml(no, index, ScheduleID, temp_jobid) {
    var drop_down_content = '';
    drop_down_content = '#dropdown-VM-Operation10';
    return '<a href="' + temp_jobid + '" value="' + (no - 1) + '" id="' + ScheduleID + '" class="btn-light btn-operation-vm ScheduleDropBtn" data-dropdown="' + drop_down_content + '" data-operation="' + no + '">' + LanguageStr_VM.VM_Setting + '&nbsp;▼</a>';
};

function InitBackupScheduleSelectAllCheckBox() {
    $('.iCheckBoxAllBackupScheduleSelect').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });
    $('.iCheckBoxAllBackupScheduleSelect').on('ifChanged', function (event) {
        var element = $(this);
        if (element.prop('checked')) {
            $('.iCheckBoxBackupScheduleSelect').iCheck('check');
            // $('.iCheckBoxDisable').iCheck('uncheck');
        }
        else {
            $('.iCheckBoxBackupScheduleSelect').iCheck('uncheck');
        }
        EnableDisableSelectBackupScheduleActionButton();
    });
}

function EnableDisableSelectBackupScheduleActionButton() {
    if ($('.iCheckBoxBackupScheduleSelect:checked').length > 0) {
        $('.btn_sc_select_operation').button("enable");
    }
    else {
        $('.btn_sc_select_operation').button("disable");
    }
}

/*=================== 排程備份 (1)清單 2017.02.10 Start ===================*/
/*=================== 排程備份 (2)刪除 2017.02.10 Start ===================*/
function DeleteScheduleBackup(id_Schedule) {
    if (id_Schedule.length > 0) {
        for (var i = 0; i < id_Schedule.length; i++) {
            var request = Ajax_DeleteSchedule(id_Schedule[i]);
            CallBackDeleteScheduleBackup(request);
        }
    }
};

function CallBackDeleteScheduleBackup(request) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        SortList(NowSortBackupScheduleDirection, NowSortBackupScheduleType, NowSortBackupScheduleDOMID, BackupScheduleList);
        // GetBKScheduleForCreateTableList();
        GetScheduleForCreateTableList();
    });
    request.fail(function (jqxhr, textStatus) {

    });
}
/*=================== 排程備份 (2)刪除 2017.02.10 End ===================*/
/*=================== 排程備份 (3)建立 2017.02.10 Start ===================*/
function InitCreateBackupScheduleButton() {
    $('#btn_create_bkschedule_Confirm').click(function (event) {
        event.preventDefault();
        var name = $('#csbk_name_input').val();
        var desc = $('#csbk_desc_input').val();
        var type;
        var startdate = GetDateValue($('#datepicker_schedulebk').val());
        var starttime = GetTimeValue($('#timepicker_schedulebk').val());
        var fail_msg = LanguageStr_Service.Schedule_Create_Fail;        

        type = $('input[name=radMethodbk]:checked', '#st_bktable').val();

        if(name.length==0) {
            alert(fail_msg);
            return false;
        } else if(typeof(type) == "undefined") {
            alert(fail_msg);
            return false;
        } else if($('#datepicker_schedulebk').val().length==0) {
            alert(fail_msg);
            return false;
        } else if($('#timepicker_schedulebk').val().length==0) {
            alert(fail_msg);      
            return false;
        }            

        OpenBackupTargetSetup();
        // CreateScheduleBackup(name, desc, isenable, type, interval, relativeinterval, recurrencefactor, startdate, starttime);
    });
}

function OpenBackupTargetSetup() {
    $("#dialog_create_bkschedule").dialog('close');
    if(FirstTimeClickFlag) {
        FirstTimeClickFlag = false;
        GetVDsForCreateBackupTableList();
        $('#autobk_ListBottom').empty();
        $('#autobk_ListJobBottom').empty();      
        $('#autobk_Previous').show();      
        $('#autobk_Confirm').show();          
        $('#autobk_Modify').hide();          
    }        
    $("#dialog_autobk").dialog('open');   
}


function CreateScheduleBackup(name, description, isenable, type, interval, relativeinterval, recurrencefactor, start_date, start_time) {
    var request = Ajax_CreateSchedule(name, description, isenable, type, interval, relativeinterval, recurrencefactor, start_date, start_time);
    CallBackCreateScheduleBackup(request);
};

function CallBackCreateScheduleBackup(request) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        idSchedule = msg['ScheduleID'];
        pname = msg['Name'];
        pdesc = msg['Desc'];

        InitCreateScheduleJobBackupButton(idSchedule, pname, pdesc);
        $("#dialog_autobk").dialog('close');  

    });
    request.fail(function (jqxhr, textStatus) {
        if(jqxhr.status == 400) {
            var fail_msg = LanguageStr_Service.Schedule_Create_Fail;
            alert(fail_msg);
        }
    });
}
/*=================== 排程備份 (3)建立 2017.02.10 End ===================*/
/*=================== 排程備份 (4)修改 2017.02.14 Start ===================*/
function BKScheduleInfo(valueSchedule) {
    var Info_name = BackupScheduleList[valueSchedule].Name;
    var Info_desc = BackupScheduleList[valueSchedule].Desc;
    var Info_isenab = BackupScheduleList[valueSchedule].isEnable;
    var Info_type = BackupScheduleList[valueSchedule].FreqType;
    var Info_interval = BackupScheduleList[valueSchedule].FreqInterval;
    var Info_relativeinterval = BackupScheduleList[valueSchedule].FreqRelativeInterval;
    var Info_recurrencefactor = BackupScheduleList[valueSchedule].FreqRecurrenceFactor;
    var Info_startdate = ConvertDateValue(BackupScheduleList[valueSchedule].StartDate);
    var Info_starttime = ConvertTimeValue(BackupScheduleList[valueSchedule].StartTime);

    var x, y, z, w;

    $('#csbk_name_input').val(Info_name);
    $('#csbk_desc_input').val(Info_desc);
    $('#datepicker_schedulebk').val(Info_startdate);
    $('#timepicker_schedulebk').val(Info_starttime);

    if (Info_type == 4) {
        $('#div_bk' + Info_type).show();
    }
    else if (Info_type == 8) {
        $('#div_bk' + Info_type).show();
        x = Info_interval.toString(2);
        y = padLeft(x, 7);
        isWeekbkchecked(y);
    }
    else if (Info_type == 16) {
        $('#div_bk' + Info_type).show();
        x = Info_interval.toString(2);
        y = padLeft(x, 32);
        isMonthbkchecked(y);
    }
    else if (Info_type == 32) {
        $('#div_bk' + Info_type).show();
        x = Info_interval.toString(2);
        z = Info_relativeinterval.toString(2);
        y = padLeft(x, 7);
        w = padLeft(z, 5);
        isWeekRbkchecked(y);
        isMonthRbkchecked(w);
    }

    $('input[name="radMethodbk"][value="' + Info_type + '"]', '#st_bktable').prop('checked', true);
    // $("input[name=mygroup][value=" + value + "]").prop('checked', true);    
}

function InitModifyScheduleBackupButton(idSchedule) {
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

    ModifyScheduleBackup(idSchedule, name, desc, isenable, type, interval, relativeinterval, recurrencefactor, startdate, starttime);
}

function ModifyScheduleBackup(id_Schedule, name, description, isenable, type, interval, relativeinterval, recurrencefactor, start_date, start_time) {
    var request = Ajax_ModifySchedule(id_Schedule, name, description, isenable, type, interval, relativeinterval, recurrencefactor, start_date, start_time);
    CallBackModifyScheduleBackup(request);
};

function CallBackModifyScheduleBackup(request) {
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        $("#dialog_create_bkschedule").dialog('close');
        SortList(NowSortBackupScheduleDirection, NowSortBackupScheduleType, NowSortBackupScheduleDOMID, BackupScheduleList);
        // GetBKScheduleForCreateTableList();
        GetScheduleForCreateTableList();
        ResetAllSchedule();

    });
    request.fail(function (jqxhr, textStatus) {

    });
}
/*=================== 排程備份 (4)修改 2017.02.14 End ===================*/
/*=========================== Backup備份 End ===========================*/
/**************************************************************************/
/*                                                                        */
/*                           Relay Ajax Rule建立                           */
/*                                                                        */
/**************************************************************************/
// 1. 取得全部 Schedule 資訊
function Ajax_ListSchedule_SendCommand() {
    var request = CallAjax("/manager/schedule", "", "json", "GET");
    return request;
}
// 2. 建立 Schedule 
function Ajax_CreateSchedule(name, description, isenable, type, interval, relativeinterval, recurrencefactor, start_date, start_time) {
    var jsonrequest = '{';
    jsonrequest += '"Name":"' + name + '",';
    jsonrequest += '"Desc":"' + description + '",';
    jsonrequest += '"isEnable":' + isenable + ',';
    jsonrequest += '"FreqType":' + type + ',';
    jsonrequest += '"FreqInterval":' + interval + ',';
    jsonrequest += '"FreqRelativeInterval":' + relativeinterval + ',';
    jsonrequest += '"FreqRecurrenceFactor":' + recurrencefactor + ',';
    jsonrequest += '"StartDate":' + start_date + ',';
    jsonrequest += '"StartTime":' + start_time;
    jsonrequest += '}';
    var request = CallAjax("/manager/schedule", jsonrequest, "", "POST");
    return request;
};
// 3. 刪除 Schedule 
function Ajax_DeleteSchedule(id_Schedule) {
    var request = CallAjax("/manager/schedule/" + id_Schedule, "", "", "DELETE");
    return request;
};
// 4. 修改 Schedule 
function Ajax_ModifySchedule(id_Schedule, name, description, isenable, type, interval, relativeinterval, recurrencefactor, start_date, start_time) {
    var jsonrequest = '{';
    jsonrequest += '"Name":"' + name + '",';
    jsonrequest += '"Desc":"' + description + '",';
    jsonrequest += '"isEnable":' + isenable + ',';
    jsonrequest += '"FreqType":' + type + ',';
    jsonrequest += '"FreqInterval":' + interval + ',';
    jsonrequest += '"FreqRelativeInterval":"' + relativeinterval + '",';
    jsonrequest += '"FreqRecurrenceFactor":"' + recurrencefactor + '",';
    jsonrequest += '"StartDate":' + start_date + ',';
    jsonrequest += '"StartTime":' + start_time;
    jsonrequest += '}';
    var request = CallAjax("/manager/schedule/" + id_Schedule, jsonrequest, "", "PUT");
    return request;
};
/**************************************************************************/
/*                                                                        */
/*                                  功能                                  */
/*                                                                        */
/**************************************************************************/
// 1. datepicker和timepicker表格產生  2017.01.17 william 
function CreateDatepicker() {
    $("#datepicker_schedule,#datepicker_schedulebk").datepicker({
        // 可使用下拉式選單 - 月份
        changeMonth: true,
        // 可使用下拉式選單 - 年份
        changeYear: true,
        yearRange: "2017:2099",
        // 設定 下拉式選單月份 在 年份的後面
        showMonthAfterYear: true,
        dateFormat: "yy-mm-dd",
        // appendText: "(yyyy-mm-dd)",
        showOn: "both",
        buttonImageOnly: true,
        //buttonImage: "img/calendar.gif",
        showOn: 'focus', // button -> focus
        buttonText: "Calendar",
        // 不能選擇之前的日期
        minDate: dateToday,
    });

    $('#timepicker_schedule,#timepicker_schedulebk').timepicker({
        "timeFormat": "HH:mm" //只有 時、分、秒 用 timepicker
    });

    // $('.ui-slider-handle').css({"width":"0.6em"});    
    $('.ui-datepicker').css({ "font-family": "Times New Roman" });
    // : "Times New Roman","PMingLiU","新細明體";
}
// 2. datepicker和timepicker dialog的起始位置  2017.01.17 william 
function elementlocation() {
    $('.datepicker_schedule_2').click(function () {            
        var popup = $(this).offset();
        var popupTop = popup.top - 0;
        var popupLeft = popup.left + 120;
        $('.ui-datepicker').css({
            'top': popupTop,
            'left': popupLeft
        });
        if($('.ui-datepicker-today').hasClass('ui-datepicker-current-day')) {
            $('.ui-datepicker-today>.ui-state-default').removeClass().addClass("calendarBtn_for_today");
        }        
    });

    $('.timepicker_schedule_2').click(function () {
        var popup = $(this).offset();
        var popupTop = popup.top - 0;
        var popupLeft = popup.left + 125;
        $('.ui-datepicker').css({
            'top': popupTop,
            'left': popupLeft
        });
    });

    $('.ui-datepicker-trigger').click(function () {
        var popup = $(this).offset();
        var popupTop = popup.top - 2.5;
        var popupLeft = popup.left + 142.5;
        $('.ui-datepicker').css({
            'top': popupTop,
            'left': popupLeft
        });
    });
};
// 3. 排程類型使用radion-切換不同的DIV頁籤 2017.01.17 william 
var radChecked = function () {
    var val = $('input[name="radMethod"]:checked').val();
    // show顯示-hidden隱藏所選區域
    $('.methodDiv').hide();
    $('#div_' + val).show();
};

var radCheckedbk = function () {
    var valbk = $('input[name="radMethodbk"]:checked').val();
    // show顯示-hidden隱藏所選區域
    $('.methodDivbk').hide();
    $('#div_bk' + valbk).show();
};
// 4. 取日期值並轉換INT 2017.02.13 william 
function GetDateValue(data) {
    var datavalue, datavalue_return;
    datavalue = data.substr(0, 4);
    datavalue += data.substr(5, 2);
    datavalue += data.substr(8, 2);
    datavalue_return = parseInt(datavalue);
    return datavalue_return;
}
// 5. 取時間值並轉換INT 2017.02.13 william 
function GetTimeValue(time) {
    var timevalue, timevalue_return;
    timevalue = time.substr(0, 2);
    timevalue += time.substr(3, 2);
    // timevalue += '00';
    timevalue_return = parseInt(timevalue) * 100;
    return timevalue_return;
}

function ConvertDateValue(data) {
    var datavalue, datavalue_return;
    datavalue = data.toString();
    datavalue_return = datavalue.substr(0, 4);
    datavalue_return += '-';
    datavalue_return += datavalue.substr(4, 2);
    datavalue_return += '-';
    datavalue_return += datavalue.substr(6, 2);

    return datavalue_return;
}
function ConvertTimeValue(time) {
    var timevalue, timevalue_return;
    timevalue = time.toString();
    timevalue = padLeft(timevalue, 6);
    timevalue_return = timevalue.substr(0, 2);
    timevalue_return += ':';
    timevalue_return += timevalue.substr(2, 2);

    return timevalue_return;
}
// 補0使用 2017.02.13 william 
function padLeft(str, cc) {
    if (str.length >= cc)
        return str;
    else
        return padLeft(0 + str, cc);
}
// 快照修改日期
function isWeekchecked(y) {
    var w = y.length;
    for (var i = 0; i <= w; i++) {
        if (y.substr(i, 1) == 1) {
            $('input[name="user_active_Week"][id="FreqInterval_Week_' + i + '"]').prop('checked', true);
        }
    }
}

function isMonthchecked(y) {
    var w = y.length;
    for (var i = 0; i <= w; i++) {
        if (y.substr(i, 1) == 1) {
            $('input[name="user_active_Month"][id="FreqInterval_Month_' + i + '"]').prop('checked', true);
        }
    }
}

function isWeekRchecked(y) {
    var w = y.length;
    for (var i = 0; i <= w; i++) {
        if (y.substr(i, 1) == 1) {
            $('input[name="user_active_WR"][id="FreqInterval_WR_' + i + '"]').prop('checked', true);
        }
    }
}

function isMonthRchecked(w) {
    var a = w.length;
    for (var i = 0; i <= a; i++) {
        if (w.substr(i, 1) == 1) {
            $('input[name="user_active_MR"][id="FreqInterval_MR_' + i + '"]').prop('checked', true);
        }
    }
}

// 備份修改日期
function isWeekbkchecked(y) {
    var w = y.length;
    for (var i = 0; i <= w; i++) {
        if (y.substr(i, 1) == 1) {
            $('input[name="user_active_Weekbk"][id="FreqInterval_Weekbk_' + i + '"]').prop('checked', true);
        }
    }
}

function isMonthbkchecked(y) {
    var w = y.length;
    for (var i = 0; i <= w; i++) {
        if (y.substr(i, 1) == 1) {
            $('input[name="user_active_Monthbk"][id="FreqInterval_Monthbk_' + i + '"]').prop('checked', true);
        }
    }
}

function isWeekRbkchecked(y) {
    var w = y.length;
    for (var i = 0; i <= w; i++) {
        if (y.substr(i, 1) == 1) {
            $('input[name="user_active_WRbk"][id="FreqInterval_WRbk_' + i + '"]').prop('checked', true);
        }
    }
}

function isMonthRbkchecked(w) {
    var a = w.length;
    for (var i = 0; i <= a; i++) {
        if (w.substr(i, 1) == 1) {
            $('input[name="user_active_MRbk"][id="FreqInterval_MRbk_' + i + '"]').prop('checked', true);
        }
    }
}

// 建立排程後清除表單內的資料 2017.02.13 william 
function ResetAllSchedule() {
    var $dates = $('#datepicker_schedule, #timepicker_schedule').datepicker();
    $dates.datepicker('setDate', null);
    // $dates.datetimepicker("setDate", null);
    $('input[name="radMethod"]').attr('checked', false);
    // $('input[name="radMethod"]').prop('checked', false);
    $('input[name="user_active_Week"]').prop('checked', false);
    $('input[name="user_active_Month"]').prop('checked', false);
    $('input[name="user_active_MR"]').prop('checked', false);
    $('input[name="user_active_WR"]').prop('checked', false);
    $('#cs_name_input').val('');
    $('#cs_desc_input').val('');
    $('.methodDiv').hide();

    var $dates2 = $('#datepicker_schedulebk, #timepicker_schedulebk').datepicker();
    $dates2.datepicker('setDate', null);
    $('input[name="radMethodbk"]').attr('checked', false);
    $('input[name="user_active_Weekbk"]').prop('checked', false);
    $('input[name="user_active_Monthbk"]').prop('checked', false);
    $('input[name="user_active_MRbk"]').prop('checked', false);
    $('input[name="user_active_WRbk"]').prop('checked', false);
    $('#csbk_name_input').val('');
    $('#csbk_desc_input').val('');
    $('.methodDivbk').hide();
}

function SortList(eSortDirection, eSortType, sSortVariable, List) {
    switch (eSortDirection) {
        case SortDirection.ASC:
            switch (eSortType) {
                case SortType.Num:
                    List.sort(function (a, b) {
                        return (a[sSortVariable] - b[sSortVariable]);
                    });
                    break;
                case SortType.String:
                    List.sort(function (a, b) {
                        return a[sSortVariable].localeCompare(b[sSortVariable]);
                    });
                    break;
            }
            break;

        case SortDirection.Desc:
            switch (eSortType) {
                case SortType.Num:
                    List.sort(function (a, b) {
                        return (b[sSortVariable] - a[sSortVariable]);
                    });
                    break;
                case SortType.String:
                    List.sort(function (a, b) {
                        return b[sSortVariable].localeCompare(a[sSortVariable]);
                    });
                    break;
            }
            break;
    }
}

/*=========================== Snapshot快照 Start ===========================*/
// 綁定清單的下拉式按鈕 2017.02.13 william 
function RebindScheduleActionButtonEvent(btn_element) {
    if (btn_element.hasClass('ScheduleDelete')) { /* Schedule 動作下拉式清單-刪除 2017.02.10 william */
        idSchedule_temparr.push(idSchedule);
        DeleteScheduleSnapshot(idSchedule_temparr);
    }
    else if (btn_element.hasClass('ScheduleJobs')) { /* Schedule 動作下拉式清單-Jobs 2017.02.10 william */
        // alert(idJob); 
        $('#autoss_Previous').hide();        
        $('#autoss_Confirm').hide();         
        $('#autoss_Modify').show();       
        JobsID = idJob;
        GetJobsForCreateTableList(idJob);
        $('#autoss_ListBottom').empty();
        $('#autoss_ListJobBottom').empty();
        $("#dialog_autoss").dialog('open');
    }
    else if (btn_element.hasClass('ScheduleRule')) { /* Schedule 動作下拉式清單-Rule 2017.02.10 william */
        ScheduleInfo(valueSchedule);
        $('#btn_create_schedule_Confirm').hide();
        $('#btn_create_schedule_Modify').show();
        $("#dialog_create_schedule").dialog('open');
    }
}
// 選取式(checkbox)按鈕實作按鈕 2017.02.13 william 
function InitScheduleDeleteButton() {
    //    var delete_ask = LanguageStr_VM.VM_Delete.replace("@",VMorVDI);
    var delete_ask = LanguageStr_Service.Service_Schedule_DeleteAsk;
    $('#btn_scdelete_all_vm').click(function (event) {
        event.preventDefault();
        var confirmable = confirm(delete_ask);
        if (confirmable === true) {
            DeleteSelectSchedule('iCheckBoxScheduleSelect');
        }
    });
}

function DeleteSelectSchedule(checkboxclass) {        // ,ListidSchedule
    var idSchedulearr = [];
    $.each($('.' + checkboxclass + ':checked'), function (index, element) {
        idSchedulearr.push($(element).attr('id'));
    });
    if (idSchedulearr.length > 0)
        DeleteScheduleSnapshot(idSchedulearr);
}

function InitScheduleJobsButton() {
    var jobs_ask = LanguageStr_Service.Service_Schedule_JobsAsk;


    $('#btn_scjobs_all_vm').click(function (event) {
        event.preventDefault();
        if ($('.iCheckBoxScheduleSelect:checked').length > 1) {
            alert(jobs_ask);
        }
        else {
            $('#autoss_Previous').hide();        
            $('#autoss_Confirm').hide();         
            $('#autoss_Modify').show();    
            JobsSelectSchedule('iCheckBoxScheduleSelect');
        }

    });
}

function JobsSelectSchedule(checkboxclass) {
    var idJob_temp;
    $.each($('.' + checkboxclass + ':checked'), function (index, element) {
        idJob_temp = $(element).attr('href');
    });
    JobsID = idJob_temp;    
    GetJobsForCreateTableList(idJob_temp);
    $('#autoss_ListBottom').empty();
    $('#autoss_ListJobBottom').empty();
    $("#dialog_autoss").dialog('open');
}

function InitScheduleRuleButton() {
    var rule_ask = LanguageStr_Service.Service_Schedule_RuleAsk;
    $('#btn_scrule_all_vm').click(function (event) {
        event.preventDefault();
        if ($('.iCheckBoxScheduleSelect:checked').length > 1) {
            alert(rule_ask);
        }
        else {
            RuleSelectSchedule('iCheckBoxScheduleSelect');
        }
    });
}

function RuleSelectSchedule(checkboxclass) {
    var idSchedule_temp;
    $.each($('.' + checkboxclass + ':checked'), function (index, element) {
        // idSchedule_temp = parseInt($(element).attr('value'));
        idSchedule_temp = $(element).attr('value');
        idSchedule = $(element).attr('id');
    });
    ScheduleInfo(idSchedule_temp);
    $('#btn_create_schedule_Confirm').hide();
    $('#btn_create_schedule_Modify').show();
    $("#dialog_create_schedule").dialog('open');
}


function AutossClickSort() {
    $('.ScheduleHeader').click(function (event) {
        // StopPolling();
        event.preventDefault();
        var th = $(this);
        var eSortType = SortType.String;
        var eSortDirection = SortDirection.ASC;
        // if(th.attr('id') === "state" || th.attr('id') === "online" || th.attr('id') === "ram" || th.attr('id') === "suspend")
        //     eSortType = SortType.Num;
        if (th.hasClass('asc')) {
            $('.ScheduleHeader').removeClass('asc');
            $('.ScheduleHeader').removeClass('desc');
            $('.label_autoss_arrow').html('');
            $('#autoss' + th.attr('id') + '1').html('&#9660;');
            //                        th.removeClass('asc');
            th.addClass('desc');
            eSortDirection = SortDirection.Desc;
        }
        else {
            $('.ScheduleHeader').removeClass('asc');
            $('.ScheduleHeader').removeClass('desc');
            $('.label_autoss_arrow').html('');
            $('#autoss' + th.attr('id') + '1').html('&#9650;');
            //th.removeClass('desc');
            th.addClass('asc');
            eSortDirection = SortDirection.ASC;
        }
        NowSortSnapshotScheduleType = eSortType;
        NowSortSnapshotScheduleDirection = eSortDirection;
        var sort_id = th.attr('id');
        // if(sort_id=='suspend'){
        //     if(!($('.iCheckBoxVMSuspend:checked').length !=0 && $('.iCheckBoxVMSuspend:checked').length != $('.iCheckBoxVMSuspend').length)){
        //         StartPolling(true,true);
        //         return;
        //     }
        // }
        if (th.attr('id') === 'ScheduleName')
            sort_id = 'Name';
        if (th.attr('id') === 'ScheduleDesc')
            sort_id = 'Desc';
        if (th.attr('id') === 'ScheduleCreateTime')
            sort_id = 'CreateTime';             
        NowSortSnapshotScheduleDOMID = sort_id;
        SortList(eSortDirection, eSortType, sort_id, ScheduleList);
        $('#Div_Service_Snapshot_List').html(CreateListScheduleHtml());
        // GetScheduleId();
        // GetScheduleValue();  
        // GetJobId();                                                        
        BindDropDownBtn();
        // $('.btn-operation-vm').click(function(event){                               
        //     event.preventDefault();
        //     var btn_element = $(this);
        //     vmname = btn_element.data('operation');
        //     ChangeVMNametoID(vmname);
        // });
        // $('.iCheckBoxAllSelect').iCheck('uncheck');
        // $('.iCheckBoxVMSelect').iCheck({  
        //     checkboxClass: 'icheckbox_minimal-blue'    
        // });
        // InitRowSuspendCheck('iCheckBoxVMSuspend');                                     
        // EnableDisableSelectVMActionButton();
        // $('.iCheckBoxVMSelect').on('ifChanged', function(event){                            
        //     EnableDisableSelectVMActionButton();
        // });                    
        // StartPolling(true,true);
    });
}
/*=========================== Snapshot快照 End ===========================*/
/*=========================== Backup備份 Start ===========================*/
// 綁定清單的下拉式按鈕 2017.02.13 william 
function RebindBKScheduleActionButtonEvent(btn_element) {
    if (btn_element.hasClass('BKScheduleDelete')) { /* Schedule 動作下拉式清單-刪除 2017.02.10 william */
        idSchedule_temparr.push(idSchedule);
        DeleteScheduleBackup(idSchedule_temparr);
    }
    else if (btn_element.hasClass('BKScheduleJobs')) { /* Schedule 動作下拉式清單-Jobs 2017.02.10 william */
        // alert(idJob); 
        $('#autobk_Previous').hide();     
        $('#autobk_Confirm').hide();          
        $('#autobk_Modify').show();  
        JobsID = idJob;        
        GetBKJobsForCreateTableList(idJob);
        $('#autobk_ListBottom').empty();
        $('#autobk_ListJobBottom').empty();
        $("#dialog_autobk").dialog('open');
    }
    else if (btn_element.hasClass('BKScheduleRule')) { /* Schedule 動作下拉式清單-Rule 2017.02.10 william */
        BKScheduleInfo(valueSchedule);
        $('#btn_create_bkschedule_Confirm').hide();
        $('#btn_create_bkschedule_Modify').show();
        $("#dialog_create_bkschedule").dialog('open');
    }
}
// 選取式(checkbox)按鈕實作按鈕 2017.02.13 william 
function InitBackupScheduleDeleteButton() {
    //    var delete_ask = LanguageStr_VM.VM_Delete.replace("@",VMorVDI);
    var delete_ask = LanguageStr_Service.Service_Schedule_DeleteAsk;
    $('#btn_scbkdelete_all_vm').click(function (event) {
        event.preventDefault();
        var confirmable = confirm(delete_ask);
        if (confirmable === true) {
            DeleteSelectBackupSchedule('iCheckBoxBackupScheduleSelect');
        }
    });
}

function DeleteSelectBackupSchedule(checkboxclass) {        // ,ListidSchedule
    var idSchedulearr = [];
    $.each($('.' + checkboxclass + ':checked'), function (index, element) {
        idSchedulearr.push($(element).attr('id'));
    });
    if (idSchedulearr.length > 0)
        DeleteScheduleBackup(idSchedulearr);
}

function InitBackupScheduleJobsButton() {
    var jobs_ask = LanguageStr_Service.Service_Schedule_JobsAsk;

    $('#btn_scbkjobs_all_vm').click(function (event) {
        event.preventDefault();
        if ($('.iCheckBoxBackupScheduleSelect:checked').length > 1) {
            alert(jobs_ask);
        }
        else {
            $('#autobk_Previous').hide();     
            $('#autobk_Confirm').hide();          
            $('#autobk_Modify').show();               
            JobsSelectBackupSchedule('iCheckBoxBackupScheduleSelect');
        }
    });
}

function JobsSelectBackupSchedule(checkboxclass) {
    var idJob_temp;
    $.each($('.' + checkboxclass + ':checked'), function (index, element) {
        idJob_temp = $(element).attr('href');
    });
    JobsID = idJob_temp;    
    GetBKJobsForCreateTableList(idJob_temp);
    $('#autobk_ListBottom').empty();
    $('#autobk_ListJobBottom').empty();
    $("#dialog_autobk").dialog('open');
}

function InitBackupScheduleRuleButton() {
    var rule_ask = LanguageStr_Service.Service_Schedule_RuleAsk;
    $('#btn_scbkrule_all_vm').click(function (event) {
        event.preventDefault();
        if ($('.iCheckBoxBackupScheduleSelect:checked').length > 1) {
            alert(rule_ask);
        }
        else {
            RuleSelectBackupSchedule('iCheckBoxBackupScheduleSelect');
        }
    });
}

function RuleSelectBackupSchedule(checkboxclass) {
    var idSchedule_temp;
    $.each($('.' + checkboxclass + ':checked'), function (index, element) {
        idSchedule_temp = $(element).attr('value');
        idSchedule = $(element).attr('id');
    });
    BKScheduleInfo(idSchedule_temp);
    $('#btn_create_bkschedule_Confirm').hide();
    $('#btn_create_bkschedule_Modify').show();
    $("#dialog_create_bkschedule").dialog('open');
}

function AutobkClickSort() {
    $('.ScheduleBackupHeader').click(function (event) {
        // StopPolling();
        event.preventDefault();
        var th = $(this);
        var eSortType = SortType.String;
        var eSortDirection = SortDirection.ASC;
        // if(th.attr('id') === "state" || th.attr('id') === "online" || th.attr('id') === "ram" || th.attr('id') === "suspend")
        //     eSortType = SortType.Num;
        if (th.hasClass('asc')) {
            $('.ScheduleBackupHeader').removeClass('asc');
            $('.ScheduleBackupHeader').removeClass('desc');
            $('.label_autobk_arrow').html('');
            $('#autobk' + th.attr('id') + '1').html('&#9660;');
            //                        th.removeClass('asc');
            th.addClass('desc');
            eSortDirection = SortDirection.Desc;
        }
        else {
            $('.ScheduleBackupHeader').removeClass('asc');
            $('.ScheduleBackupHeader').removeClass('desc');
            $('.label_autobk_arrow').html('');
            $('#autobk' + th.attr('id') + '1').html('&#9650;');
            //th.removeClass('desc');
            th.addClass('asc');
            eSortDirection = SortDirection.ASC;
        }
        NowSortBackupScheduleType = eSortType;
        NowSortBackupScheduleDirection = eSortDirection;
        var sort_id = th.attr('id');
        // if(sort_id=='suspend'){
        //     if(!($('.iCheckBoxVMSuspend:checked').length !=0 && $('.iCheckBoxVMSuspend:checked').length != $('.iCheckBoxVMSuspend').length)){
        //         StartPolling(true,true);
        //         return;
        //     }
        // }
        if (th.attr('id') === 'ScheduleBackupName')
            sort_id = 'Name';
        if (th.attr('id') === 'ScheduleBackupDesc')
            sort_id = 'Desc';
        if (th.attr('id') === 'ScheduleBackupCreateTime')
            sort_id = 'CreateTime';            
        NowSortBackupScheduleDOMID = sort_id;
        SortList(eSortDirection, eSortType, sort_id, BackupScheduleList);
        $('#Div_Service_BackupSchedule_List').html(CreateListBackupScheduleHtml());
        // GetScheduleId();
        // GetScheduleValue();  
        // GetJobId();                                                        
        BindDropDownBtn();
        // $('.btn-operation-vm').click(function(event){                               
        //     event.preventDefault();
        //     var btn_element = $(this);
        //     vmname = btn_element.data('operation');
        //     ChangeVMNametoID(vmname);
        // });
        // $('.iCheckBoxAllSelect').iCheck('uncheck');
        // $('.iCheckBoxVMSelect').iCheck({  
        //     checkboxClass: 'icheckbox_minimal-blue'    
        // });
        // InitRowSuspendCheck('iCheckBoxVMSuspend');                                     
        // EnableDisableSelectVMActionButton();
        // $('.iCheckBoxVMSelect').on('ifChanged', function(event){                            
        //     EnableDisableSelectVMActionButton();
        // });                    
        // StartPolling(true,true);
    });
}

/*=========================== Backup備份 End ===========================*/