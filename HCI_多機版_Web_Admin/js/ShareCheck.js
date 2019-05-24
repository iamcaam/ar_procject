$(document).ready(function ()
{
    CheckPwdClick();
});

function CheckPwdClick()
{
    $('#confirm').click(function () {        
        var passwd = hex_md5($('input.Lbl_PwdInput').val());      
        var jsonrequest = '{"sid":' + sid + ',"cid":' + cid + ',"key":"' + key + '"' + ',"pwd":"' + passwd + '"}';        
        var request = CallAjax("ShareCmdProcessPost.php?act=CheckPwd", jsonrequest , "json");    
        request.done(function(msg,statustext,jqxhr) {                
            $.each( msg, function( index, element ) 
            {            
                switch(index)
                {
                    case 'result':                                                  
                        if(element == 0){
                            window.location.href = "SharePwd.php?sid="+sid+"&id=" + cid + "&key=" + key + "&key1=" + passwd + "&type=" + type;
                        }                            
                        else if(element < 0 && element > -99){
                            alert('Password is Error.')
                        }   
                        else if(element <= -100 && element > -200){
                            alert('Password is Error.')
                        }        
                        else if(element <= -200 && element > -300){
                            alert('Password is Error.')
                        }
                        else if(element <= -300 && element > -400){
                            alert('Password is Error.')
                        }
                        break;                
                }            
            });
        });
        request.fail(function(jqXHR, textStatus) {              
            alert('Password is Error(Result:-501).')
        });
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