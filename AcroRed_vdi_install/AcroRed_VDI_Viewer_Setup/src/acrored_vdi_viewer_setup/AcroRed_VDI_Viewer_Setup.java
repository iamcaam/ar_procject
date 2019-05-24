/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer_setup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import static java.lang.Thread.sleep;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author william
 */
public class AcroRed_VDI_Viewer_Setup extends Application{
    /***   版本   ***/
    private static String Ver = "V 5.3.2"; // V 5.0.16-build3 ----- AcroRed
    /***    顯示文字對應表    ***/
    public Map<String, String> WordMap = new HashMap<>();
    /***  語言與JSON File對應表 ***/
    public Map<String, String> LanguageFileMap = new HashMap<>();
    /***   系統環境宣告   ***/
    private String LangNow;
    private String LangChange;
    /*** Thread 宣告 ***/
    private InstallProcess IP;
    private InstallCheck IC;
    private InstallCopy ICP;
    // global variable
    public GB GB; // 2017.10.27 william 版本判斷
    /***  以下為顯示元件宣告    ***/
    final int scene_width         = 446; // Scene寬度
    final int scene_height        = 304; // Scene高度
    final int anchorpane_height   = 60;  // 下層高度
    final int logo_height         = 60;  // logo高度
    final int btn_width           = 190; // btn寬度 
    final int btn_height          = 35;  // btn高度    
    final int Installing_height   = 35;  // 高度   
    final int Installing_width    = 370; // 高度   
    final int Title_width         = 380; // Title寬度 
    final int Title_height        = 60;  // Title高度    
    int count                     = 0;   
    private Stage public_stage;           // 畫面最外層，包Scene
    private Scene scene;                  // 畫面層2層，包parent和node
    private BorderPane rootPane;          // 包MainGP(中層)和anchorpane(上下層)
    private AnchorPane anchorpane;        // 上層
    private GridPane MainGP;              // 中層
    private AnchorPane anchorpane_bottom; // 下層
    // HBox
    private HBox WC;
    private HBox WC2;
    private HBox btn_area;
    private HBox message_area;
    // label
    private Label Logo;
    private Label MyTitle;
    private Label MyTitle2;  
    private Label Installing;
    // String
    public String[] params;
    // Image
    private Image company_Logo;
    // Process
    public Process process; // 測試用
    public Process process_shutdown;
    // boolean
    private boolean Leave_Color   = false;
    private boolean Install_Color = false;
    private boolean complete_flag   = false;   
    public boolean IsXP             = false; 
    // timer
    public AnimationTimer timer;
    public AnimationTimer timer2;
    // 變更文字  
    BlockingQueue<String> messageQueue = null;
    String messageText;
    String message;    
    // button
    private Button Install;
    private Button Leave;
    static Map<String, Long> rxCurrentMap       = new HashMap<String, Long>();
    static Map<String, List<Long>> rxChangeMap = new HashMap<String, List<Long>>();
    static Map<String, Long> txCurrentMap       = new HashMap<String, Long>();
    static Map<String, List<Long>> txChangeMap = new HashMap<String, List<Long>>();
    // 讀檔案
    public File file_InstallInfo = new File("InstallInfo.txt");
    public FileReader fr         = null; 
    public BufferedReader br     = null; 
    String data; 
  
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setAlwaysOnTop(true);
        public_stage = primaryStage;
        
 

        
        IP           = new InstallProcess(); // 安裝remove viewer相關檔案
        IC           = new InstallCheck();   // 系統檢查
        ICP          = new InstallCopy();    // 複製檔案
        GB           = new GB();             // 存放全域變數
        messageQueue = new ArrayBlockingQueue<>(1);
        /* -----------檢查系統版本----------- */
//        System.out.println("是否為WIN7:"+IC.ISWIN7+","+"OS是否為64版本:"+IC.OS+","+"是否為SP1:"+IC.ISSP1+","+"是否已安裝KB:"+IC.IsKB);
        IsXP = IC.checkOSVerIsXP();
        if(!IsXP){
            IC.checkOS();
            IC.checkISWIN7();                // 如果為true就不安裝(非SP1)
            IC.checkISSP1();
            IC.checkIsKB();
        }
        /* -----------檢查設定值----------- */
        System.out.println("是否為WIN7："+IC.ISWIN7+","+"OS是否為64版本："+IC.OS+","+"是否為SP1："+IC.ISSP1+","+"是否已安裝KB："+IC.IsKB);
        System.out.print("安裝選擇(0：64bit / 1：32bit)：" + GB.Install_ver + " \n");    
        System.out.println("是否安裝完成："+ complete_flag +" \n");                                         
//        InitailLanguageFileMap();  //初始化語言與JSON對應，每次新增語言必須修改出使化程式     
//        LangNow="繁體中文";   //若沒有預設語言，以繁體中文為主
//        LoadLanguageMapFile(LanguageFileMap.get(LangNow)); // 設定這次啟動的語言，建議每一個元件的文字
        
