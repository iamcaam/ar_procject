<?php
    // ini_set('display_errors', 'On');
    include_once("/var/www/html/libraries/Language.php");
    include_once ("/var/www/html/libraries/BaseAPI.php");
    include_once ("/var/www/html/libraries/HandleLogin.php");
    include_once ("/var/www/html/libraries/ErrorEnum.php");
    include_once ("/var/www/html/libraries/HandleVswitch.php");    
    include_once ("/var/www/html/libraries/HandleDisk.php");
    
    $oLogin = new Login();
    $oLogin->Sec_Session_Start();
    $sess_val = time();
    $sess_name = ini_get("session.upload_progress.name");
    $eLoginStatus = $oLogin->Login_Check();
    if($eLoginStatus != APIStatus::LoginSuccess)
    {
        header('Location: index.php');
        exit();      
    }    
    $oDisk = new DiskAction();
    $oDisk->listZfsDisk($outputDisk);    
    $oLogin->getStorageType();   
    $oLogin->getUIConfig($outputUIConfig); 
    $haveDedupe = false;
    if(isset($outputUIConfig['Dedupe']))
        $haveDedupe = $outputUIConfig['Dedupe'] == 1 ? true : false;  
    if(is_null($oLogin->isCeph)){
        echo 'Get Storage Type Error';
        exit();
    }
    $utilPolling = true;
    if(isset($outputUIConfig['UtilPolling']))
        $utilPolling = $outputUIConfig['UtilPolling'];            
    $lang = new Language(true);
    $lang->load("langAdminMain");
    $VDI = 1;
    $iSCSI = 0;    
    $Header = $lang->line("langAdminMain.Str_Header"); // 新式樣修改(藍黃) 2016.12.13 william
    if($outputUIConfig['Seed'] == 0)
        $Header = $lang->line("langAdminMain.Str_Header_VM");
    $VMorVDI = $lang->line("langAdminMain.Str_VD");
    $VMorVDIBack = $lang->line("langAdminMain.Str_VD_Back");
    
    $model="AFS-2010";
    $poweron_limit = $oLogin->getPoweonLimit(); 
    
    if(file_exists("/var/www/html/machine.config")){
        $data_json = file_get_contents("/var/www/html/machine.config");
        $data = json_decode($data_json,true);
        if($data != null){
            $model = $data["Model"];
        }
    }
    $ipFirst = '';
    $vSwitchC = new vSwitchAction();   
    for($i = 0 ; $i < 3; $i++){
        $vSwitchC->listVswitch(array('ConnectIP' =>'127.0.0.1'),$ouputVswitch);          
        if(count($ouputVswitch['Vswitchs']) == 0){           
            sleep(2);
        }
        else
            break;        
    }        
    if(count($ouputVswitch['Vswitchs']) > 0)
        $ipFirst = $ouputVswitch['Vswitchs'][0]['IP'];
    else
        $ouputVswitch['Vswitchs'][]=array('IP' => '','Mask'=>'','Gateway'=>'','Devs'=>array() );
?>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<?php
$version = "v1.0.0.240";
?>
<title><?php echo $Header;?></title>
<script type="text/javascript">
    if (typeof(Storage) !== "undefined") {
        // alert(sessionStorage.token);
        if (!sessionStorage.token) {
            window.location.href = 'index.php';
        }
    }
</script> 
<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.10.3.custom.js"></script>
<script type="text/javascript" src="js/md5.js"></script>
<script type="text/javascript" src="js/sha512.min.js"></script>
<script type="text/javascript" src="js/Cookie.js"></script>
<script src="/js/jquery.easytabs.js" type="text/javascript"></script>
<script type='text/javascript' src="/js/jquery.scombobox.js"></script>
<script type='text/javascript' src="/js/jquery.dropdown.min.js"></script>
<script type='text/javascript' src="/js/jquery.easing.min.js"></script>
<script type='text/javascript' src="/js/missed.js"></script>
<link type='text/css' href='/style/flat/blue.css' rel='stylesheet'/>
<link type='text/css' href='/style/minimal/blue.css' rel='stylesheet'/>
<link type='text/css' href="/style/jquery.scombobox.css" rel="stylesheet" />
<link type='text/css' href='/style/jquery.dropdown.css' rel='stylesheet'/>
<script type='text/javascript' src="/js/icheck.js"></script>
<script type="text/javascript" src="js/<?php echo $lang->language_name;?>/langAdmin_Main.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="js/<?php echo $lang->language_name;?>/langSysCfg_VM.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="js/<?php echo $lang->language_name;?>/langSysCfg_Raid.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="js/<?php echo $lang->language_name;?>/langSysCfg_DiskMgr.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="js/<?php echo $lang->language_name;?>/langSysCfg_Service.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="js/<?php echo $lang->language_name;?>/langSysCfg_UpdateFW.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="js/<?php echo $lang->language_name;?>/langSysCfg_System.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="js/<?php echo $lang->language_name;?>/langSysCfg_SystemInfo.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="js/<?php echo $lang->language_name;?>/langSysCfg_Network.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="js/<?php echo $lang->language_name;?>/langSysCfg_iSCSI.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="js/<?php echo $lang->language_name;?>/langError.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="js/<?php echo $lang->language_name;?>/langSysCfg_Log.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="js/<?php echo $lang->language_name;?>/langSysCfg_UserMgt.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="js/Admin_Main.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="/js/Structure.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="/js/ErrorHandle.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="/js/SysCfg_Performance.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="/js/SysCfg_VM.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="/js/SysCfg_SysInfo.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="/js/SysCfg_HostName.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="/js/HtmlCreate.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="/js/UISystemControl.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="/js/SysCfg_UpdateFw.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="/js/UIUpgrageControl.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="/js/UIIPControl.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="/js/UIADControl.js?<?php echo $version;?>"></script> <!-- /* 新增AD功能設定  2017.09.04 william */ -->
<script type="text/javascript" src="/js/SysCfg_iSCSI.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="/js/RelayAjax.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="js/SysCfg_DiskMgr.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="js/SysCfg_Ups.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="js/SysCfg_Email.js?<?php echo $version;?>"></script>
<?php
if($oLogin->isCeph)
    echo '<script type="text/javascript" src="js/SysCfg_Raid.js?'.$version.'"></script>';
else
    echo '<script type="text/javascript" src="js/SysCfg_Zfs.js?'.$version.'"></script>';
?>
<script type="text/javascript" src="js/SysCfg_log.js?<?php echo $version;?>"></script>
<script type="text/javascript" src="js/SysCfg_Schedule.js?<?php echo $version;?>"></script> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
<script type="text/javascript" src="js/backup.js?<?php echo $version;?>"></script> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
<script type="text/javascript" src="js/SysCfg_Autoss.js?<?php echo $version;?>"></script> <!-- vSwitch william 2017.01.19 新增 -->
<script type="text/javascript" src="js/SysCfg_Autobk.js?<?php echo $version;?>"></script> <!-- backup william 2017.02.22 新增 -->
<script type="text/javascript" src="js/jquery-ui-timepicker-addon.js?<?php echo $version;?>"></script> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
<script type="text/javascript" src="js/UserMgt_listview.js?<?php echo $version;?>"></script>
<link href="style/jquery-ui-1.10.3.custom.css" rel="stylesheet" type="text/css" />
<link href="style/Admin_Main.css?<?php echo $version;?>" rel="stylesheet" type="text/css" />
<link href="style/index.css?<?php echo $version;?>" rel="stylesheet" type="text/css" />
<link href="style/SysCfg_Network.css?<?php echo $version;?>" rel="stylesheet" type="text/css" />
<link href="style/SysCfg_Performance.css?<?php echo $version;?>" rel="stylesheet" type="text/css" />
<link href="style/SysCfg_DiskMgr.css?<?php echo $version;?>" rel="stylesheet" type="text/css" />
<link href="style/SysCfg_VM.css?'.$version.'" rel="stylesheet" type="text/css" />
<link href="style/SysCfg_Raid.css?<?php echo $version;?>" rel="stylesheet" type="text/css" />
<link href="style/SysCfg_Smtp.css?<?php echo $version;?>" rel="stylesheet" type="text/css" />
<link href="style/SysCfg_log.css?<?php echo $version;?>" rel="stylesheet" type="text/css" />
<link href="style/Account_PrivilegeMgr.css?<?php echo $version;?>" rel="stylesheet" type="text/css" />
<link href="/style/SysCfg_iSCSI.css?<?php echo $version;?>" rel="stylesheet" type="text/css" />
<!--<link href="style/Account_UserMgr.css" rel="stylesheet" type="text/css" />-->
<!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
<link href="style/SysCfg_takesnapshot.css?<?php echo $version;?>" rel="stylesheet" type="text/css" />
<link href="style/Backup_style.css?<?php echo $version;?>" rel="stylesheet" type="text/css" />    
<link href="style/SysCfg_schedule.css?<?php echo $version;?>" rel="stylesheet" type="text/css" />
<link href="style/SysCfg_create_schedule.css?<?php echo $version;?>" rel="stylesheet" type="text/css" />
<link href="style/jquery-ui-timepicker-addon.css?<?php echo $version;?>" rel="stylesheet" type="text/css" />
<link href="style/SysCfg_Autoss.css?<?php echo $version;?>" rel="stylesheet" type="text/css" />
<link href="style/SysCfg_Autobk.css?<?php echo $version;?>" rel="stylesheet" type="text/css" /> <!-- backup william 2017.02.22 新增 -->
<link href="style/vSwitch_style.css?<?php echo $version;?>" rel="stylesheet" type="text/css" /> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
<link href="style/UserMgt_listview.css?<?php echo $version;?>" rel="stylesheet" type="text/css" />
<?php
    $AccessKey=$_GET['sid'];
?>
<script language='JavaScript' type='text/JavaScript'>
var VMorVDI = "<?php echo $VMorVDI; ?>";
var VMorVDIBack = "<?php echo $VMorVDIBack; ?>";
var iSCSI = <?php echo $iSCSI; ?>;
var UtilPolling = <?php if($utilPolling)echo 'true';else echo 'false'; ?>;
var dedupe = <?php if($haveDedupe)echo 'true';else echo 'false'; ?>;
$(document).ready(function() 
{
    if ( "" != "<?php echo $AccessKey ?>" )
    {
        AccessKey = "<?php echo $AccessKey ?>";
    }
    sess_val = "<?php echo $sess_val ?>";
    InitialSettingAndEvent();

    // 新式樣修改(藍黃)--Alarm連結 2016.12.13 william
    $("#img_Alarm_Notify").click(function(){
        $("#H5_SubTitle_SysMgr_Network").click();
        $('#tab-container').easytabs('select', '#tabs1-alarm');  // $("a[href$='#tabs1-alarm']").click();
        $("#H5_SubTitle_SysMgr_Network").removeClass('MenuButtonClickHover'); /* SnapShot&Schedule&Backup  2017.01.17 william */
    });

    /* SnapShot&Schedule&Backup  2017.01.11 william */
    $("#img_RAID_Notify").click(function() {
        $("#H5_SubTitle_SysMgr_SysInfo").click();
        $("a[href$='#tabs1-cpu']").click();
        $("#H5_SubTitle_SysMgr_SysInfo").removeClass('MenuButtonClickHover');
    });

    // 新式樣修改(藍黃)--效能reset按鍵顯示 2016.12.12 william
    if ($('#tabs1-cpu').attr("class") !== 'active'){
        $("#btn_refresh_performance").css({"display":"none"});
    }

    $("a[href$='#tabs1-cpu']").click(function(){
        $("#btn_refresh_performance").css({"display":"inline"});
    });

    $(".MenuButtonLabel, a[href$='#tabs1-info']").click(function(){
        $("#btn_refresh_performance").css({"display":"none"});
    });

    $(".ui-spinner-button").css({"border-radius":"0px"});  /* 桌面暫停設定的上下按鈕  2017.02.03 william */
 
} );
</script>
</head>
<body>


<!--

	畫面主要切割為上中下三個部分


















	本頁面有五個功能，這五個功能都寫在 Div_LayoutZone_ 開頭的 Div 物件中，具備的 Div_Design_Layout 會讓這些
    設計用的畫面垂直並排在畫面上，方便製作。 而接下來的那層 Div_SysCfg_RaidMgr 才是真正的顯示層，他本身的 position
    是 absolute ，才可以精準的決定放在主畫面的 Div_Main_LayoutContain 的哪個位置，而其內層 Div_RelativeLayout
    則是用來讓裡頭的所有元件有個 relative 的父親，這樣才可以讓裡面元件以 position:absolute 來精準控制位置。


















    
    <div id = "Div_LayoutZone_SysCfg_RaidMgr" class = "Div_Design_Layout" >
        
        <div id = "Div_SysCfg_RaidMgr" class = "Div_MainContain" >
          
            <div class = "Div_RelativeLayout" >
            
            
    如果要再執行階段看到每個頁面的排版狀況，就把底下兩個函式遮掉就可以了。


















	
	//AddDivToLayer();

	//RemoveDesignLayoutZone();


-->


<!--
	+++++++++++++++++++++
	+		上部分		+
    +++++++++++++++++++++
-->
        
<div id = "Div_Main_LayoutTop" style="height: 100px" >
    
        <img id = "Img_Main_LayoutTop" />
        <div class="Logo">
            <img src = "img/MCLogo.png" style="left: 25px;top: 10px;position:  relative;"></img>
        </div>
        <div style="position: absolute;width: 100%;top:0px">
            <div style="position:relative;display: block;margin-right: auto;margin-left: auto;text-align: center;top:13px"> <!-- 新式樣修改(藍黃) 2016.12.13 william -->
                <label id = "H1_WebTitle" ><?php echo $Header?></label>
            </div>
        </div>
           
        <!-- 新式樣修改(藍黃)--按鈕式樣修改 2016.12.13 william -->
        <div class = "Div_BntBar" >
            <div id = "H5_SubTitle_SysMgr_SysInfo" class = "MenuButton" >
            <!-- <label class = "MenuButtonLabel" > -->
                <label class = "MenuButtonLabel" >
                    <?php echo $lang->line("langAdminMain.H5_SubTitle_Sys_Info")?>
                </label>
            </div>

            <div id = "H5_SubTitle_SysMgr_Network" class = "MenuButton" >
                <label class = "MenuButtonLabel" >
                    <?php echo $lang->line("langAdminMain.H5_SubTitle_SysMgr_System")?>
                </label>
            </div>

            <div id = "H5_SubTitle_SysMgr_Storage" class = "MenuButton" >
                <label class = "MenuButtonLabel" >                    
                    <?php 
                        if($oLogin->isCeph)                            
                            echo $lang->line("langAdminMain.H5_SubTitle_SysMgr_AcroSAN");
                        else
                            echo $lang->line("langAdminMain.H5_SubTitle_SysMgr_Storage");
                    ?>
                </label>
            </div>        
            
            <div id = "H5_SubTitle_SysMgr_AccountMgr_User" class = "MenuButton" >
                <label class = "MenuButtonLabel" >
                    <?php echo $lang->line("langAdminMain.H5_SubTitle_AccountMgr_User")?>
                </label>
            </div>

            <div id = "H5_SubTitle_SysMgr_VMSetting" class = "MenuButton">
                <label class = "MenuButtonLabel" >
                    <?php echo $VMorVDIBack.$lang->line("langAdminMain.H5_SubTitle_Setting")?>
                </label>
            </div>

            <?php
                if($outputUIConfig[APPluse] == 0){
                    echo '<div class = "MenuButton_disable"><label class = "MenuButtonLabel_disable MenuButtonLabel" style="right:240px">'.$lang->line("langAdminMain.H5_SubTitle_Services").'</label></div>';
                }
                else{
                    echo '<div id = "H5_SubTitle_SysMgr_Service" class = "MenuButton"><label class = "MenuButtonLabel" >'.$lang->line("langAdminMain.H5_SubTitle_Services").'</label></div>';
                }
            ?>

            <div id = "H5_SubTitle_SysMgr_Log" class = "MenuButton">
                <label class = "MenuButtonLabel" >
                    <?php echo $lang->line("langAdminMain.H5_SubTitle_Sys_Log")?>
                </label>
            </div>   

            <div id = "H5_Btn_Logout" class = "MenuButton" >
                <label class = "MenuButtonLabel" >
                    <?php echo $lang->line("langAdminMain.H5_Btn_Logout")?> 
                </label>
            </div>    
        </div>    

        <img id="img_RAID_Notify" src="img/TP.png" title="123">
        <div id="div_RAID_Notify" class="circle"></div>
        
        <img id="img_Alarm_Notify" src="img/alarm.png" title="123">
        <div id="div_Alarm_Notify" class="circle"></div>
        
        <div id="Power_Control" class = "MenuButton" >
            <div style="position:relative">
                <a href="#"  data-dropdown="#dropdown-Power" style="display:block;width: 60px;height: 25px"><img src="/img/PowerControl1.png" height="23" width="23" style="border:0"/></a> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
            </div>
        </div>
    </div>    

<!--
	+++++++++++++++++++++
	+		中部分		+
    +++++++++++++++++++++
-->

    <div id = "Div_Main_LayoutContain" class="lang<?php if($lang->language_name == 'zh-tw')echo '0'; else if ($lang->language_name == 'zh-cn')echo '1';else echo '2';?>"> <!-- 2018.01.08 william 列表標題及內容的文字位置調整 -->                          
    	
        <div id = "Div_MainContainBg" class = "Div_MainContain" >          	            
            
    	</div>        
    </div>
       
<!--
	+++++++++++++++++++++
	+		下部分		+
    +++++++++++++++++++++
