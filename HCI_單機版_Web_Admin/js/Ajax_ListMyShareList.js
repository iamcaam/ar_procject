var ShareFors;
// Ajax 取回我的分享的清單
function Ajax_ListMyShare_FromMe()
{
    var jsonrequest = 
	'{"accesskey":"' + AccessKey + 
	'"}';
	
	if ( true == ShowAjaxDebug )
	{
		alert( "Ajax_ListMyShare_FromMe(Send):" + jsonrequest );
	}
	
	
	
    var request = CallAjax("ShareCmdProcessPost.php?act=ListShares", jsonrequest , "json");
	
	
	
    request.done(function (msg, statustext, jqxhr) 
	{
		IsShareExist = false;
		var ShareCount = 0;
		var HtmlStr = "";
		List_ShareFromMe = [];
		
		if ( true == ShowAjaxDebug )
		{
			alert( "Ajax_ListMyShare_FromMe(Rcv):" + jqxhr.responseText );
		}
		
        $.each( msg, function( index, element ) 
        {
            switch(index)
            {
                case 'result':
                    if(element == 0)
					{
                    }
                    else if(element < 0 && element > -99)
					{
                        alert( LanguageStr_MyShare.AjaxMsg00 + element);
                    }   
                    else if(element <= -100 && element > -200)
					{
                        alert( LanguageStr_MyShare.AjaxMsg01 + element);
                    }
                    else if(element <= -200 && element > -300)
					{
                        alert( LanguageStr_MyShare.AjaxMsg02 + element);
                    }     
                    else if(element <= -300 && element > -400)
					{
                        alert( LanguageStr_MyShare.AjaxMsg03 + element);
                    }
                    else
					{
                        alert( LanguageStr_MyShare.AjaxMsg04 + element);
                    }
                    break;      
					
					      
                case 'Shares':
					
					SortFlag = SortFlag_File_Asc;
					       
					// 逐一 Parse	  
                    $.each(element,function(index, element)
                    {
						// 資料轉文字的變數
						var FileOrDirName = "";
						var UserOrUgName = "";
						var ShareCaseStr = "";
						var ShareDateTimeStr = "";
						var ImageStr = "";
						var TmpHtmlStr = "";
						List_ShareFromMe[ ShareCount ] = [];
						
						IsShareExist = true;
						
						// 模式
						if ( ShareCase_UploadDownloadBrowse == element.idShareCase )
						{
							ShareCaseStr = LanguageStr_MyShare.ShareCaseStr00;
						}
						else if ( ShareCase_DownloadBrowse == element.idShareCase )
						{
							ShareCaseStr = LanguageStr_MyShare.ShareCaseStr01;
						}
						else if ( ShareCase_UploadBrowse == element.idShareCase )
						{
							ShareCaseStr = LanguageStr_MyShare.ShareCaseStr02;
						}
						else if ( ShareCase_PublicShare == element.idShareCase )
						{
							ShareCaseStr = LanguageStr_MyShare.ShareCaseStr03;
						}
						
						// 檔案名稱
						if ( 1 == element.isFile )
						{
							FileOrDirName = element.nameObject + "";
						}
						else
						{
							FileOrDirName = element.nameObject + LanguageStr_MyShare.IsFolder;
						}
						
						// 分享結束時間
						if ( null == element.timeEnd ) 
						{
							ShareDateTimeStr = LanguageStr_MyShare.ShareDateTimeStr; 
						}
						else
						{
							ShareDateTimeStr = element.timeEnd;
						}
						
						
						// 分享者的分類
                        switch(element.type)
                        {
                            // Public
                            case ShareTypr_Public:
								//  人名
								UserOrUgName = LanguageStr_MyShare.PublicShare;
								ImageStr = "img/Globe_20.png";
								TmpHtmlStr = ' RowNum = "' + ShareCount + '" idType = "' + element.type + '" + idObject = "' + element.idObject + '" idShareCase = "' + element.idShareCase + '" idUser = "0" idGroup = "0"';
                                break;
								
								
								
							// User Assigned
                            case ShareTypr_ByUser:
								//  人名
								UserOrUgName = element.nameUserLast + " " + element.nameUser;
								ImageStr = "img/User_20.png";
								TmpHtmlStr = ' RowNum = "' + ShareCount + '" idType = "' + element.type + '" + idObject = "' + element.idObject + '" idShareCase = "' + element.idShareCase + '" idUser = "' + element.idUser + '" idGroup = "0"';
							    break;
								
								
								
                            // Group Assigned
                            case ShareTypr_ByGroup:
								//  人名
								UserOrUgName = element.nameGroup;
								ImageStr = "img/Group_20.png";
								TmpHtmlStr = ' RowNum = "' + ShareCount + '" idType = "' + element.type + '" + idObject = "' + element.idObject + '" idShareCase = "' + element.idShareCase + '" idUser = "0" idGroup = "' + element.idGroup + '"';
                                break;
                        }
						
						// 儲存資料
						List_ShareFromMe[ ShareCount ] = [];
						List_ShareFromMe[ ShareCount ][ "idObject" ] = element.idObject;
						List_ShareFromMe[ ShareCount ][ "UserOrUgName" ] = UserOrUgName;
						List_ShareFromMe[ ShareCount ][ "ShareCaseStr" ] = ShareCaseStr;
						List_ShareFromMe[ ShareCount ][ "ShareDateTimeStr" ] = ShareDateTimeStr;
						List_ShareFromMe[ ShareCount ][ "FileOrDirName" ] = FileOrDirName;
						List_ShareFromMe[ ShareCount ][ "ImageStr" ] = ImageStr;
						List_ShareFromMe[ ShareCount ][ "RowNum" ] = ShareCount;
						List_ShareFromMe[ ShareCount ][ "TmpHtmlStr" ] = TmpHtmlStr;
						
						
						ShareCount++;
                    });

					break;
            
			}		// switch(index)
			
        });		// $.each( msg, function( index, element )
		
		List_ShareFromMe_Search = List_ShareFromMe.slice(0);
		List_ShareFromMe_ShowDataToHtml();
    });
    request.fail(function (jqxhr, textStatus)
	{
        alert( LanguageStr_MyShare.AjaxMsg05 + jqxhr.responseText);
    });
}





