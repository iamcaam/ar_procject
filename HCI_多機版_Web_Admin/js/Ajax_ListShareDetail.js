
function Ajax_ListUserGroupofShare( accesskey, id )
{
    var jsonrequest = 
	'{"id":"' + id + 
	'","accesskey":"' + accesskey + 
	'"}';
	
	if ( true == ShowAjaxDebug )
	{
		alert( "Ajax_ListUserGroupofShare(Send):" + jsonrequest );
	}
	
    var request = CallAjax("ShareCmdProcessPost.php?act=ListUserGroup", jsonrequest , "json");    
    request.done(function (msg, statustext, jqxhr) 
	{
		var HtmlStr = "";
		var ShareCaseStr = "";
		var IsShareCaseExist = false;
		
		if ( true == ShowAjaxDebug )
		{
			alert( "Ajax_ListUserGroupofShare(Rcv):" + jqxhr.responseText );
		}
		
        $.each( msg, function( index, element )
        {
            switch(index)
            {
                case 'result':
                    if(element == 0)
					{
                    }
                    else if(element < 0 && element > -99)
					{
                        alert(LanguageStr_ListShareDetail.AjaxMsg00);
                    }   
                    else if(element <= -100 && element > -200)
					{
                        alert(LanguageStr_ListShareDetail.AjaxMsg01);
                    }
                    else if(element <= -200 && element > -300)
					{
                        alert(LanguageStr_ListShareDetail.AjaxMsg02);
                    }     
                    else if(element <= -300 && element > -400)
					{
                        alert(LanguageStr_ListShareDetail.AjaxMsg03);
                    }
                    else
					{
                        alert(LanguageStr_ListShareDetail.AjaxMsg04 + element);
                    }
                    break;      
					
					      
                case 'Group':
                    $.each(element,function(index, element)
                    {
						IsShareCaseExist = true;
						
						if ( ShareCase_UploadDownloadBrowse == element.sharecase )
						{
							ShareCaseStr = LanguageStr_ListShareDetail.UploadDownload;
						}
						else if ( ShareCase_DownloadBrowse == element.sharecase )
						{
							ShareCaseStr = LanguageStr_ListShareDetail.Download;
						}
						else if ( ShareCase_UploadBrowse == element.sharecase )
						{
							ShareCaseStr = LanguageStr_ListShareDetail.Homework;
						}
						
						HtmlStr = HtmlStr + '<div class = "Div_ShareListRow">';
							HtmlStr = HtmlStr + '<img class = "Img_ShareListPeople" src="img/Group_20.png" alt="Group" />';
							HtmlStr = HtmlStr + '<h4 class="H4_ShareListAccount">' + element.name + '</h4>';
							HtmlStr = HtmlStr + '<h4 class="H4_ShareListMode">' + ShareCaseStr + '</h4>';
							HtmlStr = HtmlStr + '<img class = "Img_ShareListRemove" ShareCase = "' + element.sharecase + '" GroupID = ' + element.id + ' src="img/Remove.png" alt="Remove" />';
							HtmlStr = HtmlStr + '<h4 class="H4_ShareListResult"></h4>';
						HtmlStr = HtmlStr + '</div>';
                    });
                    break;
					
					
                case 'User':
					$.each(element,function(index, element)
					{
						IsShareCaseExist = true;
						
						if ( ShareCase_UploadDownloadBrowse == element.sharecase )
						{
							ShareCaseStr = LanguageStr_ListShareDetail.UploadDownload;
						}
						else if ( ShareCase_DownloadBrowse == element.sharecase )
						{
							ShareCaseStr = LanguageStr_ListShareDetail.Download;
						}
						else if ( ShareCase_UploadBrowse == element.sharecase )
						{
							ShareCaseStr = LanguageStr_ListShareDetail.Homework;
						}
						
						HtmlStr = HtmlStr + '<div class = "Div_ShareListRow">';
							HtmlStr = HtmlStr + '<img class = "Img_ShareListPeople" src="img/User_20.png" alt="User" />';
							HtmlStr = HtmlStr + '<h4 class="H4_ShareListAccount" >' + element.accountname + '</h4>';
							HtmlStr = HtmlStr + '<h4 class="H4_ShareListMode">' + ShareCaseStr + '</h4>';
							HtmlStr = HtmlStr + '<img class = "Img_ShareListRemove" ShareCase = "' + element.sharecase + '" AccountID = "' + element.id + '" src="img/Remove.png" alt="Remove" />';
							HtmlStr = HtmlStr + '<h4 class="H4_ShareListResult"></h4>';
						HtmlStr = HtmlStr + '</div>';
					});
						
					$('#Div_ShareListContain').html( HtmlStr );
					$('#Div_Left_Contain').html( $('#Div_ShareListContain').html() );
					/*	
					if (  NowMenuPage_AccountShare == NowMenuPage )
					{
						$('.Div_ShareListRowHeader').css( "font-size", "0.7em" );
						$('.Div_ShareListRow').css( "font-size", "0.7em" );
						
					}
					else
					{
						$('.Div_ShareListRowHeader').css( "font-size", "1em" );
						$('.Div_ShareListRow').css( "font-size", "1em" );
					}*/
					
					
					RegEvt_RemoveShareMember_Click();
                    break;
            }
			
			if ( false == IsShareCaseExist )
			{
				$('.H4_SharePeriod').text(LanguageStr_ListShareDetail.AjaxMsg05);
			}
			else
			{
				if ( PeriodType_Special == Ajax_PeriodType )
				{
					$('.H4_SharePeriod').text(LanguageStr_ListShareDetail.AjaxMsg06);
				}
				else
				{
					$('.H4_SharePeriod').text(LanguageStr_ListShareDetail.AjaxMsg07 + Ajax_PeriodDate);
				}
			}
        });
    });
    request.fail(function (jqxhr, textStatus)
	{
        alert( LanguageStr_ListShareDetail.AjaxMsg08 + jqxhr.responseText);
    });
}



