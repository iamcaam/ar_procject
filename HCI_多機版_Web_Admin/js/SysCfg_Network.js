// JavaScript Document


// 設定網路資訊
function Ajax_SysCfg_SetInfo_Network()
{
    var StrJson = '{ ' + 
            '"AccessKey":"' + AccessKey + '"' + ',' +
            '"hostname":"' + newStorageName + '"' + ',' +
            '"ipaddr":"' + newIpv4 + '"' + ',' +
            '"netmask":"' + newIpv4Mask + '"' + ',' +
            '"gateway":"' + newIpv4Gateway + '"' + ',' +
            '"ipv6addr":"' + newIpv6 + '"' + ',' +
            '"ipv6mask":"' + newIpv6Prefix + '"' + ',' +
            '"ipv6gw":"' + newIpv6Gateway + '"' + ',' +
            '"dns1":"' + newIpv4Dns1 + '"' + ',' +
            '"dns2":"' + newIpv4Dns2 + '"' + ',' +
            '"TimeZone":"' + newTimeZone + '"' + ',' +
            '"Time":"' + '"' + ',' +
            '"NTP":"start",' +
            '"NTP_Server":"' + newNtp + '"' +
            '}';
	

	if ( true == DebugMsg )
	{
		OpenDialog_Message( "Ajax_SysCfg_SetInfo_Network(Send)" + StrJson );
	}
	
	
	OpenDialog_Message( "Please reconnect to new IP, if you changed." );
	
	
	$( ".Div_WaitAjax" ).dialog( "open" );
	//var WaitTimer = setTimeout( "CheckIfShowWaitDialog()", 500 ) 
	
	
			
    var request = CallAjax ( "/system/config/set", StrJson, "json" );
    
	
	
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		if ( true == DebugMsg )
		{
			alert("Ajax_SysCfg_GetInfo_Network(Rcv):" + jqXHR.responseText);
		}
	
		$( ".Div_WaitAjax" ).dialog( "close" );
		JSON_ChkStatus( AjaxData, LanguageStr_Network.Ajax01, LanguageStr_Network.Ajax02 );    
        Ajax_SysCfg_GetInfo_Network();
    });
	
    request.fail( function( jqXHR, textStatus )
	{ 
		if ( true == DebugMsg )
		{
			alert("Ajax_SysCfg_GetInfo_Network(Rcv, AjaxFail):" + jqXHR.responseText);
		}
		OpenDialog_Message( LanguageStr_Network.Ajax03 );
    });
}




// 取得網路資訊
function Ajax_SysCfg_GetInfo_Network()
{
    var StrJson = '{ ' + 
            '"AccessKey":"' + AccessKey + '"' +
            '}';
			
    //CreateDialog_Waiting();		// TODO Dialog
	

	if ( true == DebugMsg )
	{
		alert( "Ajax_SysCfg_GetInfo_Network(Send)" + StrJson );
	}
    var request = CallAjax ( "/system/config", StrJson, "json" );
    
	// Ajax Success
    request.done( function( AjaxData, statustext, jqXHR ) 
	{
		
		if ( true == DebugMsg )
		{
			alert("Ajax_SysCfg_GetInfo_Network(Rcv):" + jqXHR.responseText);
		}
		
        Async_Ajax_SysCfg_GetInfo_Network(AjaxData);
        //CloseDialog_Waiting();
    });
   
	// Ajax Fail
    request.fail( function( jqXHR, textStatus )
	{
		OpenDialog_Message( LanguageStr_Network.Ajax03 );
		if ( true == DebugMsg )
		{
			alert("Ajax_SysCfg_GetInfo_Network(Rcv, AjaxFail):" + jqXHR.responseText);
		}
    });
}





