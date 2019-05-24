
var Const_ActionType_Add = 0;
var Const_ActionType_Edit = 1;

var ActionType = 0;




// 註冊 Privilege 頁面的事件
function RegPrivilegePageEvent()
{
	// 切換到 Company 畫面
	$('#H4_BtnCompany').click(function(e) 
	{
		$('#Div_CompanyMgr').show();
		$('#Div_DepartmentMgr').hide();
		$('#Div_TitleMgr').hide();
		
		$('#Div_CompanyAction').hide();
		$('#PrivilegeListCompany_BtnAdd').fadeIn(500);
		Ajax_ListCompany();
	});
	
	
	
	
	
	// 切換到 Department 畫面
	$('#H4_BtnDepartment').click(function(e) 
	{
		$('#Div_CompanyMgr').hide();
		$('#Div_DepartmentMgr').show();
		$('#Div_TitleMgr').hide();
		
		$('#Div_DepartmentAction').hide();
		$('#PrivilegeListDepartment_BtnAdd').fadeIn(500);
		Ajax_ListDepartment();
	});
	
	
	
	
	
	// 切換到 Title 畫面
	$('#H4_BtnTitle').click(function(e) 
	{
		$('#Div_CompanyMgr').hide();
		$('#Div_DepartmentMgr').hide();
		$('#Div_TitleMgr').show();
		
		$('#Div_TitleAction').hide();
		$('#PrivilegeListTitle_BtnAdd').fadeIn(500);
		Ajax_ListTitle();
	});
	
	
	
	
	
	////////////////
	// 	Company
	////////////////
	
	// 按鈕-新增
	$('#PrivilegeListCompany_BtnAdd').click(function(e) 
	{
		ShowEditLabel_ForTrue( true );
		
		$('#PrivilegeListCompany_BtnAdd').fadeOut(500);
		$('#Div_CompanyAction').fadeIn(500);
	});
	
	
	
	// 按鈕-取消
	$('#PrivilegeListCompany_BtnCancel').click(function(e) 
	{
		// 輸入畫面取消
		$('#Div_CompanyAction').fadeOut(500);
		$('#Input_Company').val( "" );
		$('#PrivilegeListCompany_BtnAdd').fadeIn(500);
	});
	
	
	
	// 按鈕-確定
	$('#PrivilegeListCompany_BtnOk').click(function(e) 
	{
		var ItemName = $('#Input_Company').val();
		
		if ( "" == ItemName )
		{
			OpenDialog_Message( LanguageStr_Privilege.NoInputName );
			return;
		}
		else
		{
			Ajax_AddCompany( ItemName );
		}
	});
	
	
	
	
	
	////////////////
	// 	Department
	////////////////
	
	// 按鈕-新增
	$('#PrivilegeListDepartment_BtnAdd').click(function(e) 
	{
		ShowEditLabel_ForTrue( true );
		
		// 標記為新增模 式（按下確定按鈕後會需要判斷是新增或修改）
		ActionType = Const_ActionType_Add;
		
		$('#PrivilegeListDepartment_BtnAdd').fadeOut(500);
		$('#Div_DepartmentAction').fadeIn(500);
	});
	
	
	
	// 按鈕-取消
	$('#PrivilegeListDepartment_BtnCancel').click(function(e) 
	{
		// 輸入畫面取消
		$('#Div_DepartmentAction').fadeOut(500);
		$('#Input_Department').val( "" );
		$('#PrivilegeListDepartment_BtnAdd').fadeIn(500);
	});
	
	
	
	// 按鈕-確定
	$('#PrivilegeListDepartment_BtnOk').click(function(e) 
	{
		var ItemName = $('#Input_Department').val();
		
		if ( "" == ItemName )
		{
			OpenDialog_Message( LanguageStr_Privilege.NoInputName );
			return;
		}
		else
		{
			Ajax_AddDepartment( ItemName );
		}
	});
	
	
	
	
	
	////////////////
	// 	Title
	////////////////
	
	// 按鈕-新增
	$('#PrivilegeListTitle_BtnAdd').click(function(e) 
	{
		ShowEditLabel_ForTrue( true );
		
		// 標記為新增模 式（按下確定按鈕後會需要判斷是新增或修改）
		ActionType = Const_ActionType_Add;
		
		$('#PrivilegeListTitle_BtnAdd').fadeOut(500);
		$('#Div_TitleAction').fadeIn(500);
	});
	
	
	
	// 按鈕-取消
	$('#PrivilegeListTitle_BtnCancel').click(function(e) 
	{
		// 輸入畫面取消
		$('#Div_TitleAction').fadeOut(500);
		$('#Input_Title').val( "" );
		$('#PrivilegeListTitle_BtnAdd').fadeIn(500);
	});
	
	
	
	// 按鈕-確定
	$('#PrivilegeListTitle_BtnOk').click(function(e) 
	{
		var ItemName = $('#Input_Title').val();
			
		if ( "" == ItemName )
		{
			OpenDialog_Message( LanguageStr_Privilege.NoInputName );
			return;
		}
		else
		{
			Ajax_AddTitle( ItemName );
		}
	});
}





