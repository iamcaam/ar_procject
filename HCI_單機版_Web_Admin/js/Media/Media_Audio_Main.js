

var Page_MediaType_Albums 							= 1;		// Page_MediaType 的常數，識別目前在那個頁面
var Page_MediaType_Albums_Media 					= 2;
var Page_MediaType_Artists 							= 3;
var Page_MediaType_Artists_Media 					= 4;
var Page_MediaType_Folders 							= 5;
var Page_MediaType_FavoriteList 					= 6;
var Page_MediaType_FavoriteList_Media 				= 7;
var Page_MediaType_Playing 							= 8;
var Page_MediaType_Search 							= 9;


var CreateFavoriteListType_ThenInsert 				= 1;
var CreateFavoriteListType_ThenNothing 				= 2;
var CreateFavoriteListType_ThenGetAllFavoriteList 	= 3;

var IsPlayByVlcOrHtml5_HTML5 						= 1;
var IsPlayByVlcOrHtml5_VLC 							= 2;

var VLC_State_CLOSE 								= 0;
var VLC_State_OPENING 								= 1;
var VLC_State_BUFFERING 							= 2;
var VLC_State_PLAYING 								= 3;
var VLC_State_PAUSED 								= 4;
var VLC_State_STOPPING 								= 5;



var RootPath = "";								// 記錄根目錄路徑（用來判斷回上層是否回到了跟目錄，不用顯示 BACK 按鈕 ）-「檔案結構模式才用到」
var EveryLevelPath = [];						// 記錄回到父親的父親的路徑-「檔案結構模式才用到」
var EveryLevelName = [];						// 記錄回到父親的父親的名稱-「檔案結構模式才用到」
var NowLevel = 0;								// 記錄目前在哪一層-「檔案結構模式才用到」
var UserID = "";								// 全域紀錄 User ID，從 Web Access 傳遞而藍
var RootPathID = "";							// 全域紀錄 User ID，從 Web Access 傳遞而藍
var Username = "";								// 全域紀錄 User ID，從 Web Access 傳遞而藍
var LUNName = "";								// 全域紀錄 User ID，從 Web Access 傳遞而藍
var RenameCount = 0;							// 記錄目前有多少「歌單」被勾選，只有選擇一個時，更名按鈕可以按
var AddListCount = 0;							// 記錄目前有多少「歌曲、歌手、專輯、歌單」被勾選，判斷「加入歌單、現正播放」的工具按鈕要不要顯示
var MediaCount = 0;								// 記錄目前有多少個 Media
var VLC_Player;									// 用來控制 VLC ODM 的變數（VLC無法使用JQuery方式取用）

var Page_MediaType = 0;
var AlbumId_BeAddToList = [];					// 要新增的 Album 的 ID Array
var ArtistId_BeAddToList = [];					// 要新增的 Artist 的 ID Array
var MediaId_BeAddToList = [];					// 要新增的 Media 的 ID Array
var FolderId_BeAddToList = [];					// 要新增的 Folder 的 ID Array
var FavoriteListId_BeAddToList = [];			// 要新增的 FavoriteList 的 ID Array

var PlayingQueueRowTop = [];					// 播放中歌單，最初始的位置
var FavoriteListData = [];						// FavoriteList 的 資訊
var PlayingQueueData = [];						// Playing List 內的 歌曲資訊
var FakePlayingQueue = [];						// 記載著播放清單的歌曲順序，可能亂數播放，可能循序播放，
var FakePlayingQueueIndex = -1;					// 現在播放第幾首了，起始值要設定為 -1 ，因為呼叫NEXT會自動+1
var PlayingQueueIndex = 0;						// 現在播放第幾首轉換成Queue當中的歌曲順序，也就是亂數排序過後，取Queue的順序而取的歌曲
var PlayingQueueIndex_Last = 0;					// 上次播放第幾首轉換成Queue當中的歌曲順序，也就是亂數排序過後，取Queue的順序而取的歌曲
var PlayQueue_AfterRemoveIndex = 0;				// 紀錄 PlayQueue 刪除了哪一首
var IsPlaying = false;							// 現在是否播放中
var IsRandomPlay = false;						// 是否亂數播放
var IsRepeatAll = false;						// 是否循環播放
var IsRepeatOne = false;						// 是否循環播放
var IsQueueRemove_AnyMedia = false;				// 是否有從播放中清單移除 
var IsQueueRemove_PlayingMedia = false;			// 是否有從播放中清單移除 播放中那首歌 
var IsQueueRemove_DownMedia = false;			// 是否有從播放中清單移除 播放中之後的歌
var IsQueueRemove_AboveMedia = false;			// 是否有從播放中清單移除 播放中之前的歌（會造成次序錯）
var IsQueueRemove_AboveMedia_AndPushPre = false;// 是否有從播放中清單移除 播放中之前的歌（會造成次序錯），且，還按了往前一首歌的按鈕
var IsPlayingQueueChanged = false;				// 是否現正播放清單被取代或更動
var IsPlayEnd_NeedStop = false;					// 是否因為沒有循環播放而需要在撥完之後「進行停止播放」的設定
var IsPlayEnd_Stoped = false;					// 是否因為沒有循環播放而需要在撥完之後且處於停止狀態
var IsDragProgress = false;						// 是否開始拖拉進度條

var PlayingQueueID = 0;
var CreateFavoriteListID = 0;
var CreateFavoriteListType = 0;
var IsReplayceList = false;
var GetFavoriteListOrQueue = 0;
var IsPlayQueue = 1;
var IsNotPlayQueue = 0;
var IsBrowserCanPlayMp3Flag = true;				// 瀏覽器是否可播放 mp3 格式
var IsBrowserSafari = true;						// Safari 瀏覽器會在MP3判斷上告訴程式「他可以播」，但實際上他不能播放 mp3 
var IsVLCPlugedFlag = true;						// 瀏覽器是否有安裝可用的 VLC
var IsPlayByVlcOrHtml5 = 0;						// 音樂廳要用什麼方式播放音樂	

var isOpera = !!window.opera || navigator.userAgent.indexOf('Opera') >= 0;
var isFirefox = typeof InstallTrigger !== 'undefined';
var isSafari = Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0;
var isChrome = !!window.chrome;
var isIE = /*@cc_on!@*/false;



function InitialSettingAndEvent()
{
	InitialSetting();
	
	// 取得現正播放清單 的 ID, 在函式中，會判斷如果是取得現正播放清單ID，則會做後續的「建立序列」、「撥歌曲」動作，為什麼不寫在這邊？因為這是非同步函式，後續動作必須寫在 Request.Done 那邊
	GetPlayingQueueID();
	
	// 一進入之後選擇「專輯」
	GetAlbums();
	
	// 函式為非同步，在資料回來時，會呼叫 UpdateDialog_AddToFavoriteList 來完成頁面內容
	UpdateDialog_GetFavoriteList( );
	
	// 註冊所有物件的事件
	RegisterEvents( );
	
	CreateDialog_NewFavoriteList( );
	CreateDialog_Message();
	CreateDialog_WaitProcess();
}



