

// 註冊 User 頁面的事件
function RegUserPageEvent()
{
	// 按鈕-新增
	$('.H4_BtnCreateUser').click( function(e) 
	{
		IsUserCreatMode = true;
		
		//$('.H4_BtnCreateUser').hide();
		$('.Div_CreateUser').dialog( 'option', 'title', LanguageStr_Account.DialogTitleCreaete );
		$('.Div_CreateUser').dialog( 'open' );
	});
}





// 列出所有使用者
function Ajax_ListUser()
{
    var StrXml = 
		"<acrored>" +
			AccessKeyTag +
			"<order>" + 1 + "</order>" +
			"<page>" + 1 + "</page>" +
			"<maxcount>" + 10000 + "</maxcount>" +
		"</acrored>";

    var request = CallAjax ( "/user/list", StrXml, "xml" );
	
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		Async_Ajax_ListUser( AjaxData );  
    });
	
    request.fail( function( jqXHR, textStatus )
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		OpenDialog_Message( LanguageStr_Account.Ajax00 );
    });
}





// 非同步解析 - 列出所有使用者
function Async_Ajax_ListUser( XmlData )
{
    var Result = -99;
	var Description = "";
	var HtmlStr = "";
	
    // 先確認回傳的 Statu
	Result = $(XmlData).find('status').text();
	Description = $(XmlData).find('description').text();
	
	if ( "0" == Result )
	{
		var User_Quota = "";
		var UserCount = 0;
		
		
		
		$(XmlData).find('user').each(function() 
		{
			if ( 'admin' != $(this).find('account').text() )	// Admin 不能
			{
				UserList[ UserCount ] = [];
				UserList[ UserCount ][ 'User_ID' ] = $(this).find('uid').text();
				UserList[ UserCount ][ 'User_Name' ] = $(this).find('name').text();
				UserList[ UserCount ][ 'User_LastName' ] = $(this).find('lastname').text();
				UserList[ UserCount ][ 'User_Account' ] = $(this).find('account').text();
				UserList[ UserCount ][ 'User_Email' ] = $(this).find('email').text();
				UserList[ UserCount ][ 'User_Level_Val' ] = $(this).find('level').text();
				UserList[ UserCount ][ 'User_Company_ID' ] = $(this).find('company').text();
				UserList[ UserCount ][ 'User_Department_ID' ] = $(this).find('department').text();
				UserList[ UserCount ][ 'User_Title_ID' ] = $(this).find('title').text();
				UserList[ UserCount ][ 'UserCount' ] = UserCount;
				
				User_Quota = $(this).find('quota').text();
				User_Quota = ( User_Quota / 1024 / 1024 / 1024 )
				UserList[ UserCount ][ 'User_Quota_Val' ] = User_Quota;
				UserList[ UserCount ][ 'User_Quota_Str' ] = User_Quota + "GB";
				
				for ( var i = 0; i < PrivilegeCompany.length; i++ )
				{
					if ( PrivilegeCompany[ i ][ 'ID' ] == UserList[ UserCount ][ 'User_Company_ID' ] )
					{
						UserList[ UserCount ][ 'User_Company_Str' ] = PrivilegeCompany[ i ][ 'Name' ];
						break;
					}
					
					if ( PrivilegeCompany.length - 1 == i )
					{
						UserList[ UserCount ][ 'User_Company_Str' ] = LanguageStr_Account.NoData;
					}
				}
				
				
				for ( var i = 0; i < PrivilegeDepartment.length; i++ )
				{
					if ( PrivilegeDepartment[ i ][ 'ID' ] == UserList[ UserCount ][ 'User_Department_ID' ] )
					{
						UserList[ UserCount ][ 'User_Department_Str' ] = PrivilegeDepartment[ i ][ 'Name' ];
						break;
					}
					
					if ( PrivilegeDepartment.length - 1 == i )
					{
						UserList[ UserCount ][ 'User_Department_Str' ] = LanguageStr_Account.NoData;
					}
				}
				
				
				for ( var i = 0; i < PrivilegeTitle.length; i++ )
				{
					if ( PrivilegeTitle[ i ][ 'ID' ] == UserList[ UserCount ][ 'User_Title_ID' ] )
					{
						UserList[ UserCount ][ 'User_Title_Str' ] = PrivilegeTitle[ i ][ 'Name' ];
						break;
					}
					
					if ( PrivilegeTitle.length - 1 == i )
					{
						UserList[ UserCount ][ 'User_Title_Str' ] = LanguageStr_Account.NoData;
					}
				}
				
				
				UserList[ UserCount ][ 'User_Level_Str' ] = PrivilegeLevel[ UserList[ UserCount ][ 'User_Level_Val' ] ];
				
            	HtmlStr += '<div class = "Div_AccountRow" >';
             
//					HtmlStr += '<input class = "UserList_ChkBox AbsoluteLayout SelectAll" type="checkbox" ></input>';
					HtmlStr += '<H4 class = "UserList_UserAccount AbsoluteLayout RowField" >' + UserList[ UserCount ][ 'User_Account' ] + '</H4>';
					HtmlStr += '<H4 class = "UserList_UserName AbsoluteLayout RowField" >' + ( UserList[ UserCount ][ 'User_Name' ] + UserList[ UserCount ][ 'User_LastName' ] ) + '</H4>';
					HtmlStr += '<H4 class = "UserList_UserEmail AbsoluteLayout RowField" >' + UserList[ UserCount ][ 'User_Email' ] + '</H4>';
					HtmlStr += '<H4 class = "UserList_UserPrivilege AbsoluteLayout RowField" >' + UserList[ UserCount ][ 'User_Level_Str' ] + '</H4>';
					HtmlStr += '<H4 class = "UserList_UserCompany AbsoluteLayout RowField" >' + UserList[ UserCount ][ 'User_Company_Str' ] + '</H4>';
					HtmlStr += '<H4 class = "UserList_UserDepartment AbsoluteLayout RowField" >' + UserList[ UserCount ][ 'User_Department_Str' ] + '</H4>';
					HtmlStr += '<H4 class = "UserList_UserTitle AbsoluteLayout RowField" >' + UserList[ UserCount ][ 'User_Title_Str' ] + '</H4>';
					HtmlStr += '<H4 class = "UserList_UserCapacity AbsoluteLayout RowField" >' + UserList[ UserCount ][ 'User_Quota_Str' ] + '</H4>';
					HtmlStr += '<img class = "Img_UserList_EditItem AbsoluteLayout" src = "img/Edit.png" title = LanguageStr_Account.ImgEdit  UserCount = "' + UserCount + '" User_ID = "' + UserList[ UserCount ][ 'User_ID' ] + '" />';
					HtmlStr += '<img class = "Img_UserList_DeleteItem AbsoluteLayout" src = "img/Remove.png" title = LanguageStr_Account.ImgDelete UserCount = "' + UserCount + '" User_ID = "' + UserList[ UserCount ][ 'User_ID' ] + '" />';
					
				HtmlStr += '</div> ';
				
				UserCount++;
			}
			
		});
		
		HtmlStr = HtmlStr + '<h4 class = "h4_List_NoItem">' + LanguageStr_Account.NoUserAtList + '</h4>';
		$('#Div_UserList').html( HtmlStr );
		$('.Div_AccountRow:odd').css( "background-color", "#E1FFFF" );
		$('.Div_AccountRow:even').css( "background-color", "#E1FFF3" );
		$('.Div_AccountRow:eq(0)').css( "background-color", "#9FEDFD" );
		
		if ( 0 < UserCount )
		{
			$('.h4_List_NoItem').hide();
			$('.ChkBoxSelectAll').show();
			RegEvt_User_Btn_Click();
		}
		else
		{
			$('.ChkBoxSelectAll').hide();
		}
		
		// 在資料確定回來之有建立權限方面的下拉選項，但操作者可能更改，所以進USER畫面要重新依照新的資料建立一次。
		Privilege_ComboOption();
	}
	else if ( "67" == Result )
	{
		HtmlStr = HtmlStr + '<h4 class = "h4_List_NoItem">' + LanguageStr_Account.Ajax01 + '</h4>';
		$('#Div_UserList').html( HtmlStr );
		$('.ChkBoxSelectAll').hide();
		OpenDialog_Message( LanguageStr_Account.Ajax02 );
	}
	
	else
	{
        OpenDialog_Message( LanguageStr_Account.Ajax03 + Description );
	}
}




