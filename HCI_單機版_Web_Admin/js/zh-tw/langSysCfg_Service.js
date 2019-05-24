var LanguageStr_Service =
{
    Service_New_Password_Empty : "密碼欄不能為空白",
    Service_Confirm_Password_Empty : "確認密碼欄不能為空白",
    Service_Password_Whitespace : "密碼不能輸入空白字元",
    Service_Password_Not_Same : "密碼與確認密碼欄輸入的值必須相同",
    /* SnapShot&Schedule&Backup  2017.01.11 william */   
    Service_SS_Schedule_title : "排程快照之排程設定",
    Service_SS_Schedule_title2 : "排程快照之快照目標設定",
    Service_Schedule_DeleteAsk : "您確定要刪除快照排程嗎?",
    Service_Schedule_JobsAsk : "警告：只能選擇『一個』任務，不能『多』選!",
    Service_Schedule_RuleAsk : "警告：只能選擇『一個』排程，不能『多』選!",

    Service_BK_Schedule_title : "排程備份之排程設定",
    Service_BK_Schedule_title2 : "排程備份之備份目標設定",

    Service_CheckBackupState0 : "未初始化",
    Service_CheckBackupState1 : "未初始化", // 格式化過但無備份過
    Service_CheckBackupState2 : "備份版本不同，初始化才能使用", // 格式化過但備份版本不同
    Service_CheckBackupState3 : "此iSCSI LUN已經被其他超融合主機使用過，請問是否採用?",
    Service_CheckBackupState4 : "",   // 格式化過但備份資料與儲存UUID相同
    Service_CheckBackupState5 : "未初始化", // 格式化過但無備份版本與UUID
    Service_BackupDesc_Empty_Warning : "請在描述字段輸入值",
    Service_BackupAsk : "您確定要備份嗎?",
    Service_RestoreAsk : "您確定要復原嗎?",
    Service_BackupVDDeleteAsk : "您確定要刪除備份端VD嗎?",
    VM_BackupType_backup : "備份",
    VM_BackupType_restorenew : "建立新還原",
    VM_BackupType_restoresame : "還原覆蓋",    
    VM_BackupType_schedulebackup : "排程備份",
    BackupVD_Rename : "VD名稱重複，請重新命名",
    Service_LUN_Connection : "儲存連線設定",
    BackupMgt_Message1 : "尚未備份",
    BackupMgt_Message2 : "尚未還原",
    Backup_FormatAsk : "您確定初始化嗎?",
    CheckBackupLUN_failure : "連線失敗",
    BackupLUN_disconnect : "關閉連線",
    BackupLUN_OnlineCheck : "已連線",
    BackupLUN_OfflineCheck : "已斷線",
    Timepicker_btntext_currentText : "現在",
    Timepicker_btntext_closeText : "完成",
    BackupLUN_OfflineMessage_Warning : "警告 !",    
    BackupMgt_Bktitle : "備份端",
    Backup_ClearAllAsk : "您確定清空備份嗎?",
    Backup_ClearAllComplete : "清空備份完成",
    Backup_SourceVD_Crash : "VD毀損，無法備份",
    Backup_BKVD_Fail : "備份失敗，請重新備份",
    BackupMgt_AllBK_Capacity : "備份端容量 : ",
    BackupMgt_Total_Capacity : "總容量 : ",
    BackupMgt_Avail_Capacity : "可用容量 : ",
    BackupMgt_Used_Capacity : "已使用容量 : ",
    BackupMgt_BK_Capacity : "備份檔案 : ",
    BackupMgt_FS_Capacity : "系統檔案 : ",
    BackupMgt_text_Dot : "。",
    Schedule_Create_Fail : "1. 檢查名稱是否為空白或是重複!\n2. 檢查排程類型及開始日期是否為空白!",
    SelectRAID : "選擇RAID"
}