        /************************************************************畫面Layout Start************************************************************/
        /***   主畫面使用GRID的方式Layout   ***/
        MainGP = new GridPane();        
//        MainGP.setGridLinesVisible(true);              // 開啟或關閉表格的分隔線       
        MainGP.setAlignment(Pos.TOP_CENTER);       
        MainGP.setPadding(new Insets(30, 15, 30, 15)); // 設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        MainGP.setHgap(8);                             // 元件間的水平距離
   	MainGP.setVgap(8);                             // 元件間的垂直距離
        /***   狀態列-- 功能(下)畫面使用Anchor的方式Layout   ***/
        anchorpane = new AnchorPane();
        anchorpane.setStyle("-fx-background-color: rgb(118,185,237);");
        anchorpane.setPrefHeight(anchorpane_height);
        anchorpane.setMaxHeight(anchorpane_height);
        anchorpane.setMinHeight(anchorpane_height);
        /***   狀態列-- 功能(下)畫面使用Anchor的方式Layout   ***/
        anchorpane_bottom = new AnchorPane();
        anchorpane_bottom.setStyle("-fx-background-color: rgb(230,230,230) ;");
        anchorpane_bottom.setPrefHeight(anchorpane_height);
        anchorpane_bottom.setMaxHeight(anchorpane_height);
        anchorpane_bottom.setMinHeight(anchorpane_height);        
        /***   底層畫面使用Border的方式Layout   ***/
        rootPane = new BorderPane();
        rootPane.setTop(anchorpane);
        rootPane.setCenter(MainGP);       
        rootPane.setBottom(anchorpane_bottom);
        /***   畫面式樣   ***/
        Logo();
        Title();
        Title2();
        InstallingLabel();
        /************************************************************畫面Layout End************************************************************/
        /***   安裝 btn  ***/
        Install = new Button("安裝");
        Install.setPrefSize(btn_width,btn_height); //80,30
        Install.setMaxSize(btn_width,btn_height);
        Install.setMinSize(btn_width,btn_height);
        Install.getStyleClass().add("btn_style");
        //Install.setId("button");
        Install.setOnAction((ActionEvent event) -> {
            boolean CanInstall = true;
            if(GB.Install_ver==0) { // 2019.01.16 william bug fix
                String cmd = "tasklist /FI \"IMAGENAME eq AcroRed AcroViewer for Win 10_8_7.exe\"";
                try {            
                    Process Info =   Runtime.getRuntime().exec(new String[]{"cmd","/c", cmd});
                    BufferedReader reader = new BufferedReader(new InputStreamReader(Info.getInputStream()));
                    String returnResult;
                    while ((returnResult = reader.readLine()) != null) {      
                        // System.out.println("returnResult : " + returnResult);
                        if(returnResult.contains("AcroViewer")) {                        
                            CanInstall = false;
                            break;
                        }                        
                    }                    
                } catch (IOException ex) {
                    Logger.getLogger(AcroRed_VDI_Viewer_Setup.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if(GB.Install_ver==1) {                                                
                String cmd = "tasklist /FI \"IMAGENAME eq AcroRed AcroViewer for Win 10_8_7_XP.exe\"";
                try {            
                    Process Info =   Runtime.getRuntime().exec(new String[]{"cmd","/c", cmd});
                    BufferedReader reader = new BufferedReader(new InputStreamReader(Info.getInputStream()));
                    String returnResult;
                    while ((returnResult = reader.readLine()) != null) {             
                        if(returnResult.contains("AcroViewer")) {                        
                            CanInstall = false;
                            break;
                        }                        
                    }                     
                } catch (IOException ex) {
                    Logger.getLogger(AcroRed_VDI_Viewer_Setup.class.getName()).log(Level.SEVERE, null, ex);
                }
            }      
            //CanInstall = false;
            if(CanInstall) {
            
            updateInstall();
            Install.setDisable(true);
            Leave.setDisable(true);
            /* 處理等待中文字 */
            
            MessageProducer producer = new MessageProducer(messageQueue);
            Thread t = new Thread(producer);
            t.setDaemon(true);
            t.start();     
            
            Task<Void> InstallRunningTask = new Task<Void>() {
                @Override
                    protected Void call() throws Exception {
                        if(!complete_flag) {
                            if(GB.Install_ver==0&&!IC.ISWIN7) {
                                ICP.start();
                            }
                            else if (GB.Install_ver==1) {
                                ICP.start();
                            }
                        }
                        return null;
                    }
            };

            InstallRunningTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    System.out.println("** 安裝失敗 ** \n");
                    Install.setDisable(true);
                    Leave.setDisable(false);                    
                    Installing.setText("安裝失敗，按離開關閉安裝程式!");
                    Installing.setTranslateX(7);
                    timer.stop();
                }
            });

            InstallRunningTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    if(GB.Install_ver==0&&IC.ISWIN7==true) {
                        Installing.setText("安裝失敗，不支援此作業系統!");
                        Installing.setTranslateX(9);
                        Install.setDisable(true);
                        Leave.setDisable(false);    
                        timer.stop();
                    }
                    else {
                        if(complete_flag) {
                            if(GB.Install_ver==0) {
                                try {
                                    ShutdownCMD();
                                } catch (IOException ex) {
                                    Logger.getLogger(AcroRed_VDI_Viewer_Setup.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            else if (GB.Install_ver==1) {
                                Installing.setText("已安裝完成!");
                                Installing.setTranslateX(115);
                                Install.setDisable(false);
                                Leave.setDisable(false);                                  
                            }
                        }
                        else {
                            System.out.println("** 正在執行安裝 ** \n");
                            IP.start();
                            CheckInstallcompleted();   
                        }
                    }               
                }
            });
            
            new Thread(InstallRunningTask).start();
            
            } else {
                primaryStage.setAlwaysOnTop(false);
                CloseViewerAlert();
            }
            
        });
        
