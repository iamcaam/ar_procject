/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import static acrored_vdi_viewer.AcroRed_VDI_Viewer.getProtocol;
import static acrored_vdi_viewer.AcroRed_VDI_Viewer.setProtocol;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import static java.lang.Thread.sleep;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.stage.WindowEvent;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author william
 */
public class LoginMultiVD {
     /*------------------------ import檔案及匯入文字檔 ------------------------*/
    public Map<String, String> WordMap       = new HashMap<>();  
    // 取值，(IP, 使用者帳號, 使用者密碼, uniqueKey, Port)
    public String Address;
    public String Port;  
    public String uniqueKey;
    public String CurrentUserName;
    public String CurrentPasseard;      
    public JsonArray jsondata;    
    private Stage public_stage;  
    //  thread
    private QueryMachine QM;
    private LoginAlert LA;    
    /*------------------------ 設定Layout ------------------------*/
    Stage MultiVDList_secondStage            = new Stage();
    private Scene secondScene; 
    private BorderPane rootPane;                                // 2017.11.27 william BorderPane框架
    private GridPane MainGP;                                    // 2017.11.27 william BorderPane中間層 (ListView物件)
    // 2017.11.29 william 多VD登入中畫面鎖住
    private StackPane stack                  = new StackPane(); 
    private Label Login_title = new Label();  // 2018.11.30 新增等待中文字動態顯示                                
    private Label Login_title2 = new Label();  // 2018.11.30 新增等待中文字動態顯示 
    private Label Login_title3 = new Label();  // 2018.11.30 新增等待中文字動態顯示                               
    private GridPane LoginGP;                                   
    private ProgressIndicator p1;                               
    private Bounds mainBounds;     
    private Label LMVDTitle;
    
    private Label LoginMultiVD_desc1;                           // 操作說明：1. 請點選上面雲主機/雲桌面列表。
    private Label LoginMultiVD_desc2;                           // 操作說明：2. 或點選『離開』按鈕關閉視窗。    
    private HBox WC;
    private HBox selection_Title;
    private HBox selection; 
    private HBox btn; 
    private VBox Desc; 
    private Button Btn_exit;
    private Button Btn_login; // 2018.03.28 william  多VD登入新增登入按鈕  
    String name; // 2018.03.28 william  多VD登入新增登入按鈕         
    /*------------------------ 設定viewlist------------------------*/
    ListView<String> VDList                  = new ListView<>();
    ObservableSet<String> selectedVDs;
    public static final ObservableList data  = FXCollections.observableArrayList();     
    /*------------------------ 設定TableView------------------------*/
    private TableView<MultiVD> vd_table      = new TableView();    
    // 預設值寫進表格內
    public ObservableList<MultiVD> vd_data   = FXCollections.observableArrayList();    
    // 表格內Column設定
    TableColumn vdSNCol;
    TableColumn<MultiVD, String> vdNameCol;     
    TableColumn<MultiVD, String> ActionCol;
    TableColumn<MultiVD, String> vdDescCol;    
    TableColumn<MultiVD, String> ActionBtnCol; // 2018.03.28 william  多VD登入新增登入按鈕    
    TableColumn<MultiVD, String> vdStatusCol; // 2018.04.12 william  多VD登入新增開機狀態
    TableColumn<MultiVD, String> RDPStatusCol;
    TableColumn<MultiVD, ProtocolType> optionColumn;      
    // RadioButtonCell使用
    private ToggleGroup group;
    int selectIndex;
    MultiVD selectVD;
    // 我的最愛-Radio讀寫檔使用
    JSONObject favoriteVD;
    public String favoriteVD_username;
    public String favoriteVD_IP;
    public String favoriteVD_VdName;
    public String favoriteVD_ID;
//    public String favoriteVD_sele;
    /*------------------------ 設定長寬 ------------------------*/
    int secondScene_width                    = 720; 
    int secondScene_heighth                  = 456;       
    int listView_width                       = 630;
    int listView_height                      = 235;
    int btn_width                            = 80; // 2018.03.28 william  多VD登入新增登入按鈕
    int btn_height                           = 30;  // 2018.03.28 william  多VD登入新增登入按鈕
    int MainGP_width                         = 590;
    int MainGP_height                        = 365;  
    int vd_tableView_width                   = 733; //  2018.04.10  william  功能修改   630 ->  680 // 2018.04.12 william  多VD登入新增開機狀態   
    int vd_tableView_height                  = 307; //   2018.04.10  william  功能修改   285->  307
    /*------------------------ 設定值 ------------------------*/
    // GET json資料
    public JsonObject term;
    public String VdName;
    public String Select_VdName;
    public String VdId;    
    public String VdDesc;    
    public String IsVdOnline;  
    public String IsDefault;
    public static String Vd_Id;
    public static String Vd_Name; // 2018.03.28 william  多VD登入新增登入按鈕    
    public String VdStatus; // 2018.04.12 william  多VD登入新增開機狀態
    URL url;
    HttpURLConnection conn;
        
    private double screenCenterX;
    private double screenCenterY;      
    
    public AnimationTimer timer;
    public boolean checkLogin_flag      = false;
    
    /*------------------------ Get vd index ------------------------*/    
    public int vd_index; // 2018.04.10  william  功能修改   
    public String sorting_vdname; // 2018.04.12 william  多VD登入新增開機狀態
    public GB GB;    
    // 2018.11.11 新增AcroDesk & RDP Protocol 
    public ComboBox<ProtocolType> TypecomboBox;
    public enum ProtocolType { AcroSpice, AcroRDP } // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP      
    Dictionary dict = new Hashtable();  // create a new hashtable
    Dictionary dictVDStatus = new Hashtable();
    Dictionary dictComboBox = new Hashtable();
//    Dictionary dictVdIndex = new Hashtable();
    public boolean StopPolling = false;
    MultiVD checkMultiVD;
    MultiVD[] _checkarray = null;
    public boolean SortFlag = false;
    public static int select_index; 
    TableColumn currentSortColumn;
    String IsVdOnlineVdName = null;
    String IsVdOnlineVdId = null;
    JsonArray jsondataTemp = null;
    String GetVdName = null;        
    public boolean StopPollingFresh = false;
    // 2018.11.30 新增等待中文字動態顯示
    public boolean StopMessageQueue = false;    
    public AnimationTimer TextTimer;      
    BlockingQueue<String> messageQueue = null;
    String messageText;
    String message;     
    
    // 2018.12.12 新增 431NoVGA 和第一次開機
    Dictionary dictRDPFirst = new Hashtable();    
    Dictionary dictKVMVDStatus = new Hashtable();
    Dictionary dictCannotConnect = new Hashtable();
    Dictionary dictLockType = new Hashtable();
    int lockStringType = -1;
    public boolean StopMessageQueue2 = false;    
    public AnimationTimer TextTimer2;      
    BlockingQueue<String> messageQueueRow1 = null;
    String messageTextRow1;
    String messageRow1;    
    BlockingQueue<String> messageQueueRow2 = null;
    String messageTextRow2;
    String messageRow2;    
    boolean connectFlag = false;
    boolean IsVdOrg = false;
    
    boolean enableusb = false;
    
    // 2019.03.28 VM/VD migration reconnect
    Dictionary dict3DType = new Hashtable();    
    Dictionary dictSelectProtocol = new Hashtable();  
    
    boolean killthread = false;

