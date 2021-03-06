<?php
abstract class APIStatus
{
    const VDSC_CMD_NOT_FOUND = 9000;
    const VDSC_CMD_CONNECT_FAIL = 9001;
    const VDSC_CMD_ERROR = 9002;
    const DBConnectFail = 0;
    const DBPrepareFail = 1;
    const DBConnectSuccess = 2;
    const LoginFail = 3;
    const LoginSuccess = 4;
    const NotFound = 5;
    const Conflict = 6;
    const NoService = 7;
    const ChangePWDSuccess = 8;
    const ChangePWDFail = 9;
    const ChangePWDOldPWDFail = 10;
}

abstract class CPAPIStatus extends APIStatus
{
    //Cluster
    const AddClusterFail = 901;
    const AddClusterSuccess = 902;
    const ListClusterFail = 903;
    const ListClusterSuccess = 904;
    const DeleteClusterFail = 905;
    const DeleteClusterSuccess = 906;
    const ListClusterInfoFail = 907;
    const ListClusterInfoSuccess = 908;
    const AddNodeToClusterFail = 909;
    const AddNodeToClusterSuccess = 910;
    const RemoveNodeFromClusterFail = 911;
    const RemoveNodeFromClusterSuccess = 912;
    const ListClusterFreeFail = 913;
    const ListClusterFreeSuccess = 914;
    const ListClusterPollingFail = 915;
    const ListClusterPollingSucess = 916;
    const SetClusterAlarmFail = 917;
    const SetClusterAlarmSuccess = 918;
    const SetClusterAutosuspendFail = 919;
    const SetClusterAutosuspendSuccess = 920;
    const SetClusterSambaFail = 921;
    const SetClusterSambaSuccess = 922;
    const RebootClusterFail = 923;
    const RebootClusterSuccess = 924;
    const ShutdownClusterFail = 925;
    const ShutdownClusterSuccess = 926;
    const SetClusterDedupFail = 927;
    const SetClusterDedupSuccess = 928;
    const ListClusterDedupFail = 929;
    const ListClusterDedupSuccess = 930;
    const ListClusterVolumesFail = 931;
    const ListClusterVolumesSuccess = 932;
    const AddClusterVolumesFail = 933;
    const AddClusterVolumesSuccess = 934;
    const SetUPSFail = 935;
    const SetUPSSuccess = 936;
    const AddClusterGroupFail = 937;
    const AddClusterGroupSuccess = 938;
    const DeleteClusterGroupFail = 939;
    const DeleteClusterGroupSuccess = 940;
    const ReplaceClusterNodeFail = 941;
    const ReplaceClusterNodeSuccess = 942;
    const SetClusterGroupMasterFail = 943;
    const SetClusterGroupMasterSuccess = 944;
    const ListGlusterSummaryFail = 945;
    const ListGlusterSummarySuccess = 946;
    const clearGlusterFail = 947;
    const clearGlusterSuccess = 948;
    const splitProcessFail = 949;
    const splitProcessSuccess = 949;
    const LocatePairFail = 950;
    const LocatePairSuccess = 951;
    const SkipGlusterWaitGroupFail = 952;
    const SkipGlusterWaitGroupSuccess = 953;
    const ListGlusterNotReadyNodeFail = 954;
    const ListGlusterNotReadyNodeSuccess = 955;
    const ListMailFail = 956;
    const ListMailSuccess = 957;
    const SetMailFail = 958;
    const SetMailSuccess = 959;
    const SendTestMailFail=960;
    const SendTestMailSuccess=961;
    const SetLocalMasterFail=962;
    const SetLocalMasterSuccess=963;
    const ListViewerVersionFail=964;
    const ListViewerVersionSuccess=965;
    const RestoreGlusterFail=966;
    const RestoreGlusterSuccess=967;
    const ListDNSFail=968;
    const ListDNSSuccess=969;
    const SetDNSFail=970;
    const SetDNSSuccess=971;
    const ListVMSIPFail = 972;
    const ListVMSIPSuccess = 973;
    const SetVMSIPFail = 974;
    const SetVMSIPSuccess = 975;
    const ListMiscellaneousFail = 976;
    const ListMiscellaneousSuccess = 977;
    //Server
    const ListServerbyIDFail = 531;
    const ListServerbyIDSuccess = 532;
    const ChangeServerStateFail = 533;
    const ChangeServerStateSuceess = 534;
    const ListNodePollingFail = 535;
    const ListNodePollingSucess = 536;
    const ShutdownNodeFail = 537;
    const ShutdownNodeSuccess = 538;
    const RebootNodeFail = 539;
    const RebootNodeSuccess = 540;
    const UpdateNodeFail = 541;
    const UpdateNodeSuccess = 542;
    const ListNodesForLoginCheckFail = 543;
    const ListNodesForLoginCheckSuccess = 544;
    const ListNodesFail = 545;
    const ListNodesSuccess = 546;
    const ListNodeInfoFail = 547;
    const ListNodeInfoSuccess = 548;
    const CheckNodeFail = 549;
    const CheckNodeSuccess = 550;
    const SetNodeMaintainanceFail = 549;
    const SetNodeMaintainanceSuccess = 550;
    const ListNodesPoweronVDFail = 545;
    const ListNodesPoweronVDSuccess = 546;
    const ReserveNodeFail = 547;
    const ReserveNodeSuccess = 548;
    const SetNodeVMSFail = 549;
    const SetNodeVMSSuccess = 550;
    const SetNodeExternalFail = 551;
    const SetNodeExternalSuccess = 552;
    const SetNodeSSHFail = 553;
    const SetNodeSSHSuccess = 554;
    const ListNodeSSHFail = 555;
    const ListNodeSSHSuccess = 556;
    const ChangeNodeRoleFail = 557;
    const ChangeNodeRoleSuccess = 558;
    const ListUpdateNodeProgressFail = 559;
    const ListUpdateNodeProgressSuccess = 560;
    const LocateNodeFail = 561;
    const LocateNodeSuccess = 562;
    const SetNodeMaintainanReentry = 563;
    //Domain
    const CreateDomainFail=61;
    const CreateDomainSuccess=62;
    const ListDomainFail=63;
    const ListDomainSuccess=64;
    const DeleteDomainFail=65;
    const DeleteDomainSuccess=66;
    const AddDomainADAuthSuccess=67;
    const AddDomainADAuthFail = 68;
    const ListDomainADConfigSuccess = 69;
    const ListDomainADConfigFail = 70;
    const ModifyDomainSuccess = 71;
    const ModifyDomainFail = 72;

