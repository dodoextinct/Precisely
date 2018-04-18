<?php
    // include  'connect_to_db.php';
error_reporting(0);
    $user_id = $_POST["id"];
    $page = $_POST["page"];
    $tags = $_POST["tags"];
    $tags = json_decode($tags, TRUE);

    $sheets_url = "https://docs.google.com/spreadsheets/d/e/2PACX-1vSbOfLmDmhL4h0FFbMiESQpfgpVa4SwNglVC-_ONTsMLxrlwrYSTwmoE0BJgc__UURReHrxauv5Wskh/pub?gid=0&single=true&output=csv";

    function stripslashes_deep($value)
    {
        $value = is_array($value) ?
                    array_map('stripslashes_deep', $value) :
                    stripslashes($value);
        return $value;
    }

    if(!ini_set('default_socket_timeout', 15)) echo "<!-- unable to change socket timeout -->";


$file = new SplFileObject($sheets_url);
// $file->setFlags(SplFileObject::READ_CSV);
// foreach ($file as $row) {
//     print_r($row);
// }

// foreach ($file as $line_num => $line) {
//     echo "Line $line_num is $line";
// }

$obj      = new SplFileObject($sheets_url);
$obj->setFlags(SplFileObject::READ_CSV);
$norewind = new NoRewindIterator($obj);
$caching  = new CachingIterator($norewind);
$limit    = new LimitIterator($caching, 68, 10);

foreach ($limit as $i)
{
    $spreadsheet_data[] = stripslashes_deep($i);
}
echo json_encode($spreadsheet_data);

    // if (($handle = fopen($sheets_url, "r")) !== FALSE) {
    //     while (($data = fgetcsv($handle, ",")) !== FALSE) {
    //         if (count($data)>7){
    //             // print_r($data);
    //         	foreach ($tags as $value) {
    //     	    	if (strpos($data[10], $value) !== false) {
    //     	        	$spreadsheet_data[] = stripslashes_deep($data);
    //     	        	break;
    //     	        }
    //     	    }
    //         }
    // 	}
    //     fclose($handle);
    //     echo json_encode(array_values(array_slice($spreadsheet_data, $page*15, ($page+1)*15, true)));
    // }
    // else
    //     die("");
?>