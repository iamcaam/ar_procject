/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author root
 */
public class SystemAlert {
    
    
    /***    顯示文字對應表    ***/
    public Map<String, String> WordMap=new HashMap<>();
    
    
    public SystemAlert(Map<String, String> LangMap){
        WordMap=LangMap;
    }
    
    /********* CheckSystemButton IP 輸入格式 錯誤訊息 ************/
    public void CheckSystemResultAlert(Stage MainStage){       
        
        if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){       
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("ThinClient"));
            alert_error.setHeaderText(null);
            alert_error.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("TC_Check_Error_IP")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_07")
                );
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK); 
            
            /*** alert style ***/
            DialogPane dialogPane = alert_error.getDialogPane();
            dialogPane.getStylesheets().add("myDialogs.css");
            dialogPane.getStyleClass().add("myDialog");

            alert_error.initModality(Modality.APPLICATION_MODAL);
            alert_error.initOwner( MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係

            alert_error.showAndWait();
            //alert_error.show();

        }
        if("English".equals(WordMap.get("SelectedLanguage"))){
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("ThinClient"));
            alert_error.setHeaderText(null);
            alert_error.getDialogPane().setMinSize(550,Region.USE_PREF_SIZE);
            alert_error.getDialogPane().setMaxSize(550,Region.USE_PREF_SIZE);
            alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VM_IP_01")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("TC_Check_Error_IP")
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
    
    /********* CheckSystemButton -Ping- testError 錯誤訊息 ************/
    public void CheckSystem_testError_Alert(Stage MainStage){ 
        
        if("English".equals(WordMap.get("SelectedLanguage"))){
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("ThinClient"));
            alert_error.setHeaderText(null);
            alert_error.getDialogPane().setMinSize(830,Region.USE_PREF_SIZE);
            alert_error.getDialogPane().setMaxSize(830,Region.USE_PREF_SIZE);
            alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("TC_Check_Error")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "+"\n"
                +"                    "+" 1. "+WordMap.get("Message_Error_IP_03")+"\n"
                +"                    "+" 2. "+WordMap.get("Message_Error_IP_02")
            );                                    
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK);
            
            /*** alert style ***/
            DialogPane dialogPane = alert_error.getDialogPane();
            dialogPane.getStylesheets().add("myDialogs.css");
            dialogPane.getStyleClass().add("myDialog");

            alert_error.initModality(Modality.APPLICATION_MODAL);
            alert_error.initOwner(MainStage.getScene().getWindow());  //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
            
            alert_error.showAndWait();
            //alert_error.show();
        }
        if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
            Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
            alert_error.setTitle(WordMap.get("ThinClient"));
            alert_error.setHeaderText(null);

            //for Win7-Dialog
            alert_error.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
            alert_error.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE);
            alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("TC_Check_Error")+"\n"                                            
                +"\n"+WordMap.get("Message_Suggest")+" ： "
                +" 1. "+WordMap.get("Message_Error_IP_03")+"\n"
                +"　　　  "+" 2. "+WordMap.get("Message_Error_IP_02")
            );                                    
            ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
            alert_error.getButtonTypes().setAll(buttonTypeOK);
            
            /*** alert style ***/
            DialogPane dialogPane = alert_error.getDialogPane();
            dialogPane.getStylesheets().add("myDialogs.css");
            dialogPane.getStyleClass().add("myDialog");

            alert_error.initModality(Modality.APPLICATION_MODAL);
            alert_error.initOwner(MainStage.getScene().getWindow());  //有沒有這一行很重要，決定這兩個視窗是否有從屬關係
            
            alert_error.showAndWait();
            //alert_error.show();
        }        
        
    }
        
    /********* CheckSystemButton -update-  錯誤訊息 ************/
    public void CheckSystem_updateFailed_Alert(Stage MainStage){ 
        
        Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
        alert_error.setTitle(WordMap.get("ThinClient"));
        alert_error.setHeaderText(null);
        alert_error.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("TC_Update_Error")+"\n"                                            
            +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("TC_Update_Error_Suggest")
            );
        ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
        alert_error.getButtonTypes().setAll(buttonTypeOK); 

        /*** alert style ***/
        DialogPane dialogPane = alert_error.getDialogPane();
        dialogPane.getStylesheets().add("myDialogs.css");
        dialogPane.getStyleClass().add("myDialog");

        alert_error.initModality(Modality.APPLICATION_MODAL);
        alert_error.initOwner( MainStage.getScene().getWindow()); //有沒有這一行很重要，決定這兩個視窗是否有從屬關係

        alert_error.showAndWait();
        //alert_error.show();
        
        
    }
    
}
