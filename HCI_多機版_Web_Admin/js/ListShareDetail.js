

function RegEvt_RemoveShareMember_Click()
{
	// Remove 按鈕事件
	$('.Img_ShareListRemove').click( function ()
	{
		// 判斷要移除的是 Group 或 User
		if ( "Group" == $(this).parent().children('.Img_ShareListPeople').attr( "alt" ) )
		{
			var UserGroupIDs = [];
			UserGroupIDs[ 0 ] = $(this).attr('GroupID');
			var ShareCase = $(this).attr('ShareCase');
			
			$(this).addClass('SelectMe');
			
			Ajax_RemoveGroupfromShare( AccessKey, ObjFileOrDirId, ShareCase, UserGroupIDs );
		}
		else
		{
			var UserIDs = [];
			UserIDs[ 0 ] = $(this).attr('AccountID');
			var ShareCase = $(this).attr('ShareCase');
			
			$(this).addClass('SelectMe');
			
			Ajax_RemoveUserfromShare( AccessKey, ObjFileOrDirId, ShareCase, UserIDs );
		}
	} );
}