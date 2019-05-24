<?php
// connect to ldap server
$ldapconn = ldap_connect("192.168.92.17")  or die("Could not connect to LDAP server.");
$set = ldap_set_option($ldapconn, LDAP_OPT_PROTOCOL_VERSION, 3);

if ($ldapconn) {
if(@ldap_bind($ldapconn,"uid=user01,ou=people,dc=my-domain,dc=com","000000"))
{
        echo "password correct!";
} else {
        echo "wrong password!";
}
}

$ldap_bd = ldap_bind($ldapconn,"cn=admin,dc=my-domain,dc=com","!qAz@wSx");
$result = ldap_search($ldapconn,"ou=people,dc=my-domain,dc=com","(uid=user01)") or die ("Error in query");

$data = ldap_get_entries($ldapconn,$result);

echo $data["count"]. " entries returned\n";

for($i=0; $i<=$data["count"];$i++) {
        for ($j=0;$j<=$data[$i]["count"];$j++) {
                echo $data[$i][$j].": ".$data[$i][$data[$i][$j]][0]."\n<br>";
        }
}

/* 若要 show 出某個欄位 */
echo $data[0]["userpassword"][0] ."<br>";

ldap_close($ldapconn);
?>