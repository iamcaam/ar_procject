/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import com.google.gson.JsonArray;

/**
 *
 * @author william
 * 64bit
 */
public class GB {
    public static int JavaVersion = 0; // 0: AcroRed, 1: MegaCube    
    public static String ConnectionAddress = ""; // 2018.11.06 william 多執行緒Ping IP
    public static String ConnectionPort = ""; // 2018.11.06 william 多執行緒Ping IP
    // 2018.11.11 新增AcroDesk & RDP Protocol
    public static JsonArray _jsonArrayData = null;   
    public static int _jsonArraySize = 0;   
    public static boolean ExitFlag = false; 
    // 2018.12.04 新增VD&RDP狀態判斷
    public static int RDP_Status = -1;
    public static boolean RDP_IsPingOK = false;
    public static boolean Check_RDPPing = false;
    public static int RDP_IpCount1 = -1;
    public static int RDP_IpCount2 = -1;
//    public static boolean RejumpProtocol_AcroView = false;
//    public static boolean RejumpProtocol_RDP = false;    
    
    // 2018.12.12 新增 431NoVGA 和第一次開機
    public static int RDPFirst = -1;    // 2018.18.18 new create & reborn 
    public static String IsAssignedUserDisk = ""; // 2018.18.18 new create & reborn   
    public static int RDP_Stage_Type = -100;
    
    // 2018.12.18 RDP Enable
    public static boolean RDPEnable = true;
    
    // 2019.01.02 view D Drive
    public static boolean IsViewDisk = false;
    public static String _viewDiskVDName = "";    
    
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
    public static boolean migrationSPICE = false;
    
    public static String VMIP = "";
    
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
