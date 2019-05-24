// JavaScript Document
var StrTempGroupName = "";



function RegEvt_InitialEvents()
{
	$('.Img_Group_ButtonConfirm').click( function ()
	{
		// 處理 Input 並發送 Ajax 處理
		$('.Img_Group_ButtonConfirm').attr( 'src', 'img/Btn_Confirm_Active.png' );
		InputNameHandle();
	} );
	
	$('.Img_Group_ButtonConfirm').mouseenter( function ()
	{
		$('.Img_Group_ButtonConfirm').attr( 'src', 'img/Btn_Confirm_Hover.png' );
	} );
	
	$('.Img_Group_ButtonConfirm').mouseleave( function ()
	{
		$('.Img_Group_ButtonConfirm').attr( 'src', 'img/Btn_Confirm.png' );
	} );
	
	$('.Input_Group_InputName').keypress( function (event)
	{
		if ( event.which == 13 || event.keyCode == 13 )
		{
			// 處理 Input 並發送 Ajax 處理
			InputNameHandle();
		}
	} );
	
	// ESC 按鈕只能被 KeyUp 抓到，無法在 KeyPress 抓到（限定瀏覽器）
	$('.Input_Group_InputName').keyup( function (event)
	{
		if ( event.which == 27 || event.keyCode == 27 )
		{
			$(this).parent('.Div_GroupRow').remove();
		}
	} );
}





function AssignUserToGroup()
{
	var GroupID = [];
	var AddUserID = [];
	var Count = 0;
	AjaxFlag = AjaxFlag_CallByAssignBtn;
	
	// 檢查左邊的 Group ID
	$.each( $('.Input_ChkBxGroupList'), function()
	{
		if ($(this).prop( "checked") )
		{
			GroupID[ Count ] = $(this).attr( "GroupID" );
			Count++;
		}
	} );
	
	Count = 0;
	
	// 檢查右邊的 Account ID
	$.each( $('.Input_ChkBxAccountList'), function()
	{
		if ( false == $(this).hasClass( "SelectAll" ) )
		{
//			if (  "checked" == $(this).attr( "checked") )
                        if (  $(this).prop( "checked") )
			{
				AddUserID[ Count ] = $(this).attr( "UserId" );
				Count++;
			}
		}
	} );
	
	if ( 0 >= GroupID.length )
	{
		alert( LanguageStr_GroupManager.YouNoSelectGroup );
		return;
	}
	
	if ( 0 >= AddUserID.length )
	{
		alert( LanguageStr_GroupManager.YouNoSelectAccount );
		return;
	}
	
	// 逐一送出 Ajax 做加入動作
	for ( var i = 0; i < GroupID.length; i++ )
	{
		Ajax_AssignRemoveAddMemberFromGroup( AccessKey, GroupID[ i ] , AddUserID, "" );
	}
}
		
		

function Handle_Expand_Collapse( Element )
{
	// 收折狀態。.............'+' 號圖示，準備展開
	if ( "img/Group_Expand.png" == Element.attr( 'src' ) )
	{
		// 確認有沒有展開過，如果有會有 GroupExpandedNow 這個 Class 標記
		if ( Element.parent('.Div_GroupRow').hasClass( "GroupExpandedNow" ) )
		{
			Element.attr( 'src', 'img/Group_Collapse.png' );
			Element.parent('.Div_GroupRow').children( '.Div_GroupMember' ).show();
		}
		
		// 用 Ajax 取得裡面的 Account
		else
		{	
			Element.attr( 'src', 'img/Group_Collapse.png' );
			Element.parent('.Div_GroupRow').addClass( "GroupExpandedNow" );
			Element.parent('.Div_GroupRow').addClass( "AjaxHandling_Group" );
			
			// 處理展開 Ajax
			GroupNode_Expand_CallAjax( Element.attr( 'GroupID' ) );
		}
	}
	// 展開狀態。.............'-' 號圖示，準備收折
	else
	{
		// 處理收折
		Element.parent('.Div_GroupRow').children( '.Div_GroupMember' ).hide();
		Element.attr( 'src', 'img/Group_Expand.png' );
		Element.parent('Div_GroupRow').removeClass( "GroupExpandedNow" );
	}
}




