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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author root
 */
public class TimeSetting {
    
    
    public Map<String, String> WordMap=new HashMap<>();
    
    public Stage TimeSetting_stage = new Stage();
    private Button Cencel;
    private Button Confirm;
    private Button Leave;
    private Button TimeUpdate;
    private BorderPane TS_root = new BorderPane();
    private Scene TS_scene;
    private Label TSTitle = new Label();
    private String CurrentTime_now;
    private String zoneId;
    private String zoneName;
    private ComboBox TimeZoneSelection;
    private ComboBox Hour_Selection;
    private ComboBox Min_Selection;
    private ComboBox Sec_Selection;
    private ComboBox Server_Selection;
    private Label New_CurrentTime;
    private String TimeZone_selectItem; 
    private String TimeZone_selectItem_zoneId ; 
    private String TimeZone_CurrentZoneName ; 
    private ZoneId zoneidDefault ; 
    private ToggleGroup group;
    private RadioButton rb1_Manually = new RadioButton();
    private RadioButton rb2_AutoSync = new RadioButton();
    private DatePicker checkInDatePicker;
    private final String pattern = "yyyy-MM-dd";
    private String Hour_selectItem ;
    private String Min_selectItem ;
    private String Sec_selectItem ;
    private String Current_Hour ;
    private String Current_Min ;
    private String Current_Sec ;  
    public boolean TimeUpdate_lock = false;
    private boolean Cencel_lock = false;
    private boolean Confirm_lock = false;
    private boolean Manually_or_Auto = false;
    private String TimeZone_outputID;
    private Stage PrimaryStage;
    private String DefaultZoneId ;
    private String CurrentDate ;
    private String CurrentTime ;
    private Process Zoneid_process;
    private Process CurrentDT_process;
    private String CurrentDate_Year;
    private String CurrentDate_Mounth_Eng;
    private String CurrentDate_Mounth;
    private String CurrentDate_Day;
    private String CurrentTime_Hour;
    private String CurrentTime_Min;
    private String CurrentTime_Sec;
    private String date_str;
    private String time_str;
    private String Date_selectItem;
    private Timer timer;
    
    private boolean update_lock = false;    // 2017.06.14 william  UpdateDateandTime() lock use
    
    public String NTP_Stop          = "service ntp stop";        // 2017.06.16 william  Time setting fix  
    public String NTP_Start         = "service ntp start";       // 2017.06.16 william  Time setting fix
    public String NTPDate_Check     = "ntpdate -s pool.ntp.org"; // 2017.06.16 william  Time setting fix
    public int Auto_Time_ReturnCode = -999;                      // 2017.06.16 william  Time setting fix
    
    public GB GB;
    JSONObject TimeStatus;
    
    ObservableList<String> TimeZone_list =FXCollections.observableArrayList();   
    ObservableList<String> Hour_list = FXCollections.observableArrayList();
    ObservableList<String> Min_list = FXCollections.observableArrayList();
    ObservableList<String> Sec_list = FXCollections.observableArrayList(); //Integer
    
