function Ajax_ListFilter(accesskey, companyid, departmentid)
{
    var jsonrequest = 
	'{"AccessKey":"' + accesskey + 
	'","company":"' + companyid + 
	'","department":"' + departmentid + 
	'"}';
    
	if ( true == ShowAjaxDebug )
	{
		alert("Ajax_ListFilter(Send):" + jsonrequest);
	}
	
    var request = CallAjax("/filter/option", jsonrequest , "json");    
    request.done(function (msg, statustext, jqxhr)
	{
		var GetCompanyData = true;
		var GetDepartmentData = true;
		var GetTitleData = true;
		
		//
		// 先處理各個 ComboBox 的顯示機制，底下機制為先判斷 C, 然後 D 然後 T，共三階
		// 判斷邏輯大概是如果第一階為0個，就可確定下一層以後都不會有數值
		// 判斷邏輯大概是如果第一階為1個，就可確定有下一層，且第一階的下拉選單是 Disable
		// 判斷邏輯大概是如果第一階為多個，就可確定有下一層，且第一階的下拉選單是 Enable，等他選擇某個項目，再發 Ajax 傳ID過去
		// 
		
		if ( true == ShowAjaxDebug )
		{
        	alert("Ajax_ListFilter(Rcv):" + jqxhr.responseText);
		}
		
        $.each( msg, function( index, element ) 
        {
            switch(index)
            {
                case 'status':     
                    if(element == 0)
					{
						// 依照傳入的參數判斷此次要抓的是 Company 或 Department 或 Title
						if ( "" == companyid )
						{
							GetCompanyData = true;
							GetDepartmentData = false;
							GetTitleData = false;
							Ajax_Company = [];
						}
						
						if ( ( "" != companyid ) && ( "" == departmentid ) )
						{
							GetCompanyData = false;
							GetDepartmentData = true;
							GetTitleData = false;
							Ajax_Department = [];
						}
						
						if ( ( "" != companyid ) && ( "" != departmentid ) )	
						{
							GetCompanyData = false;
							GetDepartmentData = false;
							GetTitleData = true;
							Ajax_Title = [];
						}
					}
                    else
					{
                        alert(LanguageStr_AccountAssign.AjaxMsg46 + element );
					}
                    break;
					
					
                case 'company':
					if ( true == GetCompanyData )
					{
						$.each(element,function(index, element)
						{
							Ajax_Company[index] = Array(2);
							Ajax_Company[index]['id'] = element.id;
							Ajax_Company[index]['name'] = element.name;
						});
						
						//底下兩個函式順序不得顛倒，因為數值有被改，會判斷錯誤
						
						// 決定 ComboBox 狀態是否顯示、是否 Enable、並且決定是否繼續呼叫 Ajax_ListFilter()
						ComBx_SetStatus_Company();
						
						// 根據項目個數決定下拉選單是否需要加上[列出所有]與[請選擇]
						ComBx_SetItem_Company();
					}
                    break;
					
					
					
                case 'department':
					if ( true == GetDepartmentData )
					{
						$.each(element,function(index, element)
						{
							Ajax_Department[index] = Array(2);
							Ajax_Department[index]['id'] = element.id;
							Ajax_Department[index]['name'] = element.name;
						}); 
						
						//底下兩個函式順序不得顛倒，因為數值有被改，會判斷錯誤
												
						// 決定 ComboBox 狀態是否顯示、是否 Enable、並且決定是否繼續呼叫 Ajax_ListFilter()
						ComBx_SetStatus_Department( companyid );
						
						// 根據項目個數決定下拉選單是否需要加上[列出所有]與[請選擇]
						ComBx_SetItem_Department();
					}
                    break;
					
					
					
                case 'title':
					if ( true == GetTitleData )
					{
						$.each(element,function(index, element)
						{
							Ajax_Title[index] = Array(2);
							Ajax_Title[index]['id'] = element.id;
							Ajax_Title[index]['name'] = element.name;
						}); 
						
						//底下兩個函式順序不得顛倒，因為數值有被改，會判斷錯誤
							
						// 決定 ComboBox 狀態是否顯示、是否 Enable、並且決定是否繼續呼叫 Ajax_ListFilter()
						ComBx_SetStatus_Title( companyid, departmentid );
						
						// 根據項目個數決定下拉選單是否需要加上[列出所有]與[請選擇]
						ComBx_SetItem_Title();
					}
                    break;
            }
        });
		
    });
    request.fail(function (jqxhr, textStatus)
	{
        alert(LanguageStr_AccountAssign.AjaxMsg47 + jqxhr.responseText); 
    });
}






