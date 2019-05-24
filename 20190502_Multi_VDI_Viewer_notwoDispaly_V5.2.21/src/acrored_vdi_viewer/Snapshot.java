/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import static javafx.beans.property.BooleanProperty.booleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.simple.JSONObject;

/**
 *
 * @author william
 */
public class Snapshot {
    public GB GB;
    /*------------------------ import檔案及匯入文字檔 ------------------------*/
    public Map<String, String> WordMap = new HashMap<>();
    public JsonArray jsondata;  
    public String Address;
    public String Port;  
    public String CurrentUserName;
    public String CurrentPasseard; 
    public String uniqueKey;
    /*------------------------ 設定Layout ------------------------*/
    private Stage public_stage;
    private Stage Snapshot_secondStage = new Stage();
    private final Scene secondScene;
    private BorderPane rootPane; // 2017.12.04 william BorderPane框架(底層)  
    private GridPane MainGP;     // 2017.12.04 william BorderPane中間層 (快照層 - ListView物件)
    private GridPane LeftGP;     // 2017.12.04 william BorderPane左層   (VD層 - ListView物件)
    private GridPane BottomGP;   // 2017.12.04 william BorderPane下層   (說明層)
    private final Bounds mainBounds;       
    
    private Label Snapshot_VDTitle;
    private Label Snapshot_ListTitle;
    private HBox WC1;
    private HBox WC2;
    private HBox vd_selection; 
    private HBox ss_selection; 
    private HBox btn; 
    private VBox Desc;
    private Button Btn_exit;
    private Button Btn_view;
    private Button Btn_stopview;
    private Button Btn_login;    
//    private final Button Btn_stop;
//    private final Button Btn_login;
    private String ss_label_vm; 
    private Label ss_desc1; 
    private Label ss_desc2; 
    /*------------------------ 設定viewlist------------------------*/
    // public static final ObservableList vd_data  = FXCollections.observableArrayList();  
    // public static final ObservableList ss_data  = FXCollections.observableArrayList();  
    ListView<String> vdList                     = new ListView<>();
    ListView<String> ssList                     = new ListView<>();
    ObservableSet<String> selectedvs;
    ObservableSet<String> selectedss;
    /*------------------------ 設定TableView------------------------*/
    private TableView<VD> vdtable               = new TableView();
    private TableView<SS> sstable               = new TableView();
    // 預設值寫進表格內
    public ObservableList<VD> vd_data           = FXCollections.observableArrayList();
    public ObservableList<SS> ss_data           = FXCollections.observableArrayList(); 
    // 表格內Column設定
    TableColumn vdSNCol;
    TableColumn<VD, String> vdNameCol;
    TableColumn<VD, String> vdIsSnapshotViewCol;
    TableColumn ssSNCol;
    TableColumn<SS, String> StateCol;
    TableColumn<SS, String> CreateTimeCol;
    TableColumn<SS, String> SizeCol;
    TableColumn<SS, String> DescCol;
    TableColumn<SS, String> ActionCol;
    /*------------------------ 設定長寬 ------------------------*/
    int secondScene_width                       = 1080; // 885 * 1.1 // 973
    int secondScene_height                      = 603;  // 548 * 1.1    
    int LeftGP_width                            = 324;
    int LeftGP_height                           = 422;    
    int MainGP_width                            = 756;
    int MainGP_height                           = 422;    
    int BottomGP_width                          = 973;
    int BottomGP_height                         = 181;    
    int vd_listView_width                       = 250;
    int vd_listView_height                      = 360; // 2019.01.02 view D Drive   
    int ss_listView_width                       = 712; //712
    int ss_listView_height                      = 360; // 2019.01.02 view D Drive   
    int btn_width                               = 120;
    int btn_height                              = 35;        
    /*------------------------ 設定值 ------------------------*/
    public String Select_vdName;
    public String Select_vdID;
    public String Select_ssName;  
    URL url;
    URL url2;
    HttpURLConnection conn;
    HttpURLConnection conn2;
    JSONObject obj;
    public String query;
    public JsonObject vd_term;
    public String vd_Name;
    public JsonObject ss_term;
    public String ss_Name;    
    public String VdId;
    public boolean Vd_IsSnapshotView;
    public boolean Vd_IsOrg;
    public int Chcek_ss_ResponseCode;
    
    JsonArray sslist_jsonArr;
    public boolean sslist_json_length_empty;
    public String sslist_State;
    public String sslist_CreateTime; 
    public String sslist_Size;
    public String sslist_Desc;
    public String sslist_Layer;
    public String sslist_UpperLayer;
    public boolean sslist_isView;

    public boolean IsKeepNIC;       
    public static String ViewLayer;
    public static String RollBackLayer;  // 2018.07.06 快照新增還原按鈕
    public static String Vd_Id;
    
    public String ss_state_string;
    
    public String[][] sslist_array;
    public String[][] vdlist_array;
    public boolean[][] vdlist_array2;
    
    public AnimationTimer login_timer;
    public boolean login_timerflag;
    int ccc = 0;
    
    public boolean NoSSfunc_flag;  //  2018.01.12 william 例外處理
    public int RespCode;   //  2018.01.12 william 例外處理    
    public int BtnDisable_flag;  //  2018.01.18 william 例外處理
    /*------------------------ Polling ------------------------*/ 
    private Timer fresh_sslist_timer;
    TimerTask sstask;
//    private Timer fresh_vdlist_timer;
//    TimerTask vdtask;
    /*------------------------ Get vd index ------------------------*/    
    public int vd_index;
    /*------------------------ 狀態檢視 ------------------------*/
    public boolean NowIsView;
    public boolean NowIsLoginView;
    /*------------------------ 畫面鎖定 ------------------------*/
    private StackPane stack                  = new StackPane(); // 2017.11.29 william 多VD登入中畫面鎖住
    private Label Login_title;                                  // 2017.11.29 william 多VD登入中畫面鎖住
    private GridPane LoginGP;                                   // 2017.11.29 william 多VD登入中畫面鎖住
    private ProgressIndicator p1;                               // 2017.11.29 william 多VD登入中畫面鎖住      
    /*------------------------ Spice設定值 ------------------------*/
    public String SpiceAddress;
    public String SpicePort;
    public String[] params;
    public Process process;
    /*------------------------ 主程式 ------------------------*/    
    private QueryMachine QM;
    
    // 2019.01.02 view D Drive
    VD[] _checkVDarray = null;
    SS[] _checkSSarray = null;
    public static String bId;
    public static String _diskName = null; // 2019.01.30 UserDisk快照顯示異常問題修正
    public static String _vdName;
    private Timer fresh_timer;
    UserDisk[] _checkUserDiskarray = null;  
    VD[] _checkUDVDarray = null;
    JsonArray getlist_jsonArr;
    private TableView<UserDisk> userdisktable = new TableView();
    public ObservableList<UserDisk> userdisk_data = FXCollections.observableArrayList(); 
    TableColumn<UserDisk, String> UDStateCol;
    TableColumn<UserDisk, String> UDActionCol;  
    public static String _diskNamePolling;
    int ud_listView_width                       = 712; 
    int ud_listView_height                      = 360;     
    public boolean NoUserDiskfunc_flag;
    private TableView<VD> udvdtable = new TableView();
    public ObservableList<VD> udvd_data = FXCollections.observableArrayList();
    public int udvd_index;
    private BorderPane UDrootPane; 
    private GridPane UDMainGP;     
    private GridPane UDLeftGP;     
    private GridPane UDBottomGP;   
    private Button UDBtn_exit;
    private Button UDBtn_stopview;
    private Button UDBtn_login;     
    public boolean UDlogin_timerflag;
    int UDRespCode = -1;
    public AnimationTimer UDlogin_timer;
    public boolean IsUDBtnDisable = false;
    
    // 2019.01.10 User Disk bug fix
    public boolean IsUDViewDisable;
    
    // 2019.01.30 UserDisk快照顯示異常問題修正
    public static String UserDisk_Vd_Id;         
    public static String _compareVdId; // 2019.05.16
    Label ss_desc1_1;    
        
