/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_ipsc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author sabrina_yeh
 */
public class acrored_ipsc extends Application{
    
    /***   版本   ***/
    private static String Ver="( V 6.0.4 )";//( V 1.0.0 )
    /***    顯示文字對應表    ***/
    public Map<String, String> WordMap=new HashMap<>();
    /***  語言與JSON File對應表 ***/
    public Map<String, String> LanguageFileMap=new HashMap<>();
    /***   系統環境宣告   ***/
    private String LangNow;
    private String LangChange;
    public String iscsicliIP;
    public String iscsicliIPv4;
    public String iscsicliIPv6;
    public String CurrentUserName;
    public String CurrentPasseard;
    /*** Thread 宣告 ***/
    private QueryMachine QM;
    private UnMount UM;
    private Refresh RF;
    private CheckMount CM;
    private CheckAutoLogin CAL;
    
    /***  以下為顯示元件宣告    ***/
    private Stage public_stage;
    private GridPane MainGP;
    private AnchorPane anchorpane;
    private BorderPane rootPane;
    private GridPane MountGP;
    private GridPane UNMountGP;
    private GridPane RefreshGP;
    private GridPane FormatGP;
    private Label Language;
    private Label MyTitle;
    private TextField userTextField;
    private TextField IP_Addr;
    private PasswordField pwBox;
    private Label L_IP_Addr;
    private Label userName;
    private Label pw;
    private Label Colon1;
    private Label Colon2;
    private Label Colon3;
    private Label Colon4;
    private Label Colon5;
    private Label Colon6;
    private Label title_computer_system; 
    private Label MountStatus;
    private Label mounted;
    private Label Not_mounted;
    private Label Mount_title;
    private Label UNMount_title;
    private Label Refresh_title;
    private Label Format_title;
    private CheckBox checkBox_AutoLogin;
    private CheckBox checkBox_anonymous_binding;
    private Button Mount;
    private Button UNMount;
    private Button Refresh;
    private Button Leave;
    public static Sigar sigar;
    static Map<String, Long> rxCurrentMap = new HashMap<String, Long>();
    static Map<String, List<Long>> rxChangeMap = new HashMap<String, List<Long>>();
    static Map<String, Long> txCurrentMap = new HashMap<String, Long>();
    static Map<String, List<Long>> txChangeMap = new HashMap<String, List<Long>>();
    private Label title_download;
    private Label title_upload;
    private Label download_kbs;
    private Label upload_kbs;
    private Label bs_up;
    private Label bs_down;
    private String networkspeed_download;
    private String networkspeed_upload;
    //private HBox Status_networkspeed;
    //private HBox Status_name;
    private boolean checkBox_A_B_change;
    private boolean checkBox_AutoLogin_change;
    private String A_B_change;
    private String AutoLogin_change;
    public boolean button_change=true ;    
    private Scene scene;
    private Image EyeClose;
    private Image EyeOpen;
    private ToggleButton buttonPw=new ToggleButton();
    private ToggleButton buttonIP=new ToggleButton();
    private ToggleButton buttonUname=new ToggleButton();
    private boolean changeAfterSaved = true;
    private boolean IP_changeAfterSaved;
    private boolean Name_changeAfterSaved;
    private PasswordField pwIP;
    private PasswordField pwName;    
    private boolean change_mount_status = false;
    private String Yesmount;
    private String Nomount;
    private String Yesmount_format;
    private ComboBox LanguageSelection;
    private TextField textField01;
    private boolean lock_button_Mount=false;
    private boolean lock_button_Unmount=false;
    private boolean lock_button_Refresh=false;
    private boolean lock_format=false;
    private boolean Mount_Color = false;
    private boolean UNMount_Color = false;
    private boolean Refresh_Color = false;
    private boolean Leave_Color = false;
    private boolean Format_status = false;
    
    /*****Pattern:檢查IP是否符合正確格式*****/
    private static final Pattern PATTERN = Pattern.compile(
        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    
    /****** 存取 IP 帳號 與 眼睛 的系統路徑 (user home) ******/
    //String path = System.getProperty("user.home") + File.separator + "Documents"+ File.separator+"AcroRed IPSC";
    String path = System.getProperty("user.home") +File.separator+"AcroRed IPSC";
            
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception, SigarException {
        public_stage = primaryStage;
        QM=new QueryMachine(WordMap); //建立呼叫HTTP GET　POST REST JSON 的 Class
        UM=new UnMount(WordMap);
        RF=new Refresh(WordMap);
        CM=new CheckMount(WordMap);
        CAL=new CheckAutoLogin(WordMap);
        InitailLanguageFileMap();  //初始化語言與JSON對應，每次新增語言必須修改出使化程式     
        LangNow="繁體中文";   //若沒有預設語言，以繁體中文為主
        LoadLastStatus();   //讀出上一次的系統設定、連接伺服器IP、帳號
        LoadLanguageMapFile(LanguageFileMap.get(LangNow)); // 設定這次啟動的語言，建議每一個元件的文字
        /*** 開啟時,先檢查是否有掛載中的磁碟,若有--> 不鎖住卸載button ; 若無--> 鎖住卸載button ***/
        CM.CheckMountListSession(); 
        button_change = CM.CheckMount_button_change;
        change_mount_status = CM.CheckMount_status_change ;
        CM.format_status_List.clear();

        /************顯示 明碼 暗碼*************/
        EyeClose=new Image("image/CloseEye.png",22,22,false, false);     
        EyeOpen=new Image("image/OpenEye5.png",22,22,false, false);        
        IP_changeAfterSaved =true;   //若沒有預設明碼或暗碼，以明碼為主  
        buttonIP.setGraphic(new ImageView(EyeOpen));       
        Name_changeAfterSaved =true; //若沒有預設明碼或暗碼，以明碼為主
        buttonUname.setGraphic(new ImageView(EyeOpen));        
        LoadLastChange();
        
        /*** checkbox Loading ***/
        checkBox_A_B_change=false;
        checkBox_AutoLogin_change=false;
        CM.GetLoginInformation();
        if(CM.getLoginInfo_con == 404){
            checkBox_A_B_change=false;
            checkBox_AutoLogin_change=false;
        }
        if(CM.getLoginInfo_con == 200){
            if(CM.GetisAnonymous.equals("true")){
                checkBox_A_B_change=true;
            }
            if(CM.GetisAnonymous.equals("false")){
                checkBox_A_B_change=false;
            }
            if(CM.GetisAutoLogin.equals("true")){
                checkBox_AutoLogin_change=true;
            }
            if(CM.GetisAutoLogin.equals("false")){
                checkBox_AutoLogin_change=false;
            }           
        }
        //LoadCheckChange();  //讀取 json檔 紀錄checkbox的變化 
        //checkBox_anonymous_binding.setSelected(checkBox_A_B_change);
        //checkBox_AutoLogin.setSelected(checkBox_AutoLogin_change);        
         /***  主畫面使用GRID的方式Layout ***/
        MainGP=new GridPane();        
        //MainGP.setGridLinesVisible(true); //開啟或關閉表格的分隔線       
        MainGP.setAlignment(Pos.CENTER);       
        MainGP.setPadding(new Insets(10, 15, 13, 15)); //設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        MainGP.setHgap(5); //元件間的水平距離
   	MainGP.setVgap(5); //元件間的垂直距離
        //MainGP.setPrefSize(540, 320);
        //MainGP.setMaxSize(540, 320);
        //MainGP.setMinSize(540, 320); 
        //MainGP.setStyle("-fx-background-color: rgb(0,180,240);-fx-background-radius: 4;-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 10, 0.0 , 0 , 5 );");
        
        /*** 狀態列-- 功能(下)畫面使用Anchor的方式Layout ***/
        anchorpane = new AnchorPane();
        anchorpane.setStyle("-fx-background-color: yellow ;");
        anchorpane.setPrefHeight(60);
        anchorpane.setMaxHeight(60);
        anchorpane.setMinHeight(60);

        /***  底層畫面使用Border的方式Layout ***/
        rootPane = new BorderPane();
        rootPane.setCenter(MainGP);       
        rootPane.setBottom(anchorpane);
        rootPane.setStyle("-fx-background-color: #0D47A1;");
        //rootPane.setStyle("-fx-background-color: rgb(0,180,240);");
        
        /* 採用文字*/
        Label Logo=new Label();         
        Logo.setAlignment(Pos.CENTER_LEFT);        
        Image  company_Logo=new Image("image/Logo.png");               
        Logo.setGraphic(new ImageView(company_Logo));        
        Logo.setMaxHeight(35);        
        Logo.setMinHeight(35);        
        Logo.setPrefHeight(35);
        //Logo.setId("Logo");
        MainGP.add(Logo,0,0,3,1); //2,1是指這個元件要佔用2格的column和1格的row 
        
         /***  多國語言設定UI ***/
        Language=new Label(WordMap.get("Language")+"  :");
        Language.setAlignment(Pos.CENTER_RIGHT);
        ObservableList<String> LanguageList=FXCollections.observableArrayList("繁體中文", "简体中文", "English");
        LanguageSelection=new ComboBox(LanguageList);
        //LanguageList.getClass().getResource(FontList.getStyle());
        /***  以下抓取變更語言後的動作  ***/
        LanguageSelection.setOnAction((Event event) -> {       
            try {
                
                LangComboChanged(LanguageSelection.getValue().toString());  //處理語言變更
            
            } catch (org.json.simple.parser.ParseException ex) {
                Logger.getLogger(acrored_ipsc.class.getName()).log(Level.SEVERE, null, ex);
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
        MyTitle=new Label("IPSC Advanced");       // 2019.03.08 william IPSC新增記住上一次連線的IP WordMap.get("Title") -> "IPSC Advanced"
        HBox WC=new HBox(MyTitle);
        WC.setAlignment(Pos.TOP_CENTER);
        //GridPane.setHalignment(MyTitle, HPos.LEFT);
        WC.setPadding(new Insets(0, 20, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        //WC.setSpacing(5); 

        MainGP.add(WC,1,1,2, 1);//1121
        
        /*******************  iSCSI儲存器IP ************************/       
        L_IP_Addr = new Label(WordMap.get("IP_Addr"));//+":"    
        GridPane.setHalignment(L_IP_Addr, HPos.LEFT);      
        L_IP_Addr.setMaxWidth(90);//強制限制Label最大寬度,讓每個Label在變換語言時不會變動
        L_IP_Addr.setMinWidth(90);//強制限制Label最小寬度,讓每個Label在變換語言時不會變動
        L_IP_Addr.setPrefWidth(90);//強制限制Label預設寬度,讓每個Label在變換語言時不會變動
	MainGP.add(L_IP_Addr, 0, 2);
        L_IP_Addr.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        
        Colon1=new Label(":");//將":"固定在一個位置
        GridPane.setHalignment(Colon1, HPos.LEFT);
        MainGP.add(Colon1, 1, 2); 
        Colon1.setPadding(new Insets(0, 5, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)  
        
        /*******iSCSI儲存器IP*******明碼 與 暗碼 轉換*************/        
        pwIP = new PasswordField();
         // Set initial state
        //pwIP.setManaged(false);
        //pwIP.setVisible(false);        
	MainGP.add(pwIP,2, 2);
        pwIP.setPrefSize(350, 38);
        pwIP.setMaxSize(350, 38);
        pwIP.setMinSize(350, 38);
        
        IP_Addr = new TextField(iscsicliIP);          
	MainGP.add(IP_Addr,2, 2);
        IP_Addr.setPrefSize(350, 38);
        IP_Addr.setMaxSize(350, 38);
        IP_Addr.setMinSize(350, 38);
        //IP_Addr.setPrefHeight(39);
        //IP_Addr.setMaxHeight(39);
        //IP_Addr.setMinHeight(39);
        //IP_Addr.setStyle("-fx-background-image:url('images/storage.png');fx-background-repeat: no-repeat;-fx-background-position: right center;");
        //IP_Addr.setPromptText(WordMap.get("IP_Addr"));
        //IP_Addr.setId("user");
        
        MainGP.add(buttonIP,2, 2);      
        buttonIP.setAlignment(Pos.CENTER);        
        buttonIP.setTranslateX(351);  //移動TextField內的Button位置    
        buttonIP.setTranslateY(-1);
        buttonIP.setPrefHeight(37);
        buttonIP.setMaxHeight(37);
        buttonIP.setMinHeight(37);        
        /************讀取--IP_changeAfterSaved--為明碼或暗碼 若明碼為true,暗碼為false**************/
        if(IP_changeAfterSaved==true){
            
            pwIP.setManaged(false);
            pwIP.setVisible(false);   
            buttonIP.setGraphic(new ImageView(EyeOpen));
            
            buttonIP.setOnAction(new EventHandler<ActionEvent>() {
                @Override        
                public void handle(ActionEvent e) {                     
                    if(IP_changeAfterSaved==true){                   

                        if (e.getEventType().equals(ActionEvent.ACTION)){                    

                            pwIP.managedProperty().bind(buttonIP.selectedProperty());
                            pwIP.visibleProperty().bind(buttonIP.selectedProperty());

                            IP_Addr.managedProperty().bind(buttonIP.selectedProperty().not());
                            IP_Addr.visibleProperty().bind(buttonIP.selectedProperty().not());
                            pwIP.managedProperty().bind(buttonIP.selectedProperty());
                            pwIP.visibleProperty().bind(buttonIP.selectedProperty());

                            buttonIP.setGraphic(new ImageView(EyeClose));
                        } 

                        IP_changeAfterSaved=false;

                    }           
                    else{

                        if (e.getEventType().equals(ActionEvent.ACTION)){                 

                            IP_Addr.managedProperty().bind(buttonIP.selectedProperty().not());
                            IP_Addr.visibleProperty().bind(buttonIP.selectedProperty().not());

                            pwIP.managedProperty().bind(buttonIP.selectedProperty());
                            pwIP.visibleProperty().bind(buttonIP.selectedProperty());
                            IP_Addr.managedProperty().bind(buttonIP.selectedProperty().not());
                            IP_Addr.visibleProperty().bind(buttonIP.selectedProperty().not());

                            buttonIP.setGraphic(new ImageView(EyeOpen));   
                        } 

                        IP_changeAfterSaved=true;

                    }
                    //IP_Addr.textProperty().bindBidirectional(pwIP.textProperty());
                    pwIP.textProperty().bindBidirectional(IP_Addr.textProperty());           
                    //System.out.println("IP_changeAfterSaved : "+IP_changeAfterSaved+"\n");
                    WriteLastChange(); //記錄明碼與暗碼
                }
            });
                   
        }else{
            
            IP_Addr.setManaged(false);
            IP_Addr.setVisible(false);         
            buttonIP.setGraphic(new ImageView(EyeClose));
            pwIP.setText(iscsicliIP); 
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

                            IP_Addr.managedProperty().bind(buttonIP.selectedProperty());
                            IP_Addr.visibleProperty().bind(buttonIP.selectedProperty());
                            pwIP.managedProperty().bind(buttonIP.selectedProperty().not());
                            pwIP.visibleProperty().bind(buttonIP.selectedProperty().not());

                            buttonIP.setGraphic(new ImageView(EyeClose));
                        } 

                    IP_changeAfterSaved=false; 

                    }           
                    else{                           
                        if (e.getEventType().equals(ActionEvent.ACTION)){ 

                            IP_Addr.managedProperty().bind(buttonIP.selectedProperty());
                            IP_Addr.visibleProperty().bind(buttonIP.selectedProperty());

                            pwIP.managedProperty().bind(buttonIP.selectedProperty().not());
                            pwIP.visibleProperty().bind(buttonIP.selectedProperty().not());
                            IP_Addr.managedProperty().bind(buttonIP.selectedProperty());
                            IP_Addr.visibleProperty().bind(buttonIP.selectedProperty());

                            buttonIP.setGraphic(new ImageView(EyeOpen));   
                        } 

                    IP_changeAfterSaved=true;

                    }

                IP_Addr.textProperty().bindBidirectional(pwIP.textProperty());
                //pwIP.textProperty().bindBidirectional(IP_Addr.textProperty());           
                //System.out.println("IP_changeAfterSaved : "+IP_changeAfterSaved+"\n");
                WriteLastChange(); //記錄明碼與暗碼
                }
            });
        }    
        
        /****************  登入帳號 ********************/
	userName = new Label(WordMap.get("Username"));        
        GridPane.setHalignment(userName, HPos.LEFT);         
	MainGP.add(userName, 0, 5);
        //userName.setPadding(new Insets(0, 0, 0, 10));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        userName.setMaxWidth(90);//強制限制Label最大寬度,讓每個Label在變換語言時不會變動
        userName.setMinWidth(90);//強制限制Label最小寬度,讓每個Label在變換語言時不會變動
        userName.setPrefWidth(90);//強制限制Label預設寬度,讓每個Label在變換語言時不會變動
        
        Colon2=new Label(":");//將":"固定在一個位置
        //Colon2.setPadding(new Insets(0, 0, 0, 0)); //設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        GridPane.setHalignment(Colon2, HPos.LEFT);        
        MainGP.add(Colon2, 1, 5);   
        
        /**********輸入使用者帳號*********明碼 與 暗碼 轉換*************/
        pwName = new PasswordField();
        // Set initial state
        //pwName.setManaged(false);
        //pwName.setVisible(false);
	MainGP.add(pwName,2, 5);
        pwName.setPrefSize(350, 38);
        pwName.setMaxSize(350, 38);
        pwName.setMinSize(350, 38);
        
        userTextField = new TextField(CurrentUserName);      
	MainGP.add(userTextField, 2, 5);
        userTextField.setPrefSize(350, 38);
        userTextField.setMaxSize(350, 38);
        userTextField.setMinSize(350, 38);
        //userTextField.setPrefHeight(39);
        //userTextField.setMaxHeight(39);
        //userTextField.setMinHeight(39);
        
        MainGP.add(buttonUname,2, 5);     
        //buttonUname.setStyle("-fx-background-color: white;");
        buttonUname.setAlignment(Pos.CENTER);       
        buttonUname.setTranslateX(351);  //移動TextField內的Button位置 
        buttonUname.setTranslateY(-1);
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

                            userTextField.managedProperty().bind(buttonUname.selectedProperty().not());
                            userTextField.visibleProperty().bind(buttonUname.selectedProperty().not());                    
                            pwName.managedProperty().bind(buttonUname.selectedProperty());
                            pwName.visibleProperty().bind(buttonUname.selectedProperty());
                            
                            buttonUname.setGraphic(new ImageView(EyeClose));
                        } 

                        Name_changeAfterSaved=false;

                    }           
                    else{              
                        if (e.getEventType().equals(ActionEvent.ACTION)){                   

                            userTextField.managedProperty().bind(buttonUname.selectedProperty().not());
                            userTextField.visibleProperty().bind(buttonUname.selectedProperty().not());

                            pwName.managedProperty().bind(buttonUname.selectedProperty());
                            pwName.visibleProperty().bind(buttonUname.selectedProperty());
                            userTextField.managedProperty().bind(buttonUname.selectedProperty().not());
                            userTextField.visibleProperty().bind(buttonUname.selectedProperty().not());

                            buttonUname.setGraphic(new ImageView(EyeOpen));
                        }                 

                        Name_changeAfterSaved=true;       

                    }

                    pwName.textProperty().bindBidirectional(userTextField.textProperty());           
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

                            userTextField.managedProperty().bind(buttonUname.selectedProperty());
                            userTextField.visibleProperty().bind(buttonUname.selectedProperty());                    
                            pwName.managedProperty().bind(buttonUname.selectedProperty().not());
                            pwName.visibleProperty().bind(buttonUname.selectedProperty().not());
                            buttonUname.setGraphic(new ImageView(EyeClose));
                        } 

