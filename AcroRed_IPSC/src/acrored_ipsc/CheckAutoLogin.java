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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;

/**
 *
 * @author victor
 */
public class CheckAutoLogin {
    /***    顯示文字對應表    ***/
    public Map<String, String> WordMap=new HashMap<>();
    public String iscsicliIP;
    public String CurrentUserName;
    public String CurrentPasseard;
    private boolean IsAnonymous;
    private boolean IsAutoLogin;
    public int AutoLogin_ResponseCode;
    
    public CheckAutoLogin(Map<String, String> LangMap){
        WordMap=LangMap;
       // uniqueKey=Ukey;
    }
    
    public void CheckAutoLoginInfo (String IPAddr,String Cname,String CPassword,boolean isAnonymous, boolean isAutoLogin ) throws MalformedURLException, IOException{
        URL url;        
        iscsicliIP = IPAddr;//load CurrentIP
        CurrentUserName = Cname; //load CurrentUserName
        CurrentPasseard = CPassword;//load CurrentPasseard
        IsAnonymous = isAnonymous;
        IsAutoLogin = isAutoLogin;

        System.out.println("---------------CheckAutoLoginInfo-----------------\n");
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
            
            AutoLogin_ResponseCode = conn.getResponseCode() ;
            
            System.out.println("URL : "+url+"\n"); 
            //System.out.println("conn.getOutputStream() : "+conn.getOutputStream()+"\n");
            System.out.println("AutoLogin_ResponseCode :  "+AutoLogin_ResponseCode+"\n");
            System.out.println("AutoLogin_Resp Message:"+ conn.getResponseMessage()+"\n");
            
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
            System.out.println("CheckAutoLoginInfo data: "+ data +"\n");          
            
            conn.disconnect();  
            
        } catch (MalformedURLException e) {            
	}   
    }
    
}
