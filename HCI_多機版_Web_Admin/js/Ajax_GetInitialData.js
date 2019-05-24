// 程式一開始執行一次取得「分享」跟「期間」，之後不用再每次換頁就取資料，但，每次有執行更新「分享」或「期間」的指令成功了，就要重新呼叫此函式
// 取得目前有什麼 ShareCase 跟 分享期間
function Ajax_GetShareCaseAndPeriod(id,accesskey)
{
    var jsonrequest = 
	'{"id":' + id + 
	',"accesskey":"' + accesskey + 
	'"}';
	 
	if ( true == ShowAjaxDebug )
	{
    	alert( "Ajax_GetShareCaseAndPeriod(Send):" +  jsonrequest);
	}
	
    var request = this.CallAjax("ShareCmdProcessPost.php?act=ListCase", jsonrequest , "json");
	
    request.done(function (msg, statustext, jqxhr) 
	{
		if ( true == ShowAjaxDebug )
		{
        	alert( "Ajax_GetShareCaseAndPeriod(Rcv):" + jqxhr.responseText);
		}
		
        $.each( msg, function( index, element ) 
        {            
            switch(index)
            {
                case 'result':  
                    //    -1 ~ -99 parameter error
                    //    -101,-102 access key error 
                    //    -200 ~ -300 db error
                    if(element == 0)
					{
                    }
                    else if(element < 0 && element > -99)
					{
                        alert(LanguageStr_ShareMain.AjaxMsg00);
                    }   
                    else if(element <= -100 && element > -200)
					{
                        alert(LanguageStr_ShareMain.AjaxMsg01);
                    }        
                    else if(element <= -200 && element > -300)
					{
                        alert(LanguageStr_ShareMain.AjaxMsg02);
                    }
                    else
					{
                        alert(LanguageStr_ShareMain.AjaxMsg03 + element);
                    }
                    break;
					
                case 'Shares':
				
					// 每次取數值要先清除前次資料，否則當取數值回來沒有任何分享，則會變成用前一次資料
					Ajax_ShareCase = 0;
					Ajax_ShareLink = "";
					Ajax_SharePassword = "";
					Ajax_Description = "";
					Ajax_PeriodDate = PeriodDate_Special;
					Ajax_PeriodType = PeriodType_Special;
					
					$('.Btn_SharePeriod').fadeOut( 500 );
					
					// Shares 是一個 Node 內容會包含零個到多個 ShareCase 資訊組，透過 Each 把每一個資訊組解析
                    $.each(element,function(index, element)
                    {
						// 有抓到任何的 ShareCase 才可以讓「分享期間」按鈕出現
						$('.Btn_SharePeriod').fadeIn( 500 );
						
						Ajax_PeriodDate = element.lefttime;
						
						if ( "" == Ajax_PeriodDate )
						{
							Ajax_PeriodDate = PeriodDate_Special;
						}
						else
						{
							Ajax_PeriodType = PeriodType_Date;
						}
							
						if ( ShareCase_Public == element.sharecase )
						{
							Ajax_ShareCase = element.sharecase;
							Ajax_ShareLink = element.url;
							Ajax_SharePassword = element.password;
							Ajax_Description = element.description;
						}
                    }); 
                    break;
            }
        });
    });
    request.fail(function (jqxhr, textStatus)
	{
		alert(LanguageStr_ShareMain.AjaxMsg04);                
    });
}









