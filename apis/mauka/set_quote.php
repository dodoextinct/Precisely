<?php
	include  'connect_to_db.php';
	$quote = $_POST['quote'];

	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_quote (id INT AUTO_INCREMENT, quote VARCHAR(255), potus INT NOT NULL, PRIMARY KEY(potus), KEY(id));");
	$statement->execute();

	$statement = $pdo->prepare("REPLACE INTO api_quote (quote, potus) VALUES (?, ?);");
	$statement->execute([$quote, 0]);

	$stmt = $pdo->prepare("SELECT id FROM api_quote WHERE quote = '$quote' LIMIT 1;");
    $stmt->execute();
    $data = $stmt->fetchAll(PDO::FETCH_ASSOC);
    $stmt->closeCursor();
    header("content-type:application/json");
    echo json_encode($data);
	
?>