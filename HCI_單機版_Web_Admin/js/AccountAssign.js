
var AccountSourceType_SysFilter 		= 0;
var AccountSourceType_ByGroup 			= 1;
var AccountSourceType_Import 			= 2;


var AccountSourceType = 0;
var ShowAll = -99;


var Ajax_GetDataCount = 0;
var Ajax_FisrtInside = 0;




// 「指定帳號分享」 跟 「群組管理」 會共用這個函式，註冊帳號過濾模式三按鈕事件與下拉選單事件
function RegEvt_Btn_ShowAccountModeAndComBx_Click()
{
	// 註冊一些按鈕事件以及數值的初始化
	AccountAssign_Initial();
	
	// 選擇帳號的 3種 模式切換
	$('.Btn_SysAccount').click( function ()
	{
		Ajax_ListFilter( AccessKey, "", "" );
		AccountAssign_Initial();			// 註冊一些按鈕事件以及數值的初始化
		
		// 重新撈資料
		Ajax_Initial_ListFilter( AccessKey, "", "" );
	} );
	
	$('.Btn_GroupAccount').click( function ()
	{
		AccountSourceType = AccountSourceType_ByGroup;
		$('#Div_AccountSourceContent').html( $( '.Div_ShowGroupAccount' ).html() );
		RegEvt_Btn_SelectedAccountAssign_Click();		// 帳號分享有三個選擇帳號模式，「系統帳號」、「群組帳號」、「匯入帳號」，這函式註冊三個按鈕的事件
		
		$('.H5_BtnAccountToGroup').click( function ()
		{
			AssignUserToGroup();
		} );
		
		Ajax_ListGroup( AccessKey );
	} );
	
	$('.Btn_ImportAccount').click( function ()
	{
		AccountSourceType = AccountSourceType_Import;
		$('#Div_AccountSourceContent').html( $( '.Div_ShowImportAccount' ).html() );
		RegEvt_Btn_SelectedAccountAssign_Click();		// 帳號分享有三個選擇帳號模式，「系統帳號」、「群組帳號」、「匯入帳號」，這函式註冊三個按鈕的事件
	
		$('.H5_BtnAccountToGroup').click( function ()
		{
			AssignUserToGroup();
		} );
		
		
		$('.TextArea_AccountImport').resize(function(e) 
		{
			if ( 440 < $(this).width() )
			{
				$(this).width( 440 );
			}
		});
	} );
}





// 註冊一些按鈕事件以及數值的初始化
function AccountAssign_Initial()
{
	Ajax_GetDataCount = 0;
	DontGetCompany = false;
	DontGetDepartment = false;
	
	$('.ComboBx_Company').fadeOut(500);
	$('.ComboBx_Department').fadeOut(500);
	$('.ComboBx_Title').fadeOut(500);
	
	// 檔案就沒有模式可以設定，資料夾才有
	if ( IsFolderOrFile_IsFolder == IsFolderOrFile )
	{
		$('.ComboBx_ShareMode').removeAttr( 'disabled' );
	}
	else
	{
		$('.ComboBx_ShareMode option:lt(2)').attr("selected","selected");
		$('.ComboBx_ShareMode').attr( 'disabled', 'disabled' );
	}

	AccountSourceType = AccountSourceType_SysFilter;
	$('#Div_AccountSourceContent').html( $( '.Div_ShowSysAccount' ).html() );
	
	// 兩個頁面有共用兩個按鈕，要分別顯示
	if ( NowMenuPage_AccountShare == NowMenuPage )
	{
		$('.H5_BtnAccountToGroup').hide();
		$('.H5_BtnAccountShare').show();
	}
	else if ( NowMenuPage_GroupManager == NowMenuPage )
	{
		// 不要換畫面，不能把畫面變成
		$('.H5_BtnAccountToGroup').show();
		$('.H5_BtnAccountShare').hide(); 
	
		// 帳號加入群組
		$('.H5_BtnAccountToGroup').click( function ()
		{
			AssignUserToGroup();
		} );
	}
	
	RegEvt_ComBx_Change();
	RegEvt_Btn_SelectedAccountAssign_Click();	// 帳號分享有三個選擇帳號模式，「系統帳號」、「群組帳號」、「匯入帳號」，這函式註冊三個按鈕的事件
}
	

