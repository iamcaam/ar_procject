/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;


import static acrored_vdi_viewer.PingServer.QM;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.json.simple.JSONObject;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Priority;
import javafx.stage.Screen;
import javafx.stage.WindowEvent;



/**
 *
 * @author duck_chang
 */
public final class VDManagement {
    
    /***    顯示文字對應表    ***/
    public Map<String, String> WordMap=new HashMap<>();
    private Label MyTitle;
   // private TableView table = new TableView();
    public Scene scene = new Scene(new Group());
    public Button colume8button;      
    public TableView<Person> table = new TableView<>();
    public String CurrentIP;
    public String CurrentUserName;
    public String CurrentPasseard;
    public String VdId;    
    public String IsVdOnline;
    public String IsDefault;
    public String VdServers;  
    public String uniqueKey;
    public String VdName;
    public String CreateTime;
    public String VdStatus;
    public String Suspend;
    public String Desc;
    public String selectIP;
    public String selectVdId;
    public Label IsVdOnlineColor;
    public Label AutoSupendColor;
    private Button Confirm;
    
    public String CurrentIP_Port; // 2017.08.10 william IP增加port欄位，預設443
    public JsonArray jsondata; //  2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)
    JsonObject term;  //  2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)
    
    Rectangle2D primaryScreenBounds; // 2018.01.12 william 雲桌面管理 畫面大小處理
    double currentScreenX; // 2018.01.12 william 雲桌面管理 畫面大小處理
    double currentScreenY; // 2018.01.12 william 雲桌面管理 畫面大小處理
    Scene secondScene; // 2018.01.12 william 雲桌面管理 畫面大小處理
    
    //預設值寫進表格內
    public  ObservableList<Person> data =FXCollections.observableArrayList();
    public Stage secondStage = new Stage();
    
    public GB GB;    
    
    // 2018.07.06 關機時，按強制關機按鈕不刪除VD列
    public String Pwd;
    public String UKey;
    private VDMLogin VDML;
    private Timer List_Timer;
    private TimerTask List_Task;    
    TableColumn<Person, String> NameCol3;
    TableColumn<Person, String> NameCol4;            
    TableColumn<Person, String> NameCol5;
    TableColumn<Person, String> NameCol8;    
    
     // 2019.04.10 william  新增強關狀態
    Dictionary dictVDStatus = new Hashtable();    
    
    //String text="test";

