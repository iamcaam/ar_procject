/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import static acrored_vdi_viewer.VDMLogin.IPAddress;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.simple.JSONObject;

/**
 *
 * @author victor
 */
public class TCLogin extends Thread{
    
     public Map<String, String> WordMap=new HashMap<>();
    public String CurrentIP;
    public String CurrentUserName;
    public String CurrentPasseard;
    public String VdId;    
    public String IsVdOnline;
    public String IsDefault;
    public String VdServers;  
    public String uniqueKey;
    public String VdName;
    public String CreateTime;
    public String VdStatus;
    private int connCode;
    public boolean testError;
    private Thread TCLT;
    private InetAddress IPAddr;
    public static String IPAddress;
    
    /*****Pattern:檢查IP是否符合正確格式*****/
    private static final Pattern PATTERN = Pattern.compile(
        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    
    public TCLogin(Map<String, String> LangMap){
        
        WordMap=LangMap;  
    }

     
     public void ThinClientLogin(String IPAddr,String Uname,String Password, String UniqueKey) throws MalformedURLException, IOException{
         
         URL url;        
         CurrentIP=IPAddr;//load CurrentIP
         CurrentUserName=Uname; //load CurrentUserName
         CurrentPasseard=Password;//load CurrentPasseard
         uniqueKey=UniqueKey;
         
         JSONObject obj =new JSONObject();
         obj.put("AccountName", CurrentUserName);
         obj.put("Password", CurrentPasseard);       
         String query=obj.toString();   

       /******bypass SSL 網頁認證問題,系統SSL之Certification key非信任組織發行*************/
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
          }};
         HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
         /***********************************SSL END**************************************/
       
