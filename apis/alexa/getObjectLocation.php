<?php
	include  'connect_to_db.php';
	$object_name = $_GET["object"];
	$stmt = $pdo->prepare("SELECT location FROM object_details WHERE object_name = '$object_name';");
    $stmt->execute();
    $data = $stmt->fetch(PDO::FETCH_ASSOC);
    $stmt->closeCursor();
    if ($stmt->rowCount() > 0) {
    	echo ("The ".$object_name." is on the ".$data['location']." in your living room.");
    }else{
    	echo ("The ".$object_name." is not in the living room");
    }