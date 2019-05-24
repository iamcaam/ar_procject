/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

/**
 * 2018.11.06 william 多執行緒Ping IP
 * @author william
 */
public class PingIP {
    static Semaphore mutex = new Semaphore(1);
    boolean GetDataLock = false;

    public PingIP(JsonArray _jsonArray) {
        // https://blog.csdn.net/bairrfhoinn/article/details/16848785
        System.out.println("------------------Ping IP測試------------------\n");
        int totalIps = _jsonArray.size();
        
        // 2019.04.16 Ping IP方式修改
        Executor executor = Executors.newFixedThreadPool(totalIps);
        String[] ipArr = new String[totalIps+1]; 
        String[] portArr = new String[totalIps+1]; 
        ipArr[0] = "0.0.0.0";
        portArr[0] = "0";        
        
        GB.ConnectionAddress = "";
        GB.ConnectionPort = "";
        for(int i = 0; i < totalIps; i++) {
            JsonObject Sterm = _jsonArray.get(i).getAsJsonObject();    
            // 2019.04.16 Ping IP方式修改
            ipArr[i+1] = Sterm.get("Address").getAsString();
            portArr[i+1] = Sterm.get("Port").getAsString();            
            /*
            new Thread(new Runnable() {                
                @Override
                public void run() {
                    try {
                        int resultCode = isReachableByTcp(Sterm.get("Address").getAsString(), Integer.parseInt(Sterm.get("Port").getAsString()), 5000) ? 0 : -1;                            
                        if(resultCode == 0 && !GetDataLock) {
                            mutex.acquire();
                            GetDataLock = true;
                            GB.ConnectionAddress = Sterm.get("Address").getAsString();
                            GB.ConnectionPort = Sterm.get("Port").getAsString();
                            System.out.println("IpAddress : " + Sterm.get("Address").getAsString() + " Port : " + Sterm.get("Port").getAsString() + " Result Code : " + resultCode);
                            mutex.release();
                        }                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("IpAddress : " + Sterm.get("Address").getAsString() + " Port : " + Sterm.get("Port").getAsString() + " Result Code : -1");
                    }
                }
            }).start();
            */
        }    
        
        // 2019.04.16 Ping IP方式修改
        for(int i = 0; i < totalIps; i++) {
            executor.execute(new PingVD(ipArr[i+1], portArr[i+1]));        
            
        }        

        /*
                    ExecutorService executor = Executors.newFixedThreadPool(totalIps);
                    List<Future<PingResult>> list = new ArrayList<Future<PingResult>>();
                    Callable<PingResult> callable = null;
                    for(int i = 0; i < totalIps; i++){
                        JsonObject Sterm = _jsonArray.get(i).getAsJsonObject();     
                        callable = new PingTask(Sterm.get("Address").getAsString(), Sterm.get("Port").getAsString());
                        Future<PingResult> future = executor.submit(callable);
                        list.add(future);
                    }
                    for(Future<PingResult> fut : list){
                        try {
                            System.out.println(new Date()+ "::"+fut.get());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    executor.shutdown();        
            */
    }
    // 2019.04.16 Ping IP方式修改
    public class PingVD implements Runnable {
        private String ipAddress;
        private String port;

        public PingVD(String ipAddress, String port) {
            this.ipAddress = ipAddress;
            this.port = port;
        }
       
        @Override
        public void run() {
            System.out.println("Get IpAddress : " +  ipAddress + " Get Port : " + port);
            try {
                int resultCode = isReachableByTcp(ipAddress, Integer.parseInt(port), 5000) ? 0 : -1;                            
                if(resultCode == 0 && !GetDataLock) {
                    mutex.acquire();
                    GetDataLock = true;
                    GB.ConnectionAddress = ipAddress;
                    GB.ConnectionPort = port;
                    System.out.println("Set IpAddress : " +  GB.ConnectionAddress + " Set Port : " + GB.ConnectionPort);
                    mutex.release();
                }                        
            } catch (Exception e) {
                e.printStackTrace();
                //System.out.println("IpAddress : " + Sterm.get("Address").getAsString() + " Port : " + Sterm.get("Port").getAsString() + " Result Code : -1");
            }
        }
    }        
    
    public class PingTask implements Callable<PingResult> {
        private String ipAddress;
        private String port;

        public PingTask(String ipAddress, String port) {
            this.ipAddress = ipAddress;
            this.port = port;
        }

        @Override
        public PingResult call() {
            InetAddress inet = null;
            try {
                // inet = InetAddress.getByName(ipAddress);
                // int resultCode = inet.isReachable(5000) ? 0 : -1;
                int resultCode = isReachableByTcp(ipAddress, Integer.parseInt(port), 5000) ? 0 : -1;                                              
                return new PingResult(ipAddress, port, resultCode);
            } catch (Exception e) {
                e.printStackTrace();
                return new PingResult(ipAddress, port, -1);
            }
        }
    }    

    public class PingResult {
        private String ipAddress;
        private String port;
        private int resultCode;

        public PingResult(String ipAddress, String port, int resultCode) {
            this.ipAddress = ipAddress;
            this.port = port;
            this.resultCode = resultCode;
        }

        public String getIpAddress() {
            return ipAddress;
        }
        
        public String getPort() {
            return port;
        }        

        public int getResultCode() {
            return resultCode;
        }

        public String toString() {
            return "IpAddress : " + ipAddress + " Port : " + port + " Result Code : " + resultCode;
        }
    }

    public static boolean isReachableByTcp(String host, int port, int timeout) {
        try {
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(host, port);
            socket.connect(socketAddress, timeout);
            socket.close();
//            System.out.print(" PING success");
            return true;
        } catch (IOException e) {
//            System.out.print(" PING fail");
            return false;
        }
    }    

    
}
