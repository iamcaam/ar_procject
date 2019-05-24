// https://www.w3schools.com/howto/howto_js_list_grid_view.asp

// Get the elements with class
var elements_no     = document.getElementsByClassName("no_user");
var elements_user   = document.getElementsByClassName("column_user");
var elements_enable = document.getElementsByClassName("userrnable");
var user_index; // Declare a loop variable
var AllUserList; 
var UserDetailList;
var GetUserID;
var UserID;
var UserEnableState = -1;
var show = false;
var user_id_data;
var IsUserEnable;
var NoFreshUI = false;
var LastUserData = false;
var UserCreateOrModify;
var temp_username;
var temp_raidid = 0;
var temp_disksize_forModify = 0;
// var ElementCssClass = document.getElementById("Target").className;

function InitUserMgt() {    
    InitEnableButton();
    Dialog_MoveUserDisk();
    InitButtonForMoveUserDisk();
    InitMoveUserDiskButton();
    ListViewColumnSize(); 
    CreateDialog_UserCreate();
    CreateDialog_UserProfileCreate();
    ModifyDialog_UserProfileCreate();
    InitUserCreateCombo();
    InitUserProfileCreateCombo();
    InitUserProfileModifyCombo();

    InitButtonForCreateUser();    
    InitUserCreatButton();
    InitUserDeleteButton();
    InitButtonForCreateUserProfile();
    InitUserProfileDeleteButton();
    InitButtonForModifyUserProfile();
    InitUserProfileCreatButton();
    InitUserProfileModifyButton();
    InitResetPWBtn();            
    InitUserEnableCheckbox();
    refreshUserListStatus();
    refreshUserProfileStatus();
    InitUserModifyButton();
    $('#userenable_icheck').iCheck({  
        checkboxClass: 'icheckbox_minimal-blue'    
    });      
    $('#tab-user-container').easytabs('select', '#tabs1-user_disk');   
    
    if(browserNeedChanage){        
        var div = document.getElementById ("listview_content");                        
        if (div.addEventListener) {    
            // div.removeEventListener("DOMAttrModified", OnDomAttrChanged, false);            

            // div.addEventListener("DOMAttrModified", OnDomAttrChanged, false);    
            // div.addEventListener ('overflow', OnOverflowChanged, false);
            // div.addEventListener ('underflow', UnderOverflowChanged, false);
        }    
    }

    $('#input_UserName').on("input", function () {            
        if($('#input_UserName').val().length ==0)    
            $('#btn_create_user').button( "disable" );
        else
            $('#btn_create_user').button( "enable" );  
    });         
    limitText( $('#input_UserName'), 32);    
}


function UserEnableBtnStyle(status) {
        switch (status) {
            case 0: 
                $('#btn_disable_user').css({ "border": "1px solid #add", "background": "#3ec6e5", "font-weight": "normal", "color": "#ffffff" });
                $('#btn_enable_user').css({ "border": "1px solid #91979c", "background": "#b3c5c5", "font-weight": "normal", "color": "#ffffff" });
                $("#btn_disable_user")
                    .mouseover(function () {
                        $(this).css("background", "-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #378de5), color-stop(1, #79bbff) )");
                        $(this).css("background", "-moz-linear-gradient( center top, #378de5 5%, #79bbff 100% )");
                        $(this).css("filter", "progid:DXImageTransform.Microsoft.gradient(startColorstr='#378de5', endColorstr='#79bbff')");
                        $(this).css("background-color", "#378de5");
                        $(this).css("color", "#ffffff");
                    })
                    .mouseout(function () {
                        $(this).css({ "border": "1px solid #add", "background": "#3ec6e5", "font-weight": "normal", "color": "#ffffff" });
                    });
                $("#btn_enable_user")
                    .mouseover(function () {
                        $(this).css({ "border": "1px solid #91979c", "background": "#b3c5c5", "font-weight": "normal", "color": "#ffffff" });
                    })
                    .mouseout(function () {
                        $(this).css({ "border": "1px solid #91979c", "background": "#b3c5c5", "font-weight": "normal", "color": "#ffffff" });
                    });
                break;
            case 1:
                $('#btn_enable_user').css({ "border": "1px solid #add", "background": "#3ec6e5", "font-weight": "normal", "color": "#ffffff" });
                $('#btn_disable_user').css({ "border": "1px solid #91979c", "background": "#b3c5c5", "font-weight": "normal", "color": "#ffffff" });
                $("#btn_enable_user")
                    .mouseover(function () {
                        $(this).css("background", "-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #378de5), color-stop(1, #79bbff) )");
                        $(this).css("background", "-moz-linear-gradient( center top, #378de5 5%, #79bbff 100% )");
                        $(this).css("filter", "progid:DXImageTransform.Microsoft.gradient(startColorstr='#378de5', endColorstr='#79bbff')");
                        $(this).css("background-color", "#378de5");
                        $(this).css("color", "#ffffff");
                    })
                    .mouseout(function () {
                        $(this).css({ "border": "1px solid #add", "background": "#3ec6e5", "font-weight": "normal", "color": "#ffffff" });
                    });
                $("#btn_disable_user")
                    .mouseover(function () {
                        $(this).css({ "border": "1px solid #91979c", "background": "#b3c5c5", "font-weight": "normal", "color": "#ffffff" }); 
                    })
                    .mouseout(function () {
                        $(this).css({ "border": "1px solid #91979c", "background": "#b3c5c5", "font-weight": "normal", "color": "#ffffff" }); 
                    });
                break;
        }
}

