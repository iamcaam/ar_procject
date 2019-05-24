/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import java.io.*;
import static java.lang.Thread.sleep;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.StageStyle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.CheckBox;
import javafx.animation.PauseTransition;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author duck_chang ,sabrina_yeh
 */
public class AcroRed_VDI_Viewer extends Application {
    /***   版本   ***/
    private static String Ver="(V 5.3.2)";// 32bit ( V 1.0.0 )_build11
   
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
    private TextField IP_Addr;
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
    private Button Check;
    private Button Leave;
    private Stage public_stage;
    //Thread 宣告
    private QueryMachine QM;
    private VDMLogin VDML;
    private CPLogin CPL;
    private PingServer PS;
    private LoginAlert LA;
    private VDLoginAlert VDMLA;
    private CPLoginAlert CPLA;
    
    public final UUID uniqueKey = UUID.randomUUID();   
    List<Process> processes =FXCollections.observableArrayList() ;
    private Image EyeClose;
    private Image EyeOpen;
    private ToggleButton buttonPw=new ToggleButton();
    private ToggleButton buttonIP=new ToggleButton();
    private ToggleButton buttonUname=new ToggleButton();
    private boolean changeAfterSaved = true;
    private boolean IP_changeAfterSaved;
    private boolean Name_changeAfterSaved;
    public boolean checkBox_Pw_change = false; 
    private boolean Login_Color = false;
    private boolean ManageVD_Color = false;
    private boolean ChangePW_Color = false;
    private boolean Leave_Color = false;
    private boolean SS_Color = false; 
    public CheckBox checkBox_Pw; 
    private PasswordField pwIP;
    private PasswordField pwName;
    private ComboBox LanguageSelection;
    private TextField textField01;
    public PasswordField pwBox; 
    private boolean lock_Login = false;
    private boolean lock_ManageVD = false;
    private boolean lock_ChangePW = false;
    private TextField IP_Port; 
    public String CurrentIP_Port;
    private PasswordField pwIP_Port;    
    private GridPane MainGP; 
    private GridPane LoginGP; 
    private Label Login_title;
    private BorderPane rootPane;   
    public static int CheckPort;
    private String  tasklist_cmd = System.getenv("windir") + "\\system32\\" + "tasklist.exe"; 
    private Process tasklist_p; 
    public String tasklist_s = "remote-viewer.exe";
    public String tasklist_line;     
    /*===============================================錯誤修改(3)-清除密碼 for windows===============================================*/
    public boolean ClearPW_flag = false; 
    public boolean ClearPW_Login_flag;
    public int ClearPW_count = 0;
    public boolean ClearPW_first_start = true;
    public int ClearPW_max_count;
    /*===============================================錯誤修改(3)-清除密碼 for windows===============================================*/        
    public GB GB; 
    Scene scene;     
    private Button SS;        
    private boolean IsSnapShot   = false;    
    JSONObject SystemStatus;
    JSONObject AfterChange;        
    Image company_Logo;    
    public static String ProtocolTypeValue; 
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
        public_stage=primaryStage;
        QM = new QueryMachine(WordMap); 
        VDML = new VDMLogin(WordMap); 
        CPL = new CPLogin(WordMap); 
        PS=new PingServer(WordMap); 
        LA=new LoginAlert(WordMap); 
        VDMLA=new VDLoginAlert(WordMap); 
        CPLA=new CPLoginAlert(WordMap); 
        
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
        buttonUname.setGraphic(new ImageView(EyeOpen));        
        LoadLastChange();
        
        // 2019.01.02 Remember10 username
        LoadUserNameJson();        
        
        GB.winXP_ver = checkOSVer(); // 2017.10.27 william 版本判斷
//        GB.winXP_ver = true; // XP測試用
        System.out.println("32bit版本(true：XP版，false：Windows 7/8/10) : "+GB.winXP_ver+"\n");
        
        /***  主畫面使用GRID的方式Layout ***/
        MainGP=new GridPane(); // 2017.08.10 william 登入中thread畫面鎖住       
        //MainGP.setGridLinesVisible(true); //開啟或關閉表格的分隔線       
        MainGP.setAlignment(Pos.CENTER);       
        MainGP.setPadding(new Insets(22, 13, 32, 20)); //設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        MainGP.setHgap(5); //元件間的水平距離
   	MainGP.setVgap(5); //元件間的垂直距離
        
        /***  底層畫面使用Border的方式Layout ***/ // 2017.08.10 william 登入中thread畫面鎖住    
        rootPane = new BorderPane();
        rootPane.setCenter(MainGP);           
        
        /*********判斷 OS 系統***********/
        /*
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println("System.getProperty(os.name) : "+os+"\n");
        
        String os2 = System.getProperty("sun.arch.data.model").toLowerCase();
        System.out.println("System.getProperty(sun.arch.data.mod) : "+ os2 +"\n");
        */
        /*********************************/
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
        //Logo.setId("Logo");
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
        MainGP.add(LanguageBar, 2,0,1,1);  // (2,0,1,1)1,0,2,1表示放在GRID 0 0 的位置，寬度跨越2個Colume，高度1個Row
     
        /***  Title 放入一個HBOX內 ***/
        MyTitle=new Label(WordMap.get("Title"));
       
        HBox WC=new HBox(MyTitle);
        WC.setAlignment(Pos.TOP_LEFT);
        //GridPane.setHalignment(MyTitle, HPos.LEFT);
        WC.setPadding(new Insets(0, 0, 0, 15));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        //WC.setSpacing(5); 
        MainGP.add(WC,1,1,2, 1);//1121

        /*******************  輸入VDI Server 的IP位址 ************************/  
        if(!GB.winXP_ver) {
            L_IP_Addr = new Label(WordMap.get("IP_Addr"));//+":"    
        }
        else {
            L_IP_Addr = new Label(WordMap.get("IP_Addr_XP"));    
        }
        
        GridPane.setHalignment(L_IP_Addr, HPos.LEFT);      
        if(!GB.winXP_ver) {
            L_IP_Addr.setMaxWidth(90);//強制限制Label最大寬度,讓每個Label在變換語言時不會變動
            L_IP_Addr.setMinWidth(90);//強制限制Label最小寬度,讓每個Label在變換語言時不會變動
            L_IP_Addr.setPrefWidth(90);//強制限制Label預設寬度,讓每個Label在變換語言時不會變動
        }
        else {
            L_IP_Addr.setMaxWidth(90);//強制限制Label最大寬度,讓每個Label在變換語言時不會變動
            L_IP_Addr.setMinWidth(90);//強制限制Label最小寬度,讓每個Label在變換語言時不會變動
            L_IP_Addr.setPrefWidth(90);//強制限制Label預設寬度,讓每個Label在變換語言時不會變動        
        }

        //L_IP_Addr.setPrefSize(90, 36);
       // L_IP_Addr.setMaxSize(90, 36);
        //L_IP_Addr.setMinSize(90, 36);
	MainGP.add(L_IP_Addr, 0, 2);
        L_IP_Addr.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        
        Colon1=new Label(":");//將":"固定在一個位置
        GridPane.setHalignment(Colon1, HPos.LEFT);
        MainGP.add(Colon1, 1, 2); 
        Colon1.setPadding(new Insets(0, 5, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)             

        /*******輸入VDI Server 的IP位址*******明碼 與 暗碼 轉換*************/        
        pwIP = new PasswordField();
         // Set initial state
        //pwIP.setManaged(false);
        //pwIP.setVisible(false);        
//	MainGP.add(pwIP,2, 2); // 2017.08.10 william IP增加port欄位，預設443 取消功能
        pwIP.setPrefSize(248, 39); // 2017.08.10 william IP增加port欄位，預設443 328 -> 248
        pwIP.setMaxSize(248, 39); // 2017.08.10 william IP增加port欄位，預設443 328 -> 248
        pwIP.setMinSize(248, 39); // 2017.08.10 william IP增加port欄位，預設443 328 -> 248
        
        IP_Addr = new TextField(CurrentIP);          
//	MainGP.add(IP_Addr,2, 2);
        IP_Addr.setPrefSize(248, 39); // 2017.08.10 william IP增加port欄位，預設443 328 -> 248
        IP_Addr.setMaxSize(248, 39); // 2017.08.10 william IP增加port欄位，預設443 328 -> 248
        IP_Addr.setMinSize(248, 39); // 2017.08.10 william IP增加port欄位，預設443 328 -> 248
        
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
        MainGP.add(IPBox, 2, 2,1,1);         
      
        //buttonIP.setVisible(false);    
        //buttonIP.setGraphic(new ImageView(EyeOpen));     
        //buttonIP.setStyle("-fx-background-color: white;  ");            
        MainGP.add(buttonIP,2, 2);      
        buttonIP.setAlignment(Pos.CENTER);        
        buttonIP.setTranslateX(329);  //移動TextField內的Button位置        
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
        if(!GB.winXP_ver) {
            userName = new Label(WordMap.get("Username"));//+":"
        }
        else {
            userName = new Label(WordMap.get("Username_XP"));
        }
        
        GridPane.setHalignment(userName, HPos.LEFT);         
	MainGP.add(userName, 0, 5);
        //userName.setPadding(new Insets(0, 0, 0, 10));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        if(!GB.winXP_ver) {
            userName.setMaxWidth(90);//強制限制Label最大寬度,讓每個Label在變換語言時不會變動
            userName.setMinWidth(90);//強制限制Label最小寬度,讓每個Label在變換語言時不會變動
            userName.setPrefWidth(90);//強制限制Label預設寬度,讓每個Label在變換語言時不會變動
        }
        else {
            userName.setMaxWidth(90);//強制限制Label最大寬度,讓每個Label在變換語言時不會變動
            userName.setMinWidth(90);//強制限制Label最小寬度,讓每個Label在變換語言時不會變動
            userName.setPrefWidth(90);//強制限制Label預設寬度,讓每個Label在變換語言時不會變動        
        }
        Colon2=new Label(":");//將":"固定在一個位置
        //Colon2.setPadding(new Insets(0, 0, 0, 0)); //設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        GridPane.setHalignment(Colon2, HPos.LEFT);        
        MainGP.add(Colon2, 1, 5);   
        
        // 2019.01.02 Remember10 username
        
        UsernameComboBox = new ComboBox(users);
             
        UsernameComboBox.setPrefSize(328, 39);
        UsernameComboBox.setMaxSize(328, 39);
        UsernameComboBox.setMinSize(328, 39);
        
        UsernameComboBox.setEditable(true);
        
//        UsernameComboBox.getSelectionModel().selectFirst();
        
        UsernameComboBox.setValue(CurrentUserName);
        
        MainGP.add(UsernameComboBox,2, 5);
        UsernameComboBox.setOnAction((event) -> {

        });          

         /**********輸入使用者帳號*********明碼 與 暗碼 轉換*************/
        pwName = new PasswordField();
        // Set initial state
        //pwName.setManaged(false);
        //pwName.setVisible(false);
	MainGP.add(pwName,2, 5);
        pwName.setPrefSize(328, 39);
        pwName.setMaxSize(328, 39);
        pwName.setMinSize(328, 39);
        
        userTextField = new TextField(CurrentUserName);      
//	MainGP.add(userTextField, 2, 5);
        userTextField.setPrefSize(328, 39);
        userTextField.setMaxSize(328, 39);
        userTextField.setMinSize(328, 39);
        
        //buttonUname.setVisible(false); 
        //buttonUname.setGraphic(new ImageView(EyeOpen));
        MainGP.add(buttonUname,2, 5);     
        //buttonUname.setStyle("-fx-background-color: white;");
        buttonUname.setAlignment(Pos.CENTER);       
        buttonUname.setTranslateX(329);  //移動TextField內的Button位置 
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
                            
//                            userTextField.managedProperty().bind(buttonUname.selectedProperty());
//                            userTextField.visibleProperty().bind(buttonUname.selectedProperty());
                            UsernameComboBox.managedProperty().bind(buttonUname.selectedProperty());
                            UsernameComboBox.visibleProperty().bind(buttonUname.selectedProperty());                            
                               
                            pwName.managedProperty().bind(buttonUname.selectedProperty().not());
                            pwName.visibleProperty().bind(buttonUname.selectedProperty().not());
                            
//                            userTextField.managedProperty().bind(buttonUname.selectedProperty());
//                            userTextField.visibleProperty().bind(buttonUname.selectedProperty());
                            UsernameComboBox.managedProperty().bind(buttonUname.selectedProperty());
                            UsernameComboBox.visibleProperty().bind(buttonUname.selectedProperty()); 
                                                        
                            buttonUname.setGraphic(new ImageView(EyeOpen));        
                            
                            UsernameComboBox.setDisable(false);
                        }   

                        Name_changeAfterSaved=true;   

                    }
