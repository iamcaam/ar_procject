var NowDiskSortType;
var NowDiskSortDirection;
var NowDiskSortDOMID;
var PollingListDisk; 
var bRunPollingListDisk;

// $(document).ready(function ()   
// {
//     headerRelocate();
// });

// function headerRelocate() // 磁碟管理的Header標頭文字位置 2017.02.23 william
// {
//     $DS_Header_label_width = $("#DiskList_Slot_1").width();
//     if($DS_Header_label_width<38){
//         $(".DiskList_Status").css({"left": "9%","width": "115px"});
//         $(".DisktList_Capacity").css({"left": "17%","width": "160px"});
//         $(".DiskList_Vendor").css({"left": "32%","width": "130px"});
//         $(".DiskList_Speed").css({"left": "44%","width": "150px"});
//         $(".DiskList_Model").css({"left": "58%","width": "150px"});
//         $(".DiskList_FirmwareVer").css({"left": "80%","width": "145px"});
//         $(".DiskList_BtnInitial").css({"left": "93%","width": "120px"});
//     }
// }

function InitDiskPage()
{    
    PollingListDisk = true;
    bRunPollingListDisk = false;
    $('#btn_refresh_disk').click(function(){
        if(typeof(NowDiskSortType)=== 'undefined')
        {
            var th = $('#poition .VMHeader');
            th.removeClass('desc');
            th.addClass('asc');
            NowDiskSortDirection = SortDirection.ASC;
            NowDiskSortType = SortType.String;
            NowDiskSortDOMID = th.attr('id');
        }                                            
        Ajax_ListDisk(true);  
        Ajax_GetInfo_Raid(false);
    });
    DiskHederClickSort();
    // DiskMgr_langStyleChange();
}

function StartorStopPollingDisk(bstart){
    if(bstart){
        PollingListDisk = true;
    }
    else{                
        PollingListDisk = false;        
    }
};       

// 取得全部 Disk 資訊
function Ajax_ListDisk_SendCommand()
{  						
    var request = CallAjax ( "/manager/disk", "", "json","GET");
    return request;
}

function SendPollingListDisk()
{    
    if(!bRunPollingListDisk){        
//        console.log('Polling Disk');
        bRunPollingListDisk = true;        
        var request = Ajax_ListDisk_SendCommand();        
        CallbackPollingListDisk(request);
    }
}