function InitialSetting()
{
	// 判斷瀏覽器支援度
	if ( false == IsBrowserCanPlayMp3() )
	{
		if ( false == IsVLCPluged() )
		{
			alert("抱歉，這個瀏覽器不支援播放MP3格式的音樂檔，且沒有安裝 VLC 外掛套件，音樂廳無法正常執行。");
		}
		else
		{
			// 產生 VLC 物件
			IsPlayByVlcOrHtml5 = IsPlayByVlcOrHtml5_VLC;
			VLC_Player = document.getElementById( 'VLC_AudioPlayer' );
			
			// 產生 VLC 專用 Timer ，會不斷去讀取他的播放進度
			var CheckProgress_Timer = setInterval( function(){ VLC_GetProgressValue() }, 500 );
		}
	}
	else
	{
		// Safari 瀏覽器會無法撥 MP3
		if ( isSafari )
		{
			// 產生 VLC 物件
			IsPlayByVlcOrHtml5 = IsPlayByVlcOrHtml5_VLC;
			VLC_Player = document.getElementById( 'VLC_AudioPlayer' );
			
			// 產生 VLC 專用 Timer ，會不斷去讀取他的播放進度
			var CheckProgress_Timer = setInterval( function(){ VLC_GetProgressValue() }, 500 );
		}
		else
		{
			IsPlayByVlcOrHtml5 = IsPlayByVlcOrHtml5_HTML5;
		}
	}
	
	$( '.Lbl_SortOut_Album' ).css( 'background-color', '#ff7200' );
	$( '.Div_NowPlayingQueue' ).fadeTo( 100, 0 );
	setTimeout( "reLocateLayout()", 500 );
	$('#Main_AudioPlayer').get(0).volume = 0.5;
	FavoriteListData = [];
	
	ChangePic_ToPlayerStop();
	IsPlayingQueueChanged = false;
	IsPlaying = false;
	IsPlayEnd_Stoped = true;
}







