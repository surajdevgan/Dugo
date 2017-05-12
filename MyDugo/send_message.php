
 
 <?php
$response = array();
$city=$_POST['city'];
$seeker_id=$_POST['id'];
$time=$_POST['time'];
$date = $_POST['date'];
$bloodgrp = $_POST['blood'];
$token = $_POST['token'];
include('dbconfig.php');
include('fcm.php');


$fcm=new GCM();
$tokens=array();

$result = @mysql_query("INSERT INTO Request values(null, '$seeker_id','$bloodgrp','null','$city','$time','$date')");

$last_id=@mysql_insert_id();
//$token = @mysql_query("SELECT TOKEN from UserDetail where ID = $seeker_id");  

$resNoti=@mysql_query("SELECT TOKEN FROM UserDetail where CITY='$city' and BLOOD_GROUP = '$bloodgrp' and not TOKEN = '$token'");

$message="Lets do a good Deed by Donating Blood "."-".$seeker_id."-".$last_id;
$noti = array("message" => $message);

if(@mysql_num_rows($resNoti)>0 and $result){
	while ($row=@mysql_fetch_array($resNoti)) {
		$dev_id=$row['TOKEN'];
		array_push($tokens, $dev_id);
	}
	$fcm->send_notification($tokens,$noti);
	
	$response['UserId']=$last_id;
	$response["id"] = $seeker_id;
	$response["success"] = 1;
	$response["message"] = "Notification Sent";
}else{
	$response["success"] = 0;
	$response["message"] = "Notification Not Sent";
}

echo json_encode($response);
?>					 