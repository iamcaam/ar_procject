/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer_setup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author william
 */
public class InstallProcess extends Thread {
    public GB GB; // 2017.10.27 william 版本判斷
    public Process process_install;
    public int ReturnCode         = 0;
    public InputStreamReader isr;
    public BufferedReader br;
    public String Install_path;
    public String line;
    public boolean Install_flag = false;
    /*****************************************/
    private String Desktopshortcut_filepath;
    private String Regedit_filepath;
    private String msu_filepath;
    private String USBClerk_filepath;
    private String USBDk_filepath;
    public int pCDS_ReturnCode    = 0;   
    public int pCRE_ReturnCode    = 0;   
    public int pIMSU_ReturnCode   = 0;   
    public int pIUSBC_ReturnCode  = 0;  
    public int pIUSBDk_ReturnCode = 0;
    public Process process1;
    public Process process2;
    public Process process3;
    public Process process4;
    public Process process5;
    
    File sourceFile64 = new File("C:\\AcroRed_AcroViewer_64bit_Folder\\AcroRed AcroViewer for Win 10_8_7.exe"); // C:\\AcroRed_VDI_Viewer_64bit_Folder\\AcroRed VDI Viewer for Win 10_8_7.exe
    File sourceFile32 = new File("C:\\AcroRed_AcroViewer_32bit_Folder\\AcroRed AcroViewer for Win 10_8_7_XP.exe"); //  C:\\AcroRed_VDI_Viewer_32bit_Folder\\AcroRed VDI Viewer for Win 10_8_7_XP.exe
    
    // System.getProperty("user.home") + "/Desktop";
    File sourceFile = new File(System.getProperty("user.dir"));
    File destFile64 = new File("C:\\AcroRed_AcroViewer_64bit_Folder");   //C:\\AcroRed_VDI_Viewer_64bit_Folder
    File destFile32 = new File("C:\\AcroRed_AcroViewer_32bit_Folder");   //C:\\AcroRed_VDI_Viewer_32bit_Folder
    // 2018.06.21 william 安裝方式修改
    File sourceFile64_forViewer = new File(sourceFile.getParent()+"\\VirtViewer v3.1_64bit_wusb");
    File sourceFile32_forViewer = new File(sourceFile.getParent()+"\\VirtViewer v3.1_32bit_wousb");
    File destFile64_forViewer = new File("C:\\AcroRed_AcroViewer_64bit_Folder\\VirtViewer v3.1_64bit_wusb");   // C:\\AcroRed_VDI_Viewer_64bit_Folder\\VirtViewer v3.1_64bit_wusb
    File destFile32_forViewer = new File("C:\\AcroRed_AcroViewer_32bit_Folder\\VirtViewer v3.1_32bit_wousb");  //    C:\\AcroRed_VDI_Viewer_32bit_Folder\\VirtViewer v3.1_32bit_wousb
    int source_count = 0;
    int dest_count = 0;
    
    String exe;
    String lnk;
    String wd;    
    
    public int stop_count = 0;
    private static final String WINDOWS_DESKTOP = "Desktop";    
    
    Timer delay_timer ;
    