-->  
    <div id = "Div_Main_LayoutBottom" >
        <h4 id ='H4_System_Vesion'><?php echo $model; ?></h4>
        <div style="display: block;margin-right: auto;margin-left: auto;text-align: center">            
        </div>
    </div>
    
    <!--
        ++++++++++++++++++++++++++++++
        +			Network			 +
        ++++++++++++++++++++++++++++++
    -->
    <div id = "Div_LayoutZone_SysCfg_Network" class = "Div_Design_Layout" >        
        <div id = "Div_SysCfg_Network" class = "Div_MainContain" >            
            <div id="tab-container" class='tab-container' style="margin: 20px">
                <ul class='etabs'>                    
                    <li class='tab'><a href="#tabs1-ip"><?php echo $lang->line("langAdminMain.System_IPSetting")?></a></li>                       
                    <li class='tab'><a href="#tabs1-external-ip"><?php echo $lang->line("langAdminMain.System_ExternalIP")?></a></li>                       
                    <li class='tab'<?php if(!$iSCSI){echo ' style="display:none"'; } ?>><a href="#tabs1-iscsi-ip"><?php echo $lang->line("langAdminMain.System_iSCSI_IPSetting")?></a></li>
                    <li class='tab'><a href="#tabs1-time"><?php echo $lang->line("langAdminMain.System_Time")?></a></li>                                                                               
                    <li class='tab'><a href="#tabs1-password"><?php echo $lang->line("langAdminMain.System_ChgPWD")?></a></li>
                    <li class='tab'><a href="#tabs1-host-name"><?php echo $lang->line("langAdminMain.System_HostName")?></a></li>
                    <li class='tab'><a href="#tabs1-smb"><?php echo $lang->line("langAdminMain.Service_Samba")?></a></li>  <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                    <li class='tab '><a href="#tabs1-ssh"?><?php echo $lang->line("langAdminMain.Service_SSH")?></a></li>
                    <li class='tab'><a href="#tabs1-alarm"><?php echo $lang->line("langAdminMain.System_Alarm")?></a></li>
                    <li class='tab'><a href="#tabs1-system-update"><?php echo $lang->line("langAdminMain.Update_SystemUpate")?></a></li>
                    <li class='tab'><a href="#tabs1-ad"><?php echo $lang->line("langAdminMain.System_ADSetting")?></a></li> <!-- 新增AD功能設定  2017.09.04 william --> 
                    <li class='tab'><a href="#tabs1-ups"><?php echo $lang->line("langAdminMain.System_UPSSetting")?></a></li>
                    <li class='tab'><a href="#tabs1-email">Email</a></li>
                </ul>
                <div class='panel-container'>                    
                    <div id="tabs1-ip">
                        <!--<div  style="width: 550px;height: 320px">-->
                        <div  style="width: 550px">
                            <div style="width: 735px;font-size: 12px;overflow: auto;"> <!-- vSwitch william 2017.01.19  新增:overflow: auto;position: relative; 原值:width: 550px;-->                                                                 
                                    <div id="div_ip_field">            
                                    </div>                           
                                    <div style="width: 685px;text-align: right;padding-top: 5px;height: 30px;position: absolute;"> <!-- vSwitch william 2017.01.19  原值:width: 550px;;position: absolute;top: 542px;取消margin-top: 20px;新增padding-top  -> 取消 top: 542px; 2017.02.03 -->                                  
                                        <a id="btn_ip_confirm" class="btn-jquery large-btn" ><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
                                        <a style="margin-left: 10px" id="btn_ip_cancel" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
                                    </div>                                
                            </div>   
                        </div>
                    </div>
                    <div id="tabs1-iscsi-ip" <?php if(!$iSCSI){echo ' style="display:none"';} ?>>
                        <div style="width: 550px;font-size: 12px">                                
                            <div id="div_iscsi_ip_field">            
                            </div>                           
                            <div style="width: 550px;text-align: right;margin-top: 20px;height: 30px">
                                <a id="btn_iscsi_ip_confirm" href="#" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
                                <a style="margin-left: 10px" id="btn_iscsi_ip_cancel" href="#" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
                            </div>                                
                        </div>   
                    </div>
                    <div id="tabs1-external-ip" style="font-size: 12px">
                        <div class="div_fieldcontent" style="width: 100%;top: 3px;right: 30px;position: relative"> <!-- 新增外部IP及轉址功能設定  2017.09.04 william 480px -> 800px -->      
                            <label class="LabelHead" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'width: 65px;';else echo 'width: 85px;';?>"><?php echo $lang->line("langAdminMain.System_ExternalIP")?> :</label>                    
                            <input id="input_ExternalIP" class="InputText" value="" type="text">
                            <div style="display:  inline-block;padding: 0 10px;font-size: 12px;font-weight: 900;color: red;">/</div>
                            <label class="LabelHead" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'width: 75px;';else echo 'width: 75px;';?>"><?php echo $lang->line("langAdminMain.System_PortFrom")?> :</label>      
                            <input id="input_PortFrom" class="InputText" value="" type="number" style="width:50px;text-align: center;">
                            <div style="display:  inline-block;padding: 0 10px;font-size: 12px;font-weight: 900;color: red;">/</div>
                            <label class="LabelHead" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'width: 60px;';else echo 'width: 110px;';?>"><?php echo $lang->line("langAdminMain.System_PortCount")?> :</label>
                            <input id="input_PortCount" class="InputText" value="" type="number" style="width:50px;text-align: center;">
                            <!-- 新增外部IP及轉址功能設定  2017.09.04 william position: absolute;right: 12px -> position: relative;display: inline-block; -->                                 
                            <div style="position: relative;display: inline-block;bottom: 2px;"> 
                                <a id="btn_chg_external_ip" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
                                <a id="btn_clear_external_ip" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
                            </div>
                            <div style="height:10px"></div>
                        </div>
                        <!-- 新增外部IP及轉址功能設定  2017.09.04 william  外部IP列表 -->
                        <div id="div_exip_scroll" style="margin-top: 6px;"> <!-- 取消style="overflow-x: auto;overflow-y: auto"; 2017.03.03 william -->
                            <div class="Div_exip_Shot">                            
                                <div id="LogRowHeader" class = "Div_VMLognRow VM_RowHeader" style="width: 100%;height: 32px;padding-top: 3px;border-top-left-radius: 0px;border-top-right-radius: 0px;overflow: hidden;position: relative;"> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                                    <div id="Divexip_RowHeader_inner"  style="height: 100%;width: 100%;overflow-x: auto;overflow-y: hidden;position: absolute;padding-bottom: 30px;top: 0px;">
                                        <div class="VMList_ItemNo AbsoluteLayout RowField">                                        
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader" id="VMList_SN_1"><?php echo $lang->line("langAdminMain.VMList_SN")?></H4> <!-- 新式樣修改(藍黃) 2016.12.13 william -->
                                            </div>
                                        </div>
                                        <!--<div class="VMList_Select AbsoluteLayout RowField">
                                            <div style="position:relative">
                                                <input type="checkbox" class="iCheckBoxAllSelect">
                                            </div>
                                        </div>-->
                                        <div class = "EXIPList_IP AbsoluteLayout RowField VMHeader">                                        
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.System_ExternalIP")?>&nbsp;</H4>
                                            </div>
                                        </div>
                                        <div class = "EXIPList_PortFrom AbsoluteLayout RowField VMHeader">                                        
                                            <div class="div_RowHeader" style="float: right;">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.System_PortFrom")?>&nbsp;</H4>
                                            </div>
                                        </div>

                                        <div class = "EXIPList_PortTo AbsoluteLayout RowField VMHeader" >
                                            <div class="div_RowHeader" style="float: right;">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.System_PortTo")?>&nbsp;</H4>                                                                        
                                            </div>
                                        </div>
                                        <div class = "EXIPList_PortCount AbsoluteLayout RowField VMHeader" >
                                            <div class="div_RowHeader" style="float: right;">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.System_PortCount")?>&nbsp;</H4>
                                            </div>
                                        </div> 
                                        <div class = "EXIPList_Action AbsoluteLayout RowField" >
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.List_Action")?></H4>                                            
                                            </div>                                        
                                        </div>      

   
                                    </div>                                                                         
                                </div>                      
                                <!--  VM - List  -->
                                <div id = "Div_exipList" style="font-size: 12px;width: 100%;overflow-y: auto;" onscroll="document.getElementById('Divexip_RowHeader_inner').scrollLeft = this.scrollLeft;"> <!-- 取消overflow-x: hidden; 2017.03.03 william -->     

                                </div> 
                            </div>
                        </div>
                    </div>
                   <div id="tabs1-time" style="font-size: 12px">
                        <div id="div_time" class="div_fieldcontent" style="width: 650px;position: relative;overflow-y: auto;overflow-x: hidden">                                
                            <div style="height: 20px;border-bottom: dashed 1px rgba(0, 0, 0, .2);
                                    -webkit-background-clip: padding-box; 
                                    background-clip: padding-box;width: 600px">
                                <label style="padding: 10px;color: blue;font-size: 14px"><?php echo $lang->line("langAdminMain.System_CurrentTime")?></label>    
                            </div>                            
                            <label id="curTime" style="position: absolute;top:30px" class="LabelHead TimeShift">1234</label>
                            <div style="height:30px"></div>
                            <div style="height: 20px;border-bottom: dashed 1px rgba(0, 0, 0, .2);
                                    -webkit-background-clip: padding-box; 
                                    background-clip: padding-box;top: 60px;width: 600px">
                                <label style="padding: 10px;color: blue;font-size: 14px"><?php echo $lang->line("langAdminMain.System_TimeZone")?></label>    
                            </div>                            
                            <div style="height:40px;position: relative;">
                                <label class="LabelHead TimeShift" style="position: absolute;top:15px" ><?php echo $lang->line("langAdminMain.System_TimeZone")?> :</label>
                                <select id="combotimezone" >
                                    <option value="Pacific/Midway">(GMT-11:00) Samnona Standard Time; Midway Is.</option>
                                    <option value="Pacific/Honolulu">(GMT-10:00) Hawaii Standard Time</option>
                                    <option value="US/Alaska">(GMT-09:00) Alaska Standard Time</option>
                                    <option value="US/Pacific">(GMT-08:00) Pacific Time(US & Canada); Tijuana</option>
                                    <option value="US/Arizona">(GMT-07:00) Arizona</option>
                                    <option value="US/Mountain">(GMT-07:00) Chihuahua, Mazatlan</option>
                                    <option value="America/Chihuahua">(GMT-07:00) Mountain Time (US & Canada)</option>
                                    <option value="America/Guatemala">(GMT-06:00) Central America Standard Time; Guate</option>
                                    <option value="US/Central">(GMT-06:00) Central Time (US & Canada)</option>
                                    <option value="America/Mexico_City">(GMT-06:00) Mexico City; Tegucigalpa</option>
                                    <option value="Canada/Saskatchewan">(GMT-06:00) Saskatchewan</option>
                                    <option value="America/Bogota">(GMT-05:00) Bogota, Lima, Quito, Rio Branco</option>
                                    <option value="US/Eastern">(GMT-05:00) Eastern Time (US & Canada)</option>
                                    <option value="US/East-Indiana">(GMT-05:00) Indiana (East)</option>
                                    <option value="America/Caracas">(GMT-04:30) Caracas</option>
                                    <option value="Canada/Atlantic">(GMT-04:00) Atlantic Time (Canada)</option>
                                    <option value="America/La_Paz">(GMT-04:00) La Paz</option>
                                    <option value="America/Manaus">(GMT-04:00) Manaus</option>
                                    <option value="America/New_York">(GMT-04:00) New York</option>
                                    <option value="Canada/Newfoundland">(GMT-03:30) Newfoundland</option>
                                    <option value="America/Santiago">(GMT-03:00) Santiago</option>
                                    <option value="America/Fortaleza">(GMT-03:00) Brasilia</option>
                                    <option value="America/Buenos_Aires">(GMT-03:00) Buenos Aires, Georgetown</option>
                                    <option value="America/Godthab">(GMT-03:00) Greenland Standard Time</option>
                                    <option value="America/Montevideo">(GMT-02:00) Montevideo</option>
                                    <option value="Atlantic/South_Georgia">(GMT-02:00) Mid-Atlantic Standard Time</option>
                                    <option value="Atlantic/Azores">(GMT-01:00) Azores Standard Time</option>
                                    <option value="Atlantic/Cape_Verde">(GMT-01:00) Cape Verde Is.</option>
                                    <option value="Africa/Casablanca">(GMT) Casablanca</option>
                                    <option value="Europe/London">(GMT) Dublin, Edinburgh, Lisbon, London</option>
                                    <option value="Africa/Monrovia">(GMT) Monrovia, Reykjavik</option>
                                    <option value="Europe/Amsterdam">(GMT+01:00) Amsterdam, Berlin, Rome, Stockholm, Vienna</option>
                                    <option value="Europe/Belgrade">(GMT+01:00) Belgrade, Bratislava, Budapest, Prague</option>
                                    <option value="Europe/Brussels">(GMT+01:00) Brussels, Copenhagen, Madrid, Paris</option>
                                    <option value="Europe/Sarajevo">(GMT+01:00) Sarajevo, Skopie, Warsaw, Zagreb</option>
                                    <option value="Africa/Kinshasa">(GMT+01:00) Western Africa Time</option>
                                    <option value="Africa/Windhoek">(GMT+02:00) Windhoek</option>
                                    <option value="Asia/Amman">(GMT+02:00) Amman</option>
                                    <option value="Europe/Athens">(GMT+02:00) Athens, Bucharest, Istanbul</option>
                                    <option value="Asia/Beirut">(GMT+02:00) Beirut</option>
                                    <option value="Egypt">(GMT+02:00) Egypt Standard Time</option>
                                    <option value="Africa/Harare">(GMT+02:00) Harare, Pretoria</option>
                                    <option value="Europe/Helsinki">(GMT+02:00) Helsinki, Kyiv, Riga, Sofia, Tallinn, Vilnius</option>
                                    <option value="Israel">(GMT+02:00) Israel Standard Time</option>
                                    <option value="Africa/Gaborone">(GMT+02:00) Central Africa Time</option>
                                    <option value="Europe/Sofia">(GMT+02:00) Eastern Europe Standard Time</option>
                                    <option value="Europe/Minsk">(GMT+03:00) Minsk</option>
                                    <option value="Asia/Baghdad">(GMT+03:00) Baghdad</option>
                                    <option value="Asia/Kuwait">(GMT+03:00) Kuwait</option>
                                    <option value="Africa/Nairobi">(GMT+03:00) Nairobi</option>
                                    <option value="Europe/Moscow">(GMT+03:00) Moscow, St. Petersburg, Kazan, Volgograd</option>
                                    <option value="Iran">(GMT+03:30) Iran Standard Time</option>
                                    <option value="Asia/Muscat">(GMT+04:00) Abu Dhabi, Muscat</option>
                                    <option value="Asia/Baku">(GMT+04:00) Baku</option>
                                    <option value="Asia/Tbilisi">(GMT+04:00) Tbilisi</option>
                                    <option value="Asia/Yerevan">(GMT+04:00) Yerevan</option>
                                    <option value="Asia/Kabul">(GMT+04:30) Afghanistan Standard Time</option>
                                    <option value="Asia/Karachi">(GMT+05:00) Karachi, Islamabad, Tashkent</option>
                                    <option value="Asia/Yekaterinburg">(GMT+05:00) Ekaterinburg</option>
                                    <option value="Asia/Calcutta">(GMT+05:30) Bombay, Calcutta, Madras, New Delhi, Colombo</option>
                                    <option value="Asia/Kathmandu">(GMT+05:45) Kathmandu</option>
                                    <option value="Asia/Almaty">(GMT+06:00) Almaty, Astana</option>
                                    <option value="Asia/Dhaka">(GMT+06:00) Dhaka</option>
                                    <option value="Asia/Novosibirsk">(GMT+06:00) Novosibirsk</option>
                                    <option value="Asia/Rangoon">(GMT+06:30) Yangon(Rangoon)</option>
                                    <option value="Asia/Bangkok">(GMT+07:00) Bangkok, Hanoi, Jakarta</option>
                                    <option value="Asia/Krasnoyarsk">(GMT+07:00) Krasnoyarsk</option>
                                    <option value="Asia/Taipei">(GMT+08:00) Taipei</option>
                                    <option value="Asia/Hong_Kong">(GMT+08:00) Beijing, Chongqing, Hong Kong, Urumqi</option>
                                    <option value="Asia/Ulaanbaatar">(GMT+08:00) Ulaanbaatar</option>
                                    <option value="Asia/Kuala_Lumpur">(GMT+08:00) Kuala Lumpur, Singapore</option>
                                    <option value="Australia/Perth">(GMT+08:00) Perth</option>
                                    <option value="Asia/Irkutsk">(GMT+08:00) Irkutsk</option>
                                    <option value="Asia/Tokyo">(GMT+09:00) Tokyo, Osaka, Sapporo</option>
                                    <option value="Asia/Seoul">(GMT+09:00) Seoul</option>
                                    <option value="Asia/Yakutsk">(GMT+09:00) Yakutsk</option>                                        
                                    <option value="Australia/Darwin">(GMT+09:30) Darwin</option>
                                    <option value="Australia/Brisbane">(GMT+10:00) Brisbane</option>                                        
                                    <option value="Pacific/Guam">(GMT+10:00) Guam, Port Moresby</option>                                        
                                    <option value="Asia/Vladivostok">(GMT+10:00) Vladivostok</option>
                                    <option value="Asia/Magadan">(GMT+10:00) Magadan, Solomon Is., New Caledonia</option>
                                    <option value="Australia/Adelaide">(GMT+10:30) Adelaide</option>
                                    <option value="Australia/Tasmania">(GMT+11:00) Tasmania Standard Time</option>
                                    <option value="Australia/Melbourne">(GMT+11:00) Melbourne, Sydney, Canberra</option>
                                    <option value="Pacific/Noumea">(GMT+11:00) New Caledonia</option>
                                    <option value="Pacific/Auckland">(GMT+12:00) Auckland, Wellington</option>
                                    <option value="Pacific/Fiji">(GMT+12:00) Fiji, Kamchatka, Marshall Is.</option>
                                </select>                 
                            </div>
                            <div style="height:10px"></div>
                            <div style="height: 20px;border-bottom: dashed 1px rgba(0, 0, 0, .2);
                                    -webkit-background-clip: padding-box; /* for Safari */
                                    background-clip: padding-box;top:140px;width: 600px">
                                <label style="padding: 10px;color: blue;font-size: 14px"><?php echo $lang->line("langAdminMain.System_TimeSetting")?></label>    
                            </div>
                            <div style="height:10px"></div>
                            <div style="height:230px"> 
                                <div style="position: relative">
                                    <div style="position: relative;top:8px;left:20px">
                                        <input type="radio" id="radio-manually" name="radio-time"  class="iCheckRadio" value="manually" >
                                        <label class="LabelHead" style="position: absolute;top:2px;left: 30px;cursor:pointer" for="radio-manually"><?php echo $lang->line("langAdminMain.System_Manually")?></label>
                                    </div>                                    
                                    <label class="LabelHead TimeSettingShift" style="position: absolute;top:50px;"><?php echo $lang->line("langAdminMain.System_Date")?> :</label> 
                                    <input type="text" id="datepicker" style="position: absolute;
                                    width: 350px;
                                    height: 30px;
                                    top: 40px;
                                    left: 225px;"> 
                                    <label class="LabelHead TimeSettingShift" style="position: absolute;top:95px;"><?php echo $lang->line("langAdminMain.System_Time")?> :</label> 
                                    <select id="combohour" >
                                        <option value="00">00</option>
                                        <option value="01">01</option>
                                        <option value="02">02</option>
                                        <option value="03">03</option>
                                        <option value="04">04</option>
                                        <option value="05">05</option>
                                        <option value="06">06</option>
                                        <option value="07">07</option>
                                        <option value="08">08</option>
                                        <option value="09">09</option>
                                        <option value="10">10</option>
                                        <option value="11">11</option>
                                        <option value="12">12</option>
                                        <option value="13">13</option>
                                        <option value="14">14</option>
                                        <option value="15">15</option>
                                        <option value="16">16</option>
                                        <option value="17">17</option>
                                        <option value="18">18</option>
                                        <option value="19">19</option>
                                        <option value="20">20</option>
                                        <option value="21">21</option>
                                        <option value="22">22</option>
                                        <option value="23">23</option>
                                    </select>
                                    <label id="lablehourmin">:</label>
                                    <select id="combomin" >
                                        <option value="00">00</option>
                                        <option value="01">01</option>
                                        <option value="02">02</option>
                                        <option value="03">03</option>
                                        <option value="04">04</option>
                                        <option value="05">05</option>
                                        <option value="06">06</option>
                                        <option value="07">07</option>
                                        <option value="08">08</option>
                                        <option value="09">09</option>
                                        <option value="10">10</option>
                                        <option value="11">11</option>
                                        <option value="12">12</option>
                                        <option value="13">13</option>
                                        <option value="14">14</option>
                                        <option value="15">15</option>
                                        <option value="16">16</option>
                                        <option value="17">17</option>
                                        <option value="18">18</option>
                                        <option value="19">19</option>
                                        <option value="20">20</option>
                                        <option value="21">21</option>
                                        <option value="22">22</option>
                                        <option value="23">23</option>
                                        <option value="24">24</option>
                                        <option value="25">25</option>
                                        <option value="26">26</option>
                                        <option value="27">27</option>
                                        <option value="28">28</option>
                                        <option value="29">29</option>
                                        <option value="30">30</option>
                                        <option value="31">31</option>
                                        <option value="32">32</option>
                                        <option value="33">33</option>
                                        <option value="34">34</option>
                                        <option value="35">35</option>
                                        <option value="36">36</option>
                                        <option value="37">37</option>
                                        <option value="38">38</option>
                                        <option value="39">39</option>
                                        <option value="40">40</option>
                                        <option value="41">41</option>
                                        <option value="42">42</option>
                                        <option value="43">43</option>
                                        <option value="44">44</option>
                                        <option value="45">45</option>
                                        <option value="46">46</option>
                                        <option value="47">47</option>
                                        <option value="48">48</option>
                                        <option value="49">49</option>
                                        <option value="50">50</option>  
                                        <option value="51">51</option>
                                        <option value="52">52</option>
                                        <option value="53">53</option>
                                        <option value="54">54</option>
                                        <option value="55">55</option>
                                        <option value="56">56</option>
                                        <option value="57">57</option>
                                        <option value="58">58</option>
                                        <option value="59">59</option>
                                    </select>
                                    <label id="lableminsecond">:</label>
                                    <select id="combosecond" >
                                        <option value="00">00</option>
                                        <option value="01">01</option>
                                        <option value="02">02</option>
                                        <option value="03">03</option>
                                        <option value="04">04</option>
                                        <option value="05">05</option>
                                        <option value="06">06</option>
                                        <option value="07">07</option>
                                        <option value="08">08</option>
                                        <option value="09">09</option>
                                        <option value="10">10</option>
                                        <option value="11">11</option>
                                        <option value="12">12</option>
                                        <option value="13">13</option>
                                        <option value="14">14</option>
                                        <option value="15">15</option>
                                        <option value="16">16</option>
                                        <option value="17">17</option>
                                        <option value="18">18</option>
                                        <option value="19">19</option>
                                        <option value="20">20</option>
                                        <option value="21">21</option>
                                        <option value="22">22</option>
                                        <option value="23">23</option>
                                        <option value="24">24</option>
                                        <option value="25">25</option>
                                        <option value="26">26</option>
                                        <option value="27">27</option>
                                        <option value="28">28</option>
                                        <option value="29">29</option>
                                        <option value="30">30</option>
                                        <option value="31">31</option>
                                        <option value="32">32</option>
                                        <option value="33">33</option>
                                        <option value="34">34</option>
                                        <option value="35">35</option>
                                        <option value="36">36</option>
                                        <option value="37">37</option>
                                        <option value="38">38</option>
                                        <option value="39">39</option>
                                        <option value="40">40</option>
                                        <option value="41">41</option>
                                        <option value="42">42</option>
                                        <option value="43">43</option>
                                        <option value="44">44</option>
                                        <option value="45">45</option>
                                        <option value="46">46</option>
                                        <option value="47">47</option>
                                        <option value="48">48</option>
                                        <option value="49">49</option>
                                        <option value="50">50</option>  
                                        <option value="51">51</option>
                                        <option value="52">52</option>
                                        <option value="53">53</option>
                                        <option value="54">54</option>
                                        <option value="55">55</option>
                                        <option value="56">56</option>
                                        <option value="57">57</option>
                                        <option value="58">58</option>
                                        <option value="59">59</option>
                                    </select>
                                    <div style="position: relative;top:115px;left:20px">
                                        <input type="radio" id="radio-sync" name="radio-time"  class="iCheckRadio" value="sync">
                                        <label class="LabelHead" style="position: absolute;top:2px;left: 30px;cursor:pointer" for="radio-sync"><?php echo $lang->line("langAdminMain.System_Sync")?></label>
                                    </div>                                    
                                    <label class="LabelHead TimeSettingShift" style="position: absolute;top:170px"><?php echo $lang->line("langAdminMain.System_Server_Address")?> :</label> 
                                    <select id="combontpserver">
                                        <option value="pool.ntp.org">pool.ntp.org</option>
                                        <option value="time.nist.gov">time.nist.gov</option>
                                    </select>
                                    <!--<a id="btnupdate" class="TimeSettingShift btn-jquery large-btn" style="position: absolute;top:195px;width: 125px"><?php echo $lang->line("langAdminMain.System_Update_Now")?></a>                                    -->
                                </div>                                
                            </div>                                                                     
                            
                            <div style="position:absolute;right: 60px">                                                              
                                <a id='btn_set_time'class="btn-jquery large-btn"></a> <!-- <a id='btn_set_time' class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>  -->
                                <a id='btn_reset_time' class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>                                    
                            </div>
                        </div>
                        <!--</div>-->
                    </div>
                    <div id="tabs1-password" style="font-size: 12px"> 
                        <div class="div_fieldcontent" style="width: 510px;position: relative">                                        
                            <label class="LabelHead"><?php echo $lang->line("langAdminMain.System_CurrentPWD")?></label>                    
                            <input id="CurPWD" class="InputText" value="" type="password">
                            <!--<img id = "CurPWDpic" class="CurPWDpic adminmain_eyes" src = "img/HideTextWord.png" >-->
                            <br>    
                            <br>
                            <label class="LabelHead"><?php echo $lang->line("langAdminMain.System_NewPWD")?></label>                    
                            <input id="NewPWD" class="InputText"  value="" type="password">
                            <!--<img id = "NewPWDpic" class="NewPWDpic adminmain_eyes" src = "img/HideTextWord.png" >                            -->
                            <br>
                            <br>
                            <label class="LabelHead"><?php echo $lang->line("langAdminMain.System_ConfirmNewPWD")?></label>                    
                            <input id="ConfNewPWD" class="InputText"  value="" type="password">                    
                            <!--<img id = "ConfNewPWDpic" class="ConfNewPWDpic adminmain_eyes" src = "img/HideTextWord.png" >                            -->
                            <br>
                            <br>
                            <div style="position: absolute;right: 41px;margin-top: 5px;"> <!-- 有眼睛的位置調整到right: 4px; 2017.03.24 william -->
                                <a id="btn_chg_pwd" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
                                <a id="btn_clear_pwd" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
                            </div>
                        </div>
                    </div>     
                    <div id="tabs1-host-name" style="font-size: 12px">     
                        <div class="div_fieldcontent" style="width: 480px;position: relative">
                            <label class="LabelHead"><?php echo $lang->line("langAdminMain.System_HostName").' :'?></label>                    
                            <input id="input_HostName" class="InputText" value="" type="text" maxlength="60">
                                <div style="height:10px"></div>
                            <div style="position: absolute;right: 12px">
                                <a id="btn_chg_host_name" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
                                <a id="btn_clear_host_name" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
                            </div>
                        </div>
                    </div>
                    <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                    <div id="tabs1-smb">
                        <!--<div  class="div_fieldcontent" style="width: 680px;height: 380px;overflow-x: hidden;overflow-y: auto;">  Size調整 2017.03.01 william 原值：width: 550px;height: 320px -->
                            <div id="div_smb" class="div_fieldcontent" style="font-size: 12px;width: 650px;position: relative;overflow-x: hidden;overflow-y: auto;"> <!-- Size調整 2017.03.01 william 原值：height: 320px -->                                                                    
                                <div style="width: 550px;height: 130px;margin: 15px 0px;position: relative;">                               
                                    <div style="position: absolute;
                                        -webkit-background-clip: padding-box; 
                                        background-clip: padding-box;width: 800px">
                                        <label style="padding: 10px;color: #044de1;font-size: 16px"><?php echo $lang->line("langAdminMain.Service_Samba_Server")?></label>    
                                    </div>                                                 
                                    <div style="position: absolute;top:40px;left: 50px">
                                        <label class="LabelHead">IP : </label>                    
                                        <label id="label_nasip"><?php echo $ipFirst;?></label>                    
                                        <br>
                                        <br>
                                        <label class="LabelHead"><?php echo $lang->line("langAdminMain.Service_Account");?></label>                    
                                        <label>manager</label>
                                        <br>
                                        <br>
                                        <div style="position: relative;width: 800px">
                                            <label class="LabelHead"><?php echo $lang->line("langAdminMain.Service_Server_Status")?></label>
                                            <label id="label_smd_status"></label>
                                            <div style=" position: relative;top: 10px;margin-left: 213px;">
                                                <!--<div style="position: absolute;top: 0px;left: 210px">
                                                    <input type="radio" id="radio-enable-smb" name="radio-smb"  class="iCheckRadioSMB" value="1" >
                                                    <label style="cursor:pointer;" for="radio-enable-smb"><?php echo $lang->line("langAdminMain.Service_Enable")?></label>
                                                </div>
                                                <div style="position: absolute;top: 0px;left: 290px">
                                                    <input type="radio" id="radio-disable-smb" name="radio-smb"  class="iCheckRadioSMB" value="0">
                                                    <label style="cursor:pointer" for="radio-disable-smb"><?php echo $lang->line("langAdminMain.Service_Disable")?></label>
                                                </div>-->
                                                <a id="btn_enable_smb" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Service_Enable")?></a>
                                                <a id="btn_disable_smb" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Service_Disable")?></a>                                                   
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                 <div style="width: 550px;height: 130px;margin: 15px 0px;position: relative;top: 15px;">
                                    <div style="position: absolute;
                                        padding-top: 10px;
                                        top:0px">
                                        <label style="padding: 10px;color: #044de1;font-size: 16px;white-space: nowrap;"><?php echo $lang->line("langAdminMain.Service_ChgPWD_Samba_Attention")?></label>                        
                                    </div>
                                    <div style="position: absolute;top:46px;left: 50px">                    
                                        <label class="LabelHead"><?php echo $lang->line("langAdminMain.System_NewPWD")?></label>
                                        <input id="ChangeSMBPWD" class="InputText"  value="" type="password"></input>
                                        <!--<img id = "ChangeSMBPWDpic" class="ChangeSMBPWDpic adminmain_eyes" src = "img/HideTextWord.png" >  -->
                                        <br>
                                        <br>    
                                        <label class="LabelHead"><?php echo $lang->line("langAdminMain.System_ConfirmNewPWD")?></label>
                                        <input id="ConfirmSMBPWD" class="InputText"  value="" type="password"></input>
                                        <!--<img id = "ConfirmSMBPWDpic" class="ConfirmSMBPWDpic adminmain_eyes" src = "img/HideTextWord.png" >-->
                                        <br> 
                                        <br>   
                                        <div style="right: 12px">
                                            <a id="btn_for_smb_cancel" class="btn-jquery  large-btn" style="float: right;"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
                                            <a id="btn_for_smb" class="btn-jquery btn_change_vnc_pwd large-btn" style="float: right;"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        <!--</div>-->
                    </div>      
                    <div id="tabs1-ssh">
                        <!--<div  class="div_fieldcontent" style="width: 680px;height: 380px;overflow-x: hidden;overflow-y: auto;">  Size調整 2017.03.01 william 原值：width: 550px;height: 320px -->
                            <div class="div_fieldcontent" style="font-size: 12px;width: 650px;position: relative;overflow-x: hidden;overflow-y: auto;"> <!-- Size調整 2017.03.01 william 原值：height: 320px -->                     
                                <div style="width: 550px;height: 130px;margin: 15px 0px;position: relative;">                               
                                    <div style="position: absolute;
                                        -webkit-background-clip: padding-box; 
                                        background-clip: padding-box;width: 800px">
                                        <label style="padding: 10px;color: #044de1;font-size: 16px"><?php echo $lang->line("langAdminMain.Service_SSH")?></label>    
                                    </div>                                                 
                                    <div style="position: absolute;top:40px;left: 50px">
                                        <div style="position: relative;width: 800px">
                                            <label class="LabelHead" style="width: 85px;"><?php echo $lang->line("langAdminMain.Service_Server_Status")?></label>
                                            <label id="label_ssh_status"></label>
                                            <div style=" position: relative;top: 10px;margin-left: 88px;">
                                                <!--<div style="position: absolute;top: 0px;left: 210px">
                                                    <input type="radio" id="radio-enable-ssh" name="radio-ssh"  class="iCheckRadioSSH" value="1" >
                                                    <label style="cursor:pointer;" for="radio-enable-ssh"><?php echo $lang->line("langAdminMain.Service_Enable")?></label>
                                                </div>
                                                <div style="position: absolute;top: 0px;left: 290px">
                                                    <input type="radio" id="radio-disable-ssh" name="radio-ssh"  class="iCheckRadioSSH" value="0">
                                                    <label style="cursor:pointer" for="radio-disable-ssh"><?php echo $lang->line("langAdminMain.Service_Disable")?></label>
                                                </div>-->
                                                <a id="btn_enable_ssh" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Service_Enable")?></a>
                                                <a id="btn_disable_ssh" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Service_Disable")?></a>                                                
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        <!--</div>-->
                    </div>                
                    <div id="tabs1-alarm" style="font-size: 12px">     
                        <div class="div_fieldcontent" style="width: 480px;position: relative">
                            <label><?php echo $lang->line("langAdminMain.System_Alarm_Status").' : '?></label>                    
                            <label id="label_alarm_status"></label>
                                <div style="height:10px"></div>
                            <div style="position: absolute;left: 72px">
                                <a id="btn_enable_alarm" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Service_Enable")?></a>
                                <a id="btn_disable_alarm" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Service_Disable")?></a>
                            </div>
                        </div>
                    </div>
                    <div id="tabs1-system-update">                                            
                        <div id="div_upgrade_control">                           
                            <div class="div_fieldcontent" style="font-size: 12px">

                                <div style="position: absolute; left: 40px; top: -30px;" >
                                    <div style="position: relative;bottom: 3px;">
                                        <input type="radio" id="radio-online" name="radio-update"  class="iCheckUpDate" value="online" checked>
                                        <label class="LabelHead" style="position: relative;font-size: 18px;cursor:pointer;top: 2px;font-size: 12px;" for="radio-online"><?php echo $lang->line("langAdminMain.Online_update_Str")?></label>
                                    </div>
                                </div>
                                <div style="position: absolute; left: 160px; top: -30px;width: 250px;">
                                    <div style="position: relative;bottom: 3px;">
                                        <input type="radio" id="radio-local" name="radio-update"  class="iCheckUpDate" value="local" style="position: relative;top: 10px;left: 40px;" >
                                        <label class="LabelHead" style="position: relative;font-size: 18px;cursor:pointer;top: 2px;font-size: 12px;" for="radio-local"><?php echo $lang->line("langAdminMain.Local_update_Str")?></label>
                                    </div>
                                </div>

                                <!-- 新式樣修改(藍黃)增加上次更新時間 2016.12.13 william -->
                                <div id="Div_online_page" style="margin-top: 10px;">
                                    <label class="LabelVersionHead" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'width: 100px;';else echo 'width: 150px;';?>"><?php echo $lang->line("langAdminMain.Update_Last")?></label>
                                    <label class="txt_last"></label>
                                    <br>
                                    <br>
                                    <label class="LabelVersionHead" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'width: 100px;';else echo 'width: 150px;';?>"><?php echo $lang->line("langAdminMain.Update_CurrentVer")?></label>
                                    <label class="txt_now_firmware"></label>
                                    <br>
                                    <br>
                                    <label class="LabelVersionHead" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'width: 100px;';else echo 'width: 150px;';?>"><?php echo $lang->line("langAdminMain.Update_NewVer")?></label>
                                    <label id="txt_new_firmware" style="color:blue"></label>                            
                                    <br>            
                                    <br>
                                    <a id="btn_check_firmware" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Check_New_Firmware")?></a>
                                    <a id="btn_update_firmware" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Update")?></a> 
                                </div>
                                <!--<div style="width:500px;height:10px;border-bottom: dashed 1px rgba(0, 0, 0, .5)"></div>-->
                                <div id="Div_local_page" style="margin-top: 10px;display: none;">
                                    <label class="LabelVersionHead" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'width: 100px;';else echo 'width: 150px;';?>"><?php echo $lang->line("langAdminMain.Update_Last")?></label>
                                    <label class="txt_last"></label>
                                    <br>
                                    <br>
                                    <label class="LabelVersionHead" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'width: 100px;';else echo 'width: 150px;';?>"><?php echo $lang->line("langAdminMain.Update_CurrentVer")?></label>
                                    <label class="txt_now_firmware"></label>
                                    <br>
                                    <br>
                                    <form id="Form_UploadUpdateFile" target="acrored_update" action="upload_updatefile.php"  method="POST" enctype="multipart/form-data">
                                        <div>
                                            <input type="hidden" name="<?php echo $sess_name ?>" value="<?php echo $sess_val; ?>" />    
                                            <a id="upfile1" class="btn-jquery large-btn" style="width: 120px;"><?php echo $lang->line("langAdminMain.Choose_File_Str") ?></a>                                
                                            <input id = "FwUploadSelector" type="file" name="updatefile" style="width: 300px;display:none" />   
                                            <lable id="UploadFileName"><?php echo $lang->line("langAdminMain.No_file_chosen_Str") ?></lable>                                        
                                        </div>
                                        <div style=" position: relative; top: 65px; ">
                                            <input id = "FwUploadSubmit" type="submit" class="btn-jquery large-btn" value="<?php echo $lang->line("langAdminMain.Btn_Update") ?>" />
                                        </div>
                                    </form>    
                                    <iframe name="acrored_update" style="display:none;"></iframe>
                                    <br>                                                                                            
                                    <div style=" position: relative; bottom: 30px; ">
                                        <label class="LabelVersionHead" tyle="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'width: 100px;';else echo 'width: 150px;';?>"><?php echo $lang->line("langAdminMain.List_Upload_Progress") ?></label>
                                        <div id="progressbar" style="position: relative;left: 0px; top: 5px;">
                                            <div class="progress-label"></div>
                                        </div>                                          
                                    </div>
                                </div>                                   
                            </div>
                        </div>              
                    </div> 
                    <div id="tabs1-ad" style="font-size: 12px"> <!-- 新增AD功能設定  2017.09.06 william -->     
                        <div class="div_fieldcontent" style="width: 480px;position: relative">

                            <label class="LabelHead"><?php echo $lang->line("langAdminMain.AD_Server_IP")?></label>                    
                            <input id="AD_ServerIP" class="InputText" value="" type="text">
                            <br>    
                            <br>
                            <label class="LabelHead"><?php echo $lang->line("langAdminMain.AD_DomainName")?></label>                    
                            <input id="AD_DomainName" class="InputText"  value="" type="text">
                            <br>
                            <br>
                            <label class="LabelHead"><?php echo $lang->line("langAdminMain.AD_Account")?></label>                    
                            <input id="AD_Account" class="InputText"  value="" type="text">                    
                            <br>
                            <br>
                            <label class="LabelHead"><?php echo $lang->line("langAdminMain.AD_Password")?></label>                    
                            <input id="AD_Password" class="InputText"  value="" type="password">                    
                            <br>
                            <br>          
                            <label class="LabelHead"><?php echo $lang->line("langAdminMain.AD_Enable")?></label>                 
                            <input id="AD_Enable_checkBox" type="checkbox" style="width: 20px;height: 20px;position: relative;right: 4px;">
                            <div style="position: absolute;right: 10px;margin-top: 5px;">
                                <a id="btn_ad_confirm" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
                                <a id="btn_ad_cancel" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
                            </div>


                        </div>
                    </div>
                    <div id="tabs1-ups" style="font-size: 12px">
                        <div class="div_fieldcontent" style="width: 480px;position: relative">     
                            <label class="LabelHead"><?php echo $lang->line("langAdminMain.Str_Type")?> : </label>                    
                            <div style="position: relative; left: 40px; top: -15px;" >
                                <div style="position: relative;bottom: 3px;">
                                    <input type="radio" id="radio-SNMP" name="radio-Ups"  class="iCheckRadioUps" value="SNMP" checked>
                                    <label class="LabelHead" style="position: relative;font-size: 18px;cursor:pointer;font-size: 12px;" for="radio-SNMP">SNMP</label>
                                </div>
                            </div>                        
                            <label class="LabelHead"><?php echo $lang->line("langAdminMain.UPS_Server_IP")?> : </label>                    
                            <input id="input_UPSIP" class="InputText" value="" type="text" style="width: 190px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo '';else echo 'position: relative;left: 90px;';?>">
                            <br>   
                            <br>                 
                            <div style="height:40px">
                                <div style="position:relative">
                                    <label style="position:absolute;top:7px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo '';else echo 'width: 300px;';?>" class="LabelHead"><?php echo $lang->line("langAdminMain.Str_UPS_DelayTime")?> : </label>
                                    <div style="position:absolute;width: 200px;top:0px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'left:210px;';else echo 'left:300px;';?>">
                                        <select id="combo_ups_delaytime">
                                            <option value="30">30 <?php echo $lang->line("langAdminMain.Str_Seconds")?></option>
                                            <option value="60">60 <?php echo $lang->line("langAdminMain.Str_Seconds")?></option>
                                            <option value="120">120 <?php echo $lang->line("langAdminMain.Str_Seconds")?></option>
                                            <option value="240">240 <?php echo $lang->line("langAdminMain.Str_Seconds")?></option>
                                            <option value="300">300 <?php echo $lang->line("langAdminMain.Str_Seconds")?></option>
                                            <option value="600">600 <?php echo $lang->line("langAdminMain.Str_Seconds")?></option>
                                        </select>
                                    </div>
                                </div>                
                            </div>
                            <label class="LabelHead" style="position: absolute;"><?php echo $lang->line("langAdminMain.UPS_Battery_Status")?> : </label>    
                            <div id="UPSBatteryStatus" style="font-size: 16px;width: 190px;position: absolute;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'left: 215px;';else echo 'left: 305px;';?>"></div>                
                            <br>    
                            <br> 
                            <label class="LabelHead" style="position: absolute;"><?php echo $lang->line("langAdminMain.UPS_Battery_Time")?> : </label>                    
                            <div id="UPSBatteryTime" style="font-size: 16px;width: 190px;position: absolute;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'left: 215px;';else echo 'left: 305px;';?>"></div>                
                            <br>    
                            <br>
                            <label class="LabelHead" style="position: absolute;"><?php echo $lang->line("langAdminMain.UPS_Battery_ChargeCondition")?> : </label>    
                            <div id="UPSChargeCondition" style="font-size: 16px;width: 190px;position: absolute;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'left: 215px;';else echo 'left: 305px;';?>"></div>                                
                            <br>    
                            <br>
                            <div style="position:absolute;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'right: 70px;';else echo 'right: -20px;';?>">                                                              
                                <a id='btn_set_ups'class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
                                <a id='btn_reset_ups' class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>                                    
                            </div>
                        </div>
                    </div>
                    <div id="tabs1-email" style="font-size: 12px">
                        <div class="div_fieldcontent" style="width: 480px;position: relative">
                            <input id="Email_Enable_checkBox" type="checkbox" style="width: 20px;height: 20px;position: relative;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'right: -128px;';else echo 'right: -178px;';?>"><label class="LabelHead" style=" bottom: 5px; <?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'right: -130px;';else echo 'right: -180px;';?> position: relative; "><?php echo $lang->line("langAdminMain.Service_Enable")?></label>
                            <div style="height:5px"></div>      
                            <div class='field'>
                                <label class="LabelHead"><?php echo $lang->line("langAdminMain.H4_SMTP_ServerIP")?> : </label>                    
                                <input id="input_SMTPIP" class="InputText" value="" type="text" style="width: 190px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'position: relative;right: 81px;';else echo 'position: relative;left: -30px;';?>">
                                <label class="LabelHead" style="color: red;position: relative;width: 10px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'right: 70px;';else echo 'right: 15px;';?>">*</label>
                            </div>
                            <div style="height:5px"></div> 
                            <div class='field'>
                                <label class="LabelHead"><?php echo $lang->line("langAdminMain.H4_SMTP_ServerPort")?> : </label>                    
                                <input id="input_SMTPPort" class="InputText" value="" type="text" style="width: 190px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'position: relative;right: 81px;';else echo 'position: relative;left: -30px;';?>">
                                <label class="LabelHead" style="color: red;position: relative;width: 10px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'right: 70px;';else echo 'right: 15px;';?>">*</label>
                            </div>
                            <div style="height:5px"></div>     
                            <div class='field'>
                                <label class="LabelHead"><?php echo $lang->line("langAdminMain.Email_User")?> : </label>                    
                                <input id="input_EmailUser" class="InputText" value="" type="text" style="width: 190px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'position: relative;right: 81px;';else echo 'position: relative;left: -30px;';?>">
                                <label class="LabelHead" style="color: red;position: relative;width: 10px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'right: 70px;';else echo 'right: 15px;';?>">*</label>
                            </div>
                            <div style="height:5px"></div> 
                            <div class='field'>                       
                                <label class="LabelHead"><?php echo $lang->line("langAdminMain.Email_Pwd")?> : </label>                    
                                <input id="input_EmailPwd" class="InputText" value="" type="password" style="width: 190px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'position: relative;right: 81px;';else echo 'position: relative;left: -30px;';?>">
                                <label class="LabelHead" style="color: red;position: relative;width: 10px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'right: 70px;';else echo 'right: 15px;';?>">*</label>
                            </div>
                            <div style="height:5px"></div>                         
                            <label class="LabelHead"><?php echo $lang->line("langAdminMain.Email_Sender")?> : </label>                    
                            <input id="input_EmailSender" class="InputText" value="" type="text" style="width: 190px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'position: relative;right: 81px;';else echo 'position: relative;left: -30px;';?>">
                            <div style="height:5px"></div>                         
                            <div class='field'>
                                <label class="LabelHead"><?php echo $lang->line("langAdminMain.Email_Receiver")?>1 : </label>                    
                                <input id="input_EmailReceiver1" class="InputText" value="" type="text" style="width: 190px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'position: relative;right: 81px;';else echo 'position: relative;left: -30px;';?>">
                                <label class="LabelHead" style="color: red;position: relative;width: 10px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'right: 70px;';else echo 'right: 15px;';?>">*</label>
                            </div>
                            <div style="height:5px"></div>                         
                            <label class="LabelHead"><?php echo $lang->line("langAdminMain.Email_Receiver")?>2 : </label>                    
                            <input id="input_EmailReceiver2" class="InputText" value="" type="text" style="width: 190px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'position: relative;right: 81px;';else echo 'position: relative;left: -30px;';?>">
                            <div style="height:5px"></div>                         
                            <label class="LabelHead"><?php echo $lang->line("langAdminMain.Email_Receiver")?>3 : </label>                    
                            <input id="input_EmailReceiver3" class="InputText" value="" type="text" style="width: 190px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'position: relative;right: 81px;';else echo 'position: relative;left: -30px;';?>">
                            <div style="height:5px"></div>                         
                            <label class="LabelHead"><?php echo $lang->line("langAdminMain.Email_Receiver")?>4 : </label>                    
                            <input id="input_EmailReceiver4" class="InputText" value="" type="text" style="width: 190px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'position: relative;right: 81px;';else echo 'position: relative;left: -30px;';?>">
                            <div style="height:5px"></div>                         
                            <label class="LabelHead"><?php echo $lang->line("langAdminMain.Email_Receiver")?>5 : </label>                    
                            <input id="input_EmailReceiver5" class="InputText" value="" type="text" style="width: 190px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'position: relative;right: 81px;';else echo 'position: relative;left: -30px;';?>">
                            <div style="height:5px"></div>                                                     
                            <label class="LabelHead"><?php echo $lang->line("langAdminMain.H4_SMTP_EnableSecurityConn")?> : </label>             
                            <input id="Email_TLS_checkBox" type="checkbox" style="width: 20px;height: 20px;position: relative;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'right: 85px;';else echo 'right: 34px;';?>top: 5px;">
                            <div style="height:5px"></div>  
                            <label class="LabelHead" style="color: red;"><?php echo $lang->line("langAdminMain.MustField")?></label>             
                            <div style="height:10px"></div>                                                     
                            <div style="position:absolute;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'right: 150px;';else echo 'right: 100px;';?>">                                                              
                                <a id='btn_set_email'class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
                                <a id='btn_test_email'class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Test")?></a>
                                <a id='btn_reset_email' class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>                                    
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>             
    </div>
             
    <!--
        ++++++++++++++++++++++++++++++
        +		Storage Manager		 +
        ++++++++++++++++++++++++++++++
    -->
    <div id = "Div_LayoutZone_SysCfg_Storage" class = "Div_Design_Layout" >        
        <div id = "Div_SysCfg_Storage" class = "Div_MainContain" >
            <div id="tab-storage-container" class='tab-container' style="margin: 20px">
                <ul class='etabs'>                    
                    <li class='tab'><a href="#tabs1-disk"><?php echo $lang->line("langAdminMain.H5_SubTitle_SysMgr_Disk")?></a></li>                    
                    <li class='tab'><a href="#tabs1-raid"><?php echo $lang->line("langAdminMain.H5_SubTitle_SysMgr_Raid")?></a></li>
                    <li class='tab'<?php if(!$iSCSI){echo ' style="display:none"'; } ?>><a href="#tabs1-iscsi"><?php echo $lang->line("langAdminMain.Storage_iSCSI")?></a></li>
                </ul>
                <div class='panel-container'> 
                    <div id="tabs1-disk"> 
                        <!-- 修改磁碟管理顯示 2017.02.24 william -->

                            <div class="BtnRowHeader" style="width:99%"> 
                                <div style="position: relative">
                                    <a id='btn_refresh_disk' class="btn-jquery large-btn" style="position: absolute;right: -5px"><?php echo $lang->line("langAdminMain.Btn_Refresh")?></a>
                                </div>    
                            </div>       
                        <!-- 畫面修改 2017.03.03 william -->
                        <div id="div_disk_scroll" style="overflow-x: auto;overflow-y: hidden;width: 100%;margin-top: 6px;">
  
                            <div style="height: 5px"></div>
                            <div style="position: relative;"> 

                              <div style="position: relative;width: 99%;margin-left: auto;margin-right: auto;">  

                                <!--  Div - Header -->
                                <div id="DiskRowHeader" class = "Div_DiskRowHeader RowHeader" style="font-size: 14px">
                                    <div class = "Div_RelativeLayout" style="overflow: hidden;">
                                        <div id="DivDisk_RowHeader_inner"  style="height: 100%;width: 100%;overflow-x: auto;overflow-y: hidden;position: absolute;padding-bottom: 30px;top: 0px;">
                                            <div class = "DiskList_ItemNo AbsoluteLayout RowField DiskHeader" >
                                                <div class="div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.VMList_SN")?></H4> 
                                                </div>
                                            </div>                                              
                                            <div id="postion" class = "DiskList_Slot AbsoluteLayout RowField DiskHeader" >
                                                <div class="div_RowHeader adjust_Disk_div_RowHeader">
                                                    <H4 class="H4_RowHeader" id="DiskList_Slot_1"><?php echo $lang->line("langAdminMain.DiskList_Slot")?>&nbsp;</H4> <!-- 新式樣修改(藍黃) 2016.12.13 william -->
                                                    <label id="postion1" class="label_disk_arrow label_RowHeader">&#9650;</label>
                                                </div>
                                            </div>
                                            <div id="status" class = "DiskList_Status AbsoluteLayout RowField DiskHeader">
                                                <div class="div_RowHeader adjust_Disk_div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.DiskList_Status")?>&nbsp;</H4>                        
                                                    <label id="status1" class="label_disk_arrow label_RowHeader"></label>
                                                </div>
                                            </div>
                                            <div id="capacity" class = "DisktList_Capacity AbsoluteLayout RowField DiskHeader">
                                                <div class="div_RowHeader adjust_Disk_div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.DisktList_Capacity")?>&nbsp;</H4>
                                                    <label id="capacity1" class="label_disk_arrow label_RowHeader"></label>
                                                </div>
                                            </div>
                                            <div id="vendor" class = "DiskList_Vendor AbsoluteLayout RowField DiskHeader" >
                                                <div class="div_RowHeader adjust_Disk_div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.DiskList_Vendor")?>&nbsp;</H4>
                                                    <label id="vendor1" class="label_disk_arrow label_RowHeader"></label>
                                                </div>
                                            </div>
                                            <div id="if_type" class = "DiskList_Speed AbsoluteLayout RowField DiskHeader">
                                                <div class="div_RowHeader adjust_Disk_div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.DiskList_Speed")?>&nbsp;</H4>
                                                    <label id="if_type1" class="label_disk_arrow label_RowHeader"></label>
                                                </div>
                                            </div>
                                            <div id="model" class = "DiskList_Model AbsoluteLayout RowField DiskHeader" >
                                                <div class="div_RowHeader adjust_Disk_div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.DiskList_Model")?>&nbsp;</H4>
                                                    <label id="model1" class="label_disk_arrow label_RowHeader"></label>
                                                </div>
                                            </div>
                                            <div id="rev" class = "DiskList_FirmwareVer AbsoluteLayout RowField DiskHeader"> 
                                                <div class="div_RowHeader adjust_Disk_div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.DiskList_FirmwareVer")?>&nbsp;</H4>
                                                    <label id="rev1" class="label_disk_arrow label_RowHeader"></label>
                                                </div>
                                            </div>
                                            <H4 class = "DiskList_BtnInitial AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.List_Action")?></H4>   
                                        </div>                                                  
                                    </div> 
                                </div>
                                <!--  Div - List  -->
                                <div id = "Div_DiskList" style="font-size: 12px;" onscroll="document.getElementById('DivDisk_RowHeader_inner').scrollLeft = this.scrollLeft;">

                                </div>

                            </div>

                            </div>
                        </div>
                    </div>
                    <div id="tabs1-raid">     
                        <div id="div_raid_scroll" style="overflow-x: auto;overflow-y: hidden;">
                            <div class="BtnRowHeader">
                                <div style="position: relative;width: 100%;">
                                    <label id="label_alert_unimportable"><?php echo $lang->line("langAdminMain.Str_alert_unimportable")?></label>
                                    <!-- <a id='btn_create_raid_dialog' class="btn-jquery large-btn" ><?php //echo $lang->line("langAdminMain.H4_BtnRaidCreate")?></a> -->
                                    <a id='btn_refresh_raid' class="btn-jquery large-btn" style="position: absolute;right: -5px"><?php echo $lang->line("langAdminMain.Btn_Refresh")?></a>
                                </div>    
                            </div>        
                            <div style="height: 5px"></div>
                            <!--  Div - Header -->
                            <div style="position: relative;width: 99%;margin-left: auto;margin-right: auto;margin-top:6px;"> 
                                <div id="RAIDRowHeader" class = "Div_RAIDRowHeader RowHeader" style="width: 100%;font-size: 14px">
                                    <div class = "Div_RelativeLayout" style="overflow: hidden;"> <!-- 2017.11.17 william RAID管理畫面修改 -->
                                        <div id="DivRAID_RowHeader_inner"  style="height: 100%;width: 100%;overflow-x: auto;overflow-y: auto;position: absolute;padding-bottom: 30px;top: 0px;"> <!-- 2017.11.17 william RAID管理畫面修改 -->
                                            <!--RAID Sorting-->
                                            <div class = "RAIDList_ItemNo AbsoluteLayout RowField RAIDHeader" >
                                                <div class="div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.VMList_SN")?></H4> 
                                                </div>
                                            </div>                                                                                          
                                            <div id="RAIDID" class = "RAIDList_RAIDID AbsoluteLayout RowField RAIDHeader asc" >
                                                <div class="div_RowHeader adjust_raid_div_RowHeader">
                                                    <H4 class="H4_RowHeader">RAID ID&nbsp;</H4> 
                                                    <label id="RAIDID1" class="label_raid_arrow label_RowHeader">&#9650;</label>
                                                </div>
                                            </div>                                              
                                            <div id="Level" class = "RAIDList_Level AbsoluteLayout RowField RAIDHeader" >
                                                <div class="div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.RAID_RAIDLevel")?>&nbsp;</H4> 
                                                    <label id="Level1" class="label_raid_arrow label_RowHeader"></label>
                                                </div>
                                            </div>                         
                                            <div id="Allocate" class = "RAIDList_VMAssign_Capacity AbsoluteLayout RowField RAIDHeader" >
                                                <div class="div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $VMorVDIBack.$lang->line("langAdminMain.RAID_Assign_Capacity")?>&nbsp;</H4> 
                                                    <label id="Allocate1" class="label_raid_arrow label_RowHeader"></label>
                                                </div>
                                            </div>        
                                            <div id="Used" class = "RAIDList_FreeCapacity AbsoluteLayout RowField RAIDHeader" >
                                                <div class="div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.RAID_UsedCapacity")?>&nbsp;</H4> 
                                                    <label id="Used1" class="label_raid_arrow label_RowHeader"></label>
                                                </div>
                                            </div>                      
                                            <div id="Total" class = "RAIDList_Capacity AbsoluteLayout RowField RAIDHeader" >
                                                <div class="div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.RAID_Capacity")?>&nbsp;</H4> 
                                                    <label id="Total1" class="label_raid_arrow label_RowHeader"></label>
                                                </div>
                                            </div>                                                                                                                                  
                                            <div id="Status" class = "RAIDList_Status AbsoluteLayout RowField RAIDHeader" >
                                                <div class="div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.RAID_Status")?>&nbsp;</H4> 
                                                    <label id="Status1" class="label_raid_arrow label_RowHeader"></label>
                                                </div>
                                            </div>   
                                            <div class = "RAIDList_Action AbsoluteLayout RowField RAIDHeader" >
                                                <div class="div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.List_Action")?></H4> 
                                                </div>
                                            </div>                                               
                                        
                                        
                                        <!--<H4 class = "RAIDList_ItemNo AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.VMList_SN")?></H4>
                                            <H4 class = "RAIDList_RAIDID AbsoluteLayout RowField" >RAID ID</H4>
                                        <H4 class = "RAIDList_Level AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.RAID_RAIDLevel")?></H4>
                                        <H4 class = "RAIDList_VMAssign_Capacity AbsoluteLayout RowField" ><?php echo $VMorVDIBack.$lang->line("langAdminMain.RAID_Assign_Capacity")?></H4>
                                        <H4 class = "RAIDList_FreeCapacity AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.RAID_UsedCapacity")?></H4>                                                                
                                        <H4 class = "RAIDList_Capacity AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.RAID_Capacity")?></H4>                                        -->
                                       <!--  <H4 class = "RAIDList_TP AbsoluteLayout RowField" ><?php //echo $lang->line("langAdminMain.RAID_TP")?></H4> -->
                                        <!--<H4 class = "RAIDList_Status AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.RAID_Status")?></H4>                                                                            
                                        <H4 class = "RAIDList_Action AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.List_Action")?></H4>                                                                            -->
                                    </div>
                                </div>
                                </div>
                                <!--  Div - List  -->
                                <div id = "Div_RAIDList" style="font-size:12px" onscroll="document.getElementById('DivRAID_RowHeader_inner').scrollLeft = this.scrollLeft;"> <!-- 2017.11.17 william RAID管理畫面修改 -->

                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="tabs1-iscsi" <?php if(!$iSCSI){echo ' style="display:none"';} ?>>
                        <div id="div_iscsi_scroll" style="overflow-x: auto;overflow-y: hidden">
                            <div class="BtnRowHeader">
                                <div style="position: relative">
                                    <a id='btn_create_iscsi_dialog' href="#" class="btn-jquery large-btn" ><?php echo $lang->line("langAdminMain.H4_BtnRaidCreate")?></a>                                
                                    <a id='btn_refresh_iscsi' href="#" class="btn-jquery large-btn" style="position: absolute;right: 0px"><?php echo $lang->line("langAdminMain.Btn_Refresh")?></a>
                                </div>    
                            </div>
                            <div style="height: 5px"></div>
                            <div style="position: relative;"> 
                                <div class = "Div_iSCSIRowHeader RowHeader" style="width: 850px;font-size: 14px">
                                    <div class = "Div_RelativeLayout" >
                                        <H4 class = "iSCSIList_Name AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.VMList_Name")?></H4>
                                        <H4 class = "iSCSIList_IP AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.Str_Connect_Ip")?></H4>                                    
                                        <H4 class = "iSCSIList_Action AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.List_Action")?></H4>                                                                            
                                    </div>
                                </div>
                                <!--Div - List -->
                                <div id = "Div_iSCISList" style="font-size:12px">

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>       

    <!--
        ++++++++++++++++++++++++++++++
        +		Service		 +
        ++++++++++++++++++++++++++++++
    -->    
    <div id = "Div_LayoutZone_SysCfg_Service" class = "Div_Design_Layout" >        
        <div id = "Div_SysCfg_Service" class = "Div_MainContain" >
            <div id="tab-service-container" class='tab-container' style="margin: 20px">
                <ul class='etabs'>                    
                    <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                    <li class='tab'><a href="#tabs1-service_ScheduleSnapshot"><?php echo $lang->line("langAdminMain.Service_Snapshot")?></a></li> 
                    <li class='tab'><a href="#tabs1-service_Backup"><?php echo $lang->line("langAdminMain.Service_Backup")?></a></li>                                 
                </ul>
                <div class='panel-container'>                    
                    <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                    <!--  快照功能實作 2017.02.08  -->
                    <div id="tabs1-service_ScheduleSnapshot" style="font-size: 12px">
                        <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                        <div class = "Div_RelativeLayout" >                            
                                <div style="width:99%;height: 30px;display: block;margin-left: auto;margin-right: auto"> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                                    <div style="position: relative">                                    
                                        <a id='btn_ss_create_schedule' class="btn-jquery large-btn" <?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'style="width: 115px;top: -3px"';else echo 'style="width: 205px;top: -3px"';?>><?php echo $lang->line("langAdminMain.Create_Schedule")?></a>                           
                                        <div style="position: absolute;right: 0px;top:0px">
                                            <a id='btn_scrule_all_vm' class="btn-jquery btn_sc_select_operation large-btn" ><?php echo $lang->line("langAdminMain.ScheduleRule")?></a>
                                            <a id='btn_scjobs_all_vm' class="btn-jquery btn_sc_select_operation large-btn" ><?php echo $lang->line("langAdminMain.ScheduleJobs")?></a>                                                                                       
                                            <a id='btn_scdelete_all_vm' class="btn-jquery btn_sc_select_operation large-btn" ><?php echo $lang->line("langAdminMain.ScheduleDelete")?></a>                                      
                            </div>
                                    </div>
                                </div> 
                                <div style="height:6px"></div>
                                    <div id="div_SnapShotSchedule_scroll" style="margin-top:6px;"> <!-- 取消style="overflow-x: auto;overflow-y: auto"; 2017.03.06 william -->
                                        <div id='Div_VM' class="Div_VM_Shot" style="width: 99%;">                            
                                            <div id="VMRowHeader" class = "Div_VMRow VM_RowHeader" style="width: 100%;height: 32px;padding-top: 3px;border-top-left-radius: 0px;border-top-right-radius: 0px;overflow: hidden;position: relative;">
                                                <div id="DivSnapShotSchedule_RowHeader_inner"  style="height: 100%;width: 100%;overflow-x: auto;overflow-y: hidden;position: absolute;padding-bottom: 30px;top: 0px;">
                                                    <div class = "VMList_Task_ItemNo AbsoluteLayout RowField">
                                                        <div class="div_RowHeader">
                                                            <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.VMList_SN")?></H4>
                                                        </div>
                                                    </div>
                                                    <div class="Schedule_Snapshot_Select AbsoluteLayout RowField">
                                                        <div style="position:relative">
                                                            <input type="checkbox" class="iCheckBoxAllScheduleSelect">
                                                        </div>
                                                    </div>                                    
                                                    <div id="ScheduleName" class = "Schedule_Snapshot_Name AbsoluteLayout RowField ScheduleHeader">
                                                        <div class="div_RowHeader">
                                                            <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.Schedule_Name")?>&nbsp;</H4>
                                                            <label id="autossScheduleName1" class="label_autoss_arrow label_RowHeader"></label>
                                                        </div>
                                                    </div>                                       
                                                    <div id="ScheduleDesc" class = "Schedule_Snapshot_Desc AbsoluteLayout RowField ScheduleHeader">
                                                        <div class="div_RowHeader">
                                                            <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.Schedule_Desc")?>&nbsp;</H4>                                    
                                                            <label id="autossScheduleDesc1" class="label_autoss_arrow label_RowHeader"></label>
                                                        </div>
                                                    </div>    
                                                    <div id="ScheduleCreateTime" class = "Schedule_Snapshot_Time AbsoluteLayout RowField ScheduleHeader">
                                                        <div class="div_RowHeader">
                                                            <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.Schedule_Time")?>&nbsp;</H4>                                    
                                                            <label id="autossScheduleCreateTime1" class="label_autoss_arrow label_RowHeader"></label>
                                                        </div>
                                                    </div>                                                   
                                                    <div id="ScheduleAction" class = "Schedule_Snapshot_Action AbsoluteLayout RowField">
                                                        <div class="div_RowHeader">
                                                            <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.Schedule_Action")?>&nbsp;</H4>
                                                        </div>
                                                    </div>       
                                                </div>                                                                                                                                                                 
                                            </div>           
                                            <!--  SnapshotSchedule - List  -->
                                            <div id = "Div_Service_Snapshot_List" style="font-size: 12px; width: 100%; overflow-y: auto;" onscroll="document.getElementById('DivSnapShotSchedule_RowHeader_inner').scrollLeft = this.scrollLeft;">
                                            </div> 
                                        </div>
                                    </div>
                                    </div>
                                </div>
                    <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                    <div id="tabs1-service_Backup">
                        <div id="tab-Backup-container" class='tab-container'>
                            <ul class='etabs'>                    
                                <li class='tab'><a href="#tabs1-Backup_Setting"><?php echo $lang->line("langAdminMain.Backup_Setting")?></a></li>
                                <li class='tab'><a href="#tabs1-Backup_Manager"><?php echo $lang->line("langAdminMain.Backup_Manager")?></a></li>
                                <li class='tab'><a href="#tabs1-Backup_schedule"><?php echo $lang->line("langAdminMain.Backup_schedule")?></a></li>                               
                                <li class='tab'><a href="#tabs1-bktask-list"><?php echo $lang->line("langAdminMain.Backup_Task")?></a></li>                   
                            </ul>
                            <div class='panel-container'>
                            <!-- 備份儲存設定  2017.01.11 william Start*/ -->
                                <div id="tabs1-Backup_Setting">
                                    <div class="CheckBackupLUN_wrapper">
                                        <div class="BKLogin">
                                            <div class="LoginContent">
                                                <fieldset class="Backup_Content1">
                                                    <!--<legend>Login:</legend>-->
                                                    <form id="bkformlogin" method="post" name="login_form_">
                                                        <div>
                                                            <div style="position: relative;top:5px;margin-top: 0px; margin-bottom: 10px;">
                                                                <label class="Backup_Content_L1"><?php echo $lang->line("langAdminMain.Category")?> :</label>

                                                                <div style="position: absolute; left: 130px; top: 10px;" >
                                                                    <div style="position: relative;bottom: 3px;">
                                                                        <input type="radio" id="radio-iSCSI" name="radio-Storage"  class="iCheckRadioStorage" value="iSCSI" checked>
                                                                        <label class="LabelHead" style="position: relative;font-size: 18px;cursor:pointer" for="radio-iSCSI"><?php echo $lang->line("langAdminMain.iSCSI_Storage_Str")?></label>
                                                                    </div>
                                                                </div>
                                                                <div style="position: absolute; left: 280px; top: 10px;width: 250px;">
                                                                    <div style="position: relative;bottom: 3px;">
                                                                        <input type="radio" id="radio-nas" name="radio-Storage"  class="iCheckRadioStorage" value="nas" style="position: relative;top: 10px;left: 40px;" >
                                                                        <label class="LabelHead" style="position: relative;font-size: 18px;cursor:pointer" for="radio-nas"><?php echo $lang->line("langAdminMain.NAS_Storage_Str")?></label>
                                                                    </div>
                                                                </div>




                                                                <!--<input type="radio" id="iSCSI" style="position: relative;top: 15px; left: 15px;" checked>
                                                                <label for="iSCSI" style="position: relative;top: 13px; left: 15px;font-size: 16px;">iSCSI</label>-->
                                                            </div>
                                                            <div id="Div_iSCSI_page">
                                                                <div style="position: relative;top:5px;">
                                                                    <label class="Backup_Content_L1"><?php echo $lang->line("langAdminMain.StorageIP_Str")?> :</label><input type="text" id="backup_iscsiip" class="BackupInfo_Content1_input1"><br>
                                                                    <label class="Backup_Content_L1"><?php echo $lang->line("langAdminMain.Backup_TargetName")?> :</label><input type="text" id="backup_targetname" class="BackupInfo_Content1_input1"><br>
                                                                    <img id="chk_bkchap" class="Img_PC_ChkBxSaveKey bkchap" src="img/checkbox_empty.png"></img>
                                                                    <label id="label_bkchap" class="label_bkchap Backup_Content_L1" for="chk_bkchap" style="top: 0px;left: 134px;text-align: left;width: 40px;">CHAP</label><br>
                                                                </div>
                                                                <div style="position: relative;top:5px;">
                                                                    <label name="bktext" class="Backup_Content_L1"><?php echo $lang->line("langAdminMain.Backup_Username")?> :</label><input type="text" id="backup_usename" class="BackupInfo_Content1_input1"><br>
                                                                    <label name="bktext" class="Backup_Content_L1"><?php echo $lang->line("langAdminMain.Backup_Password")?> :</label><input type="password" id="backup_password" class="BackupInfo_Content1_input1"><br>
                                                                    <label class="Backup_Content_L1"><?php echo $lang->line("langAdminMain.Backup_Desc")?> :</label><label id="checkLUNdesc_text" class="Backup_Content_L1" style="width: 312px;text-align: left;margin-left: 0px;"></label><br>
                                                                    <label class="Backup_Content_L1" style="top:10px;margin-top: 15px;"><?php echo $lang->line("langAdminMain.Backup_online_Status")?> :</label><label id="LUNOnlineCheck" class="Backup_Content_L1" style="width: 312px;text-align: left;margin-left: 0px;top:10px"></label>
                                                                    <a id='btn_checkLUN_Connect' class="btn-jquery large-btn" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'left: 274px;top: 155px;';else echo 'left: 210px;top: 155px;';?> position: absolute;"><?php echo $lang->line("langAdminMain.Backup_ConnectCheck")?></a>
                                                                    <a id="Backup_Disconnect_btn" class="btn-jquery large-btn" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'left: 274px;top: 155px;';else echo 'left: 190px;top: 155px;';?> position: absolute;"><?php echo $lang->line("langAdminMain.Backup_Disconnect")?></a>
                                                                    <a id='clearallbk' class="btn-jquery large-btn" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'top: 155px;left: 361px;';else echo 'top: 155px;left: 311px;';?>position: absolute;"><?php echo $lang->line("langAdminMain.Backup_ClearBK")?></a>
                                                                </div>                                                            
                                                            </div>
                                                            <div id="Div_Nas_page" style="display: none;">
                                                                <div style="position: relative;top:5px;">
                                                                    <label class="Backup_Content_L1">NAS URL :</label><input type="text" id="backup_nasurl" class="BackupInfo_Content1_input1"><br>
                                                                </div>
                                                                <div style="position: relative;top:5px;">
                                                                    <label class="Backup_Content_L1"><?php echo $lang->line("langAdminMain.Backup_Username")?> :</label><input type="text" id="backup_nas_usename" class="BackupInfo_Content1_input1"><br>
                                                                    <label class="Backup_Content_L1"><?php echo $lang->line("langAdminMain.Backup_Password")?> :</label><input type="password" id="backup_nas_password" class="BackupInfo_Content1_input1"><br>
                                                                    <label class="Backup_Content_L1">Port :</label><input type="text" id="nas_port_input" value="445" class="BackupInfo_Content1_input1"><br>
                                                                    <label class="Backup_Content_L1"><?php echo $lang->line("langAdminMain.Backup_Desc")?> :</label><label id="checkLUNdesc_nas_text" class="Backup_Content_L1" style="width: 312px;text-align: left;margin-left: 0px;"></label><br>
                                                                    <label class="Backup_Content_L1" style="top:10px;margin-top: 15px;"><?php echo $lang->line("langAdminMain.Backup_online_Status")?> :</label><label id="nas_status" class="Backup_Content_L1" style="width: 312px;text-align: left;margin-left: 0px;top:10px"></label>
                                                                    <a id='btn_nas_Connect' class="btn-jquery large-btn" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'left: 274px;top: 235px;';else echo 'left: 210px;top: 235px;';?> position: absolute;"><?php echo $lang->line("langAdminMain.Backup_ConnectCheck")?></a>
                                                                    <a id="btn_nas_Disconnect" class="btn-jquery large-btn" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'left: 274px;top: 235px;';else echo 'left: 190px;top: 235px;';?> position: absolute;"><?php echo $lang->line("langAdminMain.Backup_Disconnect")?></a>
                                                                    <a id='clearallbkfornas' class="btn-jquery large-btn" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'top: 235px;left: 361px;';else echo 'top: 235px;left: 311px;';?>position: absolute;"><?php echo $lang->line("langAdminMain.Backup_ClearBK")?></a>
                                                                </div>                                                                
                                                            </div>
                                                        </div>
                                                    </form>
                                                </fieldset>
                                            </div>
                                        </div>
                                    </div>                    
                                </div>               
                            <!-- 備份儲存設定  2017.01.11 william End*/ -->
                            <!-- 備份管理  2017.01.11 william Start*/ -->
                                <div id="tabs1-Backup_Manager">
                                    <!--<div class="BackupMgt_Content">
                                        <div style="overflow-x: auto;overflow-y: auto;height: 100%; width: 100%;">
                                            <div class="BackupMgt_div_Header" style="width:99%;height: 30px;display: block;margin-left: auto;margin-right: auto">
                                                <div style="position: relative;top: 5px;">

                                                </div>                                                    
                                            </div>
                                            <div class="BackupMgt_div_body">


                                            </div>
                                        </div>
                                    </div>-->
                                    <div class = "Div_RelativeLayout" >
                                        <div style="height:6px"></div> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                                        <div id="div_BackupMgt_scroll" style="position: relative;">
                                        <!---------------------------------------------------------------------------------------------------->

                                            <div class="BackupMgt_div_source">
                                                <div class="BackupMgt_div_Headertext" style="position: absolute;top: -27px;left: 0px;color:#044de1;font-size: 16px;"><?php echo $lang->line("langAdminMain.BackupMgt_Srtitle")?></div>                                            
                                                <div id="BKSRowHeader" class = "Div_VMLognRow VM_RowHeader" style="width: 100%;height: 32px;padding-top: 3px;border-top-left-radius: 0px;border-top-right-radius: 0px;overflow: hidden;position: relative;">                                    
                                                    <div id="Divsource_RowHeader_inner"  style="height: 100%;width: 100%;overflow-x: auto;overflow-y: hidden;position: absolute;padding-bottom: 40px;top: 0px;">
                                                <div class="BackupMgt_stable_row1 AbsoluteLayout RowField">                                        
                                                    <div class="div_RowHeader">
                                                        <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.BackupMgt_SN")?></H4>
                                                    </div>
                                                </div>
                                                <div class="BackupMgt_stable_row2 AbsoluteLayout RowField">
                                                    <div style="position:relative">
                                                        <input type="checkbox" class="iCheckBoxAllSourceVDSelect">
                                                    </div>
                                                </div>
                                                <div class = "BackupMgt_stable_row3 AbsoluteLayout RowField">                                        
                                                    <div class="div_RowHeader">
                                                        <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.BackupMgt_VDname")?>&nbsp;</H4>
                                                    </div>
                                                </div>
                                                <div class = "BackupMgt_stable_row7 AbsoluteLayout RowField">                                        
                                                    <div class="div_RowHeader">
                                                        <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.BackupMgt_CreateTime")?>&nbsp;</H4>
                                                    </div>
                                                </div> 
                                                <div class = "BackupMgt_stable_row4 AbsoluteLayout RowField">                                        
                                                    <div class="div_RowHeader">
                                                        <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.BackupMgt_resTime")?>&nbsp;</H4>
                                                    </div>
                                                </div>                                                                                
                                                <div class="BackupMgt_stable_row5 AbsoluteLayout RowField">
                                                    <div class="div_RowHeader">                                                   
                                                        <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.BackupMgt_fileSize")?>&nbsp;</H4>
                                                    </div>
                                                </div>
                                                <div class="BackupMgt_stable_row6 AbsoluteLayout RowField">
                                                    <div class="div_RowHeader">                                                    
                                                        <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.BackupMgt_Desc")?>&nbsp;</H4> 
                                                    </div>
                                                </div>
                                                    </div>                                        
                                                </div>
                                                <div id = "BackupMgt_Content_body_source" style="font-size: 12px;width: 100%;overflow-y: auto;" onscroll="document.getElementById('BackupMgt_Content_body_backup').scrollTop = this.scrollTop;document.getElementById('Divsource_RowHeader_inner').scrollLeft = this.scrollLeft;"></div> 
                                                <div id="BackupMgt_Storage_Capacity" class="BackupMgt_div_Sizetext"></div>
                                            </div>
                                            <div class="BackupMgt_div_Arrow">
                                                <div class="BackupMgt_div_Arrowtext"><?php echo $lang->line("langAdminMain.BackupMgt_Actiontext")?></div>
                                                    <div style="position: relative;width: 65px;">
                                                        <div style="position: absolute;top: -115px;left: 7px;">
                                                            <div style="position: relative;top: 125px;vertical-align: middle;text-align: center;left: 2px;font-size: 12px;"><?php echo $lang->line("langAdminMain.BackupMgt_Bktext")?></div>
                                                            <div id="BackupVDBtn" class="Backup_arrow" style="top: 130px;">⇨</div>
                                                            <div id="RestoreVDBtn" class="Backup_arrow" style="top: 150px;">⇦</div>
                                                            <div style="position: relative;top: 160px;vertical-align: middle;text-align: center;left: 2px;font-size: 12px;"><?php echo $lang->line("langAdminMain.BackupMgt_Restext")?></div>
                                                        </div>
                                                    </div>            
                                                </div>
                                            <div class="BackupMgt_div_backup">
                                                <div class="BackupMgt_div_Headertext" style="position: absolute;top: -27px;left: 0px;color:#044de1;font-size: 16px;"><?php echo $lang->line("langAdminMain.BackupMgt_Bktitle")?></div>
                                                <div style="position: absolute;right: 0px;top: -30px; width: 300px;text-align: right;margin: -7px 0;">
                                                    <a id='btn_delete_all_BackupVD' class="btn-jquery btn_BackupMgt_select_operation large-btn btn_resize_use" ><?php echo $lang->line("langAdminMain.DeleteBackup")?></a>
                                                    <a id='btn_reflash_all_BackupVD' class="btn-jquery large-btn btn_resize_use" ><?php echo $lang->line("langAdminMain.Btn_Refresh")?></a>
                                                </div> 
                                                <div id="BKBRowHeader" class = "Div_VMLognRow VM_RowHeader" style="width: 100%;height: 32px;padding-top: 3px;border-top-left-radius: 0px;border-top-right-radius: 0px;overflow: hidden;position: relative;">
                                                    <div id="Divbackup_RowHeader_inner"  style="height: 100%;width: 100%;overflow-x: auto;overflow-y: hidden;position: absolute;padding-bottom: 30px;top: 0px;">
                                                <div class="BackupMgt_btable_row1 AbsoluteLayout RowField">                                        
                                                    <div class="div_RowHeader">
                                                        <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.BackupMgt_SN")?></H4>
                                                    </div>
                                                </div>
                                                <div class="BackupMgt_btable_row2 AbsoluteLayout RowField">
                                                    <div style="position:relative">
                                                        <input type="checkbox" class="iCheckBoxAllBackUpVDSelect">
                                                    </div>
                                                </div>
                                                <div class = "BackupMgt_btable_row3 AbsoluteLayout RowField">                                        
                                                    <div class="div_RowHeader">
                                                        <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.BackupMgt_VDname")?>&nbsp;</H4>
                                                    </div>
                                                </div>
                                                <div class = "BackupMgt_btable_row4 AbsoluteLayout RowField">                                        
                                                    <div class="div_RowHeader">
                                                        <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.BackupMgt_bkTime")?>&nbsp;</H4>
                                                    </div>
                                                </div>                                                                                
                                                <div class="BackupMgt_btable_row5 AbsoluteLayout RowField">
                                                    <div class="div_RowHeader">                                                   
                                                        <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.BackupMgt_fileSize")?>&nbsp;</H4>
                                                    </div>
                                                </div>
                                                    </div>                                        
                                            </div>                                        
                                                <div id = "BackupMgt_Content_body_backup" style="font-size: 12px;width: 100%;overflow-y: auto;" onscroll="document.getElementById('BackupMgt_Content_body_source').scrollTop = this.scrollTop;document.getElementById('Divbackup_RowHeader_inner').scrollLeft = this.scrollLeft;"></div> 
                                                
                                            </div> 

                                        <!---------------------------------------------------------------------------------------------------->
                                        </div>
                                    </div>
                                </div>
                            <!-- 備份管理  2017.01.11 william End*/ -->
                            <!-- 備份排程  2017.01.11 william Start*/ -->
                                <div id="tabs1-Backup_schedule">

                        <div class = "Div_RelativeLayout" >                            
                                <div style="width:99%;height: 30px;display: block;margin-left: auto;margin-right: auto">
                                    <div style="position: relative">                                    
                                        <a id='btn_bk_create_schedule' class="btn-jquery large-btn" <?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'style="width: 115px;"';else echo 'style="width: 205px;"';?>><?php echo $lang->line("langAdminMain.Create_Schedule")?></a>                           
                                        <div style="position: absolute;right: 0px;top:0px">
                                            <a id='btn_scbkrule_all_vm' class="btn-jquery btn_sc_select_operation large-btn" ><?php echo $lang->line("langAdminMain.ScheduleRule")?></a>                                               
                                            <a id='btn_scbkjobs_all_vm' class="btn-jquery btn_sc_select_operation large-btn" ><?php echo $lang->line("langAdminMain.ScheduleJobs")?></a>                                                                                    
                                            <a id='btn_scbkdelete_all_vm' class="btn-jquery btn_sc_select_operation large-btn" ><?php echo $lang->line("langAdminMain.ScheduleDelete")?></a>
                                        </div>
                                    </div>
                                </div> 
                                <div style="height:6px"></div> 
                                    <div id="div_BackupSchedule_scroll" style="margin-top: 6px"> <!-- 取消style="overflow-x: auto;overflow-y: auto"; 2017.03.03 william -->            
                                        <div id='Div_VM' class="Div_BackupSC_Shot">                            
                                            <div id="VMRowHeader" class = "Div_VMRow VM_RowHeader" style="width: 100%;height: 32px;padding-top: 3px;border-top-left-radius: 0px;border-top-right-radius: 0px;overflow: hidden;position: relative;">
                                                <div id="DivBackupSC_RowHeader_inner"  style="height: 100%;width: 100%;overflow-x: auto;overflow-y: hidden;position: absolute;padding-bottom: 30px;top: 0px;">
                                                    <div class = "VMList_Task_ItemNo AbsoluteLayout RowField">
                                                        <div class="div_RowHeader">
                                                            <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.VMList_SN")?></H4>
                                                        </div>
                                                    </div>
                                                    <div class="Schedule_Snapshot_Select AbsoluteLayout RowField">
                                                        <div style="position:relative">
                                                            <input type="checkbox" class="iCheckBoxAllBackupScheduleSelect">
                                                        </div>
                                                    </div>                                    
                                                    <div id="ScheduleBackupName" class = "Schedule_Snapshot_Name AbsoluteLayout RowField ScheduleBackupHeader">
                                                        <div class="div_RowHeader">
                                                            <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.Schedule_Name")?>&nbsp;</H4>
                                                            <label id="autobkScheduleBackupName1" class="label_autobk_arrow label_RowHeader"></label>
                                                        </div>
                                                    </div>                                       
                                                    <div id="ScheduleBackupDesc" class = "Schedule_Snapshot_Desc AbsoluteLayout RowField ScheduleBackupHeader">
                                                        <div class="div_RowHeader">
                                                            <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.Schedule_Desc")?>&nbsp;</H4>                                    
                                                            <label id="autobkScheduleBackupDesc1" class="label_autobk_arrow label_RowHeader"></label>
                                                        </div>
                                                    </div>    
                                                    <div id="ScheduleBackupCreateTime" class = "Schedule_Snapshot_Time AbsoluteLayout RowField ScheduleBackupHeader">
                                                        <div class="div_RowHeader">
                                                            <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.Schedule_Time")?>&nbsp;</H4>                                    
                                                            <label id="autobkScheduleBackupCreateTime1" class="label_autobk_arrow label_RowHeader"></label>
                                                        </div>
                                                    </div>                                                     
                                                    <div id="ScheduleAction" class = "Schedule_Snapshot_Action AbsoluteLayout RowField">
                                                        <div class="div_RowHeader">
                                                            <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.Schedule_Action")?>&nbsp;</H4>
                                                        </div>
                                                    </div> 
                                                </div>                                                                                                                                                                       
                                            </div>      
                                            <!--  BackupSchedule - List  -->
                                            <div id = "Div_Service_BackupSchedule_List" style="font-size: 12px; width: 100%; overflow-y: auto;" onscroll="document.getElementById('DivBackupSC_RowHeader_inner').scrollLeft = this.scrollLeft;"> <!-- 取消overflow-x: hidden; 2017.03.03 william -->                               
                                            </div> 
                                        </div>
                                    </div>
                        </div>


                                </div>
                            <!-- 備份排程  2017.01.11 william End --> 
                            <!-- 備份任務  2017.01.11 william Start -->
                    <div id="tabs1-bktask-list" style="font-size: 12px">
                        <div class = "Div_RelativeLayout" >                            
                                <div style="width:99%;height: 30px;display: block;margin-left: auto;margin-right: auto"> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                                    <div style="position: relative">                                    
                                                                       
                                        <div style="position: absolute;right: 0px;top:0px"> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->           
                                       
                                            <a id='btn_clear_bktask' class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.VD_Task_Clear")?></a>  
                                            <a id='btn_refresh_bktask' class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Refresh")?></a>
                                        </div>
                                    </div>
                                </div> 
                            <div style="height:6px"></div>
                                <div id="div_bktask_scroll" style="margin-top: 6px;"> <!-- 取消style="overflow-x: auto;overflow-y: auto"; 2017.03.06 william -->
                                <div id='Div_VM' class="Div_Backup_Task">      
                                <div id="VMRowHeader" class = "Div_VMRow VM_RowHeader" style="width: 100%;height: 32px;padding-top: 3px;border-top-left-radius: 0px;border-top-right-radius: 0px;overflow: hidden;position: relative;"> 
                                    <div id="Divbktask_RowHeader_inner"  style="height: 100%;width: 100%;overflow-x: auto;overflow-y: hidden;position: absolute;padding-bottom: 30px;top: 0px;">
                                        <div class = "BKList_Task_ItemNo AbsoluteLayout RowField">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.VMList_SN")?></H4>
                                            </div>
                                        </div>
                                        <div id="BKListType" class = "VMBackupList_Task_Type AbsoluteLayout RowField BKTaskHeader">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.TaskListType")?>&nbsp;</H4>
                                                <label id="bktaskBKListType1" class="label_bktask_arrow label_RowHeader"></label>
                                            </div>
                                        </div>                                    
                                        <div  id="BKListSourcVD" class = "VMBackupList_Task_SourcVD AbsoluteLayout RowField BKTaskHeader">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.VD_Task_Source")?>&nbsp;</H4>
                                                <label id="bktaskBKListSourcVD1" class="label_bktask_arrow label_RowHeader"></label>
                                            </div>
                                        </div>     
                                        <div id="BKListDestVD" class = "VMBackupList_Task_DestVD AbsoluteLayout RowField BKTaskHeader">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.VD_Task_Dest")?>&nbsp;</H4>                                    
                                                <label id="bktaskBKListDestVD1" class="label_bktask_arrow label_RowHeader"></label>
                                            </div>
                                        </div>      
                                        <div id="BKListProgress" class = "VMBackupList_Task_Progress AbsoluteLayout RowField">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.List_Progress")?>&nbsp;</H4>                                    
                                            </div>
                                        </div>                                                                                                    
                                        <div id="BKListState" class = "VMBackupList_Task_State AbsoluteLayout RowField BKTaskHeader">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.TaskListState")?>&nbsp;</H4>
                                                <label id="bktaskBKListState1" class="label_bktask_arrow label_RowHeader"></label>
                                            </div>
                                        </div>                                                                     
                                        <div  id="BKListStartTime" class = "VMBackupList_Task_StartTime AbsoluteLayout RowField BKTaskHeader">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.TaskListStartTime")?>&nbsp;</H4>
                                                <label id="bktaskBKListStartTime1" class="label_bktask_arrow label_RowHeader">&#9660;</label>
                                            </div>
                                        </div>
                                        <div  id="BKListEndTime" class = "VMBackupList_Task_EndTime AbsoluteLayout RowField BKTaskHeader">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.TaskListEndTime")?>&nbsp;</H4>
                                                <label id="bktaskBKListEndTime1" class="label_bktask_arrow label_RowHeader"></label>
                                            </div>
                                        </div>
                                        <!--<H4 class = "VMList_Action AbsoluteLayout RowField" >Action</H4>-->
                                    </div>
                                </div>    
                                <!--  VM - List  -->
                                <div id = "Div_VMBackup_Clone_List" style="font-size: 12px; width: 100%; overflow-y: auto;" onscroll="document.getElementById('Divbktask_RowHeader_inner').scrollLeft = this.scrollLeft;"> <!-- 取消overflow-x: hidden; 2017.03.03 william -->                             
                                </div> 
                            </div>
                            </div>
                        </div>
                    </div>                            
                            <!-- 備份任務  2017.01.11 william End-->                       
                            </div>  
                        </div> 
                    </div> 
                </div>
            </div>
        </div>
    </div>
                
    <!--
        ++++++++++++++++++++++++++++++
        +		VM Setting		 +
        ++++++++++++++++++++++++++++++
    -->
    <div id = "Div_LayoutZone_SysCfg_VM" class = "Div_Design_Layout" >        
        <div id = "Div_SysCfg_VM" class = "Div_MainContain" >
            <div id="tab-vm-container" class='tab-container' style="margin: 20px;margin-bottom: 7px;">
                <ul class='etabs'>                    
                    <li class='tab'><a href="#tabs1-vm"><?php echo  $lang->line("langAdminMain.VD_Original")?></a></li>
                    <li class='tab <?php if($outputUIConfig['Seed'] != 1){echo 'tabdisabled';}?>' ><a href="#tabs1-task-seed" <?php if($outputUIConfig['Seed'] != 1){echo ' style="text-decoration:none"';} ?>><?php echo $lang->line("langAdminMain.VD_Seed").$VMorVDI;?></a></li>
                    <li class='tab <?php if($outputUIConfig['Seed'] != 1){echo 'tabdisabled';}?>'><a href="#tabs1-borned" <?php if($outputUIConfig['Seed'] != 1){echo ' style="text-decoration:none"';} ?>> <?php echo $lang->line("langAdminMain.VD_Users")?></a></li>
                    <li class='tab' ><a href="#tabs1-vm-setting"><?php echo $lang->line("langAdminMain.Str_VD_Suspend_Setting")?></a></li>
                    <li class='tab'><a href="#tabs1-task-list"><?php echo  $lang->line("langAdminMain.VD_Task")?></a></li>                    
                    <li class='tab'><a href="#tabs1-sstask-list"><?php echo  $lang->line("langAdminMain.Task")?></a></li> <!-- // Snapshot task list介面功能實作 2017.01.26 -->                     
                    <!--<li class='tab'><a href="#tabs1-action">VM Action</a></li>-->                    
                </ul>
                <div class='panel-container'>                                        
                    <div id="tabs1-vm" style="font-size: 12px">                                                
            		<div class = "Div_RelativeLayout" >                            
                            <div style="width:99%;height: 30px;display: block;margin-left: auto;margin-right: auto"> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                                <div style="position: relative">
                                    <a id='btn_create_vm_dialog' class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.H4_BtnRaidCreate")?></a>                                    
                                    <a id="btn_import_vm" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.H4_BtnImport")?></a>                                    
                                    <div style="position: absolute;right: 0px;top:0px">
                                       <!--  <a id='btn_enable_all_vm' class="btn-jquery btn_vm_select_operation large-btn" ><?php //echo $lang->line("langAdminMain.Service_Enable")?></a>
                                        <a id='btn_disable_all_vm' class="btn-jquery btn_vm_select_operation large-btn"><?php //echo $lang->line("langAdminMain.Service_Disable")?></a> -->
                                        <a id='btn_poweron_all_vm' class="btn-jquery btn_vm_select_operation large-btn"><?php echo $lang->line("langAdminMain.VD_Poweron")?></a>
                                        <a id='btn_shutdown_all_vm' class="btn-jquery btn_vm_select_operation large-btn"><?php echo $lang->line("langAdminMain.VD_Shutdown")?></a>
                                        <a id='btn_poweroff_all_vm' class="btn-jquery btn_vm_select_operation large-btn"><?php echo $lang->line("langAdminMain.VD_Poweroff")?></a>
                                        <a id='btn_delete_all_vm' class="btn-jquery btn_vm_select_operation large-btn"><?php echo $lang->line("langAdminMain.H4_BtnRaidDelete")?></a>
                                        <a id='btn_refresh_vm' class="btn-jquery vm_refresh large-btn"><?php echo $lang->line("langAdminMain.Btn_Refresh")?></a>
                                    </div>
                                </div>
                            </div> 
                            <div style="height:6px"></div> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                            <div id="div_vm_scroll" style="margin-top: 6px;"> <!-- 取消style="overflow-x: auto;overflow-y: auto"; 2017.03.03 william -->
                            <div id='Div_VM' class="Div_VM_Logn">  
                                <div id="LogRowHeader" class = "Div_VMLognRow VM_RowHeader" style="width: 100%;height: 32px;padding-top: 3px;border-top-left-radius: 0px;border-top-right-radius: 0px;overflow: hidden;position: relative;"> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                                    <div id="DivVM_RowHeader_inner"  style="height: 100%;width: 100%;overflow-x: auto;overflow-y: hidden;position: absolute;padding-bottom: 30px;top: 0px;">
                                        <div class="VMList_ItemNo AbsoluteLayout RowField">                                        
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader" id="VMList_SN_1"><?php echo $lang->line("langAdminMain.VMList_SN")?></H4> <!-- 新式樣修改(藍黃) 2016.12.13 william -->
                                            </div>
                                        </div>
                                        <div class="VMList_Select AbsoluteLayout RowField">
                                            <div style="position:relative">
                                                <input type="checkbox" class="iCheckBoxAllSelect">
                                            </div>
                                        </div>
                                        <div id="username" class = "VMList_Username AbsoluteLayout RowField VMHeader">                                        
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.Str_Username")?>&nbsp;</H4>
                                                <label id="vmusername1" class="label_vm_arrow label_RowHeader"></label>
                                            </div>
                                        </div>
                                        <div id="name" class = "VMList_Name AbsoluteLayout RowField VMHeader">                                        
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $VMorVDIBack.$lang->line("langAdminMain.VMList_Name")?>&nbsp;</H4>
                                                <label id="vmname1" class="label_vm_arrow label_RowHeader">&#9650;</label>
                                            </div>
                                        </div>
                                        <div class="VMList_Auto_Suspend_Select AbsoluteLayout RowField">
                                            <!--<div style="position:relative;bottom: 3px;height: 30px;">--> 
                                            <div style="position:relative">                                                   
                                                <input id="vm_auto_suspend" type="checkbox" class="SelectAllVMSuspend">                                            
                                            </div>
                                        </div>
                                        <div id="suspend" class="VMList_Auto_Suspend AbsoluteLayout RowField VMHeader">
                                            <div class="div_RowHeader">                                         
                                                <!-- 新式樣修改(藍黃) 2016.12.13 william -->
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.Str_Auto_Suspend")?>&nbsp;</H4> 
                                                <label id="vmsuspend1" class="label_vm_arrow label_RowHeader"></label>
                                            </div>
                                        </div>
                                        <div id="state_display" class = "VMList_Status AbsoluteLayout RowField VMHeader" >
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $VMorVDIBack.$lang->line("langAdminMain.VD_Status")?>&nbsp;</H4>                                                                        
                                                <label id="vmstate_display1" class="label_vm_arrow label_RowHeader"></label>
                                            </div>
                                        </div>
                                        <div id="online" class = "VMList_Online AbsoluteLayout RowField VMHeader" >
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.List_Online_Status")?>&nbsp;</H4>                                                                        
                                                <label id="vmonline1" class="label_vm_arrow label_RowHeader"></label>
                                            </div>
                                        </div>
                                        <div id="ram" class = "VMList_RAM AbsoluteLayout RowField VMHeader" >
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader" id="ram_1" style="padding-right: 5px;"><?php echo $lang->line("langAdminMain.List_RAM")?></H4>                                            
                                                <label id="vmram1" class="label_vm_arrow label_RowHeader"></label>
                                            </div>                                        
                                        </div>      
                                        <div id="action" class = "VMList_Action AbsoluteLayout RowField" >
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.List_Action")?></H4>                                            
                                            </div>                                        
                                        </div>      
                                        <div id="create_time" class = "VMList_CreateTime AbsoluteLayout RowField VMHeader" >
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.List_Create_Time")?>&nbsp;</H4>
                                                <label id="vmcreate_time1" class="label_vm_arrow label_RowHeader"></label>
                                            </div>                                        
                                        </div> 
                                        <!-- 桌面設定-新增VD檔案大小實作 2017.05.03 william -->
                                        <div id="all_size" class = "VMList_AllSize AbsoluteLayout RowField VMHeader" >
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.List_All_Size")?>&nbsp;</H4>
                                                <label id="vmall_size1" class="label_vm_arrow label_RowHeader"></label>
                                            </div>                                        
                                        </div>
                                        <div id="desc" class = "VMList_Desc AbsoluteLayout RowField VMHeader" >
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.List_Description")?>&nbsp;</H4>
                                                <label id="vmdesc1" class="label_vm_arrow label_RowHeader"></label>
                                            </div>
                                        </div>    
                                    </div>                                                                         
                                </div>                      
                            <!--  VM - List  -->
                                <div id = "Div_VMList" style="font-size: 12px;width: 100%;overflow-y: auto;" onscroll="document.getElementById('DivVM_RowHeader_inner').scrollLeft = this.scrollLeft;"> <!-- 取消overflow-x: hidden; 2017.03.03 william -->     

                                </div> 
                            </div>
                            </div>
                        </div>
                    </div>
                    <div id="tabs1-task-seed" style="font-size: 12px;<?php if(!$VDI){echo 'display:none';} ?>">                        
                        <div class = "Div_RelativeLayout" >
                                <div style="width:99%;height: 30px;display: block;margin-left: auto;margin-right: auto"> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                                    <div style="position: relative">                                    
                                        <div style="position: absolute;right: 0px;top:0px">  
                                            <a id='btn_delete_all_seed' class="btn-jquery btn_seed_select_operation  large-btn"><?php echo $lang->line("langAdminMain.H4_BtnRaidDelete")?></a>
                                            <a id='btn_refresh_seed' class="btn-jquery vm_refresh  large-btn"><?php echo $lang->line("langAdminMain.Btn_Refresh")?></a>
                                        </div>
                                    </div>
                                </div> 
                            <div style="height:6px"></div> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                            <div id="div_seed_scroll" style="margin-top: 6px;"> <!-- 取消style="overflow-x: auto;overflow-y: auto"; 2017.03.03 william -->
                                <div id='Div_VM' class="Div_VM_Shot" style="width: 99%;">                            
                                    <div id="VMRowHeader" class = "Div_VMRow VM_RowHeader" style="width: 100%;height: 32px;padding-top: 3px;border-top-left-radius: 0px;border-top-right-radius: 0px;overflow: hidden;position: relative;"> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                                        <div id="DivSeed_RowHeader_inner"  style="height: 100%;width: 100%;overflow-x: auto;overflow-y: hidden;position: absolute;padding-bottom: 30px;top: 0px;">
                                            <div class="VMList_ItemNo AbsoluteLayout RowField">                                        
                                                <div class="div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.VMList_SN")?></H4>
                                                </div>
                                            </div>
                                            <div class="SeedList_Select AbsoluteLayout RowField">
                                                <div style="position:relative">
                                                    <input type="checkbox" class="iCheckBoxSeedAllSelect">
                                                </div>
                                            </div>
                                            <div id="name" class = "SeedList_Name AbsoluteLayout RowField SeedHeader">
                                                <div class="div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $VMorVDIBack.$lang->line("langAdminMain.VMList_Name")?>&nbsp;</H4>                                    
                                                    <label id="seedname1" class="label_seed_arrow label_RowHeader">&#9650;</label>
                                                </div>
                                            </div>
                                            <div id="state_display" class = "SeedList_Status AbsoluteLayout RowField SeedHeader" >
                                                <div class="div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.Seed_Status")?>&nbsp;</H4>                                                                        
                                                    <label id="seedstate_display1" class="label_seed_arrow label_RowHeader"></label>
                                                </div>
                                            </div>
                                            <div id="vdi_count" class = "SeedList_VDICount AbsoluteLayout RowField SeedHeader">
                                                <div class="div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.VD_Users").$VMorVDI.$lang->line("langAdminMain.VD_Quantity");?>&nbsp;</H4>                                    
                                                    <label id="seedvdi_count1" class="label_seed_arrow label_RowHeader"></label>
                                                </div>
                                            </div>
                                            <div id="ram" class = "SeedList_RAM AbsoluteLayout RowField SeedHeader" >
                                                <div class="div_RowHeader">
                                                    <H4 class="H4_RowHeader" style="padding-right: 5px;"><?php echo $lang->line("langAdminMain.List_RAM")?></H4>                                            
                                                    <label id="seedram1" class="label_seed_arrow label_RowHeader"></label>
                                                </div>                                        
                                            </div>
                                            <div id="create_time" class = "SeedList_CreateTime AbsoluteLayout RowField SeedHeader" >
                                                <div class="div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.List_Create_Time")?>&nbsp;</H4>
                                                    <label id="seedcreate_time1" class="label_seed_arrow label_RowHeader"></label>
                                                </div>
                                            </div>
                                            <!-- 桌面設定-新增VD檔案大小實作 2017.05.03 william -->
                                            <div id="all_size" class = "SeedList_AllSize AbsoluteLayout RowField SeedHeader" >
                                                <div class="div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.List_All_Size")?>&nbsp;</H4>
                                                    <label id="seedall_size1" class="label_seed_arrow label_RowHeader"></label>
                                                </div>                                        
                                            </div>
                                            <H4 class = "SeedList_Action AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.List_Action")?></H4>                   
                                            <div id="desc" class = "SeedList_Description AbsoluteLayout RowField SeedHeader">
                                                <div class="div_RowHeader">
                                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.List_Description")?>&nbsp;</H4>   
                                                    <label id="seeddesc1" class="label_seed_arrow label_RowHeader"></label>
                                                </div>
                                            </div>
                                        </div>
                                    </div> 
                                    <!--  VM - List  -->
                                    <div id = "Div_SeedList" style="font-size: 12px;width: 100%;overflow-y: auto;" onscroll="document.getElementById('DivSeed_RowHeader_inner').scrollLeft = this.scrollLeft;"> <!-- 取消overflow-x: hidden; 2017.03.03 william -->    

                                    </div> 
                                </div>
                            </div>
                        </div>
                     </div>
                    <div id="tabs1-borned" style="font-size: 12px;<?php if(!$VDI){echo 'display:none';} ?>">  
                        <div class = "Div_RelativeLayout" >
                            
                                <div style="width:99%;height: 30px;display: block;margin-left: auto;margin-right: auto"> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                                <div style="position: relative">                                    
                                    <div style="position: absolute;right: 0px;top:0px">
                                        <!-- <a id='btn_enable_all_borned' class="btn-jquery btn_borned_select_operation large-btn"><?php //echo $lang->line("langAdminMain.Service_Enable")?></a>
                                        <a id='btn_disable_all_borned' class="btn-jquery btn_borned_select_operation large-btn"><?php //echo $lang->line("langAdminMain.Service_Disable")?></a> -->
                                        <a id='btn_poweron_all_borned' class="btn-jquery btn_borned_select_operation large-btn"><?php echo $lang->line("langAdminMain.VD_Poweron")?></a>
                                        <a id='btn_shutdown_all_borned' class="btn-jquery btn_borned_select_operation large-btn"><?php echo $lang->line("langAdminMain.VD_Shutdown")?></a>
                                        <a id='btn_poweroff_all_borned' class="btn-jquery btn_borned_select_operation large-btn"><?php echo $lang->line("langAdminMain.VD_Poweroff")?></a>
                                        <a id='btn_delete_all_borned' class="btn-jquery btn_borned_select_operation large-btn"><?php echo $lang->line("langAdminMain.H4_BtnRaidDelete")?></a>
                                        <a id='btn_refresh_borned' class="btn-jquery vm_refresh large-btn"><?php echo $lang->line("langAdminMain.Btn_Refresh")?></a>
                                    </div>
                                </div>                                 
                            </div>     
                            <div style="height:6px"></div> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->                                
                                <div id="div_vm_scroll" style="margin-top: 6px;"> <!-- 取消style="overflow-x: auto;overflow-y: auto"; 2017.03.03 william -->
                                    <div id='Div_Borned' class="Div_VM_Logn">                            
                                        <div id="VMRowHeader" class = "Div_VMLognRow VM_RowHeader" style="width: 100%;height: 32px;padding-top: 3px;border-top-left-radius: 0px;border-top-right-radius: 0px;overflow: hidden;position: relative;"> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                                            <div id="DivBorned_RowHeader_inner"  style="height: 100%;width: 100%;overflow-x: auto;overflow-y: hidden;position: absolute;padding-bottom: 30px;top: 0px;">
                                                <div class="VMList_ItemNo AbsoluteLayout RowField">                                        
                                                    <div class="div_RowHeader">
                                                        <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.VMList_SN")?></H4>
                                                    </div>
                                                </div>
                                                <div class="BornedList_Select AbsoluteLayout RowField">
                                                    <div style="position:relative">
                                                        <input type="checkbox" class="iCheckBoxBornedAllSelect">
                                                    </div>
                                                </div>
                                                <div id="username" class = "BornedList_Username AbsoluteLayout RowField BornedHeader">                                        
                                                    <div class="div_RowHeader">
                                                        <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.Str_Username")?>&nbsp;</H4>
                                                        <label id="bornedusername1" class="label_borned_arrow label_RowHeader"></label>
                                                    </div>
                                                </div>
                                                <div id="name" class = "BornedList_Name AbsoluteLayout RowField BornedHeader">                                        
                                                    <div class="div_RowHeader">
                                                        <H4 class="H4_RowHeader"><?php echo $VMorVDIBack.$lang->line("langAdminMain.VMList_Name")?>&nbsp;</H4>
                                                        <label id="bornedname1" class="label_borned_arrow label_RowHeader">&#9650;</label>
                                                    </div>
                                                </div>                                                                                
                                                <div class="BornedList_Auto_Suspend_Select AbsoluteLayout RowField">
                                                    <div style="position:relative">                                                   
                                                        <input type="checkbox" class="SelectAllBornedSuspend">
                                                    </div>
                                                </div>
                                                <div id="suspend" class="BornedList_Auto_Suspend AbsoluteLayout RowField BornedHeader">
                                                    <div class="div_RowHeader">                                                    
                                                        <!-- 新式樣修改(藍黃) 2016.12.13 william -->
                                                        <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.Str_Auto_Suspend")?>&nbsp;</H4> 
                                                        <label id="bornedsuspend1" class="label_borned_arrow label_RowHeader"></label>
                                                    </div>
                                                </div>
                                                <div id="state_display" class = "BornedList_Status AbsoluteLayout RowField BornedHeader" >
                                                    <div class="div_RowHeader">
                                                        <H4 class="H4_RowHeader"><?php echo $VMorVDIBack.$lang->line("langAdminMain.VD_Status")?>&nbsp;</H4>                                                                        
                                                        <label id="bornedstate_display1" class="label_borned_arrow label_RowHeader"></label>
                                                    </div>
                                                </div>
                                                <div id="online" class = "BornedList_Online AbsoluteLayout RowField BornedHeader" >
                                                    <div class="div_RowHeader">
                                                        <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.List_Online_Status")?>&nbsp;</H4>                                                                        
                                                        <label id="bornedonline1" class="label_borned_arrow label_RowHeader"></label>
                                                    </div>
                                                </div>
                                                <div id="ram" class = "BornedList_RAM AbsoluteLayout RowField BornedHeader" >
                                                    <div class="div_RowHeader">
                                                        <H4 class="H4_RowHeader" style="padding-right: 5px;"><?php echo $lang->line("langAdminMain.List_RAM")?></H4>                                            
                                                        <label id="bornedram1" class="label_borned_arrow label_RowHeader"></label>
                                                    </div>                                        
                                                </div>                                                  
                                                <H4 class = "BornedList_Action AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.List_Action")?></H4>                      
                                                <div id="seed_src" class = "BornedList_SeedSrc AbsoluteLayout RowField BornedHeader">
                                                    <div class="div_RowHeader">
                                                        <H4 class="H4_RowHeader"><?php echo $VMorVDIBack.$lang->line("langAdminMain.VD_Source")?>&nbsp;</H4>
                                                        <label id="bornedseed_src1" class="label_borned_arrow label_RowHeader"></label>
                                                    </div>
                                                </div>
                                                <div id="create_time" class = "BornedList_CreateTime AbsoluteLayout RowField BornedHeader" >
                                                    <div class="div_RowHeader">
                                                        <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.List_Create_Time")?>&nbsp;</H4>
                                                        <label id="bornedcreate_time1" class="label_borned_arrow label_RowHeader"></label>
                                                    </div>                                        
                                                </div> 
                                                <!-- 桌面設定-新增VD檔案大小實作 2017.05.03 william -->
                                                <div id="all_size" class = "BornedList_AllSize AbsoluteLayout RowField BornedHeader" >
                                                    <div class="div_RowHeader">
                                                        <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.List_All_Size")?>&nbsp;</H4>
                                                        <label id="bornedall_size1" class="label_borned_arrow label_RowHeader"></label>
                                                    </div>                                        
                                                </div>                                                
                                                <div id="desc" class = "BornedList_Desc AbsoluteLayout RowField BornedHeader" >
                                                    <div class="div_RowHeader">
                                                        <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.List_Description")?>&nbsp;</H4>
                                                        <label id="borneddesc1" class="label_borned_arrow label_RowHeader"></label>
                                                    </div>
                                                </div>     
                                            </div>                                        
                                        </div>
                                        <!--  VM - List  -->
                                        <div id = "Div_Borned_List" style="font-size: 12px;width: 100%;overflow-y: auto;" onscroll="document.getElementById('DivBorned_RowHeader_inner').scrollLeft = this.scrollLeft;"> <!-- 修改2017.03.03 william -->     

                                        </div> 
                                    </div>
                                </div> 
                            </div>                        
                    </div>
                    <div id="tabs1-task-list" style="font-size: 12px">
                        <div class = "Div_RelativeLayout" >                            
                                <div style="width:99%;height: 30px;display: block;margin-left: auto;margin-right: auto"> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                                    <div style="position: relative">                                    
                                                                      
                                        <div style="position: absolute;right: 0px;top:0px"> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->           
                                            <!-- 未來新增之項目 2016.12.23 william                
                                            <a id='btn_delete_task' href="#" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.VD_Task_Delete")?></a>
                                            -->                                             
                                            <a id='btn_clear_task' class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.VD_Task_Clear")?></a>  
                                            <a id='btn_refresh_task' class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Refresh")?></a> 
                                        </div>
                                    </div>
                                </div> 
                            <div style="height:6px"></div> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                                <div id="div_task_scroll" style="margin-top: 6px;"> <!-- 取消style="overflow-x: auto;overflow-y: auto"; 2017.03.03 william -->            
                                <div id='Div_VM' class="Div_VM_Shot" style="width: 99%;">                            
                                <div id="VMRowHeader" class = "Div_VMRow VM_RowHeader" style="width: 100%;height: 32px;padding-top: 3px;border-top-left-radius: 0px;border-top-right-radius: 0px;overflow: hidden;position: relative;"> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                                    <div id="VMList_Task_RowHeader_inner"  style="height: 100%;width: 100%;overflow-x: auto;overflow-y: hidden;position: absolute;padding-bottom: 30px;top: 0px;">
                                        <div class = "VMList_Task_ItemNo AbsoluteLayout RowField">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.VMList_SN")?></H4>
                                            </div>
                                        </div>
                                        <!-- 未來新增之項目 2016.12.23 william
                                        <div class="TaskList_Select AbsoluteLayout RowField">
                                            <div style="position:relative">
                                                <input type="checkbox" class="iCheckBoxTaskAllSelect">
                                            </div>
                                        </div>
                                        -->
                                        <div id="type" class = "VMList_Type AbsoluteLayout RowField TaskHeader">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.VMList_Type")?>&nbsp;</H4>
                                                <label id="tasktype1" class="label_task_arrow label_RowHeader"></label>
                                            </div>
                                        </div>
                                        <div  id="srcDomName" class = "VMList_Source_Name AbsoluteLayout RowField TaskHeader">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.VD_Task_Source")?>&nbsp;</H4>
                                                <label id="tasksrcDomName1" class="label_task_arrow label_RowHeader"></label>
                                            </div>
                                        </div>
                                        <div id="destDomName" class = "VMList_Dest_Name AbsoluteLayout RowField TaskHeader">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.VD_Task_Dest")?>&nbsp;</H4>
                                                <label id="taskdestDomName1" class="label_task_arrow label_RowHeader"></label>
                                            </div>
                                        </div>
                                        <div id="process" class = "VMList_Task_Progress AbsoluteLayout RowField TaskHeader">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.List_Progress")?>&nbsp;</H4>                                    
                                                <label id="taskprocess1" class="label_task_arrow label_RowHeader"></label>
                                            </div>
                                        </div>
                                        <div id="status" class = "VMList_Task_Status AbsoluteLayout RowField TaskHeader">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.VD_Status")?>&nbsp;</H4>
                                                <label id="taskstatus1" class="label_task_arrow label_RowHeader"></label>
                                            </div>
                                        </div>
                                        <div  id="start_time" class = "VMList_Start_Time AbsoluteLayout RowField TaskHeader">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.List_Start_Time")?>&nbsp;</H4>
                                                <label id="taskstart_time1" class="label_task_arrow label_RowHeader">&#9660;</label>
                                            </div>
                                        </div>
                                        <div  id="end_time" class = "VMList_End_Time AbsoluteLayout RowField TaskHeader">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.List_End_Time")?>&nbsp;</H4>
                                                <label id="taskend_time1" class="label_task_arrow label_RowHeader"></label>
                                            </div>
                                        </div>
                                        <!--<H4 class = "VMList_Action AbsoluteLayout RowField" >Action</H4>-->
                                    </div>
                                </div> 
                                                                   
                            <!--  VM - List  -->
                                <div id = "Div_VM_Clone_List" style="font-size: 12px; width: 100%; overflow-y: auto;" onscroll="document.getElementById('VMList_Task_RowHeader_inner').scrollLeft = this.scrollLeft;"> <!-- 取消overflow-x: hidden; 2017.03.03 william -->                               
                                </div> 
                            </div>
                            </div>
                        </div>
                    </div>

                    <!--  快照-任務清單功能實作 2017.02.02  -->
                    <div id="tabs1-sstask-list" style="font-size: 12px">
                        <div class = "Div_RelativeLayout" >                            
                                <div style="width:99%;height: 30px;display: block;margin-left: auto;margin-right: auto"> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                                    <div style="position: relative">                                    
                                                                       
                                        <div style="position: absolute;right:0px;top:0px"> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->           
                                       
                                            <a id='btn_clear_sstask' class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.VD_Task_Clear")?></a>  
                                            <a id='btn_refresh_sstask' class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Refresh")?></a>
                                        </div>
                                    </div>
                                </div> 
                            <div style="height:6px"></div> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                                <div id="div_sstask_scroll" style="margin-top: 6px;"> <!-- 取消style="overflow-x: auto;overflow-y: auto"; 2017.03.03 william -->                                   
                                <div id='Div_VM' class="Div_Snapshot_Task">                            
                                <div id="VMRowHeader" class = "Div_VMRow VM_RowHeader" style="width: 100%;height: 32px;padding-top: 3px;border-top-left-radius: 0px;border-top-right-radius: 0px;overflow: hidden;position: relative;">
                                    <div id="SnapShotTask_RowHeader_inner"  style="height: 100%;width: 100%;overflow-x: auto;overflow-y: hidden;position: absolute;padding-bottom: 30px;top: 0px;">
                                        <div class = "VMList_Task_ItemNo AbsoluteLayout RowField">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.VMList_SN")?></H4>
                                            </div>
                                        </div>
                                        <div id="type" class = "VMSnapshotList_Task_Type AbsoluteLayout RowField SSTaskHeader">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.TaskListType")?>&nbsp;</H4>
                                                <label id="sstasktype1" class="label_sstask_arrow label_RowHeader"></label>
                                            </div>
                                        </div>                                    
                                        <div  id="vdname" class = "VMSnapshotList_Task_VDName AbsoluteLayout RowField SSTaskHeader">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.TaskListVDName")?>&nbsp;</H4>
                                                <label id="sstaskvdname1" class="label_sstask_arrow label_RowHeader"></label>
                                            </div>
                                        </div>     
                                        <div id="layerdate" class = "VMSnapshotList_Task_LayerDate AbsoluteLayout RowField SSTaskHeader">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.TaskListLayerDate")?>&nbsp;</H4>                                    
                                                <label id="sstasklayerdate1" class="label_sstask_arrow label_RowHeader"></label>
                                            </div>
                                        </div>      
                                        <div id="layerdesc" class = "VMSnapshotList_Task_LayerDesc AbsoluteLayout RowField SSTaskHeader">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.TaskListLayerDesc")?>&nbsp;</H4>                                    
                                                <label id="sstasklayerdesc1" class="label_sstask_arrow label_RowHeader"></label>
                                            </div>
                                        </div>                                                                                                    
                                        <div id="state" class = "VMSnapshotList_Task_State AbsoluteLayout RowField SSTaskHeader">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.TaskListState")?>&nbsp;</H4>
                                                <label id="sstaskstate1" class="label_sstask_arrow label_RowHeader"></label>
                                            </div>
                                        </div>                                                                     
                                        <div  id="start_time" class = "VMSnapshotList_Task_StartTime AbsoluteLayout RowField SSTaskHeader">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.TaskListStartTime")?>&nbsp;</H4>
                                                <label id="sstaskstart_time1" class="label_sstask_arrow label_RowHeader">&#9660;</label>
                                            </div>
                                        </div>
                                        <div  id="end_time" class = "VMSnapshotList_Task_EndTime AbsoluteLayout RowField SSTaskHeader">
                                            <div class="div_RowHeader">
                                                <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.TaskListEndTime")?>&nbsp;</H4>
                                                <label id="sstaskend_time1" class="label_sstask_arrow label_RowHeader"></label>
                                            </div>
                                        </div>
                                        <!--<H4 class = "VMList_Action AbsoluteLayout RowField" >Action</H4>-->
                                    </div> 
                                </div>                 
                                <!--  VM - List  -->
                                <div id = "Div_VMSnapshot_Clone_List" style="font-size: 12px; width: 100%; overflow-y: auto;" onscroll="document.getElementById('SnapShotTask_RowHeader_inner').scrollLeft = this.scrollLeft;"> <!-- 取消overflow-x: hidden; 2017.03.03 william -->                                   
                                </div> 
                            </div>
                            </div>
                        </div>
                    </div>
                    <div id="tabs1-vm-setting" style="font-size: 12px;">
                        <div class = "Div_RelativeLayout" > 
                            <label class="LabelSuspendHead"><?php echo $lang->line("langAdminMain.Str_Auto_Suspend_Setting")?></label>                    
                            <input id="spinner_suspend" class="jquery_suspend_spinner" name="value" style="width:100px">
                            <label style="display: inline-block;width:50px"><?php echo $lang->line("langAdminMain.Str_Minute")?></label>                    
                            <a id="btn_chg_auto_suspend" class="btn-jquery large-btn" ><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
                            <a id="btn_clear_chg_auto_suspend" class="btn-jquery large-btn" ><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
                        </div>
                    </div>
                </div>
            </div>
            <div style="margin-left:20px;color:#044de1;font-size: 18px;"> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->               
                <label><?php echo $lang->line("langAdminMain.Str_Max_VD")?> : </label>
                <label><?php if((int)$poweron_limit === 9999999){echo $lang->line("langAdminMain.Str_Poweron_Unlimited");}else{echo $poweron_limit;} ?></label>
                <label> <?php echo $lang->line("langAdminMain.Str_Item")?></label>
                <label style="margin-left:20px"><?php echo $lang->line("langAdminMain.Str_Poweron")?> : </label>
                <label id="vd_poweron_count"></label>                
                <label> <?php echo $lang->line("langAdminMain.Str_Item")?></label>               
                <label style="margin-left:20px"><?php echo $lang->line("langAdminMain.Str_Online")?> : </label>
                <label id="vd_online_count"></label>
                <label> <?php echo $lang->line("langAdminMain.Str_Item")?></label> 
                <label style="margin-left:20px"><?php echo $lang->line("langAdminMain.Str_Total")?> : </label>
                <label id="vd_total_count"></label>
                <label> <?php echo $lang->line("langAdminMain.Str_Item")?></label>
                <label style="margin-left:20px"><?php echo $lang->line("langAdminMain.Str_Suspend")?> : </label>
                <label id="vd_suspend_count"></label>
                <label> <?php echo $lang->line("langAdminMain.Str_Item")?></label>
            </div>
        </div>    
    </div>
    
    <!--
        ++++++++++++++++++++++++++++++
        +		Log		 +
        ++++++++++++++++++++++++++++++
    -->    
    <div id = "Div_LayoutZone_SysCfg_Log" class = "Div_Design_Layout" >      
        <div id = "Div_SysCfg_Log" class = "Div_MainContain" >
            <div id="div_log_select" style="height:40px;width:1150px;margin-left: auto;margin-right: auto">
                <div style="position:relative;top:15px">
                    <label id="label_log_level"><?php echo $lang->line("langAdminMain.Str_Level")?> :</label>
                    <div id="div_select_log_level">           
                        <select id="select_log_level">                            
                            <option value='0'>All</option>
                            <option value='3'>Error</option>
                            <option value='2'>Warning</option>
                            <option value='1'>Information</option>                                
                        </select>        
                    </div>
                    <label id="label_log_type"><?php echo $lang->line("langAdminMain.Str_Type")?> :</label>
                    <div id="div_select_log_type">           
                        <select id="select_log_type">                            
                            <option value='0'>All</option>
                            <option value='6'>System</option>                            
                            <option value='3'>RAID</option>                         
                            <option value='4'>User</option>
                            <option value='5'>VD</option>  
                             <?php
                                if($outputUIConfig['APPluse'] == 1)                          
                                    echo "<option value='8'>Schedule</option><option value='9'>Job</option>";
                            ?>
                            <option value='10'>Snapshot</option>
                            <?php
                                if($outputUIConfig['APPluse'] == 1)
                                    echo "<option value='12'>Backup</option>";
                            ?>
                        </select>        
                    </div>
                    <a href="download_log.php?AccessKey=<?php echo $AccessKey;?>" style="position :absolute;right: 0px;text-decoration: underline;" download><?php echo $lang->line("langAdminMain.Str_Click_Download_Log")?></a>
                </div>                
            </div>            
            <div id="div_log_scroll" style="margin:10px;margin-bottom: 4px;overflow-x: auto;overflow-y: auto"> 
                <!--  畫面調整 2017.02.03 william  -->
                <div id="div_log_table" style="position: relative;width: 99%;margin-left: auto;margin-right: auto;border: 2px solid #000;border-top-left-radius: 5px;border-top-right-radius: 5px;border-bottom-left-radius: 5px;border-bottom-right-radius: 5px;">                                            
                    <div id="log_RowHeader" class = "Div_VMRow VM_RowHeader" style="border-top-left-radius: 0px;border-top-right-radius: 0px;overflow: hidden;position: relative;"> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                        <div id="log_RowHeader_inner"  style="height: 100%;width: 100%;overflow-x: auto;overflow-y: hidden;position: absolute;padding-bottom: 20px;">
                            <div id="logitem" class="LogList_ItemNo AbsoluteLayout RowField">                                        
                                <div class="div_RowHeader">
                                    <H4 class="H4_RowHeader" id="LogList_SN_1"><?php echo $lang->line("langAdminMain.Str_SN")?></H4>
                                </div>
                            </div>
                            <div id="loglevel" class="LogList_Level AbsoluteLayout RowField LogHeader">
                                <div class="div_RowHeader">
                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.Str_Level")?>&nbsp;</H4>
                                    <label id="arrowloglevel" class="label_log_arrow label_RowHeader"></label>
                                </div>
                            </div>    
                            <div id="logtype" class="LogList_Type AbsoluteLayout RowField LogHeader">
                                <div class="div_RowHeader">
                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.Str_Type")?>&nbsp;</H4>
                                    <label id="arrowlogtype" class="label_log_arrow label_RowHeader"></label>
                                </div>
                            </div>
                            <div id="logtime"  class="LogList_Time AbsoluteLayout RowField LogHeader">
                                <div class="div_RowHeader">
                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.System_Time")?>&nbsp;</H4>
                                    <label id="arrowlogtime" class="label_log_arrow label_RowHeader">&#9660;</label>
                                </div>
                            </div>
                            <div id="logcode" class = "LogList_EventCode AbsoluteLayout RowField LogHeader">
                                <div class="div_RowHeader">
                                    <H4 id="logcode" class="H4_RowHeader"><?php echo $lang->line("langAdminMain.Str_Event_Code")?>&nbsp;</H4>
                                    <label id="arrowlogcode" class="label_log_arrow label_RowHeader"></label>
                                </div>
                            </div> 
                            <div id="logcontent" class = "LogList_Content AbsoluteLayout RowField">
                                <div class="div_RowHeader">
                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.Str_Content")?>&nbsp;</H4>
                                    <label id="arrowlogcode" class="label_log_arrow label_RowHeader"></label>                                
                                </div>
                            </div>                          
                            <div id="logriser" class = "LogList_User AbsoluteLayout RowField LogHeader">
                                <div class="div_RowHeader">
                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.Str_User")?>&nbsp;</H4>
                                    <label id="arrowlogriser" class="label_log_arrow label_RowHeader"></label>
                                </div>
                            </div>                       
                            <div id="logsrcip" class = "LogList_SrcIP AbsoluteLayout RowField LogHeader">
                                <div class="div_RowHeader">
                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.Str_Source_IP")?>&nbsp;</H4>
                                    <label id="arrowlogsrcip" class="label_log_arrow label_RowHeader"></label>
                                </div>
                            </div>
                        </div>                     
                    </div>      
                    <div id = "Div_VM_Log_List" style="font-size: 12px; width: 100%; overflow-y: auto;"  onscroll="document.getElementById('log_RowHeader_inner').scrollLeft = this.scrollLeft;">                        
                    </div> 
                </div>    
                
            </div>            
            <div id="div_log_page" style="height:20px;width:1150px;margin-left: auto;margin-right: auto">
                <div style="position:relative">
                    <a id="btn_page_previous" class="log_btn" title="<?php echo $lang->line("langAdminMain.SystemLogPagePrevious")?>" style="position:absolute;left: 8px;height: 28.5px;line-height: 28px;">&lt;</a> <!-- /* SnapShot&Schedule&Backup  2017.01.17 william */-->
                    <div id="div_select_log">           
                        <select id="select_log_page">                            
                            <option value='1'>1</option>                            
                        </select>        
                    </div>
                    <div style="position:absolute;left: 116px">
                        <label id="label_last_page" style="font-size:14px;line-height: 28px;">/1</label>                                         
                        <a id="btn_page_next" class="log_btn" title="<?php echo $lang->line("langAdminMain.SystemLogPageNext")?>" style="height: 28.5px;line-height: 28px;">&gt;</a> <!-- /* SnapShot&Schedule&Backup  2017.01.17 william */-->
                        <a id="btn_page_refresh" class="log_btn" title="<?php echo $lang->line("langAdminMain.SystemLogRefresh")?>" style="height: 28.5px;line-height: 28px;">&#8635;</a> <!-- /* SnapShot&Schedule&Backup  2017.01.17 william */-->                                                             
                    </div>
                    <label id="label_log_display" style="position: absolute;right:11px"></label>
                </div>
            </div>
        </div>
    </div>  
        
    <!--
        ++++++++++++++++++++++++++++++
        +		Performance Manager		 +
        ++++++++++++++++++++++++++++++
    -->
    <div id = "Div_LayoutZone_SysCfg_SysInfo" class = "Div_Design_Layout" >        
        <div id = "Div_SysCfg_SysInfo" class = "Div_MainContain" >          
            <div id="tab-performance-container" class='tab-container' style="margin: 20px;position: relative;">
                <!-- 新式樣修改(藍黃)新增重新整理功能 2016.12.12 william -->
                <a id='btn_refresh_performance' href="#" class="btn-jquery large-btn" style="position: absolute;top: 0%;right: 0%;"><?php echo $lang->line("langAdminMain.Btn_Refresh")?></a>

                <ul class='etabs'>                    
                    <li class='tab'><a href="#tabs1-info"><?php echo $lang->line("langAdminMain.Str_Information")?></a></li>                       
                    <li class='tab'><a href="#tabs1-cpu"><?php echo $lang->line("langAdminMain.Str_Performance")?></a></li>                       
                </ul>
                <div class='panel-container'>      
                    <div id="tabs1-info">  
                        <div id="div_info" style="width: 100%;height: 100%;position: relative;font-size: 12px">  <!-- 修改 width: 1000px;height: 320px; 2017.03.03 william -->
                            <div style="width: 100%;height: 100%;overflow:auto;position: absolute;">  <!-- 修改 height:300px; 2017.03.03 william -->
                                <div class="div_cpu_ram_title">
                                    <label class="label_cpu_ram_title"><?php echo $lang->line("langAdminMain.Str_CPUInfo")?></label>                                    
                                </div>      
                                <div style="height: 10px"></div>
                                <div>
                                    <div style="position: relative;left: 30px;width: 420px">
                                        <div>
                                            <label class="LabelCPURAMHead" style="font-size: 14px;color: #044de1;"><?php echo $lang->line("langAdminMain.Str_CPU")?></label>
                                            <label id="label_cpu" style="font-size: 14px;"></label>
                                            <label id="label_cpu_count" style="font-size: 14px;color:#044de1;font-weight: bolder"></label> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->
                                        </div>
                                        <div style="height: 10px"></div>
                                        <div>
                                            <label class="LabelCPURAMHead" style="font-size: 14px;color: #044de1;"><?php echo $lang->line("langAdminMain.Str_CPU_CR")?></label>
                                            <label id="label_cpu_clock" style="font-size: 14px;"></label>
                                        </div>
                                        <div style="height: 10px"></div>
                                        <div>
                                            <label class="LabelCPURAMHead" style="font-size: 14px;color: #044de1;"><?php echo $lang->line("langAdminMain.Str_CPU_Cores")?></label>
                                            <label id="label_cpu_cores" style="font-size: 14px;"></label>
                                        </div>
                                    </div>
                                </div>
                                <div style="height: 20px"></div>                         
                                <div class="div_cpu_ram_title_line">            
                                    <label class="label_cpu_ram_title"><?php echo $lang->line("langAdminMain.Str_RAMInfo")?></label>
                                </div>                                    
                                <div id="div_ram_info" style="position: absolute;left: 480px;top: 27px;width: 300px;height:400px;overflow-y:auto;overflow-x: hidden">
                                                                     
                                </div>                                
                                <div style="height: 10px"></div>                         
                                <div class="div_nic_title">            
                                    <label class="label_cpu_ram_title"><?php echo $lang->line("langAdminMain.Str_NetworkInfo")?></label>
                                </div>                
                                <div style="position: absolute;left: 810px;top: 27px;width: 300px">
                                    <div id="div_nic_state" style="position: relative;left: 30px">                                        
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="tabs1-cpu"> <!-- 修改 2017.03.03 william -->
                        <div style="width: 100%;height: 100%;position: relative;font-size: 12px"> 
                            <div id="div_cpu_ram" style="width: 100%;height: 100%;position: absolute;font-size: 12px;overflow:auto;">                            
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>          

