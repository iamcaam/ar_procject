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
var ChangeAddressAction = true;
var renameobj;
var mobile;
var detect_vlc = false;
var ShareFors;
var ShareForClass = function(idR,idO,idU,sharecase,securitycode){
    this.idR=idR;
    this.idO=idO;
    this.idU=idU;
    this.sharecase = sharecase;
    this.securitycode = securitycode;
}
var shareforinfo;
var uploadact;
$(document).ready(function ()
{
    IntoRecycle = 0;
    uploadact = 1;
    NowActionForZipDownload = '0';
    DeleteClickFunction();
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
        MediaClick();
//        MoveClick();
//        ShareClickFunction();
        PCLayout();
//        ShareLinkClickFunction();        
        AddFolderUploadLogoutClick();
//        ManageClickFunction();        
//        CreateFolderClick_PC();
//        RenameClick_PC();
//        ClearClick_PC();
        ZIPDownloadClick();
        //RecycleFolderClick();
//        SortOut_Initial();
//        Information_Initial();   
//        GetAccountinfo();                          
    }
    // 手機版
    else
    {
        mobile = true;
        addressChangEvent();
        MoveMobileClick();
        UploadClick_Mobile();
        ShareMobileClickFunction();
        ShareLinkMobileClickFunction();
        ChkBoxImgClick();
        ManageMobileClickFunction();
        CreateFolderClick_Mobile();
        RenameClick_Mobile();
        logout();
    }

    $(".renamepc").css("cursor", "default");
    $("#imgClear").css("display", "none");
    select_all = true;   
    TxtBxSearchEnter();
    Event_ReLayoutLeftItem();
});

function GetAccountinfo()
{     
    var postData = {                        
            sid:sid,            
            act:"getaccountinfo"         
    };               
    var request = CallAjax("FileCmdProcessPost.php", postData, "json");    
    request.done(function (msg, statustext, jqxhr) {           
        $.each( msg, function( index, element ) 
        {                        
            switch(index)
            {                
                case 'name':
                    $(".Lbl-Info-Account").text(lang.Account + element);
                    break;
                case 'size':
                    $(".Lbl-Info-TotalSpace").text(lang.TotalSpace + element);
                    break;
                case 'freesize':
                    $(".Lbl-Info-FreeSpace").text(lang.FreeSpace + element);
                    break;
            }
        });        
    });
    request.fail(function (jqxhr, textStatus) {                 
    });
}

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
            act:"getpath",
            id:objid
        };                                                   
        if(detect_vlc){
            switch (mediatype)
            {
                case "1":
                case "2":
                case "7":
                    opennewwindow("http://" + window.location.host + "/VLCPlay.php?sid=" + sid + "&cid=" + objid); 
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
                    opennewwindow("http://" + window.location.host + "/media.jplayer.php?sid=" + sid + "&cid=" + objid);
                    break;
                case "2":
                    opennewwindow("http://" + window.location.host + "/audio.jplayer.php?sid=" + sid + "&cid=" + objid);
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
            act:"getpath",
            id:objid
        };                                                   
            
        switch (mediatype)
        {
            case "1":
                opennewwindow("http://" + window.location.host + "/media.php?sid=" + sid + "&cid=" + objid);
                break;
            case "2":
                opennewwindow("http://" + window.location.host + "/audio.php?sid=" + sid + "&cid=" + objid);
                break;               
            case "3":
                GetPathAndRedirect(true,postData)
                break;
        }   
        
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
            act:"getpath",
            id:objid
        };                                                          
        switch (mediatype)
        {
            case "1":
            case "2":
            case "7":
                if(detect_vlc)
                    opennewwindow("http://" + window.location.host + "/VLCPlay.php?sid=" + sid + "&cid=" + objid); 
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
    var request = CallAjax_NoAsync("FileCmdProcessPost.php", postData, "json");    
        request.done(function (msg, statustext, jqxhr) {   
            if(msg == "-101" || msg == "-100"){
                bolcheck = false;
                logout_funct();
                return;
            }                               
            if(msg == -4){
                bolcheck = false;
                alert('Not Found');
                return;
            }                
            var redirectpath = msg;            
            bolcheck = false;
            $("body").css("cursor", "default");           
            modalWindow.closenoreload();           
            host = window.location.host;
            if(ssl)
                var head = 'https://';
            else
                head = 'http://';
//            $('#atest').attr('href' ,head + host + '/' + path);   
//            document.getElementById('atest').click(); 
//            $("#atest").trigger("click");
//            var isSafari = Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0;
//            if(isSafari){
//                var event = document.createEvent("HTMLEvents");
//                event.initEvent("click", true, true);
//                document.getElementById('atest').dispatchEvent(event);
//            }
//            else{
//                document.getElementById('atest').click();
//            }
             window.open(
                    head + host + '/' + redirectpath,
                    '_blank' 
                    );
        });
        request.fail(function (jqxhr, textStatus) {         
            //alert(jqxhr.responseText);
            bolcheck = false;
            $("body").css("cursor", "default");  
            alert("Fail No Response.");
            $("#pkgLineTable").empty();
            modalWindow.closenoreload();
        });
}

function addressChangEvent()
{    
    $.address.bind('change', function(event) {              
    if(ChangeAddressAction)
    {
        var act = event.parameters.act;                    
            if(typeof  act == 'undefined' ){  
                $('.Text-Search').val('');
//                RootGetFolderList(1);   
                //$.address.value('/');
                uploadact = 1;
                BtnBackRootClick();
            }                                                  
            else{          
                uploadact = 1;
                switch(act)
                {
//                    case '-1':
//                        $('.Text-Search').val('');
//                        var cid = event.parameters.cid;  
//                        if(typeof  cid == 'undefined' ){  
//                            //cid = 1;
//                            //RootGetFolderList(1);   
//                            //$.address.value('/');                            
//                            BtnBackRootClick();
//                        } 
//                        else
//                        {                                           
//                            RootGetFolderList(cid,act);                    
//                        }
//                        break;
//                    case '1':
//                        $('.Text-Search').val('');
//                        var cid = event.parameters.cid;  
//                        if(typeof  cid == 'undefined' ){                                                          
//                            BtnBackRootClick();
//                        } 
//                        else
//                        {                            
//                            RootGetFolderList(cid,act);                    
//                        }
//                        break;
//                    case '2':
//                        var pattern = event.parameters.pat;
//                        if(typeof  pattern == 'undefined' ){                                 
//                            BtnBackRootClick();
//                        } 
//                        else{
//                            $('.Text-Search').val(decodeURI(pattern));                           
//                            Find(pattern);  
//                        }
//                        break;
//                    case '3':
//                        $('.Text-Search').val('');
//                        var type = event.parameters.tp;                        
//                        if(typeof  type == 'undefined' ){                                
//                            BtnBackRootClick();
//                        } 
//                        else{                                                        
//                            GetByFileType(type); 
//                        }
//                        break;
//                    case '4':
//                        GetShaerFor();
//                        break;
                    case '5':
                        var idR = event.parameters.idR;     
                        HeadNoShow();
                        if(typeof  idR == 'undefined' ){                                
                            BtnBackRootClick();
                            return;
                        } 
                        var idO = event.parameters.idO;                        
                        if(typeof idO == 'undefined' ){                                
                            BtnBackRootClick();
                            return;
                        }
                        var idU = event.parameters.idU;                        
                        if(typeof idU == 'undefined' ){                                
                            BtnBackRootClick();
                            return;
                        } 
                        var code = event.parameters.code;                        
                        if(typeof idU == 'undefined' ){                                
                            BtnBackRootClick();
                            return;
                        } 
                        var sharecase = event.parameters.sharecase;                        
                        if(typeof sharecase == 'undefined' ){                                
                            BtnBackRootClick();
                            return;
                        }
                        shareforinfo = new ShareForClass(idR,idO,idU,sharecase,code);
                        GetShareForContent(idR,idO,idU,sharecase,code);
                        break;
                    default:
                        HeadNoShow();
                        $("#path").empty();
                        $("#pkgLineTable").empty();
                        break;
                }
            } 
        }
    });
}