//                    userTextField.textProperty().bindBidirectional(pwName.textProperty());           
                    UsernameComboBox.valueProperty().bindBidirectional(pwName.textProperty());
                    //System.out.println("Name_changeAfterSaved-final : "+Name_changeAfterSaved+"\n");
                    WriteLastChange(); //記錄明碼與暗碼 
                }
            });
        }

        /**********************  輸入密碼 ***********************/
        if(!GB.winXP_ver) {
            pw = new Label(WordMap.get("Password"));//+":"
        }
        else {
            pw = new Label(WordMap.get("Password_XP"));
        }
        
        GridPane.setHalignment(pw, HPos.LEFT);
	 MainGP.add(pw, 0, 8);
        //pw.setPadding(new Insets(0, 0, 0, 10));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)      
        if(!GB.winXP_ver) {
            pw.setMaxWidth(90);//強制限制Label最大寬度,讓每個Label在變換語言時不會變動
            pw.setMinWidth(90);//強制限制Label最小寬度,讓每個Label在變換語言時不會變動
            pw.setPrefWidth(90);//強制限制Label預設寬度,讓每個Label在變換語言時不會變動
        }
        else {
            pw.setMaxWidth(90);//強制限制Label最大寬度,讓每個Label在變換語言時不會變動
            pw.setMinWidth(90);//強制限制Label最小寬度,讓每個Label在變換語言時不會變動
            pw.setPrefWidth(90);//強制限制Label預設寬度,讓每個Label在變換語言時不會變動        
        }        
        
        
        Colon3=new Label(":");//將":"固定在一個位置      
        GridPane.setHalignment(Colon3, HPos.LEFT);
        MainGP.add(Colon3, 1, 8);
        
        /*******輸入密碼********明碼 與 暗碼 轉換*******************/       
    	pwBox = new PasswordField();        
	MainGP.add(pwBox,2, 8);
        pwBox.setPrefSize(328, 39);
        pwBox.setMaxSize(328, 39);
        pwBox.setMinSize(328, 39);

        // Bind the textField and passwordField text values bidirectionally.
        textField01 = new TextField();
        // Set initial state
        textField01.setManaged(false);
        textField01.setVisible(false);
        MainGP.add(textField01,2, 8); 
        textField01.setPrefSize(328, 39);
        textField01.setMaxSize(328, 39);
        textField01.setMinSize(328, 39);
        
        buttonPw.setGraphic(new ImageView(EyeClose));
        MainGP.add(buttonPw,2, 8);     
        //buttonPw.setStyle("-fx-background-color: white;");
        //buttonPw.visibleProperty().bind( pwBox.textProperty().isEmpty().not() );
        buttonPw.setAlignment(Pos.CENTER);
        buttonPw.setTranslateX(329); //移動TextField內的Button位置
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

                        changeAfterSaved=false;
                    } 

                    changeAfterSaved=false;

                }           
                else{

                    if (e.getEventType().equals(ActionEvent.ACTION)){                   

                        pwBox.managedProperty().bind(buttonPw.selectedProperty().not());
                        pwBox.visibleProperty().bind(buttonPw.selectedProperty().not());             
                        buttonPw.setGraphic(new ImageView(EyeClose));
             
                        changeAfterSaved=true;  
                    } 

                changeAfterSaved=true;

                }

                textField01.textProperty().bindBidirectional(pwBox.textProperty());                       
               // System.out.println("changeAfterSaved : "+changeAfterSaved+"\n");
         
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
        Login.setPrefSize(140,30);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        Login.setMaxSize(140,30);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        Login.setMinSize(140,30);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動
        Login.setAlignment(Pos.CENTER);
        
        checkBox_Pw = new CheckBox("  "+WordMap.get("Read_password"));        
        
        Login.setOnAction((ActionEvent event) -> {    
            Login_func(false, false); // 2019.01.02 view D Drive
        });
         
        /*******  以下擺放操作功能 *******/
        //HBox LoginBox=new HBox(Login);
        HBox LoginBox=new HBox();
        LoginBox.getChildren().addAll(Login,checkBox_Pw);
        LoginBox.setAlignment(Pos.CENTER_LEFT);
        LoginBox.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        LoginBox.setSpacing(15); 
        MainGP.add(LoginBox, 2, 11,1,1);//2,11,3,2
        
        /*****桌面雲管理*****/
        ManageVD=new Button(WordMap.get("ManageVD"));        
        ManageVD.setPrefSize(140,30);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        ManageVD.setMaxSize(140,30);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        ManageVD.setMinSize(140,30);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動
        ManageVD.setAlignment(Pos.CENTER);
        ManageVD.setOnAction((ActionEvent event) -> { 
            
            if(lock_ManageVD == false){
                lock_ManageVD=true;
            
                updateManageVD();            
                Boolean Result=PATTERN.matcher(IP_Addr.getText()).matches();
                System.out.println(" -----Result------"+Result+"\n");
                if(Result==true||IP_Addr!=null){ // 2017.07.21 william IP use domain name
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
                            // VDML.VDManagementLogin(IP_Addr.getText(),userTextField.getText(),pwBox.getText(),uniqueKey.toString(),CurrentIP_Port);  // 2017.08.10 william IP增加port欄位，預設443
                            VDML.VDManagementLogin(IP_Addr.getText(),UsernameComboBox.getValue().toString(),pwBox.getText(),uniqueKey.toString(),CurrentIP_Port); 

                            return null;
                        }
                    };
                    //new Thread(ManageVDTask).start();

                    ManageVDTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            System.out.println("** ManageVDTask.setOnFailed ** \n");
                            System.out.println("ManageVDTask Ping - testError = "+VDML.testError+"\n"); 
                            System.out.println("ManageVDTask - connCode = "+VDML.VDMLconnCode+"\n");
                            if(VDML.testError==false){
                                VDMLA.VDMLtestErrorAlert();
                            }
                            VDMLA.VDMLoginAlertChange(VDML.VDMLconnCode);
                            lock_ManageVD=false;
                            VDML.VDMLconnCode = 0; // 2017.09.18 william 錯誤修改(2)-2個警告訊息                 
                        }
                    });
                    //new Thread(ManageVDTask).start();

                    ManageVDTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            System.out.println("** ManageVDTask.setOnSucceeded ** \n");
                            // VDManagement VDM=new VDManagement(public_stage, WordMap, VDML.CurrentUserName, VDML.VdName, VDML.Suspend, VDML.VdStatus, VDML.IsVdOnline, VDML.CreateTime, VDML.Desc, VDML.CurrentIP, VDML.VdId, VDML.CurrentIP_Port);   // 2017.08.10 william IP增加port欄位，預設443
                            VDManagement VDM=new VDManagement(public_stage, WordMap, VDML.CurrentUserName,VDML.VDML_jsonArr, VDML.CurrentIP, VDML.CurrentIP_Port,pwBox.getText(),uniqueKey.toString()); // 2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD) , 2018.07.06 關機時，按強制關機按鈕不刪除VD列
                            lock_ManageVD=false;
                        }
                    });
                    new Thread(ManageVDTask).start();

                }else{
                    VDMLResultAlert();
                    lock_ManageVD=false;
                    System.out.println("** VDML else end ** \n");
                }
            
            }
        });
        
         /*****變更密碼****/
        ChangePW=new Button(WordMap.get("ChangePassword"));
        ChangePW.setPrefSize(150,30);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        ChangePW.setMaxSize(150,30);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        ChangePW.setMinSize(150,30);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動        
        ChangePW.setAlignment(Pos.CENTER);
        ChangePW.setOnAction((ActionEvent event) -> {
            
            if(lock_ChangePW==false){
                lock_ChangePW=true;
            
                updateChangePW();
                Boolean Result=PATTERN.matcher(IP_Addr.getText()).matches();
                System.out.println(" -----Result------"+Result+"\n");
                if(Result==true||IP_Addr!=null){ // 2017.07.21 william IP use domain name
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
                            System.out.println("** ChangePWTask.setOnFailed ** \n");
                            System.out.println("ChangePWTask Ping - testError = "+CPL.testError+"\n"); 
                            System.out.println("ChangePWTask - connCode = "+CPL.CPLconnCode+"\n");
                            if(CPL.testError==false){
                                CPLA.CPLtestErrorAlert();
                            }
                            CPLA.CPLoginAlertChange(CPL.CPLconnCode);
                            lock_ChangePW=false;
                            CPL.CPLconnCode = 0; // 2017.09.18 william 錯誤修改(2)-2個警告訊息                            
                        }
                    });
                    //new Thread(ChangePWTask).start();

                    ChangePWTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            System.out.println("** ChangePWTask.setOnSucceeded ** \n");
                            try {
                                ChangePassword CP;
                                CP = new ChangePassword(public_stage, WordMap,CPL.CurrentPasseard,CPL.CurrentIP,CPL.CurrentUserName,CPL.CurrentIP_Port); // 2017.08.10 william IP增加port欄位，預設443
                                lock_ChangePW=false;
                            } catch (IOException ex) {
                                Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    new Thread(ChangePWTask).start();

                }else{
                    CPLResultAlert();
                    lock_ChangePW=false;
                    System.out.println("** CP else end ** \n");
                }
            
            }
            
        });                
          
        /*****測試VDI伺服器*****/
        /*
        Check=new Button(WordMap.get("TestServer"));       
        Check.setOnAction((ActionEvent event) -> {
            try {
                PingServer PS=new PingServer(WordMap);
                PS.Ping(primaryStage, IP_Addr.getText());
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        */
        
        /*****離開*****/
        Leave=new Button(WordMap.get("Exit"));
        Leave.setPrefSize(60,30);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        Leave.setMaxSize(60,30);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        Leave.setMinSize(60,30);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動           
        Leave.setAlignment(Pos.CENTER);
        Leave.setOnAction((event) -> {
            updateLeave();
            ExitProcess();  
        });
                         
        HBox Operation=new HBox();
        Operation.setPadding(new Insets(1, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        //Operation.getChildren().addAll(ManageVD, ChangePW, Check, Leave);  
        Operation.getChildren().addAll(ManageVD, ChangePW, Leave);  
        Operation.setAlignment(Pos.CENTER_LEFT);
        Operation.setSpacing(10);     
        MainGP.add(Operation, 2, 13,1,1);//2,15,1,1
        
        /*******SnapShot******/
        // 2018.01.01 william SnapShot實作       
        SS = new Button(WordMap.get("SnapShot"));
        SS.setPrefSize(85,30); // 強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        SS.setMaxSize(85,30);  // 強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        SS.setMinSize(85,30);  // 強制限制button最小寬度/高度,讓每個button在變換語言時不會變動
        SS.setAlignment(Pos.CENTER);
        SS.setTranslateX(10);
        SS.setTranslateY(0);      
        SS.setOnAction((ActionEvent event) -> {
            Login_func(true, false); // 2019.01.02 view D Drive false (Snapshot) -> true (User disk)
        }); 

        HBox extra = new HBox();
        extra.setPadding(new Insets(1, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        extra.getChildren().addAll(SS);  
        extra.setAlignment(Pos.CENTER_LEFT);
        extra.setSpacing(10);     
        MainGP.add(extra, 0, 13,1,1);//2,15,1,1    
        
        /*****button 設置一行按鈕的大小一致*****/
        /*
        ManageVD.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        ChangePW.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        //Check.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Leave.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        TilePane tileButtons = new TilePane();//Orientation.HORIZONTAL
        tileButtons.setPadding(new Insets(10, 10, 10, 10));
        tileButtons.setHgap(8.0);
        tileButtons.setVgap(6.0);
        tileButtons.setAlignment(Pos.BOTTOM_LEFT);
        tileButtons.getChildren().addAll(ManageVD, ChangePW, Leave );   
        MainGP.add(tileButtons, 1, 11,1,1); 
        */
  
        /***  產生主頁視窗 ***/      
        if(!GB.winXP_ver) {
            scene = new Scene(rootPane, 550, 380);//550 350 // 2017.08.10 william 登入中thread畫面鎖住
        }
        else {
            scene = new Scene(rootPane, 550, 380);//550 350 // 2017.07.21 william 550 -> 600 // 2017.08.10 william 登入中thread畫面鎖住
        }
        scene.getStylesheets().add("vdi.css");
        MyTitle.setId("Title");
        ChangeLabelLang(L_IP_Addr, userName, pw, Language, Colon1, Colon2, Colon3); //變更Label語言字型
        ChangeButtonLang(Login, ManageVD, ChangePW, Leave, checkBox_Pw, SS); //變更Button語言字型 / 2018.01.01 william SnapShot實作
        IP_Addr.requestFocus();
        keydownup();
        // mousemove(); // 2018.07.06 textfield不focus在其他按鈕物件上
        buttonhover_focus();
        
        switch (GB.JavaVersion) {
            case 0:
                public_stage.getIcons().add(new Image("images/Icon2.png"));
                break;
            case 1:
                public_stage.getIcons().add(new Image("images/MCIcon2.png"));
                break;                
            default:
                break;
        } 
        if(!GB.winXP_ver) {
            public_stage.setTitle(WordMap.get("APP_Title")+"  for Win 10/8/7 32bit "+Ver); //視窗標題+版本
        }
        else {
            public_stage.setTitle(WordMap.get("APP_Title")+"  for Win XP 32bit "+Ver); //視窗標題+版本
        }
        public_stage.setScene(scene);
        public_stage.initStyle(StageStyle.DECORATED);
        public_stage.setResizable(false);
        public_stage.show();
        
        /*****關閉視窗"X"-會與離開按鍵一樣 可以關閉全部視窗 *****/
        public_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {  
              System.out.println("Stage is closing");
              ExitWindowProcess(we);
              //primaryStage.close();             
              System.out.println("----------end----------");
            }
        }); 
        //StartMigrationLock();
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
        ChangeButtonLang(Login, ManageVD, ChangePW, Leave, checkBox_Pw, SS); //變更Button語言字型 / 2018.01.01 william SnapShot實作
    }
    // Input：,  Output： , 功能： 變更物件文字內容
    public void SwitchLanguage(){
        Language.setText(WordMap.get("Language")+"  :");//+":"
        MyTitle.setText(WordMap.get("Title"));
        if(!GB.winXP_ver) {
            L_IP_Addr.setText(WordMap.get("IP_Addr"));//+":"
            userName.setText(WordMap.get("Username"));//+":"
            pw.setText(WordMap.get("Password"));//+":"
        }
        else {
            L_IP_Addr.setText(WordMap.get("IP_Addr_XP"));
            userName.setText(WordMap.get("Username_XP"));
            pw.setText(WordMap.get("Password_XP"));
        }
        Login.setText(WordMap.get("Login"));
        checkBox_Pw.setText("  "+WordMap.get("Read_password"));
        ManageVD.setText(WordMap.get("ManageVD"));
        ChangePW.setText(WordMap.get("ChangePassword"));
        //Check.setText(WordMap.get("TestServer"));
        Leave.setText(WordMap.get("Exit"));
        if(!GB.winXP_ver) {
            public_stage.setTitle(WordMap.get("APP_Title")+"  for Win 10/8/7 32bit "+Ver);
        }
        else {
            public_stage.setTitle(WordMap.get("APP_Title")+"  for Win XP 32bit "+Ver);
        }
        if( lock_Login == true ){ // 2017.08.10 william 登入中thread畫面鎖住    
            Login_title.setText(WordMap.get("Thread_Login")+"\n"+WordMap.get("Thread_Waiting"));
            System.out.print(" **Login ** SwitchLanguage** \n");
        }          
        SS.setText(WordMap.get("SnapShot")); // 2018.01.01 william SnapShot實作
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
            System.out.print(" SystemStatus.json 讀取失敗  \n"); 
           // e.printStackTrace();
        }
    }
    // Input：,  Output： , 功能： 寫入設定資訊( ip . 帳號)
    public void WriteLastStatus(){
        
        JSONObject SystemStatus=new JSONObject();
        
        if("繁體中文".equals(LangNow)){     //將SystemStatus.json檔,寫入"Language"時,使用英文(建立jar檔時,僅讀取英文,若有中文會有亂碼顯示)
            LangChange="TraditionalChinese";
            LangNow=LangChange;
        }
        if("简体中文".equals(LangNow)){     //將SystemStatus.json檔,寫入"Language"時,使用英文(建立jar檔時,僅讀取英文,若有中文會有亂碼顯示)
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
            System.out.print(" AfterChange.json 讀取失敗  \n");
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
//                if(!GB.winXP_ver) {
                    WordMap.put("IP_Addr", LMF.get("IP_Addr").toString());
                    WordMap.put("Username", LMF.get("Username").toString());
                    WordMap.put("Password", LMF.get("Password").toString());                
//                }
//                else {
                    WordMap.put("IP_Addr_XP", LMF.get("IP_Addr_XP").toString());
                    WordMap.put("Username_XP", LMF.get("Username_XP").toString());
                    WordMap.put("Password_XP", LMF.get("Password_XP").toString());                  
//                }
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
                WordMap.put("VD_SN", LMF.get("VD_SN").toString());
                WordMap.put("Message_Error_VD_503", LMF.get("Message_Error_VD_503").toString());
                WordMap.put("Message_Error_VD_406", LMF.get("Message_Error_VD_406").toString());
                WordMap.put("Message_Error_VD_412", LMF.get("Message_Error_VD_412").toString());
                WordMap.put("Message_Error_VD_500", LMF.get("Message_Error_VD_500").toString());
                WordMap.put("Message_Error_VD_403", LMF.get("Message_Error_VD_403").toString());
                WordMap.put("Message_Error_VD_417", LMF.get("Message_Error_VD_417").toString());
                WordMap.put("Message_Error_VD_410", LMF.get("Message_Error_VD_410").toString());
                WordMap.put("Message_Error_setVDonline", LMF.get("Message_Error_setVDonline").toString());                
                // 2017.08.10 william 登入中thread畫面鎖住    
                WordMap.put("Thread_Login", LMF.get("Thread_Login").toString());
                WordMap.put("Thread_Waiting", LMF.get("Thread_Waiting").toString()); 
                //  2017.08.10 william 400錯誤處理
                WordMap.put("Message_Bad_Request", LMF.get("Message_Bad_Request").toString());   
                WordMap.put("Message_Error_400", LMF.get("Message_Error_400").toString());    
                //  2017.09.18 william 錯誤修改(4) 其他未知的錯誤
                WordMap.put("Message_Unknown_system_error", LMF.get("Message_Unknown_system_error").toString());          
                // 2018.01.01 william 單一帳號多個VD登入
                WordMap.put("Select_VD", LMF.get("Select_VD").toString());
                WordMap.put("LoginMultiVD_desc1", LMF.get("LoginMultiVD_desc1").toString());
                WordMap.put("LoginMultiVD_desc2", LMF.get("LoginMultiVD_desc2").toString());
                WordMap.put("LoginMultiVD_Mark", LMF.get("LoginMultiVD_Mark").toString()); 
                // 2018.01.01 william SnapShot實作
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
                WordMap.put("SnapShot", LMF.get("SnapShot").toString());
                WordMap.put("VM_Migrate", LMF.get("VM_Migrate").toString());
                WordMap.put("VM_Preparing_Boot", LMF.get("VM_Preparing_Boot").toString());      
                WordMap.put("Message_Error_VD_428", LMF.get("Message_Error_VD_428").toString());
                WordMap.put("Message_Error_VD_429", LMF.get("Message_Error_VD_429").toString());         
                WordMap.put("Snapshot_Rollback", LMF.get("Snapshot_Rollback").toString()); // 2018.07.06 快照新增還原按鈕               
                WordMap.put("Snapshot_RollBack_Confirm", LMF.get("Snapshot_RollBack_Confirm").toString()); // 2018.07.12 還原按鈕和修改dialog
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
                // 2019.03.28 VM/VD migration reconnect
                WordMap.put("Str_Dynamic_Transfer", LMF.get("Str_Dynamic_Transfer").toString());
                WordMap.put("Str_Dynamic_Transfer_Reconnect", LMF.get("Str_Dynamic_Transfer_Reconnect").toString());                
                // 2019.04.16 Ping IP方式修改                
                WordMap.put("Message_SpiceError_ConnectTimeout", LMF.get("Message_SpiceError_ConnectTimeout").toString());                                                                
                WordMap.put("Desc_Snapshot_operation3", LMF.get("Desc_Snapshot_operation3").toString());                    
                WordMap.put("Desc_ViewUserDisk_operation3", LMF.get("Desc_ViewUserDisk_operation3").toString());                                                    
                WordMap.put("Desc_ViewUserDisk_operation1-1", LMF.get("Desc_ViewUserDisk_operation1-1").toString());                                                                    
                WordMap.put("Message_Error_WinXP_RDPFail", LMF.get("Message_Error_WinXP_RDPFail").toString());  
            }else{
                WordMap.put("APP_Title", "AcroRed AcroViewer"); // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP
                WordMap.put("Language", "Language");
                WordMap.put("Title", "AcroViewer"); // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP
//                if(!GB.winXP_ver) {
                    WordMap.put("IP_Addr", "IP");
                    WordMap.put("Username", "Username");
                    WordMap.put("Password", "Password");
//                }
//                else {
                    WordMap.put("IP_Addr_XP", "IP");
                    WordMap.put("Username_XP", "Username");
                    WordMap.put("Password_XP", "Password");                
//                }
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
                WordMap.put("ExitMassage", "Do you want to exit VDI Viewer？");
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
                WordMap.put("VD_SN", "SN");
                WordMap.put("Message_Error_VD_503", "Login failed, out of Resources!");
                WordMap.put("Message_Error_VD_406", "Login failed, too many users!");
                WordMap.put("Message_Error_VD_412", "Login failed, out of memory!");
                WordMap.put("Message_Error_VD_500", "Login failed, System Error!");
                WordMap.put("Message_Error_VD_403", "Login failed, VD disabled!");
                WordMap.put("Message_Error_VD_417", "Login failed,  VD in Task!");
                WordMap.put("Message_Error_VD_410", "Login failed, VD server not available!");
                WordMap.put("Message_Error_setVDonline", "The server IP can not connect!");
                // 2017.08.10 william 登入中thread畫面鎖住    
                WordMap.put("Thread_Login", "Loading");
                WordMap.put("Thread_Waiting", "Please wait");  
                //  2017.08.10 william 400錯誤處理
                WordMap.put("Message_Bad_Request", "There is a problem with the server！");   
                WordMap.put("Message_Error_400", "Please contact the information staff！");      
                //  2017.09.18 william 錯誤修改(4) 其他未知的錯誤
                WordMap.put("Message_Unknown_system_error", "Unknown system error");      
                // 2018.01.01 william 單一帳號多個VD登入
                WordMap.put("Select_VD", "Login VD");    
                WordMap.put("LoginMultiVD_desc1", "1. Please select the VD and click confirm to login VD."); 
                WordMap.put("LoginMultiVD_desc2", "2. Or click the Exit button to close the window."); 
                WordMap.put("LoginMultiVD_Mark", "Mark"); 
                // 2018.01.01 william SnapShot實作
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
                WordMap.put("SnapShot", "SnapShot");    
                WordMap.put("VM_Migrate", "Migrating");
                WordMap.put("VM_Preparing_Boot", "Preparing Boot");      
                WordMap.put("Message_Error_VD_428", "VD is preparing boot!");
                WordMap.put("Message_Error_VD_429", "VD is migrating!");       
                WordMap.put("Snapshot_Rollback", "Rollback"); // 2018.07.06 快照新增還原按鈕
                WordMap.put("Snapshot_RollBack_Confirm", "Are you sure rollback designated snapshot layer？"); //  2018.07.12 還原按鈕和修改dialog
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
                WordMap.put("Message_RdpError_ConnectTimeout","RDP connection timeout.");
                WordMap.put("Message_RdpSuggest_Relogin", "Please log in again.");                 
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
                // 2019.03.28 VM/VD migration reconnect
                WordMap.put("Str_Dynamic_Transfer", "Dynamic Transfer");  
                WordMap.put("Str_Dynamic_Transfer_Reconnect", "Reconnecting");      
                // 2019.04.16 Ping IP方式修改                
                WordMap.put("Message_SpiceError_ConnectTimeout", "AcroSpice connection timeout！");                                                                
                WordMap.put("Desc_Snapshot_operation3", "");                    
                WordMap.put("Desc_ViewUserDisk_operation3", "");    
                WordMap.put("Desc_ViewUserDisk_operation1-1", "");  
                WordMap.put("Message_Error_WinXP_RDPFail", "This OS cannot use the RDP protocol！");
            }
        }catch(IOException e){
           // e.printStackTrace();
                WordMap.put("APP_Title", "AcroRed AcroViewer"); // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP
                WordMap.put("Language", "Language");
                WordMap.put("Title", "AcroViewer"); // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP
//                if(!GB.winXP_ver) {
                    WordMap.put("IP_Addr", "IP");
                    WordMap.put("Username", "Username");
                    WordMap.put("Password", "Password");
//                }
//                else {
                    WordMap.put("IP_Addr_XP", "IP");
                    WordMap.put("Username_XP", "Username");
                    WordMap.put("Password_XP", "Password");                
//                }                
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
                WordMap.put("ExitMassage", "Do you want to exit VDI Viewer？");
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
                WordMap.put("VD_SN", "SN");
                WordMap.put("Message_Error_VD_503", "Login failed, out of Resources!");
                WordMap.put("Message_Error_VD_406", "Login failed, too many users!");
                WordMap.put("Message_Error_VD_412", "Login failed, out of memory!");
                WordMap.put("Message_Error_VD_500", "Login failed, System Error!");
                WordMap.put("Message_Error_VD_403", "Login failed, VD disabled!");
                WordMap.put("Message_Error_VD_417", "Login failed,  VD in Task!");
                WordMap.put("Message_Error_VD_410", "Login failed, VD server not available!");
                WordMap.put("Message_Error_setVDonline", "The server IP can not connect!");
                // 2017.08.10 william 登入中thread畫面鎖住    
                WordMap.put("Thread_Login", "Loading");
                WordMap.put("Thread_Waiting", "Please wait");    
                //  2017.08.10 william 400錯誤處理
                WordMap.put("Message_Bad_Request", "There is a problem with the server！");   
                WordMap.put("Message_Error_400", "Please contact the information staff！");  
                //  2017.09.18 william 錯誤修改(4) 其他未知的錯誤
                WordMap.put("Message_Unknown_system_error", "Unknown system error");       
                // 2018.01.01 william 單一帳號多個VD登入
                WordMap.put("Select_VD", "Login VD");    
                WordMap.put("LoginMultiVD_desc1", "1. Please select the VD and click confirm to login VD."); 
                WordMap.put("LoginMultiVD_desc2", "2. Or click the Exit button to close the window."); 
                WordMap.put("LoginMultiVD_Mark", "Mark"); 
                // 2018.01.01 william SnapShot實作
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
                WordMap.put("SnapShot", "SnapShot");          
                WordMap.put("VM_Migrate", "Migrating");
                WordMap.put("VM_Preparing_Boot", "Preparing Boot");      
                WordMap.put("Message_Error_VD_428", "VD is preparing boot!");
                WordMap.put("Message_Error_VD_429", "VD is migrating!");   
                WordMap.put("Snapshot_Rollback", "Rollback"); // 2018.07.06 快照新增還原按鈕
                WordMap.put("Snapshot_RollBack_Confirm", "Are you sure rollback designated snapshot layer？"); // 2018.07.12 還原按鈕和修改dialog
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
                WordMap.put("Message_RdpError_ConnectTimeout","RDP connection timeout.");
                WordMap.put("Message_RdpSuggest_Relogin", "Please log in again.");                 
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
                // 2019.03.28 VM/VD migration reconnect
                WordMap.put("Str_Dynamic_Transfer", "Dynamic Transfer");  
                WordMap.put("Str_Dynamic_Transfer_Reconnect", "Reconnecting");      
                // 2019.04.16 Ping IP方式修改                
                WordMap.put("Message_SpiceError_ConnectTimeout", "AcroSpice connection timeout！");                  
                WordMap.put("Desc_Snapshot_operation3", "");                    
                WordMap.put("Desc_ViewUserDisk_operation3", "");      
                WordMap.put("Desc_ViewUserDisk_operation1-1", "");  
                WordMap.put("Message_Error_WinXP_RDPFail", "This OS cannot use the RDP protocol！");
        }
    }
    // Input：,  Output： , 功能： 結束AcroViewer應用程式
    public void ExitProcess(){
        final Alert alert = new Alert(AlertType.CONFIRMATION); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
        alert.setTitle("Exit Confirmation"); //設定對話框視窗的標題列文字
        alert.setHeaderText(""); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        alert.setContentText(WordMap.get("ExitMassage")); //設定對話框的訊息文字
        ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType(WordMap.get("Alert_Cancel"),ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOK,buttonTypeCancel); 
//        alert.show();
//        alert.resultProperty().addListener((observable,oldValue,newValue)->{
//            if ( newValue == buttonTypeOK ){
//                WriteLastStatus();  //紀錄設定的程式語言 
//                /*
//                if(QM.process!=null){             
//                   for(Process p : processes) {
//                        p.destroy();
//                     }
//                } 
//                */
//                System.out.print("button OK  action\n");
//
//                Platform.exit();
//                System.out.print(" Platform.exit()\n");
//
//                public_stage.close();
//                System.out.print("public_stage.close()\n");
//
//                //System.exit(0); // 結束程式 
//                //System.out.print("System.exit(0)n\n");
//
//                System.exit(1);
//                System.out.print("System.exit(1)\n");
//
//            }else{                 
//                System.out.print("button cancel action\n");
//            }
//        });        
        
        final Optional<ButtonType> opt = alert.showAndWait();
        final ButtonType rtn = opt.get(); //可以直接用「alert.getResult()」來取代
        if (rtn == buttonTypeOK) {   //若使用者按下「確定」
            WriteLastStatus();  //紀錄設定的程式語言 
            /*
            if(QM.process!=null){             
               for(Process p : processes) {
                    p.destroy();
                 }
            } 
            */
            System.out.print("button OK  action\n");
            
            Platform.exit();
            System.out.print(" Platform.exit()\n");

            public_stage.close();
            System.out.print("public_stage.close()\n");
            
            //System.exit(0); // 結束程式 
            //System.out.print("System.exit(0)n\n");
            
            System.exit(1);
            System.out.print("System.exit(1)\n");
            
        } 
         else{ 
            System.out.print("button cancel action\n");
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
           
            //System.exit(0); // 結束程式     0         
            //System.out.print("System.exit(0)n\n");
            
            System.exit(1);      
            System.out.print("System.exit(1)\n");
            
        }         
        else{
            we.consume();
            //System.out.print("button cancel action\n");
        }

    }     
    // Input：IP標籤, 使用者標籤, 密碼標籤, 語言標籤, ；標籤, ；標籤, ；標籤 ,  Output： , 功能： 變更Label字型,字型:中文-標楷體,英文:Times New Roman (RUN WinXP版 需把Win7版隱藏才可顯示出字型)
     public void ChangeLabelLang(Label lab01, Label lab02, Label lab03, Label lab04, Label col01, Label col02, Label col03){
        //Label "IP", "UserName", "Password", "Language"
        if("English".equals(LangNow)){
            lab01.setStyle("-fx-font-family:'Times New Roman'; -fx-font-size: 19px; -fx-font-weight: 900;");//21
            lab02.setStyle("-fx-font-family:'Times New Roman'; -fx-font-size: 19px; -fx-font-weight: 900;");
            lab03.setStyle("-fx-font-family:'Times New Roman'; -fx-font-size: 19px; -fx-font-weight: 900;");
            lab04.setStyle("-fx-font-family:'Times New Roman'; -fx-font-size: 19px; -fx-font-weight: 900;");//17
            
            //winXP 僅能用中文名稱
            //冒號":"-winXP
            col01.setStyle("-fx-font-family:'微軟正黑體'; -fx-font-size: 17px; -fx-font-weight: 900;");
            col02.setStyle("-fx-font-family:'微軟正黑體'; -fx-font-size: 17px; -fx-font-weight: 900;");
            col03.setStyle("-fx-font-family:'微軟正黑體'; -fx-font-size: 17px; -fx-font-weight: 900;");
            
            //win7以上僅能用英文名稱
            //冒號":"-win7
            col01.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 17px; -fx-font-weight: 900;");
            col02.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 17px; -fx-font-weight: 900;");
            col03.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 17px; -fx-font-weight: 900;");
            
        }        
        else{//if(("繁體中文".equals(LangNow))||("TraditionalChinese".equals(LangNow))||("简体中文".equals(LangNow))||("SimpleChinese".equals(LangNow))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/
         
            //winXP 僅能用中文名稱
            lab01.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-size: 17px; -fx-font-weight: 900;");//20           
            lab02.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-size: 17px; -fx-font-weight: 900;");
            lab03.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-size: 17px; -fx-font-weight: 900;");
            lab04.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-size: 17px; -fx-font-weight: 900;");//17
            //冒號":"
            col01.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-size: 17px; -fx-font-weight: 900;");
            col02.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-size: 17px; -fx-font-weight: 900;");
            col03.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-size: 17px; -fx-font-weight: 900;");
            
            //win7以上僅能用英文名稱
            lab01.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 17px; -fx-font-weight: 900;");//20
            lab02.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 17px; -fx-font-weight: 900;");
            lab03.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 17px; -fx-font-weight: 900;");
            lab04.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 18px; -fx-font-weight: 900;");
            //冒號":"
            col01.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 17px; -fx-font-weight: 900;");
            col02.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 17px; -fx-font-weight: 900;");
            col03.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 17px; -fx-font-weight: 900;");
            
        }
       
     }
    // Input：主頁面的所有button ,  Output： , 功能： 變更Label字型,字型:中文-標楷體,英文:Times New Roman
      public void ChangeButtonLang(Button but01, Button but02, Button but03, Button but04, CheckBox box, Button ss){ // 2018.01.01 william SnapShot實作
        //Button "Login", "ManagerVD", "ChangePassword", "Leave"
        if("English".equals(LangNow)){
            but01.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
            but02.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
            but03.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
            but04.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
            box.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
            ss.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
        }        
        else { //if(("繁體中文".equals(LangNow))||("TraditionalChinese".equals(LangNow))||("简体中文".equals(LangNow))||("SimpleChinese".equals(LangNow))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/
            //winXP 僅能用中文名稱
            but01.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-weight: 900;");
            but02.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-weight: 900;");
            but03.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-weight: 900;");
            but04.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-weight: 900;");
            if(("日本語".equals(LangNow)))
                box.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-weight: 900;-fx-font-size: 14px;");
            else
                box.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-weight: 900;");            
            ss.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-weight: 900;");            
            //win7以上僅能用英文名稱
            but01.setStyle("-fx-font-family:'Microsoft JhengHei';");
            but02.setStyle("-fx-font-family:'Microsoft JhengHei';");
            but03.setStyle("-fx-font-family:'Microsoft JhengHei';");
            but04.setStyle("-fx-font-family:'Microsoft JhengHei';");   
            if(("日本語".equals(LangNow)))
                box.setStyle("-fx-font-family:'Microsoft JhengHei';-fx-font-size: 14px;"); 
            else
                box.setStyle("-fx-font-family:'Microsoft JhengHei';");             
            ss.setStyle("-fx-font-family:'Microsoft JhengHei';");             
        }
        
    }
      // Input： ,  Output： , 功能： 變更登入按鈕的ID及屬性  
    private void updateLogin() { 
        //System.out.print("******** IN Login ********\n");
        Login_Color = true;
        ManageVD_Color = false;
        ChangePW_Color = false;
        Leave_Color = false;
        SS_Color = false; // 2018.01.01 william SnapShot實作
        if(Login_Color == true){
            Login.setId("Button_Login");
            ManageVD.setId("button");
            ChangePW.setId("button");
            Leave.setId("button");
            SS.setId("button"); // 2018.01.01 william SnapShot實作
        }  
        //System.out.print("******** OFF Login ********\n");     
    }
    // Input： ,  Output： , 功能： 變更雲桌面管理按鈕的ID及屬性
    private void updateManageVD() {
        ManageVD_Color = true;
        Login_Color = false;
        ChangePW_Color = false;
        Leave_Color = false;
        SS_Color = false; // 2018.01.01 william SnapShot實作
        if(ManageVD_Color == true){
            ManageVD.setId("Button_ManageVD");
            Login.setId("button");
            ChangePW.setId("button");
            Leave.setId("button");
            SS.setId("button"); // 2018.01.01 william SnapShot實作
        }
    }
    // Input： ,  Output： , 功能： 變更修改密碼按鈕的ID及屬性
    private void updateChangePW() {
        ChangePW_Color = true;
        Login_Color = false;
        ManageVD_Color = false;
        Leave_Color = false;
        SS_Color = false; // 2018.01.01 william SnapShot實作
        if(ChangePW_Color == true){
            ChangePW.setId("Button_ChangePW");
            Login.setId("button");
            ManageVD.setId("button");
            Leave.setId("button");
            SS.setId("button"); // 2018.01.01 william SnapShot實作
        }
    }
    // Input： ,  Output： , 功能： 變更離開按鈕的ID及屬性
    private void updateLeave() {
        Leave_Color = true;
        Login_Color = false;
        ManageVD_Color = false;
        ChangePW_Color = false;
        SS_Color = false; // 2018.01.01 william SnapShot實作
        if(Leave_Color == true){
            Leave.setId("Button_Leave");
            Login.setId("button");
            ManageVD.setId("button");
            ChangePW.setId("button");
            SS.setId("button"); // 2018.01.01 william SnapShot實作
        }
    }    
    // Input： ,  Output： , 功能： 變更快照按鈕的ID及屬性
    private void updateSS() {
        SS_Color = true;
        Leave_Color = false;
        Login_Color = false;
        ManageVD_Color = false;
        ChangePW_Color = false;
        if(SS_Color == true){            
            SS.setId("Button_SS");
            Leave.setId("button");
            Login.setId("button");
            ManageVD.setId("button");
            ChangePW.setId("button");
        }
    }            
    // Input： ,  Output： , 功能： Login 錯誤訊息 (-----目前無使用-----)
    public void LoginResultAlert(){        
        if("English".equals(WordMap.get("SelectedLanguage"))){
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("Message_Login"));
            alert_error.setHeaderText(null);
            if(!GB.winXP_ver) {
                //For Win7 - Dialog 
                alert_error.getDialogPane().setMinSize(450,Region.USE_PREF_SIZE);
                alert_error.getDialogPane().setMaxSize(450,Region.USE_PREF_SIZE);            
            }
            else{
                //For XP - Dialog 
                alert_error.getDialogPane().setMinSize(500,Region.USE_PREF_SIZE);
                alert_error.getDialogPane().setMaxSize(500,Region.USE_PREF_SIZE);
            }

            alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_IP_01")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_07")
                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK); 
            alert_error.showAndWait();
            //alert_error.show();            
        }          
        else {//if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){       
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("Message_Login"));
            alert_error.setHeaderText(null);
            if(!GB.winXP_ver) {
                alert_error.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            }
            else {
                //For XP - Dialog 
                alert_error.getDialogPane().setMinSize(450,Region.USE_PREF_SIZE);
                alert_error.getDialogPane().setMaxSize(450,Region.USE_PREF_SIZE);            
            }
            alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_IP_01")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_07")
                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK); 
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
            alert_error.showAndWait();
            //alert_error.show();
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){       
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("ManageVD_Title"));
            alert_error.setHeaderText(null);
            if(!GB.winXP_ver) {
                alert_error.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            }
            else {
                //For XP - Dialog 
                alert_error.getDialogPane().setMinSize(500,Region.USE_PREF_SIZE);
                alert_error.getDialogPane().setMaxSize(500,Region.USE_PREF_SIZE);            
            }
            
            alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VM_IP_01")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_07")
                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK); 
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
            alert_error.showAndWait();
            //alert_error.show();
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){       
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("CP_Window_Title"));
            alert_error.setHeaderText(null);
            
            if(!GB.winXP_ver) {
                alert_error.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            }
            else {
                //For XP - Dialog 
                alert_error.getDialogPane().setMinSize(500,Region.USE_PREF_SIZE);
                alert_error.getDialogPane().setMaxSize(500,Region.USE_PREF_SIZE);
            }
            
            alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_CP_IP_01")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_07")
                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK); 
            alert_error.showAndWait();
            //alert_error.show();
        }            
    }    
   // Input： ,  Output： , 功能： VD已經有人連線中，他人在登入時，會跳出視窗詢問是否還要連線
    public void IsVdOnlineAlert() throws IOException, MalformedURLException, InterruptedException{        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("VD Online");//設定對話框視窗的標題列文字  , "您真的要結束程式嗎？", ButtonType.YES, ButtonType.NO
        //alert.setHeaderText(WordMap.get("Message")+" : "+" VD is Online!! ");//設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        alert.setHeaderText(null);
        alert.getDialogPane().setMinSize(500,Region.USE_PREF_SIZE);
        alert.getDialogPane().setMaxSize(500,Region.USE_PREF_SIZE);
        alert.setContentText(WordMap.get("Message_VD_Online"));//VD 已上線,是否還要連線,若連線,則前一個連線會自動斷線.
        ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType(WordMap.get("Alert_Cancel"),ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel); 
        
        Optional<ButtonType> result01 = alert.showAndWait();
            if (result01.get() == buttonTypeOK){                    
                //QM.DoChcekServerAddressAvailabilityGet(QM.Address,QM.Port);
                QM.SetVDOnline(QM.Address,  QM.VdId, QM.CurrentUserName, QM.CurrentPasseard, QM.uniqueKey,QM.CurrentIP_Port);
                /*===============================================錯誤修改(3)-清除密碼 for windows===============================================*/
                ClearPW_Login_flag = true;  
//                ClearPW_first_start = false; 
                CheckUserIsOnline(); 
                /*===============================================錯誤修改(3)-清除密碼 for windows===============================================*/                    
                // ... user chose OK                         
//                File f = new File("jsonfile/SpiceText.txt");       
//                if(f.exists()){         
//                    //String[] params = new String [2]; 
//                    QM.params = new String [2];      
//                    //32bit版
//                    //QM.params[0] = "VirtViewer v3.1_32bit_nousb\\bin\\remote-viewer.exe";//32bit版
//                    //64bit版
//                    QM.params[0] = "VirtViewer v3.1_64bit\\bin\\remote-viewer.exe";//64bit版
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
            
//        alert.show();
//        alert.resultProperty().addListener((observable,oldValue,newValue)->{
//            if ( newValue == buttonTypeOK ){                        
//                try {
//                    QM.DoChcekServerAddressAvailabilityGet(QM.Address,QM.Port);
//                    QM.SetVDOnline(QM.Address,  QM.VdId, QM.CurrentUserName, QM.CurrentPasseard, QM.uniqueKey);
//                    // ... user chose OK                         
//                    File f = new File("jsonfile/SpiceText.txt");       
//                    if(f.exists()){         
//                        //String[] params = new String [2]; 
//                        QM.params = new String [2];      
//                        //32bit版
//                        //params[0] = "VirtViewer v3.1_32bit_nousb\\bin\\remote-viewer.exe";//32bit版
//                        //64bit版
//                        QM.params[0] = "VirtViewer v3.1_64bit\\bin\\remote-viewer.exe";//64bit版
//                        QM.params[1] = f.toString();                          
//                        QM.process =Runtime.getRuntime().exec(QM.params);        
//                        QM.process=null;
//                        System.gc();//清除系統垃圾
//
//                    }else{
//                            System.out.print("remote-viewer.exe is not existed.\n");
//                    } 
//                } catch (IOException ex) {
//                    Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//            }else {
//                // ... user chose CANCEL or closed the dialog
//                alert.close();
//            } 
//        });       
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
    // Input： ,  Output： , 功能：  控制鍵盤與滑鼠 觸碰button的反應 (-----目前無使用-----)
    public void buttonhover_focus(){       
        Login.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if(Login.getId()=="Button_Login"){
                    //Login.setId("Button_actionON");
                }else{
                    Login.setId("button");
                }
                //System.out.print("**Login IN**\n");
                
            } if (oldValue){                
                if(Login.getId()=="Button_Login"){
                    //Login.setId("Button_actionON");
                }else{
                    Login.setId("button");
                }               
                //System.out.print("**Login OUT**\n");                
            }
            if(Login.isFocused()){                
                //System.out.print("--Login.isFocused()--\n");
                //System.out.print("**Login isFocused** : "+ Login.getId() +"\n");
                
                if(ManageVD.getId()=="Button_ManageVD"){
                    //ManageVD.setId("Button_actionON");
                    Login.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");
                }
                if(Login.getId()=="Button_Login"){
                    //Login.setId("Button_actionON");
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");           
                }
                if(ChangePW.getId()=="Button_ChangePW"){
                    //ChangePW.setId("Button_actionON");
                    Login.setId("button");
                    ManageVD.setId("button");
                    Leave.setId("button");
                }
                if(Leave.getId()=="Button_Leave"){
                    //Leave.setId("Button_actionON");
                    Login.setId("button");
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                }
                if(Login.getId()=="button"){
                    if(ManageVD.getId() != "Button_ManageVD"){
                        if(ChangePW.getId() != "Button_ChangePW"){
                            if(Leave.getId() != "Button_Leave"){
                                //Login.setId("Button_hover");
                                ManageVD.setId("button");
                                ChangePW.setId("button");
                                Leave.setId("button");
                            } 
                        }    
                    }            
                }

//                if(Login_Color==true){
//                    if(Login.getId()=="Button_Login"){
//                        if(ManageVD.getId() != "Button_ManageVD"){
//                            if(ChangePW.getId() != "Button_ChangePW"){
//                                if(Leave.getId() != "Button_Leave"){
//                                    //Login.setId("Button_hover");
//                                    ManageVD.setId("button");
//                                    ChangePW.setId("button");
//                                    Leave.setId("button");
//                                } 
//                            }    
//                        }            
//                    }
//                    
//                }
//                if(Login.getId()=="Button_Login"){
//                    if(ManageVD.getId() != "Button_ManageVD"){
//                        if(ChangePW.getId() != "Button_ChangePW"){
//                            if(Leave.getId() != "Button_Leave"){
//                                ManageVD.setId("button");
//                                ChangePW.setId("button");
//                                Leave.setId("button");                                      
//                                System.out.print("** not enter ** \n");
//                                Login.setOnKeyTyped((event)-> {
//                                    System.out.print("** enter key 000 ** \n");                                    
//                                    if(Login.getId()=="Button_Login"){
//                                        if(ManageVD.getId() != "Button_ManageVD"){
//                                            if(ChangePW.getId() != "Button_ChangePW"){
//                                                if(Leave.getId() != "Button_Leave"){
//                                                    System.out.print("** Typed 001 ** \n");
//                                                    Login.setId("button");
//                                                    
//                                                } 
//                                            }    
//                                        } 
//                                    }     
//                                    System.out.print("--Login--Login.isFocused()--::"+ Login.getId() +"\n");
//                                }); 
//                                if(!Login.isFocused()){ 
//                                    System.out.print("** Login.NOT isFocused() ** \n"); 
//                                    Login.setOnKeyReleased((event)-> {
//                                    System.out.print("** NOT Typed 001 ** \n");
//                                    if(Login.getId()=="button"){
//                                        if(ManageVD.getId() != "Button_ManageVD"){
//                                            if(ChangePW.getId() != "Button_ChangePW"){
//                                                if(Leave.getId() != "Button_Leave"){
//                                                    System.out.print("** NOT Typed 002 ** \n");
//                                                    Login.setId("Button_Login"); 
//                                                } 
//                                            }    
//                                        }       
//                                    } 
//                                    System.out.print("--Login.NOT--Login.isFocused()--::"+ Login.getId() +"\n");
//                                });  
//                                }
//                            } 
//                        }    
//                    }       
//                }  
            }
        });
        
        Login.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if(Login.getId()=="Button_Login"){
                    //Login.setId("Button_actionON");
                }else{
                    Login.setId("button");
                }
                //System.out.print("**Login IN**\n");
                
            } if (oldValue){                
                if(Login.getId()=="Button_Login"){
                    //Login.setId("Button_actionON");
                }else{
                    Login.setId("button");
                }               
                //System.out.print("**Login OUT**\n");                
            }
            //System.out.print("**Login ** : "+ Login.getId() +"\n");
            if(Login.isHover()){                               
                //System.out.print("--Login.isHover()--\n");
                //System.out.print("**Login isHover ** : "+ Login.getId() +"\n");                
                if(ManageVD.getId()=="Button_ManageVD"){
                    //ManageVD.setId("Button_actionON");
                    Login.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");
                }
                if(Login.getId()=="Button_Login"){
                    //Login.setId("Button_actionON");
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");    
                }    
                if(ChangePW.getId()=="Button_ChangePW"){
                    //ChangePW.setId("Button_actionON");
                    Login.setId("button");
                    ManageVD.setId("button");
                    Leave.setId("button");
                }
                if(Leave.getId()=="Button_Leave"){
                    //Leave.setId("Button_actionON");
                    Login.setId("button");
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                }
                if(Login.getId()=="button"){
                    if(ManageVD.getId() != "Button_ManageVD"){
                        if(ChangePW.getId() != "Button_ChangePW"){
                            if(Leave.getId() != "Button_Leave"){
                                ManageVD.setId("button");
                                ChangePW.setId("button");
                                Leave.setId("button");
                                //Login.setId("button");
                            } 
                        }    
                    }       
                } 
                /*** button 在第一次 Mouse-in 後 皆會直接進入 setOnMouseMoved ; 因此在mouse-in時加入條件  ***/
                /*** button 在第一次 Mouse-out 後 皆會直接進入 setOnMouseExited ; 因此在mouse-out時加入條件  ***/
                if(Login.getId()=="Button_Login"){
                    if(ManageVD.getId() != "Button_ManageVD"){
                        if(ChangePW.getId() != "Button_ChangePW"){
                            if(Leave.getId() != "Button_Leave"){
                                ManageVD.setId("button");
                                ChangePW.setId("button");
                                Leave.setId("button");                                
                                Login.setOnMouseMoved((event)-> {
                                    //System.out.print("** Moved 003 ** \n");
                                    if(Login_Color == true){
                                        Login.setId("button");
                                    }
//                                    if(Login.getId()=="Button_Login"){
//                                        if(ManageVD.getId() != "Button_ManageVD"){
//                                            if(ChangePW.getId() != "Button_ChangePW"){
//                                                if(Leave.getId() != "Button_Leave"){
//                                                    //System.out.print("** Moved 004 ** \n");
//                                                    Login.setId("button");
//                                                } 
//                                            }    
//                                        }       
//                                    } 
                                    //System.out.print("--Login.setOnMouseMoved--::"+ Login.getId() +"\n");
                                });                                
                                Login.setOnMouseExited((event)-> {
                                    //System.out.print("** Exited 001 ** \n"); 
                                    if(Login_Color == true){
                                        Login.setId("Button_Login"); 
                                    }
//                                    if(Login.getId()=="button"){
//                                        if(ManageVD.getId() != "Button_ManageVD"){
//                                            if(ChangePW.getId() != "Button_ChangePW"){
//                                                if(Leave.getId() != "Button_Leave"){
//                                                    //System.out.print("** Exited 002 ** \n");
//                                                    Login.setId("Button_Login"); 
//                                                } 
//                                            }    
//                                        }       
//                                    }     
                                    //System.out.print("--Login.setOnMouseExited--::"+ Login.getId() +"\n");                       
                                });                                
                            } 
                        }    
                    }       
                }                
            }
        });
        
        ManageVD.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {
                if(ManageVD.getId()=="Button_ManageVD"){
                    //ManageVD.setId("Button_actionON");
                }else{
                    ManageVD.setId("button");
                }
                //System.out.print("**ManageVD IN**\n");
                
            } if (oldValue) {
                if(ManageVD.getId()=="Button_ManageVD"){
                    //ManageVD.setId("Button_actionON");
                }else{
                    ManageVD.setId("button");
                }
                //System.out.print("**ManageVD OUT**\n");
            }
            if(ManageVD.isFocused()){
                //System.out.print("--ManageVD.isFocused()--\n");
                //System.out.print("**ManageVD isFocused ** : "+ ManageVD.getId() +"\n");
                
                if(Login.getId()=="Button_Login"){
                    //Login.setId("Button_actionON");
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");
                }
                if(ManageVD.getId()=="Button_ManageVD"){
                    //ManageVD.setId("Button_actionON");
                    Login.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");
//                    ManageVD.setOnMouseMoved((event)-> {
//                        ManageVD.setId("Button_hover");        
//                    });
//                    ManageVD.setOnMouseExited((event)-> {
//                        ManageVD.setId("Button_actionON");       
//                    });
                }
                if(ChangePW.getId()=="Button_ChangePW"){
                    //ChangePW.setId("Button_actionON");
                    Login.setId("button");
                    ManageVD.setId("button");
                    Leave.setId("button");
                }
                if(Leave.getId()=="Button_Leave"){
                    //Leave.setId("Button_actionON");
                    Login.setId("button");
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                }
                if(ManageVD.getId()=="button"){
                    if(Login.getId() != "Button_Login"){
                        if(ChangePW.getId() != "Button_ChangePW"){
                            if(Leave.getId() != "Button_Leave"){
                                Login.setId("button");
                                ChangePW.setId("button");
                                Leave.setId("button");
                            } 
                        }    
                    }            
                }
            }
        });
        
        ManageVD.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {
                if(ManageVD.getId()=="Button_ManageVD"){
                    //ManageVD.setId("Button_actionON");
                }else{
                    ManageVD.setId("button");
                }
                //System.out.print("**ManageVD IN**\n");
                //System.out.print("**ManageVD IN**** : "+ ManageVD.getId() +"\n");
            }if (oldValue) {
                if(ManageVD.getId()=="Button_ManageVD"){
                    //ManageVD.setId("Button_actionON");
                }else{
                    ManageVD.setId("button");
                }
                //System.out.print("**ManageVD OUT**\n");
                //System.out.print("**ManageVD OUT**** : "+ ManageVD.getId() +"\n");
            }
            //System.out.print("**ManageVD ** : "+ ManageVD.getId() +"\n");
            if(ManageVD.isHover()){
                //System.out.print("--ManageVD.isHover()--\n");
                //System.out.print("**ManageVD isHover ** : "+ ManageVD.getId() +"\n");                
                if(Login.getId()=="Button_Login"){
                    //Login.setId("Button_actionON");
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");
                }
                if(ManageVD.getId()=="Button_ManageVD"){
                    //ManageVD.setId("Button_actionON");
                    Login.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");
                }
                if(ChangePW.getId()=="Button_ChangePW"){
                    //ChangePW.setId("Button_actionON");
                    Login.setId("button");
                    ManageVD.setId("button");
                    Leave.setId("button");
                }
                if(Leave.getId()=="Button_Leave"){
                    //Leave.setId("Button_actionON");
                    Login.setId("button");
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                }
                if(ManageVD.getId()=="button"){
                    if(Login.getId() != "Button_Login"){
                        if(ChangePW.getId() != "Button_ChangePW"){
                            if(Leave.getId() != "Button_Leave"){
                                Login.setId("button");
                                ChangePW.setId("button");
                                Leave.setId("button");
                            } 
                        }    
                    }           
                }
                /*** button 在第一次 Mouse-in 後 皆會直接進入 setOnMouseMoved ; 因此在mouse-in時加入條件  ***/
                /*** button 在第一次 Mouse-out 後 皆會直接進入 setOnMouseExited ; 因此在mouse-out時加入條件  ***/
                if(ManageVD.getId()=="Button_ManageVD"){
                    if(Login.getId() != "Button_Login"){
                        if(ChangePW.getId() != "Button_ChangePW"){
                            if(Leave.getId() != "Button_Leave"){
                                Login.setId("button");
                                ChangePW.setId("button");
                                Leave.setId("button");                                
                                ManageVD.setOnMouseMoved((event)-> { 
                                    if(ManageVD_Color == true){
                                        ManageVD.setId("button");
                                    }
//                                    if(ManageVD.getId()=="Button_ManageVD"){
//                                        if(Login.getId() != "Button_Login"){
//                                            if(ChangePW.getId() != "Button_ChangePW"){
//                                                if(Leave.getId() != "Button_Leave"){                                                    
//                                                    ManageVD.setId("button");
//                                                } 
//                                            }    
//                                        }       
//                                    } 
                                    //System.out.print("--ManageVD.setOnMouseMoved--::"+ ManageVD.getId() +"\n");
                                });                                
                                ManageVD.setOnMouseExited((event)-> {  
                                    if(ManageVD_Color == true){
                                        ManageVD.setId("Button_ManageVD");
                                    }
//                                    if(ManageVD.getId()=="button"){
//                                        if(Login.getId() != "Button_Login"){
//                                            if(ChangePW.getId() != "Button_ChangePW"){
//                                                if(Leave.getId() != "Button_Leave"){                                                    
//                                                    ManageVD.setId("Button_ManageVD");
//                                                } 
//                                            }    
//                                        }       
//                                    }     
                                    //System.out.print("--ManageVD.setOnMouseExited--::"+ ManageVD.getId() +"\n");                      
                                });                                
                            } 
                        }    
                    }           
                }
                
            }
        });
        
        ChangePW.focusedProperty().addListener((ov, oldValue, newValue) -> {
            //System.out.print("**ChangePW**\n");
            if (newValue) {                
                if(ChangePW.getId()=="Button_ChangePW"){                    
                    //ChangePW.setId("Button_actionON");                    
                }else{                    
                    ChangePW.setId("button");                    
                }                
                //System.out.print("**ChangePW IN**\n"); //Button_hover                
            } if (oldValue) {
                if(ChangePW.getId()=="Button_ChangePW"){
                    //ChangePW.setId("Button_actionON");
                }else{
                    ChangePW.setId("button");                   
                }
                //System.out.print("**ChangePW OUT**\n");
            }
            if(ChangePW.isFocused()){
                //System.out.print("--ChangePW.isFocused()--\n");
                //System.out.print("**ChangePW isFocused ** : "+ ChangePW.getId() +"\n");
                
                if(Login.getId()=="Button_Login"){
                    //Login.setId("Button_actionON");
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");
                }
                if(ChangePW.getId()=="Button_ChangePW"){
                    //ChangePW.setId("Button_actionON");
                    Login.setId("button");
                    ManageVD.setId("button");
                    Leave.setId("button");
//                    ChangePW.setOnMouseMoved((event)-> {
//                        ChangePW.setId("Button_hover");        
//                    });
//                    ChangePW.setOnMouseExited((event)-> {
//                        ChangePW.setId("Button_actionON");       
//                    });
                }
                if(ManageVD.getId()=="Button_ManageVD"){
                    //ManageVD.setId("Button_actionON");
                    Login.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");
                }
                if(Leave.getId()=="Button_Leave"){
                    //Leave.setId("Button_actionON");
                    Login.setId("button");
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                }
                if(ChangePW.getId()=="button"){
                    if(Login.getId() != "Button_Login"){
                        if(ManageVD.getId() != "Button_ManageVD"){
                            if(Leave.getId() != "Button_Leave"){
                                Login.setId("button");
                                ManageVD.setId("button");
                                Leave.setId("button");
                            } 
                        }    
                    }              
                }
            }
        });
        
        ChangePW.hoverProperty().addListener((ov, oldValue, newValue) -> {
            //System.out.print("**ChangePW**\n");
            if (newValue) {                
                if(ChangePW.getId()=="Button_ChangePW"){                    
                    //ChangePW.setId("Button_actionON");                    
                }else{                    
                    ChangePW.setId("button");        //Button_hover            
                }                
                //System.out.print("**ChangePW IN**\n");
                
            } if (oldValue) {
                if(ChangePW.getId()=="Button_ChangePW"){
                    //ChangePW.setId("Button_actionON");
                }else{
                    ChangePW.setId("button");                   
                }
                //System.out.print("**ChangePW OUT**\n");
            }
            //System.out.print("**ChangePW ** : "+ ChangePW.getId() +"\n");
            if(ChangePW.isHover()){
                //System.out.print("--ChangePW.isHover()--\n");
                //System.out.print("**ChangePW isHover ** : "+ ChangePW.getId() +"\n");                
                if(Login.getId()=="Button_Login"){
                    //Login.setId("Button_actionON");
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");
                }
                if(ChangePW.getId()=="Button_ChangePW"){
                    //ChangePW.setId("Button_actionON");
                    Login.setId("button");
                    ManageVD.setId("button");
                    Leave.setId("button");
                }
                if(ManageVD.getId()=="Button_ManageVD"){
                    //ManageVD.setId("Button_actionON");
                    Login.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");
                }
                if(Leave.getId()=="Button_Leave"){
                    //Leave.setId("Button_actionON");
                    Login.setId("button");
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                }
                if(ChangePW.getId()=="button"){
                    if(Login.getId() != "Button_Login"){
                        if(ManageVD.getId() != "Button_ManageVD"){
                            if(Leave.getId() != "Button_Leave"){
                                Login.setId("button");
                                ManageVD.setId("button");
                                Leave.setId("button");
                            } 
                        }    
                    }             
                }        
                /*** button 在第一次 Mouse-in 後 皆會直接進入 setOnMouseMoved ; 因此在mouse-in時加入條件  ***/
                /*** button 在第一次 Mouse-out 後 皆會直接進入 setOnMouseExited ; 因此在mouse-out時加入條件  ***/
                if(ChangePW.getId()=="Button_ChangePW"){
                    if(Login.getId() != "Button_Login"){
                        if(ManageVD.getId() != "Button_ManageVD"){
                            if(Leave.getId() != "Button_Leave"){
                                Login.setId("button");
                                ManageVD.setId("button");
                                Leave.setId("button");
                                ChangePW.setOnMouseMoved((event)-> {
                                    if(ChangePW_Color == true){
                                        ChangePW.setId("button");
                                    }
//                                    if(ChangePW.getId()=="Button_ChangePW"){
//                                        if(Login.getId() != "Button_Login"){
//                                            if(ManageVD.getId() != "Button_ManageVD"){
//                                                if(Leave.getId() != "Button_Leave"){
//                                                    ChangePW.setId("button");
//                                                } 
//                                            }    
//                                        }             
//                                    }
                                    //System.out.print("--ChangePW.setOnMouseMoved--::"+ ChangePW.getId() +"\n");
                                });
                                ChangePW.setOnMouseExited((event)-> {
                                    if(ChangePW_Color == true){
                                        ChangePW.setId("Button_ChangePW");
                                    }
//                                    if(ChangePW.getId()=="button"){
//                                        if(Login.getId() != "Button_Login"){
//                                            if(ManageVD.getId() != "Button_ManageVD"){
//                                                if(Leave.getId() != "Button_Leave"){
//                                                    ChangePW.setId("Button_ChangePW");
//                                                } 
//                                            }    
//                                        }             
//                                    }                                  
                                    //System.out.print("--ChangePW.setOnMouseExited--::"+ ChangePW.getId() +"\n");

                                });
                            } 
                        }    
                    }             
                }                
            }
        });
        
        Leave.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {
                if(Leave.getId()=="Button_Leave"){                    
                    //Leave.setId("Button_actionON");                    
                }else{                    
                    Leave.setId("button");  //Button_hover             
                } 
                //System.out.print("**Leave IN** Focused \n");
            } if (oldValue) {
                if(Leave.getId()=="Button_Leave"){
                    //Leave.setId("Button_actionON");
                }else{
                    Leave.setId("button");                   
                }
                //System.out.print("**Leave OUT** Focused \n");
            }
            if(Leave.isFocused()){
                //System.out.print("--Leave.isFocused()--\n");
                //System.out.print("**Leave isFocused ** : "+ Leave.getId() +"\n");
                
                if(Login.getId()=="Button_Login"){
                    //Login.setId("Button_actionON");
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");
                }
                if(ChangePW.getId()=="Button_ChangePW"){
                    //ChangePW.setId("Button_actionON");
                    Login.setId("button");
                    ManageVD.setId("button");
                    Leave.setId("button");
                }
                if(ManageVD.getId()=="Button_ManageVD"){
                    //ManageVD.setId("Button_actionON");
                    Login.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");
                }
                if(Leave.getId()=="Button_Leave"){
                    //Leave.setId("Button_actionON");
                    Login.setId("button");
                    ManageVD.setId("button");
                    ChangePW.setId("button");
//                    Leave.setOnMouseMoved((event)-> {
//                        Leave.setId("Button_hover");        
//                    });
//                    Leave.setOnMouseExited((event)-> {
//                        Leave.setId("Button_actionON");       
//                    });
                }
                if(Leave.getId()=="button"){
                    if(Login.getId() != "Button_Login"){
                        if(ManageVD.getId() != "Button_ManageVD"){
                            if(ChangePW.getId() != "Button_ChangePW"){
                                Login.setId("button");
                                ManageVD.setId("button");
                                ChangePW.setId("button");
                            } 
                        }    
                    }              
                }                
            }
        });
        
        Leave.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {
                if(Leave.getId()=="Button_Leave"){                    
                    //Leave.setId("Button_actionON");   
                }else{                    
                    Leave.setId("button");   
                } 
                //System.out.print("**Leave IN**\n");
                //System.out.print("**Leave IN** : "+ Leave.getId() +"\n");
            } 
            if (oldValue) {
                if(Leave.getId()=="Button_Leave"){
                    //Leave.setId("Button_actionON");
                }else{
                    Leave.setId("button");         
                }
                //System.out.print("**Leave OUT**\n");
                //System.out.print("**Leave OUT** : "+ Leave.getId() +"\n");
            }
            //System.out.print("**Leave ** : "+ Leave.getId() +"\n");
            if(Leave.isHover()){
                //System.out.print("--Leave.isHover()--\n");
                //System.out.print("**Leave isHover ** : "+ Leave.getId() +"\n");                
                if(Login.getId()=="Button_Login"){
                   // Login.setId("Button_actionON");
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");
                }
                if(ChangePW.getId()=="Button_ChangePW"){
                    //ChangePW.setId("Button_actionON");
                    Login.setId("button");
                    ManageVD.setId("button");
                    Leave.setId("button");
                }
                if(ManageVD.getId()=="Button_ManageVD"){
                    //ManageVD.setId("Button_actionON");
                    Login.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");
                }
                if(Leave.getId()=="Button_Leave"){
                    //Leave.setId("Button_actionON");                    
                    Login.setId("button");
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                }
                if(Leave.getId()=="button"){
                    if(Login.getId() != "Button_Login"){
                        if(ManageVD.getId() != "Button_ManageVD"){
                            if(ChangePW.getId() != "Button_ChangePW"){
                                Login.setId("button");
                                ManageVD.setId("button");
                                ChangePW.setId("button");
                            } 
                        }    
                    }             
                }
                /*** button 在第一次 Mouse-in 後 皆會直接進入 setOnMouseMoved ; 因此在mouse-in時加入條件  ***/
                /*** button 在第一次 Mouse-out 後 皆會直接進入 setOnMouseExited ; 因此在mouse-out時加入條件  ***/
                if(Leave.getId()=="Button_Leave"){
                    if(Login.getId() != "Button_Login"){
                        if(ManageVD.getId() != "Button_ManageVD"){
                            if(ChangePW.getId() != "Button_ChangePW"){
                                Login.setId("button");
                                ManageVD.setId("button");
                                ChangePW.setId("button");
                                Leave.setOnMouseMoved((event)-> {
                                    if(Leave_Color == true){
                                        Leave.setId("button");
                                    }
//                                    if(Leave.getId()=="Button_Leave"){
//                                        if(Login.getId() != "Button_Login"){
//                                            if(ManageVD.getId() != "Button_ManageVD"){
//                                                if(ChangePW.getId() != "Button_ChangePW"){
//                                                    Leave.setId("button");
//                                                } 
//                                            }    
//                                        }             
//                                    }                                    
                                    //System.out.print("--Leave.setOnMouseMoved--::"+ Leave.getId() +"\n");
                                });
                                Leave.setOnMouseExited((event)-> {
                                    if(Leave_Color == true){
                                        Leave.setId("Button_Leave");
                                    }
//                                    if(Leave.getId()=="button"){
//                                        if(Login.getId() != "Button_Login"){
//                                            if(ManageVD.getId() != "Button_ManageVD"){
//                                                if(ChangePW.getId() != "Button_ChangePW"){
//                                                    Leave.setId("Button_Leave");
//                                                } 
//                                            }    
//                                        }             
//                                    }                                         
                                    //System.out.print("--Leave.setOnMouseExited--::"+ Leave.getId() +"\n");
                                }); 
                            } 
                        }    
                    }             
                }
            }
        });
    
 // 2018.01.01 william SnapShot實作
        SS.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {
                if(SS.getId() == "Button_SS") {
                
                } else {
                    SS.setId("button");
                }                
            } 
            
            if (oldValue) {
                if(SS.getId() == "Button_SS") {
                
                } else {
                    SS.setId("button");
                }
            }
            
            if(SS.isFocused()) {               
                if(Login.getId() == "Button_Login") {
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");
                    SS.setId("button");
                }
                if(ManageVD.getId() == "Button_ManageVD"){
                    Login.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");
                    SS.setId("button");
                }
                if(ChangePW.getId() == "Button_ChangePW"){
                    Login.setId("button");
                    ManageVD.setId("button");
                    Leave.setId("button");
                    SS.setId("button");
                }
                if(Leave.getId() == "Button_Leave"){
                    Login.setId("button");
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                    SS.setId("button");
                }
                if(SS.getId() == "Button_SS") {
                    Login.setId("button");
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");
                }                
                if(SS.getId() == "button") {
                    if(Login.getId() != "Button_Login") {
                        if(ManageVD.getId() != "Button_ManageVD") {    
                            if(ChangePW.getId() != "Button_ChangePW") {
                                if(Leave.getId() != "Button_Leave") {
                                    Login.setId("button");
                                    ManageVD.setId("button");
                                    ChangePW.setId("button");
                                    Leave.setId("button");
                                } 
                            }  
                        }
                    }            
                }
            }
        });        
        // 2018.01.01 william SnapShot實作
        SS.hoverProperty().addListener((ov, oldValue, newValue) -> {                       
            if (newValue) {
                if(SS.getId() == "Button_SS") {
                
                } else {
                    SS.setId("button");
                }                
            } 
            
            if (oldValue) {
                if(SS.getId() == "Button_SS") {
                
                } else {
                    SS.setId("button");
                }
            }            
                        
            if(SS.isHover()) {                                       
                if(Login.getId() == "Button_Login") {
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");
                    SS.setId("button");
                }
                if(ManageVD.getId() == "Button_ManageVD"){
                    Login.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");
                    SS.setId("button");
                }
                if(ChangePW.getId() == "Button_ChangePW"){
                    Login.setId("button");
                    ManageVD.setId("button");
                    Leave.setId("button");
                    SS.setId("button");
                }
                if(Leave.getId() == "Button_Leave"){
                    Login.setId("button");
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                    SS.setId("button");
                }
                if(SS.getId() == "Button_SS") {
                    Login.setId("button");
                    ManageVD.setId("button");
                    ChangePW.setId("button");
                    Leave.setId("button");
                }  
                if(SS.getId() == "button") {
                    if(Login.getId() != "Button_Login") {
                        if(ManageVD.getId() != "Button_ManageVD") {    
                            if(ChangePW.getId() != "Button_ChangePW") {
                                if(Leave.getId() != "Button_Leave") {
                                    Login.setId("button");
                                    ManageVD.setId("button");
                                    ChangePW.setId("button");
                                    Leave.setId("button");
                                } 
                            }  
                        }
                    }            
                }
                /*** button 在第一次 Mouse-in 後 皆會直接進入 setOnMouseMoved ; 因此在mouse-in時加入條件  ***/
                /*** button 在第一次 Mouse-out 後 皆會直接進入 setOnMouseExited ; 因此在mouse-out時加入條件  ***/
                if(SS.getId() == "Button_SS") {
                    if(Login.getId() != "Button_Login") {
                        if(ManageVD.getId() != "Button_ManageVD") {
                            if(ChangePW.getId() != "Button_ChangePW") {
                                if(Leave.getId() != "Button_Leave") {
                                    Login.setId("button");
                                    ManageVD.setId("button");
                                    ChangePW.setId("button");
                                    Leave.setId("button");                                    
                                    SS.setOnMouseMoved((event)-> {
                                        if(SS_Color == true){
                                            SS.setId("button");
                                        }
                                    });                                           
                                    SS.setOnMouseExited((event)-> {
                                        if(SS_Color == true){
                                            SS.setId("Button_SS"); 
                                        }                 
                                    });                                
                                } 
                            }    
                        }                       
                    }
                }                
            }
        });                
        
        
    }     
    // Input： ,  Output： , 功能： 鍵盤點擊之動作
    public void keydownup(){
        /***以下****Start*******Enter & Page Down & Page Up 功能**********************/        
        //Enter Key & PAGE_DOWN Key - down ; PAGE_UP Key - up 
        LanguageSelection.setOnKeyPressed((event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){
                IP_Addr.requestFocus();
                pwIP.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                IP_Addr.requestFocus();
                pwIP.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.TAB){
                IP_Addr.requestFocus();
                pwIP.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                Leave.requestFocus();
                event.consume();// Consume Event
            }
        });
        //Enter Key & PAGE_DOWN Key - down ; PAGE_UP Key - up
        IP_Addr.setOnKeyPressed((event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){
                // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//                userTextField.requestFocus();
//                pwName.requestFocus();
                IP_Port.requestFocus();
                pwIP_Port.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//                userTextField.requestFocus();
//                pwName.requestFocus();
                IP_Port.requestFocus();
                pwIP_Port.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.TAB){
                // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//                userTextField.requestFocus();
//                pwName.requestFocus();
                IP_Port.requestFocus();
                pwIP_Port.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                LanguageSelection.requestFocus();
                event.consume();// Consume Event
            }
        });
          //Enter Key & PAGE_DOWN Key - down ; PAGE_UP Key - up
        pwIP.setOnKeyPressed((event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){
                // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//                userTextField.requestFocus();
//                pwName.requestFocus();
                IP_Port.requestFocus();
                pwIP_Port.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//                userTextField.requestFocus();
//                pwName.requestFocus();
                IP_Port.requestFocus();
                pwIP_Port.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.TAB){
                // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//                userTextField.requestFocus();
//                pwName.requestFocus();
                IP_Port.requestFocus();
                pwIP_Port.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                LanguageSelection.requestFocus();
                event.consume();// Consume Event
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
        });         
        
        //Enter Key & PAGE_DOWN Key - down ; PAGE_UP Key - up 
        userTextField.setOnKeyPressed((event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){
                pwBox.requestFocus();
                textField01.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                pwBox.requestFocus();
                textField01.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.TAB){
                pwBox.requestFocus();
                textField01.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                 // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//                IP_Addr.requestFocus();
//                pwIP.requestFocus();
                IP_Port.requestFocus();
                pwIP_Port.requestFocus();
                event.consume();// Consume Event
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
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                pwBox.requestFocus();
                textField01.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.TAB){
                pwBox.requestFocus();
                textField01.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                 // 2017.08.10 william IP增加port欄位，預設443 新增鍵盤行為
//                IP_Addr.requestFocus();
//                pwIP.requestFocus();
                IP_Port.requestFocus();
                pwIP_Port.requestFocus();
                event.consume();// Consume Event
            }
        });
        //Enter Key & PAGE_DOWN Key - down ; PAGE_UP Key - up 
        pwBox.setOnKeyPressed((event)-> {
            //event.get
            if(event.getCode() == KeyCode.ENTER){
                Login.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                Login.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.TAB){
                Login.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                userTextField.requestFocus();
                pwName.requestFocus();
                // 2018.12.25 Remember10 username
                UsernameComboBox.requestFocus();                  
                event.consume();// Consume Event
            }
        });
        textField01.setOnKeyPressed((event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){
                Login.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                Login.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.TAB){
                Login.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                userTextField.requestFocus();
                pwName.requestFocus();
                // 2018.12.25 Remember10 username
                UsernameComboBox.requestFocus();                  
                event.consume();// Consume Event
            }
        });
        //Enter Key-進入功能 ; PAGE_DOWN Key - down ; PAGE_UP Key - up 
        Login.setOnKeyPressed((event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){
//                Login.setId("Button_actionON");
//                //Login.arm();
//                PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
//                pause.setOnFinished(e -> {
//                    Login.setId("button");
//                    //Login.disarm();       
//                    Login.fire(); 
//                });                     
//                pause.play();
                Login.fire();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                checkBox_Pw.requestFocus();
                //ManageVD.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.TAB){
                checkBox_Pw.requestFocus();
                //ManageVD.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                pwBox.requestFocus();
                textField01.requestFocus();
                event.consume();// Consume Event
            }
        });
