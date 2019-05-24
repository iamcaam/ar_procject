/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var path;
var session;
var bol_uploading;
var StrUploadInfo_FileName = [];
var StrUploadInfo_FileSize = [];
var StrUploadInfo_FileCount;
var checkname_pattern = /^[:/,._]|[\\\*<>\|"/']+/;

$(document).ready(function ()
{
    bol_uploading = false;
    // $('#stateDiv').hide();       // Foxy 2012/09/06
    $('#img_prog').hide();          // Foxy 2012/09/06
    CancelClick();
//    ExitClick();
    ChkBoxImgClick();


    if (!String.prototype.trim)
    {
        String.prototype.trim = function ()
        {
            return this.replace(/^\s+|\s+$/g,'');
        }
    }

    $("input[type='file']").bind("change", function (event) 
    {
        StrUploadInfo_FileCount = event.target.files.length;

        // 如果有選檔案才進入
        if ( 0 < StrUploadInfo_FileCount )
        {
            $('.Div_UploadInfo').css("visibility", "visible");

            // 這是為了未來而寫的，目前暫時先用單檔上傳
            // 超過一定個數才顯示出 ScrollBar
            if (4 < StrUploadInfo_FileCount)
            {
                $('.Div_UploadInfo').css("overflow", "scroll");
            }
            else
            {
                $('.Div_UploadInfo').css("overflow", "hidden");
            }

            // 建立陣列個數
            StrUploadInfo_FileName = [StrUploadInfo_FileCount];
            StrUploadInfo_FileSize = [StrUploadInfo_FileCount];

            for (var i = 0; i < StrUploadInfo_FileCount; i++)
            {
                // 第一個資訊只需要填入已經在畫面上的物件就好
                if (0 == i)
                {
                    var file = event.target.files[i];
                    StrUploadInfo_FileName[i] = file.name;
                    StrUploadInfo_FileSize[i] = file.size;

                    var DotPotition = StrUploadInfo_FileName[i].lastIndexOf('.');
                    var ChangeName = StrUploadInfo_FileName[i].substr(0, DotPotition);
                    var OriginSize = StrUploadInfo_FileSize[i];
                    var ChangeSize = 0;

                    if (1024 > OriginSize)
                    {
                        ChangeSize = parseInt(StrUploadInfo_FileSize[i] + "Byte");
                    }
                    else if (1048576 > OriginSize)
                    {
                        ChangeSize = parseInt(StrUploadInfo_FileSize[i] / 1024) + "KB";
                    }
                    else if (1073741824 > OriginSize)
                    {
                        ChangeSize = parseInt(StrUploadInfo_FileSize[i] / 1024 / 1024 ) + "MB";
                    }
                    
                    $('.FileNameInfo').val(ChangeName);
                    $('.FileSizeInfo').text(ChangeSize);
                }

                // 這是為了未來而寫的，目前暫時先用單檔上傳
                else
                {
                    var UI_Template_FileInfo = $('.Div_UploadInfo').html;
                    $('.Div_UploadInfo').append(UI_Template_FileInfo);
                    $('.FileNameTitle' + i).css(StrUploadInfo_FileName[i]);
                }
            }
        }
        else
        {
            $('.Div_UploadInfo').css("visibility", "hidden");
        }
    });
});

function ChkBoxImgClick()
{
    $('#img_check_exit').click(function(){
       if($(this).attr("src") == "img/checkbox_empty.png")
           $(this).attr("src","img/checkbox.png");
       else
           $(this).attr("src","img/checkbox_empty.png");
    });
}

function ExitClick()
{
    $('#btn_close').click(function(){   
        if(!bol_uploading)
            window.location.assign('content.php?path='+ path +"&session="+session);
        else{            
            var answer = confirm(lang.upexit);
            if (answer){                  
                window.location.assign('content.php?path='+ path +"&session="+session);
            }
        }
    });
}

function CancelClick()
{
    $('#btn_cancel').click(function(){   
         var filepath = $('#userfile').attr("value");        
        if(filepath.length > 0){    
            var answer = confirm(lang.upcancel);
            if (answer){  
                window.top.location.href = window.top.location.href;
            }
        }
        else
            window.top.location.href = window.top.location.href;
    });
}

function startUpload()
{
    var filepath = $('#FileNameInfo').val();

    if (filepath.length > 0)
    {
        if(filepath.match(checkname_pattern)){
                alert(lang.charwarning);
                return false;
        }       
        bol_uploading = true;
        //$('.set').attr({disabled:'disabled'}); 
        $('#lab_status').text(lang.uploaidng)
        //$('#stateDiv').fadeIn();       // Foxy 2012/09/06
        $('#img_prog').fadeIn();
        //alert("true3");               
        return true;
    }
    else
    {
        alert(lang.upselectfile);
        return false;
    }
   
}

function CancelUpload()
{
    bol_uploading = false;
    $('.set').removeAttr('disabled');   
    //$('#stateDiv').hide();         // Foxy 2012/09/06
    $('#img_prog').hide();
    var file = $(":file");   
    file.after(file.clone().val(""));   
    file.remove();  
}

function stopUpload(result)
{    
    bol_uploading = false;
    $('#img_prog').hide();    
    if(result == "0"){
        $('#lab_status').text(lang.upsuccess);
        if( $('#img_check_exit').attr("src") == "img/checkbox.png"){
            //alert("Exit");
            //window.top.ExitClick();
            //window.location.assign('content.php?path='+ path +"&session="+session);
            if(idU == "-1")
                window.top.ExitRefreshFolderContent(cid);
            else{
                window.top.ExitRefreshShareFolderContent(idR,idO,idU,sharecase,code);
            }
        }
        //else
         //   alert("Reserve");
             
    }   
    else{        
        if(result == "1"){ 
            $('#lab_status').text(lang.upover);
        }
        else{
            $('#lab_status').text(lang.uperror);            
        }
    }
}
