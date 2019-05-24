/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import static acrored_vdi_viewer.AcroRed_VDI_Viewer.getProtocol;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.net.InetAddress; 
import java.io.OutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.io.InputStreamReader;
import org.json.simple.JSONObject;
import com.google.gson.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.io.BufferedWriter;
import java.io.UnsupportedEncodingException;
import static java.lang.Thread.sleep;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 *
 * @author duck_chang,sabrina_yeh
 */
public class QueryMachine extends Thread {
    public static String Job;
    public static String Status;//狀態
    public static String Messages;
    private String Username;
    private String Password;
    public static String IPAddress;
    private InetAddress IPAddr;
    private final ReentrantLock lockStatus=new ReentrantLock();
    private final ReentrantLock lockMessages=new ReentrantLock();
    private final ReentrantLock lockResult=new ReentrantLock(); 
    public String uniqueKey;
    public String CurrentIP;
    public String CurrentUserName;
    public String CurrentPasseard;
    public String VdId;    
    public String IsVdOnline;
    public String IsDefault;
    public String VdServers;
    public String Address;
    public String Port;
    public String SpiceAddress;
    public String SpicePort;
    public Process process;
    public String  ppid;
    public Process pclose;
    public String[] params;
    public boolean result;
    public boolean testError;
    private Thread QMT;
    public int connCode;
    public int ChcekServerAddress_connCode;
    public int SetVDonline_connCode;
    public boolean VdOnline_connect=false;
    public int Alert_spiceaddr;  
    //Thread 宣告
    private PingSetVDOnline PSV;
    
    public String CurrentIP_Port;   // 2017.08.10 william IP增加port欄位，預設443
    public static int IPPort;       // 2017.08.10 william IP增加port欄位，預設443
    
    private LoginMultiVD LMVD;      // 2017.11.24 william 單一帳號多個VD登入
    private Stage public_stage;     // 2017.11.24 william 單一帳號多個VD登入
    public boolean checkaddr;       // 2017.11.24 william 單一帳號多個VD登入
    public int s;                   // 2017.11.24 william 單一帳號多個VD登入
    public boolean checkLogin_flag; // 2017.11.24 william 單一帳號多個VD登入
    
    private Snapshot ss;            // 2017.12.08 william SnapShot實作
    private boolean Snapshot_flag;  // 2017.12.08 william SnapShot實作
    public File MultiVD_file;       // 2018.01.19 william
    // 2018.02.06 william 雙螢幕實作
    private Process Get_Display_process; 
    private String Display_primary_name;
    private String Display_second_name;      
    boolean IsPrimary = false;
    boolean IsSecond  = false;
    URL Monitor_url;   
    HttpURLConnection Monitor_conn;
    public static String VDid;
    public GB GB;
    private Timer Duplicate_timer;
    private TimerTask Duplicate_task; 
    String Duplicate_command = "ps -aux | grep /root/RemoteViewer/virt/bin/remote-viewer";
    Process Duplicate_process;
    public String line_Duplicate;
    
    static int select_size = 0; // 2018.11.11 新增AcroDesk & RDP Protocol
    
    // 2019.01.02 view D Drive
    String _diskName = "";    
         
    boolean OneVD_SPICE = false;
    