    @Override
    public void run(){
        GB = new GB();             // 存放全域變數
        //source_count = countFilesInDirectory(sourceFile);
        if(GB.Install_ver==0) { // 2018.06.21 william 安裝方式修改
            source_count = countFilesInDirectory(sourceFile64_forViewer);
        } else if(GB.Install_ver==1) {
            source_count = countFilesInDirectory(sourceFile32_forViewer);
        }         
        try {
            CreateRegEdit();
            if(GB.Install_ver==0) {
                InstallMSU();        
                InstallUSBClerk();
                InstallUSBDk();
            }
            CheckFileIsExists(); 
            CheckIsFinish();          
             
        } catch (IOException ex) {
            Logger.getLogger(InstallProcess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(InstallProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public static int countFilesInDirectory(File directory) {
        int count = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                count++;
            }
            if (file.isDirectory()) {
                count += countFilesInDirectory(file);
            }
        }
        
        return count;
    }     
    
    public void Install_VDI_Bat() throws IOException, InterruptedException {
        File f = new File("tool.bat"); 
        if(f.exists()) {
            Install_path = f.getAbsolutePath().toString();
            process_install = Runtime.getRuntime().exec(new String[]{"cmd","/c",Install_path});
//            process_install = Runtime.getRuntime().exec(System.getProperty("user.dir"));

//            process_install = Runtime.getRuntime().exec("cmd /c mode con cp select=1252 &&"+Install_path);
            process_install.waitFor();
            ReturnCode  = process_install.exitValue();            
            isr = new InputStreamReader(process_install.getInputStream());
            br = new BufferedReader(isr);
//            OutputStream out = process_install.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));            
            line=null;
            while ( (line = br.readLine()) != null) {
                System.out.println(line);           
                if(line.contains("end")==true&&ReturnCode==0) {
                    Install_flag = true;
                    System.out.println("** vdi tool 成功執行安裝 ** \n");
                }
            }            

        }
    
    }  
    
    public void CheckFileIsExists() {
	Timer timer_check=new Timer(true);
        TimerTask task_check=new TimerTask(){
            @Override
            public void run() {
                if(GB.Install_ver==0) {
                    if(sourceFile64.exists()&&!sourceFile64.isDirectory()) {
                        timer_check.cancel();
//                    System.out.println("** 檔案複製完成 ** \n");
/*
                        try {
                            CreateDesktopshortcut();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(InstallProcess.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(InstallProcess.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        */
                    }
                    else {
//                        System.out.println("** 檔案複製中 ** \n");
                    }                
                }
                else if(GB.Install_ver==1) {
                    if(sourceFile32.exists()&&!sourceFile32.isDirectory()) {
                        timer_check.cancel();
//                    System.out.println("** 檔案複製完成 ** \n");
/*
                        try {
                            CreateDesktopshortcut();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(InstallProcess.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(InstallProcess.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        */
                    }
                    else {
//                        System.out.println("** 檔案複製中 ** \n");
                    }                 
                }
            }
        };
        timer_check.schedule(task_check, 0,1000); 
    }    

    public static String getWindowsCurrentUserDesktopPath() {
        return System.getenv("userprofile") + "\\" + WINDOWS_DESKTOP ;
    }    
    
    public void CreateDesktopshortcut() throws InterruptedException, IOException {
////        File f = new File("bin\\Desktopshortcut.bat"); 
//        File f = new File("C:\\AcroRed\\bin\\Desktopshortcut.bat"); 
//        if(f.exists()) {         
//            System.out.print("建立桌面bat檔案存在 \n");
//            Desktopshortcut_filepath = f.getAbsolutePath().toString();
//            try {  
//                process1 = Runtime.getRuntime().exec(new String[]{"cmd","/c", Desktopshortcut_filepath}); // wusa.exe "+f+" /quiet /norestart
//                process1.waitFor();
//                pCDS_ReturnCode  = process1.exitValue();
//                if(pCDS_ReturnCode==0) {                    
//                    System.out.print("建立桌面link完成 " + pCDS_ReturnCode + " \n");
//                }
//                else {
//                }
//                process1 = null;
//            } catch (IOException ex) {
//                Logger.getLogger(AcroRed_VDI_Viewer_Setup.class.getName()).log(Level.SEVERE, null, ex);
//            }                        
//        } else {
//            System.out.print("建立桌面bat檔案不存在 \n");
//        }  
        if(GB.Install_ver==0) {
            exe = "C:\\AcroRed_AcroViewer_64bit_Folder\\AcroRed AcroViewer for Win 10_8_7.exe"; //C:\\AcroRed_VDI_Viewer_64bit_Folder\\AcroRed VDI Viewer for Win 10_8_7.exe
            lnk = "AcroRed AcroViewer for Win 10_8_7"; //AcroRed VDI Viewer for Win 10_8_7
            wd = "C:\\AcroRed_AcroViewer_64bit_Folder\\"; //     C:\\AcroRed_VDI_Viewer_64bit_Folder\\   
        }
        else if(GB.Install_ver==1) {
            exe = "C:\\AcroRed_AcroViewer_32bit_Folder\\AcroRed AcroViewer for Win 10_8_7_XP.exe"; // C:\\AcroRed_VDI_Viewer_32bit_Folder\\AcroRed VDI Viewer for Win 10_8_7_XP.exe
            lnk = "AcroRed AcroViewer for Win 10_8_7_XP";//AcroRed VDI Viewer for Win 10_8_7_XP
            wd = "C:\\AcroRed_AcroViewer_32bit_Folder\\";//    C:\\AcroRed_VDI_Viewer_32bit_Folder\\      
        }
//        String cmd = "mshta VBScript:Execute(\"Set a=CreateObject(\"\"WScript.Shell\"\"):Set b=a.CreateShortcut(a.SpecialFolders(\"\"Desktop\"\") & \"\"\\"+lnk+".lnk\"\"):b.TargetPath=\"\""+exe+"\"\":b.WorkingDirectory=\"\""+wd+"\"\":b.Save:close\") && $objShell = New-Object -comObject Shell.Application && $objDesktop = $objShell.NameSpace(0X0) && $shortcutFilename = \"AcroRed VDI Viewer for Win 10_8_7.lnk\" && $objFolderItem = $objDesktop.ParseName($shortcutFilename) && $objShortcut = $objFolderItem.GetLink  && $objShortcut.SetIconLocation(\"C:\\AcroRed VDI Viewer\\bin\\Logo.ico\") && $objShortcut.Save()";
        if(getOperatingSystem().contains("Windows 10") || getOperatingSystem().contains("Windows 8") || getOperatingSystem().contains("Windows 8.1")) { // 2018.06.21 william 安裝方式修改
            String cp_shortcut = "mklink \"" + getWindowsCurrentUserDesktopPath() + "\\" + lnk + ".exe" + "\" \"" + exe + "\" ";
            
//        FileWriter fw = new FileWriter("users.txt");
//        fw.write(cp_shortcut);
//
//        fw.flush();
//        fw.close();            
            
            Runtime.getRuntime().exec(new String[]{"cmd","/c", cp_shortcut});
        } else {
            String cmd = "mshta VBScript:Execute(\"Set a=CreateObject(\"\"WScript.Shell\"\"):Set b=a.CreateShortcut(a.SpecialFolders(\"\"Desktop\"\") & \"\"\\"+lnk+".lnk\"\"):b.TargetPath=\"\""+exe+"\"\":b.WorkingDirectory=\"\""+wd+"\"\":b.Save:close\")";
            process1 =Runtime.getRuntime().exec(new String[]{"cmd","/c", cmd});
            process1.waitFor();
            pCDS_ReturnCode = process1.exitValue();   
            System.out.print("建立桌面"+pCDS_ReturnCode+"\n");            
//            String path = getWindowsCurrentUserDesktopPath() + "/"+ lnk + ".URL";
//            String logo = sourceFile + "\\"+ "Logo.ico";
//    //        System.out.println("---------------------------------"+ logo);
//            Create(lnk, path, exe, logo);        
        }
        

                
        /*
        if(GB.Install_ver==0) {
            Install_flag = true;
        } else if (GB.Install_ver==1) {
            Timer delay_timer = new Timer();
            TimerTask task_delay = new TimerTask(){
                @Override
                public void run() {       
                    Install_flag = true;
                    delay_timer.cancel();
                }
            };
            if(checkOSVer_XP())
                delay_timer.schedule(task_delay, 60000); //  100000 (100 sec)                                      
            else
                delay_timer.schedule(task_delay, 60000); //  60000 (60 sec)                                                                            
        }
        */                            
    }
    
    public void Create(String name, String where, String target, String icon) throws IOException {
        FileWriter fw = new FileWriter(where);
        fw.write("[InternetShortcut]\n");
        fw.write("URL=" + target + "\n");
        if (!icon.equals("")) {
            fw.write("IconFile=" + icon + "\n");
        }
        fw.flush();
        fw.close();
    }    
     
    public String getOperatingSystem() {
        String os = System.getProperty("os.name");
         System.out.println("Using System Property: " + os);
        return os;
    }      
    
    public boolean checkOSVer_XP() throws IOException{  // 檢查作業系統      
        String command = "ver | findstr /i \"5\\.\""; // https://msdn.microsoft.com/en-us/library/windows/desktop/ms724832(v=vs.85).aspx 查版本表
        Process Info = Runtime.getRuntime().exec(new String[]{"cmd","/c",command}); // "cmd","/c" "/bin/sh","-c" https://stackoverflow.com/questions/13212033/get-windows-version-in-a-batch-file
        BufferedReader reader = new BufferedReader(new InputStreamReader(Info.getInputStream()));
        String returnResult;
        if ((returnResult = reader.readLine()) != null) {
            System.out.println("----- OS is WinXP:"+returnResult+" -----\n");
            return true;
        }
        else {
            System.out.println("----- OS is Win7/8/10 -----\n");
            return false;
        }
    }     
    
    public void CreateRegEdit() throws InterruptedException, IOException {
        
//        File f = new File("bin\\installregedit.bat"); 
//        if(f.exists()) {         
//            System.out.print("登錄檔案存在 \n");
//            Regedit_filepath = f.getAbsolutePath().toString();
//            try {  
//                process2 =Runtime.getRuntime().exec(new String[]{"cmd","/c", Regedit_filepath});
//                process2.waitFor();
//                pCRE_ReturnCode = process2.exitValue();
////                if(Install_ver==1||Install_ver==2){
////                    Installing.setText("安裝完成!");
////                    reboot_flag = true;
////                    public_stage.toFront();
////                    IsShutdownAlert();                
////                }
//                if(pCRE_ReturnCode==0) {                    
//                     System.out.print("登錄檔案安裝完成 " + pCRE_ReturnCode + " \n");
//                }
//                else {           
//                }
//            } catch (IOException ex) {             
//                Logger.getLogger(AcroRed_VDI_Viewer_Setup.class.getName()).log(Level.SEVERE, null, ex);
//            }                        
//        } else {
//            System.out.print("登錄檔案不存在 \n");
//        }        
        String cmd = "regedit /s bin\\KeepAlive.reg";
        process2 =Runtime.getRuntime().exec(new String[]{"cmd","/c", cmd});
        process2.waitFor();
        pCRE_ReturnCode = process2.exitValue();    
        System.out.print("登錄檔案"+pCRE_ReturnCode+"\n");
    }    
        
    public void InstallMSU() throws IOException, InterruptedException {
        
//        File f = new File("bin\\installmsu.bat"); 
////        File f = new File("bin\\Windows6.1-KB3033929-x64.msu"); 
//        if(f.exists()) {         
//            System.out.print("更新套件存在 \n");
//            msu_filepath = f.getAbsolutePath().toString();
//            try {  
//                process3 =Runtime.getRuntime().exec(new String[]{"cmd","/c",msu_filepath});
//                process3.waitFor();
//                pIMSU_ReturnCode = process3.exitValue();
//                if(pIMSU_ReturnCode==0 || pIMSU_ReturnCode==2359302) {                  
//                     System.out.print("更新套件安裝完成 " + pIMSU_ReturnCode + " \n");
//                }
//                else {                    
//                }
//                 process3 = null;
//            } catch (IOException ex) {                
//                Logger.getLogger(AcroRed_VDI_Viewer_Setup.class.getName()).log(Level.SEVERE, null, ex);
//            }                        
//        } else {
//            System.out.print("更新套件不存在 \n");
//        }         
        String cmd = "start /wait bin\\Windows6.1-KB3033929-x64.msu /quiet /norestart";
        process3 =Runtime.getRuntime().exec(new String[]{"cmd","/c",cmd});
        process3.waitFor();
        pIMSU_ReturnCode = process3.exitValue();
        System.out.print("更新套件"+pIMSU_ReturnCode+"\n");
    }
    
    public void InstallUSBClerk() throws InterruptedException, IOException {
        
////        File f = new File("bin\\usbclerk-x64.msi"); 
//        File f = new File("bin\\USBClerk.bat"); 
//        if(f.exists()) {         
//            System.out.print("USBClerk存在 \n");
//            USBClerk_filepath = f.getAbsolutePath().toString();
//            try {  
////                process4 =Runtime.getRuntime().exec("msiexec /i " + USBClerk_filepath + " /passive /qb"); // http://blogforopensource.blogspot.tw/2014/01/run-msi-file-from-java-how-to.html
//                process4 =Runtime.getRuntime().exec(new String[]{"cmd","/c",USBClerk_filepath}); 
//                process4.waitFor();
//                pIUSBC_ReturnCode = process4.exitValue();
//                if(pIUSBC_ReturnCode==0) {                    
//                     System.out.print("USBClerk安裝完成 " + pIUSBC_ReturnCode + " \n");
//                }
//                else {                    
//                }
//                 process4 = null;
//            } catch (IOException ex) {                
//                Logger.getLogger(AcroRed_VDI_Viewer_Setup.class.getName()).log(Level.SEVERE, null, ex);
//            }                        
//        } else {
//            System.out.print("USBClerk不存在 \n");
//        }         
//        String cmd = "start /wait msiexec /i bin\\usbclerk-x64.msi /qn AGREETOLICENSE=\"yes\"";
        String cmd = "start /wait msiexec /i bin\\AcroRed_USBClerk.msi /qn AGREETOLICENSE=\"yes\"";
        process4 =Runtime.getRuntime().exec(new String[]{"cmd","/c",cmd});
        process4.waitFor();
        pIUSBC_ReturnCode = process4.exitValue();
        System.out.print("USBClerk安裝完成"+pIUSBC_ReturnCode+"\n");
    }    
    
    public void InstallUSBDk() throws InterruptedException, IOException {
//        
////        File f = new File("bin\\usbdk_1.0.14_x64.msi"); 
//        File f = new File("bin\\USBDk.bat"); 
//        if(f.exists()) {         
//            System.out.print("USBDk存在 \n");
//            USBDk_filepath = f.getAbsolutePath().toString();
//            try {  
////                process5 =Runtime.getRuntime().exec("msiexec /i " + USBDk_filepath+ " /passive /qb");
//                process5 =Runtime.getRuntime().exec(new String[]{"cmd","/c",USBDk_filepath});
//                process5.waitFor();
//                pIUSBDk_ReturnCode = process5.exitValue();
////                if(pIUSBDk_ReturnCode==0) {
//                    Installing.setText("安裝完成!");
//                    reboot_flag = true;
//                    public_stage.toFront();
//                    IsShutdownAlert();
//                     System.out.print("USBDk安裝完成 " + pIUSBDk_ReturnCode + " \n");
////                }
////                else {                  
////                }
//                 process5 = null;
//            } catch (IOException ex) {              
//                Logger.getLogger(AcroRed_VDI_Viewer_Setup.class.getName()).log(Level.SEVERE, null, ex);
//            }                        
//        } else {
//            System.out.print("USBDk不存在 \n");
//        }         
//        String cmd = "start /wait msiexec /i bin\\usbdk_1.0.14_x64.msi /qn AGREETOLICENSE=\"yes\"";
        String cmd = "start /wait msiexec /i bin\\AcroRed_USBDK.msi /qn AGREETOLICENSE=\"yes\"";
        process5 =Runtime.getRuntime().exec(new String[]{"cmd","/c",cmd});
        process5.waitFor();
        pIUSBDk_ReturnCode = process4.exitValue();
        System.out.print("USBDk安裝完成"+pIUSBDk_ReturnCode+"\n");
    }    
 
    public void StopAll() {
        Install_flag = true;
        delay_timer.cancel();     
        try {
            CreateDesktopshortcut();
        } catch (InterruptedException ex) {
            Logger.getLogger(InstallProcess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InstallProcess.class.getName()).log(Level.SEVERE, null, ex);
        }         
    }
    
   
    
    public void CheckIsFinish() {
            delay_timer = new Timer();
            TimerTask task_delay = new TimerTask(){
                @Override
                public void run() {       
                    stop_count++;
                    if(GB.Install_ver==0) {                        
                        try {
//                            sleep(100);
//                            source_count = countFilesInDirectory(sourceFile);
                            sleep(100);
//                            if(destFile64.exists())
//                                dest_count   = countFilesInDirectory(destFile64);  
                            // 2018.06.21 william 安裝方式修改
                            if(destFile64_forViewer.exists())
                                dest_count   = countFilesInDirectory(destFile64_forViewer); 
                            
                            if (source_count == dest_count) {
                                StopAll();                      
                            } else if(stop_count == 600) {
                                StopAll();
                            }                               
                        } catch (InterruptedException ex) {
                            Logger.getLogger(InstallProcess.class.getName()).log(Level.SEVERE, null, ex);
                        }                                                           
                    } else if (GB.Install_ver==1) {
                        try {
//                            sleep(100);
//                            source_count = countFilesInDirectory(sourceFile);
                            sleep(100);
//                            if(destFile32.exists())
//                                dest_count   = countFilesInDirectory(destFile32);
                            // 2018.06.21 william 安裝方式修改
                            if(destFile32_forViewer.exists())
                                dest_count   = countFilesInDirectory(destFile32_forViewer); 
                            
                            if (source_count == dest_count) {
                                StopAll();                      
                            } else if(stop_count == 600) {
                                StopAll();
                            }                            
                        } catch (InterruptedException ex) {
                            Logger.getLogger(InstallProcess.class.getName()).log(Level.SEVERE, null, ex);
                        }                                                     
                    }                                                            
                }
            };
            delay_timer.schedule(task_delay, 30000, 1000);
    }                
}