<!--
        ++++++++++++++++++++++++++++++
        +		User Managerment     +
        ++++++++++++++++++++++++++++++
    -->    
    <div id = "Div_LayoutZone_SysCfg_UserManagerment" class = "Div_Design_Layout" >  
        <div id = "Div_SysCfg_UserManagerment" class = "Div_MainContain" >
            <div id="tab-usermanagerment-container" class='tab-container' style="margin: 20px">
                <ul class='etabs'>                    
                    <li class='tab'><a href="#tabs1-user_list"><?php echo $lang->line("langAdminMain.UserMgt_List")?></a></li>                     
                </ul>            
                <div class='panel-container'>
                    <div id="tabs1-user_list">
                        <div class = "Div_RelativeLayout" >
                            <!-- btn -->
                            <div style="width:99%;height: 30px;display: block;margin-left: auto;margin-right: auto"> 
                                <div style="position: relative">
                                    <a id='btn_create_user_dialog' class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.H4_BtnRaidCreate")?><?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo '';else echo ' ';?><?php echo $lang->line("langAdminMain.User")?></a>                                    
                                    <a id='btn_modify_user' class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Str_Modify")?><?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo '';else echo ' ';?><?php echo $lang->line("langAdminMain.User")?></a>
                                    <a id='btn_delete_user' class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.H4_BtnRaidDelete")?><?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo '';else echo ' ';?><?php echo $lang->line("langAdminMain.User")?></a>                                    
                                    <!--<a id="btn_import_user" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.H4_BtnImport")?></a>                                    -->
                                    <div style="position: absolute;right: 0px;top:0px">
                                        <a id='btn_refresh_userprofile' class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Refresh")?></a> <!-- btn_refresh_user -->
                                    </div>
                                </div>
                            </div> 
                            <div style="height:6px"></div>
                            <!-- content -->
                            <div class="listContainer">
                                <div class="listview_title">
                                    <div class="row_title">
                                        <div class="no_user" style="font-size: 16px;font-size: 18px; font-family: Times New Roman,PMingLiU,新細明體; font-weight: 900;"><?php echo $lang->line("langAdminMain.Item")?></div>                
                                        <div class="column_user" style="font-size: 16px;font-size: 18px; font-family: Times New Roman,PMingLiU,新細明體; font-weight: 900;"><?php echo $lang->line("langAdminMain.UserName")?></div>                
                                        <div class="userrnable" style="font-size: 16px;font-size: 18px; font-family: Times New Roman,PMingLiU,新細明體; font-weight: 900;"><?php echo $lang->line("langAdminMain.State")?></div>
                                    </div>                
                                </div>
                                <div id="listview_content"></div>                                                                                             
                            </div>
                            <!-- user infomation content Start -->
                            <div id="tab-user-container" class='tab-container'>
                                <ul class='etabs'>                    
                                    <!--<li class='tab'><a href="#tabs1-user_vm">雲主機</a></li>
                                    <li class='tab'><a href="#tabs1-user_borned">雲桌面</a></li>-->
                                    <li class='tab'><a href="#tabs1-user_disk"><?php echo $lang->line("langAdminMain.UserDisk")?></a></li>        
                                    <li class='tab'><a href="#tabs1-user_Enable"><?php echo $lang->line("langAdminMain.User_Status")?></a></li>
                                    <li class='tab'><a href="#tabs1-user_password"><?php echo $lang->line("langAdminMain.ResetPwd")?></a></li>                       
                                </ul>
                                <div class='panel-container'>
                                    <!--<div id="tabs1-user_vm">
                                        <div class = "Div_RelativeLayout">
                                        </div>
                                    </div>
                                    <div id="tabs1-user_borned">
                                        <div class = "Div_RelativeLayout">
                                        </div>
                                    </div>-->
                                    <div id="tabs1-user_disk">
                                        <div class = "Div_RelativeLayout">
                                            <div id="user_disk_info">
                                                <div class="user_disk_info_BtnDiv" style="right: 0px;top:0px">                                                    
                                                    <a id='btn_create_userprofile' class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.H4_BtnRaidCreate")?></a>
                                                    <a id='btn_modify_userprofile' class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Str_Modify")?></a>
                                                    <a id='btn_move_raidid' class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Move")?></a>
                                                    <a id='btn_delete_userprofile' class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.H4_BtnRaidDelete")?></a>
                                                    <!--<a id='btn_refresh_userprofile' class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Btn_Refresh")?></a>-->
                                                </div>
                                                <div style="height:6px"></div>
                                                <div style="height: 27px;">
                                                    <label class="LabelHead" style="float:  left;width: 130px;line-height: 27px;visibility: hidden;"><?php echo $lang->line("langAdminMain.EnableUser")?>:</label>
                                                    <input type="checkbox" class="iCheckUserEnable" id="_userenable_icheck" style="visibility: hidden;">                                                    
                                                    <a id='_btn_reset_userpassword' class="btn-jquery" style="font-size: smaller; visibility: hidden;"><?php echo $lang->line("langAdminMain.ResetPwd")?></a>
                                                </div>
                                                <div style="height:5px"></div>
                                                <div style="height: 27px;">
                                                    <label class="LabelHead" style="float:  left;width: 130px;font-size: 16px;"><?php echo $lang->line("langAdminMain.UserName")?>:</label>
                                                    <div id="user_disk_info_username" style="font-size: 16px;"></div>
                                                </div>                                                  
                                                <div style="height:0px"></div>
                                                <div style="height: 27px;">
                                                    <label class="LabelHead" style="float:  left;width: 130px;font-size: 16px;"><?php echo $lang->line("langAdminMain.DiskSize")?></label>
                                                    <div id="user_disk_info_disksize" style="font-size: 16px;"></div>
                                                </div>                                                                                            
                                                <div style="height:0px"></div>
                                                <div style="height: 27px;">
                                                    <label class="LabelHead" style="float:  left;width: 130px;font-size: 16px;"><?php echo $lang->line("langAdminMain.DiskType")?></label>
                                                    <div id="user_disk_info_disktype" style="font-size: 16px;"></div>
                                                </div> 
                                                <div style="height:0px"></div>
                                                <div style="height: 27px;">
                                                    <label class="LabelHead" style="float:  left;width: 130px;font-size: 16px;">RAID ID:</label>
                                                    <div id="user_disk_info_raidid" style="font-size: 16px;"></div>
                                                </div>                                                 
                                                <div style="height:0px"></div>
                                                <div style="height: 27px;">
                                                    <label class="LabelHead" style="font-size: 16px;float:  left;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'width: 130px;';else echo 'width: 150px;';?>"><?php echo $lang->line("langAdminMain.BindVD")?></label>
                                                    <div id="user_disk_info_bindVD" style="font-size: 16px;"></div>
                                                </div>                                                
                                            </div>
                                        </div>
                                    </div>                                          
                                    <div id="tabs1-user_Enable" style="font-size: 12px">     
                                        <div class="div_fieldcontent" style="width: 480px;position: relative;margin-left: 0px;right: 15px;">
                                            <label style="font-size: 16px;display: inline-block;position: relative;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'left: 16px;'; else echo 'left: 16px;';?>"><?php echo $lang->line("langAdminMain.UserName").' : '?></label>                    
                                            <label id="label_user_status_name" style="font-size: 16px;display: inline-block;position: relative;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'left: 16px;'; else echo 'left: 16px;';?>"></label>                                            
                                            <div style="height:10px"></div>
                                            <label style="font-size: 16px;display: inline-block;position: relative;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'left: 16px;'; else echo 'left: 16px;';?>"><?php echo $lang->line("langAdminMain.User_Status").' : '?></label>                    
                                            <label id="label_user_status" style="font-size: 16px;display: inline-block;position: relative;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'left: 16px;'; else echo 'left: 16px;';?>"></label>
                                                <div style="height:10px"></div>
                                            <div style="position: absolute;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'left: 93px;';else echo 'left: 100px';?>">
                                                <a id="btn_enable_user" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Service_Enable")?></a>
                                                <a id="btn_disable_user" class="btn-jquery large-btn"><?php echo $lang->line("langAdminMain.Service_Disable")?></a>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="tabs1-user_password" style="font-size: 12px">     
                                        <div class="div_fieldcontent" style="width: 480px;position: relative">
                                            <!--<label  style="font-size: 16px;display: inline-block;font-family: Times New Roman,PMingLiU,新細明體;"><?php echo $lang->line("langAdminMain.System_ChgPWD").' : '?></label>                    -->
                                            <a id='btn_reset_userpassword' class="btn-jquery large-btn" style="position: relative;right: 40px;"><?php echo $lang->line("langAdminMain.ResetPwd")?></a>
                                        </div>
                                    </div>
                                </div>
                            </div>                                
                            <!-- user infomation content End -->
                        </div>                                        
                    </div>
                </div>
            </div>
        </div>
    </div>