                        Name_changeAfterSaved=false;

                    }           
                    else{             
                        if (e.getEventType().equals(ActionEvent.ACTION)){                   

                            userTextField.managedProperty().bind(buttonUname.selectedProperty());
                            userTextField.visibleProperty().bind(buttonUname.selectedProperty());

                            pwName.managedProperty().bind(buttonUname.selectedProperty().not());
                            pwName.visibleProperty().bind(buttonUname.selectedProperty().not());
                            userTextField.managedProperty().bind(buttonUname.selectedProperty());
                            userTextField.visibleProperty().bind(buttonUname.selectedProperty());
                            buttonUname.setGraphic(new ImageView(EyeOpen));        
                        }   

                        Name_changeAfterSaved=true;   

                    }
                    userTextField.textProperty().bindBidirectional(pwName.textProperty());           
                    //System.out.println("Name_changeAfterSaved-final : "+Name_changeAfterSaved+"\n");
                    WriteLastChange(); //記錄明碼與暗碼 
                }
            });
        }
        
        /**********************  登入密碼 ***********************/
        pw = new Label(WordMap.get("Password"));
        GridPane.setHalignment(pw, HPos.LEFT);
	MainGP.add(pw, 0, 8);
        //pw.setPadding(new Insets(0, 0, 0, 10));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)      
        pw.setMaxWidth(90);//強制限制Label最大寬度,讓每個Label在變換語言時不會變動
        pw.setMinWidth(90);//強制限制Label最小寬度,讓每個Label在變換語言時不會變動
        pw.setPrefWidth(90);//強制限制Label預設寬度,讓每個Label在變換語言時不會變動
        
        Colon3=new Label(":");//將":"固定在一個位置      
        GridPane.setHalignment(Colon3, HPos.LEFT);
        MainGP.add(Colon3, 1, 8);
        
        /*******輸入密碼********明碼 與 暗碼 轉換*******************/  
        pwBox = new PasswordField();     
        //pwBox.setText("000000000000");
	MainGP.add(pwBox,2, 8);
        pwBox.setPrefSize(350,38); //328,39
        pwBox.setMaxSize(350,38);
        pwBox.setMinSize(350, 38);
        //pwBox.setPrefHeight(39);
        //pwBox.setMaxHeight(39);
        //pwBox.setMinHeight(39);
        
        textField01 = new TextField();
        // Set initial state
        textField01.setManaged(false);
        textField01.setVisible(false);
        MainGP.add(textField01,2, 8); 
        textField01.setPrefSize(350, 38);
        textField01.setMaxSize(350, 38);
        textField01.setMinSize(350, 38);
        
        buttonPw.setGraphic(new ImageView(EyeClose));
        MainGP.add(buttonPw,2, 8);     
        //buttonPw.setStyle("-fx-background-color: white;");
        //buttonPw.visibleProperty().bind( pwBox.textProperty().isEmpty().not() );
        buttonPw.setAlignment(Pos.CENTER);
        buttonPw.setTranslateX(351); //移動TextField內的Button位置
        buttonPw.setTranslateY(-1);
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
        
        /**checkbox**/
        checkBox_AutoLogin = new CheckBox(""+WordMap.get("Auto_Login")); 
        checkBox_AutoLogin.setAlignment(Pos.CENTER_LEFT);
        MainGP.add(checkBox_AutoLogin, 2, 11);//2,11         
        
        /**checkbox**/
        checkBox_anonymous_binding= new CheckBox(""+WordMap.get("anonymous_binding"));
        checkBox_anonymous_binding.setAlignment(Pos.CENTER_LEFT);
        MainGP.add(checkBox_anonymous_binding, 2, 11);//2,11,3,2    
        checkBox_anonymous_binding.setTranslateX(200); //移動TextField內的Button位置 
        
        
        /*** 讀取 json --> 若true 則checkbox 會顯示已選擇 ***/
        if( checkBox_A_B_change == true ){            
            checkBox_anonymous_binding.setSelected(checkBox_A_B_change);
            pwName.setEditable(false);
            userTextField.setEditable(false);
            pwBox.setEditable(false);
            textField01.setEditable(false);            
        }
        if( checkBox_AutoLogin_change==true ){
            checkBox_AutoLogin_change=true;
            checkBox_AutoLogin.setSelected(checkBox_AutoLogin_change);
        }        
        /*** 匿名/綁定 Action ***/
        checkBox_anonymous_binding.setOnAction((ActionEvent event) -> {
            checkBox_A_B_change = checkBox_anonymous_binding.isSelected();//匿名/綁定 選擇(true/false)
            if(checkBox_anonymous_binding.isSelected()){                
                //System.out.println("checkBox_anonymous_binding.isSelected() \n");                
                pwName.setEditable(false);
                userTextField.setEditable(false);
                pwBox.setEditable(false);
                textField01.setEditable(false);
                Task<Void> checkBox_A_B_true_sleeper = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        QM.SaveLoginInformation(iscsicliIP, CurrentUserName, CurrentPasseard, checkBox_A_B_change, checkBox_AutoLogin_change);
                        return null;
                    }
                };
                checkBox_A_B_true_sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        //System.out.println("** Refresh sleeper.setOnSucceeded ** \n");                     
                    }
                });
                new Thread(checkBox_A_B_true_sleeper).start();
                /*
                if (IP_Addr.getText() == null || IP_Addr.getText().trim().isEmpty()) {
                    IP_Addr.setId("autoLogin_true");
                }else{
                    IP_Addr.setId("");
                }
                */
            }else{
                pwName.setEditable(true);
                userTextField.setEditable(true);
                pwBox.setEditable(true);
                textField01.setEditable(true); 
                Task<Void> checkBox_A_B_false_sleeper = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        QM.SaveLoginInformation(iscsicliIP, CurrentUserName, CurrentPasseard, checkBox_A_B_change, checkBox_AutoLogin_change);
                        return null;
                    }
                };
                checkBox_A_B_false_sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        //System.out.println("** Refresh sleeper.setOnSucceeded ** \n");                     
                    }
                });
                new Thread(checkBox_A_B_false_sleeper).start();
            }        
        });
        
        /*** 自動登入 Action ***/
        checkBox_AutoLogin.setOnAction((ActionEvent event) -> {              
            checkBox_A_B_change = checkBox_anonymous_binding.isSelected();//匿名/綁定 選擇(true/false)
            checkBox_AutoLogin_change = checkBox_AutoLogin.isSelected();//自動登入 選擇(true/false) 
            if(IP_Addr.getText()!=null){
                iscsicliIP=IP_Addr.getText();
            }else{
                iscsicliIP=""; 
            }
            //選取->匿名/綁定 = ture ; 未選取->匿名/綁定 = false 
            if(checkBox_A_B_change == true){
                CurrentUserName="anonymous";
                CurrentPasseard = "0";             
            }else{                    
                if(userTextField.getText()!=null){
                    CurrentUserName=userTextField.getText();
                }else{
                    CurrentUserName="";              
                }  
                
                WriteLastStatus();
               
                if(pwBox.getText()!=null){
                    CurrentPasseard=pwBox.getText();                            
                }else{
                    CurrentPasseard="";
                }              
            } 
            //取消:checkbo自動登入，會把servers中的自動登入改寫成 false!!
            if(!checkBox_AutoLogin.isSelected()){
                System.out.println("checkBox_AutoLogin.isSelected() == false \n");                
                try {
                    QM.SaveLoginInformation(iscsicliIP, CurrentUserName, CurrentPasseard, checkBox_A_B_change, checkBox_AutoLogin_change);
                    //WriteLastCheckboxChange();
                } catch (IOException ex) {
                    Logger.getLogger(acrored_ipsc.class.getName()).log(Level.SEVERE, null, ex);
                }   
            }else{
                //checkbo自動登入，僅驗證 IP是否合法 與 帳密 是否可正確登入 但不會有連線錯誤的問題
                System.out.println("checkBox_AutoLogin.isSelected() == true \n"); 
                /*****Pattern:檢查IP是否符合正確格式*****/
                Boolean Result_Mount =PATTERN.matcher(iscsicliIP).matches();
                //System.out.println(" -----Result_Mount------"+Result_Mount+"\n");
                //驗證IP是否合法
                if(Result_Mount==true||IP_Addr!=null){ // 2017.07.28 william IP use domain name
                    /***判斷 iscsicliIP 是 IPv4 或 IPv6 ***/
                    try { // 2017.07.28 william 新增domain處理及IPv4/IPv6判斷處理
                        InetAddress address = InetAddress.getByName(iscsicliIP);
                        String ip = address.getHostAddress();
                        if (address instanceof Inet4Address){ // 2017.07.28 william 新增domain處理及IPv4/IPv6判斷處理 iscsicliIP.replaceAll("\\d", "").length() == 3
//                            iscsicliIPv4 = iscsicliIP;             
                            iscsicliIPv4 = ip; // 2017.07.28 william 新增domain處理及IPv4/IPv6判斷處理
                            /***讓 Stage 不會沒有回應 可以等待 thread 結束***/
                            Task<Void> sleeper = new Task<Void>() {
                                @Override
                                protected Void call() throws Exception {  
                                    //驗證帳號密碼 是否正確
                                    CAL.CheckAutoLoginInfo(iscsicliIPv4, CurrentUserName, CurrentPasseard, checkBox_A_B_change, checkBox_AutoLogin_change);
                                    //若正確，則寫入servers中
                                    QM.SaveLoginInformation(iscsicliIPv4, CurrentUserName, CurrentPasseard, checkBox_A_B_change, checkBox_AutoLogin_change);
                                    //紀錄 checkbox的變化
                                    //WriteLastCheckboxChange();                               
                                    return null;
                                }
                            };
                            sleeper.setOnFailed(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    System.out.println("** Mount sleeper.setOnFailed ** \n");                                          
                                    /***帳號或密碼錯誤***/
                                    if( CAL.AutoLogin_ResponseCode == 401 ){
                                        System.out.println("** CAL.AutoLogin_ResponseCode == 401 ** \n");
                                        AutoLoginAlert();
                                    }
                                    else{
                                        //checkbo自動登入，僅驗證帳密是否可正確登入 但不會有連線錯誤的問題
                                        System.out.println("** iSCSI-connect Erroe!! ** \n");
                                        AutoLoginIPAlert();
                                    }
                                    checkBox_AutoLogin.setSelected(false);
                                    checkBox_AutoLogin_change = checkBox_AutoLogin.isSelected();
                                    checkBox_A_B_change = checkBox_anonymous_binding.isSelected();//匿名/綁定 選擇(true/false)
                                    //System.out.println(" /*/checkBox_AutoLogin_change :: "+ checkBox_AutoLogin_change +" \n");
                                    //WriteLastCheckboxChange();
                                }
                            });   
                            sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    System.out.println("** checkBox_AutoLogin.setOnSucceeded ** \n");                           
                                }
                            });
                            new Thread(sleeper).start();
                        }
                        else if(address instanceof Inet6Address){  // 2017.07.28 william 新增domain處理及IPv4/IPv6判斷處理
//                            iscsicliIPv6 = "["+ iscsicliIP + "]" ;   
                            iscsicliIPv6 = "["+ ip + "]" ; // 2017.07.28 william 新增domain處理及IPv4/IPv6判斷處理
                            /***讓 Stage 不會沒有回應 可以等待 thread 結束***/
                            Task<Void> sleeper = new Task<Void>() {
                                @Override
                                protected Void call() throws Exception { 
                                    //驗證帳號密碼 是否正確
                                    CAL.CheckAutoLoginInfo(iscsicliIPv6, CurrentUserName, CurrentPasseard, checkBox_A_B_change, checkBox_AutoLogin_change);
                                    //若正確，則寫入servers中
                                    QM.SaveLoginInformation(iscsicliIPv6, CurrentUserName, CurrentPasseard, checkBox_A_B_change, checkBox_AutoLogin_change);
                                    //紀錄 checkbox的變化
                                    //WriteLastCheckboxChange();                               
                                    return null;
                                }
                            };
                            sleeper.setOnFailed(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    System.out.println("** Mount sleeper.setOnFailed ** \n");                                          
                                    /***帳號或密碼錯誤***/
                                    if( CAL.AutoLogin_ResponseCode == 401 ){
                                        System.out.println("** QM.Login_ResponseCode == 401 ** \n");
                                        AutoLoginAlert();
                                    }
                                    else{
                                        //checkbo自動登入，僅驗證帳密是否可正確登入 但不會有連線錯誤的問題
                                        System.out.println("** iSCSI-connect Erroe!! ** \n");
                                        AutoLoginIPAlert();
                                    }
                                }
                            }); 
                            sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    System.out.println("** checkBox_AutoLogin.setOnSucceeded ** \n");                           
                                }
                            });
                            new Thread(sleeper).start();
                        }
                    } 
                    catch (UnknownHostException ex) {
                        IPLoginAlert();
//                        Logger.getLogger(acrored_ipsc.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else{
                    AutoLoginResultAlert(); 
                }
            }
        });          
        /*
        if(checkBox_Pw.isSelected()==checkBox_Pw_change){     

            pwBox.clear(); //若使用者登入後 密碼會自動清除
            
        }else{
            //記住密碼
        }
        */        
        /*******  以下擺放操作功能 *******/  
        /*
        HBox checkBox_aba=new HBox();
        checkBox_aba.getChildren().addAll(checkBox_anonymous_binding,checkBox_AutoLogin);
        checkBox_aba.setAlignment(Pos.CENTER_LEFT);
        //checkBox_aba.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        checkBox_aba.setSpacing(15); 
        MainGP.add(checkBox_aba, 2, 11,1,1);//2,11,3,2
        */

        
        /***** 掛 載 *****/
        Mount=new Button(WordMap.get("Mount"));
        Mount.setPrefSize(80,30);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        Mount.setMaxSize(80,30);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        Mount.setMinSize(80,30);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動    
        Mount.setId("button");
        Mount.setOnAction((ActionEvent event) -> {
            QM.MountResponseCode = -1;
            
            if( lock_button_Mount==false ) {
                lock_button_Mount=true;
                updateMount();
                
                checkBox_A_B_change = checkBox_anonymous_binding.isSelected();//匿名/綁定 選擇(true/false)
                checkBox_AutoLogin_change = checkBox_AutoLogin.isSelected();//自動登入 選擇(true/false)            

                if(IP_Addr.getText()!=null){
                    iscsicliIP=IP_Addr.getText();
                }else{
                    iscsicliIP=""; 
                }   

                //選取->匿名/綁定 = ture ; 未選取->匿名/綁定 = false 
                if(checkBox_A_B_change == true){
                    CurrentUserName="anonymous";
                    CurrentPasseard = "0";             
                }else{    

                    if(userTextField.getText()!=null){
                        CurrentUserName=userTextField.getText();
                    }else{
                        CurrentUserName="";              
                    }
           
                    WriteLastStatus();
             
                    if(pwBox.getText()!=null){
                        CurrentPasseard=pwBox.getText();                            
                    }else{
                        CurrentPasseard="";
                    }  

                }          

                /*** 掛載中 -- thread 畫面鎖住  ***/       
                MountGP=new GridPane();        
                //MountGP.setGridLinesVisible(true); //開啟或關閉表格的分隔線       
                MountGP.setAlignment(Pos.BASELINE_CENTER);       
                //MountGP.setPadding(new Insets(0, 0, 0, 0)); //設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
                MountGP.setHgap(5); //元件間的水平距離
                MountGP.setVgap(5); //元件間的垂直距離
                MountGP.setPrefSize(530, 235); //400, 480
                MountGP.setMaxSize(530, 235);
                MountGP.setMinSize(530, 235);
                MountGP.setTranslateY(45);
                MountGP.setStyle("-fx-background-color: #CFD8DC ; -fx-opacity: 0.8 ; -fx-background-radius: 4;-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 10, 0.0 , 0 , 5 );");
                
                if("English".equals(LangNow)) {
                    Mount_title=new Label(WordMap.get("Thread_Mount")+","+WordMap.get("Thread_Waiting") + "...");
                    Mount_title.setTranslateX(50);
                    Mount_title.setTranslateY(20);    
                }        
                else { 
                    Mount_title=new Label(WordMap.get("Thread_Mount")+"，"+WordMap.get("Thread_Waiting") + "...");
                    Mount_title.setTranslateX(105); // 100 -> 60
                    Mount_title.setTranslateY(20);                
                }                  
                                                
                Mount_title.setAlignment(Pos.CENTER);                
                Mount_title.setId("Thread_status");
                MountGP.add(Mount_title,0,0); //2,1是指這個元件要佔用2格的column和1格的row 
                
                // 2019.03.08 william IPSC新增記住上一次連線的IP                              
                Label Mount_tips = new Label(WordMap.get("Mount_Connecting_tips"));
                Label Mount_tips_eng0 = new Label(WordMap.get("Mount_Connecting_tips_eng0"));                
                Label Mount_tips_eng1 = new Label(WordMap.get("Mount_Connecting_tips_eng1"));
                Label Mount_tips_eng2 = new Label(WordMap.get("Mount_Connecting_tips_eng2"));
                Mount_tips.setAlignment(Pos.CENTER);
                Mount_tips_eng0.setAlignment(Pos.CENTER);
                Mount_tips_eng1.setAlignment(Pos.CENTER);
                Mount_tips_eng2.setAlignment(Pos.CENTER);
                
                if("English".equals(LangNow)) {
                    Mount_tips_eng0.setStyle("-fx-font-size: 14px;-fx-text-fill: #1A237E;-fx-font-weight: 900;-fx-font-family: 'Microsoft JhengHei';");
                    Mount_tips_eng1.setStyle("-fx-font-size: 14px;-fx-text-fill: #1A237E;-fx-font-weight: 900;-fx-font-family: 'Microsoft JhengHei';");
                    Mount_tips_eng2.setStyle("-fx-font-size: 14px;-fx-text-fill: #1A237E;-fx-font-weight: 900;-fx-font-family: 'Microsoft JhengHei';");
                    Mount_tips_eng0.setTranslateY(15);
                    Mount_tips_eng1.setTranslateY(15);
                    Mount_tips_eng2.setTranslateY(20);
                    Mount_tips_eng2.setTranslateX(0);
                    MountGP.add(Mount_tips_eng0,0,2);                       
                    MountGP.add(Mount_tips_eng1,0,3);                       
                    MountGP.add(Mount_tips_eng2,0,4);    
                }        
                else { 
//                    Mount_tips.setStyle("-fx-font-size: 24px;-fx-text-fill: #1A237E;-fx-font-weight: 900;-fx-font-family: 'Microsoft JhengHei';");
//                    Mount_tips.setTranslateY(40);
//                    MountGP.add(Mount_tips,0,3);   
                    Mount_tips_eng1.setStyle("-fx-font-size: 20px;-fx-text-fill: #1A237E;-fx-font-weight: 900;-fx-font-family: 'Microsoft JhengHei';");
                    Mount_tips_eng2.setStyle("-fx-font-size: 20px;-fx-text-fill: #1A237E;-fx-font-weight: 900;-fx-font-family: 'Microsoft JhengHei';");
                    Mount_tips_eng1.setTranslateY(15);
                    Mount_tips_eng2.setTranslateY(25);
                    Mount_tips_eng2.setTranslateX(120);
                    MountGP.add(Mount_tips_eng1,0,3);                       
                    MountGP.add(Mount_tips_eng2,0,4);  
                }                  
                
              

                ProgressIndicator p1 = new ProgressIndicator();
                if("English".equals(LangNow)) {
                    p1.setTranslateY(30);
                } else { 
                    p1.setTranslateY(30);
                }                 
                
                //p1.setStyle("-fx-foreground-color: #1A237E");
                MountGP.add(p1,0,5); //2,1是指這個元件要佔用2格的column和1格的row 
                
                

                StackPane stack = new StackPane();
                stack.getChildren().addAll(MainGP, MountGP);
                
                /*Start-----------------------------------------------------------------------------------------------------------------------------------------------*/ 
                
                /*****Pattern:檢查IP是否符合正確格式*****/
                Boolean Result_Mount =PATTERN.matcher(iscsicliIP).matches();
                //System.out.println(" -----Result_Mount------"+Result_Mount+"\n");
                if(Result_Mount==true||IP_Addr!=null){ // 2017.07.28 william 新增domain處理及IPv4/IPv6判斷處理            
                    /***判斷 iscsicliIP 是 IPv4 或 IPv6 ***/
                    try { // 2017.07.28 william 新增domain處理及IPv4/IPv6判斷處理
                        InetAddress address = InetAddress.getByName(iscsicliIP);
                        String ip = address.getHostAddress();
                        if (address instanceof Inet4Address){ // 2017.07.28 william 新增domain處理及IPv4/IPv6判斷處理 iscsicliIP.replaceAll("\\d", "").length() == 3
//                            iscsicliIPv4 = iscsicliIP;
                            iscsicliIPv4 = ip; // 2017.07.28 william 新增domain處理及IPv4/IPv6判斷處理
                            System.out.println("ipv4 : "+ iscsicliIPv4 +"\n");
                            rootPane.setCenter(stack);

                            /***讓 Stage 不會沒有回應 可以等待 thread 結束***/
                            Task<Void> sleeper = new Task<Void>() {
                                @Override
                                protected Void call() throws Exception {                                                                        
                                    System.out.println("** call01 ** \n");
                                    if(checkBox_A_B_change==true){
                                        QM.SetLoginInfo(iscsicliIPv4, CurrentUserName, CurrentPasseard, checkBox_A_B_change, checkBox_AutoLogin_change);
                                    }
                                    else{
                                        QM.LoginStorage(iscsicliIPv4, CurrentUserName, CurrentPasseard, checkBox_A_B_change, checkBox_AutoLogin_change);
                                    }                                    
                                    return null;
                                }
                            };

                            sleeper.setOnFailed(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    System.out.println("** Mount sleeper.setOnFailed ** \n");                                          
                                    /***帳號或密碼錯誤***/
                                    if( QM.Login_ResponseCode == 401 ){
                                        System.out.println("** QM.Login_ResponseCode == 401 ** \n");
                                        LoginAlert();
                                    }
                                    else{
                                        System.out.println("** iSCSI-connect Erroe!! ** \n");
                                        IPLoginAlert();
                                    }
                                    lock_button_Mount=false;
                                }
                            });

                            sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    System.out.println("** sleeper.setOnSucceeded ** \n");
                                    System.out.println("** fotmat **"+ QM.PhysicalName_List +" \n");
                                    if(QM.MountCode200 == true){ //QM.MountResponseCode >= 200 && QM.MountResponseCode <= 206
                                        //掛載成功
                                        try {
                                            if(checkBox_AutoLogin_change == true){
                                                QM.SaveLoginInformation(iscsicliIPv4, CurrentUserName, CurrentPasseard, checkBox_A_B_change, checkBox_AutoLogin_change);
                                            }
                                            //WriteLastCheckboxChange();                                            
                                            // 2019.03.08 william IPSC新增記住上一次連線的IP
                                            QM.GetServiceResponseData();
                                            QM.SetServiceResponseData(GB.dictTargetIP);
                                            MountSuccessAlertCode(QM.MountResponseCode);
 
                                            

                                        } 
                                        catch (IOException ex) {
                                            Logger.getLogger(acrored_ipsc.class.getName()).log(Level.SEVERE, null, ex);
                                        }  

                                        // 2019.03.08 william IPSC新增記住上一次連線的IP
                                        /********若PhysicalName_List != null,詢問是否需要format **********/
//                                        if(QM.PhysicalName_List != null && !QM.PhysicalName_List.isEmpty()){  
//
//                                            System.out.println("-------PhysicalName_List != null------\n");
//
//                                            Alert alert01 = new Alert(Alert.AlertType.CONFIRMATION);
//                                            alert01.setTitle(WordMap.get("Message_Format"));//設定對話框視窗的標題列文字  , "您真的要結束程式嗎？", ButtonType.YES, ButtonType.NO
//                                            alert01.setHeaderText(null);
//                                            alert01.getDialogPane().setMinSize( 320 ,Region.USE_PREF_SIZE);
//                                            alert01.getDialogPane().setMaxSize( 320 ,Region.USE_PREF_SIZE);
//                                            alert01.setContentText(WordMap.get("Message_Format_01"));//VD 已上線,是否還要連線,若連線,則前一個連線會自動斷線.
//                                            ButtonType buttonTypeOK01 = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
//                                            ButtonType buttonTypeCancel = new ButtonType(WordMap.get("Alert_Cancel"),ButtonBar.ButtonData.CANCEL_CLOSE);
//                                            alert01.getButtonTypes().setAll(buttonTypeOK01, buttonTypeCancel); 
//
//                                            Optional<ButtonType> result01 = alert01.showAndWait(); 
//                                            if (result01.get() == buttonTypeOK01){                   
//                                                lock_format=true;
//                                                formatPane();
//                                                /***讓 Stage 不會沒有回應 可以等待 thread 結束***/
//                                                Task<Void> format_sleeper = new Task<Void>() {
//                                                    @Override
//                                                    protected Void call() throws Exception {
//                                                        System.out.println("** FormatDisk call01 ** \n");                    
//                                                        QM.FormatDisk(QM.PhysicalName_List);
//
//                                                        return null;
//                                                    }
//                                                };
//                                                format_sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//                                                    @Override
//                                                    public void handle(WorkerStateEvent event) {
//                                                        System.out.println("** Format sleeper.setOnSucceeded ** \n");
//                                                        fotmatAlertCode(QM.Format_ResponseCode);
//                                                        lock_format=false;
//                                                    }
//                                                });
//                                                new Thread(format_sleeper).start();
//
//                                            } 
//                                            else {
//                                                // ... user chose CANCEL or closed the dialog
//                                                alert01.close();
//                                            }                
//                                        }
                                        
                                    }
                                    if(QM.MountClear_ResponseCode >= 200 && QM.MountClear_ResponseCode <= 206 ){

                                        MountFailedAlertCode(QM.MountClear_ResponseCode);

                                    }                            
                                    if( QM.Storage_Status.equals("6") ){
                                        LoginAlert();
                                    }
                                    if(QM.StatusCode6 == false){
                                        StorageLoginFailedAlert();
                                    }                                
                                    lock_button_Mount=false;
                                }
                            });
                            new Thread(sleeper).start();
                        } 
                        else if(address instanceof Inet6Address) { // 2017.07.28 william 新增domain處理及IPv4/IPv6判斷處理
//                            iscsicliIPv6 = "["+ iscsicliIP + "]" ;
                            iscsicliIPv6 = "["+ ip + "]" ; // 2017.07.28 william 新增domain處理及IPv4/IPv6判斷處理
                            System.out.println("ipv6 : "+ iscsicliIPv6 +"\n");
                            rootPane.setCenter(stack);

                            /***讓 Stage 不會沒有回應 可以等待 thread 結束***/
                            Task<Void> sleeper = new Task<Void>() {
                                @Override
                                protected Void call() throws Exception {
                                    System.out.println("** call01 ** \n");
                                    if(checkBox_A_B_change==true){
                                        QM.SetLoginInfo(iscsicliIPv6, CurrentUserName, CurrentPasseard, checkBox_A_B_change, checkBox_AutoLogin_change);
                                    }
                                    else{
                                        QM.LoginStorage(iscsicliIPv6, CurrentUserName, CurrentPasseard, checkBox_A_B_change, checkBox_AutoLogin_change);
                                    }
                                    return null;
                                }
                            };

                            sleeper.setOnFailed(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    System.out.println("** Mount sleeper.setOnFailed ** \n");                                          
                                    /***帳號或密碼錯誤***/
                                    if( QM.Login_ResponseCode == 401 ){
                                        System.out.println("** QM.Login_ResponseCode == 401 ** \n");
                                        LoginAlert();
                                    }
                                    else{
                                        System.out.println("** iSCSI-connect Erroe!! ** \n");
                                        IPLoginAlert();
                                    }
                                    lock_button_Mount=false;
                                }
                            });

                            sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                                @Override
                                public void handle(WorkerStateEvent event) {
                                    System.out.println("** sleeper.setOnSucceeded ** \n");
                                    if(QM.MountCode200==true){
                                        //掛載成功
                                        try {
                                            if(checkBox_AutoLogin_change == true){
                                                QM.SaveLoginInformation(iscsicliIPv6, CurrentUserName, CurrentPasseard, checkBox_A_B_change, checkBox_AutoLogin_change);
                                            }
                                            //WriteLastCheckboxChange();
                                            // 2019.03.08 william IPSC新增記住上一次連線的IP
                                            QM.GetServiceResponseData();
                                            QM.SetServiceResponseData(GB.dictTargetIP);                                            
                                            MountSuccessAlertCode(QM.MountResponseCode);

                                        } 
                                        catch (IOException ex) {
                                            Logger.getLogger(acrored_ipsc.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                        // 2019.03.08 william IPSC新增記住上一次連線的IP
                                        /********若PhysicalName_List != null,詢問是否需要format **********/
//                                        if(QM.PhysicalName_List != null && !QM.PhysicalName_List.isEmpty()){  
//
//                                            System.out.println("-------PhysicalName_List != null------\n");
//
//                                            Alert alert01 = new Alert(Alert.AlertType.CONFIRMATION);
//                                            alert01.setTitle(WordMap.get("Message_Format"));//設定對話框視窗的標題列文字  , "您真的要結束程式嗎？", ButtonType.YES, ButtonType.NO
//                                            alert01.setHeaderText(null);
//                                            alert01.getDialogPane().setMinSize( 320 ,Region.USE_PREF_SIZE);
//                                            alert01.getDialogPane().setMaxSize( 320 ,Region.USE_PREF_SIZE);
//                                            alert01.setContentText(WordMap.get("Message_Format_01"));//VD 已上線,是否還要連線,若連線,則前一個連線會自動斷線.
//                                            ButtonType buttonTypeOK01 = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
//                                            ButtonType buttonTypeCancel = new ButtonType(WordMap.get("Alert_Cancel"),ButtonBar.ButtonData.CANCEL_CLOSE);
//                                            alert01.getButtonTypes().setAll(buttonTypeOK01, buttonTypeCancel); 
//
//                                            Optional<ButtonType> result01 = alert01.showAndWait();
//                                            if (result01.get() == buttonTypeOK01){                   
//
//                                                formatPane();
//                                                /***讓 Stage 不會沒有回應 可以等待 thread 結束***/
//                                                Task<Void> format_sleeper = new Task<Void>() {
//                                                    @Override
//                                                    protected Void call() throws Exception {
//                                                        System.out.println("** FormatDisk call01 ** \n");                    
//                                                        QM.FormatDisk(QM.PhysicalName_List);
//
//                                                        return null;
//                                                    }
//                                                };
//                                                format_sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//                                                    @Override
//                                                    public void handle(WorkerStateEvent event) {
//                                                        System.out.println("** Format sleeper.setOnSucceeded ** \n");
//                                                        fotmatAlertCode(QM.Format_ResponseCode);
//                                                    }
//                                                });
//                                                new Thread(format_sleeper).start();
//
//                                            } 
//                                            else {
//                                                // ... user chose CANCEL or closed the dialog
//                                                alert01.close();
//                                            }                
//                                        }

                                    }
                                    if(QM.MountClear_ResponseCode >= 200 && QM.MountClear_ResponseCode <= 206){
                                        //掛載失敗
                                        MountFailedAlertCode(QM.MountClear_ResponseCode);
                                    }
                                    if( QM.Storage_Status.equals("6") ){
                                        LoginAlert();
                                    }
                                    if(QM.StatusCode6 == false){
                                        StorageLoginFailedAlert();
                                    }
                                    lock_button_Mount=false;
                                }
                            });
                            new Thread(sleeper).start();
                        }  
                    
                    } 
                    catch (UnknownHostException ex) {
                        lock_button_Mount=false;
                        IPLoginAlert();
//                        Logger.getLogger(acrored_ipsc.class.getName()).log(Level.SEVERE, null, ex);
                    }                                        
                }
                else{
                    rootPane.setCenter(MainGP);                        
                    ResultAlert();           
                    lock_button_Mount=false;
                }
            /*End-----------------------------------------------------------------------------------------------------------------------------------------------*/ 
            }

        });
        
        /***** 卸 載 *****/
        UNMount=new Button(WordMap.get("UNMount"));
        UNMount.setPrefSize(100,30);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        UNMount.setMaxSize(100,30);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        UNMount.setMinSize(100,30);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動    
        UNMount.setId("button");
        UNMount.setDisable(button_change);       
        UNMount.setOnAction((event) -> {  
            if( lock_button_Unmount == false ){
                lock_button_Unmount = true;
                updateUNMount();
            
                /*** 卸載中 -- thread 畫面鎖住  ***/       
                UNMountGP=new GridPane();        
                //UNMountGP.setGridLinesVisible(true); //開啟或關閉表格的分隔線       
                UNMountGP.setAlignment(Pos.BASELINE_CENTER);       
                //UNMountGP.setPadding(new Insets(0, 0, 0, 0)); //設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
                UNMountGP.setHgap(5); //元件間的水平距離
                UNMountGP.setVgap(5); //元件間的垂直距離
                UNMountGP.setPrefSize(530, 235);
                UNMountGP.setMaxSize(530, 235);
                UNMountGP.setMinSize(530, 235);
                UNMountGP.setTranslateY(45);
                UNMountGP.setStyle("-fx-background-color: #CFD8DC ; -fx-opacity: 0.6 ; -fx-background-radius: 4;-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 10, 0.0 , 0 , 5 );");
                UNMount_title=new Label(WordMap.get("Thread_UNMount")+"\n"+WordMap.get("Thread_Waiting") + "...");
                UNMount_title.setAlignment(Pos.CENTER);
                UNMount_title.setTranslateY(40);
                UNMount_title.setId("Thread_status");
                UNMountGP.add(UNMount_title,0,0); //2,1是指這個元件要佔用2格的column和1格的row 

                ProgressIndicator p2 = new ProgressIndicator();
                p2.setTranslateY(50);
                //p2.setStyle("-fx-foreground-color: #1A237E");
                UNMountGP.add(p2,0,1); //2,1是指這個元件要佔用2格的column和1格的row 

                StackPane stack_unmount = new StackPane();
                stack_unmount.getChildren().addAll(MainGP, UNMountGP);            
                rootPane.setCenter(stack_unmount);

                /***讓 Stage 不會沒有回應 可以等待 thread 結束***/
                Task<Void> UNMount_sleeper = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        System.out.println("** UNMount call01 ** \n");                    
                        UM.DoUnmoutListSession();
                        QM.PhysicalName_List.clear();
                        return null;
                    }
                };

                UNMount_sleeper.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        System.out.println("** UNMount sleeper.setOnFailed ** \n");                                       

                        if( UM.Check_UnMount_failed == true ){
                            System.out.println("** UM.Check_UnMount_failed == true ** \n");
                            CheckUNMountAlert();
                        }
                        lock_button_Unmount = false;
                    }
                });

                UNMount_sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        System.out.println("** UNMount sleeper.setOnSucceeded ** \n");
                        if( UM.UnMountResponseCode >= 200 && UM.UnMountResponseCode <= 206){
                            UNMountAlertCode(UM.UnMountResponseCode);
                        }else{
                            UNMountAlertCode(UM.UnMountResponseCode);
                        }
                        lock_button_Unmount = false;
                    }
                });
                new Thread(UNMount_sleeper).start();
            
            }

        });
        
        /***** 重新整理 *****/
        Refresh=new Button(WordMap.get("Refresh"));
        Refresh.setPrefSize(100,30);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        Refresh.setMaxSize(100,30);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        Refresh.setMinSize(100,30);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動   
        Refresh.setId("button");
        Refresh.setDisable(button_change);
        Refresh.setOnAction((event) -> {
            if(lock_button_Refresh==false){
                lock_button_Refresh=true;
                updateRefresh();
            
                /*** 重新整理中 -- thread 畫面鎖住  ***/              
                RefreshGP=new GridPane();        
                //RefreshGP.setGridLinesVisible(true); //開啟或關閉表格的分隔線       
                RefreshGP.setAlignment(Pos.BASELINE_CENTER);       
                //RefreshGP.setPadding(new Insets(0, 0, 0, 0)); //設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
                RefreshGP.setHgap(5); //元件間的水平距離
                RefreshGP.setVgap(5); //元件間的垂直距離
                RefreshGP.setPrefSize(530, 235);
                RefreshGP.setMaxSize(530, 235);
                RefreshGP.setMinSize(530, 235);
                RefreshGP.setTranslateY(45);
                RefreshGP.setStyle("-fx-background-color: #CFD8DC ; -fx-opacity: 0.6 ; -fx-background-radius: 4;-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 10, 0.0 , 0 , 5 );");
                Refresh_title=new Label(WordMap.get("Thread_Waiting") + "...");
                Refresh_title.setAlignment(Pos.CENTER);
                Refresh_title.setTranslateY(40);
                Refresh_title.setId("Thread_status");
                RefreshGP.add(Refresh_title,0,0); //2,1是指這個元件要佔用2格的column和1格的row 

                ProgressIndicator p3 = new ProgressIndicator();
                p3.setTranslateY(50);
                //p3.setStyle("-fx-foreground-color: #1A237E");
                RefreshGP.add(p3,0,1); //2,1是指這個元件要佔用2格的column和1格的row 

                StackPane stack_refresh = new StackPane();
                stack_refresh.getChildren().addAll(MainGP, RefreshGP);        
                rootPane.setCenter(stack_refresh);

                Task<Void> Refresh_sleeper = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        System.out.println("** Refresh call01 ** \n");                    
                        RF.RefreshListSession();

                        return null;
                    }
                };
                Refresh_sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        System.out.println("** Refresh sleeper.setOnSucceeded ** \n");
                        rootPane.setCenter(MainGP);
                        lock_button_Refresh=false;
                    }
                });
                new Thread(Refresh_sleeper).start();
            
            }

        });
        
        /*****離開*****/
        Leave=new Button(WordMap.get("Exit"));
        Leave.setPrefSize(80,30);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        Leave.setMaxSize(80,30);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        Leave.setMinSize(80,30);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動    
        Leave.setId("button");
        Leave.setOnAction((event) -> {
            updateLeave();
            ExitProcess();    
        });
        
        HBox Operation_01=new HBox();
        Operation_01.setPadding(new Insets(1, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        Operation_01.getChildren().addAll(Mount, UNMount, Refresh, Leave);  
        Operation_01.setAlignment(Pos.CENTER_LEFT);
        Operation_01.setSpacing(10);     
        MainGP.add(Operation_01, 2, 13);//2, 13,1,1
        /*
        HBox Operation_02=new HBox();
        Operation_02.setPadding(new Insets(1, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        Operation_02.getChildren().addAll(Refresh, Leave );  
        Operation_02.setAlignment(Pos.CENTER_LEFT);
        Operation_02.setSpacing(11);     
        MainGP.add(Operation_02, 2, 17);//2, 13,1,1
        */
        /*********** 狀態列 ***************/
        /*** 掛載狀態 : 已掛載 / 未掛載 / 已掛載(未格式化)***/
        MountStatus =  new Label(WordMap.get("Status_Mount")+"：");
        MountStatus.setId("Status_name");
        //MountStatus.setPrefWidth(80);
        //MountStatus.setMaxWidth(80);
        //MountStatus.setMinWidth(80);
        //Colon5=new Label("：");
        //Colon5.setPrefWidth(12);
        //Colon5.setMaxWidth(12);
        //Colon5.setMinWidth(12);
        //Colon5.setId("Status_colon");
        
        mounted = new Label();    
        //mounted.setId("Status_mount_status");        
        //Not_mounted = new Label(WordMap.get("Status_Mount_no")); 
        //Not_mounted.setId("Status_name");       
        Yesmount = WordMap.get("Status_Mount_yes");
        Nomount = WordMap.get("Status_Mount_no");   
        Yesmount_format = WordMap.get("Status_Mount_yes") + " ( "+ WordMap.get("Status_Mount_format") +" )" ;
        
//        if(change_mount_status == true){
//            QM.CheckDiskNeedFormat();
//            if(QM.format_status_List != null && !QM.format_status_List.isEmpty()){
//                mounted.setText(Yesmount_format);
//                mounted.setId("Status_mount_status_format");
//            }else{
//                mounted.setText(Yesmount);
//                mounted.setId("Status_mount_status");
//            }
//        }else{
//            mounted.setText(Nomount);   
//            mounted.setId("Status_mount_status");
//        }
        
        Task<Void> mount_status_sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                //System.out.println("** mount_status_sleeper call01 ** \n");                    
                CM.CheckMountListSession();
                button_change = CM.CheckMount_button_change;
                change_mount_status = CM.CheckMount_status_change ;
                Format_status = CM.Format_status;
                //CM.CheckService();
                return null;
            }
        };
        mount_status_sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                //System.out.println("** mount_status_sleeper sleeper.setOnSucceeded ** \n");
                if(change_mount_status == true){                                    
                    if( Format_status == true ){
                        mounted.setText(Yesmount_format);
                        mounted.setId("Status_mount_status_format");
                        UNMount.setDisable(button_change); 
                        Refresh.setDisable(button_change);
                    }else{
                        mounted.setText(Yesmount);
                        mounted.setId("Status_mount_status");
                        UNMount.setDisable(button_change); 
                        Refresh.setDisable(button_change);
                    }            
                }else{
                    mounted.setText(Nomount);   
                    mounted.setId("Status_mount_status");
                }                                
            //System.out.print( "---run mount---" + mounted.getText() +"\n");
            CM.format_status_List.clear();
            }
        });
        new Thread(mount_status_sleeper).start();  
        
        HBox mount_status=new HBox();
        mount_status.setPadding(new Insets(1, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        //mount_status.getChildren().addAll(MountStatus, Colon5, mounted);      
        mount_status.getChildren().addAll(MountStatus, mounted);  
        mount_status.setAlignment(Pos.CENTER_LEFT);
        //mount_status.setSpacing(5); 

        //每秒check 掛載狀態
        Timer timer=new Timer(true);
        TimerTask task=new TimerTask(){
            @Override
            public void run() { 
                //System.out.print(" -- time change mount status -- \n");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {                         
                        Task<Void> mount_status_sleeper = new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                CM.CheckMountListSession();
                                button_change = CM.CheckMount_button_change;
                                change_mount_status = CM.CheckMount_status_change ;
                                Format_status = CM.Format_status;
                                return null;
                            }
                        };
                        mount_status_sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent event) {
                                //System.out.println("** mount_status_sleeper sleeper.setOnSucceeded ** \n");
                                if(change_mount_status == true){                                    
                                    if( Format_status == true ){
                                        mounted.setText(Yesmount_format);
                                        mounted.setId("Status_mount_status_format");
                                        UNMount.setDisable(button_change); 
                                        Refresh.setDisable(button_change);
                                    }else{
                                        mounted.setText(Yesmount);
                                        mounted.setId("Status_mount_status");
                                        UNMount.setDisable(button_change); 
                                        Refresh.setDisable(button_change);
                                    }            
                                }else{
                                    mounted.setText(Nomount);   
                                    mounted.setId("Status_mount_status");
                                }                                
                            //System.out.print( "---run mount---" + mounted.getText() +"\n");
                            // System.out.print( "---CM.format_status_List-::" + CM.format_status_List +"\n"); // 2019.03.08 william IPSC新增記住上一次連線的IP     
                            CM.format_status_List.clear();
                            }
                        });
                        new Thread(mount_status_sleeper).start();               
                    }
                });                
            }
        };
            timer.schedule(task, 15000, 7000);   

        /*** Windows 使用者帳號 : xxx / xxx ***/
        String computerName =System.getenv("COMPUTERNAME");      
        String systemName =System.getProperty("user.name");    
        String computer_system = computerName +" / "+ systemName ;
        Label computer_system_Name = new Label(computer_system);
        computer_system_Name.setId("Status_change");
        
        title_computer_system =  new Label(WordMap.get("title_computer_system")+"：");
        title_computer_system.setId("Status_name");
        //title_computer_system.setPrefWidth(130);
        //title_computer_system.setMaxWidth(130);
        //title_computer_system.setMinWidth(130);
        //Colon4=new Label("：");
        //Colon4.setPrefWidth(12);
        //Colon4.setMaxWidth(12);
        //Colon4.setMinWidth(12);
        //Colon4.setId("Status_colon");

        HBox Status_name=new HBox();
        Status_name.setPadding(new Insets(1, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        //Status_name.getChildren().addAll(title_computer_system, Colon4, computer_system_Name); 
        Status_name.getChildren().addAll(title_computer_system, computer_system_Name); 
        Status_name.setAlignment(Pos.CENTER_LEFT);
        //Status_name.setSpacing(5); 
        
        String bs = "B/s";
        //bs_up = new Label(bs);
        //bs_up.setId("Status_change");
        //bs_up.setPrefWidth(30);
        //bs_up.setMaxWidth(30);
        //bs_up.setMinWidth(30);
        title_upload = new Label(WordMap.get("NetworkSpewd_up_title")+"：");
        title_upload.setId("Status_name");
        //title_upload.setPrefWidth(60);
        //title_upload.setMaxWidth(60);
        //title_upload.setMinWidth(60);
        /*
        Colon5=new Label(":");
        Colon5.setPrefWidth(5);
        Colon5.setMaxWidth(5);
        Colon5.setMinWidth(5);
        Colon5.setId("Status_name");
        */
        //bs_down = new Label(bs);
        //bs_down.setId("Status_change");
        //bs_down.setTranslateX(90);
        title_download = new Label(WordMap.get("NetworkSpewd_down_title")+"：");
        title_download.setId("Status_name");
        //title_download.setTranslateX(5);
        //title_download.setPrefWidth(70);
        //title_download.setMaxWidth(70);
        //title_download.setMinWidth(70);
        /*
        Colon6=new Label(":");
        Colon6.setPrefWidth(5);
        Colon6.setMaxWidth(5);
        Colon6.setMinWidth(5);
        Colon6.setId("Status_name");
        */
        networkspeed_download=" 0";
        download_kbs = new Label(networkspeed_download +" "+bs );
        download_kbs.setId("Status_change");
        //download_kbs.setTranslateX(5);
        //download_kbs.setPrefWidth(30);
        //download_kbs.setMaxWidth(30);
        //download_kbs.setMinWidth(30);
        
        networkspeed_upload = " 0";
        upload_kbs = new Label(networkspeed_upload +" "+ bs);
        upload_kbs.setId("Status_change");
        upload_kbs.setPrefWidth(120);
        upload_kbs.setMaxWidth(120);
        upload_kbs.setMinWidth(120);
        
        HBox Status_networkspeed_upload=new HBox();
        Status_networkspeed_upload.setPadding(new Insets(1, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        //Status_networkspeed.getChildren().addAll(title_upload, upload_kbs,  bs_up, title_download, download_kbs , bs_down);
        Status_networkspeed_upload.getChildren().addAll(title_upload, upload_kbs);
        Status_networkspeed_upload.setAlignment(Pos.CENTER_LEFT);
        //Status_networkspeed.setSpacing(5);
        
        HBox Status_networkspeed_download=new HBox();
        Status_networkspeed_download.setPadding(new Insets(1, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        //Status_networkspeed.getChildren().addAll(title_upload, upload_kbs,  bs_up, title_download, download_kbs , bs_down);
        Status_networkspeed_download.getChildren().addAll(title_download, download_kbs);
        Status_networkspeed_download.setAlignment(Pos.CENTER_LEFT);
        //Status_networkspeed.setSpacing(5);
            
        anchorpane.getChildren().addAll(mount_status, Status_name, Status_networkspeed_upload, Status_networkspeed_download); // 將功能Button 放置AnchorPane中       
        AnchorPane.setTopAnchor(mount_status,2.0);  //設置邊距
        AnchorPane.setLeftAnchor(mount_status,15.0);  
        AnchorPane.setTopAnchor(Status_name,19.0);  //設置邊距
        AnchorPane.setLeftAnchor(Status_name,15.0);  
        AnchorPane.setTopAnchor(Status_networkspeed_upload,35.0);  //設置邊距
        AnchorPane.setLeftAnchor(Status_networkspeed_upload,15.0);
        AnchorPane.setTopAnchor(Status_networkspeed_download,35.0);  //設置邊距
        AnchorPane.setLeftAnchor(Status_networkspeed_download,150.0);
        
        /*** 下載 上傳 網速 ***/
        sigar = new Sigar();        
        Timer timer_down_up=new Timer(true);
        TimerTask task_down_up=new TimerTask(){
            @Override
            public void run() {                 
                try {
                    while (true) {
                        NetworkSpeed NS= new NetworkSpeed(sigar);
                        Long[] m = NS.getMetric();
                        long totalrx00 = m[0];
                        long totaltx00 = m[1];
                        String speed_down = Sigar.formatSize(totalrx00);
                        String speed_up = Sigar.formatSize(totaltx00);               
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {    
                                String bs = "B/s";
                                String Kbs = "KB/s";
                                String Mbs = "MB/s";
                                String Gbs = "GB/s";
                                //System.out.print( "---speed_down--- " + speed_down +"\n");
                                //System.out.print( "---speed_up--- " + speed_up +"\n");
                                if(speed_down.contains("K")){
                                    String[] speed_downd_sp = speed_down.split("K");
                                    networkspeed_download = speed_downd_sp[0];
                                    //System.out.print( "---networkspeed_download---( K ) " + networkspeed_download +"\n");
                                    //bs_down.setText(Kbs);
                                    download_kbs.setText(networkspeed_download+" "+Kbs);
                                }
                                if(speed_down.contains("M")){
                                    String[] speed_downd_sp = speed_down.split("M");
                                    networkspeed_download = speed_downd_sp[0];
                                    //System.out.print( "---networkspeed_download---( M ) " + networkspeed_download +"\n");
                                    //bs_down.setText(Mbs);
                                    download_kbs.setText(networkspeed_download+" "+Mbs);
                                }
                                if(speed_down.contains("G")){
                                    String[] speed_downd_sp = speed_down.split("G");
                                    networkspeed_download = speed_downd_sp[0];
                                    //System.out.print( "---networkspeed_download---( M ) " + networkspeed_download +"\n");
                                    //bs_down.setText(Mbs);
                                    download_kbs.setText(networkspeed_download+" "+Gbs);
                                }
                                if(!speed_down.contains("K") && !speed_down.contains("M") && !speed_down.contains("G")){
                                    networkspeed_download = speed_down;
                                    //bs_down.setText(bs);
                                    download_kbs.setText(networkspeed_download+" "+bs);                                   
                                }
                                if(speed_down.contains("E")){
                                    networkspeed_download = "0";
                                    //bs_down.setText(bs);
                                    download_kbs.setText(networkspeed_download+" "+bs); 
                                    //System.out.print( "---networkspeed_download---( E) " + networkspeed_download +"\n");                                    
                                }
                                if(speed_up.contains("K")){
                                    String[] speed_up_sp = speed_up.split("K");
                                    networkspeed_upload = speed_up_sp[0];
                                    //System.out.print( "---networkspeed_upload---( K )" + networkspeed_upload +"\n");
                                    //bs_up.setText(Kbs);
                                    upload_kbs.setText(networkspeed_upload+" "+Kbs);
                                }
                                if(speed_up.contains("M")){
                                    String[] speed_up_sp = speed_up.split("M");
                                    networkspeed_upload = speed_up_sp[0];
                                    //System.out.print( "---networkspeed_upload---( M ) " + networkspeed_upload +"\n");
                                    //bs_up.setText(Mbs);      
                                    upload_kbs.setText(networkspeed_upload+" "+Mbs);
                                }         
                                if(speed_up.contains("G")){
                                    String[] speed_up_sp = speed_up.split("G");
                                    networkspeed_upload = speed_up_sp[0];
                                    //System.out.print( "---networkspeed_upload---( M ) " + networkspeed_upload +"\n");
                                    //bs_up.setText(Mbs);      
                                    upload_kbs.setText(networkspeed_upload+" "+Gbs);
                                } 
                                if(!speed_up.contains("K") && !speed_up.contains("M") && !speed_up.contains("G")){
                                    networkspeed_upload = speed_up;
                                    //bs_up.setText(bs);          
                                    upload_kbs.setText(networkspeed_upload+" "+bs);
                                }
                                if(speed_up.contains("E")){
                                    networkspeed_upload = "0";
                                    //bs_up.setText(bs);     
                                    upload_kbs.setText(networkspeed_upload+" "+bs);                                   
                                }
                            }
                        });                            
                        //Thread.sleep(1000);
                    }
                } catch (SigarException | InterruptedException ex) {
                    Logger.getLogger(acrored_ipsc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        timer_down_up.schedule(task_down_up, 1000);
        
        /***  產生主頁視窗 ***/      
        scene = new Scene(rootPane, 550, 390);//400,480 ; 440,480 ; 550,380
        scene.getStylesheets().add("ipsc.css")  ;
        MyTitle.setId("Title");
        ChangeLabelLang(L_IP_Addr, userName, pw, Language, Colon1, Colon2, Colon3); //變更Label語言字型
        ChangeButtonLang(checkBox_anonymous_binding, checkBox_AutoLogin, Mount, UNMount, Refresh, Leave); //變更Button語言字型
        IP_Addr.requestFocus();
        keydownup();
        mousemove();
        buttonhover_focus();
        
        public_stage.getIcons().add(new Image("image/ICON.png"));//ICON.png IPSC_ICON.ico
        public_stage.setTitle(WordMap.get("APP_Title")+" Advanced for Win 10/8/7  "+Ver); //視窗標題+版本     // 2019.03.08 william IPSC新增記住上一次連線的IP
        public_stage.setScene(scene);
        public_stage.initStyle(StageStyle.DECORATED);
        public_stage.setResizable(false);
        public_stage.show();
        
        /*** 第一次啟動 建議 重新啟動電腦(因service 會改機碼) ***/
//        File checkFile=new File("jsonfile/SystemStatus.json");
//        boolean exist = checkFile.exists();  //如果文件或目錄存在，返回true
//        if(exist==false){
//            FirstInstallAlert();
//        }
        
        /*****關閉視窗"X"-會與離開按鍵一樣 可以關閉全部視窗 *****/
        public_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {  
                System.out.println("Stage is closing");      
                ExitWindowProcess(we);        
                //public_stage.close();             
                System.out.println("----------end----------");
            }
        }); 
        
    }
    
    public void InitailLanguageFileMap(){
        LanguageFileMap.put("English", "jsonfile/English.json");
        LanguageFileMap.put("繁體中文", "jsonfile/TraditionalChinese.json");
        LanguageFileMap.put("简体中文", "jsonfile/SimpleChinese.json");     
        LanguageFileMap.put("TraditionalChinese", "jsonfile/TraditionalChinese.json");
        LanguageFileMap.put("SimpleChinese", "jsonfile/SimpleChinese.json");   
    }
    
    public void LangComboChanged(String newLang) throws org.json.simple.parser.ParseException{
        LangNow=newLang;
        WriteLastStatus();  //紀錄設定的程式語言
        WriteLastChange();  //紀錄明碼與暗碼        
        WordMap.clear();
        LoadLanguageMapFile(LanguageFileMap.get(LangNow));
        SwitchLanguage();
        ChangeLabelLang(L_IP_Addr, userName, pw, Language, Colon1, Colon2, Colon3); //變更Label語言字型
        ChangeButtonLang(checkBox_anonymous_binding, checkBox_AutoLogin, Mount, UNMount, Refresh, Leave); //變更Button語言字型
    }
    
    public void SwitchLanguage(){        
        Language.setText(WordMap.get("Language")+"  :");
        MyTitle.setText("IPSC Advanced"); // 2019.03.08 william IPSC新增記住上一次連線的IP WordMap.get("Title") -> "IPSC Advanced"
        L_IP_Addr.setText(WordMap.get("IP_Addr"));
        userName.setText(WordMap.get("Username"));
        pw.setText(WordMap.get("Password"));
        checkBox_AutoLogin.setText(WordMap.get("Auto_Login"));
        checkBox_anonymous_binding.setText(WordMap.get("anonymous_binding"));     
        Mount.setText(WordMap.get("Mount"));
        UNMount.setText(WordMap.get("UNMount"));
        Refresh.setText(WordMap.get("Refresh"));        
        Leave.setText(WordMap.get("Exit"));
        public_stage.setTitle(WordMap.get("APP_Title")+" Advanced for Win 10/8/7  "+Ver); //視窗標題+版本   // 2019.03.08 william IPSC新增記住上一次連線的IP
        title_computer_system.setText(WordMap.get("title_computer_system")+"：");
        title_upload.setText(WordMap.get("NetworkSpewd_up_title")+"：");
        title_download.setText(WordMap.get("NetworkSpewd_down_title")+"：");
        MountStatus.setText(WordMap.get("Status_Mount")+"："); 
        //掛載中 請稍後..
        if( lock_button_Mount == true ){
            Mount_title.setText(WordMap.get("Thread_Mount")+"\n"+WordMap.get("Thread_Waiting") + "...");
            System.out.print(" **Mount ** SwitchLanguage** \n");
        }
        //卸載中 請稍後..
        if( lock_button_Unmount == true ){
            UNMount_title.setText(WordMap.get("Thread_UNMount")+"\n"+WordMap.get("Thread_Waiting") + "...");
        }
        //重新整理 請稍後..
        if( lock_button_Refresh == true ){
            Refresh_title.setText(WordMap.get("Thread_Waiting") + "...");
        }
        //掛載中 請稍後..
        if( lock_format == true ){
            Format_title.setText(WordMap.get("Thread_Format")+"\n"+WordMap.get("Thread_Waiting") + "...");
        }
        //掛載狀態 : 已掛載 / 未掛載 / 以掛載(未格式化)
        Yesmount = WordMap.get("Status_Mount_yes");
        Nomount = WordMap.get("Status_Mount_no"); 
        Yesmount_format = WordMap.get("Status_Mount_yes") + " ( "+ WordMap.get("Status_Mount_format") +" )" ;
        if(change_mount_status == true){            
            if(Format_status == true){
                mounted.setText(Yesmount_format);
                mounted.setId("Status_mount_status_format");
            }else{
                mounted.setText(Yesmount);
                mounted.setId("Status_mount_status");
            }            
        }
        if(change_mount_status == false){
            mounted.setText(Nomount);   
            mounted.setId("Status_mount_status");
        }
    }
    
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
        SystemStatus.put("LastUsername",  userTextField.getText());
        
        //System.out.print("*** write path *** :: "+path+"\n");        
        try{
            /*** 建立 folder 並確認  檔案是否存在 ***/
            File myFile=new File(path);  //"jsonfile/SystemStatus.json"
            if(myFile.exists()){
                //System.out.println(myFile + " already exists");
                myFile.delete();
                myFile=null;
            }else if (myFile.mkdirs()) {
                //System.out.println(myFile + " was created");
            } else {
                //System.out.println(myFile + " was not created");
            }
            try (FileWriter JsonWriter = new FileWriter(path+"/SystemStatus.json")) {
                //System.out.println(JsonWriter + "** filewrite **");
                JsonWriter.write(SystemStatus.toJSONString());
                JsonWriter.flush();
            }
            /*** 隱藏 Json 資料夾 ***/          
//            String parmas_SS="attrib +s +h"+" \""+ path + "/SystemStatus.json\"";
//            System.out.println(" parmas :: "+parmas_SS+"\n");
//            Process process_hidden_SS =Runtime.getRuntime().exec(parmas_SS);            
//            String parmas="attrib +s +h"+" \""+ path + "\"";
//            System.out.println(" parmas :: "+parmas+"\n");
//            Process process_hidden =Runtime.getRuntime().exec(parmas);
            
        }catch(IOException e){
           // e.printStackTrace();
        }
    }
    
    public void LoadLastStatus() throws org.json.simple.parser.ParseException{
        try{
            //System.out.print("*** path *** :: "+path+"\n");            
            File myFile=new File(path+"/SystemStatus.json");
            //System.out.print("LangNow-1: "+LangNow+"\n");
            System.out.print("*** Load path *** :: "+path+"\n");
            
            if(myFile.exists()){

                String FilePath=myFile.getPath();
                List<String> JSONLINE=Files.readAllLines(Paths.get(FilePath));
                String JSONCode="";
                JSONCode = JSONLINE.stream().map((s) -> s).reduce(JSONCode, String::concat);
                // 參考 http://stackoverflow.com/questions/7463414/what-s-the-best-way-to-load-a-jsonobject-from-a-json-text-file
                //System.out.print(JSONCode+"\n");
                org.json.simple.parser.JSONParser Jparser=new org.json.simple.parser.JSONParser();
                JSONObject SystemStatus=(JSONObject) Jparser.parse(JSONCode);
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
                
                iscsicliIP=SystemStatus.get("LastIP").toString();           
                CurrentUserName=SystemStatus.get("LastUsername").toString();
                 
            }else{
                LangNow="繁體中文";
                iscsicliIP="";
                CurrentUserName="";
            }
        }catch(IOException e){
           // e.printStackTrace();
        }
    }
    
    /*******讀取IP與帳號的明暗碼********/
    public void LoadLastChange() throws ParseException{
        try{
            File myFile=new File( path + "/AfterChange.json" );
            if(myFile.exists()){

                String FilePath=myFile.getPath();
                List<String> JSONLINE=Files.readAllLines(Paths.get(FilePath));
                String JSONCode="";
                JSONCode = JSONLINE.stream().map((s) -> s).reduce(JSONCode, String::concat);
                // 參考 http://stackoverflow.com/questions/7463414/what-s-the-best-way-to-load-a-jsonobject-from-a-json-text-file
                //System.out.print(JSONCode+"\n");
                JSONParser Jparser=new JSONParser();
                JSONObject AfterChange=(JSONObject) Jparser.parse(JSONCode);
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
        }catch(IOException e){
           // e.printStackTrace();
        }
    }
    /*******紀錄IP與帳號的明暗碼********/
    public void WriteLastChange(){
        
        JSONObject AfterChange=new JSONObject();       
        AfterChange.put("IP_changeAfterSaved", IP_changeAfterSaved);
        AfterChange.put("Name_changeAfterSaved", Name_changeAfterSaved);      
        try{
             /*** 建立 folder 並確認  檔案是否存在 ***/
            File myFile=new File(path);  //"jsonfile/AfterChange.json"
            
            if(myFile.exists()){
                //System.out.println(myFile + " already exists");
                myFile.delete();
                myFile=null;
            }else if (myFile.mkdirs()) {
                //System.out.println(myFile + "*** was created");                
            } else {
                //System.out.println(myFile + " was not created");
            }     
            
            try (FileWriter JsonWriter = new FileWriter( path +"/AfterChange.json")) {
                JsonWriter.write(AfterChange.toJSONString());
                JsonWriter.flush();
            }
            /*** 隱藏 Json 資料夾 ***/           
//            String parmas_AC="attrib +s +h"+" \""+ path + "/AfterChange.json\"";
//            System.out.println(" parmas :: "+parmas_AC+"\n");
//            Process process_hidden_AC =Runtime.getRuntime().exec(parmas_AC);            
//            String parmas="attrib +s +h"+" \""+ path + "\"";
//            System.out.println(" parmas :: "+parmas+"\n");
//            Process process_hidden =Runtime.getRuntime().exec(parmas);
            
        }catch(IOException e){
           // e.printStackTrace();
        }
    }
    
    public void LoadCheckChange() throws ParseException{
        try{
            File myFile=new File("jsonfile/CheckboxChange.json");
            if(myFile.exists()){
                
                String FilePath=myFile.getPath();
                List<String> JSONLINE=Files.readAllLines(Paths.get(FilePath));
                String JSONCode="";
                JSONCode = JSONLINE.stream().map((s) -> s).reduce(JSONCode, String::concat);
                // 參考 http://stackoverflow.com/questions/7463414/what-s-the-best-way-to-load-a-jsonobject-from-a-json-text-file
                //System.out.print(JSONCode+"\n");
                JSONParser Jparser01=new JSONParser();
                JSONObject CheckboxChange=(JSONObject) Jparser01.parse(JSONCode);      
                A_B_change=CheckboxChange.get("checkBox_A_B_change").toString();                             
                AutoLogin_change=CheckboxChange.get("checkBox_AutoLogin_change").toString();                
                //System.out.print("A_B_change-Load : "+A_B_change+"\n");
                //System.out.print("AutoLogin_change-Load : "+AutoLogin_change+"\n");                
                
            }else{    
                A_B_change = "false";   
                AutoLogin_change = "false";
            }
        }catch(IOException e){
           // e.printStackTrace();
        }
    }
    
    public void WriteLastCheckboxChange(){
        
        JSONObject CheckboxChange=new JSONObject();        
        CheckboxChange.put("checkBox_A_B_change", checkBox_A_B_change);
        CheckboxChange.put("checkBox_AutoLogin_change", checkBox_AutoLogin_change);        
        //System.out.print("checkBox_A_B_change-Write : "+checkBox_A_B_change+"\n");
        //System.out.print("checkBox_AutoLogin_change-Write : "+checkBox_AutoLogin_change+"\n");
        
        try{
            File myFile=new File("jsonfile/CheckboxChange.json");
            if(myFile.exists()){
                myFile.delete();
                myFile=null;
            }
            try (FileWriter JsonWriter = new FileWriter("jsonfile/CheckboxChange.json")) {
                JsonWriter.write(CheckboxChange.toJSONString());
                JsonWriter.flush();
            }
            
        }catch(IOException e){
           // e.printStackTrace();
        }
    }
    
    public void LoadLanguageMapFile(String LanguageFileName) throws org.json.simple.parser.ParseException{
        try{
            File myFile=new File(LanguageFileName);
            if(myFile.exists()){

                String FilePath=myFile.getPath();
                List<String> JSONLINE=Files.readAllLines(Paths.get(FilePath));
                String JSONCode="";
                JSONCode = JSONLINE.stream().map((s) -> s).reduce(JSONCode, String::concat);
                // 參考 http://stackoverflow.com/questions/7463414/what-s-the-best-way-to-load-a-jsonobject-from-a-json-text-file
                org.json.simple.parser.JSONParser Jparser=new org.json.simple.parser.JSONParser();
                JSONObject LMF=(JSONObject) Jparser.parse(JSONCode);
                WordMap.put("APP_Title", LMF.get("APP_Title").toString());
                WordMap.put("Language", LMF.get("Language").toString());
                WordMap.put("Title", LMF.get("Title").toString());
                WordMap.put("IP_Addr", LMF.get("IP_Addr").toString());
                WordMap.put("Username", LMF.get("Username").toString());
                WordMap.put("Password", LMF.get("Password").toString());
                WordMap.put("Exit", LMF.get("Exit").toString());
                WordMap.put("Mount", LMF.get("Mount").toString());
                WordMap.put("UNMount", LMF.get("UNMount").toString());
                WordMap.put("Refresh", LMF.get("Refresh").toString());
                WordMap.put("Auto_Login", LMF.get("Auto_Login").toString());
                WordMap.put("anonymous_binding", LMF.get("anonymous_binding").toString());
                WordMap.put("title_computer_system", LMF.get("title_computer_system").toString());
                WordMap.put("ExitMassage", LMF.get("ExitMassage").toString());
                WordMap.put("Alert_Cancel", LMF.get("Alert_Cancel").toString());
                WordMap.put("Alert_Confirm", LMF.get("Alert_Confirm").toString());
                WordMap.put("NetworkSpewd_down_title", LMF.get("NetworkSpewd_down_title").toString());
                WordMap.put("NetworkSpewd_up_title", LMF.get("NetworkSpewd_up_title").toString());
                WordMap.put("Mount_Success", LMF.get("Mount_Success").toString());
                WordMap.put("Mount_Failed", LMF.get("Mount_Failed").toString());
                WordMap.put("UNMount_Success", LMF.get("UNMount_Success").toString());
                WordMap.put("UNMount_Failed", LMF.get("UNMount_Failed").toString());
                WordMap.put("Message_Mount", LMF.get("Message_Mount").toString());
                WordMap.put("Message_UNMount", LMF.get("Message_UNMount").toString());
                WordMap.put("Thread_Mount", LMF.get("Thread_Mount").toString());
                WordMap.put("Thread_UNMount", LMF.get("Thread_UNMount").toString());
                WordMap.put("Thread_Waiting", LMF.get("Thread_Waiting").toString());
                WordMap.put("Message_Format", LMF.get("Message_Format").toString());
                WordMap.put("Message_Format_01", LMF.get("Message_Format_01").toString());
                WordMap.put("Format_Success", LMF.get("Format_Success").toString());
                WordMap.put("Format_Failed", LMF.get("Format_Failed").toString());
                WordMap.put("Thread_Format", LMF.get("Thread_Format").toString());
                WordMap.put("Message_Warning", LMF.get("Message_Warning").toString());
                WordMap.put("Message_Suggest", LMF.get("Message_Suggest").toString());
                WordMap.put("Message_Error_IP_01", LMF.get("Message_Error_IP_01").toString());
                WordMap.put("Message_Error_IP_02", LMF.get("Message_Error_IP_02").toString());
                WordMap.put("Message_Error_User_PW_01", LMF.get("Message_Error_User_PW_01").toString());
                WordMap.put("Message_Error_User_PW_02", LMF.get("Message_Error_User_PW_02").toString());
                WordMap.put("Message_Error_IP_03", LMF.get("Message_Error_IP_03").toString());
                WordMap.put("Message_Error_IP_04", LMF.get("Message_Error_IP_04").toString());
                WordMap.put("Message_Error_IP_05", LMF.get("Message_Error_IP_05").toString());
                WordMap.put("Message_Error_IP_06", LMF.get("Message_Error_IP_06").toString());
                WordMap.put("Message_FirstInstall_01", LMF.get("Message_FirstInstall_01").toString());
                WordMap.put("Message_Error_Stroage_01", LMF.get("Message_Error_Stroage_01").toString());
                WordMap.put("Message_Error_Stroage_02", LMF.get("Message_Error_Stroage_02").toString());
                WordMap.put("Message_Error_AutoLogin_01", LMF.get("Message_Error_AutoLogin_01").toString());
                WordMap.put("Message_Error_AutoLogin_02", LMF.get("Message_Error_AutoLogin_02").toString());
                WordMap.put("Message_Error_AutoLogin_03", LMF.get("Message_Error_AutoLogin_03").toString());
                WordMap.put("Message_Error_AutoLogin_04", LMF.get("Message_Error_AutoLogin_04").toString());
                WordMap.put("Message_Error_AutoLogin_05", LMF.get("Message_Error_AutoLogin_05").toString());
                WordMap.put("Message_Error_AutoLogin_06", LMF.get("Message_Error_AutoLogin_06").toString());
                WordMap.put("Message_Error_AutoLogin_07", LMF.get("Message_Error_AutoLogin_07").toString());
                WordMap.put("Message_Error_AutoLogin_08", LMF.get("Message_Error_AutoLogin_08").toString());
                WordMap.put("Status_Mount", LMF.get("Status_Mount").toString());
                WordMap.put("Status_Mount_yes", LMF.get("Status_Mount_yes").toString());
                WordMap.put("Status_Mount_no", LMF.get("Status_Mount_no").toString());
                WordMap.put("Status_Mount_format", LMF.get("Status_Mount_format").toString());          
                // 2019.03.08 william IPSC新增記住上一次連線的IP                          
                WordMap.put("Mount_Connecting_tips", LMF.get("Mount_Connecting_tips").toString()); 
                WordMap.put("Mount_Connecting_tips_eng0", LMF.get("Mount_Connecting_tips_eng0").toString()); 
                WordMap.put("Mount_Connecting_tips_eng1", LMF.get("Mount_Connecting_tips_eng1").toString()); 
                WordMap.put("Mount_Connecting_tips_eng2", LMF.get("Mount_Connecting_tips_eng2").toString());                 
            }else{
                WordMap.put("APP_Title", "AcroRed IPSC");
                WordMap.put("Language", "Language");
                WordMap.put("Title", "IP SAN Connector");
                WordMap.put("IP_Addr", "IP");
                WordMap.put("Username", "Username");
                WordMap.put("Password", "Password");
                WordMap.put("Exit", "Exit");
                WordMap.put("Mount", "Mount");
                WordMap.put("UNMount", "Unmount");
                WordMap.put("Refresh", "Refresh");
                WordMap.put("Auto_Login", "Auto Mount");
                WordMap.put("anonymous_binding", "anonymous/binding");
                WordMap.put("title_computer_system", "Computer Name / User Account");
                WordMap.put("ExitMassage", "Do you want to exit IPSC？");
                WordMap.put("Alert_Cancel", "Cancel");
                WordMap.put("Alert_Confirm", "Confirm");
                WordMap.put("NetworkSpewd_down_title", "Download");
                WordMap.put("NetworkSpewd_up_title", "Upload");
                WordMap.put("Mount_Success", "Mount successful!");
                WordMap.put("Mount_Failed", "Mount Failed!");
                WordMap.put("UNMount_Success", "Unmount successful!");
                WordMap.put("UNMount_Failed", "Unmount Failed!");
                WordMap.put("Message_Mount", "Mount");
                WordMap.put("Message_UNMount", "Unmount");
                WordMap.put("Thread_Mount", "Mount");
                WordMap.put("Thread_UNMount", "Unmount");
                WordMap.put("Thread_Waiting", "Please wait");
                WordMap.put("Message_Format", "Format");
                WordMap.put("Message_Format_01", "Would you like to format the disk?");
                WordMap.put("Format_Success", "Format successful!");
                WordMap.put("Format_Failed", "Format Failed!");
                WordMap.put("Thread_Format", "Format");
                WordMap.put("Message_Warning", "Warning");
                WordMap.put("Message_Suggest", "Suggestion");
                WordMap.put("Message_Error_IP_01", "Mount failed, it may be the iSCSI storage IP format error!");
                WordMap.put("Message_Error_IP_02", "Please enter the correct Host IP.");
                WordMap.put("Message_Error_User_PW_01", "Mount failed. The Username or Password is incorrect!");
                WordMap.put("Message_Error_User_PW_02", "Please enter the correct Username and Password.");
                WordMap.put("Message_Error_IP_03", "Mount failed, iSCSI storage connection error!");
                WordMap.put("Message_Error_IP_04", "Please enter the correct iSCSI storage IP, or");
                WordMap.put("Message_Error_IP_05", "Please check the network to verify if it is connected, or");
                WordMap.put("Message_Error_IP_06", "Check that the iSCSI storage is powered on.");
                WordMap.put("Message_FirstInstall_01", "AcroRed IPSC has been installed, this application is recommended to perform after you restart the computer.");
                WordMap.put("Message_Error_Stroage_01", "Mount failed. System Error!");
                WordMap.put("Message_Error_Stroage_02", "Please ask support from System Administrator.");
                WordMap.put("Message_Error_AutoLogin_01", "Automatic login error, it may be the iSCSI storage IP format error!");
                WordMap.put("Message_Error_AutoLogin_02", "Please enter the correct Host IP.");
                WordMap.put("Message_Error_AutoLogin_03", "Automatic login error, the Username or Password is incorrect!");
                WordMap.put("Message_Error_AutoLogin_04", "Please enter the correct Username and Password.");
                WordMap.put("Message_Error_AutoLogin_05", "Automatic login error, iSCSI storage connection error!");
                WordMap.put("Message_Error_AutoLogin_06", "Please enter the correct iSCSI storage IP, or");
                WordMap.put("Message_Error_AutoLogin_07", "Please check the network to verify if it is connected, or");
                WordMap.put("Message_Error_AutoLogin_08", "Check that the iSCSI storage is powered on.");
                WordMap.put("Status_Mount", "iSCSI Disk Status");
                WordMap.put("Status_Mount_yes", "Mounted");
                WordMap.put("Status_Mount_no", "Not mounted");
                WordMap.put("Status_Mount_format", "Unformatted");        
                 // 2019.03.08 william IPSC新增記住上一次連線的IP    
                WordMap.put("Mount_Connecting_tips", "Connections are from a few seconds to a few minutes, depending on the network environment！");
                WordMap.put("Mount_Connecting_tips_eng0", "(If it is mounted for the first time, the connection time "); 
                WordMap.put("Mount_Connecting_tips_eng1", "takes about several seconds to several minutes.)"); 
                WordMap.put("Mount_Connecting_tips_eng2", "(Connection time depends on the network environment.)");                                 
            }
        }catch(IOException e){
           // e.printStackTrace();
                WordMap.put("APP_Title", "AcroRed IPSC");
                WordMap.put("Language", "Language");
                WordMap.put("Title", "IP SAN Connector");
                WordMap.put("IP_Addr", "IP");
                WordMap.put("Username", "Username");
                WordMap.put("Password", "Password");                
                WordMap.put("Exit", "Exit");
                WordMap.put("Mount", "Mount");
                WordMap.put("UNMount", "Unmount");
                WordMap.put("Refresh", "Refresh");
                WordMap.put("Auto_Login", "Auto Mount");
                WordMap.put("anonymous_binding", "Anonymous/Binding");
                WordMap.put("title_computer_system", "Computer Name / User Account");
                WordMap.put("ExitMassage", "Do you want to exit IPSC？");
                WordMap.put("Alert_Cancel", "Cancel");
                WordMap.put("Alert_Confirm", "Confirm");
                WordMap.put("NetworkSpewd_down_title", "Download");
                WordMap.put("NetworkSpewd_up_title", "Upload");
                WordMap.put("Mount_Success", "Mount successful!");
                WordMap.put("Mount_Failed", "Mount Failed!");
                WordMap.put("UNMount_Success", "Unmount successful!");
                WordMap.put("UNMount_Failed", "Unmount Failed!");
                WordMap.put("Message_Mount", "Mount");
                WordMap.put("Message_UNMount", "Unmount");
                WordMap.put("Thread_Mount", "Mount");
                WordMap.put("Thread_UNMount", "Unmount");
                WordMap.put("Thread_Waiting", "Please wait");
                WordMap.put("Message_Format", "Format");
                WordMap.put("Message_Format_01", "Would you like to format the disk?");
                WordMap.put("Format_Success", "Format successful!");
                WordMap.put("Format_Failed", "Format Failed!");
                WordMap.put("Thread_Format", "Format");
                WordMap.put("Message_Warning", "Warning");
                WordMap.put("Message_Suggest", "Suggestion");
                WordMap.put("Message_Error_IP_01", "Mount failed, it may be the iSCSI storage IP format error!");
                WordMap.put("Message_Error_IP_02", "Please enter the correct Host IP.");
                WordMap.put("Message_Error_User_PW_01", "Mount failed. The Username or Password is incorrect!");
                WordMap.put("Message_Error_User_PW_02", "Please enter the correct Username and Password.");
                WordMap.put("Message_Error_IP_03", "Mount failed, iSCSI storage connection error!");
                WordMap.put("Message_Error_IP_04", "Please enter the correct iSCSI storage IP, or");
                WordMap.put("Message_Error_IP_05", "Please check the network to verify if it is connected, or");
                WordMap.put("Message_Error_IP_06", "Check that the iSCSI storage is powered on.");
                WordMap.put("Message_FirstInstall_01", "AcroRed IPSC has been installed, this application is recommended to perform after you restart the computer.");
                WordMap.put("Message_Error_Stroage_01", "Mount failed. System Error!");
                WordMap.put("Message_Error_Stroage_02", "Please ask support from System Administrator.");
                WordMap.put("Message_Error_AutoLogin_01", "Automatic login error, it may be the iSCSI storage IP format error!");
                WordMap.put("Message_Error_AutoLogin_02", "Please enter the correct Host IP.");
                WordMap.put("Message_Error_AutoLogin_03", "Automatic login error, the Username or Password is incorrect!");
                WordMap.put("Message_Error_AutoLogin_04", "Please enter the correct Username and Password.");
                WordMap.put("Message_Error_AutoLogin_05", "Automatic login error, iSCSI storage connection error!");
                WordMap.put("Message_Error_AutoLogin_06", "Please enter the correct iSCSI storage IP, or");
                WordMap.put("Message_Error_AutoLogin_07", "Please check the network to verify if it is connected, or");
                WordMap.put("Message_Error_AutoLogin_08", "Check that the iSCSI storage is powered on.");
                WordMap.put("Status_Mount", "iSCSI Disk Status");
                WordMap.put("Status_Mount_yes", "Mounted");
                WordMap.put("Status_Mount_no", "Not mounted");
                WordMap.put("Status_Mount_format", "Unformatted");         
                // 2019.03.08 william IPSC新增記住上一次連線的IP                                  
                WordMap.put("Mount_Connecting_tips", "Connections are from a few seconds to a few minutes, depending on the network environment！");
                WordMap.put("Mount_Connecting_tips_eng0", "(If it is mounted for the first time, the connection time "); 
                WordMap.put("Mount_Connecting_tips_eng1", "takes about several seconds to several minutes.)"); 
                WordMap.put("Mount_Connecting_tips_eng2", "(Connection time depends on the network environment.)");                               
        }
    }
    
    public void ExitProcess(){
//        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
//        alert.setTitle("Exit Confirmation"); //設定對話框視窗的標題列文字
//        alert.getDialogPane().setMinSize( 320 ,Region.USE_PREF_SIZE);
//        alert.getDialogPane().setMaxSize( 320 ,Region.USE_PREF_SIZE);
//        alert.setHeaderText(""); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
//        alert.setContentText(WordMap.get("ExitMassage")); //設定對話框的訊息文字
//        ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
//        ButtonType buttonTypeCancel = new ButtonType(WordMap.get("Alert_Cancel"),ButtonBar.ButtonData.CANCEL_CLOSE);
//        alert.getButtonTypes().setAll(buttonTypeOK,buttonTypeCancel); 
//       
//        final Optional<ButtonType> opt = alert.showAndWait();
//        final ButtonType rtn = opt.get(); //可以直接用「alert.getResult()」來取代           
//        
//        if (rtn == buttonTypeOK) {   //若使用者按下「確定」
            
            WriteLastStatus();  //紀錄設定的程式語言     
            //WriteLastCheckboxChange();
            /*
            Platform.exit();
            System.out.print(" Platform.exit()\n");            
            public_stage.close();
            System.out.print("public_stage.close()\n");
            */
            System.exit(0); // 結束程式     0        
            System.out.print("System.exit(0) \n");

//        }         
//        else{           
//            System.out.print("button cancel action\n");
//        }        
     
    }   
    
    /****視窗X -按視窗X 可關閉所有正開啟得視窗以及系統關閉*****/
    public void ExitWindowProcess(WindowEvent we){
//        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
//        alert.setTitle("Exit Confirmation"); //設定對話框視窗的標題列文字
//        //Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//        //stage.getIcons().add(new Image(this.getClass().getResource("images/Icon2.png").toString()));   
//        alert.getDialogPane().setMinSize( 320 ,Region.USE_PREF_SIZE);
//        alert.getDialogPane().setMaxSize( 320 ,Region.USE_PREF_SIZE);
//        alert.setHeaderText(""); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
//        alert.setContentText(WordMap.get("ExitMassage")); //設定對話框的訊息文字        
//        ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
//        ButtonType buttonTypeCancel = new ButtonType(WordMap.get("Alert_Cancel"),ButtonBar.ButtonData.CANCEL_CLOSE);
//        alert.getButtonTypes().setAll(buttonTypeOK,buttonTypeCancel); 
//        
//        final Optional<ButtonType> opt = alert.showAndWait();
//        final ButtonType rtn = opt.get(); //可以直接用「alert.getResult()」來取代           
//        
//        if (rtn == buttonTypeOK) {   //若使用者按下「確定」
            
            WriteLastStatus();  //紀錄設定的程式語言     
            //WriteLastCheckboxChange();
            /*
            Platform.exit();
            System.out.print(" Platform.exit()\n");            
            public_stage.close();
            System.out.print("public_stage.close()\n");
            */
            System.exit(0); // 結束程式     0        
            System.out.print("System.exit(0) \n");

//        }         
//        else{
//            we.consume();
//            //System.out.print("button cancel action\n");
//        }        
    } 

    /****變更Label字型****字型:中文-標楷體,英文:Times New Roman*********RUN WinXP版 需把Win7版隱藏才可顯示出字型***********/
     public void ChangeLabelLang(Label lab01, Label lab02, Label lab03, Label lab04, Label col01, Label col02, Label col03){
        //Label "IP", "UserName", "Password", "Language"
        if("English".equals(LangNow)){
            lab01.setStyle("-fx-font-family:'Times New Roman'; -fx-font-size: 19px; -fx-font-weight: 900;");//21
            lab02.setStyle("-fx-font-family:'Times New Roman'; -fx-font-size: 19px; -fx-font-weight: 900;");
            lab03.setStyle("-fx-font-family:'Times New Roman'; -fx-font-size: 19px; -fx-font-weight: 900;");
            lab04.setStyle("-fx-font-family:'Times New Roman'; -fx-font-size: 17px; -fx-font-weight: 900;");//17
            //冒號":"
            col01.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 17px; -fx-font-weight: 900;");
            col02.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 17px; -fx-font-weight: 900;");
            col03.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 17px; -fx-font-weight: 900;");
            
        }        
        if(("繁體中文".equals(LangNow))||("TraditionalChinese".equals(LangNow))||("简体中文".equals(LangNow))||("SimpleChinese".equals(LangNow))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/       
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
    /****變更button字型****字型:中文-標楷體,英文:Times New Roman****/
      public void ChangeButtonLang(CheckBox box01, CheckBox box02, Button but01, Button but02, Button but03, Button but04){
        //Button "Login", "ManagerVD", "ChangePassword", "Leave"
        if("English".equals(LangNow)){
            box01.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
            box02.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
            but01.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
            but02.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
            but03.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
            but04.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
            
        }        
        if(("繁體中文".equals(LangNow))||("TraditionalChinese".equals(LangNow))||("简体中文".equals(LangNow))||("SimpleChinese".equals(LangNow))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/
            //win7以上僅能用英文名稱
            box01.setStyle("-fx-font-family:'Microsoft JhengHei';"); 
            box02.setStyle("-fx-font-family:'Microsoft JhengHei';"); 
            but01.setStyle("-fx-font-family:'Microsoft JhengHei';");
            but02.setStyle("-fx-font-family:'Microsoft JhengHei';");
            but03.setStyle("-fx-font-family:'Microsoft JhengHei';");
            but04.setStyle("-fx-font-family:'Microsoft JhengHei';");   
            
        }        
    }
      
    /********* 第一次安裝完成 - 訊息 -建議重新啟動電腦 ************/
    public void FirstInstallAlert(){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(WordMap.get("APP_Title"));
            alert.setHeaderText(null);
            alert.getDialogPane().setMinSize( 500 ,Region.USE_PREF_SIZE);
            alert.getDialogPane().setMaxSize( 500 ,Region.USE_PREF_SIZE);
            alert.setContentText(WordMap.get("Message_FirstInstall_01"));
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(buttonTypeOK);
            alert.showAndWait();
            //alert.show();
    }   
      
    /********* IP 輸入格式 錯誤訊息 ************/
    public void ResultAlert(){        
        if(("繁體中文".equals(LangNow))||("TraditionalChinese".equals(LangNow))||("简体中文".equals(LangNow))||("SimpleChinese".equals(LangNow))){
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("Message_Mount"));
            alert_error.setHeaderText(null);
            alert_error.getDialogPane().setMinSize(400, Region.USE_PREF_SIZE);
            alert_error.setContentText(WordMap.get("Message_Warning")+" ： "+WordMap.get("Message_Error_IP_01")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_02")
                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK); 
            alert_error.show(); // 2019.03.08 william IPSC新增記住上一次連線的IP
            //alert_error.show();
        }
        if("English".equals(LangNow)){
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("Message_Mount"));
            alert_error.setHeaderText(null);                
            alert_error.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
            alert_error.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE); 
            alert_error.setContentText(WordMap.get("Message_Warning")+" ： "+WordMap.get("Message_Error_IP_01")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_02")
                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK); 
            alert_error.show(); // 2019.03.08 william IPSC新增記住上一次連線的IP
            //alert_error.show();            
        }
           
    }    
    
    /********* iSCSI 連線 錯誤訊息 ************/
    public void IPLoginAlert(){
        
        if(("繁體中文".equals(LangNow))||("TraditionalChinese".equals(LangNow))||("简体中文".equals(LangNow))||("SimpleChinese".equals(LangNow))){
            //訊息:Mount--> iSCSI 連線錯誤
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(WordMap.get("Message_Mount"));
            alert.setHeaderText(null);                
            alert.getDialogPane().setMinSize(400,Region.USE_PREF_SIZE);
            alert.getDialogPane().setMaxSize(400,Region.USE_PREF_SIZE);
            alert.setContentText(WordMap.get("Message_Warning")+" ： "+WordMap.get("Message_Error_IP_03")+"\n"                                            
                                +"\n"+WordMap.get("Message_Suggest")+" ： "
                                +" 1. "+WordMap.get("Message_Error_IP_04")+"\n"
                                +"　　　  "+" 2. "+WordMap.get("Message_Error_IP_05")+"\n"
                                +"　　　  "+" 3. "+WordMap.get("Message_Error_IP_06")
                                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(buttonTypeOK); 
            alert.show(); // 2019.03.08 william IPSC新增記住上一次連線的IP
            //alert.show();
            
            rootPane.setCenter(MainGP);
            button_change=QM.Mount_button_change;
            UNMount.setDisable(button_change);
            Refresh.setDisable(button_change);
            //change_mount_status = QM.Mount_status_change ;            
        }

        if("English".equals(LangNow)){
            //訊息:Mount--> iSCSI 連線錯誤
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(WordMap.get("Message_Mount"));
            alert.setHeaderText(null);                
            alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
            alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE);
            alert.setContentText(WordMap.get("Message_Warning")+" ： "+WordMap.get("Message_Error_IP_03")+"\n"                                            
                                +"\n"+WordMap.get("Message_Suggest")+" ： "+"\n"
                                +"   "+" 1. "+WordMap.get("Message_Error_IP_04")+"\n"
                                +"   "+" 2. "+WordMap.get("Message_Error_IP_05")+"\n"
                                +"   "+" 3. "+WordMap.get("Message_Error_IP_06")
                                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(buttonTypeOK); 
            alert.show(); // 2019.03.08 william IPSC新增記住上一次連線的IP   
            //alert.show();
            
            rootPane.setCenter(MainGP);
            button_change=QM.Mount_button_change;
            UNMount.setDisable(button_change);
            Refresh.setDisable(button_change);
            //change_mount_status = QM.Mount_status_change;
        }
        
    } 
    
    /********* 帳號或密碼 錯誤訊息 ************/
    public void LoginAlert(){
        
        if(("繁體中文".equals(LangNow))||("TraditionalChinese".equals(LangNow))||("简体中文".equals(LangNow))||("SimpleChinese".equals(LangNow))){
            //訊息:登入-帳號或密碼錯誤
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
            alert.setTitle(WordMap.get("Message_Mount"));
            alert.setHeaderText(null);
            alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);           
            alert.setContentText(WordMap.get("Message_Warning")+" ： "+WordMap.get("Message_Error_User_PW_01")+"\n"
                                 +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_User_PW_02")
                                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(buttonTypeOK); 
            alert.showAndWait(); // 2019.03.08 william IPSC新增記住上一次連線的IP
            //alert.show();
            
            rootPane.setCenter(MainGP);
            button_change=QM.Mount_button_change;
            UNMount.setDisable(button_change);
            Refresh.setDisable(button_change);
            //change_mount_status = QM.Mount_status_change;
        }

        if("English".equals(LangNow)){
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("Message_Mount"));
            alert_error.setHeaderText(null);                
            alert_error.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
            alert_error.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE); 
            alert_error.setContentText(WordMap.get("Message_Warning")+" ： "+WordMap.get("Message_Error_User_PW_01")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_User_PW_02")
                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK); 
            alert_error.showAndWait(); // 2019.03.08 william IPSC新增記住上一次連線的IP //test lock showAndWait  show
            //alert_error.show();
            
            rootPane.setCenter(MainGP);
            button_change=QM.Mount_button_change;
            UNMount.setDisable(button_change);
            Refresh.setDisable(button_change);
            //change_mount_status = QM.Mount_status_change;
        }
        
    } 
      
    /********* Mount 成功 錯誤訊息 ************/
    public void MountSuccessAlertCode(int conn){        
        if(QM.MountResponseCode >= 200 && QM.MountResponseCode <= 206){
            //訊息:掛載成功
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(WordMap.get("Message_Mount"));
            alert.setHeaderText(null);
            alert.getDialogPane().setMinSize( 220 ,Region.USE_PREF_SIZE);
            alert.getDialogPane().setMaxSize( 220 ,Region.USE_PREF_SIZE);
            alert.setContentText(WordMap.get("Mount_Success"));
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(buttonTypeOK);
            //alert.show(); // 2019.03.08 william IPSC新增記住上一次連線的IP
            //alert.show();

            rootPane.setCenter(MainGP);
            button_change=QM.Mount_button_change;
            UNMount.setDisable(button_change);
            Refresh.setDisable(button_change);
            //change_mount_status = QM.Mount_status_change;
            //alert.show(); // 2019.03.08 william IPSC新增記住上一次連線的IP
            final Optional<ButtonType> opt = alert.showAndWait();
            final ButtonType rtn = opt.get();                     // 可以直接用「alert.getResult()」來取代           
            if (rtn == buttonTypeOK) {                            // 若使用者按下「確定」
                //System.out.println("test");
                iSCSI_format_check();
            }             
            
        }
        
    }  
    
    /********* Mount 失敗 錯誤訊息 ************/
    public void MountFailedAlertCode(int conn){        
        if(QM.MountClear_ResponseCode >= 200 && QM.MountClear_ResponseCode <= 206 ){
            //訊息:掛載失敗
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(WordMap.get("Message_Mount"));
            alert.setHeaderText(null);
            alert.getDialogPane().setMinSize( 220 ,Region.USE_PREF_SIZE);
            alert.getDialogPane().setMaxSize( 220 ,Region.USE_PREF_SIZE);
            alert.setContentText(WordMap.get("Mount_Failed"));
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(buttonTypeOK);
            alert.show();// 2019.03.08 william IPSC新增記住上一次連線的IP
            //alert.show();

            rootPane.setCenter(MainGP);
            button_change=QM.Mount_button_change;
            UNMount.setDisable(button_change);
            Refresh.setDisable(button_change);
            //change_mount_status = QM.Mount_status_change;
        }
        
    }
    
    /********* Mount - StorageLogin -Result Status 錯誤訊息 ************/
    public void StorageLoginFailedAlert(){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(WordMap.get("Message_Mount"));
            alert.setHeaderText(null);
            alert.getDialogPane().setMinSize( 400 ,Region.USE_PREF_SIZE);
            alert.getDialogPane().setMaxSize( 400 ,Region.USE_PREF_SIZE);
            alert.setContentText(WordMap.get("Message_Warning")+" ： "+WordMap.get("Message_Error_Stroage_01")+"\n"
                                 +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_Stroage_02")
                                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(buttonTypeOK);
            alert.show();// 2019.03.08 william IPSC新增記住上一次連線的IP
            //alert.show();

            rootPane.setCenter(MainGP);
            button_change=QM.Mount_button_change;
            UNMount.setDisable(button_change);
            Refresh.setDisable(button_change);  
            //change_mount_status = QM.Mount_status_change;
    }
    
    /********* fotmat 錯誤訊息 ************/
    public void fotmatAlertCode(int conn){        
        if( QM.Format_ResponseCode >= 200 && QM.Format_ResponseCode <= 206){
            //訊息:格式化成功
            Alert alert02 = new Alert(Alert.AlertType.CONFIRMATION);          
            alert02.setTitle(WordMap.get("Message_Format"));
            alert02.setHeaderText(null);
            alert02.getDialogPane().setMinSize( 220 ,Region.USE_PREF_SIZE);
            alert02.getDialogPane().setMaxSize( 220 ,Region.USE_PREF_SIZE);           
            alert02.setContentText(WordMap.get("Format_Success"));           
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            alert02.getButtonTypes().setAll(buttonTypeOK); 
            alert02.show(); // 2019.03.08 william IPSC新增記住上一次連線的IP
            //alert02.show();

            rootPane.setCenter(MainGP);

        }else{
            //訊息:格式化失敗
            Alert alert03 = new Alert(Alert.AlertType.CONFIRMATION);          
            alert03.setTitle(WordMap.get("Message_Format"));
            alert03.setHeaderText(null);
            alert03.getDialogPane().setMinSize( 220 ,Region.USE_PREF_SIZE);
            alert03.getDialogPane().setMaxSize( 220 ,Region.USE_PREF_SIZE);           
            alert03.setContentText(WordMap.get("Format_Failed"));           
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            alert03.getButtonTypes().setAll(buttonTypeOK); 
            alert03.show();   // 2019.03.08 william IPSC新增記住上一次連線的IP
            //alert03.show();

            rootPane.setCenter(MainGP);

        }
        
    }
    
    /********* UNMount 錯誤訊息 ************/
    public void UNMountAlertCode(int conn){        
        if( UM.UnMountResponseCode >= 200 && UM.UnMountResponseCode <= 206){
            //訊息:卸載成功
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
            alert.setTitle(WordMap.get("Message_UNMount"));
            alert.setHeaderText(null);
            alert.getDialogPane().setMinSize( 220 ,Region.USE_PREF_SIZE);
            alert.getDialogPane().setMaxSize( 220 ,Region.USE_PREF_SIZE);           
            alert.setContentText(WordMap.get("UNMount_Success"));           
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(buttonTypeOK); 
            alert.show(); // 2019.03.08 william IPSC新增記住上一次連線的IP
            //alert.show();

            rootPane.setCenter(MainGP);
            button_change=UM.UnMount_button_change;
            UNMount.setDisable(button_change);
            Refresh.setDisable(button_change);
            //change_mount_status = UM.UnMount_status_change;
            
        }else{
            //訊息:卸載失敗
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
            alert.setTitle(WordMap.get("Message_UNMount"));
            alert.setHeaderText(null);
            alert.getDialogPane().setMinSize( 220 ,Region.USE_PREF_SIZE);
            alert.getDialogPane().setMaxSize( 220 ,Region.USE_PREF_SIZE);           
            alert.setContentText(WordMap.get("UNMount_Failed"));           
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(buttonTypeOK); 
            alert.show();  // 2019.03.08 william IPSC新增記住上一次連線的IP
            //alert.show();

            rootPane.setCenter(MainGP);
            button_change=UM.UnMount_button_change;
            UNMount.setDisable(button_change);
            Refresh.setDisable(button_change);
            //change_mount_status = UM.UnMount_status_change;
        }
        
    }
    
    /********* Check UNMount 錯誤訊息 ************/
    public void CheckUNMountAlert(){        
        
            //訊息:卸載失敗
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
            alert.setTitle(WordMap.get("Message_UNMount"));
            alert.setHeaderText(null);
            alert.getDialogPane().setMinSize( 220 ,Region.USE_PREF_SIZE);
            alert.getDialogPane().setMaxSize( 220 ,Region.USE_PREF_SIZE);           
            alert.setContentText(WordMap.get("UNMount_Failed"));           
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(buttonTypeOK); 
            alert.show(); // 2019.03.08 william IPSC新增記住上一次連線的IP
            //alert.show();

            rootPane.setCenter(MainGP);
            button_change=UM.UnMount_button_change;
            UNMount.setDisable(button_change);
            Refresh.setDisable(button_change); 
            //change_mount_status = UM.UnMount_status_change;
    }
    
    /********* AutoLogin IP 輸入格式 錯誤訊息 ************/
    public void AutoLoginResultAlert(){   
        
        if(("繁體中文".equals(LangNow))||("TraditionalChinese".equals(LangNow))||("简体中文".equals(LangNow))||("SimpleChinese".equals(LangNow))){
                Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
                alert_error.setTitle(WordMap.get("Message_Mount"));
                alert_error.setHeaderText(null);
                alert_error.getDialogPane().setMinSize(420, Region.USE_PREF_SIZE);
                alert_error.setContentText(WordMap.get("Message_Warning")+" ： "+WordMap.get("Message_Error_AutoLogin_01")+"\n"                                            
                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_AutoLogin_02")
                    );
                ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
                alert_error.getButtonTypes().setAll(buttonTypeOK); 
                alert_error.show(); // 2019.03.08 william IPSC新增記住上一次連線的IP
                //alert_error.show();
        }
        
        if("English".equals(LangNow)){
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("Message_Mount"));
            alert_error.setHeaderText(null);                
            alert_error.getDialogPane().setMinSize(540,Region.USE_PREF_SIZE);
            alert_error.getDialogPane().setMaxSize(540,Region.USE_PREF_SIZE); 
            alert_error.setContentText(WordMap.get("Message_Warning")+" ： "+WordMap.get("Message_Error_AutoLogin_01")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_AutoLogin_02")
                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK); 
            alert_error.show();// 2019.03.08 william IPSC新增記住上一次連線的IP
            //alert_error.show();            
        }           
    }
    
    /********* AutoLogin iSCSI 連線 錯誤訊息 ************/
    public void AutoLoginIPAlert(){
        
        if(("繁體中文".equals(LangNow))||("TraditionalChinese".equals(LangNow))||("简体中文".equals(LangNow))||("SimpleChinese".equals(LangNow))){
            //訊息:Mount--> iSCSI 連線錯誤
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(WordMap.get("Message_Mount"));
            alert.setHeaderText(null);                
            alert.getDialogPane().setMinSize(410,Region.USE_PREF_SIZE);
            alert.getDialogPane().setMaxSize(410,Region.USE_PREF_SIZE);
            alert.setContentText(WordMap.get("Message_Warning")+" ： "+WordMap.get("Message_Error_AutoLogin_05")+"\n"                                            
                                +"\n"+WordMap.get("Message_Suggest")+" ： "
                                +" 1. "+WordMap.get("Message_Error_AutoLogin_06")+"\n"
                                +"　　　  "+" 2. "+WordMap.get("Message_Error_AutoLogin_07")+"\n"
                                +"　　　  "+" 3. "+WordMap.get("Message_Error_AutoLogin_08")
                                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(buttonTypeOK); 
            alert.show();// 2019.03.08 william IPSC新增記住上一次連線的IP
            //alert.show();
        }

        if("English".equals(LangNow)){
            //訊息:Mount--> iSCSI 連線錯誤
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(WordMap.get("Message_Mount"));
            alert.setHeaderText(null);                
            alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
            alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE);
            alert.setContentText(WordMap.get("Message_Warning")+" ： "+WordMap.get("Message_Error_AutoLogin_05")+"\n"                                            
                                +"\n"+WordMap.get("Message_Suggest")+" ： "+"\n"
                                +"   "+" 1. "+WordMap.get("Message_Error_AutoLogin_06")+"\n"
                                +"   "+" 2. "+WordMap.get("Message_Error_AutoLogin_07")+"\n"
                                +"   "+" 3. "+WordMap.get("Message_Error_AutoLogin_08")
                                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(buttonTypeOK); 
            alert.show(); // 2019.03.08 william IPSC新增記住上一次連線的IP           
            //alert.show();
        }
        
    } 
    
    /********* AutoLogin 帳號或密碼 錯誤訊息 ************/
    public void AutoLoginAlert(){
        
        if(("繁體中文".equals(LangNow))||("TraditionalChinese".equals(LangNow))||("简体中文".equals(LangNow))||("SimpleChinese".equals(LangNow))){
            //訊息:AutoLogin-帳號或密碼錯誤
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
            alert.setTitle(WordMap.get("Message_Mount"));
            alert.setHeaderText(null);
            alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);           
            alert.setContentText(WordMap.get("Message_Warning")+" ： "+WordMap.get("Message_Error_AutoLogin_03")+"\n"
                                 +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_AutoLogin_04")
                                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(buttonTypeOK); 
            alert.show();// 2019.03.08 william IPSC新增記住上一次連線的IP
            //alert.show();
        }

        if("English".equals(LangNow)){
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("Message_Mount"));
            alert_error.setHeaderText(null);                
            alert_error.getDialogPane().setMinSize(520,Region.USE_PREF_SIZE);
            alert_error.getDialogPane().setMaxSize(520,Region.USE_PREF_SIZE); 
            alert_error.setContentText(WordMap.get("Message_Warning")+" ： "+WordMap.get("Message_Error_AutoLogin_03")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_AutoLogin_04")
                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK); 
            alert_error.show(); // 2019.03.08 william IPSC新增記住上一次連線的IP           
            //alert_error.show();
        }
        
    } 
    
    /********* format 等待畫面 ************/
    public void formatPane(){
        /*** 掛載中 -- thread 畫面鎖住  ***/       
        FormatGP=new GridPane();        
        //FormatGP.setGridLinesVisible(true); //開啟或關閉表格的分隔線       
        FormatGP.setAlignment(Pos.BASELINE_CENTER);       
        //FormatGP.setPadding(new Insets(0, 0, 0, 0)); //設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        FormatGP.setHgap(5); //元件間的水平距離
        FormatGP.setVgap(5); //元件間的垂直距離
        FormatGP.setPrefSize(530, 235);
        FormatGP.setMaxSize(530, 235);
        FormatGP.setMinSize(530, 235);
        FormatGP.setTranslateY(45);
        FormatGP.setStyle("-fx-background-color: #CFD8DC ; -fx-opacity: 0.6 ; -fx-background-radius: 4;-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 10, 0.0 , 0 , 5 );");
        Format_title=new Label(WordMap.get("Thread_Format")+"\n"+WordMap.get("Thread_Waiting") + "...");
        Format_title.setAlignment(Pos.CENTER);
        Format_title.setTranslateY(40);
        Format_title.setId("Thread_status");
        FormatGP.add(Format_title,0,0); //2,1是指這個元件要佔用2格的column和1格的row 

        ProgressIndicator p4 = new ProgressIndicator();
        p4.setTranslateY(50);
        //p1.setStyle("-fx-foreground-color: #1A237E");
        FormatGP.add(p4,0,1); //2,1是指這個元件要佔用2格的column和1格的row 

        StackPane stack_format = new StackPane();
        stack_format.getChildren().addAll(MainGP, FormatGP);
        
        rootPane.setCenter(stack_format);
        
    }
    
    /********* Enter & Page Down & Page Up 功能 ************/
    public void keydownup(){
        //Enter Key & PAGE_DOWN Key - down ; PAGE_UP Key - up 
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
            if(event.getCode() == KeyCode.TAB){
                IP_Addr.requestFocus();
                pwIP.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                Leave.requestFocus();
                event.consume();
            }
        });
        //Enter Key & PAGE_DOWN Key - down ; PAGE_UP Key - up
        IP_Addr.setOnKeyPressed((event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){
                userTextField.requestFocus();
                pwName.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                userTextField.requestFocus();
                pwName.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.TAB){
                userTextField.requestFocus();
                pwName.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                LanguageSelection.requestFocus();
                event.consume();
            }
        });
          //Enter Key & PAGE_DOWN Key - down ; PAGE_UP Key - up
        pwIP.setOnKeyPressed((event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){
                userTextField.requestFocus();
                pwName.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                userTextField.requestFocus();
                pwName.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.TAB){
                userTextField.requestFocus();
                pwName.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                LanguageSelection.requestFocus();
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
            if(event.getCode() == KeyCode.TAB){
                pwBox.requestFocus();
                textField01.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                IP_Addr.requestFocus();
                pwIP.requestFocus();
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
            if(event.getCode() == KeyCode.TAB){
                pwBox.requestFocus();
                textField01.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                IP_Addr.requestFocus();
                pwIP.requestFocus();
                event.consume();
            }
        });
        //Enter Key & PAGE_DOWN Key - down ; PAGE_UP Key - up 
        pwBox.setOnKeyPressed((event)-> {
            //event.get
            if(event.getCode() == KeyCode.ENTER){
                Mount.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                checkBox_AutoLogin.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.TAB){
                checkBox_AutoLogin.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                userTextField.requestFocus();
                pwName.requestFocus();
                event.consume();
            }
        });
        textField01.setOnKeyPressed((event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){
                Mount.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                checkBox_AutoLogin.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.TAB){
                checkBox_AutoLogin.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                userTextField.requestFocus();
                pwName.requestFocus();
                event.consume();
            }
        });
        //Enter Key-進入功能 ; PAGE_DOWN Key - down ; PAGE_UP Key - up 
        checkBox_AutoLogin.setOnKeyPressed((event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){                  
                checkBox_AutoLogin.fire(); 
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){   
                checkBox_anonymous_binding.requestFocus();
                event.consume();            
            }
            if(event.getCode() == KeyCode.TAB){
                checkBox_anonymous_binding.requestFocus();
                event.consume();  
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                pwBox.requestFocus();
                textField01.requestFocus();
                event.consume();
            }
        });
        
        //Enter Key-進入功能 ; PAGE_DOWN Key - down ; PAGE_UP Key - up 
        checkBox_anonymous_binding.setOnKeyPressed((event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){                  
                checkBox_anonymous_binding.fire(); 
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){   
                Mount.requestFocus();
                event.consume();            
            }
            if(event.getCode() == KeyCode.TAB){
                Mount.requestFocus();
                event.consume();  
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                checkBox_AutoLogin.requestFocus();
                event.consume();
            }
        });
        
        //Enter Key-進入功能 ; PAGE_DOWN Key - down ; PAGE_UP Key - up 
        Mount.setOnKeyPressed((event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){                   
                Mount.fire();
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                if(button_change == false){
                    UNMount.requestFocus();
                    event.consume();
                }else{
                    Leave.requestFocus();
                    event.consume();
                }
            }
            if(event.getCode() == KeyCode.TAB){
                if(button_change == false){
                    UNMount.requestFocus();
                    event.consume();
                }else{
                    Leave.requestFocus();
                    event.consume();
                }
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                checkBox_anonymous_binding.requestFocus();
                event.consume();
            }
        });
