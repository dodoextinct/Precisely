<?php
	include  'connect_to_db.php';
	$id = $_POST["id"];
	$status = $_POST["status"];

	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_eyic_completed (id INT AUTO_INCREMENT, user VARCHAR(5), status VARCHAR(5), PRIMARY KEY(id));");
	$statement->execute();

	$statement = $pdo->prepare("DELETE FROM api_eyic_completed;");
	$statement->execute();

	$statement = $pdo->prepare("INSERT INTO api_eyic_completed (user, status) VALUES ('$id', '$status');");
	$statement->execute();
	echo "upadted";
	
?>