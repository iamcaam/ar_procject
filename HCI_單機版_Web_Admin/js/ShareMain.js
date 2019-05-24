// JavaScript Document

////    常數    ////

// Share Case
var ShareCase_UploadDownloadBrowse 			= 29;							// 上傳 下載 瀏覽
var ShareCase_DownloadBrowse 				= 21;							// 下載、瀏覽（僅下載）
var ShareCase_UploadBrowse 					= 13;							// 上傳 瀏覽（交作業模式）
var ShareCase_Public 						= 53;							// 公開分享

// 分享期間的格式
var PeriodType_Date							= 0;							// 日期格式
var PeriodType_Special						= 1;							// 特殊格式
var PeriodDate_Special						= "-1";							// 特殊格式

// 記錄目前在哪個頁面
var NowMenuPage_SharePeriod					= 0;							// 分享期間
var NowMenuPage_PublicShare					= 1;							// 公開分享
var NowMenuPage_AccountShare				= 2;							// 帳號分享
var NowMenuPage_GroupShare					= 3;							// 群組分享
var NowMenuPage_GroupManager				= 4;							// 群組管理
var NowMenuPage_ShareList					= 5;							// 分享明細

var IsFolderOrFile_IsFolder					= 0;							// Folder
var IsFolderOrFile_IsFile					= 1;							// File

var AjaxFlag_CallByAssignBtn				= 0;							// 按下指定來呼叫 Ajax_AssignRemoveAddMemberFromGroup()
var AjaxFlag_CallByExpand					= 1;							// 展開 Group 來呼叫 Ajax_AssignRemoveAddMemberFromGroup()
var AjaxFlag_DeleteUser						= 2;							// 刪除 Account 來呼叫 Ajax_AssignRemoveAddMemberFromGroup()

var AjaxResult_Success						= 0;							// 成功
var AjaxResult_Exist						= 1;							// 已經存在不用新增
var AjaxResult_SqlError						= -1;							// 資料異常或資料庫連線錯誤



////    變數    ////
var NowMenuPage = 0;
var PeriodType = PeriodType_Special;										// 分享期間的格式，預設為 [ 非日期格式 ]
var PeriodDate = PeriodDate_Special;										// 分享到哪一天
var ShareCase = 53;															// ShareCase，預設 Public Share
var AccessKey = "449a878f8927c7fb3a9a8a2e8a04297d";							// Access Key
var ObjFileOrDirId = 1;														// 被分享的東西的 ID
var SharePassword = "";														// 公開分享密碼
var Description = "";														// 公開分享描述
var FolderOrFileName = "";													// 公開分享描述
var IsFolderOrFile = "";													// 公開分享描述
var ReturnUrl = "";

var DontGetCompany = false;													// 由於底層回應的機制，即便有多個 Company ID，如果選了其中一個來 GetFilter ，回傳時只會回傳一個 Company ID
var DontGetDepartment = false;												// 由於底層回應的機制，即便有多個 Department ID，如果選了其中一個來 GetFilter ，回傳時只會回傳一個  Department ID

var DoRemoving = false;														// 記錄處理中的 Group 的所有 Account 的 Name

// Ajax
var Ajax_PeriodType = 0;													// 分享期間的格式，預設為 [ 非日期格式 ]
var Ajax_PeriodDate = "";													// 分享到哪一天
var Ajax_ShareCase = 0;														// ShareCase，預設 Public Share
var Ajax_ShareLink = "";													// 公開分享Link
var Ajax_SharePassword = "";												// 公開分享密碼
var Ajax_Description = "";													// 公開分享描述
var Ajax_Company = [];														// 記錄此 User 可獲得的 Company 資訊
var Ajax_Department = [];													// 記錄此 User 可獲得的 Department 資訊
var Ajax_Title = [];														// 記錄此 User 可獲得的 Title 資訊
var Ajax_GroupID = [];														// 記錄此 User 目前擁有的 Group ID
var Ajax_GroupName = [];													// 記錄此 User 目前擁有的 Group Name
var Ajax_GroupMemberID = [];												// 記錄處理中的 Group 的所有 Account 的 ID
var Ajax_Result_ID = [];													// 記錄指定處理的ID
var Ajax_Result_Value = [];													// 記錄指定處理的Result
var Ajax_Result_Description = [];											// 記錄指定處理的Description
var Ajax_GroupMemberNam
var AjaxFlag;

var ShowAjaxDebug = false;



function ExitMe()
{
	window.location.assign( ReturnUrl );
}



function InitialSettingAndEvent()
{
	InitialSetting();
	
	// 註冊事件
	RegisterEvents();
}




