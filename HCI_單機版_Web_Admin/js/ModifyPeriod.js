// JavaScript Document


// 註冊事件
function SetSharePeriod_UiEvent()
{
	// Radio Button 看哪一個被選擇
	$( '#Input_RadioPeriodMode_Date' ).click( function ()
	{	
		PeriodType = PeriodType_Date;
		
		$( '#Input_RadioPeriodMode_DateSelect' ).removeAttr( 'disabled' );
		
		$( '#Input_RadioPeriodMode_Date' ).attr( "MySelect", "true" );
		$( '#Input_RadioPeriodMode_NoLimit' ).attr( "MySelect", "false" );
	} );
	
	
	// Radio Button 看哪一個被選擇
	$( '#Input_RadioPeriodMode_NoLimit' ).click( function ()
	{
		PeriodType = PeriodType_Special;
			
		$( '#Input_RadioPeriodMode_DateSelect' ).attr( 'disabled', 'disabled' );
		
		// 因為 Attr 當中的 checked 屬性不是想像中的控制方式，所以自己設定 Attr 來判斷
		$( '#Input_RadioPeriodMode_Date' ).attr( "MySelect", "false" );
		$( '#Input_RadioPeriodMode_NoLimit' ).attr( "MySelect", "true" );
	} );
	
	
	$( '.H4_OkBtn' ).click( function ()
	{
		// 判斷使用者設定
		if ( "true" == $( '#Input_RadioPeriodMode_NoLimit' ).attr( "MySelect" ) )
		{
			PeriodType = PeriodType_Special;
			PeriodDate = PeriodDate_Special;
			Ajax_ModifyShareDate( AccessKey, ObjFileOrDirId, PeriodDate, PeriodType );
		}
		else
		{
			PeriodType = PeriodType_Date;
			PeriodDate = $( "#Input_RadioPeriodMode_DateSelect" ).val();
			
			if ( "" == PeriodDate )
			{
				alert( LanguageStr_SharePeriod.Input_RadioPeriodMode_DateSelect );
				return;
			}
			
			Ajax_ModifyShareDate( AccessKey, ObjFileOrDirId, PeriodDate, PeriodType );
		}
	} );
	
	
	var opt=
	{
		dateFormat: 'yy-mm-dd',
		showSecond: true,
		timeFormat: 'HH:mm:ss',
		
		// 多國語系分
		dayNames:[LanguageStr_SharePeriod.WDay0,LanguageStr_SharePeriod.WDay1,LanguageStr_SharePeriod.WDay2,LanguageStr_SharePeriod.WDay3,LanguageStr_SharePeriod.WDay4,LanguageStr_SharePeriod.WDay5,LanguageStr_SharePeriod.WDay6],
		dayNamesMin:[LanguageStr_SharePeriod.WD0,LanguageStr_SharePeriod.WD1,LanguageStr_SharePeriod.WD2,LanguageStr_SharePeriod.WD3,LanguageStr_SharePeriod.WD4,LanguageStr_SharePeriod.WD5,LanguageStr_SharePeriod.WD6],
		monthNames:[LanguageStr_SharePeriod.Month01,LanguageStr_SharePeriod.Month02,LanguageStr_SharePeriod.Month03,LanguageStr_SharePeriod.Month04,LanguageStr_SharePeriod.Month05,LanguageStr_SharePeriod.Month06,LanguageStr_SharePeriod.Month07,LanguageStr_SharePeriod.Month08,LanguageStr_SharePeriod.Month09,LanguageStr_SharePeriod.Month10,LanguageStr_SharePeriod.Month11,LanguageStr_SharePeriod.Month12],
		monthNamesShort:[LanguageStr_SharePeriod.Month01,LanguageStr_SharePeriod.Month02,LanguageStr_SharePeriod.Month03,LanguageStr_SharePeriod.Month04,LanguageStr_SharePeriod.Month05,LanguageStr_SharePeriod.Month06,LanguageStr_SharePeriod.Month07,LanguageStr_SharePeriod.Month08,LanguageStr_SharePeriod.Month09,LanguageStr_SharePeriod.Month10,LanguageStr_SharePeriod.Month11,LanguageStr_SharePeriod.Month12],
		prevText:LanguageStr_SharePeriod.PreviosMonth,
		nextText:LanguageStr_SharePeriod.NextMonth,
		weekHeader:LanguageStr_SharePeriod.WeekHeader,
		showMonthAfterYear:true,
		dateFormat:"yy-mm-dd",
		
		timeOnlyTitle:LanguageStr_SharePeriod.SelectTime,
		timeText:LanguageStr_SharePeriod.Time,
		hourText:LanguageStr_SharePeriod.Hour,
		minuteText:LanguageStr_SharePeriod.Minute,
		secondText:LanguageStr_SharePeriod.Second,
		millisecText:LanguageStr_SharePeriod.MilliSecond,
		timezoneText:LanguageStr_SharePeriod.TimeZone,
		currentText:LanguageStr_SharePeriod.NowTime,
		closeText:LanguageStr_SharePeriod.OK,
		amNames:[LanguageStr_SharePeriod.AM,"AM","A"],
		pmNames:[LanguageStr_SharePeriod.PM,"PM","P"],
		timeFormat:"HH:mm:ss",
		hour:"23",
		minute:"59",
		second:"59"
		   };
		   
	$( '#Input_RadioPeriodMode_DateSelect' ).datetimepicker( opt );
}





function SetSharePeriod_UiElement()
{
	if ( PeriodType_Special == Ajax_PeriodType )
	{
		$( '#Input_RadioPeriodMode_NoLimit' ).prop( "checked", "true" );
		$( '#Input_RadioPeriodMode_DateSelect' ).attr( 'disabled', 'disabled' );
		
		$( '#Input_RadioPeriodMode_Date' ).attr( "MySelect", "false" );
		$( '#Input_RadioPeriodMode_NoLimit' ).attr( "MySelect", "true" );
	}
	else
	{
		$( '#Input_RadioPeriodMode_Date' ).prop( "checked", "true" );
		
		$( '#Input_RadioPeriodMode_Date' ).attr( "MySelect", "true" );
		$( '#Input_RadioPeriodMode_NoLimit' ).attr( "MySelect", "false" );
		
	   	$( "#Input_RadioPeriodMode_DateSelect" ).val( Ajax_PeriodDate );
		
		
		
		// 判斷是否過期
		var Today = new Date( Date.now() );
		var DbDay = new Date( Ajax_PeriodDate );
		var Diff = Today - DbDay;
		
		if ( 0 < Diff )
		{
			//Ajax_PeriodDate = Ajax_PeriodDate + "()";
			$( ".H4_Description" ).text( LanguageStr_SharePeriod.H4_DescriptionPeriod );
		}
		
		PeriodType = PeriodType_Date;
		PeriodDate = Ajax_PeriodDate;
	}
}

