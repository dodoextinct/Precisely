<?php
	include  'connect_to_db.php';
	$sid = $_POST["id"];
	$vat = $_POST["vat"];
	$cst = $_POST["cst"];
	$permanent = $_POST["permanent"];
	$pickup = $_POST["pickup"];
	$state_operation = $_POST["state_operation"];
	$categories = $_POST["categories"];

	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_seller_official_details (id INT AUTO_INCREMENT, vat VARCHAR(255), cst VARCHAR(255), permanent VARCHAR(255), pickup VARCHAR(255), state_operation VARCHAR(255), categories VARCHAR(255), seller_id INT NOT NULL, FOREIGN KEY(seller_id) REFERENCES api_seller_contact_info(id), PRIMARY KEY(id));");
	$statement->execute();

	$statement = $pdo->query("SELECT id FROM api_seller_official_details WHERE seller_id = '$sid';");

    if($statement->fetchColumn()>0){
    	$statement = $pdo->prepare("UPDATE api_seller_official_details SET vat = '$vat', cst = '$cst', permanent = '$permanent', pickup = '$pickup', state_operation = '$state_operation', categories = '$categories' WHERE seller_id = '$sid';");
		$statement->execute();
		echo json_encode(array('output' => "updated"));
    }else{
		$statement = $pdo->prepare("INSERT INTO api_seller_official_details (vat, cst, permanent, pickup, state_operation, seller_id, categories) VALUES ('$vat', '$cst', '$permanent', '$pickup', '$state_operation', $sid, '$categories');");
		$statement->execute();

		echo json_encode(array('output' => "updated"));
	}
	
?>