// 取得網路資訊 的非同步函式
function Async_Ajax_SysCfg_GetInfo_Network( JsonData )
{
	// 僅判斷 Status 是否為0
    if ( false === JSON_ChkStatus( JsonData, "", LanguageStr_Network.Ajax02 ) )
	{
        return false;
	}
	
	// 取得資料後填入顯示元件
    oldIpv4 = JsonData['nic'][0]['ipaddr'];
    oldIpv4Mask = JsonData['nic'][0]['netmask'];
    oldIpv4Gateway = JsonData['nic'][0]['gateway'];
    oldIpv4Dns1 = JsonData['nic'][0]['dns1'];
    oldIpv4Dns2 = JsonData['nic'][0]['dns2'];
	
    oldIpv6 = JsonData['nic'][0]['ipv6addr'];
    oldIpv6Prefix = JsonData['nic'][0]['ipv6mask'];
    oldIpv6Gateway = JsonData['nic'][0]['ipv6gw'];
	
    oldStorageName = JsonData['hostname']; 		//storage name --> hostname
    oldTimeZone = JsonData['TimeZone'];
    oldNtp = JsonData['NTP_Server'];
    
	
	// 填入 Information 畫面
	$('#H4_IpAdderss_InformationValue').text( oldIpv4 );
	$('#H4_Mask_InformationValue').text( oldIpv4Mask );
	$('#H4_Gateway_InformationValue').text( oldIpv4Gateway );
	$('#H4_DNS1_InformationValue').text( oldIpv4Dns1 );
	$('#H4_DNS2_InformationValue').text( oldIpv4Dns2 );
	
	$('#H4_IpAdderssV6_InformationValue').text( oldIpv6 );
	$('#H4_IpAdderssV6Prefix_InformationValue').text( oldIpv6Prefix );
	$('#H4_GatewayV6_InformationValue').text( oldIpv6Gateway );
	
	$('#H4_TimeServer_InformationValue').text( oldNtp );
	$('#H4_TimeZone_InformationValue').text( oldTimeZone );
	$('#H4_Host_InformationValue').text( oldStorageName );
}




// 進入編輯模式
function EnterEditNetwork()
{
    $('#H4_IpAdderss_Error').text("");
    $('#H4_Mask_Error').text("");
    $('#H4_Gateway_Error').text("");
    $('#H4_DNS1_Error').text("");
    $('#H4_DNS2_Error').text("");
    $('#H4_IpAdderssV6_Error').text("");
    $('#H4_GatewayV6_Error').text("");
    $('#H4_IpAdderssV6_Prefix_Error').text("");
    $('#H4_TimeServer_Error').text("");
    $('#H4_TimeZone_Error').text("");
    $('#H4_StorageName_Error').text("");
	// 填入 Edit 畫面
    Fill4IpAddress(oldIpv4, '#Input_IpAdderss_0');
    Fill4IpAddress(oldIpv4Mask, '#Input_Mask_0');
    Fill4IpAddress(oldIpv4Gateway, '#Input_Gateway_0');
    Fill4IpAddress(oldIpv4Dns1, '#Input_DNS1_0');
    Fill4IpAddress(oldIpv4Dns2, '#Input_DNS2_0');
	
    $('#Input_IpAdderssV6').val(oldIpv6);
    $('#Input_IpAdderssV6Prefix').val(oldIpv6Prefix);
    $('#Input_GatewayV6').val(oldIpv6Gateway);
	
    SetComboBox_SelectedIndex('ComboBx_TimeServer', oldNtp);
    SetComboBox_SelectedIndex('ComboBx_TimeZone', oldTimeZone);
    $('#Input_HostName').val(oldStorageName);
}




// 將傳入的IP字串拆解到四個格子內 
function Fill4IpAddress(ip, ElementId )
{
    var ipField = ip.split(".");
    if (ipField.length == 4)
    {
        $(ElementId+'1').val(ipField[0]);
        $(ElementId+'2').val(ipField[1]);
        $(ElementId+'3').val(ipField[2]);
        $(ElementId+'4').val(ipField[3]);
    }
}



// 根據資料設定下拉選單位置
function SetComboBox_SelectedIndex(s, v) 
{
    var obj=document.getElementById(s);
    for ( var i = 0; i < obj.options.length; i++ ) 
	{
        if ( obj.options[i].value == v ) 
		{
            obj.options[i].selected = true;
            return;
        }
    }
}




