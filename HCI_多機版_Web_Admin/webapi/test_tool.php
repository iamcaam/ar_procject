<?php
define("SANDEBUG", false);
// define("DEBUG", false);
// ini_set('display_errors', 'On');
include_once("/var/www/html/webapi/api.php");
class CmdTool extends BaseAPI
{
	function __construct()
    {                 
    }

    function insertNodevSwitch($input)
    {
        $vSwitchC = new vSwitchAction();
        $responsecode = $vSwitchC->initInsertVswitchNew($input);
        var_dump($responsecode);
    }    

    function adTest($input)
    {
        $adC = new ADAction();
        $adC->mydapListUsersOfOUs($input,$outputOUs);
        var_dump(json_encode($outputOUs));
    }
}
// var_dump($_GET['api']);
$tool = new CmdTool();
$tool->insertNodevSwitch(array('NodeID'=>1,'ConnectIP'=>'192.168.90.61','CommunicateIP'=>'192.168.90.61'));
$tool->insertNodevSwitch(array('NodeID'=>2,'ConnectIP'=>'192.168.88.215','CommunicateIP'=>'192.168.88.215'));
// $sync = array('OU=Domain Controllers,DC=adtest,DC=com','OU=OU1,DC=adtest,DC=com','OU=OU2,DC=adtest,DC=com','OU=OU1分部1,OU=OU1,DC=adtest,DC=com','OU=OU2分部1,OU=OU2,DC=adtest,DC=com','OU=OU1分部1支部1,OU=OU1分部1,OU=OU1,DC=adtest,DC=com','OU=OU_EN,OU=OU1,DC=adtest,DC=com');
// $sync = array('OU=Domain Controllers,DC=adtest,DC=com','OU=OU1,DC=adtest,DC=com','OU=OU2,DC=adtest,DC=com');
// $tool->adTest(array('DomainID'=>1,'Account'=>'administrator','Password'=>'!QAZ2wsx','DomainName'=>'adtest.com','IP'=>'192.168.95.249','SyncOU'=>$sync));