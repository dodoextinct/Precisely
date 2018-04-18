<?php
// $user_id = $POST["id"];
// $page = $_POST["page"];
// $tags = $POST["tags"];
include 'connect_to_db.php';
$page = 0;
$tags = ["Competitions", "Awards"];
$sheets_url = "https://docs.google.com/spreadsheets/d/e/2PACX-1vSbOfLmDmhL4h0FFbMiESQpfgpVa4SwNglVC-_ONTsMLxrlwrYSTwmoE0BJgc__UURReHrxauv5Wskh/pub?gid=0&single=true&output=csv";

function stripslashes_deep($value)
{
    $value = is_array($value) ?
                array_map('stripslashes_deep', $value) :
                stripslashes($value);
    return $value;
}

if(!ini_set('default_socket_timeout', 15)) echo "<!-- unable to change socket timeout -->";

// foreach ($pdo->query("SELECT event_id FROM mauka_api_deleted WHERE user_id = '$user_id'") as $row) {
//     $deleted_events[] = $data;
// }

// && !in_array($data[0], $deleted_events)
if (($handle = fopen($sheets_url, "r")) !== FALSE) {
    while (($data = fgetcsv($handle, 1000, ",")) !== FALSE) {
    	foreach ($tags as $value) {
	    	if (strpos($data[8], $value) !== false) {
	        	$spreadsheet_data[] = $data;
	        	break;
	        }
	    }
	}
    fclose($handle);
    echo json_encode(array_slice($spreadsheet_data, $page*15, ($page+1)*15, true));
}
else
    die("Problem reading csv");
?>