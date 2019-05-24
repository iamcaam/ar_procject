/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class GBFunc {
        
    // 顯示文字對應表 
    public Map<String, String> WordMap = new HashMap<>();
    
    
    public GBFunc(Map<String, String> LangMap){
        WordMap = LangMap;
    }    
    
    /*
        警告 = 0，
        setTitle = WordMap.get("WW_Header")
        內容文字
            WordMap.get("WW_Header") + " ： " + WordMap.get("Message_Error_CP_IP_01") 
                                     + "\n"                                            
                                     + "\n"                    
                                     + 
            WordMap.get("Message_Suggest") + " ： "+WordMap.get("Message_Error_IP_07")      
        
        訊息建議 = 1,
        setTitle = WordMap.get("Message")
        內容文字            
            WordMap.get("Message")+" : "+WordMap.get("Comfirm_RebootMassage"));                              
    */        
    
    public void Alert(Stage mainStage, int Titletype, String ContentText, boolean btn_OK, boolean btn_cancel, int Size, String Action) {
        // 建立Alert視窗
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION); 
        
        // 設定對話框視窗的標題列文字。
        switch (Titletype) {
            case 0:   
                alert.setTitle(WordMap.get("WW_Header"));
                break;
            case 1:   
                alert.setTitle(WordMap.get("Message"));
                break;
            case 2:   
                alert.setTitle(WordMap.get("WW_Header"));
                break;        
            default:
                break;                                
        }                
             
        alert.setHeaderText(null);         // 設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭。
        alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);             
        alert.setContentText(ContentText); // 設定對話框視窗裡的內容文字。       
                
        // 建立按鈕類別
        ButtonType buttonTypeOK       = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);      // 確定
        ButtonType buttonTypeCancel   = new ButtonType(WordMap.get("Alert_Cancel") ,ButtonBar.ButtonData.CANCEL_CLOSE); // 取消   
        
        // 視窗內設置按鈕類別
        if(btn_OK && !btn_cancel) {
            alert.getButtonTypes().setAll(buttonTypeOK);
        } else if (!btn_OK && btn_cancel) {
            alert.getButtonTypes().setAll(buttonTypeCancel);
        } else if (btn_OK && btn_cancel) {
            alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
        }
                
        // 建立按鈕及設定按鈕動作
        Button button_ok              = (Button) alert.getDialogPane().lookupButton(buttonTypeOK);
        Button button_cancel          = (Button) alert.getDialogPane().lookupButton(buttonTypeCancel);        
    
        if(btn_OK) {
            button_ok.setOnMouseEntered((event)     -> { button_ok.requestFocus(); });
            button_ok.setOnKeyPressed((event)       -> { if(event.getCode() == KeyCode.ENTER) { button_ok.fire(); }});        
        }             
        if(btn_cancel) {
            button_cancel.setOnMouseEntered((event) -> { button_cancel.requestFocus(); });
            button_cancel.setOnKeyPressed((event)   -> { if(event.getCode() == KeyCode.ENTER) { button_cancel.fire(); }});           
        }

        // Alert視窗式樣
        DialogPane dialogPane         = alert.getDialogPane();
        dialogPane.getStylesheets().add("myDialogs.css");
        dialogPane.getStyleClass().add("myDialog");        
        
        // Alert屬性設定
        Bounds mainBounds = mainStage.getScene().getRoot().getLayoutBounds();     
        alert.getDialogPane().setMinSize(Size,Region.USE_PREF_SIZE);
        alert.getDialogPane().setMaxSize(Size,Region.USE_PREF_SIZE);        
        alert.setX(mainStage.getX() + (mainBounds.getWidth() - Size ) / 2);  
        alert.setY(mainStage.getY() + (mainBounds.getHeight() - 200 ) / 2);  
        
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainStage.getScene().getWindow());  //有沒有這一行很重要，決定這兩個視窗是否有從屬關係         
        
        // 按鈕功能設定
        Optional<ButtonType> result01 = alert.showAndWait();
        
        if (result01.get() == buttonTypeOK){                    
            try {
                Action_Function(Action);
            } catch (IOException ex) {
                Logger.getLogger(GBFunc.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // ... user chose CANCEL or closed the dialog
            alert.close();
        }                
    }       
    
    public void Action_Function(String action) throws IOException {
        switch (action) {
            case "reboot":   
                Runtime.getRuntime().exec("reboot");
                break;       
            case "close":   
                System.exit(1); 
                break;                                
            default:
                break;                                
        }      
    }
    
}