// 取List值
function getListValue() {
    $(".userdata_row > .column_user").click(function(event) {
        var btn_element = $(this);
        //do something
        $(".userdata_row").removeClass("MenuButtonClick");
        btn_element.parent(".userdata_row").addClass("MenuButtonClick");        
        $('#userenable_icheck').iCheck('uncheck');

        user_id_data = btn_element.attr('id').split(":");                    
        GetUserID    = user_id_data[0];                    
        IsUserEnable = user_id_data[1];  

        if(IsUserEnable == 1) {
            $('#userenable_icheck').iCheck('uncheck');
            $('#label_user_status').text(LanguageStr_langSysCfgUserMgt.Disable);
            $('#label_user_status').css("color", "red");
        } else if(IsUserEnable == 0) {
            $('#userenable_icheck').iCheck('check');
            $('#label_user_status').text(LanguageStr_langSysCfgUserMgt.Enable);
            $('#label_user_status').css("color", "green");
        }

        UserEnableBtnStyle(parseInt(IsUserEnable));

        temp_username = btn_element.text();
        UserID        = GetUserID;
        GetUserDetailForCreateTableList(UserID);
        
        $('#user_disk_info_username').text(btn_element.text());
        $('#label_user_status_name').text(btn_element.text());   
    });    
}

function ListViewColumnSize() {
    for (user_index = 0; user_index < elements_user.length; user_index++) {
        elements_no[user_index].style.width     = "15%";
        elements_user[user_index].style.width   = "70%";
        elements_enable[user_index].style.width = "15%";
    }
}
/**************************************************************************/
/*                                                                        */
/*                               產生排程相關畫面                           */
/*                                                                        */
/**************************************************************************/

/******************************* 列出使用者清單 *******************************/
// 1-1. Get all user list data
function GetUserDataForCreateTableList() {
    $('.Div_WaitAjax').dialog( 'open' );
    var request = Ajax_GetUserList();
    request.done(function (AjaxData, statustext, jqXHR) {        
        UserListToUI(AjaxData);
        $('.Div_WaitAjax').dialog('close');
    });
    request.fail(function (jqXHR, textStatus) {
        $('.Div_WaitAjax').dialog('close');      
      
        if(jqXHR.status == 400) {

        } else if(jqXHR.status == 409) {
                
        } else if(jqXHR.status == 500) {
            alert(LanguageStr_langSysCfgUserMgt.CreateRAID_alert);                  
        } else{

        }        
    });
}
// 1-2. 定義class承接json資料
function UserListToUI(JsonData) {
    var HtmlStr = "";
    var uc;
    AllUserList   = [];

    $.each(JsonData, function (index, element) {
        uc = new UserClass(element['ID'], element['Name'], element['isAdmin'], element['State']);
        AllUserList.push(uc);
    });

    CreateUserListHtml();
}
// 1-3. 將所有User相關資料List出來
function CreateUserListHtml() {
    var HtmlStr = "";
    var no = 1;
    
    // 判斷使用者個數
    if(AllUserList.length == 0) {
        $('#userenable_icheck').iCheck('uncheck');
        UserProfileInfoClear();
    } else if(AllUserList.length == 1) {
        UserProfileInfoClear();          
        LastUserData = true;
        NoFreshUI    = false;
    } else if(AllUserList.length > 1) {
        LastUserData = false;
    }
             
    $.each(AllUserList, function (index, element) {
        HtmlStr += '<div class="userdata_row" onresize = "OnOverflowChanged(event)">';
        HtmlStr += '<div class="no_user" style="font-size: 12px; line-height: 27px;font-weight: 900;">' + no + '</div>';
        if(no == 1 && !NoFreshUI) {
            HtmlStr += '<div class="column_user" id="' + element['id'] + ":" + element['state'] + '" title="' + element['name'] + '" style="font-size: 12px; line-height: 27px;font-weight: 900;">' + element['name'] + '</div>';
            temp_username = element['name'];
        } else if(temp_username == element['name'] && NoFreshUI) {
            HtmlStr += '<div class="column_user" id="' + element['id'] + ":" + element['state'] + '" title="' + element['name'] + '" style="font-size: 12px; line-height: 27px;font-weight: 900;">' + element['name'] + '</div>';
            temp_username = element['name'];
        } else {
            HtmlStr += '<div class="column_user" id="' + element['id'] + ":" + element['state'] + '" title="' + element['name'] + '" style="font-size: 12px; line-height: 27px;font-weight: 900;">' + element['name'] + '</div>';
        }            
        if(element['state']==1) {
            HtmlStr += '<div class="userrnable" style="color: red;font-size: 12px; line-height: 27px;font-weight: 900;">' + LanguageStr_langSysCfgUserMgt.Disable + '</div>';
        } else if(element['state']==0) {
            HtmlStr += '<div class="userrnable" style="color: green;font-size: 12px; line-height: 27px;font-weight: 900;">' + LanguageStr_langSysCfgUserMgt.Enable + '</div>';
        }                
        HtmlStr += '</div>';
        
        // 抓第一筆user資料，刷使用者磁碟的畫面並取user id
        if(no == 1 && !NoFreshUI) {
            $('#userenable_icheck').iCheck('uncheck');    
            UserProfileInfoClear();

            if(element['state'] == 1) {
                $('#userenable_icheck').iCheck('uncheck');
                $('#label_user_status').text(LanguageStr_langSysCfgUserMgt.Disable);
                $('#label_user_status').css("color", "red");
            } else if(element['state'] == 0) {
                $('#userenable_icheck').iCheck('check');
                $('#label_user_status').text(LanguageStr_langSysCfgUserMgt.Enable);
                $('#label_user_status').css("color", "green");
            }                

            UserEnableBtnStyle(element['state']);
            GetUserDetailForCreateTableList(element['id']);
            UserID = element['id'];
            $('#user_disk_info_username').text(element['name']);
            $('#label_user_status_name').text(element['name']);            
        }

        no++;
    });
    $('#listview_content').empty();    
    $('#listview_content').html(HtmlStr);
    if(!NoFreshUI) {
        $("#listview_content>.userdata_row:eq(0)").addClass('MenuButtonClick');
    }
    if(AllUserList.length > 0){
        setTimeout( function(){
        // alert($('#listview_content>.userdata_row:eq(0)').outerWidth());
        // alert(element.offsetWidth);
        var totalWidth = $('#listview_content>.userdata_row:eq(0)').outerWidth();
        nowUserWidth = totalWidth;
        var noWidth = totalWidth*0.15;
        var nameWidth = totalWidth*0.70;
        var userrnableWidth = totalWidth*0.15 - 5;
        // alert(totalWidth*0.1);
        $('.listview_title > .row_title > .no_user').outerWidth(noWidth);
        $('.listview_title > .row_title > .column_user').outerWidth(nameWidth);
        $('.listview_title > .row_title > .userrnable').outerWidth(userrnableWidth); },200); 
    }   
    
    getListValue(); // 刷畫面後，重新bind取user data的功能
};

