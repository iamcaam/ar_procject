// JavaScript Document
var AvailableDiskCount;
var ComboLevelChangeWork;
var bol_check_change_action = true;
var PollingListRAID; 
var bRunPollingListRAID;
var tmp_raid_length;


$(document).ready(function ()   //  新增標頭位置對齊(英文) 2016.12.8 by william
{
    var RS_Header_label_width = $(".RAIDList_Level").text().length;
    if(RS_Header_label_width<8){
        $(".RAIDList_Level").css({"left": "3%"});
        // $(".RAIDList_VMAssign_Capacity").css({"left": "17%","width": "200px"});
        $(".RAIDList_FreeCapacity").css({"left": "36.5%"});
        $(".RAIDList_Capacity").css({"left": "50.5%","width": "130px"});
        // $(".RAIDList_TP").css({"left": "62%"});
        $(".RAIDList_Status").css({"left": "62%","width": "260px"});
        $(".RAIDList_Action").css({"left": "89.5%"});
    }
});



function GetDiskForCreateTableList()
{
    tmp_raid_length = 0;
    PollingListRAID = true;  
    // bRunPollingListRAID = false; 
    AvailableDiskCount = 0;
    $('.Div_WaitAjax').dialog( 'open' );   
    var request = Ajax_ListDisk_SendCommand();    			
    request.done( function( AjaxData, statustext, jqXHR ) {                
        if ( true == DebugMsg )
        {
            alert( "jqXHR:" + jqXHR.responseText );
        }		
        $('.Div_WaitAjax').dialog( 'close' );
        CreateTableList(AjaxData);
        if(AvailableDiskCount >= 3){
            $(".btn-create").button( "disable" );
            $('#combolevel').scombobox('val','2');
            $('#RAID_hotspare_comment').hide();        
            $('.iCheckBox_RAID_All_Disk').iCheck('uncheck');
            $('#dialog_create_raid').dialog('open');
            // $('#iCheckTP').iCheck('check');
        }
        else{
            alert(LanguageStr_Raid.RAID_Not_Enough_Disk);
        }
    });        	
    request.fail( function( jqXHR, textStatus ){ 
        if ( true == DebugMsg )
        {
            alert( "jqXHR:" + jqXHR.responseText );
        }
        $('.Div_WaitAjax').dialog( 'close' );
        oUIError.CheckAuth(jqxhr.status,ActionStatus.ListDisk);
//        OpenDialog_Message( LanguageStr_Raid.Ajax02 );
    });
}

function CreateTableList(JsonData)
{
    var append_str = '';
    AvailableDiskCount = 0;
    
    $.each( JsonData, function(index, element){ 
        if(element['Status'] === 1){
            AvailableDiskCount++;
            append_str += '<tr id="tr' + element['DevID'] +'">';
            append_str += '<td style="text-align:center"><input type="checkbox" class="iCheckBox" id="' + element['DiskName'] +  '"></td>';              
            append_str += '<td style="text-align:center">' + element['Slot'] + '</td>';
            append_str += '<td style="text-align:center">' + element['Capacity'] + '</td>';
            append_str += '</tr>';
        }
    });  
    switch($('#combolevel').scombobox('val')){
        case 's':
            $('.iCheckBox_RAID_All_Disk').iCheck('disable');
            break;
        default:
            $('.iCheckBox_RAID_All_Disk').iCheck('enable');
            break;        
    }
    $('#table-disk-for-raid').find("tr:gt(0)").remove();
    $('#table-disk-for-raid').append(append_str);
    $('.iCheckBox').iCheck({  
        checkboxClass: 'icheckbox_minimal-blue'                
    });
     $('.iCheckBox').on('ifChanged', function(event){          
        if(!bol_check_change_action)
            return false;        
        var n = $( ".iCheckBox:checked" ).length;
        switch($('#combolevel').scombobox('val')){
            case '2':
                if(n >= 3){
                    $(".btn-create").button( "option", "disabled", false );
                    return;
                }
                break;           
            case '3':
                if(n >= 4){
                    $(".btn-create").button( "option", "disabled", false );
                    return;
                }
                break;
        }        
        $(".btn-create").button( "option", "disabled", true );
    });
}