function RegEvt_ComBx_Change()
{	
	$('.ComboBx_Company').fadeOut(500);
	$('.ComboBx_Department').fadeOut(500);
	$('.ComboBx_Title').fadeOut(500);
	
	// 下拉選單	
	$('.ComboBx_Company').change( function ()
	{
		$(".ComboBx_Company option[value='-1']").remove();
		
		$('.Div_AccountList:eq(0)').html( "" );
		if ( ShowAll == $('.ComboBx_Company').val() )
		{
			Ajax_ListFilterUsers( AccessKey, "", "", "" );
		}
		else
		{
			Ajax_ListFilter( AccessKey, $('.ComboBx_Company').val(), "" );
		}
	} );
	

	$('.ComboBx_Department').change( function ()
	{
		$(".ComboBx_Department option[value='-1']").remove();
		
		$('.Div_AccountList:eq(0)').html( "" );
		if ( ShowAll == $('.ComboBx_Department').val() )
		{
			Ajax_ListFilterUsers( AccessKey, $('.ComboBx_Company').val(), "", "" );
		}
		else
		{
			Ajax_ListFilter( AccessKey, $('.ComboBx_Company').val(), $('.ComboBx_Department').val() );
		}
	} );
	
	$('.ComboBx_Title').change( function ()
	{
		$(".ComboBx_Title option[value='-1']").remove();
		
		$('.Div_AccountList:eq(0)').html( "" );
		if ( ShowAll == $('.ComboBx_Title').val() )
		{
			Ajax_ListFilterUsers( AccessKey, $('.ComboBx_Company').val(), $('.ComboBx_Department').val(), "" );
		}
		else
		{
			Ajax_ListFilterUsers( AccessKey, $('.ComboBx_Company').val(), $('.ComboBx_Department').val(), $('.ComboBx_Title').val() );
		}
	} );
}




// 帳號分享有三個選擇帳號模式，「系統帳號」、「群組帳號」、「匯入帳號」，這函式註冊三個按鈕的事件
function RegEvt_Btn_SelectedAccountAssign_Click()
{		
	$('.H5_BtnAccountShare').click( function ()
	{
		var ShareCase = 0;
		var UserIDs = [];
		var GroupIDs = [];
		var Index = 0;
		
		// 確認數值-分享模式
		if ( 0 == $('.ComboBx_ShareMode').val() )
		{
			ShareCase = ShareCase_UploadDownloadBrowse;
		}
		else if ( 1 == $('.ComboBx_ShareMode').val() )
		{
			ShareCase = ShareCase_DownloadBrowse;
		}
		else if ( 2 == $('.ComboBx_ShareMode').val() )
		{
			ShareCase = ShareCase_UploadBrowse;
		}
		
		// 根據目前在哪種模式下，做不同處理
		if ( AccountSourceType_SysFilter == AccountSourceType )
		{
			// 分享給誰
			$.each( $('.Div_AccountList').eq(0).children('.Div_AccountListRow').children('.Input_ChkBxAccountList'), function()
			{                            
				if ( $(this).prop( "checked" ) )
				{
					UserIDs[ Index ] = $(this).attr( "UserId" );
					Index++;
				}
				else
				{
					// 不用做事
				}
			} );
			
			if ( 0 == UserIDs.length )
			{
				alert( LanguageStr_AccountAssign.YouNoSelectAccount );
			}
			else
			{
				$('.Div_WaitAjax').dialog('open');
				
				// 分享系統帳號
				Ajax_CreateShareandAssigntoUser( AccessKey, ObjFileOrDirId, ShareCase, SharePassword, Description, Ajax_PeriodDate, Ajax_PeriodType, UserIDs );
			}
		}
		else if ( AccountSourceType_Import == AccountSourceType )
		{
			var InputStr = $('.TextArea_AccountImport').val();
			var InputAccounts = [];
			InputStr = InputStr.replace("\r\n","");
			var InputAccounts = InputStr.split(";");
				
			if ( 0 == InputAccounts.length )
			{
				alert( LanguageStr_AccountAssign.ImportError );
			}
			else
			{
				$('.Div_WaitAjax').dialog('open');
				
				// 分享匯入帳號
				Ajax_CreateShareandAssigntoUserByName( AccessKey, ObjFileOrDirId, ShareCase, SharePassword, Description, Ajax_PeriodDate, Ajax_PeriodType, InputAccounts );
			}
		}
		else if ( AccountSourceType_ByGroup == AccountSourceType )
		{
			// 分享給誰
			$.each( $('.Input_ChkBxGroupList'), function()
			{
                if ( $(this).prop( "checked" ) )
				{
					GroupIDs[ Index ] = $(this).attr( "GroupID" );
					Index++;
				}
				else
				{
					// 不用做事
				}
			} );
			
			if ( 0 == GroupIDs.length )
			{
				alert( LanguageStr_AccountAssign.YouNoSelectGroup );
			}
			else
			{
				$('.Div_WaitAjax').dialog('open');
				
				// 分享群組帳號
				Ajax_CreateShareandAssigntoGroups( AccessKey, ObjFileOrDirId, ShareCase, SharePassword, Description, Ajax_PeriodDate, Ajax_PeriodType, GroupIDs );
			}
		}
	} );
}





