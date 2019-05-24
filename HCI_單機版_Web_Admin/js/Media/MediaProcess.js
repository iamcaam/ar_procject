/*
var AlbumInfo = function(_idAlbum, _titleAlbum, _nameArtist){
    this.idAlbum = _idAlbum;
    this.titleAlbum = _titleAlbum;
    this.nameArtist = _nameArtist;
};
var ArtistInfo = function(_idArtist,_nameArtist){
    this.idArtist = _idArtist;
    this.nameArtist = _nameArtist;
};
var FileStructInfo = function(_idParent,_idPath,_idMedia,_name,_type){
    this.idParent = _idParent;
    this.idPath = _idPath;
    this.idMedia = _idMedia;
    this.name = _name;
    this.type = _type;    
}; 
*/
var FavoriteListInfo = function(_idPlaying,_isPlayingQueue,_indexPlaying,_timeShitf,_nameFavoriteList)
{
    this.idPlaying = _idPlaying;
    this.isPlayingQueue = _isPlayingQueue;
    this.indexPlaying = _indexPlaying;
    this.timeShitf = _timeShitf;
    this.nameFavoriteList = _nameFavoriteList;
};

var MediaInfo = function(_idMedia,_idArtist,_idAlbum,_idPath,_nameMediaFile,_nameTitle,_lengthMedia,_pathFull,_nameArtist,_titleAlbum){
    this.idMedia = _idMedia;
    this.idArtist = _idArtist;
    this.idAlbum = _idAlbum;
    this.idPath = _idPath;
    this.nameMediaFile = _nameMediaFile;
    this.nameTitle = _nameTitle;
    this.lengthMedia = _lengthMedia;
    this.pathFull = _pathFull;
    this.nameArtist = _nameArtist;
    this.titleAlbum = _titleAlbum;
};

//
// 注意，自訂屬性名稱不要用 [ Selected ]，否則會跟內建屬性 [ Select ] 沖到
//



// <<< 播放類函式 >>>

// 取得歌曲在磁碟位置，並播放歌曲
// function GetPath_PlaySong( idMedia )

// 播放上一首歌
// function PlayTheQueue_Pre()

// 播放下一首歌
// function PlayTheQueue_Next()

// 取得上一首歌該有的 Index
// function MoveToPreIndex()

// 取得下一首歌該有的 Index
// function MoveToNextIndex()


// <<< 判斷類函式 >>>

// 判斷 播放中歌單 ID
// function GetPlayingQuqueID()

// 判斷 現在選了哪些 專輯
// function CheckWhatAlbumSelected()

// 判斷 現在選了哪 些歌手
// function CheckWhatArtistSelected()

// 判斷 現在選了哪些 歌單
// function CheckWhatFavoriteListSelected()

// 判斷 現在選了哪些 歌單內的歌曲（ Class Name跟下一個函式不同 ）
// function CheckWhatFavoriteListMediaSelected()

// 判斷 現在選了哪些（專輯或歌手內） 歌曲
// function CheckWhatMediaSelected()

// 判斷 現在選了哪些 目錄
// function CheckWhatFolderSelected()

// 檢查使用者選擇什麼，並將所選歌曲加入或取代至「現正播放清單」
// function CheckWhatSelected_InsertToPlayingQueue()



// <<< 取得類函式 >>>

// 取得 所有專輯
// function GetAlbums()

// 取得 某專輯 內所有歌曲
// function GetMediasofAlbum( albumid, titleAlbum )

// 取得 所有歌手
// function GetArtists()

// 取得 某歌手 內所有歌曲
// function GetMediasofArtist( artistid, nameArtist )

// 取得 所有檔案結構
// function GetFileStruct( idPath, PathFolderName, ShowBar )

// 取得 所有歌單清單
// function GetFavoriteList( isPlayingQueue )

// 取得 歌單清單 的所有歌曲檔
// function GetMediasOfFavoriteLists( idPlaying, FavoriteListName )

// 取得 搜尋 相關歌曲
// function Ajax_Search( idUser,Pattern )

// 取得現正播放清單的所有歌曲檔，並判斷是否播放
// function GetMediasOfPlayingQueue_FillDiv_AndPlay( )



// <<< 新增類函式 >>>

// 新增 專輯 到 [歌單]
// function InsertAlbumsToFavoriteList( idPlaying, idAlbums, DeleteFlag )

// 新增 歌手 到 [歌單]
// function InsertArtistsToFavoriteList( idPlaying, idArtists, DeleteFlag )

// 新增 歌曲 到 [歌單]
// function InsertMediasToFavoriteList( idPlaying, Medias, DeleteFlag )

// 新增 目錄 到 [歌單]
// function InsertFoldersToFavoriteList( idPlaying, idPaths, DeleteFlag )

// 新增 [歌單] 到 [現正播放清單]，注意，不能加入歌單到歌單中，後端PHP會回傳 Fail
// function InsertFavoriteListsToPlaying( idPlaying, idFavoriteLists, DeleteFlag )





// <<< Dialog 類函式 >>>

// 取得歌單清單，僅資料，放在公用變數 FavoriteListData[] 中
// function UpdateDialog_GetFavoriteList( )

// 更新對話框的「歌單」清單 ( Div_AddToFavoriteList ) 內容
// function UpdateDialog_AddToFavoriteList()

// 建立對話框「新增歌單」
// function CreateDialog_NewFavoriteList()

// 檢查使用者選擇什麼，並呼叫出選擇現有「歌單」對話框
// function OpenCreateFavoriteListDialog()

// 檢查使用者選擇什麼，並呼叫出選擇現有「歌單」對話框
// function CheckWhatSelected_OpenFavoriteListDialog()




// <<< 畫面控制 >>>

// 播放進度條
// function PlayerProgressControl()

// 工具列按鈕-除了移除按鈕之外，全部顯示或隱
// function ToolBarBtn_AllControl_ButNotRemove( AllEnable )

// 工具列按鈕全部顯示或隱藏
// function ToolBarBtn_AllControl( AllEnable )

// 等候幾秒
// function WaitSeconds( m_seconds )




// 在切換歌曲的時候，才來對刪除歌曲的動作進行處理，將新的歌曲清單送回Server更新
// function CheckPlayQueue_Reflash_UpdateDiv()

// 建立 [ 歌單 ]
// function CreateFavoriteLists( isPlayingQueue, indexPlaying, timeShift, nameFavoriteList )

// 刪除「歌單」
// function DelFavoriteLists( TmpIdFavoriteList )

// 建立播放順序，根據公用變數決定是否亂數排歌曲
// function CreateTheQueue()



var NowFavoriteListID = 0;
	
	

// 等候幾秒
function WaitSeconds( m_seconds )
{
	var StartTime = new Date();
	var CheckTime = new Date();
	
	while ( m_seconds > CheckTime - StartTime )
	{
		CheckTime = new Date();
	}
}



// 取得現在選了哪些專輯
function CheckWhatAlbumSelected()
{
	AlbumId_BeAddToList = [];
	LoopCount = 0;
	
	// 以回圈逐一判斷勾選項目有哪些
	$.each( $('.Img_Album_CheckBox'), function() 
	{
		if ( 'Yes' == ( $(this).attr( 'BeSelected' ) ) )
		{
			AlbumId_BeAddToList[ LoopCount ] = $(this).parent().attr( 'id' );
			LoopCount++;
		}
	});
	
	// 勾選去除
	$('.Img_Album_CheckBox').attr( 'src', 'img/Media/checkbox_empty.png' );
	$('.Img_Album_CheckBox').attr( 'BeSelected', 'No' );
	ToolBarBtn_AllControl_ButNotRemove( false );
	AddListCount = 0;
}



// 取得現在選了哪些歌手
function CheckWhatArtistSelected()
{
	ArtistId_BeAddToList = [];
	LoopCount = 0;
	
	// 以回圈逐一判斷勾選項目有哪些
	$.each( $('.Img_Singer_CheckBox'), function() 
	{
		if ( 'Yes' == ( $(this).attr( 'BeSelected' ) ) )
		{
			ArtistId_BeAddToList[ LoopCount ] = $(this).parent().attr( 'id' );
			LoopCount++;
		}
	});
	
	// 勾選去除
	$('.Img_Singer_CheckBox').attr( 'src', 'img/Media/checkbox_empty.png' );
	$('.Img_Singer_CheckBox').attr( 'BeSelected', 'No' );
	ToolBarBtn_AllControl_ButNotRemove( false );
	AddListCount = 0;
}



// 取得現在選了哪些 歌單
function CheckWhatFavoriteListSelected()
{
	FavoriteListId_BeAddToList = [];
	LoopCount = 0;
	
	// 以回圈逐一判斷勾選項目有哪些
	$.each( $('.Img_FavoriteList_CheckBox'), function() 
	{
		if ( 'Yes' == ( $(this).attr( 'BeSelected' ) ) )
		{
			FavoriteListId_BeAddToList[ LoopCount ] = $(this).attr( 'idPlaying' );
			LoopCount++;
		}
	});
	
	// 勾選去除
	$('.Img_FavoriteList_CheckBox').attr( 'src', 'img/Media/checkbox_empty.png' );
	$('.Img_FavoriteList_CheckBox').attr( 'BeSelected', 'No' );
	ToolBarBtn_AllControl( false );
	AddListCount = 0;
}



// 取得現在選了哪些歌單內的歌曲，要做刪除
function CheckWhatFavoriteListMediaSelected_ToDelete()
{
	MediaId_BeAddToList = [];
	LoopCount = 0;
	
	// 以回圈逐一判斷勾選項目有哪些
	$.each( $('.Img_FavoriteListDetail_CheckBox'), function() 
	{
		if ( 'No' == ( $(this).attr( 'BeSelected' ) ) )
		{
			MediaId_BeAddToList[ LoopCount ] = $(this).attr( 'idMedia' );
			LoopCount++;
		}
	});
	
	// 以回圈逐一判斷勾選項目有哪些
	$.each( $('.Img_FavoriteListDetail_CheckBox'), function() 
	{
		if ( 'Yes' == ( $(this).attr( 'BeSelected' ) ) )
		{
			$(this).parent().parent().remove();
		}
	});
	
	// 勾選去除
	$('.Img_FavoriteListDetail_CheckBox').attr( 'src', 'img/Media/checkbox_empty.png' );
	$('.Img_FavoriteListDetail_CheckBox').attr( 'BeSelected', 'No' );
	ToolBarBtn_AllControl( false );
	AddListCount = 0;
}



// 取得現在選了哪些歌單內的歌曲（Class Name跟下一個函式不同）
function CheckWhatFavoriteListMediaSelected()
{
	MediaId_BeAddToList = [];
	LoopCount = 0;
	
	// 以回圈逐一判斷勾選項目有哪些
	$.each( $('.Img_FavoriteListDetail_CheckBox'), function() 
	{
		if ( 'Yes' == ( $(this).attr( 'BeSelected' ) ) )
		{
			MediaId_BeAddToList[ LoopCount ] = $(this).attr( 'idMedia' );
			LoopCount++;
		}
	});
	
	// 勾選去除
	$('.Img_FavoriteListDetail_CheckBox').attr( 'src', 'img/Media/checkbox_empty.png' );
	$('.Img_FavoriteListDetail_CheckBox').attr( 'BeSelected', 'No' );
	ToolBarBtn_AllControl( false );
	AddListCount = 0;
}



// 取得現在選了哪些歌曲
function CheckWhatMediaSelected()
{
	MediaId_BeAddToList = [];
	LoopCount = 0;
	
	// 以回圈逐一判斷勾選項目有哪些
	$.each( $('.Img_MediaDetail_CheckBox'), function() 
	{
		if ( 'Yes' == ( $(this).attr( 'BeSelected' ) ) )
		{
			MediaId_BeAddToList[ LoopCount ] = $(this).attr( 'idMedia' );
			LoopCount++;
		}
	});
	
	// 勾選去除
	$('.Img_MediaDetail_CheckBox').attr( 'src', 'img/Media/checkbox_empty.png' );
	$('.Img_MediaDetail_CheckBox').attr( 'BeSelected', 'No' );
	ToolBarBtn_AllControl_ButNotRemove( false );
	AddListCount = 0;
}



// 取得現在選了哪些目錄
function CheckWhatFolderSelected()
{
	FolderId_BeAddToList = [];
	LoopCount = 0;
	
	// 以回圈逐一判斷勾選項目有哪些
	$.each( $('.Div_Folder'), function() 
	{
		if ( 'Yes' == ( $(this).children('.Img_Folder_CheckBox').attr( 'BeSelected' ) ) )
		{
			FolderId_BeAddToList[ LoopCount ] = $(this).attr( 'id' );
			LoopCount++;
		}
	});
	
	// 勾選去除
	$('.Img_Folder_CheckBox').attr( 'src', 'img/Media/checkbox_empty.png' );
	$('.Img_Folder_CheckBox').attr( 'BeSelected', 'No' );
	ToolBarBtn_AllControl_ButNotRemove( false );
	AddListCount = 0;
}



// 取得現在選了哪些目錄結構中的歌曲
function CheckWhatMediaInFolderSelected()
{
	MediaId_BeAddToList = [];
	LoopCount = 0;
	
	// 以回圈逐一判斷勾選項目有哪些
	$.each( $('.Img_File_CheckBox' ), function() 
	{
		if ( 'Yes' == ( $(this).attr( 'BeSelected' ) ) )
		{
			MediaId_BeAddToList[ LoopCount ] = $(this).parent().attr( 'id' );
			LoopCount++;
		}
	});
}