function CallbackPollingListDisk(request)
{
    var bsameid = true;
    var tmpIndex;
    // Ajax Success
    request.done( function( AjaxData, statustext, jqXHR ){
//        $('.Div_WaitAjax').dialog( 'close' );
        if ( true == DebugMsg )
        {
            alert( "jqXHR:" + jqXHR.responseText );
        }
        if(typeof(AjaxData['status']) !== "undefined" && AjaxData['status'] == 2){
            Logout_Another_Admin_Login();
            return false;
        }        
        if(DiskListTable.length > 0){
            $.each( AjaxData, function(index, element){  
                tmpIndex = element['Slot'] -1;                
                // if(element['Status'] != DiskNoSortListTable[tmpIndex]['status'] || element['DevID'] != DiskNoSortListTable[tmpIndex]['dev_id'])
                // {
                    // alert(element['Status']);
                    // alert(DiskListTable[tmpIndex]['status']);
                    // alert(element['DevID']);
                    // alert(DiskListTable[tmpIndex]['dev_id']);
                    DiskNoSortListTable[tmpIndex]['dev_id'] = element['DevID'];
                    DiskNoSortListTable[tmpIndex]['status'] = element['Status'];
                    DiskNoSortListTable[tmpIndex]['model'] = element['Model'];
                    DiskNoSortListTable[tmpIndex]['vendor'] = element['Vendor'];
                    DiskNoSortListTable[tmpIndex]['capacity'] = element['Capacity'];
                    DiskNoSortListTable[tmpIndex]['if_type'] = element['Speed'];
                    DiskNoSortListTable[tmpIndex]['rev'] = element['Firmware'];
                    $('#div_disk_' + element['Slot'] + ' .DiskList_Status_Row').html(Get_Disk_Status(element['Status']));
                    $('#'+element['Slot']+'DisktList_Capacity_Row').html(add_comma_of_number(element['Capacity']));                    
                    $('#'+element['Slot']+'DiskList_Vendor_Row').html(Get_Disk_Vendor(element['Vendor']));                    
                    $('#'+element['Slot']+'DiskList_Speed_Row').html(Get_Disk_Speed(element['Speed']));
                    $('#'+element['Slot']+'DiskList_Model_Row').html(element['Model']);
                    $('#'+element['Slot']+'DiskList_FirmwareVer_Row').html(element['Firmware']);                                         
                    if(element['Status'] === 9){                          			                    
                        $('#'+element['Slot']+'DiskList_BtnInitial_Row').html('<a id="' + element['DevID'] + ":" + element['Slot'] + '" href="#" class="btn-disk-locate" style="font-size: 14px;min-width: 80px;top: 1px;">' + LanguageStr_DiskMgr.Disk_Locate + '</a><a id="' + element['DevID'] + '" href="#" class="btn-disk-reset" style="font-size: 14px;min-width: 80px;top: 1px;">' + LanguageStr_DiskMgr.Disk_Rest + '</a>');                                    
                    } else if(element['Status'] === 1) {
                        $('#'+element['Slot']+'DiskList_BtnInitial_Row').html('<a id="' + element['DevID'] + ":" + element['Slot'] + '" href="#" class="btn-disk-locate" style="font-size: 14px;min-width: 80px;top: 1px;">' + LanguageStr_DiskMgr.Disk_Locate + '</a><a id="' + element['DevID'] + '" href="#" class="btn-disk-initialize" style="font-size: 14px;min-width: 80px;top: 1px;">' + LanguageStr_DiskMgr.Disk_Initialize + '</a>');                        
                    }
                    else {
                        $('#'+element['Slot']+'DiskList_BtnInitial_Row').html('<a id="' + element['DevID'] + ":" + element['Slot'] + '" href="#" class="btn-disk-locate" style="font-size: 14px;min-width: 80px;top: 1px;">' + LanguageStr_DiskMgr.Disk_Locate + '</a>');                        
                    }  
                // }
                // }
            });
            $('.btn-disk-locate').button();
            $('.btn-disk-reset').button();
            $('.btn-disk-initialize').button();
            if(!showInitialBtn)
                $('.btn-disk-initialize').hide();
            RegEvt_Disk_RestBtn_Click();
            RegEvt_Disk_LocateBtn_Click();  
            RegEvt_Disk_InitializeBtn_Click();
            // if(!bsameid){
            //     Async_Ajax_ListDisk(AjaxData);
            // }
        }
        else
            Async_Ajax_ListDisk(AjaxData);      

        // RegEvt_Disk_LocateBtn_Click();            

        if(PollingListDisk){                    
            setTimeout(function(){bRunPollingListDisk = false;SendPollingListDisk();},6000);
        }
        else
            bRunPollingListDisk = false;
    });             
    request.fail( function( jqXHR, textStatus ){ 
//        $('.Div_WaitAjax').dialog( 'close' );
//        bRunPollingListDisk = false;
//        if ( true == DebugMsg )
//        {
//            alert( "jqXHR:" + jqXHR.responseText );
//        }
//        oUIError.CheckAuth(jqxhr.status,ActionStatus.ListDisk);    
    });
}

