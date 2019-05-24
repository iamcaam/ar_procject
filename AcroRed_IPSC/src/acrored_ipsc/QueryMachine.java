/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_ipsc;

import com.google.gson.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//import org.apache.commons.codec.binary.StringUtils;

/**
 *
 * @author sabrina_yeh
 */
public class QueryMachine extends Thread{
    /***    顯示文字對應表    ***/
    public Map<String, String> WordMap=new HashMap<>();  
    
    public String iscsicliIP;
    public String CurrentUserName;
    public String CurrentPasseard;
    public String ADUserName;
    public String ADPasseard;
    private String UserName;
    private String UserPasseard;
    private String Address;
    private boolean IsAnonymous;
    private boolean IsAutoLogin;
    private String target_list;
    private String pname_list;
    public boolean Mount_button_change=true ;
    public boolean Mount_status_change=false ;
    public int MountResponseCode;
    public int MountClear_ResponseCode;
    private String Storage_UserID;
    private String Storage_Key;
    private String Storage_Result;
    public String Storage_Status;
    private String outputstring ;
    public boolean Format_change=true ;
    public int Format_ResponseCode;
    public int Login_ResponseCode;
    public boolean MountCode200=false ;
    public boolean StatusCode6=false ;
    public String xml;
 
    List<String> PhysicalName_List =FXCollections.observableArrayList() ;
    
    // 2019.03.08 william IPSC新增記住上一次連線的IP
    Map dictTargetIP = new HashMap();
    
    public QueryMachine(Map<String, String> LangMap){
        WordMap=LangMap;
       // uniqueKey=Ukey;
    }    

