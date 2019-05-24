/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var orgcid;
var MovePostData= {		
    FID: [],      
    sid:null,
    pid:null,
    fid:null,
    act:'move'
};
$(document).ready(function() {  
    ExitMoveMobile();    
    addressChangEvent();
    MoveClick();
});       

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
            act:"movemobilelf",
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
                    $("#path").empty();
                    $("#path").append(element);
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

function MoveClick()
{
     $('.move').click(function(){                            
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
    var request = $.ajax({ 
    type: "POST", 
     url: url,
    data:data,
    dataType: datatype
    });    
    return request;
} 

function ExitMoveMobile()
{
    $('.close-window').click(function(){  
        Exit();
    });    
}

function Exit()
{
    window.location.assign('content.php?sid='+ MovePostData.sid +'#/' + MovePostData.pid + '/?cid=' + MovePostData.pid + '&act=1');
}


