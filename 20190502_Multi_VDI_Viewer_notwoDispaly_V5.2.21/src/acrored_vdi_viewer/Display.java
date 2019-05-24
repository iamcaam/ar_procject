/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import static java.lang.Thread.sleep;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import javafx.collections.FXCollections;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
//import sun.awt.Mutex;

/**
 *
 * @author root
 */
public class Display extends Thread { /** 2018.02.23 william 雙螢幕實作 **/
    public GB GB;
    public GBFunc GBFunc;
    
    private String Display_VGA1_name;
    private String Display_HDMI1_name;  
    private String Display_HDMI2_name;   

    private String VGA1_resolution_width;
    private String VGA1_resolution_height;
    private String HDMI1_resolution_width;     
    private String HDMI1_resolution_height; 
    private String HDMI2_resolution_width;
    private String HDMI2_resolution_height;    
    public String monitor_exec_string;        
    public String monitor_exec;    
    private String Display_primary_name;
    private String Display_second_name;      
    private String primary_resolution_width;
    private String primary_resolution_height;
    private String second_resolution_width;     
    private String second_resolution_height;     
    int AllMonitorConnected          = 0;    
    boolean Monitor_Status_VGA1      = false; // true: connected, false: disconnected   
    boolean Monitor_Status_HDMI1     = false; // true: connected, false: disconnected  
    boolean Monitor_Status_HDMI2     = false; // true: connected, false: disconnected    
 
    boolean VGA1_FirstData           = false;
    boolean HDMI1_FirstData          = false;
    boolean HDMI2_FirstData          = false;
    
    boolean Monitor_Setting_file     = false; // true: exists   , false: not exists
    boolean AllDisconnected          = false; // true: exists   , false: not exists    
    
    boolean single_vga               = false;
    boolean single_hdmi1             = false;
    boolean single_hdmi2             = false;
    ArrayList<String> Monitor_HWList = new ArrayList<String>();
   
    public String temp_primary_name;
    public String temp_primary_width;
    public String temp_primary_height;    
    public String temp_second_name;
    public String temp_second_width;
    public String temp_second_height;    
    
    /***    顯示文字對應表    ***/
    public Map<String, String> WordMap = new HashMap<>();      
    public Stage MainStage;


    static Timer timer;
    
//    boolean VGA1_StopCount           = false;
//    boolean HDMI1_StopCount          = false;
//    boolean HDMI2_StopCount          = false; 
    
    static boolean Monitor1_exist = false; 
    static boolean Monitor2_exist = false; 
    static boolean IsNBMonitor = false;
    static String Monitor1_device = ""; 
    static String Monitor2_device = ""; 
    boolean Monitor_Enabled_VGA1     = false; // true: enabled  , false: disabled   
    boolean Monitor_Enabled_HDMI1    = false; // true: enabled  , false: disabled       
    boolean Monitor_Enabled_HDMI2    = false; // true: enabled  , false: disabled  
    boolean Monitor_Enabled_eDP1     = false; // true: enabled  , false: disabled
    
    Dictionary dictMonitorWidth = new Hashtable();    
    Dictionary dictMonitorHeight = new Hashtable();    
    Dictionary dictMonitorStatus = new Hashtable();    
    Dictionary dictMonitorEnabled = new Hashtable();   
    
    boolean IsVGA1Disable            = false; // true: exists   , false: not exists  
    boolean IsHDMI1Disable           = false; // true: exists   , false: not exists  
    boolean IsHDMI2Disable           = false; // true: exists   , false: not exists  
    boolean IseDP1Disable            = false; // true: exists   , false: not exists      
    
    String[] Monitor_Type  = {"VGA1", "HDMI1", "HDMI2"};
    boolean[] MonitorActive = {false, false, false};
    ArrayList<String> eDP1List = new ArrayList<String>();
    ArrayList<String> HDMI1List = new ArrayList<String>();
    ArrayList<String> HDMI2List = new ArrayList<String>();
    ArrayList<String> VGA1List = new ArrayList<String>();
    boolean VGA1StopAdd = true;
    boolean HDMI1StopAdd = true;
    boolean HDMI2StopAdd = true;
    boolean eDP1StopAdd = true;
    List<String> Display_primary_resolution_list = FXCollections.observableArrayList();
    List<String> Display_second_resolution_list  = FXCollections.observableArrayList();     
    