    public void LoginStorage (String IPAddress,String Uname,String Password,boolean isAnonymous, boolean isAutoLogin) throws MalformedURLException, IOException, SAXException, ParserConfigurationException, UnsupportedEncodingException, NoSuchAlgorithmException, Exception{
        URL url;        
        iscsicliIP=IPAddress;//load iscsicliIP
        UserName=string_to_utf8(Uname); //load UserName
        System.out.println("Uname : "+Uname+"\n");
        System.out.println("UserName : "+UserName+"\n");
        UserPasseard=string_to_utf8(Password);//load UserPasseard
        System.out.println("Password : "+Password+"\n");
        System.out.println("UserPasseard : "+UserPasseard+"\n");

        System.out.println("---------------LoginStorage-----------------\n");
 
        //inputText=./mgtap 0 0 get_iscsi_temp_key {username} {password}
        String query="inputText=./mgtap 0 0 get_iscsi_temp_key "+ UserName +" "+ UserPasseard ;
        System.out.println("query : "+query+"\n");     
        
        /***bypass SSL 網頁認證問題,系統SSL之Certification key非信任組織發行*******/
        bypassSSL();
        System.out.println("** bypassSSL() **\n");
        
        try {
            
            //URL：https://{ip}:{port}/upperCase.php
            url = new URL("https://"+iscsicliIP+"/upperCase.php");  
            System.out.println("URL : "+url+"\n"); 
            
            String length = String.valueOf((url + query).getBytes().length);            
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();  //建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線            
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");//利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種            
	    //conn.setRequestProperty("Accept", "application/json"); //設置接收數據格式
            //conn.setRequestProperty("Content-Type", "application/json");//設置發送數據的格式
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8"); 
            //conn.setRequestProperty("Content-Type", "*/*");//設置發送數據的格式("Content-Type", "application/json; charset=UTF-8")
            conn.setRequestProperty("Accept", "*/*"); //設置接收數據格式
            conn.setRequestProperty("Content-length", length);//("Content-length", String.valueOf(QueryLogin_query.getBytes().length))
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Connection", "Keep-Alive");//維持長連接
            conn.setUseCaches (false);
            conn.setInstanceFollowRedirects(true);

            //寫入資料            
            try (OutputStream LoginStorage_out = conn.getOutputStream()) {
                LoginStorage_out.write(query.getBytes());
                LoginStorage_out.flush();
                LoginStorage_out.close();
                //System.out.println("output LoginStorage_out : "+LoginStorage_out+"\n");
            } 
            
            System.out.println("URL : "+url+"\n"); 
            //System.out.println("conn.getOutputStream() : "+conn.getOutputStream()+"\n");
            System.out.println("Resp Code : "+conn.getResponseCode()+"\n");
            System.out.println("Resp Message:"+ conn.getResponseMessage()+"\n");

           /*********跳出警告訊息:401 404**********/
            //connCode = conn.getResponseCode();
            //AlertChangeLang(connCode);       
           /*******************************************/

           //讀資料
            InputStream LoginStorage_is = conn.getInputStream();
            //System.out.println("InputStream is : "+LoginStorage_is+"\n");
            StringBuilder LoginStorage_sb;
            String LoginStorage_line;  

                try (BufferedReader LoginStorage_br = new BufferedReader(new InputStreamReader(LoginStorage_is))) {              
                    LoginStorage_sb = new StringBuilder("");
                    while ((LoginStorage_line = LoginStorage_br.readLine()) != null) {            
                        LoginStorage_sb.append(LoginStorage_line);         
                    }                 
                    //System.out.println("return sb: "+LoginStorage_sb.toString()+"\n");
                    LoginStorage_br.close();
                }

            xml = LoginStorage_sb.toString();  
            System.out.println("LoginStorage_data: "+ xml +"\n");
            /*** 讀取 XML格式的字串內容 ***/
            //參考 : http://stackoverflow.com/questions/4076910/how-to-retrieve-element-value-of-xml-using-java
            //參考 : https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(new InputSource(new StringReader(xml)));           
            //document.getDocumentElement().normalize();
            //System.out.println("Root element :" + document.getDocumentElement().getNodeName()+"\n");            
            NodeList nList = document.getElementsByTagName("IscsiTempKey");
            for (int temp = 0; temp < nList.getLength(); temp++) {

		Node nNode = nList.item(temp);
		//System.out.println("Current Element :" + nNode.getNodeName()+"\n");
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;
                        Storage_UserID = eElement.getAttribute("UserID");
                        Storage_Key = eElement.getAttribute("Key");
                        Storage_Result = eElement.getAttribute("Result");
                        
			System.out.println("UserID : " + Storage_UserID +"\n");
                        System.out.println("Key : " + Storage_Key +"\n");
                        System.out.println("Result : " + Storage_Result +"\n");                        
		}
            }            
            NodeList Result_nList = document.getElementsByTagName("Result");
            for (int temp = 0; temp < Result_nList.getLength(); temp++) {

		Node nNode = Result_nList.item(temp);
		//System.out.println("Current Element :" + nNode.getNodeName()+"\n");

		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;
                        Storage_Status = eElement.getAttribute("Status") ;
			System.out.println("Status : " + Storage_Status +"\n");                       
		}
            }
            //若是 Storage_Status = 0，解析 IscsiTempKey 中的 Result，其列舉值如下 
            if(Storage_Status.equals("0")){  
                StatusCode6 = true;
                System.out.println(" ** is IPSC ** \n");                  
                //若是 Normal=0 || Other=99 ，則此帳號是AD帳號。
                if(Storage_Result.equals("0") || Storage_Result.equals("99")){  
                    
                    if(Storage_Key!=null && !Storage_Key.isEmpty() ){  //string != null && !string.isEmpty()
                        
                        System.out.println(" ** 此帳號是AD帳號 ** \n");
                        //string.Format("acrored::{0}::derorca", UserID )
                        ADUserName= "acrored::"+ Storage_UserID +"::derorca";
                        System.out.println("ADUserName : " + ADUserName +"\n");
                        //將Key (此為UTF-8 的格式)，轉換成一般文字，並視為新的密碼 
                        ADPasseard = utf8_to_string(Storage_Key);
                        System.out.println("ADPasseard : " + ADPasseard +"\n");
                        SetLoginInfo(iscsicliIP , ADUserName, ADPasseard, isAnonymous, isAutoLogin);    
                        
                    }else{
                        
                        System.out.println(" ** 此帳號'是'AD帳號，但 XML 中有空值  ** \n");
                        SetLoginInfo(iscsicliIP , Uname, Password, isAnonymous, isAutoLogin);              
                        
                    }                                  
                }else{                    
                    
                    System.out.println(" ** 此帳號'非'AD帳號 ** \n");
                    SetLoginInfo(iscsicliIP , Uname, Password, isAnonymous, isAutoLogin);
                    
                }                
            }if(Storage_Status.equals("6")){
                StatusCode6 = true;
                System.out.println("***Storage_Status*** : " + Storage_Status +"\n"); 
            } 
            if(!Storage_Status.equals("0") && !Storage_Status.equals("6") ){
                System.out.println(" -- System Error --  \n"); 
                StatusCode6 = false;
            }

