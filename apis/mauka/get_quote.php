<?php
	include  'connect_to_db.php';

	$stmt = $pdo->prepare("SELECT quote FROM api_quote LIMIT 1;");
    $stmt->execute();
    $data = $stmt->fetch(PDO::FETCH_ASSOC);
    echo $data['quote'];
    $stmt->closeCursor();
    // header("content-type:application/json");
	
?>