    public TimeSetting(Stage primaryStage, Map<String, String> LangMap)throws MalformedURLException, IOException, ParseException{
        
        TS_scene = null;
        // System.gc();//清除系統垃圾 // 2017.06.05 william System.gc disable
        
        WordMap = LangMap;
        PrimaryStage = primaryStage ;
        
        /** Title **/
        TSTitle.setText(WordMap.get("TimeSettings_title"));
        HBox TSTitle_Box=new HBox();
        TSTitle_Box.getChildren().addAll(TSTitle);
        TSTitle_Box.setAlignment(Pos.CENTER);      
        TSTitle_Box.setId("Time_Title");
        
        /***  將 主畫面 放入 GridPane 內 , 並放置再 BorderPane setCenter  ***/
        GridPane grid_main = new GridPane();
        //grid_main.setGridLinesVisible(true); //開啟或關閉表格的分隔線       
        grid_main.setHgap(8);
        grid_main.setVgap(8);     
//        grid_main.setPrefSize(400, 200); //680, 400
//        grid_main.setMaxSize(400, 200);
//        grid_main.setMinSize(400, 200); 
        grid_main.setAlignment(Pos.CENTER);
        grid_main.setPadding(new Insets(25, 20, 0, 20));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        //grid_main.setStyle("-fx-background-color: #F5F5F5;");
        
        /***  功能(下)畫面使用Anchor的方式Layout ***/
        AnchorPane anchor_control = new AnchorPane();
        anchor_control.setPrefHeight(80);
        anchor_control.setMaxHeight(80);
        anchor_control.setMinHeight(80);
        
        /** 現在時間 **/
        Label CurrentTime_title = new Label(WordMap.get("TimeSettings_CurrentTime"));
        GridPane.setHalignment(CurrentTime_title, HPos.LEFT);      
	grid_main.add(CurrentTime_title, 0, 0);
        //CurrentTime.setPadding(new Insets(0, 0, 0, 8));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)  
        
        Label Colon1=new Label(":");//將":"固定在一個位置
        GridPane.setHalignment(Colon1, HPos.LEFT);
        grid_main.add(Colon1, 1, 0); 
        Colon1.setId("Colon_Label");
        //Colon1.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)             
        
        New_CurrentTime = new Label();
        GridPane.setHalignment(New_CurrentTime, HPos.LEFT);      
	grid_main.add(New_CurrentTime, 2, 0);
        //CurrentTime.setPadding(new Insets(0, 0, 0, 8));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        
//        String date_str = ZonedDateTime.now().getYear() + " - " + ZonedDateTime.now().getMonthValue() + " - " + ZonedDateTime.now().getDayOfMonth() ;
//        String time_str = ZonedDateTime.now().getHour() + " : " + ZonedDateTime.now().getMinute() + " : " + ZonedDateTime.now().getSecond() ;    
//        //String date_str = LocalDate.now().getYear() + " - " + LocalDate.now().getMonthValue() + " - " + LocalDate.now().getDayOfMonth() ;
//        //String time_str = LocalTime.now().getHour() + " : " + LocalTime.now().getMinute() + " : " + LocalTime.now().getSecond() ;         
//        New_CurrentTime = date_str +"   "+ time_str ; 

        GetCurrentDateandTime();
        date_str = CurrentDate_Year + " - " + CurrentDate_Mounth + " - " + CurrentDate_Day ;
        time_str = CurrentTime_Hour + " : " + CurrentTime_Min + " : " + CurrentTime_Sec ;  
        CurrentTime_now = date_str +"   "+ time_str ;
        
        New_CurrentTime.setText( CurrentTime_now );
        
        /** Time Thread **/
        UpdateDateandTime();

        /** 時區 **/
        Label TimeZone_title = new Label(WordMap.get("TimeSettings_TimeZone"));
        GridPane.setHalignment(TimeZone_title, HPos.LEFT);      
	grid_main.add(TimeZone_title, 0, 2);
        //CurrentTime.setPadding(new Insets(0, 0, 0, 8));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)  
        
        Label Colon2=new Label(":");//將":"固定在一個位置
        GridPane.setHalignment(Colon2, HPos.LEFT);
        grid_main.add(Colon2, 1, 2); 
        Colon2.setId("Colon_Label");
        //Colon1.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)   
        
        /****** TimeZone 時區的選擇(ComboBox) ******/      
        GetSystemZoneID();
        LoadTimeZone();
        TimeZoneSelection=new ComboBox(TimeZone_list);          
        TimeZoneSelection.getSelectionModel().select(TimeZone_CurrentZoneName);
        System.out.println(" TimeZone_CurrentZoneName : "+ TimeZone_CurrentZoneName +"\n");
        TimeZone_selectItem = null;
        TimeZoneSelection.setPrefWidth(520);
        TimeZoneSelection.setMaxWidth(520);
        TimeZoneSelection.setMinWidth(520);        
        /***  以下抓取變更 TimeZone 時區 後的動作  ***/
        TimeZoneSelection.setOnAction((Event event) -> {

            try {
                TimeZone_selectItem = TimeZoneSelection.getValue().toString(); 
                System.out.println(" TimeZone_selectItem : "+ TimeZone_selectItem +"\n");
                LoadTimeZone();
                System.out.println(" TimeZone_selectItem_zoneId : "+ TimeZone_selectItem_zoneId +"\n");
                
            } catch (ParseException ex) {
                Logger.getLogger(TimeSetting.class.getName()).log(Level.SEVERE, null, ex);
            }
        });       
        grid_main.add(TimeZoneSelection,2,2);
        
        /** 時間設定 Title **/
        Label TimeSetting_title = new Label(WordMap.get("TimeSettings_title"));
        GridPane.setHalignment(TimeSetting_title, HPos.LEFT);      
	grid_main.add(TimeSetting_title, 0, 4);     
        //Creating a line object         
        Line line_t = new Line() ;
        line_t.setStartX(100.0) ; 
        line_t.setStartY(150.0) ; 
        line_t.setEndX(630.0) ; 
        line_t.setEndY(150.0) ; 
        line_t.setStrokeWidth(2);
        line_t.setStrokeDashOffset(1);
        //line_t.setStrokeLineCap(StrokeLineCap.ROUND);
        line_t.setStroke(Color.WHITE);
        Group line_title = new Group(line_t) ;
        grid_main.add(line_title,1,4,2,1);
        
        
        /************RadioButton : 手動 & 自動同步 ************/
        group = new ToggleGroup();
        rb1_Manually.setText(WordMap.get("TimeSettings_Manually"));
        rb1_Manually.setToggleGroup(group);
        grid_main.add(rb1_Manually,0,6);
        rb1_Manually.setPadding(new Insets(0, 0, 0, 10));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        //rb1.setUserData("RadioButton1");
        //rb1.setSelected(true);
        //rb1.requestFocus();

        rb2_AutoSync.setText(WordMap.get("TimeSettings_AutoSync"));      
        rb2_AutoSync.setToggleGroup(group);
        // group.selectToggle(rb2_AutoSync);
        grid_main.add(rb2_AutoSync,0,12,3,1);   
        rb2_AutoSync.setPadding(new Insets(0, 0, 0, 10));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        
        /** 日期 **/
        Label Date_title = new Label(WordMap.get("TimeSettings_date"));
        GridPane.setHalignment(Date_title, HPos.LEFT);      
	grid_main.add(Date_title, 0, 8);
      
        Label Colon3=new Label(":");//將":"固定在一個位置
        GridPane.setHalignment(Colon3, HPos.LEFT);
        grid_main.add(Colon3, 1, 8); 
        Colon3.setId("Colon_Label");
        //Colon1.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)    
        
        /** 日期選擇器 **/
        checkInDatePicker = new DatePicker();
        /** Default = "mm/dd/yyyy" 變更為 pattern = "yyyy-MM-dd" -> format 日期 顯示格式 **/
        StringConverter converter = new StringConverter<LocalDate>() {
           DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
           @Override
           public String toString(LocalDate date) {
               if (date != null) {
                   return dateFormatter.format(date);
               } else {
                   return "";
               }
           }

           @Override
           public LocalDate fromString(String string) {
               if (string != null && !string.isEmpty()) {
                   return LocalDate.parse(string, dateFormatter);
               } else {
                   return null;
               }
           }
       };            

        checkInDatePicker.setConverter(converter);
        checkInDatePicker.setPromptText(pattern.toLowerCase());
        checkInDatePicker.setValue(LocalDate.parse(CurrentDate)); //CurrentDate ; LocalDate.now()
        checkInDatePicker.setId("DatePicker");
        checkInDatePicker.setOnAction((ActionEvent event) -> {

            LocalDate date = checkInDatePicker.getValue(); // 取得所選擇的日期
            Date_selectItem = date.toString();
            System.out.println(" checkInDatePicker getValue : "+ Date_selectItem +"\n");
       
        });
        grid_main.add(checkInDatePicker, 2, 8); 
       
        if( Date_selectItem == null ){
            Date_selectItem = CurrentDate;
        }
       
       
       /** 時間 **/
        Label Time_title = new Label(WordMap.get("TimeSettings_time"));
        GridPane.setHalignment(Time_title, HPos.LEFT);      
	grid_main.add(Time_title, 0, 10);
        //Time_title.setPadding(new Insets(0, 0, 0, 20));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)  
        
        Label Colon4=new Label(":");//將":"固定在一個位置
        GridPane.setHalignment(Colon4, HPos.LEFT);
        grid_main.add(Colon4, 1, 10); 
        Colon4.setId("Colon_Label");
        //Colon1.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)   
        
        /** 日期和時間 Title 位置 **/
        if("English".equals(WordMap.get("SelectedLanguage"))){
            Date_title.setPadding(new Insets(0, 0, 0, 60));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)  
            Time_title.setPadding(new Insets(0, 0, 0, 60));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)  
            
        }
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
            Date_title.setPadding(new Insets(0, 0, 0, 50));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)  
            Time_title.setPadding(new Insets(0, 0, 0, 50));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)  
        }
        
        
        /** Hour to ComboBox **/   
        String stringValue_Hour;
        String stringValue_Min;
        String stringValue_Sec;
          /** 判斷 正確時間 若數字 <= 9  則在前面加 00 ~ 09 **/      
        for( int h = 0 ; h<=23 ; h++ ){
            if(h <= 9){
                //String stringValue = Integer.toString(h);
                stringValue_Hour="0"+h;

            }else{
                stringValue_Hour= Integer.toString(h);
            }
            Hour_list.add(stringValue_Hour);
        }
        /** Minute to ComboBox **/
        /** 判斷 正確時間 若數字 <= 9  則在前面加 00 ~ 09 **/
        for( int m = 0 ; m<=60 ; m++ ){
            if(m <= 9){               
                stringValue_Min="0"+m;

            }else{
                stringValue_Min= Integer.toString(m);
            }
            Min_list.add(stringValue_Min);
        }
        /** Second to ComboBox **/
        /** 判斷 正確時間 若數字 <= 9  則在前面加 00 ~ 09 **/
        for( int s = 0 ; s<=60 ; s++ ){
            if(s <= 9){               
                stringValue_Sec="0"+s;

            }else{
                stringValue_Sec= Integer.toString(s);
            }
            Sec_list.add(stringValue_Sec);
        }
        
        /****** Time時間的選擇 " Hour " (ComboBox) ******/        
        Hour_Selection=new ComboBox(Hour_list);                            
        Hour_Selection.getSelectionModel().select(CurrentTime_Hour);      
