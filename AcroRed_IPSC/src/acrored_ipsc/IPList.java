/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_ipsc;

import com.google.gson.JsonArray;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

/**
 *
 * @author victor
 */
public class IPList {
    int MainGP_width = 590;
    int MainGP_height = 365;    
    int secondScene_width = 720; 
    int secondScene_heighth = 456;   
    int ip_tableView_width = 733;
    int ip_tableView_height = 307;  
    int btn_width = 80;
    int btn_height = 30;    
    public static int select_index; 
    GridPane MainGP; 
    Stage IPList_secondStage = new Stage();
    public Map<String, String> WordMap = new HashMap<>();
    private TableView<IP> ip_table = new TableView();        
    public ObservableList<IP> ip_data = FXCollections.observableArrayList();  // 預設值寫進表格內
    boolean connectFlag = false;
    public String Select_IP;
    public String Select_Port;
    public static String _ip;
    public static String _port;
        
    public IPList(Stage primaryStage, Map<String, String> LangMap) { // , JsonArray jsonArr
        WordMap = LangMap;
        /***  主畫面使用GRID的方式Layout ***/        
        MainGP = new GridPane();
        MainGP.setGridLinesVisible(false);       
        MainGP.setAlignment(Pos.CENTER);       
        MainGP.setPadding(new Insets(35, 50, 50, 20)); 
        MainGP.setHgap(15);                            
   	MainGP.setVgap(15);                            
        MainGP.setPrefSize(MainGP_width, MainGP_height);
        MainGP.setMaxSize(MainGP_width, MainGP_height);
        MainGP.setMinSize(MainGP_width, MainGP_height);         
        MainGP.setTranslateY(-5);
        MainGP.setTranslateX(-3);
        
        ip_table.setRowFactory(tv -> new TableRow<IP>() {            
            {                               
                // double click
                setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (! this.isEmpty()) && !connectFlag) { // 2018.12.12 新增 431NoVGA 和第一次開機                   
                        connectFlag = true; // 2018.12.12 新增 431NoVGA 和第一次開機
                        IP rowData = this.getItem();
                        Select_IP = rowData.getIP();
                        Select_Port = rowData.getPort();
 
                            //ConnectCmd(Select_IP, Select_Port);
                                                                                                                           
                    } else {
                        IP rowData = this.getItem();             
                        if(rowData != null) {
                            setip(rowData.getIP());                    
                            setport(rowData.getPort());                    
                            setSelectIndex(ip_table.getSelectionModel().getSelectedIndex());                             
                        }
                
                    }                    
                }); 
            }
        });         
                
        
        // 畫面上層
        top_layout();     
        // 畫面中層
        ip_layout();
        // 畫面下層
        bottom_layout();
        
        ip_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // 2018.04.10  william  功能修改 Remove extra column in JavaFX TableView http://www.superglobals.net/remove-extra-column-tableview-javafx/
        
        /***  底層畫面使用Border的方式Layout ***/
        BorderPane rootPane = new BorderPane();     
        rootPane.setCenter(MainGP);        
        rootPane.setStyle("-fx-background-color: #0D47A1;");
        
        Scene secondScene = new Scene(rootPane, secondScene_width, secondScene_heighth); // 590, 365
        secondScene.getStylesheets().add("iplistStyle.css");        
        
        Bounds mainBounds = primaryStage.getScene().getRoot().getLayoutBounds();
        IPList_secondStage.setX(primaryStage.getX() + (mainBounds.getWidth() - secondScene_width ) / 2);
        IPList_secondStage.setY(primaryStage.getY() + (mainBounds.getHeight() - secondScene_heighth ) / 2); 
        IPList_secondStage.setScene(secondScene);
        IPList_secondStage.setTitle(WordMap.get("Select_IP_List_title"));  // 視窗標題        
        IPList_secondStage.initStyle(StageStyle.DECORATED);     // DECORATED -> UNDECORATED 
        IPList_secondStage.initModality(Modality.WINDOW_MODAL); // window modal lock NONE -> WINDOW_MODAL
        IPList_secondStage.initOwner(primaryStage);
        IPList_secondStage.setResizable(false);                 // 將視窗放大,關閉             
        IPList_secondStage.showAndWait();                

        // 新增Stage關閉事件
        IPList_secondStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("IP list is closing");           
                StopAll(); // 2018.12.04 新增VD&RDP狀態判斷                            
                IPList_secondStage.close();
            }
        });         
        
    
    } 
    
    // 畫面上層
    public final void top_layout() {
        /***  Title 放入一個HBOX內 ***/
        Label Title = new Label(WordMap.get("Select_IP_List_title")); // WordMap.get("Select_VD")
        
        HBox title_hbox = new HBox(Title);
        title_hbox.setAlignment(Pos.TOP_CENTER);
        title_hbox.setId("Title"); 
        title_hbox.setPadding(new Insets(0, 0, 0, 0));
        
                title_hbox.setTranslateY(31); 
                title_hbox.setTranslateX(16);         
        
//        switch (WordMap.get("SelectedLanguage")) { 
//            case "English":
//                title_hbox.setTranslateY(31); 
//                title_hbox.setTranslateX(16); 
//                break;
//            default:
//                title_hbox.setTranslateY(39);
//                title_hbox.setTranslateX(16);   
//                break;
//        }  
        MainGP.add(title_hbox,0,0);
    }        
    // 畫面中層
    public final void ip_layout() {
        ip_data.clear();
        TableColumn SNCol;
        TableColumn<IP, String> IPCol;     
        TableColumn<IP, String> PortCol;      
        
        SNCol = new TableColumn(WordMap.get("SN")); // 項次   WordMap.get("VD_SN")
        
        SNCol.setCellValueFactory(new Callback<CellDataFeatures<IP, IP>, ObservableValue<IP>>() {
             @Override
             public ObservableValue<IP> call(CellDataFeatures<IP, IP> v) {
                return new ReadOnlyObjectWrapper(v.getValue());
            }
        });        
        
        SNCol.setCellFactory(new Callback<TableColumn<IP, IP>, TableCell<IP, IP>>() {
            @Override 
            public TableCell<IP, IP> call(TableColumn<IP, IP> param) {
                return new TableCell<IP, IP>() {
                    @Override 
                    protected void updateItem(IP item, boolean empty) {
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
        
        SNCol.setSortable(false);
        SNCol.setPrefWidth(60);        
        SNCol.setMinWidth(60);
        SNCol.setMaxWidth(60);        
        
        IPCol = new TableColumn("IP");          
        IPCol.setCellValueFactory(new PropertyValueFactory<IP, String>("IP"));   
//        IPCol.setPrefWidth(268); 
//        IPCol.setMinWidth(268);
//        IPCol.setMaxWidth(268);        
        IPCol.setStyle( "-fx-alignment: center-left;"); 

        PortCol = new TableColumn("Port");   
        PortCol.setCellValueFactory(new PropertyValueFactory<IP, String>("Port"));   
//        PortCol.setPrefWidth(250); 
//        PortCol.setMinWidth(250);
//        PortCol.setMaxWidth(250);           
        PortCol.setStyle( "-fx-alignment: center-left;");           
        
        String[] fakeIP = {"192.168.88.251:3260", "192.168.33.251:3260"};
        String[] Tmp;
        IP[] _array;
        _array = new IP[fakeIP.length];
        
        for (int i = 0; i < fakeIP.length; i++) {
            Tmp = fakeIP[i].split(":");
            _array[i] = new IP(Tmp[0], Tmp[1]);
        }
        
        ip_data.addAll(_array);
                           
        ip_table.setEditable(false);              
        ip_table.getColumns().addAll(SNCol, IPCol, PortCol); 
        ip_table.setItems(ip_data);     
        ip_table.getStyleClass().add("table_header");
        ip_table.setPrefSize(ip_tableView_width, ip_tableView_height);
        ip_table.setMaxSize(ip_tableView_width , ip_tableView_height);
        ip_table.setMinSize(ip_tableView_width , ip_tableView_height);         

        // 設定第一個元素為被選擇狀態
        ip_table.requestFocus();
        ip_table.getSelectionModel().select(0);
        ip_table.getFocusModel().focus(0);
        setSelectIndex(0);
        
        HBox selectionhbox = new HBox();
	selectionhbox.setSpacing(20);       
        selectionhbox.setAlignment(Pos.CENTER);
        selectionhbox.setPadding(new Insets(0, 0, 0, 0));  
	selectionhbox.getChildren().addAll(ip_table);    
        selectionhbox.setTranslateX(17);
        selectionhbox.setTranslateY(15);
        
        MainGP.add(selectionhbox ,0,2);                          
    }
    // 畫面下層 (描述及按鈕)
    public final void bottom_layout() {
        // 建立Label物件（描述）
        Label desc1label = new Label(WordMap.get("IP_List_Desc1"));
        desc1label.setId("List_label");
        Label desc2label = new Label(WordMap.get("IP_List_Desc2"));
        desc2label.setId("List_label");
        
        VBox Descvbox = new VBox();
	Descvbox.setSpacing(10);
        Descvbox.setTranslateY(18); 
        Descvbox.setTranslateX(39); 
        Descvbox.setAlignment(Pos.CENTER_LEFT);
        Descvbox.setPadding(new Insets(0, 0, 0, 0));        
        Descvbox.getChildren().addAll(desc1label, desc2label);      
        
        MainGP.add(Descvbox,0,3);                
        
        Button Btn_exit;
        Button Btn_login; 
        Btn_login = new Button(WordMap.get("Confirm_Btn"));
                Btn_login.setPrefSize(btn_width, btn_height);                  
                Btn_login.setMaxSize(btn_width, btn_height);                   
                Btn_login.setMinSize(btn_width, btn_height);          
                
        switch (WordMap.get("SelectedLanguage")) { // 2018.04.12 william  多VD登入新增開機狀態
            case "English":
                Btn_login.setPrefSize(100, btn_height);                  
                Btn_login.setMaxSize(100, btn_height);                   
                Btn_login.setMinSize(100, btn_height);  
                break;
            default:
                Btn_login.setPrefSize(btn_width, btn_height);                  
                Btn_login.setMaxSize(btn_width, btn_height);                   
                Btn_login.setMinSize(btn_width, btn_height);  
                break;
        }                 


        // Btn_exit.setTranslateY(-27);
        Btn_login.setId("LoginMultiVD_Exit");
        Btn_login.setOnAction((ActionEvent event) -> {    
            //GB.IP_Status = getip() +  ":" + getport();     
            StopAll(); // 2018.12.04 新增VD&RDP狀態判斷
            IPList_secondStage.close();           
        });         
        Btn_exit = new Button(WordMap.get("Exit_Btn"));
//        // 建立按鈕物件(離開)
//        switch (WordMap.get("SelectedLanguage")) {
//            case "English":
//                Btn_exit = new Button("Exit");
//                break;
//            case "SimpleChinese":
//                Btn_exit = new Button("离开");                
//                break;
//            case "TraditionalChinese":
//                Btn_exit = new Button("離開");                
//                break;                
//            default:
//                Btn_exit = new Button("Exit");             
//                break;
//        }         
        Btn_exit.setPrefSize(btn_width, btn_height);                  // 強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        Btn_exit.setMaxSize(btn_width, btn_height);                   // 強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        Btn_exit.setMinSize(btn_width, btn_height);                   // 強制限制button最小寬度/高度,讓每個button在變換語言時不會變動
//        Btn_exit.setTranslateY(-27);
//        Btn_exit.setTranslateX(15);
        Btn_exit.setId("LoginMultiVD_Exit");
        Btn_exit.setOnAction((ActionEvent event) -> {
            StopAll(); // 2018.12.04 新增VD&RDP狀態判斷
            IPList_secondStage.close();
        });              

        HBox btnhbox = new HBox();
	btnhbox.setSpacing(20);
        btnhbox.setAlignment(Pos.CENTER_RIGHT);
        btnhbox.setPadding(new Insets(0, 0, 0, 20));   
        btnhbox.setTranslateY(-31); // -25 // 2018.04.12 william  多VD登入新增開機狀態 -55
        btnhbox.setTranslateX(-5); // 2018.04.12 william  多VD登入新增開機狀態 12
        btnhbox.getChildren().addAll(Btn_exit, Btn_login);   
		
        MainGP.add(btnhbox,0,4);  
    }        
    
        
    // 定義IP資料模型    
    public class IP {         
        private final SimpleStringProperty IP;
        private final SimpleStringProperty Port;
      

        IP(String _ip, String _port) { 
            this.IP = new SimpleStringProperty(_ip);
            this.Port = new SimpleStringProperty(_port);
        } 
        // iscsi ip
        public String getIP() {
            return IP.get();
        }

        public void setIP(String _ip) {
            IP.set(_ip);
        }

        public StringProperty IPProperty() {
            return IP;
        }
        // iscsi port
        public String getPort() {
            return Port.get();
        }

        public void setPort(String _port) {
            Port.set(_port);
        }

        public StringProperty PortProperty() {
            return Port;
        }        
    }    
    
    public static synchronized String getip() {
        return _ip;
    }

    public static synchronized void setip(String ip) {
        _ip = ip;
    }     
    
    public static synchronized String getport() {
        return _port;
    }

    public static synchronized void setport(String port) {
        _port = port;
    }       
    
    public static synchronized int getSelectIndex() {
        return select_index;
    }

    public static synchronized void setSelectIndex(int _index) {
        select_index = _index;
    }         
    
    public void StopAll() {
   
    }    
    
}