<!--
    ++++++++++++++++++++++++++++++++++++++++++
    +		Making 設計用，執行階段看不到		 +
    ++++++++++++++++++++++++++++++++++++++++++
-->
    <div id = "Div_LayoutZone_MakingElement" class = "Div_Design_Layout" >
    
        <div id = "Div_WhiteBorder" class = "Div_MainContain">
        
            <div class = "Div_RelativeLayout" >
            
                <div class = "Div_AccountRow" >
                	
                    <!-- <input class = "UserList_ChkBox AbsoluteLayout SelectAll" type="checkbox" ></input>   -->
                    <H4 class = "UserList_UserAccount AbsoluteLayout " ><?php echo $lang->line("langAdminMain.UserList_UserAccount")?></H4>
                    <H4 class = "UserList_UserName AbsoluteLayout" ><?php echo $lang->line("langAdminMain.UserList_UserName")?></H4>
                    <H4 class = "UserList_UserEmail AbsoluteLayout" ><?php echo $lang->line("langAdminMain.UserList_UserEmail")?></H4>
                    <H4 class = "UserList_UserPrivilege AbsoluteLayout" ><?php echo $lang->line("langAdminMain.UserList_UserPrivilege")?></H4>
                    <H4 class = "UserList_UserCompany AbsoluteLayout" ><?php echo $lang->line("langAdminMain.UserList_UserCompany")?></H4>
                    <H4 class = "UserList_UserDepartment AbsoluteLayout" ><?php echo $lang->line("langAdminMain.UserList_UserDepartment")?></H4>
                    <H4 class = "UserList_UserTitle AbsoluteLayout" ><?php echo $lang->line("langAdminMain.UserList_UserTitle")?></H4>
                    <H4 class = "UserList_UserCapacity AbsoluteLayout" ><?php echo $lang->line("langAdminMain.UserList_UserCapacity")?></H4>
                    <img class = "Img_UserList_EditItem AbsoluteLayout" src = "img/Edit.png" title = "<?php echo $lang->line("langAdminMain.Img_UserList_EditItem")?>"/>
                    <img class = "Img_UserList_DeleteItem AbsoluteLayout" src = "img/Remove.png" title = "<?php echo $lang->line("langAdminMain.Img_UserList_DeleteItem")?>"/>                        
                </div>                      
            </div>   
            
            
            <div class = "Div_DiskRow RowHeader" >
            
                <div class = "Div_RelativeLayout" >
         
                    <H4 class = "DiskList_Slot AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.DiskList_Slot")?></H4>
                    <H4 class = "DiskList_Status AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.DiskList_Status")?></H4>
                    <H4 class = "DisktList_FreeCapacity AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.DisktList_FreeCapacity")?></H4>
                    <H4 class = "DisktList_Capacity AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.DisktList_Capacity")?></H4>
                    <H4 class = "DiskList_Vendor AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.DiskList_Vendor")?></H4>
                    <H4 class = "DiskList_Speed AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.DiskList_Speed")?></H4>
                    <H4 class = "DiskList_Model AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.DiskList_Model")?></H4>
                    <H4 class = "DiskList_FirmwareVer AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.DiskList_FirmwareVer")?></H4>
                    <H4 class = "DiskList_BtnInitial AbsoluteLayout RowField MenuButton" ><?php echo $lang->line("langAdminMain.DiskList_BtnInitial")?></H4>
                        
                </div> 

            </div>
               
        </div> 
                
    </div> 
 
