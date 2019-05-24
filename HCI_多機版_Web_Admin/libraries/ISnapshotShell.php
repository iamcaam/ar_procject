<?php 
include_once("/var/www/html/libraries/SnapshotShell.php");
include_once("/var/www/html/libraries/SnapshotShellTest.php");
interface ISnapShotShell
{
	public function takeSnapshotShell($input,$type);
	public function deleteSnapshotShell($input);
	public function rollbackSnapshotShell($input);
	public function takeUPSnapshotShell($input,$type);
	public function deleteUPSnapshotShell($input);
	public function rollbackUPSnapshotShell($input);
}