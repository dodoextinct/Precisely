<?php
	include  'connect_to_db.php';
	$user_id = $_POST['user_id'];
	$post_id = $_POST['post_id'];

	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_post_viewed (id INT AUTO_INCREMENT, user_id VARCHAR(255), post_id VARCHAR(255), PRIMARY KEY(id), KEY(id));");
	$statement->execute();
	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_deleted (id INT AUTO_INCREMENT, user_id INT NOT NULL, event_id INT NOT NULL, PRIMARY KEY(id));");
	$statement->execute();

	$statement = $pdo->prepare("INSERT IGNORE INTO api_user (user_name, user_id, user_image) VALUES (?, ?, ?);");
	$statement->execute([$user_name, $user_id,$image]);

	$stmt = $pdo->prepare("SELECT id FROM api_user WHERE user_id = '$user_id' LIMIT 1;");
    $stmt->execute();
    $data = $stmt->fetch(PDO::FETCH_ASSOC);
    $stmt->closeCursor();
    echo $data['id'];
	
?>