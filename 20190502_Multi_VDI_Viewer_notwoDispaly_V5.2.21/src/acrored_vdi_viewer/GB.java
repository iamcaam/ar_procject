/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import com.google.gson.JsonArray;

/**
 *
 * @author root
 */
// Input：,  Output： , 功能： 存放全域變數(Global variable)
public class GB {
    public static boolean lock_ThinClient;
    public static boolean lock_ChangePW;
    public static boolean Time_change;                    // 2017.06.19 william click bug fix
    public static boolean lock_AutoTimeRenew;             // 2017.06.19 william Auto updata time action lock
    public static int arandr_returncode             = -1; // 2018.01.29 william 雙螢幕實作
    public static int monitor_restart               = -1; // 2018.01.29 william 雙螢幕實作
    public static int DuplicateSize                 = 0;  // 2018.01.29 william 雙螢幕實作
    public static int Duplicate_Restart_second_name = 0;  // 2018.03.09 william 雙螢幕實作
    public static int Duplicate_Restart_width       = 0;  // 2018.03.09 william 雙螢幕實作
    public static int Duplicate_Restart_height      = 0;  // 2018.03.09 william 雙螢幕實作
    public static boolean reboot                    = false;
    public static boolean AllDisplayOff             = false;
    public static boolean OneDisplayDisable         = false;
    public static String second_Display_shutdown_off;
    public static String monitor_exec_string;
    public static String monitoroff_exec_string;
    public static String wifi_name;    
    public static String wifi_security_type;
    public static boolean wifi_stop_setvalue        = false;
    public static boolean wifi_Connectting_status   = false;
    public static boolean setGraphic_flag           = false;

    public static boolean lock_wifilist;
    public static boolean lock_wifipwd;
    public static boolean lock_battery;
    public static String fail_reconnect_wifi_name;
    public static boolean reconnect_flag;
    public static String reconnect_wifiname;
    public static String reconnect_securitytype;
    public static boolean stopFlag;
    public static int Brightness_value = -1;
    public static int JavaVersion = 0; // 0: AcroRed, 1: MegaCube
    public static String eth_Str;
    public static String ConnectionAddress = ""; // 2018.11.06 william 多執行緒Ping IP
    public static String ConnectionPort = ""; // 2018.11.06 william 多執行緒Ping IP      
    // 2018.11.11 新增AcroDesk & RDP Protocol
    public static JsonArray _jsonArrayData = null;   
    public static int _jsonArraySize = 0;   
    public static boolean ExitFlag = false; 
    public static String RejumpProtocol_AcroView;
    public static String RejumpProtocol_RDP;
    // 2018.12.04 新增VD&RDP狀態判斷
    public static int RDP_Status = -1;
    public static boolean RDP_IsPingOK = false;
    public static boolean Check_RDPPing = false;
    public static int RDP_IpCount1 = -1;
    public static int RDP_IpCount2 = -1;    
    // 2018.12.12 新增 431NoVGA 和第一次開機
    public static int RDPFirst = -1;    // 2018.18.18 new create & reborn 
    public static String IsAssignedUserDisk = ""; // 2018.18.18 new create & reborn 
    public static int RDP_Stage_Type = -100;      
    // 2018.12.18 RDP Enable
    public static boolean RDPEnable = true;    
    
    // 2019.01.02 view D Drive
    public static boolean IsViewDisk = false;
    public static String _viewDiskVDName = "";
    
    // 20019.02.18 新增FreeRDP for dual screen & usb redirection
    public static boolean FreeRDPEnable = false;
    
    // 2019.03.28 VM/VD migration reconnect
    public static String migrationVDID = "null";
    public static int _3D_Type = -1;
    public static String migrationApiIP = "";
    public static String migrationApiPort = "";    
    public static String migrationUserName = "";
    public static String migrationPwd = "";    
    public static String migrationUkey = "";        
    public static String migrationProtocol = "";  
    public static boolean migrationIsVdOrg = false;     
    public static boolean stopconnect = false;
    public static boolean EnableUSB = false;
    
    public static int AvailableProtocol = -1;
    public static int VDStatus = -100;
    public static int _rdpArraySize = 0;
    public static boolean IsPingOK = false;    
    public static int RDPStatus = -1;
    public static boolean adAuth = false;
    public static String adDomain = "";    
}
