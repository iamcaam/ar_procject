var modalWindow = {
	parent:"body",
	windowId:null,
	content:null,
	width:null,
	height:null,
	close:function()
	{
		$(".modal-window").remove();
		$(".modal-overlay").remove();
		//window.top.location.href = window.top.location.href;
                switch(uploadact){       
                    case 1:
                        RootGetFolderList(cid,'1');
                        break;
                    case 2:
                        GetShareForContent(shareforinfo.idR,shareforinfo.idO,shareforinfo.idU,shareforinfo.sharecase,shareforinfo.securitycode)                        
                        break;
                }
	},
	closenoreload:function()
	{
//            HeartBeatPostData = {
//                User: null                
//            };
//            HeartBeatPostData.User = session;
//            var request = CallAjax("PostSetActionTime.php", HeartBeatPostData, "text");         
	    $(".modal-window").remove();
	    $(".modal-overlay").remove();
            
	},
        closereget:function()
	{       
	    $(".modal-window").remove();
	    $(".modal-overlay").remove();
            parseUrlParamReGet();
	},
	open:function(source)
	{
		var modal = "";
		modal += "<div class=\"modal-overlay\"></div>";
		modal += "<div id=\"" + this.windowId + "\" class=\"modal-window\" style=\"width:" + this.width + "px; height:" + this.height + "px; margin-top:-" + (this.height / 2) + "px; margin-left:-" + (this.width / 2) + "px;\">";
		modal += "<iframe id=\"modalframe \" width=\"" + this.width +"\" height=\""+ this.height +"\"' frameborder='0' scrolling='no' allowtransparency='1' src='" + source + "'>&lt/iframe>";
		modal += "</div>";	

		$(this.parent).append(modal);

		$(".modal-window").append("<a class=\"close-window\"></a>");
		$(".close-window").click(function(){modalWindow.close();});
//		$(".modal-overlay").click(function(){modalWindow.close();});
	},
        openreget:function(source)
	{
		var modal = "";
		modal += "<div class=\"modal-overlay\"></div>";
		modal += "<div id=\"" + this.windowId + "\" class=\"modal-window\" style=\"width:" + this.width + "px; height:" + this.height + "px; margin-top:-" + (this.height / 2) + "px; margin-left:-" + (this.width / 2) + "px;\">";
		modal += "<iframe id=\"modalframe \" width=\"" + this.width +"\" height=\""+ this.height +"\"' frameborder='0' scrolling='no' allowtransparency='1' src='" + source + "'>&lt/iframe>";
		modal += "</div>";	

		$(this.parent).append(modal);

		$(".modal-window").append("<a class=\"close-window\"></a>");
		$(".close-window").click(function(){modalWindow.closereget();});
	},
	opennoreload: function (source) {
	    var modal = "";
	    modal += "<div class=\"modal-overlay\"></div>";
	    modal += "<div id=\"" + this.windowId + "\" class=\"modal-window\" style=\"width:" + this.width + "px; height:" + this.height + "px; margin-top:-" + (this.height / 2) + "px; margin-left:-" + (this.width / 2) + "px;\">";
	    modal += "<iframe id=\"modalframe \" width=\"" + this.width + "\" height=\"" + this.height + "\"' frameborder='0' scrolling='no' allowtransparency='1' src='" + source + "'>&lt/iframe>";
	    modal += "</div>";

	    $(this.parent).append(modal);

	    $(".modal-window").append("<a class=\"close-window\"></a>");
	    $(".close-window").click(function () {modalWindow.closenoreload();});
	    //		$(".modal-overlay").click(function(){modalWindow.close();});
	},
        openwaitnoreload: function () {
	    var modal = "";
	    modal += "<div class=\"modal-overlay\"></div>";
	    modal += "<div id=\"" + this.windowId + "\" class=\"modal-window\" style=\"width:" + this.width + "px; height:" + this.height + "px; margin-top:-" + (this.height / 2) + "px; margin-left:-" + (this.width / 2) + "px;\">";	    
	    modal += "</div>";

	    $(this.parent).append(modal);	   
	},
    /* Foxy 2012.11.20 為了做全畫面的對話框.所以新增了一個openMax()方法.不用傳入指定的視窗大小.一律用全視窗(瀏覽器)大小 
                     而之所以要用全畫面對話框.而不改用 window.location.assign() 轉網頁的方式.是因為[搬移]畫面有一個 Class 
                     如果要用轉網頁的方式.會需要把這個 Class 也傳過去 moveto.php 這個目前太困難了.目前只會傳字串.所以只好
                     繼續用 Open Dialog 的方式來開啟頁面做功能處理*/
    openMax:function(source)
    {
        var modal = "";
        modal += "<div class=\"modal-overlay\"></div>";
        modal += "<div id=\"" + this.windowId + "\" class=\"modal-Maxwindow\" style=\"width:" + getBrowserWidth() + "px; height:" + getBrowserHeight() + "px;\" >";
        modal += "<iframe id=\"modalframe \" width=\"" + getBrowserWidth() + "\" height=\"" + getBrowserHeight() + "\" frameborder=\"0\" scrolling=\"no\" allowtransparency=\"1\" src=\"" + source + "\"></iframe>";
        modal += "</div>";	

        $(this.parent).append(modal);
    },

    /* Foxy 2012.11.20 End*/
    closeMax:function()
    {
        $(".modal-Maxwindow").remove();
        $(".modal-overlay").remove();
        window.top.location.href = window.top.location.href;
    }
};

function openModal(source,width,height)
{
    modalWindow.windowId = "myModal";
    modalWindow.width = width;
    modalWindow.height = height;
    modalWindow.open(source);
}

function openModalNoReload(source,width,height)
{
    modalWindow.windowId = "myModal";
    modalWindow.width = width;
    modalWindow.height = height;
    modalWindow.opennoreload(source);
}

function openModalReget(source,width,height)
{
    modalWindow.windowId = "myModal";
    modalWindow.width = width;
    modalWindow.height = height;
    modalWindow.openreget(source);
}

function openModalWaitNoReload(source,width,height)
{
    modalWindow.windowId = "myModal";
    modalWindow.width = width;
    modalWindow.height = height;
    modalWindow.openwaitnoreload();
}

function ExitClick()
{
    modalWindow.close();
}

function ExitNoReloadClick()
{
    modalWindow.closenoreload();
}

function ExitGetShareFrom(index)
{
    ShareFrom(index);
    modalWindow.closenoreload();    
}

function ExitRefreshFolderContent(fid)
{     
   RootGetFolderList(fid,'1');
   modalWindow.closenoreload(); 
}

function ExitRefreshShareFolderContent(idR,idO,idU,sharecase,code)
{
    GetShareForContent(idR,idO,idU,sharecase,code)
    modalWindow.closenoreload();    
}

function ExitRenameClick(fid,newname) 
{      
    renameobj.text(newname);
    modalWindow.closenoreload();
    //$(".modal-window").remove();
    //$(".modal-overlay").remove();    
    //$('#' + fid +'.filename').text(newname);     
}

//取得瀏覽器視窗高度
function getBrowserHeight() {
    if ($.browser.msie) {
        return document.compatMode == "CSS1Compat" ? document.documentElement.clientHeight :
                 document.body.clientHeight;
    } else {
        return self.innerHeight;
    }
}

//取得瀏覽器視窗寬度
function getBrowserWidth() {
    if ($.browser.msie) {
        return document.compatMode == "CSS1Compat" ? document.documentElement.clientWidth :
                 document.body.clientWidth;
    } else {
        return self.innerWidth;
    }
} 

