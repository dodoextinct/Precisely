<?php
	include  'connect_to_db.php';
	$languageDetails = $_POST['languageDetails'];

	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_languages (id INT AUTO_INCREMENT, languageDetails VARCHAR(255), PRIMARY KEY(languageDetails), KEY(id));");
	$statement->execute();
	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_user_selected_language (id INT AUTO_INCREMENT, user_id INT NOT NULL, language_id INT NOT NULL, PRIMARY KEY(id));");
	$statement->execute();

	$statement = $pdo->prepare("INSERT IGNORE INTO api_languages (languageDetails) VALUES (?);");
	if ($statement->execute([$languageDetails])){
		echo "SUCCESS";
	}else{
		echo "FAILURE";
	}
?>