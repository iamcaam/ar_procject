/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var isMobile;

$(document).ready(function ()
{
    // 如果是 64bit 則不能設定「唯讀」，所以該區域隱藏  
    $('.DivSetReadOnly').css("visibility", "hidden");
    $('.DivChangePwd').css("width", "90%");
    $('.DivChangePwd').css("left", "5%");
    

    if (isMobile == 1)
    {
        // 左邊字體大小
        $('.LblPwdTitle').css("font-size", 15);
        $('.LblOldPwd').css("font-size", 13);
        $('.LblNewPwd').css("font-size", 13);
        $('.LblNewPwdAgain').css("font-size", 13);
        $('.InputOldPwd').css("font-size", 13);
        $('.InputNewPwd').css("font-size", 13);
        $('.InputNewPwdAgain').css("font-size", 13);

        // 右邊字體大小
        $('.LblReadOnlyTitle').css("font-size", 15);
        $('.LblReadOnly').css("font-size", 13);

        // 按鈕長寬
        $('.ImgBtnPwdOk').css("width", 91);
        $('.ImgBtnPwdOk').css("height", 30);
        $('.ImgBtnReadOnlyOk').css("width", 91);
        $('.ImgBtnReadOnlyOk').css("height", 30);
    }
    else
    {        
        // 左邊字體大小
        $('.LblPwdTitle').css("font-size", 20);
        $('.LblOldPwd').css("font-size", 16);
        $('.LblNewPwd').css("font-size", 16);
        $('.LblNewPwdAgain').css("font-size", 16);
        $('.InputOldPwd').css("font-size", 16);
        $('.InputNewPwd').css("font-size", 16);
        $('.InputNewPwdAgain').css("font-size", 16);

        // 右邊字體大小
        $('.LblReadOnlyTitle').css("font-size", 20);
        $('.LblReadOnly').css("font-size", 16);

        // 按鈕長寬
        $('.ImgBtnPwdOk').css("width", 114);
        $('.ImgBtnPwdOk').css("height", 38);
        $('.ImgBtnReadOnlyOk').css("width", 114);
        $('.ImgBtnReadOnlyOk').css("height", 38);
    }
    reLocateLayout();
});


// 視窗變更大小就會觸發
$(window).resize(function ()
{
    reLocateLayout();
});



function reLocateLayout()
{
    // 大標頭
    var NewLeft = $('.divmodal').width() * 0.5 - $('.Lbl_Title').width() * 0.5;
    $('.Lbl_Title').css("left", NewLeft);


    ///// 左邊設定密碼區 /////
    var DivPwdWidth = $('.DivChangePwd').width();

    var NewLeft = DivPwdWidth * 0.5 - $('.LblPwdTitle').width() * 0.5;
    $('.LblPwdTitle').css("left", NewLeft);


    // 確定按鈕
    var NewTop = $('.DivChangePwd').height() - $('.ImgBtnPwdOk').height() - 15;
    NewLeft = DivPwdWidth * 0.5 - $('.ImgBtnPwdOk').width() * 0.5;
    $('.ImgBtnPwdOk').css("left", NewLeft);
    $('.ImgBtnPwdOk').css("top", NewTop);



    ///// 右邊設定唯獨區 /////
    var DivSetReadOnly = $('.DivSetReadOnly').width();

    // 標題文字內容
    NewLeft = DivSetReadOnly * 0.5 - $('.LblReadOnlyTitle').width() * 0.5;
    $('.LblReadOnlyTitle').css("left", NewLeft);

    // 提示文字內容
    NewLeft = DivSetReadOnly * 0.5 - $('.LblReadOnly').width() * 0.5;
    $('.LblReadOnly').css("left", NewLeft);

    // 確定按鈕
    NewTop = $('.DivSetReadOnly').height() - $('.ImgBtnReadOnlyOk').height() - 15;
    NewLeft = DivSetReadOnly * 0.5 - $('.ImgBtnReadOnlyOk').width() * 0.5;
    $('.ImgBtnReadOnlyOk').css("left", NewLeft);
    $('.ImgBtnReadOnlyOk').css("top", NewTop);
}