function Ajax_RemoveUserfromShare( accesskey, id, sharecase, uids )
{
    var jsonrequest = 
	'{"id":' + id + 
	',"accesskey":"' + accesskey + 
	'","sharecase":' + sharecase + 
	',"DelUIDs":[';     
    
	$.each(uids,function(index,element)
	{
        if(index == 0)
            jsonrequest += '{"uid":' + element + '}';
        else
            jsonrequest += ',{"uid":' + element + '}';
    })
    jsonrequest += ']}';
	
	if ( true == ShowAjaxDebug )
	{
		alert( "Ajax_RemoveUserfromShare(Send):" + jsonrequest );
	}
	
    var request = CallAjax("ShareCmdProcessPost.php?act=RemoveUser", jsonrequest , "json");
	
    request.done(function (msg, statustext, jqxhr) 
	{
		if ( true == ShowAjaxDebug )
		{
			alert( "Ajax_RemoveUserfromShare(Rcv):" + jqxhr.responseText );
		}
		
		// 為了讓「分享期間」按鈕可浮現出來
		Ajax_ChkInitialStatus();
		
        $.each( msg, function( index, element ) 
        {                           
            switch(index)
            {
                case 'result':
				
                    if(element == 0)
					{
                        
                    }
                    else if(element < 0 && element > -99)
					{
                        alert(LanguageStr_ListShareDetail.AjaxMsg10);
                    }   
                    else if(element <= -100 && element > -200)
					{
                        alert(LanguageStr_ListShareDetail.AjaxMsg11);
                    }
                    else if(element <= -200 && element > -300)
					{
                        alert(LanguageStr_ListShareDetail.AjaxMsg12);
                    }     
                    else if(element <= -300 && element > -400)
					{
                        alert(LanguageStr_ListShareDetail.AjaxMsg13);
                    }
                    else
					{
                        alert(LanguageStr_ListShareDetail.AjaxMsg14 + element);
                    }
                    break;     
					          
                case 'DelUIDs':
					    
                    $.each( element,function( index, element )
					{
						var ResultStr = "";
						
						if ( 0 == element.result )
						{
							$('.SelectMe').parent('.Div_ShareListRow').children('.H4_ShareListResult').css( "color", "#060" );
							ResultStr = LanguageStr_ListShareDetail.AjaxMsg15;
						}
						else
						{
							$('.SelectMe').parent('.Div_ShareListRow').children('.H4_ShareListResult').css( "color", "#006" );
							ResultStr = LanguageStr_ListShareDetail.AjaxMsg16;
						}
						
						$('.SelectMe').parent('.Div_ShareListRow').children('.H4_ShareListResult').text( ResultStr );
						
						if ( 0 == element.result )
						{
							// Raymond 說不要動畫，太慢，看起來會誤認為是效能不好
							//$('.SelectMe').parent().slideUp( 1000, function() 
							//{
								$('.SelectMe').parent().remove();
							//} );
						}
					});
                    break;
            }
        });
    });
    request.fail(function (jqxhr, textStatus) 
	{
        alert(LanguageStr_ListShareDetail.AjaxMsg17 + jqxhr.responseText);              
    });
}