    // Input：主畫面Stage Data, 語系變數, 使用者的VD資料Array, IP, 用戶名, 密碼, uuid, port,  Output： , 功能： 建構式    
    public LoginMultiVD(Stage primaryStage, Map<String, String> LangMap,JsonArray jsonArr, String IPAddr,String Uname,String Password,String UniqueKey,String IPPort){
        public_stage    = primaryStage;        
        WordMap         = LangMap;
        jsondata        = jsonArr;
        // 取值，(IP, 使用者帳號, 使用者密碼, uniqueKey, Port)
        Address         = IPAddr;
        CurrentUserName = Uname;
        CurrentPasseard = Password;
        uniqueKey       = UniqueKey;
        Port            = IPPort;        
        term            = null;
        QM              = new QueryMachine(WordMap);
        LA              = new LoginAlert(WordMap);     
        // 2018.11.30 新增等待中文字動態顯示
        messageQueue = new ArrayBlockingQueue<>(1);        
        // 2018.12.12 新增 431NoVGA 和第一次開機
        messageQueueRow1 = new ArrayBlockingQueue<>(1);
        messageQueueRow2 = new ArrayBlockingQueue<>(1);
        GB.RDP_Stage_Type = -100;
        // 2018.18.18 new create & reborn 
        GB.RDPFirst = -1;
        GB.IsAssignedUserDisk = "";      
        
        
        if("Japanese".equals(WordMap.get("SelectedLanguage"))) {
            secondScene_width = 1012; 
            secondScene_heighth = 456;    
            MainGP_width = 1024;
            MainGP_height = 365;  
            vd_tableView_width = 1024;
            vd_tableView_height = 307;
        }         
        
        
//        vd_cc           = 2;

        // read json file
//        try {
//            LoadMyfavoriteStatus();
//            System.out.println("my favorite VD : " + favoriteVD_VdName + "\n");
//        } catch (ParseException ex) {
//            Logger.getLogger(LoginMultiVD.class.getName()).log(Level.SEVERE, null, ex);
//        }
        /***  主畫面使用GRID的方式Layout ***/
        MainGP = new GridPane();
        MainGP.setGridLinesVisible(false);             // 開啟或關閉表格的分隔線       
        MainGP.setAlignment(Pos.CENTER);       
        MainGP.setPadding(new Insets(35, 50, 50, 20)); // 設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        MainGP.setHgap(15);                            // 元件間的水平距離
   	MainGP.setVgap(15);                            // 元件間的垂直距離        
        MainGP.setPrefSize(MainGP_width, MainGP_height);
        MainGP.setMaxSize(MainGP_width, MainGP_height);
        MainGP.setMinSize(MainGP_width, MainGP_height); 
        
        if("Japanese".equals(WordMap.get("SelectedLanguage"))) {
            MainGP.setTranslateY(-12);
            MainGP.setTranslateX(-13);
        } else {
            MainGP.setTranslateY(-5); //   2018.04.10  william  功能修改 
            MainGP.setTranslateX(-3); //   2018.04.10  william  功能修改           
        }           
        
       
        
              
        /***  底層畫面使用Border的方式Layout ***/
        rootPane = new BorderPane();                   // 多VD登入中畫面鎖住    
        rootPane.setCenter(MainGP);
        
        /*------------------------ RadioBtn ------------------------*/ 
        group = new ToggleGroup();
        // selectIndex = -1;
        selectIndex = 0;
        // 建構子,監聽group內被選擇的Radio，並把被選擇的Radio的Index,設定在selectIndex
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    selectIndex       = (int)group.getSelectedToggle().getUserData();
                    selectVD          = (MultiVD)vd_table.getItems().get(selectIndex);
                    favoriteVD_VdName = selectVD.getVD_Name();
                    favoriteVD_ID     = selectVD.getVD_ID();
                    setVDID(favoriteVD_ID);
                    System.out.println("selected VD (Radio): " + favoriteVD_VdName + ", " + "selectIndex: " + selectIndex + ", " + "selectVD: " + selectVD + ", "  + "select VD_ID: " + favoriteVD_ID +"\n");
                }                
            }
        });           
        /*----------------------------------------------------------*/ 
        /*----------------------------------------------------------*/ 
        vd_table.setRowFactory(tv -> new TableRow<MultiVD>() {            
            {                               
                // double click
                setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (! this.isEmpty()) && !connectFlag) { // 2018.12.12 新增 431NoVGA 和第一次開機                   
                        connectFlag = true; // 2018.12.12 新增 431NoVGA 和第一次開機
                        MultiVD rowData = this.getItem();
                        Select_VdName = rowData.getVD_Name();
                        
                        if(Boolean.parseBoolean(dictCannotConnect.get(Select_VdName).toString()) && "AcroRDP".equals(dictSelectProtocol.get(Select_VdName).toString())) { // 2019.03.28 VM/VD migration reconnect getProtocol() -> dictSelectProtocol.get(Select_VdName).toString()
                            CannotConnectCmd();
                        } else if(Integer.parseInt(dictKVMVDStatus.get(Select_VdName).toString()) == 6 || Integer.parseInt(dictKVMVDStatus.get(Select_VdName).toString()) == 7) { // 2018.12.27  其他中斷連線狀態 - Crash & Migrate
                            int t = -100;
                            if(Integer.parseInt(dictKVMVDStatus.get(Select_VdName).toString()) == 6)                        
                                t = -11;
                            else if(Integer.parseInt(dictKVMVDStatus.get(Select_VdName).toString()) ==7)
                                t = -12;

                            DisconnectCmd(t);
                        } else {
                            ConnectCmd(Select_VdName);
                        }                                                                                                   
                    } else {
                        MultiVD rowData = this.getItem();             
                        if(rowData != null) {
                            setVDName(rowData.getVD_Name());                    
                            setSelectIndex(vd_table.getSelectionModel().getSelectedIndex());                             
                        }
                
                    }                    
                }); 
            }
        });         
        
        
        // 畫面上層
        top_layout();
        // 畫面中層
        vd_layout(jsonArr);
        // 畫面下層 (描述及按鈕)
        bottom_layout();
        
        CheckLogincompleted(); // 檢查登入是否完成
        detectSelection();     // 動作偵測
        RenewString();// 2018.11.30 新增等待中文字動態顯示        
        RenewString2(); // 2018.12.12 新增 431NoVGA 和第一次開機
        
        ChangeLabelLang(LMVDTitle, LoginMultiVD_desc1, LoginMultiVD_desc2, vdSNCol, ActionCol, vdNameCol, vdDescCol, vdStatusCol, optionColumn, RDPStatusCol); // 2018.04.10  william  功能修改 // 2018.04.12 william  多VD登入新增開機狀態   
        ChangeButtonLang(Btn_exit, Btn_login); // 2018.04.10  william  功能修改   
        
        vd_table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY); // 2018.04.10  william  功能修改 Remove extra column in JavaFX TableView http://www.superglobals.net/remove-extra-column-tableview-javafx/

        /* 針對sort處理用的-無中間態(unsorted) */
        // set default sort column
        currentSortColumn = vd_table.getColumns().get(0);
        vd_table.getSortOrder().setAll(currentSortColumn);

        // update current sort column after sorting changes
        for (TableColumn column : vd_table.getColumns()) {
            column.sortTypeProperty().addListener((observable, oldValue, newValue) -> {
                currentSortColumn = column;
            });
        }
        // if table loses comparator restore the sort order!
        vd_table.comparatorProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {                        
                vd_table.getSortOrder().setAll(currentSortColumn);
                if (currentSortColumn.getSortType().equals(TableColumn.SortType.ASCENDING)) {
                    currentSortColumn.setSortType(TableColumn.SortType.DESCENDING);
                } else {
                    currentSortColumn.setSortType(TableColumn.SortType.ASCENDING);
                }
            }
        });         
        
        /* 2017.11.27 william 以下為跳出視窗之功能 */                
	// Set the Style-properties of the VBox
        // https://examples.javacodegeeks.com/desktop-java/javafx/listview-javafx/javafx-listview-example/
        
        secondScene = new Scene(rootPane, secondScene_width, secondScene_heighth); // 590, 365
        secondScene.getStylesheets().add("LoginMultiVD.css");
        
        mainBounds = primaryStage.getScene().getRoot().getLayoutBounds();    

        MultiVDList_secondStage.setX(primaryStage.getX() + (mainBounds.getWidth() - secondScene_width ) / 2);
        MultiVDList_secondStage.setY(primaryStage.getY() + (mainBounds.getHeight() - secondScene_heighth ) / 2); 
        MultiVDList_secondStage.setScene(secondScene);
        MultiVDList_secondStage.setTitle(WordMap.get("APP_Title"));  // 視窗標題
        switch (GB.JavaVersion) {
            case 0:
                MultiVDList_secondStage.getIcons().add(new Image("images/Icon2.png"));
                break;
            case 1:
                MultiVDList_secondStage.getIcons().add(new Image("images/MCIcon2.png"));
                break;                
            default:
                break;
        }     
        MultiVDList_secondStage.initStyle(StageStyle.DECORATED);     // DECORATED -> UNDECORATED 
        MultiVDList_secondStage.initModality(Modality.WINDOW_MODAL); // window modal lock NONE -> WINDOW_MODAL
        MultiVDList_secondStage.initOwner(primaryStage);
        MultiVDList_secondStage.setResizable(false);                 // 將視窗放大,關閉             
        MultiVDList_secondStage.show();        
        
        // 2018.11.11 新增AcroDesk & RDP Protocol
        new Thread(new Runnable() {                
            @Override
            public void run() {
                while(true) {  

                    if(StopPolling)
                        break;
//                    System.out.println("Run Polling"); 
                    try {
                        TimeUnit.SECONDS.sleep(3);
                        VDListPolling();                        
                    } catch (InterruptedException ex) {
                        Logger.getLogger(LoginMultiVD.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(LoginMultiVD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }  
            }
        }).start();        
        
        // 新增Stage關閉事件
        MultiVDList_secondStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
            System.out.println("Stage is closing");           
            MultiVDList_secondStage.close();
            GB.ExitFlag = true; // 2018.11.11 新增AcroDesk & RDP Protocol
            StopAll(); // 2018.12.04 新增VD&RDP狀態判斷            
          }
      }); 
        
    }  

    /*------------------------ 畫面layout ------------------------*/ 
    // 畫面上層
    public final void top_layout() {
        /***  Title 放入一個HBOX內 ***/
        LMVDTitle = new Label(WordMap.get("Select_VD"));
//        LMVDTitle.setId("Select_VD"); // 2018.04.12 william  多VD登入新增開機狀態
        
        WC = new HBox(LMVDTitle);
        WC.setAlignment(Pos.TOP_CENTER);
        WC.setId("Title"); // 2018.04.12 william  多VD登入新增開機狀態
        WC.setPadding(new Insets(0, 0, 0, 0));
        
        switch (WordMap.get("SelectedLanguage")) { // 2018.04.12 william  多VD登入新增開機狀態
            case "English":
                WC.setTranslateY(31); 
                WC.setTranslateX(16); 
                break;
            default:
                WC.setTranslateY(39); //   45 -> 39
                WC.setTranslateX(16); //  10  -> 16    
                break;
        }  
        MainGP.add(WC,0,0);
    }
    // 畫面中層
    public final void vd_layout(JsonArray jsondata) {
        /*------------------------ 畫面顯示(將VD資料填入Table內) ------------------------*/
        TableLayout();              
        

        
        // 顯示可選擇之VD
        tablelist_show(jsondata);

        // 顯示可選擇之VD (測試用)
//        for (int i = 0; i < 18; i++) {              
//            vd_data.add(new MultiVD("vd_Name " + i));
//        } 
            
        /*------------------------ 產生列表按鈕 ------------------------*/
        CreateTableBtn(ActionBtnCol); // 2018.03.28 william  多VD登入新增登入按鈕
        /*------------------------ 產生Radio按鈕 ------------------------*/
        ActionCol.setCellFactory(param -> new RadioButtonCell());
               
        vd_table.setEditable(false);       
        
        // 2018.12.18 RDP Enable
        if(GB.RDPEnable)
            vd_table.getColumns().addAll(vdSNCol, vdNameCol, vdStatusCol, optionColumn, RDPStatusCol, vdDescCol); // 2018.04.12 william  多VD登入新增開機狀態   
        else
            vd_table.getColumns().addAll(vdSNCol, vdNameCol, vdStatusCol, optionColumn, vdDescCol); 
        vd_table.setItems(vd_data);     
        vd_table.getStyleClass().add("table_header");
        vd_table.setPrefSize(vd_tableView_width, vd_tableView_height);
        vd_table.setMaxSize(vd_tableView_width , vd_tableView_height);
        vd_table.setMinSize(vd_tableView_width , vd_tableView_height);         

        // 設定第一個元素為被選擇狀態
        vd_table.requestFocus();
        vd_table.getSelectionModel().select(0);
        vd_table.getFocusModel().focus(0);
        setSelectIndex(0);
        
        selection = new HBox();
	selection.setSpacing(20);       
        selection.setAlignment(Pos.CENTER);
        selection.setPadding(new Insets(0, 0, 0, 0));  
	selection.getChildren().addAll(vd_table);    
        selection.setTranslateX(17);
        selection.setTranslateY(15);
        
        MainGP.add(selection,0,2);                          
    }
    
    public void TableLayout() {
        vd_data.clear(); // 清空data先前的內容 
        /*------------------------ 讓Table內顯示項次 ------------------------*/
        vdSNCol = new TableColumn(WordMap.get("VD_SN"));    
        
        vdSNCol.setCellValueFactory(new Callback<CellDataFeatures<MultiVD, MultiVD>, ObservableValue<MultiVD>>() {
             @Override
             public ObservableValue<MultiVD> call(CellDataFeatures<MultiVD, MultiVD> v) {
                return new ReadOnlyObjectWrapper(v.getValue());
            }
        });        
        
        vdSNCol.setCellFactory(new Callback<TableColumn<MultiVD, MultiVD>, TableCell<MultiVD, MultiVD>>() {
            @Override 
            public TableCell<MultiVD, MultiVD> call(TableColumn<MultiVD, MultiVD> param) {
                return new TableCell<MultiVD, MultiVD>() {
                    @Override 
                    protected void updateItem(MultiVD item, boolean empty) {
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
        
          if("Japanese".equals(WordMap.get("SelectedLanguage"))) {
            vdSNCol.setPrefWidth(90);
            vdSNCol.setMinWidth(90);   
          } else {
            vdSNCol.setPrefWidth(60);
            vdSNCol.setMinWidth(60);
          }        
        

        vdSNCol.setSortable(false);
//        vdSNCol.setStyle("-fx-font-family:'Times New Roman';");        
        /*------------------------ 讓Table內顯示動作 ------------------------*/
        ActionCol = new TableColumn(WordMap.get("LoginMultiVD_Mark"));    
        ActionCol.setPrefWidth(80);
        ActionCol.setMinWidth(80);
        ActionCol.setMaxWidth(80);           
        /*------------------------ 讓Table內登入動作 ------------------------*/
        // 2018.03.28 william  多VD登入新增登入按鈕      
        ActionBtnCol = new TableColumn(WordMap.get("Action"));    
        ActionBtnCol.setPrefWidth(80);
        ActionBtnCol.setMinWidth(80);
        ActionBtnCol.setMaxWidth(80);          
        /*------------------------ 讓Table內顯示名稱 ------------------------*/
        vdNameCol = new TableColumn(WordMap.get("VD_Name"));          
        vdNameCol.setPrefWidth(165); // 468 ->  268 // 2018.03.28 william  多VD登入新增登入按鈕 
        vdNameCol.setMinWidth(165);
        vdNameCol.setStyle( "-fx-alignment: center-left;"); // https://stackoverflow.com/questions/13455326/javafx-tableview-text-alignment
        /*------------------------ 讓Table內顯示備註 ------------------------*/
        vdDescCol = new TableColumn(WordMap.get("Comment"));    
          if("Japanese".equals(WordMap.get("SelectedLanguage"))) {
            vdDescCol.setPrefWidth(280); // 2018.04.10  william  功能修改
            vdDescCol.setMinWidth(280);
          } else {
            vdDescCol.setPrefWidth(150); // 2018.04.10  william  功能修改
            vdDescCol.setMinWidth(150);
          }           

        vdDescCol.setStyle( "-fx-alignment: center-left;");    
        /*------------------------ 讓Table內顯示開機狀態 ------------------------*/

         vdStatusCol = new TableColumn(WordMap.get("Str_Status"));    
         
          if("Japanese".equals(WordMap.get("SelectedLanguage"))) {
            vdStatusCol.setPrefWidth(160);
            vdStatusCol.setMinWidth(160); 
          } else {
            vdStatusCol.setPrefWidth(100);
            vdStatusCol.setMinWidth(100);
   //         vdStatusCol.setMaxWidth(100);
          }          
         

         vdStatusCol.setCellFactory(getCustomVDCellFactory());      
       
         
//        RDPStatusCol.setCellFactory(getRDPCellFactory());   
        RDPStatusCol = new TableColumn(WordMap.get("Str_Protocol" ));  
          if("Japanese".equals(WordMap.get("SelectedLanguage"))) {
            RDPStatusCol.setPrefWidth(190);
            RDPStatusCol.setMinWidth(190);
          } else {
            RDPStatusCol.setPrefWidth(120);
            RDPStatusCol.setMinWidth(120);
          }         

         
        // ==== Protocol (COMBO BOX) === https://o7planning.org/en/11079/javafx-tableview-tutorial
        optionColumn = new TableColumn(WordMap.get("Str_Protocol_2"));  
        optionColumn.setPrefWidth(135);
        optionColumn.setMinWidth(135);
        optionColumn.setMaxWidth(135);         
        optionColumn.setSortable(false);
        if(!GB.RDPEnable) { // 2019.01.24 New 5.1.X
            optionColumn.setVisible(false); 
        }        
        optionColumn.setCellFactory(param -> new ComboBoxCell());        
        optionColumn.setCellValueFactory(i -> {
            
            // final StringProperty value = i.getValue().optionProperty();           
            final ProtocolType value = i.getValue().getOption();            
            int StatusValue = 0;
            GetVdName = i.getValue().getVD_Name();
            
            // 2018.12.18 RDP Enable
            if(GB.RDPEnable) {
                if(null != i.getValue().getConnect_Type()) switch (i.getValue().getConnect_Type()) {
                    case 0:
                        getComboBox().getItems().addAll(ProtocolType.AcroSpice); // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP   
                        getComboBox().setDisable(true);
                        StatusValue = 1;
                        break;
                    case 1:
                        getComboBox().getItems().addAll(ProtocolType.AcroRDP);  // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP   
                        getComboBox().setDisable(true);
                        StatusValue = 2;
                        break;
                    case 2:
                        switch (i.getValue().getProtocol()) {
                            case 0:
                                if(dict.get(i.getValue().getVD_Name()) == null || "AcroSpice".equals(dict.get(i.getValue().getVD_Name()).toString())) { // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP   
                                    StatusValue = 3;
                                    getComboBox().getItems().addAll(ProtocolType.values());
                                } else {
                                    StatusValue = 4;
                                    getComboBox().getItems().addAll(ProtocolType.AcroRDP, ProtocolType.AcroSpice); // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP   
                                }
                                break;
                            case 1:
                                if(dict.get(i.getValue().getVD_Name()) == null || "AcroSpice".equals(dict.get(i.getValue().getVD_Name()).toString())) { // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP   
                                    StatusValue = 3;
                                    getComboBox().getItems().addAll(ProtocolType.values());
                                } else {
                                    StatusValue = 4;
                                    getComboBox().getItems().addAll(ProtocolType.AcroRDP, ProtocolType.AcroSpice); // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP   
                                }
                                break;
                            case 2:
                                if(dict.get(i.getValue().getVD_Name()) == null || "AcroRDP".equals(dict.get(i.getValue().getVD_Name()).toString())) { // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP   
                                    StatusValue = 4;
                                    getComboBox().getItems().addAll(ProtocolType.AcroRDP, ProtocolType.AcroSpice); // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP   
                                } else {
                                    StatusValue = 3;
                                    getComboBox().getItems().addAll(ProtocolType.values());
                                }                            
                                break;
                            default:
                                break;
                        }   
                        break;
                    default:            
                        break;
                }                                                
            } else {
                getComboBox().getItems().addAll(ProtocolType.AcroSpice); 
                StatusValue = 1;                                    
            }
            
            getComboBox().getSelectionModel().selectFirst();
            getComboBox().setId(i.getValue().getVD_Name());
            dictComboBox.put(i.getValue().getVD_Name(), StatusValue);
            if(i.getValue().getConnect_Type() == 2 && dict.get(i.getValue().getVD_Name()) != null)
                dict.put(i.getValue().getVD_Name(), dict.get(i.getValue().getVD_Name()));
            else    
                dict.put(i.getValue().getVD_Name(), value);

            // binding to constant value
            return Bindings.createObjectBinding(() -> value );
        });        
        

        // 設定每一個欄位對應的JavaBean物件與Property名稱        
        vdNameCol.setCellValueFactory(new PropertyValueFactory<MultiVD, String>("VD_Name"));                        
        ActionCol.setCellValueFactory(new PropertyValueFactory<>("Action"));
        vdDescCol.setCellValueFactory(new PropertyValueFactory<MultiVD, String>("VD_Desc"));                        
        ActionBtnCol.setCellValueFactory(new PropertyValueFactory<>("ActionBtn")); // 2018.03.28 william  多VD登入新增登入按鈕      
        vdStatusCol.setCellValueFactory(new PropertyValueFactory<MultiVD, String>("VD_Status")); // 2018.04.12 william  多VD登入新增開機狀態           
//        RDPStatusCol.setCellValueFactory(new PropertyValueFactory<MultiVD, String>("RDP_Status"));
        RDPStatusCol.setCellValueFactory(new Callback<CellDataFeatures<MultiVD, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<MultiVD, String> v) {
                MultiVD a  = v.getValue();
//                System.out.println( "getRDP_Status : " + a.getRDP_Status());
                return Bindings.createStringBinding(() -> a.getRDP_Status(), a.RDP_StatusProperty());                 
            }
        });  
       
       RDPStatusCol.setCellFactory(col -> new TableCell<MultiVD, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.toString());
                    if(item.equals(WordMap.get("Str_CannotConnect"))) {  // 2019.04.10 william  RDP狀態新增無法連線顏色                                      
                        setStyle("-fx-text-fill: " + "red" + ";");
                    } else {
                        setStyle("-fx-text-fill: " + "black" + ";");
                    }                    
                }
            }
        });      
    }
        
    
    // 我的最愛顯示方式
    public void tablelist_show(JsonArray jsondata) {        
        // 2018.11.11 新增AcroDesk & RDP Protocol 
        jsondataTemp = jsondata;
        // MultiVD[] _array = new MultiVD[jsondata.size()];
        // 2019.01.02 view D Drive
        MultiVD[] _array; 
        if(!GB.IsViewDisk)        
            _array = new MultiVD[jsondata.size()];
        else
            _array = new MultiVD[1];        
        int count = 1;
        int connect_type = 0;
        ProtocolType _ProtocolType = null;
        String RDPStatus = "";
        int _protocol = 0;    
        // 2018.12.04 新增VD&RDP狀態判斷
        JsonArray jsonArrForRDP = null;
        String RDPIP = "";
        String RDPPort = ""; // 2018.12.07 RDP ping ip and port
        boolean IsPingOK = false;        
        boolean IsCannotConnect = false; // 2018.12.12 新增 431NoVGA 和第一次開機
        
        // 2019.01.17 相容性處理
        boolean QXL = false;
        int VGACount = 0;
        int RDPFirst = 0;        
        int _rdpArraySize = 0;
        
        // 2019.04.16 修改協定判斷邏輯
        int AvailableProtocol = 0;
        
        System.out.println("Json Data : " + jsondata);
        
        for (int i = 0; i < jsondata.size(); i++) {
            term      = jsondata.get(i).getAsJsonObject();            
            VdName    = term.get("VdName").getAsString();
            
            // 2019.01.02 view D Drive
            System.out.println("GB._viewDiskVDName : " + GB._viewDiskVDName + " ,GB.IsViewDisk: " + GB.IsViewDisk);
            if(GB.IsViewDisk) {
                if(jsondata.size() > 1) {
                    if(!GB._viewDiskVDName.equals(VdName))
                        continue;                
                }
            }            
            
            VdId      = term.get("VdId").getAsString();
            VdDesc    = term.get("Desc").toString();
            VdStatus  = VDStatusName(term.get("VdStatus").toString(), vdStatusCol); // 2018.04.12 william  多VD登入新增開機狀態   
            // 2018.12.04 新增VD&RDP狀態判斷            
            IsCannotConnect = false; // 2018.12.12 新增 431NoVGA 和第一次開機
            
            if(GB.RDPEnable) { // 2019.01.24 New 5.1.X            
                            // 2019.01.17 相容性處理             
                try {
                    jsonArrForRDP = term.getAsJsonArray("RDP");
                    _rdpArraySize = jsonArrForRDP.size();
                    for(int j = 0; j < jsonArrForRDP.size(); j++) {
                        // 2018.12.07 RDP ping ip and port
                        // RDPIP = jsonArrForRDP.get(j).getAsString();
                        RDPIP = jsonArrForRDP.get(j).getAsJsonObject().get("IP").getAsString();
                        RDPPort = jsonArrForRDP.get(j).getAsJsonObject().get("Port").getAsString();
                        if(IsPingOK = PingIP.isReachableByTcp(RDPIP, Integer.parseInt(RDPPort), 1000))
                            break;
                    }
                } catch (Exception ex) {
                    IsPingOK = false;  
                    _rdpArraySize = 0;
                } 

                // 2019.01.17 相容性處理
                try {
                    _protocol = term.get("Protocol").getAsInt(); 
                } catch (Exception ex) {
                    _protocol = 0;    
                }                    

                // 2019.01.17 相容性處理
                try {
                    QXL = term.get("QXL").getAsBoolean();
                    VGACount = term.get("VGACount").getAsInt();
                    RDPFirst = term.get("RDPFirst").getAsInt();
                } catch (Exception ex) {
                    QXL = false;
                    VGACount = 0;
                    RDPFirst = 0;
                }            

                // 2019.04.16 修改協定判斷邏輯
                try {
                    AvailableProtocol = term.get("AvailableProtocol").getAsInt();
                    
                    if(AvailableProtocol == 1) { 
                        _ProtocolType = ProtocolType.AcroSpice;
                        connect_type = 0;
                    } else if(AvailableProtocol == 2) {
                        _ProtocolType = ProtocolType.AcroRDP;
                        connect_type = 1;
                    } else if(AvailableProtocol == 3) {
                        if(_protocol == 1 || _protocol == 0)
                            _ProtocolType = ProtocolType.AcroSpice;
                        else if(_protocol == 2)
                            _ProtocolType = ProtocolType.AcroRDP;
                        connect_type = 2;
                    }                        
                } catch (Exception ex) {
                    _ProtocolType = ProtocolType.AcroSpice;
                    connect_type = 0;
                }                   
            
                
                /*
                if(QXL && VGACount > 0) { 
                    _ProtocolType = ProtocolType.AcroSpice; // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP   
                    connect_type = 0;
                } else if(!QXL && VGACount > 0) {
                    _ProtocolType = ProtocolType.AcroRDP; // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP  
                    connect_type = 1;
                } else if(QXL && VGACount == 0) {
                    if(_protocol == 1 || _protocol == 0)
                        _ProtocolType = ProtocolType.AcroSpice;// 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP  
                    else if(_protocol == 2)
                        _ProtocolType = ProtocolType.AcroRDP; // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP  
                    connect_type = 2;
                }
                */
                
                
                // 2018.12.04 新增VD&RDP狀態判斷
                if(term.get("VdStatus").getAsInt() == 5) {
                    RDPStatus = WordMap.get("Str_Shutdown");                
                    lockStringType = 0; // 2018.12.12 新增 431NoVGA 和第一次開機
                } else if(term.get("VdStatus").getAsInt() == 1 && _rdpArraySize == 0) {
                    RDPStatus = WordMap.get("Str_Preparing");
                    lockStringType = 1; // 2018.12.12 新增 431NoVGA 和第一次開機
                } else if(term.get("VdStatus").getAsInt() == 1 && _rdpArraySize > 0 && IsPingOK) {
                    RDPStatus = WordMap.get("Str_Ready");                
                    lockStringType = 2; // 2018.12.12 新增 431NoVGA 和第一次開機
                } else if(term.get("VdStatus").getAsInt() == 1 && _rdpArraySize > 0 && !IsPingOK) {
                    RDPStatus = WordMap.get("Str_CannotConnect");
                    IsCannotConnect = true; // 2018.12.12 新增 431NoVGA 和第一次開機
                } else if(term.get("VdStatus").getAsInt() == 10) {
                    RDPStatus = WordMap.get("Str_Preparing");
                    lockStringType = 0; // 2018.12.12 新增 431NoVGA 和第一次開機
                }
            }            
   
                        
            dictVDStatus.put(VdName, RDPStatus);     
            
            // 2018.12.12 新增 431NoVGA 和第一次開機
            if(GB.RDPEnable) { // 2019.01.24 New 5.1.X
                dictRDPFirst.put(VdName, RDPFirst); // 2018.18.18 new create & reborn
            }             

            dictKVMVDStatus.put(VdName, term.get("VdStatus").getAsInt());
            dictLockType.put(VdName, lockStringType);
            dictCannotConnect.put(VdName, IsCannotConnect);
            // 2019.03.28 VM/VD migration reconnect
            dict3DType.put(VdName, connect_type);
            dictSelectProtocol.put(VdName, _ProtocolType);            
            
            // System.out.println("VdName: " + VdName + " QXL: " + term.get("QXL").getAsBoolean() + " VGA: " + term.get("VGACount").getAsInt());
            
            if( VdDesc == null ){
                VdDesc = "";
            }
            if("null".equals(VdDesc)){
                VdDesc = "";
            } else {
                VdDesc = term.get("Desc").getAsString(); 
            }            
            
            
            IsDefault = term.get("IsDefault").toString();
            if(IsDefault.equals("true")) {
                setVDName(VdName);
                System.out.println("******************* first VD Name :" + getVDName() + "******************* \n");
//                 vd_data.add(new MultiVD(VdName, VdId, VdDesc, VdStatus, null)); // 2018.04.12 william  多VD登入新增開機狀態 

                if(!GB.RDPEnable) { // 2019.01.24 New 5.1.X
                    _array[0] = new MultiVD(VdName, VdId, VdDesc, VdStatus, ProtocolType.AcroRDP, WordMap.get("Str_Shutdown"), connect_type, _protocol);
                } else {
                    _array[0] = new MultiVD(VdName, VdId, VdDesc, VdStatus, _ProtocolType, RDPStatus, connect_type, _protocol);
                }                
            } else {
                // vd_data.add(new MultiVD(VdName, VdId, VdDesc, VdStatus)); 
                if(!GB.RDPEnable) { // 2019.01.24 New 5.1.X
                    if(jsondata.size() > 1) {
                        
                        // 2019.01.30 UserDisk快照顯示異常問題修正
                        if(GB.IsViewDisk) {
                            setVDName(VdName);
                        }                           
                        
                        // 2019.01.02 view D Drive
                        if(GB.IsViewDisk) {
                            _array[0] = new MultiVD(VdName, VdId, VdDesc, VdStatus, ProtocolType.AcroRDP, WordMap.get("Str_Shutdown"), connect_type, _protocol);
                        } else {
                            _array[count] = new MultiVD(VdName, VdId, VdDesc, VdStatus, ProtocolType.AcroRDP, WordMap.get("Str_Shutdown"), connect_type, _protocol);
                        }
                    } else {                
                        setVDName(VdName);
                        _array[0] = new MultiVD(VdName, VdId, VdDesc, VdStatus, ProtocolType.AcroRDP, WordMap.get("Str_Shutdown"), connect_type, _protocol);
                    }                                   
                } else {
                    if(jsondata.size() > 1) {    
                        
                        // 2019.01.30 UserDisk快照顯示異常問題修正
                        if(GB.IsViewDisk) {
                            setVDName(VdName);
                        }                         
                        
                        // 2019.01.02 view D Drive
                        if(GB.IsViewDisk) {
                            _array[0] = new MultiVD(VdName, VdId, VdDesc, VdStatus, _ProtocolType, RDPStatus, connect_type, _protocol);
                        } else {
                            _array[count] = new MultiVD(VdName, VdId, VdDesc, VdStatus, _ProtocolType, RDPStatus, connect_type, _protocol);
                        }
                    } else {                
                        setVDName(VdName);
                        _array[0] = new MultiVD(VdName, VdId, VdDesc, VdStatus, _ProtocolType, RDPStatus, connect_type, _protocol);
                    }                                
                }
                count++;
            }           
        }       
        vd_data.addAll(_array);   
        _checkarray = _array;               
    }
    // 產生列表按鈕 // 2018.03.28 william  多VD登入新增登入按鈕 
    public void CreateTableBtn (TableColumn<MultiVD, String> ActionBtnCol) {
        Callback<TableColumn<MultiVD, String>, TableCell<MultiVD, String>> cellFactory = new Callback<TableColumn<MultiVD, String>, TableCell<MultiVD, String>>() {
            @Override
            public TableCell call(final TableColumn<MultiVD, String> param) {
                final TableCell<MultiVD, String> cell = new TableCell<MultiVD, String>() {

                    final Button btn = new Button(WordMap.get("Message_Login"));

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.getStyleClass().add("table-btn");
                            btn.setOnAction(event -> {
//                                this.btn.getStyleClass().add("table-table-btn_favoriteVD_check");
                                
                                MultiVD sv = getTableView().getItems().get(getIndex());
                                                                  
//                                Login_lock();
                                //Select_VdName = (String) vd_table.getSelectionModel().getSelectedItem().getVD_Name();
                                Select_VdName = sv.getVD_Name().toString();
                                System.out.print("*****************************************" + Select_VdName + "*****************************************\n");
                                Login_lock_thr llthr2 = new Login_lock_thr();
                                Thread thr = new Thread(llthr2);
                                thr.start();
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };

        ActionBtnCol.setCellFactory(cellFactory);       
    }
    // RadioButton 實作
    private class RadioButtonCell<S, T> extends TableCell<S, T> {
       private RadioButton radioButton;
       private ObservableValue<T> ov;
       private RadioButtonCell() {            
            radioButton = new RadioButton();
            radioButton.setToggleGroup(group);
            setGraphic(radioButton);
        }               
       
        @Override
        protected void updateItem(T item, boolean empty) {        
            int now_index = getIndex(); // 刷畫面後radio的index
            MultiVD now_vd;
            super.updateItem(item, empty);
            if(!empty){ // 2018.04.12 william  多VD登入新增開機狀態  
                radioButton.setUserData(now_index);
                 now_vd = (MultiVD)vd_table.getItems().get(now_index);
                 sorting_vdname = now_vd.getVD_Name();                
//                if(selectIndex == -1) { // 判斷是否為第一次
//                    
//                    now_vd = (MultiVD)vd_table.getItems().get(now_index);
//                    
//                } else {
                    if(now_index != selectIndex) // 如果刷畫面後radio的index不是為被選擇的Radio的Index(selectIndex)就設為radio不選取，反之為選取
                        radioButton.setSelected(false);
                    else
                        radioButton.setSelected(true);

                    if(favoriteVD_VdName != null && favoriteVD_VdName == sorting_vdname) {
                        radioButton.setSelected(true);
                    } else {
                        radioButton.setSelected(false);
                    }                    
                    
//                }
            } else {
                // 如果沒有資料的欄位就設為空
                setText(null);
                setGraphic(null);
            }
        }        
    }     
    // 畫面下層 (描述及按鈕)
    public final void bottom_layout() {
        // 建立Label物件（描述）
        LoginMultiVD_desc1 = new Label(WordMap.get("LoginMultiVD_desc1"));
        LoginMultiVD_desc1.setId("List_label");
        LoginMultiVD_desc2 = new Label(WordMap.get("LoginMultiVD_desc2"));
        LoginMultiVD_desc2.setId("List_label");
        
        Desc = new VBox();
	Desc.setSpacing(10);
        Desc.setTranslateY(18); // 2018.04.12 william  多VD登入新增開機狀態 20
        Desc.setTranslateX(39);  // 2018.04.12 william  多VD登入新增開機狀態 18
        Desc.setAlignment(Pos.CENTER_LEFT);
        Desc.setPadding(new Insets(0, 0, 0, 0));        
        Desc.getChildren().addAll(LoginMultiVD_desc1, LoginMultiVD_desc2);      
        
        MainGP.add(Desc,0,3);                
        
        // Login 2018.03.28 william  多VD登入新增登入按鈕        
        
        switch (WordMap.get("SelectedLanguage")) {
            case "English":
                Btn_login = new Button("Confirm");
                Btn_login.setPrefSize(100, btn_height);                  // 強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
                Btn_login.setMaxSize(100, btn_height);                   // 強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
                Btn_login.setMinSize(100, btn_height);                   // 強制限制button最小寬度/高度,讓每個button在變換語言時不會變動
                break;
            case "SimpleChinese":
                Btn_login = new Button("确认");
                Btn_login.setPrefSize(btn_width, btn_height);                  
                Btn_login.setMaxSize(btn_width, btn_height);                   
                Btn_login.setMinSize(btn_width, btn_height);                   
                break;
            case "TraditionalChinese":
                Btn_login = new Button("確認");
                Btn_login.setPrefSize(btn_width, btn_height);                  
                Btn_login.setMaxSize(btn_width, btn_height);                   
                Btn_login.setMinSize(btn_width, btn_height);                   
                break;          
            case "Japanese":
                Btn_login = new Button("確認する");
                Btn_login.setPrefSize(100, btn_height);                  
                Btn_login.setMaxSize(100, btn_height);                   
                Btn_login.setMinSize(100, btn_height);                   
                break;                  
            default:
                Btn_login = new Button("Confirm");
                Btn_login.setPrefSize(btn_width, btn_height);                  
                Btn_login.setMaxSize(btn_width, btn_height);                   
                Btn_login.setMinSize(btn_width, btn_height);                   
                break;
        }     

        // Btn_exit.setTranslateY(-27);
        Btn_login.setId("LoginMultiVD_Exit");
        Btn_login.setOnAction((ActionEvent event) -> {    
            if(!connectFlag) { // 2018.12.12 新增 431NoVGA 和第一次開機    
                connectFlag = true; // 2018.12.12 新增 431NoVGA 和第一次開機
                Select_VdName = getVDName();
                
                // 2019.01.30 UserDisk快照顯示異常問題修正
                if(GB.IsViewDisk) {
                    if(Select_VdName == null) {
                        Select_VdName = GB._viewDiskVDName;
                    }
                }                  
                                
                if(Boolean.parseBoolean(dictCannotConnect.get(Select_VdName).toString()) && "AcroRDP".equals(dictSelectProtocol.get(Select_VdName).toString())) { // 2019.03.28 VM/VD migration reconnect getProtocol() -> dictSelectProtocol.get(Select_VdName).toString()
                    CannotConnectCmd();
                } else if(Integer.parseInt(dictKVMVDStatus.get(Select_VdName).toString()) == 6 || Integer.parseInt(dictKVMVDStatus.get(Select_VdName).toString()) == 7) { // 2018.12.27  其他中斷連線狀態 - Crash & Migrate
                    int t = -100;
                    if(Integer.parseInt(dictKVMVDStatus.get(Select_VdName).toString()) == 6)                        
                        t = -11;
                    else if(Integer.parseInt(dictKVMVDStatus.get(Select_VdName).toString()) ==7)
                        t = -12;

                    DisconnectCmd(t);
                } else {
                    ConnectCmd(Select_VdName);
                }                             
            }                          
        });         
        
        // 建立按鈕物件(離開)
        switch (WordMap.get("SelectedLanguage")) {
            case "English":
                Btn_exit = new Button("Exit");
                break;
            case "SimpleChinese":
                Btn_exit = new Button("离开");                
                break;
            case "TraditionalChinese":
                Btn_exit = new Button("離開");                
                break;    
            case "Japanese":
                Btn_exit = new Button("立ち去る");
                break;                   
            default:
                Btn_exit = new Button("Exit");             
                break;
        }     
        
        if("Japanese".equals(WordMap.get("SelectedLanguage"))) {
            Btn_exit.setPrefSize(100, btn_height);                 
            Btn_exit.setMaxSize(100, btn_height);                  
            Btn_exit.setMinSize(100, btn_height);                  
        } else {
            Btn_exit.setPrefSize(btn_width, btn_height);                  // 強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
            Btn_exit.setMaxSize(btn_width, btn_height);                   // 強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
            Btn_exit.setMinSize(btn_width, btn_height);                   // 強制限制button最小寬度/高度,讓每個button在變換語言時不會變動
        }         
        

//        Btn_exit.setTranslateY(-27);
//        Btn_exit.setTranslateX(15);
        Btn_exit.setId("LoginMultiVD_Exit");
        Btn_exit.setOnAction((ActionEvent event) -> {
            GB.ExitFlag = true; // 2018.11.11 新增AcroDesk & RDP Protocol
            StopAll(); // 2018.12.04 新增VD&RDP狀態判斷
            MultiVDList_secondStage.close();
        });              

        btn = new HBox();
	btn.setSpacing(20);
        btn.setAlignment(Pos.CENTER_RIGHT);
        btn.setPadding(new Insets(0, 0, 0, 20));
        if("Japanese".equals(WordMap.get("SelectedLanguage"))) {
            btn.setTranslateY(-20); 
        } else {
            btn.setTranslateY(-31); 
        }        
        
        btn.setTranslateX(-5); // 2018.04.12 william  多VD登入新增開機狀態 12
        btn.getChildren().addAll(Btn_exit, Btn_login);   
		
        MainGP.add(btn,0,4);  
    }    
    // 畫面鎖定
    public void Login_lock(String _type, String _status, int rdpfirst, int _vdstatus) { // 2018.12.12 新增 431NoVGA 和第一次開機 // 2018.18.18 new create & reborn
        LoginGP = new GridPane();              
        LoginGP.setAlignment(Pos.CENTER);       
        LoginGP.setPadding(new Insets(35, 50, 50, 20));
        LoginGP.setHgap(15); //元件間的水平距離
        LoginGP.setVgap(0); //元件間的垂直距離 // 2018.12.12 新增 431NoVGA 和第一次開機        
        if("Japanese".equals(WordMap.get("SelectedLanguage"))) {
            LoginGP.setPrefSize(vd_tableView_width-20, vd_tableView_height);  
            LoginGP.setMaxSize(vd_tableView_width-20, vd_tableView_height);  
            LoginGP.setMinSize(vd_tableView_width-20, vd_tableView_height);  
            LoginGP.setTranslateX(-10);
        } else {
            LoginGP.setPrefSize(vd_tableView_width-40, vd_tableView_height);  // 2018.04.12 william  多VD登入新增開機狀態
            LoginGP.setMaxSize(vd_tableView_width-40, vd_tableView_height);  // 2018.04.12 william  多VD登入新增開機狀態
            LoginGP.setMinSize(vd_tableView_width-40, vd_tableView_height);  // 2018.04.12 william  多VD登入新增開機狀態
            LoginGP.setTranslateX(0);
        }            
        
        LoginGP.setTranslateY(-15);
        
        // 2018.12.12 新增 431NoVGA 和第一次開機        
//        LoginGP.setGridLinesVisible(true); //開啟或關閉表格的分隔線       
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
        // 2018.11.30 新增等待中文字動態顯示
        LoginGP.setStyle("-fx-background-color: #EEF5F5 ; -fx-opacity: 0.7 ; -fx-background-radius: 4;-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 10, 0.0 , 0 , 5 );");    // 2018.18.18 new create & reborn 
        // 2018.12.12 新增 431NoVGA 和第一次開機        
        if(rdpfirst == 1 || rdpfirst == 2) { // 2018.18.18 new create & reborn 
            if("AcroSpice".equals(_type)) { // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP  
                Login_title = new Label(WordMap.get("Thread_Connecting"));  
                Login_title2 = new Label(WordMap.get("Thread_Waiting"));                
            } else if ("AcroRDP".equals(_type)) { // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP  
                // 2018.18.18 new create & reborn
                Login_title = new Label("");            
                Login_title2 = new Label(WordMap.get("Thread_Booting"));
                ChangeLabelLang(Login_title, Login_title2, 1);
            }                                          
        } else { 
            if("AcroSpice".equals(_type)) { // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP  
                Login_title = new Label(WordMap.get("Thread_Connecting"));   
                Login_title2 = new Label(WordMap.get("Thread_Waiting"));
            }
            // 2018.18.18 new create & reborn
                        
            else if ("AcroRDP".equals(_type) && (_vdstatus == 10  || _status.equals(WordMap.get("Str_Shutdown")))) { // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP  
                Login_title = new Label(WordMap.get("Message_RDP_Booting"));   
                Login_title2 = new Label(WordMap.get("Thread_Booting"));          
                ChangeLabelLang(Login_title, Login_title2, 1);
            } else if ("AcroRDP".equals(_type) && _status.equals(WordMap.get("Str_Preparing"))) { // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP  
                Login_title = new Label(WordMap.get("Message_RDP_Booting")); 
                Login_title2 = new Label(WordMap.get("Message_RDP_Preparing"));
                ChangeLabelLang(Login_title, Login_title2, 1);
            } else if ("AcroRDP".equals(_type) && _status.equals(WordMap.get("Str_Ready"))) {  // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP   
                Login_title = new Label("");                    
                Login_title2 = new Label(WordMap.get("Message_RDP_Preparing"));
                ChangeLabelLang(Login_title, Login_title2, 1);
            } else {

            }    
            
        }        
        
        Login_title.setAlignment(Pos.CENTER);        
        Login_title2.setAlignment(Pos.CENTER);        
        Login_title3.setAlignment(Pos.CENTER);
        Login_title3.setStyle("-fx-font-family:'Ubuntu';");
        Login_title.setId("Thread_status");
        Login_title2.setId("Thread_status");
        Login_title3.setId("Thread_status");
        p1 = new ProgressIndicator();
             
        HBox FirstRow = new HBox();
        FirstRow.setAlignment(Pos.CENTER);
        FirstRow.setTranslateY(0); // 2018.18.18 new create & reborn
        FirstRow.setTranslateX(20);
        FirstRow.getChildren().addAll(Login_title);
        LoginGP.add(FirstRow, 0, 0);        
        
        HBox S1 = new HBox();
        S1.setAlignment(Pos.CENTER);    
        S1.setTranslateX(10);
        S1.getChildren().addAll(Login_title2);
        
        HBox S2 = new HBox();                
        S2.setAlignment(Pos.CENTER_LEFT);
        S2.setTranslateY(-5);
        S2.setTranslateX(20);
        S2.getChildren().addAll(Login_title3);
        S2.setPrefWidth(20);
        S2.setMaxWidth(20);
        S2.setMinWidth(20);
        
        HBox SecondRow = new HBox();
        SecondRow.setAlignment(Pos.CENTER);
        SecondRow.setSpacing(0);        
        SecondRow.setTranslateY(15);    // 2018.18.18 new create & reborn
        SecondRow.setTranslateX(20);        
        SecondRow.getChildren().addAll(S1, S2);
        LoginGP.add(SecondRow, 0, 1);        
        
        HBox ThirdRow = new HBox();
        ThirdRow.setAlignment(Pos.CENTER);
        ThirdRow.setTranslateY(15);    // 2018.18.18 new create & reborn
        ThirdRow.setTranslateX(20);
        ThirdRow.getChildren().addAll(p1);
        LoginGP.add(ThirdRow, 0, 2);        
        
        
        Button _cancelBtn = new Button(WordMap.get("Cancel"));
        if("Japanese".equals(WordMap.get("SelectedLanguage"))) {
            _cancelBtn.setPrefSize(110, btn_height);
            _cancelBtn.setMaxSize(110, btn_height);
            _cancelBtn.setMinSize(110, btn_height);
            _cancelBtn.setTranslateX(430);
        } else {
            _cancelBtn.setPrefSize(btn_width, btn_height);
            _cancelBtn.setMaxSize(btn_width, btn_height);
            _cancelBtn.setMinSize(btn_width, btn_height);
            _cancelBtn.setTranslateX(285);
        }             

        
        _cancelBtn.setTranslateY(10);
        _cancelBtn.setId("LoginMultiVD_Exit");
        
        if("English".equals(WordMap.get("SelectedLanguage"))) {
            _cancelBtn.setStyle("-fx-font-family:'Times New Roman';");             
        } 
        else { //else if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))) {
            _cancelBtn.setStyle("-fx-font-family: '微軟正黑體';");            
            _cancelBtn.setStyle("-fx-font-family: 'Microsoft JhengHei';");          
        }          

        _cancelBtn.requestFocus();
        _cancelBtn.setOnAction((event) -> {
            rootPane.setCenter(MainGP);    
            timer.stop();  
            TextTimer.stop();
            TextTimer2.stop();      
            connectFlag = false;
            GB.stopconnect = true;
            killthread = true;                                    
        });       
        
        HBox _hbox = new HBox();
        _hbox.setPadding(new Insets(1, 0, 0, 0));
        _hbox.getChildren().addAll(_cancelBtn);  
        _hbox.setAlignment(Pos.BOTTOM_CENTER);
        _hbox.setSpacing(10);     
        LoginGP.add(_hbox, 0, 9);        
        
     
        
        /*
        Login_title.setAlignment(Pos.CENTER);
        Login_title.setTranslateY(0);
        Login_title.setId("Thread_status");
        LoginGP.add(Login_title,0,0);

        p1 = new ProgressIndicator();
        p1.setTranslateY(10);
        //p1.setStyle("-fx-foreground-color: #1A237E");
        LoginGP.add(p1,0,1); //2,1是指這個元件要佔用2格的column和1格的row 
        */        
        stack = new StackPane();
        stack.getChildren().addAll(MainGP, LoginGP);      
        rootPane.setCenter(stack);
    }    
    
    // 2018.12.12 新增 431NoVGA 和第一次開機
    public void ChangeLabelLang(Label lab01, Label lab02, int _type) {
        //Label "IP", "UserName", "Password", "Language"
        if(_type == 1) {
            if("English".equals(WordMap.get("SelectedLanguage"))) {
                lab01.setStyle("-fx-font-family:'Times New Roman'; -fx-font-size: 22px; -fx-font-weight: 900;");//21
                lab02.setStyle("-fx-font-family:'Times New Roman'; -fx-font-size: 30px; -fx-font-weight: 900;"); // 2018.18.18 new create & reborn

            }        
            else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))) {        
                lab01.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 27px; -fx-font-weight: 900;");//20
                lab02.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 35px; -fx-font-weight: 900;");                   
            }        
        } else if(_type == 2) {
            if("English".equals(WordMap.get("SelectedLanguage"))) {
                lab01.setStyle("-fx-font-family:'Times New Roman'; -fx-font-size: 30px; -fx-font-weight: 900;");//21
                lab02.setStyle("-fx-font-family:'Times New Roman'; -fx-font-size: 30px; -fx-font-weight: 900;");

            }        
            else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))) {        
                lab01.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-size: 30px; -fx-font-weight: 900;");//20
                lab02.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-size: 30px; -fx-font-weight: 900;");            
            }           
        }
       
     }
       
    // 畫面解除
    public void Login_Unlock() {
        Platform.runLater(() -> { 
            rootPane.setCenter(MainGP);            
            MultiVDList_secondStage.close();        
        });
        
        if(getProtocol()=="AcroSpice") {
            QM.params = new String [2];           
            QM.params[0] = "VirtViewer v3.1_64bit_wusb\\bin\\remote-viewer.exe";//64bit版
            QM.params[1] = QM.MultiVD_file.toString();                       
            try {  
                QM.process =Runtime.getRuntime().exec(QM.params);
            } catch (IOException ex) {
                Logger.getLogger(LoginMultiVD.class.getName()).log(Level.SEVERE, null, ex);
            }
            QM.process=null;        
        }
                   
        System.gc();//清除系統垃圾        
        
    }      
    /*------------------------  動作偵測 ------------------------*/ 
    public void detectSelection() {
        /* 偵測List View上所選擇的Cell     */
	VDList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed (ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // change the label text value to the newly selected item.
                System.out.println("clicked on " + VDList.getSelectionModel().getSelectedItem());                
            }
	}); 

