// JavaScript Document

////    常數    ////

var ShareCase_PublicShare		 			= 53;							// 公開分享
var ShareCase_UploadDownloadBrowse 			= 29;							// 上傳 下載 瀏覽
var ShareCase_DownloadBrowse 				= 21;							// 下載、瀏覽（僅下載）
var ShareCase_UploadBrowse 					= 13;							// 上傳 瀏覽（交作業模

var ListMode_FromOthers 					= 1;							// 別人分享給我
var ListMode_FromMe 						= 0;							// 我分享給別人

var ShareTypr_Public 						= 0;							// 分享者是 Public
var ShareTypr_ByUser 						= 1;							// 分享者是 User
var ShareTypr_ByGroup 						= 2;							// 分享者是 Group

var SortFlag_File_Asc 						= 0;							// 排序-檔案，順向
var SortFlag_File_Desc 						= 1;							// 排序-檔案，逆向
var SortFlag_Who_Asc 						= 2;							// 排序-（被）分享對象，順向
var SortFlag_Who_Desc 						= 3;							// 排序-（被）分享對象，
var SortFlag_ShareMode_Asc 					= 4;							// 排序-模式，順向
var SortFlag_ShareMode_Desc 				= 5;							// 排序-模式，逆向
var SortFlag_PeriodTime_Asc 				= 6;							// 排序-分享時間，順向
var SortFlag_PeriodTime_Desc 				= 7;							// 排序-分享時間，逆向順向







var AccessKey = "953e834eb3caab3367aba92bfe2d02a7";							// Access Key
var ShowAjaxDebug = false;
var IsSingleDelete = false;													// 是單一刪除，或連續多重刪除
var Del_Share_FileObj_UserId = [];											// 要刪除的 ID
var Del_Share_FileObj_GroupId = [];											// 要刪除的 ID
var Del_Share_FileObj_PublicId = [];										// 要刪除的 ID
var Del_Share_IndexId = [];													// 要刪除的 ID

var ListMode = 0;
var AjaxCounter = 0;
var AjaxCounterRcv = 0;

var SortFlag = 0;

var List_ShareFromMe = [];
var List_ShareFromOther = [];
var List_ShareFromMe_Search = [];
var List_ShareFromOther_Search = []; 
var IsShareExist = false;
var ReturnUrl = "";
var Language = "";





function ExitMe()
{
	window.location.assign( ReturnUrl );
}




function InitialSettingAndEvent()
{
	InitialSetting();
	
	// 註冊事件
	RegisterEvents();
	reLocateLayout();
	
	$('.h3_Btn_FromMe').click();
}







// 視窗變更大小就會觸發，利用 Timer 來延後
$(window).resize(function()
{
	setTimeout( "reLocateLayout()", 500 );
});





function InitialSetting()
{
	CreateDialog();
	
	$('#h5_BtnDel_SelectedShareList').hide();		// 有勾選檔案才可以出現刪除按鈕
	//Ajax_ListMyShare_FromMe();
}





