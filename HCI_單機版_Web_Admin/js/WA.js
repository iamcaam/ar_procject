/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var authnum;
var mobile;
var login_username;
$(document).ready(function(){  
    $(".user").change(function() {
        login_username = $('.user').val();
    });
    ChkBoxImgClick();
    pwdHideShow();
    $("input.user").not( $(":button") ).keydown(function (evt) {
        if (evt.keyCode == 13) { 
            if ($(this).attr("type") !== 'submit'){ 
                var fields = $(this).parents('form:eq(0),body').find('button, input, textarea, select'); 
                var index = fields.index( this ); 
                if ( index > -1 && ( index + 1 ) < fields.length ) { 
                    fields.eq( index + 1 ).focus(); 
                } 
                $(this).blur(); 
                return false; 
            } 
        } 
    }); 
    Language_Change();
    if($.cookie("ARChkrm")==1) { // 取cookie值設置於帳號欄位 2017.03.16 william
        $('#chk_rmpwd').attr("src","img/checkbox.png");
    }
    else {
        $('#chk_rmpwd').attr("src","img/checkbox_empty.png");
    }
    $(".user").val($.cookie("ARUsername")); // 取cookie值設置於帳號欄位 2017.03.16 william
});

function Language_Change(){
    $("#select_lang").change(function(){
        var select_lang = "en-us";
        $("#select_lang option:selected").each(function () {            
            select_lang = $(this).attr("value");
            RememberLang(select_lang);            
        });        
//        window.location.href = window.location.href;
        switch(select_lang){
            case 'en-us':
                lang = lang_en;      
                $('.title_wrap').css("top", "12px");     
                $('#title').css("font-size", "39px");
                break;
            case 'zh-tw':
                lang = lang_tw;  
                $('.title_wrap').css("top", "11px");
                $('#title').css("font-size", "34px");
                break;
            case 'zh-cn':
                lang = lang_cn;  
                $('.title_wrap').css("top", "11px");
                $('#title').css("font-size", "34px");                
                break;
        }
        /* 新式樣修改(藍黃)--RAID容量顯示 2016.12.13 william */
        if(VMorVDI == 'VDI')
            $('#title').html(lang.loginTitle);
        else
            $('#title').html(lang.loginTitleVM);
        $('#label_rmbme').html(lang.loginrmbme);  
        $('#label_username').html(lang.loginAccount);
        $('#label_password').html(lang.loginPWD);
        $('#label_lang').html(lang.loginChooseLanguage);
        $('#label_expire').html(lang.loginExpire);
        $('#input_login').val(lang.loginlogin);      
    });
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

function pwdHideShow() {
    $('#CurUserNamepic').click(function () {
        if ($('#CurUserNamepic').attr("src") == "img/HideTextWord.png") {
            $('#CurUserNamepic').attr("src", "img/ShowTextWord.png");
            $('.chkusername').attr("type", "text");
        }
        else {
            $('#CurUserNamepic').attr("src", "img/HideTextWord.png");
            $('.chkusername').attr("type", "password");
        }
    });
    $('#CurUserPWDpic').click(function () {
        if ($('#CurUserPWDpic').attr("src") == "img/ShowTextWord.png") {
            $('#CurUserPWDpic').attr("src", "img/HideTextWord.png");
            $('.chkpwd').attr("type", "password");
        }
        else {
            $('#CurUserPWDpic').attr("src", "img/ShowTextWord.png");
            $('.chkpwd').attr("type", "text");
        }
    });    
}

function Button_Login()
{
    $('#LoginBtn').click(function () { 
        LoginFunct();
        return false;
    }); 
}

function formhash(form, password) {
    // $("#input_login").css("background-image","-webkit-gradient(linear, left top, left bottom, from(#b6e2ff), to(#6ec2e8))");
    // $("#input_login").css("background-image","-moz-linear-gradient(top left 90deg, #b6e2ff 0%, #6ec2e8 100%)");
    // $("#input_login").css("background-image","linear-gradient(top left 90deg, #b6e2ff 0%, #6ec2e8 100%)");
    $("#input_login").css("background-color","#fdda2d");
    $("#input_login").css("border","#fdda2d");
    $("#input_login").css("color","#000000");
    // Create a new element input, this will be our hashed password field. 
    var p = document.createElement("input");

    // Add the new element to our form. 
    form.appendChild(p);
    p.name = "p";
    p.type = "hidden";
    p.value = hex_sha512(password.value);  
    var id = setSessionStorage();
    if(id != null)
    {
        var token = document.createElement("input");   
        form.appendChild(token);
        token.name = "token";
        token.type = "hidden";
        token.value = id;       
    }  
    // Make sure the plaintext password doesn't get sent. 
    // password.value = ""; // 登入時，密碼不會被清除 2017.03.16 william
    login_username = $('.user').val();    
    RememberUsername(login_username);
    // Finally submit the form. 
    form.submit();
}

function CookieHandle(set)
{
    if(navigator.cookieEnabled){  
        if(set){           
            $.cookie('ARLGIN', '1'); 
//            if($('#chk_rmpwd').attr("src") == "img/checkbox.png")
            if($(document).find("#chk_rmpwd").length > 0){                
                if($('#chk_rmpwd').attr("src") == "img/checkbox.png")
                    $.cookie('ARRM', '1', {expires: 365});
            }
            else
            {
                if($('#ChkKeepLogin').prop( "checked" ))
                    $.cookie('ARRM', '1', {expires: 365});
            }
        }
        else{
            $.cookie('ARLGIN', null);
            $.cookie('ARRM', null);
        }
        
    }  
}

function RememberLang(input_select_lang)
{
    if(navigator.cookieEnabled){
        $.cookie('ARLang', input_select_lang, {expires: 365});                                               
    }                                              
}

function RememberUsername(login_username)
{
    if($('#chk_rmpwd').attr("src") === "img/checkbox.png") {
        $.cookie('ARUsername', login_username, {expires: 365}); 
        $.cookie('ARChkrm', 1 , {expires: 365});
    } 
    else if($('#chk_rmpwd').attr("src") === "img/checkbox_empty.png") {
        $.cookie('ARUsername', null);   
        $.cookie('ARChkrm', null);   
    }  
}       

function setSessionStorage()
{
    var id = null;
    if (typeof(Storage) !== "undefined") {
        id = guid();        
        sessionStorage.setItem('token',id);    
        // alert(sessionStorage.token);    
    }    
    return id;
}

function guid() {
  function s4() {
    return Math.floor((1 + Math.random()) * 0x10000)
      .toString(16)
      .substring(1);
  }
  return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
    s4() + '-' + s4() + s4() + s4();
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