function OnOverflowChanged(){            
    // var noWidth = $('#listview_content .no_user:eq(0)').outerWidth();
    // $('.listview_title > .row_title > .no_user').outerWidth(noWidth);
    // var nameWidth = $('#listview_content .column_user:eq(0)').innerWidth();
    // var nameOuterWidth = $('#listview_content .column_user:eq(0)').outerWidth();
    // alert("Inner:"+nameWidth);
    // alert("Outer:"+nameOuterWidth);
    // $('.listview_title > .row_title > .column_user').outerWidth(nameOuterWidth);
    // var userrnableWidth = $('#listview_content .userrnable:eq(0)').outerWidth();
    // $('.listview_title > .row_title > .userrnable').outerWidth(userrnableWidth);
    // alert(1);
    // var element = $('#listview_content');
    // if (element.offsetHeight < element.scrollHeight ||
    //     element.offsetWidth < element.scrollWidth) {
    //     alert('overflow');
    // } else {
    //     alert('no overflow');
    // }
    //             
    setTimeout( function(){var element = document.getElementById ("listview_content");
    // alert($('#listview_content>.userdata_row:eq(0)').outerWidth());
    // alert(element.offsetWidth);
    var totalWidth = element.scrollWidth;
    var noWidth = totalWidth*0.15;
    var nameWidth = totalWidth*0.70;
    var userrnableWidth = totalWidth*0.15;
    // alert(totalWidth);
    $('.listview_title > .row_title > .no_user').outerWidth(noWidth);
    $('.listview_title > .row_title > .column_user').outerWidth(nameWidth);
    $('.listview_title > .row_title > .userrnable').outerWidth(userrnableWidth); },40);                
}

var refreshIntervalId;
function UnderOverflowChanged(){
    // setTimeout( function(){var element = document.getElementById ("listview_content");
    // alert($('#listview_content>.userdata_row:eq(0)').outerWidth());
    // alert(element.offsetWidth);    
    // var i = 0;        
    // if (typeof (refreshIntervalId) !== 'undefined')
    //     clearInterval(refreshIntervalId);
    refreshIntervalId = setInterval(checkWidth, 200);
     
// },500);    
}

function stopUserListWidthInterval(){
    if (typeof (refreshIntervalId) !== 'undefined')
        clearInterval(refreshIntervalId);
}

function checkWidth(){    
    if(AllUserList.length > 0){
        var totalWidth = $('#listview_content>.userdata_row:eq(0)').outerWidth();
        if(nowUserWidth != totalWidth){
            // alert('change');
            nowUserWidth = totalWidth;
            var noWidth = totalWidth*0.15;
            var nameWidth = totalWidth*0.70;
            var userrnableWidth = totalWidth*0.15 - 5;
            // alert(totalWidth*0.1);
            $('.listview_title > .row_title > .no_user').outerWidth(noWidth);
            $('.listview_title > .row_title > .column_user').outerWidth(nameWidth);
            $('.listview_title > .row_title > .userrnable').outerWidth(userrnableWidth);
            clearInterval(refreshIntervalId);
        }
    }else{
        clearInterval(refreshIntervalId);
    }    
    
}

function OnDomAttrChanged(){
    // alert("OnOverflowChanged");
    // var element = document.getElementById ("listview_content");
    // alert(element.offsetWidth);
    // alert(element.scrollWidth);
    // if (element.offsetHeight < element.scrollHeight ||
    //     element.offsetWidth < element.scrollWidth) {
    //     alert('overflow');
    // } else {
    //     alert('no overflow');
    // }
    // alert('attr Chanage');    
    UnderOverflowChanged();   
}

