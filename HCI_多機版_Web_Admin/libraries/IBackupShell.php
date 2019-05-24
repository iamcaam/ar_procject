<?php
include_once("/var/www/html/libraries/BackupShell.php");
include_once("/var/www/html/libraries/BackupShellTest.php");
interface IBackupShell
{
	public function backupSeedTaskShell($input);   
	public function restoreSameShell($input,$isSeed);
	public function restoreNewShell($input,$isSeed);
	public function backupUserDiskTaskShell($input,$type,&$rtn);
	public function restoreSameUPShell($input);
	public function restoreNewUPShell($input,&$rtn);
}