// 根據排序變數的數值來進行製作資料畫面
function List_ShareFromMe_ShowDataToHtml()
{
	ShareCount = 0;
	var HtmlStr= "";
	var List_ShareFromMe_Copy = List_ShareFromMe.slice(0);

	switch ( SortFlag )
	{
		// （被）分享者
		case SortFlag_Who_Asc:
			List_ShareFromMe_Copy.sort( function(a,b)
			{	
				return a["UserOrUgName"].toUpperCase() < b["UserOrUgName"].toUpperCase() ? -1 : ( a["UserOrUgName"].toUpperCase() > b["UserOrUgName"].toUpperCase() ? 1 : 0 );
			});
			break;
			
		case SortFlag_Who_Desc:
			List_ShareFromMe_Copy.sort( function(a,b)
			{
				return a["UserOrUgName"].toUpperCase() > b["UserOrUgName"].toUpperCase() ? -1 : ( a["UserOrUgName"].toUpperCase() < b["UserOrUgName"].toUpperCase() ? 1 : 0 );
			});
			break;
			
			
		// 檔案	
		case SortFlag_File_Asc:
			List_ShareFromMe_Copy.sort( function(a,b)
			{
				return a["FileOrDirName"].toUpperCase() < b["FileOrDirName"].toUpperCase() ? -1 : ( a["FileOrDirName"].toUpperCase() > b["FileOrDirName"].toUpperCase() ? 1 : 0 );
			});
			break;
			
		case SortFlag_File_Desc:
			List_ShareFromMe_Copy.sort( function(a,b)
			{
				return a["FileOrDirName"].toUpperCase() > b["FileOrDirName"].toUpperCase() ? -1 : ( a["FileOrDirName"].toUpperCase() < b["FileOrDirName"].toUpperCase() ? 1 : 0 );
			});
			break;
			
			
		// 模式
		case SortFlag_ShareMode_Asc :
			List_ShareFromMe_Copy.sort( function(a,b)
			{
				return a["ShareCaseStr"].toUpperCase() < b["ShareCaseStr"].toUpperCase() ? -1 : ( a["ShareCaseStr"].toUpperCase() > b["ShareCaseStr"].toUpperCase() ? 1 : 0 );
			});
			break;
			
		case SortFlag_ShareMode_Desc :
			List_ShareFromMe_Copy.sort( function(a,b)
			{
				return a["ShareCaseStr"].toUpperCase() > b["ShareCaseStr"].toUpperCase() ? -1 : ( a["ShareCaseStr"].toUpperCase() < b["ShareCaseStr"].toUpperCase() ? 1 : 0 );
			});
			break;
			
			
		// 時間
		case SortFlag_PeriodTime_Asc :
			List_ShareFromMe_Copy.sort( function(a,b)
			{
				return a["ShareDateTimeStr"].toUpperCase() < b["ShareDateTimeStr"].toUpperCase() ? -1 : ( a["ShareDateTimeStr"].toUpperCase() > b["ShareDateTimeStr"].toUpperCase() ? 1 : 0 );
			});
			break;
			
		case SortFlag_PeriodTime_Desc :
			List_ShareFromMe_Copy.sort( function(a,b)
			{
				return a["ShareDateTimeStr"].toUpperCase() > b["ShareDateTimeStr"].toUpperCase() ? -1 : ( a["ShareDateTimeStr"].toUpperCase() < b["ShareDateTimeStr"].toUpperCase() ? 1 : 0 );
			});
			break;
	}
	
	
	$.each( List_ShareFromMe_Copy, function( index, element ) 
	{
		HtmlStr = HtmlStr + '<li>';
			HtmlStr = HtmlStr + '<div class = "Div_ShareList_Row">';
				HtmlStr = HtmlStr + '<input class = "Input_ShareListRow_Target" type = "checkbox"' + List_ShareFromMe_Copy[ ShareCount ][ "TmpHtmlStr" ] + ' ></input>';
				HtmlStr = HtmlStr + '<img class = "Img_ShareListRow_Target" src="' + List_ShareFromMe_Copy[ ShareCount ][ "ImageStr" ] + '" ></img>';
				HtmlStr = HtmlStr + '<h4 class = "h4_ShareListRow_TargetName ShortCutText">' + List_ShareFromMe_Copy[ ShareCount ][ "UserOrUgName" ] + '</h4>';
				HtmlStr = HtmlStr + '<h4 class = "h4_ShareListRow_FileName ShortCutText">' + List_ShareFromMe_Copy[ ShareCount ][ "FileOrDirName" ] + '</h4>';
				HtmlStr = HtmlStr + '<h4 class = "h4_ShareListRow_Mode ShortCutText">' + List_ShareFromMe_Copy[ ShareCount ][ "ShareCaseStr" ] + '</h4>';
				HtmlStr = HtmlStr + '<h4 class = "h4_ShareListRow_DateTime ShortCutText">' + List_ShareFromMe_Copy[ ShareCount ][ "ShareDateTimeStr" ] + '</h4>';
				HtmlStr = HtmlStr + '<img class = "Img_ShareListRow_Remove ShortCutText"' + List_ShareFromMe_Copy[ ShareCount ][ "TmpHtmlStr" ] + '" src="img/Remove.png" alt="Remove" />';
				HtmlStr = HtmlStr + '<h4 class = "h4_ShareListRow_RemoveResult ShortCutText">' + "" + '</h4>';
			HtmlStr = HtmlStr + '</div>';
		HtmlStr = HtmlStr + '</li>';
		
		ShareCount++;
	} );
	
	HtmlStr = HtmlStr + '<h4 class = "h4_ShareList_NoShare">' + LanguageStr_MyShare.NoFileShareOutNow + '</h4>';
	$('#Div_ShareList').html( HtmlStr );
	
	if ( true == IsShareExist )
	{
		$('.h4_ShareList_NoShare').hide();
		$('.ChkBoxSelectAll').show();
		RegEvt_AfterUiGenerated();
	}
	else
	{
		$('.ChkBoxSelectAll').hide();
	}
}





