<?php
	include  'connect_to_db.php';
	$user_id = $_POST['user_id'];
	$post_id = $_POST['post_id'];

	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_post_viewed (id INT AUTO_INCREMENT, user_id VARCHAR(255), post_id VARCHAR(255), PRIMARY KEY(id), UNIQUE abc(user_id, post_id));");
	$statement->execute();
	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_deleted (id INT AUTO_INCREMENT, user_id INT NOT NULL, event_id INT NOT NULL, PRIMARY KEY(id));");
	$statement->execute();

	$statement = $pdo->prepare("INSERT IGNORE INTO api_user (user_id, post_id) VALUES (?, ?);");
	if ($statement->execute([$user_id, $post_id])) {
		echo "SUCCESS";
	}else{
		echo "FAILURE";
	}
	
?>