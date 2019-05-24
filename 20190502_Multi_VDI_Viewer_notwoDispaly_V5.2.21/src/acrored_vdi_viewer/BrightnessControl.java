/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author root
 */
public class BrightnessControl {
    /***    顯示文字對應表    ***/
    public Map<String, String> WordMap = new HashMap<>();     
    GridPane MainGP;
    Button Btn_Set_Brightness = new Button();
    int Brightness_level = 0;
    int Brightness_value = 0;
    Text text = new Text();
    int addListener_count = 0;
    static Semaphore mutex = new Semaphore(1);
    GB GB;
    final Slider slider = new Slider(){
            @Override
            protected void layoutChildren() {
                super.layoutChildren();

                Region thumb = (Region) lookup(".thumb");
                if (thumb != null) {
                    text.setLayoutX(
                            thumb.getLayoutX() 
                                    + thumb.getWidth() / 2 
                                    - text.getLayoutBounds().getWidth() / 2
                    );
//                    text.setStyle("-fx-font-family:'Ubuntu';-fx-font-size: 16px;-fx-font-weight: 700;");
                    text.setFill(Color.WHITE);
                    text.setFont(Font.font("Ubuntu", FontWeight.EXTRA_BOLD, 24));
                }
            }
        };
    
    public BrightnessControl(Map<String, String> LangMap){     
        WordMap = LangMap;
    }       
    
