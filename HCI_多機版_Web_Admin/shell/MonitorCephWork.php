<?php	
$fileCmd = '/mnt/tmpfs/cephcmd/cmd.json';
while (1) {
	if(file_exists($fileCmd)){
		$content = file_get_contents($fileCmd);
		$json = json_decode($content,true);
		if(isset($json)){
			if(isset($json['Method'])){
				switch ($json['Method']) {
					case 'Create':
						if(isset($json['Duplicate']) && isset($json['Disks']) && isset($json['Hostname']) && isset($json['PoolName'])){
							$cmd = 'cd /var/www/html/webapi;java -jar Ceph_Mgt.jar CreateSNCeph ';
							$cmd .= $json['Hostname'].' ';
							$cmd .= $json['Duplicate'].' ';
							$cmd .= $json['PoolName'].' ';
							foreach ($json['Disks'] as $value) {
								$cmd .= $value.' ';
							}
							$cmd .= ' >/dev/null 2>&1 &';
							var_dump($cmd);
							exec($cmd,$output,$rtn);
							unlink($fileCmd);
						}
						break;
					case 'Delete':
						break;
				}
			}
		}
	}
	sleep(1);
}