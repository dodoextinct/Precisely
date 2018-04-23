<?php
    include  'connect_to_db.php';
    error_reporting(0);

    $statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_languages (id INT AUTO_INCREMENT, language VARCHAR(255), PRIMARY KEY(language), KEY(id));");
    $statement->execute();

    $statement = $pdo->prepare("SELECT language FROM api_languages");
    $statement->execute();

    $languages = $statement->fetchAll();

    function stripslashes_deep($value)
    {
        $value = is_array($value) ?
                    array_map('stripslashes_deep', $value) :
                    stripslashes($value);
        return $value;
    }

    foreach($languages as $language){
        $language = $language["language"];
        $table_name = "opportunities_".$language;
        $statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS ".$table_name." (ID INT NOT NULL, HEADLINE TEXT, DESCRIPTION TEXT, ELIGIBILITY TEXT, REQUIREMENTS TEXT, BENEFITS TEXT, COST TEXT, DEADLINE DATE, IMAGE TEXT, LINK TEXT, TAGS TEXT, SUBTAGS TEXT, NAME TEXT, NAMELINK TEXT, PRIMARY KEY(ID)) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;");
        $statement->execute();

        $stmt = $pdo->prepare("SELECT ID FROM ".$table_name." ORDER BY ID DESC LIMIT 1;");
        $stmt->execute();
        $id = $stmt->fetch();

        if (!isset($id))
            $id = 1;
        else
            $id = $id["ID"];
        // echo $id."\n";

        $stmt = $pdo->prepare("SELECT location FROM api_regional_files WHERE language = ? LIMIT 1;");
        $stmt->execute([$language]);
        $sheets_url = $stmt->fetch();
        $sheets_url = $sheets_url["location"];
        // echo $sheets_url."\n";

        $obj = new SplFileObject($sheets_url);
        $obj->setFlags(SplFileObject::READ_CSV);
        $norewind = new NoRewindIterator($obj);
        $caching  = new CachingIterator($norewind);
        $limit    = new LimitIterator($caching, $id+1);

        if(!ini_set('default_socket_timeout', 15)) echo "<!-- unable to change socket timeout -->";

        // $spreadsheet_data = array();
        foreach ($limit as $data)
        {
            // echo $data[0];
            // if (strlen($data[1])==0){
            //     echo $data[1];
            //     break;
            // }
            $stmt = $pdo->prepare("INSERT IGNORE INTO ".$table_name." (ID, HEADLINE, DESCRIPTION, ELIGIBILITY, REQUIREMENTS, BENEFITS, COST, DEADLINE, IMAGE, LINK, TAGS, SUBTAGS, NAME, NAMELINK) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            // print_r(strtotime($data[7]));
            // echo $data[7];
            $stmt->execute([$data[0], $data[1], $data[2], $data[3], $data[4], $data[5], $data[6], date_create_from_format("m-d-Y", $data[7])->format('Y-m-d'), $data[8], $data[9], $data[10], $data[11], $data[12], $data[13]]);
        }

    }

    // $sheets_url = "https://docs.google.com/spreadsheets/d/e/2PACX-1vSbOfLmDmhL4h0FFbMiESQpfgpVa4SwNglVC-_ONTsMLxrlwrYSTwmoE0BJgc__UURReHrxauv5Wskh/pub?gid=0&single=true&output=csv";
    
    // echo json_encode($spreadsheet_data);
?>