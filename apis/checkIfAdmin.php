<?php
	include  'connect_to_db.php';

	$id = $_POST['id'];

    $statement = $pdo->query("SELECT * FROM api_admin_firebase_id WHERE app_seller_id = '$id';");

    if($statement->fetchColumn()>0){
		echo "1";
    }else{
    	echo "0";
    }

?>