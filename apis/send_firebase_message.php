<?php
/*  
Parameter Example
	$data = array('post_id'=>'699','post_title'=>'first notif');
	$target = 'single token id or topic name';
	or
	$target = array('token1','token2','...'); // up to 1000 in one request
*/
	//maine yahan par testing ke liye target hardcode kr diya but target bhi sql query ke through milega from the database jahan bhejna h notif uske hisaab se
	//target is obtained from onTokenRefresh() method in MyFirebaseInstanceIDService in android and then stored in server against each user

include  'connect_to_db.php';
date_default_timezone_set('Indian/Maldives');

$message = $_POST["message"];
$send_to = $_POST["send_to"];
$my_id = $_POST["my_id"];
$my_name = $_POST["my_name"];
$chat_id = $my_id;

$multi_tokens = array();
if ($send_to != 'admin') {
	if ($statement = $pdo->prepare("SELECT token FROM api_sellers_firebase_id WHERE seller_id = $send_to;")){
	    $statement->execute();
	    foreach ($statement->fetchAll(PDO::FETCH_ASSOC) as $row) {
	    	$multi_tokens[] = $row['token'];
		}
		if ($statement = $pdo->prepare("SELECT token FROM api_admin_firebase_id WHERE app_seller_id != $my_id"))
		{
		    $statement->execute();
		    foreach ($statement->fetchAll(PDO::FETCH_ASSOC) as $row) {
		    	$multi_tokens[] = $row['token'];
			}
		    $statement->closeCursor();
			$chat_id = $send_to;
		}
	}
}else{
	if ($statement = $pdo->prepare("SELECT token FROM api_admin_firebase_id"))
	{
	    $statement->execute();
	    foreach ($statement->fetchAll(PDO::FETCH_ASSOC) as $row) {
	    	$multi_tokens[] = $row['token'];
		}
	    $statement->closeCursor();
	}
}
$data = array('message'=>$message, "chat_id"=>$chat_id, "name"=>$my_name);

//FCM api URL
$url = 'https://fcm.googleapis.com/fcm/send';
//api_key available in Firebase Console -> Project Settings -> CLOUD MESSAGING -> Server key
$server_key = 'AAAAjyixb8Y:APA91bEXdBAN9XQg2b00hx2ny3DI68AK6UK8UANOCeLmxkhbU9MvU-7cPm3xk3njQIIZ2Cb6t6AwbBuJGk8qvy0Rfm9VEEXng9l4wkDY5YbOqvdjXYUL8WK3UMyKeOyYoYHkCvz8xzbj';


$fields = array();
$fields['data'] = $data;
if(is_array($multi_tokens)){
	$fields['registration_ids'] = $multi_tokens;
}else{
	$fields['to'] = $multi_tokens;
}
//header with content_type api key
$headers = array(
	'Content-Type:application/json',
  'Authorization:key='.$server_key
);
			
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_POST, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
$result = curl_exec($ch);
if ($result === FALSE) {
	die('FCM Send Error: ' . curl_error($ch));
}
curl_close($ch);
echo $result;

?>