/******************************* 建立使用者 *******************************/
// 2-1. Create User dialog
function CreateDialog_UserCreate() {
    $("#dialog_create_user").dialog({
        title: LanguageStr_langSysCfgUserMgt.Create_User,
        modal: true,
        resizable: false,
        draggable: true,
        autoOpen: false,
        height: 350,
        width: 500,
        dialogClass: "no-close",
        closeOnEscape: false
    });
}
// 2-2. open dialog
function InitButtonForCreateUser(){
    $('#btn_create_user_dialog').click(function(event){
        event.preventDefault();
        ResetValueUserCreateDialog();
        UserCreateOrModify = 0;
        InitUserCreateOrModify();
        $( "#dialog_create_user" ).dialog('open');
        $('#btn_create_user').button( "disable" );
    });
}
// 2-3. initial
function InitUserCreateCombo() {
    $('#isadmin_icheck').iCheck({  
        checkboxClass: 'icheckbox_minimal-blue'    
    });    
    
    $('#user_combo_disk_type').scombobox({
        editAble:false,
        wrap:false       
    });

    $('#btn_close_create_user').click(function (event) {
        event.preventDefault();
        $( "#dialog_create_user" ).dialog('close');                    
    });       
}    
// 2-4. Reset Value
function ResetValueUserCreateDialog() {
        $('#input_UserName').val('');
        $('#input_userdisksize').val('');
        $('#isadmin_icheck').iCheck('uncheck');
        $('#user_combo_disk_type').scombobox('val',0);
}
// 2-5. Create User - initial Button
function InitUserCreatButton() {
    $('#btn_create_user').click(function (event) {            
        event.preventDefault();
        var name = $('#input_UserName').val();           
        if(name.length === 0) {
            alert(LanguageStr_langSysCfgUserMgt.User_Name_Empty_Warning);
            return false;
        }         
            // if(!VerifyVMInput(name))
            // {
            //     var name_warning = (LanguageStr_VM.User_Name_Warning).replace("&",name);
            //     alert(name_warning);
            //     return false;
            // }         
            // if(reg.test(name))
            // {
            //     var name_warning_1 = (LanguageStr_VM.User_Name_Warning_1).replace("&",name);
            //     alert(name_warning_1);
            //     return false;
            // }        
        /*
            var findVMIndex = VMCheckName.indexOf(name);
            if(findVMIndex !== -1)
            {
                var name_warning_2 = (LanguageStr_VM.VM_Clone_Name_Warning_2).replace("&",name);                    
                alert(name_warning_2);
                return false;
            }
        */
        var disksize = $('#input_userdisksize').val();
        if(disksize == null || disksize == "") {
            disksize = 0;
        }    

        if(!/^[0-9]+$/.test(disksize)) {
            alert(LanguageStr_langSysCfgUserMgt.Only_number_Warning);
            return false;
        }        
        
        var disktype = $('#user_combo_disk_type').scombobox('val');
        var check_isadmin = $('#icheckpause').prop('checked');
        var raidid = $('#combo_raidid_createuser').scombobox('val');

        switch (UserCreateOrModify) {
            case 0:
                UserCreate(name,check_isadmin,disksize,disktype,raidid);
                break;
            case 1:
                UserModify(name,UserID);
                break;
            default:
                break;
        }                        
    });      
}
// 2-6. 
function UserCreate(name,isadmin,size,type,raidid) { 
    $('.Div_WaitAjax').dialog( 'open' );
    var request = Ajax_CreateUser(name,isadmin,size,type,raidid);  
    CallBackUserCreate(request);
};
// 2-7. 
function CallBackUserCreate(request) {
    request.done(function(msg, statustext, jqxhr) {                 
        if(typeof(msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        } else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }          
            
        $( "#dialog_create_user" ).dialog('close');
        NoFreshUI = false;
        GetUserDataForCreateTableList();
    });
    request.fail(function(jqxhr, textStatus) {                  
        $('.Div_WaitAjax').dialog('close');       
        alert("Error Code: " + jqxhr.status);        

        if(jqxhr.status == 400) {

        } else if(jqxhr.status == 409) {
                
        } else if(jqxhr.status == 500) {
                
        } else{
            
        }  
    });
}
/******************************* 刪除使用者 *******************************/
// 3-1. Delete user - initial Button
function InitUserDeleteButton() {
    $('#btn_delete_user').click(function (event) {            
        event.preventDefault();
        if(LastUserData) {
            UserProfileInfoClear();                       
        }
        UserDelete(UserID);
    });
}    
// 3-2. Delete user
function UserDelete(id_User) {
    if(id_User === undefined) {
        alert(LanguageStr_langSysCfgUserMgt.Delete_User_Warning);
    } else {
        var delete_ask = LanguageStr_langSysCfgUserMgt.Delete_User_confirm;
        var confirmable = confirm(delete_ask);

        if (confirmable === true) {
            var request = Ajax_DeleteUser(id_User);  
            CallBackUserDelete(request);
        }
    }
}
// 3-3. Delete user
function CallBackUserDelete(request) {
    request.done(function(msg, statustext, jqxhr) {                 
        if(typeof(msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        } else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }          
        NoFreshUI = false;    
        GetUserDataForCreateTableList();
    });
    request.fail(function(jqxhr, textStatus) {                  
        $('.Div_WaitAjax').dialog('close');       
        alert("Error Code: " + jqxhr.status);        

        if(jqxhr.status == 400) {

        } else if(jqxhr.status == 409) {
                
        } else if(jqxhr.status == 500) {
                
        } else{
            
        }  
    });
}
/******************************* 建立User Profile *******************************/
// 4-1. Create User Profile dialog
function CreateDialog_UserProfileCreate() {
    $("#dialog_user_profile").dialog({
        title: LanguageStr_langSysCfgUserMgt.Create_UserProfile_title,
        modal: true,
        resizable: false,
        draggable: true,
        autoOpen: false,
        height: 350,
        width: 500,
        dialogClass: "no-close",
        closeOnEscape: false
    });
}
// 4-2. open dialog
function InitButtonForCreateUserProfile(){
    $('#btn_create_userprofile').click(function(event){
        event.preventDefault();
        ResetValueUserProfileCreateDialog();
        $( "#dialog_user_profile" ).dialog('open');
        Get_Raid_ID($('#raidid_userprofile'), -1, false);
    });
}  
// 4-3. initial
function InitUserProfileCreateCombo() {   
    $('#userprofile_combo_disk_type').scombobox({
        editAble:false,
        wrap:false       
    });

    $('#btn_close_userprofile').click(function (event) {
        event.preventDefault();
        $( "#dialog_user_profile" ).dialog('close');                    
    });       
}
// 4-4. Reset Value
function ResetValueUserProfileCreateDialog() {
    $('#input_userprofile_disksize').val('');
    $('#userprofile_combo_disk_type').scombobox('val',0);
}
// 4-5. Create User Profile - initial Button
function InitUserProfileCreatButton() {
    $('#btn_confirm_userprofile').click(function (event) {            
        event.preventDefault();   

        var disksize = $('#input_userprofile_disksize').val();        
        if(disksize==null || disksize=="") {
            alert(LanguageStr_langSysCfgUserMgt.UserProfile_DiskSize_Empty_Warning);
            return false;            
        }           

        if(!/^[0-9]+$/.test(disksize)) {
            alert(LanguageStr_langSysCfgUserMgt.Only_number_Warning);
            return false;
        }        

        var disktype = $('#userprofile_combo_disk_type').scombobox('val');
        var raidid   = $('#combo_raidid_userprofile').scombobox('val');

        UserProfileCreate(UserID,disksize,disktype,raidid);
    });      
}
// 4-6.
function UserProfileCreate(id_User, size, type, raidid) {
    $('.Div_WaitAjax').dialog( 'open' );
    var request = Ajax_CreateUserProfile(id_User, size, type, raidid);
    request.done(function(msg, statustext, jqxhr) {                 
        if(typeof(msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        } else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }          
            
        $( "#dialog_user_profile" ).dialog('close');
        $('.Div_WaitAjax').dialog('close');
        GetUserDetailForCreateTableList(id_User);
    });
    request.fail(function(jqxhr, textStatus) {                  
        $( "#dialog_user_profile" ).dialog('close');
        $('.Div_WaitAjax').dialog('close');       
        alert("Error Code: " + jqxhr.status);        

        if(jqxhr.status == 400) {

        } else if(jqxhr.status == 409) {
                
        } else if(jqxhr.status == 500) {
                
        } else{
            oError.CheckAuth(jqxhr.status,ActionStatus.VMCreate);
        }  
    });
}
/******************************* 刪除User Profile *******************************/
// 5-1. Delete User Profile - initial Button
function InitUserProfileDeleteButton() {
    $('#btn_delete_userprofile').click(function (event) {            
        event.preventDefault();
        UserProfileDelete(UserID);
    });
}   
// 5-2. Delete User Profile
function UserProfileDelete(id_User) {
    if(id_User === undefined) {
        alert(LanguageStr_langSysCfgUserMgt.UserProfile_Select_Delete_Warning);
    } else {
        var delete_ask = LanguageStr_langSysCfgUserMgt.Delete_UserProfile_confirm;
        var confirmable = confirm(delete_ask);

        if (confirmable === true) {
            $('.Div_WaitAjax').dialog( 'open' );
            var request = Ajax_DeleteUserProfile(id_User);  
            CallBackUserProfileDelete(request);
        }
    }
}
// 5-3.
function CallBackUserProfileDelete(request) {    
    request.done(function(msg, statustext, jqxhr) {                 
        if(typeof(msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        } else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }          
            
        $('.Div_WaitAjax').dialog('close');
        GetUserDetailForCreateTableList(UserID);
    });
    request.fail(function(jqxhr, textStatus) {                  
        $('.Div_WaitAjax').dialog('close');       
        alert("Error Code: " + jqxhr.status);        

        if(jqxhr.status == 400) {

        } else if(jqxhr.status == 409) {
                
        } else if(jqxhr.status == 500) {
                
        } else{
            
        }  
    });
}
/******************************* 顯示User Profile資訊 *******************************/
// 6-1. User Detail information
function GetUserDetailForCreateTableList(id_User) {
    $('.Div_WaitAjax').dialog( 'open' );
    var request = Ajax_GetUserDetailList(id_User);
    request.done(function (AjaxData, statustext, jqXHR) {
        $('.Div_WaitAjax').dialog('close');
        UserDetailListToUI(AjaxData);
    });
    request.fail(function (jqXHR, textStatus) {
        //oUIError.CheckAuth(jqxhr.status, ActionStatus.ListSchedule);
        $('.Div_WaitAjax').dialog('close');
    });
}
// 6-2. 定義class承接json資料
function UserDetailListToUI(JsonData) {
    var HtmlStr = "";
    var udc;
    show = false;
    UserDetailList = [];

    $.each(JsonData, function (index, element) {
        if(element == null) {
            show = false;
        } else {
            udc = new UserDetailClass(element['ID'], element['DiskSize'], element['VDName'], element['VDID'], element['DiskType'],element['RAIDID'],element['DiskState']);
            UserDetailList.push(udc);
            show = true;
        }

    });
    CreateUserDetailListHtml(show);
}
// 6-3. 將User Detail相關資料List出來
function CreateUserDetailListHtml(show) {
    UserProfileInfoClear();

    if(!show) {           
        $('#btn_create_userprofile').button( "enable" );                   
        $('#btn_delete_userprofile').button( "disable" );
        $('#btn_modify_userprofile').button( "disable" );       
        $('#btn_move_raidid').button( "disable" );
    } else {
        $('#btn_create_userprofile').button( "disable" );
        $('#btn_delete_userprofile').button( "enable" );
        $('#btn_modify_userprofile').button( "enable" );
        $('#btn_move_raidid').button( "enable" );

        $.each(UserDetailList, function (index, element) {
            $('#user_disk_info_disksize').text(element['disksize']);
            $('#UserDiskOrgSize').text(add_comma_of_number(add_two_fixed_float(element['disksize'])));   
            temp_disksize_forModify = element['disksize'];

            switch(element['disktype']) {
                case 0:
                    $('#user_disk_info_disktype').text("IDE");
                    break;
                case 1:
                    $('#user_disk_info_disktype').text("SCSI");
                    break;
                case 2:
                    $('#user_disk_info_disktype').text("Virtio");
                    break;                
                default:
                    break;
            }
            if(element['state']==33) {
                $('#user_disk_info_raidid').text(LanguageStr_langSysCfgUserMgt.Moving);
                $('#user_disk_info_raidid').css("color", "red");
            } else {
                $('#user_disk_info_raidid').text(element['raidid']);
                $('#user_disk_info_raidid').css("color", "blue");
            }   
                

            temp_raidid = parseInt(element['raidid']);

            if(element['vdname'] == null) {
                $('#user_disk_info_bindVD').text(LanguageStr_langSysCfgUserMgt.NoBindingUserVD);
            } else {
                $('#user_disk_info_bindVD').text(element['vdname']);
            }        
        });  

    }
  
};
/******************************* 啟用使用者 *******************************/