// 取得現在選了哪些歌單內的歌曲，將這些歌先做動畫
function SelectedFavoriteListMedia_Animate()
{
	var TotalRemove = 0;
	var index = 0;
	
	$.each( $('.Img_FavoriteListDetail_CheckBox'), function() 
	{
		if ( 0 != index )
		{
			if ( 'Yes' == ( $(this).attr( 'BeSelected' ) ) )
			{
				$(this).addClass( 'NeedRemove' );
				TotalRemove++;
			}
		}
		index++;
	});
	
	
	var index = 0;
	$.each( $('.NeedRemove'), function() 
	{
		if ( index == TotalRemove - 1 )
		{
			$(this).parent().parent().fadeOut( 1000, SelectedFavoriteListMedia_ToRemove );
		}
		else
		{
			$(this).parent().parent().fadeOut( 1000 );
		}
		index++
	});
	
	AddListCount = 0;
}


// 取得現在選了哪些歌單內的歌曲，將這些歌曲移除，並把剩下歌曲記錄到 MediaId_BeAddToList
function SelectedFavoriteListMedia_ToRemove()
{
	// 以回圈逐一判斷勾選項目有
	$.each( $('.NeedRemove'), function() 
	{
		$(this).parent().parent().remove();
	});
	
	index = 0;
	$.each( $('.Img_FavoriteListDetail_CheckBox'), function() 
	{
		if ( 0 != index )
		{
			MediaId_BeAddToList[ index ] = $(this).attr( 'idMedia' );
		}
		index++;
	});
	
	// 勾選去除
	$('.Img_File_CheckBox').attr( 'src', 'img/Media/checkbox_empty.png' );
	$('.Img_File_CheckBox').attr( 'BeSelected', 'No' );
	AddListCount = 0;
	
	InsertMediasToFavoriteList( NowFavoriteListID, MediaId_BeAddToList, true );
}


// 取得現在選了哪些歌單內的歌曲，將這些歌曲移除，並把剩下歌曲記錄到 MediaId_BeAddToList
function CheckRemainFavoriteListMedia()
{
	MediaId_BeAddToList = [];
	LoopCount = 0;
	$.each( $('.Img_FavoriteListDetail_CheckBox'), function() 
	{
		MediaId_BeAddToList[ LoopCount ] = $(this).attr( 'idMedia' );
		LoopCount++;
	});
}








// 取得所有 專輯
function GetAlbums( )
{
	Page_MediaType = Page_MediaType_Albums;

    var rtnresult =
	{
        result : 0,
        albums : null
    };
	
    var postData = 
	{
        Action:'GetAlbum',
        idUser:UserID
    };
	
    var request = CallAjax( 'MediaPost.php', postData, 'json' );
	
    request.done( function ( Msg, statustext, jqxhr ) 
	{
		// 有資料回來則進行後續處理
		if( Msg != null)
		{
			AddListCount = 0;
			var HtmlStr = "";
			
			HtmlStr += '<div class = "Div_BackLevel">';
				HtmlStr += '<img class = "Img_MediaList_ReplacePlayingQueue" src = "img/Media/Replace_PlayingQueue.png" title = "立即播放" />';
				HtmlStr += '<img class = "Img_MediaList_AppendPlayingQueue" src = "img/Media/Append_PlayingQueue.png" title = "將歌曲加入「播放中」" />';
				HtmlStr += '<img class = "Img_MediaList_AppendFavoriteList" src = "img/Media/Append_FavoriteList.png" title = "將歌曲加入現有「歌單」" />';
				HtmlStr += '<img class = "Img_MediaList_KillFavoriteList" src = "img/Media/Kill_FavoriteList.png" title = "移除「歌單」" />';
				HtmlStr += '<img class = "Img_MediaList_RemoveFavoriteMedia" src = "img/Media/Remove_FavoriteMedia.png" title = "將歌曲從「歌單」中移除" />';
			HtmlStr += '</div>';
			
			// 由於後端的 PHP 與資料庫的架構上必須考慮一個專輯有多個歌手，所以回應的訊息中，Array的 Index不會從0開始，這會造成 Msg.length 出問題而變成「未定義」
			// 所以這邊寫法無法跟其他函式寫法一樣。
			var CountItem = 0;
			$.each( Msg, function( index, element ) 
			{
				CountItem++;
			});
			
			if ( 0 < CountItem )
			{
				$.each( Msg, function( index, element ) 
				{
					/* 以新資料更新畫面 */
					HtmlStr += '<div class = "Div_Album" id = "' + element.idAlbum + '" >';
					
						HtmlStr += '<img class ="Img_Album_CheckBox" src="img/Media/checkbox_empty.png" alt="勾選" BeSelected = "No"/>';
						HtmlStr += '<img class = "Img_Album_AlbumImg" src = "img/Media/CD_Pack.png" alt = "' + element.titleAlbum + '" />';
						HtmlStr += '<label class = "Lbl_Album_AlbumName" >' + element.titleAlbum + '</label>';
						HtmlStr += '<label class = "Lbl_Album_ArtistName" >' + element.nameArtist + '</label>';
						HtmlStr += '<img class = "Img_BigItem_ReplacePlayingQueue" src="img/Media/PlayingNow.png" title = "立即播放" />';
				
					HtmlStr += '</div>';
				});
			}
			else
			{
				HtmlStr += '<label style = "position:absolute; left:5%; top:40%" >抱歉，目前不存在任何專輯</label>';
			}
			
				
			$('.Div_AudioListTitle').text( '專輯' );
			$('.Div_AudioListArea').html( HtmlStr );
			ToolBarBtn_AllControl( false );
			
			//////
			////// 事件
			//////
			
			// 圖形綁定 Click 事件，可進入歌曲清單畫面
			$('.Img_Album_AlbumImg').click( function ()
			{
				GetMediasofAlbum( $(this).parent().attr( 'id' ), $(this).parent().children('.Lbl_Album_AlbumName').text() );
			});
			
			
			
			// 將 專輯 元件組的 CheckBox 綁定 Click 事件，做勾選動作
			$( '.Img_Album_CheckBox' ).click( function ()
			{
				// 如果判定是已勾選
				if ( 'Yes' == $(this).attr( 'BeSelected' ) )
				{
					// 做出不勾選的效果
					$(this).attr( 'src', 'img/Media/checkbox_empty.png' );
					$(this).attr( 'BeSelected', 'No' );
					AddListCount--;
					
					if ( 0 >= AddListCount )
					{
						ToolBarBtn_AllControl_ButNotRemove( false );
					}
				}
				// 如果判定非已勾選
				else
				{
					// 做出勾選效果
					$(this).attr( 'src', 'img/Media/checkbox.png' );
					$(this).attr( 'BeSelected', 'Yes' );
					AddListCount++;
					
					if ( 0 <= AddListCount )
					{
						ToolBarBtn_AllControl_ButNotRemove( true );
					}
				}
			});
			
			
			
			// 點選播放，可直接播放專輯內所有歌曲（將此專輯加入播放中歌單，並立刻播放）
			$('.Img_BigItem_ReplacePlayingQueue').click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				// 開啟等待中視窗
				$('.Div_WaitAjax').dialog('open');
					
				var TempIdArray = [];
				TempIdArray[ 0 ] = $(this).parent().attr( 'id' );
				
				IsReplayceList = true;
				IsPlayingQueueChanged = true;
				InsertAlbumsToFavoriteList( PlayingQueueID, TempIdArray, IsReplayceList );
			});
			
			
			
			// 取代播放中
			$( '.Img_MediaList_ReplacePlayingQueue' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				// 開啟等待中視窗
				$('.Div_WaitAjax').dialog('open');
				
				IsReplayceList = true;
				IsPlayingQueueChanged = true;
				CheckWhatSelected_InsertToPlayingQueue();
			} );
		
		
		
			// 加入播放中
			$( '.Img_MediaList_AppendPlayingQueue' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				// 開啟等待中視窗
				$('.Div_WaitAjax').dialog('open');
				
				IsReplayceList = false;
				IsPlayingQueueChanged = true;
				CheckWhatSelected_InsertToPlayingQueue();
			} );
			
			
		
			// 加入到歌單
			$( '.Img_MediaList_AppendFavoriteList' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				IsReplayceList = false;
				CheckWhatSelected_OpenFavoriteListDialog();
				$('.Div_AddToFavoriteList').dialog('option', 'title', '請選擇歌單後，按「確定」加入新歌');
			} );
		}
		else
		{
			rtnresult.result = -1;        
		}
		
    });
	
    request.fail(function (jqxhr, textStatus) 
	{
       rtnresult.result = -2;       
    });
} 





// 取得某專輯內所有歌曲
function GetMediasofAlbum( albumid, titleAlbum )
{
	Page_MediaType = Page_MediaType_Albums_Media;
	
    var rtnresult =
	{
        result : 0,
        mediasofalbum : null
    };
	
    var postData = 
	{  
        Action:'GetAlbumMedia',
        idUser:UserID,
        idAlbum:albumid
    };
	
    var request = CallAjax( 'MediaPost.php', postData, 'json' );
	
    request.done( function ( Msg, statustext, jqxhr )
	{
        if( Msg != null)
		{
			AddListCount = 0;
            rtnresult.mediasofalbum = [];
			var HtmlStr = "";
			
			// 回上層
			HtmlStr += '<div class = "Div_BackLevel">';
				HtmlStr += '<img class = "Img_BackLevel" src = "img/Media/BtnBack.png" alt= "回上層" >';
				HtmlStr += '<img class = "Img_MediaList_ReplacePlayingQueue" src = "img/Media/Replace_PlayingQueue.png" title = "立即播放" />';
				HtmlStr += '<img class = "Img_MediaList_AppendPlayingQueue" src = "img/Media/Append_PlayingQueue.png" title = "將歌曲加入「播放中」" />';
				HtmlStr += '<img class = "Img_MediaList_AppendFavoriteList" src = "img/Media/Append_FavoriteList.png" title = "將歌曲加入現有「歌單」" />';
				HtmlStr += '<img class = "Img_MediaList_KillFavoriteList" src = "img/Media/Kill_FavoriteList.png" title = "移除「歌單」" />';
				HtmlStr += '<img class = "Img_MediaList_RemoveFavoriteMedia" src = "img/Media/Remove_FavoriteMedia.png" title = "將歌曲從「歌單」中移除" />';
			HtmlStr += '</div>';
			
		
		
			// 標頭
			HtmlStr += '<div class = "Div_MediaDetail_Header">';
			
				HtmlStr += '<div class = "Div_MediaDetail_CheckBox SelectAll" BeSelected = "No">';
				HtmlStr += '<img class = "Img_MediaDetail_CheckBox" src= "img/Media/checkbox_empty.png" alt = "勾選" BeSelected = "No" />';
				HtmlStr += '</div>';
				
				HtmlStr += '<div class = "Div_MediaDetail_MediaTitle" >';
				HtmlStr += '<label class = "Lbl_MediaDetail_MediaTitle" >歌曲名稱</label>';
				HtmlStr += '</div>';
				
				HtmlStr += '<div class = "Div_MediaDetail_AlbumName" >';
				HtmlStr += '<label class = "Lbl_MediaDetail_AlbumName" >專輯名稱</label>';
				HtmlStr += '</div>';
				
				HtmlStr += '<div class = "Div_MediaDetail_ArtistName" >';
				HtmlStr += '<label class = "Lbl_MediaDetail_ArtistName" >演唱者</label>';
				HtmlStr += '</div>';
				
				HtmlStr += '<div class = "Div_MediaDetail_Length" >';
				HtmlStr += '<label class = "Lbl_MediaDetail_Length" >長度</label>';
				HtmlStr += '</div>';
				
				HtmlStr += '<div class = "Div_MediaDetail_FileName" >';
				HtmlStr += '<label class = "Lbl_MediaDetail_FileName" >檔案名稱</label>';
				HtmlStr += '</div>';
				
			HtmlStr += '</div>';
			
			MediaCount = 0;
				
			if ( 0 < Msg.length )
			{
				$.each( Msg, function( index, element ) 
				{
					LastListenRow = 0;
					var LoopCount = 0;
					
					// 以新資料更新畫面 //
					HtmlStr += '<div class = "Div_MediaDetail" id = "' + element.idMedia + '" BeSelected = "No" >';
					
						HtmlStr += '<div class = "Div_MediaDetail_CheckBox" >';
							HtmlStr += '<img class = "Img_MediaDetail_CheckBox" src="img/Media/checkbox_empty.png" alt="勾選" BeSelected = "No" idMedia = "' + element.idMedia + '" />';
						HtmlStr += '</div>';
						
						HtmlStr += '<div class = "Div_MediaDetail_MediaTitle" >';
							HtmlStr += '<label class = "Lbl_MediaDetail_MediaTitle" >' + element.nameTitle + '</label>';
						HtmlStr += '</div>';
						
						HtmlStr += '<div class = "Div_MediaDetail_AlbumName" >';
							HtmlStr += '<label class = "Lbl_MediaDetail_AlbumName" >' + titleAlbum + '</label>';
						HtmlStr += '</div>';
						
						HtmlStr += '<div class = "Div_MediaDetail_ArtistName" >';
							HtmlStr += '<label class = "Lbl_MediaDetail_ArtistName" >' + element.nameArtist + '</label>';
						HtmlStr += '</div>';
						
						HtmlStr += '<div class = "Div_MediaDetail_Length" >';
							HtmlStr += '<label class = "Lbl_MediaDetail_Length" >' + element.lengthMedia + '</label>';
						HtmlStr += '</div>';
						
						HtmlStr += '<div class = "Div_MediaDetail_FileName" >';
							HtmlStr += '<label class = "Lbl_MediaDetail_FileName" >' + element.nameMediaFile + '</label>';
						HtmlStr += '</div>';
						
					HtmlStr += '</div>';
				
					MediaCount++;
				});
			
			}
			else
			{
				HtmlStr += '<label style = "position:absolute; left:5%; top:40%" >抱歉，此專輯下沒有相關音樂檔案</label>';
				$('.Div_AudioListArea').html( HtmlStr );
			}
				
			$('.Div_AudioListTitle').text( '專輯 >> ' + titleAlbum );
			$('.Div_AudioListArea').html( HtmlStr );
			
			// 列表用顏色區隔每一ROW
			$('.Div_MediaDetail:nth-child(even)').css( "background-color", "#eefcef" );
			$('.Div_MediaDetail:nth-child(odd)').css( "background-color", "#f0fafe" );
			
			ToolBarBtn_AllControl( false );
			
			
			
			//////
			////// 事件
			//////
			
			$('#Div_AddToNewList').hide();
			
			
			// 將歌曲元件組的容器綁定事件，作 CheckBox 的選取動作
			$('.Img_MediaDetail_CheckBox').click( function ()
			{
				// 如果判定是已勾選
				if ( 'Yes' == $(this).attr( 'BeSelected' ) )
				{
					// 做出不勾選的效果
					$(this).attr( 'src', 'img/Media/checkbox_empty.png' );
					$(this).attr( 'BeSelected', 'No' );
					AddListCount--;
					
					if ( 0 >= AddListCount )
					{
						ToolBarBtn_AllControl_ButNotRemove( false );
					}
				}
				// 如果判定非已勾選
				else
				{
					// 做出勾選效果
					$(this).attr( 'src', 'img/Media/checkbox.png' );
					$(this).attr( 'BeSelected', 'Yes' );
					AddListCount++;
					
					if ( 0 < AddListCount )
					{
						ToolBarBtn_AllControl_ButNotRemove( true );
					}
				}
			});
			
			
			
			// 標題的 CheckBox 要進行全選或全不選
			$('.SelectAll').click( function ()
			{
				if ( 'Yes' == $(this).attr( "BeSelected" ) )
				{
					$(this).attr( 'BeSelected', 'No' );
					$('.Img_MediaDetail_CheckBox').attr( 'src', 'img/Media/checkbox_empty.png' );
					$('.Img_MediaDetail_CheckBox').attr( 'BeSelected', 'No' );
					
					AddListCount = 0;
					ToolBarBtn_AllControl_ButNotRemove( false );
				}
				else
				{
					$(this).attr( 'BeSelected', 'Yes' );
					$('.Img_MediaDetail_CheckBox').attr( 'src', 'img/Media/checkbox.png' );
					$('.Img_MediaDetail_CheckBox').attr( 'BeSelected', 'Yes' );
					
					AddListCount = MediaCount;
					ToolBarBtn_AllControl_ButNotRemove( true );
				}
			} );
			
			
			
			// 點選回上頁
			$('.Img_BackLevel' ).click( function ()
			{
				GetAlbums( );
			});
			
			
			
			// 取代播放中
			$('.Img_MediaList_ReplacePlayingQueue' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				// 開啟等待中視窗
				$('.Div_WaitAjax').dialog('open');
				
				IsReplayceList = true;
				IsPlayingQueueChanged = true;
				CheckWhatSelected_InsertToPlayingQueue();
			});
			
			// 加入播放中
			$('.Img_MediaList_AppendPlayingQueue' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				// 開啟等待中視窗
				$('.Div_WaitAjax').dialog('option', 'title', '資料處理中，請稍候');
				$('.Div_WaitAjax').dialog('open');
				
				IsReplayceList = false;
				IsPlayingQueueChanged = true;
				CheckWhatSelected_InsertToPlayingQueue();
			});
			
			// 加入到歌單
			$('.Img_MediaList_AppendFavoriteList' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				IsReplayceList = false;
				CheckWhatSelected_OpenFavoriteListDialog();
				$('.Div_AddToFavoriteList').dialog('option', 'title', '請選擇歌單後，按「確定」加入新歌');
			});
        }
        else
		{
            rtnresult.result = -1;
		}
    });
	
    request.fail(function (jqxhr, textStatus) 
	{
    	rtnresult.result = -2;       
    });    
}