function Ajax_ListFilterUsers(accesskey,companyid,departmentid,titleid)
{
    var jsonrequest = 
	'{"AccessKey":"' + accesskey + 
	'","company":"' + companyid + 
	'","department":"' + departmentid + 
	'","title":"' + titleid + 
	'","order":1,"page":1,"maxcount":10000}';
	
	if ( true == ShowAjaxDebug )
	{
		alert( "Ajax_ListFilterUsers(Send):" + jsonrequest);
	}
	
    var request = CallAjax("/filter/user", jsonrequest , "json");
	
    request.done(function (msg, statustext, jqxhr)
	{
		if ( true == ShowAjaxDebug )
		{
			alert( "Ajax_ListFilterUsers(Rcv):" + jqxhr.responseText);
		}
		
        $.each( msg, function( index, element ) 
        {
            switch(index)
            {
                case 'status':                    
                    if(element == 0)
					{
						$('.Div_AccountList:eq(0)').html( "" );
					}
                    else
					{
        				alert(LanguageStr_AccountAssign.AjaxMsg00 + jqxhr.responseText);
					}
                    break;
					
                case 'user':
					
					var HtmlStr = "";
				    
					// Header Row
					HtmlStr = HtmlStr + '<div class = "Div_AccountListRowHeader">';
						HtmlStr = HtmlStr + '<input class = "Input_ChkBxAccountList SelectAll" type="checkbox" name="AccountSelectAll" />';
						HtmlStr = HtmlStr + '<h5 class="H5_AccountListCheck">' + LanguageStr_AccountAssign.H5_AccountListCheck + '</h5>';
						HtmlStr = HtmlStr + '<h5 class="H5_AccountListAccount">' + LanguageStr_AccountAssign.H5_AccountListAccount  + '(Email)</h5>';
					HtmlStr = HtmlStr + '</div>';
		
                    $.each(element,function(index, element)
                    {
						HtmlStr = HtmlStr + '<div class = "Div_AccountListRow DisableSelect">';
							HtmlStr = HtmlStr + '<input class = "Input_ChkBxAccountList DisableSelect" type="checkbox" name="AccountSelectOne" UserId = "' + element.uid + '"/>';
							HtmlStr = HtmlStr + '<h5 class="H5_AccountListCheck DisableSelect"></h5>';
							HtmlStr = HtmlStr + '<h5 class="H5_AccountListAccount DisableSelect" draggable="true" ondragstart="DragStart(event)">' + element.account + '(' + element.email + ')' + '</h5>';
						HtmlStr = HtmlStr + '</div>';
                    });
						

					$('.Div_AccountList:eq(0)').html( HtmlStr );
					
					SetAccountShare_CheckBoxEvent();
                    break;                
            }
        });
    });
    request.fail(function (jqxhr, textStatus)
	{
        alert(LanguageStr_AccountAssign.AjaxMsg01 + jqxhr.responseText);               
    });
}