            conn.disconnect();            
            
        } catch (MalformedURLException e) {            
	}   
    }     
    
    public void SetLoginInfo (String IPAddr,String Cname,String CPassword,boolean isAnonymous, boolean isAutoLogin ) throws MalformedURLException, IOException{
        URL url;        
        iscsicliIP = IPAddr;//load CurrentIP
        CurrentUserName = Cname; //load CurrentUserName
        CurrentPasseard = CPassword;//load CurrentPasseard
        IsAnonymous = isAnonymous;
        IsAutoLogin = isAutoLogin;

        System.out.println("---------------LoginInfo-----------------\n");
        String address = null;
        address = InetAddress.getLocalHost().getHostAddress();
        //address="127.0.0.1";
        //address="192.168.92.10";
        
        JSONObject obj =new JSONObject();
        obj.put("Username", CurrentUserName);
        obj.put("Password", CurrentPasseard);   
        obj.put("IpAddress", iscsicliIP);
        obj.put("Port", 443); //數值 int
        obj.put("IsAnonymous", IsAnonymous);
        obj.put("IsAutoLogin", IsAutoLogin);
        String QueryLogin_query=obj.toString();
        System.out.println("query : "+QueryLogin_query+"\n");         

        try {
            
            //URL : http://{IP}:9000/api/iscsicli/account/info
            url = new URL("http://"+"localhost"+":27678"+"/api"+"/iscsicli"+"/account/"+"info");   
            String length = String.valueOf((url + QueryLogin_query).getBytes().length);            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  //建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線            
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");//利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種            
	    conn.setRequestProperty("Accept", "application/json"); //設置接收數據格式
            conn.setRequestProperty("Content-Type", "application/json");//設置發送數據的格式("Content-Type", "application/json; charset=UTF-8")
            //conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
            conn.setRequestProperty("Content-length", length);//("Content-length", String.valueOf(QueryLogin_query.getBytes().length))
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Connection", "Keep-Alive");//維持長連接
            conn.setUseCaches (false);
            conn.setInstanceFollowRedirects(true);
                      
            //寫入資料            
            try (OutputStream QueryLogin_out = conn.getOutputStream()) {
                QueryLogin_out.write(QueryLogin_query.getBytes());
                QueryLogin_out.flush();
                QueryLogin_out.close();
                //System.out.println("output QueryLogin_out : "+QueryLogin_out+"\n");
            } 
            
            Login_ResponseCode = conn.getResponseCode() ;
            
            System.out.println("URL : "+url+"\n"); 
            //System.out.println("conn.getOutputStream() : "+conn.getOutputStream()+"\n");
            System.out.println("Login_ResponseCode :  "+Login_ResponseCode+"\n");
            System.out.println("Login_Resp Message:"+ conn.getResponseMessage()+"\n");
            
           /*********跳出警告訊息:401 404**********/
            //connCode = conn.getResponseCode();
            //AlertChangeLang(connCode);       
           /*******************************************/

           //讀資料
            InputStream QueryLogin_is = conn.getInputStream();
            //System.out.println("InputStream is : "+QueryLogin_is+"\n");
            StringBuilder QueryLogin_sb;
            String QueryLogin_line;  

                try (BufferedReader QueryLogin_br = new BufferedReader(new InputStreamReader(QueryLogin_is))) {              
                    QueryLogin_sb = new StringBuilder("");
                    while ((QueryLogin_line = QueryLogin_br.readLine()) != null) {            
                        QueryLogin_sb.append(QueryLogin_line);         
                    }                 
                    //System.out.println("return br: "+QueryLogin_br.readLine()+"\n");
                    //System.out.println("return sb: "+QueryLogin_sb.toString()+"\n");

                    QueryLogin_br.close();
                }

            String data = QueryLogin_sb.toString();  
            System.out.println("data: "+ data +"\n");          
            if(data.equals("true")){
                
                DoListTarget();
                
            }else{
                System.out.println("若iscsicliIP不合法時 回false : "+data+"\n");
            }
            
            conn.disconnect();  
            
        } catch (MalformedURLException e) {            
	}   
    } 
    
    public void DoListTarget() throws MalformedURLException, IOException{ //String addr
        URL url;
        try{
            // http://{IP}:9000/api/iscsicli/targets 
            //http://localhost:9000/api/iscsicli/targets 
            url = new URL("http://"+"localhost"+":27678"+"/api/"+"iscsicli/"+"targets");      
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  //建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線    
            conn.setRequestMethod("GET");//利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
	    conn.setRequestProperty("Accept", "application/json");   
            //conn.setRequestProperty("Accept", "*/*"); 
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            //conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Connection", "Keep-Alive");//維持長連接.
            conn.connect(); // 建立實際的連接

            System.out.println("-------------------DoListTarget----------------------\n");
            System.out.println("DoListTarget - URL : "+url+"\n"); 
            System.out.println("DoListTarget - Resp Code:"+conn.getResponseCode()+"\n"); 
            System.out.println("DoListTarget - Resp Message:"+ conn.getResponseMessage()+"\n");

            InputStream DoListTarget_is = conn.getInputStream();
            //System.out.println("InputStream DoListTargetis : "+DoListTarget_is+"\n");
            StringBuilder DoListTarget_sb;
            String DoListTarget_line;  

            try (BufferedReader DoListTarget_br = new BufferedReader(new InputStreamReader(DoListTarget_is))) {              
                DoListTarget_sb = new StringBuilder("");
                while ((DoListTarget_line = DoListTarget_br.readLine()) != null) {            
                    DoListTarget_sb.append(DoListTarget_line);         
                }                 
                //System.out.println("return DoListTarget_br: "+DoListTarget_br.readLine()+"\n");
                //System.out.println("DoListTarget - return DoListTarget_sb: "+DoListTarget_sb.toString()+"\n");             
         
                DoListTarget_br.close();
            }
            String target_list = DoListTarget_sb.toString();
            System.out.println("DoListTarget - target_list: "+target_list+"\n"); 
            LoginmountTarget(target_list);
            
            conn.disconnect();
            
        } catch (MalformedURLException e) {
            
	} catch (IOException e) {    
            
        }  
    }
    
    public void LoginmountTarget (String Target_List) throws MalformedURLException, IOException{
        URL url;        
        JSONArray list = new JSONArray();
        System.out.println("-------------------LoginmountTarget----------------------\n");
        int len = 0;
        len = Target_List.split("[\\[\",\"\\]\\s]+").length;
        System.out.println("len : "+len+"\n");        
        if(len != 0){               
            for(String p : Target_List.split("[\\[\",\"\\]\\s]+")) {
                String intern = p;                
                //string != null && !string.isEmpty()
                if(intern != null && !intern.isEmpty()){            //若有值,會產生一個空值,這邊要排除空值傳入JSONArray中                  
                    System.out.println("intern : "+ intern +"\n");
                    list.add(intern);
                    target_list=list.toString();                    
                }
            }
        }else{            
        }
        System.out.println("target_list : "+target_list+"\n");         
        
        try {
            
            //URL : http://{IP}:9000/api/iscsicli/targets/login
            url = new URL("http://"+"localhost"+":27678"+"/api"+"/iscsicli"+"/targets/"+"login");    
            String length = String.valueOf((url + target_list).getBytes().length);            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  //建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線            
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");//利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種            
	    conn.setRequestProperty("Accept", "application/json"); //設置接收數據格式
            conn.setRequestProperty("Content-Type", "application/json");//設置發送數據的格式("Content-Type", "application/json; charset=UTF-8")
            //conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
            conn.setRequestProperty("Content-length", length);//("Content-length", String.valueOf(query.getBytes().length))
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Connection", "Keep-Alive");//維持長連接
            conn.setUseCaches (false);
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
                      
            //寫入資料            
            try (OutputStream out = conn.getOutputStream()) {
                out.write(target_list.getBytes());
                out.flush();
                out.close();
                //System.out.println("output out : "+out+"\n");
            } 
         
            System.out.println("URL : "+url+"\n"); 
            //System.out.println("LoginmountTarget - conn.getOutputStream() : "+conn.getOutputStream()+"\n");
            System.out.println("LoginmountTarget - Resp Code : "+conn.getResponseCode()+"\n");
            System.out.println("LoginmountTarget - Resp Message:"+ conn.getResponseMessage()+"\n");

           /*********跳出警告訊息:401 404**********/
            //connCode = conn.getResponseCode();
            //AlertChangeLang(connCode);       
           /*******************************************/
           
           //讀資料
            InputStream is = conn.getInputStream();
            //System.out.println("InputStream is : "+is+"\n");
            StringBuilder sb;
            String line;  

                try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {              
                    sb = new StringBuilder("");
                    while ((line = br.readLine()) != null) {            
                        sb.append(line);         
                    }                 
                    //System.out.println("LoginmountTarget br: "+br.readLine()+"\n");
                    System.out.println("LoginmountTarget sb: "+sb.toString()+"\n");
                    br.close();
                }

            String data = sb.toString();       
            DoListSession();
            
            conn.disconnect();
            
        } catch (MalformedURLException e) {            
	}   
    } 
    
    public void DoListSession() throws MalformedURLException, IOException{ //String addr
        URL url;
       
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
            
            System.out.println("-------------------DoListSession----------------------\n");
            System.out.println("DoListSession - URL : "+url+"\n"); 
            System.out.println("DoListSession - Resp Code:"+conn.getResponseCode()+"\n"); 
            System.out.println("DoListSession - Resp Message:"+ conn.getResponseMessage()+"\n");
            
            InputStream DoListSession_is = conn.getInputStream();
            //System.out.println("InputStream DoListSession : "+DoListSession_+"\n");
            StringBuilder DoListSession_sb;
            String DoListSession_line;  

            try (BufferedReader DoListSession_br = new BufferedReader(new InputStreamReader(DoListSession_is))) {              
                DoListSession_sb = new StringBuilder("");
                while ((DoListSession_line = DoListSession_br.readLine()) != null) {            
                    DoListSession_sb.append(DoListSession_line);         
                }                 
                //System.out.println("return DoListTarget_br: "+DoListTarget_br.readLine()+"\n");
                //System.out.println("DoListTarget - return DoListTarget_sb: "+DoListTarget_sb.toString()+"\n");             
         
                DoListSession_br.close();
            }
            String session_list = DoListSession_sb.toString();
            System.out.println("DoListSession - session_list: "+session_list+"\n");       
            //string != null && !string.isEmpty()  
            int session_length = session_list.split("[\\[\",\"\\]\\s]+").length;
            System.out.println("session_length : "+session_length+"\n"); 
            MountResponseCode =conn.getResponseCode();
            MountClear_ResponseCode = -1;// 2019.03.08 william IPSC新增記住上一次連線的IP
            
            if(session_length == 0){
                System.out.println(" Call ClearPortals()  \n");
                ClearPortals();
            }else{
                if( MountResponseCode == 200){ //若成功掛載,則打開button                    
                    Mount_button_change=false ;
                    MountCode200 = true;
                    Mount_status_change= true;
                    
                    // 2019.03.08 william IPSC新增記住上一次連線的IP
                    for(int i = 0; i < 3; i++) {
                        CheckDiskNeedFormat();
                        try {
                            sleep(2000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(QueryMachine.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                }
            }
            conn.disconnect();
            
        } catch (MalformedURLException e) {
            
	} catch (IOException e) {    
            
        }  
    }
  
    public void ClearPortals () throws MalformedURLException, IOException{ //String addr
        URL url; 
        System.out.println("-----session = null ; Call ClearPortals()-----\n");
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
            MountClear_ResponseCode =conn.getResponseCode();
            if( MountClear_ResponseCode >= 200 && MountResponseCode <= 206){ //若掛載失敗,則鎖住button      
          
                Mount_button_change=true ;  
                Mount_status_change= false;
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
            conn.connect(); // 建立實際的連接
            
            System.out.println("-------------------CheckDiskNeedFormat----------------------\n");
            //System.out.println("CheckDiskNeedFormat - URL : "+url+"\n"); 
            System.out.println("CheckDiskNeedFormat - Resp Code:"+conn.getResponseCode()+"\n"); 
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
            
            int CheckCount = 0; // 2019.03.08 william IPSC新增記住上一次連線的IP
            
            for (int i = 0; i < CheckArr.size(); i++) {
                
                JsonObject Checkterm = CheckArr.get(i).getAsJsonObject();
                String PName=Checkterm.get("PhysicalName").getAsString();
                //System.out.println("PhysicalName: "+PName+"\n");
                String Partition=Checkterm.get("HavePartition").toString();
                //System.out.println("HavePartition: "+Partition+"\n");

                CheckCount = 0; // 2019.03.08 william IPSC新增記住上一次連線的IP
                if(Partition.equals("false")){   //false        
                    // 2019.03.08 william IPSC新增記住上一次連線的IP
                    for(int j = 0; j < PhysicalName_List.size(); j++) {
                        if(PhysicalName_List.get(j).equals(PName))
                            CheckCount++;
                    }                                                                           
                    
                    if(CheckCount==0)
                        PhysicalName_List.add(PName);                      
                }
               
            }
            System.out.println("PhysicalName_List : "+ PhysicalName_List +"\n");
            /*
            if(PhysicalName_List != null && !PhysicalName_List.isEmpty()){
                System.out.println("-------PhysicalName_List != null------\n");
                FormatDisk(PhysicalName_List);
            }
            */
            conn.disconnect();           

        } catch (MalformedURLException e) {
            
	} catch (IOException e) {    
            
        }  
    }    
   
    public void FormatDisk (List PName_List) throws MalformedURLException, IOException{
        URL url;       
        JSONArray listArr = new JSONArray();
        for(Object p : PName_List) {
            listArr.add(p);
            pname_list=listArr.toString();         
        }
        System.out.println("pname_list : "+pname_list+"\n");   

        try {
            
            //URL : Http://{ip}:9000/api/system/disks/format
            url = new URL("http://"+"localhost"+":27678"+"/api"+"/system"+"/disks/"+"format");   
            String length = String.valueOf((url + pname_list).getBytes().length);            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  //建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線            
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");//利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種            
	    conn.setRequestProperty("Accept", "application/json"); //設置接收數據格式
            conn.setRequestProperty("Content-Type", "application/json");//設置發送數據的格式("Content-Type", "application/json; charset=UTF-8")
            //conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
            conn.setRequestProperty("Content-length", length);//("Content-length", String.valueOf(QueryLogin_query.getBytes().length))
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Connection", "Keep-Alive");//維持長連接
            conn.setUseCaches (false);
            conn.setInstanceFollowRedirects(true);
            conn.connect(); 
                      
            //寫入資料            
            try (OutputStream Format_out = conn.getOutputStream()) {
                Format_out.write(pname_list.getBytes());
                Format_out.flush();
                Format_out.close();
            } 

            System.out.println("Format - URL : "+url+"\n"); 
            System.out.println("Format - conn.getOutputStream() : "+conn.getOutputStream()+"\n");
            System.out.println("Format - Resp Code : "+conn.getResponseCode()+"\n");
            System.out.println("Format - Resp Message:"+ conn.getResponseMessage()+"\n");

           /*********跳出警告訊息:401 404**********/
            //connCode = conn.getResponseCode();
            //AlertChangeLang(connCode);       
           /*******************************************/

           //讀資料
            InputStream Format_is = conn.getInputStream();
            StringBuilder Format_sb;
            String Format_line;  

                try (BufferedReader Format_br = new BufferedReader(new InputStreamReader(Format_is))) {              
                    Format_sb = new StringBuilder("");
                    while ((Format_line = Format_br.readLine()) != null) {            
                        Format_sb.append(Format_line);         
                    }                 
                    //System.out.println("return br: "+QueryLogin_br.readLine()+"\n");
                    System.out.println("return sb: "+Format_sb.toString()+"\n");

                    Format_br.close();
                }

            String data = Format_sb.toString();  
            System.out.println("data: "+ data +"\n");
            
            Format_ResponseCode = conn.getResponseCode();
            System.out.println("Format - Resp Code : "+ Format_ResponseCode +"\n");
            
            conn.disconnect();            
          
        } catch (MalformedURLException e) {            
	}   
    } 
    
    public void SaveLoginInformation (String IPAddr,String Cname,String CPassword,boolean isAnonymous, boolean isAutoLogin ) throws MalformedURLException, IOException{
        URL url;        
        iscsicliIP = IPAddr;//load CurrentIP
        CurrentUserName = Cname; //load CurrentUserName
        CurrentPasseard = CPassword;//load CurrentPasseard
        IsAnonymous = isAnonymous;
        IsAutoLogin = isAutoLogin;

        System.out.println("---------------SaveLoginInformation-----------------\n");
        
        JSONObject obj =new JSONObject();
        obj.put("Username", CurrentUserName);
        obj.put("Password", CurrentPasseard);   
        obj.put("IpAddress", iscsicliIP);
        obj.put("Port", 443); //數值 int
        obj.put("IsAnonymous", IsAnonymous);
        obj.put("IsAutoLogin", IsAutoLogin);
        String QueryLogin_query=obj.toString();
        System.out.println("SaveLoginInformation -- query : "+QueryLogin_query+"\n");         

        try {
            
            //URL : http://{IP}:9000/api/iscsicli/account/info
            url = new URL("http://"+"localhost"+":27678"+"/api"+"/iscsicli"+"/account/"+"info");   
            String length = String.valueOf((url + QueryLogin_query).getBytes().length);            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  //建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線    
            conn.setRequestMethod("PUT");//利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Content-Type", "application/json");//設置發送數據的格式("Content-Type", "application/json; charset=UTF-8")
            conn.setRequestProperty("Content-length", length);
            conn.setRequestProperty("Connection", "Keep-Alive");//維持長連接
            //conn.connect();    
            conn.setUseCaches (false);
            conn.setInstanceFollowRedirects(true);

            try (OutputStream out2 = conn.getOutputStream()) {
                out2.write(QueryLogin_query.getBytes());
                out2.flush();
                out2.close();
                //System.out.println("SetVDOnline-output out : "+out2+"\n");
                      
                System.out.println("SaveLoginInformation-URL : "+url+"\n"); 
                //System.out.println("SetVDOnline-conn.getOutputStream() : "+conn_setVDonline.getOutputStream()+"\n");
                System.out.println("SaveLoginInformation-Resp Code : "+conn.getResponseCode()+"\n");
                System.out.println("SaveLoginInformation-Resp Message:"+ conn.getResponseMessage()+"\n");
                
                InputStream is2 = conn.getInputStream();           
                StringBuilder sb2;
                String line2;  

                try (BufferedReader br2 = new BufferedReader(new InputStreamReader(is2))) {              
                    sb2 = new StringBuilder("");
                    while ((line2 = br2.readLine()) != null) {            
                        sb2.append(line2);         
                    }                 
                   //System.out.println("SetVDOnline-return br: "+br2.readLine()+"\n");
                   //System.out.println("SetVDOnline-return sb: "+sb2.toString()+"\n");         
                    br2.close();
                }

                String Sdata = sb2.toString();  
                System.out.println(" Sdata : "+Sdata+"\n");                
                conn.disconnect();  
            }
        } catch (MalformedURLException e) {            
	}   
         
    }
    
    /***bypass SSL 網頁認證問題,系統SSL之Certification key非信任組織發行*******/
    public void bypassSSL() throws MalformedURLException, IOException{ 
        
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
            @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {  }
            @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {  }
       }};
         
        try { 
            
         SSLContext sc = SSLContext.getInstance("SSL");
         sc.init(null, trustAllCerts, new java.security.SecureRandom());
         HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
         
         } catch (KeyManagementException | NoSuchAlgorithmException e){
        }

         // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }    
        };
         HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
    
    public static String string_to_utf8(String strText)throws Exception{
        char c;
        StringBuilder str = new StringBuilder();
        int intAsc;
        String strHex;
        for (int i = 0; i < strText.length(); i++)
        {
            c = strText.charAt(i);
            intAsc = (int) c;
            strHex = Integer.toHexString(intAsc);
            str.append(strHex);
        }        
        return str.toString().toUpperCase();
    }
    
    public static String utf8_to_string(String s) {  
        if (s == null || s.equals("")) {  
            return null;  
        }            
        try {  
            s = s.toUpperCase();    
            int total = s.length() / 2;  
            int pos = 0;    
            byte[] buffer = new byte[total];  
            for (int i = 0; i < total; i++) {    
                int start = i * 2;    
                buffer[i] = (byte) Integer.parseInt(  
                s.substring(start, start + 2), 16);  
                pos++;  
            }    
            return new String(buffer, 0, pos, "UTF-8");  
            
        } catch (UnsupportedEncodingException e) {  
        }  
        return s;  
    }
    
    // 2019.03.08 william IPSC新增記住上一次連線的IP
    public void GetServiceResponseData() throws MalformedURLException, IOException {
        System.out.println("************ 取得Session ************");      
        try {
            URL url = new URL("http://localhost:27678/api/iscsicli/sessions/detail"); 
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
	    conn.setRequestProperty("Accept", "application/json");   
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.connect();

            InputStream _getSession_IS = conn.getInputStream();
            StringBuilder _getSession_SB;
            String _getSession_line;  

            try (BufferedReader DoListSession_BR = new BufferedReader(new InputStreamReader(_getSession_IS))) {              
                _getSession_SB = new StringBuilder("");
                while ((_getSession_line = DoListSession_BR.readLine()) != null) {            
                    _getSession_SB.append(_getSession_line);         
                }                 
         
                DoListSession_BR.close();
            }
            
            String sessionData = _getSession_SB.toString();   
            JsonParser sessionJP = new JsonParser();
            //JsonObject returndata = (JsonObject)jsonParser.parse(data).getAsJsonObject(); 
            JsonArray jsonArr = (JsonArray) sessionJP.parse(sessionData);     
            
            System.out.println("jsonArr: " + jsonArr);  
            GB.dictTargetIP.clear();
            
            JsonObject term;
            
            for (int i = 0; i < jsonArr.size(); i++) {
                term = jsonArr.get(i).getAsJsonObject(); 
                dictTargetIP.put(term.get("TargetName").getAsString(), term.get("IpNPort").getAsString());
                System.out.println("TargetName: " + term.get("TargetName").getAsString() + " SessionID : " + term.get("SessionID").getAsString() + " IpNPort : " + term.get("IpNPort").getAsString());                
            }
            
            GB.dictTargetIP = dictTargetIP;
            
            conn.disconnect();
            
        } catch (MalformedURLException e) {} catch (IOException e) {}  
    }
      
    public void SetServiceResponseData (Map _dict) throws MalformedURLException, IOException {       
        JSONArray arr = new JSONArray();
        
        Iterator iter = _dict.entrySet().iterator();
        
        while(iter.hasNext()) {            
            JSONObject jsonValue = new JSONObject(); //https://stackoverflow.com/questions/27122447/json-from-java-with-multiple-values
            Map.Entry entry = (Map.Entry)iter.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            jsonValue.put("TargetName", key);
            jsonValue.put("IpNPort", value); 
            arr.add(jsonValue);
            System.out.println("key: " + key + " value : " + value);                
        }        
        System.out.println("----------------------------------------------------");                
        System.out.println("arr: " + arr);                
        String _query = arr.toString();
        
        try {
            URL url = new URL("http://localhost:27678/api/iscsicli/sessions/prefer");              
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
	    conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-length", String.valueOf((url + _query).getBytes().length));
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches (false);
            conn.setInstanceFollowRedirects(true);
            conn.connect();
                      
            
            //寫入資料            
            try (OutputStream _out = conn.getOutputStream()) {
                _out.write(_query.getBytes());
                _out.flush();
                _out.close();
            } 
            
            System.out.println("************ 送出Session ************");
            System.out.println("*************************************");
            System.out.println("Response Code: "+ conn.getResponseCode());
            System.out.println("*************************************");
            
            conn.disconnect();              
        } catch (MalformedURLException e) {}   
    } 
    
//    public void AutoMountUnmountCheckBox() throws MalformedURLException, IOException {
//        System.out.println("************ 取消自動掛載 ************");      
//        try {
//            URL url = new URL("http://localhost:27678/api/iscsicli/account/info"); 
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//	    conn.setRequestProperty("Accept", "application/json");   
//            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
//            conn.setRequestProperty("Connection", "Keep-Alive");
//            conn.connect();
//
//            InputStream _getSession_IS = conn.getInputStream();
//            StringBuilder _getSession_SB;
//            String _getSession_line;  
//
//            try (BufferedReader DoListSession_BR = new BufferedReader(new InputStreamReader(_getSession_IS))) {              
//                _getSession_SB = new StringBuilder("");
//                while ((_getSession_line = DoListSession_BR.readLine()) != null) {            
//                    _getSession_SB.append(_getSession_line);         
//                }                 
//         
//                DoListSession_BR.close();
//            }
//            
//            String sessionData = _getSession_SB.toString();   
//            JsonParser sessionJP = new JsonParser();
//            //JsonObject returndata = (JsonObject)jsonParser.parse(data).getAsJsonObject(); 
//            JsonArray jsonArr = (JsonArray) sessionJP.parse(sessionData);     
//            
//            System.out.println("jsonArr: " + jsonArr);  
//            GB.dictTargetIP.clear();
//            
//            JsonObject term;
//            
//            for (int i = 0; i < jsonArr.size(); i++) {
//                term = jsonArr.get(i).getAsJsonObject(); 
//                dictTargetIP.put(term.get("TargetName").getAsString(), term.get("IpNPort").getAsString());
//                System.out.println("TargetName: " + term.get("TargetName").getAsString() + " SessionID : " + term.get("SessionID").getAsString() + " IpNPort : " + term.get("IpNPort").getAsString());                
//            }
//            
//            GB.dictTargetIP = dictTargetIP;
//            
//            conn.disconnect();
//            
//        } catch (MalformedURLException e) {} catch (IOException e) {}  
//    }    
//        
    
}
    