// 註冊刪除事件
function RegEvt_User_Btn_Click()
{
	// Delete 按鈕事件
	$('.Img_UserList_DeleteItem').click( function ()
	{
		var UserIDs = [];
		UserIDs[ 0 ] = $(this).attr( 'User_ID' );
	
		$(this).parent('.Div_AccountRow').addClass( 'SelectedUser' );
		
		// 送出刪除指令
		Ajax_DeleteUser( UserIDs );
	});
	
	
	
	
	// Edit 按鈕事件
	$('.Img_UserList_EditItem').click( function ()
	{
		IsUserCreatMode = false;
		var UserCount = $(this).attr( 'UserCount' );
		SelectedUserID = $(this).attr( 'User_ID' );
		
		SetUserInfoAndCheckInput( UserCount );
		
		$('.Div_CreateUser').dialog('option', 'title', LanguageStr_Account.DialogTitleCreaete );
		$('.Div_CreateUser').dialog( 'open' );
	} );
	
}




// 設定對話框的輸入數值
function SetUserInfoAndCheckInput( UserCount )
{
	// 數值轉入元件中	
	$('.Input_UserAdd_UserAccount').val( UserList[ UserCount ][ 'User_Account' ] );
	$('.Input_UserAdd_UserName').val( UserList[ UserCount ][ 'User_Name' ] );
	$('.Input_UserAdd_UserLastName').val( UserList[ UserCount ][ 'User_LastName' ] );
	$('.Input_UserAdd_UserEmail').val( UserList[ UserCount ][ 'User_Email' ] );
	$('.Input_UserAdd_UserCapacity').val( UserList[ UserCount ][ 'User_Quota_Val' ] );
	
	$('.Input_UserAdd_UserPrivilege option[ value = ' + UserList[ UserCount ][ 'User_Level_Val' ] + ' ] ' ).prop( "selected", "true");
	$('.Input_UserAdd_UserCompany option[ value = ' + UserList[ UserCount ][ 'User_Company_ID' ] + ' ] ' ).prop( "selected", "true");
	$('.Input_UserAdd_UserDepartment option[ value = ' + UserList[ UserCount ][ 'User_Department_ID' ] + ' ] ' ).prop( "selected", "true");
	$('.Input_UserAdd_UserTitle option[ value = ' + UserList[ UserCount ][ 'User_Title_ID' ] + ' ] ' ).prop( "selected", "true");
}