//        Login.setOnKeyReleased((event)-> {
//            //Login.setId("button");
//        });

        //Enter Key-進入功能 ; PAGE_DOWN Key - down ; PAGE_UP Key - up 
        checkBox_Pw.setOnKeyPressed((KeyEvent event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){                
                checkBox_Pw.fire(); 
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                SS.requestFocus(); // 2018.01.01 william SnapShot實作 ManageVD -> SS
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.TAB){
                SS.requestFocus(); // 2018.01.01 william SnapShot實作 ManageVD -> SS
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                Login.requestFocus();
                event.consume();// Consume Event
            }
        });
        // 2018.01.01 william SnapShot實作
        SS.setOnKeyPressed((KeyEvent event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){                
                SS.fire(); 
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                ManageVD.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.TAB){
                ManageVD.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                checkBox_Pw.requestFocus();
                event.consume();// Consume Event
            }        
        });
        
        //Enter Key-進入功能 ; PAGE_DOWN Key - down ; PAGE_UP Key - up 
        ManageVD.setOnKeyPressed((KeyEvent event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){                
//                ManageVD.setId("Button_actionON");                 
                //ManageVD.arm();
//                PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
//                pause.setOnFinished(e -> {
//                    ManageVD.setId("button");
//                    //ManageVD.disarm();       
//                    ManageVD.fire(); 
//                });                 
//                pause.play();
                ManageVD.fire(); 
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                ChangePW.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.TAB){
                ChangePW.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                SS.requestFocus(); // 2018.01.01 william SnapShot實作 checkBox_Pw -> SS
                event.consume();// Consume Event
            }
        });       
