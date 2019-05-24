/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import com.google.gson.JsonArray;
import com.sun.javafx.scene.control.skin.TableViewSkin;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.Thread.sleep;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//import sun.awt.Mutex;

/**
 *
 * @author root
 */
public class GetWifiInfo extends Thread{
    /*------------------------ import檔案及匯入文字檔 ------------------------*/
    public Map<String, String> WordMap = new HashMap<>();      
    public JsonArray jsondata;    
    private Stage public_stage;      
    private Bounds mainBounds;    
    /*------------------------ 設定Layout ------------------------*/    
    Stage WifiList_thirdStage = new Stage();    
    private Scene thirdScene;    
    private BorderPane rootPane;                                // BorderPane框架
    private GridPane MainGP;                                    // BorderPane中間層 (TableView物件)     
    private HBox btn;     
    private VBox Desc;     
    private Label desc1;
    private Label desc2;    
    private Button Btn_exit;
    private Button Btn_confirm = new Button();
    private Button Btn_refresh;
    private Button Btn_connect;
    /*------------------------ 設定TableView------------------------*/
    private TableView<wifi> wifi_table    = new TableView();    
    // 預設值寫進表格內
    public ObservableList<wifi> wifi_data = FXCollections.observableArrayList();    
    // 表格內Column設定
    TableColumn wifi_SNCol;
    TableColumn<wifi, String> wifi_NameCol;     
    TableColumn<wifi, String> wifi_DescCol;    
    TableColumn<wifi, String> wifi_StatusCol; 
    TableColumn<wifi, String> wifi_SecurityCol;
    /*------------------------ 設定長寬 ------------------------*/
    int thirdScene_width      = 340; 
    int thirdScene_heighth    = 570; 
    int btn_width             = 80; 
    int btn_height            = 30; 
    int MainGP_width          = 340; 
    int MainGP_height         = 570; 
    int wifi_tableView_width  = 340; 
    int wifi_tableView_height = 395; // 385
    
    public GB GB;

    private String data;        
    public String Connecting_wifi_name_NoShow = "";

  
//    public boolean Is_wifi_security_wap    = true;
//    public boolean get_name_lock           = false;
//    public boolean get_security_wap1lock   = false;
//    public boolean get_security_wap2lock   = false;  
    
    

    public boolean wifi_Connect_Disconnect = false;    
    public boolean wifi_name_hidden        = false;    
    public boolean vision_flag             = false;
    
    private boolean stopFlag               = false;
    private boolean checkClose               = true;
    
    public TextField Connecting_wifi_Name;
    public TextField Connecting_Network_Name;

    private Button wifi_Btn;    
    private Button network_Btn;    
    public Label wifi_name_title;
    private Label network_show_desc1;
    private Label network_show_desc2;    
    private Label wifi_show_desc1;
    private Label wifi_show_desc2;       
//    sprivate Label wifi_pwd_desc;

    ArrayList<String> output_Get_wifi_config_Line_list = new ArrayList<String>();
    private StringBuilder compare_str_buf = new StringBuilder(); 
    
    Image lock_img    = new Image("images/lock.png"     , 30, 30, false, false);    
    Image unlock_img  = new Image("images/unlock.png"   , 30, 30, false, false); 
    Image network_img = new Image("images/Network.png"  , 32, 32 ,false, false);
    Image wifi_img    = new Image("images/Wifi_Full.png", 30, 30, false, false);
    ImageView lock    = new ImageView(lock_img);
    ImageView unlock  = new ImageView(unlock_img);          
    ImageView network = new ImageView(network_img);      
    ImageView wifi    = new ImageView(wifi_img);
    //每秒check  網路狀態
    Thread thread_checkconnect;
    Timer timer_checkconnect;
    TimerTask task_checkconnect;
    // 我的最愛-Radio讀寫檔使用
    JSONObject keepwifi;    
    public String json_wifiname;    
    
    private final UUID TC_uniqueKey = UUID.randomUUID();
    public AcroRed_VDI_Viewer ARVI;            
    
    
    BorderPane detailsPane = new BorderPane();
    
    PasswordField wifi_Pwd_TextField = new PasswordField();  
    TextField wifi_show_Pwd = new TextField();
    Label wifi_pwd_desc = new Label(); // "請輸入Wi-Fi金鑰"  
    int connect_type = -1;
    private boolean checkresult = false;            
    private int checkcount = 0;    
    public String CheckResultName;
    private  boolean npw_changeAfterSaved = true;
    private ToggleButton buttonNpw = new ToggleButton();
    Image EyeClose = new Image("images/CloseEye.png",18,18,false, false);     
    Image EyeOpen  = new Image("images/OpenEye5.png",18,18,false, false);   
    
    public int scrollToIndex  = 0;
    TableColumn currentSortColumn;   
    public int setGraphic_Number  = -1;
    private boolean networkFlag = false; 
    private boolean test_code = false; // true / false
    
    public String wlanName = "";
    
    /*--------------------------double click--------------------------*/
//    private Mutex click_lock = new Mutex(); 
//    int click_count = 0; 
//    Thread thread_click; 
    
     

    
    public GetWifiInfo(Stage primaryStage,AcroRed_VDI_Viewer inputARVI, Map<String, String> LangMap, boolean opentype) {
        createfolder();
        boolean open_type;
    //public GetWifiInfo(Stage primaryStage, Map<String, String> LangMap, boolean opentype) {
        ARVI = inputARVI;
        public_stage    = primaryStage;        
        WordMap         = LangMap;
        open_type       = opentype;        
        GB.wifi_Connectting_status = false;
        GB.lock_wifipwd           = false;
//        GB.reconnect_flag         = false;
        GB.reconnect_wifiname     = "";
        GB.reconnect_securitytype = "";
        
        /***  主畫面使用GRID的方式Layout ***/
        MainGP = new GridPane();
//        MainGP.setGridLinesVisible(true);            // 開啟或關閉表格的分隔線       
        MainGP.setAlignment(Pos.CENTER);       
        MainGP.setPadding(new Insets(35, 50, 50, 20)); // 設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        MainGP.setHgap(0);                             // 元件間的水平距離
   	  
        switch (WordMap.get("SelectedLanguage")) {     // 元件間的垂直距離 
            case "English":
                MainGP.setVgap(14);                    
                break;
            default:
                MainGP.setVgap(9);                     
                break;
        }          
        MainGP.setPrefSize(MainGP_width, MainGP_height);
        MainGP.setMaxSize(MainGP_width, MainGP_height);
        MainGP.setMinSize(MainGP_width, MainGP_height); 

        switch (WordMap.get("SelectedLanguage")) {
            case "English":
                MainGP.setTranslateY(17); 
                MainGP.setTranslateX(5); 
                break;
            default:
                MainGP.setTranslateY(13);  
                MainGP.setTranslateX(5); 
                break;
        }        
   
        /***  底層畫面使用Border的方式Layout ***/
        rootPane = new BorderPane();             
        rootPane.setCenter(MainGP);
        
        try {
            Get_Wifi_Config();
            stopFlag = false;
            networkconn_change();                        
        } catch (IOException ex) {
            Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*----------------------------------------------------------*/ 
        /*----------------------------------------------------------*/ 
        wifi_table.setRowFactory(tv -> new TableRow<wifi>() {
            Node detailsPane ;
            {                
                selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {   
//                    System.out.print("wifi table item: " + wifi_table.getSelectionModel().getSelectedItem() + "\n");
                    
                    detailsPane = createDetailsPane(wifi_table.getSelectionModel().getSelectedItem()); // wifi_table.getSelectionModel().selectedItemProperty()
                    
                    if (isNowSelected) {
                        /* delay */
                        final CountDownLatch count_down = new CountDownLatch(1);
                        Task<Void> sleeper = new Task<Void>() { 
                            @Override 
                            protected Void call() throws Exception { 
                                try {                                 
                                    Thread.sleep(300);    
                                    count_down.countDown();

                                } catch (InterruptedException e) { 
                                } 
                                return null; 
                            } 
                        };                    
                        new Thread(sleeper).start(); 
                        try {
                            count_down.await();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
                        }
//                        System.out.println("Open Detail");
                        
                        getChildren().add(detailsPane);
                        
                    } else {
//                        System.out.println("Close Detail");
                        getChildren().remove(detailsPane);
                    }
                    this.requestLayout();
                    
                    
                   
                });
                
                // double click
                setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (! this.isEmpty()) ) {
//                        scrollToIndex = wifi_table.getSelectionModel().getSelectedIndex();
//                        wifi_table.scrollTo(scrollToIndex);
//                        System.out.println("scroll To Index: " + scrollToIndex);                        
                        wifi rowData = this.getItem();
                        // System.out.println("Double click on: "+rowData.getwifi_Name());
                        Row_Click_Connect(rowData.getwifi_Name(), rowData.getwifi_Security());
                    } else {
                        
//int[] range = getVisibleRange();
//System.out.println("Der Index der ersten sichbaren Zeile lautet: " + range[0]);
//System.out.println("Der Index der zweiten sichbaren Zeile lautet: " + range[1]);                        
                        
                        // System.out.println("click on");
//                        scrollToIndex = wifi_table.getSelectionModel().getSelectedIndex();
//                        wifi_table.scrollTo(scrollToIndex-3);
//                        System.out.println("scroll To Index: " + scrollToIndex);
                    }                    
                }); 
            }
            
            @Override
            protected double computePrefHeight(double width) {
                if (isSelected()) {
//                    System.out.println("step1");
                    return super.computePrefHeight(width)+detailsPane.prefHeight(getWidth());
                } else {
//                    System.out.println("step2");
                    return super.computePrefHeight(width);
                }
            }

            @Override
            protected void layoutChildren() {
                super.layoutChildren();
                if (isSelected()) {
//                    System.out.println("step3");
                    double width = getWidth();
                    double paneHeight = detailsPane.prefHeight(width);
                    detailsPane.resizeRelocate(0, getHeight()-paneHeight, width, paneHeight);
                } 
            }
        });      
                    
        // 畫面上層
        top_layout();
        // 畫面中層
        vd_layout();
        // 畫面下層 (描述及按鈕)
        bottom_layout();

        Platform.runLater(new Runnable() {
            public void run() {
                rootPane.requestFocus(); // 防wifi_Btn按鈕變色
            }
        });
        
        
//        detectSelection();
//        clickdectect(); // double click   
        
        ChangeLabelLang(network_show_desc1, network_show_desc2, wifi_SNCol, wifi_NameCol, wifi_SecurityCol,  wifi_show_desc1, wifi_show_desc2); // wifilist_Title, wifi_name_title,
        ChangeButtonLang(Btn_refresh, Btn_confirm, wifi_Btn);
        
        wifi_table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY); 
        
        /* 針對sort處理用的-無中間態(unsorted) */
        // set default sort column
        currentSortColumn = wifi_table.getColumns().get(0);
        wifi_table.getSortOrder().setAll(currentSortColumn);

        // update current sort column after sorting changes
        for (TableColumn column : wifi_table.getColumns()) {
            column.sortTypeProperty().addListener((observable, oldValue, newValue) -> {
                currentSortColumn = column;
            });
        }
        // if table loses comparator restore the sort order!
        wifi_table.comparatorProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                wifi_table.getSortOrder().setAll(currentSortColumn);
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
        
        thirdScene = new Scene(rootPane, thirdScene_width, thirdScene_heighth); // 590, 365
        thirdScene.getStylesheets().add("wifi.css");
        
        mainBounds = primaryStage.getScene().getRoot().getLayoutBounds();    
        Bounds rootBounds = thirdScene.getRoot().getLayoutBounds();
        
        WifiList_thirdStage.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if(ov.getValue() == false && !GB.lock_wifipwd) { 
                stopFlag = true;
                System.out.println("close : " + checkClose);
                if(checkClose)
                    WifiList_thirdStage.close();
//                timer_checkconnect.cancel();
                GB.lock_wifilist = false;
            }
        });
                                                                        
        WifiList_thirdStage.setX(primaryStage.getX() + (mainBounds.getWidth() - 342 ));
        WifiList_thirdStage.setY(primaryStage.getY() + (mainBounds.getHeight() - 660 )); 
        WifiList_thirdStage.setScene(thirdScene);
        WifiList_thirdStage.setTitle("Wi-Fi");  // 視窗標題
        switch (GB.JavaVersion) {
            case 0:
                WifiList_thirdStage.getIcons().add(new Image("images/Icon2.png"));
                break;
            case 1:
                WifiList_thirdStage.getIcons().add(new Image("images/MCIcon2.png"));
                break;                
            default:
                break;
        }         
                 
        WifiList_thirdStage.initStyle(StageStyle.DECORATED); // DECORATED -> UNDECORATED 
        WifiList_thirdStage.initModality(Modality.NONE);     // window modal lock NONE -> WINDOW_MODAL ->  APPLICATION_MODAL
        WifiList_thirdStage.initOwner(primaryStage);
        WifiList_thirdStage.setResizable(false);             // 將視窗放大,關閉    
        if(open_type) {
            WifiList_thirdStage.show();                      // important *** lock and return value : show -> showAndWait
        } else {
            WifiList_thirdStage.showAndWait();
        }
        
        // 新增Stage關閉事件
        WifiList_thirdStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
            System.out.println("wifi list Stage is closing");
            stopFlag = true;
            System.out.println("close : " + checkClose);
            if(checkClose)
                WifiList_thirdStage.close();
