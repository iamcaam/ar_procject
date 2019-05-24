/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var MovePostData= {		
    FID: [],      
    sid:null,
    fid:null,
    pid:null,
    act:'move'
};

$(document).ready(function ()
{
    addressChangEvent();    
    MoveClick();
    getBrowserWidth();
    reLocateLayout(event);
    MoveExitClick();
});


// 視窗變更大小就會觸發
$(window).resize(function (event)
{
    reLocateLayout(event);
});

// 將標題文字、表格、底下的公司名稱 隨時靠右對齊，按鈕靠左對齊
function reLocateLayout(event)
{
    var NewLeft;
    var NewWidth;
    var MarginRight = 50
    var Browser_Width = getBrowserWidth()
    var HeaderTitleText_Width = $(".HeaderTitleText").width();

    // 上方主標題會放在視窗最右邊，保持 MarginRight 寬度的位置
    NewLeft = Browser_Width - HeaderTitleText_Width - MarginRight;
    $(".HeaderTitleText").css("left", NewLeft);

    // 中間表格的寬度，則是要左右都保持 MarginRight 寬度的位置之下，剩餘的都給表
    NewWidth = Browser_Width - MarginRight * 2;
    NewLeft = MarginRight;
    $(".DivTableMoveList").css("width", NewWidth);
    $(".DivTableMoveList").css("left", NewLeft);

    // 上方的「回上一頁按鈕」跟「目標路徑」都靠左保持MarginRight 寬度
    //$(".HeaderBtnBackMain").css("left", MarginRight);
    //$(".TargetPath").css("left", MarginRight);

    // 底下的按鈕要置中對齊
    NewLeft = Browser_Width * 0.5 - $(".Btn_Move").width() - MarginRight * 0.5
    $(".Btn_Move").css("left", NewLeft);

    // 底下的按鈕要置中對齊
    NewLeft = Browser_Width * 0.5 + MarginRight * 0.5
    $(".Btn_Exit").css("left", NewLeft);
}

//取得瀏覽器視窗寬度
function getBrowserWidth()
{
    if ($.browser.msie)
    {
        return document.compatMode == "CSS1Compat" ? document.documentElement.clientWidth : document.body.clientWidth;
    }
    else
    {
        return self.innerWidth;
    }
}

function addressChangEvent()
{    
    $.address.bind('change', function(event) {   
        var cid = event.parameters.cid;                    
            if(typeof  cid == 'undefined' ){                  
                GetFolderList(1);          
                $.address.value('/');
            }                                                  
            else{                                       
                GetFolderList(cid);            
            }                            
    });
}

function GetFolderList(fid)
{     
    var postData = {                        
            sid:MovePostData.sid,            
            act:"movelf",
            fid:fid           
    };
    $("body").css("cursor", "wait");
    openModalWaitNoReload('',0,0);    
    var request = CallAjax("FileCmdProcessPost.php", postData, "json");    
    request.done(function (msg, statustext, jqxhr) {             
        $.each( msg, function( index, element ) 
        {
            switch(index)
            {
                case 0:
                    $(".DivPath").empty();
                    $(".DivPath").append(element);
                    break;
                case 1:
                    $(".listTable").empty();
                    $(".listTable").append(element);
                    break;
            }
        });                 
        $("body").css("cursor", "default");           
        modalWindow.closenoreload();
        MovePostData.fid = fid;
    });
    request.fail(function (jqxhr, textStatus) {         
        $("body").css("cursor", "default");  
        alert("Fail Get Content");
        $("#pkgLineTable").empty();
        modalWindow.closenoreload();
    });
}

function Exit()
{
//    if(IntoRecycle == "0")
        window.location.assign('content_pc.php?sid='+ MovePostData.sid +'#/' + MovePostData.pid + '/?cid='+MovePostData.pid + '&act=1')
//    else
//        window.location.assign('content_pc.php?path='+orgpath+'&session='+session+'&IntoRecycle=1')
}

function MoveExitClick()
{
    $('.Btn_Exit').click(function ()
    {
         Exit();
     });
}

function MoveClick()
{
    $('.Btn_Move').click(function ()
    {
        var answer = confirm(lang.askmove);
            if (answer){                   
                $("body").css("cursor", "wait") ;
                $('.Btn_Move').attr({ disabled: 'disabled' });                        
                var request = CallAjax("FileCmdProcessPost.php",MovePostData,"text");                
                request.done(function(msg,statustext,jqxhr) { 
//                    alert(jqxhr.responseText);
                    $("body").css("cursor", "default");                    
                    $('.Btn_Move').removeAttr('disabled');                    
                    Exit();
                });
                request.fail(function(jqXHR, textStatus) {                      
//                    alert(jqXHR.responseText);
                    $("body").css("cursor", "default");
                    $('.Btn_Move').removeAttr('disabled');                   
                    Exit();
                });
            }
     });
}

function CallAjax(url, data, datatype){   
  //alert(data);
  var request = $.ajax({ 
  type: "POST", 
  url: url,
  data:data,
  dataType: datatype
  });    
  return request;
}