function SetAccountShare_CheckBoxEvent()
{
	$( "input[name='AccountSelectAll']:checkbox" ).click( function ()
	{
		var CheckValue = $( "input[name='AccountSelectAll']:checkbox" ).prop( "checked" )
		
        if ( CheckValue )
		{
       		$( "input[name='AccountSelectOne']:checkbox" ).prop( "checked", CheckValue );
		}
		else
		{
   			$( "input[name='AccountSelectOne']:checkbox" ).prop( "checked", false );
		}
	} );
}



// 根據項目個數決定下拉選單是否需要加上[列出所有]與[請選擇]
function ComBx_SetItem_Company()
{
	// 將所有數並加上 [ 列出所有 ]
	var ArrayLength = Ajax_Company.length;
	
	if ( 1 == ArrayLength )
	{
		$('.ComboBx_Company').empty();
		$('.ComboBx_Company').append( new Option( Ajax_Company[0]['name'], Ajax_Company[0]['id'] ) );
	}
	else
	{
		Ajax_Company[ArrayLength] = Array(2);
		Ajax_Company[ArrayLength]['id'] = ShowAll;
		Ajax_Company[ArrayLength]['name'] = LanguageStr_AccountAssign.ShowAll;
		
		// 將傳回的數值填入 ComboBox
		$('.ComboBx_Company').empty();
		$('.ComboBx_Company').append( new Option( LanguageStr_AccountAssign.PleaseSelect,  "-1" ) );
		
		for ( var i = 0; i < Ajax_Company.length; i++ )
		{
			$('.ComboBx_Company').append( new Option( Ajax_Company[i]['name'],  Ajax_Company[i]['id'] ) );
		}
	}
}



// 根據項目個數決定下拉選單是否需要加上[列出所有]與[請選擇]
function ComBx_SetItem_Department()
{
	// 將所有數並加上 [ 列出所有 ] 
	ArrayLength = Ajax_Department.length;
		
	if ( 1 == ArrayLength )
	{
		$('.ComboBx_Department').empty();
		$('.ComboBx_Department').append( new Option( Ajax_Department[0]['name'], Ajax_Department[0]['id'] ) );
	}
	else
	{
		Ajax_Department[ArrayLength] = Array(2);
		Ajax_Department[ArrayLength]['id'] = ShowAll;	
		Ajax_Department[ArrayLength]['name'] = LanguageStr_AccountAssign.ShowAll;
		
		// 將傳回的數值填入 ComboBox
		$('.ComboBx_Department').empty();
		$('.ComboBx_Department').append( new Option( LanguageStr_AccountAssign.PleaseSelect,  "-1" ) );
		
		for ( var i = 0; i < Ajax_Department.length; i++ )
		{
			$('.ComboBx_Department').append( new Option( Ajax_Department[i]['name'], Ajax_Department[i]['id'] ) );
		}
	}
}