// 建立使用者的結果訊息顯示控制
function SetResultStrUI( StrResult, IsSuccess )
{
	// 結果訊息顯示在對話框上，並且固定時間
	if ( true == IsSuccess )
	{
		$('.H4_UserAdd_Result').css( "color", "#070" );
	}
	else
	{
		$('.H4_UserAdd_Result').css( "color", "#A00" );
	}
	
	$('.H4_UserAdd_Result').text( StrResult );
	$('.H4_UserAdd_Result').show();
}




// 取得對話框的輸入數值
function GetUserInfoAndCheckInput()
{
	// 逐一取得每個輸入值
	NewUserAccount = $('.Input_UserAdd_UserAccount').val();
	if ( "" == NewUserAccount ) 
	{
		SetResultStrUI( LanguageStr_Account.CreateUserMsg01, false );
		return false;
	}
	
	NewUserQuota = $('.Input_UserAdd_UserCapacity').val();
	
	if ( "" == NewUserQuota )
	{
		SetResultStrUI( LanguageStr_Account.CreateUserMsg02, false );
		return false;
	}
	
	if ( !( !isNaN( parseInt( NewUserQuota )) && isFinite( NewUserQuota ) ) ) 
	{
		SetResultStrUI( LanguageStr_Account.CreateUserMsg03, false );
		return false;
	}
	
	if ( parseFloat( NewUserQuota ) != parseInt( NewUserQuota ) )
	{
		SetResultStrUI( LanguageStr_Account.CreateUserMsg03, false );
		return false;
	}
	
	if ( 1 > parseInt( NewUserQuota ) )
	{
		SetResultStrUI( LanguageStr_Account.CreateUserMsg02, false );
		return false;
	}
	
	/*
	NewUserPassword = $('.Input_UserAdd_Password').val();
	if ( "" == NewUserPassword )
	{
		SetResultStrUI( "密碼不得為空白", true );
		return false;
	}*/
	
	NewUserEmail = $('.Input_UserAdd_UserEmail').val();
	if ( "" == NewUserEmail )
	{
		SetResultStrUI( LanguageStr_Account.CreateUserMsg04, false );
		return false;
	}
	
	NewUserLastName = $('.Input_UserAdd_UserLastName').val();
	if ( "" == NewUserLastName )
	{
		SetResultStrUI( LanguageStr_Account.CreateUserMsg05, false );
		return false;
	}
	
	NewUserFirstName = $('.Input_UserAdd_UserName').val();
	if ( "" == NewUserFirstName )
	{
		SetResultStrUI( LanguageStr_Account.CreateUserMsg06, false );
		return false;
	}
	
	NewUserCompany = $('.Input_UserAdd_UserCompany option:selected').val();
	if ( "" == NewUserFirstName )
	{
		SetResultStrUI( LanguageStr_Account.CreateUserMsg07, false );
		return false;
	}
	
	NewUserDepartment = $('.Input_UserAdd_UserDepartment option:selected').val();
	if ( "" == NewUserFirstName )
	{
		SetResultStrUI( LanguageStr_Account.CreateUserMsg08, false );
		return false;
	}
	
	NewUserTitle = $('.Input_UserAdd_UserTitle option:selected').val();
	if ( "" == NewUserFirstName )
	{
		SetResultStrUI( LanguageStr_Account.CreateUserMsg09, false );
		return false;
	}
	
	NewUserLevel = $('.Input_UserAdd_UserPrivilege option:selected').val();
	if ( "" == NewUserFirstName )
	{
		SetResultStrUI( LanguageStr_Account.CreateUserMsg10, false );
		return false;
	}
	
	return true;
}