function HeadNoShow()
{
    $("#upload").css("display", "none");
    $("#move").css("display", "none");
    $("#imgZipDownloadPc").css("display", "none");
    $("#addfolder").css("display", "none");
    $("#imgshare").css("display", "none");
    $("#imgmanage").css("display", "none");
    $("#imgrename").css("display", "none");
    $("#imgClear").css("display", "none");
    $("#delete").css("display", "none");
}

function HeadShareShow(casecode)
{
    $("#move").css("display", "none");
    $("#addfolder").css("display", "none");
    $("#imgshare").css("display", "none");
    $("#imgmanage").css("display", "none");
    $("#imgrename").css("display", "none");
    $("#imgClear").css("display", "none");
    $("#delete").css("display", "none");
    switch(casecode)
    {
        case "29":
            $("#upload").css("display", "inline");
            $("#imgZipDownloadPc").css("display", "inline");
            break;
        case "13":
            $("#upload").css("display", "inline");
            $("#imgZipDownloadPc").css("display", "none");
            break;
        case "21":
            $("#upload").css("display", "none");
            $("#imgZipDownloadPc").css("display", "inline");
            break;
        default:
            $("#upload").css("display", "none");
            $("#imgZipDownloadPc").css("display", "none");
            break;
    }    
}

function RecycleHeadShow()
{
    $("#upload").css("display", "none");
    $("#move").css("display", "inline");
    $("#imgZipDownloadPc").css("display", "none");
    $("#addfolder").css("display", "none");
    $("#imgshare").css("display", "none");
    $("#imgmanage").css("display", "none");
    $("#imgrename").css("display", "none");
    $("#imgClear").css("display", "inline");
    $("#delete").css("display", "inline");
}

function NoRecycleHeadShow()
{
    $("#deleteitem").css("display", "inline");
    $("#move").css("display", "inline");
    $("#upload").css("display", "inline");
    $("#imgZipDownloadPc").css("display", "inline");
    $("#addfolder").css("display", "inline");
    $("#imgshare").css("display", "inline");
    $("#imgmanage").css("display", "inline");
    $("#imgrename").css("display", "inline");
    $("#imgClear").css("display", "none");
    $("#delete").css("display", "inline");
}

function RootGetFolderList(fid,act)
{
    NowActionForZipDownload = '0';
    if(!mobile) {        
        if(act == "1")
            NoRecycleHeadShow();
        else
            RecycleHeadShow(); 
        SetSelectAllChkFalse();
        GetFolderList(fid);    
    }
    else
        GetMobileFolderList(fid);
}

function GetShareForContent(idR,idO,idU,sharecase,code)
{
    var jsonrequest = '{"accesskey":"' + sid + '","idR":' + idR + ',"idO":' + idO + ',"idU":' + idU + ',"sharecase":' + sharecase + ',"code":"' + code +  '"}';
    var request = CallAjax("ShareCmdProcessPost.php?act=ListSANShareContent", jsonrequest , "json");
    $("body").css("cursor", "wait");
    openModalWaitNoReload('',0,0);               
    request.done(function (msg, statustext, jqxhr) {     
//        alert(jqxhr.responseText);
        $.each( msg, function( index, element ) 
        {                           
            switch(index)
            {
                case 'result':
                    //    -1 ~ -99 parameter error
                    //    -101,-102 access key error 
                    //    -200 ~ -299 db error
                    //    -300 ~ -399 Not Find Share
                    if(element == 0){                        
                    }
//                    else if(element < 0 && element > -99){
//                        logout_funct();
//                    }   
//                    else if(element <= -100 && element > -200){
//                        logout_funct();
//                    }        
//                    else if(element <= -200 && element > -300){
//                        logout_funct();
//                    }
                    else {
                       alert('Not Find Share(Result:' + element + ')');
                       $("#path").empty();
                       $("#pkgLineTable").empty();
                    }                   
                    break;      
                case 'isFile' :
                    if(element == '1')
                        HeadNoShow();
                    else{
                        HeadShareShow(shareforinfo.sharecase);                        
                    }                       
                    break;
                case 'HeadHtml':
                    $("#path").empty();
                    $("#path").append(element);
                    break;
                case 'TableBodyHtml':
                    uploadact = 2;                    
                    $("#pkgLineTable").empty();
                    $("#pkgLineTable").append(element);     
                    RebindShareTableClickEvent();
                    break;
            }
        });
        $("body").css("cursor", "default");  
        modalWindow.closenoreload();
    });
    request.fail(function (jqxhr, textStatus) { 
//        alert(jqxhr.responseText);        
        alert('Fail');    
        $("body").css("cursor", "default");  
        modalWindow.closenoreload();
        $("#path").empty();
        $("#pkgLineTable").empty();        
    });
}

function RebindShareTableClickEvent()
{   
    if(detect_vlc){           
        $('.AVLC').css("display", "inline");           
    }
    DownloadClick();
    PlayShareHyperFileClick();
    ShareHyperClick();
    TableMouseDown(0);
    TableMove(0);
    TableMoveOut();
    TableClick(0);       
}

function DownloadClick()
{
    $('.DownloadShare').click(function () {                
        var objid = $(this).attr('id');                                                                      
        opennewwindow("download.ShareFor.php?sid=" + sid + "&idR=" + shareforinfo.idR + "&idO=" + objid + "&idU=" + shareforinfo.idU + "&sharecase=" + shareforinfo.sharecase + "&code=" + shareforinfo.securitycode); 
        return false;
    });        
}

function PlayShareHyperFileClick()
{
    $('.SharePlayHyper').click(function () {        
        var tmphref = $(this).attr('href');
        var mediatype = tmphref.substring(3);
        var objid = $(this).attr('id');         
        var jsonrequest = '{"accesskey":"' + sid + '","idR":' + shareforinfo.idR + ',"idO":' + objid + ',"idU":' + shareforinfo.idU + ',"sharecase":' + shareforinfo.sharecase + ',"code":"' + shareforinfo.securitycode +  '"}';                                                      
        if(detect_vlc){
            switch (mediatype)
            {
                case "1":
                case "2":
                case "7":
                    opennewwindow("http://" + window.location.host + "/VLCPlay_ShareFor.php?sid=" + sid + "&idR=" + shareforinfo.idR + "&idO=" + objid + "&idU=" + shareforinfo.idU + "&sharecase=" + shareforinfo.sharecase + "&code=" + shareforinfo.securitycode); 
                    break;               
                case "3":
                    GetSharePathAndRedirect(true,jsonrequest)
                    break;
            }
        }
        else{            
            switch (mediatype)
            {
                case "1":
                    opennewwindow("http://" + window.location.host + "/media.jplayer.ShareFor.php?sid=" + sid + "&idR=" + shareforinfo.idR + "&idO=" + objid + "&idU=" + shareforinfo.idU + "&sharecase=" + shareforinfo.sharecase + "&code=" + shareforinfo.securitycode); 
                    break;
                case "2":
                    opennewwindow("http://" + window.location.host + "/audio.jplayer.ShareFor.php?sid=" + sid + "&idR=" + shareforinfo.idR + "&idO=" + objid + "&idU=" + shareforinfo.idU + "&sharecase=" + shareforinfo.sharecase + "&code=" + shareforinfo.securitycode); 
                    break;               
                case "3":
                    GetSharePathAndRedirect(true,jsonrequest)
                    break;
            }   
        }
        return false;
    });        
}

