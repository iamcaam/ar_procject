/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 *
 * @author root
 */
public class webbrowser {
    
    public Process web_process;
    List<Process> Web_processes =FXCollections.observableArrayList();
    public Map<String, String> WordMap=new HashMap<>();
    
    Stage WebStage = new Stage();

    
    public webbrowser(Map<String, String> LangMap){
     
        WordMap=LangMap;
       // uniqueKey=Ukey;
       //UUID 需要非同步(需固定 不能改)
    }
    
    /**
     *
     * @throws MalformedURLException
     * @throws IOException
     */
    public void Firefoxwebbrowser()throws MalformedURLException, IOException {
        
        //web_process=null;
        //Web_processes=null;
        /*
        Runtime rt = Runtime.getRuntime();
        String url = "http://google.com";
        String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror","netscape","opera","links","lynx"};
        StringBuilder cmd = new StringBuilder();
        for (int i=0; i<browsers.length; i++)
        cmd.append(i==0  ? "" : " || ").append(browsers[i]).append(" \"").append(url).append("\" ");
        rt.exec(new String[] { "sh", "-c", cmd.toString() });
        */
        /*
        String[] Web_args = new String[] { "firefox", "http://www.google.com" };
        for(int Web_num =0 ; ;Web_num++ ){
                    
        web_process[Web_num] = Runtime.getRuntime().exec( Web_args );
        Web_processes.add(web_process[Web_num]);
                    
        System.out.println("Web_processes : "+Web_processes+"\n");
                    
        }
        */
        
        String[] Web_args = new String[] { "firefox", "http://www.google.com" };
        web_process = Runtime.getRuntime().exec( Web_args );
        
        /*
        if(web_process!=null){
                
            Web_processes.add(web_process);//登入一組帳號密碼,會有一個VD,將所有VD-process加入List中,以便之後可將所有process關閉
                
        }
        System.out.println("Web_processes : "+Web_processes+"\n");

        
        int size = Web_processes.size();
        System.out.println("Web_processes.size() : "+size+"\n");
        */
        
    }

  
    
}
