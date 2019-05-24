/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup; 
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import java.net.InetAddress; 
import java.util.Enumeration;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.WindowEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 *
 * @author victor
 */
public class ThinClient {
    
    public Map<String, String> WordMap=new HashMap<>();
    
    public String CurrentIP;
    public String CurrentUserName;
    public String CurrentPasseard;
    public String OriginalPassword;
    public String NewPassword;
    public String ConfirmPassword;
    private Button Leave;
    private Button Cancel;
    private Button Confirm;
    private Button Monitor;
    private Button Check;
    private Button Update;       
    private String Display01_name;
    private String Display02_name;
    private String Display01_current_data;
    private String Display02_current_data;
    private Process Display01_name_process;   
    private Process Display02_name_process;
    private String[] Display01_current_params;   
    private Process Display01_current_process;
    public String[] HDMI01_resolution_params;
    public Process HDMI01_resolution_process;
    private String HDMI01_setting_params;
    private String HDMI02_setting_params;
    private Process HDMI01_setting_process; 
    private Process HDMI02_setting_process;
    public String[] HDMI01_resolution_list;
    public String[] HDMI02_resolution_list;
    private InetAddress IPAddr;    
    private String DHCP_submask;
    private String DHCP_IP;
    private String DHCP_Gateway; 
    private String DHCP_DNS01;
    private String DHCP_DNS02;
    private String[] DHCP_DNS01_Line_list;
    private String[] DHCP_DNS02_Line_list; 
    private String[] StaticIP_DNS01_Line_list;
    private String[] StaticIP_DNS02_Line_list;  
    private boolean DNS_or_StaticIP;
    private short DHCP_prflen_ipv4;
    private String StaticIP_submask;
    private String StaticIP_IP;
    private String StaticIP_Gateway; 
    private String StaticIP_DNS01;
    private String StaticIP_DNS02;
    private int StaticIP_prflen_ipv4;
    private String StaticIP_Final_mask_cidr;
    private InetAddress StaticIP_mask_cidr;
    private ToggleGroup group;
    private RadioButton rb1_DHCP;
    private RadioButton rb2_StaticIP;
    private TextField IPv4_IP_TextField;
    private TextField IPv4_Mask_TextField;
    private TextField IPv4_Getway_TextField;
    private TextField IPv4_DNS1_TextField;
    private TextField IPv4_DNS2_TextField;
    private String HDMI01_current_data;
    private String dns127;
    private String StaticIP_submask_Setting;
    private String StaticIP_IP_Setting;
    private String StaticIP_Gateway_Setting; 
    private String StaticIP_DNS01_Setting;
    private String StaticIP_DNS02_Setting;    
    private String HDMI01_Resolution_selectItem;    
    private String HDMI02_Resolution_selectItem;    
    public String Display_params; 
    public Process Display_process; 
    private String ethernet_name_DHCP;
    private String ethernet_name_StaticIP;
    private Stage MainStage;
    private boolean ModifyStaticIP_IP;
    private boolean ModifyStaticIP_submask;
    private boolean ModifyStaticIP_Gateway;
    private boolean ModifyStaticIP_DNS01;
    private boolean ModifyStaticIP_DNS02;
    private boolean Path_change = true;
    private boolean Lock_Path_change = true;
    public boolean Dispaly01_change = true;
    public boolean Dispaly02_change = false;
    private Tab tab01;
    private Tab tab01_wifi; // 2018.04.13 william wifi實作
    private Tab tab02;
    private Tab tab03;
    private Tab tab04_KB;
    private Tab tabNull = new Tab();
    private final UUID TC_uniqueKey = UUID.randomUUID();
    private boolean Lock_Check = false;
    private boolean Lock_Update = false;
    private double screenCenterX;
    private double screenCenterY; 
    // 2018.04.13 william wifi實作
    private ToggleGroup wifi_group;
    private RadioButton wifi_rb1_DHCP;
    private RadioButton wifi_rb2_StaticIP;   
    private TextField wifi_IPv4_IP_TextField;
    private TextField wifi_IPv4_Mask_TextField;
    private TextField wifi_IPv4_Getway_TextField;
    private TextField wifi_IPv4_DNS1_TextField;
    private TextField wifi_IPv4_DNS2_TextField;    
    private Button wifi_Confirm_IPv4Pane;    
    private Button wifi_Connect; 
    private Button wifi_Disconnect;  
    private Button wifi_Leave_IPv4Pane;      
    private String wifi_StaticIP_submask_Setting;
    private String wifi_StaticIP_IP_Setting;
    private String wifi_StaticIP_Gateway_Setting; 
    private String wifi_StaticIP_DNS01_Setting;
    private String wifi_StaticIP_DNS02_Setting;     
    private String wifi_StaticIP_submask;
    private String wifi_StaticIP_IP;
    private String wifi_StaticIP_Gateway; 
    private String wifi_StaticIP_DNS01;
    private String wifi_StaticIP_DNS02;    
    private boolean wifi_ModifyStaticIP_IP;
    private boolean wifi_ModifyStaticIP_submask;
    private boolean wifi_ModifyStaticIP_Gateway;
    private boolean wifi_ModifyStaticIP_DNS01;
    private boolean wifi_ModifyStaticIP_DNS02;    
    private boolean wifi_DNS_or_StaticIP;
    private String wifi_DHCP_submask;
    private String wifi_DHCP_IP;
    private String wifi_DHCP_Gateway; 
    private String wifi_DHCP_DNS01;
    private String wifi_DHCP_DNS02;    
    private String[] wifi_DHCP_DNS01_Line_list;
    private String[] wifi_DHCP_DNS02_Line_list; 
    private String[] wifi_StaticIP_DNS01_Line_list;
    private String[] wifi_StaticIP_DNS02_Line_list;      
    private short wifi_DHCP_prflen_ipv4;  
    private int wifi_StaticIP_prflen_ipv4;   
    private String wifi_StaticIP_Final_mask_cidr;
    public TextField wifi_Name_TextField;
    private PasswordField wifi_Password_TextField;
    private TextField textField01;      
    private Button wifi_list;     
    private GetWifiInfo wifi;
    private String wifi_ethernet_name_DHCP;
    private String wifi_ethernet_name_StaticIP;
    private InetAddress wifi_StaticIP_mask_cidr;
    private boolean Get_IPV4_flag;
    String get_wifi_name;
    String temp_wifi_name;
    String get_wifi_pwd;
    private ToggleButton buttonPw = new ToggleButton();    
    private Image EyeClose;
    private Image EyeOpen;      
    private  boolean changeAfterSaved = true;
    private StackPane stack = new StackPane(); 
    private Label Search_Wifi_title;                                  
    private GridPane Search_WifiGP;                                   
    private ProgressIndicator p1;    
    GridPane grid = new GridPane();
    private BorderPane rootPane; 
    Label wifi_name_title;    
    Label wifi_pw_title;

    
    //Thread 宣告
    private CheckVersion CV;
    private SystemAlert SA;
    private PingCheckVersion PCV;
    
    
    private String Version_num ;
    private String Address;
    
    private String Port; // 2017.08.10 william IP增加port欄位，預設443
    private String CurrentIP_Port; // 2017.08.10 william IP增加port欄位，預設443
    
    public GB GB; // 2017.06.09 william Disable click many times will show mutil Dialog
    private boolean Disconnected_flag = false; // 2017.07.11 william remove internet plug & DNS Exception fix
    private boolean DNS_or_StaticIP_flag = true; // 2017.07.11 william remove internet plug & DNS Exception fix
    
    JSONObject IPStatus;  
    JSONObject StaticIPStatus;
    
    private Button Confirm_IPv4Pane;    // 2018.01.15 william 修改TC設定的按鈕邏輯   
    private Button Confirm_DisplayPane; // 2018.01.15 william 修改TC設定的按鈕邏輯   
    private Button Confirm_SystemPane;  // 2018.01.15 william 修改TC設定的按鈕邏輯   
    private Button Leave_IPv4Pane;      // 2018.01.15 william 修改TC設定的按鈕邏輯   
    private Button Leave_DisplayPane;   // 2018.01.15 william 修改TC設定的按鈕邏輯   
    private Button Leave_SystemPane;    // 2018.01.15 william 修改TC設定的按鈕邏輯   
    
