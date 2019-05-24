// JavaScript Document

// 註冊 SMTP 頁面的事件
function RegSmtpPageEvent()
{
	$('.H4_SMTP_BtnOk').click( function(e) 
	{
		GetDataToUI_SMTP();
    	Ajax_SetSmtp();    
    });
	
	
	$('.H4_SMTP_BtnClear').click( function(e) 
	{
		Ajax_DeleteSmtp();
    });
	
	InitialTimeComboBox();
}




// 取得 SMTP 資訊
function InitialTimeComboBox()
{
	var StrTemp = "";
	
	for ( var i = 0; i < 24; i++ )
	{
		StrTemp += '<option value = "' + i + '" >' + i + '</option>';
	}
	
	$('.ComboBox_SMTP_DailyAlertHour').html( StrTemp );
		
	StrTemp = "";
		
	for ( var i = 0; i < 60; i++ )
	{
		StrTemp += '<option value = "' + i + '" >' + i + '</option>';
	}
	
	$('.ComboBox_SMTP_DailyAlertMin').html( StrTemp );	
}



// 取得 SMTP 資訊
function Ajax_GetSmtp()
{
    var StrJson = '{ ' + 
            '"AccessKey":"' + AccessKey + '"' +
            '}';
			
			
			
    var request = CallAjax ( "/system/email/get", StrJson, "json" );
    
	
	
	// Ajax Success
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
        Async_Ajax_GetSmtp( AjaxData );
    });
   
   
   
	// Ajax Fail
    request.fail( function( jqXHR, textStatus )
	{ 
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		OpenDialog_Message( LanguageStr_SMTP.Ajax01 );
    });
}






// 取得 SMTP 資訊 的非同步函式
function Async_Ajax_GetSmtp( JsonData )
{
    var Result = -99;
	var Counter = 0;
	
	if ( 0 != JsonData[ 'status' ] )
	{
		OpenDialog_Message( LanguageStr_SMTP.Ajax02 );
		return;
	}
	

	SMTP_Info[ 'domain' ] = JsonData[ 'domain' ];
	SMTP_Info[ 'port' ] = JsonData[ 'port' ];
	SMTP_Info[ 'ssl' ] = JsonData[ 'ssl' ];
	SMTP_Info[ 'auth' ] = JsonData[ 'auth' ];
	SMTP_Info[ 'account' ] = JsonData[ 'account' ];
	SMTP_Info[ 'passwd' ] = JsonData[ 'passwd' ];
	SMTP_Info[ 'mail_1' ] = JsonData[ 'mail_1' ];
	SMTP_Info[ 'mail_2' ] = JsonData[ 'mail_2' ];
	SMTP_Info[ 'time' ] = JsonData[ 'time' ];
	
	SetDataToUI_SMTP();
}





// 將資料顯示到畫面上
function SetDataToUI_SMTP()
{
	var HtmlStr = "";
	var Counter = 0;

	$('.Input_SMTP_ServerIP').val( SMTP_Info[ 'domain' ] );
	$('.Input_SMTP_ServerPort').val( SMTP_Info[ 'port' ] );
	$('.Input_SMTP_Account').val( SMTP_Info[ 'account' ] );
	$('.Input_SMTP_Password').val( SMTP_Info[ 'passwd' ] );
	$('.Input_SMTP_Email1').val( SMTP_Info[ 'mail_1' ] );
	$('.Input_SMTP_Email2').val( SMTP_Info[ 'mail_2' ] );
	
	// 每日時間，傳入的是一天的總秒數 ( 86400 ) 當中的第幾秒，比方傳入 25230 就表示7點10分那一秒
	var Seconds = SMTP_Info[ 'time' ];
	var Hour = Math.round( Seconds / 60 / 60 );
	var Min = Math.round( ( Seconds - ( Hour * 60 * 60 ) ) / 60 );
	
	$('.ComboBox_SMTP_DailyAlertHour option[ value = ' + Hour + ']' ).prop( "selected", "true" );
	$('.ComboBox_SMTP_DailyAlertMin option[ value = ' + Min + ']' ).prop( "selected", "true" );
	
	
	if ( "1" == SMTP_Info[ 'auth' ] )
	{
		$('.Input_SMTP_EnableAuth').prop( "checked", "true" );
	}
	else
	{
		$('.Input_SMTP_EnableAuth').prop( "checked", "false" );
	}
	
	if ( "1" == SMTP_Info[ 'ssl' ] )
	{
		$('.Input_SMTP_EnableSecurityConn').prop( "checked", "true" );
	}
	else
	{
		$('.Input_SMTP_EnableSecurityConn').prop( "checked", "false" );
	}
	
	
}





