// JavaScript Document
var AvailableDiskCount;
var ComboLevelChangeWork;
var bol_check_change_action = true;
var PollingListRAID; 
var bRunPollingListRAID;
var tmp_raid_length;

var RAID_Expend_Level; // 2017.11.13 william 新增Raid擴充，紀錄RAID的Level狀態




var idDiskExpend; // 2017.11.13 william 新增Raid擴充，傳字串用




var IsDedupe;
var raidsWithkey;
var clickRAIDID;
var GetAjaxData;
var NowRAIDSortType;
var NowRAIDSortDirection;
var NowRAIDSortDOMID;

var Unimportable_RAID_ID;
var showInitialBtn = false;
// function Zfs_langStyleChange() { // 新增-針對中英文版式樣作不同的顯示標頭位置對齊(英文) 2017.11.14 by william
//     var RS_Header_label_width = $(".RAIDList_Level").text().length;
//     if(RS_Header_label_width<8){
//         $(".RAIDList_Level").css({"left": "2.5%"});
//         $(".RAIDList_VMAssign_Capacity").css({"left": "17%","width": "200px"});
//         $(".RAIDList_FreeCapacity").css({"left": "36.5%"});
//         $(".RAIDList_Capacity").css({"left": "50.5%","width": "130px"});
//         // $(".RAIDList_TP").css({"left": "62%"});
//         $(".RAIDList_Status").css({"left": "62%","width": "260px"});
//         $(".RAIDList_Action").css({"left": "84.5%"});
//     }
// }

function GetDiskForCreateTableList()
{
    // tmp_raid_length = 0;
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
        if(AvailableDiskCount >= 1){
            $(".btn-create").button( "disable" );
            $('#combolevel').scombobox('val','jbod'); // 2017.11.13 william 新增Raid擴充，預設 Single / JBOD
            $('#RAID_disk_comment').text(LanguageStr_Raid.RAID_Least_One);
            $('#RAID_hotspare_comment').hide();
            $('#RAID_hotspare_comment').hide();        
            $('.iCheckBox_RAID_All_Disk').iCheck('uncheck');
            // $('.iCheckBox_RAID_All_Disk').iCheck('disable'); // 2017.11.13 william 新增Raid擴充，取消關閉全選




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
        case 'jbod': // 2017.11.13 william 新增Raid擴充，取消關閉全選 disable -> enable
            $('.iCheckBox_RAID_All_Disk').iCheck('enable');
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
             case 'jbod': // 2017.11.13 william 新增Raid擴充
                // if(n === 0){
                //     $.each($(".iCheckBox:not(:checked)" ),function(index,element){
                //        $(element).iCheck('enable');
                //     });
                // }
                // else if(n === 1){                    
                //     $.each($(".iCheckBox:not(:checked)" ),function(index,element){
                //        $(element).iCheck('disable');
                //     });
                //     $(".btn-create").button( "option", "disabled", false );
                //     return;
                // }
                if(n >= 1){
                    $(".btn-create").button( "option", "disabled", false );
                    return;
                }                
                break
            // case '0':
            //     if(n >= 2){
            //         $(".btn-create").button( "option", "disabled", false );
            //         return;
            //     }
            //     break;
            case '1':
                if(n == 2){
                    $.each($(".iCheckBox:not(:checked)" ),function(index,element){
                       $(element).iCheck('disable');
                    });
                    $(".btn-create").button( "option", "disabled", false );
                    return;
                }
                else{
                    $.each($(".iCheckBox:not(:checked)" ),function(index,element){
                       $(element).iCheck('enable');
                    });
                }
                break;
            case '5':
                if(n >= 3){
                    $(".btn-create").button( "option", "disabled", false );
                    return;
                }
                break;
            case '6':
                if(n >= 4){
                    $(".btn-create").button( "option", "disabled", false );
                    return;
                }
                break;
            case '7':
                if(n >= 5){
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
    tmp_raid_length = 0;
    raidsWithkey = [];
    // Zfs_langStyleChange(); // 新增-針對中英文版式樣作不同的顯示標頭位置對齊(英文) 2017.11.14 by william
    bRunPollingListRAID = false;
    RAIDHederClickSort();
    $('#btn_refresh_raid').click(function(e){   
        if(typeof(NowDiskSortType)=== 'undefined')
        {
            var th = $('#poition .RAIDHeader');
            th.removeClass('desc');
            th.addClass('asc');
            NowRAIDSortDirection = SortDirection.ASC;
            NowRAIDSortType = SortType.String;
            NowRAIDSortDOMID = th.attr('id');
        }                                            
        Ajax_ListDisk(false);  
        Ajax_GetInfo_Raid(true);
        SendPollingListRAID();
    });
//	// 順便做畫面按鈕控制，先全部隱藏




    // $('#btn_create_raid_dialog').click(function(e) 
    // {
    //     GetDiskForCreateTableList();        
    // });
    
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
            case 'jbod': // 2017.11.13 william 新增Raid擴充，取消關閉全選 disable -> enable
                $('.iCheckBox_RAID_All_Disk').iCheck('enable');
                $('#RAID_disk_comment').text(LanguageStr_Raid.RAID_Least_One);
                $('#RAID_hotspare_comment').hide();
                break;
            case '0':
                $('.iCheckBox_RAID_All_Disk').iCheck('enable');
                $('#RAID_disk_comment').text(LanguageStr_Raid.RAID_Least_Two);
                $('#RAID_hotspare_comment').hide();
                break;
            case '1': 
                $('.iCheckBox_RAID_All_Disk').iCheck('disable');
                $('#RAID_disk_comment').text(LanguageStr_Raid.RAID_Least_Two);
                $('#RAID_hotspare_comment').show();
                break;
            case '5':
                $('.iCheckBox_RAID_All_Disk').iCheck('enable');
                $('#RAID_disk_comment').text(LanguageStr_Raid.RAID_Least_Three);
                $('#RAID_hotspare_comment').show();
                break;
            case '6':
                $('.iCheckBox_RAID_All_Disk').iCheck('enable');
                $('#RAID_disk_comment').text(LanguageStr_Raid.RAID_Least_Four);
                $('#RAID_hotspare_comment').show();
                break;
            case '7':
                $('.iCheckBox_RAID_All_Disk').iCheck('enable');
                $('#RAID_disk_comment').text(LanguageStr_Raid.RAID_Least_Five);
                $('#RAID_hotspare_comment').show();
                break;            
        }        
        if(!ComboLevelChangeWork)
            ComboLevelChangeWork = true;
        else{        
            if($('#combolevel').scombobox('val') === '5'){
                if(AvailableDiskCount < 3){
                    alert(LanguageStr_Raid.RAID_Not_Enough_RAID5);
                    ComboLevelChangeWork = false;
                    $('#combolevel').scombobox('val','jbod');    
                    $('#RAID_disk_comment').text(LanguageStr_Raid.RAID_Least_Three);
                    $('#RAID_hotspare_comment').hide();            
                    $('.iCheckBox_RAID_All_Disk').iCheck('enable');    
                }                
            } 
            else if($('#combolevel').scombobox('val') === '0'){
                if(AvailableDiskCount < 2){
                    alert(LanguageStr_Raid.RAID_Not_Enough_RAID0);
                    ComboLevelChangeWork = false;
                    $('#combolevel').scombobox('val','jbod');                    
                    $('#RAID_disk_comment').text(LanguageStr_Raid.RAID_Least_Two);
                    $('#RAID_hotspare_comment').hide();
                    $('.iCheckBox_RAID_All_Disk').iCheck('enable');
                }  
                
            }
            else if($('#combolevel').scombobox('val') === '1'){
                if(AvailableDiskCount < 2){
                    alert(LanguageStr_Raid.RAID_Not_Enough_RAID1);
                    ComboLevelChangeWork = false;
                    $('#combolevel').scombobox('val','jbod');   
                    $('#RAID_disk_comment').text(LanguageStr_Raid.RAID_Least_Two);
                    $('#RAID_hotspare_comment').hide();    
                    $('.iCheckBox_RAID_All_Disk').iCheck('enable');             
                }  
            }
            else if($('#combolevel').scombobox('val') === '6'){
                if(AvailableDiskCount < 4){
                    alert(LanguageStr_Raid.RAID_Not_Enough_RAID6);
                    ComboLevelChangeWork = false;
                    $('#combolevel').scombobox('val','jbod');                    
                    $('#RAID_disk_comment').text(LanguageStr_Raid.RAID_Least_Four);
                    $('#RAID_hotspare_comment').hide();
                    $('.iCheckBox_RAID_All_Disk').iCheck('enable');
                }  
            }
            else if($('#combolevel').scombobox('val') === '7'){
                if(AvailableDiskCount < 5){
                    alert(LanguageStr_Raid.RAID_Not_Enough_RAID7);
                    ComboLevelChangeWork = false;
                    $('#combolevel').scombobox('val','jbod');                    
                    $('#RAID_disk_comment').text(LanguageStr_Raid.RAID_Least_Five);
                    $('#RAID_hotspare_comment').hide();
                    $('.iCheckBox_RAID_All_Disk').iCheck('enable');
                }  
            }
        }
    });   
    // 2017.11.13 william 新增Raid擴充
    Raid_Expand_Dialog();  
    Disk_Info_Dialog();            
}

