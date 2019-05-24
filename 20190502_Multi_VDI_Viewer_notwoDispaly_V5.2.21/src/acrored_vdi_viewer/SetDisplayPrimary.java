/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
//import static oracle.jrockit.jfr.events.Bits.intValue;

/**
 *
 * @author root
 */
public class SetDisplayPrimary {
//        public SetDisplayPrimary SetDP; // 2018.01.29 william 雙螢幕實作 - 設定主從螢幕   
    //SDP = new SetDisplayPrimary(MainStage, WordMap, Display_primary_name, Display_second_name, primary_resolution_width, primary_resolution_height, second_resolution_width, second_resolution_height);
//                SDP = new SetDisplayPrimary(MainStage, WordMap, Display_primary_name, "HDMI1", primary_resolution_width, primary_resolution_height, "1440", "900");
    
    /*------------------------ import檔案及匯入文字檔 ------------------------*/
    public Map<String, String> WordMap = new HashMap<>();
    /*------------------------ 設定Layout ------------------------*/
    private Stage public_stage;    
    private Stage thirdStage = new Stage();
    private final Scene thirdScene;   
    private final GridPane MainGP;     // 2017.12.04 william BorderPane中間層
    private final BorderPane rootPane; // 2017.12.04 william BorderPane框架(底層)      
    private final Bounds mainBounds;
    Group G1 = new Group();    
    Group G2 = new Group();    
    StackPane stack1 = new StackPane();     
    StackPane stack2 = new StackPane();
    StackPane stack3 = new StackPane();
    StackPane stack4 = new StackPane();
    
    HBox hbox1 = new HBox();    
    HBox hbox2 = new HBox();    
    VBox vbox1 = new VBox();
    VBox vbox2 = new VBox();    
    /*------------------------ 設定長寬 ------------------------*/
    int thirdScene_width  = 702; // (1080*0.75 = 810) -> (1080*0.65 = 702)
    int thirdScene_height = 392; // (603*0.75 = 452) -> (603*0.65 = 392)
    int MainGP_width      = 702;
    int MainGP_height     = 392;      
    /*------------------------ 話圖形 ------------------------*/
    Text PrimaryName_text1;
    Text PrimaryName_text2;
    Rectangle Primaryrectangle;    
    Text SecondName_text1;
    Text SecondName_text2;
    Rectangle Secondrectangle;    
    int primarywidth;
    int primaryheight;
    int secondwidth;
    int secondheight;   
    
    private ToggleGroup display_group;
    private RadioButton rb_display01;
    private RadioButton rb_display02;   
    public GB GB;
    Label left1;
    Label right1;    
    Label left2;
    Label right2;    
//    int PrimaryName_text_wp;
//    int PrimaryName_text_hp;    
//    int SecondName_text_wp;
//    int SecondName_text_hp;
    
