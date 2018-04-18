<?php
	include  'connect_to_db.php';

    $sql_01 = "SELECT qty FROM cataloginventory_stock_item;";

    $stmt = $pdo->prepare($sql_01);
    $stmt->execute();
    $data = $stmt->fetchAll(PDO::FETCH_ASSOC);
    $stmt->closeCursor();
    echo ($data[1]+"");

?>