// 取回別人分享給我的清單
function Ajax_ListMyShare_FromOther()
{
    var jsonrequest = 
	'{"accesskey":"' + AccessKey + 
	'"}';
	
	if ( true == ShowAjaxDebug )
	{
		alert( "Ajax_ListMyShare_FromOther(Send):" + jsonrequest );
	}
	
    var request = CallAjax("ShareCmdProcessPost.php?act=ListShareFor", jsonrequest , "json");   
	
    request.done( function (msg, statustext, jqxhr ) 
	{
		IsShareExist = false;
		var ShareCount = 0;
		var HtmlStr = "";
		List_ShareFromOther = [];
		
		if ( true == ShowAjaxDebug )
		{
			alert( "Ajax_ListMyShare_FromOther(Rcv):" + jqxhr.responseText );
		}
		
        $.each( msg, function( index, element ) 
        {		
            switch(index)
            {
                case 'result':
                    if(element == 0)
					{
                    }
                    else if(element < 0 && element > -99)
					{
                        alert( LanguageStr_MyShare.AjaxMsg10 + element);
                    }   
                    else if(element <= -100 && element > -200)
					{
                        alert( LanguageStr_MyShare.AjaxMsg11 + element);
                    }
                    else if(element <= -200 && element > -300)
					{
                        alert( LanguageStr_MyShare.AjaxMsg12 + element);
                    }     
                    else if(element <= -300 && element > -400)
					{
                        alert( LanguageStr_MyShare.AjaxMsg13 + element);
                    }
                    else
					{
                        alert( LanguageStr_MyShare.AjaxMsg14 + element);
                    }
                    break;      
					
					      
                case 'Shares':
					
                    ShareFors = [];
					SortFlag = SortFlag_File_Asc;
					
					// 逐一 Parse	              
                    $.each(element,function(index, element)
                    {
						// 資料轉文字的變數
						var FileOrDirName = "";
						var UserOrUgName = "";
						var ShareCaseStr = "";
						var ShareDateTimeStr = "";
						var ImageStr = "";
						var TmpHtmlStr = "";
						List_ShareFromOther[ ShareCount ] = [];
						
						IsShareExist = true;
						
						// 模式
						if ( ShareCase_UploadDownloadBrowse == element.idShareCase )
						{
							ShareCaseStr = LanguageStr_MyShare.ShareCaseStr00;
						}
						else if ( ShareCase_DownloadBrowse == element.idShareCase )
						{
							ShareCaseStr = LanguageStr_MyShare.ShareCaseStr01;
						}
						else if ( ShareCase_UploadBrowse == element.idShareCase )
						{
							ShareCaseStr = LanguageStr_MyShare.ShareCaseStr02;
						}
						else if ( ShareCase_PublicShare == element.idShareCase )
						{
							ShareCaseStr = LanguageStr_MyShare.ShareCaseStr03;
						}
						
						// 檔案名稱
						if ( 1 == element.isFile )
						{
							FileOrDirName = element.nameObject + "";
						}
						else
						{
							FileOrDirName = element.nameObject + LanguageStr_MyShare.IsFolder;
						}
						
						// 分享結束時間
						if ( null == element.timeEnd ) 
						{
							ShareDateTimeStr = LanguageStr_MyShare.ShareDateTimeStr;
						}
						else
						{
							ShareDateTimeStr = element.timeEnd;
						}
						
						// 別人分享給我不會有群組對象，也不會顯示公開分享，但，會有idGroup，刪除時會用到
						UserOrUgName = element.nameUserLast + " " + element.nameUser;
						ImageStr = "img/User_20.png";
						TmpHtmlStr = ' RowNum = "' + ShareCount + '" idType = "' + element.type + '" + idObject = "' + 
										element.idObject + '" idShareCase = "' + element.idShareCase + '" idUser = "' + 
										element.idUser + '" idGroup = "' + element.idGroup +'" idStorage = "' + element.stgid + '"';
						
						 
						ShareFors[ShareCount] = Array(10);
						ShareFors[ShareCount]['nameUser'] = element.nameAccount;
						ShareFors[ShareCount]['nameObject'] = element.nameObject;
						ShareFors[ShareCount]['isFile'] = element.isFile;
						ShareFors[ShareCount]['idShareCase'] = element.idShareCase;
						ShareFors[ShareCount]['idUser'] = element.idUser;
						ShareFors[ShareCount]['idObject'] = element.idObject;
						ShareFors[ShareCount]['codeSecurity'] = element.codeSecurity;
						ShareFors[ShareCount]['san'] = element.san;
						ShareFors[ShareCount]['stgid'] = element.stgid;
						ShareFors[ShareCount]['RowNum'] = ShareCount;
						
						// window.top.ShareFors[ShareCount]['RowNum'] 紀錄著現在第幾筆
						// List_ShareFromOther[ ShareCount ][ "RowNum" ] 也紀錄著現在第幾筆
						// 
						// 經過排序之後，在畫面上的順序就變了，但是 RowNum 則記錄著最初資料讀出的時候的順序，當點選分享頁面（別人分享給我）的檔案或資料夾
						// 的時候，會在 $('.h4_ShareListRow_FileName_FromOther').click() 事件做比對動作，找出原本該使用第幾筆資料（順位）來跳頁
						 

						// 儲存資料
						List_ShareFromOther[ ShareCount ] = [];
						List_ShareFromOther[ ShareCount ][ "idObject" ] = element.idObject;
						List_ShareFromOther[ ShareCount ][ "UserOrUgName" ] = UserOrUgName;
						List_ShareFromOther[ ShareCount ][ "ShareCaseStr" ] = ShareCaseStr;
						List_ShareFromOther[ ShareCount ][ "ShareDateTimeStr" ] = ShareDateTimeStr;
						List_ShareFromOther[ ShareCount ][ "ImageStr" ] = ImageStr;
						List_ShareFromOther[ ShareCount ][ "RowNum" ] = ShareCount;
						List_ShareFromOther[ ShareCount ][ "FileOrDirName" ] = FileOrDirName;
						List_ShareFromOther[ ShareCount ][ "TmpHtmlStr" ] = TmpHtmlStr;
						
						ShareCount++;
                    });

					break;
            
			}		// switch(index)
			
        });		// $.each( msg, function( index, element )
		
		List_ShareFromOther_Search = List_ShareFromOther.slice(0);
		List_ShareFromOther_ShowDataToHtml();
		
    });
    request.fail(function (jqxhr, textStatus)
	{
        alert( LanguageStr_MyShare.AjaxMsg15 + jqxhr.responseText);
    });
}