function RegisterEvents()
{
	// 點選分類「專輯」的事件
	$( '.Lbl_SortOut_Album' ).click( function ()
	{
		$( '.SortOut_Btn' ).css( 'background-color', '#E4F9DE' );
		$( '.Lbl_SortOut_Album' ).css( 'background-color', '#fea321' );
		
		GetAlbums( );
	} );
	
	
	
	// 點選分類「歌手」的事件
	$( '.Lbl_SortOut_Artist' ).click( function ()
	{
		$( '.SortOut_Btn' ).css( 'background-color', '#E4F9DE' );
		$( '.Lbl_SortOut_Artist' ).css( 'background-color', '#fea321' );
		
		GetArtists( );
	} );
	
	
	
	// 點選分類「資料夾」的事件
	$( '.Lbl_SortOut_Folder' ).click( function ()
	{
		$( '.SortOut_Btn' ).css( 'background-color', '#E4F9DE' );
		$( '.Lbl_SortOut_Folder' ).css( 'background-color', '#fea321' );
		
		NowLevel = 0;
		GetFileStruct( RootPathID, "根目錄", "No" );
	} );
	
	
	
	// 點選分類「歌單」的事件
	$( '.Lbl_SortOut_FavoriteList' ).click( function ()
	{
		$( '.SortOut_Btn' ).css( 'background-color', '#E4F9DE' );
		$( '.Lbl_SortOut_FavoriteList' ).css( 'background-color', '#fea321' );
		
		GetFavoriteList( IsNotPlayQueue );
	} );
	
	
	
	
	// 點選 [搜尋 ] 的事件
	$( '.Img_Search' ).click( function ()
	{
		$( '.SortOut_Btn' ).css( 'background-color', '#E4F9DE' );
		Page_MediaType = Page_MediaType_Search;
		Ajax_Search( UserID, $('.TxtBx_Search').attr( 'value' ) );
	} );
	
	
	
	// 點選 [ 加入歌曲到現有歌單 ] 的事件
	$( '.Img_Media_AppendExistList' ).click( function ()
	{
		IsReplayceList = false;
		CheckWhatSelected_OpenFavoriteListDialog();
		$('.Div_AddToFavoriteList').dialog('option', 'title', '請選擇歌單做加入');
	} );
	
	
	
	// 點選 [ 取代歌曲取代現有歌單 ] 的事件
	$( '.Img_Media_ReplaceExistList' ).click( function ()
	{
		IsReplayceList = true;
		CheckWhatSelected_OpenFavoriteListDialog();
		$('.Div_AddToFavoriteList').dialog('option', 'title', '請選擇歌單後，按「確定」加入新歌');
	} );
	
	
	
	// 點選 [ 加入歌曲到現正播放清單 ] 的事件
	$( '.Img_Media_AppendPlayQueue' ).click( function ()
	{
		IsReplayceList = false;
		CheckWhatSelected_InsertToPlayingQueue();
	} );
	
	
	
	// 點選 [ 加入歌曲取代現正播放清單 ] 的事件
	$( '.Img_Media_ReplacePlayQueue' ).click( function ()
	{
		IsReplayceList = true;
		IsPlayingQueueChanged = true;
		CheckWhatSelected_InsertToPlayingQueue();
	} );
	
	
	
	// 亂數播放
	$( '.PC_Random' ).click( function ()
	{
		if ( true == IsRandomPlay )
		{
			IsRandomPlay = false;
			$( '.PC_Random' ).attr( 'src', 'img/Media/Player01_Random_Off.png' );
		}
		else
		{
			IsRandomPlay = true;
			$( '.PC_Random' ).attr( 'src', 'img/Media/Player01_Random_On.png' );
		}
		
		CreateTheQueue();
	} );
	
	
	
	
	// 循環
	$( '.PC_Repeat' ).click( function ()
	{
		// No --> All --> 1 --> No
		
		// All --> 1
		if ( true == IsRepeatAll )
		{
			IsRepeatAll = false;
			IsRepeatOne = true;
			$( '.PC_Repeat' ).attr( 'src', 'img/Media/Player02_Repeat_OnOne.png' );
		}
		// 1 --> No
		else if ( true == IsRepeatOne )
		{
			IsRepeatAll = false;
			IsRepeatOne = false;
			$( '.PC_Repeat' ).attr( 'src', 'img/Media/Player02_Repeat_Off.png' );
		}
		// No --> All
		else if (  ( false == IsRepeatAll ) && ( false == IsRepeatOne ) )
		{
			IsRepeatAll = true;
			IsRepeatOne = false;
			$( '.PC_Repeat' ).attr( 'src', 'img/Media/Player02_Repeat_OnAll.png' );
		}
	} );
	
	
	
	// 暫停或繼續，或重新啟動播放
	$( '.PC_Play' ).click( function ()
	{
		
		// 如果沒有歌曲在播放中清單內，則要給提示訊息
		if ( 0 >= PlayingQueueData.length )
		{
			// 開啟告警對話框：說明沒有歌曲可播放
			$('.Div_Message').dialog('open');
		}
		else
		{
			// 判斷目前是播放或暫停狀態
			if ( IsPlayByVlcOrHtml5_HTML5 == IsPlayByVlcOrHtml5 )
			{
				IsPlaying = !$('#Main_AudioPlayer').get(0).paused;
			}
			else
			{
				var State = VLC_Player.input.state;
				
				if ( VLC_State_PLAYING == State )
				{
					IsPlaying = true;
				}
				else
				{
					IsPlaying = false;
				}
			}
			
			// 已停止
			if ( false == IsPlaying )
			{
				// 播放中歌單撥完了而停止的，再按下 Play 會需要從頭播放
				if ( true == IsPlayEnd_Stoped )
				{
					// 將圖形改為 Stop 圖形
					IsPlayEnd_Stoped = false;
					PlayTheQueue_Next();
				}
				// 一般狀況則為暫停
				else
				{
					// 將圖形改為 Stop 圖形
					ChangePlayIcon_AsPause();
					
					if ( IsPlayByVlcOrHtml5_HTML5 == IsPlayByVlcOrHtml5 )
					{
						Html5_Audio_Play();
					}
					else
					{
						VLC_Audio_Play()
					}
				}
			}
			// 暫停
			else
			{
				ChangePlayIcon_AsPlay();
				
				if ( IsPlayByVlcOrHtml5_HTML5 == IsPlayByVlcOrHtml5 )
				{
					Html5_Audio_Pause();
				}
				else
				{
					VLC_Audio_Pause();
				}
			}
		}
	} );
	
	
	
	// 播放上一歌曲
	$( '.PC_Pre' ).click( function ()
	{
		PlayTheQueue_Pre();
	} );
	
	// Mouse 按下與起來的時候換圖，製作互動效果
	$( '.PC_Pre' ).mousedown( function ()
	{
		$( '.PC_Pre' ).attr( 'src', 'img/Media/Player03_Pre_Click.png' );
	} );
	
	$( '.PC_Pre' ).mouseup( function ()
	{
		$( '.PC_Pre' ).attr( 'src', 'img/Media/Player03_Pre.png' );
	} );
	
	$( '.PC_Pre' ).mouseout( function ()
	{
		$( '.PC_Pre' ).attr( 'src', 'img/Media/Player03_Pre.png' );
	} );
	
	
	
	// 播放下一歌曲
	$( '.PC_Next' ).click( function ()
	{
		PlayTheQueue_Next();
	} );
	
	// Mouse 按下與起來的時候換圖，製作互動效果
	$( '.PC_Next' ).mousedown( function ()
	{
		$( '.PC_Next' ).attr( 'src', 'img/Media/Player05_Next_Click.png' );
	} );
	
	$( '.PC_Next' ).mouseup( function ()
	{
		$( '.PC_Next' ).attr( 'src', 'img/Media/Player05_Next.png' );
	} );
	
	$( '.PC_Next' ).mouseout( function ()
	{
		$( '.PC_Next' ).attr( 'src', 'img/Media/Player05_Next.png' );
	} );
	
	
	// 聲音降低
	$( '.PC_SoundLower' ).click( function ()
	{
		if ( IsPlayByVlcOrHtml5_HTML5 == IsPlayByVlcOrHtml5 )
		{
			Html5_Volume_Lower();
		}
		else
		{
			VLC_Volume_Lower();
		}
	} );
	
	// Mouse 按下與起來的時候換圖，製作互動效果
	$( '.PC_SoundLower' ).mousedown( function ()
	{
		$( '.PC_SoundLower' ).attr( 'src', 'img/Media/Player07_SoundSmall_Click.png' );
	} );
	
	$( '.PC_SoundLower' ).mouseup( function ()
	{
		$( '.PC_SoundLower' ).attr( 'src', 'img/Media/Player07_SoundSmall.png' );
	} );
	
	$( '.PC_SoundLower' ).mouseout( function ()
	{
		$( '.PC_SoundLower' ).attr( 'src', 'img/Media/Player07_SoundSmall.png' );
	} );
	
	
	
	// 聲音提高
	$( '.PC_SoundHigher' ).click( function ()
	{
		if ( IsPlayByVlcOrHtml5_HTML5 == IsPlayByVlcOrHtml5 )
		{
			Html5_Volume_Higher();
		}
		else
		{
			VLC_Volume_Higher();
		}
	} );
	
	// Mouse 按下與起來的時候換圖，製作互動效果
	$( '.PC_SoundHigher' ).mousedown( function ()
	{
		$( '.PC_SoundHigher' ).attr( 'src', 'img/Media/Player08_SoundBig_Click.png' );
	} );
	
	$( '.PC_SoundHigher' ).mouseup( function ()
	{
		$( '.PC_SoundHigher' ).attr( 'src', 'img/Media/Player08_SoundBig.png' );
	} );
	
	$( '.PC_SoundHigher' ).mouseout( function ()
	{
		$( '.PC_SoundHigher' ).attr( 'src', 'img/Media/Player08_SoundBig.png' );
	} );
	
	
	// 聲音打開或關閉
	$( '.PC_SoundMute' ).click( function ()
	{
		if ( IsPlayByVlcOrHtml5_HTML5 == IsPlayByVlcOrHtml5 )
		{
			Html5_Volume_Mute();
		}
		else
		{
			VLC_Volume_Mute();
		}
	} );
	
	// 按在進度條，要能切換到該時間來播放
	$( '.Img_PlayerProgressBar' ).click( function (e)
	{
		if ( IsPlayByVlcOrHtml5_HTML5 == IsPlayByVlcOrHtml5 )
		{
			Html5_Move_Progress(e);
		}
		else
		{
			VLC_Move_Progress(e);
		}
	} );
	
	// 按在進度條，要能切換到該時間來播放
	$( '.Img_PlayerProgressBar_Dark' ).click( function (e)
	{
		if ( IsPlayByVlcOrHtml5_HTML5 == IsPlayByVlcOrHtml5 )
		{
			Html5_Move_Progress(e);
		}
		else
		{
			VLC_Move_Progress(e);
		}
	} );
	
	
	// 可拖拉移動進度條 : MouseDown - 紀錄已經觸發
	$( '.Lbl_PlayerProgress_Tick' ).mousedown( function (e)
	{
		// 確認是否第一次觸發
		if ( false == IsDragProgress )
		{
			IsDragProgress = true;
		}
	} );
	
				
	// 縮小播放中視窗
	$('.Img_PlayingQueue_MinmunBtn').click( function ()
	{
		if ( 0 == $('.Div_PlayingQueue_Area').css( 'opacity' ) )
		{
			TheQueue_HiddenOrShow( true );
			$('.Img_PlayingQueue_MinmunBtn').attr( 'src', 'img/Media/PlayingQueue_MinmunIcon.png' );
		}
		else
		{
			TheQueue_HiddenOrShow( false );
			$('.Img_PlayingQueue_MinmunBtn').attr( 'src', 'img/Media/PlayingQueue_NormalIcon.png' );
		}
	});
	
	
	// 為了拖動進度條而存在的 Mouse 事件
	$(window).mousemove( function (e)
	{
		// 確認是否有過 MouseDown 
		if ( true == IsDragProgress )
		{
			NowLeft = ( e.clientX|e.pageX ) - $( '.Div_PlayerArea' ).position().left;
			if (   ( 0 <= NowLeft )   &&   ( 540 > NowLeft )   )
			{
				$('.Lbl_PlayerProgress_Tick').css( 'left', NowLeft );
			}
		}
	} );
	
	
	
	// 為了拖動進度條而存在的 Mouse 事件
	$(window).mouseup( function (e) 
	{
		if ( true == IsDragProgress )
		{
			if ( IsPlayByVlcOrHtml5_HTML5 == IsPlayByVlcOrHtml5 )
			{
				HTML5_Drag_Progress( e.clientX |e.pageX );
			}
			else
			{
				VLC_Drag_Progress( e.clientX |e.pageX );
			}
		}
	} );
	
	
	
	
	// 捲動瀏覽器時，要把播放中清單固定位置的事件，播放中清單靠下方
	$(window).scroll( function (e)
	{
		// 若，視窗拉小，則主畫面依舊維持700高度，視窗高度則會比700還小
		// 若，視窗高度超過700，則主畫面高度用100％來滿版，此時視窗高度跟主畫面高度相同
		// 若，視窗用瀏覽器的「放大 ( Ctrl 與 + )」，將畫面放大到很大很大，則主畫面高度會超過100%，並出現捲軸（因為內部元件並非用％，而是指定pixel來決定寬、高），此時主畫面高度會大於視窗高度
		// 總和上述三點，要讓播放中清單固定在下方，則要用較小的 getBrowserHeight()
		//var Smaller = $('.Div_MainArea').height() < getBrowserHeight() ? $('.Div_MainArea').height() : getBrowserHeight();
		
		var QueueTop = getBrowserHeight() - $('.Div_NowPlayingQueue').height() + getBrowserScrollY() - 3;
		$('.Div_NowPlayingQueue').css( 'top', QueueTop );
		
		// 由於 getBrowserScrollY() 會有誤差或BUG，所以必須再定位之後再做一次判斷，確認是否播放中清單的 Top + Height 不超過主畫面高度，超過就要拉高回來。
		if ( $('.Div_MainArea').height() < ( QueueTop + $('.Div_NowPlayingQueue').height() ) )
		{
			$('.Div_NowPlayingQueue').css( 'top', $('.Div_MainArea').height() - $('.Div_NowPlayingQueue').height() );
		}
		
		
		// 播放中清單靠右方 100
		var QueueLeft = getBrowserWidth() - $('.Div_NowPlayingQueue').width() + getBrowserScrollX() - 100;
		$('.Div_NowPlayingQueue').css( 'left', QueueLeft );
	} );
}