function ManageClick()
{
    PostData =
	{
	    user: null
	};

    $("body").css("cursor", "wait");
    $('.ImgBtnReadOnlyOk').attr({ disabled: 'disabled' });
    PostData.user = session;
    var request = CallAjax("managepost.php", PostData, "xml");
    request.done(function (msg, statustext, jqxhr)
    {
        $("body").css("cursor", "default");
        $('.ImgBtnReadOnlyOk').removeAttr('disabled');
        window.top.location.href = window.top.location.href;
    });
    request.fail(function (jqXHR, textStatus)
    {
        $("body").css("cursor", "default");
        $('.ImgBtnReadOnlyOk').removeAttr('disabled');
        window.top.location.href = window.top.location.href;
    });
}

function ChangePwdClick()
{
    var OldPwd = $('.InputOldPwd').val();
    var NewPwd = $('.InputNewPwd').val();
    var NewPwdAgain = $('.InputNewPwdAgain').val();

    // 有任何一個空格留白    
    if (OldPwd == "")
    {
        alert("請輸入舊密碼。");
        return;
    }

    if (NewPwd == "")
    {
        alert(lang.newpwdwar);
        return;
    }

    if (NewPwdAgain == "")
    {
        alert(lang.renewpwdwar);
        return;
    }

    // 兩個新密碼要相同
    if (NewPwd != NewPwdAgain)
    {
        alert(lang.diffpwdwar);
        return;
    }
    
    //只能1-16字元
    RegularPwd = /^.{1,16}$/;    

    if (false == RegularPwd.test(NewPwd))
    {
        alert(lang.pwdreg);
        return;
    }

    var xmlstr = "<acrored><AccessKey>" + key + "</AccessKey><passwd>" + NewPwd + "</passwd><oldpasswd>" + OldPwd + "</oldpasswd></acrored>";

    $("body").css("cursor", "wait");
    $('.ImgBtnPwdOk').attr({ disabled: 'disabled' });    
    var request = CallAjax("user/change/passwd", xmlstr, "xml");
    request.done(function (msg, statustext, jqxhr)
    {
//        alert(jqxhr.responseText);
        $("body").css("cursor", "default");
        $('.ImgBtnPwdOk').removeAttr('disabled');
        XmlParse(msg);
    });
    request.fail(function (jqXHR, textStatus)
    {
//        alert(jqXHR.responseText);
        $("body").css("cursor", "default");
        $('.ImgBtnPwdOk').removeAttr('disabled');
        alert(lang.modpwdfail);
    });
}




function XmlParse(xmldata){
    var result = -99;    
    $(xmldata).find('status').each(function(){
        result = $(this).text();
    });
    if(result == 0){        
            alert(lang.modpwdsuccess);       
//            window.top.location.href = "index.php";
            logout_funct();       
    }
    else{
        alert(lang.modpwdfail);               
    }
}

function logout_funct()
{    
    var xmlstr = "<acrored><AccessKey>" + key +"</AccessKey></acrored>";       
    var request = CallAjax("auth/logout", xmlstr, "xml");
    request.done(function(msg,statustext,jqxhr) {         
//        alert(jqxhr.responseText);
        if(navigator.cookieEnabled){          
            $.cookie('ARLGIN', null);
            $.cookie('ARRM', null)                
        }  
        var url = XmlParseLogout(msg);        
        window.top.location.href = url;
    });
    request.fail(function(jqXHR, textStatus) {  
//        alert(jqxhr.responseText);
        if(navigator.cookieEnabled){          
            $.cookie('ARLGIN', null);
            $.cookie('ARRM', null)                
        }                     
        window.top.location.href = 'index.php';
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
function CallAjax(url, data, datatype)
{
    var request = $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType: datatype
    });
    return request;
}
