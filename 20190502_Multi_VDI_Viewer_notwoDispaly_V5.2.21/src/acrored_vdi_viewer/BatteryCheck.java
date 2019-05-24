/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author root
 */
public class BatteryCheck {
    
    /***    顯示文字對應表    ***/
    public Map<String, String> WordMap = new HashMap<>(); 
    // power supply     
    public boolean power_supply   = false; // true: 有, false: 沒有
    // status       
    public boolean power_Charging = false; // true: 有, false: 沒有
    // capacity       
    public int power_capacity     = 0;
    int percent_int = 0;
    Label label_percent_int = new Label();
    Label label_percent     = new Label("%"); 
    Label label_BattStatus  = new Label();
    GridPane MainGP;  // BorderPane中間層    
    Button Btn_Set_Brightness = new Button();
    String Brightness_str = "";
    int Brightness_value = 0;
    int Brightness_level = 0;
    int power_capacity_value = 0;
    public GB GB;
    public AcroRed_VDI_Viewer ARVI; 
    public int setGraphic_Number  = -1;
    Timer timer_BatteryCheck;
    TimerTask task_BatteryCheck;
    // For debug 
    File debugFile;    
  
    Image Battery_No_Charging_full_img = new Image("images/BAT.png",60, 60 ,false, false);     
    Image Battery_No_Charging_3_img    = new Image("images/bat_full-1.png",60, 60 ,false, false);
    Image Battery_No_Charging_2_img    = new Image("images/bat_full-2.png",60, 60 ,false, false); 
    Image Battery_No_Charging_1_img    = new Image("images/bat_full-3.png",60, 60 ,false, false); 
    Image Battery_No_Charging_0_img    = new Image("images/bat_Empty.png",60, 60 ,false, false);
    Image Battery_Charging_full_img    = new Image("images/BAT_Full_InCharge.png", 60, 60 ,false, false);
    Image Battery_Charging_3_img       = new Image("images/BAT_Full_InCharge-1.png",60, 60 ,false, false);
    Image Battery_Charging_2_img       = new Image("images/BAT_Full_InCharge-2.png",60, 60 ,false, false); 
    Image Battery_Charging_1_img       = new Image("images/BAT_Full_InCharge-3.png",60, 60 ,false, false); 
    Image Battery_Charging_0_img       = new Image("images/BAT_Full_InCharge-Empty.png",60, 60 ,false, false);        
    Image Brightness_img               = new Image("images/sun.png",20, 20 ,false, false);        

    ImageView Battery_ImageView        = new ImageView();
    ImageView Brightness_ImageView     = new ImageView(Brightness_img);
//    ImageView Battery_No_Charging_full = new ImageView(Battery_No_Charging_full_img);
//    ImageView Battery_No_Charging_3    = new ImageView(Battery_No_Charging_3_img);    
//    ImageView Battery_No_Charging_2    = new ImageView(Battery_No_Charging_2_img);  
//    ImageView Battery_No_Charging_1    = new ImageView(Battery_No_Charging_1_img);
//    ImageView Battery_No_Charging_0    = new ImageView(Battery_No_Charging_0_img);
//    ImageView Battery_Charging         = new ImageView(Battery_Charging_full_img);
//    ImageView Battery_Charging_3       = new ImageView(Battery_Charging_3_img);    
//    ImageView Battery_Charging_2       = new ImageView(Battery_Charging_2_img);  
//    ImageView Battery_Charging_1       = new ImageView(Battery_Charging_1_img);
//    ImageView Battery_Charging_0       = new ImageView(Battery_Charging_0_img);  


    
    
    
    public BatteryCheck(Map<String, String> LangMap){     
        WordMap = LangMap;
    }    
    
    public void BatteryCheck() throws SocketException, UnknownHostException, IOException{
        Get_Power_Supply();        
        Get_Power_Status();
        Get_Power_Capacity();
    }
    