// 註冊 RAID 頁面的事件
function RegRaidPageEvent()
{
    bRunPollingListRAID = false;
    $('#btn_refresh_raid').click(function(e){   
        if(typeof(NowDiskSortType)=== 'undefined')
        {
            var th = $('#poition .VMHeader');
            th.removeClass('desc');
            th.addClass('asc');
            NowDiskSortDirection = SortDirection.ASC;
            NowDiskSortType = SortType.String;
            NowDiskSortDOMID = th.attr('id');
        }                                            
        Ajax_ListDisk(false);  
        Ajax_GetInfo_Raid(true);
        SendPollingListRAID();
    });
//	// 順便做畫面按鈕控制，先全部隱藏
    $('#btn_create_raid_dialog').click(function(e) 
    {
        GetDiskForCreateTableList();        
    });
    
    $('#btn_create_raid').click(function(event){
        event.preventDefault();
        Ajax_CreateRaid();
    });
    
    ComboLevelChangeWork = true;
    $('#combolevel').scombobox('change',function(event){
        event.preventDefault();
        $(".btn-create").button( "option", "disabled", true );
        $(".iCheckBox").iCheck('enable');
        bol_check_change_action = false;
        $(".iCheckBox").iCheck('uncheck');
        $('.iCheckBox_RAID_All_Disk').iCheck('uncheck');
        bol_check_change_action = true;
//        var n = $( ".iCheckBox:checked" ).length;
        switch($('#combolevel').scombobox('val')){           
            case '2': 
                $('.iCheckBox_RAID_All_Disk').iCheck('enable');
                $('#RAID_disk_comment').text(LanguageStr_Raid.RAID_Least_Three);
                // $('#RAID_hotspare_comment').show();
                break;     
            case '3':
                $('.iCheckBox_RAID_All_Disk').iCheck('enable');
                $('#RAID_disk_comment').text(LanguageStr_Raid.RAID_Least_Four);
                // $('#RAID_hotspare_comment').show();
                break;
        }        
        if(!ComboLevelChangeWork)
            ComboLevelChangeWork = true;
        else{        
            if($('#combolevel').scombobox('val') === '3'){
                if(AvailableDiskCount < 4){
                    alert(LanguageStr_Raid.RAID_Not_Enough_Disk);
                    ComboLevelChangeWork = false;
                    $('#combolevel').scombobox('val','0');                    
                }                
            } 
            else if($('#combolevel').scombobox('val') === '2'){
                if(AvailableDiskCount < 3){
                    alert(LanguageStr_Raid.RAID_Not_Enough_Disk);
                    ComboLevelChangeWork = false;
                    $('#combolevel').scombobox('val','0');                    
                }  
                
            }
        }
    });           
}

function StartorStopPollingRAID(bstart){
    if(bstart){
        PollingListRAID = true;
    }
    else{                
        tmp_raid_length = 0;
        PollingListRAID = false;        
    }
};    

function SendPollingListRAID(){
    if(!bRunPollingListRAID){
//        console.log('Polling RAID');
        bRunPollingListRAID = true;     
        var request = CallAjax ( "/manager/ceph", "", "json", "GET" );
        CallbackPollingListRAID(request);
    }
}

function CallbackPollingListRAID(request){
    request.done( function( AjaxData, statustext, jqXHR ) 
    {       
        var raid_count = 0;
        if ( true == DebugMsg )
        {
                //$('#Div_RaidInfo').html( jqXHR.responseText );
                alert( "jqXHR:" + jqXHR.responseText );
        }        
        if(typeof(AjaxData['status']) !== "undefined" && AjaxData['status'] == 2){
            Logout_Another_Admin_Login();
            return false;
        }        
        if(jqXHR.responseText.trim() !== '{}')
            raid_count = 1;       
        if(raid_count !== tmp_raid_length)    
            ListDataToUI_RAID(AjaxData);
        else{
            $.each( AjaxData, function(index, element){ 
                // var tp = element['TP'] === 1 ? LanguageStr_Raid.RAID_Enable : LanguageStr_Raid.RAID_Disable;
                // $(".RAIDList_TP_Row").html(tp);
                $(".RAIDList_Level_Row").html(element['Duplicate']);
                var used = new Number(element['Used']);
                var total = new Number(element['Total']);
                var allocate = new Number(element['Allocate']);
                used = used.toFixed(2);       
                total = total.toFixed(2);    
                allocate = allocate.toFixed(2);     
                // $(".RAIDList_VMAssign_Capacity_Row").html(allocate + " GB");
                $(".RAIDList_FreeCapacity_Row").html(used + " GB");
                $(".RAIDList_Capacity_Row").html(total + " GB");   
                // var status= element['Status'].substring(0,1).toUpperCase();
                // status += element['Status'].substring(1).toLowerCase();
                var status= element['Status'];
                $(".RAIDList_Status_Row").html(status);                     
                if(element['Status'].indexOf("Proceeding")!=-1)
                    $(".btn-raid-destroy").button( "option", "disabled", true ); 
                else
                    $(".btn-raid-destroy").button( "option", "disabled", false ); 
            });
        }
        tmp_raid_length = raid_count;
        if(PollingListRAID){                    
            setTimeout(function(){bRunPollingListRAID = false;SendPollingListRAID();},8000);  // 避免頻繁的Request,將5秒改成8秒 2017.03.10 william
        }
        else
            bRunPollingListRAID = false;
    });      
    
    // Ajax Fail
    request.fail( function( jqXHR, textStatus )
    { 
        if(PollingListRAID){                    
            setTimeout(function(){bRunPollingListRAID = false;SendPollingListRAID();},8000);   // 避免頻繁的Request,將5秒改成8秒 2017.03.10 william
        }
        else
            bRunPollingListRAID = false;
//        if ( true == DebugMsg )
//        {
//            alert( "jqXHR:" + jqXHR.responseText );
//        }
//	$('.Div_WaitAjax').dialog( 'close' ); 	
////        OpenDialog_Message( LanguageStr_Raid.Ajax02 );
//        oUIError.CheckAuth(jqxhr.status,ActionStatus.ListRAID);
    });
}

