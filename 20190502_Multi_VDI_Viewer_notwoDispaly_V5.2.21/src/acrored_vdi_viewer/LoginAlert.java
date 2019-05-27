/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;


import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author victor
 */
// Input：,  Output： , 功能： 登入與快照登入警告視窗(邏輯架構與CPLoginAlert.java相同)
public class LoginAlert {
    
    /***    顯示文字對應表    ***/
    public Map<String, String> WordMap=new HashMap<>();
    
    
    public LoginAlert(Map<String, String> LangMap){
        WordMap=LangMap;
    }
    
    /****************跳出警告訊息:401 403 404*******************/
    public void AlertChangeLang(int conn, Stage MainStage){
        if(conn!=0&&conn!=200) { //  2017.09.18 william 錯誤修改(4) 其他未知的錯誤  
            if("English".equals(WordMap.get("SelectedLanguage"))){
                //若帳號,密碼錯誤 回傳401 ,則跳出視窗通知使用者更改.
                if(conn == 401){
                    //訊息:登入-帳號或密碼錯誤
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                
                    //for Win7-Dialog          
                    alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE); 
                    /*
                    //for XP-Dialog
                    alert.getDialogPane().setMinSize(570,120);
                    alert.getDialogPane().setMaxSize(570,120);
                    */
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Input_User_PW")+"\n"
                                     +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_User_PW")
                    );
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();
                    //alert.show();
                }
                //若回傳403 ,則跳出視窗說明:使用者鎖定.
                else if(conn == 403){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(520,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(520,Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Lock")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait(); 
                    //alert.show();
                }
                //若回傳404 ,則跳出視窗說明:伺服器不存在 或 伺服器未開機.
                else if(conn == 404){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                
                    //for Win7-Dialog
                    alert.getDialogPane().setMinSize(600,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(600,Region.USE_PREF_SIZE);
                    /*
                    //for XP-Dialog
                    alert.getDialogPane().setMinSize(680,130);
                    alert.getDialogPane().setMaxSize(680,130);
                    */
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_404")+"\n"                                            
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+"\n"
                                    +"   "+" 1. "+WordMap.get("Message_Error_IP_04")+"\n"
                                    +"   "+" 2. "+WordMap.get("Message_Error_IP_05")
                    );
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();
                    //alert.show();

                }
                //  2017.08.10 william 400錯誤處理，若回傳400 ,則跳出視窗說明:Not Found 請求失敗.
                else if(conn == 400){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                
                    //for Win7-Dialog          
                    alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE); 
                    /*
                    //for XP-Dialog
                    alert.getDialogPane().setMinSize(570,120);
                    alert.getDialogPane().setMaxSize(570,120);
                    */
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Bad_Request")+"\n"
                                     +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_400")
                    );
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                    alert.showAndWait();
                    //alert.show();            
                }
                //  2017.09.18 william 錯誤修改(4) 其他未知的錯誤
                else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                
                    //for Win7-Dialog          
                    alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE); 
                    /*
                    //for XP-Dialog
                    alert.getDialogPane().setMinSize(570,120);
                    alert.getDialogPane().setMaxSize(570,120);
                    */
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Unknown_system_error")+" ( "+conn+" ) "+"\n"
                                     +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_400")
                    );
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                    alert.showAndWait();
                    //alert.show();             
                }                
            }                   
        
            else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
                //若帳號,密碼錯誤 回傳401 ,則跳出視窗通知使用者更改.
                if(conn == 401){
                    //訊息:登入-帳號或密碼錯誤
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Input_User_PW")+"\n"
                                     +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_User_PW")
                    );
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();
                    //alert.show();
                }
                //若回傳403 ,則跳出視窗說明:使用者鎖定.
                else if(conn == 403){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Lock")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();
                    //alert.show();
                }
                //若回傳404 ,則跳出視窗說明:伺服器不存在 或 伺服器未開機.
                else if(conn == 404){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                
                    //for Win7-Dialog
                    alert.getDialogPane().setMinSize(500,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(500,Region.USE_PREF_SIZE);
                    /*
                    //for XP-Dialog
                    alert.getDialogPane().setMinSize(600,130);
                    alert.getDialogPane().setMaxSize(600,130);
                    */
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_404")+"\n"                                            
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "
                                    +" 1. "+WordMap.get("Message_Error_IP_04")+"\n"
                                    +"　　　  "+" 2. "+WordMap.get("Message_Error_IP_05")
                    );
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();
                    //alert.show();
                }
                //  2017.08.10 william 400錯誤處理，若回傳400 ,則跳出視窗說明:Not Found 請求失敗.
                else if(conn == 400){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                
                    //for Win7-Dialog          
                    alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE); 
                    /*
                    //for XP-Dialog
                    alert.getDialogPane().setMinSize(570,120);
                    alert.getDialogPane().setMaxSize(570,120);
                    */
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Bad_Request")+"\n"
                                     +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_400")
                    );
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                    alert.showAndWait();
                    //alert.show();            
                }
                //  2017.09.18 william 錯誤修改(4) 其他未知的錯誤
                else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                
                    //for Win7-Dialog          
                    alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE); 
                    /*
                    //for XP-Dialog
                    alert.getDialogPane().setMinSize(570,120);
                    alert.getDialogPane().setMaxSize(570,120);
                    */
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Unknown_system_error")+" ( "+conn+" ) "+"\n"
                                     +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_400")
                    );
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                    alert.showAndWait();
                    //alert.show();             
                }                
            }
        }
    }
    
    /*************錯誤訊息: 503 406 412 500 403 417 410********************/
    public void SetVDOnline_AlertChangeLang(int conn, Stage MainStage){
        if(conn!=0&&conn!=200) { //  2017.09.18 william 錯誤修改(4) 其他未知的錯誤   
            if("English".equals(WordMap.get("SelectedLanguage"))){
            
                //若回傳403 ,則跳出視窗說明:使用者鎖定.
                if(conn == 503){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(520,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(520,Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VD_503")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();   
                    //alert.show();
                }
                //若回傳403 ,則跳出視窗說明:使用者鎖定.
                else if(conn == 406){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(520,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(520,Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VD_406")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait(); 
                    //alert.show();
                }
                //若回傳403 ,則跳出視窗說明:使用者鎖定.
                else if(conn == 412){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(520,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(520,Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Lock")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_VD_412")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();   
                    //alert.show();
                }
                //若回傳403 ,則跳出視窗說明:使用者鎖定.
                else if(conn == 500){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(520,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(520,Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VD_500")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();    
                    //alert.show();
                }
                //若回傳403 ,則跳出視窗說明:使用者鎖定.
                else if(conn == 403){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(520,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(520,Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VD_403")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();   
                    //alert.show();
                }
                //若回傳403 ,則跳出視窗說明:使用者鎖定.
                else if(conn == 417){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(520,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(520,Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VD_417")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();   
                    //alert.show();
                }
                //若回傳403 ,則跳出視窗說明:使用者鎖定.
                else if(conn == 410){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(520,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(520,Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VD_410")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();
                    //alert.show();
                }
                else if(conn == 428) {
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(520,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(520,Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("Message_Error_VD_428"));           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                    alert.showAndWait();
                    //alert.show();
                }               
                else if(conn == 429) {
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(520,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(520,Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("Message_Error_VD_429"));           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                    alert.showAndWait();
                    //alert.show();
                }     
                // 2018.12.12 新增 431NoVGA 和第一次開機
                else if(conn == 431) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VD_431")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();  
                }          
                // 2018.12.28 block 3D vd
                else if(conn == 451) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VD_loginFail")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Suggest_Error_451")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();  
                }          
                else if(conn == 411){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(520,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(520,Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VD_411")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();   
                    //alert.show();
                }                
                //  2017.09.18 william 錯誤修改(4) 其他未知的錯誤
                else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                
                    //for Win7-Dialog          
                    alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE); 
                    /*
                    //for XP-Dialog
                    alert.getDialogPane().setMinSize(570,120);
                    alert.getDialogPane().setMaxSize(570,120);
                    */
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Unknown_system_error")+" ( "+conn+" ) "+"\n"
                                     +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_400")
                    );
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                    alert.showAndWait();
                    //alert.show();             
                }                 
            
            }                   
        
            else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
            
                //若回傳403 ,則跳出視窗說明:使用者鎖定.
                if(conn == 503){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VD_503")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();
                    //alert.show();
                }
                //若回傳403 ,則跳出視窗說明:使用者鎖定.
                else if(conn == 406){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VD_406")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();  
                    //alert.show();
                }
                //若回傳403 ,則跳出視窗說明:使用者鎖定.
                else if(conn == 412){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VD_412")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();   
                    //alert.show();
                }
                //若回傳403 ,則跳出視窗說明:使用者鎖定.
                else if(conn == 500){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VD_500")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();  
                    //alert.show();
                }
                //若回傳403 ,則跳出視窗說明:使用者鎖定.
                else if(conn == 403){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VD_403")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();  
                    //alert.show();
                }
                //若回傳403 ,則跳出視窗說明:使用者鎖定.
                else if(conn == 417){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VD_417")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();
                    //alert.show();
                }
                //若回傳403 ,則跳出視窗說明:使用者鎖定.
                else if(conn == 410){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VD_410")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();  
                    //alert.show();
                }
                else if(conn == 428) {
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(520,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(520,Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("Message_Error_VD_428"));           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                    alert.showAndWait();
                    //alert.show();
                }               
                else if(conn == 429) {
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(520,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(520,Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("Message_Error_VD_429"));           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                    alert.showAndWait();
                    //alert.show();
                }        
                // 2018.12.12 新增 431NoVGA 和第一次開機
                else if(conn == 431) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VD_431")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();  
                }            
                // 2018.12.28 block 3D vd
                else if(conn == 451) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VD_loginFail")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Suggest_Error_451")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();  
                }                
                else if(conn == 411){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(520,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(520,Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VD_411")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                
                    /*** alert style ***/
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("myDialogs.css");
                    dialogPane.getStyleClass().add("myDialog");

                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
                
                    alert.showAndWait();   
                    //alert.show();
                }                 
                //  2017.09.18 william 錯誤修改(4) 其他未知的錯誤
                else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
                    alert.setTitle(WordMap.get("Message_Login"));
                    alert.setHeaderText(null);
                
                    //for Win7-Dialog          
                    alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE); 
                    /*
                    //for XP-Dialog
                    alert.getDialogPane().setMinSize(570,120);
                    alert.getDialogPane().setMaxSize(570,120);
                    */
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Unknown_system_error")+" ( "+conn+" ) "+"\n"
                                     +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_400")
                    );
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                    alert.showAndWait();
                    //alert.show();             
                }                 
            }
        }
    }
    
    /********* Login testError 錯誤訊息 ************/
    public void LogintestErrorAlert(Stage MainStage){        
        if("English".equals(WordMap.get("SelectedLanguage"))){
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("Message_Login"));
            alert_error.setHeaderText(null);
            alert_error.getDialogPane().setMinSize(700,Region.USE_PREF_SIZE);
            alert_error.getDialogPane().setMaxSize(700,Region.USE_PREF_SIZE);
            alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_IP_06")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+ " ： " + WordMap.get("Message_Error_IP_03")
//                +"                    "+" 1. "+WordMap.get("Message_Error_IP_03")+"\n"
//                +"                    "+" 2. "+WordMap.get("Message_Error_IP_02")
            );                                    
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK);
            
            /*** alert style ***/
            DialogPane dialogPane = alert_error.getDialogPane();
            dialogPane.getStylesheets().add("myDialogs.css");
            dialogPane.getStyleClass().add("myDialog");

            alert_error.initModality(Modality.APPLICATION_MODAL);
            alert_error.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
            
            alert_error.showAndWait();
            //alert_error.show();
        }
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("Message_Login"));
            alert_error.setHeaderText(null);
            alert_error.getDialogPane().setMinSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);
            alert_error.getDialogPane().setMaxSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);
            alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_IP_06")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest") + " ： " + WordMap.get("Message_Error_IP_03")
//                +" 1. "+WordMap.get("Message_Error_IP_03")+"\n"
//                +"　　　  "+" 2. "+WordMap.get("Message_Error_IP_02")
            );                                    
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK);
            
            /*** alert style ***/
            DialogPane dialogPane = alert_error.getDialogPane();
            dialogPane.getStylesheets().add("myDialogs.css");
            dialogPane.getStyleClass().add("myDialog");

            alert_error.initModality(Modality.APPLICATION_MODAL);
            alert_error.initOwner(MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
            
            alert_error.showAndWait();
            //alert_error.show();
        }         
    }
    
}