function Ajax_ListDisk(bBlock)
{    			    
    if(bBlock)
        $('.Div_WaitAjax').dialog( 'open' );
    var request = Ajax_ListDisk_SendCommand();    
    var bsameid = true;
    var tmpIndex;
    // Ajax Success
    request.done( function( AjaxData, statustext, jqXHR ){
        $('.Div_WaitAjax').dialog( 'close' );
        if ( true == DebugMsg )
        {
            alert( "jqXHR:" + jqXHR.responseText );
        }
        if(typeof(AjaxData['status']) !== "undefined" && AjaxData['status'] == 2){
            Logout_Another_Admin_Login();
            return false;
        }        
        if(DiskListTable.length > 0){
            $.each( AjaxData, function(index, element){  
                tmpIndex = element['Slot'] - 1;
                // if(element['DevID'] !== DiskListTable[index]['dev_id']){
                //     bsameid = false;
                //     return false;
                // }
                // else{                            
                // if(element['Status'] != DiskNoSortListTable[tmpIndex]['status'] || element['DevID'] != DiskNoSortListTable[tmpIndex]['dev_id'])
                // {
                    DiskNoSortListTable[tmpIndex]['dev_id'] = element['DevID'];
                    DiskNoSortListTable[tmpIndex]['status'] = element['Status'];
                    DiskNoSortListTable[tmpIndex]['model'] = element['Model'];
                    DiskNoSortListTable[tmpIndex]['vendor'] = element['Vendor'];
                    DiskNoSortListTable[tmpIndex]['capacity'] = element['Capacity'];
                    DiskNoSortListTable[tmpIndex]['if_type'] = element['Speed'];
                    DiskNoSortListTable[tmpIndex]['rev'] = element['Firmware'];
                    $('#div_disk_' + element['Slot'] + ' .DiskList_Status_Row').html(Get_Disk_Status(element['Status']));
                    $('#'+element['Slot']+'DisktList_Capacity_Row').html(add_comma_of_number(element['Capacity']));
                    $('#'+element['Slot']+'DiskList_Vendor_Row').html(Get_Disk_Vendor(element['Vendor']));
                    $('#'+element['Slot']+'DiskList_Speed_Row').html(element['Speed']);
                    $('#'+element['Slot']+'DiskList_Model_Row').html(element['Model']);
                    $('#'+element['Slot']+'DiskList_FirmwareVer_Row').html(element['Firmware']);  
                    if(element['Status'] === 9){                          			                    
                        $('#'+element['Slot']+'DiskList_BtnInitial_Row').html('<a id="' + element['DevID'] + ":" + element['Slot'] + '" href="#" class="btn-disk-locate" style="font-size: 14px;min-width: 80px;top: 1px;">' + LanguageStr_DiskMgr.Disk_Locate + '</a><a id="' + element['DevID'] + '" href="#" class="btn-disk-reset" style="font-size: 14px;min-width: 80px;top: 1px;">' + LanguageStr_DiskMgr.Disk_Rest + '</a>');                                    
                          
                        // $('.btn-disk-locate').button();
                    } else if(element['Status'] === 1) {
                        $('#'+element['Slot']+'DiskList_BtnInitial_Row').html('<a id="' + element['DevID'] + ":" + element['Slot'] + '" href="#" class="btn-disk-locate" style="font-size: 14px;min-width: 80px;top: 1px;">' + LanguageStr_DiskMgr.Disk_Locate + '</a><a id="' + element['DevID'] + '" href="#" class="btn-disk-initialize" style="font-size: 14px;min-width: 80px;top: 1px;">' + LanguageStr_DiskMgr.Disk_Initialize + '</a>');                                                           
                    }
                    else {
                        $('#'+element['Slot']+'DiskList_BtnInitial_Row').html('<a id="' + element['DevID'] + ":" + element['Slot'] + '" href="#" class="btn-disk-locate" style="font-size: 14px;min-width: 80px;top: 1px;">' + LanguageStr_DiskMgr.Disk_Locate + '</a>');                       
                    } 
                // }
            // }
            });
            $('.btn-disk-locate').button();
            $('.btn-disk-reset').button();
            $('.btn-disk-initialize').button();
            if(!showInitialBtn)
                $('.btn-disk-initialize').hide();
            RegEvt_Disk_RestBtn_Click();
            RegEvt_Disk_LocateBtn_Click();  
            RegEvt_Disk_InitializeBtn_Click();    
            // if(!bsameid){
            //     Async_Ajax_ListDisk(AjaxData);
            // }
        }
        else
            Async_Ajax_ListDisk(AjaxData);      

        // RegEvt_Disk_LocateBtn_Click();              
    });         
    // Ajax Fail
    request.fail( function( jqXHR, textStatus ){ 
        $('.Div_WaitAjax').dialog( 'close' );
        if ( true == DebugMsg )
        {
            alert( "jqXHR:" + jqXHR.responseText );
        }
        oUIError.CheckAuth(jqxhr.status,ActionStatus.ListDisk);    
//        OpenDialog_Message( LanguageStr_DiskMgr.Ajax01 );
    });
}