function StartorStopPollingRAID(bstart){
    if(bstart){
        PollingListRAID = true;
    }
    else{                
        // tmp_raid_length = 0;
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
        GetAjaxData = AjaxData;
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
            raid_count = AjaxData.length;      
        if(raid_count !== tmp_raid_length)    
            ListDataToUI_RAID(AjaxData,false);
        else{
            var OtherIDHaveRAID = false;
            var ID1HaveRAID = false;
            var ID1InGluster = false;
            var isProcess = false;
            var check_Unimportable = false;
            Unimportable_RAID_ID = [];            
            $.each( AjaxData, function(index, element){
                $('#info'+element['RAIDID']).removeClass('list_unimportable_RAID');
                if(element['Status'].indexOf('unimportable') != -1) {
                    isProcess = true;
                    check_Unimportable = true;
                    $('#info'+element['RAIDID']).addClass('list_unimportable_RAID');
                    Unimportable_RAID_ID.push('RAID ID ' + element['RAIDID']);  
                }                                    
                if(element['RAIDID'] == 1 && element['Level'].length > 0 && element['Status'].indexOf("creating") ==-1 && element['Status'].indexOf("deleting")==-1){
                    ID1HaveRAID = true;
                    if(element['InGluster'])
                        ID1InGluster = true;
                }
                raidsWithkey[element['RAIDID']]['Level'] = element['Level'];
                raidsWithkey[element['RAIDID']]['Used'] = element['Used'];
                raidsWithkey[element['RAIDID']]['Total'] = element['Total'];
                raidsWithkey[element['RAIDID']]['Allocate'] = element['Allocate'];
                raidsWithkey[element['RAIDID']]['Status'] = element['Status'];
                raidsWithkey[element['RAIDID']]['InGluster'] = element['InGluster']; 
                // var tp = element['TP'] === 1 ? LanguageStr_Raid.RAID_Enable : LanguageStr_Raid.RAID_Disable;
                // $(".RAIDList_TP_Row").html(tp);                
                if(element['Status'].length > 0)            
                    $("#raidLevel" + element['RAIDID']).html(element['Level']);
                else     
                    $("#raidLevel" + element['RAIDID']).html("Empty");                
                var used = new Number(element['Used']);
                var total = new Number(element['Total']);
                var allocate = new Number(element['Allocate']);
                used = used.toFixed(2);       
                total = total.toFixed(2);    
                allocate = allocate.toFixed(2);  
                if(element['Level'].length > 0) {
                    $("#raidAssign" + element['RAIDID']).html(add_comma_of_number(allocate) + " GB");              
                    $("#raidFree" + element['RAIDID']).html(add_comma_of_number(used) + " GB");
                    $("#raidTotal" + element['RAIDID']).html(add_comma_of_number(total) + " GB");
                }                    
                else{
                    $("#raidAssign" + element['RAIDID']).html('');              
                    $("#raidFree" + element['RAIDID']).html('');
                    $("#raidTotal" + element['RAIDID']).html('');
                }              
                var status= element['Status'].substring(0,1).toUpperCase();
                status += element['Status'].substring(1).toLowerCase();
                if(element['Status'].length > 0){
                    $('#create'+element['RAIDID']).button( "option", "disabled", true );
                }   
                else{
                    if(element['RAIDID'] > 1){ 
                        if(ID1HaveRAID == false){
                            $('#create'+element['RAIDID']).button( "option", "disabled", true );
                        }
                        else{                            
                            $('#create'+element['RAIDID']).button( "option", "disabled", false );
                        }
                    }else
                        $('#create'+element['RAIDID']).button( "option", "disabled", false );
                }                  
                $('#raidStatus'+element['RAIDID']).html(status);                 
                if(element['Status'] == '' || element['Status'].indexOf("creating") !=-1 || element['Status'].indexOf("deleting")!=-1 || element['Status'].indexOf("adding")!=-1) { // 2017.11.13 william 新增Raid擴充，增加adding狀態
                    if(element['Status'] != '') 
                        isProcess = true; 
                    $('#delete'+element['RAIDID']).button( "option", "disabled", true ); 
                    $('#expand'+element['RAIDID']).button( "option", "disabled", true ); // 2017.11.13 william 新增Raid擴充
                    $('#locate'+element['RAIDID']).button( "option", "disabled", true ); 
                    $('#info'+element['RAIDID']).button( "option", "disabled", true );
                    $("#dedupe" + element['RAIDID']).iCheck('disable');
                }                    
                else {                    
                    $("#dedupe" + element['RAIDID']).iCheck('enable');
                    if(element['RAIDID'] > 1){
                        if(element['InGluster'])
                            $('#delete'+element['RAIDID']).button( "option", "disabled", true ); 
                        else{
                            $('#delete'+element['RAIDID']).button( "option", "disabled", false ); 
                        }
                    }
                    $('#expand'+element['RAIDID']).button( "option", "disabled", false ); // 2017.11.13 william 新增Raid擴充
                    $('#locate'+element['RAIDID']).button( "option", "disabled", false );
                    $('#info'+element['RAIDID']).button( "option", "disabled", false );
                }
                if(element['Dedupe'])
                    $("#dedupe" + element['RAIDID']).iCheck('check');
                else
                    $("#dedupe" + element['RAIDID']).iCheck('uncheck');
                if(element['Status'] != '' && element['RAIDID'] > 1)
                    OtherIDHaveRAID = true;
            })
            if(OtherIDHaveRAID)
                $('#delete1').button( "option", "disabled", true ); 
            else{
                if(ID1HaveRAID){
                    if(ID1InGluster)
                        $('#delete1').button( "option", "disabled", true ); 
                    else
                        $('#delete1').button( "option", "disabled", false ); 
                }
                else
                    $('#delete1').button( "option", "disabled", true ); 
            }
            if(isProcess){
                $('.btn-raid-create').button("option", "disabled", true);
                $('.btn-raid-expand').button("option", "disabled", true);
                $('.btn-raid-destroy').button("option", "disabled", true);
            }
            if(check_Unimportable) {
                var temp_str = '';
                for(var i = 0; i < Unimportable_RAID_ID.length; i++) {
                    if(i==Unimportable_RAID_ID.length-1)
                        temp_str += Unimportable_RAID_ID[i];
                    else {
                        switch(SysCfg_lang_ver){
                            case 'zh-tw':
                                temp_str += Unimportable_RAID_ID[i] + '、';
                                break;
                            case 'zh-cn': 
                                temp_str += Unimportable_RAID_ID[i] + '、';
                                break;
                            case 'en-us':
                                temp_str += Unimportable_RAID_ID[i] + ',';
                                break;       
                        } 
                    }                                        
                }
                $("#label_alert_unimportable").text(LanguageStr_Raid.Str_alert_unimportable.replace("@",temp_str));
                $("#label_alert_unimportable").show();
                showInitialBtn = true;
            } else {
                $("#label_alert_unimportable").hide();
                showInitialBtn = false;
            }
        }
        tmp_raid_length = raid_count;
        if(PollingListRAID){                    
            setTimeout(function(){bRunPollingListRAID = false;SendPollingListRAID();},6000);
        }
        else
            bRunPollingListRAID = false;
    });      
    
    // Ajax Fail
    request.fail( function( jqXHR, textStatus )
    { 
        if(PollingListRAID){                    
            setTimeout(function(){bRunPollingListRAID = false;SendPollingListRAID();},5000);
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
        // alert(jqXHR.responseText);
        GetAjaxData = AjaxData;
        // alert(jqXHR.responseText);
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
        if(jqXHR.responseText.trim() !== '{ }'){
            raid_count = AjaxData.length;       
        }
        if(bBlock)
            $('.Div_WaitAjax').dialog( 'close' );            
        if(raid_count !== tmp_raid_length)    
            ListDataToUI_RAID(AjaxData,false);
        else{
            var OtherIDHaveRAID = false;
            var ID1HaveRAID = false;
            var ID1InGluster = false;
            var isProcess = false;
            var check_Unimportable = false;       
            Unimportable_RAID_ID = [];     
            $.each( AjaxData, function(index, element){
                $('#info'+element['RAIDID']).removeClass('list_unimportable_RAID');
                if(element['Status'].indexOf('unimportable') != -1) {
                    isProcess = true;
                    check_Unimportable = true;
                    $('#info'+element['RAIDID']).addClass('list_unimportable_RAID');
                    Unimportable_RAID_ID.push('RAID ID ' + element['RAIDID']); 
                }                                       
                if(element['RAIDID'] == 1 && element['Level'].length > 0 && element['Status'].indexOf("creating") ==-1 && element['Status'].indexOf("deleting")==-1){
                    ID1HaveRAID = true;
                    if(element['InGluster'])
                        ID1InGluster = true;
                }
                raidsWithkey[element['RAIDID']]['Level'] = element['Level'];
                raidsWithkey[element['RAIDID']]['Used'] = element['Used'];
                raidsWithkey[element['RAIDID']]['Total'] = element['Total'];
                raidsWithkey[element['RAIDID']]['Allocate'] = element['Allocate'];
                raidsWithkey[element['RAIDID']]['Status'] = element['Status'];
                raidsWithkey[element['RAIDID']]['InGluster'] = element['InGluster']; 
                // var tp = element['TP'] === 1 ? LanguageStr_Raid.RAID_Enable : LanguageStr_Raid.RAID_Disable;
                // $(".RAIDList_TP_Row").html(tp);                
                if(element['Status'].length > 0)            
                    $("#raidLevel" + element['RAIDID']).html(element['Level']);
                else     
                    $("#raidLevel" + element['RAIDID']).html("Empty");                
                var used = new Number(element['Used']);
                var total = new Number(element['Total']);
                var allocate = new Number(element['Allocate']);
                used = used.toFixed(2);       
                total = total.toFixed(2);    
                allocate = allocate.toFixed(2);  
                if(element['Level'].length > 0) {
                    $("#raidAssign" + element['RAIDID']).html(add_comma_of_number(allocate) + " GB");              
                    $("#raidFree" + element['RAIDID']).html(add_comma_of_number(used) + " GB");
                    $("#raidTotal" + element['RAIDID']).html(add_comma_of_number(total) + " GB");
                }                    
                else{
                    $("#raidAssign" + element['RAIDID']).html('');              
                    $("#raidFree" + element['RAIDID']).html('');
                    $("#raidTotal" + element['RAIDID']).html('');
                }              
                var status= element['Status'].substring(0,1).toUpperCase();
                status += element['Status'].substring(1).toLowerCase();
                if(element['Status'].length > 0){
                    $('#create'+element['RAIDID']).button( "option", "disabled", true );
                }   
                else{
                    if(element['RAIDID'] > 1){ 
                        if(ID1HaveRAID == false){
                            $('#create'+element['RAIDID']).button( "option", "disabled", true );
                        }
                        else{                            
                            $('#create'+element['RAIDID']).button( "option", "disabled", false );
                        }
                    }else
                        $('#create'+element['RAIDID']).button( "option", "disabled", false );
                }                  
                $('#raidStatus'+element['RAIDID']).html(status);                   
                if(element['Dedupe'])
                    $("#dedupe" + element['RAIDID']).iCheck('check');
                else
                    $("#dedupe" + element['RAIDID']).iCheck('uncheck');
                if(element['Status'] == '' || element['Status'].indexOf("creating") !=-1 || element['Status'].indexOf("deleting")!=-1 || element['Status'].indexOf("adding")!=-1) { // 2017.11.13 william 新增Raid擴充，增加adding狀態
                    if(element['Status'] != '') 
                        isProcess = true;  
                    $('#delete'+element['RAIDID']).button( "option", "disabled", true ); 
                    $('#expand'+element['RAIDID']).button( "option", "disabled", true ); // 2017.11.13 william 新增Raid擴充
                    $('#locate'+element['RAIDID']).button( "option", "disabled", true ); 
                    $('#info'+element['RAIDID']).button( "option", "disabled", true );
                    $("#dedupe" + element['RAIDID']).iCheck('disable');
                }                    
                else {                    
                    $("#dedupe" + element['RAIDID']).iCheck('enable');
                    if(element['RAIDID'] > 1){
                        if(element['InGluster'])
                            $('#delete'+element['RAIDID']).button( "option", "disabled", true ); 
                        else{
                            $('#delete'+element['RAIDID']).button( "option", "disabled", false ); 
                        }                        
                    }
                    $('#expand'+element['RAIDID']).button( "option", "disabled", false ); // 2017.11.13 william 新增Raid擴充
                    $('#locate'+element['RAIDID']).button( "option", "disabled", false ); 
                    $('#info'+element['RAIDID']).button( "option", "disabled", false );
                }                               
                if(element['Status'] != '' && element['RAIDID'] > 1)
                    OtherIDHaveRAID = true;                    
            })
            if(OtherIDHaveRAID)
                $('#delete1').button( "option", "disabled", true ); 
            else{
                if(ID1HaveRAID){
                    if(ID1InGluster)
                        $('#delete1').button( "option", "disabled", true ); 
                    else
                        $('#delete1').button( "option", "disabled", false ); 
                }
                else
                    $('#delete1').button( "option", "disabled", true );
            }
            if(isProcess){
                $('.btn-raid-create').button("option", "disabled", true);
                $('.btn-raid-expand').button("option", "disabled", true);
                $('.btn-raid-destroy').button("option", "disabled", true);
            }
            if(check_Unimportable) {
                var temp_str = '';
                for(var i = 0; i < Unimportable_RAID_ID.length; i++) {
                    if(i==Unimportable_RAID_ID.length-1)
                        temp_str += Unimportable_RAID_ID[i];
                    else {
                        switch(SysCfg_lang_ver){
                            case 'zh-tw':
                                temp_str += Unimportable_RAID_ID[i] + '、';
                                break;
                            case 'zh-cn': 
                                temp_str += Unimportable_RAID_ID[i] + '、';
                                break;
                            case 'en-us':
                                temp_str += Unimportable_RAID_ID[i] + ',';
                                break;       
                        } 
                    } 
                }
                $("#label_alert_unimportable").text(LanguageStr_Raid.Str_alert_unimportable.replace("@",temp_str));
                $("#label_alert_unimportable").show();
                $('.btn-disk-initialize').show();
            } else {
                $("#label_alert_unimportable").hide();
                $('.btn-disk-initialize').hide();
            }           
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




function ListDataToUI_RAID(JsonData,isSort)
{
    var HtmlStr = "";
    var RAIDCount = 0;    
    var no = 0;    
    var ID1HaveRAID = false;
    var ID1InGluster = false;
    var OtherIDHaveRAID = false;
    var isProcess = false;
    var check_Unimportable = false;
    Unimportable_RAID_ID = [];
    if(isSort){
        if(raidsWithkey[1]['Level'].length > 0 && raidsWithkey[1]['Status'].indexOf("creating") ==-1 && raidsWithkey[1]['Status'].indexOf("deleting")==-1){
            ID1HaveRAID = true;
        }
    }   
    $.each( JsonData, function(index, element){  
        // alert(element['Status']);
        if(element['Status'].indexOf('unimportable') != -1) {
            isProcess = true;
            check_Unimportable = true;    
            Unimportable_RAID_ID.push('RAID ID ' + element['RAIDID']);         
        }                                  
        if(!isSort){         
            if(element['RAIDID'] == 1 && element['Level'].length > 0 && element['Status'].indexOf("creating") ==-1 && element['Status'].indexOf("deleting")==-1){
                ID1HaveRAID = true;
                if(element['InGluster'])
                    ID1InGluster = true;
            }
        }
        raidsWithkey[element['RAIDID']] = element;
        // var status= element['Status'].substring(0,1).toUpperCase();
        // status += element['Status'].substring(1).toLowerCase();
        var status= element['Status'].substring(0,1).toUpperCase();
        status += element['Status'].substring(1).toLowerCase();
        var isdegraded = false;
        // var tp = element['TP'] === 1 ? LanguageStr_Raid.RAID_Enable : LanguageStr_Raid.RAID_Disable;
        if(element['Status'] === 'Degrade')
            isdegraded = true;
        HtmlStr += '<div class = "Div_RAIDRow" >';
	         HtmlStr += '<div style="padding-top:45px;">';
        no = index + 1;
        HtmlStr += '<H4 class = "RAIDList_ItemNo_Row AbsoluteLayout RowField" >' + no  + '</H4>';    	 

        HtmlStr += '<H4 id="raidID'+element['RAIDID']+'" class = "RAIDList_RAIDID_Row AbsoluteLayout RowField" >' +  element['RAIDID']  + '</H4>';
        if(element['Status'].length > 0)
            HtmlStr += '<H4 id="raidLevel'+element['RAIDID']+'" class = "RAIDList_Level_Row AbsoluteLayout RowField" >' +  element['Level'] + '</H4>';            
        else   
            HtmlStr += '<H4 id="raidLevel'+element['RAIDID']+'" class = "RAIDList_Level_Row AbsoluteLayout RowField" >Empty</H4>';                    
        var used = new Number(element['Used']);
        var total = new Number(element['Total']);
        var allocate = new Number(element['Allocate']);
        used = used.toFixed(2);       
        total = total.toFixed(2);    
        allocate = allocate.toFixed(2); 
        if(element['Level'].length > 0) {
           // HtmlStr += '<H4 id="raidAssign'+element['RAIDID']+'" class = "RAIDList_VMAssign_Capacity_Row AbsoluteLayout RowField" >' + add_comma_of_number(allocate) +  ' GB</H4>';        
           HtmlStr += '<H4 id="raidFree'+element['RAIDID']+'" class = "RAIDList_FreeCapacity_Row AbsoluteLayout RowField" >' + add_comma_of_number(used) +  ' GB</H4>';
           HtmlStr += '<H4 id="raidTotal'+element['RAIDID']+'" class = "RAIDList_Capacity_Row AbsoluteLayout RowField" >' + add_comma_of_number(total) + ' GB</H4>';        
        }      
        else{
           // HtmlStr += '<H4 id="raidAssign'+element['RAIDID']+'" class = "RAIDList_VMAssign_Capacity_Row AbsoluteLayout RowField" ></H4>';        
           HtmlStr += '<H4 id="raidFree'+element['RAIDID']+'" class = "RAIDList_FreeCapacity_Row AbsoluteLayout RowField" ></H4>';
           HtmlStr += '<H4 id="raidTotal'+element['RAIDID']+'" class = "RAIDList_Capacity_Row AbsoluteLayout RowField" ></H4>';            
        }
        // HtmlStr += '<H4 class = "RAIDList_TP_Row AbsoluteLayout RowField" >' +  tp + '</H4>';          
        HtmlStr += '<H4 id="raidStatus'+element['RAIDID']+'" class = "RAIDList_Status_Row AbsoluteLayout RowField" >' +  status + '</H4>';                                                  
        // 2017.11.20 william 打勾物件-dedup功能
        /*if(dedupe) {
            HtmlStr += '<div class = "Dedup_Select_Row AbsoluteLayout RowField">';
            HtmlStr += '<div style="position:relative">';
            isDisableButtonStr = '';
            if(element['Status'] == '' || element['Status'].indexOf("creating") !=-1 || element['Status'].indexOf("deleting")!=-1 || element['Status'].indexOf("adding")!=-1) {
                isDisableButtonStr = 'iCheckBoxDedupSelectDisable';
            }    
            if(element['Dedupe'])
                HtmlStr += '<input type="checkbox" id="dedupe' + element['RAIDID'] + '" class="iCheckBoxDedupSelect iCheckBoxDedupSelectCheck ' + isDisableButtonStr + '"><label style="margin-left:7px;position: relative;top: 3px;font-weight: bold;">Dedupe</label>';        
            else 
                HtmlStr += '<input type="checkbox" id="dedupe' + element['RAIDID'] + '" class="iCheckBoxDedupSelect ' + isDisableButtonStr + '"><label style="margin-left:7px;position: relative;top: 3px;font-weight: bold;">Dedupe</label>';        
            HtmlStr += '</div>';
            HtmlStr += '</div>';
        }*/
        HtmlStr += '<div id="raidAction'+element['RAIDID']+'" class = "RAIDList_Action_Row AbsoluteLayout RowField" >';  
//        if(isdegraded)
//            HtmlStr += '<a id="' + element['name'] + '" href="#" class="btn-raid-recovery" style="font-size: smaller;min-width: 80px">' + LanguageStr_Raid.RAID_Recovery + '</a>';                
//        else
//            HtmlStr += '<a id="' + element['name'] + '" href="#" class="btn-raid-recovery raid-not-degraded" style="font-size: smaller;min-width: 80px">' + LanguageStr_Raid.RAID_Recovery + '</a>';                
        isDisableButtonStr = ''
        if(status.length > 0)
            isDisableButtonStr = 'btn-raid-disable';
        if(element['RAIDID'] > 1 &&  ID1HaveRAID == false)
            isDisableButtonStr = 'btn-raid-disable';
        HtmlStr += '<a id="create' + element['RAIDID'] + '" class="btn-raid-create ' + isDisableButtonStr + '" style="font-size: 14px;min-width: 80px">' + LanguageStr_Raid.RAID_Create + '</a>';  
        isDisableButtonStr = '';
        if(element['InGluster'] || element['Status'] == '' || element['Status'].indexOf("creating") !=-1 || element['Status'].indexOf("deleting")!=-1 || element['Status'].indexOf("adding")!=-1) {
            if(element['Status'] != '') 
                isProcess = true;           
            isDisableButtonStr = 'btn-raid-disable';
        }            
        if(element['Status'] != '' && element['RAIDID'] > 1)
            OtherIDHaveRAID = true;                
        // 2017.11.13 william 新增Raid擴充
        HtmlStr += '<a id="expand' + element['RAIDID'] + '" class="btn-raid-expand ' + isDisableButtonStr + '" style="font-size: 14px;min-width: 80px">' + LanguageStr_Raid.RAID_Expand + '</a>';
        HtmlStr += '<a id="locate' + element['RAIDID'] + '" class="btn-raid-locate ' + isDisableButtonStr + '" style="font-size: 14px;min-width: 80px">' + LanguageStr_DiskMgr.Disk_Locate + '</a>';
        if(element['Status'].indexOf('unimportable') != -1)
            HtmlStr += '<a id="info' + element['RAIDID'] + '" class="btn-raid-info list_unimportable_RAID ' + isDisableButtonStr + '" style="font-size: 14px;min-width: 80px;width: 157px;">' + LanguageStr_Raid.Disk_Info + '</a>';
        else
            HtmlStr += '<a id="info' + element['RAIDID'] + '" class="btn-raid-info ' + isDisableButtonStr + '" style="font-size: 14px;min-width: 80px;width: 157px;">' + LanguageStr_Raid.Disk_Info + '</a>';
        HtmlStr += '<a id="delete' + element['RAIDID'] + '" class="btn-raid-destroy ' + isDisableButtonStr + '" style="font-size: 14px;min-width: 80px">' + LanguageStr_Raid.RAID_Delete + '</a>';        
        HtmlStr += '</div>';
        HtmlStr += '</div>';
        HtmlStr += '</div> ';
        
        RAIDCount++;
    });        
    tmp_raid_length = RAIDCount;
    $('#Div_RAIDList').html( HtmlStr );
    $('.btn-raid-create').button(); 
    $('.btn-raid-recovery').button();        
    $('.btn-raid-destroy').button();     
    $('.btn-raid-expand').button(); // 2017.11.13 william 新增Raid擴充 加這段才能在螢幕變成按鈕式樣    
    $('.btn-raid-locate').button();    
    $('.btn-raid-info').button();    
    $('.btn-raid-disable').button("option", "disabled", true);
    RebindRAIDActionEvent();
    tmp_raid_length = RAIDCount;
    if(OtherIDHaveRAID)
        $('#delete1').button( "option", "disabled", true ); 
    else{        
        if(ID1HaveRAID){
            if(ID1InGluster)
                $('#delete1').button( "option", "disabled", true ); 
            else
                $('#delete1').button( "option", "disabled", false ); 
        }
        else
            $('#delete1').button( "option", "disabled", true ); 
    }
    if(isProcess){
        $('.btn-raid-create').button("option", "disabled", true);
        $('.btn-raid-expand').button("option", "disabled", true);
        $('.btn-raid-destroy').button("option", "disabled", true);
    }
    if(check_Unimportable) {
        var temp_str = '';
        for(var i = 0; i < Unimportable_RAID_ID.length; i++) {
            if(i==Unimportable_RAID_ID.length-1)
                temp_str += Unimportable_RAID_ID[i];
            else {
                switch(SysCfg_lang_ver){
                    case 'zh-tw':
                        temp_str += Unimportable_RAID_ID[i] + '、';
                        break;
                    case 'zh-cn': 
                        temp_str += Unimportable_RAID_ID[i] + '、';
                        break;
                    case 'en-us':
                        temp_str += Unimportable_RAID_ID[i] + ',';
                        break;       
                }                 
            }               
        }
        $("#label_alert_unimportable").text(LanguageStr_Raid.Str_alert_unimportable.replace("@",temp_str));
        $("#label_alert_unimportable").show();
        $('.btn-disk-initialize').show();
        showInitialBtn = true;
    } else {
        $("#label_alert_unimportable").hide();
        $('.btn-disk-initialize').hide();
        showInitialBtn = false;
    }    
    
    // 2017.11.17 william 修改CheckBox式樣
    $('.iCheckBoxDedupSelect').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });        
    $('.iCheckBoxDedupSelect').on('ifClicked', function(event){                            
        var element = $(this);
        ActionClickDedupCheck(element);
    });
    $('.iCheckBoxDedupSelectCheck').iCheck('check');
    $('.iCheckBoxDedupSelectDisable').iCheck('disable');     
   
    $('.Div_RAIDRow:odd').css( "background-color", "#E1FFFF" );
    $('.Div_RAIDRow:even').css( "background-color", "#E1FFF3" );    	
    // 2017.11.13 william 新增Raid擴充
    // if ($('.Div_RAIDRow').is(":even")) { 
    //     $(".RAIDList_Action_Row").css({"left": "870px", "width": "340px","background-color": "#E1FFF3"});
    //     $(".RAIDList_Status_Row").css({"background-color": "#E1FFF3"});        
    // }
    // else if($('.Div_RAIDRow').is(":odd")){ 
    //     $(".RAIDList_Action_Row").css({"left": "870px", "width": "340px","background-color": "#E1FFFF"});
    //     $(".RAIDList_Status_Row").css({"background-color": "#E1FFFF"});
    // }    

    $('.Div_RAIDRow').each(function(i, el) {
    // As a side note, this === el.
        if (i % 2 === 0) { /* we are even */          
            $(".RAIDList_Action_Row:even").css({"left": "790px", "width": "500px","background-color": "#E1FFF3"});
            $(".RAIDList_Status_Row:even").css({"background-color": "#E1FFF3"});     
            $(".RAIDList_Capacity_Row:even").css({"background-color": "#E1FFF3"});                          
            $(".Dedup_Select_Row:even").css({"background-color": "#E1FFF3"});
        } else { /* we are odd */
            $(".RAIDList_Action_Row:odd").css({"left": "790px", "width": "500px","background-color": "#E1FFFF"});
            $(".RAIDList_Status_Row:odd").css({"background-color": "#E1FFFF"});         
            $(".RAIDList_Capacity_Row:odd").css({"background-color": "#E1FFFF"});                   
            $(".Dedup_Select_Row:odd").css({"background-color": "#E1FFFF"});          
        }
    });    
    // GetDedupeList();    
}

function RebindRAIDActionEvent()
{
    $('.btn-raid-create').click(function(event){
        var element = $(this);
        event.preventDefault();     
        clickRAIDID = element.attr('id').substring(6);
        GetDiskForCreateTableList();
    });
    $('.btn-raid-recovery').click(function(event){
        var element = $(this);
        event.preventDefault();   
        var confirmable = confirm(LanguageStr_Raid.RAID_Recovery_Ask);
        if (confirmable === true) {
            Ajax_RecoveryRaid(element.attr('id').substring(6));
        }       
    });
    $('.btn-raid-destroy').click(function(event){
        var element = $(this);
        event.preventDefault();        
        clickRAIDID = element.attr('id').substring(6);
        if(raidsWithkey[clickRAIDID]['Allocate'] > 0){
            alert(LanguageStr_Raid.RAID_Waring_Have_VD);
        }
        else{
            var confirmable = confirm(LanguageStr_Raid.RAID_Delete_Ask);
            if (confirmable === true) {
                Ajax_DeleteRaid(element.attr('id').substring(6));
            }
        }
    });
    // 2017.11.13 william 新增Raid擴充
    $('.btn-raid-expand').click(function(event){
        var element = $(this);    
        clickRAIDID = element.attr('id').substring(6);
        var raidLevel = raidsWithkey[clickRAIDID]['Level'];
        RAID_Expend_Level = raidLevel;        
        event.preventDefault();      
        $("#dialog_create_expand").dialog('open');  
        $('#Div_RAIDExpandList').html('');
        InitUnusedDiskSelectAllCheckBox();
        UnusedDiskList();
        switch(RAID_Expend_Level){
            case 'Single Disk/JBOD': 
                $('.iCheckBoxAllUnusedDiskSelect').iCheck('enable');
                break;
            case 'RAID 1': 
                $('.iCheckBoxAllUnusedDiskSelect').iCheck('disable');
                break;        
            case 'RAID 5':
                $('.iCheckBoxAllUnusedDiskSelect').iCheck('enable');
                break;
            case 'RAID 6':
                $('.iCheckBoxAllUnusedDiskSelect').iCheck('enable');
                break;
            case 'RAID 7':
                $('.iCheckBoxAllUnusedDiskSelect').iCheck('enable');
                break;           
        }  
    });     
    $('.btn-raid-locate').click(function(event){
        var element = $(this);        
        event.preventDefault(); 
        var getid = element.attr('id').substring(6);
        // console.log(element.attr('id').substring(6));
        Ajax_LocateRaid(getid);
    });
    $('.btn-raid-info').click(function(event){
        var element = $(this);
        var getid = element.attr('id').substring(4);
        event.preventDefault();   
        if(element.hasClass('list_unimportable_RAID'))  {
            // alert('list_unimportable_RAID');
            GetUnimportableRAIDList(getid);
        } else {
            GetRAIDDiskSlotList(getid);
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
    var request = CallAjax ( "/system/raid/recovery", StrJson, "" );
    
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
    var jsonStr = '{"RAIDID":' + PoolName + '}';
    var request = CallAjax ( "/manager/ceph", jsonStr, "", "DELETE" );
    
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
        if(typeof(NowRAIDSortType)=== 'undefined')
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

function Ajax_LocateRaid(PoolName) {  
    $('.Div_WaitAjax').dialog( 'open' );  	
    PollingListRAID = false;
    var jsonStr = '{"RAIDID":' + PoolName + '}';
    var request = CallAjax ( "/manager/ceph/locate", jsonStr, "", "POST" );
    
    request.done( function( AjaxData, statustext, jqXHR ){
        if ( true == DebugMsg ) {
            alert( "jqXHR:" + jqXHR.responseText );
        }

        if(typeof(AjaxData['status']) !== "undefined" && AjaxData['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        if(typeof(NowRAIDSortType)=== 'undefined') {
            var th = $('#poition .VMHeader');
            th.removeClass('desc');
            th.addClass('asc');
            NowRAIDSortDirection = SortDirection.ASC;
            NowRAIDSortType = SortType.String;
            NowRAIDSortDOMID = th.attr('id');
        }                                            
        setTimeout( function(){Ajax_ListDisk(false);}, 1000 );
        setTimeout( function(){PollingListRAID = true;Ajax_GetInfo_Raid(true);SendPollingListRAID();}, 1000 );        
    });

    request.fail( function( jqXHR, textStatus ){
        if ( true == DebugMsg )
        {
                alert( "jqXHR:" + jqXHR.responseText );
        }
        $('.Div_WaitAjax').dialog( 'close' );  
        // oUIError.CheckAuth(jqxhr.status,ActionStatus.DeleteRAID);
    });
}

// 2017.11.13 william 新增Raid擴充
function Raid_Expand_Dialog() {
    $("#dialog_create_expand").dialog({
        title: LanguageStr_Raid.RAID_Expand_DialogTitle,
        modal: true,
        resizable: false,
        draggable: true,
        closeOnEscape: false,
        autoOpen: false,
        dialogClass: "no-close",
        height: 563,
        width: 1040         
    });
    $('#btn_close_create_expand').click(function (event) {
        event.preventDefault();
        $("#dialog_create_expand").dialog('close');
    });
    $('#btn_create_expand').click(function (event) {
        event.preventDefault();
        DiskExpendCreate('iCheckBoxUnusedSelect');
    });
}
// 2017.11.13 william 新增Raid擴充
function DiskExpendCreate(checkboxclass) {
    var n = $( ".iCheckBoxUnusedSelect:checked" ).length;
    var cc = 1;
    idDiskExpend = '';
    $.each($('.' + checkboxclass + ':checked'), function (index, element) {
        if(cc==n) {
            idDiskExpend = idDiskExpend + '"'+ $(element).attr('id')+'"';
        }
        else {
            idDiskExpend = idDiskExpend + '"'+ $(element).attr('id')+'",';
        }
        cc++;        
    });
    DiskExpend(idDiskExpend);
}
// 2017.11.13 william 新增Raid擴充
function DiskExpend(idDiskExpend) {
    var jsonStr = '{'
    jsonStr += '"RAIDID":' + clickRAIDID + ',';
    jsonStr += '"Disks":[';
    jsonStr += idDiskExpend;
    jsonStr += ']}';
    // alert(jsonStr);
    var request = Ajax_DiskExpend(jsonStr);
    CallBackDiskExpend(request);
};
// 2017.11.13 william 新增Raid擴充
function CallBackDiskExpend(request) {
    
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
        $("#dialog_create_expand").dialog('close');
        setTimeout( function(){Ajax_ListDisk(false)}, 500 );
        setTimeout( function(){PollingListRAID = true;Ajax_GetInfo_Raid(true);SendPollingListRAID();}, 500 );   
    });
    request.fail(function (jqxhr, textStatus) {
        $('.Div_WaitAjax').dialog( 'close' );
        $("#dialog_create_expand").dialog('close');
    });
}
// 2017.11.13 william 新增Raid擴充
function Ajax_DiskExpend(id) {
    var request = CallAjax("/manager/ceph/expansion", id, "", "POST");
    return request;
};

function Ajax_CreateRaid() 
{
    var StrJson = '{';
    StrJson += '"RAIDID":' + clickRAIDID + ',';
    StrJson += '"Disks":[';                      
    var index = 0;
    $.each($( ".iCheckBox:checked" ),function(){
        var element = $(this);
        if(index !== 0)
           StrJson += ',';
        StrJson += '"' + element.attr('id') + '"';                 
        index++;
    });
    StrJson += '],"Level":"';
    var level = $('#combolevel').scombobox('val');
    StrJson += level;
    StrJson += '"}';        
    // alert(StrJson);
    $('.Div_WaitAjax').dialog( 'open' );
    PollingListRAID = false;
    // alert(StrJson);
    var request = CallAjax ( "/manager/ceph", StrJson, "" );    
		
    request.done( function( AjaxData, statustext, jqXHR ) {        
        $('.Div_WaitAjax').dialog( 'close' );
        $('#dialog_create_raid').dialog('close');
        if(typeof(AjaxData['status']) !== "undefined" && AjaxData['status'] == 2){
            Logout_Another_Admin_Login();
            return false;
        }
        else{
            if(typeof(NowRAIDSortType)=== 'undefined')
            {
                var th = $('#poition .VMHeader');
                th.removeClass('desc');
                th.addClass('asc');
                NowRAIDSortDirection = SortDirection.ASC;
                NowRAIDSortType = SortType.String;
                NowRAIDSortDOMID = th.attr('id');
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

/*=================== 2017.11.13 william 新增Raid擴充 Start ===================*/
function UnusedDiskList() {
    $('.Div_WaitAjax').dialog( 'open' );
    var request = Ajax_ListDisk_SendCommand();
    request.done(function (AjaxData, statustext, jqXHR) {
        $('.Div_WaitAjax').dialog('close');
        UnusedDiskListListToUI(AjaxData);
    });
    request.fail(function (jqXHR, textStatus) {
        // oUIError.CheckAuth(jqxhr.status, ActionStatus.ListSchedule);
    });
}

// 定義class承接json資料
function UnusedDiskListListToUI(JsonData) {
    var HtmlStr = "";
    $.each( JsonData, function(index, element){   
        DiskListTable[index] = new DiskClass(element['Slot'],element['Status'],element['Capacity'],element['Vendor'],element['Speed'],element['Model'],element['Firmware'],element['DevID']);
    });   
    CreateUnusedDiskListHtml();
}

// 將相關資料List出來
function CreateUnusedDiskListHtml() {
    var HtmlStr = "";    
    $.each( DiskListTable, function(index, element){        
        if(element['status']==1) {
            HtmlStr += '<div id="div_disk_' + element['postion'] +  '" class = "Div_DiskRow" >';
            
            HtmlStr += '<H4 id="' + element['dev_id'] + 'DiskList_Slot_Row" class = "DiskList_Slot_Row AbsoluteLayout RowField" >' + element['postion'] + '</H4>';
            // 打勾物件
            HtmlStr += '<div class = "UnusedDisk_Select_Row AbsoluteLayout RowField" style="position: relative; top: 6px; left: 15px;">';
            HtmlStr += '<div style="position:relative">';
            HtmlStr += '<input type="checkbox" id="' + element['dev_id'] + '" class="iCheckBoxUnusedSelect">';
            HtmlStr += '</div>';
            HtmlStr += '</div>';

            HtmlStr += '<H4 id="' + element['dev_id'] + 'DiskList_Slot_Row" class = "DiskList_Slot_Row AbsoluteLayout RowField" >' + element['postion'] + '</H4>';
            HtmlStr += '<H4 id="' + element['dev_id'] + 'DiskList_Status_Row" class = "DiskList_Status_Row AbsoluteLayout RowField" >' + Get_Disk_Status(element['status']) +  '</H4>';
            HtmlStr += '<H4 id="' + element['dev_id'] + 'DisktList_Capacity_Row" class = "DisktList_Capacity_Row AbsoluteLayout RowField" >' + add_comma_of_number(element['capacity']) + '</H4>';
            HtmlStr += '<H4 id="' + element['dev_id'] + 'DiskList_Vendor_Row" class = "DiskList_Vendor_Row AbsoluteLayout RowField" >' + Get_Disk_Vendor(element['vendor']) + '</H4>';
            HtmlStr += '<H4 id="' + element['dev_id'] + 'DiskList_Speed_Row" class = "DiskList_Speed_Row AbsoluteLayout RowField" >' + element['if_type'] + '</H4>';
            HtmlStr += '<H4 id="' + element['dev_id'] + 'DiskList_Model_Row" class = "DiskList_Model_Row AbsoluteLayout RowField" >' + element['model'] + '</H4>';
            HtmlStr += '<H4 id="' + element['dev_id'] + 'DiskList_FirmwareVer_Row" class = "DiskList_FirmwareVer_Row AbsoluteLayout RowField" >' + element['rev'] + '</H4>';                       			
            HtmlStr += '<div id="' + element['dev_id'] + 'DiskList_BtnInitial_Row" class = "DiskList_BtnInitial_Row AbsoluteLayout RowField" >';
            if(element['status'] === 9){                          			                    
                HtmlStr += '<a id="' + element['dev_id'] + '" href="#" class="btn-disk-reset" style="font-size: smaller;min-width: 80px">' + LanguageStr_DiskMgr.Disk_Rest + '</a>';                                    
            }
            HtmlStr += '</div> ';
            HtmlStr += '</div> ';
        }
    });   

    $('#Div_RAIDExpandList').html( HtmlStr );    
    UsedDiskQuantity(RAID_Expend_Level);
    // $('.btn-disk-reset').button();
    // RegEvt_Disk_RestBtn_Click();
    $('.Div_DiskRow:odd').css( "background-color", "#E1FFFF" );
    $('.Div_DiskRow:even').css( "background-color", "#E1FFF3" );        

    $('.iCheckBoxAllUnusedDiskSelect').iCheck('uncheck');
    $('.iCheckBoxUnusedSelect').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });
    $('.iCheckBoxUnusedSelect').iCheck('uncheck');
    
    $('.Div_DiskRow').each(function(i, el) {
    // As a side note, this === el.
        if (i % 2 === 0) { /* we are even */             
            $(".DiskList_Model_Row:even").css({"background-color": "#E1FFF3"});
            $(".DiskList_FirmwareVer_Row:even").css({"background-color": "#E1FFF3"});
            $(".DiskList_BtnInitial_Row:even").css({"background-color": "#E1FFF3"});
        } else { /* we are odd */
            $(".DiskList_Model_Row:odd").css({"background-color": "#E1FFFF"});
            $(".DiskList_FirmwareVer_Row:odd").css({"background-color": "#E1FFFF"});
            $(".DiskList_BtnInitial_Row:odd").css({"background-color": "#E1FFFF"});
        }
    });


};


function InitUnusedDiskSelectAllCheckBox() {
    $('.iCheckBoxAllUnusedDiskSelect').iCheck({
        checkboxClass: 'icheckbox_minimal-blue'
    });
    $('.iCheckBoxAllUnusedDiskSelect').on('ifChanged', function (event) {
        var element = $(this);
        if (element.prop('checked')) {
            $('.iCheckBoxUnusedSelect').iCheck('check');
            // $('.iCheckBoxDisable').iCheck('uncheck');
        }
        else {
            $('.iCheckBoxUnusedSelect').iCheck('uncheck');
        }
    });
    $("#btn_create_expand").button( "disable" );
}

function UsedDiskQuantity(Level) { 
     $('.iCheckBoxUnusedSelect').on('ifChanged', function(event){ 
         var n = $( ".iCheckBoxUnusedSelect:checked" ).length;
    switch(Level){
        case 'Single Disk/JBOD':
                if(n >= 1){
                    $("#btn_create_expand").button( "option", "disabled", false );
                    return;
                }                
                break
        case 'RAID 1':
            if(n == 2){
                $.each($(".iCheckBoxUnusedSelect:not(:checked)" ),function(index,element){
                   $(element).iCheck('disable');
                });
                $("#btn_create_expand").button( "option", "disabled", false );
                return;
            }
            else{
                $.each($(".iCheckBoxUnusedSelect:not(:checked)" ),function(index,element){
                   $(element).iCheck('enable');
                });
            }
            break;
        case 'RAID 5':
            if(n >= 3){
                $("#btn_create_expand").button( "option", "disabled", false );
                return;
            }
            break;
        case 'RAID 6':
            if(n >= 4){
                $("#btn_create_expand").button( "option", "disabled", false );
                return;
            }
            break;
        case 'RAID 7':
            if(n >= 5){
                $("#btn_create_expand").button( "option", "disabled", false );
                return;
            }
            break;            
    }
            $("#btn_create_expand").button( "option", "disabled", true );
    }); 
}
/*=================== 2017.11.13 william 新增Raid擴充 End ===================*/

// 2017.11.17 william Dedup功能實作
function ActionClickDedupCheck(element){   
    StartorStopPollingRAID(false);         
    // var sets = [];
    var raidID = element.attr('id').substring(6);
    // var ischeck = !element.prop('checked');             
    // ChangeVMNametoID(vd_name);                       
    // sets.push({VDID:vmid,Suspend:ischeck});
    // VMModifyAutoSuspend(sets,true);
    if (element.is(":checked")) {
        IsDedupe = false;
    }
    else {
        IsDedupe = true;
    }
    DedupeEnable(raidID,IsDedupe);
}

function DedupeEnable(raidID,IsDedupe) {
    var setdedupejson = '{';
    setdedupejson += '"RAIDID" : ' + raidID;
    setdedupejson += ',"Dedupe" : ' + IsDedupe;
    setdedupejson += '}';
    // alert(setdedupejson);
    $('.Div_WaitAjax').dialog( 'open' );    
    var request = Ajax_setdedupe(setdedupejson);
    CallBackSetDedupe(request);
}

function CallBackSetDedupe(request) {   
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

        // setTimeout( function(){Ajax_ListDisk(false)}, 500 );
        setTimeout( function(){Ajax_GetInfo_Raid(true)}, 500 );   
        StartorStopPollingRAID(true);
        SendPollingListRAID();
    });
    request.fail(function (jqxhr, textStatus) {
        $('.Div_WaitAjax').dialog( 'close' );
    });
}

function Ajax_setdedupe(JsonData) {
    var request = CallAjax("/manager/ceph/dedupe", JsonData, "", "PUT");
    return request;
}

// RAID Sorting
function RAIDHederClickSort(){
    $('.RAIDHeader').click(function(event){           
        event.preventDefault();
        StartorStopPollingRAID(false);
        var th = $(this);
        var eSortType = SortType.String;
        var eSortDirection = SortDirection.ASC;                    
        if(th.attr('id') === "Allocate" || th.attr('id') === "Total" || th.attr('id') === "Used")
            eSortType = SortType.Num;
        if(th.hasClass('asc')){
            $('.RAIDHeader').removeClass('asc');
            $('.RAIDHeader').removeClass('desc');
            $('.label_raid_arrow').html('');
            $('#' + th.attr('id') + '1').html('&#9660;');
//            th.removeClass('asc');
            th.addClass('desc');
            eSortDirection = SortDirection.Desc;
        }
        else{
            $('.RAIDHeader').removeClass('asc');
            $('.RAIDHeader').removeClass('desc');
            $('.label_raid_arrow').html('');
            $('#' + th.attr('id') + '1').html('&#9650;');            
//            th.removeClass('desc');
            th.addClass('asc');
            eSortDirection = SortDirection.ASC;
        }
        NowRAIDSortType = eSortType;
        NowRAIDSortDirection = eSortDirection;
        NowRAIDSortDOMID = th.attr('id');
        SortRAIDList(eSortDirection,eSortType,th.attr('id')); 
        // Ajax_GetInfo_Raid(false);
        ListDataToUI_RAID(GetAjaxData,true);
        StartorStopPollingRAID(true);
        SendPollingListRAID();
    });            
}
// RAID Sorting
function SortRAIDList(eSortDirection,eSortType,sSortVariable)
{                    
    switch ( eSortDirection )
    {
        case SortDirection.ASC:
            switch(eSortType)
            {
                case SortType.Num:
                    GetAjaxData.sort(function(a,b)
                    {                                   
                        return (a[sSortVariable] - b[sSortVariable]);
                    });
                    break;
                case SortType.String:
                    GetAjaxData.sort( function(a,b)
                    {                 
                        return a[sSortVariable] < b[sSortVariable] ? -1 : ( a[sSortVariable] > b[sSortVariable] ? 1 : 0 );
                    });
                    break;
            }
            break;

        case SortDirection.Desc:
            switch(eSortType)
            {
                case SortType.Num:
                    GetAjaxData.sort( function(a,b)
                    {                                                              
                        return (b[sSortVariable] - a[sSortVariable]);
                    });
                    break;
                case SortType.String:
                    GetAjaxData.sort( function(a,b)
                    {    
                        return a[sSortVariable] > b[sSortVariable] ? -1 : ( a[sSortVariable] < b[sSortVariable] ? 1 : 0 );
                    });
                    break;
            }
            break;               
    }                    
}      

/*=================== 2017.11.21 william 新增Dedupe功能 End ===================*/
        /* 2018.05.24 william 列表標題及內容的文字位置調整，針對中英文版式樣作不同的顯示標頭位置對齊 */         
        function Zfs_langStyleChange() {    
            
            switch(SysCfg_lang_ver){
                case 'zh-tw':
                    break;
                case 'zh-cn': 
                    break;
                case 'en-us':
                    Zfs_langStyle_en_us();
                    break;       
            } 
        }

    function Zfs_langStyle_en_us() {
        $(".RAIDList_ItemNo").css({"left": "2.5%"});
        $(".RAIDList_Level").css({"left": "6%"});        
        $(".RAIDList_VMAssign_Capacity").css({"left": "16.5%","width": "200px"});
        $(".RAIDList_FreeCapacity").css({"left": "32%"});
        $(".RAIDList_Capacity").css({"left": "43%","width": "130px"});
        $(".RAIDList_Action").css({"left": "80.5%"});
    }   


function Disk_Info_Dialog() {
    $("#dialog_disk_info").dialog({
        title: LanguageStr_Raid.Disk_Info_DialogTitle,
        modal: true,
        resizable: false,
        draggable: true,
        closeOnEscape: false,
        autoOpen: false,
        dialogClass: "no-close",
        height: 560,
        width: 720         
    });
    $('#btn_close_disk_info').click(function (event) {
        event.preventDefault();
        $("#dialog_disk_info").dialog('close');
    });
}  

function GetUnimportableRAIDList(id) {
    $('.Div_WaitAjax').dialog( 'open' );   
    var request = Ajax_ListDisk_Unimportable(id);    			
    request.done( function( AjaxData, statustext, jqXHR ) {     
        if ( true == DebugMsg )
        {
            alert( "jqXHR:" + jqXHR.responseText );
        }                   
        $('.Div_WaitAjax').dialog( 'close' );
        //CreateTableList(AjaxData);
        CreateUnimportableRAIDList(AjaxData, id, true);
        $('#dialog_disk_info').dialog('open');
    });        	
    request.fail( function( jqXHR, textStatus ){ 
        if ( true == DebugMsg )i
        {
            alert( "jqXHR:" + jqXHR.responseText );
        }
        $('.Div_WaitAjax').dialog( 'close' );
        // oUIError.CheckAuth(jqxhr.status,ActionStatus.ListDisk);
    });
}

function CreateUnimportableRAIDList(JsonData, id, isUnimportable)
{
    var append_str = '';
    var DataCount = 0;
    if(isUnimportable) {
        $.each( JsonData, function(index, element) {
            DataCount++;
            append_str += '<div class="wrap_div_for_unimportable">';
            append_str += '<div class = "RowHeader_for_unimportable">';
            append_str += '<div class = "RowField" style=" line-height: 35px; left: 10px; position: relative; margin: 0px; padding: 0px;">';
            append_str += '<div class="div_RowHeader">';
            append_str += '<H4 class="H4_RowHeader" style="text-align: center;">RAID ID ' + id + ':(#' + DataCount + ')</H4> ';
            append_str += '</div>';
            append_str += '</div>';
            append_str += '</div>';

            append_str += '<div class = "List_for_unimportable">';
        //console.log(element['Disks'].length);
            $.each( element['Disks'], function(diskindex, diskelement){
                append_str += '<div class="Div_VMRow">';
                append_str += '<label style="position:  relative;top: 12px;left: 12px;">Slot ' + diskelement + '</label>';
                append_str += '</div>';
            });
            append_str += '</div>';
            append_str += '</div>';
        });  
    } else {
        append_str += '<div class="wrap_div_for_unimportable">';
        append_str += '<div class = "RowHeader_for_unimportable">';
        append_str += '<div class = "RowField" style=" line-height: 35px; left: 10px; position: relative; margin: 0px; padding: 0px;">';
        append_str += '<div class="div_RowHeader">';
        append_str += '<H4 class="H4_RowHeader" style="text-align: center;">RAID ID ' + id + '</H4> ';
        append_str += '</div>';
        append_str += '</div>';
        append_str += '</div>';

        append_str += '<div class = "List_for_unimportable">';
        $.each( JsonData['Disks'], function(index, element){
            append_str += '<div class="Div_VMRow">';
            append_str += '<label style="position:  relative;top: 12px;left: 12px;">Slot ' + element + '</label>';
            append_str += '</div>';
        });
        append_str += '</div>';
        append_str += '</div>';     
    }

    $('#div_unimportable_list').html('');;
    $('#div_unimportable_list').html(append_str);
}

function GetRAIDDiskSlotList(id) {
    $('.Div_WaitAjax').dialog( 'open' );   
    var request = Ajax_ListDisk_Slot(id);    			
    request.done( function( AjaxData, statustext, jqXHR ) {     
        if ( true == DebugMsg )
        {
            alert( "jqXHR:" + jqXHR.responseText );
        }                   
        $('.Div_WaitAjax').dialog( 'close' );
        //CreateTableList(AjaxData);
        CreateUnimportableRAIDList(AjaxData, id, false);
        $('#dialog_disk_info').dialog('open');
    });        	
    request.fail( function( jqXHR, textStatus ){ 
        if ( true == DebugMsg )i
        {
            alert( "jqXHR:" + jqXHR.responseText );
        }
        $('.Div_WaitAjax').dialog( 'close' );
        // oUIError.CheckAuth(jqxhr.status,ActionStatus.ListDisk);
    });
}

function Ajax_ListDisk_Unimportable(id) {  						
    var request = CallAjax ( "/manager/ceph/unimportable/" + id, "", "json","GET");
    return request;
}

function Ajax_ListDisk_Slot(id) {  						
    var request = CallAjax ( "/manager/ceph/" + id, "", "json","GET");
    return request;
}