//////////////////////////
// 	Company Ajax 取資料	//
//////////////////////////


// 顯示所有 Company
function Ajax_ListCompany()
{
    var StrXml = "<acrored>" + AccessKeyTag + "</acrored>";
	
	
	
    var request = CallAjax ( "/company/list", StrXml, "xml" );
	
	
	
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		// 網站初始化時有五項資料要載入，取回的話要記錄在 Counter
		ChkIfDataReadFinish_Counter++;
		
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		Async_Ajax_ListCompany( AjaxData );
    });
	
    request.fail( function( jqXHR, textStatus )
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
        OpenDialog_Message( LanguageStr_Privilege.Ajax01 + "C" );
    });
}





// 非同步解析 顯示所有 Company
function Async_Ajax_ListCompany( XmlData )
{
    var Result = -99;
	var Description = "";
	var Counter = 0;
	
    // 先確認回傳的 Status
	Result = $(XmlData).find('status').text();
	Description = $(XmlData).find('description').text();
	PrivilegeCompany = [];
	
	// 如果成功才解析
    if( Result == 0 )
	{
		$(XmlData).find('company').each(function() 
		{
			PrivilegeCompany[ Counter ] = [];
			PrivilegeCompany[ Counter ][ 'Name' ] = $(this).find('name').text();
			PrivilegeCompany[ Counter ][ 'ID' ] = $(this).find('id').text();
			Counter++;
		});
		
		// 程式一開始讀取資料時不用顯示畫面
		if ( false == StartUp_DataRead )
		{
			ListDataToUI_Company();
		}
	}
	else if ( "67" == Result )
	{
		if ( true == IsRaidCreatedFlag )
		{
			HtmlStr = HtmlStr + '<h4 class = "h4_List_NoItem">' + LanguageStr_Privilege.Ajax01 + "67" + '</h4>';
			$('#Div_UserList').html( HtmlStr );
			$('.ChkBoxSelectAll').hide();
			OpenDialog_Message( LanguageStr_Privilege.Ajax03 );
		}
	}
	else
	{
        OpenDialog_Message( LanguageStr_Privilege.Ajax04 + Description );
	}
}




// 將資料顯示到畫面上
function ListDataToUI_Company()
{
	var HtmlStr = "";
	var Counter = 0;
	
	$.each( PrivilegeCompany, function()
	{
		// 以新資料更新畫面 //
		HtmlStr += '<div class = "ListItemRow" CompanyID = "' + PrivilegeCompany[ Counter ][ 'ID' ] + '">';
			HtmlStr += '<H4 class = "AbsoluteLayout Lbl_PrivilegeItem" > ' + PrivilegeCompany[ Counter ][ 'Name' ] + '</H4>';
			HtmlStr += '<input class = "Input_PrivilegeItem" maxlength="40" type="text" CompanyID = "' + PrivilegeCompany[ Counter ][ 'ID' ] + '" + CompanyName = "' + PrivilegeCompany[ Counter ][ 'Name' ] + '" value = "' + PrivilegeCompany[ Counter ][ 'Name' ] + '">';
			HtmlStr += '<img class = "Img_DeleteItem" src = "img/Remove.png" title = "' + LanguageStr_Privilege.Ajax05 + '" CompanyID = "' + PrivilegeCompany[ Counter ][ 'ID' ] + '" />';
			HtmlStr += '<img class = "Img_EditItem" src = "img/Edit.png" title = "' + LanguageStr_Privilege.Ajax06 + '" CompanyID = "' + PrivilegeCompany[ Counter ][ 'ID' ] + '"  CompanyName = "' + PrivilegeCompany[ Counter ][ 'Name' ] + '" />';
		HtmlStr += '</div>';
		
		Counter++;
	} );
		
	$('#Div_CompanyList').html( HtmlStr );
	
	RegEvt_BtnCompany_Click();
	$('.Input_PrivilegeItem').hide();
}