function ShareHyperClick()
{
    $('.sharehyperfile').click(function () {   
        var tmphref = $(this).attr('href');
        var mediatype = tmphref.substring(3);
        var objid = $(this).attr('id');        
        var jsonrequest = '{"accesskey":"' + sid + '","idR":' + shareforinfo.idR + ',"idO":' + objid + ',"idU":' + shareforinfo.idU + ',"sharecase":' + shareforinfo.sharecase + ',"code":"' + shareforinfo.securitycode +  '"}';           
        switch (mediatype)
        {
            case "1":
            case "2":
            case "7":
                if(detect_vlc)
                    opennewwindow("http://" + window.location.host + "/VLCPlay_ShareFor.php?sid=" + sid + "&idR=" + shareforinfo.idR + "&idO=" + objid + "&idU=" + shareforinfo.idU + "&sharecase=" + shareforinfo.sharecase + "&code=" + shareforinfo.securitycode); 
                else
                    GetSharePathAndRedirect(false,jsonrequest)
                break;
            case "0":
            case "3":
            case "4":
            case "5":    
            case "6":
                GetSharePathAndRedirect(true,jsonrequest)
                break;
        }        
        return false;
    });
}

function GetSharePathAndRedirect(ssl,postData)
{    
    $("body").css("cursor", "wait");
    openModalWaitNoReload('',0,0);           
    var request = CallAjax_NoAsync("ShareCmdProcessPost.php?act=GetSANSharePath", postData, "json");    
        request.done(function (msg, statustext, jqxhr) {   
        $.each( msg, function( index, element ) 
        {                               
            switch(index)
            {
                case 'result':
                    //    -1 ~ -99 parameter error
                    //    -101,-102 access key error 
                    //    -200 ~ -299 db error
                    //    -300 ~ -399 Not Find Share
                    if(element == 0){                        
                    }
                    else {
                       alert('Not Find Share(Result:' + element + ')');
                    }   
                    break;
                case 'playpath':
                    var redirectpath = element;                                
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
//            alert(jqxhr.responseText);           
            $("body").css("cursor", "default");  
            alert("Fail No Response.");            
            modalWindow.closenoreload();
            BtnBackRootClick();
        });
}

function GetShaerFor()
{
    var jsonrequest = '{"accesskey":"' + sid + '"}';
    var request = CallAjax("ShareCmdProcessPost.php?act=ListShareFor", jsonrequest , "json");    
    request.done(function (msg, statustext, jqxhr) {          
        $.each( msg, function( index, element ) 
        {                           
            switch(index)
            {
                case 'result':
                    //    -1 ~ -99 parameter error
                    //    -101,-102 access key error 
                    //    -200 ~ -299 db error
                    if(element == 0){
                        
                    }
                    else if(element < 0 && element > -99){
                        logout_funct();
                    }   
                    else if(element <= -100 && element > -200){
                        logout_funct();
                    }        
                    else if(element <= -200 && element > -300){
                        logout_funct();
                    }
                    
                    break;               
                case 'Shares':
                    ShareFors = [];
                    $("#pkgLineTable").empty();
                    HeadNoShow();
                    var appendHtml = '';
                    var san = 0;
                    appendHtml += CreateShareHeadHtml();
                    $.each(element,function(index, element)
                    {
                        ShareFors[index] = Array(6);
                        ShareFors[index]['nameUser'] = element.nameAccount;
                        ShareFors[index]['nameObject'] = element.nameObject;
                        ShareFors[index]['isFile'] = element.isFile;
                        ShareFors[index]['idShareCase'] = element.idShareCase;
                        ShareFors[index]['idUser'] = element.idUser;
                        ShareFors[index]['idObject'] = element.idObject;
                        ShareFors[index]['codeSecurity'] = element.codeSecurity;
                        ShareFors[index]['san'] = element.san;
                        san = ShareFors[index]['san'];
                        ShareFors[index]['stgid'] = element.stgid;
                        appendHtml += CreateShareHtml(element.nameAccount,element.nameObject,element.idShareCase,element.isFile,index)                                                
//                        alert(element.nameUser + " " + element.nameObject + " " + element.isFile + " " + element.idShareCase + " " + element.idObject + " " + element.codeSecurity);
                    });
                    
                    $("#path").empty();
                    $("#path").append($(".Lbl_MyShare").text());
                    $("#pkgLineTable").append(appendHtml);
                    if(san == 1)
                        ShareForClick();
                    break;
            }
        });
    })
    request.fail(function (jqxhr, textStatus) {     
//        alert(jqxhr.responseText);
        alert('Fail');    
        BtnBackRootClick();
    });
}

function CreateShareHeadHtml()
{
    var Htmlstr = '<tr class="listTabletr" style= "text-align:left">';        
        Htmlstr += '<th width="30%" style="word-break:break-all;text-align:left">' + lang.ShareUser +'</td>';
        Htmlstr += '<th width="30%" style="text-align:left">' + lang.ShareType + '</td>';
        Htmlstr += '<th width="10%" style="text-align:left">' + lang.Type + '</td>';
        Htmlstr += '<th width="40%" style= "text-align:left">' + lang.Name + '</td>';        
        Htmlstr += '</tr>';
    return Htmlstr;
}

function CreateShareHtml(user,name,sharecase,isFile,id)
{    
    var strsharecase = '';
    switch(sharecase)
    {
        case '13':
            strsharecase = lang.HomewordMode;
            break;
        case '21':
            strsharecase = lang.DownloadMode;
            break;
        case '29':
            strsharecase = lang.UploadDownloadMode;
            break;
    }
    var Htmlstr = '<tr class="listTabletr" style= "text-align:left">';        
        Htmlstr += '<td width="30%" style="word-break:break-all;text-align:left">' + user +'</td>';       
        Htmlstr += '<td width="30%" style="text-align:left">' + strsharecase + '</td>';
        if(isFile == '1')
            Htmlstr += '<td width="10%" style="text-align:left">' + lang.File + '</td>';
        else
            Htmlstr += '<td width="10%" style="text-align:left">' + lang.Folder + '</td>';
        if( ShareFors[id]['idObject'] == 0)
            Htmlstr += '<td id="' + id +'" class="NameShare" width="40%" style= "text-align:left"><a id ="' + id + '" href="/' + id + '" rel="address:/' + id + '/?act=5&idR=' + ShareFors[id]['idObject'] + '&idO=' + ShareFors[id]['idObject'] + '&idU=' + ShareFors[id]['idUser'] + '&code=' + ShareFors[id]['codeSecurity'] + '&sharecase=' + ShareFors[id]['idShareCase'] + '">' + name +'</a></td>';        
        else
            Htmlstr += '<td id="' + id +'" class="NameShare" width="40%" style= "text-align:left"><label id ="' + id + '" class="sansharelink">' + name +'</label></td>';        
        Htmlstr += '</tr>';
    return Htmlstr;
}

function ShareForClick()
{
    $('.NameShare').click(function () {     
        var bol_check = false;
        var id = $(this).attr('id');        
        var postData = '{ "sid":"'+ sid + '","stgid":' + ShareFors[id]['stgid'] +'}';        
        $("body").css("cursor", "wait");
        openModalWaitNoReload('',0,0);            
        var request = CallAjax_NoAsync("ShareCmdProcessPost.php?act=CheckShareLocation", postData, "json");    
        request.done(function (msg, statustext, jqxhr) {               
            if(msg['result'] != 0)
            {    
                switch(msg['result'])
                {
                    case -100 :
                    case -101 :
                        logout_funct();
                        break;
                    default :
                        alert('Something Error(Result:'+msg['result'] + ')');
                }
            }
            else
            {                
                if(msg['local'] == 1)
                {
                    bol_check = CheckShare(id);
                }
            }           
            $("body").css("cursor", "default");           
            modalWindow.closenoreload();            
        });
        request.fail(function (jqxhr, textStatus) {         
            $("body").css("cursor", "default");  
            alert("Fail Check");
            $(".listTable").empty();
            modalWindow.closenoreload();
        });
        if(bol_check)
        {
            alert('redirect');
            window.location.href = 'content_san.php?sid=' + sid + '#/' + id + '/?act=5&idR=' + ShareFors[id]['idObject'] + '&idO=' + ShareFors[id]['idObject'] + '&idU=' + ShareFors[id]['idUser'] + '&code=' + ShareFors[id]['codeSecurity'] + '&sharecase=' + ShareFors[id]['idShareCase'];                        
        }
    });
}

function CheckShare(index)
{                    
    var bol_check = false;
    var postData = '{ "sid":"'+ sid + '","stgid":' + ShareFors[index]['stgid'] + ',"idR":' + ShareFors[index]['idObject'] + ',"idO":' + ShareFors[index]['idObject'] + ',"idU":' + ShareFors[index]['idUser'] + ',"sharecase":' + ShareFors[index]['idShareCase']  +'}';            
    $("body").css("cursor", "wait");
    openModalWaitNoReload('',0,0);            
    var request = CallAjax_NoAsync("ShareCmdProcessPost.php?act=CheckSANShare", postData, "json");    
    request.done(function (msg, statustext, jqxhr) {               
        if(msg['result'] != 0)
        {    
            switch(msg['result'])
            {
                case -100 :
                case -101 :
                    logout_funct();
                    break;
                default :
                    alert('Something Error(Result:'+msg['result'] + ')');
            }
        }
        else
        {                            
            bol_check = true;            
        }           
        $("body").css("cursor", "default");           
        modalWindow.closenoreload();            
    });
    request.fail(function (jqxhr, textStatus) {         
        $("body").css("cursor", "default");          
        alert("Fail Check");
        $(".listTable").empty();
        modalWindow.closenoreload();
    });
    return bol_check;
}

function BtnListShareForClick()
{
    document.location.hash = '/?act=4';
}

function GetMobileFolderList(fid)
{
    var postData = {                        
            sid:sid,            
            act:"mobilelf",
            fid:fid           
    };
    $("body").css("cursor", "wait");
    openModalWaitNoReload('',0,0);    
    var totalspace = 0;
    var request = CallAjax("FileCmdProcessPost.php", postData, "json");    
    request.done(function (msg, statustext, jqxhr) {     
        if(msg == "-101" || msg == "-100"){
            logout_funct();
            return;
        }
        $.each( msg, function( index, element ) 
        {
            switch(index)
            {
                case 0:
                    $("#path").empty();
                    $("#path").append(element);
                    break;
                case 1:
                    $(".listTable").empty();
                    $(".listTable").append(element);
                    break;
                case 2:
                    $("#Lbl-Info-Account").html(lang.Account + "<br>" + element);
                    break;
                case 3:
                    totalspace = element;
                    break;
                case 4:
                    $("#Lbl-Info-Space").html(lang.SpaceStatus + "<br>" + element + '/' + totalspace);
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
            act:"lf",
            fid:fid           
    };    
    $("body").css("cursor", "wait");
    openModalWaitNoReload('',0,0);    
    
    
    var request = CallAjax("FileCmdProcessPost.php", postData, "json");    
    request.done(function (msg, statustext, jqxhr) {   
        if(msg == "-101" || msg == "-100"){
            logout_funct();
            return;
        }
        $.each( msg, function( index, element ) 
        {                        
            switch(index)
            {
                case 0:
                    $("#path").empty();
                    $("#path").append(element);
                    break;
                case 1:
                    $("#pkgLineTable").empty();
                    $("#pkgLineTable").append(element);
                    break;
                case 2:
                    $(".Lbl-Info-Account").text(lang.Account + element);
                    break;
                case 3:
                    $(".Lbl-Info-TotalSpace").text(lang.TotalSpace + element);
                    break;
                case 4:
                    $(".Lbl-Info-FreeSpace").text(lang.FreeSpace + element);
                    break;
            }
        });
        EnableDisableZIPButton();
        RebindTableClickEvent();          
        $("body").css("cursor", "default");           
        modalWindow.closenoreload();
        cid = fid;
    });
    request.fail(function (jqxhr, textStatus) {         
        $("body").css("cursor", "default");  
        alert("Fail Get Content");
        
        $("#pkgLineTable").empty();
        modalWindow.closenoreload();
    });
}

function GetByFileType(type)
{
    NowActionForZipDownload = type;
    var postData = {                        
            sid:sid,            
            act:"lftype",
            type:type           
    };    
    $("body").css("cursor", "wait");
    openModalWaitNoReload('',0,0);    
    $("#deleteitem").css("display", "inline");
    $("#move").css("display", "none");
    $("#upload").css("display", "none");
    $("#imgZipDownloadPc").css("display", "inline");
    $("#addfolder").css("display", "none");
    $("#imgshare").css("display", "none");
    $("#imgmanage").css("display", "none");
    $("#imgrename").css("display", "inline");
    $("#imgClear").css("display", "none");
    $("#delete").css("display", "inline");
    $("#path").empty();
    SetSelectAllChkFalse();
    switch(type)
    {
        case '1':
            $("#path").append('<a>' + lang.SortOutAUDIO + '</a>');
            now_path = '/' + lang.SortOutAUDIO;
            break;
        case '2':
                $("#path").append('<a>' + lang.SortOutVIDEO + '</a>');
            now_path = '/' + lang.SortOutVIDEO;
            break;
        case '3':
            $("#path").append('<a>' + lang.SortOutPHOTO + '</a>');
            now_path = '/' + lang.SortOutPHOTO;
            break;
        case '4':
            $("#path").append('<a>' + lang.SortOutDOC + '</a>');
            now_path = '/' + lang.SortOutDOC;
            break;

    }    
    
    var request = CallAjax("FileCmdProcessPost.php", postData, "json");    
    request.done(function (msg, statustext, jqxhr) {  
        if(msg == "-101" || msg == "-100"){
            logout_funct();
            return;
        }
        $.each( msg, function( index, element ) 
        {            
            switch(index)
            {               
                case 0:
                    $("#pkgLineTable").empty();
                    $("#pkgLineTable").append(element);
                    break;
            }
        });               
        $("body").css("cursor", "default");           
        EnableDisableZIPButton();
        RebindTableClickEvent();
        modalWindow.closenoreload();        
    });
    request.fail(function (jqxhr, textStatus) {         
        $("body").css("cursor", "default");  
        alert("Fail Get Content");
        $("#pkgLineTable").empty();
        EnableDisableZIPButton();
        modalWindow.closenoreload();
    });
}

function Find(input_pattern)
{
    NowActionForZipDownload = '-1';
    var postData = {                        
            sid:sid,            
            Pattern:input_pattern,
            act:"find"
    };    
    $("body").css("cursor", "wait");
    openModalWaitNoReload('',0,0);
    var request = CallAjax("FileCmdProcessPost.php", postData, "json");
    $("#path").empty();
    $("#path").append('<a>' + lang.SearchResult + '</a>');
    now_path = '/' + lang.SearchResult;

    // 按鈕顯示控制
    $("#deleteitem").css("display", "inline");
    $("#move").css("display", "none");
    $("#upload").css("display", "none");
    $("#imgZipDownloadPc").css("display", "inline");
    $("#addfolder").css("display", "none");
    $("#imgshare").css("display", "none");
    $("#imgmanage").css("display", "none");
    $("#imgrename").css("display", "inline");
    $("#imgClear").css("display", "none");
    $("#delete").css("display", "inline");
    SetSelectAllChkFalse();
    EnableDisableZIPButton();
    request.done(function (msg, statustext, jqxhr) {   
        if(msg == "-101" || msg == "-100"){
            logout_funct();
            return;
        }
        $.each( msg, function( index, element ) 
        {                
            switch(index)
            {               
                case 0:
                    $("#pkgLineTable").empty();
                    $("#pkgLineTable").append(element);
                    break;
            }
        });
        RebindTableClickEvent();          
        $("body").css("cursor", "default");           
        modalWindow.closenoreload();
    });
    request.fail(function (jqxhr, textStatus) {                    
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
    ShareLinkClickFunction();
    PlayHyperFileClick();
    HyperFileClick();
    TableMouseDown(0);
    TableMove(0);
    TableMoveOut();
    TableClick(0);       
}

function TxtBxSearchEnter()
{
    $(".Text-Search").keypress(function (event)
    {
        // 13 代表 Enter 鍵，keyCode, which 是怕有些瀏覽器不支援所以都判斷
        if (13 == (event.keyCode))
        {
            BtnSearchClick();
        }
        else if (13 == (event.which))
        {
            BtnSearchClick();
        }
    });
}

function Event_ReLayoutLeftItem()
{
    setTimeout("ReLayoutLeftItem()", 300);       // 2013.03.18 Foxy 
}

function SortOut_Initial()
{
    $(".Lbl-SortOut-Title").click(function ()
    {
        setTimeout("SortOutTreeHandle()", 300);       // 2013.03.18 Foxy 
    });
    $(".Img-SortOut-Triangle").click(function ()
    {
        setTimeout("SortOutTreeHandle()", 300);       // 2013.03.18 Foxy 
    });
}

function SortOutTreeHandle()
{
    // 若 true 然後觸發此 function 則 變成收折（隱藏），反之顯示
    if (true == IsSortOutTreeExpand)
    {
        $('.SortOutDetail').css("display", "none");
        $('.Div-SortOut').css("height", "50px");
        $(".Img-SortOut-Triangle").attr("src", "img/TreeTriangleCollapse.png");
        IsSortOutTreeExpand = false;
    }
    else
    {
        $('.SortOutDetail').css("display", "inline");
        $('.Div-SortOut').css("height", "200px");
        $(".Img-SortOut-Triangle").attr("src", "img/TreeTriangleExpanded.png");
        IsSortOutTreeExpand = true;
    }
    ReLayoutLeftItem();
}

function Information_Initial()
{
    $(".Lbl-Info-AccountTitle").click(function ()
    {
        setTimeout("InfoTreeHandle()", 300);       // 2013.03.18 Foxy 
    });
    $(".Img-Info-Triangle").click(function ()
    {
        setTimeout("InfoTreeHandle()", 300);       // 2013.03.18 Foxy 
    });
}

function InfoTreeHandle()
{
    // 若 true 然後觸發此 function 則 變成收折（隱藏），反之顯示
    if (true == IsInfoTreeExpand)
    {
        $(".InfoDetail").css("display", "none");
        $('.Div-Info').css("height", "50px");
        $(".Img-Info-Triangle").attr("src", "img/TreeTriangleCollapse.png");
        IsInfoTreeExpand = false;
    }
    else
    {
        $(".InfoDetail").css("display", "inline");
        $('.Div-Info').css("height", "200px");
        $(".Img-Info-Triangle").attr("src", "img/TreeTriangleExpanded.png");
        IsInfoTreeExpand = true;
    }
    ReLayoutLeftItem();
}

//For Recycle Hyper Link
function opennewwindow(location)
{
    window.open(
        location,
        '_blank' // <- This is what makes it open in a new window.
    );
}

function logout()
{   
    $('.btnlogout').click(function () {
        var answer = confirm(lang.asklogout)
            if (answer) {
                logout_funct();
            }                
                
    });           
}

function logout_funct()
{
    var xmlstr = "<acrored><AccessKey>" + sid +"</AccessKey></acrored>";       
    var request = CallAjax("auth/logout", xmlstr, "xml");
    request.done(function(msg,statustext,jqxhr) {         
//        alert(jqxhr.responseText);
        if(navigator.cookieEnabled){          
            $.cookie('ARLGIN', null);
            $.cookie('ARRM', null)                
        }  
        var url = XmlParseLogout(msg);
//        alert(url);
        window.location.href = url;
    });
    request.fail(function(jqXHR, textStatus) {  
//        alert(jqxhr.responseText);
        if(navigator.cookieEnabled){          
            $.cookie('ARLGIN', null);
            $.cookie('ARRM', null)                
        }                     
        window.location.href = 'index.php';
    });
}

function XmlParseLogout(xmldata){
    var url = 'index.php';
    var level = -99;
    var result = -99;    
    $(xmldata).find('status').each(function(){
        result = $(this).text();
    });
//    if(result == 0){        
        $(xmldata).find('url').each(function(){            
            url = $(this).text();
        })        
//    }    
    return url;
}

// 點選左邊的 Search 圖示或文字要.......      2013.02.18 Foxy
function BtnSearchClick()
{
    var searchval = $('.Text-Search').val();

    if (searchval.match(checksearchname_pattern))
    {
        alert(lang.charwarning);
        return;
    }

    if (searchval.length > 0)
    {                   
        //$.address.value('/?act=2&pat=' + encodeURIComponent(searchval));
        document.location.hash = '/?act=2&pat=' + encodeURIComponent(searchval);
    }       
}

// 點選左邊的 Recycle 圖示或文字要列出回收桶檔案      2013.02.08 Foxy
function BtnRecycleBinClick()
{
    //GetFolderList(-1);
    //$.address.value('/-1/?cid=-1&act=1');
    document.location.hash = '/-1/?cid=-1&act=-1';
}

// 點選左邊的 Back Root 圖示或文字要.......      2013.02.18 Foxy
function BtnBackRootClick()
{
    //window.location.assign('content_pc.php?path=/root/web/' + session + '/' + lunname + "&session=" + session);
    //GetFolderList(1);    
        window.location.href = 'https://' + orgip +'/content_pc.php?sid=' + sid + '#/1/?cid=1&act=1';    
}

// 點選左邊的 分類 內的文字其中的 [ 文件 ] 要.......      2013.02.18 Foxy
function BtnSortOutClick( SortOut_Kind )
{       
   document.location.hash = '/?act=3&tp=' + SortOut_Kind;       
}

function CreateFolderClick_Mobile()
{
    $('.btnAddFolder').click(function ()
    {
        openModalNoReload('createfolder.php?sid=' + sid + '&cid=' + cid, 400, 350);
        return false;
    });
}

function CreateFolderClick_PC()
{
    $('.btnAddFolder').click(function ()
    {
        openModalNoReload('createfolder.php?sid=' + sid + '&cid=' + cid , 800, 450);
        return false;
    });
}

function UploadClick_Mobile()
{
    $('.btnUpload').click(function ()
    {
        /* 2012.11.23 把上一頁按鈕失能. ( 回到 History 第一頁 ) */
//        if(Silverlight.isInstalled('5.0')){                    
//            window.location.assign('ARWADD_Upload.php?sid=' + sid + '&crt=' + sid +'&cnt=3&cid=' + cid + '&showdest=' + encodeURIComponent($('#path').text().replace(/'/g, "%27")));
//        }
//        else
        switch(uploadact)
        {
            case 1:
                openModal('upload.php?sid=' + sid + '&idU=-1&crt=' + crt +'&cid=' + cid, 400, 380) ;
                break;
            case 2:
                openModal('upload.php?sid=' + sid + "&idR=" + shareforinfo.idR + "&idU=" + shareforinfo.idU + "&sharecase=" + shareforinfo.sharecase + "&code=" + shareforinfo.securitycode + '&crt=' + crt +'&cid=' + shareforinfo.idO, 400 ,380) ;
                break;
        }    
    });
}

function AddFolderUploadLogoutClick() {
    $('.btnUpload').click(function ()
    {
        /* 2012.11.23 把上一頁按鈕失能. ( 回到 History 第一頁 ) */
        if(Silverlight.isInstalled('5.0')){    
            ChangeAddressAction = false;
            switch(uploadact)
            {
                case 1:
                    window.location.assign('ARWADD_Upload.php?sid=' + sid + '&idU=-1&crt=' + crt +'&cnt=8&cid=' + cid + '&showdest=' + encodeURIComponent($('#path').text().replace(/'/g, "%27")));
                    break;
                case 2:
                    window.location.assign('ARWADD_Upload.php?sid=' + sid + "&idR=" + shareforinfo.idR + "&idU=" + shareforinfo.idU + "&sharecase=" + shareforinfo.sharecase + "&code=" + shareforinfo.securitycode + '&crt=' + crt +'&cnt=8&cid=' + shareforinfo.idO + '&showdest=' + encodeURIComponent($('#path').text().replace(/'/g, "%27")));
                    break;
            }
        }
        else{      
            switch(uploadact)
            {
                case 1:
                    openModal('upload.php?sid=' + sid + '&idU=-1&crt=' + crt +'&cid=' + cid, 400, 380) ;
                    break;
                case 2:
                    openModal('upload.php?sid=' + sid + "&idR=" + shareforinfo.idR + "&idU=" + shareforinfo.idU + "&sharecase=" + shareforinfo.sharecase + "&code=" + shareforinfo.securitycode + '&crt=' + crt +'&cid=' + shareforinfo.idO, 400 ,380) ;
                    break;
            }
        }
    });
    logout();
}

function GotoPage_UploadSilverlight()
{
    switch(uploadact)
    {
        case 1:
            window.location.assign('ARWADD_Upload.php?sid=' + sid + '&idU=-1&crt=' + crt +'&cnt=8&cid=' + cid + '&showdest=' + encodeURIComponent($('#path').text().replace(/'/g, "%27")));
            break;
        case 2:
            window.location.assign('ARWADD_Upload.php?sid=' + sid + "&idR=" + shareforinfo.idR + "&idU=" + shareforinfo.idU + "&sharecase=" + shareforinfo.sharecase + "&code=" + shareforinfo.securitycode + '&crt=' + crt +'&cnt=8&cid=' + shareforinfo.idO + '&showdest=' + encodeURIComponent($('#path').text().replace(/'/g, "%27")));
            break;
    }
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

// 重新排版，僅對左邊
function ReLayoutLeftItem()
{
    /* 2013.02.21 Foxy */

    // 搜尋輸入框的 Left 位置 = 距離左牆寬8 + 圖形物件 + 間距3 + 文字物件 + 間距5 
    var tmpLeft = 8 + $('.Img-Search').width() + 3 + $('.Lbl-Search').width() + 5;

    // 搜尋輸入框的 Width 寬度 = 總寬度 - 距離左牆寬8 - 圖形物件 - 間距3 - 文字物件 - 間距5  - ENTER按鈕圖示 - ENTER右邊距離牆壁寬15 - 文字輸入框本身寬度計算有微幅錯誤
    var tmpWidth = $('.Div-Base').width() - 8 - $('.Img-Search').width() - 6 - $('.Lbl-Search').width() - 5 - $('.Img-SearchEnter').width() - 15 - 10;
    $('.Text-Search').css("left", tmpLeft);
    $('.Text-Search').css("width", tmpWidth);

    // Enter 按鈕 = 總寬度 - 本身寬度 - 間距
    tmpLeft = $('.Div-Base').width() - $('.Img-SearchEnter').width() - 5 - 10;
    $('.Img-SearchEnter ').css("left", tmpLeft);


    // --- 帳號資訊 User Information ---

    // 帳號位置
    // 搜尋輸入框的 Left 位置 = 前兩個物件寬度 + 距離左邊牆壁寬度 + 間距5
    tmpLeft = $('.Lbl-Info-AccessStatus').width() + 33 + 5;

    // 搜尋輸入框的 Width 位置 = 總寬度 - 前兩個物件寬度 - 距離左邊牆壁寬度與物件間距總和 
    tmpWidth = $('.Div-Base').width() - $('.Lbl-Info-AccessStatus').width() - 33
    $('.Lbl-Info-AccessStatusResult').css("left", tmpLeft);
    $('.Lbl-Info-AccessStatusResult').css("width", tmpWidth);
    
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

function MoveMobileClick()
{
    $('.movetomoblie').click(function ()
    {
        var index = 1;
        var i = 0;
        var t1= new Array();        
        document.FormPostMoveTo.reset();
        $(".row_selected").each(function () {            
            var moveimg = $(this).children("td").eq(0).children('.chk');
            if (moveimg.attr("src") == "img/checkbox.png") {
                t1[i]=document.createElement('input');
                t1[i].type='hidden';
                t1[i].name='FID['+index +']';
                if(moveimg.hasClass('hyperfile'))
                    t1[i].value = 'f' + moveimg.attr("id");
                else
                    t1[i].value = 'd' + moveimg.attr("id");
                //t1[i].value = moveimg.attr("id");
                document.FormPostMoveTo.appendChild(t1[i]);               
                i++;
                index++;
            }
        });
        if (i > 0)
        {            
            t1[i]=document.createElement('input');
            t1[i].type='hidden';
            t1[i].name='sid';
            t1[i].value = sid;
            document.FormPostMoveTo.appendChild(t1[i]);  
            i++;            
            t1[i]=document.createElement('input');
            t1[i].type='hidden';
            t1[i].name='cid';
            t1[i].value = cid;
            document.FormPostMoveTo.appendChild(t1[i]);  
            document.FormPostMoveTo.submit();
        }
        else 
        {
            alert(lang.askmove);
        }
    });
}

function MoveClick()
{
    $('.move_to').click(function ()
    {
//        MovePostData = {
//            FID: [],
//            cid: cid
//        };
        var index = 1;
        var i = 0;
//        var inputpostarraystr = "";
        var t1= new Array();        
        document.FormPostMoveTo.reset();
        $(".row_selected").each(function () {            
            var moveimg = $(this).children("td").eq(0).children('.chk');
            if (moveimg.attr("src") == "img/checkbox.png") {
//                MovePostData.FID.push(moveimg.attr("id"));
                t1[i]=document.createElement('input');
                t1[i].type='hidden';
                t1[i].name='FID['+index +']';
                if(moveimg.hasClass('hyperfile'))
                    t1[i].value = 'f' + moveimg.attr("id");
                else
                    t1[i].value = 'd' + moveimg.attr("id");
                document.FormPostMoveTo.appendChild(t1[i]);               
                i++;
                index++;
            }
        });
        if (i > 0)
        {            
            t1[i]=document.createElement('input');
            t1[i].type='hidden';
            t1[i].name='sid';
            t1[i].value = sid;
            document.FormPostMoveTo.appendChild(t1[i]);  
            i++;            
            t1[i]=document.createElement('input');
            t1[i].type='hidden';
            t1[i].name='cid';
            t1[i].value = cid;
            document.FormPostMoveTo.appendChild(t1[i]);  
//            i++;
//            t1[i]=document.createElement('input');
//            t1[i].type='hidden';
//            t1[i].name='IntoRecycle';
//            t1[i].value = IntoRecycle;
//            document.FormPostMoveTo.appendChild(t1[i]); 
//            i++;
//            t1[i]=document.createElement('input');
//            t1[i].type='hidden';
//            t1[i].name='LUN';
//            t1[i].value = lunname;
//            document.FormPostMoveTo.appendChild(t1[i]);  
//            i++;
//            t1[i]=document.createElement('input');
//            t1[i].type='hidden';
//            t1[i].name='cid';
//            t1[i].value = cid;
//            document.FormPostMoveTo.appendChild(t1[i]); 
            document.FormPostMoveTo.submit();
        }
        else 
        {
            alert(lang.askmove);
        }
    });        
}

function MediaClick()
{
    $('.Div-MediaCenter').click(function ()
    {                
        opennewwindow('Media_Audio_Main.php?sid=' + sid);
//        var i=0;
//        var t1= new Array();    
//        document.FormMedia.reset();              
//        t1[i]=document.createElement('input');
//        t1[i].type='hidden';
//        t1[i].name='UID';
//        t1[i].value = uid;
//        document.FormMedia.appendChild(t1[i]);  
//        i++;
//        t1[i]=document.createElement('input');
//        t1[i].type='hidden';
//        t1[i].name='RootPathID';
//        t1[i].value = rootpathid;
//        document.FormMedia.appendChild(t1[i]);  
//        i++;
//        t1[i]=document.createElement('input');
//        t1[i].type='hidden';
//        t1[i].name='Username';
//        t1[i].value = session;
//        document.FormMedia.appendChild(t1[i]); 
//        i++;
//        t1[i]=document.createElement('input');
//        t1[i].type='hidden';
//        t1[i].name='LUNName';
//        t1[i].value = lunname;
//        document.FormMedia.appendChild(t1[i]);         
//        document.FormMedia.submit();        
    });       
}

function RenameClick_PC() {
    $('.renamepc').click(function () {
        var filename;
        var filecount = 0;
        var type = 0;
        $(".row_selected").each(function () {
            //tempimg = $(this).children("td").eq(0).children('.chk');
            var name_a = $(this).children("td").eq(2).children();
            renameobj = name_a;
            selid = name_a.attr("id");
            filename = name_a.text();                        
            if(name_a.hasClass('hyperfile'))
                type = 1;            
            filecount++;
        });

        if (filecount == 1) {                             
            openModalNoReload("newname.php?sid=" + sid  + "&cid=" + selid + "&tp=" + type + "&name=" +encodeURIComponent(filename.replace(/'/g, "%27")), 800, 450);
        }
        return false;
    });
}

function RenameClick_Mobile()
{
    $('.rename').click(function () {
        var filename;
        var filecount = 0;
        var type = 0;
        $(".row_selected").each(function () {
            var name_a = $(this).children("td").eq(2).children('a').eq(0);
            renameobj = name_a;
            selid = name_a.attr("id");
            filename = name_a.text();
            if(name_a.hasClass('hyperfile'))
                type = 1;            
            filecount++;
        });

        if (filecount == 1) {            
            openModalNoReload("newname.php?sid=" + sid + "&name=" + encodeURIComponent(filename.replace(/'/g, "%27")) + "&cid=" + selid + "&tp=" + type, 400, 350);
        }
        return false;
    });
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

function RebindMobileClick()
{
    ChkBoxImgClick();
    HyperFileClick();
    ShareLinkMobileClickFunction();
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

function EnableDisableZIPButton() {
    if ($('.row_selected').length == 1) {
        $(".renamepc").css("cursor", "pointer");
        $('.renamepc').attr("src", "img/"+ language_name +"/Rename.png");
        $('.rename').attr("src", "img/"+language_name+"/Phone_Rename_Enable.png");      /* Foxy Add */
        /* Foxy Add *//*$('.rename').fadeIn();*/
        
    }
    else {
        $(".renamepc").css("cursor", "default");
        $('.renamepc').attr("src", "img/" + language_name +"/NoRename.png");
        $('.rename').attr("src", "img/"+language_name+"/Phone_Rename_Disable.png");              /* Foxy Add */
        /* Foxy Add *//*$('.rename').hide();*/
        
    }
    if ($('.row_selected').length > 0) {
        $('#imgZipDownloadPc').css("cursor", "pointer");
        $('#imgZipDownloadPc').attr("src", "img/" + language_name +"/ZipDownload-Enable.png");
    }
    else{
        $('#imgZipDownloadPc').css("cursor", "default");
        $('#imgZipDownloadPc').attr("src", "img/" + language_name + "/ZipDownload-Disable.png");
    }
}

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(document.location.hash);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function DeleteClickFunction() {
    $('.deleteitem').click(function () {        
        var root_a = $("#path").children('a').eq(0);
        if(typeof(root_a.attr('id')) == 'undefined' || root_a.attr('id') == 1)        
            MoveToRecycle();            
        else if(root_a.attr('id') == -1)
            RecycleDelete();           
    });
}

function RecycleDelete()
{
    var DelPostData= {		
            FID: [],      
            sid:null,            
            act:'delete'
        };
    $(".row_selected").each(function () {
        var delimg = $(this).children("td").eq(0).children('.chk');
        if (delimg.attr("src") == "img/checkbox.png"){         
            if(delimg.hasClass('hyperfile'))
                fid = 'f' + delimg.attr("id");
            else
                fid = 'd' + delimg.attr("id");
            DelPostData.FID.push(fid);
        }
    });
    if (DelPostData.FID.length > 0) {
        var answer = confirm(lang.askdelete)
        if (answer) {
            DelPostData.sid = sid;                         
            $("body").css("cursor", "wait");
            $('.delete').attr({disabled: 'disabled'});   
            openModalWaitNoReload('',0,0);
            var request = CallAjax("FileCmdProcessPost.php", DelPostData, "json");
            request.done(function (msg, statustext, jqxhr) {                
//                alert(jqxhr.responseText);
                $("body").css("cursor", "default");                                       
                $('.delete').removeAttr('disabled');                
                modalWindow.closenoreload();
                parseUrlParamReGet();
            });
            request.fail(function (jqXHR, textStatus) {    
//                alert(jqXHR.responseText);
//                alert("fail");
                $("body").css("cursor", "default");
                $('.delete').removeAttr('disabled');                
                modalWindow.closenoreload();
                parseUrlParamReGet();
            });
        }
    }
    else {
        alert(lang.delwar);
    }
}

function MoveToRecycle()
{
    var MovePostData= {		
            FID: [],      
            sid:null,
            fid:null,
            pid:null,
            act:'move'
        };
    $(".row_selected").each(function () {
        var delimg = $(this).children("td").eq(0).children('.chk');
        if (delimg.attr("src") == "img/checkbox.png"){         
            if(delimg.hasClass('hyperfile'))
                fid = 'f' + delimg.attr("id");
            else
                fid = 'd' + delimg.attr("id");
            MovePostData.FID.push(fid);
        }
    });
    if (MovePostData.FID.length > 0) {
        var answer = confirm(lang.askdelete)
        if (answer) {
            MovePostData.sid = sid;
            MovePostData.fid = -1;   
            MovePostData.pid = cid;             
            $("body").css("cursor", "wait");
            $('.delete').attr({disabled: 'disabled'});   
            openModalWaitNoReload('',0,0);
            var request = CallAjax("FileCmdProcessPost.php", MovePostData, "json");
            request.done(function (msg, statustext, jqxhr) {                
//                alert(jqxhr.responseText);
                $("body").css("cursor", "default");                                       
                $('.delete').removeAttr('disabled');                
                modalWindow.closenoreload();
                parseUrlParamReGet();
            });
            request.fail(function (jqXHR, textStatus) {    
//                alert(jqXHR.responseText);
//                alert("fail");
                $("body").css("cursor", "default");
                $('.delete').removeAttr('disabled');                
                modalWindow.closenoreload();
                parseUrlParamReGet();
            });
        }
    }
    else {
        alert(lang.delwar);
    }
}

function CleanRecycle()
{
    if (confirm("資源回收桶內檔案將會永久消失，確定嗎？"))
    {
        var postData =
        {
            sid:null,
            act:'clean'
        };
        postData.sid = sid;
        $("body").css("cursor", "wait");
        openModalWaitNoReload('',0,0);
        var request = CallAjax("FileCmdProcessPost.php", postData, "html");
        request.done(function (msg, statustext, jqxhr)
        {            
            $("body").css("cursor", "default");                 
            modalWindow.closenoreload();
            if(document.location.hash == '#/-1/?cid=-1&act=-1')
                parseUrlParamReGet();
            else
                document.location.hash = '/-1/?cid=-1&act=-1';
        });
        request.fail(function (jqxhr, textStatus)
        {            
            $("body").css("cursor", "default");            
            modalWindow.closenoreload();
            if(document.location.hash == '#/-1/?cid=-1&act=-1')
                parseUrlParamReGet();
            else
                document.location.hash = '/-1/?cid=-1&act=-1';
        });
    }
    else
    {
        return;
    }
}

function parseUrlParamReGet()
{
    act = getParameterByName('act');
    switch(act)
    {
        case '-1':
            $('.Text-Search').val('');
            cid = getParameterByName('cid');
            if(cid == '' ){  
                BtnBackRootClick();
            } 
            else
            {                            
                RootGetFolderList(cid,act);                    
            }
            break;
        case '1':
            $('.Text-Search').val('');
            cid = getParameterByName('cid');
            if(cid == ''){  
                BtnBackRootClick();
            } 
            else
            {                            
                RootGetFolderList(cid,act);                    
            }
            break;
        case '2':
            pattern = getParameterByName('pat');
            if(pattern == '' ){                                
                BtnBackRootClick();
            } 
            else{
                $('.Text-Search').val(decodeURI(pattern));                           
                Find(encodeURIComponent(pattern));  
            }
            break;
        case '3':
            $('.Text-Search').val('');
            type = getParameterByName('tp');                        
            if(type == '' ){                                
                BtnBackRootClick();
            } 
            else{                                                        
                GetByFileType(type); 
            }
            break;
    }
}

function ShareLinkClickFunction() {
    $('.Shared').click(function () {        
        var idobj = $(this).attr("id");
        var type = 1;
        if($(this).hasClass('sharefolder'))
            type=0;
        var name = $(this).parents("tr").children('td').eq(2).children('a').text();       
        openModalReget('ShareMain.php?sid=' + sid + "&id=" + idobj + '&name=' + encodeURIComponent(name.replace(/'/g, "%27")) + '&identify=' + type,1000,600);
    });
}

function ShareLinkMobileClickFunction() {
    $('.Shared').click(function () {
        var idobj = $(this).attr("id");
        var type = 1;
        if($(this).hasClass('sharefolder'))
            type=0;
        var name = $(this).parents("tr").children('td').eq(2).children('a').text();
        openModalReget('ShareMain.php?sid=' + sid + "&id=" + idobj + '&name=' + encodeURIComponent(name.replace(/'/g, "%27")) + '&identify=' + type,1000,600);
    });
}

function ShareClickFunction() {
    $('.share').click(function ()
    {
        /* Foxy 2012.11.20 改用全畫面對話框.以免使用者可能想要移動拖動對話框 */
        /*window.location.assign("GetAllSHID.php?username=" + session + "&lunname=" + lunname + "&orgpath=" + path);*/
        /*openModal("GetAllSHID.php?username=" + session + "&lunname=" + lunname, 800, 600);        */
        //openMaxModal("GetAllSHID.php?username=" + session + "&lunname=" + lunname + "&orgpath=" + path);

        /* 2012.11.23 把上一頁按鈕失能. ( 回到 History 第一頁 ) */
        window.location.assign("GetAllSHID.php?sid=" + sid + "&cid=" + cid);        
        /* Foxy 11.21 Change Back redirect htm  ResizeLastPage = "GetAllSHID.php?username=" + session + "&lunname=" + lunname + "&orgpath=" + path;    // Foxy 2012.11.21 */
        /* Foxy 11.21 Change Back redirect htm  openMaxModal(ResizeLastPage);      // Foxy 2012.11.21 */

    });
}

function ShareMobileClickFunction(){
    $('.sharemobile').click(function(){
        window.location.assign('GetAllSHID_mobile.php?sid=' + sid + "&cid=" + cid);
    });
}

function ManageClickFunction() {
    $('.manage').click(function () {
        openModalNoReload("manage.php?sid=" + sid, 800, 450);
    });
}

function ManageMobileClickFunction()
{
    $('.manage').click(function ()
    {
        openModalNoReload("manage.php?sid=" + sid, 400, 350);
    });
}

function ZIPDownloadClick()
{
    $('#imgZipDownloadPc').click(function ()
    {                 
        if ($(".row_selected").length > 0)
        {
            switch(uploadact)
            {
                case 1:
                    NormalZipDownload();
                    break;
                case 2:
                    ShareZipDownload();
                    break;
            }
        }      
    });
}

function ShareZipDownload()
{
    var index = 1;
    var i = 0;
    var t1= new Array();
    $('#FormZipShareFile').empty();  
    $(".row_selected").each(function () {            
    var moveimg = $(this).children("td").eq(0).children('.chk');
    if (moveimg.attr("src") == "img/checkbox.png") {                   
        //MovePostData.PathName.push(moveimg.attr("id"));                
        t1[i]=document.createElement('input');
        t1[i].type='hidden';
        t1[i].name='idObjs['+index +']';
        t1[i].value = moveimg.attr("id");
        document.FormZipShareFile.appendChild(t1[i]);               
        i++;
        index++;
    }
    });                                  
    t1[i]=document.createElement('input');
    t1[i].type='hidden';
    t1[i].name='sid';
    t1[i].value = sid;
    document.FormZipShareFile.appendChild(t1[i]);  
    i++;                           
    t1[i]=document.createElement('input');
    t1[i].type='hidden';
    t1[i].name='idO';
    t1[i].value = shareforinfo.idO;
    document.FormZipShareFile.appendChild(t1[i]);  
    i++;       
    t1[i]=document.createElement('input');
    t1[i].type='hidden';
    t1[i].name='idR';
    t1[i].value = shareforinfo.idR;
    document.FormZipShareFile.appendChild(t1[i]); 
    i++;       
    t1[i]=document.createElement('input');
    t1[i].type='hidden';
    t1[i].name='idU';
    t1[i].value = shareforinfo.idU;
    document.FormZipShareFile.appendChild(t1[i]);
    i++;       
    t1[i]=document.createElement('input');
    t1[i].type='hidden';
    t1[i].name='sharecase';
    t1[i].value = shareforinfo.sharecase;
    document.FormZipShareFile.appendChild(t1[i]);
    i++;       
    t1[i]=document.createElement('input');
    t1[i].type='hidden';
    t1[i].name='code';
    t1[i].value = shareforinfo.securitycode;
    document.FormZipShareFile.appendChild(t1[i]);    
    document.FormZipShareFile.submit();
}

function NormalZipDownload()
{
    var index = 1;
    var i = 0;
    var t1= new Array();
    $('#FormZipFile').empty();  
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
    switch(NowActionForZipDownload)
    {
        case '0':
            t1[i]=document.createElement('input');
            t1[i].type='hidden';
            t1[i].name='cid';
            t1[i].value = cid;           
            document.FormZipFile.appendChild(t1[i]); 
            i++;
            break;
        case '-1':    
        case '1':
        case '2':
        case '3':
        case '4':
            t1[i]=document.createElement('input');
            t1[i].type='hidden';
            t1[i].name='tp';
            t1[i].value = NowActionForZipDownload;           
            document.FormZipFile.appendChild(t1[i]); 
            i++;
            break;               
    }
    t1[i]=document.createElement('input');
    t1[i].type='hidden';
    t1[i].name='action';
    t1[i].value = NowActionForZipDownload;      
    document.FormZipFile.appendChild(t1[i]);             
    document.FormZipFile.submit();
}

function ClearClick_PC()
{
    $('#imgClear').click(function ()
    {
        CleanRecycle();
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