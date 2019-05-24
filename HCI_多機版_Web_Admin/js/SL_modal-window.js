var SL_modalWindow = {
	parent:"body",
	windowId:null,
	content:null,
	width:null,
	height:null,
	SL_close: function ()
	{
		$(".SL_modal-window").remove();
		$(".SL_modal-overlay").remove();
		window.top.location.href = window.top.location.href;
	},
	SL_open: function ()
	{
	    /* Foxy 2012/10/23 */
	    /* 修改 modal 文字串，拿掉margin，讓CSS的 Style內的 top 與 left 來控制初始位置在左上角，並串上 Title 的 Div 用來控制視窗移動*/
	    var modal = "";

	    /* Foxy 2012.10.31 */
	    /* 請注意在某些物件裡頭，加入了 OnMouseXxxx = "DivMousexxxx() 的事件關連，是因為 FireFox 不吃 Javascript 當中的 Mouse 事件，所以只好寫在 Html 中 */
		modal += "<div class=\"SL_Modal_Overlay\" onmouseup = \"DivMouseUp()\" onmousemove = \"DivMouseMove(event)\"></div>";      /* SL_Modal_Overlay 是灰色的背景層 */
		modal += "<div id=\"" + this.windowId + "\" class=\"SL_Modal_Window\" style=\"width:" + this.width + "px; height:" + this.height + "px; \">";
		modal += "<button class=\"DivDialogTitle\" onmouseup = \"DivMouseUp()\" onmousemove = \"DivMouseMove(event)\" onmousedown = \"DivMouseDown(event)\" >按著我可拖動視窗</button>";

	    /* Foxy 2012/10/26 */
	    /* 由於某些元件（ 可當容器的 ）在進行滑鼠點下然後移動（Drag）的事件過程，滑鼠會被設定成 text 屬性（就是 I 形狀），所以這邊在 Title 要把
           原本使用的 Div 改為 Button，（或Image），就可以無痛的：滑鼠在點下然後移動（Drag）的事件過程，滑鼠會自動設定move屬性


	    /* Foxy 2012/10/26 */
	    /* 由於 iframe 無法觸發控制 event ，所以當滑鼠移動到視窗內部，就無法捕捉 MouseMove ，所以把 iframe 的機制改掉（只針對 Upload ）*/
	    /*modal += "<iframe id=\"modalframe \" class = \"SL_ModalFrame\" width=\"" + this.width + "\" height=\"" + this.height + "\"' frameborder='0' scrolling='no' allowtransparency='1' src='" + source + "'>&lt/iframe>";*/


        /* 以 Object 來掛入 Silverlight ，可能因為要跟 Server 的 PHP 溝通，所以多包了一層 Form ，不確定可否拿掉 */	    
		modal += "<form id=\"form1\" runat=\"server\" style=\"height:100%\">";
		modal += "<div id=\"silverlightControlHost\" onmouseup = \"DivMouseUp()\" onmousemove = \"DivMouseMove(event)\">";
        
		modal += "<object data=\"data:application/x-silverlight-2,\" type=\"application/x-silverlight-2\" width=\"500\" height=\"330\">";       /* Foxy 2012.11.12 為了能讓使用者再低解析度與150%放大瀏覽下，視窗還能完全顯示出來，所以對話框要縮小 */
        
        /* 這些 param 是針對 Object 來設定，並非 Silverlight */
		modal += "<param name=\"source\" value=\"ARWADD.xap?201207311512\"/>";
		modal += "<param name=\"background\" value=\"#66ccff\" />";
		modal += "<param name=\"minRuntimeVersion\" value=\"5.0.61118.0\" />";
		modal += "<param name=\"autoUpgrade\" value=\"true\" />";

		modal += "<iframe id=\"modalframe \" width=\"" + this.width + "\" height=\"" + this.height + "\"' frameborder='0' scrolling='no' allowtransparency='1' src='moveto.php'>&lt/iframe>";
		
		modal += "<a href=\"http://go.microsoft.com/fwlink/?LinkID=149156&v=5.0.61118.0\" style=\"text-decoration:none\">";
		modal += "<img src=\"http://go.microsoft.com/fwlink/?LinkId=161376\" alt=\"Get Microsoft Silverlight\" style=\"border-style:none\"/>";
		modal += "</a>";
		modal += "</object> <iframe id=\"_sl_historyFrame\" style=\"visibility:hidden;height:0px;width:0px;border:0px\"></iframe>";
		modal += "</div>";
		modal += "</form>";

	    
		modal += "</div>";      /*<Div SL_Modal_Window >*/

		$(this.parent).append(modal);

		$(".SL_modal-window").append("<a class=\"close-window\"></a>");
		$(".SL_close-window").click(function () { SL_modalWindow.close(); });

	    /* Foxy 2012.10.31 */
	    // 底下的寫法，原本是沒有問題的，但是為了相容於 FireFox 所以改成寫在 Html 當中寫事件與涵式的關連，寫在上面那段裡 modal+=

	    /* Foxy 2012.10.23 */
        /* 只有在滑鼠在標題區按下才能開始移動視窗 */
		//$(".DivDialogTitle").mousedown(function (e) { DivMouseDown(event); });
        
        /* 滑鼠在任何地方放開，都要把 Flag 設定為 false ，讓 MouseMove 雖然繼續接收但不控制視窗 */
		//$(".SL_Modal_Overlay").mouseup(function () { DivMouseUp(); });
		//$("#silverlightControlHost").mouseup(function () { DivMouseUp(); });
		//$(".DivDialogTitle").mouseup(function () { DivMouseUp(); });

        /* 滑鼠在視窗內外任何地方都可以接收到滑鼠事件 */
		//$(".SL_Modal_Overlay").mousemove(function (e) { DivMouseMove(event); });
		//$("#silverlightControlHost").mousemove(function (e) { DivMouseMove(event); });
		//$(".DivDialogTitle").mousemove(function (e) { DivMouseMove(event); });

	    /* 為什麼不只在 Title Bar 抓取 MouseUp, MouseDown, MouseMove 就好呢？因為可能網頁的效能跟不上，當滑鼠移動很快，
           就會讓視窗移動跟不上滑鼠（這不太合理），所以只好在 Title Bar 以外的地方也抓取 MouseDown, MouseMove 來控制 */
	}
};
function SL_ExitClick()
{
    SL_modalWindow.SL_close();
}

