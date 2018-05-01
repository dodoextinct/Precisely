<?php
	include  'connect_to_db.php';
	$user_id = $_POST['id'];
	$versionCode = $_POST['alexa_code'];
	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_user_alexa (id INT AUTO_INCREMENT, user_id VARCHAR(255), referral_code VARCHAR(10),PRIMARY KEY(user_id), KEY(id), UNIQUE (referral_code));");
	$statement->execute();

	$stmt = $pdo->prepare("SELECT id FROM api_user_alexa WHERE referral_code = '$versionCode';");
    $stmt->execute();
    $data = $stmt->fetch(PDO::FETCH_ASSOC);
    if (!empty($data)){
    	echo "SUCCESS";
    }else{
    	echo "FAILURE";
    }
	
?>