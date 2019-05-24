//window.onload = init;
var interval;
var session_path;
function init()
{
    interval = setInterval(trackLogin,5000);
}
function trackLogin()
{
    var xmlReq = false;
    try {
        xmlReq = new ActiveXObject("Msxml2.XMLHTTP");
    } 
    catch (e) {
        try {
            xmlReq = new ActiveXObject("Microsoft.XMLHTTP");
        } 
        catch (e2) {
            xmlReq = false;
        }
    }
    if (!xmlReq && typeof XMLHttpRequest != 'undefined') {
        xmlReq = new XMLHttpRequest();
    }
    xmlReq.open('get', 'checktime.php?session='+session_path, true);
    xmlReq.setRequestHeader("Connection", "close");
    xmlReq.send(null);
    xmlReq.onreadystatechange = function(){
        if(xmlReq.readyState == 4 && xmlReq.status==200) {
            switch(xmlReq.responseText)
            {
                case "1":
                    clearInterval(interval);
                    document.location.href = "timeout.php?session="+session_path;
                    break;
                case "2":
                    clearInterval(interval);
                    document.location.href = "logout.php?session="+session_path;
                    break;
                case "3":
                    alert("Please notify Administrator to start AcroCloud.");
                    clearInterval(interval);
                    document.location.href = "logout.php?session="+session_path;                            
                    break;
            }
        }
    }
}
