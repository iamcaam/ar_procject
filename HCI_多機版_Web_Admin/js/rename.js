/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//var checkname_pattern = /^[:/,._]|[\\\*<>\|"/']+/;
var checkname_pattern = /^[:/,._]|[\\\*<>\|"/]+/;
$(document).ready(function () {
    action =false;
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

    var DivRenameWidth = $('.DivChangeName').width();
    var DivRenameHeight = $('.DivChangeName').height();

    // 輸入框位置由提示文字位置決定
    NewLeft = DivRenameWidth * 0.05 + $('.Lbl_NewName').width();
    $('.Text_NewName').css("left", NewLeft);

    // 輸入框寬度為總寬度-兩個5％邊距-文字提示寬度 - 為蘋果瀏覽器多預留的 15px
    NewWidth = DivRenameWidth - ( DivRenameWidth * 0.1 ) - $('.Lbl_NewName').width() - 15;      
    $('.Text_NewName').css("width", NewWidth);

    // 確定按鈕
    NewTop = DivRenameHeight - $('.Image_BtnOk').height() - 15;
    NewLeft = DivRenameWidth * 0.5 - $('.Image_BtnOk').width() * 0.5;
    $('.Image_BtnOk').css("left", NewLeft);
    $('.Image_BtnOk').css("top", NewTop);
}

function btn_rename_click()
{    
   
    $('.Image_BtnOk').click(function ()
    {
        if(!action)
        {
            var checkname = $('.Text_NewName').val();
            //alert(checkname);
            if(checkname.match(checkname_pattern)){
                alert(lang.charwarning);
                return;
            }           
            if(ext.length > 0)
                $newname = checkname + '.' + ext;
            else
                $newname = checkname;
            var postData = {                        
                sid:sid,            
                act:"rename",
                fid:cid,
                tp : tp,
                oldname:encodeURIComponent(old),
                newname:encodeURIComponent($newname)
            };         
            $("body").css("cursor", "wait");                
            var request = CallAjax("FileCmdProcessPost.php", postData, "json");    
            request.done(function (msg, statustext, jqxhr) {             
                $("body").css("cursor", "default");     
                action =false;
                if(msg == '0'){                   
                   window.top.ExitRenameClick(cid,$newname);
                }                        
                else if(msg == '-3')
                    alert(lang.FileExit);            
                else
                    alert(lang.RenameFail);  
                
            });
            request.fail(function (jqxhr, textStatus) {               
                alert(jqxhr.responseText);
                $("body").css("cursor", "default");  
                alert("Fail Rename(Something Error).");            
                action =false;
            });
            
        }
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