/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer_setup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author william
 */
public class InstallCheck extends Thread {
    /* 系統判斷 */
    public boolean OS            = false;
    public boolean ISWIN7        = false;
    public boolean ISSP1         = false;    
    public boolean IsKB          = false;      
    
//    @Override
//    public void run(){
//        try {
//            checkOS();
//            checkISWIN7();
//            checkISSP1();
//            checkIsKB();
//        } catch (IOException ex) {
//            Logger.getLogger(InstallCheck.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }        
    
    public void checkOS() throws IOException{  // 判斷 32bit或是64bit      
        String command = "wmic os get osarchitecture | findstr \"64\"";
        Process Info = Runtime.getRuntime().exec(new String[]{"cmd","/c",command}); // "cmd","/c" "/bin/sh","-c"
        BufferedReader reader = new BufferedReader(new InputStreamReader(Info.getInputStream()));
        String returnResult;
        while ((returnResult = reader.readLine()) != null) {
            if(returnResult.contains("64")==true){
                OS = true;
//                System.out.println("----- OS returnBoolean:" + OS + "-----\n");
            }            
        }
    }      
    
    public void checkISWIN7() throws IOException{        
        String command = "wmic os get version | findstr \"6.1.7600\"";
        Process Info = Runtime.getRuntime().exec(new String[]{"cmd","/c",command}); // "cmd","/c" "/bin/sh","-c"
        BufferedReader reader = new BufferedReader(new InputStreamReader(Info.getInputStream()));
        String returnResult;
        while ((returnResult = reader.readLine()) != null) {
            if(returnResult.contains("6.1.7600")==true){
                ISWIN7 = true;
//                System.out.println("----- returnBoolean:" + ISWIN7 + "-----\n");
            }                    
        }
//        System.out.println("----- ISWIN7 returnBoolean:" + ISWIN7 + "-----\n");
    }    
    
    public void checkISSP1() throws IOException{        
        String command = "wmic os get version | findstr \"6.1.7601\"";
        Process Info = Runtime.getRuntime().exec(new String[]{"cmd","/c",command}); // "cmd","/c" "/bin/sh","-c"
        BufferedReader reader = new BufferedReader(new InputStreamReader(Info.getInputStream()));
        String returnResult;
        while ((returnResult = reader.readLine()) != null) {
            if(returnResult.contains("6.1.7601")==true){
                ISSP1 = true;
//                System.out.println("----- ISSP1 returnBoolean:" + ISSP1 + "-----\n");
            }            
        }
    }    
    
    public void checkIsKB() throws IOException{        
        String command = "wmic qfe | findstr \"KB3033929\"";
        Process Info = Runtime.getRuntime().exec(new String[]{"cmd","/c",command}); // "cmd","/c" "/bin/sh","-c"
        BufferedReader reader = new BufferedReader(new InputStreamReader(Info.getInputStream()));
        String returnResult;
        while ((returnResult = reader.readLine()) != null) {
            if(returnResult.contains("3033929")==true){
                IsKB = true;
//                System.out.println("----- IsKB returnBoolean:" + IsKB + "-----\n");
            }            
        }
    }     
    
    public boolean checkOSVerIsXP() throws IOException{  // 檢查作業系統      
        String command = "ver | findstr /i \"5\\.\""; // https://msdn.microsoft.com/en-us/library/windows/desktop/ms724832(v=vs.85).aspx 查版本表
        Process Info = Runtime.getRuntime().exec(new String[]{"cmd","/c",command}); // "cmd","/c" "/bin/sh","-c" https://stackoverflow.com/questions/13212033/get-windows-version-in-a-batch-file
        BufferedReader reader = new BufferedReader(new InputStreamReader(Info.getInputStream()));
        String returnResult;
        if ((returnResult = reader.readLine()) != null) {
            System.out.println("----- OS為WinXP:"+returnResult+" -----\n");
            return true;
        }
        else {
            System.out.println("----- OS為Win7/8/10 -----\n");
            return false;
        }
    }       
    
    
}
