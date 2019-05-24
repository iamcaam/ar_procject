/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
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
import org.apache.commons.io.FileUtils;

/**
 *
 * @author root
 */
public class CheckVersion {
    
     /***    顯示文字對應表    ***/
    public Map<String, String> WordMap=new HashMap<>();   
    
    private String Address;
    public String Model;
    public String newVersion;
    public String UriNewFirmware;
    public String update_code;
    public int check_code;
    
    public String CurrentIP_Port; // 2017.08.10 william IP增加port欄位，預設443

    
    public CheckVersion(Map<String, String> LangMap){
       
        WordMap=LangMap;
     
    }
    
    public void ListFirmwareVersion(String IPAddr,String IPPort) throws MalformedURLException, IOException, InterruptedException{ // 2017.08.10 william IP增加port欄位，預設443
        URL url;
        Address = IPAddr;
        CurrentIP_Port = IPPort; // 2017.08.10 william IP增加port欄位，預設443
        bypassSSL();
        
        try{
            //https://{ip}/vdi/version?type=1
            System.out.println("-------------------------Firmware Current IP Port : "+CurrentIP_Port+"-------------------------\n");
            url = new URL("https://"+Address+":"+CurrentIP_Port+"/vdi/"+"version?type=1"); // 2017.08.10 william IP增加port欄位，預設443
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  //建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線
            conn.setRequestMethod("GET");//利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
	    conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Connection", "Keep-Alive");//維持長連接
            conn.connect();
            /*
            if (conn.getResponseCode() != 200) {
		  throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
	     }
            */
            check_code = conn.getResponseCode() ;
            System.out.println("-------------------ListFirmwareVersion----------------------\n");
            System.out.println("URL : "+url+"\n"); 
            System.out.println("ListFirmwareVersion-Resp Code:"+check_code+"\n"); 
            System.out.println("ListFirmwareVersion-Resp Message:"+ conn.getResponseMessage()+"\n");
            
            
            InputStream is = conn.getInputStream();
            //System.out.println("InputStream is : "+is+"\n");
            StringBuilder sb;
            String line;  

            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {              
                 sb = new StringBuilder("");
                 while ((line = br.readLine()) != null) {            
                     sb.append(line);         
                 }                 
                 //System.out.println("return br: "+br.readLine()+"\n");
                 System.out.println("ListFirmwareVersion-return sb: "+sb.toString()+"\n");             
         
                 br.close();
            }
             
            String CVdata = sb.toString();             
            JsonParser jsonCVParser = new JsonParser();
            JsonObject CheckVersion_data = (JsonObject)jsonCVParser.parse(CVdata).getAsJsonObject();
            Model = CheckVersion_data.get("Model").getAsString();
            newVersion = CheckVersion_data.get("Version").getAsString();
            UriNewFirmware = CheckVersion_data.get("UriNewFirmware").getAsString();
            
            System.out.println("CheckVersion -- Model: "+Model+"\n");
            System.out.println("CheckVersion -- newVersion: "+newVersion+"\n");
            System.out.println("CheckVersion -- UriNewFirmware: "+UriNewFirmware+"\n");
            
            conn.disconnect();
        } catch (MalformedURLException e) {
            
	} catch (IOException e) {    
            
        }  
        
    }
    
    public void downloadNewUI() throws MalformedURLException, IOException, InterruptedException{
        
        try{
            bypassSSL();
            String strUrl = UriNewFirmware; // "你要下載的檔案網址"            
            URL source = new URL(strUrl); //https://192.168.95.11/ strUrl
            //System.out.println("source ::"+ source +"\n");
            String theStrDestDir = "/tmp"; // "你要下載的檔案目的資料夾"
            //System.out.println("theStrDestDir ::"+ theStrDestDir +"\n");
            File theStockDest = new File(theStrDestDir);
            //System.out.println("theStockDest ::"+ theStockDest +"\n");
            
            //使用FileUtils.forceMkdir先來建立要存放下載檔案的資料夾，使用FileUtils.forceMkdir有一個好處，不用擔心目的資料夾的父資料夾是否存在，使用此方法，會將傳入的資料夾參數依層建好。 
            FileUtils.forceMkdir(theStockDest);

            File destination = new File(theStrDestDir +"/image.bin"); //theStrDestDir +"下載儲存的檔名"
            System.out.println("destination ::"+ destination +"\n");

            //使用FileUtils.copyURLToFile這個方法，將網路上的檔案下載回來！ 
            //第一個參數為指向要下載網址的URL(文件)物件，第二個為下載檔案路徑的File(文件)物件。 
            //FileUtils.copyURLToFile的文件說明，在存放下載檔案的資料夾不存在時，其實此方法就會建立目的資料夾，所有FileUtils.forceMkdir(theStockDest);先行建立目的資料夾的步驟就可以省略。 
            FileUtils.copyURLToFile(source, destination);
            //File file = new File(".");
            System.out.println("File Downloaded!   \n");
            
        } catch (MalformedURLException e){
        } catch (IOException e){
        }
        
    }
    
    public void updateNewUI() throws IOException{
        String[] update_str = new String[2];        
        update_str[0] = "sh";
        update_str[1] = "/root/update.sh";
        Process update_process = Runtime.getRuntime().exec(update_str);                     
        try (BufferedReader update_output = new BufferedReader (new InputStreamReader(update_process.getInputStream()))) {
            
            String update_line = update_output.readLine();
            update_code = update_line;
            System.out.println(" update_code  : " + update_code +"\n");

        }
        
        
    }
    
    
    
     /******bypass SSL 網頁認證問題,系統SSL之Certification key非信任組織發行*************/
    private void bypassSSL() throws SocketException, UnknownHostException, IOException{
       
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
    
    
}