// 註冊刪除事件
function RegEvt_BtnCompany_Click()
{
	// Delete 按鈕事件
	$('.Img_DeleteItem').click( function ()
	{
		var UserIDs = [];
		UserIDs[ 0 ] = $(this).attr( 'CompanyID' );
		
		$(this).parent('.ListItemRow').slideUp( 500, "swing",  function() 
		{
			$(this).parent('.ListItemRow').remove();
			
			// 送出刪除指令
			Ajax_DeleteCompany( UserIDs );
		} );
		
		ShowEditLabel_ForTrue( true );
	});
	
	// Edit 按鈕事件
	$('.Img_EditItem').click( function ()
	{
		ShowEditLabel_ForTrue( true );
		$(this).parent().children('.Lbl_PrivilegeItem').addClass("LastEdit_Label");
		$(this).parent().children('.Input_PrivilegeItem').addClass("LastEdit_Input");
		$('#Div_CompanyAction').hide();
		ShowEditLabel_ForTrue( false );
		
		$(this).parent().children('.Input_PrivilegeItem').focus();
	});
	
	
	// 輸入框事件
	$('.Input_PrivilegeItem').keypress( function (event)
	{
		if ( event.which == 13 || event.keyCode == 13 )
		{
			// 處理
			var ItemID = $(this).attr( "CompanyID" );
			var ItemName = $(this).val();
			
			Ajax_EditCompany( ItemID, ItemName );
			ShowEditLabel_ForTrue( true );
		}
	});
	
	$('.Input_PrivilegeItem').keyup( function (event)
	{
		if ( event.which == 27 || event.keyCode == 27 )
		{
			// 取消、還原
			$(this).val( "" );
			$(this).parent().children('.Lbl_PrivilegeItem').fadeIn(300);
			$(this).parent().children('.Input_PrivilegeItem').fadeOut(500);
			ShowEditLabel_ForTrue( true );
		}
	} );
}




function ShowEditLabel_ForTrue( TrueOrFalse )
{
	if ( true == TrueOrFalse )
	{
		$('.LastEdit_Label').fadeIn(300);
		$('.LastEdit_Input').fadeOut(500);
		
		$('.LastEdit_Label').removeClass( "LastEdit_Label" );
		$('.LastEdit_Input').removeClass( "LastEdit_Input" );
	}
	else
	{
		$('.LastEdit_Label').fadeOut(300);
		$('.LastEdit_Input').fadeIn(500);
	}
}




// 刪除 Company
function Ajax_DeleteCompany( CompanyIdArray ) 
{
    var StrXml = "";
		StrXml += "<acrored>" +	AccessKeyTag;
	
	for( var i = 0; i < CompanyIdArray.length; i++ )
	{
		StrXml += "<id>" + CompanyIdArray[i] + "</id>";
	}
	
	StrXml += "</acrored>";
	
	
	
    var request = CallAjax ( "/company/delete", StrXml, "xml" );
	
	
	
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		Async_Ajax_DeleteCompany( AjaxData );
    });
	
    request.fail( function( jqXHR, textStatus )
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
        OpenDialog_Message( LanguageStr_Privilege.Ajax07 );
    });
}






// 非同步解析 刪除 Company
function Async_Ajax_DeleteCompany( XmlData ) 
{
    var Result = -99;
	var Description = "";
	
    // 先確認回傳的 Status
	Result = $(XmlData).find('status').text();
	Description = $(XmlData).find('description').text();
	
	if ( "0" != Result )	
	{
		// 無法刪除可能原因：這個項目有人使用了，無法刪除關連
		if ( Const_TheIdUsedByOtherTable == Result )
		{
			OpenDialog_Message( LanguageStr_Privilege.Ajax08 );
		}
		else
		{
        	OpenDialog_Message( LanguageStr_Privilege.Ajax09 + Description );
		}
                // 刷畫面
		Ajax_ListCompany();
	}
}





// 新增 Company
function Ajax_AddCompany( CompanyItemName ) 
{
	var StrXml = 
		"<acrored>" +
			AccessKeyTag +
			"<name>" + CompanyItemName + "</name>" +
		"</acrored>";
		
    var request = CallAjax ( "/company/create", StrXml, "xml" );
	
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		Async_Ajax_AddCompany( AjaxData );
    });
	
    request.fail( function( jqXHR, textStatus )
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		OpenDialog_Message( LanguageStr_Privilege.Ajax10 );
    });
}





