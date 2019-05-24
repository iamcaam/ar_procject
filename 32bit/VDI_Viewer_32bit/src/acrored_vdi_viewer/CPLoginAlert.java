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
import javafx.scene.layout.Region;

/**
 *
 * @author victor
 */
public class CPLoginAlert {
    
    /***    顯示文字對應表    ***/
    public Map<String, String> WordMap=new HashMap<>();
    
    
    public CPLoginAlert(Map<String, String> LangMap){
        WordMap=LangMap;
    }
    
    public GB GB; // 2017.10.27 william 版本判斷
    
    /****************跳出警告訊息:401 403 404*******************/ 
    public void CPLoginAlertChange(int conn){
        if(conn!=0&&conn!=200) { //  2017.09.18 william 錯誤修改(4) 其他未知的錯誤  
            if("English".equals(WordMap.get("SelectedLanguage"))){
                //若帳號,密碼錯誤 回傳401 ,則跳出視窗通知使用者更改.
                if(conn == 401){
                    //訊息:登入-帳號或密碼錯誤
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
                    alert.setTitle(WordMap.get("CP_Window_Title"));
                    alert.setHeaderText(null);
                
                    if(!GB.winXP_ver) {
                        //for Win7-Dialog   
                        alert.getDialogPane().setMinSize(580,Region.USE_PREF_SIZE);
                        alert.getDialogPane().setMaxSize(580,Region.USE_PREF_SIZE); 
                    }
                    else {
                        //for XP-Dialog
                        alert.getDialogPane().setMinSize(620,Region.USE_PREF_SIZE);
                        alert.getDialogPane().setMaxSize(620,Region.USE_PREF_SIZE);                     
                    }
  
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_CP_IP_401")+"\n"
                                     +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_User_PW")
                    );
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                    alert.showAndWait();
                    //alert.show();
                }
                //若回傳403 ,則跳出視窗說明:使用者鎖定.
                else if(conn == 403){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("CP_Window_Title"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(520,Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMaxSize(520,Region.USE_PREF_SIZE);            
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_CP_IP_403")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                    alert.showAndWait();    
                    //alert.show();
                }
                //若回傳404 ,則跳出視窗說明:伺服器不存在 或 伺服器未開機.
                else if(conn == 404){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle(WordMap.get("CP_Window_Title"));
                    alert.setHeaderText(null);
                
                    if(!GB.winXP_ver) {
                        //for Win7-Dialog
                        alert.getDialogPane().setMinSize(600,Region.USE_PREF_SIZE);
                        alert.getDialogPane().setMaxSize(600,Region.USE_PREF_SIZE);                    
                    }
                    else {
                        //for XP-Dialog
                        alert.getDialogPane().setMinSize(680,Region.USE_PREF_SIZE);
                        alert.getDialogPane().setMaxSize(680,Region.USE_PREF_SIZE);                    
                    }

                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_CP_IP_404")+"\n"                                            
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+"\n"
                                    +"   "+" 1. "+WordMap.get("Message_Error_IP_04")+"\n"
                                    +"   "+" 2. "+WordMap.get("Message_Error_IP_05")
                    );
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                    alert.showAndWait();
                    //alert.show();
                }
                //  2017.08.10 william 400錯誤處理，若回傳400 ,則跳出視窗說明:Not Found 請求失敗.
                else if(conn == 400){
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
                    alert.setTitle(WordMap.get("CP_Window_Title"));
                    alert.setHeaderText(null);       
                    if(GB.winXP_ver) {
                        //for XP-Dialog
                        alert.getDialogPane().setMinSize(500,Region.USE_PREF_SIZE);
                        alert.getDialogPane().setMaxSize(500,Region.USE_PREF_SIZE);                          
                    }
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_CP_IP_401")+"\n"
                                     +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_User_PW")
                    );
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                    alert.showAndWait();
                    //alert.show();
                }
                //若回傳403 ,則跳出視窗說明:使用者鎖定.
                else if(conn == 403){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                    alert.setTitle(WordMap.get("CP_Window_Title"));
                    alert.setHeaderText(null);
                    alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);           
                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_CP_IP_403")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                    );           
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                    alert.showAndWait();    
                    //alert.show();
                }
                //若回傳404 ,則跳出視窗說明:伺服器不存在 或 伺服器未開機.
                else if(conn == 404){
                    //訊息:登入-使用者鎖定
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle(WordMap.get("CP_Window_Title"));
                    alert.setHeaderText(null);   
                    if(!GB.winXP_ver) {
                        //for Win7-Dialog
                        alert.getDialogPane().setMinSize(500,Region.USE_PREF_SIZE);
                        alert.getDialogPane().setMaxSize(500,Region.USE_PREF_SIZE);                     
                    }
                    else {
                        //for XP-Dialog
                        alert.getDialogPane().setMinSize(600,Region.USE_PREF_SIZE);
                        alert.getDialogPane().setMaxSize(600,Region.USE_PREF_SIZE);                    
                    }


                    alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_CP_IP_404")+"\n"                                            
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "
                                    +" 1. "+WordMap.get("Message_Error_IP_04")+"\n"
                                    +"　　　  "+" 2. "+WordMap.get("Message_Error_IP_05")
                    );
                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                    alert.getButtonTypes().setAll(buttonTypeOK); 
                    alert.showAndWait();
                    //alert.show();
                }
                //  2017.08.10 william 400錯誤處理，若回傳400 ,則跳出視窗說明:Not Found 請求失敗.
                else if(conn == 400){
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
    
    /********* Login testError 錯誤訊息 ************/
    public void CPLtestErrorAlert(){        
        if("English".equals(WordMap.get("SelectedLanguage"))){
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("CP_Window_Title"));
            alert_error.setHeaderText(null);
            alert_error.getDialogPane().setMinSize(830,Region.USE_PREF_SIZE);
            alert_error.getDialogPane().setMaxSize(830,Region.USE_PREF_SIZE);
            alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_CP_IP_02")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "+"\n"
                +"                    "+" 1. "+WordMap.get("Message_Error_IP_03")+"\n"
                +"                    "+" 2. "+WordMap.get("Message_Error_IP_02")
            );                                    
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK);
            alert_error.showAndWait();
            //alert_error.show();
        }
        else { //if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("CP_Window_Title"));
            alert_error.setHeaderText(null);

            if(!GB.winXP_ver) {
                //for Win7-Dialog
                alert_error.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
                alert_error.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE);            
            }
            else {
                //for XP-Dialog
                alert_error.getDialogPane().setMinSize(600,Region.USE_PREF_SIZE);
                alert_error.getDialogPane().setMaxSize(600,Region.USE_PREF_SIZE);            
            }

            alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_CP_IP_02")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "
                +" 1. "+WordMap.get("Message_Error_IP_03")+"\n"
                +"　　　  "+" 2. "+WordMap.get("Message_Error_IP_02")
            );                                    
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK);
            alert_error.showAndWait();
            //alert_error.show();
        }        
    }
    
    
}