function TheQueue_HiddenOrShow( IsShow )
{
	if ( true == IsShow )
	{
		// 播放中清單靠下方, 3是邊框寬度
		var QueueTop = getBrowserHeight() - 240 - 3;
		$('.Div_NowPlayingQueue').animate( { height:'240px', top:QueueTop }, 1000 );
		$('.Div_PlayingQueue_Area').fadeTo( 1000, 1 );
		$('.Div_PlayingQueue_Area').css( 'height', '190' );
	}
	else
	{
		// 播放中清單靠下方, 3是邊框寬度
		var QueueTop = getBrowserHeight() - 50 - 3;
		$('.Div_PlayingQueue_Area').fadeTo( 1000, 0 );
		$('.Div_PlayingQueue_Area').css( 'height', '0' );
		$('.Div_NowPlayingQueue').animate( { height:'50px', top:QueueTop }, 1000 );
	}
}

	
// 播放上一首歌
function PlayTheQueue_Pre()
{
	// true 表示不循環播放且播放到最後一首歌了, 就不允許他按下一首了
	if ( true != IsPlayEnd_Stoped )
	{
		MoveToPreIndex();
	
		PlayingQueueIndex = FakePlayingQueue[ FakePlayingQueueIndex ];
		GetPath_PlaySong( PlayingQueueData[ PlayingQueueIndex ].idMedia );
	}
}
		
		

// 播放下一首歌
function PlayTheQueue_Next()
{
	// true 表示不循環播放且播放到最後一首歌了, 就不允許他按下一首了
	if ( true != IsPlayEnd_Stoped )
	{
		MoveToNextIndex();
	
		// IsPlayEnd_NeedStop = true 表示不循環播放且播放到最後一首歌了
		if ( true == IsPlayEnd_NeedStop ) 
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
			
			IsPlayEnd_NeedStop = false;
			IsPlayEnd_Stoped = true;
		}
		else
		{
			PlayingQueueIndex = FakePlayingQueue[ FakePlayingQueueIndex ];
			GetPath_PlaySong( PlayingQueueData[ PlayingQueueIndex ].idMedia );
		}
	}
}
		

// 取得上一首歌該有的 Index
function MoveToPreIndex()
{
	if ( true == IsRepeatOne )
	{
		return;
	}
	
	FakePlayingQueueIndex = FakePlayingQueueIndex - 1;
	
	if ( FakePlayingQueueIndex < 0 )
	{
		if ( true == IsRepeatAll )
		{
			FakePlayingQueueIndex = PlayingQueueData.length - 1;
		}
		else
		{
			FakePlayingQueueIndex = 0;
		}
	}
}
		
		

// 取得下一首歌該有的 Index
function MoveToNextIndex()
{
	if ( true == IsRepeatOne )
	{
		return;
	}
	
	FakePlayingQueueIndex = FakePlayingQueueIndex + 1;
	
	if ( FakePlayingQueueIndex > PlayingQueueData.length - 1 )
	{
		// 如果沒有設定循環播放，則設定不用設定 flag ( IsPlayEnd_NeedStop ) 為 true 
		if ( true == IsRepeatAll )
		{
			FakePlayingQueueIndex = 0;
		}
		else
		{
			FakePlayingQueueIndex = -1;
			IsPlayEnd_NeedStop = true;
		}
	}
}
		

// 播放停止時，播放器該呈現的顯示
function ChangePic_ToPlayerStop()
{
	$('.Lbl_Playing_Media').text( "" );
	$('.Lbl_Playing_Album').text( "" );
	$('.Lbl_Playing_Artist').text( "" );
	$('.Lbl_Playing_TimeNow').text( "" );
	$('.Lbl_Playing_TimeTotal').text( "" );
	$('.Lbl_Playing_TimeNow').hide();
	$('.Lbl_Playing_TimeTotal').hide();
	$('.Img_PlayerProgressBar_Dark').hide();
	$('.Img_PlayerProgressBar').hide();
	$('.Lbl_PlayerProgress_Tick').hide();		
}
		

// 播放開始時，播放器該呈現的顯示
function ChangePic_ToPlayerStart( SongName, AlbumName, ArtistName )
{		
	$('.Lbl_Playing_Media').text( SongName );
	$('.Lbl_Playing_Album').text( AlbumName );
	$('.Lbl_Playing_Artist').text( ArtistName );
	$('.Lbl_Playing_TimeNow').show();
	$('.Lbl_Playing_TimeTotal').show();
	$('.Img_PlayerProgressBar_Dark').show();
	$('.Img_PlayerProgressBar').show();
	$('.Lbl_PlayerProgress_Tick').show();
		
}



// 搜尋
function Ajax_Search( idUser,Pattern )
{
    var postData =
	{
        Action:'Search',
        idUser:idUser,
        Pattern:Pattern
    };
	
    var request = CallAjax('MediaPost.php', postData, 'json');
	  
    request.done( function ( Msg, statustext, jqxhr )
	{
        if( Msg != null )
		{
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
			
			MediaCount = 0;
				
			if ( 0 < Msg.length )
			{
				$.each( Msg, function( index, element ) 
				{
					LastListenRow = 0;
					var LoopCount = 0;
					
					// 目錄是 0
					if ( 0 == element.type )
					{
						/*
						// 以新資料更新畫面 //
						HtmlStr += '<div class = "Div_Folder" id = "' + element.idPath + '" idPath = "' + element.idParent + '" >';
							HtmlStr += '<img class = "Img_Folder_CheckBox" src = "img/Media/checkbox_empty.png" alt = "勾選" BeSelected = "No" />';
							HtmlStr += '<img class = "Img_Folder_FolderImg" src = "img/Media/Folder.png" alt = "目錄圖" />';
							HtmlStr += '<img class = "Img_BigItem_ReplacePlayingQueue IsFolder" src = "img/Media/PlayingNow.png" title = "立即播放" MediaID = "' + element.idMedia + '"/>';
							HtmlStr += '<label class = "Lbl_Folder_FolderName" >' + element.name + '</label>';
						HtmlStr += '</div>';
						*/
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
					
					MediaCount++;
				});
			}
			else
			{
				HtmlStr += '<label style = "position:absolute; left:5%; top:40%" >抱歉，搜尋不到相關音樂檔案</label>';
			}
			
			$('.Div_AudioListTitle').text( '搜尋 >> ' );
            $('.Div_AudioListArea').html( HtmlStr );
			$('.Img_BackLevel').fadeTo( 300, 0.3 );
			$('.Img_BackLevel').css( 'cursor', 'default' );
			
			ToolBarBtn_AllControl( false );
			
			
			
			//////
			////// 事件
			//////
			
			
			
			// 將目錄歌曲，作 CheckBox 的選取動作
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
				if ( 0 < AddListCount )
				{
					IsReplayceList = true;
					IsPlayingQueueChanged = true;
					CheckWhatSelected_InsertToPlayingQueue();
				}
			});
			
			// 加入播放中
			$('.Img_MediaList_AppendPlayingQueue' ).click( function ()
			{
				if ( 0 < AddListCount )
				{
					IsReplayceList = false;
					IsPlayingQueueChanged = true;
					CheckWhatSelected_InsertToPlayingQueue();
				}
			});
			
			// 取代歌單
			$('.Img_MediaList_AppendFavoriteList' ).click( function ()
			{
				if ( 0 < AddListCount )
				{
					IsReplayceList = false;
					CheckWhatSelected_OpenFavoriteListDialog();
					$('.Div_AddToFavoriteList').dialog('option', 'title', '請選擇歌單做加入');
				}
			});
        }
        else
		{
			$('.Div_AudioListArea').append( '搜尋無資料' );
		}
    }); 
    request.fail(function (jqxhr, textStatus)
	{
		$('.Div_AudioListArea').append( '搜尋發生錯誤' );
    });     
}





