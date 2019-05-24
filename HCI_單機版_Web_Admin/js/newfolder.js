/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//var checkname_pattern = /^[:/,._]|[\\\*<>\|"/']+/;
var checkname_pattern = /^[:/,._]|[\\\*<>\|"/]+/;
$(document).ready(function () {
    btn_rename_click();
    reLocateLayout();
});


// 視窗變更大小就會觸發
$(window).resize(function ()
{
    reLocateLayout();
});


function reLocateLayout()
{
    var NewLeft;
    var NewTop;
    var NewWidth;

    // 大標頭
    NewLeft = $('.divmodal').width() * 0.5 - $('.Label_Title').width() * 0.5;
    $('.Label_Title').css("left", NewLeft);

    var DivCreateFolderWidth = $('.DivCreateFolder').width();
    var DivCreateFolderHeight = $('.DivCreateFolder').height();

    // 輸入框位置由提示文字位置決定
    NewLeft = DivCreateFolderWidth * 0.05 + $('.Label_FolderName').width();
    $('.Text_FolderName').css("left", NewLeft);

    // 輸入框寬度為總寬度-兩個5％邊距-文字提示寬度 - 為蘋果瀏覽器多預留的 15px
    NewWidth = DivCreateFolderWidth - ( DivCreateFolderWidth * 0.1 ) - $('.Label_FolderName').width() - 15;
    $('.Text_FolderName').css("width", NewWidth);


    // 確定按鈕
    NewTop = DivCreateFolderHeight - $('.Image_BtnOk').height() - 15;
    NewLeft = DivCreateFolderWidth * 0.5 - $('.Image_BtnOk').width() * 0.5;
    $('.Image_BtnOk').css("left", NewLeft);
    $('.Image_BtnOk').css("top", NewTop);
}



function btn_rename_click()
{
    $('.Image_BtnOk').click(function ()
    {
        var checkname = $('.Text_FolderName').val();
        //alert(checkname);
        if(checkname.match(checkname_pattern)){
            alert(lang.charwarning);
            return;
        }                       
         var postData = {                        
            sid:sid,            
            act:"mf",
            fid:cid,
            name:encodeURIComponent(checkname)
        };
        
        $("body").css("cursor", "wait");                
        var request = CallAjax("FileCmdProcessPost.php", postData, "json");    
        request.done(function (msg, statustext, jqxhr) {             
            $("body").css("cursor", "default");  
            if(msg == '0'){
                window.top.ExitRefreshFolderContent(cid);
            }
            else if(msg == '-3')
                alert(lang.FolderExit);            
            else
                alert(lang.CreateFolderFail);            
        });
        request.fail(function (jqxhr, textStatus) {               
            alert(jqxhr.responseText);
            $("body").css("cursor", "default");  
            alert("Fail Create(Something Error).");            
        });
    });
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

