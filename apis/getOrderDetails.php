<?php
	include  'connect_to_db.php';
    $seller_id = $_POST["seller_id"];
    $order_id = $_POST["order_id"];

    $sql = "SELECT A.increment_id, A.status, A.customer_firstname, A.customer_lastname, A.customer_email, A.created_at, B.sku, B.name, B.qty_ordered, C.postcode, C.street, C.city FROM sales_flat_order AS A INNER JOIN sales_flat_order_item AS B ON A.entity_id = B.order_id INNER JOIN sales_flat_order_address AS C ON A.entity_id = C.parent_id WHERE A.increment_id = $order_id";


    $stmt = $pdo->prepare($sql);
    $stmt->execute();
    $data = $stmt->fetchAll(PDO::FETCH_ASSOC);
    $stmt->closeCursor();
    header("content-type:application/json");
    echo json_encode($data);

?>