function Ajax_RemoveGroupfromShare( accesskey, id, sharecase, gids )
{
    var jsonrequest = 
	'{"id":' + id + 
	',"accesskey":"' + accesskey +  
	'","sharecase":' + sharecase + 
	',"DelGIDs":['; 
	    
    $.each(gids,function(index,element)
	{
        if(index == 0)
            jsonrequest += '{"gid":' + element + '}';
        else
            jsonrequest += ',{"gid":' + element + '}';
    })
	
    jsonrequest += ']}';
	
	if ( true == ShowAjaxDebug )
	{
		alert( "Ajax_RemoveUserfromShare(Send):" + jsonrequest );
	}
	
    var request = CallAjax("ShareCmdProcessPost.php?act=RemoveGroup", jsonrequest , "json");   
	 
    request.done(function (msg, statustext, jqxhr)
	{ 
		if ( true == ShowAjaxDebug )
		{
			alert( "Ajax_RemoveUserfromShare(Rcv):" + jqxhr.responseText );
		}
		
		// 為了讓「分享期間」按鈕可浮現出來
		Ajax_ChkInitialStatus();
		 
        $.each( msg, function( index, element ) 
        {                           
            switch(index)
            {
                case 'result':
				
                    if(element == 0)
					{
                        
                    }
                    else if(element < 0 && element > -99)
					{
                        alert(LanguageStr_ListShareDetail.AjaxMsg20);
                    }   
                    else if(element <= -100 && element > -200)
					{
                        alert(LanguageStr_ListShareDetail.AjaxMsg21);
                    }
                    else if(element <= -200 && element > -300)
					{
                        alert(LanguageStr_ListShareDetail.AjaxMsg22);
                    }     
                    else if(element <= -300 && element > -400)
					{
                        alert(LanguageStr_ListShareDetail.AjaxMsg23);
                    }
                    else
					{
                        alert(LanguageStr_ListShareDetail.AjaxMsg24 + element);
                    }
                    break;
					              
                case 'DelGIDs':
                    // result : 0 success
                    //          not 0 fail                    
                    $.each(element,function(index, element)
                    {
						var ResultStr = "";
						
						if ( 0 == element.result )
						{
							$('.SelectMe').parent('.Div_ShareListRow').children('.H4_ShareListResult').css( "color", "#060" );
							ResultStr = LanguageStr_ListShareDetail.AjaxMsg15;
						}
						else
						{
							$('.SelectMe').parent('.Div_ShareListRow').children('.H4_ShareListResult').css( "color", "#006" );
							ResultStr = LanguageStr_ListShareDetail.AjaxMsg16;
						}
						
						$('.SelectMe').parent('.Div_ShareListRow').children('.H4_ShareListResult').text( ResultStr );
						
						if ( 0 == element.result )
						{
							// Raymond 說不要動畫，太慢，看起來會誤認為是效能不好
							//$('.SelectMe').parent().slideUp( 1000, function() 
							//{
								$('.SelectMe').parent().remove();
							//} );
						}
                    });
                    break;
            }
        });
    });
    request.fail(function (jqxhr, textStatus) 
	{
        alert(LanguageStr_ListShareDetail.AjaxMsg25 + jqxhr.responseText);              
    });
}