// 取得所有 歌手
function GetArtists( )
{
	Page_MediaType = Page_MediaType_Artists;
	UserID = UserID;
	
    var rtnresult =
	{
        result : 0,
        artists : null
    };
	
    var postData = 
	{  
        Action:'GetArtist',
        idUser:UserID       
    };  
	
    var request = CallAjax('MediaPost.php', postData, 'json');
	
    request.done( function ( Msg, statustext, jqxhr ) 
	{
        if( Msg != null)
		{
			AddListCount = 0;
			rtnresult.artists = [];
			var HtmlStr = "";
			
			HtmlStr += '<div class = "Div_BackLevel">';
				HtmlStr += '<img class = "Img_MediaList_ReplacePlayingQueue" src = "img/Media/Replace_PlayingQueue.png" title = "立即播放" />';
				HtmlStr += '<img class = "Img_MediaList_AppendPlayingQueue" src = "img/Media/Append_PlayingQueue.png" title = "將歌曲加入「播放中」" />';
				HtmlStr += '<img class = "Img_MediaList_AppendFavoriteList" src = "img/Media/Append_FavoriteList.png" title = "將歌曲加入現有「歌單」" />';
				HtmlStr += '<img class = "Img_MediaList_KillFavoriteList" src = "img/Media/Kill_FavoriteList.png" title = "移除「歌單」" />';
				HtmlStr += '<img class = "Img_MediaList_RemoveFavoriteMedia" src = "img/Media/Remove_FavoriteMedia.png" title = "將歌曲從「歌單」中移除" />';
			HtmlStr += '</div>';
			
			if ( 0 < Msg.length )
			{
				$.each( Msg, function( index, element ) 
				{
					// 以新資料更新畫面 //
					HtmlStr += '<div class = "Div_Singer" id = "' + element.idArtist + '" >';
						HtmlStr += '<img class ="Img_Singer_CheckBox" src="img/Media/checkbox_empty.png" alt="勾選" BeSelected = "No"/>';
						HtmlStr += '<img class = "Img_Singer_ArtistImg" src = "img/Media/Singer.png" alt = "' + element.nameArtist + '" />';
						HtmlStr += '<label class = "Lbl_Singer_ArtistName" >' + element.nameArtist + '</label>';
						HtmlStr += '<img class = "Img_BigItem_ReplacePlayingQueue" src="img/Media/PlayingNow.png" title = "立即播放" />';
					HtmlStr += '</div>';                 
				});
			}
			else
			{
				HtmlStr += '<label style = "position:absolute; left:5%; top:40%" >抱歉，目前不存在任何歌手</label>';
				$('.Div_AudioListArea').html( HtmlStr );
			}
				
			$('.Div_AudioListTitle').text( '歌手' );
			$('.Div_AudioListArea').html( HtmlStr );
			
			ToolBarBtn_AllControl( false );
			
			
			
			//////
			////// 事件
			//////
			
			// 將 歌手 元件組的容器綁定 Click 事件，可進入歌曲清單畫面
			$('.Img_Singer_ArtistImg').click( function ()
			{
				GetMediasofArtist( $(this).parent().attr( 'id' ), $(this).parent().children('.Lbl_Singer_ArtistName').text() );
			});
			
			
			
			// 將 CD 光碟元件組的 CheckBox 綁定 Click 事件，做勾選動作
			$( '.Img_Singer_CheckBox' ).click( function ()
			{
				// 如果判定是已勾選
				if ( 'Yes' == $(this).attr( 'BeSelected' ) )
				{
					// 做出不勾選的效果
					$(this).attr( 'src', 'img/Media/checkbox_empty.png' );
					$(this).attr( 'BeSelected', 'No' );
					AddListCount--;
					
					if ( 0 >= AddListCount )
					{
						ToolBarBtn_AllControl_ButNotRemove( false );
					}
				}
				// 如果判定非已勾選
				else
				{
					// 做出勾選效果
					$(this).attr( 'src', 'img/Media/checkbox.png' );
					$(this).attr( 'BeSelected', 'Yes' );
					AddListCount++;
					
					if ( 0 < AddListCount )
					{
						 ToolBarBtn_AllControl_ButNotRemove( true );
					}
				}
			});
			
			// 點選播放，可直接播放專輯內所有歌曲（將此專輯加入播放中歌單，並立刻播放）
			$('.Img_BigItem_ReplacePlayingQueue').click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				// 開啟等待中視窗
				$('.Div_WaitAjax').dialog('open');
				
				var TempIdArray = [];
				TempIdArray[ 0 ] = $(this).parent().attr( 'id' );
				
				IsReplayceList = true;
				IsPlayingQueueChanged = true;
				InsertArtistsToFavoriteList( PlayingQueueID, TempIdArray, IsReplayceList );
			});
			
			
			
			// 取代播放中
			$( '.Img_MediaList_ReplacePlayingQueue' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				// 開啟等待中視窗
				$('.Div_WaitAjax').dialog('open');
				
				IsReplayceList = true;
				IsPlayingQueueChanged = true;
				CheckWhatSelected_InsertToPlayingQueue();
			} );
			
			
			
			// 加入播放中
			$( '.Img_MediaList_AppendPlayingQueue' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				// 開啟等待中視窗
				$('.Div_WaitAjax').dialog('open');
				
				IsReplayceList = false;
				IsPlayingQueueChanged = true;
				CheckWhatSelected_InsertToPlayingQueue();
			} );			
			
		
		
			// 加入到歌單
			$( '.Img_MediaList_AppendFavoriteList' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				IsReplayceList = false;
				CheckWhatSelected_OpenFavoriteListDialog();
				$('.Div_AddToFavoriteList').dialog('option', 'title', '請選擇歌單後，按「確定」加入新歌');
			} );

        }
        else
		{
            rtnresult.result = -1;
		}
    });
    request.fail(function (jqxhr, textStatus)
	{
       rtnresult.result = -2;       
    });
}





