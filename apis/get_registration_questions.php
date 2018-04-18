<?php
	include  'connect_to_db.php';

	$sql = "SELECT question, answer_type FROM api_registration_questions";

    if ($stmt = $pdo->prepare($sql))
    {
        $stmt->execute();
        $data = $stmt->fetchAll(PDO::FETCH_ASSOC);
        $stmt->closeCursor();

        header("content-type:application/json");

        echo json_encode($data);
    }
?>