// 取得 Disk 資訊 的非同步函式
function Async_Ajax_ListDisk( JsonData )
{
    // 程式一開始讀取資料時不用顯示畫面
    if ( false === StartUp_DataRead )
    {
        ListDataToUI_Disk(JsonData);
    }
}


function Get_Disk_Status(Status)
{
    switch(Status){
        case 0 :
            return 'Empty';
        case 1 :
            return 'Unused';
        case 2 :
            return 'Ready';
        case 3 :
            return 'Recovering';
        case 9:
            return 'Unusable';
        case 10 :
            return 'Hotspare';
        case 11 :
            return 'Hotspare In Use';
        case -1:
            return 'Error';
        case -10:
            return 'Error(-10)';
        case -11:
            return 'Error(-11)';
        case -12:
            return 'Offline';
        default:
            return Status;
    }    
}

function Get_Disk_Vendor(Vendor)
{       
    if(Vendor != null){
        var n = Vendor.indexOf("Hitachi");
        if( n !== -1)
            return 'Hitachi';
        n = Vendor.indexOf("Western");
        if( n !== -1)
            return 'WD';
        n = Vendor.indexOf("Seagate");
        if( n !== -1)
            return 'Segate';    
    }
    else
        Vendor = '';
    return Vendor;
}

function Get_Disk_Speed(Speed)
{
    switch(Speed){
        case 0:
            return '';
        case 1:
            return 'SATA 1.5 Gbps';
        case 2:
            return 'SATA 3.0 Gbps';
        case 3:
            return 'SATA 6.0 Gbps';
        case 20:
            return 'iSCSI';
        default:
            return Speed;
    }
}

// 將資料顯示到畫面上
function ListDataToUI_Disk(JsonData)
{
    var HtmlStr = "";
    var tmpDisk;
    $.each( JsonData, function(index, element){   
        tmpDisk = new DiskClass(element['Slot'],element['Status'],element['Capacity'],element['Vendor'],element['Speed'],element['Model'],element['Firmware'],element['DevID']);
        DiskNoSortListTable[index] = tmpDisk;
        DiskListTable[index] = tmpDisk;
//        HtmlStr += '<div class = "Div_DiskRow" >';	 
//        HtmlStr += '<H4 class = "DiskList_Slot_Row AbsoluteLayout RowField" >' + element['postion'] + '</H4>';
//        HtmlStr += '<H4 class = "DiskList_Status_Row AbsoluteLayout RowField" >' + Get_Disk_Status(element['status']) +  '</H4>';
//        HtmlStr += '<H4 class = "DisktList_Capacity_Row AbsoluteLayout RowField" >' + element['capacity'] + '</H4>';
//        HtmlStr += '<H4 class = "DiskList_Vendor_Row AbsoluteLayout RowField" >' + Get_Disk_Vendor(element['vendor']) + '</H4>';
//        HtmlStr += '<H4 class = "DiskList_Speed_Row AbsoluteLayout RowField" >' + Get_Disk_Speed(element['if_type']) + '</H4>';
//        HtmlStr += '<H4 class = "DiskList_Model_Row AbsoluteLayout RowField" >' + element['model'] + '</H4>';
//        HtmlStr += '<H4 class = "DiskList_FirmwareVer_Row AbsoluteLayout RowField" >' + element['rev'] + '</H4>';                       			
//        if(element['status'] === 9){
//            HtmlStr += '<div class = "DiskList_BtnInitial_Row AbsoluteLayout RowField" >';                       			        
//            HtmlStr += '<a id="' + element['dev_id'] + '" href="#" class="btn-disk-reset" style="font-size: smaller;min-width: 80px">Reset</a>';                        
//            HtmlStr += '</div>';
//        }
//        HtmlStr += '</div> ';
    });    
    
    SortDiskList(NowDiskSortDirection,NowDiskSortType,NowDiskSortDOMID);
    $('#postion').addClass('asc');
    $('.label_disk_arrow').html('');
    $('#postion1').html('&#9650;');
    ListDataToUI_Disk_By_List();
//    $('#Div_DiskList').html( HtmlStr );    
//    $('.btn-disk-reset').button();
//    RegEvt_Disk_RestBtn_Click();
//    $('.Div_DiskRow:odd').css( "background-color", "#E1FFFF" );
//    $('.Div_DiskRow:even').css( "background-color", "#E1FFF3" );    
}

