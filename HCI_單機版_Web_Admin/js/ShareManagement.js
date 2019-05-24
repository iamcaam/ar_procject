/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var select_all;
var bolclickshift;
var lastSelected;
var lastSelectedRow;
$(document).ready(function() {  
                SelectClickFunction();
                ChkBoxImgClick();
                DeleteShare();
                getBrowserWidth();
                reLocateLayout(event);               
                select_all = true;  
            });
       
// 視窗變更大小就會觸發
$(window).resize( function (event)
{
    reLocateLayout(event);
});

// 將標題文字、表格、底下的公司名稱 隨時靠右對齊，按鈕靠左對齊
function reLocateLayout(event)
{
	var NewLeft;
	var NewWidth;
	var MarginRight = 50
	var HeaderTitleText_Width = $(".HeaderTitleText").width();
	var Browser_Width = getBrowserWidth()

    // 上方主標題會放在視窗最右邊，保持 MarginRight 寬度的位置
	NewLeft = Browser_Width - HeaderTitleText_Width - MarginRight;
	$(".HeaderTitleText").css("left", NewLeft);

    // 上方的「回上一頁按鈕」跟「全選」按鈕都靠左保持MarginRight 寬度
	//$(".HeaderBtnBackMain").css("left", MarginRight);
	$(".DivSelectAllCheckBox").css("left", MarginRight);
	$(".LabelSelectAllText").css("left", MarginRight + 32);

    // 表格中間有個標題，標題要在正中央，就是用視窗寬度一半，扣掉自身寬度一半
	NewLeft = Browser_Width * 0.5 - $(".DivTableTitle").width() * 0.5
	$(".DivTableTitle").css("left", NewLeft);

    // 中間表格的寬度，則是要左右都保持 MarginRight 寬度的位置之下，剩餘的都給表格
	NewWidth = Browser_Width - MarginRight * 2;
	NewLeft = MarginRight;
	$(".DivTableShareList").css( "width", NewWidth );
	$(".DivTableShareList").css("left", NewLeft);

    // 底下的按鈕要置中對齊
	NewLeft = Browser_Width * 0.5 - $(".DivBottomBtnDelete").width() - MarginRight * 0.5
	$(".DivBottomBtnDelete").css("left", NewLeft);

    // 底下的按鈕要置中對齊
	NewLeft = Browser_Width * 0.5 + MarginRight * 0.5
	$(".DivBottomBtnExit").css("left", NewLeft);
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




function ChkBoxImgClick()
{
    $('.chk').click(function(){        
        var parent;
        var parent1;
       if($(this).attr("src") == "img/checkbox_empty.png"){           
           parent = $(this).parent().get(0)           
           parent1 = $(parent).parent().get(0);
           $(parent1).addClass('row_selected'); 
           $(this).addClass('select');
           $(this).attr("src","img/checkbox.png");
       }
       else{
           parent = $(this).parent().get(0);          
           parent1 = $(parent).parent().get(0);
           $(parent1).removeClass('row_selected'); 
           $(this).removeClass('select');
           $(this).attr("src","img/checkbox_empty.png");
       }        
    });
}

function SelectClickFunction()
{
    $('.selectitem').click(function(){    
             if(select_all){         
                    $("img:.chk").addClass('select');
                    $("img:.chk").attr("src","img/checkbox.png");
                }
                else{
                    $("img:.chk").removeClass('select');
                    $("img:.chk").attr("src","img/checkbox_empty.png");
                }      
      if(select_all){
          bolclickshift = true;
          //$('.listTable tr').removeClass('listTabletr');
          $('.listTable tr').addClass('row_selected');  
          //$('.listTable tr:eq(0)').removeClass('row_selected');  
          //$('.listTable tr:eq(0)').addClass('listTabletr');
          if($(this).hasClass('imgselectall'))
//            $('.selectitem').text("取消全部");
//          else
              $(this).attr("src","img/checkbox.png");
      }
       else{
           bolclickshift = false;
           $('.listTable tr').removeClass('row_selected');  
           //$('.listTable tr').addClass('listTabletr');  
           if($(this).hasClass('imgselectall'))
//                $('.selectitem').text("選擇全部");
//           else
               $(this).attr("src","img/checkbox_empty.png");
       }
       select_all = !select_all;       
    });
}

function DeleteShare()
{
    $('.deleteshare').click(function(){   
         var postData = {	
                sid :sid,
                act : 'delshare',
                ShareIDlist: []  
            };
         $(".row_selected").each(function() { 
            var delimg = $(this).children("td").eq(0).children('.chk');
            if(delimg.attr("src") == "img/checkbox.png")
                postData.ShareIDlist.push(delimg.attr("id"));
        });
        if(postData.ShareIDlist.length > 0){             
            var answer = confirm(lang.askdelshare)
            if (answer){                   
                $("body").css("cursor", "wait") ;
                openModalWaitNoReload('',0,0);
                var request = CallAjax("ShareCmdProcessPost.php",postData,"text");                
                request.done(function(msg,statustext,jqxhr) {
//                    alert(jqxhr.responseText);
                    $("body").css("cursor", "default");
                    modalWindow.closenoreload();
                    window.location.href = window.location.href;
                });
                request.fail(function(jqXHR, textStatus) {
//                    alert(jqXHR.responseText);
                    $("body").css("cursor", "default");
                    modalWindow.closenoreload();
                    window.location.href = window.location.href;
                });
            }
        }
        else{
            alert(lang.delsharewar);
        } 
    });
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
