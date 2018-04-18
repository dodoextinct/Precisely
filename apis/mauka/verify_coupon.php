<?php
	include  'connect_to_db.php';
	$coupon_code = $_POST['coupon_code'];
	$stmt = $pdo->prepare("CREATE TABLE IF NOT EXISTS coupons (id INT AUTO_INCREMENT, coupon_code VARCHAR(255), PRIMARY KEY(coupon_code), KEY(id));");
	$stmt->execute();
	$stmt = $pdo->prepare("SELECT coupon_code FROM coupons WHERE coupon_code = '$coupon_code' LIMIT 1;");
    $stmt->execute();
    $data = $stmt->fetch(PDO::FETCH_ASSOC);
    $stmt->closeCursor();
    header("content-type:application/json");
    echo $data['coupon_code'];
?>