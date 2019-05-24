/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_vdi_viewer_setup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author william
 */
public class InstallCopy extends Thread {
    
    File sourceFile = new File(System.getProperty("user.dir"));
//    File sourceFile = new File("C:\\Users\\victor\\Desktop\\AcroRed_VDI_Viewer_Installation_64bit_V 5.0.17-build1\\AcroRed_VDI_Viewer_64bit_Folder"); // 測試用
    File destFile64 = new File("C:\\AcroRed_AcroViewer_64bit_Folder");   // AcroRed_VDI_Viewer_64bit_Folder
    File destFile32 = new File("C:\\AcroRed_AcroViewer_32bit_Folder");   // AcroRed_VDI_Viewer_32bit_Folder
    File tempFile;
    public boolean Copy_flag = false;
    public GB GB; // 2017.10.27 william 版本判斷
    
    String cmd_copy_64 = "XCOPY . " + destFile64 + " /D/K/E/Y/C/I/H";
    String cmd_copy_32 = "XCOPY . " + destFile32 + " /D/K/E/Y/C/I/H";
    public Process process_copy;
    public int copy_ReturnCode    = -1;
    
    
    @Override
    public void run(){
        GB = new GB();             // 存放全域變數
        try {
            if(GB.Install_ver==0) {
                recursiveDelete(destFile64);
                recursiveDelete(destFile64);
                recursiveDelete(destFile64);
                try {
                    sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(InstallCopy.class.getName()).log(Level.SEVERE, null, ex);
                }                
                copy(sourceFile, destFile64);
                // 2018.06.21 william 安裝方式修改
                String jsonfile_64bit = sourceFile.getParent() + "\\jsonfile";
                String lib_64bit = sourceFile.getParent() + "\\lib";
                String VirtViewer_64bit = sourceFile.getParent() + "\\VirtViewer v3.1_64bit_wusb";
                File sourceFile_jsonfile = new File(jsonfile_64bit);
                File sourceFile_lib = new File(lib_64bit);
                File sourceFile_VirtViewer = new File(VirtViewer_64bit);     
                String jsonfile_dest = destFile64 + "\\jsonfile";
                String lib_dest = destFile64 + "\\lib";
                String VirtViewer_dest = destFile64 + "\\VirtViewer v3.1_64bit_wusb";                
                File destFile_jsonfile = new File(jsonfile_dest);
                File destFile_lib = new File(lib_dest);
                File destFile_VirtViewer = new File(VirtViewer_dest);                 
                copy(sourceFile_jsonfile, destFile_jsonfile);
                copy(sourceFile_lib, destFile_lib);
                copy(sourceFile_VirtViewer, destFile_VirtViewer);                  
//                process_copy =Runtime.getRuntime().exec(new String[]{"cmd","/c", cmd_copy_64});
//                try {
//                    process_copy.waitFor();
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(InstallCopy.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                copy_ReturnCode = process_copy.exitValue();   
            }
            else if (GB.Install_ver==1) {
                recursiveDelete(destFile32);
                recursiveDelete(destFile32);
                recursiveDelete(destFile32);
                try {
                    sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(InstallCopy.class.getName()).log(Level.SEVERE, null, ex);
                }
                copy(sourceFile, destFile32);
                // 2018.06.21 william 安裝方式修改
                String jsonfile_32bit = sourceFile.getParent() + "\\jsonfile";
                String lib_32bit = sourceFile.getParent() + "\\lib";
                String VirtViewer_32bit = sourceFile.getParent() + "\\VirtViewer v3.1_32bit_wousb";
                File sourceFile_jsonfile = new File(jsonfile_32bit);
                File sourceFile_lib = new File(lib_32bit);
                File sourceFile_VirtViewer = new File(VirtViewer_32bit);     
                String jsonfile_dest = destFile32 + "\\jsonfile";
                String lib_dest = destFile32 + "\\lib";
                String VirtViewer_dest = destFile32 + "\\VirtViewer v3.1_32bit_wousb";                
                File destFile_jsonfile = new File(jsonfile_dest);
                File destFile_lib = new File(lib_dest);
                File destFile_VirtViewer = new File(VirtViewer_dest);                 
                copy(sourceFile_jsonfile, destFile_jsonfile);
                copy(sourceFile_lib, destFile_lib);
                copy(sourceFile_VirtViewer, destFile_VirtViewer);                  
//                process_copy =Runtime.getRuntime().exec(new String[]{"cmd","/c", cmd_copy_32});
//                try {
//                    process_copy.waitFor();
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(InstallCopy.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                copy_ReturnCode = process_copy.exitValue();   
            }
        } catch (IOException ex) {
            Logger.getLogger(InstallCopy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }      
    
    // 判斷是否為資料夾，不是就當作檔案複製
    public void copy(File sourceLocation, File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            copyDirectory(sourceLocation, targetLocation);
        } else {
            copyFile(sourceLocation, targetLocation);
        }
    } 
    
    // 複製資料夾
    private void copyDirectory(File source, File target) throws IOException {
        if (!target.exists()) {
            target.mkdir();
        }
//        else {
//            recursiveDelete(target);
//            target.mkdir();
//        }
        source.setReadable(true); // 加這段才能夠傳檔案 https://stackoverflow.com/questions/7093683/error-in-java-java-io-filenotfoundexception-c-users-fssd-desktop-my-test-ac
        for (String f : source.list()) {
            copy(new File(source, f), new File(target, f));            
//            if(f.equals("AcroRed VDI Viewer for Win 10_8_7.exe")) {
//                tempFile = new File(source, f);
//                tempFile.delete();
//            }
        }
        Copy_flag = true;
    }

    // 複製檔案
    private void copyFile(File source, File target) throws IOException {        
        try (
            InputStream in = new FileInputStream(source);
            OutputStream out = new FileOutputStream(target)
        ) {
            byte[] buf = new byte[1024];
            int length;
            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
        }
    }  

    public static void recursiveDelete(File file) {
        //to end the recursive loop
        if (!file.exists())
            return;
         
        //if directory, go inside and call recursively
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                //call recursively
                recursiveDelete(f);
            }
        }
        //call delete to delete files and empty directory
        file.delete();
        System.out.println("Deleted file/folder: "+file.getAbsolutePath());
    }     
    
}