// 建立 User Ajax
function Ajax_CreateUser()
{
    var StrXml = 
		"<acrored>" +
		AccessKeyTag +
		"<account>" + NewUserAccount + "</account>" +
		"<quota>" + (NewUserQuota*1024*1024*1024) + "</quota>" +
		"<passwd>" + "000000" + "</passwd>" +
		"<lastname>" + NewUserLastName + "</lastname>" +
		"<username>" + NewUserFirstName + "</username>" +
		"<company>" + NewUserCompany + "</company>";
   
   		if ( NewUserDepartment != "-1")
		StrXml += "<department>" + NewUserDepartment + "</department>" ;
		
   		if ( NewUserTitle != "-1")
		StrXml += "<title>" + NewUserTitle + "</title>";
		
   		StrXml += "<level>" + NewUserLevel + "</level>" +
		"<email>" + NewUserEmail + "</email>" +
		"<auth>0</auth>" + 
		"<share>0</share>" +
		"<maxShareCount>0</maxShareCount>" +
		"</acrored>";
	
    var request = CallAjax ( "/user/create", StrXml, "xml" );
	
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		var StrStatus = $(AjaxData).find('status').text();
		
		if ( StrStatus != '0')
		{
			SetResultStrUI( LanguageStr_Account.Ajax04 + StrStatus, false );
			setTimeout( function() { $('.H4_UserAdd_Result').fadeOut( 2000 ) }, 12000 );
		}
		else
		{
			SetResultStrUI( NewUserAccount + LanguageStr_Account.Ajax05, true );
			setTimeout( function() { $('.H4_UserAdd_Result').fadeOut( 2000 ) }, 6000 );
			
			$('.Input_UserAdd_UserName').val( "" );
			$('.Input_UserAdd_UserLastName').val( "" );
			$('.Input_UserAdd_UserEmail').val( "" );
			
			Ajax_ListUser();
		}
    });
	
    request.fail( function( jqXHR, textStatus )
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
        OpenDialog_Message( LanguageStr_Account.Ajax06 );
    });
}