// 先從輸入元件轉出資料
function getSystemInputData()
{
	// IPV4
	newIpv4 = $('#Input_IpAdderss_01').val() + '.' + $('#Input_IpAdderss_02').val() + '.' + $('#Input_IpAdderss_03').val() + '.' + $('#Input_IpAdderss_04').val();
    if (newIpv4 === "...") { newIpv4 = ""; }
	
	newIpv4Mask = $('#Input_Mask_01').val() + '.' + $('#Input_Mask_02').val() + '.' + $('#Input_Mask_03').val() + '.' + $('#Input_Mask_04').val();
    if (newIpv4Mask === "...") { newIpv4Mask = ""; }	
        
    newIpv4ANDMask1 = $('#Input_IpAdderss_01').val() & $('#Input_Mask_01').val();    
    newIpv4ANDMask2 = $('#Input_IpAdderss_02').val() & $('#Input_Mask_02').val();    
    newIpv4ANDMask3 = $('#Input_IpAdderss_03').val() & $('#Input_Mask_03').val();    
    newIpv4ANDMask4 = $('#Input_IpAdderss_04').val() & $('#Input_Mask_04').val();    
    
    newIpv4Gateway = $('#Input_Gateway_01').val() + '.' + $('#Input_Gateway_02').val() + '.' + $('#Input_Gateway_03').val() + '.' + $('#Input_Gateway_04').val();
    if (newIpv4Gateway === "...") { newIpv4Gateway = ""; }
    
    newIpv4GatewayANDMask1 = $('#Input_Gateway_01').val() & $('#Input_Mask_01').val();    
    newIpv4GatewayANDMask2 = $('#Input_Gateway_02').val() & $('#Input_Mask_02').val();    
    newIpv4GatewayANDMask3 = $('#Input_Gateway_03').val() & $('#Input_Mask_03').val();    
    newIpv4GatewayANDMask4 = $('#Input_Gateway_04').val() & $('#Input_Mask_04').val();    
    
    newIpv4Dns1 = $('#Input_DNS1_01').val() + '.' + $('#Input_DNS1_02').val() + '.' + $('#Input_DNS1_03').val() + '.' + $('#Input_DNS1_04').val();
    if (newIpv4Dns1 === "...") newIpv4Dns1 = "";
	
	newIpv4Dns2 = $('#Input_DNS2_01').val() + '.' + $('#Input_DNS2_03').val() + '.' + $('#Input_DNS2_03').val() + '.' + $('#Input_DNS2_04').val();
    if (newIpv4Dns2 === "...") newIpv4Dns2 = "";
	
	// IPV6
	newIpv6 = $('#Input_IpAdderssV6').val();
	newIpv6Prefix = $('#Input_IpAdderssV6Prefix').val();
	newIpv6Gateway = $('#Input_GatewayV6').val();
	
	// Host
	newNtp = $('#ComboBx_TimeServer').val();
	newTimeZone = $('#ComboBx_TimeZone').val();
    newStorageName = $('#Input_HostName').val();
}




// 判斷輸入資料是否有錯
function CheckSystemInput()
{
    var regexIp=/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
    var rc=true;
	
    $('#H4_IpAdderss_Error').text("");
    $('#H4_Mask_Error').text("");
    $('#H4_Gateway_Error').text("");
    $('#H4_DNS1_Error').text("");
    $('#H4_DNS2_Error').text("");
    $('#H4_IpAdderssV6_Error').text("");
    $('#H4_GatewayV6_Error').text("");
    $('#H4_IpAdderssV6_Prefix_Error').text("");
    $('#H4_TimeServer_Error').text("");
    $('#H4_TimeZone_Error').text("");
    $('#H4_StorageName_Error').text("");

    if (newStorageName == "")
    {
        $('#H4_StorageName_Error').text( LanguageStr_Network.H4_StorageName_Error );
        rc = false;
    }
    if ((newIpv4 !== "") && (regexIp.test(newIpv4) === false))
    {
        $('#H4_IpAdderss_Error').text( LanguageStr_Network.H4_IpAdderss_Error );
        rc = false;
    }
    if ((newIpv4Mask !== "") && (regexIp.test(newIpv4Mask) === false))
    {
        $('#H4_Mask_Error').text( LanguageStr_Network.H4_Mask_Error01 );
        rc = false;
    }
    if ((newIpv4 !== "") && (newIpv4Mask === ""))
    {
        $('#H4_Mask_Error').text( LanguageStr_Network.H4_Mask_Error02);
	rc = false;
    }
    if ((newIpv4 === "") && (newIpv4Mask !== ""))
    {
	$('#H4_Mask_Error').text( LanguageStr_Network.H4_Mask_Error03);
	rc = false;
    }
    if ((newIpv4Gateway !== "") && (regexIp.test(newIpv4Gateway) === false))
    {
	$('#ipGwMsg').text( LanguageStr_Network.ipGwMsg);
	rc = false;
    }
    if ((newIpv4 !== "") && (newIpv4Gateway === ""))
    {
	$('#H4_Gateway_Error').text( LanguageStr_Network.H4_Gateway_Error01 );
	rc = false;
    }
    if ((newIpv4 === "") && (newIpv4Gateway !== ""))
    {
	$('#H4_Gateway_Error').text( LanguageStr_Network.H4_Gateway_Error02 );
	rc = false;
    }
    if(newIpv4ANDMask1 !== newIpv4GatewayANDMask1 || newIpv4ANDMask2 !== newIpv4GatewayANDMask2 || newIpv4ANDMask3 !== newIpv4GatewayANDMask3 || newIpv4ANDMask4 !== newIpv4GatewayANDMask4)
    {
        $('#H4_Gateway_Error').text( LanguageStr_Network.H4_Gateway_Error03 );
        rc = false;
    }
    if ((newIpv4Dns1 !== "") && (regexIp.test(newIpv4Dns1) === false))
    {
	$('#H4_DNS1_Error').text( LanguageStr_Network.H4_StorageName_Error.H4_DNS_Error01 );
	rc = false;
    }
    if ((newIpv4Dns2 !== "") && (regexIp.test(newIpv4Dns2) === false))
    {
	$('#H4_DNS2_Error').text( LanguageStr_Network.H4_DNS_Error02 );
	rc = false;
    }
    if ((newIpv4Dns1 === "") && (newIpv4Dns2 === ""))
    {
	$('#H4_DNS1_Error').text( LanguageStr_Network.H4_DNS_Error03 );
	rc = false;
    }
    if ((newIpv4 === "") && (newIpv6 === ""))
    {
	$('#H4_IpAdderss_Error').text( LanguageStr_Network.H4_IpAdderss_Error );
	rc = false;
    }
    if (newIpv6Gateway !== "")
    {
        if (newIpv6 === "")
        {
            $('#H4_GatewayV6_Error').text( LanguageStr_Network.H4_GatewayV6_Error02);
            rc = false;
        }
    }
    if(newIpv6Prefix !== "")
    {
        if (newIpv6 === "")
        {
            $('#H4_IpAdderssV6_Prefix_Error').text( LanguageStr_Network.H4_PrefixV6_Error01);
            rc = false;
        }
    }
    if (newIpv6 !== "")
    {
        if (!checkIpv6(newIpv6))
        {
            $('#H4_IpAdderssV6_Error').text( LanguageStr_Network.H4_IPV6_Error);
            rc = false;
        }
        if (newIpv6Gateway !== "")
        {
            if (!checkIpv6(newIpv6Gateway))
            {
                $('#H4_GatewayV6_Error').text( LanguageStr_Network.H4_GatewayV6_Error);
                rc = false;
            }
        }
        else
        {
            $('#H4_GatewayV6_Error').text( LanguageStr_Network.H4_GatewayV6_Error01);
            rc = false;
        }
        if(newIpv6Prefix !== "")
        {
            if(!isInt(newIpv6Prefix))
            {
                $('#H4_IpAdderssV6_Prefix_Error').text( LanguageStr_Network.H4_PrefixV6_Error02);
                rc = false;                
            }
        }
        else
        {
            $('#H4_IpAdderssV6_Prefix_Error').text( LanguageStr_Network.H4_PrefixV6_Error);
            rc = false;
        }
    }    
    return rc;
}

