<?php
	include  'connect_to_db.php';
	$id = $_POST["id"];


	$statement = $pdo->prepare("SELECT seller_sku FROM api_seller_relational WHERE seller_app_id = '$id' LIMIT 1");
	$statement->execute();
	if ($statement->rowCount()>0) {
    	foreach ($statement->fetchAll(PDO::FETCH_ASSOC) as $row) {
    		echo ($row['seller_sku']);
		}
    	$statement->closeCursor();
	}else{
		echo ("no");
	}
?>