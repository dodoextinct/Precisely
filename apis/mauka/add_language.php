<?php
	include  'connect_to_db.php';
	$language = $_POST['language'];

	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_languages (id INT AUTO_INCREMENT, language VARCHAR(255), PRIMARY KEY(language), KEY(id));");
	$statement->execute();
	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_user_selected_language (id INT AUTO_INCREMENT, user_id INT NOT NULL, language_id INT NOT NULL, PRIMARY KEY(id));");
	$statement->execute();

	$statement = $pdo->prepare("INSERT IGNORE INTO api_languages (language) VALUES (?);");
	if ($statement->execute([$language])){
		echo "SUCCESS";
	}else{
		echo "FAILURE";
	}
?>