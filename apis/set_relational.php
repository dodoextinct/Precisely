<?php
	include  'connect_to_db.php';
	$seller_app_id = $_POST["seller_app_id"];
	$seller_id = $_POST["seller_id"];
	$sku = $_POST["sku"];

	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_seller_relational (id INT AUTO_INCREMENT, seller_app_id INT, seller_id INT, seller_sku VARCHAR(255), PRIMARY KEY(id));");
	$statement->execute();
	$statement = $pdo->prepare("INSERT INTO api_seller_relational (seller_app_id, seller_id, seller_sku) VALUES ('$seller_app_id', '$seller_id', '$sku');");
	$statement->execute();

	echo ("updated");
	

	// $rows = array();
	// while($r = mysql_fetch_assoc($query)) {
 //    	$rows[] = $r;
	// }
	// print json_encode($rows);
	// mysql_close($conn);

?>