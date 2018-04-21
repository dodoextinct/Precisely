<?php
    include  'connect_to_db.php';

    $user_id = $_POST["id"];

    $statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_event_starred (id INT AUTO_INCREMENT, user_id VARCHAR(255), post_id VARCHAR(255), PRIMARY KEY(id), UNIQUE abc(user_id, post_id));");
	$statement->execute();

    $statement = $pdo->prepare("SELECT * FROM api_event_starred WHERE user_id = ?");
    $statement->execute([$user_id]);

    $data = $statement->fetchAll(PDO::FETCH_ASSOC);
    echo json_encode($data);
?>