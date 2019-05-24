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
import org.json.simple.JSONArray;

/**
 *
 * @author victor
 */
public class UnMount extends Thread{
    /***    顯示文字對應表    ***/
    public Map<String, String> WordMap=new HashMap<>();
    
    private String session_list;
    private String Checksession_list;
    private String Address;
    public boolean UnMount_button_change=false ;
    public int UnMountResponseCode;
    public boolean Check_UnMount_failed=false ;
    public boolean UnMount_status_change=true ;
    
    public UnMount(Map<String, String> LangMap){
        WordMap=LangMap;
    }
    
    
    public void DoUnmoutListSession() throws MalformedURLException, IOException{ //String addr
        URL url;
        
        System.out.println("---------------DoUnmoutListSession----------------\n");
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

            System.out.println("Unmout - URL : "+url+"\n"); 
            System.out.println("Unmout - Resp Code:"+conn.getResponseCode()+"\n"); 
            System.out.println("Unmout - Resp Message:"+ conn.getResponseMessage()+"\n");

            InputStream Unmout_is = conn.getInputStream();
            //System.out.println("InputStream DoListSession : "+DoListSession_+"\n");
            StringBuilder Unmout_sb;
            String Unmout_line;  

            try (BufferedReader Unmout_br = new BufferedReader(new InputStreamReader(Unmout_is))) {              
                Unmout_sb = new StringBuilder("");
                while ((Unmout_line = Unmout_br.readLine()) != null) {            
                    Unmout_sb.append(Unmout_line);         
                }                 
                //System.out.println("return DoListTarget_br: "+DoListTarget_br.readLine()+"\n");
                //System.out.println("DoListTarget - return DoListTarget_sb: "+DoListTarget_sb.toString()+"\n");             
         
                Unmout_br.close();
            }
            session_list = Unmout_sb.toString();
            System.out.println("Unmout - session_list: "+session_list+"\n"); 
            LogoutTargetSession(session_list);

            conn.disconnect();
            
        } catch (MalformedURLException e) {
            
	} catch (IOException e) {    
            
        }  
    }
    
    public void LogoutTargetSession (String Session_List) throws MalformedURLException, IOException{ //String addr
        URL url;
        JSONArray list = new JSONArray();  
        String sessionId = null;        
        
        System.out.println("--------------LogoutTargetSession---------------\n");
        int len = 0;
        len = Session_List.split(",").length;
        System.out.println("len : "+len+"\n");
        if(len != 0){
            //String[] uu =Session_List.split("[\\[\",\"\\]\\s]+");
            //System.out.println("uu : "+Arrays.toString(uu)+"\n");            
            for(String p : Session_List.split("[\\[\",\"\\]\\s]+")) {
                sessionId = p;
                if(sessionId != null && !sessionId.isEmpty()){     
                    System.out.println("--------------LogoutTargetSession--------------\n");
                    System.out.println("sessionId : "+ sessionId +"\n");
                    //list.add(sessionId);
                    //session_list=list.toString();     
                    try{
                        //URL : http://{IP}:9000/api/iscsicli/targets/{sessionId}
                        //http://localhost:9000/api/iscsicli/targets 
                        url = new URL("http://"+"localhost"+":27678"+"/api/"+"iscsicli/"+"targets/"+sessionId);   
                        //url = new URL("http://"+Address+":27678"+"/api/"+"iscsicli/"+"targets/"+sessionId);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  //建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線    
                        conn.setRequestMethod("DELETE");//利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
                        conn.setRequestProperty("Accept", "application/json");   
                        //conn.setRequestProperty("Accept", "*/*"); 
                        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
                        //conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
                        conn.setRequestProperty("Connection", "Keep-Alive");//維持長連接.
                        conn.setRequestProperty("Content-Type", "application/json");//設置發送數據的格式
                        conn.setDoOutput(true);
                        conn.connect(); // 建立實際的連接
        
                        //System.out.println("LogoutTargetSession - URL : "+url+"\n"); 
                        System.out.println("LogoutTargetSession - Resp Code:"+conn.getResponseCode()+"\n"); 
                        System.out.println("LogoutTargetSession - Resp Message:"+ conn.getResponseMessage()+"\n");

                        InputStream Logout_is = conn.getInputStream();
                        //System.out.println("InputStream DoListSession : "+DoListSession_+"\n");
                        StringBuilder Logout_sb;
                        String DoListSession_line;  

                        try (BufferedReader Logout_br = new BufferedReader(new InputStreamReader(Logout_is))) {              
                            Logout_sb = new StringBuilder("");
                            while ((DoListSession_line = Logout_br.readLine()) != null) {            
                                Logout_sb.append(DoListSession_line);         
                            }                 
                            //System.out.println("Logout return br: "+Logout_br.readLine()+"\n");
                            //System.out.println("Logout sb: "+ Logout_sb.toString()+"\n");             

                            Logout_br.close();
                        }
                        String return_session = Logout_sb.toString();
                        System.out.println("Logout - return_session: "+return_session+"\n"); 
                        if(return_session.equals("true")){
                            //System.out.println("return_session == true \n");
                        }else{
                            //System.out.println("return_session == false \n");
                        }
                        
                        conn.disconnect();

                    } catch (MalformedURLException e) {
                    } catch (IOException e) {    
                    }
                }                 
            }
        }else{
            
        }
        
        CheckUnmoutListSession();
        
    }
    
    public void CheckUnmoutListSession() throws MalformedURLException, IOException{ //String addr
        URL url;
        
        System.out.println("---------------CheckUnmoutListSession----------------\n");
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

            System.out.println("CheckUnmout - URL : "+url+"\n"); 
            System.out.println("CheckUnmout - Resp Code:"+conn.getResponseCode()+"\n"); 
            System.out.println("CheckUnmout - Resp Message:"+ conn.getResponseMessage()+"\n");

            InputStream CheckUnmout_is = conn.getInputStream();
            //System.out.println("InputStream DoListSession : "+DoListSession_+"\n");
            StringBuilder CheckUnmout_sb;
            String CheckUnmout_line;  

            try (BufferedReader Unmout_br = new BufferedReader(new InputStreamReader(CheckUnmout_is))) {              
                CheckUnmout_sb = new StringBuilder("");
                while ((CheckUnmout_line = Unmout_br.readLine()) != null) {            
                    CheckUnmout_sb.append(CheckUnmout_line);         
                }                 
                //System.out.println("return DoListTarget_br: "+DoListTarget_br.readLine()+"\n");
                //System.out.println("DoListTarget - return DoListTarget_sb: "+DoListTarget_sb.toString()+"\n");             
         
                Unmout_br.close();
            }
            
            Checksession_list = CheckUnmout_sb.toString();
            System.out.println("CheckUnmout - session_list: "+Checksession_list+"\n");     
            int Checksession_length = Checksession_list.split("[\\[\",\"\\]\\s]+").length;
            System.out.println("Checksession_length : "+Checksession_length+"\n");            
            if(Checksession_length == 0){
                LogoutClearPortals();                
            }else{
                Check_UnMount_failed=true;
                UnMount_status_change=true ;
            }
            
            conn.disconnect();
            
        } catch (MalformedURLException e) {
            
	} catch (IOException e) {    
            
        }  
    }
    
    public void LogoutClearPortals () throws MalformedURLException, IOException{ //String addr
        URL url;
        
        System.out.println("---------------LogoutClearPortals----------------\n");
        try{
            //URL : http://{IP}:9000/api/iscsicli/portals
            //http://localhost:9000/api/iscsicli/portals 
            url = new URL("http://"+"localhost"+":27678"+"/api/"+"iscsicli/"+"portals");    
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  //建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線    
            conn.setRequestMethod("DELETE");//利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
	    //conn.setRequestProperty("Accept", "application/json");   
            conn.setRequestProperty("Accept", "*/*"); 
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            //conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Connection", "Keep-Alive");//維持長連接.
            conn.setRequestProperty("Content-Type", "application/json");//設置發送數據的格式
            conn.setDoOutput(true);
            conn.connect(); // 建立實際的連接

            System.out.println("LogoutClearPortals - URL : "+url+"\n"); 
            System.out.println("LogoutClearPortals - Resp Code:"+conn.getResponseCode()+"\n"); 
            System.out.println("LogoutClearPortals - Resp Message:"+ conn.getResponseMessage()+"\n");

            InputStream Clear_is = conn.getInputStream();
            //System.out.println("InputStream DoListSession : "+DoListSession_+"\n");
            StringBuilder Clear_sb;
            String Clear_line;  

            try (BufferedReader Clear_br = new BufferedReader(new InputStreamReader(Clear_is))) {              
                Clear_sb = new StringBuilder("");
                while ((Clear_line = Clear_br.readLine()) != null) {            
                    Clear_sb.append(Clear_line);         
                }                 
                //System.out.println("Logout return br: "+Logout_br.readLine()+"\n");
                //System.out.println("Logout sb: "+ Logout_sb.toString()+"\n");              
                Clear_br.close();
            }
            String Clear_list = Clear_sb.toString();
            //System.out.println("Logout - Clear_list : "+Clear_list+"\n"); 
            //若卸載成功,則鎖住button 
            UnMountResponseCode =conn.getResponseCode();
            if( UnMountResponseCode == 204){               
                UnMount_button_change=true ;
                UnMount_status_change= false;
            }
            
            conn.disconnect();

        } catch (MalformedURLException e) {
            
	} catch (IOException e) {    
            
        }  
    }
    
}
