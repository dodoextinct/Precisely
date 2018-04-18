<?php
    // include  'connect_to_db.php';
    error_reporting(0);

    // $statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS opportunities (ID INT NOT NULL, HEADLINE TEXT, DESCRIPTION TEXT, ELIGIBILITY TEXT, REQUIREMENTS TEXT, BENEFITS TEXT, COST TEXT, DEADLINE DATE, IMAGE TEXT, LINK TEXT, TAGS TEXT, SUBTAGS TEXT, NAME TEXT, NAMELINK TEXT, PRIMARY KEY(ID));");
    // $statement->execute();

    // $stmt = $pdo->prepare("SELECT ID FROM opportunities ORDER BY ID DESC LIMIT 1;");
    // $stmt->execute();
    // $id = $stmt->fetch();

    // if (!isset($id))
    //     $id = 1;
    // else
    //     $id = $id["ID"];
    // echo ($id);

    $sheets_url = "https://docs.google.com/spreadsheets/d/e/2PACX-1vSbOfLmDmhL4h0FFbMiESQpfgpVa4SwNglVC-_ONTsMLxrlwrYSTwmoE0BJgc__UURReHrxauv5Wskh/pub?gid=0&single=true&output=csv";
    $obj = new SplFileObject($sheets_url);
    $obj->setFlags(SplFileObject::READ_CSV);
    $norewind = new NoRewindIterator($obj);
    $caching  = new CachingIterator($norewind);
    $limit    = new LimitIterator($caching, 20);

    function stripslashes_deep($value)
    {
        $value = is_array($value) ?
                    array_map('stripslashes_deep', $value) :
                    stripslashes($value);
        return $value;
    }

    if(!ini_set('default_socket_timeout', 15)) echo "<!-- unable to change socket timeout -->";

    // $spreadsheet_data = array();
    foreach ($limit as $data)
    {
        if (strlen($data[1])==0)
            break;
        // $stmt = $pdo->prepare("INSERT INTO opportunities (ID, HEADLINE, DESCRIPTION, ELIGIBILITY, REQUIREMENTS, BENEFITS, COST, DEADLINE, IMAGE, LINK, TAGS, SUBTAGS, NAME, NAMELINK) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
        print_r($data[7]);
        echo "\n";
        print_r(date_create_from_format("m-d-Y", $data[7]));
        echo "\n";
        print_r(date_create_from_format("m-d-Y", $data[7])->format('Y-m-d'));
        echo "\n\n";
        // $stmt->execute([$data[0], $data[1], $data[2], $data[3], $data[4], $data[5], $data[6], date('Y-m-d', strtotime($data[7])), $data[8], $data[9], $data[10], $data[11], $data[12], $data[13]]);
    }
    // echo json_encode($spreadsheet_data);
?>