// 取得某 歌手 內所有歌曲
function GetMediasofArtist( artistid, nameArtist )
{
	Page_MediaType = Page_MediaType_Artists_Media;
	UserID = UserID;
	
    var rtnresult =
	{
        result : 0,
        mediasofartist : null
    };
	
    var postData = 
	{  
        Action:'GetArtistMedia',
        idUser:UserID,
        idArtist:artistid
    };
	
    var request = CallAjax( 'MediaPost.php', postData, 'json' );
	
    request.done( function ( Msg, statustext, jqxhr )
	{
        if( Msg != null)
		{
			AddListCount = 0;
            rtnresult.mediasofartist = [];
            
			var HtmlStr = "";
			
			// 回上層
			HtmlStr += '<div class = "Div_BackLevel">';
				HtmlStr += '<img class = "Img_BackLevel" src = "img/Media/BtnBack.png" alt="回上層" />';
				HtmlStr += '<img class = "Img_MediaList_ReplacePlayingQueue" src = "img/Media/Replace_PlayingQueue.png" title = "立即播放" />';
				HtmlStr += '<img class = "Img_MediaList_AppendPlayingQueue" src = "img/Media/Append_PlayingQueue.png" title = "將歌曲加入「播放中」" />';
				HtmlStr += '<img class = "Img_MediaList_AppendFavoriteList" src = "img/Media/Append_FavoriteList.png" title = "將歌曲加入現有「歌單」" />';
				HtmlStr += '<img class = "Img_MediaList_KillFavoriteList" src = "img/Media/Kill_FavoriteList.png" title = "移除「歌單」" />';
				HtmlStr += '<img class = "Img_MediaList_RemoveFavoriteMedia" src = "img/Media/Remove_FavoriteMedia.png" title = "將歌曲從「歌單」中移除" />';
			HtmlStr += '</div>';
			
		
			// 標頭
			HtmlStr += '<div class = "Div_MediaDetail_Header">';
			
				HtmlStr += '<div class = "Div_MediaDetail_CheckBox SelectAll" BeSelected = "No">';
					HtmlStr += '<img class = "Img_MediaDetail_CheckBox" src="img/Media/checkbox_empty.png" alt="勾選" BeSelected = "No"/>';
				HtmlStr += '</div>';
				
				HtmlStr += '<div class = "Div_MediaDetail_MediaTitle" >';
					HtmlStr += '<label class = "Lbl_MediaDetail_MediaTitle" >歌曲名稱</label>';
				HtmlStr += '</div>';
				
				HtmlStr += '<div class = "Div_MediaDetail_AlbumName" >';
					HtmlStr += '<label class = "Lbl_MediaDetail_AlbumName" >專輯名稱</label>';
				HtmlStr += '</div>';
				
				HtmlStr += '<div class = "Div_MediaDetail_ArtistName" >';
					HtmlStr += '<label class = "Lbl_MediaDetail_ArtistName" >演唱者</label>';
				HtmlStr += '</div>';
				
				HtmlStr += '<div class = "Div_MediaDetail_Length" >';
					HtmlStr += '<label class = "Lbl_MediaDetail_Length" >長度</label>';
				HtmlStr += '</div>';
				
				HtmlStr += '<div class = "Div_MediaDetail_FileName" >';
					HtmlStr += '<label class = "Lbl_MediaDetail_FileName" >檔案名稱</label>';
				HtmlStr += '</div>';
			
			HtmlStr += '</div>';
				
			MediaCount = 0;
				
			if ( 0 < Msg.length )
			{
				$.each( Msg, function( index, element ) 
				{
					// 以新資料更新畫面 //
					var LoopCount = 0;
					HtmlStr += '<div class = "Div_MediaDetail" id = "' + element.idMedia + '" BeSelected = "No" >';
					
						HtmlStr += '<div class = "Div_MediaDetail_CheckBox" >';
							HtmlStr += '<img class = "Img_MediaDetail_CheckBox" src="img/Media/checkbox_empty.png" alt="勾選" BeSelected = "No" idMedia = "' + element.idMedia + '"/>';
						HtmlStr += '</div>';
						
						HtmlStr += '<div class = "Div_MediaDetail_MediaTitle" >';
							HtmlStr += '<label class = "Lbl_MediaDetail_MediaTitle" >' + element.nameTitle + '</label>';
						HtmlStr += '</div>';
						
						HtmlStr += '<div class = "Div_MediaDetail_AlbumName" >';
							HtmlStr += '<label class = "Lbl_MediaDetail_AlbumName" >' + element.titleAlbum + '</label>';
						HtmlStr += '</div>';
						
						HtmlStr += '<div class = "Div_MediaDetail_ArtistName" >';
							HtmlStr += '<label class = "Lbl_MediaDetail_ArtistName" >' + nameArtist + '</label>';
						HtmlStr += '</div>';
						
						HtmlStr += '<div class = "Div_MediaDetail_Length" >';
							HtmlStr += '<label class = "Lbl_MediaDetail_Length" >' + element.lengthMedia + '</label>';
						HtmlStr += '</div>';
						
						HtmlStr += '<div class = "Div_MediaDetail_FileName" >';
							HtmlStr += '<label class = "Lbl_MediaDetail_FileName" >' + element.nameMediaFile + '</label>';
						HtmlStr += '</div>';
					
					HtmlStr += '</div>';
					
					MediaCount++;
				});
			}
			else
			{
				HtmlStr += '<label style = "position:absolute; left:5%; top:40%" >抱歉，此歌手沒有相關音樂檔案</label>';
			}
			
			
			$('.Div_AudioListTitle').text( '歌手 >> ' + nameArtist );
			$('.Div_AudioListArea').html( HtmlStr );
				
			// 列表用顏色區隔每一ROW
			$('.Div_MediaDetail:nth-child(even)').css( "background-color", "#eefcef" );
			$('.Div_MediaDetail:nth-child(odd)').css( "background-color", "#f0fafe" );
			
			ToolBarBtn_AllControl( false );
			
			
			
			//////
			////// 事件
			//////
			
			// 將歌曲元件組的容器綁定事件，作 CheckBox 的選取動作
			$('.Img_MediaDetail_CheckBox').click( function ()
			{
				// 如果判定是已勾選
				if ( 'Yes' == $(this).attr( 'BeSelected' ) )
				{
					// 做出不勾選的效果
					$(this).attr( 'src', 'img/Media/checkbox_empty.png' );
					$(this).attr( 'BeSelected', 'No' );
					AddListCount--;
					
					if ( 0 >= AddListCount )
					{
						ToolBarBtn_AllControl_ButNotRemove( false );
					}
				}
				// 如果判定非已勾選
				else
				{
					// 做出勾選效果
					$(this).attr( 'src', 'img/Media/checkbox.png' );
					$(this).attr( 'BeSelected', 'Yes' );
					AddListCount++;
					
					if ( 0 < AddListCount )
					{
						ToolBarBtn_AllControl_ButNotRemove( true );
					}
				}
			});
			
			
			
			
			// 標題的 CheckBox 要進行全選或全不選
			$('.SelectAll').click( function ()
			{
				if ( 'Yes' == $(this).attr( "BeSelected" ) )
				{
					$(this).attr( 'BeSelected', 'No' );
					$('.Img_MediaDetail_CheckBox').attr( 'src', 'img/Media/checkbox_empty.png' );
					$('.Img_MediaDetail_CheckBox').attr( 'BeSelected', 'No' );
					
					AddListCount = 0;
					ToolBarBtn_AllControl_ButNotRemove( false );
				}
				else
				{
					$(this).attr( 'BeSelected', 'Yes' );
					$('.Img_MediaDetail_CheckBox').attr( 'src', 'img/Media/checkbox.png' );
					$('.Img_MediaDetail_CheckBox').attr( 'BeSelected', 'Yes' );
					
					AddListCount = MediaCount;
					ToolBarBtn_AllControl_ButNotRemove( true );
				}
			} );
			
		
			// 點選回上頁
			$('.Img_BackLevel' ).click( function ()
			{
				GetArtists( );
			});
			

			
			// 取代播放中
			$('.Img_MediaList_ReplacePlayingQueue' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				// 開啟等待中視窗
				$('.Div_WaitAjax').dialog('open');
				
				IsReplayceList = true;
				IsPlayingQueueChanged = true;
				CheckWhatSelected_InsertToPlayingQueue();
			});
			
			
			
			// 加入播放中
			$('.Img_MediaList_AppendPlayingQueue' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				// 開啟等待中視窗
				$('.Div_WaitAjax').dialog('option', 'title', '資料處理中，請稍候');
				$('.Div_WaitAjax').dialog('open');
			
				IsReplayceList = false;
				IsPlayingQueueChanged = true;
				CheckWhatSelected_InsertToPlayingQueue();
			});
			
			
			
			// 取代歌單
			$('.Img_MediaList_AppendFavoriteList' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				IsReplayceList = false;
				CheckWhatSelected_OpenFavoriteListDialog();
				$('.Div_AddToFavoriteList').dialog('option', 'title', '請選擇歌單後，按「確定」加入新歌');
			});
        }
        else
		{
            rtnresult.result = -1;
		}
        return rtnresult;
    });
	
    request.fail(function (jqxhr, textStatus) 
	{
		rtnresult.result = -2;
    });
    
}





// 取得檔案結構
function GetFileStruct( idPath, PathFolderName, ShowBar )
{
	Page_MediaType = Page_MediaType_Folders;
	
    var rtnresult =
	{
        result : 0,
        Files : null
    };
	
    var postData = 
	{
        Action:'GetFileStruct',
        idUser:UserID,
        idParent:idPath
    };
	
    var request = CallAjax( 'MediaPost.php', postData, 'json' );
	
    request.done( function ( Msg, statustext, jqxhr ) 
	{
        if( Msg != null )
		{
			AddListCount = 0;
			
			// 記錄這一層的 Path
			EveryLevelPath[ NowLevel ] = idPath;
			EveryLevelName[ NowLevel ] = PathFolderName;
			NowLevel++;
			
            rtnresult.Files = [];
			var HtmlStr = "";
			
			// 回上層
			HtmlStr += '<div class = "Div_BackLevel">';
				HtmlStr += '<img class = "Img_BackLevel" src = "img/Media/BtnBack.png" alt = "回上層" >';
				HtmlStr += '<img class = "Img_MediaList_ReplacePlayingQueue" src = "img/Media/Replace_PlayingQueue.png" title = "立即播放" />';
				HtmlStr += '<img class = "Img_MediaList_AppendPlayingQueue" src = "img/Media/Append_PlayingQueue.png" title = "將歌曲加入「播放中」" />';
				HtmlStr += '<img class = "Img_MediaList_AppendFavoriteList" src = "img/Media/Append_FavoriteList.png" title = "將歌曲加入現有「歌單」" />';
				HtmlStr += '<img class = "Img_MediaList_KillFavoriteList" src = "img/Media/Kill_FavoriteList.png" title = "移除「歌單」" />';
				HtmlStr += '<img class = "Img_MediaList_RemoveFavoriteMedia" src = "img/Media/Remove_FavoriteMedia.png" title = "將歌曲從「歌單」中移除" />';
			HtmlStr += '</div>';
			
			
			if ( 0 < Msg.length )
			{
				$.each( Msg, function( index, element ) 
				{
					// 目錄是 0
					if ( 0 == element.type )
					{
						// 以新資料更新畫面 //
						HtmlStr += '<div class = "Div_Folder" id = "' + element.idPath + '" idPath = "' + element.idParent + '" >';
							HtmlStr += '<img class = "Img_Folder_CheckBox" src = "img/Media/checkbox_empty.png" alt = "勾選" BeSelected = "No" />';
							HtmlStr += '<img class = "Img_Folder_FolderImg" src = "img/Media/Folder.png" alt = "目錄圖" />';
							HtmlStr += '<img class = "Img_BigItem_ReplacePlayingQueue IsFolder" src = "img/Media/PlayingNow.png" title = "立即播放" MediaID = "' + element.idMedia + '"/>';
							HtmlStr += '<label class = "Lbl_Folder_FolderName" >' + element.name + '</label>';
						HtmlStr += '</div>';
					}
					
					// 檔案是 1
					else
					{
						// 以新資料更新畫面 //
						HtmlStr += '<div class = "Div_File" id = "' + element.idMedia + '" >';
							HtmlStr += '<img class = "Img_File_CheckBox" src = "img/Media/checkbox_empty.png" alt = "勾選" BeSelected = "No" />';
							HtmlStr += '<img class = "Img_File_FileImg" src="img/Media/AudioFile.png" alt="目錄圖" />';
							HtmlStr += '<img class = "Img_BigItem_ReplacePlayingQueue IsFile" src="img/Media/PlayingNow.png" title = "立即播放" MediaID = "' + element.idMedia + '"/>';
							HtmlStr += '<label class = "Lbl_File_FileName" >' + element.name + '</label>';
						HtmlStr += '</div>';
					}
				});
			}
			else
			{
				HtmlStr += '<label style = "position:absolute; left:5%; top:40%" >抱歉，此資料夾下沒有相關音樂檔案</label>';
			}
			
			var StrPath = "";
			
			for ( var i = 0; i < NowLevel; i++ )
			{
				StrPath = StrPath + ' >> ' + EveryLevelName[ i ];
			}
			
			$('.Div_AudioListTitle').text( '資料夾' + StrPath );
            $('.Div_AudioListArea').html( HtmlStr );
			
			if ( "Yes" == ShowBar )
			{
				$('.Img_BackLevel').fadeTo( 300, 1 );
				$('.Img_BackLevel').css( 'cursor', 'pointer' );
			}
			else
			{
				$('.Img_BackLevel').fadeTo( 300, 0.1 );
				$('.Img_BackLevel').css( 'cursor', 'default' );
				RootPath = idPath;
			}
			
			ToolBarBtn_AllControl( false );
			
			
			
			//////
			////// 事件
			//////
			

			// 將目錄元件組的容器綁定事件，作進入該目錄動作，idPath 在上方已經用自訂屬性帶入 
			$('.Img_BackLevel' ).click( function ()
			{
				NowLevel = NowLevel - 2;
				
				if ( RootPath == EveryLevelPath[ NowLevel ] )
				{
					GetFileStruct( EveryLevelPath[ NowLevel ], EveryLevelName[ NowLevel ], "No" );
				}
				else
				{
					GetFileStruct( EveryLevelPath[ NowLevel ], EveryLevelName[ NowLevel ], "Yes" );
				}
			});
			
			// 將目錄元件組的容器綁定事件，點選元件組，可以進入下一層目錄。（idPath 在上方已經用自訂屬性帶入 ）
			$( '.Img_Folder_FolderImg' ).click( function ()
			{
				BackPath = $( this ).parent().attr( 'idPath' );
				GetFileStruct( $( this ).parent().attr( 'id' ), $( this ).parent().children('.Lbl_Folder_FolderName').text(), "Yes" );
			});
			
			// 將目錄勾選，作 CheckBox 的選取動作
			$( '.Img_Folder_CheckBox' ).click( function ()
			{
				// 如果判定是已勾選
				if ( 'Yes' == $( this ).attr( 'BeSelected' ) )
				{
					// 做出不勾選的效果
					$( this ).attr( 'src', 'img/Media/checkbox_empty.png' );
					$( this ).attr( 'BeSelected', 'No' );
					AddListCount--;
					
					if ( 0 >= AddListCount )
					{
						ToolBarBtn_AllControl_ButNotRemove( false );
					}
				}
				// 如果判定非已勾選
				else
				{
					// 做出勾選效果
					$( this ).attr( 'src', 'img/Media/checkbox.png' );
					$( this ).attr( 'BeSelected', 'Yes' );
					AddListCount++;
					
					if ( 0 < AddListCount )
					{
						ToolBarBtn_AllControl_ButNotRemove( true );
					}
				}
			});
			
			
			// 將目錄勾選，作 CheckBox 的選取動作
			$( '.Img_File_CheckBox' ).click( function ()
			{
				// 如果判定是已勾選
				if ( 'Yes' == $( this ).attr( 'BeSelected' ) )
				{
					// 做出不勾選的效果
					$( this ).attr( 'src', 'img/Media/checkbox_empty.png' );
					$( this ).attr( 'BeSelected', 'No' );
					AddListCount--;
					
					if ( 0 >= AddListCount )
					{
						ToolBarBtn_AllControl_ButNotRemove( false );
					}
				}
				// 如果判定非已勾選
				else
				{
					// 做出勾選效果
					$( this ).attr( 'src', 'img/Media/checkbox.png' );
					$( this ).attr( 'BeSelected', 'Yes' );
					AddListCount++;
					
					if ( 0 < AddListCount )
					{
						ToolBarBtn_AllControl_ButNotRemove( true );
					}
				}
			});
			
			
			// 點選播放，可直接播放專輯內所有歌曲（將此專輯加入播放中歌單，並立刻播放）
			$('.Img_BigItem_ReplacePlayingQueue').click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				// 開啟等待中視窗
				$('.Div_WaitAjax').dialog('option', 'title', '資料處理中，請稍候');
				$('.Div_WaitAjax').dialog('open');
				
				var TempIdArray = [];
				TempIdArray[ 0 ] = $(this).parent().attr( 'id' );
				
				IsReplayceList = true;
				IsPlayingQueueChanged = true;
				
				if ( true == $(this).hasClass( 'IsFolder' ) )
				{
					InsertFoldersToFavoriteList( PlayingQueueID, TempIdArray, IsReplayceList );
				}
				else
				{
					InsertMediasToFavoriteList( PlayingQueueID, TempIdArray, IsReplayceList );
				}
			});
			
			
			
			// 取代播放中
			$('.Img_MediaList_ReplacePlayingQueue' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				// 開啟等待中視窗
				$('.Div_WaitAjax').dialog('open');
			
				IsReplayceList = true;
				IsPlayingQueueChanged = true;
				CheckWhatSelected_InsertToPlayingQueue();
			});
			
			
			
			// 加入播放中
			$('.Img_MediaList_AppendPlayingQueue' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				// 開啟等待中視窗
				$('.Div_WaitAjax').dialog('open');
			
				IsReplayceList = false;
				IsPlayingQueueChanged = true;
				CheckWhatSelected_InsertToPlayingQueue();
			});
			
			
			
			// 取代歌單
			$('.Img_MediaList_AppendFavoriteList' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				IsReplayceList = false;
				CheckWhatSelected_OpenFavoriteListDialog();
				$('.Div_AddToFavoriteList').dialog('option', 'title', '請選擇歌單後，按「確定」加入新歌');
			});
        }
        else
		{
            rtnresult.result = -1;
		}
    });
	
    request.fail(function (jqxhr, textStatus)
	{
        rtnresult.result = -2;
    });
	
}