// 編輯 User Ajax
function Ajax_ModifyUser()
{
    var StrXml = 
		"<acrored>" +
		AccessKeyTag +
		"<uid>" + SelectedUserID + "</uid>" +
		"<account>" + NewUserAccount + "</account>" +
		"<quota>" + (NewUserQuota*1024*1024*1024) + "</quota>" +
		"<lastname>" + NewUserLastName + "</lastname>" +
		"<username>" + NewUserFirstName + "</username>" +
		"<email>" + NewUserEmail + "</email>" +
   		"<level>" + NewUserLevel + "</level>" +
		"<company>" + NewUserCompany + "</company>";
   
   		if ( NewUserDepartment != "-1" )
		StrXml += "<department>" + NewUserDepartment + "</department>";
		
   		if ( NewUserTitle != "-1" )
		StrXml += "<title>" + NewUserTitle + "</title></acrored>";
		
		
		
    var request = CallAjax ( "/user/update", StrXml, "xml" );
	
	
	
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		var StrStatus = $(AjaxData).find('status').text();
		
		if ( StrStatus != '0')
		{
			SetResultStrUI( LanguageStr_Account.Ajax07 + StrStatus, true );
			setTimeout( function() { $('.H4_UserAdd_Result').fadeOut( 2000 ) }, 5000 );
		}
		else
		{
			$('.Div_CreateUser').dialog( "close" );
			Ajax_ListUser();
		}
    });
	
    request.fail( function( jqXHR, textStatus )
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
        OpenDialog_Message( LanguageStr_Account.Ajax08 );
    });
}





function Ajax_DeleteUser( UserIDArray ) 
{
    var StrXml = "";
	StrXml += "<acrored>" +	AccessKeyTag;
	for( var i=0; i<UserIDArray.length; i++ )
		StrXml += "<uid>" + UserIDArray[i] + "</uid>";
	StrXml += "</acrored>";
	
	
	
    var request = CallAjax ( "/user/delete", StrXml, "xml" );
	
	
	
	
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		var StrStatus = $(AjaxData).find('status').text();
		
		if ( StrStatus != '0' )
		{
			OpenDialog_Message( LanguageStr_Account.Ajax09 + StrStatus );
		}
		else
		{
			$('.SelectedUser').slideUp( 500, "swing",  function() 
			{
				$('.SelectedUser').remove();
				$('.Div_AccountRow:odd').css( "background-color", "#E1FFFF" );
				$('.Div_AccountRow:even').css( "background-color", "#E1FFF3" );
				$('.Div_AccountRow:eq(0)').css( "background-color", "#9FEDFD" );
				
			} );
		}      
    });
	
    request.fail( function( jqXHR, textStatus )
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
        OpenDialog_Message( LanguageStr_Account.Ajax10 );
    });
}





function Privilege_ComboOption() 
{
	var StrTemp = "";
	
	for ( var i = 0; i < PrivilegeLevel.length; i++ )
	{
		StrTemp += '<option value = "' + i + '" >' + PrivilegeLevel[i] + '</option>';
	}
	$('.Input_UserAdd_UserPrivilege').html( StrTemp );
	
	StrTemp = "";
	for ( var i = 0; i < PrivilegeCompany.length; i++ )
	{
		StrTemp += '<option value = "' + PrivilegeCompany[i]['ID'] + '" >' + PrivilegeCompany[i]['Name'] + '</option>';
	}
	$('.Input_UserAdd_UserCompany').html( StrTemp );
	
	StrTemp = "";
	for ( var i = 0; i < PrivilegeDepartment.length; i++ )
	{
		StrTemp += '<option value = "' + PrivilegeDepartment[i]['ID'] + '" >' + PrivilegeDepartment[i]['Name']+ '</option>';
	}
	StrTemp += '<option value = "-1" selected = "true" >' + LanguageStr_Account.NoData + '</option>';
	$('.Input_UserAdd_UserDepartment').html( StrTemp );
	
	StrTemp = "";
	for ( var i = 0; i < PrivilegeTitle.length; i++ )
	{
		StrTemp += '<option value = "' + PrivilegeTitle[i]['ID'] + '" >' + PrivilegeTitle[i]['Name'] + '</option>';
	}
	StrTemp += '<option value = "-1" selected = "true" >' + LanguageStr_Account.NoData + '</option>';
	$('.Input_UserAdd_UserTitle').html( StrTemp );
}



