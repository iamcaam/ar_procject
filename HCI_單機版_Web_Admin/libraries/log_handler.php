 <?php
class LogProcess extends BaseAPI
{
    private $TimeZoneArrMapping=array("Pacific/Midway"=>"-11:00",
    "Pacific/Honolulu"=>"-10:00",
    "US/Alaska"=>"-09:00",
    "US/Pacific"=>"-08:00",
    "US/Arizona"=>"-07:00",
    "US/Mountain"=>"-07:00",
    "America/Chihuahua"=>"-07:00",
    "America/Guatemala"=>"-06:00)",
    "US/Central"=>"-06:00",
    "America/Mexico_City"=>"-06:00",
    "Canada/Saskatchewan"=>"-06:00",
    "America/Bogota"=>"-05:00",
    "US/Eastern"=>"-05:00",
    "US/East-Indiana"=>"-05:00",
    "America/Caracas"=>"-04:30",
    "Canada/Atlantic"=>"-04:00",
    "America/La_Paz"=>"-04:00",
    "America/Manaus"=>"-04:00",
    "America/New_York"=>"-04:00",
    "Canada/Newfoundland"=>"-03:30",
    "America/Santiago"=>"-03:00",
    "America/Fortaleza"=>"-03:00",
    "America/Buenos_Aires"=>"-03:00",
    "America/Godthab"=>"-03:00",
    "America/Montevideo"=>"-02:00",
    "Atlantic/South_Georgia"=>"-02:00",
    "Atlantic/Azores"=>"-01:00",
    "Atlantic/Cape_Verde"=>"-01:00",
    "Africa/Casablanca"=>"+00:00",
    "Europe/London"=>"+00:00",
    "Africa/Monrovia"=>"+00:00",
    "Europe/Amsterdam"=>"+01:00",
    "Europe/Belgrade"=>"+01:00",
    "Europe/Brussels"=>"+01:00",
    "Europe/Sarajevo"=>"+01:00",
    "Africa/Kinshasa"=>"+01:00",
    "Africa/Windhoek"=>"+02:00",
    "Asia/Amman"=>"+02:00",
    "Europe/Athens"=>"+02:00",
    "Asia/Beirut"=>"+02:00",
    "Egypt"=>"+02:00",
    "Africa/Harare"=>"+02:00",
    "Europe/Helsinki"=>"+02:00",
    "Israel"=>"+02:00",
    "Africa/Gaborone"=>"+02:00",
    "Europe/Sofia"=>"+02:00",
    "Europe/Minsk"=>"+03:00",
    "Asia/Baghdad"=>"+03:00",
    "Asia/Kuwait"=>"+03:00",
    "Africa/Nairobi"=>"+03:00",
    "Europe/Moscow"=>"+03:00",
    "Iran"=>"+03:30",
    "Asia/Muscat"=>"+04:00",
    "Asia/Baku"=>"+04:00",
    "Asia/Tbilisi"=>"+04:00",
    "Asia/Yerevan"=>"+04:00",
    "Asia/Kabul"=>"+04:30",
    "Asia/Karachi"=>"+05:00",
    "Asia/Yekaterinburg"=>"+05:00",
    "Asia/Calcutta"=>"+05:30",
    "Asia/Kathmandu"=>"+05:45",
    "Asia/Almaty"=>"+06:00",
    "Asia/Dhaka"=>"+06:00",
    "Asia/Novosibirsk"=>"+06:00",
    "Asia/Rangoon"=>"+06:30",
    "Asia/Bangkok"=>"+07:00",
    "Asia/Krasnoyarsk"=>"+07:00",
    "Asia/Taipei"=>"+08:00",
    "Asia/Hong_Kong"=>"+08:00",
    "Asia/Ulaanbaatar"=>"+08:00",
    "Asia/Kuala_Lumpur"=>"+08:00",
    "Australia/Perth"=>"+08:00",
    "Asia/Irkutsk"=>"+08:00",
    "Asia/Tokyo"=>"+09:00",
    "Asia/Seoul"=>"+09:00",
    "Asia/Yakutsk"=>"+09:00",
    "Australia/Darwin"=>"+09:30",
    "Australia/Brisbane"=>"+10:00",
    "Pacifi/Guam"=>"+10:00",
    "Asia/Vladivostok"=>"+10:00",
    "Asia/Magadan"=>"+10:00",
    "Australia/Adelaide"=>"+10:30",
    "Australia/Tasmania"=>"+11:00",
    "Australia/Melbourne"=>"+11:00",
    "Pacific/Noumea"=>"+11:00",
    "Pacific/Auckland"=>"+12:00",
    "Pacific/Fiji"=>"+12:00");
    protected $per_page_count = 1000;    
    private $dbh;
    function LogProcess(){
        if($this->dbh == null){
            $dbc = new ConnectDB();
            $this->dbh = $dbc->Connect('VDI');            
        }
    }
    function Log_List($json_data){  
        if($this->dbh){
            $exu_str = "sudo /var/www/html/date.sh get tz";
            $timezone = exec($exu_str,$outputarr);         
            $gmt = $this->TimeZoneArrMapping[$timezone] == null ? "+08:00" : $this->TimeZoneArrMapping[$timezone];            
            $sqlSetTimeZone = <<<SQL
                    set time_zone=:timezone;
SQL;
            $condition = '';                
            $bindtype = false;
            $bindlevel = false;
            if($json_data['Type'] != 0 || $json_data[Level] != 0){
                $condition .= 'WHERE';
                if($json_data['Type'] != 0 && $json_data['Level'] != 0 ){
                    $bindtype = true;
                    $bindlevel = true;
                    $condition .= ' typeLog = :type AND levelLog = :level';
                }
                else{
                    if($json_data['Type'] != 0){
                        $bindtype = true;
                        $condition .= ' typeLog = :type';
                    }
                    else{
                        $bindlevel = true;
                        $condition .= ' levelLog = :level';
                    }
                }
            }        
            $field = 'timeCreate';        
            //0:levelLog,1:typeLog,2:timeCreate,3:sourceIP,4:riser,5:codeLog
            switch($json_data['Field']){
                case 0:
                    $field = 'levelLog';
                    break;
                case 1:
                    $field = 'typeLog';
                    break;
                case 2:
                    $field = 'timeCreate';
                    break;
                case 3:
                    $field = 'sourceIP';
                    break;
                case 4:
                    $field = 'riser';
                    break;
                case 5:
                    $field = 'codeLog';
            }          
            $ascordesc = 'ASC';
            //0:DESC,1:ASC
            if($json_data['ASC'] == 0)
                $ascordesc = 'DESC';    
            $SQLQueryLogCount = "SELECT COUNT(idLog) FROM tbVDILog $condition";
            $SQLQueryLogContent = "SELECT * FROM tbVDILog $condition ORDER BY $field $ascordesc LIMIT :page_count OFFSET :offset;";                
            try
            {
                $count = 0;
                $sth = $this->dbh->prepare($sqlSetTimeZone);
                $sth->bindValue(':timezone', $gmt, PDO::PARAM_STR);
                $sth->execute(); 
                $sth = $this->dbh->prepare($SQLQueryLogCount);        
                if($bindtype){
                    $sth->bindValue(':type', $json_data['Type'],PDO::PARAM_INT);
                }
                if($bindlevel){
                    $sth->bindValue(':level', $json_data['Level'],PDO::PARAM_INT);
                }            
                $sth->execute();           
                while($row = $sth->fetch()){
                    $count = (int)$row['COUNT(idLog)'];
                }
                $page = $json_data['Page'];
                $totalpage =1;
                if($count > 0){
                    $totalpage = (int)($count /$this->per_page_count);
                    if($count%$this->per_page_count != 0)
                        $totalpage +=1;
                }            
                if($page != 1 && $count < (($json_data['Page']-1)*$this->per_page_count+1)){
                    $page = 1;
                }            
                $sth = $this->dbh->prepare($SQLQueryLogContent);            
                $page_count = $this->per_page_count;
                $sth->bindValue(':page_count', $page_count,PDO::PARAM_INT);
                $sth->bindValue(':offset', ($json_data['Page']-1)*$page_count,PDO::PARAM_INT); 
                if($bindtype){
                    $sth->bindValue(':type', $json_data['Type'],PDO::PARAM_INT);
                }
                if($bindlevel){
                    $sth->bindValue(':level', $json_data['Level'],PDO::PARAM_INT);
                }         
                $sth->execute(); 
                $display_count = 0;
                $logs = array();
                while($row = $sth->fetch()){
                    $logs[] = array("Level"=>(int)$row['levelLog'],"Type"=>(int)$row['typeLog']
                            ,"Riser"=>$row['riser'],"Time"=>$row['timeCreate']
                            ,"SourceIP"=>$row['sourceIP'],"Message"=>$row['message']
                            ,"Code"=>$row['codeLog']);
                    $display_count++;                
                }
                echo json_encode(array("TotalPage"=>$totalpage,"NowPage"=>$page,"TotalCount"=>$count,"DisplayCount"=>$display_count,"Logs"=>$logs,"PageCount"=>$this->per_page_count));
            }
            catch (Exception $e){

            }
                    }
        else{
            echo json_encode(array("TotalPage"=>1,"NowPage"=>1,"TotalCount"=>0,"DisplayCount"=>0,"Logs"=>array(),"PageCount"=>$this->per_page_count));
        }
        $this->dbh = null;
   }
   
