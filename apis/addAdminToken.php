<?php
	include  'connect_to_db.php';
	$token = $_POST["firebase_token"];
	$id = $_POST["id"];

	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_admin_firebase_id (id INT AUTO_INCREMENT, app_seller_id VARCHAR(255), token VARCHAR(255), PRIMARY KEY(id));");
	$statement->execute();

	$statement = $pdo->prepare("DELETE FROM api_sellers_firebase_id WHERE token = '$token';");
	$statement->execute();

	$statement = $pdo->prepare("INSERT INTO api_admin_firebase_id (app_seller_id, token) VALUES ('$id', '$token');");
	$statement->execute();
	echo ("UPDATED");
	
?>