    //Resource
    const CreateResourceFail=75;
    const CreateResourceSuccess=76;
    const ListResourceFail=77;
    const ListResourceSuccess=78;
    const DeleteResourceFail=79;
    const DeleteResourceSuccess=80;
    const ListClusterResourceFail = 81;
    const ListClusterResourceSuccess = 82;
    const ListClusterResourcePollingFail = 83;
    const ListClusterResourcePollingSuccess = 84;
    const ModifyResourceFail=85;
    const ModifyResourceSuccess=86;
    const ListAllocatedofResourceFail=87;
    const ListAllocatedofResourceSuccess=88;
    
    //iSCSI
    const DiscoveryFail = 91;
    const DiscoverySuccess = 92;
    
    //User
    const CreateUserFail = 101;
    const CreateUserSuccess = 102;
    const ListUserofDomainFail = 103;
    const ListUserofDomainSuccess = 104;
    const DeleteUserFail = 105;
    const DeleteUserSuccess = 106;
    const DisableUserFail = 107;
    const DisableUserSuccess = 108;
    const CreateUserProfileFail = 109;
    const CreateUserProfileSuccess = 110;
    const DeleteUserProfileFail = 111;
    const DeleteUserProfileSuccess = 112;
    const ListUserInfoFail = 113;
    const ListUserInfoSuccess = 114;
    const ModifyUserProfileFail = 115;
    const ModifyUserProfileSuccess = 116;
    const ModifyUserFail = 117;
    const ModifyUserSuccess = 118;