// 分享-系統帳號
function Ajax_CreateShareandAssigntoUser(accesskey,id,sharecase,passwd,description,lifetime,lifetype,uids)
{
    var jsonrequest = 
	'{"id":' + id + 
	',"accesskey":"' + accesskey + 
	'","sharecase":' + sharecase +
	',"passwd":"' + passwd + 
	'","description":"' + description + 
	'","lifetime":"' + lifetime + 
	'","lifetype":' + lifetype + 
	',"AddUIDs":[';     
    
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
		alert( "Ajax_CreateShareandAssigntoUser(Send):" + jsonrequest);
	}
	
    var request = CallAjax("ShareCmdProcessPost.php?act=CreateAssignUser", jsonrequest , "json");
	
    request.done(function (msg, statustext, jqxhr) 
	{
		$('.Div_WaitAjax').dialog('close');
		if ( true == ShowAjaxDebug )
		{
       		alert('Ajax_CreateShareandAssigntoUser(Rcv)： ' + jqxhr.responseText);
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
                        alert(LanguageStr_AccountAssign.AjaxMsg10);
                    }   
                    else if(element <= -100 && element > -200)
					{
                        alert(LanguageStr_AccountAssign.AjaxMsg11);
                    }
                    else if(element <= -200 && element > -300)
					{
                        alert(LanguageStr_AccountAssign.AjaxMsg12);
                    }     
                    else if(element <= -300 && element > -400)
					{
                        alert(LanguageStr_AccountAssign.AjaxMsg13);
                    }
                    else
					{
                        alert(LanguageStr_AccountAssign.AjaxMsg14 + element);
                    }
                    break;
                case 'idObject' :
                    //alert(element);
                    break;
                case 'url':
                    //alert(element);
                    break;
                case 'AddUIDs':
                    
					var HtmlStr = LanguageStr_AccountAssign.AjaxMsg20 + "\n\n";
					var IsAnyFail = false;
					
                    $.each( element,function(index, element)
                    {
						var Result = "";
						
						HtmlStr = HtmlStr + element.uname;
						if ( 0 == element.result )
						{
							Result = LanguageStr_AccountAssign.AjaxMsg21 + "\n";
						}
						else if ( 1 == element.result )
						{
							Result = LanguageStr_AccountAssign.AjaxMsg22 + "\n";
							IsAnyFail = true;
						}
						else
						{
							Result = LanguageStr_AccountAssign.AjaxMsg23 + element.result + " \n";
							IsAnyFail = true;
						}
						HtmlStr = HtmlStr + Result;
                    });
					
					if ( true == IsAnyFail )
					{
						alert( HtmlStr );
					}
					
					Ajax_ListUserGroupofShare( AccessKey, ObjFileOrDirId );
					
            }
        });
    });
    request.fail(function (jqxhr, textStatus) 
	{
		$('.Div_WaitAjax').dialog('close');
        alert(LanguageStr_AccountAssign.AjaxMsg15 + jqxhr.responseText);        
    });
}




// 分享-匯入帳號
function Ajax_CreateShareandAssigntoUserByName(accesskey,id,sharecase,passwd,description,lifetime,lifetype,names)
{
    var jsonrequest = 
	'{"id":' + id + 
	',"accesskey":"' + accesskey +  
	'","sharecase":' + sharecase + 
	',"passwd":"' + passwd + 
	'","description":"' + description + 
	'","lifetime":"' + lifetime + 
	'","lifetype":' + lifetype + 
	',"AddUnames":[';     
	
    $.each(names,function(index,element)
	{
        if(index == 0)
            jsonrequest += '{"uname":"' + element + '"}';
        else
            jsonrequest += ',{"uname":"' + element + '"}';
    })
    jsonrequest += ']}';
	
	if ( true == ShowAjaxDebug )
	{
		alert( "Ajax_CreateShareandAssigntoUserByName(Send):" + jsonrequest);
	}
	
    var request = CallAjax("ShareCmdProcessPost.php?act=CreateAssignUsername", jsonrequest , "json");
    
	
    request.done(function (msg, statustext, jqxhr)
	{
		$('.Div_WaitAjax').dialog('close');
		
		if ( true == ShowAjaxDebug )
		{
			alert('Ajax_CreateShareandAssigntoUserByName(Rcv)： ' + jqxhr.responseText);
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
                        alert(LanguageStr_AccountAssign.AjaxMsg30);
                    }   
                    else if(element <= -100 && element > -200)
					{
                        alert(LanguageStr_AccountAssign.AjaxMsg31);
                    }
                    else if(element <= -200 && element > -300)
					{
                        alert(LanguageStr_AccountAssign.AjaxMsg32);
                    }     
                    else if(element <= -300 && element > -400)
					{
                        alert(LanguageStr_AccountAssign.AjaxMsg33);
                    }
                    else
					{
                        alert(LanguageStr_AccountAssign.AjaxMsg34 + element);
                    }
                    break;
					
					
                case 'idObject' :
                    if ( ObjFileOrDirId != element )
                    {
                    	alert(LanguageStr_AccountAssign.AjaxMsg35);
                    }
                    break;
					
					
                case 'url':
                    //alert(element);
                    break;
					
					
                case 'AddUnames':
				
					var HtmlStr = LanguageStr_AccountAssign.AjaxMsg20 + "： \n\n";
					var Result = "";
					var IsAnyFail = false;
					
                    $.each( element,function(index, element)
                    {
						
						HtmlStr = HtmlStr + element.uname;
						
						if ( 0 == element.result )
						{
							Result = LanguageStr_AccountAssign.AjaxMsg21 + "\n";
						}
						else if ( 1 == element.result )
						{
							Result = LanguageStr_AccountAssign.AjaxMsg22 + "\n";
							IsAnyFail = true;
						}
						else if ( -1 == element.result )
						{
							Result = LanguageStr_AccountAssign.AjaxMsg48 + "\n";
							IsAnyFail = true;
						}
						else
						{
							Result = LanguageStr_AccountAssign.AjaxMsg49 + "\n";
							IsAnyFail = true;
						}
						HtmlStr = HtmlStr + Result;
                    });
					
					
					if ( true == IsAnyFail )
					{
						alert( HtmlStr );
					}
					
					Ajax_ListUserGroupofShare( AccessKey, ObjFileOrDirId );
                    break;
            }
        });
    });
    request.fail(function (jqxhr, textStatus)
	{
		$('.Div_WaitAjax').dialog('close');
        alert(LanguageStr_AccountAssign.AjaxMsg36 + jqxhr.responseText);              
    });
}