//            timer_checkconnect.cancel();
            GB.lock_wifilist = false;
          }
      }); 
        
    }      
    
    // 取連線的wlan名稱
    public void Get_Wlan_Name() throws IOException { 
                            
        Process Detect_wlan_Process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli d status | grep wlan[0-9] | awk '{print $1}'"});

        try (BufferedReader output_wlan = new BufferedReader (new InputStreamReader(Detect_wlan_Process.getInputStream()))) {
            wlanName = output_wlan.readLine();                                                  
            output_wlan.close();
        }               

    }       
    
    
    public int[] getVisibleRange(TableView table) {
        TableViewSkin<?> skin = (TableViewSkin) table.getSkin();
        if (skin == null) {
            return new int[] {0, 0};
        }
        VirtualFlow<?> flow = (VirtualFlow) skin.getChildren().get(1);
        int indexFirst;
        int indexLast;
        if (flow != null && flow.getFirstVisibleCellWithinViewPort() != null && flow.getLastVisibleCellWithinViewPort() != null) {
                indexFirst = flow.getFirstVisibleCellWithinViewPort().getIndex();

                if (indexFirst >= table.getItems().size()) 
                    indexFirst = table.getItems().size() - 1;

                indexLast = flow.getLastVisibleCellWithinViewPort().getIndex();

                if (indexLast >= table.getItems().size())
                    indexLast = table.getItems().size() - 1;
        } else {
            indexFirst = 0;
            indexLast = 0;
        }
        return new int[] {indexFirst, indexLast};
    }    
    
    private Node createDetailsPane(wifi item) {
        if(item != null){
            boolean no_type_pwd   = false;
            connect_type          = -1;
            // CheckResultName       = "";
            wifi_Pwd_TextField.setText("");    
            String Select_wifiName;
            String Select_security_type;                
            Select_wifiName       = "WiFi_" + item.getwifi_Name();                    
            Select_security_type  = item.getwifi_Security();                     

            File file=new File("/etc/NetworkManager/system-connections/temp/" + Select_wifiName);
            if(file.exists())
                no_type_pwd = true;  

            if(no_type_pwd) {
                vision_flag = false;   
                connect_type = 1;
//                System.out.println("1");
            } else if(Select_security_type.equals("Open") && !no_type_pwd) {
                vision_flag = false;   
                connect_type = 2;        
//                System.out.println("2");
            } else {        
                vision_flag = true; 
//                System.out.println("3");
            }        
            
//            System.out.println("Select_wifiName: " + item.getwifi_Name() + " , Select_security_type: " + Select_security_type + ", vision_flag: " + vision_flag );
       
            Connection_PWD_Desc(vision_flag);
                                
            Btn_confirm = new Button(WordMap.get("Connect"));     
            Btn_confirm.setPadding(new Insets(1, 0, 2, 0));
            switch (WordMap.get("SelectedLanguage")) {
                case "English":
                    Btn_confirm.setPrefSize(110,24);
                    Btn_confirm.setMaxSize(110,24);
                    Btn_confirm.setMinSize(110,24);
                    break;
                default:
                    Btn_confirm.setPrefSize(80,24);
                    Btn_confirm.setMaxSize(80,24);
                    Btn_confirm.setMinSize(80,24);
                    break;
            }         
        
            Btn_confirm.setTranslateY(7);
            Btn_confirm.setOnAction((ActionEvent event) -> {                
                data                  = Select_wifiName;            
                GB.wifi_name          = Select_wifiName;    
                CheckResultName       = Select_wifiName;    
                GB.wifi_security_type = Select_security_type;   
                                    
//                System.out.println("wifi Name: " + item.getwifi_Name() + "security type: " + item.getwifi_Security() + "wifi Pwd: " + wifi_Pwd_TextField.getText() + "\n");

                if(wifi_Pwd_TextField.getText().isEmpty()&&vision_flag) {
                    
                    wifi_pwd_desc.setText(WordMap.get("Message_Error_WiFi_01"));
                    
                    switch (WordMap.get("SelectedLanguage")) {
                        case "English":
                            wifi_pwd_desc.setStyle("-fx-font-family:'Ubuntu'; -fx-text-fill: red;-fx-font-size: 18px;"); 
                            break;
                        default:
                            wifi_pwd_desc.setStyle("-fx-font-family: 'Droid Sans Fallback'; -fx-text-fill: red;-fx-font-size: 18px;");
                            break;
                    }                                     
                
                } else {              
                    Connection_PWD_Desc(vision_flag);
                    if(vision_flag) {
                        Connection_FirstTime(item.getwifi_Name(), item.getwifi_Security(), wifi_Pwd_TextField.getText());
                        System.out.println("Connection_FirstTime");
                    } else {                    
                        switch (connect_type) {
                            case 1:
                                Connection_RememberPWD(item.getwifi_Name()); 
                                System.out.println("Connection_RememberPWD");
                                break;
                            case 2:
                                Connection_Security_Open(item.getwifi_Name(), item.getwifi_Security(), "");                             
                                System.out.println("Connection_Security_Open");
                                break;                            
                            default:                            
                                break;
                        }                       
                    }
                    Btn_confirm.setDisable(true);     
                    wifi_Btn.setDisable(true);                    
                }
            });         

        
        HBox WiFI_Btn = new HBox();
	WiFI_Btn.setSpacing(8);       
        WiFI_Btn.setAlignment(Pos.CENTER_RIGHT);
        WiFI_Btn.setPadding(new Insets(0, 0, 0, 0));          
        WiFI_Btn.getChildren().addAll(Btn_confirm);  
        
        wifi_Pwd_TextField.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Connection_PWD_Desc(vision_flag);
            }
        });           

        VBox labels = new VBox(5, wifi_pwd_desc, wifi_Pwd_TextField, WiFI_Btn); // new Label("These are the")
        labels.setAlignment(Pos.CENTER_LEFT);
        labels.setPadding(new Insets(15, 20, 20, 20)); // 設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        detailsPane.setCenter(labels);

        detailsPane.setStyle("-fx-background-color: rgb(0,180,240);-fx-border-width: 1 0;-fx-border-color: #cfcfcf;"); //  ->  -fx-background; -fx-background: linear-gradient(derive(#0049a6, 120%), derive(#0049a6, 90%))


        }
        return detailsPane ;
    
    }    
    
    public void Row_Click_Connect(String name, String security_type) {             
        String row_wifi_name          = "WiFi_" + name;                    
        String row_wifi_security_type = security_type;   
        boolean no_type_pwd   = false;
        boolean row_vision_flag   = false;
        int row_connect_type          = -1;          
        wifi_Pwd_TextField.setText("");  
        
            File file=new File("/etc/NetworkManager/system-connections/temp/" + row_wifi_name);
            if(file.exists())
                no_type_pwd = true;  

            if(no_type_pwd) {
                row_vision_flag = false;   
                row_connect_type = 1;
            } else if(row_wifi_security_type.equals("Open") && !no_type_pwd) {
                row_vision_flag = false;   
                row_connect_type = 2;        
            } else {        
                row_vision_flag = true; 
            }         
            
            Connection_PWD_Desc(row_vision_flag);
            
                if(wifi_Pwd_TextField.getText().isEmpty()&&row_vision_flag) {
                    
                    wifi_pwd_desc.setText(WordMap.get("Message_Error_WiFi_01"));
                    
                    switch (WordMap.get("SelectedLanguage")) {
                        case "English":
                            wifi_pwd_desc.setStyle("-fx-font-family:'Ubuntu'; -fx-text-fill: red;-fx-font-size: 18px;"); 
                            break;
                        default:
                            wifi_pwd_desc.setStyle("-fx-font-family: 'Droid Sans Fallback'; -fx-text-fill: red;-fx-font-size: 18px;");
                            break;
                    }                                     
                
                } else {              
                    Connection_PWD_Desc(row_vision_flag);
                    if(row_vision_flag) {
                        Connection_FirstTime(name, security_type, wifi_Pwd_TextField.getText());
                        System.out.println("Connection_FirstTime");
                    } else {                    
                        switch (row_connect_type) {
                            case 1:
                                Connection_RememberPWD(name); 
                                System.out.println("Connection_RememberPWD");
                                break;
                            case 2:
                                Connection_Security_Open(name, security_type, "");                             
                                System.out.println("Connection_Security_Open");
                                break;                            
                            default:                            
                                break;
                        }                       
                    }
                    Btn_confirm.setDisable(true);     
                    wifi_Btn.setDisable(true);                    
                }            
            
            
    
    }
    
    
    public void Connection_PWD_Desc(boolean show) {
        if(show) {
            wifi_Pwd_TextField.setVisible(true);
            wifi_pwd_desc.setText(WordMap.get("Message_advice_WiFi_01"));
        } else {
            wifi_Pwd_TextField.setVisible(false);
            wifi_pwd_desc.setText(WordMap.get("Message_advice_WiFi_02"));
        }        
                
        switch (WordMap.get("SelectedLanguage")) {
            case "English":
                wifi_pwd_desc.setStyle("-fx-font-family:'Ubuntu'; -fx-text-fill: white;-fx-font-size: 18px;"); 
                break;
            default:
                wifi_pwd_desc.setStyle("-fx-font-family: 'Droid Sans Fallback'; -fx-text-fill: white;-fx-font-size: 18px;");
                break;
        }         
    }    
    
    public void Connection_FirstTime(String name, String type, String pwd) {
        try {                 
                     
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli d disconnect iface " + wlanName }).waitFor();           
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","rm /etc/NetworkManager/system-connections/WiFi_*" }).waitFor();
            Set_Disconnect_Icon();            
            // System.out.println("Write File : "  + name + " " + pwd);
            writeWifiDHCPSetting(name, type, pwd);   
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","chmod 600 '/etc/NetworkManager/system-connections/WiFi_" + name + "'"}).waitFor();
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","killall nmcli;sleep 1;nmcli c;nmcli c up id '" + name + "' &" }).waitFor();                      
            Set_Connecting_WiFiName(name);                        
        } catch (Exception ex) {
            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
        }         
        GB.wifi_Connectting_status = true;
        checkresult                = true;                              
    }
    
    public void Connection_RememberPWD(String name) {
        try {                        
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli d disconnect iface " + wlanName }).waitFor();
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","rm /etc/NetworkManager/system-connections/WiFi_*" }).waitFor();
            Set_Disconnect_Icon();            
            String exec = "cp '/etc/NetworkManager/system-connections/temp/WiFi_" + name + "' /etc/NetworkManager/system-connections/;sync" ;
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",exec}).waitFor();
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","killall nmcli;sleep 1;nmcli c;nmcli c up id '" + name + "' &" }).waitFor();                              
            Set_Connecting_WiFiName(name);
        } catch (Exception ex) {
            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
        }        
        GB.wifi_Connectting_status = true;
    }    
    
    public void Connection_Security_Open(String name, String type, String pwd) {
        try {                        
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli d disconnect iface " + wlanName }).waitFor();
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","rm /etc/NetworkManager/system-connections/WiFi_*" }).waitFor();
            Set_Disconnect_Icon();            
            writeWifiDHCPSetting(name, type, pwd);                 
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","chmod 600 '/etc/NetworkManager/system-connections/WiFi_" + name + "'"}).waitFor();                                                                             
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","killall nmcli;sleep 1;nmcli c;nmcli c up id '" + name + "' &" }).waitFor();          
            Set_Connecting_WiFiName(name);
        } catch (Exception ex) {
            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
        }      
        GB.wifi_Connectting_status = true;
    }      
    
    public void Set_Connecting_WiFiName(String name) {
        Platform.runLater(new Runnable() {
            public void run() {
                wifi_show_desc1.setText(name);
            }
        });        
    }
    
    private static <S,T> TableColumn<S,T> column(String title, Function<S, ObservableValue<T>> property) {
        TableColumn<S,T> col = new TableColumn<>(title);
        col.setCellValueFactory(cellData -> property.apply(cellData.getValue()));
        col.setPrefWidth(150);
        return col ;
    }    
            
    /*------------------------ 畫面layout ------------------------*/ 
    // 畫面上層
    public final void top_layout() {
        network_show_desc1 = new Label(WordMap.get("Network"));
        GridPane.setHalignment(network_show_desc1, HPos.LEFT);         
        network_show_desc1.setPadding(new Insets(0, 0, 0, 0));        
             
        network_show_desc2 = new Label();
        GridPane.setHalignment(network_show_desc2, HPos.LEFT);         
        network_show_desc2.setPadding(new Insets(0, 0, 0, 0));     
        
//        if("Japanese".equals(WordMap.get("SelectedLanguage"))){                 
//            network_show_desc2.setPrefWidth(110);
//        }         
        
        network_Btn = new Button();
        network_Btn.setPadding(new Insets(1, 0, 2, 0));
        GridPane.setHalignment(network_Btn, HPos.RIGHT);   
        if("English".equals(WordMap.get("SelectedLanguage"))){                 
            network_Btn.setPrefSize(110,24);
            network_Btn.setMaxSize(110,24);
            network_Btn.setMinSize(110,24);
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){           
            network_Btn.setPrefSize(80,24);
            network_Btn.setMaxSize(80,24);
            network_Btn.setMinSize(80,24);
        }          
        
          
               
        network_Btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {     
                if(networkFlag) {    
                    Btn_Disconnect_forNetwork();
                } else {                    
                    Btn_Connect_forNetwork();
                }
            }
        });           
        
        HBox network_Disconnect_vbox = new HBox();
	  
        
        switch (WordMap.get("SelectedLanguage")) {
            case "English":
                network_Disconnect_vbox.setSpacing(80);
                break;
            case "Japanese":                
                network_Disconnect_vbox.setSpacing(140);
                break;                
            default:
                network_Disconnect_vbox.setSpacing(160);
                break;
        }         
                
        network_Disconnect_vbox.setAlignment(Pos.CENTER_LEFT);
        network_Disconnect_vbox.setPadding(new Insets(0, 0, 0, 0));          
        network_Disconnect_vbox.getChildren().addAll(network_show_desc2, network_Btn);           
        
        VBox Network_status_vbox = new VBox();
	Network_status_vbox.setSpacing(0);       
        Network_status_vbox.setAlignment(Pos.CENTER_LEFT);
        Network_status_vbox.setPadding(new Insets(0, 0, 0, 0));          
        Network_status_vbox.getChildren().addAll(network_show_desc1, network_Disconnect_vbox);    
        
        HBox Network_Name = new HBox();
	Network_Name.setSpacing(8);       
        Network_Name.setAlignment(Pos.CENTER_LEFT);
        Network_Name.setPadding(new Insets(0, 0, 0, 0));  
	Network_Name.getChildren().addAll(network, Network_status_vbox);    
      
        Network_Name.setTranslateX(10);
        
        HBox Board = new HBox();
        Board.setTranslateY(11);
        Board.setStyle("-fx-border-width: 0 0 1 0;-fx-border-color: white;");
        MainGP.add(Board,0,1);                   
        
        
        try {
            Get_Network_Status();
        } catch (IOException ex) {
            Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        MainGP.add(Network_Name,0,1);        

    }
    
    public void Btn_Disconnect_forNetwork() {
        try {
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","ifconfig " + GB.eth_Str + " down" }).waitFor();
        } catch (Exception ex) {
            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
        }                
        network_Btn.setDisable(true);     
    }
    
    public void Btn_Connect_forNetwork() {
        try {
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","ifconfig " + GB.eth_Str + " up" }).waitFor();
        } catch (Exception ex) {
            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
        }                                 
        network_Btn.setDisable(true);    
    }        
    
    
    // 畫面中層
    public final void vd_layout() {             
        wifi_show_desc1 = new Label();
        GridPane.setHalignment(wifi_show_desc1, HPos.LEFT);         
        wifi_show_desc1.setPadding(new Insets(0, 0, 0, 0));          
        
        wifi_show_desc2 = new Label();
        GridPane.setHalignment(wifi_show_desc2, HPos.LEFT);         
        wifi_show_desc2.setPadding(new Insets(0, 0, 0, 0));     
        
        
        if(test_code) {
            wifi_show_desc1.setText("ABCD12334848456");
            wifi_show_desc2.setText(WordMap.get("Disconnected"));        
        }

        
        /**********Disconnect************/ 
        wifi_Btn = new Button();
        wifi_Btn.setPadding(new Insets(1, 0, 2, 0));
        GridPane.setHalignment(wifi_Btn, HPos.RIGHT);   
        if("English".equals(WordMap.get("SelectedLanguage"))){                 
            wifi_Btn.setPrefSize(110,24);
            wifi_Btn.setMaxSize(110,24);
            wifi_Btn.setMinSize(110,24);
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){           
            wifi_Btn.setPrefSize(80,24);
            wifi_Btn.setMaxSize(80,24);
            wifi_Btn.setMinSize(80,24);
        }          
        
         
        
        
        wifi_Btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {     
                if(wifi_Connect_Disconnect) {    
                    Btn_Disconnect();
                } else {                    
                    Btn_Connect();
                }
            }
        });                 
        
        
        
        HBox WiFI_Disconnect_vbox = new HBox();
	  
        
        switch (WordMap.get("SelectedLanguage")) {
            case "English":
                WiFI_Disconnect_vbox.setSpacing(80);
                break;
            case "Japanese":                
                WiFI_Disconnect_vbox.setSpacing(140);
                break;                 
            default:
                WiFI_Disconnect_vbox.setSpacing(160);
                break;
        }           
        
        
        WiFI_Disconnect_vbox.setAlignment(Pos.CENTER_LEFT);
        WiFI_Disconnect_vbox.setPadding(new Insets(0, 0, 0, 0));          
        WiFI_Disconnect_vbox.getChildren().addAll(wifi_show_desc2, wifi_Btn);          
        
        
        VBox WiFI_status_vbox = new VBox();
	WiFI_status_vbox.setSpacing(0);       
        WiFI_status_vbox.setAlignment(Pos.CENTER_LEFT);
        WiFI_status_vbox.setPadding(new Insets(0, 0, 0, 0));          
        WiFI_status_vbox.getChildren().addAll(wifi_show_desc1, WiFI_Disconnect_vbox);    
        
        HBox WiFI_Name = new HBox();
	WiFI_Name.setSpacing(8);       
        WiFI_Name.setAlignment(Pos.CENTER_LEFT);
        WiFI_Name.setPadding(new Insets(0, 0, 0, 0));  
	WiFI_Name.getChildren().addAll(wifi, WiFI_status_vbox);    
      
        WiFI_Name.setTranslateX(10);
        WiFI_Name.setTranslateY(5);                 
        MainGP.add(WiFI_Name,0,2);
        
        try {
            Get_Wifi_Status();
        } catch (IOException ex) {
            Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
        }        

        
        
       
        
        
        
        
        
        /*------------------------ 畫面顯示(將VD資料填入Table內) ------------------------*/
        wifi_data.clear(); // 清空data先前的內容        
                   
        /*------------------------ 讓Table內顯示項次 ------------------------*/
        wifi_SNCol = new TableColumn(WordMap.get("VD_SN"));    
        
        wifi_SNCol.setCellValueFactory(new Callback<CellDataFeatures<wifi, wifi>, ObservableValue<wifi>>() {
             @Override
             public ObservableValue<wifi> call(CellDataFeatures<wifi, wifi> v) {
                return new ReadOnlyObjectWrapper(v.getValue());
            }
        });        
        
        wifi_SNCol.setCellFactory(new Callback<TableColumn<wifi, wifi>, TableCell<wifi, wifi>>() {
            @Override 
            public TableCell<wifi, wifi> call(TableColumn<wifi, wifi> param) {
                return new TableCell<wifi, wifi>() {
                    @Override 
                    protected void updateItem(wifi item, boolean empty) {
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
        
        wifi_SNCol.setPrefWidth(60);
        wifi_SNCol.setMinWidth(60);
        //wifi_SNCol.setMaxWidth(60);        
        wifi_SNCol.setSortable(false);      
        /*------------------------ 讓Table內顯示名稱 ------------------------*/
                     
        switch (WordMap.get("SelectedLanguage")) {
            case "English":
                wifi_NameCol = new TableColumn("Wi-Fi Name");
                break;                
            case "Japanese":
                wifi_NameCol = new TableColumn("Wi-Fiお名前");
                break;
            case "SimpleChinese":
                wifi_NameCol = new TableColumn("Wi-Fi名称");
                break;                
            case "TraditionalChinese":
                wifi_NameCol = new TableColumn("Wi-Fi名稱");
                break;                                                 
            default:
                wifi_NameCol = new TableColumn("Wi-Fi Name");
                break;
        }         
        
        
        wifi_NameCol.setPrefWidth(268); 
        wifi_NameCol.setMinWidth(268);
//        wifi_NameCol.setStyle( "-fx-alignment: center-left;"); // https://stackoverflow.com/questions/13455326/javafx-tableview-text-alignment
        /*------------------------ 讓Table內顯示備註 ------------------------*/
//        wifi_DescCol = new TableColumn(WordMap.get("Comment"));    
//        wifi_DescCol.setPrefWidth(250);
//        wifi_DescCol.setStyle( "-fx-alignment: center-left;");    
        /*------------------------ 讓Table內顯示狀態 ------------------------*/
         wifi_StatusCol = new TableColumn(WordMap.get("Security")); 
        if("Japanese".equals(WordMap.get("SelectedLanguage"))) {
            wifi_StatusCol.setPrefWidth(120);
            wifi_StatusCol.setMinWidth(120);
        } 
        else { 
            wifi_StatusCol.setPrefWidth(100);
            wifi_StatusCol.setMinWidth(100);
        }          

         //wifi_StatusCol.setMaxWidth(100);
         //wifi_StatusCol.setCellFactory(getCustomWifiListCellFactory());     
         wifi_StatusCol.setCellFactory(param -> new getCustomWifiListCellFactory());     
         /*------------------------ 讓Table內顯示Security ------------------------*/
         wifi_SecurityCol = new TableColumn(WordMap.get("Security")); 

            wifi_SecurityCol.setPrefWidth(150);
            wifi_SecurityCol.setMinWidth(150);
                  

         //wifi_SecurityCol.setMaxWidth(150);         
         
         
        
        // 設定每一個欄位對應的JavaBean物件與Property名稱        
        wifi_NameCol.setCellValueFactory(new PropertyValueFactory<wifi, String>("wifi_Name"));                        
        wifi_SecurityCol.setCellValueFactory(new PropertyValueFactory<wifi, String>("wifi_Security"));
        wifi_StatusCol.setCellValueFactory(new PropertyValueFactory<wifi, String>("wifi_Status"));
//        wifi_DescCol.setCellValueFactory(new PropertyValueFactory<wifi, String>("VD_Desc"));                                
//        wifi_StatusCol.setCellValueFactory(new PropertyValueFactory<wifi, String>("wifi_Status")); 
        
        // 顯示可選擇之VD
        try {            
            wifilist_show();
        } catch (IOException ex) {
            Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
               
        wifi_table.setEditable(false);       
        wifi_table.getColumns().addAll(wifi_StatusCol, wifi_NameCol);
        wifi_table.setItems(wifi_data);     
        /*Sort*/
        wifi_table.getSortOrder().setAll(Collections.singletonList(wifi_NameCol));
        wifi_NameCol.setSortType(TableColumn.SortType.ASCENDING);
        
        wifi_table.getStyleClass().add("table_header");
        wifi_table.setPrefSize(wifi_tableView_width, wifi_tableView_height);
        wifi_table.setMaxSize(wifi_tableView_width , wifi_tableView_height);
        wifi_table.setMinSize(wifi_tableView_width , wifi_tableView_height);         
        /*
        // 設定第一個元素為被選擇狀態
        wifi_table.requestFocus();
        wifi_table.getSelectionModel().select(0);
        wifi_table.getFocusModel().focus(0);
        
        
        if(!wifi_data.isEmpty()) {
            Select_wifiName       = (String) wifi_table.getSelectionModel().getSelectedItem().getwifi_Name();
            Select_security_type  = (String) wifi_table.getSelectionModel().getSelectedItem().getwifi_Security();
        }
        */
        
        HBox selection = new HBox();
	selection.setSpacing(20);       
        selection.setAlignment(Pos.CENTER);
        selection.setPadding(new Insets(0, 0, 0, 0));  
	selection.getChildren().addAll(wifi_table);    
        selection.setTranslateX(0); //17
        selection.setTranslateY(3); //15
        
        MainGP.add(selection,0,3);                          
    }    
    
    public void Btn_Disconnect(){
        try {
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli d disconnect iface " + wlanName }).waitFor();
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","rm /etc/NetworkManager/system-connections/WiFi_*" }).waitFor();
            Set_Disconnect_Icon();                        
        } catch (Exception ex) {
            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
        }                
        GB.wifi_Connectting_status = true;
        Btn_confirm.setDisable(true);     
        wifi_Btn.setDisable(true);     
    }
    
    public void Btn_Connect(){
        try {
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","rm /etc/NetworkManager/system-connections/WiFi_*" }).waitFor();
            String exec = "cp '/etc/NetworkManager/system-connections/temp/WiFi_" + wifi_show_desc1.getText() + "' /etc/NetworkManager/system-connections/;sync" ;
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",exec}).waitFor();
//                        File aaa = new File("/etc/NetworkManager/system-connections/temp/WiFi_" + wifi_show_desc1.getText());
//                        if(aaa.exists())
//                            System.out.println("connect wifi " +  wifi_show_desc1.getText());
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","killall nmcli;sleep 1;nmcli c;nmcli c up id '" + wifi_show_desc1.getText() + "' &" }).waitFor();
        } catch (Exception ex) {
            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
        }                                 
        GB.wifi_Connectting_status = true;
        Btn_confirm.setDisable(true);     
        wifi_Btn.setDisable(true);    
    }    
    
    private class getCustomWifiListCellFactory<S, T> extends TableCell<S, T> {

        @Override
        protected void updateItem(T item, boolean empty) {        
            int now_index = getIndex(); 
            String sorting_security;
            wifi now_wifi;
            super.updateItem(item, empty);
            if(!empty){ 
//                System.out.println("item: " + item + ", " + "empty: " + empty +"\n"); //https://stackoverflow.com/questions/34896299/javafx-change-tablecell-column-of-selected-tablerow-in-a-tableview
                
                now_wifi = (wifi)wifi_table.getItems().get(now_index);
                
                sorting_security = now_wifi.getwifi_Status();
                if(sorting_security.equals("安全") || sorting_security.equals("安全") || sorting_security.equals("Safe") ) {
                    setText(sorting_security);
                    setGraphic(new ImageView(lock_img));                
                } else {
                    setText(sorting_security);
                    setGraphic(new ImageView(unlock_img));
                }                
            } else {

                setText(null);
                setGraphic(null);
            }
        }        
    }     

    public void wifilist_show() throws IOException {        
        Process Get_Wifi_information = Runtime.getRuntime().exec("nmcli -t -f  ssid,signal,security d wifi list"); 
        try {
            Get_Wifi_information.waitFor();
        } catch (InterruptedException ex) {
            Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        String get_wifi_name     = "";
        String get_wifi_security = "";
        String get_wifi_signal   = "";
        String get_wifi_status   = "";
        
        try (BufferedReader output_Wifi_info = new BufferedReader (new InputStreamReader(Get_Wifi_information.getInputStream()))) {
            String line_Wifi    = output_Wifi_info.readLine();     
            String[] Wifi_name_Line_list = null;
            // 若無資料就顯示為無內容
            if(line_Wifi == null) {
                wifi_data.add(new wifi("", "", "", ""));                          
            }                        
            
            while(line_Wifi != null) {     
                Wifi_name_Line_list  = line_Wifi.split(":");
                
                if(!compare_str_buf.toString().contains(Wifi_name_Line_list[0])) {
                    // 將wifi名稱加入比較字串內
                    compare_str_buf.append(Wifi_name_Line_list[0] + ":");                 
                    // 將wifi名稱去除單引號跟空白
                    get_wifi_name = Wifi_name_Line_list[0].replace("\'", "");
                    get_wifi_name = get_wifi_name.trim();                    
                    // 若wifi名稱與正在連線wifi名稱相同，且訊號強度小於40，就不加入wifi清單
                    if(!Connecting_wifi_name_NoShow.equals(get_wifi_name)&&Integer.parseInt(Wifi_name_Line_list[1])>=35) {
                        get_wifi_signal = Wifi_name_Line_list[1];
                
                        if(Wifi_name_Line_list.length == 3) {                            
                            if(Wifi_name_Line_list[2].contains("WPA")) {
                                get_wifi_security = "WPA";
                            } else if(Wifi_name_Line_list[2].contains("WEP")) {
                                get_wifi_security = "WEP";
                            }                             
                            if(Wifi_name_Line_list[2].contains("WPA") || Wifi_name_Line_list[2].contains("WEP")) {          
                                get_wifi_status = WordMap.get("Safe");
                            }                                                                                                    
                        } else  if(Wifi_name_Line_list.length == 2) {   
                            get_wifi_security = "Open";
                            get_wifi_status   = WordMap.get("Open");                                        
                        }
                    
                        wifi_data.add(new wifi(get_wifi_name, get_wifi_security, get_wifi_signal, get_wifi_status));                                                             
                    }                                                                      
                }
                                
                line_Wifi = output_Wifi_info.readLine();
            }
 
            output_Wifi_info.close();        
        }            
        
        if(test_code) {
            wifi_data.add(new wifi("test"       , "WEP", "50", "安全")); 
            wifi_data.add(new wifi("hello world", "WEP", "80", "安全")); 
            wifi_data.add(new wifi("av8d"       , "WPA", "90", "安全")); 
            wifi_data.add(new wifi("999"        , "WEP", "80", "安全")); 
            wifi_data.add(new wifi("111"        , "WEP", "80", "安全")); 
            wifi_data.add(new wifi("0"          , "WEP", "80", "安全")); 
            wifi_data.add(new wifi("2"          , "WEP", "80", "安全")); 
            wifi_data.add(new wifi("3"          , "WEP", "80", "安全")); 
            wifi_data.add(new wifi("4"          , "WEP", "80", "安全")); 
            wifi_data.add(new wifi("5"          , "WEP", "80", "安全")); 
            wifi_data.add(new wifi("6"          , "WEP", "80", "安全")); 
            wifi_data.add(new wifi("7"          , "WEP", "80", "安全")); 
            wifi_data.add(new wifi("8"          , "WEP", "80", "安全")); 
            wifi_data.add(new wifi("9"          , "WEP", "80", "安全")); 
            wifi_data.add(new wifi("10"          , "WEP", "80", "安全")); 
            wifi_data.add(new wifi("11"          , "WEP", "80", "安全")); 
            wifi_data.add(new wifi("12"          , "WEP", "80", "安全")); 
            wifi_data.add(new wifi("13"          , "WEP", "80", "安全")); 
            wifi_data.add(new wifi("14"          , "WEP", "80", "安全")); 
            wifi_data.add(new wifi("15"          , "WEP", "80", "安全")); 
            wifi_data.add(new wifi("16"          , "WEP", "80", "安全")); 
            
            
        }

    }
    
    public void wifidata_refresh() throws IOException {
        compare_str_buf.setLength(0); // 將比較字串清除內容
        Process Get_Wifi_information = Runtime.getRuntime().exec("nmcli -t -f  ssid,signal,security d wifi list");         
        try {
            Get_Wifi_information.waitFor();
        } catch (InterruptedException ex) {
            Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String get_wifi_name     = "";   
        String get_wifi_security = "";
        String get_wifi_signal   = "";
        String get_wifi_status   = "";        
        if(stopFlag)
            return;
//        DelayTime(5000);
        System.out.println("count down sleep");        
        final CountDownLatch count_down = new CountDownLatch(1);
        
        
       
       
         Platform.runLater(new Runnable() {             
             public void run() {                
                try{
                    wifi_data.clear(); // 清空data先前的內容                     
                    System.out.println("clear");
    //              count_down.countDown();
                }
                finally
                {
                    count_down.countDown();
                }                        
             }             
          });
  
        try {
            if(stopFlag)
                count_down.countDown();
            System.out.println("count down 1");
            count_down.await();
            System.out.println("count down 2");
        } catch (InterruptedException ex) {
//            Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(stopFlag)
            return;
        System.out.println("start 0");
        try (BufferedReader output_Wifi_info = new BufferedReader (new InputStreamReader(Get_Wifi_information.getInputStream()))) {
            String line_Wifi    = output_Wifi_info.readLine();     
            String[] Wifi_name_Line_list = null;
            // 若無資料就顯示為無內容
            if(line_Wifi == null) {
                wifi_data.add(new wifi("", "", "", ""));                          
            }              
            
            while(line_Wifi != null) {                           
                Wifi_name_Line_list  = line_Wifi.split(":");
                
                if(!compare_str_buf.toString().contains(Wifi_name_Line_list[0])) {
                    // 將wifi名稱加入比較字串內
                    compare_str_buf.append(Wifi_name_Line_list[0] + ":"); 
                    // 將wifi名稱去除單引號跟空白
                    get_wifi_name = Wifi_name_Line_list[0].replace("\'", "");
                    get_wifi_name = get_wifi_name.trim();
                    // 若wifi名稱與正在連線wifi名稱相同，且訊號強度小於40，就不加入wifi清單       
                    if(!Connecting_wifi_name_NoShow.equals(get_wifi_name)&&Integer.parseInt(Wifi_name_Line_list[1])>=35) {
                        get_wifi_signal = Wifi_name_Line_list[1];
                
                        if(Wifi_name_Line_list.length == 3) {                    
                            if(Wifi_name_Line_list[2].contains("WPA")) {
                                get_wifi_security = "WPA";
                            } else if(Wifi_name_Line_list[2].contains("WEP")) {
                                get_wifi_security = "WEP";
                            } 
                             
                            if(Wifi_name_Line_list[2].contains("WPA") || Wifi_name_Line_list[2].contains("WEP")) {            
                                get_wifi_status = WordMap.get("Safe");                          
                            } 
                                                                                                     
                        } else  if(Wifi_name_Line_list.length == 2) {   
                            get_wifi_security = "Open";
                            get_wifi_status   = WordMap.get("Open");
                        }
                        wifi temp = new wifi(get_wifi_name, get_wifi_security, get_wifi_signal, get_wifi_status);
                        final CountDownLatch count_down_1 = new CountDownLatch(1);
                        
                        Platform.runLater(new Runnable() {             
                            public void run() {
                                try{
                                    System.out.println("start 1");
                                    wifi_data.add(temp);
                                }
                                finally
                                {
                                    count_down_1.countDown();
                                } 
                        }             
                        });
                       
                        try {
                            if(stopFlag)
                                count_down_1.countDown();
                            System.out.println("count down 1");
                            count_down_1.await();
                            System.out.println("count down 2");
                        } catch (InterruptedException ex) {
//                            Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.out.println("start 2");
                    }
                }
                
                line_Wifi = output_Wifi_info.readLine();
            }        

            output_Wifi_info.close();        
        } 
        Platform.runLater(new Runnable() {
             public void run() {
                wifi_table.getSortOrder().setAll(Collections.singletonList(wifi_NameCol));
                wifi_NameCol.setSortType(TableColumn.SortType.ASCENDING);     
             }
        });
        
        
    }    
    
    public void btn_refresh() throws IOException {
        compare_str_buf.setLength(0); // 將比較字串清除內容
        Process Get_Wifi_information = Runtime.getRuntime().exec("nmcli -t -f  ssid,signal,security d wifi list");         
        try {
            Get_Wifi_information.waitFor();
        } catch (InterruptedException ex) {
            Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String get_wifi_name     = "";    
        String get_wifi_security = "";
        String get_wifi_signal   = "";
        String get_wifi_status   = "";
        wifi_data.clear(); // 清空data先前的內容        

                

        try (BufferedReader output_Wifi_info = new BufferedReader (new InputStreamReader(Get_Wifi_information.getInputStream()))) {
            String line_Wifi             = output_Wifi_info.readLine();
            String[] Wifi_name_Line_list = null;     
            // 若無資料就顯示為無內容
            if(line_Wifi == null) {
                wifi_data.add(new wifi("", "", "", ""));                          
            }              
            
            while(line_Wifi != null) {                           
                Wifi_name_Line_list  = line_Wifi.split(":");
                
                if(!compare_str_buf.toString().contains(Wifi_name_Line_list[0])) {
                    // 將wifi名稱加入比較字串內
                    compare_str_buf.append(Wifi_name_Line_list[0] + ":"); 
                    // 將wifi名稱去除單引號跟空白
                    get_wifi_name = Wifi_name_Line_list[0].replace("\'", "");
                    get_wifi_name = get_wifi_name.trim();
                    // 若wifi名稱與正在連線wifi名稱相同，且訊號強度小於40，就不加入wifi清單       
                    if(!Connecting_wifi_name_NoShow.equals(get_wifi_name)&&Integer.parseInt(Wifi_name_Line_list[1])>=35) {
                        get_wifi_signal = Wifi_name_Line_list[1];
                
                        if(Wifi_name_Line_list.length == 3) {                    
                            if(Wifi_name_Line_list[2].contains("WPA")) {
                                get_wifi_security = "WPA";
                            } else if(Wifi_name_Line_list[2].contains("WEP")) {
                                get_wifi_security = "WEP";
                            } 
                             
                            if(Wifi_name_Line_list[2].contains("WPA") || Wifi_name_Line_list[2].contains("WEP")) {            
                                get_wifi_status = WordMap.get("Safe");                          
                            } 
                                                                                                     
                        } else  if(Wifi_name_Line_list.length == 2) {   
                            get_wifi_security = "Open";
                            get_wifi_status   = WordMap.get("Open");
                        }
                              
                        wifi_data.add(new wifi(get_wifi_name, get_wifi_security, get_wifi_signal, get_wifi_status));
                    }
                }
                
                line_Wifi = output_Wifi_info.readLine();
            }        

            output_Wifi_info.close();        
        } 
        Platform.runLater(new Runnable() {
             public void run() {
                wifi_table.getSortOrder().setAll(Collections.singletonList(wifi_NameCol));
                wifi_NameCol.setSortType(TableColumn.SortType.ASCENDING);     
             }
        });
        
        
    }    
    
    // 畫面下層 (描述及按鈕)
    public final void bottom_layout() {
        // 建立Label物件（描述）
        desc1 = new Label();
        desc1.setId("List_label");
        desc2 = new Label();
        desc2.setId("List_label");
        
        Desc = new VBox();
	Desc.setSpacing(10);
        
                Desc.setTranslateY(13); 
                Desc.setTranslateX(39); 

        Desc.setAlignment(Pos.CENTER_LEFT);
        Desc.setPadding(new Insets(0, 0, 0, 0));        
        Desc.getChildren().addAll(desc1, desc2);      
        
        MainGP.add(Desc,0,4);                                
        
        // 建立按鈕物件(refresh)
        Btn_refresh = new Button(WordMap.get("Wifi_List"));  
        Btn_refresh.setPadding(new Insets(1, 0, 2, 0));  
        if("English".equals(WordMap.get("SelectedLanguage"))){                 
            Btn_refresh.setPrefSize(110,24);
            Btn_refresh.setMaxSize(110,24);
            Btn_refresh.setMinSize(110,24);
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){           
            Btn_refresh.setPrefSize(80,24);
            Btn_refresh.setMaxSize(80,24);
            Btn_refresh.setMinSize(80,24);
        }                 // 強制限制button最小寬度/高度,讓每個button在變換語言時不會變動
//        Btn_exit.setTranslateY(-27);
//        Btn_exit.setTranslateX(15);
        Btn_refresh.setId("LoginMultiVD_Exit");
        Btn_refresh.setOnAction((ActionEvent event) -> {
            /*
            lt = new lock_thr();
            thread_refresh = new Thread(lt);
            thread_refresh.start();
            */
            try {
                btn_refresh();
            } catch (IOException ex) {
                Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
        });      
        
        
        Btn_confirm = new Button(WordMap.get("Connect"));
        
        if("English".equals(WordMap.get("SelectedLanguage"))){                 
            Btn_confirm.setPrefSize(100, btn_height);
            Btn_confirm.setMaxSize(100, btn_height); 
            Btn_confirm.setMinSize(100, btn_height); 
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){           
            Btn_confirm.setPrefSize(btn_width, btn_height);
            Btn_confirm.setMaxSize(btn_width, btn_height); 
            Btn_confirm.setMinSize(btn_width, btn_height);   
        }          
        
      
        Btn_confirm.setOnAction((ActionEvent event) -> {    
            /*
            // config檔案檢查
            for(int i = 0; i < output_Get_wifi_config_Line_list.size(); i++) {
                if(Select_wifiName.contains(output_Get_wifi_config_Line_list.get(i))){
                    no_type_pwd = true;                    
                }
            }
                    // 沒有輸入過密碼,要輸入密碼
                    if(open_type && !no_type_pwd && !Select_security_type.equals("Open")) {
                        GB.lock_wifipwd     = true;
                        WindowController wc = new WindowController();
                        wc.showStage(public_stage, Select_wifiName, Select_security_type);
                        
                    } else if(no_type_pwd) { // 輸入過密碼,不需要在輸入第2次
                        no_type_pwd = false;
                        try {
                            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli d disconnect iface wlan0" });
                            GB.wifi_Connectting_status = true;
                            wifi_show_desc1.setText(Select_wifiName);
                            wifi_data.remove(wifi_table.getSelectionModel().getSelectedIndex());
                            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli c up id " + Select_wifiName + " &" });
                        } catch (IOException ex) {
                            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                        }                    
                    } else if(Select_security_type.equals("Open")) {                        
                        // wifi_show_desc1.setText(Select_wifiName);
                        GB.wifi_Connectting_status = true;
                        wifi_data.remove(wifi_table.getSelectionModel().getSelectedIndex());                        
                        try {
                            WindowController wc = new WindowController();
                            // 斷線
                            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli d disconnect iface wlan0" });
                            // 寫檔
                            wc.writeWifiDHCPSetting(Select_wifiName, Select_security_type, "");                 
                            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","chmod 600 '/etc/NetworkManager/system-connections/" + Select_wifiName + "'"});                
                            try {
                                sleep(1000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            // 連線
                            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli c up id '" + Select_wifiName + "' &" });     
                        } catch (IOException ex) {
                            Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
                        }                                               
                    }   
            */
        });         
        
        
        // 建立按鈕物件(離開)
        Btn_exit = new Button(WordMap.get("TC_Exit"));  
        Btn_exit.setPrefSize(btn_width, btn_height);                  // 強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        Btn_exit.setMaxSize(btn_width, btn_height);                   // 強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        Btn_exit.setMinSize(btn_width, btn_height);                   // 強制限制button最小寬度/高度,讓每個button在變換語言時不會變動
//        Btn_exit.setTranslateY(-27);
//        Btn_exit.setTranslateX(15);
        Btn_exit.setId("LoginMultiVD_Exit");
        Btn_exit.setOnAction((ActionEvent event) -> {
            GB.lock_wifilist = false;
            WifiList_thirdStage.close();
            timer_checkconnect.cancel();
        });           
        

        btn = new HBox();
	btn.setSpacing(20);
        btn.setAlignment(Pos.CENTER_RIGHT);
        btn.setPadding(new Insets(0, 0, 0, 20));   
        // btn.setTranslateY(8);
        
        
        switch (WordMap.get("SelectedLanguage")) {
            case "English":
                btn.setTranslateY(-65); // -60
                btn.setTranslateX(-125); // -65
                break;
            default:
                btn.setTranslateY(-52);  // -49
                btn.setTranslateX(-125); // -75
                break;
        }           
        

        btn.getChildren().addAll(Btn_refresh);   // , Btn_confirm
		
        MainGP.add(btn,0,5);    
    }           
    
  
    
    /****變更Label字型****字型:中文-標楷體,英文:Times New Roman****/ // 2018.04.10  william  功能修改   // 2018.04.12 william  多VD登入新增開機狀態   
    public void ChangeLabelLang(Label Label01, Label Label02, TableColumn tc00, TableColumn tc01, TableColumn tc02, Label Label03, Label Label04){ // Label title, Label nametitle
        //Label "title", "TableColumn"
        if("English".equals(WordMap.get("SelectedLanguage"))){
//            title.setStyle("-fx-font-family:'Ubuntu'; -fx-text-fill: white; "); //-fx-font-size: 17px;
            Label01.setStyle("-fx-font-family:'Ubuntu'; -fx-text-fill: white;-fx-font-size: 18px;"); //-fx-font-size: 17px;
//            Label02.setStyle("-fx-font-family:'Ubuntu';-fx-font-size: 18px;"); //-fx-font-size: 17px;
            tc00.setStyle("-fx-font-family:'Ubuntu';");
            tc01.setStyle("-fx-font-family:'Ubuntu';-fx-padding: 7 0 0 7;"); // -fx-alignment: center-left;
            tc02.setStyle("-fx-font-family:'Ubuntu';");
//            nametitle.setStyle("-fx-font-family:'Ubuntu'; -fx-text-fill: white;-fx-font-size: 18px; ");
            Label03.setStyle("-fx-font-family:'Ubuntu'; -fx-text-fill: white;-fx-font-size: 18px;"); //-fx-font-size: 17px;
//            Label04.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 18px;"); //-fx-font-size: 17px;            
            
//            tc03.setStyle("-fx-font-family:'Ubuntu';-fx-alignment: center-left;");
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/

//            title.setStyle("-fx-font-family: 'Droid Sans Fallback'; -fx-text-fill: white;");
            Label01.setStyle("-fx-font-family: 'Droid Sans Fallback'; -fx-text-fill: white;-fx-font-size: 18px;");
//            Label02.setStyle("-fx-font-family: 'Droid Sans Fallback';-fx-font-size: 18px;");
            tc00.setStyle("-fx-font-family: 'Droid Sans Fallback'; ");//;-fx-font-weight: 700; 
            tc01.setStyle("-fx-font-family: 'Droid Sans Fallback';-fx-padding: 7 0 0 7;"); // -fx-alignment: center-left;
            tc02.setStyle("-fx-font-family: 'Droid Sans Fallback';");
//            nametitle.setStyle("-fx-font-family: 'Droid Sans Fallback'; -fx-text-fill: white;-fx-font-size: 18px;");
            Label03.setStyle("-fx-font-family: 'Droid Sans Fallback'; -fx-text-fill: white;-fx-font-size: 18px;");
//            Label04.setStyle("-fx-font-family: 'Droid Sans Fallback'; -fx-font-size: 18px;");            
//            tc03.setStyle("-fx-font-family: 'Droid Sans Fallback';-fx-alignment: center-left;  ");                        
        }
       
     }
    /****變更button字型****字型:中文-標楷體,英文:Times New Roman****/ // 2018.04.10  william  功能修改   
    public void ChangeButtonLang(Button but01, Button but02, Button but03){
        //Button "Leave"
        if("English".equals(WordMap.get("SelectedLanguage"))){
            but01.setStyle("-fx-font-family:'Ubuntu';");         
            but02.setStyle("-fx-font-family:'Ubuntu';");   
            but03.setStyle("-fx-font-family:'Ubuntu';");         

        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/

            but01.setStyle("-fx-font-family: 'Droid Sans Fallback';");          
            but02.setStyle("-fx-font-family: 'Droid Sans Fallback';");    
            but03.setStyle("-fx-font-family: 'Droid Sans Fallback';");          
 
        }  
        
     }                
    
    // 定義VD資料模型
    public class wifi {         
        private final SimpleStringProperty wifi_Name;
        private final SimpleStringProperty wifi_Security;
        private final SimpleStringProperty wifi_Signal;
        private final SimpleStringProperty wifi_Status;
        
//        private ImageView image;        


        wifi(String wifi_name, String wifi_security, String wifi_signal, String wifi_status) {
            this.wifi_Name     = new SimpleStringProperty(wifi_name);
            this.wifi_Security = new SimpleStringProperty(wifi_security);
            this.wifi_Signal   = new SimpleStringProperty(wifi_signal);
            this.wifi_Status   = new SimpleStringProperty(wifi_status);
        } 
        // wifi name
        public String getwifi_Name() {
            return wifi_Name.get();
        }

        public void setwifi_Name(String wifi_name) {
            wifi_Name.set(wifi_name);
        }

        public StringProperty wifi_NameProperty() {
            return wifi_Name;
        }
        // wifi security
        public String getwifi_Security() {
            return wifi_Security.get();
        }

        public void setwifi_Security(String wifi_security) {
            wifi_Security.set(wifi_security);
        }

        public StringProperty wifi_SecurityProperty() {
            return wifi_Security;
        }        
        
        // wifi signal
        
        public String getwifi_Signal() {
            return wifi_Signal.get();
        }

        public void setwifi_Signal(String wifi_signal) {
            wifi_Signal.set(wifi_signal);
        }

        public StringProperty wifi_SignalProperty() {
            return wifi_Signal;
        } 
        
        // wifi status
        
        public String getwifi_Status() {
            return wifi_Status.get();
        }

        public void setwifi_Status(String wifi_status) {
            wifi_Status.set(wifi_status);
        }

        public StringProperty wifi_StatusProperty() {
            return wifi_Status;
        }         

//    CustomImage(ImageView img) {
//        this.image = img;
//    }
//
//    public void setImage(ImageView value) {
//        image = value;
//    }
//
//    public ImageView getImage() {
//        return image;
//    }        

        
    }     
    
    /*------------------------  動作偵測 ------------------------*/ 
    
//    public void clickdectect() {
//        thread_click = new Thread() {
//                // runnable for that thread
//                public void run() {
//                    while(true){
////                        System.out.println("Single & Double Click  check");
//                        if(stopFlag)
//                            break;
//                        try {
//                            System.out.println(" click start");
//                            if(click_count == 1) {
//                                sleep(500);
//                                System.out.println("--------------Click 01--------------");
//                                click_lock.lock();                                
//                                if(click_count >= 2) {
//                                    
//                                    System.out.println("Double Click  count:  " + click_count);
//                                    click_count = 0;
//                                    
//                                } else {
//                                    System.out.println("Single Click  count:  " + click_count);
//                                    click_count = 0;
//                                    
//                                }
//                                click_lock.unlock();
//                                System.out.println("--------------Click 02--------------");
//                                System.out.println();
//                                
//                            } 
//                            sleep(100);
//                        } catch (InterruptedException ex) {
//                            Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                }
//            };
//        
//        thread_click.start();    
//    
//    }    
//    
//    public void detectSelection() {
//        
//        wifi_table.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                click_count++;
////                System.out.println("------------Click add------------");
////                if(mouseEvent.getClickCount() == 1) {
////                    scrollToIndex = wifi_table.getSelectionModel().getSelectedIndex();
////                    System.out.println("Click Index: " + scrollToIndex);
////                    
////                    wifi_table.scrollTo(scrollToIndex-3);
////                }
////   
////            row_wifi_name          = (String) wifi_table.getSelectionModel().getSelectedItem().getwifi_Name();                    
////            row_wifi_security_type = (String) wifi_table.getSelectionModel().getSelectedItem().getwifi_Security();
////            File file=new File("/etc/NetworkManager/system-connections/temp/" + wifi_name);
////            if(file.exists())
////                row_no_type_pwd = true; 
////
////            if(row_no_type_pwd) {
////                vision_flag = false;   
////                connect_type = 1;
//////                System.out.println("1");
////            } else if(wifi_security_type.equals("Open") && !no_type_pwd) {
////                vision_flag = false;   
////                connect_type = 2;        
//////                System.out.println("2");
////            } else {        
////                vision_flag = true; 
//////                System.out.println("3");
////            }  
//            
//
///*
//                if(mouseEvent.getClickCount() == 2) {     
//
//                    Select_wifiName       = (String) wifi_table.getSelectionModel().getSelectedItem().getwifi_Name();                    
//                    Select_security_type  = (String) wifi_table.getSelectionModel().getSelectedItem().getwifi_Security();
//                    data                  = Select_wifiName;            
//                    GB.wifi_name          = Select_wifiName;    
//                    CheckResultName       = Select_wifiName;                 
//                    GB.wifi_security_type = Select_security_type;                                        
//
//                     
//                    System.out.print(Select_wifiName + " , "  + Select_security_type + "\n");
//                    
//                    try {
//                        Get_Wifi_Config();
//                    } catch (IOException ex) {
//                        Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
//                    }                    
//                    
//                    // config檔案檢查
//                    for(int i = 0; i < output_Get_wifi_config_Line_list.size(); i++) {
//                        if(Select_wifiName.contains(output_Get_wifi_config_Line_list.get(i))){
//                            no_type_pwd = true;
//                        }
//                    }
//                    
//                    if(no_type_pwd) {                    
//                        connect_type = 1;
//                    } else if(Select_security_type.equals("Open") && !no_type_pwd) {   
//                        connect_type = 2;        
//                    } else {        
//                        connect_type = 3;
//                    }                      
//                    
//
//                    
//                    switch (connect_type) {
//                        case 1:
//                            GB.wifi_Connectting_status = true;
//                            GB.setGraphic_flag         = false;  
//                            writename_flag = true;
//                            fresh_flag = true;
//                            try {
//                                Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli d disconnect iface wlan0" });
//                                Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","killall nmcli;nmcli c up id '" + Select_wifiName + "' &" });          
//                    
//                            } catch (IOException ex) {
//                                Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
//                            }                             
//                            break;
//                        case 2:
//                            GB.wifi_Connectting_status = true;
//                            GB.setGraphic_flag         = false;                              
//                            writename_flag = true;
//                            fresh_flag = true;
//                            try {
//                                Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli d disconnect iface wlan0" });
//                                // 寫檔
//                                writeWifiDHCPSetting(Select_wifiName, Select_security_type, "");                 
//                                Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","chmod 600 '/etc/NetworkManager/system-connections/" + Select_wifiName + "'"});                
//                                try {
//                                    sleep(1000);
//                                } catch (InterruptedException ex) {
//                                    Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
//                                }                                
//                                Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","killall nmcli;nmcli c up id '" + Select_wifiName + "' &" });          
//                                                        
//                            } catch (IOException ex) {
//                                Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
//                            }                               
//                            break;    
//                        case 3:
//                            if(wifi_Pwd_TextField.getText().isEmpty()&&vision_flag) {
//                                wifi_pwd_desc.setText(WordMap.get("Message_Error_WiFi_01"));
//                                if("English".equals(WordMap.get("SelectedLanguage"))){
//                                    wifi_pwd_desc.setStyle("-fx-font-family:'Ubuntu'; -fx-text-fill: red;-fx-font-size: 18px;"); 
//                                }        
//                                if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
//                                    wifi_pwd_desc.setStyle("-fx-font-family: 'Droid Sans Fallback'; -fx-text-fill: red;-fx-font-size: 18px;");
//                                }                                  
//                            } else {
//                                GB.wifi_Connectting_status = true;
//                                GB.setGraphic_flag         = false;  
//                                writename_flag = false;
//                                checkresult    = true;
//                                checkcount     = 0;                      
//                                try {
//                                    Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli d disconnect iface wlan0" });
//                                    writeWifiDHCPSetting(Select_wifiName, Select_security_type, wifi_Pwd_TextField.getText());
//                
//                                    Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","chmod 600 '/etc/NetworkManager/system-connections/" + Select_wifiName + "'"});
//                                    try {
//                                        sleep(1000);
//                                    } catch (InterruptedException ex) {
//                                        Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
//                                    }
//                                    Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","killall nmcli;nmcli c up id '" + Select_wifiName + "' &" });          
//                                    
//                                } catch (IOException ex) {
//                                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
//                                }                             
//
//                            }                             
//                            break;                                                                                                                                            
//                        default:                            
//                            break;
//                    }                        
//                    
//                    
//                    
////                    // 沒有輸入過密碼,要輸入密碼
////                    if(open_type && !no_type_pwd && !Select_security_type.equals("Open")) {
////                       
////                    } else if(no_type_pwd) { // 輸入過密碼,不需要在輸入第2次
////                   
////                    } else if(Select_security_type.equals("Open")) {                        
////                                               
////                    }                          
//                }
//                
//                */
//            }
//        });                       
//    }    
//        
    public String getData() {
        return data;
    }    
    // 取正在連線的Wi-Fi名稱
    public void Get_Wifi_Status() throws IOException { 
            String line_connection_status;
                            
            Process Detect_connection_status_Process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli d status | grep wlan[0-9] | awk '{print $3}'"});
                            
            try (BufferedReader output_connection_status = new BufferedReader (new InputStreamReader(Detect_connection_status_Process.getInputStream()))) {
                line_connection_status = output_connection_status.readLine();
                // System.out.println("Start get line: " + line_connection_status);
                if (line_connection_status.contains("disconnected")) {     
                        setGraphic_Number = 0;
                        wifi_Connect_Disconnect = false;
                        wifi_Btn.setDisable(false);
                        Btn_confirm.setDisable(false);
                        Set_JsonName();
                        Set_Wifi_Disconnected_Status();
                        if(GB.wifi_Connectting_status && !networkFlag) {
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    ARVI.Network_setGraphic(setGraphic_Number);
                                }
                            });
                            GB.setGraphic_flag = true;
                            GB.wifi_Connectting_status = false;                             
                        }     
                        
                        if(checkresult) {
                            try {
                                Check_FirstTime_Connection();
                            } catch (Exception ex) {
                                Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            // reconnect wifi
                            if(!wifi_show_desc1.getText().equals("") && !wifi_show_desc1.getText().equals("off/any"))
                                Btn_Connect();
                        }
                        
                        output_connection_status.close();
                        return;
                }
                else if (line_connection_status.contains("connected") && !line_connection_status.contains("disconnected")){
                        setGraphic_Number = 1;      
                        wifi_Connect_Disconnect     = true;
                        wifi_Btn.setDisable(false);
                        Btn_confirm.setDisable(false);
                        Get_Wifi_Name();   
                        checkresult    = false;
                }
                 else if (line_connection_status.contains("connecting")){
                        setGraphic_Number = 2;
                        if(GB.wifi_Connectting_status && !networkFlag) {
                            GB.setGraphic_flag = false;
                               
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    ARVI.Network_setGraphic(setGraphic_Number);
                                }
                            });                                

                            Set_Wifi_Connecting_Status();                            
                            
                        }
                 }                     
                 else{
                    System.out.println("======break======");
                 }
                                  
                output_connection_status.close();
//                System.out.println("line_connecting:=====" + line_connection_status + "=======, setGraphic_Number: " + setGraphic_Number);
            }               
            
            /*
            Process Detect_wifi_Process = Runtime.getRuntime().exec("iwconfig wlan0"); 
            try (BufferedReader output_Detect_wifi = new BufferedReader (new InputStreamReader(Detect_wifi_Process.getInputStream()))) {
                String line_Detect_wifi = output_Detect_wifi.readLine();
                String[] Detect_wifi_Line_list = null;

                while(line_Detect_wifi != null) {  
                    if(line_Detect_wifi.contains("ESSID")) {
                        Detect_wifi_Line_list = line_Detect_wifi.split(":");
                        get_Connecting_wifi_name = Detect_wifi_Line_list[1];

                        if(get_Connecting_wifi_name.contains("off/any")) {

                        } else {                         
                            if(!line_connection_status.equals("connected")) {  
                                checkcount++;
                                
//                                if(checkcount==5) {
//                                    checkcount = 0;
//                                    Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli d disconnect iface wlan0" });
                                   
                                    Platform.runLater(new Runnable() {
                                        public void run() {
                                            wifi_pwd_desc.setText(WordMap.get("Message_Error_WiFi_02"));
                                        }
                                    });
                                    if("English".equals(WordMap.get("SelectedLanguage"))){
                                        Platform.runLater(new Runnable() {
                                            public void run() {
                                                wifi_pwd_desc.setStyle("-fx-font-family:'Ubuntu'; -fx-text-fill: red;-fx-font-size: 18px;");
                                            }
                                        });
                                        
                                         
                                    }        
                                    if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
                                         Platform.runLater(new Runnable() {
                                            public void run() {
                                               wifi_pwd_desc.setStyle("-fx-font-family: 'Droid Sans Fallback'; -fx-text-fill: red;-fx-font-size: 18px;");
                                            }
                                        });
                                        
                                    }                                     

//                                }
                                
                                
                                Platform.runLater(new Runnable() {
                                    public void run() {
                                        ARVI.Network_setGraphic(setGraphic_Number);
                                    }
                                });                                
                                
                                Set_Wifi_Connecting_Status();
                                System.out.println("break");
                                break;
                            }
                            wifi_Connect_Disconnect = true;
                            get_Connecting_wifi_name    = get_Connecting_wifi_name.replace('"', ' ');
                            get_Connecting_wifi_name    = get_Connecting_wifi_name.trim();                      
                            GB.wifi_name                = get_Connecting_wifi_name;
                            GB.fail_reconnect_wifi_name = get_Connecting_wifi_name;
                            Connecting_wifi_name_NoShow = get_Connecting_wifi_name;
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    wifi_show_desc1.setText(get_Connecting_wifi_name);
                                }
                            });

                         if(fresh_flag) {
                                fresh_flag = false;
                                try {
                                    wifidata_refresh();
                                } catch (IOException ex) {
                                    Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
                                }                             

                            }                        


                            Set_Wifi_Connected_Status();
                            if(GB.wifi_Connectting_status) {
                                Platform.runLater(new Runnable() {
                                    public void run() {
                                        ARVI.Network_setGraphic(setGraphic_Number);
                                    }
                                });
                                GB.wifi_Connectting_status = false;                     
                            }   
                            break;
                        }
                    }                       
               
                    line_Detect_wifi = output_Detect_wifi.readLine();
                }

                output_Detect_wifi.close();       
            
        }
            */
    }     
            
    public void Get_Wifi_Name() throws IOException { 
        String  get_Connecting_wifi_name;
        
        Process Detect_wifi_Process = Runtime.getRuntime().exec("iwconfig " + wlanName); 
        try (BufferedReader output_Detect_wifi = new BufferedReader (new InputStreamReader(Detect_wifi_Process.getInputStream()))) {
            String line_Detect_wifi = output_Detect_wifi.readLine();
            String[] Detect_wifi_Line_list = null;

            while(line_Detect_wifi != null) {  
                if(line_Detect_wifi.contains("ESSID")) {
                    Detect_wifi_Line_list = line_Detect_wifi.split(":");
                    get_Connecting_wifi_name    = Detect_wifi_Line_list[1];
                    get_Connecting_wifi_name    = get_Connecting_wifi_name.replace('"', ' ');
                    get_Connecting_wifi_name    = get_Connecting_wifi_name.trim();                      
                    GB.wifi_name                = get_Connecting_wifi_name;
                    GB.fail_reconnect_wifi_name = get_Connecting_wifi_name;
                    Connecting_wifi_name_NoShow = get_Connecting_wifi_name;
                    Platform.runLater(new Runnable() {
                        public void run() {
                            wifi_show_desc1.setText(Connecting_wifi_name_NoShow);
                        }
                    });

                    Set_Wifi_Connected_Status();
                    Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","cp '/etc/NetworkManager/system-connections/WiFi_" + get_Connecting_wifi_name + "' /etc/NetworkManager/system-connections/temp/"});
                    
                    if(GB.wifi_Connectting_status && !networkFlag) {
                        checkClose = false;
                        Platform.runLater(new Runnable() {
                            public void run() {
                                ARVI.Network_setGraphic(setGraphic_Number);
                            }
                        });
                        GB.wifi_Connectting_status = false;      
                        GB.setGraphic_flag = true;
                        checkresult = false;
                        
                        
                        try {
                            System.out.println("Refresh");                                                 
                            wifidata_refresh();                                 
                            checkClose = true;
                            if(stopFlag){
                                Platform.runLater(new Runnable() {
                                    public void run() {
                                        System.out.println("Close");
                                        WifiList_thirdStage.close();
                                    }
                                });
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
                        }                          
                        
                    }   
                    break;
                        
                    }                       
               
                    line_Detect_wifi = output_Detect_wifi.readLine();
            }
            output_Detect_wifi.close();                   
        }
    }      
    
    public void Set_Wifi_Disconnected_Status() {       
        Platform.runLater(new Runnable() {
            public void run() {
                wifi_show_desc2.setText(WordMap.get("Disconnected"));
            }
        });
                        
        switch (WordMap.get("SelectedLanguage")) {
            case "English":
                Platform.runLater(new Runnable() {
                    public void run() {
                        wifi_show_desc2.setStyle("-fx-font-family:'Ubuntu';-fx-font-size: 18px;-fx-text-fill: #cfcfcf;");
                    }
                });
                break;
            default: 
                Platform.runLater(new Runnable() {
                    public void run() {                
                        wifi_show_desc2.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 18px;-fx-text-fill: #cfcfcf;");
                    }
                });
                break;
        }                                                                                             
        Platform.runLater(new Runnable() {
            public void run() {     
                wifi_Btn.setText(WordMap.get("Connect"));      
                wifi_Btn.setTranslateY(-1);
                switch (WordMap.get("SelectedLanguage")) {
                    case "English":
                        wifi_Btn.setTranslateX(-18); 
                        break;
                    case "Japanese":
                        wifi_Btn.setTranslateX(3); 
                        break;                        
                    default:
                        wifi_Btn.setTranslateX(-15);  
                        break;
                }                   
            }
        });
    }
        
    public void Set_Wifi_Connected_Status() {
        Platform.runLater(new Runnable() {
            public void run() {
                wifi_show_desc2.setText(WordMap.get("Connected"));
            }
        });

        WriteWiFiStatusChange();                        

                                
        switch (WordMap.get("SelectedLanguage")) {
            case "English":
                Platform.runLater(new Runnable() {
                    public void run() {
                        wifi_show_desc2.setStyle("-fx-font-family:'Ubuntu';-fx-font-size: 18px;-fx-text-fill: white;");
                    }
                });
                break;
            default:
                Platform.runLater(new Runnable() {
                    public void run() {
                        wifi_show_desc2.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 18px;-fx-text-fill: white;");
                    }
                });
                break;
        }                                                  
        
                Platform.runLater(new Runnable() {
                    public void run() {
                        wifi_Btn.setText(WordMap.get("Disconnect"));
                        wifi_Btn.setTranslateY(-1);
                        switch (WordMap.get("SelectedLanguage")) {
                            case "English":
                                wifi_Btn.setTranslateX(5); 
                                break;
                            default:
                                wifi_Btn.setTranslateX(-15);  
                                break;
                        }                            
                    }
                });        
        
        
    }
    
    public void Set_Wifi_Connecting_Status() {
        Platform.runLater(new Runnable() {
            public void run() {
                wifi_show_desc2.setText(WordMap.get("Connecting"));
            }
        });
                                
        switch (WordMap.get("SelectedLanguage")) {
            case "English":
                Platform.runLater(new Runnable() {
                    public void run() {
                        wifi_show_desc2.setStyle("-fx-font-family:'Ubuntu';-fx-font-size: 18px;-fx-text-fill: white;");
                    }
                });
                break;
            default:
                Platform.runLater(new Runnable() {
                    public void run() {
                        wifi_show_desc2.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 18px;-fx-text-fill: white;");
                    }
                });
                break;
        }                                                  
        
                Platform.runLater(new Runnable() {
                    public void run() {
                        wifi_Btn.setText(WordMap.get("Disconnect"));
                wifi_Btn.setTranslateY(-1);
                switch (WordMap.get("SelectedLanguage")) {
                    case "English":
                        wifi_Btn.setTranslateX(0); 
                        break;
                    default:
                        wifi_Btn.setTranslateX(-15);  
                        break;
                }                         
                    }
                });        
        
        
    }    
    
    public void Check_FirstTime_Connection() throws Exception {
//        checkcount++;
//        if(checkcount == 10) {
//            checkcount = 0;
            checkresult = false;
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","rm /etc/NetworkManager/system-connections/WiFi_*" }).waitFor(); //  + CheckResultName
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli d disconnect iface " + wlanName }).waitFor();
            Set_JsonName();
            Platform.runLater(new Runnable() {
                public void run() {
                    wifi_pwd_desc.setText(WordMap.get("Message_Error_WiFi_02"));
                }
            });
            if("English".equals(WordMap.get("SelectedLanguage"))){
                Platform.runLater(new Runnable() {
                    public void run() {
                        wifi_pwd_desc.setStyle("-fx-font-family:'Ubuntu'; -fx-text-fill: red;-fx-font-size: 18px;");
                    }
                });


            }        
            else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
                 Platform.runLater(new Runnable() {
                    public void run() {
                       wifi_pwd_desc.setStyle("-fx-font-family: 'Droid Sans Fallback'; -fx-text-fill: red;-fx-font-size: 18px;");
                    }
                });

            }                                     

//        }     
    }
    
    public void Set_JsonName() {
        //config檔為空時，不顯示wifi名稱
        if(!wifi_name_hidden) {
            try {
                LoadWiFiStatus();
                Platform.runLater(new Runnable() {
                    public void run() {
                        wifi_show_desc1.setText(json_wifiname);       
                    }
                });
                Connecting_wifi_name_NoShow = json_wifiname;
            } catch (ParseException ex) {
                Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
            }                                                
        } else if (wifi_name_hidden || wifi_show_desc1.equals("off/any")) {        
            Platform.runLater(new Runnable() {
                public void run() {
                    wifi_Btn.setDisable(true);
                    wifi_show_desc1.setText("");                                        
                }
            });            
        }
    }
    
    public void Set_Disconnect_Icon() {
        Set_Wifi_Disconnected_Status();
        Platform.runLater(new Runnable() {
            public void run() {
                ARVI.Network_setGraphic(0);
            }
        });   
    }    
    
    // 檢查config檔
    public void Get_Wifi_Config() throws IOException {
//        output_Get_wifi_config_Line_list.clear(); // 清除內容
        Process Get_wifi_config_Process = Runtime.getRuntime().exec("ls /etc/NetworkManager/system-connections/temp/"); 
        try (BufferedReader output_Get_wifi_config = new BufferedReader (new InputStreamReader(Get_wifi_config_Process.getInputStream()))) {
            String line_Get_wifi_config = output_Get_wifi_config.readLine();
            
            if(line_Get_wifi_config != null) {
                wifi_name_hidden = false;
            } else {
                wifi_name_hidden = true;
            }
             
//            while(line_Get_wifi_config != null) {  
//                output_Get_wifi_config_Line_list.add(line_Get_wifi_config);                                                     
//                line_Get_wifi_config = output_Get_wifi_config.readLine();
//            }

            output_Get_wifi_config.close();       
        }
    } 
    // 檢查有線連線狀態
    public void Get_Network_Status() throws IOException {
        String command = "ip link | grep eth | grep DOWN\\ mode";  
        String line_Connection = null;
        
//        try {
            Process Connection_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",command});
            BufferedReader output_Connection = new BufferedReader (new InputStreamReader(Connection_process.getInputStream()));
            line_Connection = output_Connection.readLine();              
//        } catch (IOException ex) {
//            Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
//        }
 
        if(line_Connection != null) {
            networkFlag = false;
             Platform.runLater(new Runnable() {
                public void run() {
                    network_show_desc2.setText(WordMap.get("Disconnected"));
                }
             });
            switch (WordMap.get("SelectedLanguage")) {
                case "English":
                    Platform.runLater(new Runnable() {
                        public void run() {
                            network_show_desc2.setStyle("-fx-font-family:'Ubuntu';-fx-font-size: 18px;-fx-text-fill: #cfcfcf;");
                        }
                    });
                    break;
                default:
                    Platform.runLater(new Runnable() {
                        public void run() {
                            network_show_desc2.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 18px;-fx-text-fill: #cfcfcf;");
                        }
                    });
                break;
            }     
            Platform.runLater(new Runnable() {
                public void run() {     
                    network_Btn.setText(WordMap.get("Connect"));       
                    network_Btn.setTranslateY(-1);
                    switch (WordMap.get("SelectedLanguage")) {
                        case "English":
                            network_Btn.setTranslateX(-20); // -18
                            break;
                        case "Japanese":
                            network_Btn.setTranslateX(3); 
                            break;                              
                        default:
                            network_Btn.setTranslateX(-15);  
                            break;
                    }  
                }
            });            
            network_Btn.setDisable(false);
        } else {
            networkFlag = true;
             Platform.runLater(new Runnable() {
                public void run() {
                    network_show_desc2.setText(WordMap.get("Connected"));
                }
             });            
            switch (WordMap.get("SelectedLanguage")) {
                case "English":
                    Platform.runLater(new Runnable() {
                        public void run() {
                            network_show_desc2.setStyle("-fx-font-family:'Ubuntu';-fx-font-size: 18px;-fx-text-fill: white;");
                        }
                    });
                    break;
                default:
                    Platform.runLater(new Runnable() {
                        public void run() {
                            network_show_desc2.setStyle("-fx-font-family:'Ubuntu'; -fx-font-size: 18px;-fx-text-fill: white;");
                        }
                    });
                    break;
            }    
            Platform.runLater(new Runnable() {
                public void run() {     
                    network_Btn.setText(WordMap.get("Disconnect"));    
                    switch (WordMap.get("SelectedLanguage")) {
                        case "English":
                            network_Btn.setTranslateX(3); // 5
                            break;
                        default:
                            network_Btn.setTranslateX(-15);  
                            break;
                    }                     
                }
            });  
            network_Btn.setDisable(false);            
        } 
    }     
    
    //每秒check  網路狀態
    public void networkconn_change() throws UnknownHostException, IOException{                       
        thread_checkconnect = new Thread() {
                // runnable for that thread
                public void run() {
                    while(true){
                        if(stopFlag)
                            break;
                        try {                         
                            checkClose=true;
                            Get_Network_Status();
                            if(stopFlag){
                                
                                break;
                            }
                            Get_Wifi_Config();
                            if(stopFlag)
                                break;                           
                            Get_Wifi_Status();
                        } catch (IOException ex) {
                            Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        /*
                        if(checkresult) {
                            checkcount++;
                            if(checkcount > 4) {
                                if(!CheckResultName.equals(Connecting_wifi_name_NoShow) || !wifi_Connect_Disconnect) {                                    
                                    writename_flag  = false;
                                    Platform.runLater(new Runnable() {
                                        public void run() {
                                            wifi_pwd_desc.setText(WordMap.get("Message_Error_WiFi_02"));
                                        }
                                    });
                                    if("English".equals(WordMap.get("SelectedLanguage"))){
                                        Platform.runLater(new Runnable() {
                                            public void run() {
                                                wifi_pwd_desc.setStyle("-fx-font-family:'Ubuntu'; -fx-text-fill: red;-fx-font-size: 18px;");
                                            }
                                        });
                                        
                                         
                                    }        
                                    if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
                                         Platform.runLater(new Runnable() {
                                            public void run() {
                                               wifi_pwd_desc.setStyle("-fx-font-family: 'Droid Sans Fallback'; -fx-text-fill: red;-fx-font-size: 18px;");
                                            }
                                        });
                                        
                                    }                                     

                                    try {                                        
                                        Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","rm /etc/NetworkManager/system-connections/" + CheckResultName });
                                    } catch (IOException ex) {
                                        Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
                                    }             
                                    
//                                    System.out.println("Wi-Fi密碼錯誤 \n");
                                } else {         
                                    writename_flag = true;
                                    try {
                                        wifidata_refresh();
                                    } catch (IOException ex) {
                                        Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
                                    }                                                                
                                }
                                checkresult = false;
                                checkcount  = 0;                                                                         
                            }                        
                        }     
                        */
                        try {
                            if(stopFlag)
                                break;
                            sleep(3000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            };
        
        thread_checkconnect.start();
//        timer_checkconnect = new Timer(true);
//        task_checkconnect  = new TimerTask() {
//            @Override
//            public void run() { 
////                System.out.println(" wifi list time \n");
//                Platform.runLater(() -> {                    
//                    try {
//                        Get_Network_Status();                        
//                        Get_Wifi_Config();
//                        Get_ConnecttingWifi_Name();
//                        
//                        if(checkresult) {
//                            checkcount++;
//                            if(checkcount > 4) {
//                                if(!CheckResultName.equals(Connecting_wifi_name_NoShow) || !wifi_Connect_Disconnect) {                                    
//                                    writename_flag  = false;
//                                    wifi_pwd_desc.setText(WordMap.get("Message_Error_WiFi_02"));
//                                    if("English".equals(WordMap.get("SelectedLanguage"))){
//                                        wifi_pwd_desc.setStyle("-fx-font-family:'Ubuntu'; -fx-text-fill: red;-fx-font-size: 18px;"); 
//                                    }        
//                                    if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
//                                        wifi_pwd_desc.setStyle("-fx-font-family: 'Droid Sans Fallback'; -fx-text-fill: red;-fx-font-size: 18px;");
//                                    }                                     
//
//                                    try {                                        
//                                        Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","rm /etc/NetworkManager/system-connections/" + CheckResultName });
//                                    } catch (IOException ex) {
//                                        Logger.getLogger(AcroRed_VDI_Viewer.class.getName()).log(Level.SEVERE, null, ex);
//                                    }             
//                                    
////                                    System.out.println("Wi-Fi密碼錯誤 \n");
//                                } else {         
//                                    writename_flag = true;
//                                    try {
//                                        wifidata_refresh();
//                                    } catch (IOException ex) {
//                                        Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
//                                    }                                                                
//                                }
//                                checkresult = false;
//                                checkcount  = 0;
//                                
//                                         
//                            }                        
//                        }                   
                        
                        
                        
//                        wifitable_index++;
//                        if(wifitable_index == 5) {
//                            wifidata_refresh();
//                            wifitable_index = 0;
//                        }
                        
                        
//                    } catch (IOException ex) {
//                        Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
//                    }                                        
//                });              
//            }
//        };
//            timer_checkconnect.schedule(task_checkconnect, 0, 3000); 
         
    }    
    
    //  讀取檔案
    public void LoadWiFiStatus() throws ParseException {
        try {
            File myFile = new File("jsonfile/keep_wifi_name.json");

            if(myFile.exists()) {
                String FilePath       = myFile.getPath();
                List<String> JSONLINE = Files.readAllLines(Paths.get(FilePath));
                String JSONCode       = "";
                JSONCode              = JSONLINE.stream().map((s) -> s).reduce(JSONCode, String::concat);
                JSONParser Jparser    = new JSONParser();
                keepwifi              = (JSONObject) Jparser.parse(JSONCode);                
                json_wifiname         = keepwifi.get("wifi_name").toString();                          
            } else {
                json_wifiname         = "";

            }
        }
        catch(Exception e) { // 2017.11.09 william Json檔案讀寫失敗或錯誤的處理，將IOException 改為通用型例外 Exception
            json_wifiname             = "";
//            System.out.print("keep_wifi_name.json read failed \n");
           // e.printStackTrace();
        }
    }    
    // 寫檔案
    public void WriteWiFiStatusChange() {
        keepwifi = new JSONObject();
        keepwifi.put("wifi_name", GB.wifi_name); 
        
        try {
            File myFile = new File("jsonfile/keep_wifi_name.json");
            if(myFile.exists()) {
                myFile.delete();
                myFile = null;
            }
            try(FileWriter JsonWriter = new FileWriter("jsonfile/keep_wifi_name.json")) {
                JsonWriter.write(keepwifi.toJSONString());
                JsonWriter.flush();
                JsonWriter.close();
            }   
        }
        catch(Exception e) {
           // e.printStackTrace();
        }
    }      
 
    // DHCP寫檔
    private void writeWifiDHCPSetting(String wifi_name, String wifi_security_type, String wifi_pwd)
    {
        String file_path = "/etc/NetworkManager/system-connections/WiFi_" + wifi_name;
        //String file_path = "WiFi_" + wifi_name;
        File myFile=new File(file_path);
        if(myFile.exists()){
            myFile.delete();
            myFile=null;
        }
        try {
            boolean file_create = myFile.createNewFile();
            System.out.println("Create : " + file_create);
        } catch (IOException ex) {
//            Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("Write File 1 ");            
        try (BufferedWriter bufferedDHCPWriter = new BufferedWriter(new FileWriter(file_path,false))) {
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

            if(wifi_security_type.contains("WPA")) {
                bufferedDHCPWriter.write("security=802-11-wireless-security");
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("[802-11-wireless-security]");
                bufferedDHCPWriter.newLine();                        
                bufferedDHCPWriter.write("key-mgmt=wpa-psk");
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("auth-alg=open");
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("psk=" + wifi_pwd);
            } else if (wifi_security_type.contains("WEP")) {
                bufferedDHCPWriter.write("security=802-11-wireless-security");
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.newLine();
                bufferedDHCPWriter.write("[802-11-wireless-security]");
                bufferedDHCPWriter.newLine();                        
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
            bufferedDHCPWriter.flush();
            bufferedDHCPWriter.close();

//            System.out.println("Write File 2 ");
        }
        catch (IOException e) {
            e.printStackTrace();

        }        
    }        
    
    // create folder
    public void createfolder() {
        File theDir = new File("/etc/NetworkManager/system-connections/temp");
        
        if(!theDir.exists()) {
            System.out.println("Creating folder");
            boolean result = false;
            
            try {
                theDir.mkdir();
            } catch(SecurityException se) {
                // handle it
            }
            
            if(result) {
                System.out.println("Folder created");
            }
                        
        } else {
            System.out.println("Folder is created");
        }
        
    }
    
    public void DelayTime(int t) {
        try {
            sleep(t);
        } catch (InterruptedException ex) {
            Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
//    
//    class WindowController{
//        // private String data;
//        GridPane grid = new GridPane();
//        public TextField wifi_Name_TextField;
//        private PasswordField wifi_Password_TextField;
//        private TextField textField01; 
//        private Image EyeClose;
//        private Image EyeOpen;      
//        private  boolean changeAfterSaved = true;  
//        private ToggleButton buttonPw = new ToggleButton();
//        
//        private  boolean opw_changeAfterSaved = true;
//        private  boolean npw_changeAfterSaved = true;
//        private  boolean cpw_changeAfterSaved = true;
//        private ToggleButton buttonOpw=new ToggleButton();
//        private ToggleButton buttonNpw=new ToggleButton();
//        private ToggleButton buttonCpw=new ToggleButton();  
//        private final UUID TC_uniqueKey = UUID.randomUUID();
//        private Bounds mainBounds;
//        Stage stage;
//
//        void showStage(Stage primaryStage, String wifi_name, String wifi_security) {
//            stage = new Stage();
//            stage.initModality(Modality.APPLICATION_MODAL);
//                    
//            GB.reconnect_flag         = false;
//            GB.reconnect_wifiname     = wifi_name;
//            GB.reconnect_securitytype = wifi_security;            
//            
//            EyeClose = new Image("images/CloseEye.png",22,22,false, false);     
//            EyeOpen  = new Image("images/OpenEye5.png",22,22,false, false);    
//            grid.setHgap(5);
//            grid.setVgap(10);
//            grid.setAlignment(Pos.TOP_CENTER);
//            grid.setPadding(new Insets(0, 0, 10, 0));
////            grid.setGridLinesVisible(true); //開啟或關閉表格的分隔線 
//
//        
//            HBox Header = new HBox();
//        
//            Label Title=new Label(WordMap.get("WiFi_Enter_PWD_title"));
//            Header.getChildren().addAll(Title);
//            Header.setAlignment(Pos.CENTER);       
//            Header.setId("Title");
//            grid.add(Header, 0, 0, 8, 1);
//        
//            /*******************wifi名稱***************************/
////            Label emptylable  = new Label("    ");
//            Label emptylable2 = new Label("    ");
//            Label wifi_name_title = new Label(WordMap.get("Wifi_Name") + " :");
//            GridPane.setHalignment(wifi_name_title, HPos.LEFT);
//            wifi_name_title.setId("item");
////            grid.add(emptylable, 0, 1);
//            grid.add(wifi_name_title, 1, 1, 2, 1);   
//            grid.add(emptylable2, 3, 1);
//        
//    
//            wifi_Name_TextField = new TextField();
//            wifi_Name_TextField.setPrefSize(180,35);
//            wifi_Name_TextField.setMaxSize(180,35);
//            wifi_Name_TextField.setMinSize(180,35);       
//            wifi_Name_TextField.setText(wifi_name);
//            wifi_Name_TextField.setDisable(true);
//            wifi_Name_TextField.setStyle("-fx-opacity: 1.0;");
//            grid.add(wifi_Name_TextField,2, 1); 
//   
//                                    
//        /***************新密碼******************/
//        Label wifi_pw_title = new Label(WordMap.get("Wifi_PW")+" :");
//        GridPane.setHalignment(wifi_pw_title, HPos.LEFT);
//        wifi_pw_title.setId("item");
//        grid.add(wifi_pw_title, 1, 2);     
//        wifi_Password_TextField = new PasswordField(); 
//        // wifi_Password_TextField.setPromptText("請輸入wifi密碼");        
//        wifi_Password_TextField.setPrefSize(180,35);//218
//        wifi_Password_TextField.setMaxSize(180,35);
//        wifi_Password_TextField.setMinSize(180,35);
//        grid.add(wifi_Password_TextField, 2, 2);    
//        
//        /************新密碼**********明碼 與 暗碼 轉換**********/ 
//         // Bind the textField and passwordField text values bidirectionally.
//        TextField textField02 = new TextField();
//        textField02.setPrefSize(180,35);
//        textField02.setMaxSize(180,35);
//        textField02.setMinSize(180,35);       
//        // Set initial state
//        textField02.setManaged(false);
//        textField02.setVisible(false);
//        grid.add(textField02,2, 2); 
//        
//
//
//        buttonNpw.setGraphic(new ImageView(EyeClose));
//        grid.add(buttonNpw,2, 2);   
//        buttonNpw.setAlignment(Pos.CENTER);
//        
//        //win7
//        buttonNpw.setTranslateX(181); //移動TextField內的Button位置 220
//
//        
//        buttonNpw.setPrefHeight(33);
//        buttonNpw.setMaxHeight(33);
//        buttonNpw.setMinHeight(33);
//     
//        buttonNpw.setOnAction(new EventHandler<ActionEvent>() {
//            @Override        
//            public void handle(ActionEvent e) {
//                if(npw_changeAfterSaved==true) {                
//                    if (e.getEventType().equals(ActionEvent.ACTION)) {                                                        
//                        textField02.managedProperty().bind(buttonNpw.selectedProperty());
//                        textField02.visibleProperty().bind(buttonNpw.selectedProperty());            
//                        buttonNpw.setGraphic(new ImageView(EyeOpen));                                     
//                    } 
//             
//                    npw_changeAfterSaved=false;             
//                } else {                
//                    if (e.getEventType().equals(ActionEvent.ACTION)) {                                     
//                        wifi_Password_TextField.managedProperty().bind(buttonNpw.selectedProperty().not());
//                        wifi_Password_TextField.visibleProperty().bind(buttonNpw.selectedProperty().not());             
//                        buttonNpw.setGraphic(new ImageView(EyeClose));                    
//                    } 
//                
//                    npw_changeAfterSaved=true;             
//                }
//            
//                textField02.textProperty().bindBidirectional(wifi_Password_TextField.textProperty());                       
//                System.out.println("npw_changeAfterSaved : "+npw_changeAfterSaved+"\n");         
//            }
//        });
//
//        /****************************** Button ******************************/
//        
//        // 確認
//        Button Confirm=new Button(WordMap.get("Confirm"));
//        Confirm.setPrefHeight(30);
//        Confirm.setOnAction((ActionEvent event) -> {
//            
//            if(wifi_Password_TextField.getText().isEmpty()) {
//                Alert_EmptyPWD();
//            } else {
//                try {
//                    Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli d disconnect iface wlan0" });
//                    writeWifiDHCPSetting(wifi_Name_TextField.getText(), wifi_security, wifi_Password_TextField.getText());
//                
//                    Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","chmod 600 '/etc/NetworkManager/system-connections/" + wifi_Name_TextField.getText() + "'"});
//                    GB.wifi_Connectting_status = true;
//                    try {
//                        sleep(1000);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli c up id '" + wifi_Name_TextField.getText() + "' &" });          
//                    stage.close();
//                } catch (IOException ex) {
//                    Logger.getLogger(ThinClient.class.getName()).log(Level.SEVERE, null, ex);
//                }  
//                GB.lock_wifipwd = false;                                            
//            }                                                            
//        });
//        
//        //取消
//        Button Cancel=new Button(WordMap.get("Cancel"));
//        Cancel.setPrefHeight(30);
//        Cancel.setOnAction((ActionEvent event) -> {        
//            stage.close();
//            GB.wifi_Connectting_status = true;
//            GB.lock_wifipwd = false;
//        });
//        
//        // 進階        
//        Button Advanced_DisplayPane = new Button(WordMap.get("Advanced_Settings"));       
//               
//        if("English".equals(WordMap.get("SelectedLanguage"))){
//            Advanced_DisplayPane.setPrefSize(95,30);
//            Advanced_DisplayPane.setMaxSize(95,30);
//            Advanced_DisplayPane.setMinSize(95,30);           
//        }        
//        if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
//            Advanced_DisplayPane.setPrefSize(80,30);
//            Advanced_DisplayPane.setMaxSize(80,30);
//            Advanced_DisplayPane.setMinSize(80,30);    
//        }           
//    
//        
//        HBox OptionBox = new HBox(Cancel, Confirm);
//        OptionBox.setAlignment(Pos.CENTER_LEFT);
//        OptionBox.setSpacing(15);
//        OptionBox.setPadding(new Insets(2, 0, 15, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
//        grid.add(OptionBox, 2, 4, 3, 1);        
//      
//
//            ChangeLabelLang(Title, wifi_name_title, wifi_pw_title);
//            
//
//            
//            
//            Scene scene = new Scene(grid); // , 433, 260
//            
//            stage.focusedProperty().addListener((ov, oldValue, newValue) -> {
//                if(ov.getValue()==false) {
//                    stage.close();
//                    GB.wifi_Connectting_status = true;
//                    GB.lock_wifipwd = false;
//                }
//            });            
//            
//            mainBounds = primaryStage.getScene().getRoot().getLayoutBounds();    
//            // Bounds rootBounds = scene.getRoot().getLayoutBounds();  
//            stage.setX(primaryStage.getX() + (mainBounds.getWidth() - 310 ));
//            stage.setY(primaryStage.getY() + (mainBounds.getHeight() - 313 ));             
//            
//            scene.getStylesheets().add("ChangePassword.css");
//            
//            stage.getIcons().add(new Image("images/Icon2.png"));
//            stage.setScene(scene);
//            stage.initStyle(StageStyle.DECORATED);
//            stage.setResizable(false); //將視窗放大,關閉
//            stage.initOwner(primaryStage);
//            stage.showAndWait(); // show -> showAndWait 
//            
//            stage.setOnCloseRequest(new EventHandler<WindowEvent>() { // 2017.06.09 william Disable click many times will show mutil Dialog
//                @Override
//                public void handle(WindowEvent we) {  
//                    stage.close();
//                    GB.lock_wifipwd = false;                    
//                }
//            });                
//            
//        }
//
//        String getData() {
//            return data;
//        }
//        
//        public void ChangeLabelLang(Label title, Label Label01, Label Label02) {
//        //Label "title", "TableColumn"
//            if("English".equals(WordMap.get("SelectedLanguage"))){
//                title.setStyle("-fx-font-family:'Ubuntu'; -fx-text-fill: white; "); //-fx-font-size: 17px;
//                Label01.setStyle("-fx-font-family:'Ubuntu'; -fx-text-fill: white; "); //-fx-font-size: 17px;
//                Label02.setStyle("-fx-font-family:'Ubuntu'; -fx-text-fill: white; "); //-fx-font-size: 17px;
//            }        
//            if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
//            /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/
//
//                title.setStyle("-fx-font-family: 'Droid Sans Fallback'; -fx-text-fill: white;");
//                Label01.setStyle("-fx-font-family: 'Droid Sans Fallback'; -fx-text-fill: white;");
//                Label02.setStyle("-fx-font-family: 'Droid Sans Fallback'; -fx-text-fill: white;");
//            }      
//        }        
//    
//        public void Alert_EmptyPWD(){
//            if("English".equals(WordMap.get("SelectedLanguage"))) { 
//                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
//                alert.setTitle(WordMap.get("Connect"));
//                alert.setHeaderText(null);
//                
//                alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
//                alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE); 
//
//                alert.setContentText(
//                    WordMap.get("WW_Header") + " ： " + WordMap.get("Message_Error_WiFi_01") +
//                    "\n" +
//                    "\n" +
//                    WordMap.get("Message_Suggest") + " ： " + WordMap.get("Message_advice_WiFi_01")
//                );
//                ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
//                alert.getButtonTypes().setAll(buttonTypeOK); 
//                
//                /*** alert style ***/
//                DialogPane dialogPane = alert.getDialogPane();
//                dialogPane.getStylesheets().add("myDialogs.css");
//                dialogPane.getStyleClass().add("myDialog");
//
//                alert.initModality(Modality.APPLICATION_MODAL);
////                alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係                
//                alert.showAndWait();
//                                
//            }                   
//        
//            if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage"))) || ("SimpleChinese".equals(WordMap.get("SelectedLanguage")))) {
//                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
//                alert.setTitle(WordMap.get("Connect"));
//                alert.setHeaderText(null);
//                alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);           
//                alert.setContentText(
//                    WordMap.get("WW_Header") + " ： " + WordMap.get("Message_Error_WiFi_01") + 
//                    "\n" +
//                    "\n" + 
//                    WordMap.get("Message_Suggest") + " ： " + WordMap.get("Message_advice_WiFi_01")
//                );
//                ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
//                alert.getButtonTypes().setAll(buttonTypeOK); 
//                
//                /*** alert style ***/
//                DialogPane dialogPane = alert.getDialogPane();
//                dialogPane.getStylesheets().add("myDialogs.css");
//                dialogPane.getStyleClass().add("myDialog");
//
//                alert.initModality(Modality.APPLICATION_MODAL);
////                alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係                
//                    alert.showAndWait();                               
//            }        
//        }        
//        
//        public void Alert_ErrorPWD(){
//            if("English".equals(WordMap.get("SelectedLanguage"))) { 
//                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
//                alert.setTitle(WordMap.get("Connect"));
//                alert.setHeaderText(null);
//                
//                alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
//                alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE); 
//
//                alert.setContentText(
//                    WordMap.get("WW_Header") + " ： " + WordMap.get("Message_Error_WiFi_02") +
//                    "\n" +
//                    "\n" +
//                    WordMap.get("Message_Suggest") + " ： " + WordMap.get("Message_advice_WiFi_01")
//                );
//                ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
//                alert.getButtonTypes().setAll(buttonTypeOK); 
//                
//                /*** alert style ***/
//                DialogPane dialogPane = alert.getDialogPane();
//                dialogPane.getStylesheets().add("myDialogs.css");
//                dialogPane.getStyleClass().add("myDialog");
//
//                alert.initModality(Modality.APPLICATION_MODAL);
////                alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係                
//                alert.showAndWait();
//                                
//            }                   
//        
//            if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage"))) || ("SimpleChinese".equals(WordMap.get("SelectedLanguage")))) {
//                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
//                alert.setTitle(WordMap.get("Connect"));
//                alert.setHeaderText(null);
//                alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);           
//                alert.setContentText(
//                    WordMap.get("WW_Header") + " ： " + WordMap.get("Message_Error_WiFi_02") + 
//                    "\n" +
//                    "\n" + 
//                    WordMap.get("Message_Suggest") + " ： " + WordMap.get("Message_advice_WiFi_01")
//                );
//                ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
//                alert.getButtonTypes().setAll(buttonTypeOK); 
//                
//                /*** alert style ***/
//                DialogPane dialogPane = alert.getDialogPane();
//                dialogPane.getStylesheets().add("myDialogs.css");
//                dialogPane.getStyleClass().add("myDialog");
//
//                alert.initModality(Modality.APPLICATION_MODAL);
////                alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係                
//                    alert.showAndWait();                               
//            }        
//        }             
//        
//        
//        
//    // DHCP寫檔
//    private void writeWifiDHCPSetting(String wifi_name, String wifi_security_type, String wifi_pwd){
//   
//        try{
//            File myFile=new File("/etc/NetworkManager/system-connections/" + wifi_name);
//            if(myFile.exists()){
//                myFile.delete();
//                myFile=null;
//            }
//            FileWriter DHCPWriter=new FileWriter("/etc/NetworkManager/system-connections/" + wifi_name, true);
//            try (BufferedWriter bufferedDHCPWriter = new BufferedWriter(DHCPWriter)) {
//                bufferedDHCPWriter.write("[connection]");
//                bufferedDHCPWriter.newLine();
//                bufferedDHCPWriter.write("id=" + wifi_name);
//                bufferedDHCPWriter.newLine();              
//                bufferedDHCPWriter.write("uuid=");
//                bufferedDHCPWriter.write(TC_uniqueKey.toString());                
//                bufferedDHCPWriter.newLine();
//                bufferedDHCPWriter.write("type=802-11-wireless");   
//                bufferedDHCPWriter.newLine();                
//                bufferedDHCPWriter.newLine();
//                bufferedDHCPWriter.write("[802-11-wireless]");                             
//                bufferedDHCPWriter.newLine();
//                bufferedDHCPWriter.write("ssid=" + wifi_name);
//                bufferedDHCPWriter.newLine();              
//                bufferedDHCPWriter.write("mode=infrastructure");
//                bufferedDHCPWriter.newLine();
//                                
//                if(wifi_security_type.contains("WPA")) {
//                    bufferedDHCPWriter.write("security=802-11-wireless-security");
//                    bufferedDHCPWriter.newLine();
//                    bufferedDHCPWriter.newLine();
//                    bufferedDHCPWriter.write("[802-11-wireless-security]");
//                    bufferedDHCPWriter.newLine();                        
//                    bufferedDHCPWriter.write("key-mgmt=wpa-psk");
//                    bufferedDHCPWriter.newLine();
//                    bufferedDHCPWriter.write("auth-alg=open");
//                    bufferedDHCPWriter.newLine();
//                    bufferedDHCPWriter.write("psk=" + wifi_pwd);
//                } else if (wifi_security_type.contains("WEP")) {
//                    bufferedDHCPWriter.write("security=802-11-wireless-security");
//                    bufferedDHCPWriter.newLine();
//                    bufferedDHCPWriter.newLine();
//                    bufferedDHCPWriter.write("[802-11-wireless-security]");
//                    bufferedDHCPWriter.newLine();                        
//                    bufferedDHCPWriter.write("key-mgmt=none");
//                    bufferedDHCPWriter.newLine();
//                    bufferedDHCPWriter.write("wep-key0=" + wifi_pwd);                
//                }                                                
//
//                bufferedDHCPWriter.newLine();
//                bufferedDHCPWriter.newLine();
//                bufferedDHCPWriter.write("[ipv4]");
//                bufferedDHCPWriter.newLine();
//                bufferedDHCPWriter.write("method=auto");
//                bufferedDHCPWriter.newLine();
//                bufferedDHCPWriter.newLine();
//                bufferedDHCPWriter.write("[ipv6]");
//                bufferedDHCPWriter.newLine();
//                bufferedDHCPWriter.write("method=auto");
//                bufferedDHCPWriter.newLine();
//                bufferedDHCPWriter.flush();
//                bufferedDHCPWriter.close();
//            }
//            
//        }catch(IOException e){
//        }
//    
//    }            
//        // 進階設定 - 靜態寫檔
////        private void writeWifiStaticIPSetting(String wifi_name, String wifi_security_type, String wifi_pwd, String SIP, String SMask, String SGateway) {        
////            try{
////                File myFile = new File("/etc/NetworkManager/system-connections/"  + wifi_name);
////                if(myFile.exists()) {
////                    myFile.delete();
////                    myFile = null;
////                }
////                FileWriter StaticIPWriter = new FileWriter("/etc/NetworkManager/system-connections/" + wifi_name, true);
////                try (BufferedWriter bufferedSIPWriter = new BufferedWriter(StaticIPWriter)) {
////                    bufferedSIPWriter.write("[connection]");
////                    bufferedSIPWriter.newLine();
////                    bufferedSIPWriter.write("id=" + wifi_name);
////                    bufferedSIPWriter.newLine();              
////                    bufferedSIPWriter.write("uuid=");
////                    bufferedSIPWriter.write(TC_uniqueKey.toString());                
////                    bufferedSIPWriter.newLine();
////                    bufferedSIPWriter.write("type=802-11-wireless");   
////                    bufferedSIPWriter.newLine();                
////                    bufferedSIPWriter.newLine();
////                    bufferedSIPWriter.write("[802-11-wireless]");                             
////                    bufferedSIPWriter.newLine();
////                    bufferedSIPWriter.write("ssid=" + wifi_name);
////                    bufferedSIPWriter.newLine();              
////                    bufferedSIPWriter.write("mode=infrastructure");
////                    bufferedSIPWriter.newLine();
////                    bufferedSIPWriter.write("security=802-11-wireless-security");
////                    bufferedSIPWriter.newLine();
////                    bufferedSIPWriter.newLine();
////                    bufferedSIPWriter.write("[802-11-wireless-security]");
////                    bufferedSIPWriter.newLine();
////                    
////                    if(wifi_security_type.contains("WPA")) {
////                        bufferedSIPWriter.write("key-mgmt=wpa-psk");
////                        bufferedSIPWriter.newLine();
////                        bufferedSIPWriter.write("auth-alg=open");
////                        bufferedSIPWriter.newLine();
////                        bufferedSIPWriter.write("psk=" + wifi_pwd);
////                    } else if (wifi_security_type.contains("WEP")) {
////                        bufferedSIPWriter.write("key-mgmt=none");
////                        bufferedSIPWriter.newLine();
////                        bufferedSIPWriter.write("wep-key0=" + wifi_pwd);                
////                    }
////                    
////                    bufferedSIPWriter.newLine();
////                    bufferedSIPWriter.newLine();
////                    bufferedSIPWriter.write("[ipv4]");
////                    bufferedSIPWriter.newLine();
////                    bufferedSIPWriter.write("method=manual");
////                    bufferedSIPWriter.newLine();
////                    bufferedSIPWriter.write("address1=");
////                    bufferedSIPWriter.write(SIP);
////                    bufferedSIPWriter.write("/");
////                    bufferedSIPWriter.write(SMask);                
////                    bufferedSIPWriter.write(",");    
////                    
////                    if(SGateway!=null) {
////                        bufferedSIPWriter.write(SGateway);
////                        bufferedSIPWriter.newLine();
////                    } else {
////                        bufferedSIPWriter.newLine();
////                    } 
////                    
////                    bufferedSIPWriter.newLine();
////                    bufferedSIPWriter.write("[ipv6]");
////                    bufferedSIPWriter.newLine();
////                    bufferedSIPWriter.write("method=auto");
////                    bufferedSIPWriter.newLine();                       
////                }            
////            } catch(IOException e) {}    
////        }           
////        
////        
////        
//        
//    }
//    
//    
    
    
    /*------------------------ thread ------------------------*/    
//    public class lock_thr implements Runnable {
//
//        @Override
//        public void run() {
//            try {
//                wifidata_refresh();
//            } catch (IOException ex) {
//                Logger.getLogger(GetWifiInfo.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }      
    
    
    
    
    
}