        final LongProperty lastUpdate = new SimpleLongProperty();                 
        final long minUpdateInterval = 5000 ; // nanoseconds. Set to higher number to slow output.

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate.get() > minUpdateInterval) {
                    messageText = messageQueue.poll();
                    if (messageText != null) {
                        Installing.setText(messageText); 
                        messageQueue.clear();
                    }
                    lastUpdate.set(now);
                }
            }
        };
        timer.start();           
        
        /***   離開 btn  ***/
        Leave = new Button("離開"); // WordMap.get("Exit")
        Leave.setPrefSize(btn_width,btn_height);
        Leave.setMaxSize(btn_width,btn_height);
        Leave.setMinSize(btn_width,btn_height);
        Leave.getStyleClass().add("btn_style");
//        Leave.setId("button");
        Leave.setOnAction((event) -> {                                               
            updateLeave();
            ExitProcess();    
        });      
        
        btn_area=new HBox();
        btn_area.setPadding(new Insets(1, 0, 0, 0));      //設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        btn_area.setSpacing(20);
        btn_area.getChildren().addAll(Install, Leave);                
        btn_area.setAlignment(Pos.CENTER);
            
        anchorpane_bottom.getChildren().addAll(btn_area); // 將功能Button 放置AnchorPane中       
        anchorpane_bottom.setTopAnchor(btn_area,11.0);     // 設置邊距
        anchorpane_bottom.setLeftAnchor(btn_area,30.0);     // 430.0
        
        /***   產生主頁視窗   ***/      
        scene = new Scene(rootPane, scene_width, scene_height);
        scene.getStylesheets().add("main.css")  ;
        /***   設定元件的css id   ***/     
        MyTitle.setId("Title");
        MyTitle2.setId("Title2");
        Installing.setId("Installing");
        Install.getStyleClass().addAll("btn_focused_style","btn_hover_style");
        
//        ChangeLabelLang(L_IP_Addr, userName, pw,  Colon1, Colon2, Colon3); //變更Label語言字型
//        ChangeButtonLang(Install,Leave); //變更Button語言字型
         
//        Install.requestFocus();
        keydownup();
//        mousemove();
//        buttonhover_focus();
               
        switch ( GB.Install_ver ) { // 視窗標題+版本
            case 0:
                public_stage.setTitle(" AcroRed AcroViewer Installation 64bit " + Ver);           
                public_stage.getIcons().add(new Image("image/64bit_install.png"));                 // 視窗Icon 
                break;
            case 1:
                public_stage.setTitle(" AcroRed AcroViewer Installation 32bit " + Ver);        
                public_stage.getIcons().add(new Image("image/32bit_install.png"));                 // 視窗Icon 
                break;             
            case 2:
                public_stage.setTitle(" AcroRed IPSC Installation " + Ver);        
//                public_stage.getIcons().add(new Image("image/.png"));                 // 視窗Icon 
                break;                 
        }        
        public_stage.setScene(scene);
        public_stage.initStyle(StageStyle.DECORATED);                            // 視窗裝飾，想關閉可用 -> TRANSPARENT
        public_stage.setResizable(false);                                          // 視窗縮放關閉
        public_stage.show();
        /* 畫面置中 */
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        public_stage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        public_stage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);        
        public_stage.toFront();
        
        /*****關閉視窗"X"-會與離開按鍵一樣 可以關閉全部視窗 *****/
        public_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {  
                killTask();
                System.out.println("Stage is closing");      
//                ExitWindowProcess(we);        
                //public_stage.close();             
                System.out.println("----------end----------");
            }
        }); 
        
    }
    
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
                    switch (messageCount) {
                       case  0:
                           message = "安裝中，請稍後 ";
                           messageQueue.put(message);
                           messageCount ++;
                           sleep(300);
                           break;
                        case  1:   
                           message = "安裝中，請稍後 .";
                           messageQueue.put(message);                            
                           messageCount ++;   
                           sleep(300);
                           break;
                        case  2:   
                           message = "安裝中，請稍後 ..";
                           messageQueue.put(message);                                 
                           messageCount ++;   
                           sleep(300);
                           break;
                        case  3:   
                           message = "安裝中，請稍後 ...";
                           messageQueue.put(message);                               
                           messageCount = 0;       
                           sleep(300);
                           break;                    
                    }
