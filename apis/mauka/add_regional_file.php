<?php
	include  'connect_to_db.php';
	$language = $_POST['language'];
	$file_location = $_POST['location'];

	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_languages (id INT AUTO_INCREMENT, language VARCHAR(255) NOT NULL, PRIMARY KEY(language), KEY(id));");
	$statement->execute();
	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_regional_files (id INT AUTO_INCREMENT, location VARCHAR(1023) NOT NULL, language VARCHAR(255) NOT NULL, PRIMARY KEY(language), KEY(id));");
	$statement->execute();

	$statement = $pdo->prepare("REPLACE INTO api_regional_files (language, location) VALUES (?, ?);");
	if ($statement->execute([$language, $file_location])) {
		echo "SUCCESS";
	}else{
		echo "FAILURE";
	}
?>