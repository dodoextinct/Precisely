<?php
	include 'connect_to_db.php';
	// echo $_POST["location"];
	if ($_SERVER['REQUEST_METHOD']=='POST'){
	if (!empty($_POST["location"]) || !empty($_POST["name"])) {
		$location = $_POST["location"];
		$object_name = $_POST["name"];

		$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS object_details (id INT AUTO_INCREMENT, object_name VARCHAR(255), location VARCHAR(255), PRIMARY KEY(object_name), KEY(id));");
		$statement->execute();

		$statement = $pdo->prepare("REPLACE INTO object_details (object_name, location) VALUES (?, ?);");
		$statement->execute([$object_name, $location]);
		echo "SUCCESS!";
	}else{
		echo "No args provided";
	}
}else{
	echo "string";
}
	
?>