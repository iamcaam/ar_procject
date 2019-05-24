
// 列出所有使用者
function Ajax_ListSystemStatusLight()
{
    var StrJson = 	'{ ' + 
            		'"AccessKey":"' + AccessKey + '"' +
            		'}';
					
    var request = CallAjax ( "/system/systemstatus/light", StrJson, "json" );
	
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		// 網站初始化時有6項資料要載入，取回的話要記錄在 Counter
		ChkIfDataReadFinish_Counter++;
        Async_Ajax_Systemstatus_Light(AjaxData);
    });
         	
    request.fail( function( jqXHR, textStatus )
	{
		if ( true == DebugMsg )
		{
			alert( "jqXHR:" + jqXHR.responseText );
		}
		
		OpenDialog_Message( LanguageStr_Environment.AjaxMsg00 );
    });
}



// 列出所有使用者 如果成功，則進行解析資料並設定到UI上
function Async_Ajax_Systemstatus_Light(JsonData)
{
    if ( 0 != JsonData['status'] )
    {
		OpenDialog_Message( LanguageStr_Environment.AjaxMsg01 + JsonData['status'] );
        // 999 : unknow cmd, -1 : Storage Error            
        return;
    }
	
    var PowerStatus = JsonData['PowerStatus'];
    var RAIDStatus = JsonData['RAIDStatus'];
    var DiskStatus = JsonData['DiskStatus'];
    var CPUTempStatus = JsonData['CPUTempStatus'];
    var SysTempStatus = JsonData['SysTempStatus'];
    var HDDTempStatus = JsonData['HDDTempStatus'];
    var FanStatus = JsonData['FanStatus'];
	var AllTempStatus = -1;
	var NICNumber = 0;
    var NicName = new Array();
    var NicStatus = new Array();
	
	if ( ( 0 == CPUTempStatus ) && ( 0 == SysTempStatus ) && ( 0 == HDDTempStatus ) )
	{
		AllTempStatus = 0;
	}
	
	//
    // Power 圖示判斷
	//
	if ( -1 == PowerStatus )
	{
		$('#Img_Power').css( "border-color", "#777" );
	}
	else if ( 0 == PowerStatus )
	{
		$('#Img_Power').css( "border-color", "#0F0" );
	}
	else
	{
		$('#Img_Power').css( "border-color", "#F00" );
	}
	
	
	//
    // Raid 圖示判斷
	//
	if ( -1 == RAIDStatus )
	{
		$('#Img_Raid').css( "border-color", "#777" ); 
	}
	else if ( ( 0 == RAIDStatus ) || ( 7 == RAIDStatus ) || ( 15 == RAIDStatus ) || ( 19 == RAIDStatus ) || ( 20 == RAIDStatus ) || ( 22 == RAIDStatus ) || ( 24 == RAIDStatus ) || ( 25 == RAIDStatus ) )
	{
		$('#Img_Raid').css( "border-color", "#0F0" ); 
	}
	else
	{
		$('#Img_Raid').css( "border-color", "#F00" );
	}
	
	
	//
    // Disk 圖示判斷
	//
	if ( 0 == DiskStatus )
	{
		$('#Img_Disk').css( "border-color", "#0F0" ); 
	}
	else
	{
		$('#Img_Disk').css( "border-color", "#F00" );
	}
	
	
	//
    // 溫度 圖示判斷
	//
	if ( 0 == AllTempStatus )
	{
		$('#Img_Temp').css( "border-color", "#0F0" ); 
	}
	else
	{
		$('#Img_Temp').css( "border-color", "#F00" );
	}
	
	
	//
    // Fan 圖示判斷
	//
	if ( 0 == FanStatus )
	{
		$('#Img_Fan').css( "border-color", "#0F0" ); 
	}
	else
	{
		$('#Img_Fan').css( "border-color", "#F00" );
	}
	
	
	//
	// 多網卡會影響 DIV 的寬度，以及位置
	//
    $.each( JsonData['NICStatus'], function( index, element )
    {
		NicStatus[ NICNumber ] = element['Online'];
		NicName[ NICNumber ] = element['Name'];
		NICNumber++;
    });
	
	
	
	if ( 0 < NICNumber )
	{
		$('#Img_Network0').show();
		
		if ( 1 == NicStatus[ 0 ] )
		{
			$('#Img_Network0').css( "border-color", "#0F0" ); 
		}
		else
		{
			$('#Img_Network0').css( "border-color", "#F00" ); 
		}
	}
	if ( 1 < NICNumber )
	{
		$('#Img_Network1').show();
		
		if ( 1 == NicStatus[ 1 ] )
		{
			$('#Img_Network1').css( "border-color", "#0F0" ); 
		}
		else
		{
			$('#Img_Network1').css( "border-color", "#F00" ); 
		}
	}
	if ( 2 < NICNumber )
	{
		$('#Img_Network2').show();
		
		if ( 1 == NicStatus[ 2 ] )
		{
			$('#Img_Network2').css( "border-color", "#0F0" ); 
		}
		else
		{
			$('#Img_Network2').css( "border-color", "#F00" ); 
		}
	}
	if ( 3 < NICNumber )
	{
		$('#Img_Network3').show();
		
		if ( 1 == NicStatus[ 3 ] )
		{
			$('#Img_Network3').css( "border-color", "#0F0" ); 
		}
		else
		{
			$('#Img_Network3').css( "border-color", "#F00" ); 
		}
	}
	
	// 算寬度跟位置
	AdjPosition = ( NICNumber - 1 )*70;
	$('#Div_BottomEnvironment').css( "left", 950 - AdjPosition );
	$('#Div_BottomEnvironment').width(  $('#Div_BottomEnvironment').width() + AdjPosition  );
}