function InitEnableButton(){
    $('#btn_enable_user').click(function(event){
        event.preventDefault();  
        $('#label_user_status').text(LanguageStr_langSysCfgUserMgt.Enable);
        $('#label_user_status').css("color", "green");
        UserEnableState = 0;
        UserEnableBtnStyle(UserEnableState);
        SetUserEnable(UserID, UserEnableState);
    });
    $('#btn_disable_user').click(function(event){
        event.preventDefault();               
        $('#label_user_status').text(LanguageStr_langSysCfgUserMgt.Disable); 
        $('#label_user_status').css("color", "red");
        UserEnableState = 1;
        UserEnableBtnStyle(UserEnableState);
        SetUserEnable(UserID, UserEnableState);
    });
}  

// 7-1. Disable / Enable User
function InitUserEnableCheckbox() {
    /*
    $("#userenable_icheck").change(function() {
        if(this.checked) {
            //Do stuff
            alert(UserID);
        }
    });
    */
    $('#userenable_icheck').on('ifClicked', function(event){                            
        var element = $(this);
        if (element.is(":checked")) {
            UserEnableState = 1;
            $('#label_user_status').text(LanguageStr_langSysCfgUserMgt.Disable);
        } else {
            UserEnableState = 0;
            $('#label_user_status').text(LanguageStr_langSysCfgUserMgt.Enable);
        }

        UserEnableBtnStyle(UserEnableState);
        
        SetUserEnable(UserID, UserEnableState);
    });  
}
// 7-2. 
function SetUserEnable(userid, state) {
    var JsonPath = "/manager/user/" + userid + "?disable=" + state;
    $('.Div_WaitAjax').dialog( 'open' );
    var request = Ajax_EnableUser(JsonPath);
    CallBackSetUserEnable(request);
};
// 7-3. 
function CallBackSetUserEnable(request) {
    
    request.done(function (msg, statustext, jqxhr) {
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }      
        $('.Div_WaitAjax').dialog( 'close' );
        NoFreshUI = true;
        GetUserDataForCreateTableList();  
        GetUserDetailForCreateTableList(GetUserID);
    });
    request.fail(function (jqxhr, textStatus) {
        $('.Div_WaitAjax').dialog( 'close' );
    });
}
/******************************* 修改User Profile *******************************/
// 8-1. Modify User Profile dialog
function ModifyDialog_UserProfileCreate() {
    $("#dialog_Modify_user_profile").dialog({
        title: LanguageStr_langSysCfgUserMgt.Modify_UserProfile_title,
        modal: true,
        resizable: false,
        draggable: true,
        autoOpen: false,
        height: 350,
        width: 500,
        dialogClass: "no-close",
        closeOnEscape: false
    });
}
// 8-2. open dialog
function InitButtonForModifyUserProfile(){
    $('#btn_modify_userprofile').click(function(event){
        event.preventDefault();
        ResetValueUserProfileModifyDialog();

        switch($('#user_disk_info_disktype').text()) {
            case "IDE":
                $('#modify_userprofile_combo_disk_type').scombobox('val',0);
                break;
            case "SCSI":
                $('#modify_userprofile_combo_disk_type').scombobox('val',1);
                break;
            case "Virtio":
                $('#modify_userprofile_combo_disk_type').scombobox('val',2);
                break;                
            default:
                break;
        }
            
        $('#modify_userprofile_combo_disk_type').scombobox('disabled',true);
        $( "#dialog_Modify_user_profile" ).dialog('open');
    });
}  
// 8-3. initial
function InitUserProfileModifyCombo() {   
    $('#modify_userprofile_combo_disk_type').scombobox({
        editAble:false,
        wrap:false       
    });

    $('#btn_modify_close_userprofile').click(function (event) {
        event.preventDefault();
        $( "#dialog_Modify_user_profile" ).dialog('close');                    
    });       
}
// 8-4. Reset Value
function ResetValueUserProfileModifyDialog() {
    $('#modify_userprofile_disksize').val('');
    $('#modify_userprofile_combo_disk_type').scombobox('val',0);
}
// 8-5. Modify User Profile - initial Button
function InitUserProfileModifyButton() {
    $('#btn_modify_confirm_userprofile').click(function (event) {            
        event.preventDefault();   

        var disksize = Number($('#modify_userprofile_disksize').val()) + Number(temp_disksize_forModify);
        if(disksize == null || disksize == "") {
            alert(LanguageStr_langSysCfgUserMgt.UserProfile_DiskSize_Empty_Warning);
            return false;            
        }           

        if(!/^[0-9]+$/.test(disksize)) {
            alert(LanguageStr_langSysCfgUserMgt.Only_number_Warning);
            return false;
        }

        var disktype = $('#modify_userprofile_combo_disk_type').scombobox('val');

        UserProfileModify(UserID,disksize,disktype);
    });      
}
// 8-6.
function UserProfileModify(id_User, size, type) {
    $('.Div_WaitAjax').dialog( 'open' );
    var request = Ajax_ModifyUserProfile(id_User, size, type);
    request.done(function(msg, statustext, jqxhr) {                 
        if(typeof(msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        } else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }          
            
        $( "#dialog_Modify_user_profile" ).dialog('close');
        $('.Div_WaitAjax').dialog('close');
        GetUserDetailForCreateTableList(id_User);
    });
    request.fail(function(jqxhr, textStatus) {        
        $( "#dialog_Modify_user_profile" ).dialog('close');      
        $('.Div_WaitAjax').dialog('close');       
        alert("Error Code: " + jqxhr.status);        

        if(jqxhr.status == 400) {

        } else if(jqxhr.status == 409) {
                
        } else if(jqxhr.status == 500) {
                
        } else{
            
        }  
    });
}
/******************************* 重置使用者密碼 *******************************/
// 9-1. 
function InitResetPWBtn() {
    $('#btn_reset_userpassword').click(function(event){
        event.preventDefault();
        var reset_ask = LanguageStr_VM.VM_ResetPWD_ask.replace("@",VMorVDI);
        var confirmable = confirm(reset_ask);
        if(UserID==null) {
            alert("請選擇要重設密碼的使用者");
        }
        if (confirmable === true) {
            UserResetPWD(UserID);
        }
    }); 
}
// 9-2.
function UserResetPWD(userid) {
    var request = Ajax_ResetUserPWD(userid);  
    CallBackUserResetPWD(request);
};
// 9-3.
function CallBackUserResetPWD(request) {
    request.done(function(msg, statustext, jqxhr) {                 
        if(typeof(msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }   
        else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }
        alert(LanguageStr_langSysCfgUserMgt.Reset_UserPWD_success + "(" + jqxhr.status + ")");
            
    });
    request.fail(function(jqxhr, textStatus) {                  
        alert("Error Code: " + jqxhr.status);
    });
}
/******************************* move user disk *******************************/
// 10-1. Move User Disk dialog
function Dialog_MoveUserDisk() {
    $("#dialog_moveraid").dialog({
        title: LanguageStr_langSysCfgUserMgt.MoveUserDisk,
        modal: true,
        resizable: false,
        draggable: true,
        autoOpen: false,
        height: 350,
        width: 500,
        dialogClass: "no-close",
        closeOnEscape: false
    });
}
// 10-2. open dialog
function InitButtonForMoveUserDisk(){
    $('#btn_move_raidid').click(function(event){
        event.preventDefault();
        $('.move_raid_alert').hide();       
        InitMoveUserDisk();
        $( "#dialog_moveraid" ).dialog('open');      
    });
}
// 10-3. initial
function InitMoveUserDisk() {    
    Get_Raid_ID($('#raidid_move'), temp_raidid, true);    
    $('#btn_close_moveraid').click(function (event) {
        event.preventDefault();
        $( "#dialog_moveraid" ).dialog('close');                    
    });             
}    
// 10-4. Move User Disk - initial Button
function InitMoveUserDiskButton() {
    $('#btn_moveraid_confirm').click(function (event) {            
        event.preventDefault();
        var raidid = $('#combo_raidid_move').scombobox('val');
        var userid = UserID;        
        MoveUserDisk(userid, raidid);            
    });      
}