// 根據項目個數決定下拉選單是否需要加上[列出所有]與[請選擇]
function ComBx_SetItem_Title()
{
	// 將所有數並加上 [ 列出所有 ] 
	ArrayLength = Ajax_Title.length;
	
	if ( 1 == ArrayLength )
	{
		$('.ComboBx_Title').empty();
		$('.ComboBx_Title').append( new Option( Ajax_Title[0]['name'], Ajax_Title[0]['id'] ) );
	}
	else
	{
		Ajax_Title[ArrayLength] = Array(2);
		Ajax_Title[ArrayLength]['id'] = ShowAll;
		Ajax_Title[ArrayLength]['name'] = LanguageStr_AccountAssign.ShowAll;
		
		// 將傳回的數值填入 ComboBox
		$('.ComboBx_Title').empty();
		$('.ComboBx_Title').append( new Option( LanguageStr_AccountAssign.PleaseSelect,  "-1" ) );
		
		for ( var i = 0; i < Ajax_Title.length; i++ )
		{
			$('.ComboBx_Title').append( new Option( Ajax_Title[i]['name'], Ajax_Title[i]['id'] ) );
		}
	}
}



// 決定 ComboBox 狀態是否顯示、是否 Enable、並且決定是否繼續呼叫 Ajax_ListFilter()
function ComBx_SetStatus_Company()
{
	if ( 0 == Ajax_Company.length )
	{
		$('.H5_TabAS_ContentTitle').text( LanguageStr_AccountAssign.H5_Tab_ContentTitle );
	}
	else if ( 1 == Ajax_Company.length )
	{
		$('.ComboBx_Company').fadeIn(500);
		$('.ComboBx_Company').attr( 'disabled', 'disabled' );
		Ajax_ListFilter( AccessKey, Ajax_Company[0]['id'], "" );
	}
	else if ( 1 < Ajax_Company.length )
	{
		$('.ComboBx_Company').removeAttr( 'disabled' );
		$('.ComboBx_Company').fadeIn(500);
	}
}



// 決定 ComboBox 狀態是否顯示、是否 Enable、並且決定是否繼續呼叫 Ajax_ListFilter()
function ComBx_SetStatus_Department( CompanyID )
{
	if ( 0 == Ajax_Department.length )		
	{
		Ajax_ListFilterUsers( AccessKey, $('.ComboBx_Company').val(), "", "" );
	}
	else if ( 1 == Ajax_Department.length )
	{
		$('.ComboBx_Department').fadeIn(500);
		$('.ComboBx_Department').attr( 'disabled', 'disabled' );
		Ajax_ListFilter( AccessKey, CompanyID, Ajax_Department[0]['id'] );
	}
	else if ( 1 < Ajax_Department.length )
	{
		$('.ComboBx_Department').fadeIn(500);
		$('.ComboBx_Department').removeAttr( 'disabled' );
	}
}




// 決定 ComboBox 狀態是否顯示、是否 Enable、並且決定是否繼續呼叫 Ajax_ListFilter()
function ComBx_SetStatus_Title( CompanyID, DepartmentID )
{
	if ( 0 == Ajax_Title.length )		
	{
		Ajax_ListFilterUsers( AccessKey, CompanyID, DepartmentID, "" );
	}
	else if ( 1 == Ajax_Title.length )
	{
		$('.ComboBx_Title').fadeIn(500);
		$('.ComboBx_Title').attr( 'disabled', 'disabled' );
		Ajax_ListFilterUsers( AccessKey, CompanyID, DepartmentID, Ajax_Title[0]['id'] );
	}
	else if ( 1 < Ajax_Title.length )
	{
		$('.ComboBx_Title').fadeIn(500);
		$('.ComboBx_Title').removeAttr( 'disabled' );
	}
}