// 根據排序變數的數值來進行製作資料畫面
function List_ShareFromOther_ShowDataToHtml()
{
	ShareCount = 0;
	var HtmlStr= "";
	var List_ShareFromOther_Copy = List_ShareFromOther.slice(0);
	
	switch ( SortFlag )
	{
		// （被）分享者
		case SortFlag_Who_Asc:
			List_ShareFromOther_Copy.sort( function(a,b)
			{	
				return a["UserOrUgName"].toUpperCase() < b["UserOrUgName"].toUpperCase() ? -1 : ( a["UserOrUgName"].toUpperCase() > b["UserOrUgName"].toUpperCase() ? 1 : 0 );
			});
			break;
			
		case SortFlag_Who_Desc:
			List_ShareFromOther_Copy.sort( function(a,b)
			{
				return a["UserOrUgName"].toUpperCase() > b["UserOrUgName"].toUpperCase() ? -1 : ( a["UserOrUgName"].toUpperCase() < b["UserOrUgName"].toUpperCase() ? 1 : 0 );
			});
			break;
			
			
		// 檔案	
		case SortFlag_File_Asc:
			List_ShareFromOther_Copy.sort( function(a,b)
			{
				return a["FileOrDirName"].toUpperCase() < b["FileOrDirName"].toUpperCase() ? -1 : ( a["FileOrDirName"].toUpperCase() > b["FileOrDirName"].toUpperCase() ? 1 : 0 );
			});
			break;
			
		case SortFlag_File_Desc:
			List_ShareFromOther_Copy.sort( function(a,b)
			{
				return a["FileOrDirName"].toUpperCase() > b["FileOrDirName"].toUpperCase() ? -1 : ( a["FileOrDirName"].toUpperCase() < b["FileOrDirName"].toUpperCase() ? 1 : 0 );
			});
			break;
			
			
		// 模式
		case SortFlag_ShareMode_Asc :
			List_ShareFromOther_Copy.sort( function(a,b)
			{
				return a["ShareCaseStr"].toUpperCase() < b["ShareCaseStr"].toUpperCase() ? -1 : ( a["ShareCaseStr"].toUpperCase() > b["ShareCaseStr"].toUpperCase() ? 1 : 0 );
			});
			break;
			
		case SortFlag_ShareMode_Desc :
			List_ShareFromOther_Copy.sort( function(a,b)
			{
				return a["ShareCaseStr"].toUpperCase() > b["ShareCaseStr"].toUpperCase() ? -1 : ( a["ShareCaseStr"].toUpperCase() < b["ShareCaseStr"].toUpperCase() ? 1 : 0 );
			});
			break;
			
			
		// 時間
		case SortFlag_PeriodTime_Asc :

			List_ShareFromOther_Copy.sort( function(a,b)
			{
				return a["ShareDateTimeStr"].toUpperCase() < b["ShareDateTimeStr"].toUpperCase() ? -1 : ( a["ShareDateTimeStr"].toUpperCase() > b["ShareDateTimeStr"].toUpperCase() ? 1 : 0 );
			});
			break;
			
		case SortFlag_PeriodTime_Desc :
			List_ShareFromOther_Copy.sort( function(a,b)
			{
				return a["ShareDateTimeStr"].toUpperCase() > b["ShareDateTimeStr"].toUpperCase() ? -1 : ( a["ShareDateTimeStr"].toUpperCase() < b["ShareDateTimeStr"].toUpperCase() ? 1 : 0 );
			});
			break;
	}
	
	
	$.each( List_ShareFromOther_Copy, function( index, element ) 
	{	
		HtmlStr = HtmlStr + '<li>';
			HtmlStr = HtmlStr + '<div class = "Div_ShareList_Row RowIndex' + List_ShareFromOther_Copy[ ShareCount ][ "RowNum" ] + '">';
				HtmlStr = HtmlStr + '<input class = "Input_ShareListRow_Target" type = "checkbox"' + List_ShareFromOther_Copy[ ShareCount ][ "TmpHtmlStr" ] + ' ></input>';
				HtmlStr = HtmlStr + '<img class = "Img_ShareListRow_Target" src="' + List_ShareFromOther_Copy[ ShareCount ][ "ImageStr" ] + '" ></img>';
				HtmlStr = HtmlStr + '<h4 class = "h4_ShareListRow_TargetName ShortCutText">' + List_ShareFromOther_Copy[ ShareCount ][ "UserOrUgName" ] + '</h4>';
				HtmlStr = HtmlStr + '<h4 class = "h4_ShareListRow_FileName ShortCutText h4_ShareListRow_FileName_FromOther" RowIndex = "' + List_ShareFromOther_Copy[ ShareCount ][ "RowNum" ] + '" >' + List_ShareFromOther_Copy[ ShareCount ][ "FileOrDirName" ] + '</h4>';
				HtmlStr = HtmlStr + '<h4 class = "h4_ShareListRow_Mode ShortCutText">' + List_ShareFromOther_Copy[ ShareCount ][ "ShareCaseStr" ] + '</h4>';
				HtmlStr = HtmlStr + '<h5 class = "h4_ShareListRow_DateTime ShortCutText">' + List_ShareFromOther_Copy[ ShareCount ][ "ShareDateTimeStr" ] + '</h5>';
				HtmlStr = HtmlStr + '<img class = "Img_ShareListRow_Remove"' + List_ShareFromOther_Copy[ ShareCount ][ "TmpHtmlStr" ] + '" src="img/Remove.png" alt="Remove" />';
			HtmlStr = HtmlStr + '</div>';
		HtmlStr = HtmlStr + '</li>';
						
		ShareCount++;
	} );
	
	HtmlStr = HtmlStr + '<h4 class = "h4_ShareList_NoShare">' + LanguageStr_MyShare.NoFileShareInNow + '</h4>';
	$('#Div_ShareList').html( HtmlStr );
	
	if ( true == IsShareExist )
	{
		$('.h4_ShareList_NoShare').hide();
		$('.ChkBoxSelectAll').show();
		RegEvt_AfterUiGenerated();
	}
	else
	{
		$('.ChkBoxSelectAll').hide();
	}
}







