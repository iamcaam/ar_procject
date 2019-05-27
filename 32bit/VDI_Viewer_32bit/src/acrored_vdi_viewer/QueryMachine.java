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
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.util.Base64;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
// 2019.01.23 RDP 連線過程隱藏
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.W32APIOptions;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

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
    //Thread 宣告
    private PingSetVDOnline PSV;
    public int Alert_spiceaddr; 
    
    public String CurrentIP_Port; // 2017.08.10 william IP增加port欄位，預設443
    public static int IPPort; // 2017.08.10 william IP增加port欄位，預設443
    
    private LoginMultiVD LMVD;        // 2018.01.01 william 單一帳號多個VD登入
    private Stage public_stage;     // 2018.01.01 william 單一帳號多個VD登入
    public boolean checkaddr;       // 2018.01.01 william 單一帳號多個VD登入
    public int s;                   // 2018.01.01 william 單一帳號多個VD登入
    public boolean checkLogin_flag; // 2018.01.01 william 單一帳號多個VD登入
    
    private Snapshot ss;              // 2018.01.01 william SnapShot實作
    private boolean Snapshot_flag;  // 2018.01.01 william SnapShot實作     
    public File MultiVD_file;       // 2018.01.19 william  單一帳號多個VD登入
    /*===============================================錯誤修改(3)-清除密碼 for windows===============================================*/
    public boolean IsVdOnline_flage = false;
    /*===============================================錯誤修改(3)-清除密碼 for windows===============================================*/    
    
    // 2018.03.29 william 雙螢幕實作
    URL Monitor_url;   
    HttpURLConnection Monitor_conn;    
    public static String VDid;    
    
    static int select_size = 0; // 2018.11.11 新增AcroDesk & RDP Protocol
    public boolean RejumpProtocol_AcroView = false;
    public static Stage GetStage;     
    
    // 2019.01.02 view D Drive
    String _diskName = "";        
    
    // 2019.01.23 RDP 連線過程隱藏
    public static String TitleName;    
    
    boolean OneVD_SPICE = false;
    public String IsRDPVdOnline;
    boolean connectflag = false;
    
    /*****Pattern:檢查IP是否符合正確格式*****/
    private static final Pattern PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");    
    /***    顯示文字對應表    ***/
    public Map<String, String> WordMap=new HashMap<>();   
    
    @Override
    public void run() {
        Status = "Proceeding";
        String doJob = getJob();        
        switch (doJob) {
            case "login": 
                {
                    try {
                        QueryLogin(public_stage, CurrentIP, CurrentUserName, CurrentPasseard, uniqueKey, CurrentIP_Port, Snapshot_flag);
                    } catch (IOException | InterruptedException ex) {
                        Logger.getLogger(QueryMachine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case "Ping": 
                {
                    try {                 
                        doPing();                      
                    } catch (IOException ex) {
                        Logger.getLogger(QueryMachine.class.getName()).log(Level.SEVERE, null, ex);
                    }         
                }
                break;
            default:
                break;
        }
        Status = "Done";
    }    
    
    public QueryMachine(Map<String, String> LangMap) {
        Status = "Waiting";
        WordMap = LangMap;
        
       /****** bypass SSL 網頁認證問題,系統SSL之Certification key非信任組織發行 *************/
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }
        };
         
        try {             
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());         
        } catch (KeyManagementException | NoSuchAlgorithmException e) {}

         // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            @Override
                public boolean verify(String hostname, SSLSession session) { return true; }    
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
         /***********************************SSL END**************************************/             
    }
         
    public static synchronized String getJob() {
        return Job;
    }
    
    public static synchronized void setJob(String job) {
        Job = job;
    }
    
    public static synchronized String getIP() {        
        return IPAddress;
    }
    
    public static synchronized void setIP(String ip) {      
        IPAddress = ip;
    }
 
    public static synchronized void setPort(String port) {      
        IPPort = Integer.parseInt(port);
    }
    
    public static synchronized String getStatus() {
        return Status;
    }
    
    public static synchronized void setStatus(String status) {
        Status = status;
    }
    
    public static synchronized String getMessages() {
        return Messages;
    }
    
    public static synchronized void setMessages(String messages) {
        Messages = messages;
    }
    
    public synchronized Boolean getResult () {
        return testError;
    }
    
    public synchronized void setResult(Boolean res) {
        testError = res;
    }
     
    public void Reset() {
        lockStatus.lock();
        try {
            setStatus("Waiting");           
        } finally {
            lockStatus.unlock();
        }
        lockMessages.lock();
        try {
            setMessages("");
        } finally {
            lockMessages.unlock();
        }
        lockResult.lock();
        try {
            setResult(testError);
        } finally {
            lockResult.unlock(); 
        }
    }
    
    public String getQMStatus() {
        String tempStatus;
        lockStatus.lock();
        try {
            tempStatus=Status;
        } finally {
            lockStatus.unlock();
        }
        return tempStatus;
    }
    
    public String getQMMessages() {
        String tempMessages;
        lockMessages.lock();
        try {
            tempMessages=Messages;
        } finally {
            lockMessages.unlock();
        }
        return tempMessages;
    }
    
    public boolean getQMResult() {
        Boolean tempResult;
        lockResult.lock();
        try {
            tempResult=testError;
        } finally {
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
    public void QueryLogin(Stage primaryStage, String IPAddr, String Uname, String Password, String UniqueKey, String IPPort, Boolean IsSnapShot) throws MalformedURLException, IOException, InterruptedException { 
        public_stage    = primaryStage;
        setStage(primaryStage);
        URL url;        
        CurrentIP       = IPAddr;      
        CurrentUserName = Uname;       
        CurrentPasseard = Password;    
        uniqueKey       = UniqueKey;
        CurrentIP_Port  = IPPort;      
        Snapshot_flag   = IsSnapShot;  
         
        JSONObject obj = new JSONObject();
        obj.put("AccountName", CurrentUserName);
        obj.put("Password", CurrentPasseard);       
        String query = obj.toString();

       
         /*** POST Start ***/
        try { 
            GB.ExitFlag = false; // 2018.11.11 新增AcroDesk & RDP Protocol
            url = new URL("https://" + CurrentIP + ":" + CurrentIP_Port + "/vdi" + "/user" + "/vd/" + uniqueKey); 
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();                // 建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線         
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");                                                      // 利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種            
	    conn.setRequestProperty("Accept", "application/json");                              // 設置接收數據格式
            conn.setRequestProperty("Content-Type", "application/json");                        // 設置發送數據的格式("Content-Type", "application/json; charset=UTF-8")
            //conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
            conn.setRequestProperty("Content-length", String.valueOf(query.getBytes().length)); // ("Content-length", String.valueOf(query.length()))
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Connection", "Keep-Alive");                                // 維持長連接
            conn.setUseCaches (false);
            conn.setInstanceFollowRedirects(true);               
            conn.setConnectTimeout(20000); // 2017.09.18 william 錯誤修改(1)-IP Port例外處理 / 2018.01.01 單一帳號多個VD登入 (5秒改20秒，不然在getResponseCode容易失敗) // 5 -> 10 -> 20
            conn.setReadTimeout(10000);    // 2017.09.18 william 錯誤修改(1)-IP Port例外處理 / 2018.01.01 單一帳號多個VD登入 (2秒改10秒，不然在getResponseCode容易失敗) // 2 -> 10                
                         
            // 寫入資料     
            try (OutputStream out = conn.getOutputStream()) {
                out.write(query.getBytes());
                out.flush();
                out.close();
            }  catch (IOException ex) { // 2017.09.18 william 錯誤修改(1)-IP Port例外處理
                 System.out.println("IP PORT 輸入錯誤"); 
                 testError = false;
            }

//            System.out.println("URL : " + url + "\n"); 
//            System.out.println("conn.getOutputStream() : " + conn.getOutputStream() + "\n");
//            System.out.println("Resp Code : " + conn.getResponseCode() + "\n");
//            System.out.println("Resp Message:" + conn.getResponseMessage() + "\n");
//            System.out.println("-----------------------------------------\n");
       
            /*********跳出警告訊息:401 404**********/
            try {
                connCode = conn.getResponseCode();
            } catch(Exception e) {
                testError = false;
            }            

             //AlertChangeLang(connCode);       
            /***************************************/
        
            //讀資料
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
             
            String data = sb.toString();             
            JsonParser jsonParser = new JsonParser();
            JsonObject returndata = (JsonObject)jsonParser.parse(data).getAsJsonObject();        
            JsonArray jsonArr = returndata.getAsJsonObject().getAsJsonArray("UserVdImages");
            
            // 2019.01.02 view D Drive
            _diskName = "";
            try{
                _diskName = returndata.get("DiskName").getAsString();
            } catch(Exception e) {}            
            
            if(jsonArr.size()==0) { // 2018.18.18 new create & reborn
                System.out.println("no user");
                Platform.runLater(() -> { 
                    ReLogin(-10);                     
                });         
            }             
            checkaddr = false;                           // 2018.01.01 william 單一帳號多個VD登入
            // gson-JsonArray
            int i = 0;          // 2018.01.01 william 單一帳號多個VD登入            
            s = jsonArr.size(); // 2018.01.01 william 單一帳號多個VD登入
            setSize(s); // 2018.11.11 新增AcroDesk & RDP Protocol
            JsonObject term = jsonArr.get(i).getAsJsonObject();
            
            VdId = term.get("VdId").getAsString();
            setVDID(VdId); // 2018.03.29 william 雙螢幕實作            
            VdServers = term.get("VdServers").toString(); //JsonArray-UserVdImages->VdServers            
            Address = term.getAsJsonArray("VdServers").get(i).getAsJsonObject().get("Address").getAsString(); //JsonArray-UserVdImages->VdServers->Address
            Port = term.getAsJsonArray("VdServers").get(i).getAsJsonObject().get("Port").getAsString();//JsonArray-UserVdImages->VdServers->Port              
            IsVdOnline = term.get("IsVdOnline").toString(); //JsonArray-UserVdImages->IsVdOnline                         
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
            
            System.out.println("VdId: " + VdId + "\n");
            System.out.println("VdServers: " + VdServers + "\n");
            System.out.println("Address: " + Address + "\n");
            System.out.println("Port: " + Port + "\n");        
            System.out.println(" *** IsVdOnline: ***  " + IsVdOnline + "\n");
            System.out.println(" *** IsDefault: ***  " + IsDefault + "\n");            
            /*****跳出視窗詢問 是否還要連線******/
            if("true".equals(IsVdOnline)) {}
            /** 2018.01.01 william SnapShot實作 - VD登入及快照登入 **/
            /*****跳出視窗詢問 可列出全部VD讓使用者選擇******/            
            if(!Snapshot_flag) {
                if ( s > 0 ) { // 2018.11.11 新增AcroDesk & RDP Protocol  1 -> 0      
                    RejumpProtocol_AcroView = false;     
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
                            GB.migrationProtocol = "AcroSpice";
                            DoChcekServerAddressAvailabilityGet(Address,Port);
                            IsVdOnline = term.get("IsVdOnline").toString(); 
                            
                            CountDownLatch runalert = new CountDownLatch(1);
                            
                            if(term.get("IsVdOnline").getAsBoolean()) {
                            Platform.runLater(() -> { 
                                try {
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("VD Online");//設定對話框視窗的標題列文字  , "您真的要結束程式嗎？", ButtonType.YES, ButtonType.NO
                                    //alert.setHeaderText(WordMap.get("Message")+" : "+" VD is Online!! ");//設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
                                    alert.setHeaderText(null);
                                    alert.getDialogPane().setMinSize(500,Region.USE_PREF_SIZE);
                                    alert.getDialogPane().setMaxSize(500,Region.USE_PREF_SIZE);
                                    alert.setContentText(WordMap.get("Message_VD_Online"));//VD 已上線,是否還要連線,若連線,則前一個連線會自動斷線.
                                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
                                    ButtonType buttonTypeCancel = new ButtonType(WordMap.get("Alert_Cancel"),ButtonBar.ButtonData.CANCEL_CLOSE);
                                    alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel); 

                                    Optional<ButtonType> result01 = alert.showAndWait();
                                    if(result01.get().getButtonData().equals(buttonTypeOK.getButtonData())) {
                                        connectflag = true;
                                    } else {
                                        connectflag = false;
                                    }
                                } finally{
                                    runalert.countDown();
                                }
                                });                                                                                      
                                runalert.await();                            
                            } else {
                                connectflag = true;
                            }                               
                            
                            if(connectflag)
                                SetVDOnline(Address, term.get("VdId").getAsString(), term.get("VdName").getAsString(), CurrentPasseard, uniqueKey, Port);
                        } else if (getSize() == 1 && term.get("AvailableProtocol").getAsInt() == 2) {
                            if(GB.winXP_ver) {
                                Platform.runLater(() -> { 
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
                                    alert.setTitle(WordMap.get("WW_Header"));
                                    alert.setHeaderText(null);                
                                    alert.getDialogPane().setMinSize(480,Region.USE_PREF_SIZE);
                                    alert.getDialogPane().setMaxSize(480,Region.USE_PREF_SIZE);             
                                    alert.setContentText(
                                        WordMap.get("WW_Header")+" ： "+WordMap.get("Message_Error_WinXP_RDPFail") 
                                        + "\n"
                                        + "\n" 
                                    );       
                                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Exit"),ButtonBar.ButtonData.OK_DONE);
                                    alert.getButtonTypes().setAll(buttonTypeOK); 
                                    alert.showAndWait();             
                                });                                                                    
                            } else {
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
                                GB.adAuth = adAuth;
                                GB.adDomain = adDomain;                            
                                GB.migrationProtocol = "AcroRDP";
                                DoChcekServerAddressAvailabilityGet(Address,Port);
                                IsRDPVdOnline = term.get("IsRDPOnline").toString(); 
                            CountDownLatch runalert = new CountDownLatch(1);
                            
                            if(term.get("IsRDPOnline").getAsBoolean()) {
                            Platform.runLater(() -> { 
                                try {
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("VD Online");//設定對話框視窗的標題列文字  , "您真的要結束程式嗎？", ButtonType.YES, ButtonType.NO
                                    //alert.setHeaderText(WordMap.get("Message")+" : "+" VD is Online!! ");//設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
                                    alert.setHeaderText(null);
                                    alert.getDialogPane().setMinSize(500,Region.USE_PREF_SIZE);
                                    alert.getDialogPane().setMaxSize(500,Region.USE_PREF_SIZE);
                                    alert.setContentText(WordMap.get("Message_VD_Online"));//VD 已上線,是否還要連線,若連線,則前一個連線會自動斷線.
                                    ButtonType buttonTypeOK = new ButtonType(WordMap.get("Alert_Confirm"),ButtonBar.ButtonData.OK_DONE);
                                    ButtonType buttonTypeCancel = new ButtonType(WordMap.get("Alert_Cancel"),ButtonBar.ButtonData.CANCEL_CLOSE);
                                    alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel); 

                                    Optional<ButtonType> result01 = alert.showAndWait();
                                    if(result01.get().getButtonData().equals(buttonTypeOK.getButtonData())) {
                                        connectflag = true;
                                    } else {
                                        connectflag = false;
                                    }
                                } finally{
                                    runalert.countDown();
                                }
                                });                                                                                      
                                runalert.await();                            
                            } else {
                                connectflag = true;
                            }                                
                                if(GB.RDPStatus == 3)
                                    Platform.runLater(() -> { ReLogin(2); });    
                                else if(connectflag)                                                                                                                       
                                    SetVDRDPOnline(Address, VdId, CurrentUserName, CurrentPasseard, uniqueKey, Port, term.get("IsVdOrg").getAsBoolean(), adAuth, adDomain);
                            }                                                        
                        } else {
                            GB.AvailableProtocol = 3;
                            Platform.runLater(() -> { LMVD = new LoginMultiVD(public_stage, WordMap,jsonArr, Address, CurrentUserName, CurrentPasseard,uniqueKey,CurrentIP_Port); }); // 2017.11.24 william 單一帳號多個VD登入                              
                        }                      
                    } catch (Exception e) {
                        if(getSize() == 1) {
                            OneVD_SPICE = true;
                            GB.AvailableProtocol = 1;
                            SetVDOnline(Address, term.get("VdId").getAsString(), term.get("VdName").getAsString(), CurrentPasseard, uniqueKey, Port);
                        } else {
                            GB.AvailableProtocol = 3;
                            Platform.runLater(() -> { LMVD = new LoginMultiVD(public_stage, WordMap,jsonArr, Address, CurrentUserName, CurrentPasseard,uniqueKey,CurrentIP_Port); }); // 2017.11.24 william 單一帳號多個VD登入                              
                        }                       
                    }                                                          
                } else {           
                    /*** 檢查Server的可及性,HttpCode回覆200 表示即可連線 ***/ 
                    DoChcekServerAddressAvailabilityGet(Address, Port);
                switch (getProtocol()) {
                    case "AcroView":
                        if(ChcekServerAddress_connCode == 200 && checkaddr == false) {  
                            checkaddr = true;
                            if("false".equals(IsVdOnline)) { SetVDOnline(Address,  VdId, CurrentUserName, CurrentPasseard, uniqueKey, CurrentIP_Port); }                
                        } 
                        break;
                    case "RDP":
                        if(ChcekServerAddress_connCode == 200 && checkaddr == false) {  
                            checkaddr = true;
                            //if("false".equals(IsVdOnline)) { SetVDRDPOnline(Address,  VdId, CurrentUserName, CurrentPasseard, uniqueKey, CurrentIP_Port); }  // 2018.12.12 新增 431NoVGA 和第一次開機               
                        }                         
                        break;       
                    default: 
                        break;               
                }
                }
            } else {
                Platform.runLater(() -> { ss = new Snapshot(public_stage, WordMap, jsonArr, Address, CurrentIP_Port, CurrentUserName, CurrentPasseard, uniqueKey, _diskName); }); // 2017.11.24 william SnapShot實作    // 2019.01.02 view D Drive                                                             
            }
            conn.disconnect();     
        } catch (MalformedURLException e) {}                   
    }
    
    /*******GET
     * @param address*
     * @param port
     ******/
    
    public void DoChcekServerAddressAvailabilityGet(String address, String port) throws MalformedURLException, IOException {
        URL url;
        Address = address;
        Port = port;
        try {           
            url = new URL("https://" + Address + ":" + Port + "/vdi/" + "port");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // 建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線
            conn.setRequestMethod("GET");                                      // 利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種
	    conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Connection", "Keep-Alive");               // 維持長連接
            conn.setConnectTimeout(10000);                                     // 2018.01.01 單一帳號多個VD登入 (5秒改10秒，不然在getResponseCode容易失敗)
            conn.connect();
            /*
                            if (conn.getResponseCode() != 200) {
		throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                            }
                            */
            ChcekServerAddress_connCode = conn.getResponseCode();
            System.out.println("-------------------ChcekServerAddressAvailabilityGet----------------------\n");
            System.out.println("URL : " + url + "\n"); 
            System.out.println("ChcekServerAddressAvailabilityGet-Resp Code:" + ChcekServerAddress_connCode + "\n"); 
            System.out.println("ChcekServerAddressAvailabilityGet-Resp Message:" + conn.getResponseMessage() + "\n");
            
            //若ResponseCode!=200,則連至下一組Address及port 重複chack到ResponseCode=200 
            if(conn.getResponseCode() != 200){
                //補程式碼
                //System.out.println("DoChcekServerAddressAvailabilityGet : "+ conn.getResponseCode() +"\n");
            } 
            conn.disconnect();
        } catch (MalformedURLException e) {} catch (IOException e) {}  
    }
    
    public void SetVDOnline(String address, String vdid, String Uname, String Password, String UniqueKey, String port)throws MalformedURLException, IOException, InterruptedException {
        URL url;
        Address = address;
        VdId = vdid;
        CurrentUserName = Uname;
        CurrentPasseard = Password;
        uniqueKey = UniqueKey;
        CurrentIP_Port = port;
        checkLogin_flag = false; // 2018.01.01 william 單一帳號多個VD登入        
        setVDID(vdid); 
        Dual_Screen(address, port); 
        System.out.println("-------------------SetVDOnline----------------------\n");
        JSONObject obj2 =new JSONObject();
        obj2.put("ClientID", uniqueKey); // UUID需有雙引號->將uniqueKey轉為字串toString    
        String ClientID=obj2.toString();
        boolean ReLogin = false; 
     
        try{
            url = new URL("https://" + Address + ":" + CurrentIP_Port + "/vdi/user/vd/" + VdId); //"https://"+CurrentIP+"/vdi/"+"/user/"+"/vd/"+uniqueKey
            HttpURLConnection conn_setVDonline = (HttpURLConnection) url.openConnection();                     // 建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線
            conn_setVDonline.setRequestMethod("PUT");
            conn_setVDonline.setDoOutput(true);
            conn_setVDonline.setDoInput(true);
            conn_setVDonline.setRequestProperty("Accept", "application/json");
            conn_setVDonline.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn_setVDonline.setRequestProperty("Content-Type", "application/json");                           // 設置發送數據的格式("Content-Type", "application/json; charset=UTF-8")
            conn_setVDonline.setRequestProperty("Content-length", String.valueOf(ClientID.getBytes().length)); // ("Content-length", String.valueOf(query.length()))
            conn_setVDonline.setRequestProperty("Connection", "Keep-Alive");                                   // 維持長連接
            //conn.connect();    
            conn_setVDonline.setUseCaches (false);
            conn_setVDonline.setInstanceFollowRedirects(true);
            conn_setVDonline.setConnectTimeout(10000); // 2018.01.01 單一帳號多個VD登入 (5秒改10秒，不然在getResponseCode容易失敗)                        
            
            try (OutputStream out2 = conn_setVDonline.getOutputStream()) {
                out2.write(ClientID.getBytes());
                out2.flush();
                out2.close();
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

                        br2.close();
                    }

                    String Sdata = sb2.toString();             
                    JsonParser jsonSParser = new JsonParser();
                    JsonObject Spicedata = (JsonObject)jsonSParser.parse(Sdata).getAsJsonObject();
                    JsonArray SpiceArr = Spicedata.getAsJsonObject().getAsJsonArray("SpiceServers");
                    //System.out.println("JsonArray SpiceArr: "+SpiceArr + "\n");
                    
                    int Count = 0;
                    //  2018.11.06 william 多執行緒Ping IP
                    new PingIP(SpiceArr);                
                    long startTime = System.currentTimeMillis(); // fetch starting time
                    while(false || (System.currentTimeMillis()-startTime) < 30000) {
                         System.out.println("print 0");
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
                        System.out.println("print 1");
                        if(GB.ExitFlag) { // 2018.11.11 新增AcroDesk & RDP Protocol
                             System.out.println("Spice direct exit");
                            break;
                        }      
                        
                        // 2019.01.30 UserDisk快照顯示異常問題修正
                        if(Count > 10) {
                            GB.ExitFlag = false;
                            break;
                        }                            
                        Count++;
                        TimeUnit.SECONDS.sleep(2);
                        
                        if("".equals(GB.ConnectionAddress)  && "".equals(GB.ConnectionPort)) {
                            new PingIP(SpiceArr);
                        }            
                        System.out.println("print 2");
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
            
                /*
                PSV = new PingSetVDOnline(WordMap); //建立呼叫HTTP GET　POST REST JSON 的 Class
                boolean spiceaddr = false;  
                Alert_spiceaddr = 0;
                if(Alert_spiceaddr == 0) {            
                    if(s == 1) { // 2018.01.01 william 單一帳號多個VD登入
                        for (int i = 0; i < SpiceArr.size(); i++) {
                            JsonObject Sterm = SpiceArr.get(i).getAsJsonObject();
                            SpiceAddress = Sterm.get("Address").getAsString();                            
                            SpicePort = Sterm.get("Port").toString();
                            // 以下三行會判斷各IP的測試結果
                            System.out.println("SpiceAddress: " + SpiceAddress + "\n");
                            System.out.println("SpicePort: " + SpicePort + "\n");                    
                            PSV.PingSetVD(SpiceAddress, SpicePort); 
                            if(PSV.testError == true && spiceaddr == false) {
                                spiceaddr = true;
                                Alert_spiceaddr = 2;
                                WriteSpiceText(SpiceAddress, SpicePort, CurrentUserName, CurrentPasseard);
                                File f = new File("jsonfile/SpiceText.txt");       
                                if(f.exists()) {         
                                    //String[] params = new String [2]; 
                                    params = new String [2];           
                                    //32bit版
                                    params[0] = "VirtViewer v3.1_32bit_wousb\\bin\\remote-viewer.exe";//32bit版
                                    //64bit版
                                    //params[0] = "VirtViewer v3.1_64bit\\bin\\remote-viewer.exe";//64bit版
                                    params[1] = f.toString();                       
                                    process =Runtime.getRuntime().exec(params);  
                                    process=null;
                                    System.gc();//清除系統垃圾
                                } else {
                                    System.out.print("remote-viewer.exe is not existed.\n");
                                    File SpiceFile=new File("jsonfile/SpiceLog.txt");
                                    if(SpiceFile.exists()){
                                        SpiceFile.delete();
                                        SpiceFile = null;
                                    }                                                                                
                                    FileWriter fw = new FileWriter("jsonfile/SpiceLog.txt");
                                    fw.write("SpiceText.txt is not existed");
                                    fw.flush();
                                    fw.close();                                                                        
                                }  
                            }
                        }
                    } else {
                        for (int i = 0; i < SpiceArr.size(); i++) {
                            JsonObject Sterm = SpiceArr.get(i).getAsJsonObject();
                            SpiceAddress = Sterm.get("Address").getAsString();                            
                            SpicePort = Sterm.get("Port").toString();
                            System.out.println("SpiceAddress: " + SpiceAddress + "\n");
                            System.out.println("SpicePort: " + SpicePort + "\n"); 
                            
                            if(isReachableByTcp(SpiceAddress, Integer.parseInt(SpicePort), 1000)) {
                                PSV.PingSetVD(SpiceAddress, SpicePort);  // 2017.08.10 william IP增加port欄位，預設443                   
                                if(PSV.testError == true && spiceaddr == false) {                                    
                                    spiceaddr = true;
                                    Alert_spiceaddr = 2;
                                    WriteSpiceText(SpiceAddress, SpicePort, CurrentUserName, CurrentPasseard);
                                    MultiVD_file = new File("jsonfile/SpiceText.txt");       
                                    if(MultiVD_file.exists()) {         
                                        checkLogin_flag = true; // 2018.01.01 william 單一帳號多個VD登入
                                    } else {
                                        System.out.print("remote-viewer.exe is not existed.\n");                                                                                                                           
                                    } 
                                }
                            }
                        }
                    }                    
                    if(Alert_spiceaddr == 0 && spiceaddr == false) {    
                        Alert_spiceaddr =1;
                        checkLogin_flag = true; // 2018.01.01 william 單一帳號多個VD登入
                    }
                }
                */
                conn_setVDonline.disconnect();      
            }            
        } catch (MalformedURLException e) {} catch (IOException e) {}        
    }
    
    //  2018.11.06 william 多執行緒Ping IP
    public void RunSpice(String address, String port, int s) throws IOException, InterruptedException {
        System.out.println("RunSpice IpAddress : " + address + " Port : " + port);
        
        // 2019.03.28 VM/VD migration reconnect
        if(GB.migrationSPICE)
            s = 0;
        
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
                    spiceaddr = true;
                    Alert_spiceaddr = 2;
                    // GB.RejumpProtocol_AcroView = CurrentUserName + "-" + SpiceAddress + " (1) - Remote Viewer";
                    RejumpProtocol_AcroView = true;
                    WriteSpiceText(SpiceAddress, SpicePort, CurrentUserName, CurrentPasseard);
                    File f = new File("jsonfile/SpiceText.txt");       
                    if(f.exists()) {         
                        //String[] params = new String [2]; 
                        params = new String [2];           
                        //32bit版
                        params[0] = "VirtViewer v3.1_32bit_wousb\\bin\\remote-viewer.exe";//32bit版
                        //64bit版
                        //params[0] = "VirtViewer v3.1_64bit\\bin\\remote-viewer.exe";//64bit版
                        params[1] = f.toString();                       
                        process =Runtime.getRuntime().exec(params);  
                        process=null;
                        System.gc();//清除系統垃圾
                    } else {
                        System.out.print("remote-viewer.exe is not existed.\n");
                        File SpiceFile=new File("jsonfile/SpiceLog.txt");
                        if(SpiceFile.exists()){
                            SpiceFile.delete();
                            SpiceFile = null;
                        }                                                                                
                        FileWriter fw = new FileWriter("jsonfile/SpiceLog.txt");
                        fw.write("SpiceText.txt is not existed");
                        fw.flush();
                        fw.close();                                          
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
                        Alert_spiceaddr = 2;
                        WriteSpiceText(SpiceAddress, SpicePort, CurrentUserName, CurrentPasseard);
                        MultiVD_file = new File("jsonfile/SpiceText.txt");       
                        if(MultiVD_file.exists()) {         
                            checkLogin_flag = true; // 2018.01.01 william 單一帳號多個VD登入
                            
                            if(OneVD_SPICE) {         
                                params = new String [2];           
                                params[0] = "VirtViewer v3.1_32bit_wousb\\bin\\remote-viewer.exe";//32bit版
                                params[1] = MultiVD_file.toString();                       
                                try {  
                                    process =Runtime.getRuntime().exec(params);
                                } catch (IOException ex) {
                                    Logger.getLogger(LoginMultiVD.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                process=null;    
                            }                             
                            
                        } else {
                            System.out.print("remote-viewer.exe is not existed.\n");
                        } 
                    }
                }
            }                    
            if(Alert_spiceaddr == 0 && spiceaddr == false){    
                Alert_spiceaddr = 1;
                checkLogin_flag = true; // 2018.01.01 william 單一帳號多個VD登入
            }
        }    
    }
    
    // 2018.11.11 新增AcroDesk & RDP Protocol 
    // 增加Set VD RDP Online       
    public void SetVDRDPOnline(String address, String vdid, String Uname, String Password, String UniqueKey, String port, boolean IsVdOrg, boolean adAuth, String adDomain)throws MalformedURLException, IOException, InterruptedException{ // 2018.12.12 新增 431NoVGA 和第一次開機
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
                    //if(SetVDonline_connCode == 200 ) {// 2018.12.12 新增 431NoVGA 和第一次開機
                        new PingRDP(address, port, vdid);                      
                    //}  
                    
                    int ResetTimeType = -100;// 2018.18.18 new create & reborn

                    long startTime = System.currentTimeMillis(); // fetch starting time
                    while(false || (System.currentTimeMillis()-startTime) < time_count) {  
                        if(GB.stopconnect) {
                            GB.ExitFlag = true;
                            break;                        
                        }
                        
                        // 2018.12.27  其他中斷連線狀態 - Crash & Migrate
                        if(GB.RDP_Status == 6 || GB.RDP_Status == 7)  {
                            ReLogin = true;
                            break;
                        }                            
                        
                        // 2018.12.12 新增 431NoVGA 和第一次開機
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
                            RunRdp(GB.ConnectionAddress, GB.ConnectionPort, Uname, Password, getSize(), adAuth, adDomain);  // 2018.12.07 RDP ping ip and port                        
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
         
    public void RunRdp(String address, String port, String Uname, String Password, int s, boolean adAuth, String adDomain) throws IOException, InterruptedException {
        System.out.println("RunRDP IpAddress : " + address + " Uname : " + Uname + " Password : " + Password);  
        // 2019.03.28 VM/VD migration reconnect
        GB.VMIP = address;        
        // GB.RejumpProtocol_RDP = "RDP-" + address;

            // 2019.01.07 PCI RDP user name format(@) 
            String[] _StrArray = null;
            if(Uname.contains("@")) {
                _StrArray = Uname.split("@");
                Uname   = _StrArray[0];                               
            }        
        
        
//        PingIP.isReachableByTcp(address, 3389, 3000);
        if(s == 0) {  // 2018.11.11 新增AcroDesk & RDP Protocol  1 -> 0                 
           checkLogin_flag = true; 
            System.out.println("直接登入");  
        } else {
            // 2018.11.11 新增AcroDesk & RDP Protocol       
            String cmdKeyClear = "cmdkey /delete:Domain:target=TERMSRV/" + address;
            Runtime.getRuntime().exec(cmdKeyClear);            
            String userprofile = System.getenv("USERPROFILE");
            String cmdKey0 = "";
            if(GB.winXP_ver) {
                checkLogin_flag = true; 
                cmdKey0 = "cmd /c del " + userprofile + "\\My Documents\\Default.rdp /S /A:H /Q /F";
            } else {
                cmdKey0 = "cmd /c del " + userprofile + "\\Documents\\Default.rdp /S /A:H /Q /F";
            }                
            Runtime.getRuntime().exec(cmdKey0);            
            
            WriteDefaultRDP();
            String FilePath = userprofile + "\\Documents\\Default.rdp";            
            checkLogin_flag = true; 
            sleep(500);
            String cmdKey1 = "cmdkey /generic:" + address + " /user:" + Uname + (adAuth ? "@" + adDomain : "") + " /pass:" + Password; // https://dotblogs.com.tw/dean/2013/07/22/111858
            String cmdKey2 = "mstsc " + FilePath + " /admin /v:" + address + ":" + port + " /f"; 
            Runtime.getRuntime().exec(cmdKey1);   
            // Runtime.getRuntime().exec(cmdKey2);
            
//            WriteMinWindowBatch("start /min " + cmdKey2);
//            sleep(300);
            // 2019.01.23 RDP 連線過程隱藏                                    
            Runtime.getRuntime().exec("reg add \"HKEY_CURRENT_USER\\SOFTWARE\\Microsoft\\Terminal Server Client\\Default\" /f /v \"MRU0\" /t \"REG_SZ\" /d " + address + ":" + port);
            String cmdreg = "reg add \"HKEY_CURRENT_USER\\SOFTWARE\\Microsoft\\Terminal Server Client\\LocalDevices\" /f /v \"" + address + "\" /t \"REG_DWORD\" /d 107";
            Runtime.getRuntime().exec(cmdreg);
            Runtime.getRuntime().exec("reg add \"HKEY_CURRENT_USER\\Software\\Microsoft\\Terminal Server Client\" /v \"AuthenticationLevelOverride\" /t \"REG_DWORD\" /d 0 /f");            
//            File sourceFile = new File(System.getProperty("user.dir"));
//            String _path = sourceFile.toString() + "/jsonfile/32bit.bat";
//            RunBatch(_path);
            Runtime.getRuntime().exec("cmd /c start /min " + cmdKey2);
            MaximizeMstsc(address + ":" + port);                                              
            
//            new Thread(new Runnable() {                
//                @Override
//                public void run() {
//                    while(true) {  
//                          System.out.println(" *** CheckMstsc: ***  " + CheckMstsc());
//                        try {
//                            TimeUnit.SECONDS.sleep(1);
//                        } catch (InterruptedException ex) {
//                            Logger.getLogger(QueryMachine.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }  
//                }
//            }).start();             
            
            
//            Platform.runLater(() -> { getStage().setIconified(true); }); // 2017.11.24 william 單一帳號多個VD登入                                        
            System.out.println("多個VD登入");                                    
        }                                
    }    
    
    // 2019.01.23 RDP 連線過程隱藏
    public void MaximizeMstsc(String _ip) {
        String windowTitle = "";
        String langNumber = GetInstalllanguage ();    
        
        long startTime = System.currentTimeMillis(); // fetch starting time
        while(false || (System.currentTimeMillis()-startTime) < 30000) {
            if(WindowTitle(_ip)) {
                if(langNumber.contains("0404")) {
                    windowTitle = _ip + " - 遠端桌面連線";
                } else if(langNumber.contains("0804")) {
                    windowTitle = _ip + " - 远程桌面连接";
                } else {
                    windowTitle = getWindowTitle() ; //    _ip + " - Remote Desktop Connection"
                }      
                
                User32 user32 = User32.instance;
                HWND hWnd = user32.FindWindow(null, windowTitle); // address + ":" + port +" - 遠端桌面連線", "192.168.92.62:3389 - 遠端桌面連線" , 192.168.92.62:3389 - Remote Desktop Connection
                user32.ShowWindow(hWnd, User32.SW_SHOW);
                user32.SetForegroundWindow(hWnd);                      
                
                break;
            }

            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException ex) { Logger.getLogger(QueryMachine.class.getName()).log(Level.SEVERE, null, ex); }
        }          
    }
    // 2019.01.23 RDP 連線過程隱藏
    public static synchronized String getWindowTitle() {
        return TitleName;
    }    
    // 2019.01.23 RDP 連線過程隱藏
    public static synchronized void setWindowTitle(String _title) {
        TitleName = _title;
    }            
    // 2019.01.23 RDP 連線過程隱藏
    public interface User32 extends W32APIOptions {
 
          User32 instance = (User32) Native.loadLibrary("user32", User32.class, DEFAULT_OPTIONS);
           
         // interface WNDENUMPROC extends StdCallCallback {
         //      boolean callback(Pointer hWnd, Pointer arg);
         //  }
 
         // boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer arg);
             
          boolean ShowWindow(HWND hWnd, int nCmdShow);
          boolean SetForegroundWindow(HWND hWnd);
          HWND FindWindow(String winClass, String title);
           
          int SW_SHOW = 1;
           
    }    
    // 2019.01.23 RDP 連線過程隱藏
    public String GetInstalllanguage () {
        //String cmd = "reg query \"hklm\\system\\controlset001\\control\\nls\\language\" /v Installlanguage";
        String cmd = "for /f \"tokens=3\" %a in ('reg query \"hklm\\system\\controlset001\\control\\nls\\language\" /v Installlanguage 2^>NUL') do echo %a";
        // for /f "tokens=3" %a in ('reg query "hklm\system\controlset001\control\nls\language" /v Installlanguage 2^>NUL') do echo %a
        
        String _language = "";
        try {            
            Process Info =   Runtime.getRuntime().exec(new String[]{"cmd","/c", cmd});
            BufferedReader reader = new BufferedReader(new InputStreamReader(Info.getInputStream()));
            String returnResult;
            while ((returnResult = reader.readLine()) != null) {   
                if(!returnResult.contains("echo") && !returnResult.isEmpty()) { 
                    _language = returnResult;
                    System.out.println("Install language : " + returnResult);                
                }                
            }                     
        } catch (Exception ex) {}    
        return _language;
    }
    // 2019.01.23 RDP 連線過程隱藏    
    public boolean WindowTitle (String ip) {
        // String cmd = "tasklist /v /fi \"IMAGENAME eq mstsc.exe\" | find /i \"" + ip + "\" 2>NUL";
        String cmd = "for /f \"tokens=10,11,12\" %a in ('tasklist /v /fi \"IMAGENAME eq mstsc.exe\" ^| find /i \"" + ip + "\" 2^>NUL') do echo %a %b %c";
        boolean IsGet = false;
        try {            
            Process Info =   Runtime.getRuntime().exec(new String[]{"cmd","/c", cmd});
            BufferedReader reader = new BufferedReader(new InputStreamReader(Info.getInputStream()));
            String returnResult;
            while ((returnResult = reader.readLine()) != null) {
                if(!returnResult.contains("echo") && returnResult.contains(ip)) { 
                    setWindowTitle(returnResult);
                    IsGet = true;
                    System.out.println(" : " + returnResult);                
                }
            }                     
        } catch (Exception ex) {}    
        
        return IsGet;
    }
    
    public void WriteMinWindowBatch(String cmd) {
        try{
            File BatchFile = new File("jsonfile/32bit.bat");
            if(BatchFile.exists()) {
                BatchFile.delete();
                BatchFile = null;
            }
            FileWriter SWriter = new FileWriter("jsonfile/32bit.bat",true);
            try (BufferedWriter bufferedSWriter = new BufferedWriter(SWriter)) {
                bufferedSWriter.write(cmd);
                bufferedSWriter.newLine();              
                bufferedSWriter.flush();
                bufferedSWriter.close();                    
            }
            
        }catch(IOException e){
        }
    }
        
    public void RunBatch(String _path) {
        ProcessBuilder pb = new ProcessBuilder(_path);
        // ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", "START /MIN " + _path);
        pb.redirectError();
        try {
            Process p = pb.start();
//            try (InputStream inputStream = p.getInputStream()) {
//                int in = -1;
//                while ((in = inputStream.read()) != -1) {
//                    System.out.println("-------");
//                    System.out.println((char)in);
//                }
//                 
//            }
            sleep(1000);
            p.destroy();
            System.out.println("Exited with " + p.waitFor());
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
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
                alert.getDialogPane().setMinSize(650,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(650,Region.USE_PREF_SIZE);             
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
                alert.getDialogPane().setMinSize(650,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(650,Region.USE_PREF_SIZE);             
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
                alert.getDialogPane().setMinSize(650,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(650,Region.USE_PREF_SIZE);             
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
                alert.getDialogPane().setMinSize(650,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(650,Region.USE_PREF_SIZE);             
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
                alert.getDialogPane().setMinSize(650,Region.USE_PREF_SIZE);
                alert.getDialogPane().setMaxSize(650,Region.USE_PREF_SIZE);             
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
    
//    private boolean CheckMstsc() {
//        String line_Check = null;
//        Process Check_process = new Process() ;
//        try {
////            String command1 = "tasklist /FI \"IMAGENAME eq mstsc.exe\" 2>NUL | find /I /N \"mstsc.exe\">NUL";  
//            String command2 = "tasklist /FI \"IMAGENAME eq mstsc.exe\" 2>NUL | find /I /N \"mstsc.exe\">NUL && if \"%ERRORLEVEL%\"==\"0\" echo Program is running";
////            Runtime.getRuntime().exec(command1);                        
//            BufferedReader output_Check = new BufferedReader (new InputStreamReader(Check_process.getInputStream()));
//            Check_process = Runtime.getRuntime().exec(command2);
//            
//            line_Check = output_Check.readLine();
//        }
//        catch(IOException e) {}
//        
//        if(line_Check != null) {
//            return true; 
//        } else {
//            return false;
//        }
//    }          
//    
//            
    
    
    /******將Spice需要的資料 寫入txt檔********/
    public void WriteSpiceText(String address, String port, String Uname, String Password){
        // 2019.03.28 VM/VD migration reconnect
        GB.VMIP = address;
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
            //FileWriter SWriter=new FileWriter("SpiceText.txt",true);
            FileWriter SWriter=new FileWriter("jsonfile/SpiceText.txt",true);
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
                // 2018.06.01
                bufferedSWriter.flush();
                bufferedSWriter.close();                    
            }
            
        }catch(IOException e){
        }
    }
    
    public void WriteDefaultRDP() {        
        String userprofile = System.getenv("USERPROFILE");
        String FilePath = userprofile + "\\Documents\\Default.rdp";        
        try{
            File myFile=new File(FilePath);
            if(myFile.exists()){
                myFile.delete();
                myFile = null;
            }
            //FileWriter SWriter=new FileWriter("SpiceText.txt",true);
            FileWriter SWriter=new FileWriter(FilePath, true);
            try (BufferedWriter bufferedSWriter = new BufferedWriter(SWriter)) {
                bufferedSWriter.write("redirectclipboard:i:0");
                bufferedSWriter.newLine();
                bufferedSWriter.write("use multimon:i:1");
                bufferedSWriter.newLine();    
                /*---------- usb redirection ----------*/
//                bufferedSWriter.write("usbdevicestoredirect:s:*");
//                bufferedSWriter.newLine();                 
//                bufferedSWriter.write("devicestoredirect:s:*");
//                bufferedSWriter.newLine();            
//                bufferedSWriter.write("drivestoredirect:s:DynamicDrives");
//                bufferedSWriter.newLine();     
//                bufferedSWriter.write("redirectsmartcards:i:1");
//                bufferedSWriter.newLine();                
//                bufferedSWriter.write("redirectcomports:i:1");
//                bufferedSWriter.newLine();  
                /*---------- usb redirection ----------*/
                bufferedSWriter.flush();
                bufferedSWriter.close();     
            }
            
        }catch(IOException e){
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
           
//        IPAddr=InetAddress.getByName(IPAddress);  // 2017.08.10 william IP增加port欄位，預設443   
//        
//        if(IPAddr.isReachable(1000)){   // 2017.08.10 william IP增加port欄位，預設443   
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
 
    /*===============================================錯誤修改(3)-清除密碼 for windows===============================================*/
    public void CheckUserLogin(String IPAddr,String Uname,String Password,String UniqueKey,String IPPort) throws MalformedURLException, IOException, InterruptedException{ 
        URL url;        
        CurrentIP=IPAddr;
        CurrentUserName=Uname;
        CurrentPasseard=Password;
        uniqueKey=UniqueKey;
        CurrentIP_Port = IPPort; 
         
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
        } 
        catch (KeyManagementException | NoSuchAlgorithmException e){
        }

         // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }    
        };
         HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
         /***********************************SSL END**************************************/
       
         /***POST Start***/
        try {
            url = new URL("https://"+CurrentIP+":"+CurrentIP_Port+"/vdi"+"/user"+"/vd/"+uniqueKey); // 2017.08.10 william IP增加port欄位，預設443           
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();  //建立一個HttpURLConnection物件，並利用URL的openConnection()來建立連線            
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");//利用setRequestMethod("")來設定連線的方式，一般分為POST及GET兩種            
	    conn.setRequestProperty("Accept", "application/json"); //設置接收數據格式
            conn.setRequestProperty("Content-Type", "application/json");//設置發送數據的格式("Content-Type", "application/json; charset=UTF-8")
            conn.setRequestProperty("Content-length", String.valueOf(query.getBytes().length));//("Content-length", String.valueOf(query.length()))
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Connection", "Keep-Alive");//維持長連接
            conn.setUseCaches (false);
            conn.setInstanceFollowRedirects(true);
            
            conn.setConnectTimeout(20000); // 2017.09.18 william 錯誤修改(1)-IP Port例外處理 / 2018.01.01 單一帳號多個VD登入 (5秒改20秒，不然在getResponseCode容易失敗) // 5 -> 10 -> 20
            conn.setReadTimeout(10000);    // 2017.09.18 william 錯誤修改(1)-IP Port例外處理 / 2018.01.01 單一帳號多個VD登入 (2秒改10秒，不然在getResponseCode容易失敗) // 2 -> 10  
           
             
        //寫入資料     
        try (OutputStream out = conn.getOutputStream()) {
            out.write(query.getBytes());
            out.flush();
            out.close();
        } catch (IOException ex) { 
        }
        System.out.println("Resp Code : "+conn.getResponseCode()+"\n");
        if(conn.getResponseCode()==401) {
            IsVdOnline_flage = true;
            return;
        }
       //讀資料
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
             
        String data = sb.toString();             
        JsonParser jsonParser = new JsonParser();
        JsonObject returndata = (JsonObject)jsonParser.parse(data).getAsJsonObject();

        JsonArray jsonArr = returndata.getAsJsonObject().getAsJsonArray("UserVdImages");
        System.out.println("JsonArray jsonArr: "+jsonArr+"\n");
       //gson-JsonArray
        for (int i = 0; i < jsonArr.size(); i++) {
            JsonObject term = jsonArr.get(i).getAsJsonObject();     

            IsVdOnline=term.get("IsVdOnline").toString(); //JsonArray-UserVdImages->IsVdOnline
            System.out.println(" *** 使用者登入狀態 : ***  "+IsVdOnline+"\n"); 
            if("true".equals(IsVdOnline)) { 
                IsVdOnline_flage = true;
            }
        }          
	conn.disconnect();
      
        } catch (MalformedURLException e) {            
	}           
        
    }
    /*===============================================錯誤修改(3)-清除密碼 for windows===============================================*/    
    
    // 2018.03.29 william 雙螢幕實作
    public void  Dual_Screen(String ipaddr,String port) throws MalformedURLException, IOException, InterruptedException {                
        JSONObject jobj = new JSONObject();      
        jobj.put("Monitor", 2);        
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

    // 2018.11.11 新增AcroDesk & RDP Protocol
    public static synchronized int getSize() {
        return select_size;
    }

    public static synchronized void setSize(int _size) {
        select_size = _size;
    }      
        
    public static synchronized Stage getStage() {
        return GetStage;
    }

    public static synchronized void setStage(Stage _stage) {
        GetStage = _stage;
    }      
    
    // 2018.11.05 多執行緒平行Ping IP
    private static class PingWork implements Runnable {
        private static final int TIMEOUT = 5000;
        private InetSocketAddress target;

        private PingWork(InetSocketAddress target) {
            this.target = target;
        }

        @Override
        public void run() {
            Socket connection = new Socket();
            boolean reachable;

            try {
                try {
                    connection.connect(target, TIMEOUT);
                } finally {
                    connection.close();
                }
                reachable = true;
            } catch (Exception e) {
                reachable = false;
            }

            if (!reachable) {
                System.out.println(
                        String.format(
                                "%s:%d was UNREACHABLE",
                                target.getAddress(),
                                target.getPort()
                        )
                );
            }
        }
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

    