//        ManageVD.setOnKeyReleased((event)-> {  
//            //ManageVD.setId("button");
//        });
        
        //Enter Key-進入功能 ; PAGE_DOWN Key - down ; PAGE_UP Key - up 
        ChangePW.setOnKeyPressed((event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){
//                ChangePW.setId("Button_actionON");
//                //ChangePW.arm();
//                PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
//                pause.setOnFinished(e -> {
//                    ChangePW.setId("button");
//                    //ChangePW.disarm();       
//                    ChangePW.fire(); 
//                });                     
//                pause.play();
                ChangePW.fire(); 
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                Leave.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.TAB){
                Leave.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                ManageVD.requestFocus();
                event.consume();// Consume Event
            }
        });
//        ChangePW.setOnKeyReleased((event)-> {
//            //ChangePW.setId("button");
//
//        });
        //Enter Key-進入功能 ; PAGE_DOWN Key - down ; PAGE_UP Key - up
        Leave.setOnKeyPressed((event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){
//                Leave.setId("Button_actionON");
//                //Leave.arm();
//                PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
//                pause.setOnFinished(e -> {
//                    Leave.setId("button");
//                    //Leave.disarm();       
//                    Leave.fire(); 
//                });                     
//                pause.play();
                Leave.fire(); 
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                LanguageSelection.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.TAB){
                LanguageSelection.requestFocus();
                event.consume();// Consume Event
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                ChangePW.requestFocus();
                event.consume();// Consume Event
            }
        });