function ListDataToUI_Disk_By_List()
{
    var HtmlStr = "";    
    var no = 0;    
    $.each( DiskListTable, function(index, element){        
        HtmlStr += '<div id="div_disk_' + element['postion'] +  '" class = "Div_DiskRow" >';	 
        no = index + 1;
        HtmlStr += '<H4 class = "DiskList_ItemNo_Row AbsoluteLayout RowField" >' + no  + '</H4>';         
        HtmlStr += '<H4 id="' + element['postion'] + 'DiskList_Slot_Row" class = "DiskList_Slot_Row AbsoluteLayout RowField" >' + element['postion'] + '</H4>';
        HtmlStr += '<H4 id="' + element['postion'] + 'DiskList_Status_Row" class = "DiskList_Status_Row AbsoluteLayout RowField" >' + Get_Disk_Status(element['status']) +  '</H4>';
        HtmlStr += '<H4 id="' + element['postion'] + 'DisktList_Capacity_Row" class = "DisktList_Capacity_Row AbsoluteLayout RowField" >' + add_comma_of_number(element['capacity']) + '</H4>';
        HtmlStr += '<H4 id="' + element['postion'] + 'DiskList_Vendor_Row" class = "DiskList_Vendor_Row AbsoluteLayout RowField" >' + Get_Disk_Vendor(element['vendor']) + '</H4>';
        HtmlStr += '<H4 id="' + element['postion'] + 'DiskList_Speed_Row" class = "DiskList_Speed_Row AbsoluteLayout RowField" >' + element['if_type'] + '</H4>';
        HtmlStr += '<H4 id="' + element['postion'] + 'DiskList_Model_Row" class = "DiskList_Model_Row AbsoluteLayout RowField" >' + element['model'] + '</H4>';
        HtmlStr += '<H4 id="' + element['postion'] + 'DiskList_FirmwareVer_Row" class = "DiskList_FirmwareVer_Row AbsoluteLayout RowField" >' + element['rev'] + '</H4>';                       			
        HtmlStr += '<div id="' + element['postion'] + 'DiskList_BtnInitial_Row" class = "DiskList_BtnInitial_Row AbsoluteLayout RowField" >';
        HtmlStr += '<a id="' + element['dev_id'] + ":" + element['postion'] + '" class="btn-disk-locate" style="font-size: 14px;min-width: 80px;top: 1px;">' + LanguageStr_DiskMgr.Disk_Locate + '</a>';
        if(element['status']===1)
            HtmlStr += '<a id="' + element['dev_id'] + '" class="btn-disk-initialize" style="font-size: 14px;min-width: 80px;top: 1px;">' + LanguageStr_DiskMgr.Disk_Initialize + '</a>';             
        if(element['status'] === 9){                          			                    
            HtmlStr += '<a id="' + element['dev_id'] + '" href="#" class="btn-disk-reset" style="font-size: 14px;min-width: 80px;top: 1px;">' + LanguageStr_DiskMgr.Disk_Rest + '</a>';
        }
        HtmlStr += '</div> ';
        HtmlStr += '</div> ';        
    });    
    
    $('#Div_DiskList').html( HtmlStr );    
    $('.btn-disk-locate').button();
    $('.btn-disk-reset').button();
    $('.btn-disk-initialize').button();
    if(!showInitialBtn)
        $('.btn-disk-initialize').hide();
    RegEvt_Disk_RestBtn_Click();
    RegEvt_Disk_LocateBtn_Click();  
    RegEvt_Disk_InitializeBtn_Click();
    $('.Div_DiskRow:odd').css( "background-color", "#E1FFFF" );
    $('.Div_DiskRow:even').css( "background-color", "#E1FFF3" );    
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

}