// 從現有「歌單」對話框開啟「新增歌單」對話框
function OpenCreateFavoriteListDialog()
{
	$( '.Div_AddToNewCreateFavoriteList' ).dialog( 'open' );
}




// 檢查使用者選擇什麼，並呼叫出選擇現有「歌單」對話框
function CheckWhatSelected_OpenFavoriteListDialog()
{
	// ToAdd
	if ( 10 <= FavoriteListData.length )
	{
		alert( "最多只能建立10個歌單" );
		return;
	}
	
	if ( Page_MediaType_Albums == Page_MediaType )
	{
		CheckWhatAlbumSelected();
	}
	else if ( Page_MediaType_Artists == Page_MediaType )
	{
		CheckWhatArtistSelected();
	}
	else if ( ( Page_MediaType_Artists_Media  == Page_MediaType ) || ( Page_MediaType_Albums_Media  == Page_MediaType ) )
	{
		CheckWhatMediaSelected();
	}
	else if ( ( Page_MediaType_Folders == Page_MediaType ) || ( Page_MediaType_Search  == Page_MediaType ) )
	{
		CheckWhatFolderSelected();
		CheckWhatMediaInFolderSelected();
	}
	
	$( '.Div_AddToFavoriteList' ).dialog( 'open' );
}




// 檢查使用者選擇什麼，並將所選歌曲加入或取代至「現正播放清單」
function CheckWhatSelected_InsertToPlayingQueue()
{
	if ( Page_MediaType_Albums == Page_MediaType )
	{
		CheckWhatAlbumSelected();
		InsertAlbumsToFavoriteList( PlayingQueueID, AlbumId_BeAddToList, IsReplayceList );
	}
	else if ( Page_MediaType_Artists == Page_MediaType )
	{
		CheckWhatArtistSelected();
		InsertArtistsToFavoriteList( PlayingQueueID, ArtistId_BeAddToList, IsReplayceList );
	}
	else if ( ( Page_MediaType_Artists_Media  == Page_MediaType ) || ( Page_MediaType_Albums_Media  == Page_MediaType ) )
	{
		CheckWhatMediaSelected();
		InsertMediasToFavoriteList( PlayingQueueID, MediaId_BeAddToList, IsReplayceList );
	}
	else if ( ( Page_MediaType_Folders == Page_MediaType ) || ( Page_MediaType_Search  == Page_MediaType ) )
	{
		CheckWhatFolderSelected();
		CheckWhatMediaInFolderSelected();
				
		if ( 0 < FolderId_BeAddToList.length )
		{
			InsertFoldersToFavoriteList( PlayingQueueID, FolderId_BeAddToList, IsReplayceList );
		}
		else if ( 0 < MediaId_BeAddToList.length )
		{
			InsertMediasToFavoriteList( PlayingQueueID, MediaId_BeAddToList, IsReplayceList );
		}
	}
	else if ( ( Page_MediaType_FavoriteList  == Page_MediaType ) )
	{
		CheckWhatFavoriteListSelected();
		InsertFavoriteListsToPlaying( PlayingQueueID, FavoriteListId_BeAddToList, IsReplayceList );
	}
	else if ( Page_MediaType_FavoriteList_Media == Page_MediaType )
	{
		CheckWhatFavoriteListMediaSelected();
		InsertMediasToFavoriteList( PlayingQueueID, MediaId_BeAddToList, IsReplayceList );
	}
}






// 更新對話框的「歌單」清單 ( Div_AddToFavoriteList ) 內容
function UpdateDialog_AddToFavoriteList()
{
	//
	// Dialog	
	// 點選 [ 存入現有歌單 ] 時，跳出的對話框，先把畫面鑲在這裡，在事件當中會串 Dialog Event
	//
	SelectFavoriteList_ID = "";
	LoopCount = 0;
	var	HtmlStr = "";
		
	if ( 0 < FavoriteListData.length )
	{
		$.each( FavoriteListData, function() 
		{
			HtmlStr += '<div class = "Div_FavoriteListRow" id = ' + FavoriteListData[ LoopCount ].idPlaying + ' BeSelected = "No">';
				HtmlStr += '<img class = "Img_FavoriteListRow_RadioBtn" src = "img/Media/RadioButton_Empty.png" alt = "勾選"/>';
				HtmlStr += '<label class = "Lbl_FavoriteListRow_Name" >' + FavoriteListData[ LoopCount ].nameFavoriteList + '</label>';
			HtmlStr += '</div>';
			LoopCount++;
		} );
	}
	
	HtmlStr += '<label class = "Lbl_ChangeToCreate_FavoriteList DisableSelect" >建立新的歌單</label>';
	$('.Div_AddToFavoriteList').html( HtmlStr );
	

	// 對話框內的 Radio Button 的事件
	if ( 0 < FavoriteListData.length )
	{
		$('.Div_FavoriteListRow').click( function ()
		{
			$('.Img_FavoriteListRow_RadioBtn').attr( 'src', 'img/Media/RadioButton_Empty.png' );
			$(this).children('.Img_FavoriteListRow_RadioBtn').attr( 'src', 'img/Media/RadioButton.png' );
			SelectFavoriteList_ID = $(this).attr('id');
		} );
	}	
				
			
	//// [ 存入現有歌單 ] 對話框與事件
	//// 
	//// 這裡是 JQuery UI 所提供的，寫法固定，把要顯示在對話框的內容先鑲在 HTML 中，並設定隱藏，並且撰寫以下片段，產生一個對話框物件。
	//// 然後再想觸發的按鈕事件（比方點新增按鈕會需要這個畫面來當對話框）中呼叫 Dialog( 'open' )就可以了
	////
	$( '.Div_AddToFavoriteList' ).dialog( 
	{
		autoOpen: false,
		resizable: false,
		width:500,
		height:600,
		modal: true,
		show: 
		{
			effect: 'fade',
			duration: 1000
		},
		hide: 
		{
			effect: 'fade',
			duration: 1000
		},
		  
		// 對話框有幾個按鈕，以及按了有什麼事件觸發
		buttons: 
		{
			"確定": function()
			{
				if ( Page_MediaType_Albums == Page_MediaType )
				{
					InsertAlbumsToFavoriteList( SelectFavoriteList_ID, AlbumId_BeAddToList, IsReplayceList );
				}
				else if ( Page_MediaType_Albums_Media  == Page_MediaType )
				{
					InsertMediasToFavoriteList( SelectFavoriteList_ID, MediaId_BeAddToList, IsReplayceList );
				}
				else if ( Page_MediaType_Artists == Page_MediaType )
				{
					InsertArtistsToFavoriteList( SelectFavoriteList_ID, ArtistId_BeAddToList, IsReplayceList );
				}
				else if ( Page_MediaType_Artists_Media  == Page_MediaType )
				{
					InsertMediasToFavoriteList( SelectFavoriteList_ID, MediaId_BeAddToList, IsReplayceList );
				}
				else if ( Page_MediaType_Folders == Page_MediaType )
				{	
					if ( 0 < FolderId_BeAddToList.length )
					{
						InsertFoldersToFavoriteList( SelectFavoriteList_ID, FolderId_BeAddToList, IsReplayceList );
					}
					else if ( 0 < MediaId_BeAddToList.length )
					{
						InsertMediasToFavoriteList( SelectFavoriteList_ID, MediaId_BeAddToList, IsReplayceList );
					}
				}
				else if ( ( Page_MediaType_FavoriteList  == Page_MediaType ) )
				{
					InsertFavoriteListsToPlaying( PlayingQueueID, FavoriteListId_BeAddToList, IsReplayceList );
				}
				else if ( Page_MediaType_FavoriteList_Media == Page_MediaType )
				{
					InsertMediasToFavoriteList( PlayingQueueID, MediaId_BeAddToList, IsReplayceList );
				}
				
				$( this ).dialog( 'close' );
				
				// 開啟等待中視窗
				$('.Div_WaitAjax').dialog('open');
			},
			"取消": function()
			{
				$( this ).dialog( 'close' );
			}
		}
	});
	
	
	// 點選建立新的歌單
	$( '.Lbl_ChangeToCreate_FavoriteList' ).click( function ()
	{	
		OpenCreateFavoriteListDialog()
	});
}




