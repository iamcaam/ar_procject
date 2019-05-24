/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.application.Platform;

/**
 *
 * @author root
 */
public class NetworkConnect {

   
    String Connect_IP;
    String ethernet_name_DHCP;
    int signal_strength;
    
    
    /*****Pattern:檢查IP是否符合正確格式*****/
    private static final Pattern PATTERN = Pattern.compile(
        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    
    /***    顯示文字對應表    ***/
    public Map<String, String> WordMap=new HashMap<>();   
    
    
    
    
    public NetworkConnect(Map<String, String> LangMap){
     
        WordMap=LangMap;
       // uniqueKey=Ukey;
       //UUID 需要非同步(需固定 不能改)
    }
    
    public int NetworkConnect() throws SocketException, UnknownHostException, IOException{
        Connect_IP = null;
        ethernet_name_DHCP = null;
        // System.gc();//清除系統垃圾 // 2017.06.05 william System.gc disable
        
        /***Get ethernet name : eth0***/       
//        Process ethernet_name_process = Runtime.getRuntime().exec("ip route show"); //route | grep 'default' | awk '{print $2}'
//        try (BufferedReader output_ethernet = new BufferedReader (new InputStreamReader(ethernet_name_process.getInputStream()))) {
//            String line_ethernet = output_ethernet.readLine();
//            while(line_ethernet != null){  
//
//                if ( line_ethernet.startsWith("default") ==true ){
//
//                    String[] ethernet_Line_list =line_ethernet.split(" ");
//                    ethernet_name_DHCP = ethernet_Line_list[4];              
//                    //System.out.println(" ethernet_name_DHCP  : " + ethernet_name_DHCP +"\n");
//                    break;
//                }
//
//                line_ethernet = output_ethernet.readLine();
//                //System.out.println(" line_ethernet  : " + line_ethernet +"\n");
//            }
//        }

        // 2017.07.11 william remove internet plug & DNS Exception fix
        String command1 = "ip route show";  
        Process Connection_process1 = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",command1});
        BufferedReader output_Connection1 = new BufferedReader (new InputStreamReader(Connection_process1.getInputStream()));
        String line_Connection1 = output_Connection1.readLine();


        // 2017.07.11 william remove internet plug & DNS Exception fix
        String command = "ip link | grep eth | grep DOWN";  
        Process Connection_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",command});
        BufferedReader output_Connection = new BufferedReader (new InputStreamReader(Connection_process.getInputStream()));
        String line_Connection = output_Connection.readLine();
        
        // 2018.04.13 william wifi實作        
        String wifi_command = "ip link | grep wlan | grep DOWN";  
        Process Wifi_Connection_process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",wifi_command});
        BufferedReader output_Wifi_Connection = new BufferedReader (new InputStreamReader(Wifi_Connection_process.getInputStream()));
        String line_wifi_Connection = output_Wifi_Connection.readLine();   
        
        // 2018.04.13 william wifi實作   
        
        Get_ConnecttingWifi_Signal();        
        
        if((line_Connection != null && line_wifi_Connection != null)) {
            return 0; // 都沒有連線
        } else if(line_Connection != null && line_wifi_Connection == null && signal_strength == 4) {
            return 1; // 只有wifi 滿格
        } else if(line_Connection != null && line_wifi_Connection == null && signal_strength == 3) {
            return 4; // 只有wifi 3格
        } else if(line_Connection != null && line_wifi_Connection == null && signal_strength == 2) {
            return 5; // 只有wifi 2格
        } else if(line_Connection != null && line_wifi_Connection == null && signal_strength == 1) {
            return 6; // 只有wifi 1格
        } else if(line_Connection == null && line_wifi_Connection != null) {
            return 2; // 只有網路      
        } else {
            if(line_Connection1 != null)
                return 3; // 有網路有wifi
            else
                return 0; 
        }       
        
        
//        if(line_Connection != null && line_wifi_Connection != null) {
//            return false;
//        } else {
//            return true;
//        }
        
    
        
//        try (BufferedReader output_ethernet = new BufferedReader (new InputStreamReader(ethernet_name_process.getInputStream()))) {
//            String line_ethernet = output_ethernet.readLine();
//            while(line_ethernet != null){  
//
//                String[] ethernet_Line_list =line_ethernet.split(" ");
//                String ethIndex = "eth";
//                int index = -1;
//                
//                for (int i=0;i<ethernet_Line_list.length;i++) {
//                    if (ethernet_Line_list[i].contains(ethIndex)) {
//                        index = i;
//                        break;
//                    }
//                }
//                ethernet_name_DHCP = ethernet_Line_list[index]; 
//
//                line_ethernet = output_ethernet.readLine();
//                //System.out.println(" line_ethernet  : " + line_ethernet +"\n");
//            }
//        }
//        
//        if(ethernet_name_DHCP==null ){
//            Connect_IP = null;
//        }
//        
//        if( ethernet_name_DHCP != null ){
//           
//            NetworkInterface Connect_ni = NetworkInterface.getByName(ethernet_name_DHCP);
//            Enumeration<InetAddress> inetAddresses =  Connect_ni.getInetAddresses();
//
//            while(inetAddresses.hasMoreElements()) {
//
//                InetAddress Connect_ia = inetAddresses.nextElement();
//                if(!Connect_ia.isLinkLocalAddress()) {
//
//                    Connect_IP = Connect_ia.getHostAddress() ;
//                    //System.out.println("Connect_IP : " + Connect_IP +"\n"); 
//                }
//            }        
//        }
        
     
        
    }
    
    // 2018.04.13 william wifi實作 
    public void Get_ConnecttingWifi_Signal() throws IOException {
        String get_Connecting_wifi_Quality = "";
        String line_Detect_wifi            = "";
        Process Detect_wifi_Process = Runtime.getRuntime().exec("iwconfig wlan0"); 
        try (BufferedReader output_Detect_wifi = new BufferedReader (new InputStreamReader(Detect_wifi_Process.getInputStream()))) {
            line_Detect_wifi = output_Detect_wifi.readLine();
            String[] Detect_wifi_Line_list = null;
            String[] Signal_Line_list      = null;
            String[] Signal_Line_list2     = null;
             
            while(line_Detect_wifi != null) {  
                
                if (line_Detect_wifi.contains("Link Quality")){
                    Detect_wifi_Line_list = line_Detect_wifi.split(" ");
                    
//                    for(int i = 0; i < Detect_wifi_Line_list.length; i++){
//                        if(i==11)
//                            System.out.println(i + ":" + Detect_wifi_Line_list[i] +"\n");
//                    }                                        
                    String Str_temp_1 = Detect_wifi_Line_list[11];
                    Signal_Line_list  = Str_temp_1.split("=");
                    String Str_temp_2 = Signal_Line_list[1];
                    Signal_Line_list2 = Str_temp_2.split("/");
                    String Str_temp_3 = Signal_Line_list2[0]; 
                    
                    int signal_number = Integer.parseInt(Str_temp_3);
                    
                    if( 52 < signal_number && signal_number <= 70) {
                        signal_strength = 4;
                    } else if(35 < signal_number && signal_number <= 52) {
                        signal_strength = 3;
                    } else if(17 < signal_number && signal_number <= 35) {
                        signal_strength = 2;
                    } else if(signal_number <= 17) {
                        signal_strength = 1;
                    }
                    
                    
//                    System.out.println(" Signal  : " + Str_temp_3 +"\n");
                }
                    
                
                line_Detect_wifi = output_Detect_wifi.readLine();
            }

            
            output_Detect_wifi.close();       
        }
    }      
    
    public boolean Check_Connecting_Status() throws IOException {
        String line_connection_status = "";
        boolean ConnectingFlag = false;                
        Process Detect_connection_status_Process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","nmcli d status | grep wlan[0-9] | awk '{print $3}'"});

        try (BufferedReader output_connection_status = new BufferedReader (new InputStreamReader(Detect_connection_status_Process.getInputStream()))) {
            line_connection_status = output_connection_status.readLine();
//            System.out.println("line_connection_status: " + line_connection_status);
            
            if(line_connection_status != null) {
                if (line_connection_status.contains("connecting")) {
                    ConnectingFlag = true;
                } else if (line_connection_status.contains("disconnected")) {
                    ConnectingFlag = false;
                } else if (line_connection_status.contains("connected") && !line_connection_status.contains("disconnected")) {
                    ConnectingFlag = false;
                }            
            } else {
                ConnectingFlag = false;
            }


            output_connection_status.close();
        }    
        return ConnectingFlag;        
    }
    
    
}