// 取得 Raid 資訊
function Ajax_GetInfo_Raid(bBlock)
{    
    if(bBlock)
        $('.Div_WaitAjax').dialog( 'open' );         
    var request = CallAjax ( "/manager/ceph", "", "json", "GET" );    			
    // Ajax Success
    request.done( function( AjaxData, statustext, jqXHR ) 
    {       
        var raid_count = 0;
        if ( true == DebugMsg )
        {
                //$('#Div_RaidInfo').html( jqXHR.responseText );
                alert( "jqXHR:" + jqXHR.responseText );
        }        
        if(typeof(AjaxData['status']) !== "undefined" && AjaxData['status'] == 2){
            Logout_Another_Admin_Login();
            return false;
        }
        if(jqXHR.responseText.trim() !== '{ }')
            raid_count = 1;       
        if(bBlock)
            $('.Div_WaitAjax').dialog( 'close' );    
        if(raid_count !== tmp_raid_length)    
            ListDataToUI_RAID(AjaxData);
        else{
            $.each( AjaxData, function(index, element){
                // var tp = element['TP'] === 1 ? LanguageStr_Raid.RAID_Enable : LanguageStr_Raid.RAID_Disable;
                // $(".RAIDList_TP_Row").html(tp);
                $(".RAIDList_Level_Row").html(element['Duplicate']);
                 var used = new Number(element['Used']);
                var total = new Number(element['Total']);
                var allocate = new Number(element['Allocate']);
                used = used.toFixed(2);       
                total = total.toFixed(2);    
                allocate = allocate.toFixed(2);                
                $(".RAIDList_FreeCapacity_Row").html(used + " GB");
                $(".RAIDList_Capacity_Row").html(total + " GB");   
                // var status= element['Status'].substring(0,1).toUpperCase();
                // status += element['Status'].substring(1).toLowerCase();
                var status =  element['Status'];
                $(".RAIDList_Status_Row").html(status);                   
                if(element['Status'].indexOf("Proceeding")!=-1)
                    $(".btn-raid-destroy").button( "option", "disabled", true ); 
                else
                    $(".btn-raid-destroy").button( "option", "disabled", false ); 
            })
        }
        tmp_raid_length = raid_count;
    });      
    
    // Ajax Fail
    request.fail( function( jqXHR, textStatus )
    { 
        if ( true == DebugMsg )
        {
            alert( "jqXHR:" + jqXHR.responseText );
        }
	$('.Div_WaitAjax').dialog( 'close' ); 	
//        OpenDialog_Message( LanguageStr_Raid.Ajax02 );
        oUIError.CheckAuth(jqxhr.status,ActionStatus.ListRAID);
    });
}