// 建立對話框「新增歌單」
function CreateDialog_NewFavoriteList()
{	
	//
	// Dialog		
	// 點選 [ 建立歌單，並將歌曲存入 ] 時，跳出的對話框，先把畫面鑲在這裡，在事件當中會串 Dialog Event
	var HtmlStr = "";
	HtmlStr += '<div id = "Div_CreateContent" title = "建立歌單，並將歌曲存入">';
		HtmlStr += '<label class = "Lbl_Dialog_NewFavoriteList" style = "margin:15px;" >請輸入要建立的「歌單」的名稱，所選擇的專輯內歌曲將會加到此「歌單」中</label>';
		HtmlStr += '<img class = "Img_Dialog_NewFavoriteList" src = "img/Media/FavoriteList.png" alt = "歌單圖" style = "margin:15px;" />';
		HtmlStr += '<input class = "Txt_Dialog_NewFavoriteList_Name DialogInput" type = "text" value ="請輸入名稱" style = "margin:15px;" />';
	HtmlStr += '</div>';
	
	$('.Div_AddToNewCreateFavoriteList').html( HtmlStr );
	
	
				
				
			
	//// [ 建立歌單，並將歌曲存入 ] 對話框與事件
	//// 
	//// 這裡是 JQuery UI 所提供的，寫法固定，把要顯示在對話框的內容先鑲在 HTML 中，並設定隱藏，並且撰寫以下片段，產生一個對話框物件。
	//// 然後再想觸發的按鈕事件（比方點新增按鈕會需要這個畫面來當對話框）中呼叫 Dialog( 'open' )就可以了
	////
	$( '.Div_AddToNewCreateFavoriteList' ).dialog( 
	{
		autoOpen: false,
		resizable: false,
		width:400,
		height:450,
		modal: true,
		show: 
		{
			effect: 'fade',
			duration: 1000
		},
		hide: 
		{
			effect: 'fade',
			duration: 1000
		},
		  
		// 對話框有幾個按鈕，以及按了有什麼事件觸發
		buttons: 
		{
			"建立": function()
			{
				CreateFavoriteListType = CreateFavoriteListType_ThenInsert;
				var TmpInputTxt = $( '#Div_CreateContent' ).children( '.Txt_Dialog_NewFavoriteList_Name' ).attr( 'value' );
				CreateFavoriteLists( false, 0, '00:00:00', TmpInputTxt );
				
				$( this ).dialog( 'close' );
				$( '.Txt_Dialog_NewFavoriteList_Name' ).attr( 'value', '請輸入名稱' );
			},
			"取消": function()
			{
				$( this ).dialog( 'close' );
				$( '.Txt_Dialog_NewFavoriteList_Name' ).attr( 'value', '請輸入名稱' );
			}
		}
	});
}





// 建立對話框「訊息」
function CreateDialog_Message()
{	
	//
	// Dialog		
	// 
	var HtmlStr = "";
	HtmlStr += '<div id = "Div_MessageContent" title = "沒有歌曲">';
		HtmlStr += '<label class = "Lbl_Dialog_Message" >請先從專輯、歌手、資料夾或歌單中選擇要播放的歌曲才能播放</label>';
		//HtmlStr += '<img class = "Img_Dialog_Message" src = "img/Media/AjaxLoader.gif" alt = "等待圖" />';
	HtmlStr += '</div>';
	
	$('.Div_Message').html( HtmlStr );
	
	
				
				
			
	//// [ 訊息 ] 對話框與事件
	//// 
	//// 這裡是 JQuery UI 所提供的，寫法固定，把要顯示在對話框的內容先鑲在 HTML 中，並設定隱藏，並且撰寫以下片段，產生一個對話框物件。
	//// 然後再想觸發的按鈕事件（比方點新增按鈕會需要這個畫面來當對話框）中呼叫 Dialog( 'open' )就可以了
	////
	$( '.Div_Message' ).dialog( 
	{
		autoOpen: false,
		resizable: false,
		width:500,
		height:300,
		modal: true,
		
		show: 
		{
			effect: 'fade',
			duration: 1000
		},
		hide: 
		{
			effect: 'fade',
			duration: 1000
		},
		
		// 對話框有幾個按鈕，以及按了有什麼事件觸發
		buttons: 
		{
			"確定": function()
			{
				$( this ).dialog( 'close' );
			}
		}
	});
}





// 建立對話框「等待中」
function CreateDialog_WaitProcess()
{	
	//
	// Dialog		
	// 
	var HtmlStr = "";
	HtmlStr += '<div id = "Div_WaitAjaxContent" title = "資料處理中，請稍候">';
		HtmlStr += '<label class = "Lbl_Dialog_WaitAjax" ></label>';
		HtmlStr += '<img class = "Img_Dialog_WaitAjax" src = "img/Media/AjaxLoader.gif" alt = "等待圖" />';
	HtmlStr += '</div>';
	
	$('.Div_WaitAjax').html( HtmlStr );
	
	
				
				
			
	//// [ 等待中 ] 對話框與事件
	//// 
	//// 這裡是 JQuery UI 所提供的，寫法固定，把要顯示在對話框的內容先鑲在 HTML 中，並設定隱藏，並且撰寫以下片段，產生一個對話框物件。
	//// 然後再想觸發的按鈕事件（比方點新增按鈕會需要這個畫面來當對話框）中呼叫 Dialog( 'open' )就可以了
	////
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
			duration: 100
		},
		hide: 
		{
			effect: 'fade',
			duration: 100
		},
	})
}







//取得瀏覽器視窗高度
function getBrowserHeight() 
{
    if ($.browser.msie) 
	{
        return document.compatMode == "CSS1Compat" ? document.documentElement.clientHeight : document.body.clientHeight;
    } 
	else 
	{
        return self.innerHeight;
    }
}

