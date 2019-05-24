
function Ajax_CreateGroup(accesskey,gnames)
{
    var jsonrequest = 
	'{"AccessKey":"' + accesskey + 
	'","addrole":[],"name":[';   
    $.each(gnames,function(index,element)
	{
        if(index == 0)
            jsonrequest += '"' + element + '"';
        else
            jsonrequest += ',"' + element + '"';
    })
	
    jsonrequest += ']}';
	
	if ( true == ShowAjaxDebug )
	{
    	alert( "Ajax_CreateGroup(Send):" +  jsonrequest);
	}
	
    var request = CallAjax("/group/create", jsonrequest , "json");
	
    request.done(function (msg, statustext, jqxhr)
	{
		if ( true == ShowAjaxDebug )
		{
			alert( "Ajax_CreateGroup(Rcv):" + jqxhr.responseText);
		}
		
        $.each( msg, function( index, element ) 
        {            
            switch(index)
            {
                case 'status':
                    if(element == 0)
					{
                        
                    }
                    else if(element < 0 && element > -99)
					{
                        alert(LanguageStr_GroupManager.AjaxMsg00);
                    }   
                    else if(element <= -100 && element > -200)
					{
                        alert(LanguageStr_GroupManager.AjaxMsg01);
                    }
                    else if(element <= -200 && element > -300)
					{
                        alert(LanguageStr_GroupManager.AjaxMsg02);
                    }     
                    else if(element <= -300 && element > -400)
					{
                        alert(LanguageStr_GroupManager.AjaxMsg03);
                    }     
                    else
					{
                        alert(LanguageStr_GroupManager.AjaxMsg04 + ":" + element);
                    }
                    break;
					
                case 'group':
                    $.each(element,function(index, element)
                    {
						// 1 成功 , 0 失敗
						if ( 1 == element.result )
						{
							AsyncAjax_CreateGroup_Success( element.gid, element.name );
						}
						else
						{
						    alert(LanguageStr_GroupManager.AjaxMsg05 + element.result);
						}
                    });  
                    break;
            }
        });
    });
    request.fail(function (jqxhr, textStatus)
	{
        alert(LanguageStr_GroupManager.AjaxMsg06 + jqxhr.responseText);
    });
}





function DeleteGroup(accesskey,gids)
{
    var jsonrequest = '{"AccessKey":"' + accesskey +  '","gid":[';
	
    $.each(gids,function(index,element)
	{
        if(index == 0)
            jsonrequest += '"' + element + '"';
        else
            jsonrequest += ',"' + element + '"';
    })
	
    jsonrequest += ']}';
	
	if ( true == ShowAjaxDebug )
	{
    	alert( "!!!!! DeleteGroup !!!!!(Send):" + jsonrequest);
	}
	
    var request = CallAjax("/group/delete", jsonrequest , "json"); 
	
    request.done(function (msg, statustext, jqxhr)
	{
		if ( true == ShowAjaxDebug )
		{
       		alert( "!!!!! DeleteGroup !!!!!(Rcv):" + jqxhr.responseText);
		}                
        $.each( msg, function( index, element ) 
        {            
            switch(index)
            {
                case 'status':
                    if(element == 0)
					{
                        
                    }
                    else if( 11 == element )
					{
                        alert(LanguageStr_GroupManager.AjaxMsg10);
                    }  
                    else if(element < 0 && element > -99)
					{
                        alert(LanguageStr_GroupManager.AjaxMsg11);
                    }   
                    else if(element <= -100 && element > -200)
					{
                        alert(LanguageStr_GroupManager.AjaxMsg12);
                    }
                    else if(element <= -200 && element > -300)
					{
                        alert(LanguageStr_GroupManager.AjaxMsg13);
                    }     
                    else if(element <= -300 && element > -400)
					{
                        alert(LanguageStr_GroupManager.AjaxMsg14);
                    }
                    else
					{
                        alert(LanguageStr_GroupManager.AjaxMsg15 + element);
                    }
                    break;   
					
                case 'count':
                    //alert(element);
					AsyncAjax_DeleteGroup_Success();
                    break;                
            }
        });
    });
    request.fail(function (jqxhr, textStatus)
	{         
        alert(LanguageStr_GroupManager.AjaxMsg16);                  
    });
}









function Ajax_ListGroup(accesskey)
{
    var jsonrequest = '{"AccessKey":"' + accesskey + '"}';
	
	if ( true == ShowAjaxDebug )
	{
    	alert( "Ajax_ListGroup(Send): " + jsonrequest);
	}
	
    var request = CallAjax("/group/list", jsonrequest , "json");
	
    request.done(function (msg, statustext, jqxhr)
	{
		if ( true == ShowAjaxDebug )
		{
    		alert( "Ajax_ListGroup(Rcv): " + jqxhr.responseText);
		}
		
		var GroupCount = 0;
		
        $.each( msg, function( index, element ) 
        {            
            switch(index)
            {
                case 'status':
                    if(element == 0)
					{
                        Ajax_GroupID = [];
                    }
                    else if(element < 0 && element > -99)
					{
                        alert(LanguageStr_GroupManager.AjaxMsg20);
                    }   
                    else if(element <= -100 && element > -200)
					{
                        alert(LanguageStr_GroupManager.AjaxMsg21);
                    }
                    else if(element <= -200 && element > -300)
					{
                        alert(LanguageStr_GroupManager.AjaxMsg22);
                    }     
                    else if(element <= -300 && element > -400)
					{
                        alert(LanguageStr_GroupManager.AjaxMsg23);
                    }
                    break;     
					
                case 'group':
                    $.each(element,function(index, element)
                    {
                        Ajax_GroupID[ GroupCount ] = element.gid;
						Ajax_GroupName[ GroupCount ] = element.name;
						GroupCount++;
                    }); 
                    break;                
            }
			
			if ( 0 < GroupCount )
			{
				AsyncAjax_ListGroup();
			}
        });
    });
    request.fail(function (jqxhr, textStatus)
	{
		alert(LanguageStr_GroupManager.AjaxMsg24);
    });
}