//        Hour_Selection.setPrefWidth(250);
//        Hour_Selection.setMaxWidth(250);
//        Hour_Selection.setMinWidth(250);       
        /***  以下抓取變更 TimeZone 時區 後的動作  ***/
        Hour_Selection.setOnAction((Event event) -> {

            Hour_selectItem = Hour_Selection.getValue().toString();           
            System.out.println(" Hour_selectItem : "+ Hour_selectItem +"\n");
            
        });       
        //grid_main.add(TimeZoneSelection,2,8);
        
        if( Hour_selectItem == null ){
            Hour_selectItem = CurrentTime_Hour;
        }

        Label Colon5=new Label(":");//將":"固定在一個位置
        GridPane.setHalignment(Colon5, HPos.LEFT);
        Colon5.setId("Colon_Label");
        //grid_main.add(Colon5, 1, 8); 
        
        /****** Time時間的選擇 " Minute " (ComboBox) ******/        
        Min_Selection=new ComboBox(Min_list);           
        Min_Selection.getSelectionModel().select(CurrentTime_Min);      
//        Min_Selection.setPrefWidth(250);
//        Min_Selection.setMaxWidth(250);
//        Min_Selection.setMinWidth(250);        
        /***  以下抓取變更 TimeZone 時區 後的動作  ***/
        Min_Selection.setOnAction((Event event) -> {

            Min_selectItem = Min_Selection.getValue().toString();
            System.out.println(" Min_selectItem : "+ Min_selectItem +"\n");
            
        });       
        //grid_main.add(TimeZoneSelection,2,8);
        
        if( Min_selectItem == null ){
            Min_selectItem = CurrentTime_Min;
        }
        
        Label Colon6=new Label(":");//將":"固定在一個位置
        GridPane.setHalignment(Colon6, HPos.LEFT); 
        Colon6.setId("Colon_Label");
        //grid_main.add(Colon6, 1, 8); 
        
        /****** Time時間的選擇 " Second " (ComboBox) ******/        
        Sec_Selection=new ComboBox(Sec_list);                             
        Sec_Selection.getSelectionModel().select(CurrentTime_Sec);      
//        Sec_Selection.setPrefWidth(250);
//        Sec_Selection.setMaxWidth(250);
//        Sec_Selection.setMinWidth(250);        
        /***  以下抓取變更 TimeZone 時區 後的動作  ***/
        Sec_Selection.setOnAction((Event event) -> {

            Sec_selectItem = Sec_Selection.getValue().toString();
            System.out.println(" Sec_selectItem : "+ Sec_selectItem +"\n");
            
        });       
        //grid_main.add(TimeZoneSelection,2,8);
        
        if( Sec_selectItem == null ){
            Sec_selectItem = CurrentTime_Sec;
        }
        
        HBox TimeHBox=new HBox();
        //TimeHBox.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        TimeHBox.getChildren().addAll(Hour_Selection, Colon5, Min_Selection, Colon6, Sec_Selection);  
        TimeHBox.setAlignment(Pos.CENTER_LEFT);
        TimeHBox.setSpacing(10); 
        grid_main.add(TimeHBox, 2,10);
        
        /** 伺服器位置 label **/
        Label Server_title = new Label(WordMap.get("TimeSettings_ServerAddress"));
        GridPane.setHalignment(Server_title, HPos.LEFT);      
	grid_main.add(Server_title, 0, 14,3,1);
        //Server_title.setPadding(new Insets(0, 0, 0, 30));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        
        Label Colon7=new Label(":");//將":"固定在一個位置
        GridPane.setHalignment(Colon7, HPos.LEFT); 
        Colon7.setId("Colon_Label");
        //grid_main.add(Colon7, 1, 14);
        
        /******  伺服器 ntp (ComboBox) ******/ 
        ObservableList<String> ServerList=FXCollections.observableArrayList("pool.ntp.org");
        Server_Selection=new ComboBox(ServerList);  
        Server_Selection.getSelectionModel().select("pool.ntp.org");  
        Server_Selection.setPrefWidth(300);
        Server_Selection.setMaxWidth(300);
        Server_Selection.setMinWidth(300);  
        
        /***  以下抓取變更 伺服器 後的動作  ***/
//        Server_Selection.setOnAction((Event event) -> {
//
//            String Server_selectItem = Server_Selection.getValue().toString();
//            System.out.println(" Server_selectItem : "+ Server_selectItem +"\n");
//            
//        });      
//        grid_main.add(Server_Selection,2,14);
        
        HBox serverHBox=new HBox();       
        serverHBox.getChildren().addAll(Colon7, Server_Selection);  
        serverHBox.setAlignment(Pos.CENTER_LEFT);
        serverHBox.setSpacing(10); 
        grid_main.add(serverHBox, 2,14);
        
        /** 伺服器 Title 位置 **/
        if("English".equals(WordMap.get("SelectedLanguage"))){
            Server_title.setPadding(new Insets(0, 0, 0, 40));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
            serverHBox.setTranslateX(40);            
        }
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
            Server_title.setPadding(new Insets(0, 0, 0, 50));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
            serverHBox.setTranslateX(40);
        }
        
         /** 立即更新 button **/
        TimeUpdate = new Button(WordMap.get("TimeSettings_update"));
