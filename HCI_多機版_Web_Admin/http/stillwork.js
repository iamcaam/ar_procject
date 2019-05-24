//window.onload = init;
var interval;
var session_path;
function init()
{
	interval = setInterval(trackLogin,60000);
}
function trackLogin()
{
	var xmlReq = false;
	try {
	xmlReq = new ActiveXObject("Msxml2.XMLHTTP");
	} catch (e) {
	try {
	xmlReq = new ActiveXObject("Microsoft.XMLHTTP");
	} catch (e2) {
	xmlReq = false;
	}
	}
	if (!xmlReq && typeof XMLHttpRequest != 'undefined') {
		xmlReq = new XMLHttpRequest();
	}
	xmlReq.open('get', 'stillonuse.php?session='+session_path, true);
	xmlReq.setRequestHeader("Connection", "close");
	xmlReq.send(null);
/*	alert('trackLogin:'+session_path);
	xmlReq.onreadystatechange = function(){
		if(xmlReq.readyState == 4 && xmlReq.status==200) {
			if(xmlReq.responseText == 1)
			{
				clearInterval(interval);
				document.location.href = "timeout.php?session="+session_path;
			}
			if(xmlReq.responseText == 2)
			{
				clearInterval(interval);
				document.location.href = "logout.php?session="+session_path;
			}
		}

	}
*/
}