function RegEvt_Btn_CreateGroup_Click()
{
	$('.H4_Btn_CreateGroup').click( function ()
	{
		var StrHtml = "";
		
		// 先清空畫面，把提示字拿走
		if ( 0 == Ajax_GroupName.length )
		{
			$('.Div_GroupList').html( "" );
		}
		
		StrHtml = StrHtml + '<div class = "Div_GroupRow">';
			StrHtml = StrHtml + '<input class = "Input_ChkBxGroupList DisableSelect" type="checkbox" name="GroupSelectOne" GroupID = "0" ></input>';
			StrHtml = StrHtml + '<img class="Img_GroupNode_Create" src="img/Group_Expand.png"/>';
			StrHtml = StrHtml + '<img class="Img_GroupIcon" src="img/Group_20.png"/>';
			StrHtml = StrHtml + '<input class="Input_Group_InputName" type = "text" maxlength="30" required placeholder = "' + LanguageStr_GroupManager.Input_Group_InputName + '"/>';
			StrHtml = StrHtml + '<label class = "LblGroup_LabelName_Create"> </label>';
			StrHtml = StrHtml + '<img class = "Img_Group_ButtonConfirm" src="img/Btn_Confirm.png" />';
			StrHtml = StrHtml + '<img class="Img_RemoveGroup_Create" src="img/Remove.png" title = "' + LanguageStr_GroupManager.Img_RemoveGroup_Create + '"/>';
		StrHtml = StrHtml + '</div>';
		
		$('.Div_GroupList:eq(0)').append( StrHtml );
		
		$('.LblGroup_LabelName_Create').hide();
		$('.Img_RemoveGroup_Create').hide();
		$('.Input_Group_InputName:eq(0)').focus();
		$('.H4_Btn_CreateGroup').css( 'visibility', 'collapse');
		RegEvt_InitialEvents();
	} );
}





function InputNameHandle()
{
	var GroupName = [];
	
	//
	// 判斷是否有INPUT尚未輸入，如果有就自動作命名動作
	//
	if ( "" != $('.Input_Group_InputName').val() )
	{
		StrTempGroupName = $('.Input_Group_InputName').val();
	}
	else
	{
		alert( LanguageStr_GroupManager.Input_Group_InputName );
		return;
	}
	
	GroupName[0] = StrTempGroupName;
	Ajax_CreateGroup( AccessKey, GroupName );
}





// 展開節點
function GroupNode_Expand_CallAjax( GrpID )
{
	AjaxFlag = AjaxFlag_CallByExpand;
	Ajax_AssignRemoveAddMemberFromGroup( AccessKey, GrpID , "", "" );
}





function RegEvt_RemoveUser()
{
	var RemoveUserID = [];
	
	// 移除使用者
	$('.Img_RemoveUser').click( function ()
	{
		AjaxFlag = AjaxFlag_DeleteUser;
		
		if ( false == DoRemoving )
		{
			DoRemoving = true;
			RemoveUserID[0] = $(this).attr( "AccountID" );
			var GroupID = $(this).parent('.Div_GroupMember').parent('.Div_GroupRow').attr( "GroupID" );
			$(this).parent('.Div_GroupMember').addClass("RemovingNow");
			Ajax_AssignRemoveAddMemberFromGroup( AccessKey, GroupID, "", RemoveUserID );
		}
	} );
}







function AsyncAjax_ListGroup( )
{
	var Count = 0;
	var StrHtml = "";
	
	$.each( Ajax_GroupID, function () 
	{
		StrHtml = StrHtml + '<div class = "Div_GroupRow" GroupID = ' + Ajax_GroupID[ Count ] + '>';
			StrHtml = StrHtml + '<input class = "Input_ChkBxGroupList DisableSelect" type="checkbox" name="GroupSelectOne" GroupID = ' + Ajax_GroupID[ Count ] + ' />';
			StrHtml = StrHtml + '<img class="Img_GroupNode" src="img/Group_Expand.png" GroupID = ' + Ajax_GroupID[ Count ] + ' />';
			StrHtml = StrHtml + '<img class="Img_GroupIcon" src="img/Group_20.png" />';
			StrHtml = StrHtml + '<label class = "LblGroup_LabelName" >' + Ajax_GroupName[ Count ] + '</label>';
			
			if ( NowMenuPage_GroupManager == NowMenuPage )
			{
				StrHtml = StrHtml + '<img class="Img_RemoveGroup" src="img/Remove.png" title = "' + LanguageStr_GroupManager.Img_RemoveGroup_Create + '" GroupID = "' + Ajax_GroupID[ Count ] + '"/>';
			}
			
		StrHtml = StrHtml + '</div>';
		Count++;
	} );
	
	if ( NowMenuPage_GroupManager == NowMenuPage )
	{
		$('.Div_GroupList:eq(0)').html( "" );
		$('.Div_GroupList:eq(0)').append( StrHtml );
	}
	else
	{
		$('.Div_GroupListToAssign').html( "" );
		$('.Div_GroupListToAssign').append( StrHtml );
	}
	
	// 點了節點的 + 或 -
	$('.Img_GroupNode').click( function ()
	{
		Handle_Expand_Collapse( $(this) );
	} );
	
	// 移除群組
	$('.Img_RemoveGroup').click( function ()
	{
		var GroupID = [];
		
		if ( false == DoRemoving )
		{
			GroupID[ 0 ] = $(this).attr( "GroupID" );
			
			DoRemoving = true;
			$(this).parent('.Div_GroupRow').addClass("RemovingNow");
			DeleteGroup( AccessKey, GroupID );
		}
	} );
}
	
	
	