function InitialSetting()
{
	$( '#Div_TabPublicShare' ).hide();
	$( '#Div_TabAssignAccount' ).hide();
	$( '#Div_TabGroupManager' ).hide();
	$( '#Div_TabShareList' ).hide();
	$( '#Div_TabSharePeriod' ).hide();
	$( '.Div_AccountSourceEdit_BigArea' ).hide();
	
	var StrObjIdentify = "";
	
	if ( IsFolderOrFile_IsFolder == IsFolderOrFile )
	{
		StrObjIdentify = LanguageStr_ShareMain.StrObjIdentify_Dir;
	}
	else
	{
		StrObjIdentify = LanguageStr_ShareMain.StrObjIdentify_File;
	}
		
	$('.DialogTitleMsg').text( FolderOrFileName + StrObjIdentify + LanguageStr_ShareMain.DialogTitleMsg );
	
	
	//setTimeout( "reLocateLayout()", 500 );
	$('.Btn_SharePeriod').hide();
	
	CreateDialog();
	
	// 程式一開始執行一次取得「分享」跟「期間」，之後不用再每次換頁就取資料，但，每次有執行更新「分享」或「期間」的指令成功了，就要重新呼叫此函式
	Ajax_ChkInitialStatus();
}




function CreateDialog()
{
	var StrHtml = "";
    
	StrHtml += '<div class = "Div_DialogBase" >';
        StrHtml += '<div class = "Div_WaitAjax" title = "' + LanguageStr_ShareMain.Div_WaitAjax + '" >';
        StrHtml += '</div>';
    StrHtml += '</div>';
	
	$( '#Div_MainFull' ).append( StrHtml );
	
	$( '.Div_WaitAjax' ).dialog( 
	{
		autoOpen: false,
		resizable: false,
		width:300,
		height:200,
		modal: true,
		
		show: 
		{
			effect: 'fade',
			duration: 200
		},
		hide: 
		{
			effect: 'fade',
			duration: 500
		},
	});
}

function RegisterEvents()
{
	// 最上方六個功能項目按鈕事件
	$('.Btn_PublicShare').click( function ()
	{
		NowMenuPage = NowMenuPage_PublicShare;
		$('#apDiv_TabContent').html( $( '#Div_TabPublicShare' ).html() );
		
		SetPublicShare_UiElement();
		SetPublicShare_UiEvent();
	} );
	
	
	
	$('.Btn_AssignAccount').click( function ()
	{
		NowMenuPage = NowMenuPage_AccountShare;
		Ajax_ListFilter( AccessKey, "", "" );
		$('#apDiv_TabContent').html( $( '#Div_TabAssignAccount' ).html() );
		
		RegEvt_Btn_ShowAccountModeAndComBx_Click();		// 「指定帳號分享」 跟 「群組管理」 會共用這個函式，註冊帳號過濾模式三按鈕事件與下拉選單事件
		$('.Img_RemoveGroup').remove();
		
		$(".TextArea_AccountImport").resizable(
		{
    		resize: function()
			{
				if ( 440 < $(".TextArea_AccountImport").width() )
				{
					$(".TextArea_AccountImport").width( 440 );
				}
			}
		});
		
		Ajax_ListUserGroupofShare( AccessKey, ObjFileOrDirId );
	} );
	
	
	
	
	
	$('.Btn_GroupManage').click( function ()
	{
		NowMenuPage = NowMenuPage_GroupManager;
		Ajax_ListFilter( AccessKey, "", "" );
		$('#apDiv_TabContent').html( $( '#Div_TabGroupManager' ).html() );
		
		Ajax_ListGroup( AccessKey );
		RegEvt_Btn_CreateGroup_Click();
		RegEvt_Btn_ShowAccountModeAndComBx_Click();		// 「指定帳號分享」 跟 「群組管理」 會共用這個函式，註冊帳號過濾模式三按鈕事件與下拉選單事件
	} );
	
	
	
	$('.Btn_ShareList').click( function ()
	{
		NowMenuPage = NowMenuPage_ShareList;
		$('#apDiv_TabContent').html( $( '#Div_TabShareList' ).html() );
		Ajax_ListUserGroupofShare( AccessKey, ObjFileOrDirId );
	} );
	
	
	
	$('.Btn_SharePeriod').click( function ()
	{
		NowMenuPage = NowMenuPage_SharePeriod;
		$('#apDiv_TabContent').html( $( '#Div_TabSharePeriod' ).html() );
		
		SetSharePeriod_UiElement();
		SetSharePeriod_UiEvent();
	} );
}




function Ajax_ChkInitialStatus()
{
	Ajax_GetShareCaseAndPeriod( ObjFileOrDirId, AccessKey );
}














