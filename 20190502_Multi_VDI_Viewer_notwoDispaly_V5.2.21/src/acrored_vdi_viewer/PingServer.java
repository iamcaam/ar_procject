/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import java.io.IOException;
import java.net.*;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.*;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.ColumnConstraints;

/**
 *
 * @author duck_chang
 */
public final class PingServer {
    
    /***    顯示文字對應表    ***/
    public Map<String, String> WordMap=new HashMap<>();
    private Label MyTitle;
    private Label PingResult;
    private InetAddress address;
    private TimerTask task;

    /**
     *
     */
    public String Messages;
    //Thread 宣告
    public static QueryMachine QM;
    public GB GB;
    // Constructor of PingServer Class
    public PingServer(Map<String, String> LangMap){
        WordMap=LangMap;
    }
    
    public void Ping(Stage primaryStage, String IPAddr) throws IOException, InterruptedException{
        Stage secondStage = new Stage();
        QM=new QueryMachine(WordMap); //建立呼叫HTTP GET　POST REST JSON 的 Class
        QM.Reset();
        GridPane GP=new GridPane();
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(100);
        GP.getColumnConstraints().add(column1);
        GP.setAlignment(Pos.TOP_CENTER);
        GP.setPadding(new Insets(0, 0, 15, 0)); //設定元件和邊界的距離
        GP.setHgap(10); //元件間的水平距離
   	GP.setVgap(10); //元件間的垂直距離
 
        MyTitle=new Label(WordMap.get("Ping_Header")+IPAddr);
      
        ImageView IV=new ImageView();
        IV.setImage(new Image("images/find.png"));
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
        
        PingResult=new Label("");
        PingResult.setBorder(Border.EMPTY);
        PingResult.setId("item");
        ResultBox.getChildren().addAll(PingResult);
        GP.add(ResultBox, 0, 1);
        
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
        
        switch (GB.JavaVersion) {
            case 0:
                secondStage.getIcons().add(new Image("images/Icon2.png"));
                break;
            case 1:
                secondStage.getIcons().add(new Image("images/MCIcon2.png"));
                break;                
            default:
                break;
        }         
        
        secondStage.setTitle(WordMap.get("Ping_Window_Title"));
        secondStage.setScene(secondScene);
        secondStage.initStyle(StageStyle.DECORATED);
        secondStage.initModality(Modality.NONE);
        secondStage.initOwner(primaryStage);
        secondStage.show();
            
        
        /*
        Timer timer=new Timer();
        task=new TimerTask(){
          @Override
          public void run(){
              String QMStatus=QM.getQMStatus();
              
              if("Waiting".equals(QMStatus)){
                  QM.Ping(IPAddr);
              }
              
              if("Proceeding".equals(QMStatus)){
                  Messages="Pinging...";
              }
              
              if("Done".equals(QMStatus)){
                  Messages=QM.getQMMessages();
                  QM.Reset();
                  timer.cancel();
              }
              Platform.runLater(new Runnable() {
              @Override public void run() {
                PingResult.setText(Messages);
                }
              });
          }  
        };                
        timer.schedule(task, 1, 1000);
        */
    }

}