   function Insert_Log($typeLog,$levelLog,$riser,$sourceIP,$message,$codeLog){
        if($sourceIP == -1){
            if (!empty($_SERVER['HTTP_CLIENT_IP'])) {
                $sourceIP = $_SERVER['HTTP_CLIENT_IP'];
            } elseif (!empty($_SERVER['HTTP_X_FORWARDED_FOR'])) {
                $sourceIP = $_SERVER['HTTP_X_FORWARDED_FOR'];
            } else {
                $sourceIP = $_SERVER['REMOTE_ADDR'];
            }
        }    
        $exu_str = "sudo /var/www/html/log.sh $typeLog $levelLog \"$sourceIP\" \"\" \"$codeLog\" \"$message\" \"$riser\"";            
        exec($exu_str,$outputarr);       
       
//        if($this->dbh){
//                    
//       $sqlInsertLog = <<<SQL
//            CALL insert_log(:MaxRow,:typeLog,:levelLog,:sourceIP,"",:codeLog,:message,:riser);
//SQL;
//            try{                
//                $sth = $this->dbh->prepare($sqlInsertLog);        
//                $sth->bindValue(':MaxRow', $this->MaxRow,PDO::PARAM_INT);
//                $sth->bindValue(':typeLog', $typeLog,PDO::PARAM_INT);
//                $sth->bindValue(':levelLog', $levelLog,PDO::PARAM_INT); 
//                $sth->bindValue(':sourceIP', $sourceIP,PDO::PARAM_STR); 
//                $sth->bindValue(':codeLog', $codeLog,PDO::PARAM_STR); 
//                $sth->bindValue(':message', $message,PDO::PARAM_STR); 
//                $sth->bindValue(':riser',$riser,PDO::PARAM_STR);       
//                $sth->execute();
//            }
//            catch (Exception $e){
//
//            }
//        }
        $this->dbh = null;
   }
   
