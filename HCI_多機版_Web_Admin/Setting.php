<?php
include_once("/var/www/html/libraries/BaseAPI.php");
if(isset($_GET['mode'])){
	$cmd = CmdRoot.'127.0.0.1 fw_sys_set_mode ';	
	switch ($_GET['mode']){
		case 'release':			
			$cmd .= 'release';
			exec($cmd,$outputArr,$rtn);
			break;
		case 'develop':
			$cmd .= 'develop';
			// var_dump($cmd);
			exec($cmd,$outputArr,$rtn);
			break;		
	}
	header('Location: Setting.php');
}
$cmd = CmdRoot."127.0.0.1 fw_sys_list_version";
// var_dump($cmd);
exec($cmd,$outputArr,$rtn);
$versionList = json_decode($outputArr[0],true);
?>
<!DOCTYPE html>
<head>
	<style type="text/css">
		.lab
		{
			font-size: 16px
		}
		.labHead
		{
			display: inline-block;
			width: 200px			
		}
		input[type="button"]
		{
			font-size: 16px
		}
	</style>	
</head>
<body>
	<label class="labHead lab">Mode : </label>
	<label class="lab"><?php echo $versionList['mode']?></label>
	<br>
	<label class="labHead">Now Firmware Version : </label>
	<label class="lab"><?php echo $versionList['hostVer']?></label>
	<br>
	<label class="labHead">Delevelop Server IP : </label>
	<label class="lab"><?php echo $versionList['developIp']?></label>
	<br>
	<label class="labHead">Delevelop Version : </label>
	<label class="lab"><?php echo $versionList['developVer']?></label>
	<br>
	<label class="labHead">Release Server IP : </label>
	<label class="lab"><?php echo $versionList['releaseIp']?></label>
	<br>
	<label class="labHead">Release Version : </label>
	<label class="lab"><?php echo $versionList['releaseVer']?></label>
	<br>	
	<?php
		if($versionList['mode'] == 'release')
			echo '<input type="button" value="Chanager to Develop Mode" onclick="window.location=\'Setting.php?mode=develop\'">';
		else
			echo '<input type="button" value="Change to Release Mode" onclick="window.location=\'Setting.php?mode=release\'">';		
	?>
</body>