function DiskHederClickSort(){
    $('.DiskHeader').click(function(event){           
        event.preventDefault();
        StartorStopPollingDisk(false);
        var th = $(this);
        var eSortType = SortType.String;
        var eSortDirection = SortDirection.ASC;                    
        if(th.attr('id') === "postion" || th.attr('id') === "status")
            eSortType = SortType.Num;
        if(th.hasClass('asc')){
            $('.DiskHeader').removeClass('asc');
            $('.DiskHeader').removeClass('desc');
            $('.label_disk_arrow').html('');
            $('#' + th.attr('id') + '1').html('&#9660;');
//            th.removeClass('asc');
            th.addClass('desc');
            eSortDirection = SortDirection.Desc;
        }
        else{
            $('.DiskHeader').removeClass('asc');
            $('.DiskHeader').removeClass('desc');
            $('.label_disk_arrow').html('');
            $('#' + th.attr('id') + '1').html('&#9650;');            
//            th.removeClass('desc');
            th.addClass('asc');
            eSortDirection = SortDirection.ASC;
        }
        NowDiskSortType = eSortType;
        NowDiskSortDirection = eSortDirection;
        NowDiskSortDOMID = th.attr('id');
        SortDiskList(eSortDirection,eSortType,th.attr('id')); 
        ListDataToUI_Disk_By_List();
        StartorStopPollingDisk(true);
        SendPollingListDisk();
    });            
}
        