   function OutputDownloadFile()
   {                
        if($this->connectDB()){
            $exu_str = "sudo /var/www/html/date.sh get tz";
            $timezone = exec($exu_str,$outputarr);         
            $gmt = $this->TimeZoneArrMapping[$timezone] == null ? "+08:00" : $this->TimeZoneArrMapping[$timezone];            
            $sqlSetTimeZone = <<<SQL
                set time_zone=:timezone;
SQL;
            $SQLQueryLogCount = "SELECT COUNT(idLog) FROM tblogbaseset";
            $SQLQueryLogContent = "SELECT * FROM tblogbaseset ORDER BY timeCreate DESC LIMIT :page_count OFFSET :offset;";                
            
            set_time_limit(0);

            $mime_type="application/force-download";


            // required for IE, otherwise Content-Disposition may be ignored
            if(ini_get('zlib.output_compression'))
            ini_set('zlib.output_compression', 'Off');

            header('Content-Type: ' . $mime_type);
            header("Content-Type: application/octet-stream; text/html; charset=utf-8");
            header("Content-Type: application/download; text/html; charset=utf-8");  
            date_default_timezone_set($timezone);
            $name = 'log_'.date("Y_m_d_His").'.csv';
            if(preg_match('/MSIE/i',  $_SERVER['HTTP_USER_AGENT']))    
            { 
                $encoded_filename = rawurlencode($name);   
                header('Content-Disposition: attachment; filename="'.$encoded_filename.'"');    
            }
            else
                header('Content-Disposition: attachment; filename="'.$name.'"');
            header("Content-Transfer-Encoding: binary");
            header('Accept-Ranges: bytes');

            /* The three lines below basically make the 
                download non-cacheable */
            header("Cache-control: private");
            header('Pragma: private');
            header("Expires: Mon, 26 Jul 1997 05:00:00 GMT");
//            $BOM = "\xEF\xBB\xBF";
            $out = fopen('php://output', 'w');  
//            fwrite($out, $BOM);        
            fputs($out,"\n");             
            fputs($out,'Export Date: '.date('Y/m/d H:i:s')."\n");
            fputs($out,'Host Name: '.gethostname()."\n");
            $exu_str = 'sudo /var/www/html/check_update/list_version.sh';        
            $output = exec($exu_str,$outputarr);
            $json_arr = json_decode($output,true);    
            fputs($out,'Firmware Version: '.$json_arr['Version']."\n"); 
            // fputs($out,'Server IP: '."\n");             
            // $exu_str = 'sudo /var/www/html/ifcfg.sh ip_list';     
            // $output = shell_exec($exu_str);
            // $json_arr = json_decode($output,true);    
            // foreach ($json_arr['managernic'] as $key=>$value){
            //     fputs($out,' IPV4: '.$value['ipv4']."\n");
            // }
            // foreach ($json_arr['iscsinic'] as $key=>$value){
            //     fputs($out,' IPV4: '.$value['ipv4']."\n");
            // }            
            fputs($out,"\n"); 
            fputcsv($out,array('Type','Level','Time','Source IP','User','Content','Event Code'));
            try{
                $this->per_page_count = 200000;
                $sth = connectDB::$dbh->prepare($sqlSetTimeZone);
                $sth->bindValue(':timezone', $gmt, PDO::PARAM_STR);
                $sth->execute();
                $sth = connectDB::$dbh->prepare($SQLQueryLogCount);  
                $sth->execute();           
                while($row = $sth->fetch()){
                    $count = (int)$row['COUNT(idLog)'];
                }                            
                $totalpage =1;
                if($count > 0){
                    $totalpage = (int)($count /$this->per_page_count);
                    if($count%$this->per_page_count != 0)
                        $totalpage +=1;
                }                   
                /* output the file itself */
                for($i=0;$i<$totalpage;$i++){
                    $sth = connectDB::$dbh->prepare($SQLQueryLogContent);            
                    $page_count = $this->per_page_count;
                    $sth->bindValue(':page_count', $page_count,PDO::PARAM_INT);
                    $sth->bindValue(':offset', $i*$page_count,PDO::PARAM_INT);
                    $sth->execute(); 
                    $csv_content = '';
                    while($row = $sth->fetch()){
                        switch ((int)$row['typeLog']){
                            case 6:
                                $LogType = 'System';
                                break;
                            case 2:
                                $LogType = 'Disk';
                                break;
                            case 3:
                                $LogType = 'RAID';
                                break;
                            case 4:
                                $LogType = 'iSCSI';
                                break;
                            case 5:
                                $LogType = 'VD';
                                break;
                            case 8:
                                $LogType = 'Schedule';
                                break;
                            case 10:
                                $LogType = 'Snapshot';
                                break;
                            case 12:
                                $LogType = 'Backup';
                                break;
                        }
                        switch ((int)$row['levelLog']){
                            case 1:
                                $LogLevel = 'Information';
                                break;
                            case 2:
                                $LogLevel = 'Warning';
                                break;
                            case 3:
                                $LogLevel = 'Error';
                                break;                            
                        }
                        $phpdate = strtotime( $row['timeCreate'] );
                        $mysqldate = date( 'Y/m/d H:i:s', $phpdate );
                        $csv_content.="$LogType,$LogLevel,\"$mysqldate\",".$row['sourceIP'].",".$row['riser'].',"'.$row['message'].'",'.$row['codeLog']."\n";
//                        fputcsv($out, array($LogType
//                                    ,$LogLevel                        
//                                    ,$mysqldate
//                                    ,$row['sourceIP']
//                                    ,$row['riser']
//                                    ,$row['message']
//                                    ,$row['codeLog']
//                                    ));
                    }
                    fputs($out,$csv_content);
                }
            }
            catch (Exception $e){
                
            }                    
            fclose($out);
                  
            die();
        }
    }
}
?>
