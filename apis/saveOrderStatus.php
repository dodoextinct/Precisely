<?php
	include  'connect_to_db.php';
	$sku_product = $_POST["sku_product"];
	$order_id = $_POST["order_id"];

	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_order_status (id INT AUTO_INCREMENT, sku_product VARCHAR(25), order_id VARCHAR(25), PRIMARY KEY(id));");
	$statement->execute();

	$statement = $pdo->prepare("INSERT INTO api_order_status (sku_product, order_id) VALUES ('$sku_product', '$order_id');");
	$statement->execute();

	echo ("updated");

?>