    public SetDisplayPrimary(Stage primaryStage, Map<String, String> LangMap, String primary_name, String second_name, String primary_width, String primary_height, String second_width, String second_height){
        public_stage     = primaryStage;        
        WordMap          = LangMap;            
        primarywidth     = Integer.parseInt(primary_width) / 8;
        primaryheight    = Integer.parseInt(primary_height) / 8;
        secondwidth      = Integer.parseInt(second_width) / 8;
        secondheight     = Integer.parseInt(second_height) / 8 ; 
        PrimaryName_text1 = createText(primary_name);
        SecondName_text1  = createText(second_name);    
        PrimaryName_text2 = createText(second_name);
        SecondName_text2  = createText(primary_name);   
//        s11 = new Label("左: "  + primary_name + " " + primary_width + "x" + primary_height);       
//        s12 = new Label("右: " + second_name + " " + second_width + "x" + second_height);        
//        s21 = new Label("左: "  + second_name + " " + second_width + "x" + second_height);
//        s22 = new Label("右: " + primary_name + " " + primary_width + "x" + primary_height);        
//        s11.setStyle("-fx-font-size: 18px; -fx-font-weight: 900;");
//        s12.setStyle("-fx-font-size: 18px; -fx-font-weight: 900;");
//        s21.setStyle("-fx-font-size: 18px; -fx-font-weight: 900;"); 
//        s22.setStyle("-fx-font-size: 18px; -fx-font-weight: 900;");
        Rectangle rectangle1 = enrect(PrimaryName_text1, 0, 0, primarywidth, primaryheight);        
        Rectangle rectangle2 = enrect(SecondName_text1 , 0, 0, secondwidth , secondheight);  
        Rectangle rectangle3 = enrect(SecondName_text2, 0, 0, secondwidth, secondheight);        
        Rectangle rectangle4 = enrect(PrimaryName_text2 , 0, 0, primarywidth , primaryheight);  

        display_group = new ToggleGroup();
        rb_display01 = new RadioButton();
        rb_display01.setToggleGroup(display_group);
        
        rb_display02 = new RadioButton();
        rb_display02.setToggleGroup(display_group);        
         
        
//        PrimaryName_text_wp = getWidth(new Text(primary_name));
//        PrimaryName_text_hp = getHeight(new Text(primary_name));
//        SecondName_text_wp = getWidth(new Text(second_name));
//        SecondName_text_hp = getHeight(new Text(second_name));      

        /***  主畫面使用GRID的方式Layout ***/
        MainGP = new GridPane();
        MainGP.setGridLinesVisible(true);                // 開啟或關閉表格的分隔線       
        MainGP.setAlignment(Pos.CENTER);       
        MainGP.setPadding(new Insets(35, 50, 50, 20)); // 設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        MainGP.setHgap(15);                               // 元件間的水平距離
   	MainGP.setVgap(25);                                // 元件間的垂直距離        
        MainGP.setPrefSize(MainGP_width, MainGP_height);
        MainGP.setMaxSize(MainGP_width , MainGP_height);
        MainGP.setMinSize(MainGP_width , MainGP_height);       
        MainGP.setTranslateY(0); // 55
        MainGP.setTranslateX(0);        
   
        
        stack1.getChildren().addAll(rectangle1, PrimaryName_text1);
        stack2.getChildren().addAll(rectangle2, SecondName_text1);
        stack3.getChildren().addAll(rectangle3, PrimaryName_text2);
        stack4.getChildren().addAll(rectangle4, SecondName_text2);        
//        G1.getChildren().addAll(stack1);
//        G2.getChildren().addAll(rectangle3, rectangle4);
        hbox1.getChildren().addAll(stack1, stack2);
        hbox2.getChildren().addAll(stack3, stack4);    
        
//        vbox1.getChildren().addAll(s11, s12, rb_display01);
//        vbox2.getChildren().addAll(s21, s22, rb_display02);
//        hbox1.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
//        + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
//        + "-fx-border-radius: 5;" + "-fx-border-color: rgb(0,180,240);");   
//        
//        hbox2.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
//        + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
//        + "-fx-border-radius: 5;" + "-fx-border-color: rgb(0,180,240);");          
        
        
        MainGP.add(hbox1,0,0);   
        MainGP.add(hbox2,0,1);
        MainGP.add(vbox1, 1, 0);
        MainGP.add(vbox2, 1, 1);
        /***  底層畫面使用Border的方式Layout ***/
        rootPane = new BorderPane(); 
        rootPane.setCenter(MainGP);        
        
        thirdScene = new Scene(rootPane, thirdScene_width, thirdScene_height);
//        thirdScene.getStylesheets().add("");
        
        MainGP.setStyle("-fx-background-color: rgb(0,180,240);"); 

        mainBounds = primaryStage.getScene().getRoot().getLayoutBounds();    

        thirdStage.setX(primaryStage.getX() + (mainBounds.getWidth() - thirdScene_width ) / 2);
        thirdStage.setY(primaryStage.getY() + (mainBounds.getHeight() - thirdScene_height ) / 2); 
        thirdStage.setScene(thirdScene);
        thirdStage.setTitle(WordMap.get("APP_Title"));                    // 視窗標題 Title_Snapshot_onlineviewing
        switch (GB.JavaVersion) {
            case 0:
                thirdStage.getIcons().add(new Image("images/Icon2.png"));
                break;
            case 1:
                thirdStage.getIcons().add(new Image("images/MCIcon2.png"));
                break;                
            default:
                break;
        }         
                
        thirdStage.initStyle(StageStyle.DECORATED);                       // DECORATED -> UNDECORATED 
        thirdStage.initModality(Modality.WINDOW_MODAL);                   // window modal lock NONE -> WINDOW_MODAL
        thirdStage.initOwner(primaryStage);
        thirdStage.setResizable(false);                                   // 將視窗放大,關閉             
        thirdStage.show();             
        
        
    }
    
    private Text createText(String string) {
        Text text = new Text(string);
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setStyle(
                "-fx-font-family: \"Times New Roman\";" +
                "-fx-font-style: italic;" +
                "-fx-font-size: 36px;"
        );
//        text.setX(width);
//        text.setY(height);

        return text;
    }    
    
    // draw circle
//    private Circle encircle(Text text) {
//        Circle circle = new Circle();
//        circle.setFill(Color.ORCHID);
//        final double PADDING = 10;
//        circle.setRadius(getWidth(text) / 2 + PADDING);
//
//        return circle;
//    }    
    
    // draw rectangle
    private Rectangle enrect(Text text, int x, int y, int width, int height) {
        Rectangle rect = new Rectangle(x, y, width, height);
        // rect.setFill(Color.TRANSPARENT);
        rect.setFill(Color.TRANSPARENT);
        rect.setFill(Color.LIGHTGREY);
        rect.setStroke(Color.GRAY);
        rect.setStrokeWidth(2); 

        return rect;
    }    
    
    // get Text Width
//    private int getWidth(Text text) {
//        new Scene(new Group(text));
//        text.applyCss();
//        
//        return intValue(text.getLayoutBounds().getWidth());
//    }   
//    
    // get Text Height
//    private int getHeight(Text text) {
//        new Scene(new Group(text));
//        text.applyCss();
//               
//        return intValue(text.getLayoutBounds().getHeight());
//    }       
//    
    
}
