<?php
	include  'connect_to_db.php';

	$token = $_POST["firebase_token"];

	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_eyic_firebase_id (id INT AUTO_INCREMENT, token VARCHAR(255), PRIMARY KEY(id));");
	$statement->execute();

	$statement = $pdo->prepare("INSERT INTO api_eyic_firebase_id (token) VALUES ('$token');");
	$statement->execute();

	// $id = '1';

	// if ($statement = $pdo->prepare("SELECT id FROM api_eyic_firebase_id WHERE token = '$token' LIMIT 1;")){
	//     $statement->execute();
	//     foreach ($statement->fetchAll(PDO::FETCH_ASSOC) as $row) {
	//     	$id = $row['id'];
	//     	break;
	// 	}
	// }

	$stmt = $pdo->prepare("SELECT id FROM api_eyic_firebase_id WHERE token = '$token' LIMIT 1;");
    $stmt->execute();
    $data = $stmt->fetchAll(PDO::FETCH_ASSOC);
    $stmt->closeCursor();
    header("content-type:application/json");
    echo json_encode($data);
	
?>