// 非同步解析 新增 Company
function Async_Ajax_AddCompany( XmlData ) 
{
    var Result = -99;
	var Description = "";
	
    // 先確認回傳的 Status
	Result = $(XmlData).find('status').text();
	Description = $(XmlData).find('description').text();
	
	if ( "0" == Result )
	{
		// 成功之後刷畫面刷畫面
		$('#Div_CompanyAction').fadeOut(500);
		$('#PrivilegeListCompany_BtnAdd').fadeIn(500);
		Ajax_ListCompany();
	}
	else
	{
		// 無法新增可能原因：名稱已經存在
		if ( Const_TheNameIsExist == Result )
		{
			OpenDialog_Message( LanguageStr_Privilege.Ajax11 );
		}
		else
		{
        	OpenDialog_Message( LanguageStr_Privilege.Ajax12 + Description );
		}
	}
}




// 修改
function Ajax_EditCompany( CompanyItemID, CompanyItemName )
{
	var StrXml = 
	"<acrored>" +
		AccessKeyTag +
		"<id>" + CompanyItemID + "</id>" +
		"<name>" + CompanyItemName + "</name>" +
	"</acrored>";
	
    var request = CallAjax ( "/company/update", StrXml, "xml" );
	
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		Async_Ajax_EditCompany( AjaxData );
    });
	
    request.fail( function( jqXHR, textStatus )
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		OpenDialog_Message( LanguageStr_Privilege.Ajax13 );
    });
}





// 非同步解析 修改 Company
function Async_Ajax_EditCompany( XmlData ) 
{
    var Result = -99;
	var Description = "";
	
    // 先確認回傳的 Status
	Result = $(XmlData).find('status').text();
	Description = $(XmlData).find('description').text();
	
	if ( "0" == Result )
	{
		// 成功之後刷畫面刷畫面
		$('#Div_CompanyAction').fadeOut(500);
		$('#PrivilegeListCompany_BtnAdd').fadeIn(500);
		Ajax_ListCompany();
	}
	else
	{
		// 無法修改可能原因：名稱已經存在
		if ( Const_EditToExistName == Result )
		{
			OpenDialog_Message( LanguageStr_Privilege.Ajax14 );
		}
		else
		{
        	OpenDialog_Message( LanguageStr_Privilege.Ajax15 + Description );
		}
	}
}






/////////////////
// 	Department
/////////////////


// 顯示所有 Department
function Ajax_ListDepartment()
{
    var StrXml = "<acrored>" + AccessKeyTag + "</acrored>";
    var request = CallAjax ( "/department/list", StrXml, "xml" );
	
	
	
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		// 網站初始化時有五項資料要載入，取回的話要記錄在 Counter
		ChkIfDataReadFinish_Counter++;
		
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		Async_Ajax_ListDepartment( AjaxData );
    });
	
    request.fail( function( jqXHR, textStatus )
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
        OpenDialog_Message( LanguageStr_Privilege.Ajax01 + "D" );
    });
}





// 非同步解析 顯示所有 Department
function Async_Ajax_ListDepartment( XmlData )
{
    var Result = -99;
	var Description = "";
	
    // 先確認回傳的 Status
	Result = $(XmlData).find('status').text();
	Description = $(XmlData).find('description').text();
	
	// 如果成功才解析
    if( Result == 0 )
	{
		var id = "";
		var name = "";
		var Counter = 0;
		PrivilegeDepartment = [];
		
		$(XmlData).find('department').each(function() 
		{
			PrivilegeDepartment[ Counter ] = [];
			PrivilegeDepartment[ Counter ][ 'Name' ] = $(this).find('name').text();
			PrivilegeDepartment[ Counter ][ 'ID' ] = $(this).find('id').text();
			Counter++;
		});
		
		// 程式一開始讀取資料時不用顯示畫面
		if ( false == StartUp_DataRead )
		{
			ListDataToUI_Department();
		}
	}
	else if ( "67" == Result )
	{
		if ( true == IsRaidCreatedFlag )
		{
			HtmlStr = HtmlStr + '<h4 class = "h4_List_NoItem">' + LanguageStr_Privilege.Ajax16 + '</h4>';
			$('#Div_UserList').html( HtmlStr );
			$('.ChkBoxSelectAll').hide();
			OpenDialog_Message( LanguageStr_Privilege.Ajax03 );
		}
	}
	else
	{
        OpenDialog_Message( LanguageStr_Privilege.Ajax17 + Description );
	}
}