function MoveUserDisk(id_User, id_Raid) {
    var request = Ajax_MoveUserDisk(id_User, id_Raid);  
    CallBackMoveUserDisk(request, id_User);
}

function CallBackMoveUserDisk(request, id_User) {
    $('.Div_WaitAjax').dialog( 'open' );
    request.done(function(msg, statustext, jqxhr) {                 
        if(typeof(msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        } else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }          
        $( "#dialog_moveraid" ).dialog('close');
        $('.Div_WaitAjax').dialog('close');
        GetUserDetailForCreateTableList(id_User);
    });
    request.fail(function(jqxhr, textStatus) {                  
        $( "#dialog_moveraid" ).dialog('close');   
        $('.Div_WaitAjax').dialog('close');
        GetUserDetailForCreateTableList(id_User);

        alert("Error Code: " + jqxhr.status);        

        if(jqxhr.status == 400) {

        } else if(jqxhr.status == 409) {
                
        } else if(jqxhr.status == 500) {
                
        } else{
            
        }  
    });
}


/******************************* 重新整理 *******************************/
// All user List refresh
function refreshUserListStatus() {
    $('#btn_refresh_user').click(function(event){
        event.preventDefault();
        NoFreshUI = false;    
        UserID = null;
        // alert(UserID);
        GetUserDataForCreateTableList();
    });    
}
// User Profile refresh
function refreshUserProfileStatus() {
    $('#btn_refresh_userprofile').click(function(event){
        event.preventDefault();
        GetUserDetailForCreateTableList(UserID);
    });    
}


