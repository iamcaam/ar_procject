<?php
// ini_set('display_errors', 'On');
include_once("/var/www/html/libraries/Language.php");
include_once ("/var/www/html/libraries/BaseAPI.php");
include_once ("/var/www/html/libraries/HandleLogin.php");
include_once("/var/www/html/libraries/Language.php");
include_once("/var/www/html/libraries/HandleLog.php");
include_once("/var/www/html/libraries/HandleDES.php");
// 新式樣修改(藍黃) 2016.12.13 william
$lang = new Language(true);
$lang->load("langIndex");
$version = "v1.0.0.42";
$oLogin = new Login();
// $oLogin->Sec_Session_Start();
// if($oLogin->Login_Check() == APIStatus::LoginSuccess)
// {
//     header("Location: Admin_Main.php");
//     exit();      
// }   
$VMorVDI = "VDI";
$oLogin->getUIConfig($outputUIConfig);  
$Header = $lang->line("langIndex.Str_Header");//"VDI&nbsp;&nbsp;Manager"; // 新式樣修改(藍黃) 2016.12.13 william
if($outputUIConfig['Seed'] == 0){
    $Header = $lang->line("langIndex.Str_Header_VM");
    $VMorVDI = "VM";
}

$output_expire = shell_exec('sudo /var/www/html/script/monitor_time.sh show');
$arr_output_expire = json_decode($output_expire,true);
$show_expire = false;
if($arr_output_expire['Flag'] == 1)
{
    $show_expire = true;
    $expire_time = (int)ceil($arr_output_expire['Time']/(24*60*60));    
}
$password = '';
if($_COOKIE['ARChkrm'] == 1){   
    // var_dump(1);    
    $sUserBrowser = $_SERVER['HTTP_USER_AGENT'];
    // var_dump($sUserBrowser);
    // var_dump($_COOKIE);
    
    if(isset($_COOKIE['arkey']) && strlen($_COOKIE['arkey'])>0){
        // var_dump('1-1');   
        $desC = new STD3Des(base64_encode('ar3527678')); 
        $password = $desC->decrypt($_COOKIE['arkey']);
        // var_dump($password);
        $password = str_replace($sUserBrowser,'', $password);
        // $password = $password.replace($sUserBrowser,'');
        // var_dump('1-2');    
    }
    // var_dump($password);
    // exit();
}
?>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>

    <!-- Basics -->
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>  

    <link rel="stylesheet" href="/style/reset.css"/>    
    <link rel="stylesheet" href="/style/login_new.css?<?php echo $version;?>"/>
    <script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.10.3.custom.js"></script>
    <script type="text/javascript" src="js/en-us/langlogin.js"></script>
    <script type="text/javascript" src="js/zh-cn/langlogin.js"></script>
    <script type="text/javascript" src="js/zh-tw/langlogin.js"></script>    
    <script type='text/javascript' src="/js/sha512.min.js"></script>
    <script type="text/javascript" src="js/Cookie.js"></script> 
    <script type="text/javascript" src="js/WA.js?<?php echo $version;?>"></script>
    <script type="text/javascript" src="js/md5.js"></script>   
    <script language='JavaScript' type='text/JavaScript'>
        var VMorVDI = "<?php echo $VMorVDI; ?>";
    </script>
  </head>

  <!-- Main HTML -->

  <body>

    <!-- Begin Page Content -->
    <div id="login">
        <div id="container">     
            <div style="position:  relative;width: 100%;height: 20%;top: 5px;">
                <div class="Logo"><img src = "img/MCLogo.png" style="left: 25px;top: 10px;position:  relative;"></img></div>                       
                <div style="float:right;height: 100%;padding-right: 42px;padding-top: 25px;width: 330px;">
                    <div style="float:left;text-align: right;line-height: 45px;width: 150px;right: -40px;position: relative;">
                        <label id="label_lang" for="language" class="L1" style="font-weight: 600;"><?php echo $lang->line("langIndex.ChooseLanguage")?></label>
                    </div>                        
                    <div style="float:right;">
                        <select id="select_lang" class="nameinput pw loginlun">
                            <option value="zh-tw" <?php if($lang->language_name == 'zh-tw')echo 'selected';?>>正體中文</option>
                            <option value="zh-cn" <?php if($lang->language_name == 'zh-cn')echo 'selected';?>>簡體中文</option>
                            <option value="en-us" <?php if($lang->language_name == 'en-us')echo 'selected';?>>English</option>                                                                                    
                        </select>                     
                    </div>                                     
                </div>
            </div>
            <div class="title_wrap" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'top:11px;'; else if ($lang->language_name == 'en-us')echo 'top:12px;';?>">
                <div class="title_offset">
                    <label id="title" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'font-size:34px;'; else if ($lang->language_name == 'en-us')echo 'font-size:39px;';?>"><?php echo $Header?></label>
                </div>                
            </div>            
            <form id="formlogin" action="Login.php"  method="post" name="login_form">
                <!--<label class="LabelHead" for="username">帳&nbsp&nbsp&nbsp號 :</label>-->
                <div>
                    <div class="formlogin_wrap">
                        <br>
                        <div class="username_wrap">  <!-- display: inline-block;-->                                     
                            <div class="label_username_div">
                                <label id="label_username" class="LabelHead L1" for="username" style="top: 11px;font-weight: 600;"><?php echo $lang->line("langIndex.Account")?></label>
                            </div>                        
                            <div style="float:left;">
                                <input type="text" id="username InputAccount" class="user loginlun chkusername" name="username" />
                                <div class="div_icon"><img id = "CurUserNamepic" class="pic_eyes" src = "img/ShowTextWord.png"></div>                                
                            </div>                                     
                        </div>        
                        <div class="pwd_wrap"> <!-- display: inline-block;-->                 
                            <div class="label_password_div">
                                <label id="label_password" for="password" class="L1" style="top: 11px;font-weight: 600;"><?php echo $lang->line("langIndex.PWD")?></label>
                            </div>                        
                            <div style="float:left;">
                                <input type="password" id="password InputPassword" class="nameinput pw loginlun chkpwd" name="password" value="<?php echo $password?>"/>
                                <div class="div_icon"><img id = "CurUserPWDpic" class="pic_eyes" src = "img/HideTextWord.png"></div>                                
                            </div>                                     
                        </div>
                        <div class="btn_wrap">                      
                            <div style="float:left;left: 13px;position: relative;">
                                <input id="input_login" type="submit" value="<?php echo $lang->line("langIndex.Login")?>" onclick="formhash(this.form, this.form.password);">
                                <input id="InputKey" name="AccessKey" type=hidden value=""/>
                            </div>                        
                            <div style="float:left;">
                                <img id = "chk_rmpwd" class = "Img_PC_ChkBxSaveKey rmpwd" src = "img/checkbox.png" ></img>
                                <label id="label_rmbme" class="label_rmbme" for="rmbme"><?php echo $lang->line("langIndex.RmbMe")?></label>
                            </div>                                     
                        </div>            
                    </div>
                </div>
                <div id="lower">
                    <label id="label_expire" <?php if(!$show_expire){echo 'style="display:none"';} ?>></label>
                    <!--<input type="checkbox"><label class="check" for="checkbox">Keep me logged in</label>-->                    
                </div><!--/ lower-->
            </form>
        </div><!--/ container-->
    </div>
    <!--/ container-->
    <!-- End Page Content -->
    <?php
        if (isset($_GET['error'])) {
            echo '<script>alert("Login Failed.");window.location = "index.php"</script>';
            exit();
        }            
    ?>    
    <script language='JavaScript' type='text/JavaScript' >              
        language_name="<?php echo $lang->language_name?>";        
        lang_en.loginExpire = lang_en.loginExpire.replace("$", "<?php echo $expire_time?>");
        lang_tw.loginExpire = lang_tw.loginExpire.replace("$", "<?php echo $expire_time?>");
        lang_cn.loginExpire = lang_cn.loginExpire.replace("$", "<?php echo $expire_time?>");
        switch('<?php echo $lang->language_name ?>'){
            case 'en-us':
                lang = lang_en;              
                break;
            case 'zh-tw':
                lang = lang_tw;
                break;
            case 'zh-cn':
                lang = lang_cn;
                break;
        }
        <?php
        if($show_expire)
        {
        ?>
            $('#label_expire').text(lang.loginExpire);
        <?php                
        }
        ?>
    </script>
  </body>

</html>