//取得瀏覽器視窗寬度
function getBrowserWidth() 
{
    if ($.browser.msie) 
	{
        return document.compatMode == "CSS1Compat" ? document.documentElement.clientWidth : document.body.clientWidth;
    }
	else 
	{
        return self.innerWidth;
    }
}


//取得瀏覽器捲軸位置X
function getBrowserScrollX() 
{
    if ($.browser.msie) 
	{
        return document.body.parentNode.scrollLeft;
    } 
	else 
	{
        return window.scrollX;
    }
}

//取得瀏覽器捲軸位置Y
function getBrowserScrollY() 
{
    if ($.browser.msie) 
	{
        return document.body.parentNode.scrollTop;
    }
	else 
	{
        return window.scrollY;
    }
}



// 判斷瀏覽器是否支援 MP3 格式播放
function IsBrowserCanPlayMp3()
{
	var a = document.createElement('audio');
	IsBrowserCanPlayMp3Flag = !!(a.canPlayType && a.canPlayType('audio/mpeg;').replace(/no/, ''));
	return IsBrowserCanPlayMp3Flag;
}



// 判斷瀏覽器是否有裝 VLC
function IsVLCPluged()
{
	var P = {name:"VLC", status:-1, version:null, minVersion:"1,0,0,0"};
	var Det=PluginDetect;
	if (Det.getVersion)
	{
		P.version = Det.getVersion(P.name);            
	};
	if (Det.isIE)
	{     
		if(Det.ActiveXEnabled){
			if (Det.isMinVersion)
			{
				P.status = Det.isMinVersion(P.name, P.minVersion);
				if(P.status == 1 || P.status == 0)
				{
					IsVLCPlugedFlag = true;
					return true;
				}
			};
		}          
	}
	else
	{
		if (Det.isMinVersion)
		{
			P.status = Det.isMinVersion(P.name, P.minVersion);
			if(P.status == 1 || P.status == 0)
			{
				IsVLCPlugedFlag = true;
				return true;
			}
		};
	}
	
	IsVLCPlugedFlag = false;
	return false;
}
    



	
// 視窗變更大小就會觸發
$(window).resize(function()
{
	setTimeout( "reLocateLayout()", 500 );
});



function reLocateLayout()
{
	// 主畫面保持最小長寬 1024 X 700 , 如果發現視窗大小判斷有出錯，可改用 getBrowserWidth() 這個涵式來判斷
	if ( $(window).width() <= 1024 )
	{
		$('.Div_MainArea').css( "width", "1024px" );
	}
	else
	{
		$('.Div_MainArea').css( "width", "100%" );
	}

	
	if ( $(window).height() <= 700 )
	{
		$('.Div_MainArea').css( "height", "700px" );
	}
	else
	{
		$('.Div_MainArea').css( "height", "100%" );
	}

	// 播放機靠右對齊
	var NewValue = $('.Div_MainArea').width() - $('.Div_PlayerArea').width() - 50 ;		// 靠右保持50px
	$('.Div_PlayerArea').css( "left", NewValue );
	
	// 音樂清單 Toolbar 靠著分類按鈕 50px 距離
	NewValue =  $('.Div_SortAndSearch').css( 'left' ) + $('.Div_SortAndSearch').width() + 50;	// 與 Div_SortAndSearch 保持50px
	$('.Div_Grid_MediaToolBar').css( "left", NewValue );
		
	// 音樂清單 置中對齊鈕
	NewValue = $('.Div_MainArea').width() * 0.5 - $('.Div_AudioListArea').width() * 0.5;
	$('.Div_AudioListArea').css( "left", NewValue );
	
	// 音樂清單高度  70 是到底部的距離
	NewValue = $('.Div_MainArea').height() - $('.Div_Grid_TitleAndPlayer').height() - $('.Div_Grid_MediaToolBar').height() - $('.Div_Grid_MediaGridTitle').height() - 80 ; 
	$('.Div_AudioListArea').css( "height", NewValue );
	

	// 若，視窗拉小，則主畫面依舊維持700高度，視窗高度則會比700還小
	// 若，視窗高度超過700，則主畫面高度用100％來滿版，此時視窗高度跟主畫面高度相同
	// 若，視窗用瀏覽器的「放大 ( Ctrl 與 + )」，將畫面放大到很大很大，則主畫面高度會超過100%，並出現捲軸（因為內部元件並非用％，而是指定pixel來決定寬、高），此時主畫面高度會大於視窗高度
	// 總和上述三點，要讓播放中清單固定在下方，則要用較小的 getBrowserHeight()
	//var Smaller = $('.Div_MainArea').height() < getBrowserHeight() ? $('.Div_MainArea').height() : getBrowserHeight();
	
	var QueueTop = getBrowserHeight() - $('.Div_NowPlayingQueue').height() + getBrowserScrollY() - 3;
	$('.Div_NowPlayingQueue').css( 'top', QueueTop );
	
	// 由於 getBrowserScrollY() 會有誤差或BUG，所以必須再定位之後再做一次判斷，確認是否播放中清單的 Top + Height 不超過主畫面高度，超過就要拉高回來。
	if ( $('.Div_MainArea').height() < ( QueueTop + $('.Div_NowPlayingQueue').height() ) )
	{
		$('.Div_NowPlayingQueue').css( 'top', $('.Div_MainArea').height() - $('.Div_NowPlayingQueue').height() );
	}
	
	// 播放中清單靠右方 100
	Smaller = $('.Div_MainArea').width() < getBrowserWidth() ? $('.Div_MainArea').width() : getBrowserWidth();
	var QueueLeft = Smaller + getBrowserScrollX() - $('.Div_NowPlayingQueue').width() - 100;
	$('.Div_NowPlayingQueue').css( 'left', QueueLeft );
}



// 播放進度條
function PlayerProgressControl( Val, Max )
{	
	// 根據百分比，畫出圖
	Location = Val / Max * 540; 
	$('.Img_PlayerProgressBar_Dark').width( Location );
	
	// 確認是處於Drag狀態
	if ( false == IsDragProgress )
	{
		$('.Lbl_PlayerProgress_Tick').css( 'left', Location );
	}
	
	// 顯示播放時間與總時間
	if ( !isNaN(Max) )
	{	
		if ( IsPlayByVlcOrHtml5_HTML5 == IsPlayByVlcOrHtml5 )
		{
			// 將秒數轉成時間格式
			var MaxTime = new Date( Max*1000 );
			var ValTime = new Date( Val*1000 );
		}
		else
		{
			var MaxTime = new Date( Max );
			var ValTime = new Date( Val );
		}
		
		// 取出分鐘、秒鐘（個位數會補零）
		var MaxTime_Min = MaxTime.getMinutes(); if ( 10 > MaxTime_Min ) { MaxTime_Min = "0" + MaxTime_Min };
		var MaxTime_Sec = MaxTime.getSeconds(); if ( 10 > MaxTime_Sec ) { MaxTime_Sec = "0" + MaxTime_Sec };
		var ValTime_Min = ValTime.getMinutes(); if ( 10 > ValTime_Min ) { ValTime_Min = "0" + ValTime_Min };
		var ValTime_Sec = ValTime.getSeconds(); if ( 10 > ValTime_Sec ) { ValTime_Sec = "0" + ValTime_Sec };
				
		var TimeNow = ValTime_Min + ":" + ValTime_Sec;
		var TimeTotal = " / " + MaxTime_Min + ":" + MaxTime_Sec;
		
		$('.Lbl_Playing_TimeNow').text( TimeNow );
		$('.Lbl_Playing_TimeTotal').text( TimeTotal );
	}
}