function onSilverlightError(sender, args) {
    var appSource = "";
    if (sender != null && sender != 0) {
        appSource = sender.getHost().Source;
    }

    var errorType = args.ErrorType;
    var iErrorCode = args.ErrorCode;

    if (errorType == "ImageError" || errorType == "MediaError") {
        return;
    }

    var errMsg = "Unhandled Error in Silverlight Application " + appSource + "\n";

    errMsg += "Code: " + iErrorCode + "    \n";
    errMsg += "Category: " + errorType + "       \n";
    errMsg += "Message: " + args.ErrorMessage + "     \n";

    if (errorType == "ParserError") {
        errMsg += "File: " + args.xamlFile + "     \n";
        errMsg += "Line: " + args.lineNumber + "     \n";
        errMsg += "Position: " + args.charPosition + "     \n";
    }
    else if (errorType == "RuntimeError") {
        if (args.lineNumber != 0) {
            errMsg += "Line: " + args.lineNumber + "     \n";
            errMsg += "Position: " + args.charPosition + "     \n";
        }
        errMsg += "MethodName: " + args.methodName + "     \n";
    }

    throw new Error(errMsg);
}

var DivSourceX = 0;
var DivSourceY = 0;
var DivEnvDwX = 0;
var DivEnvDwY = 0;
var DivEnvMvX = 0;
var DivEnvMvY = 0;
var DivMouseFlag = false;    // 用來判斷是否處於滑鼠按著的狀態