function CreateDialog()
{
	var StrHtml = "";
    
	StrHtml += '<div class = "Div_DialogBase" >';
        StrHtml += '<div class = "Div_WaitAjax" title = "' + LanguageStr_MyShare.Div_WaitAjax > + '"';
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




// 註冊事件
function RegisterEvents()
{
	// 移除按鈕要特別獨立出來註冊事件，是因為每次重新撈資料之後畫面會重新刷過，事件就必須重新註冊了
	RegEvt_AfterUiGenerated();
	
	// 標頭的排序事件
	RegEvt_ColumnSort();
	
	
	
	
	// 搜尋按鈕事件
	$('#h5_BtnSearch').click( function( e ) 
	{
		var StrInputSearch = $('#Input_SearchKeyword').val();
		var Regex = new RegExp( ".{0,}" + StrInputSearch + ".{0,}", "g");
		
		// From Other
		if ( ListMode_FromOthers == ListMode )
		{
			List_ShareFromOther = $.grep(
				  List_ShareFromOther_Search,
				  function(value) 
				  {
					 return ( ( value['UserOrUgName'].match( Regex ) != null ) || ( value['FileOrDirName'].match( Regex ) != null ) ); 
				  });
				  
			List_ShareFromOther_ShowDataToHtml();
		}
		// From Me
		else
		{
			List_ShareFromMe = $.grep(
				  List_ShareFromMe_Search,
				  function(value) 
				  {
					 return ( ( value['UserOrUgName'].match( Regex ) != null ) || ( value['FileOrDirName'].match( Regex ) != null ) );
				  });
				  	 
			List_ShareFromMe_ShowDataToHtml();
		}
	} );
	
	
	
	// Header 全選事件
	$('.ChkBoxSelectAll').click( function(e) 
	{
		// 全不選
		if ( false ==  $('.ChkBoxSelectAll').prop( 'checked' ) )
		{
			$('#h5_BtnDel_SelectedShareList').fadeOut(500);
        	$('.Input_ShareListRow_Target').prop( 'checked', false );
		}
		// 全選
		else
		{
			$('#h5_BtnDel_SelectedShareList').fadeIn(500);
			$('.Input_ShareListRow_Target').prop( 'checked', true );
		}
    });
	
	
	// 「來自其他人的分享」
	$('.h3_Btn_FromOther').click( function( e ) 
	{
		ListMode = ListMode_FromOthers;
		
		// 設定畫面元件
		$('#Div_ShareList_HeaderRow').children('.h4_ShareListRow_TargetName').text( LanguageStr_MyShare.h4_ShareListRow_TargetName_FromOther );
		$('.h3_Btn_FromOther').css( "background-color" , "#2164ff" );
		$('.h3_Btn_FromOther').css( "color" , "#FFF" );
		
		$('.h3_Btn_FromMe').css( "background-color" , "#DEF5FA" );
		$('.h3_Btn_FromMe').css( "color" , "#999" );
		
		Ajax_ListMyShare_FromOther();
	});
	
	
	// 切換「我分享出去的檔案」
	$('.h3_Btn_FromMe').click( function( e ) 
	{
		ListMode = ListMode_FromMe;
		
		// 設定畫面元件
		$('#Div_ShareList_HeaderRow').children('.h4_ShareListRow_TargetName').text( LanguageStr_MyShare.h4_ShareListRow_TargetName_FromMe );
		
		$('.h3_Btn_FromOther').css( "background-color" , "#DEF5FA" );
		$('.h3_Btn_FromOther').css( "color" , "#999" );
		
		$('.h3_Btn_FromMe').css( "background-color" , "#2164ff" );
		$('.h3_Btn_FromMe').css( "color" , "#FFF" );
		
		Ajax_ListMyShare_FromMe();
	});
	
	
	// 一次刪除所選
	$('#h5_BtnDel_SelectedShareList').click( function( e ) 
	{
		$('#h5_BtnDel_SelectedShareList').fadeOut(500);
		IsSingleDelete = false;
		
		// 「別人分享給我」
		if ( ListMode_FromOthers == ListMode )
		{
			var idType;
			var idObject;
			var idShareCase;
			var idUser;
			var idGroup;
			var idStorage;
			var Para_Str = [];
			var LoopCounter = 0;
			
			// 逐一判斷哪些被勾選了
			$.each( $('.Input_ShareListRow_Target'), function()
			{
				if ( $(this).prop( "checked" ) && false == $(this).hasClass( "ChkBoxSelectAll" ) )
				{
					idType = $(this).attr( "idType" );
					idObject = $(this).attr( "idObject" );
					idShareCase = $(this).attr( "idShareCase" );
					idUser = $(this).attr( "idUser" );
					idGroup = $(this).attr( "idGroup" );
					idStorage = $(this).attr( "idStorage" );
					
					Para_Str[ LoopCounter ] = [];
					Para_Str[ LoopCounter ][ 'idUser' ] = idUser;
					Para_Str[ LoopCounter ][ 'idObject' ] = idObject;
					Para_Str[ LoopCounter ][ 'idShareCase' ] = idShareCase;
					Para_Str[ LoopCounter ][ 'idGroup' ] = idGroup;
					Para_Str[ LoopCounter ][ 'idStorage' ] = idStorage;
					
					
					$(this).addClass( "DeleteMe" );		// 由於這個指令是一次刪除多筆資料，所以可以用 AddClass 將所有物件標籤起來，在 Ajax 函式最後做動畫移除UI元件
					LoopCounter++;
				}
				
			} );
			
			$('.Div_WaitAjax').dialog('open');
			Ajax_DelMyShare_FromOther( Para_Str );		// 由於這個指令是一次刪除多筆資料，所以可以用 AddClass 將所有物件標籤起來，在 Ajax 函式最後做動畫移除UI元件
		}
		// 我分享給別人
		else
		{	
			var Para_Public = [];
			var Para_User = [];
			var Para_Group = [];
			AjaxCounter = 0;
			AjaxCounterRcv = 0;
			
			$('.Div_WaitAjax').dialog('open');
			
			// 先計算總共會發幾個 Ajax 出去，在 Ajax 函式中會做收的 Counter ，兩者相等時會把對話框關掉 
			$.each( $('.Input_ShareListRow_Target'), function()
			{
				if ( $(this).prop( "checked" ) && false == $(this).hasClass( "ChkBoxSelectAll" ) )
				{
					AjaxCounter++;
				}
			} );
			
			
			// 逐一判斷哪些被勾選了
			$.each( $('.Input_ShareListRow_Target'), function()
			{
				/* TODO 完美邏輯？ 在多想幾天吧！ API + 非同步特性，很難呼叫*/
				
				if ( $(this).prop( "checked" ) && false == $(this).hasClass( "ChkBoxSelectAll" ) )
				{
					var idType;
					var idObject;
					var idShareCase;
					var idUser;
					var idGroup;
					
					var Para_Public = [];
					var Para_User = [];
					var Para_Group = [];
					
					idType = $(this).attr( "idType" );
					idObject = $(this).attr( "idObject" );
					idShareCase = $(this).attr( "idShareCase" );
					idUser = $(this).attr( "idUser" );
					idGroup = $(this).attr( "idGroup" );
					RowNumber = $(this).attr( "RowNum" );
				
					// 公開分享
					if ( "0" == idType )
					{
						Para_Public[ 0 ] = [];
						Para_Public[ 0 ]['ID'] = idObject;
						Para_Public[ 0 ]['ShareCase'] = idShareCase;
						
						Ajax_DeletePublicShare_MyAllList( AccessKey, Para_Public );
					}
					// USER 分享
					else if ( "1" == idType )
					{
						Para_User[ 0 ] = idUser;
						Ajax_RemoveUserfromShare_MyAllList( AccessKey, idObject, idShareCase, Para_User );
					}
					// GROUP分享
					else if ( "2" == idType )
					{
						Para_Group[ 0 ] = idGroup;
						Ajax_RemoveGroupfromShare_MyAllList( AccessKey, idObject, idShareCase, Para_Group );
					}
					
					// Raymond 說不要動畫，太慢，看起來會誤認為是效能不好
					//$(this).parent().fadeOut( 1000, function() 
					//{
						$(this).parent().remove();
						
						// 判斷是否有資料存在，是否要顯示「目前沒有檔案分享......」的字
						if ( false == ChkIsAnyDataExist() )
						{
							$('.h4_ShareList_NoShare').show( 1000 );
							$('.ChkBoxSelectAll').hide();
						}
					//} );
				}
				
			});
			
		}		// if ( ListMode_FromOthers == ListMode )
		
		$( '.ChkBoxSelectAll' ).prop( "checked", false );
	} );
}





// 標頭的排序事件
function RegEvt_ColumnSort()
{
	// 對象
	$('#Div_ShareList_HeaderRow').children('.h4_ShareListRow_TargetName').mouseover( function(e) 
	{
		$(this).css( "border", "1px solid #373" );
	} );
	
	$('#Div_ShareList_HeaderRow').children('.h4_ShareListRow_TargetName').mouseleave( function(e) 
	{
		$(this).css( "border", "0px solid #373" );
	} );
		
	$('#Div_ShareList_HeaderRow').children('.h4_ShareListRow_TargetName').click( function(e) 
	{
		if ( SortFlag_Who_Asc == SortFlag )
		{
			SortFlag = SortFlag_Who_Desc;
		}
		else
		{
			SortFlag = SortFlag_Who_Asc;
		}
		
		if ( ListMode_FromMe == ListMode )
		{
			List_ShareFromMe_ShowDataToHtml();
		}
		else
		{
			List_ShareFromOther_ShowDataToHtml();
		}
	} );
	
	
	
	// 檔名
	$('#Div_ShareList_HeaderRow').children('.h4_ShareListRow_FileName').mouseover( function(e) 
	{
		$(this).css( "border", "1px solid #373" );
	} );
	
	$('#Div_ShareList_HeaderRow').children('.h4_ShareListRow_FileName').mouseleave( function(e) 
	{
		$(this).css( "border", "0px solid #373" );
	} );
		
	$('#Div_ShareList_HeaderRow').children('.h4_ShareListRow_FileName').click( function(e) 
	{
		if ( SortFlag_File_Asc == SortFlag )
		{
			SortFlag = SortFlag_File_Desc;
		}
		else
		{
			SortFlag = SortFlag_File_Asc;
		}
		
		if ( ListMode_FromMe == ListMode )
		{
			List_ShareFromMe_ShowDataToHtml();
		}
		else
		{
			List_ShareFromOther_ShowDataToHtml();
		}
	} );
	
	
	
	// ShareMode
	$('#Div_ShareList_HeaderRow').children('.h4_ShareListRow_Mode').mouseover( function(e) 
	{
		$(this).css( "border", "1px solid #373" );
	} );
	
	$('#Div_ShareList_HeaderRow').children('.h4_ShareListRow_Mode').mouseleave( function(e) 
	{
		$(this).css( "border", "0px solid #373" );
	} );
		
	$('#Div_ShareList_HeaderRow').children('.h4_ShareListRow_Mode').click( function(e) 
	{
		if ( SortFlag_ShareMode_Asc == SortFlag )
		{
			SortFlag = SortFlag_ShareMode_Desc;
		}
		else
		{
			SortFlag = SortFlag_ShareMode_Asc;
		}
		
		if ( ListMode_FromMe == ListMode )
		{
			List_ShareFromMe_ShowDataToHtml();
		}
		else
		{
			List_ShareFromOther_ShowDataToHtml();
		}
	} );
	
	
	
	// 分享時間
	$('#Div_ShareList_HeaderRow').children('.h4_ShareListRow_DateTime').mouseover( function(e) 
	{
		$(this).css( "border", "1px solid #373" );
	} );
	
	$('#Div_ShareList_HeaderRow').children('.h4_ShareListRow_DateTime').mouseleave( function(e) 
	{
		$(this).css( "border", "0px solid #373" );
	} );
		
	$('#Div_ShareList_HeaderRow').children('.h4_ShareListRow_DateTime').click( function(e) 
	{
		if ( SortFlag_PeriodTime_Asc == SortFlag )
		{
			SortFlag = SortFlag_PeriodTime_Desc;
		}
		else
		{
			SortFlag = SortFlag_PeriodTime_Asc;
		}
		
		if ( ListMode_FromMe == ListMode )
		{
			List_ShareFromMe_ShowDataToHtml();
		}
		else
		{
			List_ShareFromOther_ShowDataToHtml();
		}
	} );
}



// 移除按鈕要特別獨立出來註冊事件，是因為每次重新撈資料之後畫面會重新刷過，事件就必須重新註冊了
function RegEvt_AfterUiGenerated()
{
	// CheckBox 單選事件
	$('.Input_ShareListRow_Target').click( function(e) 
	{
		var Counter = 0;
		
		$.each( $('.Input_ShareListRow_Target'), function ()
		{
			if ( $(this).prop( "checked" ) )
			{
				Counter++;
			}
		} );
		
		if ( 1 <= Counter )
		{
			$('#h5_BtnDel_SelectedShareList').fadeIn(500);
		}
		else
		{
			$('#h5_BtnDel_SelectedShareList').fadeOut(500);
		}
	} );
	
	
	
	// 移除單一指定 Share
	$('.Img_ShareListRow_Remove').click( function( e ) 
	{
		IsSingleDelete = true;
			
		if ( ListMode_FromOthers == ListMode )
		{
			var idObject = $(this).attr( "idObject" );
			var idShareCase = $(this).attr( "idShareCase" );
			var idUser = $(this).attr( "idUser" );
			var idGroup = $(this).attr( "idGroup" );
			var idStorage = $(this).attr( "idStorage" );
			var Para_Str = [];
			
			Para_Str[ 0 ] = [];
			Para_Str[ 0 ][ 'idUser' ] = idUser;
			Para_Str[ 0 ][ 'idObject' ] = idObject;
			Para_Str[ 0 ][ 'idShareCase' ] = idShareCase;
			Para_Str[ 0 ][ 'idGroup' ] = idGroup;
			Para_Str[ 0 ][ 'idStorage' ] = idStorage;
			
			$(this).addClass( "DeleteMe" );		// 由於這個指令是一次刪除多筆資料，所以可以用 AddClass 將所有物件標籤起來，在 Ajax 函式最後做動畫移除UI元件
					
			Ajax_DelMyShare_FromOther( Para_Str );
		}
		else
		{
			$(this).addClass('DeleteMe');
			var idType = $(this).attr( "idType" );
			var idObject = $(this).attr( "idObject" );
			var idShareCase = $(this).attr( "idShareCase" );
			
			
			// Raymond 說不要動畫，太慢，看起來會誤認為是效能不好
			//$('.DeleteMe').parent().fadeOut( 1000, function() 
			//{
				$('.DeleteMe').parent().remove();
			//} );
			
			// 公開分享
			if ( 0 == idType )
			{
				var DeleteShareArray = [];
				DeleteShareArray[0] = [];
				DeleteShareArray[0]['ID'] = idObject;
				DeleteShareArray[0]['ShareCase'] = idShareCase;
				Ajax_DeletePublicShare_MyAllList( AccessKey, DeleteShareArray );
			}
			// User
			else if ( 1 == idType )
			{
				var idUser = [];
				idUser[0] = $(this).attr( "idUser" );
				Ajax_RemoveUserfromShare_MyAllList( AccessKey, idObject, idShareCase, idUser );
			}
			// Group
			else
			{
				var idGroup = [];
				idGroup[0] = $(this).attr( "idGroup" );
				Ajax_RemoveGroupfromShare_MyAllList( AccessKey, idObject, idShareCase, idGroup );
			}
		}
	} );
	
	
	
	
	// 點檔案或目錄會跳頁面過去
	$('.h4_ShareListRow_FileName_FromOther').click( function( e ) 
	{
		// 根據元件所記錄的 Row Index 來做比對，找出排序之前應該是對應的應該是哪一個 window.top.ShareFors
		var RowIndex_GoWhere = $(this).attr( "RowIndex" );
		
		// 點在Header要排除
		if ( false == $(this).hasClass( "RowHeader" ) )
		{
			// 「別人分享給我」
			if ( ListMode_FromOthers == ListMode )
			{
				ShareCount = 0;
				
				$.each( ShareFors, function ()
				{
					if ( ShareFors[ShareCount]['RowNum'] == RowIndex_GoWhere ) 
					{
						ShareFrom( RowIndex_GoWhere );
                                                return;
					}
					
					ShareCount++;
				} );
			}
		}
	} );
}

function ShareFrom(index)
{    
    switch(ShareFors[index]['san'])
    {
        case 0:
            LocalShareFrom(index)
            break;
        case 1:
            SANShareFrom(index);
            break;
    }
}

function LocalShareFrom(index)
{
     window.location.href = 'content_pc.php?sid=' +  AccessKey + '#/' + index + '/?act=5&idR=' + ShareFors[index]['idObject'] + '&idO=' + ShareFors[index]['idObject'] + '&idU=' + ShareFors[index]['idUser'] + '&code=' + ShareFors[index]['codeSecurity'] + '&sharecase=' + ShareFors[index]['idShareCase']
}

function SANShareFrom(index)
{
    var bol_check = false;
    var code = '';
    var redirecturl = '';
               
    var postData = '{ "sid":"'+ AccessKey + '","stgid":' + ShareFors[index]['stgid'] +'}';        
    $("body").css("cursor", "wait");            
    var request = CallAjax_NoAsync("ShareCmdProcessPost.php?act=CheckShareLocation", postData, "json");    
    request.done(function (msg, statustext, jqxhr) {               
        if(msg['result'] != 0)
        {    
            switch(msg['result'])
            {
                case -100 :
                case -101 :
                    alert('Fail');
                    break;
                default :
                    alert('Something Error(Result:'+msg['result'] + ')');
            }
        }
        else
        {                
            if(msg['local'] == 1)
            {
                bol_check = CheckShare(index);
                if(bol_check)
                    redirecturl = 'content_san.php?sid=' + AccessKey + '#/' + index + '/?act=5&idR=' + ShareFors[index]['idObject'] + '&idO=' + ShareFors[index]['idObject'] + '&idU=' + ShareFors[index]['idUser'] + '&code=' + ShareFors[index]['codeSecurity'] + '&sharecase=' + ShareFors[index]['idShareCase'];
            }
            else
            {
                bol_check = true;
                redirecturl = 'https://' + msg['stgip'] + '/SANRedirect.php?idR=' + ShareFors[index]['idObject'] + '&idO=' + ShareFors[index]['idObject'] + '&idU=' + ShareFors[index]['idUser'] + '&sharecase=' + ShareFors[index]['idShareCase'] + '&sanid=' + msg['sanid'] + '&sid=' + sid;
            }
        }           
        $("body").css("cursor", "default");                         
    });
    request.fail(function (jqxhr, textStatus) {         
        $("body").css("cursor", "default");  
        alert("Fail Check");                
    });
    if(bol_check)                
        window.location.href = redirecturl;    
}

function CallAjax_NoAsync(url, data, datatype) {
    var request = $.ajax({
        type: "POST",
        async: false,
        url: url,
        data: data,
        dataType: datatype
    });
    return request;
}





function CheckShare(index)
{                    
    var bol_check = false;
    var postData = '{ "sid":"'+ AccessKey + '","stgid":' + ShareFors[index]['stgid'] + ',"idR":' + ShareFors[index]['idObject'] + ',"idO":' + ShareFors[index]['idObject'] + ',"idU":' + ShareFors[index]['idUser'] + ',"sharecase":' + ShareFors[index]['idShareCase']  +'}';            
    $("body").css("cursor", "wait");           
    var request = CallAjax_NoAsync("ShareCmdProcessPost.php?act=CheckSANShare", postData, "json");
	
	
	
    request.done(function (msg, statustext, jqxhr)
	{  
        if(msg['result'] != 0)
        {    
            switch(msg['result'])
            {
                case -100 :
                case -101 :
                    alert('Fail');
                    break;
                default :
                    alert('Something Error(Result:'+msg['result'] + ')');
            }
        }
        else
        {                            
            bol_check = true;                 
            ShareFors[index]['codeSecurity'] = msg['code'];
        }           
        $("body").css("cursor", "default");                           
    });
	
	
	
    request.fail(function (jqxhr, textStatus)
	{         
        $("body").css("cursor", "default");          
        alert("Fail Check");        
    });
	
	
	
    return bol_check;
}


// 判斷是否有資料存在，是否要顯示「目前沒有檔案分享......」的字
function ChkIsAnyDataExist()
{
	var DataCounter = 0;
	
	// 判斷是否還有資料，或是已經沒有資料存在
	$.each( $('.Input_ShareListRow_Target'), function()
	{
		if ( false == $(this).hasClass( "ChkBoxSelectAll" ) )
		{
			DataCounter++;
		}
	} );
	
	if ( 0 == DataCounter )
	{
		return false;
	}
	else
	{
		return true;
	}
}



// 重新排版
function reLocateLayout()
{
	var NewLeft;
	var NewTop;
	var NewHeight;
	var BrowserHeight = self.innerHeight;
	var BrowserWidth = self.innerWidth;
	
	// 搜尋按鈕的排版
	$('#h5_BtnSearch').css( "left", $('#Div_ShareList').width() + $('#Div_ShareList').position().left - $('#h5_BtnSearch').width() );
	$('#Input_SearchKeyword').css( "left", $('#h5_BtnSearch').position().left - $('#Input_SearchKeyword').width() - 20 );
}










function CallAjax(url, data, datatype)
{
    var request = $.ajax(
	{
        type: "POST",
        url: url,
        data: data,
        dataType: datatype
    });
    return request;
}