// 取得歌單清單
function GetFavoriteList( isPlayingQueue )
{
	Page_MediaType = Page_MediaType_FavoriteList;
	RenameCount = 0;
	
    var rtnresult =
	{
        result : 0,
        playlists : null
    };
    
	var postData = 
	{
        Action:'GetPlayList',
        idUser:UserID,
        isPlayingQueue:isPlayingQueue
    };
	
    var request = CallAjax('MediaPost.php', postData, 'json');
	
    request.done( function ( Msg, statustext, jqxhr )
	{
        if ( Msg != null )
		{
			if ( true == isPlayingQueue )
			{
				// 僅取回 Playing Queue ID
				$.each( Msg, function( index, element )
				{
					PlayingQueueID = element.idPlaying;
				});
	
				// 取得現正播放清單並開始播放
				GetMediasOfPlayingQueue_FillDiv_AndPlay( );
				return;	
			}
			
			AddListCount = 0;
            rtnresult.FavoriteLists = [];
			var HtmlStr = "";
			FavoriteListData = [];
				
			HtmlStr += '<div class = "Div_BackLevel">';
				HtmlStr += '<img class = "Img_MediaList_ReplacePlayingQueue" src = "img/Media/Replace_PlayingQueue.png" title = "立即播放" />';
				HtmlStr += '<img class = "Img_MediaList_AppendPlayingQueue" src = "img/Media/Append_PlayingQueue.png" title = "將歌曲加入「播放中」" />';
				HtmlStr += '<img class = "Img_MediaList_AppendFavoriteList" src = "img/Media/Append_FavoriteList.png" title = "將歌曲加入現有「歌單」" />';
				HtmlStr += '<img class = "Img_MediaList_KillFavoriteList" src = "img/Media/Kill_FavoriteList.png" title = "移除「歌單」" />';
				HtmlStr += '<img class = "Img_MediaList_RemoveFavoriteMedia" src = "img/Media/Remove_FavoriteMedia.png" title = "將歌曲從「歌單」中移除" />';
			HtmlStr += '</div>';
				
			if ( 0 < Msg.length )
			{
				$.each( Msg, function( index, element )
				{
					// 以新資料更新畫面 //
					HtmlStr += '<div class = "Div_FavoriteList" id = "' + element.idPlaying + '" >';
						HtmlStr += '<img class ="Img_FavoriteList_CheckBox" src="img/Media/checkbox_empty.png" title = "勾選" BeSelected = "No" idPlaying = "' + element.idPlaying + '"/>';
						HtmlStr += '<img class = "Img_FavoriteList_FavoriteListImg" src = "img/Media/FavoriteList.png" alt = "歌單圖"/>';
						HtmlStr += '<label class = "Lbl_FavoriteList_Name" >' + element.namePlayList + '</label>';
						HtmlStr += '<img class = "Img_BigItem_ReplacePlayingQueue" src="img/Media/PlayingNow.png" title = "立即播放" />';
						HtmlStr += '<input class = "Txt_FavoriteList_Name" type = "text"/>';
					HtmlStr += '</div>';
					
					FavoriteListData[ index ] = new FavoriteListInfo( element.idPlaying, element.isPlayingQueue, element.indexPlaying, element.timeShift, element.namePlayList );
				});
				
				// 點選 [ 刪除 ] 時，跳出的對話框，先把畫面鑲在這裡，在事件當中會串 Dialog Event
				HtmlStr += '<div id = "Div_DelDialogConfirm" title = "確定要刪除？" style = "visibility:collapse">';
				HtmlStr += '<p class = "P_ConfirmMsg" >所選定的「歌單」歌曲清單將會被刪除，你確定嗎？</p>';
				HtmlStr += '</div>';
			}
			else
			{
				HtmlStr += '<label style = "position:absolute; left:5%; top:40%" >抱歉，目前不存在任何歌單</label>';
			}

				
			$('.Div_AudioListTitle').text( '歌單' );
			$('.Div_AudioListArea').html( HtmlStr );
			
			ToolBarBtn_AllControl( false );
			
			
			//////
			////// 事件
			//////
			
			// 將歌單元件組的容器綁定事件
			$('.Img_FavoriteList_FavoriteListImg').click( function ()
			{
				GetMediasOfFavoriteLists( $(this).parent().attr( 'id' ), $(this).parent().children('.Lbl_FavoriteList_Name').text() );
			});
			
			
			
			// 將歌單勾選，作 CheckBox 的選取動作綁定事件
			$( '.Img_FavoriteList_CheckBox' ).click( function ()
			{
				// 如果判定是已勾選
				if ( 'Yes' == $(this).attr( 'BeSelected' ) )
				{
					// 做出不勾選的效果
					$(this).attr( 'src', 'img/Media/checkbox_empty.png' );
					$(this).attr( 'BeSelected', 'No' );
					RenameCount--;
					AddListCount--;
					
					if ( 0 >= AddListCount )
					{
						$('.Img_MediaList_ReplacePlayingQueue').fadeTo( 300, 0.3 );
						$('.Img_MediaList_AppendPlayingQueue').fadeTo( 300, 0.3 );
						$('.Img_MediaList_KillFavoriteList').fadeTo( 300, 0.3 );
						$('.Img_MediaList_ReplacePlayingQueue').css( 'cursor', 'default' );
						$('.Img_MediaList_AppendPlayingQueue').css( 'cursor', 'default' );
						$('.Img_MediaList_KillFavoriteList').css( 'cursor', 'default' );
					}
				}
				// 如果判定非已勾選
				else
				{
					// 做出勾選效果
					$(this).attr( 'src', 'img/Media/checkbox.png' );
					$(this).attr( 'BeSelected', 'Yes' );
					RenameCount++;
					AddListCount++;
					
					if ( 0 < AddListCount )
					{
						$('.Img_MediaList_ReplacePlayingQueue').fadeTo( 300, 1 );
						$('.Img_MediaList_AppendPlayingQueue').fadeTo( 300, 1 );
						$('.Img_MediaList_KillFavoriteList').fadeTo( 300, 1 );
						$('.Img_MediaList_ReplacePlayingQueue').css( 'cursor', 'pointer' );
						$('.Img_MediaList_AppendPlayingQueue').css( 'cursor', 'pointer' );
						$('.Img_MediaList_KillFavoriteList').css( 'cursor', 'pointer' );
					}
				}
				
				// 判斷只有選擇一個「歌單」時才讓更名按鈕可點選
				if ( 1 == RenameCount )
				{
					$('.Img_BtnRename').css( 'visibility', 'visible' );
				}
				else
				{
					$('.Img_BtnRename').css( 'visibility', 'hidden' );
				}
			});
			
			
			
			// 將輸入框綁定事件，更名的時候用
			$('.Txt_FavoriteList_Name').keydown( function (e)
			{
				// DialogInput 是新增、建立新的「歌單」才會有這個 Class ，所以出現這個表示新增，否則是重新命名。新增的話，就不要做事
				if ( false == $(this).hasClass('DialogInput') )
				{
					// 按 下ENTER 則進行指令
					if ( 13 == e.keyCode )
					{
						var TmpPlayingID = $(this).parent().attr( 'id' );
						var TmpListName = $(this).attr( 'value' );
						UpdateFavoriteLists( TmpPlayingID, TmpListName );
					}
					// 按下 ESC 則取消指令
					else if ( 27 == e.keyCode )
					{
						$(this).css( 'visibility', 'hidden' );
						$(this).parent().children('.Lbl_FavoriteList_Name').css( 'visibility', 'visible' );
					}
				}
			} );
			
			
			
			// 失去 Focus 則進行指令
			$('.Txt_FavoriteList_Name').focusout(function(e) 
			{
				// DialogInput 是新增、建立新的「歌單」才會有這個 Class ，所以出現這個表示新增，否則是重新命名。新增的話，就不要做事
				if ( false == $(this).hasClass('DialogInput') )
				{
					var TmpPlayingID = $(this).parent().attr( 'id' );
					var TmpListName = $(this).attr( 'value' );
					UpdateFavoriteLists( TmpPlayingID, TmpListName );
				}
            });
			
			// 點選播放，可直接播放專輯內所有歌曲（將此專輯加入播放中歌單，並立刻播放）
			$('.Img_BigItem_ReplacePlayingQueue').click( function ()
			{
				// 開啟等待中視窗
				$('.Div_WaitAjax').dialog('option', 'title', '資料處理中，請稍候');
				$('.Div_WaitAjax').dialog('open');
				
				var TempIdArray = [];
				TempIdArray[ 0 ] = $(this).parent().attr( 'id' );
				
				IsReplayceList = true;
				IsPlayingQueueChanged = true;
				InsertFavoriteListsToPlaying( PlayingQueueID, TempIdArray, IsReplayceList );
			});

		
		
			/*
			// 將 新建按鈕 綁定事件
			$('.Img_BtnCreate').click( function ()
			{
				$( '#Div_CreateDialogConfirm' ).css( 'visibility', 'visible' );
				$( '#Div_CreateDialogConfirm' ).children().children( '.Txt_FavoriteList_Name' ).css( 'visibility', 'visible' );
				$( '#Div_CreateDialogConfirm' ).dialog( 'open' );
			} );
			
			
			
			// 將 更名按鈕 綁定事件
			$('.Img_BtnRename').click( function ()
			{
				// 以回圈找出被勾選項目 
				$.each( $('.Img_FavoriteList_CheckBox'), function() 
				{
					if ( 'Yes' == ( $(this).attr( 'BeSelected' ) ) )
					{
						var TmpName = $(this).parent().children('.Lbl_FavoriteList_Name').text();
						$(this).parent().children('.Txt_FavoriteList_Name').attr( 'value', TmpName );
						$(this).parent().children('.Lbl_FavoriteList_Name').css( 'visibility', 'collapse' );
						$(this).parent().children('.Txt_FavoriteList_Name').css( 'visibility', 'visible' );
						$(this).parent().children('.Txt_FavoriteList_Name').focus();						// 這一行非常重要。一定要
					}
				});
			} );
			*/
			
			
			
			
			
			// 取代播放中
			$( '.Img_MediaList_ReplacePlayingQueue' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				// 開啟等待中視窗
				$('.Div_WaitAjax').dialog('open');
				
				IsReplayceList = true;
				IsPlayingQueueChanged = true;
				CheckWhatSelected_InsertToPlayingQueue();
			} );
		
		
			
			// 加入播放中
			$( '.Img_MediaList_AppendPlayingQueue' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				// 開啟等待中視窗
				$('.Div_WaitAjax').dialog('open');
				
				IsReplayceList = false;
				IsPlayingQueueChanged = true;
				CheckWhatSelected_InsertToPlayingQueue();
			} );
			
			
		
			// 加入到歌單
			$( '.Img_MediaList_AppendFavoriteList' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				IsReplayceList = false;
				CheckWhatSelected_OpenFavoriteListDialog();
				$('.Div_AddToFavoriteList').dialog('option', 'title', '請選擇歌單後，按「確定」加入新歌');
			} );
			
			
			
			// 將 刪除按鈕 綁定事件
			$('.Img_MediaList_KillFavoriteList').click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				$( "#Div_DelDialogConfirm" ).css( 'visibility', 'visible' );
				$( '#Div_DelDialogConfirm' ).children( '.P_ConfirmMsg' ).css( 'visibility', 'visible' );
				$( "#Div_DelDialogConfirm" ).dialog( 'open' );
			});
			
			//// [ 刪除 ] 對話框與事件
			//// 
			//// 這裡是 JQuery UI 所提供的，寫法固定，把要顯示在對話框的內容先鑲在 HTML 中，並設定隱藏，並且撰寫以下片段，產生一個對話框物件。
			//// 然後再想觸發的按鈕事件（比方點新增按鈕會需要這個畫面來當對話框）中呼叫 Dialog( 'open' )就可以了
			//// 
			$(function() 
			{	
				$( "#Div_DelDialogConfirm" ).dialog( 
				{
					autoOpen: false,
					resizable: false,
					height:240,
					modal: true,
					show: 
					{
						effect: "fade",
						duration: 1000
					},
					hide: 
					{
						effect: "fade",
						duration: 1000
					},
					  
					// 對話框有幾個按鈕，以及按了有什麼事件觸發
					buttons: 
					{
						"刪除所選項目": function()
						{
							CheckWhatFavoriteListSelected();
							
							// 對話框消失之前，要把上面的 DIV 隱藏
							$( "#Div_DelDialogConfirm" ).css( 'visibility', 'collapse' );
							$( '#Div_DelDialogConfirm' ).children( '.P_ConfirmMsg' ).css( 'visibility', 'collapse' );
							DelFavoriteLists( FavoriteListId_BeAddToList );
							
							$( this ).dialog( "close" );
						},
						"取消": function()
						{
							// 對話框消失之前，要把上面的 DIV 隱藏
							$( "#Div_DelDialogConfirm" ).css( 'visibility', 'collapse' );
							$( '#Div_DelDialogConfirm' ).children( '.P_ConfirmMsg' ).css( 'visibility', 'collapse' );
							$( this ).dialog( "close" );
						}
					}
					
				});
				
			});
        }
        else
		{
            rtnresult.result = -1;        
		}
    });
	
    request.fail( function ( jqxhr, textStatus )
	{
       rtnresult.result = -2;
    });
    
}