<!--
	+++++++++++++++++++++++++++++++		對話框	+++++++++++++++++++++++++++++++
-->

    <div class = "Div_DialogBase" class = "Div_MainContain" >
            
        <div class = "Div_WaitAjax" title = "<?php echo $lang->line("langAdminMain.Div_WaitAjax")?>" >
            <label class = "Lbl_Dialog_WaitAjax" ></label>
            <img class = "Img_Dialog_WaitAjax" src = "img/AjaxLoader.gif" alt = "等待圖" />
        </div>
        
        <div class = "Div_Message" title = "<?php echo $lang->line("langAdminMain.Str_Information")?>" >
            <img class = "Img_Dialog_Message" src = "img/Exclamation.png" alt = "驚嘆號" />
            <label class = "Lbl_Dialog_Message" >要顯示的訊息</label>
        </div>
        
        <div class = "Div_QuestionYesNo" title = "<?php echo $lang->line("langAdminMain.Div_QuestionYesNo")?>" >
            <img class = "Img_Dialog_Message" src = "img/Exclamation.png" alt = "驚嘆號" />
            <label class = "Lbl_Dialog_Message" >要顯示的訊息</label>
        </div>
        
        <!-- Update Fw 確認對話框 -->
        <div class = "Div_UpdateFwYesNo" title = "<?php echo $lang->line("langAdminMain.Div_UpdateFwYesNo")?>" >
            <img class = "Img_Dialog_Message" src = "img/Exclamation.png" alt = "驚嘆號" />
            <label class = "Lbl_Dialog_UpdateFwYesNo_OldVer_Title" ><?php echo $lang->line("langAdminMain.Lbl_Dialog_UpdateFwYesNo_OldVer_Title")?></label>
            <label class = "Lbl_Dialog_UpdateFwYesNo_OldVer_Value" >0.99</label>
            <label class = "Lbl_Dialog_UpdateFwYesNo_NewVer_Title" ><?php echo $lang->line("langAdminMain.Lbl_Dialog_UpdateFwYesNo_NewVer_Title")?></label>
            <label class = "Lbl_Dialog_UpdateFwYesNo_NewVer_Value" >1.01</label>
        </div>
        
        <div class = "Div_CreateUser" title = "<?php echo $lang->line("langAdminMain.Div_CreateUser")?>" >

            <div class = "Div_CreateUserTitle">
            
                <div class = "Div_RelativeLayout" >
         
                    <H4 class = "H4_UserAdd_UserAccount TitleLayout" ><?php echo $lang->line("langAdminMain.UserList_UserAccount")?></H4>
                    <H4 class = "H4_UserAdd_UserName TitleLayout" ><?php echo $lang->line("langAdminMain.H4_UserAdd_UserName")?></H4>
                    <H4 class = "H4_UserAdd_UserLastName TitleLayout"><?php echo $lang->line("langAdminMain.H4_UserAdd_UserLastName")?></H4>
                    <H4 class = "H4_UserAdd_UserEmail TitleLayout" ><?php echo $lang->line("langAdminMain.UserList_UserEmail")?></H4>
                    <H4 class = "H4_UserAdd_UserPrivilege TitleLayout" ><?php echo $lang->line("langAdminMain.UserList_UserPrivilege")?></H4>
                    <H4 class = "H4_UserAdd_UserCompany TitleLayout" ><?php echo $lang->line("langAdminMain.UserList_UserCompany")?></H4>
                    <H4 class = "H4_UserAdd_UserDepartment TitleLayout" ><?php echo $lang->line("langAdminMain.UserList_UserDepartment")?></H4>
                    <H4 class = "H4_UserAdd_UserTitle TitleLayout" ><?php echo $lang->line("langAdminMain.UserList_UserTitle")?></H4>
                    <H4 class = "H4_UserAdd_UserCapacity TitleLayout" ><?php echo $lang->line("langAdminMain.UserList_UserCapacity")?></H4>
                    
                </div>
                
            </div>
                
                
            <div class = "Div_CreateUserInput">
            
                <div class = "Div_RelativeLayout" >
             
                    <input class = "Input_UserAdd_UserAccount InputLayout" placeholder="<?php echo $lang->line("langAdminMain.UserList_UserAccount")?>" ></input>
                    <input class = "Input_UserAdd_UserName InputLayout" placeholder="<?php echo $lang->line("langAdminMain.H4_UserAdd_UserName")?>" ></input>
                    <input class = "Input_UserAdd_UserLastName InputLayout" placeholder="<?php echo $lang->line("langAdminMain.H4_UserAdd_UserLastName")?>" ></input>
                    <input class = "Input_UserAdd_UserEmail InputLayout" placeholder="<?php echo $lang->line("langAdminMain.UserList_UserEmail")?>" ></input>
                    <select class = "Input_UserAdd_UserPrivilege ComboLayout" placeholder="<?php echo $lang->line("langAdminMain.UserList_UserPrivilege")?>" ></select>
                    <select class = "Input_UserAdd_UserCompany ComboLayout" placeholder="<?php echo $lang->line("langAdminMain.UserList_UserCompany")?>" ></select>
                    <select class = "Input_UserAdd_UserDepartment ComboLayout" placeholder="<?php echo $lang->line("langAdminMain.UserList_UserDepartment")?>" ></select>
                    <select class = "Input_UserAdd_UserTitle ComboLayout" placeholder="<?php echo $lang->line("langAdminMain.UserList_UserTitle")?>" ></select>
                    <input class = "Input_UserAdd_UserCapacity InputLayout" placeholder="<?php echo $lang->line("langAdminMain.UserList_UserCapacity")?>" ></input>
                    
                </div>
                
            </div>
            
            <H2 class = "H4_UserAdd_Result AbsoluteLayout RowField" > </H2>
        </div>
    </div>

    <div id="dialog_wait">
        <div id="dialog_msg" style="text-align: center;top:50%">Please Wait...</div>
    </div> 

    <div id="dialog_vm_message">
        <div id="dialog_vm_msg" style="top:50%">Please Wait...</div>
    </div> 

    <div id="dialog_vm_message_reboot">
        <div id="dialog_vm_msg_reboot" style="top:50%">Please Wait...</div>
    </div> 
    
    <div id="dialog_create_raid">
        <div>
            <div style="position: relative;height: 40px">
                <?php
                if($oLogin->isCeph)
                    echo '<label style="position: absolute;top:10px">Duplicate Number :</label><select id="combolevel" >                   
                    <option value="2">2</option>
                    <option value="3">3</option>
                </select>';
                else
                    // 2017.11.13 william 新增Raid擴充，建立RAID修改，說明：RAID1：等於2個；RAID5：3個以上；Single改成Single / JBOD：1個以上









                    echo '<label style="position: absolute;top:10px">'.$lang->line("langAdminMain.Str_RAID_Level").'</label> <select id="combolevel" >
                    <option value="jbod">Single Disk / JBOD</option>';
                    // if($oLogin->diskNumber > 2) {
                        echo '<option value="1">RAID 1</option>
                              <option value="5">RAID 5</option>
                              <option value="6">RAID 6</option>';
                        if(count($outputDisk) > 4)
                              echo '<option value="7">RAID 7</option>';
                        // }
                    echo '</select>';
                ?>
               
                <label style="position: absolute;left: 390px;top:8px" id="RAID_disk_comment"><?php echo $lang->line("langAdminMain.Str_Disk_Comment")?></label>
            </div>
            
        </div>
        <!-- <div style="position: relative;margin-top: 40px">
            <label style="top:10px"><?php //echo $lang->line("langAdminMain.Str_TP")?></label>
            <div style="position:absolute;left:170px;top: 0px">
                <input type="checkbox" id="iCheckTP">
                <label style="width:auto"><?php //echo $lang->line("langAdminMain.Service_Enable")?></label>
            </div>            
        </div> -->
       <!--  <div style="position: relative;margin-top: 5px">
            <label><?php //echo $lang->line("langAdminMain.Str_Disk_List")?></label>
        </div>         -->
        <div class="table-wrapper">
            <div class="table-scroll">
                <table id="table-disk-for-raid">
                    <thead>
                        <tr>
                            <th>
                                <div> <!-- 2017.11.17 william 建立RAID修改，Check Box錯誤修改 -->
                                    <div style="position:relative">
                                        <!--
                                        <span style="position:absolute;left:0px;white-space: nowrap" class="text"><?php echo $lang->line("langAdminMain.DiskList_Select")?></span>
                                        <div style="position:absolute;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'left: 58px;';else echo 'left: 50px;';?>">
                                            <input type="checkbox" class="iCheckBox_RAID_All_Disk">
                                        </div>
                                        -->
                                        <div>
                                            <input type="checkbox" class="iCheckBox_RAID_All_Disk">
                                        </div>  
                                    </div>
                                </div>
                            </th>
                            <th><span class="text"><?php echo $lang->line("langAdminMain.DiskList_Slot")?></span></th>
                            <th><span class="text"><?php echo $lang->line("langAdminMain.DisktList_Capacity")?></span></th>                
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>                
            </div>            
        </div>   
        
        <label id="RAID_hotspare_comment" style="position: relative;margin-top: 5px;color: red;display: none;top: 107px;"><?php echo $lang->line("langAdminMain.Str_Hotspare_Comment")?></label>
        <div style="position: absolute;bottom: 10px;right: 10px">                
            <a id="btn_create_raid" href="#" class="btn-create" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
            <a id="btn_close_create_raid" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
        </div>
    </div>
    
    <div id="dialog_create_vm" style="font-size:12px">
        <div style="height:40px">
            <label class="LabelHead">VD <?php echo $lang->line("langAdminMain.Str_Name_Dot")?></label>
            <input id="input_VMName" class="InputText" value="" type="text">
        </div>            
        <div style="height:40px">
            <label class="LabelHead"><?php echo $lang->line("langAdminMain.UserName")?> :</label>
            <input id="input_VM_User" maxlength="64" class="InputText" value="" type="text">
            <a id="btn_get_user" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.UserMgt_List")?></a>
        </div>
        <div style="height:40px">
            <div style="position:relative">
                <label style="position:absolute;top:7px;" class="LabelHead"><?php echo $lang->line("langAdminMain.Str_CPU_Cores")?></label>
                <div style="position:absolute;width: 200px;top:0px;left:210px">
                    <select id="combo_cpu_cores">
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="4">4</option>
                        <option value="8">8</option>
                        <option value="16">16</option>
                    </select>
                </div>
            </div>                
        </div>
        <!-- 新增CPU類型  2017.09.01 william -->
        <div style="height:40px">
            <div style="position:relative">
                <label style="position:absolute;top:7px;" class="LabelHead"><?php echo $lang->line("langAdminMain.Str_CPU_Type")?></label>
                <div style="position:absolute;width: 200px;top:0px;left:210px">
                    <select id="combo_cpu_type">
                        <option value="0"><?php echo $lang->line("langAdminMain.Str_CPU_full_virtual")?></option>
                        <option value="1"><?php echo $lang->line("langAdminMain.Str_CPU_Half_virtual")?></option>
                    </select>
                </div>
            </div>                
        </div>
        <div style="height:40px">
            <div style="position:relative">
                <label style="position:absolute;top:7px;" class="LabelHead"><?php echo $lang->line("langAdminMain.Str_CPU_CR")?></label>                    
                <div style="position:absolute;top:8px;left:215px">
                    <input type="checkbox" class="iCheckBoxCPULimit" id="icheckcpulimit">
                    <label style="cursor: pointer;padding-left: 5px;font-size: 14px;vertical-align: middle;" for="icheckcpulimit"><?php echo $lang->line("langAdminMain.Str_CPU_Limit")?></label>                
                </div>
                <div style="position:absolute;width: 280px;top:10px;left:295px">
                    <div id="slider-range-cpu-clock"></div>
                </div>
                <a href="#" style="color: white;position:absolute;left:605px;line-height: 17px;width: 17px;height: 17px;top:6px" class="log_btn btn_cpu_clock">&#45;</a>                    
                <a href="#" style="color: white;position:absolute;left:585px;line-height: 17px;width: 17px;height: 17px;top:6px" class="log_btn btn_cpu_clock add">&#43;</a>                                        
                <label id="now-cpu-clock" style="position:absolute;left:630px;top:8px"></label>
            </div>                
        </div>
        <div style="height:0px;display: none">
            <div style="position:relative">
                <label class="LabelHead"><?php echo $lang->line("langAdminMain.Str_Memory_Allocation_Dot")?></label>
                <div style="position: relative;padding-left: 212px;top:-15px">
                    <div style="position: absolute">
                        <input type="radio" id="radio-fix-create" name="radio-memory-allocate-create"  class="iCheckRadioMemoryAllocateCreate" value="1" checked>
                        <label class="LabelHead" style="cursor:pointer" for="radio-fix-create">Static</label>
                    </div>
                    <div style="position: absolute;margin-left: 105px">
                        <input type="radio" id="radio-dynamic-create" name="radio-memory-allocate-create"  class="iCheckRadioMemoryAllocateCreate" value="0">
                        <label class="LabelHead" style="cursor:pointer" for="radio-dynamic-create">Dynamic</label>
                    </div>                        
                </div>                    
            </div> 
        </div>
        <div style="height:40px">
            <div style="position:relative">
                <label class="LabelHead"><?php echo $lang->line("langAdminMain.Str_Memory_Dot")?></label>   
                <input id="input_VMMemory" class="InputText" value="" type="text">
                <label>GB</label>
            </div>
        </div>                   
        <div style="height:40px">
            <div style="position:relative">
                <label style="position:absolute;top:7px;" class="LabelHead">CD ROM :</label>
                <div id="div_install_iso" style="position:absolute;width: 200px;top:0px;left:210px">                        
                </div>
                <!--<label style="position:absolute;top:7px;left:410px">iso</label>-->
            </div>                
        </div>
        <div style="height:40px">
            <div style="position:relative">
                <label style="position:absolute;top:7px;" class="LabelHead">RAID ID :</label>
                <div id="raidid_createvd" style="position:absolute;width: 200px;top:0px;left:210px">
                </div>
            </div>                
        </div>        
        <div style="height:40px">
            <label class="LabelHead"><?php echo $lang->line("langAdminMain.Str_Boot_Disk")?></label>
            <input id="input_VMBootDiskSize"class="InputText" value="" type="text">
            <label>GB</label>
        </div>
        <div style="height:40px">
            <div style="position:relative">
                <label style="position:absolute;top:7px;" class="LabelHead"><?php echo $lang->line("langAdminMain.VD_USB_Type")?></label>
                <div style="position:absolute;width: 350px;top:10px;left:210px">
                    <div style="position: absolute">
                        <input type="radio" id="radio-usb-3" name="radio-usb"  class="iCheckRadioUSB" value="3" checked>
                        <label class="LabelHead" style="cursor:pointer" for="radio-usb-3">USB 3.0</label>
                    </div>
                    <div style="position: absolute;margin-left: 105px">
                        <input type="radio" id="radio-usb-2" name="radio-usb"  class="iCheckRadioUSB" value="2">
                        <label class="LabelHead" style="cursor:pointer" for="radio-usb-2">USB 2.0</label>
                    </div>          
                </div>
            </div>                
        </div>
        <div style="height:40px">
            <div style="position:relative">
                <label style="position:absolute;top:7px;" class="LabelHead"><?php echo $lang->line("langAdminMain.VD_USB_Redirection_Count")?></label>
                <div style="position:absolute;width: 200px;top:0px;left:210px">
                    <select id="combo_usb_redirect">
                        <option value="0">0</option>
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                    </select>
                </div>
            </div>                
        </div>
        <div style="height:40px">
            <div style="position:relative">
                <label style="position:absolute;top:7px;" class="LabelHead"><?php echo $lang->line("langAdminMain.VD_Sound")?> :</label>
                <div style="position:absolute;width: 200px;top:0px;left:210px">
                    <select id="combo_audio">
                        <option value="1">ich6</option>
                        <option value="2">ac97(for Windows XP)</option>            
                    </select>
                </div>
            </div>                
        </div>
        <div style="height:40px">
            <div style="position:relative">
                <label style="position:absolute;top:7px;" class="LabelHead"><?php echo $lang->line("langAdminMain.Str_NIC_Dot")?></label>
                <div style="position:absolute;width: 200px;top:0px;left:210px">
                    <select id="combo_nic">                            
                        <option value="1">1,000 Mb/s</option>
                        <option value="10">10,000 Mb/s</option>
                    </select>                        
                </div>
                <div style="position:absolute;width: 200px;top:0px;left:410px">
                    <select id="combo_vswitch">                            
                        <?php 
                            foreach ($ouputVswitch['Vswitchs'] as $key => $value) {
                                if(count($value['Devs']) > 0)
                                    echo "<option value=\"$key\">Virtual Switch $key</option>";
                            }
                        ?>                        
                    </select>                        
                </div>
                <!-- <label id="nic_warning" style="position:absolute;top:7px;left:415px;display: none"><?php //echo $lang->line("langAdminMain.Str_NIC_Warning_Install_Guest")?></label> -->
            </div>
        </div>     
        <div style="height:40px">
            <div style="position:relative">
                <label style="position:absolute;top:7px;" class="LabelHead"><?php echo $lang->line("langAdminMain.Str_Auto_Suspend")?></label>                    
                <div style="position:absolute;top:8px;left:215px">
                    <input type="checkbox" class="iCheckBoxAutoPause" id="icheckpause">
                    <label style="cursor: pointer;padding-left: 5px;font-size: 14px;vertical-align: middle;" for="icheckpause"><?php echo $lang->line("langAdminMain.Service_Enable")?></label>                
                </div>
            </div>
        </div>
        <div style="height:30px">
            <label class="LabelHead"><?php echo $lang->line("langAdminMain.Str_Password_Dot")?></label>
            <label>000000</label>
        </div> 
        <div style="height:40px">
            <label class="LabelHead"><?php echo $lang->line("langAdminMain.Str_Remark_Dot")?></label>
            <input id="input_VMDescription" maxlength="64" class="InputText" value="" type="text">
        </div>
        <!-- 新增快照層數  2017.11.22 william -->
        <div style="height:40px">
            <label class="LabelHead"><?php echo $lang->line("langAdminMain.Str_SSLayer")?></label>
            <input id="input_SSLayer" class="InputText" value="" type="text">
        </div>        
        <div style="height:40px">
            <label class="label_SSLayer_warning_1"></label>                
        </div>  
        <div style="position: absolute;bottom: -25px;right: 10px">                
            <a id="btn_create_vm" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
            <a id="btn_close_create_vm" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
        </div>
    </div>
               
    <div id="dialog_import_vm" style="font-size:12px">
        <div class="table-wrapper" style="width:520px">
            <div class="table-scroll" style="height:350px">
                <table id="table-import_vm">
                    <thead>
                        <tr>
                            <th style="width:50px"><span class="text"><?php echo $lang->line("langAdminMain.VMList_SN")?></span></th>
                            <th style="width:200px"><span class="text"><?php echo $lang->line("langAdminMain.VMList_Name")?></span></th>
                            <th style="width:120px"><span class="text">RAID ID</span></th>
                            <th><span class="text"><?php echo $lang->line("langAdminMain.List_Action")?></span></th>                
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td style="text-align:center">1</td>
                            <td style="text-align:center"><input style="width:300px;height:25px" type="text"></td>                                          
                            <td style="text-align:center"><a href="#" class="btn-vd-import" style="font-size: smaller"><?php echo $lang->line("langAdminMain.H4_BtnImport")?></a></td>
                        </tr>                        
                    </tbody>
                </table>                
            </div>            
        </div>    
        <div style="position: absolute;bottom: 10px;right: 10px">                            
            <a id="btn_close_import" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
        </div>
    </div>

    <div id="dialog_modify_vm" style="font-size:12px">
        <div id="tab-vm-modify-container" class='tab-container'>
            <ul class='etabs'>                    
                <li class='tab'><a href="#tabs1-vm-properties"><?php echo $lang->line("langAdminMain.Str_Properties")?></a></li>   
                <li class='tab'><a href="#tabs1-vm-disk"><?php echo $lang->line("langAdminMain.Str_Disk")?></a></li>
                <li class='tab'><a href="#tabs1-vm-nic"><?php echo $lang->line("langAdminMain.Str_NIC")?></a></li>                    
            </ul>
            <div class='panel-container'>                                        
                <div id="tabs1-vm-properties" style="font-size: 12px;height: 470px"> <!-- 新增CPU類型  2017.09.01 william 380 -> 407 -->
                    <div style="height:40px">
                        <label class="LabelHead"><?php echo $lang->line("langAdminMain.Str_Name_Dot")?></label>
                        <!--<label id="input_Mofify_VMName"></label>-->
                        <input id="input_Mofify_VMName" class="InputText" value="" type="text">
                    </div>                    
                    <div style="height:40px">
                        <div style="position:relative">
                            <label style="position:absolute;top:7px;" class="LabelHead"><?php echo $lang->line("langAdminMain.Str_CPU_Cores")?></label>
                            <div style="position:absolute;width: 200px;top:0px;left:210px">
                                <select id="combo_Modify_cpu_cores">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="4">4</option>
                                    <option value="8">8</option>
                                    <option value="16">16</option>
                                </select>
                            </div>
                        </div>                
                    </div>
                    <!-- 新增CPU類型  2017.09.01 william -->
                    <div style="height:40px">
                        <div style="position:relative">
                            <label style="position:absolute;top:7px;" class="LabelHead"><?php echo $lang->line("langAdminMain.Str_CPU_Type")?></label>
                            <div style="position:absolute;width: 200px;top:0px;left:210px">
                                <select id="combo_Modify_cpu_type">
                                    <option value="0"><?php echo $lang->line("langAdminMain.Str_CPU_full_virtual")?></option>
                                    <option value="1"><?php echo $lang->line("langAdminMain.Str_CPU_Half_virtual")?></option>
                                </select>
                            </div>
                        </div>                
                    </div>                    
                    <div style="height:40px">
                        <div style="position:relative">                            
                            <label style="position:absolute;top:7px;" class="LabelHead"><?php echo $lang->line("langAdminMain.Str_CPU_CR")?></label>                    
                            <div style="position:absolute;top:8px;left:215px">
                                <input type="checkbox" class="iCheckBoxCPULimit" id="icheckmodifycpulimit">
                                <label style="cursor: pointer;padding-left: 5px;font-size: 14px;vertical-align: middle;" for="icheckmodifycpulimit"><?php echo $lang->line("langAdminMain.Str_CPU_Limit")?></label>                
                            </div>
                            <div style="position:absolute;width: 250px;top:10px;left:295px">
                                <div id="slider-modify-range-cpu-clock"></div>
                            </div>
                            <a href="#" style="color: white;position:absolute;left:575px;line-height: 17px;width: 17px;height: 17px;top:6px" class="log_btn btn_cpu_clock modify">&#45;</a>                    
                            <a href="#" style="color: white;position:absolute;left:555px;line-height: 17px;width: 17px;height: 17px;top:6px" class="log_btn btn_cpu_clock add modify">&#43;</a>                                        
                            <label id="now-modify-cpu-clock" style="position:absolute;left:600px;top:7px"></label>
                        </div>                
                    </div>
                    <div style="height:0px;display:none">
                        <div style="position:relative">
                            <label class="LabelHead"><?php echo $lang->line("langAdminMain.Str_Memory_Allocation_Dot")?></label>
                            <div style="position: relative;padding-left: 212px;top:-15px">
                                <div style="position: absolute">
                                    <input type="radio" id="radio-fix" name="radio-memory-allocate"  class="iCheckRadioMemoryAllocate" value="1" checked>
                                    <label class="LabelHead" style="cursor:pointer" for="radio-fix">Static</label>
                                </div>
                                <div style="position: absolute;margin-left: 105px">
                                    <input type="radio" id="radio-dynamic" name="radio-memory-allocate"  class="iCheckRadioMemoryAllocate" value="0">
                                    <label class="LabelHead" style="cursor:pointer" for="radio-dynamic">Dynamic</label>
                                </div>                        
                            </div>                    
                        </div> 
                    </div>
                    <div style="height:40px">
                        <div style="position:relative">
                            <label class="LabelHead"><?php echo $lang->line("langAdminMain.Str_Memory_Dot")?></label>   
                            <input id="input_Modify_VMMemory" class="InputText" value="" type="text">
                            <label>GB</label>                   
                        </div>
                    </div>       
                    <div style="height:40px">
                        <div style="position:relative">
                            <label style="position:absolute;top:7px;" class="LabelHead"><?php echo $lang->line("langAdminMain.VD_USB_Type")?></label>
                            <div style="position:absolute;width: 350px;top:10px;left:210px">
                                <div style="position: absolute">
                                    <input type="radio" id="radio-usb-modify-3" name="radio-usb-modify"  class="iCheckRadioModifyUSB" value="3">
                                    <label class="LabelHead" style="cursor:pointer" for="radio-usb-modify-3">USB 3.0</label>
                                </div>
                                <div style="position: absolute;margin-left: 105px">
                                    <input type="radio" id="radio-usb-modify-2" name="radio-usb-modify"  class="iCheckRadioModifyUSB" value="2">
                                    <label class="LabelHead" style="cursor:pointer" for="radio-usb-modify-2">USB 2.0</label>
                                </div>          
                            </div>
                        </div>                
                    </div>
                    <div style="height:40px">
                        <div style="position:relative">
                            <label style="position:absolute;top:7px;" class="LabelHead"><?php echo $lang->line("langAdminMain.VD_USB_Redirection_Count")?></label>
                            <div style="position:absolute;width: 200px;top:0px;left:210px">
                                <select id="combo_modify_usb_redirect">
                                    <option value="0">0</option>
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                </select>
                            </div>
                        </div>                
                    </div>
                    <div style="height:40px">
                        <div style="position:relative">
                            <label style="position:absolute;top:8px;" class="LabelHead">CD ROM :</label>
                            <div id="div_modify_install_iso" style="position:absolute;width: 200px;top:0px;left:210px">                        
                            </div>
                        </div>                
                    </div>  
                    <div style="height:40px">
                        <div style="position:relative">
                            <label style="position:absolute;top:7px;" class="LabelHead"><?php echo $lang->line("langAdminMain.VD_Sound")?></label>
                            <div style="position:absolute;width: 200px;top:0px;left:210px">
                                <select id="combo_Modify_audio">
                                    <option value="1">ich6</option>
                                    <option value="2">ac97(for Windows XP)</option>                                    
                                </select>
                            </div>
                        </div>                
                    </div>
                    <div <?php if(!$suspend){echo ' style="height:0px;display:none"'; }else{echo ' style="height:40px"';} ?>>
                        <div style="position:relative">
                            <label style="position:absolute;top:7px;" class="LabelHead"><?php echo $lang->line("langAdminMain.Str_Auto_Suspend")?></label>                    
                            <div style="position:absolute;top:8px;left:215px">
                                <input type="checkbox" class="iCheckBoxModifyAutoPause" id="icheckmodifypause">
                                <label style="cursor: pointer;padding-left: 5px;font-size: 14px;vertical-align: middle;" for="icheckmodifypause"><?php echo $lang->line("langAdminMain.Service_Enable")?></label>                
                            </div>
                        </div>
                    </div>
                    <div style="height:40px">
                        <label class="LabelHead"><?php echo $lang->line("langAdminMain.Str_Remark_Dot")?></label>
                        <input id="input_VMModifyDescription" maxlength="64" class="InputText" value="" type="text">
                    </div>
                    <div style="height:40px">
                        <label class="LabelHead"><?php echo $lang->line("langAdminMain.UserName")?></label>
                        <input id="input_VD_Assign_User" maxlength="64" class="InputText" value="" type="text">
                        <a id="btn_VMModify_get_user" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.UserMgt_List")?></a>
                    </div>
                    <div style="height:40px">
                        <div style="position:relative">
                            <a id="btn_modify_vm_info" class="btn-jquery large-btn" style="position: absolute;right: 0px;font-size: 14px;top: 0px;"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a> <!-- 新增CPU類型  2017.09.01 william 新增top -->
                        </div>
                    </div>
                </div>
                <div id="tabs1-vm-disk" style="font-size: 12px;height: 407px"> 
                    <div style="height:40px">
                        <div style="position: relative">                                                       
                            <label class="LabelHead" style="width: 140px;cursor:pointer"><?php echo $lang->line("langAdminMain.Str_Create_New_Disk")?></label>
                            
                            <input id="input_VMAddDiskSize" style="width:100px" class="InputText" value="" type="text">
                            <label>GB</label>  
                            <div style="display:  inline-block;padding: 0 2px;font-size: 12px;font-weight: 900;color: red;">/</div>
                            <!-- 新增磁碟類型  2017.09.01 william -->
                                <select id="combo_disk_type">
                                    <option value="0">IDE</option>
                                    <option value="1">SCSI</option>
                                    <option value="2">VirtIO</option>
                                </select>
                            <div style="display:  inline-block;padding: 0 2px;font-size: 12px;font-weight: 900;color: red;">/</div>    
                            <label> RAID ID : </label>      
                            <div id="raidid_modifyvd" style="display: inline-block;width: 123px;">
                            </div>                                
                            <a id="btn_add_vm_new_disk" class="btn-jquery large-btn" style="font-size: 14px;position: absolute;margin-top: 1px;"><?php echo $lang->line("langAdminMain.Str_Add")?></a>
                        </div>
                    </div>     
                    <div style="height:40px">        
                        <a class="btn-jquery btn_refresh_vm_disk" style="font-size: 14px;"><?php echo $lang->line("langAdminMain.Btn_Refresh")?></a>
                        <!-- 2018.01.08 william 新增磁碟擴充功能，建立擴充視窗 -->    
                        <label class="disk_type_warning" style="font-size: 12px;font-weight: 900;color: red;position: relative;left: 55px;"></label>
                    </div>
                    <div style="width:625px;height:10px;border-top: dashed 1px rgba(0, 0, 0, .5)"></div>
                    <div id="div_vm_disk_list"></div>
                </div>
                <div id="tabs1-vm-nic" style="font-size: 12px;height: 407px"> <!-- 新增CPU類型  2017.09.01 william 350 -> 407 -->
                    <div style="height: 40px">
                        <div style="position:relative">
                            <label style="position:absolute;top:8px" class="LabelHead"><?php echo $lang->line("langAdminMain.Str_NIC_Speed")?></label>
                            <div style="position:absolute;width: 200px;top:0px;left:203px">
                                <select id="combo_add_nic">
                                    <option value="1">1,000 Mb/s</option>
                                    <option value="10">10,000 Mb/s</option>                                    
                                </select>
                            </div>
                            <div style="position:absolute;width: 200px;top:0px;left:410px">
                                <select id="combo_add_vswitch">                            
                                    <?php 
                                        foreach ($ouputVswitch['Vswitchs'] as $key => $value) {
                                            if(count($value['Devs']) > 0)
                                                echo "<option value=\"$key\">Virtual Switch $key</option>";
                                        }
                                    ?>                        
                                </select>                        
                            </div>
                            <a id="btn_add_vm_nic" class="btn-jquery large-btn" style="position: absolute;top:2px;left: 620px;font-size: 14px;"><?php echo $lang->line("langAdminMain.Str_Add")?></a>
                        </div>
                    </div>
                    <div style="width:480px;height:10px;border-top: dashed 1px rgba(0, 0, 0, .5)"></div>
                    <div id="div_vm_nic_list">
                        
                    </div>
                </div>
            </div>
        </div>
    </div>   

    <div id="dialog_modify_vm_boot" style="font-size:12px">
        <div style="height:40px">
            <div style="position:relative">
                <label style="position:absolute;top:8px;" class="LabelHead">CD ROM :</label>
                <div id="div_install_iso_boot" style="position:absolute;width: 200px;top:0px;left:150px">                        
                </div>
            </div>                
        </div> 
        <div style="position: absolute;bottom: 10px;right: 10px">                
            <a id="btn_modify_vm_boot" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
            <a id="btn_close_modify_vm_boot" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
        </div>
    </div>

    <div id="dialog_vm_info" style="font-size:12px">
        <div id="tab-vm-info-container" class='tab-container'>
            <ul class='etabs'>                    
                <li class='tab'><a href="#tabs1-vm-info-properties"><?php echo $lang->line("langAdminMain.Str_Properties")?></a></li>   
                <li class='tab'><a href="#tabs1-vm-info-disk"><?php echo $lang->line("langAdminMain.Str_Disk")?></a></li>
                <li class='tab'><a href="#tabs1-vm-info-nic"><?php echo $lang->line("langAdminMain.Str_NIC")?></a></li>                    
            </ul>
            <div class='panel-container'>                                        
                <div id="tabs1-vm-info-properties" style="font-size: 12px;height: 350px">
                    <div style="height:30px">
                        <label class="LabelHead"><?php echo $lang->line("langAdminMain.Str_Name_Dot")?></label>
                        <label id="label_info_VMName"></label>
                    </div>                    
                    <div style="height:30px">                        
                        <label class="LabelHead"><?php echo $lang->line("langAdminMain.Str_CPU_Cores")?></label>                                                     
                        <label id="label_info_CPU_Cores"></label>
                    </div>
                    <!-- 新增CPU類型  2017.09.01 william -->
                    <div style="height:30px">                        
                        <label class="LabelHead"><?php echo $lang->line("langAdminMain.Str_CPU_Type")?></label>                                                     
                        <label id="label_info_CPU_Type"></label>
                    </div>
                    <div style="height:30px">                        
                        <label class="LabelHead"><?php echo $lang->line("langAdminMain.Str_CPU_CR")?></label>                                                     
                        <label id="label_info_CPU_Clock"></label>
                    </div>
                    <div style="height:0px;display: none">                        
                        <label class="LabelHead"><?php echo $lang->line("langAdminMain.Str_Memory_Allocation_Dot")?></label>   
                        <label id="label_info_VMMemoryAllocation"></label>                                            
                    </div>    
                    <div style="height:30px">                        
                        <label class="LabelHead"><?php echo $lang->line("langAdminMain.Str_Memory_Dot")?></label>   
                        <label id="label_info_VMMemory"></label>
                        <label>GB</label>                                           
                    </div>      
                    <div style="height:30px">                        
                        <label class="LabelHead"><?php echo $lang->line("langAdminMain.VD_USB_Type")?></label>   
                        <label id="label_info_USBType"></label>                                          
                    </div> 
                    <div style="height:30px">                        
                        <label class="LabelHead"><?php echo $lang->line("langAdminMain.VD_USB_Redirection_Count")?></label>   
                        <label id="label_info_USBRediect"></label>                                          
                    </div>     
                    <div style="height:30px">                        
                        <label class="LabelHead">CD ROM :</label>
                        <label id="label_info_install_iso"></label>                                                                                            
                        <!--<label>iso</label>-->
                    </div>  
                    <div style="height:30px">                        
                        <label class="LabelHead"><?php echo $lang->line("langAdminMain.VD_Sound")?> :</label>
                        <label id="label_info_audio"></label>                                                                                                                    
                    </div>  
                    <div <?php if(!$suspend){echo ' style="height:0px;display:none"'; }else{echo ' style="height:30px"';} ?>>                        
                        <label class="LabelHead"><?php echo $lang->line("langAdminMain.Str_Auto_Suspend")?> :</label>
                        <label id="label_info_auto_suspend"></label>                                                                                                                    
                    </div>
                    <div style="height:30px">
                        <label class="LabelHead"><?php echo $lang->line("langAdminMain.Str_Remark_Dot")?></label>
                        <label id="label_info_desc"></label>
                    </div>
                </div>
                <div id="tabs1-vm-info-disk" style="font-size: 12px;height: 350px">   
                    <div style="height:40px">        
                        <a class="btn-jquery btn_refresh_vm_disk" style="font-size: 14px;"><?php echo $lang->line("langAdminMain.Btn_Refresh")?></a>
                    </div>              
                    <div style="width:480px;height:10px;border-top: dashed 1px rgba(0, 0, 0, .5)"></div>                       
                    <div id="div_vm_info_disk_list">
                        
                    </div>
                </div>
                <div id="tabs1-vm-info-nic" style="font-size: 12px;height: 350px">                    
                    <div id="div_vm_info_nic_list">
                        
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <div id="dialog_vm_clone" style="font-size:12px">
        <div style="height: 0px;display: none">
            <label class="LabelHead" style="white-space: nowrap;position: absolute;top:8px">Type :</label>
            <div>
                <div style="position: absolute;top:8px;left:70px">    
                    <div style="position: relative">
                        <input type="radio" id="radio-normal-clone" name="radio-clone-type"  class="iCheckRadioClone" value="0" checked>
                        <label class="LabelHead" style="position: absolute;top:1px;left: 30px;cursor:pointer" for="radio-normal-clone">Normal</label>
                    </div>
                </div>
                <div style="position: absolute;top:8px;left:160px">
                    <div style="position: relative">
                        <input type="radio" id="radio-seed-clone" name="radio-clone-type"  class="iCheckRadioClone" value="1">
                        <label class="LabelHead" style="position: absolute;top:1px;left: 30px;cursor:pointer" for="radio-seed-clone">Seed</label>
                    </div>
                </div>
                <!--2017.11.22 william 雲桌面轉移至雲主機-->
                <div style="position: absolute;top:8px;left:250px">
                    <div style="position: relative">
                        <input type="radio" id="radio-uservd-clone" name="radio-clone-type"  class="iCheckRadioClone" value="3">
                        <label class="LabelHead" style="position: absolute;top:1px;left: 30px;cursor:pointer" for="radio-uservd-clone">User VD</label>
                    </div>
                </div>
            </div>
        </div>
        <div>
            <label ><?php echo $lang->line("langAdminMain.Str_Source_Name")?></label>
            <label id="label_clone_source"></label>
        </div>        
        <div style="height: 10px"></div>        
        <div id="usernameforClone" style="height:40px">
            <label><?php echo $lang->line("langAdminMain.UserName")?> :</label>
            <input id="input_VM_User_forClone" maxlength="64" class="InputText" value="" type="text" style=" <?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'margin-left: 10px;';else echo 'margin-left: 20px;';?> ">
            <a id="btn_VD_get_user" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.UserMgt_List")?></a>
        </div>         
        <div style="height: 10px"></div>
        <div id="div_seed_dest">
            <label id="sourcenameforClone" style="white-space: nowrap"><?php echo $lang->line("langAdminMain.Str_Seed_Dest_Name")?></label>
            <input id="input_seed_dest_name" class="InputText" value="" name="" type="text" style=" <?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'margin-left: 10px;';else echo 'margin-left: 10px;';?> ">            
        </div>        
        <div class="div_clone_dest">            
            <label style="white-space: nowrap"><?php echo $lang->line("langAdminMain.Str_Dest_Name_ex")?></label>            
        </div>        
        <div style="height: 5px"></div>
        <div class="div_clone_dest" style="width:  100%;height: 72%;">
            <!--<textarea id="textarea_dest_name" style="width:300px;height:100px;"></textarea>
            <a id="btn_clone_getuserlist" href="#" class="btn-jquery" style="font-size: smaller;position:  relative;bottom: 8px;"><?php echo $lang->line("langAdminMain.UserMgt_List")?></a>-->
                <div class="listContainer_cb" style="width:  100%;height: 100%;">
                    <div class="listview_title_cb listview_title_clone">
                        <div class="row_title_cb">
                            <div class="no_user_cb"><?php echo $lang->line("langAdminMain.Item")?></div>                
                            <div class="select_user_cb">
                                <input type="checkbox" class="iCheckBoxUserAllSelect">
                            </div>
                            <div class="column_user_cb"><?php echo $lang->line("langAdminMain.UserName")?></div>                                                                
                            <div class="column_vd_cb"><?php echo $lang->line("langAdminMain.Str_VD")?><?php echo $lang->line("langAdminMain.VMList_Name")?></div>                            
                        </div>                
                    </div>
                    <div id="listview_content_cb_clone"></div>                                                                                             
                </div>
                <textarea id="textarea_dest_name" style="width:300px;height:100px;display:none;"></textarea>
        </div>        
        <div style="height: 10px"></div>
        <div>
            <label ><?php echo $lang->line("langAdminMain.Str_Remark_Dot")?></label>
            <input id="input_clone_VMDescription" maxlength="64" class="InputText" value="" type="text" style=" <?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'margin-left: 34px;';else echo 'margin-left: 32px;';?> ">
        </div>
        <div style="position: absolute;bottom: 10px;right: 10px">                
            <a id="btn_clone_vm" href="#" class="btn-create" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
            <a id="btn_close_clone_vm" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
        </div>
    </div>
    
    <div id="dialog_vm_create_vdi" style="font-size:12px">
        <div style="height:25px">
            <label><?php echo $lang->line("langAdminMain.Str_Source_Name")?></label>
            <label id="label_snapshot_source"></label>
        </div>        
        <div style="height: 0px"></div>
        <div style="height:20px">            
            <label class="LabelHead" style="white-space: nowrap"><?php echo $lang->line("langAdminMain.Str_Dest_Name_ex")?></label>
        </div>        
        <div style="width:  100%;height: 70%;">
            <!--<textarea id="textarea_snapshot_dest_name" style="width:300px;height:100px;"></textarea>
            <a id="btn_BornUser_getuserlist" href="#" class="btn-jquery" style="font-size: smaller;position:  relative;bottom: 8px;"><?php echo $lang->line("langAdminMain.UserMgt_List")?></a>-->
                <div class="listContainer_cb" style="width:  100%;height: 100%;">
                    <div class="listview_title_cb listview_title_born">
                        <div class="row_title_cb">
                            <div class="no_user_cb"><?php echo $lang->line("langAdminMain.Item")?></div>                
                            <div class="select_user_cb">
                                <input type="checkbox" class="iCheckBoxBornUserAllSelect">
                            </div>
                            <div class="column_user_cb"><?php echo $lang->line("langAdminMain.UserName")?></div>                                                                
                            <div class="column_vd_cb"><?php echo $lang->line("langAdminMain.Str_VD")?><?php echo $lang->line("langAdminMain.VMList_Name")?></div>                            
                        </div>                
                    </div>
                    <div id="listview_content_cb_bornuser"></div>                                                                                             
                </div>
                <textarea id="textarea_snapshot_dest_name" style="width:300px;height:100px;display:none;"></textarea>

        </div>
        <div style="height: 10px"></div>
        <div style="height:40px; position: relative; top: 31px; ">
            <label><?php echo $lang->line("langAdminMain.Str_Remark_Dot")?></label>
            <input id="input_create_vdi_VMDescription" maxlength="64" class="InputText" value="" type="text">
        </div>
        <div style="position: absolute;bottom: 10px;right: 10px">                
            <a id="btn_create_vdi" href="#" class="btn-create" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
            <a id="btn_close_create_vdi" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
        </div>
    </div>

    <div id="dialog_modify_seed" style="font-size:12px">
        <div style="height:40px">
            <label class="LabelHead" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'width: 80px;';else echo 'width: 80px;';?>"><?php echo $lang->line("langAdminMain.Str_Name_Dot")?></label>
            <!--<label id="label_Mofify_Seed_Name"></label>-->
            <input id="label_Mofify_Seed_Name" class="InputText" value="" type="text">
        </div>
        <div style="height:40px">
            <label class="LabelHead" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'width: 80px;';else echo 'width: 80px;';?>"><?php echo $lang->line("langAdminMain.Str_Remark_Dot")?></label>
            <input id="input_Modify_Seed_Description" maxlength="64" class="InputText" value="" type="text">
        </div>

        <div style="height:40px">
            <div style="position:relative;top: 28px;">
                <a id="btn_modify_vm_seed" href="#" class="btn-jquery" style="position: absolute;right: 0px;font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
            </div>
        </div>                
    </div>   
    
    <div id="dialog_create_iscsi" style="font-size: 12px">
        <div id='connect_iscsi_input_info' style="display: none">
            <div style="height:30px;">
                 <div style="position:relative;">
                    <label class="LabelHead" style="line-height: 30px;vertical-align: middle;">IP :</label>
                    <input id="input_iscsi_ip" style="position:absolute;margin-left: 24px" class="InputText" value="" type="text">
                 </div>
            </div>        
            <div style="height: 40px;display: table">
                <div style="position:relative;display: table-cell;vertical-align: middle">
                    <input type="checkbox" id="check-chap" class="iCheckBoxCHAP">
                    <label style="cursor:pointer;" for="check-chap"><?php echo $lang->line("langAdminMain.Str_Enable_CHAP")?></label>
                </div>
            </div>
            <div style="height:40px">
                <div style="position:relative;margin-left: 20px">
                    <label class="LabelHead" style="line-height: 40px;vertical-align: middle;"><?php echo $lang->line("langAdminMain.Str_Username_Dot")?></label>
                    <input  id="input_iscsi_user_name" class="InputText" value="" type="text">
                </div>
            </div>
            <div style="height:40px">
                <div style="position:relative;margin-left: 20px">
                    <label style="line-height: 40px;vertical-align: middle;" class="LabelHead"><?php echo $lang->line("langAdminMain.Str_Password_Dot")?></label>
                    <input id="input_iscsi_passwd" class="InputText" value="" type="password">
                </div>
            </div>
        </div>
        <div id='connect_iscsi_select_target'>            
        </div>
        <div style="position: absolute;bottom: 10px;right: 10px">                
            <a id="btn_iscsi_previous" href="#" class="btn-create" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Str_Previous_Step")?></a>
            <a id="btn_iscsi_next" href="#" class="btn-create" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Str_Next_Step")?></a>
            <a id="btn_close_create_iscsi" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
        </div>
    </div>

    <div id="dropdown-VM-Operation2" class="dropdown dropdown-tip">
        <ul class="dropdown-menu">
            <li><a href="#1" class="btn_vm_operation vm-close"><?php echo $lang->line("langAdminMain.VD_Poweroff")?></a></li>
            <li><a href="#2" class="btn_vm_operation vm-shutdown"><?php echo $lang->line("langAdminMain.VD_Shutdown")?></a></li>
            <li><a href="#2" class="btn_vm_operation vm-modify-boot"><?php echo $lang->line("langAdminMain.Str_Modify")?></a></li>
            <li><a href="#3" class="btn_vm_operation vm-inquiry"><?php echo $lang->line("langAdminMain.Str_Inquiry")?></a></li>
            <li><a href="#3" class="btn_vm_operation vm-snapshot"><?php echo $lang->line("langAdminMain.VD_SnapShot")?></a></li> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->  
            <?php
                if(isset($outputUIConfig['APPluse']) && $outputUIConfig['APPluse'] != 0)
                    echo '<li><a href="#3" class="btn_vm_operation vm-backup">'.$lang->line("langAdminMain.VD_Backup").'</a></li>';
                    /* SnapShot&Schedule&Backup  2017.01.11 william */
            ?>
        </ul>
    </div>

    <div id="dropdown-VM-Operation1" class="dropdown dropdown-tip">
        <ul class="dropdown-menu">
            <li><a href="#1" class="btn_vm_operation vm-poweron"><?php echo $lang->line("langAdminMain.VD_Poweron")?></a></li>
            <li><a href="#2" class="btn_vm_operation vm-modify"><?php echo $lang->line("langAdminMain.Str_Modify")?></a></li>
            <li><a href="#4" class="btn_vm_operation vm-inquiry"><?php echo $lang->line("langAdminMain.Str_Inquiry")?></a></li>
            <!--
            <li><a href="#4" class="btn_vm_operation vm-disable"><?php echo $lang->line("langAdminMain.Service_Disable")?></a></li>
            <li><a href="#3" class="btn_vm_operation vm-reset"><?php echo $lang->line("langAdminMain.Str_Reset_Password")?></a></li>
            -->
            <li><a href="#3" class="btn_vm_operation vm-clone"><?php echo $lang->line("langAdminMain.Str_Clone")?></a></li>
            <?php
                if($outputUIConfig['Seed'] == 1)
                    echo '<li><a href="#3" class="btn_vm_operation vm-make-seed">'.$lang->line("langAdminMain.Str_Make_Seed").'</a></li>';
                    echo '<li><a href="#4" class="btn_vm_operation vm-overwriteseed">'.$lang->line("langAdminMain.Str_overwriteseed").'</a></li>';
            ?>
            <li><a href="#3" class="btn_vm_operation vm-export"><?php echo $lang->line("langAdminMain.Str_Export")?></a></li>
            <li><a href="#3" class="btn_vm_operation vm-snapshot"><?php echo $lang->line("langAdminMain.VD_SnapShot")?></a></li> <!-- /* SnapShot&Schedule&Backup  2017.01.11 william */ -->  
            <?php
                if(isset($outputUIConfig['APPluse']) && $outputUIConfig['APPluse'] != 0)
                    echo '<li><a href="#3" class="btn_vm_operation vm-backup">'.$lang->line("langAdminMain.VD_Backup").'</a></li>';
                    /* SnapShot&Schedule&Backup  2017.01.11 william */
            ?>
            <li><a href="#3" class="btn_vm_operation vm-delete"><?php echo $lang->line("langAdminMain.H4_BtnRaidDelete")?></a></li>             
        </ul>
    </div>

    <div id="dropdown-VM-Operation3" class="dropdown dropdown-tip">
        <ul class="dropdown-menu">
            <li><a href="#1" class="btn_vm_operation vm-close"><?php echo $lang->line("langAdminMain.VD_Poweroff")?></a></li>                             
            <li><a href="#2" class="btn_vm_operation vm-inquiry"><?php echo $lang->line("langAdminMain.Str_Inquiry")?></a></li>
            <!--<li><a href="#3" class="btn_vm_operation vm-disable"><?php // echo $lang->line("langAdminMain.Service_Disable")?></a></li>-->            
        </ul>
    </div>

    <div id="dropdown-VM-Operation4" class="dropdown dropdown-tip">
        <ul class="dropdown-menu">
            <li><a href="#4" class="btn_vm_operation vm-enable"><?php echo $lang->line("langAdminMain.Service_Enable")?></a></li>
            <li><a href="#2" class="btn_vm_operation vm-inquiry"><?php echo $lang->line("langAdminMain.Str_Inquiry")?></a></li>
            <li><a href="#4" class="btn_vm_operation vm-delete"><?php echo $lang->line("langAdminMain.H4_BtnRaidDelete")?></a></li>        
        </ul>
    </div>

    <div id="dropdown-VM-Operation5" class="dropdown dropdown-tip">
        <ul class="dropdown-menu">
            <li><a href="#2" class="btn_vm_operation vm-modify-seed"><?php echo $lang->line("langAdminMain.Str_Modify")?></a></li>
            <li><a href="#4" class="btn_vm_operation vm-create-vdi"><?php echo $lang->line("langAdminMain.VD_Born_Users").$VMorVDI;?></a></li>            
            <li><a href="#2" class="btn_vm_operation vm-inquiry"><?php echo $lang->line("langAdminMain.Str_Inquiry")?></a></li>
            <li><a href="#2" class="btn_vm_operation vm-RebornUser"><?php echo $lang->line("langAdminMain.Str_RebornUser")?></a></li>
            <li><a href="#4" class="btn_vm_operation vm-delete-seed"><?php echo $lang->line("langAdminMain.H4_BtnRaidDelete")?></a></li>            
        </ul>
    </div>

    <div id="dropdown-VM-Operation5-1" class="dropdown dropdown-tip">
        <ul class="dropdown-menu">
            <li><a href="#2" class="btn_vm_operation vm-modify-seed"><?php echo $lang->line("langAdminMain.Str_Modify")?></a></li>
            <li><a href="#2" class="btn_vm_operation vm-RenewUser"><?php echo $lang->line("langAdminMain.Str_RenewUser")?></a></li>
            <li><a href="#2" class="btn_vm_operation vm-inquiry"><?php echo $lang->line("langAdminMain.Str_Inquiry")?></a></li>
            <li><a href="#4" class="btn_vm_operation vm-delete-seed"><?php echo $lang->line("langAdminMain.H4_BtnRaidDelete")?></a></li>            
        </ul>
    </div>

    <div id="dropdown-VM-Operation6" class="dropdown dropdown-tip">
        <ul class="dropdown-menu">
            <li><a href="#1" class="btn_vm_operation vm-poweron"><?php echo $lang->line("langAdminMain.VD_Poweron")?></a></li>
            <li><a href="#2" class="btn_vm_operation vm-modify"><?php echo $lang->line("langAdminMain.Str_Modify")?></a></li>
            <li><a href="#4" class="btn_vm_operation vm-inquiry"><?php echo $lang->line("langAdminMain.Str_Inquiry")?></a></li>
            <li><a href="#4" class="btn_vm_operation vm-VDRecovery"><?php echo $lang->line("langAdminMain.Str_VDRecovery")?></a></li>
            <!--
            <li><a href="#4" class="btn_vm_operation vm-disable"><?php echo $lang->line("langAdminMain.Service_Disable")?></a></li>
            <li><a href="#3" class="btn_vm_operation vm-reset"><?php echo $lang->line("langAdminMain.Str_Reset_Password")?></a></li>            
            -->
            <!-- 2017.11.22 william 雲桌面轉移至雲主機 -->
            <li><a href="#" class="btn_vm_operation vm-uservd-clone"><?php echo $lang->line("langAdminMain.Str_Clone")?></a></li>            
            <li><a href="#3" class="btn_vm_operation vm-delete"><?php echo $lang->line("langAdminMain.H4_BtnRaidDelete")?></a></li>                   
        </ul>
    </div>

    <div id="dropdown-VM-Operation7" class="dropdown dropdown-tip">
        <ul class="dropdown-menu">            
            <li><a href="#3" class="btn_vm_operation vm-inquiry"><?php echo $lang->line("langAdminMain.Str_Inquiry")?></a></li>
            <!--<li><a href="#4" class="btn_vm_operation vm-disable"><?php // echo $lang->line("langAdminMain.Service_Disable")?></a></li>-->            
        </ul>
    </div>

    <div id="dropdown-VM-Operation12" class="dropdown dropdown-tip">
        <ul class="dropdown-menu">            
            <li><a href="#4" class="btn_vm_operation vm-delete"><?php echo $lang->line("langAdminMain.H4_BtnRaidDelete")?></a></li>        
        </ul>
    </div>

    <div id="dropdown-Power" class="dropdown dropdown-tip dropdown-anchor-right">
        <ul class="dropdown-menu">
            <li><a href="#1" class="drop-shutdown"><?php echo $lang->line("langAdminMain.VD_Shutdown")?></a></li>
            <li><a href="#2" class="drop-reboot"><?php echo $lang->line("langAdminMain.Str_Reboot")?></a></li>                       
        </ul>
    </div>

    <div id="dropdown-iSCSI" class="dropdown dropdown-tip">
        <ul class="dropdown-menu">
            <li><a href="#1" class="drop-logout"><?php echo $lang->line("langAdminMain.H5_Btn_Logout")?></a></li>                                
        </ul>
    </div>
    <!-- /* SnapShot 動作下拉式清單 2017.01.26 william */ -->
    <div id="dropdown-VM-Operation8" class="dropdown dropdown-tip">
        <ul class="dropdown-menu">            
            <!-- <li><a href="#1" class="btn_vm_operation vm-SnapshotView"><?php //echo $lang->line("langAdminMain.SnapshotView")?></a></li>  -->
            <li><a href="#1" class="btn_vm_operation vm-SnapshotRollback"><?php echo $lang->line("langAdminMain.SnapshotRollback")?></a></li>       
            <li><a href="#1" class="btn_vm_operation vm-SnapshotDelete"><?php echo $lang->line("langAdminMain.SnapshotDelete")?></a></li>            
        </ul>
    </div>

    <!-- /* Schedule 動作下拉式清單 2017.01.26 william */ -->
    <div id="dropdown-VM-Operation9" class="dropdown dropdown-tip">
        <ul class="dropdown-menu">            
            <li><a href="#1" class="btn_vm_operation ScheduleRule"><?php echo $lang->line("langAdminMain.ScheduleRule")?></a></li>
            <li><a href="#1" class="btn_vm_operation ScheduleJobs"><?php echo $lang->line("langAdminMain.ScheduleJobs")?></a></li>                    
            <li><a href="#1" class="btn_vm_operation ScheduleDelete"><?php echo $lang->line("langAdminMain.ScheduleDelete")?></a></li>            
        </ul>
    </div>

    <!-- /* Schedule 動作下拉式清單(bk) 2017.01.26 william */ -->
    <div id="dropdown-VM-Operation10" class="dropdown dropdown-tip">
        <ul class="dropdown-menu">                        
            <li><a href="#1" class="btn_vm_operation BKScheduleRule"><?php echo $lang->line("langAdminMain.ScheduleRule")?></a></li>        
            <li><a href="#1" class="btn_vm_operation BKScheduleJobs"><?php echo $lang->line("langAdminMain.ScheduleJobs")?></a></li>
            <li><a href="#1" class="btn_vm_operation BKScheduleDelete"><?php echo $lang->line("langAdminMain.ScheduleDelete")?></a></li>        
        </ul>
    </div>

    <div id="dropdown-VM-Operation11" class="dropdown dropdown-tip">
        <ul class="dropdown-menu">
            <li><a href="#1" class="btn_vm_operation vm-close"><?php echo $lang->line("langAdminMain.VD_Poweroff")?></a></li>
            <li><a href="#2" class="btn_vm_operation vm-shutdown"><?php echo $lang->line("langAdminMain.VD_Shutdown")?></a></li>
            <li><a href="#2" class="btn_vm_operation vm-modify-boot"><?php echo $lang->line("langAdminMain.Str_Modify")?></a></li>
            <li><a href="#3" class="btn_vm_operation vm-inquiry"><?php echo $lang->line("langAdminMain.Str_Inquiry")?></a></li>          
        </ul>
    </div>

    <!-- /* SnapShot&Schedule&Backup 快照清單 2017.01.11 william */ -->
    <div id="dialog_vm_snapshot" style="font-size:12px">            
        <div class="takess_wrapper">
            <div class="Takess_header0">
                <div class="Takess_header_inner0">
                    <label class="SnapshotList_Username"><?php echo $lang->line("langAdminMain.Str_Username")?> : </label><label class="Takess_Username Takess_header_inner0lable" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo '';else echo 'left: 120px;';?>"></label>
                </div>
            </div>
            <div class="Takess_header0">
                <div class="Takess_header_inner0">
                    <label class="SnapshotList_Name"><?php echo $lang->line("langAdminMain.Str_VD")?><?php echo $lang->line("langAdminMain.VMList_Name")?> : </label><label class="Takess_VDName Takess_header_inner0lable" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo '';else echo 'left: 120px;';?>"></label>
                </div>  
            </div>
            <!-- 新增快照層數  2017.11.22 william -->
            <div class="Takess_header0">
                <div class="Takess_header_inner0" style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'width: 95px;';else echo 'width: 125px;';?>">
                    <label class="SnapshotLayerlabel"><?php echo $lang->line("langAdminMain.Str_SSLayer")?></label>
                </div>
                <input type="text" name="SnapshotUsedLayer" id="SnapshotUsedLayer_input">
                <div id="btn_modify_ss_layer" class="btn-jquery large-btn">
                        <label class="ButtonLabel"><?php echo $lang->line("langAdminMain.VD_SnapShot_Layer_ModifyConfirm")?></label>
                </div>                
                <label class="label_SSLayer_warning_2"></label>
            </div>
            <div class="Takess_header">
                <div class="takess_header_inner1">
                    <label class="Takess_desc"><?php echo $lang->line("langAdminMain.VD_SnapShot_Desc")?> : </label><input id="Takess_desc_input" class="Takess_desc_input" type="text" name="ScheduleName" value=""  style="<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo '';else echo 'left: 125px;';?>">
                </div>
                <div class="takess_header_inner2">
                    <div id="btn_Take_Snapshot" class="btn_Take_Snapshot btn-jquery large-btn">
                        <label class="ButtonLabel"><?php echo $lang->line("langAdminMain.VD_SnapShot_Take_Snapshot")?></label>
                    </div>
                    <!--  離開按鈕取消 2017.02.03 william -->
                    <div id="btn_StopView_snapshot" class="btn-jquery large-btn">
                        <label class="ButtonLabel"><?php echo $lang->line("langAdminMain.SnapshotStopView")?></label>
                </div>
            </div>
            </div>
            <div class="Takess_body">
                <div class="takess_content">
                    <!--<div class="Takess_label"><?php echo $lang->line("langAdminMain.VD_SnapShot_Layer_List")?></div>-->
                    <div class="Takess_ContentInner">
                        <div class="Takess_ContentInner_Top">
                            <table id="Takess_ListTop">    
                                <tr>
                                    <td id="takess_top_Item" class="takess_top0 takess_table_top_row">
                                        <label><?php echo $lang->line("langAdminMain.VD_SnapShot_Item")?></label>
                                    </td>
                                    <td id="takess_top_Create_Time" class="takess_top2 takess_table_top_row">
                                        <label><?php echo $lang->line("langAdminMain.VD_SnapShot_Create_Time")?></label>
                                    </td>
                                    <td id="takess_top_Desc" class="takess_top4 takess_table_top_row">
                                        <label><?php echo $lang->line("langAdminMain.VD_SnapShot_Desc2")?></label>
                                    </td>           
                                    <td id="takess_top_size" class="takess_top5 takess_table_top_row">
                                        <label><?php echo $lang->line("langAdminMain.VD_SnapShot_Size")?></label>
                                    </td>                                                                     
                                    <td id="takess_top_view" class="takess_top1 takess_table_top_row">
                                        <label><?php echo $lang->line("langAdminMain.VD_SnapShot_States")?></label>
                                    </td>                                      
                                    <td id="takess_top_Action" class="takess_top3 takess_table_top_row takess_last_column">
                                        <label><?php echo $lang->line("langAdminMain.VD_SnapShot_Action")?></label>
                                    </td>                                
                                </tr>
                            </table>
                        </div>
                    <div class="Takess_ContentInner_Bottom">  
                        <table id="Takess_ListBottom">

                        </table>
                    </div>
                </div>
            </div>
        </div>
                    </div>
                </div>
    <!--   /* SnapShot&Schedule 建立工作排程 2017.01.11 william */ -->
        <div id="dialog_create_schedule">  
             <div id="create-schedule-container" class='create-schedule-container'>
                     <div class="cs_wrapper">
                        <div class="cs_header row1">
                            <div class="cs_header_inner1">
                                <label class="cs_name"><?php echo $lang->line("langAdminMain.Schedule_Name")?>：</label><input id="cs_name_input" class="cs_name_input" style="font-size: 18px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'width: 645px;';else echo 'width: 598px;';?>" type="text">
                                <!--<div id="btn_close_ss_create_schedule" class="MenuButton btn_close_create_schedule"><label class="MenuButtonLabel"><?php echo $lang->line("langAdminMain.Schedule_Exit")?></label></div>-->
                            </div>
                        </div>
                        <div class="cs_body row2">
                            <div class="left_side">
                                <div class="left_side_content">
                                    <label class="st_label"><?php echo $lang->line("langAdminMain.Schedule_Type")?></label>
                                    <table id="st_table" border="0">
                                        <!--<tr>
                                            <td>
                                                <input type="radio" name="radMethod" value="1" id="st_once">
                                                <label for="st_once"><?php echo $lang->line("langAdminMain.Schedule_Type_Onetime")?></label>
                                            </td>
                                        </tr>-->
                                        <tr>
                                            <td>
                                                <input type="radio" name="radMethod" value="4" id="st_Day">
                                                <label for="st_Day"><?php echo $lang->line("langAdminMain.Schedule_Type_Daily")?></label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input type="radio" name="radMethod" value="8" id="st_Week">
                                                <label for="st_Week"><?php echo $lang->line("langAdminMain.Schedule_Type_Weekly")?></label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input type="radio" name="radMethod" value="16" id="st_Month">
                                                <label for="st_Month"><?php echo $lang->line("langAdminMain.Schedule_Type_Monthly")?></label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input type="radio" name="radMethod" value="32" id="st_Month_R">
                                                <label for="st_Month_R"><?php echo $lang->line("langAdminMain.Schedule_Type_MonthlyR")?></label>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                            <div class="right_side">
                                <div class="right_side_content">
                                    <div class="right_side_top">
                                        <label class="start_date"><?php echo $lang->line("langAdminMain.Schedule_Start_Date")?>：</label>
                                        <input type="text" id="datepicker_schedule" class="datepicker_schedule_2" name="DateText">
                                        <label class="start_date" style=" margin-left: 15px; "><?php echo $lang->line("langAdminMain.Schedule_Start_Time")?>：</label>
                                        <input type="text" id="timepicker_schedule" class="timepicker_schedule_2" name="TimeText">
                                    </div>
                                    <div class="right_side_bottom">
                                        <div class="methodDiv" id="div_1">
                                            <div class="div_once_content">
                                                <div class="div_once_text"><?php echo $lang->line("langAdminMain.Schedule_Onetime_Desc")?></div>
                                            </div>
                                        </div>
                                        <div class="methodDiv" id="div_4">
                                            <div class="div_Day_content" style="font-size: 18px;">
                                                <label><?php echo $lang->line("langAdminMain.Schedule_Daily_text1")?> ：</label><input type="text" id="FreqInterval_Day" value="1" style="width: 30px; text-align: right;" disabled><label> <?php echo $lang->line("langAdminMain.Schedule_Daily_text2")?></label>
                                            </div>
                                        </div>
                                        <div class="methodDiv" id="div_8">
                                            <div class="div_Week_content" style="font-size: 18px;">
                                                <table class="Weekly_table table_1" id="Weekly_table_1" border="0">
                                                    <tr>
                                                        <td colspan="3"><label><?php echo $lang->line("langAdminMain.Schedule_Weekly_text")?></label></td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type="checkbox" value="2" name="user_active_Week" id="FreqInterval_Week_5" class="css-checkbox checkbox_check">
                                                            <label for="FreqInterval_Week_5" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Monday")?></label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="4" name="user_active_Week" id="FreqInterval_Week_4" class="css-checkbox checkbox_check">
                                                            <label for="FreqInterval_Week_4" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Tuesday")?></label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="8" name="user_active_Week" id="FreqInterval_Week_3" class="css-checkbox checkbox_check">
                                                            <label for="FreqInterval_Week_3" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Wednesday")?></label>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type="checkbox" value="16" name="user_active_Week" id="FreqInterval_Week_2" class="css-checkbox checkbox_check">
                                                            <label for="FreqInterval_Week_2" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Thursday")?></label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="32" name="user_active_Week" id="FreqInterval_Week_1" class="css-checkbox checkbox_check">
                                                            <label for="FreqInterval_Week_1" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Friday")?></label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="64" name="user_active_Week" id="FreqInterval_Week_0" class="css-checkbox checkbox_check">
                                                            <label for="FreqInterval_Week_0" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Saturday")?></label>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type="checkbox" value="1" name="user_active_Week" id="FreqInterval_Week_6" class="css-checkbox checkbox_check">
                                                            <label for="FreqInterval_Week_6" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Sunday")?></label>
                                                        </td>
                                                        <td></td>
                                                        <td></td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                        <div class="methodDiv" id="div_16">
                                            <div class="div_Month_content" style="font-size: 18px;">
                                                <table class="Monthly_table table_1" id="Monthly_table" border="0">
                                                    <tr>
                                                        <td colspan="10"><label><?php echo $lang->line("langAdminMain.Schedule_Monthly_text")?></label></td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type="checkbox" value="0x1" name="user_active_Month" id="FreqInterval_Month_31" class="css-checkbox">
                                                            <label for="FreqInterval_Month_31" class="css-label monthly_text_right">1</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x2" name="user_active_Month" id="FreqInterval_Month_30" class="css-checkbox">
                                                            <label for="FreqInterval_Month_30" class="css-label monthly_text_right">2</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x4" name="user_active_Month" id="FreqInterval_Month_29" class="css-checkbox">
                                                            <label for="FreqInterval_Month_29" class="css-label monthly_text_right">3</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x8" name="user_active_Month" id="FreqInterval_Month_28" class="css-checkbox">
                                                            <label for="FreqInterval_Month_28" class="css-label monthly_text_right">4</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x10" name="user_active_Month" id="FreqInterval_Month_27" class="css-checkbox">
                                                            <label for="FreqInterval_Month_27" class="css-label monthly_text_right">5</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x20" name="user_active_Month" id="FreqInterval_Month_26" class="css-checkbox">
                                                            <label for="FreqInterval_Month_26" class="css-label monthly_text_right">6</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x40" name="user_active_Month" id="FreqInterval_Month_25" class="css-checkbox">
                                                            <label for="FreqInterval_Month_25" class="css-label monthly_text_right">7</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x80" name="user_active_Month" id="FreqInterval_Month_24" class="css-checkbox">
                                                            <label for="FreqInterval_Month_24" class="css-label monthly_text_right">8</label>
                                                        </td>       
                                                        <td>
                                                            <input type="checkbox" value="0x100" name="user_active_Month" id="FreqInterval_Month_23" class="css-checkbox">
                                                            <label for="FreqInterval_Month_23" class="css-label monthly_text_right">9</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x200" name="user_active_Month" id="FreqInterval_Month_22" class="css-checkbox">
                                                            <label for="FreqInterval_Month_22" class="css-label">10</label>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type="checkbox" value="0x400" name="user_active_Month" id="FreqInterval_Month_21" class="css-checkbox">
                                                            <label for="FreqInterval_Month_21" class="css-label">11</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x800" name="user_active_Month" id="FreqInterval_Month_20" class="css-checkbox">
                                                            <label for="FreqInterval_Month_20" class="css-label">12</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x1000" name="user_active_Month" id="FreqInterval_Month_19" class="css-checkbox">
                                                            <label for="FreqInterval_Month_19" class="css-label">13</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x2000" name="user_active_Month" id="FreqInterval_Month_18" class="css-checkbox">
                                                            <label for="FreqInterval_Month_18" class="css-label">14</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x4000" name="user_active_Month" id="FreqInterval_Month_17" class="css-checkbox">
                                                            <label for="FreqInterval_Month_17" class="css-label">15</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x8000" name="user_active_Month" id="FreqInterval_Month_16" class="css-checkbox">
                                                            <label for="FreqInterval_Month_16" class="css-label">16</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x10000" name="user_active_Month" id="FreqInterval_Month_15" class="css-checkbox">
                                                            <label for="FreqInterval_Month_15" class="css-label">17</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x20000" name="user_active_Month" id="FreqInterval_Month_14" class="css-checkbox">
                                                            <label for="FreqInterval_Month_14" class="css-label">18</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x40000" name="user_active_Month" id="FreqInterval_Month_13" class="css-checkbox">
                                                            <label for="FreqInterval_Month_13" class="css-label">19</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x80000" name="user_active_Month" id="FreqInterval_Month_12" class="css-checkbox">
                                                            <label for="FreqInterval_Month_12" class="css-label">20</label>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type="checkbox" value="0x100000" name="user_active_Month" id="FreqInterval_Month_11" class="css-checkbox">
                                                            <label for="FreqInterval_Month_11" class="css-label">21</label>
                                                        </td>
                                                    <td>
                                                        <input type="checkbox" value="0x200000" name="user_active_Month" id="FreqInterval_Month_10" class="css-checkbox">
                                                        <label for="FreqInterval_Month_10" class="css-label">22</label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="0x400000" name="user_active_Month" id="FreqInterval_Month_9" class="css-checkbox">
                                                        <label for="FreqInterval_Month_9" class="css-label">23</label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="0x800000" name="user_active_Month" id="FreqInterval_Month_8" class="css-checkbox">
                                                        <label for="FreqInterval_Month_8" class="css-label">24</label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="0x1000000" name="user_active_Month" id="FreqInterval_Month_7" class="css-checkbox">
                                                        <label for="FreqInterval_Month_7" class="css-label">25</label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="0x2000000" name="user_active_Month" id="FreqInterval_Month_6" class="css-checkbox">
                                                        <label for="FreqInterval_Month_6" class="css-label">26</label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="0x4000000" name="user_active_Month" id="FreqInterval_Month_5" class="css-checkbox">
                                                        <label for="FreqInterval_Month_5" class="css-label">27</label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="0x8000000" name="user_active_Month" id="FreqInterval_Month_4" class="css-checkbox">
                                                        <label for="FreqInterval_Month_4" class="css-label">28</label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="0x10000000" name="user_active_Month" id="FreqInterval_Month_3" class="css-checkbox">
                                                        <label for="FreqInterval_Month_3" class="css-label">29</label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="0x20000000" name="user_active_Month" id="FreqInterval_Month_2" class="css-checkbox">
                                                        <label for="FreqInterval_Month_2" class="css-label">30</label>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="checkbox" value="0x40000000" name="user_active_Month" id="FreqInterval_Month_1" class="css-checkbox">
                                                        <label for="FreqInterval_Month_1" class="css-label">31</label>
                                                    </td>
                                                    <td colspan="9">
                                                        <input type="checkbox" value="0x80000000" name="user_active_Month" id="FreqInterval_Month_0" class="css-checkbox">
                                                        <label for="FreqInterval_Month_0" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Monthly_LastDay")?></label>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="methodDiv" id="div_32">
                                        <div class="Month_R_1" style="font-size: 18px;">
                                            <table class="Monthly_R_table table_1" id="Monthly_R_table" border="0">
                                                <tr>
                                                    <td colspan="10"><label><?php echo $lang->line("langAdminMain.Schedule_MonthlyR_text")?></label></td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="checkbox" value="1" name="user_active_MR" id="FreqInterval_MR_4" class="css-checkbox">
                                                        <label for="FreqInterval_MR_4" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_MR_First")?></label>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="checkbox" value="2" name="user_active_MR" id="FreqInterval_MR_3" class="css-checkbox">
                                                        <label for="FreqInterval_MR_3" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_MR_Second")?></label>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="checkbox" value="4" name="user_active_MR" id="FreqInterval_MR_2" class="css-checkbox">
                                                        <label for="FreqInterval_MR_2" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_MR_Third")?></label>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="checkbox" value="8" name="user_active_MR" id="FreqInterval_MR_1" class="css-checkbox">
                                                        <label for="FreqInterval_MR_1" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_MR_Fourth")?></label>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="checkbox" value="16" name="user_active_MR" id="FreqInterval_MR_0" class="css-checkbox">
                                                        <label for="FreqInterval_MR_0" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_MR_Last")?></label>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                        <div class="Month_R_2" style="font-size: 18px;">
                                            <table class="Weekly_table table_1" id="Weekly_R_table" border="0">
                                                <tr>
                                                    <td colspan="3"><label><?php echo $lang->line("langAdminMain.Schedule_Weekly_text")?></label></td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="checkbox" value="2" name="user_active_WR" id="FreqInterval_WR_5" class="css-checkbox">
                                                        <label for="FreqInterval_WR_5" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Monday")?></label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="4" name="user_active_WR" id="FreqInterval_WR_4" class="css-checkbox">
                                                        <label for="FreqInterval_WR_4" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Tuesday")?></label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="8" name="user_active_WR" id="FreqInterval_WR_3" class="css-checkbox">
                                                        <label for="FreqInterval_WR_3" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Wednesday")?></label>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="checkbox" value="16" name="user_active_WR" id="FreqInterval_WR_2" class="css-checkbox">
                                                        <label for="FreqInterval_WR_2" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Thursday")?></label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="32" name="user_active_WR" id="FreqInterval_WR_1" class="css-checkbox">
                                                        <label for="FreqInterval_WR_1" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Friday")?></label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="64" name="user_active_WR" id="FreqInterval_WR_0" class="css-checkbox">
                                                        <label for="FreqInterval_WR_0" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Saturday")?></label>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="checkbox" value="1" name="user_active_WR" id="FreqInterval_WR_6" class="css-checkbox">
                                                        <label for="FreqInterval_WR_6" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Sunday")?></label>
                                                    </td>
                                                    <td></td>
                                                    <td></td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="cs_footer row3">
                        <div class="cs_footer_inner1">
                            <label class="cs_desc"><?php echo $lang->line("langAdminMain.Schedule_Desc")?>：</label><input id="cs_desc_input" class="cs_desc_input" style="font-size: 18px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'width: 680px;';else echo '';?>" type="text" name="ScheduleDescription" id="input_ScheduleDescription">
                        </div>
                        <div class="cs_footer_inner2">                            
                            <a id="btn_create_schedule_Modify"  class="btn-jquery" style="font-size: 12px;"><?php echo $lang->line("langAdminMain.Schedule_Modify_bnt")?></a>                            
                            <a id="btn_create_schedule_Confirm" class="btn-jquery" style="font-size: 12px;"><?php echo $lang->line("langAdminMain.Btn_Next")?></a>
                            <a id="btn_create_schedule_Cancel" class="btn-jquery" style="font-size: 12px;"><?php echo $lang->line("langAdminMain.Cancel")?></a>                            
                    </div>
                </div>
             </div>
          </div>  
          </div>  
    <!--   /* Backup&Schedule 建立工作排程 2017.01.11 william */ -->
        <div id="dialog_create_bkschedule">  
             <div id="create-schedule-container" class='create-schedule-container'>
                     <div class="cs_wrapper">
                        <div class="cs_header row1">
                            <div class="cs_header_inner1">
                                <label class="cs_name"><?php echo $lang->line("langAdminMain.Schedule_Name")?>：</label><input id="csbk_name_input" class="cs_name_input" style="font-size: 18px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'width: 645px;';else echo 'width: 598px;';?>" type="text">
                                <!--<div id="btn_close_bk_create_schedule" class="MenuButton btn_close_create_schedule"><label class="MenuButtonLabel"><?php echo $lang->line("langAdminMain.Schedule_Exit")?></label></div>-->
                            </div>
                        </div>
                        <div class="cs_body row2">
                            <div class="left_side">
                                <div class="left_side_content">
                                    <label class="st_label"><?php echo $lang->line("langAdminMain.Schedule_Type")?></label>
                                    <table id="st_bktable" border="0">
                                        <!--<tr>
                                            <td>
                                                <input type="radio" name="radMethod" value="1" id="st_once">
                                                <label for="st_once"><?php echo $lang->line("langAdminMain.Schedule_Type_Onetime")?></label>
                                            </td>
                                        </tr>-->
                                        <tr>
                                            <td>
                                                <input type="radio" name="radMethodbk" value="4" id="st_Daybk">
                                                <label for="st_Daybk"><?php echo $lang->line("langAdminMain.Schedule_Type_Daily")?></label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input type="radio" name="radMethodbk" value="8" id="st_Weekbk">
                                                <label for="st_Weekbk"><?php echo $lang->line("langAdminMain.Schedule_Type_Weekly")?></label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input type="radio" name="radMethodbk" value="16" id="st_Monthbk">
                                                <label for="st_Monthbk"><?php echo $lang->line("langAdminMain.Schedule_Type_Monthly")?></label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input type="radio" name="radMethodbk" value="32" id="st_Month_Rbk">
                                                <label for="st_Month_Rbk"><?php echo $lang->line("langAdminMain.Schedule_Type_MonthlyR")?></label>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                            <div class="right_side">
                                <div class="right_side_content">
                                    <div class="right_side_top">
                                        <label class="start_date"><?php echo $lang->line("langAdminMain.Schedule_Start_Date")?>：</label>
                                        <input type="text" id="datepicker_schedulebk" class="datepicker_schedule_2" name="DateText">
                                        <label class="start_date" style=" margin-left: 15px; "><?php echo $lang->line("langAdminMain.Schedule_Start_Time")?>：</label>
                                        <input type="text" id="timepicker_schedulebk" class="timepicker_schedule_2" name="TimeText">
                                    </div>
                                    <div class="right_side_bottom">
                                        <div class="methodDivbk" id="div_bk1">
                                            <div class="div_once_content">
                                                <div class="div_once_text"><?php echo $lang->line("langAdminMain.Schedule_Onetime_Desc")?></div>
                                            </div>
                                        </div>
                                        <div class="methodDivbk" id="div_bk4">
                                            <div class="div_Day_content" style="font-size: 18px;">
                                                <label><?php echo $lang->line("langAdminMain.Schedule_Daily_text1")?> ：</label><input type="text" id="FreqInterval_Day" value="1" style="width: 30px; text-align: right;" disabled><label> <?php echo $lang->line("langAdminMain.Schedule_Daily_text2")?></label>
                                            </div>
                                        </div>
                                        <div class="methodDivbk" id="div_bk8">
                                            <div class="div_Week_content" style="font-size: 18px;">
                                                <table class="Weekly_table table_1" id="Weekly_table_1" border="0">
                                                    <tr>
                                                        <td colspan="3"><label><?php echo $lang->line("langAdminMain.Schedule_Weekly_text")?></label></td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type="checkbox" value="2" name="user_active_Weekbk" id="FreqInterval_Weekbk_5" class="css-checkbox checkbox_check">
                                                            <label for="FreqInterval_Weekbk_5" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Monday")?></label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="4" name="user_active_Weekbk" id="FreqInterval_Weekbk_4" class="css-checkbox checkbox_check">
                                                            <label for="FreqInterval_Weekbk_4" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Tuesday")?></label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="8" name="user_active_Weekbk" id="FreqInterval_Weekbk_3" class="css-checkbox checkbox_check">
                                                            <label for="FreqInterval_Weekbk_3" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Wednesday")?></label>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type="checkbox" value="16" name="user_active_Weekbk" id="FreqInterval_Weekbk_2" class="css-checkbox checkbox_check">
                                                            <label for="FreqInterval_Weekbk_2" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Thursday")?></label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="32" name="user_active_Weekbk" id="FreqInterval_Weekbk_1" class="css-checkbox checkbox_check">
                                                            <label for="FreqInterval_Weekbk_1" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Friday")?></label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="64" name="user_active_Weekbk" id="FreqInterval_Weekbk_0" class="css-checkbox checkbox_check">
                                                            <label for="FreqInterval_Weekbk_0" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Saturday")?></label>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type="checkbox" value="1" name="user_active_Weekbk" id="FreqInterval_Weekbk_6" class="css-checkbox checkbox_check">
                                                            <label for="FreqInterval_Weekbk_6" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Sunday")?></label>
                                                        </td>
                                                        <td></td>
                                                        <td></td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                        <div class="methodDivbk" id="div_bk16">
                                            <div class="div_Month_content" style="font-size: 18px;">
                                                <table class="Monthly_table table_1" id="Monthly_table" border="0">
                                                    <tr>
                                                        <td colspan="10"><label><?php echo $lang->line("langAdminMain.Schedule_Monthly_text")?></label></td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type="checkbox" value="0x1" name="user_active_Monthbk" id="FreqInterval_Monthbk_31" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_31" class="css-label monthly_text_right">1</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x2" name="user_active_Monthbk" id="FreqInterval_Monthbk_30" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_30" class="css-label monthly_text_right">2</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x4" name="user_active_Monthbk" id="FreqInterval_Monthbk_29" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_29" class="css-label monthly_text_right">3</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x8" name="user_active_Monthbk" id="FreqInterval_Monthbk_28" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_28" class="css-label monthly_text_right">4</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x10" name="user_active_Monthbk" id="FreqInterval_Monthbk_27" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_27" class="css-label monthly_text_right">5</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x20" name="user_active_Monthbk" id="FreqInterval_Monthbk_26" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_26" class="css-label monthly_text_right">6</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x40" name="user_active_Monthbk" id="FreqInterval_Monthbk_25" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_25" class="css-label monthly_text_right">7</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x80" name="user_active_Monthbk" id="FreqInterval_Monthbk_24" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_24" class="css-label monthly_text_right">8</label>
                                                        </td>       
                                                        <td>
                                                            <input type="checkbox" value="0x100" name="user_active_Monthbk" id="FreqInterval_Monthbk_23" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_23" class="css-label monthly_text_right">9</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x200" name="user_active_Monthbk" id="FreqInterval_Monthbk_22" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_22" class="css-label">10</label>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type="checkbox" value="0x400" name="user_active_Monthbk" id="FreqInterval_Monthbk_21" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_21" class="css-label">11</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x800" name="user_active_Monthbk" id="FreqInterval_Monthbk_20" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_20" class="css-label">12</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x1000" name="user_active_Monthbk" id="FreqInterval_Monthbk_19" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_19" class="css-label">13</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x2000" name="user_active_Monthbk" id="FreqInterval_Monthbk_18" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_18" class="css-label">14</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x4000" name="user_active_Monthbk" id="FreqInterval_Monthbk_17" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_17" class="css-label">15</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x8000" name="user_active_Monthbk" id="FreqInterval_Monthbk_16" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_16" class="css-label">16</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x10000" name="user_active_Monthbk" id="FreqInterval_Monthbk_15" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_15" class="css-label">17</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x20000" name="user_active_Monthbk" id="FreqInterval_Monthbk_14" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_14" class="css-label">18</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x40000" name="user_active_Monthbk" id="FreqInterval_Monthbk_13" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_13" class="css-label">19</label>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" value="0x80000" name="user_active_Monthbk" id="FreqInterval_Monthbk_12" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_12" class="css-label">20</label>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type="checkbox" value="0x100000" name="user_active_Monthbk" id="FreqInterval_Monthbk_11" class="css-checkbox">
                                                            <label for="FreqInterval_Monthbk_11" class="css-label">21</label>
                                                        </td>
                                                    <td>
                                                        <input type="checkbox" value="0x200000" name="user_active_Monthbk" id="FreqInterval_Monthbk_10" class="css-checkbox">
                                                        <label for="FreqInterval_Monthbk_10" class="css-label">22</label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="0x400000" name="user_active_Monthbk" id="FreqInterval_Monthbk_9" class="css-checkbox">
                                                        <label for="FreqInterval_Monthbk_9" class="css-label">23</label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="0x800000" name="user_active_Monthbk" id="FreqInterval_Monthbk_8" class="css-checkbox">
                                                        <label for="FreqInterval_Monthbk_8" class="css-label">24</label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="0x1000000" name="user_active_Monthbk" id="FreqInterval_Monthbk_7" class="css-checkbox">
                                                        <label for="FreqInterval_Monthbk_7" class="css-label">25</label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="0x2000000" name="user_active_Monthbk" id="FreqInterval_Monthbk_6" class="css-checkbox">
                                                        <label for="FreqInterval_Monthbk_6" class="css-label">26</label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="0x4000000" name="user_active_Monthbk" id="FreqInterval_Monthbk_5" class="css-checkbox">
                                                        <label for="FreqInterval_Monthbk_5" class="css-label">27</label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="0x8000000" name="user_active_Monthbk" id="FreqInterval_Monthbk_4" class="css-checkbox">
                                                        <label for="FreqInterval_Monthbk_4" class="css-label">28</label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="0x10000000" name="user_active_Monthbk" id="FreqInterval_Monthbk_3" class="css-checkbox">
                                                        <label for="FreqInterval_Monthbk_3" class="css-label">29</label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="0x20000000" name="user_active_Monthbk" id="FreqInterval_Monthbk_2" class="css-checkbox">
                                                        <label for="FreqInterval_Monthbk_2" class="css-label">30</label>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="checkbox" value="0x40000000" name="user_active_Monthbk" id="FreqInterval_Monthbk_1" class="css-checkbox">
                                                        <label for="FreqInterval_Monthbk_1" class="css-label">31</label>
                                                    </td>
                                                    <td colspan="9">
                                                        <input type="checkbox" value="0x80000000" name="user_active_Monthbk" id="FreqInterval_Monthbk_0" class="css-checkbox">
                                                        <label for="FreqInterval_Monthbk_0" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Monthly_LastDay")?></label>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="methodDivbk" id="div_bk32">
                                        <div class="Month_R_1" style="font-size: 18px;">
                                            <table class="Monthly_R_table table_1" id="Monthly_R_table" border="0">
                                                <tr>
                                                    <td colspan="10"><label><?php echo $lang->line("langAdminMain.Schedule_MonthlyR_text")?></label></td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="checkbox" value="1" name="user_active_MRbk" id="FreqInterval_MRbk_4" class="css-checkbox">
                                                        <label for="FreqInterval_MRbk_4" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_MR_First")?></label>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="checkbox" value="2" name="user_active_MRbk" id="FreqInterval_MRbk_3" class="css-checkbox">
                                                        <label for="FreqInterval_MRbk_3" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_MR_Second")?></label>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="checkbox" value="4" name="user_active_MRbk" id="FreqInterval_MRbk_2" class="css-checkbox">
                                                        <label for="FreqInterval_MRbk_2" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_MR_Third")?></label>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="checkbox" value="8" name="user_active_MRbk" id="FreqInterval_MRbk_1" class="css-checkbox">
                                                        <label for="FreqInterval_MRbk_1" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_MR_Fourth")?></label>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="checkbox" value="16" name="user_active_MRbk" id="FreqInterval_MRbk_0" class="css-checkbox">
                                                        <label for="FreqInterval_MRbk_0" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_MR_Last")?></label>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                        <div class="Month_R_2" style="font-size: 18px;">
                                            <table class="Weekly_table table_1" id="Weekly_R_table" border="0">
                                                <tr>
                                                    <td colspan="3"><label><?php echo $lang->line("langAdminMain.Schedule_Weekly_text")?></label></td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="checkbox" value="2" name="user_active_WRbk" id="FreqInterval_WRbk_5" class="css-checkbox">
                                                        <label for="FreqInterval_WRbk_5" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Monday")?></label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="4" name="user_active_WRbk" id="FreqInterval_WRbk_4" class="css-checkbox">
                                                        <label for="FreqInterval_WRbk_4" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Tuesday")?></label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="8" name="user_active_WRbk" id="FreqInterval_WRbk_3" class="css-checkbox">
                                                        <label for="FreqInterval_WRbk_3" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Wednesday")?></label>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="checkbox" value="16" name="user_active_WRbk" id="FreqInterval_WRbk_2" class="css-checkbox">
                                                        <label for="FreqInterval_WRbk_2" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Thursday")?></label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="32" name="user_active_WRbk" id="FreqInterval_WRbk_1" class="css-checkbox">
                                                        <label for="FreqInterval_WRbk_1" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Friday")?></label>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" value="64" name="user_active_WRbk" id="FreqInterval_WRbk_0" class="css-checkbox">
                                                        <label for="FreqInterval_WRbk_0" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Saturday")?></label>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="checkbox" value="1" name="user_active_WRbk" id="FreqInterval_WRbk_6" class="css-checkbox">
                                                        <label for="FreqInterval_WRbk_6" class="css-label"><?php echo $lang->line("langAdminMain.Schedule_Sunday")?></label>
                                                    </td>
                                                    <td></td>
                                                    <td></td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="cs_footer row3">
                        <div class="cs_footer_inner1">
                            <label class="cs_desc"><?php echo $lang->line("langAdminMain.Schedule_Desc")?>：</label><input id="csbk_desc_input" class="cs_desc_input" style="font-size: 18px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'width: 680px;';else echo '';?>" type="text" name="ScheduleDescription" id="input_ScheduleDescription">
                        </div>
                        <div class="cs_footer_inner2">                            
                            <a id="btn_create_bkschedule_Modify" class="btn-jquery" style="font-size: 12px;"><?php echo $lang->line("langAdminMain.Schedule_Modify_bnt")?></a>                            
                            <a id="btn_create_bkschedule_Confirm" class="btn-jquery" style="font-size: 12px;"><?php echo $lang->line("langAdminMain.Btn_Next")?></a>
                            <a id="btn_create_bkschedule_Cancel" class="btn-jquery" style="font-size: 12px;"><?php echo $lang->line("langAdminMain.Cancel")?></a>                            
                        </div>
                    </div>
                </div>
             </div>
          </div>                    
          <!--   /* SnapShot&Schedule 建立快照(自動) 2017.01.23 william */ -->
          <div id="dialog_autoss">  
             <div>
                <div class="autoss_wrapper">
                    <div class="autoss_wrappertop">
                        <div class="autoss_header">
                            <div class="autoss_header_inner1">
                                <label class="autoss_name_Schedule"><?php echo $lang->line("langAdminMain.VirtualHost_VDList")?></label>
                            </div>
                            <!--<div id="btn_close_autoss" class="btn-jquery large-btn btn_autoss">
                                <?php echo $lang->line("langAdminMain.Jobs_Exit")?>
                            </div>-->
                        </div>
                        <div class="autoss_body">
                            <div class="autoss_content">
                                <div class="autoss_content_inner">
                                    <div class="autoss_content_innertop">
                                        <table id="autoss_ListTop">
                                            <tr>
                                                <td id="ss_wrapper_top_top_Item" class="table_top_row autoss_top0" style="text-align: center;">
                                                    <labe><?php echo $lang->line("langAdminMain.Jobs_SN")?></label>
                                                </td>
                                                <td id="ss_selectall_top_chk" class="autoss_selectall_topchk" style="text-align: center;">
                                                    <!--<input type="checkbox" value="" name="top_select_all" id="top_select_all" class="css-checkbox">
                                                    <label for="top_select_all" class="css-label" style="margin: 0.5px;"></label>-->
                                                    <input type="checkbox" class="iCheckBoxAllStandbyJobsSelect">
                                                </td>
                                                <td id="ss_wrapper_top_top_Owner" class="table_top_row autoss_top2">
                                                    <label><?php echo $lang->line("langAdminMain.Jobs_Owner")?></label>
                                                </td>
                                                <td id="ss_wrapper_top_top_VDName" class="table_top_row autoss_top3">
                                                    <label><?php echo $lang->line("langAdminMain.Jobs_VDName")?></label>
                                                </td>
                                                <td id="ss_wrapper_top_top_VDStatus" class="table_top_row autoss_top4">
                                                    <label><?php echo $lang->line("langAdminMain.Jobs_Status")?></label>
                                                </td>
                                                <td id="ss_wrapper_top_top_Remark" class="table_top_row last_column autoss_top5">
                                                    <label><?php echo $lang->line("langAdminMain.Jobs_Remark")?></label>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                    <div class="autoss_content_innerbottom">
                                        <table id="autoss_ListBottom">

                                        </table>
                                    </div>
                                    <div id="autoss_AddToJob" class="downwards_arrow_wrapper" style="background: #0273b7;"><div id="ss_Arrow_down" class="arrowtext"></div></div>
                                    <div id="autoss_AddToStandby" class="upwards_arrow_wrapper"><div id="ss_Arrow_up" class="arrowtext"></div></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <hr color="#91979c" size="2" style="position: relative;top: -2.5%;width: 737px;">
                    <!-- the snapshot source vd list-->
                    <div class="autoss_wrapperbottom">
                        <div class="autoss_wrapperbottom_body">
                            <div class="autoss_wrapperbottom_content">
                                <label style="color:#ffffff;position:absolute;top: -33.5px;background: #0273b7;width: 325px;font-weight: bold;font-size: 18px;"><?php echo $lang->line("langAdminMain.Added_VDList")?></label>
                                <div class="autoss_wrapperbottom_contentinner">
                                    <div class="autoss_content_innertop">
                                        <table id="autoss_ListTop">
                                            <tr>
                                                <td id="ss_wrapper_bottom_top_Item" class="table_top_row autoss_top0" style="text-align: center;">
                                                    <label><?php echo $lang->line("langAdminMain.Jobs_SN")?></label>
                                                </td>
                                                <td id="ss_selectall_bottom_chk" class="ss_selectall_bottom_chk" style="text-align: center;">
                                                    <!--<input type="checkbox" value="" name="bottom_select_all" id="bottom_select_all" class="css-checkbox">
                                                    <label for="bottom_select_all" class="css-label"></label>-->
                                                    <input type="checkbox" class="iCheckBoxAllForwardbackJobsSelect">
                                                </td>
                                                <td id="ss_wrapper_bottom_top_Owner" class="table_top_row autoss_top2">
                                                    <label><?php echo $lang->line("langAdminMain.Jobs_Owner")?></label>
                                                </td>
                                                <td id="ss_wrapper_bottom_top_VDName" class="table_top_row autoss_top3">
                                                    <label><?php echo $lang->line("langAdminMain.Jobs_VDName")?></label>
                                                </td>
                                                <td id="ss_wrapper_bottom_top_VDStatus" class="table_top_row autoss_top4">
                                                    <label><?php echo $lang->line("langAdminMain.Jobs_Status")?></label>
                                                </td>
                                                <td id="ss_wrapper_bottom_top_Remark" class="table_top_row last_column autoss_top5">
                                                    <label><?php echo $lang->line("langAdminMain.Jobs_Remark")?></label>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                    <div class="autoss_content_innerbottom">
                                        <table id="autoss_ListJobBottom">

                                        </table>
                                    </div>
                                </div>
                                <div class="autoss_btn_row">
                                    <a id="autoss_Cancel" class="btn-jquery" style="font-size: 12px;"><?php echo $lang->line("langAdminMain.Cancel")?></a>                            
                                    <a id="autoss_Confirm" class="btn-jquery" style="font-size: 12px;"><?php echo $lang->line("langAdminMain.Confirm")?></a>
                                    <a id="autoss_Modify" class="btn-jquery" style="font-size: 12px;float: right; margin: 0px 3px 0px 3px;"><?php echo $lang->line("langAdminMain.Str_Modify")?></a>
                                    <a id="autoss_Previous" class="btn-jquery" style="font-size: 12px;float: right; margin: 0px 3px 0px 3px;"><?php echo $lang->line("langAdminMain.Btn_Previous")?></a>
                                    <!--<div id="autoss_Cancel" class="btn-jquery large-btn btn_autoss">
                                        <?php echo $lang->line("langAdminMain.Jobs_Cancel")?>
                                    </div>
                                    <div id="autoss_Confirm" class="btn-jquery large-btn btn_autoss">
                                        <?php echo $lang->line("langAdminMain.Jobs_Confirm")?>
                                    </div>-->
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>    
          <!--   /* SnapShot&Schedule&Backup 建立備份(自動) 2017.02.21 william */ -->
          <div id="dialog_autobk">  
             <div>
                <div class="autobk_wrapper">
                    <div class="autobk_wrappertop">
                        <div class="autobk_header">
                            <div class="autobk_header_inner1">
                                <label class="autobk_name"><?php echo $lang->line("langAdminMain.VirtualHost_VDList")?></label>
                            </div>
                            <!--<div id="btn_close_autobk" class="btn-jquery large-btn btn_autobk">
                                <?php echo $lang->line("langAdminMain.Jobs_Exit")?>
                            </div>-->
                        </div>
                        <div class="autobk_body">
                            <div class="autobk_content">
                                <div class="autobk_content_inner">
                                    <div class="autobk_content_innertop">
                                        <table id="autobk_ListTop">
                                            <tr>
                                                <td id="ss_wrapper_top_top_Item" class="table_top_row autobk_top0" style="text-align: center;">
                                                    <labe><?php echo $lang->line("langAdminMain.Jobs_SN")?></label>
                                                </td>
                                                <td id="ss_selectall_top_chk" class="autobk_selectall_topchk" style="text-align: center;">
                                                    <!--<input type="checkbox" value="" name="top_select_all" id="top_select_all" class="css-checkbox">
                                                    <label for="top_select_all" class="css-label" style="margin: 0.5px;"></label>-->
                                                    <input type="checkbox" class="iCheckBoxAllStandbyBackupJobsSelect">
                                                </td>
                                                <td id="ss_wrapper_top_top_Owner" class="table_top_row autobk_top2">
                                                    <label><?php echo $lang->line("langAdminMain.Jobs_Owner")?></label>
                                                </td>
                                                <td id="ss_wrapper_top_top_VDName" class="table_top_row autobk_top3">
                                                    <label><?php echo $lang->line("langAdminMain.Jobs_VDName")?></label>
                                                </td>
                                                <td id="ss_wrapper_top_top_VDStatus" class="table_top_row autobk_top4">
                                                    <label><?php echo $lang->line("langAdminMain.Jobs_Status")?></label>
                                                </td>
                                                <td id="ss_wrapper_top_top_Remark" class="table_top_row last_column autobk_top5">
                                                    <label><?php echo $lang->line("langAdminMain.Jobs_Remark")?></label>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                    <div class="autobk_content_innerbottom">
                                        <table id="autobk_ListBottom">

                                        </table>
                                    </div>
                                    <div id="autobk_AddToJob" class="downwards_arrow_wrapper" style="background: #0273b7;"><div id="bk_Arrow_down" class="arrowtext bk_Arrow_down"></div></div>
                                    <div id="autobk_AddToStandby" class="upwards_arrow_wrapper"><div id="bk_Arrow_up" class="arrowtext bk_Arrow_up"></div></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <hr color="#91979c" size="2" style="position: relative;top: -2.5%;width: 737px;">
                    <!-- the snapshot source vd list-->
                    <div class="autobk_wrapperbottom">
                        <div class="autobk_wrapperbottom_body">
                            <div class="autobk_wrapperbottom_content">
                                <label style="color:#ffffff;position:absolute;top: -33.5px;background: #0273b7;width: 325px;font-weight: bold;font-size: 18px;"><?php echo $lang->line("langAdminMain.Added_VDList")?></label>
                                <div class="autobk_wrapperbottom_contentinner">
                                    <div class="autobk_content_innertop">
                                        <table id="autobk_ListTop">
                                            <tr>
                                                <td id="ss_wrapper_bottom_top_Item" class="table_top_row autobk_top0" style="text-align: center;">
                                                    <label><?php echo $lang->line("langAdminMain.Jobs_SN")?></label>
                                                </td>
                                                <td id="ss_selectall_bottom_chk" class="ss_selectall_bottom_chk" style="text-align: center;">
                                                    <!--<input type="checkbox" value="" name="bottom_select_all" id="bottom_select_all" class="css-checkbox">
                                                    <label for="bottom_select_all" class="css-label"></label>-->
                                                    <input type="checkbox" class="iCheckBoxAllBackupForwardbackJobsSelect">
                                                </td>
                                                <td id="ss_wrapper_bottom_top_Owner" class="table_top_row autobk_top2">
                                                    <label><?php echo $lang->line("langAdminMain.Jobs_Owner")?></label>
                                                </td>
                                                <td id="ss_wrapper_bottom_top_VDName" class="table_top_row autobk_top3">
                                                    <label><?php echo $lang->line("langAdminMain.Jobs_VDName")?></label>
                                                </td>
                                                <td id="ss_wrapper_bottom_top_VDStatus" class="table_top_row autobk_top4">
                                                    <label><?php echo $lang->line("langAdminMain.Jobs_Status")?></label>
                                                </td>
                                                <td id="ss_wrapper_bottom_top_Remark" class="table_top_row last_column autobk_top5">
                                                    <label><?php echo $lang->line("langAdminMain.Jobs_Remark")?></label>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                    <div class="autobk_content_innerbottom">
                                        <table id="autobk_ListJobBottom">

                                        </table>
                                    </div>
                                </div>
                                <div class="autobk_btn_row">
                                    <a id="autobk_Cancel" class="btn-jquery" style="font-size: 12px;"><?php echo $lang->line("langAdminMain.Cancel")?></a>                            
                                    <a id="autobk_Confirm" class="btn-jquery" style="font-size: 12px;"><?php echo $lang->line("langAdminMain.Confirm")?></a>
                                    <a id="autobk_Modify" class="btn-jquery" style="font-size: 12px;float: right; margin: 0px 3px 0px 3px;"><?php echo $lang->line("langAdminMain.Str_Modify")?></a>
                                    <a id="autobk_Previous" class="btn-jquery" style="font-size: 12px;float: right; margin: 0px 3px 0px 3px;"><?php echo $lang->line("langAdminMain.Btn_Previous")?></a>                                
                                    <!--<div id="autobk_Cancel" class="btn-jquery large-btn btn_autobk">
                                        <?php echo $lang->line("langAdminMain.Jobs_Cancel")?>
                                    </div>
                                    <div id="autobk_Confirm" class="btn-jquery large-btn btn_autobk">
                                        <?php echo $lang->line("langAdminMain.Jobs_Confirm")?>
                                    </div>-->
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>   
        <!-- Backup修改名稱 2017.03.07 william -->
        <div id="dialog_bkvdrename">  
            <div style="line-height: 40px;">
                <label><?php echo $lang->line("langAdminMain.BackupMgt_SourceVDname")?>：</label><label id="conflictSource" style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis;"></label>
            </div>
            <div style="line-height: 40px;">
                <label><?php echo $lang->line("langAdminMain.BackupMgt_VDrename")?>：</label><input id="bkrename__input" class="bkrename__input" type="text">
            </div>            
            <div style="position: relative; top: 233px; left: 650px;" id="BKRename_Bnt_Confirm" class="btn-jquery large-btn">
                <?php echo $lang->line("langAdminMain.BackupMgt_VDrename_btnConfirm")?>
            </div>
        </div>  
        <!-- Backup 連線設定 2017.03.08 william -->
        <div id="dialog_bkConnectSetting">  
            <div class="BKInfo">
                <div class="InfoContent">
                    <fieldset class="Backup_Content2">
                        <div>
                            <div style="position: relative;top:0px;line-height: 32px;">
                                <label class="Backup_Content_L1" style="text-align: left;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'width: 45px;';else echo 'width: 85px;';?>"><?php echo $lang->line("langAdminMain.Backup_Status")?><?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo '&nbsp;';else echo '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';?>:</label><label id="checkLUNstate_text" class="Backup_Content_L1" style="width: 550px;text-align: left;margin-left: 5px;"></label><br>
                                <label name="bktext" class="Backup_Content_L1" style="text-align: left;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'width: 45px;';else echo 'width: 85px;';?>"><?php echo $lang->line("langAdminMain.Backup_Desc")?> :</label><input type="text" id="Backup_Desc" class="BackupInfo_Content1_input1" style="font-size: 18px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'width: 624px;';else echo 'width: 585px;';?>"><br>
                            </div>
                            <div style="position: relative;top: 21px;left: 0px;">
                                <a id="Backup_Format_btn" class="btn-jquery" style="font-size: 12px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'left: 585px;';else echo 'left: 535px;';?>position: absolute;"><?php echo $lang->line("langAdminMain.Backup_Format")?></a>
                                <a id="Backup_Use_btn" class="btn-jquery" style="font-size: 12px;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'left: 655px;';else echo 'left: 655px;';?>position: absolute;"><?php echo $lang->line("langAdminMain.Backup_Use")?></a>
                            </div>
                        </div>
                    </fieldset>
                </div>
            </div>
        </div>           
        <!-- LUN尚未連線 2017.03.15 william -->
        <div id="dialog_bkStorageConnCheck">
            <div style="position: relative;width: 100%;height: 100%">  
                <div style="position: absolute;word-wrap:break-word;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'left: -10px;top: 0px;width: 442px;font-size: 14px;';else echo 'left: -10px;top: 0px;width: 442px;font-size: 14px;';?>"><?php echo $lang->line("langAdminMain.BackupLUN_OfflineMessage1")?></div><br>
                <div style="position: absolute;word-wrap:break-word;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'left: -10px;top: 30px;width: 442px;font-size: 14px;';else echo 'left: -10px;top: 30px;width: 442px;font-size: 14px;';?>"><?php echo $lang->line("langAdminMain.BackupLUN_OfflineMessage2")?></div>
                <div id="BKredirect_Bnt_Confirm" class="btn-jquery large-btn" style="position: absolute;<?php if($lang->language_name == 'zh-tw' || $lang->language_name == 'zh-cn')echo 'top: 83px;left: 392px;';else echo 'top: 83px;left: 380px;';?>;font-size: 14px">
                    <?php echo $lang->line("langAdminMain.BackupMgt_VDrename_btnConfirm")?>
                </div>
            </div>
        </div>  

    <!-- 2017.11.13 william 新增Raid擴充，建立擴充視窗 -->
    <div id="dialog_create_expand" style="font-size:12px">
        <div style="height:493px">
            <div style="position: relative;width: 99%;margin-left: auto;margin-right: auto;height: 100%;">  
                <!--  Div - Header -->
                <div id="DiskRowHeader" class = "Div_DiskRowHeader RowHeader" style="font-size: 14px">
                    <div class = "Div_RelativeLayout" style="overflow: hidden;">
                        <div id="DivRAIDExpand_RowHeader_inner"  style="height: 100%;width: 100%;overflow-x: auto;overflow-y: hidden;position: absolute;padding-bottom: 30px;top: 0px;">
                            <div class="UnusedDisk_Select AbsoluteLayout RowField" style="top: 6px; left: 15px;">
                                <div style="position:relative">
                                    <input type="checkbox" class="iCheckBoxAllUnusedDiskSelect">
                                </div>
                            </div>                             
                            <div id="postion" class = "DiskList_Slot AbsoluteLayout RowField DiskHeader" >
                                <div class="div_RowHeader adjust_Disk_div_RowHeader">
                                    <H4 class="H4_RowHeader" id="DiskList_Slot_1"><?php echo $lang->line("langAdminMain.DiskList_Slot")?>&nbsp;</H4> <!-- 新式樣修改(藍黃) 2016.12.13 william -->
                                    <label id="postion1" class="label_disk_arrow label_RowHeader">&#9650;</label>
                                </div>
                            </div>
                            <div id="status" class = "DiskList_Status AbsoluteLayout RowField DiskHeader">
                                <div class="div_RowHeader adjust_Disk_div_RowHeader">
                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.DiskList_Status")?>&nbsp;</H4>                        
                                    <label id="status1" class="label_disk_arrow label_RowHeader"></label>
                                </div>
                            </div>
                            <div id="capacity" class = "DisktList_Capacity AbsoluteLayout RowField DiskHeader">
                                <div class="div_RowHeader adjust_Disk_div_RowHeader">
                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.DisktList_Capacity")?>&nbsp;</H4>
                                    <label id="capacity1" class="label_disk_arrow label_RowHeader"></label>
                                </div>
                            </div>
                            <div id="vendor" class = "DiskList_Vendor AbsoluteLayout RowField DiskHeader" >
                                <div class="div_RowHeader adjust_Disk_div_RowHeader">
                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.DiskList_Vendor")?>&nbsp;</H4>
                                    <label id="vendor1" class="label_disk_arrow label_RowHeader"></label>
                                </div>
                            </div>
                            <div id="if_type" class = "DiskList_Speed AbsoluteLayout RowField DiskHeader">
                                <div class="div_RowHeader adjust_Disk_div_RowHeader">
                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.DiskList_Speed")?>&nbsp;</H4>
                                    <label id="if_type1" class="label_disk_arrow label_RowHeader"></label>
                                </div>
                            </div>
                            <div id="model" class = "DiskList_Model AbsoluteLayout RowField DiskHeader" >
                                <div class="div_RowHeader adjust_Disk_div_RowHeader">
                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.DiskList_Model")?>&nbsp;</H4>
                                    <label id="model1" class="label_disk_arrow label_RowHeader"></label>
                                </div>
                            </div>
                            <div id="rev" class = "DiskList_FirmwareVer AbsoluteLayout RowField DiskHeader"> 
                                <div class="div_RowHeader adjust_Disk_div_RowHeader">
                                    <H4 class="H4_RowHeader"><?php echo $lang->line("langAdminMain.DiskList_FirmwareVer")?>&nbsp;</H4>
                                    <label id="rev1" class="label_disk_arrow label_RowHeader"></label>
                                </div>
                            </div>
                            <H4 class = "DiskList_BtnInitial AbsoluteLayout RowField" ><?php echo $lang->line("langAdminMain.List_Action")?></H4>   
                        </div>                                                  
                    </div> 
                </div>
                <!--  Div - List  -->
                <div id = "Div_RAIDExpandList" style="font-size: 12px;" onscroll="document.getElementById('DivRAIDExpand_RowHeader_inner').scrollLeft = this.scrollLeft;">
                </div>
            </div>
        </div>            
        <div style="position: absolute;bottom: 5px;right: 10px">                
            <a id="btn_create_expand" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
            <a id="btn_close_create_expand" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
        </div>
    </div>

        <!-- 2018.01.08 william 新增磁碟擴充功能，建立擴充視窗 -->    
        <div id="dialog_create_expand_disk">
            <div style="position: relative;width: 100%;height: 100%">  
                <div style="height:40px">
                    <label class="LabelHead" style="width: 350px;font-size: 18px;"><?php echo $lang->line("langAdminMain.Str_Expend_Disk")?></label>
                </div>
                <div style="height:40px">
                    <label class="ExpendVMDiskOrgSize" style="font-size: 18px;"></label>
                    <label style="font-size: 18px;">GB + </label>
                    <input id="input_ExpendVMDiskSize"class="InputText" value="" type="text" style="width: 50px;font-size: 18px;">
                    <label style="font-size: 18px;">GB</label>
                </div>
                <div id="Expanddisk_Btn_Confirm" class="btn-jquery large-btn" style="position: absolute;top: 83px;left: 300px;;font-size: 14px;margin: 0px 0px 0px 0px;">
                    <?php echo $lang->line("langAdminMain.Confirm")?>
                </div>
                <div id="Expanddisk_Btn_Exit" class="btn-jquery large-btn" style="position: absolute;top: 83px;left: 390px;;font-size: 14px;margin: 0 0px 0px 4px;">
                    <?php echo $lang->line("langAdminMain.Cancel")?>
                </div>                
            </div>
        </div>  


        <div id="dialog_create_user" style="font-size:12px">
            <div style="height:40px">
                <label class="LabelHead" style="width: 100px;"><?php echo $lang->line("langAdminMain.Str_Name_Dot")?></label>
                <input id="input_UserName" class="InputText" value="" type="text">
            </div>            
            <div class="dialog_createuser_disksize" style="height:40px">
                <div style="position:relative">
                    <label class="LabelHead" style="width: 100px;"><?php echo $lang->line("langAdminMain.DiskSize")?></label>   
                    <input id="input_userdisksize" class="InputText" value="" type="text">
                    <label>GB</label>
                </div>
            </div>            
            <div class="dialog_createuser_disktype" style="height:40px">
                <div style="position:relative">
                    <label style="position:absolute;top:7px;width: 100px;" class="LabelHead"><?php echo $lang->line("langAdminMain.DiskType")?></label>
                    <div style="position:absolute;width: 200px;top:0px;left:103px">
                        <select id="user_combo_disk_type">
                            <option value="0">IDE</option>
                            <option value="1">SCSI</option>
                            <option value="2">VirtIO</option>
                        </select>
                    </div>                
                </div>                
            </div>        

            <div class="dialog_createuser_raidid" style="height:40px">
                <div style="position:relative">
                    <label style="position:absolute;top:7px;width: 100px;" class="LabelHead">RAID ID :</label>
                     <div id="raidid_createuser" style="position:absolute;width: 200px;top:0px;left:103px">
                    </div>                
                </div>                
            </div>  

             

            <div class="dialog_createuser_manager" style="height:40px">
                <div style="position:relative">
                    <label style="position:absolute;top:7px;width: 100px;" class="LabelHead"><?php echo $lang->line("langAdminMain.IsManager")?></label>                    
                    <div style="position:absolute;top:3px;left:103px">
                        <input type="checkbox" class="iCheckUserisAdmin" id="isadmin_icheck">                        
                    </div>
                </div>
            </div>
            <div style="position: absolute;bottom: 10px;right: 10px">                
                <a id="btn_create_user" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
                <a id="btn_close_create_user" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
            </div>
        </div>

        <div id="dialog_user_profile" style="font-size:12px">
            <!--<div style="height:40px">
                <label class="LabelHead" style="width: 100px;"><?php echo $lang->line("langAdminMain.Str_Name_Dot")?></label>
                <input id="input_UserName" class="InputText" value="" type="text">
            </div>            -->
            <div style="height:40px">
                <div style="position:relative">
                    <label class="LabelHead" style="width: 100px;"><?php echo $lang->line("langAdminMain.DiskSize")?></label>   
                    <input id="input_userprofile_disksize" class="InputText" value="" type="text">
                    <label>GB</label>
                </div>
            </div>            
            <div style="height:40px">
                <div style="position:relative">
                    <label style="position:absolute;top:7px;width: 100px;" class="LabelHead"><?php echo $lang->line("langAdminMain.DiskType")?></label>
                    <div style="position:absolute;width: 200px;top:0px;left:103px">
                        <select id="userprofile_combo_disk_type">
                            <option value="0">IDE</option>
                            <option value="1">SCSI</option>
                            <option value="2">VirtIO</option>
                        </select>
                    </div>
                </div>                
            </div>       
            <div class="dialog_userprofile_raidid" style="height:40px">
                <div style="position:relative">
                    <label style="position:absolute;top:7px;width: 100px;" class="LabelHead">RAID ID :</label>
                     <div id="raidid_userprofile" style="position:absolute;width: 200px;top:0px;left:103px">
                    </div>                
                </div>                
            </div>                          
            <!--<div style="height:40px">
                <div style="position:relative">
                    <label style="position:absolute;top:7px;width: 100px;" class="LabelHead">管理者</label>                    
                    <div style="position:absolute;top:3px;left:103px">
                        <input type="checkbox" class="iCheckUserisAdmin" id="isadmin_icheck">                        
                    </div>
                </div>
            </div>-->
            <div style="position: absolute;bottom: 10px;right: 10px">                
                <a id="btn_confirm_userprofile" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
                <a id="btn_close_userprofile" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
            </div>
        </div>        

        <div id="dialog_Modify_user_profile" style="font-size:12px">
            <div style="height:40px">
                <div style="position:relative">
                    <label class="LabelHead" style="width: 100px;"><?php echo $lang->line("langAdminMain.DiskSize")?></label>   
                    <label id="UserDiskOrgSize"></label>
                    <label>GB + </label>
                    <input id="modify_userprofile_disksize" class="InputText" value="" type="text" style="width: 110px;">
                    <label>GB</label>
                </div>
            </div>            
            <div style="height:40px">
                <div style="position:relative">
                    <label style="position:absolute;top:7px;width: 100px;" class="LabelHead"><?php echo $lang->line("langAdminMain.DiskType")?></label>
                    <div style="position:absolute;width: 200px;top:0px;left:103px">
                        <select id="modify_userprofile_combo_disk_type">
                            <option value="0">IDE</option>
                            <option value="1">SCSI</option>
                            <option value="2">VirtIO</option>
                        </select>
                    </div>
                </div>                
            </div>                   
            <div style="position: absolute;bottom: 10px;right: 10px">                
                <a id="btn_modify_confirm_userprofile" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
                <a id="btn_modify_close_userprofile" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
            </div>
        </div>      

        <div id="dialog_VM_userlist" style="font-size:12px">
            <div style="height:40px">
                <div class="Dialog_listContainer">
                    <div class="Dialog_listview_title">
                        <div class="Dialog_row_title">
                            <div class="Dialog_no_user"><?php echo $lang->line("langAdminMain.Item")?></div>                
                            <div class="Dialog_column_user"><?php echo $lang->line("langAdminMain.UserName")?></div>                                    
                            <div class="Dialog_action"><?php echo $lang->line("langAdminMain.List_Action")?></div>
                        </div>                
                    </div>
                    <div id="Dialog_listview_content"></div>                                                                                             
                </div>
            </div>                            
            <div style="position: absolute;bottom: 10px;right: 10px">                
                <!--<a id="btn_modify_confirm_userprofile" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>-->
                <a id="btn_close_DialogUserList" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
            </div>
        </div>    

        <div id="dialog_VM_seedlist" style="font-size:12px">
            <div style="height:40px">
                <div class="Dialog_SeedlistContainer">
                    <div class="Dialog_Seedlistview_title">
                        <div class="Dialog_Seedrow_title">
                            <div class="Dialog_no_Seed"><?php echo $lang->line("langAdminMain.Item")?></div>                
                            <div class="Dialog_column_Seed"><?php echo $lang->line("langAdminMain.VD_Seed")?></div>                                    
                            <div class="Dialog_Seed_action"><?php echo $lang->line("langAdminMain.List_Action")?></div>
                        </div>                
                    </div>
                    <div id="Dialog_Seedlistview_content"></div>                                                                                             
                </div>
            </div>                            
            <div style="position: absolute;bottom: 10px;right: 10px">                
                <!--<a id="btn_modify_confirm_userprofile" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>-->
                <a id="btn_close_DialogSeedList" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
            </div>
        </div>    

        <div id="dialog_VM_userlist_checkbox" style="font-size:12px">
            <div style="height:40px">
                <div class="listContainer_cb">
                    <div class="listview_title_cb">
                        <div class="row_title_cb">
                            <div class="no_user_cb"><?php echo $lang->line("langAdminMain.Item")?></div>                
                            <div class="select_user_cb">
                                <input type="checkbox" class="iCheckBoxUserAllSelect">
                            </div>
                            <div class="column_user_cb"><?php echo $lang->line("langAdminMain.UserName")?></div>                                                                
                        </div>                
                    </div>
                    <div id="listview_content_cb"></div>                                                                                             
                </div>
            </div>                            
            <div style="position: absolute;bottom: 10px;right: 10px">                
                <a id="btn_UserList_cb_confirm" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
                <a id="btn_close_UserList_cb" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
            </div>
        </div>            

        <div id="dialog_moveraid" style="font-size:12px">                 
            <div style="height:40px">
                <div style="position:relative">
                    <label style="position:absolute;top:7px;width: 100px;" class="LabelHead">RAID ID :</label>
                     <div id="raidid_move" style="position:absolute;width: 200px;top:0px;left:103px">
                    </div>                
                </div>                
            </div>  
            <div style="height:40px">
                <div style="position:relative">
                    <label class="move_raid_alert" style="position:absolute;top:7px;width: 100%;color: red; font-size: 14px; font-weight: 900;" class="LabelHead"><?php echo $lang->line("langAdminMain.Move_RAID_alert")?></label>
                </div>                
            </div>              

            <div style="position: absolute;bottom: 10px;right: 10px">                
                <a id="btn_moveraid_confirm" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
                <a id="btn_close_moveraid" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
            </div>
        </div>

        <div id="dialog_moveraid_forVD" style="font-size:12px">                 
            <div style="height:40px">
                <div style="position:relative">
                    <label style="position:absolute;top:7px;width: 100px;" class="LabelHead">RAID ID :</label>
                     <div id="raidid_move_forVD" style="position:absolute;width: 200px;top:0px;left:103px">
                    </div>                
                </div>                
            </div>  
            <div style="height:40px">
                <div style="position:relative">
                    <label class="move_raid_alert" style="position:absolute;top:7px;width: 100%;color: red; font-size: 14px; font-weight: 900;" class="LabelHead"><?php echo $lang->line("langAdminMain.Move_RAID_alert")?></label>
                </div>                
            </div>                

            <div style="position: absolute;bottom: 10px;right: 10px">                
                <a id="btn_moveraid_forVD_confirm" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
                <a id="btn_close_forVD_moveraid" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
            </div>
        </div>        

        <div id="dialog_RestoreSelectRaid" style="font-size:12px">                 
            <div id="SelectRaidContent" style="height: 250px;overflow: auto;">             
            </div>  
            <div style="position: absolute;bottom: 10px;right: 10px">                
                <a id="btn_Restore_confirm" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
                <a id="btn_close_Restore" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
            </div>
        </div>             

        <div id="dialog_disk_info">    
            <div style="height:460px;overflow: auto;">
                <div id="div_unimportable_list">                                                      
                </div>
            </div>            
            <div style="position: absolute;bottom: 5px;right: 10px">                
                <a id="btn_close_disk_info" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
            </div>
        </div>  

        <!--<div id="dialog_Backup_Restore" style="font-size:12px">
            <div style="height:40px">
                <div style="position:relative">
                    <label class="LabelHead" style="width: 100px;"><?php echo $lang->line("langAdminMain.DiskSize")?></label>   
                    <input id="modify_userprofile_disksize" class="InputText" value="" type="text">
                    <label>GB</label>
                </div>
            </div>            
            <div style="height:40px">
                <div style="position:relative">
                    <label style="position:absolute;top:7px;width: 100px;" class="LabelHead"><?php echo $lang->line("langAdminMain.DiskType")?></label>
                    <div style="position:absolute;width: 200px;top:0px;left:103px">
                        <select id="modify_userprofile_combo_disk_type">
                            <option value="0">IDE</option>
                            <option value="1">SCSI</option>
                            <option value="2">VirtIO</option>
                        </select>
                    </div>
                </div>                
            </div>                   
            <div style="position: absolute;bottom: 10px;right: 10px">                
                <a id="btn_modify_confirm_userprofile" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Confirm")?></a>
                <a id="btn_modify_close_userprofile" href="#" class="btn-jquery" style="font-size: smaller"><?php echo $lang->line("langAdminMain.Btn_Network_Cancel")?></a>
            </div>
        </div>            -->

</body>
</html>