// 跟原本的函式分開，因為要走不同的 UI 反應流程
function Ajax_DeletePublicShare_MyAllList( accesskey, shares )
{
    var jsonrequest = 
	'{"accesskey":"' + accesskey +  
	'","Shares":[';     
    $.each(shares,function(index,element)
	{
        if(index == 0)
            jsonrequest += '{"id":' + element.ID + ',"sharecase":' + element.ShareCase + '}';
        else
            jsonrequest += ',{"id":' + element.ID + ',"sharecase":' + element.ShareCase + '}';
    })
    jsonrequest += ']}';
	
	if ( true == ShowAjaxDebug )
	{
		alert( "Ajax_DeletePublicShare_MyAllList(Send):" + jsonrequest );
	}
	
    var request = CallAjax("ShareCmdProcessPost.php?act=Delete", jsonrequest , "json");
	    
    request.done(function (msg, statustext, jqxhr) 
	{
		var IsThereHasFail = false;
						
		if ( true == ShowAjaxDebug )
		{
			alert( "Ajax_DeletePublicShare_MyAllList(Rcv):" + jqxhr.responseText );
		}
		
        $.each( msg, function( index, element ) 
        {                           
            switch(index)
            {
                case 'result':
                    if(element == 0)
					{
                        
                    }
                    else if(element < 0 && element > -99)
					{
						IsThereHasFail = true;
                        alert( LanguageStr_MyShare.AjaxMsg20 );
                    }   
                    else if(element <= -100 && element > -200)
					{
						IsThereHasFail = true;
                        alert( LanguageStr_MyShare.AjaxMsg21 );
                    }        
                    else if(element <= -200 && element > -300)
					{
						IsThereHasFail = true;
                        alert( LanguageStr_MyShare.AjaxMsg22 );
                    }        
                    else if(element <= -300 && element > -400)
					{
                        alert( LanguageStr_MyShare.AjaxMsg23 + element);
                    }
                    else
					{
                        alert( LanguageStr_MyShare.AjaxMsg24 + element);
                    }
                    break;
					
                case 'Shares':
				
                    $.each( element,function( index, element )
					{
						if ( 0 != element.result )
						{
							alert( LanguageStr_MyShare.AjaxMsg23 + " , id = " + element.id + ", ShareCase = " + element.sharecase + ", result = " + element.result );
						}
					} );
					
                    break;
            }
        });
		
		if ( true == IsThereHasFail )
		{
			Ajax_ListMyShare_FromMe();
		}
		
		// 一次刪除多資料，會送出多個Ajax，要等到所有指令都回來然後把等待對話框關掉
		ChkMultiAjax();
    });
    request.fail(function (jqxhr, textStatus)
	{
        alert( LanguageStr_MyShare.AjaxMsg25 + jqxhr.responseText);          
    });
}