         /***POST Start***/
        try {
          
            url = new URL("https://"+CurrentIP+"/vdi"+"/user"+"/vd/"+uniqueKey);           
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();  //建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線            
             conn.setDoOutput(true);
             conn.setDoInput(true);
             conn.setRequestMethod("POST");//利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種            
	     conn.setRequestProperty("Accept", "application/json"); //設置接收數據格式
             conn.setRequestProperty("Content-Type", "application/json");//設置發送數據的格式("Content-Type", "application/json; charset=UTF-8")
             //conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
             conn.setRequestProperty("Content-length", String.valueOf(query.getBytes().length));//("Content-length", String.valueOf(query.length()))
             conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
             conn.setRequestProperty("Connection", "Keep-Alive");//維持長連接
             conn.setUseCaches (false);
             conn.setInstanceFollowRedirects(true);
 
        try (OutputStream out = conn.getOutputStream()) {
                 out.write(query.getBytes());
                 out.flush();
                 out.close();         
        }  
        
        System.out.println("Resp Code : "+conn.getResponseCode()+"\n");
        System.out.println("Resp Message:"+ conn.getResponseMessage()+"\n");
        System.out.println("-----------------------------------------\n");
        
        /*********跳出警告訊息:401 403 404**********/
        connCode = conn.getResponseCode();
        AlertChangeLang(connCode); 
        /*******************************************/

        InputStream is = conn.getInputStream();
        StringBuilder sb;
        String line;  
            
             try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {              
                 sb = new StringBuilder("");
                 while ((line = br.readLine()) != null) {            
                     sb.append(line);         
                 }                 
           
                 br.close();
             }

	conn.disconnect();
   
        } catch (MalformedURLException e) {            
	}   
        
       
    }
     
    public  synchronized Boolean getResult (){
        return testError;
    }
    
    public  synchronized void setResult(Boolean res)
    {
        testError=res;
    }
    
    public static synchronized String getIP(){
        
        return IPAddress;
    }
    
    public static synchronized void setIP(String ip)
    {
      
        IPAddress=ip;
    } 
     
    public void Ping(String ipaddr) throws InterruptedException, IOException{
        Boolean Result=PATTERN.matcher(ipaddr).matches();
        System.out.println(" -----Result------"+Result+"\n");
        if(Result==true){
            setIP(ipaddr);
            try {                 
                doPing();
                System.out.println(" ----testError-1--: "+testError+ "\n");
                    
                if(testError==false){
                    System.out.println(" ---try---\n");
                    //runAndWait(new Runnable() {
                    Platform.runLater(new Runnable() {
                        @Override public void run() {                               
                        System.out.println(" ----------Platform-Messages-----------\n");
                        System.out.println(" Platform-testError- "+testError+"\n");
                            if("English".equals(WordMap.get("SelectedLanguage"))){
                                Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
                                alert_error.setTitle(WordMap.get("ThinClient"));
                                alert_error.setHeaderText(null);
                                alert_error.getDialogPane().setMinSize(830,Region.USE_PREF_SIZE);
                                alert_error.getDialogPane().setMaxSize(830,Region.USE_PREF_SIZE);
                                alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VM_IP_02")+"\n"                                            
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+"\n"
                                    +"                    "+" 1. "+WordMap.get("Message_Error_IP_03")+"\n"
                                    +"                    "+" 2. "+WordMap.get("Message_Error_IP_02")
                                );                                    
                                ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                                alert_error.getButtonTypes().setAll(buttonTypeOK);
                                alert_error.showAndWait();
                            }
                            if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
                                Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
                                alert_error.setTitle(WordMap.get("ThinClient"));
                                alert_error.setHeaderText(null);
                                
                                //for Win7-Dialog
                                alert_error.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
                                alert_error.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE);
                              
                                alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VM_IP_02")+"\n"                                            
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "
                                    +" 1. "+WordMap.get("Message_Error_IP_03")+"\n"
                                    +"　　　  "+" 2. "+WordMap.get("Message_Error_IP_02")
                                );                                    
                                ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                                alert_error.getButtonTypes().setAll(buttonTypeOK);
                                alert_error.showAndWait();
                            }
                        }
                    });                        
                }
                      
            } catch (IOException ex) {
                    Logger.getLogger(QueryMachine.class.getName()).log(Level.SEVERE, null, ex);
            }            
            doPing();
            TCLT=new TCLogin(WordMap);
            TCLT.start();        
            System.out.println("--Ping--testError--"+testError+" \n");      
            if(!testError){
                TCLT.notifyAll();      
            }
        }else{
            if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){       
                Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
                alert_error.setTitle(WordMap.get("ThinClient"));
                alert_error.setHeaderText(null);
              
                alert_error.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VM_IP_01")+"\n"                                            
                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_07")
                    );
                ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                alert_error.getButtonTypes().setAll(buttonTypeOK); 
                alert_error.showAndWait();
                TCLT.notifyAll(); 
            }
            if("English".equals(WordMap.get("SelectedLanguage"))){
                Alert alert_error = new Alert(Alert.AlertType.CONFIRMATION);
                alert_error.setTitle(WordMap.get("ThinClient"));
                alert_error.setHeaderText(null);
                alert_error.getDialogPane().setMinSize(550,Region.USE_PREF_SIZE);
                alert_error.getDialogPane().setMaxSize(550,Region.USE_PREF_SIZE);
                alert_error.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VM_IP_01")+"\n"                                            
                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_07")
                    );
                ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                alert_error.getButtonTypes().setAll(buttonTypeOK); 
                alert_error.showAndWait();
                TCLT.notifyAll();
            
            }
        }   
    }
    
    public boolean doPing() throws UnknownHostException, IOException{      

        if((IPAddress==null)||("".equals(IPAddress))){
            testError=false;
            setResult(testError);
            System.out.println("IPAddress=null : "+ testError +"\n");   
            return false;
        }
           
        IPAddr=InetAddress.getByName(IPAddress);
        
        if(IPAddr.isReachable(1000)){ 
            // Success Ping     
            testError=true;
            setResult(testError);
            System.out.println(" ----Success--- \n");
            System.out.println(" ----Success--: "+testError+ "\n");
            return true;
        }
         else{
            // Fail Ping                               
            testError=false;
            setResult(testError);
            System.out.println("--------Fail---------- \n");
            System.out.println(" ----Fail--: "+testError+ "\n");                    
            return false;
        }
         //return true;
     } 
    
    /****************跳出警告訊息:401 403 404*******************/
    public void AlertChangeLang(int conn){
        
        if("English".equals(WordMap.get("SelectedLanguage"))){
            //若帳號,密碼錯誤 回傳401 ,則跳出視窗通知使用者更改.
            if(conn == 401){
                //訊息:登入-帳號或密碼錯誤
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
                alert.setTitle(WordMap.get("ThinClient"));
                alert.setHeaderText(null);
                
                //for Win7-Dialog   
                alert.getDialogPane().setMinSize(580,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(580,Region.USE_PREF_SIZE);  
         
                alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VM_IP_401")+"\n"
                                     +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_User_PW")
                                    );
                ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(buttonTypeOK); 
                alert.showAndWait();

            }
            //若回傳403 ,則跳出視窗說明:使用者鎖定.
            if(conn == 403){
                //訊息:登入-使用者鎖定
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                alert.setTitle(WordMap.get("ThinClient"));
                alert.setHeaderText(null);
                alert.getDialogPane().setMinSize(520,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(520,Region.USE_PREF_SIZE);            
                alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VM_IP_403")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                                    );           
                ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(buttonTypeOK); 
                alert.showAndWait();    
            }
             //若回傳404 ,則跳出視窗說明:伺服器不存在 或 伺服器未開機.
            if(conn == 404){
                //訊息:登入-使用者鎖定
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(WordMap.get("ThinClient"));
                alert.setHeaderText(null);
                
                //for Win7-Dialog
                alert.getDialogPane().setMinSize(600,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(600,Region.USE_PREF_SIZE);
                
                alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VM_IP_404")+"\n"                                            
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+"\n"
                                    +"   "+" 1. "+WordMap.get("Message_Error_IP_04")+"\n"
                                    +"   "+" 2. "+WordMap.get("Message_Error_IP_05")
                                    );
                ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(buttonTypeOK); 
                alert.showAndWait();

            }
        }
                   
        
        if(("TraditionalChinese".equals(WordMap.get("SelectedLanguage")))||("SimpleChinese".equals(WordMap.get("SelectedLanguage")))){
            //若帳號,密碼錯誤 回傳401 ,則跳出視窗通知使用者更改.
            if(conn == 401){
                //訊息:登入-帳號或密碼錯誤
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
                alert.setTitle(WordMap.get("ThinClient"));
                alert.setHeaderText(null);              
                alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VM_IP_401")+"\n"
                                     +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_User_PW")
                                    );
                ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(buttonTypeOK); 
                alert.showAndWait();

            }
            //若回傳403 ,則跳出視窗說明:使用者鎖定.
            if(conn == 403){
                //訊息:登入-使用者鎖定
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                alert.setTitle(WordMap.get("ThinClient"));
                alert.setHeaderText(null);
                alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);           
                alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VM_IP_403")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                                    );           
                ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(buttonTypeOK); 
                alert.showAndWait();    
            }
             //若回傳404 ,則跳出視窗說明:伺服器不存在 或 伺服器未開機.
            if(conn == 404){
                //訊息:登入-使用者鎖定
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(WordMap.get("ThinClient"));
                alert.setHeaderText(null);
                
                //for Win7-Dialog
                alert.getDialogPane().setMinSize(500,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(500,Region.USE_PREF_SIZE); 
              
                alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VM_IP_404")+"\n"                                            
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "
                                    +" 1. "+WordMap.get("Message_Error_IP_04")+"\n"
                                    +"　　　  "+" 2. "+WordMap.get("Message_Error_IP_05")
                                    );
                ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(buttonTypeOK); 
                alert.showAndWait();

            }
        }
     
    }
    
    public void CheckInetAddr() throws SocketException, UnknownHostException, IOException{

        Process result = Runtime.getRuntime().exec("ifconfig eth0"); //all IP-eth0
           
        BufferedReader checkInet = new BufferedReader (new InputStreamReader(result.getInputStream()));
        String line_check = checkInet.readLine();
        while(line_check != null){
                    
            if ( line_check.matches("inet addr")==true ){  

                break; 
                
            }else{
                
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);          
                alert.setTitle(WordMap.get("Message_Login"));
                alert.setHeaderText(null);
                alert.getDialogPane().setMinSize(520,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(520,Region.USE_PREF_SIZE);           
                alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Lock")+"\n"
                                    +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_Error_IP_403")
                                    );           
                ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(buttonTypeOK); 
                alert.showAndWait(); 
  
            
            }

            line_check = checkInet.readLine();
            System.out.println(" @@@ : " + line_check +"\n");
                    
        }
                               
    }
    
    
    
}