    public Snapshot(Stage primaryStage, Map<String, String> LangMap,JsonArray jsonArr, String IPAddr, String IPPort, String Uname, String Password, String UniqueKey, String _diskname){ // 2019.01.02 view D Drive
        public_stage    = primaryStage;        
        WordMap         = LangMap;
        jsondata        = jsonArr;
        // 取值，(IP, 使用者帳號, 使用者密碼, Port)
        Address         = IPAddr;
        Port            = IPPort; 
        CurrentUserName = Uname;
        CurrentPasseard = Password;   
        uniqueKey       = UniqueKey;
        sslist_array    = new String[1000][10]; // https://stackoverflow.com/questions/8089798/strange-java-string-array-null-pointer-exception
        vdlist_array    = new String[1000][10];
        vdlist_array2   = new boolean[1000][2];
        login_timerflag = false;
        fresh_sslist_timer       = null;
        sslist_json_length_empty = false;
        QM              = new QueryMachine(WordMap);
        
        if(WordMap.get("SelectedLanguage").equals("English")){ 
            secondScene_height = 630;      
        }  else if(WordMap.get("SelectedLanguage").equals("Japanese")) {
            secondScene_width                       = 1080;
            secondScene_height                      = 650;                 
        }         
        
        // 2019.01.02 view D Drive   
        UDlogin_timerflag = false;
        fresh_timer       = null;
        TabPane tp = new TabPane();            
        Tab _snapShotTab = new Tab(WordMap.get("Snapshot"));
        Tab _viewDiskTab = new Tab(WordMap.get("ViewUserDisk"));
        
        if("English".equals(WordMap.get("SelectedLanguage"))){                 
            _snapShotTab.setStyle("-fx-font-family:'Ubuntu';");
            _viewDiskTab.setStyle("-fx-font-family:'Ubuntu';");
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){           
            _snapShotTab.setStyle("-fx-font-family:'Droid Sans Fallback';");
            _viewDiskTab.setStyle("-fx-font-family:'Droid Sans Fallback';");
        }          
        
        tp.getTabs().addAll(_snapShotTab, _viewDiskTab);
        
        _snapShotTab.closableProperty().set(false);
        _viewDiskTab.closableProperty().set(false);
        _snapShotTab.setContent(SnapshotPane()); 
        _viewDiskTab.setContent(ViewDiskPane(jsonArr, _diskname)); 

        BorderPane backPane = new BorderPane();
        backPane.setStyle("-fx-background-color: rgb(0,180,240);");
        backPane.setCenter(tp);     
        
        vdtable.setRowFactory(tv -> new TableRow<VD>() {            
            {                               
                setOnMouseClicked(event -> {
        
                        VD rowData = this.getItem();        
                        if(rowData != null) {
                            vd_index = vdtable.getSelectionModel().getSelectedIndex();
                            fresh_sslist_timer.cancel();
                            setBeforevid(getVDID());
                            setVDID(rowData.getVD_ID());   
                            System.out.println("click on " + rowData.getVD_ID() + "\n");
                            ssdata_refresh();
                        }                                        
                }); 
            }
        });         
        
        udvdtable.setRowFactory(tv -> new TableRow<VD>() {            
            {                               

                setOnMouseClicked(event -> {
        
                        VD rowData = this.getItem();        
                        if(rowData != null) {
                            fresh_timer.cancel();
                            // 2019.01.30 UserDisk快照顯示異常問題修正
                            setUserDiskVDID(rowData.getVD_ID());
                            System.out.println("click on " + rowData.getVD_ID() + "\n");
                            data_refresh();
                        }
                                        
                }); 
            }
        });             
        

        
        /* 2017.11.27 william 以下為跳出視窗之功能 */                
        
        secondScene = new Scene(backPane, secondScene_width, secondScene_height);
        secondScene.getStylesheets().add("snapshot.css");
        
        mainBounds = primaryStage.getScene().getRoot().getLayoutBounds();    

        Snapshot_secondStage.setX(primaryStage.getX() + (mainBounds.getWidth() - secondScene_width ) / 2);
        Snapshot_secondStage.setY(primaryStage.getY() + (mainBounds.getHeight() - secondScene_height ) / 2); 
        Snapshot_secondStage.setScene(secondScene);
        Snapshot_secondStage.setTitle(WordMap.get("APP_Title"));                    // 視窗標題 Title_Snapshot_onlineviewing
        switch (GB.JavaVersion) {
            case 0:
                Snapshot_secondStage.getIcons().add(new Image("images/Icon2.png"));
                break;
            case 1:
                Snapshot_secondStage.getIcons().add(new Image("images/MCIcon2.png")); 
                break;                
            default:
                break;
        }          
               
        Snapshot_secondStage.initStyle(StageStyle.DECORATED);                       // DECORATED -> UNDECORATED 
        Snapshot_secondStage.initModality(Modality.WINDOW_MODAL);                   // window modal lock NONE -> WINDOW_MODAL
        Snapshot_secondStage.initOwner(primaryStage);
        Snapshot_secondStage.setResizable(false);                                   // 將視窗放大,關閉             
        Snapshot_secondStage.show();          
        
         //  2018.01.12 william 例外處理
        if(NoSSfunc_flag) {                                
           StopAll(); // 2019.01.02 view D Drive   
        }       
        
        // 2019.01.02 view D Drive   
        if(NoUserDiskfunc_flag) {     
            StopAll();
        }           
        
        // 新增Stage關閉事件
        Snapshot_secondStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
              System.out.println("Stage is closing");
              StopAll(); // 2019.01.02 view D Drive   
          }
        });         
    
    }
    
    // 2019.01.02 view D Drive
    
    // Tab1 : Snapshot    
    private Pane SnapshotPane() {
        NoSSfunc_flag = false;  //  2018.01.12 william 例外處理
        /***  主畫面使用GRID的方式Layout ***/
        MainGP = new GridPane();
        MainGP.setGridLinesVisible(false);                // 開啟或關閉表格的分隔線       
        MainGP.setAlignment(Pos.CENTER);       
        // MainGP.setPadding(new Insets(35, 50, 50, 20)); // 設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        MainGP.setHgap(15);                               // 元件間的水平距離
   	MainGP.setVgap(5);                                // 元件間的垂直距離        
        MainGP.setPrefSize(MainGP_width, MainGP_height);
        MainGP.setMaxSize(MainGP_width , MainGP_height);
        MainGP.setMinSize(MainGP_width , MainGP_height);       
        MainGP.setTranslateY(25); // 55
        MainGP.setTranslateX(-30);
        /***  左邊VD畫面使用GRID的方式Layout ***/
        LeftGP = new GridPane();
        LeftGP.setGridLinesVisible(false);                
        LeftGP.setAlignment(Pos.CENTER);       
        // LeftGP.setPadding(new Insets(35, 50, 50, 20)); 
        LeftGP.setHgap(15);                               
   	LeftGP.setVgap(5);                                
        LeftGP.setPrefSize(LeftGP_width, LeftGP_height);
        LeftGP.setMaxSize(LeftGP_width , LeftGP_height);
        LeftGP.setMinSize(LeftGP_width , LeftGP_height);    
        LeftGP.setTranslateY(25);
        /***  底部說明畫面使用GRID的方式Layout ***/        
        BottomGP = new GridPane();
        BottomGP.setGridLinesVisible(false);               
        BottomGP.setAlignment(Pos.CENTER);       
        // BottomGP.setPadding(new Insets(35, 50, 50, 20)); 
        BottomGP.setHgap(15);                             
   	BottomGP.setVgap(15);                             
        BottomGP.setPrefSize(BottomGP_width, BottomGP_height);
        BottomGP.setMaxSize(BottomGP_width , BottomGP_height);
        BottomGP.setMinSize(BottomGP_width , BottomGP_height);                         
        /***  底層畫面使用Border的方式Layout ***/
        rootPane = new BorderPane();                      // 多VD登入中畫面鎖住    
        rootPane.setCenter(MainGP);        
        rootPane.setLeft(LeftGP);
        rootPane.setBottom(BottomGP);
        rootPane.setTranslateY(-30); // 55
//        rootPane.setVisible(true);              

        /***  左層VD的Layout ***/        
        vd_layout();        
        /***  中間層快照的Layout ***/                
        ss_layout();
        /***  下層說明的Layout ***/
        desc_layout();
        /***  動作功能 ***/        
        detectSelection();    
        return rootPane;
    }
    // Tab2 : View Disk    
    private Pane ViewDiskPane(JsonArray jsondata, String _diskname) {
        NoUserDiskfunc_flag = false;  //  2018.01.12 william 例外處理
        /***  主畫面使用GRID的方式Layout ***/
        UDMainGP = new GridPane();
        UDMainGP.setGridLinesVisible(false);                // 開啟或關閉表格的分隔線       
        UDMainGP.setAlignment(Pos.CENTER);       
        // MainGP.setPadding(new Insets(35, 50, 50, 20)); // 設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        UDMainGP.setHgap(15);                               // 元件間的水平距離
   	UDMainGP.setVgap(5);                                // 元件間的垂直距離        
        UDMainGP.setPrefSize(MainGP_width, MainGP_height);
        UDMainGP.setMaxSize(MainGP_width , MainGP_height);
        UDMainGP.setMinSize(MainGP_width , MainGP_height);       
        UDMainGP.setTranslateY(25); // 55
        UDMainGP.setTranslateX(-30);
        /***  左邊VD畫面使用GRID的方式Layout ***/
        UDLeftGP = new GridPane();
        UDLeftGP.setGridLinesVisible(false);                
        UDLeftGP.setAlignment(Pos.CENTER);       
        // LeftGP.setPadding(new Insets(35, 50, 50, 20)); 
        UDLeftGP.setHgap(15);                               
   	UDLeftGP.setVgap(5);                                
        UDLeftGP.setPrefSize(LeftGP_width, LeftGP_height);
        UDLeftGP.setMaxSize(LeftGP_width , LeftGP_height);
        UDLeftGP.setMinSize(LeftGP_width , LeftGP_height);    
        UDLeftGP.setTranslateY(25);
        /***  底部說明畫面使用GRID的方式Layout ***/        
        UDBottomGP = new GridPane();
        UDBottomGP.setGridLinesVisible(false);               
        UDBottomGP.setAlignment(Pos.CENTER);       
        // BottomGP.setPadding(new Insets(35, 50, 50, 20)); 
        UDBottomGP.setHgap(15);                             
   	UDBottomGP.setVgap(15);                             
        UDBottomGP.setPrefSize(BottomGP_width, BottomGP_height);
        UDBottomGP.setMaxSize(BottomGP_width , BottomGP_height);
        UDBottomGP.setMinSize(BottomGP_width , BottomGP_height);                         
        /***  底層畫面使用Border的方式Layout ***/
        UDrootPane = new BorderPane();                      // 多VD登入中畫面鎖住    
        UDrootPane.setCenter(UDMainGP);        
        UDrootPane.setLeft(UDLeftGP);
        UDrootPane.setBottom(UDBottomGP);
        UDrootPane.setTranslateY(-30); // 55
//        rootPane.setVisible(true);  

        /***  下層說明的Layout ***/
        BottomView();

        /***  左層VD的Layout ***/        
        LeftView(jsondata, _diskname);        
        /***  中間層快照的Layout ***/                
        CenterView();

        /***  動作功能 ***/        
        UserDiskdetectSelection();    
        return UDrootPane;
    }    
    
    
    
    /*------------------------ 畫面layout ------------------------*/ 
    public final void vd_layout() {
        if(WordMap.get("SelectedLanguage").equals("English")) {
            ss_label_vm = "Select a VM/VD";
        } else if(WordMap.get("SelectedLanguage").equals("TraditionalChinese")) {
            ss_label_vm = "請選擇 雲主機/雲桌面";
        } else if(WordMap.get("SelectedLanguage").equals("SimpleChinese")) {
            ss_label_vm = "请选择 云主机/云桌面";
        }  else if(WordMap.get("SelectedLanguage").equals("Japanese")) {
            ss_label_vm = "VM/VD 選択してください";
        } 
        /***  Title 放入一個HBOX內 ***/
        Snapshot_VDTitle = new Label(ss_label_vm);
        Snapshot_VDTitle.getStyleClass().add("Title_label");
        
        WC1 = new HBox(Snapshot_VDTitle);
        WC1.setAlignment(Pos.TOP_CENTER);
        WC1.setPadding(new Insets(0, 0, 0, 0));
        WC1.setTranslateY(0);
        WC1.setTranslateX(0);
        
        LeftGP.add(WC1,0,0);        
        
        /*------------------------ 畫面顯示(將VD資料填入Table內) ------------------------*/
        vd_data.clear(); // 清空data先前的內容        
                
        /*------------------------ 讓Table內顯示項次 ------------------------*/
        vdSNCol = new TableColumn(WordMap.get("VD_SN"));       
        vdSNCol.setCellValueFactory(new Callback<CellDataFeatures<VD, VD>, ObservableValue<VD>>() {
             @Override
             public ObservableValue<VD> call(CellDataFeatures<VD, VD> v) {
                return new ReadOnlyObjectWrapper(v.getValue());
            }
        });        
        
        vdSNCol.setCellFactory(new Callback<TableColumn<VD, VD>, TableCell<VD, VD>>() {
            @Override 
            public TableCell<VD, VD> call(TableColumn<VD, VD> param) {
                return new TableCell<VD, VD>() {
                    @Override 
                    protected void updateItem(VD item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && item != null) {
                            int number = this.getTableRow().getIndex();
                            setText(number + 1 + " ");
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });        
        
        if(WordMap.get("SelectedLanguage").equals("Japanese")) {
            vdSNCol.setPrefWidth(90);
            vdSNCol.setMinWidth(90);
        } else {
            vdSNCol.setPrefWidth(60);
            vdSNCol.setMinWidth(60);
            //vdSNCol.setMaxWidth(60);                
        }  
        vdSNCol.setSortable(false);
        vdSNCol.setStyle("-fx-font-family:'Times New Roman';");        
        /*------------------------ 讓Table內顯示VD ------------------------*/
        vdNameCol = new TableColumn(WordMap.get("VD_Name"));     
        if(WordMap.get("SelectedLanguage").equals("Japanese")) {
            vdNameCol.setPrefWidth(138); // 468
            vdNameCol.setMinWidth(138); 
        } else {
            vdNameCol.setPrefWidth(168); // 468
            vdNameCol.setMinWidth(168); 
        }    
        vdNameCol.setStyle( "-fx-alignment: center-left;"); 
        
        vdNameCol.setSortable(false); // 取消Sort
        
        vdNameCol.setCellFactory(getCustomVDCellFactory());

        // 設定每一個欄位對應的JavaBean物件與Property名稱        
        vdNameCol.setCellValueFactory(new PropertyValueFactory<VD, String>("VD_Name"));    
        
        // 2019.01.02 view D Drive
        VD[] _array = new VD[jsondata.size()];
        int arraySize = 0;
        
        //  2018.01.12 william 例外處理
        try {         
            // 顯示可選擇之VD
            for (int i = 0; i < jsondata.size(); i++) {
                vd_term           = jsondata.get(i).getAsJsonObject();            
                vd_Name           = vd_term.get("VdName").getAsString();
                VdId              = vd_term.get("VdId").getAsString();
                // 判斷是否有檢視中的layer和是否為雲主機
                Vd_IsSnapshotView = vd_term.get("IsSnapshotView").getAsBoolean();
                Vd_IsOrg          = vd_term.get("IsVdOrg").getAsBoolean();
      
                System.out.println("-------------------------------------------------------------");  
                System.out.println("IsSnapshotView: " + Vd_IsSnapshotView + ", IsOrg:" + Vd_IsOrg);                
            
                if (i==0) {
                    setVDID(VdId);
                    _compareVdId = VdId; // 2019.05.16
                }     
                
                // 2019.01.02 view D Drive
                /*                
                vdlist_array[i][0] = vd_Name;
                vdlist_array[i][1] = VdId;
                vdlist_array2[i][0] = Vd_IsSnapshotView;
                vdlist_array2[i][1] = Vd_IsOrg;            
                */
                if(Vd_IsOrg) {
                    _array[i] = new VD(vd_Name, VdId, Vd_IsSnapshotView);
                    arraySize++;
                }
                    
            }    
            // 2019.01.02 view D Drive
            VD[] _arrayshow;
            if(arraySize == 0)
                _arrayshow = new VD[1];
            else
                _arrayshow = new VD[arraySize];

            for(int j = 0; j < jsondata.size(); j++) {
                if(_array[j] != null) {_arrayshow[j] = _array[j];}
            }                        
            
            // 2019.01.02 view D Drive
            /*
            for (int i = 0; i < jsondata.size(); i++) {
                if(vdlist_array2[i][1]) {
                    vd_data.add(new VD(vdlist_array[i][0], vdlist_array[i][1], vdlist_array2[i][0]));
                }            
            }
            */
            
            // 2019.01.02 view D Drive
            /*
            // 2018.01.26 william 檢查VD List是否為空(判斷是否為雲主機)
            if(vd_data.isEmpty()) {                               
                vd_data.add(new VD("", "", false));                
            }        
            */
            
            // 2019.01.02 view D Drive
            vd_data.addAll(_arrayshow);
            _checkVDarray = _arrayshow;
            
        } catch (Exception ex) {
            //  2018.01.12 william 例外處理
            NoSSfunc_flag = true; //  2018.01.12 william 例外處理
            vd_data.add(new VD("Null","Null", false));
            NoSSfunc();
        }        
            
        vdtable.setEditable(false);       
        vdtable.getColumns().addAll(vdSNCol, vdNameCol);
        vdtable.setItems(vd_data);     
        vdtable.getStyleClass().add("table_header");
        vdtable.setPrefSize(vd_listView_width, vd_listView_height);
        vdtable.setMaxSize(vd_listView_width , vd_listView_height);
        vdtable.setMinSize(vd_listView_width , vd_listView_height);         
        vdtable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY); // 自動重新整理大小,table的colum大小隨table的大小變化而變化

        // 設定第一個元素為被選擇狀態
        vdtable.requestFocus();
        vdtable.getSelectionModel().select(0);
        vdtable.getFocusModel().focus(0);
        vd_index = vdtable.getSelectionModel().getSelectedIndex();
        System.out.println("get vd index: " + vd_index);
        
        vd_selection = new HBox();
	vd_selection.setSpacing(20);       
        vd_selection.setAlignment(Pos.CENTER);
        vd_selection.setPadding(new Insets(0, 0, 0, 0));  
	vd_selection.getChildren().addAll(vdtable);   // 使用Table Column顯示
        
        LeftGP.add(vd_selection,0,1);          
    }

    public final void ss_layout() {   
        /***  Title 放入一個HBOX內 ***/
        Snapshot_ListTitle = new Label(WordMap.get("Select_Snapshot_Layer"));
        Snapshot_ListTitle.getStyleClass().add("Title_label");
        
        WC2 = new HBox(Snapshot_ListTitle);
        WC2.setAlignment(Pos.TOP_CENTER);
        WC2.setPadding(new Insets(0, 0, 0, 12));
        WC2.setTranslateY(0);
        
        MainGP.add(WC2,0,0);           
        
        // 產生畫面列表
        try {
            ListSnapshotofVD();
        } catch (IOException ex) {
            Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }        
       
    public final void desc_layout() {
        if(WordMap.get("SelectedLanguage").equals("Japanese")){ 
            ss_desc1 = new Label("1. まず、クラウドホスト/クラウド型仮想デスクトップを選択してください。次にスナップショットを選択して[表示]");          
            ss_desc1_1 = new Label("ボタンをクリックしてください。");       
            ss_desc1_1.setId("List_label");      
        } else {
            ss_desc1 = new Label(WordMap.get("Desc_Snapshot_operation1"));
        }        
        ss_desc1.setId("List_label");
        ss_desc2 = new Label(WordMap.get("Desc_Snapshot_operation2"));
        ss_desc2.setId("List_label");
        Label ss_desc3 = new Label(WordMap.get("Desc_Snapshot_operation3"));                            
        ss_desc3.setId("List_label");          
        
        Desc = new VBox();
	Desc.setSpacing(10);
        if(WordMap.get("SelectedLanguage").equals("English")){ 
            Desc.setTranslateY(-5);
            Desc.setTranslateX(-90);          
        }  else if((WordMap.get("SelectedLanguage").equals("Japanese"))) {
            Desc.setTranslateY(10);
            Desc.setTranslateX(30);                  
        }
        else { //else if((WordMap.get("SelectedLanguage").equals("TraditionalChinese")) || (WordMap.get("SelectedLanguage").equals("SimpleChinese"))){
            Desc.setTranslateY(-5);
            Desc.setTranslateX(-195);        
        }
        Desc.setAlignment(Pos.CENTER_LEFT);
        Desc.setPadding(new Insets(0, 0, 0, 0));        
        if(WordMap.get("SelectedLanguage").equals("Japanese")){     
            Desc.getChildren().addAll(ss_desc1, ss_desc1_1, ss_desc2, ss_desc3);      
        } else {
            Desc.getChildren().addAll(ss_desc1, ss_desc2, ss_desc3);      
        }    
        
        BottomGP.add(Desc,0,0);  
                         
        // 建立按鈕物件(stop view )
        Btn_stopview = new Button("  " + WordMap.get("Snapshot_Stop_View") + "  ");
        if((WordMap.get("SelectedLanguage").equals("Japanese"))){
            Btn_stopview.setPrefSize(170, btn_height);                  
            Btn_stopview.setMaxSize(170, btn_height);                   
            Btn_stopview.setMinSize(170, btn_height);                   
        } else {
            Btn_stopview.setPrefSize(btn_width, btn_height);                  // 強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
            Btn_stopview.setMaxSize(btn_width, btn_height);                   // 強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
            Btn_stopview.setMinSize(btn_width, btn_height);                   // 強制限制button最小寬度/高度,讓每個button在變換語言時不會變動        
        }     
        Btn_stopview.setOnAction((ActionEvent event) -> {
            try {
                stop_SnapshotLayer();
            } catch (IOException ex) {
                Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        
        // 建立按鈕物件(離開)
        Btn_exit = new Button("  "+WordMap.get("Snapshot_Exit")+"  ");  
        Btn_exit.setPrefSize(btn_width, btn_height);                  // 強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        Btn_exit.setMaxSize(btn_width, btn_height);                   // 強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        Btn_exit.setMinSize(btn_width, btn_height);                   // 強制限制button最小寬度/高度,讓每個button在變換語言時不會變動
//        Btn_exit.setTranslateY(-27);
//        Btn_exit.setTranslateX(15);
//        Btn_exit.setId("LoginMultiVD_Exit");
          

        // 建立按鈕物件(Login)
        //Btn_login = new Button("    "+WordMap.get("Login")+"    ");  
        Btn_login = new Button("  " + WordMap.get("Snapshot_Login") + "  ");  
        Btn_login.setPrefSize(btn_width, btn_height);                   // 強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        Btn_login.setMaxSize(btn_width , btn_height);                   // 強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        Btn_login.setMinSize(btn_width , btn_height);                   // 強制限制button最小寬度/高度,讓每個button在變換語言時不會變動
//        Btn_exit.setTranslateY(-27);
//        Btn_exit.setTranslateX(15);
//        Btn_login.setId("LoginMultiVD_Exit");
        Btn_login.setOnAction((ActionEvent event) -> {
        Login_lock();
                    
        Task<Void> SSloginTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                RespCode = SS_QueryLogin();

                return null;
            }
        };                    
                    
        SSloginTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                login_timerflag = false;               
            }
        });                    
                
        SSloginTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {                                
                if(RespCode == 200) {
                    Login_lock_thr llthr2 = new Login_lock_thr();
                    Thread thr = new Thread(llthr2);
                    thr.start();              
                } else {
                    Alert_HttpsStatusError(RespCode);
                }
            }
        });
        new Thread(SSloginTask).start(); 
        });                
        
        btn = new HBox();
	btn.setSpacing(15); // 元件間的間距設定 // 170 -> 15
        btn.setAlignment(Pos.CENTER_RIGHT);
        btn.setPadding(new Insets(0, 0, 0, 0));   
        if(WordMap.get("SelectedLanguage").equals("English")){ 
            btn.setTranslateX(185); // For windows VDI Viewer        
            btn.setTranslateY(21); // For windows VDI Viewer        
        }  else if((WordMap.get("SelectedLanguage").equals("Japanese"))){
            btn.setTranslateX(65); // For windows VDI Viewer
            btn.setTranslateY(15);        
        }
        else { //else if((WordMap.get("SelectedLanguage").equals("TraditionalChinese")) || (WordMap.get("SelectedLanguage").equals("SimpleChinese"))){
        btn.setTranslateY(-5);
        btn.setTranslateX(287); // 137 -> 145
        }         

        btn.getChildren().addAll(Btn_exit, Btn_stopview, Btn_login);   
		
        BottomGP.add(btn,0,1);         
    }  
            
    public void Login_lock() {
        LoginGP = new GridPane();              
        LoginGP.setAlignment(Pos.CENTER);       
        LoginGP.setPadding(new Insets(35, 50, 50, 20));
        LoginGP.setHgap(15); //元件間的水平距離
        LoginGP.setVgap(15); //元件間的垂直距離
        LoginGP.setPrefSize(MainGP_width - 50, vd_listView_height);
        LoginGP.setMaxSize(MainGP_width - 50, vd_listView_height);
        LoginGP.setMinSize(MainGP_width - 50, vd_listView_height);
        LoginGP.setTranslateX(-25);
        LoginGP.setTranslateY(45);
        
        LoginGP.setStyle("-fx-background-color: #EEF5F5 ; -fx-opacity: 0.7 ; -fx-background-radius: 4;-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 10, 0.0 , 0 , 5 );");    // 2018.18.18 new create & reborn 
        Login_title = new Label(WordMap.get("Thread_Login")+"\n"+WordMap.get("Thread_Waiting"));
        Login_title.setAlignment(Pos.CENTER);
        Login_title.setTranslateY(0);
        Login_title.setId("Thread_status");
        LoginGP.add(Login_title,0,0);

        p1 = new ProgressIndicator();
        p1.setTranslateY(10);
        //p1.setStyle("-fx-foreground-color: #1A237E");
        LoginGP.add(p1,0,1); //2,1是指這個元件要佔用2格的column和1格的row 
                
        stack = new StackPane();
        stack.getChildren().addAll(MainGP, LoginGP);      
        rootPane.setCenter(stack);
        // 按鈕物件失效
        Btn_exit.setDisable(true);
        Btn_stopview.setDisable(true);
        Btn_login.setDisable(true);
        
    }
    
    public void Login_Unlock() {
        // 按鈕物件失效
        Btn_exit.setDisable(false);
        Btn_stopview.setDisable(false);
        Btn_login.setDisable(false);
        rootPane.setCenter(MainGP);
    }  
    
    /*------------------------ 快照：列表, 檢視, 停止檢視, 登入 ------------------------*/    
    // (1-1) 讀取資料
    public void ListSnapshotofVD() throws MalformedURLException, IOException, InterruptedException{
        try{             
            url  = new URL("https://" + Address + ":" + Port + "/vdi/vd/" + getVDID() + "/snapshot");          
            conn = (HttpURLConnection) url.openConnection();     // 建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線
            conn.setRequestMethod("GET");                        // 利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
	    conn.setRequestProperty("Accept"    , "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Connection", "Keep-Alive"); // 維持長連接            
            conn.setConnectTimeout(20000);                       // (5秒改20秒，不然在getResponseCode容易失敗) // 5 -> 10 -> 20
            conn.setReadTimeout(10000);                          // (2秒改10秒，不然在getResponseCode容易失敗) // 2 -> 10               
            conn.connect();
            
            Chcek_ss_ResponseCode = conn.getResponseCode() ;

            System.out.println("List Snapshot of VD-URL         : " + url + "\n"); 
            System.out.println("List Snapshot of VD-Resp Code   : " + Chcek_ss_ResponseCode + "\n"); 
            System.out.println("List Snapshot of VD-Resp Message: " + conn.getResponseMessage() + "\n");
            
            // 讀取資料
            InputStream is = conn.getInputStream();
            StringBuilder sb;
            String line;  
            
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {              
                 sb = new StringBuilder("");
                 while ((line = br.readLine()) != null) {            
                     sb.append(line);         
                 }             
                 System.out.println("List Snapshot of VD-return sb: "+sb.toString()+"\n");  
                 if(sb.toString().equals("[]")) {
                     sslist_json_length_empty = true;
                 } else {
                     sslist_json_length_empty = false;
                 }
                 br.close();
            }
             
            String sslist_data = sb.toString();   
            JsonParser data_jp = new JsonParser();
//            JsonObject data_jo = (JsonObject)data_jp.parse(sslist_data);
            
            sslist_jsonArr = (JsonArray) data_jp.parse(sslist_data);
            
            CreateSnapshotList();
            
            conn.disconnect();
        } catch (MalformedURLException e) {
           System.out.println("MalformedURLException: " + e + "\n");             
	} catch (IOException e) {    
           System.out.println("IOException: " + e + "\n");
           CreateSnapshotList();
        } 

        
    }    
    // (1-2) 產生列表
    public void CreateSnapshotList() {
        ss_data.clear(); // 清空data先前的內容
    
        ssSNCol   = new TableColumn(WordMap.get("VD_SN"));
        /*------------------------ 讓Table內顯示項次 ------------------------*/
        ssSNCol.setCellValueFactory(new Callback<CellDataFeatures<SS, SS>, ObservableValue<SS>>() {
             @Override
             public ObservableValue<SS> call(CellDataFeatures<SS, SS> s) {
                return new ReadOnlyObjectWrapper(s.getValue());
            }
        });        
        
        ssSNCol.setCellFactory(new Callback<TableColumn<SS, SS>, TableCell<SS, SS>>() {
            @Override 
            public TableCell<SS, SS> call(TableColumn<SS, SS> param) {
                return new TableCell<SS, SS>() {
                    @Override 
                    protected void updateItem(SS item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && item != null) {
                            int number = this.getTableRow().getIndex();
                            setText(" " + (vd_index + 1) + "-" + (number + 1) + " ");
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });        
        
        if(WordMap.get("SelectedLanguage").equals("Japanese")) {
            ssSNCol.setPrefWidth(90);
            ssSNCol.setMinWidth(90);
        } else {
            ssSNCol.setPrefWidth(60);
            ssSNCol.setMinWidth(60);
            //ssSNCol.setMaxWidth(60);              
        }             

        ssSNCol.setSortable(false);
        ssSNCol.setStyle("-fx-font-family:'Times New Roman';");      
        /*------------------------ 讓Table內顯示狀態 ------------------------*/
        StateCol  = new TableColumn(WordMap.get("Snapshot_State"));
        StateCol.setPrefWidth(100);
        StateCol.setMinWidth(100);
        StateCol.setCellFactory(getCustomCellFactory());
        /*------------------------ 讓Table內顯示時間 ------------------------*/             
        CreateTimeCol = new TableColumn(WordMap.get("Time"));
        CreateTimeCol.setPrefWidth(190);
        CreateTimeCol.setMinWidth(190);
        //CreateTimeCol.setMaxWidth(190);            
        /*------------------------ 讓Table內顯示大小 ------------------------*/          
        SizeCol   = new TableColumn(WordMap.get("Snapshot_Size"));     
        SizeCol.setPrefWidth(100);
        SizeCol.setMinWidth(100);
        //SizeCol.setMaxWidth(100);
        SizeCol.setStyle( "-fx-alignment: center-right;"); 
        /*------------------------ 讓Table內顯示備註 ------------------------*/          
        DescCol   = new TableColumn(WordMap.get("Snapshot_Description"));     
        DescCol.setPrefWidth(180);
        DescCol.setMinWidth(180);
        DescCol.setStyle( "-fx-alignment: center-left;"); 
        /*------------------------ 讓Table內顯示動作 ------------------------*/      
        ActionCol = new TableColumn(WordMap.get("Action"));    // 2018.07.06 快照新增還原按鈕
        if((WordMap.get("SelectedLanguage").equals("Japanese"))){
            ActionCol.setPrefWidth(230);
            ActionCol.setMinWidth(230);                  
        } else {
            ActionCol.setPrefWidth(160);
            ActionCol.setMinWidth(160);
            //ActionCol.setMaxWidth(160); 
        }       
        // 設定每一個欄位對應的JavaBean物件與Property名稱    
        StateCol.setCellValueFactory(new PropertyValueFactory<>("State"));                           
        CreateTimeCol.setCellValueFactory(new PropertyValueFactory<>("CreateTime"));
        SizeCol.setCellValueFactory(new PropertyValueFactory<>("Size"));
        DescCol.setCellValueFactory(new PropertyValueFactory<>("Desc"));
        ActionCol.setCellValueFactory(new PropertyValueFactory<>("Action"));   
        
        // 2019.01.02 view D Drive
         SS[] _array = null;
        
        if(sslist_jsonArr != null) {
            if(sslist_jsonArr.size() == 0)
                _array = new SS[1];            
            else
                _array = new SS[sslist_jsonArr.size()];        
        } else {
            _array = new SS[1];        
        }
                    
        // 判斷傳入DATA的json是否為空
        if(sslist_jsonArr != null && sslist_json_length_empty == false) {                        
            for (int i = 0; i < sslist_jsonArr.size(); i++) {
                ss_term           = sslist_jsonArr.get(i).getAsJsonObject();  
                sslist_Layer      = ss_term.get("Layer").getAsString();                
                sslist_State      = ss_term.get("State").getAsString();                
                if (sslist_State.equals("2")) {
                    setViewLayerID(sslist_Layer);
                } 
                
                sslist_State      = SS_StatusName(ss_term.get("State").getAsString(), StateCol);                
                sslist_CreateTime = ss_term.get("CreateTime").getAsString();                                                         
                sslist_Size       = formatFileSize(ss_term.get("Size").getAsLong(),false);                
                sslist_Desc       = ss_term.get("Desc").getAsString();      
            
                // 2019.01.02 view D Drive
                /*
                sslist_array[i][0] = sslist_Layer;
                sslist_array[i][1] = sslist_State;
                sslist_array[i][2] = sslist_CreateTime;
                sslist_array[i][3] = sslist_Size;
                sslist_array[i][4] = sslist_Desc;
                */
                
                _array[i] = new SS(sslist_Layer, sslist_State, sslist_CreateTime, sslist_Size, sslist_Desc);
            
                // ss_data.add(new SS(sslist_Layer, sslist_State, sslist_CreateTime, sslist_Size, sslist_Desc));
            }          

            // 2019.01.02 view D Drive
            /*
            for (int i = 0; i < sslist_jsonArr.size(); i++) {
                ss_data.add(new SS(sslist_array[i][0], sslist_array[i][1], sslist_array[i][2], sslist_array[i][3], sslist_array[i][4]));
            }
            */    
            /*------------------------ 產生列表按鈕 ------------------------*/
            CreateTableBtn(ActionCol);                
        } else {
            // 2019.01.02 view D Drive
            // ss_data.add(new SS("", "", "", "", ""));
            _array[0] = new SS("", "", "", "", "");
            
            //  2018.01.12 william 例外處理
            // 2018.01.26 william  取消未建立快照的訊息
//            if(!NoSSfunc_flag){ //  2018.01.12 william 例外處理
//                SnapshotData_Empty();
//            } 
        }
        
        // 2019.01.02 view D Drive
        ss_data.addAll(_array);
        _checkSSarray = _array;
        
        ssdata_refresh();

        sstable.setEditable(true);       
        sstable.getColumns().addAll(ssSNCol, ActionCol, StateCol, DescCol, CreateTimeCol, SizeCol);
        sstable.setItems(ss_data);  
        sstable.getStyleClass().add("table_header");
        sstable.setPrefSize(ss_listView_width, ss_listView_height);
        sstable.setMaxSize(ss_listView_width , ss_listView_height);
        sstable.setMinSize(ss_listView_width , ss_listView_height); 
        sstable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY); // 自動重新整理大小,table的colum大小隨table的大小變化而變化

        ss_selection = new HBox();
	ss_selection.setSpacing(20);       
        ss_selection.setAlignment(Pos.CENTER);
        ss_selection.setPadding(new Insets(0, 0, 0, 0));  
	ss_selection.getChildren().addAll(sstable);   // 使用Table Column顯示     

        MainGP.add(ss_selection,0,1);         
    }   
    // (1-3) 產生列表按鈕
    public void CreateTableBtn (TableColumn<SS, String> ActionCol) {
        Callback<TableColumn<SS, String>, TableCell<SS, String>> cellFactory = new Callback<TableColumn<SS, String>, TableCell<SS, String>>() {
            @Override
            public TableCell call(final TableColumn<SS, String> param) {
                final TableCell<SS, String> cell = new TableCell<SS, String>() {

                    final Button btn = new Button(WordMap.get("Snapshot_View"));
                    final Button btn2 = new Button(WordMap.get("Snapshot_Rollback")); // 2018.07.06 快照新增還原按鈕

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.getStyleClass().add("table-btn");
                            btn.setOnAction(event -> {
                                SS s = getTableView().getItems().get(getIndex());
                                // System.out.println(s.getLayer());
                                /*------------------------ 是否開啟網路 ------------------------*/ 
                                // 2018.07.12 還原按鈕和修改dialog 
                                String l = s.getLayer();                                
                                IsKeepNIC_func(l);
//                                try {
//                                    setViewLayerID(s.getLayer());
//                                    View_SnapshotLayer();
//                                } catch (Exception e) {
//                                    
//                                } 
                            });
                            // 2018.07.06 快照新增還原按鈕
                            btn2.getStyleClass().add("table-btn");
                            btn2.setOnAction(event -> {
                                SS s = getTableView().getItems().get(getIndex());
                                setRollBackLayerID(s.getLayer());
                                // 2018.07.12 還原按鈕和修改dialog
                                try {
                                    IsRollBackAlert();
                                } catch (IOException ex) {
                                    Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            });                            
                            //setGraphic(btn);
                            HBox pane = new HBox(btn, btn2);
                            switch (WordMap.get("SelectedLanguage")) {
                                case "English":
                                    pane.setPadding(new Insets(3, 5, 3, 15));
                                    break;
                                default:
                                    pane.setPadding(new Insets(3, 5, 3, 30));
                                    break;
                            }                                                          
                            pane.setSpacing(10);                            
                            setGraphic(pane); 
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };

        ActionCol.setCellFactory(cellFactory);       
    }
    // (2) 檢視
    public void  View_SnapshotLayer() throws MalformedURLException, IOException, InterruptedException {        
        JSONObject jobj = new JSONObject();
        jobj.put("KeepNIC", IsKeepNIC);
        String KeepNIC = jobj.toString();        
        try {
            url  = new URL("https://" + Address + ":" + Port + "/vdi/snapshot/view/" + getViewLayerID());          
            conn = (HttpURLConnection) url.openConnection();                                     // 建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線
            conn.setRequestMethod("PUT");                                                        // 利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Accept"        , "application/json");
            conn.setRequestProperty("User-Agent"    , "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Content-Type"  , "application/json");                       // 設置發送數據的格式("Content-Type", "application/json; charset=UTF-8")
            conn.setRequestProperty("Content-length", String.valueOf(KeepNIC.getBytes().length)); // ("Content-length", String.valueOf(query.length()))
            conn.setRequestProperty("Connection"    , "Keep-Alive");                             // 維持長連接
            conn.setUseCaches (false);
            conn.setInstanceFollowRedirects(true);        

            conn.setConnectTimeout(10000);                                                       // 5秒改10秒，不然在getResponseCode容易失敗       
            /*----------------------------content----------------------------*/
            try (OutputStream ops = conn.getOutputStream()) {
                ops.write(KeepNIC.getBytes());
                ops.flush();
                ops.close();
                System.out.println("View Snapshot Layer-output out : "  + ops + "\n");                  
                System.out.println("View Snapshot Layer-URL         : " + url + "\n");             
                System.out.println("View Snapshot Layer-Resp Code   : " + conn.getResponseCode() + "\n");
                System.out.println("View Snapshot Layer-Resp Message: " + conn.getResponseMessage() + "\n");                           
                conn.disconnect();      
            }                                 
        } catch (MalformedURLException e) {

        } catch (IOException e) {    

        }         

    }
    // (3) 停止檢視
    public void  stop_SnapshotLayer() throws MalformedURLException, IOException, InterruptedException {              
        try {
            url  = new URL("https://" + Address + ":" + Port + "/vdi/snapshot/stop_view/" + getVDID());          
            conn = (HttpURLConnection) url.openConnection();                                     // 建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線
            conn.setRequestMethod("PUT");                                                        // 利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Accept"        , "application/json");
            conn.setRequestProperty("User-Agent"    , "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Content-Type"  , "application/json");                       // 設置發送數據的格式("Content-Type", "application/json; charset=UTF-8")

            conn.setRequestProperty("Connection"    , "Keep-Alive");                             // 維持長連接
            conn.setUseCaches (false);
            conn.setInstanceFollowRedirects(true);        

            conn.setConnectTimeout(10000);                                                       // 5秒改10秒，不然在getResponseCode容易失敗       
            /*----------------------------content----------------------------*/
            try (OutputStream ops = conn.getOutputStream()) {
                ops.flush();
                ops.close();
                System.out.println("stop View -output out : "  + ops + "\n");                  
                System.out.println("stop View -URL         : " + url + "\n");             
                System.out.println("stop View -Resp Code   : " + conn.getResponseCode() + "\n");
                System.out.println("stop View -Resp Message: " + conn.getResponseMessage() + "\n");            
                conn.disconnect();      
            }                                 
        } catch (MalformedURLException e) {

        } catch (IOException e) {    

        }         

    }
    // (4) 登入
    public void  Login_SnapshotLayer() throws MalformedURLException, IOException, InterruptedException {             
        try {                       
            login_timerflag = true;
            CheckSSLogincompleted();
            url  = new URL("https://" + Address + ":" + Port + "/vdi/snapshot/login/" + getViewLayerID());   
            HttpURLConnection conn_setVDonline = (HttpURLConnection) url.openConnection();                     // 建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線
            conn_setVDonline.setRequestMethod("PUT");                                                          // 利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
            conn_setVDonline.setDoOutput(true);
            conn_setVDonline.setDoInput(true);
            conn_setVDonline.setRequestProperty("Accept"        , "application/json");
            conn_setVDonline.setRequestProperty("User-Agent"    , "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn_setVDonline.setRequestProperty("Content-Type"  , "application/json");                         // 設置發送數據的格式("Content-Type", "application/json; charset=UTF-8")
//            conn_setVDonline.setRequestProperty("Content-length", String.valueOf(cliendID.getBytes().length)); // ("Content-length", String.valueOf(query.length()))
            conn_setVDonline.setRequestProperty("Connection"    , "Keep-Alive");                               // 維持長連接
            //conn.connect();    
            conn_setVDonline.setUseCaches (false);
            conn_setVDonline.setInstanceFollowRedirects(true);
            
            conn_setVDonline.setConnectTimeout(10000); // 2017.11.24 單一帳號多個VD登入 (5秒改10秒，不然在getResponseCode容易失敗)

            try (OutputStream out2 = conn_setVDonline.getOutputStream()) {                                
                out2.flush();
                out2.close();
                System.out.println("login View -output out : "  + out2 + "\n");                  
                System.out.println("login View -URL         : " + url + "\n");         
                RespCode = conn_setVDonline.getResponseCode(); //  2018.01.12 william 例外處理
                System.out.println("login View -Resp Code   : " + RespCode + "\n"); //  2018.01.12 william 例外處理                
                System.out.println("login View -Resp Code   : " + conn_setVDonline.getResponseCode() + "\n");
                System.out.println("login View -Resp Message: " + conn_setVDonline.getResponseMessage() + "\n");            
            
                InputStream is = conn_setVDonline.getInputStream();           
                StringBuilder sb;
                String line;  

                try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {              
                     sb = new StringBuilder("");
                    while ((line = br.readLine()) != null) {            
                        sb.append(line);         
                    }                                 
                    System.out.println("login View Snapshot Layer-return sb: " + sb.toString() + "\n");         
                    br.close();
                }

                String Sdata = sb.toString();             
                JsonParser jsonSParser = new JsonParser();
                JsonObject Spicedata = (JsonObject)jsonSParser.parse(Sdata).getAsJsonObject();
                JsonArray SpiceArr = Spicedata.getAsJsonObject().getAsJsonArray("SpiceServers");
//                System.out.println("JsonArray SpiceArr: "+SpiceArr+"\n");       
                //  2018.11.06 william 多執行緒Ping IP
                new PingIP(SpiceArr);                
                long startTime = System.currentTimeMillis(); // fetch starting time
                while(false || (System.currentTimeMillis()-startTime) < 5000) {
                    // System.out.println("IpAddress : " + GB.ConnectionAddress + " Port : " + GB.ConnectionPort);
                    if(!"".equals(GB.ConnectionAddress)  && !"".equals(GB.ConnectionPort)) {
                        RunSpice(GB.ConnectionAddress, GB.ConnectionPort);
                        break;
                    }
                    TimeUnit.SECONDS.sleep(1);
                }                
                System.out.println("Finish Run Spice");                 
                login_timerflag = false;
//                for (int i = 0; i < SpiceArr.size(); i++) {
//                    JsonObject Sterm = SpiceArr.get(i).getAsJsonObject();
//                    SpiceAddress     = Sterm.get("Address").getAsString();
//                    SpicePort        = Sterm.get("Port").toString();
////                    PSV.PingSetVD(SpiceAddress,SpicePort); 
//                    System.out.println("SpiceAddress: " + SpiceAddress + " SpicePort: " + SpicePort + "\n");                                                                            
//                                        
//                    if(isReachableByTcp(SpiceAddress, Integer.parseInt(SpicePort), 1000)) {
//                        WriteSpiceText(SpiceAddress, SpicePort, CurrentUserName, CurrentPasseard);
//                    
//                        File f = new File("jsonfile/SpiceText.txt");       
//                    
//                        if(f.exists()) {    
//                            // 2018.10.11 william G300s Mouse問題修改
//                            QM.CheckRmProcess(public_stage);
//                            /*
//                            params    = new String [2];           
//                            params[0] = "/root/RemoteViewer/virt/bin/remote-viewer"; 
//                            params[1] = f.toString();                       
//                            process   = Runtime.getRuntime().exec(params);           
//                            process   = null;    
//                            */
//                        }         
//                    }
//                }
//               
//                login_timerflag = false;
                conn_setVDonline.disconnect();                  
            }            
        } catch (MalformedURLException e) {} catch (IOException e) {}       
    }
    
    public void RunSpice(String address, String port) throws IOException, InterruptedException {
        SpiceAddress     = address;
        SpicePort        = port;  
        System.out.println("SS SpiceAddress: " + SpiceAddress + " SpicePort: " + SpicePort + "\n");                                                                            
                                                            
        WriteSpiceText(SpiceAddress, SpicePort, CurrentUserName, CurrentPasseard);                    
        File f = new File("jsonfile/SpiceText.txt");       
        if(f.exists()) {    
            // 2018.10.11 william G300s Mouse問題修改
            QM.CheckRmProcess(public_stage);
            /*
            params    = new String [2];           
            params[0] = "/root/RemoteViewer/virt/bin/remote-viewer"; 
            params[1] = f.toString();                       
            process   = Runtime.getRuntime().exec(params);           
            process   = null;    
            */            
            
        }         
               
               
    }      

    // (5) 還原 // 2018.07.06 快照新增還原按鈕
    public void  RollBack_SnapshotLayer() throws MalformedURLException, IOException, InterruptedException {              
        try {
            URL url  = new URL("https://" + Address + ":" + Port + "/vdi/snapshot/rollback/" + getRollBackLayerID());          
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();                   // 建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線
            conn.setRequestMethod("PUT");                                                        // 利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Accept"        , "application/json");
            conn.setRequestProperty("User-Agent"    , "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Content-Type"  , "application/json");                       // 設置發送數據的格式("Content-Type", "application/json; charset=UTF-8")

            conn.setRequestProperty("Connection"    , "Keep-Alive");                             // 維持長連接
            conn.setUseCaches (false);
            conn.setInstanceFollowRedirects(true);        

            conn.setConnectTimeout(10000);                                                       // 5秒改10秒，不然在getResponseCode容易失敗       
            /*----------------------------content----------------------------*/
            try (OutputStream ops = conn.getOutputStream()) {
                ops.flush();
                ops.close();
                System.out.println("IsDefault -output out : "  + ops + "\n");                  
                System.out.println("IsDefault -URL         : " + url + "\n");             
                System.out.println("IsDefault -Resp Code   : " + conn.getResponseCode() + "\n");
                if(conn.getResponseCode()==404) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                
                    alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE); 

                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Unknown_system_error")+" ( "+ conn.getResponseCode() +" ) "+"\n"
                                     +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_400")
                    );
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                    alert.showAndWait();
                }                
                System.out.println("IsDefault -Resp Message: " + conn.getResponseMessage() + "\n");            
                conn.disconnect();      
            }                                 
        } catch (MalformedURLException e) {

        } catch (IOException e) {    

        }         

    }    
              
    
    public static boolean isReachableByTcp(String host, int port, int timeout) {
        try {
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(host, port);
            socket.connect(socketAddress, timeout);
            socket.close();
            System.out.println("isReachableByTcp: true \n");                                                                            
            return true;
        } catch (IOException e) {
            System.out.println("isReachableByTcp: false \n");
            return false;
        }
    }            
    
    /*------------------------ 檔案大小轉換 ------------------------*/
    public static String formatFileSize(long size, boolean si) {
        String hrSize     = null;
        int unit         = (si ? 1000 : 1024);

        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(unit));
        return new DecimalFormat("#,##0.00").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
        // 0 顯示, # 不顯示
    }   
    /*------------------------ 桌面狀態 VD_Status 轉換語言(json) ------------------------*/
    public  String SS_StatusName(String Satus, TableColumn column){                           
     
        if(null != Satus) switch (Satus) {
            case "0":
                ss_state_string = WordMap.get("Snapshot_Normal");
                break;
            case "1":
                ss_state_string = WordMap.get("Snapshot_Preparing_view");
                break;
            case "2":
                ss_state_string = WordMap.get("Snapshot_state_View");
                break;
            case "3":
                ss_state_string = WordMap.get("Snapshot_Preparing_delete");
                break;
            case "4":
                ss_state_string = WordMap.get("Snapshot_Preparing_rollback");
                break;
            case "5":
                ss_state_string = WordMap.get("Snapshot_Preparing_unview");
                break;
            default:
                break;
        }
        
        return ss_state_string;
    
    }    
    /*------------------------ TableColumn 文字 變換顏色 ------------------------*/
    private Callback<TableColumn<SS, String>, TableCell<SS, String>> getCustomCellFactory() { // final String color
        return new Callback<TableColumn<SS, String>, TableCell<SS, String>>() {
            @Override
            public TableCell<SS, String> call(TableColumn<SS, String> param) {
                TableCell<SS, String> cell = new TableCell<SS, String>() {

                    @Override
                    public void updateItem(final String item, boolean empty) {
                        if (item != null) {
//                            System.out.println("item: " + item + ", " + "empty: " + empty +"\n");
                            
                            if(item.equals("檢視中")||item.equals("View")||item.equals("检视中")) {
                                setText(item);
                                setStyle("-fx-text-fill: " + "green" + ";");
                            } else {
                                setText(item);
                                setStyle("-fx-text-fill: " + "blue" + ";");
                            }
                            
                            

                        }
                    }
                };
                return cell;
            }
        };
    }        
    // VD Status change color
    private Callback<TableColumn<VD, String>, TableCell<VD, String>> getCustomVDCellFactory() { // final String color
        return new Callback<TableColumn<VD, String>, TableCell<VD, String>>() {
            @Override
            public TableCell<VD, String> call(TableColumn<VD, String> param) {
                TableCell<VD, String> cell = new TableCell<VD, String>() {

                    @Override
                    public void updateItem(final String item, boolean empty) {
                        if (item != null) {
//                            System.out.println("item: " + item + ", " + "empty: " + empty +"\n");
                                VD v = getTableView().getItems().get(getIndex());
                            if(v.getVD_IsSnapshotView()) {
                                setText(item);
                                setStyle("-fx-text-fill: " + "green" + ";");
                            } else {
                                setText(item);
                                setStyle("-fx-text-fill: " + "blue" + ";");
                            }
                            
                            

                        }
                    }
                };
                return cell;
            }
        };
    }
    /*------------------------ 使用網路警告視窗 ------------------------*/
    public void IsKeepNIC_func(String l) {  // 2018.07.12 還原按鈕和修改dialog 
           
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
        alert.setTitle(WordMap.get("Alert_Title_Warning"));          // 設定對話框視窗的標題列文字
        alert.setHeaderText("");                                     // 設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        // 設定大小
        alert.getDialogPane().setMinSize(450,Region.USE_PREF_SIZE);
        alert.getDialogPane().setMaxSize(450,Region.USE_PREF_SIZE);        
        // 設定對話框的訊息文字
        alert.setContentText(
            WordMap.get("WW_Header")       + " ： " + WordMap.get("Snapshot_keepnic") 
            + "\n" 
            + "\n" 
            + 
            WordMap.get("Message_Suggest") + " ： " + WordMap.get("Snapshot_keepnic_Suggest")
        );        
 
        
        // 建立ButtonType物件
        ButtonType buttonTypeOK     = new ButtonType(WordMap.get("Alert_SSBtn_Keep"),ButtonBar.ButtonData.OK_DONE);          // OK_DONE // 2018.07.12 還原按鈕和修改dialog 
        ButtonType buttonTypeCancel = new ButtonType(WordMap.get("Alert_SSBtn_NotKeep") ,ButtonBar.ButtonData.OK_DONE);      // CANCEL_CLOSE // 2018.07.12 還原按鈕和修改dialog 
        ButtonType buttonTypeExit   = new ButtonType(WordMap.get("Alert_Cancel"),ButtonBar.ButtonData.CANCEL_CLOSE); // 2018.07.12 還原按鈕和修改dialog
            
        alert.getButtonTypes().setAll(buttonTypeOK,buttonTypeCancel,buttonTypeExit); // 2018.07.12 還原按鈕和修改dialog 
            
        // 建立Button物件並設定按鍵邏輯
        Button button_ok     = (Button) alert.getDialogPane().lookupButton(buttonTypeOK);
        Button button_cancel = (Button) alert.getDialogPane().lookupButton(buttonTypeCancel);
        Button button_exit   = (Button) alert.getDialogPane().lookupButton(buttonTypeExit); // 2018.07.12 還原按鈕和修改dialog

        button_ok.setOnMouseEntered((event)-> {
            button_ok.requestFocus();      
        });

        button_cancel.setOnMouseEntered((event)-> {
            button_cancel.requestFocus();        
        });

        button_exit.setOnMouseEntered((event)-> { // 2018.07.12 還原按鈕和修改dialog 
            button_exit.requestFocus();        
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

        button_exit.setOnKeyPressed((event)-> { // 2018.07.12 還原按鈕和修改dialog           
            if(event.getCode() == KeyCode.ENTER){
                button_exit.fire();              
            }        
        });          
        
        // 警告視窗Style設定
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("myDialogs.css");
        dialogPane.getStyleClass().add("myDialog");

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(public_stage.getScene().getWindow()); // 有沒有這一行很重要，決定這兩個視窗是否有從屬關係
            
        final Optional<ButtonType> opt = alert.showAndWait();
        final ButtonType rtn = opt.get();                     // 可以直接用「alert.getResult()」來取代           
        if (rtn == buttonTypeOK) {                            // 若使用者按下「確定」
            IsKeepNIC  = true;
             // 2018.07.12 還原按鈕和修改dialog 
            System.out.println("IsKeepNIC: " + IsKeepNIC + "layer: " + l + "\n");
            try {
                setViewLayerID(l);
                View_SnapshotLayer();
            } catch (Exception e) {

            } 
        } else if (rtn == buttonTypeCancel) {
            IsKeepNIC  = false;
             // 2018.07.12 還原按鈕和修改dialog 
            System.out.println("IsKeepNIC: " + IsKeepNIC +  "layer: " + l + "\n");
            try {
                setViewLayerID(l);
                View_SnapshotLayer();
            } catch (Exception e) {

            }  
        } else {
            System.out.println("IsKeepNIC: " + "none" + "\n");
        }         
        
        
    }   

    public void IsRollBackAlert() throws IOException, MalformedURLException, InterruptedException{ // 2018.07.12 還原按鈕和修改dialog      
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(WordMap.get("Alert_Title_Warning"));
        alert.setHeaderText(null);
        alert.getDialogPane().setMinSize(450,Region.USE_PREF_SIZE);
        alert.getDialogPane().setMaxSize(450,Region.USE_PREF_SIZE);
        alert.setContentText(WordMap.get("WW_Header") + " ： " + WordMap.get("Snapshot_RollBack_Confirm"));            
        ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType(WordMap.get("Alert_Cancel"),ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);         
        Optional<ButtonType> result01 = alert.showAndWait();
        if (result01.get() == buttonTypeOK) {                    
            try {                                                
                RollBack_SnapshotLayer();
            } catch (IOException ex) {
                Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
            }     
        } else {
            alert.close();
        }
    }    
        
    
    public void SnapshotData_Empty() {
           
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
        alert.setTitle(WordMap.get("Alert_Title_Warning"));          // 設定對話框視窗的標題列文字
        alert.setHeaderText("");                                     // 設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        alert.setContentText(WordMap.get("Snapshot_SSDataEmpty"));   // 設定對話框的訊息文字
        
        // 建立ButtonType物件
        ButtonType buttonTypeOK     = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            
        alert.getButtonTypes().setAll(buttonTypeOK); 
            
        // 建立Button物件並設定按鍵邏輯
        Button button_ok     = (Button) alert.getDialogPane().lookupButton(buttonTypeOK);

        button_ok.setOnMouseEntered((event)-> {
            button_ok.requestFocus();      
        });

        button_ok.setOnKeyPressed((event)-> {           
            if(event.getCode() == KeyCode.ENTER){
                button_ok.fire();              
            }        
        });

        // 警告視窗Style設定
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("myDialogs.css");
        dialogPane.getStyleClass().add("myDialog");

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(public_stage.getScene().getWindow()); // 有沒有這一行很重要，決定這兩個視窗是否有從屬關係
            
        final Optional<ButtonType> opt = alert.showAndWait();
        final ButtonType rtn = opt.get();                     // 可以直接用「alert.getResult()」來取代           
        if (rtn == buttonTypeOK) {                            // 若使用者按下「確定」
            StopAll(); // 2019.01.02 view D Drive   
        }          
    }    
    /*------------------------ Polling 取值 ------------------------*/
    public void ssdata_refresh() {  
        fresh_sslist_timer = new Timer(); 

        sstask = new TimerTask() {    
            @Override
            public void run() {
//                ccc++;
//              System.out.println("listening table: "+ccc+"\n");

        try{             
            url  = new URL("https://" + Address + ":" + Port + "/vdi/vd/" + getVDID() + "/snapshot");          
            conn = (HttpURLConnection) url.openConnection();     // 建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線
            conn.setRequestMethod("GET");                        // 利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
	    conn.setRequestProperty("Accept"    , "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Connection", "Keep-Alive"); // 維持長連接
            conn.setConnectTimeout(20000);                       // (5秒改20秒，不然在getResponseCode容易失敗) // 5 -> 10 -> 20
            conn.setReadTimeout(10000);                          // (2秒改10秒，不然在getResponseCode容易失敗) // 2 -> 10              
            conn.connect();
            
            // Chcek_ss_ResponseCode = conn.getResponseCode() ;
            
            // 2019.01.02 view D Drive
            if(conn.getResponseCode()== 400) {
                fresh_sslist_timer.cancel();
                setVDID(getBeforevid());
                ssdata_refresh();
                System.out.println("VD is gone");
            }

            // 讀取資料
            InputStream is = conn.getInputStream();
            StringBuilder sb;
            String line;  
            
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {              
                 sb = new StringBuilder("");
                 while ((line = br.readLine()) != null) {            
                     sb.append(line);         
                 }          
                 if(sb.toString().equals("[]")) {
                     sslist_json_length_empty = true;
                 } else {
                     sslist_json_length_empty = false;
                 }                 
                 br.close();
            }
             
            String sslist_data = sb.toString();   
            JsonParser data_jp = new JsonParser();
//            JsonObject data_jo = (JsonObject)data_jp.parse(sslist_data);
            
            sslist_jsonArr = (JsonArray) data_jp.parse(sslist_data);
            BtnDisable_flag = 0;
            
            // 2019.01.02 view D Drive
            SS[] _array = null;

            if(sslist_jsonArr != null) {
                if(sslist_jsonArr.size() == 0)
                    _array = new SS[1];            
                else
                    _array = new SS[sslist_jsonArr.size()];        
            } else {
                _array = new SS[1];        
            }             
                        
            for (int i = 0; i < sslist_jsonArr.size(); i++) {
                ss_term           = sslist_jsonArr.get(i).getAsJsonObject();  
                sslist_Layer      = ss_term.get("Layer").getAsString();                
                sslist_State      = ss_term.get("State").getAsString();
                                        
                if (sslist_State.equals("2")) {
                    setViewLayerID(sslist_Layer);
                    BtnDisable_flag ++;
                }         
            
            
                sslist_State      = SS_StatusName(ss_term.get("State").getAsString(), StateCol);
                sslist_CreateTime = ss_term.get("CreateTime").getAsString();                                                         
                sslist_Size       = formatFileSize(ss_term.get("Size").getAsLong(),false);                
                sslist_Desc       = ss_term.get("Desc").getAsString();      
                
                // 2019.01.02 view D Drive
                /*
                sslist_array[i][0] = sslist_Layer;
                sslist_array[i][1] = sslist_State;
                sslist_array[i][2] = sslist_CreateTime;
                sslist_array[i][3] = sslist_Size;
                sslist_array[i][4] = sslist_Desc;
                */
                
                _array[i] = new SS(sslist_Layer, sslist_State, sslist_CreateTime, sslist_Size, sslist_Desc);
            
                //ss_data.add(new SS(sslist_Layer, sslist_State, sslist_CreateTime, sslist_Size, sslist_Desc));
            }          
        
            if (BtnDisable_flag>0) {
                Btn_login.setDisable(false);
                Btn_stopview.setDisable(false);
            } else {
                Btn_login.setDisable(true);
                Btn_stopview.setDisable(true); 
            }

            // 2019.01.02 view D Drive
            if(sslist_jsonArr.size() > 0)
                CreateTableBtn(ActionCol);   
//        sstable.refresh();  
//        ss_data.removeAll(ss_data);
                                   
            if(sslist_json_length_empty == false) {
                if(sslist_jsonArr != null && sslist_jsonArr.size() > 0 ) { // 2018.07.06 快照新增還原按鈕                                        
                    // 2019.01.02 view D Drive
                    /*
                    ss_data.clear(); // 清空data先前的內容
                    for (int i = 0; i < sslist_jsonArr.size(); i++) {
                        ss_data.add(new SS(sslist_array[i][0], sslist_array[i][1], sslist_array[i][2], sslist_array[i][3], sslist_array[i][4]));
                    }                
                    */
                    // 2019.01.30 UserDisk快照顯示異常問題修正
                    if(!equals(_checkSSarray, _array) || !_compareVdId.equals(getVDID())) { //  2019.05.16    
                        _compareVdId = getVDID(); //  2019.05.16
                        ss_data.removeAll(ss_data);
                        ss_data.addAll(_array); 
                        _checkSSarray = _array;
                        sstable.refresh();        
                        System.out.println("fresh Snapshot view");
                    }   

                }
            } else {
                // 2019.01.02 view D Drive
                _array[0] = new SS("", "", "", "", "");
                ss_data.clear(); // 清空data先前的內容
                //ss_data.add(new SS("", "", "", "", ""));
                ss_data.addAll(_array);
                _checkSSarray = _array;
                sstable.refresh(); // 2019.01.30 UserDisk快照顯示異常問題修正
//                System.out.println("Snapshot empty");
            }
        
            
            conn.disconnect();
            
        vddata_refresh();    
            
        } catch (MalformedURLException e) {
            
	} catch (IOException e) {    
            
        } 

            }
            
            // 2019.01.02 view D Drive
            private boolean equals(SS[] a, SS[] b) {
                if(a.length != b.length)
                    return false;
                for (int i = 0; i < a.length; i++) {
                    if(a[i].State.getValue() != b[i].State.getValue())
                        return false;
                }
                return true;
            }             
            
            
        };    

        fresh_sslist_timer.schedule(sstask, 100, 8000);  // 2018.07.06 快照新增還原按鈕
    }
 
    public void vddata_refresh() {
        int getIndex = 0; // 2018.07.06 快照新增還原按鈕        
//        fresh_vdlist_timer = new Timer();
//        
//        vdtask = new TimerTask() {
//            @Override
//            public void run() {
                // POST Data(username and password)
                obj = new JSONObject();
                obj.put("AccountName", CurrentUserName);
                obj.put("Password"   , CurrentPasseard);       
                query = obj.toString();                
                
                try {
                    bypassSSL();
                } catch (UnknownHostException ex) {
                    Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
                }                
                /***************************** POST Start *****************************/
                try {
                    url2  = new URL("https://" + Address + ":" + Port + "/vdi"+"/user"+"/vd/" + uniqueKey); 
                    conn2 = (HttpsURLConnection) url2.openConnection();                                    //建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線                                            
                    conn2.setDoOutput(true);
                    conn2.setDoInput(true);
                    conn2.setRequestMethod("POST");                                                      // 利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種            
                    conn2.setRequestProperty("Accept"        , "application/json");                      // 設置接收數據格式
                    conn2.setRequestProperty("Content-Type"  , "application/json");                      // 設置發送數據的格式("Content-Type", "application/json; charset=UTF-8")            
                    conn2.setRequestProperty("Content-length", String.valueOf(query.getBytes().length)); // ("Content-length", String.valueOf(query.length()))
                    conn2.setRequestProperty("User-Agent"    , "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
                    conn2.setRequestProperty("Connection"    , "Keep-Alive");                            // 維持長連接
                    conn2.setUseCaches (false);
                    conn2.setInstanceFollowRedirects(true);            
                    conn2.setConnectTimeout(20000);                                                      // (5秒改20秒，不然在getResponseCode容易失敗) // 5 -> 10 -> 20
                    conn2.setReadTimeout(10000);                                                         // (2秒改10秒，不然在getResponseCode容易失敗) // 2 -> 10         
                             
                    //寫入資料     

                    try (OutputStream out = conn2.getOutputStream()) {
                        out.write(query.getBytes());
                        out.flush();
                        out.close();            
                    } catch (Exception ex) { 
                    }           
                
                    /*
                    System.out.println("URL                    : " + url2 + "\n"); 
                    System.out.println("conn.getOutputStream() : " + conn2.getOutputStream() + "\n");
                    System.out.println("Resp Code              : " + conn2.getResponseCode() + "\n");
                    System.out.println("Resp Message           : " + conn2.getResponseMessage() + "\n");
                    System.out.println("-----------------------------------------\n");  
                    */
                    
                    //讀資料
                    InputStream is = conn2.getInputStream();
                    StringBuilder sb;
                    String line;                      
                    
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {              
                        sb = new StringBuilder("");
                        while ((line = br.readLine()) != null) {
                            sb.append(line);         
                        }                 
                        
                        br.close();
                    }                    
                    
                    String data = sb.toString();             
                    JsonParser jsonParser = new JsonParser();
                    JsonObject returndata = (JsonObject)jsonParser.parse(data).getAsJsonObject();
                    jsondata = returndata.getAsJsonObject().getAsJsonArray("UserVdImages");
                    
                    // 2019.01.02 view D Drive
                    VD[] _array = new VD[jsondata.size()];
                    int arraySize = 0;
                    
                    if(jsondata != null && jsondata.size() > 0 ) { // 2018.07.06 快照新增還原按鈕                                                               
                        // 顯示可選擇之VD
                        for (int i = 0; i < jsondata.size(); i++) {
                            vd_term           = jsondata.get(i).getAsJsonObject();            
                            vd_Name           = vd_term.get("VdName").getAsString();
                            VdId              = vd_term.get("VdId").getAsString();
                            // 判斷是否有檢視中的layer和是否為雲主機
                            Vd_IsSnapshotView = vd_term.get("IsSnapshotView").getAsBoolean();
                            Vd_IsOrg          = vd_term.get("IsVdOrg").getAsBoolean();       


                            // 2019.01.02 view D Drive
                            if(VdId.equals(getVDID())) {// 2018.07.06 快照新增還原按鈕
                                getIndex = i;
                            } 
                            /*
                            vdlist_array[i][0] = vd_Name;
                            vdlist_array[i][1] = VdId;                       
                            vdlist_array2[i][0] = Vd_IsSnapshotView;
                            vdlist_array2[i][1] = Vd_IsOrg;   
                            */
                            if(Vd_IsOrg) {
                                _array[i] = new VD(vd_Name, VdId, Vd_IsSnapshotView);                        
                                arraySize++;
                            }
                                

                        }     
                        
                      
                        // 2019.01.02 view D Drive
                        /*
                        vd_data.clear(); // 清空data先前的內容

                        for (int i = 0; i < jsondata.size(); i++) {
                            if(vdlist_array2[i][1]) {
                                vd_data.add(new VD(vdlist_array[i][0], vdlist_array[i][1], vdlist_array2[i][0]));
                            }            
                        }                    
                        */
                        
                        // 2019.01.02 view D Drive
                        /*
                        // 2018.01.26 william 檢查VD List是否為空(判斷是否為雲主機)
                        if(vd_data.isEmpty()) {                               
                            // vd_data.add(new VD("", "", false));
                        }                       
                        */
                    }         
                    
                        // 2019.01.02 view D Drive
                        VD[] _arrayshow;
                        if(arraySize == 0)
                            _arrayshow = new VD[1];
                        else
                            _arrayshow = new VD[arraySize];

                        for(int j = 0; j < jsondata.size(); j++) {
                            if(_array[j] != null) {_arrayshow[j] = _array[j];}
                        }                       
                    
                    if(!equals(_checkVDarray, _arrayshow)) {
                        vd_data.removeAll(vd_data);
                        vd_data.addAll(_arrayshow); 
                        _checkVDarray = _arrayshow;     
                        vdtable.refresh();    
                        // 2018.07.06 快照新增還原按鈕
                        vdtable.getSelectionModel().select(getIndex);
                        vdtable.getFocusModel().focus(getIndex);                        
                    }
                    

                    
                    conn2.disconnect();
                } catch (Exception ex) {                
                }                
//            }
//        
//        };
//        
//        fresh_vdlist_timer.schedule(vdtask, 1, 10000);
    }
    
    // 2019.01.02 view D Drive
    private boolean equals(VD[] a, VD[] b) {
        if(a.length != b.length)
            return false;
        for (int i = 0; i < a.length; i++) {
            if(a[i].VD_IsSnapshotView.getValue() != b[i].VD_IsSnapshotView.getValue())
                return false;
        }
        return true;
    }      


      /******bypass SSL 網頁認證問題,系統SSL之Certification key非信任組織發行*************/
    private void bypassSSL() throws SocketException, UnknownHostException, IOException{
       
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
            @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {  }
            @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {  }
        }};
         
        try { 
            
         SSLContext sc = SSLContext.getInstance("SSL");
         sc.init(null, trustAllCerts, new java.security.SecureRandom());
         HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
         
        } catch (KeyManagementException | NoSuchAlgorithmException e){
        }

         // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }    
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        
    }            
    /*------------------------ 檢查登入是否完成，並關閉登入畫面 ------------------------*/
    public void CheckSSLogincompleted() {
        final LongProperty lastUpdate = new SimpleLongProperty();                            
        final long minUpdateInterval = 500000000 ; // nanoseconds. Set to higher number to slow output.

        login_timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate.get() > minUpdateInterval) {
                    if (!login_timerflag) { 
                        login_timer.stop();
                        Login_Unlock();  
                    }          
                    lastUpdate.set(now);
                }
            }
        };
        login_timer.start();  
    }     
    /*------------------------ thread ------------------------*/
    public class Login_lock_thr implements Runnable {

        @Override
        public void run() {
            try {
                Login_SnapshotLayer();
            } catch (IOException ex) {
                Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }      
    /*------------------------ 將Spice需要的資料 寫入txt檔 ------------------------*/
    public void WriteSpiceText(String address, String port, String Uname, String Password) {
        SpiceAddress    = address;
        SpicePort       = port;
        CurrentUserName = Uname;    // load CurrentUserName
        CurrentPasseard = Password; // load CurrentPasseard
        try{
            File myFile = new File("jsonfile/SpiceText.txt");
            
            if(myFile.exists()){
                myFile.delete();
                myFile = null;
            }
            
            FileWriter SWriter = new FileWriter("jsonfile/SpiceText.txt",true);
            try (BufferedWriter bufferedSWriter = new BufferedWriter(SWriter)) {
                bufferedSWriter.write("[virt-viewer]");
                bufferedSWriter.newLine();
                bufferedSWriter.write("type = spice");
                bufferedSWriter.newLine();
                bufferedSWriter.write("host= ");
                bufferedSWriter.write(SpiceAddress);               
                bufferedSWriter.newLine();
                bufferedSWriter.write("port = ");
                bufferedSWriter.write(SpicePort);
                bufferedSWriter.newLine();              
                bufferedSWriter.write("username = ");
                bufferedSWriter.write(CurrentUserName);
                bufferedSWriter.newLine();
                bufferedSWriter.write("password = ");
                bufferedSWriter.write(CurrentPasseard);
                bufferedSWriter.newLine();
                bufferedSWriter.write("fullscreen = 1");//1=大視窗 0=小視窗
                bufferedSWriter.newLine();
                bufferedSWriter.write("delete-this-file = 1");
                bufferedSWriter.newLine();
                bufferedSWriter.write("title =  ");
                bufferedSWriter.write(CurrentUserName);
                bufferedSWriter.write("-");
                bufferedSWriter.write(SpiceAddress);
                bufferedSWriter.newLine();           
                //  2018.11.06 william 多執行緒Ping IP
                bufferedSWriter.flush();
                bufferedSWriter.close();                   
            }            
        } catch(IOException e) {
            
        }
    }   
    /*------------------------ 動作功能 ------------------------*/ 
    public final void detectSelection() {        
        /*
        vdtable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() == 1){
                    vd_index = vdtable.getSelectionModel().getSelectedIndex();
                    
                    if(vd_index != -1) { // 2017.12.29 點空白出會出現error 
//                    Login_lock();
                        fresh_sslist_timer.cancel();
                        // 2019.01.02 view D Drive
                        setBeforevid(getVDID());
                        
                        Select_vdID = (String) vdtable.getSelectionModel().getSelectedItem().getVD_ID(); 
                        System.out.println("click on " + Select_vdID + "\n");
                        setVDID(Select_vdID);
                        ssdata_refresh();
//                    Login_Unlock();
                    }
                }
            }
        });                     
        */
        vdtable.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){ 
                vd_index = vdtable.getSelectionModel().getSelectedIndex();
                fresh_sslist_timer.cancel();
                Select_vdID = (String) vdtable.getSelectionModel().getSelectedItem().getVD_ID();
                System.out.println("click on " + Select_vdID + "\n");
                setVDID(Select_vdID);
                ssdata_refresh();
//                Login_Unlock();
           }           
           if(event.getCode() == KeyCode.RIGHT){
                sstable.requestFocus();
                sstable.getSelectionModel().select(0);
                sstable.getFocusModel().focus(0);
                event.consume();
           }
           if(event.getCode() == KeyCode.ESCAPE){ 
               StopAll(); // 2019.01.02 view D Drive   
                event.consume();
           }           
        });         

        sstable.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.DOWN&&sstable.getSelectionModel().isSelected(sslist_jsonArr.size()-1)){
                Btn_login.requestFocus();
                event.consume();
           }
           if(event.getCode() == KeyCode.ESCAPE){ 
               StopAll(); // 2019.01.02 view D Drive   
                event.consume();
           }           
        });           
        
        
        Btn_exit.setOnAction((ActionEvent event) -> {
            StopAll(); // 2019.01.02 view D Drive   
        });            
    }        
    /*------------------------ 取值＆資料模型 ------------------------*/
    // 定義VD資料模型
    public class VD {         
        private final SimpleStringProperty VD_Name;
        private final SimpleStringProperty VD_ID;
        private final SimpleBooleanProperty VD_IsSnapshotView;
        
        VD(String vd_Name, String vd_ID, boolean vd_IsSnapshotView) {
            this.VD_Name           = new SimpleStringProperty(vd_Name);
            this.VD_ID             = new SimpleStringProperty(vd_ID);
            this.VD_IsSnapshotView = new SimpleBooleanProperty(vd_IsSnapshotView);            
        } 
        // vd name
        public String getVD_Name() {
            return VD_Name.get();
        }

        public void setVD_Name(String vd_Name) {
            VD_Name.set(vd_Name);
        }

        public StringProperty VD_NameProperty() {
            return VD_Name;
        }
        
        // vd id
        public String getVD_ID() {
            return VD_ID.get();
        }

        public void setVD_ID(String vd_ID) {
            VD_ID.set(vd_ID);
        }

        public StringProperty VD_IDProperty() {
            return VD_ID;
        }        
        // vd IsSnapshotView
        public boolean getVD_IsSnapshotView() {
            return VD_IsSnapshotView.get();
        }     
        
        public void setVD_IsSnapshotView(boolean vd_IsSnapshotView) {
            VD_IsSnapshotView.set(vd_IsSnapshotView);
        }    
        
        public BooleanProperty VD_IsSnapshotViewProperty() {
            return VD_IsSnapshotView;
        }         
        
    }         
    // 定義快照資料模型
    public class SS {         
        private final SimpleStringProperty Layer;
        private final SimpleStringProperty State;
        private final SimpleStringProperty CreateTime;
        private final SimpleStringProperty Size;
        private final SimpleStringProperty Desc;


        SS(String l, String state, String ct, String size, String desc) {
            this.Layer      = new SimpleStringProperty(l);
            this.State      = new SimpleStringProperty(state);
            this.CreateTime = new SimpleStringProperty(ct);
            this.Size       = new SimpleStringProperty(size);
            this.Desc       = new SimpleStringProperty(desc);
        } 
        // Layer
        public String getLayer() {
            return Layer.get();
        }

        public void setLayer(String l) {
            Layer.set(l);
        }

        public StringProperty LayerProperty() {
            return Layer;
        }        
        // State
        public String getState() {
            return State.get();
        }

        public void setState(String state) {
            State.set(state);
        }

        public StringProperty StateProperty() {
            return State;
        }
        // CreateTime
        public String getCreateTime() {
            return CreateTime.get();
        }

        public void setCreateTime(String ct) {
            CreateTime.set(ct);
        }

        public StringProperty CreateTimeProperty() {
            return CreateTime;
        }
        // Size
        public String getSize() {
            return Size.get();
        }

        public void setSize(String size) {
            Size.set(size);
        }

        public StringProperty SizeProperty() {
            return Size;
        }
        // Desc
        public String getDesc() {
            return Desc.get();
        }

        public void setDesc(String desc) {
            Desc.set(desc);
        }

        public StringProperty DescProperty() {
            return Desc;
        }
    }      
    
    public static synchronized String getViewLayerID() {
        return ViewLayer;
    }
    
    public static synchronized void setViewLayerID(String layer) {
        ViewLayer = layer;
    }
    
    //  2018.07.06 快照新增還原按鈕
    public static synchronized String getRollBackLayerID() {
        return RollBackLayer;
    }
    
    public static synchronized void setRollBackLayerID(String layer) {
        RollBackLayer = layer;
    }      

    public static synchronized String getVDID() {
        return Vd_Id;
    }

    public static synchronized void setVDID(String vdid) {
        Vd_Id = vdid;
    }    
    
    // 2019.01.02 view D Drive
    public static synchronized String getBeforevid() {
        return bId;
    }

    public static synchronized void setBeforevid(String vdid) {
        bId = vdid;
    }      
    
    
    /*------------------------ HTTPS Status Code Error ------------------------*/    
    public void Alert_HttpsStatusError(int conn){
        if(conn != 200) {           
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
            alert.setTitle(WordMap.get("Message_Login"));
            alert.setHeaderText(null);
                          
            alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
            alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE); 

            alert.setContentText(
                WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Unknown_system_error")+" ( "+conn+" ) " 
                + "\n"
                + "\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_400")
            );
            
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(buttonTypeOK); 
            
            
            final Optional<ButtonType> opt = alert.showAndWait();
            final ButtonType rtn = opt.get();                     // 可以直接用「alert.getResult()」來取代
        if (rtn == buttonTypeOK) {                            // 若使用者按下「確定」
            login_timerflag = false;
            Login_Unlock();
        }            
            
        }
    }    
    
     //  2018.01.12 william 例外處理
    public void NoSSfunc() {
           
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
        alert.setTitle(WordMap.get("Alert_Title_Warning"));          // 設定對話框視窗的標題列文字
        alert.setHeaderText("");                                     // 設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭

        
        if(WordMap.get("SelectedLanguage").equals("English") || WordMap.get("SelectedLanguage").equals("Japanese")) {
            alert.setContentText("Snapshot function is abnormal. Please contact the information staff.");   // 設定對話框的訊息文字
        } else if (WordMap.get("SelectedLanguage").equals("TraditionalChinese")) {
            alert.setContentText("快照功能異常，請洽資訊人員！");   
        } else if (WordMap.get("SelectedLanguage").equals("SimpleChinese")) {
            alert.setContentText("快照功能异常，请洽资讯人员！");   
        }   
        
        // 建立ButtonType物件
        ButtonType buttonTypeOK     = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            
        alert.getButtonTypes().setAll(buttonTypeOK); 
            
        // 建立Button物件並設定按鍵邏輯
        Button button_ok     = (Button) alert.getDialogPane().lookupButton(buttonTypeOK);

        button_ok.setOnMouseEntered((event)-> {
            button_ok.requestFocus();      
        });

        button_ok.setOnKeyPressed((event)-> {           
            if(event.getCode() == KeyCode.ENTER){
                button_ok.fire();              
            }        
        });

        // 警告視窗Style設定
//        DialogPane dialogPane = alert.getDialogPane();
//        dialogPane.getStylesheets().add("myDialogs.css");
//        dialogPane.getStyleClass().add("myDialog");
//        // 若加上下面2行，會繼承風格
//        alert.initModality(Modality.APPLICATION_MODAL);
//        alert.initOwner(public_stage.getScene().getWindow()); // 有沒有這一行很重要，決定這兩個視窗是否有從屬關係
            
        final Optional<ButtonType> opt = alert.showAndWait();
        final ButtonType rtn = opt.get();                     // 可以直接用「alert.getResult()」來取代           
        if (rtn == buttonTypeOK) {                            // 若使用者按下「確定」
            StopAll(); // 2019.01.02 view D Drive   
        }          
    }        
    
     public int SS_QueryLogin() throws MalformedURLException, IOException {
            url  = new URL("https://" + Address + ":" + Port + "/vdi/snapshot/login/" + getViewLayerID());   
            HttpURLConnection conn_setVDonline = (HttpURLConnection) url.openConnection();                     // 建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線
            conn_setVDonline.setRequestMethod("PUT");                                                          // 利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
            conn_setVDonline.setDoOutput(true);
            conn_setVDonline.setDoInput(true);
            conn_setVDonline.setRequestProperty("Accept"        , "application/json");
            conn_setVDonline.setRequestProperty("User-Agent"    , "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn_setVDonline.setRequestProperty("Content-Type"  , "application/json");                         // 設置發送數據的格式("Content-Type", "application/json; charset=UTF-8")
            conn_setVDonline.setRequestProperty("Connection"    , "Keep-Alive");                               // 維持長連接

            conn_setVDonline.setUseCaches (false);
            conn_setVDonline.setInstanceFollowRedirects(true);
            
            conn_setVDonline.setConnectTimeout(10000); 
            try (OutputStream out2 = conn_setVDonline.getOutputStream()) {                                
                out2.flush();
                out2.close();     
                conn_setVDonline.getResponseCode(); //  2018.01.12 william 例外處理                
                conn_setVDonline.disconnect();                  
            }                
            return conn_setVDonline.getResponseCode();
     }    
    
    /*------------------------ End ------------------------*/    
     
     // 2019.01.02 view D Drive
    /*------------------------ View User Disk ------------------------*/     
    /*------------------------ 畫面layout ------------------------*/ 
    public final void LeftView(JsonArray jsondata, String _diskname) {
        String label_vdTitleString = "" ;
        TableColumn vdSNCol = new TableColumn(WordMap.get("VD_SN"));    
        TableColumn<VD, String> vdNameCol = new TableColumn(WordMap.get("VD_Name"));      
        udvd_data.clear(); // 清空data先前的內容        
        
        // 2019.01.30 UserDisk快照顯示異常問題修正
        if(WordMap.get("SelectedLanguage").equals("English")) {
            label_vdTitleString = "Select a VD";
        } else if(WordMap.get("SelectedLanguage").equals("TraditionalChinese")) {
            label_vdTitleString = "請選擇 雲桌面";
        } else if(WordMap.get("SelectedLanguage").equals("SimpleChinese")) {
            label_vdTitleString = "请选择 云桌面";
        }  else if(WordMap.get("SelectedLanguage").equals("Japanese")) {
            label_vdTitleString = "VD 選択してください";
        }  
        /***  Title 放入一個HBOX內 ***/
        Label vdTitleLabel = new Label(label_vdTitleString);
        vdTitleLabel.getStyleClass().add("Title_label");
                       
        if((WordMap.get("SelectedLanguage").equals("Japanese"))){
            vdSNCol.setPrefWidth(90);
            vdSNCol.setMinWidth(90);         
        } else {
            vdSNCol.setPrefWidth(60);
            vdSNCol.setMinWidth(60);
            //vdSNCol.setMaxWidth(60);        
        }     
        vdSNCol.setSortable(false);
        vdSNCol.setStyle("-fx-font-family:'Times New Roman';");                  
        vdSNCol.setCellValueFactory(new Callback<CellDataFeatures<VD, VD>, ObservableValue<VD>>() {
             @Override
             public ObservableValue<VD> call(CellDataFeatures<VD, VD> v) {
                return new ReadOnlyObjectWrapper(v.getValue());
            }
        });                
        vdSNCol.setCellFactory(new Callback<TableColumn<VD, VD>, TableCell<VD, VD>>() {
            @Override 
            public TableCell<VD, VD> call(TableColumn<VD, VD> param) {
                return new TableCell<VD, VD>() {
                    @Override 
                    protected void updateItem(VD item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && item != null) {
                            int number = this.getTableRow().getIndex();
                            setText(number + 1 + " ");
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });        

        if((WordMap.get("SelectedLanguage").equals("Japanese"))){
            vdNameCol.setPrefWidth(138);
            vdNameCol.setMinWidth(138);     
        } else {
            vdNameCol.setPrefWidth(168);
            vdNameCol.setMinWidth(168);
        }             

        vdNameCol.setStyle( "-fx-alignment: center-left;");         
        vdNameCol.setSortable(false);        
        vdNameCol.setCellFactory(getCustomVDCellFactory());
        vdNameCol.setCellValueFactory(new PropertyValueFactory<VD, String>("VD_Name"));    
        
        VD[] _array = new VD[1]; // jsondata.size() -> 1
        
        
        boolean IsBind = false;
        
        // 2019.01.30 UserDisk快照顯示異常問題修正
        boolean IsSetDisk = false;           
        
        try {         
            // 顯示可選擇之VD
            for (int i = 0; i < jsondata.size(); i++) {
                JsonObject vd_term  = jsondata.get(i).getAsJsonObject();            
                String vd_Name      = vd_term.get("VdName").getAsString();
                String VdId         = vd_term.get("VdId").getAsString();
                int Vd_Status = vd_term.get("VdStatus").getAsInt();
                
                

                
                
                try {
                    //System.out.println("Bind User Disk : " + vd_term.get("BindUserDisk").getAsBoolean());
                    if(vd_term.get("BindUserDisk").getAsBoolean()) {
                        IsBind = true;
                    } else {
                        IsBind = false;
                    }
                } catch (Exception e){
                    IsBind = false;
                }

                if(IsBind) {
                    // 2019.01.30 UserDisk快照顯示異常問題修正
                    GB._viewDiskVDName = _vdName = vd_Name;
                    IsSetDisk = true;
                    setUserDiskVDID(VdId);  
                    switch(Vd_Status) {
                        case 5:
                            IsUDBtnDisable = false;
                            _array[0] = new VD(vd_Name, VdId, false);                
                            break;
                        case -2:
                            IsUDBtnDisable = true;
                            _array[0] = new VD(vd_Name, VdId, true);                
                            break;    
                        case 1:
                            IsUDBtnDisable = true;
                            _array[0] = new VD(vd_Name, VdId, true);                
                            break; 
                        case 4:
                            IsUDBtnDisable = true;
                            _array[0] = new VD(vd_Name, VdId, true);                
                            break;                             
                        default:    
                            IsUDBtnDisable = false;
                            _array[0] = new VD(vd_Name, VdId, false);                
                            break;                    
                    }

                }                       
            }                   

            udvd_data.addAll(_array);
            _checkUDVDarray = _array;
            
        } catch (Exception ex) {
            NoUserDiskfunc_flag = true; 
            udvd_data.add(new VD("Null","Null", false));
            NoUserDiskfunc();
        }        
        
        // 2019.01.30 UserDisk快照顯示異常問題修正
        if(IsSetDisk) {
           setDiskName(_diskname);
        } else {
            setDiskName("");
        }           
            
        udvdtable.setEditable(false);       
        udvdtable.getColumns().addAll(vdSNCol, vdNameCol);
        udvdtable.setItems(udvd_data);     
        udvdtable.getStyleClass().add("table_header");
        udvdtable.setPrefSize(vd_listView_width, vd_listView_height);
        udvdtable.setMaxSize(vd_listView_width , vd_listView_height);
        udvdtable.setMinSize(vd_listView_width , vd_listView_height);         
        udvdtable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY); // 自動重新整理大小,table的colum大小隨table的大小變化而變化

        // 設定第一個元素為被選擇狀態
        udvdtable.requestFocus();
        udvdtable.getSelectionModel().select(0);
        udvdtable.getFocusModel().focus(0);
        udvd_index = udvdtable.getSelectionModel().getSelectedIndex();
        System.out.println("get vd index: " + udvd_index);
        
        HBox WC1 = new HBox(vdTitleLabel);
        WC1.setAlignment(Pos.TOP_CENTER);
        WC1.setPadding(new Insets(0, 0, 0, 0));
        WC1.setTranslateY(0);
        WC1.setTranslateX(0);
                         
        HBox vd_selection = new HBox();
	vd_selection.setSpacing(20);       
        vd_selection.setAlignment(Pos.CENTER);
        vd_selection.setPadding(new Insets(0, 0, 0, 0));  
	vd_selection.getChildren().addAll(udvdtable);
                
        UDLeftGP.add(WC1,0,0);
        UDLeftGP.add(vd_selection,0,1);          
    }

    public final void CenterView() {   
        /***  Title 放入一個HBOX內 ***/
        Label UserDiskListTitleLabel = new Label(WordMap.get("Select_Snapshot_Layer"));
        UserDiskListTitleLabel.getStyleClass().add("Title_label");
        
        HBox WC2 = new HBox(UserDiskListTitleLabel);
        WC2.setAlignment(Pos.TOP_CENTER);
        WC2.setPadding(new Insets(0, 0, 0, 12));
        WC2.setTranslateY(0);
        
        UDMainGP.add(WC2,0,0);           
        
        // 產生畫面列表
        try {
            ListUserDiskofVD();
        } catch (IOException ex) { Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex); } catch (InterruptedException ex) { Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex); }        
    }        
       
    // 讀取資料
    public void ListUserDiskofVD() throws MalformedURLException, IOException, InterruptedException{
        try{             
            URL url  = new URL("https://" + Address + ":" + Port + "/vdi/userdisk/" + getDiskName() + "/snapshot");          
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");                        
	    conn.setRequestProperty("Accept"    , "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Connection", "Keep-Alive"); 
            conn.setConnectTimeout(20000);                       
            conn.setReadTimeout(10000);                          
            conn.connect();

            System.out.println("List Disk Snapshot of VD-URL         : " + url + "\n"); 
            System.out.println("List Disk Snapshot of VD-Resp Code   : " + conn.getResponseCode() + "\n"); 
            System.out.println("List Disk Snapshot of VD-Resp Message: " + conn.getResponseMessage() + "\n");
            
            // 讀取資料
            InputStream is = conn.getInputStream();
            StringBuilder sb;
            String line;  
            
            boolean json_length_empty = false;
            
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {              
                 sb = new StringBuilder("");
                 while ((line = br.readLine()) != null) {            
                     sb.append(line);         
                 }             
                 System.out.println("List Snapshot of VD-return sb: "+sb.toString()+"\n");  
                 if(sb.toString().equals("[]")) {
                    json_length_empty = true;
                 } else {
                    json_length_empty = false;
                 }
                 br.close();
            }
             
            String sslist_data = sb.toString();   
            JsonParser data_jp = new JsonParser();
            JsonArray list_jsonArr = (JsonArray) data_jp.parse(sslist_data);
            
            CreateSnapshotList(list_jsonArr, json_length_empty);
            
            conn.disconnect();
        } catch (MalformedURLException e) {
           System.out.println("MalformedURLException: " + e + "\n");             
	} catch (IOException e) {    
           System.out.println("IOException: " + e + "\n");
            JsonArray list_jsonArr = null;

           CreateSnapshotList(list_jsonArr, false);
        } 

        
    }    
    // 產生列表
    public void CreateSnapshotList(JsonArray jsondata, boolean json_length_empty) {
        TableColumn UDSNCol;
        TableColumn<UserDisk, String> CreateTimeCol;
        TableColumn<UserDisk, String> SizeCol;
        TableColumn<UserDisk, String> DescCol;
       
        UserDisk[] _array = null;
        JsonObject term;
        String State;
        String CreateTime; 
        String Size;
        String Desc;
        String Layer;             
        userdisk_data.clear(); // 清空data先前的內容

        UDSNCol   = new TableColumn(WordMap.get("VD_SN"));
        if((WordMap.get("SelectedLanguage").equals("Japanese"))){
            UDSNCol.setPrefWidth(90);
            UDSNCol.setMinWidth(90);
        } else {
            UDSNCol.setPrefWidth(60);
            UDSNCol.setMinWidth(60);
            //UDSNCol.setMaxWidth(60);    
        }  
        UDSNCol.setSortable(false);
        UDSNCol.setStyle("-fx-font-family:'Times New Roman';");    
        UDSNCol.setCellFactory(new Callback<TableColumn<UserDisk, UserDisk>, TableCell<UserDisk, UserDisk>>() {
            @Override 
            public TableCell<UserDisk, UserDisk> call(TableColumn<UserDisk, UserDisk> param) {
                return new TableCell<UserDisk, UserDisk>() {
                    @Override 
                    protected void updateItem(UserDisk item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && item != null) {
                            int number = this.getTableRow().getIndex();
                            setText(" " + (vd_index + 1) + "-" + (number + 1) + " ");
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });        
        
        UDStateCol  = new TableColumn(WordMap.get("Snapshot_State"));
        UDStateCol.setPrefWidth(100);
        UDStateCol.setMinWidth(100);
        UDStateCol.setCellFactory(UDgetCustomCellFactory());
        
        CreateTimeCol = new TableColumn(WordMap.get("Time"));
        CreateTimeCol.setPrefWidth(190);
        CreateTimeCol.setMinWidth(190);
        //CreateTimeCol.setMaxWidth(190);            
        
        SizeCol   = new TableColumn(WordMap.get("Snapshot_Size"));     
        SizeCol.setPrefWidth(100);
        SizeCol.setMinWidth(100);
        //SizeCol.setMaxWidth(100);
        SizeCol.setStyle( "-fx-alignment: center-right;"); 
        
        DescCol   = new TableColumn(WordMap.get("Snapshot_Description"));     
        DescCol.setPrefWidth(180);
        DescCol.setMinWidth(180);
        DescCol.setStyle( "-fx-alignment: center-left;"); 
        
        UDActionCol = new TableColumn(WordMap.get("Action"));    // 2018.07.06 快照新增還原按鈕
        if((WordMap.get("SelectedLanguage").equals("Japanese"))){
            UDActionCol.setPrefWidth(230);
            UDActionCol.setMinWidth(230);              
        } else {
            UDActionCol.setPrefWidth(160);
            UDActionCol.setMinWidth(160);
            //UDActionCol.setMaxWidth(160); 
        }          
        // 設定每一個欄位對應的JavaBean物件與Property名稱    
        UDSNCol.setCellValueFactory(new Callback<CellDataFeatures<UserDisk, UserDisk>, ObservableValue<UserDisk>>() {
             @Override
             public ObservableValue<UserDisk> call(CellDataFeatures<UserDisk, UserDisk> s) {
                return new ReadOnlyObjectWrapper(s.getValue());
            }
        });                        
        UDStateCol.setCellValueFactory(new PropertyValueFactory<>("State"));                           
        CreateTimeCol.setCellValueFactory(new PropertyValueFactory<>("CreateTime"));
        SizeCol.setCellValueFactory(new PropertyValueFactory<>("Size"));
        DescCol.setCellValueFactory(new PropertyValueFactory<>("Desc"));
        UDActionCol.setCellValueFactory(new PropertyValueFactory<>("Action"));   
                    
        if(jsondata != null) {
            if(jsondata.size() == 0)
                _array = new UserDisk[1];            
            else
                _array = new UserDisk[jsondata.size()];        
        } else {
            _array = new UserDisk[1];        
        }
                    
        getlist_jsonArr = jsondata;
        
        IsUDViewDisable = true; // 2019.01.10 User Disk bug fix
        
        // 判斷傳入DATA的json是否為空
        if(jsondata != null && json_length_empty == false) {                        
            for (int i = 0; i < jsondata.size(); i++) {
                term       = jsondata.get(i).getAsJsonObject();  
                Layer      = term.get("Layer").getAsString();                
                State      = term.get("State").getAsString();                
                if (State.equals("2")) { setViewLayerID(Layer); IsUDViewDisable = false; }  // 2019.01.10 User Disk bug fix               
                State      = UD_StatusName(term.get("State").getAsString(), UDStateCol);                
                CreateTime = term.get("CreateTime").getAsString();                                                         
                Size       = formatFileSize(term.get("Size").getAsLong(),false);                
                Desc       = term.get("Desc").getAsString();      

                _array[i] = new UserDisk(Layer, State, CreateTime, Size, Desc);            
            }          
               
            UDCreateTableBtn(UDActionCol); // 產生列表按鈕               
        } else {
            _array[0] = new UserDisk("", "", "", "", "");
            UDBtn_stopview.setDisable(true);
        }
        
        UDBtn_login.setDisable(IsUDViewDisable); // 2019.01.10 User Disk bug fix               
        
        userdisk_data.addAll(_array);
        _checkUserDiskarray = _array;
        
        data_refresh();

        userdisktable.setEditable(true);       
        userdisktable.getColumns().addAll(UDSNCol, UDActionCol, UDStateCol, DescCol, CreateTimeCol, SizeCol);
        userdisktable.setItems(userdisk_data);  
        userdisktable.getStyleClass().add("table_header");
        userdisktable.setPrefSize(ud_listView_width, ud_listView_height);
        userdisktable.setMaxSize(ud_listView_width , ud_listView_height);
        userdisktable.setMinSize(ud_listView_width , ud_listView_height); 
        userdisktable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY); // 自動重新整理大小,table的colum大小隨table的大小變化而變化

        HBox ud_selection = new HBox();
	ud_selection.setSpacing(20);       
        ud_selection.setAlignment(Pos.CENTER);
        ud_selection.setPadding(new Insets(0, 0, 0, 0));  
	ud_selection.getChildren().addAll(userdisktable);   // 使用Table Column顯示     

        UDMainGP.add(ud_selection,0,1);         
    }   
    // 產生列表按鈕
    public void UDCreateTableBtn (TableColumn<UserDisk, String> ActionCol) {
        Callback<TableColumn<UserDisk, String>, TableCell<UserDisk, String>> cellFactory = new Callback<TableColumn<UserDisk, String>, TableCell<UserDisk, String>>() {
            @Override
            public TableCell call(final TableColumn<UserDisk, String> param) {
                final TableCell<UserDisk, String> cell = new TableCell<UserDisk, String>() {

                    final Button btn = new Button(WordMap.get("Snapshot_View"));
                    final Button btn2 = new Button(WordMap.get("Snapshot_Rollback")); // 2018.07.06 快照新增還原按鈕

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.getStyleClass().add("table-btn");
                            btn.setOnAction(event -> {
                                UserDisk s = getTableView().getItems().get(getIndex());

//                                String l = s.getLayer();                                
//                                IsKeepNIC_func(l);
                                
                                try {
                                    setViewLayerID(s.getLayer());
                                    ViewUserDiskLayer();
                                } catch (Exception e) {}  
                                
                            });
                            
                            btn2.getStyleClass().add("table-btn");
                            btn2.setOnAction(event -> {
                                UserDisk s = getTableView().getItems().get(getIndex());
                                setRollBackLayerID(s.getLayer());
                                
                                try {
                                    // 2019.01.10 User Disk bug fix
                                    IsUDRollBackAlert();
                                } catch (IOException ex) {
                                    Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            });             
                            
                            btn.setDisable(IsUDBtnDisable);
                            
                            if(IsUDViewDisable && !IsUDBtnDisable)
                                btn2.setDisable(false);
                            else
                                btn2.setDisable(true);                            


                            //setGraphic(btn);
                            HBox pane = new HBox(btn, btn2);
                            switch (WordMap.get("SelectedLanguage")) {
                                case "English":
                                    pane.setPadding(new Insets(3, 5, 3, 15));
                                    break;
                                default:
                                    pane.setPadding(new Insets(3, 5, 3, 30));
                                    break;
                            }                                                          
                            pane.setSpacing(10);                            
                            setGraphic(pane); 
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };

        ActionCol.setCellFactory(cellFactory);       
    }    
        
    public final void BottomView() {
        Label _desc1 = new Label(WordMap.get("Desc_ViewUserDisk_operation1"));
        _desc1.setId("List_label");
        
            Label _desc1_1 = new Label(WordMap.get("Desc_ViewUserDisk_operation1-1"));
            _desc1_1.setId("List_label");          
        
        Label _desc2 = new Label(WordMap.get("Desc_ViewUserDisk_operation2"));
        _desc2.setId("List_label");
        Label _desc3 = new Label(WordMap.get("Desc_ViewUserDisk_operation3"));
        _desc3.setId("List_label");                
        
                                              
        UDBtn_stopview = new Button("  " + WordMap.get("Snapshot_Stop_View") + "  ");
        if((WordMap.get("SelectedLanguage").equals("Japanese"))){
            UDBtn_stopview.setPrefSize(170, btn_height);
            UDBtn_stopview.setMaxSize(170, btn_height); 
            UDBtn_stopview.setMinSize(170, btn_height);                
        } else {
            UDBtn_stopview.setPrefSize(btn_width, btn_height);
            UDBtn_stopview.setMaxSize(btn_width, btn_height); 
            UDBtn_stopview.setMinSize(btn_width, btn_height); 
        }   
        UDBtn_stopview.setOnAction((ActionEvent event) -> {
            try { StopUserDiskLayer(); } 
            catch (IOException ex) { Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex); } catch (InterruptedException ex) { Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex); }
        }); 
        
        // 建立按鈕物件(離開)
        UDBtn_exit = new Button("  "+WordMap.get("Snapshot_Exit")+"  ");  
        UDBtn_exit.setPrefSize(btn_width, btn_height);
        UDBtn_exit.setMaxSize(btn_width, btn_height); 
        UDBtn_exit.setMinSize(btn_width, btn_height); 

        UDBtn_login = new Button("  " + WordMap.get("Snapshot_Login") + "  ");  
        UDBtn_login.setPrefSize(btn_width, btn_height);
        UDBtn_login.setMaxSize(btn_width , btn_height);
        UDBtn_login.setMinSize(btn_width , btn_height);

        UDBtn_login.setOnAction((ActionEvent event) -> {
            // 2019.01.30 UserDisk快照顯示異常問題修正
            GB.ExitFlag = false;            
            try {
                LoginUserDiskLayer();
            } catch (IOException ex) {
                Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*
            Login_lock();
                    
            Task<Void> loginTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    // UDRespCode = QueryLogin();
                    LoginUserDiskLayer();
                    return null;
                }
            };                    
                    
            loginTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) { UDlogin_timerflag = false; }
            });                    
                
            loginTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {                                
                    if(UDRespCode == 200) {
                        Login_lock_thr llthr2 = new Login_lock_thr();
                        Thread thr = new Thread(llthr2);
                        thr.start();              
                    } else {
                        Alert_HttpsStatusError(UDRespCode);
                    }
                }
            });
            
            new Thread(loginTask).start(); 
            */
        });                
        
        VBox Desc = new VBox();
	Desc.setSpacing(10);
        if(WordMap.get("SelectedLanguage").equals("English")){ 
            Desc.setTranslateY(10);
            Desc.setTranslateX(15);          
        }  else if(WordMap.get("SelectedLanguage").equals("Japanese")) {
            Desc.setTranslateY(10);
            Desc.setTranslateX(27);          
        }
        else { //else if((WordMap.get("SelectedLanguage").equals("TraditionalChinese")) || (WordMap.get("SelectedLanguage").equals("SimpleChinese"))){
            Desc.setTranslateY(-5);
            Desc.setTranslateX(-72);        
        }
        Desc.setAlignment(Pos.CENTER_LEFT);
        Desc.setPadding(new Insets(0, 0, 0, 0));        
        if(WordMap.get("SelectedLanguage").equals("English") || WordMap.get("SelectedLanguage").equals("Japanese")){ 
            Desc.getChildren().addAll(_desc1, _desc1_1, _desc2, _desc3);    
        } 
        else { //else if((WordMap.get("SelectedLanguage").equals("TraditionalChinese")) || (WordMap.get("SelectedLanguage").equals("SimpleChinese"))){
            Desc.getChildren().addAll(_desc1, _desc2, _desc3);    
        }  
        
        HBox btn = new HBox();
	btn.setSpacing(15);
        btn.setAlignment(Pos.CENTER_RIGHT);
        btn.setPadding(new Insets(0, 0, 0, 0));  
        if(WordMap.get("SelectedLanguage").equals("English")){ 
            btn.setTranslateY(5);
            btn.setTranslateX(80);         
        } else if(WordMap.get("SelectedLanguage").equals("Japanese")) {
            btn.setTranslateY(15);
            btn.setTranslateX(67);           
        }    
        else { //else if((WordMap.get("SelectedLanguage").equals("TraditionalChinese")) || (WordMap.get("SelectedLanguage").equals("SimpleChinese"))){
            btn.setTranslateY(-5);
            btn.setTranslateX(163);    
        }        

        btn.getChildren().addAll(UDBtn_exit, UDBtn_stopview, UDBtn_login);   
        
        UDBottomGP.add(Desc,0,0);		
        UDBottomGP.add(btn,0,1);         
    }  
            
    /*------------------------ Polling 取值 ------------------------*/
    public void data_refresh() {  
        fresh_timer = new Timer(); 

        TimerTask sstask = new TimerTask() {    
            @Override
            public void run() {
                UserDiskvddata_refresh();
                try{             
                    URL url  = new URL("https://" + Address + ":" + Port + "/vdi/userdisk/" + getDiskName() + "/snapshot");          
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");                        
                    conn.setRequestProperty("Accept"    , "application/json");
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
                    conn.setRequestProperty("Connection", "Keep-Alive"); 
                    conn.setConnectTimeout(20000);                       
                    conn.setReadTimeout(10000);                          
                    conn.connect();

                    if(conn.getResponseCode() == 400) {
                        StopAll();
                        System.out.println("VD is gone");
                    } else if (conn.getResponseCode() == 404) {
                        System.out.println("Snapshot list is empty");
                    } else if (conn.getResponseCode() == 200) {
//                        System.out.println("Get list");
                    }

                    // 讀取資料
                    InputStream is = conn.getInputStream();
                    StringBuilder sb;
                    String line;  

                    boolean json_length_empty = false;
                    
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {              
                         sb = new StringBuilder("");
                         while ((line = br.readLine()) != null) {            
                             sb.append(line);         
                         }          
                         if(sb.toString().equals("[]")) {
                            json_length_empty = true; 
                         } else {
                            json_length_empty = false; 
                         }                 
                         br.close();
                    }

                    String sslist_data = sb.toString();   
                    JsonParser data_jp = new JsonParser();
        //            JsonObject data_jo = (JsonObject)data_jp.parse(sslist_data);

                    JsonArray list_jsonArr = (JsonArray) data_jp.parse(sslist_data);

                    getlist_jsonArr = list_jsonArr;
                    int BtnDisable_flag = 0;
                    IsUDViewDisable = true; // 2019.01.10 User Disk bug fix

                    UserDisk[] _array = null;
                    JsonObject term;
                    String State;
                    String CreateTime; 
                    String Size;
                    String Desc;
                    String Layer;                

                    if(list_jsonArr != null) {
                        if(list_jsonArr.size() == 0)
                            _array = new UserDisk[1];            
                        else
                            _array = new UserDisk[list_jsonArr.size()];        
                    } else {
                        _array = new UserDisk[1];        
                    }             

                    for (int i = 0; i < list_jsonArr.size(); i++) {
                        term           = list_jsonArr.get(i).getAsJsonObject();  
                        Layer      = term.get("Layer").getAsString();                
                        State      = term.get("State").getAsString();

                        if (State.equals("2")) {
                            setViewLayerID(Layer);
                            BtnDisable_flag ++;
                            IsUDViewDisable = false; // 2019.01.10 User Disk bug fix
                        }         


                        State      = UD_StatusName(term.get("State").getAsString(), StateCol);
                        CreateTime = term.get("CreateTime").getAsString();                                                         
                        Size       = formatFileSize(term.get("Size").getAsLong(),false);                
                        Desc       = term.get("Desc").getAsString();      


                        _array[i] = new UserDisk(Layer, State, CreateTime, Size, Desc);

                    }          



                    if(list_jsonArr.size() > 0) {                    
                        UDCreateTableBtn(UDActionCol);   
                        UDBtn_stopview.setDisable(IsUDBtnDisable); // 2019.01.10 User Disk bug fix
                    } else if (list_jsonArr.size() == 0)
                        UDBtn_stopview.setDisable(true); // 2019.01.10 User Disk bug fix
                    
                    UDBtn_login.setDisable(IsUDViewDisable); // 2019.01.10 User Disk bug fix
        //        sstable.refresh();  
        //        ss_data.removeAll(ss_data);

                    if(json_length_empty == false) {
                        if(list_jsonArr != null && list_jsonArr.size() > 0 ) { // 2018.07.06 快照新增還原按鈕                                        
                            // 2019.01.30 UserDisk快照顯示異常問題修正
                            if(!equals(_checkUserDiskarray, _array)) {
                                userdisk_data.removeAll(userdisk_data);
                                userdisk_data.addAll(_array); 
                                _checkUserDiskarray = _array;
                                userdisktable.refresh();        
                                System.out.println("fresh Snapshot view");
                            }   
                        }
                    } else {
                        _array[0] = new UserDisk("", "", "", "", "");
                        //userdisk_data.clear(); // 清空data先前的內容 // 2019.01.10 User Disk bug fix
                         userdisk_data.removeAll(userdisk_data); // 2019.01.10 User Disk bug fix
                        //ss_data.add(new SS("", "", "", "", ""));
                        userdisk_data.addAll(_array);
                        _checkUserDiskarray = _array;
                        
                        userdisktable.refresh(); // 2019.01.10 User Disk bug fix
        //                System.out.println("Snapshot empty");
                    }


                    conn.disconnect();
                    
                    
                } catch (MalformedURLException e) {} catch (IOException e) {} 

            }
            
            
            private boolean equals(UserDisk[] a, UserDisk[] b) {
                if(a.length != b.length)
                    return false;
                for (int i = 0; i < a.length; i++) {
                    if(a[i].State.getValue() != b[i].State.getValue())
                        return false;
                }
                return true;
            }             
            
            
        };    

        fresh_timer.schedule(sstask, 100, 8000);  // 2018.07.06 快照新增還原按鈕
    }
    
    public void UserDiskvddata_refresh() {
        int getIndex = 0; // 2018.07.06 快照新增還原按鈕        

        JSONObject obj = new JSONObject();
        obj.put("AccountName", CurrentUserName);
        obj.put("Password"   , CurrentPasseard);       
        String query = obj.toString();                
                
        try { bypassSSL(); } 
        catch (UnknownHostException ex) { Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex); } 
        catch (IOException ex) { Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex); }                
                /***************************** POST Start *****************************/
                try {
                    URL url  = new URL("https://" + Address + ":" + Port + "/vdi/user/vd/" + uniqueKey); 
                    HttpURLConnection conn = (HttpsURLConnection) url.openConnection();                                    
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestMethod("POST");                                                      
                    conn.setRequestProperty("Accept"        , "application/json");                      
                    conn.setRequestProperty("Content-Type"  , "application/json");                      
                    conn.setRequestProperty("Content-length", String.valueOf(query.getBytes().length)); 
                    conn.setRequestProperty("User-Agent"    , "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
                    conn.setRequestProperty("Connection"    , "Keep-Alive");                            
                    conn.setUseCaches (false);
                    conn.setInstanceFollowRedirects(true);            
                    conn.setConnectTimeout(20000);                                                      
                    conn.setReadTimeout(10000);                                                         
                    conn.connect();         
                    
                    //寫入資料     

                    try (OutputStream out = conn.getOutputStream()) {
                        out.write(query.getBytes());
                        out.flush();
                        out.close();            
                    } catch (Exception ex) { }           
                
                    /*
                    System.out.println("URL                    : " + url2 + "\n"); 
                    System.out.println("conn.getOutputStream() : " + conn2.getOutputStream() + "\n");
                    System.out.println("Resp Code              : " + conn2.getResponseCode() + "\n");
                    System.out.println("Resp Message           : " + conn2.getResponseMessage() + "\n");
                    System.out.println("-----------------------------------------\n");  
                    */
                    
                    //讀資料
                    InputStream is = conn.getInputStream();
                    StringBuilder sb;
                    String line;                      
                    
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {              
                        sb = new StringBuilder("");
                        while ((line = br.readLine()) != null) {
                            sb.append(line);         
                        }                 
                        
                        br.close();
                    }                    
                    
                    String data = sb.toString();             
                    JsonParser jsonParser = new JsonParser();
                    JsonObject returndata = (JsonObject)jsonParser.parse(data).getAsJsonObject();
                    JsonArray jsondata = returndata.getAsJsonObject().getAsJsonArray("UserVdImages");
                    

                    VD[] _array = new VD[1]; // jsondata.size() -> 1
                    
                    _diskNamePolling = "";
                    try{
                        _diskNamePolling = returndata.get("DiskName").getAsString();
                    } catch(Exception e) {}                    
                    

                    boolean IsBind = false;         
                    
                    // 2019.01.30 UserDisk快照顯示異常問題修正
                    boolean IsSetDisk = false;                       
                    
                    if(jsondata != null && jsondata.size() > 0 ) { // 2018.07.06 快照新增還原按鈕                                                               
                        // 顯示可選擇之VD
                        for (int i = 0; i < jsondata.size(); i++) {
                            JsonObject vd_term           = jsondata.get(i).getAsJsonObject();            
                            String vd_Name           = vd_term.get("VdName").getAsString();
                            String VdId              = vd_term.get("VdId").getAsString();
                            int Vd_Status = vd_term.get("VdStatus").getAsInt();
                            

                            

                             try {
                                // System.out.println("Bind User Disk : " + vd_term.get("BindUserDisk").getAsBoolean());
                                if(vd_term.get("BindUserDisk").getAsBoolean()) {
                                    IsBind = true;
                                } else {
                                    IsBind = false;
                                }
                            } catch (Exception e){
                                IsBind = false;
                            }

                                                                                    
                            if(IsBind) {
                                // 2019.01.30 UserDisk快照顯示異常問題修正
                                GB._viewDiskVDName = _vdName = vd_Name;
                                IsSetDisk = true;
                                setUserDiskVDID(VdId); 
                                switch(Vd_Status) {
                                    case 5:
                                        IsUDBtnDisable = false;
                                        _array[0] = new VD(vd_Name, VdId, false);                
                                        break;
                                    case -2:
                                        IsUDBtnDisable = true;
                                        _array[0] = new VD(vd_Name, VdId, true);                
                                        break;    
                                    case 1:
                                        IsUDBtnDisable = true;
                                        _array[0] = new VD(vd_Name, VdId, true);                
                                        break; 
                                    case 4: 
                                        IsUDBtnDisable = true;
                                        _array[0] = new VD(vd_Name, VdId, true);                
                                        break;                             
                                    default:    
                                        IsUDBtnDisable = false;
                                        _array[0] = new VD(vd_Name, VdId, false);                
                                        break;                    
                                }
                            } 
                        }                             
                    }         
                    
        // 2019.01.30 UserDisk快照顯示異常問題修正
        if(IsSetDisk) {
           setDiskName(_diskNamePolling);
        } else {
            setDiskName("");
            userdisk_data.clear();
            userdisk_data.removeAll(userdisk_data);                        
            userdisk_data.add(new UserDisk("", "", "", "", ""));
            UserDisk[] _temp = new UserDisk[1];
            _temp[0] = new UserDisk("", "", "", "", "");
            _checkUserDiskarray = _temp;
            userdisktable.refresh();
        }                       

                        udvd_data.removeAll(udvd_data);
                        udvd_data.addAll(_array); 
                        _checkUDVDarray = _array;     
                        udvdtable.refresh();    
                        
                    

                    
                    conn.disconnect();
                } catch (Exception ex) {}                

    }    
    /*------------------------ 動作功能 ------------------------*/ 
    public final void UserDiskdetectSelection() {         
//        udvdtable.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                if(mouseEvent.getClickCount() == 1){
//                    udvd_index = udvdtable.getSelectionModel().getSelectedIndex();
//                    
//                    if(udvd_index != -1) { // 2017.12.29 點空白出會出現error 
//                        fresh_timer.cancel();                        
//                        Select_vdID = (String) udvdtable.getSelectionModel().getSelectedItem().getVD_ID(); 
//                        System.out.println("click on " + Select_vdID + "\n");
//                        setVDID(Select_vdID);
//                        data_refresh();
//                    }
//                }
//            }
//        });                     
        
        udvdtable.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){ 
                udvd_index = udvdtable.getSelectionModel().getSelectedIndex();
                fresh_timer.cancel();
                Select_vdID = (String) udvdtable.getSelectionModel().getSelectedItem().getVD_ID();
                System.out.println("click on " + Select_vdID + "\n");
                // 2019.01.30 UserDisk快照顯示異常問題修正
                setUserDiskVDID(Select_vdID);   
                data_refresh();
//                Login_Unlock();
           }           
           if(event.getCode() == KeyCode.RIGHT){
                userdisktable.requestFocus();
                userdisktable.getSelectionModel().select(0);
                userdisktable.getFocusModel().focus(0);
                event.consume();
           }
           if(event.getCode() == KeyCode.ESCAPE){ 
                StopAll();
                event.consume();
           }           
        });         

        userdisktable.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.DOWN&&userdisktable.getSelectionModel().isSelected(getlist_jsonArr.size()-1)){
                UDBtn_login.requestFocus();
                event.consume();
           }
           if(event.getCode() == KeyCode.ESCAPE){ 
                StopAll();
                event.consume();
           }           
        });           
        
        
        UDBtn_exit.setOnAction((ActionEvent event) -> {
            StopAll();
        });            
    }        
    /*======================= 基本功能 =======================*/
   
    

    public  String UD_StatusName(String Satus, TableColumn column){     
        String state_string = null;
     
        if(null != Satus) switch (Satus) {
            case "0":
                state_string = WordMap.get("Snapshot_Normal");
                break;
            case "1":
                state_string = WordMap.get("Snapshot_Preparing_view");
                break;
            case "2":
                state_string = WordMap.get("Snapshot_state_View");
                break;
            case "3":
                state_string = WordMap.get("Snapshot_Preparing_delete");
                break;
            case "4":
                state_string = WordMap.get("Snapshot_Preparing_rollback");
                break;
            case "5":
                state_string = WordMap.get("Snapshot_Preparing_unview");
                break;
            default:
                break;
        }
        
        return state_string;
    
    }    

    private Callback<TableColumn<UserDisk, String>, TableCell<UserDisk, String>> UDgetCustomCellFactory() { // final String color
        return new Callback<TableColumn<UserDisk, String>, TableCell<UserDisk, String>>() {
            @Override
            public TableCell<UserDisk, String> call(TableColumn<UserDisk, String> param) {
                TableCell<UserDisk, String> cell = new TableCell<UserDisk, String>() {

                    @Override
                    public void updateItem(final String item, boolean empty) {
                        if (item != null) {
//                            System.out.println("item: " + item + ", " + "empty: " + empty +"\n");
                            
                            if(item.equals("檢視中")||item.equals("View")||item.equals("检视中")) {
                                setText(item);
                                setStyle("-fx-text-fill: " + "green" + ";");
                            } else {
                                setText(item);
                                setStyle("-fx-text-fill: " + "blue" + ";");
                            }
                            
                            

                        }
                    }
                };
                return cell;
            }
        };
    }       

    public void StopAll() {
        fresh_timer.cancel();     
        fresh_sslist_timer.cancel();
//        login_timer.stop();
//        UDlogin_timer.stop();
        Snapshot_secondStage.close();
    }     
    
    /*======================= Exception =======================*/
    public void NoUserDiskfunc() {
           
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
        alert.setTitle(WordMap.get("Alert_Title_Warning"));          // 設定對話框視窗的標題列文字
        alert.setHeaderText("");                                     // 設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭

        
        if(WordMap.get("SelectedLanguage").equals("English") || WordMap.get("SelectedLanguage").equals("Japanese")) {
            alert.setContentText("Snapshot function is abnormal. Please contact the information staff.");   // 設定對話框的訊息文字
        } else if (WordMap.get("SelectedLanguage").equals("TraditionalChinese")) {
            alert.setContentText("快照功能異常，請洽資訊人員！");   
        } else if (WordMap.get("SelectedLanguage").equals("SimpleChinese")) {
            alert.setContentText("快照功能异常，请洽资讯人员！");   
        }   
        
        // 建立ButtonType物件
        ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            
        alert.getButtonTypes().setAll(buttonTypeOK); 
            
        // 建立Button物件並設定按鍵邏輯
        Button button_ok = (Button) alert.getDialogPane().lookupButton(buttonTypeOK);

        button_ok.setOnMouseEntered((event)-> { button_ok.requestFocus(); });

        button_ok.setOnKeyPressed((event)-> {           
            if(event.getCode() == KeyCode.ENTER) {
                button_ok.fire();              
            }        
        });
            
        final Optional<ButtonType> opt = alert.showAndWait();
        final ButtonType rtn = opt.get();                     // 可以直接用「alert.getResult()」來取代           
        if (rtn == buttonTypeOK) {                            // 若使用者按下「確定」
            StopAll();
        }          
    }        
    
    
    /*======================= HTTP Command =======================*/
    // 檢視
    public void ViewUserDiskLayer() throws MalformedURLException, IOException, InterruptedException {                   
        try {
            URL url  = new URL("https://" + Address + ":" + Port + "/vdi/snapshot_userdisk/view/" + getViewLayerID());          
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();                                     
            conn.setRequestMethod("PUT");                                                        
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Accept"        , "application/json");
            conn.setRequestProperty("User-Agent"    , "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Content-Type"  , "application/json");                       
            conn.setRequestProperty("Connection"    , "Keep-Alive");                             
            conn.setUseCaches (false);
            conn.setInstanceFollowRedirects(true);               
            conn.setConnectTimeout(10000);                                                                                             
            conn.connect();
            
            System.out.println("檢視: " + conn.getResponseCode());
            
            try (OutputStream ops = conn.getOutputStream()) {
                ops.flush();
                ops.close();     
                AlertType(conn.getResponseCode());                               
            }   
            
            conn.disconnect();
        } catch (MalformedURLException e) {} catch (IOException e) {}         
    }    
    // 停止檢視
    public void StopUserDiskLayer() throws MalformedURLException, IOException, InterruptedException {        
        try {
            URL url  = new URL("https://" + Address + ":" + Port + "/vdi/snapshot_userdisk/stop_view/" + getDiskName());          
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");                                     
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Accept"        , "application/json");
            conn.setRequestProperty("User-Agent"    , "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Content-Type"  , "application/json");    
            conn.setRequestProperty("Connection"    , "Keep-Alive");          
            conn.setUseCaches (false);
            conn.setInstanceFollowRedirects(true);        
            conn.setConnectTimeout(10000);
            conn.connect();
            
            System.out.println("停止檢視: " + conn.getResponseCode());

            try (OutputStream ops = conn.getOutputStream()) {
                ops.flush();
                ops.close();   
                
                AlertType(conn.getResponseCode());              
            }   
            
            conn.disconnect();
        } catch (MalformedURLException e) {} catch (IOException e) {}         
    }    
    // 還原
    public void RollBackUserDiskLayer() throws MalformedURLException, IOException, InterruptedException {                
        try {
            URL url  = new URL("https://" + Address + ":" + Port + "/vdi/snapshot_userdisk/rollback/" + getRollBackLayerID());          
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");                                     
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Accept"        , "application/json");
            conn.setRequestProperty("User-Agent"    , "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Content-Type"  , "application/json");    
            conn.setRequestProperty("Connection"    , "Keep-Alive");          
            conn.setUseCaches (false);
            conn.setInstanceFollowRedirects(true);        
            conn.setConnectTimeout(10000);                
            conn.connect();
            
            System.out.println("還原: " + conn.getResponseCode());
            
            try (OutputStream ops = conn.getOutputStream()) {
                ops.flush();
                ops.close();

                AlertType(conn.getResponseCode());              
            }   
            
            conn.disconnect();
        } catch (MalformedURLException e) {} catch (IOException e) {}         
    }    
    // Query Login    
    public int QueryLogin() throws MalformedURLException, IOException {
        int returnCode = -100;
        URL url  = new URL("https://" + Address + ":" + Port + "/vdi/snapshot/login/" + getViewLayerID());   
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();                  
        conn.setRequestMethod("PUT");                                                       
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Accept"        , "application/json");
        conn.setRequestProperty("User-Agent"    , "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
        conn.setRequestProperty("Content-Type"  , "application/json");                      
        conn.setRequestProperty("Connection"    , "Keep-Alive");                            
        conn.setUseCaches (false);
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(10000); 
        conn.connect();        
        
        try (OutputStream out2 = conn.getOutputStream()) {                                
            out2.flush();
            out2.close();     
            returnCode = conn.getResponseCode();
        }                
        conn.disconnect();                
        return returnCode;
    }        
    // 登入
    public void  LoginUserDiskLayer() throws MalformedURLException, IOException, InterruptedException {     
        Platform.runLater(() -> { 
            new LoginMultiVD(public_stage, WordMap,jsondata, Address, CurrentUserName, CurrentPasseard, uniqueKey, Port); 
        });  

       /* 
        try {                       
            UDlogin_timerflag = true;
            CheckLogincompleted();
            URL url  = new URL("https://" + Address + ":" + Port + "/vdi/snapshot/login/" + getViewLayerID());   
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();        
            conn.setRequestMethod("PUT");                                             
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Accept"        , "application/json");
            conn.setRequestProperty("User-Agent"    , "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Content-Type"  , "application/json");            
            conn.setRequestProperty("Connection"    , "Keep-Alive");                                  
            conn.setUseCaches (false);
            conn.setInstanceFollowRedirects(true);            
            conn.setConnectTimeout(10000); 
            conn.connect();

            try (OutputStream out2 = conn.getOutputStream()) {                                
                out2.flush();
                out2.close();
                System.out.println("login View -output out : "  + out2 + "\n");                  
                System.out.println("login View -URL         : " + url + "\n");         
                int RespCode = conn.getResponseCode();
                System.out.println("login View -Resp Code   : " + RespCode + "\n"); 
                System.out.println("login View -Resp Code   : " + conn.getResponseCode() + "\n");
                System.out.println("login View -Resp Message: " + conn.getResponseMessage() + "\n");            
            
                InputStream is = conn.getInputStream();           
                StringBuilder sb;
                String line;  

                try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {              
                    sb = new StringBuilder("");
                    while ((line = br.readLine()) != null) { sb.append(line); }                                         
                    br.close();
                }

                String Sdata = sb.toString();             
                JsonParser jsonSParser = new JsonParser();
                JsonObject Spicedata = (JsonObject)jsonSParser.parse(Sdata).getAsJsonObject();
                JsonArray SpiceArr = Spicedata.getAsJsonObject().getAsJsonArray("SpiceServers");
                new PingIP(SpiceArr);                
                long startTime = System.currentTimeMillis(); // fetch starting time
                while(false || (System.currentTimeMillis()-startTime) < 5000) {
                    if(!"".equals(GB.ConnectionAddress)  && !"".equals(GB.ConnectionPort)) {
                        RunSpice(GB.ConnectionAddress, GB.ConnectionPort);
                        break;
                    }
                    TimeUnit.SECONDS.sleep(1);
                }                
                System.out.println("Finish Run Spice");                 
                UDlogin_timerflag = false;                                  
            }   
            conn.disconnect();
        } catch (MalformedURLException e) {} catch (IOException e) {}   
        */
    }

    public void AlertType(int ResCode) {
        String Error_403_en = "The VD bound to this user disk is currently powered on！";
        String Error_403_zh = "此用戶磁碟所綁定的VD目前開機中！";
        String Error_403_cn = "此用户磁碟所绑定的VD目前开机中！";       
        String Suggest_403_en = "Please power off the VD before viewing.";
        String Suggest_403_zh = "請先關閉VD再進行檢視。";
        String Suggest_403_cn = "请先关闭VD再进行检视。";           
                
        
        if(ResCode == 403) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
            alert.setTitle(WordMap.get("Message_Login"));
            alert.setHeaderText(null);

            alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
            alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE); 

            switch (WordMap.get("SelectedLanguage")) {
                case "English":
                case "Japanese":
                    alert.setContentText(
                            WordMap.get("WW_Header") + " ： " + Error_403_en + " ( " + ResCode + " ) "
                                    + "\n" + "\n" +
                                    WordMap.get("Message_Suggest") + " ： " + Suggest_403_en
                                    + "\n" + "\n" +
                                    "VD Name ： " + _vdName
                    );  break;
                case "TraditionalChinese":
                    alert.setContentText(
                            WordMap.get("WW_Header") + " ： " + Error_403_zh + " ( " + ResCode + " ) "
                                    + "\n" + "\n" +
                                    WordMap.get("Message_Suggest") + " ： " + Suggest_403_zh
                                    + "\n" + "\n" +
                                    "VD名稱 ： " + _vdName                            
                    );  break;
                case "SimpleChinese":
                    alert.setContentText(
                            WordMap.get("WW_Header") + " ： " + Error_403_cn + " ( " + ResCode + " ) "
                                    + "\n" + "\n" +
                                    WordMap.get("Message_Suggest") + " ： " + Suggest_403_cn
                                    + "\n" + "\n" +
                                    "VD名称 ： " + _vdName                            
                    );  break;                     
                default:
                    break;
            }


            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(buttonTypeOK); 
            alert.showAndWait();
        }     

        if(ResCode == 400) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
            alert.setTitle(WordMap.get("Message_Login"));
            alert.setHeaderText(null);

            alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
            alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE); 

            alert.setContentText(
                WordMap.get("WW_Header") + " ： " + WordMap.get("Message_Unknown_system_error") + " ( " + ResCode + " ) "
                + "\n" + "\n" + 
                WordMap.get("Message_Suggest") + " ： " + WordMap.get("Message_Error_400")
            );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(buttonTypeOK); 
            alert.showAndWait();                
        }     
    
    }
        
    public void CheckLogincompleted() {
        final LongProperty lastUpdate = new SimpleLongProperty();                            
        final long minUpdateInterval = 500000000 ; // nanoseconds. Set to higher number to slow output.

        UDlogin_timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate.get() > minUpdateInterval) {
                    if (!UDlogin_timerflag) { 
                        UDlogin_timer.stop();
                        Login_Unlock();  
                    }          
                    lastUpdate.set(now);
                }
            }
        };
        UDlogin_timer.start();  
    }          
    
    // 2019.01.10 User Disk bug fix
    public void IsUDRollBackAlert() throws IOException, MalformedURLException, InterruptedException{ // 2018.07.12 還原按鈕和修改dialog      
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(WordMap.get("Alert_Title_Warning"));
        alert.setHeaderText(null);
        alert.getDialogPane().setMinSize(450,Region.USE_PREF_SIZE);
        alert.getDialogPane().setMaxSize(450,Region.USE_PREF_SIZE);
        alert.setContentText(WordMap.get("WW_Header") + " ： " + WordMap.get("Snapshot_RollBack_Confirm"));            
        ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType(WordMap.get("Alert_Cancel"),ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);         
        Optional<ButtonType> result01 = alert.showAndWait();
        if (result01.get() == buttonTypeOK) {                    
            try {                                                
                RollBackUserDiskLayer();
            } catch (IOException ex) {
                Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
            }     
        } else {
            alert.close();
        }
    }      
    
    
    /*======================= 資料處理 =======================*/
      

    public class UserDisk { // 定義User Disk資料模型        
        private final SimpleStringProperty Layer;
        private final SimpleStringProperty State;
        private final SimpleStringProperty CreateTime;
        private final SimpleStringProperty Size;
        private final SimpleStringProperty Desc;


        UserDisk(String l, String state, String ct, String size, String desc) {
            this.Layer      = new SimpleStringProperty(l);
            this.State      = new SimpleStringProperty(state);
            this.CreateTime = new SimpleStringProperty(ct);
            this.Size       = new SimpleStringProperty(size);
            this.Desc       = new SimpleStringProperty(desc);
        } 
        // Layer
        public String getLayer() {
            return Layer.get();
        }

        public void setLayer(String l) {
            Layer.set(l);
        }

        public StringProperty LayerProperty() {
            return Layer;
        }        
        // State
        public String getState() {
            return State.get();
        }

        public void setState(String state) {
            State.set(state);
        }

        public StringProperty StateProperty() {
            return State;
        }
        // CreateTime
        public String getCreateTime() {
            return CreateTime.get();
        }

        public void setCreateTime(String ct) {
            CreateTime.set(ct);
        }

        public StringProperty CreateTimeProperty() {
            return CreateTime;
        }
        // Size
        public String getSize() {
            return Size.get();
        }

        public void setSize(String size) {
            Size.set(size);
        }

        public StringProperty SizeProperty() {
            return Size;
        }
        // Desc
        public String getDesc() {
            return Desc.get();
        }

        public void setDesc(String desc) {
            Desc.set(desc);
        }

        public StringProperty DescProperty() {
            return Desc;
        }
    }      

    public static synchronized String getDiskName() {
        return _diskName;
    }

    public static synchronized void setDiskName(String _name) {
        _diskName = _name;
    }        
    
    // 2019.01.30 UserDisk快照顯示異常問題修正
    public static synchronized String getUserDiskVDID() {
        return UserDisk_Vd_Id;
    }

    public static synchronized void setUserDiskVDID(String vdid) {
        UserDisk_Vd_Id = vdid;
    }       
     
    /* ScheduledService 的用法
        final MyService service = new MyService();
        service.setDelay(new Duration(8000));
        service.setPeriod(new Duration(8000));
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(final WorkerStateEvent workerStateEvent) {
                List<String> results = (List<String>) workerStateEvent.getSource().getValue();
                StateCol.setText(results.get(0));
            }
        });
        service.start();            
           
        class MyService extends ScheduledService<List<String>> {
            @Override
            protected Task<List<String>> createTask() {
                final Task<List<String>> voidTask = new Task<List<String>>() {
                    @Override
                    protected List<String> call() throws Exception {

                        return null;
                    }
                };
                return voidTask;
            }
        }
    */
}