//                    final String message = "安裝中，請稍後 " + (++messageCount);
//                    messageQueue.put(message);
                }
            } catch (InterruptedException exc) {
                System.out.println("Message producer interrupted: exiting.");
            }
        }
    }    

    public void CheckInstallcompleted() {
        final LongProperty lastUpdate = new SimpleLongProperty();
                            
        final long minUpdateInterval = 500000000 ; // nanoseconds. Set to higher number to slow output.

        timer2 = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate.get() > minUpdateInterval) {
//                    System.out.println(IP.Install_flag+","+ICP.Copy_flag);
//                    System.out.println("Copy Return Code: " + ICP.copy_ReturnCode + " ,Install flag: " + IP.Install_flag);
                    if (IP.Install_flag==true&&ICP.Copy_flag==true) {
//                    if (IP.Install_flag==true&&ICP.copy_ReturnCode!=-1) {    
//                    if (IP.Install_flag==true) {    
                        timer.stop();
                        timer2.stop();
                        Install.setDisable(false);
                        Leave.setDisable(false);  
                        complete_flag = true;
                        if(GB.Install_ver==0) {
                            Installing.setText("安裝完成，請重新開機!");
                            Installing.setTranslateX(50);
                            Install.setText("重新開機");
                            
                        }
                        else if(GB.Install_ver==1) {
                            Installing.setText("安裝完成，按離開關閉安裝程式!");
                            Installing.setTranslateX(7);
                        }                                            
                    }
                    lastUpdate.set(now);
                }
            }
        };
        timer2.start();  
    }      
    // 檢查並清除檔案
    public void ClearInstallInfo() {
        if (file_InstallInfo.exists()) {
             file_InstallInfo.delete();
        }
    } 
    // 讀檔案
    public void CheckInstallInfo() throws FileNotFoundException, IOException {
        if (file_InstallInfo.exists()) {
             fr = new FileReader(file_InstallInfo);
             br = new BufferedReader(fr); 
             
             while ((data = br.readLine()) != null){
                 System.out.print(data);
             }
        }
        
	Timer timer_check=new Timer(true);
        TimerTask task_check=new TimerTask(){
            @Override
            public void run() {       

            }
        };
        timer_check.schedule(task_check, 0,1000);         
    }     
    
    public void Logo() {
        Logo = new Label();         
        Logo.setAlignment(Pos.CENTER_LEFT);        
        company_Logo = new Image("image/Logo.png");               
        Logo.setGraphic(new ImageView(company_Logo));        
        Logo.setMaxHeight(logo_height);        
        Logo.setMinHeight(logo_height);        
        Logo.setPrefHeight(logo_height);
        Logo.setPadding(new Insets(0, 0, 0, 0));        // 設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 0, 0, 48, 0 -> 0, 0, 0, 0
        Logo.setTranslateX(17);
        Logo.setTranslateY(10);
//        MainGP.add(Logo, 0, 0, 1, 1);                  // 元件佔用多行或多列，使用「add(元件,列,行,列數,行數)」方法加入元件
        anchorpane.getChildren().addAll(Logo);
    }
    
    public void Title() {
        /***   Title 放入一個HBOX內   ***/
        MyTitle = new Label("AcroViewer");       
        WC = new HBox(MyTitle);
        WC.setAlignment(Pos.TOP_CENTER);
        WC.setMaxSize(Title_width,Title_height);  // 550                     
        WC.setMinSize(Title_width,Title_height);                       
        WC.setPrefSize(Title_width,Title_height);                      
        WC.setPadding(new Insets(0, 0, 0, 0));        // 設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 0, 75, 10, 0
        WC.setTranslateY(12); //-45
        WC.setTranslateX(-79); // -80 -25
        MainGP.add(WC, 0, 1, 2, 1); //  1, 1, 2, 1
    }
    
    public void Title2() {
        /***   Title 放入一個HBOX內   ***/
        MyTitle2 = new Label("安裝程式");       
        WC2 = new HBox(MyTitle2);
        WC2.setAlignment(Pos.TOP_CENTER);
        WC2.setMaxSize(Title_width,Title_height);  // 550                     
        WC2.setMinSize(Title_width,Title_height);                       
        WC2.setPrefSize(Title_width,Title_height);                      
        WC2.setPadding(new Insets(0, 0, 0, 0));        // 設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 0, 75, 10, 0
        if(!IsXP){
            WC2.setTranslateY(8); //-45
            }
        else {
            WC2.setTranslateY(10);
        }
        WC2.setTranslateX(98); // 100 115
        MainGP.add(WC2, 0, 1, 2, 1); //  1, 1, 2, 1
    }    
       
    public void InstallingLabel() {
        
        Installing = new Label();
//        GridPane.setHalignment(Installing, HPos.CENTER);      
        Installing.setAlignment(Pos.CENTER);   
        Installing.setPrefSize(Installing_width,Installing_height);                       
        Installing.setPrefSize(Installing_width,Installing_height);                  
        Installing.setPrefSize(Installing_width,Installing_height);    
        
        
        message_area=new HBox();
        message_area.setPadding(new Insets(0, 0, 0, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔) 
        message_area.setSpacing(0);
        message_area.getChildren().addAll(Installing);                
        message_area.setAlignment(Pos.CENTER);        
        
        
	                   
        Installing.setTranslateY(0); // -15
        Installing.setTranslateX(70);//Installing.setTranslateX(0);
        Installing.setPadding(new Insets(0, 0, 0, 0));     
        MainGP.add(message_area, 0, 3, 2, 1);  
    }
        
    public void IsShutdownAlert(){        
         /***   跳出視窗詢問 是否安裝程式   ***/
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("訊息");//設定對話框視窗的標題列文字  , "您真的要結束程式嗎？", ButtonType.YES, ButtonType.NO
        //alert.setHeaderText(WordMap.get("Message")+" : "+" VD is Online!! ");//設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        alert.setHeaderText(null);
        alert.getDialogPane().setMinSize(380,Region.USE_PREF_SIZE);
        alert.getDialogPane().setMaxSize(380,Region.USE_PREF_SIZE);
        if(GB.Install_ver==1||GB.Install_ver==2){
            alert.setContentText("AcroRed AcroViewer程式安裝完成，是否關閉安裝程式?");
        }
        else {
            alert.setContentText("AcroRed AcroViewer程式安裝完成，是否重新開機?");//VD 已上線,是否還要連線,若連線,則前一個連線會自動斷線.
        }
        ButtonType buttonTypeOK = new ButtonType("確認",ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("取消",ButtonBar.ButtonData.CANCEL_CLOSE); // WordMap.get("Alert_Cancel"
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel); 
        /* 畫面置中 */
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        alert.setX((primScreenBounds.getWidth() - 380.0) / 2);
        alert.setY((primScreenBounds.getHeight() - 121.0) / 2);            
        /* 加下面程式才能使用enter*/
        Button btCancel = (Button) alert.getDialogPane().lookupButton(buttonTypeCancel);
        btCancel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER) {
                    btCancel.fire();
//                        Leave.requestFocus();
                    event.consume();
                }
            }
        });           
        
        Optional<ButtonType> result01 = alert.showAndWait();
            if (result01.get() == buttonTypeOK){                    
            try {
                if(GB.Install_ver==1||GB.Install_ver==2){
                    ExitProcess(); 
                }
                else {
                    ShutdownCMD();
                }
            } catch (IOException ex) {
                Logger.getLogger(AcroRed_VDI_Viewer_Setup.class.getName()).log(Level.SEVERE, null, ex);
            }

            } else {
                if(GB.Install_ver==1||GB.Install_ver==2) {
                    Installing.setText("安裝完成，按離開關閉安裝程式!");
                }
                else {
                    Installing.setText("安裝完成，請重新開機!");
                    Install.setText("重新開機");
                    Install.setDisable(false);
                    Leave.setDisable(false);                    
                }
                
//                    try {
//                        CreateDesktopshortcut();
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(AcroRed_VDI_Viewer_Setup.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (IOException ex) {
//                        Logger.getLogger(AcroRed_VDI_Viewer_Setup.class.getName()).log(Level.SEVERE, null, ex);
//                    }                
//                ExitProcess();
                    // ... user chose CANCEL or closed the dialog
                    alert.close();
            }      
    }        
    
    public void CloseViewerAlert() {        
         /***   跳出視窗詢問 是否安裝程式   ***/
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("訊息");//設定對話框視窗的標題列文字  , "您真的要結束程式嗎？", ButtonType.YES, ButtonType.NO
        //alert.setHeaderText(WordMap.get("Message")+" : "+" VD is Online!! ");//設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        alert.setHeaderText(null);
        alert.getDialogPane().setMinSize(380,Region.USE_PREF_SIZE);
        alert.getDialogPane().setMaxSize(380,Region.USE_PREF_SIZE);
        alert.setContentText("請先關閉AcroViewer，再點擊安裝!");

        /* 畫面置中 */
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        alert.setX((primScreenBounds.getWidth() - 380.0) / 2);
        alert.setY((primScreenBounds.getHeight() - 121.0) / 2);            
                  
        ButtonType buttonTypeOK = new ButtonType("確認",ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(buttonTypeOK); 
        alert.showAndWait();      
    }             
    
    

    public void ShutdownCMD() throws IOException{
        // windows XP 快速關機指令 shutdown -s -f -t 0
        process_shutdown = Runtime.getRuntime().exec("shutdown -r -t 0");
    }

    public void InitailLanguageFileMap(){
        LanguageFileMap.put("English"           , "jsonfile/English.json");
        LanguageFileMap.put("繁體中文"          , "jsonfile/TraditionalChinese.json");
        LanguageFileMap.put("简体中文"          , "jsonfile/SimpleChinese.json");     
        LanguageFileMap.put("TraditionalChinese", "jsonfile/TraditionalChinese.json");
        LanguageFileMap.put("SimpleChinese"     , "jsonfile/SimpleChinese.json");   
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
                WordMap.put("Exit", LMF.get("Exit").toString());   
                WordMap.put("Alert_Cancel", LMF.get("Alert_Cancel").toString());
                WordMap.put("Alert_Confirm", LMF.get("Alert_Confirm").toString());
            }else{
                WordMap.put("Exit", "Exit");   
                WordMap.put("Alert_Cancel", "Cancel");
                WordMap.put("Alert_Confirm", "Confirm");
            }
        }catch(IOException e){
           // e.printStackTrace();         
                WordMap.put("Exit", "Exit");  
                WordMap.put("Alert_Cancel", "Cancel");
                WordMap.put("Alert_Confirm", "Confirm");                
        }
    }
    
    public void ExitProcess(){
        killTask();
        System.exit(0); // 結束程式     0        
        System.out.print("System.exit(0) \n");    
    }   
    
    public void killTask() {
        try {
            Runtime.getRuntime().exec("taskkill /f /t /im execution.exe");
        } catch (IOException ex) {
            Logger.getLogger(AcroRed_VDI_Viewer_Setup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }     
    
    /****視窗X -按視窗X 可關閉所有正開啟得視窗以及系統關閉*****/
    public void ExitWindowProcess(WindowEvent we){
        System.exit(0); // 結束程式     0        
        System.out.print("System.exit(0) \n");
    } 

    /****變更Label字型****字型:中文-標楷體,英文:Times New Roman*********RUN WinXP版 需把Win7版隱藏才可顯示出字型***********/
     public void ChangeLabelLang(Label lab01, Label lab02, Label lab03, Label col01, Label col02, Label col03){
        //Label "IP", "UserName", "Password", "Language"
        if("English".equals(LangNow)){
            lab01.setStyle("-fx-font-family:'Times New Roman'; -fx-font-size: 19px; -fx-font-weight: 900;");//21
            lab02.setStyle("-fx-font-family:'Times New Roman'; -fx-font-size: 19px; -fx-font-weight: 900;");
            lab03.setStyle("-fx-font-family:'Times New Roman'; -fx-font-size: 19px; -fx-font-weight: 900;");

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
//            lab04.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 18px; -fx-font-weight: 900;");
            //冒號":"
            col01.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 17px; -fx-font-weight: 900;");
            col02.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 17px; -fx-font-weight: 900;");
            col03.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-size: 17px; -fx-font-weight: 900;");
            
        }
       
     }
    /****變更button字型****字型:中文-標楷體,英文:Times New Roman****/
      public void ChangeButtonLang(Button but01,  Button but04) {
        //Button "Login", "ManagerVD", "ChangePassword", "Leave"
        if("English".equals(LangNow)){
            but01.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");

            but04.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
            
        }        
        if(("繁體中文".equals(LangNow))||("TraditionalChinese".equals(LangNow))||("简体中文".equals(LangNow))||("SimpleChinese".equals(LangNow))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/
            //win7以上僅能用英文名稱
            but01.setStyle("-fx-font-family:'Microsoft JhengHei';");

            but04.setStyle("-fx-font-family:'Microsoft JhengHei';");   
            
        }        
    }
      
    /********* 第一次安裝完成 - 訊息 -建議重新啟動電腦 ************/
    public void FirstInstallAlert() {
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

    /********* Enter & Page Down & Page Up 功能 ************/
    public void keydownup() {

        Leave.setOnKeyPressed((event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){                     
                Leave.fire();
                event.consume();
            }

            if(event.getCode() == KeyCode.LEFT){
                Install.requestFocus();
//                Install.setId("button");
                Install.getStyleClass().add("btn_hover_style");
                Leave.getStyleClass().remove("btn_hover_style");
                event.consume();
            }                

        });
        
        Install.setOnKeyPressed((event)-> {
           //event.get
            if(event.getCode() == KeyCode.ENTER){                     
                Install.fire();
                event.consume();
            }         
            if(event.getCode() == KeyCode.RIGHT){
                Leave.requestFocus();
//                Leave.setId("button");
                Leave.getStyleClass().add("btn_hover_style");
                Install.getStyleClass().remove("btn_hover_style");
                event.consume();
            }            
        });        

    }
    
    public void mousemove() {        
        Install.setOnMouseEntered((event)-> {
            Install.requestFocus();
        });

        Leave.setOnMouseEntered((event)-> {
            Leave.requestFocus();
        });        
    }
    
    private void updateInstall() { 
        //System.out.print("******** IN Login ********\n");
        Install_Color = true;
        Leave_Color   = false;
        if(Install_Color==true) {
            Install.setId("Button_Install");
            Leave.setId("button");
        }  
        //System.out.print("******** OFF Login ********\n");     
    }    
    
    private void updateLeave() {
        Leave_Color   = true; 
        Install_Color = false;
        if(Leave_Color==true) {
            Leave.setId("Button_Leave");
            Install.setId("button");
        }
    }
    
    /******  控制鍵盤與滑鼠 觸碰button的反應 ******/
    public void buttonhover_focus() {       
        Install.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if(Install.getId()=="Button_Install") {
                }
                else {
                    Install.setId("button");
                }    
            } 
            if (oldValue) {                
                if(Install.getId()=="Button_Install") {
                }
                else {
                    Install.setId("button");
                }                        
            }
            if(Install.isFocused()) {                      
                if(Install.getId()=="Button_Install") {                  
                    Leave.setId("button");
                }
                if(Leave.getId()=="Button_Leave") {
                    Install.setId("button");
                }
                if(Install.getId()=="button") {
                    if(Leave.getId() != "Button_Leave"){                                
                        Leave.setId("button");
                    } 
                } 
            }
        });
        Install.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {                
                if(Install.getId()=="Button_Install") {
                }
                else {
                    Install.setId("button");
                }
            } 
            if (oldValue) {                
                if(Install.getId()=="Button_Install") {
                }
                else {
                    Install.setId("button");
                }               
            }
            if(Install.isHover()) {                               
                if(Install.getId()=="Button_Install") {                  
                    Leave.setId("button");    
                }    
                if(Leave.getId()=="Button_Leave") {
                    Install.setId("button");
                }
                if(Install.getId()=="button") {                   
                    if(Leave.getId() != "Button_Leave") {
                        Leave.setId("button");
                    } 
                } 
                /*** button 在第一次 Mouse-in 後 皆會直接進入 setOnMouseMoved ; 因此在mouse-in時加入條件  ***/
                /*** button 在第一次 Mouse-out 後 皆會直接進入 setOnMouseExited ; 因此在mouse-out時加入條件  ***/
                if(Install.getId()=="Button_Install") {
                    if(Leave.getId() != "Button_Leave") {
                        Leave.setId("button");                                
                        Install.setOnMouseMoved((event)-> {
                            if(Install_Color == true) {
                                Install.setId("button");
                            }
                        });                                
                        Install.setOnMouseExited((event)-> {
                            if(Install_Color == true){
                                Install.setId("Button_Install"); 
                            }                    
                        });                                
                    } 
                }                
            }
        });
        
        Leave.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {
                if(Leave.getId()=="Button_Leave") {                                      
                }
                else {                    
                    Leave.setId("button");        
                } 
            } 
            if (oldValue) {
                if(Leave.getId()=="Button_Leave") {
                }
                else {
                    Leave.setId("button");                   
                }
            }
            if(Leave.isFocused()){
                if(Leave.getId()=="Button_Leave") {
                    Install.setId("button");
                }
                if(Install.getId()=="Button_Install") {
                    Leave.setId("button");
                }                
                if(Leave.getId()=="button"){
                    if(Install.getId() != "Button_Install"){                                
                        Install.setId("button");
                    }             
                }                
            }
        });
        Leave.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue) {
                if(Leave.getId()=="Button_Leave") {                      
                }else{                    
                    Leave.setId("button");   
                } 
            } 
            if (oldValue) {
                if(Leave.getId()=="Button_Leave") {
                }
                else {
                    Leave.setId("button");         
                }
            }
            if(Leave.isHover()) {
                if(Leave.getId()=="Button_Leave") {                    
                    Install.setId("button");   
                }
                if(Install.getId()=="Button_Install") {
                    Leave.setId("button");
                }                
                if(Leave.getId()=="button") {
                    if(Install.getId() != "Button_Install") {
                        Install.setId("button");
                    }              
                }
                /*** button 在第一次 Mouse-in 後 皆會直接進入 setOnMouseMoved ; 因此在mouse-in時加入條件  ***/
                /*** button 在第一次 Mouse-out 後 皆會直接進入 setOnMouseExited ; 因此在mouse-out時加入條件  ***/
                if(Leave.getId()=="Button_Leave") {
                    if(Install.getId() != "Button_Install") {
                        Install.setId("button"); 
                        Leave.setOnMouseMoved((event)-> {
                            if(Leave_Color == true){
                                Leave.setId("button");
                            }                               
                        });
                        Leave.setOnMouseExited((event)-> {
                            if(Leave_Color == true){
                                Leave.setId("Button_Leave");
                            }                                       
                        }); 
                    }
                }
            }
        });        
    }

    
    public void CreateDesktopshortcut() throws InterruptedException, IOException {
        String exe = "";
        String lnk = "";
        String wd = "";  
        
        if(GB.Install_ver==0) {
            exe = "C:\\AcroRed_AcroViewer_64bit_Folder\\AcroRed AcroViewer for Win 10_8_7.exe"; //C:\\AcroRed_VDI_Viewer_64bit_Folder\\AcroRed VDI Viewer for Win 10_8_7.exe
            lnk = "AcroRed AcroViewer for Win 10_8_7"; //AcroRed VDI Viewer for Win 10_8_7
            wd = "C:\\AcroRed_AcroViewer_64bit_Folder\\"; //     C:\\AcroRed_VDI_Viewer_64bit_Folder\\   
        }
        else if(GB.Install_ver==1) {
            exe = "C:\\AcroRed_AcroViewer_32bit_Folder\\AcroRed AcroViewer for Win 10_8_7_XP.exe"; // C:\\AcroRed_VDI_Viewer_32bit_Folder\\AcroRed VDI Viewer for Win 10_8_7_XP.exe
            lnk = "AcroRed AcroViewer for Win 10_8_7_XP";//AcroRed VDI Viewer for Win 10_8_7_XP
            wd = "C:\\AcroRed_AcroViewer_32bit_Folder\\";//    C:\\AcroRed_VDI_Viewer_32bit_Folder\\      
        }
//        String cmd = "mshta VBScript:Execute(\"Set a=CreateObject(\"\"WScript.Shell\"\"):Set b=a.CreateShortcut(a.SpecialFolders(\"\"Desktop\"\") & \"\"\\"+lnk+".lnk\"\"):b.TargetPath=\"\""+exe+"\"\":b.WorkingDirectory=\"\""+wd+"\"\":b.Save:close\") && $objShell = New-Object -comObject Shell.Application && $objDesktop = $objShell.NameSpace(0X0) && $shortcutFilename = \"AcroRed VDI Viewer for Win 10_8_7.lnk\" && $objFolderItem = $objDesktop.ParseName($shortcutFilename) && $objShortcut = $objFolderItem.GetLink  && $objShortcut.SetIconLocation(\"C:\\AcroRed VDI Viewer\\bin\\Logo.ico\") && $objShortcut.Save()";
        if(System.getProperty("os.name").contains("Windows 10") || System.getProperty("os.name").contains("Windows 8") || System.getProperty("os.name").contains("Windows 8.1")) { // 2018.06.21 william 安裝方式修改
            String cp_shortcut = "mklink \"" + System.getenv("userprofile") + "\\" + "Desktop" + "\\" + lnk + ".exe" + "\" \"" + exe + "\" ";
   
            
            Runtime.getRuntime().exec(new String[]{"cmd","/c", cp_shortcut});
        } else {
            String cmd = "mshta VBScript:Execute(\"Set a=CreateObject(\"\"WScript.Shell\"\"):Set b=a.CreateShortcut(a.SpecialFolders(\"\"Desktop\"\") & \"\"\\"+lnk+".lnk\"\"):b.TargetPath=\"\""+exe+"\"\":b.WorkingDirectory=\"\""+wd+"\"\":b.Save:close\")";
            Runtime.getRuntime().exec(new String[]{"cmd","/c", cmd}); 
        }
                       
    }
        
    
    
    
}
