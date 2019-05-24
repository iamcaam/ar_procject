/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.json.simple.parser.ParseException;

/**
 *
 * @author root
 */
public class DateandTime {
    
    public Map<String, String> WordMap=new HashMap<>();
    
    private TimeSetting TS;
    
    public Stage DateandTime_stage = new Stage();
    private Button Settings; 
    private BorderPane DAT_root = new BorderPane();
    private Scene DAT_scene;
    private Label DATTitle = new Label();
    public boolean Settings_color = true;
    private boolean Settings_lock = true;
    
    public GB GB;
    
    public DateandTime(Stage primaryStage, Map<String, String> LangMap, String Date)throws MalformedURLException, IOException{ 
      
        Settings = null;
        DAT_scene = null;
        // System.gc();//清除系統垃圾 // 2017.06.05 william System.gc disable
        WordMap=LangMap;
      
        //DATTitle=new Label(WordMap.get("TimeCalendar_title"));
        DATTitle.setText(WordMap.get("TimeCalendar_title"));
        HBox DATTitle_Box=new HBox();
        DATTitle_Box.getChildren().addAll(DATTitle);
        DATTitle_Box.setAlignment(Pos.CENTER);      
//        DATTitle_Box.setMaxHeight(50);
//        DATTitle_Box.setMinHeight(50);
//        DATTitle_Box.setPrefHeight(50);
        DATTitle_Box.setId("Date_Title");
       
        /*** DatePickerSkin 日曆 : 日期為 LocalDate ***/
        DatePickerSkin datePickerSkin = new DatePickerSkin(new DatePicker(LocalDate.parse(Date)));
        Node popupContent = datePickerSkin.getPopupContent(); 
        popupContent.setId("Node");
        
        /***  將日曆放入 GridPane 內 , 並放置再 BorderPane setCenter  ***/
        GridPane grid_Date = new GridPane();
        grid_Date.setHgap(15);
        grid_Date.setVgap(15);     
        grid_Date.setPrefSize(400, 200); //680, 400
        grid_Date.setMaxSize(400, 200);
        grid_Date.setMinSize(400, 200); 
        grid_Date.setAlignment(Pos.CENTER);
        grid_Date.add(popupContent,0,0,28,1); //2,1是指這個元件要佔用2格的column和1格的row   
        
        
        /***  將 進階設定button 放入 GridPane 內 , 並放置再 BorderPane setBottom  ***/
        Settings = new Button(WordMap.get("TimeCalendar_Settings")); 

        GridPane grid_Settings = new GridPane();
        grid_Settings.setHgap(15);
        grid_Settings.setVgap(15);
        grid_Settings.setPrefSize(400, 65); //680, 400
        grid_Settings.setMaxSize(400, 65);
        grid_Settings.setMinSize(400, 65); 
        grid_Settings.setAlignment(Pos.CENTER);
        grid_Settings.setPadding(new Insets(0, 0, 5, 0)); //設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 21
        grid_Settings.add(Settings,0,0); //2,1是指這個元件要佔用2格的column和1格的row 
        
        Settings.setOnAction((ActionEvent event) -> { 
            
            if(Settings_lock == true){
                
                try {
                    TS = new TimeSetting(primaryStage, WordMap);
                } catch (IOException | ParseException ex) {
                    Logger.getLogger(DateandTime.class.getName()).log(Level.SEVERE, null, ex);
                }                
                
            }
            
          
        });
        
        /*****關閉視窗"X"-會與離開按鍵一樣 可以關閉全部視窗 *****/
        DateandTime_stage.setOnCloseRequest(new EventHandler<WindowEvent>() { // 2017.06.13 william add a "x" to close time dialog
            @Override
            public void handle(WindowEvent we) {   
                DateandTime_stage.close();
                GB.Time_change=true; // 2017.06.19 william click bug fix
            }
        });         
        
        /***  BorderPane 中,放各Pane的位置  ***/      
        DAT_root.setTop(DATTitle_Box);
        DAT_root.setCenter(grid_Date);
        DAT_root.setBottom(grid_Settings);
        
        ChangeLabelLang(DATTitle);
        ChangeButtonLang(Settings);
        
        DAT_scene = new Scene(DAT_root, 400, 360); // 400 300
        DAT_scene.getStylesheets().add("Times.css");
        
        switch (GB.JavaVersion) {
            case 0:
                DateandTime_stage.getIcons().add(new Image("images/Icon2.png"));
                break;
            case 1:
                DateandTime_stage.getIcons().add(new Image("images/MCIcon2.png")); // 2017.06.13 william add a "x" to close time dialog
                break;                
            default:
                break;
        }         
        
        DateandTime_stage.setScene(DAT_scene);
        DateandTime_stage.initStyle(StageStyle.DECORATED); // UNDECORATED -> DECORATED 2017.06.13 william add a "x" to close time dialog
        DateandTime_stage.initModality(Modality.WINDOW_MODAL); // 2017.06.20 william window modal lock NONE -> WINDOW_MODAL
        DateandTime_stage.initOwner(primaryStage);
        DateandTime_stage.setResizable(false);//將視窗放大,關閉  // 2017.06.13 william add a "x" to close time dialog
        
        Bounds mainBounds = primaryStage.getScene().getRoot().getLayoutBounds();
        Bounds rootBounds = DAT_scene.getRoot().getLayoutBounds();
        DateandTime_stage.setX(primaryStage.getX() + (mainBounds.getWidth() - 400 )); 
        DateandTime_stage.setY(primaryStage.getY() + (mainBounds.getHeight() - 450 )); // 420 -> 450 2017.06.13 william add a "x" to close time dialog

        DateandTime_stage.show();
        DAT_root.requestFocus(); // 2017.06.20 william avoid to button Leave being focus
        
    }
    
    /****變更Label字型****字型:中文-標楷體,英文:Times New Roman****/
    public void ChangeLabelLang(Label title){
        //Label "title"
        if("English".equals(WordMap.get("SelectedLanguage"))){
            
            title.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900; -fx-text-fill: white;  ");  //Ubuntu; Liberation Serif;
          
        }        
        if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
              
            title.setStyle("-fx-font-family:'Droid Sans Fallback'; -fx-font-weight: 900; -fx-text-fill: white;");

        }
       
    }

    /****變更button字型****字型:中文-標楷體,英文:Times New Roman****/
    public void ChangeButtonLang(Button but01){
        //Button "確定" "取消"
        if("English".equals(WordMap.get("SelectedLanguage"))){
            
            but01.setStyle("-fx-font-family:'Ubuntu';-fx-font-weight: 900;");            
          
        }        
        if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
            
            but01.setStyle("-fx-font-family:'Droid Sans Fallback';-fx-font-weight: 900;");
          
        }
        
    }
    
    
}