function AsyncAjax_DeleteUser_Handle( UserID, Result, Description )
{
	if ( UserID != $('.RemovingNow').attr( "AccountID" ) )
	{
		alert( LanguageStr_GroupManager.AsyncAjax_DeleteUser_Handle00 );
		return;
	}
	
	if ( AjaxResult_Success != Result )
	{
		alert( LanguageStr_GroupManager.AsyncAjax_DeleteUser_Handle01 + Description + LanguageStr_GroupManager.AsyncAjax_DeleteUser_Handle02 + Result );
		return;
	}
	
	$('.RemovingNow').slideUp( 1000, function() 
	{
		$('.RemovingNow').html( "" );
		$('.RemovingNow').removeClass( "RemovingNow" );
		$('.RemovingNow').remove();		// remove 只是從父親元件移走，但並未消消失*/
		DoRemoving = false;
	} );
}
	
	
	


function AsyncAjax_DeleteGroup_Success()
{
	$('.RemovingNow').slideUp( 1000, function() 
	{
		$('.RemovingNow').children( '.Input_ChkBxGroupList' ).prop( "checked", false );
		$('.RemovingNow').removeAttr( "GroupID" );
		$('.RemovingNow').html( "" );
		$('.RemovingNow').removeClass( "RemovingNow" );
		$('.RemovingNow').remove();		// remove 只是從父親元件移走，但並未消失*/
		DoRemoving = false;
	} );
}




function AsyncAjax_CreateGroup_Success( GroupID, GroupName )
{
	var ArrayLen = Ajax_GroupName.length;
	Ajax_GroupName[ ArrayLen ] = GroupName;
	Ajax_GroupID[ ArrayLen ] = GroupID;
	
	// 建立時才需要的元件移除
	$('.Input_Group_InputName').remove();
	$('.Img_Group_ButtonConfirm').remove();
	
	// 群組名稱取代後，改 Class
	$('.LblGroup_LabelName_Create').text( GroupName );
	$('.LblGroup_LabelName_Create').attr( "GroupID", GroupID );
	$('.LblGroup_LabelName_Create').show();
	
	$('.LblGroup_LabelName_Create').parent('.Div_GroupRow').attr( "GroupID", GroupID );
	$('.LblGroup_LabelName_Create').parent('.Div_GroupRow').children('.Input_ChkBxGroupList').attr( "GroupID", GroupID );
	$('.LblGroup_LabelName_Create').parent('.Div_GroupRow').children('.Img_GroupNode_Create').attr( "GroupID", GroupID );
	$('.LblGroup_LabelName_Create').parent('.Div_GroupRow').children('.Img_RemoveGroup_Create').attr( "GroupID", GroupID );
	
	$('.LblGroup_LabelName_Create').addClass( 'LblGroup_LabelName' );
	$('.LblGroup_LabelName_Create').removeClass( 'LblGroup_LabelName_Create' );
	
	// 點了節點的 + 或 -
	$('.Img_GroupNode_Create').click( function ()
	{
		Handle_Expand_Collapse( $(this) );
	} );
	
	// 移除按鈕 Class 控制
	$('.Img_GroupNode_Create').show();
	$('.Img_GroupNode_Create').addClass( 'Img_GroupNode' );
	$('.Img_GroupNode_Create').removeClass( 'Img_GroupNode_Create' );
	
	
	// 移除群組按鈕事件
	$('.Img_RemoveGroup_Create').click( function ()
	{
		var GroupID = [];
		
		if ( false == DoRemoving )
		{
			GroupID[ 0 ] = $(this).attr( "GroupID" );
			
			DoRemoving = true;
			$(this).parent('.Div_GroupRow').addClass("RemovingNow");
			DeleteGroup( AccessKey, GroupID );
		}
	} );
	
	// 移除按鈕 Class 控制
	$('.Img_RemoveGroup_Create').show();
	$('.Img_RemoveGroup_Create').addClass( 'Img_RemoveGroup' );
	$('.Img_RemoveGroup_Create').removeClass( 'Img_RemoveGroup_Create' );
	
	$('.H4_Btn_CreateGroup').css( 'visibility', 'visible' );
	
}


                       


