<?php
	include  'connect_to_db.php';
	$question = $_POST["question"];
	$answer_type = $_POST["answer_type"];

	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_registration_questions (id INT AUTO_INCREMENT, question VARCHAR(255), answer_type TEXT, PRIMARY KEY(id));");
	$statement->execute();

	$statement = $pdo->prepare("INSERT INTO api_registration_questions (question, answer_type) VALUES ('$question', '$answer_type')");
	$statement->execute();

	echo ("updated");

?>