// 將資料顯示到畫面上
function ListDataToUI_Department()
{
	var HtmlStr = "";
	var Counter = 0;
		
	$.each( PrivilegeDepartment, function()
	{
		// 以新資料更新畫面 //
		HtmlStr += '<div class = "ListItemRow" DepartmentID = "' + PrivilegeDepartment[ Counter ][ 'ID' ] + '">';
			HtmlStr += '<H4 class = "AbsoluteLayout Lbl_PrivilegeItem" > ' + PrivilegeDepartment[ Counter ][ 'Name' ] + '</H4>';
			HtmlStr += '<input class = "Input_PrivilegeItem" maxlength="40" type="text" DepartmentID = "' + PrivilegeDepartment[ Counter ][ 'ID' ] + '" + DepartmentName = "' + PrivilegeDepartment[ Counter ][ 'Name' ] + '" value = "' + PrivilegeDepartment[ Counter ][ 'Name' ] + '">';
			HtmlStr += '<img class = "Img_DeleteItem" src = "img/Remove.png" title = "' + LanguageStr_Privilege.Ajax05 + '" DepartmentID = "' + PrivilegeDepartment[ Counter ][ 'ID' ] + '" />';
			HtmlStr += '<img class = "Img_EditItem" src = "img/Edit.png" title = "' + LanguageStr_Privilege.Ajax06 + '" DepartmentID = "' + PrivilegeDepartment[ Counter ][ 'ID' ] + '"  DepartmentName = "' + PrivilegeDepartment[ Counter ][ 'Name' ] + '" />';
		HtmlStr += '</div>';
		
		Counter++;
	} );
		
	$('#Div_DepartmentList').html( HtmlStr );
	
	RegEvt_BtnDepartment_Click();
	$('.Input_PrivilegeItem').hide();
}




// 註冊刪除事件
function RegEvt_BtnDepartment_Click()
{
	// Delete 按鈕事件
	$('.Img_DeleteItem').click( function ()
	{
		var UserIDs = [];
		UserIDs[ 0 ] = $(this).attr( 'DepartmentID' );
		
		$(this).parent('.ListItemRow').slideUp( 500, "swing",  function() 
		{
			$(this).parent('.ListItemRow').remove();
			
			// 送出刪除指令
			Ajax_DeleteDepartment( UserIDs );
		} );
		
		ShowEditLabel_ForTrue( true );
	} );
	
	// Edit 按鈕事件
	$('.Img_EditItem').click( function ()
	{
		ShowEditLabel_ForTrue( true );
		$(this).parent().children('.Lbl_PrivilegeItem').addClass("LastEdit_Label");
		$(this).parent().children('.Input_PrivilegeItem').addClass("LastEdit_Input");
		
		$('#Div_DepartmentAction').hide();
		ShowEditLabel_ForTrue( false );
	} );
	
	// 輸入框事件
	$('.Input_PrivilegeItem').keypress( function (event)
	{
		if ( event.which == 13 || event.keyCode == 13 )
		{
			// 處理
			var ItemID = $(this).attr( "DepartmentID" );
			var ItemName = $(this).val();
			
			Ajax_EditDepartment( ItemID, ItemName );
			ShowEditLabel_ForTrue( true );
		}
	});
	
	$('.Input_PrivilegeItem').keyup( function (event)
	{
		if ( event.which == 27 || event.keyCode == 27 )
		{
			// 取消、還原
			$(this).val( "" );
			$(this).parent().children('.Lbl_PrivilegeItem').fadeIn(300);
			$(this).parent().children('.Input_PrivilegeItem').fadeOut(500);
			ShowEditLabel_ForTrue( true );
		}
	} );
}





// 刪除 Department
function Ajax_DeleteDepartment( DepartmentIdArray ) 
{
    var StrXml = "";
		StrXml += "<acrored>" +	AccessKeyTag;
	
	for( var i = 0; i < DepartmentIdArray.length; i++ )
	{
		StrXml += "<id>" + DepartmentIdArray[i] + "</id>";
	}
	
	StrXml += "</acrored>";
	
    var request = CallAjax ( "/department/delete", StrXml, "xml" );
	
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		Async_Ajax_DeleteDepartment( AjaxData );
    });
	
    request.fail( function( jqXHR, textStatus )
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
        OpenDialog_Message( LanguageStr_Privilege.Ajax07 );
    });
}






