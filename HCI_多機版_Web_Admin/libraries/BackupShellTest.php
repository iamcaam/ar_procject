<?php
class BackupShellTest implements IBackupShell
{
	public function backupSeedTaskShell($input)
    {
    	return true;
    }

    public function restoreSameShell($input,$isSeed)
    {
    	return true;
    }

    public function restoreNewShell($input,$isSeed)
    {
    	return true;
    }

    public function backupUserDiskTaskShell($input,$type,&$rtn)
    {
    	return true;
    }

    public function restoreSameUPShell($input)
    {
    	return true;
    }

    public function restoreNewUPShell($input,&$rtn)
    {
    	return true;
    }
}