    public Display(Stage primaryStage, Map<String, String> LangMap) { // https://stackoverflow.com/questions/11707066/timer-in-java-thread
        MainStage = primaryStage;
        WordMap   = LangMap;
        GBFunc    = new GBFunc(WordMap); 
        // DebugWrite("enter timer1 \n");

//        System.out.println("----------start----------");
//        showTime();            
        
        try {
            CheckAllDisplayDisconnected(); // 檢查是否一開始未接螢幕線
            ReadMonitorSh();               // 讀Monitor.Sh檔
            Sleep(150);
            // if(!AllDisconnected) {
                CheckDisplay();                // 第一次檢查全部螢幕狀態       
                DisplayDisable();              // Disable不需要的螢幕     
                // DuplicateSettings(temp_primary_name, temp_second_name, temp_primary_width, temp_primary_height, temp_second_width, temp_second_height);            
            // }
        } catch (IOException ex) {
            Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
        }                       
                
        //create timer task to get display information
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                 //System.out.println("");
//                for(int i = 0; i < Monitor_HWList.size(); i++) {                        
//                    ConnectedCheck(Monitor_HWList.get(i)); 
//                    EnabledCheck(Monitor_HWList.get(i));
//                    GetResolution(Monitor_HWList.get(i));
//                } 
            }
        };

        //create thread to check value
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {    
                    Sleep(100);
//                    System.out.println("----------1----------");
//                    showTime();                                
                    
                    
                    for(int i = 0; i < Monitor_HWList.size(); i++) {                        
                        ConnectedCheck(Monitor_HWList.get(i)); 
                        EnabledCheck(Monitor_HWList.get(i));
                        GetResolution(Monitor_HWList.get(i));
                    }                     
                    
                    Sleep(100);
//                    System.out.println("----------2----------");
//                    showTime();                    
                    if(IsNBMonitor) {
                        // System.out.println("run NB detect Monitor");
                        // SetSecondMonitorExtend(); // Set Extend
                        SetNBSecondMonitorDuplicate();
                        
                        if( (!Monitor_Enabled_VGA1 && Monitor_Status_VGA1 && !IsVGA1Disable) || 
                            (!Monitor_Enabled_HDMI1 && Monitor_Status_HDMI1 && !IsHDMI1Disable) || 
                            (!Monitor_Enabled_HDMI2 && Monitor_Status_HDMI2 && !IsHDMI2Disable) ) {
                            ActiveNBMonitor();
                        }                         
                    } else {
//                        System.out.println("Display_primary_resolution_list: " + Display_primary_resolution_list);  
                        CheckTCMonitorConnected(); // 檢查螢幕連接的個數   
                        
                        // Setting Display primary name & Display_second name                    
                        if(MonitorActive[0] && MonitorActive[1]) {           
                            Display_primary_name      = Display_VGA1_name;
                            primary_resolution_width  = VGA1_resolution_width;
                            primary_resolution_height = VGA1_resolution_height;
                            Display_second_name       = Display_HDMI1_name;
                            second_resolution_width   = HDMI1_resolution_width;
                            second_resolution_height  = HDMI1_resolution_height;                                                                                                
                        } else if (MonitorActive[1] && MonitorActive[2]) {
                            Display_primary_name      = Display_HDMI1_name;
                            primary_resolution_width  = HDMI1_resolution_width;
                            primary_resolution_height = HDMI1_resolution_height;
                            Display_second_name       = Display_HDMI2_name;
                            second_resolution_width   = HDMI2_resolution_width;
                            second_resolution_height  = HDMI2_resolution_height;                     
                        }  else if (MonitorActive[0] && MonitorActive[2]) {
                            Display_primary_name      = Display_VGA1_name;
                            primary_resolution_width  = VGA1_resolution_width;
                            primary_resolution_height = VGA1_resolution_height;
                            Display_second_name       = Display_HDMI2_name;
                            second_resolution_width   = HDMI2_resolution_width;
                            second_resolution_height  = HDMI2_resolution_height;                  
                        }                        
                        
                        // 開機後才接上2個畫面，重開機
                        if(AllDisconnected && AllMonitorConnected == 2) {                                                                                    
                            Reboot();                          
                            timer.cancel(); 
                            break;          
                        }                         
                        
                        ActiveTCMonitor();    
                        CheckMonitorDisconnected(); // 20019.02.18 新增FreeRDP for dual screen & usb redirection
                        
        

                        // 畫面調整
                        if(getMonitorExist2() && !Monitor_Setting_file) {
                            if(Display_primary_name != null && Display_second_name != null && primary_resolution_width != null && primary_resolution_height != null && second_resolution_width != null && second_resolution_height != null){
                                SetDisplay(Display_primary_name, Display_second_name, primary_resolution_width, primary_resolution_height, second_resolution_width, second_resolution_height);                                                                               
                            }                                                                                
                        }              
                    }
                    
//                    System.out.println("----------3----------");
//                    showTime();                        

                }
            }
        });

        timer = new Timer("DisplayTimer");              // create a new timer
        timer.scheduleAtFixedRate(timerTask, 30, 500); // start timer in 30ms to get display information

        t.start();                                      // start thread
    }    
    
    // 讀Monitor.Sh檔
    public void ReadMonitorSh() {
        File MonitorShFile = new File("/root/.screenlayout/monitor.sh");
        if(MonitorShFile.exists()) {            
            Monitor_Setting_file = true;
        } else {
            // Create monitor.sh // 2018.12.19 Display bug fix
            GB.AllDisplayOff = true;
        }
    }
   
    // 第一次檢查全部螢幕狀態
    public void CheckDisplay() throws IOException {
        boolean IsPrimary = false;
        boolean IsSecond  = false; 
        String[] primary_data_list;
        String[] second_data_list;        
        String primary_resolution;
        String second_resolution;        
        
        Process Get_Display_process = Runtime.getRuntime().exec("xrandr");
        try (BufferedReader output_Display_info = new BufferedReader (new InputStreamReader(Get_Display_process.getInputStream()))) {
            String line_Display                     = output_Display_info.readLine();  
            String[] Display_name_Line_list         = null;
            String[] Display_current_data_Line_list = null;             
            while(line_Display != null) { 
                /*********** 取得螢幕名稱 ***********/                
                if ( line_Display.contains("connected") && !line_Display.contains("disconnected")) {
                    if (!IsPrimary) {
                        Display_name_Line_list = line_Display.split(" ");
                        temp_primary_name = Display_name_Line_list[0]; 
                        setMonitor1(Display_name_Line_list[0]);        
                        // 撿查有HDMI就將HDMI1設為"--set audio on"
                        if(Display_name_Line_list[0].contains("HDMI1")) {
                            Runtime.getRuntime().exec("xrandr --output HDMI1 --set audio on");
                        } else if (Display_name_Line_list[0].contains("HDMI2")) {
                            Runtime.getRuntime().exec("xrandr --output HDMI2 --set audio on");
                        } else if (Display_name_Line_list[0].contains("eDP1")) {
                            IsNBMonitor = true;
                        }
                        
                        IsPrimary = true;   
                        setMonitorExist1(IsPrimary);
                    } else {
                        Display_name_Line_list = line_Display.split(" ");
                        temp_second_name = Display_name_Line_list[0];   
                        setMonitor2(Display_name_Line_list[0]);        
                        // 撿查有HDMI就將HDMI1設為"--set audio on"
                        if(Display_name_Line_list[0].contains("HDMI1")) {
                            Runtime.getRuntime().exec("xrandr --output HDMI1 --set audio on");
                        } else if (Display_name_Line_list[0].contains("HDMI2")) {
                            Runtime.getRuntime().exec("xrandr --output HDMI2 --set audio on");
                        } else if (Display_name_Line_list[0].contains("eDP1")) {
                            IsNBMonitor = true;
                        }                              
                        IsSecond = true;           
                        setMonitorExist2(IsSecond);
                    }                                                            
                }                 
                
                /*********** 取得目前解析度 ***********/
                if ( line_Display.contains("*") || line_Display.contains(" +")) {
                    if(!IsSecond) {
                        Display_current_data_Line_list     = line_Display.split(" ");
                        String Display_primary_current_resolution = Display_current_data_Line_list[3];                        
                        String[] primary_resolution_size            = Display_primary_current_resolution.split("x");
                        temp_primary_width                 = primary_resolution_size[0];
                        temp_primary_height                = primary_resolution_size[1];
                        setMonitorResolution(getMonitor1(), primary_resolution_size[0], primary_resolution_size[1]);
                    } else {
                        Display_current_data_Line_list    = line_Display.split(" ");
                        String Display_second_current_resolution = Display_current_data_Line_list[3];                        
                        String[] second_resolution_size            = Display_second_current_resolution.split("x");
                        temp_second_width                 = second_resolution_size[0];
                        temp_second_height                = second_resolution_size[1];        
                        setMonitorResolution(getMonitor2(), second_resolution_size[0], second_resolution_size[1]);                     
                    }                 
                }                 
                
                /*********** 取得支援解析度 List ***********/
                if (line_Display.startsWith("   ")) { 
                    if(!IsSecond) { 
                        primary_data_list  = line_Display.split(" ");
                        primary_resolution = primary_data_list[3];
                        if( !primary_resolution.contains("i") ){
                            Display_primary_resolution_list.add(primary_resolution);  
                        }                     
                    } else {
                        second_data_list  = line_Display.split(" ");
                        second_resolution = second_data_list[3];
                        if( !second_resolution.contains("i") ){
                            Display_second_resolution_list.add(second_resolution);  
                        }                           
                    }
                      
                }                
                

                
                // 避免在同步時，第2層蓋到第1層上面
                if ( line_Display.contains("connected") && !line_Display.contains("disconnected")) {
                    if (line_Display.contains("+0+0")) { 
                        GB.DuplicateSize++;                                         
                    }                                                              
                }                  
                
                line_Display = output_Display_info.readLine();
            }
            output_Display_info.close();        
        }        
        
        // Get monitor name and Information
        try (BufferedReader output_Display_info = new BufferedReader (new InputStreamReader(Runtime.getRuntime().exec("ls /sys/class/drm/").getInputStream()))) {
            String line_Display = output_Display_info.readLine();  
            while(line_Display != null) { 
                if (line_Display.contains("VGA") || line_Display.contains("HDMI") || line_Display.contains("eDP")) { // 2018.12.19 Display bug fix
                    Monitor_HWList.add(line_Display);
                }  
                
                line_Display = output_Display_info.readLine();
            }
            output_Display_info.close();        
        }         
    }     
    
    // 檢查是否一開始未接螢幕線
    public void CheckAllDisplayDisconnected() {
        File nodisplayFile = new File("/root/nodisplay.txt");
        if(nodisplayFile.exists()) {            
            AllDisconnected = true;
            try {            
                Runtime.getRuntime().exec("rm /root/.screenlayout/monitor.sh");
                // 2018.12.19 Display bug fix
                Runtime.getRuntime().exec("rm /root/.screenlayout/DisplayDisable_VGA1.txt");
                Runtime.getRuntime().exec("rm /root/.screenlayout/DisplayDisable_HDMI1.txt");
                Runtime.getRuntime().exec("rm /root/.screenlayout/DisplayDisable_HDMI2.txt");
                Runtime.getRuntime().exec("rm /root/.screenlayout/DisplayDisable_eDP1.txt");                
            } catch (IOException ex) {
                Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }    
        
    // Disable不需要的螢幕
    public void DisplayDisable() {
        File VGA1  = new File("/root/.screenlayout/DisplayDisable_VGA1.txt");
        File HDMI1 = new File("/root/.screenlayout/DisplayDisable_HDMI1.txt");
        File HDMI2 = new File("/root/.screenlayout/DisplayDisable_HDMI2.txt");
        File eDP1  = new File("/root/.screenlayout/DisplayDisable_eDP1.txt");
        if(VGA1.exists()) {            
            IsVGA1Disable = true;            
            OffDisplay("VGA1");
        } else if(HDMI1.exists()) {
            IsHDMI1Disable = true;
            OffDisplay("HDMI1");
        } else if(HDMI2.exists()) {
            IsHDMI2Disable = true;
            OffDisplay("HDMI2");
        } else if(eDP1.exists()) {
            IseDP1Disable = true;
            OffDisplay("eDP1");
        }        
    }     
    
    // Disable螢幕功能
    public void OffDisplay(String primary_name) {
        String OffDisplay_exec_string;
        OffDisplay_exec_string = "xrandr --output " + primary_name + " --off";
        try {        
            Runtime.getRuntime().exec(OffDisplay_exec_string);
        } catch (IOException ex) {
            Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
                
    // 同步Duplicate設定
    public void DuplicateSettings(String primary_name, String second_name, String primary_width, String primary_height, String second_width, String second_height) {
        int pw, ph, sw, sh;    
        
        if(primary_width == null) {
            pw = 0;
        } else {
            pw = Integer.parseInt(primary_width);
        }
        
        if(primary_height == null) {
            ph = 0;
        } else {
            ph = Integer.parseInt(primary_height);
        }

        if(second_width == null) {
            sw = 0;
        } else {
            sw = Integer.parseInt(second_width);
        }

        if(second_height == null) {
            sh = 0;
        } else {
            sh = Integer.parseInt(second_height);
        }          
        
        if(pw == 0 || ph == 0) {
            // monitor_exec_string += "xrandr --output " + primary_name + " --off " + "--output " + second_name + " --mode " + second_width + "x" + second_height + " --pos " + "0x0 --rotate normal";                
        } else if(sw == 0 || sh == 0) {
            // monitor_exec_string += "xrandr --output " + primary_name + " --mode " + primary_width + "x" + primary_height + " --pos 0x0 --rotate normal --output " + second_name + " --off";                
        } else { // Display Duplicate
            // monitor_exec_string += "xrandr --output " + Display_primary_name + " --mode " + primary_resolution_width + "x" + primary_resolution_height + " --pos 0x0 --rotate normal --output " + Display_second_name + " --mode " + second_resolution_width + "x" + second_resolution_height + " --pos " + primary_resolution_width + "x0 --rotate normal";                                          
            // OffDisplay(primary_name, second_name);
            if((pw * ph) < (sw * sh)) {
                GB.monitor_exec_string = "xrandr --output " + second_name + " --mode " + primary_width + "x" + primary_height + " --pos 0x0 --rotate normal";            
                // System.out.println(" 同步 第1螢幕 Duplicate \n");
            } else if ((pw * ph) > (sw * sh)) {
                GB.monitor_exec_string = "xrandr --output " + second_name + " --mode " + second_width + "x" + second_height + " --pos 0x0 --rotate normal";
                // System.out.println(" 同步 第2螢幕 Duplicate \n");
            } else if ((pw * ph) == (sw * sh)) {
                GB.monitor_exec_string = "xrandr --output " + second_name + " --mode " + primary_width + "x" + primary_height + " --pos 0x0 --rotate normal";            
                // System.out.println(" 同步 相同螢幕 Duplicate \n");
            } else {
                System.out.println(" 不同步 Non Duplicate \n");
            }             
        } 
        GB.monitoroff_exec_string = "xrandr --output " + second_name + " --off";            
    }  
    
    // 檢查螢幕連接的個數
    public void CheckTCMonitorConnected() {
        if(Monitor_Enabled_VGA1 && Monitor_Status_VGA1 && !MonitorActive[0]) { //!VGA1_StopCount
            AllMonitorConnected++;
            MonitorActive[0] = true; // VGA1_StopCount = true;
        }  
        
        if(Monitor_Enabled_HDMI1 && Monitor_Status_HDMI1 && !MonitorActive[1]) { //!HDMI1_StopCount
            AllMonitorConnected++;
            MonitorActive[1] = true; // HDMI1_StopCount = true; 
        }       
        
        if(Monitor_Enabled_HDMI2 && Monitor_Status_HDMI2  && !MonitorActive[2]) { //!HDMI2_StopCount
            AllMonitorConnected++;
            MonitorActive[2] = true; // HDMI2_StopCount = true; 
        }        
    }
        
    // 啟動NB螢幕
    public void ActiveNBMonitor() {
        for (int i = 0; i < Monitor_Type.length; i++) {
             if(dictMonitorEnabled.get(Monitor_Type[i]) != null && dictMonitorStatus.get(Monitor_Type[i]) != null) {
                if(!Boolean.parseBoolean(dictMonitorEnabled.get(Monitor_Type[i]).toString()) && Boolean.parseBoolean(dictMonitorStatus.get(Monitor_Type[i]).toString())) {  
                    if(dictMonitorWidth.get(Monitor_Type[i]) != null && dictMonitorHeight.get(Monitor_Type[i]) != null) {
                        RestartMonitor(Monitor_Type[i], dictMonitorWidth.get(Monitor_Type[i]).toString(), dictMonitorHeight.get(Monitor_Type[i]).toString());
                        System.exit(1);                 
                    }
                }         
            }
        }    
    }    
    
    // 啟動TC螢幕
    public void ActiveTCMonitor() {
        if(!Monitor_Enabled_VGA1 && Monitor_Status_VGA1 && !IsVGA1Disable) {                          
            RestartMonitor("VGA1", VGA1_resolution_width, VGA1_resolution_height);
            System.exit(1);  
        }       

        if(!Monitor_Enabled_HDMI1 && Monitor_Status_HDMI1 && !IsHDMI1Disable) {
            RestartMonitor("HDMI1", HDMI1_resolution_width, HDMI1_resolution_height);
            System.exit(1);                            
        }          

        if(!Monitor_Enabled_HDMI2 && Monitor_Status_HDMI2 && !IsHDMI2Disable) {                                                         
            RestartMonitor("HDMI2", HDMI2_resolution_width, HDMI2_resolution_height);
            System.exit(1); 
        }     
    }
    
    // 啟動螢幕後需要重啟
    public void RestartMonitor(String _name, String _width, String _height) {   
        String execStr = "xrandr --output " + _name + " --mode " + _width + "x" + _height;
        // System.out.println(execStr);
        try {                                        
            Runtime.getRuntime().exec("rm /root/.screenlayout/monitor.sh");
            Runtime.getRuntime().exec(execStr);                
            Runtime.getRuntime().exec("killall remote-viewer");
            Runtime.getRuntime().exec("killall remmina");
            Runtime.getRuntime().exec("killall xfreerdp");
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","/root/restartui.sh"}); 
        } catch (IOException ex) {
            Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
        }
   
    }
    
    // 重啟TC
    public void Reboot() {
        try {
            Runtime.getRuntime().exec("reboot");
        } catch (IOException ex) {
            Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }    
    
    // Thread休眠時間
    public void Sleep(int _time) {
        try { 
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    /*-----------------------------------Polling Function-----------------------------------*/
    // 啟動NB的第二個螢幕
    public void SetSecondMonitorExtend() {
        if(getMonitorExist2() && !Monitor_Setting_file) {
            String execStr = "xrandr --output " + getMonitor1() + " --mode " + dictMonitorWidth.get(getMonitor1()).toString() + "x" + dictMonitorHeight.get(getMonitor1()).toString() + " --pos 0x0 --rotate normal --output " + getMonitor2() + " --mode " + dictMonitorWidth.get(getMonitor2()).toString() + "x" + dictMonitorHeight.get(getMonitor2()).toString() + " --pos " + dictMonitorWidth.get(getMonitor1()).toString() + "x0 --rotate normal";                            
            try {
                Runtime.getRuntime().exec(execStr);
            } catch (IOException ex) {
                Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }        
   
    // 螢幕connected檢查
    public void ConnectedCheck(String detect_name) {
        try (BufferedReader Display_status = new BufferedReader (new InputStreamReader(Runtime.getRuntime().exec("cat /sys/class/drm/" + detect_name + "/status").getInputStream()))) {
            String line_Display_status = Display_status.readLine();
            while(line_Display_status != null) {                   
                if ( line_Display_status.contains("connected") && !line_Display_status.contains("disconnected")) {                                                        
                    switch (detect_name) {
                        case "card0-VGA-1":   
                            Display_VGA1_name   = "VGA1";
                            Monitor_Status_VGA1 = true;
                            dictMonitorStatus.put("VGA1", true);
                            break;
                        case "card0-HDMI-A-1":                            
                            Display_HDMI1_name   = "HDMI1";
                            Monitor_Status_HDMI1 = true;
                            dictMonitorStatus.put("HDMI1", true);
                            break;
                        case "card0-HDMI-A-2":
                            Display_HDMI2_name   = "HDMI2";
                            Monitor_Status_HDMI2 = true;
                            dictMonitorStatus.put("HDMI2", true);
                            break;
                        default:
                            break;
                    }                                                                                                                
                } else { // 20019.02.18 新增FreeRDP for dual screen & usb redirection
                    switch (detect_name) {
                        case "card0-VGA-1":   
                            Monitor_Status_VGA1 = false;
                            break;
                        case "card0-HDMI-A-1":                            
                            Monitor_Status_HDMI1 = false;
                            break;
                        case "card0-HDMI-A-2":
                            Monitor_Status_HDMI2 = false;
                            break;
                        default:
                            break;
                    }                 
                
                }                             
                        
                line_Display_status = Display_status.readLine();                    
            }
            
            Display_status.close();        
        } catch (IOException ex) {   
            Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    // 螢幕enabled檢查
    public void EnabledCheck(String detect_name) {
        try (BufferedReader Display_enabled = new BufferedReader (new InputStreamReader(Runtime.getRuntime().exec("cat /sys/class/drm/" + detect_name + "/enabled").getInputStream()))) {
            String line_Display_enabled = Display_enabled.readLine();
            while(line_Display_enabled != null) {                   
                if ( line_Display_enabled.contains("enabled")) {                                                                
                    switch (detect_name) {
                        case "card0-VGA-1":
                            Monitor_Enabled_VGA1 = true;
                            dictMonitorEnabled.put("VGA1", true);
                            break;
                        case "card0-HDMI-A-1":
                            Monitor_Enabled_HDMI1 = true;
                            dictMonitorEnabled.put("HDMI1", true);
                            break;
                        case "card0-HDMI-A-2":
                            Monitor_Enabled_HDMI2 = true;
                            dictMonitorEnabled.put("HDMI2", true);
                            break;
                        case "card0-eDP-1":
                            Monitor_Enabled_eDP1 = true;
                            dictMonitorEnabled.put("eDP1", true);
                            break;                            
                        default:
                            break;
                    }                                  
                } else {
                    switch (detect_name) {
                        case "card0-VGA-1":
                            Monitor_Enabled_VGA1 = false;
                            dictMonitorEnabled.put("VGA1", false);
                            break;
                        case "card0-HDMI-A-1":
                            Monitor_Enabled_HDMI1 = false;
                            dictMonitorEnabled.put("HDMI1", false);
                            break;
                        case "card0-HDMI-A-2":
                            Monitor_Enabled_HDMI2 = false;
                            dictMonitorEnabled.put("HDMI2", false);
                            break;
                        case "card0-eDP-1":
                            Monitor_Enabled_eDP1 = false;
                            dictMonitorEnabled.put("eDP1", false);
                            break;                              
                        default:
                            break;
                    }             
                }                                                     
                            
                line_Display_enabled = Display_enabled.readLine();                    
            }
                        
            Display_enabled.close();        
        } catch (IOException ex) {   
            Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }  
    
    // 螢幕resolution檢查 // Get resolution // cat /sys/class/drm/card0-VGA-1/modes | head -1            
    public void GetResolution(String detect_name) {
        try (BufferedReader Display_Resolution = new BufferedReader (new InputStreamReader(Runtime.getRuntime().exec("cat /sys/class/drm/" + detect_name + "/modes").getInputStream()))) {
            String line_Display_Resolution = Display_Resolution.readLine();
            while(line_Display_Resolution != null) {                
                switch (detect_name) {
                    case "card0-VGA-1":   
                        if(!VGA1_FirstData) {
                            VGA1_FirstData = true;
                            String[] VGA1_resolution_size   = line_Display_Resolution.split("x");
                            VGA1_resolution_width  = VGA1_resolution_size[0];
                            VGA1_resolution_height = VGA1_resolution_size[1];    
                            dictMonitorWidth.put("VGA1", VGA1_resolution_size[0]);
                            dictMonitorHeight.put("VGA1", VGA1_resolution_size[1]);  
                        }
                        if(VGA1StopAdd)
                            VGA1List.add(line_Display_Resolution);
                        break;
                    case "card0-HDMI-A-1":                            
                        if(!HDMI1_FirstData) {
                            HDMI1_FirstData = true;
                            String[] HDMI1_resolution_size   = line_Display_Resolution.split("x");
                            HDMI1_resolution_width  = HDMI1_resolution_size[0];
                            HDMI1_resolution_height = HDMI1_resolution_size[1];    
                            dictMonitorWidth.put("HDMI1", HDMI1_resolution_size[0]);
                            dictMonitorHeight.put("HDMI1", HDMI1_resolution_size[1]); 
                        }
                        if(HDMI1StopAdd)
                            HDMI1List.add(line_Display_Resolution);
                        break;
                    case "card0-HDMI-A-2":
                        if(!HDMI2_FirstData) {
                            HDMI2_FirstData = true;
                            String[] HDMI2_resolution_size   = line_Display_Resolution.split("x");
                            HDMI2_resolution_width  = HDMI2_resolution_size[0];
                            HDMI2_resolution_height = HDMI2_resolution_size[1];  
                            dictMonitorWidth.put("HDMI2", HDMI2_resolution_size[0]);
                            dictMonitorHeight.put("HDMI2", HDMI2_resolution_size[1]);                             
                        }
                        if(HDMI2StopAdd)
                            HDMI2List.add(line_Display_Resolution);
                        break;
                    case "card0-eDP-1":
                        if(eDP1StopAdd)
                            eDP1List.add(line_Display_Resolution);
                        break;                        
                    default:
                        break;
                    }                 
                
                line_Display_Resolution = Display_Resolution.readLine();                   
            }

            Display_Resolution.close();
            StopAddRes(detect_name); 
        } catch (IOException ex) {   
            Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }   
            
    /*-----------------------------------Get Data Function-----------------------------------*/    
    
    public static synchronized String getMonitor1() {
        return Monitor1_device;
    }

    public static synchronized void setMonitor1(String name) {
        Monitor1_device = name;
    }     
    
    public static synchronized String getMonitor2() {
        return Monitor2_device;
    }

    public static synchronized void setMonitor2(String name) {
        Monitor2_device = name;
    }        
    
    public static synchronized boolean getMonitorExist1() {
        return Monitor1_exist;
    }

    public static synchronized void setMonitorExist1(boolean _exist) {
        Monitor1_exist = _exist;
    }       
    
    public static synchronized boolean getMonitorExist2() {
        return Monitor2_exist;
    }

    public static synchronized void setMonitorExist2(boolean _exist) {
        Monitor2_exist = _exist;
    }    
    
    public void setMonitorResolution(String _name, String _width, String _height) {
        dictMonitorWidth.put(_name, _width);
        dictMonitorHeight.put(_name, _height);              
    }
    
    // Write Monitor.Sh file
    public void WriteMonitorSh(String script) {
        try {
            File MonitorShFile = new File("/root/.screenlayout/monitor.sh");
            
            if(MonitorShFile.exists()) {
                MonitorShFile.delete();
                MonitorShFile = null;
            }
            
            try(FileWriter ShFileWriter = new FileWriter("/root/.screenlayout/monitor.sh")) {
                ShFileWriter.write("#!/bin/sh \n" + script + "\n");
                ShFileWriter.flush();
                ShFileWriter.close();  
            }   
            
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","chmod +x /root/.screenlayout/monitor.sh"});
        }
        catch(Exception e) {/* e.printStackTrace();*/}         
    }      
    
    // 執行螢幕設定
    public void SetDisplay(String primary_name, String second_name, String primary_width, String primary_height, String second_width, String second_height) {
        int pw, ph, sw, sh;    
        
        if(primary_width == null) {
            pw = 0;
        } else {
            pw = Integer.parseInt(primary_width);
        }
        
        if(primary_height == null) {
            ph = 0;
        } else {
            ph = Integer.parseInt(primary_height);
        }

        if(second_width == null) {
            sw = 0;
        } else {
            sw = Integer.parseInt(second_width);
        }

        if(second_height == null) {
            sh = 0;
        } else {
            sh = Integer.parseInt(second_height);
        }          
        
        if(pw == 0 || ph == 0) {
            monitor_exec_string += "xrandr --output " + primary_name + " --off " + "--output " + second_name + " --mode " + second_width + "x" + second_height + " --pos " + "0x0 --rotate normal";                
        } else if(sw == 0 || sh == 0) {
            monitor_exec_string += "xrandr --output " + primary_name + " --mode " + primary_width + "x" + primary_height + " --pos 0x0 --rotate normal --output " + second_name + " --off";                
        } else { // Display Duplicate
            // monitor_exec_string += "xrandr --output " + Display_primary_name + " --mode " + primary_resolution_width + "x" + primary_resolution_height + " --pos 0x0 --rotate normal --output " + Display_second_name + " --mode " + second_resolution_width + "x" + second_resolution_height + " --pos " + primary_resolution_width + "x0 --rotate normal";                                          
            // OffDisplay(primary_name, second_name);
            
            
            String Resolution = GetMonitorResolution();
            monitor_exec_string = "xrandr --output " + primary_name + " --mode " + Resolution + " --pos 0x0 --rotate normal --output " + second_name + " --mode " + Resolution + " --pos 0x0 --rotate normal";              
            
            /*
            if((pw * ph) < (sw * sh)) {
                monitor_exec_string = "xrandr --output " + primary_name + " --mode " + primary_width + "x" + primary_height + " --pos 0x0 --rotate normal --output " + second_name + " --mode " + primary_width + "x" + primary_height + " --pos 0x0 --rotate normal";            
                // System.out.println(" 同步 第1螢幕 Duplicate \n");
            } else if ((pw * ph) > (sw * sh)) {
                monitor_exec_string = "xrandr --output " + primary_name + " --mode " + second_width + "x" + second_height + " --pos 0x0 --rotate normal --output " + second_name + " --mode " + second_width + "x" + second_height + " --pos 0x0 --rotate normal";
                // System.out.println(" 同步 第2螢幕 Duplicate \n");
            } else if ((pw * ph) == (sw * sh)) {
                monitor_exec_string = "xrandr --output " + primary_name + " --mode " + primary_width + "x" + primary_height + " --pos 0x0 --rotate normal --output " + second_name + " --mode " + primary_width + "x" + primary_height + " --pos 0x0 --rotate normal";            
                // System.out.println(" 同步 相同螢幕 Duplicate \n");
            } else {
                System.out.println(" 不同步 Non Duplicate \n");
            }   
            */
        }      
                
        try {
            WriteMonitorSh(monitor_exec_string);
            try {
                sleep(250);
            } catch (InterruptedException ex) {
                Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
            }
            Runtime.getRuntime().exec(monitor_exec_string);
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","/root/restartui.sh"});
        } catch (IOException ex) {
            Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.exit(1); 
    }  
    
    public String GetMonitorResolution() {
        String Resolution = "";
        boolean Stop = false;
//        System.out.println("Display_primary_resolution_list: " + Display_primary_resolution_list);  
//        System.out.println("Display_second_resolution_list: " + Display_second_resolution_list);          

        for(int i = 0; i < Display_primary_resolution_list.size(); i++) {               
            if(Stop)                
                break;
                                      
            if(Display_primary_resolution_list.get(i).equals("1024x768")) {
                Resolution = "1024x768";
                break;
            }             
            
            for (String List : Display_second_resolution_list) {
                // 20019.02.18 新增FreeRDP for dual screen & usb redirection                
                if(Display_primary_resolution_list.get(i).equals(List)) {
                    Resolution = List;
                    Stop = true;
                    break;
                }                  
            } 
            
        }  
        
        if("".equals(Resolution))
            Resolution = "1024x768";
                    
        return Resolution;
    }     
           
    // debug 寫檔案
    public void DebugWrite(String s) {
        
        try {
            File debugFile = new File("/root/dfile.txt");
            if(debugFile.exists()) {
                debugFile.delete();
                debugFile = null;
            }

            try(FileWriter debugWriter = new FileWriter("/root/dfile.txt")) {
                debugWriter.write(s);
                debugWriter.flush();
                debugWriter.close();
            }   
        }
        catch(Exception e) {
           // e.printStackTrace();
        }
    }        
    
    public void SetNBSecondMonitorDuplicate() {       
        if(getMonitorExist2() && !Monitor_Setting_file) {
            String Resolution = GetNBSecondMonitorResolution();              
            String execStr = "xrandr --output " + getMonitor1() + " --mode " + Resolution + " --pos 0x0 --rotate normal --output " + getMonitor2() + " --mode " + Resolution + " --pos " + "0x0 --rotate normal";                                      
            //System.out.println("Duplicate: " + execStr);
            WriteMonitorSh(execStr);
            Sleep(500);
            try {
                Runtime.getRuntime().exec(execStr);
                Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","/root/restartui.sh"});
            } catch (IOException ex) {
                Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
            }
                    System.exit(1); 
        } 
    }        
    
    public String GetNBSecondMonitorResolution() {
        String Resolution = "";
        boolean Stop = false;
//        System.out.println("eDP1List: " + eDP1List);  
//        System.out.println("HDMI1List: " + HDMI1List);          
//        System.out.println("VGA1List: " + VGA1List);          
        for(int i = 0; i < eDP1List.size(); i++) {  
            
            if(Stop)                
                break;            
            
            if(eDP1List.get(i).equals("1024x768")) {
                Resolution = "1024x768";
                break;
            }                            
            
            if(HDMI1List.size() != 0) {
                for (String HDMI1List1 : HDMI1List) {
                    if(HDMI1List1.equals(eDP1List.get(i))) {
                        Resolution = HDMI1List1;
                        Stop = true;
                        break;
                    }                    
                }                    
            }            
            else if(VGA1List.size() != 0) {
                for (String VGA1List1 : VGA1List) {
                    if(VGA1List1.equals(eDP1List.get(i))) {
                        Resolution = VGA1List1;
                        Stop = true;
                        break;
                    }
                }
            }            
            else if(HDMI2List.size() != 0) {
                for (String HDMI1List2 : HDMI2List) {
                    if(HDMI1List2.equals(eDP1List.get(i))) {
                        Resolution = HDMI1List2;
                        Stop = true;
                        break;
                    }
                }
            }  
        }  
        
        if("".equals(Resolution))
            Resolution = "1024x768";
                    
        return Resolution;
    }    
    
    public void StopAddRes(String _name) {
        switch (_name) {
            case "card0-VGA-1":   
                VGA1StopAdd = false;
                break;
            case "card0-HDMI-A-1":                            
                HDMI1StopAdd = false;
                break;
            case "card0-HDMI-A-2":                            
                HDMI2StopAdd = false;
                break;                
            case "card0-eDP-1":
                eDP1StopAdd = false;
                break;                        
            default:
                break;
        }  
    }        
    
    public void SetMonitorsh() {
        if(getMonitorExist2() && !Monitor_Setting_file) {

            String execStr = "xrandr --output " + getMonitor1() + " --mode " + dictMonitorWidth.get(getMonitor1()).toString() + "x" + dictMonitorHeight.get(getMonitor1()).toString() + " --pos 0x0 --rotate normal --output " + getMonitor2() + " --mode " + dictMonitorWidth.get(getMonitor2()).toString() + "x" + dictMonitorHeight.get(getMonitor2()).toString() + " --pos " + dictMonitorWidth.get(getMonitor1()).toString() + "x0 --rotate normal";                            
            try {
                Runtime.getRuntime().exec(execStr);
            } catch (IOException ex) {
                Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }      
    
    public void SetARandRString(String _name1, String _width1, String _height1, String _name2, String _width2, String _height2) {
        String _script = "xrandr --output " + _name1 + " --mode " + _width1 + "x" + _height1 + " --pos 0x0 --rotate normal --output " + _name2 + " --mode " + _width2 + "x" + _height2 + " --pos " + _width1 + "x0 --rotate normal";    
        WriteMonitorSh(_script);
    }
        
    // 20019.02.18 新增FreeRDP for dual screen & usb redirection
    public void CheckMonitorDisconnected() {
        if(Monitor_Enabled_VGA1 && !Monitor_Status_VGA1) {
            IsVGA1Disable = false;
            killallViewer();
            OffDisplay("VGA1");
        } else if(Monitor_Enabled_HDMI1 && !Monitor_Status_HDMI1) {
            IsHDMI1Disable = false;
            killallViewer();
            OffDisplay("HDMI1");
        } else if(Monitor_Enabled_HDMI2 && !Monitor_Status_HDMI2) {
            IsHDMI2Disable = false;
            killallViewer();
            OffDisplay("HDMI2");
        }
    
    }    
    // 20019.02.18 新增FreeRDP for dual screen & usb redirection
    public void killallViewer() {
        try {                                          
            Runtime.getRuntime().exec("killall remote-viewer");
            Runtime.getRuntime().exec("killall remmina");
            Runtime.getRuntime().exec("killall xfreerdp");
        } catch (IOException ex) {
            Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    public void showTime() {
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
	System.out.println("*****************" + dateFormat.format(date) + "*****************"); //2016/11/16 12:08:43    
    }    

}