// 設定對話框的輸入數值
function GetDataToUI_SMTP()
{
	// 數值轉入元件中	
	SMTP_Info[ 'domain' ] = $('.Input_SMTP_ServerIP').val( );
	SMTP_Info[ 'port' ] = $('.Input_SMTP_ServerPort').val( );
	SMTP_Info[ 'account' ] = $('.Input_SMTP_Account').val( );
	SMTP_Info[ 'passwd' ] = $('.Input_SMTP_Password').val( );
	SMTP_Info[ 'mail_1' ] = $('.Input_SMTP_Email1').val( );
	SMTP_Info[ 'mail_2' ] = $('.Input_SMTP_Email2').val( );
	
	var Hour = $('.ComboBox_SMTP_DailyAlertHour option:selected').val(); 
	var Min = $('.ComboBox_SMTP_DailyAlertMin option:selected').val(); 
	
	SMTP_Info[ 'time' ] = Hour * 60 * 60 + Min * 60;
	
	if ( true == $('.Input_SMTP_EnableAuth').prop( "checked" ) )
	{
		SMTP_Info[ 'auth' ] = 1;
	}
	else
	{
		SMTP_Info[ 'auth' ] = 0;
	}
	
	if ( true == $('.Input_SMTP_EnableSecurityConn').prop( "checked" ) )
	{
		SMTP_Info[ 'ssl' ] = 1;
	}
	else
	{
		SMTP_Info[ 'ssl' ] = 0;
	}
	
	if ( true == $('.Input_SMTP_SendTestMail').prop( "checked" ) )
	{
		SMTP_Info[ 'test' ] = 1;
	}
	else
	{
		SMTP_Info[ 'test' ] = 0;
	}
}




// 設定 SMTP
function Ajax_SetSmtp()
{
    var StrJson = '{ ' + 
            '"AccessKey":"' + AccessKey + '"' + ',' +
            '"domain":"' + SMTP_Info[ 'domain' ] + '"' + ',' +
            '"port":"' + SMTP_Info[ 'port' ] + '"' + ',' +
            '"account":"' + SMTP_Info[ 'account' ] + '"' + ',' +
            '"passwd":"' + SMTP_Info[ 'passwd' ] + '"' + ',' + 
            '"mail_1":"' + SMTP_Info[ 'mail_1' ] + '"' + ',' + 
            '"mail_2":"' + SMTP_Info[ 'mail_2' ] + '"' + ',' + 
            '"time":"' + SMTP_Info[ 'time' ] + '"' + ',' + 
            '"ssl":"' + SMTP_Info[ 'ssl' ] + '"' + ',' + 
            '"auth":"' + SMTP_Info[ 'auth' ] + '"' + ',' + 
            '"test":"' + SMTP_Info[ 'test' ] + '"' + 
            '}';
    
	
	
    var request = CallAjax ( "/system/email/set", StrJson, "json" );
    
	
	
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		if ( AjaxData['status'] == 0 )
		{
			OpenDialog_Message( LanguageStr_SMTP.Ajax04 );
		}
		else
		{
			OpenDialog_Message( LanguageStr_SMTP.Ajax03 + AjaxData['status'] );
		}
    });

    request.fail( function( jqXHR, textStatus )
	{ 
		OpenDialog_Message( LanguageStr_SMTP.Ajax05 );
    });
}






function Ajax_DeleteSmtp() 
{
    var StrJson = '{ ' + '"AccessKey":"' + AccessKey + '"' + '}';
			
    var request = CallAjax ( "/system/email/delete", StrJson, "json" );
    
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		if ( AjaxData['status'] != 0 )
		{
			OpenDialog_Message( LanguageStr_SMTP.Ajax06 + AjaxData['status'] );
		}
		else
		{
			OpenDialog_Message( LanguageStr_SMTP.Ajax07 );
			Ajax_GetSmtp();
		}
    });

    request.fail( function( jqXHR, textStatus )
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		OpenDialog_Message( LanguageStr_SMTP.Ajax08 );
    });
}