function isInt(val)
{
    var reg = /^[0-9]*$/;
    return reg.test(val);
}


function checkIpv6(str)
{
    var perlipv6regex = "^\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))(%.+)?\s*$";
    var aeronipv6regex = "^\s*((?=.*::.*)(::)?([0-9A-F]{1,4}(:(?=[0-9A-F])|(?!\2)(?!\5)(::)|\z)){0,7}|((?=.*::.*)(::)?([0-9A-F]{1,4}(:(?=[0-9A-F])|(?!\7)(?!\10)(::))){0,5}|([0-9A-F]{1,4}:){6})((25[0-5]|(2[0-4]|1[0-9]|[1-9]?)[0-9])(\.(?=.)|\z)){4}|([0-9A-F]{1,4}:){7}[0-9A-F]{1,4})\s*$";

    var regex = "/"+perlipv6regex+"/";
    return (/^\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))(%.+)?\s*$/.test(str));
}

// 註冊Network頁面的事件
function RegNetworkPageEvent()
{
    ///// Network /////
    $('.Btn_Network_Edit').click(function(e) 
    {
        ChangeDiv_Network_ToEditModeOrInformationMode( true );  
        EnterEditNetwork();

        $('.Btn_Network_Edit').hide();
        $('.Btn_Network_Cancel').fadeIn(500);
        $('.Btn_Network_OK').fadeIn(500);
    });
	
	
    $('.Btn_Network_Cancel').click(function(e) 
    {
        ChangeDiv_Network_ToEditModeOrInformationMode( false );

        $('.Btn_Network_Edit').fadeIn(500);
        $('.Btn_Network_Cancel').hide();
        $('.Btn_Network_OK').hide();
    });
	
	
    $('.Btn_Network_OK').click(function(e) 
    {
        // 先從輸入元件轉出資料
        getSystemInputData();		
        // 判斷輸入資料是否有錯 
        if ( true == CheckSystemInput() )
        {                    
            $('.Btn_Network_Edit').fadeIn(500);
            $('.Btn_Network_Cancel').hide();
            $('.Btn_Network_OK').hide();

            Ajax_SysCfg_SetInfo_Network();
            ChangeDiv_Network_ToEditModeOrInformationMode( false );
        }
    });
}