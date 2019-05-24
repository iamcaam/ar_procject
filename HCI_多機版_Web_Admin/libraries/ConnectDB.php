<?php
define('connect_ip',"localhost");
define('connect_port',"3306");
define('connect_user','root');
// define('connect_pwd','#rH,*tDzLy$'); 
define('connect_pwd',''); 
class ConnectDB
{    
    public static $dbh;
    public static function connect()
    {
        if(self::$dbh == null){              
            self::$dbh = self::connectPDO('VDICeph');
        } 
    } 

    public static function connectPDO($dbname)
    {  
        $pdo = null;        
        try
        {          
            $pdo = new PDO( 
            'mysql:host='.connect_ip.';dbname='.$dbname.';port='.connect_port, 
            connect_user, 
            connect_pwd, 
            array(PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES utf8")); 
            
        }
        catch (PDOException $ex)
        {
            $pdo = null;
            // echo "Error!: " . $ex->getMessage();
        }        
        return $pdo;
    }
}
?>
