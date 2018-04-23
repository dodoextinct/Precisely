<?php
	include  'connect_to_db.php';
	$versionCode = $_POST['versionCode'];
	$stmt = $pdo->prepare("CREATE TABLE IF NOT EXISTS current_version (id INT AUTO_INCREMENT, version_code VARCHAR(255), PRIMARY KEY(version_code), KEY(id));");
	$stmt->execute();

	$statement = $pdo->prepare("INSERT IGNORE INTO current_version (version_code) VALUES (?);");
	$statement->execute([$versionCode]);
?>
