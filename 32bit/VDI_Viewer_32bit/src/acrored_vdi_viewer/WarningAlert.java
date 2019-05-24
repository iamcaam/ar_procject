/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author duck_chang
 */
public class WarningAlert {
    
    /***    顯示文字對應表    ***/
    public Map<String, String> WordMap=new HashMap<>();
    public String Message;
    
    public WarningAlert(Stage primaryStage, Map<String, String> LangMap, String M){
        WordMap=LangMap;
        Message=M;
        
        Stage secondStage = new Stage();
        
        GridPane GP=new GridPane();
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(100);
        GP.getColumnConstraints().add(column1);
        GP.setAlignment(Pos.TOP_CENTER);
        GP.setPadding(new Insets(0, 0, 15, 0)); //設定元件和邊界的距離
        GP.setHgap(10); //元件間的水平距離
   	 GP.setVgap(10); //元件間的垂直距離
         
        Label MyTitle=new Label(WordMap.get("WW_Header"));
      
        ImageView IV=new ImageView();
        IV.setImage(new Image("images/warning.png"));
        IV.setFitHeight(30);
        IV.setFitWidth(30);
        
        HBox Title=new HBox();
        Title.setAlignment(Pos.CENTER_LEFT);
        Title.setSpacing(10);
        Title.getChildren().addAll(IV, MyTitle);
        Title.setId("Title");
        GP.add(Title, 0, 0);
        
        HBox ResultBox=new HBox();
        ResultBox.setAlignment(Pos.CENTER);
        ResultBox.setSpacing(10);
        
        /*** Error  Message ***/
        
        TextArea TA_Message=new TextArea();
        TA_Message.appendText(Message);
        GP.add(TA_Message, 0, 1);
        
        HBox ConfirmBox=new HBox();
        ConfirmBox.setAlignment(Pos.CENTER);
        ConfirmBox.setSpacing(10);
        Button Confirm=new Button(WordMap.get("Confirm"));
        Confirm.setOnAction((ActionEvent event) -> {
            secondStage.close();
        });
        
        ConfirmBox.getChildren().addAll(Confirm);
        GP.add(ConfirmBox, 0, 2);
        
        Scene secondScene = new Scene(GP);
        secondScene.getStylesheets().add("ping.css");
        
        secondStage.getIcons().add(new Image("images/warning.png"));
        secondStage.setTitle(WordMap.get("WW_Window_Title"));
        secondStage.setScene(secondScene);
        secondStage.initStyle(StageStyle.DECORATED);
        secondStage.initModality(Modality.NONE);
        secondStage.initOwner(primaryStage);
        secondStage.show();
        
    }
    
}