// 取得歌單清單的所有歌曲檔
function GetMediasOfFavoriteLists( FavoriteListID, FavoriteListName )
{
	Page_MediaType = Page_MediaType_FavoriteList_Media;
	
    var rtnresult =
	{
        result : 0,
        mediasofplaylist : null
    };
	
    var postData = 
	{
        Action:'GetPlayListMedia',
        idUser:UserID,
        idPlaying:FavoriteListID
    };
	
    var request = CallAjax( 'MediaPost.php', postData, 'json' )
	
    request.done( function ( Msg, statustext, jqxhr )
	{
         if( Msg != null )
		 {
			AddListCount = 0;  
			
            var HtmlStr = "";
					
			// 回上層
			HtmlStr += '<div class = "Div_BackLevel">';
				HtmlStr += '<img class = "Img_BackLevel" src = "img/Media/BtnBack.png" alt="回上層" />';
				HtmlStr += '<img class = "Img_MediaList_ReplacePlayingQueue" src = "img/Media/Replace_PlayingQueue.png" title = "立即播放" />';
				HtmlStr += '<img class = "Img_MediaList_AppendPlayingQueue" src = "img/Media/Append_PlayingQueue.png" title = "將歌曲加入「播放中」" />';
				HtmlStr += '<img class = "Img_MediaList_AppendFavoriteList" src = "img/Media/Append_FavoriteList.png" title = "將歌曲加入現有「歌單」" />';
				HtmlStr += '<img class = "Img_MediaList_KillFavoriteList" src = "img/Media/Kill_FavoriteList.png" title = "移除「歌單」" />';
				HtmlStr += '<img class = "Img_MediaList_RemoveFavoriteMedia" src = "img/Media/Remove_FavoriteMedia.png" title = "將歌曲從「歌單」中移除" />';
			HtmlStr += '</div>';
		
		
			// 標頭
			HtmlStr += '<div class = "Div_FavoriteListDetail_Header">';
			
				HtmlStr += '<div class = "Div_FavoriteListDetail_Playing" >';
				HtmlStr += '</div>';
			
				// 做全選 CheckBox //
				HtmlStr += '<div class = "Div_FavoriteListDetail_CheckBox SelectAll" >';
				HtmlStr += '<img class = "Img_FavoriteListDetail_CheckBox" src="img/Media/checkbox_empty.png" alt="勾選" BeSelected = "No"/>';
				HtmlStr += '</div>';
				
				HtmlStr += '<div class = "Div_FavoriteListDetail_MediaTitle" >';
				HtmlStr += '<label class = "Lbl_FavoriteListDetail_MediaTitle" >歌曲名稱</label>';
				HtmlStr += '</div>';
				
				HtmlStr += '<div class = "Div_FavoriteListDetail_AlbumName" >';
				HtmlStr += '<label class = "Lbl_FavoriteListDetail_AlbumName" >專輯名稱</label>';
				HtmlStr += '</div>';
				
				HtmlStr += '<div class = "Div_FavoriteListDetail_ArtistName" >';
				HtmlStr += '<label class = "Lbl_FavoriteListDetail_ArtistName" >演唱者</label>';
				HtmlStr += '</div>';
				
				HtmlStr += '<div class = "Div_FavoriteListDetail_Length" >';
				HtmlStr += '<label class = "Lbl_FavoriteListDetail_Length" >長度</label>';
				HtmlStr += '</div>';
				
				HtmlStr += '<div class = "Div_FavoriteListDetail_FileName" >';
				HtmlStr += '<label class = "Lbl_MediaDetail_FileName" >檔案名稱</label>';
				HtmlStr += '</div>';
				
			HtmlStr += '</div>';			
			MediaCount = 0;
			
			if ( 0 < Msg.length )
			{
				$.each( Msg, function( index, element )
				{
					HtmlStr += '<div class = "Div_FavoriteListDetail" BeSelected = "No" id = "' + element.idMedia + '">';
					
					HtmlStr += '<div class = "Div_FavoriteListDetail_Playing" id = "' + element.idMedia + '">';
					HtmlStr += '</div>';
				
					HtmlStr += '<div class = "Div_FavoriteListDetail_CheckBox" >';
					HtmlStr += '<img class = "Img_FavoriteListDetail_CheckBox" src="img/Media/checkbox_empty.png" alt="勾選" BeSelected = "No" idMedia = "' + element.idMedia + '" />';
					HtmlStr += '</div>';
					
					HtmlStr += '<div class = "Div_FavoriteListDetail_MediaTitle" >';
					HtmlStr += '<label class = "Lbl_FavoriteListDetail_MediaTitle" >' + element.nameTitle + '</label>';
					HtmlStr += '</div>';
					
					HtmlStr += '<div class = "Div_FavoriteListDetail_AlbumName" >';
					HtmlStr += '<label class = "Lbl_FavoriteListDetail_AlbumName" >' + element.titleAlbum + '</label>';
					HtmlStr += '</div>';
					
					HtmlStr += '<div class = "Div_FavoriteListDetail_ArtistName" >';
					HtmlStr += '<label class = "Lbl_FavoriteListDetail_ArtistName" >' + element.nameArtist + '</label>';
					HtmlStr += '</div>';
					
					HtmlStr += '<div class = "Div_FavoriteListDetail_Length" >';
					HtmlStr += '<label class = "Lbl_FavoriteListDetail_Length" >' + element.lengthMedia + '</label>';
					HtmlStr += '</div>';
					
					HtmlStr += '<div class = "Div_FavoriteListDetail_FileName" >';
					HtmlStr += '<label class = "Lbl_MediaDetail_FileName" >' + element.nameMediaFile + '</label>';
					HtmlStr += '</div>';
						
					HtmlStr += '</div>';
					MediaCount++;
				});
			}
			else
			{
				HtmlStr += '<label style = "position:absolute; left:5%; top:40%" >抱歉，此歌單下沒有相關音樂檔案</label>';
			}
			
			$('.Div_AudioListTitle').text( '歌單 >> ' + FavoriteListName );
			$('.Div_AudioListArea').html(HtmlStr);
			
			// 列表用顏色區隔每一ROW
			$('.Div_FavoriteListDetail:nth-child(even)').css( "background-color", "#eefcef" );
			$('.Div_FavoriteListDetail:nth-child(odd)').css( "background-color", "#f0fafe" );
			
			ToolBarBtn_AllControl( false );
			
			
			//////
			////// 事件
			//////
			
			$('#Div_AddToNewList').hide();
			$('.Div_BackLevel_ToolBar').hide();
			
			// 歌曲作 CheckBox 的選取動作
			$('.Img_FavoriteListDetail_CheckBox').click( function ()
			{
				// 如果判定是已勾選
				if ( 'Yes' == $(this).attr( 'BeSelected' ) )
				{
					// 做出不勾選的效果
					$(this).attr( 'src', 'img/Media/checkbox_empty.png' );
					$(this).attr( 'BeSelected', 'No' );
					AddListCount--;
					
					if ( 0 >= AddListCount )
					{
						$('.Img_MediaList_ReplacePlayingQueue').fadeTo( 300, 0.3 );
						$('.Img_MediaList_AppendPlayingQueue').fadeTo( 300, 0.3 );
						$('.Img_MediaList_RemoveFavoriteMedia' ).fadeTo( 300, 0.3 );
						$('.Img_MediaList_ReplacePlayingQueue').css( 'cursor', 'default' );
						$('.Img_MediaList_AppendPlayingQueue').css( 'cursor', 'default' );
						$('.Img_MediaList_RemoveFavoriteMedia' ).css( 'cursor', 'default' );
					}
				}
				// 如果判定非已勾選
				else
				{
					// 做出勾選效果
					$(this).attr( 'src', 'img/Media/checkbox.png' );
					$(this).attr( 'BeSelected', 'Yes' );
					AddListCount++;
					
					if ( 0 < AddListCount )
					{
						$('.Img_MediaList_ReplacePlayingQueue').fadeTo( 300, 1 );
						$('.Img_MediaList_AppendPlayingQueue').fadeTo( 300, 1 );
						$('.Img_MediaList_RemoveFavoriteMedia' ).fadeTo( 300, 1 );
						$('.Img_MediaList_ReplacePlayingQueue').css( 'cursor', 'pointer' );
						$('.Img_MediaList_AppendPlayingQueue').css( 'cursor', 'pointer' );
						$('.Img_MediaList_RemoveFavoriteMedia' ).css( 'cursor', 'pointer' );
					}
				}
			});
			
			
			
			// 回上一頁
			$('.Img_BackLevel').click( function ()
			{
				GetFavoriteList( IsNotPlayQueue );
			} );
			
			
			
			// 標題的 CheckBox 要進行全選或全不選
			$('.SelectAll').click( function ()
			{
				if ( 'Yes' == $(this).attr( "BeSelected" ) )
				{
					$(this).attr( 'BeSelected', 'No' );
					$('.Img_FavoriteListDetail_CheckBox').attr( 'src', 'img/Media/checkbox_empty.png' );
					$('.Img_FavoriteListDetail_CheckBox').attr( 'BeSelected', 'No' );
					
					AddListCount = 0;
					
					$('.Img_MediaList_ReplacePlayingQueue').fadeTo( 300, 0.3 );
					$('.Img_MediaList_AppendPlayingQueue').fadeTo( 300, 0.3 );
					$('.Img_MediaList_RemoveFavoriteMedia' ).fadeTo( 300, 0.3 );
					$('.Img_MediaList_ReplacePlayingQueue').css( 'cursor', 'default' );
					$('.Img_MediaList_AppendPlayingQueue').css( 'cursor', 'default' );
					$('.Img_MediaList_RemoveFavoriteMedia' ).css( 'cursor', 'default' );
				}
				else
				{
					$(this).attr( 'BeSelected', 'Yes' );
					$('.Img_FavoriteListDetail_CheckBox').attr( 'src', 'img/Media/checkbox.png' );
					$('.Img_FavoriteListDetail_CheckBox').attr( 'BeSelected', 'Yes' );
					
					AddListCount = MediaCount;
					
					$('.Img_MediaList_ReplacePlayingQueue').fadeTo( 300, 1 );
					$('.Img_MediaList_AppendPlayingQueue').fadeTo( 300, 1 );
					$('.Img_MediaList_RemoveFavoriteMedia' ).fadeTo( 300, 1 );
					$('.Img_MediaList_ReplacePlayingQueue').css( 'cursor', 'pointer' );
					$('.Img_MediaList_AppendPlayingQueue').css( 'cursor', 'pointer' );
					$('.Img_MediaList_RemoveFavoriteMedia' ).css( 'cursor', 'pointer' );
				}
			} );
			
			

			
			// 取代播放中
			$('.Img_MediaList_ReplacePlayingQueue' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				// 開啟等待中視窗
				$('.Div_WaitAjax').dialog('open');
			
				IsReplayceList = true;
				IsPlayingQueueChanged = true;
				CheckWhatSelected_InsertToPlayingQueue();
			});
			
			
			
			// 加入播放中
			$('.Img_MediaList_AppendPlayingQueue' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				// 開啟等待中視窗
				$('.Div_WaitAjax').dialog('open');
			
				IsReplayceList = false;
				IsPlayingQueueChanged = true;
				CheckWhatSelected_InsertToPlayingQueue();
			});
			
			
			
			// 取代歌單
			$('.Img_MediaList_AppendFavoriteList' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				IsReplayceList = false;
				CheckWhatSelected_OpenFavoriteListDialog();
				$('.Div_AddToFavoriteList').dialog('option', 'title', '請選擇歌單後，按「確定」加入新歌');
			});
			
			
			// 從歌單中刪除
			$('.Img_MediaList_RemoveFavoriteMedia' ).click( function ()
			{
				// Disable 中直接跳離
				if ( 3 == Math.round( $(this).css( 'opacity' )*10 ) )
				{
					return;
				}
				
				ToolBarBtn_AllControl_ButNotRemove( false );
	
				// 開啟等待中視窗
				$('.Div_WaitAjax').dialog('open');
			
				NowFavoriteListID = FavoriteListID;
				SelectedFavoriteListMedia_Animate(  );
				// 逐一判斷var IsLastItem = false;

				
				
				
				//CheckWhatFavoriteListMediaSelected_ToDelete();
				//InsertMediasToFavoriteList( FavoriteListID, MediaId_BeAddToList, true );
			});
        }
        else
		{
            rtnresult.result = -1;
		}
    });
	
    request.fail( function ( jqxhr, textStatus )
	{
       rtnresult.result = -2;
    });
}





// 建立 [ 歌單 ]
function CreateFavoriteLists( isPlayingQueue, indexPlaying, timeShift, nameFavoriteList )
{
    var rtnresult =
	{
        result : 0,
        playlists : null
    };
    
	var postData = 
	{
        Action:'InsertPlayList',
        idUser:UserID,
        isPlayingQueue:0,
        indexPlaying:0,
        timeShift:'00:00:00',
		namePlayList:nameFavoriteList
    };
	
    var request = CallAjax('MediaPost.php', postData, 'json');
	
    request.done( function ( Msg, statustext, jqxhr )
	{
		// 建立清單之後，現在只會在清單對話框顯示而已，不會有後續加歌的連續動作。只有在對話框按下「確定」才會
		UpdateDialog_GetFavoriteList();
	});
	
    request.fail(function (jqxhr, textStatus)
	{
        rtnresult.result = -2;
    });
}




