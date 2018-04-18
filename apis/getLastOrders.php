<?php
	include 'connect_to_db.php';
    $sku = $_POST["sku"];
    $count = $_POST["count"];
    $page = $_POST["page"];
    $sku = $sku."%";
    $initial = $page*$count;
    $final = $count + ($page*$count);
    // echo $sku;
	$sql = "SELECT a.updated_at, a.sku, a.name, a.qty_ordered, a.price_incl_tax, b.increment_id, b.status, c.value AS image FROM sales_flat_order_item AS a INNER JOIN sales_flat_order AS b ON a.order_id = b.entity_id INNER JOIN ( SELECT j.attribute_id, j.value, k.sku FROM catalog_product_entity_varchar AS j INNER JOIN catalog_product_entity AS k ON j.entity_id = k.entity_id WHERE j.attribute_id =86 ) AS c ON c.sku = a.sku WHERE a.sku LIKE  '$sku' AND a.qty_invoiced >0 AND b.entity_id IN ( SELECT order_id FROM sales_flat_order_item WHERE sku LIKE  '$sku') LIMIT $initial, $final";


    if ($stmt = $pdo->prepare($sql))
    {
        $stmt->execute();
        $data = $stmt->fetchAll(PDO::FETCH_ASSOC);
        $stmt->closeCursor();

        header("content-type:application/json");

        echo json_encode($data);
    }
?>