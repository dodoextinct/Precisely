<?php
include  'connect_to_db.php';

$sheets_url = "https://docs.google.com/spreadsheets/d/e/2PACX-1vRgXQiKaDfEdiHhQSwIAdtj892QnO6DyHBwzcGytbTZ1YIqINnNemWi0K8-RYBzaeKc3vkDbWbEGxeH/pub?gid=0&single=true&output=csv";

if(!ini_set('default_socket_timeout', 15)) echo "<!-- unable to change socket timeout -->";

if (($handle = fopen($sheets_url, "r")) !== FALSE) {
    while (($data = fgetcsv($handle, ",")) !== FALSE) {
        if (count($data)>0){
        	$spreadsheet_data[] = $data[0];
        	$spreadsheet_data_url[] = $data[1];
        }
	}
    fclose($handle);
    echo json_encode($spreadsheet_data);
    echo json_encode($spreadsheet_data_url);
}else
    die("");
?>