function Ajax_RemoveUserfromShare_MyAllList( accesskey, id, sharecase, uids )
{
    var jsonrequest = 
	'{"id":' + id + 
	',"accesskey":"' + accesskey + 
	'","sharecase":' + sharecase + 
	',"DelUIDs":[';     
    
	$.each(uids,function(index,element)
	{
        if(index == 0)
            jsonrequest += '{"uid":' + element + '}';
        else
            jsonrequest += ',{"uid":' + element + '}';
    })
    jsonrequest += ']}';
	
	if ( true == ShowAjaxDebug )
	{
		alert( "Ajax_RemoveUserfromShare_MyList(Send):" + jsonrequest );
	}
	
    var request = CallAjax("ShareCmdProcessPost.php?act=RemoveUser", jsonrequest , "json");
	
    request.done(function (msg, statustext, jqxhr) 
	{
		if ( true == ShowAjaxDebug )
		{
			alert( "Ajax_RemoveUserfromShare_MyList(Rcv):" + jqxhr.responseText );
		}

		var IsThereHasFail = false;
		   
        $.each( msg, function( index, element ) 
        {                           
            switch(index)
            {
                case 'result':
				
                    if(element == 0)
					{
                        
                    }
                    else if(element < 0 && element > -99)
					{
						IsThereHasFail = true;
                        alert( LanguageStr_MyShare.AjaxMsg30 );
                    }   
                    else if(element <= -100 && element > -200)
					{
						IsThereHasFail = true;
                        alert( LanguageStr_MyShare.AjaxMsg31 );
                    }
                    else if(element <= -200 && element > -300)
					{
						IsThereHasFail = true;
                        alert( LanguageStr_MyShare.AjaxMsg32 );
                    }     
                    else if(element <= -300 && element > -400)
					{
						IsThereHasFail = true;
                        alert( LanguageStr_MyShare.AjaxMsg33 );
                    }
                    else
					{
						IsThereHasFail = true;
                        alert( LanguageStr_MyShare.AjaxMsg34 + element);
                    }
                    break;     
					          
                case 'DelUIDs':
					
                    $.each( element,function( index, element )
					{
						if ( 0 != element.result )
						{
							IsThereHasFail = true;
							alert( LanguageStr_MyShare.AjaxMsg35 + ", User ID = " + element.uid + ", result = " + element.result );
						}
						
					});
                    break;
            }
        });
		
		
		if ( true == IsThereHasFail )
		{
			Ajax_ListMyShare_FromMe();
		}
		
		// 一次刪除多資料，會送出多個Ajax，要等到所有指令都回來然後把等待對話框關掉
		ChkMultiAjax();
    });
    request.fail(function (jqxhr, textStatus) 
	{
        alert('Ajax fail ' + jqxhr.responseText);              
    });
}