// 非同步解析 刪除 Department
function Async_Ajax_DeleteDepartment( XmlData ) 
{
    var Result = -99;
	var Description = "";
	
    // 先確認回傳的 Status
	Result = $(XmlData).find('status').text();
	Description = $(XmlData).find('description').text();
	
	if ( "0" != Result )	
	{
            // 無法刪除可能原因：這個項目有人使用了，無法刪除關連
            if ( Const_TheIdUsedByOtherTable == Result )
            {
                    OpenDialog_Message( LanguageStr_Privilege.Ajax08 );
            }
            else
            {
            OpenDialog_Message( LanguageStr_Privilege.Ajax09 + Description );
            }
            // 刷畫面
            Ajax_ListDepartment();

	}
}





// 新增 Department
function Ajax_AddDepartment( DepartmentItemName ) 
{
	var StrXml = 
		"<acrored>" +
			AccessKeyTag +
			"<name>" + DepartmentItemName + "</name>" +
		"</acrored>";
		
    var request = CallAjax ( "/department/create", StrXml, "xml" );
	
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		Async_Ajax_AddDepartment( AjaxData );
    });
	
    request.fail( function( jqXHR, textStatus )
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		OpenDialog_Message( LanguageStr_Privilege.Ajax10 );
    });
}





// 非同步解析 新增 Department
function Async_Ajax_AddDepartment( XmlData ) 
{
    var Result = -99;
	var Description = "";
	
    // 先確認回傳的 Status
	Result = $(XmlData).find('status').text();
	Description = $(XmlData).find('description').text();
	
	if ( "0" == Result )
	{
		// 成功之後刷畫面刷畫面
		$('#Div_DepartmentAction').fadeOut(500);
		$('#PrivilegeListDepartment_BtnAdd').fadeIn(500);
		Ajax_ListDepartment();
	}
	else
	{
		// 無法新增可能原因：名稱已經存在
		if ( Const_TheNameIsExist == Result )
		{
			OpenDialog_Message( LanguageStr_Privilege.Ajax11 );
		}
		else
		{
        	OpenDialog_Message( LanguageStr_Privilege.Ajax12 + Description );
		}
	}
}




// 修改
function Ajax_EditDepartment( DepartmentItemID, DepartmentItemName )
{
	var StrXml = 
	"<acrored>" +
		AccessKeyTag +
		"<id>" + DepartmentItemID + "</id>" +
		"<name>" + DepartmentItemName + "</name>" +
	"</acrored>";
	
    var request = CallAjax ( "/department/update", StrXml, "xml" );
	
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		Async_Ajax_EditDepartment( AjaxData );
    });
	
    request.fail( function( jqXHR, textStatus )
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		OpenDialog_Message( LanguageStr_Privilege.Ajax13 );
    });
}




// 非同步解析 修改 Department
function Async_Ajax_EditDepartment( XmlData ) 
{
    var Result = -99;
	var Description = "";
	
    // 先確認回傳的 Status
	Result = $(XmlData).find('status').text();
	Description = $(XmlData).find('description').text();
	
	if ( "0" == Result )
	{
		// 成功之後刷畫面刷畫面
		$('#Div_DepartmentAction').fadeOut(500);
		$('#PrivilegeListDepartment_BtnAdd').fadeIn(500);
		Ajax_ListDepartment();
	}
	else
	{
		// 無法修改可能原因：名稱已經存在
		if ( Const_EditToExistName == Result )
		{
			OpenDialog_Message( LanguageStr_Privilege.Ajax14 );
		}
		else
		{
        	OpenDialog_Message( LanguageStr_Privilege.Ajax15 + Description );
		}
	}
}






/////////////////
// 		Title
/////////////////


// 顯示所有 Title
function Ajax_ListTitle()
{
    var StrXml = "<acrored>" + AccessKeyTag + "</acrored>";
    var request = CallAjax ( "/title/list", StrXml, "xml" );
	
	
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		// 網站初始化時有五項資料要載入，取回的話要記錄在 Counter
		ChkIfDataReadFinish_Counter++;
		
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		Async_Ajax_ListTitle( AjaxData );
    });
	
    request.fail( function( jqXHR, textStatus )
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
        OpenDialog_Message( LanguageStr_Privilege.Ajax01 + "T" );
    });
}