function SortDiskList(eSortDirection,eSortType,sSortVariable)
{                    
    switch ( eSortDirection )
    {
        case SortDirection.ASC:
            switch(eSortType)
            {
                case SortType.Num:
                    DiskListTable.sort(function(a,b)
                    {	                                
                        return (a[sSortVariable] - b[sSortVariable]);
                    });
                    break;
                case SortType.String:
                    DiskListTable.sort( function(a,b)
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
                    DiskListTable.sort( function(a,b)
                    {                                                              
                        return (b[sSortVariable] - a[sSortVariable]);
                    });
                    break;
                case SortType.String:
                    DiskListTable.sort( function(a,b)
                    {    
                        return a[sSortVariable] > b[sSortVariable] ? -1 : ( a[sSortVariable] < b[sSortVariable] ? 1 : 0 );
                    });
                    break;
            }
            break;               
    }                    
}   

function RegEvt_Disk_RestBtn_Click()
{	
    $('.btn-disk-reset').click( function(e) 
    {        
        var element = $(this);
        var confirmable = confirm(LanguageStr_DiskMgr.Disk_Reset_Ask);
        if (confirmable === true) {   
            StartorStopPollingDisk(false);
            Ajax_ResetDisk(element.attr('id'));
        }
    });
}


function Ajax_ResetDisk( devid )
{
    StartorStopPollingDisk(false);
    var StrJson = '{"DevID":"' + devid + '"}';
    $('.Div_WaitAjax').dialog( 'open' );
    var request = CallAjax ( "/manager/disk/reset", StrJson, "" );
    		
    request.done( function( AjaxData, statustext, jqXHR ) {	
        $('.Div_WaitAjax').dialog( 'close' );
        if(typeof(AjaxData['status']) !== "undefined" && AjaxData['status'] == 2){
            Logout_Another_Admin_Login();
            return false;
        }   
        StartorStopPollingDisk(true);
        setTimeout( function(){Ajax_ListDisk(true);SendPollingListDisk();}, 500 ); 
    });
            
    request.fail( function( jqXHR, textStatus ){
        $('.Div_WaitAjax').dialog( 'close' );
        if ( true == DebugMsg )
        {
                alert( "jqXHR:" + jqXHR.responseText );
        }
        oUIError.CheckAuth(jqxhr.status,ActionStatus.ResetDisk);    
//        OpenDialog_Message( LanguageStr_DiskMgr.Ajax04 );
    });
}

function RegEvt_Disk_LocateBtn_Click() {	
    $('.btn-disk-locate').click( function(e) {        
        var element = $(this);
        var Slotarr;
        Slotarr = element.attr("id").split(":");
        // console.log(Slotarr[1]);
        Ajax_LocateDisk(Slotarr[1]);
    });
}

function Ajax_LocateDisk(Slot_Number) {
    // StartorStopPollingDisk(false);
    var StrJson = '{"Slot":' + Slot_Number + '}';
    $('.Div_WaitAjax').dialog( 'open' );
    var request = CallAjax ( "/manager/disk/locate", StrJson, "" );
    		
    request.done( function( AjaxData, statustext, jqXHR ) {	
        $('.Div_WaitAjax').dialog( 'close' );
        if(typeof(AjaxData['status']) !== "undefined" && AjaxData['status'] == 2){
            Logout_Another_Admin_Login();
            return false;
        }           
    });
            
    request.fail( function( jqXHR, textStatus ) {
        $('.Div_WaitAjax').dialog( 'close' );
        if ( true == DebugMsg ) {
            alert( "jqXHR:" + jqXHR.responseText );
        }
        oUIError.CheckAuth(jqxhr.status,ActionStatus.LocateDisk);    
    });
}

function RegEvt_Disk_InitializeBtn_Click() {	
    $('.btn-disk-initialize').click( function(e) {   
        var element = $(this);
        var confirmable = confirm(LanguageStr_DiskMgr.Disk_Initialize_Ask);
        if (confirmable === true) {                    
            Ajax_InitializeDisk(element.attr("id"));
        }                
    });
}

function Ajax_InitializeDisk(id) {
    $('.Div_WaitAjax').dialog( 'open' );
    var StrJson = '["' + id + '"]';
    var request = CallAjax ( "/manager/disk/initial", StrJson, "","POST");		
    request.done( function(msg, statustext, jqxhr) {	
        $('.Div_WaitAjax').dialog( 'close' );
        if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
            Logout_Another_Admin_Login();
            return false;
        }
        else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
            window.location.href = 'index.php';
            return false;
        }        
        alert(LanguageStr_DiskMgr.Disk_Initialize_Completed); 
    });
            
    request.fail( function(jqxhr, textStatu) {
        $('.Div_WaitAjax').dialog( 'close' ); 
        //alert(jqxhr.status);
    });
}

        /* 2018.05.24 william 列表標題及內容的文字位置調整，針對中英文版式樣作不同的顯示標頭位置對齊 */         
        function DiskMgr_langStyleChange() {    
            
            switch(SysCfg_lang_ver){
                case 'zh-tw':
                    break;
                case 'zh-cn': 
                    break;
                case 'en-us':
                    DiskMgr_langStyle_en_us();
                    break;       
            } 
        }

    function DiskMgr_langStyle_en_us() {
        $(".DiskList_ItemNo").css({"left": "2%","width": "115px"});
        $(".DiskList_Slot").css({"left": "6%","width": "115px"});
        $(".DiskList_Status").css({"left": "10.5%","width": "115px"});
        $(".DisktList_Capacity").css({"left": "17%","width": "160px"});
        $(".DiskList_Vendor").css({"left": "32%","width": "130px"});
        $(".DiskList_Speed").css({"left": "44%","width": "150px"});
        $(".DiskList_Model").css({"left": "58%","width": "150px"});
        $(".DiskList_FirmwareVer").css({"left": "80%","width": "145px"});
        $(".DiskList_BtnInitial").css({"left": "93%","width": "180px"});
    }  
