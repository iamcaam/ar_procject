<?php 
class SnapshotShellTest implements ISnapshotShell
{
	public function takeSnapshotShell($input,$type)
	{
		$rtn=0;            
        return $rtn;
	}

	function deleteSnapshotShell($input)
    {
        // var_dump($input);
    	$rtn = 0;    	
        return $rtn;
    }

    function rollbackSnapshotShell($input)
    {
        $rtn = 0;            
        return $rtn;
    }

    function takeUPSnapshotShell($input,$type)
    {
        $rtn=0;           
        return $rtn;
    }

    function deleteUPSnapshotShell($input)
    {        
        $rtn = 0;        
        return $rtn;
    }

    function rollbackUPSnapshotShell($input)
    {
        $rtn = 0;        
        return $rtn;
    }

    function directViewSnapshotUPShell($input)
    {
        $rtn = 0;        
        return $rtn;
    }

    function directStopViewUPSnapshotShell($input)
    {
        $rtn = 0;
        return $rtn;
    }
}