// 新增  [帳號]  到  [群組]
function AsyncAjax_ListGroupMember_ByAssignBtn( GroupID )
{
	var Count_GroupRow = 0;
	var StrHtml = "";
	var ShowMsg = false;
	var StrError = "";
	
	// 找出這次 Ajax 回傳的是哪個 Group Row
	$.each( $('.Div_GroupRow') , function () 
	{
		if ( GroupID == $(this).attr( "GroupID" ) )
		{
			// 先把所有的 Member User 移除，再重新依照回傳封包安插回來
			$(this).children('.Div_GroupMember').remove();
			
			if ( 0 < Ajax_GroupMemberID.length )
			{
				$.each( Ajax_GroupMemberID, function () 
				{
					var StrTemp = ' style = "visibility: hidden" ';
					
					StrHtml = StrHtml + '<div class = "Div_GroupMember" AccountID = ' + Ajax_GroupMemberID[ Count_GroupRow ] + ' >';
					StrHtml = StrHtml + '<img class="Img_GroupIcon" src="img/User_20.png"/>';
					StrHtml = StrHtml + '<label class="LblGroup_LabelName" >' + Ajax_GroupMemberName[ Count_GroupRow ] + '</label>';
					
					if ( NowMenuPage_GroupManager == NowMenuPage )
					{
						var Count_ResultID = 0;
						
						// 比對現在要顯示的 MemberUser 是否是 送去處理的 Account 
						$.each( Ajax_Result_ID, function ()
						{
							if ( Ajax_Result_ID[ Count_ResultID ] == Ajax_GroupMemberID[ Count_GroupRow ] )
							{
								// 如果成功，則顯示 New 圖示
								if ( AjaxResult_Success == Ajax_Result_Value[ Count_ResultID ] )
								{
									StrTemp = ' style = "visibility: visible" ';
								}
								
								// 如果失敗，要有提示訊息
								if ( AjaxResult_SqlError == Ajax_Result_Value[ Count_ResultID ] )
								{
									ShowMsg = true;
									StrError = StrError + Ajax_GroupMemberName[ Count_GroupRow ] + LanguageStr_GroupManager.AsyncAjax_ListGroupMember_ByAssignBtn00 + Ajax_Result_Value[ Count_ResultID ] + LanguageStr_GroupManager.AsyncAjax_ListGroupMember_ByAssignBtn01 + Ajax_Result_Description[ Count_ResultID ];
								}
							}
							
							Count_ResultID++;
						} );
						
						StrHtml = StrHtml + '<img class="Img_RemoveUser" src="img/Remove.png" title = "' + LanguageStr_GroupManager.Img_RemoveGroup_Create + '" AccountID = ' + Ajax_GroupMemberID[ Count_GroupRow ] + ' />'
						StrHtml = StrHtml + '<img class="Img_New"' + StrTemp + ' src="img/New.png" AccountID = "' + Ajax_GroupMemberID[ Count_GroupRow ] + '" />';
					}
						
					StrHtml = StrHtml + '</div>';
					Count_GroupRow++;
				} );
				
				if ( true == ShowMsg )
				{
					alert( StrError );
				}
			}
			else
			{
				StrHtml = StrHtml + '<div class = "Div_GroupMember" ><label class="LblGroup_LabelName" /> ' + LanguageStr_GroupManager.AsyncAjax_ListGroupMember_ByAssignBtn02 + '</label></div>';
			}
		
			$(this).append( StrHtml );
			$(this).children('.Img_GroupNode').attr( "src", "img/Group_Collapse.png" );
			RegEvt_RemoveUser();
		}
	} );
}






function AsyncAjax_ListGroupMember_ByExpand( )
{
	var Count = 0;
	var StrHtml = "";
	
	if ( 0 < Ajax_GroupMemberID.length )
	{
		$.each( Ajax_GroupMemberID, function ()
		{
			StrHtml = StrHtml + '<div class = "Div_GroupMember" AccountID = ' + Ajax_GroupMemberID[ Count ] + '>';
				StrHtml = StrHtml + '<img class="Img_GroupIcon" src="img/User_20.png"/>';
				StrHtml = StrHtml + '<label class="LblGroup_LabelName" />' + Ajax_GroupMemberName[ Count ] + '</label>';
				StrHtml = StrHtml + '<img class="Img_RemoveUser" src="img/Remove.png" title = "' + LanguageStr_GroupManager.Img_RemoveGroup_Create + '" AccountID = "' + Ajax_GroupMemberID[ Count ] + '"/>';
			StrHtml = StrHtml + '</div>';
			Count++;
		} );
	}
	else
	{
		StrHtml = StrHtml + '<div class = "Div_GroupMember" ><label class="LblGroup_LabelName" />' + LanguageStr_GroupManager.AsyncAjax_ListGroupMember_ByExpand00 + '</label></div>';
	}
	
	$('.AjaxHandling_Group').append( StrHtml );
	$('.AjaxHandling_Group').removeClass('AjaxHandling_Group');
	RegEvt_RemoveUser();
}


