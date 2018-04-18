<?php
	include  'connect_to_db.php';

    $id = $_POST["id"];
        
    $sql = "SELECT a.seller_name, a.email_id, a.contact_person, a.mobile_number, b.vat, b.cst, b.permanent, b.pickup, b.state_operation, b.categories FROM api_seller_contact_info AS a INNER JOIN api_seller_official_details AS b ON a.id = b.seller_id WHERE a.id = $id;";

    $stmt = $pdo->prepare($sql);
    $stmt->execute();
    $data = $stmt->fetchAll(PDO::FETCH_ASSOC);
    $stmt->closeCursor();
    header("content-type:application/json");
    echo json_encode($data);
    
?>