//        vd_table.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//
//                if(mouseEvent.getClickCount() == 1) {
//                    // 2018.03.28 william  多VD登入新增登入按鈕 
//                    /*
//                    Login_lock();
//                    // System.out.println("clicked on " + vd_table.getSelectionModel().getSelectedItem().getVD_Name());
//                    Select_VdName = (String) vd_table.getSelectionModel().getSelectedItem().getVD_Name();
//                    Login_lock_thr llthr2 = new Login_lock_thr();
//                    Thread thr = new Thread(llthr2);
//                    thr.start();
//                    */
//                    
//                    name = (String) vd_table.getSelectionModel().getSelectedItem().getVD_Name();
//                    setVDName(name);
//                    System.out.println("clicked on :" + name);                    
//                } else if(mouseEvent.getClickCount() == 2) { // 2018.04.10  william  功能修改   
//                    vd_index = vd_table.getSelectionModel().getSelectedIndex();                    
//                    
//                    if(vd_index != -1) { 
//                        Login_lock();
//                        Select_VdName = (String) vd_table.getSelectionModel().getSelectedItem().getVD_Name();
//                        Login_lock_thr llthr2 = new Login_lock_thr();
//                        Thread thr = new Thread(llthr2);
//                        thr.start();
//                        // System.out.println("click on " + vd_index + "\n");
//                    }                                                                               
//                }
//                
//            }
//        });          
        
        vd_table.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){
               if(!connectFlag) { // 2018.12.12 新增 431NoVGA 和第一次開機 
                    connectFlag = true; // 2018.12.12 新增 431NoVGA 和第一次開機
                    Select_VdName = (String) vd_table.getSelectionModel().getSelectedItem().getVD_Name();
                    if(Boolean.parseBoolean(dictCannotConnect.get(Select_VdName).toString()) && "AcroRDP".equals(dictSelectProtocol.get(Select_VdName).toString())) { // 2019.03.28 VM/VD migration reconnect getProtocol() -> dictSelectProtocol.get(Select_VdName).toString()
                        CannotConnectCmd();
                    } else if(Integer.parseInt(dictKVMVDStatus.get(Select_VdName).toString()) == 6 || Integer.parseInt(dictKVMVDStatus.get(Select_VdName).toString()) == 7) { // 2018.12.27  其他中斷連線狀態 - Crash & Migrate
                        int t = -100;
                        if(Integer.parseInt(dictKVMVDStatus.get(Select_VdName).toString()) == 6)                        
                            t = -11;
                        else if(Integer.parseInt(dictKVMVDStatus.get(Select_VdName).toString()) ==7)
                            t = -12;
                        
                        DisconnectCmd(t);
                    } else {
                        ConnectCmd(Select_VdName);
                    }                       
               }
                                  
                event.consume();
           }                      
           if(event.getCode() == KeyCode.DOWN&&vd_table.getSelectionModel().isSelected(jsondata.size()-1)){
                Btn_exit.requestFocus();
                event.consume();
           }
           if(event.getCode() == KeyCode.ESCAPE){                
                StopAll(); // 2018.12.04 新增VD&RDP狀態判斷 
                GB.ExitFlag = true; // 2018.12.11 bug fix
                MultiVDList_secondStage.close();
                event.consume();
           } 
           if(event.getCode() == KeyCode.DOWN){              
                vd_table.getSelectionModel().selectNext();                                                                              
                setVDName(vd_table.getSelectionModel().getSelectedItem().getVD_Name());                    
                setSelectIndex(vd_table.getSelectionModel().getSelectedIndex());                                                                                    
                System.out.println("getVDName : " + getVDName() + " index : " + getSelectIndex());
                event.consume();
           }           
           if(event.getCode() == KeyCode.UP){               
                vd_table.getSelectionModel().selectPrevious();       
                setVDName(vd_table.getSelectionModel().getSelectedItem().getVD_Name());                    
                setSelectIndex(vd_table.getSelectionModel().getSelectedIndex());                                                                                    
                System.out.println("getVDName : " + getVDName() + " index : " + getSelectIndex());
                event.consume();
           }           
           
        });   
        
        Btn_exit.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){
               StopAll(); // 2018.12.04 新增VD&RDP狀態判斷              
               GB.ExitFlag = true; // 2018.12.11 bug fix
               MultiVDList_secondStage.close();
                event.consume();
           }                      
           if(event.getCode() == KeyCode.ESCAPE){                
               StopAll(); // 2018.12.04 新增VD&RDP狀態判斷              
               MultiVDList_secondStage.close();
               GB.ExitFlag = true; // 2018.12.11 bug fix
                event.consume();
           }           
        });         
        
    }
    /*------------------------  進入VD ------------------------*/ 
    public void SelectVDConn() { 
        JsonObject term;
        String VdName;
        String VdId;        
        // 2019.01.14 RDP is VD online
        boolean IsRDPOnline = false;     
        
        boolean EnableUSB = false;
        
        for (int i = 0; i < jsondata.size(); i++) {
            term   = jsondata.get(i).getAsJsonObject();
            VdName = term.get("VdName").getAsString();
            if (VdName.equals(getVDName())) {
                VdId       = term.get("VdId").getAsString();
                IsVdOnline = term.get("IsVdOnline").toString();
                IsVdOrg =  term.get("IsVdOrg").getAsBoolean(); // 2018.12.12 新增 431NoVGA 和第一次開機
                IsVdOnlineVdName = VdName;
                IsVdOnlineVdId = VdId;
                
                // 2019.03.28 VM/VD migration reconnect
                GB.migrationVDID = VdId;
                GB._3D_Type = Integer.parseInt(dict3DType.get(VdName).toString());
                GB.migrationApiIP = Address;
                GB.migrationApiPort = Port;
                GB.migrationUserName = CurrentUserName;
                GB.migrationPwd = CurrentPasseard;
                GB.migrationUkey = uniqueKey;
                GB.migrationIsVdOrg = IsVdOrg;                
                
                // 2019.01.14 RDP is VD online
                // 2019.01.17 相容性處理
                try {
                    IsRDPOnline = term.get("IsRDPOnline").getAsBoolean();
                } catch (Exception ex) {
                    IsRDPOnline = false;
                }
                
                try {
                    if(term.get("USBRedirCt").getAsInt() > 0)
                        EnableUSB = true;
                } catch (Exception ex) {
                    EnableUSB = true;
                }                             
                
                GB.EnableUSB = enableusb = EnableUSB;
                
                try {
                    QM.DoChcekServerAddressAvailabilityGet(Address,Port);
                    // 2018.11.11 新增AcroDesk & RDP Protocol 
                    // 增加Set VD RDP Online            
                    
                    System.out.println("************* VD Protocol Type ************* : " +dict.get(getVDName()));
                    
                    if(!GB.RDPEnable) { // 2019.01.24 New 5.1.X
                        setProtocol("AcroSpice");
                    } else {
                        setProtocol(dict.get(getVDName()).toString());
                    }
                    
                    if(QM.ChcekServerAddress_connCode == 200 && QM.checkaddr == false) {  
                        setVDID(VdId);
                        QM.checkaddr = true;
                        
                        // 2019.03.28 VM/VD migration reconnect
                        GB.migrationProtocol = getProtocol();                        
                                                       
                        switch (getProtocol()) {
                            case "AcroSpice": // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP   
                                if("false".equals(IsVdOnline)) {            
                                    QM.SetVDOnline(Address, VdId, CurrentUserName, CurrentPasseard, uniqueKey, Port);
                                    System.out.println("************* Login use  AcroSpice*************" + VdName);
                                }
                                break;
                            case "AcroRDP": // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP   
                                if(!IsRDPOnline) {    // 2019.01.14 RDP is VD online                                   
                                QM.SetVDRDPOnline(Address, VdId, CurrentUserName, CurrentPasseard, uniqueKey, Port, IsVdOrg, EnableUSB, GB.adAuth, GB.adDomain);  // 2018.12.12 新增 431NoVGA 和第一次開機
                                System.out.println("************* Login use  RDP*************" + VdName);
                                }                                
                                break;       
                            default: 
                                break;                              
                        } 
                                        
                    } else {
                        System.out.print("ChcekServerAddress_connCode = " + QM.ChcekServerAddress_connCode + " \n");
                    }                                
                } catch (Exception ex) {
                    Logger.getLogger(LoginMultiVD.class.getName()).log(Level.SEVERE, null, ex);
                }
                /*--------------------連線後處理 Start--------------------*/
                        
                if(QM.SetVDonline_connCode != 200) { // 2018.03.09  Bug修改，無法正確處理Error Code導致畫面卡住                  
                    Platform.runLater(() -> { 
                        // timer.stop();            
                        if(QM.SetVDonline_connCode != 0) {
                            StopPolling = true;//checkLogin_flag = true;  // 2018.12.11 bug fix                        
                            MultiVDList_secondStage.close();
                        }
                            
                        if("AcroRDP".equals(getProtocol()) && QM.SetVDonline_connCode == 428) {// 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP   
                            System.out.println("No Show Message");
                        } else {
                            LA.SetVDOnline_AlertChangeLang(QM.SetVDonline_connCode);
                        }                          

                    });                                                             
                }
                
                if(QM.ChcekServerAddress_connCode == 200 && getProtocol() == "AcroSpice"){ // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP
                    if(IsVdOnline.equals("true")) {
                        // https://stackoverflow.com/questions/17850191/why-am-i-getting-java-lang-illegalstateexception-not-on-fx-application-thread
                        Platform.runLater(() -> { 
                            try {
                                IsVdOnlineAlert();                  
                            } catch (Exception ex) {
                                Logger.getLogger(LoginMultiVD.class.getName()).log(Level.SEVERE, null, ex);
                            }                        
                        }); 
                    }
                }
                // 2019.01.14 RDP is VD online
                if(QM.ChcekServerAddress_connCode == 200 && getProtocol() == "AcroRDP") {                     
                    if(IsRDPOnline) {
                        Platform.runLater(() -> { 
                            try { IsVdOnlineAlert(); } catch (Exception ex) { Logger.getLogger(LoginMultiVD.class.getName()).log(Level.SEVERE, null, ex); }                        
                        }); 
                    }                    
                }                                                                 
                if(QM.Alert_spiceaddr==1) { 
                    Platform.runLater(() -> {
                        SetVDOlineAlert();
                    });   
                }                  
                try {
                    vd_IsDefault();
                } catch (IOException ex) {
                    Logger.getLogger(LoginMultiVD.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LoginMultiVD.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
        }     
    }    
    /*------------------------ thread ------------------------*/    
    public class Login_lock_thr implements Runnable {

        @Override
        public void run() {
           SelectVDConn();
        }
    }
    //  檢查是否登入完成 AnimationTimer
    public void CheckLogincompleted() {
        final LongProperty lastUpdate = new SimpleLongProperty();                            
        final long minUpdateInterval = 500000000 ; // nanoseconds. Set to higher number to slow output. / one second = 1,000 milliseconds = 1,000,000 microseconds = 1,000,000,000 nanoseconds

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate.get() > minUpdateInterval) {
//                    System.out.print("QM.checkLogin_flag:" + QM.checkLogin_flag + "\n");
                    if (QM.checkLogin_flag || checkLogin_flag) { 
                        StopAll(); // 2018.12.04 新增VD&RDP狀態判斷                                      
                        Login_Unlock();    
                    }                     
                    lastUpdate.set(now);
                }
            }
        };
        
    }    
    /*------------------------ 取值＆資料模型 ------------------------*/
    // 定義VD資料模型
    public class MultiVD {         
        private final SimpleStringProperty VD_Name;
        private final SimpleStringProperty VD_ID;
        private final SimpleStringProperty VD_Desc;
        private final SimpleStringProperty VD_Status; // 2018.04.12 william  多VD登入新增開機狀態
        private final SimpleObjectProperty<ProtocolType> option;
        private final SimpleStringProperty RDP_Status;
        private final SimpleIntegerProperty Connect_Type;
        private final SimpleIntegerProperty Protocol;        

        MultiVD(String vd_Name, String vd_ID, String vd_Desc, String vd_Status, ProtocolType _option, String rdp_Status, int _connect_Type, int _protocol) { // 2018.04.12 william  多VD登入新增開機狀態   , String _option
            this.VD_Name = new SimpleStringProperty(vd_Name);
            this.VD_ID   = new SimpleStringProperty(vd_ID);
            this.VD_Desc = new SimpleStringProperty(vd_Desc);
            this.VD_Status = new SimpleStringProperty(vd_Status); // 2018.04.12 william  多VD登入新增開機狀態          
            this.option = new SimpleObjectProperty(_option);
            this.RDP_Status = new SimpleStringProperty(rdp_Status);
            this.Connect_Type = new SimpleIntegerProperty(_connect_Type);
            this.Protocol = new SimpleIntegerProperty(_protocol);
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
        // vd description
        public String getVD_Desc() {
            return VD_Desc.get();
        }

        public void setVD_Desc(String vd_Desc) {
            VD_Desc.set(vd_Desc);
        }

        public StringProperty VD_DescProperty() {
            return VD_Desc;
        }    

        // vd status // 2018.04.12 william  多VD登入新增開機狀態   
        public String getVD_Status() {
            return VD_Status.get();
        }        
        
        public void setVD_Status(String vd_Status) {
            VD_Status.set(vd_Status);
        }    
        
        public StringProperty VD_StatusProperty() {
            return VD_Status;
        }       
        
         // 2018.11.11 新增AcroDesk & RDP Protocol 
        public ProtocolType getOption() {
            return option.get();
        }

        public void setOption(ProtocolType value) {
            option.set(value);
        }

        public SimpleObjectProperty<ProtocolType> optionProperty() {
            return option;
        }       
        
        public String getRDP_Status() {
            return RDP_Status.get();
        }        
        
        public void setRDP_Status(String rdp_Status) {
            RDP_Status.set(rdp_Status);
        }    
        
        public StringProperty RDP_StatusProperty() {
            return RDP_Status;
        }       
                
        public Integer getConnect_Type() {
            return Connect_Type.get();
        }

        public void setConnect_Type(Integer Type) {
            Connect_Type.set(Type);
        }

        public IntegerProperty Connect_TypeProperty() {
            return Connect_Type;
        }     
        
        public Integer getProtocol() {
            return Protocol.get();
        }        
        
        public void setProtocol(Integer _protocol) {
            Protocol.set(_protocol);
        }
       
        public IntegerProperty ProtocolProperty() {
            return Protocol;
        }             
        
    }     
    /*------------------------ 跳出視窗詢問 是否還要連線 ------------------------*/
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
//        DialogPane dialogPane = alert.getDialogPane();
//        dialogPane.getStylesheets().add("myDialogs.css");
//        dialogPane.getStyleClass().add("myDialog");
//
//        alert.initModality(Modality.APPLICATION_MODAL);
//        alert.initOwner(public_stage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
        
        Bounds mainBounds = public_stage.getScene().getRoot().getLayoutBounds();       
        alert.setX(public_stage.getX() + (mainBounds.getWidth() - 500 ) / 2);  //5.7
        alert.setY(public_stage.getY() + (mainBounds.getHeight() - 200 ) / 2);  //5
        
        Optional<ButtonType> result01 = alert.showAndWait();
            if (result01.get() == buttonTypeOK){                    
                // 2018.11.11 新增AcroDesk & RDP Protocol 
                // 增加Set VD RDP Online                   
                    if(!GB.RDPEnable) { // 2019.01.24 New 5.1.X
                        setProtocol("AcroSpice");
                    } else {
                        setProtocol(dict.get(IsVdOnlineVdName).toString());
                    }   
//                if("false".equals(IsVdOnline)) {        
                    switch (getProtocol()) {
                        case "AcroSpice": // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP
                            new Thread(new Runnable() {                
                                @Override
                                public void run() {
                                    try {
                                        QM.SetVDOnline(Address, IsVdOnlineVdId, CurrentUserName, CurrentPasseard, uniqueKey, Port); // VdId
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
                                        QM.SetVDRDPOnline(Address, IsVdOnlineVdId, CurrentUserName, CurrentPasseard, uniqueKey, Port, IsVdOrg, enableusb, GB.adAuth, GB.adDomain); // 2018.12.12 新增 431NoVGA 和第一次開機
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
                    
                    
//                }   
            } else {
                alert.close();
                checkLogin_flag = true;  
            }
     
    }
    /*------------------------ 伺服器無法連線 錯誤訊息 ------------------------*/
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
        checkLogin_flag = true;
        //alert_error.show();       
    }      
    /*------------------------ 寫檔案及讀取檔案 ------------------------*/
    //  讀取檔案
    public void LoadMyfavoriteStatus() throws ParseException {
        try {
            File myFile = new File("jsonfile/Myfavorite_" + CurrentUserName + "_" + Address + ".json");

            if(myFile.exists()) {
                String FilePath       = myFile.getPath();
                List<String> JSONLINE = Files.readAllLines(Paths.get(FilePath));
                String JSONCode       = "";
                JSONCode              = JSONLINE.stream().map((s) -> s).reduce(JSONCode, String::concat);
                JSONParser Jparser    = new JSONParser();
                favoriteVD            = (JSONObject) Jparser.parse(JSONCode);                
                favoriteVD_username   = favoriteVD.get("favoriteVD_username").toString();                
                favoriteVD_IP         = favoriteVD.get("favoriteVD_IP").toString();                
                favoriteVD_VdName     = favoriteVD.get("favoriteVD_VdName").toString();                
            } else {
                favoriteVD_username   = "";
                favoriteVD_IP         = "";
                favoriteVD_VdName     = ""; 
            }
        }
        catch(Exception e) { // 2017.11.09 william Json檔案讀寫失敗或錯誤的處理，將IOException 改為通用型例外 Exception
            favoriteVD_username   = "";
            favoriteVD_IP         = "";
            favoriteVD_VdName     = ""; 
            System.out.print("Myfavorite.json read failed \n");
           // e.printStackTrace();
        }
    }    
    // 寫檔案
    public void WriteMyfavoriteStatusChange() {
        favoriteVD = new JSONObject();
        favoriteVD.put("favoriteVD_username", CurrentUserName); 
        favoriteVD.put("favoriteVD_IP"      , Address);
        favoriteVD.put("favoriteVD_VdName"  , favoriteVD_VdName); 
        
        try {
            File myFile = new File("jsonfile/Myfavorite_" + CurrentUserName + "_" + Address + ".json");
            if(myFile.exists()) {
                myFile.delete();
                myFile = null;
            }
            try(FileWriter JsonWriter = new FileWriter("jsonfile/Myfavorite_" + CurrentUserName + "_" + Address + ".json")) {
                JsonWriter.write(favoriteVD.toJSONString());
                JsonWriter.flush();
                JsonWriter.close(); 
            }   
        }
        catch(Exception e) {
           // e.printStackTrace();
        }
    }           
    // 我的最愛API     
    public void  vd_IsDefault() throws MalformedURLException, IOException, InterruptedException {     
        System.out.println("我的最愛 VD ID : "  + getVDID() + "\n");                  
        try {
            url  = new URL("https://" + Address + ":" + Port + "/vdi/user/vd/" + getVDID() + "/default");          
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

            conn.setConnectTimeout(10000);   
            conn.connect();
            
            System.out.println("我的最愛 code : "  +conn.getResponseCode());            
            
            conn.disconnect();   
            /*----------------------------content----------------------------*/
//            try (OutputStream ops = conn.getOutputStream()) {
//                ops.flush();
//                ops.close();
//                System.out.println("IsDefault -output out : "  + ops + "\n");                  
//                System.out.println("IsDefault -URL         : " + url + "\n");             
//                System.out.println("IsDefault -Resp Code   : " + conn.getResponseCode() + "\n");
//                System.out.println("IsDefault -Resp Message: " + conn.getResponseMessage() + "\n");            
//                   
//            }                                 
        } catch (MalformedURLException e) {

        } catch (IOException e) {    

        }         

    }
    
    public static synchronized String getVDID() {
        return Vd_Id;
    }

    public static synchronized void setVDID(String vdid) {
        Vd_Id = vdid;
    }     
    
    public static synchronized String getVDName() {
        return Vd_Name;
    }

    public static synchronized void setVDName(String name) {
        Vd_Name = name;
    }         
    
       /****變更Label字型****字型:中文-標楷體,英文:Times New Roman****/ // 2018.04.10  william  功能修改    // 2018.04.12 william  多VD登入新增開機狀態   
    public void ChangeLabelLang(Label title, Label Label01, Label Label02, TableColumn tc00, TableColumn tc01, TableColumn tc02, TableColumn tc03, TableColumn tc04, TableColumn tc05, TableColumn tc06){
        //Label "title", "TableColumn"
        if("English".equals(WordMap.get("SelectedLanguage"))){
            title.setStyle("-fx-font-family:'Times New Roman';"); //-fx-font-size: 17px;
            Label01.setStyle("-fx-font-family:'Times New Roman';"); //-fx-font-size: 17px;
            Label02.setStyle("-fx-font-family:'Times New Roman';"); //-fx-font-size: 17px;
            tc00.setStyle("-fx-font-family:'Times New Roman';");
            tc01.setStyle("-fx-font-family:'Times New Roman';");
            tc02.setStyle("-fx-font-family:'Times New Roman';-fx-alignment: center-left;");
            tc03.setStyle("-fx-font-family:'Times New Roman';-fx-alignment: center-left;");
            tc04.setStyle("-fx-font-family:'Times New Roman';");
            tc05.setStyle("-fx-font-family:'Times New Roman';");
            tc06.setStyle("-fx-font-family:'Times New Roman';");            
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/
            //winXP 僅能用中文名稱
            title.setStyle("-fx-font-family: '微軟正黑體';");//-fx-font-weight: 900;
            Label01.setStyle("-fx-font-family: '微軟正黑體';");//-fx-font-weight: 900;
            Label02.setStyle("-fx-font-family: '微軟正黑體';");//-fx-font-weight: 900;            
            tc00.setStyle("-fx-font-family: '微軟正黑體';");//;-fx-font-weight: 700; 
            tc01.setStyle("-fx-font-family: '微軟正黑體'; ");
            tc02.setStyle("-fx-font-family: '微軟正黑體';-fx-alignment: center-left; ");
            tc03.setStyle("-fx-font-family: '微軟正黑體';-fx-alignment: center-left; ");
            tc04.setStyle("-fx-font-family: '微軟正黑體'; ");
            tc05.setStyle("-fx-font-family: '微軟正黑體'; ");
            tc06.setStyle("-fx-font-family: '微軟正黑體'; ");            
            
            //win7以上僅能用英文名稱
            title.setStyle("-fx-font-family: 'Microsoft JhengHei'; ");
            Label01.setStyle("-fx-font-family: 'Microsoft JhengHei'; ");
            Label02.setStyle("-fx-font-family: 'Microsoft JhengHei'; ");
            tc00.setStyle("-fx-font-family: 'Microsoft JhengHei'; ");//;-fx-font-weight: 700; 
            tc01.setStyle("-fx-font-family: 'Microsoft JhengHei';  ");
            tc02.setStyle("-fx-font-family: 'Microsoft JhengHei';-fx-alignment: center-left;  ");
            tc03.setStyle("-fx-font-family: 'Microsoft JhengHei';-fx-alignment: center-left;  ");            
            tc04.setStyle("-fx-font-family: 'Microsoft JhengHei';  ");
            tc05.setStyle("-fx-font-family: 'Microsoft JhengHei';  ");
            tc06.setStyle("-fx-font-family: 'Microsoft JhengHei';  ");
        }
       
     }
    /****變更button字型****字型:中文-標楷體,英文:Times New Roman****/ // 2018.04.10  william  功能修改   
    public void ChangeButtonLang(Button but01, Button but02){
        //Button "Leave"
        if("English".equals(WordMap.get("SelectedLanguage"))){
            but01.setStyle("-fx-font-family:'Times New Roman';");         
            but02.setStyle("-fx-font-family:'Times New Roman';");         
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/
            //winXP 僅能用中文名稱
            but01.setStyle("-fx-font-family: '微軟正黑體';");
            but02.setStyle("-fx-font-family: '微軟正黑體';");
            
            //win7以上僅能用英文名稱
            but01.setStyle("-fx-font-family: 'Microsoft JhengHei';");          
            but02.setStyle("-fx-font-family: 'Microsoft JhengHei';");
        }  
        
     }    
    
    /***********桌面狀態 VD_Status 轉換語言(json)**************/ // 2018.04.12 william  多VD登入新增開機狀態 
    public String VDStatusName(String Satus, TableColumn column){
     VdStatus=Satus;
     if("-2".equals(VdStatus)){      
         VdStatus= WordMap.get("VM_Waiting_Poweron");
     }
     if("-1".equals(VdStatus)){      
         VdStatus= WordMap.get("VM_Disable");
     }
     if("0".equals(VdStatus)){      
         VdStatus= WordMap.get("VM_Stop");
     }
     if("1".equals(VdStatus)){      
         VdStatus= WordMap.get("VM_Poweron");
         // column.setCellFactory(getCustomCellFactory("green"));
     }
     if("2".equals(VdStatus)){      
         VdStatus= WordMap.get("VM_Blocked");
     }
     if("3".equals(VdStatus)){      
         VdStatus= WordMap.get("VM_Pause");
     }
     if("4".equals(VdStatus)){      
         VdStatus= WordMap.get("VM_Shutdowning");
     }
     if("5".equals(VdStatus)){      
         VdStatus= WordMap.get("VM_Shutdown");
         // column.setCellFactory(getCustomCellFactory("blue"));
     }
     if("6".equals(VdStatus)){      
         VdStatus= WordMap.get("VM_Crash");
     }
      if("7".equals(VdStatus)){      
         VdStatus= WordMap.get("VM_Migrate");
     }
      if("10".equals(VdStatus)){      
         VdStatus= WordMap.get("VM_Preparing_Boot");
     }   
    return VdStatus;
    }    
    // 2018.04.12 william  多VD登入新增開機狀態     
    private Callback<TableColumn<MultiVD, String>, TableCell<MultiVD, String>> getCustomVDCellFactory() {
        return new Callback<TableColumn<MultiVD, String>, TableCell<MultiVD, String>>() {
            @Override
            public TableCell<MultiVD, String> call(TableColumn<MultiVD, String> param) {
                TableCell<MultiVD, String> cell = new TableCell<MultiVD, String>() {
                    @Override
                    public void updateItem(final String item, boolean empty) {
                        if (item != null) {
                            // System.out.println("item: " + item + ", " + "empty: " + empty +"\n");
                            if(item.equals("開機") || item.equals("开机") || item.equals("Poweron") ) {
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
        
    // 2018.11.11 新增AcroDesk & RDP Protocol 
    public enum Type { 
        ACROVIEW("A", "AcroView"), RDP("R", "Rdp");
 
        private String code;
        private String text;
 
        private Type(String code, String text) {
            this.code = code;
            this.text = text;
        }

        public String getCode() {
            return code;
        }

        public String getText() {
            return text;
        }
 
        public static Type getByCode(String typeCode) {
            for (Type g : Type.values()) {
                if (g.code.equals(typeCode)) {
                   return g;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return this.text;
        }
 
    }    

    public String ProtocolString(ProtocolType Type){
        String _type = null;
     if("RDP".equals(Type)){      
         _type= "RDP";
     }
     if("both".equals(Type)){      
        _type= "Both";
     }
     if("AcroView".equals(Type)){      
         _type= "AcroView";
     }


    return _type;
    }    
            
    public synchronized ComboBox<ProtocolType> getComboBox() {
        return TypecomboBox;
    }

    public synchronized void setComboBox(ComboBox<ProtocolType> _comboBox) {
        TypecomboBox = _comboBox;
    }    
    
    public static synchronized int getSelectIndex() {
        return select_index;
    }

    public static synchronized void setSelectIndex(int _index) {
        select_index = _index;
    }      
    
    private class ComboBoxCell<S, T> extends TableCell<S, T> {
       private ComboBox comboBoxButton;
       private ObservableValue<T> ov;
       private ComboBoxCell() {            
            comboBoxButton = new ComboBox();
            
            comboBoxButton.setOnAction((event) -> {
                StopPollingFresh = true;
//                System.out.println( "Size : " + dict.size());
//                System.out.println("************* VD  Name ************* : " +vd_table.getFocusModel().getFocusedItem().getVD_Name());              
//                System.out.println("1. ComboBox Cell ID  : " + comboBoxButton.getId());    
            
                //System.out.println("---Protocol Selection : ---" + comboBoxButton.getValue().toString()); 
                if(comboBoxButton.getValue() != null) {
                    dict.put(comboBoxButton.getId(), comboBoxButton.getValue().toString());
                    // 2019.04.10 VM/VD migration reconnect
                    dictSelectProtocol.put(comboBoxButton.getId(), comboBoxButton.getValue().toString());    
                    StopPollingFresh = false;
                }                    
            });            

            setComboBox(comboBoxButton);
            setGraphic(comboBoxButton);
        }               
       
        @Override
        protected void updateItem(T item, boolean empty) {        

            super.updateItem(item, empty);
            if(item != null || !empty){                     
                for(int i = 0; i < vd_data.size(); i++) {
                    if(vd_data.get(i).getVD_Name() == GetVdName) {
                        // dictVdIndex.put(GetVdName, i);
                        System.out.println( "1. Create ComboBox : " + GetVdName + " Index : " + i); //  + " , " + dictVdIndex.get(GetVdName)
                        RenewComboBox(this.comboBoxButton, i);
                    }                
                }
            } else {
                // 如果沒有資料的欄位就設為空
                setText(null);
                setGraphic(null);      
            }                               
        }        
    }       
    
    private static <S,T> TableColumn<S,T> createColumn(String title, Function<S, ObservableValue<T>> property) {
        TableColumn<S,T> col = new TableColumn<>(title);
        col.setCellValueFactory(cellData -> property.apply(cellData.getValue()));
        return col ;
    }    
                      
    public void VDListPolling() throws ProtocolException, IOException {
        URL url;    
        JSONObject obj = new JSONObject();
        obj.put("AccountName", CurrentUserName);
        obj.put("Password", CurrentPasseard);       
        String query = obj.toString();   
        
        url = new URL("https://" + Address + ":" + Port + "/vdi/user/vd/" + uniqueKey);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");            
        conn.setRequestProperty("Content-length", String.valueOf(query.getBytes().length));
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setUseCaches (false);
        conn.setInstanceFollowRedirects(true);            
        conn.setConnectTimeout(20000);
        conn.setReadTimeout(10000);   
                     
        try (OutputStream out = conn.getOutputStream()) {
            out.write(query.getBytes());
            out.flush();
            out.close();
        } catch (IOException ex) {}         
        
        InputStream is = conn.getInputStream(); // Detect server dead or live
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
        JsonArray jsonArr = returndata.getAsJsonObject().getAsJsonArray("UserVdImages");
        jsondata = jsonArr;
//        System.out.println("JsonArray jsonArr: "+jsonArr+"\n");   
        
        //MultiVD[] _array = new MultiVD[jsonArr.size()];
        // 2019.01.02 view D Drive
        // MultiVD[] _array = new MultiVD[jsonArr.size()];
        MultiVD[] _array; 
        if(!GB.IsViewDisk)        
            _array = new MultiVD[jsonArr.size()];
        else
            _array = new MultiVD[1];         
        int count = 1;
        int connect_type = 0;
        ProtocolType _ProtocolType = null;
        String RDPIP = "";
        String RDPPort = ""; // 2018.12.07 RDP ping ip and port
        String RDPStatus = "";
        JsonArray jsonArrForRDP = null;
        JsonObject termForRDP = null;
        boolean IsPingOK = false;
        int _protocol = 0;
        boolean IsCannotConnect = false; // 2018.12.12 新增 431NoVGA 和第一次開機
        
        // 2019.01.17 相容性處理
        boolean QXL = false;
        int VGACount = 0;        
        int RDPFirst = 0;        
        int _rdpArraySize = 0;
        
        // 2019.04.16 修改協定判斷邏輯
        int AvailableProtocol = 0;                
        
        for (int i = 0; i < jsonArr.size(); i++) {
            term      = jsonArr.get(i).getAsJsonObject();            
            VdName    = term.get("VdName").getAsString();
            
            // 2019.01.02 view D Drive
            if(GB.IsViewDisk) {
                if(jsonArr.size() > 1) {
                    if(!GB._viewDiskVDName.equals(VdName))
                        continue;                
                }
            }             
            
            VdId      = term.get("VdId").getAsString();
            VdDesc    = term.get("Desc").toString();
            VdStatus  = VDStatusName(term.get("VdStatus").toString(), vdStatusCol); // 2018.04.12 william  多VD登入新增開機狀態   
            
            IsCannotConnect = false; // 2018.12.12 新增 431NoVGA 和第一次開機
            
            if(GB.RDPEnable) { // 2019.01.24 New 5.1.X
                // 2019.01.17 相容性處理             
               try {
                   jsonArrForRDP = term.getAsJsonArray("RDP");
                   _rdpArraySize = jsonArrForRDP.size();
                   for(int j = 0; j < jsonArrForRDP.size(); j++) {
                       // 2018.12.07 RDP ping ip and port
                       // RDPIP = jsonArrForRDP.get(j).getAsString();
                       RDPIP = jsonArrForRDP.get(j).getAsJsonObject().get("IP").getAsString();
                       RDPPort = jsonArrForRDP.get(j).getAsJsonObject().get("Port").getAsString();
                       if(IsPingOK = PingIP.isReachableByTcp(RDPIP, Integer.parseInt(RDPPort), 1000))
                           break;
                       //IsPingOK = PingIP.isReachableByTcp(RDPIP, 3389, 1000);                
                       // System.out.println("VdName: " + VdName + " RDPIP : "+RDPIP+" Size : " + jsonArrForRDP.size() + " Is Ping OK : " + IsPingOK);

                   }
               } catch (Exception ex) {
                   IsPingOK = false;   
                   _rdpArraySize = 0;
               }            

               // 2019.01.17 相容性處理
               try {
                   _protocol = term.get("Protocol").getAsInt(); 
               } catch (Exception ex) {
                   _protocol = 0;    
               }

               // 2019.01.17 相容性處理
               try {
                   QXL = term.get("QXL").getAsBoolean();
                   VGACount = term.get("VGACount").getAsInt();
                   RDPFirst = term.get("RDPFirst").getAsInt();
               } catch (Exception ex) {
                   QXL = false;
                   VGACount = 0;
                   RDPFirst = 0;
               }         
               
                // 2019.04.16 修改協定判斷邏輯
                try {
                    AvailableProtocol = term.get("AvailableProtocol").getAsInt();
                    
                    if(AvailableProtocol == 1) { 
                        _ProtocolType = ProtocolType.AcroSpice;
                        connect_type = 0;
                    } else if(AvailableProtocol == 2) {
                        _ProtocolType = ProtocolType.AcroRDP;
                        connect_type = 1;
                    } else if(AvailableProtocol == 3) {
                        if(_protocol == 1 || _protocol == 0)
                            _ProtocolType = ProtocolType.AcroSpice;
                        else if(_protocol == 2)
                            _ProtocolType = ProtocolType.AcroRDP;
                        connect_type = 2;
                    }                        
                } catch (Exception ex) {
                    _ProtocolType = ProtocolType.AcroSpice;
                    connect_type = 0;
                }                
                
                /*
               if(QXL && VGACount > 0) {
                   _ProtocolType = ProtocolType.AcroSpice;// 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP
                   connect_type = 0;
               } else if(!QXL && VGACount > 0) {
                   _ProtocolType = ProtocolType.AcroRDP; // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP
                   connect_type = 1;
               } else if(QXL && VGACount == 0) {
                   if(_protocol == 1)
                       _ProtocolType = ProtocolType.AcroSpice;// 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP
                   else if(_protocol == 2)
                       _ProtocolType = ProtocolType.AcroRDP; // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP                
                   connect_type = 2;
               }
               */

               if(term.get("VdStatus").getAsInt() == 5) {
                   RDPStatus = WordMap.get("Str_Shutdown");                
                   lockStringType = 0; // 2018.12.12 新增 431NoVGA 和第一次開機
               } else if(term.get("VdStatus").getAsInt() == 1 && _rdpArraySize == 0) {
                   RDPStatus = WordMap.get("Str_Preparing");
                   lockStringType = 1; // 2018.12.12 新增 431NoVGA 和第一次開機
               } else if(term.get("VdStatus").getAsInt() == 1 && _rdpArraySize > 0 && IsPingOK) {
                   RDPStatus = WordMap.get("Str_Ready");                
                   lockStringType = 2; // 2018.12.12 新增 431NoVGA 和第一次開機
               } else if(term.get("VdStatus").getAsInt() == 1 && _rdpArraySize > 0 && !IsPingOK) {
                   RDPStatus = WordMap.get("Str_CannotConnect");
                   IsCannotConnect = true; // 2018.12.12 新增 431NoVGA 和第一次開機
               } else if(term.get("VdStatus").getAsInt() == 10) {
                   RDPStatus = WordMap.get("Str_Preparing");
                   lockStringType = 0; // 2018.12.12 新增 431NoVGA 和第一次開機
               }                      

            }            

            dictVDStatus.put(VdName, RDPStatus);
            
            // 2018.12.12 新增 431NoVGA 和第一次開機

            if(GB.RDPEnable) { // 2019.01.24 New 5.1.X
                dictRDPFirst.put(VdName, RDPFirst); // 2018.18.18 new create & reborn  
            }              
            
            dictKVMVDStatus.put(VdName, term.get("VdStatus").getAsInt());
            dictLockType.put(VdName, lockStringType);
            dictCannotConnect.put(VdName, IsCannotConnect);
            // 2019.03.28 VM/VD migration reconnect
            dict3DType.put(VdName, connect_type);
            dictSelectProtocol.put(VdName, _ProtocolType);            
            
            
            if( VdDesc == null ){
                VdDesc = "";
            }
            if("null".equals(VdDesc)){
                VdDesc = "";
            } else {
                VdDesc = term.get("Desc").getAsString(); 
            }            
            
            
            IsDefault = term.get("IsDefault").toString();
            if(IsDefault.equals("true")) {              
                if(!GB.RDPEnable) { // 2019.01.24 New 5.1.X
                    _array[0] = new MultiVD(VdName, VdId, VdDesc, VdStatus, ProtocolType.AcroRDP, WordMap.get("Str_Shutdown"), connect_type, _protocol);                
                } else {
                    _array[0] = new MultiVD(VdName, VdId, VdDesc, VdStatus, _ProtocolType, RDPStatus, connect_type, _protocol);                
                }
            } else {
                if(!GB.RDPEnable) { // 2019.01.24 New 5.1.X
                    if(jsonArr.size() > 1) {                                
                        // 2019.01.02 view D Drive
                        if(GB.IsViewDisk) {
                            _array[0] = new MultiVD(VdName, VdId, VdDesc, VdStatus, ProtocolType.AcroRDP, WordMap.get("Str_Shutdown"), connect_type, _protocol);
                        } else {
                            _array[count] = new MultiVD(VdName, VdId, VdDesc, VdStatus, ProtocolType.AcroRDP, WordMap.get("Str_Shutdown"), connect_type, _protocol);
                        }                
                    } else {                
                        _array[0] = new MultiVD(VdName, VdId, VdDesc, VdStatus, ProtocolType.AcroRDP, WordMap.get("Str_Shutdown"), connect_type, _protocol);
                    }                   
                } else {
                    if(jsonArr.size() > 1) {                                
                        // 2019.01.02 view D Drive
                        if(GB.IsViewDisk) {
                            _array[0] = new MultiVD(VdName, VdId, VdDesc, VdStatus, _ProtocolType, RDPStatus, connect_type, _protocol);
                        } else {
                            _array[count] = new MultiVD(VdName, VdId, VdDesc, VdStatus, _ProtocolType, RDPStatus, connect_type, _protocol);
                        }                
                    } else {                
                        _array[0] = new MultiVD(VdName, VdId, VdDesc, VdStatus, _ProtocolType, RDPStatus, connect_type, _protocol);
                    }                   
                }
                count++;
            }               
        }
        if(!equals(_checkarray, _array) && !StopPollingFresh) {
            vd_data.removeAll(vd_data);
            vd_data.addAll(_array);  
            vd_table.refresh();        
            vd_table.getSelectionModel().select(getSelectIndex());
            vd_table.getFocusModel().focus(getSelectIndex());            
        }
        
        _checkarray = null;
        _checkarray = _array;
        
        // update value : https://stackoverflow.com/questions/41424544/how-to-update-value-of-each-row-in-table-view-of-javafx
    
    }
    
    public static boolean equals(MultiVD[] a, MultiVD[] b) {
        if(a.length != b.length)
            return false;
        for (int i = 0; i < a.length; i++) {
            if(a[i].getVD_Status() != b[i].getVD_Status() || a[i].getRDP_Status() != b[i].getRDP_Status())
                return false;
        }
        return true;
    }       
    
    public void RenewComboBox(ComboBox _comboBox, int _index) {
        System.out.println("2. Sorting tableView data : " + vd_table.getItems().get(_index).getVD_Name()); 
        
        _comboBox.setId(vd_table.getItems().get(_index).getVD_Name());
        _comboBox.getItems().clear();
        switch (dictComboBox.get(vd_table.getItems().get(_index).getVD_Name()).toString()) {
            case "1":
                _comboBox.getItems().addAll(ProtocolType.AcroSpice);// 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP    
                _comboBox.setDisable(true);
                break;
            case "2":
                _comboBox.getItems().addAll(ProtocolType.AcroRDP);// 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP    
                _comboBox.setDisable(true);
                break;
            case "3":
                _comboBox.getItems().addAll(ProtocolType.AcroSpice, ProtocolType.AcroRDP);// 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP    
                _comboBox.setDisable(false);
                break;
            case "4":
                _comboBox.getItems().addAll(ProtocolType.AcroRDP, ProtocolType.AcroSpice);// 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP    
                _comboBox.setDisable(false);
                break;                                              
        }
        System.out.println("3. sort ID : " + _comboBox.getId() + " sort type : " +  dictComboBox.get(_comboBox.getId()).toString());                                                             
        System.out.println("---------------------------------------------------------------");
        _comboBox.getSelectionModel().selectFirst();     
    }

    // 2018.11.30 新增等待中文字動態顯示
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
                           message = "";// 2018.12.12 新增 431NoVGA 和第一次開機
                           messageQueue.put(message);
                           messageCount ++;
                           sleep(300);
                           break;
                        case  1:   
                           message = ".";// 2018.12.12 新增 431NoVGA 和第一次開機
                           messageQueue.put(message);                            
                           messageCount ++;   
                           sleep(300);
                           break;
                        case  2:   
                           message = "..";// 2018.12.12 新增 431NoVGA 和第一次開機
                           messageQueue.put(message);                                 
                           messageCount ++;   
                           sleep(300);
                           break;
                        case  3:   
                           message = "...";// 2018.12.12 新增 431NoVGA 和第一次開機
                           messageQueue.put(message);                               
                           messageCount = 0;       
                           sleep(300);
                           break;                    
                    }                                                                              
                    if(StopMessageQueue)
                        break;
                    if(killthread)
                        break;
                    
                }
            } catch (InterruptedException exc) {
                System.out.println("Message producer interrupted: exiting.");
            }
        }
    }    
    // 2018.12.12 新增 431NoVGA 和第一次開機
    private class MessageProducer2 implements Runnable {
        private final BlockingQueue<String> messageQueue2 ;

        public MessageProducer2(BlockingQueue<String> messageQueue2) {
            this.messageQueue2 = messageQueue2 ;
        }

        @Override
        public void run() {
            try {                
                while (true) {                       
                    if(!"AcroSpice".equals(getProtocol()) && Integer.parseInt(dictRDPFirst.get(Select_VdName).toString()) != 0) { // 2018.18.18 new create & reborn //  && !Boolean.parseBoolean(dictRDPFirst.get(Select_VdName).toString()) // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP   
                        // 2018.18.18 new create & reborn
                        if(Integer.parseInt(dictRDPFirst.get(Select_VdName).toString()) == 1) {
                            messageRow1 = WordMap.get("Message_NewCreate");
                            messageQueue2.put(messageRow1);
                        }
                            
                        else if(Integer.parseInt(dictRDPFirst.get(Select_VdName).toString()) == 2) {
                            messageRow1 = WordMap.get("Message_Reborn");
                            messageQueue2.put(messageRow1);
                        }
                            
                        
                        
                                                                        
                    }                                                                                                                     
                    if(StopMessageQueue2)
                        break;
                    if(killthread)
                        break;                    
                }
            } catch (InterruptedException exc) {
                System.out.println("Message producer interrupted: exiting.");
            }
        }
    }    
    
    private class MessageProducer3 implements Runnable {
        private final BlockingQueue<String> messageQueue3 ;

        public MessageProducer3(BlockingQueue<String> messageQueue3) {
            this.messageQueue3 = messageQueue3 ;
        }

        @Override
        public void run() {
            try {                
                while (true) {                       
                    if(!"AcroSpice".equals(getProtocol())) { // && !Boolean.parseBoolean(dictRDPFirst.get(Select_VdName).toString()) // 2018.12.17 VDI Viewer -> AcroViewer & AcroSpice & AcroRDP 
//                        if(Integer.parseInt(dictRDPFirst.get(Select_VdName).toString()) == 0 && GB.RDP_Stage_Type == 5) { // 2018.18.18 new create & reborn Boolean.parseBoolean -> Integer.parseInt
//                            messageRow2 = WordMap.get("Thread_Waiting");
//                            messageQueue3.put(messageRow2);            
//                        }
                        
                    // 2018.18.18 new create & reborn    
                    switch (GB.RDP_Stage_Type) {
                           case  0:
                                messageRow2 = WordMap.get("Message_ReadingProfile");
                                messageQueue3.put(messageRow2);
                                lockStringType = -1;
                                break;
                            case  1:   
                               messageRow2 = WordMap.get("Message_RDP_Stage_1");
                               messageQueue3.put(messageRow2); 
                                lockStringType = -1;                           
                               break;
                            case  2:   
                               messageRow2 = WordMap.get("Message_RDP_Stage_2");
                               messageQueue3.put(messageRow2);     
                               lockStringType = -1;
                               break;        
                            case  3:   
                                if("true".equals(GB.IsAssignedUserDisk))
                                    messageRow2 = WordMap.get("Message_RDP_Stage_3_Profile");
                                else if("false".equals(GB.IsAssignedUserDisk))
                                    messageRow2 = WordMap.get("Message_RDP_Stage_3_NoProfile");
                                else if("NULL".equals(GB.IsAssignedUserDisk))
                                    messageRow2 = WordMap.get("Message_IsAssignedUserDisk_NULL");
                                
                                messageQueue3.put(messageRow2);     
                               lockStringType = -1;
                               break;  
                            case  4:   
                                if("true".equals(GB.IsAssignedUserDisk))
                                    messageRow2 = WordMap.get("Message_RDP_Stage_4_Profile");
                                else if("false".equals(GB.IsAssignedUserDisk))
                                    messageRow2 = WordMap.get("Message_RDP_Stage_4_NoProfile");
                                else if("NULL".equals(GB.IsAssignedUserDisk))
                                    messageRow2 = WordMap.get("Message_IsAssignedUserDisk_NULL");
                                
                                messageQueue3.put(messageRow2);   
                               lockStringType = -1;
                               break;  
                            case  5:   
                               messageRow2 = WordMap.get("Thread_Connecting");
                               messageQueue3.put(messageRow2);     
                               lockStringType = -1;
                               break;      
                            default:   
                                // 2018.12.27  其他中斷連線狀態 - Crash & Migrate
                                if(Integer.parseInt(dictKVMVDStatus.get(Select_VdName).toString()) == 6 || Integer.parseInt(dictKVMVDStatus.get(Select_VdName).toString()) == 7) {
                                    messageRow2 = WordMap.get("Message_Error_VMAnomaly");
                                    messageQueue3.put(messageRow2);
                                    lockStringType = -1;                    
                                } else {
                                    if(dictVDStatus.get(Select_VdName).toString().equals(WordMap.get("Str_Ready")) || dictVDStatus.get(Select_VdName).toString().equals(WordMap.get("Str_CannotConnect")) || dictVDStatus.get(Select_VdName).toString().equals(WordMap.get("Str_Preparing"))) {
                                        messageRow2 = WordMap.get("Message_RDP_Preparing");
                                        messageQueue3.put(messageRow2);
                                        lockStringType = -1;                                   
                                    }                                                                     
                                }                                
                                
                                break;      
                        }                         

                    } else {
                        // 2018.12.27  其他中斷連線狀態 - Crash & Migrate
                        if(Integer.parseInt(dictKVMVDStatus.get(Select_VdName).toString()) == 6 || Integer.parseInt(dictKVMVDStatus.get(Select_VdName).toString()) == 7) {
                            messageRow2 = WordMap.get("Message_Error_VMAnomaly");
                            messageQueue3.put(messageRow2);
                            lockStringType = -1;                    
                        } else {
                            messageRow2 = WordMap.get("Thread_Waiting");
                            messageQueue3.put(messageRow2);
                            lockStringType = -1;                               
                        }                                                                                        
                                        
                    }                                                                                                                     
                    if(StopMessageQueue2)                         
                        break;     
                    if(killthread)
                        break;                    
                }
            } catch (InterruptedException exc) {
                System.out.println("Message producer interrupted: exiting.");
            }
        }
    }    
            
    
    public void RenewString() {
        final LongProperty lastUpdate = new SimpleLongProperty();                 
        final long minUpdateInterval = 5000 ; // nanoseconds. Set to higher number to slow output.

        TextTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate.get() > minUpdateInterval) {
                    //System.out.println("Run TextTimer");
                    messageText = messageQueue.poll();
                    if (messageText != null) {                        
                        Login_title3.setText(messageText);
                        messageQueue.clear();
                    }
                    lastUpdate.set(now);
                }
            }
        };
                 

    }      
    // 2018.12.12 新增 431NoVGA 和第一次開機
    public void RenewString2() {
        final LongProperty lastUpdate = new SimpleLongProperty();                 
        final long minUpdateInterval = 50000000 ; // nanoseconds. Set to higher number to slow output.

        TextTimer2 = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate.get() > minUpdateInterval) {
                    //System.out.println("Run TextTimer");
                    messageTextRow1 = messageQueueRow1.poll();
                    if (messageTextRow1 != null) {                        
                        Login_title.setText(messageTextRow1);
                        messageQueueRow1.clear();
                    }
                    messageTextRow2 = messageQueueRow2.poll();
                    if (messageTextRow2 != null) {                        
                        Login_title2.setText(messageTextRow2);                        
                        messageQueueRow2.clear();
                    }             
//                    if(getProtocol() != null && Select_VdName != null) {
//                        if(!"AcroView".equals(getProtocol()) && !Boolean.parseBoolean(dictRDPFirst.get(Select_VdName).toString())) 
//                            ChangeLabelLang(Login_title, Login_title2, 2);                    
//                    }

                    lastUpdate.set(now);
                }
            }
        };
               

    }    
    
    // 2018.12.04 新增VD&RDP狀態判斷
    public void StopAll() {
        timer.stop();
        StopMessageQueue = true; // 2018.11.30 新增等待中文字動態顯示
        TextTimer.stop();
        StopPolling = true; 
        // 2018.12.12 新增 431NoVGA 和第一次開機
        StopMessageQueue2 = true;
        TextTimer2.stop();        
    }
    
    // 2018.12.12 新增 431NoVGA 和第一次開機
    public void ConnectCmd(String _vdname) {
        GB.ConnectionAddress = "";
        GB.ConnectionPort = "";
        QM.checkaddr = false;
        GB.ExitFlag = false;
        GB.stopconnect = false;
        killthread = false;
        timer.start();  
        TextTimer.start();
        TextTimer2.start();  
        if(!GB.RDPEnable) { // 2019.01.24 New 5.1.X
            setProtocol("AcroSpice");
            Login_lock("AcroSpice", dictVDStatus.get(_vdname).toString(), 0, Integer.parseInt(dictKVMVDStatus.get(_vdname).toString()));
        } else {
            setProtocol(dict.get(_vdname).toString());
            Login_lock(getProtocol(), dictVDStatus.get(_vdname).toString(), Integer.parseInt(dictRDPFirst.get(_vdname).toString()), Integer.parseInt(dictKVMVDStatus.get(_vdname).toString())); // 2018.12.12 新增 431NoVGA 和第一次開機 // 2018.18.18 new create & reborn Boolean.parseBoolean -> Integer.parseInt
        }                   
        Login_lock_thr llthr2 = new Login_lock_thr();
        Thread thr = new Thread(llthr2);
        thr.start();   
        // 2018.11.30 新增等待中文字動態顯示
        MessageProducer producer = new MessageProducer(messageQueue);
        Thread t = new Thread(producer);
        t.start();        
        if(GB.RDPEnable) { // 2019.01.24 New 5.1.X
            // 2018.12.12 新增 431NoVGA 和第一次開機
            MessageProducer2 producer2 = new MessageProducer2(messageQueueRow1);
            Thread t2 = new Thread(producer2);
            t2.start();    
        }
        MessageProducer3 producer3 = new MessageProducer3(messageQueueRow2);
        Thread t3 = new Thread(producer3);
        t3.start();         
    }
    
    public void CannotConnectCmd() {
        StopAll();
        MultiVDList_secondStage.close();
        try {
            sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(LoginMultiVD.class.getName()).log(Level.SEVERE, null, ex);
        }
        Platform.runLater(() -> { 
            QM.ReLogin(2);
        });
                
    }    
    // 2018.12.27  其他中斷連線狀態 - Crash & Migrate
    public void DisconnectCmd(int _type) {
        StopAll();
        MultiVDList_secondStage.close();
        try {
            sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(LoginMultiVD.class.getName()).log(Level.SEVERE, null, ex);
        }
        Platform.runLater(() -> { 
            QM.ReLogin(_type);
        });
                
    }    
        
    
}
