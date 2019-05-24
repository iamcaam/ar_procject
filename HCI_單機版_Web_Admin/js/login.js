/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var LoginInfo = function(_Username, _Password,_Rmbpwd){
    this.Username = _Username;
    this.Password = _Password;
    this.Rmbpwd = _Rmbpwd;
};
function RememberUsernamePassword()
{
    //alert(logininfo.Username + " " + logininfo.Password + " " + logininfo.Rmbpwd);
    if(typeof(Storage)!=="undefined"){
        localStorage.ARUname = logininfo.Username;
        if(logininfo.Rmbpwd == "1"){
            localStorage.ARPwd = logininfo.Password;
            localStorage.ARRMPwd = "1";
        }
        else{
            localStorage.ARRMPwd = "0";
        }              
    }
    else{
        if(navigator.cookieEnabled){          
            $.cookie('ARUname', logininfo.Username, {expires: 7});
            if(logininfo.Rmbpwd == "1"){
                $.cookie('ARPwd', logininfo.Password, {expires: 7});
                $.cookie('ARRMPwd', '1', {expires: 7});
            }
            else{
                $.cookie('ARRMPwd', '0', {expires: 7});
            }                  
        }                                     
    }          
}

