// JavaScript Document

// 註冊 Update FW 頁面的事件
var UpdateFlag = false;
var UIProgressUploadFinish = false; 
function finishUpload(result)
{   
    CleanUpdateFlag(); 
    if(result == 0){       
        Ajax_GetNewVer();
    }
    else{

    }    
}

function RegUpdateFw()
{
    $( "#progressbar" ).progressbar({value: 0});    
    $('#Form_UploadUpdateFile').submit( function(e) 
    {               
        if($('#FwUploadSelector').val() == '')
        {
            alert( LanguageStr_SysCfgUpdateFW.WarningSelectFW );
            return false;
        }
        if(!UpdateFlag)
        {
            UpdateFlag = true;
            UIProgressUploadFinish = false; 
            $( ".progress-label" ).text( "0%" );
            var t = setTimeout("checkProgress()", 2000);
            // ChangeUploadMsg( LanguageStr_SysCfgUpdateFW.UploadFW,"red");                
            return true;
        }
        else
        {
            alert( LanguageStr_SysCfgUpdateFW.WarningUpdateFW);
            return false;
        }
    }); 
}

function CleanUpdateFlag()
{
    UpdateFlag = false;
    InitialUpdatePage();
}

function InitialUpdatePage()
{
//    $('#FwUploadSelector').val('');
    $( ".progress-label" ).text( "" );
    $( "#progressbar" ).progressbar( "option", "value", 0 );
    // ChangeUploadMsg('','black');
}

function checkProgress()
{   
    if(window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
    }
    else
    {// code for IE6, IE5
    xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function()
    {
        if(xmlhttp.readyState==4 && xmlhttp.status==200)
        {
            //call the function to update the progress bar visually   
            updateProgressBar(eval("("+xmlhttp.responseText+")")); //don't actually do this - it's unsafe
        }
    }
    //send the request to upload.php with the progress parameter present
    xmlhttp.open("GET", "upload_updatefile.php?progress=" + sess_val, true);
    xmlhttp.send();
}        

function updateProgressBar(response)
{
    if(response['bytes_processed']==undefined) //this upload is finished
    {
        $( "#progressbar" ).progressbar( "option", "value", 100 );
        $( ".progress-label" ).text( "100%" );       
        UIProgressUploadFinish = true;
        //we do not make another request for the progress since it's done
    }
    else
    {
        //calculate the new pixel width of the progress bar
        var new_width = Math.round((response['bytes_processed'] /response['content_length']) * 100 );
        //    alert(new_width);
        $( "#progressbar" ).progressbar( "option", "value", new_width );
        $( ".progress-label" ).text( new_width + "%" );
        checkProgress(); //make another request for the progress
    }
}

function Ajax_GetNewVer()
{                               
    var request = oRelayAjax.listuploadnewversion();
    oHtmlCreate.blockPage(); 
    request.done( function( AjaxData, statustext, jqXHR ) {  
        if(typeof(AjaxData['status']) !== "undefined" && AjaxData['status'] == 2){
            Logout_Another_Admin_Login();
            return false;
        }   
        else if(typeof(AjaxData['status']) !== "undefined" && AjaxData['status'] == 3){
            window.location.href = 'index.php';
            return false;
        }
        oHtmlCreate.stopPage();                   
        Async_Ajax_GetNewVer_Finish(AjaxData);
    });
            
    request.fail( function( jqXHR, textStatus ){
        CleanUpdateFlag();
        oHtmlCreate.stopPage();                  
            // OpenDialog_Message( LanguageStr_SysCfgUpdateFW.Ajax01 );
    });
}

function Async_Ajax_GetNewVer_Finish(JsonData)
{      
    // alert(JsonData['NewVersion']);
    // OpenDialog_UpdateFwYesNo( $('.txt_now_firmware').text(), JsonData['NewVersion'] );
    confirmable = confirm(LanguageStr_SysCfgUpdateFW.OldVer_Title +  $('.txt_now_firmware:first').text() + '\n\n' + LanguageStr_SysCfgUpdateFW.NewVer_Title + JsonData['NewVersion'] +'\n\n'+LanguageStr_SysCfgUpdateFW.Update_Before_Warning);
    if(confirmable){
        oUIUpgrade.LocalUpdate();
    }
    else
        CleanUpdateFlag();
}