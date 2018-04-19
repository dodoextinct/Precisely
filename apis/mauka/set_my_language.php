<?php
	include  'connect_to_db.php';
	$user_id = $_POST['user_id'];
	$language_id = $_POST['language_id'];

	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_user_selected_language (id INT AUTO_INCREMENT, user_id INT NOT NULL, language_id INT NOT NULL, PRIMARY KEY(user_id), KEY(id));");
	$statement->execute();

	$statement = $pdo->prepare("REPLACE INTO api_user_selected_language (user_id, language_id) VALUES (?, ?);");
	if ($statement->execute([$user_id, $language_id])) {
		echo "SUCCESS";
	}else{
		echo "FAILURE";
	}
	
?>