// 取得現正播放清單的所有歌曲檔，並判斷是否播放
function GetMediasOfPlayingQueue_FillDiv_AndPlay( )
{
    var result = 0;
	
    var postData = 
	{
        Action:'GetPlayListMedia',
        idUser:UserID,
        idPlaying:PlayingQueueID
    };
	
    var request = CallAjax( 'MediaPost.php', postData, 'json' )
	
    request.done( function ( Msg, statustext, jqxhr )
	{
		if( Msg != null )
		{
            var HtmlStr = "";
			MediaCount = 0;
			PlayingQueueData = [];
			PlayingQueueRowTop = [];
			
			// 取回詳細資料，顯示在播放中視窗內
            $.each( Msg, function( index, element )
			{
                HtmlStr += '<div class = "Div_PlayingQueue_Row DisableSelect" draggable = "false" idMedia = "' + element.idMedia + '">';
                
                    HtmlStr += '<div class = "Div_PlayingQueue_PlayIcon DisableSelect" draggable = "false" >';
                        HtmlStr += '<img class ="Img_PlayingQueue_PlayIcon DisableSelect" draggable = "false" src="img/Media/PlayingQueue_Playing.png" alt="◆" title = "播放中" />';
                    HtmlStr += '</div>';
                        
                    HtmlStr += '<div class = "Div_PlayingQueue_MediaTitle DisableSelect" draggable = "false" >';
                        HtmlStr += '<label class = "Lbl_PlayingQueue_MediaTitle DisableSelect" idMedia = "' + element.idMedia + '" >' + element.nameTitle + '</label>';
                    HtmlStr += '</div>';
                    
                    HtmlStr += '<div class = "Div_PlayingQueue_Remove DisableSelect" draggable = "false" >';
                        HtmlStr += '<img class ="Img_PlayingQueue_Remove DisableSelect" draggable = "false" src="img/Media/PlayingQueue_Remove_S.png" alt="◎" title = "移除" />';
                    HtmlStr += '</div>';
					
                HtmlStr += '</div>';
				
                PlayingQueueData[ MediaCount ] = new MediaInfo( element.idMedia, element.idArtist, element.idAlbum, element.idPath, element.nameMediaFile, element.nameTitle, element.lengthMedia, element.pathFull, element.nameArtist, element.titleAlbum );
				MediaCount++;
            });
			
			// 串好畫面，fade 顯示
			$('.Div_PlayingQueue_Area').html( HtmlStr );
			$('.Div_NowPlayingQueue').fadeTo( 1000, 1 );
			$('.Div_PlayingQueue_Area').fadeTo( 1000, 1 );
				
			// 取得每個 ROW 最原始的高度來當作 Scroll 的定位（因為後來會變的）	
			MediaCount = 0;
			$('.Div_PlayingQueue_Area').scrollTop(0);	// 取得高度之前，要把 Scroll 先捲到最高
			$.each( $('.Div_PlayingQueue_Row'), function()
			{	
				PlayingQueueRowTop[ MediaCount ] = $(this).position().top;
				MediaCount++;
			} );
				
			if ( 0 != MediaCount )
			{
				// 建立播放序列
				CreateTheQueue();
				
				if ( true == IsPlayingQueueChanged )
				{
					// 播放中歌單撥完了而停止的，之後插入歌曲則會
					if ( true == IsPlayEnd_Stoped )
					{
						// 將圖形改為 Stop 圖形
						ChangePlayIcon_AsPause();
						IsPlayEnd_Stoped = false;
					}
					
					// 如果有從播放中清單移除歌曲，FakePlayingQueueIndex 移動到正確位置
					if ( true == IsQueueRemove_AnyMedia )
					{
						if ( true != IsRandomPlay )
						{
							FakePlayingQueueIndex = PlayQueue_AfterRemoveIndex;
						}
						
						IsQueueRemove_AnyMedia = false;
					}
					
					// 依照序列撥歌曲
					PlayTheQueue_Next();
					IsPlayingQueueChanged = false;
				}
				
				
				//////
				////// 事件
				//////
				
				// 移除
				$('.Img_PlayingQueue_Remove').click( function ()
				{
					if ( true == IsPlaying )
					{
						IsPlayingQueueChanged = true;
						PlayQueue_AfterRemoveIndex = $('.Img_PlayingQueue_Remove').index(this);
						
						// 如果移除的是播放中或之後的歌
						if ( PlayingQueueIndex <= PlayQueue_AfterRemoveIndex )
						{
							if ( true != IsRandomPlay )
							{
								FakePlayingQueueIndex--;
							}
						}
						
						// 如果刪除的歌曲，是現在播放之前就要 FakePlayingQueueIndex - 2
						else if ( PlayingQueueIndex > PlayQueue_AfterRemoveIndex )
						{
							if ( true != IsRandomPlay )
							{
								FakePlayingQueueIndex--;
								FakePlayingQueueIndex--;
							}
						}
						
						// 記住 FakePlayingQueueIndex 與狀態為移除
						PlayQueue_AfterRemoveIndex = FakePlayingQueueIndex;
						IsQueueRemove_AnyMedia = true;
					}
						
					// 把該元件移除，並立刻向 Server 回報更新播放中清單
					$(this).parent().parent().remove();
					$('.Div_WaitAjax').dialog('open');
					CheckPlayQueue_Reflash_UpdateDiv();
				});
				
				// 雙擊播放
				$('.Lbl_PlayingQueue_MediaTitle').dblclick( function ()
				{
					PlayingQueueIndex = $('.Lbl_PlayingQueue_MediaTitle').index(this);
					
					// 如果亂數，必須要建立一個以該首歌為頭的亂數表，目前沒有寫這樣函式，所以就暫時讓 FakePlayingQueueIndex 回歸到第 0 首
					if ( true == IsRandomPlay )
					{
						FakePlayingQueueIndex = 0;
					}
					else
					{
						FakePlayingQueueIndex = PlayingQueueIndex;
					}
					
					GetPath_PlaySong( $(this).attr('idMedia') );
					
					// 雙擊所強制執行播放，等同重播，所以要改 IsPlayEnd
					IsPlayEnd_Stoped = false;
				});
			}
			// 如果刪除到沒有歌曲了，要讓播放停止
			else
			{
				// 不循環則要把播放器停止
				if ( IsPlayByVlcOrHtml5_HTML5 == IsPlayByVlcOrHtml5 )
				{
					Html5_Audio_Pause();
				}
				else
				{
					VLC_Audio_Pause();
				}
				
				ChangePlayIcon_AsPlay();
				ChangePic_ToPlayerStop();
				IsQueueRemove_AnyMedia = false;
			}
			
			if (  true == $( ".Div_WaitAjax" ).dialog( "isOpen" ) )
			{
				$('.Div_WaitAjax').dialog( 'close' );
			}
		}
		else
		{
			result = -1;
		}
	} );
    request.fail(function (jqxhr, textStatus)
	{
        result = -2;
    });
}



// 在切換歌曲的時候，才來對刪除歌曲的動作進行處理，將新的歌曲清單送回Server更新
function CheckPlayQueue_Reflash_UpdateDiv()
{
	LoopCount = 0;
	MediaId_BeAddToList = [];
					
	// 以回圈逐一判斷勾選項目有哪些
	$.each( $('.Div_PlayingQueue_Row'), function() 
	{
		MediaId_BeAddToList[ LoopCount ] = $(this).attr( 'idMedia' )
		LoopCount++;
	});
	
	// 回寫 Server 端
	InsertMediasToFavoriteList( PlayingQueueID, MediaId_BeAddToList, true );
}
					





// 刪除「歌單」
function DelFavoriteLists( TmpIdFavoriteList )
{
    var rtnresult =
	{
        result : 0,
        playlists : null
    };
    
	var postData = 
	{
        Action:'DelPlayList',
        idUser:UserID,
        idPlayings:TmpIdFavoriteList
    };
	
    var request = CallAjax('MediaPost.php', postData, 'json');
	
    request.done( function ( Msg, statustext, jqxhr )
	{
		// 更新成功，則重新取回清單
		UpdateDialog_GetFavoriteList( );
		GetFavoriteList( IsNotPlayQueue );
	});
	
    request.fail(function (jqxhr, textStatus)
	{
        rtnresult.result = -2;
    });
}



// 更新「歌單」
function UpdateFavoriteLists( idPlaying, nameFavoriteList )
{
    var rtnresult =
	{
        result : 0,
        playlists : null
    };
    
	var postData = 
	{
        Action:'UpdatPlayList',
        idUser:UserID,
        idPlaying:idPlaying,
		namePlayList:nameFavoriteList
    };
	
    var request = CallAjax('MediaPost.php', postData, 'json');
	
    request.done( function ( Msg, statustext, jqxhr )
	{
		// 更新成功，則重新取回清單
		GetFavoriteList( IsNotPlayQueue );
	});
	
    request.fail(function (jqxhr, textStatus)
	{
        rtnresult.result = -2;
    });
}
		



// 取得歌單清單，僅資料，放在公用變數 FavoriteListData[] 中
function UpdateDialog_GetFavoriteList( )
{
	var postData = 
	{
        Action:'GetPlayList',
        idUser:UserID,
        isPlayingQueue:false
    };
	
    var request = CallAjax('MediaPost.php', postData, 'json');
	
    request.done( function ( Msg, statustext, jqxhr )
	{
		FavoriteListData = [];
		
		$.each( Msg, function( index, element )
		{
			FavoriteListData[ index ] = new FavoriteListInfo( element.idPlaying, element.isPlayingQueue, element.indexPlaying, element.timeShift, element.namePlayList );             
		});
		
		UpdateDialog_AddToFavoriteList();
	});
	
    request.fail(function (jqxhr, textStatus)
	{ 
       alert('抱歉資料取得失敗');          
    });   
}





// 取得播放中歌單的ID
function GetPlayingQueueID()
{
	GetFavoriteList( IsPlayQueue );
}


// 新增 專輯 到 [歌單]
function InsertAlbumsToFavoriteList( idPlaying, idAlbums, DeleteFlag )
{
    var postData =
	{
        Action:'InsertAlbumToPlayList',
        idUser:UserID,
        idPlaying:idPlaying,
        idAlbums:idAlbums,
        DeleteFlag:DeleteFlag
    };    
	
	var StrTarget = " [歌單] ";
	
	if ( PlayingQueueID == idPlaying )
	{
		StrTarget = " [播放中歌單] ";
	}
	
	var StrAct = "加入";
	
	if ( true == DeleteFlag )
	{
		StrAct = "取代";
	}
	
    var request = CallAjax( 'MediaPost.php', postData, 'json' )
	
    request.done( function ( msg, statustext, jqxhr )
	{
        if( msg != null )
		{ 
            if( msg != -1 )
			{
				if ( PlayingQueueID == idPlaying )
				{
					// 如果現在是播放中，單首循環，
					if ( true == IsRepeatOne )
					{
						IsRepeatAll = false;
						IsRepeatOne = false;
						$( '.PC_Repeat' ).attr( 'src', 'img/Media/Player02_Repeat_Off.png' );
					}
					
					// 增加歌曲到現正播放清單之後，必須要更新 FavoriteListData
					GetMediasOfPlayingQueue_FillDiv_AndPlay();
				}
				else
				{
					$('.Div_WaitAjax').dialog('close');
				}
			}
            else
			{
					$('.Div_WaitAjax').dialog('close');
                alert( StrAct + StrTarget + '失敗' );
			}
        }
        else
		{
					$('.Div_WaitAjax').dialog('close');
            alert( StrAct + StrTarget + '失敗' );
		}
    });
    request.fail(function (jqxhr, textStatus)
	{ 
					$('.Div_WaitAjax').dialog('close');
		alert( StrAct + StrTarget + '失敗' );
    });         
}





// 新增 歌手 到 [歌單]
function InsertArtistsToFavoriteList( idPlaying, idArtists, DeleteFlag )
{
    var postData =
	{
        Action:'InsertArtistToPlayList',
        idUser:UserID,
        idPlaying:idPlaying,
        idArtists:idArtists,
        DeleteFlag:DeleteFlag
    };
	
	var StrTarget = " [歌單] ";
	
	if ( PlayingQueueID == idPlaying )
	{
		StrTarget = " [播放中] ";
	}
	
	var StrAct = "加入";
	
	if ( true == DeleteFlag )
	{
		StrAct = "取代";
	}
	
    var request = CallAjax( 'MediaPost.php', postData, 'json' )
	
    request.done( function ( msg, statustext, jqxhr )
	{
        if( msg != null )
		{ 
            if( msg != -1 )
			{
				if ( PlayingQueueID == idPlaying )
				{
					// 如果現在是播放中，單首循環，
					if ( true == IsRepeatOne )
					{
						IsRepeatAll = false;
						IsRepeatOne = false;
						$( '.PC_Repeat' ).attr( 'src', 'img/Media/Player02_Repeat_Off.png' );
					}
					
					// 增加歌曲到現正播放清單之後，必須要更新 FavoriteListData
					GetMediasOfPlayingQueue_FillDiv_AndPlay();
				}
				else
				{
					$('.Div_WaitAjax').dialog('close');
				}
			}
            else
			{
					$('.Div_WaitAjax').dialog('close');
                alert( StrAct + StrTarget + '失敗' );
			}
        }
        else
		{
					$('.Div_WaitAjax').dialog('close');
        	alert( StrAct + StrTarget + '失敗' );
		}
    });
    request.fail(function (jqxhr, textStatus)
	{ 
					$('.Div_WaitAjax').dialog('close');
    	alert( StrAct + StrTarget + '失敗' );
    });         
}