function EmptyUserListStatus() {
        $('#userenable_icheck').iCheck('uncheck');     
        $('#btn_create_userprofile').button( "disable" );                   
        $('#btn_delete_userprofile').button( "disable" );
        $('#btn_modify_userprofile').button( "disable" );     
        $('#btn_move_raidid').button( "disable" );
}

// Modify user - initial Button
function InitUserModifyButton() {
    $('#btn_modify_user').click(function (event) {            
        event.preventDefault();
        ResetValueUserCreateDialog();
        UserCreateOrModify = 1;
        InitUserCreateOrModify();
        $( "#dialog_create_user" ).dialog('open');
         $('#btn_create_user').button( "enable" ); 
    });
}  

function UserModify(name,userid) { 
    $('.Div_WaitAjax').dialog( 'open' );
    var request = Ajax_ModifyUser(name,userid);  
    CallBackUserModify(request);
};

function CallBackUserModify(request) {
    request.done(function(msg, statustext, jqxhr) {                 
        if(typeof(msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        } else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }          
            
        $( "#dialog_create_user" ).dialog('close');
        NoFreshUI = false;
        GetUserDataForCreateTableList();
    });
    request.fail(function(jqxhr, textStatus) {                  
        $('.Div_WaitAjax').dialog('close');       
        alert("Error Code: " + jqxhr.status);        

        if(jqxhr.status == 400) {

        } else if(jqxhr.status == 409) {
                
        } else if(jqxhr.status == 500) {
                
        } else{
            
        }  
    });
}

