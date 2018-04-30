<?php
	include  'connect_to_db.php';
	$user_id = $_POST['user_id'];
	$statement = $pdo->prepare("CREATE TABLE IF NOT EXISTS api_user_alexa (id INT AUTO_INCREMENT, user_id VARCHAR(255), referral_code VARCHAR(10),PRIMARY KEY(user_id), KEY(id), UNIQUE (referral_code));");
	$statement->execute();

	function generateRandomString($length = 5) {
	    $characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
	    $charactersLength = strlen($characters);
	    $randomString = '';
	    for ($i = 0; $i < $length; $i++) {
	        $randomString .= $characters[rand(0, $charactersLength - 1)];
	    }
	    return $randomString;
	}

	$statement = $pdo->prepare("INSERT IGNORE INTO api_user_alexa (user_id, referral_code) VALUES (?, ?);");
	$statement->execute([$user_id, generateRandomString()]);

	$stmt = $pdo->prepare("SELECT referral_code FROM api_user_alexa WHERE user_id = '$user_id' LIMIT 1;");
    $stmt->execute();
    $data = $stmt->fetch(PDO::FETCH_ASSOC);
    $stmt->closeCursor();
    echo $data['referral_code'];
	
?>