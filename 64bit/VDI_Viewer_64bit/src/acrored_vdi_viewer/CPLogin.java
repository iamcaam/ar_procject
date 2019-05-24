/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
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
public class CPLogin extends Thread {
    
    
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
    public int CPLconnCode;
    public boolean testError;
    private Thread CPLT;
    private InetAddress IPAddr;
    public static String IPAddress;
 
    public String CurrentIP_Port; // 2017.08.10 william IP增加port欄位，預設443
    public static int IPPort; // 2017.08.10 william IP增加port欄位，預設443
    
    /*****Pattern:檢查IP是否符合正確格式*****/
    private static final Pattern PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    // Input： ,  Output： , 功能： Class建構子
    public CPLogin(Map<String, String> LangMap){
     
        WordMap=LangMap;
       // uniqueKey=Ukey;
       //UUID 需要非同步(需固定 不能改)
    }
    // Input： 1. IP, 2. 用戶名, 3. 密碼, 4. uuid, 5. Port ,  Output： , 功能： 修改密碼     
    public void ChangePasswordLogin(String IPAddr,String Uname,String Password, String UniqueKey,String IPPort) throws MalformedURLException, IOException{ // 2017.08.10 william IP增加port欄位，預設443
         
         URL url;        
         CurrentIP=IPAddr;//load CurrentIP
         CurrentUserName=Uname; //load CurrentUserName
         CurrentPasseard=Password;//load CurrentPasseard
         uniqueKey=UniqueKey;
         
         CurrentIP_Port = IPPort; // 2017.08.10 william IP增加port欄位，預設443
         
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
            System.out.println("-------------------------CPL Current IP Port : "+CurrentIP_Port+"-------------------------\n"); // 2017.08.10 william IP增加port欄位，預設443
            url = new URL("https://"+CurrentIP+":"+CurrentIP_Port+"/vdi"+"/user"+"/vd/"+uniqueKey); // 2017.08.10 william IP增加port欄位，預設443          
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
 
            conn.setConnectTimeout(20000); // 2017.09.18 william 錯誤修改(1)-IP Port例外處理
            conn.setReadTimeout(10000); // 2017.09.18 william 錯誤修改(1)-IP Port例外處理             
             
        try (OutputStream out = conn.getOutputStream()) {
                 out.write(query.getBytes());
                 out.flush();
                 out.close();         
        }  catch (IOException ex) { // 2017.09.18 william 錯誤修改(1)-IP Port例外處理
             System.out.println("*********************************IP PORT輸入錯誤***********************************\n"); 
             testError=false;
        }  
        
        System.out.println("Resp Code : "+conn.getResponseCode()+"\n");
        System.out.println("Resp Message:"+ conn.getResponseMessage()+"\n");
        System.out.println("-----------------------------------------\n");
        
        /*********跳出警告訊息:401 403 404**********/
        CPLconnCode = conn.getResponseCode();
        //AlertChangeLang(connCode); 
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
    // Input： ,  Output：False/True (boolean) , 功能： 取得設定結果
    public  synchronized Boolean getResult (){
        return testError;
    }
    // Input： False/True (boolean) ,  Output： , 功能： 設定結果
    public  synchronized void setResult(Boolean res)
    {
        testError=res;
    }
    // Input： ,  Output：IP (String) , 功能： 取得IP 
    public static synchronized String getIP(){
        
        return IPAddress;
    }
    // Input： IP (String),  Output： , 功能： 設定IP
    public static synchronized void setIP(String ip)
    {
      
        IPAddress=ip;
    } 
    
    // 2017.08.10 william IP增加port欄位，預設443
//    public static synchronized String getPort(){
//        
//        return IPPort;
//    }    
    // Input： port (String),  Output： , 功能： 設定port
        public static synchronized void setPort(String port)
    {
      
        IPPort=Integer.parseInt(port);
    }      
     // Input： 1. IP(String), 2. port (String),  Output： , 功能： Ping IP
    public void Ping(String ipaddr,String port) throws InterruptedException, IOException{ // 2017.08.10 william IP增加port欄位，預設443           
        setIP(ipaddr);        
        setPort(port); // 2017.08.10 william IP增加port欄位，預設443   
        doPing();
        CPLT=new CPLogin(WordMap);
        CPLT.start();        
        //System.out.println("--Ping--testError--"+testError+" \n");
        if(!testError){
            CPLT.notifyAll();      
        }   
    }
    
    public boolean doPing() throws UnknownHostException, IOException{      

        if((IPAddress==null)||("".equals(IPAddress))){
            testError=false;
            setResult(testError);
            //System.out.println("IPAddress=null : "+ testError +"\n");   
            return false;
        }
           
//        IPAddr=InetAddress.getByName(IPAddress); // 2017.08.10 william IP增加port欄位，預設443        
//        
//        if(IPAddr.isReachable(1000)){ // 2017.08.10 william IP增加port欄位，預設443        
        if(isReachableByTcp(IPAddress, IPPort, 1000)){ // 2017.08.10 william IP增加port欄位，預設443  
            // Success Ping     
            testError=true;
            setResult(testError);
            //System.out.println(" ----Success--- \n");
            //System.out.println(" ----Success--: "+testError+ "\n");
            return true;
        }
         else{
            // Fail Ping                               
            testError=false;
            setResult(testError);
            //System.out.println("--------Fail---------- \n");
            //System.out.println(" ----Fail--: "+testError+ "\n");                    
            return false;
        }
         //return true;
     }
    // 2017.08.10 william IP增加port欄位，預設443        
    public static boolean isReachableByTcp(String host, int port, int timeout) {
        try {
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(host, port);
            socket.connect(socketAddress, timeout);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }      

    
}