    private Button Set_DisplayPane;      // 2018.01.29 william 雙螢幕實作   
    private Button Advanced_DisplayPane; // 2018.01.29 william 雙螢幕實作
    // 讀寫檔使用
    JSONObject jb;                      // 2018.01.29 william 雙螢幕實作   
    public boolean IsInstallArandr;     // 2018.01.29 william 雙螢幕實作   
    Process install_arandr_process;     // 2018.01.29 william 雙螢幕實作   
    public Process run_arandr_process;  // 2018.01.29 william 雙螢幕實作
    public int arandr_returncode = -1;  // 2018.01.29 william 雙螢幕實作
    /***** 2018.02.01 william 雙螢幕實作 *****/
    Label primary_Resolution_label; 
    Label second_Resolution_label;
    Label primary_Colon_label; 
    Label second_Colon_label;    
    private Process Get_Display_process;
    private ComboBox primary_ResolutionSelection;
    private ComboBox second_ResolutionSelection;   
    private String Display_primary_name;
    private String Display_second_name;    
    private String Display_primary_current_resolution;
    private String Display_second_current_resolution;        
    private String primary_resolution_width;
    private String primary_resolution_height;
    private String second_resolution_width;     
    private String second_resolution_height;    
    public String[] primary_resolution_size;
    public String[] second_resolution_size;    
    ObservableList<String> Display01_ResolutionList;
    ObservableList<String> Display02_ResolutionList;
    int display_count = 0;
    String[] primary_data_list;
    String[] second_data_list;
    public String[] primary_resolution_list;
    public String[] second_resolution_list;
    String primary_resolution;
    String second_resolution;
    boolean IsPrimary = false;
    boolean IsSecond  = false;
    List<String> Display_primary_resolution_list = FXCollections.observableArrayList();
    List<String> Display_second_resolution_list  = FXCollections.observableArrayList(); 
    public CheckBox checkBox_Primary;
    public CheckBox checkBox_Second;
    public CheckBox checkBox_Extend;    
    public CheckBox checkBox_Duplicate;
    private ToggleGroup display_group;
    private RadioButton rb_display01;
    private RadioButton rb_display02;
    private RadioButton rb_display_Extend;
    private RadioButton rb_display_Duplicate;    
    boolean Primary_disable = false;
    boolean Second_disable = false;
    boolean Duplicate_enable = false;
    boolean Primary_Duplicate = false;
    boolean Second_Duplicate = false;
    int select_display = -1;
    public String monitor_exec_string;
    private Button Display_Exchange;   
    Label Resolution_Title; 
    Label Display_Settings_title; 
    String temp_primary_name;
    String temp_second_name;  
    // 設定畫面的Layout
    // 1. 解析度設定
    HBox Resolution_Title_box;
    HBox Dispaly01_name_box; // 第一個解析度名稱的HBox
    HBox Dispaly02_name_box; // 第二個解析度名稱的HBox
    // 2. 畫面類型
    HBox Display_Settings_title_box; 
    HBox rbHBox; 
    HBox rbHBox2;
    // 3. 按鈕
    HBox DisplayButton;
     /*****Pattern:檢查IP是否符合正確格式*****/
    private static final Pattern PATTERN = Pattern.compile(
        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    
    List<String> Display01_resolution_list =FXCollections.observableArrayList();
    List<String> Display02_resolution_list =FXCollections.observableArrayList();
    
    HBox SystemButton = new HBox();
    
    Stage ThinClientStage = new Stage();
    
    // Arandr modify 2018.05.31
    public boolean arandr_closeFlag = true;
    public boolean arandr_TimerFlag = false;
    
    // 2019.02.12 Get device mac address
    private TextField MAC_TextField;
    String usernationSelect = "";
    
    public String wlanName = "";
    String wifi_security_type = "";
    Label System_Ver_AlertMessage;
    
    //正式用
    public ThinClient(Stage primaryStage, Map<String, String> LangMap, String IP_Addr,String IP_Port, boolean wifiFlag)throws MalformedURLException, IOException { // 2017.08.10 william IP增加port欄位，預設443   
        
        MainStage = primaryStage;
        WordMap=LangMap;  
        Address=IP_Addr;
        Port = IP_Port; // 2017.08.10 william IP增加port欄位，預設443
        //CurrentIP=IPAddr;
        //CurrentUserName=Uname;
        //CurrentPasseard=password;
        CV = new CheckVersion(WordMap);
        SA = new SystemAlert(WordMap);
        PCV = new PingCheckVersion(WordMap);
        
        GB.wifi_name = ""; // 2018.04.13 william wifi實作
        GB.wifi_security_type = ""; // 2018.04.13 william wifi實作
        
        //create thread to check value // 2018.04.13 william wifi實作
        /*
        t = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {                                                                                                    
                    try {  
                        
                        Thread.sleep(500);
                        //System.out.print("test");
                        if(!GB.wifi_name.isEmpty()) {
                            wifi_Name_TextField.setText(GB.wifi_name); 
                            // GB.wifi_stop_setvalue = false;
                        }

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });      
        t.start();
        */
        
       
        
    
        
      
        
        
        /************顯示 明碼 暗碼*************/ // 2018.04.13 william wifi實作
        EyeClose=new Image("images/CloseEye.png",22,22,false, false);     
        EyeOpen=new Image("images/OpenEye5.png",22,22,false, false);        
        
        try {
            LoadInstalllog(); // 2018.01.29 william 雙螢幕實作     
            LoadLastIPStatus();
            if(IPStatus_Type()){
                DNS_or_StaticIP_flag = true;
            }
            else {
                DNS_or_StaticIP_flag = false;
            }

        } catch (ParseException ex) {
            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        HBox Header=new HBox();
        Label TCTitle=new Label(WordMap.get("ThinClient")); 
        Header.getChildren().addAll(TCTitle);
        Header.setAlignment(Pos.CENTER);       
        Header.setId("Title");
        //grid.add(Header, 0, 0,10,1);
  
        TabPane tp = new TabPane();
        tab01 = new Tab(WordMap.get("Ethernet"));
        if(wifiFlag)
            Get_Wlan_Name();        
        if(wifiFlag)
            tab01_wifi = new Tab("WI-FI"); // 2018.04.13 william wifi實作
        tab02 = new Tab(WordMap.get("TC_Display"));
        tab03 = new Tab(WordMap.get("TC_System"));
        tab04_KB = new Tab(WordMap.get("KeyboardSetting_tab")); 
        if(wifiFlag)
            tp.getTabs().addAll(tab01,tab01_wifi,tab02,tab03, tab04_KB); // 2018.04.13 william wifi實作     
        else 
            tp.getTabs().addAll(tab01,tab02,tab03, tab04_KB); // 2018.04.13 william wifi實作     
        tab01.closableProperty().set(false);//關閉分頁'X'
        if(wifiFlag)
            tab01_wifi.closableProperty().set(false);  // 2018.04.13 william wifi實作     
        tab02.closableProperty().set(false);//關閉分頁'X'
        tab03.closableProperty().set(false);//關閉分頁'X'
        tab04_KB.closableProperty().set(false);
        tab01.setContent(IPv4Pane()); 
        //tab02.setContent(DisplayPane()); 
        //tab03.setContent(SystemPane());
      
        /** tab 選擇後 才會撈資料 並 顯示 **/
        tp.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
                @Override
                public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {                    
                    int tabSelect;
                    tabSelect = tp.getSelectionModel().getSelectedIndex() ;
                    System.out.println("*/*/ Tab Selection  */*/"+ tabSelect +"\n");
                    if(wifiFlag) {
                        if(tabSelect == 1) {  // 2018.04.13 william wifi實作                        
                            try {
                                if(tab01_wifi.getContent() == null) {
                                    tab01_wifi.setContent(WIFIPane());
                                }                            
                                System.out.println("*/*/ tab01_wifi Selection changed */*/ \n");                                                        
                            } catch (IOException ex) {
                                Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                            }                    
                        }

                        if(tabSelect == 2){ // 2018.04.13 william wifi實作 1 -> 2

                            try {
                                if(tab02.getContent()==null){
                                    tab02.setContent(DisplayPane());
                                }                            
                                System.out.println("*/*/ Tab02 Selection changed */*/ \n");


                            } catch (IOException | ParseException ex) {
                                Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        if(tabSelect == 3){ // 2018.04.13 william wifi實作 2 -> 3
                            if(tab03.getContent()==null){

                                try {
                                    tab03.setContent(SystemPane());
                                } catch (ParseException | IOException ex) {
                                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                            System.out.println("*/*/ Tab03 Selection changed */*/ \n");
                        }      
                        if(tabSelect == 4){ // 2018.04.13 william wifi實作 1 -> 2

                            try {
                                if(tab04_KB.getContent()==null){
                                    tab04_KB.setContent(KeyBoardPane());
                                }                            
                                System.out.println("*/*/ Tab04 Selection changed */*/ \n");


                            } catch (IOException | ParseException ex) {
                                Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }                        
                    } else {
                        if(tabSelect == 1){ 

                            try {
                                if(tab02.getContent()==null){
                                    tab02.setContent(DisplayPane());
                                }                            
                                System.out.println("*/*/ Tab02 Selection changed */*/ \n");


                            } catch (IOException | ParseException ex) {
                                Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        if(tabSelect == 2){ 
                            if(tab03.getContent()==null){

                                try {
                                    tab03.setContent(SystemPane());
                                } catch (ParseException | IOException ex) {
                                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                            System.out.println("*/*/ Tab03 Selection changed */*/ \n");
                        } 
                        if(tabSelect == 3){ 

                            try {
                                if(tab04_KB.getContent()==null){
                                    tab04_KB.setContent(KeyBoardPane());
                                }                            
                                System.out.println("*/*/ Tab04 Selection changed */*/ \n");


                            } catch (IOException | ParseException ex) {
                                Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }                        
                    
                    }                    
                }
            }
        );
        
        
        /***  功能(下)畫面使用Anchor的方式Layout ***/
        AnchorPane anchor_control = new AnchorPane();
        anchor_control.setPrefHeight(60);
        anchor_control.setMaxHeight(60);
        anchor_control.setMinHeight(60);
        //anchor_control.setStyle("-fx-background-color: black ;");
        
        /***  底層畫面使用Border的方式Layout ***/
        BorderPane backPane = new BorderPane();
        backPane.setStyle("-fx-background-color: rgb(0,180,240);");
        //backPane.setPrefSize(380, 450); 
        //backPane.setMaxSize(380, 450);
        //backPane.setMinSize(380, 450); 
        backPane.setTop(Header);
        backPane.setBottom(anchor_control);
        backPane.setCenter(tp);
        
        /*****離開*****/
        Leave=new Button(WordMap.get("TC_Exit"));
        Leave.setPrefSize(80,30);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        Leave.setMaxSize(80,30);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        Leave.setMinSize(80,30);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動           
        Leave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {     
                // 2017.07.11 william remove internet plug & DNS Exception fix
                //System.out.println("--Leave--:DHCP_IP:"+ DHCP_IP +"\n");
                //System.out.println("--Leave--:StaticIP_IP:"+ StaticIP_IP +"\n");
//                if(  DHCP_IP != null ||  StaticIP_IP != null ){
//                    
//                    if(DNS_or_StaticIP == true){
//                        
//                        System.out.println("--Leave-- DNS_or_StaticIP == true ------\n");
//                        //writeDHCPSetting(DHCP_IP, DHCP_prflen_ipv4, DHCP_Gateway);
//                        writeDHCPResolv(DHCP_DNS01, DHCP_DNS02);
//                        
//                    }else{
//                        
//                        System.out.println("--Leave-- DNS_or_StaticIP == false ------\n");
//                        //writeStaticIPSetting(StaticIP_IP_Setting, StaticIP_Final_mask_cidr, StaticIP_Gateway_Setting);
//                        writeStaticIPResolv(StaticIP_DNS01, StaticIP_DNS02);
//                        
//                    }
//                    
//                    if(tab02.getContent()!=null){
//                        HDMI01_ResolutionSelection.getSelectionModel().select(Display01_current_data);
//                        if( !Display02_name.equals("0")){
//                            HDMI02_ResolutionSelection.getSelectionModel().select(Display02_current_data);
//                        }
//                    }
//                }         

                GB.lock_ThinClient = false; // 2017.06.09 william Disable click many times will show mutil Dialog
                ThinClientStage.close();
            }
        }); 
        
       
        // Arandr modify 2018.05.31
        ThinClientStage.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if(ov.getValue() == true && arandr_closeFlag) { 
                try {  
                    Runtime.getRuntime().exec("killall python");
                } catch (IOException ex) {
                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                 System.out.println("arandr_TimerFlag = true");
                arandr_TimerFlag = true;
                // Advanced_DisplayPane.setDisable(false);
                //System.out.println("kill python");
            }
        });        
        
        /*****關閉視窗"X"-會與離開按鍵一樣 可以關閉全部視窗 *****/
        ThinClientStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {  
              // 2017.07.11 william remove internet plug & DNS Exception fix
//                if(DNS_or_StaticIP == true){
//
//                   //System.out.println("--Leave-- DNS_or_StaticIP == true ------\n"); 
//
//                    //writeDHCPSetting(DHCP_IP, DHCP_prflen_ipv4, DHCP_Gateway);
//                    writeDHCPResolv(DHCP_DNS01, DHCP_DNS02);
//
//                }else{
//
//                    //System.out.println("--Leave-- DNS_or_StaticIP == false ------\n");
//
//                    //writeStaticIPSetting(StaticIP_IP_Setting, StaticIP_Final_mask_cidr, StaticIP_Gateway_Setting);
//                    writeStaticIPResolv(StaticIP_DNS01, StaticIP_DNS02);
//
//                } 
                
//                if(tab02.getContent()!=null){  
//                    HDMI01_ResolutionSelection.getSelectionModel().select(Display01_current_data);
//                    if( !Display02_name.equals("0")){
//                        HDMI02_ResolutionSelection.getSelectionModel().select(Display02_current_data);
//                    }                
//                }
                GB.lock_ThinClient = false; // 2017.06.09 william Disable click many times will show mutil Dialog                
                ThinClientStage.close(); 

            }
        }); 
        
        /*****取消*****/
        Cancel=new Button(WordMap.get("TC_Cancel"));
        Cancel.setPrefSize(80,30);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        Cancel.setMaxSize(80,30);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        Cancel.setMinSize(80,30);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動           
        Cancel.setOnAction((event) -> {
            if( DHCP_IP != null ||  StaticIP_IP != null ){
                chancelAction();
            }
            
        });
        /*****確定*****/
        Confirm=new Button(WordMap.get("TC_Confirm"));
        Confirm.setPrefSize(80,30);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        Confirm.setMaxSize(80,30);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        Confirm.setMinSize(80,30);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動           
        Confirm.setOnAction((ActionEvent event) -> {            
            //group.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
            GB.lock_ThinClient = false; // 2017.06.09 william Disable click many times will show mutil Dialog
            if( group.getSelectedToggle()== rb2_StaticIP ){
                
                StaticIP_IP_Setting = IPv4_IP_TextField.getText();                                       
                StaticIP_submask_Setting = IPv4_Mask_TextField.getText();          
                StaticIP_Gateway_Setting = IPv4_Getway_TextField.getText();
                StaticIP_DNS01_Setting = IPv4_DNS1_TextField.getText();
                StaticIP_DNS02_Setting = IPv4_DNS2_TextField.getText();
                
                if( StaticIP_IP_Setting.equals(StaticIP_IP) ){
                    ModifyStaticIP_IP = true;                
                }
                if( !StaticIP_IP_Setting.equals(StaticIP_IP) ){
                    ModifyStaticIP_IP = false;                
                }
                if( StaticIP_submask_Setting.equals(StaticIP_submask) ){
                    ModifyStaticIP_submask = true;                
                }
                if( !StaticIP_submask_Setting.equals(StaticIP_submask) ){
                    ModifyStaticIP_submask = false;                
                }
                // 2017.07.11 william remove internet plug & DNS Exception fix
                if(StaticIP_Gateway_Setting != null) {
                    if( StaticIP_Gateway_Setting.equals(StaticIP_Gateway) ){
                        ModifyStaticIP_Gateway = true;                
                    }
                    if( !StaticIP_Gateway_Setting.equals(StaticIP_Gateway) ){
                        ModifyStaticIP_Gateway = false;                
                    }
                }
                else {
                    ModifyStaticIP_Gateway = false;
                }
                // 2017.07.11 william remove internet plug & DNS Exception fix
                if(StaticIP_DNS01_Setting != null) {
                    if( StaticIP_DNS01_Setting.equals(StaticIP_DNS01) ){
                        ModifyStaticIP_DNS01 = true;                
                    }
                    if( !StaticIP_DNS01_Setting.equals(StaticIP_DNS01) ){
                        ModifyStaticIP_DNS01 = false;                
                    }
                }
                else {
                    ModifyStaticIP_DNS01 = false;     
                }
                // 2017.07.11 william remove internet plug & DNS Exception fix
                if(StaticIP_DNS02_Setting != null) {
                    if( StaticIP_DNS02_Setting.equals(StaticIP_DNS02) ){
                        ModifyStaticIP_DNS02 = true;                
                    }
                    if( !StaticIP_DNS02_Setting.equals(StaticIP_DNS02) ){
                        ModifyStaticIP_DNS02 = false;               
                    }     
                }
                // 2017.07.11 william remove internet plug & DNS Exception fix
                if(StaticIP_DNS02_Setting == null) {
                    ModifyStaticIP_DNS02 = false;  
                }

            }

            if( DNS_or_StaticIP == true && group.getSelectedToggle()== rb1_DHCP){
                try {
                    //GetDHCPInetAddr();
                    System.out.println("--- Start --- DNS_or_StaticIP == true && group.getSelectedToggle()== rb1_DHCP----\n");
                    writeDHCPSetting(DHCP_IP, DHCP_prflen_ipv4, DHCP_Gateway);
                    writeDHCPResolv(DHCP_DNS01, DHCP_DNS02);
                    DisplayChangeAction();                    
                    ThinClientStage.close();
                } catch (IOException | ParseException ex) {
                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        
            if( DNS_or_StaticIP == false && group.getSelectedToggle()== rb1_DHCP){
                System.out.println("--- Start --- DNS_or_StaticIP == false && group.getSelectedToggle()== rb1_DHCP ------\n");              
                try {              
                    SIP_turn_Dhcp_alert();
                } catch (ParseException ex) {
                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if( DNS_or_StaticIP == false && group.getSelectedToggle()== rb2_StaticIP){
                System.out.println("--- Start --- DNS_or_StaticIP == false && group.getSelectedToggle()== rb2_StaticIP ------\n");                
//                if( ModifyStaticIP_IP == false || ModifyStaticIP_submask == false || ModifyStaticIP_Gateway == false || ModifyStaticIP_DNS01 == false || ModifyStaticIP_DNS02 == false ){       
//                
//                }
                if( ModifyStaticIP_IP == true && ModifyStaticIP_submask == true && ModifyStaticIP_Gateway == true && ModifyStaticIP_DNS01 == true && ModifyStaticIP_DNS02 == true ){
                    
                    try {
                        System.out.println("--- ModifyStaticIP == true------\n");                           
                        writeStaticIPResolv(StaticIP_DNS01_Setting, StaticIP_DNS02_Setting);
                        DisplayChangeAction();                        
                        writeStaticIPResolv(StaticIP_DNS01_Setting, StaticIP_DNS02_Setting);                        
                        ThinClientStage.close();
                    } catch (IOException | ParseException ex) {
                        Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                    }               
                
                } //else{
                if( ModifyStaticIP_IP == false || ModifyStaticIP_submask == false || ModifyStaticIP_Gateway == false || ModifyStaticIP_DNS01 == false || ModifyStaticIP_DNS02 == false ){     
                    System.out.println("--- ModifyStaticIP == false------\n");                
                    try {       
                        // 2017.07.11 william remove internet plug & DNS Exception fix
                        if( !IPv4_IP_TextField.getText().equals("") && !IPv4_Mask_TextField.getText().equals("")) {
                            SIP_changeText_alert();
                        }
                        else {
                            EnterCheck_alert();
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
            if( DNS_or_StaticIP == true && group.getSelectedToggle()== rb2_StaticIP){                
                System.out.println("--- Start --- DNS_or_StaticIP == true && group.getSelectedToggle()== rb2_StaticIP ------\n");
                try {
                    // 2017.07.11 william remove internet plug & DNS Exception fix
                    if( !IPv4_IP_TextField.getText().equals("") && !IPv4_Mask_TextField.getText().equals("")) {
                        Dhcp_turn_SIP_alert();
                    }
                    else {
                        EnterCheck_alert();
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //});                   
            
        });
        // 2017.06.20 william add thinclient Key Press rule
        Leave.setOnKeyPressed((event)-> {
           //event.get           
           if(event.getCode() == KeyCode.ENTER){
               Leave.fire(); //進入功能
               event.consume();
           }
           // 2018.01.15 william 修改TC設定的按鈕邏輯
           /*
           if(event.getCode() == KeyCode.UP){
               Confirm.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.LEFT){
               Confirm.requestFocus();
               event.consume();
           }           
           if(event.getCode() == KeyCode.DOWN){
               Cancel.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.RIGHT){
               Cancel.requestFocus();
               event.consume();
           }
           */
        });
        
        Cancel.setOnKeyPressed((event)-> {
           //event.get           
           if(event.getCode() == KeyCode.ENTER){
               Cancel.fire(); //進入功能
               event.consume();
           }
           if(event.getCode() == KeyCode.UP){
               Leave.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.LEFT){
               Leave.requestFocus();
               event.consume();
           }           
           if(event.getCode() == KeyCode.DOWN){
               Confirm.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.RIGHT){
               Confirm.requestFocus();
               event.consume();
           }
        });
        
        Confirm.setOnKeyPressed((event)-> {
           //event.get           
           if(event.getCode() == KeyCode.ENTER){
               Confirm.fire(); //進入功能
               event.consume();
           }
           // 2018.01.15 william 修改TC設定的按鈕邏輯
           /*           
           if(event.getCode() == KeyCode.UP){
               Cancel.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.LEFT){
               Cancel.requestFocus();
               event.consume();
           }           
           if(event.getCode() == KeyCode.DOWN){
               Leave.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.RIGHT){
               Leave.requestFocus();
               event.consume();
           }
           */
        });
        
        /* 2018.01.15 william 修改TC設定的按鈕邏輯
        HBox Operation=new HBox();
        //Operation.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        
        Operation.getChildren().addAll(Leave, Confirm);  // 2018.01.15 william 修改TC設定的按鈕邏輯 Leave, Cancel, Confirm -> Leave, Confirm
        
        Operation.setAlignment(Pos.CENTER_RIGHT);
        Operation.setSpacing(15); 
        
        anchor_control.getChildren().addAll(Operation); // 將功能Button 放置AnchorPane中       
        AnchorPane.setBottomAnchor(Operation,15.0);  //設置邊距
        AnchorPane.setRightAnchor(Operation,15.0);
        */
                                    
        Scene ThinClientScene = new Scene(backPane);
        ThinClientScene.getStylesheets().add("ThinClient.css");
        if(wifiFlag)
            ChangeLabelLang(TCTitle, tab01, tab02, tab03, tab01_wifi, tab04_KB); // 2018.04.13 william wifi實作
        else
            ChangeLabelLang(TCTitle, tab01, tab02, tab03, tab04_KB, tabNull); 
        ChangeButtonLang(Leave, Cancel, Confirm);
        
        Bounds mainBounds = primaryStage.getScene().getRoot().getLayoutBounds();
        Bounds rootBounds = ThinClientScene.getRoot().getLayoutBounds();
        ThinClientStage.setX(primaryStage.getX() + (mainBounds.getWidth() - 420 ) / 2);
        ThinClientStage.setY(primaryStage.getY() + (mainBounds.getHeight() - 500 ) / 2);
        
//        ThinClientStage.centerOnScreen();        
        
        switch (GB.JavaVersion) {
            case 0:
                ThinClientStage.getIcons().add(new Image("images/Icon2.png"));
                break;
            case 1:
                ThinClientStage.getIcons().add(new Image("images/MCIcon2.png"));
                break;                
            default:
                break;
        }  
        
        //ThinClientStage.setTitle(WordMap.get("ThinClient"));
        ThinClientStage.setScene(ThinClientScene);
        ThinClientStage.initStyle(StageStyle.DECORATED);
        ThinClientStage.initModality(Modality.WINDOW_MODAL); // 2017.06.20 william window modal lock NONE -> WINDOW_MODAL
        ThinClientStage.initOwner(primaryStage);
        ThinClientStage.setResizable(false);//將視窗放大,關閉
        //ThinClientStage.centerOnScreen(); 
        
        ThinClientStage.show();
        
        
        // 2019.02.12 Get device mac address
        String t = "ip link | grep -o eth[0-9]";
        Process ethernet_name_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",t});
        String ethernet_name = "";

        BufferedReader output_ethernet = new BufferedReader (new InputStreamReader(ethernet_name_process.getInputStream()));
        String line_ethernet = output_ethernet.readLine();
        if(line_ethernet != null) {
            

            String[] ethernet_Line_list;
            
            while(line_ethernet != null) {                  
                if(line_ethernet.contains("eth")) {
                    ethernet_Line_list = line_ethernet.split(" ");                        
                
                    String ethIndex = "eth";       
                    for (int i=0;i<ethernet_Line_list.length;i++) {
                        if (ethernet_Line_list[i].contains(ethIndex)) {
                            ethernet_name = ethernet_Line_list[i]; 
                            System.out.println(" ethernet_name: " + ethernet_name);   
                            break;
                        }
                    }                  
                }              
                
                line_ethernet = output_ethernet.readLine();
            }
            output_ethernet.close();           
        
            // InetAddress ip;
            try {			
                
                Process ethernet_mac_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","cat /sys/class/net/" + ethernet_name + "/address"});
                
                BufferedReader output_mac = new BufferedReader (new InputStreamReader(ethernet_mac_process.getInputStream()));
                String line_mac = output_mac.readLine();
                if(line_mac != null) {
            
                    
                            MAC_TextField.setText(line_mac.toUpperCase());
                            System.out.println(" line_mac: " + line_mac);   
                 
                                           
                }
            
            output_mac.close();                 
                
                
               // ip = InetAddress.getLocalHost();	
               // NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                
//                NetworkInterface ni = NetworkInterface.getByName(ethernet_name);
//
//                byte[] mac = ni.getHardwareAddress();
//
//                String mac_Str = "";
//
//                for (int i = 0; i < mac.length; i++) {                   
//                    mac_Str += (String.format("%02X", mac[i]));
//                    mac_Str += (i < mac.length - 1) ? ":" : "";
//                    System.out.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : "");
//                }            

//                System.out.println();
//                System.out.println(" MAC Address: " + mac_Str);   
//                MAC_TextField.setText(mac_Str);
//                System.out.println();  

            } catch (Exception e) {		
                e.printStackTrace();		
            }          
            
            
        }
        
       
        
        
        backPane.requestFocus(); // 2017.06.20 william avoid to button Leave being focus
//        System.out.println(" rootBounds.getWidth() :: "+ThinClientStage.getWidth()+"\n");
//        System.out.println(" rootBounds.getHeight() :: "+ThinClientStage.getHeight()+"\n");
        
    }
    
    // Arandr modify 2018.05.31
    public boolean ArandrOpenCheck() throws IOException{
        String command = "ps aux | grep \"[p]ython\" ";  
        Process Check_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",command});
        BufferedReader output_Check = new BufferedReader (new InputStreamReader(Check_process.getInputStream()));
        String line_Check = output_Check.readLine();    
        if(line_Check != null)
            return true;
        else
            return false;
    }    
    
    public void ArandrTimer() {
        Timer display_setting_timer = new Timer(); 
        TimerTask display_setting_task = new TimerTask() {    
            @Override
            public void run() {
                DisplayDisable();
                System.out.println("Timer Start");
                
                try {
                    if(ArandrOpenCheck()) {
                        arandr_closeFlag = true;                        
                    }                       
                    //System.out.println("ArandrOpenCheck: " + ArandrOpenCheck());
                } catch (IOException ex) {
                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if(arandr_TimerFlag) {
                    display_setting_timer.cancel();
                    arandr_TimerFlag = false;
                }
                
            }        
        };    
        display_setting_timer.schedule(display_setting_task, 1, 1000);      
    }
    
    public void DisplayDisable() {
        File check  = new File("/root/checkarandr.txt");
        if(check.exists()) {            
            try {
                Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","/root/restartui.sh"});
//                            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","killall python /root/arandr/arandr"});
            } catch (IOException ex) {
                Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.exit(1); 
        }         
    }      
    
    /******************** Tab01 : IPv4 *********************/
    private Pane IPv4Pane() throws SocketException, UnknownHostException, IOException{
         
        //Group root = new Group();
        //Scene scene = new Scene(root, 360, 300, Color.WHITE);
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(8);
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setPadding(new Insets(17, 10, 15, 12));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        //grid.setGridLinesVisible(true); //開啟或關閉表格的分隔線
        grid.setId("gridpane");
        
        /************RadioButton : DHCP & 靜態IP************/
        group = new ToggleGroup();
        rb1_DHCP = new RadioButton("DHCP");
        //rb1.setUserData("RadioButton1");
        rb1_DHCP.setToggleGroup(group);
        //rb1.setSelected(true);
        //rb1.requestFocus();

        group.selectToggle(rb1_DHCP);

        rb2_StaticIP = new RadioButton(WordMap.get("TC_Static_IP"));      
        rb2_StaticIP.setToggleGroup(group);

        HBox tab01HBox=new HBox();
        //tab01HBox.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        tab01HBox.getChildren().addAll(rb1_DHCP,rb2_StaticIP);  
        tab01HBox.setAlignment(Pos.CENTER_LEFT);
        tab01HBox.setSpacing(15); 
        grid.add(tab01HBox, 0, 0, 5, 1);
        
        // 2019.02.12 Get device mac address
        Label MAC_addr = new Label("MAC");
        MAC_addr.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 600; -fx-font-size: 12pt;");
        GridPane.setHalignment(MAC_addr, HPos.LEFT);      
	grid.add(MAC_addr, 0, 1);
        MAC_addr.setPadding(new Insets(0, 0, 0, 8));
        MAC_addr.setTranslateX(-7);
        
        
        Label Colon0 = new Label(":");
        Colon0.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 14px; -fx-font-weight: 700;");
        GridPane.setHalignment(Colon0, HPos.LEFT);
        grid.add(Colon0, 2, 1); 
        Colon0.setPadding(new Insets(0, 0, 0, 0));        
        
        MAC_TextField = new TextField();      
	grid.add(MAC_TextField, 3, 1);
        MAC_TextField.setPrefWidth(270);
        MAC_TextField.setMaxWidth(270);
        MAC_TextField.setMinWidth(270);   
        MAC_TextField.setDisable(true);
                
                
        
        /**********RadioButton : 靜態IP設定**********/
        Label IPv4_IP = new Label(WordMap.get("TC_Static_IP_Setting") + " :");
        GridPane.setHalignment(IPv4_IP, HPos.LEFT);      
        //IPv4_IP.setMaxWidth(120);//強制限制Label最大寬度,讓每個Label在變換語言時不會變動
        //IPv4_IP.setMinWidth(120);//強制限制Label最小寬度,讓每個Label在變換語言時不會變動
        //IPv4_IP.setPrefWidth(120);//強制限制Label預設寬度,讓每個Label在變換語言時不會變動
	grid.add(IPv4_IP, 0, 2,4,1);
        //IPv4_IP.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        //IPv4_IP.setId("title_Label");
        
        /*******IP位置*******/
        Label IPv4_IP_addr = new Label(WordMap.get("TC_IP_addr"));
        GridPane.setHalignment(IPv4_IP_addr, HPos.LEFT);      
	grid.add(IPv4_IP_addr, 0, 3);
        IPv4_IP_addr.setPadding(new Insets(0, 0, 0, 8));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)  
        
        Label Colon1=new Label(":");//將":"固定在一個位置
        GridPane.setHalignment(Colon1, HPos.LEFT);
        grid.add(Colon1, 2, 3); 
        Colon1.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)             
        
        IPv4_IP_TextField = new TextField();      
	grid.add(IPv4_IP_TextField, 3, 3);
        //IPv4_IP_TextField.setPrefSize(300, 30);
        //IPv4_IP_TextField.setMaxSize(300, 30);
        //IPv4_IP_TextField.setMinSize(300, 30);
        IPv4_IP_TextField.setPrefWidth(270);
        IPv4_IP_TextField.setMaxWidth(270);
        IPv4_IP_TextField.setMinWidth(270);
        
        /********遮罩********/
        Label IPv4_Mask = new Label(WordMap.get("TC_Mask"));
        GridPane.setHalignment(IPv4_Mask, HPos.LEFT);      
	grid.add(IPv4_Mask, 0, 4);
        IPv4_Mask.setPadding(new Insets(0, 0, 0, 8));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)       
        
        Label Colon2=new Label(":");//將":"固定在一個位置
        GridPane.setHalignment(Colon2, HPos.LEFT);
        grid.add(Colon2, 2, 4); 
        Colon2.setPadding(new Insets(0, 5, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)             
        
        IPv4_Mask_TextField = new TextField();      
	grid.add(IPv4_Mask_TextField, 3, 4);
        IPv4_Mask_TextField.setPrefWidth(270);
        IPv4_Mask_TextField.setMaxWidth(270);
        IPv4_Mask_TextField.setMinWidth(270);
        
        /********預設閘道*******/
        Label IPv4_Def_Getway = new Label(WordMap.get("TC_Def_Getway"));
        GridPane.setHalignment(IPv4_Def_Getway, HPos.LEFT);      
	grid.add(IPv4_Def_Getway, 0, 5);  // 0,4,2,1
        IPv4_Def_Getway.setPadding(new Insets(0, 0, 0, 8));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        
        Label Colon3=new Label(":");//將":"固定在一個位置
        GridPane.setHalignment(Colon3, HPos.LEFT);
        grid.add(Colon3, 2, 5); 
        Colon3.setPadding(new Insets(0, 5, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)             
        
        IPv4_Getway_TextField = new TextField();      
	grid.add(IPv4_Getway_TextField, 3, 5);
        IPv4_Getway_TextField.setPrefWidth(270);
        IPv4_Getway_TextField.setMaxWidth(270);
        IPv4_Getway_TextField.setMinWidth(270);
        
         /**********靜態DNS設定**********/
        Label IPv4_DNS = new Label(WordMap.get("TC_Static_DNS_Setting") + " :");
        GridPane.setHalignment(IPv4_DNS, HPos.LEFT);      
	grid.add(IPv4_DNS, 0, 6,4,1);
        //IPv4_DNS.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        //IPv4_DNS.setId("title_Label");
        
        /********DNS 1 ********/
        Label IPv4_DNS1 = new Label("DNS 1");
//        GridPane.setHalignment(IPv4_DNS1, HPos.LEFT);      
//	grid.add(IPv4_DNS1, 0, 6);
//        IPv4_DNS1.setPadding(new Insets(0, 0, 0, 8));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)       
        IPv4_DNS1.setId("label_DNS");
        
        Label Colon4=new Label(":");//將":"固定在一個位置
        Colon4.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 14px; -fx-font-weight: 900;");         
        
        HBox DNS01_box=new HBox(IPv4_DNS1, Colon4);
        DNS01_box.setAlignment(Pos.CENTER_LEFT);        
        DNS01_box.setPadding(new Insets(0, 0, 0, 8));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        DNS01_box.setSpacing(8);
        grid.add(DNS01_box,0,7);
        
        IPv4_DNS1_TextField = new TextField();      
	grid.add(IPv4_DNS1_TextField, 1, 7,3,1);
        //IPv4_DNS1_TextField.setPrefSize(328, 39);
        //IPv4_DNS1_TextField.setMaxSize(328, 39);
        //IPv4_DNS1_TextField.setMinSize(328, 39);
        
         /********DNS 2 ********/
        Label IPv4_DNS2 = new Label("DNS 2");
//        GridPane.setHalignment(IPv4_DNS2, HPos.LEFT);      
//	grid.add(IPv4_DNS2, 0, 7);
//        IPv4_DNS2.setPadding(new Insets(0, 0, 0, 8));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)       
        IPv4_DNS2.setId("label_DNS");
        
        Label Colon5=new Label(":");//將":"固定在一個位置
        Colon5.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 14px; -fx-font-weight: 900;");         
        
        HBox DNS02_box=new HBox(IPv4_DNS2, Colon5);
        DNS02_box.setAlignment(Pos.CENTER_LEFT);        
        DNS02_box.setPadding(new Insets(0, 0, 0, 8));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        DNS02_box.setSpacing(8);
        grid.add(DNS02_box,0,8);

        IPv4_DNS2_TextField = new TextField();      
	grid.add(IPv4_DNS2_TextField, 1, 8,3,1);
        //IPv4_DNS2_TextField.setPrefSize(328, 39);
        //IPv4_DNS2_TextField.setMaxSize(328, 39);
        //IPv4_DNS2_TextField.setMinSize(328, 39);
        
        // 2018.01.15 william 修改TC設定的按鈕邏輯       
        /**********確定************/                 
        Confirm_IPv4Pane = new Button(WordMap.get("TC_Confirm"));
        Confirm_IPv4Pane.setPrefSize(80,30);
        Confirm_IPv4Pane.setMaxSize(80,30);
        Confirm_IPv4Pane.setMinSize(80,30);
        Confirm_IPv4Pane.setOnAction((ActionEvent event) -> {                        
            GB.lock_ThinClient = false;
            if( group.getSelectedToggle()== rb2_StaticIP ){
                
                StaticIP_IP_Setting = IPv4_IP_TextField.getText();                                       
                StaticIP_submask_Setting = IPv4_Mask_TextField.getText();          
                StaticIP_Gateway_Setting = IPv4_Getway_TextField.getText();
                StaticIP_DNS01_Setting = IPv4_DNS1_TextField.getText();
                StaticIP_DNS02_Setting = IPv4_DNS2_TextField.getText();
                
                if( StaticIP_IP_Setting.equals(StaticIP_IP) ){
                    ModifyStaticIP_IP = true;                
                }
                if( !StaticIP_IP_Setting.equals(StaticIP_IP) ){
                    ModifyStaticIP_IP = false;                
                }
                if( StaticIP_submask_Setting.equals(StaticIP_submask) ){
                    ModifyStaticIP_submask = true;                
                }
                if( !StaticIP_submask_Setting.equals(StaticIP_submask) ){
                    ModifyStaticIP_submask = false;                
                }

                if(StaticIP_Gateway_Setting != null) {
                    if( StaticIP_Gateway_Setting.equals(StaticIP_Gateway) ){
                        ModifyStaticIP_Gateway = true;                
                    }
                    if( !StaticIP_Gateway_Setting.equals(StaticIP_Gateway) ){
                        ModifyStaticIP_Gateway = false;                
                    }
                }
                else {
                    ModifyStaticIP_Gateway = false;
                }

                if(StaticIP_DNS01_Setting != null) {
                    if( StaticIP_DNS01_Setting.equals(StaticIP_DNS01) ){
                        ModifyStaticIP_DNS01 = true;                
                    }
                    if( !StaticIP_DNS01_Setting.equals(StaticIP_DNS01) ){
                        ModifyStaticIP_DNS01 = false;                
                    }
                }
                else {
                    ModifyStaticIP_DNS01 = false;     
                }

                if(StaticIP_DNS02_Setting != null) {
                    if( StaticIP_DNS02_Setting.equals(StaticIP_DNS02) ){
                        ModifyStaticIP_DNS02 = true;                
                    }
                    if( !StaticIP_DNS02_Setting.equals(StaticIP_DNS02) ){
                        ModifyStaticIP_DNS02 = false;               
                    }     
                }

                if(StaticIP_DNS02_Setting == null) {
                    ModifyStaticIP_DNS02 = false;  
                }

            }

            if( DNS_or_StaticIP == true && group.getSelectedToggle()== rb1_DHCP){
                try {
                    System.out.println("--- Start --- DNS_or_StaticIP == true && group.getSelectedToggle()== rb1_DHCP----\n");
                    writeDHCPSetting(DHCP_IP, DHCP_prflen_ipv4, DHCP_Gateway);
                    writeDHCPResolv(DHCP_DNS01, DHCP_DNS02);
                    DisplayChangeAction();                    
                    ThinClientStage.close();
                } catch (IOException | ParseException ex) {
                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        
            if( DNS_or_StaticIP == false && group.getSelectedToggle()== rb1_DHCP){
                System.out.println("--- Start --- DNS_or_StaticIP == false && group.getSelectedToggle()== rb1_DHCP ------\n");              
                try {              
                    SIP_turn_Dhcp_alert();
                } catch (ParseException ex) {
                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if( DNS_or_StaticIP == false && group.getSelectedToggle()== rb2_StaticIP){
                System.out.println("--- Start --- DNS_or_StaticIP == false && group.getSelectedToggle()== rb2_StaticIP ------\n");                
                if( ModifyStaticIP_IP == true && ModifyStaticIP_submask == true && ModifyStaticIP_Gateway == true && ModifyStaticIP_DNS01 == true && ModifyStaticIP_DNS02 == true ){
                    
                    try {
                        System.out.println("--- ModifyStaticIP == true------\n");                           
                        writeStaticIPResolv(StaticIP_DNS01_Setting, StaticIP_DNS02_Setting);
                        DisplayChangeAction();                        
                        writeStaticIPResolv(StaticIP_DNS01_Setting, StaticIP_DNS02_Setting);                        
                        ThinClientStage.close();
                    } catch (IOException | ParseException ex) {
                        Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                    }               
                
                }
                if( ModifyStaticIP_IP == false || ModifyStaticIP_submask == false || ModifyStaticIP_Gateway == false || ModifyStaticIP_DNS01 == false || ModifyStaticIP_DNS02 == false ){     
                    System.out.println("--- ModifyStaticIP == false------\n");                
                    try {       
                        if( !IPv4_IP_TextField.getText().equals("") && !IPv4_Mask_TextField.getText().equals("")) {
                            SIP_changeText_alert();
                        }
                        else {
                            EnterCheck_alert();
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
            if( DNS_or_StaticIP == true && group.getSelectedToggle()== rb2_StaticIP){                
                System.out.println("--- Start --- DNS_or_StaticIP == true && group.getSelectedToggle()== rb2_StaticIP ------\n");
                try {
                    if( !IPv4_IP_TextField.getText().equals("") && !IPv4_Mask_TextField.getText().equals("")) {
                        Dhcp_turn_SIP_alert();
                    }
                    else {
                        EnterCheck_alert();
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }                           
        });                                                
        /**********離開************/ 
        Leave_IPv4Pane = new Button(WordMap.get("TC_Exit"));
        Leave_IPv4Pane.setPrefSize(80,30);
        Leave_IPv4Pane.setMaxSize(80,30);
        Leave_IPv4Pane.setMinSize(80,30);
        Leave_IPv4Pane.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {        
                GB.lock_ThinClient = false; 
                ThinClientStage.close();
            }
        });         
        // 2018.01.15 william 修改TC設定的按鈕邏輯
        HBox IPv4Button = new HBox();
        IPv4Button.getChildren().addAll(Leave_IPv4Pane, Confirm_IPv4Pane);  
        IPv4Button.setAlignment(Pos.CENTER_RIGHT);
        IPv4Button.setSpacing(15);
        IPv4Button.setPadding(new Insets(0, 0, 0, 20));
        grid.add(IPv4Button,3,18,1,1); // 2018.04.13 william wifi實作 3,8,1,1 -> 3,18,1,1
//        IPv4Button.setTranslateX(-33); 
        IPv4Button.setTranslateY(-31);  // 2018.04.13 william wifi實作        
        
        
        
        
        
        /******************判斷 是 DHCP 或 Static IP ********************/                
        Process Dhcp_or_staticIP_Process = Runtime.getRuntime().exec("cat /etc/resolv.conf"); //route | grep 'default' | awk '{print $2}' //netstat -rn               
        try (BufferedReader output_DNS_or_SIP = new BufferedReader (new InputStreamReader(Dhcp_or_staticIP_Process.getInputStream()))) {
            String line_DNS_or_SIP = output_DNS_or_SIP.readLine();                    
            
            while(line_DNS_or_SIP != null){
                
                if (line_DNS_or_SIP.matches("nameserver 127.0.1.1") == true || line_DNS_or_SIP.startsWith("domain") == true){
                    
                    DNS_or_StaticIP = true;
                    //System.out.println(" DNS_or_StaticIP  : " + DNS_or_StaticIP +"\n");
                    System.out.println("***** is DHCP *****\n");
                    GetDHCPInetAddr(); //取得 IP等等的資料
                    //writeDHCPSetting(DHCP_IP, DHCP_prflen_ipv4, DHCP_Gateway);
                    //writeDHCPResolv(DHCP_DNS01, DHCP_DNS02);
                    //System.out.println(" ---Run nameserver 127.0.1.1-- == true---\n");
                    
                    break;
                    
                }
                if( line_DNS_or_SIP.startsWith("staticIP") == true ){
                    
                    DNS_or_StaticIP = false;
                    System.out.println("***** is StaticIP *****\n");
                    break;
                }
                
                line_DNS_or_SIP = output_DNS_or_SIP.readLine();
                //System.out.println("  " + line_DNS_or_SIP +"\n");
                //break;
            }
        }
        
        //若判斷為 true ＝ DHCP ; false = Static IP ;
        if(DNS_or_StaticIP_flag == true){
            
            GetDHCPInetAddr();
            //writeDHCPSetting(DHCP_IP, DHCP_prflen_ipv4, DHCP_Gateway);
            //writeDHCPResolv(DHCP_DNS01, DHCP_DNS02); 
            //System.out.println("------- DNS_or_StaticIP == true ------\n"); 
            rb1_DHCP.setSelected(true);
            rb1_DHCP.requestFocus();
            
            IPv4_IP_TextField.setText(DHCP_IP);
            IPv4_Mask_TextField.setText(DHCP_submask);
            IPv4_Getway_TextField.setText(DHCP_Gateway);
            IPv4_DNS1_TextField.setText(DHCP_DNS01);
            IPv4_DNS2_TextField.setText(DHCP_DNS02);
            
            IPv4_IP_TextField.setEditable(false);
            IPv4_Mask_TextField.setEditable(false);
            IPv4_Getway_TextField.setEditable(false);
            IPv4_DNS1_TextField.setEditable(false);
            IPv4_DNS2_TextField.setEditable(false);
        
            
        }else{
            
            GetStaticIPInetAddr();
            //System.out.println("------- DNS_or_StaticIP == false ------\n");
            rb2_StaticIP.setSelected(true);
            rb2_StaticIP.requestFocus();
            
            IPv4_IP_TextField.setText(StaticIP_IP);
            IPv4_Mask_TextField.setText(StaticIP_submask);
            IPv4_Getway_TextField.setText(StaticIP_Gateway);
            IPv4_DNS1_TextField.setText(StaticIP_DNS01);
            IPv4_DNS2_TextField.setText(StaticIP_DNS02);
            
            IPv4_IP_TextField.setEditable(true);
            IPv4_Mask_TextField.setEditable(true);
            IPv4_Getway_TextField.setEditable(true);
//            IPv4_DNS1_TextField.setEditable(true);
            IPv4_DNS2_TextField.setEditable(true);
            
//            // 2017.07.11 william remove internet plug & DNS Exception fix
            if(Disconnected_flag) {
                try {
                    LoadStaticIPStatus();
                } catch (ParseException ex) {
                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            
        }
       
        group.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
            if (group.getSelectedToggle() == rb1_DHCP) { 
//                try { // 2017.07.11 william remove internet plug & DNS Exception fix
//                    GetDHCPInetAddr(); 
//                } catch (UnknownHostException ex) {
//                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IOException ex) {
//                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                try {                    
                    //GetDHCPInetAddr();                    
                    //writeDHCPResolv(DHCP_DNS01, DHCP_DNS02); 
                    //System.out.println(" ---Run group-- rb1_DHCP---\n");
//                    IPv4_IP_TextField.setText(DHCP_IP);
//                    IPv4_Mask_TextField.setText(DHCP_submask);
//                    IPv4_Getway_TextField.setText(DHCP_Gateway);
//                    IPv4_DNS1_TextField.setText(DHCP_DNS01);
//                    IPv4_DNS2_TextField.setText(DHCP_DNS02);
                    IPv4_IP_TextField.setEditable(false);
                    IPv4_Mask_TextField.setEditable(false);
                    IPv4_Getway_TextField.setEditable(false);
                    IPv4_DNS1_TextField.setEditable(false);
                    IPv4_DNS2_TextField.setEditable(false);
                    
                     // 2017.07.11 william remove internet plug & DNS Exception fix
//                    IPv4_IP_TextField.setText(DHCP_IP);
//                    IPv4_Mask_TextField.setText(DHCP_submask);
//                    IPv4_Getway_TextField.setText(DHCP_Gateway);
//                    IPv4_DNS1_TextField.setText(DHCP_DNS01);
//                    IPv4_DNS2_TextField.setText(DHCP_DNS02);                    
                                        
                    if(Disconnected_flag) {
                        IPv4_IP_TextField.setText("");
                        IPv4_Mask_TextField.setText("");
                        IPv4_Getway_TextField.setText("");
                        IPv4_DNS1_TextField.setText("");
                        IPv4_DNS2_TextField.setText(""); 
                    }
//                } 
//                catch (UnknownHostException ex) {
//                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IOException ex) {
//                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
//                }                  
                
            }else{               
//                try {                    
                    //GetStaticIPInetAddr();
                    IPv4_IP_TextField.setEditable(true);
                    IPv4_Mask_TextField.setEditable(true);
                    IPv4_Getway_TextField.setEditable(true);
                    IPv4_DNS1_TextField.setEditable(true);
                    IPv4_DNS2_TextField.setEditable(true); 
                    
                    if(Disconnected_flag) {
                        try {
                            LoadStaticIPStatus();
                        } catch (ParseException ex) {
                            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                        }                        
                    }                    

//                    // 2017.07.11 william remove internet plug & DNS Exception fix
//                    if(Disconnected_flag) {
//                        try {
//                            LoadLastIPStatus();
//                        } catch (ParseException ex) {
//                            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
                    
//                } catch (UnknownHostException ex) {
//                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IOException ex) {
//                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
//                }             
                //IPv4_IP_TextField.setText(group.getSelectedToggle().getUserData().toString());
            }
        });
        
        ChangeIPv4LabelLang(IPv4_IP, IPv4_DNS, IPv4_IP_addr, IPv4_Mask, IPv4_Def_Getway, Colon1, Colon2, Colon3);
        ChangeIPv4ButtonLang(rb1_DHCP, rb2_StaticIP, Confirm_IPv4Pane, Leave_IPv4Pane);
        return grid;
    }
    
    /******************** Tab01_wifi : WIFI *********************/ // 2018.04.13 william wifi實作
    private Pane WIFIPane() throws SocketException, UnknownHostException, IOException {
        
        grid.setHgap(5);
        grid.setVgap(8);
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setPadding(new Insets(17, 10, 15, 12));
        // grid.setGridLinesVisible(true);
        grid.setId("gridpane");
        // WIFI Name
        
        switch (WordMap.get("SelectedLanguage")) {
            case "English":
                wifi_name_title = new Label("Name");
                break;
            case "SimpleChinese":
                wifi_name_title = new Label("名称");
                break;                
            case "TraditionalChinese":
                wifi_name_title = new Label("名稱");
                break;                             
            case "Japanese":
                wifi_name_title = new Label("お名前");
                break;                                  
            default:
                wifi_name_title = new Label("Name");
                break;
        }         
                                
        GridPane.setHalignment(wifi_name_title, HPos.LEFT);  
        wifi_name_title.setPadding(new Insets(0, 0, 0, 8));        

        Label wifi_Colon1 = new Label(":");
        GridPane.setHalignment(wifi_Colon1, HPos.LEFT);         
        wifi_Colon1.setPadding(new Insets(0, 0, 0, 0));        
        wifi_Colon1.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 14px; -fx-font-weight: 900;");         

        wifi_Name_TextField = new TextField();
        wifi_Name_TextField.setPrefWidth(189);        
        wifi_Name_TextField.setMaxWidth(189);
        wifi_Name_TextField.setMinWidth(189);
        GridPane.setHalignment(wifi_Name_TextField, HPos.LEFT);      

        wifi_list = new Button(WordMap.get("Wifi_List"));
        wifi_list.setPrefSize(80,30);
        wifi_list.setMaxSize(80,30);
        wifi_list.setMinSize(80,30);  
        wifi_list.setTranslateY(-1);
        wifi_list.setOnAction((ActionEvent event) -> {  
//            try {

               // Search_Wifi();
                GB.lock_wifilist = true;
                wifi = new GetWifiInfo(MainStage,null, WordMap, false);
                wifi_Name_TextField.setText(wifi.getData());
                GB.lock_wifilist = false;
//                temp_wifi_name = ""; // java.lang.NullPointerException 未處理
                if(!temp_wifi_name.contains(wifi.getData())) {
                    System.out.print(temp_wifi_name + " , " + wifi.getData());
                    wifi_Password_TextField.setText("");
                    wifi_IPv4_IP_TextField.setText("");
                    wifi_IPv4_Mask_TextField.setText("");
                    wifi_IPv4_Getway_TextField.setText("");
                    wifi_IPv4_DNS1_TextField.setText("");
                    wifi_IPv4_DNS2_TextField.setText("");                    
                }
                
//            } catch (IOException ex) {
//                Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
//            }
        });        
        GridPane.setHalignment(wifi_list, HPos.RIGHT);         
        // layout
        grid.add(wifi_name_title, 0, 0);
        grid.add(wifi_Colon1, 2, 0);
        grid.add(wifi_Name_TextField, 3, 0);       
        grid.add(wifi_list, 3, 0);
        
        // WIFI Password   

        switch (WordMap.get("SelectedLanguage")) {
            case "English":
                wifi_pw_title = new Label("Password");
                break;
            case "SimpleChinese":
                wifi_pw_title = new Label("密码");
                break;                
            case "TraditionalChinese":
                wifi_pw_title = new Label("密碼");
                break;      
            case "Japanese":
                wifi_pw_title = new Label("パスワード");
                break;                  
            default:
                wifi_pw_title = new Label("密碼");
                break;
        }           
                       
        GridPane.setHalignment(wifi_pw_title, HPos.LEFT);  
        wifi_pw_title.setPadding(new Insets(0, 0, 0, 8));        

        Label wifi_Colon2 = new Label(":");      
        GridPane.setHalignment(wifi_Colon2, HPos.LEFT);         
        wifi_Colon2.setPadding(new Insets(0, 0, 0, 0));        
        wifi_Colon2.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 14px; -fx-font-weight: 900;");          
        
        wifi_Password_TextField = new PasswordField();    
        wifi_Password_TextField.setPrefWidth(226);
        wifi_Password_TextField.setMaxWidth(226);
        wifi_Password_TextField.setMinWidth(226);
        GridPane.setHalignment(wifi_Password_TextField, HPos.LEFT); 
        
        textField01 = new TextField();      
        // Set initial state
        textField01.setManaged(false);
        textField01.setVisible(false);        
        textField01.setPrefWidth(226);
        textField01.setMaxWidth(226);
        textField01.setMinWidth(226);      
        GridPane.setHalignment(textField01, HPos.LEFT); 
        
        buttonPw.setGraphic(new ImageView(EyeClose));  
        buttonPw.setId("wifi_pw_view");
        GridPane.setHalignment(buttonPw, HPos.RIGHT); 
        grid.add(buttonPw, 3, 1);
        
        buttonPw.setTranslateY(-1);
        buttonPw.setPrefHeight(28);
        buttonPw.setMaxHeight(28);
        buttonPw.setMinHeight(28);
     
        buttonPw.setOnAction(new EventHandler<ActionEvent>() {
            @Override        
            public void handle(ActionEvent e) {            
                if(changeAfterSaved==true) { 
                    if (e.getEventType().equals(ActionEvent.ACTION)) {                    
                        textField01.managedProperty().bind(buttonPw.selectedProperty());
                        textField01.visibleProperty().bind(buttonPw.selectedProperty());            
                        buttonPw.setGraphic(new ImageView(EyeOpen));

                        changeAfterSaved=false;
                    } 

                    changeAfterSaved=false;
                } else {
                    if (e.getEventType().equals(ActionEvent.ACTION)) {                   
                        wifi_Password_TextField.managedProperty().bind(buttonPw.selectedProperty().not());
                        wifi_Password_TextField.visibleProperty().bind(buttonPw.selectedProperty().not());             
                        buttonPw.setGraphic(new ImageView(EyeClose));

                        changeAfterSaved=true;
                    } 

                    changeAfterSaved=true;
                }
            
                textField01.textProperty().bindBidirectional(wifi_Password_TextField.textProperty());                                
            }
        });        
        
        // layout
        grid.add(wifi_pw_title, 0, 1);
        grid.add(wifi_Colon2, 2, 1);
        grid.add(wifi_Password_TextField, 3, 1);        
        grid.add(textField01, 3, 1);
                
        /************ RadioButton : DHCP & 靜態IP ************/
        wifi_group        = new ToggleGroup();
        wifi_rb1_DHCP     = new RadioButton("DHCP");
        wifi_rb2_StaticIP = new RadioButton(WordMap.get("TC_Static_IP"));  
        
        wifi_rb1_DHCP.setToggleGroup(wifi_group);
        wifi_rb2_StaticIP.setToggleGroup(wifi_group);
        wifi_group.selectToggle(wifi_rb1_DHCP);
        // layout          
        HBox tab01wifi_HBox = new HBox();
        tab01wifi_HBox.getChildren().addAll(wifi_rb1_DHCP, wifi_rb2_StaticIP);  
        tab01wifi_HBox.setAlignment(Pos.CENTER_LEFT);
        tab01wifi_HBox.setSpacing(15); 
        grid.add(tab01wifi_HBox, 0, 2, 5, 1);
        
        /********** 靜態IP設定 **********/
        Label IPv4_IP = new Label(WordMap.get("TC_Static_IP_Setting"));
        GridPane.setHalignment(IPv4_IP, HPos.LEFT);     
        /******** IP位置 ********/        
        Label IPv4_IP_addr = new Label(WordMap.get("TC_IP_addr"));
        GridPane.setHalignment(IPv4_IP_addr, HPos.LEFT);      
        IPv4_IP_addr.setPadding(new Insets(0, 0, 0, 8));

        Label Colon1 = new Label(":");
        GridPane.setHalignment(Colon1, HPos.LEFT);         
        Colon1.setPadding(new Insets(0, 0, 0, 0));

        wifi_IPv4_IP_TextField = new TextField();      	
        wifi_IPv4_IP_TextField.setPrefWidth(270);
        wifi_IPv4_IP_TextField.setMaxWidth(270);
        wifi_IPv4_IP_TextField.setMinWidth(270);
        // layout
        grid.add(IPv4_IP, 0, 3, 4, 1);
        grid.add(IPv4_IP_addr, 0, 4);
        grid.add(Colon1, 2, 4);
        grid.add(wifi_IPv4_IP_TextField, 3, 4);
        
        /******** 遮罩 ********/
        Label IPv4_Mask = new Label(WordMap.get("TC_Mask"));
        GridPane.setHalignment(IPv4_Mask, HPos.LEFT);      	
        IPv4_Mask.setPadding(new Insets(0, 0, 0, 8));

        Label Colon2 = new Label(":");
        GridPane.setHalignment(Colon2, HPos.LEFT);         
        Colon2.setPadding(new Insets(0, 5, 0, 0));

        wifi_IPv4_Mask_TextField = new TextField();      	
        wifi_IPv4_Mask_TextField.setPrefWidth(270);
        wifi_IPv4_Mask_TextField.setMaxWidth(270);
        wifi_IPv4_Mask_TextField.setMinWidth(270);
        // layout
        grid.add(IPv4_Mask, 0, 5);
        grid.add(Colon2, 2, 5);
        grid.add(wifi_IPv4_Mask_TextField, 3, 5);
        
        /******** 預設閘道 *******/
        Label IPv4_Def_Getway = new Label(WordMap.get("TC_Def_Getway"));
        GridPane.setHalignment(IPv4_Def_Getway, HPos.LEFT);      	
        IPv4_Def_Getway.setPadding(new Insets(0, 0, 0, 8));

        Label Colon3 = new Label(":");
        GridPane.setHalignment(Colon3, HPos.LEFT);        
        Colon3.setPadding(new Insets(0, 5, 0, 0));

        wifi_IPv4_Getway_TextField = new TextField();      	
        wifi_IPv4_Getway_TextField.setPrefWidth(270);
        wifi_IPv4_Getway_TextField.setMaxWidth(270);
        wifi_IPv4_Getway_TextField.setMinWidth(270);
        // layout
        grid.add(IPv4_Def_Getway, 0, 6);
        grid.add(Colon3, 2, 6); 
        grid.add(wifi_IPv4_Getway_TextField, 3, 6);
        
        /********** 靜態DNS設定 **********/
        Label IPv4_DNS = new Label(WordMap.get("TC_Static_DNS_Setting"));
        GridPane.setHalignment(IPv4_DNS, HPos.LEFT);      	        
        /******** DNS 1 ********/
        Label IPv4_DNS1 = new Label("DNS 1");     
        IPv4_DNS1.setId("label_DNS");

        Label Colon4=new Label(":");
        Colon4.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 14px; -fx-font-weight: 900;");         
        
        HBox DNS01_box = new HBox(IPv4_DNS1, Colon4);
        DNS01_box.setAlignment(Pos.CENTER_LEFT);        
        DNS01_box.setPadding(new Insets(0, 0, 0, 8));
        DNS01_box.setSpacing(8);        

        wifi_IPv4_DNS1_TextField = new TextField();      
	// layout
        grid.add(IPv4_DNS, 0, 7, 4, 1);
        grid.add(DNS01_box, 0, 8);
        grid.add(wifi_IPv4_DNS1_TextField, 1, 8, 3, 1);
        
        /******** DNS 2 ********/
        Label IPv4_DNS2 = new Label("DNS 2");      
        IPv4_DNS2.setId("label_DNS");

        Label Colon5 = new Label(":");
        Colon5.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 14px; -fx-font-weight: 900;");         
        
        HBox DNS02_box = new HBox(IPv4_DNS2, Colon5);
        DNS02_box.setAlignment(Pos.CENTER_LEFT);        
        DNS02_box.setPadding(new Insets(0, 0, 0, 8));
        DNS02_box.setSpacing(8);        

        wifi_IPv4_DNS2_TextField = new TextField();      
	// layout
        grid.add(DNS02_box,0,9);
        grid.add(wifi_IPv4_DNS2_TextField, 1, 9, 3, 1);
                
        /**********確定************/                 
        wifi_Confirm_IPv4Pane = new Button(WordMap.get("Connect")); // TC_Confirm -> Connect
        wifi_Confirm_IPv4Pane.setPrefSize(80,30);
        wifi_Confirm_IPv4Pane.setMaxSize(80,30);
        wifi_Confirm_IPv4Pane.setMinSize(80,30);
        
        wifi_Confirm_IPv4Pane.setOnAction((ActionEvent event) -> {                                         
            GB.lock_ThinClient = false;
                                                                                            
            // 如果選擇為靜態IP設定 1
            if(wifi_group.getSelectedToggle() == wifi_rb2_StaticIP) {                                                                                               
                if(!wifi_Name_TextField.getText().isEmpty() && !wifi_Password_TextField.getText().isEmpty() && !wifi_IPv4_IP_TextField.getText().isEmpty() && !wifi_IPv4_Mask_TextField.getText().isEmpty()) {
                    try {                    
                        wifi_StaticIP_IP_Setting      = wifi_IPv4_IP_TextField.getText();                        
                        //將submask IP讀取後, 轉為InetAddress （為了把IP轉成cidr）
                        wifi_StaticIP_submask_Setting = wifi_IPv4_Mask_TextField.getText();
                        wifi_StaticIP_mask_cidr       = InetAddress.getByName(wifi_StaticIP_submask_Setting);
                        //寫入檔案, 必須要String, 若是int會是亂碼, 這裡將-Mask IP- 轉-Mask Cidr（int）-再將int轉成String, 寫入檔案
                        wifi_convertNetmaskToCIDR(wifi_StaticIP_mask_cidr);
                        wifi_StaticIP_Final_mask_cidr = String.valueOf(wifi_StaticIP_prflen_ipv4);
                        wifi_StaticIP_Gateway_Setting = wifi_IPv4_Getway_TextField.getText();
                
                        if(wifi_IPv4_DNS1_TextField != null)
                            wifi_StaticIP_DNS01_Setting = wifi_IPv4_DNS1_TextField.getText();
                        else
                            wifi_StaticIP_DNS01_Setting = null;                               

                        if(wifi_IPv4_DNS2_TextField != null) 
                            wifi_StaticIP_DNS02_Setting = wifi_IPv4_DNS2_TextField.getText();
                        else
                            wifi_StaticIP_DNS02_Setting = null;
                                
                        writeWifiStaticIPSetting(wifi_Name_TextField.getText(), wifi_security_type, wifi_Password_TextField.getText(), wifi_StaticIP_IP_Setting, wifi_StaticIP_Final_mask_cidr, wifi_StaticIP_Gateway_Setting);
                        writeStaticIPResolv(wifi_StaticIP_DNS01_Setting, wifi_StaticIP_DNS02_Setting);                     
                    
                    //writeWifiDHCPSetting(GB.wifi_name, GB.wifi_security_type, wifi_Password_TextField.getText());
                    
                        Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","chmod 600 /etc/NetworkManager/system-connections/WiFi_" + wifi_Name_TextField.getText()});
                        try {
                            sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
                        }                          
                        Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli c up id WiFi_" + wifi_Name_TextField.getText() + " &" });    
                        ThinClientStage.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                    }                    
                } 
                else if(wifi_Name_TextField.getText().isEmpty() || wifi_Password_TextField.getText().isEmpty()) {
                    try {
                        EnterWifiCheck_alert();
                    } catch (ParseException ex) {
                        Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } 
                else if(wifi_IPv4_IP_TextField.getText().isEmpty() || wifi_IPv4_Mask_TextField.getText().isEmpty()) {
                    try {
                        EnterCheck_alert();
                    } catch (ParseException ex) {
                        Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }                
            } else if(wifi_group.getSelectedToggle() == wifi_rb1_DHCP) {
                if(!wifi_Name_TextField.getText().isEmpty()) {  // && !wifi_Password_TextField.getText().isEmpty()              
                    try {
                        writeWifiDHCPSetting(wifi_Name_TextField.getText(), wifi_security_type, wifi_Password_TextField.getText());
                        Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","chmod 600 /etc/NetworkManager/system-connections/WiFi_" + wifi_Name_TextField.getText()});
                        try {
                            sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
                        }                        
                        Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli c up id WiFi_" + wifi_Name_TextField.getText() + " &" });     
                        ThinClientStage.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                    }                    
                } else {
                    try {
                        EnterWifiCheck_alert();
                    } catch (ParseException ex) {
                        Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }                                           
            }
        });                                                
        /**********離開************/ 
        wifi_Leave_IPv4Pane = new Button(WordMap.get("TC_Exit"));
        wifi_Leave_IPv4Pane.setPrefSize(80,30);
        wifi_Leave_IPv4Pane.setMaxSize(80,30);
        wifi_Leave_IPv4Pane.setMinSize(80,30);
        if("English".equals(WordMap.get("SelectedLanguage"))){                 
            wifi_Leave_IPv4Pane.setTranslateX(-20);
        }            
        wifi_Leave_IPv4Pane.setTranslateY(1);
        GridPane.setHalignment(wifi_Leave_IPv4Pane, HPos.LEFT);        
        wifi_Leave_IPv4Pane.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {        
                GB.lock_ThinClient = false; 
                ThinClientStage.close();
            }
        });       
        

              
        /**********Cconnect************/ 
        wifi_Connect = new Button(WordMap.get("Connect"));
        wifi_Connect.setPrefSize(80,30);
        wifi_Connect.setMaxSize(80,30);
        wifi_Connect.setMinSize(80,30);
        wifi_Connect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {        
                try {
                    // GB.lock_ThinClient = false;
                    // ThinClientStage.close();
                    Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli c up id " + GB.wifi_name + " &" });
                } catch (IOException ex) {
                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });         
        
        
        /**********Disconnect************/ 
        wifi_Disconnect = new Button(WordMap.get("Disconnect"));
        
        if("English".equals(WordMap.get("SelectedLanguage"))){                 
            wifi_Disconnect.setPrefSize(100,30);
            wifi_Disconnect.setMaxSize(100,30);
            wifi_Disconnect.setMinSize(100,30);
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){           
            wifi_Disconnect.setPrefSize(80,30);
            wifi_Disconnect.setMaxSize(80,30);
            wifi_Disconnect.setMinSize(80,30);
        }          
        

        
        wifi_Disconnect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {        
                try {
                    GB.lock_ThinClient = false;
                    ThinClientStage.close();
                    Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli d disconnect iface " + wlanName });
                } catch (IOException ex) {
                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });            
        
        HBox wifiButton = new HBox();
        wifiButton.getChildren().addAll(wifi_Disconnect,  wifi_Confirm_IPv4Pane);  
        wifiButton.setAlignment(Pos.CENTER_RIGHT);
        wifiButton.setSpacing(15);
        wifiButton.setPadding(new Insets(0, 0, 0, 20));
        wifiButton.setTranslateY(1);  // 2018.04.13 william wifi實作  
        GridPane.setHalignment(wifiButton, HPos.RIGHT);
        
        grid.add(wifiButton, 3, 10, 1, 1); 
        grid.add(wifi_Leave_IPv4Pane, 3, 10, 1, 1);
        /****************** 判斷 wifi name ********************/
        Get_ConnecttingWifi_Name();
        Get_ConnecttingWifi_PWD_uuid_SecurityType();
        /****************** 判斷 是 DHCP 或 Static IP ********************/   
        Get_IPV4_flag = false;
        Process Dhcp_or_staticIP_Process = Runtime.getRuntime().exec("cat /etc/NetworkManager/system-connections/WiFi_" + get_wifi_name); 
        try (BufferedReader output_DNS_or_SIP = new BufferedReader (new InputStreamReader(Dhcp_or_staticIP_Process.getInputStream()))) {
            String line_DNS_or_SIP = output_DNS_or_SIP.readLine();    
            
            while(line_DNS_or_SIP != null) {                
                if(line_DNS_or_SIP.contains("ipv4")) {
                    Get_IPV4_flag = true;
                }
                
                if(Get_IPV4_flag && line_DNS_or_SIP.contains("method=auto")) {
                    Get_IPV4_flag        = false;
                    wifi_DNS_or_StaticIP = true;
                    break;
                } else if(Get_IPV4_flag && line_DNS_or_SIP.contains("method=manual")) {
                    Get_IPV4_flag        = false;
                    wifi_DNS_or_StaticIP = false;    
                    break;
                }                                
                
                
                line_DNS_or_SIP = output_DNS_or_SIP.readLine();
            }
            System.out.println("***** DHCP *****" + wifi_DNS_or_StaticIP + "\n");
            
            output_DNS_or_SIP.close(); 
        }
        
        //若判斷為 true ＝ DHCP ; false = Static IP ;
        if(wifi_DNS_or_StaticIP == true) {            
            GetWifiDHCPInetAddr();
            wifi_rb1_DHCP.setSelected(true);
            wifi_rb1_DHCP.requestFocus();
            
            wifi_IPv4_IP_TextField.setText(wifi_DHCP_IP);
            wifi_IPv4_Mask_TextField.setText(wifi_DHCP_submask);
            wifi_IPv4_Getway_TextField.setText(wifi_DHCP_Gateway);
            wifi_IPv4_DNS1_TextField.setText(wifi_DHCP_DNS01);
            wifi_IPv4_DNS2_TextField.setText(wifi_DHCP_DNS02);
            
            wifi_IPv4_IP_TextField.setEditable(false);
            wifi_IPv4_Mask_TextField.setEditable(false);
            wifi_IPv4_Getway_TextField.setEditable(false);
            wifi_IPv4_DNS1_TextField.setEditable(false);
            wifi_IPv4_DNS2_TextField.setEditable(false);                   
        } else {            
            GetWifiStaticIPInetAddr();
            wifi_rb2_StaticIP.setSelected(true);
            wifi_rb2_StaticIP.requestFocus();
            
            wifi_IPv4_IP_TextField.setText(wifi_StaticIP_IP);
            wifi_IPv4_Mask_TextField.setText(wifi_StaticIP_submask);
            wifi_IPv4_Getway_TextField.setText(wifi_StaticIP_Gateway);
            wifi_IPv4_DNS1_TextField.setText(wifi_StaticIP_DNS01);
            wifi_IPv4_DNS2_TextField.setText(wifi_StaticIP_DNS02);
            
            wifi_IPv4_IP_TextField.setEditable(true);
            wifi_IPv4_Mask_TextField.setEditable(true);
            wifi_IPv4_Getway_TextField.setEditable(true);
            wifi_IPv4_DNS2_TextField.setEditable(true);
            

//            if(Disconnected_flag) {
//                try {
//                    LoadStaticIPStatus();
//                } catch (ParseException ex) {
//                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }

            
        }
       
        wifi_group.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
            if (wifi_group.getSelectedToggle() == wifi_rb1_DHCP) { 
                    wifi_IPv4_IP_TextField.setEditable(false);
                    wifi_IPv4_Mask_TextField.setEditable(false);
                    wifi_IPv4_Getway_TextField.setEditable(false);
                    wifi_IPv4_DNS1_TextField.setEditable(false);
                    wifi_IPv4_DNS2_TextField.setEditable(false);       
                    
                    wifi_IPv4_IP_TextField.setText("");
                    wifi_IPv4_Mask_TextField.setText("");
                    wifi_IPv4_Getway_TextField.setText("");
                    wifi_IPv4_DNS1_TextField.setText("");
                    wifi_IPv4_DNS2_TextField.setText("");
                    
                    wifi_IPv4_IP_TextField.setVisible(false);
                    wifi_IPv4_Mask_TextField.setVisible(false);
                    wifi_IPv4_Getway_TextField.setVisible(false);
                    wifi_IPv4_DNS1_TextField.setVisible(false);
                    wifi_IPv4_DNS2_TextField.setVisible(false);                    
                    

                                        
//                    if(Disconnected_flag) {
//                        IPv4_IP_TextField.setText("");
//                        IPv4_Mask_TextField.setText("");
//                        IPv4_Getway_TextField.setText("");
//                        IPv4_DNS1_TextField.setText("");
//                        IPv4_DNS2_TextField.setText(""); 
//                    }
            } else {               
                    wifi_IPv4_IP_TextField.setEditable(true);
                    wifi_IPv4_Mask_TextField.setEditable(true);
                    wifi_IPv4_Getway_TextField.setEditable(true);
                    wifi_IPv4_DNS1_TextField.setEditable(true);
                    wifi_IPv4_DNS2_TextField.setEditable(true); 
                    
                    wifi_IPv4_IP_TextField.setVisible(true);
                    wifi_IPv4_Mask_TextField.setVisible(true);
                    wifi_IPv4_Getway_TextField.setVisible(true);
                    wifi_IPv4_DNS1_TextField.setVisible(true);
                    wifi_IPv4_DNS2_TextField.setVisible(true);                       
                    
//                    if(Disconnected_flag) {
//                        try {
//                            LoadStaticIPStatus();
//                        } catch (ParseException ex) {
//                            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
//                        }                        
//                    }                    
            }
        });
        
        ChangeWIFILabelLang(IPv4_IP, IPv4_DNS, IPv4_IP_addr, IPv4_Mask, IPv4_Def_Getway, Colon1, Colon2, Colon3, wifi_name_title, wifi_pw_title);
        ChangeWIFIButtonLang(wifi_rb1_DHCP, wifi_rb2_StaticIP, wifi_Confirm_IPv4Pane, wifi_Leave_IPv4Pane, wifi_list, wifi_Disconnect);
        
        rootPane = new BorderPane();                   // 多VD登入中畫面鎖住    
        rootPane.setCenter(grid);
        
        return rootPane;
    }    
    
    // 取連線的wlan名稱
    public void Get_Wlan_Name() throws IOException { 
                            
        Process Detect_wlan_Process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli d status | grep wlan[0-9] | awk '{print $1}'"});

        try (BufferedReader output_wlan = new BufferedReader (new InputStreamReader(Detect_wlan_Process.getInputStream()))) {
            wlanName = output_wlan.readLine();                                                  
            output_wlan.close();
        }               

    }      
    
    /******************** Tab02 : 顯示器 *********************/
    private Pane DisplayPane() throws IOException, ParseException{
        select_display = 3;

        
        /***  主畫面使用GRID的方式Layout ***/
        GridPane Display_grid = new GridPane();
//        Display_grid.setGridLinesVisible(true); // Grid show
        Display_grid.setHgap(8);
        Display_grid.setVgap(10);
        Display_grid.setAlignment(Pos.TOP_CENTER);
        Display_grid.setPadding(new Insets(15, 8, 8, 8));
        
        /***  Button(下)畫面使用GRID的方式Layout ***/       
        GridPane down_grid = new GridPane();
        down_grid.setAlignment(Pos.CENTER);
        down_grid.setPrefHeight(10); // 2018.01.29 william 60 -> 20 
        down_grid.setMaxHeight(10);  // 2018.01.29 william 60 -> 20 
        down_grid.setMinHeight(10);  // 2018.01.29 william 60 -> 20 
        
        /***  底層畫面使用Border的方式Layout ***/
        BorderPane rootPane = new BorderPane();
        rootPane.setCenter(Display_grid);
        rootPane.setBottom(down_grid);
        rootPane.setId("gridpane");
        
         /**********標題**********/
        Label Display_monitor = new Label(WordMap.get("TC_Monitor"));
        if("English".equals(WordMap.get("SelectedLanguage"))){                 
            Display_monitor.setTranslateX(7);
        }        
        else {//if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){           
            Display_monitor.setTranslateX(10);
        }          
        
        GridPane.setHalignment(Display_monitor, HPos.CENTER);      
	Display_grid.add(Display_monitor,0,0,2,1);
        
        /********** 取得螢幕資訊 Get Display information ***********/
        Get_Display_process = Runtime.getRuntime().exec("xrandr");
        try (BufferedReader output_Display_info = new BufferedReader (new InputStreamReader(Get_Display_process.getInputStream()))) {
            String line_Display                     = output_Display_info.readLine();
            String[] Display_name_Line_list         = null;
            String[] Display_current_data_Line_list = null;     
            while(line_Display != null) {
                /*********** 取得螢幕名稱 ***********/                
                if ( line_Display.contains("connected") && !line_Display.contains("disconnected")) {
                    if (!IsPrimary) {
                        Display_name_Line_list = line_Display.split(" ");
                        Display_primary_name   = Display_name_Line_list[0];                 
                        IsPrimary = true;
                        // 取得禁用螢幕
                        if (!line_Display.contains("mm")) {
                            Primary_disable = true;
                            select_display  = 2;
                        }                                                
                    } else {
                        Display_name_Line_list = line_Display.split(" ");
                        Display_second_name    = Display_name_Line_list[0];                  
                        IsSecond = true;   
                        // 取得禁用螢幕
                        if (!line_Display.contains("mm")) {
                            Second_disable = true;
                            select_display = 1;
                        }                           
                    }                                                            
                }                           
                
                /*********** 取得目前解析度 ***********/
                if ( line_Display.contains("*") || line_Display.contains(" +")) {
                    if(!IsSecond) {
                        Display_current_data_Line_list     = line_Display.split(" ");
                        Display_primary_current_resolution = Display_current_data_Line_list[3];                        
                        primary_resolution_size            = Display_primary_current_resolution.split("x");
                        primary_resolution_width           = primary_resolution_size[0];
                        primary_resolution_height          = primary_resolution_size[1];
                    } else {
                        Display_current_data_Line_list    = line_Display.split(" ");
                        Display_second_current_resolution = Display_current_data_Line_list[3];                        
                        second_resolution_size            = Display_second_current_resolution.split("x");
                        second_resolution_width           = second_resolution_size[0];
                        second_resolution_height          = second_resolution_size[1];                        
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
                
                /*********** 取得主螢幕 ***********/
                if (line_Display.contains("+0+0")) { 
                    if(!IsSecond) {                    
                        Primary_Duplicate = true;
                    } else {
                        Second_Duplicate = true;
                    }                      
                }                                                                            
                
                line_Display = output_Display_info.readLine();
            }

            output_Display_info.close();        
        }        
       
        // 判斷是否為同步
        if(Primary_Duplicate && Second_Duplicate) {
            select_display  = 4;
        }

        // 顯示的字串處理
        if(!IsSecond) {
            switch (Display_primary_name) {
                case "VGA1":
                    temp_primary_name = "VGA"; 
                    break;
                case "HDMI1":
                    temp_primary_name = "HDMI";
                    break;
                case "HDMI2":
                    temp_primary_name = "HDMI";
                    break;          
                case "eDP1": // 新增筆電螢幕顯示 2018.05.07
                    temp_primary_name = "Display";
                    break;                      
            }            
        } else {
            
//            temp_primary_name = Display_primary_name;
//            temp_second_name  = Display_second_name;
            if(Display_primary_name.equals("VGA1") && Display_second_name.equals("HDMI1")) {
                temp_primary_name = "VGA"; 
                temp_second_name  = "HDMI";
            } else if (Display_primary_name.equals("HDMI1") && Display_second_name.equals("HDMI2")) {
                temp_primary_name = "HDMI 1"; 
                temp_second_name  = "HDMI 2";                        
            } else if (Display_primary_name.equals("eDP1") && Display_second_name.equals("VGA1")) {
                temp_primary_name = "Display"; 
                temp_second_name  = "VGA";                        
            } else if (Display_primary_name.equals("eDP1") && Display_second_name.equals("HDMI1")) {
                temp_primary_name = "Display"; 
                temp_second_name  = "HDMI";                        
            } else if (Display_primary_name.equals("VGA1") && Display_second_name.equals("HDMI2")) {
                temp_primary_name = "VGA"; 
                temp_second_name  = "HDMI";                      
            } else if (Display_primary_name.equals("eDP1") && Display_second_name.equals("HDMI2")) {
                temp_primary_name = "Display"; 
                temp_second_name  = "HDMI";                        
            }  
            
        }

        /*************************************************** set display information to ui ***************************************************/
        if("English".equals(WordMap.get("SelectedLanguage"))){                 
            Resolution_Title = new Label("1. " + WordMap.get("TC_Resolution") + " " + WordMap.get("Settings"));
        }        
        else {//if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){           
            Resolution_Title = new Label("1. " + WordMap.get("TC_Resolution") + WordMap.get("Settings")); 
        }                        
        GridPane.setHalignment(Resolution_Title, HPos.LEFT);          

        Label Colon0 = new Label(":");
        Colon0.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 15px; -fx-font-weight: 900;");        
       
        //For Test
//        temp_primary_name = "HDMI";
//        temp_second_name  = "VGA";
//        IsSecond          = true;
//        temp_primary_name = "Display";


         // first display name 
        Label Display_01 = new Label("1. " + temp_primary_name); // Display01_name -> Display_primary_name
        GridPane.setHalignment(Display_01, HPos.LEFT);    
        
        Label Colon1 = new Label(":");
        Colon1.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 15px; -fx-font-weight: 900;");
                
        /******Dispaly 1 : 解析度的選擇(ComboBox)******/        
        Display01_ResolutionList      = FXCollections.observableArrayList();
        String HDMI01_resolution_data = Display_primary_resolution_list.toString(); // Display01_resolution_list -> Display_primary_resolution_list
        // 支援解析度的所有值,以（";"）為間隔取得
        for (int i = 1; i < HDMI01_resolution_data.split("[\\[\",\"\\]\\s]+").length; i++) {
            //Object term=HDMI01_resolution_data;
            HDMI01_resolution_list   = HDMI01_resolution_data.split("[\\[\",\"\\]\\s]+");
            /*** 解析度 小於 800*600 則不顯示 其解析度 給使用者選擇 ***/
            String[] text_resolution = HDMI01_resolution_list[i].split("x");
            String sx                = text_resolution[0];
            String sy                = text_resolution[1];
            int intValue_x           = Integer.valueOf(sx);
            int intValue_y           = Integer.valueOf(sy);
            int z                    = intValue_x * intValue_y;
            if(z >= 307200){ //480000 -> 921600
               Display01_ResolutionList.addAll(HDMI01_resolution_list[i]);
            }            
        }
        // Display 1 : 解析度的選擇(ComboBox)
        primary_ResolutionSelection = new ComboBox(Display01_ResolutionList);
        primary_ResolutionSelection.getSelectionModel().select(Display_primary_current_resolution); // Display01_current_data -> Display_primary_current_resolution          

        /***  以下抓取變更 Display 1 : '解析度' 後的動作  ***/
        primary_ResolutionSelection.setOnAction((Event event) -> {
            Display_primary_current_resolution = primary_ResolutionSelection.getValue().toString();             
            primary_resolution_size            = Display_primary_current_resolution.split("x");
            primary_resolution_width           = primary_resolution_size[0];
            primary_resolution_height          = primary_resolution_size[1];            
        });
                                       
        // second display name
        Label Display_02 = new Label("2. " + temp_second_name); // test -> Display_second_name Display_primary_name
        GridPane.setHalignment(Display_02, HPos.LEFT);
        
        Label Colon2 = new Label(":");
        Colon2.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 15px; -fx-font-weight: 900;");        
                            
        /******Dispaly 2 : 解析度的選擇(ComboBox)******/
        Display02_ResolutionList = FXCollections.observableArrayList();
        String HDMI02_resolution_data = Display_second_resolution_list.toString();
        for (int i = 1; i < HDMI02_resolution_data.split("[\\[\",\"\\]\\s]+").length; i++) {
            HDMI02_resolution_list   = HDMI02_resolution_data.split("[\\[\",\"\\]\\s]+");
            /*** 解析度 小於 800*600 則不顯示 其解析度 給使用者選擇 ***/
            String[] text_resolution = HDMI02_resolution_list[i].split("x");
            String sx                = text_resolution[0];
            String sy                = text_resolution[1];
            int intValue_x           = Integer.valueOf(sx);
            int intValue_y           = Integer.valueOf(sy);
            int z                    = intValue_x * intValue_y;
            if(z >= 307200){ //480000 -> 921600
               Display02_ResolutionList.addAll(HDMI02_resolution_list[i]);
            }            
        }        
        // Display 2 : 解析度的選擇(ComboBox)
        second_ResolutionSelection = new ComboBox(Display02_ResolutionList);
        second_ResolutionSelection.getSelectionModel().select(Display_second_current_resolution); // Display01_current_data -> Display_primary_current_resolution  
                        
        /***  以下抓取變更 Display 2 : '解析度' 後的動作  ***/
        second_ResolutionSelection.setOnAction((Event event) -> {
            Display_second_current_resolution = second_ResolutionSelection.getValue().toString();             
            second_resolution_size            = Display_second_current_resolution.split("x");
            second_resolution_width           = second_resolution_size[0];
            second_resolution_height          = second_resolution_size[1];                  
        });        
        /*------------------------------display mode------------------------------*/
        display_group        = new ToggleGroup();
        if("English".equals(WordMap.get("SelectedLanguage"))){                 
            rb_display01 = new RadioButton(temp_primary_name + " " + WordMap.get("Only"));
            rb_display02 = new RadioButton(temp_second_name + " " + WordMap.get("Only")); // test -> Display_second_name Display_primary_name    
        }        
        else {//if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){           
            rb_display01 = new RadioButton(WordMap.get("Only") + " " + temp_primary_name);
            rb_display02 = new RadioButton(WordMap.get("Only") + " " + temp_second_name); // test -> Display_second_name Display_primary_name   
        }                
        rb_display_Extend    = new RadioButton(WordMap.get("Display_Extend")); 
        rb_display_Duplicate = new RadioButton(WordMap.get("Display_Duplicate"));                 

        rb_display01.setToggleGroup(display_group);        
        rb_display02.setToggleGroup(display_group);
        rb_display_Extend.setToggleGroup(display_group);
        rb_display_Duplicate.setToggleGroup(display_group);               


        /*------------------------------Display Settings------------------------------*/                                
        if("English".equals(WordMap.get("SelectedLanguage"))){                 
            Display_Settings_title = new Label("2. " + WordMap.get("Mode") + " "  + WordMap.get("Settings"));
        }        
        else {//if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){           
            Display_Settings_title = new Label("2. " + WordMap.get("Mode") + WordMap.get("Settings"));
        }                                 
        GridPane.setHalignment(Display_Settings_title, HPos.LEFT);    
        
        Label Colon4 = new Label(":");
        Colon4.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 15px; -fx-font-weight: 900;");
        

                     
        /*
        checkBox_Primary.selectedProperty().addListener(new ChangeListener<Boolean>() {
           public void changed(ObservableValue<? extends Boolean> ov,
             Boolean old_val, Boolean new_val) {
             if(checkBox_Primary.isSelected()) {
                primary_ResolutionSelection.setDisable(true);
                checkBox_Second.setDisable(true);
                rb_display01.setDisable(true);
                rb_display02.setDisable(true);
                // display_group.selectToggle(null);
                checkBox_Duplicate.setDisable(true);
                checkBox_Extend.setDisable(true);
                Primary_disable = true;
             } else {
                primary_ResolutionSelection.setDisable(false);
                checkBox_Second.setDisable(false);
                rb_display01.setDisable(false);
                rb_display02.setDisable(false);       
                checkBox_Duplicate.setDisable(false);
                checkBox_Extend.setDisable(false);
                Primary_disable = false;
             }
          }
        });        
       
        */
        display_group.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {          
            if (display_group.getSelectedToggle() == rb_display01) { 
                select_display = 1;
                primary_ResolutionSelection.setDisable(false);
                second_ResolutionSelection.setDisable(true);
            } else if (display_group.getSelectedToggle() == rb_display02) {
                select_display = 2;
                primary_ResolutionSelection.setDisable(true);
                second_ResolutionSelection.setDisable(false);                
            } else if (display_group.getSelectedToggle() == rb_display_Extend) {
                select_display = 3;
                primary_ResolutionSelection.setDisable(false);
                second_ResolutionSelection.setDisable(false);                 
            } else if (display_group.getSelectedToggle() == rb_display_Duplicate) {
                select_display = 4;
                primary_ResolutionSelection.setDisable(true);
                second_ResolutionSelection.setDisable(true);                 
            }              
       });                                       
        
        /**********訊息顯示***********/ 
        Label System_Display_AlertMessage_1 = new Label();
        GridPane.setHalignment(System_Display_AlertMessage_1, HPos.LEFT);  // 設定Grid Pane中物件水平對齊方式，並且靠中央對齊
        Display_grid.add(System_Display_AlertMessage_1,0,8,4,1); // add()方法是先設定"行參數"，再設定"列參數"
        
        Label System_Display_AlertMessage_2 = new Label();
        GridPane.setHalignment(System_Display_AlertMessage_2, HPos.LEFT);  // 設定Grid Pane中物件水平對齊方式，並且靠中央對齊
        Display_grid.add(System_Display_AlertMessage_2,0,9,4,1); // add()方法是先設定"行參數"，再設定"列參數"
                                      
        
        /**********離開************/ // 2018.01.15 william 修改TC設定的按鈕邏輯       
        Leave_DisplayPane = new Button(WordMap.get("TC_Exit"));
        Leave_DisplayPane.setPrefSize(80,30);
        Leave_DisplayPane.setMaxSize(80,30);
        Leave_DisplayPane.setMinSize(80,30);
        Leave_DisplayPane.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {        
                GB.lock_ThinClient = false; 
                ThinClientStage.close();
            }
        });        
        /**********進階************/ 
        Advanced_DisplayPane = new Button(WordMap.get("Advanced_Settings"));       
               
        if("English".equals(WordMap.get("SelectedLanguage"))){
            Advanced_DisplayPane.setPrefSize(95,30);
            Advanced_DisplayPane.setMaxSize(95,30);
            Advanced_DisplayPane.setMinSize(95,30);           
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
            Advanced_DisplayPane.setPrefSize(80,30);
            Advanced_DisplayPane.setMaxSize(80,30);
            Advanced_DisplayPane.setMinSize(80,30);    
        }        
        
        Advanced_DisplayPane.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {                  
//                GB.lock_ThinClient = false; 
//                ThinClientStage.close();
                // Advanced_DisplayPane.setDisable(true); // Arandr modify 2018.05.31
                try {
                    Runtime.getRuntime().exec("rm /root/checkarandr.txt").waitFor();
                } catch (Exception ex) {
                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                }                
                arandr_closeFlag = false;
                arandr_TimerFlag = false;
                ArandrTimer();

                
                if(!IsInstallArandr) {
                    WriteInstalllogChange();
                    try {                        
                        install_arandr_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","/root/arandr/install.sh"}); 
                    } catch (IOException ex) {
                        Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                try { 
                    switch (WordMap.get("SelectedLanguage")) {
                        case "English":
                            run_arandr_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","/root/arandr/arandr"});                        
//                            {
//                                try {
//                                    run_arandr_process.waitFor();
//                                    arandr_returncode = run_arandr_process.exitValue();
//                                    GB.arandr_returncode = arandr_returncode;                          
//                                } catch (InterruptedException ex) {
//                                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                            }
                            break;
                        case "TraditionalChinese":
                            run_arandr_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","LANGUAGE=zh_TW /root/arandr/arandr"});                         
//                            {
//                                try {
//                                    run_arandr_process.waitFor();
//                                    arandr_returncode = run_arandr_process.exitValue();
//                                    GB.arandr_returncode = arandr_returncode;                          
//                                } catch (InterruptedException ex) {
//                                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                            }                            
                            break;
                        case "SimpleChinese":
                            run_arandr_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","LANGUAGE=zh_CN /root/arandr/arandr"});                               
//                            {
//                                try {
//                                    run_arandr_process.waitFor();
//                                    arandr_returncode = run_arandr_process.exitValue();
//                                    GB.arandr_returncode = arandr_returncode;                          
//                                } catch (InterruptedException ex) {
//                                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                            }                            
                            break;
                        default:
                          run_arandr_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","/root/arandr/arandr"});                              
                            break;
                    }                
                } catch (IOException ex) {
                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                }                
                
            }
        });         
        
        
        /**********設定************/ // 2018.01.29 william 雙螢幕實作     
        Set_DisplayPane = new Button(WordMap.get("TC_Setting"));       
        Set_DisplayPane.setPrefSize(80,30);
        Set_DisplayPane.setMaxSize(80,30);
        Set_DisplayPane.setMinSize(80,30);
        Set_DisplayPane.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {  
                try {
                    rmDD();
                    SetMonitor(Display_primary_name, Display_second_name, primary_resolution_width, primary_resolution_height, second_resolution_width, second_resolution_height, IsSecond, select_display);
                } catch (IOException ex) {
                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                GB.lock_ThinClient = false; 
                ThinClientStage.close();
                
                GB.monitor_restart = 1;
            }
        });         
        
        Resolution_Title_box = new HBox(Resolution_Title, Colon0);
        Resolution_Title_box.setAlignment(Pos.CENTER_LEFT);        
        Resolution_Title_box.setPadding(new Insets(0, 0, 0, 8));
        Resolution_Title_box.setSpacing(8);
        Resolution_Title_box.setTranslateX(-78); // -75                                        
        
        Dispaly01_name_box = new HBox(Display_01, Colon1);
        Dispaly01_name_box.setAlignment(Pos.CENTER_LEFT);        
        Dispaly01_name_box.setPadding(new Insets(0, 0, 0, 8));
        Dispaly01_name_box.setSpacing(8);
        Dispaly01_name_box.setTranslateX(35);                      
        
        primary_ResolutionSelection.setTranslateX(35);
        
        Dispaly02_name_box = new HBox(Display_02, Colon2);
        Dispaly02_name_box.setAlignment(Pos.CENTER_LEFT);        
        Dispaly02_name_box.setPadding(new Insets(0, 0, 0, 8));
        Dispaly02_name_box.setSpacing(8);
        Dispaly02_name_box.setTranslateX(35); 
        
        second_ResolutionSelection.setTranslateX(35);  
        
        Display_Settings_title_box = new HBox(Display_Settings_title, Colon4);
        Display_Settings_title_box.setAlignment(Pos.CENTER_LEFT);        
        Display_Settings_title_box.setPadding(new Insets(0, 0, 0, 8));
        Display_Settings_title_box.setSpacing(8);
        Display_Settings_title_box.setTranslateX(-78); // -75      
        
        rbHBox = new HBox();        
        rbHBox.getChildren().addAll(rb_display01,rb_display02);  
        rbHBox.setAlignment(Pos.CENTER_LEFT);
        rbHBox.setSpacing(15);    
        rbHBox.setTranslateX(-50);
        
        rbHBox2 = new HBox();
        rbHBox2.getChildren().addAll(rb_display_Extend, rb_display_Duplicate);  
        rbHBox2.setAlignment(Pos.CENTER_LEFT);
        rbHBox2.setSpacing(15);          
        rbHBox2.setTranslateX(-50);           
        
        // 2018.01.15 william 修改TC設定的按鈕邏輯
        DisplayButton = new HBox();
        DisplayButton.getChildren().addAll(Leave_DisplayPane, Advanced_DisplayPane, Set_DisplayPane);  
        DisplayButton.setAlignment(Pos.CENTER_RIGHT);
        DisplayButton.setSpacing(15);
        DisplayButton.setPadding(new Insets(0, 0, 0, 20));
        
        Display_grid.add(Resolution_Title_box,1,1);
        Display_grid.add(Dispaly01_name_box,0,2);        
        Display_grid.add(primary_ResolutionSelection,1,2); // 1,3 -> 1,2 

        if(IsSecond) {
            Display_grid.add(Dispaly02_name_box,0,4);        
            Display_grid.add(second_ResolutionSelection,1,4); // 1,5 -> 1,4       
            Display_grid.add(Display_Settings_title_box,1,5);
            Display_grid.add(rbHBox, 1, 6);
            Display_grid.add(rbHBox2, 1, 7);
                    
            switch (select_display) {
                case 1:
                    display_group.selectToggle(rb_display01);
                    second_ResolutionSelection.setDisable(true);
                    break;
                case 2:
                    display_group.selectToggle(rb_display02);
                    primary_ResolutionSelection.setDisable(true);
                    break;
                case 3:
                    display_group.selectToggle(rb_display_Extend);    
                    break;
                case 4:
                    display_group.selectToggle(rb_display_Duplicate);    
                    primary_ResolutionSelection.setDisable(true);
                    second_ResolutionSelection.setDisable(true);
                    break;                
            }                 
        }      
        
        Display_grid.add(DisplayButton,0,15,2,1); // 0,12 // 2018.04.13 william wifi實作 0,9,2,1 -> 0,15,2,1   
        
        /*******************************************************************/ 
        if(WordMap.get("SelectedLanguage").equals("English")) {                  
            if(IsSecond) {
                /************ 英 - 雙螢幕 ************/
                if(temp_primary_name.equals("VGA")) {
                    primary_ResolutionSelection.setPrefWidth(190);
                    primary_ResolutionSelection.setMaxWidth(190);
                    primary_ResolutionSelection.setMinWidth(190); 
                    second_ResolutionSelection.setPrefWidth(190);
                    second_ResolutionSelection.setMaxWidth(190);
                    second_ResolutionSelection.setMinWidth(190); 
                    rb_display_Duplicate.setTranslateX(23);                    
                    DisplayButton.setTranslateX(43);
                } else if (temp_primary_name.equals("HDMI") || temp_primary_name.equals("HDMI 1") || temp_primary_name.equals("HDMI 2")) {
                    primary_ResolutionSelection.setPrefWidth(210);
                    primary_ResolutionSelection.setMaxWidth(210);
                    primary_ResolutionSelection.setMinWidth(210); 
                    second_ResolutionSelection.setPrefWidth(210);
                    second_ResolutionSelection.setMaxWidth(210);
                    second_ResolutionSelection.setMinWidth(210);   
                    Dispaly01_name_box.setTranslateX(50);
                    primary_ResolutionSelection.setTranslateX(50);
                    Dispaly02_name_box.setTranslateX(50);         
                    second_ResolutionSelection.setTranslateX(50);  
                    rb_display_Duplicate.setTranslateX(48);
                    DisplayButton.setTranslateX(16);
                } else if (temp_primary_name.equals("Display")) {
                    primary_ResolutionSelection.setPrefWidth(190);
                    primary_ResolutionSelection.setMaxWidth(190);
                    primary_ResolutionSelection.setMinWidth(190); 
                    second_ResolutionSelection.setPrefWidth(190);
                    second_ResolutionSelection.setMaxWidth(190);
                    second_ResolutionSelection.setMinWidth(190); 
                    rb_display_Duplicate.setTranslateX(49);                    
                    DisplayButton.setTranslateX(28);
                }                                        
                DisplayButton.setTranslateY(12);
            } else {      
                /************ 英 - 單螢幕 ************/
                if(temp_primary_name.equals("VGA")) {
                    primary_ResolutionSelection.setPrefWidth(158);
                    primary_ResolutionSelection.setMaxWidth(158);
                    primary_ResolutionSelection.setMinWidth(158);                    
                    DisplayButton.setTranslateX(66);
                } else if (temp_primary_name.equals("HDMI") || temp_primary_name.equals("HDMI 1") || temp_primary_name.equals("HDMI 2")) {
                    primary_ResolutionSelection.setPrefWidth(150);
                    primary_ResolutionSelection.setMaxWidth(150);
                    primary_ResolutionSelection.setMinWidth(150);                    
                    DisplayButton.setTranslateX(66);
                } else if (temp_primary_name.equals("Display")) {
                    primary_ResolutionSelection.setPrefWidth(158);
                    primary_ResolutionSelection.setMaxWidth(158);
                    primary_ResolutionSelection.setMinWidth(158);                    
                    DisplayButton.setTranslateX(63);
                }                          
                DisplayButton.setTranslateY(104);
            }                                    
        } 
        
        else if (("Japanese".equals(WordMap.get("SelectedLanguage")))) {
            if(IsSecond) {       
                /************ 中 - 雙螢幕 ************/
                if(temp_primary_name.equals("VGA")) {
                    primary_ResolutionSelection.setPrefWidth(135);
                    primary_ResolutionSelection.setMaxWidth(135);
                    primary_ResolutionSelection.setMinWidth(135); 
                    second_ResolutionSelection.setPrefWidth(135);
                    second_ResolutionSelection.setMaxWidth(135);
                    second_ResolutionSelection.setMinWidth(135);   
                    rb_display_Duplicate.setTranslateX(3);
                    DisplayButton.setTranslateX(78);
                } else if (temp_primary_name.equals("HDMI") || temp_primary_name.equals("HDMI 1") || temp_primary_name.equals("HDMI 2")) {
                    primary_ResolutionSelection.setPrefWidth(155);
                    primary_ResolutionSelection.setMaxWidth(155);
                    primary_ResolutionSelection.setMinWidth(155); 
                    second_ResolutionSelection.setPrefWidth(155);
                    second_ResolutionSelection.setMaxWidth(155);
                    second_ResolutionSelection.setMinWidth(155);   
                    Dispaly01_name_box.setTranslateX(50);
                    primary_ResolutionSelection.setTranslateX(50);
                    Dispaly02_name_box.setTranslateX(50);         
                    second_ResolutionSelection.setTranslateX(50);     
                    rb_display_Duplicate.setTranslateX(30);
                    DisplayButton.setTranslateX(49);
                } else if (temp_primary_name.equals("Display")) {
                    primary_ResolutionSelection.setPrefWidth(135);
                    primary_ResolutionSelection.setMaxWidth(135);
                    primary_ResolutionSelection.setMinWidth(135); 
                    second_ResolutionSelection.setPrefWidth(135);
                    second_ResolutionSelection.setMaxWidth(135);
                    second_ResolutionSelection.setMinWidth(135);   
                    rb_display_Duplicate.setTranslateX(29);
                    DisplayButton.setTranslateX(57);
                }                   
                DisplayButton.setTranslateY(12);
            } else {       
                /************ 中 - 單螢幕 ************/
                if(temp_primary_name.equals("VGA")) {
                    
                    primary_ResolutionSelection.setPrefWidth(145);
                    primary_ResolutionSelection.setMaxWidth(145);
                    primary_ResolutionSelection.setMinWidth(145);                    
                    
                } else if (temp_primary_name.equals("HDMI") || temp_primary_name.equals("HDMI 1") || temp_primary_name.equals("HDMI 2")) {
                    primary_ResolutionSelection.setPrefWidth(135);
                    primary_ResolutionSelection.setMaxWidth(135);
                    primary_ResolutionSelection.setMinWidth(135);                    
                    
                } else if (temp_primary_name.equals("Display")) {
                    primary_ResolutionSelection.setPrefWidth(135);
                    primary_ResolutionSelection.setMaxWidth(135);
                    primary_ResolutionSelection.setMinWidth(135);                        
                    Resolution_Title.setTranslateX(-25);                        
                    Colon0.setTranslateX(-25);    
                    Dispaly01_name_box.setTranslateX(25);                              
                    primary_ResolutionSelection.setTranslateX(25);                    
                    
                }              
                DisplayButton.setTranslateX(100);
                DisplayButton.setTranslateY(110);
            }         
        }         
        
        
        else { //else if (("TraditionalChinese".equals(WordMap.get("SelectedLanguage"))) || ("SimpleChinese".equals(WordMap.get("SelectedLanguage")))) {
            if(IsSecond) {       
                /************ 中 - 雙螢幕 ************/
                if(temp_primary_name.equals("VGA")) {
                    primary_ResolutionSelection.setPrefWidth(135);
                    primary_ResolutionSelection.setMaxWidth(135);
                    primary_ResolutionSelection.setMinWidth(135); 
                    second_ResolutionSelection.setPrefWidth(135);
                    second_ResolutionSelection.setMaxWidth(135);
                    second_ResolutionSelection.setMinWidth(135);   
                    rb_display_Duplicate.setTranslateX(22);
                    DisplayButton.setTranslateX(40);
                } else if (temp_primary_name.equals("HDMI") || temp_primary_name.equals("HDMI 1") || temp_primary_name.equals("HDMI 2")) {
                    primary_ResolutionSelection.setPrefWidth(155);
                    primary_ResolutionSelection.setMaxWidth(155);
                    primary_ResolutionSelection.setMinWidth(155); 
                    second_ResolutionSelection.setPrefWidth(155);
                    second_ResolutionSelection.setMaxWidth(155);
                    second_ResolutionSelection.setMinWidth(155);   
                    Dispaly01_name_box.setTranslateX(50);
                    primary_ResolutionSelection.setTranslateX(50);
                    Dispaly02_name_box.setTranslateX(50);         
                    second_ResolutionSelection.setTranslateX(50);     
                    rb_display_Duplicate.setTranslateX(50);
                    DisplayButton.setTranslateX(11);
                } else if (temp_primary_name.equals("Display")) {
                    primary_ResolutionSelection.setPrefWidth(135);
                    primary_ResolutionSelection.setMaxWidth(135);
                    primary_ResolutionSelection.setMinWidth(135); 
                    second_ResolutionSelection.setPrefWidth(135);
                    second_ResolutionSelection.setMaxWidth(135);
                    second_ResolutionSelection.setMinWidth(135);   
                    rb_display_Duplicate.setTranslateX(47);
                    DisplayButton.setTranslateX(27);
                }                   
                DisplayButton.setTranslateY(12);
            } else {       
                /************ 中 - 單螢幕 ************/
                if(temp_primary_name.equals("VGA")) {
                    primary_ResolutionSelection.setPrefWidth(145);
                    primary_ResolutionSelection.setMaxWidth(145);
                    primary_ResolutionSelection.setMinWidth(145);                    
                    DisplayButton.setTranslateX(44);
                } else if (temp_primary_name.equals("HDMI") || temp_primary_name.equals("HDMI 1") || temp_primary_name.equals("HDMI 2")) {
                    primary_ResolutionSelection.setPrefWidth(135);
                    primary_ResolutionSelection.setMaxWidth(135);
                    primary_ResolutionSelection.setMinWidth(135);                    
                    DisplayButton.setTranslateX(44);
                } else if (temp_primary_name.equals("Display")) {
                    primary_ResolutionSelection.setPrefWidth(135);
                    primary_ResolutionSelection.setMaxWidth(135);
                    primary_ResolutionSelection.setMinWidth(135);                        
                    Resolution_Title.setTranslateX(-25);                        
                    Colon0.setTranslateX(-25);    
                    Dispaly01_name_box.setTranslateX(25);                              
                    primary_ResolutionSelection.setTranslateX(25);                    
                    DisplayButton.setTranslateX(44);
                }                                   
                DisplayButton.setTranslateY(110);
            }         
        }         
        
        ChangeDisplayButtonLang2(Leave_DisplayPane, Advanced_DisplayPane, Set_DisplayPane);
        ChangeMonitor_1_LabelLang(Display_monitor, Display_01, Display_02, rb_display01, rb_display02, rb_display_Extend, rb_display_Duplicate);     
//        System.out.println("Display_primary_resolution_list: " + Display01_ResolutionList.get(0));      
        
        return rootPane;
    }
    
    /******************** Tab03 : 系統*********************/
    private Pane SystemPane() throws ParseException, IOException{
        
        GridPane System_grid = new GridPane();
        System_grid.setHgap(8);
        System_grid.setVgap(10);
        System_grid.setAlignment(Pos.TOP_CENTER);
        System_grid.setPadding(new Insets(15, 8, 8, 8));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        //System_grid.setGridLinesVisible(true); //開啟或關閉表格的分隔線        
        System_grid.setId("gridpane");
        
         /**********瘦客戶機韌體**********/
        Label System_Client = new Label(WordMap.get("TC_Client_Firmware"));
        GridPane.setHalignment(System_Client, HPos.CENTER);      
	System_grid.add(System_Client,0,0,4,1);
        System_Client.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        //System_Client.setId("title_Label");
        
        /**********上次更新日期***********/
        Label System_Update = new Label(WordMap.get("TC_Last_Update"));
        GridPane.setHalignment(System_Update, HPos.RIGHT);    
	System_grid.add(System_Update,0,2); // 2017.06.22 william System version check 3 -> 2
        System_Update.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        //System_C_Ver.setId("title_Label");    
        
        Label Colon0=new Label(":");//將":"固定在一個位置
        GridPane.setHalignment(Colon0, HPos.LEFT);
        System_grid.add(Colon0, 1, 2); // 2017.06.22 william System version check 3 -> 2
        Colon0.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)             
                
        Label System_Up_date= new Label();
        GridPane.setHalignment(System_Up_date, HPos.LEFT);    
	System_grid.add(System_Up_date,2,2); // 2017.06.22 william System version check 3 -> 2
        System_Up_date.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        //System_Current_Ver.setId("title_Label");    
        
        /******** 讀取 上次更新時間 ********/
        String updatetime;
        String updatetime_str = "cat /root/lastupdate_time";
        Process updatetime_process = Runtime.getRuntime().exec(updatetime_str);                     
        try (BufferedReader updatetime_output = new BufferedReader (new InputStreamReader(updatetime_process.getInputStream()))) {
            
            String updatetime_line = updatetime_output.readLine();
            if(updatetime_line != null)
                updatetime = updatetime_line;
            else
                updatetime = "2019-01-01 00:00:00";
            
            System.out.println(" updatetime  : " + updatetime +"\n");

        }
        System_Up_date.setText(updatetime);
        
        
        /**********現在韌體版本***********/
        Label System_C_Ver = new Label(WordMap.get("TC_Current_Ver"));
        GridPane.setHalignment(System_C_Ver, HPos.RIGHT);    
	System_grid.add(System_C_Ver,0,4); // 2017.06.22 william System version check 6 -> 4
        System_C_Ver.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        //System_C_Ver.setId("title_Label");    
        
        Label Colon1=new Label(":");//將":"固定在一個位置
        GridPane.setHalignment(Colon1, HPos.LEFT);
        System_grid.add(Colon1, 1, 4); // 2017.06.22 william System version check 6 -> 4
        Colon1.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)             
        
        LoadVersion(); // 讀取 目前 jsonfile Version
        
        Label System_Current_Ver = new Label(Version_num);
        GridPane.setHalignment(System_Current_Ver, HPos.LEFT);    
	System_grid.add(System_Current_Ver,2,4); // 2017.06.22 william System version check 6 -> 4
        System_Current_Ver.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        //System_Current_Ver.setId("title_Label");
        
        /**********最新韌體版本***********/
        Label System_S_Ver = new Label(WordMap.get("TC_Server_Ver"));
        GridPane.setHalignment(System_S_Ver, HPos.RIGHT);    
	System_grid.add(System_S_Ver,0,6); // 2017.06.22 william System version check 9 -> 6
        System_S_Ver.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        //System_S_Ver.setId("title_Label");
        
        Label Colon2=new Label(":");//將":"固定在一個位置
        GridPane.setHalignment(Colon2, HPos.LEFT);
        System_grid.add(Colon2, 1, 6); // 2017.06.22 william System version check 9 -> 6
        Colon2.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)             
        
        
        Label System_Server_Ver = new Label();
        GridPane.setHalignment(System_Server_Ver, HPos.LEFT);    
	System_grid.add(System_Server_Ver,2,6); // 2017.06.22 william System version check 9 -> 6
        System_Server_Ver.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        //System_Server_Ver.setId("title_Label");
        
        /**********訊息顯示***********/ // 2017.06.22 william System version check
        System_Ver_AlertMessage = new Label();
        GridPane.setHalignment(System_Ver_AlertMessage, HPos.LEFT);  // 設定Grid Pane中物件水平對齊方式，並且靠中央對齊
        System_grid.add(System_Ver_AlertMessage,0,8,4,1); // add()方法是先設定"行參數"，再設定"列參數"
        System_Ver_AlertMessage.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        
        System_Ver_AlertMessage.setId("AlertMessage_Label");
        
        Label System_Ver_AlertEvent = new Label();
        GridPane.setHalignment(System_Ver_AlertEvent, HPos.LEFT);  // 設定Grid Pane中物件水平對齊方式，並且靠中央對齊
        System_grid.add(System_Ver_AlertEvent,0,9,4,1); // add()方法是先設定"行參數"，再設定"列參數"
        System_Ver_AlertEvent.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        
        System_Ver_AlertEvent.setId("AlertMessage_Label");        
        

        /**********檢查************/
        Check=new Button(WordMap.get("TC_Check"));
        
        if(WordMap.get("SelectedLanguage").equals("Japanese")) {
            Check.setPrefSize(110,30);
            Check.setMaxSize(110,30);
            Check.setMinSize(110,30);
        } else {
            Check.setPrefSize(80,30);
            Check.setMaxSize(80,30);
            Check.setMinSize(80,30);
        }        
        

        Check.setOnAction((ActionEvent event) -> {
            
            if(Lock_Check == false){
                
                Lock_Check=true;                
                 
                Boolean Result=PATTERN.matcher(Address).matches();
                if(Result==true || Address!=null){ // 2017.07.19 william IP use Domain Name
                    Task<Void> CheckTask = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            System.out.println("** CheckTask ** \n");
                            // 2017.08.10 william IP增加port欄位，預設443
                            if((Port==null)||("".equals(Port))) {
                                CurrentIP_Port = "443";
                            }
                            else {
                                CurrentIP_Port = Port;
                            }                             
                            PCV.PingCheckVer(Address,CurrentIP_Port); // 2017.08.10 william IP增加port欄位，預設443
                            CV.ListFirmwareVersion(Address,CurrentIP_Port);  // 2017.08.10 william IP增加port欄位，預設443 

                            return null;
                        }
                    };

                    CheckTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            System.out.println("** CheckTask.setOnFailed ** \n");
                            //System.out.println("ChangePWTask Ping - testError = "+CPL.testError+"\n"); 
                            //System.out.println("ChangePWTask - connCode = "+CPL.CPLconnCode+"\n");
                            if(PCV.testError==false){
                                SA.CheckSystem_testError_Alert(MainStage);
                            }
                           
                            Lock_Check=false;

                        }
                    });

                    CheckTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            System.out.println("** CheckTask.setOnSucceeded ** \n");
                            
                            if(CV.check_code != 200){
                                SA.CheckSystem_testError_Alert(MainStage);
                            }
                            /** 若檢查 版本號相同 則將更新button-Disable **/
//                            if( CV.check_code == 200 ){                            
//                                if( CV.newVersion.equals(Version_num) ){
//                                    Update.setDisable(true);        
//                                }
//                                if( !CV.newVersion.equals(Version_num) ){
//                                    Update.setDisable(false); 
//                                }
//                                System_Server_Ver.setText(CV.newVersion);                            
//                            }

                            if( CV.check_code == 200 ){
                                
                                System_Server_Ver.setText(CV.newVersion); 
                                
                                // 2017.06.22 william System version check
                                // int result = CV.newVersion.compareTo(Version_num);
                                int result = -1;
                                int count = 0;
                                String[] _newArray = null;
                                String[] _oldArray = null;

                                _oldArray = Version_num.trim().split("\\.");
                                _newArray = CV.newVersion.trim().split("\\.");
                                
                                for(int i = 0; i < 3 ; i++) {
                                    if(Integer.parseInt(_newArray[i]) > Integer.parseInt(_oldArray[i])) {
                                        System.out.println(i +". " + "New : " + Integer.parseInt(_newArray[i]) + ", Old : " + Integer.parseInt(_oldArray[i]));
                                        result = 0;
                                        break;
                                    } else if(Integer.parseInt(_newArray[i]) < Integer.parseInt(_oldArray[i])) {
                                        break;
                                    } else if(Integer.parseInt(_newArray[i]) == Integer.parseInt(_oldArray[i])) {
                                        count++;
                                    }
                                    
                                    if(count == 3)
                                        result = 0;
                                    
                                }
                                
                                // 2017.11.09 william 後續改寫 參考https://dreammushroomsprogramnotes.blogspot.tw/2015/08/stringsplit-for-java.html
                                if(result<0) { // 2017.11.09 william 版本檢查改寫 result==-1 || result==0 || result==-2
                                    System.out.println("已經是最新版本 " + result);
                                    // Update.setDisable(true); // 2018.01.15 william 修改TC設定的按鈕邏輯
                                    if(WordMap.get("SelectedLanguage").equals("Japanese")) {
                                        System_Ver_AlertMessage.setStyle("-fx-font-size: 13px;");                                    
                                        System_Ver_AlertEvent.setStyle("-fx-font-size: 13px;");  
                                    }                                     

                                    System_Ver_AlertMessage.setText(WordMap.get("TC_Server_Ver_AlertMessage")); 
                                    System_Ver_AlertEvent.setText(WordMap.get("TC_Server_Ver_AlertEvent")); 
                                    
                                    
                                    
                                    if(WordMap.get("SelectedLanguage").equals("English")) {
                                        SystemButton.setTranslateX(-90); 

                                    } else if(WordMap.get("SelectedLanguage").equals("Japanese")) {
                                        SystemButton.setTranslateX(-115); 
                                        SystemButton.setTranslateY(50);

                                    } else {
                                        SystemButton.setTranslateX(-81); 
                                    }     
                                    
                                    
                                    
                                } else {
                                    Update.setDisable(false); // 2018.01.15 william 修改TC設定的按鈕邏輯
                                }  
                                
                                Check.setDisable(true);
                            }
                          
                            Lock_Check=false;

                        }
                    });
                    new Thread(CheckTask).start();
                    
                }else{
                    SA.CheckSystemResultAlert(MainStage);
                    Lock_Check=false;
                }

            }
        }); 
        
        /**********更新************/
        Update=new Button(WordMap.get("TC_Update"));
        
        if(WordMap.get("SelectedLanguage").equals("Japanese")) {
            Update.setPrefSize(140,30);
            Update.setMaxSize(140,30);
            Update.setMinSize(140,30);
        } else {
            Update.setPrefSize(80,30);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
            Update.setMaxSize(80,30);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
            Update.setMinSize(80,30);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動      
        }          

        //Update.setDisable(true);
        Update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if( Lock_Update == false ){
                    
                    Lock_Update=true;
                    
                    /*** 跳出視窗詢問 是否確定要更新 更新完成將會自動重新啟動 ***/
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle(WordMap.get("ThinClient"));//設定對話框視窗的標題列文字  , "您真的要結束程式嗎？", ButtonType.YES, ButtonType.NO
                    //alert.setHeaderText(WordMap.get("Message")+" : "+" VD is Online!! ");//設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
                    alert.setHeaderText(null);
                    alert.setX(screenCenterX);
                    alert.setY(screenCenterY);
                    alert.getDialogPane().setMinSize(450,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(450,Region.USE_PREF_SIZE);
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("TC_Update_Suggest"));//警告：更新完成時,將會自動重新開機,您是否確定要更新？
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
                    ButtonType buttonTypeCancel = new ButtonType(WordMap.get("Alert_Cancel"),ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
                    /*** button mouse + key action ***/
                    Button button_ok = (Button) alert.getDialogPane().lookupButton(buttonTypeOK);
                    Button button_cancel = (Button) alert.getDialogPane().lookupButton(buttonTypeCancel);
                    
                    button_ok.setOnMouseEntered((event01)-> {
                        button_ok.requestFocus();
                        //System.out.print(" ** button_ok setOnMouseEntered ** \n");
                    });
                    
                    button_cancel.setOnMouseEntered((event01)-> {
                        button_cancel.requestFocus();
                        //System.out.print(" ** button_ok setOnMouseEntered ** \n");
                    });
                    
                    button_ok.setOnKeyPressed((KeyEvent event01)-> {
                        if(event01.getCode() == KeyCode.ENTER){
                            button_ok.fire();
                        }
                    });
                    
                    button_cancel.setOnKeyPressed((event01)-> {
                        if(event01.getCode() == KeyCode.ENTER){
                            button_cancel.fire();
                        }
                    });
                    
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");
                    
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                    
                    Bounds mainBounds = MainStage.getScene().getRoot().getLayoutBounds();
                    alert.setX(MainStage.getX() + (mainBounds.getWidth() - 450 ) / 2);  //5.7
                    alert.setY(MainStage.getY() + (mainBounds.getHeight() - 200 ) / 2);  //5
                    
                    Optional<ButtonType> result01 = alert.showAndWait();
                    if (result01.get() == buttonTypeOK){
                        Update.setDisable(true);
                                                 
                        System_Ver_AlertMessage.setText(WordMap.get("TC_Ver_Updating") + "..."); 
                        if(WordMap.get("SelectedLanguage").equals("English")) { System_Ver_AlertMessage.setTranslateX(95); } else { System_Ver_AlertMessage.setTranslateX(80); }          
                        System_Ver_AlertMessage.setTranslateY(35);                        
                        
                        DoUpdateTask();  //  執行 update Task                       
                        
                    } else {
                        // ... user chose CANCEL or closed the dialog
                        Lock_Update=false;
                        alert.close();
                    }
                    
                }
            }
        });
        /**********離開************/ // 2018.01.15 william 修改TC設定的按鈕邏輯       
        Leave_SystemPane = new Button(WordMap.get("TC_Exit"));
        Leave_SystemPane.setPrefSize(80,30);
        Leave_SystemPane.setMaxSize(80,30);
        Leave_SystemPane.setMinSize(80,30);
        Leave_SystemPane.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {        
                GB.lock_ThinClient = false; 
                ThinClientStage.close();
            }
        }); 
        
        
        SystemButton = new HBox();
        //SystemButton.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        Update.setDisable(true); // 2018.01.15 william 修改TC設定的按鈕邏輯
        SystemButton.getChildren().addAll(Leave_SystemPane, Check, Update);  // 2018.01.15 william 修改TC設定的按鈕邏輯
        SystemButton.setAlignment(Pos.CENTER_RIGHT);
        SystemButton.setSpacing(15);
        SystemButton.setPadding(new Insets(0, 0, 0, 20));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        System_grid.add(SystemButton,2,20,2,1); // 2017.06.22 william System version check 14 -> 12 // 2018.01.15 william 修改TC設定的按鈕邏輯 1,12,2,1 -> 2,12,2,1 // 2018.04.13 william wifi實作 2,12,2,1 -> 2,20,2,1   
        
//                                    System_Ver_AlertMessage.setText(WordMap.get("TC_Server_Ver_AlertMessage")); 
//                                    System_Ver_AlertEvent.setText(WordMap.get("TC_Server_Ver_AlertEvent"));         
        

        
        if(WordMap.get("SelectedLanguage").equals("English")) {
            SystemButton.setTranslateX(-111); 
            SystemButton.setTranslateY(42); 
        } else if(WordMap.get("SelectedLanguage").equals("Japanese")) {
            SystemButton.setTranslateX(-194); 
            SystemButton.setTranslateY(42); 
        } 
        
        else {
            SystemButton.setTranslateX(-89); // 2018.01.15 william 修改TC設定的按鈕邏輯 // -135
            SystemButton.setTranslateY(42); // 2018.01.15 william 修改TC設定的按鈕邏輯 //          
        }          
        
        ChangeSystemLabelLang(System_Client, System_C_Ver, System_Current_Ver, System_S_Ver, System_Server_Ver, System_Update, System_Up_date);
        ChangeSystemButtonLang(Check, Update, Leave_SystemPane); // 2018.01.15 william 修改TC設定的按鈕邏輯
        return System_grid;
    }
    
    /******************** Tab04 : KB Type*********************/
    private Pane KeyBoardPane() throws IOException, ParseException{ 
        GridPane KB_grid = new GridPane();
//        KB_grid.setGridLinesVisible(true);
        KB_grid.setHgap(8);
        KB_grid.setVgap(10);
        KB_grid.setAlignment(Pos.TOP_CENTER);
        KB_grid.setPadding(new Insets(15, 8, 8, 8));   
        KB_grid.setId("gridpane");
                       
        Label Title_Label = new Label(WordMap.get("KeyboardSetting_Title"));
        GridPane.setHalignment(Title_Label, HPos.CENTER);      
	KB_grid.add(Title_Label, 0, 0, 4, 1);
        Title_Label.setPadding(new Insets(0, 0, 0, 0));        
        
        Label KBType_Label = new Label(WordMap.get("KeyboardType_label"));
        GridPane.setHalignment(KBType_Label, HPos.RIGHT);    
	KB_grid.add(KBType_Label, 0, 2); 
        KBType_Label.setPadding(new Insets(0, 0, 0, 0));
        
        Label Colon0_Label = new Label(":");
        GridPane.setHalignment(Colon0_Label, HPos.LEFT);
        KB_grid.add(Colon0_Label, 1, 2);
        Colon0_Label.setPadding(new Insets(0, 0, 0, 0));
        
        String[] nation = {"US", "JP"};
        String defaultSelect = "";
        
        String command = "setxkbmap -query | grep layout | awk '{print $2}'";        
        Process Get_Nation = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",command});
        try (BufferedReader output_info = new BufferedReader (new InputStreamReader(Get_Nation.getInputStream()))) {
            String line_info = output_info.readLine(); 
            while(line_info != null) {
            System.out.println(line_info);
                for (int i = 0; i < nation.length; i++) {
                   if(line_info.contains(nation[i].toLowerCase())) {
                       defaultSelect = nation[i];
                   }
                }                
                
                line_info = output_info.readLine();
            }
            output_info.close();
        }        
        
        ObservableList<String> TypeList = FXCollections.observableArrayList();
        TypeList.addAll(nation);
        ComboBox KBTypeSelection = new ComboBox(TypeList);
        KBTypeSelection.setPrefWidth(100);
        KBTypeSelection.getSelectionModel().select(defaultSelect.toUpperCase());    
        KBTypeSelection.setOnAction((Event event) -> {
            usernationSelect = KBTypeSelection.getValue().toString();             
        });        
        
                
        KB_grid.add(KBTypeSelection, 2, 2);
        
        
        Button Confirm_Btn = new Button(WordMap.get("TC_Confirm"));
        Confirm_Btn.setPrefSize(80,30);
        Confirm_Btn.setMaxSize(80,30);
        Confirm_Btn.setMinSize(80,30);     
        
        Confirm_Btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {    
                //System.out.println(usernationSelect.toLowerCase());
                try {
                    Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","setxkbmap " + usernationSelect.toLowerCase()});
                    WritekbSh("setxkbmap " + usernationSelect.toLowerCase());
                } catch (IOException ex) {
                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                }                
                
                
                GB.lock_ThinClient = false; 
                ThinClientStage.close();
            }
        });          
        
        Button Leave_Btn = new Button(WordMap.get("TC_Exit"));
        Leave_Btn.setPrefSize(80,30);
        Leave_Btn.setMaxSize(80,30);
        Leave_Btn.setMinSize(80,30);
        Leave_Btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {        
                GB.lock_ThinClient = false; 
                ThinClientStage.close();
            }
        });        
        
        HBox Button_Hbox = new HBox();
        Button_Hbox.getChildren().addAll(Leave_Btn, Confirm_Btn);  
        Button_Hbox.setAlignment(Pos.CENTER_RIGHT);
        Button_Hbox.setSpacing(15);
        Button_Hbox.setPadding(new Insets(0, 0, 0, 20));  
        if("English".equals(WordMap.get("SelectedLanguage"))){
            Button_Hbox.setTranslateX(83);
            Button_Hbox.setTranslateY(-2);        
        } else if("Japanese".equals(WordMap.get("SelectedLanguage"))){
            Button_Hbox.setTranslateX(120);
            Button_Hbox.setTranslateY(7);                
        } else {
            Button_Hbox.setTranslateX(82);
            Button_Hbox.setTranslateY(7);                
        }
        ChangeButtonLang(Leave_Btn, Confirm_Btn);
        KB_grid.add(Button_Hbox, 0, 32, 4, 1); 
        
        return KB_grid;
    }
    
    public void WritekbSh(String script) {
        try {
            File MonitorShFile = new File("/root/kb.sh");
            
            if(MonitorShFile.exists()) {
                MonitorShFile.delete();
                MonitorShFile = null;
            }
            
            try(FileWriter ShFileWriter = new FileWriter("/root/kb.sh")) {
                ShFileWriter.write("#!/bin/sh \n" + script);
                ShFileWriter.flush();
            }   
            
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","chmod +x /root/kb.sh"});
        }
        catch(Exception e) {
           // e.printStackTrace();
        }
    }    
    
    /*******讀取 Version ********/
    public void LoadVersion() throws ParseException{
        try{
            File myFile=new File("jsonfile/Version.json");
            if(myFile.exists()){

                String FilePath=myFile.getPath();
                List<String> JSONLINE=Files.readAllLines(Paths.get(FilePath));
                String JSONCode="";
                JSONCode = JSONLINE.stream().map((s) -> s).reduce(JSONCode, String::concat);
                // 參考 http://stackoverflow.com/questions/7463414/what-s-the-best-way-to-load-a-jsonobject-from-a-json-text-file
                //System.out.print(JSONCode+"\n");
                JSONParser Jparser=new JSONParser();
                JSONObject Version=(JSONObject) Jparser.parse(JSONCode);
                Version_num=Version.get("Version").toString();               
                System.out.print("Version_num-Load : "+Version_num+"\n");                
                
            }else{
                Version_num="";
                
            }
        }catch(IOException e){
           // e.printStackTrace();
        }
    }
    
    
    private void GetDHCPInetAddr() throws SocketException, UnknownHostException, IOException{
        
        /***Get ethernet name : eth0***/       
        Process ethernet_name_process = Runtime.getRuntime().exec("ip route show"); //route | grep 'default' | awk '{print $2}'
        ethernet_name_DHCP = "";
//        try (BufferedReader output_ethernet = new BufferedReader (new InputStreamReader(ethernet_name_process.getInputStream()))) {
//            String line_ethernet = output_ethernet.readLine();
//            while(line_ethernet != null){  
//
//                if ( line_ethernet.startsWith("default") ==true ){
//
//                    String[] ethernet_Line_list =line_ethernet.split(" ");
//                    ethernet_name_DHCP = ethernet_Line_list[4];              
//                    System.out.println(" ethernet_name_DHCP  : " + ethernet_name_DHCP +"\n");
//                    break;
//                }
//
//                line_ethernet = output_ethernet.readLine();
//                //System.out.println(" line_ethernet  : " + line_ethernet +"\n");
//            }
//        }

        BufferedReader output_ethernet = new BufferedReader (new InputStreamReader(ethernet_name_process.getInputStream()));
        String line_ethernet = output_ethernet.readLine();
        
        if(line_ethernet != null){  
            
            Disconnected_flag = false; // 2017.07.11 william remove internet plug & DNS Exception fix
            
//            if ( line_ethernet.startsWith("default") ==true ){
//                String[] ethernet_Line_list =line_ethernet.split(" ");
//                ethernet_name_DHCP = ethernet_Line_list[4];              
//                System.out.println(" ethernet_name_DHCP  : " + ethernet_name_DHCP +"\n");
//            }     

            // String[] ethernet_Line_list =line_ethernet.split(" ");
            String[] ethernet_Line_list;
            
            while(line_ethernet != null) {  
                
                if(line_ethernet.contains("eth") && line_ethernet.contains("src")) {
                    ethernet_Line_list = line_ethernet.split(" ");
                    
                    String ethIndex = "eth";
                    int index = -1;
                    for (int i=0;i<ethernet_Line_list.length;i++) {
                        if (ethernet_Line_list[i].contains(ethIndex)) {
                            ethernet_name_DHCP = ethernet_Line_list[i];  // 2018.04.13 william wifi實作 
                            // index = i;
                            break;
                        }
                    }                    
                }              
                
                line_ethernet = output_ethernet.readLine();
            }
            output_ethernet.close();             

            if(!ethernet_name_DHCP.isEmpty()) { // 2018.04.13 william wifi實作               
                // ethernet_name_DHCP = ethernet_Line_list[index]; // 2018.04.13 william wifi實作            
                System.out.println(" ethernet_name_DHCP  : " + ethernet_name_DHCP +"\n");
        
                String Dhcp_dhclient = "dhclient "+ ethernet_name_DHCP ;
                Process Dhcp_dhclient_00 = Runtime.getRuntime().exec(Dhcp_dhclient); //DHCP 的環境下取得IP ( dhclient eth0 ) -v：觀看執行過程。-r：釋放IP。
                Process Dhcp_dhclient_01 = Runtime.getRuntime().exec(Dhcp_dhclient); //DHCP 的環境下取得IP ( dhclient eth0 ) -v：觀看執行過程。-r：釋放IP。
                //System.out.println(" Dhcp_dhclient : " + Dhcp_dhclient +"\n");
                NetworkInterface DHCP_ni = NetworkInterface.getByName(ethernet_name_DHCP);
                
                

                // 2019.02.12 Get device mac address
                /*
                byte[] mac = DHCP_ni.getHardwareAddress();

                String mac_Str = "";
                
                for (int i = 0; i < mac.length; i++) {                   
                  mac_Str += (String.format("%02X", mac[i]));
                  mac_Str += (i < mac.length - 1) ? ":" : "";
                  System.out.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : "");
                }            
                System.out.println();
                System.out.println(" Show MAC : " + mac_Str);   
                MAC_TextField.setText(mac_Str);
                System.out.println();
                */
                
                Enumeration<InetAddress> inetAddresses =  DHCP_ni.getInetAddresses();
        
                while(inetAddresses.hasMoreElements()) {
                    InetAddress DHCP_ia = inetAddresses.nextElement();
                    if(!DHCP_ia.isLinkLocalAddress()) {
                        /***DHCP IPv4 IP***/
                        DHCP_IP = DHCP_ia.getHostAddress() ;
                        //System.out.println("DHCP_IP : " + DHCP_IP +"\n"); 
 
                        /***DHCP IPv4 SubMask***/
                        DHCP_prflen_ipv4 =DHCP_ni.getInterfaceAddresses().get(1).getNetworkPrefixLength(); 
                        //System.out.println(" DHCP_prflen_ipv4 :　"+ DHCP_prflen_ipv4 +"\n");
                
                        //ipv6                
                        //short DHCP_prflen_ipv6 =ni.getInterfaceAddresses().get(0).getNetworkPrefixLength();
               
                        /******** 將 Host Bit Length(數字00) 轉為 Subnet Mask(000.000.000.000) *********/
                        int shft = 0xffffffff<<(32-DHCP_prflen_ipv4);
                        int oct1 = ((byte) ((shft&0xff000000)>>24)) & 0xff;
                        int oct2 = ((byte) ((shft&0x00ff0000)>>16)) & 0xff;
                        int oct3 = ((byte) ((shft&0x0000ff00)>>8)) & 0xff;
                        int oct4 = ((byte) (shft&0x000000ff)) & 0xff;
                        DHCP_submask = oct1+"."+oct2+"."+oct3+"."+oct4;
                        //System.out.println(" DHCP_submask :　"+ DHCP_submask +"\n");

                        //Process result = Runtime.getRuntime().exec("ifconfig eth0"); //all IP-eth0
               
                        /***DHCP IPv4 default Gateway***/
                        Process Dhcp_gateway_show = Runtime.getRuntime().exec("ip route show"); //route | grep 'default' | awk '{print $2}'
                        // 2017.07.11 william remove internet plug & DNS Exception fix
                        try {
                            Dhcp_gateway_show.waitFor();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                        }                
                        //Process Dhcp_gateway_show = Runtime.getRuntime().exec("route | grep 'default' | awk '{print $2}'"); //Gateway 
                        try (BufferedReader output_gateway = new BufferedReader (new InputStreamReader(Dhcp_gateway_show.getInputStream()))) {
                            String line_gateway = output_gateway.readLine();
                            while(line_gateway != null) {  
                        
                                if ( line_gateway.startsWith("default") ==true ) {
                            
                                //int Gatewat_Line_length = line.split(" ").length ;
                                //System.out.println(" Gatewat_Line_length : " + Gatewat_Line_length + "\n");
                                String[] Gatewat_Line_list =line_gateway.split(" ");
                                DHCP_Gateway= Gatewat_Line_list[2];              
                                //System.out.println(" DHCP_Gateway  : " + DHCP_Gateway +"\n");
                                break;
                                }
                        
                                line_gateway = output_gateway.readLine();
                                //System.out.println(" line_gateway  : " + line_gateway +"\n");
                            }
                        }
                        /***DHCP IPv4 DNS***/  
                        Process Dhcp_DNS_show_Process = Runtime.getRuntime().exec("cat /etc/resolv.conf"); //route | grep 'default' | awk '{print $2}' //netstat -rn
                        // 2017.07.11 william remove internet plug & DNS Exception fix
                        try {
                            Dhcp_DNS_show_Process.waitFor();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                
                        //cat /var/lib/dhcp/dhclient.leases  ---->找到「option dhcp-server-identifier」就可以知道DHCP Server的IP了!
                        //cat /etc/network/interfaces   ---->查看auto 與 iface 是否為 dhcp 或 static (但現在僅顯示 loopback)
                        //cat /etc/hosts
                        //cat /etc/resolv.conf
                        try (BufferedReader output_DNS = new BufferedReader (new InputStreamReader(Dhcp_DNS_show_Process.getInputStream()))) {
                            String line_DNS = output_DNS.readLine();
                            DHCP_DNS01_Line_list=null;
                    
                            while(line_DNS != null) {
                        
                                if (line_DNS.startsWith("nameserver") == true) {
                            
                                    if(DHCP_DNS01_Line_list == null) {                                
                                        DHCP_DNS01_Line_list = line_DNS.split(" ");
                                        // 2017.07.11 william remove internet plug & DNS Exception fix
                                        if (DHCP_DNS01_Line_list.length == 2 ) {
                                            DHCP_DNS01= DHCP_DNS01_Line_list[1];
                                        } else {
                                            DHCP_DNS01 = null;
                                        }                                
                                        //DHCP_DNS02= DNS_Line_list[];
                                        //System.out.println(" DHCP_DNS01-1-  : " + DHCP_DNS01 +"\n");
                                        //System.out.println(" DHCP_DNS02-1-  : " + DHCP_DNS02 +"\n");
                                        //break;
                                
                                    } else {                                
                                        DHCP_DNS02_Line_list = line_DNS.split(" ");
                                        // 2017.07.11 william remove internet plug & DNS Exception fix
                                        if (DHCP_DNS02_Line_list.length == 2 ) {
                                            if(DHCP_DNS01_Line_list.length == 1 ) {
                                                DHCP_DNS01 = DHCP_DNS02_Line_list[1];
                                            } else {
                                                DHCP_DNS02= DHCP_DNS02_Line_list[1];
                                            }                                 
                                        } else {
                                            DHCP_DNS02 = null;
                                        }                                 
                                        //System.out.println(" DHCP_DNS02-1-  : " + DHCP_DNS02 +"\n");
                                        break;                                
                                    }
                                }
                        
                                line_DNS = output_DNS.readLine();
                                //System.out.println(" *** " + line_DNS +"\n");
                            }
                        }
                    }
                }        
            } else {
                DHCP_IP      = "";
                DHCP_submask = "";
                DHCP_Gateway = "";
                DHCP_DNS01   = "";
                DHCP_DNS02   = "";        
            }                        
        } else { 
            Disconnected_flag = true; // 2017.07.11 william remove internet plug & DNS Exception fix
            System.out.println("******************** DHCP n/a ********************\n");
        }        

    }
    
    private void writeDHCPSetting(String DIP, Short DMask, String DGateway){
        
        DHCP_IP = DIP;
        DHCP_prflen_ipv4 = DMask;
        DHCP_Gateway = DGateway;
   
        try{
            File myFile=new File("/etc/NetworkManager/system-connections/vdiClientNic");
            if(myFile.exists()){
                myFile.delete();
                myFile=null;
            }
            FileWriter DHCPWriter=new FileWriter("/etc/NetworkManager/system-connections/vdiClientNic",true);
            try (BufferedWriter bufferedDHCPWriter = new BufferedWriter(DHCPWriter)) {
                bufferedDHCPWriter.write("[802-3-ethernet]");
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("duplex=full");
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("[connection]");                             
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("id=");
                bufferedDHCPWriter.write("vdiClientNic");
                bufferedDHCPWriter.newLine();              
                bufferedDHCPWriter.write("uuid=");
                bufferedDHCPWriter.write(TC_uniqueKey.toString());
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("type=802-3-ethernet");
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("[ipv6]");
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("method=auto");
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("[ipv4]");
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("method=auto");
                bufferedDHCPWriter.newLine();
                
                // 2017.07.11 william remove internet plug & DNS Exception fix
                DNS_or_StaticIP_flag = true;
                /*
                bufferedDHCPWriter.write("address1=");
                bufferedDHCPWriter.write(DHCP_IP);
                bufferedDHCPWriter.write("/");
                bufferedDHCPWriter.write(DHCP_prflen_ipv4);                
                bufferedDHCPWriter.write(",");
                bufferedDHCPWriter.write(DHCP_Gateway);
                bufferedDHCPWriter.newLine();
                */
            }
            
        }catch(IOException e){
        }
    
    }
    
    private void writeDHCPResolv(String DDns01, String DDns02){
        
        DHCP_DNS01 = DDns01;
        DHCP_DNS02 = DDns02;       
        dns127 = "127.0.1.1" ;

        try{
            File myFile=new File("/etc/resolv.conf");
            if(myFile.exists()){
                myFile.delete();
                myFile=null;
            }
            
            FileWriter resolv_DHCPWriter=new FileWriter("/etc/resolv.conf",true);
            try (BufferedWriter resolv_bufferedDIPWriter = new BufferedWriter(resolv_DHCPWriter)) {
                
                resolv_bufferedDIPWriter.write("domain");
                resolv_bufferedDIPWriter.newLine();
                resolv_bufferedDIPWriter.write("nameserver ");
                // 2017.07.11 william remove internet plug & DNS Exception fix                
                if(DDns01!=null) {
                    resolv_bufferedDIPWriter.write(DHCP_DNS01);
                    resolv_bufferedDIPWriter.newLine();
                }
                else {
                    resolv_bufferedDIPWriter.newLine();
                }              
                resolv_bufferedDIPWriter.write("nameserver ");
                // 2017.07.11 william remove internet plug & DNS Exception fix                               
                if(DDns02!=null) {
                    resolv_bufferedDIPWriter.write(DHCP_DNS02);
                    resolv_bufferedDIPWriter.newLine(); 
                }
                else {
                    resolv_bufferedDIPWriter.newLine(); 
                }                
                resolv_bufferedDIPWriter.write("nameserver ");
                resolv_bufferedDIPWriter.write(dns127);
                resolv_bufferedDIPWriter.newLine();
                resolv_bufferedDIPWriter.write("***writeDHCPResolv*** ");
                resolv_bufferedDIPWriter.newLine();
              
            }
            
        }catch(IOException e){
        }
    
    }
    
    private void GetStaticIPInetAddr() throws SocketException, UnknownHostException, IOException{
        
        /***Get ethernet name : eth0***/       
        Process ethernet_name_process = Runtime.getRuntime().exec("ip route show"); //route | grep 'default' | awk '{print $2}'
        ethernet_name_StaticIP = "";
//        try (BufferedReader output_ethernet = new BufferedReader (new InputStreamReader(ethernet_name_process.getInputStream()))) {
//            String line_ethernet = output_ethernet.readLine();
//            while(line_ethernet != null){  
//
//                if ( line_ethernet.startsWith("default") ==true ){
//
//                    String[] ethernet_Line_list =line_ethernet.split(" ");
//                    ethernet_name_StaticIP = ethernet_Line_list[4];              
//                    System.out.println(" ethernet_name_StaticIP  : " + ethernet_name_StaticIP +"\n");
//                    break;
//                }
//
//                line_ethernet = output_ethernet.readLine();
//                //System.out.println(" line_ethernet  : " + line_ethernet +"\n");
//            }
//        }
        // 2017.07.11 william remove internet plug & DNS Exception fix

        BufferedReader output_ethernet = new BufferedReader (new InputStreamReader(ethernet_name_process.getInputStream()));
        String line_ethernet = output_ethernet.readLine();
        if(line_ethernet != null) {
            
            Disconnected_flag = false; // 2017.07.11 william remove internet plug & DNS Exception fix
            
//            if ( line_ethernet.startsWith("default") ==true ){
//                String[] ethernet_Line_list =line_ethernet.split(" ");
//                ethernet_name_StaticIP = ethernet_Line_list[4];              
//                System.out.println(" ethernet_name_StaticIP  : " + ethernet_name_StaticIP +"\n");
//                }

            // String[] ethernet_Line_list =line_ethernet.split(" ");
            String[] ethernet_Line_list;
            
            while(line_ethernet != null) {  
                
                if(line_ethernet.contains("eth") && line_ethernet.contains("src")) {
                ethernet_Line_list = line_ethernet.split(" ");                        
                
            String ethIndex = "eth";       
            int index = -1;
            for (int i=0;i<ethernet_Line_list.length;i++) {
                if (ethernet_Line_list[i].contains(ethIndex)) {
                    ethernet_name_StaticIP = ethernet_Line_list[i]; // 2018.04.13 william wifi實作  
                    // index = i;
                    break;
                }
//            if (ethernet_Line_list[i].equals(ethIndex)) {
//                index = i;
//                break;
//            }           
            }                  
                }              
                
                line_ethernet = output_ethernet.readLine();
            }
            output_ethernet.close();             


            
            if(!ethernet_name_StaticIP.isEmpty()) { // 2018.04.13 william wifi實作               
                // ethernet_name_StaticIP = ethernet_Line_list[index];  
                System.out.println(" ethernet_name_StaticIP  : " + ethernet_name_StaticIP +"\n");

                NetworkInterface ni = NetworkInterface.getByName(ethernet_name_StaticIP);
                
                // 2019.02.12 Get device mac address
                /*
                byte[] mac = ni.getHardwareAddress();

                String mac_Str = "";
                
                for (int i = 0; i < mac.length; i++) {                   
                  mac_Str += (String.format("%02X", mac[i]));
                  mac_Str += (i < mac.length - 1) ? ":" : "";
                  System.out.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : "");
                }            
                System.out.println();
                System.out.println(" Show MAC : " + mac_Str);            
                MAC_TextField.setText(mac_Str);
                System.out.println();
                */
                Enumeration<InetAddress> inetAddresses =  ni.getInetAddresses(); 
        
                //System.out.println(" GetStaticIPInetAddr-inetAddresses :　"+ inetAddresses +"\n");
        
                while(inetAddresses.hasMoreElements()) {
                    InetAddress ia = inetAddresses.nextElement();
                    if(!ia.isLinkLocalAddress()) {
                        /***StaticIP IPv4 IP***/
                        StaticIP_IP = ia.getHostAddress() ;
                        //System.out.println("StaticIP IP : " + StaticIP_IP +"\n"); 
 
                        /***StaticIP IPv4 SubMask***/
                        StaticIP_prflen_ipv4 =ni.getInterfaceAddresses().get(1).getNetworkPrefixLength(); 
                        //System.out.println("StaticIP ipv4 prflen :　"+ StaticIP_prflen_ipv4 +"\n");
                
                        //ipv6                
                        //short DHCP_prflen_ipv6 =ni.getInterfaceAddresses().get(0).getNetworkPrefixLength();
               
                        /******** 將 Host Bit Length(數字00) 轉為 Subnet Mask(000.000.000.000) *********/
                        int shft = 0xffffffff<<(32-StaticIP_prflen_ipv4);
                        int oct1 = ((byte) ((shft&0xff000000)>>24)) & 0xff;
                        int oct2 = ((byte) ((shft&0x00ff0000)>>16)) & 0xff;
                        int oct3 = ((byte) ((shft&0x0000ff00)>>8)) & 0xff;
                        int oct4 = ((byte) (shft&0x000000ff)) & 0xff;
                        StaticIP_submask = oct1+"."+oct2+"."+oct3+"."+oct4;
                        //System.out.println(" StaticIP_submask :　"+ StaticIP_submask +"\n");

                        //Process result = Runtime.getRuntime().exec("ifconfig eth0"); //all IP-eth0
               
                        /***StaticIP IPv4 default Gateway***/
                        Process StaticIP_gateway_show = Runtime.getRuntime().exec("ip route show"); //route | grep 'default' | awk '{print $2}'
                        // 2017.07.11 william remove internet plug & DNS Exception fix
                        try {
                            StaticIP_gateway_show.waitFor();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                        }                  
                        try (BufferedReader output_gateway = new BufferedReader (new InputStreamReader(StaticIP_gateway_show.getInputStream()))) {
                            String line_gateway = output_gateway.readLine();
                            while(line_gateway != null) {  
                        
                                if ( line_gateway.startsWith("default") ==true ) {
                            
                                    //int Gatewat_Line_length = line.split(" ").length ;
                                    //System.out.println(" Gatewat_Line_length : " + Gatewat_Line_length + "\n");
                                    String[] Gatewat_Line_list =line_gateway.split(" ");
                                    StaticIP_Gateway= Gatewat_Line_list[2];              
                                    //System.out.println(" StaticIP_Gateway  : " + StaticIP_Gateway +"\n");
                                    break;
                                }
                        
                                line_gateway = output_gateway.readLine();
                                //System.out.println(" line_gateway  : " + line_gateway +"\n");
                            }
                        }
                
                        /***StaticIP IPv4 DNS***/  
                        Process StaticIP_DNS_show_Process = Runtime.getRuntime().exec("cat /etc/resolv.conf"); //route | grep 'default' | awk '{print $2}' //netstat -rn
                        // 2017.07.11 william remove internet plug & DNS Exception fix
                        try {
                            StaticIP_DNS_show_Process.waitFor();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                
                        try (   //cat /var/lib/dhcp/dhclient.leases  ---->找到「option dhcp-server-identifier」就可以知道DHCP Server的IP了!
                                //cat /etc/network/interfaces   ---->查看auto 與 iface 是否為 dhcp 或 static (但現在僅顯示 loopback)
                                //cat /etc/hosts
                                //cat /etc/resolv.conf
                            BufferedReader StaticIP_output_DNS = new BufferedReader (new InputStreamReader(StaticIP_DNS_show_Process.getInputStream()))) {
                            String StaticIP_line_DNS = StaticIP_output_DNS.readLine();
                            StaticIP_DNS01_Line_list=null;
                                        
                            while(StaticIP_line_DNS != null) {
                        
                                //System.out.println(" StaticIP line DNS  : " + StaticIP_line_DNS +"\n");
                        
                                if (StaticIP_line_DNS.startsWith("nameserver") == true) {
                            
                                    if(StaticIP_DNS01_Line_list == null) {
                                
                                        StaticIP_DNS01_Line_list = StaticIP_line_DNS.split(" ");
                                        // 2017.07.11 william remove internet plug & DNS Exception fix
                                        if (StaticIP_DNS01_Line_list.length == 2 ) {
                                            StaticIP_DNS01= StaticIP_DNS01_Line_list[1];
                                        } else {
                                            StaticIP_DNS01 = null;
                                        }
//                                System.out.println(" StaticIP_DNS01-1-  : " + StaticIP_DNS01 +"\n");
              
                                    } else {                               
                                        StaticIP_DNS02_Line_list = StaticIP_line_DNS.split(" ");
                                        // 2017.07.11 william remove internet plug & DNS Exception fix
                                        if (StaticIP_DNS02_Line_list.length == 2 ) {
                                            if(StaticIP_DNS01_Line_list.length == 1) {
                                                StaticIP_DNS01 = StaticIP_DNS02_Line_list[1];
                                            } else {
                                                StaticIP_DNS02= StaticIP_DNS02_Line_list[1];
                                            }
                                        } else {
                                            StaticIP_DNS02 = null;
                                        }
//                                System.out.println(" StaticIP_DNS02-1-  : " + StaticIP_DNS02 +"\n");
                                        break;                                
                                    }
                                }
                        
                                StaticIP_line_DNS = StaticIP_output_DNS.readLine();
                                //System.out.println(" StaticIP " + StaticIP_line_DNS +"\n");
                            }
                        }                
                    }
                }        
            } else {
                StaticIP_IP      = "";
                StaticIP_submask = "";
                StaticIP_Gateway = "";
                StaticIP_DNS01   = "";
                StaticIP_DNS02   = "";        
            }                 
        } else { 
            Disconnected_flag = true; // 2017.07.11 william remove internet plug & DNS Exception fix

            System.out.println("******************** StaticIP n/a ********************\n");
        }
        
    }
    
    private void writeStaticIPSetting(String SIP, String SMask, String SGateway){
        
        StaticIP_IP_Setting = SIP;
        StaticIP_Final_mask_cidr = SMask;
        StaticIP_Gateway_Setting = SGateway;
        
        try{
            File myFile=new File("/etc/NetworkManager/system-connections/vdiClientNic");
            if(myFile.exists()){
                myFile.delete();
                myFile=null;
            }
            FileWriter StaticIPWriter=new FileWriter("/etc/NetworkManager/system-connections/vdiClientNic",true);
            try (BufferedWriter bufferedSIPWriter = new BufferedWriter(StaticIPWriter)) {
                bufferedSIPWriter.write("[802-3-ethernet]");
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.write("duplex=full");
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.write("[connection]");                             
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.write("id=");
                bufferedSIPWriter.write("vdiClientNic");
                bufferedSIPWriter.newLine();              
                bufferedSIPWriter.write("uuid=");
                bufferedSIPWriter.write(TC_uniqueKey.toString());
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.write("type=802-3-ethernet");
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.write("[ipv6]");
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.write("method=auto");
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.write("[ipv4]");
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.write("method=manual");
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.write("address1=");
                bufferedSIPWriter.write(StaticIP_IP_Setting);
                bufferedSIPWriter.write("/");
                bufferedSIPWriter.write(StaticIP_Final_mask_cidr);                
                bufferedSIPWriter.write(",");
                // 2017.07.11 william remove internet plug & DNS Exception fix                               
                if(SGateway!=null) {
                    bufferedSIPWriter.write(StaticIP_Gateway_Setting);
                    bufferedSIPWriter.newLine();
                }
                else {
                    bufferedSIPWriter.newLine();
                } 
                // 2017.07.11 william remove internet plug & DNS Exception fix
                DNS_or_StaticIP_flag = false;                
            }
            
        }catch(IOException e){
        }
    
    }
    
    private void writeStaticIPResolv(String SDns01, String SDns02){
        
        //StaticIP_DNS01_Setting = SDns01;
        //StaticIP_DNS02_Setting = SDns02;
        
        try{
            File resolv_myFile=new File("/etc/resolv.conf");
            if(resolv_myFile.exists()){
                resolv_myFile.delete();
                resolv_myFile=null;
            }
            FileWriter resolv_StaticIPWriter=new FileWriter("/etc/resolv.conf",true);
            try (BufferedWriter resolv_bufferedSIPWriter = new BufferedWriter(resolv_StaticIPWriter)) {
                
                resolv_bufferedSIPWriter.write("staticIP");
                resolv_bufferedSIPWriter.newLine();
                resolv_bufferedSIPWriter.write("nameserver ");
                // 2017.07.11 william remove internet plug & DNS Exception fix                               
                if(SDns01!=null) {
                    resolv_bufferedSIPWriter.write(SDns01);
                    resolv_bufferedSIPWriter.newLine();
                }
                else {
                    resolv_bufferedSIPWriter.newLine();
                }              
                resolv_bufferedSIPWriter.write("nameserver ");                
                // 2017.07.11 william remove internet plug & DNS Exception fix                               
                if(SDns02!=null) {
                    resolv_bufferedSIPWriter.write(SDns02);
                    resolv_bufferedSIPWriter.newLine();
                }
                else {
                    resolv_bufferedSIPWriter.newLine();
                }                
                resolv_bufferedSIPWriter.write("***writeStaticIPResolv*** ");
                resolv_bufferedSIPWriter.newLine();
               
            }
            
        }catch(IOException e){
        }
    
    }    
    
    public int convertNetmaskToCIDR (InetAddress netmask){
        StaticIP_mask_cidr = netmask;

        byte[] netmaskBytes = netmask.getAddress();
        //int cidr = 0;
        StaticIP_prflen_ipv4 = 0;
        boolean zero = false;
        for(byte b : netmaskBytes){
            int mask = 0x80;

            for(int i = 0; i < 8; i++){
                int result = b & mask;
                if(result == 0){
                    zero = true;
                }else if(zero){
                    //throw new IllegalArgumentException("Invalid netmask.");
                } else {
                    StaticIP_prflen_ipv4++;
                }
                mask >>>= 1;
            }
        }
        return StaticIP_prflen_ipv4;
    }
    
    
    public void CheckInetAddr() throws SocketException, UnknownHostException, IOException{

        Process result = Runtime.getRuntime().exec("ifconfig eth0"); //all IP-eth0
        try (BufferedReader checkInet = new BufferedReader (new InputStreamReader(result.getInputStream()))) {
            String line_check = checkInet.readLine();
            while(line_check != null){
                
                if ( line_check.matches("inet addr")==true ){
                    
                    break;
                    
                }else{ 
                    
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(520,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(520,Region.USE_PREF_SIZE);
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Lock")+"\n"
                            +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK);
                    alert.showAndWait();
                    
                    
                }
                
                line_check = checkInet.readLine();
                //System.out.println(" @@@ : " + line_check +"\n");
                
            }
        }
                               
    }
    
    
    
    
    /****變更Label字型****字型:中文-標楷體,英文:Times New Roman****/
    private void ChangeLabelLang(Label title, Tab tab01, Tab tab02, Tab tab03, Tab tab04, Tab tab05){ // 2018.04.13 william wifi實作
        //Label "Password"
        if("English".equals(WordMap.get("SelectedLanguage"))){                 
            title.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-font-size: 16pt; -fx-text-fill: white;");
            
            tab01.setStyle("-fx-font-family:'Ubuntu';");
            tab02.setStyle("-fx-font-family:'Ubuntu';");
            tab03.setStyle("-fx-font-family:'Ubuntu';");
            tab04.setStyle("-fx-font-family:'Ubuntu';");
            tab05.setStyle("-fx-font-family:'Ubuntu';");
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){           
            title.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 900; -fx-font-size: 16pt; -fx-text-fill: white;"); 
            
            tab01.setStyle("-fx-font-family:'Droid Sans Fallback';");
            tab02.setStyle("-fx-font-family:'Droid Sans Fallback';");
            tab03.setStyle("-fx-font-family:'Droid Sans Fallback';");
            tab04.setStyle("-fx-font-family:'Droid Sans Fallback';");
            tab05.setStyle("-fx-font-family:'Droid Sans Fallback';");
        }     
     }
    
    
   /****變更Label字型****字型:中文-標楷體,英文:Times New Roman****/
     private void ChangeIPv4LabelLang(Label title01, Label title02, Label lab01, Label lab02, Label lab03, Label col01, Label col02, Label col03){
        //Label "Password"
        if("English".equals(WordMap.get("SelectedLanguage"))){
            title01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-font-size: 13pt;");  // Ubuntu ; Liberation Serif
            title02.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-font-size: 13pt;");
            
            lab01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 600; -fx-font-size: 12pt;");
            lab02.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 600; -fx-font-size: 12pt;");
            lab03.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 600; -fx-font-size: 12pt;");    
            
            //冒號":"-win7
            col01.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 14px; -fx-font-weight: 700;");
            col02.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 14px; -fx-font-weight: 700;");
            col03.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 14px; -fx-font-weight: 700;");
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/      
            //win7以上僅能用英文名稱
            title01.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 900; -fx-font-size: 13pt;");
            //title01.setStyle("-fx-font-weight: 900; -fx-font-size: 13pt;");
            title02.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 900; -fx-font-size: 13pt;");    
            //title01.setFont(Font.font("Droid Sans", FontWeight.BLACK, 13));
            //title02.setFont(Font.font("Droid Sans", FontWeight.BOLD, 13));
            
            lab01.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 600; -fx-font-size: 12pt;");
            lab02.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 600; -fx-font-size: 12pt;");
            lab03.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 600; -fx-font-size: 12pt;");        

            //冒號":"-win7
            col01.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 14px; -fx-font-weight: 700;");
            col02.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 14px; -fx-font-weight: 700;");
            col03.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 14px; -fx-font-weight: 700;");
            //col04.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-size: 14px; -fx-font-weight: 900;");
            //col05.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-size: 14px; -fx-font-weight: 900;");
        }
       
     }
     
      /****變更button字型****字型:中文-標楷體,英文:Times New Roman****/
      private void ChangeIPv4ButtonLang(RadioButton rbut01, RadioButton rbut02, Button but01, Button but02){
        //Button "確定" "取消"
        if("English".equals(WordMap.get("SelectedLanguage"))){
            
            rbut01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-font-size: 14pt;");
            rbut02.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-font-size: 14pt;");
            but01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");
            but02.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");               
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/
          
            rbut01.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900; -fx-font-size: 14pt;");
            rbut02.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900; -fx-font-size: 14pt;");
            but01.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;");
            but02.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;");          
        }
        
     }
      
      /****變更Label字型****字型:中文-標楷體,英文:Times New Roman****/
    private void ChangeMonitor_1_LabelLang(Label title01, Label lab01, Label lab_01, RadioButton rbut01, RadioButton rbut02, RadioButton rbut03, RadioButton rbut04) {    
        //Label "Password"
        if("English".equals(WordMap.get("SelectedLanguage"))){            
            title01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-font-size: 15pt;");
            
            //HDMI 1
            lab01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 700; -fx-font-size: 13pt;");
            lab_01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 700; -fx-font-size: 13pt;");

            rbut01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-font-size: 14pt;");
            rbut02.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-font-size: 14pt;");  
            rbut03.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-font-size: 14pt;");
            rbut04.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-font-size: 14pt;");            
            //HDMI 2
            //lab03.setStyle("-fx-font-family:'Liberation Serif';-fx-font-weight: 700; -fx-font-size: 12pt;");
            //lab04.setStyle("-fx-font-family:'Liberation Serif';-fx-font-weight: 700; -fx-font-size: 12pt;");            
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/      
           
            title01.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 900; -fx-font-size: 15pt;");
            //HDMI 1
            lab01.setStyle("-fx-font-family:'Ubuntu'; -fx-font-weight: 700; -fx-font-size: 13pt;");
            lab_01.setStyle("-fx-font-family:'Ubuntu'; -fx-font-weight: 700; -fx-font-size: 13pt;");

            rbut01.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900; -fx-font-size: 14pt;");
            rbut02.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900; -fx-font-size: 14pt;");
            rbut03.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900; -fx-font-size: 14pt;");
            rbut04.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900; -fx-font-size: 14pt;");            
            //HDMI 2
            //lab03.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 700; -fx-font-size: 12pt;");
            //lab04.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 700; -fx-font-size: 12pt;");
        }
       
     }  
      
      /****變更Label字型****字型:中文-標楷體,英文:Times New Roman****/
    //private void ChangeMonitorLabelLang(Label title01, Label lab01, Label lab02){
    private void ChangeMonitor_2_LabelLang(Label title01, Label lab01, Label lab02, Label lab03, Label lab04){    
        //Label "Password"
        if("English".equals(WordMap.get("SelectedLanguage"))){            
            title01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-font-size: 15pt;");
            
            //HDMI 1
            lab01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 700; -fx-font-size: 13pt;");
            lab02.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 600; -fx-font-size: 13pt;");
            //HDMI 2
            lab03.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 700; -fx-font-size: 13pt;");
            lab04.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 600; -fx-font-size: 13pt;");
            
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/      
           
            title01.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 900; -fx-font-size: 15pt;");
            //HDMI 1
            lab01.setStyle("-fx-font-family:'Ubuntu'; -fx-font-weight: 700; -fx-font-size: 13pt;");
            lab02.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 900; -fx-font-size: 13pt;");
            //HDMI 2
            lab03.setStyle("-fx-font-family:'Ubuntu'; -fx-font-weight: 700; -fx-font-size: 13pt;");
            lab04.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 900; -fx-font-size: 13pt;");

        }
       
     }
     
       /****變更Label字型****字型:中文-標楷體,英文:Times New Roman****/
     private void ChangeSystemLabelLang(Label title01, Label lab01, Label lab02, Label lab03, Label lab04, Label lab05, Label lab06){
        //Label "Password"
        if("English".equals(WordMap.get("SelectedLanguage"))){            
            title01.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900; -fx-font-size: 14pt;");
            
            lab01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 700; -fx-font-size: 12pt;");
            lab02.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 600; -fx-font-size: 12pt;");
            lab03.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 700; -fx-font-size: 12pt;");
            lab04.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 600; -fx-font-size: 12pt;");
            lab05.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 700; -fx-font-size: 12pt;");
            lab06.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 600; -fx-font-size: 12pt;");
            
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/       
            title01.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 900; -fx-font-size: 15pt;");
          
            lab01.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 700; -fx-font-size: 12pt;");
            lab02.setStyle("-fx-font-family:'Ubuntu'; -fx-font-weight: 600; -fx-font-size: 12pt;");
            lab03.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 700; -fx-font-size: 12pt;");
            lab04.setStyle("-fx-font-family:'Ubuntu'; -fx-font-weight: 600; -fx-font-size: 12pt;");
            lab05.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 700; -fx-font-size: 12pt;");
            lab06.setStyle("-fx-font-family:'Ubuntu'; -fx-font-weight: 600; -fx-font-size: 12pt;");

        }
       
     }
     
       /****變更button字型****字型:中文-標楷體,英文:Times New Roman****/
    private void ChangeButtonLang(Button but01, Button but02){
        //Button "確定" "取消"
        if("English".equals(WordMap.get("SelectedLanguage"))){
            but01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");
            but02.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");          
       
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/
            //win7以上僅能用英文名稱
            but01.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;");
            but02.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;");
            
        }
        
     } 
     
       /****變更button字型****字型:中文-標楷體,英文:Times New Roman****/
    private void ChangeSystemButtonLang(Button but01, Button but02, Button but03){
        //Button "確定" "取消"
        if("English".equals(WordMap.get("SelectedLanguage"))){
            but01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");
            but02.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");          
            but03.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");
       
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/
            //win7以上僅能用英文名稱
            but01.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;");
            but02.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;");
            but03.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;");
        }
        
     }
    
      /****變更button字型****字型:中文-標楷體,英文:Times New Roman****/
    private void ChangeButtonLang(Button but01, Button but02, Button but03){
        //Button "確定" "取消"
        if("English".equals(WordMap.get("SelectedLanguage"))){
            but01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 700;");
            but02.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 700;");
            but03.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 700;");
       
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/
            //win7以上僅能用英文名稱
            but01.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;");
            but02.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;");
            but03.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;");
   
        }
        
    }
    
    private void chancelAction(){
        
        if(DNS_or_StaticIP == true){
         
                //System.out.println("------- DNS_or_StaticIP == true ------\n"); 
                rb1_DHCP.setSelected(true);
                rb1_DHCP.requestFocus();
                
                //writeDHCPSetting(DHCP_IP, DHCP_prflen_ipv4, DHCP_Gateway);
                writeDHCPResolv(DHCP_DNS01, DHCP_DNS02);

                IPv4_IP_TextField.setText(DHCP_IP);
                IPv4_Mask_TextField.setText(DHCP_submask);
                IPv4_Getway_TextField.setText(DHCP_Gateway);
                IPv4_DNS1_TextField.setText(DHCP_DNS01);
                IPv4_DNS2_TextField.setText(DHCP_DNS02);

                IPv4_IP_TextField.setEditable(false);
                IPv4_Mask_TextField.setEditable(false);
                IPv4_Getway_TextField.setEditable(false);
                IPv4_DNS1_TextField.setEditable(false);
                IPv4_DNS2_TextField.setEditable(false);


        }else{

            //System.out.println("------- DNS_or_StaticIP == false ------\n");
            rb2_StaticIP.setSelected(true);
            rb2_StaticIP.requestFocus();

            //writeStaticIPSetting(StaticIP_IP, StaticIP_Final_mask_cidr, StaticIP_Gateway);
            writeStaticIPResolv(StaticIP_DNS01, StaticIP_DNS02);

            IPv4_IP_TextField.setText(StaticIP_IP);
            IPv4_Mask_TextField.setText(StaticIP_submask);
            IPv4_Getway_TextField.setText(StaticIP_Gateway);
            IPv4_DNS1_TextField.setText(StaticIP_DNS01);
            IPv4_DNS2_TextField.setText(StaticIP_DNS02);

            IPv4_IP_TextField.setEditable(true);
            IPv4_Mask_TextField.setEditable(true);
            IPv4_Getway_TextField.setEditable(true);
            IPv4_DNS1_TextField.setEditable(true);
            IPv4_DNS2_TextField.setEditable(true);

        } 
        
        if(tab02.getContent()!=null){  
            
            primary_ResolutionSelection.getSelectionModel().select(Display_primary_current_resolution);
            if( !Display02_name.equals("0")){
                
                second_ResolutionSelection.getSelectionModel().select(Display_second_current_resolution);
                
            }
        }
        
    }
    
    private void DisplayChangeAction() throws IOException, ParseException{       
        
        if(tab02.getContent()!=null){  
        
            if( HDMI01_Resolution_selectItem!=null && !HDMI01_Resolution_selectItem.equals(Display01_current_data) ){

                //xrandr --output VIRTUAL1 --off --output VGA1 --mode 1920x1080 --pos 0x0 --rotate normal
                String HDMI01_setting_00 = "xrandr --output "+Display01_name+" --mode "+ HDMI01_Resolution_selectItem + " --pos 0x0" ;
                System.out.println(" HDMI01_setting_00 : "+ HDMI01_setting_00 +"\n");
                HDMI01_setting_params = new String();
                HDMI01_setting_params = HDMI01_setting_00;
                HDMI01_setting_process =Runtime.getRuntime().exec(HDMI01_setting_params);

                Stage changeS = new Stage();
                Platform.runLater( () -> {
                    try {

                        MainStage.close();
                        //System.out.println("---01--- primaryStage.close(); ------\n");
                        new AcroRed_VDI_Viewer().start( changeS );
                        //System.out.println("---01--- AcroRed_VDI_Viewer().start ------\n");
                        MainStage.close();


                    } catch (ParseException | IOException ex) {
                        Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } );

                Platform.runLater( () -> {
                    try {

                        changeS.close();
                        new AcroRed_VDI_Viewer().start( new Stage() );

                    } catch (ParseException | IOException ex) {
                        Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } );
            }

            if( HDMI02_Resolution_selectItem!=null && !HDMI02_Resolution_selectItem.equals(Display02_current_data) ){

                //xrandr --output VIRTUAL1 --off --output VGA1 --mode 1920x1080 --pos 0x0 --rotate normal
                String HDMI02_setting_00 = "xrandr --output "+Display02_name+" --mode "+ HDMI02_Resolution_selectItem + " --pos 0x0" ;
                System.out.println(" HDMI02_Resolution_selectItem : "+ HDMI02_Resolution_selectItem +"\n");
                HDMI02_setting_params = new String();
                HDMI02_setting_params = HDMI02_setting_00;
                HDMI02_setting_process =Runtime.getRuntime().exec(HDMI02_setting_params);

                Stage changeSS = new Stage();
                Platform.runLater( () -> {
                    try {

                        MainStage.close();
                        //System.out.println("---01--- primaryStage.close(); ------\n");
                        new AcroRed_VDI_Viewer().start( changeSS );
                        //System.out.println("---01--- AcroRed_VDI_Viewer().start ------\n");
                        MainStage.close();


                    } catch (ParseException | IOException ex) {
                        Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } );

                Platform.runLater( () -> {
                    try {

                        changeSS.close();
                        new AcroRed_VDI_Viewer().start( new Stage() );

                    } catch (ParseException | IOException ex) {
                        Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } );
            }

            DisplayPane();
        
        }
        
    }
    
    private void Display_01_ChangePathButtonAction() throws IOException{    
        
        //xrandr --output VIRTUAL1 --off --output VGA1 --mode 1920x1080 --pos 0x0 --rotate normal
//        System.out.println(" ----- Display_01_ChangePathButtonAction ------ \n");
//        String HDMI01_setting_00 = "xrandr --output "+Display01_name+" --mode "+ Display01_current_data + " --pos 0x0" ;
//        System.out.println(" Display_01_ChangePathButtonAction : "+ Display01_current_data +"\n");
//        HDMI01_setting_params = new String();
//        HDMI01_setting_params = HDMI01_setting_00;
//        HDMI01_setting_process =Runtime.getRuntime().exec(HDMI01_setting_params);

        Stage changeS = new Stage();
        Platform.runLater( () -> {
            try {

                MainStage.close();
                //System.out.println("---01--- primaryStage.close(); ------\n");
                new AcroRed_VDI_Viewer().start( changeS );
                //System.out.println("---01--- AcroRed_VDI_Viewer().start ------\n");
                MainStage.close();


            } catch (ParseException | IOException ex) {
                Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } );

        Platform.runLater( () -> {
            try {

                changeS.close();
                new AcroRed_VDI_Viewer().start( new Stage() );

            } catch (ParseException | IOException ex) {
                Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } );
        
    }
    
    private void Display_02_ChangePathButtonAction() throws IOException{    
        
        //xrandr --output VIRTUAL1 --off --output VGA1 --mode 1920x1080 --pos 0x0 --rotate normal
//        System.out.println(" ----- Display_02_ChangePathButtonAction ------ \n");
//        String HDMI02_setting_00 = "xrandr --output "+Display02_name+" --mode "+ Display02_current_data + " --pos 0x0" ;
//        System.out.println(" Display_02_ChangePathButtonAction : "+ Display02_current_data +"\n");
//        HDMI02_setting_params = new String();
//        HDMI02_setting_params = HDMI02_setting_00;
//        HDMI02_setting_process =Runtime.getRuntime().exec(HDMI02_setting_params);

        Stage changeSS = new Stage();
        Platform.runLater( () -> {
            try {

                MainStage.close();
                //System.out.println("---01--- primaryStage.close(); ------\n");
                new AcroRed_VDI_Viewer().start( changeSS );
                //System.out.println("---01--- AcroRed_VDI_Viewer().start ------\n");
                MainStage.close();


            } catch (ParseException | IOException ex) {
                Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } );

        Platform.runLater( () -> {
            try {

                changeSS.close();
                new AcroRed_VDI_Viewer().start( new Stage() );

            } catch (ParseException | IOException ex) {
                Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } );
    }
    
    private void SIP_turn_Dhcp_alert() throws ParseException{
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(WordMap.get("ThinClient"));//設定對話框視窗的標題列文字  , "您真的要結束程式嗎？", ButtonType.YES, ButtonType.NO
        alert.setHeaderText(null);//設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        alert.setContentText(WordMap.get("Message")+" : "+WordMap.get("Comfirm_RebootMassage"));//VD 已上線,是否還要連線,若連線,則前一個連線會自動斷線.

        ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType(WordMap.get("Alert_Cancel"),ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
        /*** button mouse + key action ***/
        Button button_ok = (Button) alert.getDialogPane().lookupButton(buttonTypeOK);
        Button button_cancel = (Button) alert.getDialogPane().lookupButton(buttonTypeCancel);

        button_ok.setOnMouseEntered((event)-> {
            button_ok.requestFocus();
            //System.out.print(" ** button_ok setOnMouseEntered ** \n");        
        });

        button_cancel.setOnMouseEntered((event)-> {
            button_cancel.requestFocus();
            //System.out.print(" ** button_ok setOnMouseEntered ** \n");        
        });

        button_ok.setOnKeyPressed((event)-> {           
            if(event.getCode() == KeyCode.ENTER){
                button_ok.fire();              
            }        
        });

        button_cancel.setOnKeyPressed((event)-> {           
            if(event.getCode() == KeyCode.ENTER){
                button_cancel.fire();              
            }        
        });
        /*** alert style ***/
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("myDialogs.css");
        dialogPane.getStyleClass().add("myDialog");

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(MainStage.getScene().getWindow());  //有沒有這一行很重要，決定這兩個視窗是否有從屬關係

        Optional<ButtonType> result01 = alert.showAndWait();
        if (result01.get() == buttonTypeOK){

            try {
                GetDHCPInetAddr();
                writeDHCPSetting(DHCP_IP, DHCP_prflen_ipv4, DHCP_Gateway);
                writeDHCPResolv(DHCP_DNS01, DHCP_DNS02);
                System.out.println("------ rb1_DHCP Action ------\n");                        
                DisplayChangeAction();
                writeDHCPSetting(DHCP_IP, DHCP_prflen_ipv4, DHCP_Gateway);
                writeDHCPResolv(DHCP_DNS01, DHCP_DNS02);
                
                String IPSetting_Reboot_params = new String();
                IPSetting_Reboot_params="chmod 600 /etc/NetworkManager/system-connections/vdiClientNic";
                Process IPSetting_Reboot_process =Runtime.getRuntime().exec(IPSetting_Reboot_params);                

                // 2017.07.11 william remove internet plug & DNS Exception fix
                if(group.getSelectedToggle()== rb1_DHCP){  
                    DNS_or_StaticIP_flag = true;
                    
                }
                if(group.getSelectedToggle()== rb2_StaticIP){                
                    DNS_or_StaticIP_flag = false;
                    WriteStaticIPStatusChange();
                }
                
                WriteLastIPStatusChange();
                
                 /***Reboot***/                    
                String Reboot_params = new String();
                Reboot_params="reboot";
                Process Reboot_process =Runtime.getRuntime().exec(Reboot_params);

            } catch (IOException ex) {
                Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
            }             
            ThinClientStage.close();

        } else {
            //chancelAction();
            alert.close();
        }
    }
    
    private void SIP_changeText_alert() throws ParseException{
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(WordMap.get("ThinClient"));//設定對話框視窗的標題列文字  , "您真的要結束程式嗎？", ButtonType.YES, ButtonType.NO
        alert.setHeaderText(null);//設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        alert.setContentText(WordMap.get("Message")+" : "+WordMap.get("Comfirm_RebootMassage"));//VD 已上線,是否還要連線,若連線,則前一個連線會自動斷線.
        ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType(WordMap.get("Alert_Cancel"),ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
        /*** button mouse + key action ***/
        Button button_ok = (Button) alert.getDialogPane().lookupButton(buttonTypeOK);
        Button button_cancel = (Button) alert.getDialogPane().lookupButton(buttonTypeCancel);

        button_ok.setOnMouseEntered((event)-> {
            button_ok.requestFocus();
            //System.out.print(" ** button_ok setOnMouseEntered ** \n");        
        });

        button_cancel.setOnMouseEntered((event)-> {
            button_cancel.requestFocus();
            //System.out.print(" ** button_ok setOnMouseEntered ** \n");        
        });

        button_ok.setOnKeyPressed((event)-> {           
            if(event.getCode() == KeyCode.ENTER){
                button_ok.fire();              
            }        
        });

        button_cancel.setOnKeyPressed((event)-> {           
            if(event.getCode() == KeyCode.ENTER){
                button_cancel.fire();              
            }        
        });
        /*** alert style ***/
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("myDialogs.css");
        dialogPane.getStyleClass().add("myDialog");

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(MainStage.getScene().getWindow());  //有沒有這一行很重要，決定這兩個視窗是否有從屬關係                

        Optional<ButtonType> result01 = alert.showAndWait();

        if (result01.get() == buttonTypeOK){

            try {

                StaticIP_IP_Setting = IPv4_IP_TextField.getText();                        
                //將submask IP讀取後, 轉為InetAddress （為了把IP轉成cidr）
                StaticIP_submask_Setting = IPv4_Mask_TextField.getText();
                StaticIP_mask_cidr=InetAddress.getByName(StaticIP_submask_Setting);
                //寫入檔案, 必須要String, 若是int會是亂碼, 這裡將-Mask IP- 轉-Mask Cidr（int）-再將int轉成String, 寫入檔案
                convertNetmaskToCIDR(StaticIP_mask_cidr);
                StaticIP_Final_mask_cidr = String.valueOf(StaticIP_prflen_ipv4);
                StaticIP_Gateway_Setting = IPv4_Getway_TextField.getText();
                
                
                // 2017.07.11 william remove internet plug & DNS Exception fix
                 if(IPv4_DNS1_TextField != null) {
                    StaticIP_DNS01_Setting = IPv4_DNS1_TextField.getText();
                }
                else {
                    StaticIP_DNS01_Setting = null;
                }               

                if(IPv4_DNS2_TextField != null) {
                    StaticIP_DNS02_Setting = IPv4_DNS2_TextField.getText();
                }
                else {
                    StaticIP_DNS02_Setting = null;
                }
                
                writeStaticIPSetting(StaticIP_IP_Setting, StaticIP_Final_mask_cidr, StaticIP_Gateway_Setting);
                writeStaticIPResolv(StaticIP_DNS01_Setting, StaticIP_DNS02_Setting);

                String IPSetting_Reboot_params = new String();
                IPSetting_Reboot_params="chmod 600 /etc/NetworkManager/system-connections/vdiClientNic";
                Process IPSetting_Reboot_process =Runtime.getRuntime().exec(IPSetting_Reboot_params);
                //System.out.println("------ rb2_StaticIP Action ------\n");
                DisplayChangeAction();

                // 2017.07.11 william remove internet plug & DNS Exception fix
                if(group.getSelectedToggle()== rb1_DHCP){  
                    DNS_or_StaticIP_flag = true;
                }
                if(group.getSelectedToggle()== rb2_StaticIP){                
                    DNS_or_StaticIP_flag = false;
                    WriteStaticIPStatusChange();
                }             
                
                WriteLastIPStatusChange();
                
                /***Reboot***/                            
                String Reboot_params = new String();
                Reboot_params="reboot";
                Process Reboot_process =Runtime.getRuntime().exec(Reboot_params);                                
                
                ThinClientStage.close();

            } catch (UnknownHostException ex) {
                Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
            }                   
        } else {
            //chancelAction();
            alert.close();
        }   
        
    }
    
    private void Dhcp_turn_SIP_alert() throws ParseException{
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(WordMap.get("ThinClient"));//設定對話框視窗的標題列文字  , "您真的要結束程式嗎？", ButtonType.YES, ButtonType.NO
        alert.setHeaderText(null);//設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        alert.setContentText(WordMap.get("Message")+" : "+WordMap.get("Comfirm_RebootMassage"));//VD 已上線,是否還要連線,若連線,則前一個連線會自動斷線.
        ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType(WordMap.get("Alert_Cancel"),ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
        /*** button mouse + key action ***/
        Button button_ok = (Button) alert.getDialogPane().lookupButton(buttonTypeOK);
        Button button_cancel = (Button) alert.getDialogPane().lookupButton(buttonTypeCancel);

        button_ok.setOnMouseEntered((event)-> {
            button_ok.requestFocus();
            //System.out.print(" ** button_ok setOnMouseEntered ** \n");        
        });

        button_cancel.setOnMouseEntered((event)-> {
            button_cancel.requestFocus();
            //System.out.print(" ** button_ok setOnMouseEntered ** \n");        
        });

        button_ok.setOnKeyPressed((event)-> {           
            if(event.getCode() == KeyCode.ENTER){
                button_ok.fire();              
            }        
        });

        button_cancel.setOnKeyPressed((event)-> {           
            if(event.getCode() == KeyCode.ENTER){
                button_cancel.fire();              
            }        
        });
        /*** alert style ***/
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("myDialogs.css");
        dialogPane.getStyleClass().add("myDialog");

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(MainStage.getScene().getWindow());  //有沒有這一行很重要，決定這兩個視窗是否有從屬關係                

        Optional<ButtonType> result01 = alert.showAndWait();

        if (result01.get() == buttonTypeOK){

            try {

                StaticIP_IP_Setting = IPv4_IP_TextField.getText();                        
                //將submask IP讀取後, 轉為InetAddress （為了把IP轉成cidr）
                StaticIP_submask_Setting = IPv4_Mask_TextField.getText();
                StaticIP_mask_cidr=InetAddress.getByName(StaticIP_submask_Setting);
                //寫入檔案, 必須要String, 若是int會是亂碼, 這裡將-Mask IP- 轉-Mask Cidr（int）-再將int轉成String, 寫入檔案
                convertNetmaskToCIDR(StaticIP_mask_cidr);
                //System.out.println(" *****StaticIP_mask_cidr***** :  "+ StaticIP_mask_cidr +"\n");
                //System.out.println(" *****StaticIP_submask***** :  "+ StaticIP_submask +"\n");
                //System.out.println(" *****convertNetmaskToCIDR***** :  "+ StaticIP_prflen_ipv4 +"\n");

                StaticIP_Final_mask_cidr = String.valueOf(StaticIP_prflen_ipv4);

                //System.out.println(" *****StaticIP_Final_mask_cidr***** :  "+ StaticIP_Final_mask_cidr +"\n");

                StaticIP_Gateway_Setting = IPv4_Getway_TextField.getText();
                
                // 2017.07.11 william remove internet plug & DNS Exception fix
                 if(IPv4_DNS1_TextField != null) {
                    StaticIP_DNS01_Setting = IPv4_DNS1_TextField.getText();
                }
                else {
                    StaticIP_DNS01_Setting = null;
                }                  
                
                 if(IPv4_DNS2_TextField != null) {
                    StaticIP_DNS02_Setting = IPv4_DNS2_TextField.getText();
                }
                else {
                    StaticIP_DNS02_Setting = null;
                }                  

//                        System.out.println(" -----StaticIP_IP_Setting---- :  "+ StaticIP_IP_Setting +"\n");
//                        System.out.println(" --StaticIP_Final_mask_cidr-- :  "+ StaticIP_Final_mask_cidr +"\n");
//                        System.out.println(" --StaticIP_Gateway_Setting-- :  "+ StaticIP_Gateway_Setting +"\n");
//                        System.out.println(" --StaticIP_DNS01_Setting---  :  "+ StaticIP_DNS01_Setting +"\n");
//                        System.out.println(" --StaticIP_DNS02_Setting---  :  "+ StaticIP_DNS02_Setting +"\n");

                writeStaticIPSetting(StaticIP_IP_Setting, StaticIP_Final_mask_cidr, StaticIP_Gateway_Setting);
                writeStaticIPResolv(StaticIP_DNS01_Setting, StaticIP_DNS02_Setting);

                String IPSetting_Reboot_params = new String();
                IPSetting_Reboot_params="chmod 600 /etc/NetworkManager/system-connections/vdiClientNic";
                Process IPSetting_Reboot_process =Runtime.getRuntime().exec(IPSetting_Reboot_params);

                System.out.println("------ rb2_StaticIP Action ------\n");

                DisplayChangeAction();
                
                // 2017.07.11 william remove internet plug & DNS Exception fix
                if(group.getSelectedToggle()== rb1_DHCP){  
                    DNS_or_StaticIP_flag = true;
                }
                if(group.getSelectedToggle()== rb2_StaticIP){                
                    DNS_or_StaticIP_flag = false;
                    WriteStaticIPStatusChange();
                }            
                
                WriteLastIPStatusChange();
                
                /***Reboot***/                        
                String Reboot_params = new String();
                Reboot_params="reboot";
                Process Reboot_process =Runtime.getRuntime().exec(Reboot_params);                                 

                ThinClientStage.close();

            } catch (UnknownHostException ex) {
                Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
            }                   
        } else {
            //chancelAction();
            alert.close();
        }
    }
    
    /******* 讀取 雙螢幕 的 主螢幕  ********/
    public void LoadDispalyChange() throws ParseException{
        try{
            File myFile=new File("jsonfile/DispalyChange.json");
            if(myFile.exists()){

                String FilePath=myFile.getPath();
                List<String> JSONLINE=Files.readAllLines(Paths.get(FilePath));
                String JSONCode="";
                JSONCode = JSONLINE.stream().map((s) -> s).reduce(JSONCode, String::concat);
                // 參考 http://stackoverflow.com/questions/7463414/what-s-the-best-way-to-load-a-jsonobject-from-a-json-text-file
                //System.out.print(JSONCode+"\n");
                JSONParser Jparser=new JSONParser();
                JSONObject DispalyChange=(JSONObject) Jparser.parse(JSONCode);
                Dispaly01_change=DispalyChange.get("Dispaly01_change").equals(Dispaly01_change);               
                Dispaly02_change=DispalyChange.get("Dispaly02_change").equals(Dispaly02_change); 
                
                System.out.print("Dispaly01_change-Load : "+Dispaly01_change+"\n");  
                System.out.print("Dispaly02_change-Load : "+Dispaly02_change+"\n");  
                
            }else{
                Dispaly01_change=true;
                Dispaly02_change=false;
            }
        }catch(IOException e){
          
        }
    }
    /******* 紀錄 雙螢幕 的 主螢幕 ********/
    public void WriteDispalyChange(){
        
        JSONObject DispalyChange=new JSONObject();
        
        DispalyChange.put("Dispaly01_change", Dispaly01_change);
        DispalyChange.put("Dispaly02_change", Dispaly02_change);
      
        System.out.print("Dispaly01_change-Write : "+Dispaly01_change+"\n"); 
        System.out.print("Dispaly02_change-Write : "+Dispaly02_change+"\n"); 
        
        try{
            File myFile=new File("jsonfile/DispalyChange.json");
            if(myFile.exists()){
                myFile.delete();
                myFile=null;
            }
            try (FileWriter JsonWriter = new FileWriter("jsonfile/DispalyChange.json")) {
                JsonWriter.write(DispalyChange.toJSONString());
                JsonWriter.flush();
            }
            
        }catch(IOException e){
          
        }
        
    }
    
    private void DoUpdateTask(){
        
        Task<Void> UpdateTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                CV.downloadNewUI();
                CV.updateNewUI();

                return null;
            }
        };

        UpdateTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("** UpdateTask.setOnFailed ** \n");
                //System.out.println("ChangePWTask Ping - testError = "+CPL.testError+"\n"); 
                //System.out.println("ChangePWTask - connCode = "+CPL.CPLconnCode+"\n");

                Lock_Update = false;

            }
        });

        UpdateTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("** UpdateTask.setOnSucceeded ** \n");
                if( !CV.update_code.equals("0") ){

                    SA.CheckSystem_updateFailed_Alert(MainStage);
                    
                    System_Ver_AlertMessage.setText(""); 
                    Update.setDisable(false);                                                                  
                    Check.setDisable(false);                                        

                }
                if( CV.update_code.equals("0") ){                            
                    /***Reboot***/                    
                    String Reboot_params = new String();
                    try {

                        Reboot_params="reboot";
                        Process Reboot_process =Runtime.getRuntime().exec(Reboot_params);

                    } catch (IOException ex) {
                        Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                Lock_Update = false;

            }
        });
        new Thread(UpdateTask).start();

    }
            
    
    // 2017.07.11 william remove internet plug & DNS Exception fix
    public void LoadLastIPStatus() throws ParseException {
        try {
            File myFile=new File("jsonfile/IPStatus.json");

            if(myFile.exists()) {
                String FilePath = myFile.getPath();
                List<String> JSONLINE = Files.readAllLines(Paths.get(FilePath));
                String JSONCode = "";
                JSONCode = JSONLINE.stream().map((s) -> s).reduce(JSONCode, String::concat);            
                JSONParser Jparser = new JSONParser();
                IPStatus = (JSONObject) Jparser.parse(JSONCode);               
                
                DNS_or_StaticIP_flag = IPStatus.get("DNS_or_StaticIP_flag").equals(DNS_or_StaticIP_flag);
                if(IPStatus.get("DNS_or_StaticIP_flag")==null) {
                    DNS_or_StaticIP_flag = true;
                }
            }

        }
        catch(Exception e) { // 2017.11.09 william Json檔案讀寫失敗或錯誤的處理，將IOException 改為通用型例外 Exception
            System.out.print("IPStatus.json read failed \n");
           // e.printStackTrace();
        }
    }
    // 2017.07.11 william remove internet plug & DNS Exception fix
    public void WriteLastIPStatusChange() {
        JSONObject IPStatus=new JSONObject();
        IPStatus.put("DNS_or_StaticIP_flag", DNS_or_StaticIP_flag); 
        
        try {
            File myFile=new File("jsonfile/IPStatus.json");
            if(myFile.exists()) {
                myFile.delete();
                myFile=null;
            }
            try(FileWriter JsonWriter = new FileWriter("jsonfile/IPStatus.json")) {
                JsonWriter.write(IPStatus.toJSONString());
                JsonWriter.flush();
                JsonWriter.close();
            }
            
        }
        catch(IOException e) {
           // e.printStackTrace();
        }
    }
    // 2017.07.12 william remove internet plug & DNS Exception fix
    private void EnterCheck_alert() throws ParseException{
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(WordMap.get("ThinClient"));//設定對話框視窗的標題列文字  , "您真的要結束程式嗎？", ButtonType.YES, ButtonType.NO
        alert.setHeaderText(null);//設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        alert.setContentText(WordMap.get("Message")+" : "+WordMap.get("IP_EnterCheckMassage"));//VD 已上線,是否還要連線,若連線,則前一個連線會自動斷線.

        ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
        
        alert.getButtonTypes().setAll(buttonTypeOK);
        /*** button mouse + key action ***/
        Button button_ok = (Button) alert.getDialogPane().lookupButton(buttonTypeOK);

        button_ok.setOnMouseEntered((event)-> {
            button_ok.requestFocus();
            //System.out.print(" ** button_ok setOnMouseEntered ** \n");        
        });

        button_ok.setOnKeyPressed((event)-> {           
            if(event.getCode() == KeyCode.ENTER){
                button_ok.fire();              
            }        
        });

        /*** alert style ***/
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("myDialogs.css");
        dialogPane.getStyleClass().add("myDialog");

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(MainStage.getScene().getWindow());  //有沒有這一行很重要，決定這兩個視窗是否有從屬關係

        Optional<ButtonType> result01 = alert.showAndWait();
        if (result01.get() == buttonTypeOK){
            alert.close();
        }
    }
    // 2017.07.11 william remove internet plug & DNS Exception fix
    public void LoadStaticIPStatus() throws ParseException {
        try {
            File myFile=new File("jsonfile/StaticIPStatus.json");

            if(myFile.exists()) {
                String FilePath = myFile.getPath();
                List<String> JSONLINE = Files.readAllLines(Paths.get(FilePath));
                String JSONCode = "";
                JSONCode = JSONLINE.stream().map((s) -> s).reduce(JSONCode, String::concat);        
                JSONParser Jparser = new JSONParser();
                StaticIPStatus = (JSONObject) Jparser.parse(JSONCode);               
                
                if(StaticIPStatus.get("IP")!=null) {
                    IPv4_IP_TextField.setText(StaticIPStatus.get("IP").toString());
                }
                else {
                    IPv4_IP_TextField.setText("");
                }
                
                if(StaticIPStatus.get("Mask")!=null) {
                    IPv4_Mask_TextField.setText(StaticIPStatus.get("Mask").toString());                
                }
                else {
                    IPv4_Mask_TextField.setText("");
                }
                
                if(StaticIPStatus.get("Gateway")!=null) {
                    IPv4_Getway_TextField.setText(StaticIPStatus.get("Gateway").toString());
                }
                else {
                    IPv4_Getway_TextField.setText("");
                }
                
                if(StaticIPStatus.get("DNS1")!=null) {
                    IPv4_DNS1_TextField.setText(StaticIPStatus.get("DNS1").toString());
                }
                else {
                    IPv4_DNS1_TextField.setText("");
                }
                
                if(StaticIPStatus.get("DNS2")!=null) {
                    IPv4_DNS2_TextField.setText(StaticIPStatus.get("DNS2").toString()); 
                }
                else {
                    IPv4_DNS2_TextField.setText(""); 
                }       
            }

        }
        catch(Exception e) { // 2017.11.09 william Json檔案讀寫失敗或錯誤的處理，將IOException 改為通用型例外 Exception
            System.out.print("StaticIPStatus.json read failed \n");
           // e.printStackTrace();
        }
    }    
    // 2017.07.11 william remove internet plug & DNS Exception fix
    public void WriteStaticIPStatusChange() {
        JSONObject IPStatus=new JSONObject();
        
        if(IPv4_IP_TextField!=null) {
            IPStatus.put("IP", IPv4_IP_TextField.getText()); 
        }
        else {
            IPStatus.put("IP", ""); 
        }
        
        if(IPv4_Mask_TextField!=null) {
            IPStatus.put("Mask", IPv4_Mask_TextField.getText()); 
        }
        else {
            IPStatus.put("Mask", ""); 
        }
        
        if(IPv4_Getway_TextField!=null) {
            IPStatus.put("Gateway", IPv4_Getway_TextField.getText());
        }
        else {
            IPStatus.put("Gateway", "");
        }
        
        if(IPv4_DNS1_TextField!=null) {
            IPStatus.put("DNS1", IPv4_DNS1_TextField.getText()); 
        }
        else {
            IPStatus.put("DNS1", ""); 
        }
        if(IPv4_DNS2_TextField!=null) {
            IPStatus.put("DNS2", IPv4_DNS2_TextField.getText()); 
        }
        else {
            IPStatus.put("DNS2", ""); 
        } 
        
        
        
        try {
            File myFile=new File("jsonfile/StaticIPStatus.json");
            if(myFile.exists()) {
                myFile.delete();
                myFile=null;
            }
            try(FileWriter JsonWriter = new FileWriter("jsonfile/StaticIPStatus.json")) {
                JsonWriter.write(IPStatus.toJSONString());
                JsonWriter.flush();
                JsonWriter.close();
            }
            
        }
        catch(IOException e) {
           // e.printStackTrace();
        }
    }   
    // 2017.07.11 william remove internet plug & DNS Exception fix
    public boolean IPStatus_Type() throws IOException {
        String command = "cat /etc/NetworkManager/system-connections/vdiClientNic | grep manual";  
        Process IPStatus_Type_process =  Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",command});
        BufferedReader output_IPStatus_Type = new BufferedReader (new InputStreamReader(IPStatus_Type_process.getInputStream()));
        String line_IPStatus_Type = output_IPStatus_Type.readLine();
        if(line_IPStatus_Type != null) {
            return false;
        }
        else {
            return true;
        }      
    } 
         
       /****變更button字型****字型:中文-標楷體,英文:Times New Roman****/
    private void ChangeDisplayButtonLang2(Button but01, Button but02, Button but03){
        //Button "確定" "取消"
        if("English".equals(WordMap.get("SelectedLanguage"))){
            but01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");       
            but02.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");
            but03.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");            
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/
            //win7以上僅能用英文名稱
            but01.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;");
            but02.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;");
            but03.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;");
        }
        
     }    
    
    // 2018.01.29 william 雙螢幕實作     
    /*------------------------ 寫檔案及讀取檔案 ------------------------*/
    //  讀取檔案
    public void LoadInstalllog() throws ParseException {
        try {
            File myFile = new File("jsonfile/Install_log.json");

            if(myFile.exists()) {
                String FilePath       = myFile.getPath();
                List<String> JSONLINE = Files.readAllLines(Paths.get(FilePath));
                String JSONCode       = "";
                JSONCode              = JSONLINE.stream().map((s) -> s).reduce(JSONCode, String::concat);
                JSONParser Jparser    = new JSONParser();
                jb                    = (JSONObject) Jparser.parse(JSONCode);   
                if(jb.get("IsInstallArandr").equals(true)) {
                    IsInstallArandr = true;
                } else {
                    IsInstallArandr = false;
                }           
              
            } else {
                IsInstallArandr   = false;
            }
        }
        catch(Exception e) { // 2017.11.09 william Json檔案讀寫失敗或錯誤的處理，將IOException 改為通用型例外 Exception
            IsInstallArandr   = false;
            System.out.print("Install_log.json read failed \n");
           // e.printStackTrace();
        }
    }        
    
    // 寫檔案
    public void WriteInstalllogChange() {
        jb = new JSONObject();
        jb.put("IsInstallArandr", true); 
        
        try {
            File myFile = new File("jsonfile/Install_log.json");
            if(myFile.exists()) {
                myFile.delete();
                myFile = null;
            }
            try(FileWriter JsonWriter = new FileWriter("jsonfile/Install_log.json")) {
                JsonWriter.write(jb.toJSONString());
                JsonWriter.flush();
                JsonWriter.close();
            }   
        }
        catch(Exception e) {
           // e.printStackTrace();
        }
    }  
    
    // 執行螢幕設定
    public void SetMonitor(String primary_name, String second_name, String primary_width, String primary_height, String second_width, String second_height, boolean IsSecond, int select_display) throws IOException {
        System.out.println("--------------------------------------------------------------------------------\n");
        System.out.println(" Primary Display  : " + primary_name + "\n");
        System.out.println(" Second Display  : " + second_name + "\n");   
        System.out.println(" Primary width : " + primary_width + " ,Primary height : " + primary_height + "\n");
        System.out.println(" Second width : " + second_width + " ,Second height : " + second_height + "\n");
        System.out.println(" 雙螢幕 : " + IsSecond + "\n");
        System.out.println(" Primary Display disable : " + Primary_disable + " ,Second Display disable : " + Second_disable + "\n");
        System.out.println(" 同步 Duplicate : " + Duplicate_enable + "\n");
        System.out.println(" select display : " + select_display + "\n");
        System.out.println("--------------------------------------------------------------------------------\n");
        
        int pw;
        int ph;
        int sw;
        int sh;
        
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

        
        if (!IsSecond) {
            monitor_exec_string = "xrandr --output " + primary_name + " --mode " + primary_width + "x" + primary_height + " --pos 0x0 --rotate normal";
            WriteMonitorSh(monitor_exec_string);
            Runtime.getRuntime().exec(monitor_exec_string);
        } else {
            switch (select_display) {
                case 1:
                    DisplayDisable(second_name);
                    DisplayDisable(second_name);
                    monitor_exec_string = "xrandr --output " + primary_name + " --mode " + primary_width + "x" + primary_height + " --pos 0x0 --rotate normal --output " + second_name + " --off";
                    break;
                case 2:
                    DisplayDisable(primary_name);
                    DisplayDisable(primary_name);
                    monitor_exec_string = "xrandr --output " + primary_name + " --off --output " + second_name + " --mode " + second_width + "x" + second_height + " --pos 0x0 --rotate normal";
                    break;
                case 3:
                    monitor_exec_string = "xrandr --output " + primary_name + " --mode " + primary_width + "x" + primary_height + " --pos 0x0 --rotate normal --output " + second_name + " --mode " + second_width + "x" + second_height + " --pos " + primary_width + "x0 --rotate normal";    
                    // monitor_exec_string = "xrandr --output " + primary_name + " --mode " + primary_width + "x" + primary_height + " --pos " + second_width + "x0 --rotate normal --output " + second_name + " --mode " + second_width + "x" + second_height + " --pos 0x0 --rotate normal";            
                    break;
                case 4:
                    
                    String Resolution = GetMonitorResolution();
                    monitor_exec_string = "xrandr --output " + primary_name + " --mode " + Resolution + " --pos 0x0 --rotate normal --output " + second_name + " --mode " + Resolution + " --pos 0x0 --rotate normal";                                
//                    if((pw * ph) < (sw * sh)) {
//                        monitor_exec_string = "xrandr --output " + primary_name + " --mode " + primary_width + "x" + primary_height + " --pos 0x0 --rotate normal --output " + second_name + " --mode " + primary_width + "x" + primary_height + " --pos 0x0 --rotate normal";                                
//                    } else if ((pw * ph) > (sw * sh)) {
//                        monitor_exec_string = "xrandr --output " + primary_name + " --mode " + second_width + "x" + second_height + " --pos 0x0 --rotate normal --output " + second_name + " --mode " + second_width + "x" + second_height + " --pos 0x0 --rotate normal";                    
//                    } else if ((pw * ph) == (sw * sh)) {
//                        monitor_exec_string = "xrandr --output " + primary_name + " --mode " + primary_width + "x" + primary_height + " --pos 0x0 --rotate normal --output " + second_name + " --mode " + primary_width + "x" + primary_height + " --pos 0x0 --rotate normal";            
//                    } else {
//                        System.out.println(" 不同步 Non Duplicate \n");
//                    }  
                    break;                
            }             

            WriteMonitorSh(monitor_exec_string);
            Runtime.getRuntime().exec(monitor_exec_string);
        }
    }        

    
    public String GetMonitorResolution() {
        String Resolution = "";
        boolean Stop = false;
//        System.out.println("Display_primary_resolution_list: " + Display_primary_resolution_list);  
//        System.out.println("Display_second_resolution_list: " + Display_second_resolution_list);          

        for(int i = 0; i < Display01_ResolutionList.size(); i++) {               
            if(Stop)                
                break;
                                      
            if(Display01_ResolutionList.get(i).equals("1024x768")) {
                Resolution = "1024x768";
                break;
            }             
            
            for (String List : Display02_ResolutionList) {
                if(Display01_ResolutionList.get(i).equals(List)) {
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
    
    
    
    // Write Monitor.Sh file
    public void WriteMonitorSh(String script) {
        try {
            File MonitorShFile = new File("/root/.screenlayout/monitor.sh");
            
            if(MonitorShFile.exists()) {
                MonitorShFile.delete();
                MonitorShFile = null;
            }
            
            try(FileWriter ShFileWriter = new FileWriter("/root/.screenlayout/monitor.sh")) {
                ShFileWriter.write("#!/bin/sh \n" + script);
                ShFileWriter.flush();
            }   
            
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","chmod +x /root/.screenlayout/monitor.sh"});
        }
        catch(Exception e) {
           // e.printStackTrace();
        }
    }     
    
    // 產生被關閉螢幕的檔案
    public void DisplayDisable(String Display) {
        try {
            File File = new File("/root/.screenlayout/DisplayDisable_" + Display + ".txt");
            
            if(File.exists()) {
                File.delete();
                File = null;
            } 
            
            try(FileWriter ShFileWriter = new FileWriter("/root/.screenlayout/DisplayDisable_" + Display + ".txt")) {
                ShFileWriter.write("Disable " + Display + "\n");
                ShFileWriter.flush();
            }              
            
        }
        catch(Exception e) {
           // e.printStackTrace();
        }
    }   
    
    // remove DisplayDisable_*.txt
    public void rmDD() throws IOException {
        Runtime.getRuntime().exec("rm /root/.screenlayout/DisplayDisable_VGA1.txt");
        Runtime.getRuntime().exec("rm /root/.screenlayout/DisplayDisable_HDMI1.txt");
        Runtime.getRuntime().exec("rm /root/.screenlayout/DisplayDisable_HDMI2.txt");
        Runtime.getRuntime().exec("rm /root/.screenlayout/DisplayDisable_eDP1.txt");// 2018.12.19 Display bug fix
    }     
    
   // 2018.04.13 william wifi實作 

    /****變更Label字型****字型:中文-標楷體,英文:Times New Roman****/
    private void ChangeWIFILabelLang(Label title01, Label title02, Label lab01, Label lab02, Label lab03, Label col01, Label col02, Label col03, Label wifiname, Label wifipw ){
        //Label "Password"
        if("English".equals(WordMap.get("SelectedLanguage"))){
            title01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-font-size: 13pt;");  // Ubuntu ; Liberation Serif
            title02.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-font-size: 13pt;");
            
            lab01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 600; -fx-font-size: 12pt;");
            lab02.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 600; -fx-font-size: 12pt;");
            lab03.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 600; -fx-font-size: 12pt;");    

            wifiname.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 600; -fx-font-size: 12pt;");
            wifipw.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 600; -fx-font-size: 12pt;");    
            
            //冒號":"-win7
            col01.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 14px; -fx-font-weight: 700;");
            col02.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 14px; -fx-font-weight: 700;");
            col03.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 14px; -fx-font-weight: 700;");
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/      
            //win7以上僅能用英文名稱
            title01.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 900; -fx-font-size: 13pt;");
            //title01.setStyle("-fx-font-weight: 900; -fx-font-size: 13pt;");
            title02.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 900; -fx-font-size: 13pt;");    
            //title01.setFont(Font.font("Droid Sans", FontWeight.BLACK, 13));
            //title02.setFont(Font.font("Droid Sans", FontWeight.BOLD, 13));
            
            lab01.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 600; -fx-font-size: 12pt;");
            lab02.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 600; -fx-font-size: 12pt;");
            lab03.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 600; -fx-font-size: 12pt;");        

            wifiname.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 600; -fx-font-size: 12pt;");
            wifipw.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 600; -fx-font-size: 12pt;");             

            //冒號":"-win7
            col01.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 14px; -fx-font-weight: 700;");
            col02.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 14px; -fx-font-weight: 700;");
            col03.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 14px; -fx-font-weight: 700;");
            //col04.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-size: 14px; -fx-font-weight: 900;");
            //col05.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-size: 14px; -fx-font-weight: 900;");
        }
       
     }           
    /****變更button字型****字型:中文-標楷體,英文:Times New Roman****/
    private void ChangeWIFIButtonLang(RadioButton rbut01, RadioButton rbut02, Button but01, Button but02, Button but03, Button but04){
        //Button "確定" "取消"
        if("English".equals(WordMap.get("SelectedLanguage"))){
            
            rbut01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-font-size: 14pt;");
            rbut02.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-font-size: 14pt;");
            but01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");
            but02.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");               
            but03.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");
            but04.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/
          
            rbut01.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900; -fx-font-size: 14pt;");
            rbut02.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900; -fx-font-size: 14pt;");
            but01.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;");
            but02.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;");          
            but03.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;");          
            but04.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;");
        }
        
     }     
    
//    public void OpenWifiList() throws IOException {
//                            
//        Task<Void> longRunningTask = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {                          
//                Platform.runLater(() -> { 
//                    wifi = new GetWifiInfo(MainStage, WordMap);
//                     wifi_Name_TextField.setText(wifi.getData()); 
//                });                                                 
//
//                return null;
//            }
//        };
//
//        longRunningTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
//            @Override
//            public void handle(WorkerStateEvent event) {
//                           
//            }                        
//        });
//
//        longRunningTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//            @Override
//            public void handle(WorkerStateEvent event) {
//
//            }
//        });
//        new Thread(longRunningTask).start();
//    }
          
    private void writeWifiDHCPSetting(String wifi_name, String wifi_security_type, String wifi_pwd){
   
        try{
            File myFile=new File("/etc/NetworkManager/system-connections/WiFi_" + wifi_name);
            if(myFile.exists()){
                myFile.delete();
                myFile=null;
            }
            FileWriter DHCPWriter=new FileWriter("/etc/NetworkManager/system-connections/WiFi_" + wifi_name, true);
            try (BufferedWriter bufferedDHCPWriter = new BufferedWriter(DHCPWriter)) {
                bufferedDHCPWriter.write("[connection]");
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("id=" + wifi_name);
                bufferedDHCPWriter.newLine();              
                bufferedDHCPWriter.write("uuid=");
                bufferedDHCPWriter.write(TC_uniqueKey.toString());                
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("type=802-11-wireless");   
                bufferedDHCPWriter.newLine();                
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("[802-11-wireless]");                             
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("ssid=" + wifi_name);
                bufferedDHCPWriter.newLine();              
                bufferedDHCPWriter.write("mode=infrastructure");
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("security=802-11-wireless-security");
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("[802-11-wireless-security]");
                bufferedDHCPWriter.newLine();
                
                if(wifi_security_type.contains("WPA")) {
                    bufferedDHCPWriter.write("key-mgmt=wpa-psk");
                    bufferedDHCPWriter.newLine();
                    bufferedDHCPWriter.write("auth-alg=open");
                    bufferedDHCPWriter.newLine();
                    bufferedDHCPWriter.write("psk=" + wifi_pwd);
                } else if (wifi_security_type.contains("WEP")) {
                    bufferedDHCPWriter.write("key-mgmt=none");
                    bufferedDHCPWriter.newLine();
                    bufferedDHCPWriter.write("wep-key0=" + wifi_pwd);                
                }

                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("[ipv4]");
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("method=auto");
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("[ipv6]");
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("method=auto");
                bufferedDHCPWriter.newLine();

            }
            
        }catch(IOException e){
        }
    
    }    
      
    private void GetWifiDHCPInetAddr() throws SocketException, UnknownHostException, IOException {
        
        /*** Get ethernet name : wlan0 ***/       
        Process ethernet_name_process = Runtime.getRuntime().exec("ip route show");
        wifi_ethernet_name_DHCP = "";

        BufferedReader output_ethernet = new BufferedReader (new InputStreamReader(ethernet_name_process.getInputStream()));
        String line_ethernet = output_ethernet.readLine();
        if(line_ethernet != null) {  
            
            Disconnected_flag = false; 

            // String[] ethernet_Line_list = line_ethernet.split(" ");
            String[] ethernet_Line_list;
            
            while(line_ethernet != null) {  
                
                if(line_ethernet.contains("wlan") && line_ethernet.contains("src")) {
                    ethernet_Line_list = line_ethernet.split(" ");
                    
            String wlanIndex = "wlan";
            int index = -1;
            for (int i = 0; i < ethernet_Line_list.length; i++) {
                if (ethernet_Line_list[i].contains(wlanIndex)) {
                    wifi_ethernet_name_DHCP = ethernet_Line_list[i];
                    // index = i;
                    break;
                }
            }                   
                }              
                
                line_ethernet = output_ethernet.readLine();
            }
            output_ethernet.close();             
            


            
            if(!wifi_ethernet_name_DHCP.isEmpty()) { 
                // System.out.println(" wifi_ethernet_name_DHCP  : " + wifi_ethernet_name_DHCP +"\n");
        
                String Dhcp_dhclient = "dhclient "+ wifi_ethernet_name_DHCP ;
            
                Process Dhcp_dhclient_00 = Runtime.getRuntime().exec(Dhcp_dhclient); // DHCP 的環境下取得IP ( dhclient wlan0 ) -v：觀看執行過程。-r：釋放IP。
                // Process Dhcp_dhclient_01 = Runtime.getRuntime().exec(Dhcp_dhclient); // DHCP 的環境下取得IP ( dhclient wlan0 ) -v：觀看執行過程。-r：釋放IP。
        
                NetworkInterface DHCP_ni = NetworkInterface.getByName(wifi_ethernet_name_DHCP);
                Enumeration<InetAddress> inetAddresses =  DHCP_ni.getInetAddresses();
        
                while(inetAddresses.hasMoreElements()) {
                    InetAddress DHCP_ia = inetAddresses.nextElement();
                    if(!DHCP_ia.isLinkLocalAddress()) {
                        /*** DHCP IPv4 IP ***/
                        wifi_DHCP_IP = DHCP_ia.getHostAddress() ;
 
                        /*** DHCP IPv4 SubMask ***/
                        wifi_DHCP_prflen_ipv4 =DHCP_ni.getInterfaceAddresses().get(1).getNetworkPrefixLength();                 
               
                        /******** 將 Host Bit Length(數字00) 轉為 Subnet Mask(000.000.000.000) *********/
                        int shft = 0xffffffff<<(32-wifi_DHCP_prflen_ipv4);
                        int oct1 = ((byte) ((shft&0xff000000)>>24)) & 0xff;
                        int oct2 = ((byte) ((shft&0x00ff0000)>>16)) & 0xff;
                        int oct3 = ((byte) ((shft&0x0000ff00)>>8)) & 0xff;
                        int oct4 = ((byte) (shft&0x000000ff)) & 0xff;
                        wifi_DHCP_submask = oct1 + "." + oct2 + "." + oct3 + "."+oct4;
               
                        /*** HCP IPv4 default Gateway ***/
                        Process Dhcp_gateway_show = Runtime.getRuntime().exec("ip route show"); 
                        try {
                            Dhcp_gateway_show.waitFor();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                        }                

                        try (BufferedReader output_gateway = new BufferedReader (new InputStreamReader(Dhcp_gateway_show.getInputStream()))) {
                            String line_gateway = output_gateway.readLine();
                            while(line_gateway != null) {                          
                                if ( line_gateway.startsWith("default") == true ){
                                    String[] Gatewat_Line_list =line_gateway.split(" ");
                                    wifi_DHCP_Gateway= Gatewat_Line_list[2];              
                                    break;
                                }
                        
                                line_gateway = output_gateway.readLine();
                            }
                        }
                        /*** DHCP IPv4 DNS ***/  
                        Process Dhcp_DNS_show_Process = Runtime.getRuntime().exec("cat /etc/resolv.conf"); //route | grep 'default' | awk '{print $2}' //netstat -rn

                        try {
                            Dhcp_DNS_show_Process.waitFor();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                
                        //cat /var/lib/dhcp/dhclient.leases  ---->找到「option dhcp-server-identifier」就可以知道DHCP Server的IP了!
                        //cat /etc/network/interfaces   ---->查看auto 與 iface 是否為 dhcp 或 static (但現在僅顯示 loopback)
                        //cat /etc/hosts
                        //cat /etc/resolv.conf
                        try (BufferedReader output_DNS = new BufferedReader (new InputStreamReader(Dhcp_DNS_show_Process.getInputStream()))) {
                            String line_DNS = output_DNS.readLine();
                            wifi_DHCP_DNS01_Line_list = null;
                    
                            while(line_DNS != null) {
                        
                                if (line_DNS.startsWith("nameserver") == true) {
                            
                                    if(wifi_DHCP_DNS01_Line_list == null) {                                
                                        wifi_DHCP_DNS01_Line_list = line_DNS.split(" ");
                                        if (wifi_DHCP_DNS01_Line_list.length == 2 ) {
                                            wifi_DHCP_DNS01 = wifi_DHCP_DNS01_Line_list[1];
                                        } else {
                                            wifi_DHCP_DNS01 = null;
                                        }                                                                
                                    } else {                                
                                        wifi_DHCP_DNS02_Line_list = line_DNS.split(" ");
                                        if (wifi_DHCP_DNS02_Line_list.length == 2 ) {
                                            if(wifi_DHCP_DNS01_Line_list.length == 1 ) {
                                                wifi_DHCP_DNS01 = wifi_DHCP_DNS02_Line_list[1];
                                            } else {
                                                wifi_DHCP_DNS02 = wifi_DHCP_DNS02_Line_list[1];
                                            }                                 
                                        } else {
                                            wifi_DHCP_DNS02 = null;
                                        }                                 
                                        break;                                
                                    }
                                }
                        
                                line_DNS = output_DNS.readLine();
                            }
                        }
                    }
                }
            } else {
                wifi_DHCP_IP      = "";
                wifi_DHCP_submask = "";
                wifi_DHCP_Gateway = "";
                wifi_DHCP_DNS01   = "";
                wifi_DHCP_DNS02   = "";        
            }             
        } else { 
            Disconnected_flag = true;
        }        
    }
    
    private void GetWifiStaticIPInetAddr() throws SocketException, UnknownHostException, IOException {
        
        /***Get ethernet name : wlan0***/       
        Process ethernet_name_process = Runtime.getRuntime().exec("ip route show"); 
        wifi_ethernet_name_StaticIP = "";


        BufferedReader output_ethernet = new BufferedReader (new InputStreamReader(ethernet_name_process.getInputStream()));
        String line_ethernet = output_ethernet.readLine();
        if(line_ethernet != null) {
            
            Disconnected_flag = false; 

            // String[] ethernet_Line_list = line_ethernet.split(" ");
            String[] ethernet_Line_list;
            
            while(line_ethernet != null) {  
                
                if(line_ethernet.contains("wlan") && line_ethernet.contains("src")) {
                    ethernet_Line_list = line_ethernet.split(" ");
                    
            String ethIndex = "wlan";       
            int index = -1;
            for (int i=0;i<ethernet_Line_list.length;i++) {
                if (ethernet_Line_list[i].contains(ethIndex)) {
                    wifi_ethernet_name_StaticIP = ethernet_Line_list[i]; 
                    // index = i;
                    break;
                }       
            }                   
                }              
                
                line_ethernet = output_ethernet.readLine();
            }
            output_ethernet.close();            


            
            if(!wifi_ethernet_name_StaticIP.isEmpty()) { 
                NetworkInterface ni = NetworkInterface.getByName(wifi_ethernet_name_StaticIP);
                Enumeration<InetAddress> inetAddresses =  ni.getInetAddresses();         
        
                while(inetAddresses.hasMoreElements()) {
                    InetAddress ia = inetAddresses.nextElement();
                    if(!ia.isLinkLocalAddress()) {
                        /***StaticIP IPv4 IP***/
                        wifi_StaticIP_IP = ia.getHostAddress() ; 
 
                        /***StaticIP IPv4 SubMask***/
                        wifi_StaticIP_prflen_ipv4 = ni.getInterfaceAddresses().get(1).getNetworkPrefixLength(); 
                
                        //ipv6                
                        //short DHCP_prflen_ipv6 =ni.getInterfaceAddresses().get(0).getNetworkPrefixLength();
               
                        /******** 將 Host Bit Length(數字00) 轉為 Subnet Mask(000.000.000.000) *********/
                        int shft = 0xffffffff<<(32-wifi_StaticIP_prflen_ipv4);
                        int oct1 = ((byte) ((shft&0xff000000)>>24)) & 0xff;
                        int oct2 = ((byte) ((shft&0x00ff0000)>>16)) & 0xff;
                        int oct3 = ((byte) ((shft&0x0000ff00)>>8)) & 0xff;
                        int oct4 = ((byte) (shft&0x000000ff)) & 0xff;
                        wifi_StaticIP_submask = oct1 + "." + oct2 + "." + oct3 + "." + oct4;
               
                        /***StaticIP IPv4 default Gateway***/
                        Process StaticIP_gateway_show = Runtime.getRuntime().exec("ip route show"); 

                        try {
                            StaticIP_gateway_show.waitFor();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                        }                  
                        try (BufferedReader output_gateway = new BufferedReader (new InputStreamReader(StaticIP_gateway_show.getInputStream()))) {
                            String line_gateway = output_gateway.readLine();
                            while(line_gateway != null) {  
                        
                                if ( line_gateway.startsWith("default") == true ) {               
                                    String[] Gatewat_Line_list =line_gateway.split(" ");
                                    wifi_StaticIP_Gateway= Gatewat_Line_list[2];              
                                    break;
                                }
                        
                                line_gateway = output_gateway.readLine();
                            }
                        }
                
                        /***StaticIP IPv4 DNS***/  
                        Process StaticIP_DNS_show_Process = Runtime.getRuntime().exec("cat /etc/resolv.conf"); 
                        try {
                            StaticIP_DNS_show_Process.waitFor();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                
                        //cat /var/lib/dhcp/dhclient.leases  ---->找到「option dhcp-server-identifier」就可以知道DHCP Server的IP了!
                        //cat /etc/network/interfaces   ---->查看auto 與 iface 是否為 dhcp 或 static (但現在僅顯示 loopback)
                        //cat /etc/hosts
                        //cat /etc/resolv.conf
                        try (BufferedReader StaticIP_output_DNS = new BufferedReader (new InputStreamReader(StaticIP_DNS_show_Process.getInputStream()))) {
                            String StaticIP_line_DNS = StaticIP_output_DNS.readLine();
                            wifi_StaticIP_DNS01_Line_list = null;
                                        
                            while(StaticIP_line_DNS != null) {                        
                        
                                if (StaticIP_line_DNS.startsWith("nameserver") == true) {
                            
                                    if(wifi_StaticIP_DNS01_Line_list == null) {
                                
                                        wifi_StaticIP_DNS01_Line_list = StaticIP_line_DNS.split(" ");
                                        if (wifi_StaticIP_DNS01_Line_list.length == 2 ) {
                                            wifi_StaticIP_DNS01 = wifi_StaticIP_DNS01_Line_list[1];
                                        } else {
                                            wifi_StaticIP_DNS01 = null;
                                        }              
                                    } else {                               
                                        wifi_StaticIP_DNS02_Line_list = StaticIP_line_DNS.split(" ");
                                        if (wifi_StaticIP_DNS02_Line_list.length == 2 ) {
                                            if(wifi_StaticIP_DNS01_Line_list.length == 1) {
                                                wifi_StaticIP_DNS01 = wifi_StaticIP_DNS02_Line_list[1];
                                            } else {
                                                wifi_StaticIP_DNS02 = wifi_StaticIP_DNS02_Line_list[1];
                                            }
                                        } else {
                                            wifi_StaticIP_DNS02 = null;
                                        }
                                        break;                                
                                    }
                                }
                        
                                StaticIP_line_DNS = StaticIP_output_DNS.readLine();
                            }
                        }                
                    }
                }        
            } else {
                wifi_StaticIP_IP      = "";
                wifi_StaticIP_submask = "";
                wifi_StaticIP_Gateway = "";
                wifi_StaticIP_DNS01   = "";
                wifi_StaticIP_DNS02   = "";        
            }                 
        } else { 
            Disconnected_flag = true;
        }
        
    }    

    private void EnterWifiCheck_alert() throws ParseException{
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(WordMap.get("ThinClient"));//設定對話框視窗的標題列文字  , "您真的要結束程式嗎？", ButtonType.YES, ButtonType.NO
        alert.setHeaderText(null);//設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        alert.setContentText(WordMap.get("Message")+" : "+WordMap.get("IP_EnterWifiCheckMassage"));//VD 已上線,是否還要連線,若連線,則前一個連線會自動斷線.

        ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
        
        alert.getButtonTypes().setAll(buttonTypeOK);
        /*** button mouse + key action ***/
        Button button_ok = (Button) alert.getDialogPane().lookupButton(buttonTypeOK);

        button_ok.setOnMouseEntered((event)-> {
            button_ok.requestFocus();
            //System.out.print(" ** button_ok setOnMouseEntered ** \n");        
        });

        button_ok.setOnKeyPressed((event)-> {           
            if(event.getCode() == KeyCode.ENTER){
                button_ok.fire();              
            }        
        });

        /*** alert style ***/
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("myDialogs.css");
        dialogPane.getStyleClass().add("myDialog");

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(MainStage.getScene().getWindow());  //有沒有這一行很重要，決定這兩個視窗是否有從屬關係

        Optional<ButtonType> result01 = alert.showAndWait();
        if (result01.get() == buttonTypeOK){
            alert.close();
        }
    }    
    
    public int wifi_convertNetmaskToCIDR (InetAddress netmask){
        wifi_StaticIP_mask_cidr = netmask;

        byte[] netmaskBytes = netmask.getAddress();
        //int cidr = 0;
        wifi_StaticIP_prflen_ipv4 = 0;
        boolean zero = false;
        for(byte b : netmaskBytes){
            int mask = 0x80;

            for(int i = 0; i < 8; i++){
                int result = b & mask;
                if(result == 0){
                    zero = true;
                }else if(zero){
                    //throw new IllegalArgumentException("Invalid netmask.");
                } else {
                    wifi_StaticIP_prflen_ipv4++;
                }
                mask >>>= 1;
            }
        }
        return wifi_StaticIP_prflen_ipv4;
    }   
    
    private void writeWifiStaticIPSetting(String wifi_name, String wifi_security_type, String wifi_pwd, String SIP, String SMask, String SGateway) {        
        try{
            File myFile = new File("/etc/NetworkManager/system-connections/WiFi_"  + wifi_name);
            if(myFile.exists()){
                myFile.delete();
                myFile = null;
            }
            FileWriter StaticIPWriter = new FileWriter("/etc/NetworkManager/system-connections/WiFi_" + wifi_name, true);
            try (BufferedWriter bufferedSIPWriter = new BufferedWriter(StaticIPWriter)) {
                bufferedSIPWriter.write("[connection]");
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.write("id=" + wifi_name);
                bufferedSIPWriter.newLine();              
                bufferedSIPWriter.write("uuid=");
                bufferedSIPWriter.write(TC_uniqueKey.toString());                
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.write("type=802-11-wireless");   
                bufferedSIPWriter.newLine();                
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.write("[802-11-wireless]");                             
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.write("ssid=" + wifi_name);
                bufferedSIPWriter.newLine();              
                bufferedSIPWriter.write("mode=infrastructure");
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.write("security=802-11-wireless-security");
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.write("[802-11-wireless-security]");
                bufferedSIPWriter.newLine();
                
                if(wifi_security_type.contains("WPA")) {
                    bufferedSIPWriter.write("key-mgmt=wpa-psk");
                    bufferedSIPWriter.newLine();
                    bufferedSIPWriter.write("auth-alg=open");
                    bufferedSIPWriter.newLine();
                    bufferedSIPWriter.write("psk=" + wifi_pwd);
                } else if (wifi_security_type.contains("WEP")) {
                    bufferedSIPWriter.write("key-mgmt=none");
                    bufferedSIPWriter.newLine();
                    bufferedSIPWriter.write("wep-key0=" + wifi_pwd);                
                }
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.write("[ipv4]");
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.write("method=manual");
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.write("address1=");
                bufferedSIPWriter.write(SIP);
                bufferedSIPWriter.write("/");
                bufferedSIPWriter.write(SMask);                
                bufferedSIPWriter.write(",");
                // 2017.07.11 william remove internet plug & DNS Exception fix                               
                if(SGateway!=null) {
                    bufferedSIPWriter.write(SGateway);
                    bufferedSIPWriter.newLine();
                }
                else {
                    bufferedSIPWriter.newLine();
                } 
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.write("[ipv6]");
                bufferedSIPWriter.newLine();
                bufferedSIPWriter.write("method=auto");
                bufferedSIPWriter.newLine();                

                DNS_or_StaticIP_flag = false;                
            }            
        } catch(IOException e) {
        }    
    }    
    
    public void Get_ConnecttingWifi_Name() throws IOException {
        Process Detect_wifi_Process = Runtime.getRuntime().exec("iwconfig " + wlanName); 
        try (BufferedReader output_Detect_wifi = new BufferedReader (new InputStreamReader(Detect_wifi_Process.getInputStream()))) {
            String line_Detect_wifi = output_Detect_wifi.readLine();
            String[] Detect_wifi_Line_list = null;
             
            while(line_Detect_wifi != null) {  
                if(line_Detect_wifi.contains("ESSID")) {
                    Detect_wifi_Line_list = line_Detect_wifi.split(":");
                    get_wifi_name = Detect_wifi_Line_list[1];
                    
                    if(get_wifi_name.contains("off/any")) {
                        wifi_Name_TextField.setText("");
                        temp_wifi_name = "";
                    } else {
                        get_wifi_name = get_wifi_name.replace('"', ' ');
                        get_wifi_name = get_wifi_name.trim();                      
                        wifi_Name_TextField.setText(get_wifi_name);  
                        temp_wifi_name = get_wifi_name;
                    }
                }                       
                
                line_Detect_wifi = output_Detect_wifi.readLine();
            }
            
            if(line_Detect_wifi == null) {
                wifi_DNS_or_StaticIP = true;            
            }
            
            output_Detect_wifi.close();       
        }
    }       
    
    public void Get_ConnecttingWifi_PWD_uuid_SecurityType() throws IOException {
        if(!wifi_Name_TextField.getText().isEmpty()) {
            Process Detect_wifi_Process = Runtime.getRuntime().exec("cat /etc/NetworkManager/system-connections/WiFi_" + get_wifi_name); 
            try (BufferedReader output_Detect_wifi = new BufferedReader (new InputStreamReader(Detect_wifi_Process.getInputStream()))) {
                String line_Detect_wifi = output_Detect_wifi.readLine();
                String[] Detect_wifi_Line_list = null;
             
                while(line_Detect_wifi != null) {  
                    if(line_Detect_wifi.contains("psk")) {
                        Detect_wifi_Line_list = line_Detect_wifi.split("=");
                        get_wifi_pwd = Detect_wifi_Line_list[1];               
                        wifi_Password_TextField.setText(get_wifi_pwd);
                        wifi_security_type = "WPA";
                    }         
                
                    if(line_Detect_wifi.contains("wep")) {
                        Detect_wifi_Line_list = line_Detect_wifi.split("=");
                        get_wifi_pwd = Detect_wifi_Line_list[1];               
                        wifi_Password_TextField.setText(get_wifi_pwd);
                        wifi_security_type = "WEP";
                    }        
                    
                   
                
                    line_Detect_wifi = output_Detect_wifi.readLine();
                }
                output_Detect_wifi.close();       
            }        
        }               
    }       
    
    public void Search_Wifi() throws IOException {
                            
        Task<Void> longRunningTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {                          
                Platform.runLater(() -> { 
                    
                    //wifi = new GetWifiInfo(MainStage, WordMap);
                });                                                 
                
                
                
                return null;
            }
        };

        longRunningTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                           
            }                        
        });

        longRunningTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {

            }
        });
        new Thread(longRunningTask).start();
    }    
    
    // 畫面鎖定
    public void Search_lock() {
        Search_WifiGP = new GridPane();              
        Search_WifiGP.setAlignment(Pos.CENTER);       
        Search_WifiGP.setPadding(new Insets(35, 50, 50, 20));
        Search_WifiGP.setHgap(15); //元件間的水平距離
        Search_WifiGP.setVgap(15); //元件間的垂直距離
        
        if("English".equals(WordMap.get("SelectedLanguage"))){
            Search_WifiGP.setPrefSize(425, 400); // 2018.04.12 william  多VD登入新增開機狀態
            Search_WifiGP.setMaxSize(425, 400); // 2018.04.12 william  多VD登入新增開機狀態
            Search_WifiGP.setMinSize(425, 400); // 2018.04.12 william  多VD登入新增開機狀態
        } else {
            Search_WifiGP.setPrefSize(365, 400); // 2018.04.12 william  多VD登入新增開機狀態
            Search_WifiGP.setMaxSize(365, 400); // 2018.04.12 william  多VD登入新增開機狀態
            Search_WifiGP.setMinSize(365, 400); // 2018.04.12 william  多VD登入新增開機狀態        
        }          
                
//        Search_WifiGP.setTranslateX(0);
//        Search_WifiGP.setTranslateY(-15);
        
        Search_WifiGP.setStyle("-fx-background-color: #EEF5F5 ; -fx-opacity: 0.7 ; -fx-background-radius: 4;-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 10, 0.0 , 0 , 5 );");    // 2018.18.18 new create & reborn 
        Search_Wifi_title = new Label(WordMap.get("Thread_Login")+"\n"+WordMap.get("Thread_Waiting"));
        Search_Wifi_title.setAlignment(Pos.CENTER);
        Search_Wifi_title.setTranslateY(0);
        Search_Wifi_title.setId("Thread_status");
        Search_WifiGP.add(Search_Wifi_title,0,0);

        p1 = new ProgressIndicator();
        p1.setTranslateY(10);
        //p1.setStyle("-fx-foreground-color: #1A237E");
        Search_WifiGP.add(p1,0,1); //2,1是指這個元件要佔用2格的column和1格的row 
                
        stack = new StackPane();
        stack.getChildren().addAll(grid, Search_WifiGP);      
        rootPane.setCenter(stack);
    }       
    
    
}
