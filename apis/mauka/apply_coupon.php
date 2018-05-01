<?php
	include  'connect_to_db.php';
	$user_id = $_POST['user_id'];
	$coupon_code = $_POST['coupon_code'];
	$stmt = $pdo->prepare("CREATE TABLE IF NOT EXISTS coupons_applied (id INT AUTO_INCREMENT, coupon_code VARCHAR(255), user_id VARCHAR (10), UNIQUE abc(coupon_code, user_id), PRIMARY KEY(id));");
	$stmt->execute();
	$stmt = $pdo->prepare("SELECT coupon_code FROM coupons WHERE coupon_code = '$coupon_code' LIMIT 1;");
	$stmt->execute();
    $data = $stmt->fetch(PDO::FETCH_ASSOC);
    if (!empty($data)) {
    	$stmt = $pdo->prepare("INSERT IGNORE INTO coupons_applied (coupon_code, user_id) VALUES (?, ?)");
	    $stmt->execute([$coupon_code, $user_id]);
	    $stmt = $pdo->prepare("SELECT coupon_code FROM coupons_applied WHERE coupon_code = '$coupon_code' LIMIT 1;");
	    $stmt->execute();
	    $data = $stmt->fetch(PDO::FETCH_ASSOC);
	    if (!empty($data)) {
	    	echo $coupon_code;
	    }
    }
?>