// 將資料顯示到畫面上
function ListDataToUI_RAID(JsonData)
{
    var HtmlStr = "";
    var RAIDCount = 0;
    var raid_status = "";
    $.each( JsonData, function(index, element){   
        raid_status = element['Status'];
        // var status= element['Status'].substring(0,1).toUpperCase();
        // status += element['Status'].substring(1).toLowerCase();
        var status = element['Status'];
        var isdegraded = false;
        // var tp = element['TP'] === 1 ? LanguageStr_Raid.RAID_Enable : LanguageStr_Raid.RAID_Disable;
        if(element['Status'] === 'Degrade')
            isdegraded = true;
        HtmlStr += '<div class = "Div_RAIDRow" >';
	 
        HtmlStr += '<H4 class = "RAIDList_Level_Row AbsoluteLayout RowField" >' +  element['Duplicate'] + '</H4>';
        var used = new Number(element['Used']);
        var total = new Number(element['Total']);
        var allocate = new Number(element['Allocate']);
        used = used.toFixed(2);       
        total = total.toFixed(2);    
        allocate = allocate.toFixed(2); 
        // HtmlStr += '<H4 class = "RAIDList_VMAssign_Capacity_Row AbsoluteLayout RowField" >' + allocate +  ' GB</H4>';        
        HtmlStr += '<H4 class = "RAIDList_FreeCapacity_Row AbsoluteLayout RowField" >' + used +  ' GB</H4>';
        HtmlStr += '<H4 class = "RAIDList_Capacity_Row AbsoluteLayout RowField" >' + total + ' GB</H4>';        
        // HtmlStr += '<H4 class = "RAIDList_TP_Row AbsoluteLayout RowField" >' +  tp + '</H4>';          
        HtmlStr += '<H4 class = "RAIDList_Status_Row AbsoluteLayout RowField" >' +  status + '</H4>';                           
        HtmlStr += '<div class = "RAIDList_Action_Row AbsoluteLayout RowField" >';  
//        if(isdegraded)
//            HtmlStr += '<a id="' + element['name'] + '" href="#" class="btn-raid-recovery" style="font-size: smaller;min-width: 80px">' + LanguageStr_Raid.RAID_Recovery + '</a>';                
//        else
//            HtmlStr += '<a id="' + element['name'] + '" href="#" class="btn-raid-recovery raid-not-degraded" style="font-size: smaller;min-width: 80px">' + LanguageStr_Raid.RAID_Recovery + '</a>';                
                HtmlStr += '<a id="' + element['name'] + '"class="btn-raid-destroy" style="font-size: smaller;min-width: 80px">' + LanguageStr_Raid.RAID_Delete + '</a>';
        HtmlStr += '</div>';
        HtmlStr += '</div> ';
        RAIDCount++;
    });    
    if(RAIDCount > 0)
        $("#btn_create_raid_dialog").button( "option", "disabled", true );
    else
        $("#btn_create_raid_dialog").button( "option", "disabled", false );    
    $('#Div_RAIDList').html( HtmlStr );
    $('.btn-raid-recovery').button();
    $(".d").button( "option", "disabled", true );        
    $('.btn-raid-destroy').button();     
    if(raid_status.indexOf("Proceeding")!=-1)
        $(".btn-raid-destroy").button( "option", "disabled", true );    
    $('.raid-not-degraded').button("option", "disabled", true);
    RebindRAIDActionEvent();
    $('.Div_RAIDRow:odd').css( "background-color", "#E1FFFF" );
    $('.Div_RAIDRow:even').css( "background-color", "#E1FFF3" );    	
}

function RebindRAIDActionEvent()
{
    $('.btn-raid-recovery').click(function(event){
        event.preventDefault();   
        var confirmable = confirm(LanguageStr_Raid.RAID_Recovery_Ask);
        if (confirmable === true) {
            Ajax_RecoveryRaid($(this).attr('id'));
        }       
    });
    $('.btn-raid-destroy').click(function(event){
        event.preventDefault();        
        var confirmable = confirm(LanguageStr_Raid.RAID_Delete_Ask);
        if (confirmable === true) {
            Ajax_DeleteRaid($(this).attr('id'));
        }
    });
}

