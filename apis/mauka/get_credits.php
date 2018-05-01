<?php
    include  'connect_to_db.php';

    $user_id = $_POST["id"];

    $stmt = $pdo->prepare("CREATE TABLE IF NOT EXISTS coupons (id INT AUTO_INCREMENT, coupon_code VARCHAR(255), credits VARCHAR(255), PRIMARY KEY(coupon_code), KEY(id));");
	$stmt->execute();
	$stmt = $pdo->prepare("CREATE TABLE IF NOT EXISTS coupons_applied (id INT AUTO_INCREMENT, coupon_code VARCHAR(255), user_id VARCHAR (10), UNIQUE abc(coupon_code, user_id), PRIMARY KEY(id));");
	$stmt->execute();

	$stmt = $pdo->prepare("SELECT SUM(credits) AS credit FROM coupons WHERE coupon_code IN (SELECT coupon_code FROM coupons_applied WHERE user_id = '$user_id');");
	$stmt->execute();
	$data = $stmt->fetch(PDO::FETCH_ASSOC);
	echo $data["credit"];

?>