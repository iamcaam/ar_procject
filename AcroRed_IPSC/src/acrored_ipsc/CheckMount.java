/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_ipsc;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;

/**
 *
 * @author victor
 */
public class CheckMount {
    
    /***    顯示文字對應表    ***/
    public Map<String, String> WordMap=new HashMap<>();
    public boolean CheckMount_button_change=true ;
    public boolean CheckMount_status_change=false ;
    private String session_list;
    public boolean Format_status = false;
    public int getLoginInfo_con ;
    public String GetisAutoLogin;
    public String GetisAnonymous;
    
    List<String> format_status_List =FXCollections.observableArrayList() ;
    
    public CheckMount(Map<String, String> LangMap){
        WordMap=LangMap;
    }
    
    
    public void CheckMountListSession() throws MalformedURLException, IOException{ //String addr
        URL url;        
        //System.out.println("---------------CheckMountListSession----------------\n"); // 2019.03.08 william IPSC新增記住上一次連線的IP     

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

            //System.out.println("CheckMount - URL : "+url+"\n"); 
            //System.out.println("CheckMount - Resp Code:"+conn.getResponseCode()+"\n"); // 2019.03.08 william IPSC新增記住上一次連線的IP     
            //System.out.println("CheckMount - Resp Message:"+ conn.getResponseMessage()+"\n");

            InputStream is = conn.getInputStream();
            //System.out.println("InputStream DoListSession : "+DoListSession_+"\n");
            StringBuilder sb;
            String line;  

            try (BufferedReader Unmout_br = new BufferedReader(new InputStreamReader(is))) {              
                sb = new StringBuilder("");
                while ((line = Unmout_br.readLine()) != null) {            
                    sb.append(line);         
                }                 
                //System.out.println("return DoListTarget_br: "+DoListTarget_br.readLine()+"\n");
                //System.out.println("DoListTarget - return DoListTarget_sb: "+DoListTarget_sb.toString()+"\n");             
         
                Unmout_br.close();
            }
            session_list = sb.toString();
            //System.out.println("Unmout - session_list: "+session_list+"\n"); 
            int session_length = session_list.split("[\\[\",\"\\]\\s]+").length;
            //System.out.println("Checksession_length : "+session_length+"\n");            
            if(session_length == 0){
                CheckMount_button_change=true ;         
                CheckMount_status_change=false ; //未掛載
            }else{
                CheckMount_button_change=false;
                CheckMount_status_change=true ; //已掛載
                CheckDiskNeedFormat();
            }
            
            conn.disconnect();
            
            
        } catch (MalformedURLException e) {
            
	} catch (IOException e) {    
            
        }  
    }
    
    public void CheckDiskNeedFormat() throws MalformedURLException, IOException{ //String addr
        URL url;
       
        try{
            //URL： http://{IP}:9000/api/system/disks/info
            //http://localhost:9000/api/system/disks/info
            url = new URL("http://"+"localhost"+":27678"+"/api/"+"system/"+"disks/"+"info"); 
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  //建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線    
            conn.setRequestMethod("GET");//利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
	    conn.setRequestProperty("Accept", "application/json");   
            //conn.setRequestProperty("Accept", "*/*"); 
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            //conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Connection", "Keep-Alive");//維持長連接.         
            conn.setConnectTimeout(10000); // 2019.03.08 william IPSC新增記住上一次連線的IP
            conn.setReadTimeout(10000); // 2019.03.08 william IPSC新增記住上一次連線的IP
            conn.connect(); // 建立實際的連接
            
            //System.out.println("-------------------CheckDiskNeedFormat----------------------\n");  // 2019.03.08 william IPSC新增記住上一次連線的IP    
            //System.out.println("CheckDiskNeedFormat - URL : "+url+"\n"); 
            //System.out.println("CheckDiskNeedFormat - Resp Code:"+conn.getResponseCode()+"\n");  // 2019.03.08 william IPSC新增記住上一次連線的IP    
            //System.out.println("CheckDiskNeedFormat - Resp Message:"+ conn.getResponseMessage()+"\n");
            
            InputStream Check_is = conn.getInputStream();
            //System.out.println("InputStream DoListSession : "+DoListSession_+"\n");
            StringBuilder Check_sb;
            String Check_line;  

            try (BufferedReader Check_br = new BufferedReader(new InputStreamReader(Check_is))) {              
                Check_sb = new StringBuilder("");
                while ((Check_line = Check_br.readLine()) != null) {            
                    Check_sb.append(Check_line);         
                }                 
                //System.out.println("return DoListTarget_br: "+DoListTarget_br.readLine()+"\n");
                //System.out.println("DoListTarget - return DoListTarget_sb: "+DoListTarget_sb.toString()+"\n");             
         
                Check_br.close();
            }
            String Check_list = Check_sb.toString();
            //System.out.println("CheckDiskNeedFormat - Check_list: "+Check_list+"\n");       
            JsonParser jsonParser = new JsonParser();       
            JsonArray CheckArr = (JsonArray)jsonParser.parse(Check_list).getAsJsonArray();
            //System.out.println("json JsonArray: "+CheckArr+"\n");  
            //System.out.println("** json returndata02 **\n"); 
            for (int i = 0; i < CheckArr.size(); i++) {
                
                JsonObject Checkterm = CheckArr.get(i).getAsJsonObject();
                String PName=Checkterm.get("PhysicalName").getAsString();
                //System.out.println("PhysicalName: "+PName+"\n");
                String Partition=Checkterm.get("HavePartition").toString();
                //System.out.println("HavePartition: "+Partition+"\n");
                               
                if(Partition.equals("false")){   //false                      
                    format_status_List.add(PName);                     
                } 
            }
            //System.out.println("format_status_List : "+ format_status_List +"\n");  // 2019.03.08 william IPSC新增記住上一次連線的IP  
            
            if(format_status_List != null && !format_status_List.isEmpty()){ 
                Format_status = true;
            }else{
                Format_status = false;
            }
            
            conn.disconnect();           

        } catch (MalformedURLException e) {
            
	} catch (IOException e) {    
            
        }  
    }
    
    public void GetLoginInformation() throws MalformedURLException, IOException{ //String addr
        URL url;
       
        try{
            //URL : http://{IP}:27678/api/iscsicli/account/info
            //http://localhost:27678/api/iscsicli/account/info
            url = new URL("http://"+"localhost"+":27678"+"/api/"+"iscsicli/"+"account/"+"info"); 
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  //建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線    
            conn.setRequestMethod("GET");//利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
	    conn.setRequestProperty("Accept", "application/json");   
            //conn.setRequestProperty("Accept", "*/*"); 
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            //conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Connection", "Keep-Alive");//維持長連接.         
            conn.connect(); // 建立實際的連接
            
            getLoginInfo_con = conn.getResponseCode();
            System.out.println("-------------------GetLoginInformation----------------------\n");
            System.out.println("GetLoginInformation - URL : "+url+"\n"); 
            System.out.println("GetLoginInformation - Resp Code:"+getLoginInfo_con+"\n"); 
            System.out.println("GetLoginInformation - Resp Message:"+ conn.getResponseMessage()+"\n");
            
            InputStream getLoginInfo_is = conn.getInputStream();
            //System.out.println("InputStream DoListSession : "+DoListSession_+"\n");
            StringBuilder getLoginInfo_sb;
            String getLoginInfo_line;  

            try (BufferedReader getLoginInfo_br = new BufferedReader(new InputStreamReader(getLoginInfo_is))) {              
                getLoginInfo_sb = new StringBuilder("");
                while ((getLoginInfo_line = getLoginInfo_br.readLine()) != null) {            
                    getLoginInfo_sb.append(getLoginInfo_line);         
                }                 
                //System.out.println("return DoListTarget_br: "+DoListTarget_br.readLine()+"\n");
                //System.out.println("DoListTarget - return DoListTarget_sb: "+DoListTarget_sb.toString()+"\n");             
         
                getLoginInfo_br.close();
            }
            String getLoginInfo_list = getLoginInfo_sb.toString();
            System.out.println("getLoginInfo_list :: "+getLoginInfo_list+"\n");
            
            JsonParser jsonParser = new JsonParser();
            JsonObject returndata = (JsonObject)jsonParser.parse(getLoginInfo_list).getAsJsonObject();
            GetisAutoLogin = returndata.get("IsAutoLogin").toString();
            GetisAnonymous = returndata.get("IsAnonymous").toString();
            
            System.out.println("GetisAutoLogin :: "+GetisAutoLogin+"\n");
            System.out.println("GetisAnonymous :: "+GetisAnonymous+"\n");

            conn.disconnect();
            
        } catch (MalformedURLException e) {
            
	} catch (IOException e) {    
            
        }  
    }
    
    public void CheckService() throws MalformedURLException, IOException{ 
        
        String STATE_PREFIX = "STATE              : ";

        String s = "sc query AcroRedAgentService";
        Process p=Runtime.getRuntime().exec(s); 
        System.out.println("String s :: "+s+"\n");
        // check that the temp string contains the status prefix
        int ix = s.indexOf(STATE_PREFIX);
        if (ix >= 0) {
          // compare status number to one of the states
          String stateStr = s.substring(ix+STATE_PREFIX.length(), ix+STATE_PREFIX.length() + 1);
          System.out.println("String stateStr :: "+ stateStr +"\n");
          int state = Integer.parseInt(stateStr);
          System.out.println("int state :: "+ state +"\n");
          switch(state) {
            case (1): // service stopped
                System.out.println(" */*/ service stopped */*/ \n");
              break;
            case (4): // service started
                System.out.println(" */*/ service started */*/ \n");
              break;
           }
        }
        
    }
    
    
}
