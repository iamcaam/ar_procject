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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
 * @author root
 */

// 2019.01.02 view D Drive   
public class ViewDrive {
    public GB GB;
    private QueryMachine QM;
    /*------------------------ import檔案及匯入文字檔 ------------------------*/
    public Map<String, String> WordMap = new HashMap<>();
    
    public String Address;
    public String Port;  
    public String CurrentUserName;
    public String CurrentPasseard; 
    public String uniqueKey;
    /*------------------------ 設定Layout ------------------------*/
    private Stage public_stage;
    private Stage Snapshot_secondStage = new Stage();
    private final Scene secondScene;
    private final BorderPane rootPane; // 2017.12.04 william BorderPane框架(底層)  
    private final GridPane MainGP;     // 2017.12.04 william BorderPane中間層 (快照層 - ListView物件)
    private final GridPane LeftGP;     // 2017.12.04 william BorderPane左層   (VD層 - ListView物件)
    private final GridPane BottomGP;   // 2017.12.04 william BorderPane下層   (說明層)
    private final Bounds mainBounds;       
     
    private Button Btn_exit;
    private Button Btn_stopview;
    private Button Btn_login;        
    // 畫面鎖定
    private StackPane stack = new StackPane(); 
    private Label Login_title;                                  
    private GridPane LoginGP;                                   
    private ProgressIndicator p1;                               
    // 設定長寬
    int secondScene_width                       = 1080;
    int secondScene_height                      = 603; 
    int LeftGP_width                            = 324;
    int LeftGP_height                           = 422;    
    int MainGP_width                            = 756;
    int MainGP_height                           = 422;    
    int BottomGP_width                          = 973;
    int BottomGP_height                         = 181;    
    int vd_listView_width                       = 250;
    int vd_listView_height                      = 360; 
    int ud_listView_width                       = 712; 
    int ud_listView_height                      = 360; 
    int btn_width                               = 120;
    int btn_height                              = 35;           
    // 設定TableView
    private TableView<VD> vdtable = new TableView();
    private TableView<UserDisk> userdisktable = new TableView();
    // 預設值寫進表格內
    public ObservableList<VD> vd_data = FXCollections.observableArrayList();
    public ObservableList<UserDisk> userdisk_data = FXCollections.observableArrayList(); 
    
    public AnimationTimer login_timer;
    public boolean login_timerflag;    
    /*------------------------ Polling ------------------------*/           
    private Timer fresh_timer;
        
    public String Select_vdID;
    public static String ViewLayer;
    public static String RollBackLayer;  
    public static String Vd_Id;                    
    public boolean sslist_json_length_empty;
    public boolean IsKeepNIC;       
    public boolean NoUserDiskfunc_flag;      
    public int RespCode;   
    public int BtnDisable_flag;  
    public int vd_index;
            
    VD[] _checkVDarray = null;
    UserDisk[] _checkUserDiskarray = null;
    
    public static String _diskName;
    public static String _vdName;
    public static String _diskNamePolling;
    public boolean IsView = false;
    TableColumn<UserDisk, String> StateCol;
    TableColumn<UserDisk, String> ActionCol;     
    JsonArray getlist_jsonArr;
    
          
    
