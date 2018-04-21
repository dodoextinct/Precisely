<?php
	include  'connect_to_db.php';
	$languageDetails = $_POST['languageDetails'];
	$file_location = $_POST['location'];

	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_languages (id INT AUTO_INCREMENT, languageDetails VARCHAR(255) NOT NULL, PRIMARY KEY(languageDetails), KEY(id));");
	$statement->execute();
	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_regional_files (id INT AUTO_INCREMENT, location VARCHAR(1023) NOT NULL, languageDetails VARCHAR(255) NOT NULL, PRIMARY KEY(languageDetails), KEY(id));");
	$statement->execute();

	$statement = $pdo->prepare("REPLACE INTO api_regional_files (languageDetails, location) VALUES (?, ?);");
	if ($statement->execute([$languageDetails, $file_location])) {
		echo "SUCCESS";
	}else{
		echo "FAILURE";
	}
?>