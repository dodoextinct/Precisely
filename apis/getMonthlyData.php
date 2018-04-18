<?php
	include 'connect_to_db.php';
	date_default_timezone_set('Asia/Kolkata');
	$sku = $_POST["sku"];
	$sku = $sku."%";
	$sql = "SELECT a.updated_at, a.qty_invoiced, b.entity_id, b.status FROM sales_flat_order_item AS a INNER JOIN sales_flat_order AS b ON a.order_id = b.entity_id WHERE a.sku LIKE '$sku' AND a.qty_invoiced >0 AND b.entity_id IN ( SELECT order_id FROM sales_flat_order_item WHERE sku LIKE '$sku') AND b.status like 'Complete'";
	if ($statement = $pdo->prepare($sql))
    {
    	$dates = array();
		$statement->execute();
		if ($statement->rowCount()>0) {
		    foreach ($statement->fetchAll(PDO::FETCH_ASSOC) as $row) {
		    	if (($timestamp = strtotime($row['updated_at'])) !== false) {
		    		$dates[] = date('F', $timestamp);
		    	}
		        // echo ($row['updated_at']);
		    }
		    $statement->closeCursor();
		    header("content-type:application/json");
		    echo json_encode(array_values($dates));
		}else{
		    echo ("no order complete");
		}
	}

?>