    //VD
    const CreateVDofUserFail = 131;
    const CreateVDofUserSuccess = 132;
    const DeleteVDFail = 133;
    const DeleteVDSuccess = 134;
    const CreateVDofCephFail = 135;
    const CreateVDofCephSuccess = 136;
    const ListVDofCephFail = 137;
    const ListVDofCephSuccess = 138;
    const ListVDofUserFail = 139;
    const ListVDofUserSuccess = 140;
    const ListISOFail = 141;
    const ListISOSuccess = 142;
    const ModifyISOFail = 143;
    const ModifyISOSuccess = 144;
    const ListVDInfoFail = 145;
    const ListVDInfoSuccess = 146;
    const ListGFSFreeFail = 147;
    const ListGFSFreeSuccess = 148;
    const ModifyVDFail = 149;
    const ModifyVDSuccess = 150;
    const AssignFreeFail = 151;
    const AssignFreeSuccess = 152;
    const AssignUserFail = 153;
    const AssignUserSuccess = 154;
    const CreateSeedFail = 155;
    const CreateSeedSuccess = 156;
    const CloneOrgFail = 157;
    const CloneOrgSuccess = 158;
    const DisableVDFail = 159;
    const DisableVDSuccess = 160;
    const BornVDFail = 161;
    const BornVDSuccess = 162;
    const ExportVDFail = 163;
    const ExportVDSuccess = 164;
    const ImportVDFail = 165;
    const ImportVDSuccess = 166;
    const ListImportFail = 167;
    const ListImportSuccess = 168;
    const RecreateSeedSuccess = 169;
    const RecreateSeedFail = 170;
    const SeedRebornSuccess = 171;
    const SeedRebornFail = 172;
    const PoweronVDSuccess = 173;
    const PoweronVDFail = 174;
    const ResetPasswordSuccess = 175;
    const ResetPasswordFail = 176;
    const AddVDDiskSuccess = 177;
    const AddVDDiskFail = 178;
    const DeleteVDDiskSuccess = 179;
    const DeleteVDDiskFail = 180;
    const AddVDNICSuccess = 181;
    const AddVDNICFail = 182;
    const DeleteVDNICSuccess = 183;
    const DeleteVDNICFail = 184;
    const MigrateVDSuccess = 185;
    const MigrateVDFail = 186;    
    const ModifyVDDiskSuccess = 187;
    const ModifyVDDiskFail = 188;
    const MoveVDDiskSuccess = 189;
    const MoveVDDiskFail = 190;
    const CloneToOrgSuccess = 191;
    const CloneToOrgFail = 192;
    const InsertRDPSuccess = 193;
    const InsertRDPFail = 194;
    const ChangeRDPStatusSuccess = 195;
    const ChangeRDPStatusFail = 196;
    const ListRDPInfoSuccess = 197;
    const ListRDPInfoFail = 198;
    //Task
    const ListTaskFail = 201;
    const ListTaskSuccess = 202;
    const ClearTaskFail = 203;
    const ClearTaskSuccess = 204;
    const CancelTaskFail = 205;
    const CancelTaskSuccess = 206;
    //Update
    const UpdateUploadFail = 231;
    const UpdateUploadSuccess = 232;    
    //Log
    const ListLogFail = 251;
    const ListLogSuccess = 252;
    //VSwitch
    const ListVSwitchFail = 271;
    const ListVSwitchSuccess = 272;
    const SetVSwitchFail = 273;
    const SetVSwitchSuccess = 274;
    const ModifyVSwitchFail = 275;
    const ModifyVSwitchSuccess = 276;
    //Schedule
    const CreateScheduleFail = 291;
    const CreateScheduleSuccess = 292;
    const ModfiyScheduleFail = 293;
    const ModfiyScheduleSuccess = 294;
    const ListScheduleFail = 295;
    const ListScheduleSuccess = 296;
    const DeleteScheduleFail = 297;
    const DeleteScheduleSuccess = 298;
    //Job
    const CreateJobFail = 311;
    const CreateJobSuccess = 312;
    const ListJobDetailFail = 313;
    const ListJobDetailSuccess = 314;
    const DeleteJobFail = 315;
    const DeleteJobSuccess = 316;
    const ModifyJobFail = 317;
    const ModifyJobSuccess = 318;
    //Snapshot
    const TakeSnapshotFail = 351;
    const TakeSnapshotSuccess = 352;
    const DeleteSnapshotFail = 353;
    const DeleteSnapshotSuccess = 354;
    const ListSnapshotFail = 355;
    const ListSnapshotSuccess = 356;
    const ViewSnapshotFail = 357;
    const ViewSnapshotSuccess = 358;
    const StopViewSnapshotFail = 359;
    const StopViewSnapshotSuccess = 360;
    const RollbackSnapshotFail = 361;
    const RollbackSnapshotSuccess = 362;
    //AD
    const ListADOUFail = 381;
    const ListADOUSuccess = 382;
    const ImportADUserFail = 383;
    const ImportADUserSuccess = 384;
    const SetADOUFail = 385;
    const SetADOUSuccess = 386;
    //Ceph
    const CreateCephSuccess = 401;
    const CreateCephFail = 402;
    const ExpansionSuccess = 403;
    const ExpansionFail = 404;
    const InitialDiskSuccess = 405;
    const InitialDiskFail = 406;
    //Backup
    const LUNBrowseSuccess = 451;
    const LUNBrowseFail = 452;
    const SetBackupMappingSuccess = 453;
    const SetBackupMappingFail = 454;
    const DeleteBackupMappingSuccess = 455;
    const DeleteBackupMappingFail = 456;
    const BackupSuccess = 457;
    const BackupFail = 458;
    const RestoreSuccess = 459;
    const RestoreFail = 460;
    const DeleteBackupVDSuccess = 461;
    const DeleteBackupVDFail = 462;
    const ListBackupVDWithSizeSuccess =463;
    const ListBackupVDWithSizeFail =464;
    const FormatBackupSuccess = 465;
    const FormatBackupFail = 466;
    //AcroGateway
    const SetAcroGatewayIPFail = 481;
    const SetAcroGatewayIPSuccess = 482;
    const SetAcroGatewayDHCPFail = 483;
    const SetAcroGatewayDHCPSuccess = 484;
    const SetAcroGatewayForwardPortFail = 485; 
    const SetAcroGatewayForwardPortSuccess = 486;
    const SetAcroGatewayVswitchFail = 487;
    const SetAcroGatewayVswitchSuccess = 488;
}
