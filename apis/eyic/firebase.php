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

$message = $_POST["message"];
$data = array('message'=>$message);
$multi_tokens = array();

//FCM api URL
$url = 'https://fcm.googleapis.com/fcm/send';

if ($statement = $pdo->prepare("SELECT token FROM api_eyic_firebase_id;")){

    $statement->execute();
    foreach ($statement->fetchAll(PDO::FETCH_ASSOC) as $row) {
    	$multi_tokens[] = $row['token'];
	}
}

//api_key available in Firebase Console -> Project Settings -> CLOUD MESSAGING -> Server key
$server_key = 'AAAAcbPE14k:APA91bG-iNVdhqNPk-2yrvj6I2_JBOFw38d2ume7a9idBL27bXbqpdXhP5zhtwbXJ__NAednbhaazmZL-uYAIY_bApGYVL3jWMcgeAhDOvVTtcdp440wUsbc8nTPKYILhsRC3_YCdW4k';
			
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