function ListSystemStatus()
{
    var StrJson = 	'{ ' + 
					'"AccessKey":"' + AccessKey + '"' +
					'}';
					
					
    var request = CallAjax ( "/system/systemstatus", StrJson, "json" );
	
	    			
    request.done( function( AjaxData, statustext, jqXHR ) 
	{				
        Async_Ajax_Systemstatus(AjaxData);
    });
         	
    request.fail( function( jqXHR, textStatus )
	{ 
		OpenDialog_Message( LanguageStr_Environment.AjaxMsg00 );
    });
}





function Async_Ajax_Systemstatus(JsonData)
{
    if ( 0 != JsonData['status'] )
    {
        //-999 : unknow cmd, -1 : Storage Error            
        return;
    }
    var SysTempName = JsonData['Host']['SysTemp']['Name'];
    var SysCurTemp = JsonData['Host']['SysTemp']['Cur_Temp'];
    var SysThrTemp = JsonData['Host']['SysTemp']['Thr_Temp'];
    alert("SysTemp Name : " + SysTempName + ",Cur Temp : " + SysCurTemp + ",Thr_Temp : " + SysThrTemp);
    $.each(JsonData['Host']['CPUTemp'], function( index, element )
    {
        var Name = element['Name'];
        var Cur_Temp = element['Cur_Temp'];
        var Thr_Temp = element['Thr_Temp'];
        alert("CPUTemp Name : " + Name + ",Cur_Temp : " + Cur_Temp + ",Thr_Temp : " + Thr_Temp);
    })
    $.each(JsonData['Host']['HDDTemp'],function( index, element )
    {
        var HDDID = element['ID'];
        var HDD_Cur_Temp = element['Cur_Temp'];
        var HDD_Thr_Temp = element['Thr_Temp'];
        alert("HDD ID : " + HDDID + ",Cur_Temp : " + HDD_Cur_Temp + ",Thr_Temp : " + HDD_Thr_Temp);
    })
    $.each(JsonData['Host']['FAN'],function( index, element )
    {
        var FANName = element['Name'];
        var Cur_RPM = element['Cur_RPM'];
        var Thr_RPM = element['Thr_RPM'];
        alert("FAN Name : " + FANName + ",Cur_RPM : " + Cur_RPM + ",Thr_RPM : " + Thr_RPM);
    })
    $.each(JsonData['Host']['SysVol'],function( index, element )
    {
        var SysVolName = element['Name'];
        var SysVol_Cur_V = element['Cur_V'];
        var SysVol_Hi_V = element['Hi_V'];
        var SysVol_Lo_V = element['Lo_V'];
        alert("SysVol Name : " + SysVolName + ",Cur_V : " + SysVol_Cur_V + ",Hi_V : " + SysVol_Hi_V + ",Lo_V : " + SysVol_Lo_V);
    })    
    $.each(JsonData['JBOD'],function( index, element)
    {
        alert("JBOD Index : " + index);
        $.each(element['HDDTemp'],function( index1, element1)
        {
            var JBODHDDID = element1['ID'];
            var JBODHDD_Cur_Temp = element1['Cur_Temp'];
            var JBODHDD_Thr_Temp = element1['Thr_Temp'];
            alert("JBOD HDD ID : " + JBODHDDID + ",JBOD Cur_Temp : " + JBODHDD_Cur_Temp + ",JBOD Thr_Temp : " + JBODHDD_Thr_Temp);
        })
    })
}

//function CallAjax(url, data, datatype)
//{     
//    var request = $.ajax({ 
//    type: "POST", 
//     url: url,
//    data:data,
//    dataType: datatype
//    });    
//    return request;
//} 




/*
RAID Status :
-1 : 沒有RAID (綠燈)
0 : Ready (綠燈)
1 : Crashed (紅燈)
3 : Degraded (紅燈)
7 : Initialing (綠燈)
8 : Recovering (紅燈)
10 : Initial Fail (紅燈)
15 : Bacground Initial (綠燈)
19 : migrating (綠燈)
20 : migrating (綠燈)
22 : expanding (綠燈)
24 : expanding (綠燈)
25 : expanding (綠燈)
27 : Degraded (紅燈)
28 : Degraded (紅燈)
Others : Warning (紅燈)

Disk Status :
0 : Ready (綠燈)
1 : Warning (黃燈)
2 : Error (紅燈)

CPUTemp :
0 : Ready (綠燈)
1 : Error (紅燈)

SystemTemp :
0 : Ready (綠燈)
1 : Error (紅燈)

HDDTemp :
0 : Ready (綠燈)
1 : Error (紅燈)

FAN :
0 : Ready (綠燈)
1 : Error (紅燈)

Power :
-1 : 無Power偵測
0 : Ready (綠燈)
1 : Error (紅燈)
2 : N/A (紅燈)

NIC : 
0 : 無接線 (紅燈)
1 : 有接線(綠燈)
*/