// 非同步解析 顯示所有 Title
function Async_Ajax_ListTitle( XmlData )
{
    var Result = -99;
	var Description = "";
	
    // 先確認回傳的 Status
	Result = $(XmlData).find('status').text();
	Description = $(XmlData).find('description').text();
	
	// 如果成功才解析
    if( Result == 0 )
	{
		var id = "";
		var name = "";
		var Counter = 0;
		PrivilegeTitle = [];
		
		$(XmlData).find('title').each(function() 
		{
			PrivilegeTitle[ Counter ] = [];
			PrivilegeTitle[ Counter ][ 'Name' ] = $(this).find('name').text();
			PrivilegeTitle[ Counter ][ 'ID' ] = $(this).find('id').text();
			Counter++;
		});
		
		// 程式一開始讀取資料時不用顯示畫面
		if ( false == StartUp_DataRead )
		{
			ListDataToUI_Title();
		}
	}
	else if ( "67" == Result )
	{
		HtmlStr = HtmlStr + '<h4 class = "h4_List_NoItem">' + LanguageStr_Privilege.Ajax18 + '</h4>';
		$('#Div_UserList').html( HtmlStr );
		$('.ChkBoxSelectAll').hide();
		OpenDialog_Message( LanguageStr_Privilege.Ajax03 );
	}
	else
	{
        OpenDialog_Message( LanguageStr_Privilege.Ajax19 + Description );
	}
}





// 將資料顯示到畫面上
function ListDataToUI_Title()
{
	var HtmlStr = "";
	var Counter = 0;
		
	$.each( PrivilegeTitle, function()
	{
		// 以新資料更新畫面 //
		HtmlStr += '<div class = "ListItemRow" TitleID = "' + PrivilegeTitle[ Counter ][ 'ID' ] + '">';
			HtmlStr += '<H4 class = "AbsoluteLayout Lbl_PrivilegeItem" > ' + PrivilegeTitle[ Counter ][ 'Name' ] + '</H4>';
			HtmlStr += '<input class = "Input_PrivilegeItem" maxlength="40" type="text" TitleID = "' + PrivilegeTitle[ Counter ][ 'ID' ] + '" + TitleName = "' + PrivilegeTitle[ Counter ][ 'Name' ] + '" value = "' + PrivilegeTitle[ Counter ][ 'Name' ] + '">';
			HtmlStr += '<img class = "Img_DeleteItem" src = "img/Remove.png" alt = "' + LanguageStr_Privilege.Ajax05 + '" TitleID = "' + PrivilegeTitle[ Counter ][ 'ID' ] + '" />';
			HtmlStr += '<img class = "Img_EditItem" src = "img/Edit.png" alt = "' + LanguageStr_Privilege.Ajax06 + '" TitleID = "' + PrivilegeTitle[ Counter ][ 'ID' ] + '"  TitleName = "' + PrivilegeTitle[ Counter ][ 'Name' ] + '" />';
		HtmlStr += '</div>';
		
		Counter++;
	} );
		
	$('#Div_TitleList').html( HtmlStr );
	
	RegEvt_BtnTitle_Click();
	$('.Input_PrivilegeItem').hide();
}


// 註冊刪除事件
function RegEvt_BtnTitle_Click()
{
	// Delete 按鈕事件
	$('.Img_DeleteItem').click( function ()
	{
		var UserIDs = [];
		UserIDs[ 0 ] = $(this).attr( 'TitleID' );
		
		$(this).parent('.ListItemRow').slideUp( 500, "swing",  function() 
		{
			$(this).parent('.ListItemRow').remove();
			
			// 送出刪除指令
			Ajax_DeleteTitle( UserIDs );
		} );
		
		ShowEditLabel_ForTrue( true );
	} );
	
	// Edit 按鈕事件
	$('.Img_EditItem').click( function ()
	{
		ShowEditLabel_ForTrue( true );
		$(this).parent().children('.Lbl_PrivilegeItem').addClass("LastEdit_Label");
		$(this).parent().children('.Input_PrivilegeItem').addClass("LastEdit_Input");
		
		$('#Div_TitleAction').hide();
		ShowEditLabel_ForTrue( false );
	} );
	
	// 輸入框事件
	$('.Input_PrivilegeItem').keypress( function (event)
	{
		if ( event.which == 13 || event.keyCode == 13 )
		{
			// 處理
			var ItemID = $(this).attr( "TitleID" );
			var ItemName = $(this).val();
			
			Ajax_EditTitle( ItemID, ItemName );
			ShowEditLabel_ForTrue( true );
		}
	});
	
	$('.Input_PrivilegeItem').keyup( function (event)
	{
		if ( event.which == 27 || event.keyCode == 27 )
		{
			// 取消、還原
			$(this).val( "" );
			$(this).parent().children('.Lbl_PrivilegeItem').fadeIn(300);
			$(this).parent().children('.Input_PrivilegeItem').fadeOut(500);
			ShowEditLabel_ForTrue( true );
		}
	} );
}