// 新增 歌曲 到 [歌單]
function InsertMediasToFavoriteList( idPlaying, Medias, DeleteFlag )
{
    var postData =
	{
        Action:'InsertMediaToPlayList',
        idUser:UserID,
        idPlaying:idPlaying,
        Medias:Medias,
        DeleteFlag:DeleteFlag
    };
	
	var StrTarget = " [歌單] ";
	
	if ( PlayingQueueID == idPlaying )
	{
		StrTarget = " [播放中] ";
	}
	
	var StrAct = "加入";
	
	if ( true == DeleteFlag )
	{
		StrAct = "取代";
	}
	
    var request = CallAjax( 'MediaPost.php', postData, 'json' )
	
    request.done( function ( msg, statustext, jqxhr )
	{
        if( msg != null )
		{ 
            if( msg != -1 )
			{
				if ( PlayingQueueID == idPlaying )
				{
					// 如果現在是播放中，單首循環，
					if ( true == IsRepeatOne )
					{
						IsRepeatAll = false;
						IsRepeatOne = false;
						$( '.PC_Repeat' ).attr( 'src', 'img/Media/Player02_Repeat_Off.png' );
					}
					
					// 增加歌曲到現正播放清單之後，必須要更新 FavoriteListData
					GetMediasOfPlayingQueue_FillDiv_AndPlay();
				}
				else
				{
					$('.Div_WaitAjax').dialog('close');
				}
			}
            else
			{
					$('.Div_WaitAjax').dialog('close');
                alert( StrAct + StrTarget + '失敗' );
			}
        }
        else
		{
					$('.Div_WaitAjax').dialog('close');
        	alert( StrAct + StrTarget + '失敗' );
		}
    });
    request.fail(function (jqxhr, textStatus)
	{ 
					$('.Div_WaitAjax').dialog('close');
    	alert( StrAct + StrTarget + '失敗' );
    });         
}





// 新增 目錄 到 [歌單]
function InsertFoldersToFavoriteList( idPlaying, idPaths, DeleteFlag )
{
    var postData =
	{
        Action:'InsertPathToPlayList',
        idUser:UserID,
        idPlaying:idPlaying,
        idPaths:idPaths,
        DeleteFlag:DeleteFlag
    };
	
	var StrTarget = " [歌單] ";
	
	if ( PlayingQueueID == idPlaying )
	{
		StrTarget = " [播放中歌單] ";
	}
	
	var StrAct = "加入";
	
	if ( true == DeleteFlag )
	{
		StrAct = "取代";
	}
	
    var request = CallAjax( 'MediaPost.php', postData, 'json' )
	
    request.done( function ( msg, statustext, jqxhr )
	{
        if( msg != null )
		{ 
            if( msg != -1 )
			{
				if ( 0 <  MediaId_BeAddToList.length )
				{
					// ToAdd
					InsertMediasToFavoriteList( idPlaying, MediaId_BeAddToList, false );
				}
				else
				{
					if ( PlayingQueueID == idPlaying )
					{
						// 如果現在是播放中，單首循環，
						if ( true == IsRepeatOne )
						{
							IsRepeatAll = false;
							IsRepeatOne = false;
							$( '.PC_Repeat' ).attr( 'src', 'img/Media/Player02_Repeat_Off.png' );
						}
						
						// 增加歌曲到現正播放清單之後，必須要更新 FavoriteListData
						GetMediasOfPlayingQueue_FillDiv_AndPlay();
					}
					else
					{
						$('.Div_WaitAjax').dialog('close');
					}
				}
			}
            else
			{
					$('.Div_WaitAjax').dialog('close');
                alert( StrAct + StrTarget + '失敗' );
			}
        }
        else
		{
					$('.Div_WaitAjax').dialog('close');
        	alert( StrAct + StrTarget + '失敗' );
		}
    });
    request.fail(function (jqxhr, textStatus)
	{ 
					$('.Div_WaitAjax').dialog('close');
    	alert( StrAct + StrTarget + '失敗' );
    });         
}





// 新增 [歌單] 到 [現正播放清單] ， 注意，不能加入歌單到歌單中，後端PHP會回傳 Fail]
function InsertFavoriteListsToPlaying( idPlaying, idFavoriteLists, DeleteFlag )
{
    var postData =
	{
        Action:'InsertPlayListToNowPlay',
        idUser:UserID,
        idPlaying:idPlaying,
        idPlayings:idFavoriteLists,		// 小心，後端參數取名不同
        DeleteFlag:DeleteFlag
    };
	
	var StrTarget = " [歌單] ";
	
	if ( PlayingQueueID == idPlaying )
	{
		StrTarget = " [播放中] ";
	}
	
	var StrAct = "加入";
	
	if ( true == DeleteFlag )
	{
		StrAct = "取代";
	}
	
    var request = CallAjax( 'MediaPost.php', postData, 'json' )
	
    request.done( function ( msg, statustext, jqxhr )
	{	
        if( msg != null )
		{ 
            if( msg != -1 )
			{
				if ( PlayingQueueID == idPlaying )
				{
					// 如果現在是播放中，單首循環，
					if ( true == IsRepeatOne )
					{
						IsRepeatAll = false;
						IsRepeatOne = false;
						$( '.PC_Repeat' ).attr( 'src', 'img/Media/Player02_Repeat_Off.png' );
					}
					
					// 增加歌曲到現正播放清單之後，必須要更新 FavoriteListData
					GetMediasOfPlayingQueue_FillDiv_AndPlay();
				}
				else
				{
					$('.Div_WaitAjax').dialog('close');
				}
			}
            else
			{
					$('.Div_WaitAjax').dialog('close');
                alert( StrAct + StrTarget + '失敗' );
			}
        }
        else
		{
					$('.Div_WaitAjax').dialog('close');
        	alert( StrAct + StrTarget + '失敗' );
		}
    });
    request.fail(function (jqxhr, textStatus)
	{ 
					$('.Div_WaitAjax').dialog('close');
    	alert( StrAct + StrTarget + '失敗' );
    });         
}





function CallAjax(url, data, datatype)
{
    var request = $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType: datatype
    });
    return request;
}

function RandomCreateArray(arrlength)
{
    var i, arr = [];
	
    for (i = 0; i < arrlength; i++)
	{
        arr[i] = i + 1;
    }
    shuffle(arr); 
    return arr;
}
 
function shuffle(array)
{
    var p, n, tmp;
    for (p = array.length; p;) 
	{
        n = Math.random() * p-- | 0;
        tmp = array[n];
        array[n] = array[p];
        array[p] = tmp;        
    }
}



// 建立播放順序，根據公用變數決定是否亂數排歌曲
function CreateTheQueue()
{
	// 如果有歌曲才建立 Queue
	if ( 0 < PlayingQueueData.length )
	{
		FakePlayingQueue = [];
		
		for ( var i = 0; i < PlayingQueueData.length; i++ )
		{
			FakePlayingQueue[ i ] = i;
		}
		
		// 亂數播放則重新亂排過
		if ( true == IsRandomPlay )
		{
			shuffle( FakePlayingQueue );
		}
		
		// 更新了Playing Queue之後，才把播放回到第一首
		if ( true == IsPlayingQueueChanged )
		{
			FakePlayingQueueIndex = -1;		// 現在播放第幾首了，起始值要設定為 -1 ，因為呼叫 NEXT 會自動+1
		}
	}
}



// 取得歌曲在磁碟位置，並播放歌曲
function GetPath_PlaySong( idMedia )
{
    var postData =
	{
        Action:'GetPlayPath',
        idUser:UserID,
        idMedia:idMedia      
    }; 
	
    var request = CallAjax('MediaPost.php', postData, 'json')       
    request.done( function (msg, statustext, jqxhr)
	{         
        if( msg != null )
		{
			// 有歌曲就播放
            if( msg != '' )
			{
				if ( IsPlayByVlcOrHtml5_HTML5 == IsPlayByVlcOrHtml5 )
				{
					$('#Main_AudioPlayer').attr('src', msg );
					$('#Main_AudioPlayer').get(0).play();
				}
				else
				{
					// 用 HTTPS 會無法讓 VLC 播放
					var ServerDomain = window.location.host;
					msg = "http://" + ServerDomain + "/" + msg;
					
					if ( VLC_Player.playlist.isPlaying ) 
					{
           			 	VLC_Player.playlist.stop();
					}
					
					VLC_Player.playlist.clear();
					VLC_Player.playlist.add( encodeURI( msg ) );
					VLC_Player.playlist.play();
				}
				
				
				
				// 播放開始時，播放器該呈現的顯示
				ChangePic_ToPlayerStart( PlayingQueueData[ PlayingQueueIndex ].nameTitle, "專輯:" + PlayingQueueData[ PlayingQueueIndex ].titleAlbum, "歌手:" + PlayingQueueData[ PlayingQueueIndex ].nameArtist );
				
				
				// 歌曲是哪一首  Going  ToAdd
				$('.Img_PlayingQueue_PlayIcon:eq('+ PlayingQueueIndex_Last +')').fadeTo( 200, 0 );
				$('.Lbl_PlayingQueue_MediaTitle:eq('+ PlayingQueueIndex_Last +')').css( 'color', '#fff' );
				
				$('.Img_PlayingQueue_PlayIcon:eq('+ PlayingQueueIndex +')').fadeTo( 200, 1 );
				$('.Lbl_PlayingQueue_MediaTitle:eq('+ PlayingQueueIndex +')').css( 'color', '#ff7200' );
				
				IsPlaying = true;
				ChangePlayIcon_AsPause();
				PlayingQueueIndex_Last = PlayingQueueIndex;
				
				
				// 將播放中清單捲到該首歌的位置
				var ScrollNow = PlayingQueueRowTop[ PlayingQueueIndex ];
				
				if ( 100 > ScrollNow )
				{
					ScrollNow = 0;
				}
				$('.Div_PlayingQueue_Area').animate( {scrollTop:ScrollNow}, 700  );
				
			}
			// 否則下一首
            else
			{
                PlayTheQueue_Next();
			}
        }
		// 否則下一首
        else
		{
            PlayTheQueue_Next();
		}
    });
    request.fail(function (jqxhr, textStatus)
	{        
    	alert('網路回應錯誤，請再次操作');          
    });
}



// 工具列按鈕全部顯示或隱藏
function ToolBarBtn_AllControl( AllEnable )
{
	if ( true == AllEnable )
	{
		$('.Img_MediaList_ReplacePlayingQueue').fadeTo( 300, 1 );
		$('.Img_MediaList_AppendPlayingQueue').fadeTo( 300, 1 );
		$('.Img_MediaList_AppendFavoriteList').fadeTo( 300, 1 );
		$('.Img_MediaList_KillFavoriteList').fadeTo( 300, 1 );
		$('.Img_MediaList_RemoveFavoriteMedia').fadeTo( 300, 1 );
		$('.Img_MediaList_ReplacePlayingQueue').css( 'cursor', 'pointer' );
		$('.Img_MediaList_AppendPlayingQueue').css( 'cursor', 'pointer' );
		$('.Img_MediaList_AppendFavoriteList').css( 'cursor', 'pointer' );
		$('.Img_MediaList_KillFavoriteList').css( 'cursor', 'pointer' );
		$('.Img_MediaList_RemoveFavoriteMedia').css( 'cursor', 'pointer' );
	}
	else
	{
		$('.Img_MediaList_ReplacePlayingQueue').fadeTo( 300, 0.3 );
		$('.Img_MediaList_AppendPlayingQueue').fadeTo( 300, 0.3 );
		$('.Img_MediaList_AppendFavoriteList').fadeTo( 300, 0.3 );
		$('.Img_MediaList_KillFavoriteList').fadeTo( 300, 0.3 );
		$('.Img_MediaList_RemoveFavoriteMedia').fadeTo( 300, 0.3 );
		$('.Img_MediaList_ReplacePlayingQueue').css( 'cursor', 'default' );
		$('.Img_MediaList_AppendPlayingQueue').css( 'cursor', 'default' );
		$('.Img_MediaList_AppendFavoriteList').css( 'cursor', 'default' );
		$('.Img_MediaList_KillFavoriteList').css( 'cursor', 'default' );
		$('.Img_MediaList_RemoveFavoriteMedia').css( 'cursor', 'default' );
	}
}



// 工具列按鈕-除了刪除歌單、移除歌曲按鈕之外，全部顯示或隱
function ToolBarBtn_AllControl_ButNotRemove( AllEnable )
{
	if ( true == AllEnable )
	{
		$('.Img_MediaList_ReplacePlayingQueue').fadeTo( 300, 1 );
		$('.Img_MediaList_AppendPlayingQueue').fadeTo( 300, 1 );
		$('.Img_MediaList_AppendFavoriteList').fadeTo( 300, 1 );
		$('.Img_MediaList_ReplacePlayingQueue').css( 'cursor', 'pointer' );
		$('.Img_MediaList_AppendPlayingQueue').css( 'cursor', 'pointer' );
		$('.Img_MediaList_AppendFavoriteList').css( 'cursor', 'pointer' );
	}
	else
	{
		$('.Img_MediaList_ReplacePlayingQueue').fadeTo( 300, 0.3 );
		$('.Img_MediaList_AppendPlayingQueue').fadeTo( 300, 0.3 );
		$('.Img_MediaList_AppendFavoriteList').fadeTo( 300, 0.3 );
		$('.Img_MediaList_ReplacePlayingQueue').css( 'cursor', 'default' );
		$('.Img_MediaList_AppendPlayingQueue').css( 'cursor', 'default' );
		$('.Img_MediaList_AppendFavoriteList').css( 'cursor', 'default' );
	}
}