//        Leave.setOnKeyReleased((event)-> {
//            //Leave.setId("button");
//        });  
        
    }
    // Input： ,  Output： , 功能： 滑鼠滑過之動作 (-----目前無使用-----)
    public void mousemove(){ 
        
//        IP_Addr.setOnMouseEntered((event)-> {
//            IP_Addr.requestFocus();        
//        });
        
        Login.setOnMouseEntered((event)-> {
            Login.requestFocus();
        
        });
        ManageVD.setOnMouseEntered((event)-> {
            ManageVD.requestFocus();
        
        });
        ChangePW.setOnMouseEntered((event)-> {
            ChangePW.requestFocus();
        
        });
        Leave.setOnMouseEntered((event)-> {
            Leave.requestFocus();
        
        });
        // 2018.01.01 william SnapShot實作
        SS.setOnMouseEntered((event)-> {
            SS.requestFocus();
        
        });           
    }    
    // Input： ,  Output： , 功能： 檢查使用者使否已連上VM/VD (清除密碼 for windows)
    public void CheckUserIsOnline() {
        if(!ClearPW_Login_flag){
            return;
        } 
	Timer timer_check=new Timer(true);
        TimerTask task_check=new TimerTask(){
            @Override
            public void run() {       
                try { 
                    System.out.println("次數："+ClearPW_count+"\n"); 
                    QM.CheckUserLogin(IP_Addr.getText(),userTextField.getText(),pwBox.getText(),uniqueKey.toString(),CurrentIP_Port);
                    ClearPW_flag = CheckClearPWflag();     
                    if(ClearPW_flag&&QM.IsVdOnline_flage) {
                        timer_check.cancel();
                        System.out.println("***************************Viewer 啟動******************************\n");    
                        if(checkBox_Pw.isSelected()==checkBox_Pw_change){    
                            pwBox.clear(); //若使用者登入後 密碼會自動清除
                            ClearPW_flag = false;
                            QM.IsVdOnline_flage = false;
                            ClearPW_Login_flag = false;
                            ClearPW_count = 0;
                        } 
                        else {
                            ClearPW_flag = false;    
                            QM.IsVdOnline_flage = false;
                            ClearPW_Login_flag = false;
                            ClearPW_count = 0;
                        }
                        return;
                    }
                    
                    if((ClearPW_flag==false&&QM.IsVdOnline_flage==false)||(ClearPW_flag==false&&QM.IsVdOnline_flage==true)){
                        timer_check.cancel();
                        System.out.println("***************************Viewer 關閉******************************\n");    
                        if(checkBox_Pw.isSelected()==checkBox_Pw_change){    
                            pwBox.clear(); //若使用者登入後 密碼會自動清除
                            ClearPW_flag = false;
                            QM.IsVdOnline_flage = false;
                            ClearPW_Login_flag = false;
                            ClearPW_count = 0;
                        } 
                        else {
                            ClearPW_flag = false;    
                            QM.IsVdOnline_flage = false;
                            ClearPW_Login_flag = false;
                            ClearPW_count = 0;
                        }
                        return;
                    }            
                    
                    if(ClearPW_count>10){
                        timer_check.cancel();
                        System.out.println("***************************10秒後自動清除密碼******************************"+ClearPW_count+"\n");    
                        if(checkBox_Pw.isSelected()==checkBox_Pw_change){    
                            pwBox.clear(); //若使用者登入後 密碼會自動清除
                            ClearPW_flag = false;
                            QM.IsVdOnline_flage = false;
                            ClearPW_Login_flag = false;
                            ClearPW_count = 0;
                        } 
                        else {
                            ClearPW_flag = false;    
                            QM.IsVdOnline_flage = false;
                            ClearPW_Login_flag = false;
                            ClearPW_count = 0;
                        }
                        return;
                    }                     
                    ClearPW_count ++;
                                    
                } catch (IOException ex) {
                    Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        timer_check.schedule(task_check, 0,1000); 
    }
    // Input： ,  Output： False/True (boolean) , 功能： 檢查SPICE是否啟動，已啟動為True，反之 為False(清除密碼 for windows)
     public boolean CheckClearPWflag() throws IOException {
        tasklist_p = Runtime.getRuntime().exec(tasklist_cmd);
        InputStreamReader isr = new InputStreamReader(tasklist_p.getInputStream());
        BufferedReader input = new BufferedReader(isr);   
        while ((tasklist_line = input.readLine()) != null) {
            if(tasklist_line.contains(tasklist_s)==true){
                System.out.println("***************************remote-viewer process 啟動******************************\n");    
                return true;            
            }
        }
        System.out.println("***************************remote-viewer process 無啟動******************************\n");    
        return false;
     }    
    // Input： ,  Output： False/True (boolean), 功能： 檢查作業系統，XP為True，其他為False
    public boolean checkOSVer() throws IOException{        
        String command = "ver | findstr /i \"5\\.\""; // https://msdn.microsoft.com/en-us/library/windows/desktop/ms724832(v=vs.85).aspx 查版本表
        Process Info = Runtime.getRuntime().exec(new String[]{"cmd","/c",command}); // "cmd","/c" "/bin/sh","-c" https://stackoverflow.com/questions/13212033/get-windows-version-in-a-batch-file
        BufferedReader reader = new BufferedReader(new InputStreamReader(Info.getInputStream()));
        String returnResult;
        if ((returnResult = reader.readLine()) != null) {
            System.out.println("----- OS is WinXP:"+returnResult+" -----\n");
            return true;
        }
        else {
            System.out.println("----- OS is Win7/8/10 -----\n");
            return false;
        }
    }       
    // Input：1. 是否為快照(boolean), 2. 是否為檢視D槽(boolean),  Output：  , 功能： VD登入及快照登入連線函式 
    public void Login_func(boolean IsSnapshot, boolean IsViewDrive) {
        
        // 2019.03.28 VM/VD migration reconnect
        StopMigrationLock();
        clearMigrationParameter();
        
        //System.out.println("** lock_Login ** : "+lock_Login+"\n");
        if(lock_Login == false) {
            lock_Login = true;
            //Login.setDisable(true);
            // 檢查IP
            if(IP_Addr.getText() != null) {
                CurrentIP = IP_Addr.getText();
            } else {
                CurrentIP = ""; 
            }   
            // 檢查使用者名稱                
//            if(userTextField.getText() != null) {
//                CurrentUserName = userTextField.getText();
//            } else {
//                CurrentUserName = "";              
//            }

            // 2018.12.25 Remember10 username            
            if(!"".equals(UsernameComboBox.getValue().toString())) {
                CurrentUserName = UsernameComboBox.getValue().toString();
            } else {
                CurrentUserName = "";              
            } 

            WriteLastStatus();
            WriteLastChange(); //記錄明碼與暗碼

            if(pwBox.getText() != null) {
                CurrentPasseard = pwBox.getText();             
            } else {
                CurrentPasseard = "";
            }

            if(IsSnapshot) {
                updateSS();
                // 2019.01.02 view D Drive
                GB.IsViewDisk = true; 
                GB._viewDiskVDName = "";                
            } else {
                updateLogin();
                GB.IsViewDisk = false; // 2019.01.02 view D Drive
            }            
                
            /*** 2017.08.10 william 登入中thread畫面鎖住  ***/       
            LoginGP = new GridPane();        
            //MountGP.setGridLinesVisible(true); //開啟或關閉表格的分隔線       
            LoginGP.setAlignment(Pos.BASELINE_CENTER);       
            //MountGP.setPadding(new Insets(0, 0, 0, 0)); //設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
            LoginGP.setHgap(5); //元件間的水平距離
            LoginGP.setVgap(5); //元件間的垂直距離
            if(!GB.winXP_ver) {
                LoginGP.setPrefSize(530, 235); //400, 480
                LoginGP.setMaxSize(530, 235);
                LoginGP.setMinSize(530, 235);
            } else {
                LoginGP.setPrefSize(530, 235); //400, 480
                LoginGP.setMaxSize(530, 235);
                 LoginGP.setMinSize(530, 235);    
            }                               
            LoginGP.setTranslateY(45);
            LoginGP.setStyle("-fx-background-color: #EEF5F5 ; -fx-opacity: 0.7 ; -fx-background-radius: 4;-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 10, 0.0 , 0 , 5 );");    
            
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
                Login_title.setStyle("-fx-font-family:'Times New Roman'; -fx-font-size: 20px; -fx-font-weight: 900;");//21
                Login_title2.setStyle("-fx-font-family:'Times New Roman'; -fx-font-size: 26px; -fx-font-weight: 900;");
                Login_title3.setStyle("-fx-font-family:'Times New Roman'; -fx-font-size: 26px; -fx-font-weight: 900;");            
            } else {
                Login_title.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-size: 26px; -fx-font-weight: 900;");//20           
                Login_title2.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-size: 26px; -fx-font-weight: 900;");
                Login_title3.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-size: 26px; -fx-font-weight: 900;");

                Login_title.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 26px; -fx-font-weight: 900;");//20
                Login_title2.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 26px; -fx-font-weight: 900;");
                Login_title3.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 26px; -fx-font-weight: 900;");

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
            LoginGP.add(Login_title,0,0); //2,1是指這個元件要佔用2格的column和1格的row

            ProgressIndicator p1 = new ProgressIndicator();
            p1.setTranslateY(50);
            //p1.setStyle("-fx-foreground-color: #1A237E");
            LoginGP.add(p1,0,1); //2,1是指這個元件要佔用2格的column和1格的row 
            */    
            StackPane stack = new StackPane();
            stack.getChildren().addAll(MainGP, LoginGP);     
            
            StartLoginLock(); // 2019.05.13 單一VD登入UI顯示
            /*--------------------------------------------------------------------------------------*/
            /*--------------------------------------------------------------------------------------*/
            Boolean Result=PATTERN.matcher(CurrentIP).matches(); // 檢查IP格式
            // System.out.println(" -----Result------"+Result+"\n");
            
            if(Result == true || IP_Addr != null){ // 2017.07.21 william IP use domain name
                rootPane.setCenter(stack); // 2017.08.10 william 登入中thread畫面鎖住                       
                
                Task<Void> longRunningTask = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        IsSnapShot = IsSnapshot;         // 2018.01.01 william SnapShot實作                        
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
                        QM.Ping(IP_Addr.getText(),CurrentIP_Port); // 2017.08.10 william IP增加port欄位，預設443 
                        //QM.Dual_Screen(IP_Addr.getText(), CurrentIP_Port); // 2018.03.29 william 雙螢幕實作  -> 2018.08.22 雙螢幕設定要放在Power On 之前
                        // QM.QueryLogin(IP_Addr.getText(),userTextField.getText(),pwBox.getText(),uniqueKey.toString(),CurrentIP_Port);  // 2017.08.10 william IP增加port欄位，預設443
                        QM.QueryLogin(public_stage, IP_Addr.getText(), CurrentUserName , pwBox.getText(), uniqueKey.toString(), CurrentIP_Port, IsSnapShot);  // 2017.08.10 william IP增加port欄位，預設443 / 2017.11.24 單一帳號多個VD登入 / 2017.12.08 SnapShot實作 / // 2018.12.25 Remember10 username userTextField.getText()
                        /*===============================================錯誤修改(3)-清除密碼 for windows===============================================*/
                        ClearPW_Login_flag = true; 
//                            if(ClearPW_first_start) {
//                                ClearPW_max_count = 8;
//                            }
//                            else {
//                                ClearPW_max_count = 2;
//                            }  
                        ClearPW_flag = CheckClearPWflag();
                            /*===============================================錯誤修改(3)-清除密碼 for windows===============================================*/                            
                        return null;
                    }
                };
                    //new Thread(longRunningTask).start();

                longRunningTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                            /*
                            System.out.println("** Login longRunningTask.setOnFailed ** \n");
                            System.out.println("Ping - testError = "+QM.testError+"\n"); 
                            System.out.println("Login - connCode = "+QM.connCode+"\n");
                            */
                        if(QM.testError == false) {
                            LA.LogintestErrorAlert();
                        }
                        LA.AlertChangeLang(QM.connCode);
                        LA.SetVDOnline_AlertChangeLang(QM.SetVDonline_connCode);
                        System.out.println("Login -0000 IsVdOnline Failed = "+QM.VdOnline_connect+"\n");
                        //Login.setDisable(false);
                        rootPane.setCenter(MainGP); // 2017.08.10 william 登入中thread畫面鎖住
                        lock_Login = false;
                        QM.connCode = 0; // 2017.09.18 william 錯誤修改(2)-2個警告訊息
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
                                                
                        if(QM.s == 1 && !IsSnapshot) { // 2018.01.01 william 單一帳號多個VD登入 / SnapShot實作
                            System.out.println("** Login longRunningTask.setOnSucceeded ** \n");
                            LA.SetVDOnline_AlertChangeLang(QM.SetVDonline_connCode);
                            System.out.println("Login *** IsVdOnline Succeeded = "+QM.IsVdOnline+"\n");
                            if(QM.connCode == 200 && QM.ChcekServerAddress_connCode == 200){
                                if(QM.IsVdOnline.equals("true")){
                                    try {
                                        //System.out.println(" --- QM.IsVdOnline.equals(true)---  \n");
                                        /*===============================================錯誤修改(3)-清除密碼 for windows===============================================*/
                                        ClearPW_Login_flag = false;
                                        ClearPW_flag = false; 
                                        /*===============================================錯誤修改(3)-清除密碼 for windows===============================================*/                                        
                                        IsVdOnlineAlert();
                                    } catch (IOException | InterruptedException ex) {
                                        Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                            if(QM.Alert_spiceaddr==1){
                                /*===============================================錯誤修改(3)-清除密碼 for windows===============================================*/
                                ClearPW_Login_flag = false;
                                ClearPW_flag = false;
                                /*===============================================錯誤修改(3)-清除密碼 for windows===============================================*/                                
                                SetVDOlineAlert();
                            }
                            //Login.setDisable(false);
                            rootPane.setCenter(MainGP); // 2017.08.10 william 登入中thread畫面鎖住
                            lock_Login= false;
                            /*===============================================錯誤修改(3)-清除密碼 for windows===============================================*/                            
//                            ClearPW_first_start = false;
                            CheckUserIsOnline();
                            /*===============================================錯誤修改(3)-清除密碼 for windows===============================================*/                        
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
                        } else {                        
                            rootPane.setCenter(MainGP); 
                            lock_Login = false;                                
                            CheckUserIsOnline();
                            if(QM.s != 1 && !IsSnapshot) {
                                // 2018.01.01 william 單一帳號多個VD登入  
                            }                            
                        }
                        StopLoginLock();// 2019.05.13 單一VD登入UI顯示
                    }
                });
                new Thread(longRunningTask).start();
            } else {
                LoginResultAlert();
                rootPane.setCenter(MainGP); // 2017.08.10 william 登入中thread畫面鎖住
                lock_Login= false;               
                // System.out.println("** Login else end ** \n");
            }
        }
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
    // Input： ,  Output： , 功能： VM/VD migration 鎖定畫面
    public void MigrationLockView() {
        GridPane MigrationGP = new GridPane();
        MigrationGP.setAlignment(Pos.BASELINE_CENTER);       
        MigrationGP.setHgap(5);
        MigrationGP.setVgap(5);
        MigrationGP.setPrefSize(530, 235); //400, 480
        MigrationGP.setMaxSize(530, 235);
        MigrationGP.setMinSize(530, 235);
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
                //System.out.println("Connection: " + ConnectionProcess() + " Netstat: " + CheckNetstat());
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
                                            GB.migrationSPICE = true;
                                            QM.SetVDOnline(GB.migrationApiIP, GB.migrationVDID, GB.migrationUserName, GB.migrationPwd, GB.migrationUkey, GB.migrationApiPort);                                
                                            break;
                                        case "AcroRDP": 
                                            QM.SetVDRDPOnline(GB.migrationApiIP, GB.migrationVDID, GB.migrationUserName, GB.migrationPwd, GB.migrationUkey, GB.migrationApiPort, GB.migrationIsVdOrg, GB.adAuth, GB.adDomain); 
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
        String Spicecommand = "tasklist | find \"remote-viewer\"";  
        String remminacommand = "tasklist | find \"mstsc\"";  
              
        try {            
            
            Process CheckSpice_process = Runtime.getRuntime().exec(new String[]{"cmd","/c", Spicecommand});
            BufferedReader output_CheckSpice = new BufferedReader (new InputStreamReader(CheckSpice_process.getInputStream()));
            line_Spice = output_CheckSpice.readLine();
            
            Process Checkremmina_process = Runtime.getRuntime().exec(new String[]{"cmd","/c", remminacommand});
            BufferedReader output_Checkremmina = new BufferedReader (new InputStreamReader(Checkremmina_process.getInputStream()));
            line_remmina = output_Checkremmina.readLine();                
            
        } catch(IOException e) {}
        
        if(line_Spice == null && line_remmina == null) {
            return false;
        } else {
            return true; 
        }

    }     
    // Input： ,  Output： False/True (boolean), 功能： 檢查Protocol軟體有無連接成功，無為False，有為True
    private boolean CheckNetstat() {
        String line_Spice = "";
        String line_remmina = "";
//        String Spicecommand = "netstat -abno -p tcp | find \"remote-viewer\"";  
//        String remminacommand = "netstat -abno -p tcp | find \"mstsc\"";  
        String Spicecommand = "netstat -ano -p tcp | find \"" + GB.VMIP + "\" | find \"ESTABLISHED\"";  
        String remminacommand = "netstat -ano -p tcp | find \"" + GB.VMIP + "\" | find \"ESTABLISHED\"";  
        
        try {            
            Process CheckSpice_process = Runtime.getRuntime().exec(new String[]{"cmd","/c", Spicecommand});
            BufferedReader output_CheckSpice = new BufferedReader (new InputStreamReader(CheckSpice_process.getInputStream()));
            line_Spice = output_CheckSpice.readLine();
            
            Process Checkremmina_process = Runtime.getRuntime().exec(new String[]{"cmd","/c", remminacommand});
            BufferedReader output_Checkremmina = new BufferedReader (new InputStreamReader(Checkremmina_process.getInputStream()));
            line_remmina = output_Checkremmina.readLine();
         
            
        } catch(IOException e) {}
        
        if(line_Spice == null && line_remmina == null) {
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
            Runtime.getRuntime().exec("taskkill /f /im remote-viewer.exe");
            Runtime.getRuntime().exec("taskkill /f /im mstsc.exe");
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
        GB.migrationSPICE = false;
        GB.VMIP = "";
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
                        if("日本語".equals(LangNow)){
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