function UserProfileInfoClear() {
    $('#user_disk_info_disksize').text('');
    $('#user_disk_info_disktype').text('');
    $('#user_disk_info_bindVD').text('');  
    $('#user_disk_info_raidid').text('');      
}

function InitUserCreateOrModify() {
    if(UserCreateOrModify == 0) {        
        $( ".dialog_createuser_disksize" ).show();
        $( ".dialog_createuser_disktype" ).show();
        $( ".dialog_createuser_manager" ).hide();
        $( ".dialog_createuser_raidid" ).show();
        // 修改原title名稱
        var title = LanguageStr_langSysCfgUserMgt.Create_User;
        $("#dialog_create_user").dialog('option', 'title', title);
        Get_Raid_ID($('#raidid_createuser'), -1, false);
    } else if(UserCreateOrModify == 1) {
        $( "#input_UserName" ).val(temp_username);        
        $( ".dialog_createuser_disksize" ).hide();
        $( ".dialog_createuser_disktype" ).hide();
        $( ".dialog_createuser_manager" ).hide();
        $( ".dialog_createuser_raidid" ).hide();
        // 修改原title名稱
        var title = LanguageStr_langSysCfgUserMgt.Modify_User;
        $("#dialog_create_user").dialog('option', 'title', title);
    }
}


// 9. Reset User Password
/**************************************************************************/
/*                                                                        */
/*                           Relay Ajax Rule建立                           */
/*                                                                        */
/**************************************************************************/
// 1. 取得全部 User 資訊
function Ajax_GetUserList() {
    var request = CallAjax("/manager/user", "", "json", "GET");
    return request;
}
// 2. 建立 User 
function Ajax_CreateUser(name, isadmin, size, type,raidid) {
    var jsonrequest = '{';
    jsonrequest += '"Name":"' + name + '",';
    jsonrequest += '"isAdmin":' + false + ',';
    jsonrequest += '"DiskSize":' + size + ',';
    jsonrequest += '"DiskType":' + type + ',';
    jsonrequest += '"RAIDID":' + raidid;
    jsonrequest += '}';

    var request = CallAjax("/manager/user", jsonrequest, "", "POST");
    return request;
};
// 3. 刪除 User 
function Ajax_DeleteUser(id_User) {
     // https://{IP}/manager/user/{idUser}?vd_delete={1|0}  
    var request = CallAjax("/manager/user/" + id_User + "?vd_delete=1", "", "", "DELETE");
    return request;
};
// 4. 建立user profile
function Ajax_CreateUserProfile(id_User, size, type, raidid) {
    var jsonrequest = '{';
    jsonrequest += '"DiskSize":' + size + ',';
    jsonrequest += '"DiskType":' + type+ ',';
    jsonrequest += '"RAIDID":' + raidid;
    jsonrequest += '}';

    var request = CallAjax("/manager/user/" + id_User + "/profile", jsonrequest, "", "POST");
    return request;
};
// 5. 刪除user profile
function Ajax_DeleteUserProfile(id_User) {  
    var request = CallAjax("/manager/user/" + id_User + "/profile", "", "", "DELETE");
    return request;
};
// 6. List User Detail 
function Ajax_GetUserDetailList(id_User) {
    var request = CallAjax("manager/user/" + id_User + "/detail", "", "json", "GET");
    return request;
}
// 7. 開關User
function Ajax_EnableUser(JsonPath) {
    var request = CallAjax(JsonPath, "", "", "PUT");
    return request;
}
// 8. 修改user profile
function Ajax_ModifyUserProfile(id_User, size, type) {
    var jsonrequest = '{';
    jsonrequest += '"DiskSize":' + size + ',';
    jsonrequest += '"DiskType":' + type;
    jsonrequest += '}';    
    var request = CallAjax("/manager/user/" + id_User + "/profile", jsonrequest, "", "PUT");
    return request;
}
// 9. Reset User Password
function Ajax_ResetUserPWD(id_User) {
    var jsonrequest = '{';
    jsonrequest += '"Password":"000000"';
    jsonrequest += '}';    
    var request = CallAjax("/manager/user/" + id_User + "/resetpwd", jsonrequest, "", "PUT");
    return request;
}
// 10. Modify User 
function Ajax_ModifyUser(name, userid) {
    var jsonrequest = '{';
    jsonrequest += '"Username":"' + name + '"';
    jsonrequest += '}';

    var request = CallAjax("manager/user/" + userid + "/info", jsonrequest, "", "PUT");
    return request;
};
// 11. Move User Disk
function Ajax_MoveUserDisk(id_User, raidid) {
    var jsonrequest = '{';
    jsonrequest += '"RAIDID":' + raidid;
    jsonrequest += '}';

    var request = CallAjax("/manager/user/" + id_User + "/disk/move", jsonrequest, "", "PUT");
    return request;
};



/* Optional: Add active class to the current button (highlight it) */
// var container = document.getElementById("btnContainer");
// var btns      = container.getElementsByClassName("btn");
// for (var i = 0; i < btns.length; i++) {
//     btns[i].addEventListener("click", function() {
//     var current          = document.getElementsByClassName("active");
//     current[0].className = current[0].className.replace(" active", "");
//     this.className       += " active";
//   });
// }