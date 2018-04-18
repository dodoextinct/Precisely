<?php
	include  'connect_to_db.php';

	$user_id = $_POST["user_id"];
	$event_id = $_POST["event_id"];

	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_deleted (id INT AUTO_INCREMENT, user_id INT NOT NULL, event_id INT NOT NULL, PRIMARY KEY(id));");
	$statement->execute();
	$statement = $pdo->prepare("INSERT INTO api_deleted (user_id, event_id) VALUES (?, ?);");
	$statement->execute([$user_id, $event_id]);
	
?>