function DivMouseDown(EvtDw)
{
    // Mouse Down 的時候，紀錄當時視窗的原始位置，滑鼠當時的位置
    DivSourceX = $("#myModal").position().left;
    DivSourceY = $("#myModal").position().top;

    /* 2012.10.31 */
    /* 為了相容性，必須先判斷EvtMv是否有存在，如果不存在，則改用 Window.Enevt */
    /* 而且，IE 與 Chrome 可直接取用事件的 x, y ，但是 FireFox 卻只吃 pageX, pageY */
    if (!EvtDw)
    {
        var WinEvt = window.event;

        if (WinEvt.pageX || WinEvt.pageY)
        {
            DivEnvDwX = WinEvt.pageX;
            DivEnvDwY = WinEvt.pageY;
        }
        else if (WinEvt.clientX || WinEvt.clientY)
        {
            DivEnvDwX = WinEvt.clientX;
            DivEnvDwY = WinEvt.clientY;
        }
        else if (WinEvt.x || WinEvt.y)
        {
            DivEnvDwX = WinEvt.x;
            DivEnvDwY = WinEvt.y;
        }

    }
    else
    {
        if (EvtDw.pageX || EvtDw.pageY)
        {
            DivEnvDwX = EvtDw.pageX;
            DivEnvDwY = EvtDw.pageY;
        }
        else if (EvtDw.clientX || EvtDw.clientY)
        {
            DivEnvDwX = EvtDw.clientX;
            DivEnvDwY = EvtDw.clientY;
        }
        else if (evn.x || evn.y)
        {
            DivEnvDwX = EvtDw.x;
            DivEnvDwY = EvtDw.y;
        }
    }

    DivMouseFlag = true;
}

function DivMouseUp(EvtUp)
{
    DivMouseFlag = false;
}

function DivMouseMove(EvtMv)
{
    if (DivMouseFlag)
    {
        /* Mouse Down 的時候，紀錄當時視窗的原始位置，滑鼠移動時，再度取得滑鼠位置，
        兩個位置相減 + 原始位置 （Mouse Down時視窗的位置）就可將視窗移動到現在位置*/

        /* 2012.10.31 */
        /* 為了相容性，必須先判斷EvtMv是否有存在，如果不存在，則改用 Window.Enevt */
        /* 而且，IE 與 Chrome 可直接取用事件的 x, y ，但是 FireFox 卻只吃 pageX, pageY */
        if (!EvtMv)
        {
            var WinEvt = window.event;

            if (WinEvt.pageX || WinEvt.pageY)
            {
                DivEnvMvX = WinEvt.pageX;
                DivEnvMvY = WinEvt.pageY;
            }
            else if (WinEvt.clientX || WinEvt.clientY)
            {
                DivEnvMvX = WinEvt.clientX;
                DivEnvMvY = WinEvt.clientY;
            }
            else if (EvtMv.x || EvtMv.y)
            {
                DivEnvMvX = WinEvt.x;
                DivEnvMvY = WinEvt.y;
            }
        }

        else
        {
            if (EvtMv.pageX || EvtMv.pageY)
            {
                DivEnvMvX = EvtMv.pageX;
                DivEnvMvY = EvtMv.pageY;
            }
            else if (EvtMv.clientX || EvtMv.clientY)
            {
                DivEnvMvX = EvtMv.clientX;
                DivEnvMvY = EvtMv.clientY;
            }
            else if (EvtMv.x || EvtMv.y)
            {
                DivEnvMvX = EvtMv.x;
                DivEnvMvY = EvtMv.y;
            }
        }

        NewDivX = DivSourceX + (DivEnvMvX - DivEnvDwX);
        NewDivY = DivSourceY + (DivEnvMvY - DivEnvDwY);

        if (NewDivX < 5)
        {
            NewDivX = 5;
        }

        if (NewDivY < 5)
        {
            NewDivY = 5;
            DivMouseFlag = false;
        }


        $("#myModal").css(
		{
		    position: "absolute",
		    top: NewDivY + "px",
		    left: NewDivX + "px"
		});

    }
}