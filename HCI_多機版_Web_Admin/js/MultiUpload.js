/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function CallAjax(url, data, datatype){   
  var request = $.ajax({ 
  type: "POST", 
  url: url,
  data:data,
  dataType: datatype,
  async : false
  });    
  return request;
}

function CancelUpload(sid,cid, filename){
    var inputtext = "sid=" + sid + "&cid=" + cid  + "&filename=" + encodeURIComponent(filename.replace(/'/g, "%27"));
    //var inputtext = "inputText=/AcroRed/ui/mgtap 9999999 0 list_user";
    var mountresult = CallAjax("upload_cancel.php", inputtext, "text");
    //var mountresult = CallAjax("upperCase.php", inputtext, "text");
    mountresult.done(function(msg,statustext,jqxhr) { 
                    //alert(jqxhr.responseText);
                    return jqxhr.responseText;
                    });
    mountresult.fail(function(jqXHR, textStatus) {
                    //alert('-1');
                    return "-1";
    });
}
