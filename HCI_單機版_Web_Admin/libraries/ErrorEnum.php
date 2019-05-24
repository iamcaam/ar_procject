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
    const AddClusterFail = 11;
    const AddClusterSuccess = 12;
    const ListClusterFail = 13;
    const ListClusterSuccess = 14;
    const DeleteClusterFail = 15;
    const DeleteClusterSuccess = 16;
    const ListClusterInfoFail = 17;
    const ListClusterInfoSuccess = 18;
    const AddNodeToClusterFail = 19;
    const AddNodeToClusterSuccess = 20;
    const RemoveNodeFromClusterFail = 21;
    const RemoveNodeFromClusterSuccess = 22;
    const ListClusterFreeFail = 23;
    const ListClusterFreeSuccess = 24;
    const ListClusterPollingFail = 25;
    const ListClusterPollingSucess = 26;
    const ListMailFail = 927;
    const ListMailSuccess = 928;
    const SetMailFail = 929;
    const SetMailSuccess = 930;
    const SendTestMailFail = 931;
    const SendTestMailSuccess = 932;
    //Server
    const ListServerbyIDFail = 31;
    const ListServerbyIDSuccess = 32;
    const ChangeServerStateFail = 33;
    const ChangeServerStateSuceess = 34;
    const ListNodePollingFail = 35;
    const ListNodePollingSucess = 36;
    const ShutdownNodeFail = 37;
    const ShutdownNodeSuccess = 38;
    const RebootNodeFail = 39;
    const RebootNodeSuccess = 40;
    const UpdateNodeFail = 41;
    const UpdateNodeSuccess = 42;
    const SetNodeExternalFail = 43;
    const SetNodeExternalSuccess = 44;
    const SetNodeSSHFail = 45;
    const SetNodeSSHSuccess = 46;
    const ListNodeSSHFail = 47;
    const ListNodeSSHSuccess = 48;
    const SetUPSFail = 49;
    const SetUPSSuccess = 50;
    //Domain
    const CreateDomainFail=51;
    const CreateDomainSuccess=52;
    const ListDomainFail=53;
    const ListDomainSuccess=54;
    const DeleteDomainFail=55;
    const DeleteDomainSuccess=56;
    const AddDomainADAuthSuccess=57;
    const AddDomainADAuthFail = 58;
    const ListDomainADConfigSuccess = 59;
    const ListDomainADConfigFail = 60;
    const ModifyDomainSuccess = 61;
    const ModifyDomainFail = 62;

    //Resource
    const CreateResourceFail=71;
    const CreateResourceSuccess=72;
    const ListResourceFail=73;
    const ListResourceSuccess=74;
    const DeleteResourceFail=75;
    const DeleteResourceSuccess=75;
    const ListClusterResourceFail = 76;
    const ListClusterResourceSuccess = 77;
    const ListClusterResourcePollingFail = 78;
    const ListClusterResourcePollingSuccess = 79;
    const ModifyResourceFail=80;
    const ModifyResourceSuccess=81;
    const ListAllocatedofResourceFail=82;
    const ListAllocatedofResourceSuccess=83;
    
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
    const ModifyVDDiskSuccess = 185;
    const ModifyVDDiskFail = 186;
    const MoveVDDiskSuccess = 189;
    const MoveVDDiskFail = 190;
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
    //Ceph
    const CreateCephSuccess = 401;
    const CreateCephFail = 402;
    const ExpansionSuccess = 403;
    const ExpansionFail = 404;
    const ListDedupSuccess = 405;
    const ListDedupFail = 406;
    const SetDedupSuccess = 407;
    const SetDedupFail = 408;
    const InitialDiskSuccess = 409;
    const InitialDiskFail = 410;
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
}