    // public VDManagement(Stage primaryStage, Map<String, String> LangMap, String AccountName, String VDName, String suspend, String Satus, String VDOnline, String CTime, String desc, String IPAddr, String vdid, String IPPort ){ 
    public VDManagement(Stage primaryStage, Map<String, String> LangMap, String AccountName, JsonArray jsonArr, String IPAddr, String IPPort,String Password, String UniqueKey ){ //  2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD), 2018.07.06 關機時，按強制關機按鈕不刪除VD列
         WordMap         = LangMap;
        // Stage secondStage = new Stage();                        
         QM              = new QueryMachine(WordMap); //建立呼叫HTTP GET　POST REST JSON 的 Class
         QM.Reset();         
         CurrentIP       = IPAddr;         
         CurrentIP_Port  = IPPort; // 2017.08.10 william IP增加port欄位，預設443
         //  2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)
         jsondata        = jsonArr;
         CurrentUserName = AccountName; //  用戶名稱 
        // 2018.07.06 關機時，按強制關機按鈕不刪除VD列
        Pwd              = Password;
        UKey             = UniqueKey;
        VDML             = new VDMLogin(WordMap);         
//         VdId            = vdid;
//         VdName          = VDName;      //  桌面名稱 
//         Suspend         = suspend;     //  自動暫停
//         VdStatus        = Satus;       //  桌面狀態
//         IsVdOnline      = VDOnline;    //  上線狀態
//         CreateTime      = CTime;       //  建立時間
//         Desc            = desc;        //  備註
         GridPane GP=new GridPane();
         
         primaryScreenBounds = Screen.getPrimary().getVisualBounds(); // 2018.01.12 william 雲桌面管理 畫面大小處理
         currentScreenX = primaryScreenBounds.getWidth();
         currentScreenY = primaryScreenBounds.getHeight();
         System.out.println("Screen width: " + currentScreenX);
         System.out.println("Screen Height: " + currentScreenY);
         
         ColumnConstraints column1 = new ColumnConstraints(); //1100,680 & 1200,742 &(1100,680,Double.MAX_VALUE)        
         column1.setHgrow(Priority.ALWAYS);
         column1.setPercentWidth(100);       
         GP.getColumnConstraints().add(column1);
         GP.setAlignment(Pos.TOP_CENTER);
         GP.setPadding(new Insets(0, 0, 15, 0)); //設定元件和邊界的距離
         GP.setHgap(10); //元件間的水平距離
    	 GP.setVgap(10); //元件間的垂直距離       
      
         //table.setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
         //table.setMaxHeight(Control.USE_PREF_SIZE);
          
         MyTitle=new Label(WordMap.get("ManageVD"));
         /*
         //Title圖示
         ImageView IV=new ImageView();
         IV.setImage(new Image("images/cloud.png"));
         IV.setFitHeight(30);
         IV.setFitWidth(30);
        */
         HBox Title=new HBox();
         Title.setAlignment(Pos.CENTER_LEFT);
         Title.setSpacing(10);
         Title.getChildren().addAll( MyTitle);
         Title.setId("Title");
         GP.add(Title, 0, 0);
        
         HBox ResultBox=new HBox();
         ResultBox.setAlignment(Pos.CENTER);
         ResultBox.setSpacing(10);
         //Grid Table View       
         GP.add(ResultBox, 0, 1);
         
         table.setEditable(true);
       
         /*
         /*****方法一:No.(項目)產生*****
            TableColumn<Person, Number> NameCol0 = new TableColumn<Person, Number>("No.");
            NameCol0.setMinWidth(30);
            NameCol0.setSortable(false);
            NameCol0.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Number>(table.getItems().indexOf(column.getValue())));
          */

         /*****方法二:No.(項目)產生*****/
          TableColumn NameCol0 = new TableColumn(WordMap.get("VD_SN")); 
          if("Japanese".equals(WordMap.get("SelectedLanguage"))) {
            NameCol0.setPrefWidth(90);
            NameCol0.setMinWidth(90);
            //NameCol0.setMaxWidth(90);          
          } else {
            NameCol0.setPrefWidth(60);
            NameCol0.setMinWidth(60);
            //NameCol0.setMaxWidth(60);          
          }

          //NameCol0.setStyle("-fx-alignment: CENTER-RIGHT;");
          NameCol0.setCellValueFactory(new Callback<CellDataFeatures<Person, Person>, ObservableValue<Person>>() {
             @Override
             public ObservableValue<Person> call(CellDataFeatures<Person, Person> p) {
                
                return new ReadOnlyObjectWrapper(p.getValue());
            }
        });
           NameCol0.setCellFactory(new Callback<TableColumn<Person, Person>, TableCell<Person, Person>>() {
            @Override public TableCell<Person, Person> call(TableColumn<Person, Person> param) {
                return new TableCell<Person, Person>() {
                    @Override protected void updateItem(Person item, boolean empty) {
                        super.updateItem(item, empty); 

                        if (this.getTableRow() != null && item != null) {
                            int number=this.getTableRow().getIndex();
                            setText(number + 1 +"");
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });
        NameCol0.setSortable(false);
        //NameCol0.setStyle("-fx-font-family:'Times New Roman';");
        
        //設置表格的title
         TableColumn<Person, String> NameCol1 = new TableColumn(WordMap.get("ID_Name"));    
         NameCol1.setPrefWidth(120);//表格寬度
         NameCol1.setMinWidth(120);
         //NameCol1.setMaxWidth(160);
        //NameCol1.setStyle("-fx-alignment: CENTER-LEFT;");
                 
         TableColumn<Person, String> NameCol2 = new TableColumn(WordMap.get("VD_Name"));
         NameCol2.setPrefWidth(120);
         NameCol2.setMinWidth(120);
         //NameCol2.setMaxWidth(160);

         //NameCol2.setStyle("-fx-alignment: CENTER-LEFT;");
                   
         NameCol3 = new TableColumn(WordMap.get("Stop"));
         NameCol3.setPrefWidth(100);
         NameCol3.setMinWidth(120);
         //NameCol3.setMaxWidth(120);
         //NameCol3.setStyle("-fx-alignment: CENTER-LEFT;");
         NameCol3.setCellFactory(getCustomCellTextFactory()); //  2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)
                   
         NameCol4 = new TableColumn(WordMap.get("Mode"));
         NameCol4.setPrefWidth(150);
         NameCol4.setMinWidth(150);
         //NameCol4.setMaxWidth(150);
         //NameCol4.setStyle("-fx-alignment: CENTER-LEFT;");
         NameCol4.setCellFactory(getCustomCellTextFactory()); //  2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)
                  
         NameCol5 = new TableColumn(WordMap.get("Online"));
         NameCol5.setPrefWidth(140);
         NameCol5.setMinWidth(140);
         //NameCol5.setMaxWidth(140);
         //NameCol5.setStyle("-fx-alignment: CENTER-LEFT;");
         NameCol5.setCellFactory(getCustomCellTextFactory()); //  2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)
                   
         TableColumn<Person, String> NameCol6 = new TableColumn(WordMap.get("Time"));
         NameCol6.setPrefWidth(200);
         NameCol6.setMinWidth(200);
         //NameCol6.setMaxWidth(200);
         //NameCol6.setStyle("-fx-alignment: CENTER-LEFT;");
                   
         TableColumn<Person, String> NameCol7 = new TableColumn(WordMap.get("Comment"));
         NameCol7.setPrefWidth(230);
         NameCol7.setMinWidth(230);
         //NameCol7.setMaxWidth(300);
         //NameCol7.setStyle("-fx-alignment: CENTER-LEFT;");
        
         NameCol8 = new TableColumn(WordMap.get("Action")); // 2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)        
          if("Japanese".equals(WordMap.get("SelectedLanguage"))) {
            NameCol8.setPrefWidth(190);//170  
            NameCol8.setMinWidth(190);
            NameCol8.setMaxWidth(190);     
          } else {
            NameCol8.setPrefWidth(120);//170  
            NameCol8.setMinWidth(120);
            NameCol8.setMaxWidth(120);     
          }         

         //NameCol8.setStyle("-fx-alignment: CENTER;");
         NameCol8.setSortable(false); 
        
          //設定每一個欄位對應的JavaBean物件與Property名稱        
         NameCol1.setCellValueFactory(new PropertyValueFactory<>("ID_Name"));
         NameCol2.setCellValueFactory(new PropertyValueFactory<>("VD_Name"));
         NameCol3.setCellValueFactory(new PropertyValueFactory<>("Stop"));
         NameCol4.setCellValueFactory(new PropertyValueFactory<>("Mode"));
         NameCol5.setCellValueFactory(new PropertyValueFactory<>("Online"));
         NameCol6.setCellValueFactory(new PropertyValueFactory<>("Time"));
         NameCol7.setCellValueFactory(new PropertyValueFactory<>("Comment"));
         NameCol8.setCellValueFactory(new PropertyValueFactory<>("Action"));         
        
        //data.add(new Person("Hans", "Muster","aaa","aaa","aaa","aaa","aaa"));

        // 2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)
        // 顯示可選擇之VD
        for (int i = 0; i < jsondata.size(); i++) {
            term       = jsonArr.get(i).getAsJsonObject();            
            VdId       = term.get("VdId").getAsString();
            VdName     = term.get("VdName").getAsString();
            Suspend    = term.get("Suspend").toString();             
            CreateTime = term.get("CreateTime").getAsString();             
            VdStatus   = term.get("VdStatus").toString();             
            Desc       = term.get("Desc").toString(); 
            
            if( Desc == null ){
                Desc = "";
            }
            if("null".equals(Desc)){
                Desc = "";
            } else {
                Desc = term.get("Desc").getAsString(); 
            }
            
            IsVdOnline = term.get("IsVdOnline").toString(); 

            VDStatusName(VdStatus, NameCol4);
            AutoSuspendName(Suspend, NameCol3);
            ConncentStatusName(IsVdOnline, NameCol5); 
            
            // 2019.04.10 william  新增強關狀態
            if(term.get("VdStatus").toString().equals("1"))
                dictVDStatus.put(VdName, "poweron");
            else
                dictVDStatus.put(VdName, "null");
            
            data.add(new Person(CurrentUserName, VdName, Suspend, VdStatus, IsVdOnline, CreateTime, Desc, VdId)); // 2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)                                          
        }                          
        
        /*------------------------ 產生列表按鈕 ------------------------*/
        CreateTableBtn(NameCol8);     // 2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)
        
        VDM_UI_Refresh(); // 2018.07.06 關機時，按強制關機按鈕不刪除VD列
        
            // Insert Button
//            NameCol8.setCellValueFactory(
//                new Callback<TableColumn.CellDataFeatures<Person, Boolean>, 
//                ObservableValue<Boolean>>() {
//                @Override
//                public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Person, Boolean> p) {
//                    return new SimpleBooleanProperty(p.getValue() != null);
//                }
//            });
//            
//            NameCol8.setCellFactory(
//                new Callback<TableColumn<Person, Boolean>, TableCell<Person, Boolean>>() { 
//                @Override
//                public TableCell<Person, Boolean> call(TableColumn<Person, Boolean> p) {
//                    return new ButtonCell(CurrentIP,VdId,CurrentIP_Port); // 2017.08.10 william IP增加port欄位，預設443
//                }
//            });              
        
        //table.getColumns().add(NameCol8);
        table.getColumns().addAll(NameCol0,NameCol1, NameCol2,NameCol8, NameCol3,NameCol4,NameCol5,NameCol6,NameCol7);        
        table.setItems(data);        
        table.setId("item");
       
        //table內容中英文字型轉換
        /*
         if("English".equals(WordMap.get("SelectedLanguage"))){
            table.setStyle("-fx-font-family:'Times New Roman';");         
        }  
         if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
            table.setStyle("-fx-font-family: '標楷體'");
            table.setStyle("-fx-font-family: DFKai-SB ");
        }
        */
        NameCol8.prefWidthProperty().bind(NameCol8.getTableView().widthProperty().multiply(1.5).divide(2));
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);//自動重新整理大小,table的colum大小隨table的大小變化而變化       
        //table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);//自動重新整理大小,table的column大小固定，則會多出一个空白的column以填充table的大小 
       
        //表格
         final VBox vbox = new VBox();
         vbox.setAlignment(Pos.CENTER);
         vbox.setSpacing(10);        
         vbox.setPadding(new Insets(0, 0, 5, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
         vbox.getChildren().addAll(Title, table);
         ((Group) scene.getRoot()).getChildren().addAll(vbox);
         GP.add(vbox, 0, 0);          
                 
         HBox ConfirmBox=new HBox();
         ConfirmBox.setAlignment(Pos.CENTER);
         ConfirmBox.setSpacing(10);
         //Button Confirm=new Button(WordMap.get("Confirm"));
         Confirm=new Button("    "+WordMap.get("Exit")+"    ");
         Confirm.setPrefHeight(30);
         Confirm.setOnAction((ActionEvent event) -> {
            List_Timer.cancel(); // 2018.07.06 關機時，按強制關機按鈕不刪除VD列
             secondStage.close();
         });
         
         //增加資料
         /*
         Button addB=new Button("add");
         addB.setOnAction((ActionEvent event) -> {
             data.add(new Person("bb", "cc","dd","ee","ff","gg","rootrooto1111"));            
         });     
         ConfirmBox.getChildren().addAll(addB);
         */
         
         ConfirmBox.getChildren().addAll(Confirm);
         GP.add(ConfirmBox, 0, 2);
        
         // 2018.01.12 william 雲桌面管理 畫面大小處理
         if (currentScreenX <= 1280.0 && currentScreenY <= 1024.0) {
            secondScene = new Scene(GP, 950, 545);
         } else {
             secondScene = new Scene(GP);
         }
         
         secondScene.getStylesheets().add("ping.css");
         ChangeLabelLang(MyTitle, NameCol0, NameCol1, NameCol2, NameCol3, NameCol4, NameCol5, NameCol6, NameCol7, NameCol8);
         ChangeButtonLang(Confirm);

         System.out.print("SelectedLanguage: "+WordMap.get("SelectedLanguage")+"\n");
         
        switch (GB.JavaVersion) {
            case 0:
                secondStage.getIcons().add(new Image("images/Icon2.png"));
                break;
            case 1:
                secondStage.getIcons().add(new Image("images/MCIcon2.png"));
                break;                
            default:
                break;
        } 
         secondStage.setTitle(WordMap.get("ManageVD_Title"));         
         secondStage.setScene(secondScene);
         secondStage.initStyle(StageStyle.DECORATED);
         secondStage.initModality(Modality.WINDOW_MODAL); // 2017.09.18 william 錯誤修改(5)-多視窗顯示 NONE -> WINDOW_MODAL
         secondStage.initOwner(primaryStage); 
         secondStage.setResizable(false);//將視窗放大,關閉
         secondStage.show();
         
        // 2018.07.06 關機時，按強制關機按鈕不刪除VD列
        secondStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {  
              System.out.println("SecondStage is closing");
                List_Timer.cancel();
            }
        });          
    }
     public  class Person {
         
      private final SimpleStringProperty ID_Name;
      private final SimpleStringProperty VD_Name;
      private final SimpleStringProperty Stop;
      private final SimpleStringProperty Mode;
      private final SimpleStringProperty Online;
      private final SimpleStringProperty Time;
      private final SimpleStringProperty Comment;
      private final SimpleStringProperty VD_ID; // 2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)

     Person(String id_Name, String vd_Name,String stop,String mode,String online,String time,String comment, String vd_ID) { // 2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)
        this.ID_Name = new SimpleStringProperty(id_Name);
        this.VD_Name = new SimpleStringProperty(vd_Name);
        this.Stop = new SimpleStringProperty(stop);
        this.Mode = new SimpleStringProperty(mode);
        this.Online = new SimpleStringProperty(online);
        this.Time = new SimpleStringProperty(time);
        this.Comment = new SimpleStringProperty(comment);
        this.VD_ID = new SimpleStringProperty(vd_ID); // 2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)
    } 
     
    public String getID_Name() {
        return ID_Name.get();
    }

    public void setID_Name(String id_Name) {
        ID_Name.set(id_Name);
    }

    public StringProperty ID_NameProperty() {
        return ID_Name;
    }
    
    public String getVD_Name() {
        return VD_Name.get();
    }

    public void setVD_Name(String vd_Name) {
        VD_Name.set(vd_Name);
    }

    public StringProperty VD_NameProperty() {
        return VD_Name;
    }

    public String getStop() {
        return Stop.get();
    }

    public void setStop(String stop) {
        Stop.set(stop);
    }

    public StringProperty StopProperty() {
        return Stop;
    }
    
    public String getMode() {
        return Mode.get();
    }

    public void setMode(String mode) {
        Mode.set(mode);
    }

    public StringProperty ModeProperty() {
        return Mode;
    }
    
    public String getOnline() {
        return Online.get();
    }

    public void setOnline(String online) {
        Online.set(online);
    }

    public StringProperty OnlineProperty() {
        return Online;
    }
    
    public String getTime() {
        return Time.get();
    }

    public void setTime(String time) {
        Time.set(time);
    }

    public StringProperty TimeProperty() {
        return Time;
    }
    public String getComment() {
        return Comment.get();
    }

    public void setComment(String comment) {
        Comment.set(comment);
    }

    public StringProperty CommentProperty() {
        return Comment;
    }

    // vd id  2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)
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
    public ObservableList<Person> getPersonData() {
        return data;
    }
    
    
        //Define the button cell
    public class ButtonCell extends TableCell<Person, Boolean> {
        final Button cellButton = new Button(WordMap.get("Shutdown"));
      
        ButtonCell(String address, String vdid, String port){ // 2017.08.10 william IP增加port欄位，預設443
            
            CurrentIP=address;
            VdId=vdid;
            CurrentIP_Port = port;// 2017.08.10 william IP增加port欄位，預設443
            
        	//Action when the button is pressed
            cellButton.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent t) {
                        // get Selected Item
                	Person currentPerson = (Person) ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
                        DoShutdownVD(CurrentIP,VdId,CurrentIP_Port); // 2017.08.10 william IP增加port欄位，預設443
                        System.out.println("currentPerson: "+currentPerson+"\n");
                        /*選擇的桌面雲 強制關機
                        selectIP=ButtonCell.this.getId().concat(CurrentIP);
                        System.out.println("selectIP: "+selectIP+"\n");
                        selectVdId=ButtonCell.this.getId().concat(VdId).toString();                       
                        System.out.println("selectVdId: "+selectVdId+"\n");
                        //DoShutdownVD(selectIP,selectVdId);
                        */
                	//remove selected item from the table list
                	data.remove(currentPerson);
                }
            });
        }
         //Display button if the row is not empty
            @Override
            protected void updateItem(Boolean t, boolean empty) {
                super.updateItem(t, empty);
                if(!empty){
                    setGraphic(cellButton);
                }else{
                    setGraphic(null);
                }
            }      
    }
    
    // 2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)
    public void CreateTableBtn (TableColumn<Person, String> ActionCol) {
        Callback<TableColumn<Person, String>, TableCell<Person, String>> cellFactory = new Callback<TableColumn<Person, String>, TableCell<Person, String>>() {
            @Override
            public TableCell call(final TableColumn<Person, String> param) {
                final TableCell<Person, String> cell = new TableCell<Person, String>() {

                    final Button btn = new Button(WordMap.get("Shutdown"));

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.getStyleClass().add("table-btn");
                            btn.setOnAction(event -> {
                                Person v = getTableView().getItems().get(getIndex());
                                System.out.println("click btn " + v.getVD_ID());
                                
                                // 2019.04.10 william  新增強關狀態
                                dictVDStatus.put(v.getVD_Name(), "shutdowning");
                                v.setMode(WordMap.get("VM_Shutdowning"));
                                
                                DoShutdownVD(CurrentIP,v.getVD_ID(),CurrentIP_Port);
                                // data.remove(v); // 2018.07.06 關機時，按強制關機按鈕不刪除VD列
                                table.refresh(); 
                            });
                            // 2018.07.06 關機時，強制關機按鈕Disable
                            Person vd_status = getTableView().getItems().get(getIndex());
                            switch (vd_status.getMode()) {
                                case "關機":
                                case "关机":
                                case "Shutdown":
                                    btn.setDisable(true);
                                    break;
                                default:
                                    btn.setDisable(false);
                                    break;
                            }                             
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };

        ActionCol.setCellFactory(cellFactory);       
    }
    
    /***********桌面狀態 VD_Status 轉換語言(json)**************/
    public void VDStatusName(String Satus, TableColumn column){
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
         // column.setCellFactory(getCustomCellFactory("green")); // 2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)
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
         // column.setCellFactory(getCustomCellFactory("blue")); // 2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)
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
    
    }
    
    /***********自動暫停 Auto_Suspend 轉換語言(json)**************/
    public void AutoSuspendName(String suspend, TableColumn column){
        Suspend=suspend;
        
        if("true".equals(Suspend)){      
            Suspend= WordMap.get("VD_Stop_true");//AutoSupendColor
            // column.setCellFactory(getCustomCellFactory("green"));    // 2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)
            //System.out.println("-----------VD_Stop_true---------"+"\n");      
        }
        if("false".equals(Suspend)){      
            Suspend= WordMap.get("VD_Stop_false");  
            // column.setCellFactory(getCustomCellFactory("blue")); // 2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)
            //System.out.println("-----------VD_Stop_false---------"+"\n");
        }

    }
    
    /***********上線狀態 IsVdOnline 轉換語言(json)**************/
    public void ConncentStatusName(String isVdOnline,TableColumn column){
        IsVdOnline=isVdOnline;
    
        if("true".equals(IsVdOnline)){      
            IsVdOnline= WordMap.get("VD_Online_true");
            // column.setCellFactory(getCustomCellFactory("green"));    // 2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)
            //System.out.println("-----------VD_Online_true---------"+"\n");      
        }
        if("false".equals(IsVdOnline)){      
            IsVdOnline= WordMap.get("VD_Online_false");
            // column.setCellFactory(getCustomCellFactory("blue"));    // 2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)
            //System.out.println("-----------VD_Online_false---------"+"\n");      
        }

    }
    
    // 2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD) TableColumn 文字 變換顏色
    private Callback<TableColumn<Person, String>, TableCell<Person, String>> getCustomCellTextFactory() { // final String color
        return new Callback<TableColumn<Person, String>, TableCell<Person, String>>() {
            @Override
            public TableCell<Person, String> call(TableColumn<Person, String> param) {
                TableCell<Person, String> cell = new TableCell<Person, String>() {

                    @Override
                    public void updateItem(final String item, boolean empty) {
                        if (item != null) {                            
                            switch (item) {
                                case "開機":
                                case "开机":
                                case "Poweron":
                                    setText(item);
                                    setStyle("-fx-text-fill: " + "green" + ";");
                                    break;
                                case "啟用":
                                case "启用":
                                case "Enable":
                                    setText(item);
                                    setStyle("-fx-text-fill: " + "green" + ";");
                                    break;
                                case "連線":
                                case "连线":
                                case "Connected":
                                    setText(item);
                                    setStyle("-fx-text-fill: " + "green" + ";");
                                    break;
                                case "關機中": // 2019.04.10 william  新增強關狀態
                                case "关机中":
                                case "Shutdowning":
                                    setText(item);
                                    setStyle("-fx-text-fill: " + "red" + ";");
                                    break;                   
                                default:
                                    setText(item);
                                    setStyle("-fx-text-fill: " + "blue" + ";");
                                    break;
                            }
                        }
                    }
                };
                return cell;
            }
        };
    }   
    
    /***********TableColumn 文字 變換顏色**************/
    private Callback<TableColumn<Person, String>, TableCell<Person, String>> getCustomCellFactory(final String color) {
        return new Callback<TableColumn<Person, String>, TableCell<Person, String>>() {

            @Override
            public TableCell<Person, String> call(TableColumn<Person, String> param) {
                TableCell<Person, String> cell = new TableCell<Person, String>() {

                    @Override
                    public void updateItem(final String item, boolean empty) {
                        if (item != null) {
                            setText(item);
                            setStyle("-fx-text-fill: " + color + ";");
                        }
                    }
                };
                return cell;
            }
        };
    }
    
    
    public void DoShutdownVD(String address, String vdid, String port){  //throws MalformedURLException, IOException // 2017.08.10 william IP增加port欄位，預設443
        URL url;
        //selectIP=address;
        //selectVdId=vdid;

        CurrentIP=address;
        VdId=vdid;
        CurrentIP_Port = port;// 2017.08.10 william IP增加port欄位，預設443
        
        JSONObject obj4 =new JSONObject();
        obj4.put("Action", 0);//UUID需有雙引號->將uniqueKey轉為字串toString    
        String action=obj4.toString();
        System.out.println("Action : "+action+"\n"); 
        System.out.println("address : "+CurrentIP+"\n"); 
        System.out.println("vdid : "+VdId+"\n"); 
        System.out.println("port : "+CurrentIP_Port+"\n"); // 2017.08.10 william IP增加port欄位，預設443

        try{

                url = new URL("https://"+CurrentIP+":"+CurrentIP_Port+"/vdi"+"/user"+"/vd/"+VdId+"/action"); //"https://"+CurrentIP+"/vdi/"+"/user/"+"/vd/"+uniqueKey // 2017.08.10 william IP增加port欄位，預設443
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();  //建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線
                conn.setRequestMethod("PUT");//利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
                conn.setRequestProperty("Content-Type", "application/json");//設置發送數據的格式("Content-Type", "application/json; charset=UTF-8")
                conn.setRequestProperty("Content-length", String.valueOf(action.getBytes().length));//("Content-length", String.valueOf(query.length()))
                conn.setRequestProperty("Connection", "Keep-Alive");//維持長連接
                //conn.connect();    
                conn.setUseCaches (false);
                conn.setInstanceFollowRedirects(true);

                System.out.println("-------------content----------\n"); 

                try (OutputStream out2 = conn.getOutputStream()) {
                     out2.write(action.getBytes());
                     out2.flush();
                     out2.close();
                      System.out.println("DoShutdownVD-output out : "+out2+"\n");

                System.out.println("DoShutdownVD-URL : "+url+"\n"); 
                //System.out.println("SetVDOnline-conn.getOutputStream() : "+conn.getOutputStream()+"\n");
                System.out.println("DoShutdownVD-Resp Code : "+conn.getResponseCode()+"\n");
                System.out.println("DoShutdownVD-Resp Message:"+ conn.getResponseMessage()+"\n");            

                InputStream is2 = conn.getInputStream();           
                StringBuilder sb2;
                String line2;  

                 try (BufferedReader br2 = new BufferedReader(new InputStreamReader(is2))) {              
                     sb2 = new StringBuilder("");
                     while ((line2 = br2.readLine()) != null) {            
                         sb2.append(line2);         
                     }                 
                    //System.out.println("SetVDOnline-return br: "+br2.readLine()+"\n");
                    System.out.println("DoShutdownVD-return sb: "+sb2.toString()+"\n");         
                     br2.close();
                 }

                 conn.disconnect();      
                }            

            } catch (MalformedURLException e) {

            } catch (IOException e) {    

            } 
        
    }
     
       /****變更Label字型****字型:中文-標楷體,英文:Times New Roman****/
    public void ChangeLabelLang(Label title, TableColumn tc00, TableColumn tc01, TableColumn tc02, TableColumn tc03, TableColumn tc04, TableColumn tc05, TableColumn tc06, TableColumn tc07, TableColumn tc08){
        //Label "title", "TableColumn"
        if("English".equals(WordMap.get("SelectedLanguage"))){
            title.setStyle("-fx-font-family:'Times New Roman';"); //-fx-font-size: 17px;
            tc00.setStyle("-fx-font-family:'Times New Roman';");
            tc01.setStyle("-fx-font-family:'Times New Roman';");
            tc02.setStyle("-fx-font-family:'Times New Roman';");
            tc03.setStyle("-fx-font-family:'Times New Roman';");
            tc04.setStyle("-fx-font-family:'Times New Roman';");
            tc05.setStyle("-fx-font-family:'Times New Roman';");
            tc06.setStyle("-fx-font-family:'Times New Roman';");
            tc07.setStyle("-fx-font-family:'Times New Roman';");
            tc08.setStyle("-fx-font-family:'Times New Roman';");
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/
            //winXP 僅能用中文名稱
            title.setStyle("-fx-font-family: '微軟正黑體';");//-fx-font-weight: 900;
            tc00.setStyle("-fx-font-family: '微軟正黑體';");//;-fx-font-weight: 700; 
            tc01.setStyle("-fx-font-family: '微軟正黑體'; ");
            tc02.setStyle("-fx-font-family: '微軟正黑體'; ");
            tc03.setStyle("-fx-font-family: '微軟正黑體'; ");
            tc04.setStyle("-fx-font-family: '微軟正黑體'; ");
            tc05.setStyle("-fx-font-family: '微軟正黑體'; ");
            tc06.setStyle("-fx-font-family: '微軟正黑體'; ");
            tc07.setStyle("-fx-font-family: '微軟正黑體'; ");
            tc08.setStyle("-fx-font-family: '微軟正黑體'; ");
            
            //win7以上僅能用英文名稱
            title.setStyle("-fx-font-family: 'Microsoft JhengHei'; ");
            tc00.setStyle("-fx-font-family: 'Microsoft JhengHei'; ");//;-fx-font-weight: 700; 
            tc01.setStyle("-fx-font-family: 'Microsoft JhengHei';  ");
            tc02.setStyle("-fx-font-family: 'Microsoft JhengHei';  ");
            tc03.setStyle("-fx-font-family: 'Microsoft JhengHei';  ");
            tc04.setStyle("-fx-font-family: 'Microsoft JhengHei';  ");
            tc05.setStyle("-fx-font-family: 'Microsoft JhengHei';  ");
            tc06.setStyle("-fx-font-family: 'Microsoft JhengHei';  ");
            tc07.setStyle("-fx-font-family: 'Microsoft JhengHei';  ");
            tc08.setStyle("-fx-font-family: 'Microsoft JhengHei';  ");
            
        }
       
     }
    /****變更button字型****字型:中文-標楷體,英文:Times New Roman****/
    public void ChangeButtonLang(Button but01){
        //Button "Leave"
        if("English".equals(WordMap.get("SelectedLanguage"))){
            but01.setStyle("-fx-font-family:'Times New Roman';");         
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/
            //winXP 僅能用中文名稱
            but01.setStyle("-fx-font-family: '微軟正黑體';");
            
            //win7以上僅能用英文名稱
            but01.setStyle("-fx-font-family: 'Microsoft JhengHei';");          
        }  
        
     }
    
    // 2018.07.06 關機時，按強制關機按鈕不刪除VD列
    public void VDM_UI_Refresh() {  
        List_Timer = new Timer(); 
        List_Task = new TimerTask() {    
            @Override
            public void run() {
                try {
                    VDML.VDManagementLogin(CurrentIP, CurrentUserName, Pwd, UKey, CurrentIP_Port); // 2017.08.10 william IP增加port欄位，預設443
                } catch (IOException ex) {
                    Logger.getLogger(VDManagement.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if(VDML.VDML_jsonArr != null && VDML.VDML_jsonArr.size() > 0 ) {                
                    data.clear();
                    for (int i = 0; i < VDML.VDML_jsonArr.size(); i++) {
                        term       = VDML.VDML_jsonArr.get(i).getAsJsonObject();            
                        VdId       = term.get("VdId").getAsString();
                        VdName     = term.get("VdName").getAsString();
                        Suspend    = term.get("Suspend").toString();             
                        CreateTime = term.get("CreateTime").getAsString();             
                        VdStatus   = term.get("VdStatus").toString();             
                        Desc       = term.get("Desc").toString(); 
          
                        if( Desc == null ){
                            Desc = "";
                        }
                        if("null".equals(Desc)){
                            Desc = "";
                        } else {
                            Desc = term.get("Desc").getAsString(); 
                        }
            
                        IsVdOnline = term.get("IsVdOnline").toString(); 

                        VDStatusName(VdStatus, NameCol4);
                        AutoSuspendName(Suspend, NameCol3);
                        ConncentStatusName(IsVdOnline, NameCol5); 
                        
                        // 2019.04.10 william  新增強關狀態
                        if("shutdowning".equals(dictVDStatus.get(VdName).toString()) && !term.get("VdStatus").toString().equals("5")) { 
                            VdStatus = WordMap.get("VM_Shutdowning");
                            dictVDStatus.put(VdName, "shutdowning");
                        }  else {
                            if(term.get("VdStatus").toString().equals("1"))
                                dictVDStatus.put(VdName, "poweron");
                            else
                                dictVDStatus.put(VdName, "null");                                                  
                        }                      
                        
                      
                        
                        data.add(new Person(CurrentUserName, VdName, Suspend, VdStatus, IsVdOnline, CreateTime, Desc, VdId)); // 2018.01.04 willaim 修改雲桌面管理功能 (可列多個VD)                                          
                    }                          
        
                    CreateTableBtn(NameCol8);        
                }
            }        
        };    

        List_Timer.schedule(List_Task, 1, 8000);  
    }    

}
 