// 播放器圖形改為 Pause
function ChangePlayIcon_AsPause()
{
	$( '.PC_Play' ).attr( 'src', 'img/Media/Player04_Play_Stop.png' );
}

// 播放器圖形改為 Play
function ChangePlayIcon_AsPlay()
{
	$( '.PC_Play' ).attr( 'src', 'img/Media/Player04_Play_Start.png' );
}



////
////	HTML5 Audio 控制
////    
function Html5_Audio_Pause()
{
	$('#Main_AudioPlayer').get(0).pause();
}

  
function Html5_Audio_Play()
{
	$('#Main_AudioPlayer').get(0).play();
}

   
function Html5_Volume_Mute()
{
	var IsMute = $('#Main_AudioPlayer').get(0).muted;
	
	if( true == IsMute )
	{
		// 若原本是靜音則要改為非靜音
		$('#Main_AudioPlayer').prop( 'muted', false );
		$( '.PC_SoundMute' ).attr( 'src', 'img/Media/Player06_Sound_On.png' );
	}
	else
	{
		// 若原本是非靜音則要改為靜音
		$('#Main_AudioPlayer').prop( 'muted', true );
		$( '.PC_SoundMute' ).attr( 'src', 'img/Media/Player06_Sound_Off.png' );
	}
}


function Html5_Volume_Higher()
{
	var NowVolume = $('#Main_AudioPlayer').get(0).volume;
	
	if( 1 > NowVolume )
	{
		NowVolume += 0.1;
	}
	else
	{
		NowVolume = 1;
	}
	
	$('#Main_AudioPlayer').get(0).volume = NowVolume;
	$( '.PC_SoundLower' ).attr( 'title', '音量：' + Math.round( NowVolume*100 ) + "%" );
	$( '.PC_SoundHigher' ).attr( 'title', '音量：' + Math.round( NowVolume*100 ) + "%" );
}

  
function Html5_Volume_Lower()
{
	var NowVolume = $('#Main_AudioPlayer').get(0).volume;
	
	if( 0.1 <= NowVolume )
	{
		NowVolume -= 0.1;
	}
	else
	{
		NowVolume = 0;
	}
	
	$('#Main_AudioPlayer').get(0).volume = NowVolume;
	$( '.PC_SoundLower' ).attr( 'title', '音量：' + Math.round( NowVolume*100 ) + "%" );
	$( '.PC_SoundHigher' ).attr( 'title', '音量：' + Math.round( NowVolume*100 ) + "%" );
}



// 取得播放數值，畫進度條
function Html5_GetProgressValue()
{
	// 取出的時間是秒數
	var Max = Math.floor( $('#Main_AudioPlayer').get(0).duration );
	var Val = Math.floor( $('#Main_AudioPlayer').get(0).currentTime );
	PlayerProgressControl( Val, Max );
}


// 在進度條按下，會跳到那邊播放
function Html5_Move_Progress(e)
{
	var Max = $('.Img_PlayerProgressBar').width();
	var Val = e.offsetX;
	
	// 從現在位置播放
	$('#Main_AudioPlayer').get(0).currentTime = ( Val / Max ) * $('#Main_AudioPlayer').get(0).duration;
}


// 拖進度條的時候，MouseUp 要處理的事情
function HTML5_Drag_Progress( LocationX )
{
	if ( true == IsDragProgress )
	{
		IsDragProgress = false;
		var NowLeft = LocationX - $( '.Div_PlayerArea' ).position().left;
		var NowTime = 0;
		
		if ( 0 > NowLeft ) 
		{
			NowLeft = 0;
			NowTime = 0;
		}
		else if ( 540 < NowLeft )
		{
			NowLeft = 540;
			NowTime = $('#Main_AudioPlayer').get(0).duration;
		}
		else
		{
			NowTime = ( LocationX - 540 / 540 ) * $('#Main_AudioPlayer').get(0).duration;
		}
		$('#Main_AudioPlayer').get(0).currentTime = ( NowLeft / 540 ) * $('#Main_AudioPlayer').get(0).duration;
	}
}




    

////
////	VLC Audio 控制
//// 
function VLC_Audio_Pause()
{
	VLC_Player.playlist.pause();
}

  
function VLC_Audio_Play()
{
	VLC_Player.playlist.play();
}

  
function VLC_Volume_Mute()
{
	var IsMute = VLC_Player.audio.mute;
	
	if( true == IsMute )
	{
		// 若原本是靜音則要改為非靜音
		VLC_Player.audio.mute = false;
		$( '.PC_SoundMute' ).attr( 'src', 'img/Media/Player06_Sound_On.png' );
	}
	else
	{
		// 若原本是非靜音則要改為靜音
		VLC_Player.audio.mute = true;
		$( '.PC_SoundMute' ).attr( 'src', 'img/Media/Player06_Sound_Off.png' );
	}
}

  
function VLC_Volume_Higher()
{
	var NowVolume = VLC_Player.audio.volume;
	
	if( 100 > NowVolume )
	{
		NowVolume += 10;
	}
	else
	{
		NowVolume = 100;
	}
	
	VLC_Player.audio.volume = NowVolume;
	$( '.PC_SoundLower' ).attr( 'title', '音量：' + Math.round( NowVolume ) + "%" );
	$( '.PC_SoundHigher' ).attr( 'title', '音量：' + Math.round( NowVolume ) + "%" );
}

  
function VLC_Volume_Lower()
{
	var NowVolume = VLC_Player.audio.volume;
	
	if( 0 < NowVolume )
	{
		NowVolume -= 10;
	}
	else
	{
		NowVolume = 0;
	}
	
	VLC_Player.audio.volume = NowVolume;
	$( '.PC_SoundLower' ).attr( 'title', '音量：' + Math.round( NowVolume ) + "%" );
	$( '.PC_SoundHigher' ).attr( 'title', '音量：' + Math.round( NowVolume ) + "%" );
}


// 取得播放數值
function VLC_GetProgressValue()
{
	if( typeof( VLC_Player.input ) !== 'undefined' )
	{
		if( VLC_State_PLAYING == VLC_Player.input.state )
		{
			var Max = VLC_Player.input.length;
			var Val = VLC_Player.input.time; 
			PlayerProgressControl( Val, Max );
	
			var Percent = ( Val / Max );
			
			if ( 0.99 <= Percent )
			{
				VLC_Player.playlist.stop();
				PlayTheQueue_Next();
			}
		}
	}
}


// 在進度條按下，會跳到那邊播放
function VLC_Move_Progress(e)
{
	var Max = $('.Img_PlayerProgressBar').width();
	var Val = e.pageX - $('.Div_PlayerArea').position().left;
	
	// 從現在位置播放
	VLC_Player.input.time = ( Val / Max ) * VLC_Player.input.length;
}


// 拖進度條的時候，MouseUp 要處理的事情
function VLC_Drag_Progress( LocationX )
{
	if ( true == IsDragProgress )
	{
		IsDragProgress = false;
		var NowLeft = LocationX - $( '.Div_PlayerArea' ).position().left;
		var NowTime = 0;
		var VLC_MaxTime = VLC_Player.input.length;
		var VLC_NowTime = VLC_Player.input.time;
		
		if ( 0 > NowLeft ) 
		{
			NowLeft = 0;
			NowTime = 0;
		}
		else if ( 540 < NowLeft )
		{
			NowLeft = 540;
			NowTime = VLC_MaxTime;
		}
		else
		{
			NowTime = ( ( LocationX - 540 ) / 540 ) * VLC_MaxTime;
		}
		VLC_Player.input.time = ( NowLeft / 540 ) * VLC_MaxTime;
	}
}