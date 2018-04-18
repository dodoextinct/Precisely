<?php
    include  'connect_to_db.php';
    error_reporting(0);

    $user_id = $_POST["id"];
    $op_id = $_POST["page"];
    $tags = $_POST["tags"];
    $tags = json_decode($tags, TRUE);

    $statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_deleted (id INT AUTO_INCREMENT, user_id INT NOT NULL, event_id INT NOT NULL, PRIMARY KEY(id));");
    $statement->execute();

    $sheets_url = "https://docs.google.com/spreadsheets/d/e/2PACX-1vSbOfLmDmhL4h0FFbMiESQpfgpVa4SwNglVC-_ONTsMLxrlwrYSTwmoE0BJgc__UURReHrxauv5Wskh/pub?gid=0&single=true&output=csv";
    $obj = new SplFileObject($sheets_url);
    $obj->setFlags(SplFileObject::READ_CSV);
    $norewind = new NoRewindIterator($obj);
    $caching  = new CachingIterator($norewind);
    // $limit    = new LimitIterator($caching, ($page*15)+1, 30);

    function stripslashes_deep($value)
    {
        $value = is_array($value) ?
                    array_map('stripslashes_deep', $value) :
                    stripslashes($value);
        return $value;
    }

    if(!ini_set('default_socket_timeout', 15)) echo "<!-- unable to change socket timeout -->";

    $deleted_events = array();
    foreach ($pdo->query("SELECT event_id FROM api_deleted WHERE user_id = '$user_id'") as $row) {
        $deleted_events[] = $row['event_id'];
    }
    if (count($deleted_events) == 0){
        $deleted_events[] = -1;
    }

    $spreadsheet_data = array();
    $i = -1;
    while (count($spreadsheet_data, COUNT_NORMAL) < 15) {
        $i = $i + 1;
        $limit    = new LimitIterator($caching, (($op_id + $i)*15)+1, 30);
        foreach ($limit as $data)
        {
            if (strlen($data[1])==0) {
                $i = -1;
                break;
            }
            foreach ($tags as $value) {
                if (strpos($data[10], $value) !== false && !in_array($data[0], $deleted_events)) {
                    $spreadsheet_data[] = stripslashes_deep($data);
                    break;
                }
            }
        }
        if ($i == -1) {
            break;
        }
    }
    echo json_encode($spreadsheet_data);
?>