//        Mount.setOnKeyReleased((event)-> {
//            //Mount.setId("button");
//        });
        //Enter Key-進入功能 ; PAGE_DOWN Key - down ; PAGE_UP Key - up 
        UNMount.setOnKeyPressed((KeyEvent event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){                              
                UNMount.fire();
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                Refresh.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.TAB){
                Refresh.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                Mount.requestFocus();
                event.consume();
            }
        });       
//        UNMount.setOnKeyReleased((event)-> {  
//            //UNMount.setId("button");
//        });
        
        //Enter Key-進入功能 ; PAGE_DOWN Key - down ; PAGE_UP Key - up 
        Refresh.setOnKeyPressed((event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){                   
                Refresh.fire(); 
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
                Leave.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.TAB){
                Leave.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_UP){
                UNMount.requestFocus();
                event.consume();
            }
        });
//        Refresh.setOnKeyReleased((event)-> {
//            //Refresh.setId("button");
//        });
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
                if(button_change == false){
                    Refresh.requestFocus();
                    event.consume();
                }else{
                    Mount.requestFocus();
                    event.consume();
                }
            }
        });
//        Leave.setOnKeyReleased((event)-> {
//            //Leave.setId("button");
//        });
    }
    
    public void mousemove(){ 
        
//        IP_Addr.setOnMouseEntered((event)-> {
//            IP_Addr.requestFocus();        
//        });
        
        Mount.setOnMouseEntered((event)-> {
            Mount.requestFocus();
        
        });
        UNMount.setOnMouseEntered((event)-> {
            UNMount.requestFocus();
        
        });
        Refresh.setOnMouseEntered((event)-> {
            Refresh.requestFocus();
        
        });
        Leave.setOnMouseEntered((event)-> {
            Leave.requestFocus();
        
        });
        
    }
    
    private void updateMount() { 
        //System.out.print("******** IN Login ********\n");
        Mount_Color = true;
        UNMount_Color = false;
        Refresh_Color = false;
        Leave_Color = false;
        if(Mount_Color == true){
            Mount.setId("Button_Mount");
            UNMount.setId("button");
            Refresh.setId("button");
            Leave.setId("button");
        }  
        //System.out.print("******** OFF Login ********\n");     
    }
    
    private void updateUNMount() {
        UNMount_Color = true;
        Mount_Color = false;
        Refresh_Color = false;
        Leave_Color = false;
        if(UNMount_Color == true){
            UNMount.setId("Button_UNMount");
            Mount.setId("button");
            Refresh.setId("button");
            Leave.setId("button");
        }
    }
    
    private void updateRefresh() {
        Refresh_Color = true;
        Mount_Color = false;
        UNMount_Color = false;
        Leave_Color = false;
        if(Refresh_Color == true){
            Refresh.setId("Button_Refresh");
            Mount.setId("button");
            UNMount.setId("button");
            Leave.setId("button");
        }
    }
    
    private void updateLeave() {
        Leave_Color = true; 
        Mount_Color = false;
        UNMount_Color = false;
        Refresh_Color = false;
        if(Leave_Color == true){
            Leave.setId("Button_Leave");
            Mount.setId("button");
            UNMount.setId("button");
            Refresh.setId("button");
        }
    }
    
    /******  控制鍵盤與滑鼠 觸碰button的反應 ******/
    public void buttonhover_focus(){       
        Mount.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if(Mount.getId()=="Button_Mount"){
                    //Login.setId("Button_actionON");
                }else{
                    Mount.setId("button");
                }
                //System.out.print("**Mount IN**\n");
                
            } if (oldValue){                
                if(Mount.getId()=="Button_Mount"){
                    //Login.setId("Button_actionON");
                }else{
                    Mount.setId("button");
                }               
                //System.out.print("**Mount OUT**\n");                
            }
            if(Mount.isFocused()){                
                //System.out.print("--Mount.isFocused()--\n");
                //System.out.print("**Mount isFocused** : "+ Mount.getId() +"\n");              
                if(Mount.getId()=="Button_Mount"){                  
                    UNMount.setId("button");
                    Refresh.setId("button");
                    Leave.setId("button");
                }
                if(UNMount.getId()=="Button_UNMount"){                    
                    Mount.setId("button");
                    Refresh.setId("button");
                    Leave.setId("button");           
                }
                if(Refresh.getId()=="Button_Refresh"){
                    //ChangePW.setId("Button_actionON");
                    Mount.setId("button");
                    UNMount.setId("button");
                    Leave.setId("button");
                }
                if(Leave.getId()=="Button_Leave"){
                    //Leave.setId("Button_actionON");
                    Mount.setId("button");
                    UNMount.setId("button");
                    Refresh.setId("button");
                }
                if(Mount.getId()=="button"){
                    if(UNMount.getId() != "Button_UNMount"){
                        if(Refresh.getId() != "Button_Refresh"){
                            if(Leave.getId() != "Button_Leave"){                                
                                UNMount.setId("button");
                                Refresh.setId("button");
                                Leave.setId("button");
                            } 
                        }    
                    }
                } 
            }
        });
        
        Mount.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if(Mount.getId()=="Button_Mount"){
                    //Login.setId("Button_actionON");
                }else{
                    Mount.setId("button");
                }
                //System.out.print("**Login IN**\n");
                
            } if (oldValue){                
                if(Mount.getId()=="Button_Mount"){
                    //Login.setId("Button_actionON");
                }else{
                    Mount.setId("button");
                }               
                //System.out.print("**Login OUT**\n");                
            }
            //System.out.print("**Login ** : "+ Login.getId() +"\n");
            if(Mount.isHover()){                               
                //System.out.print("--Mount.isHover()--\n");
                //System.out.print("**Mount isHover ** : "+ Mount.getId() +"\n");                   
                if(UNMount.getId()=="Button_UNMount"){                    
                    Mount.setId("button");
                    Refresh.setId("button");
                    Leave.setId("button");
                }
                if(Mount.getId()=="Button_Mount"){                  
                    UNMount.setId("button");
                    Refresh.setId("button");
                    Leave.setId("button");    
                }    
                if(Refresh.getId()=="Button_Refresh"){                   
                    Mount.setId("button");
                    UNMount.setId("button");
                    Leave.setId("button");
                }
                if(Leave.getId()=="Button_Leave"){
                    //Leave.setId("Button_actionON");
                    Mount.setId("button");
                    UNMount.setId("button");
                    Refresh.setId("button");
                }
                if(Mount.getId()=="button"){                   
                    if(UNMount.getId() != "Button_UNMount"){
                        if(Refresh.getId() != "Button_Refresh"){
                            if(Leave.getId() != "Button_Leave"){
                                UNMount.setId("button");
                                Refresh.setId("button");
                                Leave.setId("button");
                            } 
                        }    
                    }     
                } 
                /*** button 在第一次 Mouse-in 後 皆會直接進入 setOnMouseMoved ; 因此在mouse-in時加入條件  ***/
                /*** button 在第一次 Mouse-out 後 皆會直接進入 setOnMouseExited ; 因此在mouse-out時加入條件  ***/
                if(Mount.getId()=="Button_Mount"){
                    if(UNMount.getId() != "Button_UNMount"){
                        if(Refresh.getId() != "Button_Refresh"){
                            if(Leave.getId() != "Button_Leave"){
                                UNMount.setId("button");
                                Refresh.setId("button");
                                Leave.setId("button");                                
                                Mount.setOnMouseMoved((event)-> {
                                    //System.out.print("** Moved 003 ** \n");
                                    if(Mount_Color == true){
                                        Mount.setId("button");
                                    }
                                    //System.out.print("--Mount.setOnMouseMoved--::"+ Mount.getId() +"\n");
                                });                                
                                Mount.setOnMouseExited((event)-> {
                                    //System.out.print("** Exited 001 ** \n"); 
                                    if(Mount_Color == true){
                                        Mount.setId("Button_Mount"); 
                                    }                    
                                });                                
                            } 
                        }    
                    }       
                }                
            }
        });
        
        UNMount.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {
                if(UNMount.getId()=="Button_UNMount"){
                    //UNMount.setId("Button_UNMount");
                }else{
                    UNMount.setId("button");
                }
                //System.out.print("**UNMount IN**\n");
                
            } if (oldValue) {
                if(UNMount.getId()=="Button_UNMount"){
                    //UNMount.setId("Button_UNMount");
                }else{
                    UNMount.setId("button");
                }
                //System.out.print("**UNMount OUT**\n");
            }
            if(UNMount.isFocused()){
                //System.out.print("--ManageVD.isFocused()--\n");
                //System.out.print("**ManageVD isFocused ** : "+ ManageVD.getId() +"\n");
                
                if(Mount.getId()=="Button_Mount"){                   
                    UNMount.setId("button");
                    Refresh.setId("button");
                    Leave.setId("button");
                }
                if(UNMount.getId()=="Button_UNMount"){                
                    Mount.setId("button");
                    Refresh.setId("button");
                    Leave.setId("button");
                }
                if(Refresh.getId()=="Button_Refresh"){                    
                    Mount.setId("button");
                    UNMount.setId("button");
                    Leave.setId("button");
                }
                if(Leave.getId()=="Button_Leave"){                   
                    Mount.setId("button");
                    UNMount.setId("button");
                    Refresh.setId("button");
                }
                if(UNMount.getId()=="button"){
                    if(Mount.getId() != "Button_Mount"){
                        if(Refresh.getId() != "Button_Refresh"){
                            if(Leave.getId() != "Button_Leave"){
                                Mount.setId("button");
                                Refresh.setId("button");
                                Leave.setId("button");
                            } 
                        }    
                    }            
                }
            }
        });
        
        UNMount.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {
                if(UNMount.getId()=="Button_UNMount"){
                    //UNMount.setId("Button_UNMount");
                }else{
                    UNMount.setId("button");
                }
                //System.out.print("**UNMount IN**\n");
                //System.out.print("**UNMount IN**** : "+ UNMount.getId() +"\n");
            }if (oldValue) {
                if(UNMount.getId()=="Button_UNMount"){
                    //ManageVD.setId("Button_UNMount");
                }else{
                    UNMount.setId("button");
                }
                //System.out.print("**UNMount OUT**\n");
                //System.out.print("**UNMount OUT**** : "+ UNMount.getId() +"\n");
            }
            //System.out.print("**UNMount ** : "+ UNMount.getId() +"\n");
            if(UNMount.isHover()){
                //System.out.print("--UNMount.isHover()--\n");
                //System.out.print("**UNMount isHover ** : "+ UNMount.getId() +"\n");                
                if(Mount.getId()=="Button_Mount"){             
                    UNMount.setId("button");
                    Refresh.setId("button");
                    Leave.setId("button");
                }
                if(UNMount.getId()=="Button_UNMount"){               
                    Mount.setId("button");
                    Refresh.setId("button");
                    Leave.setId("button");
                }
                if(Refresh.getId()=="Button_Refresh"){                
                    Mount.setId("button");
                    UNMount.setId("button");
                    Leave.setId("button");
                }
                if(Leave.getId()=="Button_Leave"){                    
                    Mount.setId("button");
                    UNMount.setId("button");
                    Refresh.setId("button");
                }
                if(UNMount.getId()=="button"){
                    if(Mount.getId() != "Button_Mount"){
                        if(Refresh.getId() != "Button_Refresh"){
                            if(Leave.getId() != "Button_Leave"){
                                Mount.setId("button");
                                Refresh.setId("button");
                                Leave.setId("button");
                            } 
                        }    
                    }           
                }
                /*** button 在第一次 Mouse-in 後 皆會直接進入 setOnMouseMoved ; 因此在mouse-in時加入條件  ***/
                /*** button 在第一次 Mouse-out 後 皆會直接進入 setOnMouseExited ; 因此在mouse-out時加入條件  ***/
                if(UNMount.getId()=="Button_UNMount"){
                    if(Mount.getId() != "Button_Mount"){
                        if(Refresh.getId() != "Button_Refresh"){
                            if(Leave.getId() != "Button_Leave"){
                                Mount.setId("button");
                                Refresh.setId("button");
                                Leave.setId("button");                                
                                UNMount.setOnMouseMoved((event)-> { 
                                    if(UNMount_Color == true){
                                        UNMount.setId("button");
                                    }
                                    //System.out.print("--UNMount.setOnMouseMoved--::"+ UNMount.getId() +"\n");
                                });                                
                                UNMount.setOnMouseExited((event)-> {  
                                    if(UNMount_Color == true){
                                        UNMount.setId("Button_UNMount");
                                    }                    
                                });                                
                            } 
                        }    
                    }           
                }
                
            }
        });
        
        Refresh.focusedProperty().addListener((ov, oldValue, newValue) -> {
            //System.out.print("**Refresh**\n");
            if (newValue) {                
                if(Refresh.getId()=="Button_Refresh"){                    
                    //Refresh.setId("Button_Refresh");                    
                }else{                    
                    Refresh.setId("button");                    
                }                
                //System.out.print("**Refresh IN**\n");             
            } if (oldValue) {
                if(Refresh.getId()=="Button_Refresh"){
                    //Refresh.setId("Button_Refresh");
                }else{
                    Refresh.setId("button");                   
                }
                //System.out.print("**Refresh OUT**\n");
            }
            if(Refresh.isFocused()){
                //System.out.print("--Refresh.isFocused()--\n");
                //System.out.print("**Refresh isFocused ** : "+ Refresh.getId() +"\n");
                
                if(Mount.getId()=="Button_Mount"){
                    //Login.setId("Button_actionON");
                    UNMount.setId("button");
                    Refresh.setId("button");
                    Leave.setId("button");
                }
                if(Refresh.getId()=="Button_Refresh"){
                    Mount.setId("button");
                    UNMount.setId("button");
                    Leave.setId("button");
                }
                if(UNMount.getId()=="Button_ManageVD"){
                    //ManageVD.setId("Button_actionON");
                    Mount.setId("button");
                    Refresh.setId("button");
                    Leave.setId("button");
                }
                if(Leave.getId()=="Button_Leave"){
                    //Leave.setId("Button_actionON");
                    Mount.setId("button");
                    UNMount.setId("button");
                    Refresh.setId("button");
                }
                if(Refresh.getId()=="button"){
                    if(Mount.getId() != "Button_Mount"){
                        if(UNMount.getId() != "Button_ManageVD"){
                            if(Leave.getId() != "Button_Leave"){
                                Mount.setId("button");
                                UNMount.setId("button");
                                Leave.setId("button");
                            } 
                        }    
                    }              
                }
            }
        });
        
        Refresh.hoverProperty().addListener((ov, oldValue, newValue) -> {
            //System.out.print("**ChangePW**\n");
            if (newValue) {                
                if(Refresh.getId()=="Button_Refresh"){                    
                    //Refresh.setId("Button_Refresh");                    
                }else{                    
                    Refresh.setId("button");        //Button_hover            
                }                
                //System.out.print("**Refresh IN**\n");
                
            } if (oldValue) {
                if(Refresh.getId()=="Button_Refresh"){
                    //Refresh.setId("Button_Refresh");
                }else{
                    Refresh.setId("button");                   
                }
                //System.out.print("**Refresh OUT**\n");
            }
            //System.out.print("**Refresh ** : "+ Refresh.getId() +"\n");
            if(Refresh.isHover()){
                //System.out.print("--Refresh.isHover()--\n");
                //System.out.print("**Refresh isHover ** : "+ Refresh.getId() +"\n");                
                if(Mount.getId()=="Button_Mount"){
                    UNMount.setId("button");
                    Refresh.setId("button");
                    Leave.setId("button");
                }
                if(Refresh.getId()=="Button_Refresh"){
                    Mount.setId("button");
                    UNMount.setId("button");
                    Leave.setId("button");
                }
                if(UNMount.getId()=="Button_UNMount"){
                    Mount.setId("button");
                    Refresh.setId("button");
                    Leave.setId("button");
                }
                if(Leave.getId()=="Button_Leave"){
                    Mount.setId("button");
                    UNMount.setId("button");
                    Refresh.setId("button");
                }
                if(Refresh.getId()=="button"){
                    if(Mount.getId() != "Button_Mount"){
                        if(UNMount.getId() != "Button_UNMount"){
                            if(Leave.getId() != "Button_Leave"){
                                Mount.setId("button");
                                UNMount.setId("button");
                                Leave.setId("button");
                            } 
                        }    
                    }             
                }        
                /*** button 在第一次 Mouse-in 後 皆會直接進入 setOnMouseMoved ; 因此在mouse-in時加入條件  ***/
                /*** button 在第一次 Mouse-out 後 皆會直接進入 setOnMouseExited ; 因此在mouse-out時加入條件  ***/
                if(Refresh.getId()=="Button_Refresh"){
                    if(Mount.getId() != "Button_Mount"){
                        if(UNMount.getId() != "Button_UNMount"){
                            if(Leave.getId() != "Button_Leave"){
                                Mount.setId("button");
                                UNMount.setId("button");
                                Leave.setId("button");
                                Refresh.setOnMouseMoved((event)-> {
                                    if(Refresh_Color == true){
                                        Refresh.setId("button");
                                    }
                                    //System.out.print("--ChangePW.setOnMouseMoved--::"+ ChangePW.getId() +"\n");
                                });
                                Refresh.setOnMouseExited((event)-> {
                                    if(Refresh_Color == true){
                                        Refresh.setId("Button_Refresh");
                                    }
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
                
                if(Mount.getId()=="Button_Mount"){                    
                    UNMount.setId("button");
                    Refresh.setId("button");
                    Leave.setId("button");
                }
                if(Refresh.getId()=="Button_Refresh"){
                    Mount.setId("button");
                    UNMount.setId("button");
                    Leave.setId("button");
                }
                if(UNMount.getId()=="Button_UNMount"){
                    Mount.setId("button");
                    Refresh.setId("button");
                    Leave.setId("button");
                }
                if(Leave.getId()=="Button_Leave"){
                    Mount.setId("button");
                    UNMount.setId("button");
                    Refresh.setId("button");
                }
                if(Leave.getId()=="button"){
                    if(Mount.getId() != "Button_Mount"){
                        if(UNMount.getId() != "Button_UNMount"){
                            if(Refresh.getId() != "Button_Refresh"){
                                Mount.setId("button");
                                UNMount.setId("button");
                                Refresh.setId("button");
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
                if(Mount.getId()=="Button_Mount"){
                    UNMount.setId("button");
                    Refresh.setId("button");
                    Leave.setId("button");
                }
                if(Refresh.getId()=="Button_Refresh"){
                    Mount.setId("button");
                    UNMount.setId("button");
                    Leave.setId("button");
                }
                if(UNMount.getId()=="Button_UNMount"){
                    Mount.setId("button");
                    Refresh.setId("button");
                    Leave.setId("button");
                }
                if(Leave.getId()=="Button_Leave"){                    
                    Mount.setId("button");
                    UNMount.setId("button");
                    Refresh.setId("button");
                }
                if(Leave.getId()=="button"){
                    if(Mount.getId() != "Button_Mount"){
                        if(UNMount.getId() != "Button_UNMount"){
                            if(Refresh.getId() != "Button_Refresh"){
                                Mount.setId("button");
                                UNMount.setId("button");
                                Refresh.setId("button");
                            } 
                        }    
                    }             
                }
                /*** button 在第一次 Mouse-in 後 皆會直接進入 setOnMouseMoved ; 因此在mouse-in時加入條件  ***/
                /*** button 在第一次 Mouse-out 後 皆會直接進入 setOnMouseExited ; 因此在mouse-out時加入條件  ***/
                if(Leave.getId()=="Button_Leave"){
                    if(Mount.getId() != "Button_Mount"){
                        if(UNMount.getId() != "Button_UNMount"){
                            if(Refresh.getId() != "Button_Refresh"){
                                Mount.setId("button");
                                UNMount.setId("button");
                                Refresh.setId("button");
                                Leave.setOnMouseMoved((event)-> {
                                    if(Leave_Color == true){
                                        Leave.setId("button");
                                    }                               
                                    //System.out.print("--Leave.setOnMouseMoved--::"+ Leave.getId() +"\n");
                                });
                                Leave.setOnMouseExited((event)-> {
                                    if(Leave_Color == true){
                                        Leave.setId("Button_Leave");
                                    }                                       
                                    //System.out.print("--Leave.setOnMouseExited--::"+ Leave.getId() +"\n");
                                }); 
                            } 
                        }    
                    }             
                }
            }
        });        
    }
    
    // 2019.03.08 william IPSC新增記住上一次連線的IP    
    public void iSCSI_format_check() {
        /********若PhysicalName_List != null,詢問是否需要format **********/
        if(QM.PhysicalName_List != null && !QM.PhysicalName_List.isEmpty()){  

            System.out.println("-------PhysicalName_List != null------\n");

            Alert alert01 = new Alert(Alert.AlertType.CONFIRMATION);
            alert01.setTitle(WordMap.get("Message_Format"));//設定對話框視窗的標題列文字  , "您真的要結束程式嗎？", ButtonType.YES, ButtonType.NO
            alert01.setHeaderText(null);
            alert01.getDialogPane().setMinSize( 320 ,Region.USE_PREF_SIZE);
            alert01.getDialogPane().setMaxSize( 320 ,Region.USE_PREF_SIZE);
            alert01.setContentText(WordMap.get("Message_Format_01"));//VD 已上線,是否還要連線,若連線,則前一個連線會自動斷線.
            ButtonType buttonTypeOK01 = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType(WordMap.get("Alert_Cancel"),ButtonBar.ButtonData.CANCEL_CLOSE);
            alert01.getButtonTypes().setAll(buttonTypeOK01, buttonTypeCancel); 

            Optional<ButtonType> result01 = alert01.showAndWait(); 
            if (result01.get() == buttonTypeOK01){                   
                lock_format=true;
                formatPane();
                /***讓 Stage 不會沒有回應 可以等待 thread 結束***/
                Task<Void> format_sleeper = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        System.out.println("** FormatDisk call01 ** \n");                    
                        QM.FormatDisk(QM.PhysicalName_List);

                        return null;
                    }
                };
                format_sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        System.out.println("** Format sleeper.setOnSucceeded ** \n");
                        fotmatAlertCode(QM.Format_ResponseCode);
                        lock_format=false;
                    }
                });
                new Thread(format_sleeper).start();

            } 
            else {
                // ... user chose CANCEL or closed the dialog
                alert01.close();
            }                
        }    
    }
    

    
}
