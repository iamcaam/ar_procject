/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_ipsc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author victor
 */
public class Refresh {
    /***    顯示文字對應表    ***/
    public Map<String, String> WordMap=new HashMap<>();
    
    private String Address;
    
    
    public Refresh(Map<String, String> LangMap){
        WordMap=LangMap;
    }
    
    
    public void RefreshListSession() throws MalformedURLException, IOException{ //String addr
        URL url;
        //Address=addr;
        
        String address = null;
        address = InetAddress.getLocalHost().getHostAddress();
        //address="127.0.0.1";
        //address="192.168.92.10";
        
       
        try{
            //URL : http://{IP}:9000/api/iscsicli/sessions
            //http://localhost:9000/api/iscsicli/targets 
            url = new URL("http://"+"localhost"+":27678"+"/api/"+"iscsicli/"+"sessions"); 
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  //建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線    
            conn.setRequestMethod("GET");//利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
	    conn.setRequestProperty("Accept", "application/json");   
            //conn.setRequestProperty("Accept", "*/*"); 
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            //conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Connection", "Keep-Alive");//維持長連接.         
            conn.connect(); // 建立實際的連接

            System.out.println("-----------Refresh--Session----------\n");
            System.out.println("Refresh　Session - URL : "+url+"\n"); 
            System.out.println("Refresh　Session - Resp Code:"+conn.getResponseCode()+"\n"); 
            System.out.println("Refresh　Session - Resp Message:"+ conn.getResponseMessage()+"\n");

            InputStream Session_is = conn.getInputStream();
            //System.out.println("InputStream DoListSession : "+DoListSession_+"\n");
            StringBuilder Session_sb;
            String Session_line;  

            try (BufferedReader Session_br = new BufferedReader(new InputStreamReader(Session_is))) {              
                Session_sb = new StringBuilder("");
                while ((Session_line = Session_br.readLine()) != null) {            
                    Session_sb.append(Session_line);         
                }                 
                //System.out.println("return DoListTarget_br: "+DoListTarget_br.readLine()+"\n");
                //System.out.println("DoListTarget - return DoListTarget_sb: "+DoListTarget_sb.toString()+"\n");             
         
                Session_br.close();
            }
            String session_list = Session_sb.toString();
            System.out.println("DoListSession - session_list: "+session_list+"\n");       
            conn.disconnect();
            //string != null && !string.isEmpty()  
            int session_length = session_list.split("[\\[\",\"\\]\\s]+").length;
            System.out.println("session_length : "+session_length+"\n");   
            if(session_length == 0){
                System.out.println(" NO Action  \n");               
            }else{
                //Refresh 功能
                RefreshLUN();
            }

        } catch (MalformedURLException e) {            
	} catch (IOException e) {                
        }  
    }
    
    
    public void RefreshLUN() throws MalformedURLException, IOException{ //String addr
        URL url;
        
        try{
            //URL : http://{IP}:9000/api/iscsicli/targets/refresh
            //http://localhost:9000/api/iscsicli/targets/refresh
            url = new URL("http://"+"localhost"+":27678"+"/api/"+"iscsicli/"+"targets/"+"refresh"); 
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  //建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線    
            conn.setRequestMethod("GET");//利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
	    conn.setRequestProperty("Accept", "application/json");   
            //conn.setRequestProperty("Accept", "*/*"); 
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            //conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Connection", "Keep-Alive");//維持長連接.         
            conn.connect(); // 建立實際的連接

            System.out.println("---------Refresh---------\n");
            System.out.println("Refresh - URL : "+url+"\n"); 
            System.out.println("Refresh - Resp Code:"+conn.getResponseCode()+"\n"); 
            System.out.println("Refresh - Resp Message:"+ conn.getResponseMessage()+"\n");
            
            InputStream Refresh_is = conn.getInputStream();
            System.out.println("InputStream DoListSession : "+Refresh_is+"\n");
            StringBuilder Refresh_sb;
            String Refresh_line;  

            try (BufferedReader Refresh_br = new BufferedReader(new InputStreamReader(Refresh_is))) {              
                Refresh_sb = new StringBuilder("");
                while ((Refresh_line = Refresh_br.readLine()) != null) {            
                    Refresh_sb.append(Refresh_line);         
                }                 
                //System.out.println("return DoListTarget_br: "+DoListTarget_br.readLine()+"\n");
                //System.out.println("DoListTarget - return DoListTarget_sb: "+DoListTarget_sb.toString()+"\n");             
         
                Refresh_br.close();
            }
            String Refresh_list = Refresh_sb.toString();
            //System.out.println("DoListSession - session_list: "+Refresh_list+"\n");       
            conn.disconnect();

        } catch (MalformedURLException e) {
            
	} catch (IOException e) {    
            
        }  
    }
    
    
}