    public void BatteryShow(Stage stage,AcroRed_VDI_Viewer inputARVI) {
        
//        Group root = new Group();
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.setTitle("Progress Controls");
// 
//        final Slider slider = new Slider();
//        slider.setMin(0);
//        slider.setMax(50);
//         
//        final ProgressBar pb = new ProgressBar(0);
//        final ProgressIndicator pi = new ProgressIndicator(0);
// 
//        slider.valueProperty().addListener(new ChangeListener<Number>() {
//            public void changed(ObservableValue<? extends Number> ov,
//                Number old_val, Number new_val) {
//                pb.setProgress(new_val.doubleValue()/50);
//                pi.setProgress(new_val.doubleValue()/50);
//            }
//        });
// 
//        final HBox hb = new HBox();
//        hb.setSpacing(5);
//        hb.setAlignment(Pos.CENTER);
//        hb.getChildren().addAll(slider, pb, pi);
//        scene.setRoot(hb);
//        stage.show();        
        
        ARVI = inputARVI;        
        Stage BatteryStage = new Stage();
        Scene Scene;
        BorderPane rootPane;               // BorderPane框架
        MainGP = new GridPane();
        Bounds mainBounds;
        /*------------------------ 設定長寬 ------------------------*/
        int Scene_width; 
        int Scene_heighth; 
        int MainGP_width;
        int MainGP_height;        
        int Btn_width     = 60;
        int Btn_height    = 60;        
        
        switch (WordMap.get("SelectedLanguage")) {
            case "English":
                Scene_width   = 385; // 428
                Scene_heighth = 100; // 143
                MainGP_width  = 385;
                MainGP_height = 100; 
                break;
            default:
                Scene_width   = 320; 
                Scene_heighth = 100; // 143
                MainGP_width  = 320;
                MainGP_height = 100;
                break;
        }         
        
        /*------------------------ 設定Layout component ------------------------*/        
        MainGP.setGridLinesVisible(false);             // 開啟或關閉表格的分隔線       
        MainGP.setAlignment(Pos.CENTER);       
        MainGP.setPadding(new Insets(0, 0, 0, 0)); // 設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        MainGP.setHgap(18);                             // 元件間的水平距離
   	MainGP.setVgap(0);                            // 元件間的垂直距離           
        MainGP.setPrefSize(MainGP_width, MainGP_height);
        MainGP.setMaxSize(MainGP_width, MainGP_height);
        MainGP.setMinSize(MainGP_width, MainGP_height); 
//        MainGP.setTranslateY(-15); 
        MainGP.setTranslateX(0);     
        /***  底層畫面使用Border的方式Layout ***/
//        rootPane = new BorderPane();                   
//        rootPane.setCenter(MainGP);
               
        /*----------------------------------- show Icon -----------------------------------*/        
        HBox Batt_Information_hbox = new HBox();               
        Batt_Information_hbox.setSpacing(20);
        Batt_Information_hbox.setAlignment(Pos.CENTER);
        Batt_Information_hbox.setPadding(new Insets(0, 0, 0, 0));          
        Batt_Information_hbox.getChildren().addAll(Battery_ImageView);                
        MainGP.add(Batt_Information_hbox, 0, 0);                
        /*----------------------------------- show percent -----------------------------------*/
        Set_Power_Capacity_value();
        label_percent.setStyle("-fx-font-family:'Ubuntu'; -fx-text-fill: white;-fx-font-size: 30px;-fx-font-weight: 700;"); 
        HBox Batt_capacity_hbox = new HBox();  
        Batt_capacity_hbox.setSpacing(5);
        Batt_capacity_hbox.setAlignment(Pos.CENTER);
        Batt_capacity_hbox.setPadding(new Insets(0, 0, 0, 0));
        Batt_capacity_hbox.getChildren().addAll(label_percent_int, label_percent);        
        MainGP.add(Batt_capacity_hbox, 1, 0);
        /*----------------------------------- show Status -----------------------------------*/
        try {
            ShowStatus(Get_Power_Supply(), Get_Power_Status());
        } catch (IOException ex) {
            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
        }        
        /*----------------------------------- show Button -----------------------------------*/
        /*
        Btn_Set_Brightness.setPadding(new Insets(1, 0, 2, 0));
        Btn_Set_Brightness.setPrefSize(Btn_width,Btn_height);
        Btn_Set_Brightness.setMaxSize(Btn_width,Btn_height);
        Btn_Set_Brightness.setMinSize(Btn_width,Btn_height);  
        
        Set_Brightness_Btn();        

        Btn_Set_Brightness.setOnAction((ActionEvent event) -> {
            switch (Brightness_level) {
                case 1:
                    {
                        try {
                            Set_Brightness_value(0.50);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }                    
                    break;
                case 2:
                    {
                        try {
                            Set_Brightness_value(0.75);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }                     
                    break;
                case 3:
                    {
                        try {
                            Set_Brightness_value(1.0);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }                      
                    break;
                case 4:
                    {
                        try {
                            Set_Brightness_value(0.25);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }                     
                    break;
                default:
                    break;
            }
        });         

//            System.out.println("return_value: " + return_value + " , Brightness_level: " + Brightness_level );        
        
        
        HBox Batt_Btn_hbox = new HBox();  
        Batt_Btn_hbox.setSpacing(5);
        Batt_Btn_hbox.setAlignment(Pos.CENTER);
        Batt_Btn_hbox.setPadding(new Insets(0, 0, 0, 0));
        Batt_Btn_hbox.getChildren().addAll(Btn_Set_Brightness);        
        MainGP.add(Batt_Btn_hbox, 0, 1);    
        */
        
        try {
            Battery_check();
        } catch (IOException ex) {
            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Scene = new Scene(MainGP, Scene_width, Scene_heighth); // 590, 365
        Scene.getStylesheets().add("LoginMultiVD.css");
        
        mainBounds = stage.getScene().getRoot().getLayoutBounds();        
        
        switch (WordMap.get("SelectedLanguage")) {
            case "English":
                BatteryStage.setX(stage.getX() + (mainBounds.getWidth() - 386 )); // 430
                BatteryStage.setY(stage.getY() + (mainBounds.getHeight() - 188 )); // 232
                break;
            default:
                BatteryStage.setX(stage.getX() + (mainBounds.getWidth() - 320 ));
                BatteryStage.setY(stage.getY() + (mainBounds.getHeight() - 188 )); // 232
                break;
        }        

        BatteryStage.setScene(Scene);
        BatteryStage.setTitle(WordMap.get("Battery"));  // 視窗標題
        switch (GB.JavaVersion) {
            case 0:
                BatteryStage.getIcons().add(new Image("images/Icon2.png"));
                break;
            case 1:
                BatteryStage.getIcons().add(new Image("images/MCIcon2.png"));
                break;                
            default:
                break;
        }                           
        BatteryStage.initStyle(StageStyle.DECORATED); // DECORATED -> UNDECORATED 
        BatteryStage.initModality(Modality.NONE);     // window modal lock NONE -> WINDOW_MODAL ->  APPLICATION_MODAL
//        Stage.initOwner(primaryStage);
        BatteryStage.setResizable(false);             // 將視窗放大,關閉    
        BatteryStage.show(); // important *** lock and return value : show -> showAndWait

        
        // 新增Stage關閉事件
        BatteryStage.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if(ov.getValue()==false) {
                BatteryStage.close();
                timer_BatteryCheck.cancel();
                GB.lock_battery = false;
            }
        });        
        
        BatteryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
            System.out.println("Battery Stage is closing");
            BatteryStage.close();
            timer_BatteryCheck.cancel();
            GB.lock_battery = false;
          }
      }); 
        
    }    
 
    /*------------------------------------- function -------------------------------------*/
    public boolean Get_Power_Supply() throws IOException {
        
        String line_Check_power_supply = "";       
        String command_power_supply = "cat /sys/class/power_supply/ADP1/online"; // /root/battery.sh adpOnline
        Process power_supply_process    = Runtime.getRuntime().exec(command_power_supply);

        BufferedReader output_power_supply = new BufferedReader (new InputStreamReader(power_supply_process.getInputStream()));
        line_Check_power_supply = output_power_supply.readLine();
        
        if(line_Check_power_supply != null) {  
            if (line_Check_power_supply.contains("0")) {
                power_supply  = false;                    
            } else if (line_Check_power_supply.contains("1")) {
                power_supply  = true;
            }
        }        
        
//        System.out.println("power supply: " + power_supply + "\n");
        
        if(power_supply) {
            return true;
        } else {
            return false;
        }
        
    }          
    
    public boolean Get_Power_Status() throws IOException {
        
        String line_Check_status    = "";        
        String command_status   = "cat /sys/class/power_supply/BAT0/status";  // /root/battery.sh batStatus    
        Process power_status_process = Runtime.getRuntime().exec(command_status);

        BufferedReader output_power_status = new BufferedReader (new InputStreamReader(power_status_process.getInputStream()));
        line_Check_status = output_power_status.readLine();  
        
        if(line_Check_status != null) {  
            if (line_Check_status.contains("Charging")) {
                power_Charging = true;
            } else if (line_Check_status.contains("Discharging")) {
                power_Charging = false;
            } 
        }         

//        System.out.println("power Charging: " + power_Charging + "\n");
        
        if(power_Charging) {
            return true;
        } else {
            return false;
        }        
        
    }      
    
    public int Get_Power_Capacity() throws IOException {
        
        String line_Check_capacity    = "";     
        String command_capacity = "cat /sys/class/power_supply/BAT0/capacity"; // /root/battery.sh batCapacity       
        Process power_capacity_process = Runtime.getRuntime().exec(command_capacity);

        BufferedReader output_power_capacity = new BufferedReader (new InputStreamReader(power_capacity_process.getInputStream()));
        line_Check_capacity = output_power_capacity.readLine();  
        
        if(line_Check_capacity != null) {  
            power_capacity = Integer.parseInt(line_Check_capacity);
        }           

//        System.out.println("power capacity: " + power_capacity + "\n");
        
        return power_capacity;
    }    
    
    public int Get_Brightness_value() throws IOException {
        
        String line_Check_value = "";     
        int return_value     = 0;
        String command_value = "xrandr --current --verbose | grep -A 8 \"eDP1\" | grep Brightness | cut -f2- -d:";
        Process get_value_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",command_value});

        BufferedReader output_value = new BufferedReader (new InputStreamReader(get_value_process.getInputStream()));
        line_Check_value = output_value.readLine();  

        if(line_Check_value != null) {  

            return_value = (int) (Double.parseDouble(line_Check_value) * 100);
        }         
        
        
        if(return_value > 0 && return_value <= 25) {
            Brightness_level = 1;
        } else if(return_value > 25 && return_value <= 50) {
            Brightness_level = 2;
        } else if(return_value > 50 && return_value <= 75) {
            Brightness_level = 3;
        } else if(return_value > 75 && return_value <= 100) {
            Brightness_level = 4;
        }
        System.out.println("return_value: " + return_value + " , Brightness_level: " + Brightness_level );
        return return_value;
    }    
    /*------------------------------------- UI -------------------------------------*/
    public void Set_Brightness_value(double vaule) throws IOException, InterruptedException {
        String command_value = "xrandr --output eDP1 --brightness " + vaule;
        Runtime.getRuntime().exec(command_value).waitFor();
        Set_Brightness_Btn();        
    }
    
    public void Set_Brightness_Btn() {
        try {
            Brightness_value = Get_Brightness_value();
            Brightness_str = Integer.toString(Brightness_value) + "%";
            Btn_Set_Brightness.setText(Brightness_str);
        } catch (IOException ex) {
            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
        }

        // button layout
        Btn_Set_Brightness.setGraphic(Brightness_ImageView);
        Btn_Set_Brightness.setAlignment(Pos.CENTER);
        Btn_Set_Brightness.setContentDisplay(ContentDisplay.TOP);    
    }
    
    public void Set_Power_Capacity_value() {
        try {
            power_capacity_value = Get_Power_Capacity();
        } catch (IOException ex) {
            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        label_percent_int.setText(Integer.toString(power_capacity_value));        
        label_percent_int.setStyle("-fx-font-family:'Ubuntu'; -fx-text-fill: white;-fx-font-size: 30px;-fx-font-weight: 700;");    
    }
    
    public void Set_Icon(boolean IsCharging) throws IOException {
        
        if(IsCharging) {
            if(Get_Power_Capacity() >= 80) {
                setGraphic_Number = 0;
                Battery_ImageView.setImage(Battery_Charging_full_img);
            } else if(Get_Power_Capacity() < 80 && Get_Power_Capacity() >= 60) {
                setGraphic_Number = 1;
                Battery_ImageView.setImage(Battery_Charging_3_img);
            } else if(Get_Power_Capacity() < 60 && Get_Power_Capacity() >= 40) {
                setGraphic_Number = 2;
                Battery_ImageView.setImage(Battery_Charging_2_img);
            } else if(Get_Power_Capacity() < 40 && Get_Power_Capacity() >= 20) {
                setGraphic_Number = 3;
                Battery_ImageView.setImage(Battery_Charging_1_img);
            } else if(Get_Power_Capacity() < 20) {
                setGraphic_Number = 4;
                Battery_ImageView.setImage(Battery_Charging_0_img);
            }            
        } else {
            if(Get_Power_Capacity() >= 80) {
                setGraphic_Number = 5;
                Battery_ImageView.setImage(Battery_No_Charging_full_img);
            } else if(Get_Power_Capacity() < 80 && Get_Power_Capacity() >= 60) {
                setGraphic_Number = 6;
                Battery_ImageView.setImage(Battery_No_Charging_3_img);
            } else if(Get_Power_Capacity() < 60 && Get_Power_Capacity() >= 40) {
                setGraphic_Number = 7;
                Battery_ImageView.setImage(Battery_No_Charging_2_img);
            } else if(Get_Power_Capacity() < 40 && Get_Power_Capacity() >= 20) {
                setGraphic_Number = 8;
                Battery_ImageView.setImage(Battery_No_Charging_1_img);
            } else if(Get_Power_Capacity() < 20) {
                setGraphic_Number = 9;
                Battery_ImageView.setImage(Battery_No_Charging_0_img);
            }
        } 
        
        ARVI.Battery_setGraphic(setGraphic_Number);        
        
    }    
        
    public void ShowStatus(boolean supply, boolean status) throws IOException {
        if((supply && status) || (supply && !status)) {
            label_BattStatus.setText(WordMap.get("Charging"));
            Set_Icon(true);
        } else if (!supply && !status){
            label_BattStatus.setText(WordMap.get("NotCharging"));
            Set_Icon(false);
        }
        
        ChangeLabelLang(label_BattStatus);
        
        HBox Batt_status_hbox = new HBox();  
        Batt_status_hbox.setSpacing(10);
        Batt_status_hbox.setAlignment(Pos.CENTER);
        Batt_status_hbox.setPadding(new Insets(0, 0, 0, 0));
        Batt_status_hbox.getChildren().addAll(label_BattStatus);        
        MainGP.add(Batt_status_hbox, 2, 0);        
    }
    
    public void ChangeLabelLang(Label Label01) {
        switch (WordMap.get("SelectedLanguage")) {
            case "English":
                Label01.setStyle("-fx-font-family:'Ubuntu'; -fx-text-fill: white;-fx-font-size: 30px;-fx-font-weight: 700;");
                break;
            default:
                Label01.setStyle("-fx-font-family: 'Droid Sans Fallback'; -fx-text-fill: white;-fx-font-size: 30px;-fx-font-weight: 900;");
                break;
        }                 
    }    
    
    public void Battery_check() throws UnknownHostException, IOException { 
        // 每秒check  網路狀態
        timer_BatteryCheck = new Timer(true);
        task_BatteryCheck = new TimerTask(){
            @Override
            public void run() {                    
                Platform.runLater(() -> {
                    try {
                        ShowStatus(Get_Power_Supply(), Get_Power_Status());
                    } catch (IOException ex) {
                        Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Set_Power_Capacity_value();
                });                     
            
            }
        };
            timer_BatteryCheck.schedule(task_BatteryCheck, 0, 3000);     
    }    
    
             
}
