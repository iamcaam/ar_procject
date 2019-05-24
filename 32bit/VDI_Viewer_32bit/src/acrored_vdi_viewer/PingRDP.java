/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.concurrent.Semaphore;

/**
 * // 2018.11.11 新增AcroDesk & RDP Protocol 
 * @author root
 */
public class PingRDP { 
            
    public PingRDP(String address, String port, String vdid) {        
        
        try{             
            URL url  = new URL("https://" + address + ":" + port + "/vdi/user/vd_rdp/" + vdid);          
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
            conn.setRequestMethod("GET");                                      
	    conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            conn.setRequestProperty("Connection", "Keep-Alive");               
            conn.setConnectTimeout(20000);                                     
            conn.setReadTimeout(10000);                          
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
            JsonObject Spicedata = (JsonObject)_jsonParser.parse(_data).getAsJsonObject();
            // 2018.12.04 新增VD&RDP狀態判斷
            System.out.println("RDP Status : " + Spicedata.get("State"));
            GB.RDP_Status = Spicedata.get("State").getAsInt();
            JsonArray AddressArray = Spicedata.getAsJsonObject().getAsJsonArray("Address");
            GB._jsonArraySize = AddressArray.size();
            GB._jsonArrayData = AddressArray;
            // 2018.12.12 新增 431NoVGA 和第一次開機
            GB.RDPFirst = Spicedata.get("RDPFirst").getAsInt();  // 2018.18.18 new create & reborn  getAsBoolean ->  getAsInt  
            System.out.println("****** RDP First : " + GB.RDPFirst + " ******");            
            if(Spicedata.get("IsAssignedUserDisk").isJsonNull())
                GB.IsAssignedUserDisk = "NULL";                
            else
                GB.IsAssignedUserDisk = Spicedata.get("IsAssignedUserDisk").getAsString();  // 2018.18.18 new create & reborn
            GB.RDP_Stage_Type = Spicedata.get("Stage").getAsInt();
            System.out.println("****** RDP Stage : " + Spicedata.get("Stage") + " ******");
                                   
            conn.disconnect();
        } catch (Exception e) {}// 2019.01.17 相容性處理
    }     
    
    static class PingRDPIP {
        static Semaphore mutex = new Semaphore(1);
        boolean GetDataLock = false;
        
        public PingRDPIP(JsonArray _jsonArray) {
            System.out.println("------------------Ping RDP------------------\n");
            int totalIps = _jsonArray.size();
            GB.RDP_IpCount1 = _jsonArray.size();
            GB.ConnectionAddress = "";
            GB.ConnectionPort = "";
            for(int i = 0; i < totalIps; i++) {
                // 2018.12.07 RDP ping ip and port
                // String Ip = _jsonArray.get(i).getAsString();                
                JsonObject Sterm = _jsonArray.get(i).getAsJsonObject();

                new Thread(new Runnable() {                
                    @Override
                    public void run() {
                        try {
                            int resultCode = isReachableByTcp(Sterm.get("IP").getAsString() , Integer.parseInt(Sterm.get("Port").getAsString()), 5000) ? 0 : -1; // 2018.12.07 RDP ping ip and port                                                     
                            if(resultCode == 0 && !GetDataLock) {
                                mutex.acquire();
                                GetDataLock = true;
                                GB.ConnectionAddress = Sterm.get("IP").getAsString(); // 2018.12.07 RDP ping ip and port
                                GB.ConnectionPort = Sterm.get("Port").getAsString(); // 2018.12.07 RDP ping ip and port
                                //System.out.println("IpAddress : " + Ip + " Result Code : " + resultCode);
                                mutex.release();                                
                            }     
                            // 2018.12.04 新增VD&RDP狀態判斷
                            if(resultCode == 0 && !GB.Check_RDPPing) {
                                GB.Check_RDPPing = true;
                                GB.RDP_IsPingOK = true;
                            } else if(!GB.Check_RDPPing) {
                                GB.RDP_IsPingOK = false;
                            }                                
                            GB.RDP_IpCount2++;
                        } catch (Exception e) {
                            e.printStackTrace();
                            //System.out.println("IpAddress : " + Ip + " Result Code : -1");
                            if(!GB.Check_RDPPing) {
                                GB.RDP_IsPingOK = false;
                            }  
                            GB.RDP_IpCount2++;
                        }
                    }
                }).start();            
            }
        }
        
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
}
