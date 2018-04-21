<?php
	include  'connect_to_db.php';
	$user_id = $_POST['user_id'];
	$post_id = $_POST['post_id'];
	$action = $_POST['action'];

	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_event_starred (id INT AUTO_INCREMENT, user_id VARCHAR(255), post_id VARCHAR(255), PRIMARY KEY(id), UNIQUE abc(user_id, post_id));");
	$statement->execute();
	if ($action == "1") {
		$statement = $pdo->prepare("INSERT IGNORE INTO api_event_starred (user_id, post_id) VALUES (?, ?);");
		if ($statement->execute([$user_id, $post_id])) {
			echo "SUCCESS";
		}else{
			echo "FAILURE";
		}
	}else{
		$statement = $pdo->prepare("DELETE FROM api_event_starred WHERE user_id = ?, post_id = ?");
		if ($statement->execute([$user_id, $post_id])) {
			echo "SUCCESS";
		}else{
			echo "FAILURE";
		}
	}
	
?>