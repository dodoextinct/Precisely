<?php
    include  'connect_to_db.php';

    $user_id = $_POST["id"];

    $statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_languages (id INT AUTO_INCREMENT, languageDetails VARCHAR(255), PRIMARY KEY(languageDetails), KEY(id));");
    $statement->execute();

    $statement = $pdo->prepare("SELECT * FROM api_languages");
    $statement->execute();

    $languages = $statement->fetchAll(PDO::FETCH_ASSOC);
    echo json_encode($languages);
?>