// 分享-群組
function Ajax_CreateShareandAssigntoGroups(accesskey,id,sharecase,passwd,description,lifetime,lifetype,gids)
{
    var jsonrequest = 
	'{"id":' + id + 
	',"accesskey":"' + accesskey +  
	'","sharecase":' + sharecase + 
	',"passwd":"' + passwd + 
	'","description":"' + description + 
	'","lifetime":"' + lifetime + 
	'","lifetype":' + lifetype + 
	',"AddGIDs":[';    
	 
    $.each(gids,function(index,element)
	{
        if(index == 0)
            jsonrequest += '{"gid":' + element + '}';
        else
            jsonrequest += ',{"gid":' + element + '}';
    });
	
    jsonrequest += ']}'; 
	 
	if ( true == ShowAjaxDebug )
	{
    	alert( "Ajax_CreateShareandAssigntoGroups(Send):" +  jsonrequest);
	}
	
    var request = CallAjax("ShareCmdProcessPost.php?act=CreateAssignGroup", jsonrequest , "json"); 
	   
    request.done(function (msg, statustext, jqxhr) 
	{
		$('.Div_WaitAjax').dialog('close');
		
		if ( true == ShowAjaxDebug )
		{
        	alert( "Ajax_CreateShareandAssigntoGroups(Rcv):" + jqxhr.responseText);
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
                        alert(LanguageStr_AccountAssign.AjaxMsg40);
                    }   
                    else if(element <= -100 && element > -200)
					{
                        alert(LanguageStr_AccountAssign.AjaxMsg41);
                    }
                    else if(element <= -200 && element > -300)
					{
                        alert(LanguageStr_AccountAssign.AjaxMsg42);
                    }     
                    else if(element <= -300 && element > -400)
					{
                        alert(LanguageStr_AccountAssign.AjaxMsg43);
                    }
                    else
					{
                        alert(LanguageStr_AccountAssign.AjaxMsg44 + element);
                    }
                    break;
					
					
                case 'idObject' :
                    //alert(element);
                    break;
					
					
                case 'url':
                    //alert(element);
                    break;
					
					
                case 'AddGIDs':
                    
					var IsAnyFail = false;
					var HtmlStr = LanguageStr_AccountAssign.AjaxMsg50 + "： \n\n";
                    $.each( element,function(index, element)
                    {
						var Result = "";
						
						HtmlStr = HtmlStr + element.groupname;
						
						if ( 0 == element.result )
						{
							Result = LanguageStr_AccountAssign.AjaxMsg21 + "\n";
						}
						else if ( 1 == element.result )
						{
							Result = LanguageStr_AccountAssign.AjaxMsg22 + "\n";
							IsAnyFail = true;
						}
						else
						{
							Result = LanguageStr_AccountAssign.AjaxMsg23 + "\n";
							IsAnyFail = true;
						}
						HtmlStr = HtmlStr + Result;
                    });
					
					
					if ( true == IsAnyFail )
					{
						alert( HtmlStr );
					}
					
					Ajax_ListUserGroupofShare( AccessKey, ObjFileOrDirId );
                    break;
            }
        });
    });
    request.fail(function (jqxhr, textStatus)
	{      
		$('.Div_WaitAjax').dialog('close');   
        alert(LanguageStr_AccountAssign.AjaxMsg45 + jqxhr.responseText);              
    });
}