    public ViewDrive(Stage primaryStage, Map<String, String> LangMap,JsonArray jsonArr, String IPAddr, String IPPort, String Uname, String Password, String UniqueKey, String _diskname){
        public_stage    = primaryStage;        
        WordMap         = LangMap;
        // 取值，(IP, 使用者帳號, 使用者密碼, Port)
        Address         = IPAddr;
        Port            = IPPort; 
        CurrentUserName = Uname;
        CurrentPasseard = Password;   
        uniqueKey       = UniqueKey;

        login_timerflag = false;
        fresh_timer       = null;
        sslist_json_length_empty = false;
        QM              = new QueryMachine(WordMap);
        
        
        NoUserDiskfunc_flag = false;  //  2018.01.12 william 例外處理
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
//        rootPane.setVisible(true);              

        /***  左層VD的Layout ***/        
        LeftView(jsonArr, _diskname);        
        /***  中間層快照的Layout ***/                
        CenterView();
        /***  下層說明的Layout ***/
        BottomView();
        /***  動作功能 ***/        
        detectSelection();
        
        /* 2017.11.27 william 以下為跳出視窗之功能 */                
        
        secondScene = new Scene(rootPane, secondScene_width, secondScene_height);
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
        if(NoUserDiskfunc_flag) {     
            StopAll();
        }        
        
        // 新增Stage關閉事件
        Snapshot_secondStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
              System.out.println("Stage is closing");
              StopAll();
          }
        });     
    
    }

    /*------------------------ 畫面layout ------------------------*/ 
    public final void LeftView(JsonArray jsondata, String _diskname) {
        String label_vdTitleString = "" ;
        TableColumn vdSNCol = new TableColumn(WordMap.get("VD_SN"));    
        TableColumn<VD, String> vdNameCol = new TableColumn(WordMap.get("VD_Name"));      
        vd_data.clear(); // 清空data先前的內容        
        
        if(WordMap.get("SelectedLanguage").equals("English")) {
            label_vdTitleString = "VM / VD";
        } else if(WordMap.get("SelectedLanguage").equals("TraditionalChinese")) {
            label_vdTitleString = "雲主機 / 雲桌面";
        } else if(WordMap.get("SelectedLanguage").equals("SimpleChinese")) {
            label_vdTitleString = "云主机 / 云桌面";
        } 
        /***  Title 放入一個HBOX內 ***/
        Label vdTitleLabel = new Label(label_vdTitleString);
        vdTitleLabel.getStyleClass().add("Title_label");
                       
        vdSNCol.setPrefWidth(60);
        vdSNCol.setMinWidth(60);
        vdSNCol.setMaxWidth(60);        
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

        vdNameCol.setPrefWidth(168);
        vdNameCol.setStyle( "-fx-alignment: center-left;");         
        vdNameCol.setSortable(false);        
        vdNameCol.setCellFactory(getCustomVDCellFactory());
        vdNameCol.setCellValueFactory(new PropertyValueFactory<VD, String>("VD_Name"));    
        
        VD[] _array = new VD[jsondata.size()];
        
        setDiskName(_diskname);
        
        try {         
            // 顯示可選擇之VD
            for (int i = 0; i < jsondata.size(); i++) {
                JsonObject vd_term  = jsondata.get(i).getAsJsonObject();            
                String vd_Name      = vd_term.get("VdName").getAsString();
                String VdId         = vd_term.get("VdId").getAsString();
                
                _vdName = vd_Name;
                
                System.out.println("Bind User Disk : " + vd_term.get("BindUserDisk").getAsBoolean());
                
                       
                if (i==0) { setVDID(VdId); }     
                _array[i] = new VD(vd_Name, VdId);
            }                   

            vd_data.addAll(_array);
            _checkVDarray = _array;
            
        } catch (Exception ex) {
            NoUserDiskfunc_flag = true; 
            vd_data.add(new VD("Null","Null"));
            NoUserDiskfunc();
        }        
            
        vdtable.setEditable(false);       
        vdtable.getColumns().addAll(vdSNCol, vdNameCol);
        vdtable.setItems(vd_data);     
        vdtable.getStyleClass().add("table_header");
        vdtable.setPrefSize(vd_listView_width, vd_listView_height);
        vdtable.setMaxSize(vd_listView_width , vd_listView_height);
        vdtable.setMinSize(vd_listView_width , vd_listView_height);         
//        vdtable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // 自動重新整理大小,table的colum大小隨table的大小變化而變化

        // 設定第一個元素為被選擇狀態
        vdtable.requestFocus();
        vdtable.getSelectionModel().select(0);
        vdtable.getFocusModel().focus(0);
        vd_index = vdtable.getSelectionModel().getSelectedIndex();
        System.out.println("get vd index: " + vd_index);
        
        HBox WC1 = new HBox(vdTitleLabel);
        WC1.setAlignment(Pos.TOP_CENTER);
        WC1.setPadding(new Insets(0, 0, 0, 0));
        WC1.setTranslateY(0);
        WC1.setTranslateX(0);
                         
        HBox vd_selection = new HBox();
	vd_selection.setSpacing(20);       
        vd_selection.setAlignment(Pos.CENTER);
        vd_selection.setPadding(new Insets(0, 0, 0, 0));  
	vd_selection.getChildren().addAll(vdtable);
                
        LeftGP.add(WC1,0,0);
        LeftGP.add(vd_selection,0,1);          
    }

    public final void CenterView() {   
        /***  Title 放入一個HBOX內 ***/
        Label UserDiskListTitleLabel = new Label(WordMap.get("Select_Snapshot_Layer"));
        UserDiskListTitleLabel.getStyleClass().add("Title_label");
        
        HBox WC2 = new HBox(UserDiskListTitleLabel);
        WC2.setAlignment(Pos.TOP_CENTER);
        WC2.setPadding(new Insets(0, 0, 0, 12));
        WC2.setTranslateY(0);
        
        MainGP.add(WC2,0,0);           
        
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
            JsonArray list_jsonArr = (JsonArray) data_jp.parse(sslist_data);
            
            CreateSnapshotList(list_jsonArr);
            
            conn.disconnect();
        } catch (MalformedURLException e) {
           System.out.println("MalformedURLException: " + e + "\n");             
	} catch (IOException e) {    
           System.out.println("IOException: " + e + "\n");
            JsonArray list_jsonArr = null;

           CreateSnapshotList(list_jsonArr);
        } 

        
    }    
    // 產生列表
    public void CreateSnapshotList(JsonArray jsondata) {
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
        UDSNCol.setPrefWidth(60);
        UDSNCol.setMinWidth(60);
        UDSNCol.setMaxWidth(60);        
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
        
        StateCol  = new TableColumn(WordMap.get("Snapshot_State"));
        StateCol.setPrefWidth(100);
        StateCol.setCellFactory(getCustomCellFactory());
        
        CreateTimeCol = new TableColumn(WordMap.get("Snapshot_CreateTime"));
        CreateTimeCol.setPrefWidth(190);
        CreateTimeCol.setMinWidth(190);
        CreateTimeCol.setMaxWidth(190);            
        
        SizeCol   = new TableColumn(WordMap.get("Snapshot_Size"));     
        SizeCol.setPrefWidth(100);
        SizeCol.setMinWidth(100);
        SizeCol.setMaxWidth(100);
        SizeCol.setStyle( "-fx-alignment: center-right;"); 
        
        DescCol   = new TableColumn(WordMap.get("Snapshot_Description"));     
        DescCol.setPrefWidth(180);
        DescCol.setStyle( "-fx-alignment: center-left;"); 
        
        ActionCol = new TableColumn(WordMap.get("Action"));    // 2018.07.06 快照新增還原按鈕
        ActionCol.setPrefWidth(160);
        ActionCol.setMinWidth(160);
        ActionCol.setMaxWidth(160);          
        // 設定每一個欄位對應的JavaBean物件與Property名稱    
        UDSNCol.setCellValueFactory(new Callback<CellDataFeatures<UserDisk, UserDisk>, ObservableValue<UserDisk>>() {
             @Override
             public ObservableValue<UserDisk> call(CellDataFeatures<UserDisk, UserDisk> s) {
                return new ReadOnlyObjectWrapper(s.getValue());
            }
        });                        
        StateCol.setCellValueFactory(new PropertyValueFactory<>("State"));                           
        CreateTimeCol.setCellValueFactory(new PropertyValueFactory<>("CreateTime"));
        SizeCol.setCellValueFactory(new PropertyValueFactory<>("Size"));
        DescCol.setCellValueFactory(new PropertyValueFactory<>("Desc"));
        ActionCol.setCellValueFactory(new PropertyValueFactory<>("Action"));   
                    
        if(jsondata != null) {
            if(jsondata.size() == 0)
                _array = new UserDisk[1];            
            else
                _array = new UserDisk[jsondata.size()];        
        } else {
            _array = new UserDisk[1];        
        }
                    
        getlist_jsonArr = jsondata;
        
        // 判斷傳入DATA的json是否為空
        if(jsondata != null && sslist_json_length_empty == false) {                        
            for (int i = 0; i < jsondata.size(); i++) {
                term       = jsondata.get(i).getAsJsonObject();  
                Layer      = term.get("Layer").getAsString();                
                State      = term.get("State").getAsString();                
                if (State.equals("2")) { setViewLayerID(Layer); }                 
                State      = UD_StatusName(term.get("State").getAsString(), StateCol);                
                CreateTime = term.get("CreateTime").getAsString();                                                         
                Size       = formatFileSize(term.get("Size").getAsLong(),false);                
                Desc       = term.get("Desc").getAsString();      

                _array[i] = new UserDisk(Layer, State, CreateTime, Size, Desc);            
            }          
               
            CreateTableBtn(ActionCol); // 產生列表按鈕               
        } else {
            _array[0] = new UserDisk("", "", "", "", "");
        }
        
        // 2019.01.02 view D Drive
        userdisk_data.addAll(_array);
        _checkUserDiskarray = _array;
        
        data_refresh();

        userdisktable.setEditable(true);       
        userdisktable.getColumns().addAll(UDSNCol, ActionCol, StateCol, DescCol, CreateTimeCol, SizeCol);
        userdisktable.setItems(userdisk_data);  
        userdisktable.getStyleClass().add("table_header");
        userdisktable.setPrefSize(ud_listView_width, ud_listView_height);
        userdisktable.setMaxSize(ud_listView_width , ud_listView_height);
        userdisktable.setMinSize(ud_listView_width , ud_listView_height); 

        HBox ud_selection = new HBox();
	ud_selection.setSpacing(20);       
        ud_selection.setAlignment(Pos.CENTER);
        ud_selection.setPadding(new Insets(0, 0, 0, 0));  
	ud_selection.getChildren().addAll(userdisktable);   // 使用Table Column顯示     

        MainGP.add(ud_selection,0,1);         
    }   
    // 產生列表按鈕
    public void CreateTableBtn (TableColumn<UserDisk, String> ActionCol) {
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
        
    public final void BottomView() {
        Label _desc1 = new Label(WordMap.get("Desc_Snapshot_operation1"));
        _desc1.setId("List_label");
        Label _desc2 = new Label(WordMap.get("Desc_Snapshot_operation2"));
        _desc2.setId("List_label");
                                              
        Btn_stopview = new Button("  " + WordMap.get("Snapshot_Stop_View") + "  ");
        Btn_stopview.setPrefSize(btn_width, btn_height);
        Btn_stopview.setMaxSize(btn_width, btn_height); 
        Btn_stopview.setMinSize(btn_width, btn_height); 
        Btn_stopview.setOnAction((ActionEvent event) -> {
            try { StopUserDiskLayer(); } 
            catch (IOException ex) { Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex); } catch (InterruptedException ex) { Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex); }
        }); 
        
        // 建立按鈕物件(離開)
        Btn_exit = new Button("  "+WordMap.get("Snapshot_Exit")+"  ");  
        Btn_exit.setPrefSize(btn_width, btn_height);
        Btn_exit.setMaxSize(btn_width, btn_height); 
        Btn_exit.setMinSize(btn_width, btn_height); 

        Btn_login = new Button("  " + WordMap.get("Snapshot_Login") + "  ");  
        Btn_login.setPrefSize(btn_width, btn_height);
        Btn_login.setMaxSize(btn_width , btn_height);
        Btn_login.setMinSize(btn_width , btn_height);

        Btn_login.setOnAction((ActionEvent event) -> {
            Login_lock();
                    
            Task<Void> loginTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    RespCode = QueryLogin();

                    return null;
                }
            };                    
                    
            loginTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) { login_timerflag = false; }
            });                    
                
            loginTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
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
            
            new Thread(loginTask).start(); 
        });                
        
        VBox Desc = new VBox();
	Desc.setSpacing(10);
        if(WordMap.get("SelectedLanguage").equals("English")){ 
            Desc.setTranslateY(-5);
            Desc.setTranslateX(-47);          
        } else if((WordMap.get("SelectedLanguage").equals("TraditionalChinese")) || (WordMap.get("SelectedLanguage").equals("SimpleChinese"))){
            Desc.setTranslateY(-5);
            Desc.setTranslateX(-47);        
        }
        Desc.setAlignment(Pos.CENTER_LEFT);
        Desc.setPadding(new Insets(0, 0, 0, 0));        
        Desc.getChildren().addAll(_desc1, _desc2);         
        
        HBox btn = new HBox();
	btn.setSpacing(15);
        btn.setAlignment(Pos.CENTER_RIGHT);
        btn.setPadding(new Insets(0, 0, 0, 0));   
        btn.setTranslateY(-5);
        btn.setTranslateX(145);
        btn.getChildren().addAll(Btn_exit, Btn_stopview, Btn_login);   
        
        BottomGP.add(Desc,0,0);		
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
                            if(IsView) {
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
                ViewUserDiskLayer();
            } catch (Exception e) {

            } 
        } else if (rtn == buttonTypeCancel) {
            IsKeepNIC  = false;
             // 2018.07.12 還原按鈕和修改dialog 
            System.out.println("IsKeepNIC: " + IsKeepNIC +  "layer: " + l + "\n");
            try {
                setViewLayerID(l);
                ViewUserDiskLayer();
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
                RollBackUserDiskLayer();
            } catch (IOException ex) { Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex); } catch (InterruptedException ex) { Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex); }     
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
            StopAll();
        }          
    }    
    /*------------------------ Polling 取值 ------------------------*/
    public void data_refresh() {  
        fresh_timer = new Timer(); 

        TimerTask sstask = new TimerTask() {    
            @Override
            public void run() {

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

                    JsonArray list_jsonArr = (JsonArray) data_jp.parse(sslist_data);

                    getlist_jsonArr = list_jsonArr;
                    BtnDisable_flag = 0;


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
                        }         


                        State      = UD_StatusName(term.get("State").getAsString(), StateCol);
                        CreateTime = term.get("CreateTime").getAsString();                                                         
                        Size       = formatFileSize(term.get("Size").getAsLong(),false);                
                        Desc       = term.get("Desc").getAsString();      


                        _array[i] = new UserDisk(Layer, State, CreateTime, Size, Desc);

                    }          

                    if (BtnDisable_flag>0) {
                        Btn_login.setDisable(false);
                        Btn_stopview.setDisable(false);
                    } else {
                        Btn_login.setDisable(true);
                        Btn_stopview.setDisable(true); 
                    }

                    CreateTableBtn(ActionCol);   
        //        sstable.refresh();  
        //        ss_data.removeAll(ss_data);

                    if(sslist_json_length_empty == false) {
                        if(list_jsonArr != null && list_jsonArr.size() > 0 ) { // 2018.07.06 快照新增還原按鈕                                        

                            if(!equals(_checkUserDiskarray, _array)) {
        //                        System.out.println("_array : " + _array.length + ", ss_data : " + ss_data.size());
                                userdisk_data.removeAll(userdisk_data);
                                userdisk_data.addAll(_array); 
                                _checkUserDiskarray = _array;
                                userdisktable.refresh();        
                                System.out.println("fresh Snapshot view");
                            }   
        //                    System.out.println("Snapshot 2");
                        }
                    } else {
                        // 2019.01.02 view D Drive
                        _array[0] = new UserDisk("", "", "", "", "");
                        userdisk_data.clear(); // 清空data先前的內容
                        //ss_data.add(new SS("", "", "", "", ""));
                        userdisk_data.addAll(_array);
                        _checkUserDiskarray = _array;
        //                System.out.println("Snapshot empty");
                    }


                    conn.disconnect();
                    
                    vddata_refresh();
                } catch (MalformedURLException e) {} catch (IOException e) {} 

            }
            
            // 2019.01.02 view D Drive
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
    
    public void vddata_refresh() {
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
                    
                    // 2019.01.02 view D Drive
                    VD[] _array = new VD[jsondata.size()];
                    
                    _diskNamePolling = "";
                    try{
                        _diskNamePolling = returndata.get("DiskName").getAsString();
                    } catch(Exception e) {}                    
                    
                    setDiskName(_diskNamePolling);
                    
                    if(jsondata != null && jsondata.size() > 0 ) { // 2018.07.06 快照新增還原按鈕                                                               
                        // 顯示可選擇之VD
                        for (int i = 0; i < jsondata.size(); i++) {
                            JsonObject vd_term           = jsondata.get(i).getAsJsonObject();            
                            String vd_Name           = vd_term.get("VdName").getAsString();
                            String VdId              = vd_term.get("VdId").getAsString();
                            
                            _vdName = vd_Name;

                            System.out.println("Bind User Disk : " + vd_term.get("BindUserDisk").getAsBoolean());

                            if(VdId.equals(getVDID())) {
                                getIndex = i;
                            } 
                            
                            _array[i] = new VD(vd_Name, VdId);
                        }                             
                    }         
                    

                        vd_data.removeAll(vd_data);
                        vd_data.addAll(_array); 
                        _checkVDarray = _array;     
                        vdtable.refresh();    
                        
                    

                    
                    conn.disconnect();
                } catch (Exception ex) {}                

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
    public void CheckLogincompleted() {
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
            try { LoginUserDiskLayer(); } 
            catch (IOException ex) { Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex); } catch (InterruptedException ex) { Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex); }
        }
    }      
 
    /*------------------------ 動作功能 ------------------------*/ 
    public final void detectSelection() {         
        vdtable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() == 1){
                    vd_index = vdtable.getSelectionModel().getSelectedIndex();
                    
                    if(vd_index != -1) { // 2017.12.29 點空白出會出現error 
//                    Login_lock();
                        fresh_timer.cancel();
                        
                        Select_vdID = (String) vdtable.getSelectionModel().getSelectedItem().getVD_ID(); 
                        System.out.println("click on " + Select_vdID + "\n");
                        setVDID(Select_vdID);
                        data_refresh();
//                    Login_Unlock();
                    }
                }
            }
        });                     
        
        vdtable.setOnKeyPressed((event)-> {
           //event.get
           if(event.getCode() == KeyCode.ENTER){ 
                vd_index = vdtable.getSelectionModel().getSelectedIndex();
                fresh_timer.cancel();
                Select_vdID = (String) vdtable.getSelectionModel().getSelectedItem().getVD_ID();
                System.out.println("click on " + Select_vdID + "\n");
                setVDID(Select_vdID);
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
                Btn_login.requestFocus();
                event.consume();
           }
           if(event.getCode() == KeyCode.ESCAPE){ 
                StopAll();
                event.consume();
           }           
        });           
        
        
        Btn_exit.setOnAction((ActionEvent event) -> {
            StopAll();
        });            
    }        


     
     
    /*======================= 基本功能 =======================*/
    public void RunSpice(String address, String port) throws IOException, InterruptedException {                                 
        WriteSpiceText(address, port, CurrentUserName, CurrentPasseard);                    
        File f = new File("jsonfile/SpiceText.txt");       
        if(f.exists()) {    
            // 2018.10.11 william G300s Mouse問題修改
            QM.CheckRmProcess(public_stage);
        }                                       
    }      
    
    public void WriteSpiceText(String address, String port, String Uname, String Password) { // 建立Spice config檔
        
        File myFile = new File("jsonfile/SpiceText.txt");

        if(myFile.exists()) {
            myFile.delete();
            myFile = null;
        }
        
        try {    
            FileWriter SWriter = new FileWriter("jsonfile/SpiceText.txt",true);
            try (BufferedWriter bufferedSWriter = new BufferedWriter(SWriter)) {
                bufferedSWriter.write("[virt-viewer]");
                bufferedSWriter.newLine();
                bufferedSWriter.write("type = spice");
                bufferedSWriter.newLine();
                bufferedSWriter.write("host= ");
                bufferedSWriter.write(address);               
                bufferedSWriter.newLine();
                bufferedSWriter.write("port = ");
                bufferedSWriter.write(port);
                bufferedSWriter.newLine();              
                bufferedSWriter.write("username = ");
                bufferedSWriter.write(Uname);
                bufferedSWriter.newLine();
                bufferedSWriter.write("password = ");
                bufferedSWriter.write(Password);
                bufferedSWriter.newLine();
                bufferedSWriter.write("fullscreen = 1");
                bufferedSWriter.newLine();
                bufferedSWriter.write("delete-this-file = 1");
                bufferedSWriter.newLine();
                bufferedSWriter.write("title =  ");
                bufferedSWriter.write(Uname);
                bufferedSWriter.write("-");
                bufferedSWriter.write(address);
                bufferedSWriter.newLine();           
                bufferedSWriter.flush();
                bufferedSWriter.close();                   
            }            
        } catch(IOException e) {}
    } 

    public static String formatFileSize(long size, boolean si) { // 檔案大小轉換
        String hrSize     = null;
        int unit         = (si ? 1000 : 1024);

        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(unit));
        return new DecimalFormat("#,##0.00").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
        // 0 顯示, # 不顯示
    } 
    
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

    private Callback<TableColumn<UserDisk, String>, TableCell<UserDisk, String>> getCustomCellFactory() { // final String color
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
        Snapshot_secondStage.close();
    }     
    
    /*======================= Exception =======================*/
    public void NoUserDiskfunc() {
           
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
        alert.setTitle(WordMap.get("Alert_Title_Warning"));          // 設定對話框視窗的標題列文字
        alert.setHeaderText("");                                     // 設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭

        
        if(WordMap.get("SelectedLanguage").equals("English")) {
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
        try {                       
            login_timerflag = true;
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
                login_timerflag = false;                                  
            }   
            conn.disconnect();
        } catch (MalformedURLException e) {} catch (IOException e) {}       
    }
    
    public void Alert_HttpsStatusError(int conn){
        if(conn != 200) {           
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
            alert.setTitle(WordMap.get("Message_Login"));
            alert.setHeaderText(null);
                          
            alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
            alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE); 

            alert.setContentText(
                WordMap.get("WW_Header") + " ： " + WordMap.get("Message_Unknown_system_error") + " ( " + conn + " ) " 
                + "\n" + "\n" +
                WordMap.get("Message_Suggest") + " ： " + WordMap.get("Message_Error_400")
            );
            
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(buttonTypeOK); 
            
            
            final Optional<ButtonType> opt = alert.showAndWait();
            final ButtonType rtn = opt.get();                     
            
            if (rtn == buttonTypeOK) {                            
                login_timerflag = false;
                Login_Unlock();
            }                        
        }
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
        
        
    /*======================= 資料處理 =======================*/
    /*------------------------ 取值＆資料模型 ------------------------*/
    
    public class VD { // 定義VD資料模型        
        private final SimpleStringProperty VD_Name;
        private final SimpleStringProperty VD_ID;
        
        VD(String vd_Name, String vd_ID) {
            this.VD_Name           = new SimpleStringProperty(vd_Name);
            this.VD_ID             = new SimpleStringProperty(vd_ID);            
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
    }         

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
    
    public static synchronized String getViewLayerID() {
        return ViewLayer;
    }
    
    public static synchronized void setViewLayerID(String layer) {
        ViewLayer = layer;
    }
    
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
    
    public static synchronized String getDiskName() {
        return _diskName;
    }

    public static synchronized void setDiskName(String _name) {
        _diskName = _name;
    }        
    
    
    /*------------------------ End ------------------------*/        
    
    
}
