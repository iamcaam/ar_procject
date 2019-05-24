// 設定分享期間（不光是編輯修改）
function Ajax_ModifyShareDate(accesskey,id,lifetime,lifetype)
{
    var jsonrequest = 
	'{"id":' + id + 
	',"accesskey":"' + accesskey + 
	'","lifetime":"' + lifetime + 
	'","lifetype":' + lifetype + 
	'}';
	
	if ( true == ShowAjaxDebug )
	{
		alert( "Ajax_ModifyShareDate(Send):" + jsonrequest);
	}
	
    var request = CallAjax("ShareCmdProcessPost.php?act=ModifyDate", jsonrequest , "json");    
    
	request.done(function (msg, statustext, jqxhr) 
	{
		if ( true == ShowAjaxDebug )
		{
			alert( "Ajax_ModifyShareDate(Rcv):" + jqxhr.responseText);
		}
		
        $.each( msg, function( index, element ) 
        {                       
            switch(index)
            {
                case 'result':  
                    //    -1 ~ -99 parameter error
                    //    -101,-102 access key error 
                    //    -200 ~ -299 db error
                    //    -300 ~ -399 modify error
                    if(element == 0)
					{
        				alert(LanguageStr_SharePeriod.AjaxMsg00);
						Ajax_ChkInitialStatus();
						$( ".H4_Description" ).text( "" );
                    }   
                    else if(element < 0 && element > -99)
					{
                        alert(LanguageStr_SharePeriod.AjaxMsg01);
                    }   
                    else if(element <= -100 && element > -200)
					{
                        alert(LanguageStr_SharePeriod.AjaxMsg02);
                    }        
                    else if(element <= -200 && element > -300)
					{
                        alert(LanguageStr_SharePeriod.AjaxMsg03);
                    }
                    else if(element == -303)
					{
                        alert(LanguageStr_SharePeriod.AjaxMsg04);
                    }
                    else if(element == -301)
					{
                        alert(LanguageStr_SharePeriod.AjaxMsg05);
                    }
                    else if(element <= -300 && element > -400)
					{
                        alert(LanguageStr_SharePeriod.AjaxMsg06);
                    }
                    else
					{
                        alert(LanguageStr_SharePeriod.AjaxMsg07 + element);
                    }
                    break;                
            }
        });
    });
    request.fail(function (jqxhr, textStatus) 
	{         
        alert(jqxhr.responseText);              
    });
}