    public void BrightnessShow(Stage stage) {
        Stage BrightnessStage = new Stage();
        Scene Scene;
        MainGP = new GridPane();
        Bounds mainBounds;
        /*------------------------ 設定長寬 ------------------------*/
        int Scene_width; 
        int Scene_heighth; 
        int MainGP_width;
        int MainGP_height;        
        int sliderWidth;
        int sliderHeighth;
        Scene_width   = 385; 
        Scene_heighth = 100; 
        MainGP_width  = 385;
        MainGP_height = 100;        
        sliderWidth   = 320;
        sliderHeighth = 100;
                                
        /*------------------------ 設定Layout component ------------------------*/        
        MainGP.setGridLinesVisible(false);         // 開啟或關閉表格的分隔線       
        MainGP.setAlignment(Pos.CENTER);       
        MainGP.setPadding(new Insets(0, 0, 0, 0)); // 設定元件和邊界的距離(使用Insets物件設定GridPane元件上、右、下、左的間隔)
        MainGP.setHgap(18);                        // 元件間的水平距離
   	MainGP.setVgap(0);                         // 元件間的垂直距離           
        MainGP.setPrefSize(MainGP_width, MainGP_height);
        MainGP.setMaxSize(MainGP_width, MainGP_height);
        MainGP.setMinSize(MainGP_width, MainGP_height); 
//        MainGP.setTranslateY(-15); 
        MainGP.setTranslateX(0);     
        
        text.setTextOrigin(VPos.TOP);
//        slider.setPrefSize(sliderWidth, sliderHeighth);
        slider.setMaxSize(sliderWidth, sliderHeighth);        
        slider.setMinSize(sliderWidth, sliderHeighth);
                        
        slider.setMin(0);               // 最小值
        slider.setMax(100);             // 最大值        
        try {
            Get_Brightness_value();
        } catch (IOException ex) {
            Logger.getLogger(BrightnessControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        slider.setShowTickLabels(false); // 刻度值
        slider.setShowTickMarks(false);  // 刻度
        slider.setMajorTickUnit(10);
        slider.setMinorTickCount(0);
//        slider.setBlockIncrement(10);
        
//        slider.setSnapToTicks(true);
        slider.autosize();
   
        

        
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
//                slider.setOnMouseReleased((MouseEvent event) -> {
                    Brightness_value = (int) new_val.intValue();
                    System.out.println("Brightness_value: " + Brightness_value);
                    GB.Brightness_value = Brightness_value;
                    if(Brightness_value >= 0 && Brightness_value <= 10) {                    
                        try {
                            Set_Brightness_value(0.1);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if(Brightness_value > 10 && Brightness_value <= 15) {
                        try {
                            Set_Brightness_value(0.15);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }         
                    } else if(Brightness_value > 15 && Brightness_value <= 20) {
                        try {
                            Set_Brightness_value(0.2);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }        
                    } else if(Brightness_value > 20 && Brightness_value <= 30) {
                        try {
                            Set_Brightness_value(0.3);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }        
                    } else if(Brightness_value > 30 && Brightness_value <= 40) {
                        try {
                            Set_Brightness_value(0.4);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }            
                    } else if(Brightness_value > 40 && Brightness_value <= 50) {
                        try {
                            Set_Brightness_value(0.5);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }            
                    } else if(Brightness_value > 50 && Brightness_value <= 60) {
                        try {
                            Set_Brightness_value(0.6);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }            
                    } else if(Brightness_value > 60 && Brightness_value <= 70) {
                        try {
                            Set_Brightness_value(0.7);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }            
                    } else if(Brightness_value > 70 && Brightness_value <= 80) {
                        try {
                            Set_Brightness_value(0.8);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }          
                    } else if(Brightness_value > 80 && Brightness_value <= 90) {
                        try {
                            Set_Brightness_value(0.9);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }          
                    } else if(Brightness_value > 90 && Brightness_value <= 100) {
                        try {
                            Set_Brightness_value(1);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }           
                    }              

//                });                
                
                slider.setOnKeyReleased((KeyEvent event) -> {
                    Brightness_value = (int) new_val.intValue();
                    System.out.println("Brightness_value: " + Brightness_value);
                    GB.Brightness_value = Brightness_value;
                    if(Brightness_value >= 0 && Brightness_value <= 10) {                    
                        try {
                            Set_Brightness_value(0.1);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if(Brightness_value > 10 && Brightness_value <= 15) {
                        try {
                            Set_Brightness_value(0.15);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }         
                    } else if(Brightness_value > 15 && Brightness_value <= 20) {
                        try {
                            Set_Brightness_value(0.2);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }        
                    } else if(Brightness_value > 20 && Brightness_value <= 30) {
                        try {
                            Set_Brightness_value(0.3);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }        
                    } else if(Brightness_value > 30 && Brightness_value <= 40) {
                        try {
                            Set_Brightness_value(0.4);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }            
                    } else if(Brightness_value > 40 && Brightness_value <= 50) {
                        try {
                            Set_Brightness_value(0.5);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }            
                    } else if(Brightness_value > 50 && Brightness_value <= 60) {
                        try {
                            Set_Brightness_value(0.6);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }            
                    } else if(Brightness_value > 60 && Brightness_value <= 70) {
                        try {
                            Set_Brightness_value(0.7);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }            
                    } else if(Brightness_value > 70 && Brightness_value <= 80) {
                        try {
                            Set_Brightness_value(0.8);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }          
                    } else if(Brightness_value > 80 && Brightness_value <= 90) {
                        try {
                            Set_Brightness_value(0.9);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }          
                    } else if(Brightness_value > 90 && Brightness_value <= 100) {
                        try {
                            Set_Brightness_value(1);
                        } catch (Exception ex) {
                            Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }           
                    }                    
                
                
                });
                
/*
                Brightness_value = (int) new_val.intValue();
                System.out.println("Brightness_value: " + Brightness_value);
                GB.Brightness_value = Brightness_value;
                if(Brightness_value >= 0 && Brightness_value <= 10) {                    
                    try {
                        Set_Brightness_value(0.1);
                    } catch (Exception ex) {
                        Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if(Brightness_value > 10 && Brightness_value <= 15) {
                    try {
                        Set_Brightness_value(0.15);
                    } catch (Exception ex) {
                        Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                    }         
                } else if(Brightness_value > 15 && Brightness_value <= 20) {
                    try {
                        Set_Brightness_value(0.2);
                    } catch (Exception ex) {
                        Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                    }        
                } else if(Brightness_value > 20 && Brightness_value <= 30) {
                    try {
                        Set_Brightness_value(0.3);
                    } catch (Exception ex) {
                        Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                    }        
                } else if(Brightness_value > 30 && Brightness_value <= 40) {
                    try {
                        Set_Brightness_value(0.4);
                    } catch (Exception ex) {
                        Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                    }            
                } else if(Brightness_value > 40 && Brightness_value <= 50) {
                    try {
                        Set_Brightness_value(0.5);
                    } catch (Exception ex) {
                        Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                    }            
                } else if(Brightness_value > 50 && Brightness_value <= 60) {
                    try {
                        Set_Brightness_value(0.6);
                    } catch (Exception ex) {
                        Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                    }            
                } else if(Brightness_value > 60 && Brightness_value <= 70) {
                    try {
                        Set_Brightness_value(0.7);
                    } catch (Exception ex) {
                        Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                    }            
                } else if(Brightness_value > 70 && Brightness_value <= 80) {
                    try {
                        Set_Brightness_value(0.8);
                    } catch (Exception ex) {
                        Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                    }          
                } else if(Brightness_value > 80 && Brightness_value <= 90) {
                    try {
                        Set_Brightness_value(0.9);
                    } catch (Exception ex) {
                        Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                    }          
                } else if(Brightness_value > 90 && Brightness_value <= 100) {
                    try {
                        Set_Brightness_value(1);
                    } catch (Exception ex) {
                        Logger.getLogger(BatteryCheck.class.getName()).log(Level.SEVERE, null, ex);
                    }           
                }                
*/
            }
        });
        
//slider.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
//    @Override
//    public void handle(MouseEvent mouseEvent) {
//        System.out.println("mouse click detected! " + mouseEvent.getSource());
//    }
//});        
        
        
        
//       slider.setLayoutY(20);
        text.textProperty().bind(slider.valueProperty().asString("%,.0f"));
        

        Pane sliderPane = new Pane(slider, text);
        slider.prefWidthProperty().bind(sliderPane.widthProperty());
//        sliderPane.setPrefWidth(200);

        sliderPane.setMaxSize(sliderWidth, sliderHeighth);        
        sliderPane.setMinSize(sliderWidth, sliderHeighth);
        sliderPane.setTranslateY(10);


//        StackPane layout = new StackPane(sliderPane);
//        layout.setPadding(new Insets(10));        
        
 
        final HBox hb = new HBox();
        hb.setSpacing(5);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(sliderPane);      
        hb.setTranslateY(5);
        
        MainGP.add(hb, 0, 0);             
        

        Scene = new Scene(MainGP, Scene_width, Scene_heighth); // 590, 365
        Scene.getStylesheets().add("Brightness.css");
        
        mainBounds = stage.getScene().getRoot().getLayoutBounds(); 
        
        BrightnessStage.setX(stage.getX() + (mainBounds.getWidth() - 386 ));
        BrightnessStage.setY(stage.getY() + (mainBounds.getHeight() - 188 ));         
   
        BrightnessStage.setScene(Scene);
        BrightnessStage.setTitle(WordMap.get("Brightness")); // 視窗標題
        switch (GB.JavaVersion) {
            case 0:
                BrightnessStage.getIcons().add(new Image("images/Icon2.png"));
                break;
            case 1:
                BrightnessStage.getIcons().add(new Image("images/MCIcon2.png"));
                break;                
            default:
                break;
        }                           
        BrightnessStage.initStyle(StageStyle.DECORATED); // DECORATED -> UNDECORATED 
        BrightnessStage.initModality(Modality.NONE);     // window modal lock NONE -> WINDOW_MODAL ->  APPLICATION_MODAL
//        Stage.initOwner(primaryStage);
        BrightnessStage.setResizable(false);             // 將視窗放大,關閉    
        BrightnessStage.show();                          // important *** lock and return value : show -> showAndWait

        
        // 新增Stage關閉事件
        BrightnessStage.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if(ov.getValue()==false) {
                BrightnessStage.close();
//                GB.lock_battery = false;
            }
        });        
        
        BrightnessStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
            System.out.println("Brightness Stage is closing");
            BrightnessStage.close();
//            GB.lock_battery = false;
          }
      }); 
        
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
        if(GB.Brightness_value == -1) {
            if(return_value > 0 && return_value <= 10) {
                slider.setValue(0);           // 當前值
            } else if(return_value > 10 && return_value <= 15) {
                slider.setValue(10);           
            } else if(return_value > 15 && return_value <= 20) {
                slider.setValue(20);           
            } else if(return_value > 20 && return_value <= 30) {
                slider.setValue(30);           
            } else if(return_value > 30 && return_value <= 40) {
                slider.setValue(40);           
            } else if(return_value > 40 && return_value <= 50) {
                slider.setValue(50);           
            } else if(return_value > 50 && return_value <= 60) {
                slider.setValue(60);           
            } else if(return_value > 60 && return_value <= 70) {
                slider.setValue(70);           
            } else if(return_value > 70 && return_value <= 80) {
                slider.setValue(80);           
            } else if(return_value > 80 && return_value <= 90) {
                slider.setValue(90);           
            } else if(return_value > 90 && return_value <= 100) {
                slider.setValue(100);           
            }        
        } else {
            slider.setValue(GB.Brightness_value);
        }

//        System.out.println("return_value: " + return_value + " , Brightness_level: " + Brightness_level );
        return return_value;
    }       
    
    public void Set_Brightness_value(double vaule) throws IOException, InterruptedException {
       mutex.acquire();
        String command_value = "xrandr --output eDP1 --brightness " + vaule;
        Runtime.getRuntime().exec(command_value).waitFor();  
        mutex.release();
//        Get_Brightness_value();
    }    
    
}
