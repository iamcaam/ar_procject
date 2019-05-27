/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import static acrored_vdi_viewer.QueryMachine.IPAddress;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.awt.MouseInfo;
import java.awt.Point;
import org.json.simple.*;
import java.io.*;
import static java.lang.Thread.sleep;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.HPos;
import javafx.scene.layout.TilePane;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane; 
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.StageStyle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.CheckBox;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;                // 2017.06.16 william 使用Java自動化執行工作-時間
import java.util.concurrent.ScheduledExecutorService; // 2017.06.16 william 使用Java自動化執行工作-時間
import java.util.concurrent.TimeUnit;                 // 2017.06.16 william 使用Java自動化執行工作-時間
import java.util.regex.Pattern;
import javafx.animation.AnimationTimer;
import static javafx.beans.binding.Bindings.size;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.FontPosture;
import javafx.stage.Modality;
import javafx.stage.Window;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author duck_chang
 */
public class AcroRed_VDI_Viewer extends Application {
    /***   版本   ***/
    //private static String Ver="( V 3.0.2 )";//( V 1.0.0 )
    private String Ver ;//( V 1.0.0 )
    private String Version_num ;
    /***    顯示文字對應表    ***/
    public Map<String, String> WordMap=new HashMap<>();
    /***  語言與JSON File對應表 ***/
    public Map<String, String> LanguageFileMap=new HashMap<>();
    /***   系統環境宣告   ***/
    private String LangNow;
    private String LangChange;
    public String CurrentIP;
    public String CurrentUserName;
    public String CurrentPasseard;
    /***  以下為顯示元件宣告    ***/
    private TextField userTextField;
    public TextField IP_Addr;
    private Label Language;
    private Label MyTitle;
    private Label L_IP_Addr;
    private Label userName;
    private Label pw;
    private Label Colon1;
    private Label Colon2;
    private Label Colon3;
    private Button Login;
    private Button ManageVD;
    private Button ChangePW;
    private Button ThinClient;
    private Button Web;
    private Button SS; 
    private Button Check;
    private Button Leave;
    private Button Shutdown;
    private Button Reboot;    
    private Button WiFi; 
    private Button Battery;
    private Button Brightness;
    private Stage public_stage;
    //Thread 宣告
    private QueryMachine QM;
    private VDMLogin VDML;
    private CPLogin CPL;
    private TCLogin TCL;
    private webbrowser WebB;
    private LoginAlert LA;
    private VDLoginAlert VDMLA;
    private CPLoginAlert CPLA;
    private NetworkConnect NC;
    private ThinClient TC;
    private DateandTime DAT;
    private TimeSetting TS;
    private Display Display; 
    private GBFunc GBFunc;   
    private GetWifiInfo wifi;
    private BatteryCheck BC;
    private BrightnessControl BrC;    
    public GB GB;     
    public final UUID uniqueKey = UUID.randomUUID();   
    List<Process> processes =FXCollections.observableArrayList();
    List<Process> Web_processes =FXCollections.observableArrayList();
    private Image EyeClose;
    private Image EyeOpen;
    public Image Connect_ON_img;
    public Image Connect_OFF_img;
    public Image WiFi_Connect_ON_img;  
    public Image WiFi_Connect_ON_3_img;
    public Image WiFi_Connect_ON_2_img;
    public Image WiFi_Connect_ON_1_img;
    public Image WiFi_Connect_OFF_img; 
    public ImageView connect_on;
    public ImageView connect_off;
    public ImageView WiFi_connect_on;  
    public ImageView WiFi_connect_on_3;
    public ImageView WiFi_connect_on_2;
    public ImageView WiFi_connect_on_1;
    public ImageView WiFi_connect_off; 
    private ToggleButton buttonPw=new ToggleButton();
    private ToggleButton buttonIP=new ToggleButton();
    private ToggleButton buttonUname=new ToggleButton();
    private  boolean changeAfterSaved = true;
    private  boolean IP_changeAfterSaved = true;
    private  boolean Name_changeAfterSaved = true;
    public  boolean checkBox_Pw_change = false; 
    private  boolean network_change = true;
    public CheckBox checkBox_Pw;                
    private PasswordField pwIP;
    private PasswordField pwName;
    public String Shutdown_params;
    public Process Shutdown_process;
    public String Reboot_params;
    public Process Reboot_process;
    public Process web_process;
    private ComboBox LanguageSelection;
    private TextField textField01;
    public PasswordField pwBox;                 
    private boolean lock_Login = false;
    private boolean lock_ManageVD = false;
    private boolean lock_Shutdown = false;
    private boolean lock_Reboot = false;
    public Button Connect_ON_OFF; 
    private Label Connect_OFF;
    private boolean viewer_alive = false;
    private String time_y_m_d;
    private String time_h_m ;
    private Label time_year_month = new Label();
    private Label time_hour_minute = new Label();
    /*** 提示框***/
    private Tooltip tooltip_Web = new Tooltip();
    private Tooltip tooltip_ThinClient = new Tooltip();
    private Tooltip tooltip_ManageVD = new Tooltip();
    private Tooltip tooltip_ChangePW = new Tooltip();
    private Tooltip tooltip_TimeCalendar = new Tooltip();
    private Tooltip tooltip_Shutdown = new Tooltip();
    private Tooltip tooltip_Reboot = new Tooltip();
    private Tooltip tooltip_Connect_ON_OFF = new Tooltip();    
    private Tooltip tooltip_Battery = new Tooltip(); 
    private Tooltip tooltip_Brightness = new Tooltip(); 
    private double screenCenterX;
    private double screenCenterY;    
    private Tooltip tooltip_SS = new Tooltip(); 
    /** Button 變色效果 **/
    private boolean Login_Color = false;
    private boolean Web_Color = false;
    private boolean ThinClient_Color = false;
    private boolean ManageVD_Color = false;
    private boolean ChangePW_Color = false;
    private boolean Shutdown_Color = false;
    private boolean Reboot_Color = false;
    private boolean Time_Color = false;
    private boolean SS_Color = false; 
    /** 主螢幕 改變 **/
    public boolean Main_Dispaly01_change = true;
    public boolean Main_Dispaly02_change = false;
    private String D01_change;
    private String D02_change;
    private String Display01_name;
    private String Display02_name;
    /** 狀態列 時間 日曆 設定 ToggleButton **/
    private ToggleButton TimeCalendar=new ToggleButton(); 
    private Process MainCurrentDT_process;
    private String MainCurrentDate;
    private String MainCurrentTime;
    private String MainCurrentDate_Year;
    private String MainCurrentDate_Mounth_Eng;
    private String MainCurrentDate_Mounth;
    private String MainCurrentDate_Day;
    private String MainCurrentTime_Hour;
    private String MainCurrentTime_Min;
    private String MainCurrentTime_Sec;
    private Timer timer;    
    private int result; 
    ProgressIndicator pi = new ProgressIndicator(); 
    private int lostwificonnect;
    private boolean runStart = false;  
    private String fs;
    private String returnResult;    
    private TextField IP_Port;                                                  
    private PasswordField pwIP_Port;                                            
    public String CurrentIP_Port;                                               
    private GridPane MainGP;                                                    
    private GridPane LoginGP;                                                   
    private Label Login_title;                                                  
    private BorderPane rootPane;                                                
    public static int CheckPort;                                                
    private String  tasklist_cmd = "ps aux | grep remote-viewer";               
    private Process tasklist_p;                                                 
    public String tasklist_s     = "/root/RemoteViewer/virt/bin/remote-viewer"; 
    public String tasklist_line;                                                
    private boolean IsSnapShot   = false;                                               
    JSONObject SystemStatus;
    JSONObject AfterChange;
    // 2018.01.30 william 雙螢幕實作
    double width_temp;
    double width_current;
    double heigth_temp;
    double heigth_current;
    private Timer listener_timer;
    TimerTask listener_task; 
    Screen screen;
    Rectangle2D bounds;     
    /*********** 雙螢幕實作 - 延伸螢幕 ***********/
    double[][] pos_screen;
    double[][] size_screen;    
    double second_width_current = 0;
    double second_heigth_current = 0;    
    private double xPos = 0;
    private double yPos = 0;        
    private double Shift_xPos = 0;
    private double Shift_yPos = 0;    
    private Stage SecondStage = new Stage();
    Scene Secondscene;
    StackPane Secondroot = new StackPane();
    List<Screen> screens;    
    // For debug 
    File debugFile;    
    private Image Battery_No_Charging_full_img;  
    private Image Battery_No_Charging_3_img;
    private Image Battery_No_Charging_2_img;
    private Image Battery_No_Charging_1_img;
    private Image Battery_No_Charging_0_img; 
    private Image Battery_Charging_full_img;
    private Image Battery_Charging_3_img;
    private Image Battery_Charging_2_img;
    private Image Battery_Charging_1_img;
    private Image Battery_Charging_0_img;     
    ImageView Battery_ImageView        = new ImageView();
    // 2018.05.17 william Brightness實作
    private Image Brightness_img;    
    ImageView Brightness_ImageView;
    private int pow_capacity    = 0; 
    public boolean pow_supply   = false;
    public boolean pow_Charging = false;    
    Point2D tooltip_Battery_p;
    private int tc_p_x    = 0;
    private int sc_p_x    = 0;
    private int en_p_x    = 0;    
    private boolean wifi_enable    = false;  
    private boolean battery_enable = false;      
    public String line_CheckWIFI;
    public String line_CheckBATTERY;    
    boolean Check_Connecting = false;
    public static String ProtocolTypeValue;     
    String ClickColor = "";
    Image  company_Logo;
    ObservableList<String> users = FXCollections.observableArrayList();
    ArrayList<String> _uname = new ArrayList<String>();
    ComboBox UsernameComboBox;     
    public boolean StopMessageQueue = false;    
    public AnimationTimer TextTimer;      
    BlockingQueue<String> messageQueue = null;
    String messageText;
    String message;        
    BlockingQueue<String> messageQueue2 = null;
    String messageText2;
    String message2;            
    Label _dot_String = new Label();
    Executor executor = Executors.newFixedThreadPool(7);
    MessageProducer producer;
    MessageProducer2 producer2;
    public boolean MigrationFlag = false;    
    public boolean ReconnectFlag = false;    
    public boolean ReconnectFlag2 = false;   
    public int MigrationCount = 0;    
    public int CPCount = 0;
    public int CloseViewerCount = 0;
    public boolean CPFlag = false;
    public int MigrationReturnCode = -100;
    public boolean MigrationCloseViewer = true;
    Label _mv1Title = new Label();     
    boolean leaveViewer = false;    
    private Label Login_title2 = new Label();
    private Label Login_title3 = new Label();  
    public AnimationTimer UILockTimer;
    BlockingQueue<String> messageQueueTitle1 = null;
    BlockingQueue<String> messageQueueTitle2 = null;
    BlockingQueue<String> messageQueueTitle3 = null;
    String messageTitle1;
    String messageT1;    
    String messageTitle2;
    String messageT2;
    String messageTitle3;
    String messageT3;    
    public boolean StopLoginMessageQueue = false;    
    MessageTitle1 MessageTitle1;
    MessageTitle2 MessageTitle2;
    MessageDot messageDot;
    
    // Input： ,  Output： , 功能： 檢查IP是否符合正確格式
    private static final Pattern PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    // Input： 主視圖的class,  Output： , 功能： 建構主程式
    @Override
    public void start(Stage primaryStage) throws ParseException, IOException {  
        Display = new Display(public_stage, WordMap); 
        runStart = checkSN(); // Input： ,  Output： , 功能： 檢查MAC Address
        battery_enable = checkBATTERY();
        wifi_enable = checkWIFI(); 
        GB.eth_Str = Get_eth();
        System.out.println("battery_enable:" + battery_enable + ", wifi_enable:" + wifi_enable + "\n");
        public_stage=primaryStage;
        QM=new QueryMachine(WordMap); //建立呼叫HTTP GET　POST REST JSON 的 Class
        VDML=new VDMLogin(WordMap); //建立呼叫HTTP GET　POST REST JSON 的 Class
        CPL=new CPLogin(WordMap); //建立呼叫HTTP GET　POST REST JSON 的 Class
        TCL=new TCLogin(WordMap); //建立呼叫HTTP GET　POST REST JSON 的 Class
        WebB=new webbrowser(WordMap);
        LA=new LoginAlert(WordMap); //建立呼叫HTTP GET　POST REST JSON 的 Class
        VDMLA=new VDLoginAlert(WordMap); //建立呼叫HTTP GET　POST REST JSON 的 Class
        CPLA=new CPLoginAlert(WordMap); //建立呼叫HTTP GET　POST REST JSON 的 Class
        NC =new NetworkConnect(WordMap); //建立呼叫HTTP GET　POST REST JSON 的 Class
       
        BC = new BatteryCheck(WordMap); // 2018.04.26 william Battery實作        
        BrC = new BrightnessControl(WordMap);
        
        GBFunc  = new GBFunc(WordMap);                // 2018.03.05 william 雙螢幕實作
        
        // 20019.02.18 新增FreeRDP for dual screen & usb redirection
        GB.FreeRDPEnable = false;            
        
        // 2019.03.28 VM/VD migration reconnect
        messageQueue = new ArrayBlockingQueue<>(1);  
        messageQueue2 = new ArrayBlockingQueue<>(1);  
        MigrationLock_Renew_String();
        producer = new MessageProducer(messageQueue);
        producer2 = new MessageProducer2(messageQueue2);
        executor.execute(new checkMigration());  
        executor.execute(new checkConnectionProcess());  
         
        // 2019.05.13 單一VD登入UI顯示
        messageQueueTitle1 = new ArrayBlockingQueue<>(1);  
        messageQueueTitle2 = new ArrayBlockingQueue<>(1);    
        messageQueueTitle3 = new ArrayBlockingQueue<>(1);
        LoginLock_Renew_String();
        MessageTitle1 = new MessageTitle1(messageQueueTitle1);
        MessageTitle2 = new MessageTitle2(messageQueueTitle2);
        messageDot = new MessageDot(messageQueueTitle3);
        executor.execute(MessageTitle1);  
        executor.execute(MessageTitle2);  
        executor.execute(messageDot);               
        
        InitailLanguageFileMap();  //初始化語言與JSON對應，每次新增語言必須修改出使化程式     
        LangNow="繁體中文";   //若沒有預設語言，以繁體中文為主       
        LoadLastStatus();   //讀出上一次的系統設定、連接伺服器IP、帳號
        LoadLanguageMapFile(LanguageFileMap.get(LangNow)); // 設定這次啟動的語言，建議每一個元件的文字
        /************顯示 明碼 暗碼*************/
        EyeClose=new Image("images/CloseEye.png",22,22,false, false);     
        EyeOpen=new Image("images/OpenEye5.png",22,22,false, false);
        
        IP_changeAfterSaved =true;   //若沒有預設明碼或暗碼，以明碼為主  
        buttonIP.setGraphic(new ImageView(EyeOpen));
       
        Name_changeAfterSaved =true; //若沒有預設明碼或暗碼，以明碼為主
        buttonIP.setGraphic(new ImageView(EyeOpen));        
        LoadLastChange();
        LoadVersion();
        
        // 2019.01.02 Remember10 username
        LoadUserNameJson();
        
//        Ver="( V " + Version_num + " )_build2"; //( V 3.0.7 )
        Ver="( V " + Version_num + " )"; 
        
//        LoadDispalyChange();
//        
//        if( D01_change.equals("true") ){
//            Main_Dispaly01_change=true;
//        }
//        if( D01_change.equals("false") ){
//            Main_Dispaly01_change=false;
//        }
//        if( D02_change.equals("true") ){
//            Main_Dispaly02_change=true;
//        }
//        if( D02_change.equals("false") ){
//            Main_Dispaly02_change=false;
//        }
//        
//        
//        ChecktwoDispaly(); //一開始若已接好雙螢幕 , 設定HDMI為主螢幕畫面
       
//        TC.LoadDispalyChange();
//        Main_Dispaly01_change=TC.Dispaly01_change;
//        Main_Dispaly02_change=TC.Dispaly02_change;
        
        /***  主畫面使用GRID的方式Layout ***/
        MainGP=new GridPane();  //550, 380 // 2017.09.13 william 登入中thread畫面鎖住
        //MainGP.setGridLinesVisible(true); //開啟或關閉表格的分隔線       
        MainGP.setAlignment(Pos.CENTER);       
        MainGP.setPadding(new Insets(35, 35, 50, 31)); //設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 21
        MainGP.setHgap(5); //元件間的水平距離
   	MainGP.setVgap(5); //元件間的垂直距離        
        MainGP.setPrefSize(590, 365); //680, 400
        MainGP.setMaxSize(590, 365);
        MainGP.setMinSize(590, 365); 
        MainGP.setStyle("-fx-background-color: rgb(0,180,240);-fx-background-radius: 4;-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 10, 0.0 , 0 , 5 );");
        
        /***  功能(下)畫面使用Anchor的方式Layout ***/
        AnchorPane anchorpane = new AnchorPane();       
        anchorpane.setStyle("-fx-background-color: #1a237e ;");
        anchorpane.setPrefHeight(60);
        anchorpane.setMaxHeight(60);
        anchorpane.setMinHeight(60);
        
        /***  底層畫面使用Border的方式Layout ***/
        rootPane = new BorderPane();  // 2017.09.13 william 登入中thread畫面鎖住    
        rootPane.setCenter(MainGP);
        rootPane.setBottom(anchorpane);
        rootPane.setStyle("-fx-background-color: #1565c0 ;");
        
        
        
        // 2017.06.29 william 新增 viewer bind mac address (Process Start Info Check SN)
        System.out.println("********************************* runStart:" + runStart + "****************************************\n");
        if(!runStart) {
            Label AlertMessage = new Label("系統錯誤，即將關閉");
            GridPane.setHalignment(AlertMessage, HPos.LEFT);      
            MainGP.add(AlertMessage, 0, 3);
            AlertMessage.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)

            AlertMessage.setId("Logo");
               
            Runnable helloRunnable = new Runnable() {
                public void run() {
                    try {
//                      System.out.println("----------結束應用程式----------");
//                      System.exit(1);
                        Process Shutdown_process_CL = Runtime.getRuntime().exec("poweroff");
                    } catch (IOException ex) {
                        Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
        };
        
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 10, 1, TimeUnit.SECONDS);
        
        }
        else { // 2017.06.29 william 新增 viewer bind mac address (Process Start Info Check SN)

        /***  AcroRed Logo ***/
        /* 採用圖檔*/
        /*ImageView IV=new ImageView();
        IV.setImage(new Image("file:AcroRed_Logo.png"));
        IV.setFitHeight(36);
        IV.setFitWidth(200);
        
        MainGP.add(IV,0,0,2,1);*/
        /* 採用文字*/
        Label Logo=new Label();
        Logo.setAlignment(Pos.CENTER_LEFT);
 
        switch (GB.JavaVersion) {
            case 0:
                company_Logo=new Image("images/Logo.png"); 
                break;
            case 1:
                company_Logo=new Image("images/MCLogo.png"); 
                Logo.setTranslateX(-5);
                break;                
            default:
                break;
        }          
        
      
        Logo.setGraphic(new ImageView(company_Logo));
        Logo.setMaxHeight(35);
        Logo.setMinHeight(35);
        Logo.setPrefHeight(35);
        
//        Logo.setId("Logo");
        //Logo.setFont(Font.font("Liberation Serif", FontPosture.ITALIC, 32));
        MainGP.add(Logo,0,0,3,1); //2,1是指這個元件要佔用2格的column和1格的row     
          
          
         /***  多國語言設定UI ***/
        Language=new Label(WordMap.get("Language")+"  :");
        Language.setAlignment(Pos.CENTER_RIGHT);
        ObservableList<String> LanguageList=FXCollections.observableArrayList("繁體中文", "简体中文", "English"); //  , "日本語"
        LanguageSelection=new ComboBox(LanguageList);
        //LanguageList.getClass().getResource(FontList.getStyle());
        /***  以下抓取變更語言後的動作  ***/
        LanguageSelection.setOnAction((event) -> {       
            try {
                LangComboChanged(LanguageSelection.getValue().toString());  //處理語言變更
            } catch (ParseException ex) {
                //Logger.getLogger(JavaUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        LanguageSelection.getSelectionModel().select(LangNow);
        
        /***  語言設定列 放入一個HBOX內 ***/
        HBox LanguageBar=new HBox();
        LanguageBar.setAlignment(Pos.CENTER_RIGHT);
        LanguageBar.setSpacing(5);
        LanguageBar.getChildren().addAll(Language, LanguageSelection);
        MainGP.add(LanguageBar, 2,0,9,1);  // (2,0,1,1)1,0,2,1表示放在GRID 0 0 的位置，寬度跨越2個Colume，高度1個Row
     
        // 2018.11.11 新增AcroDesk & RDP Protocol        
        
//        ObservableList<String> ProtocolType = FXCollections.observableArrayList("AcroDesk", "RDP");
//        ComboBox ProtocolSelection = new ComboBox(ProtocolType);
//        ProtocolSelection.setOnAction((event) -> {
//            setProtocol(ProtocolSelection.getValue().toString());
//            /*
//            switch (ProtocolSelection.getValue().toString()) {
//                case "AcroDesk":
//                    IP_Port.setText("443");
//                    break;
//                case "RDP":
//                    IP_Port.setText("3389");
//                    break;       
//                default: 
//                    break;               
//            }
//                        
//            System.out.println("---Protocol Selection : ---" + getProtocol()); 
//            */
//        });
//        setProtocol("AcroDesk");
//        ProtocolSelection.getSelectionModel().select("AcroDesk");      
//        
//        HBox ProtocolHbox = new HBox();
//        ProtocolHbox.setAlignment(Pos.CENTER_LEFT);
//        ProtocolHbox.setSpacing(5);      
//        ProtocolHbox.getChildren().addAll(ProtocolSelection);
//        MainGP.add(ProtocolHbox, 0, 12, 2, 1);        
        
        /***  Title 放入一個HBOX內 ***/
        MyTitle=new Label(WordMap.get("Title"));
        MyTitle.setId("Title");
        
        HBox WC=new HBox(MyTitle);
        WC.setAlignment(Pos.TOP_LEFT);
        //GridPane.setHalignment(MyTitle, HPos.LEFT);
        WC.setPadding(new Insets(0, 0, 0, 12));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        //WC.setSpacing(10); 
        MainGP.add(WC,1,2,9, 1);//1121
        
        Label VerNum = new Label(Ver);
        VerNum.setId("Ver_namber");
        //VerNum.setAlignment(Pos.BASELINE_LEFT);
        VerNum.setTranslateX(280); 
        VerNum.setTranslateY(10); 
        MainGP.add(VerNum,1,2,9, 1);//1121

        /*******************  輸入VDI Server 的IP位址 ************************/       
        L_IP_Addr = new Label(WordMap.get("IP_Addr"));//+":"    
        GridPane.setHalignment(L_IP_Addr, HPos.LEFT);      
        L_IP_Addr.setMaxWidth(100);//強制限制Label最大寬度,讓每個Label在變換語言時不會變動
        L_IP_Addr.setMinWidth(100);//強制限制Label最小寬度,讓每個Label在變換語言時不會變動
        L_IP_Addr.setPrefWidth(100);//強制限制Label預設寬度,讓每個Label在變換語言時不會變動
        //L_IP_Addr.setPrefSize(90, 36);
       // L_IP_Addr.setMaxSize(90, 36);
        //L_IP_Addr.setMinSize(90, 36);
	MainGP.add(L_IP_Addr, 0, 3);
        L_IP_Addr.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        
        Colon1=new Label(":");//將":"固定在一個位置
        GridPane.setHalignment(Colon1, HPos.LEFT);
        MainGP.add(Colon1, 1, 3); 
        Colon1.setPadding(new Insets(0, 5, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)             

        /*******輸入VDI Server 的IP位址*******明碼 與 暗碼 轉換*************/
        pwIP = new PasswordField();
         // Set initial state
        //pwIP.setManaged(false);
        //pwIP.setVisible(false);        
//	MainGP.add(pwIP,2, 3);  // 2017.08.10 william IP增加port欄位，預設443 取消功能
        pwIP.setPrefSize(258, 39); // 2017.08.10 william IP增加port欄位，預設443 338 -> 258
        pwIP.setMaxSize(258, 39); // 2017.08.10 william IP增加port欄位，預設443 338 -> 258
        pwIP.setMinSize(258, 39); // 2017.08.10 william IP增加port欄位，預設443 338 -> 258
        
        IP_Addr = new TextField(CurrentIP);          
//	MainGP.add(IP_Addr,2, 3);
        IP_Addr.setPrefSize(258, 39); // 2017.08.10 william IP增加port欄位，預設443 338 -> 258
        IP_Addr.setMaxSize(258, 39); // 2017.08.10 william IP增加port欄位，預設443 338 -> 258
        IP_Addr.setMinSize(258, 39);  // 2017.08.10 william IP增加port欄位，預設443 338 -> 258
        
        // 2017.08.10 william IP增加port欄位，預設443
        IP_Port = new TextField(CurrentIP_Port);
        IP_Port.setAlignment(Pos.CENTER);
        IP_Port.setPrefSize(80, 39); 
        IP_Port.setMaxSize(80, 39); 
        IP_Port.setMinSize(80, 39); 
        
        pwIP_Port = new PasswordField();
        pwIP_Port.setAlignment(Pos.CENTER);
        pwIP_Port.setPrefSize(80, 39); 
        pwIP_Port.setMaxSize(80, 39); 
        pwIP_Port.setMinSize(80, 39);         
        
        // 2017.08.10 william IP增加port欄位，預設443
        HBox IPBox=new HBox();
        IPBox.getChildren().addAll(pwIP,IP_Addr,IP_Port,pwIP_Port);
        IPBox.setAlignment(Pos.CENTER_LEFT);
        IPBox.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        IPBox.setSpacing(0); 
        MainGP.add(IPBox, 2, 3,1,1);  
      
        //buttonIP.setVisible(false);    
        //buttonIP.setGraphic(new ImageView(EyeOpen));     
        //buttonIP.setStyle("-fx-background-color: white;  ");            
        MainGP.add(buttonIP,2, 3);      
        buttonIP.setAlignment(Pos.CENTER);        
        buttonIP.setTranslateX(339);  //移動TextField內的Button位置        
        buttonIP.setPrefHeight(37);
        buttonIP.setMaxHeight(37);
        buttonIP.setMinHeight(37);
        //buttonIP.setId("ToggleButton");
        
        /************讀取--IP_changeAfterSaved--為明碼或暗碼 若明碼為true,暗碼為false**************/
        if(IP_changeAfterSaved==true){
            
            pwIP.setManaged(false);
            pwIP.setVisible(false);   
            
            pwIP_Port.setManaged(false); // 2017.08.10 william IP增加port欄位，預設443
            pwIP_Port.setVisible(false); // 2017.08.10 william IP增加port欄位，預設443
            
            buttonIP.setGraphic(new ImageView(EyeOpen));
            
            buttonIP.setOnAction(new EventHandler<ActionEvent>() {
                @Override        
                public void handle(ActionEvent e) {                     
                    if(IP_changeAfterSaved==true){                   

                        if (e.getEventType().equals(ActionEvent.ACTION)){                    

                            pwIP.managedProperty().bind(buttonIP.selectedProperty());
                            pwIP.visibleProperty().bind(buttonIP.selectedProperty());
                            pwIP_Port.managedProperty().bind(buttonIP.selectedProperty()); // 2017.08.10 william IP增加port欄位，預設443
                            pwIP_Port.visibleProperty().bind(buttonIP.selectedProperty()); // 2017.08.10 william IP增加port欄位，預設443                           

                            IP_Addr.managedProperty().bind(buttonIP.selectedProperty().not());
                            IP_Addr.visibleProperty().bind(buttonIP.selectedProperty().not());
                            IP_Port.managedProperty().bind(buttonIP.selectedProperty().not()); // 2017.08.10 william IP增加port欄位，預設443
                            IP_Port.visibleProperty().bind(buttonIP.selectedProperty().not()); // 2017.08.10 william IP增加port欄位，預設443                           
                            pwIP.managedProperty().bind(buttonIP.selectedProperty());
                            pwIP.visibleProperty().bind(buttonIP.selectedProperty());
                            pwIP_Port.managedProperty().bind(buttonIP.selectedProperty()); // 2017.08.10 william IP增加port欄位，預設443
                            pwIP_Port.visibleProperty().bind(buttonIP.selectedProperty()); // 2017.08.10 william IP增加port欄位，預設443                           

                            buttonIP.setGraphic(new ImageView(EyeClose));
                        } 

                        IP_changeAfterSaved=false;

                    }           
                    else{

                        if (e.getEventType().equals(ActionEvent.ACTION)){                 

                            IP_Addr.managedProperty().bind(buttonIP.selectedProperty().not());
                            IP_Addr.visibleProperty().bind(buttonIP.selectedProperty().not());
                            IP_Port.managedProperty().bind(buttonIP.selectedProperty().not()); // 2017.08.10 william IP增加port欄位，預設443
                            IP_Port.visibleProperty().bind(buttonIP.selectedProperty().not()); // 2017.08.10 william IP增加port欄位，預設443                           

                            pwIP.managedProperty().bind(buttonIP.selectedProperty());
                            pwIP.visibleProperty().bind(buttonIP.selectedProperty());
                            pwIP_Port.managedProperty().bind(buttonIP.selectedProperty()); // 2017.08.10 william IP增加port欄位，預設443
                            pwIP_Port.visibleProperty().bind(buttonIP.selectedProperty()); // 2017.08.10 william IP增加port欄位，預設443                           
                            IP_Addr.managedProperty().bind(buttonIP.selectedProperty().not());
                            IP_Addr.visibleProperty().bind(buttonIP.selectedProperty().not());
                            IP_Port.managedProperty().bind(buttonIP.selectedProperty().not()); // 2017.08.10 william IP增加port欄位，預設443
                            IP_Port.visibleProperty().bind(buttonIP.selectedProperty().not()); // 2017.08.10 william IP增加port欄位，預設443                           

                            buttonIP.setGraphic(new ImageView(EyeOpen));   
                        } 

                        IP_changeAfterSaved=true;

                    }
                    //IP_Addr.textProperty().bindBidirectional(pwIP.textProperty());
                    pwIP.textProperty().bindBidirectional(IP_Addr.textProperty());           
                    pwIP_Port.textProperty().bindBidirectional(IP_Port.textProperty()); // 2017.08.10 william IP增加port欄位，預設443 
                    //System.out.println("IP_changeAfterSaved : "+IP_changeAfterSaved+"\n");
                    WriteLastChange(); //記錄明碼與暗碼
                }
            });
                   
        }else{
            
            IP_Addr.setManaged(false);
            IP_Addr.setVisible(false);
            IP_Port.setManaged(false); // 2017.08.10 william IP增加port欄位，預設443 
            IP_Port.setVisible(false); // 2017.08.10 william IP增加port欄位，預設443                
            buttonIP.setGraphic(new ImageView(EyeClose));
            pwIP.setText(CurrentIP); 
            pwIP_Port.setText(CurrentIP_Port); // 2017.08.10 william IP增加port欄位，預設443                
            IP_changeAfterSaved=false;
            
            buttonIP.setOnAction(new EventHandler<ActionEvent>() {
                @Override        
                public void handle(ActionEvent e) {
                    
                    if(IP_changeAfterSaved==true){ 
                        //System.out.println("---Call--IP_changeAfterSaved==true--------\n"); 
                        //buttonIP.setGraphic(new ImageView(EyeOpen));
                        if (e.getEventType().equals(ActionEvent.ACTION)){                    

                            pwIP.managedProperty().bind(buttonIP.selectedProperty().not());
                            pwIP.visibleProperty().bind(buttonIP.selectedProperty().not());
                            pwIP_Port.managedProperty().bind(buttonIP.selectedProperty().not()); // 2017.08.10 william IP增加port欄位，預設443                
                            pwIP_Port.visibleProperty().bind(buttonIP.selectedProperty().not()); // 2017.08.10 william IP增加port欄位，預設443                                           

                            IP_Addr.managedProperty().bind(buttonIP.selectedProperty());
                            IP_Addr.visibleProperty().bind(buttonIP.selectedProperty());
                            IP_Port.managedProperty().bind(buttonIP.selectedProperty()); // 2017.08.10 william IP增加port欄位，預設443                                           
                            IP_Port.visibleProperty().bind(buttonIP.selectedProperty()); // 2017.08.10 william IP增加port欄位，預設443                                                                      
                            pwIP.managedProperty().bind(buttonIP.selectedProperty().not());
                            pwIP.visibleProperty().bind(buttonIP.selectedProperty().not());
                            pwIP_Port.managedProperty().bind(buttonIP.selectedProperty().not()); // 2017.08.10 william IP增加port欄位，預設443                                                                      
                            pwIP_Port.visibleProperty().bind(buttonIP.selectedProperty().not()); // 2017.08.10 william IP增加port欄位，預設443                                                                                                 

                            buttonIP.setGraphic(new ImageView(EyeClose));
                        } 

                    IP_changeAfterSaved=false; 

                    }           
                    else{                           
                        if (e.getEventType().equals(ActionEvent.ACTION)){ 

                            IP_Addr.managedProperty().bind(buttonIP.selectedProperty());
                            IP_Addr.visibleProperty().bind(buttonIP.selectedProperty());
                            IP_Port.managedProperty().bind(buttonIP.selectedProperty()); // 2017.08.10 william IP增加port欄位，預設443                                                                                                 
                            IP_Port.visibleProperty().bind(buttonIP.selectedProperty()); // 2017.08.10 william IP增加port欄位，預設443                                                                                                                            

                            pwIP.managedProperty().bind(buttonIP.selectedProperty().not());
                            pwIP.visibleProperty().bind(buttonIP.selectedProperty().not());
                            pwIP_Port.managedProperty().bind(buttonIP.selectedProperty().not()); // 2017.08.10 william IP增加port欄位，預設443                                                                                                                            
                            pwIP_Port.visibleProperty().bind(buttonIP.selectedProperty().not()); // 2017.08.10 william IP增加port欄位，預設443                                                                                                                                                       
                            IP_Addr.managedProperty().bind(buttonIP.selectedProperty());
                            IP_Addr.visibleProperty().bind(buttonIP.selectedProperty());
                            IP_Port.managedProperty().bind(buttonIP.selectedProperty()); // 2017.08.10 william IP增加port欄位，預設443                                                                                                                                                       
                            IP_Port.visibleProperty().bind(buttonIP.selectedProperty()); // 2017.08.10 william IP增加port欄位，預設443                                                                                                                                                                                  

                            buttonIP.setGraphic(new ImageView(EyeOpen));   
                        } 

                    IP_changeAfterSaved=true;

                    }

                IP_Addr.textProperty().bindBidirectional(pwIP.textProperty());
                IP_Port.textProperty().bindBidirectional(pwIP_Port.textProperty()); // 2017.08.10 william IP增加port欄位，預設443                                                                                                                                                                                  
                //pwIP.textProperty().bindBidirectional(IP_Addr.textProperty());           
                //System.out.println("IP_changeAfterSaved : "+IP_changeAfterSaved+"\n");
                WriteLastChange(); //記錄明碼與暗碼
                }
            });
        }
      
        /****************  輸入使用者帳號 ********************/
	userName = new Label(WordMap.get("Username"));//+":"
        
        GridPane.setHalignment(userName, HPos.LEFT);         
	MainGP.add(userName, 0, 6);
        //userName.setPadding(new Insets(0, 0, 0, 10));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        userName.setMaxWidth(100);//強制限制Label最大寬度,讓每個Label在變換語言時不會變動
        userName.setMinWidth(100);//強制限制Label最小寬度,讓每個Label在變換語言時不會變動
        userName.setPrefWidth(100);//強制限制Label預設寬度,讓每個Label在變換語言時不會變動
        
        Colon2=new Label(":");//將":"固定在一個位置
        //Colon2.setPadding(new Insets(0, 0, 0, 0)); //設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        GridPane.setHalignment(Colon2, HPos.LEFT);        
        MainGP.add(Colon2, 1, 6);        
        
        // 2019.01.02 Remember10 username
        
        UsernameComboBox = new ComboBox(users);
             
        UsernameComboBox.setPrefSize(338, 39);
        UsernameComboBox.setMaxSize(338, 39);
        UsernameComboBox.setMinSize(338, 39);
        
        UsernameComboBox.setEditable(true);
        
//        UsernameComboBox.getSelectionModel().selectFirst();
        
        UsernameComboBox.setValue(CurrentUserName);
        
        MainGP.add(UsernameComboBox,2, 6);
        UsernameComboBox.setOnAction((event) -> {

        });        

         /**********輸入使用者帳號*********明碼 與 暗碼 轉換*************/
        pwName = new PasswordField();
        // Set initial state
        //pwName.setManaged(false);
        //pwName.setVisible(false);
	MainGP.add(pwName,2, 6);
        pwName.setPrefSize(338, 39);
        pwName.setMaxSize(338, 39);
        pwName.setMinSize(338, 39);
        
        userTextField = new TextField(CurrentUserName);      
//	MainGP.add(userTextField, 2, 6);
        userTextField.setPrefSize(338, 39);
        userTextField.setMaxSize(338, 39);
        userTextField.setMinSize(338, 39);
        
        //buttonUname.setVisible(false); 
        //buttonUname.setGraphic(new ImageView(EyeOpen));
        MainGP.add(buttonUname,2, 6);     
        //buttonUname.setStyle("-fx-background-color: white;");
        buttonUname.setAlignment(Pos.CENTER);       
        buttonUname.setTranslateX(339);  //移動TextField內的Button位置 
        buttonUname.setPrefHeight(37);
        buttonUname.setMaxHeight(37);
        buttonUname.setMinHeight(37);

        /************讀取--Name_changeAfterSaved--為明碼或暗碼 若明碼為true,暗碼為false**************/
        if(Name_changeAfterSaved==true){
            // Set initial state
            pwName.setManaged(false);
            pwName.setVisible(false);        
            buttonUname.setGraphic(new ImageView(EyeOpen));
            
            buttonUname.setOnAction(new EventHandler<ActionEvent>() {
                @Override        
                public void handle(ActionEvent e) {

                    if(Name_changeAfterSaved == true){              
                        if (e.getEventType().equals(ActionEvent.ACTION)){                    

                            pwName.managedProperty().bind(buttonUname.selectedProperty());
                            pwName.visibleProperty().bind(buttonUname.selectedProperty());

                            /*
                            userTextField.managedProperty().bind(buttonUname.selectedProperty().not());
                            userTextField.visibleProperty().bind(buttonUname.selectedProperty().not());                    
                            */
                            UsernameComboBox.managedProperty().bind(buttonUname.selectedProperty().not());
                            UsernameComboBox.visibleProperty().bind(buttonUname.selectedProperty().not());                            
                            
                            pwName.managedProperty().bind(buttonUname.selectedProperty());
                            pwName.visibleProperty().bind(buttonUname.selectedProperty());
                            
                            buttonUname.setGraphic(new ImageView(EyeClose));
                            
                            UsernameComboBox.setDisable(true);
                        } 

                        Name_changeAfterSaved=false;

                    }           
                    else{              
                        if (e.getEventType().equals(ActionEvent.ACTION)){                   
                            /*
                            userTextField.managedProperty().bind(buttonUname.selectedProperty().not());
                            userTextField.visibleProperty().bind(buttonUname.selectedProperty().not());
                            */
                            UsernameComboBox.managedProperty().bind(buttonUname.selectedProperty().not());
                            UsernameComboBox.visibleProperty().bind(buttonUname.selectedProperty().not());                            
                            
                            pwName.managedProperty().bind(buttonUname.selectedProperty());
                            pwName.visibleProperty().bind(buttonUname.selectedProperty());
                            
                            /*
                            userTextField.managedProperty().bind(buttonUname.selectedProperty().not());
                            userTextField.visibleProperty().bind(buttonUname.selectedProperty().not());
                            */
                            UsernameComboBox.managedProperty().bind(buttonUname.selectedProperty().not());
                            UsernameComboBox.visibleProperty().bind(buttonUname.selectedProperty().not());                             

                            buttonUname.setGraphic(new ImageView(EyeOpen));
                            
                            UsernameComboBox.setDisable(false);
                        }                 

                        Name_changeAfterSaved=true;       

                    }
                    /*
                    pwName.textProperty().bindBidirectional(userTextField.textProperty());       
                    */
                    pwName.textProperty().bindBidirectional(UsernameComboBox.valueProperty());           
                    //System.out.println("Name_changeAfterSaved-final : "+Name_changeAfterSaved+"\n");
                    WriteLastChange(); //記錄明碼與暗碼 
                }
            });
                 
        }else{
            // Set initial state
            userTextField.setManaged(false);
            userTextField.setVisible(false);        
            buttonUname.setGraphic(new ImageView(EyeClose));
            pwName.setText(CurrentUserName);
            Name_changeAfterSaved=false;
            
            buttonUname.setOnAction(new EventHandler<ActionEvent>() {
                @Override        
                public void handle(ActionEvent e) {

                    if(Name_changeAfterSaved == true){              
                        if (e.getEventType().equals(ActionEvent.ACTION)){                   
                            
                            pwName.managedProperty().bind(buttonUname.selectedProperty().not());
                            pwName.visibleProperty().bind(buttonUname.selectedProperty().not());
                            
                            /*
                            userTextField.managedProperty().bind(buttonUname.selectedProperty());
                            userTextField.visibleProperty().bind(buttonUname.selectedProperty());                    
                            */
                            UsernameComboBox.managedProperty().bind(buttonUname.selectedProperty());
                            UsernameComboBox.visibleProperty().bind(buttonUname.selectedProperty()); 
                            
                            pwName.managedProperty().bind(buttonUname.selectedProperty().not());
                            pwName.visibleProperty().bind(buttonUname.selectedProperty().not());
                            buttonUname.setGraphic(new ImageView(EyeClose));
                            
                            UsernameComboBox.setDisable(true);
                        } 

                        Name_changeAfterSaved=false;

                    }           
                    else{             
                        if (e.getEventType().equals(ActionEvent.ACTION)){                   
                            /*
                            userTextField.managedProperty().bind(buttonUname.selectedProperty());
                            userTextField.visibleProperty().bind(buttonUname.selectedProperty());
                            */
                            UsernameComboBox.managedProperty().bind(buttonUname.selectedProperty());
                            UsernameComboBox.visibleProperty().bind(buttonUname.selectedProperty());                            
                                                        
                            pwName.managedProperty().bind(buttonUname.selectedProperty().not());
                            pwName.visibleProperty().bind(buttonUname.selectedProperty().not());
                            
                            /*
                            userTextField.managedProperty().bind(buttonUname.selectedProperty());
                            userTextField.visibleProperty().bind(buttonUname.selectedProperty());
                            */
                            UsernameComboBox.managedProperty().bind(buttonUname.selectedProperty());
                            UsernameComboBox.visibleProperty().bind(buttonUname.selectedProperty()); 
                            
                            buttonUname.setGraphic(new ImageView(EyeOpen));   
                            
                            UsernameComboBox.setDisable(false);
                        }   

                        Name_changeAfterSaved=true;   

                    }
                    /*
                    userTextField.textProperty().bindBidirectional(pwName.textProperty());           
                    */
                    UsernameComboBox.valueProperty().bindBidirectional(pwName.textProperty());
                    
                    //System.out.println("Name_changeAfterSaved-final : "+Name_changeAfterSaved+"\n");
                    WriteLastChange(); //記錄明碼與暗碼 
                }
            });
        }
        
        /**********************  輸入密碼 ***********************/
        pw = new Label(WordMap.get("Password"));//+":"
        GridPane.setHalignment(pw, HPos.LEFT);
	 MainGP.add(pw, 0, 9);
        //pw.setPadding(new Insets(0, 0, 0, 10));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)      
        pw.setMaxWidth(100);//強制限制Label最大寬度,讓每個Label在變換語言時不會變動
        pw.setMinWidth(100);//強制限制Label最小寬度,讓每個Label在變換語言時不會變動
        pw.setPrefWidth(100);//強制限制Label預設寬度,讓每個Label在變換語言時不會變動
        
        Colon3=new Label(":");//將":"固定在一個位置      
        GridPane.setHalignment(Colon3, HPos.LEFT);
        MainGP.add(Colon3, 1, 9);
        
        /*******輸入密碼********明碼 與 暗碼 轉換*******************/       
    	pwBox = new PasswordField();        
	MainGP.add(pwBox,2, 9);
        pwBox.setPrefSize(338, 39);
        pwBox.setMaxSize(338, 39);
        pwBox.setMinSize(338, 39);

        // Bind the textField and passwordField text values bidirectionally.
        textField01 = new TextField();
        // Set initial state
        textField01.setManaged(false);
        textField01.setVisible(false);
        MainGP.add(textField01,2, 9); 
        textField01.setPrefSize(338, 39);
        textField01.setMaxSize(338, 39);
        textField01.setMinSize(338, 39);
        
        
        buttonPw.setGraphic(new ImageView(EyeClose));
        MainGP.add(buttonPw,2, 9);     
        //buttonPw.setStyle("-fx-background-color: white;");
        //buttonPw.visibleProperty().bind( pwBox.textProperty().isEmpty().not() );
        buttonPw.setAlignment(Pos.CENTER);
        buttonPw.setTranslateX(339); //移動TextField內的Button位置
        buttonPw.setPrefHeight(37);
        buttonPw.setMaxHeight(37);
        buttonPw.setMinHeight(37);
     
        buttonPw.setOnAction(new EventHandler<ActionEvent>() {
            @Override        
            public void handle(ActionEvent e) {
            
                if(changeAfterSaved==true){ 

                    if (e.getEventType().equals(ActionEvent.ACTION)){                    

                        textField01.managedProperty().bind(buttonPw.selectedProperty());
                        textField01.visibleProperty().bind(buttonPw.selectedProperty());            
                        buttonPw.setGraphic(new ImageView(EyeOpen));
                        //System.out.println("------textField------ACTION----------- \n");

                        changeAfterSaved=false;
                    } 

                changeAfterSaved=false;

                }           
                else{

                    if (e.getEventType().equals(ActionEvent.ACTION)){                   

                        pwBox.managedProperty().bind(buttonPw.selectedProperty().not());
                        pwBox.visibleProperty().bind(buttonPw.selectedProperty().not());             
                        buttonPw.setGraphic(new ImageView(EyeClose));
                        //System.out.println("--------pwBox------ACTION-------------- \n");

                        changeAfterSaved=true;
                    } 

                changeAfterSaved=true;

                }
            
            textField01.textProperty().bindBidirectional(pwBox.textProperty());                       
            //System.out.println("changeAfterSaved : "+changeAfterSaved+"\n");
         
        }
        });
 
       /****************checkBox**************/
       /*
        CheckBox checkBox = new CheckBox("T");
        MainGP.add(checkBox,3, 8);
        textField.managedProperty().bind(checkBox.selectedProperty());//textField.managedProperty().bind(checkBox.selectedProperty());            
        textField.visibleProperty().bind(checkBox.selectedProperty());//textField.visibleProperty().bind(checkBox.selectedProperty());        
       
        pwBox.managedProperty().bind(checkBox.selectedProperty().not());//pwBox.managedProperty().bind(checkBox.selectedProperty().not());
        pwBox.visibleProperty().bind(checkBox.selectedProperty().not());//pwBox.managedProperty().bind(checkBox.selectedProperty().not());       
             
        textField.textProperty().bindBidirectional(pwBox.textProperty());
       
        MainGP.add(textField,2, 8);  
       */
        /***  登入處理 ***/
        Login=new Button(WordMap.get("Login"));  
        Login.setPrefSize(160,35);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        Login.setMaxSize(160,35);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        Login.setMinSize(160,35);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動
        Login.setId("Loginbutton");
        //Login.setTranslateX(217);//215
        checkBox_Pw = new CheckBox(WordMap.get("Read_password"));        
        
        Login.setOnAction((ActionEvent event) -> {      
            Login_func(false, false); // 2019.01.02 view D Drive
        });
         
          /***  以下擺放操作功能 ***/
        //HBox LoginBox=new HBox(Login);
        HBox LoginBox=new HBox();
        LoginBox.getChildren().addAll(Login, checkBox_Pw);
        LoginBox.setAlignment(Pos.CENTER_LEFT);
        LoginBox.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        LoginBox.setSpacing(10); 
        MainGP.add(LoginBox, 2, 12,9,1);//2,11,3,2

        //MainGP.add(checkBox_Pw, 2, 11);//2,11,3,2
        //MainGP.add(Login, 2, 11);//2,11,3,2
        
         /*******瘦客戶機設定******/
        //ThinClient=new Button(WordMap.get("ThinClient"));
        GB.lock_ThinClient = false; // 2017.06.09 william Disable click many times will show mutil Dialog
        Image ThinClient_img = new Image("images/Setup_45.png"); //Setup  Thinkclient002
        ThinClient = new Button();
        ThinClient.setGraphic(new ImageView(ThinClient_img));
        ThinClient.setPrefSize(60,60);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動 190,35
        ThinClient.setMaxSize(60,60);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動 190,35
        ThinClient.setMinSize(60,60);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動  190,35    
        ThinClient.setAlignment(Pos.CENTER);
//        tooltip_ThinClient = new Tooltip();// imageButton提示框
//        tooltip_ThinClient.setText(WordMap.get("ThinClient"));
//        ThinClient.setTooltip(tooltip_ThinClient);
        ThinClient.setOnAction((ActionEvent event) -> { 
            //正式用
            //TCL.Ping(IP_Addr.getText());
            //TCL.ThinClientLogin(IP_Addr.getText(),userTextField.getText(),pwBox.getText(),uniqueKey.toString());
            //TCL.CheckInetAddr();
            if(GB.lock_ThinClient == false){ // 2017.06.09 william Disable click many times will show mutil Dialog
                GB.lock_ThinClient=true; // 2017.06.09 william Disable click many times will show mutil Dialog
                
//                updateThinClient();
//                ClickColor = "";
//                Init_Color_id();
//                ThinClient.setId("Icon_ColorChange");
//                ClickColor = "ThinClient";

                All_Btn_hover_focus(ThinClient, "ThinClient");
                
                
                Task<Void> ThinClientTask = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {                         
                        //ThinClient TC;
                        //TC=new ThinClient(primaryStage, WordMap);
                        
                        return null;
                    }
                };
                ThinClientTask.setOnSucceeded((WorkerStateEvent event1) -> {
                    //System.out.println("** ThinClientTask.setOnSucceeded ** \n");
                    
                    try {
                        TC=new ThinClient(primaryStage, WordMap, IP_Addr.getText().trim(),IP_Port.getText().trim(), wifi_enable); // 2017.08.10 william IP增加port欄位，預設443
                    } catch (IOException ex) {
                        Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    GB.lock_ThinClient=true; // 2017.06.09 william Disable click many times will show mutil Dialog
                    tooltip_ThinClient.hide();
                });
                new Thread(ThinClientTask).start();
                
            }else{

                GB.lock_ThinClient=true; // 2017.06.09 william Disable click many times will show mutil Dialog
                //System.out.println("** TC else end ** \n");
            }
        });
        
        /*******Web******/
        //Web=new Button(WordMap.get("Web_Title"));
        Image Web_img = new Image("images/Firefox_45.png");
        Web = new Button();
        Web.setGraphic(new ImageView(Web_img));
        Web.setPrefSize(60,60);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動 160,35
        Web.setMaxSize(60,60);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動 160,35
        Web.setMinSize(60,60);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動   160,35
        Web.setAlignment(Pos.CENTER);
//        tooltip_Web = new Tooltip();// imageButton提示框
//        tooltip_Web.setText(WordMap.get("Web_Title"));
//        Web.setTooltip(tooltip_Web);
        Web.setOnAction((ActionEvent event) -> {
            try{
                    updateWeb();
                    WebB.Firefoxwebbrowser();

                
            } catch (IOException ex) {
                Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        /*****桌面雲管理*****/
        //ManageVD=new Button(WordMap.get("ManageVD")); 
        Image ManageVD_img = new Image("images/VDManager_45.png");
        ManageVD = new Button();
        ManageVD.setGraphic(new ImageView(ManageVD_img));
        ManageVD.setPrefSize(60,60);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動 170,35
        ManageVD.setMaxSize(60,60);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動 170,35
        ManageVD.setMinSize(60,60);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動  170,35  
        ManageVD.setAlignment(Pos.CENTER);
//        tooltip_ManageVD = new Tooltip(); // imageButton提示框
//        tooltip_ManageVD.setText(WordMap.get("ManageVD_Title"));        
//        ManageVD.setTooltip(tooltip_ManageVD);
        ManageVD.setOnAction((ActionEvent event) -> {            
                
            if(lock_ManageVD == false){
                lock_ManageVD=true;

//                updateManageVD();            
//                ClickColor = "";
//                Init_Color_id();
//                ManageVD.setId("Icon_ColorChange");
//                ClickColor = "ManageVD";

All_Btn_hover_focus(ManageVD, "ManageVD");
                
                
                Boolean Result=PATTERN.matcher(IP_Addr.getText()).matches();
                //System.out.println(" -----Result------"+Result+"\n");
                if(Result==true||IP_Addr.getText()!=null){ // 2017.07.19 william IP use Domain Name
                    Task<Void> ManageVDTask = new Task<Void>() {
                        @Override
                          protected Void call() throws Exception {
                            //updateManageVD();
                            // 2017.08.10 william IP增加port欄位，預設443
                            if((IP_Port.getText()==null)||("".equals(IP_Port.getText()))) {
                                CurrentIP_Port = "443";
                            }
                            else {
                                CurrentIP_Port = IP_Port.getText();
                            }    
                            // 2017.09.18 william 錯誤修改(1)-IP Port例外處理
                            CheckPort = Integer.parseInt(CurrentIP_Port);
                            if(!(CheckPort>=0&&CheckPort<=65535)) {
                                System.out.println("**IP PORT輸入錯誤 ** \n");
                                VDML.testError=false;
                            }                              
                            VDML.Ping(IP_Addr.getText(),CurrentIP_Port); // 2017.08.10 william IP增加port欄位，預設443
                            // 2019.01.02 Remember10 username
                            // VDML.VDManagementLogin(IP_Addr.getText(),userTextField.getText(),pwBox.getText(),uniqueKey.toString(),CurrentIP_Port); // 2017.08.10 william IP增加port欄位，預設443
                            VDML.VDManagementLogin(IP_Addr.getText(),UsernameComboBox.getValue().toString(),pwBox.getText(),uniqueKey.toString(),CurrentIP_Port); 

                            return null;
                        }
                    };
                    //new Thread(ManageVDTask).start();

                    ManageVDTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            //System.out.println("** ManageVDTask.setOnFailed ** \n");
                            //System.out.println("ManageVDTask Ping - testError = "+VDML.testError+"\n"); 
                            //System.out.println("ManageVDTask - connCode = "+VDML.VDMLconnCode+"\n");
                            if(VDML.testError==false){
                                VDMLA.VDMLtestErrorAlert(public_stage);
                            }
                            VDMLA.VDMLoginAlertChange(VDML.VDMLconnCode, public_stage);
                            lock_ManageVD=false;
                            VDML.VDMLconnCode = 0; // 2017.09.18 william 錯誤修改(2)-2個警告訊息
                        }
                    });
                    //new Thread(ManageVDTask).start();

                    ManageVDTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            //System.out.println("** ManageVDTask.setOnSucceeded ** \n");
                            // VDManagement VDM=new VDManagement(public_stage, WordMap, VDML.CurrentUserName, VDML.VdName, VDML.Suspend, VDML.VdStatus, VDML.IsVdOnline, VDML.CreateTime, VDML.Desc, VDML.CurrentIP, VDML.VdId, VDML.CurrentIP_Port);  // 2017.08.10 william IP增加port欄位，預設443
                            VDManagement VDM=new VDManagement(public_stage, WordMap, VDML.CurrentUserName,VDML.VDML_jsonArr, VDML.CurrentIP, VDML.CurrentIP_Port,pwBox.getText(),uniqueKey.toString()); // 2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD) , 2018.07.06 關機時，按強制關機按鈕不刪除VD列
                            lock_ManageVD=false;
                            tooltip_ManageVD.hide();
                        }
                    });
                    new Thread(ManageVDTask).start();

                }else{
                    VDMLResultAlert();
                    lock_ManageVD=false;
                    //System.out.println("** VDML else end ** \n");
                }
            }
            
    
        });
        
        
         /*****變更密碼****/
        //ChangePW=new Button(WordMap.get("ChangePassword"));
        GB.lock_ChangePW = false; // 2017.06.09 william Disable click many times will show mutil Dialog
        Image ChangePW_img = new Image("images/Password_45.png");
        ChangePW = new Button();
        ChangePW.setGraphic(new ImageView(ChangePW_img));
        ChangePW.setPrefSize(60,60);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        ChangePW.setMaxSize(60,60);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        ChangePW.setMinSize(60,60);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動   
        ChangePW.setAlignment(Pos.CENTER);
//        tooltip_ChangePW = new Tooltip(); // imageButton提示框
//        tooltip_ChangePW.setText(WordMap.get("CP_Window_Title"));
//        ChangePW.setTooltip(tooltip_ChangePW);
        ChangePW.setOnAction((ActionEvent event) -> {
            
            if(GB.lock_ChangePW==false){ // 2017.06.09 william Disable click many times will show mutil Dialog
                GB.lock_ChangePW=true; // 2017.06.09 william Disable click many times will show mutil Dialog
            
//                updateChangePW();
//                ClickColor = "";
//                Init_Color_id();
//                ChangePW.setId("Icon_ColorChange");
//                ClickColor = "ChangePW";
All_Btn_hover_focus(ChangePW, "ChangePW");
                
                Boolean Result=PATTERN.matcher(IP_Addr.getText()).matches();
                //System.out.println(" -----Result------"+Result+"\n");
                if(Result==true||IP_Addr.getText()!=null){ // 2017.07.19 william IP use Domain Name
                    Task<Void> ChangePWTask = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            //updateChangePW();           
                            // 2017.08.10 william IP增加port欄位，預設443
                            if((IP_Port.getText()==null)||("".equals(IP_Port.getText()))) {
                                CurrentIP_Port = "443";
                            }
                            else {
                                CurrentIP_Port = IP_Port.getText();
                            }     
                            // 2017.09.18 william 錯誤修改(1)-IP Port例外處理
                            CheckPort = Integer.parseInt(CurrentIP_Port);
                            if(!(CheckPort>=0&&CheckPort<=65535)) {
                                System.out.println("**IP PORT輸入錯誤 ** \n");
                                CPL.testError=false;
                            }                            
                            CPL.Ping(IP_Addr.getText(),CurrentIP_Port); // 2017.08.10 william IP增加port欄位，預設443
                            // 2019.01.02 Remember10 username
                            // CPL.ChangePasswordLogin(IP_Addr.getText(),userTextField.getText(),pwBox.getText(),uniqueKey.toString(),CurrentIP_Port); // 2017.08.10 william IP增加port欄位，預設443
                            CPL.ChangePasswordLogin(IP_Addr.getText(),UsernameComboBox.getValue().toString(),pwBox.getText(),uniqueKey.toString(),CurrentIP_Port);                             

                            return null;
                        }
                    };
                    //new Thread(ChangePWTask).start();

                    ChangePWTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            //System.out.println("** ChangePWTask.setOnFailed ** \n");
                            //System.out.println("ChangePWTask Ping - testError = "+CPL.testError+"\n"); 
                            //System.out.println("ChangePWTask - connCode = "+CPL.CPLconnCode+"\n");
                            if(CPL.testError==false){
                                CPLA.CPLtestErrorAlert(public_stage);
                            }
                            CPLA.CPLoginAlertChange(CPL.CPLconnCode, public_stage);
                            GB.lock_ChangePW=false; // 2017.06.09 william Disable click many times will show mutil Dialog
                            CPL.CPLconnCode = 0; // 2017.09.18 william 錯誤修改(2)-2個警告訊息
                        }
                    });
                    //new Thread(ChangePWTask).start();

                    ChangePWTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            //System.out.println("** ChangePWTask.setOnSucceeded ** \n");
                            try {
                                ChangePassword CP;
                                CP = new ChangePassword(public_stage, WordMap,CPL.CurrentPasseard,CPL.CurrentIP,CPL.CurrentUserName,CPL.CurrentIP_Port); // 2017.08.10 william IP增加port欄位，預設443
                                GB.lock_ChangePW=true; // 2017.06.09 william Disable click many times will show mutil Dialog
                                tooltip_ChangePW.hide();
                            } catch (IOException ex) {
                                Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    new Thread(ChangePWTask).start();

                }else{
                    CPLResultAlert();
                    GB.lock_ChangePW=true; // 2017.06.09 william Disable click many times will show mutil Dialog
                    //System.out.println("** CP else end ** \n");
                }
            
            }
        });
        
        /*******SnapShot******/
        // 2017.12.08 william SnapShot實作
        Image SnapShot_img = new Image("images/snapshot_icon.png");
        /*  控制圖片大小 */
        ImageView SnapShot_imageView = new ImageView(SnapShot_img);
        SnapShot_imageView.setFitHeight(45);
        SnapShot_imageView.setFitWidth(45);   
        
        SS = new Button();
        SS.setGraphic(SnapShot_imageView);
        SS.setPrefSize(60,60); // 強制限制button預設寬度/高度,讓每個button在變換語言時不會變動 160,35
        SS.setMaxSize(60,60);  // 強制限制button最大寬度/高度,讓每個button在變換語言時不會變動 160,35
        SS.setMinSize(60,60);  // 強制限制button最小寬度/高度,讓每個button在變換語言時不會變動   160,35
        SS.setAlignment(Pos.CENTER);
        SS.setOnAction((ActionEvent event) -> {            
            Login_func(true, false); // 2019.01.02 view D Drive false (Snapshot) -> true (User disk)            
        });        
        
       
        
        
        
//            try{
//                IsSnapShot = true;
//                QM.Ping(IP_Addr.getText(),CurrentIP_Port); 
//                QM.QueryLogin(public_stage,IP_Addr.getText(),userTextField.getText(),pwBox.getText(),uniqueKey.toString(),CurrentIP_Port, IsSnapShot);  // 2017.08.10 william IP增加port欄位，預設443 / 2017.11.24 單一帳號多個VD登入 / 2017.12.08 SnapShot實作
//            } catch (Exception ex) {
//                Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
//            }        
        
        
        
        /*****離開*****/
        /*
        Leave=new Button(WordMap.get("Exit"));
        Leave.setPrefSize(80,35);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        Leave.setMaxSize(80,35);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        Leave.setMinSize(80,35);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動           
        Leave.setOnAction((event) -> {
            ExitProcess();
            
        });        
        */
        
        HBox Operation=new HBox();
        //Operation.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        //Operation.getChildren().addAll(Web, ThinClient, ManageVD, ChangePW, Leave);  
        Operation.getChildren().addAll(ThinClient, ManageVD, ChangePW, SS); // 2017.06.22 william hide web icon and button event / 2017.12.08 SnapShot實作 
        Operation.setTranslateX(45);
        Operation.setAlignment(Pos.CENTER_LEFT);
        Operation.setSpacing(90); 
        
        /******關機(Image Button)******/ //  shutdown003.png
        Image Shutdown_img = new Image("images/shutdown.png",32, 32 ,false, false);//"images/Shutdown.png",30,30,false, false / Shutdown_30.png
        Shutdown = new Button();
        Shutdown.setGraphic(new ImageView(Shutdown_img));
        Shutdown.setPrefSize(60,60);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        Shutdown.setMaxSize(60,60);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        Shutdown.setMinSize(60,60);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動
        Shutdown.setAlignment(Pos.CENTER);
//        Shutdown.setId("Button_shutdown");        
//        tooltip_Shutdown = new Tooltip(); // imageButton提示框
//        tooltip_Shutdown.setText(WordMap.get("Button_Shutdown"));
//        Shutdown.setTooltip(tooltip_Shutdown);
        Shutdown.setOnAction((ActionEvent event) -> {
            
//            updateShutdown(); 
            tooltip_Shutdown.hide();
All_Btn_hover_focus(Shutdown, "Shutdown");
            
            ShutdownProcess();
            
        });
        
        /******重開機(Image Button)******/
        Image Reboot_img = new Image("images/reboot.png",32, 32 ,false, false); //"images/Reboot.png",30,30,false, false / Reboot_30.png /test: ChargingBAT.png",20,30,false,false
        Reboot = new Button();
        Reboot.setGraphic(new ImageView(Reboot_img));
        Reboot.setPrefSize(60,60);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        Reboot.setMaxSize(60,60);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        Reboot.setMinSize(60,60);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動
        Reboot.setAlignment(Pos.CENTER);
//        Reboot.setId("Button_reboot");
//        tooltip_Reboot = new Tooltip(); // imageButton提示框
//        tooltip_Reboot.setText(WordMap.get("Button_Reboot"));
//        Reboot.setTooltip(tooltip_Reboot);
        Reboot.setOnAction((ActionEvent event) -> {
            
//           updateReboot();  
            tooltip_Reboot.hide();
All_Btn_hover_focus(Reboot, "Reboot");
           
           RebootProcess();
            
        });
        
        /******網路連接-ON-Off (Label)******/
        //Connect_ON_img=new Image("images/Connect.png",30,30,false, false);     
        //Connect_OFF_img=new Image("images/Disconnect.png",30,30,false, false);
        
        // Connect_ON_OFF=new Label();
        GB.lock_wifilist = false;
        Connect_ON_OFF = new Button(); // 2018.04.13 william wifi實作
        Connect_ON_OFF.setPrefSize(60,60);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        Connect_ON_OFF.setMaxSize(60,60);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        Connect_ON_OFF.setMinSize(60,60);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動
        Connect_ON_OFF.setId("Label_right");
        //Connect_ON_OFF.setGraphic(new ImageView(Connect_ON_img));
        Connect_ON_OFF.setAlignment(Pos.CENTER);
        tooltip_Connect_ON_OFF = new Tooltip(); // imageButton提示框
        //tooltip_Connect_ON_OFF.setText(WordMap.get("Button_Network_ON"));
        //Connect_ON_OFF.setTooltip(tooltip_Connect_ON_OFF);
        /*** 網路連線 斷線 Task  ***/
        networkconn_change();
        if(wifi_enable) {
            Connect_ON_OFF.setOnAction((ActionEvent event) -> {
                All_Btn_hover_focus(Connect_ON_OFF, "Connect_ON_OFF");
                if(!GB.lock_wifilist) {
                    GB.lock_wifilist = true;
                    Platform.runLater(() -> {                         
                        wifi = new GetWifiInfo(public_stage,this, WordMap, true);
                        tooltip_Connect_ON_OFF.hide();
                    });                                         
                }
            });         
        }
                
        if(battery_enable) {
            /******電池******/ // 2018.04.26 william Battery實作  
        
//        Image Battery_img = new Image("images/BAT.png",30,30,false, false); 
            Battery = new Button();
            Battery.setGraphic(Battery_ImageView);
            Battery.setPrefSize(60,60);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
            Battery.setMaxSize(60,60);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
            Battery.setMinSize(60,60);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動
            Battery.setAlignment(Pos.CENTER);
            tooltip_Battery = new Tooltip(); // 2018.04.26 william Battery實作
            Battery_change(); // check Battery 
            // show Battery status
            GB.lock_battery = false;
            Battery.setOnAction((ActionEvent event) -> {
                All_Btn_hover_focus(Battery, "Battery");                
                GB.lock_battery = true;
                BC.BatteryShow(public_stage,this);      
                tooltip_Battery.hide();
            });
            
            /****** Brightness ******/ 
            Brightness_img = new Image("images/Brightness.png",32, 32 ,false, false);
            Brightness_ImageView     = new ImageView(Brightness_img);
            Brightness = new Button();
            Brightness.setGraphic(Brightness_ImageView);
            Brightness.setPrefSize(60,60);
            Brightness.setMaxSize(60,60);
            Brightness.setMinSize(60,60);
            tooltip_Brightness = new Tooltip();
            Brightness.setAlignment(Pos.CENTER);
            Brightness.setOnAction((ActionEvent event) -> {
                All_Btn_hover_focus(Brightness, "Brightness");                
                BrC.BrightnessShow(public_stage);            
                tooltip_Brightness.hide();
            });            
        }
      
        
        

        HBox Shut_Reboot_Operation=new HBox();
        //Shut_Reboot_Operation.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        if(battery_enable)        
            Shut_Reboot_Operation.getChildren().addAll(Brightness, Battery,Connect_ON_OFF,Reboot,Shutdown);  // 2018.04.13 william wifi實作 2018.04.26 william Battery實作  
        else
            Shut_Reboot_Operation.getChildren().addAll(Connect_ON_OFF,Reboot,Shutdown);
        Shut_Reboot_Operation.setAlignment(Pos.CENTER_RIGHT);
        //Shut_Reboot_Operation.setSpacing(10);
 
    /*****************動態時間-年/月/日 時/分/秒********************/    
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        int year = currentDateTime.getYear();
//        int month = currentDateTime.getMonthValue();
//        Month m = currentDateTime.getMonth();
//        int day = currentDateTime.getDayOfMonth();
//        DayOfWeek w = currentDateTime.getDayOfWeek();
//        int hour = currentDateTime.getHour();
//        int minute = currentDateTime.getMinute();
//        int second = currentDateTime.getSecond();
//        time_y_m_d=year+" / "+month+" / "+day;
//        time_h_m= hour +"："+ minute +"："+ second;     //時間還未動態變動 需再修改程式     
//        //System.out.printf("time_y_m  : "+ time_y_m_d +"\n");        
//        VBox RightControl=new VBox();
//        Label time_year_month = new Label(time_y_m_d);
//        Label time_hour_minute = new Label(time_h_m);
//        RightControl.getChildren().addAll(time_year_month, time_hour_minute);  
//        RightControl.setAlignment(Pos.CENTER);
//        RightControl.setSpacing(5); 
//        RightControl.setId("time");

        TimeCalendar.setPrefSize(120,60);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        TimeCalendar.setMaxSize(120,60);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        TimeCalendar.setMinSize(120,60);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動
        TimeCalendar.setId("Time");
        
        GetCurrentDateandTime(); // get current Date and Time
        time_y_m_d = MainCurrentDate_Year+" / "+MainCurrentDate_Mounth+" / "+MainCurrentDate_Day;
        time_h_m = "  " +MainCurrentTime_Hour +" : "+ MainCurrentTime_Min +" : "+ MainCurrentTime_Sec;
        TimeCalendar.setText(time_y_m_d + "\n " + time_h_m);
        /** Time Thread **/
       // UpdateDateandTime();
       
       GB.lock_AutoTimeRenew = false; // 2017.06.19 william Auto updata time action lock
       // 2017.06.16 william 使用Java自動化執行工作-時間
       ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
       exec.scheduleAtFixedRate(new UpdateDateandTime(), 0, 1, TimeUnit.SECONDS); // 欲執行的類別、第一次執行前要delay的時間、每次執行的時間間隔、時間單位。
        
      
//        Timer timer_restart=new Timer();
//        TimerTask task_restart=new TimerTask() {
//            @Override
//            public void run() { 
//                try {
//                    
//                    TS = new TimeSetting(public_stage, WordMap);
//                    if( TS.TimeUpdate_lock == true ){
//                        UpdateDateandTime();
//                        System.out.println(" ** timer_restart **  \n");
//                    }
//                    
//                } catch (IOException | ParseException ex) {
//                    Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                
//            }
//        };
//        timer_restart.schedule(task_restart, 5000, 1000); 
        
        
        /** ToggleButton 日期和時間 Action **/
        GB.Time_change = true; // 2017.06.19 william click bug fix
        
        TimeCalendar.setOnAction(new EventHandler<ActionEvent>() {
            @Override        
            public void handle(ActionEvent e) {
                
//                updateTime();
Time_Btn_hover_focus();
                tooltip_TimeCalendar.hide();
               
                if(GB.Time_change==true){ // 2017.06.19 william click bug fix

                    if (e.getEventType().equals(ActionEvent.ACTION)){   
                        try {
                            GB.Time_change=false; // 2017.06.19 william click bug fix
                            DAT = new DateandTime(public_stage, WordMap, MainCurrentDate);
                        } catch (IOException ex) {
                            Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        // Time_change=false;
                    } 

                // Time_change=false;

                }           
                else{

                    if (e.getEventType().equals(ActionEvent.ACTION)){                   
                        
                        GB.Time_change=true; // 2017.06.19 william click bug fix
                        DAT.DateandTime_stage.close();

                        // Time_change=true;
                    } 

                // Time_change=true;

                }
    
            }
        });
    
        //anchorpane.getChildren().addAll(Operation,RightControl,Shut_Reboot_Operation); // 將功能Button 放置AnchorPane中  
        anchorpane.getChildren().addAll(Operation,TimeCalendar,Shut_Reboot_Operation); // 將功能Button 放置AnchorPane中  
        //AnchorPane.setTopAnchor(Operation,13.0);  //設置邊距
        //AnchorPane.setLeftAnchor(Operation,15.0);
        //AnchorPane.setTopAnchor(RightControl,11.0); 
        //AnchorPane.setRightAnchor(RightControl,15.0);
//        AnchorPane.setTopAnchor(TimeCalendar,11.0); 
        AnchorPane.setRightAnchor(TimeCalendar,0.0);
        //AnchorPane.setTopAnchor(Shut_Reboot_Operation,5.0);
        AnchorPane.setRightAnchor(Shut_Reboot_Operation,120.0);
        
        // 2017.06.29 william 新增 viewer bind mac address (Process Start Info Check SN) - End
        MyTitle.setId("Title");
        ChangeLabelLang(L_IP_Addr, userName, pw, Language, Colon1, Colon2, Colon3); //變更Label語言字型
        //ChangeButtonLang(Login, ManageVD, ChangePW, Leave, checkBox_Pw, ThinClient, Web); //變更Button語言字型
        ChangeButtonLang(Login, ManageVD, ChangePW, checkBox_Pw, ThinClient, Web); //變更Button語言字型        
        IP_Addr.requestFocus();
        keydownup();
        mousemove();
        Tooptip_buttonhover();
        

        /*
        Loginbuttonhover_focus();
        Webbuttonhover_focus();
        ThinClientbuttonhover_focus();
        ManageVDbuttonhover_focus();
        ChangePWbuttonhover_focus();
        Shutdownbuttonhover_focus();
        Rebootbuttonhover_focus();
        Timetogglebuttonhover_focus();
        SSbuttonhover_focus(); // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        */
        }  // 2017.06.29 william 新增 viewer bind mac address (Process Start Info Check SN) - End

        /***  產生主頁視窗 ***/      
        //Scene scene = new Scene(MainGP, 550, 380);//550 350        
        Scene scene = new Scene(rootPane);//550 350
        /* 
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("mouse click detected! " + mouseEvent.getSource());
            }
        });
        */
        //rootPane.getChildren().addAll(MainGP,anchorpane);//MainGP,BigBP        
        scene.getStylesheets().add("vdi.css");
        // 2017.06.29 william 新增 viewer bind mac address (Process Start Info Check SN)
//        MyTitle.setId("Title");
//        ChangeLabelLang(L_IP_Addr, userName, pw, Language, Colon1, Colon2, Colon3); //變更Label語言字型
//        //ChangeButtonLang(Login, ManageVD, ChangePW, Leave, checkBox_Pw, ThinClient, Web); //變更Button語言字型
//        ChangeButtonLang(Login, ManageVD, ChangePW, checkBox_Pw, ThinClient, Web); //變更Button語言字型        
//        IP_Addr.requestFocus();
//        keydownup();
//        mousemove();
//        Tooptip_buttonhover();
//        Loginbuttonhover_focus();
//        Webbuttonhover_focus();
//        ThinClientbuttonhover_focus();
//        ManageVDbuttonhover_focus();
//        ChangePWbuttonhover_focus();
//        Shutdownbuttonhover_focus();
//        Rebootbuttonhover_focus();
//        Timetogglebuttonhover_focus();
      

           
        
        /***** 設置全螢幕 *****/
        screen = Screen.getPrimary();           // 2018.01.30 william 雙螢幕實作
        bounds = screen.getVisualBounds();      // 2018.01.30 william 雙螢幕實作
        width_current = bounds.getWidth();      // 2018.01.30 william 雙螢幕實作
        heigth_current = bounds.getHeight();    // 2018.01.30 william 雙螢幕實作       
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(width_current);   // 2018.01.30 william 雙螢幕實作
        primaryStage.setHeight(heigth_current); // 2018.01.30 william 雙螢幕實作
        primaryStage.centerOnScreen();        
//        Rectangle2D screenBounds = screen.getBounds();
//        screenCenterX = screenBounds.getMinX() + screenBounds.getWidth()/2 ;
//        screenCenterY = screenBounds.getMinY() + screenBounds.getHeight()/2;
//        primaryStage.setX(screenCenterX);
//        primaryStage.setY(screenCenterY);
        
        //primaryStage.setFullScreen(true);//esc 會關掉全螢幕
        //primaryStage.setMaximized(true);//fullScreen Maxsize
        //primaryStage.getIcons().add(new Image("images/Icon2.png"));
        primaryStage.setTitle(WordMap.get("APP_Title")+"  for Win 10/8/7  "+Ver); //視窗標題+版本
        primaryStage.setScene(scene);
        //primaryStage.initStyle(StageStyle.DECORATED);
        //primaryStage.maximizedProperty();
        primaryStage.initStyle(StageStyle.UNDECORATED);
//        primaryStage.setResizable(false);
        if(GB.AllDisplayOff && GB.DuplicateSize == 2) { /** 2018.03.15 william 雙螢幕實作 **/
            primaryStage.close();
        } else {
            primaryStage.show();
        }
        
        
        /*********** 雙螢幕實作 - 延伸螢幕 ***********/                        
        Path folderPath = Paths.get("/root/.screenlayout/"); // 檢查folder
        if (Files.notExists(folderPath)) {
            Runtime.getRuntime().exec("mkdir /root/.screenlayout/");
        }           
        SecondMonitorCreate(); // 副螢幕畫面設定          
        Monitor_listener();    // 監聽螢幕Size是否改變  Timer1                   

        /**** 清除系統垃圾 ****/        
        ClearSystemGarbage();
        
        /*****關閉視窗"X"-會與離開按鍵一樣 可以關閉全部視窗 *****/
//        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//          @Override
//          public void handle(WindowEvent we) {  
//              System.out.println("Stage is closing");
//              ExitWindowProcess(we);
//              //primaryStage.close();             
//              System.out.println("----------end----------");
//          }
//      }); 
            
        //System.out.println("----------5----------");
        //showTime();            
        
    }
       
    /**
     * @param args the command line arguments
     */
    // Input： String[] args,  Output： , 功能： 啟動主程式
    public static void main(String[] args) {
        launch(args);
    }
    
    // Input： ,  Output： , 功能： 產生頁面中下拉選單字串，連結每一個語言的設定檔 
    public void InitailLanguageFileMap(){
        LanguageFileMap.put("English", "jsonfile/English.json");
        LanguageFileMap.put("繁體中文", "jsonfile/TraditionalChinese.json");
        LanguageFileMap.put("简体中文", "jsonfile/SimpleChinese.json");     
        LanguageFileMap.put("TraditionalChinese", "jsonfile/TraditionalChinese.json");
        LanguageFileMap.put("SimpleChinese", "jsonfile/SimpleChinese.json");   
        LanguageFileMap.put("Japanese", "jsonfile/Japanese.json");
        LanguageFileMap.put("日本語", "jsonfile/Japanese.json");        
    }
    
    /***   處理語言變更程式碼
     * @param newLang
     * @throws org.json.simple.parser.ParseException ***/
    // Input：1. LanguageSelection.getValue().toString() ，combobox選擇的語系名稱,  Output： , 功能： 變更下拉選單字串，記住選擇語言的設定檔及變更文字字型
    public void LangComboChanged(String newLang) throws ParseException{
        LangNow=newLang;
        WriteLastStatus();  //紀錄設定的程式語言
        WriteLastChange();  //紀錄明碼與暗碼
        WordMap.clear();
        LoadLanguageMapFile(LanguageFileMap.get(LangNow));
        SwitchLanguage();
        ChangeLabelLang(L_IP_Addr, userName, pw, Language, Colon1, Colon2, Colon3); //變更Label語言字型
        ChangeButtonLang(Login, ManageVD, ChangePW, checkBox_Pw, ThinClient, Web); //變更Button語言字型
        //ChangeButtonLang(Login, ManageVD, ChangePW, Leave, checkBox_Pw, ThinClient, Web); //變更Button語言字型
    }
    // Input：,  Output： , 功能： 變更物件文字內容
    public void SwitchLanguage(){
        Language.setText(WordMap.get("Language")+"  :");//+":"
        MyTitle.setText(WordMap.get("Title"));
        L_IP_Addr.setText(WordMap.get("IP_Addr"));//+":"
        userName.setText(WordMap.get("Username"));//+":"
        pw.setText(WordMap.get("Password"));//+":"
        Login.setText(WordMap.get("Login"));
        checkBox_Pw.setText(WordMap.get("Read_password"));
        //ThinClient.setText(WordMap.get("ThinClient")); 
        //Web.setText(WordMap.get("Web_Title"));
        //ManageVD.setText(WordMap.get("ManageVD"));
        //ChangePW.setText(WordMap.get("ChangePassword"));
        //Check.setText(WordMap.get("TestServer"));
        //Leave.setText(WordMap.get("Exit"));
        //public_stage.setTitle(WordMap.get("APP_Title")+"  for Win 10/8/7  "+Ver);
        tooltip_Web.setText(WordMap.get("Web_Title"));
        tooltip_ThinClient.setText(WordMap.get("ThinClient"));
        tooltip_ManageVD.setText(WordMap.get("ManageVD_Title"));
        tooltip_ChangePW.setText(WordMap.get("CP_Window_Title"));
        tooltip_Shutdown.setText(WordMap.get("Button_Shutdown"));
        tooltip_Reboot.setText(WordMap.get("Button_Reboot"));
        tooltip_SS.setText(WordMap.get("Snapshot")); // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        tooltip_TimeCalendar.setText(WordMap.get("TimeCalendar_title"));
        
        if(network_change==true){
            
            tooltip_Connect_ON_OFF.setText(WordMap.get("Button_Network_ON"));
            
        }
        if(network_change==false){
            
            tooltip_Connect_ON_OFF.setText(WordMap.get("Button_Nectwork_OFF"));
            
        }
        if( lock_Login == true ){ // 2017.09.13 william 登入中thread畫面鎖住    
            Login_title.setText(WordMap.get("Thread_Login")+"\n"+WordMap.get("Thread_Waiting"));
            System.out.print(" **Login ** SwitchLanguage** \n");
        } 
        
        if(pow_Charging) { // 2018.04.26 william Battery實作
            tooltip_Battery.setText(pow_capacity + "%" + "(" + WordMap.get("Charging") + ")"); 
        } else {
            tooltip_Battery.setText(pow_capacity + "%"); 
        }
        
        tooltip_Brightness.setText(WordMap.get("Brightness"));

    }    
    // Input：,  Output： , 功能： 讀取設定資訊( ip . 帳號)
    public void LoadLastStatus() throws ParseException{
        try{
            File myFile=new File("jsonfile/SystemStatus.json");
             //System.out.print("LangNow-1: "+LangNow+"\n");

            if(myFile.exists()){

                String FilePath=myFile.getPath();
                List<String> JSONLINE=Files.readAllLines(Paths.get(FilePath));
                String JSONCode="";
                JSONCode = JSONLINE.stream().map((s) -> s).reduce(JSONCode, String::concat);
                // 參考 http://stackoverflow.com/questions/7463414/what-s-the-best-way-to-load-a-jsonobject-from-a-json-text-file
                //System.out.print(JSONCode+"\n");
                JSONParser Jparser=new JSONParser();
                SystemStatus=(JSONObject) Jparser.parse(JSONCode);           
                LangNow=SystemStatus.get("Language").toString();
                //System.out.print("LangNow-3: "+LangNow+"\n");
                 //讀取SystemStatus.json檔-"Language"時,是英文(建立jar檔時,僅讀取英文,若有中文會有亂碼顯示),因此這邊讀取時要將英文轉中文,讓語言選單可以使用中文選取
                if("TraditionalChinese".equals(LangNow)){       
                LangChange="繁體中文";//TraditionalChinese
                LangNow=LangChange;
                }
                if("SimpleChinese".equals(LangNow)){
                LangChange="简体中文";//SimpleChinese
                LangNow=LangChange;
                }
                //System.out.print("LangNow-4: "+LangNow+"\n");
                
                CurrentIP=SystemStatus.get("LastIP").toString();           
                CurrentUserName=SystemStatus.get("LastUsername").toString();
                
                CurrentIP_Port=SystemStatus.get("LastIPPort").toString(); // 2017.08.10 william IP增加port欄位，預設443 存取port
                if((CurrentIP_Port==null)||("".equals(CurrentIP_Port))) {
                    CurrentIP_Port= "443";
                }                
                 
            }else{
                LangNow="繁體中文";
                CurrentIP="";
                CurrentUserName="";
                CurrentIP_Port = "443"; // 2017.08.10 william IP增加port欄位，預設443 
            }
        }catch(Exception e){ // 2017.11.09 william Json檔案讀寫失敗或錯誤的處理，將IOException 改為通用型例外 Exception
            System.out.print("SystemStatus.json read failed \n");
           // e.printStackTrace();
           CurrentIP_Port= "443";
        }
    }
    // Input：,  Output： , 功能： 寫入設定資訊( ip . 帳號)
    public void WriteLastStatus(){
        
        JSONObject SystemStatus=new JSONObject();
        
        if("繁體中文".equals(LangNow)){           //將SystemStatus.json檔,寫入"Language"時,使用英文(建立jar檔時,僅讀取英文,若有中文會有亂碼顯示)
            LangChange="TraditionalChinese";
            LangNow=LangChange;
        }
        if("简体中文".equals(LangNow)){          //將SystemStatus.json檔,寫入"Language"時,使用英文(建立jar檔時,僅讀取英文,若有中文會有亂碼顯示)
            LangChange="SimpleChinese";
            LangNow=LangChange;
        }
        
        SystemStatus.put("Language", LangNow);
        SystemStatus.put("LastIP", IP_Addr.getText());
        
        // 2019.01.02 Remember10 username
        // SystemStatus.put("LastUsername",  userTextField.getText());
        SystemStatus.put("LastUsername",  UsernameComboBox.getValue().toString());
        // 2017.08.10 william IP增加port欄位，預設443 記住port
        SystemStatus.put("LastIPPort",  IP_Port.getText());        
        
        try{
            File myFile=new File("jsonfile/SystemStatus.json");
            if(myFile.exists()){
                myFile.delete();
                myFile=null;
            }
            try (FileWriter JsonWriter = new FileWriter("jsonfile/SystemStatus.json")) {
                JsonWriter.write(SystemStatus.toJSONString());
                JsonWriter.flush();
                JsonWriter.close();
            }
            
        }catch(IOException e){
           // e.printStackTrace();
        }
    }
    // Input：,  Output： , 功能： 讀取IP與帳號的明暗碼資訊
    public void LoadLastChange() throws ParseException{
        try{
            File myFile=new File("jsonfile/AfterChange.json");
            if(myFile.exists()){

                String FilePath=myFile.getPath();
                List<String> JSONLINE=Files.readAllLines(Paths.get(FilePath));
                String JSONCode="";
                JSONCode = JSONLINE.stream().map((s) -> s).reduce(JSONCode, String::concat);             
                // 參考 http://stackoverflow.com/questions/7463414/what-s-the-best-way-to-load-a-jsonobject-from-a-json-text-file
                //System.out.print(JSONCode+"\n");
                JSONParser Jparser=new JSONParser();
                AfterChange=(JSONObject) Jparser.parse(JSONCode);
                IP_changeAfterSaved=AfterChange.get("IP_changeAfterSaved").equals(IP_changeAfterSaved);               
                //System.out.print("IP_changeAfterSaved-Load : "+IP_changeAfterSaved+"\n");                
                
                Name_changeAfterSaved=AfterChange.get("Name_changeAfterSaved").equals(Name_changeAfterSaved);                
                //System.out.print("Name_changeAfterSaved-Load : "+Name_changeAfterSaved+"\n"); 
                 
            }else{
                IP_changeAfterSaved=true;
                //buttonIP.setGraphic(new ImageView(EyeOpen));
                Name_changeAfterSaved=true;
                //buttonUname.setGraphic(new ImageView(EyeOpen));
            }
        }catch(Exception e){ // 2017.11.09 william Json檔案讀寫失敗或錯誤的處理，將IOException 改為通用型例外 Exception
            System.out.print("AfterChange.json read failed \n");
           // e.printStackTrace();
        }
    }
    // Input：,  Output： , 功能： 紀錄IP與帳號的明暗碼資訊
    public void WriteLastChange(){
        
        JSONObject AfterChange=new JSONObject();
        
        AfterChange.put("IP_changeAfterSaved", IP_changeAfterSaved);
        AfterChange.put("Name_changeAfterSaved", Name_changeAfterSaved); 
        
        //System.out.print("IP_changeAfterSaved-Write : "+IP_changeAfterSaved+"\n");
        //System.out.print("Name_changeAfterSaved-Write : "+Name_changeAfterSaved+"\n");
        
        try{
            File myFile=new File("jsonfile/AfterChange.json");
            if(myFile.exists()){
                myFile.delete();
                myFile=null;
            }
            try (FileWriter JsonWriter = new FileWriter("jsonfile/AfterChange.json")) {
                JsonWriter.write(AfterChange.toJSONString());
                JsonWriter.flush();
                JsonWriter.close();
            }
            
        }catch(IOException e){
           // e.printStackTrace();
        }
    }    
    // Input：,  Output： , 功能： 讀取Version資訊
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
    // Input：,  Output： , 功能： 紀錄Version資訊
    public void WriteVersion(){
        
        JSONObject Version=new JSONObject();
        
        Version.put("Version_num-write : ", "3.0.11");
        System.out.print("Version-Write : "+Version+"\n");
      
        try{
            File myFile=new File("jsonfile/Version.json");
            if(myFile.exists()){
                myFile.delete();
                myFile=null;
            }
            try (FileWriter JsonWriter = new FileWriter("jsonfile/Version.json")) {
                JsonWriter.write(Version.toJSONString());
                JsonWriter.flush();
            }
            
        }catch(IOException e){
           // e.printStackTrace();
        }
    }
    
    /**
     *
     * @param LanguageFileName
     * @throws ParseException
     */
    // Input：1. 繁體中文、2. 简体中文或3. English任一字串,  Output： , 功能： 載入語言JSON檔案
    public void LoadLanguageMapFile(String LanguageFileName) throws ParseException{
        try{
            File myFile=new File(LanguageFileName);
            if(myFile.exists()){

                String FilePath=myFile.getPath();
                List<String> JSONLINE=Files.readAllLines(Paths.get(FilePath));
                String JSONCode="";
                JSONCode = JSONLINE.stream().map((s) -> s).reduce(JSONCode, String::concat);
                // 參考 http://stackoverflow.com/questions/7463414/what-s-the-best-way-to-load-a-jsonobject-from-a-json-text-file
                //System.out.print(JSONCode+"\n");
                JSONParser Jparser=new JSONParser();
                JSONObject LMF=(JSONObject) Jparser.parse(JSONCode);
                WordMap.put("APP_Title", LMF.get("APP_Title").toString());
                WordMap.put("Language", LMF.get("Language").toString());
                WordMap.put("Title", LMF.get("Title").toString());
                WordMap.put("IP_Addr", LMF.get("IP_Addr").toString());
                WordMap.put("Username", LMF.get("Username").toString());
                WordMap.put("Password", LMF.get("Password").toString());
                WordMap.put("Login", LMF.get("Login").toString());
                WordMap.put("ManageVD", LMF.get("ManageVD").toString());
                WordMap.put("ChangePassword", LMF.get("ChangePassword").toString());
                WordMap.put("TestServer", LMF.get("TestServer").toString());
                WordMap.put("Exit", LMF.get("Exit").toString());
                WordMap.put("Confirm", LMF.get("Confirm").toString());
                WordMap.put("Ping_Window_Title", LMF.get("Ping_Window_Title").toString());
                WordMap.put("Ping_Header", LMF.get("Ping_Header").toString());
                WordMap.put("Ping_Message_F", LMF.get("Ping_Message_F").toString());
                WordMap.put("Ping_Message_R1", LMF.get("Ping_Message_R1").toString());
                WordMap.put("Ping_Message_R2", LMF.get("Ping_Message_R2").toString());
                WordMap.put("Cancel", LMF.get("Cancel").toString());
                WordMap.put("CP_Window_Title", LMF.get("CP_Window_Title").toString());
                WordMap.put("CP_Header", LMF.get("CP_Header").toString());
                WordMap.put("CP_Original", LMF.get("CP_Original").toString());
                WordMap.put("CP_New", LMF.get("CP_New").toString());
                WordMap.put("CP_Confirm", LMF.get("CP_Confirm").toString());
                WordMap.put("EW_Window_Title", LMF.get("EW_Window_Title").toString());
                WordMap.put("EW_Header", LMF.get("EW_Header").toString());
                WordMap.put("WW_Window_Title", LMF.get("WW_Window_Title").toString());
                WordMap.put("WW_Header", LMF.get("WW_Header").toString());
                WordMap.put("ID_Name", LMF.get("ID_Name").toString());
                WordMap.put("VD_Name", LMF.get("VD_Name").toString());
                WordMap.put("Stop", LMF.get("Stop").toString());
                WordMap.put("Mode", LMF.get("Mode").toString());
                WordMap.put("Online", LMF.get("Online").toString());
                WordMap.put("Time", LMF.get("Time").toString());
                WordMap.put("Comment", LMF.get("Comment").toString());
                WordMap.put("Action", LMF.get("Action").toString());
                WordMap.put("Shutdown", LMF.get("Shutdown").toString());
                WordMap.put("VM_Disable", LMF.get("VM_Disable").toString());
                WordMap.put("VM_Stop", LMF.get("VM_Stop").toString());
                WordMap.put("VM_Waiting_Poweron", LMF.get("VM_Waiting_Poweron").toString());
                WordMap.put("VM_Poweron", LMF.get("VM_Poweron").toString());
                WordMap.put("VM_Blocked", LMF.get("VM_Blocked").toString());
                WordMap.put("VM_Pause", LMF.get("VM_Pause").toString());
                WordMap.put("VM_Shutdowning", LMF.get("VM_Shutdowning").toString());
                WordMap.put("VM_Shutdown", LMF.get("VM_Shutdown").toString());
                WordMap.put("VM_Crash", LMF.get("VM_Crash").toString());
                WordMap.put("VM_Suspend", LMF.get("VM_Suspend").toString());
                WordMap.put("VM_Shutdown", LMF.get("VM_Shutdown").toString());
                WordMap.put("VDO_Status", LMF.get("VDO_Status").toString());
                WordMap.put("SelectedLanguage", LMF.get("SelectedLanguage").toString());
                WordMap.put("ExitMassage", LMF.get("ExitMassage").toString());
                WordMap.put("Message_IP_Addr", LMF.get("Message_IP_Addr").toString());
                WordMap.put("Message_Username", LMF.get("Message_Username").toString());
                WordMap.put("Message_Password", LMF.get("Message_Password").toString());
                WordMap.put("Message", LMF.get("Message").toString());
                WordMap.put("Message_Input_IP", LMF.get("Message_Input_IP").toString());
                WordMap.put("Message_Input_User_PW", LMF.get("Message_Input_User_PW").toString());
                WordMap.put("Message_Input_ALL", LMF.get("Message_Input_ALL").toString());
                WordMap.put("Message_Error_User_PW", LMF.get("Message_Error_User_PW").toString());
                WordMap.put("Message_Error_OPW", LMF.get("Message_Error_OPW").toString());
                WordMap.put("Message_Correct_OPW", LMF.get("Message_Correct_OPW").toString());
                WordMap.put("Message_Error_NPW_CPW", LMF.get("Message_Error_NPW_CPW").toString());
                WordMap.put("Message_Correct_NPW_CPW", LMF.get("Message_Correct_NPW_CPW").toString());
                WordMap.put("Message_Login", LMF.get("Message_Login").toString());
                WordMap.put("Message_Lock", LMF.get("Message_Lock").toString());
                WordMap.put("Message_VD_Online", LMF.get("Message_VD_Online").toString());
                WordMap.put("Alert_Cancel", LMF.get("Alert_Cancel").toString());
                WordMap.put("Alert_Confirm", LMF.get("Alert_Confirm").toString());
                WordMap.put("VD_Stop_true", LMF.get("VD_Stop_true").toString());
                WordMap.put("VD_Stop_false", LMF.get("VD_Stop_false").toString());
                WordMap.put("VD_Online_true", LMF.get("VD_Online_true").toString());
                WordMap.put("VD_Online_false", LMF.get("VD_Online_false").toString());
                WordMap.put("Read_password", LMF.get("Read_password").toString());
                WordMap.put("Message_Error_IP", LMF.get("Message_Error_IP").toString());
                WordMap.put("Message_Error_IP_01", LMF.get("Message_Error_IP_01").toString());
                WordMap.put("Message_Error_IP_02", LMF.get("Message_Error_IP_02").toString());
                WordMap.put("Message_Error_IP_03", LMF.get("Message_Error_IP_03").toString());
                WordMap.put("Message_Error_404", LMF.get("Message_Error_404").toString());
                WordMap.put("ManageVD_Title", LMF.get("ManageVD_Title").toString());
                WordMap.put("Message_Suggest", LMF.get("Message_Suggest").toString());
                WordMap.put("Message_Error_IP_04", LMF.get("Message_Error_IP_04").toString());
                WordMap.put("Message_Error_IP_403", LMF.get("Message_Error_IP_403").toString());
                WordMap.put("Message_Error_IP_05", LMF.get("Message_Error_IP_05").toString());
                WordMap.put("ThinClient", LMF.get("ThinClient").toString());
                WordMap.put("Web_Title", LMF.get("Web_Title").toString());
                WordMap.put("TC_Exit", LMF.get("TC_Exit").toString());
                WordMap.put("TC_Cancel", LMF.get("TC_Cancel").toString());
                WordMap.put("TC_Confirm", LMF.get("TC_Confirm").toString());
                WordMap.put("TC_Display", LMF.get("TC_Display").toString());
                WordMap.put("TC_System", LMF.get("TC_System").toString());
                WordMap.put("TC_Static_IP", LMF.get("TC_Static_IP").toString());
                WordMap.put("TC_Static_IP_Setting", LMF.get("TC_Static_IP_Setting").toString());
                WordMap.put("TC_IP_addr", LMF.get("TC_IP_addr").toString());
                WordMap.put("TC_Mask", LMF.get("TC_Mask").toString());
                WordMap.put("TC_Def_Getway", LMF.get("TC_Def_Getway").toString());
                WordMap.put("TC_Static_DNS_Setting", LMF.get("TC_Static_DNS_Setting").toString());
                WordMap.put("TC_Monitor", LMF.get("TC_Monitor").toString());
                WordMap.put("TC_Resolution", LMF.get("TC_Resolution").toString());
                WordMap.put("TC_Client_Firmware", LMF.get("TC_Client_Firmware").toString());
                WordMap.put("TC_Current_Ver", LMF.get("TC_Current_Ver").toString());
                WordMap.put("TC_Server_Ver", LMF.get("TC_Server_Ver").toString());
                WordMap.put("TC_Server_Ver_AlertMessage", LMF.get("TC_Server_Ver_AlertMessage").toString()); // 2017.06.22 william System version check
                WordMap.put("TC_Server_Ver_AlertEvent", LMF.get("TC_Server_Ver_AlertEvent").toString()); // 2017.06.22 william System version check
                WordMap.put("TC_Check", LMF.get("TC_Check").toString());
                WordMap.put("TC_Update", LMF.get("TC_Update").toString());
                WordMap.put("Message_Error_IP_06", LMF.get("Message_Error_IP_06").toString());
                WordMap.put("Message_Error_IP_07", LMF.get("Message_Error_IP_07").toString());
                WordMap.put("Message_Error_VM_IP_01", LMF.get("Message_Error_VM_IP_01").toString());
                WordMap.put("Message_Error_VM_IP_02", LMF.get("Message_Error_VM_IP_02").toString());
                WordMap.put("Message_Error_VM_IP_404", LMF.get("Message_Error_VM_IP_404").toString());
                WordMap.put("Message_Error_VM_IP_401", LMF.get("Message_Error_VM_IP_401").toString());
                WordMap.put("Message_Error_VM_IP_403", LMF.get("Message_Error_VM_IP_403").toString());
                WordMap.put("Message_Error_CP_IP_01", LMF.get("Message_Error_CP_IP_01").toString());
                WordMap.put("Message_Error_CP_IP_02", LMF.get("Message_Error_CP_IP_02").toString());
                WordMap.put("Message_Error_CP_IP_404", LMF.get("Message_Error_CP_IP_404").toString());
                WordMap.put("Message_Error_CP_IP_401", LMF.get("Message_Error_CP_IP_401").toString());
                WordMap.put("Message_Error_CP_IP_403", LMF.get("Message_Error_CP_IP_403").toString());
                WordMap.put("TC_Last_Update", LMF.get("TC_Last_Update").toString());
                WordMap.put("VD_SN", LMF.get("VD_SN").toString());
                WordMap.put("Message_Error_VD_503", LMF.get("Message_Error_VD_503").toString());
                WordMap.put("Message_Error_VD_406", LMF.get("Message_Error_VD_406").toString());
                WordMap.put("Message_Error_VD_412", LMF.get("Message_Error_VD_412").toString());
                WordMap.put("Message_Error_VD_500", LMF.get("Message_Error_VD_500").toString());
                WordMap.put("Message_Error_VD_403", LMF.get("Message_Error_VD_403").toString());
                WordMap.put("Message_Error_VD_417", LMF.get("Message_Error_VD_417").toString());
                WordMap.put("Message_Error_VD_410", LMF.get("Message_Error_VD_410").toString());
                WordMap.put("Button_Shutdown", LMF.get("Button_Shutdown").toString());
                WordMap.put("Button_Reboot", LMF.get("Button_Reboot").toString());
                WordMap.put("Button_Network_ON", LMF.get("Button_Network_ON").toString());
                WordMap.put("Button_Nectwork_OFF", LMF.get("Button_Nectwork_OFF").toString());
                WordMap.put("RebootMassage", LMF.get("RebootMassage").toString());
                WordMap.put("Comfirm_RebootMassage", LMF.get("Comfirm_RebootMassage").toString());        
                WordMap.put("IP_EnterCheckMassage", LMF.get("IP_EnterCheckMassage").toString()); // 2017.07.12 william remove internet plug & DNS Exception fix         
                WordMap.put("Message_Error_setVDonline", LMF.get("Message_Error_setVDonline").toString()); 
                WordMap.put("Dispaly_Check_button", LMF.get("Dispaly_Check_button").toString());
                WordMap.put("Dispaly_PathChange_button", LMF.get("Dispaly_PathChange_button").toString());
                WordMap.put("TC_Check_Error_IP", LMF.get("TC_Check_Error_IP").toString());
                WordMap.put("TC_Check_Error", LMF.get("TC_Check_Error").toString());
                WordMap.put("TC_Update_Error", LMF.get("TC_Update_Error").toString());
                WordMap.put("TC_Update_Error_Suggest", LMF.get("TC_Update_Error_Suggest").toString());
                WordMap.put("TC_Update_Suggest", LMF.get("TC_Update_Suggest").toString());
                WordMap.put("TimeCalendar_title", LMF.get("TimeCalendar_title").toString());
                WordMap.put("TimeCalendar_Settings", LMF.get("TimeCalendar_Settings").toString());
                WordMap.put("TimeSettings_title", LMF.get("TimeSettings_title").toString());
                WordMap.put("TimeSettings_update", LMF.get("TimeSettings_update").toString());
                WordMap.put("TimeSettings_CurrentTime", LMF.get("TimeSettings_CurrentTime").toString());
                WordMap.put("TimeSettings_TimeZone", LMF.get("TimeSettings_TimeZone").toString());
                WordMap.put("TimeSettings_Manually", LMF.get("TimeSettings_Manually").toString());
                WordMap.put("TimeSettings_AutoSync", LMF.get("TimeSettings_AutoSync").toString());
                WordMap.put("TimeSettings_date", LMF.get("TimeSettings_date").toString());
                WordMap.put("TimeSettings_time", LMF.get("TimeSettings_time").toString());
                WordMap.put("TimeSettings_ServerAddress", LMF.get("TimeSettings_ServerAddress").toString());
                // 2017.08.10 william 登入中thread畫面鎖住    
                WordMap.put("Thread_Login", LMF.get("Thread_Login").toString());
                WordMap.put("Thread_Waiting", LMF.get("Thread_Waiting").toString());     
                //  2017.08.10 william 400錯誤處理
                WordMap.put("Message_Bad_Request", LMF.get("Message_Bad_Request").toString());   
                WordMap.put("Message_Error_400", LMF.get("Message_Error_400").toString());   
                //  2017.09.18 william 錯誤修改(4) 其他未知的錯誤
                WordMap.put("Message_Unknown_system_error", LMF.get("Message_Unknown_system_error").toString());                   
                // 2017.11.24 william 單一帳號多個VD登入
                WordMap.put("Select_VD", LMF.get("Select_VD").toString());
                WordMap.put("LoginMultiVD_desc1", LMF.get("LoginMultiVD_desc1").toString());
                WordMap.put("LoginMultiVD_desc2", LMF.get("LoginMultiVD_desc2").toString());
                WordMap.put("LoginMultiVD_Mark", LMF.get("LoginMultiVD_Mark").toString()); 
                // 2017.12.12 william SnapShot實作
                WordMap.put("Select_Snapshot_Layer", LMF.get("Select_Snapshot_Layer").toString());
                WordMap.put("Snapshot_View", LMF.get("Snapshot_View").toString());
                WordMap.put("Snapshot_Stop_View", LMF.get("Snapshot_Stop_View").toString());
                WordMap.put("Snapshot_Login", LMF.get("Snapshot_Login").toString());
                WordMap.put("Snapshot_Exit", LMF.get("Snapshot_Exit").toString());
                WordMap.put("Snapshot_State", LMF.get("Snapshot_State").toString());
                WordMap.put("Snapshot_CreateTime", LMF.get("Snapshot_CreateTime").toString());
                WordMap.put("Snapshot_Size", LMF.get("Snapshot_Size").toString());
                WordMap.put("Snapshot_Description", LMF.get("Snapshot_Description").toString());                
                WordMap.put("Snapshot_Normal", LMF.get("Snapshot_Normal").toString());
                WordMap.put("Snapshot_Preparing_view", LMF.get("Snapshot_Preparing_view").toString());
                WordMap.put("Snapshot_state_View", LMF.get("Snapshot_state_View").toString());
                WordMap.put("Snapshot_Preparing_delete", LMF.get("Snapshot_Preparing_delete").toString());
                WordMap.put("Snapshot_Preparing_rollback", LMF.get("Snapshot_Preparing_rollback").toString());
                WordMap.put("Snapshot_Preparing_unview", LMF.get("Snapshot_Preparing_unview").toString());  
                WordMap.put("Title_Snapshot_onlineviewing", LMF.get("Title_Snapshot_onlineviewing").toString());
                WordMap.put("Desc_Snapshot_operation1", LMF.get("Desc_Snapshot_operation1").toString());
                WordMap.put("Desc_Snapshot_operation2", LMF.get("Desc_Snapshot_operation2").toString());                
                WordMap.put("Snapshot_keepnic", LMF.get("Snapshot_keepnic").toString());
                WordMap.put("Snapshot_keepnic_Suggest", LMF.get("Snapshot_keepnic_Suggest").toString());                
                WordMap.put("Snapshot_SSDataEmpty", LMF.get("Snapshot_SSDataEmpty").toString());                
                WordMap.put("Alert_Title_Warning", LMF.get("Alert_Title_Warning").toString());
                WordMap.put("Alert_SSBtn_Keep", LMF.get("Alert_SSBtn_Keep").toString());
                WordMap.put("Alert_SSBtn_NotKeep", LMF.get("Alert_SSBtn_NotKeep").toString());
                WordMap.put("Snapshot", LMF.get("Snapshot").toString()); // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
                WordMap.put("TC_Setting", LMF.get("TC_Setting").toString());                 // 2018.01.29 william 雙螢幕實作  
                WordMap.put("Display_Disable", LMF.get("Display_Disable").toString());       // 2018.01.29 william 雙螢幕實作
                WordMap.put("Display_Primary", LMF.get("Display_Primary").toString());       // 2018.01.29 william 雙螢幕實作
                WordMap.put("Display_Duplicate", LMF.get("Display_Duplicate").toString());   // 2018.01.29 william 雙螢幕實作
                WordMap.put("Duplicate_Enable", LMF.get("Duplicate_Enable").toString());     // 2018.01.29 william 雙螢幕實作
                WordMap.put("Advanced_Settings", LMF.get("Advanced_Settings").toString());   // 2018.01.29 william 雙螢幕實作
                WordMap.put("Display_Extend", LMF.get("Display_Extend").toString());         // 2018.03.12 william 雙螢幕實作
                WordMap.put("Settings", LMF.get("Settings").toString());                     // 2018.03.12 william 雙螢幕實作
                WordMap.put("Display_Exchange", LMF.get("Display_Exchange").toString());     // 2018.03.12 william 雙螢幕實作
                WordMap.put("Only", LMF.get("Only").toString());                             // 2018.03.12 william 雙螢幕實作
                WordMap.put("Mode", LMF.get("Mode").toString());                             // 2018.03.12 william 雙螢幕實作
                WordMap.put("Wifi_Name", LMF.get("Wifi_Name").toString());   
                WordMap.put("Wifi_PW", LMF.get("Wifi_PW").toString());   
                WordMap.put("Wifi_List", LMF.get("Wifi_List").toString());                   
                WordMap.put("Security", LMF.get("Security").toString());
                WordMap.put("Refresh", LMF.get("Refresh").toString());
                WordMap.put("Connect", LMF.get("Connect").toString());
                WordMap.put("Disconnect", LMF.get("Disconnect").toString());   
                WordMap.put("Connected", LMF.get("Connected").toString());
                WordMap.put("Disconnected", LMF.get("Disconnected").toString());                  
                WordMap.put("Connecting", LMF.get("Connecting").toString());
                WordMap.put("Network", LMF.get("Network").toString());
                WordMap.put("IP_EnterWifiCheckMassage", LMF.get("IP_EnterWifiCheckMassage").toString()); 
                WordMap.put("WiFi_Enter_PWD_title", LMF.get("WiFi_Enter_PWD_title").toString()); 
                WordMap.put("Safe", LMF.get("Safe").toString());
                WordMap.put("Open", LMF.get("Open").toString());
                WordMap.put("Battery", LMF.get("Battery").toString());
                WordMap.put("Charging", LMF.get("Charging").toString());                
                WordMap.put("NotCharging", LMF.get("NotCharging").toString());
                WordMap.put("Message_Error_WiFi_01", LMF.get("Message_Error_WiFi_01").toString());
                WordMap.put("Message_Error_WiFi_02", LMF.get("Message_Error_WiFi_02").toString());
                WordMap.put("Message_advice_WiFi_01", LMF.get("Message_advice_WiFi_01").toString());   
                WordMap.put("Message_advice_WiFi_02", LMF.get("Message_advice_WiFi_02").toString());
                WordMap.put("Ethernet", LMF.get("Ethernet").toString());
                WordMap.put("Brightness", LMF.get("Brightness").toString());
                WordMap.put("VM_Migrate", LMF.get("VM_Migrate").toString());
                WordMap.put("VM_Preparing_Boot", LMF.get("VM_Preparing_Boot").toString());      
                WordMap.put("Message_Error_VD_428", LMF.get("Message_Error_VD_428").toString());
                WordMap.put("Message_Error_VD_429", LMF.get("Message_Error_VD_429").toString());                  
                WordMap.put("Snapshot_Rollback", LMF.get("Snapshot_Rollback").toString()); // 2018.07.06 快照新增還原按鈕  
                WordMap.put("Snapshot_RollBack_Confirm", LMF.get("Snapshot_RollBack_Confirm").toString()); // 2018.07.12 還原按鈕和修改dialog
                WordMap.put("Message_Error_MonitorSpice", LMF.get("Message_Error_MonitorSpice").toString());
                // 2018.11.11 新增AcroDesk & RDP Protocol 
                WordMap.put("Str_Status", LMF.get("Str_Status").toString());
                WordMap.put("Str_Protocol", LMF.get("Str_Protocol").toString());
                WordMap.put("Str_Protocol_2", LMF.get("Str_Protocol_2").toString());
                WordMap.put("Str_Shutdown", LMF.get("Str_Shutdown").toString());
                WordMap.put("Str_Preparing", LMF.get("Str_Preparing").toString());
                WordMap.put("Str_Ready", LMF.get("Str_Ready").toString());
                WordMap.put("Str_CannotConnect", LMF.get("Str_CannotConnect").toString());
                WordMap.put("Thread_Booting", LMF.get("Thread_Booting").toString());
                WordMap.put("Thread_Connecting", LMF.get("Thread_Connecting").toString());           
                WordMap.put("Message_RdpError_ConnectTimeout", LMF.get("Message_RdpError_ConnectTimeout").toString());
                WordMap.put("Message_RdpSuggest_Relogin", LMF.get("Message_RdpSuggest_Relogin").toString());                
                // 2018.12.04 新增VD&RDP狀態判斷
                WordMap.put("VM_Booting", LMF.get("VM_Booting").toString());                 
                WordMap.put("Message_RdpError_ConnectFail", LMF.get("Message_RdpError_ConnectFail").toString());
                WordMap.put("Message_RdpSuggest_ReConnect", LMF.get("Message_RdpSuggest_ReConnect").toString());    
                // 2018.12.12 新增 431NoVGA 和第一次開機
                WordMap.put("Message_Error_VD_431", LMF.get("Message_Error_VD_431").toString()); 
                WordMap.put("Message_First_Booting1", LMF.get("Message_First_Booting1").toString());
                WordMap.put("Message_First_Booting_Suggest1", LMF.get("Message_First_Booting_Suggest1").toString());     
                WordMap.put("Message_RDP_Stage_0", LMF.get("Message_RDP_Stage_0").toString());                 
                WordMap.put("Message_RDP_Stage_1", LMF.get("Message_RDP_Stage_1").toString());                 
                WordMap.put("Message_RDP_Stage_1_Error", LMF.get("Message_RDP_Stage_1_Error").toString());                 
                WordMap.put("Message_RDP_Stage_1_Suggest", LMF.get("Message_RDP_Stage_1_Suggest").toString());                 
                WordMap.put("Message_RDP_Stage_2", LMF.get("Message_RDP_Stage_2").toString());                 
                WordMap.put("Message_RDP_Stage_2_Error", LMF.get("Message_RDP_Stage_2_Error").toString());                 
                WordMap.put("Message_RDP_Stage_2_Suggest", LMF.get("Message_RDP_Stage_2_Suggest").toString());  
                WordMap.put("Message_RDP_Stage_3", LMF.get("Message_RDP_Stage_3").toString());                 
                WordMap.put("Message_RDP_Stage_3_Error", LMF.get("Message_RDP_Stage_3_Error").toString());                 
                WordMap.put("Message_RDP_Stage_3_Suggest", LMF.get("Message_RDP_Stage_3_Suggest").toString());  
                WordMap.put("Message_RDP_Stage_4", LMF.get("Message_RDP_Stage_4").toString());                 
                WordMap.put("Message_RDP_Stage_4_Error", LMF.get("Message_RDP_Stage_4_Error").toString());                 
                WordMap.put("Message_RDP_Stage_4_Suggest", LMF.get("Message_RDP_Stage_4_Suggest").toString());  
                // 2018.18.18 new create & reborn 
                WordMap.put("Message_NewCreate", LMF.get("Message_NewCreate").toString());  
                WordMap.put("Message_Reborn", LMF.get("Message_Reborn").toString());  
                WordMap.put("Message_ReadingProfile", LMF.get("Message_ReadingProfile").toString());  
                WordMap.put("Message_RDP_Stage_3_Profile", LMF.get("Message_RDP_Stage_3_Profile").toString());  
                WordMap.put("Message_RDP_Stage_3_NoProfile", LMF.get("Message_RDP_Stage_3_NoProfile").toString());  
                WordMap.put("Message_RDP_Stage_4_Profile", LMF.get("Message_RDP_Stage_4_Profile").toString());  
                WordMap.put("Message_RDP_Stage_4_NoProfile", LMF.get("Message_RDP_Stage_4_NoProfile").toString());  
                WordMap.put("Message_IsAssignedUserDisk_NULL", LMF.get("Message_IsAssignedUserDisk_NULL").toString());
                WordMap.put("Message_RDP_Booting", LMF.get("Message_RDP_Booting").toString());
                WordMap.put("Message_RDP_Preparing", LMF.get("Message_RDP_Preparing").toString());
                WordMap.put("Message_No_UserAccount", LMF.get("Message_No_UserAccount").toString());
                // 2018.12.27  其他中斷連線狀態 - Crash & Migrate
                WordMap.put("Message_Error_VMCrash", LMF.get("Message_Error_VMCrash").toString());
                WordMap.put("Message_Error_VMAnomaly", LMF.get("Message_Error_VMAnomaly").toString());                
                // 2018.12.28 block 3D vd
                WordMap.put("Message_Error_VD_loginFail", LMF.get("Message_Error_VD_loginFail").toString());
                WordMap.put("Message_Suggest_Error_451", LMF.get("Message_Suggest_Error_451").toString());     
                // 2019.01.02 view D Drive   tooltip_ViewDisk
                WordMap.put("ViewUserDisk", LMF.get("ViewUserDisk").toString());
                WordMap.put("Desc_ViewUserDisk_operation1", LMF.get("Desc_ViewUserDisk_operation1").toString());
                WordMap.put("Desc_ViewUserDisk_operation2", LMF.get("Desc_ViewUserDisk_operation2").toString());
                WordMap.put("Message_Error_VD_411", LMF.get("Message_Error_VD_411").toString());
                WordMap.put("TC_Ver_Updating", LMF.get("TC_Ver_Updating").toString());
                // 2019.03.28 VM/VD migration reconnect
                WordMap.put("Str_Dynamic_Transfer", LMF.get("Str_Dynamic_Transfer").toString());
                WordMap.put("Str_Dynamic_Transfer_Reconnect", LMF.get("Str_Dynamic_Transfer_Reconnect").toString());       
                // 2019.04.16 Ping IP方式修改                
                WordMap.put("Message_SpiceError_ConnectTimeout", LMF.get("Message_SpiceError_ConnectTimeout").toString());                    
                WordMap.put("Desc_Snapshot_operation3", LMF.get("Desc_Snapshot_operation3").toString());                    
                WordMap.put("Desc_ViewUserDisk_operation3", LMF.get("Desc_ViewUserDisk_operation3").toString());                                    
                WordMap.put("Desc_ViewUserDisk_operation1-1", LMF.get("Desc_ViewUserDisk_operation1-1").toString());   
                WordMap.put("KeyboardSetting_tab", LMF.get("KeyboardSetting_tab").toString());
                WordMap.put("KeyboardSetting_Title", LMF.get("KeyboardSetting_Title").toString());
                WordMap.put("KeyboardType_label", LMF.get("KeyboardType_label").toString());
            }else{
                WordMap.put("APP_Title", "AcroRed AcroViewer"); // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP   
                WordMap.put("Language", "Language");
                WordMap.put("Title", "AcroViewer"); // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP
                WordMap.put("IP_Addr", "IP");
                WordMap.put("Username", "Username");
                WordMap.put("Password", "Password");
                WordMap.put("Login", "Login");
                WordMap.put("ManageVD", "VD Management");
                WordMap.put("ChangePassword", "Change Password");
                WordMap.put("TestServer", "Test VDI Server");
                WordMap.put("Exit", "Exit");
                WordMap.put("Confirm", "Confirm");
                WordMap.put("Ping_Window_Title", "Test VDI Server");
                WordMap.put("Ping_Header", "Test VDI Server");
                WordMap.put("Ping_Message_F", "Test --- ");
                WordMap.put("Ping_Message_R1", " --- Success");
                WordMap.put("Ping_Message_R2", "Test --- Fail");
                WordMap.put("Cancel", "Cancel");
                WordMap.put("CP_Window_Title", "Change Password");
                WordMap.put("CP_Header", "Change Password");
                WordMap.put("CP_Original", "Current Password");
                WordMap.put("CP_New", "New Password");
                WordMap.put("CP_Confirm", "Confirm Password");
                WordMap.put("EW_Window_Title", "Error");
                WordMap.put("EW_Header", "Error");
                WordMap.put("EW_Window_Title", "Warning");
                WordMap.put("EW_Header", "Warning");
                WordMap.put("ID_Name", "ID");
                WordMap.put("VD_Name", "VD Name");
                WordMap.put("Stop", "Auto Pause");
                WordMap.put("Mode", "Mode");
                WordMap.put("Online", "Online Mode");
                WordMap.put("Time", "Created Time");
                WordMap.put("Comment", "Comment");
                WordMap.put("Action", "Action");
                WordMap.put("Shutdown", "Poweroff");
                WordMap.put("VM_Disable", "Disable");
                WordMap.put("VM_Stop", "Stop");
                WordMap.put("VM_Waiting_Poweron", "Waiting Poweron");
                WordMap.put("VM_Poweron", "Poweron");
                WordMap.put("VM_Blocked", "Blocked");
                WordMap.put("VM_Pause", "Suspend");
                WordMap.put("VM_Shutdowning", "Shutdowning");
                WordMap.put("VM_Shutdown", "Shutdown");
                WordMap.put("VM_Crash", "Crash");
                WordMap.put("VM_Suspend", "Suspend");
                WordMap.put("VDO_Status", "VD Online");
                WordMap.put("SelectedLanguage", "English");
                WordMap.put("SelectedLanguage", "TraditionalChinese");
                WordMap.put("SelectedLanguage", "SimpleChinese");
                WordMap.put("ExitMassage", "Are you sure you want to shutdown？");
                WordMap.put("Message_IP_Addr", "Host IP");
                WordMap.put("Message_Username", "Username");
                WordMap.put("Message_Password", "Password");
                WordMap.put("Message", "Message");
                WordMap.put("Message_Input_IP", "Please enter the correct Host IP.");
                WordMap.put("Message_Input_User_PW", "Login failed. The Username or Password is incorrect!");               
                WordMap.put("Message_Input_ALL", "Please enter the correct Host IP, Username and Password.");
                WordMap.put("Message_Error_User_PW", "Please enter the correct Username and Password.");
                WordMap.put("Message_Error_OPW", "Original Password, Error!");
                WordMap.put("Message_Correct_OPW", "Please re-enter the correct Original Password.");
                WordMap.put("Message_Error_NPW_CPW", "New Password and Confirm New Password, Error!");
                WordMap.put("Message_Correct_NPW_CPW", "Please re-enter the New Password and confirm the New Password.");
                WordMap.put("Message_Login", "Login");
                WordMap.put("Message_Lock", "Login failed. User is Locked!");
                WordMap.put("Message_VD_Online", "The VD is online, do you still want to connect? if you connect it then the previous one will be disconnected automatically.");
                WordMap.put("Alert_Cancel", "Cancel");
                WordMap.put("Alert_Confirm", "Confirm");
                WordMap.put("VD_Stop_true", "Enable");
                WordMap.put("VD_Stop_false", "Disable");
                WordMap.put("VD_Online_true", "Connected");
                WordMap.put("VD_Online_false", "Disconnected");
                WordMap.put("Read_password", "Remember the password");
                WordMap.put("Message_Error_IP", "Host IP is incorrected!");
                WordMap.put("Message_Error_IP_01", "Login failed, it may be the host IP format error!");
                WordMap.put("Message_Error_IP_02", "Please check the network to verify if it is connected.");
                WordMap.put("Message_Error_IP_03", "Please enter the correct Host IP, or");
                WordMap.put("Message_Error_404", "Login failed. This IP may not be AcroRed Host IP!");
                WordMap.put("ManageVD_Title", "VD Management");
                WordMap.put("Message_Suggest", "Suggestion");
                WordMap.put("Message_Error_IP_04", "Please make sure that the IP you entered is correct.");
                WordMap.put("Message_Error_IP_403", "Please ask support from System Administrator.");
                WordMap.put("Message_Error_IP_05", "If you have entered the correct IP, please ask support from System Administrator.");
                WordMap.put("ThinClient", "Thin Client Setting");
                WordMap.put("Web_Title", "Firefox Web Browser");
                WordMap.put("TC_Exit", "Exit");
                WordMap.put("TC_Cancel", "Cancel");
                WordMap.put("TC_Confirm", "Confirm");
                WordMap.put("TC_Display", "Display");
                WordMap.put("TC_System", "System");
                WordMap.put("TC_Static_IP", "Static IP");
                WordMap.put("TC_Static_IP_Setting", "Static IP Setting");
                WordMap.put("TC_IP_addr", "IP Address");
                WordMap.put("TC_Mask", "Mask");
                WordMap.put("TC_Def_Getway", "Default Gateway");
                WordMap.put("TC_Static_DNS_Setting", "Static DNS Setting");
                WordMap.put("TC_Monitor", "Display Settings");
                WordMap.put("TC_Resolution", "Resolution");
                WordMap.put("TC_Client_Firmware", "Client Firmware");
                WordMap.put("TC_Current_Ver", "Current Firmware Version");
                WordMap.put("TC_Server_Ver", "Host Firmware Version");
                WordMap.put("TC_Server_Ver_AlertMessage", "1. Current F/W is the latest version, no need to update."); // 2017.06.22 william System version check
                WordMap.put("TC_Server_Ver_AlertEvent", "2. Please press Exit, or choose another operation"); // 2017.06.22 william System version check
                WordMap.put("TC_Check", "Check");
                WordMap.put("TC_Update", "Update");
                WordMap.put("Message_Error_IP_06", "Login failed, it may be that the Host IP does not exist, or the network has been disconnected!");
                WordMap.put("Message_Error_IP_07", "Please enter the correct Host IP.");
                WordMap.put("Message_Error_VM_IP_01", "Can not enter VD Management, it may be the host IP format error!");
                WordMap.put("Message_Error_VM_IP_02", "Can not enter VD Management, it may be that the Host IP does not exist, or the network has been disconnected!");
                WordMap.put("Message_Error_VM_IP_404", "Can not enter VD Management. This IP may not be AcroRed Host IP!");
                WordMap.put("Message_Error_VM_IP_401", "Can not enter VD Management. The Username or Password is incorrect!");
                WordMap.put("Message_Error_VM_IP_403", "Can not enter VD Management. User is Locked!");
                WordMap.put("Message_Error_CP_IP_01", "Can not enter Change Password, it may be the host IP format error!");
                WordMap.put("Message_Error_CP_IP_02", "Can not enter Change Password, it may be that the Host IP does not exist, or the network has been disconnected!");
                WordMap.put("Message_Error_CP_IP_404", "Can not enter Change Password. This IP may not be AcroRed Host IP!");
                WordMap.put("Message_Error_CP_IP_401", "Can not enter Change Password. The Username or Password is incorrect!");
                WordMap.put("Message_Error_CP_IP_403", "Can not enter Change Password. User is Locked!");
                WordMap.put("TC_Last_Update", "Last updated date");
                WordMap.put("VD_SN", "SN");
                WordMap.put("Message_Error_VD_503", "Login failed, out of Resources!");
                WordMap.put("Message_Error_VD_406", "Login failed, too many users!");
                WordMap.put("Message_Error_VD_412", "Login failed, out of memory!");
                WordMap.put("Message_Error_VD_500", "Login failed, System Error!");
                WordMap.put("Message_Error_VD_403", "Login failed, VD disabled!");
                WordMap.put("Message_Error_VD_417", "Login failed,  VD in Task!");
                WordMap.put("Message_Error_VD_410", "Login failed, VD server not available!");
                WordMap.put("Button_Shutdown", "Shutdown");
                WordMap.put("Button_Reboot", "Reboot");
                WordMap.put("Button_Network_ON", "Internet-Enabled");
                WordMap.put("Button_Nectwork_OFF", "Disabled");
                WordMap.put("RebootMassage", "Are you sure you want to restart?");
                WordMap.put("Comfirm_RebootMassage", "The computer will be restarted.");                
                WordMap.put("IP_EnterCheckMassage", "IP Address and Mask can not be blank."); // 2017.07.12 william remove internet plug & DNS Exception fix
                WordMap.put("Message_Error_setVDonline", "The server IP can not connect!");
                WordMap.put("Dispaly_Check_button", "Detects the screen");
                WordMap.put("Dispaly_PathChange_button", "Change screen orientation");
                WordMap.put("TC_Check_Error_IP", "Can not Check new version, it may be the host IP format error!");
                WordMap.put("TC_Check_Error", "Can not Check new version, it may be that the Host IP does not exist, or the network has been disconnected!");
                WordMap.put("TC_Update_Error", "Update failed!");
                WordMap.put("TC_Update_Error_Suggest", "Please update again.");
                WordMap.put("TC_Update_Suggest", "When the update is complete, it will automatically reboot, are you sure you want to update?");
                WordMap.put("TimeCalendar_title", "Date and time");
                WordMap.put("TimeCalendar_Settings", "Advanced settings");
                WordMap.put("TimeSettings_title", "Time Setting");
                WordMap.put("TimeSettings_update", "Update Now");
                WordMap.put("TimeSettings_CurrentTime", "Current Time");
                WordMap.put("TimeSettings_TimeZone", "Time Zone");
                WordMap.put("TimeSettings_Manually", "Manually");
                WordMap.put("TimeSettings_AutoSync", "Auto Sync.");
                WordMap.put("TimeSettings_date", "Date");
                WordMap.put("TimeSettings_time", "Time");
                WordMap.put("TimeSettings_ServerAddress", "Server Address");
                // 2017.08.10 william 登入中thread畫面鎖住    
                WordMap.put("Thread_Login", "Loading");
                WordMap.put("Thread_Waiting", "Please wait");    
                //  2017.08.10 william 400錯誤處理
                WordMap.put("Message_Bad_Request", "There is a problem with the server！");   
                WordMap.put("Message_Error_400", "Please contact the information staff！");    
                //  2017.09.18 william 錯誤修改(4) 其他未知的錯誤
                WordMap.put("Message_Unknown_system_error", "Unknown system error");    
                // 2017.11.24 william 單一帳號多個VD登入
                WordMap.put("Select_VD", "Login VD");    
                WordMap.put("LoginMultiVD_desc1", "1. Please select the VD and click confirm to login VD."); 
                WordMap.put("LoginMultiVD_desc2", "2. Or click the Exit button to close the window."); 
                WordMap.put("LoginMultiVD_Mark", "Mark"); 
                // 2017.12.12 william SnapShot實作
                WordMap.put("Select_Snapshot_Layer", "Please select Snapshot Layer");                    
                WordMap.put("Snapshot_View", "View");
                WordMap.put("Snapshot_Stop_View", "Stop");
                WordMap.put("Snapshot_Login", "Login");
                WordMap.put("Snapshot_Exit", "Exit");
                WordMap.put("Snapshot_State", "State");
                WordMap.put("Snapshot_CreateTime", "Create Time");
                WordMap.put("Snapshot_Size", "Size");
                WordMap.put("Snapshot_Description", "Description");   
                WordMap.put("Snapshot_Normal", "Normal");
                WordMap.put("Snapshot_Preparing_view", "Preparing view");
                WordMap.put("Snapshot_state_View", "View");
                WordMap.put("Snapshot_Preparing_delete", "Preparing delete");
                WordMap.put("Snapshot_Preparing_rollback", "Preparing rollback");
                WordMap.put("Snapshot_Preparing_unview", "Preparing unview");    
                WordMap.put("Title_Snapshot_onlineviewing", "Snapshot online viewing");
                WordMap.put("Desc_Snapshot_operation1", "1. Select a VM/VD and then select a Snapshot Layer which you want to review.");
                WordMap.put("Desc_Snapshot_operation2", "2. Click 『Stop』 button to stop the viewing.");                
                WordMap.put("Snapshot_keepnic", "Would you keep the NIC settings?");
                WordMap.put("Snapshot_keepnic_Suggest", "It recommended not to keep the NIC settings.");
                WordMap.put("Snapshot_SSDataEmpty", "Please first create a snapshot, and then view it.");                
                WordMap.put("Alert_Title_Warning", "Warning!");
                WordMap.put("Alert_SSBtn_Keep", "Keep");
                WordMap.put("Alert_SSBtn_NotKeep", "Not Keep");      
                WordMap.put("Snapshot", "Snapshot"); // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
                WordMap.put("TC_Setting", "Setting");            // 2018.01.29 william 雙螢幕實作  
                WordMap.put("Display_Disable", "Disable");       // 2018.01.29 william 雙螢幕實作
                WordMap.put("Display_Primary", "Primary");       // 2018.01.29 william 雙螢幕實作
                WordMap.put("Display_Duplicate", "Duplicate");   // 2018.01.29 william 雙螢幕實作  
                WordMap.put("Duplicate_Enable", "Enable");       // 2018.01.29 william 雙螢幕實作
                WordMap.put("Advanced_Settings", "Advanced");    // 2018.01.29 william 雙螢幕實作
                WordMap.put("Display_Extend", "Extend");         // 2018.03.12 william 雙螢幕實作
                WordMap.put("Settings", "Settings");             // 2018.03.12 william 雙螢幕實作                
                WordMap.put("Display_Exchange", "Exchange");     // 2018.03.12 william 雙螢幕實作
                WordMap.put("Only", "Only");                     // 2018.03.12 william 雙螢幕實作
                WordMap.put("Mode", "Mode");                     // 2018.03.12 william 雙螢幕實作
                WordMap.put("Wifi_Name", "Name");   
                WordMap.put("Wifi_PW", "Password");   
                WordMap.put("Wifi_List", "Search");      
                WordMap.put("Security", "Security");
                WordMap.put("Refresh", "Refresh");
                WordMap.put("Connect", "Connect");
                WordMap.put("Disconnect", "Disconnect");   
                WordMap.put("Connected", "Connected");
                WordMap.put("Disconnected", "Disconnected");  
                WordMap.put("Connecting", "Connecting");
                WordMap.put("Network", "Network");
                WordMap.put("IP_EnterWifiCheckMassage", "Name and Password can not be blank."); 
                WordMap.put("WiFi_Enter_PWD_title", "Enter Wi-Fi Password"); 
                WordMap.put("Safe", "Safe");
                WordMap.put("Open", "Open");
                WordMap.put("Battery", "Battery");
                WordMap.put("Charging", "Charging");
                WordMap.put("NotCharging", "Not Charging");
                WordMap.put("Message_Error_WiFi_01", "Wi-Fi Password is blank!");
                WordMap.put("Message_Error_WiFi_02", "The Wi-Fi Password is incorrect!");
                WordMap.put("Message_advice_WiFi_01", "Please enter the Wi-Fi Password.");                  
                WordMap.put("Message_advice_WiFi_02", "Please Connect the Wi-Fi.");
                WordMap.put("Ethernet", "Ethernet");
                WordMap.put("Brightness", "Brightness");
                WordMap.put("VM_Migrate", "Migrating");
                WordMap.put("VM_Preparing_Boot", "Preparing Boot");      
                WordMap.put("Message_Error_VD_428", "VD is preparing boot!");
                WordMap.put("Message_Error_VD_429", "VD is migrating!");     
                WordMap.put("Snapshot_Rollback", "Rollback"); // 2018.07.06 快照新增還原按鈕        
                WordMap.put("Snapshot_RollBack_Confirm", "Are you sure rollback designated snapshot layer？"); // 2018.07.12 還原按鈕和修改dialog
                WordMap.put("Message_Error_MonitorSpice", "An exception occurred while logging in. Please click the \"Reboot\" button to reboot!");                
                // 2018.11.11 新增AcroDesk & RDP Protocol 
                WordMap.put("Str_Status", "VD Status");
                WordMap.put("Str_Protocol", "Protocol Status");
                WordMap.put("Str_Protocol_2", "Protocol");
                WordMap.put("Str_Shutdown", "Shutdown");
                WordMap.put("Str_Preparing", "Preparing");
                WordMap.put("Str_Ready", "Ready");
                WordMap.put("Str_CannotConnect", "Can't Connect");    
                WordMap.put("Thread_Booting", "Booting");
                WordMap.put("Thread_Connecting", "Connecting");     
                WordMap.put("Message_RdpError_ConnectTimeout","RDP connection timeout");
                WordMap.put("Message_RdpSuggest_Relogin", "Please log in again");     
                // 2018.12.04 新增VD&RDP狀態判斷
                WordMap.put("VM_Booting", "Booting");                 
                WordMap.put("Message_RdpError_ConnectFail", "The VD is in the state of 『Shutting down』 or 『Reboot』 or 『Disconnected』.");
                WordMap.put("Message_RdpSuggest_ReConnect", "Please reconnect after one minute.");  
                // 2018.12.12 新增 431NoVGA 和第一次開機
                WordMap.put("Message_Error_VD_431", "Connection failed. There is no VGA card available for the VD！"); 
                WordMap.put("Message_First_Booting1", "VD is creating 『User Profile』.");
                WordMap.put("Message_First_Booting_Suggest1", "Please wait 1~2 minutes");                
                WordMap.put("Message_RDP_Stage_0", "Preparing Boot");                 
                WordMap.put("Message_RDP_Stage_1", "Read user data");                 
                WordMap.put("Message_RDP_Stage_1_Error", "Read user data connection timeout!");                 
                WordMap.put("Message_RDP_Stage_1_Suggest", "Please reconnect after one minute.");                 
                WordMap.put("Message_RDP_Stage_2", "Check user HDDs");                 
                WordMap.put("Message_RDP_Stage_2_Error", "Check user HDDs connection timeout!");                 
                WordMap.put("Message_RDP_Stage_2_Suggest", "Please reconnect after one minute.");  
                WordMap.put("Message_RDP_Stage_3", "Format user HDDs");                 
                WordMap.put("Message_RDP_Stage_3_Error", "Format user HDDs connection timeout!");                 
                WordMap.put("Message_RDP_Stage_3_Suggest", "Please reconnect after one minute.");  
                WordMap.put("Message_RDP_Stage_4", "Restore user data");                 
                WordMap.put("Message_RDP_Stage_4_Error", "Restore user data connection timeout!");                 
                WordMap.put("Message_RDP_Stage_4_Suggest", "Please reconnect after one minute.");     
                // 2018.18.18 new create & reborn 
                WordMap.put("Message_NewCreate", "Create new VD(it takes about 1~2 minutes), please wait!");  
                WordMap.put("Message_Reborn", "Reborn VD(it takes about 1 minute), please wait!");  
                WordMap.put("Message_ReadingProfile", "Data reading");  
                WordMap.put("Message_RDP_Stage_3_Profile", "Create account and format user HDDs");  
                WordMap.put("Message_RDP_Stage_3_NoProfile", "Create an account");  
                WordMap.put("Message_RDP_Stage_4_Profile", "Create account and restore user profile");  
                WordMap.put("Message_RDP_Stage_4_NoProfile", "Create an account");      
                WordMap.put("Message_IsAssignedUserDisk_NULL", "Can't Connect");
                WordMap.put("Message_RDP_Booting", "The VD boot(it takes about 1 minute), please wait!");
                WordMap.put("Message_RDP_Preparing", "Preparing");
                WordMap.put("Message_No_UserAccount", "No User Account！");
                // 2018.12.27  其他中斷連線狀態 - Crash & Migrate
                WordMap.put("Message_Error_VMCrash", "VD has crashed！");
                WordMap.put("Message_Error_VMAnomaly", "VD Anomaly");    
                // 2018.12.28 block 3D vd
                WordMap.put("Message_Error_VD_loginFail", "Login failed！");
                WordMap.put("Message_Suggest_Error_451", "Please use the AcroRDP protocol to re-login");   
                // 2019.01.02 view D Drive   tooltip_ViewDisk
                WordMap.put("ViewUserDisk", "View user disk"); 
                WordMap.put("Desc_ViewUserDisk_operation1", "1. Please shutdown the VD first, then select \"View\", \"Rollback\" or \"Stop View\" button.");
                WordMap.put("Desc_ViewUserDisk_operation2", "2. Click 『Stop』 button to stop the viewing.");       
                WordMap.put("Message_Error_VD_411", "Login failed,  user disk is in Task！");      
                WordMap.put("TC_Ver_Updating", "Updating, Please wait");
                // 2019.03.28 VM/VD migration reconnect
                WordMap.put("Str_Dynamic_Transfer", "Dynamic Transfer");  
                WordMap.put("Str_Dynamic_Transfer_Reconnect", "Reconnecting");   
                // 2019.04.16 Ping IP方式修改                
                WordMap.put("Message_SpiceError_ConnectTimeout", "AcroSpice connection timeout！");                     
                WordMap.put("Desc_Snapshot_operation3", "");                    
                WordMap.put("Desc_ViewUserDisk_operation3", "");        
                WordMap.put("Desc_ViewUserDisk_operation1-1", "");     
                WordMap.put("KeyboardSetting_tab", "");
                WordMap.put("KeyboardSetting_Title", "");
                WordMap.put("KeyboardType_label", "");                
            }
        }catch(IOException e){
           // e.printStackTrace();
                WordMap.put("APP_Title", "AcroRed AcroViewer"); // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP
                WordMap.put("Language", "Language");
                WordMap.put("Title", "AcroViewer"); // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP
                WordMap.put("IP_Addr", "IP");
                WordMap.put("Username", "Username");
                WordMap.put("Password", "Password");
                WordMap.put("Login", "Login");
                WordMap.put("ManageVD", "VD Management");
                WordMap.put("ChangePassword", "Change Password");
                WordMap.put("TestServer", "Test VDI Server");
                WordMap.put("Exit", "Exit");
                WordMap.put("Confirm", "Confirm");
                WordMap.put("Ping_Window_Title", "Test VDI Server");
                WordMap.put("Ping_Header", "Test VDI Server");
                WordMap.put("Ping_Message_F", "Test --- ");
                WordMap.put("Ping_Message_R1", " --- Success");
                WordMap.put("Ping_Message_R2", "Test --- Fail");
                WordMap.put("Cancel", "Cancel");
                WordMap.put("CP_Window_Title", "Change Password");
                WordMap.put("CP_Header", "Change Password");
                WordMap.put("CP_Original", "Current Password");
                WordMap.put("CP_New", "New Password");
                WordMap.put("CP_Confirm", "Confirm Password");
                WordMap.put("EW_Window_Title", "Error");
                WordMap.put("EW_Header", "Error");
                WordMap.put("EW_Window_Title", "Warning");
                WordMap.put("EW_Header", "Warning");
                WordMap.put("ID_Name", "ID");
                WordMap.put("VD_Name", "VD Name");
                WordMap.put("Stop", "Auto Pause");
                WordMap.put("Mode", "Mode");
                WordMap.put("Online", "Online Mode");
                WordMap.put("Time", "Created Time");
                WordMap.put("Comment", "Comment");
                WordMap.put("Action", "Action");
                WordMap.put("Shutdown", "Poweroff");
                WordMap.put("VM_Disable", "Disable");
                WordMap.put("VM_Stop", "Stop");
                WordMap.put("VM_Waiting_Poweron", "Waiting Poweron");
                WordMap.put("VM_Poweron", "Poweron");
                WordMap.put("VM_Blocked", "Blocked");
                WordMap.put("VM_Pause", "Suspend");
                WordMap.put("VM_Shutdowning", "Shutdowning");
                WordMap.put("VM_Shutdown", "Shutdown");
                WordMap.put("VM_Crash", "Crash");
                WordMap.put("VM_Suspend", "Suspend");
                WordMap.put("VDO_Status", "VD Online");
                WordMap.put("SelectedLanguage", "English");
                WordMap.put("SelectedLanguage", "TraditionalChinese");
                WordMap.put("SelectedLanguage", "SimpleChinese");
                WordMap.put("ExitMassage", "Are you sure you want to shutdown？");
                WordMap.put("Message_IP_Addr", "Host IP");
                WordMap.put("Message_Username", "Username");
                WordMap.put("Message_Password", "Password");
                WordMap.put("Message", "Message");
                WordMap.put("Message_Input_IP", "Please enter the correct Host IP.");
                WordMap.put("Message_Input_User_PW", "Login failed. The Username or Password is incorrect!");               
                WordMap.put("Message_Input_ALL", "Please enter the correct Host IP, Username and Password.");
                WordMap.put("Message_Error_User_PW", "Please enter the correct Username and Password.");
                WordMap.put("Message_Error_OPW", "Original Password, Error!");
                WordMap.put("Message_Correct_OPW", "Please re-enter the correct Original Password.");
                WordMap.put("Message_Error_NPW_CPW", "New Password and Confirm New Password, Error!");
                WordMap.put("Message_Correct_NPW_CPW", "Please re-enter the New Password and confirm the New Password.");
                WordMap.put("Message_Login", "Login");
                WordMap.put("Message_Lock", "Login failed. User is Locked!");
                WordMap.put("Message_VD_Online", "The VD is online, do you still want to connect? if you connect it then the previous one will be disconnected automatically.");
                WordMap.put("Alert_Cancel", "Cancel");
                WordMap.put("Alert_Confirm", "Confirm");
                WordMap.put("VD_Stop_true", "Enable");
                WordMap.put("VD_Stop_false", "Disable");
                WordMap.put("VD_Online_true", "Connected");
                WordMap.put("VD_Online_false", "Disconnected");
                WordMap.put("Read_password", "Remember the password");
                WordMap.put("Message_Error_IP", "Host IP is incorrected!");
                WordMap.put("Message_Error_IP_01", "Login failed, it may be the host IP format error!");
                WordMap.put("Message_Error_IP_02", "Please check the network to verify if it is connected.");
                WordMap.put("Message_Error_IP_03", "Please enter the correct Host IP, or");
                WordMap.put("Message_Error_404", "Login failed. This IP may not be AcroRed Host IP!");
                WordMap.put("ManageVD_Title", "VD Management");
                WordMap.put("Message_Suggest", "Suggestion");
                WordMap.put("Message_Error_IP_04", "Please make sure that the IP you entered is correct.");
                WordMap.put("Message_Error_IP_403", "Please ask support from System Administrator.");
                WordMap.put("Message_Error_IP_05", "If you have entered the correct IP, please ask support from System Administrator.");
                WordMap.put("ThinClient", "Thin Client Setting");
                WordMap.put("Web_Title", "Firefox Web Browser");
                WordMap.put("TC_Exit", "Exit");
                WordMap.put("TC_Cancel", "Cancel");
                WordMap.put("TC_Confirm", "Confirm");
                WordMap.put("TC_Display", "Display");
                WordMap.put("TC_System", "System");
                WordMap.put("TC_Static_IP", "Static IP");
                WordMap.put("TC_Static_IP_Setting", "Static IP Setting");
                WordMap.put("TC_IP_addr", "IP Address");
                WordMap.put("TC_Mask", "Mask");
                WordMap.put("TC_Def_Getway", "Default Gateway");
                WordMap.put("TC_Static_DNS_Setting", "Static DNS Setting");
                WordMap.put("TC_Monitor", "Display Settings");
                WordMap.put("TC_Resolution", "Resolution");
                WordMap.put("TC_Client_Firmware", "Client Firmware");
                WordMap.put("TC_Current_Ver", "Current Firmware Version");
                WordMap.put("TC_Server_Ver", "Host Firmware Version");
                WordMap.put("TC_Server_Ver_AlertMessage", "1. Current F/W is the latest version, no need to update."); // 2017.06.22 william System version check
                WordMap.put("TC_Server_Ver_AlertEvent", "2. Please press Exit, or choose another operation"); // 2017.06.22 william System version check               
                WordMap.put("TC_Check", "Check");
                WordMap.put("TC_Update", "Update");
                WordMap.put("Message_Error_IP_06", "Login failed, it may be that the Host IP does not exist, or the network has been disconnected!");
                WordMap.put("Message_Error_IP_07", "Please enter the correct Host IP.");
                WordMap.put("Message_Error_VM_IP_01", "Can not enter VD Management, it may be the host IP format error!");
                WordMap.put("Message_Error_VM_IP_02", "Can not enter VD Management, it may be that the Host IP does not exist, or the network has been disconnected!");
                WordMap.put("Message_Error_VM_IP_404", "Can not enter VD Management. This IP may not be AcroRed Host IP!");
                WordMap.put("Message_Error_VM_IP_401", "Can not enter VD Management. The Username or Password is incorrect!");
                WordMap.put("Message_Error_VM_IP_403", "Can not enter VD Management. User is Locked!");
                WordMap.put("Message_Error_CP_IP_01", "Can not enter Change Password, it may be the host IP format error!");
                WordMap.put("Message_Error_CP_IP_02", "Can not enter Change Password, it may be that the Host IP does not exist, or the network has been disconnected!");
                WordMap.put("Message_Error_CP_IP_404", "Can not enter Change Password. This IP may not be AcroRed Host IP!");
                WordMap.put("Message_Error_CP_IP_401", "Can not enter Change Password. The Username or Password is incorrect!");
                WordMap.put("Message_Error_CP_IP_403", "Can not enter Change Password. User is Locked!");
                WordMap.put("TC_Last_Update", "Last updated date");
                WordMap.put("VD_SN", "SN");
                WordMap.put("Message_Error_VD_503", "Login failed, out of Resources!");
                WordMap.put("Message_Error_VD_406", "Login failed, too many users!");
                WordMap.put("Message_Error_VD_412", "Login failed, out of memory!");
                WordMap.put("Message_Error_VD_500", "Login failed, System Error!");
                WordMap.put("Message_Error_VD_403", "Login failed, VD disabled!");
                WordMap.put("Message_Error_VD_417", "Login failed,  VD in Task!");
                WordMap.put("Message_Error_VD_410", "Login failed, VD server not available!");
                WordMap.put("Button_Shutdown", "Shutdown");
                WordMap.put("Button_Reboot", "Reboot");
                WordMap.put("Button_Network_ON", "Internet-Enabled");
                WordMap.put("Button_Nectwork_OFF", "Disabled");
                WordMap.put("RebootMassage", "Are you sure you want to restart?");
                WordMap.put("Comfirm_RebootMassage", "The computer will be restarted.");
                WordMap.put("IP_EnterCheckMassage", "IP Address and Mask can not be blank."); // 2017.07.12 william remove internet plug & DNS Exception fix
                WordMap.put("Message_Error_setVDonline", "The server IP can not connect!");
                WordMap.put("Dispaly_Check_button", "Detects the screen");
                WordMap.put("Dispaly_PathChange_button", "Change screen orientation");
                WordMap.put("TC_Check_Error_IP", "Can not Check new version, it may be the host IP format error!");
                WordMap.put("TC_Check_Error", "Can not Check new version, it may be that the Host IP does not exist, or the network has been disconnected!");
                WordMap.put("TC_Update_Error", "Update failed!");
                WordMap.put("TC_Update_Error_Suggest", "Please update again.");
                WordMap.put("TC_Update_Suggest", "When the update is complete, it will automatically reboot, are you sure you want to update?");
                WordMap.put("TimeCalendar_title", "Date and time");
                WordMap.put("TimeCalendar_Settings", "Advanced settings");
                WordMap.put("TimeSettings_title", "Time Setting");
                WordMap.put("TimeSettings_update", "Update Now");
                WordMap.put("TimeSettings_CurrentTime", "Current Time");
                WordMap.put("TimeSettings_TimeZone", "Time Zone");
                WordMap.put("TimeSettings_Manually", "Manually");
                WordMap.put("TimeSettings_AutoSync", "Auto Sync.");
                WordMap.put("TimeSettings_date", "Date");
                WordMap.put("TimeSettings_time", "Time");
                WordMap.put("TimeSettings_ServerAddress", "Server Address");
                // 2017.08.10 william 登入中thread畫面鎖住    
                WordMap.put("Thread_Login", "Loading");
                WordMap.put("Thread_Waiting", "Please wait");     
                //  2017.08.10 william 400錯誤處理
                WordMap.put("Message_Bad_Request", "There is a problem with the server！");   
                WordMap.put("Message_Error_400", "Please contact the information staff！");    
                //  2017.09.18 william 錯誤修改(4) 其他未知的錯誤
                WordMap.put("Message_Unknown_system_error", "Unknown system error");   
                // 2017.11.24 william 單一帳號多個VD登入
                WordMap.put("Select_VD", "Login VD");  
                WordMap.put("LoginMultiVD_desc1", "1. Please select the VD and click confirm to login VD."); 
                WordMap.put("LoginMultiVD_desc2", "2. Or click the Exit button to close the window."); 
                WordMap.put("LoginMultiVD_Mark", "Mark"); 
                // 2017.12.12 william SnapShot實作
                WordMap.put("Select_Snapshot_Layer", "Please select Snapshot Layer"); 
                WordMap.put("Snapshot_View", "View");
                WordMap.put("Snapshot_Stop_View", "Stop");
                WordMap.put("Snapshot_Login", "Login");
                WordMap.put("Snapshot_Exit", "Exit");                
                WordMap.put("Snapshot_State", "State");
                WordMap.put("Snapshot_CreateTime", "Create Time");
                WordMap.put("Snapshot_Size", "Size");
                WordMap.put("Snapshot_Description", "Description");                
                WordMap.put("Snapshot_Normal", "Normal");
                WordMap.put("Snapshot_Preparing_view", "Preparing view");
                WordMap.put("Snapshot_state_View", "View");
                WordMap.put("Snapshot_Preparing_delete", "Preparing delete");
                WordMap.put("Snapshot_Preparing_rollback", "Preparing rollback");
                WordMap.put("Snapshot_Preparing_unview", "Preparing unview");
                WordMap.put("Title_Snapshot_onlineviewing", "Snapshot online viewing");                
                WordMap.put("Desc_Snapshot_operation1", "1. Select a VM/VD and then select a Snapshot Layer which you want to review.");
                WordMap.put("Desc_Snapshot_operation2", "2. Click 『Stop』 button to stop the viewing.");                
                WordMap.put("Snapshot_keepnic", "Would you keep the NIC settings?");
                WordMap.put("Snapshot_keepnic_Suggest", "It recommended not to keep the NIC settings.");
                WordMap.put("Snapshot_SSDataEmpty", "Please first create a snapshot, and then view it.");
                WordMap.put("Alert_Title_Warning", "Warning!");
                WordMap.put("Alert_SSBtn_Keep", "Keep");
                WordMap.put("Alert_SSBtn_NotKeep", "Not Keep");    
                WordMap.put("Snapshot", "Snapshot"); // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色                
                WordMap.put("TC_Setting", "Setting"); // 2018.01.29 william 雙螢幕實作  
                WordMap.put("Display_Disable", "Disable");       // 2018.01.29 william 雙螢幕實作
                WordMap.put("Display_Primary", "Primary");       // 2018.01.29 william 雙螢幕實作
                WordMap.put("Display_Duplicate", "Duplicate");   // 2018.01.29 william 雙螢幕實作    
                WordMap.put("Duplicate_Enable", "Enable");       // 2018.01.29 william 雙螢幕實作                 
                WordMap.put("Advanced_Settings", "Advanced");    // 2018.01.29 william 雙螢幕實作  
                WordMap.put("Display_Extend", "Extend");         // 2018.03.12 william 雙螢幕實作
                WordMap.put("Settings", "Settings");             // 2018.03.12 william 雙螢幕實作                 
                WordMap.put("Display_Exchange", "Exchange");     // 2018.03.12 william 雙螢幕實作
                WordMap.put("Only", "Only");                     // 2018.03.12 william 雙螢幕實作
                WordMap.put("Mode", "Mode");                     // 2018.03.12 william 雙螢幕實作
                WordMap.put("Wifi_Name", "Name");   
                WordMap.put("Wifi_PW", "Password");   
                WordMap.put("Wifi_List", "Search");    
                WordMap.put("Security", "Security");
                WordMap.put("Refresh", "Refresh");
                WordMap.put("Connect", "Connect");
                WordMap.put("Disconnect", "Disconnect");
                WordMap.put("Connected", "Connected");
                WordMap.put("Disconnected", "Disconnected");     
                WordMap.put("Connecting", "Connecting");
                WordMap.put("Network", "Network");
                WordMap.put("IP_EnterWifiCheckMassage", "Name and Password can not be blank."); 
                WordMap.put("WiFi_Enter_PWD_title", "Enter Wi-Fi Password");
                WordMap.put("Safe", "Safe");
                WordMap.put("Open", "Open");
                WordMap.put("Battery", "Battery");
                WordMap.put("Charging", "Charging");
                WordMap.put("NotCharging", "Not Charging");
                WordMap.put("Message_Error_WiFi_01", "Wi-Fi Password is blank!");
                WordMap.put("Message_Error_WiFi_02", "The Wi-Fi Password is incorrect!");
                WordMap.put("Message_advice_WiFi_01", "Please enter the Wi-Fi Password.");                   
                WordMap.put("Message_advice_WiFi_02", "Please Connect the Wi-Fi.");    
                WordMap.put("Ethernet", "Ethernet");
                WordMap.put("Brightness", "Brightness");
                WordMap.put("VM_Migrate", "Migrating");
                WordMap.put("VM_Preparing_Boot", "Preparing Boot");      
                WordMap.put("Message_Error_VD_428", "VD is preparing boot!");
                WordMap.put("Message_Error_VD_429", "VD is migrating!");    
                WordMap.put("Snapshot_Rollback", "Rollback"); // 2018.07.06 快照新增還原按鈕
                WordMap.put("Snapshot_RollBack_Confirm", "Are you sure rollback designated snapshot layer？"); // 2018.07.12 還原按鈕和修改dialog
                WordMap.put("Message_Error_MonitorSpice", "An exception occurred while logging in. Please click the \"Reboot\" button to reboot!");                
            // 2018.11.11 新增AcroDesk & RDP Protocol 
                WordMap.put("Str_Status", "VD Status");
                WordMap.put("Str_Protocol", "Protocol Status");
                WordMap.put("Str_Protocol_2", "Protocol");
                WordMap.put("Str_Shutdown", "Shutdown");
                WordMap.put("Str_Preparing", "Preparing");
                WordMap.put("Str_Ready", "Ready");
                WordMap.put("Str_CannotConnect", "Can't Connect");       
                WordMap.put("Thread_Booting", "Booting");
                WordMap.put("Thread_Connecting", "Connecting");  
                WordMap.put("Message_RdpError_ConnectTimeout","RDP connection timeout");
                WordMap.put("Message_RdpSuggest_Relogin", "Please log in again");    
                // 2018.12.04 新增VD&RDP狀態判斷
                WordMap.put("VM_Booting", "Booting");                 
                WordMap.put("Message_RdpError_ConnectFail", "The VD is in the state of 『Shutting down』 or 『Reboot』 or 『Disconnected』.");
                WordMap.put("Message_RdpSuggest_ReConnect", "Please reconnect after one minute.");    
                // 2018.12.12 新增 431NoVGA 和第一次開機
                WordMap.put("Message_Error_VD_431", "Connection failed. There is no VGA card available for the VD！"); 
                WordMap.put("Message_First_Booting1", "VD is creating 『User Profile』.");
                WordMap.put("Message_First_Booting_Suggest1", "Please wait 1~2 minutes");                
                WordMap.put("Message_RDP_Stage_0", "Preparing Boot");                 
                WordMap.put("Message_RDP_Stage_1", "Read user data");                 
                WordMap.put("Message_RDP_Stage_1_Error", "Read user data connection timeout!");                 
                WordMap.put("Message_RDP_Stage_1_Suggest", "Please reconnect after one minute.");                 
                WordMap.put("Message_RDP_Stage_2", "Check user HDDs");                 
                WordMap.put("Message_RDP_Stage_2_Error", "Check user HDDs connection timeout!");                 
                WordMap.put("Message_RDP_Stage_2_Suggest", "Please reconnect after one minute.");  
                WordMap.put("Message_RDP_Stage_3", "Format user HDDs");                 
                WordMap.put("Message_RDP_Stage_3_Error", "Format user HDDs connection timeout!");                 
                WordMap.put("Message_RDP_Stage_3_Suggest", "Please reconnect after one minute.");  
                WordMap.put("Message_RDP_Stage_4", "Restore user data");                 
                WordMap.put("Message_RDP_Stage_4_Error", "Restore user data connection timeout!");                 
                WordMap.put("Message_RDP_Stage_4_Suggest", "Please reconnect after one minute.");  	
                // 2018.18.18 new create & reborn 
                WordMap.put("Message_NewCreate", "Create new VD(it takes about 1~2 minutes), please wait!");  
                WordMap.put("Message_Reborn", "Reborn VD(it takes about 1 minute), please wait!");  
                WordMap.put("Message_ReadingProfile", "Data reading");  
                WordMap.put("Message_RDP_Stage_3_Profile", "Create account and format user HDDs");  
                WordMap.put("Message_RDP_Stage_3_NoProfile", "Create an account");  
                WordMap.put("Message_RDP_Stage_4_Profile", "Create account and restore user profile");  
                WordMap.put("Message_RDP_Stage_4_NoProfile", "Create an account");     
                WordMap.put("Message_IsAssignedUserDisk_NULL", "Can't Connect");
                WordMap.put("Message_RDP_Booting", "The VD boot(it takes about 1 minute), please wait!");
                WordMap.put("Message_RDP_Preparing", "Preparing");
                WordMap.put("Message_No_UserAccount", "No User Account！");
                // 2018.12.27  其他中斷連線狀態 - Crash & Migrate
                WordMap.put("Message_Error_VMCrash", "VD has crashed！");
                WordMap.put("Message_Error_VMAnomaly", "VD Anomaly");    
                // 2018.12.28 block 3D vd
                WordMap.put("Message_Error_VD_loginFail", "Login failed！");
                WordMap.put("Message_Suggest_Error_451", "Please use the AcroRDP protocol to re-login");    
                // 2019.01.02 view D Drive   tooltip_ViewDisk
                WordMap.put("ViewUserDisk", "View user disk");          
                WordMap.put("Desc_ViewUserDisk_operation1", "1. Please shutdown the VD first, then select \"View\", \"Rollback\" or \"Stop View\" button.");
                WordMap.put("Desc_ViewUserDisk_operation2", "2. Click 『Stop』 button to stop the viewing.");  
                WordMap.put("Message_Error_VD_411", "Login failed,  user disk is in Task！");   
                WordMap.put("TC_Ver_Updating", "Updating, Please wait");
                // 2019.03.28 VM/VD migration reconnect
                WordMap.put("Str_Dynamic_Transfer", "Dynamic Transfer");                                
                WordMap.put("Str_Dynamic_Transfer_Reconnect", "Reconnecting");   
                // 2019.04.16 Ping IP方式修改                
                WordMap.put("Message_SpiceError_ConnectTimeout", "AcroSpice connection timeout！");                     
                WordMap.put("Desc_Snapshot_operation3", "");                    
                WordMap.put("Desc_ViewUserDisk_operation3", "");      
                WordMap.put("Desc_ViewUserDisk_operation1-1", "");  
                WordMap.put("KeyboardSetting_tab", "");
                WordMap.put("KeyboardSetting_Title", "");
                WordMap.put("KeyboardType_label", "");                    
        }
    }
    // Input：,  Output： , 功能： 瘦客戶機關機
    public void ShutdownProcess(){
        
        if(lock_Shutdown == false){
            lock_Shutdown=true;
            
            final Alert alert = new Alert(AlertType.CONFIRMATION); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
            alert.setTitle("Exit Confirmation"); //設定對話框視窗的標題列文字
            alert.setHeaderText(""); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
            alert.setContentText(WordMap.get("ExitMassage")); //設定對話框的訊息文字
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType(WordMap.get("Alert_Cancel"),ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeOK,buttonTypeCancel); 
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
            alert.initOwner(public_stage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
            
            final Optional<ButtonType> opt = alert.showAndWait();
            final ButtonType rtn = opt.get(); //可以直接用「alert.getResult()」來取代           
            if (rtn == buttonTypeOK) {   //若使用者按下「確定」
                WriteLastStatus();  //紀錄設定的程式語言 
                WriteLastChange();
                try {
                    Shutdown_params = new String();            
                    Shutdown_params="poweroff"; 
                    Shutdown_process =Runtime.getRuntime().exec(Shutdown_params);
                    //System.out.printf("Shutdown_process : "+ Shutdown_process +"\n");                    

                } catch (IOException ex) {
                    Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                }

            } 
             else{ 
                System.out.print("button cancel action\n");
                lock_Shutdown=false;
            }
            
        }    
     
    }   
    // Input：,  Output： , 功能： 瘦客戶機重新開機
    public void RebootProcess(){
        
        if(lock_Reboot == false){
            lock_Reboot=true;
            
            final Alert alert = new Alert(AlertType.CONFIRMATION); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
            alert.setTitle("Exit Confirmation"); //設定對話框視窗的標題列文字
            alert.setHeaderText(""); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
            alert.setContentText(WordMap.get("RebootMassage")); //設定對話框的訊息文字
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType(WordMap.get("Alert_Cancel"),ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeOK,buttonTypeCancel); 
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
            alert.initOwner(public_stage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係

            final Optional<ButtonType> opt = alert.showAndWait();
            final ButtonType rtn = opt.get(); //可以直接用「alert.getResult()」來取代
            if (rtn == buttonTypeOK) {   //若使用者按下「確定」
                WriteLastStatus();  //紀錄設定的程式語言 
                WriteLastChange();
                
                try {
                    Reboot_params = new String();            
                    Reboot_params="reboot"; 
                    Reboot_process =Runtime.getRuntime().exec(Reboot_params);
                    //System.out.printf("Reboot_process : "+ Reboot_process +"\n");
                    lock_Reboot=false;

                } catch (IOException ex) {
                    Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                }

            } 
             else{ 
                System.out.print("button cancel action\n");
                lock_Reboot=false;                
            }
            
        }
     
    }      
    // Input：WindowEvent事件,  Output： , 功能： 按視窗X 可關閉AcroViewer應用程式
    public void ExitWindowProcess(WindowEvent we){
        final Alert alert = new Alert(AlertType.CONFIRMATION); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
        alert.setTitle("Exit Confirmation"); //設定對話框視窗的標題列文字
        //Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        //stage.getIcons().add(new Image(this.getClass().getResource("images/Icon2.png").toString()));       
        alert.setHeaderText(""); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        alert.setContentText(WordMap.get("ExitMassage")); //設定對話框的訊息文字
        ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType(WordMap.get("Alert_Cancel"),ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOK,buttonTypeCancel); 
        
        final Optional<ButtonType> opt = alert.showAndWait();
        final ButtonType rtn = opt.get(); //可以直接用「alert.getResult()」來取代
        //System.out.println(rtn+"\n");            
        
        if (rtn == buttonTypeOK) {   //若使用者按下「確定」
            WriteLastStatus();  //紀錄設定的程式語言  
            
            /*
            if(QM.process!=null){             
               for(Process p : processes) {
                    p.destroy();
                 }
            }
            */
            
            Platform.exit();
            System.out.print(" Platform.exit()\n");
            
            public_stage.close();
            System.out.print("public_stage.close()\n");
            /*
            System.exit(0); // 結束程式     0         
            System.out.print("System.exit(0)n\n");
            */
            System.exit(1);      
            System.out.print("System.exit(1)\n");
            
        }         
        else{
            we.consume();
            //System.out.print("button cancel action\n");
        }

    }
    // Input：IP標籤, 使用者標籤, 密碼標籤, 語言標籤, ；標籤, ；標籤, ；標籤 ,  Output： , 功能： 變更Label字型,字型:中文-Droid Sans Fallback,英文:Ubuntu
    public void ChangeLabelLang(Label lab01, Label lab02, Label lab03, Label lab04, Label col01, Label col02, Label col03){
        //Label "IP", "UserName", "Password", "Language"
         if("English".equals(LangNow)){
            lab01.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 20px; -fx-font-weight: 900;");//21
            lab02.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 20px; -fx-font-weight: 900;"); //Liberation Serif
            lab03.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 20px; -fx-font-weight: 900;");
            lab04.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 19px; -fx-font-weight: 900;");//17

            //win7以上僅能用英文名稱
            //冒號":"-win7
            col01.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 20px; -fx-font-weight: 900;");
            col02.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 20px; -fx-font-weight: 900;");
            col03.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 20px; -fx-font-weight: 900;");
            
        }        
         else {//if(("繁體中文".equals(LangNow))||("TraditionalChinese".equals(LangNow))||("简体中文".equals(LangNow))||("SimpleChinese".equals(LangNow))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/
        
            //win7以上僅能用英文名稱
            lab01.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-size: 20px; -fx-font-weight: 900; ");//20  -fx-font-style:Bold;
            lab02.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-size: 20px; -fx-font-weight: 900;");
            lab03.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-size: 20px; -fx-font-weight: 900;");
            lab04.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-size: 18px; -fx-font-weight: 900;");
            //冒號":"
            col01.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 20px; -fx-font-weight: 900;");
            col02.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 20px; -fx-font-weight: 900;");
            col03.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 20px; -fx-font-weight: 900;");
            
        }
       
     }
    // Input：主頁面的所有button ,  Output： , 功能： 變更Label字型,字型:中文-Droid Sans Fallback,英文:Ubuntu
    public void ChangeButtonLang(Button but01, Button but02, Button but03, CheckBox box, Button but05, Button but06){
        //Button "Login", "ManagerVD", "ChangePassword", "Leave"
        if("English".equals(LangNow)){
            but01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");
            but02.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");
            but03.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");
            //but04.setStyle("-fx-font-family:'Liberation Serif';-fx-font-weight: 900;");
            box.setStyle  ("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");
            but05.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");
            but06.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");
        }        
        else {//if(("繁體中文".equals(LangNow))||("TraditionalChinese".equals(LangNow))||("简体中文".equals(LangNow))||("SimpleChinese".equals(LangNow))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/
         
            //win7以上僅能用英文名稱
            but01.setStyle("-fx-font-family:'Droid Sans Fallback'; ");
            but02.setStyle("-fx-font-family:'Droid Sans Fallback';");
            but03.setStyle("-fx-font-family:'Droid Sans Fallback';");
            //but04.setStyle("-fx-font-family:'Droid Sans Fallback';");   
            if(("日本語".equals(LangNow)))
                box.setStyle  ("-fx-font-family:'Droid Sans Fallback';-fx-font-size: 14px;");
            else
                box.setStyle  ("-fx-font-family:'Droid Sans Fallback';");                           
            but05.setStyle("-fx-font-family:'Droid Sans Fallback';");
            but06.setStyle("-fx-font-family:'Droid Sans Fallback';");
            
        }
        
    }    
    // Input： ,  Output： , 功能： Login 錯誤訊息 (-----目前無使用-----)
    public void LoginResultAlert(){        
        if("English".equals(WordMap.get("SelectedLanguage"))){
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("Message_Login"));
            alert_error.setHeaderText(null);
            //For Win7 - Dialog 
            alert_error.getDialogPane().setMinSize(450,Region.USE_PREF_SIZE);
            alert_error.getDialogPane().setMaxSize(450,Region.USE_PREF_SIZE);

            alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_IP_01")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_07")
                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK); 
            
            /*** alert style ***/
            DialogPane dialogPane = alert_error.getDialogPane();
            dialogPane.getStylesheets().add("myDialogs.css");
            dialogPane.getStyleClass().add("myDialog");

            alert_error.initModality(Modality.APPLICATION_MODAL);
            alert_error.initOwner(public_stage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係

            alert_error.showAndWait();
            //alert_error.show();            
        }     
        else {//if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){       
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("Message_Login"));
            alert_error.setHeaderText(null);
            alert_error.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_IP_01")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_07")
                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK); 
    
            /*** alert style ***/
            DialogPane dialogPane = alert_error.getDialogPane();
            dialogPane.getStylesheets().add("myDialogs.css");
            dialogPane.getStyleClass().add("myDialog");

            alert_error.initModality(Modality.APPLICATION_MODAL);
            alert_error.initOwner(public_stage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
            
            alert_error.showAndWait();
            //alert_error.show(); 
        }        
        
    }    
    // Input： ,  Output： , 功能： 雲桌面管理登入錯誤訊息 (-----目前無使用-----)
    public void VDMLResultAlert(){        
        if("English".equals(WordMap.get("SelectedLanguage"))){
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("ManageVD_Title"));
            alert_error.setHeaderText(null);
            alert_error.getDialogPane().setMinSize(550,Region.USE_PREF_SIZE);
            alert_error.getDialogPane().setMaxSize(550,Region.USE_PREF_SIZE);
            alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VM_IP_01")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_07")
                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK); 
            
            /*** alert style ***/
            DialogPane dialogPane = alert_error.getDialogPane();
            dialogPane.getStylesheets().add("myDialogs.css");
            dialogPane.getStyleClass().add("myDialog");

            alert_error.initModality(Modality.APPLICATION_MODAL);
            alert_error.initOwner(public_stage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
            
            alert_error.showAndWait();
            //alert_error.show();
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){       
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("ManageVD_Title"));
            alert_error.setHeaderText(null);
            alert_error.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VM_IP_01")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_07")
                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK); 
            
            /*** alert style ***/
            DialogPane dialogPane = alert_error.getDialogPane();
            dialogPane.getStylesheets().add("myDialogs.css");
            dialogPane.getStyleClass().add("myDialog");

            alert_error.initModality(Modality.APPLICATION_MODAL);
            alert_error.initOwner(public_stage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係

            alert_error.showAndWait();
            //alert_error.show();

        }            
    }    
    // Input： ,  Output： , 功能： 修改密碼登入錯誤訊息 (-----目前無使用-----)
    public void CPLResultAlert(){        
        if("English".equals(WordMap.get("SelectedLanguage"))){
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("CP_Window_Title"));
            alert_error.setHeaderText(null);
            alert_error.getDialogPane().setMinSize(550,Region.USE_PREF_SIZE);
            alert_error.getDialogPane().setMaxSize(550,Region.USE_PREF_SIZE);
            alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_CP_IP_01")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_07")
                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK);
            
            /*** alert style ***/
            DialogPane dialogPane = alert_error.getDialogPane();
            dialogPane.getStylesheets().add("myDialogs.css");
            dialogPane.getStyleClass().add("myDialog");

            alert_error.initModality(Modality.APPLICATION_MODAL);
            alert_error.initOwner(public_stage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係

            alert_error.showAndWait();
            //alert_error.show();
        }        
        else {//if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){       
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("CP_Window_Title"));
            alert_error.setHeaderText(null);
            alert_error.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_CP_IP_01")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_07")
                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK); 
            
            /*** alert style ***/
            DialogPane dialogPane = alert_error.getDialogPane();
            dialogPane.getStylesheets().add("myDialogs.css");
            dialogPane.getStyleClass().add("myDialog");

            alert_error.initModality(Modality.APPLICATION_MODAL);
            alert_error.initOwner(public_stage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係

            alert_error.showAndWait();
            //alert_error.show();
        }            
    }    
   // Input： ,  Output： , 功能： VD已經有人連線中，他人在登入時，會跳出視窗詢問是否還要連線(j無作用)
    public void IsVdOnlineAlert() throws IOException, MalformedURLException, InterruptedException{        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("VD Online");//設定對話框視窗的標題列文字  , "您真的要結束程式嗎？", ButtonType.YES, ButtonType.NO
        //alert.setHeaderText(WordMap.get("Message")+" : "+" VD is Online!! ");//設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        alert.setHeaderText(null);
        alert.setX(screenCenterX);
        alert.setY(screenCenterY);
        alert.getDialogPane().setMinSize(500,Region.USE_PREF_SIZE);
        alert.getDialogPane().setMaxSize(500,Region.USE_PREF_SIZE);
        alert.setContentText(WordMap.get("Message_VD_Online"));//VD 已上線,是否還要連線,若連線,則前一個連線會自動斷線.
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
        alert.initOwner(public_stage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
        
        Bounds mainBounds = public_stage.getScene().getRoot().getLayoutBounds();       
        alert.setX(public_stage.getX() + (mainBounds.getWidth() - 500 ) / 2);  //5.7
        alert.setY(public_stage.getY() + (mainBounds.getHeight() - 200 ) / 2);  //5
        
        Optional<ButtonType> result01 = alert.showAndWait();
            if (result01.get() == buttonTypeOK){                    
                //QM.DoChcekServerAddressAvailabilityGet(QM.Address,QM.Port);

                    switch (GB.migrationProtocol) {
                        case "AcroSpice": // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP
                            new Thread(new Runnable() {                
                                @Override
                                public void run() {
                                    try {
                                        QM.SetVDOnline(GB.migrationApiIP, GB.migrationVDID, GB.migrationUserName, GB.migrationPwd, GB.migrationUkey, GB.migrationApiPort); // VdId
                                    } catch (IOException ex) {
                                        Logger.getLogger(LoginMultiVD.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(LoginMultiVD.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }).start();                            
                            break;
                        case "AcroRDP": // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP
                            new Thread(new Runnable() {                
                                @Override
                                public void run() {
                                    try {
                                        QM.SetVDRDPOnline(GB.migrationApiIP, GB.migrationVDID, GB.migrationUserName, GB.migrationPwd, GB.migrationUkey, GB.migrationApiPort, GB.migrationIsVdOrg, GB.EnableUSB, GB.adAuth, GB.adDomain); // 2018.12.12 新增 431NoVGA 和第一次開機
                                    } catch (IOException ex) {
                                        Logger.getLogger(LoginMultiVD.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(LoginMultiVD.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }).start();                                                          
                            break;       
                        default: 
                            break;                              
                    }                  
                //QM.SetVDOnline(QM.Address,  QM.VdId, QM.CurrentUserName, QM.CurrentPasseard, QM.uniqueKey,QM.CurrentIP_Port);
                // ... user chose OK                         
//                File f = new File("jsonfile/SpiceText.txt");       
//                if(f.exists()){         
//                    //String[] params = new String [2]; 
//                    QM.params = new String [2];      
//                    QM.params[0] = "/root/0509_AcroRed_Viewer/virt/bin/remote-viewer";// /root/Linux_0220_AcroRed_VDI_Viewer/virt/bin/remote-viewer //"/root/0427_AcroRed_Viewer/virt/bin/remote-viewer"
//                    QM.params[1] = f.toString();                          
//                    QM.process =Runtime.getRuntime().exec(QM.params);        
//                    QM.process=null;
//                    System.gc();//清除系統垃圾
//
//                }else{
//                        System.out.print("remote-viewer.exe is not existed.\n");
//                } 

            } else {
                    // ... user chose CANCEL or closed the dialog
                    alert.close();
            }
     
    }    
    // Input： ,  Output： , 功能： VD在登入時，伺服器無法連線 錯誤訊息
    public void SetVDOlineAlert(){        
        
        Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
        alert_error.setTitle(WordMap.get("Message_Login"));
        alert_error.setHeaderText(null);
        alert_error.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_setVDonline")+"\n"                                            
            +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
            );
        ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
        alert_error.getButtonTypes().setAll(buttonTypeOK); 
        alert_error.showAndWait();
        //alert_error.show();       
    }        
    // Input： ,  Output： , 功能： 鍵盤點擊之動作
    public void keydownup(){ // 2017.06.08 william choose wrong icon    
        
        // 2017.12.19 william SnapShot實作
        LanguageSelection.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){
               IP_Addr.requestFocus();
               pwIP.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_DOWN){
               IP_Addr.requestFocus();
               pwIP.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.DOWN){
               IP_Addr.requestFocus();
               pwIP.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.TAB){
               IP_Addr.requestFocus();
               pwIP.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_UP){ // 2017.06.22 william hide web icon and button event
               SS.requestFocus(); 
               SS.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.UP){ // 2017.06.22 william hide web icon and button event
               SS.requestFocus(); 
               SS.setId("button");
               event.consume();
           }
        });
        //Enter Key & PAGE_DOWN Key - down ; PAGE_UP Key - up
        IP_Addr.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){
                // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//               userTextField.requestFocus();
//               pwName.requestFocus();
                IP_Port.requestFocus();
                pwIP_Port.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_DOWN){
               // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//               userTextField.requestFocus();
//               pwName.requestFocus();
               IP_Port.requestFocus();
               pwIP_Port.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.DOWN){
               // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//               userTextField.requestFocus();
//               pwName.requestFocus();
               IP_Port.requestFocus();
               pwIP_Port.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.TAB){
               // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//               userTextField.requestFocus();
//               pwName.requestFocus();
               IP_Port.requestFocus();
               pwIP_Port.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_UP){
               LanguageSelection.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.UP){
               LanguageSelection.requestFocus();
               event.consume();
           }
        });
          //Enter Key & PAGE_DOWN Key - down ; PAGE_UP Key - up
        pwIP.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){
               // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//               userTextField.requestFocus();
//               pwName.requestFocus();
               IP_Port.requestFocus();
               pwIP_Port.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_DOWN){
               // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//               userTextField.requestFocus();
//               pwName.requestFocus();
               IP_Port.requestFocus();
               pwIP_Port.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.DOWN){
               // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//               userTextField.requestFocus();
//               pwName.requestFocus();
               IP_Port.requestFocus();
               pwIP_Port.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.TAB){
               // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//               userTextField.requestFocus();
//               pwName.requestFocus();
               IP_Port.requestFocus();
               pwIP_Port.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_UP){
               LanguageSelection.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.UP){
               LanguageSelection.requestFocus();
               event.consume();
           }
        });
       // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
        IP_Port.setOnKeyPressed((event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){
                userTextField.requestFocus();
                pwName.requestFocus();
                // 2019.01.02 Remember10 username
                UsernameComboBox.requestFocus();                
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                userTextField.requestFocus();
                pwName.requestFocus();
                // 2019.01.02 Remember10 username
                UsernameComboBox.requestFocus();                    
                event.consume();// Consume Event
            }
           if(event.getCode() == KeyCode.DOWN){
               userTextField.requestFocus();
               pwName.requestFocus();
                // 2019.01.02 Remember10 username
                UsernameComboBox.requestFocus();                   
               event.consume();
           }            
            if(event.getCode() == KeyCode.TAB){
                userTextField.requestFocus();
                pwName.requestFocus();
                // 2019.01.02 Remember10 username
                UsernameComboBox.requestFocus();                    
                event.consume();// Consume Event
            }  
            if(event.getCode() == KeyCode.PAGE_UP){
                IP_Addr.requestFocus();
                pwIP.requestFocus();
                event.consume();// Consume Event
            }            
           if(event.getCode() == KeyCode.UP){
                IP_Addr.requestFocus();
                pwIP.requestFocus();
               event.consume();
           }            
        });   
       // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
        pwIP_Port.setOnKeyPressed((event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){
                userTextField.requestFocus();
                pwName.requestFocus();
                // 2019.01.02 Remember10 username
                UsernameComboBox.requestFocus();                    
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                userTextField.requestFocus();
                pwName.requestFocus();
                // 2019.01.02 Remember10 username
                UsernameComboBox.requestFocus();                    
                event.consume();// Consume Event
            }
           if(event.getCode() == KeyCode.DOWN){
               userTextField.requestFocus();
               pwName.requestFocus();
                // 2019.01.02 Remember10 username
                UsernameComboBox.requestFocus();                   
               event.consume();
           }            
            if(event.getCode() == KeyCode.TAB){
                userTextField.requestFocus();
                pwName.requestFocus();
                // 2019.01.02 Remember10 username
                UsernameComboBox.requestFocus();                    
                event.consume();// Consume Event
            }  
            if(event.getCode() == KeyCode.PAGE_UP){
                IP_Addr.requestFocus();
                pwIP.requestFocus();
                event.consume();// Consume Event
            }            
           if(event.getCode() == KeyCode.UP){
                IP_Addr.requestFocus();
                pwIP.requestFocus();
               event.consume();
           }            
        });        
        
        //Enter Key & PAGE_DOWN Key - down ; PAGE_UP Key - up 
        userTextField.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){
               pwBox.requestFocus();
               textField01.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_DOWN){
               pwBox.requestFocus();
               textField01.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.DOWN){
               pwBox.requestFocus();
               textField01.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.TAB){
               pwBox.requestFocus();
               textField01.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_UP){
               // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//               IP_Addr.requestFocus();
//               pwIP.requestFocus();
               IP_Port.requestFocus();
               pwIP_Port.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.UP){
               // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//               IP_Addr.requestFocus();
//               pwIP.requestFocus();
               IP_Port.requestFocus();
               pwIP_Port.requestFocus();
               event.consume();
           }
        });
        
        
        // 2019.01.02 Remember10 username

        UsernameComboBox.addEventFilter(KeyEvent.KEY_PRESSED, (event)-> {
           //event.get                      
           if(event.getCode() == KeyCode.ENTER){
               pwBox.requestFocus();
               textField01.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_DOWN){
               pwBox.requestFocus();
               textField01.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.DOWN){
               pwBox.requestFocus();
               textField01.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.TAB){
               pwBox.requestFocus();
               textField01.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_UP){
               // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//               IP_Addr.requestFocus();
//               pwIP.requestFocus();
               IP_Port.requestFocus();
               pwIP_Port.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.UP){
               // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//               IP_Addr.requestFocus();
//               pwIP.requestFocus();
               IP_Port.requestFocus();
               pwIP_Port.requestFocus();
               event.consume();
           }
        });          
        
        //Enter Key & PAGE_DOWN Key - down ; PAGE_UP Key - up 
        pwName.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){
               pwBox.requestFocus();
               textField01.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_DOWN){
               pwBox.requestFocus();
               textField01.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.DOWN){
               pwBox.requestFocus();
               textField01.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.TAB){
               pwBox.requestFocus();
               textField01.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_UP){
               // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//               IP_Addr.requestFocus();
//               pwIP.requestFocus();
               IP_Port.requestFocus();
               pwIP_Port.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.UP){
               // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//               IP_Addr.requestFocus();
//               pwIP.requestFocus();
               IP_Port.requestFocus();
               pwIP_Port.requestFocus();
               event.consume();
           }
        });
        //Enter Key & PAGE_DOWN Key - down ; PAGE_UP Key - up 
        pwBox.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){
               Login.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_DOWN){
               Login.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.DOWN){
               Login.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.TAB){
               Login.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_UP){
               userTextField.requestFocus();
               pwName.requestFocus();
                // 2018.12.25 Remember10 username
                UsernameComboBox.requestFocus();                  
               event.consume();
           }
           if(event.getCode() == KeyCode.UP){
               userTextField.requestFocus();
               pwName.requestFocus();
                // 2018.12.25 Remember10 username
                UsernameComboBox.requestFocus();                  
               event.consume();
           }
        });
        textField01.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){
               Login.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_DOWN){
               Login.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.DOWN){
               Login.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.TAB){
               Login.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_UP){
               userTextField.requestFocus();
               pwName.requestFocus();
                // 2018.12.25 Remember10 username
                UsernameComboBox.requestFocus();                  
               event.consume();
           }
           if(event.getCode() == KeyCode.UP){
               userTextField.requestFocus();
               pwName.requestFocus();
                // 2018.12.25 Remember10 username
                UsernameComboBox.requestFocus();                  
               event.consume();
           }
        });       
        //Enter Key-進入功能 ; PAGE_DOWN Key - down ; PAGE_UP Key - up   checkBox_Pw
        Login.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){
               Login.fire();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_DOWN){
               checkBox_Pw.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.DOWN){
               checkBox_Pw.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.TAB){
               checkBox_Pw.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_UP){
               pwBox.requestFocus();
               textField01.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.UP){
               pwBox.requestFocus();
               textField01.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.LEFT){ // 2017.06.22 william hide web icon and button event
               ChangePW.requestFocus(); 
               ChangePW.setId("button");
               event.consume();
           }           
        });
        checkBox_Pw.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){
               checkBox_Pw.fire();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_DOWN){
               ThinClient.requestFocus();
               ThinClient.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.DOWN){
               ThinClient.requestFocus();
               ThinClient.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.TAB){
               ThinClient.requestFocus();
               ThinClient.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_UP){
               Login.requestFocus();              
               event.consume();
           }
           if(event.getCode() == KeyCode.UP){
               Login.requestFocus();              
               event.consume();
           }
        });
      
        //Enter Key-進入功能 ; PAGE_DOWN Key - down ; PAGE_UP Key - up 
        ThinClient.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){
               ThinClient.fire();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_DOWN){
               ManageVD.requestFocus();
               ManageVD.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.DOWN){
               ManageVD.requestFocus();
               ManageVD.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.TAB){
               ManageVD.requestFocus();
               ManageVD.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.RIGHT){
               ManageVD.requestFocus();
               ManageVD.setId("button");
               event.consume();
           }           
           if(event.getCode() == KeyCode.PAGE_UP){
               checkBox_Pw.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.UP){
               checkBox_Pw.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.LEFT){ // 2017.06.08 william add choose TimeCalendar icon
               TimeCalendar.requestFocus();
               TimeCalendar.setId("Time");
               event.consume();
           }
        }); 
        //Enter Key-進入功能 ; PAGE_DOWN Key - down ; PAGE_UP Key - up 
        ManageVD.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){
               ManageVD.fire();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_DOWN){
               ChangePW.requestFocus();
               ChangePW.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.DOWN){
               ChangePW.requestFocus();
               ChangePW.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.TAB){
               ChangePW.requestFocus();
               ChangePW.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.RIGHT){
               ChangePW.requestFocus();
               ChangePW.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_UP){
               ThinClient.requestFocus();
               ThinClient.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.UP){
               ThinClient.requestFocus();
               ThinClient.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.LEFT){
               ThinClient.requestFocus();
               ThinClient.setId("button");
               event.consume();
           }           
        });        
        //Enter Key-進入功能 ; PAGE_DOWN Key - down ; PAGE_UP Key - up 
        // 2017.12.19 william SnapShot實作
        ChangePW.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){
               ChangePW.fire();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_DOWN){ // 2017.06.22 william hide web icon and button event
               SS.requestFocus();
               SS.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.DOWN){ // 2017.06.22 william hide web icon and button event
               SS.requestFocus();
               SS.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.TAB){ // 2017.06.22 william hide web icon and button event
               SS.requestFocus();
               SS.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.RIGHT){ // 2017.06.22 william hide web icon and button event
               SS.requestFocus();
               // Web.setId("button");
               event.consume();
           }           
           if(event.getCode() == KeyCode.PAGE_UP){
               ManageVD.requestFocus();
               ManageVD.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.UP){
               ManageVD.requestFocus();
               ManageVD.setId("button");
               event.consume();
           }
            if(event.getCode() == KeyCode.LEFT){
               ManageVD.requestFocus();
               ManageVD.setId("button");
               event.consume();
           }           
        });        
        // 2017.12.19 william SnapShot實作
        SS.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){
               SS.fire();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_DOWN){
               LanguageSelection.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.DOWN){
               LanguageSelection.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.TAB){
               LanguageSelection.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_UP){
               ChangePW.requestFocus();
               ChangePW.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.UP){
               ChangePW.requestFocus();
               ChangePW.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.LEFT){
               ChangePW.requestFocus();
               ChangePW.setId("button");
               event.consume();
           }     
           
           if(battery_enable) {
               if(event.getCode() == KeyCode.RIGHT){ 
                   Brightness.requestFocus();
                   event.consume();
               }             
           } else {
               if(event.getCode() == KeyCode.RIGHT){ 
                   Connect_ON_OFF.requestFocus();
                   event.consume();
               }             
           }
           
          
           
        });
                
        
        
        Connect_ON_OFF.setOnKeyPressed((event)-> {
           //event.get
           if(wifi_enable) {
               if(event.getCode() == KeyCode.ENTER){
                   Connect_ON_OFF.fire();
                   event.consume();
               }           
           }
           
           if(event.getCode() == KeyCode.RIGHT){
               Reboot.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.DOWN){
               Reboot.requestFocus();
               event.consume();
           }
           
           if(battery_enable) {
               if(event.getCode() == KeyCode.LEFT){ 
                   Battery.requestFocus();
                   Battery.setId("button");
                   event.consume();
               }
               if(event.getCode() == KeyCode.UP){ 
                   Battery.requestFocus();
                   Battery.setId("button");
                   event.consume();
               }             
           
           } else {
               if(event.getCode() == KeyCode.LEFT){ 
                   SS.requestFocus();
                   SS.setId("button");
                   event.consume();
               }
               if(event.getCode() == KeyCode.UP){ 
                   SS.requestFocus();
                   SS.setId("button");
                   event.consume();
               }            
           }                                
        });           
        
        
        if(battery_enable) {
            Battery.setOnKeyPressed((event)-> {
               //event.get
               if(event.getCode() == KeyCode.ENTER){
                   Battery.fire();
                   event.consume();
               }
               if(event.getCode() == KeyCode.RIGHT){
                   Connect_ON_OFF.requestFocus();
                   event.consume();
               }
               if(event.getCode() == KeyCode.DOWN){
                   Connect_ON_OFF.requestFocus();
                   event.consume();
               }
               if(event.getCode() == KeyCode.LEFT){ 
                   Brightness.requestFocus();
                   Brightness.setId("button");
                   event.consume();
               }
               if(event.getCode() == KeyCode.UP){ 
                   Brightness.requestFocus();
                   Brightness.setId("button");
                   event.consume();
               }           
            });           
        
            Brightness.setOnKeyPressed((event)-> {
               //event.get
               if(event.getCode() == KeyCode.ENTER){
                   Brightness.fire();
                   event.consume();
               }
               if(event.getCode() == KeyCode.RIGHT){
                   Battery.requestFocus();
                   event.consume();
               }
               if(event.getCode() == KeyCode.DOWN){
                   Battery.requestFocus();
                   event.consume();
               }
               if(event.getCode() == KeyCode.LEFT){ 
                   SS.requestFocus();
                   SS.setId("button");
                   event.consume();
               }
               if(event.getCode() == KeyCode.UP){ 
                   SS.requestFocus();
                   SS.setId("button");
                   event.consume();
               }           
            });           
            
        }
        
        
        /* 2017.06.22 william hide web icon and button event
        Web.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){
               Web.fire();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_DOWN){
               LanguageSelection.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.DOWN){
               LanguageSelection.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.TAB){
               LanguageSelection.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_UP){
               ChangePW.requestFocus();
               ChangePW.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.UP){
               ChangePW.requestFocus();
               ChangePW.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.LEFT){
               ChangePW.requestFocus();
               ChangePW.setId("button");
               event.consume();
           }           
        });
        */
        // 2017.06.08 william add choose icon function -> Reboot,Shutdown,TimeCalendar 
        // 2017.12.19 william SnapShot實作
        Reboot.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){
               Reboot.fire();
               event.consume();
           }
           if(event.getCode() == KeyCode.RIGHT){
               Shutdown.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.DOWN){
               Shutdown.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.LEFT){ // 2017.06.22 william hide web icon and button event
               Connect_ON_OFF.requestFocus();
               Connect_ON_OFF.setId("button");
               event.consume();
           }
           if(event.getCode() == KeyCode.UP){ // 2017.06.22 william hide web icon and button event
               Connect_ON_OFF.requestFocus();
               Connect_ON_OFF.setId("button");
               event.consume();
           }           
        });        
        
        Shutdown.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){
               Shutdown.fire();
               event.consume();
           }
           if(event.getCode() == KeyCode.RIGHT){
               TimeCalendar.requestFocus();
               TimeCalendar.setId("Time");
               event.consume();
           }
           if(event.getCode() == KeyCode.DOWN){
               TimeCalendar.requestFocus();
               TimeCalendar.setId("Time");
               event.consume();
           }           
           if(event.getCode() == KeyCode.LEFT){
               Reboot.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.UP){
               Reboot.requestFocus();
               event.consume();
           }           
        });
                        
        TimeCalendar.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){
               TimeCalendar.fire();
               event.consume();
           }
           if(event.getCode() == KeyCode.RIGHT){
               ThinClient.requestFocus();
               ThinClient.setId("button");
               event.consume();
           } 
           if(event.getCode() == KeyCode.DOWN){
               ThinClient.requestFocus();
               ThinClient.setId("button");
               event.consume();
           }             
           if(event.getCode() == KeyCode.LEFT){
               Shutdown.requestFocus();
               event.consume();
           }   
           if(event.getCode() == KeyCode.UP){
               Shutdown.requestFocus();
               event.consume();
           }            
        });        
                
        /*
        //Enter Key-進入功能 ; PAGE_DOWN Key - down ; PAGE_UP Key - up
        Leave.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){
               Leave.fire();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_DOWN){
               LanguageSelection.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.TAB){
               LanguageSelection.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_UP){
               ChangePW.requestFocus();
               event.consume();
           }
        });
        */          
    }
    // Input： ,  Output： , 功能： 滑鼠滑過之動作 (-----目前無使用-----)
    public void mousemove(){ 
        
//        IP_Addr.setOnMouseEntered((event)-> {
//            IP_Addr.requestFocus();        
//        });
        
        Login.setOnMouseEntered((event)-> {
            Login.requestFocus();
        
        });
        
//        Web.setOnMouseEntered((event)-> {
//            Web.requestFocus();
//        
//        });
//        
//        ThinClient.setOnMouseEntered((event)-> {
//            ThinClient.requestFocus();
//        
//        });
//        
//        ManageVD.setOnMouseEntered((event)-> {
//            ManageVD.requestFocus();
//        
//        });
//        ChangePW.setOnMouseEntered((event)-> {
//            ChangePW.requestFocus();
//        
//        });
        
    }
    // Input： ,  Output： , 功能： 檢察Java主程序是否運作 (-----目前無使用-----)
    private void CheckViewerAlive() throws IOException { 
        Process text_process =Runtime.getRuntime().exec("ps -x"); //ps -x | grep 'remote-viewer'
        try (BufferedReader text_output = new BufferedReader (new InputStreamReader(text_process.getInputStream()))) {
            String text_line = text_output.readLine();            
            
            while(text_line != null){                
               
                if ( text_line.contains("remote-viewer")){ //.contains("   ")
                    
                    viewer_alive = true;                  
                    break;
                }

                text_line = text_output.readLine();  //.trim()       
            }
            text_output.close();
        }
        //System.out.println("*/*/ viewer_alive */*/-  : " + viewer_alive +"\n");        
    }
    // Input： ,  Output： , 功能： 變更網路狀態Icon   
    public void networkconn_change() throws UnknownHostException, IOException{ 
        
        //System.out.println(" :**: networkconn_change :: \n"); 
        result = NC.NetworkConnect();
        //System.out.println("NC.Connect_IP : " + NC.Connect_IP +"\n");
        Connect_ON_img       = new Image("images/Network.png",32, 32 ,false, false);     
        Connect_OFF_img      = new Image("images/Disconnect_30.png");
        WiFi_Connect_ON_img  = new Image("images/Wifi_Full.png",32, 32 ,false, false);    // 2018.04.13 william wifi實作    
        WiFi_Connect_ON_3_img  = new Image("images/Wifi_Full-1.png",32, 32 ,false, false);    // 2018.04.13 william wifi實作    
        WiFi_Connect_ON_2_img  = new Image("images/Wifi_Full-2.png",32, 32 ,false, false);    // 2018.04.13 william wifi實作    
        WiFi_Connect_ON_1_img  = new Image("images/Wifi_Full-3.png",32, 32 ,false, false);    // 2018.04.13 william wifi實作    
        WiFi_Connect_OFF_img = new Image("images/Wifi_Full_Noconnect.png",32, 32 ,false, false); // 2018.04.13 william wifi實作
        connect_on           = new ImageView(Connect_ON_img);
        connect_off          = new ImageView(Connect_OFF_img);    
        WiFi_connect_on      = new ImageView(WiFi_Connect_ON_img);  // 2018.04.13 william wifi實作
        WiFi_connect_on_3      = new ImageView(WiFi_Connect_ON_3_img);  // 2018.04.13 william wifi實作
        WiFi_connect_on_2      = new ImageView(WiFi_Connect_ON_2_img);  // 2018.04.13 william wifi實作
        WiFi_connect_on_1      = new ImageView(WiFi_Connect_ON_1_img);  // 2018.04.13 william wifi實作
        WiFi_connect_off     = new ImageView(WiFi_Connect_OFF_img); // 2018.04.13 william wifi實作          
        lostwificonnect = 0;// 2018.04.13 william wifi實作          
        /*
        if (result) { // 2017.07.11 william remove internet plug & DNS Exception fix NC.Connect_IP!= null && !NC.Connect_IP.isEmpty()
            network_change = true;
            Connect_ON_OFF.setGraphic( connect_on );
            
            //System.out.println(" :1: Connect_ON_OFF :: " + Connect_ON_OFF.getGraphic() +"\n");     

        } else {
            network_change = false;
            Connect_ON_OFF.setGraphic( connect_off );            
            //System.out.println(" :1: Connect_ON_OFF :: " + Connect_ON_OFF.getGraphic() +"\n");         
        }
        */
        
        switch (result) { // 2018.04.13 william wifi實作  
            case 0:
                network_change = false;
                Connect_ON_OFF.setGraphic( WiFi_connect_off );
                break;
            case 1:
                network_change = true;
                Connect_ON_OFF.setGraphic( WiFi_connect_on );
                break;
            case 2:
                network_change = true;
                Connect_ON_OFF.setGraphic( connect_on );
                break;
            case 3:
                network_change = true;
                Connect_ON_OFF.setGraphic( connect_on );
                break; 
            case 4:
                network_change = true;
                Connect_ON_OFF.setGraphic( WiFi_connect_on_3 );
                break;
            case 5:
                network_change = true;
                Connect_ON_OFF.setGraphic( WiFi_connect_on_2 );
                break;
            case 6:
                network_change = true;
                Connect_ON_OFF.setGraphic( WiFi_connect_on_1 );
                break;                
            default:
                network_change = false;  
                Connect_ON_OFF.setGraphic( WiFi_connect_off );
                break;
        }        
        
        
        //每秒check  網路狀態
        Timer timer_connect=new Timer(true);
        TimerTask task_connect=new TimerTask(){
            @Override
            public void run() { 
                //System.out.print(" -- time -- \n");                
//                Connect_ON_img=new Image("images/Connect_30.png");     
//                Connect_OFF_img=new Image("images/Disconnect_30.png");
                connect_on = null;
                connect_off = null;
                // System.gc();//清除系統垃圾 // 2017.06.05 william System.gc disable
                
                // 2018.04.13 william wifi實作  
                connect_on       = new ImageView(Connect_ON_img);
                connect_off      = new ImageView(Connect_OFF_img);    
                WiFi_connect_on  = new ImageView(WiFi_Connect_ON_img);
                WiFi_connect_on_3      = new ImageView(WiFi_Connect_ON_3_img);  // 2018.04.13 william wifi實作
                WiFi_connect_on_2      = new ImageView(WiFi_Connect_ON_2_img);  // 2018.04.13 william wifi實作
                WiFi_connect_on_1      = new ImageView(WiFi_Connect_ON_1_img);  // 2018.04.13 william wifi實作                
                WiFi_connect_off = new ImageView(WiFi_Connect_OFF_img);                  
//                ImageView connect_on = new ImageView(Connect_ON_img);
//                ImageView connect_off = new ImageView(Connect_OFF_img);
                try {
                    Check_Connecting = NC.Check_Connecting_Status();
                } catch (IOException ex) {
                    Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
//                    /** check Viewer 是否啟動, 若啟動 則 不再更新網路狀態 **/
//                    CheckViewerAlive();                    
//                    if(viewer_alive == false){                      
//                        NC.NetworkConnect();                           
//                    }


                    result = NC.NetworkConnect(); 
                    
                    

                    Platform.runLater(() -> {
                        // 2018.04.13 william wifi實作  
                        /*
                        if (result) { // 2017.07.11 william remove internet plug & DNS Exception fix NC.Connect_IP!= null && !NC.Connect_IP.isEmpty()
//                            IP_Addr.setText("3");
//                            userTextField.setText(NC.line_Connection);
                            network_change = true;
                            Connect_ON_OFF.setGraphic( connect_on );                          
                            //System.out.println(" :00: Connect_ON_OFF :: " + Connect_ON_OFF.getGraphic() +"\n");    
                            
                        } else {
//                            IP_Addr.setText("4");
//                            userTextField.setText(NC.line_Connection);
                            network_change = false;
                            Connect_ON_OFF.setGraphic( connect_off );                       
                            //System.out.println(" :00: Connect_ON_OFF :: " + Connect_ON_OFF.getGraphic() +"\n");         
                        }
                        */

//                    if(Check_Connecting) {
//                        Connect_ON_OFF.setGraphic(pi);                                        
//                    } else {
                        switch (result) { // 2018.04.13 william wifi實作  
                            case 0: // 都沒有連線
                                network_change = false;
                                
                                if(!Check_Connecting)
                                    Connect_ON_OFF.setGraphic( WiFi_connect_off );                                                            
                                else
                                    Connect_ON_OFF.setGraphic(pi);                                        
                                break;
                            case 1: // 只有wifi 滿格
                                network_change = true;
                                
                                if(!Check_Connecting)
                                    Connect_ON_OFF.setGraphic( WiFi_connect_on );
                                else
                                    Connect_ON_OFF.setGraphic(pi);                                                                 
                                break;
                            case 2: // 只有網路     
                                network_change = true;
                                Connect_ON_OFF.setGraphic( connect_on );
                                break;
                            case 3:  // 有網路有wifi
                                network_change = true;
                                Connect_ON_OFF.setGraphic( connect_on );                                
                                break; 
                            case 4: // 只有wifi 3格
                                network_change = true;
                                
                                if(!Check_Connecting)
                                    Connect_ON_OFF.setGraphic( WiFi_connect_on_3 );
                                else
                                    Connect_ON_OFF.setGraphic(pi);                                                                                                                            
                                break;
                            case 5: // 只有wifi 2格
                                network_change = true;
                                
                                if(!Check_Connecting)
                                    Connect_ON_OFF.setGraphic( WiFi_connect_on_2 );
                                else
                                    Connect_ON_OFF.setGraphic(pi);                                
                                break;
                            case 6: // 只有wifi 1格
                                network_change = true;
                                
                                if(!Check_Connecting)
                                    Connect_ON_OFF.setGraphic( WiFi_connect_on_1 );
                                else
                                    Connect_ON_OFF.setGraphic(pi);                                  
                                break;                                
                            default:
                                network_change = false;  
                                
                                if(!Check_Connecting)
                                    Connect_ON_OFF.setGraphic( WiFi_connect_off );
                                else
                                    Connect_ON_OFF.setGraphic(pi);                                 
                                break;
                        }                       
//                    }
                        
                        
                    });                     

                } catch (UnknownHostException ex) {
                    Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                }              
            }
        };
            timer_connect.schedule(task_connect, 0, 3000); 
         
    }    
    // Input： ,  Output： , 功能： 變更電池狀態Icon   
    public void Battery_change() throws UnknownHostException, IOException {                 
        Battery_No_Charging_full_img = new Image("images/BAT.png",32, 32 ,false, false);     
        Battery_No_Charging_3_img    = new Image("images/bat_full-1.png",32, 32 ,false, false);
        Battery_No_Charging_2_img    = new Image("images/bat_full-2.png",32, 32 ,false, false); 
        Battery_No_Charging_1_img    = new Image("images/bat_full-3.png",32, 32 ,false, false); 
        Battery_No_Charging_0_img    = new Image("images/bat_Empty.png",32, 32 ,false, false);
        Battery_Charging_full_img    = new Image("images/BAT_Full_InCharge.png",32, 32 ,false, false);
        Battery_Charging_3_img       = new Image("images/BAT_Full_InCharge-1.png",32, 32 ,false, false);
        Battery_Charging_2_img       = new Image("images/BAT_Full_InCharge-2.png",32, 32 ,false, false); 
        Battery_Charging_1_img       = new Image("images/BAT_Full_InCharge-3.png",32, 32 ,false, false); 
        Battery_Charging_0_img       = new Image("images/BAT_Full_InCharge-Empty.png",32, 32 ,false, false);        
        
//        Battery_No_Charging_full     = new ImageView(Battery_No_Charging_full_img);
//        Battery_No_Charging_3        = new ImageView(Battery_No_Charging_3_img);    
//        Battery_No_Charging_2        = new ImageView(Battery_No_Charging_2_img);  
//        Battery_No_Charging_1        = new ImageView(Battery_No_Charging_1_img);
//        Battery_No_Charging_0        = new ImageView(Battery_No_Charging_0_img);
//        Battery_Charging             = new ImageView(Battery_Charging_full_img);
//        Battery_Charging_3           = new ImageView(Battery_Charging_3_img);    
//        Battery_Charging_2           = new ImageView(Battery_Charging_2_img);  
//        Battery_Charging_1           = new ImageView(Battery_Charging_1_img);
//        Battery_Charging_0           = new ImageView(Battery_Charging_0_img);        
        
        
        pow_supply   = BC.Get_Power_Supply();
        pow_Charging = BC.Get_Power_Status();
        pow_capacity = BC.Get_Power_Capacity();
        
        if(pow_supply == true && pow_Charging == true || (pow_supply == true && pow_Charging == false)) {
            if(pow_capacity >= 80) {
//                Battery.setGraphic(Battery_Charging);
                Battery_ImageView.setImage(Battery_Charging_full_img);
            } else if(pow_capacity < 80 && pow_capacity >= 60) {
//                Battery.setGraphic(Battery_Charging_3);
                Battery_ImageView.setImage(Battery_Charging_3_img);
            } else if(pow_capacity < 60 && pow_capacity >= 40) {
//                Battery.setGraphic(Battery_Charging_2);
                Battery_ImageView.setImage(Battery_Charging_2_img);
            } else if(pow_capacity < 40 && pow_capacity >= 20) {
//                Battery.setGraphic(Battery_Charging_1);
                Battery_ImageView.setImage(Battery_Charging_1_img);
            } else if(pow_capacity < 20) {
//                Battery.setGraphic(Battery_Charging_0);
                Battery_ImageView.setImage(Battery_Charging_0_img);
            }            
        } else if((pow_supply == false && pow_Charging == false)) {
            if(pow_capacity >= 80) {
//                Battery.setGraphic(Battery_No_Charging_full);
                Battery_ImageView.setImage(Battery_No_Charging_full_img);
            } else if(pow_capacity < 80 && pow_capacity >= 60) {
//                Battery.setGraphic(Battery_No_Charging_3);
                Battery_ImageView.setImage(Battery_No_Charging_3_img);
            } else if(pow_capacity < 60 && pow_capacity >= 40) {
//                Battery.setGraphic(Battery_No_Charging_2);
                Battery_ImageView.setImage(Battery_No_Charging_2_img);
            } else if(pow_capacity < 40 && pow_capacity >= 20) {
//                Battery.setGraphic(Battery_No_Charging_1);
                Battery_ImageView.setImage(Battery_No_Charging_1_img);
            } else if(pow_capacity < 20) {
//                Battery.setGraphic(Battery_No_Charging_0);
                Battery_ImageView.setImage(Battery_No_Charging_0_img);
            }
        }        
        
        
        
        // 每秒check  網路狀態
        Timer timer_BatteryCheck = new Timer(true);
        TimerTask task_BatteryCheck = new TimerTask(){
            @Override
            public void run() { 
//                System.out.println("power timer \n");

//                Battery_No_Charging_full = new ImageView(Battery_No_Charging_full_img);
//                Battery_No_Charging_3    = new ImageView(Battery_No_Charging_3_img);    
//                Battery_No_Charging_2    = new ImageView(Battery_No_Charging_2_img);  
//                Battery_No_Charging_1    = new ImageView(Battery_No_Charging_1_img);
//                Battery_No_Charging_0    = new ImageView(Battery_No_Charging_0_img);
//                Battery_Charging         = new ImageView(Battery_Charging_full_img);                
//                Battery_Charging_3           = new ImageView(Battery_Charging_3_img);    
//                Battery_Charging_2           = new ImageView(Battery_Charging_2_img);  
//                Battery_Charging_1           = new ImageView(Battery_Charging_1_img);
//                Battery_Charging_0           = new ImageView(Battery_Charging_0_img);                 

                try {
                    pow_supply   = BC.Get_Power_Supply();
                    pow_Charging = BC.Get_Power_Status();
                    pow_capacity = BC.Get_Power_Capacity();
                    
                    Platform.runLater(() -> {
                        if(!GB.lock_battery) {
                            if(pow_supply == true && pow_Charging == true || (pow_supply == true && pow_Charging == false)) {
                                if(pow_capacity >= 80) {
                    //                Battery.setGraphic(Battery_Charging);
                                    Battery_ImageView.setImage(Battery_Charging_full_img);
                                } else if(pow_capacity < 80 && pow_capacity >= 60) {
                    //                Battery.setGraphic(Battery_Charging_3);
                                    Battery_ImageView.setImage(Battery_Charging_3_img);
                                } else if(pow_capacity < 60 && pow_capacity >= 40) {
                    //                Battery.setGraphic(Battery_Charging_2);
                                    Battery_ImageView.setImage(Battery_Charging_2_img);
                                } else if(pow_capacity < 40 && pow_capacity >= 20) {
                    //                Battery.setGraphic(Battery_Charging_1);
                                    Battery_ImageView.setImage(Battery_Charging_1_img);
                                } else if(pow_capacity < 20) {
                    //                Battery.setGraphic(Battery_Charging_0);
                                    Battery_ImageView.setImage(Battery_Charging_0_img);
                                }            
                            } else if((pow_supply == false && pow_Charging == false)) {
                                if(pow_capacity >= 80) {
                    //                Battery.setGraphic(Battery_No_Charging_full);
                                    Battery_ImageView.setImage(Battery_No_Charging_full_img);
                                } else if(pow_capacity < 80 && pow_capacity >= 60) {
                    //                Battery.setGraphic(Battery_No_Charging_3);
                                    Battery_ImageView.setImage(Battery_No_Charging_3_img);
                                } else if(pow_capacity < 60 && pow_capacity >= 40) {
                    //                Battery.setGraphic(Battery_No_Charging_2);
                                    Battery_ImageView.setImage(Battery_No_Charging_2_img);
                                } else if(pow_capacity < 40 && pow_capacity >= 20) {
                    //                Battery.setGraphic(Battery_No_Charging_1);
                                    Battery_ImageView.setImage(Battery_No_Charging_1_img);
                                } else if(pow_capacity < 20) {
                    //                Battery.setGraphic(Battery_No_Charging_0);
                                    Battery_ImageView.setImage(Battery_No_Charging_0_img);
                                }
                            }  
                        }                                         
                    });                     

                } catch (UnknownHostException ex) {
                    Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                }              
            }
        };
            timer_BatteryCheck.schedule(task_BatteryCheck, 0, 3000); 
         
    }                
    // Input： ,  Output： , 功能： 清除系統垃圾/
    public void ClearSystemGarbage(){
        Timer timer_clear=new Timer(true);
        TimerTask task_clear=new TimerTask(){
            @Override
            public void run() {       
                //connect_on = null;
                time_y_m_d=null;//將變數 ＝ null 後清除
                time_h_m = null;//將變數 ＝ null 後清除  
                MainCurrentDT_process = null;
                MainCurrentDate = null;
                MainCurrentTime = null;
                MainCurrentDate_Year = null;
                MainCurrentDate_Mounth = null;
                MainCurrentDate_Day = null;
                MainCurrentTime_Hour = null;
                MainCurrentTime_Min = null;
                MainCurrentTime_Sec = null;
                NC.Connect_IP=null;
                NC.ethernet_name_DHCP = null;
          
                // System.gc();//清除系統垃圾 // 2017.06.05 william System.gc disable
                //System.out.println(" System.gc() \n");
            }
        };
            timer_clear.schedule(task_clear, 0, 60000); 
        
    }          
    // Input： ,  Output： , 功能： 滑鼠滑過button產生Tooptip
    public void Tooptip_buttonhover(){
        /*** Web ***/
        //tooltip_Web = new Tooltip();// imageButton提示框
        /* 2017.06.22 william hide web icon and button event
        tooltip_Web.setText(WordMap.get("Web_Title"));        
        Web.setOnMouseEntered((MouseEvent event) -> {   
            Web.requestFocus();
            if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){       
                Point2D p = Web.localToScreen(Web.getLayoutBounds().getMaxX()-100, Web.getLayoutBounds().getMaxY()-100); // -120 -91 I position the tooltip at bottom right of the node (see below for explanation)
                tooltip_Web.setStyle("-fx-font-family:'Droid Sans Fallback';");
                tooltip_Web.show(Web, p.getX(), p.getY());          
            }
            if("English".equals(WordMap.get("SelectedLanguage"))){
                Point2D p = Web.localToScreen(Web.getLayoutBounds().getMaxX()-110, Web.getLayoutBounds().getMaxY()-96); // -120 -91 I position the tooltip at bottom right of the node (see below for explanation)
                tooltip_Web.setStyle("-fx-font-family:'Ubuntu';");
                tooltip_Web.show(Web, p.getX(), p.getY());     
            }
            

        });           
        Web.setOnMouseExited((MouseEvent event) -> {
            tooltip_Web.hide();
            if(Web_Color == true){
                Web.setId("Button_web"); 
            }
        });
        */
        /*** ThinClient ***/
        //tooltip_ThinClient = new Tooltip();// imageButton提示框
        tooltip_ThinClient.setText(WordMap.get("ThinClient"));
        ThinClient.setOnMouseEntered((MouseEvent event) -> {
            ThinClient.requestFocus();
            if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){       
                Point2D p = ThinClient.localToScreen(ThinClient.getLayoutBounds().getMaxX()-83, ThinClient.getLayoutBounds().getMaxY()-100); // -104 -91 I position the tooltip at bottom right of the node (see below for explanation)
                tooltip_ThinClient.setStyle("-fx-font-family:'Droid Sans Fallback';");
                tooltip_ThinClient.show(ThinClient, p.getX(), p.getY());
            }
            else if("English".equals(WordMap.get("SelectedLanguage"))){
                Point2D p = ThinClient.localToScreen(ThinClient.getLayoutBounds().getMaxX()-105, ThinClient.getLayoutBounds().getMaxY()-96); // -104 -91 I position the tooltip at bottom right of the node (see below for explanation)
                tooltip_ThinClient.setStyle("-fx-font-family:'Ubuntu';");
                tooltip_ThinClient.show(ThinClient, p.getX(), p.getY());
            } else if("Japanese".equals(WordMap.get("SelectedLanguage"))){
                Point2D p = ThinClient.localToScreen(ThinClient.getLayoutBounds().getMaxX()-105, ThinClient.getLayoutBounds().getMaxY()-100); // -104 -91 I position the tooltip at bottom right of the node (see below for explanation)
                tooltip_ThinClient.setStyle("-fx-font-family:'Droid Sans Fallback';");
                tooltip_ThinClient.show(ThinClient, p.getX(), p.getY());            
            }


        });             
        ThinClient.setOnMouseExited((MouseEvent event) -> {
            tooltip_ThinClient.hide();
            if(ThinClient_Color == true){
                ThinClient.setId("Button_ThinClient"); 
            }
        });        
        
        /*** ManageVD ***/
        //tooltip_ManageVD = new Tooltip(); // imageButton提示框
        tooltip_ManageVD.setText(WordMap.get("ManageVD_Title"));        
        ManageVD.setOnMouseEntered((MouseEvent event) -> {
            ManageVD.requestFocus();
            //System.out.println("*** ManageVD.setOnMouseDragEntered *** \n");
            if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){  
                Point2D p = ManageVD.localToScreen(ManageVD.getLayoutBounds().getMaxX()-77, ManageVD.getLayoutBounds().getMaxY()-100); // -100 -91 I position the tooltip at bottom right of the node (see below for explanation)
                tooltip_ManageVD.setStyle("-fx-font-family:'Droid Sans Fallback';");
                tooltip_ManageVD.show(ManageVD, p.getX(), p.getY());
            }
            else if("English".equals(WordMap.get("SelectedLanguage"))){
                Point2D p = ManageVD.localToScreen(ManageVD.getLayoutBounds().getMaxX()-95, ManageVD.getLayoutBounds().getMaxY()-96); // -100 -91 I position the tooltip at bottom right of the node (see below for explanation)
                tooltip_ManageVD.setStyle("-fx-font-family:'Ubuntu';");
                tooltip_ManageVD.show(ManageVD, p.getX(), p.getY());
            } else  if("Japanese".equals(WordMap.get("SelectedLanguage"))){
                Point2D p = ManageVD.localToScreen(ManageVD.getLayoutBounds().getMaxX()-145, ManageVD.getLayoutBounds().getMaxY()-100); // -100 -91 I position the tooltip at bottom right of the node (see below for explanation)
                tooltip_ManageVD.setStyle("-fx-font-family:'Droid Sans Fallback';");
                tooltip_ManageVD.show(ManageVD, p.getX(), p.getY());            
            }


        });             
        ManageVD.setOnMouseExited((MouseEvent event) -> {
            //System.out.println("*** ManageVD.setOnMouseExited *** \n");
            tooltip_ManageVD.hide();
            if(ManageVD_Color == true){
                ManageVD.setId("Button_ManageVD"); 
            }
        });
        
        /*** ChangePW ***/
        //tooltip_ChangePW = new Tooltip(); // imageButton提示框
        tooltip_ChangePW.setText(WordMap.get("CP_Window_Title"));
        ChangePW.setOnMouseEntered((MouseEvent event) -> {
            ChangePW.requestFocus();     
            if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){  
                Point2D p = ChangePW.localToScreen(ChangePW.getLayoutBounds().getMaxX()-69, ChangePW.getLayoutBounds().getMaxY()-100); // -93 -91 I position the tooltip at bottom right of the node (see below for explanation)
                tooltip_ChangePW.setStyle("-fx-font-family:'Droid Sans Fallback';");
                tooltip_ChangePW.show(ChangePW, p.getX(), p.getY());
            }
            else if("English".equals(WordMap.get("SelectedLanguage"))){
                Point2D p = ChangePW.localToScreen(ChangePW.getLayoutBounds().getMaxX()-100, ChangePW.getLayoutBounds().getMaxY()-96); // -93 -91 I position the tooltip at bottom right of the node (see below for explanation)
                tooltip_ChangePW.setStyle("-fx-font-family:'Ubuntu';");
                tooltip_ChangePW.show(ChangePW, p.getX(), p.getY());                
            } else if("Japanese".equals(WordMap.get("SelectedLanguage"))) {
                Point2D p = ChangePW.localToScreen(ChangePW.getLayoutBounds().getMaxX()-90, ChangePW.getLayoutBounds().getMaxY()-100); // -93 -91 I position the tooltip at bottom right of the node (see below for explanation)
                tooltip_ChangePW.setStyle("-fx-font-family:'Droid Sans Fallback';");
                tooltip_ChangePW.show(ChangePW, p.getX(), p.getY());            
            }


        });           
        ChangePW.setOnMouseExited((MouseEvent event) -> {
            tooltip_ChangePW.hide();
            if(ChangePW_Color == true){
                ChangePW.setId("Button_ChangePW"); 
            }
        });
        
        /*** TimeCalendar ***/
        //tooltip_TimeCalendar = new Tooltip(); // imageButton提示框
        tooltip_TimeCalendar.setText(WordMap.get("TimeCalendar_title"));       
        TimeCalendar.setOnMouseEntered((MouseEvent timeevent) -> {
            TimeCalendar.requestFocus();     
            
            if(GB.Time_change==true){ // 2017.06.19 william click bug fix
            
                if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){  
                    Point2D p = TimeCalendar.localToScreen(TimeCalendar.getLayoutBounds().getMaxX()-107, TimeCalendar.getLayoutBounds().getMaxY()-100); //I position the tooltip at bottom right of the node (see below for explanation)
                    tooltip_TimeCalendar.setStyle("-fx-font-family:'Droid Sans Fallback';  -fx-font-weight: 700; ");
                    tooltip_TimeCalendar.show(TimeCalendar, p.getX(), p.getY());
                }
                else if("English".equals(WordMap.get("SelectedLanguage"))){
                    Point2D p = TimeCalendar.localToScreen(TimeCalendar.getLayoutBounds().getMaxX()-118, TimeCalendar.getLayoutBounds().getMaxY()-96);
                    tooltip_TimeCalendar.setStyle("-fx-font-family:'Ubuntu';  -fx-font-weight: 700; ");
                    tooltip_TimeCalendar.show(TimeCalendar, p.getX(), p.getY());
                } else {
                    Point2D p = TimeCalendar.localToScreen(TimeCalendar.getLayoutBounds().getMaxX()-107, TimeCalendar.getLayoutBounds().getMaxY()-100); //I position the tooltip at bottom right of the node (see below for explanation)
                    tooltip_TimeCalendar.setStyle("-fx-font-family:'Droid Sans Fallback';  -fx-font-weight: 700; ");
                    tooltip_TimeCalendar.show(TimeCalendar, p.getX(), p.getY());                
                } 
                //System.out.print("* in * Time_change= true ** \n");         
            }
            if(GB.Time_change==false){ // 2017.06.19 william click bug fix
                //tooltip_TimeCalendar.hide();
                //System.out.print("* in * Time_change= false ** \n");   
            }

        });     
        
 
        TimeCalendar.setOnMouseExited((MouseEvent timeevent) -> {
            //System.out.print("* off * Time_change 00 ** \n");
            tooltip_TimeCalendar.hide();
            if(Time_Color == true){
                TimeCalendar.setId("ToggleButton_time"); 
            }           
        });
  
 
        
        /*** Shutdown ***/
        //tooltip_Shutdown = new Tooltip(); // imageButton提示框
        tooltip_Shutdown.setText(WordMap.get("Button_Shutdown"));       
        Shutdown.setOnMouseEntered((MouseEvent event) -> {
            Shutdown.requestFocus();     
            if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){  
                Point2D p = Shutdown.localToScreen(Shutdown.getLayoutBounds().getMaxX()-56, Shutdown.getLayoutBounds().getMaxY()-100); //I position the tooltip at bottom right of the node (see below for explanation)
                tooltip_Shutdown.setStyle("-fx-font-family:'Droid Sans Fallback';  -fx-font-weight: 700; ");
                tooltip_Shutdown.show(Shutdown, p.getX(), p.getY());
            }
            else if("English".equals(WordMap.get("SelectedLanguage"))){
                Point2D p = Shutdown.localToScreen(Shutdown.getLayoutBounds().getMaxX()-75, Shutdown.getLayoutBounds().getMaxY()-96);
                tooltip_Shutdown.setStyle("-fx-font-family:'Ubuntu';  -fx-font-weight: 700; ");
                tooltip_Shutdown.show(Shutdown, p.getX(), p.getY());
            } else if("Japanese".equals(WordMap.get("SelectedLanguage"))){
                Point2D p = Shutdown.localToScreen(Shutdown.getLayoutBounds().getMaxX()-105, Shutdown.getLayoutBounds().getMaxY()-100); //I position the tooltip at bottom right of the node (see below for explanation)
                tooltip_Shutdown.setStyle("-fx-font-family:'Droid Sans Fallback';  -fx-font-weight: 700; ");
                tooltip_Shutdown.show(Shutdown, p.getX(), p.getY());            
            }          
        });           
        Shutdown.setOnMouseExited((MouseEvent event) -> {
            tooltip_Shutdown.hide();
            if(Shutdown_Color == true){
                Shutdown.setId("Button_SShutdown"); 
            }
        });

        /*** Reboot ***/
        //tooltip_Reboot = new Tooltip(); // imageButton提示框
        tooltip_Reboot.setText(WordMap.get("Button_Reboot"));
        Reboot.setOnMouseEntered((MouseEvent event) -> {
            Reboot.requestFocus();  
            
            if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){  
                Point2D p = Reboot.localToScreen(Reboot.getLayoutBounds().getMaxX()-68, Reboot.getLayoutBounds().getMaxY()-100);                
                tooltip_Reboot.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 700;");
                tooltip_Reboot.show(Reboot, p.getX(), p.getY());
            }
            else if("English".equals(WordMap.get("SelectedLanguage"))){
                Point2D p = Reboot.localToScreen(Reboot.getLayoutBounds().getMaxX()-66, Reboot.getLayoutBounds().getMaxY()-96);                
                tooltip_Reboot.setStyle("-fx-font-family:'Ubuntu'; -fx-font-weight: 700;");
                tooltip_Reboot.show(Reboot, p.getX(), p.getY());
            } else if("Japanese".equals(WordMap.get("SelectedLanguage"))){
                Point2D p = Reboot.localToScreen(Reboot.getLayoutBounds().getMaxX()-63, Reboot.getLayoutBounds().getMaxY()-100);                
                tooltip_Reboot.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 700;");
                tooltip_Reboot.show(Reboot, p.getX(), p.getY());            
            }      
   
        });           
        Reboot.setOnMouseExited((MouseEvent event) -> {
            tooltip_Reboot.hide();
            if(Reboot_Color == true){
                Reboot.setId("Button_RReboot"); 
            }
        });
        
        /*** Connect_ON_OFF ***/
        //tooltip_Connect_ON_OFF = new Tooltip(); // imageButton提示框
        //tooltip_Connect_ON_OFF.setText(WordMap.get("Button_Network_ON"));
        Connect_ON_OFF.setOnMouseEntered((MouseEvent event) -> {
            Connect_ON_OFF.requestFocus(); 
            if(network_change == true){
                tooltip_Connect_ON_OFF.setText(WordMap.get("Button_Network_ON"));                
            }else{
                tooltip_Connect_ON_OFF.setText(WordMap.get("Button_Nectwork_OFF"));
            }
            
            if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))){  
                Point2D p = Connect_ON_OFF.localToScreen(Connect_ON_OFF.getLayoutBounds().getMaxX()-95, Connect_ON_OFF.getLayoutBounds().getMaxY()-100); 
                tooltip_Connect_ON_OFF.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 700;");
                tooltip_Connect_ON_OFF.show(Connect_ON_OFF, p.getX(), p.getY());
            }
            else if(("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){  
                Point2D p = Connect_ON_OFF.localToScreen(Connect_ON_OFF.getLayoutBounds().getMaxX()-93, Connect_ON_OFF.getLayoutBounds().getMaxY()-100); 
                tooltip_Connect_ON_OFF.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 700;");
                tooltip_Connect_ON_OFF.show(Connect_ON_OFF, p.getX(), p.getY());
            }
            else if("English".equals(WordMap.get("SelectedLanguage"))){
                Point2D p = Connect_ON_OFF.localToScreen(Connect_ON_OFF.getLayoutBounds().getMaxX()-100, Connect_ON_OFF.getLayoutBounds().getMaxY()-96); 
                tooltip_Connect_ON_OFF.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 700;");
                tooltip_Connect_ON_OFF.show(Connect_ON_OFF, p.getX(), p.getY());
            } else if("Japanese".equals(WordMap.get("SelectedLanguage"))){
                Point2D p = Connect_ON_OFF.localToScreen(Connect_ON_OFF.getLayoutBounds().getMaxX()-110, Connect_ON_OFF.getLayoutBounds().getMaxY()-100); 
                tooltip_Connect_ON_OFF.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 700;");
                tooltip_Connect_ON_OFF.show(Connect_ON_OFF, p.getX(), p.getY());                        
            }

        });           
        Connect_ON_OFF.setOnMouseExited((MouseEvent event) -> {
            tooltip_Connect_ON_OFF.hide();
        });
        
        
        
        // 2018.04.26 william Battery實作
        if(battery_enable) {
//        if(pow_Charging) { 
//            tooltip_Battery        
        Battery.setOnMouseEntered((MouseEvent event) -> {
            Battery.requestFocus(); 
            
            if(pow_Charging) { // 2018.04.26 william Battery實作
                tooltip_Battery.setText(pow_capacity + "%" + "(" + WordMap.get("Charging") + ")"); 
                tc_p_x    = 80;
                sc_p_x    = 80;
                en_p_x    = 90;                
            } else {
                tooltip_Battery.setText(pow_capacity + "%"); 
                tc_p_x    = 58;
                sc_p_x    = 58;
                en_p_x    = 58;
            }            
            
            
            if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))){  
                tooltip_Battery_p = Battery.localToScreen(Battery.getLayoutBounds().getMaxX()-tc_p_x, Battery.getLayoutBounds().getMaxY()-100); 
                tooltip_Battery.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 700;");
                tooltip_Battery.show(Battery, tooltip_Battery_p.getX(), tooltip_Battery_p.getY());
            }
            else if(("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){  
                tooltip_Battery_p = Battery.localToScreen(Battery.getLayoutBounds().getMaxX()-sc_p_x, Battery.getLayoutBounds().getMaxY()-100); 
                tooltip_Battery.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 700;");
                tooltip_Battery.show(Battery, tooltip_Battery_p.getX(), tooltip_Battery_p.getY());
            }
            else if("English".equals(WordMap.get("SelectedLanguage"))){
                tooltip_Battery_p = Battery.localToScreen(Battery.getLayoutBounds().getMaxX()-en_p_x, Battery.getLayoutBounds().getMaxY()-96); 
                tooltip_Battery.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 700;");
                tooltip_Battery.show(Battery, tooltip_Battery_p.getX(), tooltip_Battery_p.getY());
            } else {
                tooltip_Battery_p = Battery.localToScreen(Battery.getLayoutBounds().getMaxX()-tc_p_x, Battery.getLayoutBounds().getMaxY()-100); 
                tooltip_Battery.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 700;");
                tooltip_Battery.show(Battery, tooltip_Battery_p.getX(), tooltip_Battery_p.getY());            
            }

        });           
        Battery.setOnMouseExited((MouseEvent event) -> {
            tooltip_Battery.hide();
        });         
        
            tooltip_Brightness.setText(WordMap.get("Brightness"));
            Brightness.setOnMouseEntered((MouseEvent event) -> {
                Brightness.requestFocus(); 

                if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))){  
                    Point2D p = Brightness.localToScreen(Brightness.getLayoutBounds().getMaxX()-55, Brightness.getLayoutBounds().getMaxY()-100); 
                    tooltip_Brightness.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 700;");
                    tooltip_Brightness.show(Brightness, p.getX(), p.getY());
                }
                else if(("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){  
                    Point2D p = Brightness.localToScreen(Brightness.getLayoutBounds().getMaxX()-55, Brightness.getLayoutBounds().getMaxY()-100); 
                    tooltip_Brightness.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 700;");
                    tooltip_Brightness.show(Brightness, p.getX(), p.getY());
                }
                else if("English".equals(WordMap.get("SelectedLanguage"))){
                    Point2D p = Brightness.localToScreen(Brightness.getLayoutBounds().getMaxX()-80, Brightness.getLayoutBounds().getMaxY()-96); 
                    tooltip_Brightness.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 700;");
                    tooltip_Brightness.show(Brightness, p.getX(), p.getY());
                } else {
                    Point2D p = Brightness.localToScreen(Brightness.getLayoutBounds().getMaxX()-55, Brightness.getLayoutBounds().getMaxY()-100); 
                    tooltip_Brightness.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 700;");
                    tooltip_Brightness.show(Brightness, p.getX(), p.getY());                
                }

            });           
            Brightness.setOnMouseExited((MouseEvent event) -> {
                tooltip_Brightness.hide();
            });         
        }
       
        
        
        
        /*** SS ***/// 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        tooltip_SS.setText(WordMap.get("Snapshot"));
        SS.setOnMouseEntered((MouseEvent event) -> {
            SS.requestFocus();
            if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){       
                Point2D p = SS.localToScreen(SS.getLayoutBounds().getMaxX()-55, ThinClient.getLayoutBounds().getMaxY()-100); // -104 -91 I position the tooltip at bottom right of the node (see below for explanation)
                tooltip_SS.setStyle("-fx-font-family:'Droid Sans Fallback';");
                tooltip_SS.show(SS, p.getX(), p.getY());
            }
            else if("English".equals(WordMap.get("SelectedLanguage"))){
                Point2D p = SS.localToScreen(SS.getLayoutBounds().getMaxX()-73, ThinClient.getLayoutBounds().getMaxY()-96); // -104 -91 I position the tooltip at bottom right of the node (see below for explanation)
                tooltip_SS.setStyle("-fx-font-family:'Ubuntu';");
                tooltip_SS.show(SS, p.getX(), p.getY());
            } else if("Japanese".equals(WordMap.get("SelectedLanguage"))){
                Point2D p = SS.localToScreen(SS.getLayoutBounds().getMaxX()-95, ThinClient.getLayoutBounds().getMaxY()-100); // -104 -91 I position the tooltip at bottom right of the node (see below for explanation)
                tooltip_SS.setStyle("-fx-font-family:'Droid Sans Fallback';");
                tooltip_SS.show(SS, p.getX(), p.getY());            
            }


        });             
        SS.setOnMouseExited((MouseEvent event) -> {
            tooltip_SS.hide();
            if(SS_Color == true){
                SS.setId("Button_SS"); 
            }
        });  
                      
    }    
    // Input： ,  Output： , 功能： 滑鼠滑過登入button之動作 (-----目前無使用-----)
    public void Loginbuttonhover_focus(){       
        
        Login.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if("Button_Login".equals(Login.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    Login.setId("Loginbutton");
                }
                //System.out.print("**Login IN**\n");
                
            } if (oldValue){                
                if("Button_Login".equals(Login.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    Login.setId("Loginbutton");
                }               
                //System.out.print("**Login OUT**\n");                
            }
            if(Login.isFocused()){                
                //System.out.print("--Login.isFocused()--\n");
                //System.out.print("**Login isFocused** : "+ Login.getId() +"\n");                
                ALLButtonColorChange(); // 所有button 顏色變化 效果
                
                // 2017.06.22 william hide web icon and button event
                if("Loginbutton".equals(Login.getId())){
//                    if(!"Button_web".equals(Web.getId())){
                        if(!"Button_ThinClient".equals(ThinClient.getId())){
                            if(!"Button_ManageVD".equals(ManageVD.getId())){
                                if(!"Button_ChangePW".equals(ChangePW.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){
                                                //Login.setId("Button_hover");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                            }
                                        }
                                    } 
                                }    
                            }
                        }
//                    }
                }
            }
        });
        
        Login.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if("Button_Login".equals(Login.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    Login.setId("Loginbutton");
                }
                //System.out.print("**Login IN**\n");
                
            } if (oldValue){                
                if("Button_Login".equals(Login.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    Login.setId("Loginbutton");
                }               
                //System.out.print("**Login OUT**\n");                
            }
            //System.out.print("**Login ** : "+ Login.getId() +"\n");
            if(Login.isHover()){                               
                //System.out.print("--Login.isHover()--\n");
                //System.out.print("**Login isHover ** : "+ Login.getId() +"\n"); 
                ALLButtonColorChange(); // 所有button 顏色變化 效果
                
                // 2017.06.22 william hide web icon and button event
                if("Loginbutton".equals(Login.getId())){
//                    if(!"Button_web".equals(Web.getId())){
                        if(!"Button_ThinClient".equals(ThinClient.getId())){
                            if(!"Button_ManageVD".equals(ManageVD.getId())){
                                if(!"Button_ChangePW".equals(ChangePW.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){
                                                //Login.setId("Button_hover");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                            }
                                        }
                                    } 
                                }    
                            }
                        }
//                    }
                } 
                /*** button 在第一次 Mouse-in 後 皆會直接進入 setOnMouseMoved ; 因此在mouse-in時加入條件  ***/
                /*** button 在第一次 Mouse-out 後 皆會直接進入 setOnMouseExited ; 因此在mouse-out時加入條件  ***/
                
                // 2017.06.22 william hide web icon and button event
                if("Button_Login".equals(Login.getId())){
//                    if(!"Button_web".equals(Web.getId())){
                        if(!"Button_ThinClient".equals(ThinClient.getId())){
                            if(!"Button_ManageVD".equals(ManageVD.getId())){
                                if(!"Button_ChangePW".equals(ChangePW.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){
                                                //Login.setId("Button_hover");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                                Login.setOnMouseMoved((event)-> {
                                                    //System.out.print("** Moved 003 ** \n");
                                                    if(Login_Color == true){
                                                        Login.setId("Loginbutton");
                                                    }

                                                });                                
                                                Login.setOnMouseExited((event)-> {
                                                    //System.out.print("** Exited 001 ** \n"); 
                                                    if(Login_Color == true){
                                                        Login.setId("Button_Login"); 
                                                    }
                                                    //System.out.print("--Login.setOnMouseExited--::"+ Login.getId() +"\n");                       
                                                });  
                                            }
                                        }
                                    } 
                                }    
                            }
                        }
//                    }
                }               
            }
        });        
    }
    // Input： ,  Output： , 功能： 滑鼠滑過Web button之動作 (-----目前無使用-----)
    public void Webbuttonhover_focus(){  
        Web.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if("Button_web".equals(Web.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    Web.setId("button");
                }
                
            } if (oldValue){                
                if("Button_web".equals(Web.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    Web.setId("button");
                }               
            }
            if(Web.isFocused()){                
                //System.out.print("--Login.isFocused()--\n");
                //System.out.print("**Login isFocused** : "+ Login.getId() +"\n");              
                ALLButtonColorChange();  // 所有button 顏色變化 效果
                
                if("button".equals(Web.getId())){
                    if(!"Button_Login".equals(Login.getId())){
                        if(!"Button_ThinClient".equals(ThinClient.getId())){
                            if(!"Button_ManageVD".equals(ManageVD.getId())){
                                if(!"Button_ChangePW".equals(ChangePW.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){   
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){   
                                                Login.setId("Loginbutton");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                            }
                                        }
                                    } 
                                }    
                            }
                        }
                    }
                }
                // 2017.06.08 william add choose icon function
                if("Button_web".equals(Web.getId())){
                    if(!"Button_Login".equals(Login.getId())){
                        if(!"Button_ThinClient".equals(ThinClient.getId())){
                            if(!"Button_ManageVD".equals(ManageVD.getId())){
                                if(!"Button_ChangePW".equals(ChangePW.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){  
                                                Login.setId("Loginbutton");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                                // Web.setId("button");
                                                Web.setOnKeyPressed((event)-> {
                                                    tooltip_Web.hide();
                                                    if(Web_Color == true){
                                                        Web.setId("Button_web"); 
                                                    }
                                                    //event.get
                                                    if(event.getCode() == KeyCode.ENTER){
                                                        Web.fire();
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.PAGE_DOWN){
                                                        LanguageSelection.requestFocus();
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.DOWN){
                                                        LanguageSelection.requestFocus();
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.TAB){
                                                        LanguageSelection.requestFocus();
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.PAGE_UP){
                                                        ChangePW.requestFocus();
                                                        ChangePW.setId("button");
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.UP){
                                                        ChangePW.requestFocus();
                                                        ChangePW.setId("button");
                                                        event.consume();
                                                    }  
                                                    if(event.getCode() == KeyCode.LEFT){
                                                        ChangePW.requestFocus();
                                                        ChangePW.setId("button");
                                                        event.consume();
                                                    }
                                                });  
                                            }
                                        }
                                    } 
                                }    
                            }
                        }
                    }
                }
                
            }
        });
        
        Web.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if("Button_web".equals(Web.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    Web.setId("button");
                }
                
            } if (oldValue){                
                if("Button_web".equals(Web.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    Web.setId("button");
                }                             
            }
            //System.out.print("**Login ** : "+ Login.getId() +"\n");
            if(Web.isHover()){                               
                //System.out.print("--Login.isHover()--\n");
                //System.out.print("**Login isHover ** : "+ Login.getId() +"\n");
                ALLButtonColorChange();   // 所有button 顏色變化 效果         
 
                if("button".equals(Web.getId())){
                    if(!"Button_Login".equals(Login.getId())){
                        if(!"Button_ThinClient".equals(ThinClient.getId())){
                            if(!"Button_ManageVD".equals(ManageVD.getId())){
                                if(!"Button_ChangePW".equals(ChangePW.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){      
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){  
                                                Login.setId("Loginbutton");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                            }
                                        }
                                    } 
                                }    
                            }
                        }
                    }
                }
                /*** button 在第一次 Mouse-in 後 皆會直接進入 setOnMouseMoved ; 因此在mouse-in時加入條件  ***/
                /*** button 在第一次 Mouse-out 後 皆會直接進入 setOnMouseExited ; 因此在mouse-out時加入條件  ***/
                if("Button_web".equals(Web.getId())){
                    if(!"Button_Login".equals(Login.getId())){
                        if(!"Button_ThinClient".equals(ThinClient.getId())){
                            if(!"Button_ManageVD".equals(ManageVD.getId())){
                                if(!"Button_ChangePW".equals(ChangePW.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){  
                                                Login.setId("Loginbutton");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                                Web.setOnMouseMoved((event)-> {
                                                    //System.out.print("** Moved 003 ** \n");
                                                    if(Web_Color == true){
                                                        Web.setId("button");
                                                    }
                                                });                                
                                                Web.setOnMouseExited((event)-> {
                                                    //System.out.print("** Exited 001 ** \n"); 
                                                    tooltip_Web.hide();
                                                    if(Web_Color == true){
                                                        Web.setId("Button_web"); 
                                                    }
                                                    //System.out.print("--Web.setOnMouseExited--::"+ Web.getId() +"\n");                       
                                                });  
                                            }
                                        }
                                    } 
                                }    
                            }
                        }
                    }
                }               
            }
        });
    }    
    // Input： ,  Output： , 功能： 滑鼠滑過瘦客戶機設定button之動作 (-----目前無使用-----)
    public void ThinClientbuttonhover_focus(){  
        ThinClient.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if("Button_ThinClient".equals(ThinClient.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    ThinClient.setId("button");
                }
                
            } if (oldValue){                
                if("Button_ThinClient".equals(ThinClient.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    ThinClient.setId("button");
                }               
            }
            if(ThinClient.isFocused()){                
                //System.out.print("--Login.isFocused()--\n");
                //System.out.print("**Login isFocused** : "+ Login.getId() +"\n");                
                ALLButtonColorChange();  // 所有button 顏色變化 效果
                
                // 2017.06.22 william hide web icon and button event
                if("button".equals(ThinClient.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ManageVD".equals(ManageVD.getId())){
                                if(!"Button_ChangePW".equals(ChangePW.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){    
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){  
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }
                // 2017.06.08 william add choose icon function
                // 2017.06.22 william hide web icon and button event
                if("Button_ThinClient".equals(ThinClient.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ManageVD".equals(ManageVD.getId())){
                                if(!"Button_ChangePW".equals(ChangePW.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){  
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                                // ThinClient.setId("button");
                                                ThinClient.setOnKeyPressed((event)-> {
                                                    tooltip_ThinClient.hide();
                                                    if(ThinClient_Color == true){
                                                        ThinClient.setId("Button_ThinClient"); 
                                                    }
                                                    if(event.getCode() == KeyCode.ENTER){
                                                        ThinClient.fire();
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.PAGE_DOWN){
                                                        ManageVD.requestFocus();
                                                        ManageVD.setId("button");
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.DOWN){
                                                        ManageVD.requestFocus();
                                                        ManageVD.setId("button");
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.TAB){
                                                        ManageVD.requestFocus();
                                                        ManageVD.setId("button");
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.RIGHT){
                                                        ManageVD.requestFocus();
                                                        ManageVD.setId("button");
                                                        event.consume();
                                                    } 
                                                    if(event.getCode() == KeyCode.PAGE_UP){
                                                        checkBox_Pw.requestFocus();
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.UP){
                                                        checkBox_Pw.requestFocus();
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.LEFT){ 
                                                        TimeCalendar.requestFocus();
                                                        event.consume();
                                                    }
                                                }); 
 
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }  
                    
            }
        });
        
        ThinClient.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if("Button_ThinClient".equals(ThinClient.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    ThinClient.setId("button");
                }
                
            } if (oldValue){                
                if("Button_ThinClient".equals(ThinClient.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    ThinClient.setId("button");
                }                             
            }
            //System.out.print("**Login ** : "+ Login.getId() +"\n");
            if(ThinClient.isHover()){                               
                //System.out.print("--Login.isHover()--\n");
                //System.out.print("**Login isHover ** : "+ Login.getId() +"\n");       
                ALLButtonColorChange();  // 所有button 顏色變化 效果
                
                // 2017.06.22 william hide web icon and button event
                if("button".equals(ThinClient.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ManageVD".equals(ManageVD.getId())){
                                if(!"Button_ChangePW".equals(ChangePW.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){  
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }
                /*** button 在第一次 Mouse-in 後 皆會直接進入 setOnMouseMoved ; 因此在mouse-in時加入條件  ***/
                /*** button 在第一次 Mouse-out 後 皆會直接進入 setOnMouseExited ; 因此在mouse-out時加入條件  ***/
                
                // 2017.06.22 william hide web icon and button event
                if("Button_ThinClient".equals(ThinClient.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ManageVD".equals(ManageVD.getId())){
                                if(!"Button_ChangePW".equals(ChangePW.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){  
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                                ThinClient.setOnMouseMoved((event)-> {
                                                    //System.out.print("** Moved 003 ** \n");
                                                    if(ThinClient_Color == true){
                                                        ThinClient.setId("button");
                                                    }
                                                });                                
                                                ThinClient.setOnMouseExited((event)-> {
                                                    //System.out.print("** Exited 001 ** \n"); 
                                                    tooltip_ThinClient.hide();
                                                    if(ThinClient_Color == true){
                                                        ThinClient.setId("Button_ThinClient"); 
                                                    }
                                                    //System.out.print("--Web.setOnMouseExited--::"+ Web.getId() +"\n");                       
                                                });  
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }               
            }
        });       
        
    }    
    // Input： ,  Output： , 功能： 滑鼠滑過雲桌面管理button之動作 (-----目前無使用-----)
    public void ManageVDbuttonhover_focus(){  
        ManageVD.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if("Button_ManageVD".equals(ManageVD.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    ManageVD.setId("button");
                }
                
            } if (oldValue){                
                if("Button_ManageVD".equals(ManageVD.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    ManageVD.setId("button");
                }               
            }
            if(ManageVD.isFocused()){                
                //System.out.print("--Login.isFocused()--\n");
                //System.out.print("**Login isFocused** : "+ Login.getId() +"\n");                
                ALLButtonColorChange();  // 所有button 顏色變化 效果
                
                // 2017.06.22 william hide web icon and button event
                if("button".equals(ManageVD.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ThinClient".equals(ThinClient.getId())){
                                if(!"Button_ChangePW".equals(ChangePW.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){  
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ChangePW.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }
                // 2017.06.08 william add choose icon function
                // 2017.06.22 william hide web icon and button event
                if("Button_ManageVD".equals(ManageVD.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ThinClient".equals(ThinClient.getId())){
                                if(!"Button_ChangePW".equals(ChangePW.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){  
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ChangePW.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                                // ManageVD.setId("button");
                                                ManageVD.setOnKeyPressed((event)-> {
                                                    tooltip_ManageVD.hide();
                                                    if(ManageVD_Color == true){
                                                        ManageVD.setId("Button_ManageVD");
                                                    }
                                                    //event.get
                                                    if(event.getCode() == KeyCode.ENTER){
                                                        ManageVD.fire();
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.PAGE_DOWN){
                                                        ChangePW.requestFocus();
                                                        ChangePW.setId("button");
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.DOWN){
                                                        ChangePW.requestFocus();
                                                        ChangePW.setId("button");
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.TAB){
                                                        ChangePW.requestFocus();
                                                        ChangePW.setId("button");
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.RIGHT){
                                                        ChangePW.requestFocus();
                                                        ChangePW.setId("button");
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.PAGE_UP){
                                                        ThinClient.requestFocus();
                                                        ThinClient.setId("button");
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.UP){
                                                        ThinClient.requestFocus();
                                                        ThinClient.setId("button");
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.LEFT){
                                                        ThinClient.requestFocus();
                                                        ThinClient.setId("button");
                                                        event.consume();
                                                    }
                                                });                                
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }    
                
            }
        });
        
        ManageVD.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if("Button_ManageVD".equals(ManageVD.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    ManageVD.setId("button");
                }
                
            } if (oldValue){                
                if("Button_ManageVD".equals(ManageVD.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    ManageVD.setId("button");
                }                             
            }
            //System.out.print("**Login ** : "+ Login.getId() +"\n");
            if(ManageVD.isHover()){                               
                //System.out.print("--Login.isHover()--\n");
                //System.out.print("**Login isHover ** : "+ Login.getId() +"\n");  
                ALLButtonColorChange();  // 所有button 顏色變化 效果
                
                // 2017.06.22 william hide web icon and button event
                if("button".equals(ManageVD.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ThinClient".equals(ThinClient.getId())){
                                if(!"Button_ChangePW".equals(ChangePW.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){  
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){  
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ChangePW.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }
                /*** button 在第一次 Mouse-in 後 皆會直接進入 setOnMouseMoved ; 因此在mouse-in時加入條件  ***/
                /*** button 在第一次 Mouse-out 後 皆會直接進入 setOnMouseExited ; 因此在mouse-out時加入條件  ***/
                
                // 2017.06.22 william hide web icon and button event
                if("Button_ManageVD".equals(ManageVD.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ThinClient".equals(ThinClient.getId())){
                                if(!"Button_ChangePW".equals(ChangePW.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){  
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ChangePW.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                                ManageVD.setOnMouseMoved((event)-> {
                                                    //System.out.print("** Moved 003 ** \n");
                                                    if(ManageVD_Color == true){
                                                        ManageVD.setId("button");
                                                    }
                                                });                                
                                                ManageVD.setOnMouseExited((event)-> {
                                                    //System.out.print("** Exited 001 ** \n"); 
                                                    tooltip_ManageVD.hide();
                                                    if(ManageVD_Color == true){
                                                        ManageVD.setId("Button_ManageVD"); 
                                                    }
                                                    //System.out.print("--Web.setOnMouseExited--::"+ Web.getId() +"\n");                       
                                                });  
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }               
            }
        });       
        
    }    
    // Input： ,  Output： , 功能： 滑鼠滑過修改密碼button之動作 (-----目前無使用-----)
    public void ChangePWbuttonhover_focus(){  
        ChangePW.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if("Button_ChangePW".equals(ChangePW.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    ChangePW.setId("button");
                }
                
            } if (oldValue){                
                if("Button_ChangePW".equals(ChangePW.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    ChangePW.setId("button");
                }               
            }
            if(ChangePW.isFocused()){                
                //System.out.print("--Login.isFocused()--\n");
                //System.out.print("**Login isFocused** : "+ Login.getId() +"\n");                
                ALLButtonColorChange();   // 所有button 顏色變化 效果
                
                // 2017.06.22 william hide web icon and button event
                if("button".equals(ChangePW.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ThinClient".equals(ThinClient.getId())){
                                if(!"Button_ManageVD".equals(ManageVD.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){     
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){                                        
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }
                // 2017.06.08 william add choose icon function
                // 2017.06.22 william hide web icon and button event
                if("Button_ChangePW".equals(ChangePW.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ThinClient".equals(ThinClient.getId())){
                                if(!"Button_ManageVD".equals(ManageVD.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){  
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                                // ChangePW.setId("button");
                                                ChangePW.setOnKeyPressed((event)-> {
                                                    tooltip_ChangePW.hide();
                                                    if(ChangePW_Color == true){
                                                        ChangePW.setId("Button_ChangePW"); 
                                                    }
                                                    //event.get
                                                    if(event.getCode() == KeyCode.ENTER){
                                                        ChangePW.fire();
                                                        event.consume();
                                                    }
                                                    // 2017.06.22 william hide web icon and button event
                                                    /*
                                                    if(event.getCode() == KeyCode.PAGE_DOWN){
                                                        Web.requestFocus();
                                                        Web.setId("button");
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.DOWN){
                                                        Web.requestFocus();
                                                        Web.setId("button");
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.TAB){
                                                        Web.requestFocus();
                                                        Web.setId("button");
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.RIGHT){
                                                        Web.requestFocus();
                                                        Web.setId("button");
                                                        event.consume();
                                                    }
                                                    */
                                                    if(event.getCode() == KeyCode.PAGE_DOWN){ // 2017.06.22 william hide web icon and button event
                                                        LanguageSelection.requestFocus();
                                                        // Web.setId("button");
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.DOWN){ // 2017.06.22 william hide web icon and button event
                                                        LanguageSelection.requestFocus();
                                                        // Web.setId("button");
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.TAB){ // 2017.06.22 william hide web icon and button event
                                                        LanguageSelection.requestFocus();
                                                        // Web.setId("button");
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.RIGHT){ // 2017.06.22 william hide web icon and button event
                                                        Reboot.requestFocus();
                                                        // Web.setId("button");
                                                        event.consume();
                                                    }
                                                    
                                                    if(event.getCode() == KeyCode.PAGE_UP){
                                                        ManageVD.requestFocus();
                                                        ManageVD.setId("button");
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.UP){
                                                        ManageVD.requestFocus();
                                                        ManageVD.setId("button");
                                                        event.consume();
                                                    }   
                                                    if(event.getCode() == KeyCode.LEFT){
                                                        ManageVD.requestFocus();
                                                        ManageVD.setId("button");
                                                        event.consume();
                                                    }
                                                });  
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                } 
            }
        });
        
        ChangePW.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if("Button_ChangePW".equals(ChangePW.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    ChangePW.setId("button");
                }
                
            } if (oldValue){                
                if("Button_ChangePW".equals(ChangePW.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    ChangePW.setId("button");
                }                             
            }
            //System.out.print("**Login ** : "+ Login.getId() +"\n");
            if(ChangePW.isHover()){                               
                //System.out.print("--Login.isHover()--\n");
                //System.out.print("**Login isHover ** : "+ Login.getId() +"\n");      
                ALLButtonColorChange();   // 所有button 顏色變化 效果
                
                // 2017.06.22 william hide web icon and button event
                if("button".equals(ChangePW.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ThinClient".equals(ThinClient.getId())){
                                if(!"Button_ManageVD".equals(ManageVD.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){              
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){  
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }
                /*** button 在第一次 Mouse-in 後 皆會直接進入 setOnMouseMoved ; 因此在mouse-in時加入條件  ***/
                /*** button 在第一次 Mouse-out 後 皆會直接進入 setOnMouseExited ; 因此在mouse-out時加入條件  ***/
                
                // 2017.06.22 william hide web icon and button event
                if("Button_ChangePW".equals(ChangePW.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ThinClient".equals(ThinClient.getId())){
                                if(!"Button_ManageVD".equals(ManageVD.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){  
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                                ChangePW.setOnMouseMoved((event)-> {
                                                    //System.out.print("** Moved 003 ** \n");
                                                    if(ChangePW_Color == true){
                                                        ChangePW.setId("button");
                                                    }
                                                });                                
                                                ChangePW.setOnMouseExited((event)-> {
                                                    //System.out.print("** Exited 001 ** \n"); 
                                                    tooltip_ChangePW.hide();
                                                    if(ChangePW_Color == true){
                                                        ChangePW.setId("Button_ChangePW"); 
                                                    }
                                                    //System.out.print("--Web.setOnMouseExited--::"+ Web.getId() +"\n");                       
                                                });  
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }               
            }
        });       
        
    }    
    // Input： ,  Output： , 功能： 滑鼠滑過關機button之動作 (-----目前無使用-----)
    public void Shutdownbuttonhover_focus(){  
        Shutdown.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if("Button_SShutdown".equals(Shutdown.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    Shutdown.setId("button");
                }
                
            } if (oldValue){                
                if("Button_SShutdown".equals(Shutdown.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    Shutdown.setId("button");
                }               
            }
            if(Shutdown.isFocused()){                
                //System.out.print("--Login.isFocused()--\n");
                //System.out.print("**Login isFocused** : "+ Login.getId() +"\n");                
                ALLButtonColorChange();  // 所有button 顏色變化 效果
                
                // 2017.06.22 william hide web icon and button event
                if("button".equals(Shutdown.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ThinClient".equals(ThinClient.getId())){
                                if(!"Button_ManageVD".equals(ManageVD.getId())){
                                    if(!"Button_ChangePW".equals(ChangePW.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){  
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){ 
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }
            }
        });
        
        Shutdown.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if("Button_SShutdown".equals(Shutdown.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    Shutdown.setId("button");
                }
                
            } if (oldValue){                
                if("Button_SShutdown".equals(Shutdown.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    Shutdown.setId("button");
                }                             
            }
            //System.out.print("**Login ** : "+ Login.getId() +"\n");
            if(Shutdown.isHover()){                               
                //System.out.print("--Login.isHover()--\n");
                //System.out.print("**Login isHover ** : "+ Login.getId() +"\n");  
                ALLButtonColorChange();  // 所有button 顏色變化 效果
                
                // 2017.06.22 william hide web icon and button event
                if("button".equals(Shutdown.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ThinClient".equals(ThinClient.getId())){
                                if(!"Button_ManageVD".equals(ManageVD.getId())){
                                    if(!"Button_ChangePW".equals(ChangePW.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){    
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){ 
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }
                /*** button 在第一次 Mouse-in 後 皆會直接進入 setOnMouseMoved ; 因此在mouse-in時加入條件  ***/
                /*** button 在第一次 Mouse-out 後 皆會直接進入 setOnMouseExited ; 因此在mouse-out時加入條件  ***/
                
                // 2017.06.22 william hide web icon and button event
                if("Button_SShutdown".equals(Shutdown.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ThinClient".equals(ThinClient.getId())){
                                if(!"Button_ManageVD".equals(ManageVD.getId())){
                                    if(!"Button_ChangePW".equals(ChangePW.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){ 
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                                Shutdown.setOnMouseMoved((event)-> {
                                                    //System.out.print("** Moved 003 ** \n");
                                                    if(Shutdown_Color == true){
                                                        Shutdown.setId("button");
                                                    }
                                                });                                
                                                Shutdown.setOnMouseExited((event)-> {
                                                    //System.out.print("** Exited 001 ** \n"); 
                                                    tooltip_Shutdown.hide();
                                                    if(Shutdown_Color == true){
                                                        Shutdown.setId("Button_SShutdown"); 
                                                    }
                                                    //System.out.print("--Web.setOnMouseExited--::"+ Web.getId() +"\n");                       
                                            }); 
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }               
            }
        });       
        
    }    
    // Input： ,  Output： , 功能： 滑鼠滑過重新開機button之動作 (-----目前無使用-----)
    public void Rebootbuttonhover_focus(){  
        Reboot.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if("Button_RReboot".equals(Reboot.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    Reboot.setId("button");
                }
                
            } if (oldValue){                
                if("Button_RReboot".equals(Reboot.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    Reboot.setId("button");
                }               
            }
            if(Reboot.isFocused()){                
                //System.out.print("--Login.isFocused()--\n");
                //System.out.print("**Login isFocused** : "+ Login.getId() +"\n");                
                ALLButtonColorChange();  // 所有button 顏色變化 效果
                
                // 2017.06.22 william hide web icon and button event
                if("button".equals(Reboot.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ThinClient".equals(ThinClient.getId())){
                                if(!"Button_ManageVD".equals(ManageVD.getId())){
                                    if(!"Button_ChangePW".equals(ChangePW.getId())){
                                        if(!"Button_SShutdown".equals(Shutdown.getId())){  
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){ 
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");  
                                                Shutdown.setId("button");
                                                TimeCalendar.setId("Time");
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }
            }
        });
        
        Reboot.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if("Button_RReboot".equals(Reboot.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    Reboot.setId("button");
                }
                
            } if (oldValue){                
                if("Button_RReboot".equals(Reboot.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    Reboot.setId("button");
                }                             
            }
            //System.out.print("**Login ** : "+ Login.getId() +"\n");
            if(Reboot.isHover()){                               
                //System.out.print("--Login.isHover()--\n");
                //System.out.print("**Login isHover ** : "+ Login.getId() +"\n");      
                ALLButtonColorChange();  // 所有button 顏色變化 效果
                
                // 2017.06.22 william hide web icon and button event
                if("button".equals(Reboot.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ThinClient".equals(ThinClient.getId())){
                                if(!"Button_ManageVD".equals(ManageVD.getId())){
                                    if(!"Button_ChangePW".equals(ChangePW.getId())){
                                        if(!"Button_SShutdown".equals(Shutdown.getId())){  
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){ 
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");  
                                                Shutdown.setId("button");
                                                TimeCalendar.setId("Time");
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }
                /*** button 在第一次 Mouse-in 後 皆會直接進入 setOnMouseMoved ; 因此在mouse-in時加入條件  ***/
                /*** button 在第一次 Mouse-out 後 皆會直接進入 setOnMouseExited ; 因此在mouse-out時加入條件  ***/
                
                // 2017.06.22 william hide web icon and button event
                if("Button_RReboot".equals(Reboot.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ThinClient".equals(ThinClient.getId())){
                                if(!"Button_ManageVD".equals(ManageVD.getId())){
                                    if(!"Button_ChangePW".equals(ChangePW.getId())){
                                        if(!"Button_SShutdown".equals(Shutdown.getId())){
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");  
                                                Shutdown.setId("button");
                                                TimeCalendar.setId("Time");
                                                Reboot.setOnMouseMoved((event)-> {
                                                    //System.out.print("** Moved 003 ** \n");
                                                    if(Reboot_Color == true){
                                                        Reboot.setId("button");
                                                    }
                                                });                                
                                                Reboot.setOnMouseExited((event)-> {
                                                    //System.out.print("** Exited 001 ** \n"); 
                                                    tooltip_Reboot.hide();
                                                    if(Reboot_Color == true){
                                                        Reboot.setId("Button_RReboot"); 
                                                    }
                                                    //System.out.print("--Web.setOnMouseExited--::"+ Web.getId() +"\n");                       
                                                });  
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }               
            }
        });       
        
    }    
    // Input： ,  Output： , 功能： 滑鼠滑過時間設定button之動作 (-----目前無使用-----)
    public void Timetogglebuttonhover_focus(){  
        TimeCalendar.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if("ToggleButton_time".equals(TimeCalendar.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    TimeCalendar.setId("Time");
                }
                
            } if (oldValue){                
                if("ToggleButton_time".equals(TimeCalendar.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    TimeCalendar.setId("Time");
                }               
            }
            if(TimeCalendar.isFocused()){                
                //System.out.print("--Login.isFocused()--\n");
                //System.out.print("**Login isFocused** : "+ Login.getId() +"\n");                
                ALLButtonColorChange();  // 所有button 顏色變化 效果
                
                // 2017.06.22 william hide web icon and button event
                if("Time".equals(TimeCalendar.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ThinClient".equals(ThinClient.getId())){
                                if(!"Button_ManageVD".equals(ManageVD.getId())){
                                    if(!"Button_ChangePW".equals(ChangePW.getId())){
                                        if(!"Button_SShutdown".equals(Shutdown.getId())){  
                                            if(!"Button_RReboot".equals(Reboot.getId())){  
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");  
                                                Shutdown.setId("button");
                                                Reboot.setId("button");
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }
                // 2017.06.08 william add choose icon function
                // 2017.06.22 william hide web icon and button event
                if("ToggleButton_time".equals(TimeCalendar.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ThinClient".equals(ThinClient.getId())){
                                if(!"Button_ManageVD".equals(ManageVD.getId())){
                                    if(!"Button_ChangePW".equals(ChangePW.getId())){
                                        if(!"Button_SShutdown".equals(Shutdown.getId())){
                                            if(!"Button_RReboot".equals(Reboot.getId())){ 
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");  
                                                Shutdown.setId("button");
                                                Reboot.setId("button");
                                                // TimeCalendar.setId("Time");
                                                TimeCalendar.setOnKeyPressed((event)-> {
                                                    tooltip_TimeCalendar.hide();
                                                    if(Time_Color == true){
                                                        TimeCalendar.setId("ToggleButton_time"); 
                                                    }
                                                    //event.get
                                                    if(event.getCode() == KeyCode.ENTER){
                                                        TimeCalendar.fire();
                                                        event.consume();
                                                    }
                                                    if(event.getCode() == KeyCode.RIGHT){
                                                        ThinClient.requestFocus();
                                                        ThinClient.setId("button");
                                                        event.consume();
                                                    } 
                                                    if(event.getCode() == KeyCode.DOWN){
                                                        ThinClient.requestFocus();
                                                        ThinClient.setId("button");
                                                        event.consume();
                                                    }             
                                                    if(event.getCode() == KeyCode.LEFT){
                                                        Shutdown.requestFocus();
                                                        event.consume();
                                                    }   
                                                    if(event.getCode() == KeyCode.UP){
                                                        Shutdown.requestFocus();
                                                        event.consume();
                                                    }                       
                                                });  
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }
                
            }
        });
        
        TimeCalendar.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if("ToggleButton_time".equals(TimeCalendar.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    TimeCalendar.setId("Time");
                }
                
            } if (oldValue){                
                if("ToggleButton_time".equals(TimeCalendar.getId())){
                    //Login.setId("Button_actionON");
                }else{
                    TimeCalendar.setId("Time");
                }                             
            }
            //System.out.print("**Login ** : "+ Login.getId() +"\n");
            if(TimeCalendar.isHover()){                               
                //System.out.print("--Login.isHover()--\n");
                //System.out.print("**Login isHover ** : "+ Login.getId() +"\n");      
                ALLButtonColorChange();  // 所有button 顏色變化 效果
                
                // 2017.06.22 william hide web icon and button event
                if("Time".equals(TimeCalendar.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ThinClient".equals(ThinClient.getId())){
                                if(!"Button_ManageVD".equals(ManageVD.getId())){
                                    if(!"Button_ChangePW".equals(ChangePW.getId())){
                                        if(!"Button_SShutdown".equals(Shutdown.getId())){  
                                            if(!"Button_RReboot".equals(Reboot.getId())){  
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");  
                                                Shutdown.setId("button");
                                                Reboot.setId("button");
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }
                /*** button 在第一次 Mouse-in 後 皆會直接進入 setOnMouseMoved ; 因此在mouse-in時加入條件  ***/
                /*** button 在第一次 Mouse-out 後 皆會直接進入 setOnMouseExited ; 因此在mouse-out時加入條件  ***/
                
                // 2017.06.22 william hide web icon and button event
                if("ToggleButton_time".equals(TimeCalendar.getId())){
                    if(!"Button_Login".equals(Login.getId())){
//                        if(!"Button_web".equals(Web.getId())){
                            if(!"Button_ThinClient".equals(ThinClient.getId())){
                                if(!"Button_ManageVD".equals(ManageVD.getId())){
                                    if(!"Button_ChangePW".equals(ChangePW.getId())){
                                        if(!"Button_SShutdown".equals(Shutdown.getId())){
                                            if(!"Button_RReboot".equals(Reboot.getId())){ 
                                                Login.setId("Loginbutton");
//                                                Web.setId("button");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");  
                                                Shutdown.setId("button");
                                                Reboot.setId("button");
                                                TimeCalendar.setOnMouseMoved((event)-> {
                                                    //System.out.print("** Moved 003 ** \n");
                                                    if(Time_Color == true){
                                                        TimeCalendar.setId("Time");
                                                    }
                                                });                                
                                                TimeCalendar.setOnMouseExited((event)-> {
                                                    //System.out.print("** Exited 001 ** \n"); 
                                                    tooltip_TimeCalendar.hide();
                                                    if(Time_Color == true){
                                                        TimeCalendar.setId("ToggleButton_time"); 
                                                    }
                                                    //System.out.print("--Web.setOnMouseExited--::"+ Web.getId() +"\n");                       
                                                });  
                                            }
                                        }
                                    } 
                                }    
                            }
//                        }
                    }
                }               
            }
        });       
        
    }    
    // Input： ,  Output： , 功能： 滑鼠滑過快照button之動作 (-----目前無使用-----)
    public void SSbuttonhover_focus(){  
        SS.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if("Button_SS".equals(SS.getId())) {

                } else {
                    SS.setId("button");
                }                
            } 
            
            if (oldValue){                
                if("Button_SS".equals(SS.getId())) {

                } else {
                    SS.setId("button");
                }               
            }
            if(SS.isFocused()) {                          
                ALLButtonColorChange();  // 所有button 顏色變化 效果
                
                if("button".equals(SS.getId())){
                    if(!"Button_Login".equals(Login.getId())){
                        if(!"Button_ThinClient".equals(ThinClient.getId())){
                            if(!"Button_ManageVD".equals(ManageVD.getId())){
                                if(!"Button_ChangePW".equals(ChangePW.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){   
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){   
                                                Login.setId("Loginbutton");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                            }
                                        }
                                    } 
                                }    
                            }
                        }
                    }
                }

                if("Button_SS".equals(SS.getId())){
                    if(!"Button_Login".equals(Login.getId())){
                        if(!"Button_ThinClient".equals(ThinClient.getId())){
                            if(!"Button_ManageVD".equals(ManageVD.getId())){
                                if(!"Button_ChangePW".equals(ChangePW.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){  
                                                Login.setId("Loginbutton");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                                // Web.setId("button");
                                                SS.setOnKeyPressed((event)-> {
                                                    tooltip_SS.hide();
                                                    if(SS_Color == true){
                                                        SS.setId("Button_SS"); 
                                                    }
                                                    //event.get
                                                    if(event.getCode() == KeyCode.ENTER){
                                                        SS.fire();
                                                        event.consume();
                                                    }
                                                });  
                                            }
                                        }
                                    } 
                                }    
                            }
                        }
                    }
                }
                
            }
        });
        
        SS.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if("Button_SS".equals(SS.getId())) {

                } else {
                    SS.setId("button");
                }
                
            } 
            
            if (oldValue) {                
                if("Button_SS".equals(SS.getId())){

                } else {
                    SS.setId("button");
                }                             
            }

            if(SS.isHover()){                               
                //System.out.print("--Login.isHover()--\n");
                //System.out.print("**Login isHover ** : "+ Login.getId() +"\n");
                ALLButtonColorChange();   // 所有button 顏色變化 效果         
 
                if("button".equals(SS.getId())){
                    if(!"Button_Login".equals(Login.getId())){
                        if(!"Button_ThinClient".equals(ThinClient.getId())){
                            if(!"Button_ManageVD".equals(ManageVD.getId())){
                                if(!"Button_ChangePW".equals(ChangePW.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){      
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){  
                                                Login.setId("Loginbutton");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                            }
                                        }
                                    } 
                                }    
                            }
                        }
                    }
                }
                /*** button 在第一次 Mouse-in 後 皆會直接進入 setOnMouseMoved ; 因此在mouse-in時加入條件  ***/
                /*** button 在第一次 Mouse-out 後 皆會直接進入 setOnMouseExited ; 因此在mouse-out時加入條件  ***/
                if("Button_SS".equals(SS.getId())){
                    if(!"Button_Login".equals(Login.getId())){
                        if(!"Button_ThinClient".equals(ThinClient.getId())){
                            if(!"Button_ManageVD".equals(ManageVD.getId())){
                                if(!"Button_ChangePW".equals(ChangePW.getId())){
                                    if(!"Button_SShutdown".equals(Shutdown.getId())){
                                        if(!"Button_RReboot".equals(Reboot.getId())){
                                            if(!"ToggleButton_time".equals(TimeCalendar.getId())){  
                                                Login.setId("Loginbutton");
                                                ThinClient.setId("button");
                                                ManageVD.setId("button");
                                                ChangePW.setId("button");
                                                Shutdown.setId("button");  
                                                Reboot.setId("button");
                                                TimeCalendar.setId("Time");
                                                SS.setOnMouseMoved((event)-> {
                                                    //System.out.print("** Moved 003 ** \n");
                                                    if(SS_Color == true){
                                                        SS.setId("button");
                                                    }
                                                });                                
                                                SS.setOnMouseExited((event)-> {
                                                    //System.out.print("** Exited 001 ** \n"); 
                                                    tooltip_SS.hide();
                                                    if(SS_Color == true){
                                                        SS.setId("Button_SS"); 
                                                    }
                                                    //System.out.print("--Web.setOnMouseExited--::"+ Web.getId() +"\n");                       
                                                });  
                                            }
                                        }
                                    } 
                                }    
                            }
                        }
                    }
                }               
            }
        });
    }            
    // Input： ,  Output： , 功能： 所有button 效果的顏色變化
    private void ALLButtonColorChange() {
        
        if("Button_Login".equals(Login.getId())){    
//            Web.setId("button"); // 2017.06.22 william hide web icon and button event
            ThinClient.setId("button");
            ManageVD.setId("button");
            ChangePW.setId("button");
            Shutdown.setId("button");  
            Reboot.setId("button");
            TimeCalendar.setId("Time");
            SS.setId("button"); // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        }
//        if("Button_web".equals(Web.getId())){ // 2017.06.22 william hide web icon and button event                  
//            Login.setId("Loginbutton");
//            ThinClient.setId("button");
//            ManageVD.setId("button");
//            ChangePW.setId("button");
//            Shutdown.setId("button");
//            Reboot.setId("button");
//            TimeCalendar.setId("Time");
//        }

        // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        if("Button_SS".equals(SS.getId())){ 
            Login.setId("Loginbutton");
            ThinClient.setId("button");
            ManageVD.setId("button");
            ChangePW.setId("button");
            Shutdown.setId("button");
            Reboot.setId("button");
            TimeCalendar.setId("Time");
        }

        if("Button_ThinClient".equals(ThinClient.getId())){                   
            Login.setId("Loginbutton");
//            Web.setId("button"); // 2017.06.22 william hide web icon and button event
            ManageVD.setId("button");
            ChangePW.setId("button");
            Shutdown.setId("button");
            Reboot.setId("button");
            TimeCalendar.setId("Time");
            SS.setId("button"); // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        }
        if("Button_ManageVD".equals(ManageVD.getId())){                   
            Login.setId("Loginbutton");
//            Web.setId("button"); // 2017.06.22 william hide web icon and button event
            ThinClient.setId("button");
            ChangePW.setId("button");
            Shutdown.setId("button");
            Reboot.setId("button");
            TimeCalendar.setId("Time");
            SS.setId("button"); // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        }
        if("Button_ChangePW".equals(ChangePW.getId())){                    
            Login.setId("Loginbutton");
//            Web.setId("button"); // 2017.06.22 william hide web icon and button event
            ThinClient.setId("button");
            ManageVD.setId("button");
            Shutdown.setId("button");
            Reboot.setId("button");
            TimeCalendar.setId("Time");
            SS.setId("button"); // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        }
        if("Button_SShutdown".equals(Shutdown.getId())){                  
            Login.setId("Loginbutton");
//            Web.setId("button"); // 2017.06.22 william hide web icon and button event
            ThinClient.setId("button");
            ManageVD.setId("button");
            ChangePW.setId("button");
            Reboot.setId("button");
            TimeCalendar.setId("Time");
            SS.setId("button"); // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        }
        if("Button_RReboot".equals(Reboot.getId())){                  
            Login.setId("Loginbutton");
//            Web.setId("button"); // 2017.06.22 william hide web icon and button event
            ThinClient.setId("button");
            ManageVD.setId("button");
            ChangePW.setId("button");
            Shutdown.setId("button");
            TimeCalendar.setId("Time");
            SS.setId("button"); // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        }
        if("ToggleButton_time".equals(TimeCalendar.getId())){                  
            Login.setId("Loginbutton");
//            Web.setId("button"); // 2017.06.22 william hide web icon and button event
            ThinClient.setId("button");
            ManageVD.setId("button");
            ChangePW.setId("button");
            Shutdown.setId("button");
            Reboot.setId("button");
            SS.setId("button"); // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        }
        
    }    
    // Input： ,  Output： , 功能： 重設登入button之ID與屬性 (-----目前無使用-----)
    private void updateLogin() { 
        //System.out.print("******** IN Login ********\n");
        Login_Color = true;
        Web_Color = false;
        ThinClient_Color = false;
        ManageVD_Color = false;
        ChangePW_Color = false;
        Shutdown_Color = false;
        Reboot_Color = false;
        Time_Color=false;
        SS_Color = false; // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        if(Login_Color == true){
            Login.setId("Button_Login");
//            Web.setId("button"); // 2017.06.22 william hide web icon and button event
            ThinClient.setId("button");
            ManageVD.setId("button");
            ChangePW.setId("button");
            Shutdown.setId("button");
            Reboot.setId("button");
            TimeCalendar.setId("Time");
            SS.setId("button"); // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        }  
        //System.out.print("******** OFF Login ********\n");     
    }    
    // Input： ,  Output： , 功能： 重設快照button之ID與屬性 (-----目前無使用-----)
    private void updateSS() { 

        Login_Color = false;
        SS_Color = true;
        ThinClient_Color = false;
        ManageVD_Color = false;
        ChangePW_Color = false;
        Shutdown_Color = false;
        Reboot_Color = false;
        Time_Color=false;
        if(SS_Color == true){
            Login.setId("Loginbutton");
            SS.setId("Button_SS");
            ThinClient.setId("button");
            ManageVD.setId("button");
            ChangePW.setId("button");
            Shutdown.setId("button");
            Reboot.setId("button");
            TimeCalendar.setId("Time");
        }  
        //System.out.print("******** OFF Web ********\n");     
    }    
    // Input： ,  Output： , 功能： 重設Web button之ID與屬性 (-----目前無使用-----)    
    private void updateWeb() { 
        //System.out.print("******** IN Web ********\n");
        Login_Color = false;
        Web_Color = true;
        ThinClient_Color = false;
        ManageVD_Color = false;
        ChangePW_Color = false;
        Shutdown_Color = false;
        Reboot_Color = false;
        Time_Color=false;
        if(Web_Color == true){
            Login.setId("Loginbutton");
            Web.setId("Button_web");
            ThinClient.setId("button");
            ManageVD.setId("button");
            ChangePW.setId("button");
            Shutdown.setId("button");
            Reboot.setId("button");
            TimeCalendar.setId("Time");
        }  
        //System.out.print("******** OFF Web ********\n");     
    }
    // Input： ,  Output： , 功能： 重設瘦客戶機設定button之ID與屬性 (-----目前無使用-----)    
    private void updateThinClient() { 
        //System.out.print("******** IN ThinClient ********\n");
        Login_Color = false;
        Web_Color = false;
        ThinClient_Color = true;
        ManageVD_Color = false;
        ChangePW_Color = false;
        Shutdown_Color = false;
        Reboot_Color = false;
        Time_Color=false;
        SS_Color = false; // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        if(ThinClient_Color == true){
            Login.setId("Loginbutton");
//            Web.setId("button"); // 2017.06.22 william hide web icon and button event
            ThinClient.setId("Button_ThinClient");
            ManageVD.setId("button");
            ChangePW.setId("button");
            Shutdown.setId("button");
            Reboot.setId("button");
            TimeCalendar.setId("Time");
            SS.setId("button"); // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        }  
        //System.out.print("******** OFF ThinClient ********\n");     
    }
    // Input： ,  Output： , 功能： 重設雲桌面管理button之ID與屬性 (-----目前無使用-----) 
    private void updateManageVD() { 
        //System.out.print("******** IN ManageVD ********\n");
        Login_Color = false;
        Web_Color = false;
        ThinClient_Color = false;
        ManageVD_Color = true;
        ChangePW_Color = false;
        Shutdown_Color = false;
        Reboot_Color = false;
        Time_Color=false;
        SS_Color = false; // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        if(ManageVD_Color == true){
            Login.setId("Loginbutton");
//            Web.setId("button"); // 2017.06.22 william hide web icon and button event
            ThinClient.setId("button");
            ManageVD.setId("Button_ManageVD");
            ChangePW.setId("button");
            Shutdown.setId("button");
            Reboot.setId("button");
            TimeCalendar.setId("Time");
            SS.setId("button"); // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        }  
        //System.out.print("******** OFF ManageVD ********\n");     
    }
   // Input： ,  Output： , 功能： 重設修改密碼button之ID與屬性 (-----目前無使用-----) 
    private void updateChangePW() { 
        //System.out.print("******** IN ChangePW ********\n");
        Login_Color = false;
        Web_Color = false;
        ThinClient_Color = false;
        ManageVD_Color = false;
        ChangePW_Color = true;
        Shutdown_Color = false;
        Reboot_Color = false;
        Time_Color=false;
        SS_Color = false; // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        if(ChangePW_Color == true){
            Login.setId("Loginbutton");
//            Web.setId("button"); // 2017.06.22 william hide web icon and button event
            ThinClient.setId("button");
            ManageVD.setId("button");
            ChangePW.setId("Button_ChangePW");
            Shutdown.setId("button");
            Reboot.setId("button");
            TimeCalendar.setId("Time");
            SS.setId("button"); // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        }  
        //System.out.print("******** OFF ChangePW ********\n");     
    }
   // Input： ,  Output： , 功能： 重設關機button之ID與屬性 (-----目前無使用-----)  
    private void updateShutdown() { 
        //System.out.print("******** IN Shutdown ********\n");
        Login_Color = false;
        Web_Color = false;
        ThinClient_Color = false;
        ManageVD_Color = false;
        ChangePW_Color = false;
        Shutdown_Color = true;
        Reboot_Color = false;
        Time_Color=false;
        SS_Color = false; // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        if(Shutdown_Color == true){
            Login.setId("Loginbutton");
//            Web.setId("button"); // 2017.06.22 william hide web icon and button event
            ThinClient.setId("button");
            ManageVD.setId("button");
            ChangePW.setId("button");
            Shutdown.setId("Button_SShutdown");
            Reboot.setId("button");
            TimeCalendar.setId("Time");     
            SS.setId("button"); // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        }  
        //System.out.print("******** OFF Shutdown ********\n");     
    }
   // Input： ,  Output： , 功能： 重設重新開機button之ID與屬性 (-----目前無使用-----)   
    private void updateReboot() { 
        //System.out.print("******** IN Reboot ********\n");
        Login_Color = false;
        Web_Color = false;
        ThinClient_Color = false;
        ManageVD_Color = false;
        ChangePW_Color = false;
        Shutdown_Color = false;
        Reboot_Color = true;
        Time_Color=false;
        SS_Color = false; // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        if(Reboot_Color == true){
            Login.setId("Loginbutton");
//            Web.setId("button"); // 2017.06.22 william hide web icon and button event
            ThinClient.setId("button");
            ManageVD.setId("button");
            ChangePW.setId("button");
            Shutdown.setId("button");
            Reboot.setId("Button_RReboot");
            TimeCalendar.setId("Time"); 
            SS.setId("button"); // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        }  
        //System.out.print("******** OFF Reboot ********\n");     
    }
   // Input： ,  Output： , 功能： 重設時間設定button之ID與屬性 (-----目前無使用-----)    
    private void updateTime() { 
        //System.out.print("******** IN Reboot ********\n");
        Login_Color = false;
        Web_Color = false;
        ThinClient_Color = false;
        ManageVD_Color = false;
        ChangePW_Color = false;
        Shutdown_Color = false;
        Reboot_Color = false;
        Time_Color=true;
        SS_Color = false; // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        if(Time_Color == true){
            Login.setId("Loginbutton");
//            Web.setId("button"); // 2017.06.22 william hide web icon and button event
            ThinClient.setId("button");
            ManageVD.setId("button");
            ChangePW.setId("button");
            Shutdown.setId("button");
            Reboot.setId("button");
            TimeCalendar.setId("ToggleButton_time"); 
            SS.setId("button"); // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
        }  
        //System.out.print("******** OFF Reboot ********\n");     
    }    
    // Input： ,  Output： , 功能： 初始化button之ID
    public void Init_Color_id() {        
        Login.setId("Button_Normal");
        ThinClient.setId("Icon_Normal");
        ManageVD.setId("Icon_Normal");
        ChangePW.setId("Icon_Normal");
        SS.setId("Icon_Normal"); 
        Shutdown.setId("Icon_Normal");
        Reboot.setId("Icon_Normal");
        TimeCalendar.setId("Time_Normal");  
        
        if(wifi_enable)
            Connect_ON_OFF.setId("Icon_Normal");
        
        if(battery_enable) {
            Battery.setId("Icon_Normal");
            Brightness.setId("Icon_Normal");        
        }

    }
    // Input： ,  Output： , 功能： 登入按鈕在hover和focus的ID與屬性設定(不包含其他按鈕)
    public void Login_Btn_hover_focus() {
//        System.out.println("-----------Login_Btn_hover_focus--------------");
        ClickColor = "";
        Init_Color_id();
        Login.setId("Button_ColorChange");
        ClickColor = "Login";        
        
        Login.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if(ClickColor.contains("Login")) {
                    Init_Color_id();
                }                
            } else {
                if(ClickColor.contains("Login")) {
                    Login.setId("Button_ColorChange");
                }
            }            
        });
        
        Login.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if(ClickColor.contains("Login")) {
                    Init_Color_id();
                }     
            } else {
                if(ClickColor.contains("Login")) {
                    Login.setId("Button_ColorChange");
                }            
            }
        });      
    }
    // Input： ,  Output： , 功能： 按鈕在hover和focus的ID與屬性設定(不包含登入按鈕與時間設定按鈕)
    public void All_Btn_hover_focus(Button btn, String select) {  
//        System.out.println("-----------All_Btn_hover_focus--------------");
        ClickColor = "";
        Init_Color_id();
        btn.setId("Icon_ColorChange");
        ClickColor = select;        
        
        btn.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if(ClickColor.contains(select)) {
                    Init_Color_id();
                }                
            } else {
                if(ClickColor.contains(select)) {
                    btn.setId("Icon_ColorChange");
                }
            }            
        });
        
        btn.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if(ClickColor.contains(select)) {
                    Init_Color_id();
                }     
            } else {
                if(ClickColor.contains(select)) {
                    btn.setId("Icon_ColorChange");
                }            
            }
        });       
        
    }    
    // Input： ,  Output： , 功能： 時間設定按鈕在hover和focus的ID與屬性設定(不包含其他按鈕)
     public void Time_Btn_hover_focus() {
//         System.out.println("-----------Time_Btn_hover_focus--------------");
        ClickColor = "";
        Init_Color_id();
        TimeCalendar.setId("Time_ColorChange");
        ClickColor = "TimeCalendar";        
        
        TimeCalendar.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if(ClickColor.contains("TimeCalendar")) {
                    Init_Color_id();
                }                
            } else {
                if(ClickColor.contains("TimeCalendar")) {
                    TimeCalendar.setId("Time_ColorChange");
                }
            }            
        });
        
        TimeCalendar.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if(ClickColor.contains("TimeCalendar")) {
                    Init_Color_id();
                }     
            } else {
                if(ClickColor.contains("TimeCalendar")) {
                    TimeCalendar.setId("Time_ColorChange");
                }            
            }
        });      
    }        
   // Input： ,  Output： , 功能： 檢察是否有2個螢幕 (-----目前無使用-----)     
    private void ChecktwoDispaly() throws IOException, ParseException {
        /********** Display 01 & 02 : 螢幕名稱;目前解析度 ***********/
        Display02_name = "0";
        Display01_name = null;
        
        Process Display01_name_process =Runtime.getRuntime().exec("xrandr");
        try (BufferedReader output_Display0102_name = new BufferedReader (new InputStreamReader(Display01_name_process.getInputStream()))) {
            String line_Display0102 = output_Display0102_name.readLine();
            String[] Display01_name_Line_list =null;           
      
            while(line_Display0102 != null){
                
                if ( line_Display0102.contains("connected") && !line_Display0102.contains("disconnected")  ){  //startsWith("VGA1") .contains("connected")
                    
                    if(Display01_name_Line_list == null){
                        
                        Display01_name_Line_list =line_Display0102.split(" ");                        
                        Display01_name= Display01_name_Line_list[0];
                        System.out.println(" Display01_name  : " + Display01_name +"\n");                        
                        
                    }else{
                        
                        String[] Display02_name_Line_list =line_Display0102.split(" ");
                        Display02_name= Display02_name_Line_list[0];
                        System.out.println(" Display02_name  : " + Display02_name +"\n");
                    }
                }
                line_Display0102 = output_Display0102_name.readLine();  //.trim()
                //System.out.println("*/*/ line_Display0102 */*/-  : " + line_Display0102 +"\n");
            }
            output_Display0102_name.close();
        }
        
//        if( Display02_name.equals("0") ){
//            Main_Dispaly01_change=true;
//            Main_Dispaly02_change=false;
//            
//        }     
//        
//        if( !Display02_name.equals("0") ){
//            //LoadDispalyChange();
//        }
        
        if( Display01_name.contains("HDMI") && !Display02_name.equals("0") ){

            //透過xrandr設定主螢幕，指令下完結果馬上就會生效了   $ xrandr --output HDMI1 --primary           
            String HDMI_Dispaly_primary = "xrandr --output "+ Display01_name  +" --primary" ;
            Process HDMI_Dispaly_primary_process =Runtime.getRuntime().exec(HDMI_Dispaly_primary);    
            System.out.println(" ** Display01_name = HDMI ** HDMI_Dispaly_primary  : " + HDMI_Dispaly_primary +"\n");
            
            String HDMI_Dispaly_left = "xrandr --output "+ Display01_name  +" --left-of "+ Display02_name;
            Process HDMI_Dispaly_left_process =Runtime.getRuntime().exec(HDMI_Dispaly_left); 
            System.out.println(" ** Display01_name = HDMI ** HDMI_Dispaly_left  : " + HDMI_Dispaly_left +"\n");

//            ChecktwoDispaly_Load();
            
        }        
        
        if( !Display01_name.contains("HDMI") && !Display02_name.equals("0") && Display02_name.contains("HDMI")){
              
            //透過xrandr設定主螢幕，指令下完結果馬上就會生效了   $ xrandr --output HDMI1 --primary           
            String HDMI_Dispaly_primary = "xrandr --output "+ Display02_name  +" --primary" ;
            Process HDMI_Dispaly_primary_process =Runtime.getRuntime().exec(HDMI_Dispaly_primary);    
            System.out.println(" ** Display02_name = HDMI ** HDMI_Dispaly_primary  : " + HDMI_Dispaly_primary +"\n");
            
            String HDMI_Dispaly_left = "xrandr --output "+ Display02_name  +" --left-of "+ Display01_name;
            Process HDMI_Dispaly_left_process =Runtime.getRuntime().exec(HDMI_Dispaly_left); 
            System.out.println(" ** Display02_name = HDMI ** HDMI_Dispaly_left  : " + HDMI_Dispaly_left +"\n");
            
//            ChecktwoDispaly_Load();
            
        }
        
    }
   // Input： ,  Output： , 功能： 檢察是否有接上第2個螢幕 (-----目前無使用-----)      
    private void ChecktwoDispaly_Load() throws IOException {
        
        if( Main_Dispaly01_change == true && Main_Dispaly01_change == false ){
            
            String HDMI_Dispaly_left = "xrandr --output "+ Display01_name  +" --left-of "+ Display02_name;
            Process HDMI_Dispaly_left_process =Runtime.getRuntime().exec(HDMI_Dispaly_left); 
            System.out.println(" ** Display01_name = HDMI ** HDMI_Dispaly_left  : " + HDMI_Dispaly_left +"\n");
            
        }
        if( Main_Dispaly01_change == false && Main_Dispaly01_change == true ){
            
            String HDMI_Dispaly_left = "xrandr --output "+ Display02_name  +" --left-of "+ Display01_name;
            Process HDMI_Dispaly_left_process =Runtime.getRuntime().exec(HDMI_Dispaly_left); 
            System.out.println(" ** Display02_name = HDMI ** HDMI_Dispaly_left  : " + HDMI_Dispaly_left +"\n");

        }

    }    
   // Input： ,  Output： , 功能： 讀取雙螢幕的主螢幕 (-----目前無使用-----) 
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
                D01_change=DispalyChange.get("Dispaly01_change").toString();               
                D02_change=DispalyChange.get("Dispaly02_change").toString(); 
                
                System.out.print("D01_change-Load : "+D01_change+"\n");  
                System.out.print("D02_change-Load : "+D02_change+"\n");  
                
            }else{
                D01_change="true";
                D02_change="false";
                System.out.print("D01_change-else-Load : "+D01_change+"\n");  
                System.out.print("D02_change-else-Load : "+D02_change+"\n");  
            }
        }catch(IOException e){
          
        }
    }    
    // Input： ,  Output： , 功能： 讀取系統目前的時間值
    private void GetCurrentDateandTime() throws IOException {
        
        MainCurrentDT_process = Runtime.getRuntime().exec( "timedatectl" );//./date.sh set tz Asia/Tokyo
        try (BufferedReader output_CurrentDT = new BufferedReader (new InputStreamReader(MainCurrentDT_process.getInputStream()))) {
            String line_CurrentDT = output_CurrentDT.readLine();
            //System.out.println(" line_CurrentDT  : " + line_CurrentDT +"\n");
            while(line_CurrentDT != null){  

                if ( line_CurrentDT.contains("Local time") ==true ){

                    String[] CurrentDT_Line_list =line_CurrentDT.split(" ");
                    MainCurrentDate = CurrentDT_Line_list[9];              
                    MainCurrentTime = CurrentDT_Line_list[10]; 

                    break;
                }
                line_CurrentDT = output_CurrentDT.readLine();
                //System.out.println(" line_Zoneid  : " + line_Zoneid +"\n");
            }
            output_CurrentDT.close();
        }

//            MainCurrentDT_process = Runtime.getRuntime().exec( "date" );//./date.sh set tz Asia/Tokyo
//            try (BufferedReader output_CurrentDT = new BufferedReader (new InputStreamReader(MainCurrentDT_process.getInputStream()))) {
//                String line_CurrentDT = output_CurrentDT.readLine();
//                //System.out.println(" line_CurrentTime  : " + line_CurrentTime +"\n");
//                String[] CurrentDT_Line_list =line_CurrentDT.split(" ");                           
//                /** get 年/月/日 **/
//                MainCurrentDate_Mounth_Eng = CurrentDT_Line_list[1];                
//                MainCurrentDate_Day = CurrentDT_Line_list[2];    
//                //若 Day為個位數 則 CurrentDT_Line_list[2] 抓的是" "(空格) , 因此若為個位數 將另外抓取 MainCurrentDate_Day; MainCurrentDate_Year ; MainCurrentTime
//                if(MainCurrentDate_Day.isEmpty()){
//                    
//                    MainCurrentDate_Year = CurrentDT_Line_list[6];
//                    MainCurrentDate_Day = CurrentDT_Line_list[3];
//                    //System.out.println(" MainCurrentDate_Day =null  : " + MainCurrentDate_Day +"\n");
//                    MainCurrentTime = CurrentDT_Line_list[4];  
//                    //System.out.println(" MainCurrentTime  : " + MainCurrentTime +"\n");
//                    
//                }else{
//                    
//                    MainCurrentDate_Year = CurrentDT_Line_list[5];
//                    MainCurrentTime = CurrentDT_Line_list[3]; 
//                    //System.out.println(" MainCurrentTime  : " + MainCurrentTime +"\n");
//                    
//                }
//                //若 Day為個位數 則將 Day前面加 "0" -> 變成雙位數
//                int intV = Integer.valueOf(MainCurrentDate_Day);                
//                if(intV <= 9){
//                    MainCurrentDate_Day = "0"+intV;                   
//                }
//                
//                output_CurrentDT.close();
//            }
            
            /** get 月 -> Number **/
//            if( MainCurrentDate_Mounth_Eng.equals("Jan") ){
//                MainCurrentDate_Mounth = "01";
//            }
//            if( MainCurrentDate_Mounth_Eng.equals("Feb") ){
//                MainCurrentDate_Mounth = "02";
//            }
//            if( MainCurrentDate_Mounth_Eng.equals("Mar") ){
//                MainCurrentDate_Mounth = "03";
//            }
//            if( MainCurrentDate_Mounth_Eng.equals("Apr") ){
//                MainCurrentDate_Mounth = "04";
//            }
//            if( MainCurrentDate_Mounth_Eng.equals("May") ){
//                MainCurrentDate_Mounth = "05";
//            }
//            if( MainCurrentDate_Mounth_Eng.equals("Jun") ){
//                MainCurrentDate_Mounth = "06";
//            }
//            if( MainCurrentDate_Mounth_Eng.equals("Jul") ){
//                MainCurrentDate_Mounth = "07";
//            }
//            if( MainCurrentDate_Mounth_Eng.equals("Aug") ){
//                MainCurrentDate_Mounth = "08";
//            }
//            if( MainCurrentDate_Mounth_Eng.equals("Sep") ){
//                MainCurrentDate_Mounth = "09";
//            }
//            if( MainCurrentDate_Mounth_Eng.equals("Oct") ){
//                MainCurrentDate_Mounth = "10";
//            }
//            if( MainCurrentDate_Mounth_Eng.equals("Nov") ){
//                MainCurrentDate_Mounth = "11";
//            }
//            if( MainCurrentDate_Mounth_Eng.equals("Dec") ){
//                MainCurrentDate_Mounth = "12";
//            }
//            
//            MainCurrentDate = MainCurrentDate_Year +"-"+MainCurrentDate_Mounth+"-"+MainCurrentDate_Day ;

            /** get 年/月/日 **/
            String[] Date_Line_list =MainCurrentDate.split("-");
            MainCurrentDate_Year = Date_Line_list[0];
            MainCurrentDate_Mounth = Date_Line_list[1];
            MainCurrentDate_Day = Date_Line_list[2];
            
            /** get 時/分/秒 **/
            String[] Time_Line_list =MainCurrentTime.split(":");
            MainCurrentTime_Hour = Time_Line_list[0];
            MainCurrentTime_Min = Time_Line_list[1];
            MainCurrentTime_Sec = Time_Line_list[2];

    }
    // Input： ,  Output： , 功能： 更新系統目前的時間值(class)
    public class UpdateDateandTime implements Runnable {  //  private void UpdateDateandTime() // 2017.06.16 william 使用Java自動化執行工作-時間   
        
       // timer=new Timer(); 
       // TimerTask task=new TimerTask() {
            @Override
            public void run() {                    
//                LocalDateTime currentDateTime = LocalDateTime.now();
//                int year = currentDateTime.getYear();
//                int month = currentDateTime.getMonthValue();
//                Month m = currentDateTime.getMonth();
//                int day = currentDateTime.getDayOfMonth();
//                DayOfWeek w = currentDateTime.getDayOfWeek();
//                int hour = currentDateTime.getHour();
//                int minute = currentDateTime.getMinute();
//                int second = currentDateTime.getSecond();       
//                time_y_m_d=year+" / "+month+" / "+day;
//                time_h_m= hour +"："+ minute +"："+ second;
                if(!GB.lock_AutoTimeRenew){ // 2017.06.19 william Auto updata time action lock
                    try {
                        GetCurrentDateandTime(); // get current Date and Time
                        time_y_m_d = MainCurrentDate_Year+" / "+MainCurrentDate_Mounth+" / "+MainCurrentDate_Day;
                        time_h_m = "  " +MainCurrentTime_Hour +" : "+ MainCurrentTime_Min +" : "+ MainCurrentTime_Sec;
                    } 
                    catch (IOException ex) {
                        Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                    }
               
                    Platform.runLater(() -> { 

                        TimeCalendar.setText(time_y_m_d + "\n " + time_h_m);                   
                    
                    });
                }
            }
       // };
       // timer.schedule(task, 1, 1000);  
        
    }    
    // Input： ,  Output： procStartInfo(process), 功能： 取得bind mac address
    public Process GetPSICheckSN() throws IOException {
        String command = "ifconfig $(ifconfig | grep eth* | awk '/eth/{print $1}') | sed -n 1p | awk '{print $5}'|md5sum -t|awk '{print $1}'";           
        Process procStartInfo = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",command});

        return procStartInfo;    
    }
    // Input： ,  Output： False/True (boolean), 功能： 比對.key檔案，內容相異為False，相同為True
    private boolean checkSN() {
        try {
            Process _HostProcess = GetPSICheckSN();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(_HostProcess.getInputStream()));
            returnResult = reader.readLine();
//            while ((returnResult = reader.readLine()) != null) {
//                System.out.println("----- returnResult:" + returnResult + "-----\n");
//            }

            File fileToRead = new File("/root/.config/.key");
            
            if(fileToRead.exists()) {
                String FilePath = fileToRead.getPath();
                List<String> CONFIGLINE = Files.readAllLines(Paths.get(FilePath));
                fs = "";
                fs = CONFIGLINE.stream().map((s) -> s).reduce(fs, String::concat).trim(); // 加上trim()去掉前後字串空白
//                System.out.println("----- fs:" + fs + "-----\n");
            }
        }
        catch(IOException e) {
//            e.printStackTrace();
        }

        return fs.equals(returnResult) ? true : false;
    }    
    // Input：1. 是否為快照(boolean), 2. 是否為檢視D槽(boolean),  Output：  , 功能： VD登入及快照登入連線函式 
    public void Login_func(boolean IsSnapshot, boolean IsViewDrive) {
        
        // 2019.03.28 VM/VD migration reconnect
        StopMigrationLock();
        clearMigrationParameter();
                    
        //System.out.println("** lock_Login ** : "+lock_Login+"\n");
        if(lock_Login == false) {
            lock_Login = true;
            // Login.setDisable(true);
            // 檢查IP
            if(IP_Addr.getText() != null) {
                CurrentIP = IP_Addr.getText().trim();
            } else {
                CurrentIP = ""; 
            }   
            // 檢查使用者名稱 
            /*
            if(userTextField.getText()!=null){
                CurrentUserName = userTextField.getText();
            } else {
                CurrentUserName = "";              
            }
            */
            
            // 2018.12.25 Remember10 username            
            if(!"".equals(UsernameComboBox.getValue().toString())) {
                CurrentUserName = UsernameComboBox.getValue().toString();
            } else {
                CurrentUserName = "";              
            }    
            
            WriteLastStatus();
            WriteLastChange(); // 記錄明碼與暗碼
            // 檢查密碼 
            if(pwBox.getText() != null) {
                CurrentPasseard = pwBox.getText();             
            } else {
                CurrentPasseard = "";
            }

            if(IsSnapshot) {
//                updateSS(); // 2018.01.23 william 新增快照Tooltip提示框及按鈕變色
//                ClickColor = "";
//                Init_Color_id();
//                SS.setId("Icon_ColorChange");
//                ClickColor = "SS";
All_Btn_hover_focus(SS, "SS");
                
                // 2019.01.02 view D Drive
                GB.IsViewDisk = true; 
                GB._viewDiskVDName = "";
            } else {
//                updateLogin();

                GB.IsViewDisk = false; // 2019.01.02 view D Drive
Login_Btn_hover_focus();
            }                                    
                
            try {
                Process arp_ip_process = Runtime.getRuntime().exec("arp -d " + CurrentIP); // 2017.06.05 william add arp cache clear
            } catch (IOException ex) {
                Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            /*** 2017.09.13 william 登入中thread畫面鎖住 Start  ***/       
            LoginGP = new GridPane();        
            //MountGP.setGridLinesVisible(true);          // 開啟或關閉表格的分隔線       
            LoginGP.setAlignment(Pos.BASELINE_CENTER);       
            //MountGP.setPadding(new Insets(0, 0, 0, 0)); // 設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
            LoginGP.setHgap(5);                           // 元件間的水平距離
            LoginGP.setVgap(5);                           // 元件間的垂直距離
            LoginGP.setPrefSize(530, 220);                // 400, 480
            LoginGP.setMaxSize(530, 220);
            LoginGP.setMinSize(530, 220);
            LoginGP.setTranslateY(45);
            LoginGP.setStyle("-fx-background-color: #EEF5F5 ; -fx-opacity: 0.7 ; -fx-background-radius: 4;-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 10, 0.0 , 0 , 5 );");    // 2018.18.18 new create & reborn 
            
            // 2019.05.13 單一VD登入UI顯示            
            final int numCols = 1 ;
            final int numRows = 3 ;
            for (int i = 0; i < numCols; i++) {
                ColumnConstraints colConst = new ColumnConstraints();
                colConst.setPercentWidth(100.0 / numCols);
                LoginGP.getColumnConstraints().add(colConst);
            }
            for (int i = 0; i < numRows; i++) {
                RowConstraints rowConst = new RowConstraints();
                rowConst.setPercentHeight(100.0 / numRows);
                LoginGP.getRowConstraints().add(rowConst);         
            }                
            
            Login_title = new Label(""); 
            Login_title2 = new Label(WordMap.get("Thread_Waiting"));            
            Login_title3 = new Label("");
            
            if("English".equals(LangNow)){
                Login_title.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 26px; -fx-font-weight: 900;");//21
                Login_title2.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 26px; -fx-font-weight: 900;");
                Login_title3.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 26px; -fx-font-weight: 900;");            
            } else {
                Login_title.setStyle("-fx-font-family: 'Droid Sans Fallback'; -fx-font-size: 26px; -fx-font-weight: 900;");//20           
                Login_title2.setStyle("-fx-font-family: 'Droid Sans Fallback'; -fx-font-size: 26px; -fx-font-weight: 900;");
                Login_title3.setStyle("-fx-font-family: 'Droid Sans Fallback'; -fx-font-size: 26px; -fx-font-weight: 900;");

            }            
                                               
            Login_title.setAlignment(Pos.CENTER);   
            Login_title.setTranslateX(-7);
            
            Login_title2.setAlignment(Pos.CENTER);        
            Login_title3.setAlignment(Pos.CENTER);        
            Login_title.setId("Thread_status");
            Login_title2.setId("Thread_status");
            Login_title3.setId("Thread_status");
        
            ProgressIndicator p1 = new ProgressIndicator();

            HBox FirstRow = new HBox();
            FirstRow.setAlignment(Pos.CENTER);
            FirstRow.setTranslateY(5); 
            FirstRow.getChildren().addAll(Login_title);
            LoginGP.add(FirstRow, 0, 0);        

            HBox S1 = new HBox();
            S1.setAlignment(Pos.CENTER);    
            S1.getChildren().addAll(Login_title2);

            HBox S2 = new HBox();                
            S2.setAlignment(Pos.CENTER_LEFT);
            S2.getChildren().addAll(Login_title3);
            S2.setPrefWidth(20);
            S2.setMaxWidth(20);
            S2.setMinWidth(20);

            HBox SecondRow = new HBox();
            SecondRow.setAlignment(Pos.CENTER);
            SecondRow.setSpacing(0);        
            SecondRow.getChildren().addAll(S1, S2);
            LoginGP.add(SecondRow, 0, 1);        

            HBox ThirdRow = new HBox();
            ThirdRow.setAlignment(Pos.CENTER);
            ThirdRow.getChildren().addAll(p1);
            LoginGP.add(ThirdRow, 0, 2);                
            
            /*
            Login_title = new Label(WordMap.get("Thread_Login")+"\n"+WordMap.get("Thread_Waiting"));
            Login_title.setAlignment(Pos.CENTER);
            Login_title.setTranslateY(40);
            Login_title.setId("Thread_status");
            
            LoginGP.add(Login_title, 0, 0);               // 2,1是指這個元件要佔用2格的column和1格的row

            ProgressIndicator p1 = new ProgressIndicator();
            p1.setTranslateY(50);
            //p1.setStyle("-fx-foreground-color: #1A237E");
            LoginGP.add(p1, 0, 1);                        // 2,1是指這個元件要佔用2格的column和1格的row 
            */    
            StackPane stack = new StackPane();
            stack.getChildren().addAll(MainGP, LoginGP);          
            
            StartLoginLock(); // 2019.05.13 單一VD登入UI顯示
            /*--------------------------------------------------------------------------------------*/
            /*--------------------------------------------------------------------------------------*/
            Boolean Result = PATTERN.matcher(CurrentIP).matches(); // 檢查IP格式
            // System.out.println(" -----Result------"+Result+"\n");
            
            if(Result == true || IP_Addr.getText() != null) { // 2017.07.19 william IP use Domain Name
                rootPane.setCenter(stack);                // 2017.09.13 william 登入中thread畫面鎖住   
                
                Task<Void> longRunningTask = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        IsSnapShot = IsSnapshot;         // 2017.12.08 william SnapShot實作
                        //updateLogin();
                        
                        // 2017.08.10 william IP增加port欄位，預設443
                        if((IP_Port.getText() == null) || ("".equals(IP_Port.getText()))) {
                                CurrentIP_Port = "443";
                        } else {
                                CurrentIP_Port = IP_Port.getText();
                        }  
                        // 2017.09.18 william 錯誤修改(1)-IP Port例外處理
                        CheckPort = Integer.parseInt(CurrentIP_Port);
                        if(!(CheckPort >= 0 && CheckPort <= 65535)) {
                            System.out.println("**IP PORT輸入錯誤 ** \n");
                            QM.testError = false;
                        }                            
                        QM.Ping(IP_Addr.getText(), CurrentIP_Port);                                                                                                 
                        QM.QueryLogin(public_stage, IP_Addr.getText(), CurrentUserName , pwBox.getText(), uniqueKey.toString(), CurrentIP_Port, IsSnapShot, IsViewDrive);  // 2018.12.25 Remember10 username userTextField.getText() // 2019.01.02 view D Drive
                        
                        // QM.Dual_Screen(IP_Addr.getText(), CurrentIP_Port); // 2018.01.30 william 雙螢幕實作 -> 2018.08.22 雙螢幕設定要放在Power On 之前 
                        return null;
                    }
                };
                //new Thread(longRunningTask).start();
                longRunningTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        //System.out.println("** Login longRunningTask.setOnFailed ** \n");
                        //System.out.println("Ping - testError = "+QM.testError+"\n"); 
                        //System.out.println("Login - connCode = "+QM.connCode+"\n");
                        if(QM.testError == false) {
                            LA.LogintestErrorAlert(public_stage);
                        }
                            
                        LA.AlertChangeLang(QM.connCode, public_stage);
                        LA.SetVDOnline_AlertChangeLang(QM.SetVDonline_connCode, public_stage);
                        // System.out.println("Login -0000 IsVdOnline Failed = "+QM.VdOnline_connect+"\n");
                        // Login.setDisable(false);
                           
                        rootPane.setCenter(MainGP);  // 2017.09.13 william 登入中thread畫面鎖住   
                        lock_Login              = false;
                        QM.connCode             = 0; // 2017.09.18 william 錯誤修改(2)-2個警告訊息
                        QM.SetVDonline_connCode = 0; // 2017.09.18 william 錯誤修改(2)-2個警告訊息     
                        
                        StopLoginLock();// 2019.05.13 單一VD登入UI顯示
                    }                        
                });
                //new Thread(longRunningTask).start();
                longRunningTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        
                        // 2018.12.25 Remember10 username
                        WriteUserNameJson(UsernameComboBox.getValue().toString());                        
                        
                        if(QM.s == 1 && !IsSnapshot) { // 2017.11.24 william 單一帳號多個VD登入 / 2017.12.12 SnapShot實作                      
                            // System.out.println("** Login longRunningTask.setOnSucceeded ** \n");
                            LA.SetVDOnline_AlertChangeLang(QM.SetVDonline_connCode, public_stage);
                            // System.out.println("Login *** IsVdOnline Succeeded = "+QM.IsVdOnline+"\n");
//                            if(QM.connCode == 200 && QM.ChcekServerAddress_connCode == 200) {
//                                if(QM.IsVdOnline.equals("true") || QM.IsRDPVdOnline.equals("true")) {
////                                    try {
////                                        // System.out.println(" --- QM.IsVdOnline.equals(true)---  \n");
////                                        IsVdOnlineAlert();
////                                    } catch (IOException | InterruptedException ex) {
////                                        Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
////                                    }
//                                }
//                            }
                            if(QM.Alert_spiceaddr == 1) {
                                SetVDOlineAlert();
                            }
                            // Login.setDisable(false);
                            
                                rootPane.setCenter(MainGP); // 2017.08.10 william 登入中thread畫面鎖住      
                            lock_Login = false;
                            // 2017.09.18 william 錯誤修改(3)-清除密碼   https://stackoverflow.com/questions/54686/how-to-get-a-list-of-current-open-windows-process-with-java
                            try {                            
                                tasklist_p            = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",tasklist_cmd});
                                InputStreamReader isr = new InputStreamReader(tasklist_p.getInputStream());
                                BufferedReader input  = new BufferedReader(isr);   
                                tasklist_line         = input.readLine();
//                                while ((tasklist_line = input.readLine()) != null) {
                                if(tasklist_line != null) { // tasklist_line.contains(tasklist_s)==true
                                    System.out.println("***************************remote-viewer 啟動******************************\n");
                                    if(checkBox_Pw.isSelected()==checkBox_Pw_change){    
                                        pwBox.clear(); //若使用者登入後 密碼會自動清除
                                    }                                    
                                }
//                            }                             
                            } catch (IOException ex) {
                                Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                            }        
                            /* // 2017.09.18 william 錯誤修改(3)-清除密碼  
                            //延遲密碼clear的時間
                            Timer timer01=new Timer();
                            TimerTask task01=new TimerTask(){
                                @Override
                                public void run() {                    
                                    if(checkBox_Pw.isSelected()==checkBox_Pw_change){    
                                        pwBox.clear(); //若使用者登入後 密碼會自動清除
                                    }else{
                                        //記住密碼
                                    }
                                }
                            };
                                timer01.schedule(task01, 10000);
                            */
                        } else {                        // 2017.11.24 william 單一帳號多個VD登入  
                            rootPane.setCenter(MainGP); // 2017.09.13 william 登入中thread畫面鎖住   
                            lock_Login= false;          
                            // 2017.09.18 william 錯誤修改(3)-清除密碼   https://stackoverflow.com/questions/54686/how-to-get-a-list-of-current-open-windows-process-with-java
                            try {                            
                                tasklist_p            = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",tasklist_cmd});
                                InputStreamReader isr = new InputStreamReader(tasklist_p.getInputStream());
                                BufferedReader input  = new BufferedReader(isr);   
                                tasklist_line         = input.readLine();
//                                while ((tasklist_line = input.readLine()) != null) {
                                if(tasklist_line != null) { // tasklist_line.contains(tasklist_s)==true                                        
                                    if(checkBox_Pw.isSelected() == checkBox_Pw_change) {    
                                        pwBox.clear(); //若使用者登入後 密碼會自動清除
                                    }                                    
                                }
//                            }                             
                            } catch (IOException ex) {
                                Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                            } 
                            tooltip_SS.hide(); 
                        }
                        
                        StopLoginLock();// 2019.05.13 單一VD登入UI顯示
                    }
                });
                new Thread(longRunningTask).start();
            } else {
                LoginResultAlert();
                rootPane.setCenter(MainGP); // 2017.09.13 william 登入中thread畫面鎖住   
                lock_Login = false;
                    //System.out.println("** Login else end ** \n");
            }
        }    
    }    
    // Input： ,  Output： , 功能： 監聽螢幕Size是否改變
    private void Monitor_listener() {
        listener_timer = new Timer(); 
        listener_task  = new TimerTask() {
            @Override
            public void run() {                    
                Platform.runLater(() -> { 
                    // System.out.println(" timer1 start \n"); test
                    screen      = Screen.getPrimary();
                    bounds      = screen.getVisualBounds();                        
                    width_temp  = bounds.getWidth();
                    heigth_temp = bounds.getHeight();                                    
                    // 螢幕大小改變就重啟
                    if(width_temp != width_current || heigth_temp != heigth_current || GB.arandr_returncode == 200 || GB.monitor_restart == 1) {                         
                        try {
                            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","/root/restartui.sh"});
//                            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","killall python /root/arandr/arandr"});
                        } catch (IOException ex) {
                            Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.exit(1); 
                    }    
                    
                    
                    // 20019.02.18 新增FreeRDP for dual screen & usb redirection
                    List<Screen> DetectScreensCount = Screen.getScreens();
                    if(DetectScreensCount.size() > 1 && GB.DuplicateSize < 2) {
                        GB.FreeRDPEnable = true;
                    } else {
                        GB.FreeRDPEnable = false;
                    }
                    
                    // 一開始未接螢幕時，使用雙螢幕就重啟
//                    if(GB.reboot) {
//                        listener_timer.cancel();
//                        Login.setDisable(true);
//                        String s =  WordMap.get("WW_Header") + " ： " + "開機時未連接螢幕，導致雙螢幕異常" 
//                                     + "\n"                                            
//                                     + "\n"                    
//                                     + 
//                                    WordMap.get("Message_Suggest") + " ： " + "請重新開機";
//                        GBFunc.Alert(public_stage, 0, s, true, false, 550, "reboot");                       
//                    }
                    
                });              
            }
        };
        listener_timer.schedule(listener_task, 1000, 1000);          
    }                   
    // Input： ,  Output： , 功能： 副螢幕畫面設定
    public void SecondMonitorCreate() {
        screens = Screen.getScreens();
        System.out.println(" GB.DuplicateSize : " + GB.DuplicateSize + "\n");
                        
        if(screens.size() > 1 && GB.DuplicateSize < 2) {                        
            // 20019.02.18 新增FreeRDP for dual screen & usb redirection
            GB.FreeRDPEnable = true;
            
            pos_screen  = new double[2][2];
            size_screen = new double[2][2];
        
            Secondroot = new StackPane();
            Secondroot.setStyle("-fx-background-color: #1565c0 ;");        
                
            try {
                // Get current mouse location, could return null if mouse is moving Super-Man fast
                Point p = MouseInfo.getPointerInfo().getLocation();
                // Get list of available screens
//            List<Screen> screens = Screen.getScreens();
                if (p != null && screens != null && screens.size() > 1) {
                    // Screen bounds as rectangle
                    Rectangle2D screenBounds;
                    // Go through each screen to see if the mouse is currently on that screen
                    for (Screen screen : screens) {
                        screenBounds = screen.getVisualBounds();

                        pos_screen[screens.indexOf(screen)][0] = screenBounds.getMinX();
                        pos_screen[screens.indexOf(screen)][1] = screenBounds.getMinY();                    
                        size_screen[screens.indexOf(screen)][0] = screenBounds.getWidth();
                        size_screen[screens.indexOf(screen)][1] = screenBounds.getHeight();                    

                        if(screens.lastIndexOf(screen)==1) {
                            Shift_xPos            = pos_screen[screens.indexOf(screen)][0];
                            Shift_yPos            = pos_screen[screens.indexOf(screen)][1];
                            second_width_current  = size_screen[screens.indexOf(screen)][0];
                            second_heigth_current = size_screen[screens.indexOf(screen)][1];                     
                        }                                                                               
                    }
                }
            } catch (HeadlessException headlessException) {
                // Catch and report exceptions
                headlessException.printStackTrace();
            }         
        
            Secondscene = new Scene(Secondroot);     
            
            SecondStage.setX(Shift_xPos);
            SecondStage.setY(Shift_yPos);          
            SecondStage.setWidth(second_width_current);   
            SecondStage.setHeight(second_heigth_current); 
            SecondStage.centerOnScreen();               
            SecondStage.setScene(Secondscene);
            SecondStage.initStyle(StageStyle.UNDECORATED);
            SecondStage.show();        
         
            if(GB.AllDisplayOff && GB.DuplicateSize == 2) { /** 2018.03.15 william 雙螢幕實作 **/
                SecondStage.close();
            } else {
                SecondStage.show();
            }
        } else {
            // 20019.02.18 新增FreeRDP for dual screen & usb redirection
            GB.FreeRDPEnable = false;        
        }   
    }        
    // Input：1. 網路連線狀態(1:斷線, 2:連線, 3:連線中)(int) ,  Output： , 功能： 變更網路Icon
    public void Network_setGraphic(int status){
        switch (status) {
            case 0:
                Connect_ON_OFF.setGraphic(WiFi_connect_off);
                break;
            case 1:
                Connect_ON_OFF.setGraphic( WiFi_connect_on );
                break;
            case 2:
                Connect_ON_OFF.setGraphic(pi);
                break;                
            case 3:

                break;                
            default:
 
                break;
        }                                 
    }
    // Input：1. 目前電量狀態(0~4:充電中電量, 5~9:未充電電量)(int) ,  Output： , 功能： 變更電池Icon
    public void Battery_setGraphic(int status){
        switch (status) {
            case 0:
                Battery_ImageView.setImage(Battery_Charging_full_img);
                break;
            case 1:
                Battery_ImageView.setImage(Battery_Charging_3_img);
                break;
            case 2:
                Battery_ImageView.setImage(Battery_Charging_2_img);
                break;                
            case 3:
                Battery_ImageView.setImage(Battery_Charging_1_img);
                break;                
            case 4:
                Battery_ImageView.setImage(Battery_Charging_0_img);
                break;
            case 5:
                Battery_ImageView.setImage(Battery_No_Charging_full_img);
                break;
            case 6:
                Battery_ImageView.setImage(Battery_No_Charging_3_img);
                break;
            case 7:
                Battery_ImageView.setImage(Battery_No_Charging_2_img);
                break;
            case 8:
                Battery_ImageView.setImage(Battery_No_Charging_1_img);
                break;
            case 9:
                Battery_ImageView.setImage(Battery_No_Charging_0_img);
                break;            
            default: 
                break;            
        }                                 
    }                
    // Input：  ,  Output： , 功能： debug 寫檔案
    public void DebugWrite() {
        
        try {
            debugFile = new File("/root/dfile2.txt");
            if(debugFile.exists()) {
                debugFile.delete();
                debugFile = null;
            }
            
            try(FileWriter debugWriter = new FileWriter("/root/dfile2.txt")) {
                debugWriter.write("Display Change");
                debugWriter.flush();
                debugWriter.close();
            }   
        }
        catch(Exception e) {
           // e.printStackTrace();
        }
    }       
    // Input： ,  Output： False/True (boolean), 功能： 檢查有無wi-fi模組，無為False，有為True
    private boolean checkWIFI() {
        try {
            String command = "ifconfig | grep wlan";  
            Process CheckWIFI_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",command});
            BufferedReader output_CheckWIFI = new BufferedReader (new InputStreamReader(CheckWIFI_process.getInputStream()));
            line_CheckWIFI = output_CheckWIFI.readLine();
        }
        catch(IOException e) {
//            e.printStackTrace();
        }
        
        if(line_CheckWIFI != null) {
            return true; 
        } else {
            return false;
        }

    }
    // Input： ,  Output： False/True (boolean), 功能： 檢查有無電池模組，無為False，有為True
    private boolean checkBATTERY() {
        try {
            String command = "cat /sys/class/power_supply/BAT0/uevent";  
            Process CheckBATTERY_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",command});
            BufferedReader output_CheckBATTERY = new BufferedReader (new InputStreamReader(CheckBATTERY_process.getInputStream()));
            line_CheckBATTERY = output_CheckBATTERY.readLine();
        }
        catch(IOException e) {
//            e.printStackTrace();
        }
        
        if(line_CheckBATTERY != null) {
            return true; 
        } else {
            return false;
        }
    }     
    // Input： ,  Output： 回傳正在使用的eth (String), 功能： 檢查網路使用哪個eth
    private String Get_eth() throws IOException {
        String eth_num = null;
        String line_eth_status = "";
        String[] Line_list = null;
            String command = "ifconfig | grep '[e]th'";  
            Process Get_eth_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",command});
            try(BufferedReader output_Get_eth = new BufferedReader (new InputStreamReader(Get_eth_process.getInputStream()))) {
                line_eth_status = output_Get_eth.readLine();
                if(line_eth_status != null) {
                    if(line_eth_status.contains("eth")) {
                        Line_list = line_eth_status.split(" ");
                        eth_num = Line_list[0]; 
                        //System.out.println(Line_list[0]);                        
                    }
                }  
                output_Get_eth.close();
            }

        return eth_num; 
    }    
    // Input： ,  Output： 回傳Protocol (String), 功能： 取得目前Protocol 
    public static synchronized String getProtocol() {
        return ProtocolTypeValue;
    }
    // Input： 1.Protocol 類型；AcroSpice or AcroRDP (String),  Output： , 功能： 設定目前Protocol 
    public static synchronized void setProtocol(String type) {
        ProtocolTypeValue = type;
    }           
    // Input： ,  Output： , 功能： 讀取10組 username
    public void LoadUserNameJson() throws ParseException {
        File File = new File("jsonfile/UserName.json");
        JSONParser parser = new JSONParser();
        String FilePath = File.getPath();
        ArrayList<String> _name = new ArrayList<String>();
        ArrayList<String> _nameRes = new ArrayList<String>();
        _uname.clear();
        
        try {            
            if(File.exists()) {                
                JSONArray dataArray = (JSONArray) parser.parse(new FileReader(FilePath));                               
                
                for (Object o : dataArray) {
                    JSONObject obj = (JSONObject) o;
                    String name = (String) obj.get("name");
                    
                    _name.add(name);

                }
                
                for(int i = _name.size() - 1; i >= 0; i--) {       
                    _nameRes.add( _name.get(i));
                }
                                                              
                _uname = _nameRes;
                users.addAll(_nameRes);
            } else {
                FileWriter JsonWriter = new FileWriter(FilePath);
                JsonWriter.write("[]");
                JsonWriter.flush();
                JsonWriter.close();               
            }

        }
        catch(Exception e) { // Json檔案讀寫失敗或錯誤的處理，將IOException 改為通用型例外 Exception
            System.out.println("Json file is null");            
            try {
                FileWriter JsonWriter = new FileWriter(FilePath);
                JsonWriter.write("[]");
                JsonWriter.flush();
                JsonWriter.close();                   
            } catch (IOException ex) {
                Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
            }                                 
        }
        
    }
    // Input： 戶用名(String),  Output： , 功能： 寫入用戶名到UserName.json
    public void WriteUserNameJson(String _name) { // https://stackoverflow.com/questions/50402343/how-to-append-data-on-a-existing-json-file-using-java
        boolean WritetoFile = true;
        File File = new File("jsonfile/UserName.json");
        JSONParser parser = new JSONParser();
        String FilePath = File.getPath();                
        Object obj = null;        
        int count = 0;
        
        for(String _n : _uname) {
            if(_name.equals(_n)) {
                System.out.println("_name: " + _name + " _n: " + _n);
                WritetoFile = false;
                break;            
            }
            count++;
        }
        
        if(WritetoFile) {


            try {
                obj = parser.parse(new FileReader(FilePath));
            } catch (Exception ex) {
                Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
            }

            JSONArray jsonArray = (JSONArray)obj;      
            
            JSONObject _userName = new JSONObject();
            _userName.put("name", _name);
            jsonArray.add(_userName);
            
            JSONObject _removeName = new JSONObject();
            if(_uname.size() > 9) {
                _removeName.put("name", _uname.get(9));
                jsonArray.remove(_removeName);                 
            }            
                        
            try {
                try(FileWriter JsonWriter = new FileWriter(FilePath)) {
                    JsonWriter.write(jsonArray.toJSONString());
                    JsonWriter.flush();
                    JsonWriter.close();
                }
                
            } catch(IOException e) {}              
            
            RefreshUsernameComboBox(true, "");                
        } else {
            ArrayList<String> _nameRes = new ArrayList<String>();
//            System.out.println(_uname);
            String temp = _uname.get(count);     
            _uname.remove(count);
//            _uname.set(count, _uname.get(0));
//            _uname.set(0, temp);
//            System.out.println(_uname);
            
            for(int i = _uname.size() - 1; i >= 0; i--) {       
                _nameRes.add(_uname.get(i));
            }            
            _nameRes.add(temp);
//            System.out.println(_nameRes);
                  
            String _str = "";
            
            _str = "[";
            
            for(int i = 0; i < _nameRes.size(); i++) {
                _str += "{\"name\":\"";
                _str += _nameRes.get(i);
                _str += "\"}";

                if(i != (_nameRes.size() - 1))
                    _str += ",";

            }            
            
            _str += "]";
            
//            System.out.println(_str);
            
            if(File.exists()){
                File.delete();
            }               
            
            try(FileWriter JsonWriter = new FileWriter(FilePath)) {
                JsonWriter.write(_str);
                JsonWriter.flush();
                JsonWriter.close();   

            } catch(Exception e) {}      

            RefreshUsernameComboBox(false, temp);
//            try {
//                UsernameComboBox.getItems().clear();
//                LoadUserNameJson();
//                UsernameComboBox.setValue(temp);
//            } catch (ParseException ex) {
//                Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
//            }              
            
            
        }       
    }    
    // Input： 1. 是否刷新(boolean), 2. 戶用名(String),  Output： , 功能： 更新用戶名的ComboBox
    public void RefreshUsernameComboBox(boolean _flag , String _n) {
        try {             
            UsernameComboBox.getItems().clear();
            LoadUserNameJson();
            if(!_flag)
                UsernameComboBox.setValue(_n);
        } catch (ParseException ex) {
            Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }
    // Input： ,  Output： , 功能： log畫面顯示目前時間，除錯用
    public void showTime() {
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
	System.out.println("*****************" + dateFormat.format(date) + "*****************"); //2016/11/16 12:08:43    
    }    
    // Input： ,  Output： , 功能： VM/VD migration 鎖定畫面
    public void MigrationLockView() {
        GridPane MigrationGP = new GridPane();
        MigrationGP.setAlignment(Pos.BASELINE_CENTER);       
        MigrationGP.setHgap(5);
        MigrationGP.setVgap(5);
        MigrationGP.setPrefSize(530, 220); //400, 480
        MigrationGP.setMaxSize(530, 220);
        MigrationGP.setMinSize(530, 220);
        MigrationGP.setTranslateY(45);
        MigrationGP.setStyle("-fx-background-color: #EEF5F5 ; -fx-opacity: 0.7 ; -fx-background-radius: 4;-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 10, 0.0 , 0 , 5 );");    
        
        // Label _mv1Title = new Label(WordMap.get("Str_Dynamic_Transfer")); 
        _mv1Title.setText(WordMap.get("Str_Dynamic_Transfer"));
        Label _mv2Title = new Label(WordMap.get("Thread_Waiting")); 
        
        _mv1Title.setAlignment(Pos.CENTER);
        _mv2Title.setAlignment(Pos.CENTER);
        _dot_String.setAlignment(Pos.CENTER);
        //_migrationviewTitle.setTranslateY(40);
        _mv1Title.setId("Thread_status");
        _mv2Title.setId("Thread_status");
        
        _dot_String.setStyle("-fx-font-family:'Ubuntu';");
        _dot_String.setId("Thread_status");
        
        HBox FirstRow = new HBox();
        FirstRow.setAlignment(Pos.CENTER);
        FirstRow.setTranslateY(30);
        FirstRow.getChildren().addAll(_mv1Title);
        MigrationGP.add(FirstRow, 0, 0);          
   
        HBox S1 = new HBox();
        S1.setAlignment(Pos.CENTER);    
        S1.setTranslateX(10);
        S1.getChildren().addAll(_mv2Title);
        
        HBox S2 = new HBox();                
        S2.setAlignment(Pos.CENTER_LEFT);
        S2.setTranslateX(10);
        S2.getChildren().addAll(_dot_String);
        S2.setPrefWidth(20);
        S2.setMaxWidth(20);
        S2.setMinWidth(20);        
        
        
        HBox SecondRow = new HBox();
        SecondRow.setAlignment(Pos.CENTER);    
        SecondRow.setTranslateY(35);   
        SecondRow.getChildren().addAll(S1, S2);             
        MigrationGP.add(SecondRow, 0, 1); 

        ProgressIndicator p1 = new ProgressIndicator();
        p1.setTranslateY(50);
        MigrationGP.add(p1, 0, 2);

        Button _cancelBtn = new Button(WordMap.get("Cancel"));
        _cancelBtn.setPrefSize(100, 30);
        _cancelBtn.setMaxSize(100, 30);
        _cancelBtn.setMinSize(100, 30);
        _cancelBtn.setTranslateX(195);
        _cancelBtn.setId("Loginbutton");
        //_cancelBtn.setAlignment(Pos.BOTTOM_RIGHT);
        _cancelBtn.requestFocus();
        _cancelBtn.setOnAction((event) -> {
            rootPane.setCenter(MainGP);
            StopMigrationLock();
        });       
        
        HBox _hbox = new HBox();
        _hbox.setPadding(new Insets(1, 0, 0, 0));
        _hbox.getChildren().addAll(_cancelBtn);  
        _hbox.setAlignment(Pos.BOTTOM_CENTER);
        _hbox.setSpacing(10);     
        MigrationGP.add(_hbox, 0, 9);
        
        StackPane stack = new StackPane();
        stack.getChildren().addAll(MainGP, MigrationGP);   
        rootPane.setCenter(stack); 
    }    
    // Input： ,  Output： , 功能： VM/VD migration 鎖定畫面說明文字更新
    public void MigrationLock_Renew_String() {
        final LongProperty lastUpdate = new SimpleLongProperty();                 
        final long minUpdateInterval = 5000 ; // nanoseconds. Set to higher number to slow output.

        TextTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate.get() > minUpdateInterval) {
                    //System.out.println("Run TextTimer");
                    messageText = messageQueue.poll();
                    if (messageText != null) {                        
                        _dot_String.setText(messageText);
                        messageQueue.clear();
                        // System.out.println("2. MessageQueue running.");
                    }
                    messageText2 = messageQueue2.poll();
                    if (messageText2 != null) {                        
                        _mv1Title.setText(messageText2);
                        messageQueue2.clear();
                    }                    
                    lastUpdate.set(now);
                }
            }
        };
//        TextTimer.start();         

    }        
    // Input： ,  Output： , 功能： 說明文字第一行更新(class)
    private class MessageProducer implements Runnable {
        private final BlockingQueue<String> messageQueue ;

        public MessageProducer(BlockingQueue<String> messageQueue) {
            this.messageQueue = messageQueue ;
        }

        @Override
        public void run() {
            int messageCount = 0 ;
            try {
                while (true) {
                    //System.out.println("Run message producer");
                    switch (messageCount) {
                       case  0:
                           message = "";
                           messageQueue.put(message);
                           messageCount ++;
                           sleep(300);
                           break;
                        case  1:   
                           message = ".";
                           messageQueue.put(message);                            
                           messageCount ++;   
                           sleep(300);
                           break;
                        case  2:   
                           message = "..";
                           messageQueue.put(message);                                 
                           messageCount ++;   
                           sleep(300);
                           break;
                        case  3:   
                           message = "...";
                           messageQueue.put(message);                               
                           messageCount = 0;       
                           sleep(300);
                           break;                    
                    }
                    if(StopMessageQueue)
                        break;
                    
                    //System.out.println("1. MessageQueue running.");
                    
                }
            } catch (InterruptedException exc) {
                System.out.println("Message producer interrupted: exiting.");
            }
        }
    }  
    // Input： ,  Output： , 功能： 說明文字第二行更新(class)
    private class MessageProducer2 implements Runnable {
        private final BlockingQueue<String> messageQueue2 ;

        public MessageProducer2(BlockingQueue<String> messageQueue2) {
            this.messageQueue2 = messageQueue2 ;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    if(ReconnectFlag2) {
                        message2 = WordMap.get("Str_Dynamic_Transfer_Reconnect");                    
                        messageQueue2.put(message2);                    
                    }                   
                    sleep(500);
                } catch (Exception ex) {
                    Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                }            
            }

        }
    }      
    // Input： ,  Output： , 功能：  migration 鎖定畫面啟動
    public void StartMigrationLock() {
        Platform.runLater(() -> { 
            MigrationLockView();     
        });          
        
          
        TextTimer.start();
        //MessageProducer producer = new MessageProducer(messageQueue);
//        Thread t = new Thread(producer);  
//     
//        t.start(); 
        executor.execute(producer);  
        executor.execute(producer2);  
    }      
    // Input： ,  Output： , 功能：  migration 鎖定畫面停止
    public void StopMigrationLock() {
        //StopMessageQueue = true; // 2018.11.30 新增等待中文字動態顯示

        MigrationFlag = false;    
        ReconnectFlag = false;   
        MigrationCloseViewer = true;
        ReconnectFlag2 = false;   
        MigrationCount = 0;    
        CPCount = 0; 
        TextTimer.stop();      
    }    
    // Input： ,  Output： , 功能： 檢查migration狀態
    public class checkMigration implements Runnable {  

        @Override
        public void run() { 
            while (true) {

                try {                   
                                      
                    if(CPFlag && !GB.migrationVDID.equals("null")) {                                                                                            
                        try {                                                                                    
                            MigrationReturnCode = QM.GetVDStatus(IP_Addr.getText(), CurrentIP_Port, GB.migrationVDID);

                            if(MigrationReturnCode == 7 && !MigrationFlag && !MigrationCloseViewer) { 
                                MigrationFlag = true;    
                                KillallViewer();
                                StartMigrationLock();
                            }                            
                        } catch (IOException ex) {
                            Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                    if(MigrationFlag) {
                        
                        if(!ReconnectFlag) {
                            try {
                                MigrationReturnCode = QM.GetVDStatus(IP_Addr.getText(), CurrentIP_Port, GB.migrationVDID);                            

                                if(MigrationReturnCode != 7) {                                                                                               
                                    switch (GB.migrationProtocol) {
                                        case "AcroSpice":
                                            QM.SetVDOnline(GB.migrationApiIP, GB.migrationVDID, GB.migrationUserName, GB.migrationPwd, GB.migrationUkey, GB.migrationApiPort);                                
                                            break;
                                        case "AcroRDP": 
                                            QM.SetVDRDPOnline(GB.migrationApiIP, GB.migrationVDID, GB.migrationUserName, GB.migrationPwd, GB.migrationUkey, GB.migrationApiPort, GB.migrationIsVdOrg, GB.EnableUSB, GB.adAuth, GB.adDomain); 
                                            break;       
                                        default: 
                                            break;                              
                                    }                      
                                    
                                    ReconnectFlag = true;  
                                    sleep(1000);

                                    if(ReconnectFlag && CheckNetstat()) {
                                        Platform.runLater(() -> { 
                                            rootPane.setCenter(MainGP);
                                            StopMigrationLock();
                                        });                             
                                    } else {
                                        KillallViewer();
                                        ReconnectFlag = false;
                                        ReconnectFlag2 = true;   
                                    }                                    
                                }                              

                            } catch (IOException ex) {
                                Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                            }                        
                                                
                        }
                        


                        
                        if(GB._3D_Type != 1 && MigrationCount == 66) { // 42 -> 66
                            rootPane.setCenter(MainGP);
                            StopMigrationLock();
                        } else if(GB._3D_Type == 1 && MigrationCount == 66) {
                            rootPane.setCenter(MainGP);
                            StopMigrationLock();
                        }
                        
                        MigrationCount++;
                    }                    
                    
                    sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    // Input： ,  Output： False/True (boolean), 功能： 檢查Protocol軟體有無啟動，無為False，有為True
    private boolean ConnectionProcess() {
        String line_Spice = "";
        String line_remmina = "";
        String line_xfreerdp = "";       
        String temp = "";
        String Spicecommand = "ps -aux | grep [r]emote-viewer";  
        String remminacommand = "ps -aux | grep [r]emmina";  
        String remminacommand2 = "lsof | grep ^remmina | grep /opt/remmina_devel/remmina/bin/remmina | wc -l";  
        String xfreerdpcommand = "ps -aux | grep [x]freerdp";          
        
        try {            
            Process CheckSpice_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",Spicecommand});
            BufferedReader output_CheckSpice = new BufferedReader (new InputStreamReader(CheckSpice_process.getInputStream()));
            line_Spice = output_CheckSpice.readLine();
            
            Process Checkremmina_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",remminacommand});
            BufferedReader output_Checkremmina = new BufferedReader (new InputStreamReader(Checkremmina_process.getInputStream()));
            line_remmina = output_Checkremmina.readLine();
            
            // System.out.println("---------------------enter: " + line_remmina);
            if(line_remmina != null) {
                Process Checkremmina_process2 = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",remminacommand2});
                BufferedReader output_Checkremmina2 = new BufferedReader (new InputStreamReader(Checkremmina_process2.getInputStream()));
                temp = output_Checkremmina2.readLine();
                // System.out.println("---------------------temp: " + temp + "..............");
                if(temp.equals("1")) {
                    line_remmina = null;                    
                    // System.out.println("---------------------temp1111");
                }
                    
            }
    
            
            Process Checkxfreerdp_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",xfreerdpcommand});
            BufferedReader output_Checkxfreerdp = new BufferedReader (new InputStreamReader(Checkxfreerdp_process.getInputStream()));
            line_xfreerdp = output_Checkxfreerdp.readLine();            
            
        } catch(IOException e) {}
        
        if(line_Spice == null && line_remmina == null && line_xfreerdp == null) {
            return false;
        } else {
            return true; 
        }

    }     
    // Input： ,  Output： False/True (boolean), 功能： 檢查Protocol軟體有無連接成功，無為False，有為True
    private boolean CheckNetstat() {
        String line_Spice = "";
        String line_remmina = "";
        String line_xfreerdp = "";        
        String Spicecommand = "netstat -tup | grep remote-viewer";  
        String remminacommand = "netstat -tup | grep remmina";  
        String xfreerdpcommand = "netstat -tup | grep xfreerdp";          
        
        try {            
            Process CheckSpice_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",Spicecommand});
            BufferedReader output_CheckSpice = new BufferedReader (new InputStreamReader(CheckSpice_process.getInputStream()));
            line_Spice = output_CheckSpice.readLine();
            
            Process Checkremmina_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",remminacommand});
            BufferedReader output_Checkremmina = new BufferedReader (new InputStreamReader(Checkremmina_process.getInputStream()));
            line_remmina = output_Checkremmina.readLine();
            
            Process Checkxfreerdp_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",xfreerdpcommand});
            BufferedReader output_Checkxfreerdp = new BufferedReader (new InputStreamReader(Checkxfreerdp_process.getInputStream()));
            line_xfreerdp = output_Checkxfreerdp.readLine();            
            
        } catch(IOException e) {}
        
        if(line_Spice == null && line_remmina == null && line_xfreerdp == null) {
            return false;
        } else {
            return true; 
        }

    }         
    // Input： ,  Output： , 功能： 檢查Protocol軟體狀態
    public class checkConnectionProcess implements Runnable {  
 
        @Override
        public void run() { 

            while (true) {

                try {

                    if(ConnectionProcess()) {
                        CPFlag = true;               
                        MigrationCloseViewer = false;
                        leaveViewer = true;
                    } else {
                        MigrationCloseViewer = true;
                        CPFlag = false; 
                        
                        if(leaveViewer) {
                            RealtimeCheckMigration();
                            if(!MigrationFlag)
                                clearMigrationParameter();                            
                            leaveViewer = false;
                        }                                                
                    }
                                                                                                                        
                    sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }    
    // Input： ,  Output： , 功能： 關閉所有Protocol軟體
    public void KillallViewer() {  
 
        try {
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","killall remote-viewer"});
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","killall remmina"});
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","killall xfreerdp"});
        } catch (IOException ex) {
            Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    // Input： ,  Output： , 功能： 清空migration的連線參數
    public void clearMigrationParameter() {  
        GB.migrationVDID = "null";
        GB._3D_Type = -1;                
        GB.migrationApiIP = "";
        GB.migrationApiPort = "";
        GB.migrationUserName = "";
        GB.migrationPwd = "";
        GB.migrationUkey = "";
        GB.migrationProtocol = "";
        GB.migrationIsVdOrg = false;   
        GB.EnableUSB = false;        
        GB.adAuth = false;   
        GB.adDomain = "";        
    }    
    // Input： ,  Output： , 功能： migration的即時狀態檢查 
    public void RealtimeCheckMigration() {
        try {                                                                                    
            MigrationReturnCode = QM.GetVDStatus(IP_Addr.getText(), CurrentIP_Port, GB.migrationVDID);

            if(MigrationReturnCode == 7) { 
                MigrationFlag = true;    
                KillallViewer();
                StartMigrationLock();
            }                            
        } catch (IOException ex) {
            Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }            
    // Input： ,  Output： , 功能： 單一VD登入UI文字更新顯示 
    public void LoginLock_Renew_String() {
        final LongProperty lastUpdate = new SimpleLongProperty();                 
        final long minUpdateInterval = 5000 ; // nanoseconds. Set to higher number to slow output.

        UILockTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate.get() > minUpdateInterval) {
                    
                    messageTitle1 = messageQueueTitle1.poll();
                    messageTitle2 = messageQueueTitle2.poll();
                    messageTitle3 = messageQueueTitle3.poll();
                    //System.out.println("------------- Run Login Lock Timer -------------");
                    if (messageTitle1 != null) {                        
                        Login_title.setText(messageTitle1);
                        messageQueueTitle1.clear();

                    }

                    if (messageTitle2 != null) {                        
                        Login_title2.setText(messageTitle2);
                        messageQueueTitle2.clear();
                        // System.out.println("2. MessageQueue running.");
                    }                    
                    
                    if (messageTitle3 != null) {                        
                        Login_title3.setText(messageTitle3);
                        messageQueueTitle3.clear();
                        // System.out.println("2. MessageQueue running.");
                    }                        
                    
                    //System.out.println("Run TextTimer");
           
                    lastUpdate.set(now);
                }
            }
        };
    }        
    // Input： ,  Output： , 功能： 單一VD登入說明文字第一行更新(class)        
    private class MessageTitle1 implements Runnable {
        private final BlockingQueue<String> messageQueueTitle1 ;


        public MessageTitle1(BlockingQueue<String> messageQueueTitle1) {
            this.messageQueueTitle1 = messageQueueTitle1 ;
        }

        @Override
        public void run() {

            try {
                while (true) {
                    if(GB.AvailableProtocol == 1) {
                        messageT1 = WordMap.get("Thread_Connecting");
                        messageQueueTitle1.put(messageT1);                        
                    } else if(GB.AvailableProtocol == 2) {
                        if("English".equals(LangNow)){
                            Login_title.setStyle("-fx-font-size: 15px;");          
                        }  else if("日本語".equals(LangNow)){
                            Login_title.setStyle("-fx-font-size: 14px;");
                        }                            
                                                
                        if(GB.RDPFirst == 1) {
                            messageT1 = WordMap.get("Message_NewCreate");
                            messageQueueTitle1.put(messageT1);                        
                        } else if(GB.RDPFirst == 2) {
                            messageT1 = WordMap.get("Message_Reborn");
                            messageQueueTitle1.put(messageT1);                        
                        } else if(GB.RDPFirst == 0 && (GB.RDPStatus == 0 || GB.RDPStatus == 1)) {
                            messageT1 = WordMap.get("Message_RDP_Booting");
                            messageQueueTitle1.put(messageT1);                         
                        }
                    } else if(GB.AvailableProtocol == 3) {
                    }
                    sleep(500);
                }
            } catch (InterruptedException exc) {
                System.out.println("Message producer interrupted: exiting.");
            }
        }
    }
    // Input： ,  Output： , 功能： 單一VD登入說明文字第二行更新(class)        
    private class MessageTitle2 implements Runnable {
        private final BlockingQueue<String> messageQueueTitle2 ;


        public MessageTitle2(BlockingQueue<String> messageQueueTitle2) {
            this.messageQueueTitle2 = messageQueueTitle2 ;
        }

        @Override
        public void run() {

            try {
                while (true) {

                    if(GB.AvailableProtocol == 1) {
                        messageT2 = WordMap.get("Thread_Waiting");
                        messageQueueTitle2.put(messageT2);
                    } else if(GB.AvailableProtocol == 2) {
                        if("日本語".equals(LangNow)){
                            Login_title2.setStyle("-fx-font-size: 16px;");
                        }                           
                        
                        switch (GB.RDP_Stage_Type) {
                               case  0:
                                    messageT2 = WordMap.get("Message_ReadingProfile");
                                    messageQueueTitle2.put(messageT2);

                                    break;
                                case  1:   
                                   messageT2 = WordMap.get("Message_RDP_Stage_1");
                                   messageQueueTitle2.put(messageT2); 

                                   break;
                                case  2:   
                                   messageT2 = WordMap.get("Message_RDP_Stage_2");
                                   messageQueueTitle2.put(messageT2);     

                                   break;        
                                case  3:   
                                    if("true".equals(GB.IsAssignedUserDisk))
                                        messageT2 = WordMap.get("Message_RDP_Stage_3_Profile");
                                    else if("false".equals(GB.IsAssignedUserDisk))
                                        messageT2 = WordMap.get("Message_RDP_Stage_3_NoProfile");
                                    else if("NULL".equals(GB.IsAssignedUserDisk))
                                        messageT2 = WordMap.get("Message_IsAssignedUserDisk_NULL");

                                    messageQueueTitle2.put(messageT2);     

                                   break;  
                                case  4:   
                                    if("true".equals(GB.IsAssignedUserDisk))
                                        messageT2 = WordMap.get("Message_RDP_Stage_4_Profile");
                                    else if("false".equals(GB.IsAssignedUserDisk))
                                        messageT2 = WordMap.get("Message_RDP_Stage_4_NoProfile");
                                    else if("NULL".equals(GB.IsAssignedUserDisk))
                                        messageT2 = WordMap.get("Message_IsAssignedUserDisk_NULL");

                                    messageQueueTitle2.put(messageT2);   

                                   break;  
                                case  5:   
                                   messageT2 = WordMap.get("Thread_Connecting");
                                   messageQueueTitle2.put(messageT2);     
                                   break;      
                                default:   
                                    messageT2 = WordMap.get("Message_RDP_Preparing");
                                    messageQueueTitle2.put(messageT2);
                                    break;      
                            }                

                    } else if(GB.AvailableProtocol == 3) {
                        messageT2 = WordMap.get("Thread_Waiting");
                        messageQueueTitle2.put(messageT2);                
                    }
                    sleep(500);
                }
            } catch (InterruptedException exc) {
                System.out.println("Message producer interrupted: exiting.");
            }
        }
    }
    // Input： ,  Output： , 功能： 單一VD登入說明文字...更新(class)            
    private class MessageDot implements Runnable {
        private final BlockingQueue<String> messageQueueTitle3 ;

        public MessageDot(BlockingQueue<String> messageQueueTitle3) {
            this.messageQueueTitle3 = messageQueueTitle3 ;
        }

        @Override
        public void run() {
            int messageCount = 0 ;
            try {
                while (true) {
                    //System.out.println("Run message producer");
                    switch (messageCount) {
                       case  0:
                           messageT3 = "";
                           messageQueueTitle3.put(messageT3);
                           messageCount ++;
                           sleep(300);
                           break;
                        case  1:   
                           messageT3 = ".";
                           messageQueueTitle3.put(messageT3);                            
                           messageCount ++;   
                           sleep(300);
                           break;
                        case  2:   
                           messageT3 = "..";
                           messageQueueTitle3.put(messageT3);                                 
                           messageCount ++;   
                           sleep(300);
                           break;
                        case  3:   
                           messageT3 = "...";
                           messageQueueTitle3.put(messageT3);                               
                           messageCount = 0;       
                           sleep(300);
                           break;                    
                    }
                    if(StopMessageQueue)
                        break;
                    
                    //System.out.println("1. MessageQueue running.");
                    
                }
            } catch (InterruptedException exc) {
                System.out.println("Message producer interrupted: exiting.");
            }
        }
    }  
     // Input： ,  Output： , 功能：   單一VD登入 鎖定畫面啟動             
    public void StartLoginLock() {
        UILockTimer.start(); 
    }    
    // Input： ,  Output： , 功能：  單一VD登入 鎖定畫面停止
    public void StopLoginLock() {
        UILockTimer.stop();
        GB.AvailableProtocol = -1; 
        GB.RDP_Stage_Type = -100;
        GB.RDPFirst = -1;     
        GB.IsAssignedUserDisk = "";
        GB.VDStatus = -100;
        GB.IsPingOK = false;   
        GB._rdpArraySize = 0;        
        GB.RDPStatus = -1;
    }       
    
    
}