//        TimeUpdate.setMaxHeight(35);
//        TimeUpdate.setMinHeight(35);
//        TimeUpdate.setPrefHeight(35);
        TimeUpdate.setTranslateX(55);
        grid_main.add(TimeUpdate,2,16,2,1);
        TimeUpdate.setOnAction((ActionEvent event) -> { 
            
            if(TimeUpdate_lock == false){
                TimeUpdate_lock=true;               
                
                Task<Void> TimeUpdateTask = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //updateChangePW();   
                        timer.cancel();
                        UpdateNowAction(); 
                        //timer.cancel();
                        UpdateDateandTime();
                        return null;
                    }
                };

                TimeUpdateTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        System.out.println("** TimeUpdateTask.setOnFailed ** \n");
                        TimeUpdate_lock=false;

                    }
                });

                TimeUpdateTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        System.out.println("** TimeUpdateTask.setOnSucceeded ** \n");                        
                        TimeUpdate_lock=false;
                    
                    }
                });
                new Thread(TimeUpdateTask).start();
             
            }

        });
        LoadLastTimeStatus(); // 2017.06.08 william Remember Time Type
        /** false = Auto ; true = Manually **/
        if(Manually_or_Auto == false){            
            checkInDatePicker.setDisable(true);
            Hour_Selection.setDisable(true);
            Min_Selection.setDisable(true);
            Sec_Selection.setDisable(true);
            TimeUpdate.setDisable(false);
            Server_Selection.setDisable(false); // 2017.06.08 william Remember Time Type
            group.selectToggle(rb2_AutoSync);   // 2017.06.08 william Remember Time Type
        }else{
            checkInDatePicker.setDisable(false);
            Hour_Selection.setDisable(false);
            Min_Selection.setDisable(false);
            Sec_Selection.setDisable(false);
            TimeUpdate.setDisable(true);
            Server_Selection.setDisable(true); // 2017.06.08 william Remember Time Type
            group.selectToggle(rb1_Manually);  // 2017.06.08 william Remember Time Type
        }

        
        /** RadioButton Selected Action **/
        group.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
            if (group.getSelectedToggle() == rb1_Manually) { 
               
                checkInDatePicker.setDisable(false);
                Hour_Selection.setDisable(false);
                Min_Selection.setDisable(false);
                Sec_Selection.setDisable(false);
                TimeUpdate.setDisable(true);
                Server_Selection.setDisable(true); // 2017.06.08 william Remember Time Type
            }else{               
                
                checkInDatePicker.setDisable(true);
                Hour_Selection.setDisable(true);
                Min_Selection.setDisable(true);
                Sec_Selection.setDisable(true);
                TimeUpdate.setDisable(false);
                Server_Selection.setDisable(false); // 2017.06.08 william Remember Time Type
            }
        });
        /*****離開*****/
        Leave=new Button(WordMap.get("TC_Exit"));
        Leave.setPrefSize(90,30);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        Leave.setMaxSize(90,30);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        Leave.setMinSize(90,30);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動           
        Leave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {   
                
                if(Manually_or_Auto == false){                   
                
                    rb2_AutoSync.setSelected(true);
                    rb2_AutoSync.requestFocus();
                }
                if(Manually_or_Auto == true){                

                    rb1_Manually.setSelected(true);
                    rb1_Manually.requestFocus();
                }
                TimeZoneSelection.getSelectionModel().select(TimeZone_CurrentZoneName);
                checkInDatePicker.setValue(LocalDate.now());
                update_lock = true; // 2017.06.14 william  UpdateDateandTime() lock use
                TimeSetting_stage.close();
            }
        }); 
        
        /*****取消*****/
        Cencel=new Button(WordMap.get("TC_Cancel"));
        
        if("Japanese".equals(WordMap.get("SelectedLanguage"))){
            Cencel.setPrefSize(110,30);
            Cencel.setMaxSize(110,30);
            Cencel.setMinSize(110,30);
        } else {
            Cencel.setPrefSize(90,30);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
            Cencel.setMaxSize(90,30);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
            Cencel.setMinSize(90,30);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動                   
        }                  

        Cencel.setOnAction((event) -> {
            
            if(Cencel_lock == false){
                Cencel_lock=true;
            
                if(Manually_or_Auto == false){                   

                    rb2_AutoSync.setSelected(true);
                    rb2_AutoSync.requestFocus();
                }
                if(Manually_or_Auto == true){                

                    rb1_Manually.setSelected(true);
                    rb1_Manually.requestFocus();
                }
                TimeZoneSelection.getSelectionModel().select(TimeZone_CurrentZoneName);
                checkInDatePicker.setValue(LocalDate.now());
                
                Cencel_lock=false;
            }
            
        });
        /*****確定*****/
        Confirm=new Button(WordMap.get("TC_Confirm"));
        Confirm.setPrefSize(90,30);//強制限制button預設寬度/高度,讓每個button在變換語言時不會變動
        Confirm.setMaxSize(90,30);//強制限制button最大寬度/高度,讓每個button在變換語言時不會變動
        Confirm.setMinSize(90,30);//強制限制button最小寬度/高度,讓每個button在變換語言時不會變動           
        Confirm.setOnAction((ActionEvent event) -> {   
            
            if( Confirm_lock == false ){
                
                Confirm_lock=true;
//                try {
//                    ComfirmAction();
//                } catch (IOException ex) {
//                    Logger.getLogger(TimeSetting.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                Confirm_lock = false;
                Task<Void> ConfirmTask = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //updateChangePW();                
                        ComfirmAction();
                        
                        return null;
                    }
                };

                ConfirmTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        //System.out.println("** TimeUpdateTask.setOnFailed ** \n");
                        TimeSetting_stage.close();
                        Confirm_lock = false;
                        
                        update_lock = true; // 2017.06.14 william  UpdateDateandTime() lock use
                    }
                });

                ConfirmTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        //System.out.println("** TimeUpdateTask.setOnSucceeded ** \n");
                        TimeSetting_stage.close();
                        Confirm_lock = false;

                        update_lock = true; // 2017.06.14 william  UpdateDateandTime() lock use
                    }
                });
                new Thread(ConfirmTask).start();


            }
            
            
        });
        
        /*****關閉視窗"X"-會與離開按鍵一樣 可以關閉全部視窗 *****/ // 2017.06.14 william add a "x" to close time dialog
        TimeSetting_stage.setOnCloseRequest(new EventHandler<WindowEvent>() { // 2017.06.13 william add a "x" to close time dialog
            @Override
            public void handle(WindowEvent we) {   
                TimeSetting_stage.close();
                update_lock = true; // 2017.06.14 william  UpdateDateandTime() lock use
            }
        }); 
        
        
        HBox Operation=new HBox();
        //Operation.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        Operation.getChildren().addAll(Leave, Cencel, Confirm);  
        Operation.setAlignment(Pos.CENTER_RIGHT);
        Operation.setSpacing(20); 
        
        anchor_control.getChildren().addAll(Operation); // 將功能Button 放置AnchorPane中       
        AnchorPane.setBottomAnchor(Operation,30.0);  //設置邊距
        AnchorPane.setRightAnchor(Operation,20.0);
        
       
        
        /***  BorderPane 中,放各Pane的位置  ***/      
        TS_root.setTop(TSTitle_Box);
        TS_root.setCenter(grid_main);
        TS_root.setBottom(anchor_control);
        
        ChangeLabelLang(TSTitle, CurrentTime_title, New_CurrentTime, TimeSetting_title, TimeZone_title, Date_title, Time_title, Server_title);
        ChangeButtonLang(TimeUpdate, Cencel, Confirm, rb1_Manually, rb2_AutoSync);
        
        TS_scene = new Scene(TS_root); // 400 300
        TS_scene.getStylesheets().add("TimeCalendar.css"); // 2017.06.20 william Time setting Button focused & active
        
        Bounds mainBounds = primaryStage.getScene().getRoot().getLayoutBounds();
        Bounds rootBounds = TS_scene.getRoot().getLayoutBounds();
        TimeSetting_stage.setX(primaryStage.getX() + (mainBounds.getWidth() - 700 ) / 2);
        TimeSetting_stage.setY(primaryStage.getY() + (mainBounds.getHeight() - 540 ) / 2);        
        
        switch (GB.JavaVersion) {
            case 0:
                TimeSetting_stage.getIcons().add(new Image("images/Icon2.png"));
                break;
            case 1:
                TimeSetting_stage.getIcons().add(new Image("images/MCIcon2.png"));
                break;                
            default:
                break;
        }          
        
        TimeSetting_stage.setScene(TS_scene);
        TimeSetting_stage.initStyle(StageStyle.DECORATED);
        //TimeSetting_stage.initModality(Modality.NONE);
        TimeSetting_stage.initOwner(primaryStage);
        TimeSetting_stage.setResizable(false);//將視窗放大,關閉
        
        // TimeSetting_stage.show(); // 2017.06.14 william  Disable click many times will show mutil Dialog 
        TimeSetting_stage.initModality(Modality.APPLICATION_MODAL); // 2017.06.14 william  Disable click many times will show mutil Dialog
        TimeSetting_stage.showAndWait(); // 2017.06.14 william  Disable click many times will show mutil Dialog
        
        //System.out.println( " x*y = "+TS_scene.getWidth()+" * "+ TS_scene.getHeight() +"\n");
        
        //ClearSystemGarbage(); // 清除系統垃圾
        
        
    }
    
    /*******讀取 Version ********/
    public void LoadTimeZone() throws ParseException{
        try{
            File myFile=new File("jsonfile/Time_Zone.json");
            if(myFile.exists()){

                String FilePath=myFile.getPath();
                List<String> JSONLINE=Files.readAllLines(Paths.get(FilePath));
                String JSONCode="";
                JSONCode = JSONLINE.stream().map((s) -> s).reduce(JSONCode, String::concat);
                // 參考 http://stackoverflow.com/questions/7463414/what-s-the-best-way-to-load-a-jsonobject-from-a-json-text-file
                //System.out.print(JSONCode+"\n");
                
//                zoneidDefault = ZoneId.systemDefault();//系統預設時區
//                String DefaultZoneId ;
//                DefaultZoneId = zoneidDefault.toString();
//                //ZoneId zoneidPlus8 = ZoneId.of("UTC+8"); //UTC時間+8
//                System.out.printf("zoneidDefault :: "+zoneidDefault+"\n");
//                //System.out.printf("------zoneidPlus8-------"+zoneidPlus8+"\n");
                
                JsonParser jsonParser = new JsonParser();
                JsonObject TimeZoneObject = (JsonObject)jsonParser.parse(JSONCode).getAsJsonObject();
                JsonArray TimeZoneArr = TimeZoneObject.getAsJsonObject().getAsJsonArray("Zone");  
                
                for (int i = 0; i < TimeZoneArr.size(); i++) {
                    JsonObject Sterm = TimeZoneArr.get(i).getAsJsonObject();
                    zoneId = Sterm.get("zoneId").getAsString();                    
                    zoneName = Sterm.get("zoneName").getAsString();
                    TimeZone_list.add(zoneName) ;
                    
                    if( DefaultZoneId.equals(zoneId)){
                        
                        TimeZone_CurrentZoneName= zoneName;
                        System.out.println(" zoneName : "+ zoneName +"\n");
                    }
                    if(TimeZone_selectItem!=null && TimeZone_selectItem.equals(zoneName)){
                        
                        TimeZone_selectItem_zoneId = zoneId;
                    
                    }
                    
                }                               
                
            }else{
                //Version_num="";
                
            }
        }catch(IOException e){
           // e.printStackTrace();
        }
    }
    
    /** get Time **/
    private void GetCurrentDateandTime() throws IOException {
        
        CurrentDT_process = Runtime.getRuntime().exec( "timedatectl" );//./date.sh set tz Asia/Tokyo
        try (BufferedReader output_CurrentDT = new BufferedReader (new InputStreamReader(CurrentDT_process.getInputStream()))) {
            String line_CurrentDT = output_CurrentDT.readLine();
            //System.out.println(" line_CurrentDT  : " + line_CurrentDT +"\n");
            while(line_CurrentDT != null){  

                if ( line_CurrentDT.contains("Local time") ==true ){

                    String[] CurrentDT_Line_list =line_CurrentDT.split(" ");
                    CurrentDate = CurrentDT_Line_list[9];              
                    CurrentTime = CurrentDT_Line_list[10]; 

                    break;
                }
                line_CurrentDT = output_CurrentDT.readLine();
                //System.out.println(" line_Zoneid  : " + line_Zoneid +"\n");
            }
            output_CurrentDT.close();
        }

//        CurrentDT_process = Runtime.getRuntime().exec( "date" );//./date.sh set tz Asia/Tokyo
//        try (BufferedReader output_CurrentDT = new BufferedReader (new InputStreamReader(CurrentDT_process.getInputStream()))) {
//            String line_CurrentDT = output_CurrentDT.readLine();
//            //System.out.println(" line_CurrentTime  : " + line_CurrentTime +"\n");
//            String[] CurrentDT_Line_list =line_CurrentDT.split(" ");
//            /** get 年/月/日 **/
//            CurrentDate_Mounth_Eng = CurrentDT_Line_list[1];                
//            CurrentDate_Day = CurrentDT_Line_list[2];    
//            //若 Day為個位數 則 CurrentDT_Line_list[2] 抓的是" "(空格) , 因此若為個位數 將另外抓取 MainCurrentDate_Day; MainCurrentDate_Year ; MainCurrentTime
//            if(CurrentDate_Day.isEmpty()){
//
//                CurrentDate_Year = CurrentDT_Line_list[6];
//                CurrentDate_Day = CurrentDT_Line_list[3];
//                //System.out.println(" MainCurrentDate_Day =null  : " + CurrentDate_Day +"\n");
//                CurrentTime = CurrentDT_Line_list[4];  
//                //System.out.println(" MainCurrentTime  : " + CurrentTime +"\n");
//
//            }else{
//
//                CurrentDate_Year = CurrentDT_Line_list[5];
//                CurrentTime = CurrentDT_Line_list[3]; 
//                //System.out.println(" MainCurrentTime  : " + CurrentTime +"\n");
//
//            }
//            
//            //若 Day為個位數 則將 Day前面加 "0" -> 變成雙位數
//            int intV = Integer.valueOf(CurrentDate_Day); // int to String
//            if(intV <= 9){
//                CurrentDate_Day = "0"+intV;
//            }
//            output_CurrentDT.close();
//        }

//        /** get 月 -> Number **/
//        if( CurrentDate_Mounth_Eng.equals("Jan") ){
//            CurrentDate_Mounth = "01";
//        }
//        if( CurrentDate_Mounth_Eng.equals("Feb") ){
//            CurrentDate_Mounth = "02";
//        }
//        if( CurrentDate_Mounth_Eng.equals("Mar") ){
//            CurrentDate_Mounth = "03";
//        }
//        if( CurrentDate_Mounth_Eng.equals("Apr") ){
//            CurrentDate_Mounth = "04";
//        }
//        if( CurrentDate_Mounth_Eng.equals("May") ){
//            CurrentDate_Mounth = "05";
//        }
//        if( CurrentDate_Mounth_Eng.equals("Jun") ){
//            CurrentDate_Mounth = "06";
//        }
//        if( CurrentDate_Mounth_Eng.equals("Jul") ){
//            CurrentDate_Mounth = "07";
//        }
//        if( CurrentDate_Mounth_Eng.equals("Aug") ){
//            CurrentDate_Mounth = "08";
//        }
//        if( CurrentDate_Mounth_Eng.equals("Sep") ){
//            CurrentDate_Mounth = "09";
//        }
//        if( CurrentDate_Mounth_Eng.equals("Oct") ){
//            CurrentDate_Mounth = "10";
//        }
//        if( CurrentDate_Mounth_Eng.equals("Nov") ){
//            CurrentDate_Mounth = "11";
//        }
//        if( CurrentDate_Mounth_Eng.equals("Dec") ){
//            CurrentDate_Mounth = "12";
//        }
//        
//        CurrentDate = CurrentDate_Year + "-" + CurrentDate_Mounth + "-" + CurrentDate_Day ;
        
        /** get 年/月/日 **/
        String[] Date_Line_list =CurrentDate.split("-");
        CurrentDate_Year = Date_Line_list[0];
        CurrentDate_Mounth = Date_Line_list[1];
        CurrentDate_Day = Date_Line_list[2];
        
        /** get 時/分/秒 **/
        String[] Time_Line_list = CurrentTime.split(":");
        CurrentTime_Hour = Time_Line_list[0];
        CurrentTime_Min = Time_Line_list[1];
        CurrentTime_Sec = Time_Line_list[2];
        
    }
    
    /** get zone ID **/
    private void GetSystemZoneID() throws IOException {

        Zoneid_process = Runtime.getRuntime().exec( "timedatectl" );//./date.sh set tz Asia/Tokyo
        try (BufferedReader output_Zoneid = new BufferedReader (new InputStreamReader(Zoneid_process.getInputStream()))) {
            String line_Zoneid = output_Zoneid.readLine();
            //System.out.println(" line_Zoneid  : " + line_Zoneid +"\n");
            while(line_Zoneid != null){  

                if ( line_Zoneid.contains("Timezone") ==true ){

                    String[] Zoneid_Line_list =line_Zoneid.split(" ");
                    DefaultZoneId = Zoneid_Line_list[9];              
                    //System.out.println(" DefaultZoneId  : " + DefaultZoneId +"\n");

                    break;
                }
                line_Zoneid = output_Zoneid.readLine();
                //System.out.println(" line_Zoneid  : " + line_Zoneid +"\n");
            }
            output_Zoneid.close();
        }
        
    }
    
    private void ComfirmAction() throws IOException {
        
        if( TimeZone_selectItem != null && !TimeZone_selectItem.equals(TimeZone_CurrentZoneName) ){            
            /*** 變更時區 action ***/           
            String TimeZone_str;
            TimeZone_str= "/root/date.sh set tz " + TimeZone_selectItem_zoneId;
            Process TimeZone_process = Runtime.getRuntime().exec( TimeZone_str );//./date.sh set tz Asia/Tokyo
            try (BufferedReader output_TimeZone = new BufferedReader (new InputStreamReader(TimeZone_process.getInputStream()))) {
                String line_TimeZone = output_TimeZone.readLine();
                //System.out.println(" line_TimeZone  : " + line_TimeZone +"\n");
                while(line_TimeZone != null){  

                    if ( line_TimeZone.startsWith("timedatectl") ==true ){

                        String[] TimeZone_Line_list =line_TimeZone.split(" ");
                        TimeZone_outputID = TimeZone_Line_list[2];              
                        //System.out.println(" TimeZone_outputID  : " + TimeZone_outputID +"\n");
                        
                        break;
                    }
                }
                output_TimeZone.close();
            }
            if(TimeZone_outputID.equals(TimeZone_selectItem_zoneId)){
                System.out.println(" ** Success change Time Zone **  \n");
                GetSystemZoneID();           

            }else{
                System.out.println(" ** Faild change Time Zone **  \n");
            }
        
        }
        
        System.out.println(" group.getSelectedToggle()  : " + group.getSelectedToggle() +"\n");
            
            if( group.getSelectedToggle()== rb1_Manually ){
                System.out.println(" ** rb1_Manually **   \n");
                
                /*** 手動 設定時間 action ***/      
                //此時若要手動校時就要先將 ntp 關閉：sudo timedatectl set-ntp no
                String SetDateandTime_str_no ;
                SetDateandTime_str_no = NTP_Stop; // 2017.06.16 william timedatectl set-ntp no -> service ntp stop Time setting fix            
                System.out.println(" SetDateandTime_str_no : " + SetDateandTime_str_no +"\n");
                try {
                    Process SetDateandTime_process = Runtime.getRuntime().exec( SetDateandTime_str_no );
                } catch (IOException ex) {
                    Logger.getLogger(TimeSetting.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                String SetDateandTime_str ; //./date.sh set time 03:56
                SetDateandTime_str = "/root/date.sh set time " + Date_selectItem + " " + Hour_selectItem+ ":" +Min_selectItem + ":" + Sec_selectItem  ;
                System.out.println(" SetDateandTime_str  : " + SetDateandTime_str +"\n");
                
                try {
                    Manually_or_Auto = false;    // 2017.06.08 william Remember Time Type
                    WriteLastTimeStatusChange(); // 2017.06.08 william Remember Time Type
                    Process SetDateandTime_process = Runtime.getRuntime().exec( SetDateandTime_str );//timedatectl set-time "2016-11-12 18:10:40"
                    
                } catch (IOException ex) {
                    Logger.getLogger(TimeSetting.class.getName()).log(Level.SEVERE, null, ex);
                }


            }

            if( group.getSelectedToggle()== rb2_AutoSync ){  
                System.out.println(" ** rb2_AutoSync **   \n");
                /*** 自動同步 設定時間 action ***/                                 
                //若要恢復 ntp 自動校時，則執行 sudo timedatectl set-ntp yes
                String SetDateandTime_str ;
                String NTPDateandTime_str ;         // 2017.06.16 william Time setting fix   
                SetDateandTime_str = NTP_Stop;      // 2017.06.16 william timedatectl set-ntp yes -> ntpdate -s pool.ntp.org  自動同步時間 Time setting fix   
                NTPDateandTime_str = NTPDate_Check; // 2017.06.16 william Time setting fix               
                System.out.println(" SetDateandTime_str : " + SetDateandTime_str +"\n");
                Process SetDateandTime_process = Runtime.getRuntime().exec( SetDateandTime_str ); // 2017.06.16 william Time setting fix   
                try {
                    Manually_or_Auto = true;     // 2017.06.08 william Remember Time Type
                    WriteLastTimeStatusChange(); // 2017.06.08 william Remember Time Type
                    // 2017.06.16 william Time setting fix   
                    Process SetNTP_process = Runtime.getRuntime().exec( NTPDateandTime_str );
//                    SetNTP_process.waitFor();
//                    Auto_Time_ReturnCode = SetNTP_process.exitValue();
//                    System.out.println(" Return Code : " + Auto_Time_ReturnCode +"\n");
//                    if(Auto_Time_ReturnCode == 0) {
                        SetDateandTime_str = NTP_Start;
                        SetDateandTime_process = Runtime.getRuntime().exec( SetDateandTime_str );
//                        SetDateandTime_process.waitFor(); // 2017.06.16 william  自動同步時間 Time setting fix  
//                    }
//                    else {
//                        System.out.println(" Auto Set Date and Time fail \n"); // Auto_Time_ReturnCode == 1
//                    }
                } catch (IOException ex) {
                    Logger.getLogger(TimeSetting.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
      
    }
    
    private void UpdateNowAction() throws IOException, InterruptedException {

        if( TimeZone_selectItem != null && !TimeZone_selectItem.equals(TimeZone_CurrentZoneName) ){            
            /*** 變更時區 action ***/           
            String TimeZone_str;
            TimeZone_str= "/root/date.sh set tz " + TimeZone_selectItem_zoneId;
            Process TimeZone_process = Runtime.getRuntime().exec( TimeZone_str );//./date.sh set tz Asia/Tokyo
            try (BufferedReader output_TimeZone = new BufferedReader (new InputStreamReader(TimeZone_process.getInputStream()))) {
                String line_TimeZone = output_TimeZone.readLine();
                //System.out.println(" line_TimeZone  : " + line_TimeZone +"\n");
                while(line_TimeZone != null){  

                    if ( line_TimeZone.startsWith("timedatectl") ==true ){

                        String[] TimeZone_Line_list =line_TimeZone.split(" ");
                        TimeZone_outputID = TimeZone_Line_list[2];              
                        //System.out.println(" TimeZone_outputID  : " + TimeZone_outputID +"\n");
                        
                        break;
                    }
                }
                output_TimeZone.close();
            }
            
            if(TimeZone_outputID.equals(TimeZone_selectItem_zoneId)){
                System.out.println(" ** Success change Time Zone **  \n");
                GetSystemZoneID();           

            }else{
                System.out.println(" ** Faild change Time Zone **  \n");
            }
        
        }
        
        /*** 自動同步 設定時間 action ***/              
        GB.lock_AutoTimeRenew = true; // 2017.06.19 william Auto updata time action lock
        //若要恢復 ntp 自動校時，則執行 sudo timedatectl set-ntp yes
        String SetDateandTime_str ;
        String NTPDateandTime_str ;         // 2017.06.16 william Time setting fix   
        SetDateandTime_str = NTP_Stop;      // 2017.06.16 william timedatectl set-ntp yes -> ntpdate -s pool.ntp.org  自動同步時間 Time setting fix   
        NTPDateandTime_str = NTPDate_Check; // 2017.06.16 william Time setting fix   
        System.out.println(" SetDateandTime_str : " + SetDateandTime_str +"\n");
        Process SetDateandTime_process = Runtime.getRuntime().exec( SetDateandTime_str ); // 2017.06.16 william Time setting fix   
        try { // 2017.06.16 william Time setting fix   
            Process SetNTP_process = Runtime.getRuntime().exec( NTPDateandTime_str );
            SetNTP_process.waitFor();
            Auto_Time_ReturnCode = SetNTP_process.exitValue();
            System.out.println(" Return Code : " + Auto_Time_ReturnCode +"\n");
            if(Auto_Time_ReturnCode == 0) {
                SetDateandTime_str = NTP_Start;
                SetDateandTime_process = Runtime.getRuntime().exec( SetDateandTime_str );
                SetDateandTime_process.waitFor(); // 2017.06.16 william  自動同步時間 Time setting fix  
                
                GB.lock_AutoTimeRenew = false; // 2017.06.19 william Auto updata time action lock
            }
            else {
                System.out.println(" Auto Set Date and Time fail \n"); // Auto_Time_ReturnCode == 1
                
                GB.lock_AutoTimeRenew = false; // 2017.06.19 william Auto updata time action lock
            }
 
           
        } catch (IOException ex) {
            Logger.getLogger(TimeSetting.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private void UpdateDateandTime() {
        timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {     
                if(!update_lock){ // 2017.06.14 william  UpdateDateandTime() lock use
                  //CurrentTime_now = LocalDate.now() +"   "+ LocalTime.now().getHour() + " : " + LocalTime.now().getMinute() + " : " + LocalTime.now().getSecond() ;
//                String date_str = ZonedDateTime.now().getYear() + " - " + ZonedDateTime.now().getMonthValue() + " - " + ZonedDateTime.now().getDayOfMonth() ;
//                String time_str = ZonedDateTime.now().getHour() + " : " + ZonedDateTime.now().getMinute() + " : " + ZonedDateTime.now().getSecond() ;    
//                //String date_str = LocalDate.now().getYear() + " - " + LocalDate.now().getMonthValue() + " - " + LocalDate.now().getDayOfMonth() ;
//                //String time_str = LocalTime.now().getHour() + " : " + LocalTime.now().getMinute() + " : " + LocalTime.now().getSecond() ;
//                New_CurrentTime = date_str +"   "+ time_str ;
                    if(!GB.lock_AutoTimeRenew){ // 2017.06.19 william Auto updata time action lock
                        try {
                    
                            GetCurrentDateandTime();
                            date_str = CurrentDate_Year + " - " + CurrentDate_Mounth + " - " + CurrentDate_Day ;
                            time_str = CurrentTime_Hour + " : " + CurrentTime_Min + " : " + CurrentTime_Sec ;  
                            CurrentTime_now = date_str +"   "+ time_str ;
//                    System.out.println(" date_str  : " + date_str +"\n");
//                    System.out.println(" time_str  : " + time_str +"\n");
                    
                        } 
                        catch (IOException ex) {
                            Logger.getLogger(TimeSetting.class.getName()).log(Level.SEVERE, null, ex);
                        }
       
                        Platform.runLater(() -> {
                    
                            New_CurrentTime.setText( CurrentTime_now );
                        });
                    }
                }
            }
        };
        timer.schedule(task, 1, 1000);
        
        
    }
    
    // 2017.06.08 william Remember Time Type
    public void LoadLastTimeStatus() throws ParseException {
        try {
            File myFile=new File("jsonfile/TimeStatus.json");

            if(myFile.exists()) {
                String FilePath = myFile.getPath();
                List<String> JSONLINE = Files.readAllLines(Paths.get(FilePath));
                String JSONCode = "";
                JSONCode = JSONLINE.stream().map((s) -> s).reduce(JSONCode, String::concat);
               
                JSONParser Jparser = new JSONParser();
                TimeStatus = (JSONObject) Jparser.parse(JSONCode);                
                
                Manually_or_Auto = TimeStatus.get("Manually_or_Auto").equals(Manually_or_Auto);  
                
                if(Manually_or_Auto) {
                    System.out.print("Read Manually_or_Auto=true");
                }
                else {
                    System.out.print("Read Manually_or_Auto=false");
                }
               
            }
            else {
                Manually_or_Auto = false;
            }
        }
        catch(Exception e) { // 2017.11.09 william Json檔案讀寫失敗或錯誤的處理，將IOException 改為通用型例外 Exception
            System.out.print("StaticIPStatus.json read failed \n");
           // e.printStackTrace();
        }
    }
    // 2017.06.08 william Remember Time Type        
    public void WriteLastTimeStatusChange() {
        JSONObject TimeStatus=new JSONObject();
        TimeStatus.put("Manually_or_Auto", Manually_or_Auto); 
        
        if(Manually_or_Auto) {
            System.out.print("Manually_or_Auto=true");
        }
        else {
            System.out.print("Manually_or_Auto=false");
        }
        
        try {
            File myFile=new File("jsonfile/TimeStatus.json");
            if(myFile.exists()) {
                myFile.delete();
                myFile=null;
            }
            try(FileWriter JsonWriter = new FileWriter("jsonfile/TimeStatus.json")) {
                JsonWriter.write(TimeStatus.toJSONString());
                JsonWriter.flush();
                JsonWriter.close();
            }
            
        }
        catch(IOException e) {
           // e.printStackTrace();
        }
    }
    
    
    /****變更Label字型****字型:中文-標楷體,英文:Times New Roman****/
    private void ChangeLabelLang(Label title, Label lab01, Label lab02, Label lab03, Label lab04, Label lab05, Label lab06, Label lab07){
        //Label "title"
        if("English".equals(WordMap.get("SelectedLanguage"))){
            
            title.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 17pt;  ");  //Ubuntu; Liberation Serif;
            
            lab01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 14pt;  ");
            lab02.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 14pt;  ");
            lab03.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 14pt;  ");
            lab04.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 14pt;  ");
            lab05.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 14pt;  ");
            lab06.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 14pt;  ");
            lab07.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 14pt;  ");
                      
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
              
            title.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 17pt;  ");
            
            lab01.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 14pt;  ");
            lab02.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 14pt;  ");
            lab03.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 14pt;  ");
            lab04.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 14pt;  ");
            lab05.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 14pt;  ");
            lab06.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 14pt;  ");
            lab07.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 14pt;  ");
            
        }
       
    }

    /****變更button字型****字型:中文-標楷體,英文:Times New Roman****/
    private void ChangeButtonLang(Button but01, Button but02, Button but03,RadioButton rbut01, RadioButton rbut02){
        //Button "確定" "取消"
        if("English".equals(WordMap.get("SelectedLanguage"))){
            
            but01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;  ");        
            but02.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;  ");        
            but03.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;  ");        
            rbut01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 14pt; ");   
            rbut02.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 14pt; "); 
          
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
            
            but01.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;  ");
            but02.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;  ");
            but03.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;  ");
            rbut01.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 14pt; ");
            rbut02.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900; -fx-text-fill: white; -fx-font-size: 14pt; ");
          
        }
        
    }

    /** 清除 系統 垃圾 **/
    private void ClearSystemGarbage(){
        Timer timer_clear=new Timer(true);
        TimerTask task_clear=new TimerTask(){
            @Override
            public void run() {       
                Zoneid_process = null;
                CurrentDT_process = null;
                CurrentDate = null;
                CurrentTime = null;
                CurrentDate_Year = null;
                CurrentDate_Mounth = null;
                CurrentDate_Day = null;
                CurrentTime_Hour = null;
                CurrentTime_Min = null;
                CurrentTime_Sec = null;
                CurrentTime_now = null;
                
                // System.gc();//清除系統垃圾 // 2017.06.05 william System.gc disable
                System.out.println(" System.gc() \n");
            }
        };
            timer_clear.schedule(task_clear, 5000, 20000); 
        
    }
    
    
    
    
}
