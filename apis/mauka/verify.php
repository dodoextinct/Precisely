<?php
	include  'connect_to_db.php';
	$user_id = $_POST['id'];
	$versionCode = $_POST['versionCode'];
	$stmt = $pdo->prepare("CREATE TABLE IF NOT EXISTS current_version (id INT AUTO_INCREMENT, version_code VARCHAR(255), PRIMARY KEY(version_code), KEY(id));");
	$stmt->execute();

	$stmt = $pdo->prepare("SELECT version_code FROM current_version WHERE version_code = '$versionCode';");
    $stmt->execute();
    $data = $stmt->fetch(PDO::FETCH_ASSOC);
    if (strcmp($data['version_code'], $versionCode) == 0){
    	$stmt = $pdo->prepare("SELECT id FROM api_user WHERE id = '$user_id' LIMIT 1;");
	    $stmt->execute();
	    $data = $stmt->fetch(PDO::FETCH_ASSOC);
	    $stmt->closeCursor();
	    header("content-type:application/json");
	    echo $data['id'];
    }else{
    	echo "UPDATE";
    }
	
?>