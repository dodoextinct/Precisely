<?php
	include  'connect_to_db.php';

    $sql = "SELECT a.id, a.seller_name, a.contact_person, b.seller_id, b.seller_sku FROM api_seller_contact_info AS a INNER JOIN api_seller_relational AS b ON a.id = b.seller_app_id";

    $stmt = $pdo->prepare($sql);
    $stmt->execute();
    $data = $stmt->fetchAll(PDO::FETCH_ASSOC);
    $stmt->closeCursor();
    header("content-type:application/json");
    echo json_encode($data);

?>