function Ajax_RecoveryRaid(PoolName)
{
    var StrJson = '{ ' + 
            '"AccessKey":"' + AccessKey + '"' + ',' +
            '"RAIDID":"' + PoolName + '"' + 
            '}';
    $('.Div_WaitAjax').dialog( 'open' );      
    var request = CallAjax ( "/system/raid/recovery", StrJson, "json" );
    
    request.done( function( AjaxData, statustext, jqXHR ){
        if ( true == DebugMsg )
        {
            alert( "jqXHR:" + jqXHR.responseText );
        }
        $('.Div_WaitAjax').dialog( 'close' );  
        if(typeof(AjaxData['status']) !== "undefined" && AjaxData['status'] == 2){
            Logout_Another_Admin_Login();
            return false;
        }
        if(AjaxData['result'] === 1){
            alert("Please replace the error disk with another one.");            
        }
        else
            Ajax_GetInfo_Raid();
    });

    request.fail( function( jqXHR, textStatus ){
        if ( true == DebugMsg )
        {
            alert( "jqXHR:" + jqXHR.responseText );
        }
//        OpenDialog_Message( LanguageStr_Raid.Ajax14 );
        $('.Div_WaitAjax').dialog( 'close' );  
//        Ajax_GetInfo_Raid();
        oUIError.CheckAuth(jqxhr.status,ActionStatus.RecoveryRAID);
    });
}

function Ajax_DeleteRaid(PoolName) 
{  
    $('.Div_WaitAjax').dialog( 'open' );  	
    PollingListRAID = false;
    var request = CallAjax ( "/manager/ceph", "", "", "DELETE" );
    
    request.done( function( AjaxData, statustext, jqXHR ){
        if ( true == DebugMsg )
        {
                alert( "jqXHR:" + jqXHR.responseText );
        }
//        $('.Div_WaitAjax').dialog( 'close' );  
        if(typeof(AjaxData['status']) !== "undefined" && AjaxData['status'] == 2){
            Logout_Another_Admin_Login();
            return false;
        }
        // if(AjaxData['result'] !== 0)
        //     alert(LanguageStr_Raid.RAID_Delete_Fail);
        if(typeof(NowDiskSortType)=== 'undefined')
        {
            var th = $('#poition .VMHeader');
            th.removeClass('desc');
            th.addClass('asc');
            NowDiskSortDirection = SortDirection.ASC;
            NowDiskSortType = SortType.String;
            NowDiskSortDOMID = th.attr('id');
        }                                            
        setTimeout( function(){Ajax_ListDisk(false);}, 1000 );
        setTimeout( function(){PollingListRAID = true;Ajax_GetInfo_Raid(true);SendPollingListRAID();}, 1000 );        
    });

    request.fail( function( jqXHR, textStatus ){
        if ( true == DebugMsg )
        {
                alert( "jqXHR:" + jqXHR.responseText );
        }
//        OpenDialog_Message( LanguageStr_Raid.Ajax09 );
        $('.Div_WaitAjax').dialog( 'close' );  
        oUIError.CheckAuth(jqxhr.status,ActionStatus.DeleteRAID);
//        setTimeout( Ajax_GetInfo_Raid, 1000 );
    });
}

function Ajax_CreateRaid() 
{
    var StrJson = '{';
    StrJson += '"Disks":[';                      
    var index = 0;
    $.each($( ".iCheckBox:checked" ),function(){
        var element = $(this);
        if(index !== 0)
           StrJson += ',';
        StrJson += '"' + element.attr('id') + '"';                 
        index++;
    });
    StrJson += '],"Duplicate":';
    var level = $('#combolevel').scombobox('val');
    StrJson += level;
    StrJson += '}';        
    // alert(StrJson);
    $('.Div_WaitAjax').dialog( 'open' );
    PollingListRAID = false;
    var request = CallAjax ( "/manager/ceph", StrJson, "" );    
		
    request.done( function( AjaxData, statustext, jqXHR ) {        
        $('.Div_WaitAjax').dialog( 'close' );
        $('#dialog_create_raid').dialog('close');
        if(typeof(AjaxData['status']) !== "undefined" && AjaxData['status'] == 2){
            Logout_Another_Admin_Login();
            return false;
        }
        else{
            if(typeof(NowDiskSortType)=== 'undefined')
            {
                var th = $('#poition .VMHeader');
                th.removeClass('desc');
                th.addClass('asc');
                NowDiskSortDirection = SortDirection.ASC;
                NowDiskSortType = SortType.String;
                NowDiskSortDOMID = th.attr('id');
            }                                            
            setTimeout( function(){Ajax_ListDisk(false)}, 500 );
            setTimeout( function(){PollingListRAID = true;Ajax_GetInfo_Raid(true);SendPollingListRAID();}, 500 );               
        }
    });

    request.fail( function( jqXHR, textStatus ){ 
        $('.Div_WaitAjax').dialog( 'close' );
        $('#dialog_create_raid').dialog('close');
//        OpenDialog_Message( LanguageStr_Raid.Ajax12 );
        oUIError.CheckAuth(jqxhr.status,ActionStatus.CreateRAID);
    });
}
