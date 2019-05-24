// 設定公開分享的 UI 顯示
function SetPublicShare_UiElement()
{
	$('.H4_StatusDescription').hide();
	$('.H4_StartSetBtn').hide();
	$('.InputChkBxPwd').hide();
	$('.H4_PwdTitle').hide();
	$('.InputPwd').hide();
	$('.H4_CreateBtn').hide();
	$('.H4_CancelBtn').hide();
		
	$('.H4_Link_Title').hide();
	$('.H4_Link').hide();
	$('.H4_CopyBtn').hide();
	$('.H4_DeleteBtn').hide();
	
	// 判斷是否有設定過，來決定怎麼呈現
	if ( "" == Ajax_ShareLink )
	{
		$('.H4_StatusDescription').fadeIn(500);
		$('.H4_StartSetBtn').fadeIn(500);
	}
	else
	{
		$('.H4_Link').fadeIn(500);
		$('.H4_Link').text( Ajax_ShareLink );
		$('.H4_Link').attr( "href", Ajax_ShareLink );
		$('.H4_DeleteBtn').fadeIn(500);
	}
							
}





// 設定公開分享的 UI 事件
function SetPublicShare_UiEvent()
{
	$('.H4_StartSetBtn').click( function ()
	{
		$('.H4_StatusDescription').fadeOut(500);
		$('.H4_StartSetBtn').fadeOut(500);
		
		$('.InputChkBxPwd').fadeIn(500);
		$('.H4_PwdTitle').fadeIn(500);
		$('.H4_CreateBtn').fadeIn(500);
		$('.H4_CancelBtn').fadeIn(500);
	} );
	
	$('.H4_CancelBtn').click( function ()
	{
		$('.H4_StatusDescription').fadeIn(500);
		$('.H4_StartSetBtn').fadeIn(500);
		
		$('.InputChkBxPwd').fadeOut(500);
		$('.H4_PwdTitle').fadeOut(500);
		$('.InputPwd').fadeOut(500);
		$('.H4_CreateBtn').fadeOut(500);
		$('.H4_CancelBtn').fadeOut(500);
	} );
	
	$('.H4_CreateBtn').click( function ()
	{
		if ( true == $('.InputChkBxPwd').prop( "checked" ) )
		{
		    SharePassword = $('.InputPwd').val();

		    if ("" == SharePassword)
		    {
		        alert( LanguageStr_PublicShare.PasswordError );
		        return;
		    }
		}
		else
		{
			SharePassword = "";
		}
		
		$('.InputPwd').val("");
		$('.InputChkBxPwd').prop( "checked", false );
		Ajax_CreateShare( AccessKey, ObjFileOrDirId, ShareCase_Public, SharePassword, Description, Ajax_PeriodDate, Ajax_PeriodType );
	} );
	
	
	$('.InputChkBxPwd').click( function ()
	{
		if ( $('.InputChkBxPwd').prop( "checked" ) )
		{
			$('.InputPwd').fadeIn(500);
		}
		else
		{
			$('.InputPwd').fadeOut(500);
		}
	} );
	
	
	$('.H4_DeleteBtn').click( function ()
	{
		var DeleteShareArray = new Array(1);
		DeleteShareArray[0] = new Array(2);
		DeleteShareArray[0]['id'] = ObjFileOrDirId;
		DeleteShareArray[0]['sharecase'] = ShareCase_Public;
		Ajax_DeleteShare( AccessKey, DeleteShareArray );
	} );
}
