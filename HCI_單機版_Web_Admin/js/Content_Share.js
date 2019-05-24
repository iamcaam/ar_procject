/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var select_all;
var bolclickshift;
var lastSelected;
var lastSelectedRow;
var MovePostData;
var ResizeLastPage;
var IntoRecycle;
var RecycleListPath;
var IsSortOutTreeExpand = false;
var IsInfoTreeExpand = false;
var NowActionForZipDownload;
//var checksearchname_pattern = /^[:/,._]|[\\\*<>\|"/']+/;
var checksearchname_pattern = /^[:/,._]|[\\\*<>\|"/]+/;
var sid;
var cid;
var pwd;
var ChangeAddressAction = true;
var renameobj;
var mobile;
var detect_vlc = false;
$(document).ready(function ()
{
    IntoRecycle = 0;
    NowActionForZipDownload = '0';    
    SelectClickFunction();
    ExitClickFunction();       
    CreateTmpA();
    // PC 版
    if ($(document).find("#pkgLineTable").length > 0)
    {        
        mobile = false;
        detect_vlc = DetectVLC();        
        addressChangEvent();
        TableMouseDown(0);
        TableMove(0);
        TableMoveOut();
        TableClick(0);
        PCLayout();
        ZIPDownloadClick();                        
    }
    // 手機版
    else
    {
        mobile = true;
        addressChangEvent();        
        ChkBoxImgClick();        
    }

    $(".renamepc").css("cursor", "default");
    $("#imgClear").css("display", "none");
    select_all = true;   
});

// 視窗變更大小就會觸發
$(window).resize(function ()
{
    // 重新排版
    //PCLayout();
    
    setTimeout("ReLayoutLeftItem()", 300);       // 2013.03.18 Foxy 
});

function DetectVLC()
{
    var P = {name:"VLC", status:-1, version:null, minVersion:"1,0,0,0"};
    var Det=PluginDetect;
    if (Det.getVersion)
    {
        P.version = Det.getVersion(P.name);            
    };
    if (Det.isIE)
    {     
        if(Det.ActiveXEnabled){
            if (Det.isMinVersion)
            {
                P.status = Det.isMinVersion(P.name, P.minVersion);
                if(P.status == 1 || P.status == 0)
                    return true;
            };
        }          
    }
    else{
        if (Det.isMinVersion)
        {
            P.status = Det.isMinVersion(P.name, P.minVersion);
            if(P.status == 1 || P.status == 0)
                return true;
        };
    }
    return false;
}

function CreateTmpA()
{
    var a = document.createElement('a');            
    a.title = "";           
    a.id = 'atest';
    a.target = '_blank';
    a.style.display = "none";
    document.body.appendChild(a);
}

function PlayHyperFileClick()
{
    $('.PlayHyper').click(function () {        
        var tmphref = $(this).attr('href');
        var mediatype = tmphref.substring(3);
        var objid = $(this).attr('id');         
        var postData = {                        
            sid:sid, 
            id :idroot,                        
            key:key,
            obj:objid
        };                                                   
        if(detect_vlc){
            switch (mediatype)
            {
                case "1":
                case "2":
                case "7":           
                    opennewwindow("http://" + window.location.host + "/VLCPlay_Share.php?sid=" + sid + "&id=" + idroot + "&key=" + key + "&obj=" + objid); 
                    break;               
                case "3":                
                    GetPathAndRedirect(true,postData)
                    break;
            }
        }
        else{            
            switch (mediatype)
            {
                case "1":                    
                    opennewwindow("http://" + window.location.host + "/media.jplayer.share.php?sid=" + sid + "&id=" + idroot + "&key=" + key + "&obj=" + objid);                     
                    break;
                case "2":                      
                    opennewwindow("http://" + window.location.host + "/audio.jplayer.share.php?sid=" + sid + "&id=" + idroot + "&key=" + key + "&obj=" + objid);                     
                    break;               
                case "3":                
                    GetPathAndRedirect(true,postData)
                    break;
            }   
        }
        return false;
    });        
}

function MobilePlayHyperFileClick()
{
    $('.MPlayHyper').click(function () {        
        var tmphref = $(this).attr('href');
        var mediatype = tmphref.substring(3);
        var objid = $(this).attr('id');         
        var postData = {                        
            sid:sid, 
            id :idroot,                        
            key:key,
            obj:objid
        };                                                                     
        switch (mediatype)
        {
            case "1":
                opennewwindow("http://" + window.location.host + "/media.share.php?sid=" + sid + "&id=" + idroot + "&key=" + key + "&obj=" + objid);
                break;
            case "2":                      
                opennewwindow("http://" + window.location.host + "/audio.share.php?sid=" + sid + "&id=" + idroot + "&key=" + key + "&obj=" + objid);
                break;               
            case "3":                
                GetPathAndRedirect(true,postData)
                break;
        }   
        
        return false;
    });        
}

function DownloadClick()
{
    $('.DownloadShare').click(function () {                
        var objid = $(this).attr('id');                                                                      
        opennewwindow("download.share.php?sid=" + sid + "&id=" + idroot + "&key=" + key + "&obj=" + objid);                 
        return false;
    });        
}

function HyperFileClick()
{
    $('.hyperfile').click(function () {        
        var tmphref = $(this).attr('href');
        var mediatype = tmphref.substring(3);
        var objid = $(this).attr('id');        
        var postData = {                        
            sid:sid, 
            id :idroot,            
            act:"sharegetpath",
            key:key,
            obj:objid
        };                                                          
        switch (mediatype)
        {
            case "1":
            case "2":
            case "7":   
                if(detect_vlc)
                    opennewwindow("http://" + window.location.host + "/VLCPlay_Share.php?sid=" + sid + "&id=" + idroot + "&key=" + key + "&obj=" + objid); 
                else
                    GetPathAndRedirect(false,postData)
                break;
            case "0":
            case "3":
            case "4":
            case "5":    
            case "6":
                GetPathAndRedirect(true,postData)
                break;
        }        
        return false;
    });        
}

function GetPathAndRedirect(ssl,postData)
{
    
    $("body").css("cursor", "wait");
    openModalWaitNoReload('',0,0);           
    var request = CallAjax_NoAsync("ShareCmdProcessPost.php?act=sharegetpath", postData, "json");    
        request.done(function (msg, statustext, jqxhr) {   
            $.each( msg, function( index, element ) 
        {                               
            switch(index)
            {
                case 0:
                    if(element == '-1' || element == '-2' || element == '-3' || element == '-4' || element == '-5' || element == '-7')
                    {                       
                        alert("Can't Find Share.");                      
                    }                    
                    else if(element == '-6')
                        BtnBackRootClick();
                    break;
                case 1:
                    var redirectpath = element;            
                    bolcheck = false;
                    $("body").css("cursor", "default");           
                    modalWindow.closenoreload();           
                    host = window.location.host;
                    if(ssl)
                        var head = 'https://';
                    else
                        head = 'http://';
//                    $('#atest').attr('href' ,head + host + '/' + path);   
//                    var isSafari = Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0;
//                    if(isSafari){
//                        var event = document.createEvent("HTMLEvents");
//                        event.initEvent("click", true, true);
//                        document.getElementById('atest').dispatchEvent(event);
//                    }
//                    else{
//                        document.getElementById('atest').click();
//                    }
                    window.open(
                    head + host + '/' + redirectpath,
                    '_blank' 
                    );
                    break;                
                }
            });                        
            
        });
        request.fail(function (jqxhr, textStatus) {         
            alert(jqxhr.responseText);
            bolcheck = false;
            $("body").css("cursor", "default");  
            alert("Fail No Response.");            
            modalWindow.closenoreload();
        });
}

function addressChangEvent()
{    
    $.address.bind('change', function(event) {              
        if(ChangeAddressAction)
        {       
            var cid = event.parameters.cid;  
            if(typeof  cid == 'undefined' ){  
                //cid = 1;
                //RootGetFolderList(1);   
                //$.address.value('/');
                BtnBackRootClick();
            } 
            else
            {                            
                RootGetFolderList(cid);                    
            }                                        
        }
    });
}

function BtnBackRootClick()
{
    //window.location.assign('content_pc.php?path=/root/web/' + session + '/' + lunname + "&session=" + session);
    //GetFolderList(1);    
        document.location.hash = '/' + idroot + '/?cid=' + idroot;    
}

function RootGetFolderList(fid)
{
    NowActionForZipDownload = '0';
    if(!mobile) {                
        SetSelectAllChkFalse();
        GetFolderList(fid);    
    }
    else
        GetMobileFolderList(fid);
}

function GetMobileFolderList(fid)
{
    var postData = {                        
            sid:sid, 
            id:idroot,
            key:key,            
            cid:fid,
            key1:pwd
    };    
    $("body").css("cursor", "wait");
    openModalWaitNoReload('',0,0);        
    var request = CallAjax("ShareCmdProcessPost.php?act=mobilelfpublic", postData, "json");    
    request.done(function (msg, statustext, jqxhr) {        
        $.each( msg, function( index, element ) 
        {
            switch(index)
            {
                case 0:
                    if(element == '-1' || element == '-2' || element == '-3' || element == '-4' || element == '-5')
                    {                       
                        alert("Can't Find Share.");
                        $("#path").empty();
                        $(".listTable").empty();
                    }                    
                    else if(element == '-6')
                        BtnBackRootClick();
                    break;
                case 1:
                    $("#path").empty();
                    $("#path").append(element);
                    break;
                case 2:
                    $(".listTable").empty();
                    $(".listTable").append(element);
                    break;                
            }
        });
        RebindMobileClick();          
        $("body").css("cursor", "default");           
        modalWindow.closenoreload();
        cid = fid;
    });
    request.fail(function (jqxhr, textStatus) {            
        $("body").css("cursor", "default");  
        alert("Fail Get Content");        
        $(".listTable").empty();
        modalWindow.closenoreload();
    });
}

function GetFolderList(fid)
{     
    var postData = {                        
            sid:sid, 
            id:idroot,
            key:key,           
            cid:fid,
            key1:pwd
    };    
    $("body").css("cursor", "wait");
    openModalWaitNoReload('',0,0);            
    var request = CallAjax("ShareCmdProcessPost.php?act=lfpublic", postData, "json");    
    request.done(function (msg, statustext, jqxhr) {           
        $.each( msg, function( index, element ) 
        {                               
            switch(index)
            {
                case 0:
                    if(element == '-1' || element == '-2' || element == '-3' || element == '-4' || element == '-5')
                    {                       
                        alert("Can't Find Share.");
                        $("#path").empty();
                        $(".listTable").empty();
                    }                    
                    else if(element == '-6')
                        BtnBackRootClick();
                    break;
                case 1:
                    $("#path").empty();
                    $("#path").append(element);
                    break;
                case 2:
                    $(".listTable").empty();
                    $(".listTable").append(element);
                    break;                
            }
        });        
        RebindTableClickEvent();          
        $("body").css("cursor", "default");           
        modalWindow.closenoreload();
        cid = fid;
    });
    request.fail(function (jqxhr, textStatus) {         
        alert(jqxhr.responseText);
        $("body").css("cursor", "default");  
        alert("Fail Get Content");        
        $("#pkgLineTable").empty();
        modalWindow.closenoreload();
    });
}

function RebindTableClickEvent()
{   
    if(detect_vlc){           
        $('.AVLC').css("display", "inline");           
    }    
    DownloadClick();
    PlayHyperFileClick();
    HyperFileClick();
    TableMouseDown(0);
    TableMove(0);
    TableMoveOut();
    TableClick(0);       
}

function opennewwindow(location)
{
    window.open(
        location,
        '_blank' // <- This is what makes it open in a new window.
    );
}

function PCLayout()
{
    myLayout = $('body').layout({
        west__showOverflowOnHover: true
                        , fxSpeed: "slow"
                        , spacing_closed: 5
                        , north__spacing_open: 0
                        , south__spacing_open: 0
                        , west__spacing_open: 0
                        , west__size: 215
                        , south__size: 5
    });


    // Content1 是 Path 文字
    // Content2 是 按鈕列
    // Content3 是 全選鈕
    /*
    if (1155 < $(window).width())
    {
        $('.inner-layout-north').css("height", '165px');
        $('.intter-north-content2').css("height", '25px');
    }
    else
    {
        $('.inner-layout-north').css("height", '200px');
        $('.intter-north-content2').css("height", '60px');
    }
    */

    myLayout1 = $('.ui-layout-center').layout({
        north__paneSelector: ".inner-layout-north"
                        , center__paneSelector: ".inner-layout-center"
                        , fxSpeed: "slow"
                        , spacing_closed: 5
                        , north__spacing_open: 0
    });

    
    myLayout2 = $('.ui-layout-west').layout({

    north__paneSelector: ".inner-west-north"
                    , center__paneSelector: ".inner-west-center"
                    , fxSpeed: "slow"
                    , spacing_closed: 5
                    , north__spacing_open: 0
                    , north__size: 100
    });
    
    myLayout3 = $('.inner-west-center').layout({
        south__paneSelector: ".inner-west-center-south"
                        ,center__paneSelector: ".inner-west-center-center"
                        , fxSpeed: "slow"
                        , spacing_closed: 5
                        , south__spacing_open: 0
                        , south__size: 5
    })


}

//取得瀏覽器視窗寬度
function getBrowserWidth()
{
    if ($.browser.msie)
    {
        return document.compatMode == "CSS1Compat" ? document.documentElement.clientWidth :
                 document.body.clientWidth;
    } else
    {
        return self.innerWidth;
    }
}

//取得瀏覽器視窗高度
function getBrowserHeight()
{
    if ($.browser.msie)
    {
        return document.compatMode == "CSS1Compat" ? document.documentElement.clientHeight :
                 document.body.clientHeight;
    } else
    {
        return self.innerHeight;
    }
}

function TableMoveOut() {
    $("#pkgLineTable").mouseout(function () {
        //$(".row_mouseover").addClass('listTabletr')  
        $("#pkgLineTable tr").removeClass('row_mouseover');
    })
}

function TableMove(index) {
    $("#pkgLineTable tr").mouseover(function () {
        //alert($(this).index());
        if ($(this).index() != index) {
            $("#pkgLineTable tr").removeClass('row_mouseover');
            if (!$(this).hasClass('row_selected')) {
                //$(this).removeClass('listTabletr');
                $(this).addClass('row_mouseover');
            }
        }
    });
}

function TableClick(index) {
    $('.listTable tr').click(function (event) {
        var thisitem = $(this);
        if (thisitem.index() != index) {
            if (event.target.nodeName.toLowerCase() === 'img') {
                if ($(event.target).hasClass('chk')) {
                    if (thisitem.hasClass('row_selected')) {
                        thisitem.removeClass('row_selected');
                        //thisitem.addClass('listTabletr');
                        tempimg = thisitem.children("td").eq(0).children('.chk');
                        tempimg.removeClass('select');
                        tempimg.attr("src", "img/checkbox_empty.png");
                    }
                    else {
                        //thisitem.removeClass('listTabletr');
                        thisitem.addClass('row_selected');
                        tempimg = thisitem.children("td").eq(0).children('.chk');
                        tempimg.addClass('select');
                        tempimg.attr("src", "img/checkbox.png");
                    }
                    lastSelected = thisitem.closest("tr").prevAll("tr").length + 1;
                    EnableDisableZIPButton();
                }
            }
        }
    });

}

function TableMouseDown(index) {
    //disable text selection
    if ($.browser.mozilla) {//Firefox
        $('#pkgLineTable').css('-moz-user-select', 'none');
    } else if ($.browser.msie) {//IE
        $('#pkgLineTable').bind('selectstart', function () {return false;});
    } else {//Opera, etc.
        $('#pkgLineTable').mousedown(function () {return false;});
    }
    //disable text selection cursor
    $('#pkgLineTable').css("cursor", "default");
    var table = $('#pkgLineTable');
    //$('#pkgLineTable tr').live('click', function(event) {                        
    $('#pkgLineTable tr').mousedown(function (event) {
        var thisitem = $(this);
        if (thisitem.index() != index) {
            var tableRow = thisitem.closest("tr").prevAll("tr").length + 1;
            var tempimg;
            var Rows;
            var selectimgs;
            var selectrows;
            Rows = $('#pkgLineTable tr');
            Rows.removeClass('row_mouseover');
            if (event.target.nodeName.toLowerCase() !== 'img' && event.target.nodeName.toLowerCase() !== 'a') {
                if (event.shiftKey) {
                    bolclickshift = true;
                    Rows.removeClass('row_selected');
                    selectimgs = $("img:.select");
                    selectimgs.attr("src", "img/checkbox_empty.png");
                    selectimgs.removeClass('select');
                    var start = Math.min(tableRow, lastSelected);
                    var end = Math.max(tableRow, lastSelected);
                    //table.find('tr:gt('+(start-1)+'):lt('+(end)+')').children(0).addClass('row_selected');
                    for (i = start - 1 ; i < end; i++) {
                        var temprow = table.find('tr:eq(' + i + ')');
                        //temprow.removeClass('listTabletr');
                        temprow.addClass('row_selected');
                        tempimg = temprow.children("td").eq(0).children('.chk');
                        tempimg.addClass('select');
                        tempimg.attr("src", "img/checkbox.png");
                    }

                }
                else if (event.ctrlKey) {
                    bolclickshift = true;
                    thisitem.bind("contextmenu", function (e) {
                        return false;
                    });
                    if (thisitem.hasClass('row_selected')) {
                        thisitem.removeClass('row_selected');
                        //thisitem.addClass('listTabletr');
                        tempimg = thisitem.children("td").eq(0).children('.chk');
                        tempimg.removeClass('select');
                        tempimg.attr("src", "img/checkbox_empty.png");
                    }
                    else {
                        //thisitem.removeClass('listTabletr');
                        thisitem.addClass('row_selected');
                        tempimg = thisitem.children("td").eq(0).children('.chk');
                        tempimg.addClass('select');
                        tempimg.attr("src", "img/checkbox.png");
                    }
                }
                else {
                    lastSelected = thisitem.closest("tr").prevAll("tr").length + 1;
                    selectrows = $('.row_selected');
                    if (!bolclickshift && selectrows.length <= 1) {
                        if (thisitem.hasClass('row_selected')) {
                            thisitem.removeClass('row_selected');
                            //thisitem.addClass('listTabletr');
                            tempimg = thisitem.children("td").eq(0).children('.chk');
                            tempimg.removeClass('select');
                            tempimg.attr("src", "img/checkbox_empty.png");
                        }
                        else {
                            $(".row_selected").each(function () {
                                tempimg = $(this).children("td").eq(0).children('.chk');
                                if (tempimg.attr("src") == "img/checkbox.png")
                                    tempimg.attr("src", "img/checkbox_empty.png");
                            });
                            Rows.removeClass('row_selected');
                            //Rows.addClass('listTabletr');
                            if (typeof lastSelectedRow === "undefined")
                                lastSelectedRow = thisitem;
                            tempimg = lastSelectedRow.children("td").eq(0).children('.chk');
                            tempimg.attr("src", "img/checkbox_empty.png");
                            //thisitem.removeClass('listTabletr');
                            thisitem.addClass('row_selected');
                            tempimg = thisitem.children("td").eq(0).children('.chk');
                            tempimg.addClass('select');
                            tempimg.attr("src", "img/checkbox.png");
                        }
                    }
                    else {
                        selectimgs = $("img:.select");
                        selectimgs.attr("src", "img/checkbox_empty.png");
                        Rows.removeClass('row_selected');
                        //Rows.addClass('listTabletr');
                        selectimgs.removeClass('select');
                        //thisitem.removeClass('listTabletr');
                        thisitem.addClass('row_selected');
                        tempimg = thisitem.children("td").eq(0).children('.chk');
                        tempimg.addClass('select');
                        tempimg.attr("src", "img/checkbox.png");
                        bolclickshift = false;
                    }
                    lastSelectedRow = thisitem;
                }
                EnableDisableZIPButton();
            }
        }
    });
}

function EnableDisableZIPButton() {    
    if ($('.row_selected').length > 0) {
        $('#imgZipDownloadPc').css("cursor", "pointer");
        $('#imgZipDownloadPc').attr("src", "img/" + language_name +"/ZipDownload-Enable.png");
    }
    else{
        $('#imgZipDownloadPc').css("cursor", "default");
        $('#imgZipDownloadPc').attr("src", "img/" + language_name + "/ZipDownload-Disable.png");
    }
}

function RebindMobileClick()
{
    ChkBoxImgClick();
    DownloadClick();
    HyperFileClick();
    MobilePlayHyperFileClick();
}

function ChkBoxImgClick() {
    $('.chk').click(function () {
        var parent;
        var parent1;
        if ($(this).attr("src") == "img/checkbox_empty.png") {
            parent = $(this).parent().get(0)
            parent1 = $(parent).parent().get(0);
            $(parent1).addClass('row_selected');
            $(this).addClass('select');
            $(this).attr("src", "img/checkbox.png");
        }
        else {
            parent = $(this).parent().get(0);
            parent1 = $(parent).parent().get(0);
            $(parent1).removeClass('row_selected');
            $(this).removeClass('select');
            $(this).attr("src", "img/checkbox_empty.png");
        }
        EnableDisableZIPButton();
    });
}

function ExitClickFunction() {
    $('.exititem').click(function () {
        window.location.assign('content.php?path=' + path + "&session=" + session);
    });
}

function SelectClickFunction() {
    $('.selectitem').click(function () {        
        if (select_all) {
            $("img:.chk").addClass('select');
            $("img:.chk").attr("src", "img/checkbox.png");
        }
        else {
            $("img:.chk").removeClass('select');
            $("img:.chk").attr("src", "img/checkbox_empty.png");
        }
        if (select_all) {
            bolclickshift = true;
            //$('.listTable tr').removeClass('listTabletr');
            $('.listTable tr').addClass('row_selected');
            if(NowActionForZipDownload == 0)
                $('.listTable tr:eq(0)').removeClass('row_selected');
            //$('.listTable tr:eq(0)').addClass('listTabletr');
            if (!$(this).hasClass('imgselectall'))
                $('.selectitem').text(lang.cancelall);
            else
                $(this).attr("src", "img/checkbox.png");
        }
        else {
            bolclickshift = false;
            $('.listTable tr').removeClass('row_selected');
            //$('.listTable tr').addClass('listTabletr');  
            if (!$(this).hasClass('imgselectall'))
                $('.selectitem').text(lang.selectall);
            else
                $(this).attr("src", "img/checkbox_empty.png");
        }
        select_all = !select_all;
        EnableDisableZIPButton();
    });
}

function SetSelectAllChkFalse(){  
    $(".selectitem").attr("src", "img/checkbox_empty.png");
    select_all = true;
}

function ZIPDownloadClick()
{
    $('#imgZipDownloadPc').click(function ()
    {        
        var index = 1;
        var i = 0;
        var t1= new Array();        
        //NowActionForZipDownload = 0;        
        $('#FormZipFile').empty();            
        if ($(".row_selected").length > 0)
        {
            $(".row_selected").each(function () {            
            var moveimg = $(this).children("td").eq(0).children('.chk');
            if (moveimg.attr("src") == "img/checkbox.png") {                   
                //MovePostData.PathName.push(moveimg.attr("id"));                
                t1[i]=document.createElement('input');
                t1[i].type='hidden';
                t1[i].name='idObjs['+index +']';
                t1[i].value = moveimg.attr("id");
                document.FormZipFile.appendChild(t1[i]);               
                i++;
                index++;
            }
            });                                                    
            t1[i]=document.createElement('input');
            t1[i].type='hidden';
            t1[i].name='sid';
            t1[i].value = sid;
            document.FormZipFile.appendChild(t1[i]);  
            i++;     
            t1[i]=document.createElement('input');
            t1[i].type='hidden';
            t1[i].name='id';
            t1[i].value = idroot;
            document.FormZipFile.appendChild(t1[i]);  
            i++; 
            t1[i]=document.createElement('input');
            t1[i].type='hidden';
            t1[i].name='key';
            t1[i].value = key;
            document.FormZipFile.appendChild(t1[i]);  
            i++; 
            t1[i]=document.createElement('input');
            t1[i].type='hidden';
            t1[i].name='cid';
            t1[i].value = cid;           
            document.FormZipFile.appendChild(t1[i]); 
            i++;                                                                                          
            document.FormZipFile.submit();
        }      
    });
}

function CallGetAjax(url, datatype) {
    var request = $.ajax({
        type: "Get",
        url: url,
        dataType: datatype
    });
    return request;
}

function CallAjax_NoAsync(url, data, datatype) {
    var request = $.ajax({
        type: "POST",
        async: false,
        url: url,
        data: data,
        dataType: datatype
    });
    return request;
}

function CallAjax(url, data, datatype) {
    var request = $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType: datatype
    });
    return request;
}