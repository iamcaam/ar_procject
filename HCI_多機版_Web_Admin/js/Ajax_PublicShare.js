
function Ajax_CreateShare(accesskey, id, sharecase, passwd, description, lifetime, lifetype)
{
    var jsonrequest =
		'{"id":' + id + 
		',"accesskey":"' + accesskey +  
		'","sharecase":' + sharecase + 
		',"passwd":"' + passwd + 
		'","description":"' + description + 
		'","lifetime":"' + lifetime + 
		'","lifetype":' + lifetype + 
		'}';
		
    var request = CallAjax("ShareCmdProcessPost.php?act=Create", jsonrequest , "json");   
	 
    request.done(function (msg, statustext, jqxhr) 
		{
        $.each( msg, function( index, element ) 
        {                       
            switch(index)
            {
                case 'result':  
                    //    -1 ~ -99 parameter error
                    //    -101,-102 access key error 
                    //    -200 ~ -299 db error
                    //    -300 ~ -399 create error
                    if(element == 0)
					{
						Ajax_ChkInitialStatus();
                    }
                    else if(element < 0 && element > -99)
					{
                        alert(LanguageStr_PublicShare.AjaxMsg00);
                    }   
                    else if(element <= -100 && element > -200)
					{
                        alert(LanguageStr_PublicShare.AjaxMsg01);
                    }        
                    else if(element <= -200 && element > -300)
					{
                        alert(LanguageStr_PublicShare.AjaxMsg02);
                    }
                    else if(element <= -300 && element > -400)
					{
                        alert(LanguageStr_PublicShare.AjaxMsg03);
                    }
                    else
					{
                        alert(LanguageStr_PublicShare.AjaxMsg04 + element);
                    }
                    break;
										  
                case 'url':
						$('.H4_Link').text( element );
						$('.H4_Link').attr( 'href', element );
						Asyc_CreatedPublicShare();
                    break;              
            }
        });
    });
    request.fail(function (jqxhr, textStatus)
	{  
        alert(LanguageStr_PublicShare.AjaxMsg05 + jqxhr.responseText);              
    });
}





function Ajax_DeleteShare(accesskey,shares)
{
    var jsonrequest = 
	'{"accesskey":"' + accesskey +  
	'","Shares":[';     
    $.each(shares,function(index,element)
	{
        if(index == 0)
            jsonrequest += '{"id":' + element.id + ',"sharecase":' + element.sharecase + '}';
        else
            jsonrequest += ',{"id":' + element.id + ',"sharecase":' + element.sharecase + '}';
    })
    jsonrequest += ']}';
	
    var request = CallAjax("ShareCmdProcessPost.php?act=Delete", jsonrequest , "json");
	    
    request.done(function (msg, statustext, jqxhr) 
	{  
        $.each( msg, function( index, element ) 
        {                           
            switch(index)
            {
                case 'result':  
                    //    -1 ~ -99 parameter error
                    //    -101,-102 access key error 
                    //    -200 ~ -299 db error
                    if(element == 0)
					{
                        
                    }
                    else if(element < 0 && element > -99)
					{
                        alert(LanguageStr_PublicShare.AjaxMsg10);
                    }   
                    else if(element <= -100 && element > -200)
					{
                        alert(LanguageStr_PublicShare.AjaxMsg11);
                    }        
                    else if(element <= -200 && element > -300)
					{
                        alert(LanguageStr_PublicShare.AjaxMsg12);
                    }   
                    break;               
                case 'Shares':
                    // result : 0 success
                    //          not 0 fail                    
                    $.each(element,function(index, element)
                    {
						if ( ( ObjFileOrDirId == element.id ) && ( ShareCase_Public == element.sharecase ) && ( 0 == element.result ) )
						{
                        	alert(LanguageStr_PublicShare.AjaxMsg15);
							Ajax_ChkInitialStatus();
							Asyc_DeletedPublicShare();
						}
                    });
                    break;
            }
        });
    });
    request.fail(function (jqxhr, textStatus)
	{
        alert(LanguageStr_PublicShare.AjaxMsg16 + jqxhr.responseText);          
    });
}

function Ajax_ModifySharePwd(accesskey,id,sharecase,passwd)
{
    var jsonrequest = 
	'{"id":' + id + 
	',"accesskey":"' + accesskey +  
	'","sharecase":' + sharecase + 
	',"passwd":"' + passwd + 
	'"}';
	
    var request = CallAjax("ShareCmdProcessPost.php?act=ModifyPwd", jsonrequest , "json");
	
    request.done(function (msg, statustext, jqxhr) {  
        alert(jqxhr.responseText);
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
                        
                    }
                    else if(element < 0 && element > -99)
					{
                        alert(LanguageStr_PublicShare.AjaxMsg20);
                    }   
                    else if(element <= -100 && element > -200)
					{
                        alert(LanguageStr_PublicShare.AjaxMsg21);
                    }        
                    else if(element <= -200 && element > -300)
					{
                        alert(LanguageStr_PublicShare.AjaxMsg22);
                    }   
                    break;                
            }
        });
    });
    request.fail(function (jqxhr, textStatus)
	{
        alert(LanguageStr_PublicShare.AjaxMsg23 + jqxhr.responseText);             
    });
}















function Asyc_CreatedPublicShare()
{
	$('.InputChkBxPwd').fadeOut(500);
	$('.H4_PwdTitle').fadeOut(500);
	$('.InputPwd').fadeOut(500);
	$('.H4_CreateBtn').fadeOut(500);
	$('.H4_CancelBtn').fadeOut(500);
	
	
	$('.H4_Link').fadeIn(500);
	//$('.H4_CopyBtn').fadeIn(500);
	$('.H4_DeleteBtn').fadeIn(500);
	$('.H4_Link_Title').fadeIn(500);
}





function Asyc_DeletedPublicShare()
{
	$('.H4_StatusDescription').fadeIn(500);
	$('.H4_StartSetBtn').fadeIn(500);
	
	$('.InputChkBxPwd').fadeOut(500);
	$('.H4_PwdTitle').fadeOut(500);
	$('.InputPwd').fadeOut(500);
	$('.H4_CreateBtn').fadeOut(500);
	$('.H4_CancelBtn').fadeOut(500);
	
	$('.H4_Link').fadeOut(500);
	//$('.H4_CopyBtn').fadeOut(500);
	$('.H4_DeleteBtn').fadeOut(500);
	$('.H4_Link_Title').fadeOut(500);
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