// 刪除 Title
function Ajax_DeleteTitle( TitleIdArray ) 
{
    var StrXml = "";
		StrXml += "<acrored>" +	AccessKeyTag;
	
	for( var i = 0; i < TitleIdArray.length; i++ )
	{
		StrXml += "<id>" + TitleIdArray[i] + "</id>";
	}
	
	StrXml += "</acrored>";
	
    var request = CallAjax ( "/title/delete", StrXml, "xml" );
	
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		Async_Ajax_DeleteTitle( AjaxData );
    });
	
    request.fail( function( jqXHR, textStatus )
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
        OpenDialog_Message( LanguageStr_Privilege.Ajax07 );
    });
}






// 非同步解析 刪除 Title
function Async_Ajax_DeleteTitle( XmlData ) 
{
    var Result = -99;
	var Description = "";
	
    // 先確認回傳的 Status
	Result = $(XmlData).find('status').text();
	Description = $(XmlData).find('description').text();
	
	if ( "0" != Result )	
	{
            // 無法刪除可能原因：這個項目有人使用了，無法刪除關連
            if ( Const_TheIdUsedByOtherTable == Result )
            {
                    OpenDialog_Message( LanguageStr_Privilege.Ajax08 );
            }
            else
            {
            OpenDialog_Message( LanguageStr_Privilege.Ajax09 + Description );
            }
            // 刷畫面
            Ajax_ListTitle();
	}
}





// 新增 Title
function Ajax_AddTitle( TitleItemName ) 
{
	var StrXml = 
		"<acrored>" +
			AccessKeyTag +
			"<name>" + TitleItemName + "</name>" +
		"</acrored>";
		
    var request = CallAjax ( "/title/create", StrXml, "xml" );
	
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		Async_Ajax_AddTitle( AjaxData );
    });
	
    request.fail( function( jqXHR, textStatus )
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		OpenDialog_Message( LanguageStr_Privilege.Ajax10 );
    });
}





// 非同步解析 新增 Title
function Async_Ajax_AddTitle( XmlData ) 
{
    var Result = -99;
	var Description = "";
	
    // 先確認回傳的 Status
	Result = $(XmlData).find('status').text();
	Description = $(XmlData).find('description').text();
	
	if ( "0" == Result )
	{
		// 成功之後刷畫面刷畫面
		$('#Div_TitleAction').fadeOut(500);
		$('#PrivilegeListTitle_BtnAdd').fadeIn(500);
		Ajax_ListTitle();
	}
	else
	{
		// 無法新增可能原因：名稱已經存在
		if ( Const_TheNameIsExist == Result )
		{
			OpenDialog_Message( LanguageStr_Privilege.Ajax11 );
		}
		else
		{
        	OpenDialog_Message( LanguageStr_Privilege.Ajax12 + Description );
		}
	}
}




// 修改
function Ajax_EditTitle( TitleItemID, TitleItemName )
{
	var StrXml = 
	"<acrored>" +
		AccessKeyTag +
		"<id>" + TitleItemID + "</id>" +
		"<name>" + TitleItemName + "</name>" +
	"</acrored>";
	
    var request = CallAjax ( "/title/update", StrXml, "xml" );
	
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		Async_Ajax_EditTitle( AjaxData );
    });
	
    request.fail( function( jqXHR, textStatus )
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		OpenDialog_Message( LanguageStr_Privilege.Ajax13 );
    });
}




// 非同步解析 修改 Title
function Async_Ajax_EditTitle( XmlData ) 
{
    var Result = -99;
	var Description = "";
	
    // 先確認回傳的 Status
	Result = $(XmlData).find('status').text();
	Description = $(XmlData).find('description').text();
	
	if ( "0" == Result )
	{
		// 成功之後刷畫面刷畫面
		$('#Div_TitleAction').fadeOut(500);
		$('#PrivilegeListTitle_BtnAdd').fadeIn(500);
		Ajax_ListTitle();
	}
	else
	{
		// 無法修改可能原因：名稱已經存在
		if ( Const_EditToExistName == Result )
		{
			OpenDialog_Message( LanguageStr_Privilege.Ajax14 );
		}
		else
		{
        	OpenDialog_Message( LanguageStr_Privilege.Ajax15 + Description );
		}
	}
}