function Ajax_RemoveGroupfromShare_MyAllList( accesskey, id, sharecase, gids )
{
    var jsonrequest = 
	'{"id":' + id + 
	',"accesskey":"' + accesskey +  
	'","sharecase":' + sharecase + 
	',"DelGIDs":['; 
	    
    $.each(gids,function(index,element)
	{
        if(index == 0)
            jsonrequest += '{"gid":' + element + '}';
        else
            jsonrequest += ',{"gid":' + element + '}';
    })
	
    jsonrequest += ']}';
	
	if ( true == ShowAjaxDebug )
	{
		alert( "Ajax_RemoveUserfromShare(Send):" + jsonrequest );
	}
	
    var request = CallAjax("ShareCmdProcessPost.php?act=RemoveGroup", jsonrequest , "json");   
	 
    request.done(function (msg, statustext, jqxhr)
	{ 
		if ( true == ShowAjaxDebug )
		{
			alert( "Ajax_RemoveUserfromShare(Rcv):" + jqxhr.responseText );
		}

		var IsThereHasFail = false;
						 
        $.each( msg, function( index, element ) 
        {                           
            switch(index)
            {
                case 'result':
				
                    if(element == 0)
					{
                        
                    }
                    else if(element < 0 && element > -99)
					{
						IsThereHasFail = true;
                        alert( LanguageStr_MyShare.AjaxMsg40 );
                    }   
                    else if(element <= -100 && element > -200)
					{
						IsThereHasFail = true;
                        alert( LanguageStr_MyShare.AjaxMsg41 );
                    }
                    else if(element <= -200 && element > -300)
					{
						IsThereHasFail = true;
                        alert( LanguageStr_MyShare.AjaxMsg42 );
                    }     
                    else if(element <= -300 && element > -400)
					{
						IsThereHasFail = true;
                        alert( LanguageStr_MyShare.AjaxMsg43 );
                    }
                    else
					{
						IsThereHasFail = true;
                        alert( LanguageStr_MyShare.AjaxMsg44 + element);
                    }
                    break;
					              
                case 'DelGIDs':
                          
                    $.each(element,function(index, element)
                    {
						if ( 0 != element.result )
						{
							IsThereHasFail = true;
							alert( LanguageStr_MyShare.AjaxMsg45 + ", Group name = " + element.groupname + ", Group ID = " + element.gid + ", result = " + element.result );
						}
                    });
                    break;
            }
        });
		
		
		if ( true == IsThereHasFail )
		{
			Ajax_ListMyShare_FromMe();
		}
		
		// 一次刪除多資料，會送出多個Ajax，要等到所有指令都回來然後把等待
		ChkMultiAjax();
    });
    request.fail(function (jqxhr, textStatus) 
	{
        alert( LanguageStr_MyShare.AjaxMsg46 + jqxhr.responseText);              
    });
}




