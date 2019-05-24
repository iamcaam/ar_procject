/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var authnum;
var mobile;
$(document).ready(function() 
{
    $('#select_lang').scombobox({
        editAble:false,
        wrap:false
    });
});

function Language_Change(){
    $(".SelectLang").change(function(){
        var lang = "en-us";
        $(".SelectLang option:selected").each(function () {            
            lang = $(this).attr("id");
            RememberLang(lang);            
        });
        //window.location.href = "index.php?lang=" + lang;
        window.location.href = window.location.href;
    });
}

function loginarea_mousedown()
{
    $('.login_area').keypress(function(event) {
        if (event.which == 13){            
            loginfunct(3);
        }
    })
}
function ImgVerifyClick()
{
    $('#imgverify').click(function(){
        ImgVerifyRefresh();
    });    
}

function ImgVerifyRefresh()
{
    authnum = (Math.floor((Math.random()*9)+1))*1000 + (Math.floor((Math.random()*9)+1)) * 100 + (Math.floor((Math.random()*9)+1)) * 10 + Math.floor((Math.random()*9)+1);
    $('#imgverify').attr("src",'verify/verify.php?authnum='+ authnum);
}

function ChkBoxImgClick()
{
    $('.rmpwd').click(function(){
       if($('#chk_rmpwd').attr("src") == "img/checkbox_empty.png")
           $('#chk_rmpwd').attr("src","img/checkbox.png");
       else
           $('#chk_rmpwd').attr("src","img/checkbox_empty.png");
    });
}

function Button_Login()
{
     $('#btnlogin').click(function () { 
            loginfunct(3);
        }); 
}

function loginfunct(retrycount)
{    
    //logininfo = new LoginInfo($('input.user').val(), $('input.pw').val()); 
    $('.loginlun').attr({disabled:'disabled'});        
    if($('input.pw').val().length <= 0){
        alert(lang.loginfail);
        $('.loginlun').removeAttr('disabled');                   
        ImgVerifyRefresh();
        return;
    }
    if($(document).find("#imgverify").length > 0){  
        if($('input.verifyinput').val() != authnum){                    
                alert(lang.loginverifyerr);
                $('.loginlun').removeAttr('disabled'); 
                ImgVerifyRefresh();
                return;
            }
    }
    $("body").css("cursor", "wait") ;
    LoginAjax(retrycount,'');    
}

function LoginAjax(retrycount)
{
    var passwd = hex_md5($('input.pw').val());
    var xmlstr = "<acrored><username>" + $('label.user').text() +"</username><passwd>" + passwd + "</passwd></acrored>";       
    var request = CallAjax("auth/login", xmlstr, "xml");
    request.done(function(msg,statustext,jqxhr) {    
//        alert(jqxhr.responseText);
        $("body").css("cursor", "default");            
        XmlParse(msg);            
    });
    request.fail(function(jqXHR, textStatus) {  
//        alert(jqXHR.responseText);
        if(retrycount == 0){
            alert( lang.loginfaildot  + textStatus + ".");
            $('.loginlun').removeAttr('disabled');    
            $("body").css("cursor", "default");
            ImgVerifyRefresh();
        }
        else{
            retrycount--;
            LoginAjax(retrycount);
        }
    });
}

function XmlParse(xmldata){
    var level = -99;
    var result = -99;    
    $(xmldata).find('status').each(function(){
        result = $(this).text();
    });
    if(result == 0){        
        $(xmldata).find('level').each(function(){
            level = $(this).text();
        })
        if(level != -99){          
            $(xmldata).find('AccessKey').each(function(){
                var key = $(this).text();
                if(key.length > 0){
                    if(level != '99'){
                        CookieHandle(true);
                        if(mobile)
                            window.location.href = "content.php?sid=" + key;
                        else
                            window.location.href = "content_pc.php?sid=" + key;
                    }
                    else
                    {
                        CookieHandle(false);
                        window.location.href = "ManageUser.php?sid=" + key;
                    }
                }   
                else{
                    alert(lang.loginfail);      
                }                
            })        
        }
        else
            alert(lang.loginfail); 
    }
    else{
        alert(lang.loginfail);        
        $('.loginlun').removeAttr('disabled');
        ImgVerifyRefresh();
    }
}

function CookieHandle(set)
{
    if(navigator.cookieEnabled){  
        if(set){           
            $.cookie('ARLGIN', '1'); 
            if($('#chk_rmpwd').attr("src") == "img/checkbox.png")
                $.cookie('ARRM', '1', {expires: 365});
        }
        else{
            $.cookie('ARLGIN', null);
            $.cookie('ARRM', null)
        }
        
    }  
}

function RememberLang(input_select_lang)
{
//     if(typeof(Storage)!=="undefined"){
//        localStorage.ARLang = input_select_lang;                              
//    }
//    else{
        if(navigator.cookieEnabled){
            $.cookie('ARLang', input_select_lang, {expires: 365});                        
//            $.cookie('ARLang', input_select_lang);                        
        }                                     
//    }          
}

function CallAjax(url, data, datatype){   
    var request = $.ajax({ 
    type: "POST", 
    url: url,
    data:data,
    dataType: datatype
    });    
    return request;
}

function checkv6 (elem){
    var v6Expression = "/^(([A-Fa-f0-9]{1,4}:){7}[A-Fa-f0-9]{1,4})$|^([A-Fa-f0-9]{1,4}::([A-Fa-f0-9]{1,4}:){0,5}[A-Fa-f0-9]{1,4})$|^(([A-Fa-f0-9]{1,4}:){2}:([A-Fa-f0-9]{1,4}:){0,4}[A-Fa-f0-9]{1,4})$|^(([A-Fa-f0-9]{1,4}:){3}:([A-Fa-f0-9]{1,4}:){0,3}[A-Fa-f0-9]{1,4})$|^(([A-Fa-f0-9]{1,4}:){4}:([A-Fa-f0-9]{1,4}:){0,2}[A-Fa-f0-9]{1,4})$|^(([A-Fa-f0-9]{1,4}:){5}:([A-Fa-f0-9]{1,4}:){0,1}[A-Fa-f0-9]{1,4})$|^(([A-Fa-f0-9]{1,4}:){6}:[A-Fa-f0-9]{1,4})$/";
    if(elem.value.match(v6Expression)){
        return true;
    }else{
        return false;
    }
}