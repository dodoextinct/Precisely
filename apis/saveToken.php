<?php
	include  'connect_to_db.php';

	$user_id = $_POST["user_id"];
	$token = $_POST["firebase_token"];

	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_sellers_firebase_id (id INT AUTO_INCREMENT, seller_id INT,token VARCHAR(255), PRIMARY KEY(id));");
	$statement->execute();

	$statement = $pdo->prepare("INSERT INTO api_sellers_firebase_id (seller_id, token) VALUES ($user_id, '$token');");
	$statement->execute();
	echo ("UPDATED");
	
?>