function UpdateGroup(accesskey,gid,gname)
{
    var jsonrequest = '{"AccessKey":"' + accesskey + '","gid":"' + gid + '","name":"' + gname + '"}';   
    alert(jsonrequest);
    var request = CallAjax("/group/update", jsonrequest , "json");    
    request.done(function (msg, statustext, jqxhr) {        
        alert(jqxhr.responseText);
        $.each( msg, function( index, element ) 
        {            
            switch(index)
            {
                case 'status':                    
                    if(element == 0)
                        alert('Success');
                    else
                        alert('Fail');
                    break;                
            }
        });
    });
    request.fail(function (jqxhr, textStatus) {      
        alert('Fail');                  
    });
}




// 這個指令除了用來增加或刪除某個GROUP裡的帳號，還可以用來列出GROUP裡的帳號
function Ajax_AssignRemoveAddMemberFromGroup( accesskey, gid, adduids, deluids )
{
    var jsonrequest = 
	'{"AccessKey":"' + accesskey + 
	'","gid":"' + gid + '"';
	
	jsonrequest += ',"adduid":[';   
	$.each(adduids,function(index,element)
	{
		if(index == 0)
			jsonrequest += '"' + element + '"';
		else
			jsonrequest += ',"' + element + '"';
	})
	
	jsonrequest += ']';
	
	jsonrequest += ',"deluid":[';
	$.each(deluids,function(index,element)
	{
		if(index == 0)
			jsonrequest += '"' + element + '"';
		else
			jsonrequest += ',"' + element + '"';
	})
	
	jsonrequest += ']}';

	if ( true == ShowAjaxDebug )
	{
    	alert( "Ajax_AssignRemoveAddMemberFromGroup(Send): " + jsonrequest);
	}
	
    var request = CallAjax("/group/assign/member", jsonrequest , "json");
	    
    request.done(function (msg, statustext, jqxhr)
	{
		if ( true == ShowAjaxDebug )
		{
        	alert( "Ajax_AssignRemoveAddMemberFromGroup(Rcv):" + jqxhr.responseText);
		}
		
		Ajax_GroupMemberID = [];
		Ajax_GroupMemberName = [];
		var GroupID = 0;
		//var IsMemberExist = false;
		
        $.each( msg, function( index, element ) 
        {
            switch(index)
            {
                case 'status':
                    if(element == 0)
					{
                        //指令成功
                    }
                    else if(element ==100)
					{
                        //指令成功，欲取回GroupMember，但該Group沒有GroupMember
						Ajax_GroupMemberID = [];
						AsyncAjax_ListGroupMember_ByExpand();
                    }   
                    else if(element < 0 && element > -99)
					{
                        alert(LanguageStr_GroupManager.AjaxMsg40);
                    }   
                    else if(element <= -100 && element > -200)
					{
                        alert(LanguageStr_GroupManager.AjaxMsg41);
                    }
                    else if(element <= -200 && element > -300)
					{
                        alert(LanguageStr_GroupManager.AjaxMsg42);
                    }     
                    else if(element <= -300 && element > -400)
					{
                        alert(LanguageStr_GroupManager.AjaxMsg43);
                    }
					break;
					
					
                case 'description' :
                    break;
					
					
										
					
                case 'delresult' :
					// 刪除 User 的結果，目前的UI不允許一次刪，但之後可以
                    $.each(element,function(index, element)
                    {
						AsyncAjax_DeleteUser_Handle( element.uid, element.result, element.desc );
					} );
                    break;
					
					
					
					
                case 'groupmapping':
				
					
                    $.each(element,function(index, element)
                    {
						//IsMemberExist = true;
						GroupID = element.gid;
						Ajax_GroupMemberID[ index ] = element.member;
                        Ajax_GroupMemberName[ index ] = element.account;// + "( " + element.email + " )";
                    }); 
					
					
					if ( AjaxFlag_CallByAssignBtn == AjaxFlag )
					{
						AsyncAjax_ListGroupMember_ByAssignBtn( GroupID );
						return;
					}
					else  if ( AjaxFlag_CallByExpand == AjaxFlag )
					{
						AsyncAjax_ListGroupMember_ByExpand();
						return;
					}
                    break;
					
						
					
                case 'addresult' :
					
					Ajax_GroupMemberID = [];
					Ajax_Result_ID = [];
					Ajax_Result_Value = [];
					Ajax_Result_Description = [];
					
                    $.each(element,function(index, element)
                    {
						Ajax_Result_ID[ index ] = element.uid;
						Ajax_Result_Value[ index ] = element.result;
						Ajax_Result_Description[ index ] = element.desc;
                    } );
					
                    break;
            }
        });
		
		//if ( false == IsMemberExist )
		//{
		//	AsyncAjax_ListGroupMember_ByExpand();
		//}
    });
    request.fail(function (jqxhr, textStatus)
	{      
        alert(LanguageStr_GroupManager.AjaxMsg44);
    });
}