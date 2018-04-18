<?php
	include  'connect_to_db.php';
	$seller_name = $_POST["seller_name"];
	$email_id = $_POST["email_id"];
	$contact_person = $_POST["contact_person"];
	$mobile_number = $_POST["mobile_number"];
// 	Create seller info table
	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_seller_contact_info (id INT AUTO_INCREMENT, seller_name VARCHAR(255), email_id VARCHAR(255), contact_person VARCHAR(255), mobile_number VARCHAR(25), UNIQUE (mobile_number), PRIMARY KEY(id));");
	$statement->execute();

// 	Create official details table
	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_seller_official_details (id INT AUTO_INCREMENT, vat VARCHAR(255), cst VARCHAR(255), permanent VARCHAR(255), pickup VARCHAR(255), state_operation VARCHAR(255), categories VARCHAR(255), seller_id INT NOT NULL, FOREIGN KEY(seller_id) REFERENCES api_seller_contact_info(id), PRIMARY KEY(id));");
	$statement->execute();

	$statement = $pdo->prepare("SELECT id FROM api_seller_contact_info WHERE mobile_number = '$mobile_number' LIMIT 1;");
	$statement->execute();
	if ($statement->rowCount()>0) {
	        $data = $statement->fetchAll(PDO::FETCH_ASSOC);
	        $statement->closeCursor();

	        $statement = $pdo->prepare("UPDATE api_seller_contact_info SET seller_name = '$seller_name', email_id = '$email_id', contact_person = '$contact_person' WHERE mobile_number = $mobile_number;");
			$statement->execute();

	        header("content-type:application/json");
	        echo json_encode($data);
	}else{
		$statement = $pdo->prepare("INSERT INTO api_seller_contact_info (seller_name, email_id, contact_person, mobile_number) VALUES ('$seller_name', '$email_id', '$contact_person', '$mobile_number');");
		$statement->execute();


		if ($statement = $pdo->prepare("SELECT id FROM api_seller_contact_info WHERE mobile_number = '$mobile_number' LIMIT 1;"))
	    {
	        $statement->execute();
	        $data = $statement->fetchAll(PDO::FETCH_ASSOC);
	        $statement->closeCursor();
	        foreach ($data as $key) {
	        	$current_id = $key['id'];
	        	$statement = $pdo->prepare("INSERT INTO api_seller_official_details (vat, cst, permanent, pickup, state_operation, seller_id, categories) VALUES (' ', ' ', ' ', ' ', ' ', $current_id, ' ');");
				$statement->execute();
	        	break;
	        }
	        header("content-type:application/json");
	        echo json_encode($data);
        }
	}
	
?>