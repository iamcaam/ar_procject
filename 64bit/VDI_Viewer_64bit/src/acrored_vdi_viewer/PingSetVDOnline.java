/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import static acrored_vdi_viewer.CPLogin.IPAddress;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author victor
 */
public class PingSetVDOnline extends Thread{
    
    public Map<String, String> WordMap=new HashMap<>();
    public boolean testError;
    private Thread PSVD;
    private InetAddress IPAddr;
    
    public static int IPPort; // 2017.08.10 william IP增加port欄位，預設443
    
    public PingSetVDOnline(Map<String, String> LangMap){
       
        WordMap=LangMap;
       // uniqueKey=Ukey;
       //UUID 需要非同步(需固定 不能改)
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
    
    public void PingSetVD(String ipaddr,String port) throws InterruptedException, IOException{   // 2017.08.10 william IP增加port欄位，預設443 
        setIP(ipaddr);        
        setPort(port); // 2017.08.10 william IP增加port欄位，預設443   
        doPingSetVD();
        PSVD=new PingSetVDOnline(WordMap);
        PSVD.start();        
        System.out.println("--Ping--testError--"+testError+" \n");
//        if(!testError){
//            PSVD.notifyAll();      
//        }   
    }
    
    public boolean doPingSetVD() throws UnknownHostException, IOException{      

        if((IPAddress==null)||("".equals(IPAddress))){
            testError=false;
            setResult(testError);
            //System.out.println("IPAddress=null : "+ testError +"\n");   
            return false;
        }
           
//        IPAddr=InetAddress.getByName(IPAddress);
//        
//        if(IPAddr.isReachable(1000)){ 
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
