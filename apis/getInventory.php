<?php
	include 'connect_to_db.php';
    $seller_id = $_POST["seller_id"];
    $count = $_POST["count"];
    $page = $_POST["page"];

    $statement = $pdo->query("SELECT seller_id FROM api_seller_relational WHERE seller_app_id = '$seller_id' LIMIT 1");
    if ($statement->fetchColumn()>0) {
        $statement = $pdo->prepare("SELECT seller_id FROM api_seller_relational WHERE seller_app_id = '$seller_id' LIMIT 1");
        foreach ($pdo->query("SELECT seller_id FROM api_seller_relational WHERE seller_app_id = '$seller_id' LIMIT 1") as $row) {
            $seller_id = $row['seller_id'];
            break;
        }
        $statement->closeCursor();

        $initial = $page*$count;
        $final = $count + ($page*$count);
        $sql_01 = "SELECT A.qty, F.attribute_id AS A1, F.value AS V1, B.attribute_id AS A4, B.value AS V4, D.attribute_id AS A2, D.value AS V2, E.attribute_id AS A3, E.value AS V3, B.entity_id, C.sku FROM catalog_product_entity_text AS B INNER JOIN catalog_product_entity_varchar AS D ON D.entity_id = B.entity_id INNER JOIN catalog_product_entity_varchar AS F ON F.entity_id = B.entity_id INNER JOIN catalog_product_entity_decimal AS E ON E.entity_id = B.entity_id INNER JOIN (SELECT sku, entity_id FROM catalog_product_entity) C ON B.entity_id = C.entity_id INNER JOIN cataloginventory_stock_item AS A ON A.product_id = B.entity_id WHERE D.attribute_id = 71 AND E.attribute_id = 75 AND F.attribute_id = 86 AND B.attribute_id = 133 AND B.value = $seller_id ORDER BY entity_id LIMIT $initial, $final";


        $stmt = $pdo->prepare($sql_01);
        $stmt->execute();
        $data = $stmt->fetchAll(PDO::FETCH_ASSOC);
        $stmt->closeCursor();
        header("content-type:application/json");
        echo json_encode($data);
    }else{
        echo "no seller found";
    }

?>