    /*****Pattern:檢查IP是否符合正確格式*****/
    private static final Pattern PATTERN = Pattern.compile(
        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    
    /***    顯示文字對應表    ***/
    public Map<String, String> WordMap=new HashMap<>();   
    
    @Override
    public void run(){
        Status="Proceeding";
        String doJob=getJob();        
        switch (doJob){
            case "login":{
                try {
                    QueryLogin(public_stage, CurrentIP,CurrentUserName,CurrentPasseard,uniqueKey,CurrentIP_Port,Snapshot_flag, false);  // 2019.01.02 view D Drive
                    
                } catch (IOException | InterruptedException ex) {
                    Logger.getLogger(QueryMachine.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                break;
            case "Ping":{
                //boolean result;
                try {                 
                    doPing();
                    //System.out.println(" ----testError-1--: "+testError+ "\n");
                      
                } catch (IOException ex) {
                    Logger.getLogger(QueryMachine.class.getName()).log(Level.SEVERE, null, ex);
                }         
            }
                break;
            default:
                break;
        }
        Status="Done";
    }
    
    
    public QueryMachine(Map<String, String> LangMap){
        Status="Waiting";
        WordMap=LangMap;
        try {
            // uniqueKey=Ukey;
            //UUID 需要非同步(需固定 不能改)
            bypassSSL();
        } catch (UnknownHostException ex) {
            Logger.getLogger(QueryMachine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(QueryMachine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    
    public static synchronized String getJob(){
        return Job;
    }
    
    public static synchronized void setJob(String job)
    {
        Job=job;
    }
    
    public static synchronized String getIP(){
        
        return IPAddress;
    }
    
    public static synchronized void setIP(String ip)
    {
      
        IPAddress=ip;
    }
    
    // 2017.08.10 william IP增加port欄位，預設443
//    public static synchronized String getPort(){
//        
//        return IPPort;
//    }    
    // 2017.08.10 william IP增加port欄位，預設443
        public static synchronized void setPort(String port)
    {
      
        IPPort=Integer.parseInt(port);
    }    
    
    public static synchronized String getStatus(){
        return Status;
    }
    
    public static synchronized void setStatus(String status)
    {
        Status=status;
    }
    
    public static synchronized String getMessages(){
        return Messages;
    }
    
    public static synchronized void setMessages(String messages)
    {
        Messages=messages;
    }
    
    public  synchronized Boolean getResult (){
        return testError;
    }
    
    public  synchronized void setResult(Boolean res)
    {
        testError=res;
    }
     
    public void Reset(){
        lockStatus.lock();
        try {
            setStatus("Waiting");           
        } finally {
            lockStatus.unlock();
        }
        lockMessages.lock();
        try{
            setMessages("");
        }finally{
            lockMessages.unlock();
        }
        //lockResult
        lockResult.lock();
        try{
            setResult(testError);
        }finally{
            lockResult.unlock(); 
        }
    }
    
    public String getQMStatus(){
        String tempStatus;
        lockStatus.lock();
        try {
            tempStatus=Status;
        } finally {
            lockStatus.unlock();
        }
        return tempStatus;
    }
    
    public String getQMMessages(){
        String tempMessages;
        lockMessages.lock();
        try{
            tempMessages=Messages;
        }finally{
            lockMessages.unlock();
        }
        return tempMessages;
    }
    
    public boolean getQMResult(){
        Boolean tempResult;
        lockResult.lock();
        try{
            tempResult=testError;
        }finally{
            lockResult.unlock();
        }
        return tempResult;
    }
    
    
    /**
     * Login Process
     * @throws MalformedURLException
     * @throws IOException
     */
     /**POST
     * @param IPAddr
     * @param Password
     * @param Uname*
     * @param UniqueKey
     * @throws java.net.MalformedURLException
     **/    
    public void QueryLogin(Stage primaryStage, String IPAddr,String Uname,String Password,String UniqueKey,String IPPort,Boolean IsSnapShot, boolean IsViewDrive) throws MalformedURLException, IOException, InterruptedException{ // 2019.01.02 view D Drive
        public_stage    = primaryStage; // 2017.11.24 william 單一帳號多個VD登入
        URL url;        
        CurrentIP       = IPAddr;       // load CurrentIP
        CurrentUserName = Uname;        // load CurrentUserName
        CurrentPasseard = Password;     // load CurrentPasseard
        uniqueKey       = UniqueKey;
        CurrentIP_Port  = IPPort;       // 2017.08.10 william IP增加port欄位，預設443
        Snapshot_flag   = IsSnapShot;   // 2017.12.08 william SnapShot實作
         
        JSONObject obj =new JSONObject();
        obj.put("AccountName", CurrentUserName);
        obj.put("Password", CurrentPasseard);       
        String query=obj.toString();
        //System.out.println("query : "+query+"\n"); 
        // bypassSSL();
         /***POST Start***/
        try {
            GB.ExitFlag = false; // 2018.11.11 新增AcroDesk & RDP Protocol
            System.out.println("-------------------------QM Current IP Port : "+CurrentIP_Port+"-------------------------\n"); 
            url = new URL("https://"+CurrentIP+":"+CurrentIP_Port+"/vdi"+"/user"+"/vd/"+uniqueKey);  // 2017.08.10 william IP增加port欄位，預設443                    
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
            
            conn.setConnectTimeout(20000); // 2017.09.18 william 錯誤修改(1)-IP Port例外處理 / 2017.11.24 單一帳號多個VD登入 (5秒改20秒，不然在getResponseCode容易失敗) // 5 -> 10 -> 20
            conn.setReadTimeout(10000);    // 2017.09.18 william 錯誤修改(1)-IP Port例外處理 / 2017.11.24 單一帳號多個VD登入 (2秒改10秒，不然在getResponseCode容易失敗) // 2 -> 10         
                                   
        //寫入資料     
        try (OutputStream out = conn.getOutputStream()) {
            out.write(query.getBytes());
            out.flush();
            out.close();
            //System.out.println("output out : "+out+"\n");
        } catch (IOException ex) { // 2017.09.18 william 錯誤修改(1)-IP Port例外處理
             System.out.println("*********************************IP PORT輸入錯誤***********************************\n"); 
             testError=false;
        } 
        
        System.out.println("URL : "+url+"\n"); 
        System.out.println("conn.getOutputStream() : "+conn.getOutputStream()+"\n");
        System.out.println("Resp Code : "+conn.getResponseCode()+"\n");
        System.out.println("Resp Message:"+ conn.getResponseMessage()+"\n");
        System.out.println("-----------------------------------------\n");
       
       /*********跳出警告訊息:401 404**********/
        connCode = conn.getResponseCode();
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

         
                br.close();
            }           
            
        String data = sb.toString();             
        JsonParser jsonParser = new JsonParser();
        JsonObject returndata = (JsonObject)jsonParser.parse(data).getAsJsonObject();

         
        JsonArray jsonArr = returndata.getAsJsonObject().getAsJsonArray("UserVdImages");
        
        // 2019.01.02 view D Drive
        _diskName = "";
        try{
            _diskName = returndata.get("DiskName").getAsString();
        } catch(Exception e) {}
        
        
        System.out.println("JsonArray jsonArr: "+jsonArr+"\n");
        if(jsonArr.size()==0) { // 2018.18.18 new create & reborn
            System.out.println("no user");
            Platform.runLater(() -> { 
                ReLogin(-10);                     
            });         
        }
            
        checkaddr = false;                           // 2017.11.24 william 單一帳號多個VD登入
       //gson-JsonArray         
        int i = 0;                               // 2017.11.24 william 單一帳號多個VD登入            
        s     = jsonArr.size();                  // 2017.11.24 william 單一帳號多個VD登入
        setSize(s); // 2018.11.11 新增AcroDesk & RDP Protocol
        JsonObject term = jsonArr.get(i).getAsJsonObject();

        VdId = term.get("VdId").getAsString();
        setVDID(VdId); // 2018.02.06 william 雙螢幕實作
        VdServers = term.get("VdServers").toString();
        Address = term.getAsJsonArray("VdServers").get(i).getAsJsonObject().get("Address").getAsString();
        Port = term.getAsJsonArray("VdServers").get(i).getAsJsonObject().get("Port").getAsString();
        IsVdOnline = term.get("IsVdOnline").toString();
        IsDefault = term.get("IsDefault").toString();
        
            boolean adAuth = false;
            String adDomain = "";

            try {
                GB.adAuth = adAuth = returndata.get("ADAuth").getAsBoolean();//term.get("ADAuth").getAsBoolean();
                GB.adDomain = adDomain = returndata.get("ADDomain").getAsString();//term.get("ADDomain").getAsString();
            } catch (Exception e) {
                adAuth = false;
                adDomain = "";
            }         

        System.out.println("VdId: " + VdId);
        System.out.println("VdServers: " + VdServers);            
        System.out.println("Address: " + Address);
        System.out.println("Port: "+Port+"\n");             
        System.out.println("IsVdOnline: " + IsVdOnline); 
        System.out.println("IsDefault: " + IsDefault);     
        

        if(!Snapshot_flag) {
            if (s > 0) {  // 2018.11.11 新增AcroDesk & RDP Protocol  1 -> 0      
                    OneVD_SPICE = false;
                    //System.out.println("Array size : " + getSize() + ", Available Protocol : " + term.get("AvailableProtocol").getAsInt());
                    try {
                        if(getSize() == 1 && term.get("AvailableProtocol").getAsInt() == 1) {
                            OneVD_SPICE = true;
                            GB.AvailableProtocol = 1;
                            GB.migrationApiIP = Address;
                            GB.migrationVDID = term.get("VdId").getAsString();
                            GB.migrationUserName = term.get("VdName").getAsString();
                            GB.migrationPwd = CurrentPasseard; 
                            GB.migrationUkey = uniqueKey; 
                            GB.migrationApiPort = Port;
                            SetVDOnline(Address, term.get("VdId").getAsString(), term.get("VdName").getAsString(), CurrentPasseard, uniqueKey, Port);
                        } else if (getSize() == 1 && term.get("AvailableProtocol").getAsInt() == 2) {
                                                                                    
                            GB.AvailableProtocol = 2;
                            GB.RDPFirst = term.get("RDPFirst").getAsInt();
                            GB.VDStatus = term.get("VdStatus").getAsInt();

                            try {
                                JsonArray jsonArrForRDP = term.getAsJsonArray("RDP");
                                GB._rdpArraySize = jsonArrForRDP.size();
                                for(int j = 0; j < jsonArrForRDP.size(); j++) {
                                    if(GB.IsPingOK = PingIP.isReachableByTcp(jsonArrForRDP.get(j).getAsJsonObject().get("IP").getAsString(), Integer.parseInt(jsonArrForRDP.get(j).getAsJsonObject().get("Port").getAsString()), 1000))
                                        break;
                                }
                            } catch (Exception ex) {
                                GB.IsPingOK = false;   
                                GB._rdpArraySize = 0;
                            }                                                  

                            if(term.get("VdStatus").getAsInt() == 5) {
                                GB.RDPStatus = 0; // Shutdown       
                            } else if(term.get("VdStatus").getAsInt() == 1 && GB._rdpArraySize == 0) {
                                GB.RDPStatus = 1; // Preparing       
                            } else if(term.get("VdStatus").getAsInt() == 1 && GB._rdpArraySize > 0 && GB.IsPingOK) {
                                GB.RDPStatus = 2; // Ready                                          
                            } else if(term.get("VdStatus").getAsInt() == 1 && GB._rdpArraySize > 0 && !GB.IsPingOK) {
                                GB.RDPStatus = 3; // Cannot Connect       
                            } else if(term.get("VdStatus").getAsInt() == 10) {
                                GB.RDPStatus = 1; // Preparing
                            }                         
                            
                            GB.migrationApiIP = Address; 
                            GB.migrationVDID = VdId; 
                            GB.migrationUserName = CurrentUserName;
                            GB.migrationPwd = CurrentPasseard;
                            GB.migrationUkey = uniqueKey; 
                            GB.migrationApiPort = Port; 
                            GB.migrationIsVdOrg = term.get("IsVdOrg").getAsBoolean(); 
                            GB.EnableUSB = term.get("USBRedirCt").getAsInt() == 0 ? false : true; 
                            GB.adAuth = adAuth; 
                            GB.adDomain = adDomain;

                            if(GB.RDPStatus == 3)
                                Platform.runLater(() -> { ReLogin(2); });    
                            else                        
                                SetVDRDPOnline(Address, VdId, CurrentUserName, CurrentPasseard, uniqueKey, Port, term.get("IsVdOrg").getAsBoolean(),  term.get("USBRedirCt").getAsInt() == 0 ? false : true, adAuth, adDomain);
                        } else {
                            Platform.runLater(() -> { 
                                GB.RejumpProtocol_AcroView = "";
                                GB.RejumpProtocol_RDP = "";
                                GB.AvailableProtocol = 3;
                                LMVD = new LoginMultiVD(public_stage, WordMap,jsonArr, Address, CurrentUserName, CurrentPasseard, uniqueKey, CurrentIP_Port); // 2017.11.24 william 單一帳號多個VD登入                    
                            });
                        }                      
                    } catch (Exception e) {
                        if(getSize() == 1) {
                            OneVD_SPICE = true;
                            GB.AvailableProtocol = 1;
                            SetVDOnline(Address, term.get("VdId").getAsString(), term.get("VdName").getAsString(), CurrentPasseard, uniqueKey, Port);
                        } else {
                            GB.AvailableProtocol = 3;
                            LMVD = new LoginMultiVD(public_stage, WordMap,jsonArr, Address, CurrentUserName, CurrentPasseard, uniqueKey, CurrentIP_Port); // 2017.11.24 william 單一帳號多個VD登入                    
                        }                       
                    }                                                                                                                                                             
            } else {
                /*** 檢查Server的可及性,HttpCode回覆200 表示即可連線 ***/ 
                DoChcekServerAddressAvailabilityGet(Address,Port);
                
                switch (getProtocol()) {
                    case "AcroDesk":
                        if(ChcekServerAddress_connCode == 200 && checkaddr == false) {  
                            checkaddr = true;
                            if("false".equals(IsVdOnline)) { SetVDOnline(Address,  VdId, CurrentUserName, CurrentPasseard, uniqueKey, CurrentIP_Port); }                
                        } 
                        break;
                    case "RDP":
                        if(ChcekServerAddress_connCode == 200 && checkaddr == false) {  
                            checkaddr = true;
                            // if("false".equals(IsVdOnline)) { SetVDRDPOnline(Address,  VdId, CurrentUserName, CurrentPasseard, uniqueKey, CurrentIP_Port); } // 2018.12.12 新增 431NoVGA 和第一次開機                
                        }                         
                        break;       
                    default: 
                        break;               
                }                
                
                
           
            }

        } else {
            if(IsViewDrive) { // 2019.01.02 view D Drive
                Platform.runLater(() -> { 
                    new ViewDrive(public_stage, WordMap, jsonArr, Address, CurrentIP_Port, CurrentUserName, CurrentPasseard, uniqueKey, _diskName); 
                });                                 
            } else {
                Platform.runLater(() -> { 
                    ss = new Snapshot(public_stage, WordMap, jsonArr, Address, CurrentIP_Port, CurrentUserName, CurrentPasseard, uniqueKey, _diskName); // 2017.11.24 william SnapShot實作                   
                });              
            }
        }
         
            conn.disconnect();     
      
        } catch (MalformedURLException e) {}                   
    }
    
    /*******GET
     * @param address*
     * @param port
     ******/
    
    public void DoChcekServerAddressAvailabilityGet(String address, String port) throws MalformedURLException, IOException{
        URL url;
        Address=address;
        Port=port;
        try{
           
            url = new URL("https://"+Address+":"+Port+"/vdi/"+"port"); // 2017.08.10 william IP增加port欄位，預設443 
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  //建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線
            conn.setRequestMethod("GET");//利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
	    conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Connection", "Keep-Alive");//維持長連接
            conn.setConnectTimeout(10000); // 2017.11.24 單一帳號多個VD登入 (5秒改10秒，不然在getResponseCode容易失敗)
            conn.connect();
            /*
            if (conn.getResponseCode() != 200) {
		  throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
	     }
            */
            ChcekServerAddress_connCode = conn.getResponseCode();
            System.out.println("-------------------ChcekServerAddressAvailabilityGet----------------------\n");
            System.out.println("URL : "+url+"\n"); 
            System.out.println("ChcekServerAddressAvailabilityGet-Resp Code:"+ChcekServerAddress_connCode+"\n"); 
            System.out.println("ChcekServerAddressAvailabilityGet-Resp Message:"+ conn.getResponseMessage()+"\n");
            
            //若ResponseCode!=200,則連至下一組Address及port 重複chack到ResponseCode=200 
            if(conn.getResponseCode() != 200){
                //補程式碼
                //System.out.println("DoChcekServerAddressAvailabilityGet : "+ conn.getResponseCode() +"\n");
            } 
            /*
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
                 System.out.println("ChcekServerAddressAvailabilityGet-return sb: "+sb.toString()+"\n");             
         
                 br.close();
             }
            */
      		conn.disconnect();
        } catch (MalformedURLException e) {
            
	} catch (IOException e) {    
            
        }  
    }
    
    public void SetVDOnline(String address, String vdid, String Uname, String Password, String UniqueKey, String port)throws MalformedURLException, IOException, InterruptedException{
        URL url;
        Address=address;
        VdId=vdid;
        CurrentUserName=Uname; //load CurrentUserName
        CurrentPasseard=Password;//load CurrentPasseard
        uniqueKey=UniqueKey;
        CurrentIP_Port = port; // 2017.08.10 william IP增加port欄位，預設443
        checkLogin_flag = false; // 2017.11.24 william 單一帳號多個VD登入
        setVDID(vdid); 
        Dual_Screen(address, port); 
        System.out.println("-------------------SetVDOnline----------------------\n");
        JSONObject obj2 =new JSONObject();
        obj2.put("ClientID", uniqueKey);//UUID需有雙引號->將uniqueKey轉為字串toString    
        String ClientID=obj2.toString();
        //System.out.println("SetVDOnline-cliendID : "+cliendID+"\n"); 
        boolean ReLogin = false;
     
        try{
            url = new URL("https://" + Address + ":" + CurrentIP_Port + "/vdi/user/vd/" + VdId); //"https://"+CurrentIP+"/vdi/"+"/user/"+"/vd/"+uniqueKey
            HttpURLConnection conn_setVDonline = (HttpURLConnection) url.openConnection();  //建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線
            conn_setVDonline.setRequestMethod("PUT");//利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
            conn_setVDonline.setDoOutput(true);
            conn_setVDonline.setDoInput(true);
            conn_setVDonline.setRequestProperty("Accept", "application/json");
            conn_setVDonline.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn_setVDonline.setRequestProperty("Content-Type", "application/json");//設置發送數據的格式("Content-Type", "application/json; charset=UTF-8")
            conn_setVDonline.setRequestProperty("Content-length", String.valueOf(ClientID.getBytes().length));//("Content-length", String.valueOf(query.length()))
            conn_setVDonline.setRequestProperty("Connection", "Keep-Alive");//維持長連接
            //conn.connect();    
            conn_setVDonline.setUseCaches (false);
            conn_setVDonline.setInstanceFollowRedirects(true);
            
            conn_setVDonline.setConnectTimeout(10000); // 2017.11.24 單一帳號多個VD登入 (5秒改10秒，不然在getResponseCode容易失敗)

            try (OutputStream out2 = conn_setVDonline.getOutputStream()) {
                out2.write(ClientID.getBytes());
                out2.flush();
                out2.close();
                //System.out.println("SetVDOnline-output out : "+out2+"\n");
                      
                //System.out.println("SetVDOnline-URL : "+url+"\n"); 
                //System.out.println("SetVDOnline-conn.getOutputStream() : "+conn_setVDonline.getOutputStream()+"\n");
                System.out.println("SetVDOnline-Resp Code : "+conn_setVDonline.getResponseCode()+"\n");
                System.out.println("SetVDOnline-Resp Message:"+ conn_setVDonline.getResponseMessage()+"\n");
                SetVDonline_connCode = conn_setVDonline.getResponseCode();
               /*********跳出警告訊息:503 406 412 500 403 417 410**********/
                //SetVDOnline_AlertChangeLang(SetVDonline_connCode);
               /***********************************************************/
               
               if(SetVDonline_connCode == 200 || SetVDonline_connCode == 0 || SetVDonline_connCode == 428) { // 2018.12.11 bug fix
                    InputStream is2 = conn_setVDonline.getInputStream();           
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
                    JsonParser jsonSParser = new JsonParser();
                    JsonObject Spicedata = (JsonObject)jsonSParser.parse(Sdata).getAsJsonObject();
                    JsonArray SpiceArr = Spicedata.getAsJsonObject().getAsJsonArray("SpiceServers");
    //                System.out.println("JsonArray SpiceArr: "+SpiceArr+"\n");
                    
                    int count = 0;
                    //  2018.11.06 william 多執行緒Ping IP
                    new PingIP(SpiceArr);                
                    long startTime = System.currentTimeMillis(); // fetch starting time
                    while(false || (System.currentTimeMillis()-startTime) < 30000) {
                        if(GB.stopconnect) {
                            GB.ExitFlag = true;
                            break;                        
                        }                        
                        // System.out.println("IpAddress : " + GB.ConnectionAddress + " Port : " + GB.ConnectionPort);
                        if(!"".equals(GB.ConnectionAddress)  && !"".equals(GB.ConnectionPort)) {
                            ReLogin = true;
                            RunSpice(GB.ConnectionAddress, GB.ConnectionPort, getSize());
                            break;
                        }
                        if(GB.ExitFlag) { // 2018.11.11 新增AcroDesk & RDP Protocol
                             System.out.println("Spice direct exit");
                            break;
                        } 
                        
                        // 2019.01.30 UserDisk快照顯示異常問題修正
                        if(count > 10) {
                            GB.ExitFlag = false;
                            break;
                        }
                        count++;

                        TimeUnit.SECONDS.sleep(2);
                        
                        if("".equals(GB.ConnectionAddress)  && "".equals(GB.ConnectionPort)) {
                            new PingIP(SpiceArr);
                        }                           
                    }                
                    System.out.println("Finish Run Spice"); 

                    // 要增加重新登入連線
                    if(!ReLogin && !GB.ExitFlag) {// 2018.12.11 bug fix
                        checkLogin_flag = true; 
                        sleep(500);                    
                        Platform.runLater(() -> { 
                             ReLogin(3);                     
                        });                
                    }                              
               }
                  
                
                /***** 以ping成功的Address 登入 remoteViewer *****/
//                PSV=new PingSetVDOnline(WordMap); //建立呼叫HTTP GET　POST REST JSON 的 Class
//                boolean spiceaddr = false;  
//                Alert_spiceaddr = 0;
//                if(Alert_spiceaddr == 0){  
//                    if(s==1) {                    // 2017.11.24 william 單一帳號多個VD登入
//                        for (int i = 0; i < SpiceArr.size(); i++) {
//                            JsonObject Sterm = SpiceArr.get(i).getAsJsonObject();
//                            SpiceAddress=Sterm.get("Address").getAsString();
//                            System.out.println("SpiceAddress: "+SpiceAddress+"\n");
//                            SpicePort=Sterm.get("Port").toString();
//                            System.out.println("SpicePort: "+SpicePort+"\n");                    
//                            PSV.PingSetVD(SpiceAddress,SpicePort);  // 2017.08.10 william IP增加port欄位，預設443                   
//                            if(PSV.testError == true && spiceaddr == false){
//                                
//                                /** 2018.03.13 william 雙螢幕實作 - 同步處理**/
//                                if(GB.DuplicateSize == 2 && !GB.OneDisplayDisable) {
//                                    // sleep(3000);
//                                    Duplicate_check_listener();
//                                    Runtime.getRuntime().exec(GB.monitoroff_exec_string);
//                                    // Runtime.getRuntime().exec(GB.monitor_exec_string);
//                                }                                 
//                                
//                                spiceaddr=true;
//                                WriteSpiceText(SpiceAddress, SpicePort,CurrentUserName,CurrentPasseard);
//                                File f = new File("jsonfile/SpiceText.txt");       
//                                if(f.exists()){
//                                    // 2018.10.11 william G300s Mouse問題修改
//                                    CheckRmProcess(public_stage);
//                                    /*
//                                    //String[] params = new String [2]; 
//                                    params = new String [2];           
//                                    params[0] = "/root/RemoteViewer/virt/bin/remote-viewer"; // 0515_AcroRed_Viewer //"/root/Linux_0220_AcroRed_VDI_Viewer/virt/bin/remote-viewer"  //"/root/0427_AcroRed_Viewer/virt/bin/remote-viewer"
//                                    params[1] = f.toString();                       
//                                    process =Runtime.getRuntime().exec(params);           
//                                    process=null;
//                                    // System.gc();//清除系統垃圾 // 2017.06.05 william System.gc disable
//                                    */
//                                } else {
//                                 System.out.print("remote-viewer.exe is not existed.\n");
//                                }  
//                            }
//                        }
//                    } else {
//                        for (int i = 0; i < SpiceArr.size(); i++) {                        
//                            
//                        
//                            JsonObject Sterm = SpiceArr.get(i).getAsJsonObject();
//                            SpiceAddress=Sterm.get("Address").getAsString();
//                            System.out.println("SpiceAddress: "+SpiceAddress+"\n");
//                            SpicePort=Sterm.get("Port").toString();
//                            System.out.println("SpicePort: "+SpicePort+"\n");     
//                            
//                            if(isReachableByTcp(SpiceAddress, Integer.parseInt(SpicePort), 1000)) {                         
//                                PSV.PingSetVD(SpiceAddress,SpicePort);  // 2017.08.10 william IP增加port欄位，預設443                   
//                                if(PSV.testError == true && spiceaddr == false) {                            
//                                    spiceaddr=true;
//                                    WriteSpiceText(SpiceAddress, SpicePort,CurrentUserName,CurrentPasseard);
//                                    MultiVD_file = new File("jsonfile/SpiceText.txt");       
//                                    if(MultiVD_file.exists()){         
//                                        checkLogin_flag = true; // 2017.11.24 william 單一帳號多個VD登入
//                                        // sleep(10000);
//                                        //String[] params = new String [2]; 
//                                        /*
//                                        params = new String [2];           
//                                        params[0] = "/root/RemoteViewer/virt/bin/remote-viewer"; // 0515_AcroRed_Viewer //"/root/Linux_0220_AcroRed_VDI_Viewer/virt/bin/remote-viewer"  //"/root/0427_AcroRed_Viewer/virt/bin/remote-viewer"
//                                        params[1] = MultiVD_file.toString();                       
//                                        process   = Runtime.getRuntime().exec(params);           
//                                        process   = null;
//                                        */
//                                        // System.gc();//清除系統垃圾 // 2017.06.05 william System.gc disable
//                                
//
//                                    } else {
//                                        System.out.print("remote-viewer.exe is not existed.\n");
//                                    } 
////                            checkLogin_flag = true; // 2017.11.24 william 單一帳號多個VD登入
//                                }                        
//                            }
//                        }                        
//                    }
//                    if(Alert_spiceaddr == 0 && spiceaddr == false){    
//                        Alert_spiceaddr=1;
//                        checkLogin_flag = true; // 2017.11.24 william 單一帳號多個VD登入
//                    }
//                }
                
                conn_setVDonline.disconnect();                                                    
            }            

           } catch (MalformedURLException e) {} catch (IOException e) {}        
    }
    
    //  2018.11.06 william 多執行緒Ping IP
    public void RunSpice(String address, String port, int s) throws IOException, InterruptedException {
        System.out.println("RunSpice IpAddress : " + address + " Port : " + port);
        PSV = new PingSetVDOnline(WordMap); //建立呼叫HTTP GET　POST REST JSON 的 Class
        boolean spiceaddr = false;  
        Alert_spiceaddr = 0;
        if(Alert_spiceaddr == 0) {  
            if(s == 0) { // 2017.11.24 william 單一帳號多個VD登入 // 2018.11.11 新增AcroDesk & RDP Protocol  1 -> 0                      
                SpiceAddress = address;
                SpicePort = port;
                // 以下三行會判斷各IP的測試結果
                System.out.println("SpiceAddress: " + SpiceAddress + "\n");
                System.out.println("SpicePort: " + SpicePort + "\n");                    
                PSV.PingSetVD(SpiceAddress, SpicePort);
                if(PSV.testError == true && spiceaddr == false) {                                
                    /** 2018.03.13 william 雙螢幕實作 - 同步處理**/
                    if(GB.DuplicateSize == 2 && !GB.OneDisplayDisable) {
                        // sleep(3000);
                        Duplicate_check_listener();
                        Runtime.getRuntime().exec(GB.monitoroff_exec_string);
                        // Runtime.getRuntime().exec(GB.monitor_exec_string);
                    }                                 
                                
                    spiceaddr = true;                    
                    WriteSpiceText(SpiceAddress, SpicePort, CurrentUserName, CurrentPasseard);
                    File f = new File("jsonfile/SpiceText.txt");       
                    if(f.exists()) {
                        // 2018.10.11 william G300s Mouse問題修改
                        CheckRmProcess(public_stage);
                        
                        // 測試用，出貨要註解      
                        /*
                        //String[] params = new String [2]; 
                        params = new String [2];           
                        params[0] = "/root/RemoteViewer/virt/bin/remote-viewer"; // 0515_AcroRed_Viewer //"/root/Linux_0220_AcroRed_VDI_Viewer/virt/bin/remote-viewer"  //"/root/0427_AcroRed_Viewer/virt/bin/remote-viewer"
                        params[1] = f.toString();                       
                        process =Runtime.getRuntime().exec(params);           
                        process=null;
                        // System.gc();//清除系統垃圾 // 2017.06.05 william System.gc disable
                        */                                                                                    
                    } else {
                        System.out.print("remote-viewer.exe is not existed.\n");
                    }  
                    checkLogin_flag = true; 
                }                        
            } else {
                SpiceAddress = address;
                SpicePort = port;    
                System.out.println("SpiceAddress: " + SpiceAddress + "\n");
                System.out.println("SpicePort: " + SpicePort + "\n");                                 
                if(isReachableByTcp(SpiceAddress, Integer.parseInt(SpicePort), 1000)) {                         
                    PSV.PingSetVD(SpiceAddress, SpicePort); 
                    if(PSV.testError == true && spiceaddr == false) {                            
                        spiceaddr = true;
                        GB.RejumpProtocol_AcroView = CurrentUserName + "-" + SpiceAddress + " (1) - Remote Viewer";
                        checkLogin_flag = true;
                        sleep(500);
                        WriteSpiceText(SpiceAddress, SpicePort, CurrentUserName, CurrentPasseard);
                        MultiVD_file = new File("jsonfile/SpiceText.txt");   
                        
                        if(MultiVD_file.exists()) {     
                            // 2018.11.11 新增AcroDesk & RDP Protocol
                            /*
                            new Thread(new Runnable() {                
                                @Override
                                public void run() {
                                    long startTime = System.currentTimeMillis();
                                    int detectCount = 0;
                                    String line_Check = null;
                                    while(false || (System.currentTimeMillis()-startTime) < 5000) {

                                        try {
                                            String command = "ps -aux | grep '[r]emote-viewer'";  
                                            Process Check_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",command});
                                            BufferedReader output_Check = new BufferedReader (new InputStreamReader(Check_process.getInputStream()));
                                            line_Check = output_Check.readLine();
                                        } catch(IOException e) {}

                                        if(line_Check != null) {
                                            detectCount++; 
                                            if(detectCount == 3) {
                                                checkLogin_flag = true; // 2017.11.24 william 單一帳號多個VD登入
                                                break;
                                            }                                            
                                        }                                        
                                                                                
                                        try {
                                            TimeUnit.SECONDS.sleep(1);
                                        } catch (InterruptedException ex) {
                                            Logger.getLogger(QueryMachine.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }                
                                    System.out.println("Detect Spice"); 
                                }
                            }).start();   
                            */
                            // sleep(10000);
                            //String[] params = new String [2];                                       
                            /*
                            params = new String [2];           
                            params[0] = "/root/RemoteViewer/virt/bin/remote-viewer"; // 0515_AcroRed_Viewer //"/root/Linux_0220_AcroRed_VDI_Viewer/virt/bin/remote-viewer"  //"/root/0427_AcroRed_Viewer/virt/bin/remote-viewer"
                            params[1] = MultiVD_file.toString();                       
                            process   = Runtime.getRuntime().exec(params);           
                            process   = null;
                            */
                            // System.gc();//清除系統垃圾 // 2017.06.05 william System.gc disable
                            
                        } else {
                            System.out.print("remote-viewer.exe is not existed.\n");
                        } 
                    }                        
                }                                            
            }
            if(Alert_spiceaddr == 0 && spiceaddr == false) {    
                Alert_spiceaddr = 1;
                checkLogin_flag = true; // 2017.11.24 william 單一帳號多個VD登入
            }
        }   
    }    
    
    // 2018.11.11 新增AcroDesk & RDP Protocol 
    // 增加Set VD RDP Online       
    public void SetVDRDPOnline(String address, String vdid, String Uname, String Password, String UniqueKey, String port, boolean IsVdOrg, boolean enableusb, boolean adAuth, String adDomain)throws MalformedURLException, IOException, InterruptedException{ // 2018.12.12 新增 431NoVGA 和第一次開機
        URL url;
        CurrentUserName = Uname; 
        CurrentPasseard = Password;
        checkLogin_flag = false; // 2017.11.24 william 單一帳號多個VD登入
        JSONObject _jsonObject = new JSONObject();
        _jsonObject.put("ClientID", UniqueKey);
        String ClientID = _jsonObject.toString();
        
        boolean StopGetRDP = false; 
        boolean ReLogin = false; 
        GB.ConnectionAddress = "";       
        int rdp_stage = -100;// 2018.12.12 新增 431NoVGA 和第一次開機
        int time_count = -1;
        if(IsVdOrg)
            time_count = 150000;
        else
            time_count = 120000;        

        try{
            url = new URL("https://" + address + ":" + port + "/vdi/user/vd_rdp/" + vdid);
            HttpURLConnection conn_setVDonline = (HttpURLConnection) url.openConnection();                     // 建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線
            conn_setVDonline.setRequestMethod("PUT");                                                          // 利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
            conn_setVDonline.setDoOutput(true);
            conn_setVDonline.setDoInput(true);
            conn_setVDonline.setRequestProperty("Accept", "application/json");
            conn_setVDonline.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn_setVDonline.setRequestProperty("Content-Type", "application/json");                           // 設置發送數據的格式("Content-Type", "application/json; charset=UTF-8")
            conn_setVDonline.setRequestProperty("Content-length", String.valueOf(ClientID.getBytes().length)); // ("Content-length", String.valueOf(query.length()))
            conn_setVDonline.setRequestProperty("Connection", "Keep-Alive");                                   // 維持長連接
            conn_setVDonline.setUseCaches (false);
            conn_setVDonline.setInstanceFollowRedirects(true);            
            conn_setVDonline.setConnectTimeout(10000);

            try (OutputStream _outputStream = conn_setVDonline.getOutputStream()) {
                _outputStream.write(ClientID.getBytes());
                _outputStream.flush();
                _outputStream.close();

                SetVDonline_connCode = conn_setVDonline.getResponseCode();
                
                System.out.println("RDP return code: " + SetVDonline_connCode); 
                
                if(SetVDonline_connCode == 200 || SetVDonline_connCode == 0 || SetVDonline_connCode == 428) { // 2018.12.11 bug fix
                    GB._jsonArraySize = 0;
                    GB._jsonArrayData = null;
                    // 2018.12.04 新增VD&RDP狀態判斷
                    GB.RDP_IsPingOK = false;
                    GB.Check_RDPPing = false;
                    GB.RDP_Status = -1;
                    GB.RDP_IpCount1 = 0;
                    GB.RDP_IpCount2 = 0;
                    int RePingCount = 0;
                    boolean Reset = true;
                    sleep(500);                
//                    if(SetVDonline_connCode == 200 ) { // 2018.12.12 新增 431NoVGA 和第一次開機
                        new PingRDP(address, port, vdid);                 
//                    }

                    int ResetTimeType = -100;// 2018.18.18 new create & reborn

                    long startTime = System.currentTimeMillis(); // fetch starting time
                    while(false || (System.currentTimeMillis()-startTime) < time_count) {  // 2018.12.11 bug fix   
                        if(GB.stopconnect) {
                            GB.ExitFlag = true;
                            break;                        
                        }                        
                        //System.out.println("Current Time : " + (System.currentTimeMillis()-startTime));
                        // 2018.12.12 新增 431NoVGA 和第一次開機
                        // 2018.12.27  其他中斷連線狀態 - Crash & Migrate
                        if(GB.RDP_Status == 6 || GB.RDP_Status == 7)  {
                            ReLogin = true;
                            break;
                        }                           
                        
                        
                        switch (GB.RDP_Stage_Type) {
                           case  0:                               
                                if(GB.RDPFirst == 1 || GB.RDPFirst == 2) { // 2018.18.18 new create & reborn 
                                    if(GB.RDP_Stage_Type != ResetTimeType) {
                                        startTime = System.currentTimeMillis();     
                                        ResetTimeType = GB.RDP_Stage_Type;
                                    }                                    
                                }
                               break;
                            case 1:   
                                if(GB.RDPFirst == 1 || GB.RDPFirst == 2) { // 2018.18.18 new create & reborn 
                                    if(GB.RDP_Stage_Type != ResetTimeType) {
                                        startTime = System.currentTimeMillis();     
                                        ResetTimeType = GB.RDP_Stage_Type;
                                    }                                    
                                }
                                break;
                            case 2:   
                                if(GB.RDPFirst == 1 || GB.RDPFirst == 2) { // 2018.18.18 new create & reborn 
                                    if(GB.RDP_Stage_Type != ResetTimeType) {
                                        startTime = System.currentTimeMillis();     
                                        ResetTimeType = GB.RDP_Stage_Type;
                                    }                                    
                                }
                                break;                  
                            case 3: 
                                if(GB.RDPFirst == 1 || GB.RDPFirst == 2) { // 2018.18.18 new create & reborn 
                                    if(GB.RDP_Stage_Type != ResetTimeType) {
                                        startTime = System.currentTimeMillis();     
                                        ResetTimeType = GB.RDP_Stage_Type;
                                    }                                    
                                }
                                break;
                            case  4:
                                if(GB.RDPFirst == 1 || GB.RDPFirst == 2) { // 2018.18.18 new create & reborn 
                                    if(GB.RDP_Stage_Type != ResetTimeType) {
                                        startTime = System.currentTimeMillis();     
                                        ResetTimeType = GB.RDP_Stage_Type;
                                    }                                    
                                }
                                break;
                            case  5:   
                               break;   
                            case -1:   
                                ReLogin = true;
                                rdp_stage = -1;
                                break;
                            case -2:   
                                ReLogin = true;
                                rdp_stage = -2;
                                break;                  
                            case -3:   
                                ReLogin = true;
                                rdp_stage = -3;
                                break;
                            case -4:   
                                ReLogin = true;
                                rdp_stage = -4;
                                break;                               
                        }        
                        
                        if(rdp_stage == -1 || rdp_stage == -2 || rdp_stage == -3 || rdp_stage == -4) 
                            break;

                        
                        if(!"".equals(GB.ConnectionAddress)) {
                            RunRdp(GB.ConnectionAddress, GB.ConnectionPort, Uname, Password, getSize(), enableusb, adAuth, adDomain); // 2018.12.07 RDP ping ip and port
                            ReLogin = true;
                            break;
                        }                     
                        if(GB._jsonArraySize == 0) {
                            new PingRDP(address, port, vdid);
                        } else {                        
                            JsonArray _jsonArrayData = GB._jsonArrayData;
    //                        System.out.println("Get RDP IP");            

                            if(GB.RDP_IpCount1 == GB.RDP_IpCount2 && GB.RDP_IpCount1 != 0 && GB.RDP_IpCount2 != 0) {
                                Reset = true;
                                GB.Check_RDPPing = false;                
                                GB.RDP_IpCount1 = 0;
                                GB.RDP_IpCount2 = 0;                            
                            }

                            if(!GB.RDP_IsPingOK && Reset) {        
                                Reset = false;
                                if(RePingCount < 3) 
                                    new PingRDP.PingRDPIP(_jsonArrayData);                                                              
                                else
                                    StopGetRDP = true;                                

                                RePingCount++;                                                                                            
                            }                                                                        
                        }
                        if(GB.ExitFlag) { // 2018.11.11 新增AcroDesk & RDP Protocol
                            System.out.println("RDP direct exit");
                            break;
                        } 
                        // 2018.12.04 新增VD&RDP狀態判斷
                        if(StopGetRDP && (GB.RDP_IpCount1 == GB.RDP_IpCount2)) { 
                            if(!GB.RDP_IsPingOK && GB.RDP_Status != 5 && GB._jsonArraySize != 0) {
                                checkLogin_flag = true;
                                ReLogin = true;
                                sleep(500);
                                Platform.runLater(() -> { 
                                     ReLogin(2);                     
                                });  
                                break;
                            }                                         
                        }                     
                        System.out.println("Test RDP"); 
                        TimeUnit.SECONDS.sleep(2);
                    }              

                    System.out.println("Finish Run RDP"); 

                    // 要增加重新登入連線
                    if(!ReLogin && !GB.ExitFlag) {// 2018.12.11 bug fix
                        checkLogin_flag = true; 
                        sleep(500);                    
                        Platform.runLater(() -> { 
                             ReLogin(1);                     
                        });                
                    }
                    
                    // 2018.12.12 新增 431NoVGA 和第一次開機
                    if(rdp_stage != -100) {
                        GB.RDP_Stage_Type = rdp_stage;
                        checkLogin_flag = true; 
                        sleep(500);
                        Platform.runLater(() -> { 
                            ReLogin(GB.RDP_Stage_Type);                     
                        });                
                    }   
                    
                    // 2018.12.27  其他中斷連線狀態 - Crash & Migrate
                    if(GB.RDP_Status == 6 || GB.RDP_Status == 7) {
                        int t = -100;
                        if(GB.RDP_Status == 6) {
                            t = -11;
                            System.out.println("vd has crashed");
                        }                       
                            
                        else if(GB.RDP_Status ==7) {
                            t = -12;
                            System.out.println("vd is Migrating");
                        }
                            
                        GB.RDP_Status = t;
                        checkLogin_flag = true; 
                        sleep(500);
                        Platform.runLater(() -> { 
                            ReLogin(GB.RDP_Status);                     
                        });                
                    }                     
                    
                }
               
                
                conn_setVDonline.disconnect();                                                    
            }            

           } catch (MalformedURLException e) {} catch (IOException e) {}        
    }
         
    public void RunRdp(String address, String port, String Uname, String Password, int s, boolean enableusb, boolean adAuth, String adDomain) throws IOException, InterruptedException {
        System.out.println("RunRDP IpAddress : " + address + ":" + port + " Uname : " + Uname + " Password : " + Password);  
        GB.RejumpProtocol_RDP = "RDP-" + address + ":" + port;        
//        PingIP.isReachableByTcp(address, 3389, 3000);
        if(s == 0) {  // 2018.11.11 新增AcroDesk & RDP Protocol  1 -> 0                 
           checkLogin_flag = true; 
            System.out.println("直接登入");  
        } else {
            checkLogin_flag = true; 
            sleep(500);
            
            // 2019.01.07 PCI RDP user name format(@) 
            String[] _StrArray = null;
            if(Uname.contains("@")) {
                _StrArray = Uname.split("@");
                Uname   = _StrArray[0];                               
            }

            // 20019.02.18 新增FreeRDP for dual screen & usb redirection
            /*
            if(GB.FreeRDPEnable)
                WriteFreeRDPFile(address, port, Uname, Password);
            else
                WriteRDPFile(address + ":" + port, Uname, Password);
            */
            
            if(!GB.FreeRDPEnable && !enableusb)
                WriteRDPFile(address + ":" + port, Uname + (adAuth ? "@" + adDomain : ""), Password);                
            else
                WriteFreeRDPFile(address, port, Uname + (adAuth ? "@" + adDomain : ""), Password, enableusb);            
            
            // 2018.11.11 新增AcroDesk & RDP Protocol
            /*
            new Thread(new Runnable() {                
                @Override
                public void run() {
                    long startTime = System.currentTimeMillis();
                    int detectCount = 0;
                    String line_Check = null;
                    while(false || (System.currentTimeMillis()-startTime) < 5000) {

                        try {
                            String command = "ps -aux | grep '[r]emmina'";  
                            Process Check_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",command});
                            BufferedReader output_Check = new BufferedReader (new InputStreamReader(Check_process.getInputStream()));
                            line_Check = output_Check.readLine();
                        } catch(IOException e) {}

                        if(line_Check != null) {
                            detectCount++; 
                            if(detectCount == 3) {
                                checkLogin_flag = true; // 2017.11.24 william 單一帳號多個VD登入
                                break;
                            }                                            
                        }                                        

                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(QueryMachine.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }                
                    System.out.println("Detect Spice"); 
                }
            }).start();               
            */
            
            
//            checkLogin_flag = true; 
            System.out.println("多個VD登入");                                    
        }                                
    }    
    
    // 2018.12.04 新增VD&RDP狀態判斷
    public void ReLogin(int type) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
        alert.setTitle(WordMap.get("WW_Header"));
        alert.setHeaderText(null);


        if(type == 1) {
            alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
            alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE);             
            alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_RdpError_ConnectTimeout")+"\n"
                             +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_RdpSuggest_Relogin")
            );        
        } else if(type == 2) {
            if("English".equals(WordMap.get("SelectedLanguage"))) {
                alert.getDialogPane().setMinSize(700,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(700,Region.USE_PREF_SIZE);             
            } else {
                alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE);             
            }
            
            alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_RdpError_ConnectFail")+"\n"
                             +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_RdpSuggest_ReConnect")
            );                
        } else if(type == 3) {
            alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
            alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE);             
            alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_SpiceError_ConnectTimeout")+"\n"
                             +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_RdpSuggest_Relogin")
            );               
        } 
        // 2018.12.12 新增 431NoVGA 和第一次開機
        else if(type == -1) {
            if("English".equals(WordMap.get("SelectedLanguage"))) {
                alert.getDialogPane().setMinSize(700,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(700,Region.USE_PREF_SIZE);             
            } else {
                alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE);             
            }
            
            alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_RDP_Stage_1_Error")+"\n"
                             +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_RDP_Stage_1_Suggest")
            );                
        } 
        else if(type == -2) {
            if("English".equals(WordMap.get("SelectedLanguage"))) {
                alert.getDialogPane().setMinSize(700,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(700,Region.USE_PREF_SIZE);            
            } else {
                alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE);             
            }
            
            alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_RDP_Stage_2_Error")+"\n"
                             +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_RDP_Stage_2_Suggest")
            );                
        } 
        else if(type == -3) {
            if("English".equals(WordMap.get("SelectedLanguage"))) {
                alert.getDialogPane().setMinSize(700,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(700,Region.USE_PREF_SIZE);           
            } else {
                alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE);             
            }
            
            alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_RDP_Stage_3_Error")+"\n"
                             +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_RDP_Stage_3_Suggest")
            );                
        } 
        else if(type == -4) {
            if("English".equals(WordMap.get("SelectedLanguage"))) {
                alert.getDialogPane().setMinSize(700,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(700,Region.USE_PREF_SIZE);           
            } else {
                alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE);             
            }
            
            alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_RDP_Stage_4_Error")+"\n"
                             +"\n"+WordMap.get("Message_Suggest")+" ： "+WordMap.get("Message_RDP_Stage_4_Suggest")
            );                
        }  
         // 2018.18.18 new create & reborn
        else if(type == -10) {
            if("English".equals(WordMap.get("SelectedLanguage"))) {
                alert.getDialogPane().setMinSize(700,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(700,Region.USE_PREF_SIZE);           
            } else {
                alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE);             
            }
            
            alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_No_UserAccount"));                
        }
        
        // 2018.12.27  其他中斷連線狀態 - Crash & Migrate
        else if(type == -11) {
            if("English".equals(WordMap.get("SelectedLanguage"))) {
                alert.getDialogPane().setMinSize(700,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(700,Region.USE_PREF_SIZE);           
            } else {
                alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE);             
            }
            
            alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VMCrash"));                
        }    
        
        else if(type == -12) {
            if("English".equals(WordMap.get("SelectedLanguage"))) {
                alert.getDialogPane().setMinSize(700,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(700,Region.USE_PREF_SIZE);           
            } else {
                alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE);             
            }
            
            alert.setContentText(WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_VD_429"));                
        }           
        
        ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(buttonTypeOK); 
        alert.showAndWait();                      
    }    
    
    /******將Spice需要的資料 寫入txt檔********/
    public void WriteSpiceText(String address, String port, String Uname, String Password){
        SpiceAddress=address;
        SpicePort=port;
        CurrentUserName=Uname; //load CurrentUserName
        CurrentPasseard=Password;//load CurrentPasseard
        try{
            File myFile=new File("jsonfile/SpiceText.txt");
            if(myFile.exists()){
                myFile.delete();
                myFile=null;
            }
            // 2019.03.28 VM/VD migration reconnect
            File temp = new File("jsonfile/tempSpiceText.txt");
            if(temp.exists()){
                temp.delete();
                temp = null;
            }            
            //FileWriter SWriter=new FileWriter("SpiceText.txt",true);
            FileWriter SWriter=new FileWriter("jsonfile/SpiceText.txt",true);
            FileWriter SWriterTemp = new FileWriter("jsonfile/tempSpiceText.txt",true); // 2019.03.28 VM/VD migration reconnect
                        
            // 2019.03.28 VM/VD migration reconnect
            FileWriter[] fileNames = {SWriter,SWriterTemp};
            
            for(int i=0;i<fileNames.length;i++) {
                try (BufferedWriter bufferedSWriter = new BufferedWriter(fileNames[i])) {
                    bufferedSWriter.write("[virt-viewer]");
                    bufferedSWriter.newLine();
                    bufferedSWriter.write("type = spice");
                    bufferedSWriter.newLine();
                    bufferedSWriter.write("host= ");
                    bufferedSWriter.write(SpiceAddress);               
                    bufferedSWriter.newLine();
                    bufferedSWriter.write("port = ");
                    bufferedSWriter.write(SpicePort);
                    bufferedSWriter.newLine();              
                    bufferedSWriter.write("username = ");
                    bufferedSWriter.write(CurrentUserName);
                    bufferedSWriter.newLine();
                    bufferedSWriter.write("password = ");
                    bufferedSWriter.write(CurrentPasseard);
                    bufferedSWriter.newLine();
                    bufferedSWriter.write("fullscreen = 1");
                    bufferedSWriter.newLine();
                    bufferedSWriter.write("delete-this-file = 1");
                    bufferedSWriter.newLine();
                    bufferedSWriter.write("title =  ");
                    bufferedSWriter.write(CurrentUserName);
                    bufferedSWriter.write("-");
                    bufferedSWriter.write(SpiceAddress);
                    bufferedSWriter.newLine();
                    bufferedSWriter.flush();
                    bufferedSWriter.close();                 

                }
            }            
                                    
            /*
            try (BufferedWriter bufferedSWriter = new BufferedWriter(SWriter)) {
                bufferedSWriter.write("[virt-viewer]");
                bufferedSWriter.newLine();
                bufferedSWriter.write("type = spice");
                bufferedSWriter.newLine();
                bufferedSWriter.write("host= ");
                bufferedSWriter.write(SpiceAddress);               
                bufferedSWriter.newLine();
                bufferedSWriter.write("port = ");
                bufferedSWriter.write(SpicePort);
                bufferedSWriter.newLine();              
                bufferedSWriter.write("username = ");
                bufferedSWriter.write(CurrentUserName);
                bufferedSWriter.newLine();
                bufferedSWriter.write("password = ");
                bufferedSWriter.write(CurrentPasseard);
                bufferedSWriter.newLine();
                bufferedSWriter.write("fullscreen = 1");//1=大視窗 0=小視窗
                bufferedSWriter.newLine();
                bufferedSWriter.write("delete-this-file = 1");
                bufferedSWriter.newLine();
                bufferedSWriter.write("title =  ");
                bufferedSWriter.write(CurrentUserName);
                bufferedSWriter.write("-");
                bufferedSWriter.write(SpiceAddress);
                bufferedSWriter.newLine();
                //  2018.11.06 william 多執行緒Ping IP
                bufferedSWriter.flush();
                bufferedSWriter.close();                 
                
            }
            */        
            
        }catch(IOException e){
        }
    }
    // 2018.11.11 新增AcroDesk & RDP Protocol
    public void WriteRDPFile(String address, String Uname, String Password) {
        final Base64.Encoder encoder = Base64.getEncoder();
        byte[] textByte = null; 
        String _line;        
        Process processEncodePWD;        
        try {
            textByte = Password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(QueryMachine.class.getName()).log(Level.SEVERE, null, ex);
        }
        //編碼
        String encodedPassword = encoder.encodeToString(textByte);                        
        try {
            processEncodePWD = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","python /root/encodePWD.python " + encodedPassword});
            BufferedReader input = new BufferedReader(new InputStreamReader(processEncodePWD.getInputStream()));
            if ((_line = input.readLine()) != null) {
                
                encodedPassword = _line;
                System.out.println("python Encode Password: " + _line); 
            } 
        } catch (IOException ex) {
            Logger.getLogger(QueryMachine.class.getName()).log(Level.SEVERE, null, ex);
        }
                               
        File RDPFile = new File("jsonfile/RDP.remmina");
        if(RDPFile.exists()) {
            RDPFile.delete();
            RDPFile = null;
        }    
        
        // 2019.03.28 VM/VD migration reconnect
        File temp = new File("jsonfile/tempRDP.remmina");
        if(temp.exists()){
            temp.delete();
            temp = null;
        }           
                      
        
        try {
            
            // 2019.03.28 VM/VD migration reconnect
            FileWriter RdpWriter = new FileWriter("jsonfile/RDP.remmina", true);
            FileWriter RdpWriterTemp = new FileWriter("jsonfile/tempRDP.remmina", true);

            // 2019.03.28 VM/VD migration reconnect
            FileWriter[] fileNames = {RdpWriter, RdpWriterTemp};
            
            for(int i=0;i<fileNames.length;i++) {
                try (BufferedWriter WriterData = new BufferedWriter(fileNames[i])) {
                    WriterData.write("[remmina]");                
                    WriterData.newLine();
                    WriterData.write("sound=local");
                    WriterData.newLine();                
                    WriterData.write("sharefolder=");
                    WriterData.newLine();                                 
                    WriterData.write("name=RDP-");
                    WriterData.write(address);               
                    WriterData.newLine();                
                    WriterData.write("cert_ignore=1");
                    WriterData.newLine();  
                    WriterData.write("console=0");
                    WriterData.newLine();  
                    WriterData.write("ssh_enabled=0");
                    WriterData.newLine();  
                    WriterData.write("exec=");
                    WriterData.newLine(); 
                    WriterData.write("clientname=");
                    WriterData.newLine(); 
                    WriterData.write("loadbalanceinfo=");
                    WriterData.newLine(); 
                    WriterData.write("server=");
                    WriterData.write(address);               
                    WriterData.newLine(); 
                    WriterData.write("colordepth=64");
                    WriterData.newLine(); 
                    WriterData.write("ssh_auth=0");
                    WriterData.newLine();                 
                    WriterData.write("postcommand=");
                    WriterData.newLine();                 
                    WriterData.write("group=");
                    WriterData.newLine();                 
                    WriterData.write("sharesmartcard=0");
                    WriterData.newLine(); 
                    WriterData.write("quality=9"); // 0 -> 9
                    WriterData.newLine(); 
                    WriterData.write("username=");
                    WriterData.write(Uname);               
                    WriterData.newLine(); 
                    WriterData.write("ssh_charset=");
                    WriterData.newLine(); 
                    WriterData.write("ssh_loopback=0");
                    WriterData.newLine(); 
                    WriterData.write("ssh_username=");
                    WriterData.newLine(); 
                    WriterData.write("gateway_password=");
                    WriterData.newLine(); 
                    WriterData.write("gateway_server=");
                    WriterData.newLine(); 
                    WriterData.write("gateway_username=");
                    WriterData.newLine(); 
                    WriterData.write("microphone=0");
                    WriterData.newLine(); 
                    WriterData.write("execpath=");
                    WriterData.newLine();
                    WriterData.write("resolution_width=");
                    WriterData.newLine();
                    WriterData.write("gateway_usage=0");
                    WriterData.newLine();
                    WriterData.write("disableautoreconnect=0");
                    WriterData.newLine();
                    WriterData.write("password=");
                    WriterData.write(encodedPassword);               
                    WriterData.newLine(); 
                    WriterData.write("resolution_height=");
                    WriterData.newLine();
                    WriterData.write("security=");
                    WriterData.newLine();
                    WriterData.write("domain=");
                    WriterData.newLine();
                    WriterData.write("disablepasswordstoring=0");
                    WriterData.newLine();
                    WriterData.write("precommand=");
                    WriterData.newLine();
                    WriterData.write("ssh_server=");
                    WriterData.newLine();
                    WriterData.write("protocol=RDP");
                    WriterData.newLine();
                    WriterData.write("shareprinter=0");
                    WriterData.newLine();
                    WriterData.write("gateway_domain=");
                    WriterData.newLine();
                    WriterData.write("disableclipboard=0");
                    WriterData.newLine();
                    WriterData.write("ssh_privatekey=");
                    WriterData.newLine();
                    WriterData.write("last_success=");
                    WriterData.newLine();
                    WriterData.write("window_maximize=1");
                    WriterData.newLine();
                    WriterData.write("viewmode=4");
                    WriterData.newLine();                                
                    WriterData.flush();
                    WriterData.close();                                  
                }     
            }             
            
            /*
            try (BufferedWriter WriterData = new BufferedWriter(RdpWriter)) {
                WriterData.write("[remmina]");                
                WriterData.newLine();
                WriterData.write("sound=local");
                WriterData.newLine();                
                WriterData.write("sharefolder=/mnt/usb1");
                WriterData.newLine();                                 
                WriterData.write("name=RDP-");
                WriterData.write(address);               
                WriterData.newLine();                
                WriterData.write("cert_ignore=1");
                WriterData.newLine();  
                WriterData.write("console=0");
                WriterData.newLine();  
                WriterData.write("ssh_enabled=0");
                WriterData.newLine();  
                WriterData.write("exec=");
                WriterData.newLine(); 
                WriterData.write("clientname=");
                WriterData.newLine(); 
                WriterData.write("loadbalanceinfo=");
                WriterData.newLine(); 
                WriterData.write("server=");
                WriterData.write(address);               
                WriterData.newLine(); 
                WriterData.write("colordepth=64");
                WriterData.newLine(); 
                WriterData.write("ssh_auth=0");
                WriterData.newLine();                 
                WriterData.write("postcommand=");
                WriterData.newLine();                 
                WriterData.write("group=");
                WriterData.newLine();                 
                WriterData.write("sharesmartcard=0");
                WriterData.newLine(); 
                WriterData.write("quality=9"); // 0 -> 9
                WriterData.newLine(); 
                WriterData.write("username=");
                WriterData.write(Uname);               
                WriterData.newLine(); 
                WriterData.write("ssh_charset=");
                WriterData.newLine(); 
                WriterData.write("ssh_loopback=0");
                WriterData.newLine(); 
                WriterData.write("ssh_username=");
                WriterData.newLine(); 
                WriterData.write("gateway_password=");
                WriterData.newLine(); 
                WriterData.write("gateway_server=");
                WriterData.newLine(); 
                WriterData.write("gateway_username=");
                WriterData.newLine(); 
                WriterData.write("microphone=0");
                WriterData.newLine(); 
                WriterData.write("execpath=");
                WriterData.newLine();
                WriterData.write("resolution_width=");
                WriterData.newLine();
                WriterData.write("gateway_usage=0");
                WriterData.newLine();
                WriterData.write("disableautoreconnect=0");
                WriterData.newLine();
                WriterData.write("password=");
                WriterData.write(encodedPassword);               
                WriterData.newLine(); 
                WriterData.write("resolution_height=");
                WriterData.newLine();
                WriterData.write("security=");
                WriterData.newLine();
                WriterData.write("domain=");
                WriterData.newLine();
                WriterData.write("disablepasswordstoring=0");
                WriterData.newLine();
                WriterData.write("precommand=");
                WriterData.newLine();
                WriterData.write("ssh_server=");
                WriterData.newLine();
                WriterData.write("protocol=RDP");
                WriterData.newLine();
                WriterData.write("shareprinter=0");
                WriterData.newLine();
                WriterData.write("gateway_domain=");
                WriterData.newLine();
                WriterData.write("disableclipboard=0");
                WriterData.newLine();
                WriterData.write("ssh_privatekey=");
                WriterData.newLine();
                WriterData.write("last_success=");
                WriterData.newLine();
                WriterData.write("window_maximize=1");
                WriterData.newLine();
                WriterData.write("viewmode=4");
                WriterData.newLine();                                
                WriterData.flush();
                WriterData.close();                                  
            }            
            */
        } catch (IOException ex) {
            Logger.getLogger(QueryMachine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    // 20019.02.18 新增FreeRDP for dual screen & usb redirection
    public void WriteFreeRDPFile(String address, String port, String Uname, String Password, boolean enableusb) {
//        String[] Tmp; 
//        Tmp = address.split(":");
                               
        File RDPFile = new File("jsonfile/FreeRDP.txt");
        if(RDPFile.exists()) {
            RDPFile.delete();
            RDPFile = null;
        }    
        
        // 2019.03.28 VM/VD migration reconnect
        File temp = new File("jsonfile/tempFreeRDP.txt");
        if(temp.exists()){
            temp.delete();
            temp = null;
        }           
        
        try {
            FileWriter RdpWriter = new FileWriter("jsonfile/FreeRDP.txt", true);
                        
            // 2019.03.28 VM/VD migration reconnect
            FileWriter RdpWriterTemp = new FileWriter("jsonfile/tempFreeRDP.txt", true);

            // 2019.03.28 VM/VD migration reconnect
            FileWriter[] fileNames = {RdpWriter, RdpWriterTemp};
            
            for(int i=0;i<fileNames.length;i++) {
                try (BufferedWriter WriterData = new BufferedWriter(fileNames[i])) {
                    if(enableusb)
                        WriterData.write("xfreerdp /u:" + Uname + " /p:" + Password + " /v:" + address + " /port:" + port + " /t:" + address + ":" + port + " /f /gfx-h264:AVC420 /floatbar:sticky:off,default:hidden,show:always -window-drag /network:broadband-low +wallpaper /sound:sys:pulse /bpp:8 -themes /gdi:hw /drive:hotplug,* /smartcard +gfx-progressive /rfx-mode:video /multimon  /cert-ignore");                                   
                    else    
                        WriterData.write("xfreerdp /u:" + Uname + " /p:" + Password + " /v:" + address + " /port:" + port + " /t:" + address + ":" + port + " /f /gfx-h264:AVC420 /floatbar:sticky:off,default:hidden,show:always -window-drag /network:broadband-low +wallpaper /sound:sys:pulse /bpp:8 -themes /gdi:hw +gfx-progressive /rfx-mode:video /multimon  /cert-ignore");                                   
                    WriterData.newLine();                
                    WriterData.flush();
                    WriterData.close();                                       
                }     
            }              
                                    
            /*
            try (BufferedWriter WriterData = new BufferedWriter(RdpWriter)) {
                WriterData.write("xfreerdp /u:" + Uname + " /p:" + Password + " /v:" + address + " /port:" + port + " /t:" + address + ":" + port + " /f /gfx-h264:AVC420 /floatbar:sticky:off,default:hidden,show:always -window-drag /network:broadband-low +wallpaper /sound:sys:pulse /bpp:8 -themes /gdi:hw /drive:hotplug,* +gfx-progressive /rfx-mode:video /multimon  /cert-ignore");                                   
                WriterData.newLine();                
                WriterData.flush();
                WriterData.close();                                  
            } 
            */
            
        } catch (IOException ex) {
            Logger.getLogger(QueryMachine.class.getName()).log(Level.SEVERE, null, ex);
        }    
    
    }
    
    @SuppressWarnings("NotifyNotInSynchronizedContext")
    public void Ping(String ipaddr,String port) throws InterruptedException, IOException{ // 2017.08.10 william IP增加port欄位，預設443               
        setIP(ipaddr);
        setPort(port); // 2017.08.10 william IP增加port欄位，預設443   
        setJob("Ping");           
        doPing();
        //System.out.println(" -----doPing();------\n");
        //Thread QMT=new QueryMachine(WordMap);
        QMT=new QueryMachine(WordMap);
        QMT.start();     
        //System.out.println("--Ping--testError--"+testError+" \n");
        //System.out.println(testError+" \n");       
        if(!testError){
            //QMT.wait();
            QMT.notifyAll();      
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
 
    // 2018.02.06 william 雙螢幕實作
    public void  Dual_Screen(String ipaddr,String port) throws MalformedURLException, IOException, InterruptedException {        
        Get_Display_process = Runtime.getRuntime().exec("xrandr");
        try (BufferedReader output_Display_info = new BufferedReader (new InputStreamReader(Get_Display_process.getInputStream()))) {
            String line_Display                       = output_Display_info.readLine();
            String[] Display_name_Line_list         = null;
            String[] Display_current_data_Line_list = null;     
            while(line_Display != null) {
                /*********** 取得螢幕名稱 ***********/                
                if ( line_Display.contains("connected") && !line_Display.contains("disconnected")) {
                    if (!IsPrimary) {
                        Display_name_Line_list = line_Display.split(" ");
                        Display_primary_name   = Display_name_Line_list[0];
                        System.out.println(" Primary Display  : " + Display_primary_name + "\n");                    
                        IsPrimary = true;
                    } else {
                        Display_name_Line_list = line_Display.split(" ");
                        Display_second_name    = Display_name_Line_list[0];
                        System.out.println(" Second Display  : " + Display_second_name + "\n");                    
                        IsSecond = true;                        
                    }                                                            
                }                
                                
                line_Display = output_Display_info.readLine();
            }

            output_Display_info.close();        
        }
        
        System.out.println("** 主螢幕偵測 ** : " + IsPrimary + "類型" + Display_primary_name + "\n");
        System.out.println("** 第二螢幕偵測 ** : " + IsSecond + "類型" + Display_second_name + "\n");
        
        JSONObject jobj = new JSONObject();
        if(!IsSecond) {
            jobj.put("Monitor", 1);
        } else {
            jobj.put("Monitor", 2);
        }
        String Monitor = jobj.toString();  
        try {
            Monitor_url  = new URL("https://" + ipaddr + ":" + port + "/vdi/user/vd/" + getVDID() + "/info");          
            Monitor_conn = (HttpURLConnection) Monitor_url.openConnection();                                     // 建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線
            Monitor_conn.setRequestMethod("PUT");                                                        // 利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
            Monitor_conn.setDoOutput(true);
            Monitor_conn.setDoInput(true);
            Monitor_conn.setRequestProperty("Accept"        , "application/json");
            Monitor_conn.setRequestProperty("User-Agent"    , "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            Monitor_conn.setRequestProperty("Content-Type"  , "application/json");                       // 設置發送數據的格式("Content-Type", "application/json; charset=UTF-8")
            Monitor_conn.setRequestProperty("Content-length", String.valueOf(Monitor.getBytes().length)); // ("Content-length", String.valueOf(query.length()))
            Monitor_conn.setRequestProperty("Connection"    , "Keep-Alive");                             // 維持長連接
            Monitor_conn.setUseCaches (false);
            Monitor_conn.setInstanceFollowRedirects(true);        

            Monitor_conn.setConnectTimeout(10000);                                                       // 5秒改10秒，不然在getResponseCode容易失敗       
            /*----------------------------content----------------------------*/
            try (OutputStream ops = Monitor_conn.getOutputStream()) {
                ops.write(Monitor.getBytes());
                ops.flush();
                ops.close();
                System.out.println("雙螢幕-output out  : " + ops + "\n");                  
                System.out.println("雙螢幕-URL         : " + Monitor_url + "\n");             
                System.out.println("雙螢幕-Resp Code   : " + Monitor_conn.getResponseCode() + "\n");
                System.out.println("雙螢幕-Resp Message: " + Monitor_conn.getResponseMessage() + "\n");                           
                Monitor_conn.disconnect();      
            }                                 
        } catch (MalformedURLException e) {

        } catch (IOException e) {    

        }        
    }
    
    public static synchronized String getVDID() {
        return VDid;
    }    
    
    public static synchronized void setVDID(String vdid) {
        VDid = vdid;
    }    
    
    // 監聽螢幕Size是否改變
    private void Duplicate_check_listener() {
        Duplicate_timer = new Timer(); 
        Duplicate_task  = new TimerTask() {
            @Override
            public void run() {                    
                Platform.runLater(() -> { 
                    // System.out.println(" timer1 start \n"); test      
//                    System.out.println(" RemoteViewer ready \n");
                    try {
                        Duplicate_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",Duplicate_command});
                        BufferedReader output_Duplicate_check = new BufferedReader (new InputStreamReader(Duplicate_process.getInputStream()));
                        line_Duplicate = output_Duplicate_check.readLine();
                        if(line_Duplicate != null) {
                            Duplicate_timer.cancel();
                            /** 2018.03.13 william 雙螢幕實作 - 同步處理**/
                            if(GB.DuplicateSize == 2 && !GB.OneDisplayDisable) {
                                try {
                                    sleep(3000);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(QueryMachine.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                // Runtime.getRuntime().exec(GB.monitoroff_exec_string);
                                Runtime.getRuntime().exec(GB.monitor_exec_string);
                            }                             
                        }                     
                    } catch (IOException ex) {
                        Logger.getLogger(QueryMachine.class.getName()).log(Level.SEVERE, null, ex);
                    }

                });              
            }
        };
        Duplicate_timer.schedule(Duplicate_task, 1000, 1000);          
    }     
    
    public void CheckRmProcess(Stage EnterType) {                
        String line;
        try {
            Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","ps -aux | grep '[m]onitorSpice'"});
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            if ((line = input.readLine()) != null) {
                System.out.println("monitorSpice.sh執行中!"); 
            } else {
                
                System.out.println("未執行!"); 
                
            Platform.runLater(new Runnable() {             
             public void run() {                
                final Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
                alert.setTitle(WordMap.get("EW_Header"));                   // 設定對話框視窗的標題列文字
                alert.setHeaderText("");                                    // 設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
                if(WordMap.get("SelectedLanguage").equals("English")){                
                    alert.setContentText("An exception occurred while logging in." + 
                                        "\n" + 
                                        "\n" +
                                        "Please click the \"Reboot\" button to reboot!"
                    );                
                } else {
                    alert.setContentText(WordMap.get("Message_Error_MonitorSpice")); // 設定對話框的訊息文字
                }                                                                
                Bounds mainBounds = EnterType.getScene().getRoot().getLayoutBounds();       
                alert.setX(EnterType.getX() + (mainBounds.getWidth() - 420 ) / 2);  //5.7
                alert.setY(EnterType.getY() + (mainBounds.getHeight() - 150 ) / 2);  //5    
                ButtonType buttonTypeOK       = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);      // 確定
                alert.getButtonTypes().setAll(buttonTypeOK); 
                Button button_ok              = (Button) alert.getDialogPane().lookupButton(buttonTypeOK);
                alert.show();                                               // 顯示對話框，並等待對話框被關閉時才繼續執行之後的程式                       
                }             
             });                
                
                
                //return true;
            }
        } catch (IOException err) {
            System.out.println(err); 
        }        
        //return false;
    }        
    
    // 2018.11.11 新增AcroDesk & RDP Protocol
    public static synchronized int getSize() {
        return select_size;
    }

    public static synchronized void setSize(int _size) {
        select_size = _size;
    }      
    
    // 2019.03.28 VM/VD migration reconnect
    public int GetVDStatus(String address, String port, String vdid) throws MalformedURLException, IOException {
        /* 
            -2 : VM_Waiting_Poweron  
            -1 : VM_Disable  
            0 : VM_Stop                
            1 : VM_Poweron  
            2 : VM_Blocked; 
            3 : VM_Pause   
            4 : VM_Shutdowning  
            5 : VM_Shutdown  
            6 : VM_Crash 
            7 : VM_Migrate 
            10 : VM_Preparing_Boot         
        */
        
        int VdStatus = -100;
        
        try{           
            URL url = new URL("https://" + address + ":" + port + "/vdi/user/vd/" + vdid); // https://{IP}/vdi/user/vd/63f6647c-4c65-11e9-99f8-002590753af1
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
	    conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setConnectTimeout(10000); 
            conn.connect();
                        
            // 讀取資料
            InputStream _inputStream = conn.getInputStream();
            StringBuilder _inputData;
            String _line;    

            try (BufferedReader br = new BufferedReader(new InputStreamReader(_inputStream))) {              
                _inputData = new StringBuilder("");
                while ((_line = br.readLine()) != null) {            
                    _inputData.append(_line);         
                }             

                br.close();
            }            
            
            String _data = _inputData.toString();   
            JsonParser _jsonParser = new JsonParser();
            JsonObject Jsondata = (JsonObject)_jsonParser.parse(_data).getAsJsonObject();  
            
            if(conn.getResponseCode() != 200){
                VdStatus = -100;
            } else {
                // System.out.println("VdStatus : " + Jsondata.get("VdStatus")); 
                VdStatus = Jsondata.get("VdStatus").getAsInt();  
            }

            conn.disconnect();
        } catch (MalformedURLException e) {} catch (IOException e) {}  
        
        return VdStatus;
    }    
  
    
/*
    public boolean doPing() throws UnknownHostException, IOException{      
            
        if((IPAddress==null)||("".equals(IPAddress))){
        //IP Address is empty.
        //testError=false;
        lockResult.lock();
            try{ 
                testError=false;
                setResult(testError);
                System.out.println("IPAddress=null : "+ testError +"\n");
                //setMessages(WordMap.get("Ping_Message_F")+IPAddress+WordMap.get("Ping_Message_R2"));
   
            }finally{              
                
                lockResult.unlock();       
            }
                return false;
        }
           
        IPAddr=InetAddress.getByName(IPAddress);
        
        if(IPAddr.isReachable(1000)){ 
            // Success Ping
            lockResult.lock();
            try{               
                //setMessages(WordMap.get("Ping_Message_F")+IPAddress+WordMap.get("Ping_Message_R1")); 
                testError=true;
                setResult(testError);
                System.out.println(" ----Success--- \n");
                System.out.println(" ----Success--: "+testError+ "\n");

            }finally{
                
                lockResult.unlock();
               
            }
            return true;
        }
         else{
            // Fail Ping                           
            lockResult.lock();           
            try{ 
                //setMessages(WordMap.get("Ping_Message_F")+IPAddress+WordMap.get("Ping_Message_R2")); 
                testError=false;
                setResult(testError);
                System.out.println("--------Fail---------- \n");
                System.out.println(" ----Fail--: "+testError+ "\n");                
                
            }finally{
                
                lockResult.unlock();               
            }
            return false;
        }
         //return true;
     }
*/

}

    