function ChkMultiAjax()
{
	// 一次刪除多資料，會送出多個Ajax，要等到所有指令都回來然後把等待對話框關掉
	if ( false == IsSingleDelete )
	{
		AjaxCounterRcv++;
		
		if ( AjaxCounterRcv == AjaxCounter )
		{	
			$('.Div_WaitAjax').dialog('close');
			Ajax_ListMyShare_FromMe();
		}
	}
}


// 刪除他人可一次刪除個人或群組
function Ajax_DelMyShare_FromOther(shares)
{
	var jsonrequest = '{"accesskey":"' + AccessKey + '"' + ',"Shares":[';
	
	$.each(shares,function(index,element)
	{
		if(index == 0)
		{
			jsonrequest += '{"idUser":' + element.idUser + ',"idObject":' + element.idObject + ',"idShareCase":' + element.idShareCase + ',"idGroup":' + element.idGroup + ',"idStorage":"' + element.idStorage  + '"}';
		}
		else
		{
			jsonrequest += ',{"idUser":' + element.idUser + ',"idObject":' + element.idObject + ',"idShareCase":' + element.idShareCase + ',"idGroup":' + element.idGroup + ',"idStorage":"' + element.idStorage + '"}';
		}
	})
	jsonrequest += ']}';

	
	if ( true == ShowAjaxDebug )
	{
		alert( "Ajax_DelMyShare_FromOther(Send):" + jsonrequest );
	}
	
	var request = CallAjax("ShareCmdProcessPost.php?act=HideShareFor", jsonrequest , "json"); 
	
	request.done(function (msg, statustext, jqxhr) 
	{
		$('.Div_WaitAjax').dialog('close');
		
		if ( true == ShowAjaxDebug )
		{
			alert( "Ajax_DelMyShare_FromOther(Rcv):" + jqxhr.responseText );
		}
		
		var IsThereHasFail = false;
		
		$.each( msg, function( index, element )
		{ 
			switch(index)
			{
				case 'result':
                    if(element == 0)
					{
						// Raymond 說不要動畫，太慢，看起來會誤認為是效能不好
							
						// 由於這個指令是一次刪除多筆資料，所以可以用 AddClass 將所有物件標籤起來，在 Ajax 函式最後做動畫移除UI元件
						//$('.DeleteMe').parent().fadeOut( 1000, function() 
						//{
							$('.DeleteMe').parent().remove();
							
							// 判斷是否有資料存在，是否要顯示「目前沒有檔案分享......」的字
							if ( false == ChkIsAnyDataExist() )
							{
								$('.h4_ShareList_NoShare').show( 1000 );
								$('.ChkBoxSelectAll').hide();
							}
						//} );
                    }
                    else if(element < 0 && element > -99)
					{
						IsThereHasFail = true;
                        alert( LanguageStr_MyShare.AjaxMsg50 );
                    }   
                    else if(element <= -100 && element > -200)
					{
						IsThereHasFail = true;
                        alert( LanguageStr_MyShare.AjaxMsg51 );
                    }
                    else if(element <= -200 && element > -300)
					{
						IsThereHasFail = true;
                        alert( LanguageStr_MyShare.AjaxMsg52 );
                    }     
                    else if(element <= -300 && element > -400)
					{
						IsThereHasFail = true;
                        alert( LanguageStr_MyShare.AjaxMsg53 );
                    }
                    else
					{
						IsThereHasFail = true;
                        alert( LanguageStr_MyShare.AjaxMsg54 + element);
                    }
					break;
					
				case 'Shares':
					
					$.each(element,function(index, element)
					{
						//alert( " User = " + element.idUser + " Obj = " + element.idObject + " Case = " + element.idShareCase + " Result = " + element.result );
						if ( 0 != element.result )
						{
							IsThereHasFail = true;
						}
					});
					
					break;
			}
		});
		
		if ( true == IsThereHasFail )
		{
			alert(  LanguageStr_MyShare.AjaxMsg55 );
			Ajax_ListMyShare_FromOther();
		}
		
	})
	request.fail(function (jqxhr, textStatus)
	{
		$('.Div_WaitAjax').dialog('close');
		
        alert( LanguageStr_MyShare.AjaxMsg56 + jqxhr.responseText);  
	});
}


