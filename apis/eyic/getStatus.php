<?php
	include  'connect_to_db.php';

	$statement = $pdo->prepare("SELECT status FROM api_eyic_completed;");
	$statement->execute();
	if ($statement->rowCount()>0) {
	    foreach ($statement->fetchAll(PDO::FETCH_ASSOC) as $row) {
			echo $row['status'];
			break;
		}
	}else{
		echo '1';
	}
	
	
?>