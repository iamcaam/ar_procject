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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import org.json.simple.JSONObject;
/**
 *
 * @author duck_chang
 */
public final class ChangePassword {

    public Map<String, String> WordMap=new HashMap<>();
    public String CurrentIP;
    public String CurrentUserName;
    public String CurrentPasseard;
    public String OriginalPassword;
    public String NewPassword;
    public String ConfirmPassword;
    private ToggleButton buttonOpw=new ToggleButton();
    private ToggleButton buttonNpw=new ToggleButton();
    private ToggleButton buttonCpw=new ToggleButton();
    private  boolean opw_changeAfterSaved = true;
    private  boolean npw_changeAfterSaved = true;
    private  boolean cpw_changeAfterSaved = true;
    private Image EyeClose;
    private Image EyeOpen;
    
    public String CurrentIP_Port; // 2017.08.10 william IP增加port欄位，預設443
    public GB GB; // 2017.10.27 william 版本判斷
        
    Stage ChangePasswordStage = new Stage();
    
    private static Object getStylesheets() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     *
     * @param primaryStage
     * @param LangMap
     * @param password
     * @param IPAddr
     * @param Uname
     * @throws java.net.MalformedURLException
     */
    public ChangePassword(Stage primaryStage, Map<String, String> LangMap, String password, String IPAddr, String Uname, String IPPort)throws MalformedURLException, IOException{ // 2017.08.10 william IP增加port欄位，預設443
        WordMap=LangMap;
        //Stage ChangePasswordStage = new Stage();
        CurrentIP=IPAddr;
        CurrentUserName=Uname;
        CurrentPasseard=password;  
        CurrentIP_Port = IPPort; // 2017.08.10 william IP增加port欄位，預設443
        
        /************顯示 明碼 暗碼*************/
        EyeClose=new Image("images/CloseEye.png",22,22,false, false);     
        EyeOpen=new Image("images/OpenEye5.png",22,22,false, false);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(0, 0, 10, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        //grid.setGridLinesVisible(true); //開啟或關閉表格的分隔線 
        HBox Header=new HBox();
        /*
        ImageView IV=new ImageView();
        IV.setImage(new Image("images/changepassword.png"));
        IV.setFitHeight(30);
        IV.setFitWidth(30);
        */
        
        Label CPTitle=new Label(WordMap.get("CP_Header"));
        //Header.getChildren().addAll(IV,CPTitle);
        Header.getChildren().addAll(CPTitle);
        Header.setAlignment(Pos.CENTER);       
        Header.setId("Title");
        grid.add(Header, 0, 0, 8, 1);
        
        /*******************目前密碼***************************/
        Label emptylable=new Label("    ");
        Label emptylable2=new Label("    ");
        Label l_OriginalPassword=new Label(WordMap.get("CP_Original")+" :");
        GridPane.setHalignment(l_OriginalPassword, HPos.LEFT);
        l_OriginalPassword.setId("item");
        grid.add(emptylable, 0, 1);
        grid.add(l_OriginalPassword, 1, 1);   
        /************目前密碼**********明碼 與 暗碼 轉換**********/ 
        PasswordField opw = new PasswordField(); 
        opw.setPromptText(WordMap.get("CP_Original"));
        //opw.setId("PwItem");
        opw.setPrefSize(218,35);
        opw.setMaxSize(218,35);
        opw.setMinSize(218,35);
        grid.add(opw, 2, 1);
        grid.add(emptylable2, 3, 1);
    
        // Bind the textField and passwordField text values bidirectionally.
        TextField textField01 = new TextField();
        textField01.setPrefSize(218,35);
        textField01.setMaxSize(218,35);
        textField01.setMinSize(218,35);       
        // Set initial state
        textField01.setManaged(false);
        textField01.setVisible(false);
        grid.add(textField01,2, 1); 
   
        buttonOpw.setGraphic(new ImageView(EyeClose));
        grid.add(buttonOpw,2, 1);           
        buttonOpw.setAlignment(Pos.CENTER);
        
        //win7
        buttonOpw.setTranslateX(219); //移動TextField內的Button位置
        
        //winXP
        //buttonOpw.setTranslateX(200); //移動TextField內的Button位置 
        
        buttonOpw.setPrefHeight(33);
        buttonOpw.setMaxHeight(33);
        buttonOpw.setMinHeight(33);
     
        buttonOpw.setOnAction(new EventHandler<ActionEvent>() {
            @Override        
            public void handle(ActionEvent e) {
            //gatewayMethod();    
            if(opw_changeAfterSaved==true){ 
                
                if (e.getEventType().equals(ActionEvent.ACTION)){                    
                                    
                    textField01.managedProperty().bind(buttonOpw.selectedProperty());
                    textField01.visibleProperty().bind(buttonOpw.selectedProperty());            
                    buttonOpw.setGraphic(new ImageView(EyeOpen));
                    System.out.println("------textField------ACTION----------- \n");                    

              } 
             
             opw_changeAfterSaved=false;
             
            }           
            else{
                
                if (e.getEventType().equals(ActionEvent.ACTION)){                   
                  
                    opw.managedProperty().bind(buttonOpw.selectedProperty().not());
                    opw.visibleProperty().bind(buttonOpw.selectedProperty().not());             
                    buttonOpw.setGraphic(new ImageView(EyeClose));
                    System.out.println("--------pwBox------ACTION-------------- \n");
                
              } 
                
             opw_changeAfterSaved=true;
             
            }
            
            textField01.textProperty().bindBidirectional(opw.textProperty());                       
            System.out.println("opw_changeAfterSaved : "+opw_changeAfterSaved+"\n");
         
        }
        });
     
        /***************新密碼******************/
        Label l_NewPassword=new Label(WordMap.get("CP_New")+" :");
        GridPane.setHalignment(l_NewPassword, HPos.LEFT);
        l_NewPassword.setId("item");
        grid.add(l_NewPassword, 1, 2);     
        final PasswordField npw = new PasswordField(); 
        npw.setPromptText(WordMap.get("CP_New"));
        //npw.setId("PwItem");
        npw.setPrefSize(218,35);//218
        npw.setMaxSize(218,35);
        npw.setMinSize(218,35);
        grid.add(npw, 2, 2);    
        
        /************新密碼**********明碼 與 暗碼 轉換**********/ 
         // Bind the textField and passwordField text values bidirectionally.
        TextField textField02 = new TextField();
        textField02.setPrefSize(218,35);
        textField02.setMaxSize(218,35);
        textField02.setMinSize(218,35);       
        // Set initial state
        textField02.setManaged(false);
        textField02.setVisible(false);
        grid.add(textField02,2, 2); 

        buttonNpw.setGraphic(new ImageView(EyeClose));
        grid.add(buttonNpw,2, 2);   
        buttonNpw.setAlignment(Pos.CENTER);        
        //win7
        buttonNpw.setTranslateX(219); //移動TextField內的Button位置 220
        buttonNpw.setPrefHeight(33);
        buttonNpw.setMaxHeight(33);
        buttonNpw.setMinHeight(33);     
        buttonNpw.setOnAction(new EventHandler<ActionEvent>() {
            @Override        
            public void handle(ActionEvent e) {
            //gatewayMethod();    
            if(npw_changeAfterSaved==true){ 
                
                if (e.getEventType().equals(ActionEvent.ACTION)){                    
                                    
                    textField02.managedProperty().bind(buttonNpw.selectedProperty());
                    textField02.visibleProperty().bind(buttonNpw.selectedProperty());            
                    buttonNpw.setGraphic(new ImageView(EyeOpen));                                     

              } 
             
             npw_changeAfterSaved=false;
             
            }           
            else{
                
                if (e.getEventType().equals(ActionEvent.ACTION)){                   
                  
                    npw.managedProperty().bind(buttonNpw.selectedProperty().not());
                    npw.visibleProperty().bind(buttonNpw.selectedProperty().not());             
                    buttonNpw.setGraphic(new ImageView(EyeClose));
                    
              } 
                
             npw_changeAfterSaved=true;
             
            }
            
            textField02.textProperty().bindBidirectional(npw.textProperty());                       
            System.out.println("npw_changeAfterSaved : "+npw_changeAfterSaved+"\n");
         
        }
        });
          
        //確認新密碼
        Label l_ConfirmPassword=new Label(WordMap.get("CP_Confirm")+" :");
        GridPane.setHalignment(l_ConfirmPassword, HPos.LEFT);
        l_ConfirmPassword.setId("item");
        grid.add(l_ConfirmPassword, 1, 3);
        final PasswordField cpw = new PasswordField(); 
        cpw.setPromptText(WordMap.get("CP_Confirm"));
        //cpw.setId("PwItem");
        cpw.setPrefSize(218,35);
        cpw.setMaxSize(218,35);
        cpw.setMinSize(218,35);
        grid.add(cpw, 2, 3);
        
        /************確認新密碼**********明碼 與 暗碼 轉換**********/        
         // Bind the textField and passwordField text values bidirectionally.
        TextField textField03 = new TextField();
        textField03.setPrefSize(218,35);
        textField03.setMaxSize(218,35);
        textField03.setMinSize(218,35);       
        // Set initial state
        textField03.setManaged(false);
        textField03.setVisible(false);
        grid.add(textField03,2, 3); 

        buttonCpw.setGraphic(new ImageView(EyeClose));
        grid.add(buttonCpw,2, 3); 
        buttonCpw.setAlignment(Pos.CENTER);       
        //win7
        buttonCpw.setTranslateX(219); //移動TextField內的Button位置
        buttonCpw.setPrefHeight(33);
        buttonCpw.setMaxHeight(33);
        buttonCpw.setMinHeight(33);     
        buttonCpw.setOnAction(new EventHandler<ActionEvent>() {
            @Override        
            public void handle(ActionEvent e) {
            //gatewayMethod();    
            if(cpw_changeAfterSaved==true){ 
                
                if (e.getEventType().equals(ActionEvent.ACTION)){                    
                                    
                    textField03.managedProperty().bind(buttonCpw.selectedProperty());
                    textField03.visibleProperty().bind(buttonCpw.selectedProperty());            
                    buttonCpw.setGraphic(new ImageView(EyeOpen));
                    
              } 
             
             cpw_changeAfterSaved=false;
             
            }           
            else{
                
                if (e.getEventType().equals(ActionEvent.ACTION)){                   
                  
                    cpw.managedProperty().bind(buttonCpw.selectedProperty().not());
                    cpw.visibleProperty().bind(buttonCpw.selectedProperty().not());             
                    buttonCpw.setGraphic(new ImageView(EyeClose));
                           
              } 
                
            cpw_changeAfterSaved=true;
             
            }
            
            textField03.textProperty().bindBidirectional(cpw.textProperty());                       
            System.out.println("cpw_changeAfterSaved : "+cpw_changeAfterSaved+"\n");
         
            }
        });
        /***********************確認******************************/
        Button Confirm=new Button(WordMap.get("Confirm"));
        Confirm.setPrefHeight(30);
        Confirm.setOnAction((ActionEvent event) -> {
            
            if(opw.getText()!=null){
                OriginalPassword=opw.getText();
            }else{
                OriginalPassword=""; 
            }
            if(npw.getText()!=null){
                NewPassword=npw.getText();
            }else{
                NewPassword=""; 
            }
            if(cpw.getText()!=null){
                ConfirmPassword=cpw.getText();
            }else{
                ConfirmPassword=""; 
            }
            
            OriginalPassword=opw.getText();
            NewPassword=npw.getText();
            ConfirmPassword=cpw.getText();
            System.out.println("OriginalPassword : "+OriginalPassword+"\n");
            System.out.println("CurrentPasseard : "+CurrentPasseard+"\n");
            System.out.println("NewPassword : "+NewPassword+"\n");
            System.out.println("CurrentPasseard : "+ConfirmPassword+"\n");
            if("English".equals(WordMap.get("SelectedLanguage"))){
                //訊息:變更密碼-原密碼輸入錯誤
                if(!OriginalPassword.equals(CurrentPasseard)){ //若目前密碼與正確登入的密碼不同 跳出視窗通知使用者
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    //alert.setTitle(WordMap.get("CP_Window_Title")+" ： "+WordMap.get("Message_Error_OPW"));
                    alert.setTitle(WordMap.get("CP_Window_Title"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(520,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(520,Region.USE_PREF_SIZE);
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_OPW")+"\n"
                            +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Correct_OPW")
                    );
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK);
                    alert.showAndWait();
                    //alert.show();
                }
                //訊息:變更密碼-新密碼與確認新密碼不一致
                if( (!ConfirmPassword.equals(NewPassword))){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    //alert.setTitle(WordMap.get("CP_Window_Title")+" ： "+WordMap.get("Message_Error_NPW_CPW"));
                    alert.setTitle(WordMap.get("CP_Window_Title"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(550,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(550,Region.USE_PREF_SIZE);
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_NPW_CPW")+"\n"
                            +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Correct_NPW_CPW")
                    );
                    
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK);
                    alert.showAndWait();
                    //alert.show();
                }
                
                if( (NewPassword.isEmpty()) &&  (ConfirmPassword.isEmpty()) ){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    //alert.setTitle(WordMap.get("CP_Window_Title")+" ： "+WordMap.get("Message_Error_NPW_CPW"));
                    alert.setTitle(WordMap.get("CP_Window_Title"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(550,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(550,Region.USE_PREF_SIZE);
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_NPW_CPW")+"\n"
                            +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Correct_NPW_CPW")
                    );
                    
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK);
                    alert.showAndWait();
                    //alert.show();
                }
            }
            else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
                //訊息:變更密碼-原密碼輸入錯誤
                if(!OriginalPassword.equals(CurrentPasseard)){ //若目前密碼與正確登入的密碼不同 跳出視窗通知使用者
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    //alert.setTitle(WordMap.get("CP_Window_Title")+" ： "+WordMap.get("Message_Error_OPW"));
                    alert.setTitle(WordMap.get("CP_Window_Title"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_OPW")+"\n"
                            +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Correct_OPW")
                    );
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK);
                    alert.showAndWait();
                    //alert.show();
                }
                //訊息:變更密碼-新密碼與確認新密碼不一致
                if( (!ConfirmPassword.equals(NewPassword))){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    //alert.setTitle(WordMap.get("CP_Window_Title")+" ： "+WordMap.get("Message_Error_NPW_CPW"));
                    alert.setTitle(WordMap.get("CP_Window_Title"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_NPW_CPW")+"\n"
                            +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Correct_NPW_CPW")
                    );
                    
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK);
                    alert.showAndWait();
                    //alert.show();
                }
                
                if( (NewPassword.isEmpty()) &&  (ConfirmPassword.isEmpty())  ){                   
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    //alert.setTitle(WordMap.get("CP_Window_Title")+" ： "+WordMap.get("Message_Error_NPW_CPW"));
                    alert.setTitle(WordMap.get("CP_Window_Title"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_NPW_CPW")+"\n"
                            +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Correct_NPW_CPW")
                    );
                    
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK);
                    alert.showAndWait();
                    //alert.show();
                }
            }
            /*
            if((NewPassword == null ? ConfirmPassword == null : CurrentPasseard.equals(OriginalPassword))&&(NewPassword.equals(ConfirmPassword))){
            //if((CurrentPasseard == null ? OriginalPassword == null : CurrentPasseard.equals(OriginalPassword))&&(NewPassword.equals(ConfirmPassword))){
            //if((NewPassword!=null)&&(ConfirmPassword!=null)&&(CurrentPasseard==OriginalPassword)&&(NewPassword==ConfirmPassword)){
            DoChangePassword(CurrentIP, CurrentUserName, CurrentPasseard, OriginalPassword, NewPassword, ConfirmPassword);
            
            }
            */
            if( (NewPassword!=null) ){
                
                if( (CurrentPasseard.equals(OriginalPassword)) && (NewPassword.equals(ConfirmPassword)) ){
                    try {
                        //System.out.print("(NewPassword!=null)\n");
                        DoChangePassword(CurrentIP, CurrentUserName, CurrentPasseard, OriginalPassword, NewPassword, ConfirmPassword,CurrentIP_Port); // 2017.08.10 william IP增加port欄位，預設443
                    } catch (IOException ex) {
                        Logger.getLogger(ChangePassword.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
            
            
            
            //ChangePasswordStage.close();
        });
        
        //取消
        Button Cancel=new Button(WordMap.get("Cancel"));
        Cancel.setPrefHeight(30);
        Cancel.setOnAction((ActionEvent event) -> {
            ChangePasswordStage.close();
        });
/***以下****Start*******Enter & Page Down & Page Up 功能**********************/
         //Enter key to next TextField
        opw.setOnKeyPressed((event)-> {                    
            if (event.getCode() == KeyCode.ENTER){    
                npw.requestFocus();
                textField02.requestFocus();                
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
               npw.requestFocus();
               textField02.requestFocus();
               event.consume();
           }
            if(event.getCode() == KeyCode.TAB){
               npw.requestFocus();
               textField02.requestFocus();
               event.consume();
           }
            if(event.getCode() == KeyCode.PAGE_UP){
               Cancel.requestFocus();
               event.consume();
           }
        });
         textField01.setOnKeyPressed((event)-> {                    
            if (event.getCode() == KeyCode.ENTER){    
                npw.requestFocus();
                textField02.requestFocus();             
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
               npw.requestFocus();
               textField02.requestFocus();
               event.consume();
           }
            if(event.getCode() == KeyCode.TAB){
               npw.requestFocus();
               textField02.requestFocus();
               event.consume();
           }
            if(event.getCode() == KeyCode.PAGE_UP){
               Cancel.requestFocus();
               event.consume();
           }
        });
        
        //Enter key to next TextField
        npw.setOnKeyPressed((event)-> {                    
            if (event.getCode() == KeyCode.ENTER){    
                cpw.requestFocus();
                textField03.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
               cpw.requestFocus();
               textField03.requestFocus();
               event.consume();
           }
            if(event.getCode() == KeyCode.TAB){
               cpw.requestFocus();
               textField03.requestFocus();
               event.consume();
           }
            if(event.getCode() == KeyCode.PAGE_UP){
               opw.requestFocus();
               textField01.requestFocus();
               event.consume();
           }
        });
         //Enter key to next TextField
        textField02.setOnKeyPressed((event)-> {                    
            if (event.getCode() == KeyCode.ENTER){    
                cpw.requestFocus();
                textField03.requestFocus();
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
               cpw.requestFocus();
               textField03.requestFocus();
               event.consume();
           }
            if(event.getCode() == KeyCode.TAB){
               cpw.requestFocus();
               textField03.requestFocus();
               event.consume();
           }
            if(event.getCode() == KeyCode.PAGE_UP){
               opw.requestFocus();
               textField01.requestFocus();
               event.consume();
           }
        });
        //Enter key to next TextField
        cpw.setOnKeyPressed((event)-> {                    
            if (event.getCode() == KeyCode.ENTER){    
                Confirm.requestFocus();                    
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
               Confirm.requestFocus();
               event.consume();
           }
            if(event.getCode() == KeyCode.TAB){
               Confirm.requestFocus();
               event.consume();
           }
            if(event.getCode() == KeyCode.PAGE_UP){
               npw.requestFocus();
               textField02.requestFocus();
               event.consume();
           }
        });
          //Enter key to next TextField
        textField03.setOnKeyPressed((event)-> {                    
            if (event.getCode() == KeyCode.ENTER){    
                Confirm.requestFocus();                    
                event.consume();
            }
            if(event.getCode() == KeyCode.PAGE_DOWN){
               Confirm.requestFocus();
               event.consume();
           }
            if(event.getCode() == KeyCode.TAB){
               Confirm.requestFocus();
               event.consume();
           }
            if(event.getCode() == KeyCode.PAGE_UP){
               npw.requestFocus();
               textField02.requestFocus();
               event.consume();
           }
        });
        //Enter Key-進入功能 ; PAGE_DOWN Key - down ; PAGE_UP Key - up 
        Confirm.setOnKeyPressed((event)-> {
           //event.get           
           if(event.getCode() == KeyCode.ENTER){
                Confirm.setId("Button_actionON");
                Confirm.arm();
                PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                pause.setOnFinished(e -> {
                    Confirm.setId("button");
                    Confirm.disarm();       
                    Confirm.fire(); 
                });                     
                pause.play();
                event.consume();
           }           
           if(event.getCode() == KeyCode.PAGE_DOWN){
               Cancel.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.TAB){
               Cancel.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_UP){
               cpw.requestFocus();
               textField03.requestFocus();
               event.consume();
           }
        });
        Confirm.setOnKeyReleased((event)-> {
             Confirm.setId("button");
        });
        //Enter Key-進入功能 ; PAGE_DOWN Key - down ; PAGE_UP Key - up 
        Cancel.setOnKeyPressed((event)-> {
           //event.get           
           if(event.getCode() == KeyCode.ENTER){
                Cancel.setId("Button_actionON");
                Cancel.arm();
                PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                pause.setOnFinished(e -> {
                    Cancel.setId("button");
                    Cancel.disarm();       
                    Cancel.fire(); 
                });                     
                pause.play();
                event.consume();
           }           
           if(event.getCode() == KeyCode.PAGE_DOWN){
               opw.requestFocus();
               textField01.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.TAB){
               opw.requestFocus();
               textField01.requestFocus();
               event.consume();
           }
           if(event.getCode() == KeyCode.PAGE_UP){
               Confirm.requestFocus();
               event.consume();
           }
        });
        Cancel.setOnKeyReleased((event)-> {
             Cancel.setId("button");
        });
/***以上****End************Enter & Page Down & Page Up 功能**********************/        
        
        HBox OptionBox=new HBox(Cancel, Confirm);
        OptionBox.setAlignment(Pos.CENTER_LEFT);
        OptionBox.setSpacing(40);
        //OptionBox.setStyle("-fx-padding: 10;");
        OptionBox.setPadding(new Insets(2, 0, 15, 0));//設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        grid.add(OptionBox, 2, 4,3,1);
        
        Scene ChangePasswordScene = new Scene(grid);
        ChangePasswordScene.getStylesheets().add("ChangePassword.css");
        ChangeLabelLang(CPTitle, l_OriginalPassword, opw, l_ConfirmPassword, npw, l_NewPassword, cpw);
        ChangeButtonLang(Confirm,Cancel);
        
        switch (GB.JavaVersion) {
            case 0:
                ChangePasswordStage.getIcons().add(new Image("images/Icon2.png"));
                break;
            case 1:
                ChangePasswordStage.getIcons().add(new Image("images/MCIcon2.png"));
                break;                
            default:
                break;
        }
        ChangePasswordStage.setTitle(WordMap.get("CP_Window_Title"));
        ChangePasswordStage.setScene(ChangePasswordScene);
        ChangePasswordStage.initStyle(StageStyle.DECORATED);
        ChangePasswordStage.initModality(Modality.WINDOW_MODAL); // 2017.09.18 william 錯誤修改(5)-多視窗顯示 NONE -> WINDOW_MODAL
        ChangePasswordStage.initOwner(primaryStage);
        //ChangePasswordStage.setResizable(false);//將視窗放大,關閉
        ChangePasswordStage.show();
        
    }
    
    public void DoChangePassword(String address, String Uname, String CurrentPassword, String opw, String npw, String cpw,String port)throws MalformedURLException, IOException{ // 2017.08.10 william IP增加port欄位，預設443
        URL url;
        CurrentIP=address;
        CurrentUserName=Uname; //load CurrentUserName
        CurrentPasseard=CurrentPassword;//load CurrentPasseard
        OriginalPassword=opw;
        NewPassword=npw;
        ConfirmPassword=cpw;
        CurrentIP_Port = port; // 2017.08.10 william IP增加port欄位，預設443
       
        JSONObject obj3 =new JSONObject();        
        obj3.put("AccountName", CurrentUserName);
        obj3.put("OldPassword", CurrentPasseard); 
        obj3.put("NewPassword", NewPassword);            

        String UpdataPassword=obj3.toString();
        System.out.println("UpdataPassword : "+UpdataPassword+"\n"); 
     
        try{
           
            url = new URL("https://"+CurrentIP+":"+CurrentIP_Port+"/vdi/"+"password"); //"https://"+CurrentIP+"/vdi/"+"/user/"+"/vd/"+uniqueKey
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  //建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線
            conn.setRequestMethod("POST");//利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
            conn.setDoOutput(true);
            conn.setDoInput(true);
	    conn.setRequestProperty("Accept", "application/json");//"application/json"
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Content-Type", "application/json");//設置發送數據的格式("Content-Type", "application/json; charset=UTF-8")
            conn.setRequestProperty("Content-length", String.valueOf(UpdataPassword.getBytes().length));//("Content-length", String.valueOf(query.length()))
            conn.setRequestProperty("Connection", "Keep-Alive");//維持長連接
            //conn.connect();    
            conn.setUseCaches (false);
            conn.setInstanceFollowRedirects(true);
            
            try (OutputStream out3 = conn.getOutputStream()) {
                out3.write(UpdataPassword.getBytes());
                out3.flush();
                out3.close();
                System.out.println("DoChangePassword-output out : "+out3+"\n");
                
                System.out.println("DoChangePassword-URL : "+url+"\n"); 
                //System.out.println("SetVDOnline-conn.getOutputStream() : "+conn.getOutputStream()+"\n");
                System.out.println("DoChangePassword-Resp Code : "+conn.getResponseCode()+"\n");
                System.out.println("DoChangePassword-Resp Message:"+ conn.getResponseMessage()+"\n");                   

                InputStream is3 = conn.getInputStream(); 
                System.out.println("DoChangePassword-conn.getInputStream() : "+is3+"\n"); 
                StringBuilder sb3;
                String line3;  

                try (BufferedReader br3 = new BufferedReader(new InputStreamReader(is3))) {              
                    sb3 = new StringBuilder("");
                    while ((line3 = br3.readLine()) != null) {            
                        sb3.append(line3);         
                    }                 
                    //System.out.println("SetVDOnline-return br: "+br2.readLine()+"\n");
                    System.out.println("DoChangePassword-return sb: "+sb3.toString()+"\n");         
                    br3.close();
                }    

                if(conn.getResponseCode()==200){

                    ChangePasswordStage.close();
                }
                conn.disconnect();      
            }            
            
        } catch (MalformedURLException e) {

        } catch (IOException e) {    

        } 
        
    }
    
     /****變更Label字型****字型:中文-標楷體,英文:Times New Roman****/
     public void ChangeLabelLang(Label title, Label lab01, PasswordField pf01, Label lab03, PasswordField pf02, Label lab05, PasswordField pf03){
        //Label "Password"
        if("English".equals(WordMap.get("SelectedLanguage"))){
            title.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
            lab01.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
            pf01.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
            lab03.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
            pf02.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
            lab05.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
            pf03.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/
            //winXP 僅能用中文名稱
            title.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-weight: 900; ");
            lab01.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-weight: 900; ");
            pf01.setStyle("-fx-font-family: '微軟正黑體'; ");
            lab03.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-weight: 900; ");
            pf02.setStyle("-fx-font-family: '微軟正黑體'; ");
            lab05.setStyle("-fx-font-family: '微軟正黑體'; -fx-font-weight: 900; ");
            pf03.setStyle("-fx-font-family: '微軟正黑體';  ");
            if(!GB.winXP_ver) {
                //win7以上僅能用英文名稱
                title.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-weight: 900; ");
                lab01.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-weight: 900; ");
                pf01.setStyle("-fx-font-family:'Microsoft JhengHei'; ");
                lab03.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-weight: 900; ");
                pf02.setStyle("-fx-font-family:'Microsoft JhengHei';  ");
                lab05.setStyle("-fx-font-family:'Microsoft JhengHei'; -fx-font-weight: 900; ");
                pf03.setStyle("-fx-font-family:'Microsoft JhengHei';");
           }
        }
       
     }
    /****變更button字型****字型:中文-標楷體,英文:Times New Roman****/
      public void ChangeButtonLang(Button but01, Button but02){
        //Button "確定" "取消"
        if("English".equals(WordMap.get("SelectedLanguage"))){
            but01.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
            but02.setStyle("-fx-font-family:'Times New Roman';-fx-font-weight: 900;");
          
        }        
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
         /******下面 若無法讀取'標楷體'中文字型 ,則自動讀去下一個DFKai-SB英文字型*******/
            //winXP 僅能用中文名稱
            but01.setStyle("-fx-font-family: '微軟正黑體';-fx-font-weight: 900;");
            but02.setStyle("-fx-font-family: '微軟正黑體';-fx-font-weight: 900;");
            if(!GB.winXP_ver) {
                //win7以上僅能用英文名稱
                but01.setStyle("-fx-font-family:'Microsoft JhengHei';-fx-font-weight: 900;");
                but02.setStyle("-fx-font-family:'Microsoft JhengHei';-fx-font-weight: 900;");
            }
         
        }
        
     }
    
    
}
